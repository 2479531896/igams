$("#editCovidForm .jcxm").change(function(e){
	limitYblx();
})


/**
 * 内网打印机
 * @returns
 */
$("#editCovidForm #local_ip").click(function () {
	$("#editCovidForm #remoteDiv").hide();
	$("#editCovidForm #glxx").attr("disabled", "disabled");
})

/**
 *
 * @returns
 */
$("#editCovidForm #remote_ip").click(function () {
	$("#editCovidForm #remoteDiv").show();
	$("#editCovidForm #glxx").removeAttr("disabled");
})

function printerIpChecked() {
	if ($("#editCovidForm #local_ip").attr("checked")) {
		$("#editCovidForm #remoteDiv").hide();
		$("#editCovidForm #glxx").attr("disabled", "disabled");
	} else if ($("#editCovidForm #remote_ip").attr("checked")) {
		$("#editCovidForm #remoteDiv").show();
		$("#editCovidForm #glxx").removeAttr("disabled");
	}
}
//限制标本类型
function limitYblx(){
	var yblxlength=$("#editCovidForm .t_yblx").length;
	var jcxmid=$("input[name='fzxmids']:checked").attr("id");
	var csdm=$("#editCovidForm #"+jcxmid ).attr("csdm");
	var yblxcsdm = $("#editCovidForm #yblx option:selected").attr("csdm");
	var jcdxlxdm = $("#editCovidForm #jcdxlxid option:selected").attr("csdm");
	var isyblxin = false;
	if(yblxlength>0){
		for(var i=0;i<yblxlength;i++){
			var yblxid=$("#editCovidForm .t_yblx")[i].id;
			if(yblxid!=null && yblxid!=""){
				if ("W" == jcdxlxdm){
					var sfqt=$("#editCovidForm #"+yblxid).attr("cskz1");
					if("1" == sfqt){
						$("#editCovidForm #"+yblxid).attr("style","display:block;");
						$("#editCovidForm #"+yblxid).prop("selected","selected");
						isyblxin = true;
					}else{
						$("#editCovidForm #"+yblxid).attr("style","display:none;");
					}
				}else {
					if(jcxmid==null || jcxmid ==""){
						$("#editCovidForm #"+yblxid).attr("style","display:block;");
						isyblxin = true
					}else{
						var jcxmdm=$("#editCovidForm #"+yblxid).attr("cskz2");
						var lsyblxcsdm=$("#editCovidForm #"+yblxid).attr("csdm");
						if(jcxmdm!=null && jcxmdm!=""){
							if(jcxmdm.indexOf(csdm)>=0){
								$("#editCovidForm #"+yblxid).attr("style","display:block;");
								if (yblxcsdm == lsyblxcsdm){
									isyblxin = true
								}
							}else{
								$("#editCovidForm #"+yblxid).attr("style","display:none;");
							}
						}else{
							$("#editCovidForm #"+yblxid).attr("style","display:none;");
						}
					}
				}
			}
		}
	}
	if (!isyblxin){
		$("#yblx").clearFields();
	}
	$('#editCovidForm #yblx').trigger("chosen:updated"); //更新下拉框
	initqtyblx()
}

