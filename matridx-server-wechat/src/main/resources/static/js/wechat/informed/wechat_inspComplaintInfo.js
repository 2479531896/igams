var ywid = $("#ywid").val();
var ycid = $("#ycid").val();
var wxid = $("#wxid").val();
var wbcxdm = $("#wbcxdm").val();
var pageNumber = 0;
var pageSize = 10;
var loadMroeFlag = true;
var temp_gid;
var temp_fkid;
var ffkid;
var localids=[];
var qrr=$("#wxid").val();
var sfjs=$("#sfjs").val();

$(function(){
    $("#yinyong").hide();
    //初始化微信信息
    $.ajax({
        url: '/wechat/getJsApiInfo',
        type: 'post',
        data: {
            "url":location.href.split('#')[0],
            "wbcxdm":wbcxdm
        },
        dataType: 'json',
        success: function(result) {
            //注册信息
            wx.config({
                debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                appId: result.appid, // 必填，公众号的唯一标识
                timestamp: result.timestamp, // 必填，生成签名的时间戳
                nonceStr: result.noncestr, // 必填，生成签名的随机串
                signature: result.sign, // 必填，签名，见附录1
                jsApiList: ['checkJsApi','scanQRCode','startRecord','stopRecord','onVoiceRecordEnd','playVoice','pauseVoice','stopVoice','onVoicePlayEnd','uploadVoice','downloadVoice','translateVoice','chooseImage','previewImage','uploadImage','downloadImage']
                // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
            });
            wx.error(function(res) {
                console.log(res);
            });
            //config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作
            wx.ready(function() {
                wx.checkJsApi({
                    jsApiList: ['scanQRCode','startRecord','stopRecord','onVoiceRecordEnd','playVoice','pauseVoice','stopVoice','onVoicePlayEnd','uploadVoice','downloadVoice','translateVoice','chooseImage','previewImage','uploadImage','downloadImage'],
                    success: function(res) {
                        $("#uploader").unbind("click").click(function(e){
                            try{
                                wx.chooseImage({
                                    count: 4, // 默认9
                                    sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
                                    sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
                                    success: function (res) {
                                        var localIds = res.localIds;
                                        upload(localIds, 0);
                                    }
                                });
                            } catch (ex) {
                                $("#chrom").show();
                                $("#phone").hide();
                            }
                        });
                    }
                });
            });
        }
    });
    isFromWeiXin();
    //0.初始化fileinput
    var oFileInput = new FileInput();
    oFileInput.Init("complaintInfo","displayUpInfo",2,1,"wechat_file");
    search();
});

function search(){
    pageNumber = 0;
    loadMroeFlag = true;
    $("#commentList").html("")
    loadMore();


}

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
 * 标本信息查看
 * @param sjid
 * @param ybbh
 * @param hzxm
 */
