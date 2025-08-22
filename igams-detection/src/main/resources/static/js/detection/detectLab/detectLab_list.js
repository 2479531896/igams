var url = '/detectlab/detectlab/pageGetListDetectlab';
var Detectlab_turnOff=true;
var Detectlab_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#detectlab_formSearch #detectlab_list").bootstrapTable({
            url: url,         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#detectlab_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "sysj desc,cjsj desc,yyjcrq desc,ybbh desc,bbzbh",				// 排序字段
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
                checkbox: true,
                width: '4%'
            },{
                field: 'fzjcid',
                title: '分子检测ID',
                titleTooltip:'分子检测ID',
                sortable: true,
                width: '10%',
                align: 'left',
                visible:false
            },{
                field: 'hzid',
                title: '患者ID',
                titleTooltip:'患者ID',
                sortable: true,
                width: '10%',
                align: 'left',
                visible:false
            },{
                field: 'syh',
                title: '实验号',
                titleTooltip:'实验号',
                sortable: true,
                width: '8%',
                align: 'left',
                visible:true
            },{
                field: 'ybbh',
                title: '标本编号',
                titleTooltip:'标本编号',
                sortable: true,
                width: '10%',
                align: 'left',
                visible:true
            },{
                field: 'bbzbh',
                title: '标本子编号',
                titleTooltip:'标本子编号',
                sortable: true,
                width: '10%',
                align: 'left',
                visible:true
            },{
                field: 'nbbh',
                title: '内部编码',
                titleTooltip:'内部编码',
                sortable: true,
                width: '13%',
                align: 'left',
                visible:false
            },{
                field: 'xm',
                title: '姓名/样品名',
                titleTooltip:'姓名/样品名',
                width: '8%',
                align: 'left',
                formatter:xmformat,
                visible:true
            },{
                field: 'xb',
                title: '性别',
                titleTooltip:'性别',
                width: '4%',
                align: 'left',
                visible:true
            },{
                field: 'nl',
                title: '年龄',
                titleTooltip:'年龄',
                sortable: true,
                width: '5%',
                align: 'left',
                visible:false
            },{
                field: 'tw',
                title: '体温',
                titleTooltip:'体温',
                sortable: true,
                width: '5%',
                align: 'left',
                visible:false
            }, {
                field: 'zjh',
                title: '证件号',
                titleTooltip:'证件号',
                sortable: true,
                width: '10%',
                align: 'left',
                visible:false
            }, {
                field: 'zjlxmc',
                title: '证件类型',
                titleTooltip:'证件类型',
                sortable: true,
                width: '10%',
                align: 'left',
                visible:false
            },{
                field: 'jcxmmc',
                title: '检测项目',
                titleTooltip:'检测项目',
                width: '15%',
                align: 'left',
                visible:true
            },{
                field: 'jczxmmc',
                title: '单/混检',
                titleTooltip:'单/混检',
                width: '6%',
                align: 'left',
                visible:true
            },{
                field: 'jcdwmc',
                title: '检测单位',
                titleTooltip:'检测单位',
                width: '10%',
                align: 'left',
                visible:false
            },{
                field: 'dwmc',
                title: '医院名称',
                titleTooltip:'医院名称',
                width: '10%',
                align: 'left',
                visible:true
            },{
                field: 'jcjgmc',
                title: '结果',
                titleTooltip:'结果',
                formatter: jcjgmcDetect_format,
                width: '5%',
                align: 'left',
                visible:true
            },{
                field: 'yblx',
                title: '标本类型',
                titleTooltip:'标本类型',
                width: '10%',
                align: 'left',
                visible:true
            },{
                field: 'yyjcrq',
                title: '预约检测日期',
                titleTooltip:'预约检测日期',
                sortable: true,
                width: '13%',
                align: 'left',
                visible:true
            },{
                field: 'zffs',
                title: '支付方式',
                titleTooltip:'支付方式',
                sortable: true,
                width: '11%',
                align: 'left',
                visible: false
            },{
                field: 'cydmc',
                title: '采样点',
                titleTooltip:'采样点',
                sortable: true,
                width: '12%',
                align: 'left',
                visible:false
            },{
                field: 'cjrymc',
                title: '采集人员',
                titleTooltip:'采集人员',
                sortable: true,
                width: '8%',
                align: 'left',
                visible:false
            },{
                field: 'cjsj',
                title: '采集时间',
                titleTooltip:'采集时间',
                sortable: true,
                width: '12%',
                align: 'left',
                visible:true
            },{
                field: 'qrsj',
                title: '确认时间',
                titleTooltip:'确认时间',
                sortable: true,
                width: '10%',
                align: 'left',
                visible:false
            },{
                field: 'jssj',
                title: '接收时间',
                titleTooltip:'接收时间',
                sortable: true,
                width: '15%',
                align: 'left',
                visible:false
            },{
                field: 'sfsy',
                title: '实验标记',
                titleTooltip:'是否实验',
                width: '8%',
                align: 'left',
                visible:false
            },{
                field: 'sfsymc',
                title: '是否实验',
                titleTooltip:'是否实验',
                width: '8%',
                align: 'left',
                visible:false
            },{
                field: 'sysj',
                title: '实验时间',
                titleTooltip:'实验时间',
                sortable: true,
                width: '13%',
                align: 'left',
                visible:true
            },{
                field: 'bgrq',
                title: '报告日期',
                titleTooltip:'报告日期',
                sortable: true,
                width: '10%',
                align: 'left',
                visible:false
            },{
                field: 'sfjs',
                title: '接收标记',
                titleTooltip:'是否接收',
                width: '8%',
                align: 'left',
                visible:false
            },{
                field: 'sfjsmc',
                title: '是否接收',
                titleTooltip:'是否接收',
                width: '8%',
                align: 'left',
                visible:false
            },{
                field: 'fssj',
                title: '上传时间',
                titleTooltip:'上传时间',
                width: '10%',
                align: 'left',
                visible:false
            },{
                field: 'fsbj',
                title: '上传标记',
                titleTooltip:'上传标记',
                width: '5%',
                align: 'left',
                visible:false
            },{
                field: 'scbj',
                title: '废弃标记',
                titleTooltip:'废弃标记',
                width: '7%',
                align: 'left',
                formatter:scbjlabformat,
                visible: true
            },{
                field: 'zt',
                title: '状态',
                titleTooltip:'状态',
                width: '10%',
                align: 'left',
                // formatter:ztFormat,
                visible:false
            },{
                field: 'cz',
                title: '操作',
                titleTooltip:'操作',
                width: '7%',
                align: 'left',
                // formatter:czFormat,
                visible: false
            },{
                field: 'ypmc',
                title: '样品名称',
                titleTooltip:'样品名称',
                width: '7%',
                align: 'left',
                visible: false
            },{
                field: 'sccj',
                title: '生产厂家',
                titleTooltip:'生产厂家',
                width: '7%',
                align: 'left',
                visible: false
            },{
                field: 'scpc',
                title: '生产批次',
                titleTooltip:'生产批次',
                width: '7%',
                align: 'left',
                visible: false
            },{
                field: 'scd',
                title: '生产地',
                titleTooltip:'生产地',
                width: '7%',
                align: 'left',
                visible: false
            },{
                field: 'jcdxcsdm',
                title: '检测对象',
                titleTooltip:'检测对象',
                width: '6%',
                align: 'left',
                formatter:jcdxformat,
                visible: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                Detectlab_DealById(row.fzjcid,'view',$("#detectlab_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#detectlab_formSearch #detectlab_list").colResizable({
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
            sortLastName: "jcxx.fzjcid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getDetectlabSearchData(map);
    };
    return oTableInit;
}

//结果格式化
function jcjgmcDetect_format(value,row,index){
    if(row.jcjgmc=="阳性"){
        var jcjgmc="<span style='color:red;font-weight:bold;'>阳性</span>";
        return jcjgmc;
    }else if(row.jcjgmc=="可疑，需复查"){
        var jcjgmc="<span style='color:#0000cc;font-weight:bold;'>可疑，需复查</span>";
        return jcjgmc;
    }else if(row.jcjgmc=="阴性"){
        var jcjgmc="<span style='font-weight:bold;'>阴性</span>";
        return jcjgmc;
    }
}

var detect_PageInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        var jcdxlx=$("#detectlab_formSearch a[id^='jcdxlx_id_']")

        $.each(jcdxlx, function(i, n){
            var id = $(this).attr("id");
            var code = id.substring(id.lastIndexOf('_')+1,id.length);
            var csdm=$("#detectlab_formSearch #"+id).attr("csdm");
            if(csdm == 'R'){
                addTj('jcdxlx',code,'detectlab_formSearch');
            }
        });
    }
    return oInit;
}

