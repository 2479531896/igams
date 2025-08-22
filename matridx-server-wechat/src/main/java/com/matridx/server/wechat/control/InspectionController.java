package com.matridx.server.wechat.control;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.GrlbzdszDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.LbzdszDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IGrlbzdszService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.ILbzdszService;
import com.matridx.igams.common.util.WechatCommonUtils;
import com.matridx.server.wechat.dao.entities.FjsqDto;
import com.matridx.server.wechat.dao.entities.SjbgsmDto;
import com.matridx.server.wechat.dao.entities.SjdwxxDto;
import com.matridx.server.wechat.dao.entities.SjnyxDto;
import com.matridx.server.wechat.dao.entities.SjwzxxDto;
import com.matridx.server.wechat.dao.entities.SjxxDto;
import com.matridx.server.wechat.dao.entities.SjxxjgDto;
import com.matridx.server.wechat.dao.entities.SjzmjgDto;
import com.matridx.server.wechat.service.svcinterface.IFjsqService;
import com.matridx.server.wechat.service.svcinterface.IHbqxService;
import com.matridx.server.wechat.service.svcinterface.ISjbgsmService;
import com.matridx.server.wechat.service.svcinterface.ISjhbxxService;
import com.matridx.server.wechat.service.svcinterface.ISjnyxService;
import com.matridx.server.wechat.service.svcinterface.ISjxxService;
import com.matridx.server.wechat.service.svcinterface.ISjxxjgService;
import com.matridx.server.wechat.service.svcinterface.ISjzmjgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller              
@RequestMapping("/inspection")
//@RequestMapping("/inspectMatridx")
public class InspectionController extends BaseController{

	@Autowired
	WechatCommonUtils wechatCommonUtils;
	@Autowired
	private IGrlbzdszService grlbzdszService; 
	@Autowired
	private ILbzdszService lbzdszService;
	@Autowired
	private ISjxxService sjxxService;
	@Autowired
	private IHbqxService hbqxService;
	@Autowired
	private ISjhbxxService sjhbxxService;
	@Autowired
	private IJcsjService jcsjService;
	@Autowired
	private ISjnyxService sjnyxService;
	@Autowired
	private ISjxxjgService sjxxjgService;
	@Autowired
	private IFjcfbService fjcfbService;
	@Autowired
	private ISjzmjgService sjzmjgService;
	@Autowired
	private ISjbgsmService sjbgsmService;
	@Autowired
	private IFjsqService fjsqService;

	@Value("${matridx.wechat.bioaudurl:}")
	private String bioaudurl;

	private Logger log = LoggerFactory.getLogger(InspectionController.class);
	
