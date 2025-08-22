$(".connect").click(function(){
    djsz.call(this);// 关键：传递 this 上下文
});

function djsz(){
	var zt=this.checked;
	var szval=$(this).val();
	var zmval=$("#zm").val();
	var jtval=zmval+szval;
	var thisclass=$(this).attr("class").replace("connect ","");
	if($("#djtbj").val()=="1" && $("#xznr").children('div').length-1>0 && zt==true){
		$(this).prop("checked",false);
	}else{
	if(zt==false){
		$("#"+thisclass).prop("checked",false);
		$("#"+jtval).remove();
	}
	if(zt==true){
		if($("#xznr").children('div').length-1<8){
			$("#xznr").append("<div id='"+jtval+"' class='col-sm-1 col-md-1 col-xs-3 yxjt "+zmval+"' style='padding-left:5px;padding-right:5px;border:1px solid #B0DCF9;border-radius:3px;margin-left:5px;display:block;'><a href='javascript:void(0)' id='"+szval+"' onclick='qxxz(this)'><span style='font-size:12px;'>"+jtval+"×</span></a></div>");
			var xzsl=0;
			for(var i=0;i<$("."+thisclass).length;i++){
				if($("."+thisclass)[i].checked==true){
					xzsl++
				}
			}
			if(xzsl==8){
				$("#"+thisclass).prop("checked",true);
			}
		}else{
		$.confirm("已选接头不得超过8个!");
		$(this).prop("checked",false);
		}
	}
	}
}

$("#zm").change(function(){
	var zmval=$("#zm").val();
	var jtqlist=$("."+zmval).length;
	$(".connect").prop("checked",false);
	if(jtqlist>0){
		for(var i=0;i<jtqlist;i++){
			var id=($("."+zmval)[i].id).replace(zmval,"l");
			$("#"+id).prop("checked",true);
		}
	}else{
		$(".lqx").prop("checked",false);
	}
    $("#szlist").empty();
    var newhtml="";
    var sz=6;
    if(zmval=="Ts"){//当切换为Ts时，将接头数字清空,重新加载144个
        sz=18;
    }
    for(var i=1;i<=sz;i++){//计算8*18
        var html="<div class='col-sm-2 col-md-2 col-xs-4'>";
        for(var j=1;j<=8;j++){
            var index=(i-1)*8+j;
            if(index<10){
                index="0"+index;
            }
            html=html+"<label><input type='checkbox' name='connect' class='connect l"+i+"' value='"+index+"' id='l"+index+"'>"+index+"</label>";
        }
        html=html+"<label style='font-size:12px;'><input name='lqx' type='checkbox' class='lqx' value='' id='l"+i+"'>列全选</label>";
        html=html+"</div>";
        newhtml=newhtml+html;
    }
    $("#szlist").append(newhtml);
    // 全局事件委托
    $("#ConnectAddForm").off('click', '.connect').on('click', '.connect', function () {
        djsz.call(this);// 关键：传递 this 上下文，需要先解绑，再绑定，否则会重复触发
    })
    $("#ConnectAddForm").off('click', '.lqx').on('click', '.lqx', function () {

        qx.call(this);// 关键：传递 this 上下文
    })
})

function qxxz(a){
	var sftl=0;
	if(a.id==""){
	    $(a).parent().remove()
	    return;
	}
	var conid="l"+a.id;
	var fysid=$("#"+a.id).parent().attr("id");
	if(fysid==($("#zm").val()+a.id)){
		$("#"+conid).prop("checked",false);
	}
	$("#"+fysid).remove();
	var sclie=$("#"+("l"+fysid.substr(1,2))).attr("class").replace("connect ","");
	var yxlength=$(".yxjt").length;
	for(var i=0;i<yxlength;i++){
		var jtxx=$(".yxjt")[i].id;
		var boxid="l"+jtxx.substr(1,2);
		var lieid=$("#"+boxid).attr("class").replace("connect ","");
		if(sclie==lieid){
			sftl=sftl+1;
		}
	}
	if(sftl==7){
		$("#"+sclie).prop("checked",false);
	}
}

$(".lqx").click(function(){

	qx.call(this);
})

