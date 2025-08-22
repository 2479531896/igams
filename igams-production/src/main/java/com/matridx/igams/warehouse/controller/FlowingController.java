package com.matridx.igams.warehouse.controller;
import com.matridx.igams.common.cloud.controller.BaseBasicController;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.warehouse.dao.entities.FlowingDto;
import com.matridx.igams.warehouse.service.svcinterface.IFlowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author:JYK
 */
@Controller
@RequestMapping("/warehouse")
public class FlowingController extends BaseBasicController {
 @Autowired
 private IFlowingService flowingService;
 @Autowired
 IJcsjService jcsjService;
 @Value("${matridx.prefix.urlprefix:}")
 private String urlPrefix;
 @Override
 public String getPrefix(){
  return urlPrefix;
 }

 /**
  * 拜访对象列表
  */
 @RequestMapping("/flowing/pageListFlowing")
 public ModelAndView pageListFlowingDto() {
  ModelAndView mav = new ModelAndView("warehouse/flowing/flowing_list");
  mav.addObject("urlPrefix",urlPrefix);
  return mav;
 }
 /**
  * 获取列表数据
  */
 @RequestMapping("/flowing/pageGetListFlowing")
 @ResponseBody
 public Map<String,Object> listFlowing(FlowingDto flowingDto){
  Map<String,Object> map = new HashMap<>();
  List<FlowingDto> flowinglist=flowingService.getPageFlowingDtoList(flowingDto);
  map.put("total",flowingDto.getTotalNumber());
  map.put("rows",flowinglist);
  return map;
 }
 /**
  * 查看信息对应详情
  */
 @RequestMapping("/flowing/viewFlowing")
 public ModelAndView viewFlowingList(FlowingDto flowingDto, HttpServletRequest request){
  ModelAndView mav = new ModelAndView("warehouse/flowing/flowing_view");
  String djlx=request.getParameter("djlx");
  String viewId=request.getParameter("viewId");
  String sflb=request.getParameter("sflb");
  flowingDto.setViewId(viewId);
  FlowingDto flowingDto_t;
  if ("借出单".equals(djlx)){
   flowingDto_t=flowingService.getJcById(flowingDto);
  }
  else if ("归还单".equals(djlx)){
   flowingDto_t=flowingService.getGhById(flowingDto);

  }
  else if ("入库单".equals(djlx)){
   flowingDto_t=flowingService.getRkById(flowingDto);

  }
  else if ("出库单".equals(djlx)){
   flowingDto_t=flowingService.getCkById(flowingDto);
  }
  else if ("调拨单".equals(djlx)&&"其他出库类型".equals(sflb)){
   flowingDto_t=flowingService.getDcById(flowingDto);

  }
  else if ("调拨单".equals(djlx)&&"其他入库类型".equals(sflb)){
   flowingDto_t=flowingService.getDrById(flowingDto);

  }
  else if ("到货单".equals(djlx)){
   flowingDto_t=flowingService.getDhById(flowingDto);
  }
  else{
   flowingDto_t=flowingService.getFhById(flowingDto);

  }
  mav.addObject("flowingDto",flowingDto_t);
  mav.addObject("urlPrefix",urlPrefix);
  return  mav;
 }
}

