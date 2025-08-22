package com.matridx.igams.common.controller;

import com.matridx.igams.common.dao.entities.QxModel;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.post.ICommonDao;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/common")
public class CommonHandlerController extends BaseController{

    @Autowired
    ICommonService commonService;

    @Autowired
    ICommonDao commonDao;

    private final Logger log = LoggerFactory.getLogger(CommonHandlerController.class);

    @RequestMapping("/common/checkRseourcePower")
    @ResponseBody
    public boolean checkCzqxExt(HttpServletRequest request){
        String url=request.getParameter("url");
        String urlPrefix=request.getParameter("urlPrefix");
        User user=getLoginInfo();
        //log.error("--------进入拦截----------路径---"+url+" urlprefix-----"+urlPrefix + " yhm-----"+user.getYhm());
        return commonService.checkCzqxExt(url,user,urlPrefix);
    }

    @RequestMapping(value="/common/getBtnsByMenuId")
    @ResponseBody
    public List<QxModel> getBtnsByMenuId(HttpServletRequest request){

        String zyid = request.getParameter("zyid");

        if(StringUtil.isBlank(zyid))
            return null;

        User user = getLoginInfo();

        List<QxModel> qxDtos = user.getQxModels();

        List<QxModel> now_jsczDtos = new ArrayList<>();
        for (QxModel qxModel : qxDtos) {
            if (qxModel.getZyid().equals(zyid)) {
                now_jsczDtos.add(qxModel);
            }
        }

        return now_jsczDtos;
    }

    /**
     * 获取登录用户信息
     */
    @RequestMapping(value ="/common/getLoginUserInfo")
    @ResponseBody
    public User getLoginUserInfo(){
        return getLoginInfo();
    }

    /**
     * 根据用户Id查询用户信息
     */
    @RequestMapping(value ="/common/getUserInfo")
    @ResponseBody
    public Map<String,Object> getUserInfo(HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        String yhid = request.getParameter("yhid");
        if(StringUtil.isBlank(yhid)){
            result.put("status", "fail");
            log.error("userIdsList 未获取到用户Id信息！");
            return result;
        }
        User user = new User();
        user.setYhid(yhid);
        User t_user = commonDao.getUserInfoById(user);
        result.put("user", t_user);
        return result;
    }

}
