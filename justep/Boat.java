package justep;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

import javax.naming.NamingException;

import com.alibaba.fastjson.JSONObject;
import com.justep.baas.action.ActionContext;
import com.justep.baas.data.DataUtils;
import com.justep.baas.data.Table;
import com.justep.baas.data.Transform;


public class Boat {
	private static final String DATASOURCE_BOAT = "boat";
	public static JSONObject hasExits(JSONObject params, ActionContext context) throws SQLException, NamingException {
		Connection conn = context.getConnection(DATASOURCE_BOAT);
		String userId = params.getString("userId");
		try{
			String sql = "SELECT COUNT(FID) FROM USER WHERE USERID = '"+userId+"'";
			JSONObject rt = new JSONObject();
			int count = Integer.parseInt(DataUtils.getValueBySQL(conn, sql, null).toString());
			rt.put("count", count);
			return rt;
		} finally {
			conn.close();
		}
	}
	public static JSONObject queryUserInfo(JSONObject params, ActionContext context) throws SQLException, NamingException {
		Connection conn = context.getConnection(DATASOURCE_BOAT);
		String userId = params.getString("userId");
		try{
			String sql = "SELECT * FROM USER WHERE USERID = '"+userId+"'";
			Table table = DataUtils.queryData(conn, sql, null, null, null, null);
			return Transform.tableToJson(table);
		} finally {
			conn.close();
		}
	}
	
	public static JSONObject updateUserInfo(JSONObject params, ActionContext context) throws SQLException, NamingException {
		Connection conn = context.getConnection(DATASOURCE_BOAT);
		JSONObject rt = new JSONObject();
		String userId = params.getString("userId");
		Integer isSelf = params.getInteger("isSelf");
		java.sql.PreparedStatement pstmt = null;
		String sql = "UPDATE USER SET ISSELF='"+isSelf+"' WHERE USERID = '"+userId+"'";
		pstmt = conn.prepareStatement(sql);
		pstmt.execute();
		rt.put("state", "1");
		return rt;
	}
	
	public static JSONObject updateUserTel(JSONObject params, ActionContext context) throws SQLException, NamingException {
		Connection conn = context.getConnection(DATASOURCE_BOAT);
		JSONObject rt = new JSONObject();
		String tel = params.getString("tel");
		String userId = params.getString("userId");
		java.sql.PreparedStatement pstmt = null;
		String sql = "UPDATE USER SET TEL = '"+tel+"' WHERE USERID = '"+userId+"'";
		pstmt = conn.prepareStatement(sql);
		pstmt.execute();
		rt.put("state", "1");
		return rt;
	}
	
	public static JSONObject signSuccess(JSONObject params, ActionContext context) throws SQLException, NamingException {
		Connection conn = context.getConnection(DATASOURCE_BOAT);
		JSONObject rt = new JSONObject();
		String userId = params.getString("userId");
		Integer sequence = getSequence(userId, conn);
		double bonus = getRandom();
		java.sql.PreparedStatement pstmt = null;
		pstmt = conn.prepareStatement("UPDATE USER SET isSuccess = '1',SEQUENCE='"+sequence+"',BONUS='"+bonus+"' WHERE USERID = '"+userId+"' ");
		pstmt.execute();
		rt.put("state", "1");
		return rt;
	}
	
	private static double getRandom(){
		   Integer max = 10;
		   Random random = new Random();
		   java.text.DecimalFormat df = new java.text.DecimalFormat("#.#");
		   return Double.parseDouble(df.format(random.nextDouble() * max));
		  

	};
	
	private static Integer getSequence(String userId,Connection conn){
		String sql = "SELECT COUNT(FID) FROM user WHERE isSuccess = '1'";
		int count = Integer.parseInt(DataUtils.getValueBySQL(conn, sql, null).toString());
		return count + 1;
	}
	
}
