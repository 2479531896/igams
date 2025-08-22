package com.matridx.igams.storehouse.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import javax.servlet.http.HttpServletRequest;


import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.storehouse.dao.entities.CkjgxxDto;
import com.matridx.igams.storehouse.service.svcinterface.ICkjgxxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.matridx.igams.common.cloud.controller.BaseBasicController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.storehouse.dao.entities.CkxxDto;
import com.matridx.igams.storehouse.service.svcinterface.ICkxxService;
import com.matridx.springboot.util.base.StringUtil;

@Controller
@RequestMapping("/warehouse")
public class WarehouseController extends BaseBasicController{
	@Autowired
	private ICkxxService ckxxService;
	@Autowired
	private IJcsjService jcsjService;
	@Autowired
	private IXxglService xxglservice;
	@Autowired
	private ICkjgxxService ckjgxxService;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Override
	public String getPrefix(){
		return urlPrefix;
	}
	/**
	 * 跳转页面
	 * @return
	 */
	@RequestMapping("/view/pageListView")
	public ModelAndView View(){
		ModelAndView mav=new ModelAndView("storehouse/ckxx/warehouse_List");

		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[] { BasicDataTypeEnum.WAREHOUSE_TYPE, BasicDataTypeEnum.CK_PERMISSIONS_TYPE});
		mav.addObject("cklblist", jclist.get(BasicDataTypeEnum.WAREHOUSE_TYPE.getCode()));
		mav.addObject("ckfllist", jclist.get(BasicDataTypeEnum.CK_PERMISSIONS_TYPE.getCode()));
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 仓库信息列表
	 * @param ckxxDto
	 * @return
	 */
	@RequestMapping("/view/pageGetListView")
	@ResponseBody
	public Map<String,Object> getpageList(CkxxDto ckxxDto){
		Map<String,Object> map=new HashMap<>();
		List<CkxxDto> ckxxList=ckxxService.getPagedDtoList(ckxxDto);
		map.put("total", ckxxDto.getTotalNumber());
    	map.put("rows", ckxxList);
		return map;
	}
	
