var auditPost_turnOff=true;

var auditPost_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#auditPost_formSearch #tb_list').bootstrapTable({
            url: $("#auditPost_formSearch #urlPrefix").val()+'/systemmain/audit/pageGetListAuditPost',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#auditPost_formSearch #toolbar', //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName:"gw.gwid",					//排序字段
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
            uniqueId: "gwid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
				width: '4%'
            }, {
                field: 'gwid',
                title: '岗位ID',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'gwmc',
                title: '岗位名称',
                titleTooltip:'岗位名称',
                width: '15%',
                align: 'left',
                visible: true
            }, {
                field: 'sfywjs',
                title: '是否业务角色',
                titleTooltip:'是否业务角色',
                width: '17%',
                align: 'left',
                formatter:jsFormatter,
                visible: true
            }, {
                field: 'ywjs',
                title: '业务角色',
                titleTooltip:'业务角色',
                width: '17%',
                align: 'left',
                visible: true
            }, {
                field: 'wbgw',
                title: '外部岗位',
                titleTooltip:'外部岗位',
                width: '15%',
                align: 'left',
                visible: true
            }, {
                field: 'sfgb',
                title: '是否广播',
                titleTooltip:'是否广播',
                width: '7%',
                align: 'center',
                formatter:sfgbFormatter,
                visible: true
            }, {
				field: 'dwxz',
				title: '单位限制',
				titleTooltip:'单位限制',
				width: '7%',
				align: 'center',
				formatter:dwxzFormatter,
				visible: true
			}, {
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
            	auditPostDealById(row.gwid, 'view',$("#auditPost_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#auditPost_formSearch #tb_list").colResizable({
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
            sortLastName: "gw.gwid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

    	var cxbt = $("#auditPost_formSearch #cxbt").val();
    	// 查询条件
    	var cxnr = $.trim(jQuery('#auditPost_formSearch #cxnr').val());
    	// '0':'岗位名称','1':'外部岗位',
    	if (cxbt == "0") {
    		map["gwmc"] = cxnr;
    	}else if (cxbt == "1") {
    		map["wbgw"] = cxnr;
    	}else if (cxbt == "2") {
			map["entire"] = cxnr;
		}
    	
    	return map;
    };
    return oTableInit;
};

//是否业务角色
function jsFormatter(value, row, index) {
    if (row.sfywjs == '0') {
        return "否";
    }
    else {
    	return "是";
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
//是否单位限制
function dwxzFormatter(value,row,index) {
	if (row.dwxz =='1'){
		return "是";
	}else if (row.dwxz =='0'){
		return "否";
	}
}
//操作按钮样式
function dealFormatter(value, row, index) {
	var id = row.gwid;
    var result = "<div class='btn-group'>";
    result += "<button class='btn btn-default' type='button' onclick=\"auditPostDealById('" + id + "', 'addMember','/systemmain/audit/addAuditPostMember')\" title='添加成员'><span class='glyphicon glyphicon-plus'>添加成员</span></button>";
    result += "</div>";
    
    return result;
}
//按钮动作函数
function auditPostDealById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl = $("#auditPost_formSearch #urlPrefix").val()+tourl;
	if(action =='view'){
		var url= tourl + "?gwid=" +id;
		$.showDialog(url,'查看岗位',viewAuditPostConfig);
	}else if(action =='add'){
		var url= tourl;
		$.showDialog(url,'新增岗位',addAuditPostConfig);
	}else if(action =='mod'){
		var url=tourl + "?gwid=" +id;
		$.showDialog(url,'编辑岗位',addAuditPostConfig);
	}else if(action =='addMember'){
		var url= tourl + "?gwid=" +id;
		$.showDialog(url,'添加成员',addAuditPostMemberConfig);
	}
}

var addAuditPostMemberConfig = {
	width		: "900px",
	modalName : "addAuditPostMemberModel",
	buttons : {
		success : {
			label : "确定",
			className : "btn-primary",
			callback : function	() {
				jQuery.closeModal("addAuditPostMemberModel");
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var viewAuditPostConfig = {
	width		: "800px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var addAuditPostConfig = {
	width		: "600px",
	modalName	: "addAuditPostModal",
	formName	: "auditPost_ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				
				if(!$("#auditPost_ajaxForm").valid()){
					return false;
				}
				
				var $this = this;
				var opts = $this["options"]||{};
				
				$("#auditPost_ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchAuditPostResult();
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

var auditPost_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
    	var btn_add = $("#auditPost_formSearch #btn_add");
    	var btn_mod = $("#auditPost_formSearch #btn_mod");
    	var btn_del = $("#auditPost_formSearch #btn_del");
    	var btn_view = $("#auditPost_formSearch #btn_view");
    	var btn_auditPost_query = $("#btn_auditPost_query");

    	//绑定搜索发送功能
    	if(btn_auditPost_query != null){
    		btn_auditPost_query.unbind("click").click(function(){
    			searchAuditPostResult(true);
    		});
    	}
    	btn_add.unbind("click").click(function(){
    		auditPostDealById(null,"add",btn_add.attr("tourl"));
    	});
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#auditPost_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			auditPostDealById(sel_row[0].gwid,"mod",btn_mod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#auditPost_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			auditPostDealById(sel_row[0].gwid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#auditPost_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].gwid;
    		}
    		ids = ids.substr(1);
    		$.confirm('您确定要删除所选择的记录吗？',function(result){
    			if(result){
    				jQuery.ajaxSetup({async:false});
    				var url= $("#auditPost_formSearch #urlPrefix").val()+btn_del.attr("tourl");
    				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
    					setTimeout(function(){
    						if(responseText["status"] == 'success'){
    							$.success(responseText["message"],function() {
    								searchAuditPostResult();
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

function searchAuditPostResult(isTurnBack){
	if(isTurnBack){
		$('#auditPost_formSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#auditPost_formSearch #tb_list').bootstrapTable('refresh');
	}
}

$(function(){

    //1.初始化Table
    var oTable = new auditPost_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new auditPost_ButtonInit();
    oButtonInit.Init();
    
	//所有下拉框添加choose样式
	jQuery('#auditPost_formSearch .chosen-select').chosen({width: '100%'});
});
