var Covid_turnOff=true;

var Covid_TableInit = function (FieldList) {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function (){
        $('#covidformSearch #covid_list').bootstrapTable({
            url: '/detection/detection/pageGetListCovid',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#covidformSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "fzjcxx.bgrq",				//排序字段
            sortOrder: "DESC NULLS LAST",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 15,                       //每页的记录行数（*）
            pageList: [15, 30, 50, 100, 1000],        //可供选择的每页的行数（*）
            paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "fzjcid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns:FieldList,
            jclx:$("#covidformSearch #jclx").val() ,
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                CovidDealById(row.fzjcid,'view',$("#covidformSearch #btn_view").attr("tourl"));
            },
        });
        $("#covidformSearch #covid_list").colResizable({
                liveDrag:true,
                gripInnerHtml:"<div class='grip'></div>",
                draggingClass:"dragging",
                resizeMode:'fit',
                postbackSafe:true,
                partialRefresh:true,
            }
        );
    };
    // 得到查询的参数
    oTableInit.queryParams = function(params){
        // 请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
        // 例如 toolbar 中的参数
        // 如果 queryParamsType = ‘limit’ ,返回参数必须包含
        // limit, offset, search, sort, order
        // 否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder.
        // 返回false将会终止请求
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "fzjcxx.fzjcid", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getCovidSearchData(map);
    };
    return oTableInit;
}
//结果格式化
function jcjgmcformat(value,row,index){
    if(row.jcjgmc=="阳性"){
        var jcjgmc="<span style='color:red;font-weight:bold;'>阳性</span>";
        return jcjgmc;
    }else if(row.jcjgmc=="混管可疑阳性"){
        var jcjgmc="<span style='color:#0000cc;font-weight:bold;'>混管可疑阳性</span>";
        return jcjgmc;
    }else if(row.jcjgmc=="阴性"){
        var jcjgmc="<span style='font-weight:bold;'>阴性</span>";
        return jcjgmc;
    }
}

function getCovidSearchData(map){
    var cxtj=$("#covidformSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#covidformSearch #cxnr').val());
    if(cxtj=="0"){
        map["entire"]=cxnr
    }else if(cxtj=="1"){
        map["ybbh"]=cxnr
    }else if(cxtj=="2"){
        map["xm"]=cxnr
    }else if(cxtj=='3'){
        map["nl"]=cxnr
    }else if(cxtj=='4'){
        map["syh"]=cxnr
    }else if(cxtj=='5'){
        map["yblx"]=cxnr
    }else if(cxtj=='6'){
        map["bbzbh"]=cxnr
    }else if(cxtj=='7'){
        map["dwmc"]=cxnr
    }else if(cxtj=='8'){
        map["orderno"]=cxnr
    }else if(cxtj=='9'){
        map["fphm"]=cxnr
    }else if(cxtj=='10'){
        map["ptorderno"]=cxnr
    }else if(cxtj=='11'){
        map["pt"]=cxnr
    }else if(cxtj=='12'){
        map["sj"]=cxnr
    }else if(cxtj=='13'){
        map["cjrymc"]=cxnr
    }else if(cxtj=='14'){
        map["zjh"]=cxnr
    }

    var sfsy = jQuery('#covidformSearch #sfsy_id_tj').val();
    map["sfsy"] = sfsy;

    var sfjs = jQuery('#covidformSearch #sfjs_id_tj').val();
    map["sfjs"] = sfjs;
    var fkbj = jQuery('#covidformSearch #fkbj_id_tj').val();
    map["fkbj"] = fkbj;
    var kpbj = jQuery('#covidformSearch #kpbj_id_tj').val();
    map["kpbj"] = kpbj;
    var sfjf = jQuery('#covidformSearch #sfjf_id_tj').val();
    map["sfjf"] = sfjf;

    var zts = jQuery('#covidformSearch #zt_id_tj').val();
    map["zts"] = zts;

    var jcdxlx = jQuery('#covidformSearch #jcdxlx_id_tj').val();
    map["jcdxlxs"] = jcdxlx;

    var jczxms = jQuery('#covidformSearch #jczxm_id_tj').val();
    map["jczxms"] = jczxms;
    // 采集日期
    var cjsjstart = jQuery('#covidformSearch #cjsjstart').val();
    map["cjsjstart"] = cjsjstart;
    var cjsjend = jQuery('#covidformSearch #cjsjend').val();
    map["cjsjend"] = cjsjend;
    // 接收日期
    var jssjstart = jQuery('#covidformSearch #jssjstart').val();
    map["jssjstart"] = jssjstart;
    var jssjend = jQuery('#covidformSearch #jssjend').val();
    map["jssjend"] = jssjend;
    // 检测日期
    var sysjstart = jQuery('#covidformSearch #sysjstart').val();
    map["sysjstart"] = sysjstart;
    var sysjend = jQuery('#covidformSearch #sysjend').val();
    map["sysjend"] = sysjend;
    // 报告日期
    var bgrqstart = jQuery('#covidformSearch #bgrqstart').val();
    map["bgrqstart"] = bgrqstart;
    var bgrqend = jQuery('#covidformSearch #bgrqend').val();
    map["bgrqend"] = bgrqend;

    // 预约日期
    var yyrqstart = jQuery('#covidformSearch #yyrqstart').val();
    map["yyrqstart"] = yyrqstart;
    var yyrqend = jQuery('#covidformSearch #yyrqend').val();
    map["yyrqend"] = yyrqend;

    // 付款日期
    var fkrqstart = jQuery('#covidformSearch #fkrqstart').val();
    map["fkrqstart"] = fkrqstart;
    var fkrqend = jQuery('#covidformSearch #fkrqend').val();
    map["fkrqend"] = fkrqend;
    return map;
}

var covid_PageInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        var jcdxlx=$("#covidformSearch a[id^='jcdxlx_id_']")

        $.each(jcdxlx, function(i, n){
            var id = $(this).attr("id");
            var code = id.substring(id.lastIndexOf('_')+1,id.length);
            var csdm=$("#covidformSearch #"+id).attr("csdm");
            if(csdm == 'R'){
                addTj('jcdxlx',code,'covidformSearch');
            }
        });
    }
    return oInit;
}

