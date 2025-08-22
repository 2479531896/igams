$(document).ready(function(){
    //0.初始化fileinput
    var oFileInput = new FileInput();
    oFileInput.Init("ajaxForm","displayUpInfo",2,1,"file");
    loadingWX();
});

function displayUpInfo(fjid){
    if(!$("#ajaxForm #fjids").val()){
        $("#ajaxForm #fjids").val(fjid);
    }else{
        $("#ajaxForm #fjids").val($("#ajaxForm #fjids").val()+","+fjid);
    }
}
var target = null;
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
            uploadUrl: "/ws/saveImportFile", //上传的地址
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
                if(fileName == 'file'){
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
                target = event.target
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


/**
 * 获取数据
 */
function getPagInfo(value){
    $.ajax({
        url: '/ws/getPagInfo',
        type:'post',
        dataType:'json',
        data:{
            "nbbm" : value,
        },
        success:function(data){
            if (data.status == 'success'){
                $("#ajaxForm #sjid").val(data.dto.sjid);
                $("#ajaxForm #ybbh").text(data.dto.ybbh);
                $("#ajaxForm #hzxm").text(data.dto.hzxm);
                $("#ajaxForm #nbbm").text(data.dto.nbbm);
                $("#ajaxForm #jcxmmc").text(data.dto.jcxmmc);
                $("#ajaxForm #yblxmc").text(data.dto.yblxmc);
            }else{
                $.alert(data.message);
            }
        }
    })
}


function saveInfo(){
    if ( !$("#ajaxForm #fjids").val()){
        $.alert("附件不能为空");
        return
    }
    if ( !$("#ajaxForm #sjid").val()){
        $.alert("送检信息不能为空");
        return
    }
    $("#ajaxForm #button").attr("disabled");
    $.ajax({
        url: '/ws/savePageInfo',
        type:'post',
        dataType:'json',
        data:{
            "sjid" : $("#ajaxForm #sjid").val(),
            "fjids" : $("#ajaxForm #fjids").val(),
        },
        success:function(data){
            $.alert("文件上传成功！",function(result){
                $("#ajaxForm #button").removeAttr("disabled");
                $(target)
                    .fileinput('clear')
                    .fileinput('unlock');
                $(target)
                    .parent()
                    .siblings('.fileinput-remove')
                    .hide()
                $("#ajaxForm #sjid").val("");
                $("#ajaxForm #ybbh").text("");
                $("#ajaxForm #hzxm").text("");
                $("#ajaxForm #nbbm").text("");
                $("#ajaxForm #jcxmmc").text("");
                $("#ajaxForm #yblxmc").text("");
                $("#ajaxForm #fjids").val("");
                openScan();
            });
        }
    })
}



function loadingWX (){
    //初始化微信信息
    $.ajax({
        url: '/wechat/getJsApiInfo',
        type: 'post',
        data: {
            "url": location.href,
            "wbcxdm": $("#ajaxForm #wbcxdm").val()
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
                jsApiList: ['checkJsApi','scanQRCode']
                // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
            });
            wx.error(function(res) {
                console.log(res);
            });
            //config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作
            wx.ready(function(res) {
                wx.checkJsApi({
                    jsApiList: ['scanQRCode'],
                    success: function(res) {
                       console.log(JSON.stringify(res))
                    }
                });
            });
        }
    });
}

function openScan(){
    //扫码事件
    wx.scanQRCode({
        needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果
        scanType: ["qrCode", "barCode"],
        success: function(res) {
            getPagInfo(res.resultStr);
        },
        fail: function(res) {
            $.toptip(JSON.stringify(res), 'error');
        }
    });
}