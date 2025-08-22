/**
 * 页面初始化
 * @returns
 */
function initPage(){
    // 所有下拉框添加choose样式
    jQuery('#exportAccountForm .chosen-select').chosen({width: '100%'});
    //添加日期控件
    laydate.render({
        elem: '#exportAccountForm #jsrq'
        , theme: '#2381E9'
        , type: 'month'
        , format: 'yyyy-MM'
        , value: new Date()
    });
}

/**
 * 执行统计查询
 * @returns
 */
function selectData() {
    $.ajax({
        type : 'post',
        url : "/marketingInspection/marketingInspection/pagedataSelectQuery",
        cache : false,
        data : {
            "jsrq" : $("#exportAccountForm #jsrq").val(),
            "hbmc" : $("#exportAccountForm #hbmc").val(),
            "khmc" : $("#exportAccountForm #khmc").val(),
            "dzzq" : $("#exportAccountForm #dzzq option:selected").val(),
            "dzzqdm" : $("#exportAccountForm #dzzq option:selected").attr("csdm"),
            "access_token" : $("#ac_tk").val()
        },
        dataType : 'json',
        success : function(data) {
            // 返回值
            if (data["error"] != null) {
                $.error(data["error"]);
            } else {
                $("#exportAccountForm #nr").empty();
                var nrhtml = "";
                for (var i = 0; i < data.rows.length; i++) {
                    nrhtml = nrhtml + "<tr>";
                    var map = data.rows[i];
                    if(map.gsmc){
                        nrhtml = nrhtml + "<td title='" + map.gsmc + "' style='text-align: center'>" + map.gsmc + "</td>";
                    }else{
                        nrhtml = nrhtml + "<td title=''></td>";
                    }
                    if(map.db){
                        nrhtml = nrhtml + "<td title='" + map.db + "' style='text-align: center'>" + map.db + "</td>";
                    }else{
                        nrhtml = nrhtml + "<td title=''></td>";
                    }
                    if(map.fjsrq){
                        nrhtml = nrhtml + "<td title='" + map.fjsrq + "' style='text-align: center'>" + map.fjsrq + "</td>";
                    }else{
                        nrhtml = nrhtml + "<td title=''></td>";
                    }
                    if(map.ybbh){
                        nrhtml = nrhtml + "<td title='" + map.ybbh + "' style='text-align: center'>" + map.ybbh + "</td>";
                    }else{
                        nrhtml = nrhtml + "<td title=''></td>";
                    }
                    if(map.sjdwmc){
                        nrhtml = nrhtml + "<td title='" + map.sjdwmc + "' style='text-align: center'>" + map.sjdwmc + "</td>";
                    }else{
                        nrhtml = nrhtml + "<td title=''></td>";
                    }
                    if(map.dwmc){
                        nrhtml = nrhtml + "<td title='" + map.dwmc + "' style='text-align: center'>" + map.dwmc + "</td>";
                    }else{
                        nrhtml = nrhtml + "<td title=''></td>";
                    }
                    if(map.hzxm){
                        nrhtml = nrhtml + "<td title='" + map.hzxm + "' style='text-align: center'>" + map.hzxm + "</td>";
                    }else{
                        nrhtml = nrhtml + "<td title=''></td>";
                    }
                    if(map.yblxmc){
                        nrhtml = nrhtml + "<td title='" + map.yblxmc + "' style='text-align: center'>" + map.yblxmc + "</td>";
                    }else{
                        nrhtml = nrhtml + "<td title=''></td>";
                    }
                    if(map.jcxmmc){
                        nrhtml = nrhtml + "<td title='" + map.jcxmmc + "' style='text-align: center'>" + map.jcxmmc + "</td>";
                    }else{
                        nrhtml = nrhtml + "<td title=''></td>";
                    }
                    if(map.sfcss){
                        nrhtml = nrhtml + "<td title='" + map.sfcss + "' style='text-align: center'>" + map.sfcss + "</td>";
                    }else{
                        nrhtml = nrhtml + "<td title=''></td>";
                    }
                    if(map.sfkp){
                        nrhtml = nrhtml + "<td title='" + map.sfkp + "' style='text-align: center'>" + map.sfkp + "</td>";
                    }else{
                        nrhtml = nrhtml + "<td title=''></td>";
                    }
                    if(map.jcxmsfje){
                        nrhtml = nrhtml + "<td title='" + map.jcxmsfje + "' style='text-align: center'>" + map.jcxmsfje + "</td>";
                    }else{
                        nrhtml = nrhtml + "<td title=''></td>";
                    }
                    if(map.fkje){
                        nrhtml = nrhtml + "<td title='" + map.fkje + "' style='text-align: center'>" + map.fkje + "</td>";
                    }else{
                        nrhtml = nrhtml + "<td title=''></td>";
                    }
                    if(map.bz){
                        nrhtml = nrhtml + "<td title='" + map.bz + "' style='text-align: center'>" + map.bz + "</td>";
                    }else{
                        nrhtml = nrhtml + "<td title=''></td>";
                    }
                    if(map.kdh){
                        nrhtml = nrhtml + "<td title='" + map.kdh + "' style='text-align: center'>" + map.kdh + "</td>";
                    }else{
                        nrhtml = nrhtml + "<td title=''></td>";
                    }
                    if(map.kdfy){
                        nrhtml = nrhtml + "<td title='" + map.kdfy + "' style='text-align: center'>" + map.kdfy + "</td>";
                    }else{
                        nrhtml = nrhtml + "<td title=''></td>";
                    }
                    nrhtml = nrhtml + "</tr>";
                }
                $("#exportAccountForm #nr").append(nrhtml);
            }
        }
    });
}

$(function(){
    initPage();
})