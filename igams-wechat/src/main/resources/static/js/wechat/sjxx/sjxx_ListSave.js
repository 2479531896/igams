var access_token=null;
var tmp_fkje="";
var yblxselect="";
/**
* 标本状态增加取消操作
 * @returns
 */
function calcel(){
    $("#ztDiv input[type='checkbox']").each(function(index){
    	console.log(this.checked)
        if(this.checked){
            var ztmc=$("#"+this.value).next().text();
            $("#"+this.value).prop("checked",false);
            $("#ajaxForm #bz").val($("#ajaxForm #bz").val().replace("["+ztmc+"]",""));
        }
    })
}
$(document).ready(function(){
    if($("#ADD_ALLOWED").val()=="0"){
        $("select[name=jcdw]").attr("disabled","disabled");
        $("#modSjxxModal #ajaxForm").submit(function(){
           $("select[name=jcdw]").attr("disabled",false);
        })
    }

});
var viewPreModConfig = {
    width        : "900px",
    height        : "800px",
    offAtOnce    : true,  //当数据提交成功，立刻关闭窗口
    buttons        : {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
var vm = new Vue({
	el:'#Sjxx_Save',
	data: {
	},
	methods:{
		view:function(fjid){
			var url= "/common/file/pdfPreview?fjid=" + fjid;
            $.showDialog(url,'文件预览',viewPreModConfig);
            
		},
		view_bg:function(fjid){
			var url= "/common/file/pdfPreview?fjid=" + fjid;
            $.showDialog(url,'文件预览',viewPreModConfig);
            
		},
		xz:function(fjid){
		    jQuery('<form action="/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
	                '<input type="text" name="fjid" value="'+fjid+'"/>' + 
	                '<input type="text" name="access_token" value="'+access_token+'"/>' + 
	            '</form>')
	        .appendTo('body').submit().remove();
		},
		xz_bg:function(fjid){
		    jQuery('<form action="/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
	                '<input type="text" name="fjid" value="'+fjid+'"/>' + 
	                '<input type="text" name="access_token" value="'+access_token+'"/>' + 
	            '</form>')
	        .appendTo('body').submit().remove();
		},
		del:function(fjid,wjlj){
			$.confirm('您确定要删除所选择的记录吗？',function(result){
    			if(result){
    				jQuery.ajaxSetup({async:false});
    				var url= "/common/file/delFile";
    				jQuery.post(url,{fjid:fjid,wjlj:wjlj,"access_token":access_token},function(responseText){
    					setTimeout(function(){
    						if(responseText["status"] == 'success'){
    							$.success(responseText["message"],function() {
    								$("#"+fjid).remove();
    							});
    						}else if(responseText["status"] == "fail"){
    							$.error(responseText["message"],function() {
    							});
    						} else{
    							$.alert(responseText["message"],function() {
    							});
    						}
    					},1);
    				},'json');
    				jQuery.ajaxSetup({async:true});
    			}
    		});
		},
		del_bg:function(fjid,wjlj){
			$.confirm('您确定要删除所选择的记录吗？',function(result){
    			if(result){
    				jQuery.ajaxSetup({async:false});
    				var url= "/inspection/pathogen/pagedataDelFile";
    				jQuery.post(url,{fjid:fjid,wjlj:wjlj,"access_token":access_token},function(responseText){
    					setTimeout(function(){
    						if(responseText["status"] == 'success'){
    							$.success(responseText["message"],function() {
    								$("#"+fjid).remove();
    							});
    						}else if(responseText["status"] == "fail"){
    							$.error(responseText["message"],function() {
    							});
    						} else{
    							$.alert(responseText["message"],function() {
    							});
    						}
    					},1);
    				},'json');
    				jQuery.ajaxSetup({async:true});
    			}
    		});
		}
	}
})

function displayUpInfo(fjid){
	if(!$("#ajaxForm #fjids").val()){
		$("#ajaxForm #fjids").val(fjid);
	}else{
		$("#ajaxForm #fjids").val($("#ajaxForm #fjids").val()+","+fjid);
	}
}

$("#ajaxForm #sfsf").on('change',function(){
	var sfsf=$("#ajaxForm #sfsf option:selected").val();
	if(sfsf==0){  //不收费
		$("#ajaxForm #kpsqSpan").text("");
		$("#ajaxForm .kpsq").each(function(){
			this.checked=false;
		})
		$("#ajaxForm #fkje").val("0");
	}else{ // 收费
		$("#ajaxForm #kpsqSpan").text("*");
		$("#ajaxForm #fkje").val(tmp_fkje);
	}
})
function yl(fjid,wjmhz){
	if(wjmhz.toLowerCase()=="jpg" || wjmhz.toLowerCase()=="jpeg" || wjmhz.toLowerCase()=="jfif" || wjmhz.toLowerCase()=="png"){
		var url="/ws/sjxxpripreview/?fjid="+fjid
		$.showDialog(url,'图片预览',JPGMaterConfig);
	}else if(wjmhz.toLowerCase()=="pdf"){
		var url= "/common/file/pdfPreview?fjid=" + fjid;
        $.showDialog(url,'文件预览',JPGMaterConfig);
	}else {
		$.alert("暂不支持其他文件的预览，敬请期待！");
	}
}
var JPGMaterConfig = {
		width		: "800px",
		height		: "500px",
		offAtOnce	: true,  
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

/**
 * 根据输入的首字母获取合作伙伴
 * @returns
 */
function getsjhbByybbh(){
	var first=$("#ajaxForm #ybbh").val().substring(0,1);
	if(first=="1"){
		$("#ajaxForm #db").val("杭州艾迪康");
	}else if(first=="2"){
		$("#ajaxForm #db").val("南京艾迪康");
	}else if(first=="3"){
		$("#ajaxForm #db").val("福州艾迪康");
	}else if(first=="4"){
		$("#ajaxForm #db").val("济南艾迪康");
	}else if(first=="5"){
		$("#ajaxForm #db").val("合肥艾迪康");
	}else if(first=="6"){
		$("#ajaxForm #db").val("上海艾迪康");
	}else if(first=="7"){
		$("#ajaxForm #db").val("南昌艾迪康");
	}else if(first=="B"){
		$("#ajaxForm #db").val("北京艾迪康");
	}else if(first=="F"){
		$("#ajaxForm #db").val("长春艾迪康");
	}else if(first=="H"){
		$("#ajaxForm #db").val("沈阳艾迪康");
	}else if(first=="K"){
		$("#ajaxForm #db").val("成都艾迪康");
	}else if(first=="N"){
		$("#ajaxForm #db").val("郑州艾迪康");
	}else if(first=="W"){
		$("#ajaxForm #db").val("武汉艾迪康");
	}else if(first=="X"){
		$("#ajaxForm #db").val("长沙艾迪康");
	}else if(first=="R"){
		$("#ajaxForm #db").val("广州艾迪康");
	}else if(first=="Q"){
		$("#ajaxForm #db").val("昆明艾迪康");
	}else if(first=="D"){
		$("#ajaxForm #db").val("天津艾迪康");
	}else if(first=="A"){
		$("#ajaxForm #db").val("西安艾迪康");
	}else if(first=="J"){
		$("#ajaxForm #db").val("三明艾迪康");
	}else if(first=="V"){
		$("#ajaxForm #db").val("重庆艾迪康");
	}
}

/**
 * 判断是否调用金域数据
 */
function getGoldenData(){
	if (!isNaN(Number($("#ajaxForm #ybbh").val()))){
		var number = $("#ajaxForm #ybbh").val().substring(0,2);
		// 05-杭州；06-南京；14-福州
		if(number == "05" || number == "06" || number == "14"){
			$.ajax({
		        type: "POST",
		        dataType: "json",
		        url: "/inspection/inspection/pagedataGoldenData ",
		        data: {
		        	"ybbh": $("#ajaxForm #ybbh").val(),
		        	"access_token":access_token,
		        },
		        success: function (result) {
		            if(result.status == 'success'){
		            	if(result.sjxxDto){
		            		$("#ajaxForm #ybbh").val(result.sjxxDto.ybbh);
		            		$("#ajaxForm #wbbm").val(result.sjxxDto.wbbm);
		            		$("#ajaxForm #hzxm").val(result.sjxxDto.hzxm);
		            		$("#ajaxForm input:radio[value='"+result.sjxxDto.xb+"']").attr('checked','true');
		            		$("#ajaxForm #nl").val(result.sjxxDto.nl);
		            		$("#ajaxForm #sjdw").val(result.sjxxDto.sjdw);
		            		$("#ajaxForm #sjys").val(result.sjxxDto.sjys);
		            		$("#ajaxForm #db").val(result.sjxxDto.db);
		            		$("#ajaxForm #cwh").val(result.sjxxDto.cwh);
		            		$("#ajaxForm #kdh").val(result.sjxxDto.kdh);
		            		$("#ajaxForm #kdhspan").hide();
		            		$("#ajaxForm #ybtj").val(result.sjxxDto.ybtj);
		            		$("#ajaxForm #cyrq").val(result.sjxxDto.cyrq);
		            		$("#ajaxForm #qqzd").val(result.sjxxDto.qqzd);
		            		$("#ajaxForm #lczz").text(result.sjxxDto.lczz);
		            		$("#ajaxForm #nldw").val(result.sjxxDto.nldw);
		    				$("#ajaxForm #nldw").trigger("chosen:updated");
		    				$("#ajaxForm #ks").val(result.sjxxDto.ks);
		    				$("#ajaxForm #ks").trigger("chosen:updated");
		    				if(result.sjxxDto.qtks){
		    					$("#ajaxForm #qtkscheck").show();
	    						$("#ajaxForm #qtks").removeAttr("disabled");
	    						$("#ajaxForm #qtks").val(result.sjxxDto.qtks);
		    				}else{
		    					$("#ajaxForm #qtkscheck").hide();
	    						$("#ajaxForm #qtks").attr("disabled","disabled");
	    						$("#ajaxForm #qtks").val("");
		    				}
		    				$("#ajaxForm #kdlx").val(result.sjxxDto.kdlx);
		    				$("#ajaxForm #kdlx").trigger("chosen:updated");
		    				$("#ajaxForm #jcdw").val(result.sjxxDto.jcdw);
		    				$("#ajaxForm #jcdw").trigger("chosen:updated");
		    				$("#ajaxForm #yblx").val(result.sjxxDto.yblx);
		    				$("#ajaxForm #yblx").trigger("chosen:updated");
		    				jcxminit();
		    				if(result.sjxxDto.yblxmc){
		    					$("#ajaxForm #yblxmcspan").show();
		    					$("#ajaxForm #yblxmc").removeAttr("disabled");
		    					$("#ajaxForm #yblxmc").val(result.sjxxDto.yblxmc);
		    				}else{
		    					$("#ajaxForm #yblxmcspan").hide();
		    					$("#ajaxForm #yblxmc").attr("disabled","disabled");
		    					$("#ajaxForm #yblxmc").val("");
		    				}
		    				$("#ajaxForm input:radio[value='"+result.sjxxDto.jcxmid+"']").attr('checked','true');
		            	}
					}else if(result.status == "fail"){
						$.error(result.message,function() {
						});
					} else{
						$.alert(result.message,function() {
						});
					}
		        },
		        error : function(XMLHttpRequest, textStatus, errorThrown) {
                    $.error("异常！ 错误信息为： "+XMLHttpRequest.responseText );
		        }
		    });
		}
	}
}

var ybbhTimer = null;

/**
 * 绑定按钮事件
 */
function btnBind(){
	/**
	 *送检单位预输入 
	 */
	$('#ajaxForm #hospitalname').typeahead({
		source : function(query, process) {
			$('#ajaxForm #sjdw').val(null);
			return $.ajax({
				url : '/wechat/hospital/pagedataHospitalName',
				type : 'post',
				data : {
					"searchParam" : query,
					"access_token" : access_token
				},
				dataType : 'json',
				success : function(result) {
					var resultList = result.yyxxDtos
							.map(function(item) {
								var aItem = {
									id : item.dwid,
									name : item.dwmc,
									dwjc: item.dwjc,
									dwqtmc:item.dwqtmc,
									cskz1: item.cskz1
								};
								return JSON.stringify(aItem);
							});
					return process(resultList);
				}
			});
		},
		matcher : function(obj) {
			var item = JSON.parse(obj);
			if(item.name.toLowerCase().indexOf(this.query.toLowerCase())!=-1){
				return ~item.name.toLowerCase().indexOf(this.query.toLowerCase())
			}else if(item.dwjc.toLowerCase().indexOf(this.query.toLowerCase())!=-1){
				return ~item.dwjc.toLowerCase().indexOf(this.query.toLowerCase())
			}else if(item.dwqtmc.toLowerCase().indexOf(this.query.toLowerCase())!=-1){
				return ~item.dwqtmc.toLowerCase().indexOf(this.query.toLowerCase())
			}
		},
		sorter : function(items) {
			var beginswith = [], caseSensitive = [], caseInsensitive = [], item;
			while (aItem = items.shift()) {
				var item = JSON.parse(aItem);
				if (!item.name.toLowerCase().indexOf(
						this.query.toLowerCase()))
					beginswith.push(JSON.stringify(item));
				else if (~item.name.indexOf(this.query))
					caseSensitive.push(JSON.stringify(item));
				else
					caseInsensitive.push(JSON.stringify(item));
			}
			return beginswith.concat(caseSensitive,
					caseInsensitive)
		},
		highlighter : function(obj) {
			var item = JSON.parse(obj);
			var query = this.query.replace(
					/[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&')
			return item.name.replace(new RegExp('(' + query
					+ ')', 'ig'), function($1, match) {
				return '<strong>' + match + '</strong>'
			})
		},
		updater : function(obj) {
			var item = JSON.parse(obj);
			$('#ajaxForm #sjdw').attr('value', item.id);
			if(item.cskz1=='1'){
				$("#ajaxForm #sjdwmc").val(null);	
				$("#ajaxForm #otherHospital").show();	
			}else{
				$("#ajaxForm #sjdwmc").val(item.name);	
				$("#ajaxForm #otherHospital").hide();	
			}
			return item.name;
		}
	});
	
	$("#ajaxForm #ybbh").on('keydown',function(e){
		if(e.keyCode==123 || e.keyCode==74|| e.keyCode==13){
			var ybbh = $("#ajaxForm #ybbh").val();
			if( ybbh.length >= 8){
				var i=ybbh.indexOf("ybbh=");
				if(i!="-1"){
					var newybbh=ybbh.substring(i+5,ybbh.length);
					$("#ajaxForm #ybbh").val(newybbh);
				}else{
					var j=ybbh.indexOf("YBBH=");
					if(j!="-1"){
						var newybbh = ybbh.substring(j+5,ybbh.length);
						$("#ajaxForm #ybbh").val(newybbh);
					}
				}
				if($("#ajaxForm #ybbh").val().length == 8){
					var newybbh = $("#ajaxForm #ybbh").val().toUpperCase();
					$("#ajaxForm #ybbh").val(newybbh);
				}
			}
			if(e.keyCode==74 && $("#ajaxForm #ybbh").val().length >=8){
				return false;
			}
		}else if(e.keyCode==13 && $("#ajaxForm #ybbh").val().length ==12){
			/*getsjhbByybbh();*/
		}else if(e.keyCode==13 && $("#ajaxForm #ybbh").val().length == 10){
			getGoldenData();
		}else if(e.keyCode == 229 && ybbhTimer == null){
			ybbhTimer = setTimeout(function(){
				var ybbh = $("#ajaxForm #ybbh").val();
				if( ybbh.length >= 8){
					var i=ybbh.indexOf("ybbh=");
					if(i!="-1"){
						var newybbh=ybbh.substring(i+5,ybbh.length);
						$("#ajaxForm #ybbh").val(newybbh);
					}else{
						var j=ybbh.indexOf("YBBH=");
						if(j!="-1"){
							var newybbh = ybbh.substring(j+5,ybbh.length);
							$("#ajaxForm #ybbh").val(newybbh);
						}
					}
					if($("#ajaxForm #ybbh").val().length == 8){
						var newybbh = $("#ajaxForm #ybbh").val().toUpperCase();
						$("#ajaxForm #ybbh").val(newybbh);
					}
				}
				ybbhTimer = null;
			}, 800)
		}
	});
	
	$("#ajaxForm #ybbh").on('blur',(function(e){
		if($("#ajaxForm #ybbh").val().length ==12){
			/*getsjhbByybbh();*/
		}
		if($("#ajaxForm #ybbh").val().length == 10){
			getGoldenData();
		}
	}))
	$("#ajaxForm #hospSampleID").on('blur', function() {
		dataGet();
	});
	$("#ajaxForm #aidikangID").on('input', function() {
		let aidikangID = $("#ajaxForm #aidikangID").val()
		while (aidikangID && aidikangID.indexOf(" ") != -1 && aidikangID.indexOf("　") != -1) {
			aidikangID = aidikangID.replace(" ", "").replace("　", "");
		}
		$("#ajaxForm #aidikangID").val(aidikangID);
		$("#ajaxForm #wbbm").val(aidikangID);
	});
	document.onkeydown=function(e){
		if(e.keyCode==123)
			return false;
	};
	
	//因前端readonly付款切换时暂时样式清除不掉无法提交，暂使用disabled
	$("#ajaxForm #fkbj").on('change',function(){
		var fkbj=$("#ajaxForm #fkbj").val();
		if(fkbj=="1"){
			$("#ajaxForm #sfjejc").show();
			$("#ajaxForm #fkrqjc").show();
			$("#ajaxForm #fkrq").removeAttr("disabled");
			$("#ajaxForm #sfje").removeAttr("disabled");
		}else{
			$("#ajaxForm #sfjejc").hide();
			$("#ajaxForm #fkrqjc").hide();
			$("#ajaxForm #sfje").val("");
			$("#ajaxForm #fkrq").val("");
			$("#ajaxForm #fkrq").attr("disabled","disabled");
			$("#ajaxForm #sfje").attr("disabled","disabled");
		}
	});
	
	var cskz2=$("#ajaxForm #kdlx").find("option:selected").attr("cskz2");
	var kdlxdm = $("#ajaxForm #kdlx").find("option:selected").attr("csdm");
	$("#ajaxForm #kdh").removeAttr("placeholder");
	if(cskz2){
		$("#ajaxForm #kdh").attr("placeholder"," 如无请填无");
		$("#ajaxForm #kdhname").text("快递单号：")
		$("#ajaxForm #kdhspan").hide();
	}else{
		if (kdlxdm == "QYY"){
			$("#ajaxForm #kdh").attr("placeholder"," 请填写取样员姓名");
			$("#ajaxForm #kdhname").text("取样员：")
			$("#ajaxForm #kdhspan").show();
		}else {
			$("#ajaxForm #kdhname").text("快递单号：")
			$("#ajaxForm #kdhspan").show();
		}
	}
}
//验证付款金额
$("#ajaxForm #fkje").blur(function(){
	var fkje=$("#ajaxForm #fkje").val();
		var reg=/((^[1-9]\d*)|^0)(\.\d{0,2}){0,1}$/;
		if(fkje!=""&&!reg.test(fkje)){
			$("#ajaxForm #checkfkje").show();
		}else{
			tmp_fkje=$("#ajaxForm #fkje").val();
			$("#ajaxForm #checkfkje").hide();
		}
})

//验证实付金额
$("#ajaxForm #sfje").blur(function(){
	var sfje=$("#ajaxForm #sfje").val();
	var reg=/((^[1-9]\d*)|^0)(\.\d{0,2}){0,1}$/;
	if(sfje!=""&&!reg.test(sfje)){
		$("#ajaxForm #cheqfbjfje").show();
	}else{
		$("#ajaxForm #cheqfbjfje").hide();
	}
})

$("#ajaxForm .jcxm").click(function(e){
	checkJcxm();
	// 查询检测子项目
	getSubDetect(e.currentTarget.id, 1);
	initTab();
})


//限制标本类型
function limitYblx(){
	var yblxList=$("#ajaxForm .t_yblx");
	var jcxmList=$("input[name='jcxmids']:checked");
	if (null != yblxList && yblxList.length >0 ){
		if (null != jcxmList && jcxmList.length >0){
			for (let i = 0; i < yblxList.length; i++) {
				if(yblxList[i].id!=null && yblxList[i].id!=""){
					var jcxmdm=$("#ajaxForm #"+yblxList[i].id).attr("cskz2");
					if(jcxmdm!=null && jcxmdm!=""){
						var show = false;
						for (let j = 0; j < jcxmList.length; j++) {
							if(jcxmList[j].id!=null || jcxmList[j].id !=""){
								if(jcxmdm.indexOf($("#ajaxForm #"+jcxmList[j].id).attr("csdm"))>=0){
									show = true;
									break
								}
							}
						}
						if (show){
							$("#ajaxForm #"+yblxList[i].id).attr("style","display:block;");
						}else {
							$("#ajaxForm #"+yblxList[i].id).attr("style","display:none;");
						}
					}else{
						$("#ajaxForm #"+yblxList[i].id).attr("style","display:none;");
					}
				}
			}
		}else{
			for (let i = 0; i < yblxList.length; i++) {
				if(yblxList[i].id!=null && yblxList[i].id!="") {
					var jcxmdm=$("#ajaxForm #"+yblxList[i].id).attr("cskz2");
					if(jcxmdm!=null && jcxmdm!=""){
						$("#ajaxForm #"+yblxList[i].id).attr("style","display:block;");
					}else{
						$("#ajaxForm #"+yblxList[i].id).attr("style","display:none;");
					}
				}
			}
		}
	}
	$('#ajaxForm #yblx').trigger("chosen:updated"); //更新下拉框
}
//限制科研项目
function limitKyxm(){
	var sjqf=$('input[name="sjqf"]:checked').val();
	if(sjqf){
		var kyxmlength = $("#ajaxForm .t_kyxm").length;
		if(kyxmlength>0){
			var kyxmNum = 0;
			var kyxmShowNum = 0;
			for(var i=0;i<kyxmlength;i++){
				var kyxmid = $("#ajaxForm .t_kyxm")[i].id;
				if(!kyxmid){
					continue
				}
				kyxmNum ++;
				var kyxmfcsid = $("#ajaxForm #"+kyxmid).attr("fcsid");
				if(kyxmid!=null && kyxmfcsid!=""){
					if(sjqf==kyxmfcsid){
						$("#ajaxForm #"+kyxmid).attr("style","display:block;");
						kyxmShowNum ++;
					}else{
						$("#ajaxForm #"+kyxmid).attr("style","display:none;");
					}
				}
			}
			if(kyxmShowNum == 0){
				$("#ajaxForm #kyDIV").attr("style","display:none;");
				$("#ajaxForm #kyxm").removeAttr("validate");
			}else {
				$("#ajaxForm #kyDIV").attr("style","display:block;");
				$("#ajaxForm #kyxm").attr("validate","{required:true}");
			}
		}
		$('#ajaxForm #kyxm').trigger("chosen:updated"); //更新下拉框
	}else {
		$("#ajaxForm #kyDIV").attr("style","display:none;");
		$("#ajaxForm #kyxm").removeAttr("validate");
	}
}

function checkJcxm(){
	limitYblx();
	jcxminit();
	$("input[name='jcxmids']:checked").each(function(){
		var cskz1=$(this).attr("cskz1");
		if(cskz1=="C"){
			$("#ajaxForm #sfsf").children().eq(2).removeAttr("disabled");
			$("#ajaxForm #sfsf").children().eq(3).removeAttr("disabled");
			$("#ajaxForm #sfsf").trigger("chosen:updated");
		}
	})
}

$("#ajaxForm #kdlx").change(function(){
	var cskz2=$("#ajaxForm #kdlx").find("option:selected").attr("cskz2");
	var kdlxdm = $("#ajaxForm #kdlx").find("option:selected").attr("csdm");
	$("#ajaxForm #kdh").removeAttr("placeholder");
	if(cskz2){
		$("#ajaxForm #kdh").val("");
		$("#ajaxForm #kdh").attr("placeholder"," 如无请填无");
		$("#ajaxForm #kdhname").text("快递单号：")
		$("#ajaxForm #kdhspan").hide();
	}else{
		$("#ajaxForm #kdhspan").show();
		if (kdlxdm == "QYY"){
			$("#ajaxForm #kdh").val("");
			$("#ajaxForm #kdh").attr("placeholder"," 请填写取样员姓名");
			$("#ajaxForm #kdhname").text("取样员：")
		}else {
			$("#ajaxForm #kdhname").text("快递单号：")
		}
	}
})
//修改时进行判断，是否显示付款
$(function(){
	var fkbj=$("#ajaxForm #fkbj").val();
	if(fkbj=="1"){
		$("#ajaxForm #sfjejc").show();
		$("#ajaxForm #fkrqjc").show();
		$("#ajaxForm #sfje").removeAttr("disabled");
		$("#ajaxForm #fkrq").removeAttr("disabled");
	}else {
		$("#ajaxForm #sfjejc").hide();
		$("#ajaxForm #fkrqjc").hide();
		$("#ajaxForm #fkrq").attr("disabled","disabled");
		$("#ajaxForm #sfje").attr("disabled","disabled");
	}
	var sfsf=$("#sfsf").val();

	if(sfsf=="0"){
		$("#ajaxForm #fkjejc").hide();
//		$("#ajaxForm #fkje").attr("disabled","disabled");
		$("#ajaxForm #kpsqSpan").text("");
	}else {
		$("#ajaxForm #fkjejc").show();
//		$("#ajaxForm #fkje").removeAttr("disabled");
		$("#ajaxForm #kpsqSpan").text("*");
	}
})
//判断是否显示yblxmc科室
	$("#ajaxForm #ks").change(function(){
		var kzcs=$("#ajaxForm #ks option:selected");
		if (kzcs.attr("kzcs")=="1") {
			$("#ajaxForm #qtkscheck").show();
			$("#ajaxForm #qtks").removeAttr("disabled");
			$("#ajaxForm #qtks").val(kzcs.text());
		}else{
			$("#ajaxForm #qtkscheck").hide();
			$("#ajaxForm #qtks").attr("disabled","disabled");
			$("#ajaxForm #qtks").val("");
		}
	})
$("#ajaxForm #yblx").change(function(){
	checkYblxmc(false)
	//限制检测项目
	jcxminit();
	//限制前期检测项目
	qqjcinit();
})
function jc(){
	var kzcs=$("#ajaxForm #ks option:selected");
	if (kzcs.attr("kzcs")=="1") {
		$("#ajaxForm #qtkscheck").show();
		$("#ajaxForm #qtks").removeAttr("disabled");
	}else{
		$("#ajaxForm #qtkscheck").hide();
		$("#ajaxForm #qtks").attr("disabled","disabled");
		$("#ajaxForm #qtks").val("");
	}
}

function checkYblxmc(isInit){
	var oldyblxcsdm = yblxselect.attr("csdm");
	yblxselect=$("#ajaxForm #yblx option:selected");
	var newyblxcsdm=yblxselect.attr("csdm");
	$("#ajaxForm #yblxmcspan").show();
	$("#ajaxForm #yblxmc").removeAttr("disabled");
	if (oldyblxcsdm != 'XXX' && newyblxcsdm != 'XXX' && !isInit){
		$("#ajaxForm #yblxmc").val(yblxselect.attr("csmc"));
	}

}

function updateBz(obj){

	 var chk_value =[];//定义一个数组  
     $('input[name="zts"]:checked').each(function(){//遍历每一个名字为zts的复选框，其中选中的执行函数  
     chk_value.push($(this).val());//将选中的值添加到数组chk_value中  
     });
     var ybzts="";
     for(var i=0;i<chk_value.length;i++){
	 	var ybzt=$("#"+chk_value[i]).next().text();	 	
	 	ybzts = ybzts+"["+ybzt+"]";
    	}
     	var bz=$("#ajaxForm #bz").val();
		var head = bz.indexOf('[');
		var end = bz.lastIndexOf(']');
		if(head!="-1"&&end!="-1"){
			var text=bz.replace(bz.substring(head,end+1),"");
			$("#ajaxForm #bz").val(text+ybzts)
		}else{
			$("#ajaxForm #bz").val(bz+ybzts);
		}
}


$("#ajaxForm #nbbm").on('keydown',function(e){
	if(e.keyCode==123 || e.keyCode==74|| e.keyCode==13){
		var nbbm = $("#ajaxForm #nbbm").val();
		if( nbbm.length >= 9){
			var newnbbm = $("#ajaxForm #nbbm").val().toUpperCase();
			$("#ajaxForm #nbbm").val(newnbbm);
		}
		if(e.keyCode==74 && $("#ajaxForm #nbbm").val().length >=9){
			return false;
		}
	}
});

document.onkeydown=function(e){
	if(e.keyCode==123)
		return false;
};
function removeChecked(id){
	document.getElementById(id).checked=false;
	if ($("#ajaxForm .jczxm").length > 0){
		for (let j = $("#ajaxForm .jczxm").length -1; j >= 0 ; j--) {
			var jcxmid = $("#ajaxForm #"+$("#ajaxForm .jczxm")[j].id).attr("jcxmid");
			if (jcxmid == id){
				$("#ajaxForm #zxm_"+$("#ajaxForm .jczxm")[j].id).remove()
			}
		}
		jczxminit()
	}
}


function jcxminit(){
	var kzcs=$("#ajaxForm #yblx option:selected");
	//限制检测项目
	var csdm=kzcs.attr("cskz2");
	var jcxmids="";
	$("input[name='jcxmids']:checked").each(function(){
		jcxmids+=","+$(this).attr("id");
	})
	var oncoFlag=false;
	var tNGSFlag=false;
	if($("#ajaxForm .jcxm").length>0){
		for(var i=0;i<$("#ajaxForm .jcxm").length;i++){
			//如果什么都没选
			var id=$("#ajaxForm .jcxm")[i].id;
			var isFind=false;
			$("input[name='jcxmids']:checked").each(function(){
				var cskz3=$(this).attr("cskz3");
				var cskz4=$(this).attr("cskz4");
				if(cskz3==$("#ajaxForm .jcxm").eq(i).attr("cskz3")&&cskz4==$("#ajaxForm .jcxm").eq(i).attr("cskz4")){
					isFind=true;
				}
                if(cskz3=='IMP_REPORT_ONCO_QINDEX_TEMEPLATE'&&cskz4=='Q'){
                    oncoFlag=true;
				}
				if(cskz3=='IMP_REPORT_TNGS_TEMEPLATE'&&cskz4=='T'){
					tNGSFlag=true;
				}else if(cskz3.indexOf('IMP_REPORT_SEQ_TNGS')>=0&&cskz4=='K'){
					tNGSFlag=true;
				}
			})
			if(oncoFlag){
				if($("#ajaxForm .jcxm").eq(i).attr("cskz3")=='IMP_REPORT_ONCO_QINDEX_TEMEPLATE'&&$("#ajaxForm .jcxm").eq(i).attr("cskz4")=='Q'){
					isFind=true;
				}
			}
			if(tNGSFlag){
				var t_cskz3 = $("#ajaxForm .jcxm").eq(i).attr("cskz3");
				if(t_cskz3){
					if(t_cskz3=='IMP_REPORT_TNGS_TEMEPLATE'&&$("#ajaxForm .jcxm").eq(i).attr("cskz4")=='T'){
						isFind=true;
					}else if(t_cskz3.indexOf('IMP_REPORT_SEQ_TNGS')>=0&&$("#ajaxForm .jcxm").eq(i).attr("cskz4")=='K'){
						isFind=true;
					}
				}
			}
			if(kzcs[0].id==null || kzcs[0].id==""){
				if(isFind){
					if(jcxmids.indexOf(id)==-1){
						$("#ajaxForm #t-"+id).attr("style","visibility:hidden");
						removeChecked(id)
					}else{
						$("#ajaxForm #t-"+id).attr("style","display:block;padding-left:0px;");
						$("#ajaxForm #t-"+id).removeClass("noClick");
					}
				}else{
					$("#ajaxForm #t-"+id).attr("style","display:block;padding-left:0px;");
                    $("#ajaxForm #t-"+id).removeClass("noClick");
				}
			}else{
				if(csdm!=null && csdm!=""){
					if(csdm.indexOf($("#ajaxForm #"+id).attr("csdm"))>=0){
						if(isFind){
							if(jcxmids.indexOf(id)==-1){
								$("#ajaxForm #t-"+id).attr("style","visibility:hidden");
								removeChecked(id)
							}else{
								$("#ajaxForm #t-"+id).attr("style","display:block;padding-left:0px;");
								$("#ajaxForm #t-"+id).removeClass("noClick");
							}
						}else{
							$("#ajaxForm #t-"+id).attr("style","display:block;padding-left:0px;");
						    $("#ajaxForm #t-"+id).removeClass("noClick");
						}
					}else{
					    if(jcxmids.indexOf(id)==-1){
                            $("#ajaxForm #t-"+id).attr("style","visibility:hidden");
                            removeChecked(id)
                        }
					}
				}else{
					$("#ajaxForm #t-"+id).attr("style","visibility:hidden;");
					removeChecked(id)
				}
			}
		}
	}
}
//限制前期检测
function qqjcinit(){
	var kzcs=$("#ajaxForm #yblx option:selected");
	//限制前期检测
	var csmc=kzcs.attr("csmc");
	var csdm=kzcs.attr("csdm");
	var length = $("#ajaxForm .qqjc").length;
	//若标本类型没有选择则全部显示；若选择了，则根据cskz2显示
	if (typeof(csmc) != "undefined"){
		if($("#ajaxForm .qqjc").length>0){
			for(var i=0;i<$("#ajaxForm .qqjc").length;i++){
				var id=$("#ajaxForm .qqjc")[i].id;
				var tid = $("#ajaxForm #t-"+id);
				var cskz2= $("#ajaxForm #t-"+id).attr("cskz2");
				if(cskz2==undefined || cskz2==null){
					cskz2="";
				}
				var cskz2s = cskz2.split(",");
				if (cskz2s.includes(csdm)){
					$("#ajaxForm #t-"+id).removeClass("visibilityHidden");
					$("#ajaxForm input[name='sjqqjcs["+i+"].yjxm']").val(id);
				}else {
					$("#ajaxForm #"+id).val("");
					$("#ajaxForm #t-"+id).addClass("visibilityHidden");
				}
			}
		}
	}

}

function selectHospital(){
	url="/wechat/hospital/pagedataCheckUnitView?access_token=" + access_token+"&dwFlag=1";
	$.showDialog(url,'医院名称',SelectHospitalConfig);
};

//医院列表弹出框
var SelectHospitalConfig = {
		width		: "1000px",
		modalName	:"SelectHospitalConfig",
		offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					var sel_row=$('#hospital_formSearch #hospital_list').bootstrapTable('getSelections');
					if(sel_row.length==1){
						var dwid=sel_row[0].dwid;
						var dwmc=sel_row[0].dwmc;
						var cskz1=sel_row[0].cskz1;
						$("#ajaxForm #sjdw").val(dwid);
						$("#ajaxForm #hospitalname").val(dwmc);
						if(cskz1=='1'){
							$("#ajaxForm #sjdwmc").val(null);
							$("#ajaxForm #otherHospital").show();
						}else{
							$("#ajaxForm #sjdwmc").val(dwmc);
							$("#ajaxForm #otherHospital").hide();
							
						}
					}else{
						$.error("请选中一行!");
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
/**
 * 外部接口提交表单方法
 */
function outsubmit(){
	$.ajax({
        //几个参数需要注意一下
        type: "POST",//方法类型
        dataType: "json",//预期服务器返回的数据类型
        url: "/inspection/inspection/modSaveSjxx?access_token="+access_token ,//url
        data: $('#ajaxForm').serialize(),
        success: function (result) {
            if(result.status == 'success'){
				$.success(result.message,function() {
					var userAgent = navigator.userAgent;
				    if (userAgent.indexOf("Firefox") != -1 || userAgent.indexOf("Chrome") != -1) {
				        location.href = "about:blank";
				    } else {
				        window.opener = null;
				        window.open('', '_self');
				    }
					if(window.parent != this.window){ //判断是否存在父窗口
						window.close();
					}
				});
			}else if(result.status == "fail"){
				$.error(result.message,function() {
				});
			} else{
				$.alert(result.message,function() {
				});
			}
        },
        error : function() {
        	$.alert("异常！");
        }
    });
}

/**
 * 根据检测项目获取检测子项目
 * @returns
 */
function getSubDetect(jcxmid, gxbj){
	$.ajax({
		url: '/inspection/inspection/pagedataSubDetect',
		type: 'post',
		async: false,
		data: {
			"jcxmid": jcxmid,
			"access_token": access_token,
		},
		dataType: 'json',
		success: function(result) {
			var data = result.subdetectlist;
			// $("#ajaxForm input[name='jczxm']").attr("checked",false);
			if ($("#ajaxForm #"+jcxmid).is(':checked')){
				var jczxmHtml = "";
				if(data != null && data.length > 0){
					$("#ajaxForm #"+jcxmid).attr("flag","1");
					$.each(data,function(i){
						jczxmHtml += "<div class='col-sm-4' id = 'zxm_"+data[i].csid+"' value='" + data[i].csid + "' style='padding-left:0px;' >" +
							"<label class='checkboxLabel checkbox-inline' style='padding-left:0px;'>" +
							"<input class ='jczxm' id='"+data[i].csid+"' name='"+data[i].csid+"' type='checkbox' value='" + data[i].csid + "' jcxmid='" + data[i].fcsid + "' fcskz1='" + data[i].fcskz1 + "' csmc='" + data[i].csmc + "' onclick='initTab()'/>" +
							"<div class='color_light"+data[i].fcskz2+"'  style='border-radius: 5px' > <span class='color_"+data[i].fcskz2+"'  style='padding: 10px;'>"+data[i].csmc+"</span></div> " +
							"</label></div>";
					});
					$("#ajaxForm #jczxm_div").show();
					$("#ajaxForm #jczxmDiv").append(jczxmHtml);
				}
			}else {
				if(data != null && data.length > 0){
					$.each(data,function(i){
						if ($("#ajaxForm #zxm_"+data[i].csid)){
							$("#ajaxForm #zxm_"+data[i].csid).remove()
						}
					});
					jczxminit()
				}
			}
		}
	});
}

// 检测子项目初始化
function jczxminit(){
	if ($("#ajaxForm .jczxm").length == 0){
		$("#ajaxForm #jczxm_div").hide();
	}
}

function syDisplayUpInfo(fjid){
	if(!$("#ajaxForm #ids").val()){
		$("#ajaxForm #ids").val(fjid);
	}else{
		$("#ajaxForm #ids").val($("#ajaxForm #ids").val()+","+fjid);
	}
}
var t_map=[];
var tb_TableInit=function(){
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#ajaxForm #tb_list").bootstrapTable({
			url: '/inspection/inspection/pagedataInspectionItems',         //请求后台的URL（*）
			method: 'get',                      //请求方式（*）
			toolbar: '#ajaxForm #toolbar',                //工具按钮用哪个容器
			striped: true,                      //是否显示行间隔色
			cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   //是否显示分页（*）
			paginationShowPageGo: false,         //增加跳转页码的显示
			sortable: true,                     //是否启用排序
			sortName: "xm.xh",				//排序字段
			sortOrder: "asc",                   //排序方式
			queryParams: oTableInit.queryParams,//传递参数（*）
			sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
			pageNumber:1,                       //初始化加载第一页，默认第一页
			pageSize: 15,                       //每页的记录行数（*）
			pageList: [15, 30, 50, 100],        //可供选择的每页的行数（*）
			paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
			paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
			search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
			strictSearch: true,
			showColumns: false,                  //是否显示所有的列
			showRefresh: false,                  //是否显示刷新按钮
			minimumCountColumns: 2,             //最少允许的列数
			clickToSelect: true,                //是否启用点击选中行
			//height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId: "xmglid",                     //每一行的唯一标识，一般为主键列
			showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
			cardView: false,                    //是否显示详细视图
			detailView: false,                   //是否显示父子表
			paginationDetailHAlign:' hidden',
			columns: [{
				field: 'csmc',
				title: '项目',
				width: '15%',
				align: 'left',
				visible: true,
			},{
				field: 'zmc',
				title: '子项目',
				width: '15%',
				align: 'left',
				visible: true,
			},{
				field: 'sfsf',
				title: '是否收费',
				width: '12%',
				align: 'left',
				visible: true,
				formatter:sfsf_format,
			},{
				field: 'yfje',
				title: '应付金额',
				width: '19%',
				align: 'left',
				visible: true,
				formatter:yfjeformat,
			},{
				field: 'sfje',
				title: '实付金额',
				width: '19%',
				align: 'left',
				visible: true,
				formatter:sfjeformat,
			},{
				field: 'fkrq',
				title: '付款日期',
				width: '20%',
				align: 'left',
				visible: true,
				formatter:fkrqformat,
			}],
			onLoadSuccess:function(map){
				t_map=map;
			},
			onLoadError:function(){
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
			},
		});
		$("#ajaxForm #tb_list").colResizable({
			liveDrag:true,
			gripInnerHtml:"<div class='grip'></div>",
			draggingClass:"dragging",
			resizeMode:'fit',
			postbackSafe:true,
			partialRefresh:true}
		);
	};
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			pageSize: params.limit,   // 页面大小
			pageNumber: (params.offset / params.limit) + 1,  // 页码
			access_token:$("#ac_tk").val(),
			sortName: params.sort,      // 排序列名
			sortOrder: params.order, // 排位命令（desc，asc）
			sortLastName: "xm.xmglid", // 防止同名排位用
			sortLastOrder: "asc", // 防止同名排位用
			sjid: $("#ajaxForm #sjid").val()
		};
		return map;
	};
	return oTableInit;
}

/**
 * 是否收费
 * @param value
 * @param row
 * @param index
 * @returns
 */
function sfsf_format(value,row,index){
	var sfsf="";
	if(row.sfsf){
		sfsf=row.sfsf;
	}
	var cskz1="";
	if(row.cskz1){
		cskz1=row.cskz1;
	}
	var html='';
	html+='<div class="col-sm-12 col-xs-12 col-md-12" style="padding: 0px">' +
		'<select id="sfsf_'+index+'" name="sfsf"  class="form-control chosen-select"  onblur=\'tochangeZd("'+index+'",this,"sfsf");\' >' ;
		if("1"==sfsf){
			html+='<option value="1" selected >是</option>' ;
		}else{
			html+='<option value="1">是</option>' ;
		}
		if("0"==sfsf){
			html+='<option value="0" selected >否</option>' ;
		}else{
			html+='<option value="0">否</option>' ;
		}
		if("C"==cskz1){
			if("2"==sfsf){
				html+='<option value="2" selected >免D</option>' ;
			}else{
				html+='<option value="2">免D</option>' ;
			}
			if("3"==sfsf){
				html+='<option value="3" selected >免R</option>' ;
			}else{
				html+='<option value="3">免R</option>' ;
			}
		}
		'</select>' +
		'</div>';
	return html;
}

/**
 * 应付金额
 * @param value
 * @param row
 * @param index
 * @returns
 */
function yfjeformat(value,row,index){
	var html='';
	if(row.yfje == null ||row.yfje == undefined){
		row.yfje="";
	}
	var html='<div class="col-sm-12 col-xs-12 col-md-12" style="padding: 0px">' +
		'<div class="input-group">' +
		'<input type="number" id="yfje_'+index+'" name="yfje_'+index+'" value="'+row.yfje+'" class="form-control" onblur=\'tochangeZd("'+index+'",this,"yfje");\'>' +
		'<span class="input-group-addon">元</span>' +
		'</div>' +
		'</div>';
	return html;
}

/**
 * 实付金额
 * @param value
 * @param row
 * @param index
 * @returns
 */
function sfjeformat(value,row,index){
	var html='';
	if(row.sfje == null ||row.sfje == undefined){
		row.sfje="";
	}
	var html='<div class="col-sm-12 col-xs-12 col-md-12" style="padding: 0px">' +
		'<div class="input-group">' +
		'<input type="number" id="sfje_'+index+'" name="sfje_'+index+'" value="'+row.sfje+'" class="form-control" onblur=\'tochangeZd("'+index+'",this,"sfje");\'>' +
		'<span class="input-group-addon">元</span>' +
		'</div>' +
		'</div>';
	return html;
}

/**
 * 付款日期
 * @param value
 * @param row
 * @param index
 * @returns
 */
function fkrqformat(value,row,index){
	if(row.fkrq==null || row.fkrq == undefined){
		row.fkrq="";
	}
	var html='<div class="col-sm-12 col-xs-12 col-md-12" style="padding: 0px">' +
		'<input type="text" id="fkrq_'+index+'" name="fkrq_'+index+'" value="'+row.fkrq+'"  class="form-control" onblur=\'tochangeZd("'+index+'",this,"fkrq");\'>' +
		'</div>';
	setTimeout(function() {
		laydate.render({
			elem: '#ajaxForm #fkrq_'+index
			,type: 'datetime'
			,done: function(value, date, endDate){
				t_map.rows[index].fkrq=value;
			}
		});
	}, 100);
	return html;
}

function tochangeZd(index,e,zdmc){
	if (zdmc=='sfsf'){
		t_map.rows[index].sfsf=e.value;
	}else if(zdmc=='yfje'){
		t_map.rows[index].yfje=e.value;
	}else if(zdmc=='sfje'){
		t_map.rows[index].sfje=e.value;
	}else if(zdmc=='fkrq'){
		t_map.rows[index].fkrq=e.value;
	}
}

function initTab(){
	if("1"!=$("#ajaxForm #xg_flg").val()){
		if($("#ajaxForm .jcxm").length >0){
			var json = [];
			for (let i = 0; i <= $("#ajaxForm .jcxm").length -1; i++) {
				if ($("#ajaxForm #"+$("#ajaxForm .jcxm")[i].id).is(':checked')){
					var sz = {"xmglid":'',"jcxmid":'',"jczxmid":'',"flag":'',"csmc":'',"zcsmc":'',"cskz1":'',"sfsf":'',"yfje":'',"sfje":'',"fkrq":''};
					sz.jcxmid = $("#ajaxForm .jcxm")[i].id;
					sz.jczxmid = null;
					sz.csmc = $("#ajaxForm #" + $("#ajaxForm .jcxm")[i].id).attr("csmc");
					sz.cskz1 = $("#ajaxForm #" + $("#ajaxForm .jcxm")[i].id).attr("cskz1");
					sz.zcsmc = null;
					sz.flag = $("#ajaxForm #" + $("#ajaxForm .jcxm")[i].id).attr("flag");
					json.push(sz)
				}
			}
			if($("#ajaxForm .jczxm").length >0){
				for (let i = 0; i <= $("#ajaxForm .jczxm").length -1; i++) {
					if ($("#ajaxForm #" + $("#ajaxForm .jczxm")[i].id).is(':checked')) {
						var jcxmid = $("#ajaxForm #" + $("#ajaxForm .jczxm")[i].id).attr("jcxmid")
						if (json.length > 0) {
							for (let k = json.length - 1; k >= 0; k--) {
								if (json[k].jcxmid == jcxmid && json[k].jczxmid == null) {
									json.splice(k, 1);
								}
							}
							var sz = {"xmglid":'',"jcxmid": '', "jczxmid": '',"flag":'',"csmc":'',"zcsmc":'',"cskz1":'',"sfsf":'',"yfje":'',"sfje":'',"fkrq":''};
							sz.jcxmid = $("#ajaxForm #" + $("#ajaxForm .jczxm")[i].id).attr("jcxmid");
							sz.jczxmid = $("#ajaxForm .jczxm")[i].id;
							sz.cskz1 = $("#ajaxForm #" + $("#ajaxForm .jczxm")[i].id).attr("fcskz1");
							sz.csmc = $("#ajaxForm #" + sz.jcxmid).attr("csmc");
							sz.zcsmc = $("#ajaxForm #" + $("#ajaxForm .jczxm")[i].id).attr("csmc");
							sz.flag = $("#ajaxForm #" + sz.jcxmid).attr("flag");
							json.push(sz)
						}
					}
				}
			}
			if(!json || json.length <=0)
				return
			for (let k = 0; k <json.length; k++) {
				if(!t_map.rows || t_map.rows.length <=0)
					continue
				for (var i = 0; i < t_map.rows.length; i++) {
					if (json[k].jcxmid == t_map.rows[i].jcxmid) {
						if(json[k].jczxmid){
							if (json[k].jczxmid == t_map.rows[i].jczxmid) {
								json[k].sfsf=t_map.rows[i].sfsf;
								json[k].yfje=t_map.rows[i].yfje;
								json[k].sfje=t_map.rows[i].sfje;
								json[k].fkrq=t_map.rows[i].fkrq;
								break;
							}
						}else{
							json[k].sfsf=t_map.rows[i].sfsf;
							json[k].yfje=t_map.rows[i].yfje;
							json[k].sfje=t_map.rows[i].sfje;
							json[k].fkrq=t_map.rows[i].fkrq;
							break;
						}
					}
				}
			}

			t_map.rows=[];
			for (let k = 0; k <json.length; k++) {
				var a={"index":k,"jcxmid":json[k].jcxmid,"jczxmid":json[k].jczxmid,"csmc":json[k].csmc,"zmc":json[k].zcsmc,"cskz1":json[k].cskz1,"sfsf":json[k].sfsf,"yfje":json[k].yfje,"sfje":json[k].sfje,"fkrq":json[k].fkrq};
				t_map.rows.push(a);
			}
			$("#ajaxForm #tb_list").bootstrapTable('load',t_map);
		}
	}
}

$(document).ready(function(){
	yblxselect = $("#ajaxForm #yblx").find("option:selected");
	limitKyxm();
	checkJcxm();
	qqjcinit();
	//初始化显示已选中的检测项目（若检测项目因为样本类型限制而被隐藏）
    if($("#ajaxForm .jcxm").length>0 && $("#ajaxForm #csjcxmids").val()){
		for(var i=0;i<$("#ajaxForm .jcxm").length;i++){
            var tempid=$("#ajaxForm .jcxm")[i].id;
            if($("#ajaxForm #t-"+tempid).is(":hidden") && $("#ajaxForm #csjcxmids").val().indexOf(tempid)>-1){
                //若检测项目因为样本类型限制而被隐藏+检测项目是被选中的，则选中，并显示
                document.getElementById(tempid).checked = true
                $("#ajaxForm #t-"+tempid).attr("style","display:block;padding-left:0px;");
                $("#ajaxForm #t-"+tempid).addClass("noClick")
            }
		}
	}
	jczxminit();
	if("1"!=$("#ajaxForm #xg_flg").val()){
		var oTable=new tb_TableInit();
		oTable.Init();
	}
	var oFileInput = new FileInput();
	oFileInput.Init("ajaxForm","displayUpInfo",2,1,"sjxx_file",$("#ajaxForm #ywlx").val());
	var syoFileInput = new FileInput();
	syoFileInput.Init("ajaxForm #syfjInfoTab","syDisplayUpInfo",2,1,"sy_file",$("#ajaxForm #syfjInfoTab #ywlx").val());
	//所有下拉框添加choose样式
	jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
	
	laydate.render({
	  elem: '#ajaxForm #cyrq'
	});
	laydate.render({
		elem: '#ajaxForm #sjrq'
	});
	laydate.render({
		elem: '#ajaxForm #bgrq'
		,type: 'datetime'
	    ,ready: function(date){
			if(this.dateTime.hours==0&&this.dateTime.minutes==0&&this.dateTime.seconds==0){
				var myDate = new Date(); //实例一个时间对象；
				this.dateTime.hours=myDate.getHours();
				this.dateTime.minutes=myDate.getMinutes();
	        	this.dateTime.seconds=myDate.getSeconds();
			}
    	}
 	});
	laydate.render({
		elem: '#ajaxForm #fkrq'
		,type: 'datetime'
	 	});
	laydate.render({
		elem: '#ajaxForm #tksj'
		,type: 'datetime'
	 	});
	laydate.render({
	  elem: '#ajaxForm #qyrq'
	  ,type: 'datetime'
	  ,ready: function(date){
			if(this.dateTime.hours==0&&this.dateTime.minutes==0&&this.dateTime.seconds==0){
				var myDate = new Date(); //实例一个时间对象；
				this.dateTime.hours=myDate.getHours();
				this.dateTime.minutes=myDate.getMinutes();
	        	this.dateTime.seconds=myDate.getSeconds();
			}
    	}
	});
	laydate.render({
		  elem: '#ajaxForm #ydrq'
		  ,type: 'datetime'
		  ,ready: function(date){
				if(this.dateTime.hours==0&&this.dateTime.minutes==0&&this.dateTime.seconds==0){
					var myDate = new Date(); //实例一个时间对象；
					this.dateTime.hours=myDate.getHours();
					this.dateTime.minutes=myDate.getMinutes();
		        	this.dateTime.seconds=myDate.getSeconds();
				}
	    	}
		});
	laydate.render({
		elem: '#ajaxForm #jsrq'
		,type: 'datetime'
		,ready: function(date){
			if(this.dateTime.hours==0&&this.dateTime.minutes==0&&this.dateTime.seconds==0){
				var myDate = new Date(); //实例一个时间对象；
				this.dateTime.hours=myDate.getHours();
				this.dateTime.minutes=myDate.getMinutes();
	        	this.dateTime.seconds=myDate.getSeconds();
			}
    	}
	});
	//根据新增还是修改来判断送检报告的
	var formAction=$("#formAction").val();
	if (formAction=="addSaveSjxx") {
		$("#ajaxForm #sjbg").hide();
	}
	//判断是否显示其他送检单位
	var yyxxcskz1=$("#ajaxForm #yyxxCskz1").val();
	if(yyxxcskz1=='1'){
		$("#ajaxForm #otherHospital").show();
	}else{
		$("#ajaxForm #otherHospital").hide();
	}
	btnBind();
	jc();//检测其他科室扩展参数
	checkYblxmc(true);//检测标本类型扩展参数
	//给access_token赋值 针对于外部操作，生信部访问
	if($("#ajaxForm #out_token").val()){
		access_token=$("#ajaxForm #out_token").val();
		$("#Sjxx_Save #file_li").hide();//如果为生信部访问，不显示附件
		$("#Sjxx_Save #syfile_li").hide();//如果为生信部访问，不显示附件
	}else{
		access_token=$("#ac_tk").val();
		$("#Sjxx_Save #file_li").show();//如果为本地访问，显示附件
		$("#Sjxx_Save #syfile_li").show();//如果为本地访问，显示附件
	}
	//初始化应付金额
	tmp_fkje=$("#ajaxForm #fkje").val();
	//监听送检区分改变事件
	$('input:radio[name="sjqf"]').change( function(){
		$("#ajaxForm #kyxm").find("option").prop('selected', false);
		$('#ajaxForm #kyxm').trigger("chosen:updated");
		limitKyxm();
	})
	if($("#ajaxForm .jczxm").length >0){
		for (let i = 0; i <= $("#ajaxForm .jczxm").length -1; i++) {
			var jcxmid = $("#ajaxForm #"+$("#ajaxForm .jczxm")[i].id).attr("jcxmid");
			$("#ajaxForm #"+jcxmid).attr("flag","1");
		}
	}
	var sybj =$("#ajaxForm #sybj").val()
	// if(sybj){
	// 	$("#ajaxForm #jczxm_div").attr("class","col-md-12 col-sm-12 col-xs-12 noClick");
	// 	$("#ajaxForm #jcxmDiv").attr("class","col-md-12 col-sm-12 col-xs-12 noClick");
		// $("#ajaxForm #yblx").attr("disabled","disabled");
		// $("#ajaxForm #yblx").removeAttr("style");
		// $("#ajaxForm #yblx_chosen").attr("style","display: none;");
	// }
	if(sybj){
		$("#ajaxForm #jczxm_div").attr("class","col-md-12 col-sm-12 col-xs-12 noClick noClickblack");
		$("#ajaxForm #jcxmDiv").attr("class","col-md-12 col-sm-12 col-xs-12 noClick noClickblack");
		$("#ajaxForm #yblx_chosen").attr("style","pointer-events: none;width: 100%;");
		$("#ajaxForm #yblxDiv").attr("style","cursor:not-allowed;cursor:no-drop;");
		$("#ajaxForm #yblxDiv .chosen-single").attr("style","background:#e5e5e5 !important");
	}
});
//检测输入框内是否有输入空格，若有则去除
function clearSpace(){
	var value = event.target.value;
	while (value && value.indexOf(" ") != -1 && value.indexOf("　") != -1) {
		value = value.replace(" ", "").replace("　", "");
	}
	event.target.value = value;
}
var oldHospSampleID = "";
function dataGet(){
	let hospSampleID = $("#ajaxForm #hospSampleID").val()
	while (hospSampleID && hospSampleID.indexOf(" ") != -1 && hospSampleID.indexOf("　") != -1) {
		hospSampleID = hospSampleID.replace(" ", "").replace("　", "");
	}
	$("#ajaxForm #hospSampleID").val(hospSampleID);

	if (hospSampleID && oldHospSampleID != hospSampleID){
		oldHospSampleID = hospSampleID;
		$.ajax({
			url: '/inspection/inspection/pagedataGettingInfo',
			type: 'post',
			async: true,
			data: {
				"hospSampleID": hospSampleID,
				"access_token": access_token,
				"addFlag": $("#ajaxForm #addFlag").val(),
			},
			dataType: 'json',
			success: function(result) {
			    const inputs = document.querySelectorAll('#ajaxForm input, #ajaxForm textarea, #ajaxForm select');
                inputs.forEach(input => {
                    if (input.type === 'hidden' || input.id === 'hospSampleID') {
                        return;
                    }
                    if (input.type === 'checkbox' || input.type === 'radio') {
                        input.checked = false;
                    } else if (input.tagName === 'SELECT') {
                        input.selectedIndex = 0;
                    } else {
                        input.value = '';
                    }
                });
				if (result.status  != 'success'){
					$.error("异常！"+result.message);
					return;
				}
				let tmpSjxxDto = result.sjxxDto;
				// 设置样本编号
				if (tmpSjxxDto.ybbh) {
					$("#ajaxForm #ybbh").val(tmpSjxxDto.ybbh);
				}
				// 设置外部编码
				if (tmpSjxxDto.wbbm) {
					$("#ajaxForm #wbbm").val(tmpSjxxDto.wbbm);
				}
				// 设置患者姓名
				if (tmpSjxxDto.hzxm) {
					$("#ajaxForm #hzxm").val(tmpSjxxDto.hzxm);
				}
				// 设置性别
				if (tmpSjxxDto.xb) {
					// 单选框组选中
					selectRadioButtonByValue("xb",tmpSjxxDto.xb)
				}
				// 设置年龄
				if (tmpSjxxDto.nl) {
					$("#ajaxForm #nl").val(tmpSjxxDto.nl);
				}
				// 设置年龄单位
				if (tmpSjxxDto.nldw) {
					// select选中
					selectOptionByValue("nldw",tmpSjxxDto.nldw)
				}
				// 设置电话
				if (tmpSjxxDto.dh) {
					$("#ajaxForm #dh").val(tmpSjxxDto.dh);
				}
				// 设置送检单位
				if (tmpSjxxDto.sjdw) {
					$("#ajaxForm #sjdw").val(tmpSjxxDto.sjdw);
					if(tmpSjxxDto.sjdwbj=='1'){
						// 设置送检单位名称
						if (tmpSjxxDto.sjdwmc) {
							$("#ajaxForm #sjdwmc").val(tmpSjxxDto.sjdwmc);
						}
						$("#ajaxForm #otherHospital").show();
					}else{
						// 设置送检单位名称
						if (tmpSjxxDto.sjdwmc) {
							$("#ajaxForm #sjdwmc").val(tmpSjxxDto.sjdwmc);
						}
						$("#ajaxForm #otherHospital").hide();
					}
				}
				// 设置送检单位名称
				if (tmpSjxxDto.hospitalname) {
					$("#ajaxForm #hospitalname").val(tmpSjxxDto.hospitalname);
				}
				// 设置送检医生
				if (tmpSjxxDto.sjys) {
					$("#ajaxForm #sjys").val(tmpSjxxDto.sjys);
				}
				// 设置医生电话
				if (tmpSjxxDto.ysdh) {
					$("#ajaxForm #ysdh").val(tmpSjxxDto.ysdh);
				}
				// 设置科室
				if (tmpSjxxDto.ks) {
					selectOptionByValue("ks",tmpSjxxDto.ks)
					var kzcs=$("#ajaxForm #ks option:selected");
					if (kzcs.attr("kzcs")=="1") {
						$("#ajaxForm #qtkscheck").show();
						$("#ajaxForm #qtks").removeAttr("disabled");
						$("#ajaxForm #qtks").val(kzcs.text());
					}else{
						$("#ajaxForm #qtkscheck").hide();
						$("#ajaxForm #qtks").attr("disabled","disabled");
						$("#ajaxForm #qtks").val("");
					}
				}
				// 设置科室名称
				if (tmpSjxxDto.qtks) {
					$("#ajaxForm #qtks").val(tmpSjxxDto.qtks);
				}
				// 设置住院号
				if (tmpSjxxDto.zyh) {
					$("#ajaxForm #zyh").val(tmpSjxxDto.zyh);
				}
				// 设置床位号
				if (tmpSjxxDto.cwh) {
					$("#ajaxForm #cwh").val(tmpSjxxDto.cwh);
				}
				// 设置合作伙伴
				if (tmpSjxxDto.db) {
					$("#ajaxForm #db").val(tmpSjxxDto.db);
				}
				// 设置检测项目
				if (tmpSjxxDto.jcxmids) {
					// 若tmpSjxxDto.jcxmids包含了name为jcxmids的checkbox中的值,则设置为选中
					selectCheckboxesByValues("jcxmids",tmpSjxxDto.jcxmids)
				}
				// 设置检测子项目
				if (tmpSjxxDto.jczxmids) {
					selectCheckboxesByValues("jczxmids",tmpSjxxDto.jczxmids)
				}
				// 设置送检区分
				if (tmpSjxxDto.sjqf) {
					selectRadioButtonByValue("sjqf",tmpSjxxDto.sjqf)
				}
				// 设置样本类型
				if (tmpSjxxDto.yblx) {
					selectOptionByValue("yblx",tmpSjxxDto.yblx)
				}
				// 设置样本类型名称
				if (tmpSjxxDto.yblxmc) {
					$("#ajaxForm #yblxmc").val(tmpSjxxDto.yblxmc);
				}
				// 设置样本类型名称
				if (tmpSjxxDto.ybtj) {
					$("#ajaxForm #ybtj").val(tmpSjxxDto.ybtj);
				}
				// 设置采样日期
				if (tmpSjxxDto.cyrq) {
					$("#ajaxForm #cyrq").val(tmpSjxxDto.cyrq);
				}
				// 设置快递类型
				if (tmpSjxxDto.kdlx) {
					selectOptionByValue("kdlx",tmpSjxxDto.kdlx)
				}
				// 设置快递号
				if (tmpSjxxDto.kdh) {
					$("#ajaxForm #kdh").val(tmpSjxxDto.kdh);
				}
				// 设置检测单位
				if (tmpSjxxDto.jcdw) {
					selectOptionByValue("jcdw",tmpSjxxDto.jcdw)
				}
				// 设置临床症状
				if (tmpSjxxDto.lczz) {
					$("#ajaxForm #lczz").val(tmpSjxxDto.lczz);
				}
				// 设置临床症状
				if (tmpSjxxDto.qqzd) {
					$("#ajaxForm #qqzd").val(tmpSjxxDto.qqzd);
				}
			}
		})
	}
	function selectOptionByValue(selectId, value) {
		const selectElement = document.getElementById(selectId);
		if (selectElement) {
			for (let i = 0; i < selectElement.options.length; i++) {
				if (selectElement.options[i].value === value) {
					selectElement.options[i].selected = true;
					$('#ajaxForm #'+selectId).trigger("chosen:updated");
					break;
				}
			}
		}
	}
	function selectCheckboxesByValues(checkboxName,values) {
		const checkboxes = document.querySelectorAll('input[name="'+checkboxName+'"]');
		checkboxes.forEach(checkbox => {
			if (values.includes(checkbox.value)) {
				checkbox.checked = true;
			}
		});
	}
	function selectRadioButtonByValue(radioName, value) {
		const radioButtons = document.querySelectorAll('input[name="'+radioName+'"]');
		radioButtons.forEach(radio => {
			if (radio.value === value) {
				radio.checked = true;
			}
		})
	}
}