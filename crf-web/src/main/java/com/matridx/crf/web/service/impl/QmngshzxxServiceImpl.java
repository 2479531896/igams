package com.matridx.crf.web.service.impl;

import com.matridx.crf.web.dao.entities.*;
import com.matridx.crf.web.dao.post.IQmngskgryDao;
import com.matridx.crf.web.dao.post.IQmngsndzxxjlDao;
import com.matridx.crf.web.service.svcinterface.*;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.crf.web.dao.post.IQmngshzxxDao;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class QmngshzxxServiceImpl extends BaseBasicServiceImpl<QmngshzxxDto, QmngshzxxModel, IQmngshzxxDao> implements IQmngshzxxService {
    @Autowired
    private IQmngsndzxxjlService qmngsndzxxjlService;
    @Autowired
    private IQmngsdmxqService qmngsdmxqService;
    @Autowired
    private IQmngsshService qmngsshService;
    @Autowired
    private IQmngsxcgService qmngsxcgService;
    @Autowired
    private IQmngsyzzbService qmngsyzzbService;
    @Autowired
    private IQmngskgryService qmngskgryService;
    @Autowired
	IJcsjService jcsjService;
    @Autowired
    private IQmngskgryDao qmngskgryDao;
	@Autowired
    private IQmngsndzxxjlDao qmngsndzxxjlDao;
    private Logger log = LoggerFactory.getLogger(QmngshzxxServiceImpl.class);
    public List<Map<String,String>> getHospitailList(String dqjs) {

        return dao.getHospitailList(dqjs);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean saveQmngsNdz(QmngshzxxDto qmngshzxxDto, BeanQmngsndzxxFroms beanQmngsndzxxFroms) {
        try {
            //先保存Dto
            qmngshzxxDto.setScbj("0");
            qmngshzxxDto.setQmngshzid(StringUtil.generateUUID());
            //保存ndz第几天
            if(!saveNdz("add","1",beanQmngsndzxxFroms,qmngshzxxDto)) return false;
            if(!saveNdz("add","3",beanQmngsndzxxFroms,qmngshzxxDto)) return false;
            if(!saveNdz("add","5",beanQmngsndzxxFroms,qmngshzxxDto)) return false;
            if(!saveNdz("add","7",beanQmngsndzxxFroms,qmngshzxxDto)) return false;
            if(!saveNdz("add","28",beanQmngsndzxxFroms,qmngshzxxDto)) return false;
            //保存药物等信息
            List<QmngskgryDto> list = beanQmngsndzxxFroms.getQmngskgryDtos();
            if(!saveKgryList(list,qmngshzxxDto)) return false;
            dao.insert(qmngshzxxDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateQmngshzxx(QmngshzxxDto qmngshzxxDto, BeanQmngsndzxxFroms beanQmngsndzxxFroms) {
        try{
            //跟新每日记录信息
            //保存ndz第几天
            if(!saveNdz("mod","1",beanQmngsndzxxFroms,qmngshzxxDto)) return false;
            if(!saveNdz("mod","3",beanQmngsndzxxFroms,qmngshzxxDto)) return false;
            if(!saveNdz("mod","5",beanQmngsndzxxFroms,qmngshzxxDto)) return false;
            if(!saveNdz("mod","7",beanQmngsndzxxFroms,qmngshzxxDto)) return false;
            if(!saveNdz("mod","28",beanQmngsndzxxFroms,qmngshzxxDto)) return false;
            //保存药物等信息
            List<QmngskgryDto> list = beanQmngsndzxxFroms.getQmngskgryDtos();
            if(!saveKgryList(list,qmngshzxxDto)) return false;
            dao.updateDto(qmngshzxxDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }

        return true;


    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean delQmngsHzxx(String qmngshzid, String userid) {
        try{
            QmngshzxxDto qmngshzxxDto =new QmngshzxxDto();
            qmngshzxxDto.setQmngshzid(qmngshzid);
            qmngshzxxDto = dao.getDto(qmngshzxxDto);
            if(qmngshzxxDto!=null) {
                qmngshzxxDto.setScry(userid);
                //删除脓毒症报告
                QmngsndzxxjlDto qmngsndzxxjlDto = new QmngsndzxxjlDto();
                qmngsndzxxjlDto.setQmngshzid(qmngshzid);
                boolean resultN = qmngsndzxxjlDao.delDtoList(qmngsndzxxjlDto);
                if(!resultN)return false;
                QmngskgryDto qmngskgryDto = new QmngskgryDto();
                qmngskgryDto.setQmngshzid(qmngshzid);
                qmngskgryDto.setScry(userid);
                if(qmngskgryDao.delDtoList(qmngskgryDto));
                dao.delDto(qmngshzxxDto);
            }
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean saveNdz(String sign,String type, BeanQmngsndzxxFroms beanQmngsndzxxFroms,QmngshzxxDto qmngshzxxDto) {
        QmngsndzxxjlDto qmngsndzxxjlDto = new QmngsndzxxjlDto();

        //保存配置信息
        if (type.equals("1")) {
            qmngsndzxxjlDto = beanQmngsndzxxFroms.getNdzxxjlDtoOne();
            if(StringUtil.isBlank(qmngsndzxxjlDto.getQmngsndzjlid())){
                qmngsndzxxjlDto.setQmngsndzjlid(StringUtil.generateUUID());
            }
            qmngsdmxqService.saveDmxq(beanQmngsndzxxFroms.getDmxqsOne(), qmngsndzxxjlDto);
            qmngsshService.saveSh(beanQmngsndzxxFroms.getShsOne(), qmngsndzxxjlDto);
            qmngsxcgService.saveXcg(beanQmngsndzxxFroms.getXcgsOne(), qmngsndzxxjlDto);
            qmngsyzzbService.saveYzzb(beanQmngsndzxxFroms.getYzzbsOne(), qmngsndzxxjlDto);
        } else if (type.equals("3")) {
            qmngsndzxxjlDto = beanQmngsndzxxFroms.getNdzxxjlDtoThree();
            if(StringUtil.isBlank(qmngsndzxxjlDto.getQmngsndzjlid())){
                qmngsndzxxjlDto.setQmngsndzjlid(StringUtil.generateUUID());
            }
            qmngsdmxqService.saveDmxq(beanQmngsndzxxFroms.getDmxqsThree(), qmngsndzxxjlDto);
            qmngsshService.saveSh(beanQmngsndzxxFroms.getShsThree(), qmngsndzxxjlDto);
            qmngsxcgService.saveXcg(beanQmngsndzxxFroms.getXcgsThree(), qmngsndzxxjlDto);
            qmngsyzzbService.saveYzzb(beanQmngsndzxxFroms.getYzzbsThree(), qmngsndzxxjlDto);

        } else if (type.equals("5")) {
            qmngsndzxxjlDto = beanQmngsndzxxFroms.getNdzxxjlDtoFive();
            if(StringUtil.isBlank(qmngsndzxxjlDto.getQmngsndzjlid())){
                qmngsndzxxjlDto.setQmngsndzjlid(StringUtil.generateUUID());
            }
            qmngsdmxqService.saveDmxq(beanQmngsndzxxFroms.getDmxqsFive(), qmngsndzxxjlDto);
            qmngsshService.saveSh(beanQmngsndzxxFroms.getShsFive(), qmngsndzxxjlDto);
            qmngsxcgService.saveXcg(beanQmngsndzxxFroms.getXcgsFive(), qmngsndzxxjlDto);
            qmngsyzzbService.saveYzzb(beanQmngsndzxxFroms.getYzzbsFive(), qmngsndzxxjlDto);

        } else if (type.equals("7")) {
            qmngsndzxxjlDto = beanQmngsndzxxFroms.getNdzxxjlDtoSeven();
            if(StringUtil.isBlank(qmngsndzxxjlDto.getQmngsndzjlid())){
                qmngsndzxxjlDto.setQmngsndzjlid(StringUtil.generateUUID());
            }
            qmngsdmxqService.saveDmxq(beanQmngsndzxxFroms.getDmxqsSeven(), qmngsndzxxjlDto);
            qmngsshService.saveSh(beanQmngsndzxxFroms.getShsSeven(), qmngsndzxxjlDto);
            qmngsxcgService.saveXcg(beanQmngsndzxxFroms.getXcgsSeven(), qmngsndzxxjlDto);
            qmngsyzzbService.saveYzzb(beanQmngsndzxxFroms.getYzzbsSeven(), qmngsndzxxjlDto);

        }else if (type.equals("28")) {
            qmngsndzxxjlDto = beanQmngsndzxxFroms.getNdzxxjlDtoTwentyEight();
            if(StringUtil.isBlank(qmngsndzxxjlDto.getQmngsndzjlid())){
                qmngsndzxxjlDto.setQmngsndzjlid(StringUtil.generateUUID());
            }
            qmngsdmxqService.saveDmxq(beanQmngsndzxxFroms.getDmxqsTwentyEight(), qmngsndzxxjlDto);
            qmngsshService.saveSh(beanQmngsndzxxFroms.getShsTwentyEight(), qmngsndzxxjlDto);
            qmngsxcgService.saveXcg(beanQmngsndzxxFroms.getXcgsTwentyEight(), qmngsndzxxjlDto);
            qmngsyzzbService.saveYzzb(beanQmngsndzxxFroms.getYzzbsTwentyEight(), qmngsndzxxjlDto);

        }
        boolean result = true;
        if("add".equals(sign)){
            qmngsndzxxjlDto.setScbj("0");
            qmngsndzxxjlDto.setJldjt(type);
            qmngsndzxxjlDto.setLrry(qmngshzxxDto.getLrry());
            qmngsndzxxjlDto.setQmngshzid(qmngshzxxDto.getQmngshzid());
            result =qmngsndzxxjlService.insertDto(qmngsndzxxjlDto);
        }else{
            qmngsndzxxjlDto.setXgry(qmngshzxxDto.getXgry());
            result =qmngsndzxxjlService.updateDto(qmngsndzxxjlDto);
        }

        return result;
    }
    public boolean saveKgryList(List<QmngskgryDto> list ,QmngshzxxDto qmngshzxxDto){
        if(list!=null&&list.size()>0){
            for(int i =0; i<list.size();i++){
                QmngskgryDto qk = list.get(i);
                if(qk!=null){
                    if(StringUtil.isNotBlank(qk.getKgrywsyid())){
                        qk.setXgry(qmngshzxxDto.getXgry());
                        boolean result =  qmngskgryService.updateDto(qk);
                        if(!result){return false;}
                    }else{
                        qk.setKgrywsyid(StringUtil.generateUUID());
                        qk.setLrry(qmngshzxxDto.getLrry());
                        qk.setScbj("0");
                        qk.setXspx(i+"");
                        qk.setQmngshzid(qmngshzxxDto.getQmngshzid());
                        boolean result =qmngskgryService.insertDto(qk);
                        if(!result){return false;}
                    }

                }else{
                    continue;
                }
            }

        }        return  true;

    }
    
    
	
	/**
	 * qmngs患者信息查看
	 * 
	 * @return
	 */
	public QmngshzxxDto queryById(String id) {
		QmngshzxxDto qmngshzxxDto = dao.getDtoById(id);		
		if(StringUtil.isNotBlank(qmngshzxxDto.getJwhbz())) {
			String jwhbz = getJcsj(qmngshzxxDto.getJwhbz());
			qmngshzxxDto.setJwhbz(jwhbz);
		}
		if(StringUtil.isNotBlank(qmngshzxxDto.getGrbw())) {
			String grbw = getJcsj(qmngshzxxDto.getGrbw());
			qmngshzxxDto.setGrbw(grbw);
		}		
		return qmngshzxxDto;
	}
	
	/**
	 * 获取合并症和感染部位
	 * 
	 * @return
	 */
	private String getJcsj(String id) {
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setIds(id);
		//既然并和症
		List<JcsjDto> jcsjDtos = jcsjService.getInstopDtoList(jcsjDto);
		String result = "";
		if(jcsjDtos!=null && jcsjDtos.size()>0) {
			for (JcsjDto jcsjDto_t : jcsjDtos) {
				result = result + "," +jcsjDto_t.getCsmc();
			}
			result = result.substring(1);
		}		
		return result;
	}
	
}
