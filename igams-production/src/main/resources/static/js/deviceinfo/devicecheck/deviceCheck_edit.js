$(document).ready(function() {
    //添加日期控件
    laydate.render({
        elem: ' #editDeviceCheckForm #ysrq'
        , theme: '#2381E9'
    });
    laydate.render({
        elem: ' #editDeviceCheckForm #gzsj'
        , theme: '#2381E9'
    });
    laydate.render({
        elem: ' #editDeviceCheckForm #qyrq'
        , theme: '#2381E9'
    });
    laydate.render({
        elem: ' #editDeviceCheckForm #xcbysj'
        , theme: '#2381E9'
    });
    laydate.render({
        elem: ' #editDeviceCheckForm #xcyzsj'
        , theme: '#2381E9'
    });
    laydate.render({
        elem: ' #editDeviceCheckForm #xcjlsj'
        , theme: '#2381E9'
    });
    laydate.render({
        elem: ' #editDeviceCheckForm #ccrq'
        , theme: '#2381E9'
    });
    var val = $("#editDeviceCheckForm #sfgdzc").val();
    if(val!='1'){
        $("#editDeviceCheckForm #gdzcbh").removeAttr("validate"); //取消form验证
        $("#editDeviceCheckForm #validate").hide();
    }
});
/**
 * 选择管理人列表
 * @returns
 */
function chooseGlry(){
    var url=$('#editDeviceCheckForm #urlPrefix').val() + "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
    $.showDialog(url,'选择管理员',addGlryConfig);
}
var addGlryConfig = {
    width		: "800px",
    modalName	:"addGlryModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    $('#editDeviceCheckForm #glry').val(sel_row[0].yhid);
                    $('#editDeviceCheckForm #glrymc').val(sel_row[0].zsxm);
                    //保存操作习惯
                    $.ajax({
                        type:'post',
                        url:$('#editDeviceCheckForm #urlPrefix').val() + "/common/habit/commonModUserHabit",
                        cache: false,
                        data: {"dxid":sel_row[0].yhid,"access_token":$("#ac_tk").val()},
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
/**
 * 选择验收人列表
 * @returns
 */
function chooseYsr(){
    var url=$('#editDeviceCheckForm #urlPrefix').val() + "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
    $.showDialog(url,'选择验收人',addYsrConfig);
}
var addYsrConfig = {
    width		: "800px",
    modalName	:"addYsrModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    $('#editDeviceCheckForm #ysr').val(sel_row[0].yhid);
                    $('#editDeviceCheckForm #ysrmc').val(sel_row[0].zsxm);
                    //保存操作习惯
                    $.ajax({
                        type:'post',
                        url:$('#editDeviceCheckForm #urlPrefix').val() + "/common/habit/commonModUserHabit",
                        cache: false,
                        data: {"dxid":sel_row[0].yhid,"access_token":$("#ac_tk").val()},
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
/**
 * 刷新验收单号
 */

function refreshSbysdh(){
    $.ajax({
        type:'post',
        url: $('#editDeviceCheckForm #urlPrefix').val()+'/device/device/pagedataRefreshDh',
        cache: false,
        data: {"access_token":$("#ac_tk").val()},
        dataType:'json',
        success:function(data){
            if(data != null && data.length != 0){
                if (data.sbysdh){
                    $("#editDeviceCheckForm #sbysdh").val(data.sbysdh);
                }
            }
        }
    });
}
//打开文件上传界面
function editfile(divName,btnName){
    $("#editDeviceCheckForm"+"  #"+btnName).hide();
    $("#editDeviceCheckForm"+"  #"+divName).show();
}
//关闭文件上传界面
function cancelfile(divName,btnName){
    $("#editDeviceCheckForm"+"  #"+btnName).show();
    $("#editDeviceCheckForm"+"  #"+divName).hide();
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
        var url=$("#editDeviceCheckForm #urlPrefix").val()+"/ws/sjxxpripreview?fjid="+fjid
        $.showDialog(url,'图片预览',JPGMaterConfig);
    }else if(type.toLowerCase()==".pdf"){
        var url= $("#editDeviceCheckForm #urlPrefix").val()+"/common/file/pdfPreview?fjid=" + fjid;
        $.showDialog(url,'文件预览',JPGMaterConfig);
    }else {
        $.alert("暂不支持其他文件的预览，敬请期待！");
    }
}
var JPGMaterConfig = {
    width		: "800px",
    offAtOnce	: true,
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
/**
 * 点击文件下载
 * @param fjid
 * @returns
 */
function xz(fjid){
    jQuery('<form action='+$("#editDeviceCheckForm #urlPrefix").val()+'"/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
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
            var url= $("#editDeviceCheckForm #urlPrefix").val()+"/common/file/delFile";
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

    if(!$("#editDeviceCheckForm #fjids").val()){
        $("#editDeviceCheckForm #fjids").val(fjid);
    }else{
        $("#editDeviceCheckForm #fjids").val($("#editDeviceCheckForm #fjids").val()+","+fjid);
    }
}
$(function(){
    // 所有下拉框添加choose样式
    jQuery('#editDeviceCheckForm .chosen-select').chosen({width: '100%'});
    //0.初始化fileinput
    var sign_params=[];
    sign_params.prefix=$('#editDeviceCheckForm #urlPrefix').val();
    var oFileInput = new FileInput();
    oFileInput.Init("editDeviceCheckForm","displayUpInfo",2,1,"sign_file",null,sign_params);
    var fjid=$("#editDeviceCheckForm #fjid").val();
    if(fjid!=null&&fjid!=""){
        $("#editDeviceCheckForm #ycfj").show();
        $("#editDeviceCheckForm #fjsc").hide();
    }else{
        $("#editDeviceCheckForm #ycfj").hide();
        $("#editDeviceCheckForm #fjsc").show();
    }
});