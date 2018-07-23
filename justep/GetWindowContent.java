package justep;

import javax.naming.NamingException;

import com.alibaba.fastjson.JSONObject;
import com.justep.baas.action.ActionContext;
import com.justep.baas.data.sql.SQLException;
import java.io.File;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class GetWindowContent {

	public static JSONObject execute(JSONObject params, ActionContext context) throws SQLException, NamingException, Exception {

		JSONObject result = new JSONObject();
		String window = params.getString("window");
		if (window == null) {
			throw new RuntimeException("filePath is null");
		}
		String xid = params.getString("xid");
		if (xid == null) {
			throw new RuntimeException("xid is null");
		}

		String path = Thread.currentThread().getContextClassLoader().getResource("").getPath() + ".." + File.separator + "../model" + window;
		File file = new File(path);
		if (file.exists() && file.isFile()) {
			SAXReader reader = new SAXReader();
			Document document = reader.read(file);
			Element node = (Element) document.selectSingleNode("//*[@xid='" + xid + "']");
			if (node != null) {
				result.put("result", node.asXML());
			} else {
				throw new RuntimeException("can not found node");
			}
		}
		return result;

	}

}