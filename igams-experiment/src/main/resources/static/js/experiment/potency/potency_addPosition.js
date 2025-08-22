$("#positionAddForm .connect").click(function(){
	var zt=this.checked;
	var szval=$(this).val();
	var zmval=$("#positionAddForm #choose").val();
	var jtval=zmval+szval;
	var thisclass=$(this).attr("class").replace("connect ","");
	if($("#positionAddForm #flag").val()=="1" && $("#positionAddForm #positionnr").children('div').length-1>0 && zt==true){
		$(this).prop("checked",false);
	}else{
		if(zt==false){
			$("#positionAddForm #"+thisclass).prop("checked",false);
			$("#positionAddForm #"+jtval).remove();
		}
		if(zt==true){
			if($("#positionAddForm #positionnr").children('div').length-1<10){
				$("#positionAddForm #positionnr").append("<div id='"+jtval+"' class='col-sm-1 col-md-1 col-xs-3 yxjt "+zmval+"' style='padding-left:5px;padding-right:5px;border:1px solid #B0DCF9;border-radius:3px;margin-left:5px;display:block;'><a href='javascript:void(0)' id='"+szval+"' onclick='qxxz(this)'><span style='font-size:12px;'>"+jtval+"×</span></a></div>");
				var xzsl=0;
				for(var i=0;i<$("#positionAddForm ."+thisclass).length;i++){
					if($("#positionAddForm ."+thisclass)[i].checked==true){
						xzsl++
					}
				}
				if(xzsl==10){
					$("#positionAddForm #"+thisclass).prop("checked",true);
				}
			}else{
				$.confirm("已选接头不得超过10个!");
				$(this).prop("checked",false);
			}
		}
	}
});

$("#positionAddForm #choose").change(function(){
	var zmval=$("#positionAddForm #choose").val();
	var jtqlist=$("#positionAddForm ."+zmval).length;
	$("#positionAddForm .connect").prop("checked",false);
	if(jtqlist>0){
		for(var i=0;i<jtqlist;i++){
			var id=($("#positionAddForm ."+zmval)[i].id).replace(zmval,"l");
			$("#positionAddForm #"+id).prop("checked",true);
		}
	}else{
		$("#positionAddForm .lqx").prop("checked",false);
	}
})

function qxxz(a){
	var sftl=0;
	var conid="l"+a.id;
	var fysid=$("#positionAddForm #"+a.id).parent().attr("id");
	if(fysid==($("#positionAddForm #choose").val()+a.id)){
		$("#positionAddForm #"+conid).prop("checked",false);
	}
	$("#positionAddForm #"+fysid).remove();
	if ($("#positionAddForm #"+("l"+fysid.substr(1,2))).attr("class")){
		var sclie=$("#positionAddForm #"+("l"+fysid.substr(1,2))).attr("class").replace("connect ","");
		var yxlength=$("#positionAddForm .yxjt").length;
		for(var i=0;i<yxlength;i++){
			var jtxx=$("#positionAddForm .yxjt")[i].id;
			var boxid="l"+jtxx.substr(1,2);
			if ($("#positionAddForm #"+boxid).attr("class")){
				var lieid=$("#positionAddForm #"+boxid).attr("class").replace("connect ","");
				if(sclie==lieid){
					sftl=sftl+1;
				}
			}
		}
		if(sftl==7){
			$("#positionAddForm #"+sclie).prop("checked",false);
		}
	}
}

$("#positionAddForm .lqx").click(function(){
	var lqxid=$(this).attr("id");
	var zmval=$("#positionAddForm #choose").val();
	var szval;
	var zt;
	var szval;
	var zmval;
	var jtval;
	if($("#positionAddForm #flag").val()!="1"){
		if(this.checked==true){
			var btqsl=0;
			var btlsl=0;
			var yxsl=$("#positionAddForm #positionnr").children('div').length-1;
			for(var i=1;i<yxsl+1;i++){
				var ssq=$("#positionAddForm #positionnr").children('div')[i].id[0];
				var ssxh=$("#positionAddForm #positionnr").children('div')[i].id.substr(1,2);
				if(ssq==zmval){
					for(var j=1;j<7;j++){
						for(var k=0;k<10;k++){
							var sftl=j;
							var xh=$("#positionAddForm .l"+j)[k].value;
							if(xh==ssxh && sftl!=lqxid.substr(1)){
								btlsl=parseInt(btlsl)+1;
							}
						}
					}
				}else{
					btqsl=parseInt(btqsl)+1;
				}
			}
			if(btlsl >0 || btqsl>0){
				$.confirm("已选接头不得超过10个!");
				$(this).prop("checked",false);
			}else{
				$("#positionAddForm .yxjt").remove();
				$("#positionAddForm ."+lqxid).prop("checked",true);
				for(var i=0;i<10;i++){
					szval=$("#positionAddForm ."+lqxid)[i].value;
					zmval=$("#positionAddForm #choose").val();
					zt=$("#positionAddForm ."+lqxid)[i].checked;
					jtval=zmval+szval;
					if(zt==true){
						$("#positionAddForm #positionnr").append("<div id='"+jtval+"' class='col-sm-1 col-md-1 col-xs-3 yxjt "+zmval+"' style='padding-left:5px;padding-right:5px;border:1px solid #B0DCF9;border-radius:3px;margin-left:5px;display:block;'><a href='javascript:void(0)' id='"+szval+"' onclick='qxxz(this)'><span style='font-size:12px;'>"+jtval+"×</span></a></div>");
					}else{
						$("#positionAddForm #"+jtval).remove();
					}
				}
			}
		}else{
			$("#positionAddForm ."+lqxid).prop("checked",false);
			$("#positionAddForm .yxjt").remove();
		}
	}else{
		$(this).prop("checked",false);
	}
})

$(function(){
	//显示已选接头信息
	if($("#positionAddForm #flag").val()!="1"){
		var text=$("#positionAddForm #text").val();
		var zmmr=$("#positionAddForm #choose").val();
		var firstlie=$("#positionAddForm #firstlie").val();
		var size=$("#positionAddForm #size").val();

		var list = text.split(",");
		var tlsl=0;//同列数量
		for(var i=0;i<list.length;i++){
			var jtval=list[i];
			var zmval=list[i].substr(0,1);
			var szval=jtval.replace(zmval,"");
			if (jtval){
				tlsl ++;
				$("#positionAddForm #positionnr").append("<div id='"+jtval+"' class='col-sm-1 col-md-1 col-xs-3 yxjt "+zmval+"' style='padding-left:5px;padding-right:5px;border:1px solid #B0DCF9;border-radius:3px;margin-left:5px;display:block;'><a href='javascript:void(0)' id='"+szval+"' onclick='qxxz(this)'><span style='font-size:12px;'>"+jtval+"×</span></a></div>");
				if (zmmr === zmval){
					$("#positionAddForm #l"+szval).prop("checked",true);
				}
			}
		}
		if(tlsl==size){
			$("#positionAddForm #"+firstlie).prop("checked",true);
		}
	}
	//下拉框样式
	jQuery('#positionAddForm .chosen-select').chosen({width: '100%'});
});