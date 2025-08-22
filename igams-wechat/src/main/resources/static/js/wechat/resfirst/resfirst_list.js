var ResFirst_turnOff=true;
var ResFirst_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#resfirst_formSearch #resfirst_list").bootstrapTable({
            url: '/experimentS/experiments/pageGetListResFirst',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#resfirst_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "sjxx.jsrq",				// 排序字段
            sortOrder: "desc",                   // 排序方式
            queryParams: oTableInit.queryParams,// 传递参数（*）
            sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       // 初始化加载第一页，默认第一页
            pageSize: 15,                       // 每页的记录行数（*）
            pageList: [15, 30, 50, 100],        // 可供选择的每页的行数（*）
            paginationPreText: '‹',				// 指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			// 指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  // 是否显示所有的列
            showRefresh: true,                  // 是否显示刷新按钮
            minimumCountColumns: 2,             // 最少允许的列数
            clickToSelect: true,                // 是否启用点击选中行
            // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "sjid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%',
            },{
                title: '序号',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '3%',
                align: 'left',
                visible:true
            },{
                field: 'sjid',
                title: '送检ID',
                titleTooltip:'送检ID',
                width: '7%',
                align: 'left',
                visible:false
            },{
                field: 'ybbh',
                title: '标本编号',
                titleTooltip:'标本编号',
                width: '11%',
                align: 'left',
                visible: true
            },{
                field: 'nbbm',
                title: '内部编码',
                titleTooltip:'内部编码',
                width: '9%',
                align: 'left',
                visible:true,
            },{
                field: 'hzxm',
                title: '患者姓名',
                titleTooltip:'患者姓名',
                width: '7%',
                align: 'left',
                visible: true
            },{
                field: 'xbmc',
                title: '性别',
                titleTooltip:'性别',
                width: '7%',
                align: 'left',
                visible:false,

            },{
                field: 'nl',
                title: '年龄',
                titleTooltip:'年龄',
                width: '7%',
                align: 'left',
                visible:false,
            },{
                field: 'dh',
                title: '电话',
                titleTooltip:'电话',
                width: '7%',
                align: 'left',
                visible:false
            },{
                field: 'jcxmmc',
                title: '检测项目',
                width: '6%',
                align: 'left',
                titleTooltip:'检测项目',
                visible:true
            },{
                field: 'yblxmc',
                title: '样本类型',
                titleTooltip:'样本类型',
                width: '7%',
                align: 'left',
                visible:true,
            },{
                field: 'hospitalname',
                title: '医院名称',
                titleTooltip:'医院名称',
                width: '7%',
                align: 'left',
                visible: false,
            },{
                field: 'jsrq',
                title: '接收日期',
                titleTooltip:'接收日期',
                width: '12%',
                align: 'left',
                visible:true,
            },{
                field: 'qtsyrq',
                title: '实验日期',
                width: '12%',
                align: 'left',
                titleTooltip:'其他实验日期',
                visible:true
            },{
                field: 'bgrq',
                title: '报告日期',
                width: '12%',
                align: 'left',
                titleTooltip:'报告日期',
                visible:true
            },{
                field: 'ksmc',
                title: '科室',
                titleTooltip:'科室',
                width: '7%',
                align: 'left',
                visible:false,
            },{
                field: 'qtks',
                title: '其他科室',
                titleTooltip:'其他科室',
                width: '7%',
                align: 'left',
                visible:false,
            },{
                field: 'sjys',
                title: '主治医师',
                titleTooltip:'主治医师',
                width: '7%',
                align: 'left',
                visible:false,
            },{
                field: 'yymc',
                title: '医院名称',
                titleTooltip:'医院名称',
                width: '7%',
                align: 'left',
                visible:false,
            },{
                field: 'jcdwmc',
                title: '检测单位',
                titleTooltip:'检测单位',
                width: '7%',
                align: 'left',
                visible:false,
            },{
                field: 'zt',
                title: '状态',
                titleTooltip:'状态',
                width: '6%',
                align: 'left',
                formatter:ztFormat,
                visible:true,
            },{
                field: 'bz',
                title: '备注',
                titleTooltip:'备注',
                width: '10%',
                align: 'left',
                visible:true,
            },{
                field: 'ysdh',
                title: '医师电话',
                titleTooltip:'医师电话',
                width: '7%',
                align: 'left',
                visible:false
            },{
                field: 'db',
                title: '合作伙伴',
                titleTooltip:'合作伙伴',
                width: '7%',
                align: 'left',
                visible: false
            },{
                field: 'nldw',
                title: '年龄单位',
                titleTooltip:'年龄单位',
                width: '7%',
                align: 'left',
                visible:false,
            },{
                field: 'ybtj',
                title: '样本体积',
                titleTooltip:'样本体积',
                width: '7%',
                align: 'left',
                visible:false
            },{
                field: 'cyrq',
                title: '采样日期',
                titleTooltip:'采样日期',
                width: '7%',
                align: 'left',
                visible:false,
            },{
                field: 'bgrq',
                title: '报告日期',
                titleTooltip:'报告日期',
                width: '7%',
                align: 'left',
                visible:false,
            },{
                field: 'bz',
                title: '备注',
                titleTooltip:'备注',
                width: '7%',
                align: 'left',
                visible:false,
            },{
                field: 'sfjsmc',
                title: '是否接收',
                titleTooltip:'是否接收',
                width: '7%',
                align: 'left',
                visible:false,
            },{
                field: 'sftjmc',
                title: '是否统计',
                titleTooltip:'是否统计',
                width: '7%',
                align: 'left',
                visible:false,
            },{
                field: 'sfsfmc',
                title: '是否收费',
                titleTooltip:'是否收费',
                width: '7%',
                align: 'left',
                visible:false,
            },{
                field: 'kdlxmc',
                title: '快递类型',
                titleTooltip:'快递类型',
                width: '7%',
                align: 'left',
                visible:false,
            },{
                field: 'kdh',
                title: '快递号',
                titleTooltip:'快递号',
                width: '7%',
                align: 'left',
                visible:false,
            },{
                field: 'jcxmkzcs',
                title: '项目分类标识',
                titleTooltip:'项目分类标识',
                width: '2%',
                align: 'left',
                visible:false,
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                ResFirst_DealById(row.sjid,'view',$("#resfirst_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#resfirst_formSearch #resfirst_list").colResizable({
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
            sortLastName: "sjxx.sjid", // 防止同名排位用
            sortLastOrder: "asc", // 防止同名排位用
            jcxmdm: $("#resfirst_formSearch #jcxmdm").val()
            // 搜索框使用
            // search:params.search
        };
        return getResFirstSearchData(map);
    };
    return oTableInit;
}

/**
 * 状态格式化
 * @returns
 */
function ztFormat(value,row,index) {
    if (row.zt == '00' || row.zt == '  ' || !row.zt) {
        return '未提交';
    }else if (row.zt == '80') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.sjid + "\",event,\"\")' >审核通过</a>";
    }else if (row.zt == '15') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.sjid + "\",event,\"\")' >审核未通过</a>";
    }else{
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.sjid + "\",event,\"\")' >" + row.shxx_dqgwmc + "审核中</a>";
    }
}
/*
 * 查看审核信息
 */
