function settemplate(){
	$("#set_template").attr("style","display:none;");
	$("#newTemplate").attr("style","display:block;text-align:center;position: relative;top:50%;transform:translateY(-50%);");
}
function cancleTemplate(){
	$("#newTemplate").attr("style","display:none;");
	$("#set_template").attr("style","display:block;height:100%;");
}
function viewTemplate(mbid){

	var url="/experiment/template/pagedataTemplateListStage?mbid="+mbid;
	$("#disdiv").load(url);
}
function movein(mbid){
	$("#"+mbid+"1").attr("style","display:block;position:absolute;right:15px;top:0;padding:5px;");
	$("#"+mbid+"2").attr("style","display:block;position:absolute;right:40px;top:0;padding:5px;");
	$("#"+mbid+"3").attr("style","display:block;position:absolute;right:65px;top:0;padding:5px;color:#f5941d;");
}
function moveout(mbid){
	$("#"+mbid+"1").attr("style","display:none;");
	$("#"+mbid+"2").attr("style","display:none;");
	$("#"+mbid+"3").attr("style","display:none;");
}
function saveTemplate(){
	$.ajax({ 
	    type:'post',  
	    url:"/experiment/template/pagedataAddSaveTemplate",
	    cache: false,
	    data: {"mbmc":$("#templateid").val(),"access_token":$("#ac_tk").val()},  
	    dataType:'json', 
	    success:function(data){
	    	//返回值
	    	if(data.status=="fail"){
	    		$.confirm(data.message);
	    	}else{
	    		$("#disdiv").load("/experiment/template/pagedataTemplateListStage?mbid="+data.mbglDto.mbid);
	    	}	
	    }
	});
}
//删除模板
function deletemod(mbid){
	event.stopPropagation();
	$.confirm("您确定要删除所选择的模板吗？",function(result){
		if(result){
			$.ajax({ 
			    type:'post',  
			    url:"/experiment/template/pagedataDelTemplate",
			    cache: false,
			    data: {"mbid":mbid,"access_token":$("#ac_tk").val()},  
			    dataType:'json', 
			    success:function(){
			    	$.confirm("删除成功",function(){
			    		$("#disdiv").load("/experiment/template/pagedataTemplateList");
			    	});
			    }
			});	
		}
	});
}
//编辑模板
function editmod(mbid){
	event.stopPropagation();
	var url="/experiment/template/pagedataEditTemplateTask?mbid="+mbid;
		$.showDialog(url,'编辑模板',editTemplateTaskConfig);
}

var editTemplateTaskConfig = {
		width		: "800px",
		modalName	: "editTemplateTaskModal",
		formName	: "editTemplateForm",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {	
					if(!$("#editTemplateForm").valid()){
						return false;
					}
					var mbtbclass=$("#selectid").attr('class');
					var strs= new Array();
					strs=mbtbclass.split(" ");
					var mbtb=strs[1];
					jQuery('<input type="hidden" name="mbtb" value="'+mbtb+'"/>').appendTo('form');
					var $this = this;
					var opts = $this["options"]||{};
					$("#editTemplateForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"editTemplateForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
									//刷新页面
									$("#disdiv").load("/experiment/template/pagedataTemplateList");
								}
							});
						}else if(responseText["status"] == "fail"){
							preventResubmitForm(".modal-footer > button", false);
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

$('#templateid').on('keydown', function(event) {
    if (event.keyCode == "13") {
    	saveTemplate();
    	return false;
    }
});
    


