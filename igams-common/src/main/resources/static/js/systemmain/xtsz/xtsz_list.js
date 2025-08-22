var Message_TableInit=function(){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#xtsz_formSearch #xtsz_list").bootstrapTable({
			url: $("#xtsz_formSearch #urlPrefix").val()+'/systemmain/xtsz/pagedataListXtsz',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#xtsz_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "sz.szlb",				// 排序字段
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
            uniqueId: "szlb",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true
            },{
                field: 'szlb',
                title: '设置类别',
                width: '70%',
                align: 'left',
                visible:true
            },{
                field: 'szmc',
                title: '设置名称',
                width: '70%',
                align: 'left',
                visible: true
            },{
                field: 'szz',
                title: '设置值',
                width: '50%',
                align: 'left',
                visible:true,
                
            },{
                field: 'xh',
                title: '序号',
                width: '30%',
                align: 'left',
                visible:true,
                
            },{
                field: 'bz',
                title: '备注',
                width: '30%',
                align: 'left',
                visible:true,
                
            },{
                field: 'xslx',
                title: '显示类型',
                width: '30%',
                align: 'left',
                formatter:xslxformat,
                visible:true,
                
            },{
                field: 'kzcs1',
                title: '扩展参数1',
                width: '30%',
                align: 'left',
                visible:false,
                
            },{
                field: 'kzcs2',
                title: '扩展参数2',
                width: '30%',
                align: 'left',
                visible:false,
                
            },{
                field: 'kzcs3',
                title: '扩展参数3',
                width: '30%',
                align: 'left',
                visible:false,
                
            },{
                field: 'kzcs4',
                title: '扩展参数4',
                width: '30%',
                align: 'left',
                visible:false,
                
            }],
            onLoadSuccess:function(map){
				if(map.count>0){
					flag = true;
					var strnum=map.count;
					if(map.count>99){
						strnum='99+';
					}
					var html="";
					if($("#xtsz_formSearch #insection_num").length>0){
						$("#xtsz_formSearch #insection_num").remove();
					}
					html+="<span id='insection_num' style='position:absolute;background-color:red;color:white;border-radius: 15px;border:0px;width:20px;height:20px;top:-10px;font-size:10px;line-height:150%;'>"+strnum+"</span>";
					$("#xtsz_formSearch #btn_rabbit").append(html);
				}else{
					flag = false;
					$("#xtsz_formSearch #insection_num").remove();
				}

            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	Message_DealById(row.szlb,'view',$("#xtsz_formSearch #btn_view").attr("tourl"));
			}
		});
        $("#xtsz_formSearch #xtsz_list").colResizable({
            liveDrag:true, 
            gripInnerHtml:"<div class='grip'></div>", 
            draggingClass:"dragging", 
            resizeMode:'fit', 
            postbackSafe:true,
            partialRefresh:true}        
        );
	};
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一致，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   // 页面大小
        	pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "sz.xh", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		return getMessageSearchData(map);
	};
	return oTableInit;
}
var flag =false;
function getMessageSearchData(map){
	var xtsz_select=$("#xtsz_formSearch #xtsz_select").val();
	var xtsz_input=$.trim(jQuery('#xtsz_formSearch #xtsz_input').val());
	if(xtsz_select=="0"){
		map["szlb"]=xtsz_input
	}else if(xtsz_select=="1"){
		map["szmc"]=xtsz_input
	}
	return map;
}



function searchMessageResult(isTurnBack){
	if(isTurnBack){
		$('#xtsz_formSearch #xtsz_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#xtsz_formSearch #xtsz_list').bootstrapTable('refresh');
	}
}

function Message_DealById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl = $("#xtsz_formSearch #urlPrefix").val()+tourl;
	if(action=="add"){
		var url=tourl;
		$.showDialog(url,'新增系统设置信息',addMessageConfig); 
	}else if(action=="mod"){
		var url=tourl+"?szlb="+id
		$.showDialog(url,'修改系统设置信息',modMessageConfig);
	}else if(action=="view"){
		var url=tourl+"?szlb="+id
		$.showDialog(url,'查看系统设置详细信息',viewMessageConfig);
	}else if(action=="rabbit"){
		var url=tourl
		$.showDialog(url,'异常rabbit消息',rabbitMessageConfig);
	}
}

