function btnBind(){
	var sel_cklb = $("#CkxxListForm #cklb");
	//仓库类别下拉框事件
	sel_cklb.unbind("change").change(function(){
		cklbEvent();
	});
}

function cklbEvent(){
	var cklb=$("#CkxxListForm #cklb").val();
	var cklbdm=$("#"+cklb).attr("csdm");
	if(cklbdm=="KW"){
		$("#CkxxListForm #fcksty").attr("style","color: red;");
		$("#CkxxListForm #fckid").attr("validate","{required:true}");
	}else{
		$("#CkxxListForm #fcksty").attr("style","display:none");
		$("#CkxxListForm #fckid").attr("validate","{required:false}");
	}
	if (cklbdm == "CK"){
		$("#CkxxListForm #ckqx").show()
		$("#CkxxListForm #ckqxlx").attr("validate","{required:true}");
	}else{
		$("#CkxxListForm #ckqx").hide()
		$("#CkxxListForm #ckqxlx").attr("validate","{required:false}");
		$("#CkxxListForm #ckqxlx").val("");
	}
}

$(function(){
	btnBind();
	jQuery('#CkxxListForm .chosen-select').chosen({width: '100%'});
	var cklb=$("#CkxxListForm #cklb").val();
	var cklbdm=$("#"+cklb).attr("csdm");
	if (cklbdm == "CK"){
		$("#CkxxListForm #ckqx").show()
		$("#CkxxListForm #ckqxlx").attr("validate","{required:true}");
	}else{
		$("#CkxxListForm #ckqx").hide()
		$("#CkxxListForm #ckqxlx").attr("validate","{required:false}");
	}
})