/**
 * 刷新样本编号
 * @returns
 */
function refreshYbbh(){
	var jcdw = $("#editHPVForm #jcdw").val();
	if(jcdw){
		$.ajax({
			type:'post',
			url:"/detectionPJ/detectionPJ/pagedataGetDetectionCode",
			cache: false,
			data: {"jcdw":jcdw,
			        "access_token":$("#ac_tk").val(),
			        "jclx":$("#editHPVForm #jclx")
			        },
			dataType:'json',
			success:function(result){
				$("#editHPVForm #ybbh").val(result.ybbh);
				$("#editHPVForm #wybh").val(result.ybbh);
				$("#editHPVForm #bbzbh").val(result.ybbh);
			}
		});
	}else{
		$.alert("请选择检测单位！");
	}
}
function selectHospitaljc() {
	url = "/wechat/hospital/pagedataCheckUnitView?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '医院名称', SelectFzjcHospitalConfigjc);
};
//医院列表弹出框
var SelectFzjcHospitalConfigjc = {
	width: "1000px",
	modalName: "SelectFzjcHospitalConfigjc",
	offAtOnce: false,  //当数据提交成功，立刻关闭窗口
	buttons: {
		success: {
			label: "确 定",
			className: "btn-primary",
			callback: function() {
				var sel_row = $('#hospital_formSearch #hospital_list').bootstrapTable('getSelections');
				if (sel_row.length == 1) {
					var dwid = sel_row[0].dwid;
					var dwmc = sel_row[0].dwmc;
					var cskz1 = sel_row[0].cskz1;
					$("#editHPVForm #sjdw").val(dwid);
				    $("#editHPVForm #yyxxcskz1").val(cskz1);
					$("#editHPVForm #hospitalname").val(dwmc);
					if (cskz1 == '1') {
						$("#editHPVForm #sjdwmc").val(dwmc);
						$("#editHPVForm #qtyycheck").show();
						$("#editHPVForm #sjdwmc").removeAttr("disabled");
					} else {
						$("#editHPVForm #sjdwmc").val("");
						$("#editHPVForm #qtyycheck").hide();
						$("#editHPVForm #sjdwmc").attr("disabled", "disabled");
					
					}
				} else {
					$.error("请选中一行!");
					return false;
				}
			},
		},
		cancel: {
			label: "关 闭",
			className: "btn-default"
		}
	}
};

function chooseJsry() {
	var url = "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择接收人员', chooseJsryConfig);
}
var chooseJsryConfig = {
	width : "800px",
	height : "500px",
	modalName	: "chooseJsryModal",
	formName	: "editHPVForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#taskListFzrForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					$('#editHPVForm #jsry').val(sel_row[0].yhid);
					$('#editHPVForm #jsrymc').val(sel_row[0].zsxm);
					$.closeModal(opts.modalName);
				}else{
					$.error("请选中一行");
					return;
				}
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

function chooseSyry() {
	var url = "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择实验人员', chooseSyryConfig);
}
var chooseSyryConfig = {
	width : "800px",
	height : "500px",
	modalName	: "chooseSyryModal",
	formName	: "editHPVForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#taskListFzrForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					$('#editHPVForm #syry').val(sel_row[0].yhid);
					$('#editHPVForm #syrymc').val(sel_row[0].zsxm);
					$.closeModal(opts.modalName);
				}else{
					$.error("请选中一行");
					return;
				}
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
$("#editHPVForm #ks").change(function(){
	var kzcs=$("#editHPVForm #ks option:selected");
	if (kzcs.attr("kzcs")=="1") {
		$("#editHPVForm #qtkscheck").show();
		$("#editHPVForm #qtks").removeAttr("disabled");
		$("#editHPVForm #qtks").val($("#editHPVForm #ks option:selected").attr("mc"));

	}else{
		$("#editHPVForm #qtkscheck").hide();
		$("#editHPVForm #qtks").attr("disabled","disabled");
		$("#editHPVForm #qtks").val("");
	}
})


