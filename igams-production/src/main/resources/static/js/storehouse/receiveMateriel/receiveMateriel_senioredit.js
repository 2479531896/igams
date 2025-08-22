/**
 * 点击显示文件上传
 * @returns
 */
function editfile(){
	$("#senioreditPickingForm #fileDiv").show();
	$("#senioreditPickingForm #file_btn").hide();
}
/**
 * 点击隐藏文件上传
 * @returns
 */
function cancelfile(){
	$("#senioreditPickingForm #fileDiv").hide();
	$("#senioreditPickingForm #file_btn").show();
}

/**
 * 点击图片预览
 * @param fjid
 * @param wjm
 * @returns
 */
function view(fjid,wjm){
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin,end);
	if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
		var url=$("#senioreditPickingForm #urlPrefix").val()+"/ws/sjxxpripreview/?fjid="+fjid
		$.showDialog(url,'图片预览',JPGMaterConfig);
	}else if(type.toLowerCase()==".pdf"){
		var url= $("#senioreditPickingForm #urlPrefix").val()+"/common/file/pdfPreview?fjid=" + fjid;
        $.showDialog(url,'文件预览',JPGMaterConfig);
	}else {
		$.alert("暂不支持其他文件的预览，敬请期待！");
	}
}
var JPGMaterConfig = {
	width		: "800px",
	offAtOnce	: true,  
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
/**
 * 点击文件下载
 * @param fjid
 * @returns
 */
function xz(fjid){
    jQuery('<form action='+$("#senioreditPickingForm #urlPrefix").val()+'"/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
            '<input type="text" name="fjid" value="'+fjid+'"/>' + 
            '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' + 
        '</form>')
    .appendTo('body').submit().remove();
}
/**
 * 点击删除文件
 * @param fjid
 * @param wjlj
 * @returns
 */
function del(fjid,wjlj){
	$.confirm('您确定要删除所选择的记录吗？',function(result){
		if(result){
			jQuery.ajaxSetup({async:false});
			var url= $("#senioreditPickingForm #urlPrefix").val()+"/common/file/delFile";
			jQuery.post(url,{fjid:fjid,wjlj:wjlj,"access_token":$("#ac_tk").val()},function(responseText){
				setTimeout(function(){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							$("#"+fjid).remove();
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
/**
 * 附件上传回调
 * @param fjid
 * @returns
 */
function displayUpInfo(fjid){
	if(!$("#senioreditPickingForm #fjids").val()){
		$("#senioreditPickingForm #fjids").val(fjid);
	}else{
		$("#senioreditPickingForm #fjids").val($("#senioreditPickingForm #fjids").val()+","+fjid);
	}
}


/**
 * 实领数量操作
 * @param 
 * @returns
 */
function out(hwid,hwllid,qlsl,e){
	var klsl = $("#senioreditPickingForm #klsl_"+hwid+"_"+hwllid).text();
	var yxsl = $("#senioreditPickingForm #yxsl_"+hwid+"_"+hwllid).val();
	var wlbm = $("#senioreditPickingForm #wlbm_"+hwid+"_"+hwllid).text();
	var jy_json = $("#senioreditPickingForm #jy_json").val();
	var qyxsl = $("#senioreditPickingForm #qyxsl_"+hwid+"_"+hwllid).text();
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
	if(parseFloat(klsl)<parseFloat(yxsl)){
		var sz_1 = {"id":'',"message":''};
		sz_1.id = hwid + hwllid;
		sz_1.message = "允许领取数量不能大于可领数量！";
		json_t.push(sz_1);
		$.alert("允许领取不能大于可领数量！");
		$("#senioreditPickingForm #jy_json").val(JSON.stringify(json_t));
		return false;
	}
	var hwllmx_json = $("#senioreditPickingForm #hwllmx_json").val();
	// 初始化hwllmx_json
	var json=[];
	if(hwllmx_json!=null && hwllmx_json!=""){
		json = JSON.parse(hwllmx_json);
	}
	var yxsl_flo = parseFloat(yxsl);
	var yxsl_ql = parseFloat(yxsl);
	for(let j = 0; j < json.length; j++) {
		if(json[j].hwllid==hwllid && json[j].hwid==hwid){
			json.splice(j,1);
		}else{
			if(json[j].hwid==hwid){
				yxsl_flo=yxsl_flo+parseFloat(json[j].yxsl);
			}
			if(json[j].hwllid==hwllid){
				yxsl_ql=yxsl_ql+parseFloat(json[j].yxsl);
			}
		}
	}
	if(yxsl_flo>parseFloat(klsl)){
		var sz_2 = {"id":'',"message":''};
		sz_2.id = hwid + hwllid;
		sz_2.message = "货物允许领取数量不能大于可领数量！";
		json_t.push(sz_2);
		$("#senioreditPickingForm #jy_json").val(JSON.stringify(json_t));
		$.alert(wlbm+"货物允许领取数量不能大于可领数量！");
		return false;
	}
	if(yxsl_ql>parseFloat(qlsl)){
		var sz_3 = {"id":'',"message":''};
		sz_3.id = hwid + hwllid;
		sz_3.message = wlbm+"货物允许领取数量不能大于请领数量！";
		json_t.push(sz_3);
		$("#senioreditPickingForm #jy_json").val(JSON.stringify(json_t));
		$.alert(wlbm+"货物允许领取数量不能大于请领数量！");
		return false;
	}
	var sz = {"hwllid":'',"llid":'',"hwid":'',"yxsl":'',"qlsl":'',"qyxsl":''};
	sz.hwllid = hwllid;
	sz.llid = $("#senioreditPickingForm #llid").val();
	sz.hwid=hwid;
	sz.yxsl=$("#senioreditPickingForm #yxsl_"+hwid+"_"+hwllid).val();
	sz.qlsl = qlsl;
	if(qyxsl != null && qyxsl != ""){
		sz.qyxsl = qyxsl;
	}else{
		sz.qyxsl = "0";
	}
	json.push(sz);
	$("#senioreditPickingForm #hwllmx_json").val(JSON.stringify(json));
	$("#senioreditPickingForm #jy_json").val(JSON.stringify(json_t));
}


/**
 * 验证数量格式(两位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("yxslNumber", function (value, element){
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
function chooseSqr() {
	var url = $('#senioreditPickingForm #urlPrefix').val()+"/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择申请人', chooseQrrConfig);
}
var chooseQrrConfig = { 
	width : "800px",
	height : "500px",
	modalName	: "chooseSqrModal",
	formName	: "senioreditPickingForm",
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
					$('#senioreditPickingForm #sqry').val(sel_row[0].yhid);
					$('#senioreditPickingForm #sqrmc').val(sel_row[0].zsxm);
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
	var url=$('#senioreditPickingForm #urlPrefix').val() + "/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
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
						$('#senioreditPickingForm #jgdm').val(sel_row[0].jgdm);
					}else{
						$('#senioreditPickingForm #jgdm').val("");
						$.alert("U8不存在此部门！");
						return false;
					}
					$('#senioreditPickingForm #sqbm').val(sel_row[0].jgid);
					$('#senioreditPickingForm #sqbmmc').val(sel_row[0].jgmc);
					if (sel_row[0].kzcs1){
						let url= $('#senioreditPickingForm #urlPrefix').val() +"/storehouse/requisition/pagedataGenerateLljlbh";
						$.ajax({
							type:'post',
							url:url,
							cache: false,
							data: {"jgdh":sel_row[0].kzcs1,"access_token":$("#ac_tk").val()},
							dataType:'json',
							success:function(data){
								if (data.jlbh){
									$('#senioreditPickingForm #jlbh').val(data.jlbh);
								}
							}
						});
					}else{
						$('#senioreditPickingForm #sqbm').val("");
						$('#senioreditPickingForm #sqbmmc').val("");
						$('#senioreditPickingForm #jgdm').val("");
						$('#senioreditPickingForm #jlbh').val("");
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
	var json=[];
	var json_t=[];
	var hwllmx_json = $("#senioreditPickingForm #hwllmx_json").val();
	if(hwllmx_json!=null && hwllmx_json!=""){
		json_t = JSON.parse(hwllmx_json);
	}
	for(var j = 0; j < json_t.length; j++) {
		var sz = {"hwllid":'',"llid":'',"hwid":'',"yxsl":'',"qlsl":'',"qyxsl":''};
		sz.hwllid = json_t[j].hwllid;
		sz.llid = json_t[j].llid;
		sz.hwid = json_t[j].hwid;
		if(json_t[j].yxsl!=null && json_t[j].yxsl != ""){
			sz.yxsl = json_t[j].yxsl;
			sz.qyxsl = json_t[j].yxsl;
		}else{
			sz.yxsl = "0";
			sz.qyxsl = "0";
		}
		sz.qlsl = json_t[j].qlsl;
		// sz.qwfs = json_t[j].qwfs;
		json.push(sz);
	}
	$("#senioreditPickingForm #hwllmx_json").val(JSON.stringify(json));	
}
/**
 * 选择归属人
 * @returns
 */
function chooseBmsbfzr() {
	var url = $('#senioreditPickingForm #urlPrefix').val()+"/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择部门设备负责人', chooseBmsbfzrConfig);
}
var chooseBmsbfzrConfig = {
	width : "800px",
	height : "500px",
	modalName	: "chooseBmsbfzrModal",
	formName	: "senioreditPickingForm",
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
					$('#senioreditPickingForm #bmsbfzr').val(sel_row[0].yhid);
					$('#senioreditPickingForm #bmsbfzrmc').val(sel_row[0].zsxm);
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
// //去污方式修改事件
// $('#senioreditPickingForm .qwfs').change(function (e) {
// 	var split = this.id.split("_");
// 	var hwid = split[1];
// 	var hwllid = split[2];
// 	var qwfs = $(this).val();
// 	var json_t=[];
// 	var hwllmx_json = $("#senioreditPickingForm #hwllmx_json").val();
// 	if(hwllmx_json!=null && hwllmx_json!=""){
// 		json_t = JSON.parse(hwllmx_json);
// 	}
// 	for(var j = 0; j < json_t.length; j++) {
// 		if (json_t[j].hwid==hwid&&json_t[j].hwllid==hwllid){
// 			json_t[j].qwfs = qwfs;
// 		}
// 	}
// 	$("#senioreditPickingForm #hwllmx_json").val(JSON.stringify(json_t));
// });
function modSbys(hwid){
	var url=$("#senioreditPickingForm #urlPrefix").val()+"/device/device/modDeviceCheck?hwid="+hwid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'修改设备验收信息',editDeviceCheckConfig);
}
var editDeviceCheckConfig = {
	width		: "1600px",
	modalName	: "editDeviceCheckModel",
	formName	: "editDeviceCheckForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if (!$("#editDeviceCheckForm").valid()) {
					$.alert("请填写完整信息");
					return false;
				}
				var hwid = $("#editDeviceCheckForm #hwid").val();
				if(hwid){
				    if($("#editDeviceCheckForm #xlh").val()){
                       document.getElementById(hwid+"xlh").innerText=$("#editDeviceCheckForm #xlh").val();
                    }
                    if($("#editDeviceCheckForm #sbccbh").val()){
                       document.getElementById(hwid+"sbccbh").innerText=$("#editDeviceCheckForm #sbccbh").val();
                    }
                    if($("#editDeviceCheckForm #gdzcbh").val()){
                        document.getElementById(hwid+"gdzcbh").innerText=$("#editDeviceCheckForm #gdzcbh").val();
                    }
                    if($("#editDeviceCheckForm #sbbh").val()){
                        document.getElementById(hwid+"sbbh").innerText=$("#editDeviceCheckForm #sbbh").val();
                    }
				}

				var $this = this;
				var opts = $this["options"]||{};

				$("#editDeviceCheckForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"editDeviceCheckForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
						});
					}else if(responseText["status"] == "repetition"){
						preventResubmitForm(".modal-footer > button", false);
						$.error("固定资产编号或设备编号重复！",function() {
						});
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						$.error(responseText["message"],function() {
						});
					} else{
						preventResubmitForm(".modal-footer > button", false);
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



$(document).ready(function(){
	//初始化数据
	init();
	//0.初始化fileinput
	var oFileInput = new FileInput();
	var seniorpicking_params=[];
	seniorpicking_params.prefix=$("#senioreditPickingForm #urlPrefix").val();
	oFileInput.Init("senioreditPickingForm","displayUpInfo",2,1,"pro_file",null,seniorpicking_params);
	//所有下拉框添加choose样式
	jQuery('#senioreditPickingForm .chosen-select').chosen({width: '100%'});
});