var libPreInput;
var libSecondPreInput;


function findInfo(xh,thisnbbh){
	$.ajax({
		type: 'post',
		async: false,
		url: "/experiment/potency/pagedataGetTqdm",
		cache: false,
		data: {
			"nbbh": thisnbbh,
			"jcdw": $("#ajaxForm #jcdw").val(),
			"access_token": $("#ac_tk").val(),
		},
		dataType: 'json',
		success: function (data) {
			if (data.list && data.list.length > 0){
				if (thisnbbh.substring(0,3) == 'NC-' || thisnbbh.substring(0,3) == 'PC-'|| thisnbbh.substring(0,3) == 'DC-'){
					$("#ajaxFormEdit #nbzbm-"+xh).val((data.list[0].kzcs2?data.list[0].kzcs2:'')+'/'+(data.list[0].kzcs3?data.list[0].kzcs3:''));
					$("#ajaxFormEdit #syglid-"+xh).val((data.list[0].syglid?data.list[0].syglid:''));
				}else if (data.list.length > 1){
					if($("#ajaxForm #mrhz").val()){
						for (let k = 0; k < data.list.length; k++) {
							if (data.list[k].kzcs3 == $("#ajaxForm #mrhz").val()){
								var str = data.list[k].nbbh;
								if (data.list[k].kzcs3){
									str+='-'+data.list[k].kzcs3;
								}
								if (data.dyhz){
									str+='--'+data.dyhz;
								}
								var flag = true;
								var nbzbm = (data.list[k].kzcs2?data.list[k].kzcs2:'')+'/'+(data.list[k].kzcs3?data.list[k].kzcs3:'');
								var syglid = (data.list[k].syglid?data.list[k].syglid:'');
								for(var i=1;i<97;i++){
									if (xh != i && $("#ajaxFormEdit #nbbh-"+i).val() && $("#ajaxFormEdit #nbbh-"+i).val() == str && syglid == $("#ajaxFormEdit #syglid-"+i).val()){
										flag = false;
										break;
									}
								}
								if (flag){
									$("#ajaxFormEdit input[xh='"+xh+"']").val(str);
									$("#ajaxFormEdit #nbzbm-"+xh).val(nbzbm);
									$("#ajaxFormEdit #syglid-"+xh).val(syglid);
									return true;
								}
							}
						}
					}
					for (let k = 0; k < data.list.length; k++) {
						var str = data.list[k].nbbh;
						if (data.list[k].kzcs3){
							str+='-'+data.list[k].kzcs3;
						}
						if (data.dyhz){
							str+='--'+data.dyhz;
						}
						var flag = true;
						var nbzbm = (data.list[k].kzcs2?data.list[k].kzcs2:'')+'/'+(data.list[k].kzcs3?data.list[k].kzcs3:'');
						var syglid = (data.list[k].syglid?data.list[k].syglid:'');
						for(var i=1;i<97;i++){
							if (xh != i && $("#ajaxFormEdit #nbbh-"+i).val() && $("#ajaxFormEdit #nbbh-"+i).val() == str && syglid == $("#ajaxFormEdit #syglid-"+i).val()){
								flag = false;
								break;
							}
						}
						if (flag){
							$("#ajaxFormEdit input[xh='"+xh+"']").val(str);
							$("#ajaxFormEdit #nbzbm-"+xh).val(nbzbm);
							$("#ajaxFormEdit #syglid-"+xh).val(syglid);
							return true;
						}else if(k==0){
							if (thisnbbh.indexOf("-") == -1){
								$("#ajaxFormEdit input[xh='"+xh+"']").val(str);
								$("#ajaxFormEdit #nbzbm-"+xh).val(nbzbm);
								$("#ajaxFormEdit #syglid-"+xh).val(syglid);
							}
						}
					}
				}else{
					var str = data.list[0].nbbh;
					if (data.list[0].kzcs3){
						str+='-'+data.list[0].kzcs3;
					}
					if (data.dyhz){
						str+='--'+data.dyhz;
					}
					$("#ajaxFormEdit input[xh='"+xh+"']").val(str);
					$("#ajaxFormEdit #nbzbm-"+xh).val((data.list[0].kzcs2?data.list[0].kzcs2:'')+'/'+(data.list[0].kzcs3?data.list[0].kzcs3:''));
					$("#ajaxFormEdit #syglid-"+xh).val(data.list[0].syglid?data.list[0].syglid:'');
				}
			}else{
				$("#ajaxFormEdit #nbzbm-"+xh).val("XX/XX");
			}
		}
	});
}
var flag_a = true;
$("#ajaxFormEdit input").blur(function(e){
	var thisxh=$(this).attr("xh");
	if(thisxh){
		var nbbh=$("#ajaxFormEdit input[xh='"+thisxh+"']").val();
		if (flag_a && nbbh){
			findInfo(thisxh,nbbh);
		}
		flag_a = true;
	}
})
//回车事件
$("#ajaxFormEdit input").on('keydown',function(e){
	var thisxh=$(this).attr("xh");
	var id=$(this).attr("id");
	var result = true;
	
	if(libSecondPreInput==17 &&libPreInput==16 && e.keyCode==74){
		if(thisxh){
			addpostfix(thisxh);
		}
        if($(this).val().length >0){
        	var nextxh=parseInt(thisxh)+1;
	    	if(nextxh==97){
	    		$("#ajaxFormEdit input[xh='1']").focus();
	    	}else{
				$("#ajaxFormEdit input[xh='"+nextxh+"']").focus();
	    	}
        }
//        checklsh(thisxh);
    	result = false;
	}
	else if (event.keyCode == 13) {
		if(thisxh){
			addpostfix(thisxh);
		}
    	var nextxh=parseInt(id.split("-")[1])+1;
    	if(nextxh==97){
			if(id.indexOf("nbbh")!=-1){
				$("#ajaxFormEdit input[xh='1']").focus();
			}else if(id.indexOf("hsnd")!=-1){
				$("#ajaxFormEdit #hsnd-1").focus();
			}else if(id.indexOf("cdna")!=-1){
				$("#ajaxFormEdit #cdna-1").focus();
			}else if(id.indexOf("tqykw")!=-1){
				$("#ajaxFormEdit #tqykw-1").focus();
			}else if(id.indexOf("spike")!=-1){
				$("#ajaxFormEdit #spike-1").focus();
			}else if(id.indexOf("xsbs")!=-1){
				$("#ajaxFormEdit #xsbs-1").focus();
			}
    	}else{
			if(id.indexOf("nbbh")!=-1){
				$("#ajaxFormEdit input[xh='"+nextxh+"']").focus();
			}else if(id.indexOf("hsnd")!=-1){
				$("#ajaxFormEdit #hsnd-"+nextxh).focus();
			}else if(id.indexOf("cdna")!=-1){
				$("#ajaxFormEdit #cdna-"+nextxh).focus();
			}else if(id.indexOf("tqykw")!=-1){
				$("#ajaxFormEdit #tqykw-"+nextxh).focus();
			}else if(id.indexOf("spike")!=-1){
				$("#ajaxFormEdit #spike-"+nextxh).focus();
			}else if(id.indexOf("xsbs")!=-1){
				$("#ajaxFormEdit #xsbs-"+nextxh).focus();
			}
    	}
//    	checklsh(thisxh);
    	return false;
    }
	libSecondPreInput = libPreInput
	libPreInput = e.keyCode
	if(!result)
		return result;
});

