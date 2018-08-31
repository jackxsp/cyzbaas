package wdcyz;
import java.io.IOException;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.naming.NamingException;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.justep.baas.Utils;
import com.justep.baas.action.ActionContext;
import com.justep.baas.data.DataUtils;
import com.justep.baas.data.Row;
import com.justep.baas.data.Table;
import com.justep.baas.data.Transform;
import com.justep.baas.data.sql.SQLStruct;
import org.apache.log4j.Logger;

import wdcyz.WdcyzLogUtil;

public class Wdcyz {
	private static final String DATASOURCE_WDCYZ = "wdcyz";
	public static final String VARIABLE_FLAG = "var-"; 
	protected static Logger logger = Logger.getLogger(Wdcyz.class);
	
	public static void main(String[] args) {
		SQLStruct sql = new SQLStruct("SELECT user.fID, user.fName, user.fPhoneNumber, user.fAddress, COUNT(ord.fID) AS orderCount FROM takeout_user user LEFT JOIN takeout_order ord ON user.fID = ord.fUserID WHERE (::str) ::abc and (0=:useSearch) or (user.fID LIKE :search OR user.fName LIKE :search OR user.fPhoneNumber LIKE :search OR user.fAddress LIKE :search) GROUP BY user.fID, user.fName, user.fPhoneNumber, user.fAddress");
		Map<String,Object>p = new java.util.HashMap<String, Object>();
		p.put("str", "1=2");
		p.put("abc", "1=2");
		System.out.println(sql.getSQL(p));
		sql = new SQLStruct(null);
		System.out.println(sql.getSQL());
	}

	//蔬菜上加功能中用于获取可上架蔬菜和已经上架的蔬菜数据
	public static JSONObject getSelectVege(JSONObject params, ActionContext context) throws SQLException, NamingException {
		// 获取参数
		Object columns = params.get("columns");
		Integer limit = params.getInteger("limit");
		Integer offset = params.getInteger("offset");
		
		List<Object> sqlParams = new ArrayList<Object>();

		Table table = null;
		Connection conn = context.getConnection(DATASOURCE_WDCYZ);
		try{
			String sql = "select ifnull(b.vege_id,0) calcCheckBox,a.vege_id vege_id,a.vege_name vege_name,ifnull(b.num,0) num,ifnull(b.max_sel_num,1) max_sel_num,ifnull(b.descs,'') descs,a.sec_flag sec_flag "
					+ " from tb_b_vegetable a left join tb_b_selectvege b on a.VEGE_ID = b.vege_id";
			JSONObject ret = new JSONObject();
			table = DataUtils.queryData(conn, sql, sqlParams, columns, offset, limit);
			ret = Transform.tableToJson(table);
			return ret;
		} finally {
			conn.close();
		}
	}

	//在点菜统计功能中用于获取用户点了哪些菜,当次点菜汇总查询，用于打印配菜单。订单状态拆分为支付状态：0:初始订单,1:待支付,2:已支付,3:已取消，配送状态：0：未配送,1:已配送
	public static JSONObject getUserSelectVege(JSONObject params, ActionContext context) throws SQLException, NamingException {
		// 获取参数
		Object columns = params.get("columns");
		Integer limit = params.getInteger("limit");
		Integer offset = params.getInteger("offset");
		String userId = params.getString("user_id");
		String userName = params.getString("user_name");
		String filter = params.getString("filter");
		
		System.out.println("filter:" + filter);
		List<String> filters = new ArrayList<String>();
		if (!Utils.isEmptyString(filter)) {
			filters.add(filter);
		}
		List<Object> sqlParams = new ArrayList<Object>();
		
		Table table = null;
		Connection conn = context.getConnection(DATASOURCE_WDCYZ);
		try{
			/*String sql = "select a.send_date send_date,a.user_id user_id,c.user_no user_no,c.user_name user_name,c.user_mobile user_mobile,d.content content,d.`status` status,"
					+ " c.user_addr user_addr,a.create_date create_date,group_concat(concat(b.vege_name,'(',a.vege_num ,'份)') ORDER BY b.VEGETYPE_NAME ASC  separator ',') as vege_names,"
					+ " a.note note from tb_b_user_vege a left join tb_b_vegetable b on a.vege_id = b.vege_id left join tf_f_order d on a.user_id=d.user_id and a.send_date = d.SENT_TIME and d.`STATUS` in ('1','2'),tf_f_user c "
					+ "where a.user_id = c.user_id ";
			下面的语句拼成的sql：		
			select m.*,n.content order_content,if(isnull(t.post_name),"",t.post_name) post_name from 
			(select a.send_date send_date,a.user_id user_id,a.times,c.user_no user_no,c.user_name user_name,c.user_mobile user_mobile,c.post_id post_id,
			ifnull(c.district,'') district,c.user_addr user_addr,a.create_date create_date,group_concat(concat(b.vege_name,'(',a.vege_num ,'份)') ORDER BY b.VEGETYPE_NAME ASC separator ',') as vege_names,
			a.note note from tb_b_user_vege a left join tb_b_vegetable b on a.vege_id = b.vege_id ,tf_f_user c 
			where a.user_id = c.user_id
			group by a.send_date,a.user_id order by a.send_date,c.user_name) m left join
			(select user_id,group_concat(concat(content,'(',statusname,')'),'') content from 
			(select user_id,`status`,if(`status`='1','待支付',if(`status`='2','已支付','未知')) statusname,send_status,group_concat(content) as content from tf_f_order group by user_id ,`status`) a
			where `status` in ('1','2') and send_status='0' group by user_id) n on m.user_id = n.user_id 
			left join tb_d_post_comp t on m.post_id = t.post_id order by m.create_date desc

			*/
			//修改sql，把农产品的订单内容拼成一个附加在点菜单里面，以便一次性配送。目前把待支付的和已支付状态的都列出来了，后面支付功能做好后，就只列出已支付状态的。
			String sql = "select m.*,n.content order_content,if(isnull(t.post_name),'',t.post_name) post_name from "
					+ " (select a.send_date send_date,a.user_id user_id,a.times times,c.user_no user_no,c.user_name user_name,c.user_mobile user_mobile,c.post_id post_id,"
				+ " ifnull(c.district,'') district,c.user_addr user_addr,a.create_date create_date,group_concat(concat(b.vege_name,'(',a.vege_num ,'份)') ORDER BY b.VEGETYPE_NAME ASC separator ',') as vege_names,"
				+ " a.note note from tb_b_user_vege a left join tb_b_vegetable b on a.vege_id = b.vege_id ,tf_f_user c "
				+ " where a.user_id = c.user_id ";
				
				
			String where = (filters != null && filters.size() > 0) ? " AND " + DataUtils.arrayJoin(filters.toArray(), "(%s)", " AND ") : "";

			if(!Utils.isEmptyString(where)){
				sql = sql + where;
			}
			if(!Utils.isEmptyString(userName)){
				sql = sql + " and c.user_name like  '%" + userName + "%' ";
			}
			if(!Utils.isEmptyString(userId)){
				sql = sql + " and a.user_id = ? ";
				sqlParams.add(userId);
			}
			sql = sql + " group by a.send_date,a.user_id order by a.send_date,c.user_name";
			
			sql = sql + ") m left join " 
					+ " (select user_id,group_concat(concat(content,'(',statusname,')'),'') content from " 
					+ " (select user_id,`status`,if(`status`='1','待支付',if(`status`='2','已支付','未知')) statusname,send_status,group_concat(content) as content from tf_f_order group by user_id ,`status`) a"
					+ " where `status` in ('1','2') and send_status='0' group by user_id) n on m.user_id = n.user_id "
					+ " left join tb_d_post_comp t on m.post_id = t.post_id order by m.create_date desc";
			System.out.println("sql:" + sql);
			JSONObject ret = new JSONObject();
			table = DataUtils.queryData(conn, sql, sqlParams, columns, offset, limit);
			ret = Transform.tableToJson(table);
			return ret;
		} finally {
			conn.close();
		}
	}

