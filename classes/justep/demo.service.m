<?xml version="1.0" encoding="UTF-8"?>
<model>
	<action name="queryUser" impl="action:common/CRUD/sqlQuery">
		<private name="db" type="String">takeout</private>
		<private name="sql" type="String">SELECT u.fID, u.fName,
			u.fPhoneNumber, u.fAddress, COUNT(ord.fID) AS orderCount FROM
			takeout_user u LEFT JOIN takeout_order ord ON u.fID = ord.fUserID
			WHERE (0=:useSearch) or (u.fID LIKE :search OR u.fName LIKE :search
			OR u.fPhoneNumber LIKE :search OR u.fAddress LIKE :search) GROUP BY
			u.fID, u.fName, u.fPhoneNumber, u.fAddress</private>
		<private name="countSql" type="String">SELECT COUNT(fID) FROM
			takeout_user WHERE (0=:useSearch) or (fID LIKE :search OR fName LIKE
			:search OR fPhoneNumber LIKE :search OR fAddress LIKE :search)
		</private>
		<private name="tableName" type="String">takeout_user</private>
		<public name="filter" type="String" />
		<public name="orderBy" type="String" />
		<public name="columns" type="Object" />
		<public name="limit" type="Integer" />
		<public name="offset" type="Integer" />
		<public name="variables" type="Object" />
		<public name="var-useSearch" type="Integer">0</public>
	</action>
	<action name="saveUser" impl="action:common/CRUD/save">
		<private name="db" type="String">takeout</private>
		<private name="permissions" type="Object"><![CDATA[{"takeout_order":"","takeout_user":""}]]></private>
		<public name="tables" type="List" />
	</action>
	<action name="getOrderCount" impl="justep.Demo.getOrderCount" />
	<action name="queryOrder" impl="action:common/CRUD/query">
		<private name="condition" type="String">(0=:useSearch) or (fUserName
			LIKE :search OR fPhoneNumber LIKE :search OR fAddress LIKE :search OR
			fContent LIKE :search)</private>
		<private name="db" type="String">takeout</private>
		<private name="tableName" type="String">takeout_order</private>
		<public name="columns" type="Object" />
		<public name="filter" type="String" />
		<public name="limit" type="Integer" />
		<public name="offset" type="Integer" />
		<public name="orderBy" type="String" />
		<public name="variables" type="Object" />
		<public name="var-useSearch" type="Integer">0</public>
	</action>
	<action name="saveOrder" impl="action:common/CRUD/save">
		<private name="db" type="String">takeout</private>
		<private name="permissions" type="Object"><![CDATA[{"takeout_order":""}]]></private>
		<public name="tables" type="List" />
	</action>
	<action name="queryRegionTree" impl="action:common/CRUD/query">
		<private name="condition" type="String" />
		<private name="db" type="String">takeout</private>
		<private name="tableName" type="String">takeout_region</private>
		<public name="columns" type="Object" />
		<public name="filter" type="String" />
		<public name="limit" type="Integer" />
		<public name="offset" type="Integer" />
		<public name="orderBy" type="String" />
		<public name="variables" type="Object" />
	</action>
</model>