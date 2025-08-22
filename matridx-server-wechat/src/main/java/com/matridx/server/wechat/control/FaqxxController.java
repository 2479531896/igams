package com.matridx.server.wechat.control;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.server.wechat.dao.entities.FaqxxDto;
import com.matridx.server.wechat.service.svcinterface.IFaqxxService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.dao.entities.User;


@Controller
public class FaqxxController extends BaseController{

    @Autowired
    IJcsjService jcsjService;
    @Autowired
    private IFaqxxService faqxxService;
    @Autowired
    private IXxglService xxglService;
    @Autowired
    ICommonService commonService;

    /**
     * 跳转faqxx列表
     *
     * @return
     */
    @RequestMapping(value = "/faq/pageListFaqxx")
    public ModelAndView getFaqxxPage() {
        return new ModelAndView("wechat/faq/faqxx_list");
    }



    /**
     * 获取机构信息列表
     * @param faqxxDto
     * @return
     */

    @RequestMapping("/faq/getFaqxxList")
    @ResponseBody
    public Map<String,Object> getFaqxxList(FaqxxDto faqxxDto){
        Map<String,Object> map = new HashMap<>();
        List<FaqxxDto> list = faqxxService.getPagedDtoList(faqxxDto);
        map.put("total", faqxxDto.getTotalNumber());
        map.put("rows",  list);
        return map;
    }


    /**
     * 跳转新增页面
     * @param faqxxDto
     * @return
     */
    @RequestMapping("/faq/addFaqxx")
    public ModelAndView getaddFaqxxPage(FaqxxDto faqxxDto) {
        ModelAndView mav = new ModelAndView("wechat/faq/faqxx_add");
       faqxxDto.setFormAction("add");
        mav.addObject("faqxxDto", faqxxDto);
        return mav;
    }


    /**
     * 新增保存信息
     * @param faqxxDto
     * @return
     */

    @RequestMapping("/faq/addSaveFaqxx")
    @ResponseBody
    public Map<String,Object> addSaveFaqxx(FaqxxDto faqxxDto){
        User user=getLoginInfo();
        faqxxDto.setLrry(user.getYhid());
        Map<String,Object> map= new HashMap<>();
        boolean isSuccess=faqxxService.insertDto(faqxxDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }






    /**
     * 修改页面
     * @param faqxxDto
     * @return
     */
    @RequestMapping("/faq/modFaqxx")
    public ModelAndView getmodFaqxxPage(FaqxxDto faqxxDto) {
        ModelAndView mav = new ModelAndView("wechat/faq/faqxx_add");
        faqxxDto=faqxxService.getDto(faqxxDto);
        Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.NEWS_CLASS});
        faqxxDto.setFormAction("mod");
        mav.addObject("zxlxlist", jclist.get(BasicDataTypeEnum.NEWS_CLASS.getCode()));
        mav.addObject("faqxxDto", faqxxDto);
        return mav;
    }




    /**
     * 修改保存
     * @param faqxxDto
     * @return
     */
    @RequestMapping("/faq/modSaveFaqxx")
    @ResponseBody
    public Map<String,Object> modSaveFaqxx(FaqxxDto faqxxDto){
        User user=getLoginInfo();
        faqxxDto.setXgry(user.getYhid());
        Map<String,Object> map= new HashMap<>();
        System.out.println(faqxxDto);
        boolean isSuccess=faqxxService.update(faqxxDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }


    /**
     * 删除
     * @param faqxxDto
     * @return
     */
    @RequestMapping("/news/delFaqxx")
    @ResponseBody
    public Map<String,Object> delFaqxx(FaqxxDto faqxxDto){
        User user=getLoginInfo();
        faqxxDto.setScry(user.getYhid());
        Map<String,Object> map= new HashMap<>();
        boolean isSuccess=faqxxService.delete(faqxxDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }


    /**
     * 查看页面
     * @param faqxxDto
     * @return
     */
    @RequestMapping("/faq/viewFaqxx")
    @ResponseBody
    public ModelAndView viewFaqxx(FaqxxDto faqxxDto) throws UnsupportedEncodingException {
        ModelAndView mav=new ModelAndView("wechat/faq/faq_view");
        faqxxDto=faqxxService.getDtoById(faqxxDto.getFaqid());
        String bt=faqxxDto.getBt();
        String nr=faqxxDto.getNr();
        if(StringUtils.isNotBlank(bt)) {
            bt= URLEncoder.encode(commonService.getSign(faqxxDto.getBt()),"UTF-8");
        }
        if(StringUtils.isNotBlank(nr)) {
            nr=URLEncoder.encode(commonService.getSign(faqxxDto.getNr()),"UTF-8");
        }
        faqxxDto.setWz("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx0878828416fa84fe&redirect_uri=https%3A%2F%2Fservice.matridx.com%2Fws%2Fnews%2FgetWechatNews&zxlx="+bt+"&zxzlx="+nr+"&response_type=code&scope=snsapi_base&state=STATE＃wechat_redirect");
        mav.addObject("faqxxDto",faqxxDto);
        return mav;
    }





    /**
     * 删除信息
     * @param faqxxDto
     * @return
     */
    @RequestMapping(value="/faq/delFaq")
    @ResponseBody
    public Map<String,Object> delJgxx(FaqxxDto faqxxDto){
        User user=getLoginInfo();
        faqxxDto.setScry(user.getYhid());
        boolean isSuccess=faqxxService.delete(faqxxDto);
        Map<String, Object> map= new HashMap<>();
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? (xxglService.getModelById("ICOM00003").getXxnr()) : xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }


}
