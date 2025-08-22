package com.matridx.igams.wechat.control;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.ShlcDto;
import com.matridx.igams.common.dao.entities.ShxxDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XszbDto;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IShlcService;
import com.matridx.igams.common.service.svcinterface.IShxxService;
import com.matridx.igams.common.service.svcinterface.IXszbService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.wechat.dao.entities.BfdxglDto;
import com.matridx.igams.wechat.dao.entities.BfglDto;
import com.matridx.igams.wechat.dao.entities.DdyhDto;
import com.matridx.igams.wechat.dao.entities.FjsqDto;
import com.matridx.igams.wechat.dao.entities.FjsqyyDto;
import com.matridx.igams.wechat.dao.entities.SjdwxxDto;
import com.matridx.igams.wechat.dao.entities.SjhbxxDto;
import com.matridx.igams.wechat.dao.entities.SjqxDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxjgDto;
import com.matridx.igams.wechat.dao.entities.SjybztDto;
import com.matridx.igams.wechat.dao.entities.SjyzDto;
import com.matridx.igams.wechat.dao.entities.SjyzjgDto;
import com.matridx.igams.wechat.dao.entities.WxyhDto;
import com.matridx.igams.wechat.dao.entities.YhsjxxjgDto;
import com.matridx.igams.wechat.service.svcinterface.IBfdxglService;
import com.matridx.igams.wechat.service.svcinterface.IBfglService;
import com.matridx.igams.wechat.service.svcinterface.IDdyhService;
import com.matridx.igams.wechat.service.svcinterface.IFjsqService;
import com.matridx.igams.wechat.service.svcinterface.IFjsqyyService;
import com.matridx.igams.wechat.service.svcinterface.ISjhbxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjqxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxWsService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxjgService;
import com.matridx.igams.wechat.service.svcinterface.ISjybztService;
import com.matridx.igams.wechat.service.svcinterface.ISjyzService;
import com.matridx.igams.wechat.service.svcinterface.ISjyzjgService;
import com.matridx.igams.wechat.service.svcinterface.IWxyhService;
import com.matridx.igams.wechat.service.svcinterface.IYhsjxxjgService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 	/ws/dingtalk/sendGroupMessageAt 	钉钉群机器人发送群消息，可以@群人员
 *
 * */
@Controller
@RequestMapping("/ws")
public class DingTalkWSController extends BaseController{

	@Autowired
	IDdyhService ddyhService;
	@Autowired
	IWxyhService wxyhService;
	@Autowired
	ISjxxWsService sjxxWsService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	IFjsqService fjsqService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	ISjxxService sjxxService;
	@Autowired
	IShgcService shgcService;
	@Autowired
	ISjhbxxService sjhbxxService;
	@Autowired
	private IShxxService shxxService;
	@Autowired
	private IShlcService shlcService;
	@Autowired
	ICommonService commonService;
	@Autowired
	IBfglService bfglService;
	@Autowired
	IBfdxglService bfdxglService;
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	private ISjxxjgService sjxxjgService;
	@Autowired
	IYhsjxxjgService yhsjxxjgService;
	@Autowired
	ISjqxService sjqxService;
	@Autowired
	ISjyzService sjyzService;
	@Autowired
	ISjyzjgService sjyzjgService;
	@Autowired
	IFjsqyyService fjsqyyService;
	@Autowired
	DingTalkUtil dingTalkUtil;
	@Autowired
	private ISjybztService sjybztService;
	@Autowired
	private IXszbService xszbService;
	@Autowired
	private RedisUtil redisUtil;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Value("${matridx.wechat.externalurl:}")
	private String externalurl;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	private final Logger logger = LoggerFactory.getLogger(DingTalkWSController.class);

	@Override
	public String getPrefix(){
		return urlPrefix;
	}

