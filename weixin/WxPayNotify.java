package weixin;

import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletInputStream;


import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.sql.Connection;
import java.util.SortedMap;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wdcyz.Wdcyz;
import weixin.WXRequestUtil;

import com.alibaba.fastjson.JSONObject;
import com.justep.baas.action.ActionContext;
import com.justep.baas.data.DataUtils;
import com.justep.baas.data.Table;
import com.justep.baas.data.Row;
import com.justep.baas.data.Transform;

/**
 * 
 * @author 007slm
 * @email 007slm@163.com
 *
 */
public class WxPayNotify{
	protected static final Logger log = LoggerFactory.getLogger(WxPayNotify.class);
	private static final String DATASOURCE_WDCYZ = "wdcyz";
	public static final String VARIABLE_FLAG = "var-"; 
	
	static WxMpServiceInstance instance = WxMpServiceInstance.getInstance();
	
	public static JSONObject service(JSONObject params, ActionContext context) throws Exception,IOException{
		HttpServletRequest req = (HttpServletRequest)context.get(ActionContext.REQUEST);
		HttpServletResponse resp = (HttpServletResponse)context.get(ActionContext.RESPONSE);

		String retXml = doNotify(context,req,resp);
		System.out.println("retXml:"+retXml);
        resp.getWriter().write(retXml);

		return null;
	}

	private static String doNotify(ActionContext context,HttpServletRequest req,HttpServletResponse resp) throws Exception,IOException {
		resp.setContentType("text/xml;charset=utf-8");
		resp.setCharacterEncoding("utf-8");
		String resultCode = req.getParameter("return_code");
		System.out.println("resultCode:"+resultCode);
		
		Object columns = "";
		Integer limit = 1;
		Integer offset = 0;

		ServletInputStream instream = req.getInputStream();  
        StringBuffer sb = new StringBuffer();  
        int len = -1;  
        byte[] buffer = new byte[1024];  
          
        while((len = instream.read(buffer)) != -1){  
            sb.append(new String(buffer,0,len));  
        }  
        instream.close(); 
        //System.out.println("instream:"+sb.toString());
        
        SortedMap<String,String> map = WXRequestUtil.doXMLParseWithSorted(sb.toString());//接受微信的通知参数  
        Map<String,String> return_data = new HashMap<String,String>();  
      
        //System.out.println("map:"+map.toString());
        //创建支付应答对象  
        ResponseHandler resHandler = new ResponseHandler(req, resp);  
          
        resHandler.setAllparamenters(map);  
        resHandler.setKey(instance.getWxMpConfigStorage().getAppKey());  
        
        //判断签名  
        if(resHandler.isTenpaySign()){  
        	if(!map.get("return_code").toString().equals("SUCCESS")){  
                return_data.put("return_code", "FAIL");  
                return_data.put("return_msg", "return_code不正确");
                return WXRequestUtil.GetMapToXML(return_data);
            }else{  
                if(!map.get("result_code").toString().equals("SUCCESS")){  
                    return_data.put("return_code", "FAIL");  
                    return_data.put("return_msg", "result_code不正确");
                    return WXRequestUtil.GetMapToXML(return_data);
                }
                String out_trade_no = map.get("out_trade_no").toString();  
                String time_end = map.get("time_end").toString();  
                BigDecimal total_fee = new BigDecimal(map.get("total_fee").toString());  
                //付款完成后，支付宝系统发送该交易状态通知  
                System.out.println("交易成功");  
                Table table = null;
        		Connection conn = context.getConnection(DATASOURCE_WDCYZ);
    			JSONObject ret = new JSONObject();
    			List<Object> sqlParams = new ArrayList<Object>();
    			sqlParams.add(out_trade_no);
        		try{
        			String sql = "select * from tf_f_order where order_id = ?";
        			table = DataUtils.queryData(conn, sql, sqlParams, columns, offset, limit);
        			Row order = table.getRows().get(0);
        			System.out.println("row==="+order);
        			System.out.println("rows==="+table.getRows());
        			JSONObject roworder = WXRequestUtil.toJSON(order);
        			System.out.println("row order==="+roworder);	
        			ret = Transform.tableToJson(table);
        			System.out.println("ret==="+ret);	
        			System.out.println("jsonObj==="+ret.get("rows"));	
        			System.out.println("order==="+WXRequestUtil.JsontoMap(ret));

                    if(order == null){  
                        System.out.println("订单不存在");  
                        return_data.put("return_code", "FAIL");  
                        return_data.put("return_msg", "订单不存在");  
                        return WXRequestUtil.GetMapToXML(return_data);  
                    }  
        		
                    String payment_status = order.getString("STATUS");  
                    System.out.println("payment_status==="+payment_status);   
                    BigDecimal p = new BigDecimal("100");    
                    BigDecimal amount = new BigDecimal(String.valueOf(order.getFloat("SUM_MONEY")));  
                    System.out.println("amount==="+amount);  
                    amount  = amount.multiply(p);  
                   
                    //如果订单已经支付返回错误  
                    if("2".equals(payment_status)){  
                        System.out.println("订单已经支付");  
                        return_data.put("return_code", "SUCCESS");  
                        return_data.put("return_msg", "OK");  
                        return WXRequestUtil.GetMapToXML(return_data);  
                    }else if ("1".equals(payment_status)){//修改订单状态为已支付。
                        //如果支付金额不等于订单金额返回错误  
                        if(amount.compareTo(total_fee)!=0){  
                            System.out.println("资金异常");  
                            return_data.put("return_code", "FAIL");  
                            return_data.put("return_msg", "金额异常");  
                            return WXRequestUtil.GetMapToXML(return_data); 
                        }  
                    	sql = "update tf_f_order set `status` = '2',pay_type='1' where order_id = '" + out_trade_no + "'";
                    	Wdcyz.modify(sql, conn);
                    }
               }catch(SQLException sqle){
            	   
               }finally {
            	   conn.close();
               }
            }
        	
        }else{  
            return_data.put("return_code", "FAIL");  
            return_data.put("return_msg", "签名错误");  
            return WXRequestUtil.GetMapToXML(return_data); 
        } 
        return_data.put("return_code", "SUCCESS");  
        return_data.put("return_msg", "OK");  
        String xml = WXRequestUtil.GetMapToXML(return_data); 
        System.out.println("return xml:"+xml);  
        return xml;
	}
}
