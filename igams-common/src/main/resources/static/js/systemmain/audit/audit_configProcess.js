
var auditProcess_unselect_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#configProcessForm #tb_unlist').bootstrapTable({
            url: $("#audit_formSearch #urlPrefix").val()+'/systemmain/configProcess/pageGetListUnSelectProcess',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: false,                    //是否启用排序
            sortName:"gw.gwid",						//排序字段
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
            height: 400,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "gwid",                    //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            }, {
                field: 'gwid',
                title: '岗位ID',
                width: '20%',
                align: 'left',
                visible: false
            }, {
                field: 'gwmc',
                title: '岗位名称',
                width: '90%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	removeToSelected(row);
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
            sortLastName: "gw.gwid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };
        //岗位名称
    	var gwmc = $.trim($("#configProcessForm #gwmc").val());
    	
    	map["shid"] = $("#configProcessForm #shid").val();
    	map["gwmc"] = gwmc;
    	
    	return map;
    };
    return oTableInit;
};

var auditProcess_selected_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#configProcessForm #tb_list').bootstrapTable({
            url: $("#audit_formSearch #urlPrefix").val()+'/systemmain/configProcess/pageGetListSelectedProcess',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: false,                     //是否启用排序
            sortName:"lc.lcxh",					//排序字段
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
            height: 400,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "gwid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            }, {
                field: 'gwid',
                title: '岗位ID',
                width: '20%',
                align: 'left',
                visible: false
            }, {
                field: 'gwmc',
                title: '岗位名称',
                width: '50%',
                align: 'left',
                visible: true
            }, {
                field: 'lclbmc',
                title: '流程类别',
                width: '50%',
                align: 'left',
                formatter:lclbFormatter,
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	removeToOptional(row);
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
            sortLastName: "gw.gwid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

      //岗位名称
    	var gwmc = $.trim($("#configProcessForm #gwmc").val());
    	
    	map["shid"] = $("#configProcessForm #shid").val();
    	//map["gwmc"] = gwmc;
    	return map;
    };
    return oTableInit;
};


function lclbFormatter(value,row,index){
	var lclb=row.lclb;
	var lclbmc=row.lclbmc;
	if(lclb==null || lclb==""){
		lclb="";
	}
	if(lclbmc==null || lclbmc==""){
		lclbmc=""
	}
	var result="<span title='"+lclbmc+"' id='"+row.gwid+"' lcid='"+lclb+"'>"+lclbmc+"</span>";
	return result;
}
function removeToSelected(row){
	var ids = [];
	ids.push(row.gwid);
	$('#configProcessForm #tb_unlist').bootstrapTable('remove', {
        field: 'gwid',
        values: ids
    });
	$('#configProcessForm #tb_list').bootstrapTable('insertRow', {
        index: row,
        row: {
            gwid: row.gwid,
            gwmc: row.gwmc
        }
    });
}
function addOption(){
	var sel_row = $('#configProcessForm #tb_unlist').bootstrapTable('getSelections');//获取选择行数据
	var ids = $.map($('#configProcessForm #tb_unlist').bootstrapTable('getSelections'), function (row) {
        return row.gwid;
    });
	if(sel_row.length<1){
		$.error("请选中一行左侧数据");
	}else{
		for (var i = 0; i < sel_row.length; i++) {
			$('#configProcessForm #tb_list').bootstrapTable('insertRow', {
		        index: sel_row[i],
		        row: {
		            gwid: sel_row[i].gwid,
		            gwmc: sel_row[i].gwmc
		        }
		    });
		}
		$('#configProcessForm #tb_unlist').bootstrapTable('remove', {
            field: 'gwid',
            values: ids
        });
	}
}