	//在管理员点菜功能中扣减用户点菜次数。
	public static JSONObject decreaseLeftCnt(JSONObject params, ActionContext context) throws SQLException, NamingException {
		// 获取参数
		Object columns = params.get("columns");
		Integer limit = params.getInteger("limit");
		Integer offset = params.getInteger("offset");
		JSONArray userIds = params.getJSONArray("userids");
		String filter = params.getString("filter");
		Table table = null;
		JSONObject logData = new JSONObject();
		
		int userNum = userIds.toArray().length;
		
		System.out.println("userIds:" + userIds.toJSONString());
		String condUserStr = DataUtils.arrayJoin(userIds.toArray(), "'%s'", " , ");
		Connection conn = context.getConnection(DATASOURCE_WDCYZ);
		JSONObject ret = new JSONObject();
		List<Object> sqlParams = new ArrayList<Object>();
		List<String> filters = new ArrayList<String>();
		String errUserids = "";
		String succUserids = "";
		String userId = "";
		String tableName= "tf_member_add_fee";
		//columns = "ID,user_id,add_fee_date,vip_type,start_date,end_date,left_cnt,status";
		String orderBy = " start_date " ;
		logData.put("userId", params.getString("operUserId"));
		logData.put("operType", "补点扣减用户点菜次数");
		logData.put("operTable", tableName);
		
		try{
			int modnum = 0;
			for(int i=0;i<userIds.size();i++){
				userId = userIds.getString(i);
				sqlParams.clear();
				filters.clear();
				filters.add(" user_id = ? ");
				sqlParams.add(userId);
				filters.add(" status = ? ");
				sqlParams.add("1");
				filters.add(" end_date > CURDATE() ");
				filters.add(" left_cnt > 0 ");
				//String querySql = "select ID,user_id,add_fee_date,vip_type,start_date,end_date,left_cnt,status from tf_member_add_fee where user_id = ? and status = ? and end_date > CURDATE() order by start_date";
				//System.out.println("querySql:" + querySql);
				table = DataUtils.queryData(conn, tableName, columns, filters,orderBy,sqlParams, offset, limit);
				System.out.println("table:" + Transform.tableToJson(table) + " rows:" +table.getRows().size());
				if(table==null || table.getRows().size()==0){
					sqlParams.set(1,"0");
					table = DataUtils.queryData(conn, "tf_member_add_fee", columns, filters,orderBy,sqlParams, offset, limit);
					if(table==null || table.getRows().size()==0){
						errUserids += userId + ",";
					}else{
						Row row = table.getRows().get(0);
						String id = row.getString("ID");
						if(logger.isDebugEnabled())
							logger.debug("id:"+id);
						String sql = "update tf_member_add_fee set left_cnt = left_cnt - 1,status='1',start_date = curdate(),end_date=date_add(curdate(), interval 2 month) where id = '"+id+"'";
						if(logger.isDebugEnabled())
							logger.debug("sql:"+sql);
						ret.put("msg", userId + "启用新的套餐成功，请确认！");
						int num = modify(sql,conn);
						if(num == 0){
							errUserids += userId + ",";
						}else{
							modnum++;
							succUserids += userId + ",";
						}
					}
				}else{
					String updateSql = "update tf_member_add_fee set left_cnt = left_cnt - 1 where user_id ='"+ userId +"' and status = '1' and left_cnt > 0 and end_date > CURDATE()";
					
					int num = modify(updateSql,conn);
					if(num == 0){
						errUserids += userId + ",";
					}else{
						modnum++;
						succUserids += userId + ",";
						ret.put("code", "0");
						System.out.println("updateSql==="+updateSql);	
					}
				}
			}
			
			if(modnum != userNum){
				ret.put("code", "-1");
				ret.put("msg", "修改剩余次数时发生错误，点菜人数："+userNum +",扣减人数：" +modnum + "[" + succUserids + "]扣减成功。" + "。["+errUserids + "]未扣减成功，可能没有配送中的套餐信息。");
			    logData.put("operContent", ret.getString("msg"));
			    logData.put("operStatus", "失败");
			}else{
				ret.put("code", "0");
			    logData.put("operContent", "扣减用户点菜次数:"+succUserids);
			    logData.put("operStatus", "成功");
			}
			WdcyzLogUtil.writeLog(logData, conn);
		} catch(Exception bue){
			conn.rollback();
			ret.put("code", "-1");
			ret.put("msg", "修改剩余次数时发生错误，请确认数据！修改用户："+condUserStr + ",出错用户id:" + userId);
			bue.printStackTrace();
			throw bue;
		}finally {
			conn.close();
			return ret;
		}
	}
	//用户点菜历史查询,支持管理员与会员自已查询，管理员可以查询所有用户的，会员只能查询自已的。
	public static JSONObject getUserSelectHis(JSONObject params, ActionContext context) throws SQLException, NamingException {
		// 获取参数
		Object columns = params.get("columns");
		Integer limit = params.getInteger("limit");
		Integer offset = params.getInteger("offset");
		String startDate = params.getString("start_date");
		String endDate = params.getString("end_date");
		String userId = params.getString("user_id");
		String userName = params.getString("user_name");
		
		List<Object> sqlParams = new ArrayList<Object>();
		
		Table table = null;
		Connection conn = context.getConnection(DATASOURCE_WDCYZ);
		try{
			String sql = "select a.send_date send_date,a.user_id user_id,c.user_no user_no,c.user_name user_name,c.user_mobile user_mobile,"
					+ " c.user_addr user_addr,a.create_date create_date,group_concat(b.vege_name separator ',') as vege_names,"
					+ " a.note note from tb_bh_user_vege a left join tb_b_vegetable b on a.vege_id = b.vege_id,tf_f_user c "
					+ "where a.user_id = c.user_id ";
			if(!Utils.isEmptyString(startDate)){
				sql = sql + " and date_format(a.send_date,'%Y-%m-%d') > ? ";
				sqlParams.add(startDate);
			}
			if(!Utils.isEmptyString(endDate)){
				sql = sql + " and date_format(a.send_date,'%Y-%m-%d') < ? ";
				sqlParams.add(endDate);
			}
			if(!Utils.isEmptyString(userName)){
				sql = sql + " and c.user_name like  '%" + userName + "%' ";
			}
			if(!Utils.isEmptyString(userId)){
				sql = sql + " and a.user_id = ? ";
				sqlParams.add(userId);
			}
			sql = sql + " group by a.send_date,a.user_id order by a.send_date,c.user_name";
			System.out.println("sql:" + sql);
			JSONObject ret = new JSONObject();
			table = DataUtils.queryData(conn, sql, sqlParams, columns, offset, limit);
			ret = Transform.tableToJson(table);
			return ret;
		} finally {
			conn.close();
		}
	}
	
