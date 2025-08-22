
$(document).ready(function(){
	showloading("正在初始化...")
    $("#ajaxForm #hzxxInfo").show()
    $("#ajaxForm #sjdwInfo").hide()
    if(!getWxOrNot()){
        return false;
    }
    //绑定事件
    btnBind();
    //初始化页面数据
    getBasicData();
	hideloading();
});
$("#ajaxForm #btn_complete").unbind("click").click(function(){
	if (!isButtonActive){
		return false;
	}
	buttonDisabled(true,false,"",true);
    if(!getWxOrNot()){
        return buttonDisabled(false,false,"",false);
    }

    //校验样本编号，录单必填项
	$("#ajaxForm #ybbh").val($("#ajaxForm #ybbh").val().replace(/\s+/g,""));
	if (!checkYbbh()){
        return false;
    }
    //校验样本类型，录单必填项
    if (!$("#yblx").val()) {
        return buttonDisabled(false,true,"请选择标本类型!",false);
    }
    var yblx=$("#yblx").val();
    if(!$("#ajaxForm #qt").is(":hidden")){
        if (!$("#yblxmc").val()) {
            return buttonDisabled(false,true,"请填写其它标本类型!",false);
        }
    }
    var yblxmc = $("#yblxmc").val();
    //校验患者姓名，录单必填项
    if(!$("#ajaxForm #xm").val()){
        return buttonDisabled(false,true,"请填写患者姓名!",false);
    }
    //校验性别，录单必填项
    if($("#ajaxForm input:radio[name='xb']:checked").val()=="" || $("#ajaxForm input:radio[name='xb']:checked").val()==null){
        return buttonDisabled(false,true,"请选择性别!",false);
    }
    //校验年龄，录单必填项
    if(!$("#ajaxForm #nl").val()){
        return buttonDisabled(false,true,"请填写年龄!",false);
    }
    //校验年龄单位，录单必填项
//    if(!$("#ajaxForm #nldw").val()){
//        return buttonDisabled(false,true,"请选择年龄单位!",false);
//    }
    //校验检测项目，录单必填项
    if(!$("#ajaxForm #jcxmids").val()){
        return buttonDisabled(false,true,"请选择检测项目!",false);
    }
    //校验检测子项目，录单必填项
    if(!$("#ajaxForm #jczxmDiv").is(":hidden")){
        if(!$("#ajaxForm #jczxmids").val()){
            return buttonDisabled(false,true,"请选择检测子项目!",false);
        }
    }
    var jcxmmc="";
    var jcxmid="";
    var json=[];
    $("#ajaxForm input[name='jcxm']").each(function(){
        var flag=$(this).prop("checked");
        if(flag){
            jcxmmc+=","+$(this).attr("csmc");
            jcxmid+=","+$(this).attr("id");
            var hasSub=false;
            for (var i = 0; i < fzjczxmList.length; i++) {
                if($(this).val()==fzjczxmList[i].fcsid){
                    hasSub=true;
                    break;
                }
            }
            if(!hasSub){
                var sz={"fzjcxmid":$(this).val(),"fzjczxmid":"","fzxmid":""};
                json.push(sz);
            }
        }
    });
    jcxmmc = jcxmmc.substring(1);
    jcxmid = jcxmid.substring(1);
    $("#ajaxForm input[name='jczxm']").each(function(){
        var flag=$(this).prop("checked");
        if(flag){
            var sz={"fzjcxmid":$(this).attr("fcsid"),"fzjczxmid":$(this).val(),"fzxmid":""};
            json.push(sz);
        }
    });
    if(fzjcxmDtos!=null&&fzjcxmDtos.length>0){
        for(var i=0;i<json.length;i++){
            for(var j=0;j<fzjcxmDtos.length;j++){
                if(json[i].fzjcxmid==fzjcxmDtos[j].fzjcxmid){
                    if(json[i].fzjczxmid){
                        if(json[i].fzjczxmid==fzjcxmDtos[j].fzjczxmid){
                            json[i].fzxmid=fzjcxmDtos[j].fzxmid;
                        }
                    }else{
                        json[i].fzxmid=fzjcxmDtos[j].fzxmid;
                    }
                }
            }
        }
    }

	showloading("正在保存...");
	$.ajax({
    		type:'post',
    		url:"/wechat/detectionPJ/addSaveGeneralInspection",
    		cache: false,
    		data: {
    			"wxid"	    :$("#wxid").val(),
    			"wbcxdm"    :$("#wbcxdm").val(),
                "fzjcid"    :($("#fzjcid").val()?$("#fzjcid").val():""),
    			"fzjclx"    :$("#fzjclx").val(),
    			"fzjclxdm"  :$("#fzjclxdm").val(),
    			"ybbh"	    :$("#ajaxForm #ybbh").val(),//样本编号
    			"yblx"	    :$("#yblx").val(),//样本类型
    			"yblxmc"    :$("#yblxmc").val(),//样本类型名称
    			"xm"        :$("#xm").val(),//姓名
    			"xb"	    :$("#ajaxForm input:radio[name='xb']:checked").val(),//性别
    			"nl"	    :$("#ajaxForm #nl").val(),//年龄
//    			"nldw"	    :$("#ajaxForm #nldw").val(),//年龄单位
    			"lxdh"	:($("#ajaxForm #lxdh").val()?$("#ajaxForm #lxdh").val():''),//联系电话
    			"sjys"	:($("#sjys").val()?$("#sjys").val():''),//送检医生
//    			"ysdh"	:($("#ajaxForm #ysdh").val()?$("#ajaxForm #ysdh").val():''),//医生电话，非必填项
    			"sjdw"	:$("#ajaxForm #sjdwid").val(),//送检单位
    			"yymc"	:$("#ajaxForm #sjdwmc").val(),//报告显示单位
    			"sjdwmc":$("#ajaxForm #sjdwmc").val(),//报告显示单位
    			"ks"	:$("#ajaxForm #ksid").val(),//科室
    			"qtks"	:$("#ajaxForm #qtks").val(),//报告显示科室
    			"jcdw"	:$("#ajaxForm #jcdwid").val(),//检测单位
    			"jcxmids":$("#ajaxForm #jcxmids").val().replace("[","").replace("]",""),//检测项目，录单必填项
    			"jcxmid":jcxmid,//检测项目，录单必填项
    			"jcxmmc":jcxmmc,//检测项目，录单必填项
    			"jcxm"	:JSON.stringify(json),
    			"xgsj":$("#ajaxForm #xgsj").val(),//页面打开时间
    		},
    		dataType:'json',
    		success:function(data){
    			hideloading();
    			if (data.status=="success"){
                    $.modal({
                        text: data.message,
                        buttons: [
                            { text: "进入清单", onClick: function(){
                                    window.location.replace('/wechat/detectionPJ/generalInspectionPerfect?wxid=' + $("#wxid").val() + '&fzjclx='+ $("#fzjclx").val()+'&wbcxdm='+ $("#wbcxdm").val());
                                }},
                            { text: "再录一单", onClick: function(){
                                    window.location.replace('/wechat/detectionPJ/generalInspectionInput?wxid=' + $("#wxid").val() + '&fzjclx='+ $("#fzjclx").val()+'&wbcxdm='+ $("#wbcxdm").val());
                                }},
                        ]
                    });
                }else {
                    buttonDisabled(false,true,data.message,false);
                }
    		},
    		error: function (data) {
    			hideloading();
    			buttonDisabled(false,true,data,false);
    		}

    	})
    return true;
})
/**
 * 0、初始化
 */