var flag='2';
function clickFlag(flagValue){
    flag = flagValue;
    searchDetectlabResult(true);
}
function getDetectlabSearchData(map){
    var detectlab_select=$("#detectlab_formSearch #detectlab_select").val();
    var detectlab_input=$.trim(jQuery('#detectlab_formSearch #detectlab_input').val());
    if(detectlab_select=="0"){
        map["ybbh"]=detectlab_input
    }else if(detectlab_select=="1"){
        map["bbzbh"]=detectlab_input
    }else if(detectlab_select=="2"){
        map["xm"]=detectlab_input
    }else if(detectlab_select=="3"){
        map["zjh"]=detectlab_input
    }else if(detectlab_select=="4"){
        map["cjrymc"]=detectlab_input
    }
    // 通过开始日期
    var yyjcrqstart = jQuery('#detectlab_formSearch #yyjcrqstart').val();
    map["yyjcrqstart"] = yyjcrqstart;
    // 通过结束日期
    var yyjcrqend = jQuery('#detectlab_formSearch #yyjcrqend').val();
    map["yyjcrqend"] = yyjcrqend;
    var jcdxlx = jQuery('#detectlab_formSearch #jcdxlx_id_tj').val();
    map["jcdxlxs"] = jcdxlx;
    var jczxms = jQuery('#detectlab_formSearch #jczxm_id_tj').val();
    map["jczxms"] = jczxms;

    var syhstart = jQuery('#detectlab_formSearch #syhstart').val();
    map["syhstart"] = syhstart;
    var syhend = jQuery('#detectlab_formSearch #syhend').val();
    map["syhend"] = syhend;

    map["flag"] = flag;
    btnColorChange(flag);//增加按钮选中状态
    return map;
}

