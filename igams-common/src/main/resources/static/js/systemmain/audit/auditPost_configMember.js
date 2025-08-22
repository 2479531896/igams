
var auditPost_unselect_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#configMemberForm #tb_unlist').bootstrapTable({
            url: $("#auditPost_formSearch #urlPrefix").val()+'/systemmain/configMember/pagedataListUnSelectMember',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: false,                    //是否启用排序
            sortName:"yhid",						//排序字段
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
            height: 500,                       //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "yhid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            }, {
                field: 'gwid',
                title: '岗位ID',
                width: '10%',
                align: 'left',
                visible: false
            }, {
                field: 'yhid',
                title: '用户ID',
                width: '10%',
                align: 'left',
                visible: false
            }, {
                field: 'jsid',
                title: '角色ID',
                width: '10%',
                align: 'left',
                visible: false
            }, {
                field: 'yhm',
                title: '用户名',
                width: '30%',
                align: 'left',
                visible: true
            }, {
                field: 'zsxm',
                title: '姓名',
                width: '30%',
                align: 'left',
                visible: true
            }, {
                field: 'jsmc',
                title: '角色名称',
                width: '30%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	removeToSelected(row.yhid,row.jsid);
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
            sortLastName: "yh.yhid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };
        //用户名
    	var yhm = $.trim($("#configMemberForm #yhm").val());
    	//真实姓名
    	var zsxm = $.trim(jQuery('#configMemberForm #zsxm').val());
    	//角色名称
    	var jsmc = $.trim(jQuery('#configMemberForm #jsmc').val());
    	
    	map["gwid"] = $("#configMemberForm #gwid").val();
    	map["yhm"] = yhm;
    	map["zsxm"] = zsxm;
    	map["jsmc"] = jsmc;
    	return map;
    };
    return oTableInit;
};

var auditPost_selected_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#configMemberForm #tb_list').bootstrapTable({
            url: $("#auditPost_formSearch #urlPrefix").val()+'/systemmain/configMember/pagedataSelectedMember',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: false,                     //是否启用排序
            sortName:"yhid",					//排序字段
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
            uniqueId: "yhid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            }, {
                field: 'gwid',
                title: '岗位ID',
                width: '10%',
                align: 'left',
                visible: false
            }, {
                field: 'yhid',
                title: '用户ID',
                width: '10%',
                align: 'left',
                visible: false
            }, {
                field: 'jsid',
                title: '角色ID',
                width: '10%',
                align: 'left',
                visible: false
            }, {
                field: 'yhm',
                title: '用户名',
                width: '30%',
                align: 'left',
                visible: true
            }, {
                field: 'zsxm',
                title: '姓名',
                width: '30%',
                align: 'left',
                visible: true
            }, {
                field: 'jsmc',
                title: '角色名称',
                width: '30%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	removeToOptional(row.yhid,row.jsid);
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
            sortLastName: "yh.yhid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

        //用户名
    	var yhm = $.trim($("#configMemberForm #yhm").val());
    	// 真实姓名
    	var zsxm = $.trim(jQuery('#configMemberForm #zsxm').val());
    	//角色名称
    	var jsmc = $.trim(jQuery('#configMemberForm #jsmc').val());
    	map["gwid"] = $("#configMemberForm #gwid").val();
    	map["yhm"] = yhm;
    	map["zsxm"] = zsxm;
    	map["jsmc"] = jsmc;
    	return map;
    };
    return oTableInit;
};

function addOption(){
	var sel_row = $('#configMemberForm #tb_unlist').bootstrapTable('getSelections');//获取选择行数据
	if(sel_row.length<1){
		$.error("请选中一行左侧数据");
	}else{
		var yhids="";
		var jsids="";
		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
			yhids = yhids + ","+ sel_row[i].yhid;
			jsids = jsids + ","+ sel_row[i].jsid;
		}
		yhids = yhids.substr(1);
		jsids = jsids.substr(1);
		var url = $("#auditPost_formSearch #urlPrefix").val()+'/systemmain/configMember/pagedataToSelected';
		var map = {};
		map["yhids"] = yhids;
		map["jsids"] = jsids;
		accessConfigMember(url,map);
	}
}

function removeToSelected(yhid, jsid){
	var url = $("#auditPost_formSearch #urlPrefix").val()+'/systemmain/configMember/pagedataToSelected';
	var map = {};
	map["yhid"] = yhid;
	map["jsid"] = jsid;
	accessConfigMember(url,map);
}

function moveOption(){
	var sel_row = $('#configMemberForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
	if(sel_row.length<1){
		$.error("请选中一行右侧数据");
	}else{
		var yhids="";
		var jsids="";
		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
			yhids = yhids + ","+ sel_row[i].yhid;
			jsids = jsids + ","+ sel_row[i].jsid;
		}
		yhids = yhids.substr(1);
		jsids = jsids.substr(1);
		var url = $("#auditPost_formSearch #urlPrefix").val()+'/systemmain/configMember/pagedataToOptional';
		var map = {};
		map["yhids"] = yhids;
		map["jsids"] = jsids;
		accessConfigMember(url,map);
	}
}

function removeToOptional(yhid, jsid) {
	var url = $("#auditPost_formSearch #urlPrefix").val()+'/systemmain/configMember/pagedataToOptional';
	var map = {};
	map["yhid"] = yhid;
	map["jsid"] = jsid;
	accessConfigMember(url,map);
}

function accessConfigMember(url,map){
	map["access_token"] = $("#ac_tk").val();
	map["gwid"] = $("#configMemberForm #gwid").val();
	jQuery.ajaxSetup( {
		async : false
	});
	jQuery.post(url, map, function(data) {
		searchConfigMemberResult();
	}, 'json');
	jQuery.ajaxSetup( {
		async : true
	});
}

//查询
function searchConfigMemberResult() {
	$('#configMemberForm #tb_unlist').bootstrapTable('refresh');
	$('#configMemberForm #tb_list').bootstrapTable('refresh');
}

$(function(){

    //1.初始化Table1
    var oTable = new auditPost_unselect_TableInit();
    oTable.Init();

    //2.初始化Table2
    var oTable2 = new auditPost_selected_TableInit();
    oTable2.Init();
    
});