function getJg(){
    var hzid = $("#recruit_ajaxForm #fqr").val();
    $.ajax({
        url: "/recruit/recruit/pagedataJgxx",
        type: "post",
        dataType: 'json',
        data: { fqr: hzid, access_token: $("#ac_tk").val() },
        success: function(data) {
            $("#recruit_ajaxForm #fqrbm").val(data.rszpDto[0].jgmc);
        },
        error: function () {
            $.alert('未找到所属机构！');
        }
    })
}

var recruitEdit_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};
    oInit.Init = function () {
        //添加日期控件
        laydate.render({
            elem: '#recruit_ajaxForm #xwdgrq'
            ,theme: '#2381E9'
        });
        laydate.render({
            elem: '#recruit_ajaxForm #fqsj'
            , theme: '#2381E9'
            , type: 'datetime'
            , format: 'yyyy-MM-dd HH:mm:ss'//保留时分
        });
    };

    return oInit;
};

function btnBind(){

}

function initPage(){

}

$(document).ready(function(){
    //绑定事件
    btnBind();
    //初始化页面数据
    initPage();
    //所有下拉框添加choose样式
    //2.初始化Button的点击事件
    var oButtonInit = new recruitEdit_ButtonInit();
    oButtonInit.Init();
    jQuery('#recruit_ajaxForm .chosen-select').chosen({width: '90%'});
});