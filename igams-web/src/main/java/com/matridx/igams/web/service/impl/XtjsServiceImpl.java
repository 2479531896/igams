package com.matridx.igams.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.web.dao.entities.JsdwqxDto;
import com.matridx.igams.web.dao.entities.JsdwqxModel;
import com.matridx.igams.web.dao.entities.JszyczbDto;
import com.matridx.igams.web.dao.entities.JszyczbModel;
import com.matridx.igams.web.dao.entities.WbzyDto;
import com.matridx.igams.web.dao.entities.WbzyqxModel;
import com.matridx.igams.web.dao.entities.XtjsDto;
import com.matridx.igams.web.dao.entities.XtjsModel;
import com.matridx.igams.web.dao.entities.XtyhDto;
import com.matridx.igams.web.dao.entities.YhjgqxDto;
import com.matridx.igams.web.dao.entities.YhjsDto;
import com.matridx.igams.web.dao.entities.YhjsModel;
import com.matridx.igams.web.dao.post.IXtjsDao;
import com.matridx.igams.web.dao.post.IYhjgqxDao;
import com.matridx.igams.web.dao.post.IYhjsDao;
import com.matridx.igams.web.service.svcinterface.IJsdwqxService;
import com.matridx.igams.web.service.svcinterface.IJszyczbService;
import com.matridx.igams.web.service.svcinterface.IWbzyService;
import com.matridx.igams.web.service.svcinterface.IWbzyqxService;
import com.matridx.igams.web.service.svcinterface.IXtjsService;
import com.matridx.igams.web.service.svcinterface.IYhjgqxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class XtjsServiceImpl extends BaseBasicServiceImpl<XtjsDto, XtjsModel, IXtjsDao> implements IXtjsService {
	@Autowired
	private IYhjgqxService yhjgqxService;
	@Autowired
	private IYhjgqxDao yhjgqxDao;
	@Autowired
	private IYhjsDao yhjsDao;
	@Autowired
	private IJcsjService jcsjService;
	@Autowired
	private IJsdwqxService jsdwqxService;
	@Autowired
	IWbzyService wbzyService;
	@Autowired
	IWbzyqxService wbzyqxService;
	@Autowired
	IJszyczbService jszyczbService;
	@Autowired(required=false)
    private AmqpTemplate amqpTempl;
	@Value("${matridx.rabbit.flg:}")
	private String prefixFlg;
	@Value("${matridx.rabbit.preflg:}")
	private String preRabbitFlg;

	@Autowired
	RedisUtil redisUtil;
	/**
	 * 获取系统角色列表同时根据用户ID标识用户已有的角色信息
	 * @param yhid
	 * @return
	 */
	public List<XtjsDto> getAllJsAndChecked(String yhid){
		XtjsDto xtjsDto = new XtjsDto();
		xtjsDto.setYhid(yhid);
		return dao.getAllJsAndChecked(xtjsDto);
	}
	
	/**
	 * 获取除自己以外的角色列表
	 * @param xtjsModel
	 * @return
	 */
	public List<XtjsModel> getModelListExceptSelf(XtjsModel xtjsModel){
		return dao.getModelListExceptSelf(xtjsModel);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insert(XtjsModel xtjsModel){
		xtjsModel.setJsid(StringUtil.generateUUID());
		int result = dao.insert(xtjsModel);

        return result > 0;
    }
	/**
	 * 通过角色ID查询到角色下面的用户
	 * @param
	 * @return
	 */
	@Override
	public List<XtyhDto> getyhmByjsid(XtjsDto xtjsDto){
		// TODO Auto-generated method stub
		return dao.getyhmByjsid(xtjsDto);
	}

	@Override
	public boolean updateCkqxByjsid(XtjsDto xtjsDto) {
		List<String> ckqxlxDtos = xtjsDto.getCkqxlxDtos();
		StringBuilder ckqx = new StringBuilder();
		for (int i = 0; i < ckqxlxDtos.size(); i++) {
			if (StringUtil.isNotBlank(ckqxlxDtos.get(i))){
				ckqx.append(ckqxlxDtos.get(i));
				if ( i != ckqxlxDtos.size()-1){
					ckqx.append(",");
				}
			}
		}
		xtjsDto.setCkqx(ckqx.toString());
		boolean success = dao.updateCkqxByjsid(xtjsDto) !=0;
		if (success){
			amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate",RabbitEnum.CKQX_MOD.getCode() + JSONObject.toJSONString(xtjsDto));
		}
		return success;
	}

	/**
	 * 获取系统角色列表
	 * @param xtjsList
	 * @param
	 * @return
	 */
	@Override
	public String installTree(List<XtjsDto> xtjsList, String JSONDATA) {
		// TODO Auto-generated method stub
		JSONDATA += "[";
        StringBuilder JSONDATABuilder = new StringBuilder(JSONDATA);
        for (int i = 0; i< xtjsList.size(); i++) {
			if(i == xtjsList.size()-1){
				JSONDATABuilder.append("{\"id\" : \"").append(xtjsList.get(i).getJsid()).append("\",\"text\" : \"").append(xtjsList.get(i).getJsmc()).append("\",\"children\" : []}");
			}else{
				JSONDATABuilder.append("{\"id\" : \"").append(xtjsList.get(i).getJsid()).append("\",\"text\" : \"").append(xtjsList.get(i).getJsmc()).append("\",\"children\" : []},");
			}
		}
        JSONDATA = JSONDATABuilder.toString();
        JSONDATA += "]";
		return JSONDATA;
	}

	/**
	 * 修改角色信息
	 * @param xtjsDto
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateJsxx(XtjsDto xtjsDto){
		// TODO Auto-generated method stub
		int result=dao.update(xtjsDto);
		if(result==0) {
            return false;
        }
		xtjsDto.setPrefix(prefixFlg);
		amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.xtjs.update",JSONObject.toJSONString(xtjsDto));
		//添加用户机构权限
		if(!CollectionUtils.isEmpty(xtjsDto.getXtyhDtos())) {
			//修改用户机构权限的时候，先删除掉，然后新增
			List<String> strList= new ArrayList<>();
			for (int i = 0; i < xtjsDto.getXtyhDtos().size(); i++){//删除用户机构权限
				if(xtjsDto.getXtyhDtos().get(i).getYhid()!=null) {
					String yhid=xtjsDto.getXtyhDtos().get(i).getYhid();
					strList.add(yhid);
				}
			}
			YhjgqxDto yhjgqxDto_t=new YhjgqxDto();
			yhjgqxDto_t.setIds(strList);
			yhjgqxService.deleteJgqxxxByYhid(yhjgqxDto_t);
			for (int j = 0; j < xtjsDto.getXtyhDtos().size(); j++){
				List<YhjgqxDto> yhjgqxList= new ArrayList<>();
				if(!CollectionUtils.isEmpty(xtjsDto.getXtyhDtos().get(j).getJgids())){
					for (int l = 0; l < xtjsDto.getXtyhDtos().get(j).getJgids().size(); l++){
						YhjgqxDto yhjgqxDto=new YhjgqxDto();
						yhjgqxDto.setJgid(xtjsDto.getXtyhDtos().get(j).getJgids().get(l));
						yhjgqxDto.setYhid(xtjsDto.getXtyhDtos().get(j).getYhid());
						yhjgqxDto.setXh(l+1+"");
						yhjgqxDto.setJsid(xtjsDto.getJsid());
						yhjgqxDto.setPrefix(prefixFlg);
						yhjgqxList.add(yhjgqxDto);
					}
					yhjgqxDao.insertList(yhjgqxList);
//					amqpTempl.convertAndSend("sys.igams", "sys.igams.yhjgqx.insert",JSONObject.toJSONString(yhjgqxList));
					amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate",RabbitEnum.JGQX_ADD.getCode() + JSONObject.toJSONString(yhjgqxList));
				}
			}
		}
		//添加角色机构权限
		jsdwqxService.updateByJsid(xtjsDto);
		redisUtil.hdel("Users_QxModel",xtjsDto.getJsid());
		return true;
	}

	/**
	 * Xtjs 查看所属人员机构
	 * @param yhjsDto
	 * @return
	 */
	@Override
	public List<YhjsDto> getAllJgxxByxtjs(YhjsDto yhjsDto){
		// TODO Auto-generated method stub
		List<YhjsDto> yhjsList=yhjsDao.selectYhidByJsid(yhjsDto);
		if(!CollectionUtils.isEmpty(yhjsList)) {
            for (YhjsDto dto : yhjsList) {
                if (dto.getYhid() != null) {
                    YhjgqxDto yhjgqxDto = new YhjgqxDto();
                    yhjgqxDto.setYhid(dto.getYhid());
                    yhjgqxDto.setJsid(yhjsDto.getJsid());
                    List<YhjgqxDto> yhjgList = yhjgqxDao.getListByjsid(yhjgqxDto);
                    if (yhjgList != null) {
                        StringBuilder sb = new StringBuilder();
                        sb.setLength(0);
                        for (int j = 0; j < yhjgList.size(); j++) {
                            if (j != 0)
                                sb.append(",");
                            sb.append(yhjgList.get(j).getJgmc());
                        }
                        dto.setJgmc(sb.toString());
                    }
                }
            }
		}
		return yhjsList;
	}


	/**
	 * 修改角色信息
	 * @param xtjsDto
	 * @return
	 */
	@Override
	public  List<XtjsDto> getPagedDtoList(XtjsDto xtjsDto){
		List<XtjsDto> xtjsDtos = dao.getPagedDtoList(xtjsDto);
		for (XtjsDto xtjs: xtjsDtos) {
			if (StringUtil.isNotBlank(xtjs.getCkqx())){
				String[] temps = xtjs.getCkqx().split(",");
				StringBuilder ckqx = new StringBuilder();
				for (int i = 0; i < temps.length; i++) {
					JcsjDto jcsjDto = jcsjService.getDtoById(temps[i]);
					ckqx.append(jcsjDto.getCsmc());
					if (i != temps.length-1){
						ckqx.append(",");
					}
				}
				xtjs.setCkqx(ckqx.toString());
			}
		}
		return xtjsDtos;
	}

	/**
	 * 获取角色列表(小程序)
	 * @param xtjsDto
	 * @return
	 */
	@Override
	public List<XtjsDto> getMiniXtjsList(XtjsDto xtjsDto){
		return dao.getMiniXtjsList(xtjsDto);
	}



	/**
	 * 复制保存角色信息
	 * @param xtjsDto
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean copySaveRole(XtjsDto xtjsDto){
		String newId=StringUtil.generateUUID();
		XtyhDto xtyhDto=new XtyhDto();
		xtyhDto.setJsid(xtjsDto.getJsid());
		List<XtyhDto> pagedSelectedList = yhjsDao.getSelectedList(xtyhDto);
		List<YhjsModel> yhjsDtos=new ArrayList<>();
		if(!CollectionUtils.isEmpty(pagedSelectedList)){
			for(XtyhDto dto:pagedSelectedList){
				YhjsModel yhjsModel=new YhjsModel();
				yhjsModel.setJsid(newId);
				yhjsModel.setYhid(dto.getYhid());
				yhjsDtos.add(yhjsModel);
			}
		}
		if(!CollectionUtils.isEmpty(yhjsDtos)){
			yhjsDao.batchInsert(yhjsDtos);
		}
		JsdwqxDto jsdwqxDto=new JsdwqxDto();
		jsdwqxDto.setJsid(xtjsDto.getJsid());
		List<JsdwqxDto> yxJgxxList = jsdwqxService.getYxJgxxList(jsdwqxDto);
		List<JsdwqxModel> jsdwqxDtos=new ArrayList<>();
		if(!CollectionUtils.isEmpty(yxJgxxList)){
			for(JsdwqxDto dto:yxJgxxList){
				JsdwqxModel jsdwqxModel=new JsdwqxModel();
				jsdwqxModel.setJsid(newId);
				jsdwqxModel.setJgid(dto.getJgid());
				jsdwqxDtos.add(jsdwqxModel);
			}
		}
		if(!CollectionUtils.isEmpty(jsdwqxDtos)){
			jsdwqxService.batchInsert(jsdwqxDtos);
		}
		List<WbzyDto> wbzyList = wbzyService.getMiniMenuTreeList(xtjsDto.getJsid());
		List<WbzyqxModel> wbzyqxDtos=new ArrayList<>();
		if(!CollectionUtils.isEmpty(wbzyList)){
			for(WbzyDto dto:wbzyList){
				if(StringUtil.isNotBlank(dto.getJsid())){
					WbzyqxModel wbzyqxModel=new WbzyqxModel();
					wbzyqxModel.setJsid(newId);
					wbzyqxModel.setZyid(dto.getZyid());
					wbzyqxDtos.add(wbzyqxModel);
				}
			}
		}
		if(!CollectionUtils.isEmpty(wbzyqxDtos)){
			wbzyqxService.batchInsert(wbzyqxDtos);
		}
		List<JszyczbDto> menuTreeList = jszyczbService.getListById(xtjsDto.getJsid());
		List<JszyczbModel> jszyczbModels=new ArrayList<>();
		if(!CollectionUtils.isEmpty(menuTreeList)){
			for(JszyczbDto dto:menuTreeList){
				JszyczbModel jszyczbModel=new JszyczbModel();
				jszyczbModel.setJsid(newId);
				jszyczbModel.setZyid(dto.getZyid());
				jszyczbModel.setCzdm(dto.getCzdm());
				jszyczbModels.add(jszyczbModel);
			}
		}
		if(!CollectionUtils.isEmpty(jszyczbModels)){
			jszyczbService.batchInsert(jszyczbModels);
		}
		xtjsDto.setJsid(newId);
		int result = dao.insertDto(xtjsDto);
        return result > 0;
    }
}
