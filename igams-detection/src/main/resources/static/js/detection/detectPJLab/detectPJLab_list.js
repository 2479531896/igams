var url = '/detectPJLab/detectPJLab/pageGetListDetectPJlab';
var detectPJLab_turnOff=true;
var detectPJLab_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#detectPJLab_formSearch #detectPJLab_list").bootstrapTable({
            url: url,         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#detectPJLab_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "fzjcxx.jssj",				// 排序字段
            sortOrder: "desc",                   // 排序方式
            queryParams: oTableInit.queryParams,// 传递参数（*）
            sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       // 初始化加载第一页，默认第一页
            pageSize: 15,                       // 每页的记录行数（*）
            pageList: [15, 30, 50, 100,1000],        // 可供选择的每页的行数（*）
            paginationPreText: '‹',				// 指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			// 指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  // 是否显示所有的列
            showRefresh: true,                  // 是否显示刷新按钮
            minimumCountColumns: 2,             // 最少允许的列数
            clickToSelect: true,                // 是否启用点击选中行
            // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "fzjcid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true
            }, {
                'field': '',
                'title': '序号',
                'align': 'center',
                'width': '3%',
                'formatter': function (value, row, index) {
                    var options = $('#detectPJLab_formSearch #detectPJLab_list').bootstrapTable('getOptions');
                    return options.pageSize * (options.pageNumber - 1) + index + 1;
                }
            },{
                field: 'fzjcid',
                title: '分子检测ID',
                titleTooltip:'分子检测ID',
                width: '8%',
                align: 'left',
                visible:false
            },{
                field: 'ybbh',
                title: '标本编号',
                titleTooltip:'标本编号',
                width: '12%',
                align: 'left',
                visible:true,
                sortable:true
            },{
                field: 'syh',
                title: '实验号',
                titleTooltip:'实验号',
                width: '12%',
                align: 'left',
                visible:true,
                sortable:true
            },{
                field: 'jcxmmc',
                title: '检测项目',
                titleTooltip:'检测项目',
                width: '12%',
                align: 'left',
                visible:true,
                sortable:true
            },{
                field: 'xm',
                title: '姓名',
                titleTooltip:'姓名',
                width: '6%',
                align: 'left',
                visible:true,
                sortable:true
            },{
                field: 'sjdwmc',
                title: '医院名称',
                titleTooltip:'医院名称',
                width: '12%',
                align: 'left',
                visible:true,
                sortable:true
            },{
                field: 'nl',
                title: '年龄',
                titleTooltip:'年龄',
                width: '5%',
                align: 'left',
                visible:true,
                sortable:true
            },{
                field: 'xb',
                title: '性别',
                titleTooltip:'性别',
                width: '5%',
                align: 'left',
                formatter:xbformat,
                visible:true,
                sortable:true
            },{
                field: 'yblxmc',
                title: '标本类型',
                titleTooltip:'标本类型',
                width: '8%',
                align: 'left',
                visible:true,
                sortable:true
            },{
                field: 'bbztmc',
                title: '标本状态',
                titleTooltip:'标本状态',
                width: '10%',
                align: 'left',
                visible:true,
                sortable:true
            },{
                field: 'jcdwmc',
                title: '检测单位',
                titleTooltip:'检测单位',
                width: '12%',
                align: 'left',
                visible:false,
                sortable:true
            },{
                field: 'jssj',
                title: '接收时间',
                titleTooltip:'接收时间',
                width: '12%',
                align: 'left',
                visible:true,
                sortable:true
            },{
                field: 'sysj',
                title: '实验时间',
                titleTooltip:'实验时间',
                width: '12%',
                align: 'left',
                visible:true,
                sortable:true
            },{
                field: 'bgrq',
                title: '报告日期',
                titleTooltip:'报告日期',
                width: '10%',
                align: 'left',
                visible:false,
                sortable:true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                detectPJLab_DealById(row.fzjcid,'view',$("#detectPJLab_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#detectPJLab_formSearch #detectPJLab_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true}
        );
    };
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "fzjcxx.fzjcid", // 防止同名排位用
            sortLastOrder: "asc" ,// 防止同名排位用
            jclx:$("#detectPJLab_formSearch #jclx").val()
            // 搜索框使用
            // search:params.search
        };
        return getdetectPJLabSearchData(map);
    };
    return oTableInit;
}

