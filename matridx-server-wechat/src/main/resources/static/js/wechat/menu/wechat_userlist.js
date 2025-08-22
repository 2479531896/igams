var WechatUser_turnOff=true;
    	
var WechatUser_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function (){
        $('#wxyh_list').bootstrapTable({
            url: '/user/user/wechatUserPage',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#userformSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "yhid",				//排序字段
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
            uniqueId: "yhid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            },{
                field: 'wxid',
                title: '微信ID',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'yhid',
                title: '用户ID',
                width: '10%',
                align: 'left',
                visible: true  
            },{
                field: 'wxm',
                title: '微信名',
                width: '15%',
                align: 'left',
                visible: true
            },{
                field: 'yhm',
                title: '用户名',
                width: '15%',
                align: 'left',
                visible: true
            },{
                field: 'yhxb',
                title: '性别',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'yhcs',
                title: '城市',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'bqidlb',
                title: '标签ID列表',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'bqm',
                title: '标签名',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'gzptmc',
                title: '关注平台',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'lrry',
                title: '录入人员',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'lrsj',
                title: '录入时间',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'xgry',
                title: '修改人员',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'xgsj',
                title: '修改时间',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'scry',
                title: '删除人员',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'scsj',
                title: '删除时间',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'scbj',
                title: '删除标记',
                width: '10%',
                align: 'left',
                visible: false
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
         	   WechatUserDealById(row.yhid,'view',$("#userformSearch #btn_view").attr("tourl"));
             },
        });
    };
    // 得到查询的参数
    oTableInit.queryParams = function(params){
    	// 请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
    	// 例如 toolbar 中的参数
    	// 如果 queryParamsType = ‘limit’ ,返回参数必须包含
    	// limit, offset, search, sort, order
    	// 否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder.
    	// 返回false将会终止请求
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   // 页面大小
        	pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "yhid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getWeUserSearchData(map);
    };
    return oTableInit;
}

function searchWxyhResult(isTurnBack){
	if(isTurnBack){
		$('#userformSearch #wxyh_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#userformSearch #wxyh_list').bootstrapTable('refresh');
	}
}

//性别格式化
function xbformat(value,row,index){
	if(row.yhxb=='1'){
		return '男';
		
	}else{
		return '女';
	}
}

