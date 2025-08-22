var AllocateList=function(){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#allocate_formSearch #allocate_list").bootstrapTable({
			url: $("#allocate_formSearch #urlPrefix").val()+'/allocate/allocate/pageGetListAllocate',
            method: 'get',                      // 请求方式（*）
            toolbar: '#allocate_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "dbgl.dbrq",				// 排序字段
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
            showColumns: true,                  // 是否显示所有的列
            showRefresh: true,                  // 是否显示刷新按钮
            minimumCountColumns: 2,             // 最少允许的列数
            clickToSelect: true,                // 是否启用点击选中行
            // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "dbid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%'
            },{
				field: 'dbid',
				title: '调拨id',
				width: '2%',
				align: 'left',
				visible: false
			},{
                field: 'dbdh',
                title: '调拨单号',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'zcckmc',
                title: '转出仓库',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'zrckmc',
                title: '转入仓库',
                width: '8%',
                align: 'left',
                visible: true
            },{
				field: 'cklbmc',
				title: '出库类别',
				width: '8%',
				align: 'left',
				visible: true
			},{
                field: 'rklbmc',
                title: '入库类别',
                width: '8%',
                align: 'left',
                visible: true
            // },{
            //     field: 'dchwmc',
            //     title: '调出货物',
            //     width: '10%',
            //     align: 'left',
            //     visible: true
            // },{
			// 	field: 'drhwmc',
			// 	title: '调入货物',
			// 	width: '10%',
			// 	align: 'left',
			// 	visible: true
			// },{
			// 	field: 'dbsl',
			// 	title: '调拨数量',
			// 	width: '6%',
			// 	align: 'left',
			// 	visible: true
			// },{
			// 	field: 'dchw',
			// 	title: '调出货位',
			// 	width: '10%',
			// 	align: 'left',
			// 	visible: true
			// },{
			// 	field: 'drhw',
			// 	title: '调入货位',
			// 	width: '10%',
			// 	align: 'left',
			// 	visible: true
			},{
				field: 'dbrq',
				title: '调拨日期',
				width: '8%',
				align: 'left',
				visible: true
			},{
                field: 'bz',
                title: '备注',
                width: '8%',
                align: 'left',
                visible: true
            }, {
                field: 'jsrmc',
                title: '经手人',
                width: '3%',
                align: 'left',
                visible: true
            // },{
			// 	field: 'cz',
			// 	title: '操作',
			// 	titleTooltip:'操作',
			// 	width: '5%',
			// 	align: 'left',
			// 	formatter:allocate_czFormat,
			// 	visible: true
			}],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
				allocateById(row.dbid,'view',$("#allocate_formSearch #btn_view").attr("tourl"));
             },
		});
        $("#allocate_formSearch #allocate_list").colResizable({
            liveDrag:true, 
            gripInnerHtml:"<div class='grip'></div>", 
            draggingClass:"dragging", 
            resizeMode:'fit', 
            postbackSafe:true,
            partialRefresh:true}        
        );
	};
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   // 页面大小
        	pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "dbgl.dbdh", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		return allocateSearchData(map);
	};
	return oTableInit;
}


function allocateSearchData(map){
	var allocate_select=$("#allocate_formSearch #allocate_select").val();
	var allocate_input=$.trim(jQuery('#allocate_formSearch #allocate_input').val());
	if(allocate_select=="0"){
		map["entire"]=allocate_input;
	}else if(allocate_select=="1"){
		map["dbdh"]=allocate_input;
	}else if(allocate_select=="2"){
		map["zcckmc"]=allocate_input;
	}else if(allocate_select=="3"){
		map["zrckmc"]=allocate_input;
	}else if(allocate_select=="4"){
		map["cklbmc"]=allocate_input;
	}else if(allocate_select=="5"){
		map["rklbmc"]=allocate_input;
	}
	return map;
}
function allocateById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl = $("#allocate_formSearch #urlPrefix").val()+tourl;
	if(action =='view'){
		var url= tourl + "?dbid=" +id;
		$.showDialog(url,'调拨信息',viewAllocateConfig);
	}else if(action=='allocation'){
		var url=tourl;
		$.showDialog(url,'调拨',allocationAllocateConfig);
	}
}