function qx(){
	var lqxid=$(this).attr("id");
    	var zmval=$("#zm").val();
    	var szval;
    	var zt;
    	var szval;
    	var zmval;
    	var jtval;
    	if($("#djtbj").val()!="1"){
    	if(this.checked==true){
    		var btqsl=0;
    		var btlsl=0;
    		var yxsl=$("#xznr").children('div').length-1;
    		for(var i=1;i<yxsl+1;i++){
    			var ssq=$("#xznr").children('div')[i].id[0];
    			var ssxh=$("#xznr").children('div')[i].id.substr(1,2);
    			if(ssq==zmval){
    				for(var j=1;j<7;j++){
    					for(var k=0;k<8;k++){
    						var sftl=j;
    						var xh=$(".l"+j)[k].value;
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
    			$.confirm("已选接头不得超过8个!");
    			$(this).attr("checked",false);
    		}else{
    			$(".yxjt").remove();
    			$("."+lqxid).prop("checked",true);
    			for(var i=0;i<8;i++){
    				szval=$("."+lqxid)[i].value;
    				zmval=$("#zm").val();
    				zt=$("."+lqxid)[i].checked;
    				jtval=zmval+szval;
    					if(zt==true){
    						$("#xznr").append("<div id='"+jtval+"' class='col-sm-1 col-md-1 col-xs-3 yxjt "+zmval+"' style='padding-left:5px;padding-right:5px;border:1px solid #B0DCF9;border-radius:3px;margin-left:5px;display:block;'><a href='javascript:void(0)' id='"+szval+"' onclick='qxxz(this)'><span style='font-size:12px;'>"+jtval+"×</span></a></div>");
    					}else{
    						$("#"+jtval).remove();
    					}
    				}
    			}
    	}else{
    		$("."+lqxid).prop("checked",false);
    		$(".yxjt").remove();
    	}
    	}else{
    		$(this).prop("checked",false);
    	}
}

var djtid;
var djtxx;
$(function(){
	//显示已选接头信息
	if($("#djtbj").val()!="1"){
	var lieid=$("#lieid").val();
	var zmmr=$("#zm").val();
	var zmval;
	var jtval;
	var szval;
	var firstlie;//第一个接头所属于的列
	var tlsl=0;//同列数量
	var lie;//每个接头所属列
	for(var i=1;i<9;i++){
		jtval=$("#"+lieid+"-"+i).text();
		zmval=$("#"+lieid+"-"+i).text().substr(0,1);
		szval=jtval.replace(zmval,"");
		if(jtval!="" && zmval==zmmr){
			if(i==1){
				firstlie=$("#l"+szval).attr("class").replace("connect ","");;
			}
		 lie=$("#l"+szval).attr("class").replace("connect ","");
		}else{
			lie="";
		}
		if(jtval!=null && jtval!=""){
			$("#xznr").append("<div id='"+jtval+"' class='col-sm-1 col-md-1 col-xs-3 yxjt "+zmval+"' style='padding-left:5px;padding-right:5px;border:1px solid #B0DCF9;border-radius:3px;margin-left:5px;display:block;'><a href='javascript:void(0)' id='"+szval+"' onclick='qxxz(this)'><span style='font-size:12px;'>"+jtval+"×</span></a></div>");
			$("#l"+szval).prop("checked",true);
		}
		if(firstlie==lie){
			tlsl=tlsl+1;
		}
	}
	if(tlsl==8){
		$("#"+firstlie).prop("checked",true);
	}
	}
	//编辑单个接头信息
	if($("#djtbj").val()=="1"){
		djtid=$("#lieid").val();
		var lieid=djtid.substr(0,djtid.length-2);
		djtxx=$("#jtxx").val();
		if(djtxx!=null && djtxx!=""){
			var zmval=djtxx[0];
			$("#zm").val(zmval);
			var szval=djtxx.substr(1);
			$("#xznr").append("<div id='"+djtxx+"' class='col-sm-1 col-md-1 col-xs-3 yxjt "+zmval+"' style='padding-left:5px;padding-right:5px;border:1px solid #B0DCF9;border-radius:3px;margin-left:5px;display:block;'><a href='javascript:void(0)' id='"+szval+"' onclick='qxxz(this)'><span style='font-size:12px;'>"+djtxx+"×</span></a></div>");
			$("#l"+szval).prop("checked",true);
		}
	}
	
	//下拉框样式
    jQuery('#ConnectAddForm .chosen-select').chosen({width: '100%'});
});