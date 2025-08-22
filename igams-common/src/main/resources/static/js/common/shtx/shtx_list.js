var Shtx_TableInit=function(){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#shtx_formSearch #shtx_list").bootstrapTable({
			url: $("#shtx_formSearch #urlPrefix").val()+'/systemmain/audit/pageGetListShtxData',
            method: 'get',                      // 请求方式（*）
            toolbar: '#shtx_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "shtx.txyqrs",				// 排序字段
            sortOrder: "desc",                   // 排序方式
            queryParams: oTableInit.queryParams,// 传递参数（*）
            sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       // 初始化加载第一页，默认第一页
            pageSize: 15,                       // 每页的记录行数（*）
            pageList: [15, 30, 50, 100],        // 可供选择的每页的行数（*）
            paginationPreText: '‹',				// 指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			// 指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  // 是否显示所有的列
            showRefresh: true,                  // 是否显示刷新按钮
            minimumCountColumns: 2,             // 最少允许的列数
            clickToSelect: true,                // 是否启用点击选中行
            // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "txid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true
            },{
                field: 'txid',
                title: '提醒编号',
                width: '50%',
                align: 'left',
                visible:false
            },{
                field: 'txlb',
                title: '提醒类别',
                width: '50%',
                align: 'left',
                visible: true
            },{
                field: 'txyqrs',
                title: '提醒延期小时',
                width: '50%',
                align: 'left',
                visible:true,
                
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	Shtx_DealById(row.txid,'view',$("#shtx_formSearch #btn_view").attr("tourl"));
             },
		});
        $("#shtx_formSearch #shtx_list").colResizable({
            liveDrag:true, 
            gripInnerHtml:"<div class='grip'></div>", 
            draggingClass:"dragging", 
            resizeMode:'fit', 
            postbackSafe:true,
            partialRefresh:true}        
        );
	};
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   // 页面大小
        	pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "shtx.txid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		return getShtxSearchData(map);
	};
	return oTableInit;
}


function getShtxSearchData(map){
	var shtx_select=$("#shtx_formSearch #shtx_select").val();
	var shtx_input=$.trim(jQuery('#shtx_formSearch #shtx_input').val());
	if(shtx_select=="0"){
		map["txid"]=shtx_input
	}else if(shtx_select=="1"){
		map["txlb"]=shtx_input
	}else if(shtx_select=="2"){
		map["txyqrs"]=shtx_input
	}
	return map;
}



function searchShtxResult(isTurnBack){
	if(isTurnBack){
		$('#shtx_formSearch #shtx_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#shtx_formSearch #shtx_list').bootstrapTable('refresh');
	}
}
function Shtx_DealById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl = $("#shtx_formSearch #urlPrefix").val() + tourl;
	if(action=="add"){
		var url=tourl;
		$.showDialog(url,'新增审核提醒',addShtxConfig); 
	}else if(action=="view"){
		var url=tourl+"?txid="+id
		$.showDialog(url,'查看审核提醒',viewShtxConfig);
	}else if(action=="mod"){
		var url=tourl+"?txid="+id
		$.showDialog(url,'修改审核提醒',modShtxConfig);
	}else if(action=="del"){
		var url=tourl+"?txid="+id
		$.showDialog(url,'删除审核提醒',delShtxConfig);
	}
}
var addShtxConfig = {
		width		: "800px",
		modalName	:"addShtxModal",
		offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
//					$("#ajaxForm #txid").val($("#ajaxForm #txid").val().trim());
					if(!$("#ajaxForm").valid()){// 表单验证
						$.alert("请填写完整信息");
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								searchShtxResult();
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

var modShtxConfig = {
		width		: "800px",
		modalName	:"modShtxModal",
		offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					$("#ajaxForm #txid").val($("#ajaxForm #txid").val());
					if(!$("#ajaxForm").valid()){
						$.alert("请填写完整信息");
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								searchShtxResult();
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


var viewShtxConfig = {
		width		: "800px",
		modalName	:"viewShtxModal",
		offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};


var Shtx_oButton=function(){
	var oButtonInit = new Object();
	var postdata = {};
	oButtonInit.Init=function(){
		var btn_query=$("#shtx_formSearch #btn_query");
		var btn_add = $("#shtx_formSearch #btn_add");
		var btn_mod = $("#shtx_formSearch #btn_mod");
		var btn_view = $("#shtx_formSearch #btn_view");
		var btn_del = $("#shtx_formSearch #btn_del");
/*-----------------------模糊查询ok------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchShtxResult(true); 
    		});
		}
/*-----------------------新增------------------------------------*/
		btn_add.unbind("click").click(function(){
				Shtx_DealById(null,"add",btn_add.attr("tourl"));
    	});
/*-----------------------修改------------------------------------*/
		btn_mod.unbind("click").click(function(){
    		var sel_row = $('#shtx_formSearch #shtx_list').bootstrapTable('getSelections');// 获取选择行数据
    		if(sel_row.length==1){
    			Shtx_DealById(sel_row[0].txid,"mod",btn_mod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
/*-----------------------查看------------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#shtx_formSearch #shtx_list').bootstrapTable('getSelections');// 获取选择行数据
    		if(sel_row.length==1){
    			Shtx_DealById(sel_row[0].txid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
/*-----------------------删除------------------------------------*/
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#shtx_formSearch #shtx_list').bootstrapTable('getSelections');// 获取选择行数据
    		if (sel_row.length==0) {
				$.error("请至少选中一行");
				return;
			}else{
				var ids="";
    			for (var i = 0; i < sel_row.length; i++) {// 循环读取选中行数据
        			ids= ids + ","+ sel_row[i].txid;
        		}
    			ids=ids.substr(1);
    			$.confirm('您确定要删除所选择的信息吗？',function(result){
        			if(result){
        				jQuery.ajaxSetup({async:false});
        				var url= $('#shtx_formSearch #urlPrefix').val()+btn_del.attr("tourl");
        				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
        					setTimeout(function(){
        						if(responseText["status"] == 'success'){
        							$.success(responseText["message"],function() {
        								searchShtxResult();
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
			}
    	});
/*-------------------------------------------------------------*/
	}
	return oButtonInit;
}


$(function(){
	var oTable = new Shtx_TableInit();
	    oTable.Init();
	
	var oButton = new Shtx_oButton();
	    oButton.Init();
	    
    jQuery('#shtx_formSearch .chosen-select').chosen({width: '100%'});
})