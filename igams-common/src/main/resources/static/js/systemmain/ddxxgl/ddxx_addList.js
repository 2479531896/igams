/*$("#addListForm #tzlx").on('change',function(){
	var tzlx=$("#addListForm #tzlx").val();
	$.ajax({
		url:'/common/ddxxgl/sel_Tzlx',
		type:'post',
		dataType:'JSON',
		data:{"tzlx":tzlx,"access_token":$("#ac_tk").val()},
		success:function(data){
			
		}
	})
})*/
$(function(){
	jQuery('#addListForm .chosen-select').chosen({width: '100%'});
})