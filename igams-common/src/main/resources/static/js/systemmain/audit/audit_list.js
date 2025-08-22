var mater_turnOff=true;

var audit_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#audit_formSearch #tb_list').bootstrapTable({
            url: $("#audit_formSearch #urlPrefix").val()+'/systemmain/audit/pageGetListAudit',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#audit_formSearch #toolbar', //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName:"sh.shmc",					//排序字段
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
            uniqueId: "shid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            }, {
                field: 'shid',
                title: '审核ID',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'shmc',
                title: '审核名称',
                titleTooltip:'审核名称',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'shlb',
                title: '审核类别',
                titleTooltip:'审核类别',
                width: '12%',
                align: 'left',
                visible: true
            }, {
                field: 'shlbmc',
                title: '审核类别名称',
                titleTooltip:'审核类别名称',
                width: '12%',
                align: 'left',
                visible: true
            }, {
                field: 'mrsz',
                title: '默认设置',
                titleTooltip:'默认设置',
                width: '10%',
                align: 'left',
                formatter:szFormatter,
                visible: true
            }, {
                field: 'ms',
                title: '描述',
                titleTooltip:'描述',
                width: '12%',
                align: 'left',
                visible: true
            }, {
                field: 'kzcs',
                title: '扩展参数',
                titleTooltip:'扩展参数',
                align: 'center',
                visible: true
            }, {
                field: 'sfgb',
                title: '是否广播',
                titleTooltip:'是否广播',
                width: '6%',
                align: 'center',
                formatter:sfgbFormatter,
                visible: true
            }, {
                title: '审核过程',
                align: 'center',
                width: '20%',
                valign: 'middle',
                formatter:dealFormatter,
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	auditDealById(row.shid, 'view',$("#audit_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#audit_formSearch #tb_list").colResizable({
            liveDrag:true, 
            gripInnerHtml:"<div class='grip'></div>", 
            draggingClass:"dragging", 
            resizeMode:'fit', 
            postbackSafe:true,
            partialRefresh:true}        
        );
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
            sortLastName: "sh.shid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

    	var cxbt = $("#audit_formSearch #cxbt").val();
    	// 查询条件
    	var cxnr = $.trim(jQuery('#audit_formSearch #cxnr').val());
    	// '0':'审核名称','1':'描述',
    	if (cxbt == "0") {
    		map["shmc"] = cxnr;
    	}else if (cxbt == "1") {
    		map["ms"] = cxnr;
    	}else if (cxbt == "2") {
			map["entire"] = cxnr;
		}else if (cxbt == "3") {
			map["shlb"] = cxnr;
		}
    	return map;
    };
    return oTableInit;
};

//默认设置
function szFormatter(value, row, index) {
    if (row.mrsz == '00') {
        return "非默认";
    }
    else{
    	return "默认";
    }
}

//是否广播
function sfgbFormatter(value,row,index) {
	if (row.sfgb =='1'){
		return "是";
	}else if (row.sfgb =='0'){
		return "否";
	}
}
//审核过程按钮样式
function dealFormatter(value, row, index) {
	var id = row.shid;
    var result = "<div class='btn-group'>";
    result += "<button class='btn btn-default' type='button' onclick=\"auditDealById('" + id + "', 'addAuditProcess','/systemmain/audit/pagedataAuditProcess')\" title='添加流程'><span class='glyphicon glyphicon-plus'>添加流程</span></button>";
    result += "</div>";
    
    return result;
}

//按钮动作函数
function auditDealById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl=$("#audit_formSearch #urlPrefix").val()+tourl;
	if(action =='view'){
		var url=tourl + "?shid=" +id;
		$.showDialog(url,'查看审核',viewAuditConfig);
	}else if(action =='add'){
		var url= tourl;
		$.showDialog(url,'新增审核',addAuditConfig);
	}else if(action =='mod'){
		var url=tourl + "?shid=" +id;
		$.showDialog(url,'编辑审核',addAuditConfig);
	}else if(action =='addAuditProcess'){
		var url= tourl + "?shid=" +id;
		$.showDialog(url,'添加流程',addAuditProcessConfig);
	}
}