	/**
	 * 获取复检列表
	 * @param userid
	 * @param sjxxDto
	 * 只有微信小程序使用，关于ddid方法删除
	 * @return
	 */
	@RequestMapping(value="/dingtalk/getRecheckList")
	@ResponseBody
	public Map<String,Object> getRecheckList(String userid, SjxxDto sjxxDto){
		Map<String,Object> map= new HashMap<>();
		List<SjxxDto> sjxxlist = null;
		if(StringUtil.isNotBlank(sjxxDto.getWxid())){
			WxyhDto wxyhDto  = new WxyhDto();
			wxyhDto.setWxid(sjxxDto.getWxid());
			List<WxyhDto> wxyhlist = wxyhService.getListBySameId(wxyhDto);
			if(!CollectionUtils.isEmpty(wxyhlist)){
				List<String> lrrys = new ArrayList<>();
				String yhid = null;
                for (WxyhDto dto : wxyhlist) {
                    lrrys.add(dto.getWxid());
                    if (StringUtil.isNotBlank(dto.getDdid())) {
                        lrrys.add(dto.getDdid());
                    }
                    if (StringUtil.isNotBlank(dto.getXtyhid())) {
                        yhid = dto.getXtyhid();
                    }
                }
				if(StringUtil.isNotBlank(yhid)){
					boolean result = ddyhService.getDwxd(yhid);
					if(result){
						List<SjhbxxDto> hblist = sjhbxxService.getDbList(null, sjxxDto.getWxid());
						if(CollectionUtils.isEmpty(hblist))
							return map;
						List<String> dbs= new ArrayList<>();
						dbs.addAll(hblist.stream().map(SjhbxxDto::getHbmc).collect(Collectors.toList()));
						sjxxDto.setDbs(dbs);
						sjxxDto.setDwxzbj("1");
					}
				}
				sjxxDto.setLrrys(lrrys);
				// 查询送检查看权限
				if(StringUtil.isNotBlank(wxyhlist.get(0).getUnionid())) {
					List<SjqxDto> sjqxDtos = sjqxService.getSjqxList(wxyhlist.get(0).getUnionid(), restTemplate);
					sjxxDto.setSjqxDtos(sjqxDtos);
				}
				sjxxlist = sjxxWsService.getWeChatSjxxList(sjxxDto);
			}
		}
		else if(StringUtil.isNotBlank(sjxxDto.getYhid())){
			User user = new User();
			user.setYhid(sjxxDto.getYhid());
			User userInfoById = commonService.getUserInfoById(user);
			boolean sfdwxd=ddyhService.getDwxd(userInfoById.getYhid());
			if(sfdwxd) {
				List<SjhbxxDto> hblist=sjhbxxService.getDbList(userInfoById.getDdid(), null);
				if(CollectionUtils.isEmpty(hblist))
					return map;
				List<String> dbs= new ArrayList<>();
				dbs.addAll(hblist.stream().map(SjhbxxDto::getHbmc).collect(Collectors.toList()));
				sjxxDto.setDbs(dbs);
			}
			sjxxlist = sjxxWsService.getMiniSjxxList(sjxxDto);
		}
		//查询状态 sjid lx(2种) 查询  lrsj最新
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.RECHECK});
		List<JcsjDto> fjlblist=jclist.get(BasicDataTypeEnum.RECHECK.getCode());
		map.put("fjlblist", fjlblist);
		if(!CollectionUtils.isEmpty(sjxxlist)){
            for (SjxxDto dto : sjxxlist) {
                if (!CollectionUtils.isEmpty(fjlblist)) {
                    for (JcsjDto jcsjDto : fjlblist) {
                        dto.setLx(jcsjDto.getCsid());
                        FjsqDto f_fjsqDto = fjsqService.getDtoZt(dto);
                        if (f_fjsqDto != null && StringUtil.isNotBlank(f_fjsqDto.getZt())) {
                            if ("ADDDETECT".equals(jcsjDto.getCsdm())) {
                                dto.setJczt(f_fjsqDto.getZt());
                                dto.setJcid(f_fjsqDto.getFjid());
                            } else if ("RECHECK".equals(jcsjDto.getCsdm())) {
                                dto.setFjzt(f_fjsqDto.getZt());
                                dto.setFcid(f_fjsqDto.getFjid());
                            } else if ("REM".equals(jcsjDto.getCsdm())) {
                                dto.setQryjczt(f_fjsqDto.getZt());
                                dto.setQryjcid(f_fjsqDto.getFjid());
                            } else if ("PK".equals(jcsjDto.getCsdm())) {
                                dto.setFjzt(f_fjsqDto.getZt());
                                dto.setFcid(f_fjsqDto.getFjid());
                            } else if ("LAB_RECHECK".equals(jcsjDto.getCsdm())) {
                                dto.setFjzt(f_fjsqDto.getZt());
                                dto.setFcid(f_fjsqDto.getFjid());
                            } else if ("LAB_REM".equals(jcsjDto.getCsdm())) {
                                dto.setQryjczt(f_fjsqDto.getZt());
                                dto.setQryjcid(f_fjsqDto.getFjid());
                            } else if ("LAB_ADDDETECT".equals(jcsjDto.getCsdm())) {
                                dto.setJczt(f_fjsqDto.getZt());
                                dto.setJcid(f_fjsqDto.getFjid());
                            }
                        }
                    }
                }
            }
		}

		map.put("list", sjxxlist);
		return map;
	}

	/**
	 * 查看复检审核
	 * @return
	 */
	@RequestMapping(value="/dingtalk/viewAudit")
	@ResponseBody
	public Map<String,Object> viewAudit(ShgcDto shgcDto, HttpServletRequest request) {
		Map<String,Object> map = new HashMap<>();
		try {
			ShxxDto shxxParam = new ShxxDto();
			ShgcDto d_shgcDto;
			// 未传递gcid，则根据xmid和shlb获取shgc
			if (StringUtil.isBlank(shgcDto.getGcid())) {
				// 获取当前审核过程
				d_shgcDto = shgcService.getDtoByYwid(shgcDto.getYwid());
			} else {// 传递了gcid，则根据gcid获取shgc
					// 获取当前审核过程
				d_shgcDto = shgcService.getDtoById(shgcDto.getGcid());
			}
			if (d_shgcDto == null) {// 数据不在审核中，表示通过
				shgcDto.setXlcxh("");// 审核通过时
			} else {
				shgcDto = d_shgcDto;
			}
			shxxParam.setShlb(shgcDto.getShlb());
			shxxParam.setYwid(shgcDto.getYwid());
			//shxxParam.setCommit(true);// 查询提交的数据
			shxxParam.setGcid(shgcDto.getGcid());// 用过程id进行查询

			shxxParam.setSftg(null);
			shxxParam.setCommit(false);
			List<ShxxDto> shxxList = shxxService.getShxxOrderByShsj(shxxParam);
			if (null == shxxList) {
				shxxList = new ArrayList<>();
			}

			// 获取的审核流程列表
			ShlcDto shlcParam = new ShlcDto();
			// gcid或shid参数不全的情况（已审核完，延期审核查看会传递gcid，所以gcid和shid都需要判断），根据审核信息获取流程
			if ((StringUtil.isBlank(shgcDto.getGcid()) || StringUtil.isBlank(shgcDto.getShid()))
					&& !CollectionUtils.isEmpty(shxxList)) {
				shlcParam.setShid(shxxList.get(0).getShid());
				shlcParam.setSqsj(shxxList.get(0).getSqsj());
			} else {
				shlcParam.setShid(shgcDto.getShid());
				shlcParam.setGcid(shgcDto.getGcid());// 处理旧流程判断用
			}
			if (StringUtil.isNotBlank(shlcParam.getShid())) {
				List<ShlcDto> shlcList = shlcService.getDtoList(shlcParam);
				if (StringUtil.isNotBlank(shlcParam.getGcid())) {
					if(StringUtil.isNotBlank(shgcDto.getXlcxh())){
						for (ShlcDto shlc : shlcList) {
							if (shlc.getLcxh().equals(shgcDto.getXlcxh())) {// 相等则是当前流程
								// 当前流程做标记
								shlc.setCurrent(true);// 当前流程
								shlc.setAudited(true); //已审核流程
								break;// 跳出for循环
							}
							shlc.setAudited(true); //已审核流程
						}
					}
				}else{
					for (ShlcDto shlc : shlcList) {
						shlc.setAudited(true); //已审核流程
					}
				}
				map.put("shlcList", shlcList);
			}
			map.put("shxxList", shxxList);
			map.put("shgcDto", shgcDto);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return map;
	}

	/**
	 * 获取复检状态
	 * @return
	 */
	@RequestMapping(value="/dingtalk/getRecheckZt")
	@ResponseBody
	public Map<String,Object> getRecheckZt(SjxxDto sjxxDto) {
        Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.RECHECK});
        List<JcsjDto> fjlblist=jclist.get(BasicDataTypeEnum.RECHECK.getCode());
		Map<String,Object> map = new HashMap<>();
		if(!CollectionUtils.isEmpty(fjlblist)) {
            for (JcsjDto jcsjDto : fjlblist) {
                sjxxDto.setLx(jcsjDto.getCsid());
                FjsqDto f_fjsqDto = fjsqService.getDtoZt(sjxxDto);
                if (f_fjsqDto != null && StringUtil.isNotBlank(f_fjsqDto.getZt())) {
                    if ("ADDDETECT".equals(jcsjDto.getCsdm())) {
                        sjxxDto.setJczt(f_fjsqDto.getZt());
                        sjxxDto.setJcid(f_fjsqDto.getFjid());
                    } else if ("RECHECK".equals(jcsjDto.getCsdm())) {
                        sjxxDto.setFjzt(f_fjsqDto.getZt());
                        sjxxDto.setFcid(f_fjsqDto.getFjid());
                    } else if ("REM".equals(jcsjDto.getCsdm())) {
                        sjxxDto.setQryjczt(f_fjsqDto.getZt());
                        sjxxDto.setQryjcid(f_fjsqDto.getFjid());
                    } else if ("PK".equals(jcsjDto.getCsdm())) {
                        sjxxDto.setFjzt(f_fjsqDto.getZt());
                        sjxxDto.setFcid(f_fjsqDto.getFjid());
                    } else if ("LAB_RECHECK".equals(jcsjDto.getCsdm())) {
                        sjxxDto.setFjzt(f_fjsqDto.getZt());
                        sjxxDto.setFcid(f_fjsqDto.getFjid());
                    } else if ("LAB_REM".equals(jcsjDto.getCsdm())) {
                        sjxxDto.setQryjczt(f_fjsqDto.getZt());
                        sjxxDto.setQryjcid(f_fjsqDto.getFjid());
                    } else if ("LAB_ADDDETECT".equals(jcsjDto.getCsdm())) {
                        sjxxDto.setJczt(f_fjsqDto.getZt());
                        sjxxDto.setJcid(f_fjsqDto.getFjid());
                    }
                }
            }
		}
		map.put("sjxxDto", sjxxDto);
		return map;
	}

	/**
	 * 查看
	 * @return
	 */
	@RequestMapping(value="/dingtalk/viewRecheck")
	@ResponseBody
	public Map<String,Object> viewRecheck(String sjid) {
		SjxxDto sjxxDto=new SjxxDto();
		sjxxDto.setSjid(sjid);
		SjxxDto sjxx=sjxxService.getDto(sjxxDto);
		Map<String,Object> map= new HashMap<>();
		if(sjxx== null)
			sjxx = new SjxxDto();
		map.put("sjxxDto", sjxx);
		return map;
	}

	/**
	 * 送检列表(检测单位限制)
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/dingtalk/getLimitSjxxList")
	@ResponseBody
	public Map<String,Object> getLimitSjxxList(SjxxDto sjxxDto){
		Map<String,Object> map= new HashMap<>();
		List<SjxxDto> jcdwByDdid = sjxxWsService.getJcdwByDdid(sjxxDto);
		String[] str=new String[jcdwByDdid.size()];
		for(int i=0;i<jcdwByDdid.size();i++){
			for(int j=0;j<str.length;j++){
				str[i]=jcdwByDdid.get(i).getJcdw();
			}
		}
		sjxxDto.setJcdws(str);
		List<SjxxDto> sjxxlist=sjxxWsService.getMiniSjxxList(sjxxDto);
		if(!CollectionUtils.isEmpty(sjxxlist)){
			SjxxDto paramDto=new SjxxDto();
			List<String> ybbhs=new ArrayList<>();
			for(SjxxDto sjxxDto1:sjxxlist){
				if(StringUtil.isNotBlank(sjxxDto1.getYbbh())){
					ybbhs.add(sjxxDto1.getYbbh());
				}
			}
			paramDto.setYbbhs(ybbhs);
			List<SjxxDto>xjsjList=sjxxService.getQdListByYbbh(paramDto);
			if(!CollectionUtils.isEmpty(xjsjList)){
				for(SjxxDto xjsjDto:xjsjList){
					for(SjxxDto sjxxDto1:sjxxlist){
						if(xjsjDto.getSjid().equals(sjxxDto1.getSjid())){
							sjxxDto1.setFxwcsj(xjsjDto.getFxwcsj());
						}
					}
				}
			}
		}
		map.put("list", sjxxlist);
		return map;
	}

	/**
	 * 送检列表(无检测单位限制)
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/dingtalk/getNoLimitSjxxList")
	@ResponseBody
	public Map<String,Object> getNoLimitSjxxList(SjxxDto sjxxDto){
		Map<String,Object> map= new HashMap<>();
		List<SjxxDto> sjxxlist=sjxxWsService.getMiniSjxxList(sjxxDto);
		if(!CollectionUtils.isEmpty(sjxxlist)){
			SjxxDto paramDto=new SjxxDto();
			List<String> ybbhs=new ArrayList<>();
			for(SjxxDto sjxxDto1:sjxxlist){
				if(StringUtil.isNotBlank(sjxxDto1.getYbbh())){
					ybbhs.add(sjxxDto1.getYbbh());
				}
			}
			paramDto.setYbbhs(ybbhs);
			List<SjxxDto>xjsjList=sjxxService.getQdListByYbbh(paramDto);
			if(!CollectionUtils.isEmpty(xjsjList)){
				for(SjxxDto xjsjDto:xjsjList){
					for(SjxxDto sjxxDto1:sjxxlist){
						if(xjsjDto.getSjid().equals(sjxxDto1.getSjid())){
							sjxxDto1.setFxwcsj(xjsjDto.getFxwcsj());
						}
					}
				}
			}
		}
		map.put("list", sjxxlist);
		return map;
	}

	/**
	 * 送检列表
	 * @param sjxxDto
	 * 只有微信小程序使用，关于ddid方法删除
	 * @return
	 */
	@RequestMapping(value="/dingtalk/getSjxxList")
	@ResponseBody
	public Map<String,Object> getSjxxList(SjxxDto sjxxDto){
		Map<String,Object> map= new HashMap<>();
		if(StringUtil.isNotBlank(sjxxDto.getWxid())){
			WxyhDto wxyhDto  = new WxyhDto();
			wxyhDto.setWxid(sjxxDto.getWxid());
			List<WxyhDto> wxyhlist = wxyhService.getListBySameId(wxyhDto);
			if(!CollectionUtils.isEmpty(wxyhlist)){
//				List<String> lrrys = new ArrayList<String>();
				String yhid = null;
                for (WxyhDto dto : wxyhlist) {
//					lrrys.add(wxyhlist.get(i).getWxid());
//					if(StringUtil.isNotBlank(wxyhlist.get(i).getDdid())){
//						lrrys.add(wxyhlist.get(i).getDdid());
//					}
                    if (StringUtil.isNotBlank(dto.getXtyhid())) {
                        yhid = dto.getXtyhid();
                    }

                }
				boolean result = ddyhService.getDwxd(yhid);
				if(result){
					List<SjhbxxDto> hblist=sjhbxxService.getDbList(null, sjxxDto.getWxid());
					if(CollectionUtils.isEmpty(hblist))
						return map;
					List<String> dbs= new ArrayList<>();
					dbs.addAll(hblist.stream().map(SjhbxxDto::getHbmc).collect(Collectors.toList()));
					sjxxDto.setDbs(dbs);
					sjxxDto.setDwxzbj("1");
				}
				// 查询送检查看权限
				if(StringUtil.isNotBlank(wxyhlist.get(0).getUnionid())) {
					List<SjqxDto> sjqxDtos = sjqxService.getSjqxList(wxyhlist.get(0).getUnionid(), restTemplate);
					sjxxDto.setSjqxDtos(sjqxDtos);
				}
				List<SjxxDto> sjxxlist=sjxxWsService.getWeChatSjxxList(sjxxDto);
				map.put("list", sjxxlist);
			}
		}
		else if(StringUtil.isNotBlank(sjxxDto.getDdid())){
			User user = new User();
			user.setYhid(sjxxDto.getYhid());
			User userInfoById = commonService.getUserInfoById(user);
			boolean sfdwxd=ddyhService.getDwxd(userInfoById.getYhid());
			if(sfdwxd) {
				List<SjhbxxDto> hblist=sjhbxxService.getDbList(sjxxDto.getDdid(), null);
				if(CollectionUtils.isEmpty(hblist))
					return map;
				List<String> dbs= new ArrayList<>();
				dbs.addAll(hblist.stream().map(SjhbxxDto::getHbmc).collect(Collectors.toList()));
				sjxxDto.setDbs(dbs);
			}
			List<SjxxDto> sjxxlist=sjxxWsService.getMiniSjxxList(sjxxDto);
			map.put("list", sjxxlist);
		}
		return map;
	}

	/**
	 * 获取未反馈列表
	 * @param userid
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/dingtalk/getFkList")
	@ResponseBody
	public Map<String,Object> getFeedbackList(String userid, SjxxDto sjxxDto){
		Map<String,Object> map= new HashMap<>();
		if(StringUtil.isNotBlank(sjxxDto.getWxid())){
			WxyhDto wxyhDto  = new WxyhDto();
			wxyhDto.setWxid(sjxxDto.getWxid());
			List<WxyhDto> wxyhlist = wxyhService.getListBySameId(wxyhDto);
			if(!CollectionUtils.isEmpty(wxyhlist)){
				List<String> lrrys = new ArrayList<>();
				String yhid = null;
                for (WxyhDto dto : wxyhlist) {
                    lrrys.add(dto.getWxid());
                    if (StringUtil.isNotBlank(dto.getDdid())) {
                        lrrys.add(dto.getDdid());
                    }
                    if (StringUtil.isNotBlank(dto.getXtyhid())) {
                        yhid = dto.getXtyhid();
                    }
                }
				boolean result = ddyhService.getDwxd(yhid);
				if(result){
					List<SjhbxxDto> hblist=sjhbxxService.getDbList(null, sjxxDto.getWxid());
					if(CollectionUtils.isEmpty(hblist))
						return map;
					List<String> dbs= new ArrayList<>();
					dbs.addAll(hblist.stream().map(SjhbxxDto::getHbmc).collect(Collectors.toList()));
					sjxxDto.setDbs(dbs);
					sjxxDto.setDwxzbj("1");
				}
				sjxxDto.setLrrys(lrrys);
				List<SjxxDto> sjxxlist=sjxxWsService.getWeChatFeedbackList(sjxxDto);
				map.put("list", sjxxlist);
			}
		}else if(StringUtil.isNotBlank(userid)){
			User user = new User();
			user.setYhid(sjxxDto.getYhid());
			User userInfoById = commonService.getUserInfoById(user);
			boolean sfdwxd=ddyhService.getDwxd(userInfoById.getYhid());
			sjxxDto.setDwxzbj("0");
			if(sfdwxd) {
				List<SjhbxxDto> hblist=sjhbxxService.getDbList(userid, null);
				if(CollectionUtils.isEmpty(hblist)) {
					map.put("list", "");
					return map;
				}
				List<String> dbs= new ArrayList<>();
				dbs.addAll(hblist.stream().map(SjhbxxDto::getHbmc).collect(Collectors.toList()));
				sjxxDto.setDbs(dbs);
				sjxxDto.setDwxzbj("1");
			}
			List<SjxxDto> sjxxlist=sjxxWsService.getMiniFeedbackList(sjxxDto);
			map.put("list", sjxxlist);
		}
		return map;
	}

	/**
	 * 获取复检页面
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/dingtalk/getRecheckSjxx")
	@ResponseBody
	public Map<String,Object>  recheckSjxx(SjxxDto sjxxDto) {
		Map<String,Object> map= new HashMap<>();
		SjxxDto sjxxDtos=sjxxService.getDto(sjxxDto);
		//检测项目获取方式修改 2020/09/18   姚嘉伟  modify
		List<JcsjDto> detectlist= redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode());
//		if(sjxxDtos.getJc_cskz2()!=null) {
//			JcsjDto jcsjDto=new JcsjDto();
//			String[] cskz2=sjxxDtos.getJc_cskz2().split(",");
//			jcsjDto.setCsdms(cskz2);
//			jcsjDto.setJclb(BasicDataTypeEnum.DETECT_TYPE.getCode());
//			detectlist=jcsjService.getJcsjDtoList(jcsjDto);
//		}
		//String cskz3=sjxxDtos.getCskz3()+"_REM";
		JcsjDto jcsj_REMDto=new JcsjDto();
		if(sjxxDtos.getCskz3()!=null && !sjxxDtos.getCskz3().endsWith("_REM")) {
			String cskz3=sjxxDtos.getCskz3()+"_REM";
			jcsj_REMDto.setCskz3(cskz3);
		}else {
			jcsj_REMDto.setCskz3(sjxxDtos.getCskz3());
		}
		jcsj_REMDto.setJclb(BasicDataTypeEnum.DETECT_TYPE.getCode());
		//jcsj_REMDto.setCskz3(cskz3);
		List<JcsjDto> REMList=jcsjService.getInstopDtoList(jcsj_REMDto);
		detectlist.addAll(REMList);
		map.put("detectlist", detectlist);//检测项目
		map.put("fjyylist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.RECHECK_REASON.getCode()));//复检原因
		map.put("sjxxDto",sjxxDtos);
		return map;
	}

	/**
	 * 复检保存(手机端)
	 * @param fjsqDto
	 * @return
	 */
	@RequestMapping("/dingtalk/recheckSaveSjxx")
	@ResponseBody
	public Map<String,Object> recheckSaveSjxx(FjsqDto fjsqDto,String userid, SjxxDto sjxxDto){
		Map<String,Object> map= new HashMap<>();
		boolean isSuccess=false;
		if(StringUtil.isNotBlank(sjxxDto.getWxid())){
			WxyhDto wxyhDto  = new WxyhDto();
			wxyhDto.setWxid(sjxxDto.getWxid());
			List<WxyhDto> wxyhlist = wxyhService.getListBySameId(wxyhDto);
			fjsqDto.setLrry(sjxxDto.getWxid());
			if(!CollectionUtils.isEmpty(wxyhlist)){
                for (WxyhDto dto : wxyhlist) {
                    if (StringUtil.isNotBlank(dto.getXtyhid())) {
                        fjsqDto.setLrry(dto.getXtyhid());
                        break;
                    }
                }
			}
		}else if(StringUtil.isNotBlank(sjxxDto.getYhid())){
			fjsqDto.setLrry(sjxxDto.getYhid());
		}else{
			map.put("status","fail");
			map.put("message","未找到用户信息！");
			return map;
		}
		//复检申请量仅一次处理   author  姚嘉伟    modify 2020/10/09  start
		SjybztDto sjybztDto = new SjybztDto();
		List<String> sjybztCskz1s=new ArrayList<>();
		sjybztCskz1s.add("S");
		sjybztCskz1s.add("T");
		sjybztDto.setSjid(sjxxDto.getSjid());
		sjybztDto.setYbztCskz1s(sjybztCskz1s);
		List<SjybztDto> sjybztDtos = sjybztService.getDtoList(sjybztDto);
		if(!CollectionUtils.isEmpty(sjybztDtos)) {
			//判断是否为去人源项目，如果是则正常走,否则报量仅一次
			JcsjDto jcxm=jcsjService.getDtoById(fjsqDto.getJcxm());
            for (SjybztDto dto : sjybztDtos) {
                if ("S".equals(dto.getYbztCskz1())) {
                    map.put("status", "fail");
                    map.put("message", "量仅一次！");
                    return map;
                }
                if (!jcxm.getCskz3().contains("REM")) {
                    map.put("status", "fail");
                    map.put("message", "仅可去人源！");
                    return map;
                }
            }
		}
		List<JcsjDto> list = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.RECHECK.getCode());
		String csdm="";
		for(JcsjDto dto:list){
			if(dto.getCsid().equals(fjsqDto.getLx())){
				csdm=dto.getCsdm();
				break;
			}
		}
		List<String> lxs=new ArrayList<>();
		if(StringUtil.isNotBlank(csdm)){
			String[] strings = csdm.split("_");
			for(JcsjDto dto:list){
				if(dto.getCsdm().contains(strings[strings.length - 1])){
					lxs.add(dto.getCsid());
				}
			}
		}
		FjsqDto fjsqDto_t=new FjsqDto();
		fjsqDto_t.setSjid(fjsqDto.getSjid());
		fjsqDto_t.setFjlxs(lxs);
		int num=fjsqService.getCount(fjsqDto_t);//查询当前送检信息是否已经存在
		fjsqDto.setShlx(AuditTypeEnum.AUDIT_DING_RECHECK.getCode());
		if(num>0){ //num>0说明已经存在，然后去判断状态
			List<FjsqDto> fjsqDtos = fjsqService.getDtoList(fjsqDto_t);
			if(!CollectionUtils.isEmpty(fjsqDtos)) {
				map.put("auditType",AuditTypeEnum.AUDIT_DING_RECHECK);
                for (FjsqDto dto : fjsqDtos) {
                    if (StatusEnum.CHECK_NO.getCode().equals(dto.getZt())) {
                        map.put("status", "success");
                        map.put("ywid", dto.getFjid());
                        return map;
                    }
                    if (StatusEnum.CHECK_SUBMIT.getCode().equals(dto.getZt())) {
                        map.put("status", isSuccess ? "success" : "fail");
                        map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr() : "该信息正在审核中！");
                        return map;
                    }
                    //如果状态是审核不通过，则进行修改操作
                    if (StatusEnum.CHECK_UNPASS.getCode().equals(dto.getZt())) {
                        dto.setXgry(fjsqDto.getLrry());
                        dto.setZt(StatusEnum.CHECK_NO.getCode());
                        dto.setSfff(fjsqDto.getSfff());
                        isSuccess = fjsqService.updateZt(dto);
                        map.put("ywid", dto.getFjid());
                        map.put("status", isSuccess ? "success" : "fail");
                        map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr() : "状态修改失败！");
                        return map;
                    }
                    if (!StatusEnum.CHECK_PASS.getCode().equals(dto.getZt()) && !StatusEnum.CHECK_UNPASS.getCode().equals(dto.getZt())) {
                        return map;
                    }
                }
				//如果状态是审核通过，则新增一条记录
				fjsqDto.setZt(StatusEnum.CHECK_NO.getCode());
				if(StringUtil.isBlank(fjsqDto.getFjid())){
					fjsqDto.setFjid(StringUtil.generateUUID());
				}
				isSuccess=fjsqService.insertFjsq(fjsqDto);
				//保存原因
				if(fjsqDto.getYys()!=null && fjsqDto.getYys().length>0){
					List<FjsqyyDto> fjsqyyList=new ArrayList<>();
					for(int i=0;i<fjsqDto.getYys().length;i++){
						FjsqyyDto fjsqyyDto=new FjsqyyDto();
						fjsqyyDto.setFjid(fjsqDto.getFjid());
						fjsqyyDto.setYy(fjsqDto.getYys()[i]);
						fjsqyyList.add(fjsqyyDto);
					}
					fjsqyyService.addDtoList(fjsqyyList);
				}
				map.put("status",isSuccess?"success":"fail");
				map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
				map.put("ywid",fjsqDto.getFjid());
			}
		}else if(num==0){
			//如果num=0，说明不存在，直接进行添加操作
			fjsqDto.setZt(StatusEnum.CHECK_NO.getCode());
			if(StringUtil.isBlank(fjsqDto.getFjid())){
				fjsqDto.setFjid(StringUtil.generateUUID());
			}
			isSuccess=fjsqService.insertFjsq(fjsqDto);
			//保存原因
			if(fjsqDto.getYys()!=null && fjsqDto.getYys().length>0){
				List<FjsqyyDto> fjsqyyList=new ArrayList<>();
				for(int i=0;i<fjsqDto.getYys().length;i++){
					FjsqyyDto fjsqyyDto=new FjsqyyDto();
					fjsqyyDto.setFjid(fjsqDto.getFjid());
					fjsqyyDto.setYy(fjsqDto.getYys()[i]);
					fjsqyyList.add(fjsqyyDto);
				}
				fjsqyyService.addDtoList(fjsqyyList);
			}
			map.put("status",isSuccess?"success":"fail");
			map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
			map.put("ywid",fjsqDto.getFjid());
			map.put("auditType",AuditTypeEnum.AUDIT_DING_RECHECK);
		}
		if(isSuccess)
			sjxxService.checkSybgSfwc();
		return map;
	}

	/**
	 * 跳转反馈界面
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/dingtalk/feedback")
	@ResponseBody
	public Map<String,Object> getFeedBackPage(SjxxDto sjxxDto) {
		SjxxDto sjxxDto2=sjxxService.getDto(sjxxDto);
		sjxxDto2.setYwlx(BusTypeEnum.IMP_FEEDBACK.getCode());
		//查看附件
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwid(sjxxDto2.getSjid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_FEEDBACK.getCode());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		Map<String,Object> map= new HashMap<>();
		map.put("sjxxDto", sjxxDto2);
		map.put("fjxx", fjcfbDtos);
		return map;
	}

	/**
	 * 提交审核
	 * @return
	 */
	@RequestMapping(value ="/dingtalk/commCommitAudit")
	@ResponseBody
	public Map<String,Object> commCommitAudit(ShgcDto shgcDto,String userid,SjxxDto sjxxDto){
		Map<String, Object> result = new HashMap<>();
		User user = new User();
		StringBuilder str_ywids= new StringBuilder();
		if(!CollectionUtils.isEmpty(shgcDto.getYwids())) {
			for(int i=0;i<shgcDto.getYwids().size();i++) {
				str_ywids.append(",").append("\"").append(shgcDto.getYwids().get(i)).append("\"");
			}
			str_ywids = new StringBuilder(str_ywids.substring(1));
			str_ywids = new StringBuilder("[" + str_ywids + "]");
		}
		try{
			if(StringUtil.isNotBlank(sjxxDto.getWxid())){
				WxyhDto wxyhDto  = new WxyhDto();
				wxyhDto.setWxid(sjxxDto.getWxid());
				List<WxyhDto> wxyhlist = wxyhService.getListBySameId(wxyhDto);
				user.setYhid(sjxxDto.getWxid());
				if(!CollectionUtils.isEmpty(wxyhlist)){
                    for (WxyhDto dto : wxyhlist) {
                        if (StringUtil.isNotBlank(dto.getXtyhid())) {
                            user.setYhid(dto.getXtyhid());
                            user.setZsxm(dto.getZsxm());
                            user.setYhm(dto.getYhm());
                            user.setWbcxid(dto.getWbcxid());
                            break;
                        }
                    }
					shgcDto.setExtend_1(str_ywids.toString());
					result = shgcService.checkAndCommit(shgcDto,user);
				}
			}else if(StringUtil.isNotBlank(sjxxDto.getYhid())){
				User u_t = new User();
				u_t.setYhid(sjxxDto.getYhid());
				User userInfoById = commonService.getUserInfoById(u_t);
				user.setYhid(userInfoById.getYhid());
				user.setZsxm(userInfoById.getZsxm());
				user.setYhm(userInfoById.getYhm());
				user.setWbcxid(userInfoById.getWbcxid());
				shgcDto.setExtend_1(str_ywids.toString());
				result = shgcService.checkAndCommit(shgcDto,user);
			}else{
				result.put("status", "fail");
				result.put("message", "未接收到外部用户信息！");
			}
		} catch (BusinessException e) {
			result.put("status", "fail");
			result.put("message", xxglService.getModelById("ICOM99016").getXxnr() + (StringUtil.isBlank(e.getMsg())?"":e.getMsg()));
			logger.error(e.toString());
		} catch (Exception e) {
			result.put("status", "fail");
			result.put("message",  xxglService.getModelById("ICOM99016").getXxnr());
			logger.error(e.toString());
		}
		return result;
	}

	/**
	 * 钉钉小程序查看个人日报
	 * @return
	 */
	@RequestMapping(value="/dingtalk/getDaily")
	@ResponseBody
	public Map<String,Object> getDailyUrl(HttpServletRequest request) {
		Map<String,Object> map= new HashMap<>();
		if (StringUtil.isNotBlank(request.getParameter("yhid"))){
			map = sjxxService.sendStatisDailyToMiniProgram(request.getParameter("yhid"),request);
			return map;
		}
		DdyhDto ddyhDto = ddyhService.getUserByDdid(request.getParameter("userid"));
		if(ddyhDto != null){
			map = sjxxService.sendStatisDailyToMiniProgram(ddyhDto.getYhid(),request);
		}
		return map;
	}

	/**
	 * 钉钉小程序查看个人周报
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/dingtalk/getweekly")
	@ResponseBody
	public Map<String,Object> getWeekly(HttpServletRequest request){
		Map<String,Object> map= new HashMap<>();
		if (StringUtil.isNotBlank(request.getParameter("yhid"))){
			map = sjxxService.sendStatisWeeklyToMiniProgram(request.getParameter("yhid"),request);
			return map;
		}
		DdyhDto ddyhDto=ddyhService.getUserByDdid(request.getParameter("userid"));
		if(ddyhDto != null){
			map = sjxxService.sendStatisWeeklyToMiniProgram(ddyhDto.getYhid(),request);
		}
		return map;
	}


	/**
	 * 微信小程序查看个人日报
	 * @return
	 */
	@RequestMapping(value="/wechat/getWeChatDaily")
	@ResponseBody
	public Map<String,Object> getWeChatDaily(HttpServletRequest request) {
		Map<String,Object> map= new HashMap<>();
		WxyhDto wxyhDto  = new WxyhDto();
		wxyhDto.setWxid(request.getParameter("userid"));
		List<WxyhDto> wxyhlist = wxyhService.getListBySameId(wxyhDto);
		if(!CollectionUtils.isEmpty(wxyhlist)){
            for (WxyhDto dto : wxyhlist) {
                if (StringUtil.isNotBlank(dto.getXtyhid())) {
                    map = sjxxService.sendStatisDailyToMiniProgram(dto.getXtyhid(), request);
                    break;
                }
            }
		}
		return map;
	}

	/**
	 * 微信小程序查看个人周报
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/wechat/getWeChatWeekly")
	@ResponseBody
	public Map<String,Object> getWeChatWeekly(HttpServletRequest request){
		Map<String,Object> map= new HashMap<>();
		WxyhDto wxyhDto  = new WxyhDto();
		wxyhDto.setWxid(request.getParameter("userid"));
		List<WxyhDto> wxyhlist = wxyhService.getListBySameId(wxyhDto);
		if(!CollectionUtils.isEmpty(wxyhlist)){
            for (WxyhDto dto : wxyhlist) {
                if (StringUtil.isNotBlank(dto.getXtyhid())) {
                    map = sjxxService.sendStatisWeeklyToMiniProgram(dto.getXtyhid(), request);
                    break;
                }
            }
		}
		return map;
	}

	/**
	 * 获取筛选条件
	 * @return
	 */
	@RequestMapping(value="/dingtalk/getfilter")
	@ResponseBody
	public Map<String,Object> getFilter(HttpServletRequest request){
		Map<String,Object> map= new HashMap<>();
		List<SjdwxxDto> sjdwxxlist=sjxxService.getSjdw();
		map.put("sjdwxxlist", sjdwxxlist);
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.WECGATSAMPLE_TYPE,BasicDataTypeEnum.FIRST_SJXXKZ,BasicDataTypeEnum.SECOND_SJXXKZ,BasicDataTypeEnum.THIRD_SJXXKZ,BasicDataTypeEnum.FOURTH_SJXXKZ,BasicDataTypeEnum.DETECT_TYPE,BasicDataTypeEnum.SD_TYPE,BasicDataTypeEnum.DETECTION_UNIT});
		map.put("samplelist", jclist.get(BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));//标本类型
		map.put("detectlist", jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode()));//检测项目
		map.put("cskz1List", jclist.get(BasicDataTypeEnum.FIRST_SJXXKZ.getCode()));
		map.put("cskz2List", jclist.get(BasicDataTypeEnum.SECOND_SJXXKZ.getCode()));
		map.put("cskz3List", jclist.get(BasicDataTypeEnum.THIRD_SJXXKZ.getCode()));
		map.put("cskz4List", jclist.get(BasicDataTypeEnum.FOURTH_SJXXKZ.getCode()));
		map.put("expressage", jclist.get(BasicDataTypeEnum.SD_TYPE.getCode()));//快递类型
		map.put("jcdwlist", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位类型
		screenClassColumns(request,map);
		return map;
	}

	/**
	 * 获取筛选条件(送检验证)
	 * @return
	 */
	@RequestMapping(value="/dingtalk/getFilterForVerifi")
	@ResponseBody
	public Map<String,Object> getFilterForVerifi(HttpServletRequest request){
		Map<String,Object> map= new HashMap<>();
		map.put("verificationresultList", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.VERIFICATION_RESULT.getCode()));//送检验证结果
		map.put("verificationList", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.VERIFICATION_TYPE.getCode()));//送检验证类型
		map.put("samplelist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));//标本类型
		map.put("detectlist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode()));//检测项目
		map.put("distinguishList", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DISTINGUISH.getCode()));//区分
		map.put("clientnoticeList", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.CLIENT_NOTICE.getCode()));//客户通知
