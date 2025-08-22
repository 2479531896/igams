/**
 * 刷新合同编号
 * @returns
 */
function refreshHtbh(){
    var htlx = $("#editMarketingContractForm #htlx").val();
    var htjbbm = $("#editMarketingContractForm #htjbbm").val();
    if(htlx&&htjbbm){
        $.ajax({
            type:'post',
            url:$('#editMarketingContractForm #urlPrefix').val() + "/marketingContract/marketingContract/pagedataGetMarketingContractCode",
            cache: false,
            data: {"htlx":htlx,"htjbbm":htjbbm,"access_token":$("#ac_tk").val()},
            dataType:'json',
            success:function(result){
              if (result.status=='success'){
                  $("#editMarketingContractForm #htbh").val(result.message);
              }else {
                  $.alert(result.message);
              }
            }
        });
    }else{
        $.alert("请先选择合同类型和合同经办部门！");
    }
}
/**
 * 选择所属业务员列表
 * @returns
 */
function chooseSsywy(){
    var url=$('#editMarketingContractForm #urlPrefix').val() + "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
    $.showDialog(url,'选择负责人',addSsywyConfig);
}
var addSsywyConfig = {
    width		: "800px",
    modalName	:"addSsywyModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    $('#editMarketingContractForm #ssywy').val(sel_row[0].yhid);
                    $('#editMarketingContractForm #ssywymc').val(sel_row[0].zsxm);
                    //保存操作习惯
                    $.ajax({
                        type:'post',
                        url:$('#editMarketingContractForm #urlPrefix').val() + "/common/habit/commonModUserHabit",
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
 * 选择合同经办人列表
 * @returns
 */
function chooseHtjbr(){
    var url=$('#editMarketingContractForm #urlPrefix').val() + "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
    $.showDialog(url,'选择合同经办人',addHtjbrConfig);
}
var addHtjbrConfig = {
    width		: "800px",
    modalName	:"addHtjbrModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    $('#editMarketingContractForm #htjbr').val(sel_row[0].yhid);
                    $('#editMarketingContractForm #htjbrmc').val(sel_row[0].zsxm);
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
 * 选择直属领导列表
 * @returns
 */
function chooseZsld(){
    var url=$('#editMarketingContractForm #urlPrefix').val() + "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
    $.showDialog(url,'选择直属领导',addZsldConfig);
}
var addZsldConfig = {
    width		: "800px",
    modalName	:"addZsldModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var xzrys = $('#taskListFzrForm #xzrys').val();
                if(xzrys!=null&&xzrys!=""){
                    var sel_row = JSON.parse(xzrys);
                    var ids="";
                    var mcs="";
                    for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                        ids = ids + ","+ sel_row[i].yhid;
                        mcs = mcs + ","+ sel_row[i].zsxm;
                    }
                    ids = ids.substr(1);
                    mcs = mcs.substr(1);
                    $('#editMarketingContractForm #zsld').val(ids);
                    $('#editMarketingContractForm #zsldmc').val(mcs);
                }else{
                    $.error("请至少选中一行");
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
 * 验证金额格式(两位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("jeNumber", function (value, element){
   if(!/^\d+(\.\d{1,2})?$/.test(value)){
       $.error("请填写正确合同金额格式，可保留两位小数!");
   }
    return this.optional(element) || /^\d+(\.\d{1,2})?$/.test(value);
},"请填写正确格式，可保留两位小数。");

/**
 * 验证数量格式(两位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("slNumber", function (value, element){
    if(!/^\d+(\.\d{0})?$/.test(value)){
        $.error("请填写正确合同数量格式，整数!");
    }
    return this.optional(element) || /^\d+(\.\d{0})?$/.test(value);
},"请填写正确格式，整数！");
/**
 * 选择客户列表
 * @returns
 */
function chooseKh(){
    var url=$('#editMarketingContractForm #urlPrefix').val() + "/systemmain/client/pagedataListClient?access_token=" + $("#ac_tk").val();
    $.showDialog(url,'选择客户',addKhConfig);
}
var addKhConfig = {
    width		: "800px",
    modalName	:"addKhModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var sel_row = $('#client_list_ajaxForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    $('#editMarketingContractForm #khid').val(sel_row[0].khid);
                    $('#editMarketingContractForm #khjc').val(sel_row[0].khjc);
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
function chooseHtjbbm() {
    var url = $('#editMarketingContractForm #urlPrefix').val() + "/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
    $.showDialog(url, '选择合同经办部门', chooseHtjbbmConfig);
}
var chooseHtjbbmConfig = {
    width : "800px",
    height : "500px",
    modalName	: "chooseHtjbbmModal",
    formName	: "editMarketingContractForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#listDepartmentForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var sel_row = $('#listDepartmentForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    $('#editMarketingContractForm #htjbbm').val(sel_row[0].jgid);
                    $('#editMarketingContractForm #htjbbmmc').val(sel_row[0].jgmc);
                    $.closeModal(opts.modalName);
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
//打开文件上传界面
function editfile(divName,btnName){
    $("#editMarketingContractForm"+"  #"+btnName).hide();
    $("#editMarketingContractForm"+"  #"+divName).show();
}
//关闭文件上传界面
function cancelfile(divName,btnName){
    $("#editMarketingContractForm"+"  #"+btnName).show();
    $("#editMarketingContractForm"+"  #"+divName).hide();
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
        var url=$("#editMarketingContractForm #urlPrefix").val()+"/ws/sjxxpripreview?fjid="+fjid
        $.showDialog(url,'图片预览',JPGMaterConfig);
    }else if(type.toLowerCase()==".pdf"){
        var url= $("#editMarketingContractForm #urlPrefix").val()+"/common/file/pdfPreview?fjid=" + fjid;
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
    jQuery('<form action='+$("#editMarketingContractForm #urlPrefix").val()+'"/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
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
            var url= $("#editMarketingContractForm #urlPrefix").val()+"/common/file/delFile";
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

    if(!$("#editMarketingContractForm #fjids").val()){
        $("#editMarketingContractForm #fjids").val(fjid);
    }else{
        $("#editMarketingContractForm #fjids").val($("#editMarketingContractForm #fjids").val()+","+fjid);
    }
}
$(function(){

    //0.初始化fileinput
    var sign_params=[];
    sign_params.prefix=$('#editMarketingContractForm #urlPrefix').val();
    var oFileInput = new FileInput();
    oFileInput.Init("editMarketingContractForm","displayUpInfo",2,1,"sign_file",null,sign_params);
    //所有下拉框添加choose样式
    jQuery('#editMarketingContractForm .chosen-select').chosen({width: '100%'});
    var fjid=$("#editMarketingContractForm #fjid").val();
    if(fjid!=null&&fjid!=""){
        $("#editMarketingContractForm #ycfj").show();
        $("#editMarketingContractForm #fjsc").hide();
    }else{
        $("#editMarketingContractForm #ycfj").hide();
        $("#editMarketingContractForm #fjsc").show();
    }
    //添加日期控件
    laydate.render({
        elem: '#editMarketingContractForm #htrq'
        ,theme: '#2381E9'
        ,trigger: 'click'
    });
    laydate.render({
        elem: '#editMarketingContractForm #dqrq'
        ,theme: '#2381E9'
        ,trigger: 'click'
    });

    var dqrqflg = $("#editMarketingContractForm [name='dqrqflg']");
    if(dqrqflg[0].checked){
        $("#editMarketingContractForm #dqrqdiv").addClass("hidden").find("input").prop("disabled",true);
        $("#editMarketingContractForm #qtdiv").removeClass("hidden").find("input").prop("disabled",false);
    }else{
        $("#editMarketingContractForm #dqrqdiv").removeClass("hidden").find("input").prop("disabled",false);
        $("#editMarketingContractForm #qtdiv").addClass("hidden").find("input").prop("disabled",true);
    }

    dqrqflg.unbind("click").click(function(){
        if(dqrqflg[0].checked){
            $("#editMarketingContractForm #dqrqdiv").addClass("hidden").find("input").prop("disabled",true);
            $("#editMarketingContractForm #qtdiv").removeClass("hidden").find("input").prop("disabled",false);
        }else{
            $("#editMarketingContractForm #dqrqdiv").removeClass("hidden").find("input").prop("disabled",false);
            $("#editMarketingContractForm #qtdiv").addClass("hidden").find("input").prop("disabled",true);
        }
    });

});