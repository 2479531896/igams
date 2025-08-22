package com.matridx.crf.web.service.impl;

import com.matridx.crf.web.dao.entities.JnsjbgjlDto;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.crf.web.dao.entities.HbpdrxxjlDto;
import com.matridx.crf.web.dao.entities.HbpdrxxjlModel;
import com.matridx.crf.web.dao.post.IHbpdrxxjlDao;
import com.matridx.crf.web.service.svcinterface.IHbpdrxxjlService;

import java.util.List;
import java.util.Map;

@Service
public class HbpdrxxjlServiceImpl extends BaseBasicServiceImpl<HbpdrxxjlDto, HbpdrxxjlModel, IHbpdrxxjlDao> implements IHbpdrxxjlService{

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 根据患者ID删除记录
     * @param hbpdrxxjlDto
     * @return
     */
    public boolean deleteByHzid(HbpdrxxjlDto hbpdrxxjlDto){
        return dao.deleteByHzid(hbpdrxxjlDto);
    }
	
	@Override
    public List<HbpdrxxjlDto> getDtoListByhzid(HbpdrxxjlDto hbpdrxxjlDto) {
        return dao.getDtoListByhzid(hbpdrxxjlDto);
    }

    /**
     * 选中导出
     * @param params
     * @return
     */
    public List<HbpdrxxjlDto> getListForSelectExp(Map<String, Object> params){
        HbpdrxxjlDto hbpdrxxjlDto = (HbpdrxxjlDto) params.get("entryData");
        queryJoinFlagExport(params,hbpdrxxjlDto);
        List<HbpdrxxjlDto> list = dao.getListForSelectExp(hbpdrxxjlDto);
        if(list!=null&&list.size()>0){
            List<JcsjDto> jwbsList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.HBPJWBS));
            List<JcsjDto> grbwList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.HBPGRBW));
            List<JcsjDto> qzbbList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.HBPQZBB));
            List<JcsjDto> xghxyList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.HBPXGHXYW));
            for(HbpdrxxjlDto hbpdrxxjlDto_t:list){
                if(StringUtil.isNotBlank(hbpdrxxjlDto_t.getJwbs())){
                    String jwbs="";
                    String[] split = hbpdrxxjlDto_t.getJwbs().split(",");
                    for(String s:split){
                        for(JcsjDto dto:jwbsList){
                            if(s.equals(dto.getCsid())){
                                jwbs+=","+dto.getCsmc();
                                break;
                            }
                        }
                    }
                    if(StringUtil.isNotBlank(jwbs)){
                        hbpdrxxjlDto_t.setJwbs(jwbs.substring(1));
                    }
                }
                if(StringUtil.isNotBlank(hbpdrxxjlDto_t.getGrbw())){
                    String grbw="";
                    String[] split = hbpdrxxjlDto_t.getGrbw().split(",");
                    for(String s:split){
                        for(JcsjDto dto:grbwList){
                            if(s.equals(dto.getCsid())){
                                grbw+=","+dto.getCsmc();
                                break;
                            }
                        }
                    }
                    if(StringUtil.isNotBlank(grbw)){
                        hbpdrxxjlDto_t.setGrbw(grbw.substring(1));
                    }
                }
                if(StringUtil.isNotBlank(hbpdrxxjlDto_t.getQzbb1())){
                    String qzbb1="";
                    String[] split = hbpdrxxjlDto_t.getQzbb1().split(",");
                    for(String s:split){
                        for(JcsjDto dto:qzbbList){
                            if(s.equals(dto.getCsid())){
                                qzbb1+=","+dto.getCsmc();
                                break;
                            }
                        }
                    }
                    if(StringUtil.isNotBlank(qzbb1)){
                        hbpdrxxjlDto_t.setQzbb1(qzbb1.substring(1));
                    }
                }

                if(StringUtil.isNotBlank(hbpdrxxjlDto_t.getQzbb2())){
                    String qzbb2="";
                    String[] split = hbpdrxxjlDto_t.getQzbb2().split(",");
                    for(String s:split){
                        for(JcsjDto dto:qzbbList){
                            if(s.equals(dto.getCsid())){
                                qzbb2+=","+dto.getCsmc();
                                break;
                            }
                        }
                    }
                    if(StringUtil.isNotBlank(qzbb2)){
                        hbpdrxxjlDto_t.setQzbb2(qzbb2.substring(1));
                    }
                }

                if(StringUtil.isNotBlank(hbpdrxxjlDto_t.getXghxyw())){
                    String xghxyw="";
                    String[] split = hbpdrxxjlDto_t.getXghxyw().split(",");
                    for(String s:split){
                        for(JcsjDto dto:xghxyList){
                            if(s.equals(dto.getCsid())){
                                xghxyw+=","+dto.getCsmc();
                                break;
                            }
                        }
                    }
                    if(StringUtil.isNotBlank(xghxyw)){
                        hbpdrxxjlDto_t.setXghxyw(xghxyw.substring(1));
                    }
                }
            }
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String,Object> params, HbpdrxxjlDto hbpdrxxjlDto){
        List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
        StringBuffer sqlParam = new StringBuffer();
        for(DcszDto dcszDto:choseList){
            if(dcszDto == null || dcszDto.getDczd() == null)
                continue;

            sqlParam.append(",");
            if(StringUtil.isNotBlank(dcszDto.getSqlzd())){
                sqlParam.append(dcszDto.getSqlzd());
            }
            sqlParam.append(" ");
            sqlParam.append(dcszDto.getDczd());
        }
        String sqlcs=sqlParam.toString();
        hbpdrxxjlDto.setSqlParam(sqlcs);
    }

}
