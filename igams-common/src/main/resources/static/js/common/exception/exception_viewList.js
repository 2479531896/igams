var loadflag = false;
var listCount = 5;
var listStrat = 0;
var ycPageNumber = 0;
var ycPageSize = 5;
var loadMoreFlag = true;
var clickflag=true;
//发送 回复等修改权限
var modqx=$("#exceptionViewListForm #modqx").val();
var sjycs="";
// 获取反馈内容(评论内容)
function getFknr(ycid,index) {
    if ($("#look"+ycid).hasClass("closed") == true) {
        $("#look"+ycid).addClass("open");
        $("#look"+ycid).removeClass("closed");
        loadMoreFknr(ycid,loadMoreFlag,ycPageSize,ycPageNumber,index)
    }
    else {
        sq(ycid);
        $("#look"+ycid).removeClass("open");
        $("#look"+ycid).addClass("closed");
    }

}

//缩回评论内容
function sq(ycid) {
    $("#YC" + ycid).empty();
    $("#FS" + ycid).empty();
}

function loadMoreFknr(ycid,loadFlag,pageSize,pageNumber,index){
    if (loadFlag) {
        pageNumber = parseInt(pageNumber)+1
        $.ajax({
            type: 'post',
            url: $("#exceptionViewListForm #urlPrefix").val()+"/exception/exception/pagedataExceptionFeedback",
            cache: false,
            data: {"ycid": ycid, "access_token": $("#ac_tk").val(),"pageSize":pageSize,"pageNumber":pageNumber},
            dataType: 'json',
            success: function (data) {
                // 返回值
                var button = document.getElementById("loadMoreButton")
                if (button) {
                    button.parentNode.removeChild(button)
                }
                var Html = $("#YC"+ycid).html();
                if (data.ycfklist != null && data.ycfklist.length > 0) {
                    for (var i = 0; i < data.ycfklist.length; i++) {
                        if (!data.ycfklist[i].ffkid) {
                            Html += "<div  class='col-sm-12 col-md-12 col-xs-12 text-left' style='padding:5px;background-color:#e6f7ff;border:3px solid #7acaff;" + (i == 0 && !button ? "" : "border-top:0;") + "'>" +
                                "<div class='col-sm-12 col-md-12 col-xs-12 text-left' style='padding: 0'>" +
                                "<span style='color:#339b53'>" + (data.ycfklist[i].fkrymc?data.ycfklist[i].fkrymc:data.ycfklist[i].lrrymc) + "</span>" +
                                "<span>　</span>" +
                                "<span style='color:#8b8b8b'>" + data.ycfklist[i].hfsj + "　" + computingTime(data.ycfklist[i].hfsj) + "　</span>"
                            // Html +=     "<button type='button' style='border:1px solid #22B8DD;border-radius:3px;background-color:white;color:#22B8DD;margin: 2px;float: right;' onclick=\"fs(\'" + ycid + "\',\'" + data.ycfklist[i].fkid + "\',\'" + data.ycfklist[i].fkry + "')\">回复</button>";
                            Html += '<button id="button_moreFk_' + data.ycfklist[i].gid + '" type="button" style="color:#8b8b8b;float: right;" onclick="viewMoreFfk(\'' + data.ycfklist[i].gid + '\')">展开更多回复(<span id="moreFk_Num' + data.ycfklist[i].gid + '">' + data.ycfklist[i].ghfcs + '</span>)</button>';
                            Html += '<button id="button_moreFk_Hide' + data.ycfklist[i].gid + '" class="hidden" type="button" style="color:#8b8b8b;float: right;" onclick="hideMoreFfk(\'' + data.ycfklist[i].gid + '\')">缩回回复</button>';
                            if (data.ycfklist[i].sfscfj == true) {
                                Html += "<button type='button' style='color:#22B8DD;margin: 0 2px;float: right;' onclick=\"sc(\'" + data.ycfklist[i].fkid + "\',\'" + "1" + "\')\"><i class='glyphicon glyphicon-folder-open'></i>　查看附件(" + data.ycfklist[i].fjs + ")</button>";
                            }
                            Html += "<div class='col-sm-12 col-md-12 col-xs-12 text-left'>" +
                                "<span>" + (data.ycfklist[i].fkxx==null?"":data.ycfklist[i].fkxx) + "</span>" +
                                "</div>" +
                                "</div>" +
                                "<div id='fkplList_" + data.ycfklist[i].gid + "'></div>"
                            /*                            for (var j = 0; j < data.ycfklist.length; j++) {
                                                            var index = 0
                                                            if (data.ycfklist[j].ffkid == data.ycfklist[i].fkid) {
                                                                Html += "<div class='col-sm-12 col-md-12 col-xs-12 text-left' style='border-bottom: 1px solid #d9d9d9;"+(index==0?'border-top: 1px solid #d9d9d9':'')+"'>" +
                                                                    "<div class='col-sm-12 col-md-12 col-xs-12 text-left'>" +
                                                                    "<span style='color:#339b53'>" + data.ycfklist[j].fkrymc + "</span>" +
                                                                    "<span style='color:#8b8b8b'>回复</span>" +
                                                                    "<span style='color:#339b53'>　@" + data.ycfklist[j].qrrmc + "　</span>" +附件
                                                                    "<span style='color:#8b8b8b'>" + data.ycfklist[j].hfsj + "　" + computingTime(data.ycfklist[j].hfsj) + "　</span>" +
                                                                    // "<span style='color:#8b8b8b;' onclick=\"hf(\'" + data.ycfklist[j].fkid + "\',\'" + data.ycfklist[j].fkrymc + "\',\'" + data.ycfklist[j].fkry + "')\">回复</span>"+
                                                                    "</div>" +
                                                                    "<div class='col-sm-12 col-md-12 col-xs-12 text-left'>" +
                                                                    "<span>" + data.ycfklist[j].fkxx + "</span>"+
                                                                    "</div>" +
                                                                    "</div>";
                                                                index++
                                                            }
                                                        }*/
                            if (modqx == "1"&&data.ycfklist[i].sfjs!='1') {
                                Html += '<div class="col-md-10 col-sm-10 col-xs-10 text-left" style="padding-top: 5px">' +
                                    '<input placeholder="请输入您要回复的内容..." autocomplete="off" type="text" id="hfxx' + data.ycfklist[i].fkid + '" name="hfxx" class="form-control" style="height:30px;" data-provide="typeahead" onkeydown="if(event.keyCode==13) {fs(\'' + ycid + '\',\'' + data.ycfklist[i].fkid + '\',\'' + data.ycfklist[i].gid + '\',\'' + data.ycfklist[i].fkry + '\'); return false;}"></input>' +
                                    '</div>' +
                                    '<div class="col-md-2 col-sm-2 col-xs-2 text-left" style="padding:0;padding-top: 8px">' +
                                    "<button type='button' style='color:#8b8b8b;' onclick=\"fs(\'" + ycid + "\',\'" + data.ycfklist[i].fkid + "\',\'" + data.ycfklist[i].gid + "\',\'" + data.ycfklist[i].fkry + "')\">回复</button>" +
                                    '</div>'
                            }
                        }
                        Html += "</div>" +
                            "</div>";
                    }
                }
                var isLoadMore = data.ycfklist != null && data.ycfklist.length<5
                Html += '<button id="loadMoreButton" type="button" class="col-sm-12 col-md-12 col-xs-12" onclick="loadMoreFknr(\''+ycid+'\','+!isLoadMore+',\''+pageSize+'\',\''+pageNumber+'\',\''+index+'\')">'+(isLoadMore?"无更多反馈":"加载更多")+'</button>';
                $("#YC" + ycid).empty();
                $("#YC" + ycid).append(Html);
                // var h=0;
                // debugger
                // if(listStrat!=5){
                //     for (var i = 0; i <=index ; i++) {
                //         h += $("#" + sjycs[i].ycid).height()
                //     }
                // }
                //
                // document.getElementsByClassName("bootbox-body")[0].scrollTop=(h-50);

                $("#exceptionViewListForm [name='hfxx']").typeahead({
                    source : function(query, process) {
                        return $.ajax({
                            url : $("#exceptionViewListForm #urlPrefix").val()+'/exception/exception/pagedataSelectUser',
                            type : 'post',
                            data : {
                                "zsxm" : query,
                                "access_token" : $("#ac_tk").val()
                            },
                            dataType : 'json',
                            success : function(result) {
                                var resultList = result.f_gzglDtos
                                    .map(function(item) {
                                        var aItem = {
                                            name : '('+item.yhm+item.zsxm+')'
                                        };
                                        return JSON.stringify(aItem);
                                    });
                                return process(resultList);
                            }
                        });
                    },
                    matcher : function(obj) {
                        return true;
                    },
                    sorter : function(items) {
                        var beginswith = [], caseSensitive = [], caseInsensitive = [], item;
                        while (aItem = items.shift()) {
                            var item = JSON.parse(aItem);
                            if (!item.name.toLowerCase().indexOf(
                                this.query.toLowerCase()))
                                beginswith.push(JSON.stringify(item));
                            else if (~item.name.indexOf(this.query))
                                caseSensitive.push(JSON.stringify(item));
                            else
                                caseInsensitive.push(JSON.stringify(item));
                        }
                        return beginswith.concat(caseSensitive,
                            caseInsensitive)
                    },
                    highlighter : function(obj) {
                        var item = JSON.parse(obj);
                        var query = this.query.replace(
                            /[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&')
                        return item.name.replace(new RegExp('(' + query
                            + ')', 'ig'), function($1, match) {
                            return '<strong>' + match + '</strong>'
                        })
                    },
                    updater : function(obj) {
                        var item = JSON.parse(obj);
                        var index= this.query.lastIndexOf("@");
                        var val=this.query.substr(0,index+1);
                        return val+item.name+" ";
                    }
                });
            }
        });
    }
}

/**
 * 获取反馈评论
 * @param gid
 */
function viewMoreFfk(gid) {
    if (parseInt($("#moreFk_Num" + gid).text()) > 0) {
        $("#button_moreFk_"+gid).hide();
        $("#button_moreFk_Hide"+gid).removeClass("hidden");
        $.ajax({
            type: 'post',
            url: $("#exceptionViewListForm #urlPrefix").val()+"/ws/exception/viewMoreDiscuss",
            cache: false,
            data: {"gid": gid},
            dataType: 'json',
            success: function (data) {
                if (data.t_sjycfklist && data.t_sjycfklist.length>0) {
                    var plhtml = "";
                    var index = 0;
                    for (var i = 0; i < data.t_sjycfklist.length; i++) {
                        if (data.t_sjycfklist[i].ffkid) {
                            plhtml += "<div class='col-sm-12 col-md-12 col-xs-12 text-left' style='border-bottom: 1px solid #d9d9d9;"+(index==0?'border-top: 1px solid #d9d9d9':'')+"'>" +
                                "<div class='col-sm-12 col-md-12 col-xs-12 text-left'>" +
                                "<span style='color:#339b53'>" + data.t_sjycfklist[i].fkrymc + "　" + "</span>" +
                                /*"<span style='color:#8b8b8b'>回复</span>" +
                                "<span style='color:#339b53'>　@" + data.t_sjycfklist[i].qrrmc + "　</span>" +*/
                                "<span style='color:#8b8b8b'>" + data.t_sjycfklist[i].hfsj + "　" + computingTime(data.t_sjycfklist[i].hfsj) + "　</span>" +
                                // "<span style='color:#8b8b8b;' onclick=\"hf(\'" + data.ycfklist[j].fkid + "\',\'" + data.ycfklist[j].fkrymc + "\',\'" + data.ycfklist[j].fkry + "')\">回复</span>"+
                                "</div>" +
                                "<div class='col-sm-12 col-md-12 col-xs-12 text-left'>" +
                                "<span>" + data.t_sjycfklist[i].fkxx + "</span>"+
                                "</div>" +
                                "</div>";
                            index++
                        }
                    }
                    $("#fkplList_"+gid).html(plhtml)
                }
            }
        })
    }
}

function hideMoreFfk(gid) {
    $("#fkplList_"+gid).html("");
    $("#button_moreFk_"+gid).show();
    $("#button_moreFk_Hide"+gid).addClass("hidden");
}
// 上传附件
function sc(fkid, ckbj, ycid) {
    var fkfjids = $("#exceptionViewListForm #fkfjids"+ycid).val();
    var url = $("#exceptionViewListForm #urlPrefix").val()+"/exception/exception/pagedataSendFilePage?access_token=" + $("#ac_tk").val() + "&ycid=" + ycid;
    if (fkfjids != null && fkfjids != '') {
        url = url + "&fjids=" + fkfjids;
    }
    if (fkid != null && fkid != '') {
        url = url + "&fkid=" + fkid;
    }
    if (ckbj != null && ckbj != '') {
        url = url + "&ckbj=" + ckbj;
    }
    $.showDialog(url, '上传附件', uploadFileConfig);
}

//附件上传功能框
var uploadFileConfig = {
    width: "800px",
    height: "500px",
    modalName: "sendFileModal",
    formName: "sendFileForm",
    offAtOnce: true, // 当数据提交成功，立刻关闭窗口
    buttons: {
        success: {
            label: "确 定",
            className: "btn-primary",
            callback: function () {
                var $this = this;
                var opts = $this["options"] || {};
                var fjids = $("#sendFileForm #fjids").val()?$("#sendFileForm #fjids").val():"";
                var ycid = $("#sendFileForm #ycid").val();
                var oldfjids = $("#exceptionViewListForm #fkfjids"+ycid).val();
                if (oldfjids) {
                    fjids = oldfjids+(fjids?(','+fjids):"");
                }
                $("#exceptionViewListForm #fkfjids"+ycid).val(fjids);
                $.ajax({
                    type: 'post',
                    url: $("#exceptionViewListForm #urlPrefix").val()+"/exception/exception/pagedataLsFile",
                    cache: false,
                    data: {"fjids": fjids,"access_token": $("#ac_tk").val()},
                    dataType: 'json',
                    success: function (data) {
                        // 返回值
                        if (data.redisList.length > 0 && data.redisList != null) {
                            var fjs = "";
                            var html =  '<span id="lsfj_'+ycid+'">临时附件:</span>' +
                                '<input class="hidden" id="fjs_'+ycid+'" value="'+fjids+'"></input>';
                            for (var i = 0; i < data.redisList.length; i++) {
                                fjs += ","+ data.redisList[i].fjid;
                                html += '<span id="lsfj_'+data.redisList[i].fjid+'" style="color:#FFA366;border:1px solid #FFA366;background-color:#FFEBDD;border-radius:3px;line-height:200%;margin-left:10px;">' +
                                    '<span style="white-space:nowrap">'+data.redisList[i].wjm+'</span>' +
                                    '<a class="glyphicon glyphicon-remove" onclick="tempDel(\''+data.redisList[i].fjid+'\',\''+ycid+'\')"></a>' +
                                    '</span>'
                            }
                            $("#FJ" + ycid).empty();
                            $("#FJ" + ycid).append(html);
                            $("#fjs_"+ycid).val(fjs.substring(1))
                        }

                    }
                });

                $.closeModal(opts.modalName);
                return false;
            }
        },
        cancel: {
            label: "关 闭",
            className: "btn-default"
        }
    }
};

//删除临时附件
function tempDel(fjid,ycid){
    $("#lsfj_"+fjid).remove()
    var split = $("#fjs_"+ycid).val().split(",");
    var newFjs = "";
    for (var i = 0; i < split.length; i++) {
        if (split[i]!=fjid) {
            newFjs += ","+split[i];
        }
    }
    $("#fjs_"+ycid).val(newFjs.substring(1))
    if($("#FJ"+ycid+" span").size()==1){
        $("#lsfj_"+ycid).remove() ;
    }
    var fkfjidList = $("#exceptionViewListForm #fkfjids"+ycid).val().split(",");
    var fkfjids = "";
    for (var i = 0; i < fkfjidList.length; i++) {
        if (fkfjidList[i] != fjid){
            fkfjids += ','+ fkfjidList[i]
        }
    }
    $("#exceptionViewListForm #fkfjids"+ycid).val(fkfjids.substring(1))
    $("#upload_fj"+fjid).remove();
    if($(".upload_fj").size()==0){
        $("#lsfjList").remove();
    }

}

function fsfk(ycid,fkry){
    //防止重复触发
    if(clickflag){
        clickflag=false;
        $("#FJ" + ycid).empty();
        var fkfjids = $("#exceptionViewListForm #fkfjids"+ycid).val();
        var fkxx = $("#fkxx_"+ycid).val();
        if ((fkxx == null || fkxx == '')&&(fkfjids == null || fkfjids == '')) {
            clickflag=true;
            $.confirm("请输入反馈信息!");
        } else {
            $.ajax({
                type: 'post',
                url: $("#exceptionViewListForm #urlPrefix").val()+"/exception/exception/pagedataSaveExceptionFk",
                data: {
                    "ycid": ycid,
                    "qrr": fkry,
                    "fkxx": fkxx==null?"":fkxx,
                    "fjids": fkfjids,
                    "fjbcbj": 'local',
                    "access_token": $("#ac_tk").val()
                },
                dataType: 'json',
                success: function (data) {
                    // 返回值
                    if (data.status) {
                        $("#look").addClass("closed");
                        $("#look").removeClass("open");
                        // 清空输入内容
                        $("#fkxx_"+ycid).val("");
                        $("#look"+ycid+" span").text(parseInt($("#look"+ycid+" span").text())+1)
                    }
                    $("#exceptionViewListForm #fkfjids"+ycid).val("");
                    loadMoreFlag = true
                    ycPageNumber =-1
                    $("#YC"+ycid).html("")
                    loadMoreFknr(ycid,loadMoreFlag,ycPageSize,ycPageNumber)
                    clickflag=true;
                },
                error: function (e) {
                    clickflag=true;
                    $.alert(JSON.stringify(e));
                }
            });
        }
    }
}
// 发送评论
function fs(ycid, fkid,gid, fkry) {
    //防止重复触发
    if(clickflag){
        clickflag=false;
        $("#FJ" + ycid).empty();
        var fkfjids = $("#exceptionViewListForm #fkfjids"+ycid).val();
        var fkxx = $("#fkxx_"+ycid).val();
        if (fkid){
            fkxx = $("#hfxx"+fkid).val()
        }
        if (fkxx == null || fkxx == '') {
            clickflag=true;
            $.confirm("请输入反馈信息!");
        } else {
            $.ajax({
                type: 'post',
                url: $("#exceptionViewListForm #urlPrefix").val()+"/exception/exception/pagedataSaveExceptionFk",
                data: {
                    "ycid": ycid,
                    "ffkid": fkid,
                    "gid": gid,
                    "qrr": fkry,
                    "fkxx": fkxx,
                    "fjids": fkfjids,
                    "fjbcbj": 'local',
                    "access_token": $("#ac_tk").val()
                },
                dataType: 'json',
                success: function (data) {
                    // 返回值
                    if (data.status) {
                        $("#look").addClass("closed");
                        $("#look").removeClass("open");
                        // 清空输入内容
                        if (fkid){
                            $("#hfxx"+fkid).val("")
                        }else {
                            $("#fkxx_"+ycid).val("");
                        }
                        // $("#look"+ycid+" span").text(parseInt($("#look"+ycid+" span").text())+1)
                        $("#moreFk_Num"+gid).text(parseInt($("#moreFk_Num" + gid).text())+1)
                        $("#button_moreFk_"+gid).show()
                        viewMoreFfk(gid)
                    }
                    $("#exceptionViewListForm #fkfjids"+ycid).val("");
                    clickflag=true;
                },
                error: function (e) {
                    clickflag=true;
                    $.alert(JSON.stringify(e));
                }
            });
        }
    }

}

// 计算时间
function computingTime(date1) {
    var time = '';
    var date2 = new Date();    // 当前系统时间
    var date3 = date2.getTime() - new Date(date1).getTime();   // 时间差的毫秒数

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


// 点击文件上传
function editfile() {
    $("#fileDiv").show();
    $("#file_btn").hide();
}

// 点击隐藏文件上传
function cancelfile() {
    $("#fileDiv").hide();
    $("#file_btn").show();
}

// 附件预览
function view(fjid) {
    var url = $("#exceptionViewListForm #urlPrefix").val()+"/common/file/pdfPreview?fjid=" + fjid;
    $.showDialog(url, '文件预览', viewProTaskConfig);
}

// 附件下载
function xz(fjid) {
    jQuery('<form action="'+$("#exceptionViewListForm #urlPrefix").val()+'/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
        '<input type="text" name="fjid" value="' + fjid + '">' +
        '<input type="text" name="access_token" value="' + $("#ac_tk").val() + '">' +
        '</form>')
        .appendTo('body').submit().remove();
}

// 附件删除
function del(fjid, wjlj) {
    $.confirm('您确定要删除所选择的记录吗？', function (result) {
        if (result) {
            jQuery.ajaxSetup({async: false});
            var url = $("#exceptionViewListForm #urlPrefix").val()+"/common/file/delFile";
            jQuery.post(url, {fjid: fjid, wjlj: wjlj, "access_token": $("#ac_tk").val()}, function (responseText) {
                setTimeout(function () {
                    if (responseText["status"] == 'success') {
                        $.success(responseText["message"], function () {
                            $("#" + fjid).remove();
                        });
                    } else if (responseText["status"] == "fail") {
                        $.error(responseText["message"], function () {
                        });
                    } else {
                        $.alert(responseText["message"], function () {
                        });
                    }
                }, 1);
            }, 'json');
            jQuery.ajaxSetup({async: true});
        }
    });
}

function displayUpInfo(fjid) {
    if (!$("#exceptionViewListForm #fjids").val()) {
        $("#exceptionViewListForm #fjids").val(fjid);
    } else {
        $("#exceptionViewListForm #fjids").val($("#exceptionViewListForm #fjids").val() + "," + fjid);
    }
}


//搜索
function searchYcxxList(){
    //初始化页码
    listStrat = 0;
    //清空异常信息列表
    $("#exceptionViewListForm #sjycInfoList").empty();
    getYcxxList();
}

//查询
function getYcxxList(){
    loadflag = false;
    $.ajax({
        type: 'post',
        url: $("#exceptionViewListForm #urlPrefix").val()+"/exception/exception/pagedataMoreException",
        cache: false,
        data: {
            "sjid": $("#exceptionViewListForm #sjid").val(),
            "sjids": $("#exceptionViewListForm #sjids").val()?$("#exceptionViewListForm #sjids").val().substring(1,$("#exceptionViewListForm #sjids").val().length-1).replace(/\s+/g, ""):'',
            "ycid": $("#exceptionViewListForm #ycid").val(),
            "entire": $("#exceptionViewListForm #ycxxCxnr").val(),
            "count":listCount,
            "start":listStrat,
            "access_token": $("#ac_tk").val()
        },
        dataType: 'json',
        success: function (data) {
            var sjycxxlist = data.sjyclist
            sjycs=data.sjyclist;
            if (sjycxxlist.length>0){
                var html = '';
                for (var i = 0; i < sjycxxlist.length; i++) {
                    var sjyc = sjycxxlist[i];
                    html += '<div id="' + sjyc.ycid + '" class="col-md-12 col-sm-12 col-xs-12 viewException" style="margin-bottom:15px;padding:5px;border:' + (sjyc.sfzd != "1" ? "1px solid #ddd" : "4px solid #FFA366") + '; border-radius:5px;display:block;">' +
                        '<input type="hidden" id="fkfjids' + sjyc.ycid + '" name="fkfjids' + sjyc.ycid + '">\n' +
                        '<div style="padding-top:5px;width:100%;">' +
                        '<div class="col-md-7  col-sm-7 col-xs-7 text-left" style="padding-right:0px;">' +
                        '<span style="float: left;font-weight:bold;font-size:20px;">' + sjyc.ycbt + '</span>';
                    html += '</div>' +
                        '<div class="col-md-5  col-sm-5 col-xs-5 text-right">';
                    if (sjyc.sfjs == "1") {
                        html += '<span style="color:#66FF85;border:1px solid #66FF85;background-color:#EEFFF1;border-radius:3px;line-height:200%;margin-left:10px;">&nbsp已结束&nbsp</span>';
                    } else if (sjyc.sfjs != '1') {
                        html += '<span style="color:#FFA366;border:1px solid #FFA366;background-color:#FFEBDD;border-radius:3px;line-height:200%;margin-left:10px;">&nbsp未结束&nbsp</span>';
                    }

                    if (sjyc.fjs > 0) {
                        html += '<button type="button" style="border:1px solid #22B8DD;border-radius:3px;background-color:white;color:#22B8DD;margin: 2px;" onclick="sc(\'' + sjyc.ycid + '\',1)"><i class=\'glyphicon glyphicon-folder-open\'></i>　查看附件(' + sjyc.fjs + ')</button>';
                    }
                    if (sjyc.sfzd == "1"&&sjyc.sfjs!='1') {
                        html += '<button type="button" style="border:1px solid #FFA366;border-radius:3px;background-color:#FFEBDD;color:#FFA366;margin: 2px;" onclick="toTop(\'' + sjyc.ycid + '\',\'' + sjyc.sfzd + '\')"><i class="glyphicon glyphicon-pushpin"></i> 取消置顶</button>';
                    } else if (sjyc.sfzd != "1"&&sjyc.sfjs!='1') {
                        html += '<button type="button" style="border:1px solid #22B8DD;border-radius:3px;background-color:white;color:#22B8DD;margin: 2px;" onclick="toTop(\'' + sjyc.ycid + '\',\'' + sjyc.sfzd + '\')"><i class="glyphicon glyphicon-pushpin"></i> 置顶</button>';
                    }
                    if (($("#exceptionViewListForm #yhid").val() == sjyc.qrr||$("#exceptionViewListForm #jsid").val() == sjyc.qrr)&&sjyc.sfjs!='1'){
                        html += '<button type="button" style="border:1px solid #22B8DD;border-radius:3px;background-color:white;color:#22B8DD;margin: 2px;" onclick="repeat(\'' + sjyc.ycid + '\')"><i class="glyphicon glyphicon-share-alt"></i> 转发</button>';
                    }
                    if ($("#exceptionViewListForm #yhid").val() == sjyc.twr&&sjyc.sfjs!='1') {
                        html += '<button type="button" style="border:1px solid #7AA7EB;border-radius:3px;background-color:white;color:#7AA7EB;margin: 2px;" onclick="finish(\'' + sjyc.ycid + '\')"><i class="glyphicon glyphicon-ok"></i> 结束</button>';
                    }
                    html += '</div>' +
                        '</div>' +
                        '<div class="col-md-12 col-sm-12 col-xs-12">' +
                        '<div class="col-md-12 col-sm-12 col-xs-12" style="padding:5px;">' +
                        '<div class="col-md-6 col-sm-6 col-xs-6" style="padding:0px;">' +
                        '<div class="col-md-4 col-sm-4 col-xs-4" style="padding:0px;">' +
                        '<span style="color:#777">异常类型：</span>' +
                        '</div>' +
                        '<div class="col-md-8 col-sm-8 col-xs-8" style="padding:0px;">' +
                        '<span style="color:#777">' + sjyc.lxmc + '</span>' +
                        '</div>' +
                        '</div>';
                    if (sjyc.zlxmc != null && sjyc.zlxmc != '') {
                        html += '<div if="' + (sjyc.zlxmc != null && sjyc.zlxmc != '') + '" class="col-md-6 col-sm-6 col-xs-6" style="padding:0px;">' +
                            '<div class="col-sm-4 col-xs-4 col-xs-4 " style="padding:0px;">' +
                            '<span style="color:#777">子&nbsp;&nbsp;类&nbsp;&nbsp;型：</span>' +
                            '</div>' +
                            '<div class="col-md-8 col-sm-8 col-xs-8" style="padding:0px;">' +
                            '<span style="color:#777">' + sjyc.zlxmc + '</span>' +
                            '</div>' +
                            '</div>';
                    }
                    if (sjyc.ybbh != null && sjyc.ybbh != '') {
                        html += '<div class="col-md-6 col-sm-6 col-xs-6" style="padding:0px;">' +
                            '<div class="col-sm-4 col-xs-4 col-xs-4 " style="padding:0px;">' +
                            '<span style="color:#777">标本编号：</span>' +
                            '</div>' +
                            '<div class="col-md-8 col-sm-8 col-xs-8" style="padding:0px;">' + '<a>' +
                            '<span style="color:#3E9EE1" onclick="viewsjxx(\''+sjyc.sjid+'\')">' + sjyc.ybbh + '</span>' + '</a>' +
                            '</div>' +
                            '</div>';
                    }
                    if (sjyc.hzxm != null && sjyc.hzxm != '') {
                        html += '<div class="col-md-6 col-sm-6 col-xs-6" style="padding:0px;">' +
                            '<div class="col-sm-4 col-xs-4 col-xs-4 " style="padding:0px;">' +
                            '<span style="color:#777">患者姓名：</span>' +
                            '</div>' +
                            '<div class="col-md-8 col-sm-8 col-xs-8" style="padding:0px;">' +
                            '<span style="color:#777">' + sjyc.hzxm + '</span>' +
                            '</div>' +
                            '</div>';
                    }
                    if (sjyc.jcxmmc != null && sjyc.jcxmmc != '') {
                        html += '<div class="col-md-6 col-sm-6 col-xs-6" style="padding:0px;">' +
                            '<div class="col-sm-4 col-xs-4 col-xs-4 " style="padding:0px;">' +
                            '<span style="color:#777">检测项目：</span>' +
                            '</div>' +
                            '<div class="col-md-8 col-sm-8 col-xs-8" style="padding:0px;">' +
                            '<span style="color:#777">' + sjyc.jcxmmc + '</span>' +
                            '</div>' +
                            '</div>';
                    }
                    if (sjyc.jcdwmc != null && sjyc.jcdwmc != '') {
                        html += '<div class="col-md-6 col-sm-6 col-xs-6" style="padding:0px;">' +
                            '<div class="col-sm-4 col-xs-4 col-xs-4 " style="padding:0px;">' +
                            '<span style="color:#777">检测单位：</span>' +
                            '</div>' +
                            '<div class="col-md-8 col-sm-8 col-xs-8" style="padding:0px;">' +
                            '<span style="color:#777">' + sjyc.jcdwmc + '</span>' +
                            '</div>' +
                            '</div>';
                    }
                    if (sjyc.db != null && sjyc.db != '') {
                        html += '<div class="col-md-6 col-sm-6 col-xs-6" style="padding:0px;">' +
                            '<div class="col-sm-4 col-xs-4 col-xs-4 " style="padding:0px;">' +
                            '<span style="color:#777">合作伙伴：</span>' +
                            '</div>' +
                            '<div class="col-md-8 col-sm-8 col-xs-8" style="padding:0px;">' +
                            '<span style="color:#777">' + sjyc.db + '</span>' +
                            '</div>' +
                            '</div>';
                    }
                    html += '<div class="col-md-6 col-sm-6 col-xs-6" style="padding:0px;">' +
                        '<div class="col-sm-4 col-xs-4 col-xs-4 " style="padding:0px;">' +
                        '<span style="color:#777">确认' + sjyc.qrlxmc + '：</span>' +
                        '</div>' +
                        '<div class="col-md-8 col-sm-8 col-xs-8" style="padding:0px;">' +
                        '<span style="color:#777">' + sjyc.qrrmc + '</span>' +
                        '</div>' +
                        '</div>' +
                        '<div class="col-md-6 col-sm-6 col-xs-6" style="padding:0px;">' +
                        '<div class="col-sm-4 col-xs-4 col-xs-4 " style="padding:0px;">' +
                        '<span style="color:#777">提问人员：</span>' +
                        '</div>' +
                        '<div class="col-md-8 col-sm-8 col-xs-8" style="padding:0px;">' +
                        '<span style="color:#777">' + ((sjyc.twrmc) == null ? '' : sjyc.twrmc) + '</span>' +
                        '</div>' +
                        '</div>' +
                        '<div class="col-md-6 col-sm-6 col-xs-6" style="padding:0px;">' +
                        '<div class="col-sm-4 col-xs-4 col-xs-4 " style="padding:0px;">' +
                        '<span style="color:#777">发&nbsp;&nbsp;布&nbsp;&nbsp;于：</span>' +
                        '</div>' +
                        '<div class="col-md-8 col-sm-8 col-xs-8" style="padding:0px;">' +
                        '<span style="color:#777">' + sjyc.lrsj + '</span>' +
                        '</div>' +
                        '</div>' +
                        
                        '<div class="col-md-12 col-sm-12 col-xs-12" style="padding:0px;">' +
                        '<div class="col-sm-2 col-xs-2 col-xs-2 " style="padding:0px;">' +
                        '<span style="color:#777">异常信息：</span>' +
                        '</div>' +
                        '<div class="col-md-10 col-sm-10 col-xs-10" style="padding:0px;">' +
                        '<span style="color:#77F">' + ((sjyc.ycxx != null && sjyc.ycxx != '') ? sjyc.ycxx : '无') + '</span>' +
                        '</div>' +
                        '</div>' +
                        
                        '<div class="col-md-12 col-sm-12 col-xs-12" style="padding:0px;">' +
                        '<div class="col-sm-2 col-xs-2 col-xs-2 " style="padding:0px;">' +
                        '<span style="color:#777">最后回复时间：</span>' +
                        '</div>' +
                        '<div class="col-md-10 col-sm-10 col-xs-10" style="padding:0px;">' +
                        '<span style="color:#777">' + ((sjyc.zhhfsj) == null ? '' : sjyc.zhhfsj) + '</span>' +
                        '</div>' +
                        '</div>' +
                        '<div class="col-md-12 col-sm-12 col-xs-12" style="padding:0px;">' +
                        '<div class="col-sm-2 col-xs-2 col-xs-2 " style="padding:0px;">' +
                        '<span style="color:#777">最后回复内容：</span>' +
                        '</div>' +
                        '<div class="col-md-10 col-sm-10 col-xs-10" style="padding:0px;">' +
                        '<span style="color:#777">' + ((sjyc.zhhfnr) == null ? '' : sjyc.zhhfnr) + '</span>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '<div style="padding-top:5px;width:100%;">' +
                        '<div class="col-md-12  col-sm-12 col-xs-12 text-left" style="padding-top:15px;">'
                    if (modqx == "1"&&sjyc.sfjs!='1') {
                        html+='<input placeholder="' + '请输入您要反馈的内容...' + '" autocomplete="off" type="text" id="fkxx_' + sjyc.ycid + '" name="fkxx" class="form-control" style="height:30px;" data-provide="typeahead" onkeydown="if(event.keyCode==13) {fsfk(\'' + sjyc.ycid + '\',\'' + sjyc.twr + '\'); return false;}">'
                    }
                    html+='</div>' +
                        '</div>'+
                        '<div style="padding-top:5px;width:100%;">' +
                        '<div class="col-md-2  col-sm-2 col-xs-2 text-left" style="padding-top:15px;padding-right:0px;">' +
                        '<button type="button" class="iconfont icon-huifu closed" id="look'+sjyc.ycid+'" style="" onclick="getFknr(\''+sjyc.ycid+'\',\''+ i + '\')">　<span>'+(sjyc.pls?sjyc.pls:0)+'</span></button>' +
                        '</div>' +
                        '<div class="col-md-7  col-sm-7 col-xs-7 text-right" style="padding-top:15px;">' +
                        '<span id="FJ'+ sjyc.ycid+'"></span>' +
                        '</div>'
                    if (modqx=="1"&&sjyc.sfjs!='1'){
                        html+='<div class="col-md-3  col-sm-3 col-xs-3 text-right" style="padding-top:15px;">' +
                            '<button type="button" style="color:#77C9FF;margin: 2px;" onclick="fsfk(\'' + sjyc.ycid +'\',\''+ sjyc.twr + '\')">发送</button>' +
                            '<button type="button" style="color:#77C9FF;margin: 2px;" onclick="sc(null,null,\''+ sjyc.ycid + '\')"><i class="glyphicon glyphicon-paperclip"></i> 上传</button>' +
                            '</div>'
                    }
                    html+='</div>' +
                        '<div id="YC'+sjyc.ycid+'" class="fk"></div>' +
                        '<div class="col-sm-12 col-md-12" id="FS'+sjyc.ycid+'"></div>' +
                        '</div>'
                }
                $("#exceptionViewListForm #sjycInfoList").append(html);
                for (var i = 0; i < sjycxxlist.length; i++) {
                    getFknr(sjycxxlist[i].ycid,i);
                }
            }
            if (sjycxxlist.length<listCount){
                loadflag = false;
            }else {
                loadflag = true;
            }
            listStrat += sjycxxlist.length
            $("#exceptionViewListForm [name='fkxx']").typeahead({
                source : function(query, process) {
                    return $.ajax({
                        url : $("#exceptionViewListForm #urlPrefix").val()+'/exception/exception/pagedataSelectUser',
                        type : 'post',
                        data : {
                            "zsxm" : query,
                            "access_token" : $("#ac_tk").val()
                        },
                        dataType : 'json',
                        success : function(result) {
                            var resultList = result.f_gzglDtos
                                .map(function(item) {
                                    var aItem = {
                                        name : '('+item.yhm+item.zsxm+')'
                                    };
                                    return JSON.stringify(aItem);
                                });
                            return process(resultList);
                        }
                    });
                },
                matcher : function(obj) {
                    return true;
                },
                sorter : function(items) {
                    var beginswith = [], caseSensitive = [], caseInsensitive = [], item;
                    while (aItem = items.shift()) {
                        var item = JSON.parse(aItem);
                        if (!item.name.toLowerCase().indexOf(
                            this.query.toLowerCase()))
                            beginswith.push(JSON.stringify(item));
                        else if (~item.name.indexOf(this.query))
                            caseSensitive.push(JSON.stringify(item));
                        else
                            caseInsensitive.push(JSON.stringify(item));
                    }
                    return beginswith.concat(caseSensitive,
                        caseInsensitive)
                },
                highlighter : function(obj) {
                    var item = JSON.parse(obj);
                    var query = this.query.replace(
                        /[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&')
                    return item.name.replace(new RegExp('(' + query
                        + ')', 'ig'), function($1, match) {
                        return '<strong>' + match + '</strong>'
                    })
                },
                updater : function(obj) {
                    var item = JSON.parse(obj);
                    var index= this.query.lastIndexOf("@");
                    var val=this.query.substr(0,index+1);
                    return val+item.name+" ";
                }
            });
        }
    });
}
function viewsjxx(sjid) {
    var url=$("#exceptionViewListForm #urlPrefix").val()+"/inspection/inspection/pagedataViewSjxx?sjid="+sjid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'送检详细信息',viewBytConfig);
}
/**
 * 病原体查看页面模态框
 */
var viewBytConfig={
    width		: "1200px",
    height      :"1200px",
    modalName	:"viewBytModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
}

// 结束按钮，改变是否结束标记
var finish_ycid;
function finish(ycid) {
    finish_ycid=ycid;
    var url = $("#exceptionViewListForm #urlPrefix").val()+"/exception/exception/finishException?access_token=" + $("#ac_tk").val()+"&ids="+ycid;
    $.showDialog(url, '异常结束评价', finishExceptionConfig);
}

var finishExceptionConfig = {
    width		: "600px",
    modalName	:"finishExceptionModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#finishExceptionForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#finishExceptionForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"finishExceptionForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                $("#ftabid").empty();
                                // 返回值
                                if("0"==modqx){
                                    searchYcxxList();
                                }else{
                                    $("#exceptionViewListForm").load("/exception/exception/exceptionlistView?ycid=" + finish_ycid);
                                }
                            }
                        });
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {
                        });
                    } else{
                        $.alert(responseText["message"],function() {
                        });
                    }
                },".modal-footer > button");
                return false;
            }
        },

        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

