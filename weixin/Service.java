package weixin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.justep.baas.action.ActionContext;

public class Service extends HttpServlet {
	private static final long serialVersionUID = -3085539764084393258L;
	
	public static JSONObject service(JSONObject params, ActionContext context) throws ServletException, IOException{
		HttpServletRequest request = (HttpServletRequest)context.get(ActionContext.REQUEST);
		HttpServletResponse response = (HttpServletResponse)context.get(ActionContext.RESPONSE);
		WxMpServiceInstance.getInstance().doResponse(request, response);
		return null;
	}
}