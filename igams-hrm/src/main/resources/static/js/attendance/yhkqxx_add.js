function dateDifference_qj(){
    var faultDate=$("#Yhkdxx_ajaxForm #qjkssj").val();
    var completeTime=$("#Yhkdxx_ajaxForm #qjjssj").val();
    if(faultDate!=null&&faultDate!=""&&completeTime!=null&&completeTime!="") {
        var stime = new Date(faultDate).getTime();
        var etime = new Date(completeTime).getTime();
        var usedTime = etime - stime;  //两个时间戳相差的毫秒数
        var hour = usedTime / (3600 * 1000);
        document.getElementById("qjsc").value=hour+"小时";
        if($("#Yhkdxx_ajaxForm #gzsc").val()!=null&&$("#Yhkdxx_ajaxForm #gzsc").val()!="") {
            document.getElementById("gzsc").value = (($("#Yhkdxx_ajaxForm #gzsc").val().replace("小时", "")) * 1 - hour * 1) + "小时";
        }
    }
}
function dateDifference_jb(){
    var faultDate=$("#Yhkdxx_ajaxForm #jbkssj").val();
    var completeTime=$("#Yhkdxx_ajaxForm #jbjssj").val();
    if(faultDate!=null&&faultDate!=""&&completeTime!=null&&completeTime!=""){
        var stime =new Date(faultDate).getTime();
        var etime = new Date(completeTime).getTime();
        var usedTime = etime - stime;  //两个时间戳相差的毫秒数
        var hour=usedTime/(3600*1000);
        document.getElementById("sc").value=hour+"小时";
        if($("#Yhkdxx_ajaxForm #gzsc").val()!=null&&$("#Yhkdxx_ajaxForm #gzsc").val()!=""){
            document.getElementById("gzsc").value=(($("#Yhkdxx_ajaxForm #gzsc").val().replace("小时",""))*1+hour*1)+"小时";
        }
    }
}
function dateDifference_gz(){
    var faultDate=$("#Yhkdxx_ajaxForm #cqsj").val();
    var completeTime=$("#Yhkdxx_ajaxForm #tqsj").val();
    if(faultDate!=null&&faultDate!=""&&completeTime!=null&&completeTime!="") {
        document.getElementById("gzsc").value = 8.0 + "小时";
    }
}
var attendanceAdd_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};
    oInit.Init = function () {
        //添加日期控件
        laydate.render({
            elem: '#Yhkdxx_ajaxForm #dkrq'
            ,theme: '#2381E9'
        });
        laydate.render({
            elem: '#Yhkdxx_ajaxForm #cqsj'
            , theme: '#2381E9'
            , type: 'datetime'
            , format: 'yyyy-MM-dd HH:mm:ss'//保留时分
        });
        laydate.render({
            elem: '#Yhkdxx_ajaxForm #tqsj'
            , theme: '#2381E9'
            , type: 'datetime'
            , format: 'yyyy-MM-dd HH:mm:ss'//保留时分
        });
        laydate.render({
            elem: '#Yhkdxx_ajaxForm #qjkssj'
            , theme: '#2381E9'
            , type: 'datetime'
            , format: 'yyyy-MM-dd HH:mm:ss'//保留时分
        });
        laydate.render({
            elem: '#Yhkdxx_ajaxForm #qjjssj'
            , theme: '#2381E9'
            , type: 'datetime'
            , format: 'yyyy-MM-dd HH:mm:ss'//保留时分
        });
        laydate.render({
            elem: '#Yhkdxx_ajaxForm #jbkssj'
            , theme: '#2381E9'
            , type: 'datetime'
            , format: 'yyyy-MM-dd HH:mm:ss'//保留时分
        });
        laydate.render({
            elem: '#Yhkdxx_ajaxForm #jbjssj'
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
    var oButtonInit = new attendanceAdd_ButtonInit();
    oButtonInit.Init();
    jQuery('#Yhkdxx_ajaxForm .chosen-select').chosen({width: '100%'});
});