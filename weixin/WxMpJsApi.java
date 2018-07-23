package weixin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.Enumeration; 

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.chanjar.weixin.common.bean.WxJsapiPrepayOrder;
import me.chanjar.weixin.common.bean.WxJsapiUnifiedOrder;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.util.json.WxGsonBuilder;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.util.crypto.WxMpCryptUtil;

import com.alibaba.fastjson.JSONObject;
import com.justep.baas.action.ActionContext;

/**
 * 
 * @author 007slm
 * @email 007slm@163.com
 *
 */
public class WxMpJsApi{
	
	static WxMpServiceInstance instance = WxMpServiceInstance.getInstance();
	
	public static JSONObject service(JSONObject params, ActionContext context) throws IOException{
		HttpServletRequest req = (HttpServletRequest)context.get(ActionContext.REQUEST);
		HttpServletResponse resp = (HttpServletResponse)context.get(ActionContext.RESPONSE);
		String action = req.getParameter("action");
		if ("getTicket".equals(action)) {
			getTicket(req, resp);
		} else if ("chooseWXPay".equals(action)) {
			chooseWXPay(req, resp);
		}else if ("JSBridgeWXPay".equals(action)) {
			JSBridgeWXPay(req, resp);
		}
		return null;
	}
	private static void JSBridgeWXPay(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		WxJsapiPrepayOrder prepayOrder = getPrepayOrder(req);    
	    Map<String,String> payReq = new HashMap<String,String>();
	    payReq.put("appId", prepayOrder.getAppId());
	    payReq.put("timeStamp", "" + (System.currentTimeMillis() / 1000));
	    payReq.put("nonceStr", prepayOrder.getNonceStr());
	    payReq.put("package", "prepay_id=" + prepayOrder.getPrepayId());
	    payReq.put("signType", "MD5");
	    
	    String paySign = WxMpCryptUtil.genWithAmple(payReq,instance.getWxMpConfigStorage().getAppKey());
	    payReq.remove("appId");
	    payReq.put("paySign", paySign);
		payReq.put("returnCode", prepayOrder.getReturnCode());
		payReq.put("returnMsg", prepayOrder.getReturnMsg());
	    
	    String payReqResult = WxGsonBuilder.create().toJson(payReq, HashMap.class);
	    System.out.println("payReq:"+payReqResult);
	    resp.setCharacterEncoding("utf-8");
	    resp.getWriter().write(payReqResult);
	}
	
	private static void chooseWXPay(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		WxJsapiPrepayOrder prepayOrder = getPrepayOrder(req);    
	    Map<String,String> payReq = new HashMap<String,String>();
	    payReq.put("appId", prepayOrder.getAppId());
	    payReq.put("timestamp", "" + (System.currentTimeMillis() / 1000));
	    payReq.put("nonceStr", prepayOrder.getNonceStr());
	    payReq.put("package", "prepay_id=" + prepayOrder.getPrepayId());
	    payReq.put("signType", "MD5");
	    
	    String paySign = WxMpCryptUtil.genWithAmple(payReq,instance.getWxMpConfigStorage().getAppKey());
	    payReq.remove("appId");
	    payReq.put("paySign", paySign);
		payReq.put("returnCode", prepayOrder.getReturnCode());
		payReq.put("returnMsg", prepayOrder.getReturnMsg());
	    
	    String payReqResult = WxGsonBuilder.create().toJson(payReq, HashMap.class);
	    System.out.println("payReq:"+payReqResult);
	    resp.setCharacterEncoding("utf-8");
	    resp.getWriter().write(payReqResult);
	}
	
	private static WxJsapiPrepayOrder getPrepayOrder(HttpServletRequest req){
		WxJsapiUnifiedOrder unifiedOrder = new WxJsapiUnifiedOrder();
		/*WxMpOAuth2AccessToken oauth2AccessToken = (WxMpOAuth2AccessToken)req.getSession().getAttribute("weixinOauth2AccessToken");
		if(oauth2AccessToken == null){
			throw new RuntimeException("请先通过微信Oauth2对x5外面授权,然后才能发起支付");
		}*/
		
		//unifiedOrder.setOpenId(oauth2AccessToken.getOpenId());
        Enumeration<?> names2 = req.getParameterNames();  
        while(names2.hasMoreElements()){  
        	String tmpname = names2.nextElement().toString();
            System.out.println(tmpname+":"+req.getParameter(tmpname));  
        }  
		System.out.println("getQueryString:"+req.getQueryString());
		String openId = req.getParameter("openId");
		System.out.println("openId:"+openId);
		if(null == openId || "".equals(openId)){
			throw new RuntimeException("没有获取到用户的openid，支付不能继续！");
		}
		unifiedOrder.setOpenId(openId);
		unifiedOrder.setAppId(instance.getWxMpConfigStorage().getAppId());
		unifiedOrder.setBody(req.getParameter("body"));
		unifiedOrder.setMchId(req.getParameter("mchId"));
		unifiedOrder.setNotifyUrl(req.getParameter("notifyUrl"));
		unifiedOrder.setOutTradeNo(req.getParameter("outTradeNo"));
		String ip = req.getRemoteAddr();
		unifiedOrder.setSpbillCreateIp(isIPAdress(ip) ? ip : "127.0.0.1");
		unifiedOrder.setTotalFee(req.getParameter("totalFee"));
		try {
			WxJsapiPrepayOrder prepayOrder = instance.getWxMpService().getPrePayOrder(unifiedOrder);
			return prepayOrder;
		} catch (WxErrorException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean isIPAdress(String str) {
		Pattern pattern = Pattern.compile("^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$");
		return pattern.matcher(str).matches();
	}


	private static void getTicket(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try {
			String jsapiTicket = instance.getWxMpService().getJsapiTicket();
			resp.getWriter().write(jsapiTicket);
		} catch (WxErrorException e) {
			throw new RuntimeException(e);
		}
	}
}
