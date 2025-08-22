var message_turnOff=true;
var Message_TableInit=function(){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#message_formSearch #message_list").bootstrapTable({
			url: $("#message_formSearch #urlPrefix").val()+'/message/message/pageGetListMessage',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#message_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "xxgl.xxid",				// 排序字段
            sortOrder: "asc",                   // 排序方式
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
            uniqueId: "xxid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
				width: '4%'
            },{
                field: 'xxid',
                title: '信息编号',
                width: '16%',
                align: 'left',
                visible:true
            },{
                field: 'xxlxmc',
                title: '信息类型',
                width: '14%',
                align: 'left',
                visible: true
            },{
                field: 'xxnr',
                title: '信息内容',
                width: '40%',
                align: 'left',
                visible:true,
			},{
				field: 'ddqid',
				title: '钉钉群',
				width: '30%',
				align: 'left',
				visible:true,
                
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	Message_DealById(row.xxid,'view',$("#message_formSearch #btn_view").attr("tourl"));
             },
		});
        $("#message_formSearch #message_list").colResizable({
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
            sortLastName: "xxgl.xxid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		return getMessageSearchData(map);
	};
	return oTableInit;
}

function getMessageSearchData(map){
	var xxgl_select=$("#message_formSearch #xxgl_select").val();
	var xxnr_input=$.trim(jQuery('#message_formSearch #xxnr_input').val());
	if(xxgl_select=="0"){
		map["xxid"]=xxnr_input
	}else if(xxgl_select=="1"){
		map["xxlxmc"]=xxnr_input
	}else if(xxgl_select=="2"){
		map["xxnr"]=xxnr_input
	}
	var xxlx = jQuery('#message_formSearch #xxlx_id_tj').val();
    map["xxlxs"] = xxlx;
	return map;
}



function searchMessageResult(isTurnBack){
    $("#message_formSearch #searchMore").slideUp("low");
	message_turnOff=true;
	$("#message_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#message_formSearch #message_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#message_formSearch #message_list').bootstrapTable('refresh');
	}
}
function Message_DealById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl = $("#message_formSearch #urlPrefix").val()+tourl;
	if(action=="add"){
		var url=tourl;
		$.showDialog(url,'新增消息信息',addMessageConfig); 
	}else if(action=="mod"){
		var url=tourl+"?xxid="+id
		$.showDialog(url,'修改消息信息',modMessageConfig);
	}else if(action=="view"){
		var url=tourl+"?xxid="+id
		$.showDialog(url,'查看消息详细信息',viewMessageConfig);
	}else if(action=="setdefault"){
	    var url=tourl;
    	$.showDialog(url,'消息订阅默认设置',setDefaultMessageConfig);
	}
}

var setDefaultMessageConfig = {
		width		: "1200px",
		modalName	:"setDefaultMessageModal",
		offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					var $this = this;
					var opts = $this["options"]||{};
					$("#setForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"setForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								searchMessageResult();
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


var addMessageConfig = {
		width		: "800px",
		modalName	:"addMessageModal",
		offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					$("#ajaxForm #xxid").val($("#ajaxForm #xxid").val().trim());
					if(!$("#ajaxForm").valid()){// 表单验证
						$.alert("请填写完整信息");
						return false;
					}
					var cskz1 = $("#ajaxForm #xxlx option:selected").attr("cskz1");
                    $("#ajaxForm #cskz1").val(cskz1);
					var $this = this;
					var opts = $this["options"]||{};
					$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								searchMessageResult();
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

var modMessageConfig = {
		width		: "800px",
		modalName	:"modMessageModal",
		offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					$("#ajaxForm #xxid").val($("#ajaxForm #xxid").val());
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
								searchMessageResult();
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


var viewMessageConfig = {
		width		: "800px",
		modalName	:"viewMessageModal",
		offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};


var Message_oButton=function(){
	var oButtonInit = new Object();
	var postdata = {};
	oButtonInit.Init=function(){
		var btn_query=$("#message_formSearch #btn_query");
		var btn_add = $("#message_formSearch #btn_add");
		var btn_mod = $("#message_formSearch #btn_mod");
		var btn_view = $("#message_formSearch #btn_view");
		var btn_del = $("#message_formSearch #btn_del");
		var btn_setdefault = $("#message_formSearch #btn_setdefault");
/*-----------------------模糊查询ok------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchMessageResult(true); 
    		});
		}
/*-----------------------新增------------------------------------*/
		btn_add.unbind("click").click(function(){
				Message_DealById(null,"add",btn_add.attr("tourl"));
    	});
/*-----------------------订阅消息默认设置------------------------------------*/
        btn_setdefault.unbind("click").click(function(){
                Message_DealById(null,"setdefault",btn_setdefault.attr("tourl"));
        });
/*-----------------------修改------------------------------------*/
		btn_mod.unbind("click").click(function(){
    		var sel_row = $('#message_formSearch #message_list').bootstrapTable('getSelections');// 获取选择行数据
    		if(sel_row.length==1){
    			Message_DealById(sel_row[0].xxid,"mod",btn_mod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
/*-----------------------查看------------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#message_formSearch #message_list').bootstrapTable('getSelections');// 获取选择行数据
    		if(sel_row.length==1){
    			Message_DealById(sel_row[0].xxid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
/*-----------------------删除------------------------------------*/
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#message_formSearch #message_list').bootstrapTable('getSelections');// 获取选择行数据
    		if (sel_row.length==0) {
				$.error("请至少选中一行");
				return;
			}else{
				var ids="";
    			for (var i = 0; i < sel_row.length; i++) {// 循环读取选中行数据
        			ids= ids + ","+ sel_row[i].xxid;
        		}
    			ids=ids.substr(1);
    			$.confirm('您确定要删除所选择的信息吗？',function(result){
        			if(result){
        				jQuery.ajaxSetup({async:false});
        				var url= $("#message_formSearch #urlPrefix").val()+btn_del.attr("tourl");
        				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
        					setTimeout(function(){
        						if(responseText["status"] == 'success'){
        							$.success(responseText["message"],function() {
        								searchMessageResult();
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
        $("#message_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(message_turnOff){
                $("#message_formSearch #searchMore").slideDown("low");
                message_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#message_formSearch #searchMore").slideUp("low");
                message_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });
	}
	return oButtonInit;
}


$(function(){
	var oTable = new Message_TableInit();
	    oTable.Init();
	
	var oButton = new Message_oButton();
	    oButton.Init();
	    
    jQuery('#message_formSearch .chosen-select').chosen({width: '100%'});
})