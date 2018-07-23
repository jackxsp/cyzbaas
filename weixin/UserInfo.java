package weixin;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.alibaba.fastjson.JSONObject;
import com.justep.baas.action.ActionContext;

public class UserInfo{
	
	static WxMpServiceInstance instance = WxMpServiceInstance.getInstance();
	
	public static JSONObject service(JSONObject params, ActionContext context) throws JsonGenerationException, JsonMappingException, IOException, WxErrorException{
		HttpServletRequest request = (HttpServletRequest)context.get(ActionContext.REQUEST);
		
		String userCode = params.getString("code");
		
		WxMpOAuth2AccessToken oauth2AccessToken = instance.getWxMpService().oauth2getAccessToken(userCode);
		request.getSession().setAttribute("weixinOauth2AccessToken", oauth2AccessToken);
		WxMpUser userInfo  = instance.getWxMpService().oauth2getUserInfo(oauth2AccessToken, "zh_CN");
		JSONObject map = new JSONObject();
		map.put("openid", oauth2AccessToken.getOpenId());
		map.put("nickname", userInfo.getNickname());
		map.put("country", userInfo.getCountry());
		map.put("province", userInfo.getProvince());
		map.put("city", userInfo.getCity());
		map.put("headimgurl", userInfo.getHeadImgUrl());
		
		return map;
	}
	
}