	@RequestMapping("/inspection/pageListInspection")
	public ModelAndView pageListInspection() {
		ModelAndView mav=new ModelAndView("wechat/sjxx/sjxx_List");
		User user = getLoginInfo();
		List<SjdwxxDto> sjdwxxlist=sjxxService.getSjdwxx();
		mav.addObject("sjdwxxlist", sjdwxxlist);
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.WECGATSAMPLE_TYPE,BasicDataTypeEnum.FIRST_SJXXKZ,
				BasicDataTypeEnum.SECOND_SJXXKZ,BasicDataTypeEnum.THIRD_SJXXKZ,BasicDataTypeEnum.FOURTH_SJXXKZ,BasicDataTypeEnum.DETECT_TYPE,BasicDataTypeEnum.SD_TYPE,
				BasicDataTypeEnum.DETECTION_UNIT,BasicDataTypeEnum.STAMP_TYPE,BasicDataTypeEnum.CLASSIFY});
		mav.addObject("samplelist", jclist.get(BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));//标本类型
		mav.addObject("detectlist", jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode()));//检测项目
    	mav.addObject("cskz1List", jclist.get(BasicDataTypeEnum.FIRST_SJXXKZ.getCode()));
    	mav.addObject("sjhbflList", jclist.get(BasicDataTypeEnum.CLASSIFY.getCode()));//合作伙伴分类
    	mav.addObject("cskz2List", jclist.get(BasicDataTypeEnum.SECOND_SJXXKZ.getCode()));
    	mav.addObject("cskz3List", jclist.get(BasicDataTypeEnum.THIRD_SJXXKZ.getCode()));
    	mav.addObject("cskz4List", jclist.get(BasicDataTypeEnum.FOURTH_SJXXKZ.getCode()));
    	mav.addObject("expressage", jclist.get(BasicDataTypeEnum.SD_TYPE.getCode()));//快递类型
    	mav.addObject("stamplist", jclist.get(BasicDataTypeEnum.STAMP_TYPE.getCode()));//盖章类型
    	mav.addObject("detectionList", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
    	GrlbzdszDto grlbzdszDto = new GrlbzdszDto();
		grlbzdszDto.setYhid(getLoginInfo().getYhid());
		grlbzdszDto.setYwid("INSPECT");
		LbzdszDto lbzdszDto = new LbzdszDto();
		lbzdszDto.setYwid("INSPECT");
		lbzdszDto.setYhid(user.getYhid());
		lbzdszDto.setJsid(user.getDqjs());
		List<LbzdszDto> choseList = grlbzdszService.getChoseList(grlbzdszDto);
		List<LbzdszDto> waitList = lbzdszService.getWaitList(lbzdszDto);
		mav.addObject("choseList", choseList);
		mav.addObject("waitList", waitList);
		return mav;
	}
	
	@RequestMapping("/inspection/pageGetListInspection")
	@ResponseBody
	public Map<String, Object> SjxxList(SjxxDto sjxxDto){
		User user=getLoginInfo();
		List<SjxxDto> sjxxlist;
		List<Map<String,String>> jcdwList=sjxxService.getJsjcdwByjsid(user.getDqjs());
		if(jcdwList!=null&&jcdwList.size() > 0){
			if("1".equals(jcdwList.get(0).get("dwxdbj"))) {
				//判断是否显示个人清单 ？？清单是个什么东西
				if("1".equals(sjxxDto.getSingle_flag())) {
					List<String> userids= new ArrayList<>();
					if(user.getYhid()!=null) {
						userids.add(user.getYhid());
					}
					if(user.getDdid()!=null) {
						userids.add(user.getDdid());
					}
					if(user.getWechatid()!=null) {
						userids.add(user.getWechatid());
					}
					if(userids.size()>0) {
						sjxxDto.setUserids(userids);
					}

					//判断伙伴权限
					List<String> hbqxList = hbqxService.getHbidByYhid(user.getYhid());
					if(hbqxList!=null && hbqxList.size()>0) {
						List<String> hbmcList=sjhbxxService.getHbmcByHbid(hbqxList);
						if(hbmcList!=null  && hbmcList.size()>0) {
							sjxxDto.setSjhbs(hbmcList);
						}
					}
				}
				List<String> strList= new ArrayList<>();
				for (int i = 0; i < jcdwList.size(); i++){
					if(jcdwList.get(i).get("jcdw")!=null) {
						strList.add(jcdwList.get(i).get("jcdw"));
					}
				}
				if(strList!=null && strList.size()>0) {
					sjxxDto.setJcdwxz(strList);
					sjxxlist=sjxxService.getPagedInspection(sjxxDto);
				}else {
					sjxxlist= new ArrayList<>();
				}
			}else {
				sjxxlist=sjxxService.getPagedInspection(sjxxDto);
			}
		}else {
			sjxxlist=sjxxService.getPagedInspection(sjxxDto);
		}

		
		Map<String, Object> map= new HashMap<>();
		map.put("total",sjxxDto.getTotalNumber());
		map.put("rows", sjxxlist);
		 
		//需要筛选钉钉字段的，请调用该方法
//		screenClassColumns(request,map);
		return map;
	}

	/**
	 * 查看送检信息
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/inspection/viewSjxx")
	public ModelAndView viewSjxx(SjxxDto sjxxDto) {
		ModelAndView mav=new ModelAndView("wechat/statistics/sjxx_ListView");
		List<SjnyxDto> sjnyx=sjnyxService.getNyxBySjid(sjxxDto);
		SjxxDto sjxxDto2=sjxxService.getDto(sjxxDto);
		List<SjwzxxDto> sjwzxx=sjxxService.selectWzxxBySjid(sjxxDto2);
		if(sjwzxx!=null && sjwzxx.size()>0) {
			String xpxx=sjwzxx.get(0).getXpxx();//由于一个标本中的物种芯片信息相同，取其一
			mav.addObject("Xpxx", xpxx);
		}

//		if(("Z6").equals(sjxxDto2.getCskz1()) || ("Z12").equals(sjxxDto2.getCskz1()) || ("Z18").equals(sjxxDto2.getCskz1()) || ("F").equals(sjxxDto2.getCskz1())) {
		if( ("Z6").equals(sjxxDto2.getCskz1()) ||("Z").equals(sjxxDto2.getCskz1()) || ("F").equals(sjxxDto2.getCskz1())) {
			List<SjzmjgDto> sjzmList;
			SjzmjgDto sjzmjgDto=new SjzmjgDto();
			sjzmjgDto.setSjid(sjxxDto.getSjid());
			sjzmList=sjzmjgService.getDtoList(sjzmjgDto);
			mav.addObject("sjzmList", sjzmList);
			mav.addObject("KZCS",sjxxDto2.getCskz1());
			log.error(sjxxDto2.getCskz1());
			log.error(JSON.toJSONString(sjzmList));
		}
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwid(sjxxDto.getSjid());

		fjcfbDto.setYwlxs(wechatCommonUtils.getInspectionPdfYwlxs());

		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("fjcfbDtos",fjcfbDtos);

		fjcfbDto.setYwlxs(wechatCommonUtils.getInspectionWordYwlxs());
		List<FjcfbDto> zhwj=fjcfbService.selectzhpdf(fjcfbDto);
		fjcfbDto.setYwlxs(null);
		fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
		List<FjcfbDto> t_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		//收样附件
		fjcfbDto.setYwlxs(null);
		fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTION_CONFIRM.getCode());
		List<FjcfbDto> b_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		SjbgsmDto sjbgsmdto=new SjbgsmDto();
		sjbgsmdto.setSjid(sjxxDto.getSjid());
		List<SjbgsmDto> sjbgsmxx=sjbgsmService.selectSjbgBySjid(sjbgsmdto);


		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.DETECT_TYPE});
		List<JcsjDto> jcxmlist=jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode());
		List<JcsjDto> t_jcxmlist= new ArrayList<>();//用于结果页
		List<JcsjDto> c_jcxmlist= new ArrayList<>();//用于详细页

		if(jcxmlist!=null && jcxmlist.size()>0) {
			for(int i=0;i<jcxmlist.size();i++) {
				boolean wz_sftj=false;//判断对应该检测项目的物种信息是否存在，若存在一个则添加该项目
				if(sjwzxx!=null && sjwzxx.size()>0) {
					for(int j=0;j<sjwzxx.size();j++) {
						if(jcxmlist.get(i).getCsid().equals(sjwzxx.get(j).getJcxmid())) {
							wz_sftj=true;
							break;
						}
					}
				}
				if(wz_sftj) {
                    c_jcxmlist.add(jcxmlist.get(i));
                }

				boolean sftj=false;//判断对应该检测项目的报告说明和附件是否存在，若其中一个存在添加该项目
				//查看页面自勉报告不显示问题 // 判断WORD附件 不判断PDF附件  auther:zhanghan  2020/12/22
//				if(("Z6").equals(sjxxDto2.getCskz1())|| ("Z8").equals(sjxxDto2.getCskz1()) || ("Z12").equals(sjxxDto2.getCskz1()) || ("Z18").equals(sjxxDto2.getCskz1()) || ("F").equals(sjxxDto2.getCskz1())|| ("T3").equals(sjxxDto2.getCskz1()) || ("T6").equals(sjxxDto2.getCskz1()) || ("K").equals(sjxxDto2.getCskz1())) {
				if( ("Z6").equals(sjxxDto2.getCskz1())||("Z").equals(sjxxDto2.getCskz1()) || ("F").equals(sjxxDto2.getCskz1())|| ("T3").equals(sjxxDto2.getCskz1()) || ("T6").equals(sjxxDto2.getCskz1()) || ("K").equals(sjxxDto2.getCskz1())) {
					if(zhwj!=null && zhwj.size()>0) {
						for (int j = 0; j < zhwj.size(); j++) {
							if(zhwj.get(j).getYwlx().equals((jcxmlist.get(i).getCskz3()+"_"+jcxmlist.get(i).getCskz1()+"_WORD"))) {
								sftj=true;
								break;
							}
						}
					}
				}
				if(!sftj&&sjbgsmxx!=null && sjbgsmxx.size()>0) {
					for(int j=0;j<sjbgsmxx.size();j++) {
						if(jcxmlist.get(i).getCsid().equals(sjbgsmxx.get(j).getJcxmid())) {
							sftj=true;
							break;
						}
					}
				}else if(!sftj&&t_fjcfbDtos!=null && t_fjcfbDtos.size()>0) {
					for(int j=0;j<t_fjcfbDtos.size();j++) {
						if(t_fjcfbDtos.get(j).getYwlx().equals((jcxmlist.get(i).getCskz3()+jcxmlist.get(i).getCskz1()))) {
							sftj=true;
							break;
						}
					}
				}else if(!sftj&&fjcfbDtos!=null && fjcfbDtos.size()>0) {
					for(int j=0;j<fjcfbDtos.size();j++) {
						if(fjcfbDtos.get(j).getYwlx().equals((jcxmlist.get(i).getCskz3()+"_"+jcxmlist.get(i).getCskz1()))) {
							sftj=true;
							break;
						}
					}
				}
				if(sftj) {
                    t_jcxmlist.add(jcxmlist.get(i));
                }
			}
		}

		SjxxjgDto sjxxjgDto=new SjxxjgDto();
		sjxxjgDto.setSjid(sjxxDto.getSjid());
		List<SjxxjgDto> getJclxCount=sjxxjgService.getJclxCount(sjxxjgDto);
		//查看当前复检申请信息
		FjsqDto fjsqDto=new FjsqDto();
		String[] zts= {StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_PASS.getCode()};
		fjsqDto.setZts(zts);
		fjsqDto.setSjid(sjxxDto2.getSjid());
		List<FjsqDto> fjsqList=fjsqService.getListBySjid(fjsqDto);
		mav.addObject("fjsqList", fjsqList);
		mav.addObject("SjxxjgList", getJclxCount);
		mav.addObject("SjnyxDto", sjnyx);
		mav.addObject("zhwjpdf", zhwj);
		mav.addObject("sjbgsmList",sjbgsmxx);
		mav.addObject("t_fjcfbDtos",t_fjcfbDtos);
		mav.addObject("sjxxDto", sjxxDto2);
		mav.addObject("Sjwzxx", sjwzxx);
		mav.addObject("jcxmlist",t_jcxmlist);
		mav.addObject("wzjcxmlist", c_jcxmlist);
		mav.addObject("b_fjcfbDtos", b_fjcfbDtos);
		if(bioaudurl.endsWith("/"))
			bioaudurl = bioaudurl.substring(0, bioaudurl.length() - 1);
		mav.addObject("bioaudurl",bioaudurl);
		return mav;
	}
}
