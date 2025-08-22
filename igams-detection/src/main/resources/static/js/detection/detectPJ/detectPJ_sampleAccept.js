// var hasClick=false;//防止自动保存按enter重复提交
function updateBz(obj){
    var chk_value =[];//定义一个数组
    $('input[name="pjbbzts"]:checked').each(function(){//遍历每一个名字为bbzt的复选框，其中选中的执行函数
        chk_value.push($(this).val());//将选中的值添加到数组chk_value中
    });
    $("#ajaxForm #pjbbzts").val(chk_value);
}

$("#samplePjAcceptForm input").on('keydown',function(e){
    // if(e.keyCode != 13 && e.keyCode != 16&& e.keyCode != 17&& e.keyCode != 74&& e.keyCode != 37){
    //     return true;
    // }
    if(e.keyCode == 13){
        //查找基本信息
        getFzjcxx();
        //判断是否需要自动保存
        // autoSave();
    }
});
function Toast(msg, el, t) {
    var ele = document.querySelector(el);
    var Ospan = ele.querySelector('span')
    // console.log(ele,Ospan)
    if (typeof msg === 'string') {
        Ospan.innerHTML=msg;
        ele.style.display = 'block';
        setTimeout(()=>{
            ele.style.display = 'none';
        }, t ? t : 3000);
    } else {
        console.warn("param msg must be a String !");
    }
}
//参数n为休眠时间，单位为毫秒:
function sleep(n) {
    var start = new Date().getTime();
    //  console.log('休眠前：' + start);
    while (true) {
        if (new Date().getTime() - start > n) {
            break;
        }
    }
    // console.log('休眠后：' + new Date().getTime());
}
function autoSave(){
    //自动保存要判断必填项
    // var bcchecked=getRadioValue("bz");
    var bcchecked = $("input[name='bc']:checked").val();
    // let bcchecked = true;
    if ( bcchecked == true){
        var bcurl =  "/detectionPJ/detection/printLabel";
        var ybbh = $("#samplePjAcceptForm #ybbh").val();
        var nbbh = $("#samplePjAcceptForm #nbbh").val();
        var syh = $("#samplePjAcceptForm #syh").val();
        // var bbzt= $("input[name='bbzt']:checked").val();
        var glxx = $("#samplePjAcceptForm #glxx").val();
        var szz = $("input[name='szz']:checked").val();
        var sfjs = $("input[name='sfjs']:checked").val();
        var sfsy = $("input[name='sfsy']:checked").val();
        var jcdxcsdm = $("#samplePjAcceptForm #jcdxcsdm").val();
        var jcdw = $("#samplePjAcceptForm #jcdw").val();

        // var grsz_flg = $("#samplePjAcceptForm #grsz_flg").val();
        var grsz_flg;
        if (szz == $("#sampleAcceptForm #ysszz").val()) {
            grsz_flg = 0;
        } else {
            grsz_flg = 1;
        }
        if (ybbh == null || ybbh =="" || ybbh == undefined){
            Toast("标本编号不可为空！","#samplePjAcceptForm #toast");
        }
        if (jcdw == null || jcdw =="" || jcdw == undefined){
            Toast("请选选择检测单位！","#samplePjAcceptForm #toast");
        }
        if (glxx == null || glxx =="" || glxx == undefined){
            Toast("外机地址不可为空！","#samplePjAcceptForm #toast");
        }
        if (syh == null || syh =="" || syh == undefined){
            Toast("实验号不可为空！！","#samplePjAcceptForm #toast");
        }

        // hasClick = true;
        $.ajax({
            type: 'post',
            url: bcurl,
            data: {"nbbh": nbbh,"ybbh": ybbh,"glxx": glxx,"szz": szz,"syh": syh,"grsz_flg": grsz_flg,"bbzt": bbzt,"sfjs": sfjs,"sfsy":sfsy, "access_token": $("#ac_tk").val(),"jcdxcsdm":jcdxcsdm,"jcdw":jcdw},
            dataType: 'json',
            success: function (data) {
                if (data.status == 'success') {
                    preventResubmitForm(".modal-footer > button", false);
                    if (data.print && data.sz_flg) {
                        let ResponseSign = data.ResponseSign;
                        let type = data.type;
                        let RequestLocalCode = data.RequestLocalCode;
                        let nbbh = data.nbbh;
                        let ybbh = data.ybbh;
                        let syh = data.syh;
                        let sz_flg = data.sz_flg;
                        print_nbbh(sz_flg,ResponseSign,type,RequestLocalCode,nbbh,ybbh,syh);
                    }
                    Toast(data.message,"#samplePjAcceptForm #toast");
                    $("#samplePjAcceptForm #ybbh").val("");
                    $("#samplePjAcceptForm #syh").val("");
                    $("#samplePjAcceptForm #ybbh").focus();
                    // hasClick = false;
                } else if (data.status == "fail") {
                    preventResubmitForm(".modal-footer > button", false);
                    $.error(data.message, function () {
                    });
                    // hasClick = false;
                } else {
                    preventResubmitForm(".modal-footer > button", false);
                    $.alert("不好意思！出错了！");
                    // hasClick = false;
                }
            }
        });
    }
}
function getFzjcxx(){
    getFzjcxxDto();
}
//查找基本信息并给页面赋值
function getFzjcxxDto(){
    var url = "/detectionPJ/detection/pagedataGetSampleAcceptInfo";
    var ybbh = $("#samplePjAcceptForm #ybbh").val();
    $("#samplePjAcceptForm #xm").text("");
    $("#samplePjAcceptForm #xb").text("");
    $("#samplePjAcceptForm #nl").text("");
    $("#samplePjAcceptForm #cjryxm").text("");
    $("#samplePjAcceptForm #cjsj").text("");
    $("#samplePjAcceptForm #jcdw").val("");

    // hasClick = true;
    $.ajax({
        type : "POST",
        url : url,
        async:false,
        data : {"ybbh":ybbh,"access_token":$("#ac_tk").val()},
        dataType : "json",
        success : function(data){
            var fzjcxxDto = data.fzjcxxDtoInfo;
            if (fzjcxxDto == null){
                $.error("此标本不存在");
                $("#samplePjAcceptForm #ybbh").val("");
                $("#samplePjAcceptForm #nbbh").val("");
                $("#samplePjAcceptForm #syh").val("");
            }else{
                if(fzjcxxDto.xm){
                    $("#samplePjAcceptForm #xm").text(fzjcxxDto.xm);
                }
                if(fzjcxxDto.xb){
                    $("#samplePjAcceptForm #xb").text(fzjcxxDto.xb);
                }
                if(fzjcxxDto.nl){
                    $("#samplePjAcceptForm #nl").text(fzjcxxDto.nl);
                }
                if(fzjcxxDto.cjryxm){
                    $("#samplePjAcceptForm #cjryxm").text(fzjcxxDto.cjryxm);
                }
                if(fzjcxxDto.cjsj){
                    $("#samplePjAcceptForm #cjsj").text(fzjcxxDto.cjsj);
                }
                if(fzjcxxDto.nbbh){
                    $("#samplePjAcceptForm #nbbh").val(fzjcxxDto.nbbh);
                }
                if(fzjcxxDto.syh){
                    $("#samplePjAcceptForm #syh").val(fzjcxxDto.syh);
                }
                //标本状态先清除默认选项后更改选中
                $(":checkbox[name='pjbbzts']").prop("checked", false);
                for (var i=0; i<fzjcxxDto.pjbbzts.length; i++){
                    var bbztcsid = fzjcxxDto.pjbbzts[i];
                    $(":checkbox[name='pjbbzts'][value='" + bbztcsid + "']").prop("checked", "checked");
                }
                if(fzjcxxDto.jcdw){
                    $("#samplePjAcceptForm #jcdw").val(fzjcxxDto.jcdw);
                }
                if(fzjcxxDto.szz){
                    $("#samplePjAcceptForm #ysszz").text(fzjcxxDto.szz);
                }
                // if (fzjcxxDto.sfjs) {
                //     let sfjs = data.fzjcxxDtoInfo.sfjs;
                //     if (null != sfjs && '' != sfjs) {
                //         if (sfjs == '0') {
                //             document.getElementById("sfjs0").checked = "checked";
                //             document.getElementById("sfjs1").checked = "";
                //         } else {
                //             document.getElementById("sfjs0").checked = "";
                //             document.getElementById("sfjs1").checked = "checked";
                //         }
                //     }
                // }
            }
            // sleep(3000);
            // hasClick = false;
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            // 401一般都为两台电脑登录报错
            if (     "401"==(XMLHttpRequest.status)      ){
                $.error("系统账号被他人重复登录!请重登录");
            }else if(  "500"==(XMLHttpRequest.status)  ){
                $.error(XMLHttpRequest.status+" 内部服务器错误。");
            }else {
                $.error("状态码："+XMLHttpRequest.status+", readyState: "+XMLHttpRequest.readyState+", textStatus: "+textStatus+" 查询身份信息报错。");
            }
            // hasClick = false;
        }
    });
}

