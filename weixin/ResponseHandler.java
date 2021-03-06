package weixin;  
  
  
import java.io.IOException;  
import java.io.PrintWriter;  
import java.io.UnsupportedEncodingException;  
import java.util.Iterator;  
import java.util.Map;  
import java.util.Set;  
import java.util.SortedMap;  
import java.util.TreeMap;  
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
  
//import com.tenpay.util.MD5Util;  
//import com.tenpay.util.TenpayUtil;  
  
/** 
 *  
 * @author miklchen 
 * 
 */  
public class ResponseHandler {   
  
    private String key;  
      
  
    private SortedMap parameters;   
      
      
    private String debugInfo;  
      
    private HttpServletRequest request;  
      
    private HttpServletResponse response;  
      
    private String uriEncoding;  
      
    /** 
     *  
     * @param request 
     * @param response 
     */  
    public ResponseHandler(HttpServletRequest request,  
            HttpServletResponse response)  {  
        this.request = request;  
        this.response = response;  
  
        this.key = "";  
        this.parameters = new TreeMap();  
        this.debugInfo = "";  
          
        this.uriEncoding = "";  
    }  
      
    /** 
 
    */  
    public String getKey() {  
        return key;  
    }  
  
    /** 
    * 
    */  
    public void setKey(String key) {  
        this.key = key;  
    }  
  
    /** 
     *ֵ 
     * @param parameter  
     * @return String  
     */  
    public String getParameter(String parameter) {  
        String s = (String)this.parameters.get(parameter);   
        return (null == s) ? "" : s;  
    }  
      
    /** 
     * @param parameter 
     * @param parameterValueֵ 
     */  
    public void setParameter(String parameter, String parameterValue) {  
        String v = "";  
        if(null != parameterValue) {  
            v = parameterValue.trim();  
        }  
          
        this.parameters.put(parameter, v);  
    }  
      
    /** 
     *  
     * @return SortedMap 
     */  
    public SortedMap getAllParameters() {  
        return this.parameters;  
    }  
      
    public void setAllparamenters(SortedMap map){  
        this.parameters = map;  
    }  
      
    /** 
     * 微信异步回调签名 
     * @return boolean 
     */  
    public boolean isTenpaySign() {  
        StringBuffer sb = new StringBuffer();  
        Set es = this.parameters.entrySet();  
        Iterator it = es.iterator();  
        while(it.hasNext()) {  
            Map.Entry entry = (Map.Entry)it.next();  
            String k = (String)entry.getKey();  
            String v = (String)entry.getValue();  
            if(!"sign".equals(k) && null != v && !"".equals(v)) {  
                sb.append(k + "=" + v + "&");  
            }  
        }  
          
        sb.append("key="+this.getKey());  
        //System.out.println("sb.toString():"+sb.toString());  
        String enc = "UTF-8";  
        String sign = MD5Util.MD5Encode(sb.toString(), enc).toLowerCase();  
          
        String tenpaySign = this.getParameter("sign").toLowerCase();  
          
        System.out.println("sign:"+sign+"      tenpaysign:"+tenpaySign);  
          
        return tenpaySign.equals(sign);  
    }  
 
	private static String genMD5Str(String str) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
			byte []bytes=md5.digest(str.getBytes("utf8"));
			StringBuilder ret=new StringBuilder(bytes.length<<1);
			for(int i=0;i<bytes.length;i++){
			  ret.append(Character.forDigit((bytes[i]>>4)&0xf,16));
			  ret.append(Character.forDigit(bytes[i]&0xf,16));
			}
			return ret.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
    /** 
     *  
     * @throws IOException  
     */  
    public void sendToCFT(String msg) throws IOException {  
        String strHtml = msg;  
        PrintWriter out = this.getHttpServletResponse().getWriter();  
        out.println(strHtml);  
        out.flush();  
        out.close();  
  
    }  
      
    /** 
     * 
     * @return String 
     */  
    public String getUriEncoding() {  
        return uriEncoding;  
    }  
  
    /** 
     * @param uriEncoding 
     * @throws UnsupportedEncodingException 
     */  
    public void setUriEncoding(String uriEncoding)  
            throws UnsupportedEncodingException {  
        if (!"".equals(uriEncoding.trim())) {  
            this.uriEncoding = uriEncoding;  
  
              
            String enc = "UTF-8";  
            Iterator it = this.parameters.keySet().iterator();  
            while (it.hasNext()) {  
                String k = (String) it.next();  
                String v = this.getParameter(k);  
                v = new String(v.getBytes(uriEncoding.trim()), enc);  
                this.setParameter(k, v);  
            }  
        }  
    }  
  
    /** 
     
    */  
    public String getDebugInfo() {  
        return debugInfo;  
    }  
      
    /** 
    * 
    */  
    protected void setDebugInfo(String debugInfo) {  
        this.debugInfo = debugInfo;  
    }  
      
    protected HttpServletRequest getHttpServletRequest() {  
        return this.request;  
    }  
      
    protected HttpServletResponse getHttpServletResponse() {  
        return this.response;  
    }  
      
}  