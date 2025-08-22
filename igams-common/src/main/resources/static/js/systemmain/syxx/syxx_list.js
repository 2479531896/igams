var Message_TableInit=function(){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#syxx_formSearch #syxx_list").bootstrapTable({
			url: $("#syxx_formSearch #urlPrefix").val()+'/systemmain/syxx/pageGetList',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#syxx_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "syxx.syxxid",				// 排序字段
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
            uniqueId: "syxxid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true
            },{
                field: 'syxxid',
                title: '水印信息编号',
                width: '16%',
                align: 'left',
                visible:true
            },{
                field: 'wjlb',
                title: '文件类别',
                width: '70%',
                align: 'left',
                visible: true
            },{
                field: 'sytplj',
                title: '水印图片链接',
                width: '70%',
                align: 'left',
                visible:true,
                
            },{
                field: 'syz',
                title: '水印字',
                width: '70%',
                align: 'left',
                visible:true,
                
            },{
                field: 'syztdx',
                title: '水印字体大小',
                width: '30%',
                align: 'left',
                visible:true,
                
            },{
                field: 'syhxjg',
                title: '水印横向间隔',
                width: '30%',
                align: 'left',
                visible:true,
                
            },{
                field: 'syzxjg',
                title: '水印纵向间隔',
                width: '30%',
                align: 'left',
                visible:true,
                
            },{
                field: 'sytmd',
                title: '水印透明度',
                width: '30%',
                align: 'left',
                visible:true,
                
            },{
                field: 'syqxjd',
                title: '水印倾斜度',
                width: '30%',
                align: 'left',
                visible:true,
                
            },{
                field: 'tpsyztdx',
                title: '图片水印字体大小',
                width: '30%',
                align: 'left',
                visible:true,
                
            },{
                field: 'ksys',
                title: '开始页数',
                width: '30%',
                align: 'left',
                visible:true,
                
            },{
                field: 'ysxs',
                title: '页数显示',
                width: '30%',
                align: 'left',
                formatter:ysxsformat,
                visible:true,
                
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	Message_DealById(row.syxxid,'view',$("#syxx_formSearch #btn_view").attr("tourl"));
			}
		});
        $("#syxx_formSearch #syxx_list").colResizable({
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
            sortLastName: "syxx.syxxid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		return getMessageSearchData(map);
	};
	return oTableInit;
}

function getMessageSearchData(map){
	var syxx_select=$("#syxx_formSearch #syxx_select").val();
	var syxx_input=$.trim(jQuery('#syxx_formSearch #syxx_input').val());
	if(syxx_select=="0"){
		map["syxxid"]=syxx_input
	}else if(syxx_select=="1"){
		map["wjlb"]=syxx_input
	}
	return map;
}



function searchMessageResult(isTurnBack){
	if(isTurnBack){
		$('#syxx_formSearch #syxx_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#syxx_formSearch #syxx_list').bootstrapTable('refresh');
	}
}

function Message_DealById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl = $("#syxx_formSearch #urlPrefix").val()+tourl;
	if(action=="add"){
		var url=tourl;
		$.showDialog(url,'新增水印信息',addMessageConfig); 
	}else if(action=="mod"){
		var url=tourl+"?syxxid="+id
		$.showDialog(url,'修改水印信息',modMessageConfig);
	}else if(action=="view"){
		var url=tourl+"?syxxid="+id
		$.showDialog(url,'查看水印详细信息',viewMessageConfig);
	}
}
var addMessageConfig = {
		width		:"1100px",
		modalName	:"addMessageModal",
		offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					$("#ajaxForm #syxxid").val($("#ajaxForm #syxxid").val().trim());
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
		width		:"1100px",
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
		width		: "1100px",
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
		var btn_query=$("#syxx_formSearch #btn_query");
		var btn_add = $("#syxx_formSearch #btn_add");//新增
		var btn_mod = $("#syxx_formSearch #btn_mod");//修改
		var btn_view = $("#syxx_formSearch #btn_view");//查看
		var btn_del = $("#syxx_formSearch #btn_del");//删除
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
/*-----------------------修改------------------------------------*/
		btn_mod.unbind("click").click(function(){
    		var sel_row = $('#syxx_formSearch #syxx_list').bootstrapTable('getSelections');// 获取选择行数据
    		if(sel_row.length==1){
    			Message_DealById(sel_row[0].syxxid,"mod",btn_mod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
/*-----------------------查看------------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#syxx_formSearch #syxx_list').bootstrapTable('getSelections');// 获取选择行数据
    		if(sel_row.length==1){
    			Message_DealById(sel_row[0].syxxid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
/*-----------------------删除------------------------------------*/
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#syxx_formSearch #syxx_list').bootstrapTable('getSelections');// 获取选择行数据
    		if (sel_row.length==0) {
				$.error("请至少选中一行");
				return;
			}else{
				var ids="";
    			for (var i = 0; i < sel_row.length; i++) {// 循环读取选中行数据
        			ids= ids + ","+ sel_row[i].syxxid;
        		}
    			ids=ids.substr(1);
    			$.confirm('您确定要删除所选择的信息吗？',function(result){
        			if(result){
        				jQuery.ajaxSetup({async:false});
        				var url= btn_del.attr("tourl");
        				url = $("#syxx_formSearch #urlPrefix").val()+url
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
function ysxsformat(value,row,index){
	var ysxs="";
	if(row.ysxs=='1'){
		ysxs="<span style='color:green;'>"+"显示"+"</span>";
	}else if(row.ysxs=='0'){
		ysxs="<span style='color:red;'>"+"不显示"+"</span>";
	}
	return ysxs;
}

$(function(){
	var oTable = new Message_TableInit();
	    oTable.Init();
	
	var oButton = new Message_oButton();
	    oButton.Init();
	    
    jQuery('#syxx_formSearch .chosen-select').chosen({width: '100%'});
})