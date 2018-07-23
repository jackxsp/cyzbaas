package justep;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;

import com.alibaba.fastjson.JSONObject;
import com.justep.baas.action.ActionContext;
import com.justep.baas.data.DataUtils;

public class Taobao {
	private static final String DATASOURCE_TAKEOUT = "taobao";

	public static JSONObject newCart(JSONObject params, ActionContext context) throws SQLException, NamingException {	
		Integer cartid = 1;
		Integer fShopID = params.getInteger("fShopID");
		String fGoodID = params.getString("fGoodID");
		String fTitle = params.getString("fTitle");
		String fImg = params.getString("fImg");
		Float fPrice = params.getFloat("fPrice");
		Float fOldPrice = params.getFloat("fOldPrice");
		String fPostage = params.getString("fPostage");		
		String fColor = params.getString("fColor");
		String fUserID = params.getString("fUserID");
		
				
		Connection conn = context.getConnection(DATASOURCE_TAKEOUT);
		
		try{
			String sql = "SELECT COUNT(fID) AS number "
					+ " FROM cart where fGoodID='"+fGoodID+"' and fUserID='"+fUserID+"'";
			JSONObject ret = new JSONObject();
			int count = Integer.parseInt(DataUtils.getValueBySQL(conn, sql, null).toString());
			
			if(count>0){
				int number = Integer.parseInt(DataUtils.getValueBySQL(conn, "SELECT fNumber FROM cart where fGoodID='"+fGoodID+"' and fUserID='"+fUserID+"'", null).toString());
				
				java.sql.PreparedStatement pstmt = null;
				pstmt = conn.prepareStatement("UPDATE cart SET fNumber="+(number+1)+" WHERE fGoodID='"+fGoodID+"'");
				pstmt.execute();
			} else {
				int number = Integer.parseInt(DataUtils.getValueBySQL(conn, "SELECT COUNT(fID) FROM cart", null).toString());
				if( number> 0){
					cartid = Integer.parseInt(DataUtils.getValueBySQL(conn, "SELECT fID FROM cart ORDER BY fID DESC LIMIT 1", null).toString())+1;				
				}
				
				java.sql.PreparedStatement pstmt = null;
				pstmt = conn.prepareStatement("INSERT INTO cart VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
				pstmt.setInt(1,cartid);
				pstmt.setInt(2,fShopID);
				pstmt.setString(3, fGoodID);
				pstmt.setString(4, fTitle);
				pstmt.setString(5, fImg);
				pstmt.setFloat(6, fPrice);
				pstmt.setFloat(7, fOldPrice);
				pstmt.setString(8, fPostage);
				pstmt.setString(9, fColor);
				pstmt.setInt(10, 1);
				pstmt.setInt(11, 0);
				pstmt.setString(12, fUserID);
				pstmt.setInt(13, 0);
				
				pstmt.execute();
			}
			
			ret.put("state", "1");
			return ret;
			
		} finally {
			conn.close();
		}		
		 
	}
	
	public static JSONObject newOrder(JSONObject params, ActionContext context) throws SQLException, NamingException {	
		String fDate = params.getString("fDate");
		String fContent = params.getString("fContent");
		Float fSum = params.getFloat("fSum");
		String fUser = params.getString("fUser");
		String fTel = params.getString("fTel");
		String fAddress = params.getString("fAddress");
		String fSend = params.getString("fSend");
		String fMessage = params.getString("fMessage");
		String[] goodIDs= params.getString("goodIDs").split(",");

		Connection conn = context.getConnection(DATASOURCE_TAKEOUT);
		
		try{			
			JSONObject ret = new JSONObject();
			int orderid=1;
			int number = Integer.parseInt(DataUtils.getValueBySQL(conn, "SELECT COUNT(fID) FROM orders", null).toString());
			if( number> 0){
				orderid = Integer.parseInt(DataUtils.getValueBySQL(conn, "SELECT fID FROM orders ORDER BY fID DESC LIMIT 1", null).toString())+1;
			}
			
			java.sql.PreparedStatement pstmt = null;
			pstmt = conn.prepareStatement("INSERT INTO orders VALUES(?,?,?,?,?,?,?,?,?)");
			pstmt.setInt(1,orderid);
			pstmt.setString(2,fDate);
			pstmt.setString(3, fContent);
			pstmt.setFloat(4, fSum);
			pstmt.setString(5, fUser);
			pstmt.setString(6, fTel);
			pstmt.setString(7, fAddress);
			pstmt.setString(8, fSend);
			pstmt.setString(9, fMessage);
			pstmt.execute();
			
			for(int i=0; i<goodIDs.length; i++){
				pstmt = conn.prepareStatement("DELETE FROM cart WHERE fID="+goodIDs[i]);
				pstmt.execute();
			}
			
			ret.put("state", "1");
			return ret;
			
		} finally {
			conn.close();
		}
		
	}
	
	
}
