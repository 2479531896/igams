var listDepartment_TableInit = function () {
	var url="/systemmain/department/pagedataListDepartment";
	if($("#listDepartmentForm #fwbj").val()=="/ws"){
		url="/ws/production/department/pagedataListDepartment";
	}
    var oTableInit = new Object();
    // 初始化Table
    oTableInit.Init = function () {
        $('#listDepartmentForm #tb_list').bootstrapTable({
            url: $("#listDepartmentForm #urlPrefix").val() + url,         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#listDepartmentForm #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"jgid",					// 排序字段
            sortOrder: "asc",                   // 排序方式
            queryParams: oTableInit.queryParams,// 传递参数（*）
            sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       // 初始化加载第一页，默认第一页
            pageSize: 10,                       // 每页的记录行数（*）
            pageList: [10, 15, 20],        // 可供选择的每页的行数（*）
            paginationPreText: '‹',				// 指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			// 指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  // 是否显示所有的列
            showRefresh: true,                  // 是否显示刷新按钮
            minimumCountColumns: 2,             // 最少允许的列数
            clickToSelect: true,                // 是否启用点击选中行
            // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "jgid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '15%'
            }, {
				field: 'no',
				title: '序号',
				titleTooltip:'序号',
				width: '10%',
				align: 'left',
				visible: true,
				formatter:xhFormat
			}, {
				field: 'jgid',
				title: '机构ID',
				titleTooltip:'机构ID',
				width: '30%',
				align: 'left',
				visible: false
			}, {
				field: 'fjgid',
				title: '父机构ID',
				titleTooltip:'父机构ID',
				width: '30%',
				align: 'left',
				visible: false
			}, {
				field: 'jgmc',
				title: '机构名称',
				titleTooltip:'机构名称',
				width: '87%',
				align: 'left',
				visible: true
			}],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
          //点击每一个单选框时触发的操作
            onCheck:function(row){
            	var jgs=$("#listDepartmentForm #xzjgs").val();
            	var json_jgs=[];
            	if(jgs!="" && jgs!=null){
            		json_jgs=JSON.parse(jgs)
            	}
            	var sz={"jgmc":row.jgmc,"jgid":row.jgid};
            	json_jgs.push(sz);
            	$("#listDepartmentForm #xzjgs").val(JSON.stringify(json_jgs));
            	$("#listDepartmentForm  #t_xzjgs").tagsinput({
					itemValue: "jgid",
					itemText: "jgmc",
					})
					json=JSON.stringify(json_jgs);
					var jsonStr=eval('('+json+')');
					for (var i = 0; i < jsonStr.length; i++) {
						$("#listDepartmentForm  #t_xzjgs").tagsinput('add',jsonStr[i]);
					}
            },
            //取消每一个单选框时对应的操作；
            onUncheck:function(row){
            	var jgs=$("#listDepartmentForm #xzjgs").val();
            	var json_jgs=[];
            	if(jgs!="" && jgs!=null){
            		json_jgs=JSON.parse(jgs)
            	}
            	var deljg=[];
            	if(json_jgs.length>0){
            		for(var i=0;i<json_jgs.length;i++){
            			if(row.jgid==json_jgs[i].jgid){
            				deljg.push(json_jgs[i]);
            				json_jgs.splice(i,1);
            			}
            		}
            	}
            	$("#listDepartmentForm #xzjgs").val(JSON.stringify(json_jgs));
            	$("#listDepartmentForm #t_xzjgs").tagsinput({
					itemValue: "jgid",
					itemText: "jgmc",
					})
					json=JSON.stringify(deljg);
					var jsonStr=eval('('+json+')');
					for (var i = 0; i < jsonStr.length; i++) {
						$("#listDepartmentForm  #t_xzjgs").tagsinput('remove',jsonStr[i]);
					}
            },
            onDblClickRow: function (row, $element) {
            	return;
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
		var jgdm = $("#listDepartmentForm #jgdm").val();
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   // 页面大小
        	pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "jgid", // 防止同名排位用
            sortLastOrder: "asc", // 防止同名排位用
			jgdm:jgdm,
            // 搜索框使用
            // search:params.search
        };

    	var cxtj = $("#listDepartmentForm #cxtj").val();
    	// 查询条件
    	var cxnr = $.trim(jQuery('#listDepartmentForm #cxnr').val());
    	// '0':'机构名称',
    	if (cxtj == "0") {
    		map["jgmc"] = cxnr;
    	}
    	map["prefix"] = $("#listDepartmentForm #prefix").val();
    	
    	return map;
    };
    return oTableInit;
};

//序号格式化
function xhFormat(value, row, index) {
    //获取每页显示的数量
    var pageSize=$('#listDepartmentForm #tb_list').bootstrapTable('getOptions').pageSize;
    //获取当前是第几页
    var pageNumber=$('#listDepartmentForm #tb_list').bootstrapTable('getOptions').pageNumber;
    //返回序号，注意index是从0开始的，所以要加上1
    return pageSize * (pageNumber - 1) + index + 1;
}

var listDepartment_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
    	var btn_query = $("#listDepartmentForm #btn_query");

    	//绑定搜索发送功能
    	if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			searchListDepartmentResult(true);
    		});
    	}
    };

    return oInit;
};

function searchListDepartmentResult(isTurnBack){
	if(isTurnBack){
		$('#listDepartmentForm #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#listDepartmentForm #tb_list').bootstrapTable('refresh');
	}
	
}


$(function(){
    // 1.初始化Table
    var oTable = new listDepartment_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new listDepartment_ButtonInit();
    oButtonInit.Init();
    
	// 所有下拉框添加choose样式
	jQuery('#listDepartmentForm .chosen-select').chosen({width: '100%'});
});