// 转发按钮，选择转发人员或部门
function repeat(ycid) {
    var url = $("#exceptionViewListForm #urlPrefix").val()+"/exception/exception/pagedataChoseRepeatObject?ycid=" + ycid;
    $.showDialog(url, '转发', chooseTzdxConfig);
}



// 转发人员选择框
var chooseTzdxConfig = {
    width: "800px",
    height: "200px",
    modalName: "chooseTzdxModal",
    formName: "repeatObjectForm",
    offAtOnce: true, // 当数据提交成功，立刻关闭窗口
    buttons: {
        success: {
            label: "确 定",
            className: "btn-primary",
            callback: function () {
                if (!$("#repeatObjectForm").valid()) {
                    return false;
                }
                var $this = this;
                var opts = $this["options"] || {};

                $("#repeatObjectForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"] || "repeatObjectForm", function (responseText, statusText) {
                    if (responseText["status"] == 'success') {
                        $.success(responseText["message"], function () {
                            if (opts.offAtOnce) {
                                $.closeModal(opts.modalName);
                            }
                            $("#exceptionViewListForm #sjycInfoList").empty();
                            listCount=5;
                            listStrat=0;
                            getYcxxList();
                        });
                    } else if (responseText["status"] == "fail") {
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"], function () {
                        });
                    } else {
                        $.alert(responseText["message"], function () {
                        });
                    }
                }, ".modal-footer > button");
                return false;
            }
        },
        cancel: {
            label: "关 闭",
            className: "btn-default"
        }
    }
};

