package me.chanjar.weixin.common.bean;

import java.io.Serializable;

import me.chanjar.weixin.common.util.xml.XStreamInitializer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("xml")
public class WxMWebUnifiedOrder implements Serializable{
	
	private static final long serialVersionUID = 6161267215135941678L;
	
	@Override
	public String toString() {
		return "appid=" + appId + ",body=" + body + ",mch_id=" + mchId + ",nonce_str=" + nonceStr + ",notify_url=" + notifyUrl + ",openid=" + openId + ",out_trade_no="
				+ outTradeNo + ",spbill_create_ip=" + spbillCreateIp + ",total_fee=" + totalFee + ",trade_type=" + tradeType;
	}
	
	@XStreamAlias("appid")
	private String appId;
	
	
	
	private String body;
	
	

	@XStreamAlias("mch_id")
	private String mchId;
	
	
	
	@XStreamAlias("nonce_str")
	private String nonceStr;
	
	

	@XStreamAlias("notify_url")
	private String notifyUrl;
	
	@XStreamAlias("openid")
	private String openId;
	
	
	@XStreamAlias("out_trade_no")
	private String outTradeNo;
	
	@XStreamAlias("spbill_create_ip")
	private String spbillCreateIp;
	
	@XStreamAlias("total_fee")
	private String totalFee;
	
	@XStreamAlias("trade_type")
	private String tradeType = "JSAPI";
	
	
	
	private String sign;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
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

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getSpbillCreateIp() {
		return spbillCreateIp;
	}

	public void setSpbillCreateIp(String spbillCreateIp) {
		this.spbillCreateIp = spbillCreateIp;
	}

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public String toXml() {
		XStream xstream = XStreamInitializer.getInstance();
	    xstream.processAnnotations(WxJsapiUnifiedOrder.class);
	    return xstream.toXML(this);
	}
}
