
var yblxselect="";

$("#ajaxForm #yblx").change(function(){
	checkYblxmc();
	getDetectionInfo();
})
$("#ajaxForm .jcxm").click(function(e){
	checkJcxm();
	// 查询检测子项目
	getSubDetect(e.currentTarget.id, 1);
	getDetectionInfo();
})
$("#ajaxForm #hiddenButton").click(function(e){
    if($("#ajaxForm #hiddenInfo").is(':hidden')){
        $("#ajaxForm #hiddenInfo").show();
        $("#ajaxForm #hiddenButton").html("隐藏");
    } else{
        $("#ajaxForm #hiddenButton").html("显示");
        $("#ajaxForm #hiddenInfo").hide();
    }
})

var access_token;
$(document).ready(function(){
    access_token = $("#ac_tk").val();
	//所有下拉框添加choose样式
	jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
	yblxselect = $("#ajaxForm #yblx").find("option:selected");
	jcxminit();
	jczxminit();
    checkYblxmc();
    initLaydate("jcsj");
    initLaydate("sjsj");
    initLaydate("xjsj");
    initLaydate("jsrq");
    initLaydate("qysj");
    initLaydate("syrq");
})

//初始化时间控件
function initLaydate(classname){
    if($("#ajaxForm ."+classname) && $("#ajaxForm ."+classname).length>0){
        $("#ajaxForm ."+classname).each(function(){
            laydate.render({
                elem: this
                ,type: 'datetime'
                ,ready: function(date){
                    if(this.dateTime.hours==0 && this.dateTime.minutes==0 && this.dateTime.seconds==0){
                        var myDate = new Date(); //实例一个时间对象；
                        this.dateTime.hours=myDate.getHours();
                        this.dateTime.minutes=myDate.getMinutes();
	        	        this.dateTime.seconds=myDate.getSeconds();
                    }
                }
            });
        })
    }
}

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
//样本类型改变事件
function checkYblxmc(){
	var oldyblxcsdm = yblxselect.attr("csdm");
	yblxselect=$("#ajaxForm #yblx option:selected");
	var newyblxcsdm=yblxselect.attr("csdm");
    $("#ajaxForm #yblxmcspan").show();
    $("#ajaxForm #yblxmc").removeAttr("disabled");
    if (oldyblxcsdm != 'XXX' && newyblxcsdm != 'XXX'){
        $("#ajaxForm #yblxmc").val(yblxselect.attr("csmc"));
    }
	jcxminit();
}

