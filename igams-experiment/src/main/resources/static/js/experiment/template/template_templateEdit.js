
$(function(){
	  //默认图标
  var yslx=$("#ys").val();
  var mbtb=$("#tb").val();
  if(yslx!=null && mbtb!=null){
	  //选中默认颜色和图标
	  $("#"+mbtb).attr("id","selectid");
	  if(yslx=="ec8a89"){
		  $("input[name=mbys]:eq(0)").attr("checked",'checked'); 
		  $("#modlist").attr("style","display:block;color:#ec8a89;font-size:30px;");
		  $("#selectid").attr("style","background-color:#ec8a89;color:#fff;border-radius: 8px;");
	  }else if(yslx=="e4cc7f"){
		  $("input[name=mbys]:eq(1)").attr("checked",'checked');
		  $("#modlist").attr("style","display:block;color:#e4cc7f;font-size:30px;");
		  $("#selectid").attr("style","background-color:#e4cc7f;color:#fff;border-radius: 8px;");
	  }else if(yslx=="c3db8b"){
		  $("input[name=mbys]:eq(2)").attr("checked",'checked');
		  $("#modlist").attr("style","display:block;color:#c3db8b;font-size:30px;");
		  $("#selectid").attr("style","background-color:#c3db8b;color:#fff;border-radius: 8px;");
	  }else if(yslx=="93d7d8"){
		  $("input[name=mbys]:eq(3)").attr("checked",'checked');
		  $("#modlist").attr("style","display:block;color:#93d7d8;font-size:30px;");
		  $("#selectid").attr("style","background-color:#93d7d8;color:#fff;border-radius: 8px;");
	  }else if(yslx=="989eeb"){
		  $("input[name=mbys]:eq(4)").attr("checked",'checked');
		  $("#modlist").attr("style","display:block;color:#989eeb;font-size:30px;");
		  $("#selectid").attr("style","background-color:#989eeb;color:#fff;border-radius: 8px;");
	  }else if(yslx=="efa9db"){
		  $("input[name=mbys]:eq(5)").attr("checked",'checked');
		  $("#modlist").attr("style","display:block;color:#efa9db;font-size:30px;");
		  $("#selectid").attr("style","background-color:#efa9db;color:#fff;border-radius: 8px;");
	  }else{
		  $("input[name=mbys]:eq(6)").attr("checked",'checked');
		  $("#modlist").attr("style","display:block;color:#b9c0c7;font-size:30px;"); 
		 $("#selectid").attr("style","background-color:#b9c0c7;color:#fff;border-radius: 8px;");
	  }
	  //点击单选框单出图标样式选择
	  $(":radio").click(function (){
		  var ys=$(this).val();
	   if($(this).val()=='ec8a89'){
		   $("#modlist").attr("style","display:block;color:#ec8a89;font-size:30px;");
		   $("#selectid").attr("style","background-color:#ec8a89;color:#fff;border-radius: 8px;");
	   }else if($(this).val()=='e4cc7f'){
		   $("#modlist").attr("style","display:block;color:#e4cc7f;font-size:30px;");
		   $("#selectid").attr("style","background-color:#e4cc7f;color:#fff;border-radius: 8px;");	
	   }else if($(this).val()=='c3db8b'){
		   $("#modlist").attr("style","display:block;color:#c3db8b;font-size:30px;");
		   $("#selectid").attr("style","background-color:#c3db8b;color:#fff;border-radius: 8px;");	
	   }else if($(this).val()=='93d7d8'){
		   $("#modlist").attr("style","display:block;color:#93d7d8;font-size:30px;");
		   $("#selectid").attr("style","background-color:#93d7d8;color:#fff;border-radius: 8px;");	
	   }else if($(this).val()=='989eeb'){
		   $("#modlist").attr("style","display:block;color:#989eeb;font-size:30px;");
		   $("#selectid").attr("style","background-color:#989eeb;color:#fff;border-radius: 8px;");	
	   }else if($(this).val()=='efa9db'){
		   $("#modlist").attr("style","display:block;color:#efa9db;font-size:30px;");
		   $("#selectid").attr("style","background-color:#efa9db;color:#fff;border-radius: 8px;");	
	   }else{
		   $("#modlist").attr("style","display:block;color:#b9c0c7;font-size:30px;"); 
		   $("#selectid").attr("style","background-color:#b9c0c7;color:#fff;border-radius: 8px;");	
	   }
	   $("#ys").val(ys);
	  });
  }
 });
//点击图标样式改变选中状态
function tbys(pp){
	var yslx=$("#ys").val();
	$(".tbys").attr("id",null);
	$(".tbys").removeAttr("style");
	$(pp).attr("id","selectid");
	if(yslx=='ec8a89'){
		$("#selectid").attr("style","background-color:#ec8a89;color:#fff;border-radius: 8px;");
	}else if(yslx=='e4cc7f'){
		$("#selectid").attr("style","background-color:#e4cc7f;color:#fff;border-radius: 8px;");	
	}else if(yslx=='c3db8b'){
		$("#selectid").attr("style","background-color:#c3db8b;color:#fff;border-radius: 8px;");	
	}else if(yslx=='93d7d8'){
		$("#selectid").attr("style","background-color:#93d7d8;color:#fff;border-radius: 8px;");	
	}else if(yslx=='989eeb'){
		$("#selectid").attr("style","background-color:#989eeb;color:#fff;border-radius: 8px;");	
	}else if(yslx=='efa9db'){
		$("#selectid").attr("style","background-color:#efa9db;color:#fff;border-radius: 8px;");	
	}else{
		$("#selectid").attr("style","background-color:#b9c0c7;color:#fff;border-radius: 8px;");	
	}
}
	