function reportDealById(sjid,ybbh,hzxm){
    var sign = $("#sign").val()
    jQuery('<form action="/wechat/getReportView" method="POST">' +  // action请求路径及推送方法
        '<input type="text" name="wxid" value="'+wxid+'"/>' +
        '<input type="text" name="ywid" value="'+sjid+'"/>' +
        '<input type="text" name="hzxm" value="'+hzxm+'"/>' +
        '<input type="text" name="sign" value="'+sign+'"/>' +
        '</form>')
        .appendTo('body').submit().remove();
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
            url: '/wechat/inspComplaintComment',
            type: 'post',
            dataType: 'json',
            data : {
                pageNumber: pageNumber,
                pageSize: pageSize,
                wxid: wxid,
                ywid: ywid,
                ycid: ycid,
                wbcxdm:wbcxdm
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
        html += '<div class="weui-panel" style="border-radius: 10px;" onclick="comentParent(\''+rows[i].gid+'\',\''+rows[i].fkid+'\',\''+rows[i].lrrymc+'\',\''+rows[i].lrry+'\',\''+rows[i].fkxx+'\')">' +
                '   <div class="weui-cell__bd" style="border-radius: 10px;">' +
                '       <div class="weui-form-preview" style="border-radius: 10px;">' +
                '           <div style="height: 0.1rem;"></div>' +
                '           <div class="weui-form-preview__bd" style="font-size: 14px;font-weight: normal;padding: 0px 10px">' +
                '               <div style="display: flex">' +
                '                   <div class="weui-form-preview__item" style="flex: 1">' +
                '                       <label class="weui-form-preview__value" style="font-weight: normal;text-align: left;word-break: keep-all;overflow: hidden;white-space: nowrap;text-overflow:ellipsis;font-size: 14px;">'+ (rows[i].lrrymc?rows[i].lrrymc:'-')+'</label>' +
                '                   </div>' +
                '                   <div class="weui-form-preview__item" style="flex: 2">' +
                '                       <label class="weui-form-preview__value" style="font-weight: normal;text-align: center;word-break: keep-all;overflow: hidden;white-space: nowrap;text-overflow:ellipsis;font-size: 14px;">'+ rows[i].lrsj+'</label>' +
                '                   </div>' +
                '                   <div class="weui-form-preview__item" style="flex: 1">' +
                '                       <label class="weui-form-preview__value" style="font-weight: normal;text-align: right;word-break: keep-all;overflow: hidden;white-space: nowrap;text-overflow:ellipsis;font-size: 14px;">'+ computingTime(rows[i].lrsj)+'</label>' +
                '                   </div>' +
                '               </div>' +
                '               <div style="display: flex" onclick="comentParent(\''+rows[i].gid+'\',\''+rows[i].fkid+'\',\''+rows[i].lrrymc+'\',\''+rows[i].lrry+'\',\''+rows[i].fkxx+'\')">' +
                '                   <div class="weui-form-preview__item" style="flex: 3">' +
                '                       <label class="weui-form-preview__value" style="font-weight: normal;text-align: left;font-size: 14px;">'+ rows[i].fkxx+'</label>' +
                '                   </div>' +
                fkfjFormat(rows[i]) +
                openFkxxFormat(rows[i]) +
                '                </div>' +
                '               <div id="showFkInfo'+rows[i].fkid+'" style="display: none;">' +
                '               </div>' +
                '           </div>' +
                '           <div style="height: 0.1rem;"></div>' +
                '       </div>' +
                '   </div>' +
                '</div>'

    }
    $("#commentList").append(html)
}
function openFkxxFormat(row){
    var html = '';
    if(row.ghfcs && row.ghfcs >0){
        html += '<div class="weui-form-preview__item">' +
                '  <button type="button" class="weui-btn weui-btn-mini weui-btn_bule" style="font-size: smaller;padding: 0 8px;height: 30px;font-size: smaller;vertical-align: middle;width: auto;margin-right: 0;" data-open="false" data-text="展开('+row.ghfcs+')" onclick="showMoreFkxx(\''+row.fkid+'\',\''+row.gid+'\',\''+row.ghfcs+'\')">展开('+row.ghfcs+')</button>' +
                '   </div>';
    }
    return html;
}
function fkfjFormat(row){
    var html = '';
    if (row.fjcfbDtos && row.fjcfbDtos.length>0){
        html += '<div style="margin: 0 2px;;height: 30px;text-align: right">' +
                '<button type="button" class="weui-btn weui-btn-mini weui-btn_bule" style="font-size: smaller;padding: 0 8px;height: 30px;font-size: smaller;vertical-align: middle;width: auto;margin-right: 0;" onclick="viewSjycFj(\'ycfkFj_'+row.fkid+'\')">附件（'+row.fjcfbDtos.length+'）</button>'
        for (let i = 0; i < row.fjcfbDtos.length; i++) {
            html += '<input id="'+row.fjcfbDtos[i].fjid+'" wjm="'+row.fjcfbDtos[i].wjm+'" type="hidden" name="ycfkFj_'+row.fkid+'"/>'
        }
        html += '</div>'
    }
    return html;
}
/**
 * 附件查看
 * @param name
 */
function viewSjycFj(name){
    event.stopPropagation();
    var elementsByName = document.getElementsByName(name);
    if (elementsByName && elementsByName.length>0){
        let items = [];
        for (let i = 0; i < elementsByName.length; i++) {
            let image = "/wechat/file/getwechatFileInfo?fjid="+elementsByName[i].id;
            let item = {};
            item["image"] = image;
            item["caption"] = elementsByName[i].getAttribute("wjm");
            items.push(item);
        }
        var pb = $.photoBrowser({
            items: items,
            onOpen: function () {
                $("#commentArea").hide();
            },
            onClose: function () {
                $("#commentArea").show();
            }
        });
        pb.open();
    }
}

/**
 * 发送反馈
 */
