var libPreInput;
var libSecondPreInput;
$("#dockingPCR_Form input").on('keydown',function(e){
	if(libSecondPreInput==17 &&libPreInput==16 && e.keyCode==74){
		if($(this).val()=="")
			return false;
		var thisxh=$(this).attr("xh");
		var str = $("input[xh='"+thisxh+"']").val();
		re = new RegExp(",","g"); 
		rq = new RegExp("，","g");
		var Newstr = str.replace(re, "");
		Newstr = Newstr.replace(rq,"");
		$("input[xh='"+thisxh+"']").val(Newstr);
    	$(this).val($(this).val().toUpperCase());
        if($(this).val().length >0){
        	var nextxh=parseInt(thisxh)+1;
	    	if(nextxh==97){
	    		$("input[xh='1']").focus();
	    	}else{
				$("input[xh='"+nextxh+"']").focus();
	    	}
        }
	}else if (e.keyCode == 13) {
		if($(this).val()=="")
			return false;
    	var thisxh=$(this).attr("xh");
    	var str = $("input[xh='"+thisxh+"']").val();
    	re = new RegExp(",","g"); 
    	rq = new RegExp("，","g");
    	var Newstr = str.replace(re, "");
    	Newstr = Newstr.replace(rq,"");
    	$("input[xh='"+thisxh+"']").val(Newstr);
    	$(this).val($(this).val().toUpperCase());
    	var nextxh=parseInt(thisxh)+1;
    	if(nextxh==97){
    		$("input[xh='1']").focus();
    	}else{
			$("input[xh='"+nextxh+"']").focus();
    	}
    }else if(e.keyCode != 13 && e.keyCode != 16&& e.keyCode != 17&& e.keyCode != 74&& e.keyCode != 37){
    	return true;
    }
	libSecondPreInput = libPreInput
	libPreInput = e.keyCode
	return false;
});

$(function(){
	var ids=$("#dockingPCRYz_Form #ids").val();
	$.ajax({
		type:'post',
		url:"/verification/verification/pagedataSjyzList",
		cache: false,
		data: {"ids":ids,"access_token":$("#ac_tk").val()},
		dataType:'json',
		success:function(data){
			var listMTM=new Array();
			var listACP=new Array();
			for (var i=0; i<data.length; i++){
				var nbbm = data[i].nbbm;
				if (data[i].qfdm == 'P'){//A正常 P去人源
					nbbm = nbbm + "-REM";
				}
				if (data[i].yzdm=='ACP'){//耶氏肺孢子
					listACP.push(nbbm);
				}else if(data[i].yzdm='MTM'){//结核非结核
					listMTM.push(nbbm);
				}
			}
			for (var i=0; i<listMTM.length; i++){
				$("#dockingPCRYz_Form input[xh='"+(i+1)+"']").val(listMTM[i]);
				$("#dockingPCRYz_Form input[xh='"+(i+1)+"']").attr("title",listMTM[i]);
			}
			for (var i=49,j=0; j<listACP.length; i++,j++){
				$("#dockingPCRYz_Form input[xh='"+i+"']").val(listACP[j]);
				$("#dockingPCRYz_Form input[xh='"+i+"']").attr("title",listACP[j]);
			}
			//MTM阴性和阳性直接固定位置展示
			$("#dockingPCRYz_Form input[xh=33]").val("NC-MTM");
			$("#dockingPCRYz_Form input[xh=33]").attr("title","NC-MTM");
			$("#dockingPCRYz_Form input[xh=34]").val("NC-MTM");
			$("#dockingPCRYz_Form input[xh=34]").attr("title","NC-MTM");
			$("#dockingPCRYz_Form input[xh=39]").val("NC-MTM");
			$("#dockingPCRYz_Form input[xh=39]").attr("title","NC-MTM");
			$("#dockingPCRYz_Form input[xh=40]").val("NC-MTM");
			$("#dockingPCRYz_Form input[xh=40]").attr("title","NC-MTM");
			$("#dockingPCRYz_Form input[xh=41]").val("PC-MTM");
			$("#dockingPCRYz_Form input[xh=41]").attr("title","PC-MTM");
			$("#dockingPCRYz_Form input[xh=42]").val("PC-MTM");
			$("#dockingPCRYz_Form input[xh=42]").attr("title","PC-MTM");
			$("#dockingPCRYz_Form input[xh=47]").val("PC-MTM");
			$("#dockingPCRYz_Form input[xh=47]").attr("title","PC-MTM");
			$("#dockingPCRYz_Form input[xh=48]").val("PC-MTM");
			$("#dockingPCRYz_Form input[xh=48]").attr("title","PC-MTM");
			//ACP阴性及阳性直接固定位置展示
			$("#dockingPCRYz_Form input[xh=81]").val("NC-ACP");
			$("#dockingPCRYz_Form input[xh=81]").attr("title","NC-ACP");
			$("#dockingPCRYz_Form input[xh=82]").val("NC-ACP");
			$("#dockingPCRYz_Form input[xh=82]").attr("title","NC-ACP");
			$("#dockingPCRYz_Form input[xh=87]").val("NC-ACP");
			$("#dockingPCRYz_Form input[xh=87]").attr("title","NC-ACP");
			$("#dockingPCRYz_Form input[xh=88]").val("NC-ACP");
			$("#dockingPCRYz_Form input[xh=88]").attr("title","NC-ACP");
			$("#dockingPCRYz_Form input[xh=89]").val("PC-ACP");
			$("#dockingPCRYz_Form input[xh=89]").attr("title","PC-ACP");
			$("#dockingPCRYz_Form input[xh=90]").val("PC-ACP");
			$("#dockingPCRYz_Form input[xh=90]").attr("title","PC-ACP");
			$("#dockingPCRYz_Form input[xh=95]").val("PC-ACP");
			$("#dockingPCRYz_Form input[xh=95]").attr("title","PC-ACP");
			$("#dockingPCRYz_Form input[xh=96]").val("PC-ACP");
			$("#dockingPCRYz_Form input[xh=96]").attr("title","PC-ACP");
		}
	});
})
