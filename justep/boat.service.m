<?xml version="1.0" encoding="UTF-8"?>
<model xmlns="http://www.justep.com/model">
	<action xmlns="http://www.w3.org/1999/xhtml" name="queryUser"
		impl="action:common/CRUD/query">
		<private name="condition" type="String"></private>
		<private name="db" type="String">boat</private>
		<private name="tableName" type="String">user</private>
		<public name="columns" type="Object"></public>
		<public name="filter" type="String"></public>
		<public name="limit" type="Integer"></public>
		<public name="offset" type="Integer"></public>
		<public name="orderBy" type="String"></public>
		<public name="variables" type="Object"></public>
	</action>
	<action xmlns="http://www.w3.org/1999/xhtml" name="saveUser"
		impl="action:common/CRUD/save">
		<private name="db" type="String">boat</private>
		<private name="permissions" type="Object"><![CDATA[{"user":""}]]></private>
		<public name="tables" type="List"></public>
	</action>
	<action xmlns="http://www.w3.org/1999/xhtml" name="userExists"
		impl="justep.Boat.hasExits"></action>
	<action xmlns="http://www.w3.org/1999/xhtml" name="queryHelper"
		impl="action:common/CRUD/query">
		<private name="condition" type="String"></private>
		<private name="db" type="String">boat</private>
		<private name="tableName" type="String">helper</private>
		<public name="columns" type="Object"></public>
		<public name="filter" type="String"></public>
		<public name="limit" type="Integer"></public>
		<public name="offset" type="Integer"></public>
		<public name="orderBy" type="String"></public>
		<public name="variables" type="Object"></public>
	</action>
	<action xmlns="http://www.w3.org/1999/xhtml" name="saveHelper"
		impl="action:common/CRUD/save">
		<private name="db" type="String">boat</private>
		<private name="permissions" type="Object"><![CDATA[{"helper":""}]]></private>
		<public name="tables" type="List"></public>
	</action>
	<action xmlns="http://www.w3.org/1999/xhtml" name="queryUserInfo"
		impl="justep.Boat.queryUserInfo"></action>
	<action xmlns="http://www.w3.org/1999/xhtml" name="updateUserInfo"
		impl="justep.Boat.updateUserInfo"></action>
	<action xmlns="http://www.w3.org/1999/xhtml" name="signSuccess"
		impl="justep.Boat.signSuccess"></action>
	<action xmlns="http://www.w3.org/1999/xhtml" name="updateUseTel"
		impl="justep.Boat.updateUserTel"></action>
</model>