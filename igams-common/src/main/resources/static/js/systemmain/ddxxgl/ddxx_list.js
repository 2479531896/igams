var Ddxxgl_TableInit=function(){
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#ddxxgl_formSearch  #ddxxgl_list").bootstrapTable({
			url: $("#ddxxgl_formSearch #urlPrefix").val()+'/common/ddxxgl/pageGetList',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#ddxxgl_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "xxgl.ddxxlx desc, xxgl.tzlx",				//排序字段
            sortOrder: "desc",                   //排序方式
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
            uniqueId: "ddxxid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
            	checkbox: true,
            	width: '4%'
            },{
                field: 'ddxxid',
                title: '钉钉消息id',
                width: '25%',
                align: 'left',
                visible:false
            },{
                field: 'ddxxlx',
                title: '消息类型',
                width: '32%',
                align: 'left',
                visible: true
            },{
                field: 'tzlx',
                title: '通知类型',
                width: '32%',
                align: 'left',
                visible:true
            },{
				field: 'yhm',
				title: '用户名',
				width: '15%',
				align: 'left',
				visible: true
			},{
                field: 'zsxm',
                title: '姓名',
                width: '15%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	//双击事件
            	Btn_Ddxxgl(row.ddxxid,"view",$("#ddxxgl_formSearch #btn_view").attr("tourl"));
            },
		});
		$("#ddxxgl_formSearch  #ddxxgl_list").colResizable({
            liveDrag:true, 
            gripInnerHtml:"<div class='grip'></div>", 
            draggingClass:"dragging", 
            resizeMode:'fit', 
            postbackSafe:true,
            partialRefresh:true
            });
	};
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   // 页面大小
        	pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "xxgl.ddxxid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		return getDDxxSearchData(map);
		};
		return oTableInit;
}
function getDDxxSearchData(map){
	var cxtj=$("#ddxxgl_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#ddxxgl_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["entire"]=cxnr;
	}else if(cxtj=="1"){
		map["zsxm"]=cxnr;
	}else if(cxtj=="2"){
		map["yhm"]=cxnr;
	}
	var xxlx=$.trim($("#ddxxgl_formSearch #xxlx").val());
	var tzlx=$.trim($("#ddxxgl_formSearch #tzlx").val());
	map["ddxxlx"]=xxlx;
	map["tzlx"]=tzlx;
	return map;
}
var Ddxxgl_ButtonInit = function(){
	var oInit = new Object();
	var postdata = {};
	oInit.Init = function () {
    //初始化页面上面的按钮事件
		var btn_query=$("#ddxxgl_formSearch #btn_query");
		var btn_add=$("#ddxxgl_formSearch #btn_add");
		var btn_mod=$("#ddxxgl_formSearch #btn_mod");
		var btn_del=$("#ddxxgl_formSearch #btn_del");
		var btn_view=$("#ddxxgl_formSearch #btn_view");
		var btn_registercallback=$("#ddxxgl_formSearch #btn_registercallback");
		/*-----------------------模糊查询------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchDdxxResult(true); 
    		});
		}
		 /*------------------------------添加钉钉消息-----------------------------*/
		btn_add.unbind("click").click(function(){
	 		Btn_Ddxxgl(null,"add",btn_add.attr("tourl"));
	 	});
		 /*------------------------------修改钉钉消息-----------------------------*/
		btn_mod.unbind("click").click(function(){
			var sel_row =$('#ddxxgl_formSearch  #ddxxgl_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				Btn_Ddxxgl(sel_row[0].ddxxid,"mod",btn_mod.attr("tourl"));
			}else {
				$.error("请选择一行！");
			}
	 		
	 	});
		 /*------------------------------删除钉钉消息-----------------------------*/
		btn_del.unbind("click").click(function(){
			var sel_row = $('#ddxxgl_formSearch  #ddxxgl_list').bootstrapTable('getSelections');//获取选择行数据
	 		if (sel_row.length==0) {
				$.error("请至少选中一行");
				return;
			}else{
				var ids="";
    			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids= ids + ","+ sel_row[i].ddxxid;
        		}
    			ids=ids.substr(1);
    			$.confirm('您确定要删除所选择的信息吗？',function(result){
        			if(result){
        				jQuery.ajaxSetup({async:false});
        				var url= btn_del.attr("tourl");
        				jQuery.post($("#ddxxgl_formSearch #urlPrefix").val()+url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
        					setTimeout(function(){
        						if(responseText["status"] == 'success'){
        							$.success(responseText["message"],function() {
        								searchDdxxResult();
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
		/*------------------------------查看钉钉消息-----------------------------*/
		btn_view.unbind("click").click(function(){
			var sel_row =$('#ddxxgl_formSearch  #ddxxgl_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				Btn_Ddxxgl(sel_row[0].ddxxid,"view",btn_view.attr("tourl"));
			}else {
				$.error("请选择一行！");
			}
	 		
	 	});
		
		/*------------------------------查看钉钉注册回调-----------------------------*/
		btn_registercallback.unbind("click").click(function(){
			Btn_Ddxxgl(null,"registercallback",btn_registercallback.attr("tourl"));
	 	});
		
	};
	
return oInit;
};
function Btn_Ddxxgl(ddxxid,action,tourl){
	if(!tourl){
		return;
	}	
	tourl = $("#ddxxgl_formSearch #urlPrefix").val()+tourl;
	if(action =='add'){
		var url= tourl;
		$.showDialog(url,'钉钉消息新增',addDDxxglConfig);
	}else if(action =='mod'){
		var url=tourl+"?ddxxid="+ddxxid;
		$.showDialog(url,'钉钉消息修改',modDDxxglConfig);
	}else if(action =='view'){
		var url=tourl+"?ddxxid="+ddxxid;
		$.showDialog(url,'查看',viewDDxxglConfig);
	}else if(action='registercallback'){
		var url=tourl;
		$.showDialog(url,'注册钉钉回调',registerCallbackConfig);
	}
}
var addDDxxglConfig = {
		width		: "800px",
		modalName	: "addListModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#addListForm").valid()){
						$.alert("请填写完整信息");
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					$("#addListForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"addListForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								searchDdxxResult();
							});
						}else if(responseText["status"] == "fail"){
							$.error(responseText["message"],function() {
							});
						} else{
							$.alert(responseText["message"],function() {
							});
						}
					});
					return false;
				}
			},
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};
var modDDxxglConfig = {
		width		: "800px",
		modalName	: "modListModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#addListForm").valid()){
						$.alert("请填写完整信息");
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					$("#addListForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"addListForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								searchDdxxResult();
							});
						}else if(responseText["status"] == "fail"){
							$.error(responseText["message"],function() {
							});
						} else{
							$.alert(responseText["message"],function() {
							});
						}
					});
					return false;
				}
			},
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};
var viewDDxxglConfig={
		width		: "800px",
		modalName	: "viewListModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
}

var registerCallbackConfig={
		width		: "800px",
		modalName	: "registerCallbackModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#dingtalkCallbackForm").valid()){
						$.alert("请填写完整信息");
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					$("#dingtalkCallbackForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"dingtalkCallbackForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
							});
						}else if(responseText["status"] == "fail"){
							$.error(responseText["message"],function() {
							});
						} else{
							$.alert(responseText["message"],function() {
							});
						}
					});
					return false;
				}
			},
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
}
function searchDdxxResult(isTurnBack){
	if(isTurnBack){
		$('#ddxxgl_formSearch  #ddxxgl_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#ddxxgl_formSearch  #ddxxgl_list').bootstrapTable('refresh');
	}
}
$(function(){
	var oTable=new Ddxxgl_TableInit();
		oTable.Init();
	var oButtonInit = new Ddxxgl_ButtonInit();
		oButtonInit.Init();
	jQuery('#ddxxgl_formSearch .chosen-select').chosen({width: '100%'});
})