//0.0、初始化
var isButtonActive =  true;
var loadMoreSjdwFlag = true;//加载更多送检单位标记
var pageFlag = "hzxxInfo";
var pageNumber = 0;
var pageSize = 15;
var loadingFlag = false;
var localids=[];
var navigationIndex = 0;
//定义计时器(二维码预览使用)
var interval = null;
//0.1、判断是否获取到微信id
function getWxOrNot (){
    var wxid = $("#wxid").val();
    if (wxid==null || wxid ==''){
        $.alert("没有获取到您的微信信息，请重新至公众号打开！")
        return false;
    }
    return true;
}
//0.2、获取基础数据
function getBasicData(){
    $.ajax({
        url:'/wechat/detectionPJ/getGeneralInspBasicData',
        type:'post',
        dataType:'json',
        data:{
            "fzjclx" : $("#fzjclx").val(),
            "fzjcid" : $("#fzjcid").val()
        },
        success:function(data){
            jcdwList = data.jcdwList;
            fzjcxmList = data.fzjcxmList;
            fzjczxmList = data.fzjczxmList;
            ksxxList = data.ksxxList;
            yblxList = data.yblxList;
            fzjcxmDtos = data.fzjcxmDtos;
            dealBasicData()
        },
        failure : function(data) {
            $.alert(JSON.stringify(data));
        }
    })
}
//0.3、处理基础数据
function dealBasicData(){
    dealKsData();
    dealFzjcxmData();
    dealJcdwData();
    dealYblxData();
    //送检单位
    if($("#sjdwbj").val()=="1"){
        $("#qtdwInput").show()
    }else {
        $("#qtdwInput").hide()
    }
}
//0.4、设置上一页为空
function pushHistory() {
    var state = {
        title: "title",
        url: "#"
    };
    window.history.pushState(state, "title", "#");
}
//0.5、返回事件
function goBack(){
    if($("#ajaxForm #hzxxInfo").is(":hidden")){
        pushHistory();
        if(!$("#ajaxForm #sjdwInfo").is(":hidden")){
            //若 送检医生选择页面 是显示的
            $("#ajaxForm #hzxxInfo").show()
            $("#ajaxForm #sjdwInfo").hide()
        }
    }
    else {
        window.history.go(navigationIndex)
    }
}
//0.6、跳转下一个焦点
function JumpToNext() {
    if (event.keyCode == 13) {
        var nextFocusIndex = this.getAttribute("nextFocusIndex");
        document.all[nextFocusIndex].focus();
    }
}
//0.7、按钮禁用事件
/**
 * @param isError 是否提示错误信息
 * @param isDisabled	是否禁用按钮
 * @param errorInfo	错误信息内容
 * @param returnFlg	返回状态 true:成功 false:失败
 * @returns {*}
 */
