package com.matridx.server.detection.molecule.controller;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.BusinessTypeEnum;
import com.matridx.igams.common.enums.ProgramCodeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.igams.common.util.WechatCommonUtils;
import com.matridx.server.detection.molecule.dao.entities.FzjcxxDto;
import com.matridx.server.detection.molecule.dao.entities.HzxxtDto;
import com.matridx.server.detection.molecule.service.svcinterface.IFzjcxxService;
import com.matridx.server.detection.molecule.service.svcinterface.IHzxxtService;
import com.matridx.server.wechat.dao.entities.SjjcxmDto;
import com.matridx.server.wechat.dao.entities.SjxxDto;
import com.matridx.server.wechat.dao.entities.WbcxDto;
import com.matridx.server.wechat.dao.entities.WeChatUserModel;
import com.matridx.server.wechat.service.svcinterface.ISjjcxmService;
import com.matridx.server.wechat.service.svcinterface.ISjxxService;
import com.matridx.server.wechat.service.svcinterface.IWbcxService;
import com.matridx.server.wechat.service.svcinterface.IWeChatService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ws")
public class DetectionWsController extends BaseController {

	@Autowired
	IFzjcxxService fzjcxxService;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	IHzxxtService hzxxService;
	@Autowired
	IXtszService xtszService;
	@Autowired
	ISjxxService sjxxService;
	@Autowired
	WechatCommonUtils wechatCommonUtils;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	ISjjcxmService sjjcxmService;
	private Logger log = LoggerFactory.getLogger(DetectionController.class);

	/**
	 * 报告查询
	 * @return
	 */
	@RequestMapping("/detection/outcome")
	@ResponseBody
	public ModelAndView outcome(FzjcxxDto fzjcxxDto) {
		ModelAndView mav = new ModelAndView("wechat/outcome");
		FzjcxxDto fzjcxxDtoInfo  = null;
		try {
			fzjcxxDtoInfo = fzjcxxService.getSampleAcceptInfo(fzjcxxDto);
		} catch (BusinessException e) {
			mav.addObject("message", e);
		}
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("fzjcxxDtoInfo", fzjcxxDtoInfo);
		return mav;
	}

	/**
	 * 新冠病毒核酸检测预约
	 * @param wxid
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/detectionAppointment")
	public ModelAndView detectionAppointment(String wxid, HttpServletRequest request){
		wxid="matridx";
		ModelAndView mav = new ModelAndView("wechat/detectionAppointmentOut");
		JcsjDto jclx = new JcsjDto();
		List<JcsjDto> jclxs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
		for (JcsjDto lsjclx : jclxs) {
			if ("TYPE_COVID".equals(lsjclx.getCsdm())){
				jclx = lsjclx;
				break;
			}
		}
		List<JcsjDto> jcxmlist = new ArrayList<>();
		List<JcsjDto> jcxms = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_ITEM.getCode());
		for (JcsjDto jcxm : jcxms) {
			if (jcxm.getFcsid().equals(jclx.getCsid()) && "0".equals(jcxm.getScbj())){
				if ("1".equals(jcxm.getSfmr())){
					jcxm.setChecked("1");
				}
				jcxmlist.add(jcxm);
			}
		}
		mav.addObject("jcxmlist",jcxmlist);
		HzxxtDto hzxxDto=new HzxxtDto();
		List<JcsjDto> collect_samples = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.COLLECT_SAMPLES.getCode());
		for (JcsjDto collect_sample : collect_samples) {
			if ("1".equals(collect_sample.getSfmr())){
				hzxxDto.setCyd(collect_sample.getCsid());
			}
		}
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String format = simpleDateFormat.format(date);
		hzxxDto.setYyjcrq(format);
		mav.addObject("wxid", wxid);
		mav.addObject("title", "新冠病毒核酸检测预约");
		mav.addObject("hzxxDto", hzxxDto);
		mav.addObject("request",request);
		mav.addObject("ywlx", BusinessTypeEnum.XG.getCode());
		XtszDto xtszDto = xtszService.selectById("xg_zfje");
		String fkje ="";
		if (null!=xtszDto){
			fkje  =  xtszDto.getSzz();//获取token 访问令牌
		}
		mav.addObject("fkje", fkje);
		return mav;
	}

	/**
	 * 新冠病毒核酸检测预约信息保存
	 * @param hzxxDto
	 * @return
	 */
	@RequestMapping(value="/detectionAppointmentSave")
	@ResponseBody
	public Map<String, Object> detectionAppointmentSave(HzxxtDto hzxxDto){
		return hzxxService.detectionAppointmentAdd(hzxxDto);
	}