	//在点菜统计功能中用于获取各种蔬菜的点菜量是多少
	public static JSONObject getCountSelectVege(JSONObject params, ActionContext context) throws SQLException, NamingException {
		// 获取参数
		Object columns = params.get("columns");
		Integer limit = params.getInteger("limit");
		Integer offset = params.getInteger("offset");
		String sendDate = params.getString("send_date");
		//System.out.println("sendDate==="+sendDate);	
		
		List<Object> sqlParams = new ArrayList<Object>();
		
		sqlParams.add(sendDate);
		Table table = null;
		Connection conn = context.getConnection(DATASOURCE_WDCYZ);
		try{
			String sql = "select a.vege_id,b.vege_name as vege_name,sum(vege_num*times) as num from tb_b_user_vege a "
					+ " left join tb_b_vegetable b on a.vege_id = b.vege_id where "
					+ " date_format(a.send_date,'%Y-%m-%d') = ? group by a.vege_id";
			JSONObject ret = new JSONObject();
			table = DataUtils.queryData(conn, sql, sqlParams, columns, offset, limit);
			ret = Transform.tableToJson(table);
			//System.out.println("jsonObj==="+ret);	
			return ret;
		} finally {
			conn.close();
		}
	}
	
	//在点菜功能中用于获取各种可点蔬菜的列表及各种蔬菜的剩余量,已经使用下面的getSelVegeForUser方法替代,本方法在单品点菜功能中有使用。
	public static JSONObject getSelectVegeInfo(JSONObject params, ActionContext context) throws SQLException, NamingException {
		// 获取参数
		Object columns = params.get("columns");
		Integer limit = params.getInteger("limit");
		Integer offset = params.getInteger("offset");
		String secFlag = params.getString("sec_flag");
		//System.out.println("secFlag==="+secFlag);	
		
		List<Object> sqlParams = new ArrayList<Object>();
		sqlParams.add(secFlag);
		
		Table table = null;
		Connection conn = context.getConnection(DATASOURCE_WDCYZ);
		try{
			String sql = "select vege_id,vege_name,num,sec_flag,if(cnt is null,num,num-cnt) as surplus from "
					+ " (select a.vege_id,a.vege_name,a.num,a.sec_flag,b.cnt"
					+ " from tb_b_selectvege a left join"
					+ " (select vege_id,count(1) as cnt from tb_b_user_vege group by vege_id) b "
					+ " on a.vege_id = b.vege_id) tmp where sec_flag = ? ";
			JSONObject ret = new JSONObject();
			table = DataUtils.queryData(conn, sql, sqlParams, columns, offset, limit);
			ret = Transform.tableToJson(table);
			//System.out.println("jsonObj==="+ret);	
			return ret;
		} finally {
			conn.close();
		}
	}
	
	//在点菜功能中用于获取各种可点蔬菜的列表及各种蔬菜的剩余量、用户已经点菜的信息
	public static JSONObject getSelVegeForUser(JSONObject params, ActionContext context) throws SQLException, NamingException {
		// 获取参数
		Object columns = params.get("columns");
		Integer limit = params.getInteger("limit");
		Integer offset = params.getInteger("offset");
		String secFlag = params.getString("sec_flag");
		String userId = params.getString("user_id");
		//System.out.println("secFlag==="+secFlag);	
		
		List<Object> sqlParams = new ArrayList<Object>();
		sqlParams.add(secFlag);
		sqlParams.add(userId);
		
		Table table = null;
		Connection conn = context.getConnection(DATASOURCE_WDCYZ);
		try{
			String sql = "select t.*,if(m.vege_num is null,0,m.vege_num) as vege_num,m.times from "
					+ "(select vege_id,vege_name,num,sec_flag,if(descs is null or (descs = ''),'',concat('说明:',descs)) descs,max_sel_num,if (cnt is null,num,num-cnt) as surplus from "
					+ " (select a.vege_id,a.vege_name,a.num,a.sec_flag,a.descs,a.max_sel_num,b.cnt"
					+ " from tb_b_selectvege a left join"
					+ " (select vege_id,sum(vege_num) as cnt from tb_b_user_vege group by vege_id) b "
					+ " on a.vege_id = b.vege_id) tmp where sec_flag = ? ) t"
					+ " left join tb_b_user_vege m on t.vege_id = m.vege_id and m.user_id = ?"
					+ " where t.surplus > 0 or m.vege_num > 0 order by  m.vege_num desc,t.vege_name";
			JSONObject ret = new JSONObject();
			table = DataUtils.queryData(conn, sql, sqlParams, columns, offset, limit);
			ret = Transform.tableToJson(table);
			//System.out.println("jsonObj==="+ret);	
			return ret;
		} finally {
			conn.close();
		}
	}
	
