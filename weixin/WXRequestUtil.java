package weixin;  
  
import java.io.BufferedReader;  
import java.io.ByteArrayInputStream;  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.InputStream;  
import java.io.InputStreamReader;  
import java.io.OutputStream;  
import java.net.ConnectException;  
import java.net.HttpURLConnection;  
import java.net.InetAddress;  
import java.net.URL;  
import java.security.KeyStore;  
import java.util.Date;  
import java.util.HashMap;  
import java.util.Iterator;  
import java.util.List;  
import java.util.Map;  
import java.util.SortedMap;  
import java.util.TreeMap;  
import com.alibaba.fastjson.JSONObject;
import javax.net.ssl.SSLContext;  
import java.text.ParseException;
import java.lang.reflect.Method;
import org.apache.http.Consts;  
import org.apache.http.HttpEntity;  
import org.apache.http.client.methods.CloseableHttpResponse;  
import org.apache.http.client.methods.HttpPost;  
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;  
import org.apache.http.conn.ssl.SSLContexts;  
import org.apache.http.entity.StringEntity;  
import org.apache.http.impl.client.CloseableHttpClient;  
import org.apache.http.impl.client.HttpClients;  
import org.apache.http.util.EntityUtils;  
import org.glassfish.jersey.internal.util.Base64;  
import org.jdom.Document;  
import org.jdom.Element;  
import org.jdom.input.SAXBuilder;  
  
//import com.zhiweism.util.MD5;
//import com.zhiweism.util.Util;  
  
/* 
 * 用户发起统一下单请求 
 * 作者：董志平 
 * 用于发起微信扫码支付接口 
 */  
public class WXRequestUtil {  
    private static String WXSign = null;  
//  //测试  
    public static void main(String[] args) {  
        //Map<String,String> res = SendPayment("苹果","20170106113325",1,0);  
    }  
      
    /* 
     * 发起支付请求 
     * body 商品描述 
     * out_trade_no 订单号 
     * total_fee    订单金额        单位  元 
     * product_id   商品ID 
     */  
    public static Map<String,String> SendPayment(String body,String out_trade_no,double total_fee,int app_type){  
        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";  
        String xml = WXParamGenerate(body,out_trade_no,total_fee,app_type);  
        String res = httpsRequest(url,"POST",xml);  
        Map<String,String> data = null;  
        try {  
            data = doXMLParse(res);  
        } catch (Exception e) {  
            data = null;  
        }  
        return data;  
    }  
  
      
    /** 
     * 获取签名 
     * @return 
     */  
    public static String getWXSign() {  
        return WXSign;  
    }  
  
    /** 
     *  获得随机字符串 
     *  
     */  
    public static String NonceStr(){  
        String res = Base64.encodeAsString(Math.random()+"::"+new Date().toString()).substring(0, 30);  
        return res;  
    }  
      
    /** 
     * 获取时间戳 
     */  
    public static String GetTimeStamp(){  
        int t = (int)(System.currentTimeMillis()/1000);  
        return t+"";  
    }  
      
      
    /** 
     *  获取用户的ip 
     *  
     */  
     public static String GetIp() {  
        InetAddress ia=null;  
        try {  
            ia=InetAddress.getLocalHost();  
            String localip=ia.getHostAddress();  
            return localip;  
        } catch (Exception e) {  
            return null;  
        }  
    }  
       
     /** 
      * 获取签名 
      *  
      */  
     public static String GetSign(Map<String,String> param,int app_type){  
        //String StringA =  Util.formatUrlMap(param, false, false);  
        //String stringSignTemp = WxMpCryptUtil.genWithAmple(param,app_type).toUpperCase();  
        return null;  
     }  
       
     /** 
      *  
      * Map转xml数据 
      */  
     public static String GetMapToXML(Map<String,String> param){  
         StringBuffer sb = new StringBuffer();  
         sb.append("<xml>");  
         for (Map.Entry<String,String> entry : param.entrySet()) {   
                sb.append("<"+ entry.getKey() +">");  
                sb.append(entry.getValue());  
                sb.append("</"+ entry.getKey() +">");  
        }    
         sb.append("</xml>");  
         return sb.toString();  
     }  
      
      
    //微信统一下单参数设置  
    public static String WXParamGenerate(String description,String out_trade_no,double total_fee,int app_type){  
        int fee = (int)(total_fee * 100.00);  
        Map<String,String> param = new HashMap<String,String>();  
        param.put("appid", "");  
        param.put("mch_id", "");  
        param.put("nonce_str",NonceStr());  
        param.put("body",description);  
        param.put("out_trade_no",out_trade_no);  
        param.put("total_fee", fee+"");  
        param.put("spbill_create_ip", GetIp());  
        param.put("notify_url", "");  
        param.put("trade_type", "APP");  
          
        WXSign = GetSign(param,app_type);  
          
        param.put("sign", WXSign);  
        return GetMapToXML(param);  
    }  
      
