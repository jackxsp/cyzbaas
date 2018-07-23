<?xml version="1.0" encoding="UTF-8"?>
<model xmlns="http://www.justep.com/model">
	<action xmlns="http://www.w3.org/1999/xhtml" name="queryBanners"
		impl="action:common/CRUD/query">
		<private name="condition" type="String"></private>
		<private name="db" type="String">taobao</private>
		<private name="tableName" type="String">banners</private>
		<public name="columns" type="Object"></public>
		<public name="filter" type="String"></public>
		<public name="limit" type="Integer"></public>
		<public name="offset" type="Integer"></public>
		<public name="orderBy" type="String"></public>
		<public name="variables" type="Object"></public>
	</action>
	<action xmlns="http://www.w3.org/1999/xhtml" name="queryGoods"
		impl="action:common/CRUD/query">
		<private name="condition" type="String"></private>
		<private name="db" type="String">taobao</private>
		<private name="tableName" type="String">goods</private>
		<public name="columns" type="Object"></public>
		<public name="filter" type="String"></public>
		<public name="limit" type="Integer"></public>
		<public name="offset" type="Integer"></public>
		<public name="orderBy" type="String"></public>
		<public name="variables" type="Object"></public>
	</action>
	<action xmlns="http://www.w3.org/1999/xhtml" name="queryGoodimgs"
		impl="action:common/CRUD/query">
		<private name="condition" type="String"></private>
		<private name="db" type="String">taobao</private>
		<private name="tableName" type="String">goodimgs</private>
		<public name="columns" type="Object"></public>
		<public name="filter" type="String"></public>
		<public name="limit" type="Integer"></public>
		<public name="offset" type="Integer"></public>
		<public name="orderBy" type="String"></public>
		<public name="variables" type="Object"></public>
	</action>
	<action xmlns="http://www.w3.org/1999/xhtml" name="queryColor"
		impl="action:common/CRUD/query">
		<private name="condition" type="String">fType='color'</private>
		<private name="db" type="String">taobao</private>
		<private name="tableName" type="String">attribute</private>
		<public name="columns" type="Object"></public>
		<public name="filter" type="String"></public>
		<public name="limit" type="Integer"></public>
		<public name="offset" type="Integer"></public>
		<public name="orderBy" type="String"></public>
		<public name="variables" type="Object"></public>
	</action>
	<action xmlns="http://www.w3.org/1999/xhtml" name="queryShop"
		impl="action:common/CRUD/query">
		<private name="condition" type="String"></private>
		<private name="db" type="String">taobao</private>
		<private name="tableName" type="String">shop</private>
		<public name="columns" type="Object"></public>
		<public name="filter" type="String"></public>
		<public name="limit" type="Integer"></public>
		<public name="offset" type="Integer"></public>
		<public name="orderBy" type="String"></public>
		<public name="variables" type="Object"></public>
	</action>
	<action xmlns="http://www.w3.org/1999/xhtml" name="queryComments"
		impl="action:common/CRUD/query">
		<private name="condition" type="String"></private>
		<private name="db" type="String">taobao</private>
		<private name="tableName" type="String">comments</private>
		<public name="columns" type="Object"></public>
		<public name="filter" type="String"></public>
		<public name="limit" type="Integer"></public>
		<public name="offset" type="Integer"></public>
		<public name="orderBy" type="String"></public>
		<public name="variables" type="Object"></public>
	</action>
	<action xmlns="http://www.w3.org/1999/xhtml" name="querySize"
		impl="action:common/CRUD/query">
		<private name="condition" type="String">fType='size'</private>
		<private name="db" type="String">taobao</private>
		<private name="tableName" type="String">attribute</private>
		<public name="columns" type="Object"></public>
		<public name="filter" type="String"></public>
		<public name="limit" type="Integer"></public>
		<public name="offset" type="Integer"></public>
		<public name="orderBy" type="String"></public>
		<public name="variables" type="Object"></public>
	</action>
	<action xmlns="http://www.w3.org/1999/xhtml" name="queryParameter"
		impl="action:common/CRUD/query">
		<private name="condition" type="String">fType='parameter'</private>
		<private name="db" type="String">taobao</private>
		<private name="tableName" type="String">attribute</private>
		<public name="columns" type="Object"></public>
		<public name="filter" type="String"></public>
		<public name="limit" type="Integer"></public>
		<public name="offset" type="Integer"></public>
		<public name="orderBy" type="String"></public>
		<public name="variables" type="Object"></public>
	</action>
	<action xmlns="http://www.w3.org/1999/xhtml" name="queryCart"
		impl="action:common/CRUD/query">
		<private name="condition" type="String"></private>
		<private name="db" type="String">taobao</private>
		<private name="tableName" type="String">cart</private>
		<public name="columns" type="Object"></public>
		<public name="filter" type="String"></public>
		<public name="limit" type="Integer"></public>
		<public name="offset" type="Integer"></public>
		<public name="orderBy" type="String"></public>
		<public name="variables" type="Object"></public>
	</action>
	<action xmlns="http://www.w3.org/1999/xhtml" name="queryRootClass"
		impl="action:common/CRUD/query">
		<private name="condition" type="String">fType='root'</private>
		<private name="db" type="String">taobao</private>
		<private name="tableName" type="String">class</private>
		<public name="columns" type="Object"></public>
		<public name="filter" type="String"></public>
		<public name="limit" type="Integer"></public>
		<public name="offset" type="Integer"></public>
		<public name="orderBy" type="String"></public>
		<public name="variables" type="Object"></public>
	</action>
	<action xmlns="http://www.w3.org/1999/xhtml" name="querySecondClass"
		impl="action:common/CRUD/query">
		<private name="condition" type="String">fType='second'</private>
		<private name="db" type="String">taobao</private>
		<private name="tableName" type="String">class</private>
		<public name="columns" type="Object"></public>
		<public name="filter" type="String"></public>
		<public name="limit" type="Integer"></public>
		<public name="offset" type="Integer"></public>
		<public name="orderBy" type="String"></public>
		<public name="variables" type="Object"></public>
	</action>
	<action xmlns="http://www.w3.org/1999/xhtml" name="queryThreeClass"
		impl="action:common/CRUD/query">
		<private name="condition" type="String">fType='three'</private>
		<private name="db" type="String">taobao</private>
		<private name="tableName" type="String">class</private>
		<public name="columns" type="Object"></public>
		<public name="filter" type="String"></public>
		<public name="limit" type="Integer"></public>
		<public name="offset" type="Integer"></public>
		<public name="orderBy" type="String"></public>
		<public name="variables" type="Object"></public>
	</action>
	<action xmlns="http://www.w3.org/1999/xhtml" name="newCart"
		impl="Taobao.newCart"></action>
	<action xmlns="http://www.w3.org/1999/xhtml" name="queryCartShop"
		impl="action:common/CRUD/sqlQuery">
		<private name="countSql" type="String">SELECT COUNT(fID) FROM shop
			WHERE fID IN (select fShopID from cart)
		</private>
		<private name="db" type="String">taobao</private>
		<private name="sql" type="String">SELECT * FROM shop WHERE fID IN
			(SELECT fShopID FROM cart)</private>
		<private name="tableName" type="String">shop</private>
		<public name="columns" type="Object"></public>
		<public name="filter" type="String"></public>
		<public name="limit" type="Integer"></public>
		<public name="offset" type="Integer"></public>
		<public name="orderBy" type="String"></public>
		<public name="variables" type="Object"></public>
	</action>
	<action xmlns="http://www.w3.org/1999/xhtml" name="saveCart"
		impl="action:common/CRUD/save">
		<private name="db" type="String">taobao</private>
		<private name="permissions" type="Object"><![CDATA[{"cart":""}]]></private>
		<public name="tables" type="List"></public>
	</action>
	<action xmlns="http://www.w3.org/1999/xhtml" name="newOrder"
		impl="Taobao.newOrder"></action>
</model>