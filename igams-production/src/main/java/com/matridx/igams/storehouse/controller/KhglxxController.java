package com.matridx.igams.storehouse.controller;

import com.matridx.igams.common.cloud.controller.BaseBasicController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.production.dao.matridxsql.CustomerDao;
import com.matridx.igams.storehouse.dao.entities.KhglDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.dao.entities.CustomerDto;
import com.matridx.igams.production.service.svcinterface.IRdRecordService;
import com.matridx.igams.storehouse.service.svcinterface.IKhglxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/storehouse")
public class KhglxxController extends BaseBasicController {

	@Autowired
	IKhglxxService IKhglxxService;

	@Autowired
	IXxglService xxglService;

	@Autowired
	IJcsjService jcsjService;

	@Autowired
	IRdRecordService iRdRecordService;

	@Autowired
	RedisUtil redisUtil;

	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;

	@Autowired
	CustomerDao customerDao;
	@Autowired
	IFjcfbService fjcfbService;
	@Override
	public String getPrefix(){
		return urlPrefix;
	}



	/**
	 * 跳转客户管理列表界面
	 * @return
	 */
	@RequestMapping(value = "/khgl/pageListkhgl")
	public ModelAndView getKhglPageList() {
		ModelAndView mav=new ModelAndView("storehouse/khgl/khgl_list");
		mav.addObject("khlblist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.CUSTOMER_CATEGORY.getCode()));//客户管理类型
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 获取客户管理列表
	 * @return
	 */
	@RequestMapping(value = "/khgl/pageGetListkhgl")
	@ResponseBody
	public Map<String,Object> getKhglList(KhglDto khglDto){
		List<KhglDto> khgllist = IKhglxxService.getPagedDtoList(khglDto);
		Map<String, Object> map=new HashMap<>();
		map.put("total",khglDto.getTotalNumber());
		map.put("rows",khgllist);
		return map;
	}
	/**
	 * 获取客户管理列表钉钉
	 * @return
	 */
	@RequestMapping(value = "/khgl/minidataGetPageListkhgl")
	@ResponseBody
	public Map<String,Object> minidataGetPageListkhgl(KhglDto khglDto){
		return getKhglList(khglDto);
	}

	/**
	 * 查看客户管理信息
	 * @param khglDto
	 * @return
	 */
	@RequestMapping(value = "/khgl/viewKhgl")
	public ModelAndView viewKhglxx(KhglDto khglDto) {
		ModelAndView mav=new ModelAndView("storehouse/khgl/khgl_view");
		KhglDto dtoById = IKhglxxService.getDtoById(khglDto.getKhid());
		mav.addObject("khglDto", dtoById);
		FjcfbDto fjcfbDto=new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_CUSTOM.getCode());
		fjcfbDto.setYwid(khglDto.getKhid());
		List<FjcfbDto> fjcfbDtos=fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("fjcfbDtos", fjcfbDtos);
		return mav;
	}
	/**
	 * 获取客户管理列表
	 * @return
	 */
	@RequestMapping(value = "/khgl/minidataGetViewKhgl")
	@ResponseBody
	public Map<String,Object> minidataGetViewKhgl(KhglDto khglDto){
		KhglDto dtoById = null;
		if (khglDto!=null){
			dtoById = IKhglxxService.getDtoById(khglDto.getKhid());
		}		Map<String, Object> map=new HashMap<>();
		map.put("khglDto",dtoById);
		return map;
	}
	/**
	 * 查看客户管理信息
	 * @param khglDto
	 * @return
	 */
	@RequestMapping(value = "/khgl/pagedataViewKhgl")
	public ModelAndView pagedataViewKhgl(KhglDto khglDto) {
		ModelAndView mav=new ModelAndView("storehouse/khgl/khgl_view");
		KhglDto dtoById = null;
		if (khglDto!=null){
			dtoById = IKhglxxService.getDtoById(khglDto.getKhid());
		}
		mav.addObject("khglDto", dtoById);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 跳转至客户新增界面
	 * @return
	 */
	@RequestMapping(value = "/khgl/addKhgl")
	@ResponseBody
	public ModelAndView toAddKhglPage(KhglDto khglDto) {
		ModelAndView mav=new ModelAndView("storehouse/khgl/khgl_add");
		khglDto.setFormAction("addSaveKhgl");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		khglDto.setFzrq(df.format(new Date()));
		//Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.KHGL_TYPE,BasicDataTypeEnum.PROVINCE,BasicDataTypeEnum.CURRENCY});
		mav.addObject("khgllxlist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.KHGL_TYPE.getCode()));//客户管理类型
		mav.addObject("sflist",  redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.PROVINCE.getCode()));//省份
		mav.addObject("bizlist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.CURRENCY.getCode()));//币种
		mav.addObject("khlblist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.CUSTOMER_CATEGORY.getCode()));//客户类别
		mav.addObject("khglDto", khglDto);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("ywlx", BusTypeEnum.IMP_CUSTOM.getCode());
		return mav;
	}

	@RequestMapping(value="/khgl/pagedataGetKhdm")
	@ResponseBody
	public Map<String, Object> getKhdm(String sf){
		Map<String, Object> map = new HashMap<>();
		//获取省份参数扩展2
		JcsjDto jcsjDto = jcsjService.getDtoById(sf);
		KhglDto khglDto = new KhglDto();
		if (jcsjDto!=null){
			String sfkzcs = jcsjDto.getCskz2();
			if (StringUtil.isBlank(sfkzcs)){
				map.put("khdm","");
				return map;
			}
			//根据参数扩展2模糊查询长度为7的最大的供应商代码
			khglDto.setKhdm("200"+sfkzcs);
			// Integer num = IKhglxxService.countMax(khglDto);
			CustomerDto customerDto = new CustomerDto();
			customerDto.setcCusCode(khglDto.getKhdm());
			Integer num  = customerDao.countMax(customerDto);
			if(num==null) {//没有最大，初始化
				khglDto.setKhdm("200"+sfkzcs+"0001");
			}else {
				//有最大，最大+1
				int numnew = num+1;
				String string = Integer.toString(numnew);
				String newString = string.substring(string.length()-4); //截取后4位
				khglDto.setKhdm("200"+sfkzcs+newString); //200+省份+后四位
			}
		}
		map.put("khdm", khglDto.getKhdm());
		return map;
	}

	/**
	 * 客户新增保存
	 *
	 * @param khglDto
	 * @return
	 */
	@RequestMapping("/khgl/addSaveKhgl")
	@ResponseBody
	public Map<String, Object> addSaveKhgl(KhglDto khglDto,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		khglDto.setLrry(user.getYhid());
		khglDto.setLrrymc(user.getZsxm());
        CustomerDto dtoByKhdm = customerDao.getDtoByKhdm(khglDto.getKhdm());
        KhglDto khglDtoByKhdm = IKhglxxService.getKhglDtoByKhdm(khglDto.getKhdm());
		CustomerDto dtoByKhjc = customerDao.getDtoByKhjc(khglDto.getKhjc());
		KhglDto khglDtoByKhjc = IKhglxxService.getKhglDtoByKhjc(khglDto.getKhjc());
        if (dtoByKhdm!=null||khglDtoByKhdm!=null){
            map.put("status", "khdmrepetition");
            map.put("urlPrefix",urlPrefix);
        }else if (dtoByKhjc!=null||khglDtoByKhjc!=null){
			map.put("status", "jcrepetition");
			map.put("urlPrefix",urlPrefix);
		}else{
			try {
				boolean isSuccess = IKhglxxService.insertKhglxx(khglDto);
				map.put("status", isSuccess ? "success" : "fail");
				map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
				map.put("urlPrefix",urlPrefix);
			} catch (BusinessException e) {
				map.put("status", "fail");
				map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
				map.put("urlPrefix",urlPrefix);
			}

        }
		return map;
	}


    /**
     * 跳转至修改界面
     * @param khglDto
     * @return
     */
    @RequestMapping(value = "/khgl/modKhgl")
    public ModelAndView modKhgl(KhglDto khglDto) {
        ModelAndView mav=new ModelAndView("storehouse/khgl/khgl_add");
        KhglDto khglxx = IKhglxxService.getDtoById(khglDto.getKhid());
        khglxx.setXgqkhdm(khglxx.getKhdm());
        khglxx.setXgqkhjc(khglxx.getKhjc());
        khglxx.setFormAction("modSaveKhgl");
		FjcfbDto fjcfbDto=new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_CUSTOM.getCode());
		fjcfbDto.setYwid(khglDto.getKhid());
		List<FjcfbDto> fjcfbDtos=fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.KHGL_TYPE,BasicDataTypeEnum.PROVINCE,BasicDataTypeEnum.CURRENCY
				,BasicDataTypeEnum.CUSTOMER_CATEGORY});
        mav.addObject("khgllxlist", jclist.get(BasicDataTypeEnum.KHGL_TYPE.getCode()));//客户管理类型
        mav.addObject("sflist", jclist.get(BasicDataTypeEnum.PROVINCE.getCode()));//省份
        mav.addObject("bizlist", jclist.get(BasicDataTypeEnum.CURRENCY.getCode()));//币种
		mav.addObject("khlblist", jclist.get(BasicDataTypeEnum.CUSTOMER_CATEGORY.getCode()));//客户类别
		mav.addObject("khglDto", khglxx);
		mav.addObject("fjlist", fjcfbDtos);
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("ywlx", BusTypeEnum.IMP_CUSTOM.getCode());
        return mav;
    }

    /**
     * 修改保存客户管理信息
     * @param khglDto
     * @return
     */
    @RequestMapping(value ="/khgl/modSaveKhgl")
    @ResponseBody
    public Map<String,Object> modSaveKhgl(KhglDto khglDto, HttpServletRequest request) {
        User user = getLoginInfo(request);
        khglDto.setXgry(user.getYhid());
		khglDto.setXgrymc(user.getZsxm());
        Map<String, Object> map = new HashMap<>();
		CustomerDto dtoByKhdm = customerDao.getDtoByKhdm(khglDto.getKhdm());
		KhglDto khglDtoByKhdm = IKhglxxService.getKhglDtoByKhdm(khglDto.getKhdm());
		CustomerDto dtoByKhjc = customerDao.getDtoByKhjc(khglDto.getKhjc());
		KhglDto khglDtoByKhjc = IKhglxxService.getKhglDtoByKhjc(khglDto.getKhjc());
		if(dtoByKhdm!=null && !(khglDto.getYkhdm()).equals(dtoByKhdm.getcCusCode())){
			map.put("status", "khdmrepetition");
			map.put("urlPrefix",urlPrefix);
			return map;
		}
		if(khglDtoByKhdm!=null && !khglDto.getKhid().equals(khglDtoByKhdm.getKhid())){
			map.put("status", "khdmrepetition");
			map.put("urlPrefix",urlPrefix);
			return map;
		}
		if(dtoByKhjc!=null && !khglDto.getYkhdm().equals(dtoByKhjc.getcCusCode())){
			map.put("status", "jcrepetition");
			map.put("urlPrefix",urlPrefix);
			return map;
		}
		if(khglDtoByKhjc!=null && !khglDto.getKhid().equals(khglDtoByKhjc.getKhid())){
			map.put("status", "jcrepetition");
			map.put("urlPrefix",urlPrefix);
			return map;
		}

		try {
			boolean isSuccess = IKhglxxService.updateKhglxx(khglDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
			map.put("urlPrefix",urlPrefix);
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
			map.put("urlPrefix",urlPrefix);
		}
        return map;
    }


	/**
	 * 删除客户管理信息
	 * @param khglDto
	 * @return
	 */
	@RequestMapping(value ="/khgl/delKhgl")
	@ResponseBody
	public Map<String,Object> deleteKhglDto(KhglDto khglDto, HttpServletRequest request){
		User user = getLoginInfo(request);
		khglDto.setScry(user.getYhid());
		Map<String, Object> map = new HashMap<>();
		boolean isSuccess= IKhglxxService.deleteByKhglids(khglDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00003").getXxnr()) : xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	/**
	 * 跳转至生产控制界面
	 * @param khglDto
	 * @return
	 */
	@RequestMapping(value = "/khgl/productioncontrolKhgl")
	public ModelAndView productioncontrolKhgl(KhglDto khglDto) {
		ModelAndView mav=new ModelAndView("storehouse/khgl/khgl_control");
		khglDto.setFormAction("productioncontrolSaveKhgl");
		mav.addObject("khglDto", khglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 生产控制保存
	 * @param khglDto
	 * @return
	 */
	@RequestMapping(value ="/khgl/productioncontrolSaveKhgl")
	@ResponseBody
	public Map<String,Object> productioncontrolSavelKhgl(KhglDto khglDto){
		Map<String, Object> map = new HashMap<>();
		boolean isSuccess= IKhglxxService.updateList(khglDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
}