var detect_PageInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        var jcdxlx=$("#detectPJLab_formSearch a[id^='jcdxlx_id_']")

        $.each(jcdxlx, function(i, n){
            var id = $(this).attr("id");
            var code = id.substring(id.lastIndexOf('_')+1,id.length);
            var csdm=$("#detectPJLab_formSearch #"+id).attr("csdm");
            if(csdm == 'R'){
                addTj('jcdxlx',code,'detectPJLab_formSearch');
            }
        });
    }
    return oInit;
}

var flag='2';
function clickFlagPJ(flagValue){
    flag = flagValue;
    searchdetectPJLabResult(true);
}
function getdetectPJLabSearchData(map){
    var cxtj=$("#detectPJLab_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#detectPJLab_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["ybbh"]=cxnr
    }else if(cxtj=='1'){
        map["xm"]=cxnr
    }else if(cxtj=='2'){
        map["syh"]=cxnr
    }else if(cxtj=='3'){
        map["sjdwmc"]=cxnr
    }else if(cxtj=='4'){
        map["nl"]=cxnr
    }

    // var zts = jQuery('#detectPJ_formSearch #zt_id_tj').val();
    // map["zts"] = zts;

    // 接收日期
    var jssjstartsy = jQuery('#detectPJLab_formSearch #jssjstartsy').val();
    map["jssjstartsy"] = jssjstartsy;
    var jssjend = jQuery('#detectPJLab_formSearch #jssjend').val();
    map["jssjend"] = jssjend;
    // 检测日期
    var sysjstart = jQuery('#detectPJLab_formSearch #sysjstart').val();
    map["sysjstart"] = sysjstart;
    var sysjend = jQuery('#detectPJLab_formSearch #sysjend').val();
    map["sysjend"] = sysjend;
    // 报告日期
    var bgrqstart = jQuery('#detectPJLab_formSearch #bgrqstart').val();
    map["bgrqstart"] = bgrqstart;
    var bgrqend = jQuery('#detectPJLab_formSearch #bgrqend').val();
    map["bgrqend"] = bgrqend;
    var yblxs = jQuery('#detectPJLab_formSearch #yblx_id_tj').val();
    map["yblxs"] = yblxs;
    var zts = jQuery('#detectPJLab_formSearch #zt_id_tj').val();
    map["zts"] = zts;
    var sfsy = jQuery('#detectPJLab_formSearch #sfsy_id_tj').val();
    map["sfsy"] = sfsy;

    var sfjs = jQuery('#detectPJLab_formSearch #sfjs_id_tj').val();
    map["sfjs"] = sfjs;
    map["flag"] = flag;
    btnColorChange(flag);//增加按钮选中状态
    return map;
}


//性别格式化
function xbformat(value,row,index){
    if(row.xb=='1'){
        return '男';

    }else  if(row.xb=='2'){
        return '女';
    }else{
        return '未知';
    }
}

//未接收，已接收未实验，已实验未发送报告 按钮区分选中和未选中状态
function btnColorChange(value){
    $("#detectPJLab_one").removeAttr("style");
    $("#detectPJLab_two").removeAttr("style");
    $("#detectPJLab_three").removeAttr("style");

    if (value == '0'){
        $("#detectPJLab_one").attr("style","background-color:orange;");
    }else if (value == '1'){
        $("#detectPJLab_two").attr("style","background-color:orange;");
    }else if (value == '2'){
        $("#detectPJLab_three").attr("style","background-color:orange;");
    }
}

