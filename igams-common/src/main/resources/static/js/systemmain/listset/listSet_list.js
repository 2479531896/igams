var listSet_turnOff=true;

var listSet_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
    	$('#listSet_formSearch #tb_list').bootstrapTable({
            url: '/systemmain/list/pageGetListSet',  //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#listSet_formSearch #toolbar', //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName:"lbcx.ywid",					//排序字段
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
            uniqueId: "ywid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width: '4%'
            }, {
                field: 'zyid',
                title: '资源ID',
                width: '12%',
                align: 'left',
                visible: true
            }, {
                field: 'zybt',
                title: '资源标题',
                titleTooltip:'资源标题',
                width: '12%',
                align: 'left',
                visible: true
            }, {
                field: 'czmc',
                title: '操作名称',
                titleTooltip:'操作名称',
                width: '12%',
                align: 'left',
                visible: true
            }, {
                field: 'fjd',
                title: '父节点',
                titleTooltip:'父节点',
                width: '12%',
                align: 'left',
                visible: true
            }, {
                field: 'ywid',
                title: '业务ID',
                width: '12%',
                align: 'left',
                visible: true
            }, {
                field: 'ywmc',
                title: '业务名称',
                width: '12%',
                align: 'left',
                visible: true
            }, {
                field: 'glb',
                title: '关联表',
                titleTooltip:'关联表',
                width: '12%',
                align: 'left',
                visible: true
            }, {
                field: 'glzd',
                title: '关联字段',
                titleTooltip:'关联字段',
                width: '12%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	listSetDealById(row.ywid, 'view',$("#listSet_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#listSet_formSearch #tb_list").colResizable({
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
            sortLastName: "lbcx.ywid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
//            search:params.search
        };
        
    	var cxbt = $("#listSet_formSearch #cxbt").val();
    	// 查询条件
    	var cxnr = $.trim(jQuery('#listSet_formSearch #cxnr').val());
    	// '0':'业务名称';'1':'业务ID';'2':'关联表';
    	if (cxbt == "0") {
    		map["ywmc"] = cxnr;
    	}else if (cxbt == "1") {
    		map["ywid"] = cxnr;
    	}else if (cxbt == "2") {
    		map["glb"] = cxnr;
    	}
    	
    	return map;
    };
    return oTableInit;
};

//按钮动作函数
function listSetDealById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action =='view'){
		var url= tourl + "?ywid=" +id;
		$.showDialog(url,'查看列表',viewListSetConfig);
	}else if(action =='add'){
		var url= tourl;
		$.showDialog(url,'新增列表',addListSetConfig);
	}else if(action =='mod'){
		var url=tourl + "?ywid=" +id;
		$.showDialog(url,'编辑列表',addListSetConfig);
	}
}

var viewListSetConfig = {
	width		: "800px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var addListSetConfig = {
	width		: "1400px",
	modalName	: "addListSetModal",
	formName	: "listSet_ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#listSet_ajaxForm").valid()){
					return false;
				}
				
				var $this = this;
				var opts = $this["options"]||{};
				
				$("#listSet_ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchListSetResult();
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

var listSet_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
    	var btn_add = $("#listSet_formSearch #btn_add");
    	var btn_mod = $("#listSet_formSearch #btn_mod");
    	var btn_del = $("#listSet_formSearch #btn_del");
    	var btn_view = $("#listSet_formSearch #btn_view");
    	var btn_listSet_query = $("#btn_listSet_query");

    	//绑定搜索发送功能
    	if(btn_listSet_query != null){
    		btn_listSet_query.unbind("click").click(function(){
    			searchListSetResult(true);
    		});
    	}
    	btn_add.unbind("click").click(function(){
    		listSetDealById(null,"add",btn_add.attr("tourl"));
    	});
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#listSet_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			listSetDealById(sel_row[0].ywid,"mod",btn_mod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#listSet_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			listSetDealById(sel_row[0].ywid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#listSet_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].ywid;
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
    								searchListSetResult();
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

function searchListSetResult(isTurnBack){
	if(isTurnBack){
		$('#listSet_formSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#listSet_formSearch #tb_list').bootstrapTable('refresh');
	}
}

$(function(){

    //1.初始化Table
    var oTable = new listSet_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new listSet_ButtonInit();
    oButtonInit.Init();
    
	//所有下拉框添加choose样式
	jQuery('#listSet_formSearch .chosen-select').chosen({width: '100%'});
});
