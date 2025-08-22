package com.matridx.localization.controller;

import com.matridx.localization.service.svcinterface.IHenuoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : 郭祥杰
 * @date :
 */
@Controller
@RequestMapping("/ws")
public class LocalizationWSController {
    @Autowired
    private IHenuoService henuoService;
    @RequestMapping("/localization/getHospitalName")
    @ResponseBody
    public String getHospitalName(HttpServletRequest request){
        return henuoService.getHospitalName(request.getParameter("barcode"));
    }
}