function complaintComment(){
    var fkxx = document.getElementById("searchInput").value;
    if (!fkxx){
        $.toptip('请输入反馈信息', 'error');
        return;
    }
    var fjids = $("#fjids").val();
    $.ajax({
        url: '/wechat/saveComplaintComment',
        type: 'post',
        dataType: 'json',
        data : {
            ycid: ycid,
            fkxx: fkxx,
            fjids: fjids,
            gid: temp_gid,
            qrr: qrr,
            ffkid: ffkid,
            wxid: wxid,
            wbcxdm: wbcxdm,

        },
        success: function(data) {
            if (data && data.status =="success"){
                $.toptip(data.message, 'success');
                $("#fjids").val("");
                $("#searchInput").val("");
                search();
                hideFjInfo();
            }else{
                $.toptip(JSON.stringify(data), 'error');
            }
        }
    });
}



// 计算时间
function computingTime(dateTime) {
    var time = '';
    var date2 = new Date();    // 当前系统时间
    var date3 = date2.getTime() - new Date(dateTime).getTime();   // 时间差的毫秒数

    var hours = Math.floor(date3 / (3600 * 1000)); // 相差小时
    if (hours > 0) {
        time = hours + '小时前'
        if (hours > 24) {// 如果小时大于24，计算出天和小时
            var day = parseInt(hours / 24);
            hours %= 24;// 算出有多分钟
            time = day + '天' + hours + '小时前'
        }
    } else {
        // 计算相差分钟数
        var leave2 = date3 % (3600 * 1000);      // 计算小时数后剩余的毫秒数
        var minutes = Math.floor(leave2 / (60 * 1000));
        if (minutes > 0) {
            time = minutes + '分钟前';
        } else {
            time = '刚刚';
        }
    }
    return time;
}

/**
 * 回复评论
 */
function comentParent(gid,fkid,lrrymc,lrry,fkxx){
    event.stopPropagation();
    if (temp_fkid != fkid){
        temp_fkid = fkid;
        temp_gid = gid;
        ffkid = fkid;
        $("#yinyong").show();
        $("#yinyong").text("引用："+fkxx);
        $("#discusFj").hide();
        qrr = lrry
        hideFjInfo()
    }else{
        temp_fkid = "";
        temp_gid = "";
        ffkid = "";
        qrr = wxid
        $("#yinyong").hide();
        $("#yinyong").text("");
        $("#discusFj").show();
    }
}
function showMoreFkxx(fkid,gid,ghfcs){
    event.stopPropagation()
    var open = event.target.dataset.open;
    var id = "showFkInfo"+fkid;
    if ("false" == open){
        event.target.dataset.open = "true";
        event.target.innerText = "收起";
        if (gid && ghfcs && ghfcs>0){
            showloading("加载中...")
            $.ajax({
                url: '/wechat/viewMoreDiscuss',
                type: 'post',
                dataType: 'json',
                data : {
                    gid: gid,
                },
                success: function(data) {
                    if (data && data.rows && data.rows.length>0){
                        $("#"+id).html("");
                        var html = "";
                        for (let i = 0; i < data.rows.length; i++) {
                            let row = data.rows[i];
                            if (row.fkid != fkid){
                                html += fkInfoHtmlConcat(row);
                            }
                        }
                        $("#"+id).html(html);
                        $("#"+id).show();
                    }else {
                        $("#"+id).html("暂无更多");
                        $("#"+id).show();
                    }
                    hideloading();
                }
            });
        }
    }else {
        $("#"+id).html("");
        $("#"+id).hide();
        event.target.dataset.open = "false";
        event.target.innerText = event.target.dataset.text;
    }
}