function getWeUserSearchData(map){
	var cxtj=$("#userformSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#userformSearch #cxnr').val());
	if(cxtj=="0"){
		map["yhm"]=cxnr
	}else if(cxtj=="1"){
		map["wxid"]=cxnr
	}else if(cxtj=="2"){
		map["yhid"]=cxnr
	}else if(cxtj=="3"){
		map["wxm"]=cxnr
	}else if(cxtj=="4"){
		map["gzpt"]=cxnr
	}
			
	 return map;
}

var WechatUser_ButtonInit = function(){
	var oInit = new Object();
	var postdata = {};
	oInit.Init = function () {
    //初始化页面上面的按钮事件
	var btn_add = $("#userformSearch #btn_add");
	var btn_mod = $("#userformSearch #btn_mod");
	var btn_del = $("#userformSearch #btn_del");
	var btn_view = $("#userformSearch #btn_view");//
	var btn_query = $("#userformSearch #btn_query");//查询
	var btn_settag = $("#userformSearch #btn_settag");
	var btn_cancletag = $("#userformSearch #btn_cancletag");
	var btn_get = $("#userformSearch #btn_get");
	var btn_notice = $("#userformSearch #btn_messagenotice");//短信通知
	//添加日期控件
	laydate.render({
	   elem: '#jsrqstart'
	  ,theme: '#2381E9'
	});
	//添加日期控件
	laydate.render({
	   elem: '#jsrqend'
	  ,theme: '#2381E9'
	});
	if(btn_query!=null){
		btn_query.unbind("click").click(function(){
			searchWxyhResult(true); 
		});
	};

      /*---------------------------查看微信用户信息表-----------------------------------*/
	btn_view.unbind("click").click(function(){
		var sel_row = $('#userformSearch #wxyh_list').bootstrapTable('getSelections');//获取选择行数据
		if(sel_row.length==1){
			WechatUserDealById(sel_row[0].yhid,"view",btn_view.attr("tourl"));
		}else{
			$.error("请选中一行");
		}
	});
	 /* ------------------------------添加微信用户信息-----------------------------*/
	btn_add.unbind("click").click(function(){
		WechatUserDealById(null,"add",btn_add.attr("tourl"));
	});
	/* ------------------------------短信通知-----------------------------*/
	btn_notice.unbind("click").click(function(){
		WechatUserDealById(null,"messagenotice",btn_notice.attr("tourl"));
	});
	 /*---------------------------修改微信用户信息-----------------------------------*/
	btn_mod.unbind("click").click(function(){
		var sel_row = $('#userformSearch #wxyh_list').bootstrapTable('getSelections');//获取选择行数据
		if(sel_row.length==1){
			WechatUserDealById(sel_row[0].yhid,"mod",btn_mod.attr("tourl"));
		}else{
			$.error("请选中一行");
		}
	});
	 /* ------------------------------删除微信用户信息-----------------------------*/
	btn_del.unbind("click").click(function(){
		var sel_row = $('#userformSearch #wxyh_list').bootstrapTable('getSelections');//获取选择行数据
		if(sel_row.length==0){
			$.error("请至少选中一行");
			return;
		}else {
			var ids="";
			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids= ids + ","+ sel_row[i].yhid;
    		}
			ids=ids.substr(1);
			$.confirm('您确定要删除所选择的信息吗？',function(result){
    			if(result){
    				jQuery.ajaxSetup({async:false});
    				var url= btn_del.attr("tourl");
    				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
    					setTimeout(function(){
    						if(responseText["status"] == 'success'){
    							$.success(responseText["message"],function() {
    								searchWxyhResult();
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
	
	/* ------------------------------ 批量给用户添加标签 -----------------------------*/
	btn_settag.unbind("click").click(function(){
		var sel_row = $('#userformSearch #wxyh_list').bootstrapTable('getSelections');//获取选择行数据
		if(sel_row.length==0){
			$.error("请至少选中一行");
			return;
		}else{
			var ids="";
			var gzpt = sel_row[0].gzpt;
			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids= ids + ","+ sel_row[i].wxid;
    			if(sel_row[i].gzpt != gzpt){
    				$.error("请选择同一公众平台下用户！");
    				return;
    			}
    		}
			ids=ids.substr(1);
			var url=btn_settag.attr("tourl") + "?ids=" +ids+"&gzpt="+gzpt;
			$.showDialog(url,'添加标签',setUserTagConfig);
		}
	});
	
	/* ------------------------------ 批量取消用户标签 -----------------------------*/
	btn_cancletag.unbind("click").click(function(){
		var sel_row = $('#userformSearch #wxyh_list').bootstrapTable('getSelections');//获取选择行数据
		if(sel_row.length==0){
			$.error("请至少选中一行");
			return;
		}else{
			var ids="";
			var gzpt = sel_row[0].gzpt;
			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids= ids + ","+ sel_row[i].wxid;
    			if(sel_row[i].gzpt != gzpt){
    				$.error("请选择同一公众平台下用户！");
    				return;
    			}
    		}
			ids=ids.substr(1);
			var url=btn_cancletag.attr("tourl") + "?ids=" +ids+"&gzpt="+gzpt;
			$.showDialog(url,'取消标签',cancleUserTagConfig);
		}
	});
	
	/* ------------------------------ 获取微信用户信息 -----------------------------*/
	btn_get.unbind("click").click(function(){
		WechatUserDealById(null,"get",btn_get.attr("tourl"));
	});
};
return oInit;
};

function WechatUserDealById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action =='view'){
		var url= tourl + "?yhid=" +id ;
		$.showDialog(url,'微信用户详细信息',	viewUserConfig);
	}else if(action =='add'){
		var url= tourl;
		$.showDialog(url,'新增微信用户信息',addUserConfig);
	}else if(action =='mod'){
		var url=tourl + "?yhid=" +id;
		$.showDialog(url,'修改微信用户信息',modUserConfig);
	}else if(action =='get'){
		var url=tourl;
		$.showDialog(url,'获取用户信息',getUserConfig);
	}else if(action =='messagenotice'){
		var url=tourl;
		$.showDialog(url,'短信通知',MessageNoticeConfig);
	}
}

//获取微信用户信息
var getUserConfig = {
	width		: "500px",
	modalName	: "getUserModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#ajaxForm").valid()){
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
							searchWxyhResult();
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

//添加微信用户信息模态框
var addUserConfig = {
		width		: "800px",
		modalName	: "addUserModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#ajaxForm").valid()){
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
								searchWxyhResult();
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

//短信通知模态框
var MessageNoticeConfig = {
		width		: "600px",
		modalName	: "MessageNoticeModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

//查看微信用户信息模态框
var viewUserConfig = {
		width		: "800px",
		height		: "500px",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

/*   修改微信信息模态框*/
var modUserConfig = {
		width		: "800px",
		modalName	: "modUserModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#ajaxForm").valid()){
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
								searchWxyhResult();
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
//添加微信用户标签
var setUserTagConfig = {
	width		: "500px",
	modalName	: "setUserTagModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#ajaxForm").valid()){
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
							searchWxyhResult();
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
//取消微信用户标签
var cancleUserTagConfig = {
	width		: "500px",
	modalName	: "cancleUserTagModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#ajaxForm").valid()){
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
							searchWxyhResult();
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
$(function(){
    var oTable = new WechatUser_TableInit();
    oTable.Init();
    var oButtonInit = new WechatUser_ButtonInit();
    oButtonInit.Init();
    jQuery('#userformSearch .chosen-select').chosen({width: '100%'});
});
