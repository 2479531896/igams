
var role_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#roleFormSearch #tb_list').bootstrapTable({
            url: '/systemrole/role/pageGetListRole',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#roleFormSearch #toolbar', //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName:"jsmc",					//排序字段
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
            uniqueId: "jsid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            }, {
                field: 'jsid',
                title: '角色ID',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'jsmc',
                title: '角色名称',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'fjsmc',
                title: '父角色名称',
                width: '15%',
                align: 'left',
                visible: true
            }, {
                field: 'sylxmc',
                title: '首页类型',
                width: '20%',
                align: 'left',
                visible: true
            },{
                field: 'dwxdbj',
                title: '单位限制',
                align: 'center',
                formatter:dwxdFormatter,
                visible: true
            },{
				field: 'ckqx',
				title: '仓库分类',
				align: 'center',
				visible: true
			},{
                title: '操作',
                align: 'center',
                width: '25%',
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
            	roleDealById(row.jsid, 'view',$("#roleFormSearch #btn_view").attr("tourl"));
            },
        });
        $("#roleFormSearch #tb_list").colResizable({
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
            sortLastName: "js.jsid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

    	var cxbt = $("#roleFormSearch #cxbt").val();
    	// 查询条件
    	var cxnr = $.trim(jQuery('#roleFormSearch #cxnr').val());
    	// '0':'角色名称'
    	if (cxbt == "0") {
    		map["jsmc"] = cxnr;
    	}
    	return map;
    };
    return oTableInit;
};

//单位限定
function dwxdFormatter(value, row, index) {
    if (row.dwxdbj == '1') {
        return '是';
    }
    else{
    	return "否";
    }
}

//操作按钮样式
function dealFormatter(value, row, index) {
	var id = row.jsid;
    var result = "<div class='col-sm-12 col-md-12 '><div class='row'><div class='btn-group'>";
    result += "<button class='btn btn-default col-md-6 col-sm-6' type='button' onclick=\"roleDealById('" + id + "', 'user','/systemrole/role/pagedataConfiguser')\" title='添加用户'><span class='glyphicon glyphicon-plus'>添加用户</span></button>";
    result += "<button class='btn btn-default col-md-6 col-sm-6' type='button' onclick=\"roleDealById('" + id + "','menu','/systemrole/role/pagedataSetMenu')\" title='设置菜单'><span class='glyphicon glyphicon-menu-hamburger'>设置菜单</span></button>";
    result += "</div></div></div>";
    result += "<div class='col-sm-12 col-md-12 '><div class='row'><div class='btn-group'>";
    result += "<button class='btn btn-default col-md-6 col-sm-6' type='button' onclick=\"roleDealById('" + id + "','minimenu','/systemrole/role/pagedataSetMiniMenu')\" title='钉钉菜单'><span class='glyphicon glyphicon-menu-hamburger'>钉钉菜单</span></button>";
    if(row.dwxdbj == '1'){
    	result +="<button class='btn btn-default col-md-6 col-sm-6' type='button' onclick=\"roleDealById('" + id + "','unit','/systemrole/role/pagedataSetUnit')\" title='单位设置'><span class='glyphicon glyphicon-menu-hamburger'>单位设置</span></button>" ;
    }else{
    	result +="<div class='col-sm-6 col-md-6'><span>　　　　 &nbsp;</span></div>" ;	
    }
    result += "</div></div></div>";
    return result;
}

