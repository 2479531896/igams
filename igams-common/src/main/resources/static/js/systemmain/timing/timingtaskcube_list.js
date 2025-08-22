function runDsrw(rwid){
	var tourl = "/systemmain/timing/executeTimingTask";
	$.confirm('您确定要执行所选择的方法吗？',function(result){
		if(result){
			jQuery.ajaxSetup({async:false});
			var url= $("#timingtaskcubelist_formSearch #urlPrefix").val()+tourl;
			jQuery.post(url,{rwid:rwid,"access_token":$("#ac_tk").val()},function(responseText){
				setTimeout(function(){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
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

$(document).ready(function(){
    //所有下拉框添加choose样式
	jQuery('timingtaskcubelist_formSearch .chosen-select').chosen({width: '100%'});
});