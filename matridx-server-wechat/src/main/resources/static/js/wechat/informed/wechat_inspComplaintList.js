var ywid = $("#ywid").val();
var wxid = $("#wxid").val();
var wbcxdm = $("#wbcxdm").val();
var pageNumber = 0;
var pageSize = 10;
var loadMroeFlag = true;
$(function(){
    search();
});

function showloading(text){
    //loading显示
    $.showLoading(text);
}

function hideloading(){
    setTimeout(function() {
        //loading隐藏
        $.hideLoading();

    },200)
}

/**
 * 新增投诉
 */
function addComplaint(flag){
    var addurl = "/wechat/inspComplaintEdit?ywid="+ywid+"&wxid="+wxid+"&wbcxdm="+wbcxdm;
    if (flag && "reportList" == flag){
        addurl += "&backpage="+flag;
    }
    window.location.replace(addurl);
}

function search(){
    pageNumber = 0;
    loadMroeFlag = true;
    $("#sjycList").html("")
    loadMore()
}

function loadMore(){
    if (loadMroeFlag){
        showloading("加载中...");
        loadMroeFlag =false;
        pageNumber = pageNumber+1;
        $("#loading").show();
        $("#noInfo").hide();
        $("#loadComplete").hide();
        $.ajax({
            url: '/wechat/inspComplaintList',
            type: 'post',
            dataType: 'json',
            data : {
                pageNumber: pageNumber,
                pageSize: pageSize,
                wxid: $("#wxid").val(),
                ywid: $("#ywid").val()
            },
            success: function(data) {
                if (data.rows.length>0){
                    htmlConcat(data.rows);
                    loadMroeFlag = true
                    if (data.rows.length<pageSize){
                        loadMroeFlag = false
                        $("#loading").hide();
                        $("#noInfo").hide();
                        $("#loadComplete").show();
                    }
                    hideloading();
                }else {
                    loadMroeFlag = false
                    if (pageNumber==1){
                        $("#loading").hide();
                        $("#noInfo").show();
                        $("#loadComplete").hide();
                        if ("list" != $("#flag").val()){
                            showloading("跳转中...")
                            addComplaint("reportList");
                        }
                    }else {
                        $("#loading").hide();
                        $("#noInfo").hide();
                        $("#loadComplete").show();
                    }
                    hideloading();
                }
            }
        });
    }
}

/**
 * html拼接
 * @param rows
 */
function htmlConcat(rows){
    var html = '';
    for (var i = 0; i < rows.length; i++) {
        html += '<div class="weui-panel weui-cell_swiped" style="border-radius: 10px;margin: 8px;'+(rows[i].sfzd && rows[i].sfzd =='1'?'border:4px solid #FFA366;':'')+'" data-ycid="'+rows[i].ycid+'">' +
            '            <div class="weui-cell__bd" style="border-radius: 10px;display: flex;transform: translate3d(0px, 0px, 0px);">' +
            countFormatter(rows[i])+
            '                <div class="weui-form-preview" style="width: 95%;">' +
            '                    <div style="height: 0.1rem;"></div>' +
            '                    <div class="weui-form-preview__hd weui-media-box_appmsg" style="display: flex;border-left: 3px solid #00AFEC;margin-bottom: 0;padding: 0.1rem 0.2rem;">' +
            '                        <div class="weui-media-box__bd" style="flex: 5">' +
            '                            <label class="weui-form-preview__value" style="margin: 0;text-align: left;word-break: keep-all;overflow: hidden;white-space: nowrap;text-overflow:ellipsis;">'+(rows[i].ycbt?rows[i].ycbt:"")+'</label>' +
            '                         </div>' +
            sloganFormatter(rows[i])+
            '                    </div>' +
            '                    <div class="weui-form-preview__bd" style="font-size: 12px;padding: 0px 10px" onclick="complaintInfo(\''+rows[i].ycid+'\',\''+rows[i].ywid+'\')">' +
            '                       <div style="display: flex">' +
            '                        <div class="weui-form-preview__item" style="flex: 4">' +
            '                            <span class="weui-form-preview__value" style="text-align: left;word-break: keep-all;overflow: hidden;white-space: nowrap;text-overflow:ellipsis;">'+(rows[i].lxmc?rows[i].lxmc:"")+(rows[i].zlxmc?'-'+rows[i].zlxmc:"")+'</span>' +
            '                        </div>' +
            '                        <div class="weui-form-preview__item" style="flex: 2">' +
            '                            <span class="weui-form-preview__value" style="text-align: left;word-break: keep-all;overflow: hidden;white-space: nowrap;text-overflow:ellipsis;">'+(rows[i].lrsj?rows[i].lrsj:"")+'</span>' +
            '                        </div>' +
            '                       </div>' +
            '                       <div style="display: flex">' +
            '                        <div class="weui-form-preview__item" style="flex: 3">' +
            '                            <span class="weui-form-preview__value" style="text-align: left;word-break: keep-all;overflow: hidden;white-space: nowrap;text-overflow:ellipsis;">'+(rows[i].zhhfsj?rows[i].zhhfsj:"-")+'</span>' +
            '                        </div>' +
            '                        <div class="weui-form-preview__item" style="flex: 4">' +
            '                            <span class="weui-form-preview__value" style="text-align: left;word-break: keep-all;overflow: hidden;white-space: nowrap;text-overflow:ellipsis;">'+(rows[i].zhhfnr?rows[i].zhhfnr:"-")+'</span>' +
            '                        </div>' +
            '                       </div>' +
            '                    </div>' +
            '                </div>' +
            '               <div class="weui-icon-swipeLeft" style="width: 5%;height: 100px" ></div>' +
            '            </div>' +
            toTopFormatter(rows[i]) +
            '        </div>'
    }
    $("#sjycList").append(html)
    $('.weui-cell_swiped').swipeout()
}