var addRoleConfig = {
	width		: "600px",
	modalName	: "addRoleModal",
	formName	: "role_ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#role_ajaxForm").valid()){
					$.alert("请填写完整信息！")
					return false;
				}
				
				var $this = this;
				var opts = $this["options"]||{};
				
				$("#role_ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchRoleResult();
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

var saveTreeConfig = {
	width		: "600px",
	modalName : "saveTreeModal",
	buttons : {
		success : {
			label : "应用",
			className : "btn-primary",
			callback : function	() {
				//判断是否选择模块
				var mkzyid = $("#mk option:selected").val();
				//判断是钉钉还是OA菜单设置
				var formAction = $("#menutreeDiv #formAction").val();
				if("saveMenu"==formAction){
					if(mkzyid==null || mkzyid==""){
						$.error("请选择模块!");
						return false;
					}
				}

				//调用指定函数方法
				if(formAction){
					eval(formAction+"()"); 
				}
				//jQuery.closeModal("saveTreeModal");
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var saveTableConfig = {
	width		: "800px",
	modalName : "saveTreeModal",
	buttons : {
		success : {
			label : "确定",
			className : "btn-primary",
			callback : function	() {
				//调用指定函数方法
				var formAction = $("#menutreeDiv #formAction").val();
				if(formAction){
					eval(formAction+"()"); 
				}
				jQuery.closeModal("saveTreeModal");
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};


var StoreHouseSetConfig = {
	width: "800px",
	modalName: "StoreHouseSetModal",
	buttons: {
		success: {
			label: "确定",
			className: "btn-primary",
			callback: function () {
				var $this = this;
				var opts = $this["options"] || {};

				$("#storehouseSet_ajaxForm input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"] || "storehouseSet_ajaxForm", function (responseText, statusText) {
					if (responseText["status"] == 'success') {
						$.success(responseText["message"], function () {
							if (opts.offAtOnce) {
								$.closeModal(opts.modalName);
								searchRoleResult();
							}
						});
					} else if (responseText["status"] == "fail") {
						preventResubmitForm(".modal-footer > button", false);
						$.error(responseText["message"], function () {
						});
					} else {
						$.alert(responseText["message"], function () {
						});
					}
				}, ".modal-footer > button");
				return false;
			}
		},
		cancel: {
			label: "关 闭",
			className: "btn-default"
		}
	}
};

var SystemConfig = {
		width		: "800px",
		modalName : "SystemModal",
		buttons : {
			success : {
				label : "确定",
				className : "btn-primary",
				callback : function	() {
					var $this = this;
					var opts = $this["options"]||{};
					
					$("#system_ajaxForm input[name='access_token']").val($("#ac_tk").val());
					
					submitForm(opts["formName"]||"system_ajaxForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
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



var saveUnitConfig = {
		width		: "800px",
		modalName : "saveUnitModal",
		buttons : {
			success : {
				label : "确定",
				className : "btn-primary",
				callback : function	() {
					//调用指定函数方法
//					var formAction = $("#menutreeDiv #formAction").val();
//					if(formAction){
//						eval(formAction+"()"); 
//					}
//					jQuery.closeModal("saveUnitModal");
//					return false;
				}
			},
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

var viewRoleConfig = {
	width		: "600px",
	modalName	: "viewRoleModal",
	formName	: "viewRole_ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
var fieldSetConfig = {
		width		: "800px",
		modalName	: "fieldSetModal",
		formName	: "setting_ajaxForm",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#setting_ajaxForm").valid()){
						$.alert("请填写完整信息！")
						return false;
					}
					
					var $this = this;
					var opts = $this["options"]||{};
					
					$("#setting_ajaxForm input[name='access_token']").val($("#ac_tk").val());
					
					submitForm(opts["formName"]||"setting_ajaxForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
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

var DetectionUnitConfig = {
		width		: "800px",
		modalName : "detectionUnitModal",
		buttons : {
			success : {
				label : "确定",
				className : "btn-primary",
				callback : function	() {
					var $this = this;
					var opts = $this["options"]||{};
					
					$("#detectionunit_ajaxForm input[name='access_token']").val($("#ac_tk").val());
					
					submitForm(opts["formName"]||"detectionunit_ajaxForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
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

/**
 * 菜单保存方法
 */
function saveMenu(){
	var menuArray = $('#menutreeDiv #menuTree').jstree().get_bottom_selected();	
	var url = '/systemrole/role/pagedataMenuSaveTree';
	var map = {};
	map["menuArray"] = JSON.stringify(menuArray);
	map["jsid"] = jQuery("#menutreeDiv #jsid").val();
	map["access_token"] = $("#ac_tk").val();
	map["zyid"] = $("#mk option:selected").val();//获取当前选择项的值.
	map["fbsqz"] = $("#zt").find("option:selected").attr("cskz2");//获取当前选择项的分布式标记.
	jQuery.ajaxSetup( {
		async : false
	});
	jQuery.post(url, map, function(data) {
		$.success(data.message);
	}, 'json');
	jQuery.ajaxSetup( {
		async : true
	});
}

/**
 * 小程序菜单保存
 * @returns
 */
function saveMiniMenu(){
	var menuArray = $('#menutreeDiv #menuTree').jstree().get_selected();
	var url = '/systemrole/role/pagedataMinimenuSaveTree';
	var map = {};
	map["menuArray"] = JSON.stringify(menuArray);
	map["jsid"] = jQuery("#menutreeDiv #jsid").val();
	map["access_token"] = $("#ac_tk").val();
	jQuery.ajaxSetup( {
		async : false
	});
	jQuery.post(url, map, function(data) {
		$.success(data.message);
	}, 'json');
	jQuery.ajaxSetup( {
		async : true
	});
}

//按钮动作函数
function roleDealById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action =='view'){
		var url= tourl + "?jsid=" +id;
		$.showDialog(url,'查看角色',viewRoleConfig);
	}else if(action =='add'){
		var url=tourl;
		$.showDialog(url,'新增角色',addRoleConfig);
	}else if(action =='mod'){
		var url= tourl + "?jsid=" +id;
		$.showDialog(url,'编辑角色',addRoleConfig);
	}else if(action =='user'){
		var url= tourl + "?jsid=" +id;
		$.showDialog(url,'添加用户',saveTableConfig);
	}else if(action =='menu'){
		var url= tourl + "?jsid=" +id + "&formAction=saveMenu";
		$.showDialog(url,'设置菜单',saveTreeConfig);
	}else if(action =='unit'){
		var url= tourl + "?jsid=" +id;
		$.showDialog(url,'单位设置',saveUnitConfig);
	}else if(action=="setting"){
		var url= tourl + "?ids=" +id;
		$.showDialog(url,'字段设置',fieldSetConfig);
	}else if(action =='minimenu'){
		var url= tourl + "?jsid=" +id + "&formAction=saveMiniMenu";
		$.showDialog(url,'设置菜单',saveTreeConfig);
	}else if(action =='detectionunit'){
		var url= tourl + "?jsid=" +id;
		$.showDialog(url,'检测单位设置',DetectionUnitConfig);
	}else if(action =='system'){
		var url= tourl + "?jsid=" +id;
		$.showDialog(url,'系统设置',SystemConfig);
	}else if (action == 'storehouseset') {
		var url = tourl + "?jsid=" + id;
		$.showDialog(url, '仓库分类设置', StoreHouseSetConfig);
	}else if(action =='copy'){
		var url= tourl + "?jsid=" +id;
		$.showDialog(url,'复制角色',addRoleConfig);
	}
}


var role_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
    	var btn_add = $("#roleFormSearch #btn_add");
    	var btn_mod = $("#roleFormSearch #btn_mod");
    	var btn_del = $("#roleFormSearch #btn_del");
    	var btn_view = $("#roleFormSearch #btn_view");
    	var btn_query = $("#roleFormSearch #btn_query");
    	var btn_setting= $("#roleFormSearch #btn_setting")
    	var btn_detectionunit=$("#roleFormSearch #btn_detectionunit");
    	var btn_system=$("#roleFormSearch #btn_system");
		let btn_storehouseset = $("#roleFormSearch #btn_storehouseset");
		var btn_copy = $("#roleFormSearch #btn_copy");

		//复制按钮
		btn_copy.unbind("click").click(function(){
			var sel_row = $('#roleFormSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				roleDealById(sel_row[0].jsid,"copy",btn_copy.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		btn_storehouseset.unbind("click").click(function () {
			var sel_row = $('#roleFormSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
			if (sel_row.length == 1) {
				roleDealById(sel_row[0].jsid, "storehouseset", btn_storehouseset.attr("tourl"));
			} else {
				$.error("请选中一行");
			}
		});
    	//绑定搜索发送功能
    	if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			searchRoleResult(true);
    		});
    	}
    	btn_add.unbind("click").click(function(){
    		roleDealById(null,"add",btn_add.attr("tourl"));
    	});
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#roleFormSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			roleDealById(sel_row[0].jsid,"mod",btn_mod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#roleFormSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			roleDealById(sel_row[0].jsid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#roleFormSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].jsid;
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
    								searchRoleResult();
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
    	//字段设置
    	btn_setting.unbind("click").click(function(){
    		var sel_row = $('#roleFormSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].jsid;
    		}
    		ids = ids.substr(1);
    		if(ids.length>0){
    			roleDealById(ids,"setting",btn_setting.attr("tourl"));
    		}
    	});
    	
    	btn_detectionunit.unbind("click").click(function(){
    		var sel_row = $('#roleFormSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			if(sel_row[0].dwxdbj=="1"){
    				roleDealById(sel_row[0].jsid,"detectionunit",btn_detectionunit.attr("tourl"));
    			}else{
    				$.error("当前角色没有进行单位限制，请先进行单位限制！");
    			}
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	
    	btn_system.unbind("click").click(function(){
    		var sel_row = $('#roleFormSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    				roleDealById(sel_row[0].jsid,"system",btn_system.attr("tourl")); 			
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	
    	
    };

    return oInit;
};

function searchRoleResult(isTurnBack){
	if(isTurnBack){
		$('#roleFormSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#roleFormSearch #tb_list').bootstrapTable('refresh');
	}
}

$(function(){

    //1.初始化Table
    var oTable = new role_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new role_ButtonInit();
    oButtonInit.Init();
    
	//所有下拉框添加choose样式
	jQuery('#roleFormSearch .chosen-select').chosen({width: '100%'});
});