function searchdetectPJLabResult(isTurnBack){
    //关闭高级搜索条件
    $("#detectPJLab_formSearch #searchMore").slideUp("low");
    detectPJLab_turnOff=true;
    $("#detectPJLab_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#detectPJLab_formSearch #detectPJLab_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#detectPJLab_formSearch #detectPJLab_list').bootstrapTable('refresh');
    }
}
function detectPJLab_DealById(id,action,tourl){
    if(!tourl){
        return;
    }
    if(action=='detection'){
        var url=tourl + "?ids=" +id+"&jclx="+$("#detectPJLab_formSearch #jclx").val();
        $.showDialog(url,'实验',modPJLabDetectionConfig);
    }else if(action=='resultmod'){
        var url=tourl + "?ids=" +id+"&jclx="+$("#detectPJLab_formSearch #jclx").val();;
        $.showDialog(url,'结果修改',resultModDetectionPJLabConfig);
    }else  if(action =='view'){
        var url= tourl + "?fzjcid="+id;
        $.showDialog(url,'详情',viewPJDetectionConfig);}
    else if(action =='mod'){
        var url= tourl + "?fzjcid="+id+"&jclx="+$("#detectPJLab_formSearch #jclx").val();
        $.showDialog(url,'修改',modPJLabDetection);
    }
    else if(action =='accept'){

        if(id!=null){
            var url= tourl + "?ybbh="+id+"&jclx="+$("#detectPJLab_formSearch #jclx").val();
        }else{
            var url= tourl+"?jclx="+$("#detectPJLab_formSearch #jclx").val();
        }
        $.showDialog(url,'标本接收',confirmPjLabConfig);
        $("#samplePjAcceptForm #ybbh").focus();
    }

}
var confirmPjLabConfig = {
    width: "800px",
    modalName: "confirmPjModal",
    offAtOnce: true,  //当数据提交成功，立刻关闭窗口
    onClose: function () {
        searchdetectPJLabResult();
    },
    buttons: {
        success : {
            label : "保存",
            className : "btn-primary",
            callback : function() {
                if (!$("#samplePjAcceptForm").valid()) {
                    return false;
                }
                var nbbh = $("#samplePjAcceptForm #nbbh").val();
                // $("#samplePjAcceptForm #px").val(nbbh.substring(1, nbbh.length - 1));
                var $this = this;
                var opts = $this["options"] || {};
                $("#samplePjAcceptForm input[name='access_token']").val($("#ac_tk").val());
                var szz = $('#samplePjAcceptForm input:radio[name="szz"]:checked').val();
                if (szz == $("#samplePjAcceptForm #ysszz").val()) {
                    $("#samplePjAcceptForm #grsz_flg").val(0);
                } else {
                    $("#samplePjAcceptForm #grsz_flg").val(1);
                }

                submitForm(opts["formName"] || "samplePjAcceptForm", function (responseText, statusText) {
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

                        $.success(responseText["message"], function () {
                            // Toast(responseText["message"],"#samplePjAcceptForm #toast");
                            $("#samplePjAcceptForm #ybbh").val("");
                            $("#samplePjAcceptForm #nbbh").val("");
                            $("#samplePjAcceptForm #syh").val("");
                            searchdetectPJLabResult();
                        });
                    } else if (responseText["status"] == "fail") {
                        preventResubmitForm(".modal-footer > button", false);
                        // Toast(responseText["message"],"#samplePjAcceptForm #toast");
                        $.error(responseText["message"], function () {
                        });
                    } else {
                        preventResubmitForm(".modal-footer > button", false);
                        $.alert("不好意思！出错了！");
                        //修改弹窗为显示几秒消失
                        // Toast("不好意思！出错了！","#samplePjAcceptForm #toast");
                    }
                }, ".modal-footer > button");
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default",
            callback: function () {
                searchdetectPJLabResult();
            }
        }

    }
}

