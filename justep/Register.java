package justep;

import java.io.IOException;
import java.security.Security;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.NamingException;
import javax.servlet.ServletException;

import com.alibaba.fastjson.JSONObject;
import com.justep.baas.action.ActionContext;
import com.justep.baas.data.DataUtils;

public class Register{
	//创建验证码
	public static String gerCode(){
		String str = "0,1,2,3,4,5,6,7,8,9";  
        String str2[] = str.split(",");//将字符串以,分割  
        Random rand = new Random();//创建Random类的对象rand  
        int index = 0;  
        String randStr = "";//创建内容为空字符串对象randStr  
        randStr = "";//清空字符串对象randStr中的值  
        for (int i=0; i<4; ++i)  
        {  
            index = rand.nextInt(str2.length-1);//在0到str2.length-1生成一个伪随机数赋值给index  
            randStr += str2[index];//将对应索引的数组与randStr的变量值相连接  
       }  
        return randStr;
	}
	//给邮箱发送验证码：
	public static JSONObject sendEmail(JSONObject params, ActionContext context) throws ServletException, IOException, AddressException, MessagingException {
		//获取邮箱
		String email = params.getString("param");
        
		//设置发送者的用户名
		String SEND_UNAME = "1835412838";
		//设置发送者的授权码
		String SEND_PWD = "vehvbzdkcvcgdige";
		String VALUE_SMTP = "smtp.qq.com";
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		Properties props = System.getProperties();
		props.setProperty("mail.smtp.host", "smtp.qq.com");
		props.setProperty("mail.smtp.port", "25");
		props.put("mail.smtp.auth", "true");
		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.port", "465");
		props.setProperty("mail.smtp.socketFactory.port", "465");
		
		final String username = "1835412838";
		final String password = "vehvbzdkcvcgdige";
		Session session = Session.getDefaultInstance(props,
		        new Authenticator() {
		            protected PasswordAuthentication getPasswordAuthentication() {
		                return new PasswordAuthentication(username, password);
		            }
		        });
		
		session.setDebug(true);
		Message msg = new MimeMessage(session);
		
		msg.setFrom(new InternetAddress(username + "@qq.com"));
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(
				email, false));
		msg.setSentDate(new Date());
		
		String VerifCode = gerCode();
        msg.setText(VerifCode);
        msg.setSentDate(new Date());
        Transport transport = session.getTransport("smtp");
		// smtp验证，就是你用来发邮件的邮箱用户名和授权码
		transport.connect(VALUE_SMTP, SEND_UNAME, SEND_PWD);
		// 发送
		transport.sendMessage(msg, msg.getAllRecipients());
		Transport.send(msg);
		transport.close();
		JSONObject object = new JSONObject();
		object.put("code", VerifCode);
		System.out.println(object);
		return object;
	}
	
	//查看输入的邮箱是否存在
	public static JSONObject checkUsername(JSONObject params,ActionContext context) throws SQLException, NamingException{
		JSONObject result = new JSONObject();
		String email = params.getString("param");
		Connection conn=null;
		try{
			conn = context.getConnection("demo");
			String sql = "SELECT COUNT(fPhoneNumber) FROM NETEASE_USER WHERE fPhoneNumber='"+email+"'";
			System.out.println(sql);
			int count = Integer.parseInt(DataUtils.getValueBySQL(conn, sql, null).toString());
			result.put("count", count);
			return result;
		} finally { 
			if(conn!=null){
				conn.close();
			}
			
		}
	}

}
