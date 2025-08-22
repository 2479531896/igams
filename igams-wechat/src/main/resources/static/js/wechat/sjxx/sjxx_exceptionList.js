var loadflag = false;
var listCount = 5;
var listStrat = 0;
var ycPageNumber = 0;
var ycPageSize = 5;
var loadMoreFlag = true;
var clickflag=true;
var sjid=$("#exceptionList_Form #sjid").val();
//发送 回复等修改权限
var modqx=$("#exceptionList_Form #modqx").val();
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
            url: "/exception/exception/pagedataExceptionFeedback",
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
                                "<span>" +  (data.ycfklist[i].fkxx==null?"":data.ycfklist[i].fkxx) + "</span>" +
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
                                                                    "<span style='color:#339b53'>　@" + data.ycfklist[j].qrrmc + "　</span>" +
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
                var h=0;
                for (var i = 0; i <=index ; i++) {
                    h += $("#" + sjycs[i].ycid).height()
                }
                document.getElementsByClassName("bootbox-body")[0].scrollTop=h;
                $("#exceptionList_Form [name='hfxx']").typeahead({
                    source : function(query, process) {
                        return $.ajax({
                            url : '/exception/exception/pagedataSelectUser',
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
            url: "/ws/exception/viewMoreDiscuss",
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
    var fkfjids = $("#exceptionList_Form #fkfjids"+ycid).val();
    var url = "/exception/exception/pagedataSendFilePage?access_token=" + $("#ac_tk").val() + "&ycid=" + ycid;
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
                var oldfjids = $("#exceptionList_Form #fkfjids"+ycid).val();
                if (oldfjids) {
                    fjids = oldfjids+(fjids?(','+fjids):"");
                }
                $("#exceptionList_Form #fkfjids"+ycid).val(fjids);
                $.ajax({
                    type: 'post',
                    url: "/exception/exception/pagedataLsFile",
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
    var fkfjidList = $("#exceptionList_Form #fkfjids"+ycid).val().split(",");
    var fkfjids = "";
    for (var i = 0; i < fkfjidList.length; i++) {
        if (fkfjidList[i] != fjid){
            fkfjids += ','+ fkfjidList[i]
        }
    }
    $("#exceptionList_Form #fkfjids"+ycid).val(fkfjids.substring(1))
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
        var fkfjids = $("#exceptionList_Form #fkfjids"+ycid).val();
        var fkxx = $("#fkxx_"+ycid).val();
        if ((fkxx == null || fkxx == '')&&(fkfjids == null || fkfjids == '')) {
            clickflag=true;
            $.confirm("请输入反馈信息!");
        } else {
            $.ajax({
                type: 'post',
                url: "/exception/exception/pagedataSaveExceptionFk",
                data: {
                    "ycid": ycid,
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
                        $("#fkxx_"+ycid).val("");
                        $("#look"+ycid+" span").text(parseInt($("#look"+ycid+" span").text())+1)
                    }
                    $("#exceptionList_Form #fkfjids"+ycid).val("");
                    loadMoreFlag = true
                    ycPageNumber =-1
                    $("#YC"+ycid).html("")
                    loadMoreFknr(ycid,loadMoreFlag,ycPageSize,ycPageNumber)
                    clickflag=true;
                },
                error: function (e) {
                    clickflag=true;
                    $.alert(JSON.stringify(e.replaceAll("<","").replaceAll(">","")));
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
        var fkfjids = $("#exceptionList_Form #fkfjids"+ycid).val();
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
                url: "/exception/exception/pagedataSaveExceptionFk",
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
                    $("#exceptionList_Form #fkfjids"+ycid).val("");
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

// 新增异常按钮
function addException() {
    $("#addDiv").attr("style", "display:block;padding-top:20px");
    $("#ftabid").hide();
    $("#add").attr("style", "display:none;");
    yclbEvent()
    //  $(".viewException").attr("style","display:none;");
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
    var url = "/common/file/pdfPreview?fjid=" + fjid;
    $.showDialog(url, '文件预览', viewProTaskConfig);
}

// 附件下载
function xz(fjid) {
    jQuery('<form action="/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
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
            var url = "/common/file/delFile";
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
    if (!$("#exceptionList_Form #fjids").val()) {
        $("#exceptionList_Form #fjids").val(fjid);
    } else {
        $("#exceptionList_Form #fjids").val($("#exceptionList_Form #fjids").val() + "," + fjid);
    }
}

// 绑定按钮事件
function btnBind() {
    // 异常类别下拉框改变事件
    var sel_yclx = $("#exceptionList_Form #lx");
    sel_yclx.unbind("change").change(function () {
        yclbEvent();
    });
    // 提问人名称
    if ($("#exceptionList_Form #twrmc").val() == null || $("#exceptionList_Form #twrmc").val() == '') {
        $("#exceptionList_Form #twrmc").val($("#exceptionList_Form #mrtwrmc").val());
        $("#exceptionList_Form #twr").val($("#exceptionList_Form #mrtwr").val());
    }
    if ($("#exceptionList_Form #zlxmc").val() != null && $("#exceptionList_Form #zlxmc").val() != '') {
        $("#exceptionList_Form #yczlx").attr("style", "display:open;");
    }
}

//搜索
function searchYcxxList(){
    //初始化页码
    listStrat = 0;
    //清空异常信息列表
    $("#exceptionList_Form #sjycInfoList").empty();
    getYcxxList();
}

//查询
function getYcxxList(){
    loadflag = false;
    $.ajax({
        type: 'post',
        url: "/exception/exception/pagedataMoreException",
        cache: false,
        data: {
            "sjid": $("#exceptionList_Form #sjid").val(),
            "ycid": $("#exceptionList_Form #ycid").val(),
            "entire": $("#exceptionList_Form #ycxxCxnr").val(),
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
                    sjid = sjyc.sjid
                    html += '<div id="' + sjyc.ycid + '" class="col-md-12 col-sm-12 col-xs-12 viewException" style="margin-bottom:15px;padding:5px;border:' + (sjyc.sfzd != "1" ? "1px solid #ddd" : "4px solid #FFA366") + '; border-radius:5px;display:block;">' +
                        '<input type="hidden" id="fkfjids' + sjyc.ycid + '" name="fkfjids' + sjyc.ycid + '">\n' +
                        '<div style="padding-top:5px;width:100%;">' +
                        '<div class="col-md-7  col-sm-7 col-xs-7 text-left" style="padding-right:0px;">' +
                        '<span style="float: left;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;font-weight:bold;font-size:20px;">' + sjyc.ycbt + '</span>';
                    if (sjyc.sfjs == "1") {
                        html += '<span style="color:#66FF85;border:1px solid #66FF85;background-color:#EEFFF1;border-radius:3px;line-height:200%;margin-left:10px;">&nbsp已结束&nbsp</span>';
                    } else if (sjyc.sfjs != '1') {
                        html += '<span style="color:#FFA366;border:1px solid #FFA366;background-color:#FFEBDD;border-radius:3px;line-height:200%;margin-left:10px;">&nbsp未结束&nbsp</span>';
                    }

                    if (sjyc.fjs > 0) {
                        html += '<button type="button" style="border:1px solid #22B8DD;border-radius:3px;background-color:white;color:#22B8DD;margin: 2px;" onclick="sc(\'' + sjyc.ycid + '\',1)"><i class=\'glyphicon glyphicon-folder-open\'></i>　查看附件(' + sjyc.fjs + ')</button>';
                    }
                    html += '</div>' +
                        '<div class="col-md-5  col-sm-5 col-xs-5 text-right">';
                    if (sjyc.sfzd == "1"&&sjyc.sfjs!='1') {
                        html += '<button type="button" style="border:1px solid #FFA366;border-radius:3px;background-color:#FFEBDD;color:#FFA366;margin: 2px;" onclick="toTop(\'' + sjyc.ycid + '\',\'' + sjyc.sfzd + '\')"><i class="glyphicon glyphicon-pushpin"></i> 取消置顶</button>';
                    } else if (sjyc.sfzd != "1"&&sjyc.sfjs!='1') {
                        html += '<button type="button" style="border:1px solid #22B8DD;border-radius:3px;background-color:white;color:#22B8DD;margin: 2px;" onclick="toTop(\'' + sjyc.ycid + '\',\'' + sjyc.sfzd + '\')"><i class="glyphicon glyphicon-pushpin"></i> 置顶</button>';
                    }
                    if (($("#exceptionList_Form #yhid").val() == sjyc.qrr||$("#exceptionList_Form #jsid").val() == sjyc.qrr)&&sjyc.sfjs!='1'){
                        html += '<button type="button" style="border:1px solid #22B8DD;border-radius:3px;background-color:white;color:#22B8DD;margin: 2px;" onclick="repeat(\'' + sjyc.ycid + '\')"><i class="glyphicon glyphicon-share-alt"></i> 转发</button>';
                    }
                    if ($("#exceptionList_Form #yhid").val() == sjyc.twr&&sjyc.sfjs!='1') {
                        html += '<button type="button" style="border:1px solid #7AA7EB;border-radius:3px;background-color:white;color:#7AA7EB;margin: 2px;" onclick="finish(\'' + sjyc.ycid + '\')"><i class="glyphicon glyphicon-ok"></i> 结束</button>';
                    }
                    html += '</div>' +
                        '</div>' +
                        '<div class="col-md-12 col-sm-12 col-xs-12">' +
                        '<div class="col-md-8 col-sm-8 col-xs-12" style="padding:5px;">' +
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
                            '<span style="color:#3E9EE1" onclick="viewsjxx(sjid)">' + sjyc.ybbh + '</span>' + '</a>' +
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
                        '<span style="color:#777">' + sjyc.twrmc + '</span>' +
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
                        '<div class="col-sm-3 col-xs-3 col-xs-3 " style="padding:0px;">' +
                        '<span style="color:#777">异常信息：</span>' +
                        '</div>' +
                        '<div class="col-md-9 col-sm-9 col-xs-9" style="padding:0px;">' +
                        '<span style="color:#777">' + ((sjyc.ycxx != null && sjyc.ycxx != '') ? sjyc.ycxx : '无') + '</span>' +
                        '</div>' +
                        '</div>' +
                        '<div class="col-md-12 col-sm-12 col-xs-12" style="padding:0px;">' +
                        '<div class="col-sm-3 col-xs-3 col-xs-3 " style="padding:0px;">' +
                        '<span style="color:#777">最后回复时间：</span>' +
                        '</div>' +
                        '<div class="col-md-9 col-sm-9 col-xs-9" style="padding:0px;">' +
                        '<span style="color:#777">' + ((sjyc.zhhfsj) == null ? '' : sjyc.zhhfsj) + '</span>' +
                        '</div>' +
                        '</div>' +
                        '<div class="col-md-12 col-sm-12 col-xs-12" style="padding:0px;">' +
                        '<div class="col-sm-3 col-xs-3 col-xs-3 " style="padding:0px;">' +
                        '<span style="color:#777">最后回复内容：</span>' +
                        '</div>' +
                        '<div class="col-md-9 col-sm-9 col-xs-9" style="padding:0px;">' +
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
                $("#exceptionList_Form #sjycInfoList").append(html);
                 for (var i = 0; i < sjycxxlist.length; i++) {
                     getFknr(sjycxxlist[i].ycid,i);
                 }
            }else {
                addException()
            }
            if (sjycxxlist.length<listCount){
                loadflag = false;
            }else {
                loadflag = true;
            }
            listStrat += sjycxxlist.length
            $("#exceptionList_Form [name='fkxx']").typeahead({
                source : function(query, process) {
                    return $.ajax({
                        url : '/exception/exception/pagedataSelectUser',
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
    var url="/inspection/inspection/pagedataViewSjxx?sjid="+sjid+"&access_token=" + $("#ac_tk").val();
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
// 异常类别下拉框事件
function yclbEvent() {
    var yczlblength = $("#exceptionList_Form .yczlb").length;
    var yclx = $("#exceptionList_Form #lx").val();
    var isyblxin = false;
    if (yczlblength > 0) {
        for (var i = 0; i < yczlblength; i++) {
            var yczlxid = $("#exceptionList_Form .yczlb")[i].id;
            if (yczlxid != null && yczlxid != "") {
                var yczlcfcsid = $("#exceptionList_Form #" + yczlxid).attr("fcsid");
                if (yclx == yczlcfcsid) {
                    $("#exceptionList_Form #" + yczlxid).attr("style", "display:block;");
                    isyblxin = true;
                } else {
                    $("#exceptionList_Form #" + yczlxid).attr("style", "display:none;");
                }
            }
        }
    }
    if (isyblxin) {
        $("#exceptionList_Form #yczlx").attr("style", "display:block");
    } else {
        $("#exceptionList_Form #yczlx").attr("style", "display:none");
    }
    if(!$("#exceptionList_Form #zlx").val() || $("#exceptionList_Form #zlx").val() == ""){
        $("#exceptionList_Form #zlx").val("-1")
    }
    $("#exceptionList_Form #zlx").trigger("chosen:updated");
}

// 提问人点击事件
function chooseTwr() {
    var url = "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
    $.showDialog(url, '选择提问人', chooseTwrConfig);
}

// 异常保存按钮
function addSaveException() {
    $("#addSaveExceptionButton").attr("disabled",true);
    var fjids = $("#exceptionList_Form #fjids").val();//附件ids
    var ycbt = $("#ycbt").val();//标题
    var sjid = $("#sjid").val();//送检id
    var lx = $("#lx").val();//类型
    var zlx = $("#zlx").val();//子类型
    var ycxx = $("#ycxx").val();//异常信息
    var twr = $("#twr").val();//提问人
    var qrlx = $("#qrlx").val();//确认类型
    var qrr = $("#qrr").val();//确认人
    var tzlx = $("#tzlx").val();//通知类型
    var tzrys = $("#tzrys").val();//通知人
    var jcdw = $("#exceptionList_Form #jcdw option:selected").val();//检测单位
    var isComplete = true;
    if (tzlx!='' && tzlx!=null) {
        if (tzrys.length <= 0){
            $.confirm("通知人员/角色 不能为空！");
            isComplete = false;
        }
    }
    if (ycbt==null || ycbt==''){
        $.confirm("请填写标题！");
        isComplete = false;
    }
    if (lx==null || lx=='' || lx == '-1'){
        $.confirm("请选择类型！");
        isComplete = false;
    }
    if (!$("#yczlx").is(":hidden")){
        if (zlx==null || zlx=='' || zlx == '-1'){
            $.confirm("请选择子类型！");
            isComplete = false;
        }
    }
    if (qrlx==null || qrlx==''){
        $.confirm("请选择确认类型！");
        isComplete = false;
    }
    if (qrr==null || qrr==''){
        $.confirm("确认人员/角色 不能为空！");
        isComplete = false;
    }
    if (!isComplete){
        $("#addSaveExceptionButton").removeAttr("disabled");
        return;
    }
    var ycid = "";
    var url = "/exception/exception/pagedataSaveException";
    if ($("#action").val() =="mod"){
        ycid = $("#ycid").val();
        url = "/exception/exception/pagedataModSaveException"
    }
    $.ajax({
        type: 'post',
        url: url,
        cache: false,
        data: {
            "ycid": ycid,
            "tzrys": tzrys,
            "tzlx": tzlx,
            "fjids": fjids,
            "ycbt": ycbt,
            "sjid": sjid,
            "lx": lx,
            "zlx": zlx,
            "ycxx": ycxx,
            "twr": twr,
            "qrr": qrr,
            "qrlx": qrlx,
            "jcdw": jcdw,
            "fjbcbj": 'local',
            "access_token": $("#ac_tk").val()
        },
        dataType: 'json',
        success: function (data) {
            // 返回值
            if (data.status == "success") {
                if ($("#action").val() && ($("#action").val() == "add" || $("#action").val() == "mod")){
                    $.success(data.message,function() {
                        if ($("#action").val() =="mod") {
                            $.closeModal("modExceptionModal");
                        }else {
                            $.closeModal("addExceptionModal");
                        }
                        searchExceptionResult();
                    });
                }else {
                    $("#addDiv").attr("style", "display:none;");
                    $("#add").attr("style", "border:1px solid #CCCCCC;border-radius:3px;height:50px;padding:0px;display:block;")
                    $(".viewException").attr("style", "margin-bottom:15px;padding:5px;border:1px solid #ddd; border-radius:5px;display:block;");
                    $("#ftabid").empty();
                    $("#exceptionList_Form").load("/inspection/inspection/exceptionlistView?sjid=" + sjid);
                }
            }
        }
    });
}

// 返回异常列表
function goBackPage() {
    $("#addDiv").attr("style", "display:none;");
    $("#ftabid").show();
    $("#add").attr("style", "display:block;")
}

// 结束按钮，改变是否结束标记
function finish(ycid) {
    var sjid = $("#sjid").val();
    $.confirm('您确定要结束该异常信息吗？',function(result){
        if(result){
            $.ajax({
                type: 'post',
                url: "/exception/exception/finishException",
                cache: false,
                data: {"ids": ycid, "access_token": $("#ac_tk").val()},
                dataType: 'json',
                success: function (data) {
                    if (data.status=='success'){
                        $.success("保存成功！");
                        $("#ftabid").empty();
                        // 返回值
                        $("#exceptionList_Form").load("/inspection/inspection/exceptionlistView?sjid=" + sjid);
                    }else{
                        $.error("结束失败！");
                    }
                }
            });
        }
    });
}

// 转发按钮，选择转发人员或部门
function repeat(ycid) {
    var url = "/exception/exception/pagedataChoseRepeatObject?ycid=" + ycid;
    $.showDialog(url, '转发', chooseTzdxConfig);
}

// 通知人员、角色 选择方法
function chooseTzry() {
    var tzlx = $("#exceptionList_Form #tzlx").val();
    if (tzlx == "ROLE_TYPE") {
        $("#tzryjs").removeClass("hidden");
        $("#tzjsText").removeClass("hidden");
        $("#tzryText").addClass("hidden");
        url = "/systemrole/role/pagedataSelectListRole?access_token=" + $("#ac_tk").val();
        $.showDialog(url, '选择通知角色', chooseTzjsConfig);
    } else if (tzlx == "USER_TYPE") {
        $("#tzryjs").removeClass("hidden");
        $("#tzryText").removeClass("hidden");
        $("#tzjsText").addClass("hidden");
        var url = "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
        $.showDialog(url, '选择通知人员', chooseTzryConfig);
    } else {
        $("#tzryjs").addClass("hidden");
    }
}

// 确认人员、角色 选择方法
function chooseQrry() {
    var qrlx = $("#exceptionList_Form #qrlx").val();
    if (qrlx == "ROLE_TYPE") {
        $("#qrryjs").removeClass("hidden");
        $("#qrjsText").removeClass("hidden");
        $("#qrryText").addClass("hidden");
        url = "/systemrole/role/pagedataSelectListRole?access_token=" + $("#ac_tk").val();
        $.showDialog(url, '选择确认角色', chooseQrjsConfig);
    } else if (qrlx == "USER_TYPE") {
        $("#qrryjs").removeClass("hidden");
        $("#qrryText").removeClass("hidden");
        $("#qrjsText").addClass("hidden");
        var url = "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
        $.showDialog(url, '选择确认人员', chooseQrryConfig);
    } else {
        $("#qrryjs").addClass("hidden");
    }
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
                            $("#exceptionList_Form #sjycInfoList").empty();
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

// 通知角色选择框
var chooseTzjsConfig = {
    width: "800px",
    height: "500px",
    modalName: "chooseTzjsModal",
    formName: "exceptionList_Form",
    offAtOnce: true, // 当数据提交成功，立刻关闭窗口
    buttons: {
        success: {
            label: "确 定",
            className: "btn-primary",
            callback: function () {
                if (!$("#listRoleForm").valid()) {
                    return false;
                }
                var json = [];
                var data = $('#listRoleForm #xzjss').val();// 获取选择行数据
                for (; data.indexOf("jsid") != -1;) {
                    data = data.replace("jsid", "value")
                }
                for (; data.indexOf("jsmc") != -1;) {
                    data = data.replace("jsmc", "text")
                }
                $("#exceptionList_Form  #tzrys").tagsinput({
                    itemValue: "value",
                    itemText: "text",
                })
                json = data;
                var jsonStr = eval('(' + json + ')');
                for (var i = 0; i < jsonStr.length; i++) {
                    $("#exceptionList_Form  #tzrys").tagsinput('add', jsonStr[i]);
                }
            }
        },
        cancel: {
            label: "关 闭",
            className: "btn-default"
        }
    }
};

// 通知人员选择框
var chooseTzryConfig = {
    width: "800px",
    height: "500px",
    modalName: "chooseTzryModal",
    formName: "exceptionList_Form",
    offAtOnce: true, // 当数据提交成功，立刻关闭窗口
    buttons: {
        success: {
            label: "确 定",
            className: "btn-primary",
            callback: function () {
                if (!$("#taskListFzrForm").valid()) {
                    return false;
                }
                var json = [];
                var data = $('#taskListFzrForm #xzrys').val();// 获取选择行数据
                for (; data.indexOf("yhid") != -1;) {
                    data = data.replace("yhid", "value")
                }
                for (; data.indexOf("zsxm") != -1;) {
                    data = data.replace("zsxm", "text")
                }
                $("#exceptionList_Form  #tzrys").tagsinput({
                    itemValue: "value",
                    itemText: "text",
                })
                json = data;
                var jsonStr = eval('(' + json + ')');
                for (var i = 0; i < jsonStr.length; i++) {
                    $("#exceptionList_Form #tzrys").tagsinput('add', jsonStr[i]);
                }
            }
        },
        cancel: {
            label: "关 闭",
            className: "btn-default"
        }
    }
};

// 通知角色选择框
var chooseQrjsConfig = {
    width: "800px",
    height: "500px",
    modalName: "chooseQrjsModal",
    formName: "exceptionList_Form",
    offAtOnce: true, // 当数据提交成功，立刻关闭窗口
    buttons: {
        success: {
            label: "确 定",
            className: "btn-primary",
            callback: function () {
                if (!$("#listRoleForm").valid()) {
                    return false;
                }
                var $this = this;
                var opts = $this["options"] || {};
                var sel_row = $('#listRoleForm #tb_list').bootstrapTable('getSelections');// 获取选择行数据
                if (sel_row.length == 1) {
                    $('#exceptionList_Form #qrr').val(sel_row[0].jsid);
                    $('#exceptionList_Form #qrrmc').val(sel_row[0].jsmc);
                    $.closeModal(opts.modalName);
                } else {
                    $.error("请选中一行");
                    return;
                }
                return false;
            }
        },
        cancel: {
            label: "关 闭",
            className: "btn-default"
        }
    }
};

// 确认人员选择框
var chooseQrryConfig = {
    width: "800px",
    height: "500px",
    modalName: "chooseFzrModal",
    formName: "exceptionList_Form",
    offAtOnce: true, // 当数据提交成功，立刻关闭窗口
    buttons: {
        success: {
            label: "确 定",
            className: "btn-primary",
            callback: function () {
                if (!$("#taskListFzrForm").valid()) {
                    return false;
                }
                var $this = this;
                var opts = $this["options"] || {};
                var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');// 获取选择行数据
                if (sel_row.length == 1) {
                    $('#exceptionList_Form #qrr').val(sel_row[0].yhid);
                    $('#exceptionList_Form #qrrmc').val(sel_row[0].zsxm);
                    $.closeModal(opts.modalName);
                } else {
                    $.error("请选中一行");
                    return;
                }
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

// 提问人员选择框
var chooseTwrConfig = {
    width: "800px",
    height: "500px",
    modalName: "chooseFzrModal",
    formName: "exceptionList_Form",
    offAtOnce: true, // 当数据提交成功，立刻关闭窗口
    buttons: {
        success: {
            label: "确 定",
            className: "btn-primary",
            callback: function () {
                if (!$("#taskListFzrForm").valid()) {
                    return false;
                }
                var $this = this;
                var opts = $this["options"] || {};
                var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');// 获取选择行数据
                if (sel_row.length == 1) {
                    $('#exceptionList_Form #twr').val(sel_row[0].yhid);
                    $('#exceptionList_Form #twrmc').val(sel_row[0].zsxm);
                    $.closeModal(opts.modalName);
                } else {
                    $.error("请选中一行");
                    return;
                }
                return false;
            }
        },
        cancel: {
            label: "关 闭",
            className: "btn-default"
        }
    }
};

// 通知类型改变事件
$("#exceptionList_Form #tzlx").change(function () {
    var tzlx = $("#exceptionList_Form #tzlx").val();
    if (tzlx == "ROLE_TYPE") {
        $("#tzryjs").removeClass("hidden");
        $("#tzjsText").removeClass("hidden");
        $("#tzryText").addClass("hidden");
    } else if (tzlx == "USER_TYPE") {
        $("#tzryjs").removeClass("hidden");
        $("#tzryText").removeClass("hidden");
        $("#tzjsText").addClass("hidden");
    } else {
        $("#tzryjs").addClass("hidden");
    }
    var tzrys = $("#exceptionList_Form #tzrys").val();
    if (tzrys != null && tzrys != '') {
        $("#exceptionList_Form  #tzrys").tagsinput('removeAll');
    }
})

// 确认类型改变事件
$("#exceptionList_Form #qrlx").change(function () {
    var qrlx = $("#exceptionList_Form #qrlx").val();
    if (qrlx == "ROLE_TYPE") {
        $("#qrryjs").removeClass("hidden");
        $("#qrjsText").removeClass("hidden");
        $("#qrryText").addClass("hidden");
    } else if (qrlx == "USER_TYPE") {
        $("#qrryjs").removeClass("hidden");
        $("#qrryText").removeClass("hidden");
        $("#qrjsText").addClass("hidden");
    } else {
        $("#qrryjs").addClass("hidden");
    }
    var qrry = $("#exceptionList_Form #qrr").val();
    if (qrry != null && qrry != '') {
        $("#exceptionList_Form  #qrr").val("");
        $("#exceptionList_Form  #qrrmc").val("");
    }
})

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
    var lsdata = "[{\"title\":\"ycid\",\"value\":\""+ycid+"\"}"+
        ",{\"title\":\"sfzd\",\"value\":\""+lssfzd+"\"}"+
        ",{\"title\":\"zdry\",\"value\":\""+$("#exceptionList_Form #yhid").val()+"\"}]"
    $.ajax({
        type: 'post',
        url: "/ws/exception/pagedataTopException",
        cache: false,
        data: {
            "ycid": ycid,
            "sfzd": lssfzd,
            "zdry": $("#exceptionList_Form #yhid").val(),
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

function connectExceptionMessage(key) {
    var source = null;
    // 用时间戳模拟登录用户
    if (!!window.EventSource) {
        // 建立连接
        source = new EventSource("/sseEmit/connect/pagedataOA?access_token=" + $("#ac_tk").val()+"&key="+key);
        /**
         * 连接一旦建立，就会触发open事件
         * 另一种写法：source.onopen = function (event) {}
         */
        source.addEventListener('open', function (e) {
            console.log("建立连接。。。");
        }, false);
        /**
         * 客户端收到服务器发来的数据
         * 另一种写法：source.onmessage = function (event) {}
         */
        source.addEventListener('message', function (e) {
            setExceptionMessageInnerHTML(e.data);
        });
        /**
         * 如果发生通信错误（比如连接中断），就会触发error事件
         * 或者：
         * 另一种写法：source.onerror = function (event) {}
         */
        source.addEventListener('error', function (e) {
            if (e.readyState === EventSource.CLOSED) {
                console.log("连接关闭");
            } else {
                closeSse();
            }
            $("#exceptionList_Form #timeOver").removeAttr("style");
            $.ajax({
                url: "/inspection/inspection/pagedataFlushedInfo",
                data: {
                    "access_token": $("#ac_tk").val(),
                    "key": key,
                },
                success: function (data) {
                    if (data.key){
                        $("#exceptionList_Form #key").val(data.key);
                        $("#exceptionList_Form #dingtalkurl").val(data.dingtalkurl);
                        $("#exceptionList_Form #switchoverurl").val(data.switchoverurl);
                    }
                },
                error: function () {
                    $.error("连接手机失败，请联系管理员解决原因！");
                }
            });

        }, false);
    } else {
        console.log("你的浏览器不支持SSE");
    }
    // 监听窗口关闭事件，主动去关闭sse连接，如果服务端设置永不过期，浏览器关闭后手动清理服务端数据
    window.onbeforeunload = function () {
        if($("body").attr("isxz")=='true'){
            $("body").attr("isxz","false")
            return
        }
        closeSse();
        if ($("#exceptionList_Form #key").val()){
            $.ajax({
                url: "/sseEmit/connect/pagedataCloseOA",
                data: {
                    "access_token": $("#ac_tk").val(),
                    "key": $("#exceptionList_Form #key").val(),
                }
            });
        }
    };
    window.addEventListener('message', (event) => {
        $.ajax({
            url: "/sseEmit/connect/pagedataCloseOA",
            data: {
                "access_token": $("#ac_tk").val(),
                "key": $("#exceptionList_Form #key").val(),
            }
        });
        closeSse();
    })
    // 关闭Sse连接
    function closeSse() {
        source.close();
        // $.ajax({
        // 	url: "/sseEmit/connect/pagedataCloseOA",
        // 	data: {
        // 		"access_token": $("#ac_tk").val(),
        // 		"userId": $("#ajaxForm #yhid").val(),
        // 	},
        // 	success: function (data) {},
        // 	error: function () {
        // 		$.error("连接手机失败，请联系管理员解决原因！");
        // 	}
        // });

        console.log("close");
    }

    // 将消息显示在网页上
    function setExceptionMessageInnerHTML( res) {
        //处理读到的数据

        var obj = JSON.parse(res);
        //显示到页面
        if (obj) {
            if (obj.key == $("#exceptionList_Form #key").val()){
                if (obj.fjids){
                    if ( $("#exceptionList_Form #fjids").val()){
                        $("#exceptionList_Form #fjids").val($("#exceptionList_Form #fjids").val()+","+obj.fjids)
                    }else{
                        $("#exceptionList_Form #fjids").val(obj.fjids);
                    }
                    if (obj.fjcfbDtoList && obj.fjcfbDtoList.length>0){
                        var html = "";
                        for (let i = 0; i < obj.fjcfbDtoList.length; i++) {
                            if ($("#exceptionList_Form #bs").val() != "1"){
                                html +="<label style='padding: 0'  class=\"col-md-2 col-sm-4 col-xs-12 control-label \"></label>";
                            }else{
                                html +="<label style='padding: 0' class=\"col-md-2 col-sm-4 col-xs-12 control-label \">附件：</label>";
                            }
                            html += "<div style='padding: 0' class=\"col-md-10 col-sm-8 col-xs-12\" id=\""+obj.fjcfbDtoList[i].fjid+"\" >";
                            html += "<label>"+($("#exceptionList_Form #bs").val())+".</label>";
                            $("#exceptionList_Form #bs").val($("#exceptionList_Form #bs").val()*1+1);
                            html += " <button style=\"outline:none;margin-bottom:5px;padding:0px;\" onclick=\"xzExceptionTemporary(\'"+obj.fjcfbDtoList[i].wjlj+"\',\'"+obj.fjcfbDtoList[i].wjm+"\')\" class=\"btn btn-link\" type=\"button\" title=\"下载\">";
                            html += " <span>"+obj.fjcfbDtoList[i].wjm+"</span>";
                            html += " <span class=\"glyphicon glyphicon glyphicon-save\"></span>";
                            html += " </button>";
                            html += " <button title=\"删除\" class=\"f_button\" type=\"button\" onclick=\"delExceptionTemporary(\'"+obj.fjcfbDtoList[i].fjid+"\')\">";
                            html += " <span class=\"glyphicon glyphicon-remove\"></span>";
                            html += " </button>";
                            html += " <button title=\"预览\" class=\"f_button\" type=\"button\" onclick=\"ylExceptionTemporary(\'"+obj.fjcfbDtoList[i].fjid+"\',\'"+obj.fjcfbDtoList[i].wjm+"\')\">";
                            html += " <span  class=\"glyphicon glyphicon-eye-open\"></span>";
                            html += " </button>";
                            html += "</div>";
                        }
                        $("#exceptionList_Form #redisFj").append(html);
                    }
                }
            }
        }

    }

}


function exceptionflushed() {
    $("#exceptionList_Form #qrcode").empty();
    $("#exceptionList_Form #code").empty();
    if($("#exceptionList_Form #dingtalkurl").val()) {
        $("#exceptionList_Form #qrcode").qrcode({
            render: 'div',
            size: 185,
            text: $("#exceptionList_Form #dingtalkurl").val()
        })
    }
    if($("#exceptionList_Form #switchoverurl").val()) {
        $("#exceptionList_Form #code").qrcode({
            render: 'div',
            size: 180,
            text: $("#exceptionList_Form #switchoverurl").val(),
        })
    }
    connectExceptionMessage($("#exceptionList_Form #key").val());
    $("#exceptionList_Form #timeOver").attr("style","display: none;")
}


function ylExceptionTemporary(fjid,wjm){
    var begin=wjm.lastIndexOf(".");
    var end=wjm.length;
    var type=wjm.substring(begin,end);
    if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
        var url="/ws/sjxxpripreview/?fjid="+fjid+"&temporary=temporary"
        $.showDialog(url,'图片预览',JPGExceptionMaterConfig);
    }else {
        $.alert("暂不支持其他文件的预览，敬请期待！");
    }
}
var JPGExceptionMaterConfig = {
    width		: "800px",
    offAtOnce	: true,
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

$(".bootbox-close").click(function(){
    if ($("#exceptionList_Form #key").val()){
        $.ajax({
            url: "/sseEmit/connect/pagedataCloseOA",
            data: {
                "access_token": $("#ac_tk").val(),
                "key": $("#exceptionList_Form #key").val(),
            }
        });
    }
});

$('#exceptionListModalSy').on('hide.bs.modal', function (e) {
  // 模态框右上角叉叉关闭模态框时关闭长链接
  $.ajax({
        url: "/sseEmit/connect/pagedataCloseOA",
        data: {
            "access_token": $("#ac_tk").val(),
            "key": $("#ajaxForm #key").val(),
        }
    });
});

function xzExceptionTemporary(wjlj,wjm){
    jQuery('<form action="/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
        '<input type="text" name="wjlj" value="'+wjlj+'"/>' +
        '<input type="text" name="wjm" value="'+wjm+'"/>' +
        '<input type="text" name="temporary" value="temporary"/>' +
        '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
        '</form>')
        .appendTo('body').submit().remove();
}

function delExceptionTemporary(fjid){
    $.confirm('您确定要删除所选择的记录吗？',function(result){
        if(result){
            $("#exceptionList_Form #redisFj #"+fjid).remove();
            var strings = $("#exceptionList_Form  #fjids").val().split(",");
            if (null!= strings && strings.length>0){
                var string = "";
                for (let i = 0; i < strings.length; i++) {
                    if (strings[i] != fjid){
                        string += ","+strings[i];
                    }
                }
                $("#exceptionList_Form  #fjids").val(string.substring(1));
            }
        }
    });
}

var switchoverflagE = 2;
function switchoverE(){
    if (switchoverflagE == 1){
        $("#exceptionList_Form #text").show();
        $("#exceptionList_Form #textD").hide();
        switchoverflagE= 2;
        $("#exceptionList_Form #code").show();
        $("#exceptionList_Form #qrcode").hide();
    }else{
        $("#exceptionList_Form #textD").show();
        $("#exceptionList_Form #text").hide();
        switchoverflagE= 1;
        $("#exceptionList_Form #qrcode").show();
        $("#exceptionList_Form #code").hide();
    }
}
// 初始化
$(function () {
    var oFileInput = new FileInput();
    oFileInput.Init("exceptionList_Form", "displayUpInfo", 2, 1, "pro_file");
    //若异常标题不为空
    if (!$("#exceptionList_Form #ycbt").val() || !$("#exceptionList_Form #ycbt").val() != ''){
        var ycbt = "";
        if($("#exceptionList_Form #hzxm").val()){
            ycbt = $("#exceptionList_Form #hzxm").val();
        }
        if($("#exceptionList_Form #yblxmc").val()){
            ycbt = ycbt +"-"+$("#exceptionList_Form #yblxmc").val();
        }
        if($("#exceptionList_Form #hbmc").val()&&$("#exceptionList_Form #hbmc").val()!="无"&&$("#exceptionList_Form #hbmc").val()!="-"){
            ycbt = ycbt +"-"+$("#exceptionList_Form #hbmc").val();
        }
        $("#exceptionList_Form #ycbt").val(ycbt);
    }

    btnBind();
    // 所有下拉框添加choose样式
    jQuery('#exceptionList_Form .chosen-select').chosen({width: '100%'});

    if ($("#exceptionList_Form #key").val()){
        connectExceptionMessage($("#exceptionList_Form #key").val());
        if($("#exceptionList_Form #dingtalkurl").val()) {
            $("#exceptionList_Form #qrcode").qrcode({
                render: 'div',
                size: 185,
                text: $("#exceptionList_Form #dingtalkurl").val()
            })
        }
        if($("#exceptionList_Form #switchoverurl").val()) {
            $("#exceptionList_Form #code").qrcode({
                render: 'div',
                size: 180,
                text: $("#exceptionList_Form #switchoverurl").val()
            })
        }
    }
    if (sjid == null || sjid == '') {
        $("#exceptionList_Form #FkxxTab").html("");

        $("#exceptionList_Form #YcxxTab").attr("class", "tab-pane fade active in");
        var html = ""
        html += "<ul id='tabid' class='nav nav-tabs sl_nav_tabs' role='tablist'>";
        html += "<li class='active'>";
        html += "<a href='#YcxxTab' role='tab' data-bs-toggle='tab'>";
        html += "详细信息";
        html += "</a>";
        html += "</li>";
        html += "</ul>";
        $("#tabid").html(html)
        //如果是病原体列表 打开新增界面
        if (modqx=='0'){
            addException()
        }else {
            //异常列表查询相应异常信息
            getYcxxList();
        }
    }else {
        if ($("#action").val() =="mod"){
            addException();
            var qrlx = $("#exceptionList_Form #qrlx").val();
            if (qrlx == "ROLE_TYPE") {
                $("#qrryjs").removeClass("hidden");
                $("#qrjsText").removeClass("hidden");
                $("#qrryText").addClass("hidden");
            } else if (qrlx == "USER_TYPE") {
                $("#qrryjs").removeClass("hidden");
                $("#qrryText").removeClass("hidden");
                $("#qrjsText").addClass("hidden");
            } else {
                $("#qrryjs").addClass("hidden");
            }

            var tzlx = $("#exceptionList_Form #tzlx").val();
            if (tzlx == "ROLE_TYPE") {
                $("#tzryjs").removeClass("hidden");
                $("#tzjsText").removeClass("hidden");
                $("#tzryText").addClass("hidden");
            } else if (tzlx == "USER_TYPE") {
                $("#tzryjs").removeClass("hidden");
                $("#tzryText").removeClass("hidden");
                $("#tzjsText").addClass("hidden");
            } else {
                $("#tzryjs").addClass("hidden");
            }
            var tzrys = $("#exceptionList_Form #tzrys").val();
            if (tzrys != null && tzrys != '') {
                $("#exceptionList_Form  #tzrys").tagsinput('removeAll');
            }
            initTzJsrys()
        }else {
            getYcxxList();
        }
    }
})

function change(){
    var url = "/exception/exception/pagedataSjxxChance?access_token=" + $("#ac_tk").val();
    $.showDialog(url, '选择送检信息', chooseSjxxConfig);
}


var chooseSjxxConfig = {
    width : "800px",
    height : "500px",
    modalName	: "chooseSjxxModal",
    formName	: "exceptionList_Form",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#chanceSjxxForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var sel_row = $('#chanceSjxxForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    $('#exceptionList_Form #ybbh').val(sel_row[0].ybbh);
                    $('#exceptionList_Form #ybbhshow').val(sel_row[0].ybbh);
                    $('#exceptionList_Form #sjid').val(sel_row[0].sjid);
                    $("#exceptionList_Form #"+sel_row[0].jcdw).prop("selected","selected");
                    $("#exceptionList_Form #"+sel_row[0].jcdw).trigger("chosen:updated");
                    $('#exceptionList_Form #ycbt').val(sel_row[0].hzxm+'-'+sel_row[0].yblxmc+'-'+sel_row[0].db);
                    $.closeModal(opts.modalName);
                }else{
                    $.error("请选中一行");
                    return;
                }
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

function initTzJsrys(){
    var ycid = $("#exceptionList_Form #ycid").val();
    var tzlx = $("#exceptionList_Form #tzlx").val();
    if (tzlx!=null && tzlx!=''){
        $.ajax({
            type:'post',
            url:"/exception/exception/pagedataNotices",
            cache: false,
            data: {"ycid":ycid,"tzlx":tzlx,"access_token":$("#ac_tk").val()},
            dataType:'json',
            success:function(result){
                var tzryjss = result.sjyctzDtoList;
                var jsonStr = [];
                if (tzryjss.length>0){
                    for (var i = 0; i < tzryjss.length; i++) {
                        var text = tzryjss[i].mc;
                        var value = tzryjss[i].id;
                        jsonStr.push({'text':text,'value':value})
                    }
                }
                $("#exceptionList_Form  #tzrys").tagsinput({
                    itemValue: "value",
                    itemText: "text",
                })
                for (var i = 0; i < jsonStr.length; i++) {
                    $("#exceptionList_Form  #tzrys").tagsinput('add', jsonStr[i]);
                }
            }
        });
    }
}