function print_nbbh(sz_flg,ResponseSign,type,RequestLocalCode,nbbh,ybbh,syh){
    var print_url=null
    if(sz_flg=="0"){
        print_url="http://localhost:8081/XGQYPrint";
    }else if(sz_flg=="1"){
        var glxx=$("#samplePjAcceptForm #glxx").val();
        print_url="http://"+glxx+":8082/XGQYPrint";
    }
    openWindow = window.open(print_url+"?nbbh="+nbbh+"&ybbh="+ybbh+"&syh="+syh+"&ResponseNum=3&RequestLocalCode="+RequestLocalCode+"&ResponseSign="+ResponseSign+"&type="+type+ "&access_token="+ $("#ac_tk").val());
    setTimeout(function(){
        openWindow.close();
    }, 300);
}

/*查看详情信息模态框*/
var viewPJDetectionConfig = {
    width		: "900px",
    height		: "1500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var addPJDetection = {
    width		: "1000px",
    height      : "1000px",
    modalName	: "addPJModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success7 : {
            label : "保存",
            className : "btn-primary",
            callback : function() {
                if(!$("#editHPVForm").valid()){
                    return false;
                }
                if($("#editHPVForm #yblx option:selected").text()=="--请选择--"){
                    $.alert("请选择样本类型！");
                    return false;
                }
                if($("#editHPVForm #jcdw option:selected").text()=="--请选择--"){
                    $.alert("请选择检测单位！");
                    return false;
                }
                if($("#editHPVForm #ks option:selected").text()=="--请选择--"){
                    $.alert("请选择科室！");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#editHPVForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"editHPVForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchdetectPJLabResult();
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

var modPJLabDetection = {
    width		: "1000px",
    height      : "1000px",
    modalName	: "modPJModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success8 : {
            label : "保存",
            className : "btn-primary",
            callback : function() {
                if(!$("#editHPVForm").valid()){
                    return false;
                }
                if($("#editHPVForm #yblx option:selected").text()=="--请选择--"){
                    $.alert("请选择样本类型！");
                    return false;
                }
                if($("#editHPVForm #jcdw option:selected").text()=="--请选择--"){
                    $.alert("请选择检测单位！");
                    return false;
                }
                if($("#editHPVForm #ks option:selected").text()=="--请选择--"){
                    $.alert("请选择科室！");
                    return false;
                }
                if($("#editHPVForm #sfjs").val()=="1"&&($("#editHPVForm #syh").val()==null||$("#editHPVForm #syh").val()==undefined||$("#editHPVForm #syh").val()=="")){
                    $.alert("是否接收为是，请选择实验号");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#editHPVForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"editHPVForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchdetectPJLabResult();
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
var modPJLabDetectionConfig = {
    width		: "500px",
    modalName	: "modPJDetectionModal",
    formName	: "detectionPJ_Form",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success7 : {
            label : "保存",
            className : "btn-primary",
            callback : function() {
                if(!$("#detectionPJ_Form").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#detectionPJ_Form input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"detectPJLab_formSearch",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchdetectPJLabResult();
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
var DetectPJreportDownloadConfig={
    width		: "700px",
    modalName	: "reportDownloadConfig",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		:	{
        success 	: 	{
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#DownloadForm").valid()){
                    $.alert("请填写完整信息！");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var bglxbj = $("#DownloadForm input[type='radio']:checked").val();
                var jclx = $("#DownloadForm #jclx").val();
                var map = {
                    access_token:$("#ac_tk").val(),
                    bglxbj:bglxbj,
                    jclx:jclx,
                };
                //判断有选中的采用选中导出，没有采用选择导出
                var sel_row = $('#detectPJ_formSearch #detectPJ_list').bootstrapTable('getSelections');//获取选择行数据
                var ids="";
                if(sel_row.length>=1){
                    for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                        ids = ids + ","+ sel_row[i].fzjcid;
                    }
                    ids = ids.substr(1);
                    map["ids"] = ids;
                }else{
                    getDetectPJSearchData(map);
                }
                var url= $("#DownloadForm #action").val();
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

var resultModDetectionPJLabConfig= {
    width		: "1000px",
    modalName   :"resultModDetectionPJModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确认",
            className : "btn-primary",
            callback : function() {
                if(!$("#resultModDetectionPJForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};

                var json=[];
                var ids=$("#resultModDetectionPJForm #ids").val();
                var tab=$("#resultModDetectionPJForm .xmlist");
                if(tab.length>0){
                    for(var i=0;i<tab.length;i++){
                        var sz = {"fzxmid":'',"jcjg":'',"fzjcid":'',"jgid":'',"ctz":'',"jcjgmc":''};
                        sz.fzxmid =$("#resultModDetectionPJForm #tr_"+i).attr("fzxmid");
                        sz.jgid = $("#resultModDetectionPJForm #tr_"+i).attr("pjjg");
                        sz.fzjcid = $("#resultModDetectionPJForm #tr_"+i).attr("fzjcid");
                        sz.ctz = $("#resultModDetectionPJForm #ctz_"+i).val();
                        sz.jcjg= $("#resultModDetectionPJForm input[name=jg_"+i+"]:checked").val();
                        sz.jcjgmc= $("#resultModDetectionPJForm input[name=jg_"+i+"]:checked").attr("csmc");
                        json.push(sz);
                    }
                }

                $("#resultModDetectionPJForm #ids").val(ids);
                $("#resultModDetectionPJForm #jcjg_json").val(JSON.stringify(json));
                $("#resultModDetectionPJForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"resultModDetectionPJForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                //提交审核
                                if(responseText["auditType"]!=null){
                                    showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){
                                        searchdetectPJLabResult();
                                    });
                                }else{
                                    searchdetectPJLabResult();
                                }
                                $.closeModal(opts.modalName);
                            }
                        });
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {
                        });
                    } else{
                        preventResubmitForm(".modal-footer > button", false);
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
var detectPJLab_oButton=function(){
    var oButtonInit = new Object();
    var postdata = {};
    oButtonInit.Init=function(){
        var btn_query=$("#detectPJLab_formSearch #btn_query");
        var btn_view = $("#detectPJLab_formSearch #btn_view");//查看
        var btn_detection=$("#detectPJLab_formSearch #btn_detection");//检测
        var btn_accept =$("#detectPJLab_formSearch #btn_accept");//样本接收
        var btn_mod=$("#detectPJLab_formSearch #btn_mod");//修改
        var btn_resultmod=$("#detectPJLab_formSearch #btn_resultmod");//修改检测结果
        //添加日期控件
        laydate.render({
            elem: '#jssjstartsy'
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
        /*--------------------------------修改检测结果---------------------------*/
        btn_resultmod.unbind("click").click(function(){
            var sel_row=$('#detectPJLab_formSearch #detectPJLab_list').bootstrapTable('getSelections');
            if(sel_row.length>=1){
                // var ids="";
                var ids="";
                var str=sel_row[0].jcxmmc;
                var num=str.split(",").length;
                for(var i=0;i<sel_row.length;i++){
                    if(sel_row[i].sysj==null||sel_row[i].sysj==''){
                        $.error("标本编号为"+sel_row[i].ybbh+"的样本未实验！");
                        return;
                    }
                    if(sel_row[i].zt!='00' && sel_row[i].zt!='15') {
                        $.error("标本编号为"+sel_row[i].ybbh+"的数据，该状态不允许提交！");
                        return;
                    }
                    var split = sel_row[i].jcxmmc.split(",");
                    if(split.length==num){
                        var flag=true;
                        for(var j=0;j<split.length;j++){
                            if(str.indexOf(split[j])==-1){
                                flag=false;
                                break;
                            }
                        }
                        if(!flag){
                            $.error("请选择检测项目一致的数据！");
                            return;
                        }
                    }else{
                        $.error("请选择检测项目一致的数据！");
                        return;
                    }
                    ids=ids+","+sel_row[i].fzjcid;
                }
                ids=ids.substring(1);
                detectPJLab_DealById(ids,"resultmod",btn_resultmod.attr("tourl"));

            }else{
                $.error("请至少选中一行");
            }
        });
        /*--------------------------------修改---------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row=$('#detectPJLab_formSearch #detectPJLab_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                detectPJLab_DealById(sel_row[0].fzjcid,"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------样本接收-----------------------------------*/
        btn_accept.unbind("click").click(function(){
            var sel_row = $('#detectPJLab_formSearch #detectPJLab_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                if (sel_row[0].jcdxcsdm == "W"){//物检
                    // if (!sel_row[0].cjsj){
                    //     $.error("该用户还未录入！");
                    // }else if (!sel_row[0].ybbh){
                    //     $.error("标本编号不可为空！");
                    // }else{
                    detectPJLab_DealById(sel_row[0].ybbh,"accept",btn_accept.attr("tourl"));
                    // }
                }else {//人检
                    // if (!sel_row[0].cjsj){
                    //     $.error("该用户还未录入！");
                    // }else if (!sel_row[0].xm || !sel_row[0].xb || !sel_row[0].nl || !sel_row[0].zjh || !sel_row[0].zjlxmc  ){
                    //     $.error("个人信息不充足，是否缺少姓名、性别、年龄、证件号、证件类型等信息！");
                    // }else if (!sel_row[0].ybbh){
                    //     $.error("标本编号不可为空！");
                    // }
                    // else{
                    detectPJLab_DealById(sel_row[0].ybbh,"accept",btn_accept.attr("tourl"));
                    // }
                }
            }else{
                detectPJLab_DealById(null,"accept",btn_accept.attr("tourl"));
            }
        });
        /*-----------------------模糊查询ok------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchdetectPJLabResult(true);
            });
        }
        /*--------------------------------查看------------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#detectPJLab_formSearch #detectPJLab_list').bootstrapTable('getSelections');// 获取选择行数据
            if(sel_row.length==1){
                detectPJLab_DealById(sel_row[0].fzjcid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*--------------------------------修改检测状态-----------------------------*/
        btn_detection.unbind("click").click(function(){
            var sel_row = $('#detectPJLab_formSearch #detectPJLab_list').bootstrapTable('getSelections');//获取选择行数据
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
                    url:"/detectionPJ/detectionPJ/pagedataCheckJcxm",
                    cache: false,
                    data: {"ids":ids,"access_token":$("#ac_tk").val(),"jclx":$("#detectPJLab_formSearch #jclx").val()},
                    dataType:'json',
                    success:function(data){
                        //返回值
                        if(data==false){
                            $.error("检测项目必须相同!");
                        }else{
                            detectPJLab_DealById(ids,"detection",btn_detection.attr("tourl"));
                        }
                    }
                });
            }
        });
        /**显示隐藏**/
        $("#detectPJLab_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(detectPJLab_turnOff){
                $("#detectPJLab_formSearch #searchMore").slideDown("low");
                detectPJLab_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#detectPJLab_formSearch #searchMore").slideUp("low");
                detectPJLab_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });
        /*-------------------------------------------------------------*/
    }
    return oButtonInit;
}

$(function(){
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

    var oInit = new detect_PageInit();
    oInit.Init();
    var oTable = new detectPJLab_TableInit();
    oTable.Init();
    var oButton = new detectPJLab_oButton();
    oButton.Init();
    jQuery('#detectPJLab_formSearch .chosen-select').chosen({width: '100%'});
    $("#detectPJLab_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
})