function allocate_queryByWlbm(wlid){
	var url=$("#allocate_formSearch #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'物料信息',WlConfig);
}
var WlConfig = {
	width		: "800px",
	height		: "500px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

function preSubmitRecheck(){
	return true;
}
var allocationAllocateConfig = {
	width		: "1600px",
	modalName    : "allocationModal",
	formName    : "allocationForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		successtwo : {
			label : "保 存",
			className : "btn-success",
			callback : function() {
				if (!$("#allocationForm").valid()) {
					$.alert("请填写完整信息");
					return false;
				}
				var json = [];
				if(t_map.rows != null && t_map.rows.length > 0){
					for(var i=0;i<t_map.rows.length;i++){
						var dbmx_json=JSON.parse(t_map.rows[i].dbmx_json);
						if(dbmx_json!=null && dbmx_json!=""){
							for(var j=0;j<dbmx_json.length;j++){
								var sz = {"dchwid":'',"dchw":'',"drhw":'',"dbsl":''};
								sz.dchwid = dbmx_json[j].hwid;
								sz.dchw = dbmx_json[j].dckw;
								sz.drhw = dbmx_json[j].drkw;
								sz.dbsl = dbmx_json[j].dbsl;
								if (sz.dbsl != "0"){
									json.push(sz);
								}
							}
						}
					}
					$("#allocationForm #dbmx_json").val(JSON.stringify(json));
				}else{
					$.alert("调拨明细不能为空！");
					return false;
				}

				var $this = this;
				var opts = $this["options"] || {};

				$("#allocationForm input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"] || "allocationForm", function (responseText, statusText) {
					if (responseText["status"] == 'success') {
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							allocateResult();
						});
					} else if (responseText["status"] == "fail") {
						preventResubmitForm(".modal-footer > button", false);
						$.error(responseText["message"], function () {
						});
					} else {
						preventResubmitForm(".modal-footer > button", false);
						$.alert(responseText["message"], function () {
						});
					}
				}, ".modal-footer > button");

				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var viewAllocateConfig = {
	width		: "1200px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
var allocate_ButtonInit = function(){
	var oInit = new Object();
	var postdata = {};

	oInit.Init = function () {
		var btn_query = $("#allocate_formSearch #btn_query");
		var btn_view = $("#allocate_formSearch #btn_view");
		var btn_allocation= $("#allocate_formSearch #btn_allocation");
		var btn_del= $("#allocate_formSearch #btn_del");



		if(btn_query != null){
			btn_query.unbind("click").click(function(){
				allocateResult(true);
			});
		}
		/* ---------------------------查看调拨信息-----------------------------------*/
		btn_view.unbind("click").click(function(){
			var sel_row = $('#allocate_formSearch #allocate_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				allocateById(sel_row[0].dbid,"view",btn_view.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		/*--------------------------------调拨------------------------------------------*/
		btn_allocation.unbind("click").click(function(){
			allocateById(null,"allocation",btn_allocation.attr("tourl"));
		});
		/*-----------------------删除------------------------------------*/
		btn_del.unbind("click").click(function(){
			var sel_row = $('#allocate_formSearch #allocate_list').bootstrapTable('getSelections');//获取选择行数据
			if (sel_row.length==0) {
				$.error("请至少选中一行");
				return;
			}else{
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids= ids + ","+ sel_row[i].dbid;
				}
				ids=ids.substr(1);
				$.confirm('您确定要删除所选择的信息吗？',function(result){
					if(result){
						jQuery.ajaxSetup({async:false});
						var url= $("#allocate_formSearch #urlPrefix").val()+btn_del.attr("tourl");
						jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
							setTimeout(function(){
								if(responseText["status"] == 'success'){
									$.success(responseText["message"],function() {
										allocateResult();
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
			}
		});
	};
	return oInit;
};

var allocate_PageInit = function(){
	var oInit = new Object();
	var postdata = {};

	oInit.Init = function () {
		var scbj = $("#allocate_formSearch a[id^='scbj_id_']");
		$.each(scbj, function(i, n){
			var id = $(this).attr("id");
			var code = id.substring(id.lastIndexOf('_')+1,id.length);
			if(code === '0'){
				addTj('scbj',code,'allocate_formSearch');
			}
		});
	}
	return oInit;
}

function allocateResult(isTurnBack){
	if(isTurnBack){
		$('#allocate_formSearch #allocate_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#allocate_formSearch #allocate_list').bootstrapTable('refresh');
	}
}


$(function(){
	//0.界面初始化
	var oInit = new allocate_PageInit();
	oInit.Init();

	// 1.初始化Table
	var oTable = new AllocateList();
	oTable.Init();

	//2.初始化Button的点击事件
	var oButtonInit = new allocate_ButtonInit();
	oButtonInit.Init();

	// 所有下拉框添加choose样式
	jQuery('#contract_formSearch .chosen-select').chosen({width: '100%'});

	$("#allocate_ButtonInit [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});

});