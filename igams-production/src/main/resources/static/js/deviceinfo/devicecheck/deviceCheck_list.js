var DeviceCheck_turnOff=true;

var DeviceCheck_TableInit = function () {
    var oTableInit = new Object();
    // 初始化Table
    oTableInit.Init = function () {
        $('#deviceCheck_formSearch #deviceCheck_list').bootstrapTable({
            url: $("#deviceCheck_formSearch #urlPrefix").val()+'/device/device/pageGetListDeviceCheck',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#deviceCheck_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"dhxx.dhrq",					// 排序字段
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
            uniqueId: "sbysid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%'
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
                field: 'sbysid',
                title: '设备验收ID',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'hwid',
                title: '货物ID',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'sbysdh',
                title: '验收单号',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'wlbm',
                title: '设备编码',
                formatter:sbbmformatter,
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'wlmc',
                title: '设备名称',
                width: '8%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'sqrmc',
                title: '申请人',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sqbmmc',
                title: '申请部门名称',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'htnbbh',
                title: '订单编号',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'djh',
                title: '请购单号',
                width: '10%',
                align: 'left',
                formatter:djhformatter,
                sortable: true,
                visible: true
            },{
                field: 'sfgdzc',
                title: '是否固定资产',
                width: '8%',
                align: 'left',
                sortable: true,
                formatter:sfgdzcformat,
                visible: true
            },{
                field: 'gdzcbh',
                title: '固定资产编号',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'dhsl',
                title: '数量',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'ssyfxmmc',
                title: '所属研发项目',
                width: '7%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'dhrq',
                title: '到货日期',
                width: '7%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sbccbh',
                title: '设备出厂编号',
                width: '7%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'scph',
                title: '生产批号',
                width: '7%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sydd',
                title: '使用地点',
                width: '7%',
                align: 'left',
                visible: true
            },{
                field: 'ysjg',
                title: '验收结果',
                width: '7%',
                align: 'left',
                sortable: true,
                formatter:ysjgformat,
                visible: true
            },{
                field: 'sfgz',
                title: '贵重物品',
                width: '7%',
                align: 'left',
                sortable: true,
                formatter:sfgzformat,
                visible: true
            },{
                field: 'ysrq',
                title: '验收日期',
                width: '7%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'scs',
                title: '生产厂家',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'gysmc',
                title: '供应商',
                width: '12%',
                align: 'left',
                visible: true
            },{
                field: 'dh',
                title: '联系电话',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sbbh',
                title: '设备编号',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'bxsj',
                title: '保修时间',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'xlh',
                title: '序列号',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'gzsj',
                title: '购置时间',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'dzwz',
                title: '定置位置',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'qyrq',
                title: '启用日期',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'glrymc',
                title: '设备管理员',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'ysrmc',
                title: '验收人',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'sblxmc',
                title: '设备类型',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'ysdd',
                title: '验收地点',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'bz',
                title: '备注',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'byzq',
                title: '保养周期',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'xcbysj',
                title: '下次保养时间',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'sfjl',
                title: '是否计量',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false,
                formatter:sfjlFormatter
            },{
                field: 'jlzq',
                title: '计量周期',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'xcjlsj',
                title: '下次计量时间',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'sfyz',
                title: '是否验证',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false,
                formatter:sfyzFormatter
            },{
                field: 'yzzq',
                title: '验证周期',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'xcyzsj',
                title: '下次验证时间',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'bzqk',
                title: '包装情况',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'wgqk',
                title: '外观情况',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'jyqk',
                title: '绝缘情况',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'zxdnr',
                title: '装箱单内容',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false,
                formatter:zxdnrFormatter
            },{
                field: 'sbcs',
                title: '设备参数',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false,
                formatter:sbcsFormatter
            },{
                field: 'yqgnyz',
                title: '仪器功能运转',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false,
                formatter:yqgnyzFormatter
            },{
                field: 'sjcs',
                title: '数据存储',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false,
                formatter:sjcsFormatter
            },{
                field: 'sjwzxhbmx',
                title: '数据完整性和保密性',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false,
                formatter:sjwzxhbmxFormatter
            },{
                field: 'xnyz',
                title: '性能验证',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false,
                formatter:xnyzFormatter
            },{
                field: 'ysjl',
                title: '验收结论',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false,
                formatter:ysjlFormatter
            },{
                field: 'zt',
                title: '状态',
                width: '8%',
                align: 'left',
                formatter:ztFormat,
                visible: true
            },{
                field: 'cz',
                title: '操作',
                titleTooltip:'操作',
                width: '7%',
                align: 'left',
                formatter:czFormat,
                visible: true
            }],
            onLoadSuccess: function (map) {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                DeviceCheckDealById(row.hwid,'view',$("#deviceCheck_formSearch #btn_view").attr("tourl"));
            }
        });
        $("#deviceCheck_formSearch #deviceCheck_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true
        })
    };
    // 得到查询的参数
    oTableInit.queryParams = function(params){
        // 请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
        // 例如 toolbar 中的参数
        // 如果 queryParamsType = ‘limit’ ,返回参数必须包含
        // limit, offset, search, sort, order
        // 否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder.
        // 返回false将会终止请求
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的2333
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "sbys.sbysid", // 防止同名排位用
            sortLastOrder: "desc",// 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return deviceCheckSearchData(map);
    };
    return oTableInit;
};
/**
 * 设备编码格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function sbbmformatter(value,row,index) {
    var html = "";
    if(row.wlbm==null){
        html = "";
    }else{
        html += "<a href='javascript:void(0);' onclick=\"veriPurchase_queryByWlbm('" + row.wlid + "',event)\">"+row.wlbm+"</a>";
    }
    return html;
}
function veriPurchase_queryByWlbm(wlid){
    var url=$("#deviceCheck_formSearch #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'物料信息',WlConfig);
}
var WlConfig = {
    width		: "800px",
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
 * 单据号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function djhformatter(value,row,index){
    var html = "";
    if(row.djh==null){
        html = "";
    }else{
        html += "<a href='javascript:void(0);' onclick=\"queryByQgid('"+row.qgid+"')\">"+row.djh+"</a>";

    }
    return html;
}

function queryByQgid(qgid){
    var url=$("#deviceCheck_formSearch #urlPrefix").val()+"/purchase/purchase/viewPurchase?qgid="+qgid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'请购信息',viewQgConfig);
}

var viewQgConfig={
    width		: "1500px",
    modalName	:"viewQgModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
}

/**
 * 验收结果格式化
 * @returns
 */