function showAuditInfo(ywid,event,shlb,params){
    if(shlb.split(",").length>1){
        $.showDialog(
            (params?(params.prefix?params.prefix:""):"") + auditViewUrl,
            '审核信息',
            $.extend({},viewConfig,{"width":"960px",data:{'ywid':ywid,'shlbs':shlb}})
        );
    }else{
        $.showDialog(
            (params?(params.prefix?params.prefix:""):"") + auditViewUrl,
            '审核信息',
            $.extend({},viewConfig,{"width":"960px",data:{'ywid':ywid,'shlb':shlb}})
        );
    }
    event.stopPropagation();
}

function getResFirstSearchData(map){
    var resfirst_select=$("#resfirst_formSearch #resfirst_select").val();
    var resfirst_input=$.trim(jQuery('#resfirst_formSearch #resfirst_input').val());
    if(resfirst_select=="0"){
        map["hzxm"]=resfirst_input
    }else if(resfirst_select=="1"){
        map["db"]=resfirst_input
    }else if(resfirst_select=="2"){
        map["hospitalname"]=resfirst_input
    }else if(resfirst_select=="5"){
        map["ybbh"]=resfirst_input
    }else if(resfirst_select=="6"){
        map["nbbm"]=resfirst_input
    }else if(resfirst_select=="9"){
        map["kdh"]=resfirst_input
    }else if(resfirst_select=="10"){
        map["sjys"]=resfirst_input
    }else if(resfirst_select=="12"){
        map["jcjgmc"]=resfirst_input
    }

    // 报告日期开始日期
    var bgrqstart = jQuery('#resfirst_formSearch #bgrqstart').val();
    map["bgrqstart"] = bgrqstart;
    // 报告日期结束日期
    var bgrqend = jQuery('#resfirst_formSearch #bgrqend').val();
    map["bgrqend"] = bgrqend;
    //科室
    var dwids = jQuery('#resfirst_formSearch #sjdw_id_tj').val();
    map["dwids"] = dwids;
    //合作伙伴分类
    var sjhbfls = jQuery('#resfirst_formSearch #sjhbfl_id_tj').val();
    map["sjhbfls"] = sjhbfls;
    //标本类型
    var yblxs = jQuery('#resfirst_formSearch #yblx_id_tj').val();
    map["yblxs"] = yblxs;
    var sfjs=jQuery('#resfirst_formSearch #sfjs_id_tj').val()
    map["sfjs"]=sfjs;
    var sftj=jQuery('#resfirst_formSearch #sftj_id_tj').val()
    map["sftj"]=sftj;
    //快递类型
    var kdlxs=$("#resfirst_formSearch #kdlx_id_tj").val();
    map["kdlxs"]=kdlxs
    //检测单位
    var jcdws=$("#resfirst_formSearch #jcdw_id_tj").val();
    map["jcdws"]=jcdws
    if(jQuery('#resfirst_formSearch #syrqflg').is(":checked")){
        map["syrqflg"] = "1";
    }
    //添加日期控件
    laydate.render({
        elem: '#resfirst_formSearch #bgrqstart'
        ,type: 'date'
    });
    //添加日期控件
    laydate.render({
        elem: '#resfirst_formSearch #bgrqend'
        ,type: 'date'
    });
    return map;
}
function searchResFirstResult(isTurnBack){
    $("#resfirst_formSearch #searchMore").slideUp("low");
    ResFirst_turnOff=true;
    $("#resfirst_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#resfirst_formSearch #resfirst_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#resfirst_formSearch #resfirst_list').bootstrapTable('refresh');
    }
}
function ResFirst_DealById(id,action,tourl,qf,flg_qf){
    if(!tourl){
        return;
    }
    if(action=="view"){
        var url=tourl+"?sjid="+id+"&jcxmdm="+$("#jcxmdm").val()
        $.showDialog(url,'查看ResFirst信息',viewResFirstConfig);
    }else if(action =='add'){
        var url= tourl;
        $.showDialog(url,'新增ResFirst信息',addResFirstConfig);
    }else if(action =='mod'){
        var url=tourl + "?sjid=" +id+"&xg_flg=1";
        $.showDialog(url,'修改ResFirst信息',modResFirstConfig);
    }else if(action =='upload'){
        var url=tourl + "?sjid="+id+"&qf="+qf+"&flg_qf="+flg_qf;
        $.showDialog(url,'ResFirst检测结果上传',uploadResFirstConfig);
    }else if(action=='detection'){
        var url=tourl + "?ids="+id+"&qf="+qf+"&flg_qf="+flg_qf;;
        $.showDialog(url,'修改ResFirst检测状态',detecteResFirstConfig);
    }else if(action=="recheck"){
        var url=tourl+"?sjid="+id;
        $.showDialog(url,'ResFirst复测申请',recheckResFirstConfig);
    }else if(action =='confirm'){
        if(id!=null){
            var url= tourl+"?sjid="+id;
        }else{
            var url= tourl;
        }
        $.showDialog(url,'ResFirst收样确认',confirmResFirstConfig);
    }else if(action=="verif"){
        var url=tourl+"?sjid="+id;
        $.showDialog(url,'送检验证',verifResFirstConfig);
    }else if(action=="rfssend"){
        var url=tourl+"?sjid="+id+"&jcxmdm="+$("#jcxmdm").val();
        $.showDialog(url,'ResFirst重发报告',resendConfig);
    }
}