function fkInfoHtmlConcat(row){
    var html = '    <div style="padding: 0 5px;margin: 5px 10px;background-color: #eee;border-radius: 5px;"><div style="display: flex" onclick="comentParent(\''+row.gid+'\',\''+row.fkid+'\',\''+row.lrrymc+'\',\''+row.lrry+'\',\''+row.fkxx+'\')">' +
    '                   <div class="weui-form-preview__item" style="flex: 2">' +
    '                       <label class="weui-form-preview__value" style="font-weight: normal;text-align: left;word-break: keep-all;overflow: hidden;white-space: nowrap;text-overflow:ellipsis;font-size: 14px;">'+ (row.lrrymc?row.lrrymc:'-')+'</label>' +
    '                   </div>' +
    '                   <div class="weui-form-preview__item" style="flex: 4">' +
    '                       <label class="weui-form-preview__value" style="font-weight: normal;text-align: center;word-break: keep-all;overflow: hidden;white-space: nowrap;text-overflow:ellipsis;font-size: 14px;">'+ row.hfsj+'</label>' +
    '                   </div>' +
    '                   <div class="weui-form-preview__item" style="flex: 2">' +
    '                       <label class="weui-form-preview__value" style="font-weight: normal;text-align: right;word-break: keep-all;overflow: hidden;white-space: nowrap;text-overflow:ellipsis;font-size: 14px;">'+ computingTime(row.hfsj)+'</label>' +
    '                   </div>' +
    '               </div>' +
    '               <div style="display: flex">' +
    '                   <div class="weui-form-preview__item" style="flex: 2">' +
    '                       <label class="weui-form-preview__value" style="font-weight: normal;text-align: left;word-break: keep-all;overflow: hidden;white-space: nowrap;text-overflow:ellipsis;font-size: 14px;">'+ row.fkxx+'</label>' +
    '                   </div>' +
    '                </div></div>'

    return html;
}



//初始化fileinput
var FileInput = function () {
    var oFile = new Object();
    //初始化fileinput控件（第一次初始化）
    //参数说明 singleFlg 好像是标明 是否单文件上传限制，有点忘记了
    //impflg 用于后台判断是否为导入。1为导入，则开启线程进行导入  2：代表附件保存
    oFile.Init = function(ctrlName,callback,impflg,singleFlg,fileName) {
        var control = $('#' + ctrlName + ' #' + fileName);
        var filecnt = 0;

        //初始化上传控件的样式
        control.fileinput({
            language: 'zh', //设置语言
            uploadUrl: "/wechat/file/saveImportFile?access_token="+$("#ac_tk").val(), //上传的地址
            showUpload: false, //是否显示上传按钮
            showPreview: true, //展前预览
            showCaption: true,//是否显示标题
            showRemove: true,
            showCancel: false,
            showClose: false,
            showUploadedThumbs: true,
            encodeUrl: false,
            purifyHtml: false,
            browseClass: "btn btn-primary", //按钮样式
            dropZoneEnabled: false,//是否显示拖拽区域
            //minImageWidth: 50, //图片的最小宽度
            //minImageHeight: 50,//图片的最小高度
            //maxImageWidth: 1000,//图片的最大宽度
            //maxImageHeight: 1000,//图片的最大高度
            maxFileSize: 0,//单位为kb，如果为0表示不限制文件大小
            //minFileCount: 0,
            maxFileCount: 5, //表示允许同时上传的最大文件个数
            enctype: 'multipart/form-data',
            validateInitialCount:true,
            previewFileIcon: "<i class='glyphicon glyphicon-king'></i>",
            msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
            layoutTemplates :{
                //actionDelete:'', //去除上传预览的缩略图中的删除图标
                actionUpload:'',//去除上传预览缩略图中的上传图片；
                //actionZoom:''   //去除上传预览缩略图中的查看详情预览的缩略图标。
            },

            uploadExtraData:function (previewId, index) {
                //向后台传递id作为额外参数，是后台可以根据id修改对应的图片地址。
                var obj = {};
                obj.access_token = $("#ac_tk").val();
                obj.ywlx = $("#"+ ctrlName + " #ywlx").val();
                if(impflg && impflg == 1){
                    obj.impflg = 1;
                }
                obj.fileName = fileName;
                return obj;
            },
            initialPreviewAsData:false,
            initialPreview: [        //这里配置需要初始展示的图片连接数组，字符串类型的，通常是通过后台获取后然后组装成数组直接赋给initialPreview就行了
                //"http://localhost:8086/common/file/getFileInfo",
            ],
            initialPreviewConfig: [ //配置预览中的一些参数
            ]
        })
            //删除初始化存在文件时执行
            .on('filepredelete', function(event, key, jqXHR, data) {
            })
            //删除打开页面后新上传文件时执行
            .on('filesuccessremove', function(event, key, jqXHR, data) {
                if(fileName == 'wechat_file'){
                    var frames = $("#"+ ctrlName +" .file-preview-frame.krajee-default.kv-preview-thumb.file-preview-success");
                    var index = 0;
                    for(var i=0;i<frames.length;i++){
                        if(key == frames[i].id){
                            index = i;
                            break;
                        }
                    }
                    var str = $("#"+ ctrlName + " #fjids").val();
                    var arr=str.split(",");
                    arr.splice(index,1);
                    var str=arr.join(",");
                    $("#"+ ctrlName + " #fjids").val(str);
                }
            })

            // 实现图片被选中后自动提交
            .on('filebatchselected', function(event, files) {
                // 选中事件
                $(event.target).fileinput('upload');
                filecnt = files.length;

            })
            // 异步上传错误结果处理
            .on('fileerror', function(event, data, msg) {  //一个文件上传失败
                // 清除当前的预览图 ，并隐藏 【移除】 按钮
                $(event.target)
                    .fileinput('clear')
                    .fileinput('unlock');
                if(singleFlg && singleFlg==1){
                    $(event.target)
                        .parent()
                        .siblings('.fileinput-remove')
                        .hide()
                }
                // 打开失败的信息弹窗
                $.toptip("请求失败，请稍后重试!", 'error');
            })
            // 异步上传成功结果处理
            .on('fileuploaded', function(event, data) {
                // 判断 code 是否为  0	0 代表成功
                status = data.response.status
                if (status === "success") {

                    if(callback){
                        eval(callback+"('"+ data.response.fjcfbDto.fjid+"')");
                    }
                } else {
                    // 失败回调
                    // 清除当前的预览图 ，并隐藏 【移除】 按钮
                    $(event.target)
                        .fileinput('clear')
                        .fileinput('unlock');
                    if(singleFlg && singleFlg==1){
                        $(event.target)
                            .parent()
                            .siblings('.fileinput-remove')
                            .hide()
                    }
                    // 打开失败的信息弹窗
                    $.toptip("请求失败，请稍后重试!", 'error');
                }
            })
    }
    return oFile;
};