function buttonDisabled(isDisabled,isError,errorInfo,returnFlg){
    if (isDisabled){
        isButtonActive = false;
        $("#ajaxForm #btn_complete").attr("disabled","true");
        $("#ajaxForm #info_save").attr("disabled","true");
    }else {
        isButtonActive = true;
        $("#ajaxForm #btn_complete").removeAttr("disabled");
        $("#ajaxForm #info_save").removeAttr("disabled");
    }
    if (isError){
        $.toptip(errorInfo, 'error');
    }
    return returnFlg;
}

/**
 * 1、样本编号
 */
//1.1、样本编号扫码事件
function btnBind() {
    //初始化微信信息
    $.ajax({
        url: '/wechat/getJsApiInfo',
        type: 'post',
        data: {
            "url":location.href.split('#')[0],
            "wbcxdm":$("#wbcxdm").val()
        },
        dataType: 'json',
        success: function(result) {
            //注册信息
            wx.config({
                debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                appId: result.appid, // 必填，公众号的唯一标识
                timestamp: result.timestamp, // 必填，生成签名的时间戳
                nonceStr: result.noncestr, // 必填，生成签名的随机串
                signature: result.sign, // 必填，签名，见附录1
                jsApiList: ['checkJsApi','scanQRCode','startRecord','stopRecord','onVoiceRecordEnd','playVoice','pauseVoice','stopVoice','onVoicePlayEnd','uploadVoice','downloadVoice','translateVoice','chooseImage','previewImage','uploadImage','downloadImage']
                // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
            });
            wx.error(function(res) {
                console.log(res);
            });
            //config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作
            wx.ready(function() {
                wx.checkJsApi({
                    jsApiList: ['scanQRCode','startRecord','stopRecord','onVoiceRecordEnd','playVoice','pauseVoice','stopVoice','onVoicePlayEnd','uploadVoice','downloadVoice','translateVoice','chooseImage','previewImage','uploadImage','downloadImage'],
                    success: function(res) {
                        /**
                         * 样本编号扫码事件
                         */
                        $("#getQRCode").unbind("click").click(function(){
                            //扫码事件
                            wx.scanQRCode({
                                needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果
                                scanType: ["qrCode", "barCode"],
                                success: function(res) {
                                    // 当needResult 为 1 时，扫码返回的结果
                                    var result = res.resultStr;
                                    // http://service.matridx.com/wechat/getUserReport?ybbh=1112
                                    var s_res = result.split('ybbh=')

                                    $("#ajaxForm #ybbh").val(s_res[s_res.length-1]);
                                },
                                fail: function(res) {
                                    alert(JSON.stringify(res));
                                }
                            });
                        });
                    }
                });
            });
        }
    });
}
//1.2、校验样本编号
function checkYbbh(){
    let ybbh = $("#ajaxForm #ybbh").val();
    if(!ybbh){
        $.toptip("请填写样本编号!", 'error');
        return false;
    }
    //若样本编号中包含除英文、数字以及-以外的字符，则提示错误
    if(!/^[a-zA-Z0-9-]+$/.test(ybbh)){
        $.toptip("样本编号只能包含英文、数字以及-!", 'error');
        return false;
    }
    return true;
}