//		screenClassColumns(request,map);
		return map;
	}

	/**
	 * 小程序 所有日报页面
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/dingtalk/getAlldaily")
	public  ModelAndView getAlldaily(HttpServletRequest req,SjxxDto sjxxDto) {
		ModelAndView mav=new ModelAndView("common/view/display_view");
		String sign="";
        sign = URLEncoder.encode(commonService.getSign(sjxxDto.getJsrq()), StandardCharsets.UTF_8);
        //String netflg = req.getParameter("netflg");
		//if(StringUtil.isNotBlank(netflg) && netflg.equals("inter")){
			//内网
		String url=applicationurl+"/ws/statistics/getDaily?jsrq="+sjxxDto.getJsrq()+"&sign="+sign;
		//}else {
			//外网
			//url=externalurl+"/ws/statistics/getDaily?jsrq="+sjxxDto.getJsrq()+"&sign="+sign;
		//}
		mav.addObject("view_url",url);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 小程序 所有ResFirst™日报页面
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/dingtalk/getAllRfsdaily")
	public  ModelAndView getAllRfsdaily(HttpServletRequest req,SjxxDto sjxxDto) {
		ModelAndView mav=new ModelAndView("common/view/display_view");
		String sign="";
        sign = URLEncoder.encode(commonService.getSign(sjxxDto.getJsrq()), StandardCharsets.UTF_8);
        //String netflg = req.getParameter("netflg");
		//if(StringUtil.isNotBlank(netflg) && netflg.equals("inter")){
			//内网
		String url=applicationurl+"/ws/statistics/getRfsDaily?jsrq="+sjxxDto.getJsrq()+"&sign="+sign;
		//}else {
			//外网
			//url=externalurl+"/ws/statistics/getDaily?jsrq="+sjxxDto.getJsrq()+"&sign="+sign;
		//}
		mav.addObject("view_url",url);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 小程序日报页面
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/statistics/mini_daliy")
	public  ModelAndView local_daliy(SjxxDto sjxxDto) {
		if(StringUtil.isNotBlank(sjxxDto.getJsrq())) {
			//直接访问日报
			ModelAndView mav=new ModelAndView("wechat/statistics/statistics_day_lead");
			String sign=commonService.getSign(sjxxDto.getJsrq());
            sjxxDto.setSign(sign);
			//0 代表钉钉这边调用  1代表网页这边调用
			String load_flg="1";
			//查询区域信息
			XszbDto xszbDto = new XszbDto();
			xszbDto.setJsmc("大区经理");
			List<XszbDto> xszbDtos = xszbService.getXszbQyxx(xszbDto);
			mav.addObject("xszbDtos", xszbDtos);
			mav.addObject("sjxxDto", sjxxDto);
			mav.addObject("load_flg", load_flg);
			return mav;
		}else {//先访问单独页面之后再去访问日报
			ModelAndView mav=new ModelAndView("wechat/statistics/pageListLocal_weekly");
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			String jsrq=sdf.format(new Date());
			mav.addObject("jsrq", jsrq);
			return mav;
		}
	}

	/**
	 * 小程序 所有周报页面
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/dingtalk/getAllweekly")
	public  ModelAndView getAllweekly(HttpServletRequest req,SjxxDto sjxxDto) {
		ModelAndView mav=new ModelAndView("common/view/display_view");
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		int dayOfWeek;
		String jsrq=sjxxDto.getJsrq();
		String jsrqstart="";
		String jsrqend="";
		try {
			if(StringUtils.isNotBlank(jsrq)) {
				dayOfWeek = DateUtils.getWeek(sf.parse(jsrq));
			}else {
				jsrq=sf.format(new Date());
				dayOfWeek = DateUtils.getWeek(sf.parse(jsrq));
			}
			if (dayOfWeek <= 2) {
				jsrqstart = DateUtils.format(DateUtils.getDate(sf.parse(jsrq), -dayOfWeek - 7), "yyyy-MM-dd");
				jsrqend = DateUtils.format(DateUtils.getDate(sf.parse(jsrq), -dayOfWeek - 1), "yyyy-MM-dd");
			} else {
				jsrqstart = DateUtils.format(DateUtils.getDate(sf.parse(jsrq), -dayOfWeek), "yyyy-MM-dd");
				jsrqend = DateUtils.format(DateUtils.getDate(sf.parse(jsrq), 6 - dayOfWeek), "yyyy-MM-dd");
			}
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String sign="";
        sign = URLEncoder.encode(commonService.getSign(jsrqstart+jsrqend), StandardCharsets.UTF_8);
        //String netflg = req.getParameter("netflg");
		//if(StringUtil.isNotBlank(netflg) && netflg.equals("inter")){
			//内网
		String url=applicationurl+"/ws/statistics/weekLeadStatisPage?jsrqstart="+jsrqstart+"&jsrqend="+jsrqend+"&sign="+sign;
		//}else {
			//外网
			//url=externalurl+"/ws/statistics/weekLeadStatisPage?jsrqstart="+jsrqstart+"&jsrqend="+jsrqend+"&sign="+sign;
		//}
		mav.addObject("view_url",url);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 小程序 所有周报页面
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/dingtalk/getAllweeklyByJsrq")
	public  ModelAndView getAllweeklyByJsrq(HttpServletRequest req,SjxxDto sjxxDto) {
		ModelAndView mav=new ModelAndView("common/view/display_view");
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		int dayOfWeek;
		String jsrq=sjxxDto.getJsrq();
		String jsrqstart="";
		String jsrqend="";
		try {
			if(StringUtils.isNotBlank(jsrq)) {
				dayOfWeek = DateUtils.getWeek(sf.parse(jsrq));
			}else {
				jsrq=sf.format(new Date());
				dayOfWeek = DateUtils.getWeek(sf.parse(jsrq));
			}
			if (dayOfWeek <= 2) {
				jsrqstart = DateUtils.format(DateUtils.getDate(sf.parse(jsrq), -dayOfWeek - 7), "yyyy-MM-dd");
				jsrqend = DateUtils.format(DateUtils.getDate(sf.parse(jsrq), -dayOfWeek - 1), "yyyy-MM-dd");
			} else {
				jsrqstart = DateUtils.format(DateUtils.getDate(sf.parse(jsrq), -dayOfWeek), "yyyy-MM-dd");
				jsrqend = DateUtils.format(DateUtils.getDate(sf.parse(jsrq), 6 - dayOfWeek), "yyyy-MM-dd");
			}
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			logger.error(e1.toString());
		}
		String sign="";
        sign = URLEncoder.encode(commonService.getSign(jsrqstart+jsrqend), StandardCharsets.UTF_8);
        //String netflg = req.getParameter("netflg");
		//if(StringUtil.isNotBlank(netflg) && netflg.equals("inter")){
			//内网
		String url=applicationurl+"/ws/statistics/weekLeadStatisPageByJsrq?jsrqstart="+jsrqstart+"&jsrqend="+jsrqend+"&sign="+sign;
		//}else {
			//外网
			//url=externalurl+"/ws/statistics/weekLeadStatisPage?jsrqstart="+jsrqstart+"&jsrqend="+jsrqend+"&sign="+sign;
		//}
		mav.addObject("view_url",url);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 获取内外网地址
	 * @return
	 */
	@RequestMapping(value="/common/getNet")
	@ResponseBody
	public Map<String,Object> getNet(HttpServletRequest request){
		Map<String,Object> map= new HashMap<>();
		map.put("url", externalurl);
		map.put("interurl", applicationurl);
		return map;
	}

	/**
	 * 小程序点击拜访登记界面的单位获取该用户已录入的拜访单位
	 * @param bfglDto
	 * @return
	 */
	@RequestMapping("/dingtalk/getVisitRegistrationList")
	@ResponseBody
	public Map<String,Object> getVisitRegistrationList(BfglDto bfglDto){
		Map<String,Object> map = new HashMap<>();
		bfglDto=getXtyhList(bfglDto);
		List<BfglDto> bfglDtos=bfglService.getDtoByLrry(bfglDto);
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.VISITUNIT_CLASSIFY,BasicDataTypeEnum.VISITUNIT_TYPE,BasicDataTypeEnum.VISITUNIT_GRADE,BasicDataTypeEnum.PROVINCE});
		map.put("bfdwfllist", jclist.get(BasicDataTypeEnum.VISITUNIT_CLASSIFY.getCode()));//拜访单位分类
		map.put("bfdwlblist", jclist.get(BasicDataTypeEnum.VISITUNIT_TYPE.getCode()));//拜访单位类别
		map.put("bfdwdjlist", jclist.get(BasicDataTypeEnum.VISITUNIT_GRADE.getCode()));//拜访单位等级
		map.put("bfdwsflist", jclist.get(BasicDataTypeEnum.PROVINCE.getCode()));//拜访单位省份
		map.put("bfglDtos", bfglDtos);
        return map;
	}

	/**
	 * 小程序保存拜访登记信息
	 * @param bfglDto
	 * @return
	 */
	@RequestMapping("/dingtalk/addSaveVisitRegistration")
	@ResponseBody
	public Map<String,Object> addSaveVisitRegistration(BfglDto bfglDto){
		Map<String,Object> map= new HashMap<>();
		boolean isSuccess=bfglService.insertDto(bfglDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 小程序保存拜访对象信息
	 * @param bfdxglDto
	 * @return
	 */
	@RequestMapping("/dingtalk/addSaveVisitTarget")
	@ResponseBody
	public Map<String,Object> addSaveVisitTarget(BfdxglDto bfdxglDto){
		Map<String,Object> map= new HashMap<>();
		boolean isSuccess=bfdxglService.insertDto(bfdxglDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 小程序获取个人拜访清单/全部拜访清单
	 * @param bfglDto
	 * @return
	 */
	@RequestMapping("/dingtalk/getPersionalVisitList")
	@ResponseBody
	public Map<String,Object> getPersionalVisitList(BfglDto bfglDto){
		Map<String,Object> map = new HashMap<>();
		bfglDto=getXtyhList(bfglDto);
		List<BfglDto> list=bfglService.getPersonalListByLrry(bfglDto);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
            for (BfglDto dto : list) {
                Date qsdate = df.parse(dto.getBfsjqs());
                Date jsdate = df.parse(dto.getBfsjjs());
                long l = jsdate.getTime() - qsdate.getTime();
                long day = l / (24 * 60 * 60 * 1000);
                long hour = (l / (60 * 60 * 1000) - day * 24);
                long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
                dto.setBfsc(day + "天" + hour + "小时" + min + "分");
            }
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error(e.toString());
		}
		//钉钉小程序获取全部拜访统计数据
		Map<String,Object> t_map=getStatictisByWeek(bfglDto);
		map.put("statictis", t_map.get("statictis"));
		map.put("list", list);
		return map;
	}

	/**
	 * 钉钉小程序获取本周拜访统计数据
	 * @param bfglDto
	 * @return
	 */
	@RequestMapping("/dingtalk/getStatictisByWeek")
	@ResponseBody
	public Map<String,Object> getStatictisByWeek(BfglDto bfglDto){
		Map<String,Object> map = new HashMap<>();
		bfglDto=getXtyhList(bfglDto);
		int dayOfWeek = DateUtils.getWeek(new Date());
		if (dayOfWeek <= 2){
			bfglDto.setTjrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 7), "yyyy-MM-dd"));
			bfglDto.setTjrqend(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd"));
		}else{
			bfglDto.setTjrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek), "yyyy-MM-dd"));
			bfglDto.setTjrqend(DateUtils.format(DateUtils.getDate(new Date(), 6 - dayOfWeek), "yyyy-MM-dd"));
		}
		BfglDto statictis=bfglService.getStatictisByWeek(bfglDto);
		map.put("statictis", statictis);
		return map;
	}

	/**
	 * 钉钉小程序获取本月拜访统计数据
	 * @param bfglDto
	 * @return
	 */
	@RequestMapping("/dingtalk/getStatictisByMonth")
	@ResponseBody
	public Map<String,Object> getStatictisByMonth(BfglDto bfglDto){
		Map<String,Object> map = new HashMap<>();
		bfglDto=getXtyhList(bfglDto);
		bfglDto.setTjrqstart(DateUtils.format(new Date(), "yyyy-MM"));
		BfglDto statictis=bfglService.getStatictisByMonth(bfglDto);
		map.put("statictis", statictis);
		return map;
	}

	/**
	 * 钉钉小程序获取全部拜访统计数据
	 * @param bfglDto
	 * @return
	 */
	@RequestMapping("/dingtalk/getStatictisByAll")
	@ResponseBody
	public Map<String,Object> getStatictisByAll(BfglDto bfglDto){
		Map<String,Object> map = new HashMap<>();
		bfglDto=getXtyhList(bfglDto);
		BfglDto statictis=bfglService.getStatictisByAll(bfglDto);
		map.put("statictis", statictis);
		return map;
	}

	/**
	 * 钉钉小程序获取本日拜访统计数据
	 * @param bfglDto
	 * @return
	 */
	@RequestMapping("/dingtalk/getStatictisByDay")
	@ResponseBody
	public Map<String,Object> getStatictisByDay(BfglDto bfglDto){
		Map<String,Object> map = new HashMap<>();
		bfglDto=getXtyhList(bfglDto);
		bfglDto.setTjrqstart(DateUtils.format(new Date(), "yyyy-MM-dd"));
		BfglDto statictis=bfglService.getStatictisByDAY(bfglDto);
		map.put("statictis", statictis);
		return map;
	}

	/**
	 * 日期参数封装方法
	 * @param bfglDto
	 * @return
	 */
	public BfglDto setDate(BfglDto bfglDto) {
		if(("day").equals(bfglDto.getTjtj_flag())) {
			bfglDto.setTjrqstart(DateUtils.format(new Date(), "yyyy-MM-dd"));
		}
		if(("week").equals(bfglDto.getTjtj_flag())) {
			int dayOfWeek = DateUtils.getWeek(new Date());
			if (dayOfWeek <= 2){
				bfglDto.setTjrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 7), "yyyy-MM-dd"));
				bfglDto.setTjrqend(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd"));
			}else{
				bfglDto.setTjrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek), "yyyy-MM-dd"));
				bfglDto.setTjrqend(DateUtils.format(DateUtils.getDate(new Date(), 6 - dayOfWeek), "yyyy-MM-dd"));
			}
		}
		if(("month").equals(bfglDto.getTjtj_flag())) {
			bfglDto.setTjrqstart(DateUtils.format(new Date(), "yyyy-MM"));
		}
		return bfglDto;
	}

	/**
	 * 全部清单时获取拜访人list
	 * @param bfglDto
	 * @return
	 */
	@RequestMapping("/dingtalk/getBfrlist")
	@ResponseBody
	public Map<String,Object> getBfrlist(BfglDto bfglDto){
		Map<String,Object> map = new HashMap<>();
		bfglDto=setDate(bfglDto);
		List<BfglDto> bfrlist=bfglService.getBfrByTjrq(bfglDto);
		map.put("bfrlist", bfrlist);
		return map;
	}

	/**
	 * 钉钉小程序查询本  周/日/月/全  拜访次数统计图(根据单位区分)
	 * @param bfglDto
	 * @return
	 */
	@RequestMapping("/dingtalk/getStatictisCountByDwid")
	@ResponseBody
	public Map<String,Object> getStatictisCountByDwid(BfglDto bfglDto){
		Map<String,Object> map = new HashMap<>();
		bfglDto=getXtyhList(bfglDto);
		bfglDto=setDate(bfglDto);
		//全部拜访清单是获取统计周期内拜访人信息
		if(("all").equals(bfglDto.getList_flg())) {
			List<BfglDto> bfrlist=bfglService.getBfrByTjrq(bfglDto);
			map.put("bfrlist", bfrlist);
		}
		List<BfglDto> statictislist=bfglService.getStatictisCountByDwid(bfglDto);
		map.put("statictislist", statictislist);
		return map;
	}

	/**
	 * 钉钉小程序查询本  周/日/月/全  拜访次数统计图(根据单位区分)
	 * @param bfglDto
	 * @return
	 */
	@RequestMapping("/dingtalk/getStatictisCountByBfr")
	@ResponseBody
	public Map<String,Object> getStatictisCountByBfy(BfglDto bfglDto){
		Map<String,Object> map = new HashMap<>();
		bfglDto=getXtyhList(bfglDto);
		bfglDto=setDate(bfglDto);
		//全部拜访清单是获取统计周期内拜访人信息
		if(("all").equals(bfglDto.getList_flg())) {
			List<BfglDto> bfrlist=bfglService.getBfrByTjrq(bfglDto);
			map.put("bfrlist", bfrlist);
		}
		List<BfglDto> statictislist=bfglService.getStatictisCountByBfr(bfglDto);
		map.put("statictislist", statictislist);
		return map;
	}

	/**
	 * 钉钉小程序查询本  周/日/月/全  拜访单位个数统计图(根据拜访时间起始区分)
	 * @param bfglDto
	 * @return
	 */
	@RequestMapping("/dingtalk/getStatictisDwgsByBfsjqs")
	@ResponseBody
	public Map<String,Object> getStatictisDwgsByBfsjqs(BfglDto bfglDto){
		Map<String,Object> map = new HashMap<>();
		bfglDto=getXtyhList(bfglDto);
		bfglDto=setDate(bfglDto);
		//全部拜访清单是获取统计周期内拜访人信息
		if(("all").equals(bfglDto.getList_flg())) {
			List<BfglDto> bfrlist=bfglService.getBfrByTjrq(bfglDto);
			map.put("bfrlist", bfrlist);
		}
		List<BfglDto> statictislist=bfglService.getStatictisDwgsByBfsjqs(bfglDto);
		map.put("statictislist", statictislist);
		return map;
	}

	/**
	 * 钉钉小程序查询本  周/日/月/全  拜访单位个数统计图(根据拜访时间起始区分)
	 * @param bfglDto
	 * @return
	 */
	@RequestMapping("/dingtalk/getStatictisDwgsByBfr")
	@ResponseBody
	public Map<String,Object> getStatictisDwgsByBfr(BfglDto bfglDto){
		Map<String,Object> map = new HashMap<>();
		bfglDto=getXtyhList(bfglDto);
		bfglDto=setDate(bfglDto);
		//全部拜访清单是获取统计周期内拜访人信息
		if(("all").equals(bfglDto.getList_flg())) {
			List<BfglDto> bfrlist=bfglService.getBfrByTjrq(bfglDto);
			map.put("bfrlist", bfrlist);
		}
		List<BfglDto> statictislist=bfglService.getStatictisDwgsByBfr(bfglDto);
		map.put("statictislist", statictislist);
		return map;
	}

	/**
	 * 钉钉小程序查询本  周/日/月/全 拜访时长统计图(根据拜访单位区分)
	 * @param bfglDto
	 * @return
	 */
	@RequestMapping("/dingtalk/getStatictisBfscByDwid")
	@ResponseBody
	public Map<String,Object> getStatictisBfscByDwid(BfglDto bfglDto){
		Map<String,Object> map = new HashMap<>();
		bfglDto=getXtyhList(bfglDto);
		bfglDto=setDate(bfglDto);
		//全部拜访清单是获取统计周期内拜访人信息
		if(("all").equals(bfglDto.getList_flg())) {
			List<BfglDto> bfrlist=bfglService.getBfrByTjrq(bfglDto);
			map.put("bfrlist", bfrlist);
		}
		List<BfglDto> statictislist=bfglService.getStatictisBfscByDwid(bfglDto);
		map.put("statictislist", statictislist);
		return map;
	}

	/**
	 * 钉钉小程序查询本  周/日/月/全 拜访时长统计图(根据拜访单位区分)
	 * @param bfglDto
	 * @return
	 */
	@RequestMapping("/dingtalk/getStatictisBfscByBfr")
	@ResponseBody
	public Map<String,Object> getStatictisBfscByBfr(BfglDto bfglDto){
		Map<String,Object> map = new HashMap<>();
		bfglDto=getXtyhList(bfglDto);
		bfglDto=setDate(bfglDto);
		//全部拜访清单是获取统计周期内拜访人信息
		if(("all").equals(bfglDto.getList_flg())) {
			List<BfglDto> bfrlist=bfglService.getBfrByTjrq(bfglDto);
			map.put("bfrlist", bfrlist);
		}
		List<BfglDto> statictislist=bfglService.getStatictisBfscByBfr(bfglDto);
		map.put("statictislist", statictislist);
		return map;
	}

	/**
	 * 获取录入人信息
	 * @param bfglDto
	 * @return
	 */
	public BfglDto getLrryList(BfglDto bfglDto) {
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("ddid", bfglDto.getDdid());
		paramMap.add("wxid", bfglDto.getWxid());
		@SuppressWarnings("unchecked")
		List<String> lrrylist = restTemplate.postForObject(applicationurl+"/ws/getLrryList", paramMap, List.class);
		if(CollectionUtils.isEmpty(lrrylist)) {
			lrrylist= new ArrayList<>();
			lrrylist.add("default");
		}
		bfglDto.setLrrylist(lrrylist);
		return bfglDto;
	}

	/**
	 * 获取录入人的用户信息
	 * @param bfglDto
	 * @return
	 */
	public BfglDto getXtyhList(BfglDto bfglDto) {
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("ddid", bfglDto.getDdid());
		paramMap.add("wxid", bfglDto.getWxid());
		@SuppressWarnings("unchecked")
		Map<String,String> result = restTemplate.postForObject(applicationurl+"/ws/dingtalk/getUser", paramMap, Map.class);
		List<String> lrrylist= new ArrayList<>();
		if(result==null || StringUtil.isBlank(result.get("yhid"))) {
			lrrylist.add("default");
		}else {
			lrrylist.add(result.get("yhid"));
		}
		bfglDto.setLrrylist(lrrylist);
		return bfglDto;
	}

	/**
	 * 根据拜访id获取拜访登记信息
	 * @param bfglDto
	 * @return
	 */
	@RequestMapping("/dingtalk/getVisitRegistration")
	@ResponseBody
	public Map<String,Object> getVisitRegistration(BfglDto bfglDto){
		Map<String,Object> map= new HashMap<>();
		bfglDto=bfglService.getDtoById(bfglDto.getBfid());
		map.put("bfglDto", bfglDto);
		return map;
	}

	/**
	 * 小程序修改保存拜访登记信息
	 * @param bfglDto
	 * @return
	 */
	@RequestMapping("/dingtalk/modSaveVisitRegistration")
	@ResponseBody
	public Map<String,Object> modSaveVisitRegistration(BfglDto bfglDto){
		Map<String,Object> map= new HashMap<>();
		boolean isSuccess=bfglService.update(bfglDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 *小程序删除保存拜访登记信息
	 * @param bfglDto
	 * @return
	 */
	@RequestMapping("/dingtalk/delVisitRegistration")
	@ResponseBody
	public Map<String,Object> delVisitRegistration(BfglDto bfglDto){
		Map<String,Object> map= new HashMap<>();
		boolean isSuccess=bfglService.delete(bfglDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}

	/**
	 * 获取最新的医生数,床位数,分组数,科室合作公司信息
	 * @param bfglDto
	 * @return
	 */
	@RequestMapping("/dingtalk/getNewKsxx")
	@ResponseBody
	public Map<String,Object> getNewKsxx(BfglDto bfglDto){
		Map<String,Object> map= new HashMap<>();
		bfglDto=getLrryList(bfglDto);
		bfglDto =bfglService.getNewKsxx(bfglDto);
		map.put("bfglDto", bfglDto);
		return map;
	}


	/**
	 * 获取送检详细结果列表
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/wechat/getAnalysisList")
	@ResponseBody
	public Map<String,Object> getAnalysisList(SjxxDto sjxxDto) {
		Map<String,Object> map= new HashMap<>();
		//根据微信ID获取录入人员列表
		if(StringUtil.isNotBlank(sjxxDto.getWxid())){
			List<String> lrrys = commonService.getLrryList(sjxxDto.getWxid());
			if(CollectionUtils.isEmpty(lrrys)){
				lrrys = new ArrayList<>();
				lrrys.add(sjxxDto.getWxid());
			}
			sjxxDto.setLrrys(lrrys);
		}
		List<SjxxDto> sjxxDtos = sjxxService.getAnalysisList(sjxxDto);
		map.put("list", sjxxDtos);
		return map;
	}

	/**
	 * 获取检测类型(微信小程序 报告分析)
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/wechat/getJclxList")
	@ResponseBody
	public Map<String, Object> getJclxList(SjxxDto sjxxDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		SjxxjgDto sjxxjgDto = new SjxxjgDto();
		sjxxjgDto.setSjid(sjxxDto.getSjid());
		List<SjxxjgDto> jclxList = sjxxjgService.getJclxInfo(sjxxjgDto);
		map.put("jclxList", jclxList);
		return map;
	}

	/**
	 * 获取详细结果信息(树)
	 * @param sjxxjgDto
	 * @param request
	 * @return
	 */
	@RequestMapping("/wechat/getTreeAnalysis")
	@ResponseBody
	public Map<String,Object> getTreeAnalysis(SjxxjgDto sjxxjgDto, HttpServletRequest request) {
		Map<String,Object> map= new HashMap<>();
		List<SjxxjgDto> sjxxjglist = yhsjxxjgService.getTreeAnalysis(sjxxjgDto);
		map.put("sjxxjglist", sjxxjglist);
		return map;
	}

	/**
	 * 获取详细结果信息
	 * @param sjxxjgDto
	 * @param request
	 * @return
	 */
	@RequestMapping("/wechat/getAnalysis")
	@ResponseBody
	public Map<String,Object> getAnalysis(SjxxjgDto sjxxjgDto, HttpServletRequest request) {
		Map<String,Object> map = new HashMap<>();
		//先从用户送检详细结果表获取，没有从用户结果表获取
		List<SjxxjgDto>	sjxxjglist = yhsjxxjgService.getAnalysis(sjxxjgDto);
		map.put("sjxxjglist", sjxxjglist);
		return map;
	}

	/**
	 * 修改详细结果信息
	 * @param yhsjxxjgDto
	 * @param request
	 * @return
	 */
	@RequestMapping("/wechat/modAnalysis")
	@ResponseBody
	public Map<String,Object> modAnalysis(YhsjxxjgDto yhsjxxjgDto, HttpServletRequest request) {
		Map<String,Object> map = new HashMap<>();
		boolean isSuccess = yhsjxxjgService.modAnalysis(yhsjxxjgDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("sjxxjgDto", yhsjxxjgDto);
		return map;
	}


	/**
	 * 附件下载（用于普通的文件下载使用）
	 * @param fjcfbDto
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/miniprogram/downloadDocFile")
	@ResponseBody
	public byte[] downloadDocFile(FjcfbDto fjcfbDto,HttpServletResponse response,HttpServletRequest request){
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		FjcfbDto t_fjcfbDto = fjcfbService.getDto(fjcfbDto);
		byte[] result = null;
		if(t_fjcfbDto!=null) {
			String wjlj = t_fjcfbDto.getWjlj();
			paramMap.add("wjlj", wjlj);
			paramMap.add("fjid", t_fjcfbDto.getFjid());

			HttpHeaders headers = new HttpHeaders();
			HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(paramMap, headers);
			RestTemplate t_restTemplate = new RestTemplate();

			ResponseEntity<byte[]> t_response = t_restTemplate.exchange(applicationurl + "/ws/file/getPubFileInfo", HttpMethod.POST, httpEntity, byte[].class);
			// 校验文件夹目录是否存在，不存在就创建一个目录
			result = t_response.getBody();
		}
        return result;
	}



	@RequestMapping(value="/dingtalk/getVerifiModDingtalk")
	@ResponseBody
	public Map<String,Object> getVerifiModDingtalk(SjyzDto sjyzDto){
		Map<String,Object> map= new HashMap<>();
		SjyzDto sjyzDto_t=sjyzService.getDto(sjyzDto);
		sjyzDto_t.setAuditType(AuditTypeEnum.AUDIT_VERIFICATION.getCode());
		sjyzDto_t.setYwlx(BusTypeEnum.IMP_VERIFIFCATION.getCode());
		SjxxDto sjxxDto=new SjxxDto();
		sjxxDto.setSjid(sjyzDto_t.getSjid());
		SjxxDto sjxxDto_t=sjxxService.getDto(sjxxDto);
		if("1".equals(sjxxDto_t.getYyxxCskz1())) {
			sjxxDto_t.setHospitalname(sjxxDto_t.getHospitalname()+"-"+sjxxDto_t.getSjdwmc());
		}
		SjyzjgDto sjyzjgDto=new SjyzjgDto();
		sjyzjgDto.setYzid(sjyzDto.getYzid());
		List<SjyzjgDto> yzmxlist=sjyzjgService.getDtoList(sjyzjgDto);
		Map<String, List<JcsjDto>> jcsjlist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.VERIFICATION_TYPE,BasicDataTypeEnum.VERIFICATION_RESULT,BasicDataTypeEnum.DISTINGUISH,BasicDataTypeEnum.CLIENT_NOTICE});
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwid(sjyzDto.getYzid());
		map.put("verificationList", jcsjlist.get(BasicDataTypeEnum.VERIFICATION_TYPE.getCode()));//送检验证类型
		map.put("verificationresultList", jcsjlist.get(BasicDataTypeEnum.VERIFICATION_RESULT.getCode()));//送检验证结果
		map.put("distinguishList", jcsjlist.get(BasicDataTypeEnum.DISTINGUISH.getCode()));//区分
		map.put("clientnoticeList", jcsjlist.get(BasicDataTypeEnum.CLIENT_NOTICE.getCode()));//客户通知
		map.put("sjxxDto", sjxxDto_t);
		map.put("sjyzDto", sjyzDto_t);
		//根据验证id查询附件表信息
		map.put("fjcfbDtos",fjcfbDtos);
		map.put("yzmxlist", yzmxlist);
		return map;
	}

	/**
	 * 	【废弃，暂未使用】
	 * @Description:钉钉群机器人发送群消息，可以@群人员
	 * @Method:sendGroupMessageAt
	 * @param  msgcontent 消息内容
	 * @param  atMobile @的人员List
	*/
	@RequestMapping(value = "/dingtalk/sendGroupMessageAt")
	@ResponseBody
	public Map<String, Object> sendGroupMessageAt( String msgcontent, String atMobile) {
		Map<String, Object> map = new HashMap<>();
		List<String> atMobiles = new ArrayList<>();
		atMobiles.add("15083052617");
		boolean isSuccess =  dingTalkUtil.sendGroupMessageAt(msgcontent,atMobiles);
		map.put("status",isSuccess);
		return map;
	}

	/**
	 * 根据获取使用人员信息及收费权限(微信与钉钉公用，权限信息用于微信公众号)
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/dingtalk/isThirdParty")
	@ResponseBody
	public Map<String,String> isThirdParty(HttpServletRequest request, SjhbxxDto sjhbxxDto){
		Map<String,String> map= new HashMap<>();
		if(StringUtil.isNotBlank(request.getParameter("db"))) {
			sjhbxxDto.setHbmc(request.getParameter("db"));
			sjhbxxDto = sjhbxxService.selectSjhb(sjhbxxDto);
			if(sjhbxxDto != null) {
				map.put("flcskz2", sjhbxxDto.getFlcskz2());
			}
		}
		return map;
	}


}
