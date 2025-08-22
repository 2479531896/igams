var xsid='';
var saleDetails_turnOff=true;
var saleDetails_TableInit = function () {
    var oTableInit = new Object();
    // 初始化Table
    oTableInit.Init = function () {
        $('#saleDetails_formSearch #saleDetails_list').bootstrapTable({
            url: $("#saleDetails_formSearch #urlPrefix").val()+'/storehouse/sale/pageGetListSalesDetails',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#saleDetails_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"tmp.ddrq",					// 排序字段
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
            uniqueId: "zjid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮                 								
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '0.5%'
            },{
                title: '序号',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '2%',
                align: 'left',
                visible:true
            },{
                field: 'zjid',
                title: '主键id',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'khlxmc',
                title: '客户类型',
                width: '4%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'xslxmc',
                title: '销售类型',
                width: '4%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'ddh',
                title: '订单号',
                width: '4.5%',
                align: 'left',
                sortable: true,
                visible: true,
                formatter:ddhformat,
            },{
                field: 'yddh',
                title: '原订单号',
                width: '4.5%',
                align: 'left',
                sortable: true,
                visible: false,
                formatter:yddhformat,
            },{
                field: 'u8dh',
                title: 'u8单号',
                width: '4.5%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'yu8dh',
                title: '原u8单号',
                width: '4.5%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'ddrq',
                title: '订单日期',
                width: '3.5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'fhr',
                title: '发货日',
                width: '3.5%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'khmc',
                title: '客户名称',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true,
                formatter:khmcformat,
            },{
                field: 'fhrmc',
                title: '发货人',
                width: '2.5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'ywy',
                title: '归属人',
                width: '2.5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'bm',
                title: '部门',
                width: '4%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'fzdq',
                title: '负责大区',
                width: '4%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'wlbm',
                title: '物料编码',
                width: '3%',
                align: 'left',
                sortable: true,
                formatter:wlbmformat,
                visible: true
            },{
                field: 'cplxmc',
                title: '产品类型',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'zdqy',
                title: '终端区域',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'ysfsmc',
                title: '运输方式',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'zd',
                title: '终端',
                width: '7%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'djlxmc',
                title: '单据类型',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'khjc',
                title: '客户简称',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'wlmc',
                title: '物料名称',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'gg',
                title: '规格型号',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'jldw',
                title: '单位',
                width: '2%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'ychh',
                title: '货号',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'sl',
                title: '订单数量',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'fhsl',
                title: '发货数量',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'scph',
                title: '生产批号',
                width: '4%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'dj',
                title: '单价',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'jsze',
                title: '价税总额',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'htbh',
                title: '合同编号',
                width: '4%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'hsdj',
                title: '含税单价',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'zk',
                title: '折扣',
                width: '2%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'zsyy',
                title: '赠送原因',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'bz',
                title: '备注',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'fhr',
                title: '发货日',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'scrq',
                title: '生产日期',
                width: '4%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'yxq',
                title: '有效期',
                width: '4%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'dky',
                title: '到款月',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'fph',
                title: '发票号',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'dkje',
                title: '到款金额',
                width: '3%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'khlxr',
                title: '客户联系人',
                width: '3.5%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'khlxfs',
                title: '客户联系方式',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'shdz',
                title: '收货地址',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'shlxfs',
                title: '收货联系方式',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'kdxx',
                title: '快递信息',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                saleDetailsById(row.zjid,'view',$("#saleDetails_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#saleDetails_formSearch #saleDetails_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true
        })
    }
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "tmp.ddh", // 防止同名排位用
            sortLastOrder: "DESC" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return saleDetailsSearchData(map);
    };
    return oTableInit
};
// 根据查询条件查询
function saleDetailsSearchData(map){
    var cxtj=$("#saleDetails_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#saleDetails_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["ddh"]=cxnr;
    }else if(cxtj=="1"){
        map["bm"]=cxnr;
    }else if(cxtj=="2"){
        map["khmc"]=cxnr;
    }else if(cxtj=="3"){
        map["khjc"]=cxnr;
    }
    else if(cxtj=="4"){
        map["htbh"]=cxnr;
    }
    else if(cxtj=="5"){
        map["zd"]=cxnr;
    }
    else if(cxtj=="6"){
        map["zdqy"]=cxnr;
    }else if(cxtj=="7"){
        map["fzdq"]=cxnr;
    }else if(cxtj=="8"){
        map["ywy"]=cxnr;
    }else if(cxtj=="9"){
        map["wlbm"]=cxnr;
    }else if(cxtj=="10"){
        map["wlmc"]=cxnr;
    }else if(cxtj=="11"){
        map["gg"]=cxnr;
    }else if(cxtj=="12"){
        map["scph"]=cxnr;
    }else if(cxtj=="13"){
        map["khlxr"]=cxnr;
    }else if(cxtj=="14"){
        map["khlxfs"]=cxnr;
    }else if(cxtj=="15"){
        map["shdz"]=cxnr;
    }else if(cxtj=="16"){
        map["kdxx"]=cxnr;
    }else if(cxtj=="17"){
        map["shlxfs"]=cxnr;
    }else if(cxtj=="18"){
        map["entire"]=cxnr;
    }else if(cxtj=="19"){
        map["yddh"]=cxnr;
    }
    // 订单开始日期
    var ddrqstart = jQuery('#saleDetails_formSearch #ddrqstart').val();
    map["ddrqstart"] = ddrqstart;
    // 订单结束日期
    var ddrqend = jQuery('#saleDetails_formSearch #ddrqend').val();
    map["ddrqend"] = ddrqend;
    // 有效期开始日期
    var yxqstart = jQuery('#saleDetails_formSearch #yxqstart').val();
    map["yxqstart"] = yxqstart;
    // 有效期结束日期
    var yxqend = jQuery('#saleDetails_formSearch #yxqend').val();
    map["yxqend"] = yxqend;
    //客户类别
    var khlbs = jQuery('#saleDetails_formSearch #khlb_id_tj').val();
    map["khlbs"] = khlbs.replace(/'/g, "");
    //是否签收
    var cplxs = jQuery('#saleDetails_formSearch #cplx_id_tj').val();
    map["cplxs"] = cplxs.replace(/'/g, "");
    //是否确认
    var xslxs = jQuery('#saleDetails_formSearch #xslx_id_tj').val();
    map["xslxs"] = xslxs.replace(/'/g, "");
    var djlxs = jQuery('#saleDetails_formSearch #djlx_id_tj').val();
    map["djlxs"] = djlxs.replace(/'/g, "");
    return map;
}

//提供给导出用的回调函数
function SaleDetailsSearchData(){
    var map ={};
    map["access_token"]=$("#ac_tk").val();
    map["sortLastName"]="tmp.zjid";
    map["sortLastOrder"]="desc";
    map["sortName"]="tmp.ddh";
    map["sortOrder"]="desc";
    return saleDetailsSearchData(map);
}
//提供给导出用的回调函数
function SaleDetailsReportSearchData(){
    var map ={};
    map["access_token"]=$("#ac_tk").val();
    map["sortName"]="tmp_t.wlbm desc,tmp_t.wlmc desc,tmp_t.sl";
    map["sortOrder"]="desc";
    return saleDetailsSearchData(map);
}

function saleDetailsById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $("#saleDetails_formSearch #urlPrefix").val()+tourl;
    if(action =='view'){
        var url= tourl + "?zjid=" +id;
        $.showDialog(url,'销售明细',viewsaleDetailsConfig);
    }
}
/**
 * 物料编码格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function wlbmformat(value,row,index){
    var html = "";
    html += "<a href='javascript:void(0);' onclick=\"queryByFirstWlbm('"+row.wlid+"')\">"+row.wlbm+"</a>";
    return html;
}
function queryByFirstWlbm(wlid){
    var url=$("#saleDetails_formSearch #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'物料信息',viewWlConfig);
}
var viewWlConfig = {
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
 * 订单号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function ddhformat(value,row,index){
    var html = "";
        if (row.viewid != null && row.viewid != '') {
            html += "<a href='javascript:void(0);' onclick=\"queryByid('" + row.viewid+ "', '" + row.djlxmc+  "')\">" + row.ddh + "</a>";
        }

        else html=row.ddh
    return html;
}
function queryByid(id,djlxmc){
    if (djlxmc=='退货单'){
    var url=$("#saleDetails_formSearch #urlPrefix").val()+"/storehouse/returnManagement/viewReturnManagement?thid="+id+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'退还单信息',viewDdhConfig);
    }else if (djlxmc=='归还单'){
        var url=$("#saleDetails_formSearch #urlPrefix").val()+"/repaid/repaid/viewRepaid?jcghid="+id+"&access_token=" + $("#ac_tk").val();
        $.showDialog(url,'归还单信息',viewDdhConfig);
    }else if (djlxmc=='领料单'){
        var url=$("#saleDetails_formSearch #urlPrefix").val()+"/storehouse/receiveMateriel/viewReceiveMateriel?llid="+id+"&access_token=" + $("#ac_tk").val();
        $.showDialog(url,'领料单信息',viewDdhConfig);
    }else if (djlxmc=='借出换货单'||djlxmc=='借出单'){
        var url=$("#saleDetails_formSearch #urlPrefix").val()+"/borrowing/borrowing/viewBorrowing?jcjyid="+id+"&access_token=" + $("#ac_tk").val();
        $.showDialog(url,'借出单信息',viewDdhConfig);
    }else if (djlxmc=='销售换货单'||djlxmc=='借出转销售单'||djlxmc=='销售单'){
        var url=$("#saleDetails_formSearch #urlPrefix").val()+"/storehouse/sale/viewSale?xsid="+id+"&access_token=" + $("#ac_tk").val();
        $.showDialog(url,'销售单信息',viewDdhConfig);
    }
}

var viewDdhConfig = {
    width		: "1600",
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
 * 原订单号格式化
 */
function yddhformat(value,row,index){
    var html = "";
    if (row.yviewid != null && row.yviewid != '') {
        html += "<a href='javascript:void(0);' onclick=\"queryByYViewid('" + row.yviewid+ "', '" + row.djlxmc+ "', '" + row.vewid+ "')\">" + row.yddh + "</a>";
    }
    else html=row.yddh
    return html;
}
function queryByYViewid(id,djlxmc,vewid){
    if (djlxmc=='退货单'){
        var url=$("#saleDetails_formSearch #urlPrefix").val()+"/storehouse/sale/viewSale?xsid="+id+"&access_token=" + $("#ac_tk").val();
        $.showDialog(url,'销售单信息',viewDdhConfig);
    }else if (djlxmc=='归还单'){
        var url=$("#saleDetails_formSearch #urlPrefix").val()+"/borrowing/borrowing/viewBorrowing?jcjyid="+id+"&access_token=" + $("#ac_tk").val();
        $.showDialog(url,'借出单信息',viewDdhConfig);
    }else if (djlxmc=='领料单'){
        if (id !=vewid){
            var url=$("#saleDetails_formSearch #urlPrefix").val()+"/warehouse/outDepot/pagedataOutbound?ckid="+id+"&access_token=" + $("#ac_tk").val();
            $.showDialog(url,'出库单信息',viewDdhConfig);
        }else{
            var url=$("#saleDetails_formSearch #urlPrefix").val()+"/storehouse/receiveMateriel/viewReceiveMateriel?llid="+id+"&access_token=" + $("#ac_tk").val();
            $.showDialog(url,'领料单信息',viewDdhConfig);
        }
    }else if (djlxmc=='借出换货单'||djlxmc=='借出单'){
        var url=$("#saleDetails_formSearch #urlPrefix").val()+"/borrowing/borrowing/viewBorrowing?jcjyid="+id+"&access_token=" + $("#ac_tk").val();
        $.showDialog(url,'借出单信息',viewDdhConfig);
    }else if (djlxmc=='销售换货单'||djlxmc=='借出转销售单'||djlxmc=='销售单'){
        if (id==vewid){
            var url=$("#saleDetails_formSearch #urlPrefix").val()+"/storehouse/sale/viewSale?xsid="+id+"&access_token=" + $("#ac_tk").val();
            $.showDialog(url,'销售单信息',viewDdhConfig);
        }else {
            var url=$("#saleDetails_formSearch #urlPrefix").val()+"/borrowing/borrowing/viewBorrowing?jcjyid="+id+"&access_token=" + $("#ac_tk").val();
            $.showDialog(url,'借出单信息',viewDdhConfig);
        }

    }
}

/**
 * 物料编码格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function khmcformat(value,row,index){
    var html = "";
    if(row.khmc !=null&&row.khmc!=''){
        html += "<a href='javascript:void(0);' onclick=\"queryBykhid('"+row.khid+"')\">"+row.khmc+"</a>";
    }
    return html;
}
function queryBykhid(khid){
    var url=$("#saleDetails_formSearch #urlPrefix").val()+"/storehouse/khgl/viewKhgl?khid="+khid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'客户信息',viewKhConfig);
}
var viewKhConfig = {
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



var viewsaleDetailsConfig = {
    width		: "1600px",
    modalName	: "viewsaleDetailsModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};




var saleDetails_ButtonInit = function(){
    var oInit = new Object();

    oInit.Init = function () {
        var btn_view = $("#saleDetails_formSearch #btn_view");
        var btn_query = $("#saleDetails_formSearch #btn_query");
        var btn_searchexport = $("#saleDetails_formSearch #btn_searchexport");
        var btn_selectexport = $("#saleDetails_formSearch #btn_selectexport");
        var btn_reportexport = $("#saleDetails_formSearch #btn_reportexport");
        //添加日期控件
        laydate.render({
            elem: '#saleDetails_formSearch #ddrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#saleDetails_formSearch #ddrqend'
            ,theme: '#2381E9'
        });
        laydate.render({
            elem: '#saleDetails_formSearch #yxqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#saleDetails_formSearch #yxqend'
            ,theme: '#2381E9'
        });
        /*--------------------------------模糊查询---------------------------*/
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                saleDetailsResult(true);
            });
        }

        /* ---------------------------销售列表查看-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#saleDetails_formSearch #saleDetails_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                saleDetailsById(sel_row[0].zjid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*-----------------------显示隐藏------------------------------------*/
        $("#saleDetails_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(saleDetails_turnOff){
                $("#saleDetails_formSearch #searchMore").slideDown("low");
                saleDetails_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#saleDetails_formSearch #searchMore").slideUp("low");
                saleDetails_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
            //showMore();
        });
        // 	  ---------------------------导出-----------------------------------
        btn_selectexport.unbind("click").click(function(){
            var sel_row = $('#saleDetails_formSearch #saleDetails_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length>=1){
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].zjid;
                }
                ids = ids.substr(1);
                $.showDialog($('#saleDetails_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=XSMX_SELECT&expType=select&ids="+ids
                    ,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
            }else{
                $.error("请选择数据");
            }
        });

        btn_reportexport.unbind("click").click(function(){
            var ddrqstart=$("#saleDetails_formSearch #ddrqstart").val();
            var ddrqend=$("#saleDetails_formSearch #ddrqend").val();
            if (ddrqstart!=null&&ddrqstart!=''&&ddrqend!=null&&ddrqend!=''){
                $.showDialog($('#saleDetails_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=XSMX_REPORTSEARCH&expType=search&callbackJs=SaleDetailsReportSearchData"
                    ,btn_reportexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
            }else {
                $.error("请输入订单日期的筛选条件");
            }
        });//报表导出
        btn_searchexport.unbind("click").click(function(){
            $.showDialog($('#saleDetails_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=XSMX_SEARCH&expType=search&callbackJs=SaleDetailsSearchData"
                ,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
        });

    };
    return oInit;
};


function saleDetailsResult(isTurnBack){
    if(isTurnBack){
        $('#saleDetails_formSearch #saleDetails_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#saleDetails_formSearch #saleDetails_list').bootstrapTable('refresh');
    }
}

$(function(){
    // 1.初始化Table
    var oTable = new saleDetails_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new saleDetails_ButtonInit();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#saleDetails_formSearch .chosen-select').chosen({width: '100%'});

});