var rabbitMessageConfig = {
	width		: "800px",
	modalName	:"rabbitMessageModal",
	offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {

				var $this = this;
				var opts = $this["options"]||{};
				$("#xtszRabbitForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"xtszRabbitForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							$("#xtszRabbitForm #routingKey").val('');
							$("#xtszRabbitForm #exchange").val('');
							$("#xtszRabbitForm #body").text('');
							$("#xtszRabbitForm #key").text('');
							$("#xtszRabbitForm #exchangeDiv").text('');
							$("#xtszRabbitForm #message").text('');
							searchMessageResult();
						});
					}else if(responseText["status"] == "fail"){
						$.error(responseText["message"],function() {});
					} else{
						$.alert(responseText["message"],function() {});
					}
				},".modal-footer > button");
				preventResubmitForm(".modal-footer > button", false);
				return false;
			}
		},
		success1 : {
			label : "NEXT",
			className : "btn-primary",
			callback : function() {
				$.ajax({
					url : '/systemmain/xtsz/pagedataRabbitInfo',
					type : 'post',
					data : {
						"access_token" : $("#ac_tk").val()
					},
					dataType : 'json',
					success : function(result) {
						if(result.status == "success"){
							$("#xtszRabbitForm #routingKey").val(result.routingKey);
							$("#xtszRabbitForm #exchange").val(result.exchange);
							$("#xtszRabbitForm #body").text(result.body);
							$("#xtszRabbitForm #key").text(result.routingKey);
							$("#xtszRabbitForm #exchangeDiv").text(result.exchange);
							$("#xtszRabbitForm #message").text(result.message);
						}else{
							var $this = this;
							var opts = $this["options"]||{};
							$.success(result.info,function() {
								$.closeModal(opts.modalName);
								searchMessageResult();
							});
						}
					}
				});
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default",
			callback : function() {
				searchMessageResult();
			}
		}
	}
};
var addMessageConfig = {
		width		:"800px",
		modalName	:"addMessageModal",
		offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					$("#xtszaddForm #szlb").val($.trim($("#xtszaddForm #szlb").val()));
					if(!$("#xtszaddForm").valid()){// 表单验证
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					$("#xtszaddForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"xtszaddForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								searchMessageResult();
							});
						}else if(responseText["status"] == "fail"){
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
		width		:"800px",
		modalName	:"modMessageModal",
		offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					$("#xtszmodForm #xxid").val($("#xtszmodForm #xxid").val());
					if(!$("#xtszmodForm").valid()){
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					$("#xtszmodForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"xtszmodForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								searchMessageResult();
							});
						}else if(responseText["status"] == "fail"){
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
		var btn_query=$("#xtsz_formSearch #btn_query");
		var btn_add = $("#xtsz_formSearch #btn_add");//新增
		var btn_mod = $("#xtsz_formSearch #btn_mod");//修改
		var btn_view = $("#xtsz_formSearch #btn_view");//查看
		var btn_del = $("#xtsz_formSearch #btn_del");//删除
		var btn_rabbit = $("#xtsz_formSearch #btn_rabbit");//rabbit消息处理
/*-----------------------模糊查询ok------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchMessageResult(true); 
    		});
		}
		/*-----------------------新增------------------------------------*/
		btn_rabbit.unbind("click").click(function(){
			if (flag){
				Message_DealById(null,"rabbit",btn_rabbit.attr("tourl"));
			}else{
				$.error("没有需要处理得数据！");
			}
		});

		/*-----------------------新增------------------------------------*/
		btn_add.unbind("click").click(function(){
				Message_DealById(null,"add",btn_add.attr("tourl"));
    	});
/*-----------------------修改------------------------------------*/
		btn_mod.unbind("click").click(function(){
    		var sel_row = $('#xtsz_formSearch #xtsz_list').bootstrapTable('getSelections');// 获取选择行数据
    		if(sel_row.length==1){
    			Message_DealById(sel_row[0].szlb,"mod",btn_mod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
/*-----------------------查看------------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#xtsz_formSearch #xtsz_list').bootstrapTable('getSelections');// 获取选择行数据
    		if(sel_row.length==1){
    			Message_DealById(sel_row[0].szlb,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
/*-----------------------删除------------------------------------*/
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#xtsz_formSearch #xtsz_list').bootstrapTable('getSelections');// 获取选择行数据
    		if (sel_row.length==0) {
				$.error("请至少选中一行");
				return;
			}else{
				var ids="";
    			for (var i = 0; i < sel_row.length; i++) {// 循环读取选中行数据
        			ids= ids + ","+ sel_row[i].szlb;
        		}
    			ids=ids.substr(1);
    			$.confirm('您确定要删除所选择的信息吗？',function(result){
        			if(result){
        				jQuery.ajaxSetup({async:false});
        				var url= btn_del.attr("tourl");
        				url = $("#xtsz_formSearch #urlPrefix").val()+url
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
	}
	return oButtonInit;
}
function xslxformat(value,row,index){
	var xslx="";
	if(row.xslx=='00'){
		xslx="<span>"+"普通输入框"+"</span>";
	}else if(row.xslx=='01'){
		xslx="<span>"+"带单位的输入框"+"</span>";
	}else if(row.xslx=='02'){
		xslx="<span>"+"单选框"+"</span>";
	}
	return xslx;
}

$(function(){
	var oTable = new Message_TableInit();
	    oTable.Init();
	
	var oButton = new Message_oButton();
	    oButton.Init();
	    
    jQuery('#xtsz_formSearch .chosen-select').chosen({width: '100%'});
})