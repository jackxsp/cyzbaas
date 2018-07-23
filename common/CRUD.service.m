<?xml version="1.0" encoding="UTF-8"?>
<model xmlns="http://www.justep.com/model">
	<action name="query" impl="com.justep.baas.action.CRUD.query"
		private="true">
		<private name="db" type="String" />
		<private name="tableName" type="String" />
		<private name="condition" type="String" />
		<public name="filter" type="String" />
		<public name="limit" type="Integer" />
		<public name="offset" type="Integer" />
		<public name="orderBy" type="String" />
		<public name="columns" type="Object" />
		<public name="variables" type="Object" />
	</action>
	<action name="sqlQuery" impl="com.justep.baas.action.CRUD.sqlQuery"
		private="true">
		<private name="db" type="String" />
		<private name="sql" type="String" />
		<private name="countSql" type="String" />
		<private name="tableName" type="String" />
		<public name="filter" type="String" />
		<public name="limit" type="Integer" />
		<public name="offset" type="Integer" />
		<public name="orderBy" type="String" />
		<public name="columns" type="Object" />
		<public name="variables" type="Object" />
	</action>
	<action name="save" impl="com.justep.baas.action.CRUD.save"
		private="true">
		<private name="db" type="String" />
		<private name="permissions" type="Object" />
		<public name="tables" type="List" />
	</action>
</model>