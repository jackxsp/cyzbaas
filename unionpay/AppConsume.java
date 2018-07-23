package unionpay;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSONObject;
import com.justep.baas.action.ActionContext;
import com.unionpay.acp.sdk.HttpClient;
import com.unionpay.acp.sdk.SDKConfig;
import com.unionpay.acp.sdk.SDKUtil;

public class AppConsume{
	private static String merId;
	
	public static String encoding = "UTF-8";
	public static String version = "5.0.0";
	
	static{
		SDKConfig.getConfig().loadPropertiesFromSrc();// 从classpath加载acp_sdk.properties文件
		InputStream configFile = AppConsume.class.getResourceAsStream("unionpay.config.xml");
		try{
			SAXReader reader = new SAXReader();
			Document doc = reader.read(configFile);
			Element config = doc.getRootElement();
			merId = config.elementTextTrim("merId");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static JSONObject getTN(JSONObject params, ActionContext context) throws IOException {
		String orderId = params.getString("orderId");
		Map<String, String> data = new HashMap<String, String>();
		// 版本号
		data.put("version", "5.0.0");
		// 字符集编码 默认"UTF-8"
		data.put("encoding", "UTF-8");
		// 签名方法 01 RSA
		data.put("signMethod", "01");
		// 交易类型 01-消费
		data.put("txnType", "01");
		// 交易子类型 01:自助消费 02:订购 03:分期付款
		data.put("txnSubType", "01");
		// 业务类型
		data.put("bizType", "000201");
		// 渠道类型，07-PC，08-手机
		data.put("channelType", "08");
		// 前台通知地址 ，控件接入方式无作用
		data.put("frontUrl", "");
		// 后台通知地址 必传项
		data.put("backUrl", "http://www.wex5.com");
		// 接入类型，商户接入填0 0- 商户 ， 1： 收单， 2：平台商户
		data.put("accessType", "0");
		// 商户号码，请改成自己的商户号
		data.put("merId", merId);
		// 商户订单号，8-40位数字字母
		data.put("orderId", orderId);
		// 订单发送时间，取系统时间
		data.put("txnTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		// 交易金额，单位分
		data.put("txnAmt", "10");
		// 交易币种
		data.put("currencyCode", "156");
		// 请求方保留域，透传字段，查询、通知、对账文件中均会原样出现
		// data.put("reqReserved", "透传信息");
		// 订单描述，可不上送，上送时控件中会显示该信息
		// data.put("orderDesc", "订单描述");
		data = signData(data);
		// 交易请求url 从配置文件读取
		String requestAppUrl = SDKConfig.getConfig().getAppRequestUrl();
		Map<String, String> resmap = submitUrl(data, requestAppUrl);
		String tn = resmap.get("tn");
		JSONObject map = new JSONObject();
		map.put("tn", tn);
		return map;
		
	}
	
	
	/**
	 * java main方法 数据提交 　　 对数据进行签名
	 * 
	 * @param contentData
	 * @return　签名后的map对象
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> signData(Map<String, ?> contentData) {
		Entry<String, String> obj = null;
		Map<String, String> submitFromData = new HashMap<String, String>();
		for (Iterator<?> it = contentData.entrySet().iterator(); it.hasNext();) {
			obj = (Entry<String, String>) it.next();
			String value = obj.getValue();
			if (StringUtils.isNotBlank(value)) {
				// 对value值进行去除前后空处理
				submitFromData.put(obj.getKey(), value.trim());
			}
		}
		/**
		 * 签名
		 */
		SDKUtil.sign(submitFromData, encoding);

		return submitFromData;
	}


	/**
	 * java main方法 数据提交 提交到后台
	 * 
	 * @param contentData
	 * @return 返回报文 map
	 */
	public static Map<String, String> submitUrl(Map<String, String> submitFromData,String requestUrl) {
		String resultString = "";
		/**
		 * 发送
		 */
		HttpClient hc = new HttpClient(requestUrl, 30000, 30000);
		try {
			int status = hc.send(submitFromData, encoding);
			if (200 == status) {
				resultString = hc.getResult();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, String> resData = new HashMap<String, String>();
		/**
		 * 验证签名
		 */
		if (null != resultString && !"".equals(resultString)) {
			// 将返回结果转换为map
			resData = SDKUtil.convertResultStringToMap(resultString);
			/*if (SDKUtil.validate(resData, encoding)) {
				//logger.info("验证签名成功");
				
			} else {
				//logger.info("验证签名失败");
			}*/
			// 打印返回报文
			//logger.info("打印返回报文：" + resultString);
		}
		return resData;
	}
	
	/*public static void main(String[] args) {
		String orderId = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String tn = getTN(orderId);
		System.out.println(tn);
	}*/
	
}
