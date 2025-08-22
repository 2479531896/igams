var SampleSum_turnOff=true;

var SampleSum_TableInit=function(){
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#samplesum_formSearch #samplesum_list").bootstrapTable({
			url: '/inspection/samplenum/pageGetListSamplenum',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#samplesum_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "cwgl.cwsj",				//排序字段
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
            uniqueId: "cwid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            /*rowStyle:rowStyle,                  //通过自定义函数设置行样式
*/          columns: [{
                checkbox: true,
                width: '4%'
            },{
            	title: '序号',
				formatter: function (value, row, index) {
					return index+1;
				},
				titleTooltip:'序号',
				width: '4%',
                align: 'left',
                visible:true
            },{
                field: 'cwid',
                title: '错误id',
                width: '10%',
                align: 'left',
                sortable: true,   
                visible:false
            },{
                field: 'cwlx',
                title: '错误类型',
                width: '22%',
                align: 'left',
                visible: true
            },{
                field: 'cwsj',
                title: '错误时间',
                width: '12%',
                align: 'left',
                visible: true
            },{
                field: 'ysnr',
                title: '原始内容',
                width: '12%',
                align: 'left',
                sortable: true,   
                visible: true
            },{
                field: 'sfcl',
                title: '是否处理',
                width: '12%',
                align: 'left',
                visible: true
            },{
                field: 'clsj',
                title: '处理时间',
                width: '12%',
                align: 'left',
                visible: true
            },{
                field: 'zt',
                title: '状态',
                width: '12%',
                formatter:yblshFormat,
                align: 'left',
                visible: true
            }, {
				field: 'cz',
				title: '操作',
				titleTooltip:'操作',
				width: '10%',
				align: 'left',
				formatter:czFormat,
				visible: true
			}],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	SampleSum_DealById(row.cwid,'view',$("#samplesum_formSearch #btn_view").attr("tourl"));
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
	            sortLastName: "cwgl.cwid", // 防止同名排位用
	            sortLastOrder: "asc" // 防止同名排位用
	            // 搜索框使用
	            // search:params.search
	        };
			return getSampleSumSearchData(map);
			};
	
	return oTableInit;
	}

function getSampleSumSearchData(map){
	var cxtj=$("#samplesum_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#samplesum_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["cwlx"]=cxnr
	}
	return map;
}

/**
 * 标本量审核列表的状态格式化函数
 * @returns
 */
function yblshFormat(value,row,index) {
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80') {
    	return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.cwid + "\",event,\"AUDIT_SAMPNUM\")' >审核通过</a>";
    }else if (row.zt == '15') {
    	return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.cwid + "\",event,\"AUDIT_SAMPNUM\")' >审核未通过</a>";
    }else{
    	return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.cwid + "\",event,\"AUDIT_SAMPNUM\")' >" + row.shxx_dqgwmc + "审核中</a>";
    }
}

/**
 * 操作按钮格式化函数
 * @returns
 */
function czFormat(value,row,index) {
	if (row.zt == '10') {
		return "<span class='btn btn-warning' onclick=\"recallSampleNum('" + row.cwid + "',event)\" >撤回</span>";
	}else{
		return "";
	}
}

//撤回项目提交
function recallSampleNum(cwid,event){
	var auditType = $("#samplesum_formSearch #auditType").val();
	var msg = '您确定要撤回该文件吗？';
	$.confirm(msg,function(result){
		if(result){
			doAuditRecall(auditType,cwid,function(){
				searchSampleSumResult();
			});
		}
	});
}

/**
 * 按钮回调方法
 * @param id
 * @param action
 * @param tourl
 * @returns
 */
function SampleSum_DealById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action=="view"){
		var url=tourl+"?cwid="+id;
		$.showDialog(url,'查看信息',viewSampleSumConfig);
	}else if(action =='submit'){
		var url=tourl + "?cwid=" +id;
		$.showDialog(url,'提交审核',submitSampleSumConfig);
	}
}



function preSubmitSumpleNum(){
	if($("#auditAjaxForm #lastStep").val() == "true"){
		if(!$("#auditAjaxForm #jsids").val() && !$("#auditAjaxForm #t_jsids").val() && $("#auditAjaxForm #sftg").val() == "1"){
			$.error("请选择权限信息！");
			return false;
		}
	}
	return true;
};

var SampleSum_oButtton= function (){
	var oInit=new Object();
	var postdata = {};
	oInit.Init=function(){
		var btn_query=$("#samplesum_formSearch #btn_query");
		var btn_view = $("#samplesum_formSearch #btn_view");
		var btn_submit = $("#samplesum_formSearch #btn_submit");
		var btn_del=$("#samplesum_formSearch #btn_del");
/*-----------------------模糊查询------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchSampleSumResult(); 
    		});
		}

/*-----------------------查看------------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#samplesum_formSearch #samplesum_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			SampleSum_DealById(sel_row[0].cwid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
/*-----------------------删除------------------------------------*/
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#samplesum_formSearch #samplesum_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].cwid;
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
    								searchSampleSumResult();
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
/*-----------------------提交------------------------------------*/
    	btn_submit.unbind("submit").click(function(){
    		var sel_row = $('#samplesum_formSearch #samplesum_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				if(sel_row[0].zt !='00' && sel_row[0].zt !='15'){
    				$.error("您好！该条记录已提交或者已通过，不允许重复提交！");
    				return;
    			}
				SampleSum_DealById(sel_row[0].cwid,"submit",btn_submit.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	
	}
	return oInit;
}

/**
 * 查看页面模态框
 */
var viewSampleSumConfig={
	width		: "800px",
	modalName	:"viewSampleSumModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
}

var submitSampleSumConfig = {
		width		: "900px",
		modalName	: "submitSampleSumConfig",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					//提交审核
					var auditType = $("#samplesum_formSearch #auditType").val();
					var ywid= $("#samplenumview_formSearch #ywid").val();
					showAuditFlowDialog(auditType,ywid,function(){
						searchSampleSumResult();
					});
				}
			},
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

/**
 * 刷新bootstraptable 
 */
function searchSampleSumResult(){
	$('#samplesum_formSearch #samplesum_list').bootstrapTable('refresh',{pageNumber:1});
}
$(function(){
    var oTable=new SampleSum_TableInit();
		oTable.Init();
	//2.初始化Button的点击事件
    var oButtonInit = new SampleSum_oButtton();
    	oButtonInit.Init();
	jQuery('#samplesum_formSearch .chosen-select').chosen({width: '100%'});
});