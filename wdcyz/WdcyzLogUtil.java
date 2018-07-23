package wdcyz;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;
import javax.naming.NamingException;
import java.text.ParseException;

import com.alibaba.fastjson.JSONObject;
import com.justep.baas.action.ActionContext;

public class WdcyzLogUtil {
	private static final String DATASOURCE_WDCYZ = "wdcyz";
	
	public static JSONObject writeLog(JSONObject params, ActionContext context) throws SQLException, NamingException,ParseException {
		JSONObject logData = params.getJSONObject("logData");
		Connection conn = context.getConnection(DATASOURCE_WDCYZ);
		try{
			writeLog(logData,conn);
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			conn.close();
		}
		return null;
		
	}
	public static JSONObject writeLog(JSONObject logData, Connection conn) throws SQLException, NamingException,ParseException {
		System.out.println("logData："+logData);
		String userId = logData.getString("userId");
		Date dDate = new Date();
		SimpleDateFormat df = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss") ;
	    String strDate = df.format (dDate) ; 
	    dDate = df.parse(strDate);
	    String operType = logData.getString("operType");
	    String operTable = logData.getString("operTable");
	    String operContent = logData.getString("operContent");
	    String operStatus = logData.getString("operStatus");
	    
		String insertSql = "insert tb_b_oper_log(user_id,oper_date,oper_type,oper_table,oper_content,oper_status)"
				+" values(?,?,?,?,?,?)";
		System.out.println("insertSql："+insertSql);
		JSONObject ret = new JSONObject();
		try{
			java.sql.PreparedStatement pstmt = null;
			pstmt = conn.prepareStatement(insertSql);
			pstmt.setString(1, userId);
			pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			//pstmt.setDate(2, (java.sql.Date) dDate);
			pstmt.setString(3, operType);
			pstmt.setString(4, operTable);
			pstmt.setString(5, operContent);
			pstmt.setString(6, operStatus);
			
			int num = pstmt.executeUpdate();
			ret.put("code", 0);
		}catch(SQLException sqle){
			ret.put("code", -1);
			ret.put("msg", "插入操作日志时发生sql错误，请联系系统管理员。");
			sqle.printStackTrace();
			throw sqle;
		}catch(Exception e){
			ret.put("code", -1);
			ret.put("msg", "插入操作日志时发生sql错误，请联系系统管理员。");
			e.printStackTrace();
			throw e;
		}finally{
			return ret;
		}
	    
	}

}