//打印机改变事件
// $("input[name=szz]").on("click", function(ev) {
//     var sz_flg = $("input[name='szz']:checked").val();
//     if (sz_flg == "0") {
//         $("#samplePjAcceptForm #glxx").attr("disabled", "disabled");
//         $("#samplePjAcceptForm #glxx").val("");
//     } else if (sz_flg == "1") {
//         $("#samplePjAcceptForm #glxx").removeAttr("disabled");
//     }
// });

/**
 * 选择本地则外机地址隐藏
 */
$("#samplePjAcceptForm #local_ip").click(function () {
    $("#samplePjAcceptForm #remoteDiv").hide();
    $("#samplePjAcceptForm #glxx").attr("disabled", "disabled");
})

/**
 *选择外机，则外机地址显示且必填
 */
$("#samplePjAcceptForm #remote_ip").click(function () {
    $("#samplePjAcceptForm #remoteDiv").show();
    $("#samplePjAcceptForm #glxx").removeAttr("disabled");
})

/**
 * 显示初始化时候根据选择的打印机是本机还是外机来判断是否展示外机地址
 */
function dyjChecked() {
    if ($("#samplePjAcceptForm #local_ip").attr("checked")) {
        $("#samplePjAcceptForm #remoteDiv").hide();
        $("#samplePjAcceptForm #glxx").attr("disabled", "disabled");
    } else if ($("#samplePjAcceptForm #remote_ip").attr("checked")) {
        $("#samplePjAcceptForm #remoteDiv").show();
        $("#samplePjAcceptForm #glxx").removeAttr("disabled");
    }
}

$(document).ready(function () {
    //所有下拉框添加choose样式
    jQuery('#samplePjAcceptForm .chosen-select').chosen({width: '100%'});

    dyjChecked()

    // var bbzt = $("#samplePjAcceptForm #bbzt1").val();
    // if (!bbzt){
    //     $("#samplePjAcceptForm #bbzt input:first").attr("checked",true);
    // }

    // smqsm()


    // $("#samplePjAcceptForm #kzbxh").val("0");
    // $("#samplePjAcceptForm #ls").val("1");
    // $("#samplePjAcceptForm #hs").val("1");
    // $("#samplePjAcceptForm #ybbh1-1").css("border","3px solid red");
    // var cz=$('input[name="cz"]:checked').val();
    // if(cz=='1'){
    //     $("#samplePjAcceptForm #kzbmc_sel").attr("disabled", "disabled");
    //     $("#samplePjAcceptForm #kzbmc_sel").hide();
    // }else{
    //     $("#samplePjAcceptForm #kzbmc").attr("disabled", "disabled");
    //     $("#samplePjAcceptForm #kzbmc").hide();
    // }

});
