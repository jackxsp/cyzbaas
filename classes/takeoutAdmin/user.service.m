<?xml version="1.0" encoding="UTF-8"?>
<model xmlns="http://www.justep.com/model">
	<action name="queryTakeout_user" impl="action:common/CRUD/query">
		<private name="condition" type="String" />
		<private name="db" type="String">demo</private>
		<private name="tableName" type="String">takeout_user</private>
		<public name="columns" type="Object" />
		<public name="filter" type="String" />
		<public name="limit" type="Integer" />
		<public name="offset" type="Integer" />
		<public name="orderBy" type="String" />
		<public name="variables" type="Object" />
	</action>
	<action name="saveTakeout_user" impl="action:common/CRUD/save">
		<private name="db" type="String">demo</private>
		<private name="permissions" type="Object"><![CDATA[{"takeout_user":""}]]></private>
		<public name="tables" type="List" />
	</action>
</model>