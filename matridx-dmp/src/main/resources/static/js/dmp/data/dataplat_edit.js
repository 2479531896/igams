/**
 * 绑定按钮事件
 */
function btnBind(){
	//对接方式
	$("#ajaxForm [name='djfs']").change(function(){
		var _this = $(this);
		
		if(_this.val()=="数据库"){
			//数据库
			$("#ajaxForm #sjk").attr("style","display:block;");
			$("#ajaxForm #jk").attr("style","display:none;");
		}else{
			//接口
			$("#ajaxForm #sjk").attr("style","display:none;");
			$("#ajaxForm #jk").attr("style","display:block;");
		}
	});
	//选择方式
	$("#ajaxForm [name='xzfs']").change(function(){
		var _this = $(this);
		
		if(_this.val()=="0"){
			//现有
			$("#ajaxForm #xy").attr("style","display:block;");
			$("#ajaxForm #xz").attr("style","display:none;");
		}else{
			//新增
			$("#ajaxForm #xy").attr("style","display:none;");
			$("#ajaxForm #xz").attr("style","display:block;");
		}
	});
	//初始化单选按钮
	if($("#ajaxForm [name='djfs']").val()=="数据库"){
		//数据库
		$("#ajaxForm #sjk").attr("style","display:block;");
		$("#ajaxForm #jk").attr("style","display:none;");
	}else{
		//接口
		$("#ajaxForm #sjk").attr("style","display:none;");
		$("#ajaxForm #jk").attr("style","display:block;");
	}
	if($("#ajaxForm [name='xzfs']").val()=="0"){
		//现有
		$("#ajaxForm #xy").attr("style","display:block;");
		$("#ajaxForm #xz").attr("style","display:none;");
	}else{
		//新增
		$("#ajaxForm #xy").attr("style","display:none;");
		$("#ajaxForm #xz").attr("style","display:block;");
	}
	
	//数据库下拉框改变事件
	$("#ajaxForm #sjkid").unbind("change").change(function(){
		sjkxxEvent();
	});
}

/**
 * 数据库下拉框事件
 */
function sjkxxEvent(){
	var sjkid = $("#ajaxForm #sjkid").val();
	if(!sjkid){
		$("#ajaxForm #v_sjklx").text("");
		$("#ajaxForm #v_sjkljc").text("");
		$("#ajaxForm #v_sjkyhm").text("");
	}
	$.ajax({ 
	    type:'post',  
	    url:"/dmp/data/selectSjkxxBySjkid", 
	    cache: false,
	    data: {"sjkid":sjkid,"access_token":$("#ac_tk").val()},  
	    dataType:'json', 
	    success:function(data){
	    	if(data){
	    		$("#ajaxForm #v_sjklx").text(data.sjklx);
				$("#ajaxForm #v_sjkljc").text(data.sjkljc);
				$("#ajaxForm #v_sjkyhm").text(data.sjkyhm);
	    	}
	    }
	});
}


function addZytgf(){
	var url = "/dmp/data/addProvider"
	$.showDialog(url,'新增资源提供方',addProviderConfig);
}

var addProviderConfig = {
	width		: "700px",
	modalName	: "addProviderModal",
	formName	: "provider_ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				
				if(!$("#provider_ajaxForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				
				$("#provider_ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"provider_ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$("#ajaxForm #tgfid").append("<option value='"+responseText['zytgfDto'].tgfid+"'>"+responseText['zytgfDto'].tgfmc+"</option>");
								$("#ajaxForm #tgfid").trigger("chosen:updated");
								$.closeModal(opts.modalName);
							}
						});
					}else if(responseText["status"] == "fail"){
						$.error(responseText["message"],function() {
						});
					} else{
						$.alert(responseText["message"],function() {
						});
					}
				});
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

function init(){
}

$(document).ready(function(){
	btnBind();
	init();
	//所有下拉框添加choose样式
	jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
});