//样本类型改变事件
$("#editHPVForm #yblx").change(function() {
	initqtyblx();
});
//检测项目改变事件
$("#editHPVForm .jcxm").change(function() {
	initjczxm();
});
//初始化其他样本类型
function initqtyblx(){
	var yblxcs = $("#editHPVForm #yblx option:selected");
	if (yblxcs.attr("cskz1") == "1") {
		$("#editHPVForm #qtyblxcheck").show();
		$("#editHPVForm #yblxmc").removeAttr("disabled");
		$("#editHPVForm #yblxmc").val();

	} else {
		$("#editHPVForm #qtyblxcheck").hide();
		$("#editHPVForm #yblxmc").attr("disabled", "disabled");
		$("#editHPVForm #yblxmc").val("");
	}
	var kzcs=$("#editHPVForm #yblx option:selected");
	//限制检测项目
	var csdm=kzcs.attr("cskz2");
	if($("#editHPVForm .jcxm").length>0){
		for(var i=0;i<$("#editHPVForm .jcxm").length;i++){
			//如果什么都没选
			var id=$("#editHPVForm .jcxm")[i].id;
			if(kzcs[0].id==null || kzcs[0].id==""){
				$("#editHPVForm #t-"+id).attr("style","display:block;padding-left:0px;");
				$("#editHPVForm #"+id).removeAttr('checked');
				initjczxm();
			}else{
				if(csdm!=null && csdm!=""){
					if(csdm.indexOf($("#editHPVForm #"+id).attr("csdm"))>=0){
						$("#editHPVForm #t-"+id).attr("style","display:block;padding-left:0px;");
						initjczxm();
					}else{
						$("#editHPVForm #t-"+id).attr("style","display:none");
						$("#editHPVForm #"+id).removeAttr('checked');
						initjczxm();
					}
				}else{
					$("#editHPVForm #t-"+id).attr("style","display:none;");
					$("#editHPVForm #"+id).removeAttr('checked');
					initjczxm();
				}
			}
		}
	}
}
// 初始化检测子项目
function initjczxm(){
	
	// var count=0;
	// if($("#editHPVForm .jczxm").length>0){
	// 	for(var i=0;i<$("#editHPVForm .jczxm").length;i++) {
	// 		var zid = $("#editHPVForm .jczxm")[i].id;
	// 		$("#editHPVForm #zxm-" + zid).attr("style", "display:none");
	// 		$("#editHPVForm #" + zid).removeAttr('checked');
	// 	}
	// }
	if($("#editHPVForm .jczxm").length>0){
		if($("#editHPVForm .jcxm")!=null){
			for(var i=0;i<$("#editHPVForm .jczxm").length;i++){
				var zid=$("#editHPVForm .jczxm")[i].id;
				var fcsid=$("#editHPVForm #"+zid).attr("fcsid");
				var fid=0;
				var flag=0;
				for (let j = 0; j < $("#editHPVForm .jcxm").length; j++) {
					if($("#editHPVForm .jcxm")[j].checked){
						fid=$("#editHPVForm .jcxm")[j].id;
					}
					if(fcsid==fid){
						flag=1;
						// count=count+1;
						$("#editHPVForm #zxm-"+zid).attr("style","display:block;padding-left:0px;");
					}
				}
				if(flag==0){
					var checkbox = document.getElementById(zid);
					checkbox.checked = false;
					$("#editHPVForm #zxm-" + zid).attr("style", "display:none");
				}
			}
		}
	}
	// if(count>0){
	// 	$("#editHPVForm .jczxm").attr("validate","{required:true}");
	// }
	// else {$("#editHPVForm .jczxm").removeAttr("validate");}
}
/**
 * 是否显示接收人员时间
 */
function displayJs(){
	var js=$("input[name='sfjs']:checked").val();
	if(js == "1"){
		$("#editHPVForm .jsshow").removeClass("hidden");
		$("#editHPVForm #jsry").val($("#jsrymr").val());
		$("#editHPVForm #jsrymc").val($("#jsrymcmr").val());
		$("#editHPVForm #jssj").val($("#jssjmr").val());
		$("#editHPVForm #jsry").attr("validate","{required:true}");
		$("#editHPVForm #jsrymc").attr("validate","{required:true}");
		$("#editHPVForm #syh").attr("validate","{required:true}");
	}else{
		$("#editHPVForm .jsshow").addClass("hidden");
		$("#editHPVForm #jsry").removeAttr("validate");
		$("#editHPVForm #jsrymc").removeAttr("validate");
		$("#editHPVForm #jsry").val("");
		$("#editHPVForm #jsrymc").val("");
		$("#editHPVForm #jssj").val("");
		$("#editHPVForm #syh").removeAttr("validate");
		$("#editHPVForm #syh").val("");
	}
}