// 按钮动作函数
function CovidDealById(id,action,tourl,fzxmid){
    if(!tourl){
        return;
    }
    if(action =='view'){
        var url= tourl + "?fzjcid="+id;
        $.showDialog(url,'详情',viewCovidConfig);
    }else if(action =='historicalrecords'){
        var url= tourl + "?fzjcid="+id;
        $.showDialog(url,'历史记录',historicalrecordsViewCovidConfig);
    }else if(action =='upload'){
        var url= tourl + "?fzjcid="+id+"&jclx="+$("#covidformSearch #jclx").val();
        $.showDialog(url,'上传',uploadCovidReportConfig);
    }else if(action =='accept'){
        if(id!=null){
            var url= tourl + "?ybbh="+id+"&jclx="+$("#covidformSearch #jclx").val();
        }else{
            var url= tourl+"?jclx="+$("#covidformSearch #jclx").val();
        }
        $.showDialog(url,'标本接收',confirmCovidConfig);
        $("#sampleAcceptForm #ybbh").focus();
    }else if(action =='submit'){
        var url= tourl+"?fzjcid="+id+"&jclx="+$("#covidformSearch #jclx").val();
        $.showDialog(url,'提交',submitCovidConfig);
    }else if(action=='detection'){
        var url=tourl + "?ids=" +id+"&jclx="+$("#covidformSearch #jclx").val();
        $.showDialog(url,'实验',modDetectionConfig);
    }else if(action=='msgconfirm'){
        var url=tourl + "?fzjcid=" +id+"&jclx="+$("#covidformSearch #jclx").val();
        $.showDialog(url,'信息确认',msgconfirmDetectionConfig);
    }else if(action=='resultMod'){
        var url=tourl + "?ybbhs=" +id+"&jclx="+$("#covidformSearch #jclx").val();
        $.showDialog(url,'修改结果',modDetectionResultConfig);
    }else if(action=='mod'){
        var url=tourl + "?fzjcid=" +id+"&fzxmid="+fzxmid+"&jclx="+$("#covidformSearch #jclx").val();
        $.showDialog(url,'修改检测信息',editCovidDetectionConfig);
    }else if(action=="add"){
        var url=tourl+"?jclx="+$("#covidformSearch #jclx").val();
        $.showDialog(url,'新增物检信息',addObjDetectionConfig);
    }else if(action=='payment'){
        var url=tourl + "?fzjcid=" +id+"&jclx="+$("#covidformSearch #jclx").val();
        $.showDialog(url,'付款',payConfig);
    }else if(action=="reportdownload"){
        var url=tourl + "?ids=" +id+"&jclx="+$("#covidformSearch #jclx").val();
        $.showDialog(url,'报告下载',reportDownloadConfig);
    }else if(action=="report"){
        var url=tourl + "?ids=" +id+"&jclx="+$("#covidformSearch #jclx").val();
        $.showDialog(url,'上传平台',reportConfig);
    }else if(action=="delivery"){
        var url=tourl + "?ids=" +id+"&jclx="+$("#covidformSearch #jclx").val();
        $.showDialog(url,'修改交付状态', sfjfDetectionConfig);
    }else if(action=="checklist"){
        var url=tourl+"?jclx="+$("#covidformSearch #jclx").val();
        $.showDialog(url,'物检清单', checkListCovidReportConfig);
    }
    // else if(action=='report'){
    //     var url=tourl + "?ids=" +id;
    //     $.showDialog(url,'',reportCovidDetectionConfig);
    // }
}


/**
 *
 */
