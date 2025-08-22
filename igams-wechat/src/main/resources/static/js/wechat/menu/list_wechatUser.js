var userListWx_TableInit = function () {
    var oTableInit = new Object();
    // 初始化Table
    oTableInit.Init = function () {
        $('#userListWxForm #tb_list').bootstrapTable({
            url: '/wechat/user/pagedataListWechatUserPage',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#userListWxForm #toolbar', // 工具按钮用哪个容器
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
                checkbox: true,
                width: '5%'
            }, {
				field: 'no',
				title: '序号',
				titleTooltip:'序号',
				width: '8%',
				align: 'left',
				visible: true,
				formatter:xhFormat
			},{
                field: 'wxm',
                title: '微信名',
                width: '23%',
                align: 'left',
                visible: true
            },{
                field: 'gzpt',
                title: '关注平台',
                width: '20%',
                align: 'left',
                visible: true
            },{
                field: 'yhm',
                title: '用户名',
                width: '20%',
                align: 'left',
                visible: true
            },{
                field: 'yhxb',
                title: '性别',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'yhcs',
                title: '城市',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'zsxm',
                title: '真实姓名',
                width: '24%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	var wxid = row.wxid;
            	var wxm = row.wxm;
            	$("#userListWxForm  #yxyh").tagsinput({
        			itemValue: "value",
        			itemText: "text",
        		})
        		$("#userListWxForm  #yxyh").tagsinput('add',{"value":wxid,"text":wxm});
            	return;
            },
            onCheck:function(row){
                var wxid = row.wxid;
                var wxm = row.wxm;
                $("#userListWxForm  #yxyh").tagsinput({
                    itemValue: "value",
                    itemText: "text",
                })
                $("#userListWxForm  #yxyh").tagsinput('add',{"value":wxid,"text":wxm});
                return;
            },
            //取消每一个单选框时对应的操作；
            onUncheck:function(row){
                var wxid = row.wxid;
                var wxm = row.wxm;
                $("#userListWxForm  #yxyh").tagsinput({
                    itemValue: "value",
                    itemText: "text",
                })
                $("#userListWxForm  #yxyh").tagsinput('remove',{"value":wxid,"text":wxm});
                return;
            }
        });
       /* $("#userListWxForm #tb_list").colResizable({
            liveDrag:true, 
            gripInnerHtml:"<div class='grip'></div>", 
            draggingClass:"dragging", 
            resizeMode:'fit', 
            postbackSafe:true,
            partialRefresh:true}        
        );*/
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
                sortLastName: "wxid", // 防止同名排位用
                sortLastOrder: "asc" // 防止同名排位用
                // 搜索框使用
                // search:params.search
            };

    	var cxtj = $("#userListWxForm #cxtj").val();
    	// 查询条件
    	var cxnr = $.trim(jQuery('#userListWxForm #cxnr').val());
    	// '0':'真实姓名','1':'岗位名称',
    	if(cxtj=="0"){
    		map["yhm"]=cxnr
    	}else if(cxtj=="1"){
    		map["zsxm"]=cxnr
    	}else if(cxtj=="2"){
    		map["wxm"]=cxnr
    	}else if(cxtj=="3"){
    		map["gzpt"]=cxnr
    	}
    	
    	return map;
    };
    return oTableInit;
};

//序号格式化
function xhFormat(value, row, index) {
    //获取每页显示的数量
    var pageSize=$('#userListWxForm #tb_list').bootstrapTable('getOptions').pageSize;
    //获取当前是第几页
    var pageNumber=$('#userListWxForm #tb_list').bootstrapTable('getOptions').pageNumber;
    //返回序号，注意index是从0开始的，所以要加上1
    return pageSize * (pageNumber - 1) + index + 1;
}

var userListWx_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
    	var btn_query = $("#userListWxForm #btn_query");

    	//绑定搜索发送功能
    	if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			searchWxUserListResult();
    		});
    	}
    };

    return oInit;
};

function searchWxUserListResult(){
	$('#userListWxForm #tb_list').bootstrapTable('refresh',{pageNumber:1});
}

/**
 * 选择微信用户 
 */
function comfirm(){
	var sel_row = $('#userListWxForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
	if(sel_row.length > 0){
		$("#userListWxForm  #yxyh").tagsinput({
			itemValue: "value",
			itemText: "text",
		})
		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
			var wxid = sel_row[i].wxid;
			var wxm = sel_row[i].wxm;
			$("#userListWxForm  #yxyh").tagsinput('add',{"value":wxid,"text":wxm});
		}
	}else{
		$.error("请至少选中一行");
		return;
	}
}


$(function(){
    // 1.初始化Table
    var oTable = new userListWx_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new userListWx_ButtonInit();
    oButtonInit.Init();
    
	// 所有下拉框添加choose样式
	jQuery('#userListWxForm .chosen-select').chosen({width: '100%'});
});