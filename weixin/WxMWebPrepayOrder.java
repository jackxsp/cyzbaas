package me.chanjar.weixin.common.bean;

import java.io.Serializable;

import me.chanjar.weixin.common.util.xml.XStreamInitializer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class WxMWebPrepayOrder implements Serializable{
	private static final long serialVersionUID = -7485848947518664720L;
	@XStreamAlias("return_code")
	private String returnCode;
	@XStreamAlias("return_msg")
	private String returnMsg;
	@XStreamAlias("appid")
	private String appId;
	@XStreamAlias("mch_id")
	private String mchId;
	@XStreamAlias("nonce_str")
	private String nonceStr;
	
	private String sign;
	
	@XStreamAlias("result_code")
	private String resultCode;
	@XStreamAlias("prepay_id")
	private String prepayId;
	@XStreamAlias("trade_type")
	private String tradeType;
	
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnMsg() {
		return returnMsg;
	}
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getMchId() {
		return mchId;
	}
	public void setMchId(String mchId) {
		this.mchId = mchId;
	}
	public String getNonceStr() {
		return nonceStr;
	}
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getPrepayId() {
		return prepayId;
	}
	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	
	public static WxJsapiPrepayOrder fromXml(String xml) {
		XStream xstream = XStreamInitializer.getInstance();
	    xstream.processAnnotations(WxJsapiPrepayOrder.class);
	    return (WxJsapiPrepayOrder)xstream.fromXML(xml);
	}
	
	public String toXml() {
		XStream xstream = XStreamInitializer.getInstance();
	    xstream.processAnnotations(WxJsapiPrepayOrder.class);
	    return xstream.toXML(this);
	}
}