//标记格式化
function scbjlabformat(value,row,index){
    if(row.scbj==0){
        var html="<span style='color:green;'>"+"使用中"+"</span>";
    }else if (row.scbj==1){
        var html="<span style='color:red;'>"+"删除"+"</span>";
    }else if(row.scbj==2){
        var html="<span style='color:red;'>"+"废弃"+"</span>";
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

//未接收，已接收未实验，已实验未发送报告 按钮区分选中和未选中状态
function btnColorChange(value){
    $("#detectlab_one").removeAttr("style");
    $("#detectlab_two").removeAttr("style");
    $("#detectlab_three").removeAttr("style");
    $("#detectlab_four").removeAttr("style");
    $("#detectlab_five").removeAttr("style");
    $("#detectlab_six").removeAttr("style");

    if (value == '1'){
        $("#detectlab_one").attr("style","background-color:orange;");
    }else if (value == '2'){
        $("#detectlab_two").attr("style","background-color:orange;");
    }else if (value == '3'){
        $("#detectlab_three").attr("style","background-color:orange;");
    }else if (value == '4'){
        $("#detectlab_four").attr("style","background-color:orange;");
    }else if (value == '5'){
        $("#detectlab_five").attr("style","background-color:orange;");
    }else if (value == '6'){
        $("#detectlab_six").attr("style","background-color:orange;");
    }
}

function searchDetectlabResult(isTurnBack){
    //关闭高级搜索条件
    $("#detectlab_formSearch #searchMore").slideUp("low");
    Detectlab_turnOff=true;
    $("#detectlab_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#detectlab_formSearch #detectlab_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#detectlab_formSearch #detectlab_list').bootstrapTable('refresh');
    }
}
function Detectlab_DealById(id,action,tourl){
    if(!tourl){
        return;
    }
    if(action=="view"){
        var url=tourl+"?fzjcid="+id+"&jclx="+$("#detectlab_formSearch #jclx").val();
        $.showDialog(url,'查看消息详细信息',viewDetectlabConfig);
    }else if(action=='detection'){
        var url=tourl + "?ids=" +id+"&jclx="+$("#detectlab_formSearch #jclx").val();
        $.showDialog(url,'实验',modDetectlabConfig);
    }else if(action=='msgconfirm'){
        var url=tourl + "?fzjcid=" +id+"&jclx="+$("#detectlab_formSearch #jclx").val();
        $.showDialog(url,'信息确认',msgconfirmDetectlabConfig);
    }else if(action =='accept'){
        if(id!=null){
            var url= tourl + "?ybbh="+id+"&jclx="+$("#detectlab_formSearch #jclx").val();
        }else{
            var url= tour+"?jclx="+$("#detectlab_formSearch #jclx").val();
        }
        $.showDialog(url,'标本接收',confirmDetectlabConfig);
        $("#sampleAcceptForm #ybbh").focus();
    }else if(action=='mod'){
        var url=tourl + "?fzjcid=" +id+"&jclx="+$("#detectlab_formSearch #jclx").val();
        $.showDialog(url,'修改检测信息',editDetectlabConfig);
    }else if(action=='resultMod'){
        var url=tourl + "?ybbhs=" +id+"&jclx="+$("#detectlab_formSearch #jclx").val();
        $.showDialog(url,'修改结果',modDetectlabResultConfig);
    }else if(action=="delivery"){
        var url=tourl + "?ids=" +id+"&jclx="+$("#detectlab_formSearch #jclx").val();
        $.showDialog(url,'修改交付状态', sfjfDetectionlabConfig);
    }
}

var sfjfDetectionlabConfig= {
    width		: "700px",
    height		: "100px",
    modalName   : "sfjfDetectionlabModal",
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
                            searchDetectlabResult();
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

var modDetectlabResultConfig= {
    width		: "500px",
    height		: "100px",
    modalName   :"modDetectionlabResultModal",
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
                                    searchDetectlabResult();
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
var editDetectlabConfig = {
    width		: "900px",
    height		: "600px",
    modalName   :"editDetectlabConfig",
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
                var otherHospital= $("#editCovidForm #otherHospital").val();
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
                                searchDetectlabResult();
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

/* 样本接收模态框 */
var confirmDetectlabConfig = {
    width: "800px",
    modalName: "confirmDetectlabModal",
    offAtOnce: true,  //当数据提交成功，立刻关闭窗口
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
                                        searchDetectlabResult();
                                    // });
                                } else if (responseText["status"] == "fail") {
                                    preventResubmitForm(".modal-footer > button", false);
                                    Toast(responseText["message"],"#sampleAcceptForm #toast");
                                    // $.error(responseText["message"], function () {
                                    // });
                                } else {
                                    preventResubmitForm(".modal-footer > button", false);
                                    //$.alert("不好意思！出错了！");
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
                                searchDetectlabResult();
                            // });
                        } else if (responseText["status"] == "fail") {
                            preventResubmitForm(".modal-footer > button", false);
                            Toast(responseText["message"],"#sampleAcceptForm #toast");
                            // $.error(responseText["message"], function () {
                            // });
                        } else {
                            preventResubmitForm(".modal-footer > button", false);
                            //$.alert("不好意思！出错了！");
                            //修改弹窗为显示几秒消失
                            Toast("不好意思！出错了！","#sampleAcceptForm #toast");
                        }
                    }, ".modal-footer > button");
                }
                return false;
            }
        },
        cancel: {
            label: "关 闭",
            className: "btn-default"
        }
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
    }, 600);
    // let html ="<iframe id='test' src='"+print_url+"?nbbh="+nbbh+"&ybbh="+ybbh+"&syh="+syh+"&ResponseNum=3&RequestLocalCode="+RequestLocalCode+"&ResponseSign="+ResponseSign+"&type="+type+ "&access_token="+ $("#ac_tk").val()+"' width='0px' height='0px'></iframe>"
    // let html ="<img id='test' crossorigin='anonymous' src='"+print_url+"?nbbh="+nbbh+"&ybbh="+ybbh+"&syh="+syh+"&ResponseNum=3&RequestLocalCode="+RequestLocalCode+"&ResponseSign="+ResponseSign+"&type="+type+ "&access_token="+ $("#ac_tk").val()+"' width='0px' height='0px'></img>"
    // $("#sampleAcceptForm #iframePrint").empty();
    // $("#sampleAcceptForm #iframePrint").append(html);
}