/**
 * 是否显示实验人员时间
 */
function displaySy(){
	var sy=$("input[name='sfsy']:checked").val();
	if(sy == "1"){
		$("#editHPVForm .syshow").removeClass("hidden");
		$("#editHPVForm #syry").val($("#syrymr").val());
		$("#editHPVForm #syrymc").val($("#syrymcmr").val());
		$("#editHPVForm #sysj").val($("#sysjmr").val());
		$("#editHPVForm #syry").attr("validate","{required:true}");
		$("#editHPVForm #syrymc").attr("validate","{required:true}");
	}else{
		$("#editHPVForm .syshow").addClass("hidden");
		$("#editHPVForm #syry").removeAttr("validate");
		$("#editHPVForm #syrymc").removeAttr("validate");
		$("#editHPVForm #syry").val("");
		$("#editHPVForm #syrymc").val("");
		$("#editHPVForm #sysj").val("");
	}
}
//检测项目子
function jczxm(){
	var result=$("#editHPVForm #zxmids").val().split(",");
	for (let i = 0; i < $("#editHPVForm .jczxm").length; i++) {
		var id=$("#editHPVForm .jczxm")[i].id;
		for (let j = 0; j < result.length; j++) {
			if(result[j]==id){
				
				$("#editHPVForm #zxm-"+id).attr("style","display:block;padding-left:0px;");
				$("#editHPVForm #"+id).prop('checked','true');
			}
		}
	}
}
//检测项目父
function jcfxm(){
	
	var result=$("#editHPVForm #fxmids").val().split(",");
	for (let i = 0; i < $("#editHPVForm .jcxm").length; i++) {
		var id=$("#editHPVForm .jcxm")[i].id;
		for (let j = 0; j < result.length; j++) {
			if(result[j]==id){
				$("#editHPVForm #"+id).attr("checked","true");
			}
		}
	}
	initjczxm();
}
function InitBbzts(){
	var result=$("#editHPVForm #bbzt").val().split(",");
	for (let i = 0; i < $("#editHPVForm #bbzts").length; i++) {
		var id=$("#editHPVForm .bbzts")[i].id;
		for (let j = 0; j < result.length; j++) {
			if(result[j]==id){
				$("#editHPVForm #"+id).attr("checked","true");
			}
		}
	}
}

/**
 * 绑定按钮事件
 */
function btnBind(){
	displayJs();
	displaySy();
	var btn_js = $("input[name='sfjs']");
	var btn_sy = $("input[name='sfsy']");
	btn_js.unbind("click").click(function(){
		displayJs();
	});
	btn_sy.unbind("click").click(function(){
		displaySy();
	});
}
var t_map=[];
var Jcxm_TableInit=function(){
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#editHPVForm #jcxm_list").bootstrapTable({
			url: '/detectionPJ/detectionPJ/pagedataGetJcxms?fzjcid='+$("#editHPVForm #fzjcid").val(),         //请求后台的URL（*）
			method: 'get',                      //请求方式（*）
			toolbar: '#editHPVForm #toolbar',                //工具按钮用哪个容器
			striped: true,                      //是否显示行间隔色
			cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   //是否显示分页（*）
			paginationShowPageGo: false,         //增加跳转页码的显示
			sortable: true,                     //是否启用排序
			sortName: "fzjcxm.fzxmid",				//排序字段
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
			showColumns: false,                  //是否显示所有的列
			showRefresh: false,                  //是否显示刷新按钮
			minimumCountColumns: 2,             //最少允许的列数
			clickToSelect: true,                //是否启用点击选中行
			//height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId: "fzxmid",                     //每一行的唯一标识，一般为主键列
			showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
			cardView: false,                    //是否显示详细视图
			detailView: false,                   //是否显示父子表
			paginationDetailHAlign:' hidden',
			columns: [{
				field: 'fzxmid',
				title: '分子项目id',
				width: '10%',
				align: 'left',
				visible:false
			},{
				field: 'jcxmmc',
				title: '项目',
				width: '26%',
				align: 'left',
				visible: true
			},{
				field: 'jczxmmc',
				title: '子项目',
				width: '16%',
				align: 'left',
				visible: true
			},{
				field: 'bgrq',
				title: '报告日期',
				width: '20%',
				align: 'left',
				visible: true,
				formatter:bgrqformat,
			}],
			onLoadSuccess:function(map){
				t_map=map;
			},
			onLoadError:function(){
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
			},
		});
		$("#editHPVForm #jcxm_list").colResizable({
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
			sortLastName: "fzjcxm.fzxmid", // 防止同名排位用
			sortLastOrder: "asc" // 防止同名排位用
			// 搜索框使用
			// search:params.search
		};
		return map;
	};
	return oTableInit;
}

