package com.matridx.igams.hrm.service.impl;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.hrm.dao.entities.GrksmxDto;
import com.matridx.igams.hrm.dao.entities.GrksmxModel;
import com.matridx.igams.hrm.dao.entities.KsmxDto;
import com.matridx.igams.hrm.dao.post.IGrksmxDao;
import com.matridx.igams.hrm.service.svcinterface.IGrksmxService;
import com.matridx.igams.hrm.service.svcinterface.IKsmxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class GrksmxServiceImpl extends BaseBasicServiceImpl<GrksmxDto, GrksmxModel, IGrksmxDao> implements IGrksmxService {
    @Autowired
    IKsmxService ksmxService;
    /**
     * 获取个人考试明细
     */
    public List<GrksmxDto> getListByGrksid(GrksmxDto grksmxDto){
        return dao.getListByGrksid(grksmxDto);
    }

    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String, Object> params, GrksmxDto grksmxDto) {
        List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
        StringBuilder sqlParam = new StringBuilder();
        for (DcszDto dcszDto : choseList) {
            if (dcszDto == null || dcszDto.getDczd() == null)
                continue;

            sqlParam.append(",");
            if (StringUtil.isNotBlank(dcszDto.getSqlzd())) {
                sqlParam.append(dcszDto.getSqlzd());
            }
            sqlParam.append(" ");
            sqlParam.append(dcszDto.getDczd());
        }
        String sqlcs = sqlParam.toString();
        grksmxDto.setSqlParam(sqlcs);
    }
    /**
     * 从数据库分页获取导出数据
     */
    public List<GrksmxDto> getListForSelectExp(Map<String, Object> params){
        GrksmxDto grksmxDto = (GrksmxDto) params.get("entryData");
        queryJoinFlagExport(params,grksmxDto);
        String sqlParam = grksmxDto.getSqlParam();
        List<GrksmxDto> list;
        if(sqlParam.contains("tmnr") || sqlParam.contains("dtkssj") || sqlParam.contains("dtjssj") || sqlParam.contains("dtjg") || sqlParam.contains("df") || sqlParam.contains("da") || sqlParam.contains("tmlx")){
            list=dao.getListForSelectExp(grksmxDto);
            for(GrksmxDto dto:list){
                if("SELECT".equals(dto.getTmlx())||"MULTIPLE".equals(dto.getTmlx())){
                    KsmxDto ksmxDto=new KsmxDto();
                    ksmxDto.setKsid(dto.getKsid());
                    if(StringUtil.isNotBlank(dto.getDa())){
                        String[] split = dto.getDa().split(",");
                        StringBuilder da= new StringBuilder();
                        for(int i = 0 ; i < split.length ; i++){
                            if(i == split.length -1){
                                ksmxDto.setXxdm(split[i]);
                                split[i]=split[i]+"."+ksmxService.getXxnrByXxdm(ksmxDto).getXxnr();
                                da.append(split[i]);
                            }else{
                                ksmxDto.setXxdm(split[i]);
                                split[i]=split[i]+"."+ksmxService.getXxnrByXxdm(ksmxDto).getXxnr();
                                da.append(split[i]).append(",");
                            }
                        }
                        dto.setDa(da.toString());
                    }
                    if(StringUtil.isNotBlank(dto.getDtjg())){
                        String[] option = dto.getDtjg().split(",");
                        StringBuilder dtjg= new StringBuilder();
                        for(int i = 0 ; i < option.length ; i++){
                            if(i == option.length -1){
                                ksmxDto.setXxdm(option[i]);
                                option[i]=option[i]+"."+ksmxService.getXxnrByXxdm(ksmxDto).getXxnr();
                                dtjg.append(option[i]);
                            }else{
                                ksmxDto.setXxdm(option[i]);
                                option[i]=option[i]+"."+ksmxService.getXxnrByXxdm(ksmxDto).getXxnr();
                                dtjg.append(option[i]).append(",");
                            }
                        }
                        dto.setDtjg(dtjg.toString());
                    }
                    if("SELECT".equals(dto.getTmlx())){
                        dto.setTmlx("单选题");
                    }else if("MULTIPLE".equals(dto.getTmlx())){
                        dto.setTmlx("多选题");
                    }
                }else{
                    dto.setTmlx("简答题");
                }
            }
        }else{
            list=dao.getListForSelect(grksmxDto);
        }
        return list;
    }
    /**
     * 根据搜索条件获取导出条数
     */
    public int getCountForSearchExp(GrksmxDto grksmxDto,Map<String, Object> params){
        return dao.getCountForSearchExp(grksmxDto);
    }
    /**
     * 从数据库分页获取导出数据
     */
    public List<GrksmxDto> getListForSearchExp(Map<String, Object> params){
        GrksmxDto grksmxDto = (GrksmxDto) params.get("entryData");
        queryJoinFlagExport(params, grksmxDto);
        String sqlParam = grksmxDto.getSqlParam();
        List<GrksmxDto> list;
        if(sqlParam.contains("tmnr") || sqlParam.contains("dtkssj") || sqlParam.contains("dtjssj") || sqlParam.contains("dtjg") || sqlParam.contains("df") || sqlParam.contains("da") || sqlParam.contains("tmlx")){
            list=dao.getListForSearchExp(grksmxDto);
            for(GrksmxDto dto:list){
                if("SELECT".equals(dto.getTmlx())||"MULTIPLE".equals(dto.getTmlx())){
                    KsmxDto ksmxDto=new KsmxDto();
                    ksmxDto.setKsid(dto.getKsid());
                    if(StringUtil.isNotBlank(dto.getDa())){
                        String[] split = dto.getDa().split(",");
                        StringBuilder da= new StringBuilder();
                        for(int i = 0 ; i < split.length ; i++){
                            if(i == split.length -1){
                                ksmxDto.setXxdm(split[i]);
                                split[i]=split[i]+"."+ksmxService.getXxnrByXxdm(ksmxDto).getXxnr();
                                da.append(split[i]);
                            }else{
                                ksmxDto.setXxdm(split[i]);
                                split[i]=split[i]+"."+ksmxService.getXxnrByXxdm(ksmxDto).getXxnr();
                                da.append(split[i]).append(",");
                            }
                        }
                        dto.setDa(da.toString());
                    }
                    if(StringUtil.isNotBlank(dto.getDtjg())){
                        String[] option = dto.getDtjg().split(",");
                        StringBuilder dtjg= new StringBuilder();
                        for(int i = 0 ; i < option.length ; i++){
                            if(i == option.length -1){
                                ksmxDto.setXxdm(option[i]);
                                option[i]=option[i]+"."+ksmxService.getXxnrByXxdm(ksmxDto).getXxnr();
                                dtjg.append(option[i]);
                            }else{
                                ksmxDto.setXxdm(option[i]);
                                option[i]=option[i]+"."+ksmxService.getXxnrByXxdm(ksmxDto).getXxnr();
                                dtjg.append(option[i]).append(",");
                            }
                        }
                        dto.setDtjg(dtjg.toString());
                    }
                    if("SELECT".equals(dto.getTmlx())){
                        dto.setTmlx("单选题");
                    }else if("MULTIPLE".equals(dto.getTmlx())){
                        dto.setTmlx("多选题");
                    }
                }else{
                    dto.setTmlx("简答题");
                }
            }
        }else{
            list=dao.getListForSearch(grksmxDto);
        }
        return list;
    }


    /**
     * 根据搜索条件获取导出条数(销售)
     */
    public int getCountForSellSearchExp(GrksmxDto grksmxDto,Map<String, Object> params){
        return dao.getCountForSellSearchExp(grksmxDto);
    }
    /**
     * 从数据库分页获取导出数据(销售)
     */
    public List<GrksmxDto> getListForSellSearchExp(Map<String, Object> params){
        GrksmxDto grksmxDto = (GrksmxDto) params.get("entryData");
        queryJoinFlagExport(params, grksmxDto);
        List<GrksmxDto> list=dao.getListForSellSearchExp(grksmxDto);
        for(GrksmxDto dto:list){
            if("SELECT".equals(dto.getTmlx())||"MULTIPLE".equals(dto.getTmlx())){
                KsmxDto ksmxDto=new KsmxDto();
                ksmxDto.setKsid(dto.getKsid());
                String[] split = dto.getDa().split(",");
                StringBuilder da= new StringBuilder();
                for(int i = 0 ; i < split.length ; i++){
                    if(i == split.length -1){
                        ksmxDto.setXxdm(split[i]);
                        split[i]=split[i]+"."+ksmxService.getXxnrByXxdm(ksmxDto).getXxnr();
                        da.append(split[i]);
                    }else{
                        ksmxDto.setXxdm(split[i]);
                        split[i]=split[i]+"."+ksmxService.getXxnrByXxdm(ksmxDto).getXxnr();
                        da.append(split[i]).append(",");
                    }
                }
                dto.setDa(da.toString());
                if(StringUtil.isNotBlank(dto.getDtjg())){
                    String[] option = dto.getDtjg().split(",");
                    StringBuilder dtjg= new StringBuilder();
                    for(int i = 0 ; i < option.length ; i++){
                        if(i == option.length -1){
                            ksmxDto.setXxdm(option[i]);
                            option[i]=option[i]+"."+ksmxService.getXxnrByXxdm(ksmxDto).getXxnr();
                            dtjg.append(option[i]);
                        }else{
                            ksmxDto.setXxdm(option[i]);
                            option[i]=option[i]+"."+ksmxService.getXxnrByXxdm(ksmxDto).getXxnr();
                            dtjg.append(option[i]).append(",");
                        }
                    }
                    dto.setDtjg(dtjg.toString());
                }
            }
        }
        return list;
    }
    /**
     * 新增考试信息
     */
    public void insertList(List<GrksmxDto> list){
        dao.insertList(list);
    }

    @Override
    public List<GrksmxDto> getListByKsIds(List<String> ids) {
        return dao.getListByKsIds(ids);
    }

    @Override
    public boolean updateGrksmxDtos(List<GrksmxDto> upGrksmxDtos) {
        return dao.updateGrksmxDtos(upGrksmxDtos);
    }
}