/**
 * 2、年龄单位
 */
//2.1、年龄单位列表
var nldwList = [{label: '岁',value: '岁'}, {label: '个月',value: '个月'}, {label: '天',value: '天'}, {label: '无',value: '无'}];
//2.2、年龄单位改变事件
function chooseNldw(){
    setTimeout(function(){
        weui.picker(
            nldwList, {
                defaultValue: [$("#nldw").val()],
                onConfirm: function (result) {
                    $("#nldw").val(result[0].value)
                },
                id: 'chooseNldw',
                title: '请选择年龄单位'
            });
    },200);
}

/**
 * 3、送检单位
 */
//3.1、送检单位选择事件
function changeSjdw(){
    pageFlag = "sjdwInfo";
    $("#hzxxInfo").hide();
    $("#sjdwInfo").show();
    $("#sjdwCxnr").val("")
    searchSjdwList()
}
//3.2、选择送检单位页面返回事件
function sjdwBack(){
    pageFlag = "hzxxInfo";
    $("#hzxxInfo").show();
    $("#sjdwInfo").hide();
}
//3.3、送检单位查询事件
function searchSjdwList(){
    loadMoreSjdwFlag=true;
    $("#sjdw_loading").show()
    $("#sjdw_noInfo").hide();
    $("#sjdw_loadComplete").hide();
    pageNumber = 0;
    $("#sjdwList").html("")
    loadMoreSjdw()
}
//3.4、送检单位懒加载事件
function loadMoreSjdw(){
    if(loadMoreSjdwFlag){
        pageNumber = pageNumber+1;
        $.ajax({
            url :"/wechat/hospital/pagedataListHospital",
            type : "POST",
            dataType : "json",
            data : {
                "searchParam" : $("#sjdwCxnr").val()?$("#sjdwCxnr").val():"",
                "sortName" : "yyxx.lrsj",
                "sortOrder" : "desc",
                "sortLastName" : "yyxx.lrsj",
                "sortLastOrder" : "asc",
                "pageNumber": pageNumber,
                "pageSize": pageSize,
                "dwFlag":"1"
            },
            success : function(data) {
                if(data.rows.length>0){
                    if (data.rows.length<pageSize){
                        loadMoreSjdwFlag = false
                    }
                    sjdwHtmlConcat(data.rows);
                }else {
                    loadMoreSjdwFlag = false
                    $("#sjdw_loading").hide()
                    $("#sjdw_noInfo").show();
                    $("#sjdw_loadComplete").hide();
                }
                loadingFlag = false;
            },
            error : function(data) {
                console.log(data)
                $.alert(data.message);
                loadingFlag = false;
            }
        })
    }
}
//3.5、送检单位html拼接
function sjdwHtmlConcat(list){
    var html = 	'';
    for (var i = 0; i < list.length; i++) {
        html += '  <a class="weui-cell" style="padding-bottom: 0;padding-top: 0;color: black" onclick="chooseSjdw(\''+list[i].dwid+'\',\''+list[i].dwmc+'\',\''+list[i].cskz1+'\')">' +
            '    <div class="weui-cell__bd">' +
            '      <p style="margin: 10px 0;">'+list[i].dwmc+'</p>' +
            '    </div>' +
            '  </a>'

    }
    $("#sjdw_loading").hide()
    $("#sjdw_noInfo").hide();
    $("#sjdw_loadComplete").show();
    $("#sjdwList").append(html)
    // $('.weui-cell_swiped').swipeout()
}
//3.6、送检单位选择事件
function chooseSjdw(dwid,dwmc,cskz1){
    sjdwBack();
    $("#sjdwid").val(dwid);
    $("#yymc").val(dwmc);
    //报告显示单位
	if(cskz1=="1"){
		$("#qtdwInput").show();
		$("#sjdwmc").val(dwmc);
	}else {
		$("#qtdwInput").hide();
		$("#sjdwmc").val();
	}
}

