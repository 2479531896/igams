package com.matridx.igams.detection.molecule.service.impl;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.detection.molecule.dao.entities.FzjcjgDto;
import com.matridx.igams.detection.molecule.dao.entities.FzjcjgModel;
import com.matridx.igams.detection.molecule.dao.entities.FzzkjgDto;
import com.matridx.igams.detection.molecule.dao.post.IFzjcjgPJDao;
import com.matridx.igams.detection.molecule.service.svcinterface.IFzjcjgPJService;
import com.matridx.igams.detection.molecule.service.svcinterface.IFzzkjgService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.file.upload.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FzjcjgPJServiceImpl extends BaseBasicServiceImpl<FzjcjgDto, FzjcjgModel, IFzjcjgPJDao> implements IFzjcjgPJService {
    @Autowired
    RedisUtil redisUtil;
    @Value("${matridx.fileupload.prefix}")
    private String prefix;
    @Value("${matridx.fileupload.tempPath}")
    private String tempFilePath;

    @Value("${matridx.fileupload.releasePath}")
    private String releaseFilePath;
    @Autowired
    IFjcfbService fjcfbService;
    @Autowired
    private IFzzkjgService fzzkjgService;

    /**
     * 添加分子检测结果信息
     */
    public boolean addDtoList(List<FzjcjgDto> list){
        return dao.addDtoList(list);
    }

    /**
     * 根据分子检测IDs删除结果信息
     */
    public boolean delDtoListByFzxmid(FzjcjgDto fzjcjgDto){
        return dao.delDtoListByFzxmid(fzjcjgDto);
    }
    /**
     * 根据分子项目id查询结果信息
     */
    public List<Map<String, Object>> getFzjcjgListById(String fzxmid){
        return dao.getFzjcjgListById(fzxmid);
    }
    /**
     * 批量更新
     */
    public int updateList(List<FzjcjgDto> list){
        return dao.updateList(list);
    }

    /**
     * 批量新增
     */
    public int insertList(List<FzjcjgDto> list){
        return dao.insertList(list);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean uploadSave(FzjcjgDto fzjcjgDto) {
        List<FzjcjgDto> fzjcjgDtos = JSON.parseArray(fzjcjgDto.getJcjg_json(), FzjcjgDto.class);
        if(fzjcjgDtos!=null&&!fzjcjgDtos.isEmpty()){
            List<String> fjids = fzjcjgDto.getFjids();
            List<String> fzjcids=new ArrayList<>();
            if(fjids!=null&&!fjids.isEmpty()){
                List<FjcfbDto> fjcfbDtos=new ArrayList<>();
                Map<String, List<FzjcjgDto>> listMap = fzjcjgDtos.stream().filter(item-> StringUtil.isNotBlank(item.getFzjcid())).collect(Collectors.groupingBy(FzjcjgDto::getFzjcid));
                if (null != listMap && listMap.size()>0){
                    Iterator<Map.Entry<String, List<FzjcjgDto>>> entries = listMap.entrySet().iterator();
                    while (entries.hasNext()) {
                        Map.Entry<String, List<FzjcjgDto>> entry = entries.next();
                        String key= entry.getKey();
                        fzjcids.add(key);
                        for(String fjid:fjids){
                            Map<Object,Object> mFile = redisUtil.hmget("IMP_:_"+fjid);
                            //文件路径
                            String pathString = (String)mFile.get("fwjlj");
                            //文件名
                            String wjm = (String)mFile.get("wjm");
                            //分文件名
                            String fwjm = (String)mFile.get("fwjm");
                            //文件全路径
                            String wjlj = (String)mFile.get("wjlj");
                            //业务类型
                            String ywlx = (String)mFile.get("ywlx");

                            //根据临时文件夹创建正式文件
                            String t_path = pathString.substring(prefix.length() + tempFilePath.length());
                            //分文件路径
                            String real_path = prefix + releaseFilePath + t_path;

                            mkDirs(real_path);
                            String new_fwjm=System.currentTimeMillis() + RandomUtil.getRandomString() + fwjm.split("\\.")[1];
                            String filePath=real_path+"/"+new_fwjm;
                            //把文件从临时文件夹中移动到正式文件夹中，如果已经存在则覆盖
                            CutFile(wjlj,filePath);
                            //将正式文件夹路径更新至数据库
                            DBEncrypt bpe = new DBEncrypt();
                            FjcfbDto fjcfbDto = new FjcfbDto();
                            fjcfbDto.setFjid(StringUtil.generateUUID());
                            fjcfbDto.setYwid(key);
                            fjcfbDto.setXh("1");
                            fjcfbDto.setYwlx(ywlx);
                            fjcfbDto.setWjm(wjm);
                            fjcfbDto.setWjlj(bpe.eCode(filePath));
                            fjcfbDto.setFwjlj(bpe.eCode(real_path));
                            fjcfbDto.setFwjm(bpe.eCode(new_fwjm));
                            fjcfbDto.setZhbj("0");
                            fjcfbDto.setLrry(fzjcjgDto.getLrry());
                            fjcfbDtos.add(fjcfbDto);
                        }
                    }
                    FjcfbDto fjcfbDto=new FjcfbDto();
                    fjcfbDto.setYwids(fzjcids);
                    fjcfbDto.setYwlx(BusTypeEnum.IMP_FILE_FLU_TEMEPLATE.getCode());
                    fjcfbService.deleteByYwidsAndYwlx(fjcfbDto);
                    boolean batched = fjcfbService.batchInsertFjcfb(fjcfbDtos);
                    if(!batched){
                        return false;
                    }
                }
            }
            FzjcjgDto fzjcjgDto_t=new FzjcjgDto();
            fzjcjgDto_t.setIds(fzjcids);
            dao.delete(fzjcjgDto_t);
            int inserted = dao.insertList(fzjcjgDtos);
            if(inserted==0){
                return false;
            }
        }
        List<FzzkjgDto> fzzkjgDtos = JSON.parseArray(fzjcjgDto.getZkjg_json(), FzzkjgDto.class);
        if(fzzkjgDtos!=null&&!fzzkjgDtos.isEmpty()){
            boolean inserted = fzzkjgService.insertList(fzzkjgDtos);
            if(!inserted){
                return false;
            }
        }
        return true;
    }

    /**
     * 根据路径创建文件
     */
    private boolean mkDirs(String storePath)
    {
        File file = new File(storePath);
        if (file.isDirectory()) {
            return true;
        }
        return file.mkdirs();
    }

    /**
     * 从原路径剪切到目标路径
     */
    private boolean CutFile(String s_srcFile,String s_distFile) {
        //路径如果为空则直接返回错误
        if(StringUtil.isBlank(s_srcFile)|| StringUtil.isBlank(s_distFile))
            return false;

        File srcFile = new File(s_srcFile);
        File distFile = new File(s_distFile);
        //文件不存在则直接返回错误
        if(!srcFile.exists())
            return false;
        //目标文件已经存在
        if(distFile.exists()) {
            srcFile.renameTo(distFile);
        }else {
            srcFile.renameTo(distFile);
        }
        return true;
    }
}