function ysjgformat(value,row,index) {
    var html="";
    if (row.ysjg == '0') {
        html="<span style='color:red;'>"+"不合格"+"</span>";
    }else if (row.ysjg == '1') {
        html="<span style='color:green;'>"+"合格"+"</span>";
    }
    return html;
}

/**
 * 是否固定资产格式化
 * @returns
 */
function sfgdzcformat(value,row,index) {
    var html="";
    if (row.sfgdzc == '0') {
        html="<span style='color:red;'>"+"否"+"</span>";
    }else if (row.sfgdzc == '1') {
        html="<span style='color:green;'>"+"是"+"</span>";
    }
    return html;
}

function sfjlFormatter(value,row,index) {
    var html="";
    if (row.sfjl == '0') {
        html="<span style='color:red;'>"+"否"+"</span>";
    }else if (row.sfjl == '1') {
        html="<span style='color:green;'>"+"是"+"</span>";
    }
    return html;
}
function sfyzFormatter(value,row,index) {
    var html="";
    if (row.sfyz == '0') {
        html="<span style='color:red;'>"+"否"+"</span>";
    }else if (row.sfyz == '1') {
        html="<span style='color:green;'>"+"是"+"</span>";
    }
    return html;
}
function zxdnrFormatter(value,row,index) {
    var html="";
    if (row.zxdnr == '0') {
        html="<span style='color:red;'>"+"不符合"+"</span>";
    }else if (row.zxdnr == '1') {
        html="<span style='color:green;'>"+"符合"+"</span>";
    }
    return html;
}
function sbcsFormatter(value,row,index) {
    var html="";
    if (row.sbcs == '0') {
        html="<span style='color:red;'>"+"不符合"+"</span>";
    }else if (row.sbcs == '1') {
        html="<span style='color:green;'>"+"符合"+"</span>";
    }
    return html;
}
function yqgnyzFormatter(value,row,index) {
    var html="";
    if (row.yqgnyz == '0') {
        html="<span style='color:red;'>"+"不符合"+"</span>";
    }else if (row.yqgnyz == '1') {
        html="<span style='color:green;'>"+"符合"+"</span>";
    }
    return html;
}
function sjcsFormatter(value,row,index) {
    var html="";
    if (row.sjcs == '0') {
        html="<span style='color:red;'>"+"不符合"+"</span>";
    }else if (row.sjcs == '1') {
        html="<span style='color:green;'>"+"符合"+"</span>";
    }
    return html;
}
function sjwzxhbmxFormatter(value,row,index) {
    var html="";
    if (row.sjwzxhbmx == '0') {
        html="<span style='color:red;'>"+"不符合"+"</span>";
    }else if (row.sjwzxhbmx == '1') {
        html="<span style='color:green;'>"+"符合"+"</span>";
    }
    return html;
}
function xnyzFormatter(value,row,index) {
    var html="";
    if (row.xnyz == '0') {
        html="<span style='color:red;'>"+"否"+"</span>";
    }else if (row.xnyz == '1') {
        html="<span style='color:green;'>"+"是"+"</span>";
    }
    return html;
}
function ysjlFormatter(value,row,index) {
    var html="";
    if (row.ysjl == '0') {
        html="<span style='color:red;'>"+"否"+"</span>";
    }else if (row.ysjl == '1') {
        html="<span style='color:green;'>"+"是"+"</span>";
    }
    return html;
}
/**
 * 贵重物品格式化
 * @returns
 */
