<?xml version="1.0" encoding="UTF-8"?>
<model xmlns="http://www.justep.com/model">
	<action name="queryNetease_user" impl="action:common/CRUD/query">
		<private name="condition" type="String">fID=:usera</private>
		<private name="db" type="String">demo</private>
		<private name="tableName" type="String">netease_user</private>
		<public name="columns" type="Object" />
		<public name="filter" type="String" />
		<public name="limit" type="Integer" />
		<public name="offset" type="Integer" />
		<public name="orderBy" type="String" />
		<public name="variables" type="Object" />
		<public name="var-usera" type="String">user</public>
	</action>
	<action name="saveNetease_user" impl="action:common/CRUD/save">
		<private name="db" type="String">demo</private>
		<private name="permissions" type="Object"><![CDATA[{"netease_user":""}]]></private>
		<public name="tables" type="List" />
	</action>
	<action name="queryRegist_user" impl="action:common/CRUD/query">
		<private name="condition" type="String">fPhoneNumber=:userPhone and
			passwd=:password
		</private>
		<private name="db" type="String">demo</private>
		<private name="tableName" type="String">netease_user</private>
		<public name="columns" type="Object" />
		<public name="filter" type="String" />
		<public name="limit" type="Integer" />
		<public name="offset" type="Integer" />
		<public name="orderBy" type="String" />
		<public name="var-userPhone" type="String">18501978580</public>
		<public name="var-password" type="String">123456</public>
	</action>
</model>
