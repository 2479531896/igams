package com.matridx.las.frame.connect.util;
import com.lop.open.api.sdk.internal.fastjson.JSON;
import com.matridx.las.frame.controller.LasAutoMationMainController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import javax.annotation.PostConstruct;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
@Component
public class ConnectUtil {
    @Autowired
    private RestTemplate restTemplate;
    private static RestTemplate t_restTemplate;
    @PostConstruct
    public void initRestTemplate(){
        t_restTemplate = this.restTemplate;
    }
    //主服务器注册地址
    private static String defaultZone;
    @Value("${matridx.netty.defaultZone:}")
    public void setDefaultZone(String defaultZone) {
        ConnectUtil.defaultZone = defaultZone;
    }
    static Logger log = LoggerFactory.getLogger(LasAutoMationMainController.class);
    
    /**
     * 调用网络连接进行访问
     * @param url
     * @param param
     * @param parameterTypes
     */
	public static <T> T sendConnectRequest(String url, Object param,  Class<T> parameterTypes) {
        try {
        	return (T) t_restTemplate.postForObject(url, param, parameterTypes);
        }catch (Exception e){
        	e.printStackTrace();
            log.error("sendConnectRequest请求错误：路径：{} 参数：{}",url, JSON.toJSON(param));
        }
		return null;
    }
    
    /**
     * 调用 主服务器注册地址进行注册等服务
     * @param url
     * @param param
     * @param parameterTypes
     */
    public static <T> T sendDefaultConnectRequest(String url, Object param, Class<T> parameterTypes) {
        try {
        	return sendConnectRequest("http://" + defaultZone + url, param,parameterTypes);
        }catch (Exception e){
            log.error("sendDefaultConnectRequest请求错误：路径：{} 参数：{}","http://" + defaultZone +url, JSON.toJSON(param));
        }
		return null;
    }
    //分服务器上线信息同步到主服务器 
    public static void mainChannelActive(Map<String, String> paramMap) {
        ConnectUtil.sendDefaultConnectRequest("/autows/mainChannelActive",paramMap,Map.class);
    }
    //将分服务器的设备同步到主服务器
    public static void mainRegisterChannels(Map<String, String> paramMap) {
        ConnectUtil.sendDefaultConnectRequest("/autows/mainRegisterChannels",paramMap,Map.class);
    }
    //将分服务器的信息从主服务器去除
    public static void mainChannelInactive(Map<String, String> paramMap) {
        ConnectUtil.sendDefaultConnectRequest("/autows/mainChannelInactive",paramMap,Map.class);
    }
    //分服务器更改状态后同步更改主服务器
    public static void updateMainChannelStatus(Map<String, String> paramMap) {
        ConnectUtil.sendDefaultConnectRequest("/autows/updateMainChannelStatus",paramMap,Map.class);
    }
    //定时任务将分服务器的所有信息同步至主服务器
    public static void syncAllChannelToMain(Map<String, String> paramMap) {
        ConnectUtil.sendDefaultConnectRequest("/autows/syncAllChannelToMain",paramMap,Map.class);
    }

    //服务启动时根据状态确认地址判断该设备是否在线
    public static boolean confirmConnect(String ztqrdz){
        try{
            URL url = new URL(ztqrdz);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return true;
            }
        }catch(Exception e){
            log.error("出现错误："+e.getMessage());
        }
        return false;
    }
}
