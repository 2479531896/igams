/**
 * 全局变量
 */
var bkyysjdstart="";
var bkyysjdend="";
var disDate = [];
var nowDate = "";
var dqDate;

/**
 * 日期初始化
 */
function initbkyyrq(){
    var appDate = laydate.render({
        elem: '#editUnAppDateForm #bkyyrq',
        theme: '#2381E9',
        trigger: 'click',
        min: nowDate,
        btns: ['clear', 'confirm'],
        ready: formatDate,
        change: formatDate,
        done: function(value, date){
        }
    });
}

/**
 * 日期范围初始化
 */
function initbkyyrqfw(){
    var startDate = laydate.render({
        elem: '#editUnAppDateForm #bkyyrqstart',
        theme: '#2381E9',
        trigger: 'click',
        min: nowDate,
        btns: ['clear', 'confirm'],
        ready: formatDate,
        change: formatDate,
        done: function(value, date){
            if (value==null || value ==""){
                date.year = dqDate.getFullYear();
                date.month = dqDate.getMonth()+1;
                date.date = dqDate.getDate();
                date.hours = date.hours;
                date.minutes = date.minutes;
                date.seconds = date.seconds;
            }
            endDate.config.min = {
                year: date.year,
                month: date.month - 1,
                date: date.date,
                hours: date.hours,
                minutes: date.minutes,
                seconds: date.seconds
            };
            var chooseDate = new Date(date.year+"-"+date.month+"-"+date.date).getTime();
            var flag = true;
            if (disDate.length>0){
                var minDate = new Date(disDate[0]).getTime();
                for (var i = 0; i < disDate.length; i++) {
                    var lsdate1 = new Date(disDate[i]).getTime();
                    if (flag && chooseDate<lsdate1){
                        minDate = lsdate1;
                        flag = false;
                        console.log(new Date(minDate))
                    }
                    if (minDate>lsdate1 && chooseDate<lsdate1){
                        minDate = lsdate1
                        console.log(new Date(minDate))
                    }
                }
                if (minDate>chooseDate){
                    var lsdate2 = new Date(minDate);
                    endDate.config.max = {
                        year: lsdate2.getFullYear(),
                        month: lsdate2.getMonth(),
                        date: lsdate2.getDate(),
                        hours: lsdate2.getHours(),
                        minutes: lsdate2.getMinutes(),
                        seconds: lsdate2.getSeconds()
                    };
                }
            }
        }
    });
    var endDate = laydate.render({
        elem: '#editUnAppDateForm #bkyyrqend',
        theme: '#2381E9',
        trigger: 'click',
        min: nowDate,
        btns: ['clear', 'confirm'],
        ready: formatDate,
        change: formatDate,
        done: function(value, date){
            startDate.config.max = {
                year: date.year,
                month: date.month - 1,
                date: date.date,
                hours: date.hours,
                minutes: date.minutes,
                seconds: date.seconds
            };
            var chooseDate = new Date(date.year+"-"+date.month+"-"+date.date).getTime();
            var flag = true;
            if (disDate.length>0){
                var minDate = new Date(disDate[0]).getTime();
                for (var i = 0; i < disDate.length; i++) {
                    var lsdate1 = new Date(disDate[i]).getTime();
                    if (flag && chooseDate>lsdate1){
                        minDate = lsdate1;
                        flag = false;
                        console.log(new Date(minDate))
                    }
                    if (minDate<lsdate1 && chooseDate>lsdate1){
                        minDate = lsdate1
                        console.log(new Date(minDate))
                    }
                }
                if (minDate<chooseDate){
                    var lsdate2 = new Date(minDate);
                    startDate.config.min = {
                        year: lsdate2.getFullYear(),
                        month: lsdate2.getMonth(),
                        date: lsdate2.getDate(),
                        hours: lsdate2.getHours(),
                        minutes: lsdate2.getMinutes(),
                        seconds: lsdate2.getSeconds()
                    };
                }
            }
            //控件选择完毕后的回调---点击日期、清空、现在、确定均会触发。
        }
    });
}

/**
 * 时间段初始化
 */