//
function jcxminit(){
    //根据样本类型限制检测项目
	var kzcs = $("#ajaxForm #yblx option:selected");
	var csdm = kzcs.attr("cskz2");
	//获取选中的检测项目
	var jcxmids="";
	$("input[name='jcxmids']:checked").each(function(){
		jcxmids+=","+$(this).attr("id");
	})
	var oncoFlag=false;//onco标记
	var tNGSFlag=false;//tNGS标记
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
				}else if(cskz3=='IMP_REPORT_SEQ_INDEX_TEMEPLATE'&&cskz4=='O'){
					oncoFlag=true;
				}
				if(cskz3.indexOf('IMP_REPORT_TNGS_TEMEPLATE')>=0&&cskz4=='T'){
					tNGSFlag=true;
				}else if(cskz3.indexOf('IMP_REPORT_SEQ_TNGS')>=0&&cskz4=='K'){
					tNGSFlag=true;
				}
			})
			if(oncoFlag){
				if($("#ajaxForm .jcxm").eq(i).attr("cskz3")=='IMP_REPORT_SEQ_INDEX_TEMEPLATE'&&$("#ajaxForm .jcxm").eq(i).attr("cskz4")=='O'){
					isFind=true;
				}else if($("#ajaxForm .jcxm").eq(i).attr("cskz3")=='IMP_REPORT_ONCO_QINDEX_TEMEPLATE'&&$("#ajaxForm .jcxm").eq(i).attr("cskz4")=='Q'){
					isFind=true;
				}
			}
			if(tNGSFlag){
				var t_cskz3 = $("#ajaxForm .jcxm").eq(i).attr("cskz3")
				if(t_cskz3.indexOf('IMP_REPORT_TNGS_TEMEPLATE')>=0&&$("#ajaxForm .jcxm").eq(i).attr("cskz4")=='T'){
					isFind=true;
				}else if(t_cskz3.indexOf('IMP_REPORT_SEQ_TNGS')>=0&&$("#ajaxForm .jcxm").eq(i).attr("cskz4")=='K'){
					isFind=true;
				}
			}
			if(kzcs[0].id==null || kzcs[0].id==""){
				if(isFind){
					if(jcxmids.indexOf(id)==-1){
						$("#ajaxForm #t-"+id).attr("style","display:none");
						removeChecked(id)
					}else{
						$("#ajaxForm #t-"+id).attr("style","display:block;padding-left:0px;");
					}
				}else{
					$("#ajaxForm #t-"+id).attr("style","display:block;padding-left:0px;");
				}
			}else{
				if(csdm!=null && csdm!=""){
					if(csdm.indexOf($("#ajaxForm #"+id).attr("csdm"))>=0){
						if(isFind){
							if(jcxmids.indexOf(id)==-1){
								$("#ajaxForm #t-"+id).attr("style","display:none");
								removeChecked(id)
							}else{
								$("#ajaxForm #t-"+id).attr("style","display:block;padding-left:0px;");
							}
						}else{
							$("#ajaxForm #t-"+id).attr("style","display:block;padding-left:0px;");
						}
					}else{
						$("#ajaxForm #t-"+id).attr("style","display:none");
						removeChecked(id)
					}
				}else{
					$("#ajaxForm #t-"+id).attr("style","display:none;");
					removeChecked(id)
				}
			}
		}
	}
}