/**
 * 4、科室
 */
//4.0、科室
var ksxxList;
var xzksList;
var dqksid;
//4.1、科室数据处理
function dealKsData(){
    var isKsShow = false;
    //科室、初始化其他科室
    xzksList = [];
    for (var i = 0; i < ksxxList.length; i++) {
        var lsks = {label:ksxxList[i].dwmc,value:ksxxList[i].dwid};
        xzksList.push(lsks)
    }
    for (var i = 0; i < ksxxList.length; i++) {
        if(ksxxList[i].dwid == $("#ksid").val()){
            $("#ajaxForm #ksmc").val(ksxxList[i].dwmc)
            if (ksxxList[i].kzcs == '1'){
                isKsShow = true;
            }
            break;
        }
    }
    if(isKsShow){
        $("#qtksInput").show()
    }else{
        $("#qtksInput").hide()
        $("#qtksInput #qtks").val("")
    }
}
//4.2、科室选择事件
function chooseKs(flag){
    setTimeout(function(){
        weui.picker(
            xzksList, {
                defaultValue: [$("#ksid").val()?$("#ksid").val():xzksList[0].value],
                onConfirm: function (result) {
                    $("#ksmc").val(result[0].label);
                    $("#ksid").val(result[0].value);
                    for (var i = 0; i < ksxxList.length; i++) {
                        if(ksxxList[i].dwid == result[0].value){
                            if (ksxxList[i].kzcs == '1'){
                                $("#qtksInput").show();
                                    $("#qtks").val(result[0].label)
                            } else {
                                $("#qtksInput").hide()
                                $("#qtks").val("")
                            }
                        }
                    }
                },
                id: 'chooseKs',
                title: '请选择科室'
            });
    },200);
}

/**
 * 5、检测项目
 */
var fzjcxmDtos;
//5.0、检测项目
var fzjcxmList;
var dqfzjcxmid = "";
//5.1、检测项目数据处理
function dealFzjcxmData(){
    dqfzjcxmid = "";
    //检测项目、初始化检测项目
	var html='';
	for (var i = 0; i < fzjcxmList.length; i++) {
	    html += '<span class="RadioStyle">';
        if($("#jcxmids").val().indexOf(fzjcxmList[i].csid)!=-1){
            dqfzjcxmid += ","+fzjcxmList[i].csid
            html+='<input type="checkbox" id="'+fzjcxmList[i].csid+'" name="jcxm" value="'+fzjcxmList[i].csid+'" csmc="'+fzjcxmList[i].csmc+'" cskz1="'+fzjcxmList[i].cskz1+'"  cskz3="'+fzjcxmList[i].cskz3+'"  cskz4="'+fzjcxmList[i].cskz4+'" checked onclick=\'changeJcxm("'+fzjcxmList[i].csid+'");\'  validate="{required:true}"/>';
        }else{
            html+='<input type="checkbox" id="'+fzjcxmList[i].csid+'" name="jcxm" value="'+fzjcxmList[i].csid+'" csmc="'+fzjcxmList[i].csmc+'"  cskz1="'+fzjcxmList[i].cskz1+'"  cskz3="'+fzjcxmList[i].cskz3+'"  cskz4="'+fzjcxmList[i].cskz4+'" onclick=\'changeJcxm("'+fzjcxmList[i].csid+'");\'  validate="{required:true}"/>';
        }
        html +='<label for="'+fzjcxmList[i].csid+'" style="font-size: small;background-color: '+fzjcxmList[i].cskz2+'">'+fzjcxmList[i].csmc+'</label>';
        html +='</span>';
    }
	$("#jcxm").empty();
	$("#jcxm").append(html);
	dqfzjcxmid = dqfzjcxmid.substring(1);
	dealFzjczxmData();
}
//5.2、检测项目选择事件
function changeJcxm(jcxmid){
    var jcxmids='';
    $("#ajaxForm input[name='jcxm']").each(function(){
        var flag=$(this).prop("checked");
        if(flag){
            jcxmids += ","+$(this).val();
        }
    });
    if(jcxmids){
        jcxmids = jcxmids.substring(1);
    }
    $("#jcxmids").val(jcxmids)
    dealFzjcxmData();
}

