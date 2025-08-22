
var beforeLibraryData=[];

document.addEventListener('paste', async e => {
	//获取剪切板数据
	var clipText = e.clipboardData.getData('text/plain');
	var id=e.target.id;
	if(clipText){
		if(id=="nbbh1-1"){
				if(clipText.indexOf("\n")!=-1){
				var splitID = id.split("-");//对id进行分割，获取行数和列数
				var num=parseInt($("#ajaxForm #"+id).attr("xh"));//序号
				var line=1;
				if(parseInt(splitID[0].substring(4))<=6){
					line=parseInt(splitID[0].substring(4));//不足6列直接取列数
				}else{
					line=parseInt(splitID[0].substring(4))%6;//超过6列对6取余作为列数
				}
				var row=parseInt(splitID[1]);//行数
				var strings = clipText.split("\n");
				for(var i=0;i<strings.length;i++){
					if(row>8&&row%8==1){//8行一组，一旦过了一组则序号+40
						num=num+40;
					}
					if(strings[i]){
						var str = strings[i].split("\t");
						var length=str.length;
						if(str.length>(6-line+1)){//6-line+1  是为了获取剩余列数进行限制
							length=6-line+1;
						}
						for(var j=0;j<length;j++){
							if(j==0){
								$("input[xh='"+num+"']").val(str[j]);
							}else if(j==1){
								$("input[xh='"+(num+8*1)+"']").val(str[j]);
							}else if(j==2){
								$("input[xh='"+(num+8*2)+"']").val(str[j]);
							}else if(j==3){
								$("input[xh='"+(num+8*3)+"']").val(str[j]);
							}else if(j==4){
								$("input[xh='"+(num+8*4)+"']").val(str[j]);
							}else if(j==5){
								$("input[xh='"+(num+8*5)+"']").val(str[j]);
							}
						}
						$("#ajaxForm #nbbh"+line+"-"+row).blur();
						num++;
						row++
					}
				}
			}else{
				var strings = clipText.split(";");
				for(var i=0;i<strings.length;i++){
					var str = strings[i].split(",");
					for(var j=0;j<str.length;j++){
						var strs = str[j].split("+");
						$("#ajaxForm #nbbh"+(i+1)+"-"+(j+1)).val(strs[0]);
						changeColorForNbbm("#nbbh"+(i+1)+"-"+(j+1),strs[0]);
						$("#ajaxForm #tqm"+(i+1)+"-"+(j+1)).val(strs[1]);
						$("#ajaxForm #id"+(i+1)+"-"+(j+1)).val(strs[2]);
					}
				}
				resetImg();
			}
		}else if(id=="bbjc"){
            $("#bbjc").val(clipText)
		}else if(id=="zdpbin"){
             $("#zdpbin").val(clipText)
        }else if(id=="zdpbnotin"){
             $("#zdpbnotin").val(clipText)
        }else if(id=="sjph2"){
             $("#sjph2").val(clipText)
        }else{
			var splitID = id.split("-");//对id进行分割，获取行数和列数
			var num=parseInt($("#ajaxForm #"+id).attr("xh"));//序号
            if(!num){
                return;
            }
			var line=1;
			if(parseInt(splitID[0].substring(4))<=6){
				line=parseInt(splitID[0].substring(4));//不足6列直接取列数
			}else{
				line=parseInt(splitID[0].substring(4))%6;//超过6列对6取余作为列数
			}
			var row=parseInt(splitID[1]);//行数
			var strings = clipText.split("\n");
			for(var i=0;i<strings.length;i++){
				if(row>8&&row%8==1){//8行一组，一旦过了一组则序号+40
					num=num+40;
				}
				if(strings[i]){
					var str = strings[i].split("\t");
					var length=str.length;
					if(str.length>(6-line+1)){//6-line+1  是为了获取剩余列数进行限制
						length=6-line+1;
					}
					for(var j=0;j<length;j++){
						if(j==0){
							$("input[xh='"+num+"']").val(str[j]);
						}else if(j==1){
							$("input[xh='"+(num+8*1)+"']").val(str[j]);
						}else if(j==2){
							$("input[xh='"+(num+8*2)+"']").val(str[j]);
						}else if(j==3){
							$("input[xh='"+(num+8*3)+"']").val(str[j]);
						}else if(j==4){
							$("input[xh='"+(num+8*4)+"']").val(str[j]);
						}else if(j==5){
							$("input[xh='"+(num+8*5)+"']").val(str[j]);
						}
					}
					$("#ajaxForm #nbbh"+line+"-"+row).blur();
					num++;
					row++
				}
			}
		}

		copyText("");//清空剪切板
	}
})

copyContentH5: function copyText(content) {
	var copyDom = document.createElement('div');
	copyDom.innerText=content;
	copyDom.style.position='absolute';
	copyDom.style.top='0px';
	copyDom.style.right='-9999px';
	document.body.appendChild(copyDom);
	//创建选中范围
	var range = document.createRange();
	range.selectNode(copyDom);
	//移除剪切板中内容
	window.getSelection().removeAllRanges();
	//添加新的内容到剪切板
	window.getSelection().addRange(range);
	//复制
	var successful = document.execCommand('copy');
	copyDom.parentNode.removeChild(copyDom);
	try{
		var msg = successful ? "successful" : "failed";
		console.log('Copy command was : ' + msg);
	} catch(err){
		console.log('Oops , unable to copy!');
	}
}

function checkBox(_that){
	let id=_that.id;
	let check = $('#ajaxForm #'+id).prop('checked');
	let check_str = $('#ajaxForm #check_str').val();
	if (check){
		check_str += ','+id;
		$('#ajaxForm #check_str').val(check_str);
	}else{
		let str = ""
		let strings = check_str.split(",");
		for (let i = 1; i < strings.length; i++) {
			if (strings[i] !=id ){
				str+=","+strings[i]
			}
		}
		$('#ajaxForm #check_str').val(str);
	}
}
function checkAllBox(_that){
	let id=_that.id;
	let check = $('#ajaxForm #'+id).prop('checked');
	let check_str = $('#ajaxForm #check_str').val();
	let str = ""
	if (check_str){
		let strings = check_str.split(",");
		for (let i = 1; i < strings.length; i++) {
			if (!strings[i].includes(id+"-") ){
				str+=","+strings[i]
			}else{
				$('#ajaxForm #'+strings[i]).prop('checked',false);
			}
		}
	}
	if (check){
		for (let i = 1; i < 9; i++) {
			str  += ','+id+"-"+i;
			$('#ajaxForm #'+id+"-"+i).prop('checked',true);
		}
	}
	$('#ajaxForm #check_str').val(str);
}

