/**
 * 实领数量操作
 * @param 
 * @returns
 */
function deliveryout(hwid,hwllid,zyxsl,e){
	var yxsl = $("#deliveryForm #yxsl_"+hwid+"_"+hwllid).text();
	var slsl = $("#deliveryForm #slsl_"+hwid+"_"+hwllid).val();
	var wlbm = $("#deliveryForm #wlbm_"+hwid+"_"+hwllid).text();
	var jy_json = $("#deliveryForm #jy_json").val();
	var ckid = $("#deliveryForm #ckid_"+hwid+"_"+hwllid).text();
	// 初始化jy_json
	var json_t=[];
	if(jy_json!=null && jy_json!=""){
		json_t = JSON.parse(jy_json);
	}
	//去除当前值状态
	for(i = 0; i < json_t.length; i++) {
		if(json_t[i].id==hwid+hwllid){
			json_t.splice(i,1);
		}
	}
	if(parseFloat(yxsl)<parseFloat(slsl)){
		var sz_1 = {"id":'',"message":''};
		sz_1.id = hwid + hwllid;
		sz_1.message = "实领数量不能大于允许领取数量！";
		json_t.push(sz_1);
		$.alert("实领数量不能大于允许领取数量！");
		$("#deliveryForm #jy_json").val(JSON.stringify(json_t));
		return false;
	}
	var hwllmx_json = $("#deliveryForm #hwllmx_json").val();
	// 初始化hwllmx_json
	var json=[];
	if(hwllmx_json!=null && hwllmx_json!=""){
		json = JSON.parse(hwllmx_json);
	}
	var slsl_flo = parseFloat(slsl);
	var slsl_ql = parseFloat(slsl);
	for(j = 0; j < json.length; j++) {
		if(json[j].hwllid==hwllid && json[j].hwid==hwid){
			json.splice(j,1);
		}else{
			if(json[j].hwid==hwid){
				slsl_flo=slsl_flo+parseFloat(json[j].slsl);
			}
			if(json[j].hwllid==hwllid){
				slsl_ql=slsl_ql+parseFloat(json[j].slsl);
			}
		}
	}
	if(slsl_flo>parseFloat(yxsl)){
		var sz_2 = {"id":'',"message":''};
		sz_2.id = hwid + hwllid;
		sz_2.message = "货物实领数量不能大于允许领取数量！";
		json_t.push(sz_2);
		$("#deliveryForm #jy_json").val(JSON.stringify(json_t));
		$.alert(wlbm+"货物实领数量不能大于允许领取数量！");
		return false;
	}
	if(slsl_ql>parseFloat(zyxsl)){
		var sz_3 = {"id":'',"message":''};
		sz_3.id = hwid + hwllid;
		sz_3.message = wlbm+"货物实领数量不能大于总允许领取数量！";
		json_t.push(sz_3);
		$("#deliveryForm #jy_json").val(JSON.stringify(json_t));
		$.alert(wlbm+"货物实领数量不能大于总允许领取数量！");
		return false;
	}
	var sz = {"hwllid":'',"llid":'',"hwid":'',"slsl":'',"yxsl":'',"ck":''};
	sz.hwllid = hwllid;
	sz.llid = $("#deliveryForm #llid").val();
	sz.hwid=hwid;
	sz.yxsl=yxsl;
	sz.slsl=$("#deliveryForm #slsl_"+hwid+"_"+hwllid).val();
	sz.ck=ckid
	json.push(sz);
	$("#deliveryForm #hwllmx_json").val(JSON.stringify(json));
	$("#deliveryForm #jy_json").val(JSON.stringify(json_t));
}


/**
 * 验证数量格式(两位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("slslNumber", function (value, element){
	if(!value){
		$.error("请填写实领数量!");
		return false;
	}else{
		if(!/^\d+(\.\d{1,2})?$/.test(value)){
			$.error("请填写正确实领数量格式，可保留两位小数!");
		}
	}
    return this.optional(element) || /^\d+(\.\d{1,2})?$/.test(value);
},"请填写正确格式，可保留两位小数。");



/**
 * 选择申请人
 * @returns
 */
function chooseFlr(id) {
	$('#deliveryForm #rybj').val(id);
	var url = $('#deliveryForm #urlPrefix').val()+"/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择申请人', chooseQrrConfig);
}
var chooseQrrConfig = { 
	width : "800px",
	height : "500px",
	modalName	: "chooseSqrModal",
	formName	: "deliveryForm",
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
					var id =$('#deliveryForm #rybj').val();
					if(id==1){
						$('#deliveryForm #llr').val(sel_row[0].yhid);
						$('#deliveryForm #llrmc').val(sel_row[0].zsxm);
					}else{
						$('#deliveryForm #flry').val(sel_row[0].yhid);
						$('#deliveryForm #flrmc').val(sel_row[0].zsxm);
					}
					$('#deliveryForm #rybj').val();
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

/**
 * 选择申请部门列表
 * @returns
 */
function chooseSqbm(){
	var url=$('#deliveryForm #urlPrefix').val() + "/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
	$.showDialog(url,'选择申请部门',addSqbmConfig);	
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
						$('#deliveryForm #jgdm').val(sel_row[0].jgdm);
					}else{
						$('#deliveryForm #jgdm').val("");
						$.alert("U8不存在此部门！");
						return false;
					}
					$('#deliveryForm #sqbm').val(sel_row[0].jgid);
					$('#deliveryForm #sqbmmc').val(sel_row[0].jgmc);
					if (sel_row[0].kzcs1){
						let url= $('#deliveryForm #urlPrefix').val() + "/storehouse/requisition/pagedataGenerateLljlbh";
						$.ajax({
							type:'post',
							url:url,
							cache: false,
							data: {"jgdh":sel_row[0].kzcs1,"access_token":$("#ac_tk").val()},
							dataType:'json',
							success:function(data){
								if (data.jlbh){
									$('#deliveryForm #jlbh').val(data.jlbh);
								}
							}
						});
					}else{
						$('#deliveryForm #sqbm').val("");
						$('#deliveryForm #sqbmmc').val("");
						$('#deliveryForm #jgdm').val("");
						$('#deliveryForm #jlbh').val("");
						$.alert("所选部门存在异常！");
						return false;
					}
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
	   elem: '#ckrq'
	  ,theme: '#2381E9'
	});
	
	var json=[];
	var json_t=[];
	var hwllmx_json = $("#deliveryForm #hwllmx_json").val();
	if(hwllmx_json!=null && hwllmx_json!=""){
		json_t = JSON.parse(hwllmx_json);
	}
	for(j = 0; j < json_t.length; j++) {
		var sz = {"hwllid":'',"llid":'',"hwid":'',"slsl":'',"ck":''};
		sz.hwllid = json_t[j].hwllid;
		sz.llid = json_t[j].llid;
		sz.hwid = json_t[j].hwid;
		sz.ck=json_t[j].ckid;
		if(json_t[j].slsl!=null && json_t[j].slsl != ""){
			sz.slsl = json_t[j].slsl;
		}else{
			sz.slsl = "0";
		}
		json.push(sz);
	}
	$("#deliveryForm #hwllmx_json").val(JSON.stringify(json));	
}





$(document).ready(function(){
	//初始化数据
	init();
	//0.初始化fileinput
	var oFileInput = new FileInput();
	//所有下拉框添加choose样式
	jQuery('#deliveryForm .chosen-select').chosen({width: '100%'});
});