function initbkyysjd() {
    var startHour = laydate.render({
        elem: '#editUnAppDateForm #bkyysjdstart',
        min: bkyysjdstart,
        max: bkyysjdend,
        type: 'time',
        trigger: 'click',
        format: "HH:mm",
        btns: ['clear', 'confirm'],
        ready: formatHour,
        done: function (value, date) {
            endHour.config.min = {
                year: date.year,
                month: date.month - 1,
                date: date.date,
                hours: date.hours,
                minutes: date.minutes + 1,
                seconds: date.seconds
            };
        }
    });
    var endHour = laydate.render({
        elem: '#editUnAppDateForm #bkyysjdend',
        min: bkyysjdstart,
        max: bkyysjdend,
        type: 'time',
        format: "HH:mm",
        btns: ['clear', 'confirm'],
        trigger: 'click',
        ready: formatHour,
        done: function (value, date) {
            startHour.config.max = {
                year: date.year,
                month: date.month - 1, //关键
                date: date.date,
                hours: date.hours,
                minutes: date.minutes - 1,
                seconds: date.seconds
            }
        }
    });
}

/**
 * 去掉秒钟以及部分分钟
 */
function formatHour() {
    //分钟
    var showtime = $($(".laydate-time-list li ol")[1]).find("li");
    for (var i = showtime.length-1; i >= 0; i--) {
        var minutes = showtime[i].innerText;
        if (minutes != "00") {
            //分进行过滤 只保留00一次性显示六十个太多没必要
            showtime[i].remove()
        }
    }
    $($('.laydate-time-list li ol')[2]).find('li').remove();
}

/**
 * 不可选日期格式化
 */
function formatDate(){
    var elem = $(".layui-laydate-content");//获取table对象
    layui.each(elem.find('tr'), function (trIndex, trElem) {//遍历tr
        layui.each($(trElem).find('td'), function (tdIndex, tdElem) {
            //遍历td
            var tdTemp = $(tdElem);
            if (tdTemp.hasClass('laydate-day-next') || tdTemp.hasClass('laydate-day-prev')) {
                return;
            }
            // if (tdIndex == 1) { //此时周一不可选
            //     //此处判断，是1的加上laydate-disabled，0代表星期日
            //     tdTemp.addClass('laydate-disabled');
            // }
            if(disDate.indexOf(tdTemp.attr("lay-ymd"))>-1){//指定数组中的日期不可选
                tdTemp.addClass('laydate-disabled');
            }
        });
    });
}

/**
 * 获取并格式化禁用日期
 */
function getDisDate(){
    $.ajax({
        type:'post',
        url:"/detection/detection/pagedataUnAppDate",
        cache: false,
        data: {"access_token":$("#ac_tk").val()},
        dataType:'json',
        success:function(data){
            if (data.bkyyrqList.length>0){
                for (var i = 0; i < data.bkyyrqList.length; i++) {
                    disDate.push(data.bkyyrqList[i].bkyyrq)
                }
            }
            var lsDisDate = [];
            for (var i = 0; i < disDate.length; i++) {
                var lsDates = disDate[i].split("-")
                if(lsDates.length == 3){
                    var lsDate = parseInt(lsDates[0])+"-"+parseInt(lsDates[1])+"-"+parseInt(lsDates[2]);
                    lsDisDate.push(lsDate)
                }
            }
            disDate = lsDisDate;
            //获取最近可预约时间
            var date = new Date();
            nowDate = getNowDate(date);
            bkyysjdstart = $("#appoinmentTimeStart").val()+":00";
            bkyysjdend = $("#appoinmentTimeEnd").val()+":00";
            if ($("#flag").val() == 'add'){
                initbkyyrqfw();
            }else if ($("#flag").val() == 'mod'){
                initbkyyrq();
            }
            initbkyysjd();
        }
    })
}

function getNowDate(date){
    dqDate = date
    var lsnowDate = date.getFullYear() + '-' + (date.getMonth()+1) +'-' + date.getDate();
    if (disDate.length>0){
        for (var i = 0; i < disDate.length; i++) {
            if (disDate[i] == lsnowDate){
                // date.
                console.log(date.getFullYear() + '-' + (date.getMonth()+1) +'-' + date.getDate())
                date.setDate(date.getDate()+1)
                console.log(date.getFullYear() + '-' + (date.getMonth()+1) +'-' + date.getDate())
                getNowDate(date)
            }
        }
    }
    var lsDate = date.getFullYear() + '-' + (date.getMonth()+1) +'-' + date.getDate();
    return lsDate;
}
/**
 * 初始化页面
 * @returns
 */
function initPage(){
    getDisDate();
}

$(function(){
    initPage()
});


