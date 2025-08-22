
var impSample_TableInit = function (fjid) {
	var oTableInit = new Object();
	//初始化Table
	oTableInit.Init = function () {
		$('#sampImportForm #tb_list').bootstrapTable({
			url: '/common/file/listImpInfo',		 //请求后台的URL（*）
			method: 'get',					  //请求方式（*）
			toolbar: '#sampImportForm #toolbar', //工具按钮用哪个容器
			striped: true,					  //是否显示行间隔色
			cache: false,					   //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,				   //是否显示分页（*）
			sortable: true,					 //是否启用排序
			sortName:"lrsj",					//排序字段
			sortOrder: "desc",				   //排序方式
			queryParams: oTableInit.queryParams,//传递参数（*）
			sidePagination: "server",		   //分页方式：client客户端分页，server服务端分页（*）
			pageNumber:1,					   //初始化加载第一页，默认第一页
			pageSize: 999,					  //每页的记录行数（*）
			pageList: [999, 1999],				//可供选择的每页的行数（*）
			paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
			paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
			search: false,					  //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
			strictSearch: true,
			showColumns: false,				  //是否显示所有的列
			showRefresh: false,				  //是否显示刷新按钮
			minimumCountColumns: 2,			 //最少允许的列数
			clickToSelect: false,				//是否启用点击选中行
			//height: 500,						//行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId: "fjid",					 //每一行的唯一标识，一般为主键列
			showToggle:false,					//是否显示详细视图和列表视图的切换按钮
			cardView: false,					//是否显示详细视图
			detailView: false,				   //是否显示父子表
			columns: [{
                field: 'yblx',
                title: '标本类型',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'ybbh',
                title: '标本编号',
                width: '18%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'hzh',
                title: '盒子号',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'chth',
                title: '抽屉号',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'bxh',
                title: '冰箱号',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'hqsj',
                title: '标本获取时间',
                width: '8%',
                align: 'right',
                visible: false
            },{
                field: 'ysl',
                title: '原数量',
                width: '8%',
                align: 'right',
                visible: false
            },{
                field: 'sl',
                title: '数量',
                width: '8%',
                align: 'right',
                visible: true
            },{
                field: 'yds',
                title: '预定数',
                width: '8%',
                align: 'right',
                visible: true
            },{
                field: 'qswz',
                title: '起始位置',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'jswz',
                title: '结束位置',
                width: '6%',
                align: 'right',
                visible: true
            },{
                field: 'dw',
                title: '单位',
                align: 'right',
                sortable: true,
                visible: true
            },{
                field: 'nd',
                title: '浓度',
                width: '6%',
                align: 'left',
                visible: true
            }, {
                field: 'yblylx',
                title: '标本来源类型',
                width: '6%',
                align: 'left',
                visible: true,
            }, {
                field: 'rklx',
                title: '入库类型',
                width: '6%',
                align: 'left',
                visible: true,
            }, {
                field: 'wbbh',
                title: '外部编号',
                width: '6%',
                align: 'left',
                visible: true,
            }, {
                field: 'jyjg',
                title: '检验结果',
                width: '6%',
                align: 'left',
                visible: true,
            }, {
                field: 'lyd',
                title: '来源地',
                width: '6%',
                align: 'left',
                visible: true,
            }, {
                field: 'cysj',
                title: '采样时间',
                width: '6%',
                align: 'left',
                visible: true,
            }, {
                field: 'xb',
                title: '性别',
                width: '6%',
                align: 'left',
                visible: true,
            }, {
                field: 'yz',
                title: '孕周',
                width: '6%',
                align: 'left',
                visible: true,
            }, {
                field: 'tz',
                title: '体重',
                width: '6%',
                align: 'left',
                visible: true,
            }, {
                field: 'xm',
                title: '姓名',
                width: '6%',
                align: 'left',
                visible: true,
            }, {
                field: 'bz',
                title: '备注',
                width: '6%',
                align: 'left',
                visible: true,
            }, {
                field: 'lybh',
                title: '来源编号',
                width: '6%',
                align: 'left',
                visible: true,
            }],
			onLoadSuccess: function () {
			},
			onLoadError: function () {
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
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
			access_token: $("#ac_tk").val(),
			fjid: fjid,
			ywlx: $("#sampImportForm #ywlx").val(),
		};

		return map;
	};
	return oTableInit;
};


//信息显示配置
var showConfig = function(errormsg,infomsg,confirmmsg,warnmsg,successmsg,updList){
	var formname = "#sampImportForm";
	$(formname + " #errordiv").addClass("hidden");
	$(formname + " #infodiv").addClass("hidden");
	$(formname + " #confirmdiv").addClass("hidden");
	$(formname + " #warndiv").addClass("hidden");
	$(formname + " #btn-cancel").addClass("hidden");
	$(formname + " #btn-continue").addClass("hidden");
	$(formname + " #btn-complete").addClass("hidden");

	if(errormsg){
		$(formname + " #errormsg").html(errormsg);	
		$(formname + " #errordiv").removeClass("hidden");
		$(formname + " #btn-complete").removeClass("hidden");
		return ;
	}else{
		var existsMsg = false;
		if(infomsg){
			$(formname + " #infomsg").html(infomsg);	
			$(formname + " #infodiv").removeClass("hidden");
			$(formname + " #btn-continue").addClass("hidden");
			$(formname + " #btn-complete").addClass("hidden");
			existsMsg = true;
		}
		if(confirmmsg){
			$(formname + " #confirmmsg").html(confirmmsg);	
			$(formname + " #confirmdiv").removeClass("hidden");
			$(formname + " #btn-cancel").removeClass("hidden");
			$(formname + " #btn-continue").removeClass("hidden");
			existsMsg = true;
		}
		if(warnmsg){
			$(formname + " #warnmsg").html(warnmsg);	
			$(formname + " #warndiv").removeClass("hidden");
			$(formname + " #btn-cancel").removeClass("hidden");
			$(formname + " #btn-continue").removeClass("hidden");
			existsMsg = true;
		}
		if(successmsg){
			$(formname + " #infomsg").html(successmsg);	
			$(formname + " #infodiv").removeClass("hidden");
			$(formname + " #btn-complete").removeClass("hidden");
			existsMsg = true;
		}

		if (!existsMsg) {
			$(formname + " #infomsg").html("数据检查没有问题，请点击继续按钮继续或点击取消按钮取消上传。");	
			$(formname + " #infodiv").removeClass("hidden");
			$(formname + " #btn-cancel").removeClass("hidden");
			$(formname + " #btn-continue").removeClass("hidden");
		}
	}
}
//上传后自动提交服务器检查进度
var checkImpCheckStatus = function(intervalTime,fjid){
	var formname = "#sampImportForm";
	$.ajax({
		type : "POST",
		url : "/common/file/checkImpInfo",
		data : {"fjid":fjid,"access_token":$("#ac_tk").val()},
		dataType : "json",
		success:function(data){
			if(data.endflg==1){
				if(data.errormsg && data.errormsg!=""){
					$(formname +" #errormsg").html(data.errormsg);
					showConfig(data.errormsg);
				}else if(data.warnmsg && data.warnmsg!=""){
					$(formname +" #warnmsg").html(data.warnmsg);
					showConfig(data.warnmsg);
				}else if(data.confirmmsg && data.confirmmsg!=""){
					$(formname +" #confirmmsg").html(data.confirmmsg);
					showConfig(data.confirmmsg);
					$(formname +" #tb_list").bootstrapTable('refresh');
				}else if(data.infomsg && data.infomsg!=""){
					$(formname +"#infomsg").html(data.infomsg);
					showConfig(data.infomsg);
					//1.初始化Table
					var oTable = new impSample_TableInit();
					oTable.Init();
				}else{
					showConfig(data.infomsg);
					//1.初始化Table
					var oTable = new impSample_TableInit(fjid);
					oTable.Init();
				}
			}else{
				if(data.errormsg && data.errormsg!=""){
					$(formname +" #errormsg").html(data.errormsg);
					showConfig(data.errormsg);
				}
				else{
					if(intervalTime < 2000)
						intervalTime = intervalTime + 1000;
					if($(formname +" #infomsg")){$(formname +" #infomsg").html(data.msg)}
					setTimeout("checkImpCheckStatus("+ intervalTime +",'"+fjid+"')",intervalTime)
				}
			}
		}
	});
};

//自动检查服务器插入数据库进度
var checkImpInsertStatus = function(intervalTime,fjid){
	var formname = "#sampImportForm";
	$.ajax({
		type : "POST",
		url : "/common/file/checkImpSave",
		data : {"fjid":fjid,"access_token":$("#ac_tk").val()},
		dataType : "json",
		success:function(data){
			if(data.insertEndflg==1){
				if($(formname +" #infomsg")){$(formname +" #infomsg").html(data.insertMsg)}
				$(formname + " #infodiv").removeClass("hidden");
				$(formname + " #btn-complete").removeClass("hidden");
			}else{
				if(intervalTime < 2000)
					intervalTime = intervalTime + 1000;
				if($(formname +" #infomsg")){$(formname +" #infomsg").html(data.insertMsg)}
				$(formname + " #infodiv").removeClass("hidden");
				setTimeout("checkImpInsertStatus("+ intervalTime +",'"+fjid+"')",intervalTime)
			}
		}
	});
};

var sampleImp_ButtonInit = function(){
    var oInits = new Object();
    var formname ="#sampImportForm";
    oInits.Init = function () {
    	//取消按钮
    	$(formname + " #btn-cancel").unbind("click").click(function(){
    		$(formname + " #samp_file")
				.fileinput('clear')
				.fileinput('unlock');
			$(formname + " #samp_file")
				.parent()
				.siblings('.fileinput-remove')
				.hide();
			$('#sampImportForm #update_table').html('<table id="tb_list" class="table table-hover table-bordered"></table>');
			$(formname + " #errordiv").addClass("hidden");
			$(formname + " #infodiv").addClass("hidden");
			$(formname + " #confirmdiv").addClass("hidden");
			$(formname + " #warndiv").addClass("hidden");
			$(formname + " #btn-cancel").addClass("hidden");
			$(formname + " #btn-continue").addClass("hidden");
			$(formname + " #btn-complete").addClass("hidden");
    	});
    	//继续
    	$(formname + " #btn-continue").unbind("click").click(function(){
			$(formname + " #errordiv").addClass("hidden");
			$(formname + " #warndiv").addClass("hidden");
			$(formname + " #confirmdiv").addClass("hidden");
			$(formname + " #infodiv").addClass("hidden");
			$(formname + " #btn-cancel").addClass("hidden");
			$(formname + " #btn-continue").addClass("hidden");
			$(formname + " #btn-complete").addClass("hidden");
			var s_fjid = $("#sampImportForm #fjid").val();
    		$.ajax({
    			type : "POST",
    			url : "/common/file/saveImportRecord",
    			data : {"fjid":s_fjid,"access_token":$("#ac_tk").val(),ywlx: $("#sampImportForm #ywlx").val()},
    			dataType : "json",
    			success:function(data){
    				if(data.status == "fail")
    				{
    					$.alert("服务器异常,请稍后再次尝试。错误信息：" + data.msg);
    					$(formname + " #btn-cancel").removeClass("hidden");
    					$(formname + " #btn-continue").removeClass("hidden");
    				}else{
        				setTimeout("checkImpInsertStatus(2000,'"+ s_fjid + "')",1000);
    				}
    			}
    		});
    	});
    	
    	//完成
    	$(formname + " #btn-complete").unbind("click").click(function(){
    		$(formname + " #samp_file")
			.fileinput('clear')
			.fileinput('unlock');
			$(formname + " #samp_file")
				.parent()
				.siblings('.fileinput-remove')
				.hide();
			$('#sampImportForm #update_table').html('<table id="tb_list" class="table table-hover table-bordered"></table>');
			$(formname + " #errordiv").addClass("hidden");
			$(formname + " #infodiv").addClass("hidden");
			$(formname + " #confirmdiv").addClass("hidden");
			$(formname + " #warndiv").addClass("hidden");
			$(formname + " #btn-cancel").addClass("hidden");
			$(formname + " #btn-continue").addClass("hidden");
			$(formname + " #btn-complete").addClass("hidden");
			
    	});
    	
    	
    	//下载模板按钮
    	$(formname + " #template_dw").unbind("click").click(function(){
    		$("#updateSampleForm #access_token").val($("#ac_tk").val());
    		$("#updateSampleForm").submit();
    	});
    }
    return oInits;
}

function displayUpInfo(fjid){
	$("#sampImportForm #fjid").val(fjid);
	//1.初始化Table
	setTimeout("checkImpCheckStatus(2000,'"+ fjid + "')",1000);
}

$(function () {
	//0.初始化fileinput,这是公用方法，调用comdefine.js里的方法
	var oFileInput = new FileInput();
	oFileInput.Init("sampImportForm","displayUpInfo",1,1,"samp_file");
	//2.初始化Button的点击事件
    var oimpButtonInit = new sampleImp_ButtonInit();
	oimpButtonInit.Init();
});