function replace(){
	let th_value = $('#ajaxForm #th_value').val();
	let check_str = $('#ajaxForm #check_str').val();
	let strings = check_str.split(",");
	for (let i = 1; i < strings.length; i++) {
		var xhSplit=strings[i].split("-");
		var lie = xhSplit[0];
		var num = xhSplit[1];
		$("#id"+lie+"-"+num).val('');
		$("#tqm"+lie+"-"+num).val('');
		let nbbm = $('#ajaxForm #nbbh'+strings[i]).val();
		if (nbbm){
			var qf=[];
			var nbbh="";
			var position="";//确定--位置是在内部编码后还是后缀后，0代表在内部编码后，1代表在后缀后
			if(nbbm.indexOf("--")!=-1){
				var split1 = nbbm.split("--");
				position="0";
				for(var h=1;h < split1.length;h++){
					var t_split2 = split1[h].split("-");
					if(t_split2.length > 1){
						position="1";
					}
					qf.push(t_split2[0])
				}
				var t_split3 = split1[0].split("-");
				if(t_split3.length > 0){
					if(t_split3[0].length > 6){
						nbbh =t_split3[0];
					}else{
						nbbh =t_split3[0] +"-" + t_split3[1];
					}
				}else{
					nbbh =t_split3[0];
				}
			}else{
				var t_split3 = nbbm.split("-");
				if(t_split3.length > 0){
					if(t_split3[0].length > 6){
						nbbh =t_split3[0];
					}else{
						nbbh =t_split3[0] +"-" + t_split3[1];
					}
				}else{
					nbbh =t_split3[0];
				}
			}
			var wkid = $('#ajaxForm #wkid').val()
			$.ajax({
				type:'post',
				url:"/inspection/inspection/pagedataGetSjsyInfo",
				cache: false,
				async: false,
				data: {"nbbm":nbbh,"wkrq":$("#ajaxForm #wkrq").val(),"wkid":wkid,"jcdw":$("#ajaxForm #jcdw").val(),"access_token":$("#ac_tk").val(),"qtsyrq":$("#ajaxForm #wkrq").val()},
				dataType:'json',
				success:function(data){
					//返回值
					var isMatched=false;
					if(data. list!=null&&data.list.length>0){
						for(var i=0;i<data.list.length;i++){
							if(th_value==data.list[i].kzcs1){
								isMatched=true;
								setLibSuffix(lie,num,nbbh,data.list[i],qf,position);
								break;
							}
						}
					}
					if(!isMatched){
						var s_qf= "";
						if(qf){
							for(var i=0;i<qf.length;i++)
							{
								var t_qf = qf[i].replaceAll("-","")
								if(t_qf)
									s_qf = s_qf + "--" + t_qf
							}
						}
						if(position&&"0"==position){
							$("#nbbh"+lie+"-"+num).val(nbbh+s_qf+'-'+th_value);
						}else if(position&&"1"==position){
							$("#nbbh"+lie+"-"+num).val(nbbh+'-'+th_value+s_qf);
						}else{
							$("#nbbh"+lie+"-"+num).val(nbbh+'-'+th_value);
						}
					}
					setExtractInfo(data.tqmxList,$("#id"+lie+"-"+num).val(),lie,num);
				}
			});
			var  id = "#nbbh"+lie+"-"+num;
			changeColorForNbbm(id,$('#ajaxForm #nbbh'+strings[i]).val());
			resetImg();
		}
		$('#ajaxForm #'+strings[i]).prop('checked',false);
	}
	$('#ajaxForm #check_str').val("");
}


var xzlieid;
function addconnect(lieid){
	xzlieid=lieid.id;
	url="/experiment/library/pagedataAddConnect?lieid="+xzlieid;
	$.showDialog(url,'添加接口',addConnectConfig);
}

//修改单个接头信息
function modJtxx(jt){
	var id=jt.id;
	var value=$("#"+id).text();
	url="/experiment/library/pagedataModeJt?lieid="+id+"&jtxx="+value;
	$.showDialog(url,'编辑接口',modConnectConfig);
}

var addConnectConfig = {
		width		: "600px",
		modalName	: "addConnectModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					var xzsl=$("#xznr").children('div').length-1;
					$("."+xzlieid).val("");
					$("."+xzlieid).text("");
					for(var i=0;i<xzsl;i++){
						$("#"+xzlieid+"-"+(i+1)).val($("#xznr").children('div')[i+1].id);
						$("#"+xzlieid+"-"+(i+1)).text($("#xznr").children('div')[i+1].id);
						setTimeout(function(){
							if (xzlieid[4]!=undefined)
								$("#nbbh"+xzlieid[3]+xzlieid[4]+"-"+1).focus();
							else
								$("#nbbh"+xzlieid[3]+"-"+1).focus();
						},200)
					}
				}
			},
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

var modConnectConfig = {
		width		: "600px",
		modalName	: "addConnectModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if($("#xznr").children('div').length-1>0){
						$("#"+djtid).val($("#xznr").children('div')[1].id);
						$("#"+djtid).text($("#xznr").children('div')[1].id);
						setTimeout(function(){
							$("#nbbh"+xzlieid[3]+"-"+1).focus();
						},200)
					}else{
						$("#"+djtid).val(null);
						$("#"+djtid).text(null);
						setTimeout(function(){
							$("#nbbh"+xzlieid[3]+"-"+1).focus();
						},200)
					}
				}
			},
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

