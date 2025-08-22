package com.matridx.igams.wechat.control;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.wechat.dao.entities.SwskglDto;
import com.matridx.igams.wechat.service.svcinterface.ISwskglService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/receipts")
public class BusinessReceiptsController extends BaseController {

    @Autowired
    private ISwskglService swskglService;
    @Autowired
    IXxglService xxglService;

    /**
     * 商务收款列表
     */
    @RequestMapping("/receipts/pageGetListBusinessReceipts")
    @ResponseBody
    public Map<String, Object> pageGetListBusinessReceipts(SwskglDto swskglDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        super.setCzdmList(request,map);
        super.setTyszList(request,map);
        List<SwskglDto> list = swskglService.getPagedDtoList(swskglDto);
        map.put("total", swskglDto.getTotalNumber());
        map.put("rows", list);
        return map;
    }

    /**
     * 商务收款列表 查看
     */
    @RequestMapping("/receipts/viewBusinessReceipts")
    @ResponseBody
    public Map<String, Object> viewBusinessReceipts(SwskglDto swskglDto) {
        Map<String, Object> map = new HashMap<>();
        SwskglDto swskglDto_t = swskglService.getDto(swskglDto);
        map.put("swskglDto", swskglDto_t);
        return map;
    }

    /**
     * 商务收款列表 删除
     */
    @RequestMapping("/receipts/delBusinessReceipts")
    @ResponseBody
    public Map<String, Object> delBusinessReceipts(SwskglDto swskglDto) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo();
        swskglDto.setScry(user.getYhid());
        boolean isSuccess = swskglService.delBusinessReceipts(swskglDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }
}