function removeToOptional(row) {
	var ids = [];
	ids.push(row.gwid);
	$('#configProcessForm #tb_unlist').bootstrapTable('insertRow', {
        index: row,
        row: {
            gwid: row.gwid,
            gwmc: row.gwmc
        }
    });
	$('#configProcessForm #tb_list').bootstrapTable('remove', {
        field: 'gwid',
        values: ids
    });
}
function moveOption(){
	var sel_row = $('#configProcessForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
	var ids = $.map($('#configProcessForm #tb_list').bootstrapTable('getSelections'), function (row) {
        return row.gwid;
    });
	if(sel_row.length<1){
		$.error("请选中一行右侧数据");
	}else{
		for (var i = 0; i < sel_row.length; i++) {
			$('#configProcessForm #tb_unlist').bootstrapTable('insertRow', {
		        index: sel_row[i],
		        row: {
		            gwid: sel_row[i].gwid,
		            gwmc: sel_row[i].gwmc
		        }
		    });
		}
		$('#configProcessForm #tb_list').bootstrapTable('remove', {
            field: 'gwid',
            values: ids
        });
	}
}

function upOption(){
	var sel_row = $('#configProcessForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
	if(sel_row.length==1){
		var index = $("#configProcessForm #tb_list").find("[data-uniqueid="+sel_row[0].gwid+"]").attr("data-index");
		if(index > 0){
			$("#configProcessForm #tb_list").find("[data-uniqueid="+sel_row[0].gwid+"]").attr("data-index",index-1);
			$("#configProcessForm #tb_list").find("[data-uniqueid="+sel_row[0].gwid+"]").prev().attr("data-index",index);
		}
		$("#configProcessForm #tb_list").find("[data-uniqueid="+sel_row[0].gwid+"]").fadeOut().fadeIn();
		$("#configProcessForm #tb_list").find("[data-uniqueid="+sel_row[0].gwid+"]").prev().before($("#configProcessForm #tb_list").find("[data-uniqueid="+sel_row[0].gwid+"]"));
	}else{
		$.error("请选中一行");
	}
}

function downOption(){
	var sel_row = $('#configProcessForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
	if(sel_row.length==1){
		var index = $("#configProcessForm #tb_list").find("[data-uniqueid="+sel_row[0].gwid+"]").attr("data-index");
		if(index < $('#configProcessForm #tb_list').bootstrapTable('getData').length){
			$("#configProcessForm #tb_list").find("[data-uniqueid="+sel_row[0].gwid+"]").attr("data-index", index+1);
			$("#configProcessForm #tb_list").find("[data-uniqueid="+sel_row[0].gwid+"]").attr("data-index", index);
		}
		$("#configProcessForm #tb_list").find("[data-uniqueid="+sel_row[0].gwid+"]").fadeOut().fadeIn();
		$("#configProcessForm #tb_list").find("[data-uniqueid="+sel_row[0].gwid+"]").next().after($("#configProcessForm #tb_list").find("[data-uniqueid="+sel_row[0].gwid+"]"));
	}else{
		$.error("请选中一行");
	}
}


//查询
function searchConfigProcessResult() {
	$('#configProcessForm #tb_unlist').bootstrapTable('refresh');
	//$('#configProcessForm #tb_list').bootstrapTable('refresh');
}



var gwids="";
var lclbs="";
var lclbmcs="";
//流程设置模态框
var ProcessInstallConfig = {
		width		: "600px",
		modalName	: "ProcessInstallModal",
		formName	: "ProcessInstallForm",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#ProcessInstallForm").valid()){
						return false;
					}
					gwids="";
					lclbs="";
					lclbmcs="";
					var lclb=$("#lclb").val();
					for(var j=0;j<$("#lclb option").length;j++){
						if($("#lclb option")[j].value==lclb){
							var lclbmc=$("#lclb option")[j].text;
							if(lclbmc=="--请选择--"){
								lclbmc="";
							}
						}
					}
					var sel_row = $('#configProcessForm #tb_list').bootstrapTable('getSelections');
					for(var i=0;i<sel_row.length;i++){
						var gwid=sel_row[i].gwid;
						$("#"+gwid).text(lclbmc);
						$("#"+gwid).attr("lcid",lclb);
					}
					var allTableData = $('#configProcessForm #tb_list').bootstrapTable('getData');
					for(var k=0;k<allTableData.length;k++){
						var qgwid=allTableData[k].gwid;
						var lcid=$("#"+qgwid).attr("lcid");
						gwids=gwids+","+qgwid;
						lclbs=lclbs+","+lcid;
					}
					gwids=gwids.substr(1);
					lclbs=lclbs.substr(1);				
				}
			},
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};


function installConfigProcessResult(){
	var sel_row = $('#configProcessForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
	if(sel_row.length>0){
		var gwid=sel_row[0].gwid;
		var lcid=$("#"+gwid).attr("lcid");
		var url=$("#audit_formSearch #urlPrefix").val()+"/systemmain/audit/ProcessInstall?lclb="+lcid;
		$.showDialog(url,'流程设置',ProcessInstallConfig);
	}else{
		$.error("请至少选中一行右侧数据");
	}
}
$(function(){
    //1.初始化Table1
    var oTable = new auditProcess_unselect_TableInit();
    oTable.Init();

    //2.初始化Table2
    var oTable2 = new auditProcess_selected_TableInit();
    oTable2.Init();

});