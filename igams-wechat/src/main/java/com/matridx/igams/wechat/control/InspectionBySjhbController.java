package com.matridx.igams.wechat.control;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.GrlbzdszDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.LbzdszDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IGrlbzdszService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.ILbzdszService;
import com.matridx.igams.common.util.WechatCommonUtils;
import com.matridx.igams.wechat.dao.entities.SjbgsmDto;
import com.matridx.igams.wechat.dao.entities.SjdwxxDto;
import com.matridx.igams.wechat.dao.entities.SjnyxDto;
import com.matridx.igams.wechat.dao.entities.SjsyglDto;
import com.matridx.igams.wechat.dao.entities.SjwzxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxjgDto;
import com.matridx.igams.wechat.dao.entities.SjzmjgDto;
import com.matridx.igams.wechat.service.svcinterface.IHbqxService;
import com.matridx.igams.wechat.service.svcinterface.ISjbgsmService;
import com.matridx.igams.wechat.service.svcinterface.ISjhbxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjnyxService;
import com.matridx.igams.wechat.service.svcinterface.ISjsyglService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxjgService;
import com.matridx.igams.wechat.service.svcinterface.ISjzmjgService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/inspection")
public class InspectionBySjhbController extends BaseController{
	@Autowired
	private ISjxxService sjxxservice;
	@Autowired
	private IJcsjService jcsjService;
	@Autowired
	private IGrlbzdszService grlbzdszService;
	@Autowired
	private IHbqxService hbqxService;
	@Autowired
	private ILbzdszService lbzdszService;
	@Autowired
	private ISjhbxxService sjhbxxservice;
	@Autowired
	private ISjnyxService sjnyxService; 
	@Autowired
	private IFjcfbService fjcfbService;
	@Autowired
	private ISjbgsmService sjbgsmservice;
	@Autowired
	private ISjzmjgService sjzmjgService;
	@Autowired
	private ISjxxjgService sjxxjgService;
	@Autowired
	WechatCommonUtils wechatCommonUtils;
	@Autowired
	private ISjsyglService sjsyglService;

	@Value("${matridx.wechat.bioaudurl:}")
	private String bioaudurl;
	