var checkListCovidReportConfig = {
    width		: "600px",
    modalName	: "checkListCovidReportModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确定",
            className : "btn-primary",
            callback : function() {
                if(!$("#covidvCheck_formSearch").valid()){
                    return false;
                }
                let map = "";
                var print = $('#covidvCheck_formSearch input:radio[name="print"]:checked').val();
                if (print == "0"){
                    map = {
                        access_token: $("#ac_tk").val(),
                        print_cs: $("#covidvCheck_formSearch #print_cs").val(),
                    };
                }else{
                    map = {
                        access_token: $("#ac_tk").val()
                    };
                }
                map =  getCovidSearchData(map);
                openPostWindow("/detection/detection/checklistSavePrintCheck",map)
                return true;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var sfjfDetectionConfig= {
    width		: "700px",
    height		: "100px",
    modalName   : "sfjfDetectionModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确认",
            className : "btn-primary",
            callback : function() {
                if(!$("#covidSfjfMod_Form").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#access_token").val($("#ac_tk").val());

                $("#covidSfjfMod_Form input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"covidSfjfMod_Form",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            searchCovidResult();
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

function print_wpxz(callBackUrl,RequestLocalCode,ResponseSign,sz_flg){
    var glxx = $("#editCovidForm #glxx").val();
    var print_url = null
    if(sz_flg=="0"){
        print_url = "http://localhost:8081/XGLRPrint";
    }else if(sz_flg=="1"){
        print_url = "http://" + glxx + ":8082/XGLRPrint";
    }
    var url = print_url+"?callBackUrl="+callBackUrl+"&ResponseNum=3&RequestLocalCode="+RequestLocalCode+"&ResponseSign="+ResponseSign+"&access_token="+ $("#ac_tk").val()
    openWindow = window.open(url);
    setTimeout(function(){
        openWindow.close();
    }, 600);
}
var addObjDetectionConfig = {
    width		: "1400px",
    modalName	:"addObjDetectionModal",
    offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#editCovidForm").valid()){// 表单验证
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#editCovidForm input[name='access_token']").val($("#ac_tk").val());
                var szz = $('#editCovidForm input:radio[name="szz"]:checked').val();
                if (szz == $("#editCovidForm #ysszz").val()) {
                    $("#editCovidForm #grsz_flg").val(0);
                } else {
                    $("#editCovidForm #grsz_flg").val(1);
                }
                submitForm(opts["formName"]||"editCovidForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            preventResubmitForm(".modal-footer > button", false);
                            if (responseText["sz_flg"]) {
                                let callBackUrl = responseText["callBackUrl"];
                                let RequestLocalCode = responseText["RequestLocalCode"];
                                let ResponseSign = responseText["ResponseSign"];
                                let sz_flg = responseText["sz_flg"];
                                print_wpxz(callBackUrl,RequestLocalCode,ResponseSign,sz_flg);
                            }
                            //清除元素数据
                            setTimeout(function () {
                                $($this["document"]).each(function() {
                                    if($(this).is('input,select,textarea')){
                                        $(this).clearFields();
                                        $(this).trigger("chosen:updated");
                                    }else{
                                        //筛选要清除数据的元素
                                        var elements = $(this).find('input,select,textarea');
                                        //筛选自定义清除数据的元素
                                        var elementFilters = $(elements).filter('[data-clear="true"]');
                                        if(elementFilters.length > 0){
                                            $(elementFilters).clearFields();
                                            $(elementFilters).trigger("chosen:updated");
                                        }else{
                                            $(elements).clearFields();
                                            $(elements).trigger("chosen:updated");
                                        }

                                    }
                                });
                            }, 500);
                            // searchCovidResult();
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



function zfwc(fzjcid,sfje){
    $.confirm('确定已完成支付',function(result){
        if(result){
            jQuery.ajaxSetup({async:false});
            var url= "/detection/detection/pagedataNuonuoPay";
            jQuery.post(url,{fzjcid:fzjcid,sfje:sfje,zffs:"现金支付","access_token":$("#ac_tk").val()},function(responseText){
                setTimeout(function(){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            closePayModalZfje("payZfjeModal")
                        });
                    }else if(responseText["status"] == "fail"){
                        $.error(responseText["message"],function() {
                        });
                    } else{
                        $.alert(responseText["message"],function() {
                        });
                    }
                },1);
            },'json');
            jQuery.ajaxSetup({async:true});
        }
    });
}

function closePayModalZfje(modalName) {
    searchCovidResult();
    $.closeModal(modalName);
}
/*查看详情信息模态框*/
var payConfig = {
    width		: "400px",
    height		: "20px",
    modalName	: "payZfjeModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default",
        }
    }
};


var reportDownloadConfig={
    width		: "700px",
    modalName	: "reportCovidDownloadConfig",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		:	{
        success 	: 	{
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#ajaxForm").valid()){
                    $.alert("请填写完整信息！");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var bglxbj = $("#ajaxForm input[type='radio']:checked").val();
                var map = {
                    access_token:$("#ac_tk").val(),
                    bglxbj:bglxbj,
                };
                //判断有选中的采用选中导出，没有采用选择导出
                var sel_row = $('#covidformSearch #covid_list').bootstrapTable('getSelections');//获取选择行数据
                var ids="";
                if(sel_row.length>=1){
                    for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                        ids = ids + ","+ sel_row[i].fzjcid;
                    }
                    ids = ids.substr(1);
                    map["ids"] = ids;
                }else{
                    getCovidSearchData(map);
                }
                var url= $("#ajaxForm #action").val();
                $.post(url,map,function(data){
                    if(data){
                        if(data.status == 'success'){
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            //创建objectNode
                            var cardiv =document.createElement("div");
                            cardiv.id="cardiv";
                            var s_str = '<div class="modal-backdrop fade in" style="display: block; z-index: 9999;"><div align="center" style="top:45%"><img src="/js/plugins/backdrop/images/ico-loading.gif"><p><span id="exportCount" style="color:red;font-weight:600;">0</span><span id="totalCount" style="color:red;font-weight:600;">/' + data.count + '</span></p><p class="padding_t7"><button type="button" class="btn btn-primary" id="exportCancel">取消</button></p></div></div>';
                            cardiv.innerHTML=s_str;
                            //将上面创建的P元素加入到BODY的尾部
                            document.body.appendChild(cardiv);
                            setTimeout("checkReportZipStatus(2000,'"+ data.redisKey + "')",1000);
                            //绑定导出取消按钮事件
                            $("#exportCancel").click(function(){
                                //先移除导出提示，然后请求后台
                                if($("#cardiv").length>0) $("#cardiv").remove();
                                $.ajax({
                                    type : "POST",
                                    url : "/common/export/commCancelExport",
                                    data : {"wjid" : data.redisKey+"","access_token":$("#ac_tk").val()},
                                    dataType : "json",
                                    success:function(res){
                                        if(res != null && res.result==false){
                                            if(res.msg && res.msg!="")
                                                $.error(res.msg);
                                        }

                                    }
                                });
                            });
                        }else if(data.status == 'fail'){
                            $.error(data.error,function() {
                            });
                        }else{
                            preventResubmitForm(".modal-footer > button", false);
                            if(data.error){
                                $.alert(data.error,function() {
                                });
                            }
                        }
                    }
                },'json');
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
}

var reportConfig= {
    width		: "700px",
    height		: "100px",
    modalName   :"reportUploadModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确认",
            className : "btn-primary",
            callback : function() {
                if(!$("#ajaxForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#access_token").val($("#ac_tk").val());

                $("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            searchCovidResult();
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

//自动检查报告压缩进度
var checkReportZipStatus = function(intervalTime,redisKey){
    $.ajax({
        type : "POST",
        url : "/common/export/commCheckExport",
        data : {"wjid" : redisKey +"","access_token":$("#ac_tk").val()},
        dataType : "json",
        success:function(data){
            if(data.cancel){//取消导出则直接return
                return;
            }
            if(data.result == false){
                if(data.msg && data.msg!=""){
                    $.error(data.msg);
                    if($("#cardiv")) $("#cardiv").remove();
                }else{
                    if(intervalTime < 5000)
                        intervalTime = intervalTime + 1000;
                    if($("#exportCount")){
                        $("#exportCount").html(data.currentCount)
                        if(("/"+data.currentCount) == $("#totalCount").text()){
                            $("#totalCount").html($("#totalCount").text()+" 压缩中....")
                        }
                    }
                    setTimeout("checkReportZipStatus("+intervalTime+",'"+ redisKey +"')",intervalTime);
                }
            }else{
                if(data.msg && data.msg!=""){
                    $.error(data.msg);
                    if($("#cardiv")) $("#cardiv").remove();
                }
                else{
                    if($("#cardiv")) $("#cardiv").remove();
                    window.open("/common/export/commDownloadExport?wjid="+redisKey + "&access_token="+$("#ac_tk").val());
                }
            }
        }
    });
}


var reportCovidDetectionConfig= {
    width		: "500px",
    height		: "100px",
    modalName   :"modDetectionResultModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确认",
            className : "btn-primary",
            callback : function() {

                var $this = this;
                var opts = $this["options"]||{};
                $("#access_token").val($("#ac_tk").val());

                $.ajax({
                    type: 'post',
                    url: "/detection/detection/datareportToCity",
                    data: {"fzjcid": fzjcid, "access_token": $("#ac_tk").val()},
                    dataType: 'json',
                    success: function (data) {
                        if (data == true){

                        }
                        if(data.status=="success"){
                            $.success(data.message);
                            $.closeModal(opts.modalName);
                        }else if(data.status=="fail"){
                            $.error(data.message);
                        }
                    }
                });
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }

    }
};

var modDetectionResultConfig= {
    width		: "500px",
    height		: "100px",
    modalName   :"modDetectionResultModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确认",
            className : "btn-primary",
            callback : function() {
                if(!$("#modDetectionResultForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#access_token").val($("#ac_tk").val());

                $("#modDetectionResultForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"modDetectionResultForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                //提交审核
                                var auditType = $("#modDetectionResultForm #auditType").val();
                                var ids= $("#modDetectionResultForm #ids").val();
                                var ywids=ids.split(",");

                                showAuditFlowDialog(auditType,ywids,function(){
                                    searchCovidResult();
                                });
                                $.closeModal(opts.modalName);
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
/**修改**/
var editCovidDetectionConfig = {
    width		: "900px",
    height		: "600px",
    modalName   :"editCovidDetectionConfig",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确认",
            className : "btn-primary",
            callback : function(){
	 		if(!$("#editCovidForm").valid()){
					$.alert("请填写完整信息！");
					return false;
				}
				var otherHospital= $("#editCovidForm #sjdwmc").val();
				var yyxxcskz1= $("#editCovidForm #yyxxcskz1").val();
				if (yyxxcskz1 == '1') {
					if(otherHospital==null||otherHospital==''){
						$.alert("请填写其他单位。");
						return false;
					}
					
				}
				var kzcs = $("#editCovidForm #ks option:selected");
				var qtks= $("#editCovidForm #qtks").val();
				if (kzcs.attr("kzcs") == "1") {
					if(qtks==null||qtks==''){
						$.alert("请填写其他科室。");
						return false;
					}
				} 
				var $this = this;
				var opts = $this["options"]||{};
				$("#djrbj").val("0");//第几日标记,0全部
				$("#editCovidForm input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"editCovidForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchCovidResult();
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
/*查看详情信息模态框*/
var msgconfirmDetectionConfig = {
    width		: "800px",
    height		: "500px",
    modalName   :"msgConfirmDetectionModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "信息确认",
            className : "btn-primary",
            callback : function() {
                if(!$("#covidview_formSearch").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#access_token").val($("#ac_tk").val());
                var fzjcid = $("#ywid").val();

                $("#covidview_formSearch input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"covidview_formSearch",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchCovidResult();
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

var modDetectionConfig = {
    width		: "500px",
    modalName	: "modDetectionModal",
    formName	: "detection_Form",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success7 : {
            label : "保存",
            className : "btn-primary",
            callback : function() {
                if(!$("#detection_Form").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#detection_Form input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"detection_Form",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchCovidResult();
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
var confirmCovidConfig = {
    width: "1000px",
    modalName: "confirmCovidModal",
    offAtOnce: true,  //当数据提交成功，立刻关闭窗口
    onClose: function () {
        searchCovidResult();
    },
    buttons: {
        success: {
            label: "确 定",
            className: "btn-primary",
            callback: function () {
                if (!$("#sampleAcceptForm").valid()) {
                    return false;
                }
                // if ($("#sampleAcceptForm #sfjs1").is(":checked")) {
                //     if ($("#sampleAcceptForm #nbbh").val() == "") {
                //         $.alert("您已选择接收，请填写内部编码");
                //         return false;
                //     }
                // }
                function Toast(msg, el, t) {
                    var ele = document.querySelector(el);
                    var Ospan = ele.querySelector('span')
                    // console.log(ele,Ospan)
                    if (typeof msg === 'string') {
                        Ospan.innerHTML=msg;
                        ele.style.display = 'block';
                        setTimeout(()=>{
                            ele.style.display = 'none';
                        }, t ? t : 2000);
                    } else {
                        console.warn("param msg must be a String !");
                    }
                }
                var nbbh = $("#sampleAcceptForm #nbbh").val();
                $("#sampleAcceptForm #px").val(nbbh.substring(1, nbbh.length - 1));
                var $this = this;
                var opts = $this["options"] || {};
                $("#sampleAcceptForm input[name='access_token']").val($("#ac_tk").val());
                var szz = $('#sampleAcceptForm input:radio[name="szz"]:checked').val();
                if (szz == $("#sampleAcceptForm #ysszz").val()) {
                    $("#sampleAcceptForm #grsz_flg").val(0);
                } else {
                    $("#sampleAcceptForm #grsz_flg").val(1);
                }
                var flag = 0;
                $("#sampleAcceptForm .ybztclass").each(function () {
                    if (this.checked == true) {
                        flag++;
                    }
                });
                if (flag > 0) {
                    $.confirm("当前标本状态异常，是否继续确认？", function (result) {
                        if (result) {
                            submitForm(opts["formName"] || "sampleAcceptForm", function (responseText, statusText) {
                                if (responseText["status"] == 'success') {
                                    preventResubmitForm(".modal-footer > button", false);

                                    if (responseText["print"] && responseText["sz_flg"]) {
                                        let ResponseSign = responseText["ResponseSign"];
                                        let type = responseText["type"];
                                        let RequestLocalCode = responseText["RequestLocalCode"];
                                        let nbbh = responseText["nbbh"];
                                        let ybbh = responseText["ybbh"];
                                        let syh = responseText["syh"];
                                        let sz_flg = responseText["sz_flg"];
                                        print_nbbh(sz_flg,ResponseSign,type,RequestLocalCode,nbbh,ybbh,syh);
                                    }

                                    // $.success(responseText["message"], function () {
                                    Toast(responseText["message"],"#sampleAcceptForm #toast");
                                        $("#sampleAcceptForm #ybbh").val("");
                                        $("#sampleAcceptForm #nbbh").val("");
                                        t_map.rows = [];
                                        $("#sampleAcceptForm #hzxx_list").bootstrapTable('load',t_map);
                                        $("#sampleAcceptForm #syh").val("");
                                        searchCovidResult();
                                    // });
                                } else if (responseText["status"] == "fail") {
                                    preventResubmitForm(".modal-footer > button", false);
                                    Toast(responseText["message"],"#sampleAcceptForm #toast");
                                    // $.error(responseText["message"], function () {
                                    // });
                                } else {
                                    preventResubmitForm(".modal-footer > button", false);
                                    // $.alert("不好意思！出错了！");
                                    Toast("不好意思！出错了！","#sampleAcceptForm #toast");
                                }
                            }, ".modal-footer > button");
                        }
                    });
                } else {
                    submitForm(opts["formName"] || "sampleAcceptForm", function (responseText, statusText) {
                        if (responseText["status"] == 'success') {
                            preventResubmitForm(".modal-footer > button", false);
                            if (responseText["print"] && responseText["sz_flg"]) {
                                let ResponseSign = responseText["ResponseSign"];
                                let type = responseText["type"];
                                let RequestLocalCode = responseText["RequestLocalCode"];
                                let nbbh = responseText["nbbh"];
                                let ybbh = responseText["ybbh"];
                                let syh = responseText["syh"];
                                let sz_flg = responseText["sz_flg"];
                                print_nbbh(sz_flg,ResponseSign,type,RequestLocalCode,nbbh,ybbh,syh);
                            }

                            // $.success(responseText["message"], function () {
                            Toast(responseText["message"],"#sampleAcceptForm #toast");
                                $("#sampleAcceptForm #ybbh").val("");
                                $("#sampleAcceptForm #nbbh").val("");
                                t_map.rows = [];
                                $("#sampleAcceptForm #hzxx_list").bootstrapTable('load',t_map);
                                $("#sampleAcceptForm #syh").val("");
                                searchCovidResult();
                            // });
                        } else if (responseText["status"] == "fail") {
                            preventResubmitForm(".modal-footer > button", false);
                            Toast(responseText["message"],"#sampleAcceptForm #toast");
                            // $.error(responseText["message"], function () {
                            // });
                        } else {
                            preventResubmitForm(".modal-footer > button", false);
                            // $.alert("不好意思！出错了！");
                            Toast("不好意思！出错了！","#sampleAcceptForm #toast");
                        }
                    }, ".modal-footer > button");
                }
                return false;
            }
        },
        cancel: {
            label: "关 闭",
            className: "btn-default",
            callback: function () {
                searchCovidResult();
            }
        },
    }
}
function print_nbbh(sz_flg,ResponseSign,type,RequestLocalCode,nbbh,ybbh,syh){
    var print_url=null
    if(sz_flg=="0"){
        print_url="http://localhost:8081/XGQYPrint";
    }else if(sz_flg=="1"){
        var glxx=$("#sampleAcceptForm #glxx").val();
        print_url="http://"+glxx+":8082/XGQYPrint";
    }
    openWindow = window.open(print_url+"?nbbh="+nbbh+"&ybbh="+ybbh+"&syh="+syh+"&ResponseNum=3&RequestLocalCode="+RequestLocalCode+"&ResponseSign="+ResponseSign+"&type="+type+ "&access_token="+ $("#ac_tk").val());
    setTimeout(function(){
        openWindow.close();
    }, 300);
    // let html ="<iframe id='test' src='"+print_url+"?nbbh="+nbbh+"&ybbh="+ybbh+"&syh="+syh+"&ResponseNum=3&RequestLocalCode="+RequestLocalCode+"&ResponseSign="+ResponseSign+"&type="+type+ "&access_token="+ $("#ac_tk").val()+"' width='0px' height='0px'></iframe>"
    // let html ="<img id='test' crossorigin='anonymous' src='"+print_url+"?nbbh="+nbbh+"&ybbh="+ybbh+"&syh="+syh+"&ResponseNum=3&RequestLocalCode="+RequestLocalCode+"&ResponseSign="+ResponseSign+"&type="+type+ "&access_token="+ $("#ac_tk").val()+"' width='0px' height='0px'></img>"
    // $("#sampleAcceptForm #iframePrint").empty();
    // $("#sampleAcceptForm #iframePrint").append(html);
}

/**
 * 操作按钮格式化函数
 * @returns
 */
function czFormat(value,row,index) {
    if (row.zt == '10' ) {
        return "<span class='btn btn-warning' onclick=\"recallRequisitions('" + row.fzjcid +"','" + $("#covidformSearch #auditType").val()+ "',event)\" >撤回</span>";
    }else{
        return "";
    }
}

//撤回请购单提交
function recallRequisitions(fzjcid,auditType,event){
    var msg = '您确定要撤回该记录吗？';
    var purchase_params = [];
    $.confirm(msg,function(result){
        if(result){
            doAuditRecall(auditType,fzjcid,function(){
                searchCovidResult();
            },purchase_params);
        }
    });
}
/**
 * 审核列表的状态格式化函数
 * @returns
 */
function ztFormat(value,row,index) {
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.fzjcid + "\",event,\"AUDIT_COVID\")' >审核通过</a>";
    }else if (row.zt == '15') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.fzjcid + "\",event,\"AUDIT_COVID\")' >审核未通过</a>";
    }else if(row.zt == '10'){
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.fzjcid + "\",event,\"AUDIT_COVID\")' >" + row.shxx_dqgwmc + "审核中</a>";
    }
    else{
        return '未提交';
    }
}

//标记格式化
function sfjsformat(value,row,index){
    var html="";
    if(row.sfjs=='1'){
        html="<span style='color:green;'>"+"是"+"</span>";
    }else{
        html="<span style='color:red;'>"+"否"+"</span>";
    }
    return html;
}
//是否交付格式化
function sfjfformat(value,row,index){
    var html="";
    if(row.sfjf=='1'){
        html="<span style='color:green;'>"+"是"+"</span>";
    }else{
        html="<span style='color:red;'>"+"否"+"</span>";
    }
    return html;
}

//标记格式化
function fkbjformat(value,row,index){
    var html="";
    if(row.fkbj=='1'){
        html="<span style='color:green;'>"+"已付款"+"</span>";
    }else if(row.fkbj=='0'){
        html="<span style='color:red;'>"+"未付款"+"</span>";
    }else if(row.fkbj=='2'){
        html="<span style='color:blue;'>"+"退款成功"+"</span>";
    }else if(row.fkbj=='3'){
        html="<span style='color:blue;'>"+"退款中"+"</span>";
    }
    return html;
}

//标记格式化
function kpbjformat(value,row,index){
    var html="";
    if(row.kpbj=='1'){
        html="<span style='color:green;'>"+"已开票"+"</span>";
    }else{
        html="<span style='color:red;'>"+"未开票"+"</span>";
    }
    return html;
}

//标记格式化
function scbjformat(value,row,index){
    var html="";
    if(row.scbj=='2'){
        html="<span style='color:red;'>"+"废弃"+"</span>";
    }
    return html;
}
//标记格式化
function sfsyformat(value,row,index){
    var html="";
    if(row.sfsy=='1'){
        html="<span style='color:green;'>"+"是"+"</span>";
    }else{
        html="<span style='color:red;'>"+"否"+"</span>";
    }
    return html;
}
//标记格式化
function fsbjformat(value,row,index){
    var html="";
    if(row.fsbj=='1'){
        html="<span style='color:green;'>"+"是"+"</span>";
    }else{
        html="<span style='color:red;'>"+"否"+"</span>";
    }
    return html;
}

function jcdxformat(value,row,index){
    var html="";
    if(row.jcdxcsdm=='W'){
        html="<span>"+"物检"+"</span>";
    }else if(row.jcdxcsdm=='R'){
        html="<span>"+"人检"+"</span>";
    }
    return html;
}

function xmformat(value,row,index){
    if(row.jcdxcsdm=='W'){
        return row.ypmc;
    }else{
        return row.xm;
    }
}

function openPostWindow(url,params){
    var tempForm = document.createElement("form");
    tempForm.id = "tempForm1";
    tempForm.method = "post";
    tempForm.action = url;
    tempForm.target="_blank"; //打开新页面

    for(var key in params){
        tempForm.appendChild(generateInput(key,params));
    }
    if(document.all){
        tempForm.attachEvent("onsubmit",function(){});//IE
    }else{
        var subObj = tempForm.addEventListener("submit",function(){},false);//firefox
    }
    document.body.appendChild(tempForm);
    if(document.all){
        tempForm.fireEvent("onsubmit");
    }else{
        tempForm.dispatchEvent(new Event("submit"));
    }
    tempForm.submit();
    document.body.removeChild(tempForm);
}

function generateInput(key,params){
    var hideInput = document.createElement("input");
    hideInput.type = "hidden";
    hideInput.name = key;
    hideInput.value = params[key];
    return hideInput;
}

var Covid_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};
    oInit.Init = function () {
        //初始化页面上面的按钮事件
        var btn_view = $("#covidformSearch #btn_view");//详情
        var btn_accept =$("#covidformSearch #btn_accept");//样本接收
        var btn_query =$("#covidformSearch #btn_query");//模糊查询
	    var btn_submit=$("#covidformSearch #btn_submit");
        var btn_selectexport = $("#covidformSearch #btn_selectexport");//选中导出
        var btn_searchexport = $("#covidformSearch #btn_searchexport");//搜索导出
        var btn_upload= $("#covidformSearch #btn_upload");//上传分子检测报告
        var btn_detection=$("#covidformSearch #btn_detection");//检测
        var btn_msgconfirm=$("#covidformSearch #btn_msgconfirm");//信息确认按钮
        var btn_mod=$("#covidformSearch #btn_mod");//修改
        var btn_del=$("#covidformSearch #btn_del");//删除
        var btn_resultMod=$("#covidformSearch #btn_resultmod");//修改检测结果
        var btn_report=$("#covidformSearch #btn_report");//核酸检测报告上传至市里
        var btn_reportgenerate=$("#covidformSearch #btn_reportgenerate");//重新生成新冠报告
        var btn_amountselectexport = $("#covidformSearch #btn_amountselectexport");//选中导出
        var btn_amountsearchexport = $("#covidformSearch #btn_amountsearchexport");//搜索导出
        var btn_discard = $("#covidformSearch #btn_discard");//废弃
        var btn_add = $("#covidformSearch #btn_add");//新增
        var btn_payment = $("#covidformSearch #btn_payment");//付款
        var btn_reportdownload = $("#covidformSearch #btn_reportdownload");//报告下载
        var btn_lock=$("#covidformSearch #btn_lock");//阳性锁定
        var btn_deliverability=$("#covidformSearch #btn_deliverability");//修改交付状态
        var btn_historicalrecords=$("#covidformSearch #btn_historicalrecords");//历史记录
        var btn_checklist=$("#covidformSearch #btn_checklist");//物检清单
        var btn_cjsjreport=$("#covidformSearch #btn_cjsjreport");//采集上传

        //添加日期控件
        laydate.render({
            elem: '#cjsjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#cjsjend'
            ,theme: '#2381E9'
        });
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchCovidResult(true);
            });
        };
        //添加日期控件
        laydate.render({
            elem: '#jssjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#jssjend'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#sysjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#sysjend'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#bgrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#bgrqend'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#yyrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#yyrqend'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#fkrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#fkrqend'
            ,theme: '#2381E9'
        });

        /*--------------------------------物检清单---------------------------*/
        btn_checklist.unbind("click").click(function (){
            let jcdxlx = jQuery('#covidformSearch #jcdxlx_id_tj').val();
            if ($("#jcdxlx_id_"+jcdxlx).attr("csdm") == "W"){
                let map = {
                    access_token: $("#ac_tk").val()
                };
                map =  getCovidSearchData(map);
                $.ajax({
                    type:'post',
                    url:"/detection/detection/pagedataChecklistSize",
                    cache: false,
                    data: map,
                    dataType:'json',
                    success:function(data){
                        if (data.total > 1000){
                            $.error("数量超过1000，请增加筛选条件！");
                        }else if (data.total == 0){
                            $.error("数量为0");
                        }else{
                            CovidDealById(null,"checklist",btn_checklist.attr("tourl"),$("#covidformSearch #jclx").val());
                        }
                    }
                })

            }else {
                $.error("筛选条件里检测类型不是物品！");
            }
        });
        /*--------------------------------修改交付---------------------------*/
        btn_deliverability.unbind("click").click(function (){
            var sel_row=$('#covidformSearch #covid_list').bootstrapTable('getSelections');
            if(sel_row.length>=1){
                var ids="";
                for(var i=0;i<sel_row.length;i++){//xmread != null && xmread != '' && xmread != undefined
                    if(sel_row[i].zt!='80' || sel_row[i].scbj !='0' ||
                        sel_row[i].cjsj==null||sel_row[i].cjsj==''||sel_row[i].cjsj==undefined||
                        sel_row[i].jssj==null||sel_row[i].jssj==''||sel_row[i].jssj==undefined||
                        sel_row[i].sysj==null||sel_row[i].sysj==''||sel_row[i].sysj==undefined||
                        sel_row[i].bgrq==null||sel_row[i].bgrq==''||sel_row[i].bgrq==undefined){
                        $.error("请选择审核通过且未被废弃删除的数据!");
                        return;
                    }
                    ids=ids+","+sel_row[i].fzjcid;
                }
                ids=ids.substring(1);
                CovidDealById(ids,"delivery",btn_deliverability.attr("tourl"));
            }else{
                $.error("请至少选中一行");
            }
        });
        /*--------------------------------新增---------------------------*/
        btn_add.unbind("click").click(function(){
            CovidDealById(null,"add",btn_add.attr("tourl"));
        });
        /*----------------------------核酸报告上传至市里面-------------------------------------*/
        btn_report.unbind("click").click(function(){
            var sel_row=$('#covidformSearch #covid_list').bootstrapTable('getSelections');
            if(sel_row.length>=1){
                var ids="";
                for(var i=0;i<sel_row.length;i++){
                    if(!sel_row[i].xm || !sel_row[i].xb || !sel_row[i].nl || !sel_row[i].zjh || !sel_row[i].zjlxmc ){
                        $.error("标本子编号为"+sel_row[i].bbzbh+ "的个人信息不充足，是否缺少姓名、性别、年龄、证件类型等信息！");
                        return
                    }else if(!sel_row[i].sqdsj || !sel_row[i].jssj || !sel_row[i].sysj || !sel_row[i].cjsj || !sel_row[i].bgrq || !sel_row[i].syrymc || !sel_row[i].jcjgmc || !sel_row[i].bbzbh|| !sel_row[i].syh){
                        $.error("标本子编号为"+sel_row[i].bbzbh+ "的个人信息不充足，是否缺少接收时间、实验时间、采集时间、报告日期、实验人员、检测结果等信息！");
                        return
                    }
                    ids=ids+","+sel_row[i].fzjcid;
                }
                ids=ids.substring(1);
                CovidDealById(ids,"report",btn_report.attr("tourl"));
            }else{
                $.error("请至少选中一行");
            }
        });
        /*---------------------------采集时间上报至卫健-----------------------------------*/
        btn_cjsjreport.unbind("click").click(function(){
            var sel_row=$('#covidformSearch #covid_list').bootstrapTable('getSelections');
            if (sel_row.length==0){
                $.error("请至少选中一行")
            }else if (sel_row.length>0 && sel_row.length<=100){
                var ids = "";
                for (var i = 0; i < sel_row.length; i++) {
                    if (!sel_row[i].xm || !sel_row[i].zjh || !sel_row[i].zjlxmc || !sel_row[i].sj || !sel_row[i].hzid) {
                        $.error("标本子编号为" + sel_row[i].bbzbh + "的个人信息不充足，是否缺少姓名、证件号、证件类型、手机等信息！");
                        return
                    } else if (!sel_row[i].bbzbh || !sel_row[i].cjsj) {
                        $.error("标本子编号为" + sel_row[i].bbzbh + "的实验信息不充足，是否缺少标本子编号、采集时间等信息！");
                        return
                    }
                    ids = ids + "," + sel_row[i].fzjcid;
                }
                ids = ids.substring(1);
                // CovidDealById(ids,"cjsjreport",btn_cjsjreport.attr("tourl"));
                $.confirm('您确定要上传选中数据的采集信息到卫健吗？', function (result) {
                    if (result) {
                        jQuery.ajaxSetup({async: false});
                        var url = btn_cjsjreport.attr("tourl");
                        jQuery.post(url, {ids: ids, "access_token": $("#ac_tk").val()}, function (responseText) {
                            setTimeout(function () {
                                if (responseText["status"] == 'success') {
                                    $.success(responseText["message"], function () {
                                        searchCovidResult();
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
            }else {
                $.error("一次最多上传卫健100条采集信息");
            }
        });
        /*---------------------------上传分子检测数据报告页面-----------------------------------*/
        btn_upload.unbind("click").click(function(){
            CovidDealById("","upload",btn_upload.attr("tourl"));
        })
        /*--------------------------------修改检测状态-----------------------------*/
        btn_detection.unbind("click").click(function(){
            var sel_row = $('#covidformSearch #covid_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else{
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {
                    if(sel_row[i].sfjs!='1'||sel_row[i].jssj==null){
                        $.error("有标本未接收，不允许实验操作");
                        return;
                    }
                    ids= ids + ","+ sel_row[i].fzjcid;
                }
                ids=ids.substr(1);
                var checkjcxm=true;
                $.ajax({
                    type:'post',
                    url:"/detection/detection/pagedataCheckJcxm",
                    cache: false,
                    data: {"ids":ids,"access_token":$("#ac_tk").val(),"jclx":$("#covidformSearch #jclx").val()},
                    dataType:'json',
                    success:function(data){
                        //返回值
                        if(data==false){
                            $.error("检测项目必须相同!");
                        }else{
                            CovidDealById(ids,"detection",btn_detection.attr("tourl"));
                        }
                    }
                });
            }
        });
        /*--------------------------------修改检测结果---------------------------*/
        btn_resultMod.unbind("click").click(function(){
            var sel_row=$('#covidformSearch #covid_list').bootstrapTable('getSelections');
            if(sel_row.length>=1){
                // var ids="";
                var ybbhs="";
                for(var i=0;i<sel_row.length;i++){
                    if(sel_row[i].sfsy!='1'){
                        $.error("标本编号为"+sel_row[i].ybbh+"的样本未实验！");
                        return;
                    }else if (sel_row[i].scbj=='3'){
                        $.error("标本编号为"+sel_row[i].ybbh+"的样本不允许修改结果！");
                        return;
                    }else{
                        if(sel_row[i].zt!='00' && sel_row[i].zt!='15') {
                            $.error("标本编号为"+sel_row[i].ybbh+"的数据，该状态不允许提交！");
                            return;
                        }
                    }
                    // ids=ids+","+sel_row[i].fzjcid;
                    ybbhs=ybbhs+","+sel_row[i].ybbh;
                }
                // ids=ids.substring(1);
                ybbhs=ybbhs.substring(1);
                //此处采用标本编号，需要更新标本编号相同的样本的检测结果
                CovidDealById(ybbhs,"resultMod",btn_resultMod.attr("tourl"));

            }else{
                $.error("请至少选中一行");
            }
        });

        /*---------------------------历史记录-----------------------------------*/
        btn_historicalrecords.unbind("click").click(function(){
            var sel_row = $('#covidformSearch #covid_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                CovidDealById(sel_row[0].fzjcid,"historicalrecords",btn_historicalrecords.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------查看详情信息表-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#covidformSearch #covid_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                CovidDealById(sel_row[0].fzjcid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------确认按钮点击先调用查看页面后在确认----------------*/
        btn_msgconfirm.unbind("click").click(function(){
            var sel_row = $('#covidformSearch #covid_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                CovidDealById(sel_row[0].fzjcid,"msgconfirm",btn_msgconfirm.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* ------------------------------删除-----------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#covidformSearch #covid_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].fzjcid;
                }
                ids=ids.substr(1);
                $.confirm('您确定要删除所选择的信息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= btn_del.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchCovidResult();
                                    });
                                }else if(responseText["status"] == "fail"){
                                    $.error(responseText["message"],function() {
                                    });
                                } else{
                                    $.alert(responseText["message"],function() {
                                    });
                                }
                            },1);

                        },'json');
                        jQuery.ajaxSetup({async:true});
                    }
                });
            }
        });
        /* ------------------------------废弃-----------------------------*/
        btn_discard.unbind("click").click(function(){
            var sel_row = $('#covidformSearch #covid_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].fzjcid;
                }
                ids=ids.substr(1);
                $.confirm('您确定要废弃所选择的信息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= btn_discard.attr("tourl")+"?jclx="+$("#covidformSearch #jclx").val();
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchCovidResult();
                                    });
                                }else if(responseText["status"] == "fail"){
                                    $.error(responseText["message"],function() {
                                    });
                                } else{
                                    $.alert(responseText["message"],function() {
                                    });
                                }
                            },1);

                        },'json');
                        jQuery.ajaxSetup({async:true});
                    }
                });
            }
        });
        /* ------------------------------生成新冠报告-----------------------------*/
        btn_reportgenerate.unbind("click").click(function(){
            var sel_row = $('#covidformSearch #covid_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    if(sel_row[i].zt!='80'){
                        $.error("请选择审核通过的数据!");
                        return;
                    }
                    ids= ids + ","+ sel_row[i].fzjcid;
                }
                ids=ids.substr(1);
                $.confirm('您确定要重新生成新冠报告吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= btn_reportgenerate.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val(),"jclx":$("#covidformSearch #jclx").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchCovidResult();
                                    });
                                }else if(responseText["status"] == "fail"){
                                    $.error(responseText["message"],function() {
                                    });
                                } else{
                                    $.alert(responseText["message"],function() {
                                    });
                                }
                            },1);

                        },'json');
                        jQuery.ajaxSetup({async:true});
                    }
                });
            }
        });
        /*---------------------------查看详情信息表-----------------------------------*/
        btn_accept.unbind("click").click(function(){
            var sel_row = $('#covidformSearch #covid_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                if (sel_row[0].jcdxcsdm == "W"){//物检
                    if (!sel_row[0].cjsj){
                        $.error("该用户还未录入！");
                    }else if (!sel_row[0].ybbh){
                        $.error("标本编号不可为空！");
                    }else{
                        CovidDealById(sel_row[0].ybbh,"accept",btn_accept.attr("tourl"));
                    }
                }else {//人检
                    if (!sel_row[0].cjsj){
                        $.error("该用户还未录入！");
                    }else if (!sel_row[0].xm || !sel_row[0].xb || !sel_row[0].nl || !sel_row[0].zjh || !sel_row[0].zjlxmc  ){
                        $.error("个人信息不充足，是否缺少姓名、性别、年龄、证件号、证件类型等信息！");
                    }else if (!sel_row[0].ybbh){
                        $.error("标本编号不可为空！");
                    }
                    else{
                        CovidDealById(sel_row[0].ybbh,"accept",btn_accept.attr("tourl"));
                    }
                }
            }else{
                CovidDealById(null,"accept",btn_accept.attr("tourl"));
            }
        });
        /*--------------------------------提交---------------------------*/
        btn_submit.unbind("click").click(function(){
            var sel_row=$('#covidformSearch #covid_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                if(sel_row[0].zt=="00" || sel_row[0].zt=="15"){
                    CovidDealById(sel_row[0].fzjcid,"submit",btn_submit.attr("tourl"));
                }else{
                    $.alert("该状态不允许提交!");
                }
            }else{
                $.error("请选中一行");
            }
        });
 		/*--------------------------------修改---------------------------*/
 		btn_mod.unbind("click").click(function(){
            var sel_row=$('#covidformSearch #covid_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
	            CovidDealById(sel_row[0].fzjcid,"mod",btn_mod.attr("tourl"),sel_row[0].fzxmid);
                /*if(sel_row[0].zt=="00" || sel_row[0].zt=="15"){
                }else{
                    $.alert("该状态不允许提交!");
                }*/
            }else{
                $.error("请选中一行");
            }
        });
        /*--------------------------------付款---------------------------*/
        btn_payment.unbind("click").click(function(){
            var sel_row=$('#covidformSearch #covid_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                if(sel_row[0].fkbj=="1"){
                    $.alert("已完成支付!");
                }else{
                    CovidDealById(sel_row[0].fzjcid,"payment",btn_payment.attr("tourl"));
                }
            }else{
                $.error("请选中一行");
            }
        });
        //---------------------------------选中导出---------------------------------------
        btn_selectexport.unbind("click").click(function(){
            var sel_row = $('#covidformSearch #covid_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length>=1){
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].fzjcid;
                }
                ids = ids.substr(1);
                $.showDialog(exportPrepareUrl+"?ywid=COVID_SELECT&expType=select&ids="+ids+"&jclx="+$("#covidformSearch #jclx").val()
                    ,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
            }else{
                $.error("请选择数据");
            }
        });
        //搜索导出
        btn_searchexport.unbind("click").click(function(){
            $.showDialog(exportPrepareUrl+"?ywid=COVID_SEARCH&expType=search&callbackJs=covidSearchData"+"&jclx="+$("#covidformSearch #jclx").val()
                ,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
        });
        //---------------------------------选中导出(全)---------------------------------------
        btn_amountselectexport.unbind("click").click(function(){
            var sel_row = $('#covidformSearch #covid_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length>=1){
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].fzjcid;
                }
                ids = ids.substr(1);
                $.showDialog(exportPrepareUrl+"?ywid=COVID_AMOUNTSELECT&expType=select&ids="+ids+"&jclx="+$("#covidformSearch #jclx").val()
                    ,btn_amountselectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
            }else{
                $.error("请选择数据");
            }
        });
        //搜索导出
        btn_amountsearchexport.unbind("click").click(function(){
            $.showDialog(exportPrepareUrl+"?ywid=COVID_AMOUNTSEARCH&expType=search&callbackJs=covidSearchData"+"&jclx="+$("#covidformSearch #jclx").val()
                ,btn_amountsearchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
        });

        //------------------------------------- 报告下载 ---------------------------------------
        btn_reportdownload.unbind("click").click(function(){
            //判断有选中的采用选中导出，没有采用选择导出
            var sel_row = $('#covidformSearch #covid_list').bootstrapTable('getSelections');//获取选择行数据
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                ids = ids + ","+ sel_row[i].fzjcid;
            }
            ids = ids.substr(1);
            CovidDealById(ids,"reportdownload",btn_reportdownload.attr("tourl"));
        });

        /* ------------------------------锁定-----------------------------*/
        btn_lock.unbind("click").click(function(){
            var sel_row = $('#covidformSearch #covid_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].fzjcid;
                }
                ids=ids.substr(1);
                $.confirm('是否锁定选中标本，锁定后该标本无法修改结果，无法提交审批!',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= btn_lock.attr("tourl")+"?jclx="+$("#covidformSearch #jclx").val();
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchCovidResult();
                                    });
                                }else if(responseText["status"] == "fail"){
                                    $.error(responseText["message"],function() {
                                    });
                                } else{
                                    $.alert(responseText["message"],function() {
                                    });
                                }
                            },1);

                        },'json');
                        jQuery.ajaxSetup({async:true});
                    }
                });
            }
        });

        /**显示隐藏**/
        $("#covidformSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(Covid_turnOff){
                $("#covidformSearch #searchMore").slideDown("low");
                Covid_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#covidformSearch #searchMore").slideUp("low");
                Covid_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });
    };
    return oInit;
};

