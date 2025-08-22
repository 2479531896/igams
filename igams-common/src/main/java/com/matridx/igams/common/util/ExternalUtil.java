package com.matridx.igams.common.util;
import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.entities.external.ConsumeAmount;
import com.matridx.igams.common.dao.entities.external.Expense;
import com.matridx.igams.common.dao.entities.external.ExpenseRequestDto;
import com.matridx.igams.common.dao.entities.external.MkResponseDto;
import com.matridx.igams.common.redis.RedisUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WYX
 * @version 1.0
 * @className ExternalUtil
 * @description TODO
 * @date 9:45 2023/4/23
 **/
@Configuration
public class ExternalUtil {
    @Autowired
    RedisUtil redisUtil;
    @Value("${matridx.mk.appCode:}")
    private String MK_APPCODE;
    @Value("${matridx.mk.appSecret:}")
    private String MK_APPSECRET;
    @Value("${matridx.mk.url:}")
    private String MK_URL;
    /**
     * @description 该接口用于进行费用导入，可以用于将其他来源的费用导入到每刻系统中。导入费用成功后，将返回每刻系统内的唯一费用标识Code，
     * 该Code可用于报销单导入时，作为费用expenseCodes的参数使用《单据导入接口》
     */
    public final String MK_RECEIVE_EXPENSE = "/api/openapi/receive/expense";
    /**
     * @description 报销单导入接口
     */
    public final String MK_RECEIVE_REIMBURSE = "/api/openapi/receive/reimburse";
    /**
     * @description 获取token
     */
    public final String MK_TOKEN = "/api/openapi/auth/login";

    private final Logger logger = LoggerFactory.getLogger(DingTalkUtil.class);



    /**
     * @description 由于URL中，是不支持直接填写中文的，所以中文需要进行编码。
     * 编码前URL
     * https://dt-uat.maycur.com/api/openapi/form/reimburse/报销20030301
     * 编码后URL
     * https://dt-uat.maycur.com/api/openapi/form/reimburse/%E6%8A%A5%E9%94%8020030301
     * @param
     * @return
     */
    private String urlEncoder(String str) {
        return URLEncoder.encode(str, StandardCharsets.UTF_8);
    }
    /**
     * @description
     * 在每刻调用您的接口时，为了保证数据在推送过程中没有发生篡改行为，会在head中添加sign字段以表示此次responseBody的签名来保证数据没有发生篡改行为，
     * 用户在接收每刻推送的数据前应该对requestBody按照以下验证方式获取签名值和head中的签名值做比对，如一致则认为数据没有被篡改过。
     * 获取签名数据后，根据下面的Sign方法获取签名的sign值并和每刻推送的数据中的head中的sign值做比较，如一致则认为请求合法
     * @param key  value 首先根据每刻分配的secret（此秘钥为单独分配，非鉴权接口所使用的的appSecret）对requestBody的数据进行签名，
     *  签名方式如下，value为requestBody中的数据，key为每刻分配的secret。
     *  注: 用户在接收到requestBody时不应该做任何对象转换，可以考虑直接用String去接收，先将requestBody做签名验证，然后再将requestBody转换成用户自己需要的对象
     * @return
     */
    public String encryptCbcMode(final String value, String key) {
        if (StringUtils.isNotBlank(value)) {

            final SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            //初始化向量器
            final IvParameterSpec ivParameterSpec = new IvParameterSpec(Arrays.copyOfRange(key.getBytes(), 0, 16));

            try {
                //加密模式
                Cipher encipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                encipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
                //加密
                byte[] encrypted = encipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
                //然后转成BASE64返回
                return Base64.encodeBase64String(encrypted);
            } catch (Exception e) {
                logger.error("使用AES加密失败, errorMsg:{}, cause:{}", e.getMessage(), e.getCause());
            }
        }
        return value;
    }

    public String encryptCbcMode_new(final String value, String key) {
        if (StringUtils.isNotBlank(value)) {

            final SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            //初始化向量器
            final IvParameterSpec ivParameterSpec = new IvParameterSpec(key.getBytes());

            try {
                //加密模式
                Cipher encipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                encipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
                //加密
                byte[] encrypted = encipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
                //然后转成BASE64返回
                return Base64.encodeBase64String(encrypted);
            } catch (Exception e) {
                logger.error("使用AES加密失败, errorMsg:{}, cause:{}", e.getMessage(), e.getCause());
            }
        }
        return value;
    }

