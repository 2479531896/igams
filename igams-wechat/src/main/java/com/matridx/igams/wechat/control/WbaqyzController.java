package com.matridx.igams.wechat.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.WbaqyzDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IWbaqyzService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.wechat.dao.entities.SjhbxxDto;
import com.matridx.igams.wechat.dao.entities.WbhbyzDto;
import com.matridx.igams.wechat.service.svcinterface.ISjhbxxService;
import com.matridx.igams.wechat.service.svcinterface.IWbhbyzService;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/wbaqyz")
public class WbaqyzController extends BaseController {
    @Autowired
    private IWbaqyzService wbaqyzService;
    @Autowired
    private IXxglService xxglservice;
    @Autowired
    IXxglService xxglService;
    @Autowired
    ISjhbxxService sjhbxxService;
    @Autowired
    IWbhbyzService wbhbyzService;
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    ICommonService  commonService;

    /**
     * 拜访对象列表
     * @param wbaqyzDto
     * @return
     */
    @RequestMapping("/wbaqyz/pageListWbaqyz")
    public ModelAndView pageListWbaqyzDto(WbaqyzDto wbaqyzDto) {
        return new ModelAndView("wechat/wbaqyz/wbaqyz_list");
    }

    /**
     * 获取列表数据
     */
    @RequestMapping("/wbaqyz/pagedataWbaqyz")
    @ResponseBody
    public Map<String, Object> listWbaqyz(WbaqyzDto wbaqyzDto) {
        Map<String, Object> map = new HashMap<>();
        DBEncrypt dBEncrypt = new DBEncrypt();
        List<WbaqyzDto> wbaqyzlist = wbaqyzService.getPageWbaqyzDtoList(wbaqyzDto);
        if(null != wbaqyzlist && wbaqyzlist.size()>0){
            for (int i=0;i<wbaqyzlist.size();i++){
                wbaqyzlist.get(i).setWord(dBEncrypt.dCode(wbaqyzlist.get(i).getWord()));
                wbaqyzlist.get(i).setKey(dBEncrypt.dCode(wbaqyzlist.get(i).getKey()));
            }
        }
        map.put("total",wbaqyzDto.getTotalNumber());
        map.put("rows",wbaqyzlist);
        return map;
    }
    /**
     * 查看信息对应详情
     */
    @RequestMapping("/wbaqyz/viewWbaqyz")
    public ModelAndView viewWbaqyzList(WbaqyzDto wbaqyzDto){
        ModelAndView mav=new ModelAndView("wechat/wbaqyz/wbaqyz_view");
        WbaqyzDto wbaqyz_t=wbaqyzService.getDtoById(wbaqyzDto.getCode());
        WbhbyzDto wbhbyzDto = new WbhbyzDto();
        wbhbyzDto.setCode(wbaqyzDto.getCode());
        List<WbhbyzDto> wbhbyzDtos = wbhbyzService.getListByCode(wbhbyzDto);
        mav.addObject("wbhbyzDtos",wbhbyzDtos);
        DBEncrypt dBEncrypt = new DBEncrypt();
        wbaqyz_t.setWord(dBEncrypt.dCode(wbaqyz_t.getWord()));
        wbaqyz_t.setKey(dBEncrypt.dCode(wbaqyz_t.getKey()));
        mav.addObject("wbaqyz", wbaqyz_t);
        return mav;
    }

    /**
     * 增加数据
     *
     */
    @RequestMapping("/wbaqyz/addWbaqyz")
    public ModelAndView addWbaqyzList(WbaqyzDto wbaqyzDto){
        ModelAndView mav=new ModelAndView("wechat/wbaqyz/wbaqyz_add");
        wbaqyzDto.setFormAction("addSaveWbaqyz");
        mav.addObject("wbaqyzDto", wbaqyzDto);
        return mav;
    }
    @RequestMapping("/code")
    @ResponseBody
    public  Map<String, Object> judge(HttpServletRequest request, WbaqyzDto wbaqyzDto) {
        Map<String, Object> map= new HashMap<>();
        boolean isSuccess=false;
        WbaqyzDto wbaqyzDto_t=wbaqyzService.getDto(wbaqyzDto);
        if (wbaqyzDto_t==null)
            isSuccess=true;
        map.put("status",isSuccess?"success":"fail");
        map.put("message",isSuccess? xxglservice.getModelById("ICOM00001").getXxnr():"消息已经存在，请核实后添加！");
        return map;
    }

