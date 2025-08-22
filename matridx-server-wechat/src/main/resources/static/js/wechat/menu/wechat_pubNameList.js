var Wechatname_TableInit=function(){
	var oTableInit = new Object();
	 oTableInit.Init = function (){
		 $('#wbcxformSearch #wbcx_list').bootstrapTable({
			url: '/menu/menu/getPageDtoList',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#wbcxformSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "wbcx.wbcxid",				//排序字段
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
            uniqueId: "wbcxid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            },{
                field: 'wbcxid',
                title: '外部程序ID',
                width: '8%',
                align: 'left',
                visible: false,
				sortable: true
            },{
                field: 'wbcxmc',
                title: '程序名称',
                width: '15%',
                align: 'left',
                visible: true,
				sortable: true
            },{
				field: 'lx',
				title: '类型',
				width: '10%',
				align: 'left',
				visible: true,
				sortable: true
			},{
                field: 'appid',
                title: 'AppId',
                width: '15%',
                align: 'left',
                visible: true,
				sortable: true
            },{
                field: 'secret',
                title: 'Secret',
                width: '20%',
                align: 'left',
                visible: true,
				sortable: true
            },{
                field: 'token',
                title: 'Token',
                width: '10%',
                align: 'left',
                visible: true,
				sortable: true
            },{
                field: 'gzpt',
                title: '关注平台',
                width: '15%',
                align: 'left',
                visible: true,
				sortable: true
            },{
                field: 'wbcxdm',
                title: '程序代码',
                width: '15%',
                align: 'left',
                visible: true,
				sortable: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败!");
            },
            onDblClickRow: function (row, $element) {
         	  PubNamDealById(row.wbcxid,'view',$("#wbcxformSearch #btn_view").attr("tourl"));
            },
		 });
	 }
	 oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   // 页面大小
        	pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "wbcx.wbcxid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		return getWechatNameSearchData(map);
	};
	 return oTableInit;
}
	 
function getWechatNameSearchData(map){
	var cxtj=$("#wbcxformSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#wbcxformSearch #cxnr').val());
	if(cxtj=="0"){
		map["wbcxmc"]=cxnr
	}else if(cxtj=="1"){
		map["gzpt"]=cxnr
	}
	return map;
}	

//按钮动作函数
function PubNamDealById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action =='view'){
		var url= tourl + "?wbcxid=" +id;
		$.showDialog(url,'查看程序信息',viewWechatNameConfig);
	}else if(action =='add'){
		var url=tourl;
		$.showDialog(url,'新增程序信息',addWechatNameConfig);
	}else if(action =='mod'){
		var url= tourl + "?wbcxid=" +id;
		$.showDialog(url,'编辑程序信息',addWechatNameConfig);
	}
}
var Wechatname_ButtonInit=function(){
	var oInit = new Object();
    var postdata = {};
    oInit.Init = function () {
    	var btn_query =$("#wbcxformSearch #btn_query");
    	var btn_add = $("#wbcxformSearch #btn_add");
    	var btn_mod = $("#wbcxformSearch #btn_mod");
    	var btn_view = $("#wbcxformSearch #btn_view");
    	var btn_del = $("#wbcxformSearch #btn_del");

    	//模糊查询
		btn_query.unbind("click").click(function(){
			searchResult(true);
		});
    	//新增
    	btn_add.unbind("click").click(function(){
    		PubNamDealById(null,"add",btn_add.attr("tourl"));
    	});
    	//修改
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#wbcxformSearch #wbcx_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			PubNamDealById(sel_row[0].wbcxid,"mod",btn_mod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	//查看
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#wbcxformSearch #wbcx_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			PubNamDealById(sel_row[0].wbcxid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	//删除
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#wbcxformSearch #wbcx_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].wbcxid;
    		}
    		ids = ids.substr(1);
    		$.confirm('您确定要删除所选择的信息吗？',function(result){
    			if(result){
    				jQuery.ajaxSetup({async:false});
    				var url= btn_del.attr("tourl");
    				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
    					setTimeout(function(){
    						if(responseText["status"] == 'success'){
    							$.success(responseText["message"],function() {
    								searchResult();
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
    }
    return oInit;
}

var addWechatNameConfig = {
		width		: "600px",
		modalName	: "addWechatModal",
		formName	: "wechat_ajaxForm",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					
					if(!$("#wechat_ajaxForm").valid()){
						return false;
					}
					
					var $this = this;
					var opts = $this["options"]||{};
					
					$("#wechat_ajaxForm input[name='access_token']").val($("#ac_tk").val());
					
					submitForm(opts["formName"]||"wechat_ajaxForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
									searchResult();
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
	};

var viewWechatNameConfig = {
		width		: "600px",
		modalName	: "viewWechatModal",
		formName	: "viewUser_ajaxForm",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};
function searchResult(isTurnBack){
	if(isTurnBack){
		$('#wbcxformSearch #wbcx_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#wbcxformSearch #wbcx_list').bootstrapTable('refresh');
	}
}
$(function(){
    var oTable = new Wechatname_TableInit();
    oTable.Init();
    
    var oButtonInit = new Wechatname_ButtonInit();
    oButtonInit.Init();
    
    jQuery('#wbcxformSearch .chosen-select').chosen({width: '100%'});
});
