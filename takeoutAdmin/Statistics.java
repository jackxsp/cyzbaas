package takeoutAdmin;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import com.alibaba.fastjson.JSONObject;
import com.justep.baas.action.ActionContext;
import com.justep.baas.data.DataUtils;
import com.justep.baas.data.Table;
import com.justep.baas.data.Transform;
import com.justep.baas.data.sql.SQLException;

public class Statistics {
	public static JSONObject getCuisineStatistics(JSONObject params, ActionContext context) throws SQLException, NamingException, java.sql.SQLException {
		
		String startDate = params.getString("startDate");
		String endDate = params.getString("endDate");
		
		List<Object> sqlParams = new ArrayList<Object>();
		sqlParams.add(startDate);
		sqlParams.add(endDate);
		
		
		Table table = null;
		Connection conn = context.getConnection("demo");
		String sql = "";
		try{
			if(startDate != null && endDate != null){
				sql = "SELECT SUM(ord.fSum) AS daySum ,count(*) AS count,avg(ord.fSum) AS price,date(fCreateTime) AS times FROM takeout_order ORD where ord.fCreateTime between ? and ? GROUP BY date(ord.fCreateTime)";
				table = DataUtils.queryData(conn, sql, sqlParams, null, null, null);
			}else{
				sql = "SELECT SUM(ord.fSum) AS daySum ,count(*) AS count,avg(ord.fSum) AS price,date(fCreateTime) AS times FROM takeout_order ORD GROUP BY date(ord.fCreateTime)";
				table = DataUtils.queryData(conn, sql, null, null, null, null);

			}
			
			
			return Transform.tableToJson(table);
			
		} finally {
			conn.close();
		}
		
	
	}
}
