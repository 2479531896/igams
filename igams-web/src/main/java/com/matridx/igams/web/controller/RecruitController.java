package com.matridx.igams.web.controller;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.DepartmentDto;
import com.matridx.igams.common.dao.entities.JgxxDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.post.ICommonDao;
import com.matridx.igams.common.service.svcinterface.IJgxxService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.web.dao.entities.RslyDto;
import com.matridx.igams.web.dao.entities.RszpDto;
import com.matridx.igams.web.dao.entities.YhssjgDto;
import com.matridx.igams.web.service.svcinterface.IRslyService;
import com.matridx.igams.web.service.svcinterface.IRszpService;
import com.matridx.igams.web.service.svcinterface.IYhssjgService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/recruit")
public class RecruitController extends BaseController {
    @Autowired
    IRszpService rszpService;
    @Autowired
    IXxglService xxglService;
    @Autowired
    ICommonDao commonDao;
    @Autowired
    IYhssjgService yhssjgService;
    @Autowired
    IJgxxService jgxxService;
    @Autowired
    IRslyService rslyService;
    /**
     * 人事招聘列表
     * @return
     */
    @RequestMapping(value="/recruit/pageListRecruitments")
    public ModelAndView getRecruitmentsPageList() {
        return new ModelAndView("globalweb/systemrole/recruit_list");
    }

    /**
     * 列表内容
     * @param rszpDto
     * @return
     */
    @RequestMapping(value="/recruit/pageGetListRecruitments")
    @ResponseBody
    public Map<String,Object> getRecruitmentsList(RszpDto rszpDto){
        List<RszpDto> rszplist=rszpService.getPagedDtoList(rszpDto);
        Map<String,Object> result = new HashMap<>();
        result.put("total", rszpDto.getTotalNumber());
        result.put("rows", rszplist);
        return result;
    }

    /**
     * 查看按钮
     * @return
     */
    @RequestMapping(value="/recruit/viewRecruitment")
    public ModelAndView viewRecruitment(RszpDto rszpDto){
        ModelAndView mav=new ModelAndView("globalweb/systemrole/recruit_view");
        RszpDto dtoByZpid = rszpService.getDtoByZpid(rszpDto);
        mav.addObject("rszpDto",dtoByZpid);
        return mav;
    }
    /**
     * 删除按钮
     * @param rszpDto
     * @return
     */
    @RequestMapping(value="/recruit/delRecruitment")
    @ResponseBody
    public Map<String,Object> delRecruitment(RszpDto rszpDto){
        Map<String,Object> map = new HashMap<>();
        boolean isSuccess = rszpService.delRecruitment(rszpDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }
    /**
     * 新增按钮
     * @return
     */
    @RequestMapping(value="/recruit/addRecruitment")
    public ModelAndView addRecruitment(RszpDto rszpDto){
        ModelAndView mav=new ModelAndView("globalweb/systemrole/recruit_edit");
        List<User> xtyhlist=commonDao.getUserList();
        List<DepartmentDto> jgxxList = commonDao.getJgxxList();
        rszpDto.setFormAction("add");
        mav.addObject("xtyhlist", xtyhlist);
        mav.addObject("jgxxList", jgxxList);
        mav.addObject("rszpDto", rszpDto);
        return mav;
    }

    /**
     * 新增保存
     * @param rszpDto
     * @return
     */
    @RequestMapping(value="/recruit/addSaveRecruitment")
    @ResponseBody
    public Map<String,Object> addRecruitSave(RszpDto rszpDto){
        Map<String,Object> map = new HashMap<>();
        rszpDto.setFqrbmmc(rszpDto.getFqrbm());
        JgxxDto jgxxDto=new JgxxDto();
        jgxxDto.setJgmc(rszpDto.getFqrbm());
        rszpDto.setFqrbm(jgxxService.getJgxxByJgmc(jgxxDto).getJgid());
        RszpDto rszpDto_t = rszpService.getDtoByZpid(rszpDto);
        if(rszpDto_t!=null){
            map.put("status","fail");
            map.put("message","此招聘信息已经存在，不能重复新增");
        }else{
            rszpDto.setZpid(StringUtil.generateUUID());
            boolean isSuccess = rszpService.insert(rszpDto);
            map.put("status", isSuccess?"success":"fail");
            map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }

    /**
     * 根据用户ID获取机构信息
     * @param rszpDto
     * @return
     */
    @RequestMapping(value="/recruit/pagedataJgxx")
    @ResponseBody
    public Map<String,Object> getJgxx(RszpDto rszpDto){
        Map<String,Object> map = new HashMap<>();
        List<YhssjgDto> jgxxByYhid = yhssjgService.getJgxxByYhid(rszpDto.getFqr());
        map.put("rszpDto",jgxxByYhid);
        return map;
    }

    /**
     * 修改按钮
     * @return
     */
    @RequestMapping(value="/recruit/modRecruitment")
    public ModelAndView modRecruitment(RszpDto rszpDto){
        ModelAndView mav=new ModelAndView("globalweb/systemrole/recruit_edit");
        RszpDto dtoByZpid = rszpService.getDtoByZpid(rszpDto);
        dtoByZpid.setFormAction("mod");
        mav.addObject("rszpDto", dtoByZpid);
        return mav;
    }

    /**
     * 修改保存
     * @param rszpDto
     * @return
     */
    @RequestMapping(value="/recruit/modSaveRecruitment")
    @ResponseBody
    public Map<String,Object> modRecruitSave(RszpDto rszpDto){
        Map<String,Object> map = new HashMap<>();
        boolean isSuccess = rszpService.update(rszpDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }
    /**
     * 点击已录用人数展示页面
     * @return
     */
    @RequestMapping(value="/recruit/viewEmploy")
    public ModelAndView viewLeave(String zpid) {
        ModelAndView mav=new ModelAndView("globalweb/systemrole/recruit_details");
        RslyDto rslyDto=new RslyDto();
        rslyDto.setZpid(zpid);
        List<RslyDto> dtos = rslyService.viewEmployDetails(rslyDto);
        mav.addObject("dtos",dtos);
        return mav;
    }

}
