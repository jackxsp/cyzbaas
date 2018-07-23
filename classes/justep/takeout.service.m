<?xml version="1.0" encoding="utf-8"?>
<model>
	<action name="queryFood" impl="action:common/CRUD/query">
		<private name="db" type="String">takeout</private>
		<private name="tableName" type="String">takeout_food</private>
		<private name="condition" type="String" />
		<public name="filter" type="String" />
		<public name="limit" type="Integer" />
		<public name="offset" type="Integer" />
		<public name="columns" type="String" />
		<public name="orderBy" type="String">fID ASC</public>
		<public name="variables" type="Object" />
	</action>
	<action name="queryUser" impl="action:common/CRUD/query">
		<private name="db" type="String">takeout</private>
		<private name="tableName" type="String">takeout_user</private>
		<private name="condition" type="String" />
		<public name="filter" type="String" />
		<public name="limit" type="Integer" />
		<public name="offset" type="Integer" />
		<public name="columns" type="String" />
		<public name="orderBy" type="String">fID ASC</public>
		<public name="variables" type="Object" />
	</action>
	<action name="queryOrder" impl="action:common/CRUD/query">
		<private name="db" type="String">takeout</private>
		<private name="tableName" type="String">takeout_order</private>
		<private name="condition" type="String" />
		<public name="filter" type="String" />
		<public name="limit" type="Integer" />
		<public name="offset" type="Integer" />
		<public name="columns" type="String" />
		<public name="orderBy" type="String">fCreateTime DESC</public>
		<public name="variables" type="Object" />
	</action>
	<action name="save" impl="action:common/CRUD/save">
		<private name="db" type="String">takeout</private>
		<private name="permissions" type="Object"><![CDATA[{"takeout_user":"","takeout_order":""}]]></private>
		<public name="tables" type="List" />
	</action>
	<action name="queryAddr" impl="justep.Takeout.queryAddr">
		<public name="x" type="String">39.8622934399999</public>
		<public name="y" type="String">116.45764191999997</public>
	</action>
</model>