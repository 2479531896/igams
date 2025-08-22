var new_map = [];
var later_map = {rows:[]};
var new_Experiment_TableInit=function(){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#experimentpeo_confirm_formSearch #new_experiment_list").bootstrapTable({
			url: '/experimentS/experimentS/pagedataConfirm',         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: false,                   // 是否显示分页（*）
			paginationShowPageGo: false,         // 增加跳转页码的显示
			queryParams: oTableInit.queryParams,// 传递参数（*）
			sortable: true,                     // 是否启用排序
			clickToSelect: true,                // 是否启用点击选中行
			// height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId: "sjid",                     // 每一行的唯一标识，一般为主键列
			columns: [{
				checkbox: true
			},{
				field: 'nbbm',
				title: '内部编号',
				width: '40%',
				align: 'left',
				titleTooltip:'内部编号',
				visible: true
			},{
				field: 'hzxm',
				title: '患者姓名',
				width: '40%',
				align: 'left',
				titleTooltip:'患者姓名',
				visible:true
			},{
				field: 'qf',
				title: '类型区分',
				width: '6%',
				align: 'left',
				titleTooltip:'类型区分',
				visible:false
			}],
			onLoadSuccess:function(map){
				new_map= map;
				$("#experimentpeo_confirm_formSearch #all").text(new_map.rows.length)
			},
			onLoadError:function(){

			}

		});
		$("#experimentpeo_confirm_formSearch #new_experiment_list").colResizable({
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
			access_token:$("#ac_tk").val(),
			limitColumns: "{'sjxxDto':'sjid,nbbm,hzxm,qf'}" //筛选出这部分字段用于列表显示
		};
		return map;
	};
	return oTableInit;
}

function scanResult(){
	$("#experimentpeo_confirm_formSearch #text").text("");
	$("#experimentpeo_confirm_formSearch #text").removeAttr("style");
	var text = $("#experimentpeo_confirm_formSearch #scan").val()
	if(!text){
		return;
	}
	if (null != new_map.rows && new_map.rows.length>0){
		for (let i = new_map.rows.length-1; i >=0; i--) {
			if (new_map.rows[i].nbbm == text){
				var value = {"nbbm":'',"sjid":'',"hzxm":'',"qf":'',}
				value.nbbm = new_map.rows[i].nbbm;
				value.sjid = new_map.rows[i].sjid;
				value.hzxm = new_map.rows[i].hzxm;
				value.qf = new_map.rows[i].qf;
				later_map.rows.push(value);
				new_map.rows.splice(i,1);
				$("#experimentpeo_confirm_formSearch #later_experiment_list").bootstrapTable('load',later_map);
				$("#experimentpeo_confirm_formSearch #new_experiment_list").bootstrapTable('load',new_map);
				$("#experimentpeo_confirm_formSearch #scan").val("");
				$("#experimentpeo_confirm_formSearch #count").text(later_map.rows.length);
				return;
			}
		}
	}
	if (null != later_map.rows && later_map.rows.length>0){
		for (let i = later_map.rows.length-1; i >=0; i--) {
			if (later_map.rows[i].nbbm == text){
				$("#experimentpeo_confirm_formSearch #text").text("该内部编码已确认！");
				$("#experimentpeo_confirm_formSearch #text").attr("style","color: cornflowerblue");
				$("#experimentpeo_confirm_formSearch #scan").val("");
				return;
			}
		}
	}
	$("#experimentpeo_confirm_formSearch #text").text("该内部编码不存在！");
	$("#experimentpeo_confirm_formSearch #text").attr("style","color: red");
	$("#experimentpeo_confirm_formSearch #scan").val("");

}

function removeResult(){
	var sel_row = $('#experimentpeo_confirm_formSearch #later_experiment_list').bootstrapTable('getSelections');//获取选择行数据
	if(sel_row.length==0){
		return;
	}
	var str = ""
	for (let i = sel_row.length-1; i >=0; i--) {
		str+=","+sel_row[i].sjid
	}
	for (let i = later_map.rows.length-1; i >=0; i--) {
		if (str.indexOf(later_map.rows[i].sjid) != -1){
			var value = {"nbbm":'',"sjid":'',"hzxm":'',"qf":'',}
			value.nbbm = later_map.rows[i].nbbm;
			value.sjid = later_map.rows[i].sjid;
			value.hzxm = later_map.rows[i].hzxm;
			value.qf = later_map.rows[i].qf;
			new_map.rows.push(value);
			later_map.rows.splice(i,1);
			$("#experimentpeo_confirm_formSearch #later_experiment_list").bootstrapTable('load',later_map);
			$("#experimentpeo_confirm_formSearch #new_experiment_list").bootstrapTable('load',new_map);
			$("#experimentpeo_confirm_formSearch #scan").val("");
			$("#experimentpeo_confirm_formSearch #count").text(later_map.rows.length);
		}
	}
}
$(function(){
	var oTable = new new_Experiment_TableInit();
	oTable.Init();
	var oTable = new later_Experiment_TableInit();
	oTable.Init();


})



var later_Experiment_TableInit=function(){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#experimentpeo_confirm_formSearch #later_experiment_list").bootstrapTable({

			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: false,                   // 是否显示分页（*）
			paginationShowPageGo: false,         // 增加跳转页码的显示
			sortable: true,                     // 是否启用排序
			clickToSelect: true,                // 是否启用点击选中行
			// height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId: "sjid",                     // 每一行的唯一标识，一般为主键列
			columns: [{
				checkbox: true
			},{
				field: 'nbbm',
				title: '内部编号',
				width: '40%',
				align: 'left',
				titleTooltip:'内部编号',
				visible: true
			},{
				field: 'hzxm',
				title: '患者姓名',
				width: '40%',
				align: 'left',
				titleTooltip:'患者姓名',
				visible:true
			},{
				field: 'qf',
				title: '类型区分',
				width: '6%',
				align: 'left',
				titleTooltip:'类型区分',
				visible:false
			}],
			onLoadSuccess:function(){
			},
			onLoadError:function(){
				alert("数据加载失败！");
			}

		});
		$("#experimentpeo_confirm_formSearch #later_experiment_list").colResizable({
			liveDrag:true,
			gripInnerHtml:"<div class='grip'></div>",
			draggingClass:"dragging",
			resizeMode:'fit',
			postbackSafe:true,
			partialRefresh:true}
		);
	}
	return oTableInit;
}