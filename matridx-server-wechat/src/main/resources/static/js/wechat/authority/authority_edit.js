//事件绑定
function btnBind(){
}

function initPage(){
}

/* ------------------------------------      初始化标签table      ------------------------------------ */
var modauthority_turnOff=true;

var modauthority_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
    	$('#modAuthorityForm #tb_list').bootstrapTable('destroy'); 
        $('#modAuthorityForm #tb_list').bootstrapTable({
            url: '/inspection/authority/getPagedUnitList',     //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#modAuthorityForm #toolbar', //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName:"sjxx.sjdw",					//排序字段
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
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "sjdw",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            }, {
                field: 'no',
                title: '序号',
                titleTooltip:'序号',
                width: '10%',
                align: 'left',
                visible: true,
                formatter:xhFormat
            }, {
                field: 'sjdw',
                title: '医院名称',
                titleTooltip:'医院名称',
                width: '12%',
                align: 'left',
                visible: true
            }, {
                field: 'ksmc',
                title: '科室',
                titleTooltip:'科室名称',
                width: '12%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	var value = row.sjdw+"-"+row.ksmc;
            	$("#modAuthorityForm  #yxdwks").tagsinput({
        			itemValue: "value",
        			itemText: "text",
        		})
        		$("#modAuthorityForm  #yxdwks").tagsinput('add',{"value":value,"text":value});
            	return;
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
            sortLastName: "ksmc", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

    	var cxbt = $("#modAuthorityForm #cxbt").val();
    	// 查询条件
    	var cxnr = $.trim(jQuery('#modAuthorityForm #cxnr').val());
    	// '0':'送检单位','1':'科室',
    	if (cxbt == "0") {
    		map["sjdw"] = cxnr;
    	}else if (cxbt == "1") {
    		map["ksmc"] = cxnr;
    	}
    	
    	return map;
    };
    return oTableInit;
};

//序号格式化
function xhFormat(value, row, index) {
    //获取每页显示的数量
    var pageSize=$('#modAuthorityForm #tb_list').bootstrapTable('getOptions').pageSize;
    //获取当前是第几页
    var pageNumber=$('#modAuthorityForm #tb_list').bootstrapTable('getOptions').pageNumber;
    //返回序号，注意index是从0开始的，所以要加上1
    return pageSize * (pageNumber - 1) + index + 1;
}

function searchModAuthorityResult(){
	$('#modAuthorityForm #tb_list').bootstrapTable('refresh',{pageNumber:1});
}

/**
 * 选择单位科室 
 */
function comfirm(){
	var sel_row = $('#modAuthorityForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
	if(sel_row.length > 0){
		$("#modAuthorityForm  #yxdwks").tagsinput({
			itemValue: "value",
			itemText: "text",
		})
		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
			var value = sel_row[i].sjdw+"-"+sel_row[i].ksmc;
			$("#modAuthorityForm  #yxdwks").tagsinput('add',{"value":value,"text":value});
		}
	}else{
		$.error("请至少选中一行");
		return;
	}
}

/**
 * 初始化已选信息
 * @returns
 */
function initYxdwks(){
	var wxid = $("#modAuthorityForm #wxid").val();
	if(!wxid || wxid.length < 0){
		return;
	}
	$.ajax({ 
	    type:'post',  
	    url:"/inspection/authority/getDtoList", 
	    cache: false,
	    data: {"wxid":wxid,"access_token":$("#ac_tk").val()},  
	    dataType:'json',
	    success:function(result){
	    	//返回值
	    	var sjqxDtos = result.sjqxDtos;
	    	for(var i = 0; i < sjqxDtos.length; i++){
	    		var value = sjqxDtos[i].sjdw + "-" + sjqxDtos[i].ks;
	        	$("#modAuthorityForm  #yxdwks").tagsinput({
	    			itemValue: "value",
	    			itemText: "text",
	    		})
	    		$("#modAuthorityForm  #yxdwks").tagsinput('add',{"value": value,"text": value});
	    	}
	    }
	});
}

$(document).ready(function(){
	initYxdwks();
    //1.初始化Table
    var oTable = new modauthority_TableInit();
    oTable.Init();
    //所有下拉框添加choose样式
	jQuery('#modAuthorityForm .chosen-select').chosen({width: '100%'});
	//绑定事件 
	btnBind();
	//初始化页面数据
	initPage();
});