var r_TableInit = function () {
	var oTableInit = new Object();
	// 初始化Table
	oTableInit.Init = function () {
		$('#reagent_formSearch #tb_list').bootstrapTable({
			url: '/reagent/reagent/pageGetListReagent',         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#reagent_formSearch #toolbar', // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     // 是否启用排序
			sortName:"sjrq",					// 排序字段
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
			uniqueId: "sysjid",                     // 每一行的唯一标识，一般为主键列
			showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
			cardView: false,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			columns: [{
				checkbox: true
			}, {
				'field': '',
			    'title': '序号',
			    'align': 'center',
			    'width': '6%',
			    'formatter': function (value, row, index) {
			    　　var options = $('#reagent_formSearch #tb_list').bootstrapTable('getOptions');
			    　　return options.pageSize * (options.pageNumber - 1) + index + 1;
			    }
			}, {
				field: 'sjrq',
				title: '日期',
				width: '10%',
				align: 'left',
				visible: true
			}, {
				field: 'jcdwmc',
				title: '检测单位',
				width: '15%',
				align: 'left',
				visible: true
			}, {
				field: 'sjmc',
				title: '试剂名称',
				width: '20%',
				align: 'left',
				visible: true
			},  {
				field: 'sjph',
				title: '批号',
				width: '15%',
				align: 'left',
				visible: true
			}, {
                field: 'type',
                title: '区分',
                width: '15%',
                align: 'left',
                formatter: function (value, row, index) {
                　　if(row.type=='TQ'){
                  		return '提取';
                  	}else if(row.type=='WK'){
                        return '建库';
                    }
                },
                visible: true
            },{
                field: 'bz',
                title: '备注',
                width: '40%',
                align: 'left',
                visible: true
			}],
			onLoadSuccess: function () {
			},
			onLoadError: function () {

			},
			onDblClickRow: function (row, $element) {
				reagentById(row.sysjid,'view',$("#reagent_formSearch #btn_view").attr("tourl"));
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
        sortLastName: "lrsj", // 防止同名排位用
        sortLastOrder: "desc" // 防止同名排位用
        // 搜索框使用
        // search:params.search
    };
    return getReagentSearchData(map);
	};

	return oTableInit;
};

function getReagentSearchData(map){
    var jcdw=$("#reagent_formSearch #jcdw").val();
    map["jcdw"] = jcdw;

    var sjmc=$("#reagent_formSearch #sjmc").val();
    map["sjmc"] = sjmc;

    var jhxs = document.getElementById('jhxs');
    map["jhxs"] = jhxs.value;

	var startRq = jQuery('#reagent_formSearch #startRq').val();
	map["startRq"] = startRq;

	var endRq = jQuery('#reagent_formSearch #endRq').val();
	map["endRq"] = endRq;

	 var sjph=$("#reagent_formSearch #sjph").val();
     map["sjph"] = sjph;

     var bz=$("#reagent_formSearch #bz").val();
     map["bz"] = bz;
	 return map;
}

function reagentResult(isTurnBack){
	if(isTurnBack){
		$('#reagent_formSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#reagent_formSearch #tb_list').bootstrapTable('refresh');
	}
}

function reagentById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action =='view'){
		var url= tourl + "?sysjid=" +id;
		$.showDialog(url,'试剂信息查看',viewReagentConfig);
	}else if(action =='mod'){
		var url=tourl + "?sysjid=" +id;
		$.showDialog(url,'编辑试剂信息',modReagentConfig);
	}
}


var r_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
		var btn_view = $("#reagent_formSearch #btn_view");
		var btn_mod = $("#reagent_formSearch #btn_mod");
        var btn_query = $("#reagent_formSearch #btn_query");
		if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			reagentResult(true);
    		});
    	}

    	//添加日期控件(创建时间开始时间)
    	laydate.render({
    	   elem: '#reagent_formSearch #startRq'
		   ,type: 'date'
    	});
    	//添加日期控件(创建时间结束时间)
    	laydate.render({
    	   elem: '#reagent_formSearch #endRq'
		   ,type: 'date'
    	});

        /*---------------------------查看文库列表-----------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#reagent_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			reagentById(sel_row[0].sysjid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/*---------------------------编辑文库信息-----------------------------------*/
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#reagent_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			reagentById(sel_row[0].sysjid,"mod",btn_mod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
		/*------------------------------------------------------------------------------------*/
    };
    return oInit;
};

var viewReagentConfig = {
		width		: "1600px",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};


var modReagentConfig = {
		width		: "1600px",
		modalName	:"modReagentModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#editReagentForm").valid()){
						$.alert("请填写完整信息");
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					$("#editReagentForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"editReagentForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								reagentResult();
							});
						}else if(responseText["status"] == "fail"){
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

function tochangedbj(){
    var jhxs = $("#reagent_formSearch #jhxs").val();
    if(jhxs == "1"){
        $("#reagent_formSearch #jhxs").val("0");
        $('#reagent_formSearch #tb_list').bootstrapTable('refresh');
    }else{
        $("#reagent_formSearch #jhxs").val("1");
        $('#reagent_formSearch #tb_list').bootstrapTable('refresh');
    }
}
$(document).ready(function(){
    var oTable = new r_TableInit();
	oTable.Init();
    var oButtonInit = new r_ButtonInit();
    oButtonInit.Init();
	jQuery('#reagent_formSearch .chosen-select').chosen({width: '100%'});
    $('#reagent_formSearch #jhxs').bootstrapSwitch({
        handleWidth: '25px',//设置控件宽度
        labelWidth: '25px',
        onText: "是",      // 设置ON文本
        offText: "否",    // 设置OFF文本
        onColor: "success",// 设置ON文本颜色     (info/success/warning/danger/primary)
        offColor: "warning",  // 设置OFF文本颜色        (info/success/warning/danger/primary)
        onSwitchChange: function (event, state) {
            if(state){
                $('#reagent_formSearch #jhxs').val("1");
                $('#reagent_formSearch #tb_list').bootstrapTable('refresh');
            }else{
                $('#reagent_formSearch #jhxs').val("0");
                $('#reagent_formSearch #tb_list').bootstrapTable('refresh');
            }
        }
    });
	$('#reagent_formSearch #jhxs').bootstrapSwitch('state',true,true);
});