function covidSearchData(){
    var map ={};
    map["access_token"]=$("#ac_tk").val();
    map["sortLastName"]="covid.sysj";
    map["sortLastOrder"]="asc";
    map["sortName"]="covid.ybbh";
    map["sortOrder"]="asc";
    return getCovidSearchData(map);
}


var submitCovidConfig = {
    width		: "900px",
    modalName	: "submitCovidConfig",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                //提交审核
                var auditType = $("#covidformSearch #auditType").val();
                var ywid= $("#covidview_formSearch #ywid").val();
                showAuditFlowDialog(auditType,ywid,function(){
                    searchCovidResult();
                });
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

/*查看详情信息模态框*/
var viewCovidConfig = {
    width		: "1200px",
    height		: "500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
/*查看详情信息模态框*/
var historicalrecordsViewCovidConfig = {
    width		: "1600px",
    height		: "500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

/**
 *
 */
var uploadCovidReportConfig = {
    width		: "600px",
    modalName	: "uploadCovidReportModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "上传",
            className : "btn-primary",
            callback : function() {
                if(!$("#uploadDetectionCovidForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};

                $("#uploadDetectionCovidForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"uploadDetectionCovidForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                //提交报告审核
                                var auditType = $("#uploadDetectionCovidForm #auditType").val();
                                var ywid = $("#uploadDetectionCovidForm #fzjcid").val();
                                showAuditFlowDialog(auditType,ywid,function(){
                                    $.closeModal(opts.modalName);
                                    searchCovidResult();
                                },null,null);
                                $.closeModal(opts.modalName);
                            }
                        });
                    }else if(responseText["status"] == "fail"){
                        $.error(responseText["message"],function() {
                            preventResubmitForm(".modal-footer > button", false);
                        });
                    } else{
                        $.alert(responseText["message"],function() {
                            preventResubmitForm(".modal-footer > button", false);
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

function searchCovidResult(isTurnBack){
    //关闭高级搜索条件
    $("#covidformSearch #searchMore").slideUp("low");
    Covid_turnOff=true;
    $("#covidformSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#covidformSearch #covid_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#covidformSearch #covid_list').bootstrapTable('refresh');
    }
}

$(function(){
    var oInit = new covid_PageInit();
    oInit.Init();
    //0.界面初始化
    // 1.初始化Table
    var oTable = new Covid_TableInit(FieldList);
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new Covid_ButtonInit();
    oButtonInit.Init();
    // 所有下拉框添加choose样式
    jQuery('#covidformSearch .chosen-select').chosen({width: '100%'});
    $("#covidformSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
});