    /**
     * 增加信息到数据库
     * @param
     * @return
     */
    @RequestMapping("/wbaqyz/addSaveWbaqyz")
    @ResponseBody
    public Map<String, Object> InsertWbaqyz(WbaqyzDto wbaqyzDto){
        Map<String, Object> map= new HashMap<>();
        User user = getLoginInfo();
        wbaqyzDto.setLrry(user.getYhid());
        DBEncrypt dBEncrypt = new DBEncrypt();
        wbaqyzDto.setKey(dBEncrypt.eCode(wbaqyzDto.getKey()));
        wbaqyzDto.setWord(dBEncrypt.eCode(wbaqyzDto.getWord()));
        boolean isSuccess=wbaqyzService.insertDto(wbaqyzDto);
        commonService.insertXtyh(wbaqyzDto.getCode(),wbaqyzDto.getKey(),wbaqyzDto.getMc());
        //更新redis里se外部安全验证信息
        redisUtil.set("Wbaqyz:" + wbaqyzDto.getCode(), JSONObject.toJSONString(wbaqyzDto), -1);
        map.put("status",isSuccess?"success":"fail");
        map.put("message",isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 转到正常的spring Security 认证
     *
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    private Map<String, String> turnToSpringSecurityLogin(HttpServletRequest request) {
        Map<String, String> returnMap = null;
        try {
            String username = request.getParameter("code");
            String password = request.getParameter("word");
            List<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair("client_id", username));
            Base64 base64 = new Base64();
            String enPass = base64.encodeToString(password.getBytes());
            pairs.add(new BasicNameValuePair("client_secret", enPass));
            pairs.add(new BasicNameValuePair("grant_type", "matridx"));
            CloseableHttpClient httpclient = null;
            CloseableHttpResponse httpresponse = null;
            try {
                httpclient = HttpClients.createDefault();
                HttpPost httppost = new HttpPost( "http://172.17.53.87:8086/oauth/token");
                // StringEntity stringentity = new StringEntity(data);
                if (pairs != null && pairs.size() > 0) {
                    httppost.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
                }
                httpresponse = httpclient.execute(httppost);
                String response = EntityUtils.toString(httpresponse.getEntity());

                returnMap = JSONObject.parseObject(response, Map.class);
                if (returnMap.get("expires_in") != null) {
                    String ex_in = String.valueOf(returnMap.get("expires_in"));
                    returnMap.remove("expires_in");
                    returnMap.put("expires_in", ex_in);
                }
            } finally {
                if (httpclient != null) {
                    httpclient.close();
                }
                if (httpresponse != null) {
                    httpresponse.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnMap;
    }
    /**
     * 修改时新增一个页面
     * @param
     * @return
     */
    @RequestMapping("/wbaqyz/modWbaqyz")
    public ModelAndView modWbaqyzList(WbaqyzDto wbaqyzDto){
        ModelAndView mav=new ModelAndView("wechat/wbaqyz/wbaqyz_add");
        DBEncrypt dBEncrypt = new DBEncrypt();
        WbaqyzDto wbaqyzDto_t = wbaqyzService.getDtoById(wbaqyzDto.getCode());
        wbaqyzDto_t.setKey(dBEncrypt.dCode(wbaqyzDto_t.getKey()));
        wbaqyzDto_t.setWord(dBEncrypt.dCode(wbaqyzDto_t.getWord()));
        wbaqyzDto_t.setFormAction("modSaveWbaqyz");
        mav.addObject("wbaqyzDto", wbaqyzDto_t);
        return mav;
    }
    /**
     * 修改消息到数据库
     */
    @RequestMapping("/wbaqyz/modSaveWbaqyz")
    @ResponseBody
    public Map<String, Object> updateWbaqyz(WbaqyzDto wbaqyzDto) {
        Map<String, Object> map = new HashMap<>();
        boolean isSuccess=wbhbyzService.modInfo(wbaqyzDto);

        //更新redis里se外部安全验证信息
        redisUtil.set("Wbaqyz:" + wbaqyzDto.getCode(), JSONObject.toJSONString(wbaqyzDto), -1);
        map.put("status",isSuccess?"success":"fail");
        map.put("message",isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 删除信息
     * @param
     * @return
     */
    @RequestMapping(value="/wbaqyz/delWbaqyz")
    @ResponseBody
    public Map<String,Object> delWbaqyz(WbaqyzDto wbaqyzDto){
        Map<String,Object> map = new HashMap<>();
        boolean isSuccess=false;
        try {
            isSuccess = wbhbyzService.delInfo(wbaqyzDto);

            //更新redis里se外部安全验证信息
            redisUtil.set("Wbaqyz:" + wbaqyzDto.getCode(), null, 2);
        } catch (BusinessException e) {
            map.put("status","fail");
            map.put("message", e.getMsg());
        }
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?xxglservice.getModelById("ICOM00003").getXxnr():xxglservice.getModelById("ICOM00004").getXxnr());
        return map;
    }

    /**
     * 关联伙伴功能跳转页面
     * @return
     */
    @RequestMapping("/wbaqyz/pageListPartnerlist")
    public ModelAndView stockPendingPageList(SjhbxxDto sjhbxxDto) {
        ModelAndView mav = new  ModelAndView("wechat/wbaqyz/wbaqyz_partner");
        WbhbyzDto wbhbyzDto = new WbhbyzDto();
        wbhbyzDto.setCode(sjhbxxDto.getCode());
        List<WbhbyzDto> wbhbyzDtos = wbhbyzService.getListByCode(wbhbyzDto);
        sjhbxxDto.setFormAction("pagedataAddSavewbaqyzpartner");
        mav.addObject("wbaqyzlist",JSONObject.toJSONString(wbhbyzDtos));
        mav.addObject("code",sjhbxxDto.getCode());
        List<JcsjDto> classifylist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.CLASSIFY.getCode());
        List<JcsjDto> subclassificationlist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.SUBCLASSIFICATION.getCode());
        List<JcsjDto> templatelist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.REPORT_TEMEPLATE.getCode());

        mav.addObject("classifylist",classifylist);//合作伙伴分类
        mav.addObject("subclassificationlist",subclassificationlist);//合作伙伴子分类
        mav.addObject("templatelist",templatelist);//报告模板
        mav.addObject("wbaqyzDto",sjhbxxDto);
        return mav;
    }
    /**
     * 关联伙伴功能保存
     * @return
     */
    @RequestMapping("/wbaqyz/pagedataAddSavewbaqyzpartner")
    @ResponseBody
    public Map<String, Object> addSavewbaqyzpartner(SjhbxxDto sjhbxxDto) {
        Map<String, Object> map = new HashMap<>();
        boolean isSuccess =true;
        try {
            isSuccess = wbhbyzService.addSavewbaqyzpartner(sjhbxxDto);
        } catch (BusinessException e) {
            map.put("status","fail");
            map.put("message", e.getMsg());
        }
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }
    /**
     * 伙伴列表
     * @param
     * @return
     */
    @RequestMapping("/wbaqyz/pagedataPartner")
    @ResponseBody
    public Map<String, Object> getPagedDtoList(SjhbxxDto sjhbxxDto){
        List<SjhbxxDto> sjhblist=sjhbxxService.getPagedDtoList(sjhbxxDto);
        Map<String, Object> map= new HashMap<>();
        map.put("total", sjhbxxDto.getTotalNumber());
        map.put("rows",  sjhblist);
        return map;
    }
}
