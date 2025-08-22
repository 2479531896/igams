package com.matridx.igams.common.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.ShlcDto;
import com.matridx.igams.common.dao.entities.SpgwDto;
import com.matridx.igams.common.dao.entities.SpgwModel;
import com.matridx.igams.common.dao.entities.XtshDto;
import com.matridx.igams.common.dao.post.ISpgwDao;
import com.matridx.igams.common.service.svcinterface.IShlcService;
import com.matridx.igams.common.service.svcinterface.ISpgwService;
import com.matridx.igams.common.service.svcinterface.IXtshService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.util.CollectionUtils;

@Service
public class SpgwServiceImpl extends BaseBasicServiceImpl<SpgwDto, SpgwModel, ISpgwDao> implements ISpgwService{

	@Autowired
	IShlcService shlcService;
	@Autowired
	IXtshService xtshService;
    @Autowired(required=false)
    private AmqpTemplate amqpTempl;
    @Value("${matridx.rabbit.flg:}")
    private String prefixFlg;
	@Value("${matridx.rabbit.preflg:}")
	private String preRabbitFlg;
    
    /**
     * 插入审核岗位信息
     */
    @Override
    public boolean insert(SpgwModel spgwModel){
    	String gwid = StringUtil.generateUUID();
    	spgwModel.setGwid(gwid);
    	spgwModel.setPrefix(prefixFlg);
    	int result = dao.insert(spgwModel);
    	return result > 0;
    }
    /**
	 * 获取审核流程可选岗位
	 */
	@Override
	public List<SpgwDto> getPagedOptionalList(SpgwDto spgwDto) {
		return dao.getPagedOptionalList(spgwDto);
	}
	
	/**
	 * 获取审核流程已选岗位
	 */
	@Override
	public List<SpgwDto> getPagedSelectedList(SpgwDto spgwDto) {
		return dao.getPagedSelectedList(spgwDto);
	}

	/**
	 * 更新审核流程
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateProcess(SpgwDto spgwDto) {
		//判断审核流程是否有该审核ID
		ShlcDto shlcDto = new ShlcDto();
		shlcDto.setShid(spgwDto.getShid());
		XtshDto xtshDto=xtshService.getDtoById(spgwDto.getShid());
		List<ShlcDto> dtoList = shlcService.getDtoList(shlcDto);
		if(dtoList!=null && !dtoList.isEmpty()){
			//根据shid停用审核流程
			boolean isSuccess = shlcService.updateTysjByShid(spgwDto.getShid());
			if(!isSuccess){
				return false;
			}
			if("1".equals(xtshDto.getSfgb())) {
				spgwDto.setPrefix(prefixFlg);
				amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.shlc.update",JSONObject.toJSONString(spgwDto));
			}
		}
		//获取插入流程数据（如果审核流程不为空）
		List<String> gwidList = spgwDto.getIds();
		List<SpgwDto> spgwList = new ArrayList<>();
		if(gwidList!=null && !gwidList.isEmpty()){
			for (int i = 0; i < gwidList.size(); i++) {
				SpgwDto t_spgwDto = new SpgwDto();
				if (!CollectionUtils.isEmpty(spgwDto.getGwidlist())){
					for(int j=0;j<spgwDto.getGwidlist().size();j++) {
						if(gwidList.get(i).equals(spgwDto.getGwidlist().get(j))) {
							t_spgwDto.setLclb(spgwDto.getLclblist().get(j));
						}
					}
				}
				t_spgwDto.setGwid(gwidList.get(i));
				t_spgwDto.setShid(spgwDto.getShid());
				t_spgwDto.setLrry(spgwDto.getLrry());
				t_spgwDto.setLcxh(String.valueOf((i+1)));
				t_spgwDto.setPrefix(prefixFlg);
				spgwList.add(t_spgwDto);
			}
			boolean isSuccesss = shlcService.insertBySpgwList(spgwList);
			if(!isSuccesss){
				return false;
			}
			if("1".equals(xtshDto.getSfgb())) {
				amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.shlc.insert",JSONObject.toJSONString(spgwList));
			}
		}
		return true;
	}

	/**
	 * 根据ids获取审批岗位信息
	 */
	@Override
	public List<SpgwDto> getSpgwByIds(SpgwDto spgwDto){
		return dao.getSpgwByIds(spgwDto);
	}

	@Override
	public boolean insertSpgwDtos(List<SpgwDto> spgwDtos) {
		return dao.insertSpgwDtos(spgwDtos);
	}
}
