/**
 * 返回上一页
 * @returns
 */
$("#weeklyStatis_jsrq #weeklyStatis_jsrq #rollbackweekly").bind("click",function(){
    $("#weeklyStatis_jsrq").load("/wechat/statistics/pageListLocal_weekly_jsrq");
});
function loadAttainmentRate(){
    //加载数据
    var _url = "/ws/statistics/getSalesAttainmentRateByAreaAndJsrq";
    var param ={
        qymc:$("#weeklyStatis_jsrq #attainmentrate #qymc").val(),
        zblxcsmc:$("#weeklyStatis_jsrq #attainmentrate #zblx").val(),
        yhid:$("#weeklyStatis_jsrq #attainmentrate #yhid").val(),
        kszq:$("#weeklyStatis_jsrq #attainmentrate #searchkszq").val(),
        jszq:$("#weeklyStatis_jsrq #attainmentrate #searchjszq").val(),
        qyid:$("#weeklyStatis_jsrq #attainmentrate #qyid").val()
    };
    $.ajax({
        type : "post",
        url : _url,
        data:param,
        dataType : "json",
        success : function(datas) {
            if(datas){
                var salesAttainmentRate = datas['salesAttainmentRate'];
                var zblxmc = $("#weeklyStatis_jsrq #zblxmc").val();
                $("#weeklyStatis_jsrq #tatistics_attainmentrate_area_jsrq").html("");
                if (!(salesAttainmentRate != null && salesAttainmentRate.length > 0)) {
                    return;
                }
                var table = "<div  class='padding_t7' style='float:left;padding-left: 5px;padding-right: 5px;width:100%'><table id='tabAttainmentRate'>";
                table += "<thead> <tr>";
                table += "<th class='other_td'>姓名</th>";
                table += "<th class='other_td'>销售指标</th>";
                table += "<th class='other_td'>达成率</th>";
                table += "<th class='other_td'>免费率</th>";
                table += "</tr>";
                table += "</thead>";
                table += "<body>";
                for (var j = 0; j < salesAttainmentRate.length; j++) {
                    var color = "#ffe1ff";
                    if (j % 2 == 0) {
                        color = "#eeeed1";
                    }
                    var ratio = salesAttainmentRate[j].xswcl + "%";

    				table += "<tr onclick='showSalesAttainmentRateWeeklyByArea(\"" + salesAttainmentRate[j].yhid + "\",\"" + zblxmc + "\",\"" + salesAttainmentRate[j].qyid + "\",\"" + salesAttainmentRate[j].qymc + "\",\"" + salesAttainmentRate[j].kszq + "\",\"" + salesAttainmentRate[j].jszq + "\")'>";
    				table += "<td class='qu_td' rowspan='2' style='background-color:" + color + "'>" + salesAttainmentRate[j].zsxm + "</td>";
                    table += "<td class='other_td' rowspan='2'  style='background-color:" + color + "'>" + salesAttainmentRate[j].sz + "</td>";
                    table += "<td class='other_td' style='background-color:" + color + "'>" + salesAttainmentRate[j].sfsl +"/"+salesAttainmentRate[j].sz+ "</td>";
                    table += "<td class='other_td' style='background-color:" + color + "'>" + salesAttainmentRate[j].bsfsl +"/"+salesAttainmentRate[j].wczsl + "</td>";
                    table += "</tr>";
                    table += "<tr onclick='showSalesAttainmentRateWeeklyByArea(\"" + salesAttainmentRate[j].yhid + "\",\"" + zblxmc + "\",\"" + salesAttainmentRate[j].qyid + "\",\"" + salesAttainmentRate[j].qymc + "\",\"" + salesAttainmentRate[j].kszq + "\",\"" + salesAttainmentRate[j].jszq + "\")'>";
    				table += "<td class='other_td' style='background-color:" + color + "'>" + ratio + "</td>";
                    table += "<td class='other_td' style='background-color:" + color + "'>" + salesAttainmentRate[j].bsfl + "%</td>";
                    table += "</tr>";
                }
                table += "</body>";
                table += "</table></div>";
                $("#weeklyStatis_jsrq #tatistics_attainmentrate_area_jsrq").append(table);
            }else{
                throw "loadSalesAttainmentRateByArea数据获取异常";
            }
        }
    });
}
function showSalesAttainmentRateWeeklyByArea(yhid,zblxmc,qyid,qymc,kszq,jszq){
	var load_flg=$("#load_flg").val();
	if(load_flg=="0"){
		jQuery('<form action="/common/view/displayView" method="POST">' +  // action请求路径及推送方法
		            '<input type="text" name="view_url" value="/ws/statistics/salesAttainmentRateByAreaViewAndJsrq?load_flg='+load_flg+'&zblxcsmc='+zblxmc+'&yhid='+yhid+'&qyid='+qyid+'&qymc='+qymc+'&kszq='+kszq+'&jszq='+jszq+'&flag=weekly"/>' + 
		        '</form>')
		    .appendTo('body').submit().remove();
	}else if(load_flg=="1"){
		$("#weeklyStatis_jsrq").load("/ws/statistics/salesAttainmentRateByAreaViewAndJsrq?load_flg="+load_flg+"&zblxcsmc="+zblxmc+"&yhid="+yhid+"&qyid="+qyid+"&qymc="+qymc+"&kszq="+kszq+"&jszq="+jszq+"&flag=weekly");
	}
}
function init(){
	//添加日期控件
	laydate.render({
	   elem: '#searchkszq',
	   theme: '#2381E9',
	   type:'month'
	});
	laydate.render({
		elem: '#searchjszq',
		theme: '#2381E9',
		type:'month'
	});
}
$(function(){
    init();
    loadAttainmentRate();
});