	//按蔬菜点菜量统计数据，倒序输出，可以统计出某种蔬菜在指定的时间内用户点过多少份
	public static JSONObject getVegeVolNum(JSONObject params, ActionContext context) throws SQLException, NamingException {
		// 获取参数
		Object columns = params.get("columns");
		Integer limit = params.getInteger("limit");
		Integer offset = params.getInteger("offset");
		String startDate = params.getString("start_date");
		String endDate = params.getString("end_date");
		if(Utils.isEmptyString(params.getString("filter"))) params.put("filter", "1=1");
		String filter =  params.getString("filter");
		
		//System.out.println("secFlag==="+secFlag);	
		
		List<Object> sqlParams = new ArrayList<Object>();
		List<String> filters = new ArrayList<String>();
		if (!Utils.isEmptyString(filter)) {
			filters.add(filter);
		}
		if (!Utils.isEmptyString(startDate)) {
			filters.add(" send_date >= '" + startDate + "' ");
		}
		if (!Utils.isEmptyString(endDate)) {
			filters.add(" send_date <= '" + endDate +"' ");
		}
		
		Table table = null;
		Connection conn = context.getConnection(DATASOURCE_WDCYZ);
		try{
			String sql = "select a.vege_id vege_id,b.vege_name vege_name,count(1) cnt from tb_bh_user_vege a left join tb_b_vegetable b on a.vege_id = b.vege_id  ";
			JSONObject ret = new JSONObject();
			
			String where = (filters != null && filters.size() > 0) ? " WHERE " + DataUtils.arrayJoin(filters.toArray(), "(%s)", " AND ") : "";
			sql = sql + where + " group by vege_id order by cnt desc";
			//System.out.println("sql==="+sql);
			table = DataUtils.queryData(conn, sql, sqlParams, columns, offset, limit);
			ret = Transform.tableToJson(table);
			//System.out.println("jsonObj==="+ret);	
			return ret;
		} finally {
			conn.close();
		}
	}

	//获取指定时间内指定人员的点菜记录，用于管理员取消
	public static JSONObject getCancelUser(JSONObject params, ActionContext context) throws SQLException, NamingException {
		Object columns = params.get("columns");
		Integer limit = params.getInteger("limit");
		Integer offset = params.getInteger("offset");
		String startDate = params.getString("start_date");
		String endDate = params.getString("end_date");
		String userId = params.getString("userId");
		if(Utils.isEmptyString(params.getString("filter"))) params.put("filter", "1=1");
		String filter =  params.getString("filter");
		//System.out.println("secFlag==="+secFlag);	
		
		List<Object> sqlParams = new ArrayList<Object>();
		List<String> filters = new ArrayList<String>();
		if (!Utils.isEmptyString(userId)) {
			filters.add(" user_id = '" + userId +"' ");
		}
		if (!Utils.isEmptyString(filter)) {
			filters.add(filter);
		}
		if (!Utils.isEmptyString(startDate)) {
			filters.add(" send_date >= '" + startDate + "' ");
		}
		if (!Utils.isEmptyString(endDate)) {
			filters.add(" send_date <= '" + endDate +"' ");
		}
		

		Table table = null;
		Connection conn = context.getConnection(DATASOURCE_WDCYZ);
		try{
			String sql = "select a.user_id,a.times,a.send_date,b.user_name from (select distinct user_id,times,send_date from tb_b_user_vege ";
			JSONObject ret = new JSONObject();
			
			String where = (filters != null && filters.size() > 0) ? " WHERE " + DataUtils.arrayJoin(filters.toArray(), "(%s)", " AND ") : "";
			sql = sql + where + " union select distinct user_id,times,send_date from tb_bh_user_vege " + where + " order by send_date desc ) a left join tf_f_user b on a.user_id = b.user_id";
			System.out.println("sql==="+sql);
			table = DataUtils.queryData(conn, sql, sqlParams, columns, offset, limit);
			ret = Transform.tableToJson(table);
			//System.out.println("jsonObj==="+ret);	
			return ret;
		} finally {
			conn.close();
		}
	}