function sfgzformat(value,row,index) {
    var html="";
    if (row.sfgz == '1') {
        html="<span style='color:red;'>"+"是"+"</span>";
    }else if (row.sfgz == '0'){
        html="<span style='color:green;'>"+"否"+"</span>";
    }
    return html;
}

/**
 * 状态格式化函数
 * @returns
 */
function ztFormat(value,row,index) {
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.sbysid + "\",event,\"AUDIT_DEVICECHECK\",{prefix:\"" + $('#deviceCheck_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
    }else if (row.zt == '15') {
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.sbysid + "\",event,\"AUDIT_DEVICECHECK\",{prefix:\"" + $('#deviceCheck_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
    }else if (row.zt == '10'){
        return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.sbysid + "\",event,\"AUDIT_DEVICECHECK\",{prefix:\"" + $('#deviceCheck_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
    }else{
        return '未维护';
    }
}

/**
 * 操作按钮格式化函数
 * @returns
 */
function czFormat(value,row,index) {
    if (row.zt == '10') {
        return "<span class='btn btn-warning' onclick=\"recallDeviceCheck('" + row.sbysid +"','" + row.shlx+ "',event)\" >撤回</span>";
    }else{
        return "";
    }
}

//撤回项目提交
function recallDeviceCheck(sbysid,event){
    var auditType = $("#deviceCheck_formSearch #auditType").val();
    var msg = '您确定要撤回该条审核吗？';
    var purchase_params = [];
    purchase_params.prefix = $("#deviceCheck_formSearch #urlPrefix").val();
    $.confirm(msg,function(result){
        if(result){
            doAuditRecall(auditType,sbysid,function(){
                searchDeviceCheckResult();
            },purchase_params);
        }
    });
}

function deviceCheckSearchData(map){
    var cxtj=$("#deviceCheck_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#deviceCheck_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["wlbm"]=cxnr
    }else if(cxtj=="1"){
        map["wlmc"]=cxnr
    }else if(cxtj=="2"){
        map["sqrmc"]=cxnr
    }else if(cxtj=="3"){
        map["sqbmmc"]=cxnr
    }else if(cxtj=="4"){
        map["htnbbh"]=cxnr
    }else if(cxtj=="5"){
        map["djh"]=cxnr
    }else if(cxtj=="6"){
        map["gdzcbh"]=cxnr
    }else if(cxtj=="7"){
        map["sbccbh"]=cxnr
    }else if(cxtj=="8"){
        map["sydd"]=cxnr
    }else if(cxtj=="9"){
        map["scs"]=cxnr
    }else if(cxtj=="10"){
        map["gysmc"]=cxnr
    }else if(cxtj=="11"){
        map["dhdh"]=cxnr
    }
    var zxdnrs=jQuery('#deviceCheck_formSearch #zxdnr_id_tj').val();
    map["zxdnrs"] = zxdnrs.replace(/'/g, "");
    var sbcss=jQuery('#deviceCheck_formSearch #sbcs_id_tj').val();
    map["sbcss"] = sbcss.replace(/'/g, "");
    var yqgnyzs=jQuery('#deviceCheck_formSearch #yqgnyz_id_tj').val();
    map["yqgnyzs"] = yqgnyzs.replace(/'/g, "");
    var sjcss=jQuery('#deviceCheck_formSearch #sjcs_id_tj').val();
    map["sjcss"] = sjcss.replace(/'/g, "");
    var sjccs=jQuery('#deviceCheck_formSearch #sjcc_id_tj').val();
    map["sjccs"] = sjccs.replace(/'/g, "");
    var sjwzxhbmxs=jQuery('#deviceCheck_formSearch #sjwzxhbmx_id_tj').val();
    map["sjwzxhbmxs"] = sjwzxhbmxs.replace(/'/g, "");
    var sjclhjss=jQuery('#deviceCheck_formSearch #sjclhjs_id_tj').val();
    map["sjclhjss"] = sjclhjss.replace(/'/g, "");
    var xnyzs=jQuery('#deviceCheck_formSearch #xnyz_id_tj').val();
    map["xnyzs"] = xnyzs.replace(/'/g, "");
    var ysjls=jQuery('#deviceCheck_formSearch #ysjl_id_tj').val();
    map["ysjls"] = ysjls.replace(/'/g, "");
    var sfyzs=jQuery('#deviceCheck_formSearch #sfyz_id_tj').val();
    map["sfyzs"] = sfyzs.replace(/'/g, "");
    var sfjls=jQuery('#deviceCheck_formSearch #sfjl_id_tj').val();
    map["sfjls"] = sfjls.replace(/'/g, "");

    var jlzqstart = jQuery('#deviceCheck_formSearch #jlzqstart').val();
    map["jlzqstart"] = jlzqstart;
    var jlzqend = jQuery('#deviceCheck_formSearch #jlzqend').val();
    map["jlzqend"] = jlzqend;

    var yzzqstart = jQuery('#deviceCheck_formSearch #yzzqstart').val();
    map["yzzqstart"] = yzzqstart;
    var yzzqend = jQuery('#deviceCheck_formSearch #yzzqend').val();
    map["yzzqend"] = yzzqend;

    var byzqstart = jQuery('#deviceCheck_formSearch #byzqstart').val();
    map["byzqstart"] = byzqstart;
    var byzqend = jQuery('#deviceCheck_formSearch #byzqend').val();
    map["byzqend"] = byzqend;

    var xcbysjstart = jQuery('#deviceCheck_formSearch #xcbysjstart').val();
    map["xcbysjstart"] = xcbysjstart;

    var xcbysjend = jQuery('#deviceCheck_formSearch #xcbysjend').val();
    map["xcbysjend"] = xcbysjend;

    var xcjlsjstart = jQuery('#deviceCheck_formSearch #xcjlsjstart').val();
    map["xcjlsjstart"] = xcjlsjstart;
    var xcjlsjend = jQuery('#deviceCheck_formSearch #xcjlsjend').val();
    map["xcjlsjend"] = xcjlsjend;

    var xcyzsjstart = jQuery('#deviceCheck_formSearch #xcyzsjstart').val();
    map["xcyzsjstart"] = xcyzsjstart;
    var xcyzsjend = jQuery('#deviceCheck_formSearch #xcyzsjend').val();
    map["xcyzsjend"] = xcyzsjend;

    // 发票类型
    var ssyfxms = jQuery('#deviceCheck_formSearch #ssyfxm_id_tj').val();
    map["ssyfxms"] = ssyfxms;
    // 发票种类
    var ysjgs = jQuery('#deviceCheck_formSearch #ysjg_id_tj').val();
    map["ysjgs"] = ysjgs;
    // 状态
    var zts = jQuery('#deviceCheck_formSearch #zt_id_tj').val();
    map["zts"] = zts;
    // 到货开始日期
    var dhrqstart = jQuery('#deviceCheck_formSearch #dhrqstart').val();
    map["dhrqstart"] = dhrqstart;
    // 到货结束日期
    var dhrqend = jQuery('#deviceCheck_formSearch #dhrqend').val();
    map["dhrqend"] = dhrqend;
    // 验收开始日期
    var ysrqstart = jQuery('#deviceCheck_formSearch #ysrqstart').val();
    map["ysrqstart"] = ysrqstart;
    // 验收结束日期
    var ysrqend = jQuery('#deviceCheck_formSearch #ysrqend').val();
    map["ysrqend"] = ysrqend;
    return map;
}

function DeviceCheckDealById(id,action,tourl,sfgdzc){
    if(!tourl){
        return;
    }
    tourl = $("#deviceCheck_formSearch #urlPrefix").val()+tourl;
    if(action =='view'){
        var url= tourl + "?hwid=" +id;
        $.showDialog(url,'详细信息',viewDeviceCheckConfig);
    }else if(action == 'deviceUphold'){
        var url= tourl+ "?hwid=" +id+"&sfgdzc="+sfgdzc;
        $.showDialog(url,'设备维护',editSbwhDeviceCheckConfig);
    }else if(action == 'mod'){
        var url= tourl + "?hwid=" +id;
        $.showDialog(url,'修改',editDeviceCheckConfig);
    }else if(action == 'submit'){
        var url= tourl + "?hwid=" +id;
        $.showDialog(url,'提交',submitDeviceCheckConfig);
    }
}
var editSbwhDeviceCheckConfig = {
    width		: "1600px",
    modalName	: "editDeviceCheckModel",
    formName	: "editDeviceCheckForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if (!$("#editDeviceCheckForm").valid()) {
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};

                $("#editDeviceCheckForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"editDeviceCheckForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }

                            // searchDeviceCheckResult();
                            //提交审核
                            if(responseText["auditType"]!=null){
                                var deviceCheck_params=[];
                                deviceCheck_params.prefix=responseText["urlPrefix"];
                                showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){
                                    $.closeModal(opts.modalName);
                                    searchDeviceCheckResult();
                                },null,deviceCheck_params);
                            }else{
                                searchDeviceCheckResult();
                            }
                        });
                    }else if(responseText["status"] == "repetition"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error("固定资产编号或设备编号重复！",function() {
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
var editDeviceCheckConfig = {
    width		: "1600px",
    modalName	: "editDeviceCheckModel",
    formName	: "editDeviceCheckForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if (!$("#editDeviceCheckForm").valid()) {
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};

                $("#editDeviceCheckForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"editDeviceCheckForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchDeviceCheckResult();
                            }
                        });
                    }else if(responseText["status"] == "repetition"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error("固定资产编号或设备编号重复！",function() {
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

var submitDeviceCheckConfig = {
    width		: "1600px",
    modalName	: "editDeviceCheckModel",
    formName	: "editDeviceCheckForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if (!$("#editDeviceCheckForm").valid()) {
                    $.alert("请填写完整信息");
                    return false;
                }

                var $this = this;
                var opts = $this["options"]||{};

                $("#editDeviceCheckForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"editDeviceCheckForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            //提交审核
                            if(responseText["auditType"]!=null){
                                var deviceCheck_params=[];
                                deviceCheck_params.prefix=responseText["urlPrefix"];
                                showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){
                                    $.closeModal(opts.modalName);
                                    searchDeviceCheckResult();
                                },null,deviceCheck_params);
                            }else{
                                searchDeviceCheckResult();
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

/**
 * 查看页面模态框
 */
var viewDeviceCheckConfig={
    width		: "1400px",
    modalName	:"viewDeviceCheckModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
}

var DeviceCheck_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        var btn_query=$("#deviceCheck_formSearch #btn_query");
        var btn_deviceUphold=$("#deviceCheck_formSearch #btn_deviceUphold");
        var btn_view=$("#deviceCheck_formSearch #btn_view");
        var btn_mod=$("#deviceCheck_formSearch #btn_mod");
        var btn_del=$("#deviceCheck_formSearch #btn_del");
        var btn_discard=$("#deviceCheck_formSearch #btn_discard");
        var btn_submit=$("#deviceCheck_formSearch #btn_submit");
        var btn_advancedmod=$("#deviceCheck_formSearch #btn_advancedmod");
        var btn_print=$("#deviceCheck_formSearch #btn_print");
        //添加日期控件
        laydate.render({
            elem: '#deviceCheck_formSearch #dhrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#deviceCheck_formSearch #dhrqend'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#deviceCheck_formSearch #ysrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#deviceCheck_formSearch #ysrqend'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#deviceCheck_formSearch #xcbysjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#deviceCheck_formSearch #xcbysjend'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#deviceCheck_formSearch #xcjlsjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#deviceCheck_formSearch #xcjlsjend'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#deviceCheck_formSearch #xcyzsjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#deviceCheck_formSearch #xcyzsjend'
            ,theme: '#2381E9'
        });
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                searchDeviceCheckResult(true);
            });
        }



        /* ---------------------------查看列表-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#deviceCheck_formSearch #deviceCheck_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                DeviceCheckDealById(sel_row[0].hwid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* ---------------------------修改-----------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#deviceCheck_formSearch #deviceCheck_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                DeviceCheckDealById(sel_row[0].hwid,"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* ---------------------------打印-----------------------------------*/
        btn_print.unbind("click").click(function(){
            var sel_row = $('#deviceCheck_formSearch #deviceCheck_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                var url=$('#deviceCheck_formSearch #urlPrefix').val()+btn_print.attr("tourl")+"?sbysid="+sel_row[0].sbysid+"&access_token="+$("#ac_tk").val();
                window.open(url);
            }else{
                $.error("请选中一行");
            }
        });
        /* ---------------------------提交-----------------------------------*/
        btn_submit.unbind("click").click(function(){
            var sel_row = $('#deviceCheck_formSearch #deviceCheck_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                if(sel_row[0].zt=='00'||sel_row[0].zt=='15'){
                    DeviceCheckDealById(sel_row[0].hwid,"submit",btn_submit.attr("tourl"));
                }else{
                    $.error("请选择状态为未提交或者未通过的记录！")
                    return;
                }

            }else{
                $.error("请选中一行");
            }
        });
        /* ------------------------------新增-----------------------------*/
        btn_deviceUphold.unbind("click").click(function(){
            var sel_row = $('#deviceCheck_formSearch #deviceCheck_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                if(sel_row[0].sbysid!=null&&sel_row[0].sbysid!=''){
                    $.error("请勿重复维护！");
                    return;
                }else{
                    DeviceCheckDealById(sel_row[0].hwid,"deviceUphold",btn_deviceUphold.attr("tourl"),sel_row[0].sfgdzc);
                }
            }else{
                $.error("请选中一行");
            }
        });
        /* ------------------------------删除-----------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#deviceCheck_formSearch #deviceCheck_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                if(sel_row[i].sbysid!=null&&sel_row[i].sbysid!=''){
                    if(sel_row[i].zt=='00'||sel_row[i].zt=='15'){
                        ids= ids + ","+ sel_row[i].sbysid;
                    }else{
                        $.error("您所选择的数据存在已提交的数据，无法删除！");
                        return;
                    }
                }
            }
            if(ids==''){
                $.error("您所选择的数据还未维护，无法删除！");
                return;
            }else{
                ids=ids.substr(1);
            }
            $.confirm('您确定要删除所选择的记录吗？',function(result){
                if(result){
                    jQuery.ajaxSetup({async:false});
                    var url=  $('#deviceCheck_formSearch #urlPrefix').val() + btn_del.attr("tourl");
                    jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                        setTimeout(function(){
                            if(responseText["status"] == 'success'){
                                $.success(responseText["message"],function() {
                                    searchDeviceCheckResult();
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
        });
        /* ------------------------------废弃-----------------------------*/
        btn_discard.unbind("click").click(function(){
            var sel_row = $('#deviceCheck_formSearch #deviceCheck_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    if(sel_row[i].sbysid!=null&&sel_row[i].sbysid!=''){
                        if(sel_row[i].zt=='00'||sel_row[i].zt=='15'){
                            ids= ids + ","+ sel_row[i].sbysid;
                        }else{
                            $.error("您所选择的数据存在已提交的数据，无法废弃！");
                            return;
                        }

                    }
                }
                if(ids==''){
                    $.error("您所选择的数据还未维护，无需废弃！");
                    return;
                }else{
                    ids=ids.substr(1);
                }
                $.confirm('您确定要废弃所选择的信息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= $('#deviceCheck_formSearch #urlPrefix').val() + btn_discard.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchDeviceCheckResult();
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
        /* ------------------------------高级修改-----------------------------*/
        btn_advancedmod.unbind("click").click(function(){
            var sel_row = $('#deviceCheck_formSearch #deviceCheck_list').bootstrapTable('getSelections');//获取选择行数据
            var hwid="";
            var sbysid="";
            var ysjg="";
            var yssl="";
            if(sel_row.length==1){
                if(sel_row[0].zt=='80'){
                    hwid=sel_row[0].hwid;
                    sbysid=sel_row[0].sbysid;
                    ysjg=sel_row[0].ysjg;
                    yssl=sel_row[0].yssl;
                }else{
                    $.error("请选择状态为审核通过的记录！")
                    return;
                }
            }else{
                $.error("请选中一行");
                return;
            }
            $.confirm('您确定要修改所选择的验收结果吗？',function(result){
                if(result){
                    jQuery.ajaxSetup({async:false});
                    var url=  $('#deviceCheck_formSearch #urlPrefix').val() + btn_advancedmod.attr("tourl");
                    jQuery.post(url,{hwid:hwid,sbysid:sbysid,ysjg:ysjg,yssl:yssl,"access_token":$("#ac_tk").val()},function(responseText){
                        setTimeout(function(){
                            if(responseText["status"] == 'success'){
                                $.success(responseText["message"],function() {
                                    searchDeviceCheckResult();
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
        });
        /**显示隐藏**/
        $("#deviceCheck_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(DeviceCheck_turnOff){
                $("#deviceCheck_formSearch #searchMore").slideDown("low");
                DeviceCheck_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#deviceCheck_formSearch #searchMore").slideUp("low");
                DeviceCheck_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });

    };
    return oInit;
};


function searchDeviceCheckResult(isTurnBack){
    //关闭高级搜索条件
    $("#deviceCheck_formSearch #searchMore").slideUp("low");
    DeviceCheck_turnOff=true;
    $("#deviceCheck_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#deviceCheck_formSearch #deviceCheck_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#deviceCheck_formSearch #deviceCheck_list').bootstrapTable('refresh');
    }
}

$(function(){

    // 1.初始化Table
    var oTable = new DeviceCheck_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new DeviceCheck_ButtonInit();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#deviceCheck_formSearch .chosen-select').chosen({width: '100%'});

    $("#deviceCheck_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });

});