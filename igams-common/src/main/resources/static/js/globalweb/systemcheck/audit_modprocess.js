var shxx_turnOff=true;
var shxx_TableInit = function () { 
	 var oTableInit = new Object();
	// 初始化Table
	oTableInit.Init = function () {
		$('#viewDiv #tb_list').bootstrapTable({
			url:$('#modDiv #urlPrefix').val()+'/systemcheck/auditProcess/auditList',         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#viewDiv #toolbar', // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     // 是否启用排序
			sortName:"shsj",					// 排序字段
			sortOrder: "desc",                   // 排序方式
			queryParams: oTableInit.queryParams,// 传递参数（*）
			sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
			pageNumber:1,                       // 初始化加载第一页，默认第一页
			pageSize: 15,                       // 每页的记录行数（*）
			pageList: [15, 30, 50, 100],        // 可供选择的每页的行数（*）
			paginationPreText: '‹',				// 指定分页条中上一页按钮的图标或文字,这里是<
			paginationNextText: '›',			// 指定分页条中下一页按钮的图标或文字,这里是>
			search: false,                      // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
			strictSearch: true,
			showColumns: false,                  // 是否显示所有的列
			showRefresh: false,                  // 是否显示刷新按钮
			minimumCountColumns: 2,             // 最少允许的列数
			clickToSelect: true,                // 是否启用点击选中行
			// height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId: "shxxid",                     // 每一行的唯一标识，一般为主键列
			showToggle:false,                    // 是否显示详细视图和列表视图的切换按钮                 								
			cardView: false,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			columns: [{
				'field': '',
			    'title': '序号',
			    'align': 'center',
			    'width': '5%',
			    'formatter': function (value, row, index) {
			    　　var options = $('#viewDiv #tb_list').bootstrapTable('getOptions'); 
			    　　return options.pageSize * (options.pageNumber - 1) + index + 1; 
			    }
			}, {				
				field: 'shrmc',
				title: '审核人',
				width: '13%',
				align: 'left',
				visible: true
			},{				
				field: 'gwmc',
				title: '审核岗位',
				width: '13%',
				align: 'left',
				visible: true
			},{				
				field: 'shsj',
				title: '审核时间',
				width: '20%',
				align: 'left',
				visible: true
			}, {				
				field: 'shyj',
				title: '审核意见',
				width: '25%',
				align: 'left',
				visible: true
			}, {
                title: '操作',
                align: 'center',
                width: '10%',
                formatter:modFormatter,
                visible: true
            }],
			onLoadSuccess: function () {
			},
			onLoadError: function () {
				alert("数据加载失败！");
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
        sortLastName: "shxxid", // 防止同名排位用
        sortLastOrder: "desc", // 防止同名排位用
        ywid:$("#viewDiv #ywid").val(),
        shlb:$("#viewDiv #shlb").val()
        
        // 搜索框使用
        // search:params.search
    };
    return shxxSearchData(map);
	};
	return oTableInit;
};

//操作按钮样式
function modFormatter(value, row, index) {
	var id = row.shxxid;
    var result = "<div class='col-sm-12 col-md-12 '><div class='row'><div class='btn-group'>";
    result += "<button class='btn btn-default col-md-6 col-sm-6' style='width: 66px;' type='button' title='审核编辑'  onclick=\"modview('" + id + "')\"><span class='glyphicon glyphicon-edit'  >编辑</span></button>";
    result += "</div></div></div>";
    return result;
}

function modview(id){
	$("#viewDiv").hide();
	$("#modDiv").show();
	var access =$("#ac_tk").val();
	$.ajax({
		type : 'post',
		url : $('#modDiv #urlPrefix').val()+'/systemcheck/auditProcess/modaudit',
		data : {
			"shxxid" : id,
			"access_token" : $("#ac_tk").val()
		},
		dataType : 'json',
		success:function(map){
	    	 $("#ajaxForm #shrmc").val(map.shrmc);
	    	 $("#ajaxForm #shsj").val(map.shsj);
	    	 $("#ajaxForm #shxxid").val(map.shxxid);
	    	 if(map.sftg!= null){
	    		 $("#ajaxForm #shyjDiv").show();
	    		 $("#ajaxForm #shyj").val(map.shyj);
	    	 }else{
	    		 $("#ajaxForm #shyjDiv").attr("style","display:none;");
	    	 }	    	 
	    }
	});
}
function returnview(){
	$("#modDiv").hide();
	$("#viewDiv").show();
}
//添加日期控件
laydate.render({
   elem: '#modDiv #shsj'
   ,type: 'datetime'
   ,ready: function(date){
	   	if(this.dateTime.hours==0&&this.dateTime.minutes==0&&this.dateTime.seconds==0){
	   		var myDate = new Date(); //实例一个时间对象；
			this.dateTime.hours=myDate.getHours();
			this.dateTime.minutes=myDate.getMinutes();
			this.dateTime.seconds=myDate.getSeconds();
			}
    	}
});

function insertmod(){
	//为空判断
	var shr = $("#ajaxForm #shrmc").val();
	var shsj = $("#ajaxForm #shsj").val();
	if(shr=="" || shsj=="" || shr==null || shsj==null){
		$.alert("请填写完整信息！")
		return false;
	}
	var access =$("#ac_tk").val();
	$.ajax({
		type : 'post',
		url : $('#modDiv #urlPrefix').val()+'/systemcheck/auditProcess/savemodaudit',
		cache : false,
		data : {
			"lrry" : $("#ajaxForm #shrmc").val(),
			"shsj" : $("#ajaxForm #shsj").val(),
			"shyj" : $("#ajaxForm #shyj").val(),
			"shxxid" : $("#ajaxForm #shxxid").val(),
			"access_token" : $("#ac_tk").val()
		},
		dataType : 'json',
		success:function(map){
	    	//返回值
	    	if(map.status=="success"){
	    		$("#ajaxForm #shrmc").val(""),
	    		$("#ajaxForm #shsj").val(""),
	    		$("#ajaxForm #shyj").val(""),
	    		$("#modDiv").hide();
	    		$("#viewDiv").show();
	    		$.success(map.message);
	    		shxxResult();
	    	}else if(map.status=="fail"){
	    		$.error(map.message);
	    	}	    	
	    }
	});
}

function shxxSearchData(map){
	return map;
}
function payDealById(id,htid,action,tourl){
	if(!tourl){
		return;
	}
}

var shxx_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
  	
    };

    return oInit;
};

function shxxResult(isTurnBack){
	if(isTurnBack){
		$('#viewDiv #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#viewDiv #tb_list').bootstrapTable('refresh');
	}
}

$(function(){
	//0.界面初始化
	// 1.初始化Table
	var oTable = new shxx_TableInit();
	oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new shxx_ButtonInit();
    oButtonInit.Init(); 
	// 所有下拉框添加choose样式
	jQuery('#viewDiv .chosen-select').chosen({width: '100%'});
	
});

