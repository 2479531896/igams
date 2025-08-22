package com.matridx.igams.bioinformation.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.bioinformation.dao.entities.CnvjgDto;
import com.matridx.igams.bioinformation.dao.entities.CnvjgModel;
import com.matridx.igams.bioinformation.dao.entities.CnvjgxqDto;
import com.matridx.igams.bioinformation.dao.post.ICnvjgDao;
import com.matridx.igams.bioinformation.service.svcinterface.ICnvjgService;
import com.matridx.igams.bioinformation.service.svcinterface.ICnvjgxqService;
import com.matridx.igams.bioinformation.service.svcinterface.IOtherService;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import io.minio.MinioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CnvjgServiceImpl extends BaseBasicServiceImpl<CnvjgDto, CnvjgModel, ICnvjgDao> implements ICnvjgService {

    @Autowired
    private ICnvjgxqService cnvjgxqService;
    @Value("${matridx.wechat.applicationurl:}")
    private String applicationurl;
    @Value("${matridx.fileupload.releasePath:}")
    private String releasePath;
    @Autowired
    private IOtherService otherService;
    private static final Logger log = LoggerFactory.getLogger(CnvjgServiceImpl.class);

    /**
     * 获取today的列表数据
     */
    @Override
    public Map<String, Object> getTodayList(User user,CnvjgDto cnvjgDto){
        Map<String, Object> map = new HashMap<>();
        List<Map<String,String>> list=otherService.getJsjcdwByjsid(user.getDqjs());
        List<String> jcdwList=new ArrayList<>();
        List<String> hbmcList=new ArrayList<>();
        if(list!=null&& !list.isEmpty()){
            if("1".equals(list.get(0).get("dwxdbj"))) {//判断当前角色是否进行了单位限制
                jcdwList= new ArrayList<>();
                for (Map<String, String> stringStringMap : list) {
                    if (stringStringMap.get("jcdw") != null) {
                        jcdwList.add(stringStringMap.get("jcdw"));
                    }
                }
                //判断伙伴权限
                List<String> hbqxList = otherService.getHbidByYhid(user.getYhid());
                if(hbqxList!=null && !hbqxList.isEmpty()) {
                    hbmcList=otherService.getHbmcByHbid(hbqxList);
                }
            }
        }


        cnvjgDto.setSjhbs(hbmcList);
        cnvjgDto.setJcdws(jcdwList);
        SimpleDateFormat dayDeal = new SimpleDateFormat("yyyy-MM-dd");
        cnvjgDto.setCxkssjstart(dayDeal.format(DateUtils.getDate(new Date(), -1)) + " 16:00:00");
        cnvjgDto.setCxkssjend(dayDeal.format(DateUtils.getDate(new Date(), 0)) + " 16:00:00");
        List<CnvjgDto> cnvjgDtos = dao.getTodayList(cnvjgDto);
        if(cnvjgDtos!=null&& !cnvjgDtos.isEmpty()){
            String sysid=cnvjgDtos.get(0).getSysid();
            String sysmc=cnvjgDtos.get(0).getSysmc();
            List<CnvjgDto> dtoList=new ArrayList<>();
            for(CnvjgDto dto:cnvjgDtos){
                if(sysid.equals(dto.getSysid())){
                    dtoList.add(dto);
                }else{
                    map.put(sysmc,dtoList);
                    sysid=dto.getSysid();
                    sysmc=dto.getSysmc();
                    dtoList=new ArrayList<>();
                    dtoList.add(dto);
                }
            }
            map.put(sysmc,dtoList);
        }
        return map;
    }

    /**
     * 修改
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean modSaveCnv(CnvjgDto cnvjgDto, User user) {
        cnvjgDto.setSfsh("1");
        int insert = dao.update(cnvjgDto);
        if(insert==0){
            return false;
        }
        cnvjgxqService.updateSfhbByCnvjgid(cnvjgDto.getCnvjgid());
        boolean isSuccess;
        JSONObject cnv_data=new JSONObject();
        StringBuilder xq = new StringBuilder();
        StringBuilder jg = new StringBuilder();
        if(cnvjgDto.getIds()!=null&& !cnvjgDto.getIds().isEmpty()){
            CnvjgxqDto cnvjgxqDto=new CnvjgxqDto();
            cnvjgxqDto.setIds(cnvjgDto.getIds());
            isSuccess = cnvjgxqService.updateSfhb(cnvjgxqDto);
            if(!isSuccess){
                return false;
            }
            if("1".equals(cnvjgDto.getSfyfbg())){
                List<CnvjgxqDto> dtoList = cnvjgxqService.getDtoList(cnvjgxqDto);
                if(dtoList!=null&& !dtoList.isEmpty()){
                    List<String> xqList=new ArrayList<>();
                    List<String> jgList=new ArrayList<>();
                    for(CnvjgxqDto dto:dtoList){
                        xq.append(",").append(dto.getCnvxq());
                        jg.append(",").append(dto.getCnvjg());
                        xqList.add(dto.getCnvxq());
                        jgList.add(dto.getCnvjg());
                    }
                    cnv_data.put("total_reads",cnvjgDto.getSjl());
                    cnv_data.put("gc",cnvjgDto.getGchl());
                    cnv_data.put("detail",JSON.toJSONString(jgList));
                    cnv_data.put("result",JSON.toJSONString(xqList));
                }else{
                    cnv_data.put("total_reads",cnvjgDto.getSjl());
                    cnv_data.put("gc",cnvjgDto.getGchl());
                    cnv_data.put("detail","");
                    cnv_data.put("result","");
                }
            }
        }else{
            cnv_data.put("total_reads",cnvjgDto.getSjl());
            cnv_data.put("gc",cnvjgDto.getGchl());
            cnv_data.put("detail","");
            cnv_data.put("result","");
        }


        if("1".equals(cnvjgDto.getSfyfbg())){
            String wklx="";
            if("1".equals(cnvjgDto.getQxhbbg())){
                wklx="C";
            }else{
                if(cnvjgDto.getWkbh().contains("RNA")){
                    wklx="R";
                }
                if(cnvjgDto.getWkbh().contains("DNA")){
                    wklx="D";
                }
            }
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("report_type","onco");
            jsonObject.put("detection_type",wklx);
            jsonObject.put("sample_id",cnvjgDto.getWkbh());
            jsonObject.put("ybbh",cnvjgDto.getYbbh());
            jsonObject.put("review_result",cnvjgDto.getShjgmc());
            if(StringUtil.isNotBlank(xq.toString())){
                xq = new StringBuilder("[\"" + xq.substring(1) + "\"]");
            }
            jsonObject.put("karyotype", xq.toString());
            jsonObject.put("cnv_data",cnv_data);
            jsonObject.put("gc",cnvjgDto.getGchl());
//            jsonObject.put("result",cnvjgDto.getCnvjg());

            RestTemplate restTemplate = new RestTemplate();
            MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
            String path = saveCnvClientFile(cnvjgDto);
            if(StringUtil.isNotBlank(path)){
                File file=new File(path);
                FileSystemResource resource = new FileSystemResource(file);
                paramMap.add("file", resource);
            }else{
                log.error("未获取到图片!");
                return false;
            }
            paramMap.add("sample_result", JSON.toJSONString(jsonObject));
            String url=applicationurl+"/ws/pathogen/receiveInspectionGenerateReportSec?userid="+user.getYhm();
            Map<String,Object> map=  restTemplate.postForObject(url, paramMap, Map.class);
            //执行完发送报告，将保存的图片删除
            File file=new File(path);
            file.delete();
            String status = null;
            if (map != null) {
                status = (String)map.get("status");
            }
            if("fail".equals(status)){
                cnvjgDto.setSfyfbg("0");
            }
        }
        insert = dao.update(cnvjgDto);
        return insert != 0;
    }

    @Override
    public CnvjgDto getDtoByXpidAndWkcxId(CnvjgDto cnvjgDto) {
        return dao.getDtoByXpidAndWkcxId(cnvjgDto);
    }

    /**
     * 批量修改
     */
    public boolean batchModSaveCnv(CnvjgDto cnvjgDto, User user){
        List<CnvjgDto> list= JSON.parseArray(cnvjgDto.getCnv_json(), CnvjgDto.class);
        if(list!=null&& !list.isEmpty()){
            for(CnvjgDto dto:list){
                dto.setShry(cnvjgDto.getShry());
                dto.setJyry(cnvjgDto.getJyry());
                modSaveCnv(dto,user);
            }
        }
        return true;
    }

    /**
     * 批量新增
     */
    public boolean insertList(List<CnvjgDto> list){
        return dao.insertList(list);
    }

    /**
     * 批量更新
     */
    public int updateList(List<CnvjgDto> list){
        return dao.updateList(list);
    }

    /**
     * 根据文库编号获取数据
     */
    public List<CnvjgDto> getListByWkbhs(CnvjgDto cnvjgDto){
        return dao.getListByWkbhs(cnvjgDto);
    }

    /**
     * 根据时间获取数据
     */
    public List<CnvjgDto> getListByTime(String time){
        return dao.getListByTime(time);
    }

    /**
     * 用于发送报告临时保存图片信息
     */
     public String saveCnvClientFile(CnvjgDto cnvjgDto) {
         String date = cnvjgDto.getXpm().substring(0,6);
         String path = "20"+date.substring(0,2)+"/"+date.substring(2,4)+"/"+date.substring(4,6)+"/"+cnvjgDto.getXpm();
         String wjm=cnvjgDto.getWkbh()+".mht.png";
        MinioClient minioClient;
        String newPath="";
        try {
            minioClient = new MinioClient("https://minio.matridx.com", "ituser", "Matridx@2022");
            InputStream inputStream = minioClient.getObject("oncoplot", path+"/graphData/"+wjm);
            BufferedInputStream input = null;
            byte[] buffer = new byte[4096];
            FileOutputStream fos = null;
            BufferedOutputStream output = null;
            try {
                File toFile = new File(releasePath+wjm);
                String absolutePath = toFile.getCanonicalPath();
                /*判断路径中的文件夹是否存在，如果不存在，先创建文件夹*/
                String dirPath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
                File dir = new File(dirPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                // 覆盖
                if (toFile.exists()){
                    toFile.delete();
                }

                input = new BufferedInputStream(inputStream);

                fos = new FileOutputStream(releasePath+wjm);
                output = new BufferedOutputStream(fos);
                int n;
                while ((n = input.read(buffer, 0, 4096)) > -1) {
                    output.write(buffer, 0, n);
                }
                newPath=releasePath+wjm;
            } catch (Exception e) {
                log.error(e.getMessage());
            } finally {
                closeStream(new Closeable[] {
                        inputStream, input, output, fos });
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return newPath;
    }

    @Override
    public List<CnvjgDto> getCnvjgByNbbm(CnvjgDto cnvjgDto) {

        return dao.getCnvjgByNbbm(cnvjgDto);
    }

    /**
     * 关闭流
     */
    private static void closeStream(Closeable[] streams) {
        if(streams==null || streams.length < 1)
            return;
        for (Closeable closeable : streams) {
            try {
                if (null != closeable) {
                    closeable.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * 实验室列表数据
     */
    @Override
    public List<CnvjgDto> getPagedLaboratoryList(CnvjgDto cnvjgDto){
        return dao.getPagedLaboratoryList(cnvjgDto);
    }
}