    //发起微信支付请求  
    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {    
      try {    
          URL url = new URL(requestUrl);    
          HttpURLConnection conn = (HttpURLConnection) url.openConnection();    
            
          conn.setDoOutput(true);    
          conn.setDoInput(true);    
          conn.setUseCaches(false);    
          // 设置请求方式（GET/POST）    
          conn.setRequestMethod(requestMethod);    
          conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");    
          // 当outputStr不为null时向输出流写数据    
          if (null != outputStr) {    
              OutputStream outputStream = conn.getOutputStream();    
              // 注意编码格式    
              outputStream.write(outputStr.getBytes("UTF-8"));    
              outputStream.close();    
          }    
          // 从输入流读取返回内容    
          InputStream inputStream = conn.getInputStream();    
          InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");    
          BufferedReader bufferedReader = new BufferedReader(inputStreamReader);    
          String str = null;  
          StringBuffer buffer = new StringBuffer();    
          while ((str = bufferedReader.readLine()) != null) {    
              buffer.append(str);    
          }    
          // 释放资源    
          bufferedReader.close();    
          inputStreamReader.close();    
          inputStream.close();    
          inputStream = null;    
          conn.disconnect();    
          return buffer.toString();    
      } catch (ConnectException ce) {    
          System.out.println("连接超时：{}"+ ce);    
      } catch (Exception e) {    
          System.out.println("https请求异常：{}"+ e);    
      }    
      return null;    
    }    
        
