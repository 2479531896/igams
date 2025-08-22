
//信息显示配置
var showConfig = function(errormsg,infomsg,confirmmsg,warnmsg,successmsg,updList){
    var formname = "#pcrsangerDealForm";
    $(formname + " #errordiv").addClass("hidden");
    $(formname + " #infodiv").addClass("hidden");
    $(formname + " #confirmdiv").addClass("hidden");
    $(formname + " #warndiv").addClass("hidden");
    $(formname + " #btn-cancel").addClass("hidden");
    $(formname + " #btn-complete").addClass("hidden");
    if(errormsg){
        $(formname + " #errormsg").html(errormsg);
        $(formname + " #errordiv").removeClass("hidden");
        $(formname + " #btn-complete").removeClass("hidden");
        return ;
    }else{
        var existsMsg = false;
        if(infomsg){
            $(formname + " #infomsg").html(infomsg);
            $(formname + " #infodiv").removeClass("hidden");
            $(formname + " #btn-complete").addClass("hidden");
            existsMsg = true;
        }
        if(confirmmsg){
            $(formname + " #confirmmsg").html(confirmmsg);
            $(formname + " #confirmdiv").removeClass("hidden");
            $(formname + " #btn-cancel").removeClass("hidden");
            existsMsg = true;
        }
        if(warnmsg){
            $(formname + " #warnmsg").html(warnmsg);
            $(formname + " #warndiv").removeClass("hidden");
            $(formname + " #btn-cancel").removeClass("hidden");
            existsMsg = true;
        }
        if(successmsg){
            $(formname + " #infomsg").html(successmsg);
            $(formname + " #infodiv").removeClass("hidden");
            $(formname + " #btn-complete").removeClass("hidden");
            existsMsg = true;
        }
        if (!existsMsg) {
            $(formname + " #infomsg").html("处理已完成，请点击“下载”按钮进行下载");
            $(formname + " #infodiv").removeClass("hidden");
            $(formname + " #btn-cancel").removeClass("hidden");
            $(formname + " #btn-complete").removeClass("hidden");
        }
    }
}

//上传后自动提交服务器检查进度
var checkImpCheckStatus = function(intervalTime,fjid){
    var formname = "#pcrsangerDealForm";
    $.ajax({
        type : "POST",
        url : "/systemmain/query/pagedataPCRSangerDeal",
        data : {"fjid":fjid,"ywlx":$("#pcrsangerDealForm #ywlx").val(),"access_token":$("#ac_tk").val()},
        dataType : "json",
        success:function(data){
            if(data.status=="success"){
                showConfig(data.infomsg);
                $(formname+" #wjlj").val(data.wjlj)
            }else{
                if($(formname +" #infomsg")){$(formname +" #infomsg").html(data.message)}
            }
        }
    });
};

var reportImp_ButtonInit = function(){
    var oInits = new Object();
    var formname ="#pcrsangerDealForm";
    oInits.Init = function () {
        //取消按钮
        $(formname + " #btn-cancel").unbind("click").click(function(){
            $(formname + " #imp_file")
                .fileinput('clear')
                .fileinput('unlock');
            $(formname + " #imp_file")
                .parent()
                .siblings('.fileinput-remove')
                .hide();
            $(formname + " #errordiv").addClass("hidden");
            $(formname + " #infodiv").addClass("hidden");
            $(formname + " #confirmdiv").addClass("hidden");
            $(formname + " #warndiv").addClass("hidden");
            $(formname + " #btn-cancel").addClass("hidden");
            $(formname + " #btn-complete").addClass("hidden");
        });

        //完成
        $(formname + " #btn-complete").unbind("click").click(function(){
            jQuery('<form action="/common/file/downloadFileByFilePath" method="POST">' +  // action请求路径及推送方法
                        '<input type="text" name="filePath" value="'+ $(formname+" #wjlj").val()+'"/>' +
                        '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
                    '</form>')
                .appendTo('body').submit().remove();
            $(formname + " #errordiv").addClass("hidden");
            $(formname + " #infodiv").addClass("hidden");
            $(formname + " #confirmdiv").addClass("hidden");
            $(formname + " #warndiv").addClass("hidden");
            $(formname + " #btn-complete").addClass("hidden");
        });
    }
    return oInits;
}

function displayUpInfo(fjid){
    $("#pcrsangerDealForm #fjid").val(fjid);
    //1.初始化Table
    setTimeout("checkImpCheckStatus(2000,'"+ fjid + "')",1000);
}

$(function () {
    //0.初始化fileinput,这是公用方法，调用comdefine.js里的方法
    var oFileInput = new FileInput();
    //ctrlName,callback,impflg,singleFlg,fileName,importType
    //参数说明 singleFlg 好像是标明 是否单文件上传限制，有点忘记了
    //impflg 用于后台判断是否为导入。1为导入，则开启线程进行导入  2：代表附件保存 3固定表头
    var document_params=[];
    document_params.prefix=$('#pcrsangerDealForm #urlPrefix').val();
    oFileInput.Init("pcrsangerDealForm","displayUpInfo",1,1,"imp_file","",document_params);
    //2.初始化Button的点击事件
    var oimpButtonInit = new reportImp_ButtonInit();
    oimpButtonInit.Init();
});