package justep;


import java.io.IOException;

import javax.naming.NamingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;

import com.alibaba.fastjson.JSONObject;
import com.justep.baas.action.ActionContext;
import com.justep.baas.data.sql.SQLException;

public class InitPage {
	public static JSONObject getCode(JSONObject params, ActionContext context) throws SQLException, NamingException, HttpException, IOException {
		HttpClient client = new HttpClient();
		String str = params.getString("param");
		PostMethod post = new PostMethod(str);
		JSONObject object = new JSONObject();
		try {
			client.executeMethod(post);
			int statusCode = post.getStatusCode();
			object.put("statusCode", statusCode);
			System.out.println("状态码:" + statusCode);
		} finally {
			post.releaseConnection();
		}
		return object;
	}
}