	//用于管理员取消会员点菜，可取消历史与当次点菜，一次只能取消一个会员的一次点菜
	public static JSONObject cancelUserVege(JSONObject params, ActionContext context) throws SQLException, NamingException,ParseException {
		String sendDate = params.getString("send_date");
		String userId = params.getString("userId");
		int vegeTimes = params.getIntValue("times");
		String operUserId = params.getString("operUserId");//操作员标识，用于判断权限
		JSONObject ret = new JSONObject();
		Connection conn = context.getConnection(DATASOURCE_WDCYZ);
		
		JSONObject logData = new JSONObject();
		String tableName = "";
		logData.put("userId", operUserId);
		logData.put("operType", "管理员取消用户点菜");
		
		String sql = "select vip_type from tf_f_user u where u.status = '1' and u.user_id='" + operUserId + "'";
		Object vipObj = DataUtils.getValueBySQL(conn, sql, null);
		String vipType = vipObj==null?"":vipObj.toString();
		if(!vipType.equals("M")){
			ret.put("code", "-1");
			ret.put("msg", "操作用户没有权限进行此操作！");
			return ret;
		}
		
		String bsql = "delete from tb_b_user_vege where user_id='" + userId + "' and send_date = '" + sendDate + "'";
		String dbsql = "insert into tb_db_user_vege select * from tb_b_user_vege where user_id='" + userId + "' and send_date = '" + sendDate + "'";
		String bhsql = "delete from tb_bh_user_vege where user_id='" + userId + "' and send_date = '" + sendDate + "'";
		String dbhsql = "insert into tb_db_user_vege select * from tb_bh_user_vege where user_id='" + userId + "' and send_date = '" + sendDate + "'";
		String addFeeSql = "update tf_member_add_fee set left_cnt = left_cnt + " + vegeTimes + " where user_id='" + userId + "' and status='1' limit 1";

		sql = "select send_date from tb_d_order_conf ";
		Object sendObj = DataUtils.getValueBySQL(conn, sql, null);
		String curSendDate = sendObj==null?"":sendObj.toString();
		int delnum = 0;
		if(!curSendDate.equals(sendDate)){
			logger.debug("bhsql:"+bhsql);
			modify(dbhsql,conn);
			delnum = modify(bhsql,conn);
			tableName = "tb_bh_user_vege";
		}else{
			logger.debug("bsql:"+bsql);
			modify(dbsql,conn);
			delnum = modify(bsql,conn);
			tableName = "tb_bh_user_vege";
		}
		
		int retnum = 0; 
		if(delnum==0){//取消点菜数据为0，说明没有取消任何数据
			ret.put("code", "-1");
			ret.put("msg", "没有删除任何点菜数据，请确认条件是否正确！");
			return ret;
		}else if(delnum > 0){//取消点菜数据大于0，说明取消点菜数据成功，需要返回点菜次数
			retnum = modify(addFeeSql,conn);
			tableName += ",tf_member_add_fee";
		}
		logData.put("operTable", tableName);


		if(retnum == 1){
			ret.put("code", "0");
			ret.put("msg", "取消点菜数据成功！");
		}else if(retnum > 1){
			ret.put("code", "-1");
			ret.put("msg", "取消点菜数据成功但可能存在多条正在配送的套餐，请确认数据是否正确！");
		}else if(retnum == 0){
			ret.put("code", "-1");
			ret.put("msg", "修改会员套餐剩余次数失败，请确认数据是否正确！");
		}
	    logData.put("operContent", ret.getString("msg") +" 取消用户：" +  userId + " 取消次数：" + vegeTimes);
	    logData.put("operStatus", "成功");
	    WdcyzLogUtil.writeLog(logData, conn);
		
		return ret;
	}
	
	//按蔬菜上架量统计数据，倒序输出，可以统计出某种蔬菜在指定的时间内上架过多少份
	public static JSONObject getSelectVegeVolNum(JSONObject params, ActionContext context) throws SQLException, NamingException {
		// 获取参数
		Object columns = params.get("columns");
		Integer limit = params.getInteger("limit");
		Integer offset = params.getInteger("offset");
		String startDate = params.getString("start_date");
		String endDate = params.getString("end_date");
		if(Utils.isEmptyString(params.getString("filter"))) params.put("filter", "1=1");
		String filter =  params.getString("filter");
		
		//System.out.println("secFlag==="+secFlag);	
		
		List<Object> sqlParams = new ArrayList<Object>();
		List<String> filters = new ArrayList<String>();
		if (!Utils.isEmptyString(filter)) {
			filters.add(filter);
		}
		if (!Utils.isEmptyString(startDate)) {
			filters.add(" create_date >= '" + startDate + "' ");
		}
		if (!Utils.isEmptyString(endDate)) {
			filters.add(" create_date <= '" + endDate +"' ");
		}
		
		Table table = null;
		Connection conn = context.getConnection(DATASOURCE_WDCYZ);
		try{
			String sql = "select a.vege_id vege_id,b.vege_name vege_name,sum(num) select_cnt from tb_bh_selectvege a left join tb_b_vegetable b on a.vege_id = b.vege_id  ";
			JSONObject ret = new JSONObject();
			
			String where = (filters != null && filters.size() > 0) ? " WHERE " + DataUtils.arrayJoin(filters.toArray(), "(%s)", " AND ") : "";
			sql = sql + where + " group by vege_id order by select_cnt desc";
			System.out.println("sql==="+sql);
			table = DataUtils.queryData(conn, sql, sqlParams, columns, offset, limit);
			ret = Transform.tableToJson(table);
			//System.out.println("jsonObj==="+ret);	
			return ret;
		} finally {
			conn.close();
		}
	}
	
	//在每次的点菜完成后，执行归档操作，将点菜配置、当次的可点菜单，当次用户点菜信息进行备份，清空配置表和用户点菜记录表
	@SuppressWarnings("finally")
	public static JSONObject fileSeleVege(JSONObject params, ActionContext context) throws SQLException,BatchUpdateException, NamingException {
		Connection conn = context.getConnection(DATASOURCE_WDCYZ);
		JSONObject ret = new JSONObject();
		try{
			//
			String confCopySql = "insert into tb_dh_order_conf(conf_id,open_date,end_date,send_date,vege_num_min,vege_num,sec_num,vege_desc,select_desc) "
					+ " select conf_id,open_date,end_date,send_date,vege_num_min,vege_num,sec_num,vege_desc,select_desc from tb_d_order_conf";
			String confDelSql = "delete from tb_d_order_conf";
			String userSelectCopySql = "insert into tb_bh_user_vege(record_id,user_id,create_user_id,vege_id,vege_num,times,create_date,send_date,note) "
					+ " select record_id,user_id,create_user_id,vege_id,vege_num,times,create_date,send_date,note from tb_b_user_vege";
			String userSelectDelSql = "delete from tb_b_user_vege";
			String selectVegeCopySql = "insert into tb_bh_selectvege(order_conf_id,vege_id,vege_name,num,max_sel_num,descs,create_date,sec_flag) "
					+ " select conf_id,vege_id,vege_name,num,max_sel_num,descs,create_date,sec_flag from tb_b_selectvege,tb_d_order_conf";
			
			ArrayList<String> sqlList = new ArrayList<String>();
			sqlList.add(confCopySql);
			sqlList.add(userSelectCopySql);
			sqlList.add(selectVegeCopySql);
			sqlList.add(userSelectDelSql);
			sqlList.add(confDelSql);
			
			
			updateBatch(sqlList,conn);
			ret.put("code", "0");
			//System.out.println("jsonObj==="+ret);	
			
		} catch(BatchUpdateException bue){
			conn.rollback();
			ret.put("code", "-1");
			ret.put("msg", "归档时发生错误，请联系管理员！");
			throw bue;
		}finally {
			conn.close();
			return ret;
		}
	}