/**
 * 6、检测子项目
 */
//6.0、检测子项目
var fzjczxmList;
//6.1、检测子项目数据处理
function dealFzjczxmData(){
    //检测项目、初始化检测项目
    var zxmHtml='';
    var isjczxmShow = false;
    var lsfzjczxmid = "";
    for (var i = 0; i < fzjczxmList.length; i++) {
        if($("#jcxmids").val().indexOf(fzjczxmList[i].fcsid)!=-1){
            isjczxmShow = true;
            zxmHtml += '<span class="RadioStyle">';
            if($("#jczxmids").val().indexOf(fzjczxmList[i].csid)!=-1){
                lsfzjczxmid += ","+fzjczxmList[i].csid;
                zxmHtml+='<input type="checkbox" id="'+fzjczxmList[i].csid+'" name="jczxm" fcsid="'+fzjczxmList[i].fcsid+'" value="'+fzjczxmList[i].csid+'"  checked  onclick=\'changeJczxm("'+fzjczxmList[i].csid+'");\' validate="{required:true}"/>';
            }else{
                zxmHtml+='<input type="checkbox" id="'+fzjczxmList[i].csid+'" name="jczxm" fcsid="'+fzjczxmList[i].fcsid+'" value="'+fzjczxmList[i].csid+'" onclick=\'changeJczxm("'+fzjczxmList[i].csid+'");\'  validate="{required:true}"/>';
            }
            zxmHtml +='<label for="'+fzjczxmList[i].csid+'" style="font-size: small;background-color: '+fzjczxmList[i].cskz2+'">'+fzjczxmList[i].csmc+'</label>';
            zxmHtml +='</span>';
        }
    }
    $("#jczxmids").val(lsfzjczxmid.substring(1));
    if(isjczxmShow){
        $("#jczxmDiv").show();
    }else{
        $("#jczxmDiv").hide();
    }
    $("#jczxm").empty();
    $("#jczxm").append(zxmHtml);
}
//6.2、检测子项目选择事件
function changeJczxm(jczxmid){
	var jczxmids='';
	$("#ajaxForm input[name='jczxm']").each(function(){
		var flag=$(this).prop("checked");
		if(flag){
			jczxmids += ","+$(this).val();
		}
	});
	if(jczxmids){
		jczxmids = jczxmids.substring(1);
	}
	$("#jczxmids").val(jczxmids);
}

/**
 * 7、样本类型
 */
