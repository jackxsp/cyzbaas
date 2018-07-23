package justep;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;

import javax.naming.NamingException;
import javax.servlet.ServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.justep.baas.action.ActionContext;


public class Takeout {
	public static JSONObject queryAddr(JSONObject params, ActionContext context) throws SQLException, NamingException, ParseException{
		String httpUrl = "http://apis.baidu.com/3023/geo/address";
		String x = "", y = "";
		if (params != null && !"".equals(params)) {
			x = params.getString("x");
			y = params.getString("y");
		}
		if (x.length() == 0)
			x = "39.8622934399999";
		if (y.length() == 0)
			y = "116.45764191999997";
		String httpArg = "l=" + x + "," + y;
		httpUrl = httpUrl + "?" + httpArg;
		ServletResponse response = (ServletResponse)(context.get(ActionContext.RESPONSE));
		response.setCharacterEncoding("UTF-8");
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();
		try {
			URL url = new URL(httpUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			// 填入apikey到HTTP header
			connection.setRequestProperty("apikey", "8f91a2f0c505d88f49af77bba09ca8eb");
			connection.connect();
			InputStream is = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sbf.append(strRead);
				sbf.append("\r\n");
			}
			reader.close();
			result = sbf.toString();
			response.getWriter().println(result);
		} catch (Exception e) {
		} 
		return null;
	}

}
