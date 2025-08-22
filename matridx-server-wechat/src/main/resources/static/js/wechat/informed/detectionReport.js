var pageSize = 5;
var pageEntireNumber = -5;
var html = '';
var loadMoreEntireFlag = 0;
var flag = 0;
var noMoreHtml ='<div class="weui-loadmore" id="noMore">' +
    '   <span class="weui-loadmore__tips">暂无更多信息</span>' +
    '</div>'


//加载更多方法
function loadMore() {
    $("#loadMore").removeAttr("hidden", true);
    var pageNumber = 0;
    pageEntireNumber = pageEntireNumber + 5
    pageNumber = pageEntireNumber
    $.ajax({
        type: "post",
        url: "/wechat/getReportDetails",
        data: {
            "fzjcid": $("#fzjcid").val(),
            "pageSize": pageSize,
            "pageNumber": pageNumber,
        },
        success: function (result) {
            var lshtml = '';
            var len = result.fzjcxxDtos.length;
            if(result.fzjcxxDto.xm==null){
                result.fzjcxxDto.xm="";
            }
            if(result.fzjcxxDto.sysj==null){
                result.fzjcxxDto.sysj="";
            }
            //拼接页面
            for (var i = 0; i < len; i++) {
                if(result.fzjcxxDtos[i].jcjgmc==null){
                    result.fzjcxxDtos[i].jcjgmc="";
                }
                if(result.fzjcxxDtos[i].jcxmmc==null){
                    result.fzjcxxDtos[i].jcxmmc="";
                }
                lshtml = lshtml + '<div class="weui-panel" style="border-radius: 10px;">' +
                    '<div class="weui-form-preview">' +
                    '   <div class="weui-form-preview__bd" style="padding: 5px 10px">' +
                    '                   <div class="weui-form-preview__item">' +
                    '                       <label class="weui-form-preview__label">患者姓名</label>' +
                    '                       <span class="weui-form-preview__value" style="word-break: keep-all;">' + result.fzjcxxDto.xm  + '</span>' +
                    '                   </div>' +
                    '                   <div class="weui-form-preview__item">' +
                    '                       <label class="weui-form-preview__label">检测日期</label>' +
                    '                       <span class="weui-form-preview__value" style="word-break: keep-all;">' + result.fzjcxxDto.sysj  + '</span>' +
                    '                   </div>' +
                    '                   <div class="weui-form-preview__item">' +
                    '                       <label class="weui-form-preview__label">检测项目</label>' +
                    '                       <span class="weui-form-preview__value" style="word-break: keep-all;">' + result.fzjcxxDtos[i].jcxmmc?result.fzjcxxDtos[i].jcxmmc:"" + '</span>' +
                    '                   </div>' +
                    '                   <div class="weui-form-preview__item">' +
                    '                       <label class="weui-form-preview__label">检测结果</label>' +
                    '                       <span class="weui-form-preview__value" style="word-break: keep-all;">' + result.fzjcxxDtos[i].jcjgmc?result.fzjcxxDtos[i].jcjgmc:"" + '</span>' +
                    '                   </div>' +
                    '   </div>'
                lshtml = lshtml + '<div class="weui-form-preview__ft">' +
                    '       <button class="weui-form-preview__btn weui-form-preview__btn_primary" id="viewReport" style="color: #c87d2f" onclick="viewPDF(\'' + result.fjcfbDto.fjid + '\')">预览</button>' +
                    '       <button class="weui-form-preview__btn weui-form-preview__btn_primary" id="viewReport" style="color: #0ca2ff" onclick="xz(\'' + result.fjcfbDto.fjid + '\',\'' + result.fjcfbDto.sign + '\')">下载</button>' +
                    '   </div>'
                lshtml = lshtml + '</div>' +
                    '</div>'
                // lshtml=lshtml+'<div style="height: 5px;background-color:#3c7bfb;">'+'</div>'
            }
            /*} else {*/

            $("#fzjcxxDtos").append(lshtml)

            if (len<5){
                //拼接“没有更多数据”
                loadMoreEntireFlag = 1
                $("#fzjcxxDtos").append(noMoreHtml)
            }
            $("#loadMore").attr("hidden", true);
        },
        fail: function (result) {
            layer.msg('加载失败！');
        }
    })
}


$(window).scroll(function () {

    // scrollTop是滚动条滚动时，距离顶部的距离
    var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
    // windowHeight是可视区的高度
    var windowHeight = document.documentElement.clientHeight || document.body.clientHeight;
    // scrollHeight是滚动条的总高度
    var scrollHeight = document.documentElement.scrollHeight || document.body.scrollHeight;
    // 滚动条到底部
    if (scrollTop + windowHeight > scrollHeight - 20) {
        // 到了底部之后想做的操作,如到了底部之后加载

        if (loadMoreEntireFlag == 0) {
            loadMore()
        }
    }
});



$(document).ready(function () {
    // 在这里写你的代码...
    loadMore();
    // layer.msg('该检测正在进行或已完成，不可修改！');
    // $("#loadMore").attr("hidden",true);
});
function viewPDF(fjid){
    var url= "/common/view/displayView?view_url=/ws/file/pdfPreview?fjid=" + fjid;
    window.location.href=url;
}

function xz(fjid,sign){
    $("#ajaxForm #fjid").val(fjid);
    $("#ajaxForm #sign").val(sign);
    $("#ajaxForm").attr("action","/wechat/file/loadFile");
    $("#ajaxForm").submit();
}