package com.matridx.igams.bioinformation.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.bioinformation.dao.entities.CnvjgxqDto;
import com.matridx.igams.bioinformation.dao.entities.CnvjgxqModel;
import com.matridx.igams.bioinformation.dao.post.ICnvjgxqDao;
import com.matridx.igams.bioinformation.service.svcinterface.ICnvjgxqService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CnvjgxqServiceImpl extends BaseBasicServiceImpl<CnvjgxqDto, CnvjgxqModel, ICnvjgxqDao> implements ICnvjgxqService {
    /**
     * 根据id获取数据
     */
    public List<CnvjgxqDto> getListById(CnvjgxqDto cnvjgxqDto){
        return dao.getListById(cnvjgxqDto);
    }

    /**
     * 新增
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean addSaveCnvDetail(CnvjgxqDto cnvjgxqDto) {
        if("1".equals(cnvjgxqDto.getSffzsb())){
            String sign="dup".equalsIgnoreCase(cnvjgxqDto.getCnvlx())?"+":"-";
            if("1".equals(cnvjgxqDto.getSfqh())){
                cnvjgxqDto.setCnvxq(sign+cnvjgxqDto.getRst()+"(mos)");
                cnvjgxqDto.setCnvjg("chr"+cnvjgxqDto.getRst()+"_"+cnvjgxqDto.getKbs());
            }else{
                cnvjgxqDto.setCnvxq(sign+cnvjgxqDto.getRst());
                cnvjgxqDto.setCnvjg("chr"+cnvjgxqDto.getRst()+"_"+cnvjgxqDto.getKbs());
            }
        }else{
            BigDecimal qswz=new BigDecimal(cnvjgxqDto.getQswz());
            BigDecimal zzwz=new BigDecimal(cnvjgxqDto.getZzwz());
            BigDecimal bigDecimal=new BigDecimal(1000000);
            BigDecimal subtract = zzwz.subtract(qswz);
            String length=String.valueOf(subtract.divide(bigDecimal));
            if("1".equals(cnvjgxqDto.getSfqh())){
                if(cnvjgxqDto.getQsqd().equals(cnvjgxqDto.getZzqd())){
                    cnvjgxqDto.setCnvxq(cnvjgxqDto.getRst()+cnvjgxqDto.getQsqd()+"("+cnvjgxqDto.getCnvlx()+"[mos]_"+length+"Mb)");
                    cnvjgxqDto.setCnvjg("chr"+cnvjgxqDto.getRst()+":"+cnvjgxqDto.getQswz()+"-"+cnvjgxqDto.getZzwz()+"_"+cnvjgxqDto.getKbs()+"_"+cnvjgxqDto.getCnvlx()+"[mos]");
                }else{
                    cnvjgxqDto.setCnvxq(cnvjgxqDto.getRst()+cnvjgxqDto.getQsqd()+"-"+cnvjgxqDto.getZzwz()+"("+cnvjgxqDto.getCnvlx()+"[mos]_"+length+"Mb)");
                    cnvjgxqDto.setCnvjg("chr"+cnvjgxqDto.getRst()+":"+cnvjgxqDto.getQswz()+"-"+cnvjgxqDto.getZzwz()+"_"+cnvjgxqDto.getKbs()+"_"+cnvjgxqDto.getCnvlx()+"[mos]");
                }
            }else{
                if(cnvjgxqDto.getQsqd().equals(cnvjgxqDto.getZzqd())){
                    cnvjgxqDto.setCnvxq(cnvjgxqDto.getRst()+cnvjgxqDto.getQsqd()+"("+cnvjgxqDto.getCnvlx()+"_"+length+"Mb)");
                    cnvjgxqDto.setCnvjg("chr"+cnvjgxqDto.getRst()+":"+cnvjgxqDto.getQswz()+"-"+cnvjgxqDto.getZzwz()+"_"+cnvjgxqDto.getKbs()+"_"+cnvjgxqDto.getCnvlx());
                }else{
                    cnvjgxqDto.setCnvxq(cnvjgxqDto.getRst()+cnvjgxqDto.getQsqd()+"-"+cnvjgxqDto.getZzwz()+"("+cnvjgxqDto.getCnvlx()+"_"+length+"Mb)");
                    cnvjgxqDto.setCnvjg("chr"+cnvjgxqDto.getRst()+":"+cnvjgxqDto.getQswz()+"-"+cnvjgxqDto.getZzwz()+"_"+cnvjgxqDto.getKbs()+"_"+cnvjgxqDto.getCnvlx());
                }
            }
        }
        cnvjgxqDto.setCnvjgxqid(StringUtil.generateUUID());
        int insert = dao.insert(cnvjgxqDto);
        return insert != 0;
    }

    /**
     * 更新是否汇报字段
     */
    public boolean updateSfhb(CnvjgxqDto cnvjgxqDto){
        return dao.updateSfhb(cnvjgxqDto);
    }

    /**
     * 还原是否汇报字段
     */
    public boolean updateSfhbByCnvjgid(String cnvjgid){
        return dao.updateSfhbByCnvjgid(cnvjgid);
    }
    /**
     * 批量新增
     */
    public boolean insertList(List<CnvjgxqDto> list){
        return dao.insertList(list);
    }

    @Override
    public List<CnvjgxqDto> getAjyList(CnvjgxqDto cnvjgxqDto) {
        return dao.getAjyList(cnvjgxqDto);
    }
}