var libPreInput;
var libSecondPreInput;
$("#ajaxForm input").on('keydown',function(e){
 
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
    	// $(this).val($(this).val().toUpperCase());
        if($(this).val().length >0){
        	var nextxh=parseInt(thisxh)+1;
	    	if(nextxh==193){
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
    	// $(this).val($(this).val().toUpperCase());
    	var nextxh=parseInt(thisxh)+1;
    	if(nextxh==193){
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

$("#ajaxForm .wkmxxx input").blur(function(){
	var thisxh=$(this).attr("xh");
	var str = $("input[xh='"+thisxh+"']").val();
	if(!str){
		var xh = parseInt(thisxh);
		if(xh%8==0){
			var lie=parseInt(xh/8);
			var num=8;
			$("#tqm"+lie+"-"+num).val('');
			$("#id"+lie+"-"+num).val('');
		}else{
			var lie=parseInt(xh/8)+1;
			var num=xh%8;
			$("#tqm"+lie+"-"+num).val('');
			$("#id"+lie+"-"+num).val('');
		}
		$("#success-"+thisxh).hide();
		$("#error-"+thisxh).hide();
		setExtractInfo(null,null,lie,num);
		return;
	}
	re = new RegExp(",","g"); 
	rq = new RegExp("，","g");
	var Newstr = str.replace(re, "");
	Newstr = Newstr.replace(rq,"");
	$("input[xh='"+thisxh+"']").val(Newstr);
	//检验判断加“-DNA”还是“-RNA”
	var thisnbbh=$("input[xh='"+thisxh+"']").val();

	var thisybbhlength=thisnbbh.length;
	let mrhz = $("#ajaxForm #mrhz").val();
	let wkid = $("#ajaxForm #wkid").val();
	var xh = parseInt(thisxh);
	var lie=0;
	var num=0;
	if(xh%8==0){
		lie=parseInt(xh/8);
		num=8;
	}else{
		lie=parseInt(xh/8)+1;
		num=xh%8;
	}
	
	//判断2次触发
	var strings = thisnbbh.split(" ");
	if ( strings.length > 1){
		$("input[xh='"+thisxh+"']").val(strings[0]);
		var strs=strings[1].split("/");
		$.ajax({
			type:'post',
			url:"/inspection/inspection/pagedataGetSjsyInfo",
			cache: false,
			async: false,
			data: {"nbbm":thisnbbh,"wkrq":$("#ajaxForm #wkrq").val(),"wkid":wkid,"kzcs2":strs[0],"kzcs3":strs[1],"jcdw":$("#ajaxForm #jcdw").val(),"access_token":$("#ac_tk").val(),"qtsyrq":$("#ajaxForm #wkrq").val()},
			dataType:'json',
			success:function(data){
				//返回值
				if(data. list!=null&&data.list.length>0){
					$("#tqm"+lie+"-"+num).val('');
					$("#id"+lie+"-"+num).val('');
					var checkStr="";
					for(var i=1;i<25;i++){
						for(var j=1;j<9;j++){
							var id = $("#id"+i+"-"+j).val();
							if(id){
								checkStr=checkStr+','+id;
							}
						}
					};

					if(mrhz){//如果默认后缀不为空
						var isFind=false;
						for(var i=0;i<data.list.length;i++){
							if(checkStr.indexOf(data.list[i].syglid)==-1&&mrhz==data.list[i].kzcs1){//判断当前页面是否已经存在这一条数据
								//说明默认后缀匹配成功，直接拼上后缀以及对tqm以及id赋值
								setLibSuffix(lie,num,strings[0],data.list[i]);
								isFind=true;
								break;
							}
						}
						//说明默认后缀匹配失败
						if(!isFind){
							var isMatch=false;
							for(var i=0;i<data.list.length;i++){
								if(checkStr.indexOf(data.list[i].syglid)==-1){//判断当前页面是否已经存在这一条数据
									setLibSuffix(lie,num,strings[0],data.list[i]);
									isMatch=true;
									break;
								}
							}
							if(!isMatch){
								setLibSuffix(lie,num,strings[0],data.list[0]);
							}
						}
					}else{//说明默认后缀为空
						var isMatch=false;
						for(var i=0;i<data.list.length;i++){
							if(checkStr.indexOf(data.list[i].syglid)==-1){//判断当前页面是否已经存在这一条数据
								setLibSuffix(lie,num,strings[0],data.list[i]);
								isMatch=true;
								break;
							}
						}
						if(!isMatch){
							setLibSuffix(lie,num,strings[0],data.list[0]);
						}
					}
				}
				var isChange=true;
				if(beforeLibraryData.length>0){
					for(var i=0;i<beforeLibraryData.length;i++){
						if(thisxh==beforeLibraryData[i].xh&&$("#nbbh"+lie+"-"+num).val()==beforeLibraryData[i].nbbh){
							isChange=false;
                            break;
						}
					}
				}
				if(isChange){
					setExtractInfo(data.tqmxList,$("#id"+lie+"-"+num).val(),lie,num);
				}
			}
		});
	}else{
		var qf=[];
		var position="";//确定--位置是在内部编码后还是后缀后，0代表在内部编码后，1代表在后缀后
		if(thisnbbh.indexOf("--")!=-1){
			var split1 = thisnbbh.split("--");
			var hz=[];
			position="0";
			for(var i=1;i < split1.length;i++){
				var t_split2 = split1[i].split("-");
				if(t_split2.length > 1){
					position="1";
					for(var j=1;j < t_split2.length;j++){
						hz.push(t_split2[j])
					}
				}
				qf.push(t_split2[0])
			}
			thisnbbh =split1[0];
			for(var x=0;x < hz.length;x++){
				thisnbbh = thisnbbh + "-" + hz[x]
			}
			thisybbhlength=thisnbbh.length;//由于重新拼接了内部编码，重新获取长度
		}
		if(thisnbbh.substr(0,3)=="NC-"||thisnbbh.substr(0,3)=="PC-"||thisnbbh.substr(0,3)=="DC-" || thisnbbh.substr(0,4)=="DBL-"){
			$("#tqm"+lie+"-"+num).val('');
			$("#id"+lie+"-"+num).val('');
			getDetectionData(thisnbbh,mrhz,'',lie,num,qf,position);
			if(!$("#id"+lie+"-"+num).val()){
				$.confirm("此后缀不存在,需重新确认");
			}
		}else{
			var id= $("#id"+lie+"-"+num).val();
			if(!id){
				var nbbh="";
				var split = thisnbbh.split("-");
				var hz="";
				if(split.length>=2){
					if(split[0].length>6) {
						nbbh=split[0];
						for(var i=1;i<split.length;i++){
							hz=hz+'-'+split[i];
						}
						hz=hz.substring(1);
					}else{
						nbbh=split[0]+'-'+split[1];
						for(var i=2;i<split.length;i++){
							hz=hz+'-'+split[i];
						}
						hz=hz.substring(1);
					}
				}else{
					nbbh=split[0];
				}
				getDetectionData(nbbh,mrhz,hz,lie,num,qf,position);
			}else{
				var isChange=true;
				if(beforeLibraryData.length>0){
					for(var i=0;i<beforeLibraryData.length;i++){
						if(thisxh==beforeLibraryData[i].xh&&thisnbbh===beforeLibraryData[i].nbbh){
							isChange=false;
                            break;
						}
					}
				}
				if(isChange){
					var split = thisnbbh.split("-");
					if(split.length>=2){
						if(split[0].length>6){
							var hz='';
							for(var i=1;i<split.length;i++){
								hz=hz+'-'+split[i];
							}
							hz=hz.substring(1);
							checkDetectionData(split[0],id,hz,lie,num,qf,position);
						}else{
							var hz='';
							for(var i=2;i<split.length;i++){
								hz=hz+'-'+split[i];
							}
							hz=hz.substring(1);
							checkDetectionData(split[0]+'-'+split[1],id,hz,lie,num,qf,position);
						}
					}else{
						$("#id"+lie+"-"+num).val('');
						$("#tqm"+lie+"-"+num).val('');
						$("#nbbh"+lie+"-"+num).attr("style","height:30px;");
						getDetectionData(split[0],mrhz,'',lie,num,qf,position);
					}
				}
			}
		}
	}
	var nbbh = $("input[xh='"+thisxh+"']").val();
	var  id = "input[xh='"+thisxh+"']";
	changeColorForNbbm(id,nbbh)
	resetImg();
})


function checkDetectionData(nbbh,id,hz,lie,num,qf,position){
	$.ajax({
		type:'post',
		url:"/inspection/inspection/pagedataGetSjsyInfo",
		cache: false,
		async: false,
		data: {"nbbm":nbbh,"hz":hz,"wkrq":$("#ajaxForm #wkrq").val(),"wkid":$("#ajaxForm #wkid").val(),"jcdw":$("#ajaxForm #jcdw").val(),"access_token":$("#ac_tk").val(),"qtsyrq":$("#ajaxForm #wkrq").val()},
		dataType:'json',
		success:function(data){
			//返回值
			var checkflag=false;
			if(data.list!=null&&data.list.length>0){
				for(var i=0;i<data.list.length;i++){
					if(hz==data.list[i].kzcs1){
						checkflag=true;
						setLibSuffix(lie,num,nbbh,data.list[i],qf,position);
						break;
					}
				}
			}
			setExtractInfo(data.tqmxList,$("#id"+lie+"-"+num).val(),lie,num);
			if(!checkflag){
				$("#id"+lie+"-"+num).val('');
				$("#tqm"+lie+"-"+num).val('');
				$.confirm("当前"+ nbbh+"的检测类型不存在,上机时无法关联标本编号,需重新确认",function(result) {
					if(result){
						setTimeout(function(){
							$("#nbbh"+lie+"-"+num).focus();
						},200);
					}
				});
				resetImg();
				return;
			}
		}
	});
}

function getDetectionData(nbbh,mrhz,hz,lie,num,qf,position) {
	$.ajax({
		type:'post',
		url:"/inspection/inspection/pagedataGetSjsyInfo",
		cache: false,
		async: false,
		data: {"nbbm":nbbh,"wkrq":$("#ajaxForm #wkrq").val(),"wkid":$("#ajaxForm #wkid").val(),"jcdw":$("#ajaxForm #jcdw").val(),"access_token":$("#ac_tk").val(),"qtsyrq":$("#ajaxForm #wkrq").val()},
		dataType:'json',
		success:function(data){
			if(nbbh.substr(0,3)=="NC-"||nbbh.substr(0,3)=="PC-" ||nbbh.substr(0,3)=="DC-"|| nbbh.substr(0,4)=="DBL-"){
				var html = "";
				html += "<option value=''>--请选择--</option>";
				$.each(data.tqmxList,function(i){
					var text="";
					var split = data.tqmxList[i].nbbh.split("-");
					if(split.length>1){
						text+="-"+split[split.length-1];
					}else{
						text=data.tqmxList[i].nbbh;
					}
					var nd="";
					if(data.tqmxList[i].cdna){
						nd=data.tqmxList[i].cdna;
					}else if(data.tqmxList[i].hsnd){
						nd=data.tqmxList[i].hsnd;
					}
		
					if(i==0){
						html +="<option value='" + data.tqmxList[i].tqmxid + "' selected>" + text+"&nbsp;&nbsp;"+nd + "</option>"
					}else{
						html +="<option value='" + data.tqmxList[i].tqmxid + "'>" + text+"&nbsp;&nbsp;"+nd + "</option>"
					}
				});
				$("#ajaxForm #tqmx"+lie+"-"+num).empty();
				$("#ajaxForm #tqmx"+lie+"-"+num).append(html);
				$("#ajaxForm #tqmx"+lie+"-"+num).trigger("chosen:updated");
				if(data. list!=null&&data.list.length>0){
					$("#id"+lie+"-"+num).val(data.list[0].syglid);
				}
				return;
			}
			//返回值
			if(data.list!=null&&data.list.length>0){
				var checkStr="";
				for(var i=1;i<25;i++){
					for(var j=1;j<9;j++){
						var id = $("#id"+i+"-"+j).val();
						if(id){
							checkStr=checkStr+','+id;
						}
					}
				};

				if(mrhz){//如果默认后缀不为空
					var isFind=false;
					for(var i=0;i<data.list.length;i++){
						if(checkStr.indexOf(data.list[i].syglid)==-1&&mrhz==data.list[i].kzcs1){//判断当前页面是否已经存在这一条数据
							isFind=true;
							//判断当前输入框内是否有后缀，如果有则匹配相同后缀赋值，如果没有则取当前页面没有的后缀赋值
							if(hz){
								if(hz==data.list[i].kzcs1){
									//说明默认后缀匹配成功，直接拼上后缀以及对tqm以及id赋值
									setLibSuffix(lie,num,nbbh,data.list[i],qf,position);
									break;
								}
							}else{
								//说明默认后缀匹配成功，直接拼上后缀以及对tqm以及id赋值
								setLibSuffix(lie,num,nbbh,data.list[i],qf,position);
								break;
							}
						}
					}
					//说明默认后缀匹配失败
					if(!isFind){
						var isMatch=false;
						for(var i=0;i<data.list.length;i++){
							if(checkStr.indexOf(data.list[i].syglid)==-1){//判断当前页面是否已经存在这一条数据
								isMatch=true;
								//判断当前输入框内是否有后缀，如果有则匹配相同后缀赋值，如果没有则取当前页面没有的后缀赋值
								if(hz){
									if(hz==data.list[i].kzcs1){
										setLibSuffix(lie,num,nbbh,data.list[i],qf,position);
										break;
									}
								}else{
									setLibSuffix(lie,num,nbbh,data.list[i],qf,position);
									break;
								}
							}
						}
						if(!isMatch){
							setLibSuffix(lie,num,nbbh,data.list[0],qf,position);
						}
					}
				}else{//说明默认后缀为空
					var isMatch=false;
					for(var i=0;i<data.list.length;i++){
						if(checkStr.indexOf(data.list[i].syglid)==-1){//判断当前页面是否已经存在这一条数据
							isMatch=true;
							//判断当前输入框内是否有后缀，如果有则匹配相同后缀赋值，如果没有则取当前页面没有的后缀赋值
							if(hz){
								if(hz==data.list[i].kzcs1){
									setLibSuffix(lie,num,nbbh,data.list[i],qf,position);
									break;
								}
							}else{
								setLibSuffix(lie,num,nbbh,data.list[i],qf,position);
								break;
							}
						}
					}
					if(!isMatch){
						setLibSuffix(lie,num,nbbh,data.list[0],qf,position);
					}
				}
			}
			setExtractInfo(data.tqmxList,$("#id"+lie+"-"+num).val(),lie,num);
		}
	});
}

function setExtractInfo(list,syglid,lie,num,tqmxid){
    console.log("lie:"+lie + " num:"+num)
	if(list!=null&&list.length>0){
		//为防止用户已经更改过提取情况下，离开焦点又被重置的情况， 2024-02-05
		//覆盖前先判断当前的tqmx的val是否是在list里,如果在则证明就是原有的数据，则不进行替换，直接返回
		var now_tqmx_val = $("#ajaxForm #tqmx"+lie+"-"+num).val()
		if(now_tqmx_val && $("#ajaxForm #tqmx"+lie+"-"+num+" option").length > 1){
			for(var i=0;i<list.length;i++){
				if(now_tqmx_val==list[i].tqmxid){
					return
				}
			}
		}
		var checkStr="";
		for(var i=1;i<=24;i++){
			for(var j=1;j<=8;j++){
				if(i == lie && j == num)
					continue;
				if($("#ajaxForm #tqmx"+i+"-"+j).val()){
					checkStr+=","+$("#ajaxForm #tqmx"+i+"-"+j).val();
				}
			}
		}
		var html = "";
		html += "<option value=''>--请选择--</option>";
		var xh=0;
		if(tqmxid){
			for(var i=0;i<list.length;i++){
				if(tqmxid==list[i].tqmxid){
					xh=i;
					break;
				}
			}
		}else{
			if(syglid){
				var isFind=false;
				for(var i=0;i<list.length;i++){
					if(syglid==list[i].syglid&&checkStr.indexOf(list[i].tqmxid)==-1){
						isFind=true;
						break;
					}
				}
				for(var i=0;i<list.length;i++){
					if(syglid==list[i].syglid){
						xh=i;
						if(!isFind||checkStr.indexOf(list[i].tqmxid)==-1){
							break;
						}
					}
				}
			}else{
				for(var i=0;i<list.length;i++){
					if(checkStr.indexOf(list[i].tqmxid)==-1){
						xh=i;
						break;
					}
				}
			}
		}
		$.each(list,function(i){
			var text="";
			var split = list[i].nbbh.split("-");
			if(split.length>1){
				text+="-"+split[split.length-1];
			}else{
				text="";
			}
			var nd="";
			if(list[i].cdna){
				nd=list[i].cdna;
			}else if(list[i].hsnd){
				nd=list[i].hsnd;
			}

			if(i==xh){
				html +="<option value='" + list[i].tqmxid + "' selected>" + text+"&nbsp;&nbsp;"+nd + "</option>"
			}else{
				html +="<option value='" + list[i].tqmxid + "'>" + text+"&nbsp;&nbsp;"+nd + "</option>"
			}
		});
		$("#ajaxForm #tqmx"+lie+"-"+num).empty();
		$("#ajaxForm #tqmx"+lie+"-"+num).append(html);
		$("#ajaxForm #tqmx"+lie+"-"+num).trigger("chosen:updated");
	}else{
		var html = "";
		html += "<option value=''>--请选择--</option>";
		$("#ajaxForm #tqmx"+lie+"-"+num).empty();
		$("#ajaxForm #tqmx"+lie+"-"+num).append(html);
		$("#ajaxForm #tqmx"+lie+"-"+num).trigger("chosen:updated");
		var  id = "#nbbh"+lie+"-"+num;
		var nbbh = $("#nbbh"+lie+"-"+num).val();
        changeColorForNbbm(id,nbbh);
	}
}


document.onkeydown=function(e){
	if(e.keyCode==123)
		return false;
};
function initZdpb(){

    if(tqmxDtoList&&tqmxDtoList.length>0){
        var bhhz=$("#zdpbin").val()
        var bbhhz=$("#zdpbnotin").val()
        var hzmc=tqmxDtoList[0][pxzd];
        var index=0;
        var zjindex=0;
        for(var i=0;i<tqmxDtoList.length;i++){
            var bhflg=false;
            var bbhflg=true;
            if(bhhz!=""&&bhhz!=null){
                var bhArr=bhhz.split(",");
                for(var j=0;j<bhArr.length;j++){
                    if(bhArr[j]!=""&&bhArr[j]==tqmxDtoList[i].kzcs1){
                        bhflg=true;
                        break;
                    }
                }
            }else{
                bhflg=true
            }

            if(bbhhz!=""&&bbhhz!=null){
                var bbhArr=bbhhz.split(",");
                for(var j=0;j<bbhArr.length;j++){
                    if(bbhArr[j]!=""&&bbhArr[j]==tqmxDtoList[i].kzcs1){
                        bbhflg=false;
                        break;
                    }
                }
            }else{
                bbhflg=true
            }

            if(!tqmxDtoList[i].nbbh||!bhflg||!bbhflg){
                continue;
            }
            zjindex++;

            if(hzmc==tqmxDtoList[i][pxzd]){
                if(zjindex>=9){
                   index=parseInt(index+8)
                   zjindex=1;
                }
            }else{
                zjindex=1;
                if($("input[xh='"+parseInt(index+zjindex)+"']").val()!=null&&$("input[xh='"+parseInt(index+zjindex)+"']").val().trim()!=""){
                    index=parseInt(index+8)
                }
                hzmc=tqmxDtoList[i][pxzd]
            }
            $("input[xh='"+parseInt(index+zjindex)+"']").val(tqmxDtoList[i].nbbh+(tqmxDtoList[i].kzcs1?('-'+tqmxDtoList[i].kzcs1):""));
            var id=$("input[xh='"+parseInt(index+zjindex)+"']").attr("id")
            changeColorForNbbm("#"+id,tqmxDtoList[i].nbbh+(tqmxDtoList[i].kzcs1?('-'+tqmxDtoList[i].kzcs1):""))
            console.log(parseInt(index+zjindex))
            var attrid=$("input[xh='"+parseInt(index+zjindex)+"']").attr("id");
            if(attrid){
                var fgid=$("input[xh='"+parseInt(index+zjindex)+"']").attr("id").replace("nbbh","");
                //             if(tqmxDtoList[i].tqmxDtoList){
                //                $("#tqm"+fgid).val(tqmxDtoList[i].dydm?tqmxDtoList[i].dydm:'')
                //                $("#id"+fgid).val(tqmxDtoList[i].syglid?tqmxDtoList[i].syglid:'')
                //             }
                 $("#tqm"+fgid).val(tqmxDtoList[i].dydm?tqmxDtoList[i].dydm:'')
                 $("#id"+fgid).val(tqmxDtoList[i].syglid?tqmxDtoList[i].syglid:'')
                 $("#lie"+fgid).text(tqmxDtoList[i].jth);
                 $("#lie"+fgid).val(tqmxDtoList[i].jth);
                setExtractInfo(tqmxDtoList[i].tqmxDtoList,$("#"+id).val(),fgid.split("-")[0],fgid.split("-")[1])
            }


        }
    }
    resetImg();
}
function filter(){
    for(var i=1;i<193;i++){
        $("input[xh='"+i+"']").val("");
        $("input[xh='"+i+"']").css("background-color","white");
        var id= $("input[xh='"+i+"']").attr("id")
        $("#ajaxForm #tqm"+id.replace("nbbh","")).val("");
        $("#ajaxForm #id-"+id.replace("nbbh","")).val("");
        $("#success-"+i).css("display","none")
        $("#error-"+i).css("display","none")
         $("#ajaxForm #lie"+id.replace("nbbh","")).val("");
          $("#ajaxForm #lie"+id.replace("nbbh","")).text("")
        var html = "";
        html += "<option value=''>--请选择--</option>";
        $("#ajaxForm #tqmx"+id.replace("nbbh","")).empty();
        $("#ajaxForm #tqmx"+id.replace("nbbh","")).append(html);
        $("#ajaxForm #tqmx"+id.replace("nbbh","")).trigger("chosen:updated");
    }
    initZdpb()
}
function checkBb(bm){
    var nbbm=$("#bbjc").val();
    if(!nbbm||nbbm.trim()==""){
         for(var i=1;i<193;i++){
                $("input[xh="+i+"]").css("border-color","#ccc")
            }
        return
    }
    for(var i=1;i<193;i++){
        $("input[xh="+i+"]").css("border-color","#ccc")
        if($("input[xh="+i+"]").val().indexOf(nbbm)!=-1){
            $("input[xh="+i+"]").css("border-color","red");
        }
    }

}
function printWk(){
    window.open("/experiment/library/exportLibrary?access_token="+$("#ac_tk").val()+"&wkid="+$("#wkid").val())
}
$(function(){

     initZdpb();

     $("#ajaxForm #zdpbCheck").bootstrapSwitch({
        handleWidth: '25px',
        labelWidth: '25px',
        height: '30px',
        onText : "开",      // 设置ON文本
        offText : "关",    // 设置OFF文本
        onColor : "success",// 设置ON文本颜色     (info/success/warning/danger/primary)
        offColor : "info",  // 设置OFF文本颜色        (info/success/warning/danger/primary)
        size : "normal",    // 设置控件大小,从小到大  (mini/small/normal/large)
        // 当开关状态改变时触发
        onSwitchChange : function(event, state) {
            if (state == true) {
                $('#ajaxForm #zdpb').val("1");
                $("#zdpbin").prop("readOnly", false);
                $("#zdpbnotin").prop("readOnly", false);
                if(!(tqmxDtoList&&tqmxDtoList.length>0)){
                    changeJcdw()
                }
                initZdpb();
                checkBb();

            } else {
                $('#ajaxForm #zdpb').val("0");
                $("#zdpbin").prop("readOnly", true);
                $("#zdpbnotin").prop("readOnly", true);
                for(var i=1;i<193;i++){
                    $("input[xh='"+i+"']").val("");
                    $("input[xh='"+i+"']").css("background-color","white");
                    var id= $("input[xh='"+i+"']").attr("id")
                    $("#ajaxForm #tqm"+id.replace("nbbh","")).val("");
                    $("#ajaxForm #id-"+id.replace("nbbh","")).val("");
                    $("#success-"+i).css("display","none")
                    $("#error-"+i).css("display","none")
                     $("#ajaxForm #lie"+id.replace("nbbh","")).val("");
                      $("#ajaxForm #lie"+id.replace("nbbh","")).text("")
                    var html = "";
                    html += "<option value=''>--请选择--</option>";
                    $("#ajaxForm #tqmx"+id.replace("nbbh","")).empty();
                    $("#ajaxForm #tqmx"+id.replace("nbbh","")).append(html);
                    $("#ajaxForm #tqmx"+id.replace("nbbh","")).trigger("chosen:updated");
                }
                checkBb();
            }
        }
    });
     if ( "1"== $('#ajaxForm #zdpb').val()){

         $("#ajaxForm #zdpbCheck").bootstrapSwitch("state", true, true);
         $("#zdpbin").prop("readOnly", false);
         $("#zdpbnotin").prop("readOnly", false);
     }else{
         $("#zdpbin").prop("readOnly", true);
         $("#zdpbnotin").prop("readOnly", true);
         for(var i=1;i<193;i++){
             $("input[xh='"+i+"']").val("");
             $("input[xh='"+i+"']").css("background-color","white");
             var id= $("input[xh='"+i+"']").attr("id")
             $("#ajaxForm #tqm"+id.replace("nbbh","")).val("");
             $("#ajaxForm #id-"+id.replace("nbbh","")).val("");
             $("#success-"+i).css("display","none")
             $("#error-"+i).css("display","none")
             $("#ajaxForm #lie"+id.replace("nbbh","")).val("");
             $("#ajaxForm #lie"+id.replace("nbbh","")).text("")
             var html = "";
             html += "<option value=''>--请选择--</option>";
             $("#ajaxForm #tqmx"+id.replace("nbbh","")).empty();
             $("#ajaxForm #tqmx"+id.replace("nbbh","")).append(html);
             $("#ajaxForm #tqmx"+id.replace("nbbh","")).trigger("chosen:updated");
         }
     }

	$("#ajaxForm #checkbox").bootstrapSwitch({
		handleWidth: '25px',
		labelWidth: '25px',
		onText : "开",      // 设置ON文本
		offText : "关",    // 设置OFF文本
		onColor : "success",// 设置ON文本颜色     (info/success/warning/danger/primary)
		offColor : "info",  // 设置OFF文本颜色        (info/success/warning/danger/primary)
		size : "normal",    // 设置控件大小,从小到大  (mini/small/normal/large)
		// 当开关状态改变时触发
		onSwitchChange : function(event, state) {
			if (state == true) {
				$('#ajaxForm #checkDiv').attr("style","display:block;");
				for (let i = 1; i < 25; i++) {
					$('#ajaxForm #li'+i).attr("style","display:none;");
					$('#ajaxForm #check'+i).attr("style","display:block;text-align:left;");
					for (let j = 1; j < 9; j++) {
						$('#ajaxForm #lie'+i+'-'+j).attr("style","display:none;");
						$('#ajaxForm #check'+i+'-'+j).attr("style","display:block;");
					}
				}

			} else {
				$('#ajaxForm #checkDiv').attr("style","display:none;");
				for (let i = 1; i < 25; i++) {
					$('#ajaxForm #li'+i).attr("style","display:block;text-align:left;padding-left: 0");
					$('#ajaxForm #check'+i).attr("style","display:none;");
					for (let j = 1; j < 9; j++) {
						$('#ajaxForm #lie'+i+'-'+j).attr("style","display:block;");
						$('#ajaxForm #check'+i+'-'+j).attr("style","display:none;");
					}
				}
			}
		}
	});

	$(".liem").attr("style","min-height:20px;");
	if($("#format").val()=="addSaveLibrary"||$("#format").val()=="librarySaveExperiment"){
		var date = new Date(); //获取当前日期时间戳
		var hour = date.getHours();//获取当前时间小时值
		var wkrq= date;
		//如果为8点之前的，那么文库日期自动设置为前一天的，如果超过8点，则设置为当天
		if(hour<8){
			wkrq= date-1000*60*60*24;
		}
		laydate.render({
			elem: '#ajaxForm #wkrq'
		  	,theme: '#2381E9'
		  	,value: new Date(wkrq)
		  	,done: function(value, date, endDate){
                  changeJcdw();
             }
		});
	}else{
		laydate.render({
			elem: '#ajaxForm #wkrq'
		  	,theme: '#2381E9'
		});
	}
	
	jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
	//打开新增页面光标显示在第一个输入框
	setTimeout(function(){
		$("#nbbh1-1").focus();
	},200);
	
	var width=$(window).width();
	if(width<768){
		$("#ywl1").attr("style","display:none;");
		$("#ywl2").attr("style","display:none;");
	}

	if($("#format").val()=="modSaveLibrary"){
		var wkid=$("#ajaxForm #wkid").val();
		$.ajax({ 
		    type:'post',  
		    url:"/experiment/library/pagedataGetXgxx",
		    cache: false,
		    data: {"wkid":wkid,"access_token":$("#ac_tk").val()},
		    dataType:'json', 
		    success:function(data){
		    	//返回值
		    	var list=data;
		    	var lie;
		    	var num;
				for(var i=0;i<list.length;i++){
					var sz={"nbbh":'',"xh":''};
					var beforeNbbh=list[i].nbbh;
					if(list[i].nbbh.indexOf("--")!=-1){
						var split1 = list[i].nbbh.split("--");
						var hz=[];
						for(var k=1;k < split1.length;k++){
							var t_split2 = split1[k].split("-");
							if(t_split2.length > 1){
								for(var j=1;j < t_split2.length;j++){
									hz.push(t_split2[j])
								}
							}
						}
						beforeNbbh =split1[0];
						for(var x=0;x < hz.length;x++){
							beforeNbbh = beforeNbbh + "-" + hz[x]
						}
					}
					sz.nbbh=beforeNbbh;
					sz.xh=list[i].xh;
					beforeLibraryData.push(sz);
					if(list[i].xh%8==0){
						lie=parseInt(list[i].xh/8);
						num=8;
						$("#lie"+lie+"-"+num).text(list[i].jtxx);
						$("#nbbh"+lie+"-"+num).val(list[i].nbbh);
						$("#quantity"+lie+"-"+num).val(list[i].quantity);
						$("#tqm"+lie+"-"+num).val(list[i].tqm);
						$("#id"+lie+"-"+num).val(list[i].syglid);
						var nbbh = $("#nbbh"+lie+"-"+num).val();
						var  id = "#nbbh"+lie+"-"+num;
						changeColorForNbbm(id,nbbh);
						setExtractInfo(list[i].tqmxDtos,null,lie,num,list[i].tqmxid);
					}else{
						lie=parseInt(list[i].xh/8)+1;
						num=list[i].xh%8;
						$("#lie"+lie+"-"+num).text(list[i].jtxx);
						$("#nbbh"+lie+"-"+num).val(list[i].nbbh);
						$("#quantity"+lie+"-"+num).val(list[i].quantity);
						$("#tqm"+lie+"-"+num).val(list[i].tqm);
						$("#id"+lie+"-"+num).val(list[i].syglid);
						var nbbh = $("#nbbh"+lie+"-"+num).val();
						var  id = "#nbbh"+lie+"-"+num;
						changeColorForNbbm(id,nbbh);
						setExtractInfo(list[i].tqmxDtos,null,lie,num,list[i].tqmxid);
					}
				}
				resetImg();
		    }
		});
	}

	if($("#format").val()=="mergeSaveLibrary"){
		var wkid=$("#ajaxForm #wkid").val();
		$.ajax({
			type:'post',
			url:"/experiment/library/pagedataGetXgxx",
			cache: false,
			data: {"wkid":wkid,"ids":$("#ajaxForm #ids").val(),"access_token":$("#ac_tk").val()},
			dataType:'json',
			success:function(data){
				//返回值
				var list=data;
				var lie;
				var num;
				var xh=list[0].xh;
				var wkid=list[0].wkid;
				for(var i=0;i<list.length;i++,xh++){
                    if(list[i].wkid != wkid){
						wkid = list[i].wkid
						if (xh%8!=1){//避免出现空列,余数不为1才后退一列
							xh = (parseInt(xh/8)+1)*8+1;
							if (xh>192){//页面共192个孔信息
								$.error( "合并信息内容过多！！",function() { });
								$("#ajaxForm #isOverFlow").val(1);
							}
						}
					}
					if (xh>192){//页面共192个孔信息
						$.error( "合并信息内容过多！！",function() { });
						$("#ajaxForm #isOverFlow").val(1);
					}
					var sz={"nbbh":'',"xh":''};
					var beforeNbbh=list[i].nbbh;
					if(list[i].nbbh.indexOf("--")!=-1){
						var split1 = list[i].nbbh.split("--");
						var hz=[];
						for(var k=1;k < split1.length;k++){
							var t_split2 = split1[k].split("-");
							if(t_split2.length > 1){
								for(var j=1;j < t_split2.length;j++){
									hz.push(t_split2[j])
								}
							}
						}
						beforeNbbh =split1[0];
						for(var x=0;x < hz.length;x++){
							beforeNbbh = beforeNbbh + "-" + hz[x]
						}
					}
					sz.nbbh=beforeNbbh;
					sz.xh=xh;
					beforeLibraryData.push(sz);
					if(xh%8==0){
						lie=parseInt(xh/8);
						num=8;
						$("#lie"+lie+"-"+num).text(list[i].jtxx);
						$("#nbbh"+lie+"-"+num).val(list[i].nbbh);
						$("#quantity"+lie+"-"+num).val(list[i].quantity);
						$("#tqm"+lie+"-"+num).val(list[i].tqm);
						$("#id"+lie+"-"+num).val(list[i].syglid);
						var nbbh = $("#nbbh"+lie+"-"+num).val();
						var  id = "#nbbh"+lie+"-"+num;
						changeColorForNbbm(id,nbbh);
						setExtractInfo(list[i].tqmxDtos,null,lie,num,list[i].tqmxid);
					}else{
						lie=parseInt(xh/8)+1;
						num=xh%8;
						$("#lie"+lie+"-"+num).text(list[i].jtxx);
						$("#nbbh"+lie+"-"+num).val(list[i].nbbh);
						$("#quantity"+lie+"-"+num).val(list[i].quantity);
						$("#tqm"+lie+"-"+num).val(list[i].tqm);
						$("#id"+lie+"-"+num).val(list[i].syglid);
						var nbbh = $("#nbbh"+lie+"-"+num).val();
						var  id = "#nbbh"+lie+"-"+num;
						changeColorForNbbm(id,nbbh);
						setExtractInfo(list[i].tqmxDtos,null,lie,num,list[i].tqmxid);
					}
				}
				resetImg();
			}
		});
	}

	if($("#format").val()=="librarySaveExperiment"){
		var list = JSON.parse($("#wkmx_json").val());
		var thisxh=1;
		if (list && list.length > 0) {
			for(var k=0;k<list.length;k++){
				var xh = parseInt(thisxh);
				var lie=0;
				var num=0;
				if(xh%8==0){
					lie=parseInt(xh/8);
					num=8;
					$("#tqm"+lie+"-"+num).val('');
					$("#id"+lie+"-"+num).val('');
				}else{
					lie=parseInt(xh/8)+1;
					num=xh%8;
					$("#tqm"+lie+"-"+num).val('');
					$("#id"+lie+"-"+num).val('');
				}
				var checkStr="";
				for(var i=1;i<25;i++){
					for(var j=1;j<9;j++){
						var id = $("#id"+i+"-"+j).val();
						if(id){
							checkStr=checkStr+','+id;
						}
					}
				}
				if(checkStr.indexOf(list[k].syglid)==-1){//判断当前页面是否已经存在这一条数据
					$("input[xh='"+thisxh+"']").val(list[k].nbbh+'-'+list[k].kzcs1);
					$("#tqm"+lie+"-"+num).val(list[k].nbzbm);
					$("#id"+lie+"-"+num).val(list[k].syglid);
				}
				setExtractInfo(list[k].tqmxDtos,list[k].syglid,lie,num);
				var nbbm=$("input[xh='"+thisxh+"']").val();
				var zbbm;
				if(nbbm.substr(nbbm.length-4,4)=="-DNA" || nbbm.substr(nbbm.length-4,4)=="-RNA" || nbbm.substr(nbbm.length-3,3)=="-HS" ||nbbm.substr(nbbm.length-8,8)=="-DNA-REM"){
					if(nbbm.substr(nbbm.length-4,4)=="-RNA"){
						zbbm="R";
					}else{
						zbbm="D";
					}
				}
				var jcdw = $("#ajaxForm #jcdw").val();
				//从NGS管理表中获取接头信息
				var wkrq=$("#ajaxForm #wkrq").val();
				if(zbbm){
					$.ajax({
						async:false,
						type:'post',
						url:"/experiment/library/pagedataGetJtFromNGS",
						cache: false,
						data: {"zbbm":zbbm,"nbbm":list[k].nbbh,"jcdw":jcdw,"lrsj":wkrq,"access_token":$("#ac_tk").val()},
						dataType:'json',
						success:function(data){
							//返回值
							if(data.ngsglDtos!=null&&data.ngsglDtos.length>0){
								if(data.ngsglDtos.length==1){
									var nbbhid=$("input[xh='"+thisxh+"']").attr("id");
									var nbbhidlength=nbbhid.length;
									var jtid="lie"+nbbhid.substr(4,3+nbbhidlength-4);
									if($("#"+jtid).text()==null || $("#"+jtid).text()==''){
										$("#"+jtid).text(data.ngsglDtos[0].jth);
										$("#"+jtid).val(data.ngsglDtos[0].jth);
									}
								}
							}
						}
					});
				}
				var nbbh = $("#nbbh"+lie+"-"+num).val();
				var  id = "#nbbh"+lie+"-"+num;
				changeColorForNbbm(id,nbbh)
				thisxh++;
			}
			resetImg();
		}
	}
	initCxyxx(true)
	$("#ajaxForm #jcdw").change(function(){
	    initCxyxx(false)
	})
	$("#ajaxForm #yqlx").change(function(){
	    var cxyid=$("#ajaxForm #yqlx").val();
        var zdpb=$('#ajaxForm #zdpb').val();
        var jcdw = $("#jcdw").val();
        if(zdpb=='1'&&cxyid!=null){
             $.ajax({
                    type:'post',
                    url:"/experiment/potency/pagedataGetZdpbWkList",
                    cache: false,
                    data: {"jcdw":jcdw,"access_token":$("#ac_tk").val(),"wkrq":$("#wkrq").val(),"yqlx":cxyid},
                    dataType:'json',
                    success:function(data){
                        tqmxDtoList=data.wklist
                        pxzd=data.pxzd
                        filter();
                    }
                })
        }
		initSjxz();
	})

})
function changeJcdw(){
    if($("#zdpb").val()=='0'||$("#zdpb").val()==null||!$("#zdpb").val()){
        return
    }
	//获取证件类型代码，并设置
	var jcdw = $("#jcdw").val();
   $.ajax({
        type:'post',
        url:"/experiment/potency/pagedataGetZdpbWkList",
        cache: false,
        data: {"jcdw":jcdw,"access_token":$("#ac_tk").val(),"wkrq":$("#wkrq").val()},
        dataType:'json',
        success:function(data){
            tqmxDtoList=data.wklist
            pxzd=data.pxzd
            filter();
        }
   })
   initCxyxx(false);
}
function resetImg(){
	for(var i=1;i<193;i++){
		if($("input[xh='"+i+"']").val()) {
			if(i%8==0){
				var lie=parseInt(i/8);
				var num=8;
				if($("#id"+lie+"-"+num).val()){
					$("#success-"+i).show();
					$("#error-"+i).hide();
				}else{
					$("#success-"+i).hide();
					$("#error-"+i).show();
				}
			}else{
				var lie=parseInt(i/8)+1;
				var num=i%8;
				if($("#id"+lie+"-"+num).val()){
					$("#success-"+i).show();
					$("#error-"+i).hide();
				}else{
					$("#success-"+i).hide();
					$("#error-"+i).show();
				}
			}
		}
	}
}

//设置文库后缀
function setLibSuffix(lie,num,nbbh,data,qf,position){
	var s_qf ="";
	if(qf){
		for(var i=0;i<qf.length;i++)
		{
			var t_qf = qf[i].replaceAll("-","")
			if(t_qf)
				s_qf = s_qf + "--" + t_qf
		}
	}
	
	if(position&&"0"==position){
		$("#nbbh"+lie+"-"+num).val(nbbh+s_qf+'-'+data.kzcs1);
	}else if(position&&"1"==position){
		$("#nbbh"+lie+"-"+num).val(nbbh+'-'+data.kzcs1+s_qf);
	}else{
		$("#nbbh"+lie+"-"+num).val(nbbh+'-'+data.kzcs1);
	}
	$("#tqm"+lie+"-"+num).val(data.dydm);
	$("#id"+lie+"-"+num).val(data.syglid);
	var jtid="lie"+lie+"-"+num;
	if(data.jth&&($("#"+jtid).text()==null || $("#"+jtid).text()=='')){
		$("#"+jtid).text(data.jth);
		$("#"+jtid).val(data.jth);
	}
}

function changeColorForNbbm(id,nbbh){
	if(nbbh.indexOf("--")!=-1){
		var split1 = nbbh.split("--");
		var split2 = split1[1].split("-");
		if(split2.length>1){
			var tmp="";
			for(var i=1;i<split2.length;i++){
				tmp =tmp +"-"+split2[i];
			}
			nbbh = split1[0] + tmp;
		}else{
			nbbh=split1[0];
		}
	}
	console.log("color");
	if(nbbh.substring(nbbh.lastIndexOf("-"),nbbh.length)=="-DNA"){
		$(id).attr("style","height:30px;background-color:#CEF6CE;");
	}else if(nbbh.substring(nbbh.lastIndexOf("-"),nbbh.length)=="-RNA"){
		$(id).attr("style","height:30px;background-color:#F5DA81;");
	}else if(nbbh.substring(nbbh.lastIndexOf("-"),nbbh.length)=="-tNGS"){
		$(id).attr("style","height:30px;background-color: rgba(113,214,255,1);");
	}else if(nbbh.substring(nbbh.lastIndexOf("-"),nbbh.length)=="-tNGSA"){
        $(id).attr("style","height:30px;background-color: #ea88e9;");
    }else if(nbbh.substring(nbbh.lastIndexOf("-"),nbbh.length)=="-TBtNGS"){
        $(id).attr("style","height:30px;background-color: rgba(114,125,255,1);");
    }else if(nbbh.substring(nbbh.lastIndexOf("-"),nbbh.length)=="-HS"){
		$(id).attr("style","height:30px;background-color: #FFB6C1;");
	}else if(nbbh.substring(nbbh.lastIndexOf("-"),nbbh.length)=="-REM"){
		$(id).attr("style","height:30px;background-color: #b6a2de;");
	}else{
		$(id).attr("style","height:30px;");
	}
}
function sjphlr1(){
	$("#ajaxForm #sjph1").val($("#ajaxForm #sj1").val());
}

function sjphlr2(){
	$("#ajaxForm #sjph2").val($("#ajaxForm #sj2").val());
}

function sjphlr3(){
    if($("#ajaxForm #xsbj").val() && "1"==$("#ajaxForm #xsbj").val()){
        $("#ajaxForm #sjph3").val($("#ajaxForm #sj3").val());
    }
}

function initCxyxx(isInit){
    $.ajax({
        type:'post',
        url:"/experiment/library/pagedataJcsjByFcsidAndJclb",
        cache: false,
        data: { "jclb":"SEQUENCER_CODE",
                "access_token":$("#ac_tk").val(),
                "fcsid":$("#jcdw").val()},
        dataType:'json',
        success:function(data){
            var html = '<option value="">请选择</option>';
            var wksjhtml=''
            if(data){
                var list = data.list;
                if(list && list.length>0){
                    for(var i=0;i<list.length;i++){
                        var item = list[i];
                        if(($("#yqlxValue").val() && $("#yqlxValue").val() == item.csid && isInit) || (list.length == 1)){
                            html += '<option value="'+item.csid+'" cskz2="'+item.cskz2+'" selected>'+item.csdm + '-' + item.csmc + '</option>';
                        }else{
                            html += '<option value="'+item.csid+'" cskz2="'+item.cskz2+'">'+item.csdm + '-' + item.csmc + '</option>';
                        }
                    }
                }
                var wksjList= data.wksjList
                if(wksjList && wksjList.length>0){
                    for(var i=0;i<wksjList.length;i++){
                        var item = wksjList[i];
                         wksjhtml+='<div class="col-md-3 col-sm-3 col-xs-3 text-center" >'
                         wksjhtml+='<div class="form-group">'
                         wksjhtml+='<label class="col-md-4 col-sm-4 col-xs-4 control-label" style="padding: 0" ">'+item.title+'</label>'
                         wksjhtml+=' <div class="col-sm-8 col-md-8 col-xs-8">'
                         wksjhtml+=' <input type="text" id="'+item.variable+'" name="'+item.variable+'" value="'+item.value+'" class="form-control" style="height:30px;"/>'
                         wksjhtml+=' </div>'
                         wksjhtml+=' </div>'
                         wksjhtml+=' </div>'
                    }
                }
            }
            if(!isInit){
                $("#ajaxForm #wksjDiv").empty();
                 $("#ajaxForm #wksjDiv").append(wksjhtml);
            }

            $("#ajaxForm #yqlx").empty();
            $("#ajaxForm #yqlx").append(html);
            $("#ajaxForm #yqlx").trigger("chosen:updated");
			initSjxz();
        }
   })
}

function initSjxz(){

	var cskz2 = $('#yqlx option:selected').attr("cskz2");
	if(cskz2!=undefined){
	    cskz2 = cskz2.replace(/\s+/g, '')
	}

	var sjxzJson = $("#sjxzJson").val();
	var sjxzList = JSON.parse(sjxzJson);
	var html = '<option value="">请选择</option>';
	if(sjxzList && sjxzList.length>0){
		var list = [];
		for(var i=0;i<sjxzList.length;i++){
			if (cskz2 && cskz2!="null"){
				var split = cskz2.split(",");
				for (let j = 0; j < sjxzList.length; j++) {
					if (split[j] == sjxzList[i].csdm){
						list.push(sjxzList[i]);
					}
				}
			} else{
				list.push(sjxzList[i]);
			}
		}
		for (let i = 0; i < list.length; i++) {
			var item = list[i];
			if(($("#sjxz").val() && $("#sjxz").val() == item.csid) || (list.length == 1)){
				html += '<option value="'+item.csid+'" selected>' + item.csmc + '</option>';
			}else{
				html += '<option value="'+item.csid+'">' + item.csmc + '</option>';
			}
		}
		$("#ajaxForm #sjxz").empty();
		$("#ajaxForm #sjxz").append(html);
		$("#ajaxForm #sjxz").trigger("chosen:updated");


	}
}