	//获取会员信息
	public static JSONObject getUserInfo(JSONObject params, ActionContext context) throws SQLException, NamingException {
		// 获取参数
		Object columns = params.get("columns");
		Integer limit = params.getInteger("limit");
		Integer offset = params.getInteger("offset");
		String userId = params.getString("user_id");
		//System.out.println("userId==="+userId);	
		if(Utils.isEmptyString(params.getString("filter"))) params.put("filter", "1=1");
		String filter =  params.getString("filter");
		
		//System.out.println("secFlag==="+secFlag);	
		
		List<Object> sqlParams = new ArrayList<Object>();
		List<String> filters = new ArrayList<String>();
		if (!Utils.isEmptyString(filter)) {
			filters.add(filter);
		}
		if (!Utils.isEmptyString(userId)) {
			filters.add(" a.user_id = '" + userId + "' ");
		}		
				
		Table table = null;
		Connection conn = context.getConnection(DATASOURCE_WDCYZ);
		try{
			String sql = "select a.USER_ID,a.USER_NO,a.USER_NAME,a.SEX,a.CREATE_TIME,a.USER_MOBILE,a.USER_ADDR,a.VIP_TYPE,a.`STATUS`,a.SEND_TYPE,b.ADD_FEE_DATE,b.START_DATE,b.END_DATE,b.LEFT_CNT,c.VIP_TYPE_NAME,d.STATUS_NAME from tf_f_user a "
					+ " left join tf_member_add_fee b on a.user_id = b.user_id and b.status = '1' and b.left_cnt > 0 and b.end_date > curdate() "
					+ " left join tb_d_vip_type c on a.vip_type = c.vip_type_id "
					+ " left join tb_d_status d on a.`status` = d.status_id ";
			String where = (filters != null && filters.size() > 0) ? " WHERE " + DataUtils.arrayJoin(filters.toArray(), "(%s)", " AND ") : "";
			sql = sql + where + " order by a.user_id asc";
			
			JSONObject ret = new JSONObject();
			table = DataUtils.queryData(conn, sql, sqlParams, columns, offset, limit);
			ret = Transform.tableToJson(table);
			//System.out.println("jsonObj==="+ret);	
			return ret;
		} finally {
			conn.close();
		}
	}

	//获取成员续费信息
	public static JSONObject getMemberAddFee(JSONObject params, ActionContext context) throws SQLException, NamingException {
		// 获取参数
		Object columns = params.get("columns");
		Integer limit = params.getInteger("limit");
		Integer offset = params.getInteger("offset");
		String userId = params.getString("user_id");
		//System.out.println("userId==="+userId);	
		if(Utils.isEmptyString(params.getString("filter"))) params.put("filter", "1=1");
		String filter =  params.getString("filter");
		
		//System.out.println("secFlag==="+secFlag);	
		
		List<Object> sqlParams = new ArrayList<Object>();
		List<String> filters = new ArrayList<String>();
		if (!Utils.isEmptyString(filter)) {
			filters.add(filter);
		}
		if (!Utils.isEmptyString(userId)) {
			filters.add(" a.user_id = '" + userId + "' ");
		}		
		
		Table table = null;
		Connection conn = context.getConnection(DATASOURCE_WDCYZ);
		try{
			String sql = "select a.ID,a.user_id,d.vip_card_no,a.add_fee_date,a.vip_type,a.start_date,a.end_date,a.left_cnt,a.`status`,a.note,b.vip_type_name,c.status_name from tf_member_add_fee a  "
					+ " left join tb_d_vip_type b on a.vip_type = b.vip_type_id "
					+ " left join tb_d_status c on a.`status` = c.status_id "
					+ " left join tf_f_user d on a.user_id = d.USER_ID";
			String where = (filters != null && filters.size() > 0) ? " WHERE " + DataUtils.arrayJoin(filters.toArray(), "(%s)", " AND ") : "";
			sql = sql + where + " order by a.start_date desc";
			
			JSONObject ret = new JSONObject();
			table = DataUtils.queryData(conn, sql, sqlParams, columns, offset, limit);
			ret = Transform.tableToJson(table);
			//System.out.println("jsonObj==="+ret);	
			return ret;
		} finally {
			conn.close();
		}
	}

	//获取成员荤菜套餐信息
	public static JSONObject getMemberAcct(JSONObject params, ActionContext context) throws SQLException, NamingException {
		// 获取参数
		Object columns = params.get("columns");
		Integer limit = params.getInteger("limit");
		Integer offset = params.getInteger("offset");
		String userId = params.getString("user_id");
		System.out.println("userId==="+userId);	
		if(Utils.isEmptyString(params.getString("filter"))) params.put("filter", "1=1");
		String filter =  params.getString("filter");
		
		//System.out.println("secFlag==="+secFlag);	
		
		List<Object> sqlParams = new ArrayList<Object>();
		List<String> filters = new ArrayList<String>();
		if (!Utils.isEmptyString(filter)) {
			filters.add(filter);
		}
		if (!Utils.isEmptyString(userId)) {
			filters.add(" a.user_id = '" + userId + "' ");
		}		
		
		Table table = null;
		Connection conn = context.getConnection(DATASOURCE_WDCYZ);
		try{
			String sql = "select a.id,a.acct_no,a.user_id,a.balance,date_format(a.create_time,'%Y-%m-%d') create_time,a.note,"
					+ "b.VIP_NAME as vip_name,b.VIP_CARD_NO as vip_card_no,b.`STATUS` as `status`,"
					+ "c.status_name "
					+ "from tf_f_acct a left outer join tf_f_user b on a.user_id = b.USER_ID "
					+ "left outer join tb_d_status c on b.`STATUS` = c.status_id";
			String where = (filters != null && filters.size() > 0) ? " WHERE " + DataUtils.arrayJoin(filters.toArray(), "(%s)", " AND ") : "";
			sql = sql + where ;
			JSONObject ret = new JSONObject();
			table = DataUtils.queryData(conn, sql, sqlParams, columns, offset, limit);
			ret = Transform.tableToJson(table);
			//System.out.println("jsonObj==="+ret);	
			return ret;
		} finally {
			conn.close();
		}
	}