function bgrqformat(value,row,index){
	if(row.bgrq==null || row.bgrq == undefined){
		row.bgrq="";
	}
	var html="<input id='bgrq_"+index+"' name='bgrq_"+index+"' placeholder='yyyy-mm-dd HH:mm' class='bgrq' value='"+row.bgrq+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"tochangeZd('"+index+"',this,\'bgrq\')\" >";
	setTimeout(function() {
		laydate.render({
			elem: '#editHPVForm #bgrq_'+index
			,theme: '#2381E9'
			,type: 'datetime'
            ,format: 'yyyy-MM-dd HH:mm'//保留时分
			,btns: ['clear', 'confirm']
			,done: function(value, date, endDate){
				t_map.rows[index].bgrq=value;
			}
		});
	}, 100);
	return html;
}

function tochangeZd(index,e,zdmc){
	if (zdmc=='bgrq'){
		t_map.rows[index].bgrq=e.value;
	}
}

$(document).ready(function () {
    var oTable=new Jcxm_TableInit();
	oTable.Init();
	btnBind();
	// //采集时间 添加日期控件
	laydate.render({
		elem: '#editHPVForm #cjsj'
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
	//添加接收日期控件
	laydate.render({
		elem: '#editHPVForm #jssj'
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
	//添加实验日期控件
	laydate.render({
		elem: '#editHPVForm #sysj'
		,type: 'datetime'
		,ready: function(date){
			if(this.dateTime.hours==0&&this.dateTime.minutes==0&&this.dateTime.seconds==0){
				// var myDate = new Date($("#editHPVForm #cjsj").val($("#cjsjmr").val())); //实例一个时间对象；
				var myDate = new Date(); //实例一个时间对象；
				this.dateTime.hours=myDate.getHours();
				this.dateTime.minutes=myDate.getMinutes();
				this.dateTime.seconds=myDate.getSeconds();
			}
		}
	});
	laydate.render({
		elem: '#editHPVForm #bgrq'
		,type: 'datetime'
	});
	// $("#editHPVForm #cjsj").val($("#cjsjmr").val());
	var yyxxcskz1 = $("#editHPVForm #yyxxcskz1").val();
	if (yyxxcskz1 != null && yyxxcskz1 == '1') {
		$("#editHPVForm #sjdwmc").removeAttr("disabled");
		$("#editHPVForm #qtyycheck").show();
		if($("#editHPVForm #formAction").val()=='addSaveDetectionPJ'){
			$("#editHPVForm #sjdwmc").val($("#editHPVForm #hospitalname").val());
		}
	}
	else {
		$("#editHPVForm #qtyycheck").hide();
		$("#editHPVForm #sjdwmc").attr("disabled", "disabled");
	}
	if($("#editHPVForm #qtks").val()!=null&&$("#editHPVForm #qtks").val()!=undefined&&$("#editHPVForm #qtks").val()!=""){
		$("#editHPVForm #qtks").removeAttr("disabled");
	}
	if($("#editHPVForm #formAction").val()=='addSaveDetectionPJ'){
		var kzcs=$("#editHPVForm #ks option:selected");
		if (kzcs.attr("kzcs")=="1") {
			$("#editHPVForm #qtkscheck").show();
			$("#editHPVForm #qtks").removeAttr("disabled");
			$("#editHPVForm #qtks").val($("#editHPVForm #ks option:selected").attr("mc"));
		}
	}
	//赋值给样本类型
	var yblxcskz1 = $("#editHPVForm #yblxcskz1").val();
	if (yblxcskz1 != null && yblxcskz1 == '1') {
		$("#editHPVForm #yblxmc").removeAttr("disabled");
		$("#editHPVForm #qtyblxcheck").show();
	} else {
		$("#editHPVForm #qtyblxcheck").hide();
		$("#editHPVForm #yblxmc").attr("disabled", "disabled");
	}
	initqtyblx();
	initjczxm();
	jcfxm();
	jczxm();
	InitBbzts();
	//所有下拉框添加choose样式
    jQuery('#editHPVForm .chosen-select').chosen({width: '100%'});
});
