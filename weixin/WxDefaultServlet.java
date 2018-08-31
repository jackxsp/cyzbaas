package weixin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.justep.baas.action.ActionContext;

public class WxDefaultServlet extends HttpServlet {
	
	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException,
			IOException {
		WxMpServiceInstance.getInstance().doResponse(request, response);
	}

}