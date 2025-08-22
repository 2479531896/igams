package com.matridx.las.home.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.enums.YesNotEnum;
import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.channel.command.SendBaseCommand;
import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.global.InstrumentStateGlobal;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.las.netty.dao.entities.WkglDto;
import com.matridx.las.netty.service.svcinterface.IWkglService;
import com.matridx.springboot.util.base.StringUtil;

@Controller
@RequestMapping("/lashome")
public class LibraryManagementController extends BaseController {
    @Autowired
    IWkglService wkglService;

    @Autowired
    IFjcfbService fjcfbService;

    @Autowired
    IXxglService xxglService;

    /**
     * 跳转文库列表界面
     *
     * @return
     */
    @RequestMapping(value = "/library/pageListLibrary")
    public ModelAndView getLibraryPage() {
        ModelAndView mav = new ModelAndView("lashome/library/library_list");
        return mav;
    }

    /**
     * 获取文库列表
     *
     * @param wkglDto
     * @return
     */
    @RequestMapping("/library/listlibrary")
    @ResponseBody
    public Map<String, Object> getLibraryPageList(WkglDto wkglDto) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<WkglDto> wkglDtos = wkglService.getPagedDtoList(wkglDto);
        map.put("total", wkglDto.getTotalNumber());
        map.put("rows", wkglDtos);
        return map;
    }

    /**
     * 生成文库文件
     *
     * @param wkglDto
     * @return
     */
    @RequestMapping("/library/export")
    @ResponseBody
    public Map<String, Object> replaceContract(WkglDto wkglDto) {
        Map<String, Object> map = wkglService.getParamForLibrary(wkglDto);
        return map;
    }

    /**
     * 文库信息导入界面
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/library/import")
    public ModelAndView formalContractView(WkglDto wkglDto) {
        ModelAndView mav = new ModelAndView("lashome/library/library_import");
        FjcfbDto fjcfbDto = new FjcfbDto();
        fjcfbDto.setYwlx(BusTypeEnum.IMP_LIBRARY_EXCEL_IN.getCode());
        fjcfbDto.setYwid(wkglDto.getWkid());
        List<FjcfbDto> t_fjcfbDtos = new ArrayList<FjcfbDto>();
        mav.addObject("fjcfbDtos", t_fjcfbDtos);
        mav.addObject("ywlx", BusTypeEnum.IMP_LIBRARY_EXCEL_IN.getCode());
        mav.addObject("wkglDto", wkglDto);
        return mav;
    }

    /**
     * 文库信息导入界面
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/library/import_vue")
    @ResponseBody
    public Map<String, Object> formalContractView_vue(WkglDto wkglDto) {
        FjcfbDto fjcfbDto = new FjcfbDto();
        fjcfbDto.setYwlx(BusTypeEnum.IMP_LIBRARY_EXCEL_IN.getCode());
        fjcfbDto.setYwid(wkglDto.getWkid());
        List<FjcfbDto> t_fjcfbDtos = new ArrayList<FjcfbDto>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", "success");
        map.put("fjcfbDtos", JSON.toJSONString(t_fjcfbDtos));
        map.put("ywlx", BusTypeEnum.IMP_LIBRARY_EXCEL_IN.getCode());
        map.put("wkglDto", JSON.toJSONString(wkglDto));
        return map;
    }

    /**
     * 文库信息导入保存
     *
     * @param wkglDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/library/importSave")
    public Map<String, Object> importSave(WkglDto wkglDto, HttpServletRequest request) {
        User user = getLoginInfo(request);
        wkglDto.setXgry(user.getYhid());
        Map<String, Object> map = new HashMap<String, Object>();
        boolean isSuccess;
        try {
            map = wkglService.modSaveLibrary(wkglDto);
            //如果没到如果则调用Auto开始
            isSuccess = Boolean.valueOf(map.get("flag").toString());
            if (YesNotEnum.NOT.getCode().equals(map.get("sfdr").toString())) {
                //通过wkid找仪器id
                String deid = InstrumentStateGlobal.getAutoIdWkid(wkglDto.getWkid());
                FrameModel frameModel = new FrameModel();
                frameModel.setDeviceID(deid);
                frameModel.setCommand(Command.AUTO.toString());
                new SendBaseCommand().sendEventFlowlist("205", frameModel);
            }
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
                    : xxglService.getModelById("ICOM00002").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg()) ? e.getMsg() : xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }
}
