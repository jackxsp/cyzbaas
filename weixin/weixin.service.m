<?xml version="1.0" encoding="UTF-8"?>
<model>
	<action name="service" impl="weixin.Service.service">
	</action>
	<action name="jsapi" impl="weixin.WxMpJsApi.service">
	</action>
	<action name="notify" impl="weixin.WxPayNotify.service">
	</action>
	<action name="userinfo" impl="weixin.UserInfo.service">
		<public name="code" type="String" />
	</action>
</model>