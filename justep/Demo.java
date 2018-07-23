package justep;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;

import com.alibaba.fastjson.JSONObject;
import com.justep.baas.action.ActionContext;
import com.justep.baas.data.DataUtils;


public class Demo {
	private static final String DATASOURCE_TAKEOUT = "takeout";

	public static JSONObject getOrderCount(JSONObject params, ActionContext context) throws SQLException, NamingException {
		Connection conn = context.getConnection(DATASOURCE_TAKEOUT);
		try{
			String sql = "SELECT COUNT(ord.fID) AS orderCount "
					+ " FROM takeout_order ord ";
			JSONObject ret = new JSONObject();
			int count = Integer.parseInt(DataUtils.getValueBySQL(conn, sql, null).toString());
			ret.put("orderCount", count);
			return ret;
		} finally {
			conn.close();
		}
	}

}