var addAuditProcessConfig = {
	width		: "700px",
	modalName : "addAuditShlcModel",
	formName	: "configProcessForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确定",
			className : "btn-primary",
			callback : function	() {
				if(!$("#configProcessForm").valid()){
					return false;
				}
				
				var $this = this;
				var opts = $this["options"]||{};
				var ids="";
				
				$("#configProcessForm #tb_list").find("tbody").find("tr").each(function() {
					if($(this).attr("data-uniqueid")){
						ids = ids + ","+ $(this).attr("data-uniqueid");
					}
				});
	    		ids = ids.substr(1);
				var lclblist;
				var gwidlist;
				if(gwids!=null && gwids!="" && lclbs!=null && lclbs!=""){
					lclblist=lclbs;
					gwidlist=gwids;
				}else{
					var allTableData = $('#configProcessForm #tb_list').bootstrapTable('getData');
					for(var k=0;k<allTableData.length;k++){
						var qgwid=allTableData[k].gwid;
						var lcid=$("#"+qgwid).attr("lcid");
						gwidlist=gwidlist+","+qgwid;
						lclblist=lclblist+","+lcid;
					}
					if(ids.length == 0){

					}else {
						gwidlist=gwidlist.substr(1);
						lclblist=lclblist.substr(1);
					}
				}
	    		var url= $("#audit_formSearch #urlPrefix").val()+"/systemmain/configProcess/pagedataUpdateProcess";
	    		var shid = $("#configProcessForm #shid").val();
	    		$("#configProcessForm input[name='access_token']").val($("#ac_tk").val());
	    		jQuery.ajaxSetup({async:false});
	    		jQuery.post(url, {ids:ids,shid:shid,lclblist:lclblist,gwidlist:gwidlist,"access_token":$("#ac_tk").val()}, function(data) {
	    			setTimeout(function(){
						if(data["status"] == 'success'){
							$.success(data["message"],function() {
								searchAuditResult();
							});
						}else if(data["status"] == "fail"){
							$.error(data["message"],function() {
							});
						} else{
							$.alert(data["message"],function() {
							});
						}
					},1);
	    		}, 'json');
	    		jQuery.ajaxSetup({async:true});
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var viewAuditConfig = {
	width		: "800px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var addAuditConfig = {
	width		: "600px",
	modalName	: "addAuditModal",
	formName	: "audit_ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				
				if(!$("#audit_ajaxForm").valid()){
					return false;
				}
				
				var $this = this;
				var opts = $this["options"]||{};
				
				$("#audit_ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchAuditResult();
							}
						});
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						$.error(responseText["message"],function() {
						});
					} else{
						$.alert(responseText["message"],function() {
						});
					}
				},".modal-footer > button");
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var audit_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
    	var btn_add = $("#audit_formSearch #btn_add");
    	var btn_mod = $("#audit_formSearch #btn_mod");
    	var btn_del = $("#audit_formSearch #btn_del");
    	var btn_view = $("#audit_formSearch #btn_view");
    	var btn_audit_query = $("#btn_audit_query");

    	//绑定搜索发送功能
    	if(btn_audit_query != null){
    		btn_audit_query.unbind("click").click(function(){
    			searchAuditResult(true);
    		});
    	}
    	btn_add.unbind("click").click(function(){
    		auditDealById(null,"add",btn_add.attr("tourl"));
    	});
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#audit_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			auditDealById(sel_row[0].shid,"mod",btn_mod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#audit_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			auditDealById(sel_row[0].shid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#audit_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].shid;
    		}
    		ids = ids.substr(1);
    		$.confirm('您确定要删除所选择的记录吗？',function(result){
    			if(result){
    				jQuery.ajaxSetup({async:false});
    				var url= $("#audit_formSearch #urlPrefix").val()+btn_del.attr("tourl");
    				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
    					setTimeout(function(){
    						if(responseText["status"] == 'success'){
    							$.success(responseText["message"],function() {
    								searchAuditResult();
    							});
    						}else if(responseText["status"] == "fail"){
    							$.error(responseText["message"],function() {
    							});
    						} else{
    							$.alert(responseText["message"],function() {
    							});
    						}
    					},1);
    					
    				},'json');
    				jQuery.ajaxSetup({async:true});
    			}
    		});
    	});
    };

    return oInit;
};

function searchAuditResult(isTrunBack){
	if(isTrunBack){
		$('#audit_formSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#audit_formSearch #tb_list').bootstrapTable('refresh');
	}
}

$(function(){
    //1.初始化Table
    var oTable = new audit_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new audit_ButtonInit();
    oButtonInit.Init();
    
	//所有下拉框添加choose样式
	jQuery('#audit_formSearch .chosen-select').chosen({width: '100%'});
});
