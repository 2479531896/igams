var libPreInput;
var libSecondPreInput;
var libThreePreInput;
$("#print_formSearch #nbbm").on('keydown',function(e){
	var result = true;
	if(e.keyCode==13){
		getSjxx();
		result=false;
	}else if(libSecondPreInput==17 &&libPreInput==16 && e.keyCode==74){
		if(libThreePreInput == 13)
			return false;
		getSjxx();
		result = false;
	}
	libThreePreInput = libSecondPreInput
	libSecondPreInput = libPreInput
	libPreInput = e.keyCode
	if(!result)
		return result;
})

function getSjxx(){
	var nbbm=$("#print_formSearch #nbbm").val();
	var print_num=$("#print_formSearch #print_num").val();
	if(print_num!=null){
		if(print_num>0){
			$.ajax({
				type : "POST",
				url :"/inspection/inspection/print_Nbbm",
				data : {"nbbm":nbbm,"print_num":print_num,"access_token":$("#ac_tk").val()},
				dataType : "json",
				success : function(data){
					if(data.sjxxDto){
						if(data.sjxxDto.hzxm){
							$("#print_formSearch #hzxm").text(data.sjxxDto.hzxm);
						}
						if(data.sjxxDto.nl){
							$("#print_formSearch #nl").text(data.sjxxDto.nl);
						}
						if(data.sjxxDto.xbmc){
							$("#print_formSearch #xbmc").text(data.sjxxDto.xbmc);
						}
						if(data.sjxxDto.sjdw){
							$("#print_formSearch #sjdw").text(data.sjxxDto.hospitalname);
						}
						if(data.sjxxDto.sjys){
							$("#print_formSearch #sjys").text(data.sjxxDto.sjys);
						}
						if(data.sjxxDto.zyh){
							$("#print_formSearch #zyh").text(data.sjxxDto.zyh);
						}
						if(data.sjxxDto.sjid){
							$("#print_formSearch #sjid").val(data.sjxxDto.sjid);
						}
						if(data.sjxxDto.nbbm){
							$("#print_formSearch #nbbm").val(data.sjxxDto.nbbm);
						}
						if(data.sjxxDto.yblxmc){
							$("#print_formSearch #yblxmc").text(data.sjxxDto.yblxmc);
						}
						if(data.sjxxDto.bz){
							$("#print_formSearch #bz").val(data.sjxxDto.bz);
						}
						if(data.sjxxDto.jstj){
							$("#print_formSearch #jstj").val(data.sjxxDto.jstj);
						}
						if(data.sjxxDto.ksmc){
							$("#print_formSearch #ks").text(data.sjxxDto.ksmc);
						}
						if(data.sjxxDto.db){
							$("#print_formSearch #db").text(data.sjxxDto.db);
						}
						if(data.sjxxDto.ybtj){
							$("#print_formSearch #ybtj").text(data.sjxxDto.ybtj);
						}
						if(data.sjxxDto.jcxmmc){
							$("#print_formSearch #jcxm").text(data.sjxxDto.jcxmmc);
						}
						if(data.sjxxDto.dh){
							$("#print_formSearch #dh").text(data.sjxxDto.dh);
						}
						if(data.sjxxDto.ysdh){
							$("#print_formSearch #ysdh").text(data.sjxxDto.ysdh);
						}
						if(data.sjxxDto.jqyy){
							$("#print_formSearch #jqyy").text(data.sjxxDto.jqyy);
						}
						if(data.sjxxDto.lczz){
							$("#print_formSearch #lczz").text(data.sjxxDto.lczz);
						}
						if(data.sjxxDto.qqjc){
							$("#print_formSearch #qqjcmc").text(data.sjxxDto.qqjc);
						}
						if(data.sjxxDto.qqzd){
							$("#print_formSearch #qqzd").text(data.sjxxDto.qqzd);
						}
						if(data.sjxxDto.gzbymc){
							$("#print_formSearch #gzbymc").text(data.sjxxDto.gzbymc);
						}
						$("#print_formSearch #nbbm").val("");
						print_nbbm(data.sjxxDto.print_demise_ip);
					}
				}
			})
		}else{
			$.alert("请确认打印份数");
			$("#print_formSearch #nbbm").val("");
		}
	}else{
		$.alert("请设置打印份数！");
	}
}

function print_nbbm(host){
	var print_url=null
	print_url="http://localhost"+host;
	var openWindow = window.open(print_url);
	setTimeout(function(){
		openWindow.close();
	}, 600);
}

$(function(){
	setTimeout(function(){
		$("#print_formSearch #nbbm").focus();
	},100);
	
})