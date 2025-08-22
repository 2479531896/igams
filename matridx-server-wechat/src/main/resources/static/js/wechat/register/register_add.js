function save(){
	var flg=0;
	var message="";
	$("#registerForm .row input").each(function(){
		if($(this).attr("sfbt")=="1"){
			if(this.value==""){
				flg++;
				var xsmc=$(this).parent().prev().children().eq(0).text()+",";
				message=message+xsmc;
			}
		}
	})
	if(flg=="0"){
		$.ajax({
			url:'/wechat/register/saveRegister',
			type:'post',
			dataType:'JSON',
			data:$("#registerForm").serialize(),
			success:function(map){
				if(map.state=="success"){
					$("#btn_save").attr("disabled","disabled");
					$("#btn_save").css("background-color","#DDDDDD")
					document.getElementById("registerForm").submit();
				}else if(map.state=="error"){
					 $("#myModal .modal-body").html("注册失败！");
					 $("#myModal").modal('show');
				}
			}
		})
	}else{
		$("#myModal .modal-body").html(message+"不能为空！");
		$("#myModal").modal('show');
	}
}
function getXxdj(){
	var xxlx=$("#registerForm #xxlx").val();
	var wxid=$("#registerForm #wxid").val();
	$.ajax({
		url:'/wechat/register/getXxdj',
		type:'post',
		dataType:'JSON',
		data:{"xxlx":xxlx,"wxid":wxid},
		success:function(map){
			if(map!=null){
				var ids=[];
				$("#registerForm .row input").each(function(){
					var id=this.id;
					var value="map."+id;
					this.value=eval(value);
				})
			}
		}
	})
}
$(function(){
	getXxdj();
	var width = $(window).width();
	var height = width*2.11;1.0339
	if(height*0.49 < 450 && width < 340){
		height = height - height*0.49 + 470;
	}
	$("#panle").height(height+"px");
	$("#myModal").modal('hide');
})