	@RequestMapping(value="/chooseReportView")
	public ModelAndView chooseReportView(HttpServletRequest request){
		ModelAndView mav = new ModelAndView("wechat/chooseReportView_Index");
		String ybbh = request.getParameter("ybbh");
		mav.addObject("ybbh", ybbh);
		return mav;
	}

	@RequestMapping(value="/viewReport")
	public ModelAndView viewReport(HttpServletRequest request){
		ModelAndView mav = new ModelAndView("wechat/viewReportPage");
		String flag = request.getParameter("flag");
		String jclxdm = request.getParameter("jclxdm");
		List<FjcfbDto> fjcfbDtos =new ArrayList<>();
		if("0".equals(flag)){
			SjxxDto sjxxDto=new SjxxDto();
			sjxxDto.setYbbh(request.getParameter("ybbh"));
			SjxxDto dto = sjxxService.getDto(sjxxDto);
			if(dto!=null){
				SjjcxmDto sjjcxmDto=new SjjcxmDto();
				sjjcxmDto.setSjid(dto.getSjid());
				List<SjjcxmDto> dtoList = sjjcxmService.getDtoList(sjjcxmDto);
				FjcfbDto fjcfbDto = new FjcfbDto();
				fjcfbDto.setYwid(dto.getSjid());
				fjcfbDto.setYwlxs(wechatCommonUtils.getInspectionPdfYwlxs());
				fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
				if(dtoList!=null&&!dtoList.isEmpty()&&!fjcfbDtos.isEmpty()){
					for(FjcfbDto fjcfbDto_t:fjcfbDtos){
						for(SjjcxmDto sjjcxmDto_t:dtoList){
							if(fjcfbDto_t.getYwlx().equals(sjjcxmDto_t.getCskz3()+"_"+sjjcxmDto_t.getCskz1())){
								fjcfbDto_t.setWjm(sjjcxmDto_t.getCsmc());
								break;
							}
						}
					}
				}
			}
		}else if("1".equals(flag)){
			FzjcxxDto fzjcxxDto=new FzjcxxDto();
			fzjcxxDto.setYbbh(request.getParameter("ybbh"));
			List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
			if(jclxList!=null&&jclxList.size()>0){
				for(JcsjDto jcsjDto : jclxList){
					if(jclxdm.equals(jcsjDto.getCsdm()))
						fzjcxxDto.setJclx(jcsjDto.getCsid());
				}
			}
			FzjcxxDto dto = fzjcxxService.getDto(fzjcxxDto);
			if(dto!=null){
				FjcfbDto fjcfbDto = new FjcfbDto();
				fjcfbDto.setYwid(dto.getFzjcid());
				fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT_FLU.getCode());
				fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
				if(!fjcfbDtos.isEmpty()){
					String xmmc="";
					List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_ITEM.getCode());
					if(jcsjDtos!=null&&!jcsjDtos.isEmpty()){
						for(JcsjDto jcsjDto:jcsjDtos){
							if("FLU".equals(jcsjDto.getCsdm())){
								xmmc=jcsjDto.getCsmc();
								break;
							}
						}
					}
					for(FjcfbDto fjcfbDto_t:fjcfbDtos){
						fjcfbDto_t.setWjm(xmmc);
					}
				}
			}
		}
		mav.addObject("fjcfbDtos",fjcfbDtos);
		return mav;
	}
}