//7.0、样本类型
var yblxList;
var xzyblxList;
var mryblx;
//7.1、处理样本类型数据
function dealYblxData(){
    //初始化所有样本类型、样本类型选择list
    xzyblxList = [];
    for (var i = 0; i < yblxList.length; i++) {
        var lsyblx = {label:yblxList[i].csmc,value:yblxList[i].csid};
        xzyblxList.push(lsyblx)
        if(yblxList[i].sfmr == '1'){
            mryblx = lsyblx;
        }
    }
    if(!mryblx){
        mryblx = {label:yblxList[0].csmc,value:yblxList[0].csid};
    }
    dealQtYblxData();
}
//7.2、处理其它样本类型数据
function dealQtYblxData(){
    var isQtyblxShow = false;
    if(!$("#yblx").val()){
        $("#xzyblxmc").val(mryblx.label);
        $("#yblx").val(mryblx.value);
    }
    if($("#yblx").val()){
        for (var i = 0; i < yblxList.length; i++) {
            if(yblxList[i].csid==$("#yblx").val()) {
                $("#xzyblxmc").val(yblxList[i].csmc)
                if(yblxList[i].cskz1 == '1'){
                    isQtyblxShow = true;
                    if(!$("#yblxmc").val()){
                        $("#yblxmc").val(yblxList[i].csmc);
                    }
                    break;
                }
            }
        }
    }
    if(isQtyblxShow){
        $("#qt").show();
    }else {
        $("#qt").hide();
    }
}
//7.3、样本类型改变事件
function changeYblx(){
    setTimeout(function(){
        weui.picker(
            xzyblxList, {
                defaultValue: [$("#yblx").val()?$("#yblx").val():xzyblxList[0].value],
                onConfirm: function (result) {
                    $("#xzyblxmc").val(result[0].label)
                    $("#yblx").val(result[0].value)
                    dealQtYblxData();
                },
                id: 'yblxchange',
                title: '请选择标本类型'
            });
    },200);
}

/**
 * 8、检测单位
 */
//8.0、检测单位
var jcdwList;
var xzjcdwList;
//8.1、检测单位数据处理
function dealJcdwData(){
    //检测单位
    xzjcdwList = [];
    var jcdwCountLimit = 2;
    for (var i = 0; i < jcdwList.length; i++) {
        if(!$("#jcdwid").val() && jcdwList[i].sfmr == '1'){
            $("#jcdwmc").val(jcdwList[i].csmc);
            $("#jcdwid").val(jcdwList[i].csid);
            jcdwCountLimit ++;
        }else if(jcdwList[i].csid == $("#jcdwid").val()){
            $("#jcdwmc").val(jcdwList[i].csmc)
            jcdwCountLimit ++;
        }
        if(jcdwCountLimit > 0){
            jcdwCountLimit --;
            var lsjcdw = {label:jcdwList[i].csmc,value:jcdwList[i].csid};
            xzjcdwList.push(lsjcdw)
        }
    }
}
//8.2、检测单位改变事件
function chooseJcdw(flag){
    setTimeout(function(){
        weui.picker(
            xzjcdwList, {
                defaultValue: [$("#jcdwid").val()?$("#jcdwid").val():xzjcdwList[0].value],
                onConfirm: function (result) {
                    $("#jcdwmc").val(result[0].label);
                    $("#jcdwid").val(result[0].value);
                },
                id: 'chooseJcdw',
                title: '请选择检测单位'
            });
    }, 200);
}


window.onload = function() {
    var list = new Array();
    for (var i = 50; i < document.all.length; i++) {
        if (document.all[i].type == "text" && document.all[i].style.display != "none")
            list.push(i);
    }

    for (var i = 0; i < list.length - 1; i++) {
        document.all[list[i]].setAttribute("nextFocusIndex", list[i + 1]);
        document.all[list[i]].onkeydown = JumpToNext;
    }
    for (var i = list.length - 1; i < document.all.length; i++) {
        if (document.all[i].type == "button") {
            document.all[list[list.length - 1]].setAttribute("nextFocusIndex", i);
            document.all[list[list.length - 1]].onkeydown = JumpToNext;
            break;
        }
    }
    document.all[list[0]].focus();
}

$(function(){
    navigationIndex += -1
    pushHistory()
    window.addEventListener("popstate", function(e) {
        goBack()
    }, false);
});

$(document.body).infinite().on("infinite", function() {
    if (loadingFlag){
        return;
    }
    loadingFlag = true
    if (pageFlag == "sjdwInfo"){
        loadMoreSjdw()
    }
});


function showloading(text){
	//loading显示
	$.showLoading(text);
}

function hideloading(){
	setTimeout(function() {
		//loading隐藏
		$.hideLoading();

	},200)
}


