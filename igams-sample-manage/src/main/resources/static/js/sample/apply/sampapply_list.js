
var sampApply_turnOff=true;
    	
var sampApply_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#sampApply_formSearch #tb_list').bootstrapTable({
            url: '/sample/apply/pageGetListSampApply',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#sampApply_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "sq.lrsj",				//排序字段
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
            uniqueId: "sqid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            }, {
                field: 'ybid',
                title: '标本ID',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'sqid',
                title: '申请ID',
                width: '8%',
                align: 'left',
                visible: false
            },{
                field: 'shxx_dqgwmc',
                title: '当前审核岗位名称',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'shxx_shid',
                title: '审核ID',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'yblx',
                title: '标本类型',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true,
                formatter:yblxFormat
            }, {
                field: 'ybbh',
                title: '标本编号',
                width: '14%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'ytmc',
                title: '申请用途',
                width: '6%',
                align: 'left',
                visible: true
            }, {
                field: 'sqs',
                title: '申请数',
                width: '5%',
                align: 'left',
                visible: true
            }, {
                field: 'sqrxm',
                title: '申请人员',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'sqsj',
                title: '申请时间',
                width: '16%',
                align: 'left',
                sortable: true,
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
            },{
                field: 'zt',
                title: '状态',
                width: '10%',
                align: 'left',
                formatter:ybsqztFormat,
                visible: true
            },{
                field: 'lrsj',
                align: 'center',
                title: '录入时间',
                visible: false
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	SampApplyDealById(row.sqid, 'view',$("#sampApply_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#sampApply_formSearch #tb_list").colResizable({
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
            sortLastName: "sq.sqid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

    	var cxtj = $("#sampApply_formSearch #cxtj").val();
    	// 查询条件
    	var cxnr = $.trim(jQuery('#sampApply_formSearch #cxnr').val());
    	// '0':'来源编号','1':'标本编号','2':'冰箱号','3':'抽屉号','4':'盒子号','5':'备注'
    	if (cxtj == "0") {
    		map["sqs"] = cxnr;
    	}else if (cxtj == "1") {
    		map["ybbh"] = cxnr;
    	}else if (cxtj == "2") {
    		map["bxh"] = cxnr;
    	}else if (cxtj == "3") {
    		map["chth"] = cxnr;
    	}else if (cxtj == "4") {
    		map["hzh"] = cxnr;
    	}else if (cxtj == "5") {
    		map["bz"] = cxnr;
    	}
    	// 标本类型
    	var yblx = jQuery('#sampApply_formSearch #yblx_id_tj').val();
    	map["yblxs"] = yblx;
    	
    	// 用途
    	var yt = jQuery('#sampApply_formSearch #yt_id_tj').val();
    	map["yts"] = yt;
    	
    	// 申请开始日期
    	var sqrqstart = jQuery('#sampApply_formSearch #sqrqstart').val();
    	map["sqrqstart"] = sqrqstart;
    	
    	// 申请结束日期
    	var sqrqend = jQuery('#sampApply_formSearch #sqrqend').val();
    	map["sqrqend"] = sqrqend;
    	
    	return map;
    };
    return oTableInit;
};

var addSampApplyConfig = {
	width		: "800px",
	modalName	: "addSampApplyModal",
	formName	: "ajaxForm",
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
				
				if($("#ajaxForm #sqs").val() > ($("#sampApply_ajaxForm #ysl").val() - $("#sampApply_ajaxForm #yyds").val())){
					$.error("申请数量不允许超过库存数 - 预定数",function() {
					});
					return false;
				}
				
				if($("#ajaxForm #yt").val() == ""){
					$.error("请填写申请用途",function() {
					});
					return false;
				}

				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchSampApplyResult();
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

var viewSampApplyConfig = {
	width		: "800px",
	modalName	: "viewSampApplyModal",
	formName	: "viewsampApply_ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

//按钮动作函数
function SampApplyDealById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action =='view'){
		var url= tourl + "?sqid=" +id;
		$.showDialog(url,'查看申请',viewSampApplyConfig);
	}else if(action =='add'){
		var url= tourl;
		$.showDialog(url,'新增申请',addSampApplyConfig);
	}else if(action =='mod'){
		var url=tourl + "?sqid=" +id;
		$.showDialog(url,'编辑申请',addSampApplyConfig);
	}
}


var sampApply_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
    	//添加日期控件
		laydate.render({
		   elem: '#sqrqstart'
		  ,theme: '#2381E9'
		});
		//添加日期控件
		laydate.render({
		   elem: '#sqrqend'
		  ,theme: '#2381E9'
		});
        //初始化页面上面的按钮事件
    	var btn_add = $("#sampApply_formSearch #btn_add");
    	var btn_mod = $("#sampApply_formSearch #btn_mod");
    	var btn_del = $("#sampApply_formSearch #btn_del");
    	var btn_view = $("#sampApply_formSearch #btn_view");
    	
    	var btn_query = $("#sampApply_formSearch #btn_query");

    	//绑定搜索发送功能
    	if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			searchSampApplyResult(true);
    		});
    	}
    	btn_add.unbind("click").click(function(){
    		SampApplyDealById(null,"add",btn_add.attr("tourl"));
    	});
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#sampApply_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			SampApplyDealById(sel_row[0].sqid,"mod",btn_mod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#sampApply_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			SampApplyDealById(sel_row[0].sqid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#sampApply_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].sqid;
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
    								searchSampApplyResult();
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
    	
    	/**显示隐藏**/      
    	$("#sampApply_formSearch #sl_searchMore").on("click", function(ev){ 
    		var ev=ev||event; 
    		if(sampApply_turnOff){
    			$("#sampApply_formSearch #searchMore").slideDown("low");
    			sampApply_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#sampApply_formSearch #searchMore").slideUp("low");
    			sampApply_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    		//showMore();
    	});
    };

    return oInit;
};

function searchSampApplyResult(isTurnBack){
	//关闭高级搜索条件
	$("#sampApply_formSearch #searchMore").slideUp("low");
	sampApply_turnOff=true;
	$("#sampApply_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#sampApply_formSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#sampApply_formSearch #tb_list').bootstrapTable('refresh');
	}
}
/**
 * 标本申请列表的状态格式化函数
 * @returns
 */
function ybsqztFormat(value,row,index) {
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80') {
    	return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.sqid + "\",event,\"AUDIT_SAMPAPPLY\")' >审核通过</a>";
    }
    else{
    	return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.sqid + "\",event,\"AUDIT_SAMPAPPLY\")' >" + row.shxx_dqgwmc + "审核中</a>";
    }
}
function yblxFormat(value,row,index) {
    return row.yblxmc;
}

$(function(){

    //1.初始化Table
    var oTable = new sampApply_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new sampApply_ButtonInit();
    oButtonInit.Init();
	//所有下拉框添加choose样式
	jQuery('#sampApply_formSearch .chosen-select').chosen({width: '100%'});
	
	// 初始绑定显示更多的事件
	//var showMore = function() {
	$("#sampApply_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
	
});
