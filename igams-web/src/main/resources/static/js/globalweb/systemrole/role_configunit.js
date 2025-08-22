
var role_unselect_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
    	var jsid=$("#jsid").val();
        $('#configUnitForm #tb_unlist').bootstrapTable({
            url: '/systemrole/configunit/pagedataUnSelectJgxx?jsid='+jsid,         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: false,                    //是否启用排序
            sortName:"jgmc",						//排序字段
            sortOrder: "asc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 15,                       //每页的记录行数（*）
            pageList: [15, 30, 50, 100],        //可供选择的每页的行数（*）
            paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: false,
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "jgid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            }, {
                field: 'jgid',
                title: '机构ID',
                width: '10%',
                align: 'left',
                visible: false
            }, {
                field: 'jgmc',
                title: '机构名称',
                width: '40%',
                align: 'left',
                visible: true
            }, {
                field: 'jgdm',
                title: '机构代码',
                width: '50%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {

            },
            onDblClickRow: function (row, $element) {
            	removeToSelected(row.jgid);
            },
        });
    };

    //得到查询的参数
    oTableInit.queryParams = function(params){
    	//请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
    	//例如 toolbar 中的参数 
    	//如果 queryParamsType = ‘limit’ ,返回参数必须包含 
    	//limit, offset, search, sort, order 
    	//否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder. 
    	//返回false将会终止请求
        var map = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   //页面大小
        	pageNumber: (params.offset / params.limit) + 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名  
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "jgid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };
        //机构名称
    	var jgmc = $.trim($("#configUnitForm #jgmc").val());
    	map["jgid"] = $("#configUnitForm #jgid").val();
    	map["jgmc"] = jgmc;
    	
    	return map;
    };
    return oTableInit;
};

var role_selected_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
    	var jsid=$("#jsid").val();
        $('#configUnitForm #tb_list').bootstrapTable({
            url: '/systemrole/configunit/pagedataSelectJgxx?jsid='+jsid,         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: false,                     //是否启用排序
            sortName:"jgmc",					//排序字段
            sortOrder: "asc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 15,                       //每页的记录行数（*）
            pageList: [15, 30, 50, 100],        //可供选择的每页的行数（*）
            paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "jgid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            }, {
                field: 'jgid',
                title: '机构ID',
                width: '10%',
                align: 'left',
                visible: false
            }, {
                field: 'jgmc',
                title: '机构名称',
                width: '40%',
                align: 'left',
                visible: true
            }, {
                field: 'jgdm',
                title: '机构代码',
                width: '50%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	removeToOptional(row.jgid);
            },
        });
    };

    //得到查询的参数
    oTableInit.queryParams = function(params){
    	//请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
    	//例如 toolbar 中的参数 
    	//如果 queryParamsType = ‘limit’ ,返回参数必须包含 
    	//limit, offset, search, sort, order 
    	//否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder. 
    	//返回false将会终止请求
        var map = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   //页面大小
        	pageNumber: (params.offset / params.limit) + 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名  
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "jgid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

        var jgmc = $.trim($("#configUnitForm #jgmc").val());
    	map["jgid"] = $("#configUnitForm #jgid").val();
    	map["jgmc"] = jgmc;
    	return map;
    };
    return oTableInit;
};

function addOption(){
	var sel_row = $('#configUnitForm #tb_unlist').bootstrapTable('getSelections');//获取选择行数据
	if(sel_row.length<1){
		$.error("请选中一行左侧数据");
	}else{
		var ids="";
		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
			ids = ids + ","+ sel_row[i].jgid;
		}
		ids = ids.substr(1);
		
		var url = '/systemrole/configunit/pagedataToSelectedJg';
		var map = {};
		map["ids"] = ids;
		accessConfigUnit(url,map);
	}
}

function removeToSelected(jgid){
	var url = '/systemrole/configunit/pagedataToSelectedJg';
	var map = {};
	map["jgid"] = jgid;
	accessConfigUnit(url,map);
}

function moveOption(){
	var sel_row = $('#configUnitForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
	if(sel_row.length<1){
		$.error("请选中一行右侧数据");
	}else{
		var ids="";
		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
			ids = ids + ","+ sel_row[i].jgid;
		}
		ids = ids.substr(1);
		
		var url = '/systemrole/common/toOptionalJg';
		var map = {};
		map["ids"] = ids;
		accessConfigUnit(url,map);
	}
}

function removeToOptional(jgid) {
	var url = '/systemrole/common/toOptionalJg';
	var map = {};
	map["jgid"] = jgid;
	accessConfigUnit(url,map);
}

function accessConfigUnit(url,map){
	map["access_token"] = $("#ac_tk").val();
	map["jsid"] = $("#configUnitForm #jsid").val();
	jQuery.ajaxSetup( {
		async : false
	});
	jQuery.post(url, map, function(data) {
		searchConfigUserResult();
	}, 'json');
	jQuery.ajaxSetup( {
		async : true
	});
}

//查询
function searchConfigUserResult() {
	$('#configUnitForm #tb_unlist').bootstrapTable('refresh');
	$('#configUnitForm #tb_list').bootstrapTable('refresh');
}

$(function(){

    //1.初始化Table1
    var oTable = new role_unselect_TableInit();
    oTable.Init();

    //2.初始化Table2
    var oTable2 = new role_selected_TableInit();
    oTable2.Init();
    
});