	//获取成员荤菜套餐消费记录信息
	public static JSONObject getAcctLog(JSONObject params, ActionContext context) throws SQLException, NamingException {
		// 获取参数
		Object columns = params.get("columns");
		Integer limit = params.getInteger("limit");
		Integer offset = params.getInteger("offset");
		String acctId = params.getString("acct_id");
		System.out.println("acctId==="+acctId);	
		if(Utils.isEmptyString(params.getString("filter"))) params.put("filter", "1=1");
		String filter =  params.getString("filter");
		String orderBy =  params.getString("orderBy");
		
		//System.out.println("secFlag==="+secFlag);	
		
		List<Object> sqlParams = new ArrayList<Object>();
		List<String> filters = new ArrayList<String>();
		if (!Utils.isEmptyString(filter)) {
			filters.add(filter);
		}
		if (!Utils.isEmptyString(acctId)) {
			filters.add(" a.acct_id = '" + acctId + "' ");
		}		
		
		Table table = null;
		Connection conn = context.getConnection(DATASOURCE_WDCYZ);
		try{
			String sql = "select a.id,a.acct_id,a.oper_type,a.oper_cash,a.balance,a.oper_time,a.channel,a.order_id,"
					+ "b.content,c.channel_name,d.type_name"
					+ " from tf_f_acct_log a left outer join tf_f_order b on a.order_id=b.ORDER_ID "
					+ " left outer join tb_d_channel c on a.channel = c.channel_id "
					+ " left outer join tb_d_oper_type d on a.oper_type = d.type_id";
			String where = (filters != null && filters.size() > 0) ? " WHERE " + DataUtils.arrayJoin(filters.toArray(), "(%s)", " AND ") : "";
			orderBy = !Utils.isEmptyString(orderBy) ? " ORDER BY " + orderBy : "";
			sql = sql + where + orderBy;
			JSONObject ret = new JSONObject();
			table = DataUtils.queryData(conn, sql, sqlParams, columns, offset, limit);
			ret = Transform.tableToJson(table);
			//System.out.println("jsonObj==="+ret);	
			return ret;
		} finally {
			conn.close();
		}
	}
	//保存订单信息
	public static JSONObject saveOrderInfo(JSONObject params, ActionContext context) throws SQLException, NamingException ,ParseException{
		// 获取参数
		Object columns = params.get("columns");
		String userId = params.getString("user_id");
		//JSONObject jsonTable = params.getJSONObject("tables");
		JSONArray tables = params.getJSONArray("tables");
		System.out.println("data==="+tables);	
		Connection conn = context.getConnection(DATASOURCE_WDCYZ);
		try {
			conn.setAutoCommit(false);
			if (tables != null && tables.size()>0) {
				for(Object jsonTable : tables){
					Table table = Transform.jsonToTable((JSONObject)jsonTable);
					System.out.println("table==="+table.getTableName());
					String tableName = table.getTableName();
					DataUtils.saveData(conn, table);
				}
			}
			return null;
		} finally {
			conn.close();
		}
	}
	
