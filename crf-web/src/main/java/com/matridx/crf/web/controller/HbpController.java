package com.matridx.crf.web.controller;

import com.matridx.crf.web.dao.entities.*;
import com.matridx.crf.web.service.svcinterface.IHbpdrxxjlService;
import com.matridx.crf.web.service.svcinterface.IHbphzxxService;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/hbp")
public class HbpController  extends BaseController {

    @Autowired
    IHbphzxxService hbphzxxService;
    @Autowired
    IHbpdrxxjlService hbpdrxxjlService;
    @Autowired
    IXxglService xxglService;
    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping(value = "/hbp/pageListHbp")
    public ModelAndView pageListhbpHzxx() {
        ModelAndView mav = new ModelAndView("casereport/hbp/hbp_list");
        return mav;
    }

    @RequestMapping(value = "/hbp/listHbpHzxx")
    @ResponseBody
    public Map<String, Object> listHbpHzxx(HbphzxxDto hbphzxxDto) {
        Map<String,Object> map=new HashMap<String, Object>();
        List<HbphzxxDto> hbphzxxList = hbphzxxService.getPagedDtoList(hbphzxxDto);
        map.put("total",hbphzxxDto.getTotalNumber());
        map.put("rows",hbphzxxList);
        return map;

    }

    @RequestMapping(value = "/hbp/viewHbpHzxx")
    public ModelAndView viewHbpHzxx(HbphzxxDto hbphzxxDto) {
        ModelAndView mav = new ModelAndView("casereport/hbp/hbp_view");
        HbphzxxDto dtoById = hbphzxxService.getDto(hbphzxxDto);
        HbpdrxxjlDto hbpdrxxjlDto=new HbpdrxxjlDto();
        hbpdrxxjlDto.setHbphzxxid(hbphzxxDto.getHbphzxxid());
        HbpdrxxjlDto hbpdrxxjlDtoOne=new HbpdrxxjlDto();
        HbpdrxxjlDto hbpdrxxjlDtoTwo=new HbpdrxxjlDto();
        HbpdrxxjlDto hbpdrxxjlDtoThree=new HbpdrxxjlDto();
        HbpdrxxjlDto hbpdrxxjlDtoFour=new HbpdrxxjlDto();
        HbpdrxxjlDto hbpdrxxjlDtoFive=new HbpdrxxjlDto();
        List<HbpdrxxjlDto> list = hbpdrxxjlService.getDtoList(hbpdrxxjlDto);
        for(HbpdrxxjlDto dto:list){
            if("1".equals(dto.getJldjt())){
                hbpdrxxjlDtoOne=dto;
            }else if("2".equals(dto.getJldjt())){
                hbpdrxxjlDtoTwo=dto;
            }else if("3".equals(dto.getJldjt())){
                hbpdrxxjlDtoThree=dto;
            }else if("4".equals(dto.getJldjt())){
                hbpdrxxjlDtoFour=dto;
            }else if("5".equals(dto.getJldjt())){
                hbpdrxxjlDtoFive=dto;
            }
        }

        String hxyOne = "", hxyTwo="", hxyThree="", jwbsStr="", grbwStr="", qzbbOne="", qzbbTwo="";
        if (hbpdrxxjlDtoOne.getXghxyw() != null && hbpdrxxjlDtoOne.getXghxyw().length() > 0) {
            List<String> one_hxylist = Arrays.asList(hbpdrxxjlDtoOne.getXghxyw().split(","));
            for (String str : one_hxylist ) {
                JcsjDto jcsjDto = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.HBPXGHXYW, str);
                hxyOne += ", "+jcsjDto.getCsmc();
            }
            if (StringUtil.isNotBlank(hxyOne)){
                hbpdrxxjlDtoOne.setXghxyw(hxyOne.substring(1));
            }
        }
        if (hbpdrxxjlDtoTwo.getXghxyw() != null && hbpdrxxjlDtoTwo.getXghxyw().length() > 0) {
            List<String> two_hxylist = Arrays.asList(hbpdrxxjlDtoTwo.getXghxyw().split(","));
            for (String str : two_hxylist ) {
                JcsjDto jcsjDto = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.HBPXGHXYW, str);
                hxyTwo += ", "+jcsjDto.getCsmc();
            }
            if (StringUtil.isNotBlank(hxyTwo)){
                hbpdrxxjlDtoTwo.setXghxyw(hxyTwo.substring(1));
            }
        }
        if (hbpdrxxjlDtoThree.getXghxyw() != null && hbpdrxxjlDtoThree.getXghxyw().length() > 0) {
            List<String> three_hxylist = Arrays.asList(hbpdrxxjlDtoThree.getXghxyw().split(","));
            for (String str : three_hxylist ) {
                JcsjDto jcsjDto = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.HBPXGHXYW, str);
                hxyThree += ", "+jcsjDto.getCsmc();
            }
            if (StringUtil.isNotBlank(hxyThree)){
                hbpdrxxjlDtoThree.setXghxyw(hxyThree.substring(1));
            }
        }
        if (dtoById.getJwbs() != null && dtoById.getJwbs().length() > 0) {
            List<String> t_jwbslist = Arrays.asList(dtoById.getJwbs().split(","));
            for (String str : t_jwbslist ) {
                JcsjDto jcsjDto = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.HBPJWBS, str);
                jwbsStr += ", "+jcsjDto.getCsmc();
            }
            if (StringUtil.isNotBlank(jwbsStr)){
                dtoById.setJwbs(jwbsStr.substring(1));
            }
        }
        if (dtoById.getGrbw() != null && dtoById.getGrbw().length() > 0) {
            List<String> t_grbwlist = Arrays.asList( dtoById.getGrbw().split(",") );
            for (String str : t_grbwlist ) {
                JcsjDto jcsjDto = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.HBPGRBW, str);
                grbwStr += ", "+jcsjDto.getCsmc();
            }
            if (StringUtil.isNotBlank(grbwStr)){
                dtoById.setGrbw(grbwStr.substring(1));
            }
        }
        if (dtoById.getQzbb1() != null && dtoById.getQzbb1().length() > 0) {
            List<String> one_qzlist = Arrays.asList( dtoById.getQzbb1().split(",") );
            for (String str : one_qzlist ) {
                JcsjDto jcsjDto = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.HBPQZBB, str);
                qzbbOne += ", "+jcsjDto.getCsmc();
            }
            if (StringUtil.isNotBlank(qzbbOne)){
                dtoById.setQzbb1(qzbbOne.substring(1));
            }
        }
        if (dtoById.getQzbb2() != null && dtoById.getQzbb2().length() > 0) {
            List<String> two_qzlist = Arrays.asList( dtoById.getQzbb2().split(",") );
            for (String str : two_qzlist ) {
                JcsjDto jcsjDto = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.HBPQZBB, str);
                qzbbTwo += ", "+jcsjDto.getCsmc();
            }
            if (StringUtil.isNotBlank(qzbbTwo)){
                dtoById.setQzbb2(qzbbTwo.substring(1));
            }
        }
        mav.addObject("hbpdrxxjlDtoOne", hbpdrxxjlDtoOne);
        mav.addObject("hbpdrxxjlDtoTwo", hbpdrxxjlDtoTwo);
        mav.addObject("hbpdrxxjlDtoThree", hbpdrxxjlDtoThree);
        mav.addObject("hbpdrxxjlDtoFour", hbpdrxxjlDtoFour);
        mav.addObject("hbpdrxxjlDtoFive", hbpdrxxjlDtoFive);
        mav.addObject("hbphzxxDto", dtoById);
        List<JcsjDto> hxylist = redisUtil.lgetDto(("List_matridx_jcsj:"+BasicDataTypeEnum.HBPXGHXYW));
        List<JcsjDto> qzlist = redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.HBPQZBB);
        List<JcsjDto> grbwlist = redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.HBPGRBW);
        List<JcsjDto> jwbslist = redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.HBPJWBS);
        mav.addObject("hxylist", hxylist);
        mav.addObject("qzlist", qzlist);
        mav.addObject("grbwlist", grbwlist);
        mav.addObject("jwbslist", jwbslist);
        return mav;
    }

    @RequestMapping("/hbp/addHbpHzxx")
    @ResponseBody
    public ModelAndView addHbpHzxx(HbphzxxDto hbphzxxDto){
        ModelAndView mav=new ModelAndView("casereport/hbp/hbp_edit");
        hbphzxxDto.setFormAction("insertHbpHzxx");
        List<JcsjDto> hxylist = redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.HBPXGHXYW);
        List<JcsjDto> qzlist = redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.HBPQZBB);
        List<JcsjDto> grbwlist = redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.HBPGRBW);
        List<JcsjDto> jwbslist = redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.HBPJWBS);
        mav.addObject("hbpdrxxjlDtoOne", new HbpdrxxjlDto());
        mav.addObject("hbpdrxxjlDtoTwo", new HbpdrxxjlDto());
        mav.addObject("hbpdrxxjlDtoThree", new HbpdrxxjlDto());
        mav.addObject("hbpdrxxjlDtoFour", new HbpdrxxjlDto());
        mav.addObject("hbpdrxxjlDtoFive", new HbpdrxxjlDto());
        mav.addObject("hbphzxxDto", hbphzxxDto);

        mav.addObject("hxylist", hxylist);
        mav.addObject("qzlist", qzlist);
        mav.addObject("grbwlist", grbwlist);
        mav.addObject("jwbslist", jwbslist);
        return mav;
    }

    @RequestMapping("/hbp/insertHbpHzxx")
    @ResponseBody
    public Map<String, Object> insertHbpHzxx(HbphzxxDto hbphzxxDto, BeanHbpxxForms beanHbpxxForms){
        Map<String, Object> map=new HashMap<String, Object>();
        hbphzxxDto.setLrry(getLoginInfo().getYhid());
        boolean isSuccess=hbphzxxService.insertHbpHzxx(hbphzxxDto,beanHbpxxForms);
        map.put("status",isSuccess?"success":"fail");
        map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    @RequestMapping("/hbp/modHbpHzxx")
    @ResponseBody
    public ModelAndView modHbpHzxx(HbphzxxDto hbphzxxDto){
        ModelAndView mav=new ModelAndView("casereport/hbp/hbp_edit");
        HbpdrxxjlDto hbpdrxxjl = new HbpdrxxjlDto();
        hbpdrxxjl.setHbphzxxid(hbphzxxDto.getHbphzxxid());
        HbpdrxxjlDto hbpdrxxjlDtoOne = new HbpdrxxjlDto();
        HbpdrxxjlDto hbpdrxxjlDtoTwo = new HbpdrxxjlDto();
        HbpdrxxjlDto hbpdrxxjlDtoThree = new HbpdrxxjlDto();
        HbpdrxxjlDto hbpdrxxjlDtoFour = new HbpdrxxjlDto();
        HbpdrxxjlDto hbpdrxxjlDtoFive = new HbpdrxxjlDto();
        hbphzxxDto = hbphzxxService.getDto(hbphzxxDto);
        hbphzxxDto.setFormAction("updateHbpHzxx");
        List<HbpdrxxjlDto> xxjllist = hbpdrxxjlService.getDtoListByhzid(hbpdrxxjl);//通过患者ID查询记录信息
        if (xxjllist != null && xxjllist.size() > 0) {
            for (HbpdrxxjlDto hbpdrxxjlDto : xxjllist) {
                if (hbpdrxxjlDto != null) {
                    if (hbpdrxxjlDto.getJldjt().equals("1")) {
                        hbpdrxxjlDtoOne = hbpdrxxjlDto;
                    } else if (hbpdrxxjlDto.getJldjt().equals("2")) {
                        hbpdrxxjlDtoTwo = hbpdrxxjlDto;
                    } else if (hbpdrxxjlDto.getJldjt().equals("3")) {
                        hbpdrxxjlDtoThree = hbpdrxxjlDto;
                    } else if (hbpdrxxjlDto.getJldjt().equals("4")) {
                        hbpdrxxjlDtoFour = hbpdrxxjlDto;
                    } else if (hbpdrxxjlDto.getJldjt().equals("5")) {
                        hbpdrxxjlDtoFive = hbpdrxxjlDto;
                    }
                }
            }
        }
        mav.addObject("hbpdrxxjlDtoOne", hbpdrxxjlDtoOne);
        mav.addObject("hbpdrxxjlDtoTwo", hbpdrxxjlDtoTwo);
        mav.addObject("hbpdrxxjlDtoThree", hbpdrxxjlDtoThree);
        mav.addObject("hbpdrxxjlDtoFour", hbpdrxxjlDtoFour);
        mav.addObject("hbpdrxxjlDtoFive", hbpdrxxjlDtoFive);
        mav.addObject("hbphzxxDto", hbphzxxDto);

        List<JcsjDto> hxylist = redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.HBPXGHXYW);
        List<JcsjDto> qzlist = redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.HBPQZBB);
        List<JcsjDto> grbwlist = redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.HBPGRBW);
        List<JcsjDto> jwbslist = redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.HBPJWBS);
        mav.addObject("hxylist", hxylist);
        mav.addObject("qzlist", qzlist);
        mav.addObject("grbwlist", grbwlist);
        mav.addObject("jwbslist", jwbslist);

        if (hbpdrxxjlDtoOne.getXghxyw() != null && hbpdrxxjlDtoOne.getXghxyw().length() > 0) {
            List<String> one_hxylist = Arrays.asList(hbpdrxxjlDtoOne.getXghxyw().split(","));
            mav.addObject("one_hxylist", one_hxylist);
        }
        if (hbpdrxxjlDtoTwo.getXghxyw() != null && hbpdrxxjlDtoTwo.getXghxyw().length() > 0) {
            List<String> two_hxylist = Arrays.asList(hbpdrxxjlDtoTwo.getXghxyw().split(","));
            mav.addObject("two_hxylist", two_hxylist);
        }
        if (hbpdrxxjlDtoThree.getXghxyw() != null && hbpdrxxjlDtoThree.getXghxyw().length() > 0) {
            List<String> three_hxylist = Arrays.asList(hbpdrxxjlDtoThree.getXghxyw().split(","));
            mav.addObject("three_hxylist", three_hxylist);
        }
        if (hbphzxxDto.getJwbs() != null && hbphzxxDto.getJwbs().length() > 0) {
            List<String> t_jwbslist = Arrays.asList(hbphzxxDto.getJwbs().split(","));
            mav.addObject("t_jwbslist", t_jwbslist);
        }
        if (hbphzxxDto.getGrbw() != null && hbphzxxDto.getGrbw().length() > 0) {
            List<String> t_grbwlist = Arrays.asList( hbphzxxDto.getGrbw().split(",") );
            mav.addObject("t_grbwlist", t_grbwlist);
        }
        if (hbphzxxDto.getQzbb1() != null && hbphzxxDto.getQzbb1().length() > 0) {
            List<String> one_qzlist = Arrays.asList( hbphzxxDto.getQzbb1().split(",") );
            mav.addObject("one_qzlist", one_qzlist);
        }
        if (hbphzxxDto.getQzbb2() != null && hbphzxxDto.getQzbb2().length() > 0) {
            List<String> two_qzlist = Arrays.asList( hbphzxxDto.getQzbb2().split(",") );
            mav.addObject("two_qzlist", two_qzlist);
        }
        return mav;
    }

    @RequestMapping("/hbp/updateHbpHzxx")
    @ResponseBody
    public Map<String, Object> updateHbpHzxx(HbphzxxDto hbphzxxDto, BeanHbpxxForms beanHbpxxForms){
        Map<String, Object> map=new HashMap<String, Object>();
        hbphzxxDto.setXgry(getLoginInfo().getYhid());
        boolean isSuccess=hbphzxxService.updateHbpHzxx(hbphzxxDto,beanHbpxxForms);
        map.put("status",isSuccess?"success":"fail");
        map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    @RequestMapping("/hbp/delHbpHzxx")
    @ResponseBody
    public Map<String, Object> delHbpHzxx(HbphzxxDto hbphzxxDto, HttpServletRequest request){
        Map<String, Object> map=new HashMap<String, Object>();
        hbphzxxDto.setScry(getLoginInfo().getYhid());
        HbpdrxxjlDto hbpdrxxjlDto=new HbpdrxxjlDto();
        hbpdrxxjlDto.setIds(hbphzxxDto.getIds());
        hbpdrxxjlService.deleteByHzid(hbpdrxxjlDto);
        boolean isSuccess=hbphzxxService.delete(hbphzxxDto);
        map.put("status",isSuccess?"success":"fail");
        map.put("message",isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }

}