// 附件预览框
var viewProTaskConfig = {
    width: "900px",
    height: "800px",
    offAtOnce: true,  // 当数据提交成功，立刻关闭窗口
    buttons: {
        cancel: {
            label: "关 闭",
            className: "btn-default"
        }
    }
};


//置顶
function toTop(ycid,sfzd){
    var lssfzd = 0;
    if (sfzd==1){
        //取消置顶操作
        lssfzd = 0;
    }else {
        //设置置顶
        lssfzd = 1;
    }
    $.ajax({
        type: 'post',
        url: $("#exceptionViewListForm #urlPrefix").val()+"/ws/exception/pagedataTopException",
        cache: false,
        data: {
            "ycid": ycid,
            "sfzd": lssfzd,
            "zdry": $("#exceptionViewListForm #yhid").val(),
            "access_token": $("#ac_tk").val()
        },
        dataType: 'json',
        success: function (data) {
            searchYcxxList()
            console.log(data)
        },
        fail:function (data){
            console.log(data)
        }
    })
}
$(document.getElementsByClassName("bootbox-body")[0]).scroll(function () {
    // scrollTop是滚动条滚动时，距离顶部的距离
    var scrollTop = document.getElementsByClassName("bootbox-body")[0].scrollTop;
    // windowHeight是可视区的高度
    var windowHeight = document.getElementsByClassName("bootbox-body")[0].offsetHeight;
    // scrollHeight是滚动条的总高度
    var scrollHeight = document.getElementsByClassName("bootbox-body")[0].scrollHeight;
    // 滚动条到底部
    if (scrollTop + windowHeight > scrollHeight - 20) {
        // 到了底部之后想做的操作,如到了底部之后加载
        if (loadflag){
            getYcxxList();
        }
    }
});