function countFormatter(row){
    var html = "";
    if(row.count && row.count !='0'){
        html += '<span class="weui-badge" style="position: absolute;top: .2em;right: .2em;z-index: 2;">'+row.count+'</span>'
    }
    return html;
}

/**
 * 标记格式化
 * @param row
 * @returns {string}
 */
function sloganFormatter(row){
    var html = "";
    if(row.sfjs == '1'){
        html += "<div style=\"right: 5px;font-size: 15px;color: #00AFEC;flex: 1;text-align: center;\">已结束</div>";
    }else{
        html += "<div style=\"right: 5px;font-size: 15px;color: #FF4D4F;flex: 1;text-align: center;\">未结束</div>";
    }
    return html;
}
/**
 * 置顶标记格式化
 * @param row
 * @returns {string}
 */
function toTopFormatter(row){
    var html = "";
    html = "<div class='weui-cell__ft'>" +
        "   <div class='weui-swiped-btn delete-swipeout' onclick='topComplaint(\""+row.ycid+"\",\""+row.sfzd+"\")' style='font-weight: bold;;background-color: #00AFEC'><a style='display: block;vertical-align: middle;line-height: 70px;'>"+(row.sfzd && row.sfzd == '1' ? "取消置顶":"置顶")+"</a></div>";

    if(row && row.sfjs != '1'){
        html += "   <div class='weui-swiped-btn delete-swipeout' onclick='finishComplaint(\""+row.ycid+"\")' style='font-weight: bold;background-color: red'><a style='display: block;vertical-align: middle;line-height: 70px;'>结束</a></div>";
    }else if (row && (!row.sfpj || row.sfpj == '0')){
        html += "   <div class='weui-swiped-btn delete-swipeout' onclick='evaluationComplaint(\""+row.ycid+"\")' style='font-weight: bold;background:#DBF6FF;'><a style='color:#00AFEC;display: block;vertical-align: middle;line-height: 70px;'>评价</a></div>";
    }
    html += "   </div>";
    return html;
}

/**
 * 投诉置顶、取消置顶
 * @param ycid
 * @param sfzd
 */
function topComplaint(ycid,sfzd){
    let loadingText = "正在置顶...";
    let successText = "置顶成功!";
    let failText = "置顶失败!";
    if (sfzd && sfzd == '1'){
        sfzd = '0'
        loadingText = "正在取消置顶...";
        successText = "取消置顶成功!";
        successText = "取消置顶失败!";
    }else {
        sfzd = '1'
    }
    showloading(loadingText)
    $.ajax({
        url: '/wechat/topComplaint',
        type: 'post',
        dataType: 'json',
        data : {
            ycid: ycid,
            sfzd: sfzd,
            zdry: $("#wxid").val(),
        },
        success: function(data) {
            if (data && data.status == 'success'){
                hideloading();
                $.toptip(successText, 'success');
                search();
            }else {
                hideloading();
                $.toptip(failText+JSON.stringify(data), 'error');
            }
        }
    });
}

/**
 * 投诉结束
 * @param ycid
 */
function finishComplaint(ycid){
    $.confirm({
        title: '警告',
        text: '<span style="color: red;">确认结束？</span>',
        onOK: function () {
            showloading("正在结束...");
            $.ajax({
                url: '/wechat/finishComplaint',
                type: 'post',
                dataType: 'json',
                data : {
                    ycid: ycid,
					ids:  ycid,
                    sfjs: "1",
                    jsry: $("#wxid").val(),
					xgry: $("#wxid").val(),
                },
                success: function(data) {
                    if (data && data.status == 'success'){
                        hideloading();
                        $.confirm({
                            title: '提示',
                            text: '保存成功！是否进入评价？',
                            onOK: function () {
                                window.location.href = "/wechat/evaluation?ywid="+ycid+"&wxid="+$("#wxid").val()+"&wbcxdm="+wbcxdm;
                            },
                            onCancel: function () {
                                $.closeModal();

                            }
                        });
                        search();
                    }else {
                        hideloading();
                        $.toptip("保存失败！"+JSON.stringify(data), 'error');
                    }
                }
            });
        },
        onCancel: function () {
            $.closeModal();
        }
    });

}

function evaluationComplaint(ycid){
    window.location.href = "/wechat/evaluation?ywid="+ycid+"&wxid="+$("#wxid").val()+"&wbcxdm="+wbcxdm;
}
/**
 * 进入异常详情
 * @param ycid
 */
function complaintInfo(ycid,_ywid){
    var infoUrl = "/wechat/inspComplaintInfo?ywid=" + (ywid==""?_ywid:ywid) + "&ycid=" + ycid + "&wxid=" + wxid + "&wbcxdm=" + wbcxdm;
    window.location.href = infoUrl;
}

/**
 * 下拉到底部触发事件
 */
$(document.body).infinite(50).on("infinite", function() {
    var loading = false;
    if(loading){
        return;
    }
    loading = true;
    if (loadMroeFlag) {
        loadMore()
    }
    loading = false
});

//若页面是通过上一页按钮返回进来的，则刷新页面
window.addEventListener('pageshow', function(event) {
    if (event.persisted) {
        search()
    }
})