	/**
	 * 新增仓库页面
	 * @param ckxxDto
	 * @return
	 */
	@RequestMapping("/warehouse/addCkxx")
	public ModelAndView addCkxxView(CkxxDto ckxxDto) {
		ModelAndView mav=new ModelAndView("storehouse/ckxx/warehouse_add");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.WAREHOUSE_TYPE,BasicDataTypeEnum.WAREHOUSE_TYPE,BasicDataTypeEnum.CK_PERMISSIONS_TYPE});
		ckxxDto.setFckflg("1");
		List<CkxxDto> fcklist=ckxxService.getDtoList(ckxxDto);
		ckxxDto.setFormAction("addSaveCkxx");
		mav.addObject("cklblist", jclist.get(BasicDataTypeEnum.WAREHOUSE_TYPE.getCode()));
		mav.addObject("ckqxlist", jclist.get(BasicDataTypeEnum.CK_PERMISSIONS_TYPE.getCode()));
		mav.addObject("fcklist", fcklist);
		mav.addObject("ckxxDto", ckxxDto);
		mav.addObject("qflist", jclist.get(BasicDataTypeEnum.WAREHOUSE_TYPE.getCode()));
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
		}
	
	/**
	 * 新增保存
	 * @param ckxxDto
	 * @return
	 */
	@RequestMapping("/warehouse/addSaveCkxx")
	@ResponseBody
	public Map<String,Object> saveCkxx(CkxxDto ckxxDto, HttpServletRequest request){
		Map<String, Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		ckxxDto.setLrry(user.getYhid());
		ckxxDto.setCkid(StringUtil.generateUUID());
		boolean isSuccess=ckxxService.insert(ckxxDto);
    	map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
    	return map;
	}
	
	/**
	 * 修改之前查询单个信息
	 * @param ckid
	 * @return
	 */
	@RequestMapping("/warehouse/modCkxx")
	public ModelAndView getCkxxById(String ckid) {
		ModelAndView mav=new ModelAndView("storehouse/ckxx/warehouse_add");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.WAREHOUSE_TYPE,BasicDataTypeEnum.CK_PERMISSIONS_TYPE});
		CkxxDto ckxxDtos=new CkxxDto();
		ckxxDtos.setCkid(ckid);
		List<CkxxDto> fcklist=ckxxService.getDtoList(ckxxDtos);
		CkxxDto  ckxxDto=ckxxService.getDtoById(ckid);
		ckxxDto.setFormAction("modSaveCkxx");
		mav.addObject("fcklist", fcklist);
		mav.addObject("cklblist", jclist.get(BasicDataTypeEnum.WAREHOUSE_TYPE.getCode()));
		mav.addObject("ckqxlist", jclist.get(BasicDataTypeEnum.CK_PERMISSIONS_TYPE.getCode()));
		mav.addObject("ckxxDto", ckxxDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 修改保存操作
	 * @param ckxxDto
	 * @return
	 */
	@RequestMapping("/warehouse/modSaveCkxx")
	@ResponseBody
	public  Map<String,Object> updateCkxx(CkxxDto ckxxDto, HttpServletRequest request){
		Map<String, Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		ckxxDto.setXgry(user.getYhid());
		boolean isSuccess=ckxxService.update(ckxxDto);
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 查看页面
	 * @param ckxxDto
	 * @return
	 */
	@RequestMapping("/warehouse/viewCkxx")
	public ModelAndView getCkxxView(CkxxDto ckxxDto) {
		ModelAndView mav=new ModelAndView("storehouse/ckxx/warehouse_view");
		CkxxDto ckxxDtos=ckxxService.getDto(ckxxDto);
		mav.addObject("ckxxDto", ckxxDtos);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 删除仓库
	 * @param ckxxDto
	 * @return
	 */
	@RequestMapping("/warehouse/delCkxx")
	@ResponseBody
	public Map<String,Object> deleteCkxx(CkxxDto ckxxDto, HttpServletRequest request){
		Map<String,Object> map =new HashMap<>();
		User user=getLoginInfo(request);
		ckxxDto.setScry(user.getYhid());
		boolean isSuccess=ckxxService.deleteck(ckxxDto);
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglservice.getModelById("ICOM00003").getXxnr():xxglservice.getModelById("ICOM00004").getXxnr());
		return map;
		}
	
	/**
	 *	查询仓库信息
	 * @param ckxxDto
	 * @return
	 */
	@RequestMapping("/warehouse/pagedataQueryDtoList")
	@ResponseBody
	public List<CkxxDto> queryDtoList(CkxxDto ckxxDto){
		return ckxxService.getDtoList(ckxxDto);
	}

	/**
	 * 仓库统计
	 * @return
			 */
	@RequestMapping("/warehouse/pageStatisticsDepot")
	public ModelAndView depotStats(){
		ModelAndView mav = new ModelAndView("storehouse/ckxx/warehouse_stats");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 文件统计详情
	 * @return
	 */
	@RequestMapping("/warehouse/pagedataDepotStatsInfo")
	@ResponseBody
	public Map<String,Object> statsInfo(CkxxDto ckxxDto){
		Map<String,Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(ckxxDto.getLx())){
			if ("bmcrk".equals(ckxxDto.getLx())){
				List<CkxxDto> ckxxDtos = ckxxService.getCRkslByBm(ckxxDto);
				map.put("bmcrk",ckxxDtos);
			}else if ("lc".equals(ckxxDto.getLx())){
				List<CkxxDto> ckxxDtoList = ckxxService.getdepotStatusLcInfo(ckxxDto);
				map.put("lc",ckxxDtoList);
			}else if ("zt".equals(ckxxDto.getLx())){
				List<CkxxDto> ckxxDtos_zt = ckxxService.getdepotStatusZtInfo(ckxxDto);
				map.put("zt",ckxxDtos_zt);
			}else if ("khfh".equals(ckxxDto.getLx())){
				List<CkxxDto> ckxxDtos_khfh = ckxxService.getKhfhsl(ckxxDto);
				map.put("khfh",ckxxDtos_khfh);
			}else if ("khjysl".equals(ckxxDto.getLx())){
				List<CkxxDto> ckxxDtos_khjysl = ckxxService.getKhJysl(ckxxDto);
				map.put("khjysl",ckxxDtos_khjysl);
			}else if ("sjscjd".equals(ckxxDto.getLx())){
				ckxxDto.setCsdm("SJ");
				List<CkxxDto> dtos = ckxxService.getScLcInfoList(ckxxDto);
				map.put("sjscjd",dtos);
			}else if ("yqscjd".equals(ckxxDto.getLx())){
				ckxxDto.setCsdm("YQ");
				List<CkxxDto> dtoList = ckxxService.getScLcInfoList(ckxxDto);
				map.put("yqscjd",dtoList);
			}
		}else{
			List<CkxxDto> ckxxDtos_zt = ckxxService.getdepotStatusZtInfo(ckxxDto);
			List<CkxxDto> ckxxDtos = ckxxService.getCRkslByBm(ckxxDto);
			List<CkxxDto> ckxxDtoList = ckxxService.getdepotStatusLcInfo(ckxxDto);
			List<CkxxDto> ckxxDtos_khfh = ckxxService.getKhfhsl(ckxxDto);
			List<CkxxDto> ckxxDtos_khjysl = ckxxService.getKhJysl(ckxxDto);
			List<CkxxDto> ckxxDtos_ckkcl = ckxxService.getCkkcl(ckxxDto);
			List<CkxxDto> ckxxDtos_ckhwkcl = ckxxService.getDtoWlCkhwKcl(ckxxDto);
			ckxxDto.setCsdm("SJ");
			List<CkxxDto> dtos = ckxxService.getScLcInfoList(ckxxDto);
			map.put("sjscjd",dtos);
			ckxxDto.setCsdm("YQ");
			List<CkxxDto> dtoList = ckxxService.getScLcInfoList(ckxxDto);
			map.put("yqscjd",dtoList);
			map.put("bmcrk",ckxxDtos);
			map.put("lc",ckxxDtoList);
			map.put("zt",ckxxDtos_zt);
			map.put("khfh",ckxxDtos_khfh);
			map.put("khjysl",ckxxDtos_khjysl);
			map.put("ckkcl",ckxxDtos_ckkcl);
			map.put("ckhwkcl",ckxxDtos_ckhwkcl);
		}
		return map;
	}

	/**
	 * 按物料类别统计
	 * @return
	 */
	@RequestMapping("/warehouse/categoryStatus")
	public ModelAndView depotStats(CkxxDto ckxxDto){
		ModelAndView mav = new ModelAndView("storehouse/ckxx/warehouse_categoryStatus");
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("ckxxDto", ckxxDto);
		return mav;
	}

	/**
	 * 文件统计详情
	 * @return
	 */
	@RequestMapping("/warehouse/categoryStatusInfo")
	@ResponseBody
	public Map<String,Object> categoryStatusInfo(CkxxDto ckxxDto){
		List<CkxxDto> ckxxDtos= new ArrayList<>();
		if ("出库数量".equals(ckxxDto.getLx())){
			ckxxDtos = ckxxService.getDtoZlCksl(ckxxDto);
		}else if ("入库数量".equals(ckxxDto.getLx())){
			ckxxDtos = ckxxService.getDtoZlRksl(ckxxDto);
		}else if ("请购数量".equals(ckxxDto.getLx())){
			ckxxDtos = ckxxService.getDtoZlLcQgsl(ckxxDto);
		}else if ("采购入库".equals(ckxxDto.getLx()) || "成品入库".equals(ckxxDto.getLx()) || "其它入库".equals(ckxxDto.getLx())){
			ckxxDtos = ckxxService.getDtoZlLcDhsl(ckxxDto);
		}else if ("质检数量".equals(ckxxDto.getLx())){
			ckxxDtos = ckxxService.getDtoZlLcZjhgsl(ckxxDto);
		}else if ("质检不合格数".equals(ckxxDto.getLx())){
			ckxxDtos = ckxxService.getDtoZlLcZjbhgsl(ckxxDto);
		}else if ("领料出库".equals(ckxxDto.getLx())){
			ckxxDtos = ckxxService.getDtoZlLcLlck(ckxxDto);
		}else if ("借用数量".equals(ckxxDto.getLx())){
			ckxxDtos = ckxxService.getDtoZlLcJysl(ckxxDto);
		}else if ("归还数量".equals(ckxxDto.getLx())){
			ckxxDtos = ckxxService.getDtoZlLcGhsl(ckxxDto);
		}else if ("发货数量".equals(ckxxDto.getLx())){
			ckxxDto.setSfbj("1");
			ckxxDtos = ckxxService.getDtoZlLcSfsl(ckxxDto);
		}else if ("退货数量".equals(ckxxDto.getLx())){
			ckxxDto.setSfbj("0");
			ckxxDtos = ckxxService.getDtoZlLcSfsl(ckxxDto);
		}else if ("请购中".equals(ckxxDto.getLx())){
			ckxxDtos = ckxxService.getDtoZlZtQgzsl(ckxxDto);
		}else if ("采购中".equals(ckxxDto.getLx())){
			ckxxDtos = ckxxService.getDtoZlZtHtcgzsl(ckxxDto);
		}else if ("未到货".equals(ckxxDto.getLx())){
			ckxxDtos = ckxxService.getDtoZlZtHtwdhsl(ckxxDto);
		}else if ("质检中".equals(ckxxDto.getLx())){
			ckxxDtos = ckxxService.getDtoZlZtzjzsl(ckxxDto);
		}else if ("待处理".equals(ckxxDto.getLx())){
			ckxxDtos = ckxxService.getDtoZlZtzjdclsl(ckxxDto);
		}else if ("khfh".equals(ckxxDto.getLx())){
			ckxxDtos = ckxxService.getDtoZlKhfh(ckxxDto);
		}else if ("khjysl".equals(ckxxDto.getLx())){
			ckxxDtos = ckxxService.getDtoZlKhjy(ckxxDto);
		}else if ("ckkcl".equals(ckxxDto.getLx())){
			ckxxDtos = ckxxService.getDtoWlCkKcl(ckxxDto);
		}
		Map<String,Object> map = new HashMap<>();
		map.put("wllb",ckxxDtos);
		return map;
	}

	/**
	 * 按物料类别统计
	 * @returnju
	 */
	@RequestMapping("/warehouse/itemInfo")
	public ModelAndView itemInfo(CkxxDto ckxxDto){
		ModelAndView mav;
		if (StringUtil.isNotBlank(ckxxDto.getLx())){
			if ("出库数量".equals(ckxxDto.getLx()) || "入库数量".equals(ckxxDto.getLx())){
				mav = new ModelAndView("storehouse/ckxx/item_info");
				List<CkxxDto> itemInfo = ckxxService.getItemInfo(ckxxDto);
				mav.addObject("itemInfo",itemInfo);
				return mav;
			}else if ("成品入库".equals(ckxxDto.getLx()) || "其它入库".equals(ckxxDto.getLx())){
				mav = new ModelAndView("storehouse/ckxx/lc_item_cpsl_info");
				List<CkxxDto> itemInfo = ckxxService.getItemInfoByLcCpsl(ckxxDto);
				mav.addObject("itemInfo",itemInfo);
				return mav;
			}else if ("领料出库".equals(ckxxDto.getLx())){
				mav = new ModelAndView("storehouse/ckxx/lc_item_llsl_info");
				List<CkxxDto> itemInfo = ckxxService.getItemInfoByLcLlsl(ckxxDto);
				mav.addObject("itemInfo",itemInfo);
				return mav;
			}else if ("khfh".equals(ckxxDto.getLx())){
				mav = new ModelAndView("storehouse/ckxx/kh_item_sl_info");
				List<CkxxDto> itemInfo = ckxxService.getItemInfoByFhsl(ckxxDto);
				mav.addObject("itemInfo",itemInfo);
				mav.addObject("lx",ckxxDto.getLx());
				return mav;
			}else if ("khjysl".equals(ckxxDto.getLx())){
				mav = new ModelAndView("storehouse/ckxx/kh_item_sl_info");
				List<CkxxDto> itemInfo = ckxxService.getItemInfoByJysl(ckxxDto);
				mav.addObject("itemInfo",itemInfo);
				mav.addObject("lx",ckxxDto.getLx());
				return mav;
			}else if ("请购数量".equals(ckxxDto.getLx())){
				mav = new ModelAndView("storehouse/ckxx/lc_item_qgsl_info");
				List<CkxxDto> itemInfo = ckxxService.getItemInfoByLcQgsl(ckxxDto);
				mav.addObject("itemInfo",itemInfo);
				return mav;
			}else if ("请购中".equals(ckxxDto.getLx())){
				mav = new ModelAndView("storehouse/ckxx/lc_item_qgsl_info");
				List<CkxxDto> itemInfo = ckxxService.getItemInfoByZtQgsl(ckxxDto);
				mav.addObject("itemInfo",itemInfo);
				return mav;
			}else if ("采购中".equals(ckxxDto.getLx())){
				mav = new ModelAndView("storehouse/ckxx/zt_item_cgz_info");
				List<CkxxDto> itemInfo = ckxxService.getItemInfoByZtHtcgzsl(ckxxDto);
				mav.addObject("itemInfo",itemInfo);
				return mav;
			}else if ("未到货".equals(ckxxDto.getLx())){
				mav = new ModelAndView("storehouse/ckxx/zt_item_cgz_info");
				List<CkxxDto> itemInfo = ckxxService.getItemInfoByZtHtwdhsl(ckxxDto);
				mav.addObject("itemInfo",itemInfo);
				return mav;
			}else if ("ckkcl".equals(ckxxDto.getLx())){
				mav = new ModelAndView("storehouse/ckxx/wl_item_kcl_info");
				List<CkxxDto> itemInfo = ckxxService.getItemInfoByWlCkKcl(ckxxDto);
				mav.addObject("itemInfo",itemInfo);
				return mav;
			}else if ("ckhwkcl".equals(ckxxDto.getLx())){
				mav = new ModelAndView("storehouse/ckxx/wl_item_kcl_info");
				List<CkxxDto> itemInfo = ckxxService.getItemInfoByWlCkhwKcl(ckxxDto);
				mav.addObject("itemInfo",itemInfo);
				return mav;
			} else if ("采购入库".equals(ckxxDto.getLx())){
				mav = new ModelAndView("storehouse/ckxx/lc_item_cgrk_info");
				List<CkxxDto> itemInfo = ckxxService.getItemInfoByLcCgrk(ckxxDto);
				mav.addObject("itemInfo",itemInfo);
				return mav;
			}else if ("质检数量".equals(ckxxDto.getLx())||"质检不合格数".equals(ckxxDto.getLx())||"质检中".equals(ckxxDto.getLx())||"待处理".equals(ckxxDto.getLx())){
				mav = new ModelAndView("storehouse/ckxx/lc_item_zjsl_info");
				List<CkxxDto> itemInfo = ckxxService.getItemInfoByLcZjsl(ckxxDto);
				mav.addObject("itemInfo",itemInfo);
				return mav;
			}
		}
		return null;
	}


	/**
	 * 按物料类别统计Map
	 * @returnju
	 */
	@RequestMapping("/warehouse/itemInfoMap")
	@ResponseBody
	public Map<String,Object> itemInfoMap(CkxxDto ckxxDto){
		Map<String,Object> map = new HashMap<>();
		List<CkxxDto> itemInfo= new ArrayList<>();
		if (StringUtil.isNotBlank(ckxxDto.getLx())){
			if ("出库数量".equals(ckxxDto.getLx()) || "入库数量".equals(ckxxDto.getLx())){
				itemInfo = ckxxService.getItemInfo(ckxxDto);
			}else if ("成品入库".equals(ckxxDto.getLx()) || "其它入库".equals(ckxxDto.getLx())){
				itemInfo = ckxxService.getItemInfoByLcCpsl(ckxxDto);
			}else if ("领料出库".equals(ckxxDto.getLx())){
				itemInfo = ckxxService.getItemInfoByLcLlsl(ckxxDto);
			}else if ("khfh".equals(ckxxDto.getLx())){
				itemInfo = ckxxService.getItemInfoByFhsl(ckxxDto);
			}else if ("khjysl".equals(ckxxDto.getLx())){
				itemInfo = ckxxService.getItemInfoByJysl(ckxxDto);
			}else if ("请购数量".equals(ckxxDto.getLx())){
				itemInfo = ckxxService.getItemInfoByLcQgsl(ckxxDto);
			}else if ("请购中".equals(ckxxDto.getLx())){
				itemInfo = ckxxService.getItemInfoByZtQgsl(ckxxDto);
			}else if ("采购中".equals(ckxxDto.getLx())){
				itemInfo = ckxxService.getItemInfoByZtHtcgzsl(ckxxDto);
			}else if ("未到货".equals(ckxxDto.getLx())){
				itemInfo = ckxxService.getItemInfoByZtHtwdhsl(ckxxDto);
			}else if ("ckkcl".equals(ckxxDto.getLx())){
				itemInfo = ckxxService.getItemInfoByWlCkKcl(ckxxDto);
			}else if ("ckhwkcl".equals(ckxxDto.getLx())){
				itemInfo = ckxxService.getItemInfoByWlCkhwKcl(ckxxDto);
			} else if ("采购入库".equals(ckxxDto.getLx())){
				itemInfo = ckxxService.getItemInfoByLcCgrk(ckxxDto);
			}else if ("质检数量".equals(ckxxDto.getLx())||"质检不合格数".equals(ckxxDto.getLx())||"质检中".equals(ckxxDto.getLx())||"待处理".equals(ckxxDto.getLx())){
				itemInfo = ckxxService.getItemInfoByLcZjsl(ckxxDto);
			}
		}
		map.put("itemInfo",itemInfo);
		map.put("lx",ckxxDto.getLx());
		return map;
	}



	/**
	 * 生产进度统计详情
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/warehouse/productStatsInfo")
	@ResponseBody
	public Map<String,Object> productStatsInfo(CkxxDto ckxxDto){
		Map<String,Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(ckxxDto.getLx())){
			if ("sjscjd".equals(ckxxDto.getLx())){
				ckxxDto.setCsdm("SJ");
				List<CkxxDto> dtos = ckxxService.getScLcInfoList(ckxxDto);
				map.put("sjscjd",dtos);
			}else if ("yqscjd".equals(ckxxDto.getLx())){
				ckxxDto.setCsdm("YQ");
				List<CkxxDto> dtos = ckxxService.getScLcInfoList(ckxxDto);
				map.put("yqscjd",dtos);
			}
		}else{
			ckxxDto.setCsdm("SJ");
			List<CkxxDto> dtos = ckxxService.getScLcInfoList(ckxxDto);
			map.put("sjscjd",dtos);
			ckxxDto.setCsdm("YQ");
			List<CkxxDto> dtoList = ckxxService.getScLcInfoList(ckxxDto);
			map.put("yqscjd",dtoList);
		}
		List<CkxxDto> sjdtos = (List<CkxxDto>) map.get("sjscjd");
		List<CkxxDto> yqdtos = (List<CkxxDto>) map.get("yqscjd");
		if (!CollectionUtils.isEmpty(sjdtos)){
			double zs = 0;
			for (CkxxDto sjdto : sjdtos) {
				double sj = 0;
				if (StringUtil.isNotBlank(sjdto.getSl())){
					sj = Double.parseDouble(sjdto.getSl());
					if (sj<0){
						sj = 0;
					}
				}
				zs = zs + sj;
			}
			map.put("sjpjsczq",new BigDecimal(zs).divide(new BigDecimal(sjdtos.size()),2, RoundingMode.HALF_UP));
		}else if (!CollectionUtils.isEmpty(yqdtos)){
			double zs = 0;
			for (CkxxDto yqdto : yqdtos) {
				double sj = 0;
				if (StringUtil.isNotBlank(yqdto.getSl())){
					sj = Double.parseDouble(yqdto.getSl());
					if (sj<0){
						sj = 0;
					}
				}
				zs = zs + sj;
			}
			map.put("yqpjsczq",new BigDecimal(zs).divide(new BigDecimal(yqdtos.size()),2,RoundingMode.HALF_UP));
		}
		return map;
	}
	/**
	 * 钉钉生产进度统计详情
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/warehouse/minidataProductStatsInfo")
	@ResponseBody
	public Map<String,Object> minidataProductStatsInfo(CkxxDto ckxxDto){
		Map<String,Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(ckxxDto.getLx())){
			if ("sjscjd".equals(ckxxDto.getLx())){
				ckxxDto.setCsdm("SJ");
				List<CkxxDto> dtos = ckxxService.getScLcInfoList(ckxxDto);
				map.put("sjscjd",dtos);
			}else if ("yqscjd".equals(ckxxDto.getLx())){
				ckxxDto.setCsdm("YQ");
				List<CkxxDto> dtos = ckxxService.getScLcInfoList(ckxxDto);
				map.put("yqscjd",dtos);
			}
		}else{
			ckxxDto.setCsdm("SJ");
			List<CkxxDto> dtos = ckxxService.getScLcInfoList(ckxxDto);
			map.put("sjscjd",dtos);
			ckxxDto.setCsdm("YQ");
			List<CkxxDto> dtoList = ckxxService.getScLcInfoList(ckxxDto);
			map.put("yqscjd",dtoList);
		}
		List<CkxxDto> sjdtos = (List<CkxxDto>) map.get("sjscjd");
		List<CkxxDto> yqdtos = (List<CkxxDto>) map.get("yqscjd");
		if (!CollectionUtils.isEmpty(sjdtos)){
			double zs = 0;
			for (CkxxDto sjdto : sjdtos) {
				double sj = 0;
				if (StringUtil.isNotBlank(sjdto.getSl())){
					sj = Double.parseDouble(sjdto.getSl());
					if (sj<0){
						sj = 0;
					}
				}
				zs = zs + sj;
			}
			map.put("sjpjsczq",new BigDecimal(zs).divide(new BigDecimal(sjdtos.size()),2,RoundingMode.HALF_UP));
		}else if (!CollectionUtils.isEmpty(yqdtos)){
			double zs = 0;
			for (CkxxDto yqdto : yqdtos) {
				double sj = 0;
				if (StringUtil.isNotBlank(yqdto.getSl())){
					sj = Double.parseDouble(yqdto.getSl());
					if (sj<0){
						sj = 0;
					}
				}
				zs = zs + sj;
			}
			map.put("yqpjsczq",new BigDecimal(zs).divide(new BigDecimal(yqdtos.size()),2,RoundingMode.HALF_UP));
		}
		return map;
	}


	/**
	 * 仓库列表  权限设置
	 */
	@RequestMapping("/warehouse/batchpermitCkxx")
	public ModelAndView batchpermitCkxx(CkjgxxDto ckjgxxDto) {
		ModelAndView mav = new ModelAndView("storehouse/ckxx/warehouse_batchpermit");
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("ckjgxxDto", ckjgxxDto);
		return mav;
	}

	/**
	 * 仓库机构信息数据
	 */
	@RequestMapping("/warehouse/pagedataGetDepartmentsData")
	@ResponseBody
	public Map<String,Object> pagedataGetDepartmentsData(CkjgxxDto ckjgxxDto){
		Map<String,Object> map=new HashMap<>();
		List<CkjgxxDto> list=ckjgxxService.getDtoList(ckjgxxDto);
		map.put("rows", list);
		return map;
	}

	/**
	 * 仓库列表  权限设置保存
	 */
	@RequestMapping("/warehouse/batchpermitSaveCkxx")
	@ResponseBody
	public Map<String,Object> batchpermitSaveCkxx(CkjgxxDto ckjgxxDto){
		Map<String, Object> map=new HashMap<>();
		boolean isSuccess=ckjgxxService.batchpermitSaveCkxx(ckjgxxDto);
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
		return map;
	}

}
