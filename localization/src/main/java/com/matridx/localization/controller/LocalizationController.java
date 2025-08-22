package com.matridx.localization.controller;

import com.matridx.localization.service.svcinterface.IConvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * LocalizationController
 *
 * @author hmz
 * @version 1.0
 * @date 2024/7/18 上午9:04
 */

@Controller
@RequestMapping("/ws")
public class LocalizationController {

    @Autowired
    IConvertService convertService;
    @RequestMapping("/localization/receiveData")
    @ResponseBody
    public Map<String,Object> receiveData(HttpServletRequest request){
        String flag = request.getParameter("flag");
        String json = request.getParameter("json");
        String dwmc = request.getParameter("dwmc");
        String filePath = request.getParameter("filePath");
        String lineSplit = request.getParameter("lineSplit");
        String db = request.getParameter("db");
        //测试用接口
        Map<String,Object> result = convertService.convertJsonAndSendToServer(flag, db, dwmc, json, filePath, lineSplit);
        return result;
    }
}
