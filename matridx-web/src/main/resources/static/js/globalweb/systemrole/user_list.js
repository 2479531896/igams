
var user_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#userformSearch #tb_list').bootstrapTable({
            url: '/systemrole/user/listUser',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#userformSearch #toolbar', //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName:"yh.yhm",					//排序字段
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
            uniqueId: "yhid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            }, {
                field: 'yhid',
                title: '用户ID',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'ddid',
                title: '钉钉ID',
                width: '8%',
                align: 'left',
                visible: true
            }, {
                field: 'wechatid',
                title: '微信ID',
                width: '8%',
                align: 'left',
                visible: true
            }, {
                field: 'gwmc',
                title: '岗位名称',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'yhm',
                title: '用户名',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'zsxm',
                title: '真实姓名',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'sfsd',
                title: '是否锁定',
                width: '10%',
                align: 'left',
                formatter:sdFormatter,
                visible: true
            }, {
                field: 'dqjsmc',
                title: '当前角色',
                width: '12%',
                align: 'left',
                visible: true
            },{
                field: 'dlsj',
                title: '登录时间',
                align: 'center',
                sortable: true,
                visible: true
            },{
                field: 'cwcs',
                title: '错误次数',
                width: '6%',
                align: 'left',
                visible: true,
                sortable: true,
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	userDealById(row.yhid, 'view',$("#userformSearch #btn_view").attr("tourl"));
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
            sortLastName: "yh.yhid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

    	var cxbt = $("#userformSearch #cxbt").val();
    	// 查询条件
    	var cxnr = $.trim(jQuery('#userformSearch #cxnr').val());
    	// '0':'来源编号','1':'外部编号','2':'孕周','3':'体重','4':'备注'
    	if (cxbt == "0") {
    		map["yhm"] = cxnr;
    	}else if (cxbt == "1") {
    		map["zsxm"] = cxnr;
    	}
    	return map;
    };
    return oTableInit;
};

//锁定
function sdFormatter(value, row, index) {
    if (row.sfsd == '1') {
        return '已锁定';
    }
    else{
    	return "正常";
    }
}

var addUserConfig = {
	width		: "600px",
	modalName	: "addUserModal",
	formName	: "user_ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				
				if(!$("#user_ajaxForm").valid()){
					return false;
				}
				
				var $this = this;
				var opts = $this["options"]||{};
				
				$("#user_ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchUserResult();
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

var viewUserConfig = {
	width		: "600px",
	modalName	: "viewUserModal",
	formName	: "viewUser_ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

//按钮动作函数
function userDealById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action =='view'){
		var url= tourl + "?yhid=" +id;
		$.showDialog(url,'查看用户',viewUserConfig);
	}else if(action =='add'){
		var url=tourl;
		$.showDialog(url,'新增用户',addUserConfig);
	}else if(action =='mod'){
		var url= tourl + "?yhid=" +id;
		$.showDialog(url,'编辑用户',addUserConfig);
	}
}


var user_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
    	var btn_add = $("#userformSearch #btn_add");
    	var btn_mod = $("#userformSearch #btn_mod");
    	var btn_del = $("#userformSearch #btn_del");
    	var btn_view = $("#userformSearch #btn_view");
    	var btn_initpass = $("#userformSearch #btn_initpass");
    	var btn_initdd = $("#userformSearch #btn_initdd");
    	var btn_user_query = $("#btn_user_query");

    	//绑定搜索发送功能
    	if(btn_user_query != null){
    		btn_user_query.unbind("click").click(function(){
    			searchUserResult(true);
    		});
    	}
    	btn_add.unbind("click").click(function(){
    		userDealById(null,"add",btn_add.attr("tourl"));
    	});
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#userformSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			userDealById(sel_row[0].yhid,"mod",btn_mod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#userformSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			userDealById(sel_row[0].yhid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_initpass.unbind("click").click(function(){
    		var sel_row = $('#userformSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].yhid;
    		}
    		ids = ids.substr(1);
    		$.confirm('您确定要初始化所选用户的密码吗？',function(result){
    			if(result){
    				jQuery.ajaxSetup({async:false});
    				var url= btn_initpass.attr("tourl");
    				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
    					setTimeout(function(){
    						if(responseText["status"] == 'success'){
    							$.success(responseText["message"],function() {
    								searchUserResult();
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
    	
    	btn_initdd.unbind("click").click(function(){
    		$.confirm('您确定要更新钉钉ID信息吗？',function(result){
    			if(result){
    				jQuery.ajaxSetup({async:false});
    				var url= btn_initdd.attr("tourl");
    				jQuery.post(url,{"access_token":$("#ac_tk").val()},function(responseText){
    					setTimeout(function(){
    						if(responseText["status"] == 'success'){
    							$.success(responseText["message"],function() {
    								searchUserResult();
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
    	
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#userformSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].yhid;
    		}
    		ids = ids.substr(1);
    		$.confirm('您确定要删除所选择的记录吗？',function(result){
    			if(result){
    				jQuery.ajaxSetup({async:false});
    				var url= btn_del.attr("tourl");
    				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
    					setTimeout(function(){
    						if(responseText["status"] == 'success'){
    							$.success(responseText["message"],function() {
    								searchUserResult();
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

function searchUserResult(isTurnBack){
	if(isTurnBack){
		$('#userformSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#userformSearch #tb_list').bootstrapTable('refresh');
	}
}

$(function(){

    //1.初始化Table
    var oTable = new user_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new user_ButtonInit();
    oButtonInit.Init();
    
	//所有下拉框添加choose样式
	jQuery('#userformSearch .chosen-select').chosen({width: '100%'});
});
