
/**
 * 更改单位类别
 * @returns
 */
function choseDwlx(){
	$('#seniorEditBorrowingForm #dwdm').val("");
	$('#seniorEditBorrowingForm #dwid').val("");
	$('#seniorEditBorrowingForm #dwmc').val("");
}
/**
 * 选择业务员
 * @returns
 */
function selectywy(){
	var url=$('#seniorEditBorrowingForm #urlPrefix').val() + "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url,'选择业务员',addYwyConfig);
}
var addYwyConfig = {
	width		: "800px",
	modalName	:"addFzrModal",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					$('#seniorEditBorrowingForm #ywy').val(sel_row[0].yhid);
					$('#seniorEditBorrowingForm #ywymc').val(sel_row[0].zsxm);
					//保存操作习惯
					$.ajax({
						type:'post',
						url:$('#seniorEditBorrowingForm #urlPrefix').val() + "/common/habit/commonModUserHabit",
						cache: false,
						data: {"ywy":sel_row[0].yhid,"access_token":$("#ac_tk").val()},
						dataType:'json',
						success:function(data){}
					});
				}else{
					$.error("请选中一行");
					return false;
				}
			},
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
/**
 * 刷新借用单号
 */

function sesxjydh(){
	$.ajax({
		type:'post',
		url: $('#seniorEditBorrowingForm #urlPrefix').val()+'/borrowing/borrowing/sxjydhOA',
		cache: false,
		data: {"access_token":$("#ac_tk").val()},
		dataType:'json',
		success:function(data){
			if(data != null && data.length != 0){
				if (data.jydh){
					$("#seniorEditBorrowingForm #jydh").val(data.jydh);
				}
			}
		}
	});
}

/**
 * 更改允许数量
 * @returns
 */
function changeYxsl(jyxxid,hwid,e){
	let zyxsl = $('#seniorEditBorrowingForm #zyxsl_'+jyxxid).val();
	let qyxsl = $('#seniorEditBorrowingForm #qyxsl_'+jyxxid+'_'+hwid).val();
	let yxsl = $('#seniorEditBorrowingForm #yxsl_'+jyxxid+'_'+hwid).val();
	if (yxsl == "" ){
		$('#seniorEditBorrowingForm #yxsl_'+jyxxid+'_'+hwid).val("0");
		yxsl = 0;
	}
	let klsl = $('#seniorEditBorrowingForm #klsl_'+jyxxid+'_'+hwid).text();
	if (yxsl*1 > klsl*1 ){
		$.alert("借用数量不能大于该物料的可借用数量！");
		$('#seniorEditBorrowingForm #yxsl_'+jyxxid+'_'+hwid).val(qyxsl);
		$('#seniorEditBorrowingForm #qyxsl_'+jyxxid+'_'+hwid).val(qyxsl);
		return false;
	}
	let total = zyxsl*1 - qyxsl*1 + yxsl*1;
	let jcsl = $('#seniorEditBorrowingForm #jcsl_'+jyxxid).text();
	if (total >jcsl*1){
		$.alert("总允许借用数量不能大于该物料借用数量！");
		$('#seniorEditBorrowingForm #yxsl_'+jyxxid+'_'+hwid).val(qyxsl);
		$('#seniorEditBorrowingForm #qyxsl_'+jyxxid+'_'+hwid).val(qyxsl);
		return false;
	}
	var jymx_json = $("#seniorEditBorrowingForm #jymx_json").val();
	var jymxJson=[];
	if(jymx_json!=null && jymx_json!=""){
		jymxJson = JSON.parse(jymx_json);
		for(let j = 0; j < jymxJson.length; j++) {
			if (jymxJson[j].hwid == hwid && jymxJson[j].jyxxid == jyxxid) {
				jymxJson.splice(j, 1);
			}
		}
	}
	$('#seniorEditBorrowingForm #qyxsl_'+jyxxid+'_'+hwid).val(yxsl);
	$('#seniorEditBorrowingForm #zyxsl_'+jyxxid).val(total);
	if (yxsl != "0"){
		let jcjyid = $('#seniorEditBorrowingForm #jcjyid').val();
		var sz = {"jcjyid":'',"jyxxid":'',"hwid":'',"jysl":''};
		sz.jcjyid = jcjyid;
		sz.jyxxid = jyxxid;
		sz.hwid = hwid;
		sz.jysl = yxsl;
		jymxJson.push(sz);
	}
	$("#seniorEditBorrowingForm #jymx_json").val(JSON.stringify(jymxJson));
}
/**
 * 选择申请部门列表
 * @returns
 */
function choosedw(){
	var dwdm =$('#seniorEditBorrowingForm #dwlx').find("option:selected").attr("csdm");
	if(dwdm){
		var url=$('#seniorEditBorrowingForm #urlPrefix').val() + "/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val()+"&jgdm="+dwdm;
		$.showDialog(url,'选择部门',addDwConfig);
	}else{
		$.alert("请先选择单位类别！");
		return false;
	}
}
var addDwConfig = {
	width		: "800px",
	modalName	:"addDwModal",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row = $('#listDepartmentForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					if (sel_row[0].jgdm){
						$('#seniorEditBorrowingForm #dwdm').val(sel_row[0].jgdm);
					}else{
						$('#seniorEditBorrowingForm #dwdm').val("");
						$.alert("U8不存在此部门！");
						return false;
					}
					$('#seniorEditBorrowingForm #dwid').val(sel_row[0].jgid);
					$('#seniorEditBorrowingForm #dwmc').val(sel_row[0].jgmc);
				}else{
					$.error("请选中一行");
					return false;
				}
			},
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

/**
 * 选择申请部门列表
 * @returns
 */
function chooseSqbm(){
	var url=$('#seniorEditBorrowingForm #urlPrefix').val() + "/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
	$.showDialog(url,'选择部门',addSqbmConfig);
}
var addSqbmConfig = {
	width		: "800px",
	modalName	:"addSqbmModal",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row = $('#listDepartmentForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					if (sel_row[0].jgdm){
						$('#seniorEditBorrowingForm #bmdm').val(sel_row[0].jgdm);
					}else{
						$('#seniorEditBorrowingForm #bmdm').val("");
						$.alert("U8不存在此部门！");
						return false;
					}
					$('#seniorEditBorrowingForm #bm').val(sel_row[0].jgid);
					$('#seniorEditBorrowingForm #bmmc').val(sel_row[0].jgmc);
				}else{
					$.error("请选中一行");
					return false;
				}
			},
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
function init(){
	//添加日期控件
	laydate.render({
		elem: '#jyrq'
		,theme: '#2381E9'
	});
}

/**
 * 验证数量格式(两位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("zyxslNumber", function (value, element){
	if(value == "0"){
		$.error("存在物料的允许借用数量为0!");
		return false;
	}else if(value == ""){
		$.error("存在物料的允许借用数量为0!");
		return false;
	}
	return this.optional(element) || /^\d+(\.\d{1,2})?$/.test(value);
},"请填写正确格式！");


$(document).ready(function(){
	//初始化数据
	init();
	//所有下拉框添加choose样式
	jQuery('#seniorEditBorrowingForm .chosen-select').chosen({width: '100%'});
});