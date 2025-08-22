var ywid = $("#ywid").val();
var wxid = $("#wxid").val();
var wbcxdm = $("#wbcxdm").val();
var ycqf = $("#ycqf").val();
var yclxList = [];
var xzyclxList = [];
var yczlxList = [];
var xzyczlxList = [];
var localids=[];

$(function(){

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
    dealBasicData();
});

/**
 * 查看历史投诉
 */
function complaintHistory(){

}

function dealBasicData(){
    if (ycqf){
        $.ajax({
            url:'/wechat/inspComplaintBasicData',
            type:'post',
            dataType:'json',
            data:{},
            success:function(data){
                if (data.yclxList && data.yclxList.length>0){
                    for (var i=0;i<data.yclxList.length;i++){
                        if (data.yclxList[i].fcsid == ycqf){
                            yclxList.push(data.yclxList[i])
                            var lsyclx = {label:data.yclxList[i].csmc,value:data.yclxList[i].csid};
                            xzyclxList.push(lsyclx)
                        }
                    }
                }
                yczlxList = data.yczlxList;
                dealYczlxBasciData()
            },
            error : function(data) {
                $.toptip(JSON.stringify(date), 'error');
            }
        })
    }
}

/**
 * 选择投诉类型
 */
function chooseYclx(){
    setTimeout(function(){
        weui.picker(
            xzyclxList, {
                defaultValue: [$("#lx").val()?$("#lx").val():xzyclxList[0].value],
                onChange: function (result) {
                    console.log(result);
                },
                onConfirm: function (result) {
                    $("#lxmc").val(result[0].label);
                    $("#lx").val(result[0].value);
                    dealYczlxBasciData()
                },
                title: '请选择投诉类型',
                id: 'chooseYclx'
            });
    }, 200);
}

/**
 * 处理投诉子类型数据以及判断显示隐藏
 */
function dealYczlxBasciData(){
    var yclx = $("#lx").val();
    xzyczlxList = [];
    if (yclx && yczlxList && yczlxList.length>0){
        for (let i = 0; i < yczlxList.length; i++) {
            if (yczlxList[i].fcsid == yclx){
                var lsyczlx = {label:yczlxList[i].csmc,value:yczlxList[i].csid};
                xzyczlxList.push(lsyczlx)
            }
        }
    }
    if (xzyczlxList && xzyczlxList.length>0){
        $("#yczlxDiv").show();
    }else {
        $("#yczlxDiv").hide();
    }
}

/**
 * 选择投诉类型
 */
function chooseYczlx(){
    setTimeout(function(){
        weui.picker(
            xzyczlxList, {
                defaultValue: [$("#zlx").val()?$("#zlx").val():xzyczlxList[0].value],
                onChange: function (result) {
                    console.log(result);
                },
                onConfirm: function (result) {
                    $("#zlxmc").val(result[0].label);
                    $("#zlx").val(result[0].value);
                },
                title: '请选择投诉子类型',
                id: 'chooseYczlx'
            });
    }, 200);
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
                        var img =  '<div id="'+fjid+'" style="padding:0;width: 30%;height: 70px;margin: 0 5px;">'
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

/**
 * 取消，返回上一页面
 */
function back(){
    if ($("#backpage").val() && "reportList" == $("#backpage").val()) {
        window.history.back();
    }else {
        var url = "/wechat/inspComplaint?wxid=" + $("#wxid").val() + "&wbcxdm=" + $("#wbcxdm").val() + "&ywid=" + ywid;
        window.location.replace(url);
    }
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
 * 投诉保存事件
 */
function sjycSave(){
    $("#save").attr("disabled","true");
    $("#back").attr("disabled","true");
    if (!$("#ycbt").val()){
        $.toptip("请输入投诉标题！", 'error');
        $("#save").removeAttr("disabled");
        $("#back").removeAttr("disabled");
        return;
    }
    if (!$("#lx").val()){
        $.toptip("请选择投诉类型！", 'error');
        $("#save").removeAttr("disabled");
        $("#back").removeAttr("disabled");
        return;
    }
    if (!$("#yczlxDiv").is(":hidden") && !$("#zlx").val()){
        $.toptip("请选择投诉子类型！", 'error');
        $("#save").removeAttr("disabled");
        $("#back").removeAttr("disabled");
        return;
    }
    if (!$("#ycxx").val()){
        $.toptip("请输入投诉内容！", 'error');
        $("#save").removeAttr("disabled");
        $("#back").removeAttr("disabled");
        return;
    }
    var lsbys = ""
    var bysArr = document.getElementsByName("bys");
    for (var i = 0; i < bysArr.length; i++) {
        if (bysArr[i].checked) {
            lsbys += ","+bysArr[i].value;
        }
    }
    if(lsbys==""){
        $.toptip("请选择异常类型！", 'error');
        return;
    }
    showloading("正在保存...");
    $.ajax({
        url:'/wechat/inspComplaintSave',
        type:'post',
        dataType:'json',
        data:{
            fjids:$("#fjids").val(),
            ycbt:$("#ycbt").val(),
            ywid:$("#ywid").val(),
            lx:$("#lx").val(),
            zlx:$("#zlx").val(),
            ycxx:$("#ycxx").val(),
            twr:$("#wxid").val(),
            wxid:$("#wxid").val(),
            qrr:$("#qrjs").val(),
            qrlx:$("#qrlx").val(),
            ycqf:$("#ycqf").val(),
            jcdw:$("#jcdw").val(),
            sjycstatisticsids:lsbys
        },
        success:function(data){
            if (data && data.status == 'success'){
                hideloading();
                $.toptip("保存成功！", 'success');
                back();
            }else {
                hideloading();
                $.toptip("保存失败！"+JSON.stringify(data),'error');
            }

        },
        error : function(data) {
            $.toptip(JSON.stringify(date), 'error');
        }
    })

}