//隐藏检测项目
function removeChecked(id){
	document.getElementById(id).checked=false;
	if ($("#ajaxForm .jczxm").length > 0){
		for (let j = $("#ajaxForm .jczxm").length -1; j >= 0 ; j--) {
			var jcxmid = $("#ajaxForm #"+$("#ajaxForm .jczxm")[j].id).attr("jcxmid");
			if (jcxmid == id){
				$("#ajaxForm #zxm_"+$("#ajaxForm .jczxm")[j].id).remove();
			}
		}
		jczxminit()
	}
}
//检测项目改变事件
function checkJcxm(){
	jcxminit();
	limitYblx();
}
//根据检测项目获取检测子项目
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
							"<input class ='jczxm' id='"+data[i].csid+"' name='"+data[i].csid+"' type='checkbox' value='" + data[i].csid + "' jcxmid='" + data[i].fcsid + "' fcskz1='" + data[i].fcskz1 + "' csmc='" + data[i].csmc + "/>" +
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
//检测子项目初始化
function jczxminit(){
	if ($("#ajaxForm .jczxm").length == 0){
		$("#ajaxForm #jczxm_div").hide();
	}
}
//保存用户改变是事件id
var chooseYhInfo;
//选择用户
function chooseYh(id) {
    chooseYhInfo = id;
	var url = "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择用户', chooseYhConfig);
}
//用户选择框
var chooseYhConfig = {
	width : "800px",
	height : "500px",
	modalName	: "chooseYhModal",
	formName	: "ajaxForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#taskListFzrForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					$('#ajaxForm #'+chooseYhInfo).val(sel_row[0].yhid);
					$('#ajaxForm #'+chooseYhInfo +'mc').val(sel_row[0].zsxm+'-'+sel_row[0].yhm);
					$.closeModal(opts.modalName);
					//保存操作习惯
					$.ajax({
						type:'post',
						url:"/common/habit/commonModUserHabit",
						cache: false,
						data: {"dxid":sel_row[0].yhid,"access_token":$("#ac_tk").val()},
						dataType:'json',
						success:function(data){}
					});
				}else{
					$.error("请选中一行");
					return;
				}
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
//获取jcxm
function initJcxmJson(){
    var json = [];
    for (let i = 0; i <= $("#ajaxForm .jcxm").length -1; i++) {
        if ($("#ajaxForm #"+$("#ajaxForm .jcxm")[i].id).is(':checked')){
            var sz = {"jcxmid":'',"jczxmid":'',"flag":'',"csmc":'',"zcsmc":''};
            sz.jcxmid = $("#ajaxForm .jcxm")[i].id;
            sz.jczxmid = null;
            sz.csmc = $("#ajaxForm #" + $("#ajaxForm .jcxm")[i].id).attr("csmc");
            sz.zcsmc = null;
            sz.flag = $("#ajaxForm #" + $("#ajaxForm .jcxm")[i].id).attr("flag");
            json.push(sz)
        }
    }
    if (json.length == 0){
        $.alert("请选择检测项目！");
        return false;
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
                    var sz = {"xmglid":'',"jcxmid": '', "jczxmid": '',"flag":'',"csmc":'',"zcsmc":'',"sfsf":'',"yfje":'',"sfje":'',"fkrq":''};
                    sz.jcxmid = $("#ajaxForm #" + $("#ajaxForm .jczxm")[i].id).attr("jcxmid");
                    sz.jczxmid = $("#ajaxForm .jczxm")[i].id;
                    sz.csmc = $("#ajaxForm #" + sz.jcxmid).attr("csmc");
                    sz.zcsmc = $("#ajaxForm #" + $("#ajaxForm .jczxm")[i].id).attr("csmc");
                    sz.flag = $("#ajaxForm #" + sz.jcxmid).attr("flag");
                    json.push(sz)
                }
            }
        }
        for (let k = json.length - 1; k >= 0; k--) {
            if (json[k].flag == "1"){
                if (json[k].jczxmid == null){
                    return false;
                }
            }
        }
    }

    var csjcxmList = JSON.parse($("#ajaxForm #csjcxm").val());
    var jcxmmc = "";
    for (let i = 0; i < json.length; i++) {
        jcxmmc+=","+json[i].csmc;
        if (json[i].zcsmc)
            jcxmmc+="-"+json[i].zcsmc;
        for (let j = csjcxmList.length-1; j >= 0; j--) {
            if (json[i].jcxmid == csjcxmList[j].jcxmid && (!json[i].jczxmid || !csjcxmList[j].jczxmid || json[i].jczxmid == csjcxmList[j].jczxmid)) {
                json[i].xmglid = csjcxmList[j].xmglid;
                json[i].sfsf = csjcxmList[j].sfsf;
                json[i].yfje = csjcxmList[j].yfje;
                json[i].sfje = csjcxmList[j].sfje;
                json[i].fkrq = csjcxmList[j].fkrq;
            }
        }
    }
    $("#ajaxForm #jcxmmc").val(jcxmmc.substring(1));
    $("#ajaxForm #jcxm").val(JSON.stringify(json))
    return true;
}
//获取送检实验数据
function getDetectionInfo(){
    //获取样本类型代码
    var yblxdm = $("#ajaxForm #yblx option:selected").attr("csdm");
	// 20240904 "其它"样本类型的csdm由XXX改为G
	if("XXX".equals(yblxdm) || "G".equals(yblxdm)){
    //样本类型选择其他时
        if($("#ajaxForm #yblxmc").val() && ($("#ajaxForm #yblxmc").val() == "血浆" || $("#ajaxForm #yblxmc").val() == "全血")){
            yblxdm = "B"
        }
        if($("#ajaxForm #nbbm").val()){
            var nbbm = $("#ajaxForm #nbbm").val();
            yblxdm = nbbm.substring(nbbm.length-1);
        }
    }
    var sjqfdm = $("#ajaxForm #sjqfdm").val();
    var jcdwmc = $("#ajaxForm #jcdwmc").val();
    var sjid = $("#ajaxForm #sjid").val();
    var isSuccess = initJcxmJson();
    var jcxm =$("#ajaxForm #jcxm").val();
    if(isSuccess && jcxm){
        $.ajax({
            url: '/inspection/inspection/pagedataGetDetectionInfo',
            type: 'post',
            async: false,
            data: {
                "sjid": sjid,
                "sjqf": sjqfdm,
                "yblxdm": yblxdm,
                "jcdwmc": jcdwmc,
                "jcxm": jcxm,
                "access_token":access_token
            },
            dataType: 'json',
            success: function(result) {
                if(result){
                    var sjsyglDtos = result.sjsyglDtos;
                    var jcdwlist = result.jcdwlist;
                    dealEditInfoHtml(sjsyglDtos,jcdwlist);
                }
            }
        });
    }
}
//拼接html
function dealEditInfoHtml(sjsyglDtos,jcdwlist){
    var html = "";
    if(sjsyglDtos && sjsyglDtos.length > 0){
        for(var i = 0; i < sjsyglDtos.length; i++){
            html += '<div class="panel panel-default" id="'+i+'" th:xh="'+i+'" index="'+i+'">'
                    + '<input type="hidden" id="' + i + '_dyid" name="' + i + '_dyid" value="'+sjsyglDtos[i].dyid+'"/>'
                    + '<input type="hidden" id="' + i + '_sjid" name="' + i + '_sjid" value="'+sjsyglDtos[i].sjid+'"/>'
                    + '<input type="hidden" id="' + i + '_jcxmid" name="' + i + '_jcxmid" value="'+sjsyglDtos[i].jcxmid+'"/>'
                    + '<input type="hidden" id="' + i + '_jcxmmc" name="' + i + '_jcxmmc" value="'+sjsyglDtos[i].jcxmmc+'"/>'
                    + '<input type="hidden" id="' + i + '_jczxmid" name="' + i + '_jczxmid" value="'+sjsyglDtos[i].jczxmid+'"/>'
                    + '<input type="hidden" id="' + i + '_jclxid" name="' + i + '_jclxid" value="'+sjsyglDtos[i].jclxid+'"/>'
                    + '<input type="hidden" id="' + i + '_nbzbm" name="' + i + '_nbzbm" value="'+sjsyglDtos[i].nbzbm+'"/>'
                    + '<input type="hidden" id="' + i + '_wksxbm" name="' + i + '_wksxbm" value="'+sjsyglDtos[i].wksxbm+'"/>'
                    + '<div class="panel-body">'
                        + '<div class="row">'
                            + '<div class="col-md-6 col-sm-6">'
                                + '<div class="form-group">'
                                    + '<label for="" class="col-sm-4 col-xs-6 control-label" style="padding: 7px 0 0 0 !important;">子编码:</label>'
                                    + '<div class="col-sm-3 col-xs-6 control-label" style="text-align: left;">'
                                        + '<span class="nbzbm" title="' +sjsyglDtos[i].nbzbm+ '">'+sjsyglDtos[i].nbzbm+'</span>'
                                    + '</div>'
                                    + '<label for="" class="col-sm-2 col-xs-6 control-label" style="padding: 0">接头:</label>'
									+ '<div class="col-sm-3 col-xs-6">'
										+ '<input id="' + i + '_jt" name="' + i + '_jt" type="text" value="'+sjsyglDtos[i].jt+'" class="form-control jt" autocomplete="off"/>'
									+ '</div>'
								+ '</div>'
                            + '</div>'
                            + '<div class="col-md-6 col-sm-6">'
                                + '<div class="form-group">'
                                    + '<label for="" class="col-sm-4 col-xs-6 control-label" style="padding: 0">'
                                    	+ '<span style="color: red;">*</span>检测单位:'
                                    + '</label>'
                                    + '<div class="col-sm-8 col-xs-6 jcdw">'
                                        + '<select id="' + i + '_jcdw" name="' + i + '_jcdw" value="'+(sjsyglDtos[i].jcdw?sjsyglDtos[i].jcdw:$("#ajaxForm #jcdw").val())+'" class="form-control chosen-select" validate="{required:true}" >'
                                            + jcdwHtml(jcdwlist,(sjsyglDtos[i].jcdw?sjsyglDtos[i].jcdw:$("#ajaxForm #jcdw").val()))
                                        + '</select>'
                                    + '</div>'
                                + '</div>'
                            + '</div>'
                        + '</div>'
                        + '<div class="row">'
                        	+ '<div class="col-md-6 col-sm-6">'
                        		+ '<div class="form-group">'
                        			+ '<label for="" class="col-sm-4 col-xs-6 control-label" style="padding: 7px 0 0 0 !important;">检测日期:</label>'
                        			+ '<div class="col-sm-8 col-xs-6">'
                        				+ '<input id="' + i + '_jcsj" name="' + i + '_jcsj" type="text" value="'+sjsyglDtos[i].jcsj+'" class="form-control jcsj" autocomplete="off"/>'
                        			+ '</div>'
                        		+ '</div>'
                        	+ '</div>'
                        	+ '<div class="col-md-6 col-sm-6">'
                        		+ '<div class="form-group">'
                        			+ '<label for="" class="col-sm-4 col-xs-6 control-label" style="padding: 7px 0 0 0 !important;">实验日期:</label>'
                        			+ '<div class="col-sm-8 col-xs-6">'
                        				+ '<input id="' + i + '_syrq" name="' + i + '_jcsj" type="text" value="'+sjsyglDtos[i].syrq+'" class="form-control syrq" autocomplete="off"/>'
                        			+ '</div>'
                        		+ '</div>'
                        	+ '</div>'
                        	+ '<div class="col-md-6 col-sm-6">'
                        		+ '<div class="form-group">'
                        			+ '<label for="" class="col-sm-4 col-xs-6 control-label" style="padding: 7px 0 0 0 !important;">上机时间:</label>'
                        			+ '<div class="col-sm-8 col-xs-6">'
                        				+ '<input id="' + i + '_sjsj" name="' + i + '_sjsj" type="text" value="'+sjsyglDtos[i].sjsj+'" class="form-control sjsj" autocomplete="off"/>'
                        			+ '</div>'
                        		+ '</div>'
                        	+ '</div>'
                        	+ '<div class="col-md-6 col-sm-6">'
                        		+ '<div class="form-group">'
                        			+ '<label for="" class="col-sm-4 col-xs-6 control-label" style="padding: 7px 0 0 0 !important;">下机时间:</label>'
                        			+ '<div class="col-sm-8 col-xs-6">'
                        				+ '<input id="' + i + '_xjsj" name="' + i + '_xjsj" type="text" value="'+sjsyglDtos[i].xjsj+'" class="form-control xjsj" autocomplete="off"/>'
                        			+ '</div>'
                        		+ '</div>'
                        	+ '</div>'
                        + '</div>'
                        + '<div class="row">'
                        	+ '<div class="col-md-6 col-sm-6">'
                        		+ '<div class="form-group">'
                        			+ '<label for="" class="col-sm-4 col-xs-6 control-label" style="padding: 7px 0 0 0 !important;">接收人员:</label>'
                        			+ '<div class="col-sm-8 col-xs-6">'
                        			    + '<div class="input-group">'
                        			        + '<input id="' + i + '_jsrymc" name="' + i + '_jsrymc" type="text" value="'+(sjsyglDtos[i].jsrymc!=null && sjsyglDtos[i].jsryyhm!=null ? sjsyglDtos[i].jsrymc+'-'+sjsyglDtos[i].jsryyhm:'')+'" readonly="readonly" class="form-control" />'
                        			        + '<input id="' + i + '_jsry" name="' + i + '_jsry" type="hidden"  value="'+sjsyglDtos[i].jsry+'" class="form-control jsry" validate="{required:true}"/>'
                        			        + '<span class="input-group-btn">'
                        			            + '<button type="button" th:onclick="chooseYh(\'' + i + '_jsry\')" class="btn btn-primary">选择</button>'
                        			        + '</span>'
                        			    + '</div>'
                        			+ '</div>'
                        		+ '</div>'
                        	+ '</div>'
                        	+ '<div class="col-md-6 col-sm-6">'
                        		+ '<div class="form-group">'
                        			+ '<label for="" class="col-sm-4 col-xs-6 control-label" style="padding: 7px 0 0 0 !important;">接收日期:</label>'
                        			+ '<div class="col-sm-8 col-xs-6">'
                        			    + '<input id="' + i + '_jsrq" name="' + i + '_jsrq" type="text" value="'+sjsyglDtos[i].jsrq+'" class="form-control jsrq" autocomplete="off"/>'
                        			+ '</div>'
                        		+ '</div>'
                        	+ '</div>'
                        	+ '<div class="col-md-6 col-sm-6">'
                        		+ '<div class="form-group">'
                        			+ '<label for="" class="col-sm-4 col-xs-6 control-label" style="padding: 7px 0 0 0 !important;">取样人员:</label>'
                        			+ '<div class="col-sm-8 col-xs-6">'
                        				+ '<div class="input-group">'
                        				    + '<input id="' + i + '_qyrymc" name="' + i + '_qyrymc" type="text" value="'+(sjsyglDtos[i].qyrymc!=null && sjsyglDtos[i].qyryyhm!=null ? sjsyglDtos[i].qyrymc+'-'+sjsyglDtos[i].qyryyhm:'')+'" readonly="readonly" class="form-control" />'
                        			        + '<input id="' + i + '_qyry" name="' + i + '_qyry" type="hidden"  value="'+sjsyglDtos[i].qyry+'" class="form-control qyry" validate="{required:true}"/>'
                        					+ '<span class="input-group-btn">'
                        			            + '<button type="button" th:onclick="chooseYh(\'' + i + '_qyry\')" class="btn btn-primary">选择</button>'
                        					+ '</span>'
                        				+ '</div>'
                        			+ '</div>'
                        		+ '</div>'
                        	+ '</div>'
                        	+ '<div class="col-md-6 col-sm-6">'
                        		+ '<div class="form-group">'
                        			+ '<label for="" class="col-sm-4 col-xs-6 control-label" style="padding: 7px 0 0 0 !important;">取样时间:</label>'
                        			+ '<div class="col-sm-8 col-xs-6">'
                        			    + '<input id="' + i + '_qysj" name="' + i + '_qysj" type="text" value="'+sjsyglDtos[i].qysj+'" class="form-control qysj" autocomplete="off"/>'
                        			+ '</div>'
                        		+ '</div>'
                        	+ '</div>'
                        + '</div>'
                    + '</div>'
                + '</div>'
        }
    }
    $("#editInfo").html(html)
}
//拼接检测单位
function jcdwHtml(jcdwlist,jcdw){
    var html = '';
    if(jcdwlist && jcdwlist.length > 0){
        for(var i=0;i<jcdwlist.length;i++){
            if(jcdwlist[i].csid == jcdw){
                html += '<option  value="'+ jcdwlist[i].csid +'" selected>'+jcdwlist[i].csmc+'</option>';
            } else{
                html += '<option  value="'+ jcdwlist[i].csid +'">'+jcdwlist[i].csmc+'</option>';
            }
        }
    }
    return html;
}
//内部编码改变事件
function getNbbmChangeInfo(){
    var yblxdm = $("#ajaxForm #yblx option:selected").attr("csdm");
	// 20240904 "其它"样本类型的csdm由XXX改为G
    if("XXX" == yblxdm || "G" == yblxdm || $("#ajaxForm #yblx option:selected").attr("kzcs") == "1"){
        //只有样本类型选择其他时，才触发事件
        getDetectionInfo()
    }
}