function addException(){
    if($("#exceptionViewListForm #sjids").val()){
        var sjids = $("#exceptionViewListForm #sjids").val().substring(1,$("#exceptionViewListForm #sjids").val().length-1).replace(/\s+/g, "");
        var url=$("#exceptionViewListForm #urlPrefix").val()+"/exception/exception/addException?sjids="+sjids+"&listflg=0";
    }else{
        var url=$("#exceptionViewListForm #urlPrefix").val()+"/exception/exception/addException?ywid="+$("#exceptionViewListForm #sjid").val()+"&listflg=0";
    }
    $.showDialog(url,'新增异常信息',addExceptionConfig);
}

var addExceptionConfig = {
    width		: "800px",
    modalName	:"addExceptionModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#editExceptionForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var tzrys=$("#tzrys").val();
                // if(tzrys.length<=0){
                //     $.confirm("通知人员不能为空");
                //     return false;
                // }
                $("#editExceptionForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"editExceptionForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                if ($("#editExceptionForm #key").val()){
                                    $.ajax({
                                        url: $("#editExceptionForm #urlPrefix").val()+"/sseEmit/connect/pagedataCloseOA",
                                        data: {
                                            "access_token": $("#ac_tk").val(),
                                            "key": $("#editExceptionForm #key").val(),
                                        }
                                    });
                                }
                                $.closeModal(opts.modalName);
                            }
                            searchYcxxList();
                            $("#btn_cancel").removeAttr("disabled",true)
                        });
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {
                        });
                    } else{
                        $.alert(responseText["message"],function() {
                        });
                    }
                },".modal-footer > button");
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};


// 初始化
$(function () {
    var oFileInput = new FileInput();
    var sign_params=[];
    sign_params.prefix=$('#exceptionViewListForm #urlPrefix').val();
    oFileInput.Init("exceptionViewListForm", "displayUpInfo", 2, 1, "pro_file",null,sign_params);

    // 所有下拉框添加choose样式
    jQuery('#exceptionViewListForm .chosen-select').chosen({width: '100%'});
    getYcxxList();
})

