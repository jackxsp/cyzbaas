package jpush;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.ClientConfig;
import cn.jpush.api.common.connection.HttpProxy;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.schedule.ScheduleResult;

import com.alibaba.fastjson.JSONObject;
import com.justep.baas.action.ActionContext;

public class Push {
    
    private static String appKey = "";
    private static String masterSecret = "";
    private static Boolean apnsProduction = false;
    private static JPushClient jpushClient;
    
	private static HttpProxy getProxy() {
		String host = System.getProperties().getProperty("http.proxyHost");
		String port = System.getProperties().getProperty("http.proxyPort");
		if ((host != null) && (!"".equals(host))) {
			return new HttpProxy(host, Integer.parseInt(port));
		}
		else {
			return null;
		}
	}
	
    static{
        InputStream configFile = Push.class.getResourceAsStream("jpush.config.xml");
        try{
            SAXReader reader = new SAXReader();
            Document doc = reader.read(configFile);
            Element config = doc.getRootElement();
            appKey = config.elementTextTrim("appKey");
            masterSecret = config.elementTextTrim("masterSecret");
            apnsProduction = (config.elementTextTrim("apnsProduction").equals("true"))?true:false;
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static JSONObject push(JSONObject params, ActionContext context){
        String registrationId = params.getString("registrationId");
        try {
            sendPushMessage(registrationId, appKey, masterSecret);
        } catch (APIConnectionException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (APIRequestException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }
    
    
    public static ScheduleResult sendPushMessage(String registrationId, String key, String secret) throws APIConnectionException, APIRequestException{
        ClientConfig config = ClientConfig.getInstance();

		HttpProxy proxy = getProxy();
		jpushClient = new JPushClient(secret, key, 3, proxy, config);
        PushPayload payload = PushPayload.newBuilder()
        .setPlatform(Platform.all())
        .setAudience(Audience.registrationId(registrationId))
        .setNotification(Notification.alert("亲爱的x5外卖用户，您的订单已经配货完毕，正在运送中.."))
        .build();
        payload.resetOptionsTimeToLive(86400);
        payload.resetOptionsApnsProduction(apnsProduction);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE, 1);
        String scheduleTime =sdf.format(nowTime.getTime());
        ScheduleResult result = jpushClient.createSingleSchedule(UUID.randomUUID().toString().replaceAll("-", ""), scheduleTime, payload);
        return result;
    }
}
