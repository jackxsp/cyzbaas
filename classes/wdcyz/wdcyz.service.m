<?xml version="1.0" encoding="UTF-8"?><model xmlns="http://www.justep.com/model"><action xmlns="http://www.w3.org/1999/xhtml" name="queryProduct" impl="action:common/CRUD/query"><private name="condition" type="String"></private><private name="db" type="String">wdcyz</private><private name="tableName" type="String">tb_b_product</private><public name="columns" type="Object"></public><public name="filter" type="String"></public><public name="limit" type="Integer"></public><public name="offset" type="Integer"></public><public name="orderBy" type="String">product_type</public><public name="variables" type="Object"></public></action><action xmlns="http://www.w3.org/1999/xhtml" name="saveProduct" impl="action:common/CRUD/save"><private name="db" type="String">wdcyz</private><private name="permissions" type="Object"><![CDATA[{"tb_b_product":""}]]></private><public name="tables" type="List"></public></action><action xmlns="http://www.w3.org/1999/xhtml" name="queryUserInfo" impl="action:common/CRUD/query"><private name="condition" type="String"></private><private name="db" type="String">wdcyz</private><private name="tableName" type="String">tf_f_user</private><public name="columns" type="Object"></public><public name="filter" type="String"></public><public name="limit" type="Integer"></public><public name="offset" type="Integer"></public><public name="orderBy" type="String"></public><public name="variables" type="Object"></public></action><action xmlns="http://www.w3.org/1999/xhtml" name="saveUserInfo" impl="action:common/CRUD/save"><private name="db" type="String">wdcyz</private><private name="permissions" type="Object"><![CDATA[{"tf_f_user":""}]]></private><public name="tables" type="List"></public></action><action xmlns="http://www.w3.org/1999/xhtml" name="queryOrder" impl="action:common/CRUD/query"><private name="condition" type="String"></private><private name="db" type="String">wdcyz</private><private name="tableName" type="String">tf_f_order</private><public name="columns" type="Object"></public><public name="filter" type="String"></public><public name="limit" type="Integer"></public><public name="offset" type="Integer"></public><public name="orderBy" type="String"></public><public name="variables" type="Object"></public></action><action xmlns="http://www.w3.org/1999/xhtml" name="saveOrder" impl="action:common/CRUD/save"><private name="db" type="String">wdcyz</private><private name="permissions" type="Object"><![CDATA[{"tf_f_order":"","tf_f_user":""}]]></private><public name="tables" type="List"></public></action><action xmlns="http://www.w3.org/1999/xhtml" name="queryPaytype" impl="action:common/CRUD/query"><private name="condition" type="String"></private><private name="db" type="String">wdcyz</private><private name="tableName" type="String">tb_d_paytype</private><public name="columns" type="Object"></public><public name="filter" type="String"></public><public name="limit" type="Integer"></public><public name="offset" type="Integer"></public><public name="orderBy" type="String"></public><public name="variables" type="Object"></public></action><action xmlns="http://www.w3.org/1999/xhtml" name="queryVegetable" impl="action:common/CRUD/query"><private name="condition" type="String"></private><private name="db" type="String">wdcyz</private><private name="tableName" type="String">tb_b_vegetable</private><public name="columns" type="Object"></public><public name="filter" type="String"></public><public name="limit" type="Integer"></public><public name="offset" type="Integer"></public><public name="orderBy" type="String"></public><public name="variables" type="Object"></public></action><action xmlns="http://www.w3.org/1999/xhtml" name="saveVegetable" impl="action:common/CRUD/save"><private name="db" type="String">wdcyz</private><private name="permissions" type="Object"><![CDATA[{"tb_b_vegetable":""}]]></private><public name="tables" type="List"></public></action><action xmlns="http://www.w3.org/1999/xhtml" name="queryVegetype" impl="action:common/CRUD/query"><private name="condition" type="String"></private><private name="db" type="String">wdcyz</private><private name="tableName" type="String">tb_d_vegetype</private><public name="columns" type="Object"></public><public name="filter" type="String"></public><public name="limit" type="Integer"></public><public name="offset" type="Integer"></public><public name="orderBy" type="String"></public><public name="variables" type="Object"></public></action><action xmlns="http://www.w3.org/1999/xhtml" name="saveVegetype" impl="action:common/CRUD/save"><private name="db" type="String">wdcyz</private><private name="permissions" type="Object"><![CDATA[{"tb_d_vegetype":""}]]></private><public name="tables" type="List"></public></action><action xmlns="http://www.w3.org/1999/xhtml" name="getSelectVege" impl="wdcyz.Wdcyz.getSelectVege"><public name="columns" type="Object"></public><public name="limit" type="Integer">-1</public><public name="offset" type="Integer"></public></action><action xmlns="http://www.w3.org/1999/xhtml" name="querySelectvege" impl="action:common/CRUD/query"><private name="condition" type="String"></private><private name="db" type="String">wdcyz</private><private name="tableName" type="String">tb_b_selectvege</private><public name="columns" type="Object"></public><public name="filter" type="String"></public><public name="limit" type="Integer"></public><public name="offset" type="Integer"></public><public name="orderBy" type="String"></public><public name="variables" type="Object"></public></action><action xmlns="http://www.w3.org/1999/xhtml" name="saveSelectvege" impl="action:common/CRUD/save"><private name="db" type="String">wdcyz</private><private name="permissions" type="Object"><![CDATA[{"tb_b_selectvege":""}]]></private><public name="tables" type="List"></public></action><action xmlns="http://www.w3.org/1999/xhtml" name="queryUservege" impl="action:common/CRUD/query"><private name="condition" type="String"></private><private name="db" type="String">wdcyz</private><private name="tableName" type="String">tb_b_user_vege</private><public name="columns" type="Object"></public><public name="filter" type="String"></public><public name="limit" type="Integer"></public><public name="offset" type="Integer"></public><public name="orderBy" type="String"></public><public name="variables" type="Object"></public></action><action xmlns="http://www.w3.org/1999/xhtml" name="saveUservege" impl="action:common/CRUD/save"><private name="db" type="String">wdcyz</private><private name="permissions" type="Object"><![CDATA[{"tb_b_user_vege":""}]]></private><public name="tables" type="List"></public></action><action xmlns="http://www.w3.org/1999/xhtml" name="queryOrderConf" impl="action:common/CRUD/query"><private name="condition" type="String"></private><private name="db" type="String">wdcyz</private><private name="tableName" type="String">tb_d_order_conf</private><public name="columns" type="Object"></public><public name="filter" type="String"></public><public name="limit" type="Integer"></public><public name="offset" type="Integer"></public><public name="orderBy" type="String"></public><public name="variables" type="Object"></public></action><action xmlns="http://www.w3.org/1999/xhtml" name="saveOrderConf" impl="action:common/CRUD/save"><private name="db" type="String">wdcyz</private><private name="permissions" type="Object"><![CDATA[{"tb_d_order_conf":""}]]></private><public name="tables" type="List"></public></action><action xmlns="http://www.w3.org/1999/xhtml" name="getUserSelectVege" impl="wdcyz.Wdcyz.getUserSelectVege"><private name="columns" type="Object"></private><private name="limit" type="Integer">-1</private><private name="offset" type="Integer"></private></action><action xmlns="http://www.w3.org/1999/xhtml" name="getCountSelectVege" impl="wdcyz.Wdcyz.getCountSelectVege"><private name="columns" type="Object"></private><private name="limit" type="Integer"></private><private name="offset" type="Integer"></private></action><action xmlns="http://www.w3.org/1999/xhtml" name="getSelectVegeInfo" impl="wdcyz.Wdcyz.getSelectVegeInfo"></action><action xmlns="http://www.w3.org/1999/xhtml" name="fileSeleVege" impl="wdcyz.Wdcyz.fileSeleVege"></action><action xmlns="http://www.w3.org/1999/xhtml" name="getVegeVolNum" impl="wdcyz.Wdcyz.getVegeVolNum"></action><action xmlns="http://www.w3.org/1999/xhtml" name="getSelectVegeVolNum" impl="wdcyz.Wdcyz.getSelectVegeVolNum"></action><action xmlns="http://www.w3.org/1999/xhtml" name="regUser" impl="wdcyz.Wdcyz.regUser"><private name="db" type="String">wdcyz</private><private name="permissions" type="Object"></private><public name="tables" type="List"></public></action><action xmlns="http://www.w3.org/1999/xhtml" name="login" impl="wdcyz.Wdcyz.login"></action></model>