function selectHospitaljc() {
	url = "/wechat/hospital/pagedataCheckUnitView?access_token=" + $("#editCovidForm #access_token").val();
	$.showDialog(url, '医院名称', SelectFzjcHospitalConfigjc);
};
//送检单位模板框
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
					$("#editCovidForm #sjdw").val(dwid);
				    $("#editCovidForm #yyxxcskz1").val(cskz1);
					$("#editCovidForm #hospitalname").val(dwmc);
					if (cskz1 == '1') {
						$("#editCovidForm #sjdwmc").val("");
						$("#editCovidForm #qtyycheck").show();
						$("#editCovidForm #sjdwmc").removeAttr("disabled");
					} else {
						$("#editCovidForm #sjdwmc").val("");
						$("#editCovidForm #qtyycheck").hide();
						$("#editCovidForm #sjdwmc").attr("disabled", "disabled");
					
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

//初始化送检单位
function initqtsjdw(){

}
//处理生产日期
function dealScrq(){
	var scrq = $("#scrq").val();
	if (scrq.indexOf("/")!= -1){
		for (;scrq.indexOf("/")!= -1;){
			scrq = scrq.replace("/","-");
		}
	}
	if (scrq.indexOf(".")!= -1){
		for (;scrq.indexOf(".")!= -1;){
			scrq = scrq.replace(".","-");
		}
	}
	$("#scrq").val(scrq);
}
//处理预约检测日期
function dealYyjcrq(){
	var yyjcrq = $("#yyjcrq").val();
	if (yyjcrq.indexOf("/")!= -1){
		for (;yyjcrq.indexOf("/")!= -1;){
			yyjcrq = yyjcrq.replace("/","-");
		}
	}
	if (yyjcrq.indexOf(".")!= -1){
		for (;yyjcrq.indexOf(".")!= -1;){
			yyjcrq = yyjcrq.replace(".","-");
		}
	}
	$("#yyjcrq").val(yyjcrq);
}
//处理采集时间
function dealCjsj(){
	var cjsj = $("#editCovidForm #cjsj").val();
	if (cjsj.indexOf("/")!= -1){
		for (;cjsj.indexOf("/")!= -1;){
			cjsj = cjsj.replace("/","-");
		}
	}
	if (cjsj.indexOf(".")!= -1){
		for (;cjsj.indexOf(".")!= -1;){
			cjsj = cjsj.replace(".","-");
		}
	}
	$("#editCovidForm #cjsj").val(cjsj);
}

//科室改变事件
$("#editCovidForm #ks").change(function() {
	initqtks()
});

//初始化科室
function initqtks(){
	var kzcs = $("#editCovidForm #ks option:selected");
	if (kzcs.attr("kzcs") == "1") {
		$("#editCovidForm #qtkscheck").show();
		$("#editCovidForm #qtks").removeAttr("disabled");
	} else {
		$("#editCovidForm #qtkscheck").hide();
		$("#editCovidForm #qtks").attr("disabled", "disabled");
		$("#editCovidForm #qtks").val("");
	}
}

//样本类型改变事件
$("#editCovidForm #yblx").change(function() {
	initqtyblx()
});

//初始化其他样本类型
function initqtyblx(){
	var yblxcs = $("#editCovidForm #yblx option:selected");
	if (yblxcs.attr("cskz1") == "1") {
		$("#editCovidForm #qtyblxcheck").show();
		$("#editCovidForm #yblxmc").removeAttr("disabled");
	} else {
		$("#editCovidForm #qtyblxcheck").hide();
		$("#editCovidForm #yblxmc").attr("disabled", "disabled");
		$("#editCovidForm #yblxmc").val("");
	}
}

//检测对象类型改变事件
$("#editCovidForm #jcdxlxid").change(function() {
	var jcdxlxdm = $("#editCovidForm #jcdxlxid option:selected").attr("csdm");
	$("#editCovidForm #jcdxlx").val($("#editCovidForm #jcdxlxid option:selected").val());
	if ("W" == jcdxlxdm){
		$("#yblx").clearFields();
	}
	limitYblx();
});

$(document).ready(function () {
	//根据检测对象类型展示不同的页面
	if ( $("#editCovidForm #jcdxcsdm").val() == 'W'){
		$("#editCovidForm .obj-add").show();
		$("#editCovidForm .obj-del").hide();
		$("#editCovidForm .obj-print").hide();
		$("#editCovidForm .obj-del-content").removeAttr("validate");
	}else {
		$("#editCovidForm .obj-add").hide();
		$("#editCovidForm .obj-print").hide();
		$("#editCovidForm .obj-add-content").removeAttr("validate");
	}
	//根据fzjcid是否有值判断是新增还是修改，显示不同内容
	if($("#editCovidForm #fzjcid").val() == "" || $("#editCovidForm #fzjcid").val() == null || $("#editCovidForm #fzjcid").val() == undefined){//新增
		$("#editCovidForm .obj-add").show();
		$("#editCovidForm .obj-print").show();
		$("#editCovidForm .obj-del").hide();
		$("#editCovidForm .obj-del-content").removeAttr("validate");
		$("#editCovidForm .obj-mod").hide();
		$("#editCovidForm .obj-mod-content").removeAttr("validate");
		//检测项目和单位不为空，因为是新增需要根据这两项去生产样本编号
		$("#editCovidForm #jcxm").attr("validate","{required:true}");
		$("#editCovidForm #jcdw").attr("validate","{required:true}");
	}
	if ($("#editCovidForm #hzid").val() == "" || $("#editCovidForm #hzid").val() == null || $("#editCovidForm #hzid").val() == undefined){
		$("#editCovidForm .hzxx_info").hide();
	}

	if($("#editCovidForm #jcxmid_mr").val() != "" || $("editCovidForm #jcxmid_mr").val() != null || $("editCovidForm #jcxmid_mr").val() != undefined){
		var jcxmidlist = $("#editCovidForm #jcxmid_mr").val().split(",");
		for (let i = 0; i < jcxmidlist.length; i++) {
			$("input[type=checkbox][name=fzxmids][value="+jcxmidlist[i]+"]").attr("checked",'checked');
		}
	}
	if($("#editCovidForm #xmflag").val()=='1'){
		$("#editCovidForm #jcxm").attr("style","display:none;");
	}
	//采集时间 添加日期控件
	laydate.render({
		elem: '#editCovidForm #cjsj'
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
	//预约检测日期 添加日期控件
	laydate.render({
		elem: '#editCovidForm #yyjcrq'
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
	//添加日期控件
	laydate.render({
		elem: '#editCovidForm #scrq'
		,theme: '#2381E9'
	});
	laydate.render({
		elem: '#editCovidForm #bgrq'
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
	//接收时间 添加时间控件
	laydate.render({
		elem: '#editCovidForm #jssj'
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

	var yyxxcskz1 = $("#editCovidForm #yyxxcskz1").val();
	if (yyxxcskz1 != null && yyxxcskz1 == '1') {
		$("#editCovidForm #sjdwmc").removeAttr("disabled");
		$("#editCovidForm #qtyycheck").show();
	} else {
		$("#editCovidForm #qtyycheck").hide();
		$("#editCovidForm #sjdwmc").attr("disabled", "disabled");
	}
	//赋值给科室
	var qtks = $("#editCovidForm #qtks").val();
	if (qtks != null && qtks != "") {
		$("#editCovidForm #qtkscheck").show();
		$("#editCovidForm #qtks").removeAttr("disabled");
	} else {
		$("#editCovidForm #qtkscheck").hide();
		$("#editCovidForm #qtks").attr("disabled", "disabled");
	}

	//赋值给样本类型
	var yblxmc = $("#editCovidForm #yblxmc").val();
	if (yblxmc != null && yblxmc != "") {
		$("#editCovidForm #qtyblxcheck").show();
		$("#editCovidForm #yblxmc").removeAttr("disabled");
	} else {
		$("#editCovidForm #qtyblxcheck").hide();
		$("#editCovidForm #yblxmc").attr("disabled", "disabled");
	}
	
    //所有下拉框添加choose样式
    jQuery('#editCovidForm .chosen-select').chosen({width: '100%'});
    //限制样本类型
	limitYblx();
	printerIpChecked();
});