    /**
     *	json是签名后的数据
     */
    @SuppressWarnings("unchecked")
    public static int getHashValue(String encryptValue) {
        return Hashing.md5().newHasher().putString(encryptValue, Charsets.UTF_8).hash().asInt();
    }
    /**
     * @description 该接口为部分失败，在进行费用导入时，如果一次导入5条费用，其中一条失败，不会影响其他四条数据的正常导入。
     *  支持导入对公未到票、对公全部到票、对私费费用场景，仅支持部分主要字段。
     *  每次导入最大值为10条费用，导入过多费用时，每刻会将多出的费用进行遗弃。
     *  如需在费用上关联发票，可以在费用导入成功后，将返回的expenseCode添加到发票导入接口的入参内。
     *  根据返回的integrity可以识别该费用是否必填项传入完整
     *     ■ true
     *       ● 所有必填项均传入成功
     *     ■ false
     *       ● 接口可导入的必填项未导入完整
     *       ● 不支持接口导入，但表单设置必填（此情况需要设置单据导入为草稿或者去除费用上对应组件的必填设置）
     * @param
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean receiveReimburse(String param){
        Map<String, Object> tokenMap = getToken();
        RestTemplate restTemplate = new RestTemplate();
        // 构建你的请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("tokenId",String.valueOf(tokenMap.get("tokenId")));
        headers.set("entCode",String.valueOf(tokenMap.get("entCode")));
        // 组合请求头与请求体参数
        HttpEntity<String> requestEntity = new HttpEntity<>(param, headers);
        System.out.println(param);
        System.out.println(requestEntity);
        MkResponseDto mkResponseDto = restTemplate.postForObject(MK_URL + MK_RECEIVE_REIMBURSE, requestEntity, MkResponseDto.class);
        if (mkResponseDto.isSuccess()){
            return true;
        }else {
            logger.error("receiveReimburse--"+JSON.toJSONString(mkResponseDto));
            return false;
        }
    }
    /**
     * @description 报销单费用导入
     * 1. 若导入时，填写formCode，则会校验是否和每刻系统内单据号重复（每刻内单据作废/删除，不会释放原单据号）。
     * 2. 若导入报销单stagingFlag(暂存状态)为true，则只校验费用S级字段必填（对私：消费日期、费用金额、费用类型编码；对公：基于对私，增加到票时间、业务场景、是否对公），
     *      其他必填字段由提单人手动添加并提交。
     * 3. 单据导入成功且为提交状态，将会自动执行该单据所配置的审批流程，如果需要将单据直接完成，可以直接将单据配置为空流程（即没有审批人）。
     * 4. 单据导入成功且为提交状态，会正常触发预算、凭证、费控占用、智能审核等功能，与员工手动提单相同
     * @param param
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<String> receivExpense(String param){
        Map<String, Object> tokenMap = getToken();
        RestTemplate restTemplate = new RestTemplate();
        // 构建你的请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("tokenId",String.valueOf(tokenMap.get("tokenId")));
        headers.set("entCode",String.valueOf(tokenMap.get("entCode")));
        headers.set("Content-Type", "application/json");
        // 组合请求头与请求体参数
        HttpEntity<String> requestEntity = new HttpEntity<>(param, headers);
        MkResponseDto mkResponseDto = restTemplate.postForObject(MK_URL + MK_RECEIVE_EXPENSE, requestEntity, MkResponseDto.class);
        if (mkResponseDto.isSuccess()){
            Map<String,Object> data = (Map<String, Object>) mkResponseDto.getData();
            List<Map<String,Object>> mapList = (List<Map<String, Object>>) data.get("successData");
            List<String> list = new ArrayList<>();
            for (Map<String, Object> map : mapList) {
                list.add(String.valueOf(map.get("expenseCode")));
            }
            return list;
        }else {
            logger.error("receivExpense--"+JSON.toJSONString(mkResponseDto));
            return null;
        }
    }
    public void test(){
        Map<String, Object> tokenMap = getToken();
        ExpenseRequestDto expenseRequestDto = new ExpenseRequestDto();
        expenseRequestDto.setEmployeeId("M1045");
        Expense expense = new Expense();
        expense.setConsumeAmount(new ConsumeAmount(Double.parseDouble("555.55"),"CNY"));
        //是否对公费用
        expense.setCorpExpense(true);
        //行政费用19003；行政采购类别编码见群里附件；采购货款19002；
        expense.setExpenseTypeBizCode("19002");
        expense.setCorpType("ALL_RECEIPTS");
        //到票时间
        expense.setReceiptDate(new Date().getTime());
        //支付对象
        expense.setTradingPartnerBizCode("0000002");
        Map<String, Object> map = new HashMap<>();
        map.put("CF47","IT-0001");
        expense.setCustomObject(map);
        List<Expense> expenseList = new ArrayList<>();
        expenseList.add(expense);
        expenseRequestDto.setExpenseList(expenseList);

        RestTemplate restTemplate = new RestTemplate();
        // 构建你的请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("tokenId",String.valueOf(tokenMap.get("tokenId")));
        headers.set("entCode",String.valueOf(tokenMap.get("entCode")));
        headers.set("Content-Type", "application/json");
        // 组合请求头与请求体参数
        HttpEntity<String> requestEntity = new HttpEntity<>(JSON.toJSONString(expenseRequestDto), headers);
        MkResponseDto mkResponseDto = restTemplate.postForObject(MK_URL + MK_RECEIVE_EXPENSE, requestEntity, MkResponseDto.class);
        System.out.println(JSON.toJSONString(mkResponseDto));
    }
    /**
     * @description
     * 1. 用户向每刻申请openAPI账户
     * 2. 使用申请成功后获得的账户信息，通过登录认证接口获取token信息以及企业ID
     * 3. 登录成功后，TokenId的有效期为30分钟，请勿频繁请求，会被禁用
     * 4.  调用其他业务接口时必须将本接口返回的entCode和tokenId放入请求header中。
     * @param
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getToken(){
        Object mk_token = redisUtil.get("mk_token");
        if (mk_token!=null){
            return JSON.parseObject(String.valueOf(mk_token),Map.class);
        }else {
            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> map = new HashMap<>();
            long timestamp = new Date().getTime();
            String secret = DigestUtils.sha256Hex(MK_APPSECRET + ":" + MK_APPCODE + ":" + timestamp);
            map.put("secret", secret);
            map.put("appCode", MK_APPCODE);
            map.put("timestamp", timestamp);
            // 构建你的请求头
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            // 组合请求头与请求体参数
            HttpEntity<String> requestEntity = new HttpEntity<>(JSON.toJSONString(map), headers);
            Map<String, Object> result = restTemplate.postForObject(MK_URL + MK_TOKEN, requestEntity, Map.class);
            Map<String, String> data = (Map<String, String>) result.get("data");
            map.put("tokenId", data.get("tokenId"));
            map.put("entCode", data.get("entCode"));
            redisUtil.set("mk_token", JSON.toJSONString(map), 1200);
            return map;
        }
    }

}