	/**
	 * 合作伙伴查看列表
	 * @return
	 */
	@RequestMapping("/inspection/pageListSjhb")
	public ModelAndView pageListSjhb(){
		ModelAndView mav=new ModelAndView("wechat/list_sjhb/sjxx_List");
		User user=getLoginInfo();
		List<SjdwxxDto> sjdwxxlist=sjxxservice.getSjdw();
		mav.addObject("sjdwxxlist", sjdwxxlist);
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.WECGATSAMPLE_TYPE,BasicDataTypeEnum.FIRST_SJXXKZ,BasicDataTypeEnum.SECOND_SJXXKZ,BasicDataTypeEnum.THIRD_SJXXKZ,BasicDataTypeEnum.FOURTH_SJXXKZ,BasicDataTypeEnum.DETECT_TYPE,BasicDataTypeEnum.SD_TYPE,BasicDataTypeEnum.DETECTION_UNIT,BasicDataTypeEnum.CLASSIFY,BasicDataTypeEnum.SUBCLASSIFICATION});
    	mav.addObject("samplelist", jclist.get(BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));//标本类型
    	mav.addObject("detectlist", jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode()));//检测项目
    	mav.addObject("cskz1List", jclist.get(BasicDataTypeEnum.FIRST_SJXXKZ.getCode()));
    	mav.addObject("cskz2List", jclist.get(BasicDataTypeEnum.SECOND_SJXXKZ.getCode()));
    	mav.addObject("cskz3List", jclist.get(BasicDataTypeEnum.THIRD_SJXXKZ.getCode()));
    	mav.addObject("cskz4List", jclist.get(BasicDataTypeEnum.FOURTH_SJXXKZ.getCode()));
    	mav.addObject("expressage", jclist.get(BasicDataTypeEnum.SD_TYPE.getCode()));//快递类型
    	mav.addObject("detectionList", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
		mav.addObject("classifylist",jclist.get(BasicDataTypeEnum.CLASSIFY.getCode()));//合作伙伴分类
		mav.addObject("subclassificationlist",jclist.get(BasicDataTypeEnum.SUBCLASSIFICATION.getCode()));//合作伙伴子分类
    	GrlbzdszDto grlbzdszDto = new GrlbzdszDto();
    	grlbzdszDto.setYhid(getLoginInfo().getYhid());
    	grlbzdszDto.setYwid("PARTNER");
    	LbzdszDto lbzdszDto = new LbzdszDto();
    	lbzdszDto.setYwid("PARTNER");
    	lbzdszDto.setYhid(getLoginInfo().getYhid());
    	lbzdszDto.setJsid(user.getDqjs());
		List<LbzdszDto> choseList = grlbzdszService.getChoseList(grlbzdszDto);
		List<LbzdszDto> waitList = lbzdszService.getWaitList(lbzdszDto);
		mav.addObject("choseList", choseList);
		mav.addObject("waitList", waitList);
		//用waitList组装需要显示的列表字段
		String xszdlist = "";
		for (LbzdszDto lbzdszdto : choseList) {
			xszdlist = xszdlist+","+lbzdszdto.getXszd();
		}
		for (LbzdszDto lbzdszdto : waitList) {
			xszdlist = xszdlist+","+lbzdszdto.getXszd();
		}
		String limitColumns = "";
		if (StringUtil.isNotBlank(xszdlist)){
			limitColumns = "{'sjxxDto':'"+xszdlist.substring(1)+"'}";
		}
		mav.addObject("limitColumns",limitColumns);
		return mav;
	}
	
	/**
	 * 合作伙伴查看列表
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/inspection/pageGetListSjhb")
	@ResponseBody
	public Map<String, Object> pageGetListSjhb(SjxxDto sjxxDto, HttpServletRequest request){
		Map<String, Object> map= new HashMap<>();
		User user=getLoginInfo();
		List<String> hbqxList=hbqxService.getHbidByYhid(user.getYhid());
		if(hbqxList!=null && hbqxList.size()>0) {
			List<String> hbmcList=sjhbxxservice.getHbmcByHbid(hbqxList);
			if(hbmcList!=null  && hbmcList.size()>0) {
				sjxxDto.setDbs(hbmcList);
				List<SjxxDto> sjxxList;
				//根据角色查询到角色检测单位信息
				List<Map<String,String>> jcdwList=sjxxservice.getJsjcdwByjsid(user.getDqjs());
				if(jcdwList!=null && jcdwList.size() > 0) {
					//判断检测单位是否为1（单位限制）
					if("1".equals(jcdwList.get(0).get("dwxdbj"))) {
						//取出检测单位为一个List
						List<String> strList= new ArrayList<>();
						for (int i = 0; i < jcdwList.size(); i++){
							if(jcdwList.get(i).get("jcdw")!=null) {
								strList.add(jcdwList.get(i).get("jcdw"));
							}
						}
						//如果检测单位不为空，进行查询。
						if(strList!=null && strList.size()>0) {
							sjxxDto.setJcdwxz(strList);
							sjxxList=sjxxservice.getPageDtoBySjhb(sjxxDto);
						}else {
							//如果检测单位为空，直接返回空。没有查看权限
							sjxxList= new ArrayList<>();
						}
					}else {
						//如果没进行单位限值，不进行处理
						sjxxList=sjxxservice.getPageDtoBySjhb(sjxxDto);
					}
				}else {
					//如果没进行单位限值，不进行处理
					sjxxList=sjxxservice.getPageDtoBySjhb(sjxxDto);
				}
				map.put("total", sjxxDto.getTotalNumber());
				map.put("rows", sjxxList);
			}
		}
		//需要筛选钉钉字段的，请调用该方法
		screenClassColumns(request,map);
		return map;
	}
	
	/**
	* 查看送检信息
	* @param sjxxDto
	* @return
	*/
	@RequestMapping(value="/sjhbview/viewSjxxbyDB")
	public ModelAndView viewSjxx(SjxxDto sjxxDto) {
		ModelAndView mav=new ModelAndView("wechat/list_sjhb/sjxx_ListView");
		List<SjnyxDto> sjnyx=sjnyxService.getNyxBySjid(sjxxDto);
		SjxxDto sjxxDto2=sjxxservice.getDto(sjxxDto);
		if(sjxxDto2!=null && StringUtil.isNotBlank(sjxxDto2.getYyxxCskz1()) && "1".equals(sjxxDto2.getYyxxCskz1())) {
			sjxxDto2.setHospitalname(sjxxDto2.getHospitalname()+"-"+sjxxDto2.getSjdwmc());
		}
		List<SjwzxxDto> sjwzxx=sjxxservice.selectWzxxBySjid(sjxxDto2);
		if(sjwzxx!=null && sjwzxx.size()>0) {
			String xpxx=sjwzxx.get(0).getXpxx();//由于一个标本中的物种芯片信息相同，取其一
			mav.addObject("Xpxx", xpxx);
		}

//        if(("Z6").equals(sjxxDto2.getCskz1()) || ("Z12").equals(sjxxDto2.getCskz1()) || ("Z18").equals(sjxxDto2.getCskz1()) || ("F").equals(sjxxDto2.getCskz1())) {
        if(("Z6").equals(sjxxDto2.getCskz1()) || ("Z").equals(sjxxDto2.getCskz1()) || ("F").equals(sjxxDto2.getCskz1())) {
        	SjzmjgDto sjzmjgDto=new SjzmjgDto();
        	sjzmjgDto.setSjid(sjxxDto.getSjid());
			List<SjzmjgDto> sjzmList=sjzmjgService.getDtoList(sjzmjgDto);
        	mav.addObject("sjzmList", sjzmList);
        	mav.addObject("KZCS",sjxxDto2.getCskz1());
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
		SjbgsmDto sjbgsmdto=new SjbgsmDto();
		sjbgsmdto.setSjid(sjxxDto.getSjid());
		List<SjbgsmDto> sjbgsmxx=sjbgsmservice.selectSjbgBySjid(sjbgsmdto);
		
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.DETECT_TYPE});
		List<JcsjDto> jcxmlist=jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode());
		List<JcsjDto> t_jcxmlist= new ArrayList<>();//用于结果页
		List<JcsjDto> c_jcxmlist= new ArrayList<>();//用于详细页
		
		if(jcxmlist!=null && jcxmlist.size()>0) {
			for(int i=0;i<jcxmlist.size();i++) {
				
				boolean wz_sftj=false;//判断对应该检测项目的物种信息是否存在，若存在一个则添加该项目
				if(sjwzxx!=null && sjwzxx.size()>0) {
					for(int j=0;j<sjwzxx.size();j++) {
						if(sjwzxx.get(j).getJcxmid().equals(jcxmlist.get(i).getCsid())) {
							wz_sftj=true;
							break;
						}
					}
				}
				if(wz_sftj) 
					c_jcxmlist.add(jcxmlist.get(i));
				
				boolean sftj=false;//判断对应该检测项目的报告说明和附件是否存在，若其中一个存在添加该项目
				if(sjbgsmxx!=null && sjbgsmxx.size()>0) {
					for(int j=0;j<sjbgsmxx.size();j++) {
						if(sjbgsmxx.get(j).getJcxmid().equals(jcxmlist.get(i).getCsid())) {
							sftj=true;
							break;
						}
					}
				}
				if(t_fjcfbDtos!=null && t_fjcfbDtos.size()>0) {
					String cskz3 = jcxmlist.get(i).getCskz3();
					String cskz1 = jcxmlist.get(i).getCskz1();
					for(int j=0;j<t_fjcfbDtos.size();j++) {
						if(t_fjcfbDtos.get(j).getYwlx().equals((cskz3==null?"":cskz3.replace("_ONCO", "")+cskz1==null?"":cskz1))) {
							sftj=true;
							break;
						}
					}
				}
				if(fjcfbDtos!=null && fjcfbDtos.size()>0) {
					String cskz3 = jcxmlist.get(i).getCskz3();
					String cskz1 = jcxmlist.get(i).getCskz1();
					for(int j=0;j<fjcfbDtos.size();j++) {
						if(fjcfbDtos.get(j).getYwlx().equals((cskz3==null?"":cskz3+"_"+cskz1==null?"":cskz1))) {
							sftj=true;
							break;
						}
					}
				}
				if(sftj)
					t_jcxmlist.add(jcxmlist.get(i));
			}
		}
		
		SjxxjgDto sjxxjgDto=new SjxxjgDto();
		sjxxjgDto.setSjid(sjxxDto.getSjid());
		List<SjxxjgDto> getJclxCount=sjxxjgService.getJclxCount(sjxxjgDto);
		List<SjsyglDto> sjsyglDtos = sjsyglService.getViewDetectData(sjxxDto.getSjid());
		if(sjsyglDtos!=null&&sjsyglDtos.size()>0){
			for(SjsyglDto dto:sjsyglDtos){
				if(StringUtil.isNotBlank(dto.getSjsj())){
					sjxxDto2.setSjsj(dto.getSjsj());
					break;
				}
			}
		}
		mav.addObject("sjsyglDtos", sjsyglDtos);
        mav.addObject("SjxxjgList", getJclxCount);
		mav.addObject("SjnyxDto", sjnyx);
		mav.addObject("zhwjpdf", zhwj);
		mav.addObject("sjbgsmList",sjbgsmxx);
		mav.addObject("t_fjcfbDtos",t_fjcfbDtos);
		mav.addObject("sjxxDto", sjxxDto2);	
		mav.addObject("Sjwzxx", sjwzxx);
        mav.addObject("jcxmlist",t_jcxmlist);
        mav.addObject("wzjcxmlist", c_jcxmlist);
		if(bioaudurl.endsWith("/"))
			bioaudurl = bioaudurl.substring(0, bioaudurl.length() - 1);
		mav.addObject("bioaudurl",bioaudurl);

		return mav;
	}
	
	/**
	 * 选择下载报告压缩包条件
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value = "/sjhbview/reportdownloadZip")
	public ModelAndView reportdownloadZip(SjxxDto sjxxDto, HttpServletRequest request){
		ModelAndView mav=new ModelAndView("wechat/sjxx/sjxx_reportDownload");
		mav.addObject("flg", "1");
		return mav;
	}
}
