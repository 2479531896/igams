package com.matridx.igams.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.ISpgwcyService;
import com.matridx.igams.web.dao.entities.XtyhDto;
import com.matridx.igams.web.dao.entities.YhjgqxDto;
import com.matridx.igams.web.dao.entities.YhjsDto;
import com.matridx.igams.web.dao.entities.YhjsModel;
import com.matridx.igams.web.dao.post.IYhjgqxDao;
import com.matridx.igams.web.dao.post.IYhjsDao;
import com.matridx.igams.web.service.svcinterface.IYhjsService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class YhjsServiceImpl extends BaseBasicServiceImpl<YhjsDto, YhjsModel, IYhjsDao> implements IYhjsService{
	@Autowired
	ISpgwcyService spgwcyService;
	@Autowired
	IYhjgqxDao yhjgqxDao;
	@Autowired(required=false)
    private AmqpTemplate amqpTempl;
	@Value("${matridx.rabbit.flg:}")
	private String prefixFlg;
	@Value("${matridx.rabbit.preflg:}")
	private String preRabbitFlg;
	/**
	 * 获取可选用户
	 *@param xtyhDto
	 *@return
	 */
	public List<XtyhDto> getPagedOptionalList(XtyhDto xtyhDto){
		return dao.getPagedOptionalList(xtyhDto);
	}
	
	/**
	 * 获取已选用户
	 *@param xtyhDto
	 *@return
	 */
	public List<XtyhDto> getPagedSelectedList(XtyhDto xtyhDto){
		return dao.getPagedSelectedList(xtyhDto);
	}
	
	/**
	 * 添加用户
	 *@param yhjsDto
	 *@return
	 */
	public boolean toSelected(YhjsDto yhjsDto){
		boolean result = dao.toSelected(yhjsDto);
		if(result){
			List<String> ids = yhjsDto.getIds();
			yhjsDto.setPrefix(prefixFlg);
            for (String id : ids) {
                yhjsDto.setYhid(id);
//				amqpTempl.convertAndSend("sys.igams", "sys.igams.yhjs.insert",JSONObject.toJSONString(yhjsDto));
                amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate", RabbitEnum.YHJS_ADD.getCode() + JSONObject.toJSONString(yhjsDto));
            }
		}
		return result;
	}
	
	/**
	 * 去除用户
	 *@param yhjsDto
	 *@return
	 */
	public boolean toOptional(YhjsDto yhjsDto){
		boolean result = dao.toOptional(yhjsDto);
		yhjsDto.setPrefix(prefixFlg);
		if(result){
//			amqpTempl.convertAndSend("sys.igams", "sys.igams.yhjs.del",JSONObject.toJSONString(yhjsDto));
			amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate",RabbitEnum.YHJS_DEL.getCode() + JSONObject.toJSONString(yhjsDto));
		}
		return result;
	}
	
	/**
	 * 根据Ids查询角色里已选用户
	 * @return
	 */
	public List<YhjsDto> getYxyhList(XtyhDto xtyhDto){
		return dao.getYxyhList(xtyhDto);
	}

	/**
	 * 通过jsid查询下属用户
	 * @param yhjsDto
	 * @return
	 */
	@Override
	public List<YhjsDto> selectYhidByJsid(YhjsDto yhjsDto){
		// TODO Auto-generated method stub
		return dao.selectYhidByJsid(yhjsDto);
	}

	/**
	 * 用户查看页面查询所有
	 * @param yhjsDto
	 * @return
	 */
	@Override
	public List<YhjsDto> getAllByYhid(YhjsDto yhjsDto){
		// TODO Auto-generated method stub
		List<YhjsDto> yhjsDtos =dao.getDtoList(yhjsDto);
		if(!CollectionUtils.isEmpty(yhjsDtos)) {
            for (YhjsDto dto : yhjsDtos) {
                if (dto.getJsid() != null) {

                    //组装审批岗位String
                    SpgwcyDto spgwcyDto = new SpgwcyDto();
                    spgwcyDto.setJsid(dto.getJsid());
                    spgwcyDto.setYhid(yhjsDto.getYhid());
                    List<SpgwcyDto> spgwList = spgwcyService.getDtoList(spgwcyDto);
                    if (spgwList != null) {
                        StringBuilder sb = new StringBuilder();
                        sb.setLength(0);
                        for (int j = 0; j < spgwList.size(); j++) {
                            if (j != 0)
                                sb.append(",");
                            sb.append(spgwList.get(j).getGwmc());
                        }
                        dto.setSpgwmc(sb.toString());
                    }

                    //组装机构String
                    YhjgqxDto yhjgqxDto = new YhjgqxDto();
                    yhjgqxDto.setJsid(dto.getJsid());
                    yhjgqxDto.setYhid(yhjsDto.getYhid());
                    List<YhjgqxDto> yhjgList = yhjgqxDao.getListByjsid(yhjgqxDto);
                    if (yhjgList != null) {
                        StringBuilder sb_t = new StringBuilder();
                        sb_t.setLength(0);
                        for (int j = 0; j < yhjgList.size(); j++) {
                            if (j != 0)
                                sb_t.append(",");
                            sb_t.append(yhjgList.get(j).getJgmc());
                        }
                        dto.setJgmc(sb_t.toString());
                    }
                }
            }
		}
		return yhjsDtos;
	}
	
	/**
	 * 根据用户ID查询权限信息
	 * @param yhid
	 * @return
	 */
	@Override
	public List<YhjsDto> getDtoListById(String yhid) {
		// TODO Auto-generated method stub
		return dao.getDtoListById(yhid);
	}
}
