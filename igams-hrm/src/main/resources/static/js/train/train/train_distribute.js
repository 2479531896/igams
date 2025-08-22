function chooseWxyh() {
    var url = $("#train_ajaxForm #urlPrefix").val()+"/train/user/pagedataListUserForChoose?access_token=" + $("#ac_tk").val()+"&bm="+$("#train_ajaxForm #bm").val();
    $.showDialog(url, '选择负责人', chooseYhConfig);
}
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
                if(!$("#userListForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var sel = $("#userListForm #yxyh").tagsinput('items');
                $("#train_ajaxForm  #fzr").tagsinput({
                    itemValue: "value",
                    itemText: "text",
                });
                for(var i = 0; i < sel.length; i++){
                    var value = sel[i].value;
                    var text = sel[i].text;
                    $("#train_ajaxForm  #fzr").tagsinput('add',{"value":value,"text":text});
                }
                $.closeModal(opts.modalName);
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var train_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};
    oInit.Init = function () {
        //添加日期控件
        laydate.render({
            elem: '#train_ajaxForm #qwwcsj'
            , theme: '#2381E9'
            , format: 'yyyy-MM-dd'//保留时分
        });
    };

    return oInit;
};

function btnBind(){

}

function initPage(){

}
//打开文件上传界面
function editfile(divName,btnName){
    $("#train_ajaxForm"+"  #"+btnName).hide();
    $("#train_ajaxForm"+"  #"+divName).show();
}
//关闭文件上传界面
function cancelfile(divName,btnName){
    $("#train_ajaxForm"+"  #"+btnName).show();
    $("#train_ajaxForm"+"  #"+divName).hide();
}
/**
 * 点击图片预览
 * @param fjid
 * @param wjm
 * @returns
 */
function view(fjid,wjm){
    var begin=wjm.lastIndexOf(".");
    var end=wjm.length;
    var type=wjm.substring(begin,end);
    if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
        var url=$("#sampleSummaryForm #urlPrefix").val()+"/ws/sjxxpripreview?fjid="+fjid
        $.showDialog(url,'图片预览',JPGMaterConfig);
    }else if(type.toLowerCase()==".pdf"){
        var url= $("#sampleSummaryForm #urlPrefix").val()+"/common/file/pdfPreview?fjid=" + fjid;
        $.showDialog(url,'文件预览',JPGMaterConfig);
    }else {
        $.alert("暂不支持其他文件的预览，敬请期待！");
    }
}
var JPGMaterConfig = {
    offAtOnce	: true,
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

//下载模板
function xz(fjid){
    jQuery('<form action="'+$("#sampleSummaryForm #urlPrefix").val()+'/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
        '<input type="text" name="fjid" value="'+fjid+'"/>' +
        '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
        '</form>')
        .appendTo('body').submit().remove();
}
/**
 * 点击删除文件
 * @param fjid
 * @param wjlj
 * @returns
 */
function del(fjid,wjlj){
    $.confirm('您确定要删除所选择的记录吗？',function(result){
        if(result){
            jQuery.ajaxSetup({async:false});
            var url= $("#train_ajaxForm #urlPrefix").val()+"/common/file/delFile";
            jQuery.post(url,{fjid:fjid,wjlj:wjlj,"access_token":$("#ac_tk").val()},function(responseText){
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


function displayUpInfo(fjid){
    if(!$("#train_ajaxForm #fjids").val()){
        $("#train_ajaxForm #fjids").val(fjid);
    }else{
        $("#train_ajaxForm #fjids").val($("#train_ajaxForm #fjids").val()+","+fjid);
    }
    $.ajax({
        type: "post",
        url: $('#train_ajaxForm #urlPrefix').val() + '/common/file/listImpInfo',
        data: {"fjid":fjid,"ywlx":$('#train_ajaxForm #ywlx').val(),"access_token":$("#ac_tk").val()},
        dataType: "json",
        success: function (data) {
            var yhmorzsxms_str = '';
            for (var i = 0; i < data.rows.length; i++) {
                yhmorzsxms_str = yhmorzsxms_str + ","+data.rows[i].yhmorzsxm;
            }
           if (yhmorzsxms_str.length>0){
               yhmorzsxms_str = yhmorzsxms_str.substring(1);
           }
            $.ajax({
                type: "post",
                url: $('#train_ajaxForm #urlPrefix').val() + '/train/user/pagedataGetXtyhLitByYhms',
                data: {"yhmorzsxms_str": yhmorzsxms_str,"access_token":$("#ac_tk").val()},
                dataType: "json",
                success: function (data) {
                    if (!data){
                        $.error("导入失败！没有数据或格式错误！")
                    }
                    $("#train_ajaxForm  #fzr").tagsinput({
                        itemValue: "value",
                        itemText: "text",
                    });
                    for(var i = 0; i < data.xtyhDtos.length; i++){
                        var value = data.xtyhDtos[i].yhid;
                        var text = data.xtyhDtos[i].zsxm;
                        $("#train_ajaxForm  #fzr").tagsinput('add',{"value":value,"text":text});
                    }
                },
                error: function () {
                    $.error("导入失败！没有数据或格式错误！")
                }
            });
        }
    })
}
var reportImp_ButtonInit = function(){
    var oInits = new Object();
    var formname ="#train_ajaxForm";
    oInits.Init = function () {
        //下载模板按钮
        $(formname + " #template_wt").unbind("click").click(function(){
            $("#updateDisForm #access_token").val($("#ac_tk").val());
            $("#updateDisForm").submit();
        });
    }
    return oInits;
}
$(document).ready(function(){
    //绑定事件
    btnBind();
    //初始化页面数据
    initPage();
    //所有下拉框添加choose样式
    //2.初始化Button的点击事件
    var oButtonInit = new train_ButtonInit();
    oButtonInit.Init();
    jQuery('#train_ajaxForm .chosen-select').chosen({width: '88%'});
    //0.初始化fileinput
    var sign_params=[];
    sign_params.prefix=$('#train_ajaxForm #urlPrefix').val();
    var oFileInput = new FileInput();
    oFileInput.Init("train_ajaxForm","displayUpInfo",2,1,"sign_file",null,sign_params);
    var fjid=$("#train_ajaxForm #fjid").val();
    if(fjid!=null&&fjid!=""){
        $("#train_ajaxForm #ycfj").show();
        $("#train_ajaxForm #fjsc").hide();
    }else{
        $("#train_ajaxForm #ycfj").hide();
        $("#train_ajaxForm #fjsc").show();
    }
    var oimpButtonInit = new reportImp_ButtonInit();
    oimpButtonInit.Init();
});