function refreshFj(fjcfbDtos){
    $("#complaintInfo #fjcfbDto").html("");
    var fileHtml = "";
    if(fjcfbDtos != null && fjcfbDtos.length > 0){
        for (var i = 0; i < fjcfbDtos.length; i++) {
            fileHtml += '<div id="'+fjcfbDtos[i].fjid+'" style="font-size: small;">' +
                '	<button title="删除" class="weui-icon-delete" style="color: red;margin-top: -15px" type="button" onclick="del(\''+fjcfbDtos[i].fjid+'\',\''+fjcfbDtos[i].wjlj+'\');"></button>' +
                '	<span style="white-space: nowrap;display: inline-block;overflow: hidden;text-overflow: ellipsis;max-width: 150px">附件'+(i+1)+':'+fjcfbDtos[i].wjm+'</span>' +
                '</div>'
        }
    }
    $("#complaintInfo #fjcfbDto").append(fileHtml);
}

function refreshFile(){
    $("#complaintInfo #fjids").val("");
    $("#complaintInfo .upfile").remove();

    $("#complaintInfo #fileDiv").html("");
    var fileHtml="<input id='wechat_file' name='wechat_file' type='file'>";
    $("#complaintInfo #fileDiv").append(fileHtml);
    //初始化fileinput
    var oFileInput = new FileInput();
    oFileInput.Init("complaintInfo","displayUpInfo",2,1,"wechat_file");
}

function displayUpInfo(fjid){
    if(!$("#complaintInfo #fjids").val()){
        $("#complaintInfo #fjids").val(fjid);
    }else{
        $("#complaintInfo #fjids").val($("#complaintInfo #fjids").val()+","+fjid);
    }
}


//上传图片点击开始
$("#complaintInfo #uploader").on("touchstart",function(){
    $("#uploader img").eq(0).attr("src","/images/addon.png");
})
//上传图片点击结束
$("#complaintInfo #uploader").on("touchend",function(){
    $("#uploader img").eq(0).attr("src","/images/add.png");
})

/**
 * 判断是否为微信内置浏览器
 */
function isFromWeiXin() {
    var ua = window.navigator.userAgent.toLowerCase();
    if(ua.match(/MicroMessenger/i) == 'micromessenger'){
        var system = {
            win: false,
            mac: false
        };
        //检测平台
        var p = navigator.platform;
        system.win = p.indexOf("Win") == 0;
        system.mac = p.indexOf("Mac") == 0;
        // 非微信PC端上打开内置浏览器不做操作
        if (system.win || system.mac) {
            $("#chrom").show();
            $("#phone").hide();
        }
    }else{
        $("#chrom").show();
        $("#phone").hide();
    }
}

