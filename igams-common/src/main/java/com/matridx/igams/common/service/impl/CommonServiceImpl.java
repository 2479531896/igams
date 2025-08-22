package com.matridx.igams.common.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.DepartmentDto;
import com.matridx.igams.common.dao.entities.FjcfbModel;
import com.matridx.igams.common.dao.entities.GrszDto;
import com.matridx.igams.common.dao.entities.QxModel;
import com.matridx.igams.common.dao.entities.Role;
import com.matridx.igams.common.dao.entities.SelectListDto;
import com.matridx.igams.common.dao.entities.ShxxDto;
import com.matridx.igams.common.dao.entities.SpgwDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.WbaqyzDto;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.dao.entities.XxdyDto;
import com.matridx.igams.common.dao.post.ICommonDao;
import com.matridx.igams.common.enums.ProgramCodeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseServiceImpl;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IGrszService;
import com.matridx.igams.common.service.svcinterface.IShxxService;
import com.matridx.igams.common.service.svcinterface.ISpgwService;
import com.matridx.igams.common.service.svcinterface.IWbaqyzService;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.igams.common.service.svcinterface.IXxdyService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.common.util.HttpUtil;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.base.UrlUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.encrypt.Encrypt;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class CommonServiceImpl extends BaseServiceImpl<SelectListDto, ICommonDao> implements ICommonService {

    @Autowired
    protected RestTemplate restTemplate;
    @Autowired
    private RedisUtil redisUtil;
    //访问主程序端口
    @Value("${matridx.prefix.igamsweb:}")
    private String webPrefix;
    @Value("${matridx.wechat.menuurl:}")
    private String menuurl;
    @Value("${matridx.fileupload.releasePath:}")
    private String releaseFilePath;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Value("${matridx.wechat.applicationurl:}")
    private String applicationurl;
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    private IDdxxglService DdxxglService;
    @Autowired
    private IXxglService xxglService;
    @Autowired
    private IWbaqyzService wbaqyzService;
    @Autowired
    private IXxdyService xxdyService;
    @Autowired
    private IXtszService xtszService;
    @Autowired
    private IShxxService shxxService;
    @Autowired
    private ISpgwService spgwService;
    @Autowired
    private IGrszService grszService;

    private final Logger log = LoggerFactory.getLogger(CommonServiceImpl.class);

    /**
     * 根据给定用户信息查询其所有有权限的机构ID集
     */
    @Override
    public List<String> listOrgByYhid(Map<String, Object> param) {
        // TODO Auto-generated method stub
        return dao.listOrgByYhid(param);
    }


    /**
     * 根据用户信息获取用户的权限信息，主要用于各业务模块的委托权限处理，但跟XtyhService 有重复（因各业务无法访问web层），需注意要保持统一
     */
    public com.matridx.igams.common.dao.entities.User getAuthorByUser(com.matridx.igams.common.dao.entities.User user) {
        //根据用户信息查找权限信息
        Map<String, String> param = new HashMap<>();
        param.put("yhid", user.getYhid());
        List<Map<String, String>> yhqxDtos = dao.getJsDtoList(param);

        if (yhqxDtos == null) {
            throw new BadCredentialsException("用户：" + user.getYhm() + "没有权限！");
        }

        Map<String, String> dq_yhjs = null;
        List<String> t_jsids = new ArrayList<>();
        List<String> t_jsmcs = new ArrayList<>();
        for (Map<String, String> yhqxDto : yhqxDtos) {
            if (yhqxDto.get("jsid").equals(user.getDqjs()))
                dq_yhjs = yhqxDto;
            t_jsids.add(yhqxDto.get("jsid"));
            t_jsmcs.add(yhqxDto.get("jsmc"));
        }
        if (dq_yhjs == null)
            dq_yhjs = yhqxDtos.get(0);

        //保存用户的角色列表
        user.setJsids(t_jsids);
        user.setJsmcs(t_jsmcs);
        //设置当前角色信息
        user.setDqjs(dq_yhjs.get("jsid"));
        user.setDqjsdm(dq_yhjs.get("jsdm"));
        user.setDqjsmc(dq_yhjs.get("jsmc"));
        user.setDqjsdwxdbj(dq_yhjs.get("dwxdbj"));
        //设置用户机构信息 2019-10-21 用于机构权限限制
        user.setJgid(dq_yhjs.get("jgid"));

        //查询当前角色的资源操作表
        QxModel t_qxDto = new QxModel();
        t_qxDto.setJsid(dq_yhjs.get("jsid"));
        List<QxModel> qxModels = dao.getQxModelList(t_qxDto);

        int size = qxModels.size();
        if (size > 0) {
            List<QxModel> nobtnmenuList = new ArrayList<>();
            List<QxModel> temp_qxModels = new ArrayList<>();
            //String prezyid = "";
            for (QxModel qxModel : qxModels) {
                if ("menupower".equals(qxModel.getCzdm())) {
                    nobtnmenuList.add(qxModel);
                } else {
                    temp_qxModels.add(qxModel);
                }
            }
            user.setNobtnlistModels(nobtnmenuList);
            //设置当前角色的资源操作表
            user.setQxModels(temp_qxModels);
        }
        //设置当前角色的资源操作表
		/*user.setQxModels(qxModels);
		//查询当前角色的系统资源表
		List<QxModel> nobtnmenuList = dao.getNoButtonMenu(t_qxDto);
		user.setNobtnlistModels(nobtnmenuList);*/

        return user;
    }

    /**
     * 因用户信息保存在web 包里，无法获取，所以在共通里做一个获取委托用户信息的方法
     */
    public List<Map<String, String>> getJsDtoList(Map<String, String> param) {
        return dao.getJsDtoList(param);
    }

    @Override
    public User getYhid(String ddid) {
        return dao.getYhid(ddid);
    }

    /**
     * 设置申请人姓名
     */
    @Override
    public void setSqrxm(List<?> list) {
        try {
            List<String> yghs = new ArrayList<>();
            for (Object object : list) {
                Class<?> entityClass = object.getClass();
                Method getMethod = entityClass.getMethod("getSqr");
                String yhid = (String) getMethod.invoke(object);
                if (yhid == null)
                    continue;
                yghs.add(yhid);
            }
            if (yghs.isEmpty()) return;
            List<User> xtyhs = dao.getZsxmByYhid(yghs);
            for (Object object : list) {
                Class<?> entityClass = object.getClass();
                Method getMethod = entityClass.getMethod("getSqr");
                String ygh = (String) getMethod.invoke(object);
                if (ygh == null) continue;
                Method setMethod = entityClass.getMethod("setSqrxm",
                        String.class);
                for (User xtyh : xtyhs) {
                    if (ygh.equals(xtyh.getYhid())) {
                        setMethod.invoke(object, String.valueOf(xtyh.getZsxm()));
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 设置Cloud下获取JSON从而得到申请人姓名
     */
    public void setSqrxmByJson(List<?> list) {
        try {
            List<String> yghs = new ArrayList<>();
            for (Object object : list) {
                JSONObject json_object = (JSONObject) object;
                String yhid = (String) json_object.get("sqr");
                if (yhid == null)
                    continue;
                yghs.add(yhid);
            }
            if (yghs.isEmpty()) return;
            List<User> xtyhs = dao.getZsxmByYhid(yghs);
            for (Object object : list) {
                JSONObject json_object = (JSONObject) object;
                String ygh = (String) json_object.get("sqr");
                if (ygh == null) continue;
                for (User xtyh : xtyhs) {
                    if (ygh.equals(xtyh.getYhid())) {
                        json_object.put("sqrxm", xtyh.getZsxm());
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 不传参默认调用时间加密
     */
    @Override
    public String getSign() {
        return getSign(null);
    }

    /**
     * 获取加密信息
     */
    @Override
    public String getSign(String str) {
        DBEncrypt crypt = new DBEncrypt();
        String sign;
        if (StringUtil.isNotBlank(str)) {
            sign = crypt.eCode(str);
        } else {
            long currentTimeMillis = System.currentTimeMillis();
            String time = String.valueOf(currentTimeMillis);
            sign = crypt.eCode(time);
        }
        return sign;
    }

    /**
     * 传一个参数默认比较时间
     */
    @Override
    public boolean checkSign(String sign, HttpServletRequest req) {
        return checkSign(sign, null, req);
    }

    /**
     * 检查加密信息
     */
    @Override
    public boolean checkSign(String sign, String data, HttpServletRequest req) {
        //睡眠5s，防止有恶意服务器获取本地信息，而采取延迟返回。
        try {
            if (StringUtil.isNotBlank(sign)) {
                DBEncrypt crypt = new DBEncrypt();
                String str = crypt.dCode(sign);
                if (StringUtil.isBlank(data)) {
                    if ((System.currentTimeMillis() - Long.parseLong(str)) / 1000 < 1800) {
                        return true;
                    }
                } else {
                    if (str.equals(data)) {
                        return true;
                    }
                }
            }
            Thread.sleep(5000);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Map<String, String[]> params = req.getParameterMap();
            StringBuilder s_params = new StringBuilder();
            if (params != null) {
                for (Map.Entry<String, String[]> entry : params.entrySet()) {
                    s_params.append("[").append(entry.getKey()).append(" = ").append(entry.getValue()[0]).append("]");
                }
            }
            log.error(e.getMessage() + " IP: " + req.getRemoteAddr() + " URL:" + req.getRequestURL().toString() + " Params:" + s_params);
        }
        return false;
    }

    /**
     * 检查加密信息(不可逆)
     * @param organ key
     * @param sign  签名
     * @param req   请求信息
     */
    @Override
    public Map<String, Object> checkSignUnBack(String organ, String sign, HttpServletRequest req) {
        Map<String, Object> map = new HashMap<>();
        //organ判断安全性
        if (StringUtil.isBlank(organ)) {
            map.put("status", "fail");
            map.put("errorCode", "未获取到代码信息organ！");
            log.error("未获取到代码信息organ！");
            return map;
        }
        if (StringUtil.isBlank(sign)) {
            map.put("status", "fail");
            map.put("errorCode", "未获取到sign签名!");
            log.error("未获取到sign签名!");
            return map;
        }
        //WbaqyzDto wbaqyzDto = wbaqyzService.getDtoById(organ);
        Object o_wbaqyz = redisUtil.get("Wbaqyz:" + organ);
        WbaqyzDto wbaqyzDto = null;
        if(o_wbaqyz!=null){
            wbaqyzDto = JSONObject.parseObject((String)o_wbaqyz,WbaqyzDto.class);
        }
        if (wbaqyzDto == null) {
            map.put("status", "fail");
            map.put("errorCode", "代码信息organ不正确，未获取到秘钥！" + organ);
            log.error("代码信息organ不正确，未获取到秘钥！");
            return map;
        }
        DBEncrypt dbEncrypt = new DBEncrypt();
        String word = wbaqyzDto.getWord();
        word = dbEncrypt.dCode(word);
        String text = organ + word;
        String t_sign = Encrypt.encrypt(text, "SHA1");
        if (!sign.equalsIgnoreCase(t_sign)) {
            map.put("status", "fail");
            map.put("errorCode", "sign信息不正确！");
            log.error("sign信息不正确！");
            return map;
        }
        map.put("errorCode", "0");
        return map;
    }

    /**
     * 跳转错误页面
     */
    @Override
    public ModelAndView jumpError() {
        return new ModelAndView("common/error/error_timeout");
    }

    /**
     * 跳转审核错误页面
     */
    @Override
    public ModelAndView jumpAuditError() {
        return new ModelAndView("common/error/error_audit");
    }

    /**
     * 根据用户名获取用户信息
     */
    @Override
    public List<User> getUserListByYhms(List<String> ids) {
        // TODO Auto-generated method stub
        return dao.getUserListByYhms(ids);
    }

    /**
     * 获取用户列表
     */
    @Override
    public Map<String, Object> getUserList(String prefix, HttpServletRequest request) {

        Map<String, String> params = new HashMap<>();
        params.put("access_token", request.getParameter("access_token"));

        return getUserList(prefix, params);
    }

    /**
     * 获取所有用户列表
     */
    public Map<String, Object> getUserList(String prefix, Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        if (StringUtil.isNotBlank(prefix)) {
            MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
            paramMap.add("access_token", params.get("access_token"));
            @SuppressWarnings("unchecked")
            Map<String, List<User>> map = restTemplate.postForObject(UrlUtil.getWebPrefix(webPrefix) + "/systemrole/common/getUserList", paramMap, Map.class);
            List<User> users = new ArrayList<>();
            if (map != null) {
                users = map.get("users");
            }
            result.put("users", users);
        } else {
            //本地查询
            List<User> users = dao.getUserList();
            result.put("users", users);
        }

        return result;
    }

    /**
     * 获取角色列表
     */
    @Override
    public Map<String, Object> getRoleList(String prefix, HttpServletRequest request) {

        Map<String, String> params = new HashMap<>();
        params.put("access_token", request.getParameter("access_token"));

        return getRoleList(prefix, params);
    }

    /**
     * 获取所有角色列表
     */
    public Map<String, Object> getRoleList(String prefix, Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        if (StringUtil.isNotBlank(prefix)) {
            MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
            paramMap.add("access_token", params.get("access_token"));
            @SuppressWarnings("unchecked")
            Map<String, List<Role>> map = restTemplate.postForObject(UrlUtil.getWebPrefix(webPrefix) + "/systemrole/common/getRoleList", paramMap, Map.class);
            List<Role> roles = new ArrayList<>();
            if (map != null) {
                roles = map.get("roles");
            }
            result.put("roles", roles);
        } else {
            //本地查询
            List<Role> roles = dao.getRoleList();
            result.put("roles", roles);
        }

        return result;
    }

    /**
     * 获取机构列表
     */
    @Override
    public Map<String, Object> getDepartmentList(String prefix, HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        params.put("access_token", request.getParameter("access_token"));

        return getDepartmentList(prefix, params);
    }

    /**
     * 获取机构列表
     */
    public Map<String, Object> getDepartmentList(String prefix, Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        if (StringUtil.isNotBlank(prefix)) {
            MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
            paramMap.add("access_token", params.get("access_token"));
            @SuppressWarnings("unchecked")
            Map<String, List<DepartmentDto>> map = restTemplate.postForObject(UrlUtil.getWebPrefix(webPrefix) + "/systemrole/common/getDepartmentList", paramMap, Map.class);
            List<DepartmentDto> departmentDtos = new ArrayList<>();
            if (map != null) {
                departmentDtos = map.get("departmentDtos");
            }
            result.put("departmentDtos", departmentDtos);
        } else {
            //本地查询
            List<DepartmentDto> departmentDtos = dao.getJgxxList();
            result.put("departmentDtos", departmentDtos);
        }
        return result;
    }


    /**
     * 根据用户名获取用户列表
     */
    @Override
    public Map<String, Object> getUserListByYhm(String prefix, String yhm, HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        params.put("access_token", request.getParameter("access_token"));

        return getUserListByYhm(prefix, yhm, params);
    }

    /**
     * 根据用户名获取用户列表
     */
    public Map<String, Object> getUserListByYhm(String prefix, String yhm, Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        if (StringUtil.isNotBlank(prefix)) {
            MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
            paramMap.add("access_token", params.get("access_token"));
            paramMap.add("yhm", yhm);
            @SuppressWarnings("unchecked")
            Map<String, List<User>> map = restTemplate.postForObject(UrlUtil.getWebPrefix(webPrefix) + "/systemrole/common/getUserListByYhm", paramMap, Map.class);
            List<User> users = new ArrayList<>();
            if (map != null) {
                users = map.get("users");
            }
            result.put("users", users);
        } else {
            //本地查询
            User user = new User();
            user.setYhm(yhm);
            List<User> users = dao.getListByYhm(user);
            result.put("users", users);
        }
        return result;
    }


    /**
     * 通过用户名模糊查询
     */
    @Override
    public List<User> getListByYhm(User user){
        return dao.getListByYhm(user);
    }
    /**
     * 根据用户Ids获取用户列表
     */
    @Override
    public Map<String, Object> getUserListByIds(String prefix, List<String> ids, HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        params.put("access_token", request.getParameter("access_token"));

        return getUserListByIds(prefix, ids, params);
    }

    /**
     * 根据用户Ids获取用户列表
     */
    public Map<String, Object> getUserListByIds(String prefix, List<String> ids, Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        if (StringUtil.isNotBlank(prefix)) {
            MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
            paramMap.add("access_token", params.get("access_token"));
            paramMap.add("ids", ids);
            @SuppressWarnings("unchecked")
            Map<String, List<User>> map = restTemplate.postForObject(UrlUtil.getWebPrefix(webPrefix) + "/systemrole/common/pagedataUserListByIds", paramMap, Map.class);
            List<User> users = new ArrayList<>();
            if (map != null) {
                users = map.get("users");
            }
            result.put("users", users);
        } else {
            //本地查询
            User user = new User();
            user.setIds(ids);
            List<User> users = dao.getListByIds(user);
            result.put("users", users);
        }
        return result;
    }

    /**
     * 根据用户ID获取用户列表
     */
    @Override
    public Map<String, Object> getUserInfo(String prefix, String yhid, HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        params.put("access_token", request.getParameter("access_token"));

        return getUserInfo(prefix, yhid, params);
    }

    /**
     * 根据用户ID获取用户列表
     */
    public Map<String, Object> getUserInfo(String prefix, String yhid, Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        if (StringUtil.isNotBlank(prefix)) {
            MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
            paramMap.add("access_token", params.get("access_token"));
            paramMap.add("yhid", yhid);
            @SuppressWarnings("unchecked")
            Map<String, Object> map = restTemplate.postForObject(UrlUtil.getWebPrefix(webPrefix) + "/common/common/getUserInfo", paramMap, Map.class);
            User user = new User();
            if (map != null) {
                user = JSONObject.parseObject(JSONObject.toJSONString(map.get("user")), User.class);
            }
            result.put("user", user);
        } else {
            //本地查询
            User user = new User();
            user.setYhid(yhid);
            User t_user = dao.getUserInfoById(user);
            result.put("user", t_user);
        }
        return result;
    }

    /**
     * 根据用户ID获取机构信息
     */
    @Override
    public Map<String, Object> getDepartmentByUser(String prefix, String yhid, HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        params.put("access_token", request.getParameter("access_token"));

        return getDepartmentByUser(prefix, yhid, params);
    }

    /**
     * 根据用户ID获取机构信息
     */
    public Map<String, Object> getDepartmentByUser(String prefix, String yhid, Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        if (StringUtil.isNotBlank(prefix)) {
            MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
            paramMap.add("access_token", params.get("access_token"));
            paramMap.add("yhid", yhid);
            @SuppressWarnings("unchecked")
            Map<String, List<DepartmentDto>> map = restTemplate.postForObject(UrlUtil.getWebPrefix(webPrefix) + "/systemrole/common/getDepartmentByUser", paramMap, Map.class);
            List<DepartmentDto> departmentDtos = new ArrayList<>();
            if (map != null) {
                departmentDtos = map.get("departmentDtos");
            }
            result.put("departmentDtos", departmentDtos);
        } else {
            //本地查询
            DepartmentDto departmentDto = new DepartmentDto();
            departmentDto.setYhid(yhid);
            List<DepartmentDto> departmentDtos = dao.getJgxxByYhid(departmentDto);
            result.put("departmentDtos", departmentDtos);
        }
        return result;
    }

    /**
     * 根据ID获取机构信息
     */
    @Override
    public Map<String, Object> getDepartmentById(String prefix, String jgid, HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        params.put("access_token", request.getParameter("access_token"));

        return getDepartmentById(prefix, jgid, params);
    }

    /**
     * 根据ID获取机构信息
     */
    public Map<String, Object> getDepartmentById(String prefix, String jgid, Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setJgid(jgid);
        List<DepartmentDto> departmentDtos = dao.getJgxxById(departmentDto);
        result.put("departmentDtos", departmentDtos);
        return result;
    }

    /**
     * 根据机构Ids获取机构列表
     */
    @Override
    public Map<String, Object> getDepartmentListByIds(String prefix, List<String> ids, HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        params.put("access_token", request.getParameter("access_token"));

        return getDepartmentListByIds(prefix, ids, params);
    }

    /**
     * 根据机构Ids获取机构列表
     */
    public Map<String, Object> getDepartmentListByIds(String prefix, List<String> ids, Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        if (StringUtil.isNotBlank(prefix)) {
            MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
            paramMap.add("access_token", params.get("access_token"));
            paramMap.add("ids", ids);
            @SuppressWarnings("unchecked")
            Map<String, List<DepartmentDto>> map = restTemplate.postForObject(UrlUtil.getWebPrefix(webPrefix) + "/systemrole/common/getDepartmentListByIds", paramMap, Map.class);
            List<DepartmentDto> departmentDtos = new ArrayList<>();
            if (map != null) {
                departmentDtos = map.get("departmentDtos");
            }
            result.put("departmentDtos", departmentDtos);
        } else {
            //本地查询
            DepartmentDto departmentDto = new DepartmentDto();
            departmentDto.setIds(ids);
            List<DepartmentDto> departmentDtos = dao.getJgListByIds(departmentDto);
            result.put("departmentDtos", departmentDtos);
        }
        return result;
    }

    /**
     * 查询机构列表
     */
    @Override
    public List<DepartmentDto> getPagedDepartmentList(String prefix, DepartmentDto departmentDto, HttpServletRequest request) {
        // TODO Auto-generated method stub
        List<DepartmentDto> departmentDtos = null;
        if (StringUtil.isNotBlank(prefix)) {
            MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
            paramMap.add("access_token", request.getParameter("access_token"));
            paramMap.add("departmentDto", departmentDto);
            @SuppressWarnings("unchecked")
            Map<String, List<DepartmentDto>> map = restTemplate.postForObject(UrlUtil.getWebPrefix(webPrefix) + "/systemrole/common/getPagedDepartmentList", paramMap, Map.class);
            if (map != null) {
                departmentDtos = map.get("departmentDtos");
            }
        } else {
            //本地查询
            departmentDtos = dao.getPagedDepartmentList(departmentDto);
        }
        return departmentDtos;
    }

    /**
     * 根据departmentDtos获取信息
     */
    @Override
    public Map<String, Object> getDepartmentListByDtos(String prefix, List<DepartmentDto> departmentDtos,
                                                       HttpServletRequest request) {
        // TODO Auto-generated method stub
        Map<String, String> params = new HashMap<>();
        params.put("access_token", request.getParameter("access_token"));

        return getDepartmentListByDtos(prefix, departmentDtos, params);
    }

    /**
     * 根据departmentDtos获取信息
     */
    public Map<String, Object> getDepartmentListByDtos(String prefix, List<DepartmentDto> departmentDtos, Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, String>> listMap = dao.getDepartmentListByDtos(departmentDtos);
        result.put("listMap", listMap);
        return result;
    }

    /**
     * 根据用户Dtos获取用户列表
     */
    @Override
    public Map<String, Object> getUserListByDtos(String prefix, List<User> users, HttpServletRequest request) {
        // TODO Auto-generated method stub
        Map<String, String> params = new HashMap<>();
        params.put("access_token", request.getParameter("access_token"));

        return getUserListByDtos(prefix, users, params);
    }

    /**
     * 根据用户Dtos获取用户列表
     */
    @Override
    public Map<String, Object> getUserListByDtos(String prefix, List<User> users, Map<String, String> params) {
        // TODO Auto-generated method stub
        Map<String, Object> result = new HashMap<>();
        if (StringUtil.isNotBlank(prefix)) {
            MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
            paramMap.add("access_token", params.get("access_token"));
            paramMap.add("users", users);
            @SuppressWarnings("unchecked")
            Map<String, List<Map<String, String>>> map = restTemplate.postForObject(UrlUtil.getWebPrefix(webPrefix) + "/systemrole/common/getUserListByDtos", paramMap, Map.class);
            List<Map<String, String>> listMap = new ArrayList<>();
            if (map != null) {
                listMap = map.get("listMap");
            }
            result.put("listMap", listMap);
        } else {
            //本地查询
            List<Map<String, String>> listMap = dao.getUserListByDtos(users);
            result.put("listMap", listMap);
        }
        return result;
    }

    /**
     * 获取机构信息
     */
    @Override
    public List<DepartmentDto> getJgxxDto(DepartmentDto departmentDto) {
        // TODO Auto-generated method stub
        return dao.getJgxxDto(departmentDto);
    }

    /**
     * 根据用户token获取用户信息
     */
    public Map<String, String> getUserInfoByToken(String tokenId) {
        return dao.getUserInfoByToken(tokenId);
    }

    /**
     * 根据角色ID获取用户信息(文件)
     */
    @Override
    public Map<String, Object> selectAddXtyhByJsid(String prefix, Map<String, Object> map, HttpServletRequest request) {
        // TODO Auto-generated method stub
        Map<String, String> params = new HashMap<>();
        params.put("access_token", request.getParameter("access_token"));

        return selectAddXtyhByJsid(prefix, map, params);
    }

    /**
     * 根据角色ID获取用户信息(文件)
     */
    public Map<String, Object> selectAddXtyhByJsid(String prefix, Map<String, Object> jsMap, Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        if (StringUtil.isNotBlank(prefix)) {
            MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
            paramMap.add("access_token", params.get("access_token"));
            paramMap.add("jsMap", jsMap);
            @SuppressWarnings("unchecked")
            Map<String, Object> map = restTemplate.postForObject(UrlUtil.getWebPrefix(webPrefix) + "/systemrole/common/selectAddXtyhByJsid", paramMap, Map.class);
            String s_jsonObject = null;
            if (map != null) {
                s_jsonObject = map.get("users").toString();
            }
            List<User> users = JSONObject.parseArray(s_jsonObject, User.class);
            result.put("users", users);
        } else {
            //本地查询
            List<User> users = dao.selectAddXtyhByJsid(jsMap);
            result.put("users", users);
        }
        return result;
    }

    /**
     * 根据用户ID查询审批成员列表
     */
    @Override
    public List<SpgwcyDto> selectSpgwcyByYhid(String yhid) {
        // TODO Auto-generated method stub
        return dao.selectSpgwcyByYhid(yhid);
    }


    /**
     * 筛选机构信息，审批岗位不限制单位的时候，所有审核人员直接加到发送消息的队列中。
     * 如果审批岗位进行单位限制，则确认角色是否进行单位限制
     * 如果角色不进行单位限制，则把人员加到发送消息的队列中
     * 如果角色有单位限制的，则根据机构ID确认该角色有没有这个机构的权限，有则加入队列中
     */
    public List<SpgwcyDto> siftJgList(List<SpgwcyDto> preList, String jgid) {
        if (StringUtil.isBlank(jgid)) {
            return preList;
        }
        List<SpgwcyDto> resultList = new ArrayList<>();
        //限制单位的审批人员清单
        List<SpgwcyDto> limitJsList = new ArrayList<>();

        if (preList != null && !preList.isEmpty()) {
            for (SpgwcyDto spgwcyDto : preList) {
                //如果审批岗位进行单位限制，则确认角色是否进行单位限制
                if ("1".equals(spgwcyDto.getDwxz())) {
                    //如果角色不进行单位限制，则把人员加到发送消息的队列中
                    if (!"1".equals(spgwcyDto.getDwxdbj())) {
                        resultList.add(spgwcyDto);
                    } else {
                        //如果角色有单位限制的，则根据机构ID确认该角色有没有这个机构的权限，有则加入队列中
                        //为了性能，先加到list里，统一一次查询
                        limitJsList.add(spgwcyDto);
                    }
                } else {
                    //审批岗位不限制单位的时候，所有审核人员直接加到发送消息的队列中
                    resultList.add(spgwcyDto);
                }
            }
        }
        //限制单位的审批人员清单不为空，意味着 审批岗位要进行单位限制，相应的角色也进行单位限制，所以要筛选出指定机构的审批人员信息
        if (!limitJsList.isEmpty()) {
            Map<String, Object> paraMap = new HashMap<>();
            //指定单位
            paraMap.put("jgid", jgid);
            //限制单位的审批人员清单
            paraMap.put("list", limitJsList);

            try {
                List<SpgwcyDto> ssSpgwcyDtos = dao.getJgidsByLimitJs(paraMap);
                resultList.addAll(ssSpgwcyDtos);
            } catch (Exception e) {
                log.error(e.getMessage());
                throw e;
            }
        }

        return resultList;
    }

    /**
     * 筛选检测单位信息，审批岗位不限制单位的时候，所有审核人员直接加到发送消息的队列中。
     * 如果审批岗位进行单位限制，则确认角色是否进行单位限制
     * 如果角色不进行单位限制，则把人员加到发送消息的队列中
     * 如果角色有单位限制的，则根据检测单位ID确认该角色有没有这个检测单位的权限，有则加入队列中
     */
    public List<SpgwcyDto> siftJcdwList(List<SpgwcyDto> preList, String jcdwid) {
        if (StringUtil.isBlank(jcdwid)) {
            return preList;
        }
        List<SpgwcyDto> resultList = new ArrayList<>();
        //限制单位的审批人员清单
        List<SpgwcyDto> limitJsList = new ArrayList<>();

        if (preList != null && !preList.isEmpty()) {
            for (SpgwcyDto spgwcyDto : preList) {
                //如果角色不进行单位限制，则把人员加到发送消息的队列中
                if (!"1".equals(spgwcyDto.getDwxdbj())) {
                    resultList.add(spgwcyDto);
                } else {
                    //如果角色有单位限制的，则根据检测单位ID确认该角色有没有这个检测单位的权限，有则加入队列中
                    //为了性能，先加到list里，统一一次查询
                    limitJsList.add(spgwcyDto);
                }
            }
        }
        //限制单位的审批人员清单不为空，意味着 审批岗位要进行单位限制，相应的角色也进行检测单位限制，所以要筛选出指定机构的审批人员信息
        if (!limitJsList.isEmpty()) {
            Map<String, Object> paraMap = new HashMap<>();
            //指定单位
            paraMap.put("jcdw", jcdwid);
            //限制单位的审批人员清单
            paraMap.put("list", limitJsList);

            try {
                List<SpgwcyDto> ssSpgwcyDtos = dao.getJcdwsByLimitJs(paraMap);
                resultList.addAll(ssSpgwcyDtos);
            } catch (Exception e) {
                log.error(e.getMessage());
                throw e;
            }
        }

        return resultList;
    }

    /**
     * 根据机构名称获取机构信息
     */
    @Override
    public List<DepartmentDto> getJgxxDtoByJgmc(String value) {
        // TODO Auto-generated method stub
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setJgmc(value);
        return getJgxxDto(departmentDto);
    }

    /**
     * 根据微信ID获取录入人员列表
     */
    @Override
    public List<String> getLrryList(String wxid) {
        List<String> lrrylist = new ArrayList<>();
        if (StringUtil.isNotBlank(wxid)) {
            User user_wx = new User();
            user_wx.setWechatid(wxid);
            //根据wxid获取相同系统用户id或相同unionid的微信用户信息
            List<User> wxyhlist = getListBySameId(user_wx);
            if (wxyhlist != null && !wxyhlist.isEmpty()) {
                for (User value : wxyhlist) {
                    lrrylist.add(value.getWechatid());
                    if (value.getXtyhid() != null && !"".equals(value.getXtyhid())) {
                        lrrylist.add(value.getXtyhid());
                        //根据用户id获取ddid
                        User user = new User();
                        user.setYhid(value.getXtyhid());
                        User userInfo = dao.getUserInfoById(user);
                        if (StringUtil.isNotBlank(userInfo.getDdid())) {
                            lrrylist.add(userInfo.getDdid());
                        }
                    }
                }
            } else {
                lrrylist.add(wxid);
            }
        }
        return lrrylist;
    }

    /**
     * 根据钉钉ID获取录入人员列表
     */
    public List<String> getLrryListByDdid(String ddid) {
        List<String> lrrylist = new ArrayList<>();
        if (StringUtil.isNotBlank(ddid)) {
            User user_dd = new User();
            user_dd.setDdid(ddid);
            //根据ddid获取相同系统用户id或相同unionid的微信用户信息
            List<User> users = getUserByDdid(user_dd);
            if (users != null && !users.isEmpty()) {
                lrrylist.add(ddid);
                for (User tUser : users) {
                    if (StringUtil.isNotBlank(tUser.getXtyhid())) {
                        lrrylist.add(tUser.getXtyhid());
                        //根据用户id获取wxid
                        User user = new User();
                        user.setYhid(tUser.getXtyhid());
                        User userInfo = dao.getUserInfoById(user);
                        if (StringUtil.isNotBlank(userInfo.getWechatid())) {
                            lrrylist.add(userInfo.getWechatid());
                        }
                    }
                }
            } else {
                lrrylist.add(ddid);
            }
        }
        return lrrylist;
    }

    /**
     * 根据钉钉ID或微信id获取录入人员列表
     */
    @Override
    public List<String> getLrryListByWxidOrDdid(String ddid, String wxid) {
        List<String> lrryList = new ArrayList<>();
        if (StringUtil.isNotBlank(ddid)) {
            lrryList = getLrryListByDdid(ddid);
        } else if (StringUtil.isNotBlank(wxid)) {
            lrryList = getLrryList(wxid);
        }
        return lrryList;
    }

    /**
     * 根据微信ID获取系统用户和钉钉ID
     */
    private List<User> getListBySameId(User user_wx) {
        // TODO Auto-generated method stub
        return dao.getListBySameId(user_wx);
    }


    /**
     * 获取机构列表(小程序)
     */
    public List<DepartmentDto> getMiniDepartmentList(DepartmentDto departmentDto) {
        return dao.getMiniDepartmentList(departmentDto);
    }

    /**
     * 根据路径创建文件
     */
    private boolean mkDirs(String storePath) {
        File file = new File(storePath);
        if (file.isDirectory()) {
            return true;
        }
        return file.mkdirs();
    }

    /**
     * 小程序文件下载
     */
    public boolean downloadFile(FjcfbModel fjcfbModel) {
        DBEncrypt crypt = new DBEncrypt();
        String wjlj = fjcfbModel.getWjlj();
        String newwjlj = releaseFilePath + fjcfbModel.getXwjlj();//本地正式文件路径
        String t_newfwjlj = releaseFilePath + crypt.dCode(fjcfbModel.getXfwjlj());
        fjcfbModel.setWjlj(crypt.eCode(newwjlj));
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("wjlj", wjlj);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(paramMap, headers);
            RestTemplate t_restTemplate = new RestTemplate();
            ResponseEntity<byte[]> response = t_restTemplate.exchange(menuurl + "/wechat/getImportFile", HttpMethod.POST, httpEntity, byte[].class);
            // 校验文件夹目录是否存在，不存在就创建一个目录
            mkDirs(t_newfwjlj);
            byte[] result = response.getBody();
            inputStream = new ByteArrayInputStream(result);

            outputStream = new FileOutputStream(newwjlj);

            int len;
            byte[] buf = new byte[1024];
            while ((len = inputStream.read(buf, 0, 1024)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.flush();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        return true;
    }

    /**
     * 根据用户名获取用户信息
     */
    public User getDtoByYhm(String yhm) {
        return dao.getDtoByYhm(yhm);
    }

    /**
     * 根据用户ID获取钉钉ID
     */
    @Override
    public List<User> getDdidByYhids(List<String> yhids) {
        // TODO Auto-generated method stub
        return dao.getDdidByYhids(yhids);
    }

    /**
     * 根据钉钉ID获取用户信息
     */
    public List<User> getUserByDdid(User user) {
        return dao.getUserByDdid(user);
    }

    /**
     * 根据微信ID获取外部程序信息
     */
    public User getWbcxInfoByWxid(String wxid) {
        return dao.getWbcxInfoByWxid(wxid);
    }

    /**
     * 根据用户ID查询用户信息
     */
    public User getUserInfoById(User user) {
        return dao.getUserInfoById(user);
    }

    @Override
    public User getUserId(String zsxm) {
        // TODO Auto-generated method stub
        return dao.getUserId(zsxm);
    }

    /**
     * 跳转文件预览错误页面
     */
    @Override
    public ModelAndView jumpDocumentError() {
        return new ModelAndView("common/error/error_document");
    }

    /**
     * 获取目录下所有的文件并返回所有文件列表，使用递归方式完成
     */
    @Override
    public List<File> getAllFile(List<File> listFile, File paramFile) {
        if (paramFile.isFile()) {// 文件，添加到文件列表中，本次调用结束，返回文件列表
            listFile.add(paramFile);
            return listFile;
        } else {// 目录
            File[] localFiles = paramFile.listFiles();
            if (localFiles != null) {
                for (File localFile : localFiles) {
                    getAllFile(listFile, localFile);
                }
            }
            // 空目录，本次调用结束，返回文件列表
            return listFile;
        }
    }

    /**
     * 根据ddids获取用户信息
     */
    public List<User> getYhxxsByDdids(User user) {
        return dao.getYhxxsByDdids(user);
    }

    /**
     * 根据yhid获取系统用户姓名
     */
    @Override
    public List<User> getZsxmByYhid(List<String> yghs) {
        // TODO Auto-generated method stub
        return dao.getZsxmByYhid(yghs);
    }

    public void ddxxRemind() {
        Map<String, String> map = new HashMap<>();
        ddxxRemind(map);
    }

    /**
     * 定时任务：根据钉钉消息的类型获取发送人员，并根据这个类型发送
     */
    public void ddxxRemind(Map<String, String> map) {
        String xxlx = map.get("xxlx");
        if (StringUtil.isNotBlank(xxlx)) {
            //下面为发送钉钉cardMessage消息做准备
//			String token = talkUtil.getToken();
            String ICOMM_TZ00001 = xxglService.getMsg("ICOMM_TZ00001");

            List<DdxxglDto> ddxxgldtolist = DdxxglService.selectByDdxxlx(xxlx);//实际上这里现在每次只能根据ddxxlx取出一条信息，且用户也只有一条，故用户那里其实不用for都是ok的
            if (CollectionUtils.isNotEmpty(ddxxgldtolist)) {
                String XX_HEAD = ddxxgldtolist.get(0).getDdxxmc();
                for (DdxxglDto ddxxglDto : ddxxgldtolist) {//这里是遍历用户
                    if (StringUtil.isNotBlank(ddxxglDto.getYhm())) {
                        //===================组装访问路径============
                        //访问路径 &：%26        =：%3D  ?:%3F
                        String url = applicationurl + urlPrefix + "/common/view/displayView?view_url=/ws/query/getStaticPageByType%3Fyhid%3D" + ddxxglDto.getYhid() + "%26lbqf%3D" + ddxxglDto.getDdxxlx();
                        List<BtnJsonList> btnJsonLists = new ArrayList<>();
                        BtnJsonList btnJsonList = new BtnJsonList();
                        btnJsonList.setTitle("详细");
                        btnJsonList.setActionUrl(url);
                        btnJsonLists.add(btnJsonList);
                        //====================发送钉钉消息=================================
                        talkUtil.sendCardMessage(ddxxglDto.getYhm(),
                                ddxxglDto.getDdid(),
                                XX_HEAD,
                                StringUtil.replaceMsg(ICOMM_TZ00001, XX_HEAD, DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss")),
                                btnJsonLists,
                                "1");
                    }
                }
            }
        }
    }

    /**
     * 定时压缩附件：压缩时间作为参数指定，路径也作为参数指定
     */
    public void compressAttachments(Map<String, String> map) {
        try {
            String time = map.get("time");
            int day = Integer.parseInt(StringUtil.isNotBlank(time) ? time : "365");
            String ljstr = map.get("lj");
            List<String> ljlist = Arrays.asList(ljstr.split("%"));
            if (StringUtil.isNotBlank(time) && !ljlist.isEmpty()) {
                //压缩路径下的附件
                for (String s : ljlist) {
                    compressFiles(s, day);
                }
            }
        } catch (Exception e) {
            log.error("定时任务压缩指定附件/n" + e.getMessage());
        }
    }

    /**
     * 定时删除附件：删除时间作为参数指定，路径也作为参数指定
     */
    public void delAttachments(Map<String, String> map) {
        try {
            String notDel = map.get("notDel");
            List<String> notDels = new ArrayList<>();
            if (StringUtil.isNotBlank(notDel)){
                notDels = Arrays.asList(notDel.split("@"));
            }
            String time = map.get("time");
            int day = Integer.parseInt(StringUtil.isNotBlank(time) ? time : "365");
            String ljstr = map.get("lj");
            List<String> ljlist = Arrays.asList(ljstr.split("%"));
            if (StringUtil.isNotBlank(time) && !ljlist.isEmpty()) {
                //删除路径下的附件
                for (String s : ljlist) {
                    //clearFiles("/matridx/fileupload/release/CONVID_JYBG/UP2021/UP202111/UP20211119");
                    clearFiles(s, day,notDels);
                }
            }
        } catch (Exception e) {
            log.error("定时任务删除设定附件/n" + e.getMessage());
        }
    }

    //压缩文件和删除原文件
    private void compressFiles(String workspaceRootPath, int day) {
        File file = new File(workspaceRootPath);
        if (file.exists()) {
            log.error("即将检查压缩文件路径：" + workspaceRootPath);
            compressFile(file, day,false,null);
        }
    }

    //删除文件和目录
    private void clearFiles(String workspaceRootPath, int day,List<String> notDels) {
        File file = new File(workspaceRootPath);
        if (file.exists()&&!notDels.contains(file.getName())) {
            log.error("即将检查删除文件路径：" + workspaceRootPath);
            deleteFile(file, day,false,notDels);
        }
    }

    private void compressFile(File file, int day,boolean isCompress,File directory) {
        long filetime = file.lastModified();
        if (file.isDirectory()) {//
            File[] files = file.listFiles();
            for (File value : files) {
                compressFile(value, day, true,file);
            }
        }
        if(!file.getAbsolutePath().contains("tar.gz")){
            boolean needCompress = countDay(filetime, day);
            if (needCompress) {
                log.error("压缩文件：" + file.getName() + " 文件修改时间" + filetime);
                try{
                    ProcessBuilder processBuilder = new ProcessBuilder(
                            "tar", "-zcvf","back/" + file.getName()+".tar.gz",file.getName()
                    );

                    log.error("tar -zcvf "+file.getName()+".tar.gz " +file.getName());

                    // 设置工作目录
                    processBuilder.directory(directory);
                    Process process = processBuilder.start();

                    // 读取错误流
                    BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    String errorMessage;
                    StringBuilder errorStringBuilder = new StringBuilder();
                    while ((errorMessage = errorReader.readLine()) != null) {
                        errorStringBuilder.append(errorMessage).append("\n");
                    }

                    int exitCode = process.waitFor();
                    if (exitCode == 0) {
                        log.error("文件" + file.getAbsolutePath() +"压缩成功！");
                        file.delete();//压缩完将原文件删除
                    } else {
                        // 命令执行失败，可以通过process.getErrorStream()读取错误信息
                        log.error("文件" + file.getAbsolutePath() + "压缩失败！错误信息：" + errorStringBuilder.toString());
                    }
                }catch (IOException | InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void deleteFile(File file, int day,boolean isDel,List<String> notDels) {
        if (!notDels.contains(file.getName())){
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                long filetime = file.lastModified();
                for (File value : files) {
                    deleteFile(value, day, true,notDels);
                }
                //file为文件夹则直接判断天数差距删除
                boolean needDel = countDay(filetime, day);
                if (needDel) {
                    log.error("删除文件夹：" + file.getName() + " 文件修改时间" + filetime);
                    file.delete();
                }
            }
            long stime = file.lastModified();
            //计算时间间隔，超过间隔的附件删除
            boolean needDel = countDay(stime, day);
            if (needDel && isDel) {
                log.error("删除文件：" + file.getName() + " 文件修改时间" + stime);
                file.delete();
            }
        }
    }

    private boolean countDay(long stime, int day) {
        long etime = System.currentTimeMillis();
        int days = (int) ((new Date(etime).getTime() - new Date(stime).getTime()) / (1000 * 60 * 60 * 24));//文件修改时间和当前时间间隔
        //时间间隔超过设定的间隔天数
        return days >= day;
    }

    @Override
    public DepartmentDto getJgxxInfo(String jgid) {
        return dao.getJgxxInfo(jgid);
    }

    /**
     * 发送微信消息
     */
    @Override
    public void sendWeChatMessage(String templateid, String wxid, String title, String keyword1, String keyword2, String keyword3, String keyword4, String remark, String reporturl) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("templateid", templateid);
        paramMap.add("wxid", wxid);
        paramMap.add("title", title);// 标题
        paramMap.add("keyword1", keyword1);
        paramMap.add("keyword2", keyword2);
        paramMap.add("keyword3", keyword3);
        paramMap.add("keyword4", keyword4);
        paramMap.add("remark", remark);
        paramMap.add("reporturl", reporturl);
        RestTemplate t_restTemplate = new RestTemplate();
        // 让服务器发送信息到相应的微信里
        t_restTemplate.postForObject(menuurl + "/wechat/sendWeChatMessage", paramMap, String.class);
    }


    /**
     * 发送微信消息
     */
    @Override
    public void sendWeChatMessageMap(String templatedm, String wxid, String wbcxdm, Map<String, String> messageMap) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("templatedm", templatedm);
        paramMap.add("wxid", wxid);
        if (StringUtil.isBlank(wbcxdm)) {
            User wbcxInfo = getWbcxInfoByWxid(wxid);
            if (wbcxInfo == null)
                return;
            wbcxdm = StringUtil.isNotBlank(wbcxInfo.getWbcxdm()) ? wbcxInfo.getWbcxdm() : ProgramCodeEnum.MDINSPECT.getCode();
        }
        paramMap.add("wbcxdm", wbcxdm);
        for (String key : messageMap.keySet()) {
            if (StringUtil.isNotBlank(messageMap.get(key))) {
                paramMap.add(key, messageMap.get(key));
            }
        }
        RestTemplate t_restTemplate = new RestTemplate();
        // 让服务器发送信息到相应的微信里
        t_restTemplate.postForObject(menuurl + "/wechat/sendWeChatMessageMap", paramMap, String.class);
    }

    //新冠扫场所登录方法
    @Override
    public Map<String, String> turnToSpringSecurityLogin(String username, String password) {
        Map<String, String> returnMap = null;
        try {
            DBEncrypt dbEncrypt = new DBEncrypt();
            List<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair("client_id", username));
            org.apache.commons.codec.binary.Base64 base64 = new Base64();
            String enPass = base64.encodeToString(password.getBytes());
            pairs.add(new BasicNameValuePair("client_secret", enPass));
            pairs.add(new BasicNameValuePair("grant_type", "matridx"));
            pairs.add(new BasicNameValuePair("sign", dbEncrypt.eCode(password)));
            CloseableHttpClient httpclient = null;
            CloseableHttpResponse httpresponse = null;
            try {
                httpclient = HttpClients.createDefault();
                HttpPost httppost = new HttpPost(applicationurl + "/oauth/token");
                // StringEntity stringentity = new StringEntity(data);
                httppost.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
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
            log.error(e.toString());
        }
        return returnMap;
    }

    /**
     * 检测是否为共同权限
     */
    public boolean checkCommonPower(String url) {
        //从URL中解析到操作代码，操作代码默认为最后一个/后的第一个单词，即按照获取到第一个大写字母为标准做截取
        String[] t_url = url.split("/");
        char[] last = t_url[t_url.length - 1].toCharArray();
        String czdm = "";
        for (char c : last) {
            if (Character.isLowerCase(c)) {//判断是否为小写，如果是进行拼接，如果读到大写则停止
                czdm = czdm + c;
            } else {
                break;
            }
        }
        //1.不拦截的操作代码
        if ("token".equals(czdm)
                || "index".equals(czdm)
                || "comm".equals(czdm)
                || "common".equals(czdm)
                || "ansy".equals(czdm)
                || "audit".equals(czdm)
                || "pagedata".equals(czdm)
                || "batchaudit".equals(czdm)
                || "minidata".equals(czdm)) {
            return true;
        }
        //2.不拦截的请求
        else if ("/experiment/project/modWorkTask".equals(url)
                || "/experiment/project/modWorkSubtask".equals(url)
                || "/checkToken".equals(url)
                || "/actuator/info".equals(url)
        ) {
            return true;
        }
        //2.不拦截的请求地址第一段
        else return url.startsWith("/common")
                    || url.startsWith("/systemmain/audit")
                    || url.startsWith("/systemmain/configProcess")
                    || url.startsWith("/systemcheck")
                    || url.startsWith("/init")
                    || url.startsWith("/web")
                    || url.startsWith("/systemrole/common")
                    || url.startsWith("/inspection/recheck/submitRecheckAudit")
                    || url.startsWith("/wsapi")
                    || url.startsWith("/performanceHomePage")
					||url.startsWith("/homePage")
					||url.startsWith("/localizationHomePage")
					||url.startsWith("/financialStatistics");
    }



    /**
     * 角色资源权限限制
     */
    public boolean checkCzqxExt(String url, User user, String urlPrefix) {
        if (urlPrefix == null)
            urlPrefix = "";
        boolean power = false;
        //从URL中解析到操作代码，操作代码默认为最后一个/后的第一个单词，即按照获取到第一个大写字母为标准做截取
        String[] t_url = url.split("/");
        char[] last = t_url[t_url.length - 1].toCharArray();
        String czdm = "";
        for (char c : last) {
            if (Character.isLowerCase(c)) {//判断是否为小写，如果是进行拼接，如果读到大写则停止
                czdm = czdm + c;
            } else {
                break;
            }
        }
        String opePath = url.replaceFirst("(" + czdm + ")(?:Save|Get)([A-Z]|\\.)", "$1$2");
        //1.根据资源路径检查权限
        if (user.getQxModels() != null && !user.getQxModels().isEmpty()) {
            for (QxModel model : user.getQxModels()) {
                String dyym = model.getDyym();
                if (StringUtil.isNotBlank(dyym)) {
                    int dyym_index = model.getDyym().indexOf("?");//过滤路径参数
                    if (dyym_index > 0) {
                        dyym = model.getDyym().substring(0, dyym_index);
                    }

                    if (opePath.equals(dyym) &&
                            (urlPrefix.equals(model.getFbsqz())
                                    || (StringUtil.isBlank(urlPrefix) && StringUtil.isBlank(model.getFbsqz())))
                    ) {
                        power = true;
                        break;
                    }
                }
            }
        }
        //2.特殊类，
        if (!power) {
            for (QxModel t_model : user.getNobtnlistModels()) {
                if (StringUtil.isNotBlank(t_model.getZylj())) {
                    int index_nobtn = t_model.getZylj().indexOf("?");
                    if (index_nobtn > 0) {
                        if ((urlPrefix + url).equals(t_model.getZylj().substring(0, index_nobtn))
                                || (urlPrefix + opePath).equals(t_model.getZylj().substring(0, index_nobtn))) {
                            power = true;
                            break;
                        }
                    } else {
                        if ((urlPrefix + url).equals(t_model.getZylj())
                                || (urlPrefix + opePath).equals(t_model.getZylj())) {
                            power = true;
                            break;
                        }
                    }
                }
            }
        }
        //log.error("--------拦截结果----------" + power);
        return power;
    }

    @Override
    public boolean checkWbCzqxExt(HttpServletRequest request, User user, String urlPrefix) {
            if (urlPrefix == null){
                urlPrefix = "";
            }

        boolean power = false;
        //从URL中解析到操作代码，操作代码默认为最后一个/后的第一个单词，即按照获取到第一个大写字母为标准做截取
        String[] t_url = request.getRequestURI().split("/");
        char[] last = t_url[t_url.length - 1].toCharArray();
        String czdm = "";
        for (char c : last) {
            if (Character.isLowerCase(c)) {//判断是否为小写，如果是进行拼接，如果读到大写则停止
                czdm = czdm + c;
            } else {
                break;
            }
        }
        String opePath = request.getRequestURI();
        QxModel qxModel=new QxModel();
        qxModel.setJsid(user.getDqjs());
        Object object=redisUtil.get("Users_WbQxModel:"+user.getDqjs());
        if(object==null){
            return false;
        }
        List<QxModel> qxModelList = JSON.parseArray(object.toString(),QxModel.class);
        if(qxModelList!=null&& !qxModelList.isEmpty()){
            for(QxModel qxModel1:qxModelList){
                String fwlj=qxModel1.getDyym();
                if(StringUtil.isNotBlank(fwlj)){
                    String[] fwArr=fwlj.split(",");
                    for(String fwStr:fwArr){
                        String newStr= fwStr;
                        int dyym_index = fwStr.indexOf("?");//过滤路径参数
                        if (dyym_index > 0) {
                            newStr = fwStr.substring(0, dyym_index);
                        }
                        String reqPre=request.getParameter("urlPrefix");
                        if (opePath.equals(newStr) &&
                                (urlPrefix.equals(reqPre)
                                        || (StringUtil.isBlank(urlPrefix) && StringUtil.isBlank(reqPre)))
                        ) {
                            power = true;
                            break;
                        }
                    }

                }
            }
        }
        return power;
    }


    /**
     * 检查加密信息 未加密信息&时间戳 == 解密（加密信息）
     *
     * @param sign   未加密信息
     * @param secret 加密信息
     */
    @Override
    public boolean checkSignSecretTime(String sign, String secret, HttpServletRequest req) {
        return checkSignSecretTime(sign, secret, null, req);
    }

    /**
     * 检查加密信息 未加密信息&时间戳 == 解密（加密信息）
     *
     * @param sign             未加密信息
     * @param secret           加密信息
     * @param timeFormatString 时间格式
     */
    @Override
    public boolean checkSignSecretTime(String sign, String secret, String timeFormatString, HttpServletRequest req) {
        //睡眠5s，防止有恶意服务器获取本地信息，而采取延迟返回。
        String mode = "SignOrSecrectIsNull";//SignOrSecrectIsNull：sign或secret为空
        //初始化当前sign的攻击信息
        Object signInBlacklistObj = redisUtil.hget("requestBlackList", sign);//redis中当前sign的攻击信息 object
        try {
            if (StringUtil.isNotBlank(sign) && StringUtil.isNotBlank(secret)) {
                mode = "SignIsInTheBlacklist";//signIsInTheBlacklist：sign在黑名单中
                //获取黑名单设置
                Map<String, String> blackSettings = getBlackSettings();
                boolean isSignIn = checkIsInBlacklist(sign, signInBlacklistObj, blackSettings);//检查sign是否在黑名单中
                if (!isSignIn) {
                    DBEncrypt dbEncrypt = new DBEncrypt();
                    mode = "WrongSecret";//WrongSecret：错误的密钥
                    String dCodeSecret;
                    dCodeSecret = dbEncrypt.dCode(secret);
                    if (dCodeSecret.contains("&")) {
                        String[] dCodeSecrets = dCodeSecret.split("&");
                        if (dCodeSecrets.length == 2) {
                            String secretWxid = dCodeSecrets[0];
                            String secretTime = dCodeSecrets[1];
                            if (StringUtil.isNotBlank(secretWxid) && StringUtil.isNotBlank(secretTime)) {
                                if (secretWxid.equals(sign)) {
                                    if (StringUtil.isBlank(timeFormatString)) {
                                        timeFormatString = "yyyyMMddHHmm";
                                    }
                                    String expirationTime = blackSettings.get("expirationTime");//时间戳有效期（分钟）
                                    //获取时间戳有效期（分钟），进行下一步验证
                                    if (timeFormatString.length() == secretTime.length()) {
                                        mode = "SecretTimeOut";//secretTimeOut：密钥超时
                                        SimpleDateFormat timeFormat = new SimpleDateFormat(timeFormatString);
                                        Date secretDate = timeFormat.parse(secretTime);
                                        Date nowDate = new Date();
                                        if (nowDate.getTime() - secretDate.getTime() <= 1000L * 60 * Integer.parseInt(expirationTime)) {
                                            mode = "ForbiddenWords";//ForbiddenWords：禁用词
                                            //若时间戳在有效时间内，则进行下一步验证：校验请求中是否包含敏感字段
                                            String forbiddens = blackSettings.get("fobiddensWords");
                                            boolean notHasForbidden = checkRequestHasForbidden(forbiddens, req);
                                            if (notHasForbidden) {
                                                return true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("checkSignSecretTime error", e.getMessage());
        }
        putIntoBlacklist(sign, mode, signInBlacklistObj, req);
        return false;
    }

    /**
     * 检查请求中是否包含敏感字段
     */
    public boolean checkRequestHasForbidden(String forbiddens, HttpServletRequest req) {
        String[] forbidddenTexts = forbiddens.split(",");
        Map<String, String[]> params = req.getParameterMap();
        if (params != null) {
            Set<String> keys = params.keySet();
            for (String key : keys) {
                String[] values = params.get(key);
                for (String value : values) {
                    for (String forbidden : forbidddenTexts) {
                        if (value.toLowerCase().contains(forbidden.toLowerCase())) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * 将sign存入黑名单，记录ip地址
     */
    public boolean putIntoBlacklist(String sign, String mode, Object signInBlacklistObj, HttpServletRequest req) {
        try {
            //初始化当前sign的攻击信息
            Map<String, Object> signInBlacklist = new HashMap<>();
            if (signInBlacklistObj != null) {
                signInBlacklist = JSON.parseObject(signInBlacklistObj.toString(), Map.class);//将sign的攻击信息设置为redis中的攻击信息
            }
            //signInBlacklist.attackTotalCount
            int attackTotalCount = 1;//初始化攻击次数，默认为1次
            Object attackTotalCountObj = signInBlacklist.get("attackTotalCount");
            if (attackTotalCountObj != null) {
                attackTotalCount = Integer.parseInt(attackTotalCountObj.toString()) + 1;
            }
            signInBlacklist.put("attackTotalCount", attackTotalCount);
            String attackTime = String.valueOf(System.currentTimeMillis());
            signInBlacklist.put("attackTime", attackTime);
            //signInBlacklist.attackIps
            Map<String, Object> attackIps = new HashMap<>();
            Object attackIpsObj = signInBlacklist.get("attackIps");
            if (attackIpsObj != null) {
                attackIps = JSON.parseObject(attackIpsObj.toString(), Map.class);
            }
            //初始化当前IP的攻击信息
            Map<String, Object> attackIpInfo = new HashMap<>();
            String attackIp = HttpUtil.getIpAddress(req);//获取攻击IP地址
            Object attackIpObj = attackIps.get(attackIp);
            if (attackIpObj != null) {
                attackIpInfo = JSON.parseObject(attackIpObj.toString(), Map.class);
            }
            //attackRequestInfo
            Map<String, Object> attackRequestInfo = new HashMap<>();
            String requestURL = req.getRequestURI();
            Object attackRequestObj = attackIpInfo.get(requestURL);
            if (attackRequestObj != null) {
                attackRequestInfo = JSON.parseObject(attackRequestObj.toString(), Map.class);
            }
            //attackRequestInfo.attactTimes
            List<String> attactTimes = new ArrayList<>();
            Object attactTimesObj = attackRequestInfo.get("attactTimes");
            if (attactTimesObj != null) {
                attactTimes = JSON.parseArray(attactTimesObj.toString(), String.class);
            }
            attactTimes.add(attackTime);
            attackRequestInfo.put("attactTimes", attactTimes);
            //attackRequestInfo.attackModes
            List<String> attackModes = new ArrayList<>();
            Object attackModesObj = attackRequestInfo.get("attackModes");
            if (attackModesObj != null) {
                attackModes = JSON.parseArray(attackModesObj.toString(), String.class);
            }
            attackModes.add(mode);
            attackRequestInfo.put("attackModes", attackModes);

            attackIpInfo.put(requestURL, attackRequestInfo);
            attackIps.put(attackIp, attackIpInfo);
            signInBlacklist.put("attackIps", attackIps);
            redisUtil.hset("requestBlackList", sign, JSON.toJSONString(signInBlacklist), -1);
            log.error("sign:" + sign + " ip:" + attackIp + " requestUrl:" + requestURL);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage() + " IP: " + req.getRemoteAddr() + " URL:" + req.getRequestURL().toString());
        }
        return true;
    }

    /**
     * 查询当前sign是否在黑名单中，并且未过期
     */
    public boolean checkIsInBlacklist(String sign, Object signInBlacklistObj, Map<String, String> blackSettings) {
        if (signInBlacklistObj != null) {
            Map<String, Object> signInBlacklist = JSON.parseObject(signInBlacklistObj.toString(), Map.class);//将sign的攻击信息设置为redis中的攻击信息
            Object attackTimeObj = signInBlacklist.get("attackTime");
            if (attackTimeObj != null) {
                long attackLaterTime = JSON.parseObject(attackTimeObj.toString(), long.class);
                long nowTime = System.currentTimeMillis();
                long l = nowTime - attackLaterTime;
                //获取黑名单有效期（分钟），进行下一步验证
                String blackExpirationTime = blackSettings.get("blackExpirationTime");
                //若当前时间与最后一次攻击时间的差值大于时间戳有效期，则将该sign的攻击信息从redis中删除
                if (l > Long.parseLong(blackExpirationTime) * 60 * 1000) {
                    //若时间戳有效期已过，则清除该sign的攻击信息
                    redisUtil.hdel("requestBlackList", sign);
                    return false;
                } else {
                    //若当前时间与最后一次攻击时间的差值小于时间戳有效期，则判断该sign的攻击次数是否超过系统设置的阈值
                    String attackThreshold = blackSettings.get("attackThreshold");//默认阈值为3
                    Object attackTotalCountObj = signInBlacklist.get("attackTotalCount");
                    if (attackTotalCountObj != null) {
                        String attackTotalCount = JSON.parseObject(attackTotalCountObj.toString(), String.class);
                        //若攻击次数超过阈值，则将该sign的攻击信息设置为redis中的攻击信息
                        return Integer.parseInt(attackTotalCount) >= Integer.parseInt(attackThreshold);
                    }

                }

            }
        }
        return false;
    }

    /**
     * 加密sign(sign&时间戳)
     *
     * @param sign             未加密信息
     * @param timeFormatString 时间格式
     * @return 加密信息(sign & 时间戳)
     */
    @Override
    public String encrypSign(String sign, String timeFormatString) {
        if (StringUtil.isBlank(timeFormatString)) {
            timeFormatString = "yyyyMMddHHmm";
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat(timeFormatString);
        String time = timeFormat.format(new Date());
        DBEncrypt dbEncrypt = new DBEncrypt();
        return dbEncrypt.eCode(sign + "&" + time);
    }

    /**
     * 加密sign(sign&时间戳)
     *
     * @param sign 未加密信息
     * @return 加密信息(sign & yyyyMMddHHmm)
     */
    @Override
    public String encrypSign(String sign) {
        return encrypSign(sign, null);
    }

    /**
     * 根据Ids查询用户列表
     */
    public List<User> getListByIds(User user) {
        return dao.getListByIds(user);
    }

    /**
     * 获取系统设置中的黑名单设置
     */
    public Map<String, String> getBlackSettings() {
        Map<String, String> blackSettings = new HashMap<>();
        Object xtszDtoObj = redisUtil.hget("matridx_xtsz", "repuest.blackSettings");//系统设置中的黑名单相关设置
        if (xtszDtoObj != null) {
            XtszDto xtszDto = JSON.parseObject(xtszDtoObj.toString(), XtszDto.class);
            String blackSettingsStr = xtszDto.getSzz();
            blackSettings = JSON.parseObject(blackSettingsStr, Map.class);
        }
        //若系统设置中没有黑名单相关设置，则使用默认设置
        //secret过期时间（分钟）
        if (StringUtil.isBlank(blackSettings.get("expirationTime"))) {
            blackSettings.put("expirationTime", "30");
        }
        //敏感词
        if (StringUtil.isBlank(blackSettings.get("fobiddensWords"))) {
            blackSettings.put("fobiddensWords", "select,update,insert,delete");
        }
        //黑名单有效期（分钟）
        if (StringUtil.isBlank(blackSettings.get("blackExpirationTime"))) {
            blackSettings.put("blackExpirationTime", "60");
        }
        //访问阈值
        if (StringUtil.isBlank(blackSettings.get("attackThreshold"))) {
            blackSettings.put("attackThreshold", "3");
        }
        return blackSettings;
    }

    /**
     * 根据zsxms获取用户信息
     */
    public List<User> getUsersByYhms(User user) {
        return dao.getUsersByYhms(user);
    }


    /**
     * 根据ddid和wbcxid获取用户信息
     */
    @Override
    public User getByddwbcxid(User user) {
        return dao.getByddwbcxid(user);
    }

    @Override
    public void insertXtyh(String code,String word,String mc){
        User user=new User();
        user.setYhm(code);
        DBEncrypt dBEncrypt = new DBEncrypt();
        BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
        user.setMm(bpe.encode(dBEncrypt.dCode(word)));
        user.setZsxm(mc);
        dao.addClientDetails(user);
        redisUtil.hset("Users", user.getYhm(), JSON.toJSONString(user),-1);
    }

    @Override
    public void updateXtyh(String code, String key, String befordWord,String mc) {
        User user=new User();
        user.setYhm(code);
        DBEncrypt dBEncrypt = new DBEncrypt();
        BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
        user.setMm(bpe.encode(dBEncrypt.dCode(key)));
        user.setYhid(befordWord);
        dao.updateClientDetails(user);
        User user1=redisUtil.hugetDto("Users",befordWord);
        user1.setMm(user.getMm());
        user1.setYhm(code);
        user1.setZsxm(mc);
        redisUtil.hdel("Users",befordWord);
        redisUtil.hset("Users", user1.getYhm(), JSON.toJSONString(user1),-1);
    }

    @Override
    public void deleteXtyh(List<String> ids) {
        User user=new User();
        user.setIds(ids);
        dao.deleteXtyh(user);
        for(String id:ids){
            redisUtil.hdel("Users",id);
        }

    }

    public void addNoClientDdid() {
        List<User> noClientXtyhs = dao.getNoClientDdid();
        DBEncrypt dbEncrypt = new DBEncrypt();
        BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
        for (User noClientXtyh : noClientXtyhs) {
            User user = new User();
            String clientid = noClientXtyh.getDdid();
            if (StringUtil.isNotBlank(noClientXtyh.getMiniappid())) {
                String pretext = "@" + dbEncrypt.dCode(noClientXtyh.getMiniappid());
                clientid = clientid + pretext;
            }
            User t = new User();
            t.setClient_id(clientid);
            List<User> clients = dao.getClientinfo(t);
            if (clients != null && !clients.isEmpty()) {
                log.error("clientid已存在：" + clientid);
                continue;
            }
            user.setYhm(clientid);
            user.setMm(bpe.encode(noClientXtyh.getDdid()));
            dao.addClientDetails(user);
        }
    }

    public void dealClient() {
        List<User> wrongClientinfo = dao.getWrongClientinfo();
        if (wrongClientinfo != null && !wrongClientinfo.isEmpty()) {
            DBEncrypt dbEncrypt = new DBEncrypt();
            BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
            User t = new User();
            for (User user : wrongClientinfo) {
                if (StringUtil.isNotBlank(user.getMiniappid())) {
                    try {
                        String miniappid = dbEncrypt.dCode(user.getMiniappid());
                        String clientid = user.getDdid() + "@" + miniappid;
                        t.setClient_id(clientid);
                        List<User> clients = dao.getClientinfo(t);
                        if (clients != null && !clients.isEmpty()) {
                            log.error("clientid已存在：" + clientid);
                            continue;
                        }
                        user.setYhm(clientid);
                        user.setMm(bpe.encode(user.getDdid()));
                        dao.addClientDetails(user);
                    } catch (Exception e) {
                        log.error("wbcxid解密失败：" + user.getWbcxid());
                    }
                }
            }
        }
    }

    @Override
    public List<User> getOaXtjsList(User user) {
        return dao.getOaXtjsList(user);
    }

    /**
     * 审核人去重
     */
    @Override
    public void sprDeduplication(List<SpgwcyDto> list) {
        if (CollectionUtils.isNotEmpty(list) && list.size() > 1){
            List<String> strings = new ArrayList<>();
            for (int i = list.size()-1; i >= 0 ; i--) {
                if (StringUtil.isBlank(list.get(i).getYhm()) ||  strings.contains(list.get(i).getYhm())){
                    list.remove(i);
                    continue;
                }
                strings.add(list.get(i).getYhm());
            }
        }
    }

    /**
	 * 异常删除redis
	 */
    public void delRedisSse() {
		//获取所有key
		try {
			Set<String> set = redisUtil.getKeys("EXCEPTION_CONNECT:*");
			//要删除得key得list
			List<String> exceptionList = new ArrayList<>();
			for (String key : set) {
				//循环获取每个人下异常list
				Object exceptionConectMap_redis = redisUtil.get(key);
				int unread = 0;
				int finunread = 0;
				if (exceptionConectMap_redis != null) {
					JSONObject exceptionConectMap = JSON.parseObject(exceptionConectMap_redis.toString());
					Map<String, Object> exceptionlistMap = JSON.parseObject(exceptionConectMap.get("exceptionlist").toString(), Map.class);
					Set<String> keyset = exceptionlistMap.keySet();
					//要删除得异常list
					List<String> keylist = new ArrayList<>();
					for (String mapKey : keyset) {
						if (exceptionlistMap.get(mapKey) != null) {
							//比较天数
							JSONObject exceptionMap = JSON.parseObject(exceptionlistMap.get(mapKey).toString());
							String lastUpdateTime = exceptionMap.getString("lastUpdateTime");
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
							SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
							String newlast = time.format(time.parse(lastUpdateTime));
							LocalDate startDate = LocalDate.parse(newlast, formatter);
							LocalDate now = LocalDate.now();
							long days = startDate.until(now, ChronoUnit.DAYS);
							//3天得就要删除
							if (days > 3) {
								keylist.add(mapKey);
							} else {
								if ("0".equals(exceptionMap.get("sfjs").toString())) {
									unread += Integer.parseInt(exceptionMap.get("ex_unreadcnt") == null ? "0" : exceptionMap.get("ex_unreadcnt").toString());
								} else {
									finunread += Integer.parseInt(exceptionMap.get("ex_unreadcnt") == null ? "0" : exceptionMap.get("ex_unreadcnt").toString());
								}
							}
						}

					}
					for (String _key : keylist) {
						exceptionlistMap.remove(_key);
					}
					if (!exceptionlistMap.isEmpty()) {
						exceptionConectMap.put("unreadcount", unread);
						exceptionConectMap.put("finreadcount", finunread);
						exceptionConectMap.put("exceptionlist", exceptionlistMap);
						redisUtil.set(key, JSONObject.toJSONString(exceptionConectMap), -1);
					} else {
						exceptionList.add(key);
					}
				}
			}
			for (String key : exceptionList) {
				redisUtil.del(key);
			}
		} catch (Exception e) {
			log.error("异常redis删除错误");
			log.error(e.getMessage());
		}
	}
    /**
     * 获取超时sql
     */
    public void getTimeOutSql(Map<String,String> map){
        //符合条件的 SQL
        Map<String,Map<String,String>> timeOutSqlMap = new HashMap<>();
        //读取路径
        String readPath = map.get("readPath");
        //写入路径
        String writePath = map.get("writePath");
        //文件名多 ; 拼接
        String[] fileNames = map.get("fileNames").split(";");
        int days = 1;
        //获取几天的日志
        if (StringUtil.isNotBlank(map.get("days"))){
            days = Integer.parseInt(map.get("days"));
        }
        //指定超时时长  毫秒
        long time = Long.parseLong(map.get("time"));
        for (int i = 1; i <= days; i++) {
            Calendar instance = Calendar.getInstance();
            instance.add(Calendar.DATE,-i);
            String logDate = DateUtils.getCustomFomratCurrentDate(instance.getTime(), "yyyy-MM-dd");
            for (String fileName : fileNames) {
                BufferedReader br = null;
                BufferedWriter bw = null;
                try {
                    br = new BufferedReader(new FileReader(readPath+"/"+fileName+"."+logDate+".log"));
                    File outFile = new File(writePath);
                    if (!outFile.exists())
                    {
                        outFile.mkdir();
                    }
                    bw = new BufferedWriter((new FileWriter(writePath + "/" + "(outTimeSql["+time+"])" + fileName + "." + logDate + ".log")));
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    String line;
                    while ((line = br.readLine()) != null) {
                        if ((line.contains("Preparing:")||line.contains("Parameters:")||line.contains("Total:")||line.contains("Updates:"))
                                &&line.contains("[")&&line.contains("]")
                        ) {
                            String thread = line.substring(line.indexOf("["), line.indexOf("]")+1);
                            //子
                            Map<String, String> zhashMap = timeOutSqlMap.get(thread);
                            if (zhashMap!=null){
                                putMapWithLine(dateFormat,line,zhashMap,thread,timeOutSqlMap,time,bw);
                            }else {
                                Map<String, String> hashMap = new LinkedHashMap<>();
                                putMapWithLine(dateFormat, line, hashMap,thread,timeOutSqlMap,time,bw);
                            }
                        }
                    }
                }catch (IOException e){
                    log.error("getTimeOutSql--流异常(1)！异常{},时间{},文件名{}",e.getMessage(),logDate,fileName);
                }catch (ParseException e){
                    log.error("getTimeOutSql--日期解析异常！异常{},时间{},文件名{}",e.getMessage(),logDate,fileName);
                }finally {
                    try {
                        if (br!=null){
                            br.close();
                        }
                        if (bw!=null){
                            bw.close();
                        }
                    } catch (Exception e) {
                        log.error("getTimeOutSql--流异常(2)！异常{},时间{},文件名{}",e.getMessage(),logDate,fileName);
                    }
                }
            }
        }

    }
    /**
     * 将sql放入Map
     */
    private void putMapWithLine(SimpleDateFormat dateFormat, String line, Map<String, String> hashMap,String thread,Map<String,Map<String,String>> timeOutSqlMap,long time,BufferedWriter bw) throws ParseException, IOException {
        if (line.contains("Preparing:")){
            hashMap.put("Preparing:", line);
            hashMap.put("kssj", String.valueOf(dateFormat.parse(line.substring(0,23)).getTime()));
            timeOutSqlMap.put(thread,hashMap);
        }else if (line.contains("Parameters:")){
            hashMap.put("Parameters:", line);
        }else if (line.contains("Total:")){
            hashMap.put("Total:", line);
            dealTimeOutSql(dateFormat, line, hashMap, thread, timeOutSqlMap, time,bw);
        }else if (line.contains("Updates:")){
            hashMap.put("Updates:", line);
            dealTimeOutSql(dateFormat, line, hashMap, thread, timeOutSqlMap, time,bw);
        }

    }
    /**
     * 超时sql输出至文件 否则删除
     */
    private void dealTimeOutSql(SimpleDateFormat dateFormat, String line, Map<String, String> hashMap, String thread, Map<String, Map<String, String>> timeOutSqlMap, long time,BufferedWriter bw) throws ParseException, IOException {
        if (dateFormat.parse(line.substring(0,23)).getTime()-Long.parseLong(hashMap.get("kssj"))> time){
            hashMap.remove("kssj");
            for (String key : hashMap.keySet()) {
                bw.write(hashMap.get(key));
                bw.newLine();
            }
        }
        timeOutSqlMap.remove(thread);
    }
	
    /**
     * 定时执行.sh文件
     */
    public void toExecuteSh(Map<String,String> map){
        try {
        String wjlj=map.get("wjlj");
            String[] wjljs=wjlj.split(",");
            for (String t_wjlj:wjljs){
                Process proc = Runtime.getRuntime().exec(t_wjlj);// 执行py文件
                //用输入输出流来截取结果
                BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                in.close();
                proc.waitFor();
            }
        }catch (Exception e){
            log.error("定时任务执行sh文件/n" + e.getMessage());
        }
    }
    
    /**
	  * 根据角色信息查找该角色的相应的单位限制信息
	  * @return
	  */
    public List<Map<String, String>> getJsjcdwDtoList(Map<String, String> map){
    	return dao.getJsjcdwDtoList(map);
    }

    /**
     * 获取角色列表
     */
    public List<Role> getPagedRoleList(Role role){
        return dao.getPagedRoleList(role);
    }
    /**
     * 处理下载文件名
     */
    public void dealDonwloadFileName(HttpServletResponse response, HttpServletRequest request,String fileName){
        //指明为下载
        response.setHeader("content-type", "application/octet-stream");
        if(fileName != null){
            //获取请求头
            String agent = request.getHeader("user-agent");
            log.error("下载 user-agent:"+agent);
            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            log.error("下载 fileName:"+fileName);
            if (agent.contains("iPhone")){
                //iPhone手机
                if (agent.contains("MicroMessenger")|| agent.contains("micromessenger")){
                    //iPhone 微信内置浏览器
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
                }else {
                    //iPhone Safari浏览器、夸克浏览器、ALOOK浏览器等
                    response.setHeader("Content-Disposition","attachment;filename*=UTF-8''" + fileName);
                }
            } else if (agent.contains("Firefox")){
                //Firefox   Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/109.0
                response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));// 文件名外的双引号处理firefox的空格截断问题

            } else if (agent.contains("MSIE") || agent.contains("Trident")){
                //IE6   Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)
                //IE11  Mozilla/5.0 (Windows NT 6.3; Win64, x64; Trident/7.0; rv:11.0) like Gecko
                try {
                    fileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
                    fileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
                    fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
                    response.setHeader("Content-Disposition","attachment;filename*=UTF-8''" + fileName);
                } catch (UnsupportedEncodingException e) {
                    response.setHeader("Content-Disposition","attachment;filename*=UTF-8''" + fileName);
                }
            }else {
                //Edge浏览器 Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36 Edge/17.17134
                //Edge浏览器 Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 Edg/122.0.0.0
                //Chrome浏览器 Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36
                //PC微信内置浏览器 Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36 NetType/WIFI MicroMessenger/7.0.20.1781(0x6700143B) WindowsWechat(0x6309092b) XWEB/9079 Flue
                //PC钉钉内置浏览器 Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36
                response.setHeader("Content-Disposition","attachment;filename*=UTF-8''" + fileName);
            }
        }
    }

    @Override
    public List<Map<String, Object>> getWksjmxList(Map<String, String> map) {
        return dao.getWksjmxList(map);
    }

    /**
	* 获取审核人和检验人List
    * @param yh
    * @return
     */
    @Override
    public List<User> getShryAndJyryList(User yh){
        XxdyDto xxdyDto=new XxdyDto();
        xxdyDto.setDylxcsdm("YHSHJYQX");
        xxdyDto.setDyxxid(yh.getYhid());
        List<XxdyDto> xxdyDtos=xxdyService.getDtoList(xxdyDto);
        xxdyDto.setDylxcsdm("JSSHJYQX");
        xxdyDto.setDyxxid(yh.getDqjs());
        List<XxdyDto> js_xxdyDtos=xxdyService.getDtoList(xxdyDto);
        List<User> xtyhDtos=new ArrayList<>();
        List<User> xtyhDtoList=new ArrayList<>();
        User user = new User();
        user.setSfsd("0");
        user.setMrfz("1");
        if(!CollectionUtils.isEmpty(xxdyDtos) || !CollectionUtils.isEmpty(js_xxdyDtos)){
            List<String> yhms=new ArrayList<>();
            if(!CollectionUtils.isEmpty(xxdyDtos)){
                String t_yhms=xxdyDtos.get(0).getKzcs1();//默认取第一条的cskz1
		        if(StringUtil.isNotBlank(t_yhms)){
                    String[] sz_yhms=t_yhms.split(",");
                    List<String> yh_yhms= Arrays.asList(sz_yhms);
                    for(String yh_yhm:yh_yhms){
                        yhms.add(yh_yhm);
					}
                }
            }
            if(!CollectionUtils.isEmpty(js_xxdyDtos)) {
                String t_jsms = js_xxdyDtos.get(0).getKzcs1();//默认取第一条的cskz1
                if (StringUtil.isNotBlank(t_jsms)) {
                    String[] sz_jsms = t_jsms.split(",");
                    List<String> js_yhms = Arrays.asList(sz_jsms);
                    for (String js_yhm : js_yhms) {
                        yhms.add(js_yhm);
                    }
                }
            }
            user.setYhms(yhms);
            List<User> t_xtyhDtos = getListByYhm(user);
            if(!CollectionUtils.isEmpty(t_xtyhDtos)){
                for(User xtyhDto1:t_xtyhDtos){
					if("ALL".equals(xtyhDto1.getMrfz())){
                        User shryDto=new User();
                        shryDto.setMrfz("SHRY");
                        shryDto.setYhid(xtyhDto1.getYhid());
                        shryDto.setYhm(xtyhDto1.getYhm());
                        shryDto.setZsxm(xtyhDto1.getZsxm());
                        xtyhDtos.add(shryDto);
                        User jyryDto=new User();
                        jyryDto.setMrfz("JYRY");
                        jyryDto.setYhid(xtyhDto1.getYhid());
                        jyryDto.setYhm(xtyhDto1.getYhm());
                        jyryDto.setZsxm(xtyhDto1.getZsxm());
                        xtyhDtos.add(jyryDto);
                    }else if (StringUtil.isNotBlank(xtyhDto1.getMrfz())){
                        xtyhDtos.add(xtyhDto1);
                    }
                }
            }
        }
        if (CollectionUtils.isEmpty(xtyhDtos)){
            xtyhDtos = getListByIds(user);
            user.setMrfz("ALL");
            xtyhDtoList= getListByIds(user);
            for(User xtyhDto1:xtyhDtoList){
                User shryDto=new User();
                shryDto.setMrfz("SHRY");
                shryDto.setYhm(xtyhDto1.getYhm());
                shryDto.setZsxm(xtyhDto1.getZsxm());
                xtyhDtos.add(shryDto);
                User jyryDto=new User();
                jyryDto.setMrfz("JYRY");
                jyryDto.setYhm(xtyhDto1.getYhm());
                jyryDto.setZsxm(xtyhDto1.getZsxm());
                xtyhDtos.add(jyryDto);
            }
        }
 		return xtyhDtos;
    }
	/**
     * @Description: 共通方法，查询是否订阅消息
     * @param ywid 用户id或伙伴id
     * @param xxid 消息id
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/11/27 14:51
     */
    @Override
    public boolean
    queryAuthMessage(String ywid, String xxid) {
        boolean result = true;
        if(StringUtil.isNotBlank(ywid) && StringUtil.isNotBlank(xxid)){
            Object object = redisUtil.hget("Grsz:"+ywid,xxid);
            if(object!=null){
                result = "1".equals(String.valueOf(object));
            }else{
                GrszDto dto = new GrszDto();
                dto.setYhid(ywid);
                dto.setGlxx(xxid);
                GrszDto grszDto = grszService.getDtoByYhidAndGlxx(dto);
                if(grszDto!=null){
                    result = "1".equals(grszDto.getSzz());
                    redisUtil.hset("Grsz:"+ywid,xxid,grszDto.getSzz(),-1);
                }else {
                    Object objectMr = redisUtil.hget("Grsz:MR",xxid);
                    if(objectMr!=null){
                        result = "1".equals(String.valueOf(objectMr));
                        redisUtil.hset("Grsz:"+ywid,xxid,String.valueOf(objectMr),-1);
                    }else {
                        GrszDto dtoMr = new GrszDto();
                        dtoMr.setYhid("MR");
                        dtoMr.setGlxx(xxid);
                        GrszDto grszDtoMr = grszService.getDtoByYhidAndGlxx(dtoMr);
                        if(grszDtoMr!=null){
                            result = "1".equals(grszDtoMr.getSzz());
                            redisUtil.hset("Grsz:"+"MR",xxid,grszDtoMr.getSzz(),-1);
                            redisUtil.hset("Grsz:"+ywid,xxid,grszDtoMr.getSzz(),-1);
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * @Description: 计算审核用时
     * @param ywid
     * @param szlb
     * @return java.lang.String
     * @Author: 郭祥杰
     * @Date: 2025/1/22 15:38
     */
    @Override
    public String getCommonAuditTime(String ywid,String gwid, String szlb) {
        String result = "";
        if(StringUtil.isNotBlank(ywid) && StringUtil.isNotBlank(szlb)){
            XtszDto xtszDto = xtszService.selectById(szlb);
            XtszDto attendanceTime = xtszService.selectById("attendance.time");
            if(xtszDto!=null && attendanceTime!=null){
                if(StringUtil.isNotBlank(xtszDto.getSzz()) && StringUtil.isNotBlank(attendanceTime.getSzz())){
                    Map<String, Object> map = JSON.parseObject(xtszDto.getSzz(), new TypeReference<Map<String, Object>>(){});
                    Map<String, Object> attendanceTimeMap = JSON.parseObject(attendanceTime.getSzz(), new TypeReference<Map<String, Object>>(){});
                    if(map.get("postStart")!=null && map.get("postEnd")!=null && attendanceTimeMap.get("officeHours")!=null
                            && attendanceTimeMap.get("closingTime")!=null && attendanceTimeMap.get("beginTime")!=null  && attendanceTimeMap.get("endTime")!=null ){
                        ShxxDto shxxDto = new ShxxDto();
                        shxxDto.setPostStart(map.get("postStart").toString());
                        shxxDto.setPostEnd(map.get("postEnd").toString());
                        shxxDto.setYwid(ywid);
                        ShxxDto shxxDtoT = shxxService.queryShsj(shxxDto);
                        if(shxxDtoT!=null){
                            if(StringUtil.isNotBlank(gwid)){
                                SpgwDto spgwDto = spgwService.getDtoById(gwid);
                                if(spgwDto!=null && map.get("postEnd").toString().equals(spgwDto.getGwmc())){
                                    LocalDateTime currentDateTime = LocalDateTime.now();
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                    String formattedDateTime = currentDateTime.format(formatter);
                                    shxxDtoT.setPostEnd(formattedDateTime);
                                    BigDecimal timeLag = getHoursByD(shxxDtoT.getPostStart(),shxxDtoT.getPostEnd());
                                    shxxDtoT.setTimeLag(timeLag.toString());
                                }
                            }
                            result = getAuditTime(shxxDtoT.getPostStart(),shxxDtoT.getPostEnd(),shxxDtoT.getTimeLag(),
                                    attendanceTimeMap.get("officeHours").toString(),
                                    attendanceTimeMap.get("closingTime").toString(),
                                    attendanceTimeMap.get("beginTime").toString(),
                                    attendanceTimeMap.get("endTime").toString());
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * @Description: 计算审核时间
     * @param postStart
     * @param postEnd
     * @param timeLag
     * @param officeHours
     * @param closingTime
     * @param beginTime
     * @param endTime
     * @return java.lang.String
     * @Author: 郭祥杰
     * @Date: 2025/1/23 15:32
     */
    @Override
    public String getAuditTime(String postStart,String postEnd,String timeLag,String officeHours,String closingTime,String beginTime,String endTime) {
        Map<String,Object> shysMap = new HashMap<>();
        Map<String,Object> hoursMap = new HashMap<>();
        Map<String,Object> dayMap = new HashMap<>();
        hoursMap.put("0",timeLag);
        BigDecimal dayTime = new BigDecimal(timeLag).divide(new BigDecimal("24"),2,RoundingMode.HALF_UP );
        dayMap.put("0",dayTime.toString());
        //获取节假日
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Timestamp startStamp = Timestamp.valueOf(postStart);
        String startString = sdf.format(startStamp);
        Timestamp endStamp = Timestamp.valueOf(postEnd);
        String endString = sdf.format(endStamp);
        List<LocalDate> localDates = getAllDates(startString,endString);
        XxdyDto xxdyDto = new XxdyDto();
        xxdyDto.setDylxcsdm("Holidays");
        xxdyDto.setBeginTime(startString);
        xxdyDto.setEndTime(endString);
        List<XxdyDto> xxdyDtoList = xxdyService.queryHolidays(xxdyDto);
        BigDecimal timeString = new BigDecimal(timeLag);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        //计算每天非工作时间的小时数
        BigDecimal dayHouts = getHours(officeHours,closingTime).subtract(getHours(beginTime,endTime));
        BigDecimal everyDayHours = new BigDecimal("24").subtract(dayHouts);
        BigDecimal workDayHours = new BigDecimal("0");
        LocalDateTime dateTime = LocalDateTime.parse(postStart, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(postEnd, formatter);
        String outputString = dateTime.format(outputFormatter);
        String endOutputString = endDateTime.format(outputFormatter);
        LocalDate localDate = dateTime.toLocalDate();
        LocalDate endLocalDate = endDateTime.toLocalDate();
        BigDecimal dayTime2 = new BigDecimal("0");
        if (localDate.isEqual(endLocalDate)) {
            if(!CollectionUtils.isEmpty(xxdyDtoList)){
                timeString = new BigDecimal("0");
            }else {
                timeString = oneDayGetHours(outputString,endOutputString,officeHours,closingTime,beginTime,endTime);
            }
        }else{
            if(!CollectionUtils.isEmpty(xxdyDtoList)){
                int i = 0;
                BigDecimal sumTime = new BigDecimal("0");
                boolean startFlg = false;
                boolean endFlg = false;
                for (XxdyDto xxdy:xxdyDtoList){
                    if("1".equals(xxdy.getFlag())){
                        if(xxdy.getDyxx().equals(startString)){
                            sumTime = sumTime.add(new BigDecimal("24").subtract(calculateTime(startStamp)));
                            startFlg = true;
                        }
                        if(xxdy.getDyxx().equals(endString)){
                            sumTime = sumTime.add(calculateTime(endStamp));
                            endFlg = true;
                        }
                    }else{
                        i = i+1;
                    }
                }
                //去除节假日，星期天
                timeString = timeString.subtract(sumTime).subtract(new BigDecimal(i).multiply(new BigDecimal("24")));
                int l = localDates.size()-xxdyDtoList.size();
                if(!startFlg){
                    workDayHours = workDayHours.add(beginDayGetHours(outputString,officeHours,closingTime,beginTime,endTime));
                    l = l - 1;
                    timeString = timeString.subtract(new BigDecimal("24").subtract(calculateTime(startStamp)));
                }
                if(!endFlg){
                    workDayHours = workDayHours.add(endDayGetHours(endOutputString,officeHours,closingTime,beginTime,endTime));
                    l = l - 1;
                    timeString = timeString.subtract(calculateTime(endStamp));
                }
                String dayString = String.valueOf(l);
                timeString = timeString.add(workDayHours).subtract(everyDayHours.multiply(new BigDecimal(dayString)));
            }else{
                //获取审核之间天数
                String dayInt = String.valueOf(localDates.size()-2);
                workDayHours = workDayHours.add(endDayGetHours(endOutputString,officeHours,closingTime,beginTime,endTime));
                workDayHours = workDayHours.add(beginDayGetHours(outputString,officeHours,closingTime,beginTime,endTime));
                timeString = timeString.subtract(new BigDecimal(dayInt).multiply(everyDayHours))
                        .subtract(new BigDecimal("24").subtract(calculateTime(startStamp)))
                        .subtract(calculateTime(endStamp))
                        .add(workDayHours);
                dayTime2=timeString.divide(dayHouts,2,RoundingMode.HALF_UP );
            }
        }
        hoursMap.put("1",timeString.toString());
        dayMap.put("1",dayTime2.toString());
        shysMap.put("hour",hoursMap);
        shysMap.put("day",dayMap);
        return JSON.toJSONString(shysMap);
    }


    /**
     * @Description: 计算当前时间过了多少个小时,13点距离00点过了13小时
     * @param timestamp
     * @return java.math.BigDecimal
     * @Author: 郭祥杰
     * @Date: 2025/1/22 15:07
     */
    @Override
    public BigDecimal calculateTime(Timestamp timestamp){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timestamp);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new BigDecimal(timestamp.getTime() - calendar.getTimeInMillis()).divide(new BigDecimal("3600000"),2,RoundingMode.HALF_UP);
    }

    /**
     * @Description: 获取两个时间段之内所有日期
     * @param startString
     * @param endString
     * @return java.util.List<java.time.LocalDate>
     * @Author: 郭祥杰
     * @Date: 2025/1/22 15:07
     */
    @Override
    public List<LocalDate> getAllDates(String startString,String endString){
        List<LocalDate> dates = new ArrayList<>();
        if(StringUtil.isNotBlank(startString) && StringUtil.isNotBlank(endString)){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(startString, formatter);
            LocalDate endDate = LocalDate.parse(endString, formatter);
            LocalDate currentDate = startDate;
            while (!currentDate.isAfter(endDate)) {
                dates.add(currentDate);
                currentDate = currentDate.plusDays(1); // 递增日期
            }
        }
        return dates;
    }

    /**
     * @Description: 获取两个时间段内的所有日期
     * @param startString
     * @param endString
     * @return java.math.BigDecimal
     * @Author: 郭祥杰
     * @Date: 2025/1/22 15:08
     */
    @Override
    public BigDecimal getHours(String startString,String endString) {
        BigDecimal result = new BigDecimal("0");
        if (StringUtil.isNotBlank(startString) && StringUtil.isNotBlank(endString)) {
            LocalTime startTime = LocalTime.parse(startString);
            LocalTime endTime = LocalTime.parse(endString);
            Duration duration = Duration.between(startTime, endTime);
            result = new BigDecimal(duration.getSeconds()).divide(new BigDecimal("3600"),2, RoundingMode.HALF_UP);
        }
        return result;
    }

    @Override
    public BigDecimal getHoursByD(String startString, String endString) {
        BigDecimal hoursBetween = new BigDecimal("0");
        if(StringUtil.isNotBlank(startString) && StringUtil.isNotBlank(endString)){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime1 = LocalDateTime.parse(startString, formatter);
            LocalDateTime dateTime2 = LocalDateTime.parse(endString, formatter);

            hoursBetween = new BigDecimal(ChronoUnit.HOURS.between(dateTime1, dateTime2))
                    .add((new BigDecimal(dateTime2.getMinute())
                            .add(new BigDecimal(dateTime2.getSecond())
                                    .divide(new BigDecimal("60.0"),2, RoundingMode.HALF_UP))
                            .subtract(new BigDecimal(dateTime1.getMinute()))
                            .subtract(new BigDecimal(dateTime1.getSecond())
                                    .divide(new BigDecimal("60.0"),2, RoundingMode.HALF_UP)))
                            .divide(new BigDecimal("60.0"),2, RoundingMode.HALF_UP));
        }
        return hoursBetween;
    }

    /**
     * @Description: 比较两个日期大小
     * @param startString
     * @param endString
     * @return int
     * @Author: 郭祥杰
     * @Date: 2025/1/22 15:09
     */
    private int compareTo(String startString,String endString){
        int i=0;
        LocalTime startTime = LocalTime.parse(startString);
        LocalTime endTime = LocalTime.parse(endString);
        if (startTime.isBefore(endTime)) {
            i=-1;
        }else if (startTime.isAfter(endTime)){
            i=1;
        }
        return i;
    }

    /**
     * @Description: 计算开始审核的当天用时，去除下班时间
     * @param timeString
     * @param officeHours
     * @param closingTime
     * @param beginTime
     * @param endTime
     * @return java.math.BigDecimal
     * @Author: 郭祥杰
     * @Date: 2025/1/22 15:10
     */
    @Override
    public BigDecimal beginDayGetHours(String timeString,String officeHours,String closingTime,String beginTime,String endTime){
        BigDecimal hours = new BigDecimal("0");
        int closingInt = compareTo(timeString,closingTime);//下班时间比较
        int endInt = compareTo(timeString,endTime);//午休结束时间比较
        int beginInt = compareTo(timeString,beginTime);//午休开始时间比较
        int officeInt = compareTo(timeString,officeHours);//上班时间比较
        if(closingInt<0){ //下班前
            if(endInt<0){ //午休结束前
                if(beginInt<0){ //午休开始前
                    if(officeInt<0){ //上班前，（下班时间-上班时间）-（午休结束时间-午休开始时间）
                        hours = getHours(officeHours,closingTime).subtract(getHours(beginTime,endTime));
                    }else { //上班后，午休开始前，（下班时间-审核时间）-（午休结束时间-午休开始时间）
                        hours = getHours(timeString,closingTime).subtract(getHours(beginTime,endTime));
                    }
                }else{ //午休开始后，午休结束前，下班时间-午休结束时间
                    hours = getHours(endTime,closingTime);
                }
            }else{ //午休结束后,下班时间-审核时间
                hours = getHours(timeString,closingTime);
            }
        }
        return hours;
    }

    @Override
    public BigDecimal oneDayGetHours(String timeString,String endTimeString, String officeHours, String closingTime, String beginTime, String endTime) {
        BigDecimal hours = new BigDecimal("0");
        int closingInt = compareTo(timeString,closingTime);//下班时间比较
        int endInt = compareTo(timeString,endTime);//午休结束时间比较
        int beginInt = compareTo(timeString,beginTime);//午休开始时间比较
        int officeInt = compareTo(timeString,officeHours);//上班时间比较

        int closingInt2 = compareTo(endTimeString,closingTime);//下班时间比较，小于0,A小于B
        int endInt2 = compareTo(endTimeString,endTime);//午休结束时间比较
        int beginInt2 = compareTo(endTimeString,beginTime);//午休开始时间比较
        int officeInt2 = compareTo(endTimeString,officeHours);//上班时间比较

        if(closingInt<0){ //开始时间下班前
            if(endInt<0){ //开始时间午休结束前
                if(beginInt<0){ //午休开始前
                    if(officeInt<0){ //上班前
                        if(closingInt2<0){//结束时间，下班前
                            if(endInt2<0){//结束时间，午休结束前
                                if(beginInt2<0){//午休开始前
                                    if(officeInt2>0){
                                        hours = getHours(officeHours,endTimeString);
                                    }
                                }else{//午休开始后
                                    hours = getHours(officeHours,beginTime);
                                }
                            }else{ //下班前,午休结束后
                                hours = getHours(officeHours,endTimeString).subtract(getHours(beginTime,endTime));
                            }
                        }else{ //结束时间，下班后
                            hours = getHours(officeHours,closingTime).subtract(getHours(beginTime,endTime));
                        }
                    }else { //上班后，午休开始前
                        if(closingInt2<0){//结束时间，下班前
                            if(endInt2<0){//结束时间，午休结束前
                                if(beginInt2<0){//午休开始前
                                    hours = getHours(timeString,endTimeString);
                                }else{//午休开始后
                                    hours = getHours(timeString,beginTime);
                                }
                            }else{ //午休结束后，下班前
                                hours = getHours(timeString,endTimeString).subtract(getHours(beginTime,endTime));
                            }
                        }else{ //结束时间，下班后
                            hours = getHours(timeString,closingTime).subtract(getHours(beginTime,endTime));
                        }
                    }
                }else{ //午休开始后，午休结束前
                    if(closingInt2<0 ){//判断结束时间是否在下班前
                        if(endInt2>0){//判断结束时间是否在午休后
                            hours = getHours(endTime,endTimeString);
                        }
                    }else{
                        hours = getHours(endTime,closingTime);//午休结束到下班
                    }
                }
            }else{ //午休结束后,
                if(closingInt2<0){//判断结束时间是否是在下班前
                    hours= getHours(timeString,endTimeString);
                }else{
                    hours= getHours(timeString,closingTime);
                }
            }
        }
        return hours;
    }
    /**
     * @Description: 日志记录错误数量,如果发送成功，state=true,否则false
     * @param str
     * @param errorKey
     * @param logString
     * @param state
     * @return java.lang.String
     * @Author: 郭祥杰
     * @Date: 2025/4/28 15:26
     */
    @Override
    public String logErrorNumMap(String str,String errorKey,String logString,boolean state) {
        if(StringUtil.isNotBlank(str)) {
            Map<String, Object> map = JSON.parseObject(str, new TypeReference<Map<String, Object>>() {
            });
            if(map!=null){
                if(state){
                    logString = logString+(map.get(errorKey)!=null?map.get(errorKey).toString():"");
                    log.error(logString);
                }else {
                    if(map.get(errorKey)==null){
                        log.error(logString);
                    }
                }
                map.put(errorKey,Integer.parseInt((map.get("errorNum")==null?"0":map.get("errorNum")).toString())+1);
                return JSONObject.toJSONString(map);
            }
        }
        return null;
    }


    /**
     * @Description: 计算完成审核的当天用时，去除下班时间
     * @param timeString
     * @param officeHours
     * @param closingTime
     * @param beginTime
     * @param endTime
     * @return java.math.BigDecimal
     * @Author: 郭祥杰
     * @Date: 2025/1/22 15:10
     */
    @Override
    public BigDecimal endDayGetHours(String timeString,String officeHours,String closingTime,String beginTime,String endTime){
        BigDecimal hours = new BigDecimal("0");
        int officeInt = compareTo(timeString,officeHours);//上班时间比较
        int beginInt = compareTo(timeString,beginTime);//午休开始时间比较
        int endInt = compareTo(timeString,endTime);//午休结束时间比较
        int closingInt = compareTo(timeString,closingTime);//下班时间比较
        if (officeInt > 0) { //上班后
            if(beginInt>0){ //午休开始后
                if(endInt>0){ //午休结束后
                    if(closingInt>0){ //下班后，（下班时间-上班时间）-（午休结束-午休开始）
                        hours = getHours(officeHours,closingTime).subtract(getHours(beginTime,endTime));
                    }else { //午休结束后，下班前，（审核时间-上班时间）-(午休结束-午休开始)
                        hours = getHours(officeHours,timeString).subtract(getHours(beginTime,endTime));
                    }
                }else{ //午休开始后，午休结束前（午休开始时间-上班时间）
                    hours = getHours(officeHours,beginTime);
                }
            }else{ //上班后，午休开始前，（审核时间-上班时间）
                hours = getHours(officeHours,timeString);
            }
        }
        return hours;
    }
}