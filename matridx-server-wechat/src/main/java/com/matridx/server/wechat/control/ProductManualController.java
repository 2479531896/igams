package com.matridx.server.wechat.control;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.server.wechat.dao.entities.CpwjDto;
import com.matridx.server.wechat.service.svcinterface.ICpwjService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WYX
 * @version 1.0
 * @className ProductManualController
 * @description 产品说明书
 * @date 15:21 2023/9/25
 **/
@Controller
@RequestMapping("/productManual")
public class ProductManualController extends BaseController {
    @Autowired
    IJcsjService jcsjService;
    @Autowired
    IXxglService xxglService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    ICpwjService cpwjService;
    @Autowired
    IFjcfbService fjcfbService;
    /**
     * 产品说明书列表 页面
     * @return
     */
    @RequestMapping("/productManual/pageListProductManual")
    public ModelAndView pageListProductManual() {
        return new ModelAndView("wechat/productManual/productManual_list");
    }
    /**
     * 产品说明书列表 数据
     * @return
     */
    @RequestMapping("/productManual/pageGetListProductManual")
    @ResponseBody
    public Map<String, Object> pageGetListProductManual(CpwjDto cpwjDto) {
        Map<String, Object> map= new HashMap<>();
        List<CpwjDto> rows = cpwjService.getPagedDtoList(cpwjDto);
        map.put("total",cpwjDto.getTotalNumber());
        map.put("rows", rows);
        return map;
    }
    /**
     * 产品说明书新增 页面
     * @return
     */
    @RequestMapping("/productManual/addProductManual")
    public ModelAndView addProductManual(CpwjDto cpwjDto) {
        ModelAndView mav = new ModelAndView("wechat/productManual/productManual_edit");
        cpwjDto.setFormAction("addSaveProductManual");
        cpwjDto.setGxsj(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd"));
        mav.addObject("ywlx", BusTypeEnum.IMP_PRODUCT_MANUAL.getCode());
        mav.addObject("cpwjDto",cpwjDto);
        return mav;
    }
    /**
     * 产品说明书新增保存
     * @return
     */
    @RequestMapping("/productManual/addSaveProductManual")
    @ResponseBody
    public Map<String, Object> addSaveProductManual(CpwjDto cpwjDto) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo();
        cpwjDto.setLrry(user.getYhid());
        boolean isSuccess;
        try {
            isSuccess = cpwjService.addSaveProductManual(cpwjDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        }catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }

    /**
     * @Description: 产品说明书修改
     * @param cpwjDto
     * @return org.springframework.web.servlet.ModelAndView
     * @Author: 郭祥杰
     * @Date: 2025/4/18 17:16
     */
    @RequestMapping("/productManual/modProductManual")
    public ModelAndView modProductManual(CpwjDto cpwjDto) {
        ModelAndView mav = new ModelAndView("wechat/productManual/productManual_edit");
        CpwjDto dto = cpwjService.getDtoById(cpwjDto.getCpwjid());
        dto.setFormAction("modSaveProductManual");
        mav.addObject("ywlx", BusTypeEnum.IMP_PRODUCT_MANUAL.getCode());
        mav.addObject("cpwjDto",dto);
        FjcfbDto fjcfbDto=new FjcfbDto();
        fjcfbDto.setYwid(cpwjDto.getCpwjid());
        fjcfbDto.setYwlx(BusTypeEnum.IMP_PRODUCT_MANUAL.getCode());
        List<FjcfbDto> fjcfbDtos = fjcfbService.getDtoList(fjcfbDto);
        mav.addObject("fjcfbDtos",fjcfbDtos);
        return mav;
    }

    /**
     * @Description: 产品说明书修改保存
     * @param cpwjDto
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2025/4/21 10:24
     */
    @RequestMapping("/productManual/modSaveProductManual")
    @ResponseBody
    public Map<String, Object> modSaveProductManual(CpwjDto cpwjDto) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo();
        cpwjDto.setXgry(user.getYhid());
        boolean isSuccess;
        try {
            isSuccess = cpwjService.modSaveProductManual(cpwjDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        }catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }

    /**
     * 产品说明书查看 页面
     * @return
     */
    @RequestMapping("/productManual/viewProductManual")
    public ModelAndView viewProductManual(CpwjDto cpwjDto) {
        ModelAndView mav = new ModelAndView("wechat/productManual/productManual_view");
        cpwjDto = cpwjService.getDtoById(cpwjDto.getCpwjid());
        //查询附件
        FjcfbDto fjcfbDto = new FjcfbDto();
        fjcfbDto.setYwlx(BusTypeEnum.IMP_PRODUCT_MANUAL.getCode());
        fjcfbDto.setYwid(cpwjDto.getCpwjid());
        List<FjcfbDto> t_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        mav.addObject("t_fjcfbDtos",t_fjcfbDtos);
        fjcfbDto.setYwlx(BusTypeEnum.IMP_PRODUCT_MANUAL_QRCODE.getCode());
        List<FjcfbDto> h_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        mav.addObject("h_fjcfbDtos",h_fjcfbDtos);
        mav.addObject("cpwjDto",cpwjDto);
        return mav;
    }
    /**
     * 产品说明书删除
     * @return
     */
    @RequestMapping("/productManual/delProductManual")
    @ResponseBody
    public Map<String, Object> delProductManual(CpwjDto cpwjDto) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo();
        cpwjDto.setScry(user.getYhid());
        boolean isSuccess;
        try {
            isSuccess = cpwjService.delProductManual(cpwjDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr() : xxglService.getModelById("ICOM00004").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00004").getXxnr());
        }catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00004").getXxnr());
        }
        return map;
    }
    /**
     * 产品说明书 修改是否公开
     * @return
     */
    @RequestMapping("/productManual/publicornotProductManual")
    @ResponseBody
    public Map<String, Object> publicornotProductManual(CpwjDto cpwjDto) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo();
        cpwjDto.setXgry(user.getYhid());
        boolean isSuccess;
        try {
            isSuccess = cpwjService.publicornotProductManual(cpwjDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        }catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }
}