    //退款的请求方法    
    public static String httpsRequest2(String requestMethod, String outputStr) throws Exception {    
          KeyStore keyStore  = KeyStore.getInstance("PKCS12");    
          StringBuilder res = new StringBuilder("");    
          FileInputStream instream = new FileInputStream(new File("/home/apiclient_cert.p12"));    
          try {    
              keyStore.load(instream, "".toCharArray());    
          } finally {    
              instream.close();    
          }    
  
          // Trust own CA and all self-signed certs    
          SSLContext sslcontext = SSLContexts.custom()    
                  .loadKeyMaterial(keyStore, "1313329201".toCharArray())    
                  .build();     
          SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(    
                  sslcontext,    
                  new String[] { "TLSv1" },    
                  null,    
                  SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);    
          CloseableHttpClient httpclient = HttpClients.custom()    
                  .setSSLSocketFactory(sslsf)    
                  .build();    
          try {    
  
              HttpPost httpost = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund");    
              httpost.addHeader("Connection", "keep-alive");    
              httpost.addHeader("Accept", "*/*");    
              httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");    
              httpost.addHeader("Host", "api.mch.weixin.qq.com");    
              httpost.addHeader("X-Requested-With", "XMLHttpRequest");    
              httpost.addHeader("Cache-Control", "max-age=0");    
              httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");    
               StringEntity entity2 = new StringEntity(outputStr ,Consts.UTF_8);    
               httpost.setEntity(entity2);    
              System.out.println("executing request" + httpost.getRequestLine());    
  
              CloseableHttpResponse response = httpclient.execute(httpost);    
                 
              try {    
                  HttpEntity entity = response.getEntity();    
                      
                  System.out.println("----------------------------------------");    
                  System.out.println(response.getStatusLine());    
                  if (entity != null) {    
                      System.out.println("Response content length: " + entity.getContentLength());    
                      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));    
                      String text = "";  
                      res.append(text);    
                      while ((text = bufferedReader.readLine()) != null) {    
                          res.append(text);    
                          System.out.println(text);    
                      }    
                         
                  }    
                  EntityUtils.consume(entity);    
              } finally {    
                  response.close();    
              }    
          } finally {    
              httpclient.close();    
          }    
          return  res.toString();    
              
    }  
        
    //xml解析    
    public static Map<String, String> doXMLParse(String strxml) throws Exception {    
          strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");    
          if(null == strxml || "".equals(strxml)) {    
              return null;    
          }    
              
          Map<String,String> m = new HashMap<String,String>();     
          InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));    
          SAXBuilder builder = new SAXBuilder();    
          Document doc = builder.build(in);    
          Element root = doc.getRootElement();    
          List list = root.getChildren();    
          Iterator it = list.iterator();    
          while(it.hasNext()) {    
              Element e = (Element) it.next();    
              String k = e.getName();    
              String v = "";    
              List children = e.getChildren();    
              if(children.isEmpty()) {    
                  v = e.getTextNormalize();    
              } else {    
                  v = getChildrenText(children);    
              }    
                  
              m.put(k, v);    
          }    
              
          //关闭流    
          in.close();     
          return m;    
    }    
      
    //xml解析    
    public static SortedMap<String, String> doXMLParseWithSorted(String strxml) throws Exception {    
          strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");    
          if(null == strxml || "".equals(strxml)) {    
              return null;    
          }    
              
          SortedMap<String,String> m = new TreeMap<String,String>();     
          InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));    
          SAXBuilder builder = new SAXBuilder();    
          Document doc = builder.build(in);    
          Element root = doc.getRootElement();    
          List list = root.getChildren();    
          Iterator it = list.iterator();    
          while(it.hasNext()) {    
              Element e = (Element) it.next();    
              String k = e.getName();    
              String v = "";    
              List children = e.getChildren();    
              if(children.isEmpty()) {    
                  v = e.getTextNormalize();    
              } else {    
                  v = getChildrenText(children);    
              }    
                  
              m.put(k, v);    
          }    
              
          //关闭流    
          in.close();     
          return m;    
    }    
        
    public static String getChildrenText(List children) {    
          StringBuffer sb = new StringBuffer();    
          if(!children.isEmpty()) {    
              Iterator it = children.iterator();    
              while(it.hasNext()) {    
                  Element e = (Element) it.next();    
                  String name = e.getName();    
                  String value = e.getTextNormalize();    
                  List list = e.getChildren();    
                  sb.append("<" + name + ">");    
                  if(!list.isEmpty()) {    
                      sb.append(getChildrenText(list));    
                  }    
                  sb.append(value);    
                  sb.append("</" + name + ">");    
              }    
          }     
          return sb.toString();    
    }  

    /**
     * 将javaBean转换成Map
     *
     * @param javaBean javaBean
     * @return Map对象
     */
    public static Map<String, Object> BeantoMap(Object javaBean) {
        Map<String, Object> result = new HashMap<String, Object>();
        Method[] methods = javaBean.getClass().getDeclaredMethods();

        for (Method method : methods) {
            try {
                if (method.getName().startsWith("get")) {
                    String field = method.getName();//getName  getPassword
                    field = field.substring(field.indexOf("get") + 3);//Name  Password
                    field = field.toLowerCase().charAt(0) + field.substring(1);//name password
                    Object value = method.invoke(javaBean, (Object[]) null);
                    result.put(field, null == value ? "" : value.toString());
                }
            } catch (Exception e) {
            }
        }

        return result;
    }

    /**
     * 将javaBean转换成JSONObject
     *
     * @param bean javaBean
     * @return json对象
     */
    public static JSONObject toJSON(Object bean) {
        return new JSONObject(BeantoMap(bean));
    }

    /**
     * 将map转换成Javabean
     *
     * @param javabean javaBean
     * @param data     map数据
     */
    public static Object toJavaBean(Object javabean, Map<String, Object> data) {
        Method[] methods = javabean.getClass().getDeclaredMethods();
        for (Method method : methods) {
            try {
                if (method.getName().startsWith("set")) {
                    String field = method.getName(); //setName   setPassword
                    field = field.substring(field.indexOf("set") + 3);//Name  Password
                    field = field.toLowerCase().charAt(0) + field.substring(1);//name  password
                    method.invoke(javabean, new Object[]
                            {
                                    data.get(field)
                            });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return javabean;
    }
    
    /**
     * 将json对象转换成Map
     *
     * @param jsonObject json对象
     * @return Map对象
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> JsontoMap(JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<String, Object>();
        Iterator<String> iterator = jsonObject.keySet().iterator();
        String key = null;
        String value = null;
        while (iterator.hasNext()) {
            key = iterator.next();
            value = jsonObject.getString(key);
            result.put(key, value);
        }
        return result;
    }
}  