/*查看详情信息模态框*/
var msgconfirmDetectlabConfig = {
    width		: "800px",
    height		: "500px",
    modalName   :"msgConfirmDetectlabModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确认",
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
                                searchDetectlabResult();
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
var modDetectlabConfig = {
    width		: "500px",
    modalName	: "modDetectlabModal",
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
                                searchDetectlabResult();
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

var viewDetectlabConfig = {
    width		: "800px",
    height      : "500PX",
    modalName	:"viewDetectlabModal",
    offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};


var Detectlab_oButton=function(){
    var oButtonInit = new Object();
    var postdata = {};
    oButtonInit.Init=function(){
        var btn_query=$("#detectlab_formSearch #btn_query");
        var btn_view = $("#detectlab_formSearch #btn_view");//查看
        var btn_msgconfirm=$("#detectlab_formSearch #btn_msgconfirm");//信息确认按钮
        var btn_detection=$("#detectlab_formSearch #btn_detection");//检测
        var btn_accept =$("#detectlab_formSearch #btn_accept");//样本接收
        var btn_mod=$("#detectlab_formSearch #btn_mod");//修改
        var btn_resultMod=$("#detectlab_formSearch #btn_resultmod");//修改检测结果
        var btn_del=$("#detectlab_formSearch #btn_del");//删除
        var btn_discard = $("#detectlab_formSearch #btn_discard");//废弃
        var btn_lock= $("#detectlab_formSearch #btn_lock");//废弃
        var btn_deliverability=$("#detectlab_formSearch #btn_deliverability");//修改交付状态
        var btn_cjsjreport=$("#detectlab_formSearch #btn_cjsjreport");//采集上传
        //添加日期控件
        laydate.render({
            elem: '#yyjcrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#yyjcrqend'
            ,theme: '#2381E9'
        });
        /*---------------------------采集时间上报至卫健-----------------------------------*/
        btn_cjsjreport.unbind("click").click(function(){
            var sel_row=$('#detectlab_formSearch #detectlab_list').bootstrapTable('getSelections');
            if (sel_row.length==0){
                $.error("请至少选中一行")
            }else if (sel_row.length>0 && sel_row.length<=100){
                var ids = "";
                for (var i = 0; i < sel_row.length; i++) {
                    if (!sel_row[i].xm || !sel_row[i].zjh || !sel_row[i].zjlxmc || !sel_row[i].hzid) {
                        $.error("标本子编号为" + sel_row[i].bbzbh + "的个人信息不充足，是否缺少姓名、证件号、证件类型等信息！");
                        return
                    } else if (!sel_row[i].bbzbh || !sel_row[i].cjsj) {
                        $.error("标本子编号为" + sel_row[i].bbzbh + "的实验信息不充足，是否缺少标本子编号、采集时间等信息！");
                        return
                    }
                    ids = ids + "," + sel_row[i].fzjcid;
                }
                ids = ids.substring(1);
                $.confirm('您确定要上传选中数据的采集信息到卫健吗？', function (result) {
                    if (result) {
                        jQuery.ajaxSetup({async: false});
                        var url = btn_cjsjreport.attr("tourl");
                        jQuery.post(url, {ids: ids, "access_token": $("#ac_tk").val()}, function (responseText) {
                            setTimeout(function () {
                                if (responseText["status"] == 'success') {
                                    $.success(responseText["message"], function () {
                                        searchDetectlabResult();
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
        /*--------------------------------修改交付---------------------------*/
        btn_deliverability.unbind("click").click(function (){
            var sel_row=$('#detectlab_formSearch #detectlab_list').bootstrapTable('getSelections');
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
                Detectlab_DealById(ids,"delivery",btn_deliverability.attr("tourl"));
            }else{
                $.error("请至少选中一行");
            }
        });
        /* ------------------------------废弃-----------------------------*/
        btn_discard.unbind("click").click(function(){
            var sel_row = $('#detectlab_formSearch #detectlab_list').bootstrapTable('getSelections');//获取选择行数据
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
                        var url= btn_discard.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchDetectlabResult();
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
        /* ------------------------------删除-----------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#detectlab_formSearch #detectlab_list').bootstrapTable('getSelections');//获取选择行数据
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
                                        searchDetectlabResult();
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
        /*--------------------------------修改检测结果---------------------------*/
        btn_resultMod.unbind("click").click(function(){
            var sel_row=$('#detectlab_formSearch #detectlab_list').bootstrapTable('getSelections');
            if(sel_row.length>=1){
                var ids="";
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
                    ids=ids+","+sel_row[i].ybbh;
                }
                ids=ids.substring(1);
                Detectlab_DealById(ids,"resultMod",btn_resultMod.attr("tourl"));

            }else{
                $.error("请至少选中一行");
            }
        });
        /* ------------------------------锁定-----------------------------*/
        btn_lock.unbind("click").click(function(){
            var sel_row = $('#detectlab_formSearch #detectlab_list').bootstrapTable('getSelections');//获取选择行数据
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
                        var url= btn_lock.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchDetectlabResult(true);
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
        /*--------------------------------修改---------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row=$('#detectlab_formSearch #detectlab_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                Detectlab_DealById(sel_row[0].fzjcid,"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------样本接收-----------------------------------*/
        btn_accept.unbind("click").click(function(){
            var sel_row = $('#detectlab_formSearch #detectlab_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                if (sel_row[0].jcdxcsdm == "W") {//物检
                    if (!sel_row[0].cjsj){
                        $.error("该数据还未采集！");
                    }else if (!sel_row[0].ybbh){
                        $.error("标本编号不可为空！");
                    }else{
                        Detectlab_DealById(sel_row[0].ybbh,"accept",btn_accept.attr("tourl"));
                    }
                }else {//人检
                    if (!sel_row[0].cjsj){
                        $.error("该用户还未采集！");
                    }else if (!sel_row[0].xm || !sel_row[0].xb || !sel_row[0].nl || !sel_row[0].zjh || !sel_row[0].zjlxmc  ){
                        $.error("个人信息不充足，是否缺少姓名、性别、年龄、证件号、证件类型等信息！");
                    }else if (!sel_row[0].ybbh){
                        $.error("标本编号不可为空！");
                    }else{
                        Detectlab_DealById(sel_row[0].ybbh,"accept",btn_accept.attr("tourl"));
                    }
                }
            }else{
                Detectlab_DealById(null,"accept",btn_accept.attr("tourl"));
            }
        });
        /*-----------------------模糊查询ok------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchDetectlabResult(true);
            });
        }
        /*------------------------确认按钮点击先调用查看页面后在确认----------------*/
        btn_msgconfirm.unbind("click").click(function(){
            var sel_row = $('#detectlab_formSearch #detectlab_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                Detectlab_DealById(sel_row[0].fzjcid,"msgconfirm",btn_msgconfirm.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*--------------------------------查看------------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#detectlab_formSearch #detectlab_list').bootstrapTable('getSelections');// 获取选择行数据
            if(sel_row.length==1){
                Detectlab_DealById(sel_row[0].fzjcid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*--------------------------------修改检测状态-----------------------------*/
        btn_detection.unbind("click").click(function(){
            var sel_row = $('#detectlab_formSearch #detectlab_list').bootstrapTable('getSelections');//获取选择行数据
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
                    url:"/detectlab/detectlab/pagedataCheckJcxm",
                    cache: false,
                    data: {"ids":ids,"access_token":$("#ac_tk").val()},
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
        /**显示隐藏**/
        $("#detectlab_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(Detectlab_turnOff){
                $("#detectlab_formSearch #searchMore").slideDown("low");
                Detectlab_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#detectlab_formSearch #searchMore").slideUp("low");
                Detectlab_turnOff=true;
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
    var oTable = new Detectlab_TableInit();
    oTable.Init();
    var oButton = new Detectlab_oButton();
    oButton.Init();
    jQuery('#detectlab_formSearch .chosen-select').chosen({width: '100%'});
    $("#detectlab_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
})