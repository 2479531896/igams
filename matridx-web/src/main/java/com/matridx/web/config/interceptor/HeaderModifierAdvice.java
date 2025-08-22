package com.matridx.web.config.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.GrlbzdszDto;
import com.matridx.igams.common.dao.entities.LbzdszDto;
import com.matridx.igams.common.dao.entities.QxModel;
import com.matridx.igams.common.dao.entities.TyszmxDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.security.IgamsGrantedAuthority;
import com.matridx.igams.common.service.svcinterface.IGrlbzdszService;
import com.matridx.igams.common.service.svcinterface.ILbzdszService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class HeaderModifierAdvice implements ResponseBodyAdvice<Object> {

    @Autowired
    private ILbzdszService lbzdszService;
    @Autowired
    private IGrlbzdszService grlbzdszService;
    @Autowired
    private RedisUtil redisUtil;

    private final Logger log = LoggerFactory.getLogger(HeaderModifierAdvice.class);
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse response) {
        ServletServerHttpRequest ssReq = (ServletServerHttpRequest)request;
        ServletServerHttpResponse ssResp = (ServletServerHttpResponse)response;
        if(ssReq == null || ssResp == null
                || ssReq.getServletRequest() == null
                || ssResp.getServletResponse() == null) {
            return body;
        }

        // 对于未添加跨域消息头的响应进行处理
        HttpServletRequest req = ssReq.getServletRequest();
//        HttpServletResponse resp = ssResp.getServletResponse();
        String isvue=req.getParameter("isvue");
        String path=req.getServletPath();

//        String originHeader = "Access-Control-Allow-Origin";
//        if(!resp.containsHeader(originHeader)) {
//            String origin = req.getHeader("Origin");
//            if(origin == null) {
//                String referer = req.getHeader("Referer");
//                if(referer != null) {
//                    origin = referer.substring(0, referer.indexOf("/", 7));
//                }
//            }
//            resp.setHeader("Access-Control-Allow-Origin", origin);
//        }
//
//        String credentialHeader = "Access-Control-Allow-Credentials";
//        if(!resp.containsHeader(credentialHeader)) {
//            resp.setHeader(credentialHeader, "true");
//        }
        if((StringUtil.isNotBlank(isvue)&& "true".equals(isvue))&&(StringUtil.isNotBlank(path)&&!path.contains("pagedataGetOnlyKey"))){
            SecurityContext securityContext = SecurityContextHolder.getContext();
            if(securityContext!=null) {
                // 获取当前认证了的 principal(当事人),或者 request token (令牌)
                // 如果没有认证，会是 null
                Authentication authentication = securityContext.getAuthentication();
                if(authentication!=null) {
                    @SuppressWarnings("unchecked")
                    List<IgamsGrantedAuthority> authorities = (List<IgamsGrantedAuthority>)authentication.getAuthorities();

                    IgamsGrantedAuthority authority = authorities.get(0);

                    User user = authority.getYhxx();
                    LbzdszDto lbzdszDto = new LbzdszDto();
                    lbzdszDto.setYhid(user.getYhid());
                    lbzdszDto.setJsid(user.getDqjs());
                    lbzdszDto.setYwid(req.getParameter("zyid"));
                    List<LbzdszDto> waitList = lbzdszService.getWaitList(lbzdszDto);
                    GrlbzdszDto grlbzdszDto = new GrlbzdszDto();
                    grlbzdszDto.setYhid(user.getYhid());
                    grlbzdszDto.setYwid(req.getParameter("zyid"));
                    List<LbzdszDto> choseList = grlbzdszService.getChoseList(grlbzdszDto);
                    JSONObject jsonObject=JSONObject.parseObject(JSONObject.toJSONString(body));

                    List<QxModel> qxDtos = user.getQxModels();

                    List<QxModel> now_jsczDtos = new ArrayList<>();
                    for (QxModel qxModel : qxDtos) {
                        if (qxModel.getZyid().equals(req.getParameter("zyid"))) {
                            now_jsczDtos.add(qxModel);
                        }
                    }
                    jsonObject.put("czdmlist", now_jsczDtos);
                    try{
                        if(StringUtil.isNotBlank(req.getParameter("zyid"))){
                            Object hget = redisUtil.hget("GeneralSetting", req.getParameter("zyid"));
                            List<TyszmxDto> tyszmxDtoList = (List<TyszmxDto>) JSON.parseArray(String.valueOf(hget), TyszmxDto.class);
                            List<TyszmxDto> dtoList=new ArrayList<>();
                            //排除子级的通用设置，只传递父级或者无子级的通用设置
                            if(tyszmxDtoList!=null&& !tyszmxDtoList.isEmpty()){
                                for(TyszmxDto dto:tyszmxDtoList){
                                    if(StringUtil.isBlank(dto.getFnrid())){
                                        dtoList.add(dto);
                                    }
                                }
                            }
                            List<TyszmxDto> menuList=new ArrayList<>();//高级筛选
                            List<TyszmxDto> searchList=new ArrayList<>();//模糊查找
                            List<TyszmxDto> fieldList=new ArrayList<>();//列表字段
                            List<TyszmxDto> buttonList=new ArrayList<>();//右键菜单
                            if(!dtoList.isEmpty()){
                                TyszmxDto tyszmxDto=dtoList.get(0);
                                List<TyszmxDto> tyszmxDtos=new ArrayList<>();
                                for(TyszmxDto dto:dtoList){
                                    //lx=1为右键菜单，=2为列表字段，=3为模糊查找
                                    if("1".equals(dto.getLx())){
                                        buttonList.add(dto);
                                    }else if("2".equals(dto.getLx())){
                                        fieldList.add(dto);
                                    }else if("3".equals(dto.getLx())){
                                        searchList.add(dto);
                                    }else{
                                        //遍历查找，将相同标题的内容合成一个List
                                        if(dto.getBtid().equals(tyszmxDto.getBtid())){
                                            //筛选类别=1代表是基础数据，=0代表是自定义
                                            if("1".equals(dto.getSxlb())){
                                                dto.setTyszmxDtos(redisUtil.hmgetDto("matridx_jcsj:" + dto.getNr()));
                                                menuList.add(dto);
                                            }else{
                                                //字段类型=2代表是时间类型
                                                if("2".equals(dto.getZdlx())){
                                                    menuList.add(dto);
                                                }else{
                                                    tyszmxDtos.add(dto);
                                                }
                                            }
                                        }else{
                                            if(!tyszmxDtos.isEmpty()){
                                                TyszmxDto tyszmxDto_t= tyszmxDto.clone();
                                                tyszmxDto_t.setTyszmxDtos(tyszmxDtos);
                                                menuList.add(tyszmxDto_t);
                                            }
                                            tyszmxDto=dto;
                                            tyszmxDtos=new ArrayList<>();
                                            if("1".equals(dto.getSxlb())){
                                                dto.setTyszmxDtos(redisUtil.hmgetDto("matridx_jcsj:" + dto.getNr()));
                                                menuList.add(dto);
                                            }else{
                                                if("2".equals(dto.getZdlx())){
                                                    menuList.add(dto);
                                                }else{
                                                    tyszmxDtos.add(dto);
                                                }
                                            }
                                        }
                                    }
                                }
                                if(!tyszmxDtos.isEmpty()){
                                    TyszmxDto tyszmxDto_t= tyszmxDto.clone();
                                    tyszmxDto_t.setTyszmxDtos(tyszmxDtos);
                                    menuList.add(tyszmxDto_t);
                                }
                            }
                            jsonObject.put("filterList",menuList);
                            jsonObject.put("buttonList",buttonList);
                            jsonObject.put("searchList",searchList);
                            jsonObject.put("fieldList",fieldList);
                        }
                    }catch (Exception e){
                        log.error(e.toString());
                    }
                    jsonObject.put("waitList",waitList);
                    jsonObject.put("choseList",choseList);
                    return jsonObject;
                    }
                }

            }
        return body;
    }
}