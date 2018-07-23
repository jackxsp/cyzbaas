<?xml version="1.0" encoding="UTF-8"?>
<model xmlns="http://www.justep.com/model">
	<action name="queryAccount_class" impl="action:common/CRUD/query">
		<private name="condition" type="String" />
		<private name="db" type="String">account</private>
		<private name="tableName" type="String">account_class</private>
		<public name="columns" type="Object" />
		<public name="filter" type="String" />
		<public name="limit" type="Integer" />
		<public name="offset" type="Integer" />
		<public name="orderBy" type="String">fType ASC, fClass ASC</public>
		<public name="variables" type="Object" />
	</action>
	<action name="saveAccount_class" impl="action:common/CRUD/save">
		<private name="db" type="String">account</private>
		<private name="permissions" type="Object"><![CDATA[{"account_class":""}]]></private>
		<public name="tables" type="List" />
	</action>
	<action name="queryAccount" impl="action:common/CRUD/query">
		<private name="condition" type="String" />
		<private name="db" type="String">account</private>
		<private name="tableName" type="String">account</private>
		<public name="columns" type="Object" />
		<public name="filter" type="String" />
		<public name="limit" type="Integer" />
		<public name="offset" type="Integer" />
		<public name="orderBy" type="String">fDate DESC</public>
		<public name="variables" type="Object" />
	</action>
	<action name="saveAccount" impl="action:common/CRUD/save">
		<private name="db" type="String">account</private>
		<private name="permissions" type="Object"><![CDATA[{"account":""}]]></private>
		<public name="tables" type="List" />
	</action>
</model>