	@SuppressWarnings("finally")
	public static JSONObject login(JSONObject params, ActionContext context) throws SQLException, NamingException, IOException {
		// 获取参数
		Object columns = params.get("columns");
		Integer limit = params.getInteger("limit");
		Integer offset = params.getInteger("offset");
		
		String userNo = params.getString("USER_NO");
		String passwd = params.getString("USER_PASSWD");
		
//        System.out.println("fPhoneNumber="+fPhoneNumber+"#fPassWord="+fPassWord);
		JSONObject ret = new JSONObject();
		List<Object> sqlParams = new ArrayList<Object>();
		System.out.println("userno==="+userNo + "  passwd==="+passwd);	
		//sqlParams.add(userNo);
		
		Connection conn = context.getConnection(DATASOURCE_WDCYZ);

		try {
			String querysql = "select user_passwd from tf_f_user u where u.status = '1' and (u.user_no='" + userNo + "' or u.user_mobile='" + userNo + "' or u.vip_card_no='"+ userNo + "')";
			Object passStr = DataUtils.getValueBySQL(conn, querysql, null);
			String dbPasswd = passStr==null?"":passStr.toString();
			String secPasswd = "";
			if(!dbPasswd.equals("")){
				secPasswd = SecurityUtil.pWORD(dbPasswd, 2);
			}else{
				ret.put("msg", "用户状态不正常或用户名不存在！");
				return ret;
			}
			System.out.println("userNo==="+userNo + "input password===" + passwd + " password===" + secPasswd + " dbpassword===" + dbPasswd);	
			if(passwd.equals(secPasswd)){
				//System.out.println("-------------------------------");	
				querysql = "select USER_ID,USER_NO,USER_NAME,SEX,USER_MOBILE,USER_ADDR,VIP_CARD_NO,VIP_TYPE,USER_WEX_ID from tf_f_user u where (u.user_no='" + userNo + "' or u.user_mobile='" + userNo + "' or u.vip_card_no='"+ userNo + "')";
				Table table = DataUtils.queryData(conn, querysql, sqlParams, columns, offset, limit);
				ret = Transform.tableToJson(table);
			}else{
				ret.put("msg", "用户名或密码错误！");
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new IOException("用户登录异常！");
		}finally {
			conn.close();
			return ret;
		}
	}	   
	public static JSONObject regUser(JSONObject params, ActionContext context) throws SQLException, NamingException, IOException {
		// 获取参数
		String userID = params.getString("USER_ID");
		String userNO = params.getString("USER_NO");
		String userName = params.getString("USER_NAME");
		String sex = params.getString("SEX");
		String userMobile = params.getString("USER_MOBILE");
		String userAddr = params.getString("USER_ADDR");
		String userPasswd = params.getString("USER_PASSWD");
		String passWord = SecurityUtil.pWORD(userPasswd, 1);
		
		Connection conn = context.getConnection(DATASOURCE_WDCYZ);

		String querysql = "insert tf_f_user(user_id,user_no,user_name,sex,user_mobile,user_addr,user_passwd,create_time) values(?,?,?,?,?,?,?,?)";
		try {
			java.sql.PreparedStatement pstmt = null;
			pstmt = conn.prepareStatement(querysql);
			pstmt.setString(1, userID);
			pstmt.setString(2, userNO);
			pstmt.setString(3, userName);
			pstmt.setString(4, sex);
			pstmt.setString(5, userMobile);
			pstmt.setString(6, userAddr);
			pstmt.setString(7, passWord);
			
			Date dDate = new Date();
			SimpleDateFormat df = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss") ;
		    String strDate = df.format (dDate) ;    
			pstmt.setString(8, strDate);
			
			pstmt.execute();
			JSONObject ret = new JSONObject();
			ret.put("code", 0);
			return ret;

//			System.out.println("jsonObj==="+Transform.tableToJson(table));	
			
		} finally {
			conn.close();
		}
	}	   
	
	public static JSONObject chgPass(JSONObject params, ActionContext context) throws SQLException, NamingException, IOException, ParseException {
		// 获取参数
		String userID = params.getString("userId");
		String oldPasswd = params.getString("oldPass");
		String newPasswd = params.getString("newPass");
		
		Connection conn = context.getConnection(DATASOURCE_WDCYZ);
		JSONObject ret = new JSONObject();
		List<Object> sqlParams = new ArrayList<Object>();
		sqlParams.add(userID);
		String sql = "select user_passwd from tf_f_user u where u.status = '1' and u.user_id=?";
		Object passStr = DataUtils.getValueBySQL(conn, sql, sqlParams);
		String dbPasswd = passStr==null?"":SecurityUtil.pWORD(passStr.toString(), 2);
		String secPass = SecurityUtil.pWORD(newPasswd, 1);
		if(!dbPasswd.equals("") && oldPasswd.equals(dbPasswd)){
			sqlParams.clear();
			sqlParams.add(secPass);
			sqlParams.add(userID);
			sql = "update tf_f_user u set user_passwd = '"+ secPass +"' where u.status = '1' and u.user_id='"+userID+"'";
			int num = modify(sql,conn);
			if(num==1){
				ret.put("code","0");
				ret.put("msg", "修改密码成功！");
			}else{
				ret.put("code","-1");
				ret.put("msg", "修改密码失败，用户状态不正确或用户不存在！");
			}
		}else{
			ret.put("code","-1");
			ret.put("msg", "输入原密码不正确，请确认！");
		}
		return ret;
	}
	
	public static JSONObject addUser(JSONObject params, ActionContext context) throws SQLException, NamingException, IOException, ParseException {
		// 获取参数
		String userID = params.getString("USER_ID");
		String userNO = params.getString("USER_NO");
		String userName = params.getString("USER_NAME");
		String sex = params.getString("SEX");
		String userMobile = params.getString("USER_MOBILE");
		String userAddr = params.getString("USER_ADDR");
		String userPasswd = params.getString("USER_PASSWD");
		String passWord = SecurityUtil.pWORD(userPasswd, 1);
		String vipCardNo = params.getString("VIP_CARD_NO");
		String vipType = params.getString("VIP_TYPE");
		String userWexId = params.getString("USER_WEX_ID");
		String userWexNo = params.getString("USER_WEX_NO");
		String userDesc = params.getString("USER_DESC");
		String userStatus = params.getString("STATUS");
		String addFeeDate = params.getString("ADD_FEE_DATE");
		
		Connection conn = context.getConnection(DATASOURCE_WDCYZ);
		/*`USER_ID`,  `USER_NO`,  `test_num`,  `USER_NAME`,  `SEX`,  `CREATE_TIME`,  `USER_MOBILE`,  `USER_ADDR`,  
		`VIP_CARD_NO`,  `VIP_TYPE`,  `USER_WEX_ID`,  `USER_WEX_NO`,  `USER_PASSWD`,  LEFT(`USER_DESC`, 256),  
		`STATUS`,  `START_DATE`,  `END_DATE`,  `ADD_FEE_DATE` FROM `wdcyz`.`tf_f_user` LIMIT 1000;*/
		String querysql = "insert tf_f_user(user_id,user_no,user_name,sex,user_mobile,user_addr,user_passwd,create_time"
				+"VIP_CARD_NO,VIP_TYPE,USER_WEX_ID,USER_WEX_NO,USER_DESC,STATUS,ADD_FEE_DATE) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			java.sql.PreparedStatement pstmt = null;
			pstmt = conn.prepareStatement(querysql);
			pstmt.setString(1, userID);
			pstmt.setString(2, userNO);
			pstmt.setString(3, userName);
			pstmt.setString(4, sex);
			pstmt.setString(5, userMobile);
			pstmt.setString(6, userAddr);
			pstmt.setString(7, passWord);
			
			Date dDate = new Date();
			SimpleDateFormat df = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss") ;
		    String strDate = df.format (dDate) ; 
		    dDate = df.parse(strDate);
			pstmt.setDate(8, (java.sql.Date) dDate);
			pstmt.setString(9, vipCardNo);
			pstmt.setString(10, vipType);
			pstmt.setString(11, userWexId);
			pstmt.setString(12, userWexNo);
			pstmt.setString(13, userDesc);
			pstmt.setString(14, userStatus);
			pstmt.setString(15, addFeeDate);
			
			pstmt.execute();
			JSONObject ret = new JSONObject();
			ret.put("code", 0);
			return ret;

//			System.out.println("jsonObj==="+Transform.tableToJson(table));	
			
		} finally {
			conn.close();
		}
	}	   
	/**
	   批量修改数据库
	   @param alSqls - SQL批处理语句数组
	   @param oStatement
	   @throws SQLException@throws java.sql.SQLException
	    */
	   public static void updateBatch(ArrayList<String> alSqls, Connection oConn) throws SQLException 
	   {
	      Statement oStatement =null;
	      try
	      {
	         oStatement =oConn.createStatement();
	         for (int i = 0 ; i < alSqls.size () ; i++) 
	         {
	        	System.out.println("SQL:"+(String) alSqls.get (i));
	            oStatement.addBatch((String) alSqls.get (i)) ;
	         }
	         oStatement.executeBatch () ;
	      }catch(SQLException sqle){
	    	  oConn.rollback();
	    	  sqle.printStackTrace();
	    	  throw sqle;
	      }
	      finally
	      {
	    	  oStatement.close();
	      }
	   }

	   /**
	   执行数据库的非查询sql语句
	   @param sSql - SQL语句
	   @param oConn - 数据库连接
	   @return int
	   @throws SQLException@throws java.sql.SQLException
	    */
	   public static int modify(String sSql, Connection oConn) throws SQLException 
	   {
	      Statement st =null;
	      try
	      {
	         st = oConn.createStatement () ;
	         //返回修改的行数
	         return st.executeUpdate (sSql);
	      }
	      finally
	      {
	         try
	         {
	            if(st !=null)
	            {
	               st.close();
	               st =null;
	            }
	         }catch(Exception e){e.printStackTrace();}
	      }
	   }
}
