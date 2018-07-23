package bmap;
import java.io.IOException;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.alibaba.fastjson.JSONObject;
import com.justep.baas.action.ActionContext;


public class Api {

	public static JSONObject convertLocation(JSONObject params, ActionContext context) throws SQLException, NamingException, ClientProtocolException, IOException {
		HttpServletResponse resp = (HttpServletResponse)(context.get(ActionContext.RESPONSE));
		String longitude = params.getString("longitude");
		String latitude = params.getString("latitude");
		String url = "http://api.map.baidu.com/geoconv/v1/?coords="+longitude+ ","+ latitude +"&from=1&to=5&ak=xifH76TpyIL1cvnTzuEP0bpq";
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = httpclient.execute(httpGet);
		String resultContent = new BasicResponseHandler().handleResponse(response);
		resp.getWriter().write(resultContent);
		return null;
	}
}