document.onkeydown=function(e){
	if(e.keyCode==123)
		return false;
};

// //提取管理表临时保存
// function qgglLsSave(){
// 	if($("#ajaxForm #format").val()=="addSavePotency"){
// 		var mc=$("#ajaxForm #mc").val();
// 		var tqid=$("#ajaxForm #tqid").val();
// 		var jcdw=$("#ajaxForm #jcdw").val();
// 		if(mc!=null && mc!=""){
// 			$.ajax({
// 			    type:'post',
// 			    url:"/experiment/potency/pagedataTqglLsSave",
// 			    cache: false,
// 			    data: {"jcdw":jcdw,"tqid":tqid,"mc":mc,"access_token":$("#ac_tk").val()},
// 			    dataType:'json',
// 			    success:function(data){
// 			    	//返回值
// 			    	if(data.status==true){
// 			    		$("#ajaxForm #tqid").val(data.tqglDto.tqid);
// 			    	}
// 			    }
// 			});
// 		}
// 	}
// }
//复制粘贴
document.querySelectorAll("#ajaxFormEdit input[id^='nbbh-']").forEach(function(item) {
	item.addEventListener('paste', function(event) {
	    var inputIdNum = parseInt(event.target.id.split("-")[1]);
	    var inputhang = inputIdNum % 10;
	    var inputlie = Math.floor(inputIdNum / 10);
		var clipText = event.clipboardData.getData('Text');
		console.log(clipText);
		if (clipText) {
			//复制内容为Excel中内容的粘贴操作
			if (clipText.indexOf("\n") != -1) {
				var strings = clipText.split("\n");
				var num = inputIdNum;
				var row = inputlie;
				for (var i = 0; i < strings.length; i++) {
					if (strings[i]) {
						var str = strings[i].split("\t");
						var length = str.length;
						if (str.length > 15) {
							length = 15;
						}
                        for (var j = 0; j < length; j++) { //限制为9列，多出不管
                            console.log(str[j]);
                            if (j == 0) {
                                $("#ajaxFormEdit #nbbh-" + num).val(str[j]);
                            } else if (j == 1) {
                                //如果第二列为纯数字，则按照原方法匹配
                                if(/[+-]?([0-9]*\.[0-9]+|[0-9]+)$/.test(str[1].replace("\r",""))){
                                    $("#ajaxFormEdit #hsnd-" + num).val(str[j]);
                                }
                            } else if (j == 2) {
                                $("#ajaxFormEdit #cdna-" + num).val(str[j]);
                            } else if (j == 3) {
                                $("#ajaxFormEdit #tqykw-" + num).val(str[j]);
                            } else if (j == 4) {
                                $("#ajaxFormEdit #spike-" + num).val(str[j]);
                            } else if (j == 5) {
                                $("#ajaxFormEdit #xsbs-" + num).val(str[j]);
                            } else if (j == 6) {
                                $("#ajaxFormEdit #nbbh-" + (num + 10)).val(str[j]);
                            } else if (j == 7) {
                                $("#ajaxFormEdit #hsnd-" + (num + 10)).val(str[j]);
                            } else if (j == 8) {
                                $("#ajaxFormEdit #cdna-" + (num + 10)).val(str[j]);
                            } else if (j == 9) {
                                $("#ajaxFormEdit #tqykw-" + (num + 10)).val(str[j]);
                            } else if (j == 10) {
                                $("#ajaxFormEdit #spike-" + (num + 10)).val(str[j]);
                            } else if (j == 11) {
                                $("#ajaxFormEdit #xsbs-" + (num + 10)).val(str[j]);
                            } else if (j == 12) {
                                $("#ajaxFormEdit #nbbh-" + (num + 20)).val(str[j]);
                            } else if (j == 13) {
                                $("#ajaxFormEdit #hsnd-" + (num + 20)).val(str[j]);
                            } else if (j == 14) {
                                $("#ajaxFormEdit #cdna-" + (num + 20)).val(str[j]);
                            } else if (j == 15) {
                                $("#ajaxFormEdit #tqykw-" + (num + 20)).val(str[j]);
                            } else if (j == 16) {
                                $("#ajaxFormEdit #spike-" + (num + 20)).val(str[j]);
                            } else if (j == 17) {
                                $("#ajaxFormEdit #xsbs-" + (num + 20)).val(str[j]);
                            }
                        }
						num++;
						row++;
					    findInfo(inputIdNum+i, strings[i].split("\t")[0].replace("\r",""));
					}
				}
				copyText("");
			}

		}
	});
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


function addpostfix(xh){
	var hzqx=$("#ajaxForm #hzqx").val();//系统设置表的设置值，如果为1则添加后缀
	if(hzqx=="1"){
		//检验判断加“-DNA”还是“-RNA”
		var thisnbbh=$("#ajaxFormEdit input[xh='"+xh+"']").val();
		//判断2次触发
		var strings = thisnbbh.split(" ");
		if ( strings.length > 1){
			if (thisnbbh.indexOf('NC-')!= -1 || thisnbbh.indexOf('PC-')!= -1|| thisnbbh.indexOf('DC-')!= -1){
				$("#ajaxFormEdit input[xh='"+xh+"']").val(strings[0]);
			}else if (strings[1].split("/")[1]){
				$("#ajaxFormEdit input[xh='"+xh+"']").val(strings[0]+"-"+strings[1].split("/")[1]);
			}else{
				$("#ajaxFormEdit input[xh='"+xh+"']").val(strings[0]);
			}
			$("#ajaxFormEdit #nbzbm-"+xh).val(strings[1]);
			flag_a = false;
			findInfo(xh,$("#ajaxFormEdit input[xh='"+xh+"']").val());
		}
	}
}



function toreplace(xh){
	var str = $("#ajaxFormEdit input[xh='"+xh+"']").val();
	re = new RegExp(",","g"); 
	rq = new RegExp("，","g");
	var Newstr = str.replace(re, "");
	Newstr = Newstr.replace(rq,"");
	$("#ajaxFormEdit input[xh='"+xh+"']").val(Newstr);
	if($("#ajaxForm #format").val()=="modSavePotency"){
		var tqid=$("#ajaxForm #tqid").val();
		var nbbh=$("#ajaxFormEdit #nbbh-"+xh).val();
		if(tqid==null || tqid==""){
			$.confirm("请先填写提取名称!");
		}else{
			if(nbbh!=null && nbbh!=""){
				var hsnd=$("#ajaxFormEdit #hsnd-"+xh).val();
				var cdna=$("#ajaxFormEdit #cdna-"+xh).val();
				var tqdm=$("#ajaxFormEdit #nbzbm-"+xh).val();
				var syglid=$("#ajaxFormEdit #syglid-"+xh).val();
				var tqmxid=$("#ajaxFormEdit #tqmxid-"+xh).val();
				$.ajax({
				    async:false,
				    type:'post',  
				    url:"/experiment/potency/pagedataTqmxLsSave",
				    cache: false,
					data: {"xh":xh,"tqid":tqid,"nbbh":nbbh,"hsnd":hsnd,"cdna":cdna,"tqdm":tqdm,"tqmxid":tqmxid,"syglid":syglid,"access_token":$("#ac_tk").val()},
					dataType:'json',
				    success:function(data){
						$("#ajaxFormEdit #tqmxid-"+xh).val(data.tqmxid);
				    	//返回值
				    	$("#disdiv").load("/experiment/template/pagedataTemplateListStage");
				    }
				});
			}
		}
	}
}
function checkBb(bm){
    var nbbm=$("#bbjc").val();
    if(!nbbm||nbbm.trim()==""){
         for(var i=1;i<97;i++){
                $("#ajaxFormEdit input[xh="+i+"]").css("border-color","#ccc")
            }
        return
    }
    for(var i=1;i<97;i++){
        $("#ajaxFormEdit input[xh="+i+"]").css("border-color","#ccc")
        if($("#ajaxFormEdit input[xh="+i+"]").val().indexOf(nbbm)!=-1){
            $("#ajaxFormEdit input[xh="+i+"]").css("border-color","red");
        }
    }

}
/**
 * 验证格式(数字)
 * @param value
 * @param element
 * @returns
 */
function check(bm) {
	var id=bm.id;
	var value=$("#"+id).val();
	if (value!=null&&value!=''){
		var result=/^(([1-9][0-9]*)|(([0]\.\d{1,3}|[1-9][0-9]*\.\d{1,3})))$/;
		if(!result.test(value) && value!=0){
			$.error("请填写正确格式，只能填写数字(可以保留三位小数)!");
			$("#"+id).val("")
		}
		else {
			if(value.indexOf(".")==-1){
				if (value.length>5){
					$.error("请填写正确格式,整数只能填写5位!");
					$("#"+id).val("")
				}
			}else {
				if (value.indexOf(".")>5){
					$.error("请填写正确格式,整数部分只能填写5位!");
					$("#"+id).val("")
				}
			}
		}
	}
}
//var bjlsh;//判断前面有漏填的情况下用
//function checklsh(thisxh){
//	var nbbh=$("#nbbh-"+thisxh).val();
//	var nbbhlength=$("#nbbh-"+thisxh).val().length;
//	if(nbbh.substr(0,1)=="C" || nbbh.substr(0,1)=="R"){
//		var lsh=nbbh.substr(nbbhlength-4,3);
//		bjlsh=lsh;
//		if(thisxh!=lsh.replace(/\b(0+)/gi,"")){
//			$.alert("流水号与序号不一致!");
//		}
//	}else if(nbbh.substr(0,1)=="D"){
//		if(thisxh>1){
//			var lastnbbh=$("#nbbh-"+(thisxh-1)).val();
//			var lastnbbhlength=$("#nbbh-"+(thisxh-1)).val().length;
//			var lastlsh=lastnbbh.substr(lastnbbhlength-4,3);
//			var thislsh=nbbh.substr(nbbhlength-4,3);
//			if(lastnbbh==''){
//				if(thislsh<=bjlsh){
//					$.alert("流水号排序错误!");
//				}
//			}else if(thislsh<=lastlsh){
//				$.alert("流水号排序错误!");
//			}else{
//				bjlsh=thislsh;
//			}
//		}
//	}
//}
function filter(){
    for(var i=1;i<97;i++){
        $("#ajaxFormEdit input[xh='"+i+"']").val("");
        $("#ajaxFormEdit #nbzbm-"+i).val("");
        $("#ajaxFormEdit #syglid-"+i).val("");
    }
    initZdpb()
}
function sjphlr(){
	$("#ajaxForm #sjph").val($("#ajaxForm #sj").val());
}
function initZdpb(){
	if(tqmxDtoList&&tqmxDtoList.length>0){
        var bhhz=$("#zdpbin").val()
        var bbhhz=$("#zdpbnotin").val()

        var hzmc=tqmxDtoList[0].kzcs3;

        var index=0;
        var zjindex=0;
        for(var i=0;i<tqmxDtoList.length;i++){
            var bhflg=false;
            var bbhflg=true;
            if(bhhz!=""){
                var bhArr=bhhz.split(",");
                for(var j=0;j<bhArr.length;j++){
                    if(bhArr[j]!=""&&bhArr[j]==tqmxDtoList[i].kzcs3){
                        bhflg=true;
                        break;
                    }
                }
            }else{
                bhflg=true
            }

            if(bbhhz!=""){
                var bbhArr=bbhhz.split(",");
                for(var j=0;j<bbhArr.length;j++){
                    if(bbhArr[j]!=""&&bbhArr[j]==tqmxDtoList[i].kzcs3){
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
            if(hzmc==tqmxDtoList[i].kzcs3){
                if(zjindex>=11){
                   index=parseInt(index+10)
                   zjindex=1;
                }
            }else{
                zjindex=1;
                if($("input[xh='"+parseInt(index+zjindex)+"']").val()!=null&&$("input[xh='"+parseInt(index+zjindex)+"']").val().trim()!=""){
                    index=parseInt(index+10)
                }

                hzmc=tqmxDtoList[i].kzcs3

            }
             $("#ajaxFormEdit input[xh='"+parseInt(index+zjindex)+"']").val(tqmxDtoList[i].nbbh+(tqmxDtoList[i].kzcs3?('-'+tqmxDtoList[i].kzcs3):""));
             $("#ajaxFormEdit #nbzbm-"+parseInt(index+zjindex)).val((tqmxDtoList[i].kzcs2?tqmxDtoList[i].kzcs2:'')+'/'+(tqmxDtoList[i].kzcs3?tqmxDtoList[i].kzcs3:''));
             $("#ajaxFormEdit #syglid-"+parseInt(index+zjindex)).val((tqmxDtoList[i].syglid?tqmxDtoList[i].syglid:''));
        }
    }
}
function changeJcdw(){
	for(var i=1;i<97;i++){
        tqmxDtoList=null;
    }
    var jcdw = $("#jcdw").val();
    $.ajax({
            type:'post',
            url:"/experiment/potency/pagedataGetTqsj",
            cache: false,
            data: { "jcdw":jcdw,
                    "access_token":$("#ac_tk").val()},
            dataType:'json',
            success:function(data){
                   var tqsjList= data.tqsjList
                   var tqsjhtml=''
                   if(tqsjList && tqsjList.length>0){
                       for(var i=0;i<tqsjList.length;i++){
                           var item = tqsjList[i];
                            tqsjhtml+='<div class="row col-md-3 col-sm-3 col-xs-3 text-center "  style="margin-top:10px;">'
                            tqsjhtml+='<label  class="col-md-4 col-sm-4 col-xs-4 " style="padding: 0">'+item.title+'</label>'
                            tqsjhtml+=' <div class="col-sm-8 col-md-8 col-xs-8">'
                            tqsjhtml+=' <input type="text" id="'+item.variable+'" name="'+item.variable+'" value="'+(item.value?item.value:'')+'" class="form-control" style="height:30px;"/>'
                            tqsjhtml+=' </div>'
                            tqsjhtml+=' </div>'
                       }
                   }
                   if(!isInit){
                       $("#ajaxForm #tqsjDiv").empty();
                       $("#ajaxForm #tqsjDiv").append(tqsjhtml);
                   }
            }
    })
    isInit=false
     if($("#ajaxForm #zdpb").val()=='0'||$("#ajaxForm  #zdpb").val()==null||!$("#ajaxForm  #zdpb").val()){
        return
    }
	//获取证件类型代码，并设置

	$.ajax({
        type:'post',
        url:"/experiment/potency/pagedataGetZdpbTqList",
        cache: false,
        data: {"jcdw":jcdw,"access_token":$("#ac_tk").val()},
        dataType:'json',
        success:function(data){
            tqmxDtoList=data.tqglDtoList
             for(var i=1;i<97;i++){
                $("#ajaxFormEdit input[xh='"+i+"']").val("");
                $("#ajaxFormEdit #nbzbm-"+i).val("");
                $("#ajaxFormEdit #syglid-"+i).val("");
            }
            initZdpb();
        }
   })

}
var isInit=true
$(function(){
    initZdpb();
    $("#ajaxForm #zdpbCheck").bootstrapSwitch({
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
                $('#ajaxForm #zdpb').val("1");
                $("#ajaxForm #zdpbin").prop("readOnly", false);
                $("#ajaxForm #zdpbnotin").prop("readOnly", false);
                if(!(tqmxDtoList&&tqmxDtoList.length>0)){
        			$('#ajaxForm #bbjc').val("");
                    changeJcdw()
                }else{
               		initZdpb();
				}
                checkBb();

            } else {
                $('#ajaxForm #zdpb').val("0");
                $("#ajaxForm #zdpbin").prop("readOnly", true);
                $("#ajaxForm #zdpbnotin").prop("readOnly", true);
                for(var i=1;i<97;i++){
                    $("#ajaxFormEdit input[xh='"+i+"']").val("");
                    $("#ajaxFormEdit #nbzbm-"+i).val("");
                    $("#ajaxFormEdit #syglid-"+i).val("");
                }
                checkBb();
            }
        }
    });
    if ( "1"== $('#ajaxForm #zdpb').val()){
        $("#ajaxForm #zdpbCheck").bootstrapSwitch("state", true, true);
        $("#ajaxForm #zdpbin").prop("readOnly", false);
        $("#ajaxForm #zdpbnotin").prop("readOnly", false);
    }else{
        $("#ajaxForm #zdpbin").prop("readOnly", true);
        $("#ajaxForm #zdpbnotin").prop("readOnly", true);
        for(var i=1;i<97;i++){
            $("#ajaxFormEdit input[xh='"+i+"']").val("");
            $("#ajaxFormEdit #nbzbm-"+i).val("");
            $("#ajaxFormEdit #syglid-"+i).val("");
        }
    }
  var prefix=['A','B','C','D'];
	var kwdata="";
	for(var i=0;i<prefix.length;i++){
		for(var j=1;j<61;j++){
			kwdata+=','+prefix[i]+j.toString().padStart(2, '0');
		}
	}
	$("#kwdata").val(kwdata.substring(1));
	var spikedata="";
	for(var j=1;j<49;j++){
		spikedata+=',SP'+j.toString().padStart(2, '0');
	}
	$("#spikedata").val(spikedata.substring(1));
	if($("#format").val()=="addSavePotency"||$("#format").val()=="extractSaveExperiment"){
		var date = new Date(); //获取当前日期时间戳
		var hour = date.getHours();//获取当前时间小时值
		var tqrq= date;
		//如果为8点之前的，那么提取日期自动设置为前一天的，如果超过8点，则设置为当天
		if(hour<8){
			tqrq= date-1000*60*60*24;
		}
		laydate.render({
			elem: '#ajaxForm #tqrq'
		  	,theme: '#2381E9'
		  	,value: new Date(tqrq)
		});
	}else{
		laydate.render({
			elem: '#ajaxForm #tqrq'
		  	,theme: '#2381E9'
		});
	}
	if($("#format").val()=="extractSaveExperiment"&&$("#tqmx_json").val()){
		var list = JSON.parse($("#tqmx_json").val());
		var xh=1;
		if (list && list.length > 0){
			for (let k = 0; k <list.length; k++) {
				if (list[k].nbbh.substring(0,3) == 'NC-' || list[k].nbbh.substring(0,3) == 'PC-'|| list[k].nbbh.substring(0,3) == 'DC-'){
					$("#ajaxFormEdit #nbzbm-"+xh).val((list[k].kzcs2?list[k].kzcs2:'')+'/'+(list[k].kzcs3?list[k].kzcs3:''));
				}else{
					var str = list[k].nbbh;
					if (list[k].kzcs3){
						str+='-'+list[k].kzcs3;
					}
					var flag = true;
					var nbzbm = (list[k].kzcs2?list[k].kzcs2:'')+'/'+(list[k].kzcs3?list[k].kzcs3:'');
					for(var i=1;i<97;i++){
						if ($("#ajaxFormEdit #nbbh-"+i).val() && $("#ajaxFormEdit #nbbh-"+i).val() == str && nbzbm == $("#ajaxFormEdit #nbzbm-"+i).val()){
							flag = false;
							break;
						}
					}
					if (flag){
						$("#ajaxFormEdit input[xh='"+xh+"']").val(str);
						$("#ajaxFormEdit #nbzbm-"+xh).val(nbzbm);
					}else{
						$("#ajaxFormEdit input[xh='"+xh+"']").val(list[k].nbbh);
					}
				}
				xh++;
			}
		}else{
			$("#ajaxFormEdit #nbzbm-"+xh).val("XX/XX");
		}

	}
	
	//打开新增页面光标显示在第一个输入框
	setTimeout(function(){
		$("#mc").focus();
	},200);
	
	if($("#format").val()=="modSavePotency"){
		var tqid=$("#tqid").val();
		$.ajax({
		    async:false,
		    type:'post',  
		    url:"/experiment/potency/pagedataXgxx",
		    cache: false,
		    data: {"tqid":tqid,"access_token":$("#ac_tk").val()},  
		    dataType:'json', 
		    success:function(data){
		    	//返回值
		    	$("#tqrq").val(data[0].tqrq);
		    	$("#tqrq").text(data[0].tqrq);
		    	$("#mc").val(data[0].mc);
		    	$("#mc").text(data[0].mc);
		    	for(var i=0;i<data.length;i++){
					var xh=parseInt(data[i].xh);
		    		$("#ajaxFormEdit #nbbh-"+data[i].xh).val(data[i].nbbh);
		    		$("#ajaxFormEdit #nbzbm-"+data[i].xh).val(data[i].tqdm);
					$("#ajaxFormEdit #syglid-"+data[i].xh).val(data[i].syglid);
		    		$("#ajaxFormEdit #tqmxid-"+data[i].xh).val(data[i].tqmxid);
		    		$("#ajaxFormEdit #hsnd-"+data[i].xh).val(data[i].hsnd);
		    		$("#ajaxFormEdit #cdna-"+data[i].xh).val(data[i].cdna);
					$("#ajaxFormEdit #tqykw-"+data[i].xh).val(data[i].tqykw);
					$("#ajaxFormEdit #spike-"+data[i].xh).val(data[i].spike);
					$("#ajaxFormEdit #xsbs-"+data[i].xh).val(data[i].xsbs);
		    		$("#ajaxFormEdit #nbbh-"+data[i].xh).text(data[i].nbbh);
		    		$("#ajaxFormEdit #hsnd-"+data[i].xh).text(data[i].hsnd);
		    		$("#ajaxFormEdit #cdna-"+data[i].xh).text(data[i].cdna);
					$("#ajaxFormEdit #tqykw-"+data[i].xh).text(data[i].tqykw);
					if(xh<=10){
						$("#ajaxFormEdit #tqybm-1").val(data[i].tqybm);
					}else if(xh>10&&xh<=20){
						$("#ajaxFormEdit #tqybm-2").val(data[i].tqybm);
					}else if(xh>20&&xh<=30){
						$("#ajaxFormEdit #tqybm-3").val(data[i].tqybm);
					}else if(xh>30&&xh<=40){
						$("#ajaxFormEdit #tqybm-4").val(data[i].tqybm);
					}else if(xh>40&&xh<=50){
						$("#ajaxFormEdit #tqybm-5").val(data[i].tqybm);
					}else if(xh>50&&xh<=60){
						$("#ajaxFormEdit #tqybm-6").val(data[i].tqybm);
					}else if(xh>60&&xh<=70){
						$("#ajaxFormEdit #tqybm-7").val(data[i].tqybm);
					}else if(xh>70&&xh<=80){
						$("#ajaxFormEdit #tqybm-8").val(data[i].tqybm);
					}else if(xh>80&&xh<=90){
						$("#ajaxFormEdit #tqybm-9").val(data[i].tqybm);
					}else if(xh>90&&xh<=96){
						$("#ajaxFormEdit #tqybm-10").val(data[i].tqybm);
					}
		    	}
		    }
		});
	}
	jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
})


var xzPoreid;
function addPosition(poreid){
	xzPoreid=poreid.id;
	var text="";
	let minid = (xzPoreid.split("_")[1]*1-1)*10;
	for(let i=1;i<11;i++){
		minid++;
		if ($("#tqykw-"+minid).val()){
			text += ","+$("#tqykw-"+minid).val();
		}
	}
	if (text){
		text = text.substring(1);
	}
	var url="/experiment/potency/pagedataAddPosition?pageSize=6&pageNumber=10&firstlie="+xzPoreid.split("_")[1]+"&text="+text;
	$.showDialog(url,'添加孔位',addPositionConfig);
}

var addPositionConfig = {
	width		: "600px",
	modalName	: "addPositionModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				let minid = (xzPoreid.split("_")[1]*1-1)*10;
				for(let i=1;i<11;i++){
					minid++;
					if ($("#positionAddForm #positionnr").children('div')[i]){
						$("#ajaxFormEdit #tqykw-"+minid).val($("#positionAddForm #positionnr").children('div')[i].id);
					}else{
						$("#ajaxFormEdit #tqykw-"+minid).val('');
					}
				}
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
var spikeid;
function addSpike(e){
	spikeid=e.id;
	var text="";
	let minid = (spikeid.split("_")[1]*1-1)*10;
	for(let i=1;i<11;i++){
		minid++;
		if ($("#spike-"+minid).val()){
			text += ","+$("#spike-"+minid).val();
		}
	}
	if (text){
		text = text.substring(1);
	}
	var url="/experiment/potency/pagedataAddSpike?pageSize=6&pageNumber=8&firstlie="+spikeid.split("_")[1]+"&text="+text;
	$.showDialog(url,'添加Spike',addSpikeConfig);
}
function printTq(){
    window.open("/experiment/potency/exportPotency?access_token="+$("#ac_tk").val()+"&tqid="+$("#tqid").val())
}
var addSpikeConfig = {
	width		: "600px",
	modalName	: "addSpikeModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				let minid = (spikeid.split("_")[1]*1-1)*10;
				for(let i=1;i<11;i++){
					minid++;
					if ($("#addSpikeForm #positionnr").children('div')[i]){
						$("#spike-"+minid).val($("#addSpikeForm #positionnr").children('div')[i].id);
					}else{
						$("#spike-"+minid).val('');
					}
				}
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

function chooseCzr(){
	var url="/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url,'选择操作人',addCzrConfig);
}
var addCzrConfig = {
	width		: "800px",
	modalName	:"addCzrModal",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					$('#ajaxForm #czr').val(sel_row[0].yhid);
					$('#ajaxForm #czrmc').val(sel_row[0].zsxm);
					//保存操作习惯
					$.ajax({
					    type:'post',
					    url:"/common/habit/pagedataModGrsz",
					    cache: false,
					    data: {"szz":sel_row[0].yhid,"glxx":sel_row[0].zsxm,"szlb":"OPERATOR","access_token":$("#ac_tk").val()},
					    dataType:'json',
					    success:function(data){}
					});
	    		}else{
	    			$.error("请选中一行");
	    			return false;
	    		}
			},
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};


function chooseHdr(){
	var url="/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url,'选择核对人',addHdrConfig);
}
var addHdrConfig = {
	width		: "800px",
	modalName	:"addHdrModal",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					$('#ajaxForm #hdr').val(sel_row[0].yhid);
					$('#ajaxForm #hdrmc').val(sel_row[0].zsxm);
					//保存操作习惯
					$.ajax({
					    type:'post',
					    url:"/common/habit/pagedataModGrsz",
					    cache: false,
					    data: {"szz":sel_row[0].yhid,"glxx":sel_row[0].zsxm,"szlb":"COLLATOR","access_token":$("#ac_tk").val()},
					    dataType:'json',
					    success:function(data){}
					});
	    		}else{
	    			$.error("请选中一行");
	    			return false;
	    		}
			},
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
