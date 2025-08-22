package com.matridx.igams.common.controller;

import com.matridx.igams.common.dao.entities.Role;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.HabitsTypeEnum;
import com.matridx.igams.common.service.svcinterface.ICommonService;
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
@Controller
@RequestMapping("/role")
public class RoleController extends BaseController{
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Autowired
    private ICommonService commonService;

    /**
     * 角色列表
     */
    @RequestMapping("/role/pagedataRoleList")
    public ModelAndView pagedataRoleList() {
        ModelAndView mav =new ModelAndView("common/role/role_list");
        mav.addObject("urlPrefix",urlPrefix);
        return mav;
    }

    /**
     * 角色列表
     */
    @RequestMapping(value ="/role/pagedataGetRoleList")
    @ResponseBody
    public Map<String,Object> pagedataGetRoleList(Role role){
        User user = getLoginInfo();
        role.setYhid(user.getYhid());
        role.setQf(HabitsTypeEnum.ROLE_HABITS.getCode());
        List<Role> list=commonService.getPagedRoleList(role);
        Map<String, Object> map= new HashMap<>();
        map.put("total", role.getTotalNumber());
        map.put("rows", list);
        return map;
    }
}
