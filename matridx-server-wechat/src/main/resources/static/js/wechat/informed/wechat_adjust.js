var unitlist;
var xzunitList = [];

/**
 * 检测单位改变事件
 * @param flag
 */
function chooseJcdw(syglid,sfjs){
    if (sfjs){
        $.alert("已经接收，不能修改检测单位！");
        return;
    }
    let defaultValue = $("#"+syglid+"_id").val();
    weui.picker(
        xzunitList, {
            id:syglid,
            defaultValue: [defaultValue],
            onConfirm: function (result) {
                $("#"+syglid+"_mc").text(result[0].label);
                $("#"+syglid+"_mc").append("&nbsp;&nbsp;&nbsp;&nbsp;<span class='glyphicon glyphicon-hand-left'></span>")
                $("#"+syglid+"_id").val(result[0].value);
            },
            title: '请选择检测单位'
        });
}

/**
 * 获取基础数据
 */
function getBasicData(){
    $.ajax({
        url:'/wechat/getInspBasicData',
        type:'post',
        dataType:'json',
        data:{
            "dbm" : $("#db").val()
        },
        success:function(data){
            unitlist = data.unitlist;
            //检测单位
            if (unitlist.length > 0){
                for (var i = 0; i < unitlist.length; i++) {
                    var lsuniy = {label:unitlist[i].csmc,value:unitlist[i].csid};
                    xzunitList.push(lsuniy)
                }
            }
        },
        error : function(data) {
            $.toptip(JSON.stringify(date), 'error');
        }
    })
}

/**
 * 页面初始化
 */
function initPage(){
    //初始化基础数据
    getBasicData()
}

function saveSjsy(){
    var ids = $("#ids").val();
    var split = ids.split(",");
    var json=[];
    for(var i=0;i<split.length;i++){
        var sz = {"syglid":'',"jcdw":''};
        sz.syglid = split[i];
        sz.jcdw = $("#"+split[i]+"_id").val();
        json.push(sz);
    }
    var jcdwChange = isJcdwChange(json);
    if (!jcdwChange){
        window.history.go(-1);
        return;
    }else {
        $.ajax({
            type:'post',
            url:"/wechat/miniprogram/adjustSave",
            cache: false,
            data: {
                "sy_json":JSON.stringify(json),
                "wxid":$("#wxid").val(),
                "sjid":$("#sjid").val(),
            },
            dataType:'json',
            success:function(result){
                if(result.status == "success"){
                    $.alert("保存成功！",function () {
                        // window.location.replace('/wechat/inspPerfectReport?wxid=' + $("#wxid").val()+'&flag=geneDtetection');
                        window.history.go(-1);
                    });
                }else{
                    $.alert("保存失败！",function () {
                        // window.location.replace('/wechat/inspPerfectReport?wxid=' + $("#wxid").val()+'&flag=geneDtetection');
                        window.history.go(-1);
                    });
                }
            },
            error: function (data) {
                buttonDisabled(false,true,JSON.stringify(data),false);
            }

        })
    }
}

$(document).ready(function(){
    //初始化页面数据
    initPage();
    $(".jcdw").each(function(){
        $(this).append(" <img src='/images/inspectionServices/update.png'>")
    })
    getInitInfo()
});

//实验管理检测单位初始信息
var infoList = [];
//获取实验管理检测单位初始信息
function getInitInfo(){
    var ids = $("#ids").val();
    if (ids){
        var idList = ids.split(",");
        for (var i = 0; i < idList.length; i++) {
            var id = idList[i];
            var info = {"syglid":idList[i],"jcdw":$("#"+idList[i]+"_id").val()};
            infoList.push(info);
        }
    }
}

//判断实验管理信息是否改变
function isJcdwChange(json){
    //判读json和infoList是否相同
    var flag = false;
    if (json.length != infoList.length){
        flag = true;
        return flag;
    }
    for (var i = 0; i < json.length; i++) {
        var syglid = json[i].syglid;
        var jcdw = json[i].jcdw;
        for (var j = 0; j < infoList.length; j++) {
            if (syglid == infoList[j].syglid){
                if (jcdw != infoList[j].jcdw){
                    flag = true;
                    return flag
                }
            }
        }
    }
    return flag;
}