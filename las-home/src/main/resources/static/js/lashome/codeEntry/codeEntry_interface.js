//序号，标记1-4div
var xh="0";

//div1点击事件
function div1but(){
	//如果其他div没有点击，打开隐藏录入div,并更新xh标记
	if(xh=="0"){
		xh="1";
		$("#enter").show();
	}else{
		return true;
	}
}

//div1点击事件
function div2but(){
	//如果其他div没有点击，打开隐藏录入div,并更新xh标记
	if(xh=="0"){
		xh="2";
		$("#enter").show();
	}else{
		return true;
	}
}

//div1点击事件
function div3but(){
	//如果其他div没有点击，打开隐藏录入div,并更新xh标记
	if(xh=="0"){
		xh="3";
		$("#enter").show();
	}else{
		return true;
	}
}

//div1点击事件
function div4but(){
	//如果其他div没有点击，打开隐藏录入div,并更新xh标记
	if(xh=="0"){
		xh="4";
		$("#enter").show();
	}else{
		return true;
	}
}

//关闭按钮，清空录入信息
function closebut(){
	$("#ybbh_1").val("");
	$("#ybbh_2").val("");
	$("#ybbh_3").val("");
	$("#ybbh_4").val("");
	xh="0";
	$("#enter").hide();
}

function determine(){
	//获取录入信息
	var ybbh1 = $("#ybbh_1").val();
	var ybbh2 = $("#ybbh_2").val();
	var ybbh3 = $("#ybbh_3").val();
	var ybbh4 = $("#ybbh_4").val();
	
	//判断当前信息是录入哪个div
	if(xh=="1"){
		$("#bh1_1").val(ybbh1);
		$("#bh1_2").val(ybbh2);
		$("#bh1_3").val(ybbh3);
		$("#bh1_4").val(ybbh4);
	}else if(xh=="2"){
		$("#bh2_1").val(ybbh1);
		$("#bh2_2").val(ybbh2);
		$("#bh2_3").val(ybbh3);
		$("#bh2_4").val(ybbh4);
	}else if(xh=="3"){
		$("#bh3_1").val(ybbh1);
		$("#bh3_2").val(ybbh2);
		$("#bh3_3").val(ybbh3);
		$("#bh3_4").val(ybbh4);
	}else{
		$("#bh4_1").val(ybbh1);
		$("#bh4_2").val(ybbh2);
		$("#bh4_3").val(ybbh3);
		$("#bh4_4").val(ybbh4);
	}
	
	//清空录入界面信息
	$("#ybbh_1").val("");
	$("#ybbh_2").val("");
	$("#ybbh_3").val("");
	$("#ybbh_4").val("");
	
	//更新xh为o
	xh="0";
	$("#enter").hide();
}

//回车事件
function focusNextInput(thisInput){
    var inputs = document.getElementsByTagName("input");   
    for(var i = 0;i<inputs.length;i++){
        // 如果是最后一个，则焦点回到第一个 
        if(i==(inputs.length-1)){   
            inputs[20].focus(); 
            break;  
        }else if(thisInput == inputs[i]){
            inputs[i+1].focus(); 
            break;   
        }   
    }   
}  

//执行按钮
function implementbtn(){
	//组装样本编号json
	var nbbhJson = [];
	var sz={"nbbh":""};
	if($("#bh1_1").val()!=null && $("#bh1_1").val()!=""){
		sz.nbbh= $("#bh1_1").val();
		nbbhJson.push(sz)
	}
	if($("#bh1_2").val()!=null && $("#bh1_2").val()!=""){
		sz.nbbh= $("#bh1_2").val();
		nbbhJson.push(sz)
	}
	if($("#bh1_3").val()!=null && $("#bh1_3").val()!=""){
		sz.nbbh= $("#bh1_3").val();
		nbbhJson.push(sz)
	}
	if($("#bh1_4").val()!=null && $("#bh1_4").val()!=""){
		sz.nbbh= $("#bh1_4").val();
		nbbhJson.push(sz)
	}
	
	if($("#bh2_1").val()!=null && $("#bh2_1").val()!=""){
		sz.nbbh= $("#bh2_1").val();
		nbbhJson.push(sz)
	}
	if($("#bh2_2").val()!=null && $("#bh2_2").val()!=""){
		sz.nbbh= $("#bh2_2").val();
		nbbhJson.push(sz)
	}
	if($("#bh2_3").val()!=null && $("#bh2_3").val()!=""){
		sz.nbbh= $("#bh2_3").val();
		nbbhJson.push(sz)
	}
	if($("#bh2_4").val()!=null && $("#bh2_4").val()!=""){
		sz.nbbh= $("#bh2_4").val();
		nbbhJson.push(sz)
	}
	
	if($("#bh3_1").val()!=null && $("#bh3_1").val()!=""){
		sz.nbbh= $("#bh3_1").val();
		nbbhJson.push(sz)
	}
	if($("#bh3_2").val()!=null && $("#bh3_2").val()!=""){
		sz.nbbh= $("#bh3_2").val();
		nbbhJson.push(sz)
	}
	if($("#bh3_3").val()!=null && $("#bh3_3").val()!=""){
		sz.nbbh= $("#bh3_3").val();
		nbbhJson.push(sz)
	}
	if($("#bh3_4").val()!=null && $("#bh3_4").val()!=""){
		sz.nbbh= $("#bh3_4").val();
		nbbhJson.push(sz)
	}
	
	if($("#bh4_1").val()!=null && $("#bh4_1").val()!=""){
		sz.nbbh= $("#bh4_1").val();
		nbbhJson.push(sz)
	}
	if($("#bh4_2").val()!=null && $("#bh4_2").val()!=""){
		sz.nbbh= $("#bh4_2").val();
		nbbhJson.push(sz)
	}
	if($("#bh4_3").val()!=null && $("#bh4_3").val()!=""){
		sz.nbbh= $("#bh4_3").val();
		nbbhJson.push(sz)
	}
	if($("#bh4_4").val()!=null && $("#bh4_4").val()!=""){
		sz.nbbh= $("#bh4_4").val();
		nbbhJson.push(sz)
	}

	if(nbbhJson!=null && nbbhJson!=""){
		var nbbh_json = JSON.stringify(nbbhJson);
		
		$.ajax({
			url: "/lashome/codeEntry/codeEntrySave", 
			type: "post",
			dataType:'json',
			data:{nbbh_json:nbbh_json,access_token:$("#ac_tk").val()},
			success: function(data){
				if(data.status=="success"){
					$.error(data.message);
				}else{
					$.error(data.message);
	    			return;
				}
			}
		})		
	}else{
		$.error("请录入样本！");
		return;
	}
}