var resendConfig = {
    width		: "800px",
    modalName	: "resendModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var sel_row = $('#resend_formSearch #resend_list').bootstrapTable('getSelections');// 获取选择行数据
                if(sel_row.length==1){
                    var $this = this;
                    var opts = $this["options"]||{};
                    $("#resend_formSearch input[name='access_token']").val($("#ac_tk").val());
                    $("#resend_formSearch input[name='fjid']").val(sel_row[0].fjid);
                    $("#resend_formSearch input[name='flg_qf']").val(sel_row[0].flg);
                    submitForm(opts["formName"]||"resend_formSearch",function(responseText,statusText){
                        if(responseText["status"] == 'success'){
                            $.success(responseText["message"],function() {
                                if(opts.offAtOnce){
                                    $.closeModal(opts.modalName);
                                }
                                searchResFirstResult();
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
                }else{
                    $.error("请选中一行");
                    return false;
                }
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};


var verifResFirstConfig = {
    width		: "1000px",
    modalName	: "verifResFirstModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if($("#verificationForm #isB").val()=="true"){
                    $.error("当前样本类型为“血”,不可发起PCR验证新增!");
                    return false;
                }
                if(!$("#verificationForm").valid()){
                    $.alert("请填写完整信息!");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                let data = JSON.parse($("#verificationForm #yzjgjson").val());
                let yzjg = "";
                $.each(data,function(i){
                    let jg = ''
                    if (typeof($("#verificationForm input[name='"+data[i].csid+"']:checked").val()) != 'undefined'){
                        jg = $("#verificationForm input[name='"+data[i].csid+"']:checked").val();
                    }else{
                        jg = $("#verificationForm input[name='"+data[i].csid+"']").val();
                        let str = jg.split(":")
                        jg = str[0]+":";
                    }
                    yzjg += jg;
                    if (i != data.length-1){
                        yzjg +=",";
                    }
                });
                $("#verificationForm input[name='yzjg']").val(yzjg);
                $("#verificationForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"verificationForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            //提交审核
                            if(responseText["auditType"]!=null){
                                showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){
                                    searchResFirstResult();
                                });
                            }else{
                                searchResFirstResult();
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

var confirmResFirstConfig = {
    width		: "1200px",
    modalName	: "confirmResFirstModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#ajaxForm").valid()){
                    return false;
                }
                if($("#ajaxForm #sfjs1").is(":checked")){
                    if($("#ajaxForm #nbbm").val()==""){
                        $.alert("您已选择接收，请填写内部编码");
                        return false;
                    }
                }
                var json = '';
                $("#ajaxForm #jclx input[type='checkbox']").each(function(index){
                    if (this.checked){
                        json+=","+$("#ajaxForm #"+this.value).attr("nbzbm")
                    }
                });
                if (json==''){
                    $.alert("请选择检测类型！");
                    return false;
                }
                $("#ajaxForm #jclxs").val(json.substring(1));
                var nbbm=$("#ajaxForm #nbbm").val();
                $("#ajaxForm #px").val(nbbm.substring(1,nbbm.length-1));
                var $this = this;
                var opts = $this["options"]||{};
                $("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
                var szz=$('#ajaxForm input:radio[name="szz"]:checked').val();
                if(szz==$("#ajaxForm #ysszz").text()){
                    $("#ajaxForm #grsz_flg").val(0);
                }else {
                    $("#ajaxForm #grsz_flg").val(1);
                }
                var flag=0;
                var fxsj_flag=false;
                $("#ajaxForm .ybztclass").each(function(){
                    if(this.checked==true){
                        flag++;
                        if ($(this).attr("cskz3")){
                            fxsj_flag = true
                        }
                    }
                });
                if(flag>0){
                    $.confirm("当前标本状态异常，是否继续确认？",function(result){
                        if(result){
                            submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
                                if(responseText["status"] == 'success'){
                                    if (fxsj_flag){
                                        $.confirm("将发起标本退回操作，是否继续确认？",function(_result){
                                            if (_result){
                                                $.ajax({
                                                    type:'post',
                                                    url:"/experiment/board/pagedataAddBoard",
                                                    cache: false,
                                                    data: {
                                                        "ywid":$("#ajaxForm #sjid").val(),
                                                        "access_token":$("#ac_tk").val(),
                                                        "bz":$("#ajaxForm #bz").val(),
                                                    },
                                                    dataType:'json',
                                                    success:function(data){
                                                        if (data.status = 'success' && data.auditType && data.ywid){
                                                            showAuditFlowDialog(data.auditType,data.ywid,function(){});
                                                            clearInfo(responseText)
                                                        }else{
                                                            $.alert("标本退回通知发起失败！");
                                                        }
                                                    }
                                                });
                                            }else{
                                                clearInfo(responseText)
                                            }
                                        })
                                    }else{
                                        clearInfo(responseText)
                                    }

                                }else if(responseText["status"] == "fail"){
                                    preSelectYbbh = null;
                                    $("#ajaxForm #ybbh").val("");
                                    preventResubmitForm(".modal-footer > button", false);
                                    $.error(responseText["message"],function() {
                                    });
                                } else{
                                    preventResubmitForm(".modal-footer > button", false);
                                    $.alert("不好意思！出错了！");
                                }
                            },".modal-footer > button");
                        }
                    });
                }else{
                    submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
                        if(responseText["status"] == 'success'){
                            clearInfo(responseText)
                        }else if(responseText["status"] == "fail"){
                            preSelectYbbh = null;
                            $("#ajaxForm #ybbh").val("");
                            preventResubmitForm(".modal-footer > button", false);
                            $.error(responseText["message"],function() {
                            });
                        } else{
                            preventResubmitForm(".modal-footer > button", false);
                            $.alert("不好意思！出错了！");
                        }
                    },".modal-footer > button");
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
function clearInfo(responseText){
    preventResubmitForm(".modal-footer > button", false);
    if(responseText["print"] && responseText["sz_flg"]){
        var host=responseText["print"];
        var sz_flg=responseText["sz_flg"];
        print_nbbm(host,sz_flg);
    }

    $.success(responseText["message"],function() {
        $("#ajaxForm #jclxs").val("")
        $("#ajaxForm #ybbh").val("");
        $("#ajaxForm #nbbm").val("");
        $("#ajaxForm #hzxm").text("");
        $("#ajaxForm #fjids").val("");
        $("#ajaxForm #bs").val("1");
        $("#ajaxForm #redisFj").remove();
        $("#ajaxForm #nl").text("");
        $("#ajaxForm #xbmc").text("");
        $("#ajaxForm #sjdw").text("");
        $("#ajaxForm #sjys").text("");
        $("#ajaxForm #xgsj").val("");
        $("#ajaxForm #ks").text("");
        $("#ajaxForm #db").text("");
        $("#ajaxForm #ybtj").text("");
        $("#ajaxForm #yblxmc").text("");
        $("#ajaxForm #yblxdm").val("");
        $("#ajaxForm #jcdwmc").val("");
        $("#ajaxForm #jcdw").val("");
        $("#ajaxForm #sjqfmc").val("");
        $("#ajaxForm #yblx_flg").val("");
        $("#ajaxForm #state").val("");
        $("#ajaxForm #jcxm").text("");
        $("#ajaxForm #lczz").text("");
        $("#ajaxForm #qqzd").text("");
        $("#ajaxForm #gzbymc").text("");
        $("#ajaxForm #sjid").val("");
        $("#ajaxForm .ybztclass").prop("checked",false)
        $("#ajaxForm .sfjs").prop("checked",false)
        $("#ajaxForm #bz").val("");
        $("#ajaxForm #nbbmspan").show();
        $("#ajaxForm #jstj").val("");
        $("#ajaxForm #print_flg").val("");
        $("#ajaxForm #ybzt_flg").val("");
        $("#ajaxForm #ybztmc").val("");
        $("#ajaxForm #ysdh").val("");
        $("#ajaxForm #kyxmmc").text("")
        $("#ajaxForm .fileinput-remove-button").click();
        $("#ajaxForm label[name='jclxs']").remove()
        $("#ajaxForm #button").remove()
        $("#ajaxForm #button2").remove()
        searchSjxxResult();
    });
}

var recheckResFirstConfig={
    width		: "800px",
    modalName	: "recheckResFirstConfig",
    formName	: "recheck_Form",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		:	{
        success : 	{
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#recheck_Form").valid()){
                    $.alert("请填写完整信息！");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#recheck_Form input[name='access_token']").val($("#ac_tk").val());
                var ybztcskz = $("#recheck_Form #ybztcskz").val();
                if(ybztcskz=="1"){
                    $.alert("量仅一次");
                    return false;
                }
                if($("#recheck_Form .yy")!=null && $("#recheck_Form .yy").length>0){
                    var yys="";
                    var yymcs="";
                    for(var i=0;i<$("#recheck_Form .yy").length;i++){
                        if($("#recheck_Form .yy")[i].checked){
                            yys=yys+","+$("#recheck_Form .yy")[i].value;
                            yymcs=yymcs+";"+$("#recheck_Form #yy_"+$("#recheck_Form .yy")[i].value+" span").text();
                        }
                    }
                    $("#recheck_Form #yys").val(yys.substring(1));
                    $("#recheck_Form #yy").val(yymcs.substring(1));
                }
                submitForm(opts["formName"]||"recheck_Form",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        if(opts.offAtOnce){
                            $.closeModal(opts.modalName);
                        }
                        //提交审核
                        if(responseText["auditType"]!=null){
                            showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){
                                searchResFirstResult();
                            });
                        }else{
                            searchResFirstResult();
                        }
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
}
var uploadResFirstConfig = {
    width		: "900px",
    modalName	: "uploadResFirstModal",
    formName	: "uploadFileAjaxForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#uploadFileAjaxForm #fjids").val()){
                    $.error("请上传文件！")
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#uploadFileAjaxForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"uploadFileAjaxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.closeModal(opts.modalName);
                        searchResFirstResult();
                        $.showDialog("/experimentS/experimentS/pagedataUpView?fjid="+responseText["fjids"],'上传进度',viewUpViewConfig);
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
var viewUpViewConfig = {
    width		: "900px",
    modalName	:"viewUpViewModal",
    offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
var detecteResFirstConfig = {
    width		: "800px",
    modalName	: "detecteResFirstModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var $this = this;
                var opts = $this["options"]||{};
                var sfsytz="0";
                if($("#detectionForm #djcbj")=='1'){
                    if($("#detectionForm #dsyrqfirst").val()==null || $("#detectionForm #dsyrqfirst").val()==''){
                        $.confirm("请选择DNA实验日期！");
                        return false;
                    }
                }
                if($("#detectionForm #jcbj")=='1'){
                    if($("#detectionForm #syrqfirst").val()==null || $("#detectionForm #syrqfirst").val()==''){
                        $.confirm("请选择RNA实验日期！");
                        return false;
                    }
                }
                if($("#detectionForm #qtjcbj")=='1'){
                    if($("#detectionForm #qtsyrqfirst").val()==null || $("#detectionForm #qtsyrqfirst").val()==''){
                        $.confirm("请选择其他实验日期！");
                        return false;
                    }
                }
                if(($("#detectionForm #syrqfirst").val()==null || $("#detectionForm #syrqfirst").val()=='') && ($("#detectionForm #syrq").val()!=null && $("#detectionForm #syrq").val()!='')){
                    $("#detectionForm #sfsytz").val("1");
                }
                if(($("#detectionForm #dsyrqfirst").val()==null || $("#detectionForm #dsyrqfirst").val()=='') && ($("#detectionForm #dsyrq").val()!=null && $("#detectionForm #dsyrq").val()!='')){
                    $("#detectionForm #sfsytz").val("1");
                }
                if(($("#detectionForm #qtsyrqfirst").val()==null || $("#detectionForm #qtsyrqfirst").val()=='') && ($("#detectionForm #qtsyrq").val()!=null && $("#detectionForm #qtsyrq").val()!='')){
                    $("#detectionForm #sfsytz").val("1");
                }
                $("#detectionForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"detectionForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchResFirstResult();
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
var modResFirstConfig = {
    width		: "1000px",
    modalName	: "modResFirstModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                $("#ajaxForm_resfirstEdit #db").val($("#ajaxForm_resfirstEdit #db").val().replace(/\s+/g,""));

                if($("#ajaxForm_resfirstEdit #ybbh").val()==""||$("#ajaxForm_resfirstEdit #ybbh").val()==null){
                    $.alert("请输入标本编号！");
                    return false;
                }else if($("#ajaxForm_resfirstEdit #hzxm").val()==""||$("#ajaxForm_resfirstEdit #hzxm").val()==null){
                    $.alert("请输入患者姓名！");
                    return false;
                }else if($("#ajaxForm_resfirstEdit input:radio[name='xb']:checked").val()==null){
                    $.alert("请选择性别！");
                    return false;
                }else if($("#ajaxForm_resfirstEdit #nl").val()==""||$("#ajaxForm_resfirstEdit #nl").val()==null){
                    $.alert("请输入年龄！");
                    return false;
                }else if($("#ajaxForm_resfirstEdit #hospitalname").val()==""||$("#ajaxForm_resfirstEdit #hospitalname").val()==null){
                    $.alert("请选择医院名称！");
                    return false;
                }else if($("#ajaxForm_resfirstEdit #sjdw").val()==""||$("#ajaxForm_resfirstEdit #sjdw").val()==null){
                    $.alert("医院名称不正确！");
                    return false;
                }else if($("#ajaxForm_resfirstEdit #sjdwmc").val()==""||$("#ajaxForm_resfirstEdit #sjdwmc").val()==null){
                    $.alert("请输入报告显示单位！");
                    return false;
                }else if($("#ajaxForm_resfirstEdit #sjys").val()==""||$("#ajaxForm_resfirstEdit #sjys").val()==null){
                    $.alert("请输入主治医师！");
                    return false;
                }else if($("#ajaxForm_resfirstEdit #ks option:selected").text()=="--请选择--"){
                    $.alert("请选择科室！");
                    return false;
                }else if($("#ajaxForm_resfirstEdit #db").val()==""||$("#ajaxForm_resfirstEdit #db").val()==null){
                    $.alert("请输入合作伙伴！");
                    return false;
                }else if($("#ajaxForm_resfirstEdit input:radio[name='jcxmids']:checked").val()==null){
                    $.alert("请选择检测项目！");
                    return false;
                }else if($("#ajaxForm_resfirstEdit input:radio[name='sjqf']:checked").val()==null){
                    $.alert("请选择送检区分！");
                    return false;
                }else if(!$("#ajaxForm_resfirstEdit #kyDIV").is(":hidden") && $("#ajaxForm_resfirstEdit #kyxm option:selected").text()=="--请选择--"){
                    $.alert("请选择立项编号！");
                    return false;
                }else if($("#ajaxForm_resfirstEdit #yblx option:selected").text()=="--请选择--"){
                    $.alert("请选择标本类型！");
                    return false;
                }else if($("#ajaxForm_resfirstEdit #ybtj").val()==""||$("#ajaxForm_resfirstEdit #ybtj").val()==null){
                    $.alert("请输入标本体积！");
                    return false;
                }else if($("#ajaxForm_resfirstEdit #cyrq").val()==""||$("#ajaxForm_resfirstEdit #cyrq").val()==null){
                    $.alert("请输入采样日期！");
                    return false;
                }else if($("#ajaxForm_resfirstEdit #kdlx").val()==""||$("#ajaxForm_resfirstEdit #kdlx").val()==null){
                    $.alert("请选择快递类型！");
                    return false;
                }else if($("#ajaxForm_resfirstEdit .jcxm").length >0){
                    var json = [];
                    for (let i = 0; i <= $("#ajaxForm_resfirstEdit .jcxm").length -1; i++) {
                        if ($("#ajaxForm_resfirstEdit #"+$("#ajaxForm_resfirstEdit .jcxm")[i].id).is(':checked')){
                            var sz = {"jcxmid":'',"jczxmid":'',"flag":'',"csmc":'',"zcsmc":'',"sfsf":'',"yfje":'',"sfje":'',"fkrq":''};
                            sz.jcxmid = $("#ajaxForm_resfirstEdit .jcxm")[i].id;
                            sz.jczxmid = null;
                            sz.csmc = $("#ajaxForm_resfirstEdit #" + $("#ajaxForm_resfirstEdit .jcxm")[i].id).attr("csmc");
                            sz.zcsmc = null;
                            sz.flag = $("#ajaxForm_resfirstEdit #" + $("#ajaxForm_resfirstEdit .jcxm")[i].id).attr("flag");
                            json.push(sz)
                        }
                    }
                    if (json.length == 0){
                        $.alert("请选择检测项目！");
                        return false;
                    }
                    if($("#ajaxForm_resfirstEdit .jczxm").length >0){
                        for (let i = 0; i <= $("#ajaxForm_resfirstEdit .jczxm").length -1; i++) {
                            if ($("#ajaxForm_resfirstEdit #" + $("#ajaxForm_resfirstEdit .jczxm")[i].id).is(':checked')) {
                                var jcxmid = $("#ajaxForm_resfirstEdit #" + $("#ajaxForm_resfirstEdit .jczxm")[i].id).attr("jcxmid")
                                if (json.length > 0) {
                                    for (let k = json.length - 1; k >= 0; k--) {
                                        if (json[k].jcxmid == jcxmid && json[k].jczxmid == null) {
                                            json.splice(k, 1);
                                        }
                                    }
                                    var sz = {"jcxmid": '', "jczxmid": '',"flag":'',"csmc":'',"zcsmc":'',"sfsf":'',"yfje":'',"sfje":'',"fkrq":''};
                                    sz.jcxmid = $("#ajaxForm_resfirstEdit #" + $("#ajaxForm .jczxm")[i].id).attr("jcxmid");
                                    sz.jczxmid = $("#ajaxForm_resfirstEdit .jczxm")[i].id;
                                    sz.csmc = $("#ajaxForm_resfirstEdit #" + sz.jcxmid).attr("csmc");
                                    sz.zcsmc = $("#ajaxForm_resfirstEdit #" + $("#ajaxForm .jczxm")[i].id).attr("csmc");
                                    sz.flag = $("#ajaxForm_resfirstEdit #" + sz.jcxmid).attr("flag");
                                    json.push(sz)
                                }
                            }
                        }
                        for (let k = json.length - 1; k >= 0; k--) {
                            if (json[k].flag == "1"){
                                if (json[k].jczxmid == null){
                                    $.alert("请选择检测子项目！");
                                    return false;
                                }
                            }
                        }
                    }
                    var dtoList = JSON.parse($("#ajaxForm_resfirstEdit #dtoList").val());
                    var jcxmmc = "";
                    for (let i = 0; i < json.length; i++) {
                        jcxmmc+=","+json[i].csmc;
                        if (json[i].zcsmc)
                        jcxmmc+="-"+json[i].zcsmc;
                        for (let j = dtoList.length-1; j >= 0; j--) {
                            if (json[i].jcxmid == dtoList[j].jcxmid && (!json[i].jczxmid || !dtoList[j].jczxmid || json[i].jczxmid == dtoList[j].jczxmid)) {
                                json[i].xmglid = dtoList[j].xmglid;
                                json[i].sfsf = dtoList[j].sfsf;
                                json[i].yfje = dtoList[j].yfje;
                                json[i].sfje = dtoList[j].sfje;
                                json[i].fkrq = dtoList[j].fkrq;
                            }
                        }
                    }
                    $("#ajaxForm_resfirstEdit #jcxmmc").val(jcxmmc.substring(1));
                    $("#ajaxForm_resfirstEdit #jcxm").val(JSON.stringify(json))
                }
                //验证快递单号
                var cskz2=$("#ajaxForm_resfirstEdit #kdlx").find("option:selected").attr("cskz2");
                if(!cskz2){
                    if($("#ajaxForm_resfirstEdit #kdh").val()==""){
                        $.alert("请填写快递单号！");
                        return false;
                    }
                }
                if($("#ajaxForm_resfirstEdit #nbbm").val()!=null){
                    var nbbm=$("#ajaxForm_resfirstEdit #nbbm").val();
                    $("#ajaxForm_resfirstEdit #px").val(nbbm.substring(1,nbbm.length-1));
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#ajaxForm_resfirstEdit input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"ajaxForm_resfirstEdit",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
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
var addResFirstConfig = {
    width		: "1000px",
    modalName	: "addResFirstModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                //去除空格
                $("#ajaxForm_resfirstEdit #db").val($("#ajaxForm_resfirstEdit #db").val().replace(/\s+/g,""));
                $("#ajaxForm_resfirstEdit #sjys").val($("#ajaxForm_resfirstEdit #sjys").val().trim());

                var cskz2=$("#ajaxForm_resfirstEdit #kdlx").find("option:selected").attr("cskz2");

                if($("#ajaxForm_resfirstEdit #ybbh").val()==""||$("#ajaxForm_resfirstEdit #ybbh").val()==null){
                    $.alert("请输入标本编号！");
                    return false;
                }else if($("#ajaxForm_resfirstEdit #hzxm").val()==""||$("#ajaxForm_resfirstEdit #hzxm").val()==null){
                    $.alert("请输入患者姓名！");
                    return false;
                }else if($("#ajaxForm_resfirstEdit input:radio[name='xb']:checked").val()==null){
                    $.alert("请选择性别！");
                    return false;
                }else if($("#ajaxForm_resfirstEdit #nl").val()==""||$("#ajaxForm_resfirstEdit #nl").val()==null){
                    $.alert("请输入年龄！");
                    return false;
                }else if($("#ajaxForm_resfirstEdit #sjdw").val()==""||$("#ajaxForm_resfirstEdit #sjdw").val()==null){
                    $.alert("医院名称不正确！");
                    return false;
                }else if($("#ajaxForm_resfirstEdit #sjdwmc").val()==""||$("#ajaxForm_resfirstEdit #sjdwmc").val()==null){
                    $.alert("请输入报告显示单位！");
                    return false;
                }else if($("#ajaxForm_resfirstEdit #sjys").val()==""||$("#ajaxForm_resfirstEdit #sjys").val()==null){
                    $.alert("请输入主治医师！");
                    return false;
                }else if($("#ajaxForm_resfirstEdit #ks option:selected").text()=="--请选择--"){
                    $.alert("请选择科室！");
                    return false;
                }else if($("#ajaxForm_resfirstEdit #db").val()==""||$("#ajaxForm_resfirstEdit #db").val()==null){
                    $.alert("请输入合作伙伴！");
                    return false;
                }else if($("#ajaxForm_resfirstEdit input:radio[name='jcxmids']:checked").val()==null){
                    $.alert("请选择检测项目！");
                    return false;
                }else if($("#ajaxForm_resfirstEdit input:radio[name='sjqf']:checked").val()==null){
                    $.alert("请选择送检区分！");
                    return false;
                }else if(!$("#ajaxForm_resfirstEdit #kyDIV").is(":hidden") && $("#ajaxForm_resfirstEdit #kyxm option:selected").text()=="--请选择--"){
                    $.alert("请选择立项编号！");
                    return false;
                }else if($("#ajaxForm_resfirstEdit #yblx option:selected").text()=="--请选择--"){
                    $.alert("请选择标本类型！");
                    return false;
                }else if($("#ajaxForm_resfirstEdit #ybtj").val()==""||$("#ajaxForm_resfirstEdit #ybtj").val()==null){
                    $.alert("请输入标本体积！");
                    return false;
                }else if($("#ajaxForm_resfirstEdit #cyrq").val()==""||$("#ajaxForm_resfirstEdit #cyrq").val()==null){
                    $.alert("请输入采样日期！");
                    return false;
                }else if($("#ajaxForm_resfirstEdit #kdlx").val()==""||$("#ajaxForm_resfirstEdit #kdlx").val()==null){
                    $.alert("请选择快递类型！");
                    return false;
                }else if(!cskz2){
                    if($("#ajaxForm_resfirstEdit #kdh").val()==""){
                        $.alert("请填写快递单号！");
                        return false;
                    }
                }else if($("#ajaxForm_resfirstEdit .jcxm").length >0){
                    var json = [];
                    for (let i = 0; i <= $("#ajaxForm_resfirstEdit .jcxm").length -1; i++) {
                        if ($("#ajaxForm_resfirstEdit #"+$("#ajaxForm_resfirstEdit .jcxm")[i].id).is(':checked')){
                            var sz = {"jcxmid":'',"jczxmid":'',"flag":'',"csmc":'',"zcsmc":''};
                            sz.jcxmid = $("#ajaxForm_resfirstEdit .jcxm")[i].id;
                            sz.jczxmid = null;
                            sz.csmc = $("#ajaxForm_resfirstEdit #" + $("#ajaxForm_resfirstEdit .jcxm")[i].id).attr("csmc");
                            sz.zcsmc = null;
                            sz.flag = $("#ajaxForm_resfirstEdit #" + $("#ajaxForm_resfirstEdit .jcxm")[i].id).attr("flag");
                            json.push(sz)
                        }
                    }
                    if (json.length == 0){
                        $.alert("请选择检测项目！");
                        return false;
                    }
                    if($("#ajaxForm_resfirstEdit .jczxm").length >0){
                        for (let i = 0; i <= $("#ajaxForm_resfirstEdit .jczxm").length -1; i++) {
                            if ($("#ajaxForm_resfirstEdit #" + $("#ajaxForm_resfirstEdit .jczxm")[i].id).is(':checked')) {
                                var jcxmid = $("#ajaxForm_resfirstEdit #" + $("#ajaxForm_resfirstEdit .jczxm")[i].id).attr("jcxmid")
                                if (json.length > 0) {
                                    for (let k = json.length - 1; k >= 0; k--) {
                                        if (json[k].jcxmid == jcxmid && json[k].jczxmid == null) {
                                            json.splice(k, 1);
                                        }
                                    }
                                    var sz = {"jcxmid": '', "jczxmid": '',"flag":'',"csmc":'',"zcsmc":''};
                                    sz.jcxmid = $("#ajaxForm_resfirstEdit #" + $("#ajaxForm_resfirstEdit .jczxm")[i].id).attr("jcxmid");
                                    sz.jczxmid = $("#ajaxForm_resfirstEdit .jczxm")[i].id;
                                    sz.csmc = $("#ajaxForm_resfirstEdit #" + sz.jcxmid).attr("csmc");
                                    sz.zcsmc = $("#ajaxForm_resfirstEdit #" + $("#ajaxForm .jczxm")[i].id).attr("csmc");
                                    sz.flag = $("#ajaxForm_resfirstEdit #" + sz.jcxmid).attr("flag");
                                    json.push(sz)
                                }
                            }
                        }
                        for (let k = json.length - 1; k >= 0; k--) {
                            if (json[k].flag == "1"){
                                if (json[k].jczxmid == null){
                                    $.alert("请选择检测子项目！");
                                    return false;
                                }
                            }
                        }
                    }
                    var jcxmmc = "";
                    for (let i = 0; i < json.length; i++) {
                        jcxmmc+=","+json[i].csmc;
                        if (json[i].zcsmc)
                            jcxmmc+="-"+json[i].zcsmc;
                    }
                    $("#ajaxForm_resfirstEdit #jcxmmc").val(jcxmmc.substring(1));
                    $("#ajaxForm_resfirstEdit #jcxm").val(JSON.stringify(json))
                }

                var $this = this;
                var opts = $this["options"]||{};
                $("#ajaxForm_resfirstEdit input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"ajaxForm_resfirstEdit",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
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
var viewResFirstConfig = {
    width		: "1000px",
    modalName	:"viewResFirstModal",
    offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var ResFirst_oButton=function(){
    var oButtonInit = new Object();
    var postdata = {};
    oButtonInit.Init=function(){
        var btn_query=$("#resfirst_formSearch #btn_query");
        var btn_view = $("#resfirst_formSearch #btn_view");
        var btn_add = $("#resfirst_formSearch #btn_add");
        var btn_mod = $("#resfirst_formSearch #btn_mod");
        var btn_del = $("#resfirst_formSearch #btn_del");
        var btn_recheck=$("#resfirst_formSearch #btn_recheck"); //复检
        var btn_upload = $("#resfirst_formSearch #btn_upload");
        var btn_detection=$("#resfirst_formSearch #btn_detection");
        var btn_confirm = $("#resfirst_formSearch #btn_confirm");//收样确认
        var btn_verif=$("#resfirst_formSearch #btn_verif");//送检验证
        var btn_rfssend=$("#resfirst_formSearch #btn_rfssend");//重发报告

        /*-----------------------模糊查询ok------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchResFirstResult(true);
            });
        }

        /*---------------------------送检验证-----------------------------------*/
        btn_rfssend.unbind("click").click(function(){
            var sel_row = $('#resfirst_formSearch #resfirst_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                ResFirst_DealById(sel_row[0].sjid,"rfssend",btn_rfssend.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------送检验证-----------------------------------*/
        btn_verif.unbind("click").click(function(){
            var sel_row = $('#resfirst_formSearch #resfirst_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                var nbbm = sel_row[0].nbbm;
                if (nbbm != null && nbbm != "") {
                    var lastStr = nbbm.substring(nbbm.length-1);
                    if("B"==lastStr || "b"==lastStr){
                        $.error("当前样本类型为“血”,不可发起PCR验证新增!");
                        return;
                    }
                }
                ResFirst_DealById(sel_row[0].sjid,"verif",btn_verif.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*------------------------收样确认---------------------------------*/
        //送检收样确认
        btn_confirm.unbind("click").click(function(){
            var sel_row = $('#resfirst_formSearch #resfirst_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                ResFirst_DealById(sel_row[0].sjid,"confirm",btn_confirm.attr("tourl"));
            }else{
                ResFirst_DealById(null,"confirm",btn_confirm.attr("tourl"));
            }
        });
        /*-----------------------查看------------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#resfirst_formSearch #resfirst_list').bootstrapTable('getSelections');// 获取选择行数据
            if(sel_row.length==1){
                ResFirst_DealById(sel_row[0].sjid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------修改信息-----------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#resfirst_formSearch #resfirst_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                ResFirst_DealById(sel_row[0].sjid,"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* --------------------------添加信息-----------------------------*/
        btn_add.unbind("click").click(function(){
            ResFirst_DealById(null,"add",btn_add.attr("tourl"));
        });
        /* --------------------------删除信息-----------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#resfirst_formSearch #resfirst_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].sjid;
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
                                        searchResFirstResult();
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
        /* -----------------------实验(功能：修改DNA/RNA的实验状态)------------*/
        btn_detection.unbind("click").click(function(){
            var sel_row = $('#resfirst_formSearch #resfirst_list').bootstrapTable('getSelections');
            if(sel_row.length==0){
                $.error("请至少选择一行");
                return;
            }else{
                var ids="";
                for(var i=0; i<sel_row.length; i++){
                    ids = ids+","+sel_row[i].sjid;
                }
                ids=ids.substr(1);
                ResFirst_DealById(ids,"detection",btn_detection.attr("tourl"));
            }
        });
        /* --------------------------上传信息-----------------------------*/
        btn_upload.unbind("click").click(function(){
            var sel_row = $('#resfirst_formSearch #resfirst_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                if(sel_row[0].jcxmkzcs == 'F'){
                    ResFirst_DealById(sel_row[0].sjid,"upload",btn_upload.attr("tourl"),sel_row[0].qf,sel_row[0].flg_qf);
                }else {
                    $.error("请选择ResFirst项目的数据！");
                }
            }else{
                ResFirst_DealById(null,"upload",btn_upload.attr("tourl"));

            }
        });
        /* --------------------------复测信息-----------------------------*/
        btn_recheck.unbind("click").click(function(){
            var sel_row = $('#resfirst_formSearch #resfirst_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                ResFirst_DealById(sel_row[0].sjid,"recheck",btn_recheck.attr("tourl"));
            }else{
                $.error("请选中一行!");
                return;
            }
        })
        /*--------------------------------------------------------*/
        /**显示隐藏**/
        $("#resfirst_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(ResFirst_turnOff){
                $("#resfirst_formSearch #searchMore").slideDown("low");
                ResFirst_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#resfirst_formSearch #searchMore").slideUp("low");
                ResFirst_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
            //showMore();
        });
    }
    return oButtonInit;
}


$(function(){
    var oTable = new ResFirst_TableInit();
    oTable.Init();

    var oButton = new ResFirst_oButton();
    oButton.Init();

    $("#resfirst_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });

    jQuery('#resfirst_formSearch .chosen-select').chosen({width: '100%'});
})