function upload(localIds, i){
    var ywlx = $("#ywlx").val();
    wx.uploadImage({
        localId:localIds[i], //需要上传的图片的本地ID,由chooseImage接口获得
        isShowProgressTips:1,   //默认为1，显示进度提示
        success:function (res) {
            var serverId = res.serverId; //返回图片的服务器端ID
            $.ajax({
                url: '/wechat/file/saveTempFile',
                type: 'post',
                dataType: 'json',
                data : {"mediaid" : serverId,"localid" : localIds[i], "ywlx":ywlx,"wbcxdm":$("#wbcxdm").val()},
                success: function(result) {
                    i = i + 1;
                    var localid = result.fjcfbDto.localid;
                    var fjid = result.fjcfbDto.fjid;
                    displayUpInfo(fjid);
                    var u = navigator.userAgent, app = navigator.appVersion;
                    var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //安卓
                    var isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
                    if (isAndroid) {
                        localids.push(localid);
                        var img =  '<div id="'+fjid+'" class="uploadFjForm" padding:0;width: 30%;height: 70px;margin: 0 5px;">'
                            +'<div style="padding:0;border:1px solid grey;">'
                            +'<img onclick="viewfile(event)" style="height: 50px;width: 100%;" src="'+localid+'"/>'
                            +'<img onclick=\'delfile(event,"'+fjid+'");\' data-wjlj="'+localid+'" style="height: 20px;width: 100%;" src="/images/delete.png"/>'
                            +'</div>'
                            +'</div>'
                        $("#uploader").before(img);
                    }else if (isIOS) {
                        wx.getLocalImgData({
                            localId: localid, // 图片的localID
                            success: function (res) {
                                var localData = res.localData; // localData是图片的base64数据，可以用img标签显示
                                localids.push(localData);
                                var img =  '<div id="'+fjid+'" style="padding:0;width: 30%;height: 70px;margin: 0 5px;">'
                                    +'<div style="padding:0;border:1px solid grey;">'
                                    +'<img onclick="viewfile(event)" style="height: 50px;width: 100%;" src="'+localid+'"/>'
                                    +'<img onclick=\'delfile(event,"'+fjid+'");\' data-wjlj="'+localid+'" style="height: 20px;width: 100%;" src="/images/delete.png"/>'
                                    +'</div>'
                                    +'</div>'
                                $("#uploader").before(img);
                            }
                        });
                    }else{
                        localids.push(localid);

                        var img =  '<div id="'+fjid+'" style="padding:0;width: 30%;height: 70px;margin: 0 5px;">'
                            +'<div style="padding:0;border:1px solid grey;">'
                            +'<img onclick="viewfile(event)" style="height: 50px;width: 100%;" src="'+localid+'"/>'
                            +'<img onclick=\'delfile(event,"'+fjid+'");\' data-wjlj="'+localid+'" style="height: 20px;width: 100%;" src="/images/delete.png"/>'
                            +'</div>'
                            +'</div>'
                        $("#uploader").before(img);
                    }
                    if(i < localIds.length){
                        upload(localIds, i);
                    }
                }
            });
        }
    });
}

function viewfile(e){
    wx.previewImage({
        current: e.currentTarget.src, // 当前显示图片的http链接
        urls: localids // 需要预览的图片http链接列表
    });
}

function delfile(e,fjid){
    for(var i = 0; i < localids.length; i++){
        if(e.currentTarget.dataset.wjlj == localids[i]){
            localids.splice(i,1);
            break;
        }
    }
    var str = $("#complaintInfo #fjids").val();
    var arr=str.split(",");
    for(var i=0;i<arr.length;i++){
        if(fjid == arr[i]){
            arr.splice(i,1);
            break;
        }
    }
    var str = arr.join(",");
    $("#complaintInfo #fjids").val(str);
    $("#complaintInfo #"+fjid).remove();
}
function openFjUploadInfo(){
    if($("#discusFjUploadDiv").is(":hidden")){
        showFjInfo();
    }else {
        hideFjInfo()
    }
}

function hideFjInfo(){
    $("#discusFjUploadDiv").hide();
    $("#fjids").val("");
    document.getElementsByClassName("fileinput-remove-button")[0].click();
    $(".uploadFjForm").remove()
}

function showFjInfo(){
    $("#discusFjUploadDiv").show();
}