var Inspection_turnOff = true;
var sfzjezdqx = 0;
var tkzjezdqx = 0;
var Inspection_TableInit = function (fieldList, firstFlg) {
    var oTableInit = new Object();
    $('#tab_030101').on('scroll', function () {
        if ($('#yxSjxx_formSearch #sjxx_list').offset() && $('#yxSjxx_formSearch #sjxx_list').offset() != "null") {
            if ($('#yxSjxx_formSearch #sjxx_list').offset().top - 122 < 0) {
                if (!$('#yxSjxx_formSearch .js-affix').hasClass("affix")) {
                    $('#yxSjxx_formSearch .js-affix').removeClass("affix-top").addClass("affix");
                }
                $('#yxSjxx_formSearch .js-affix').css({
                    'top': '100px',
                    "z-index": 1000,
                    "width": '100%'
                });
            } else {
                if (!$('#yxSjxx_formSearch .js-affix').hasClass("affix-top")) {
                    $('#yxSjxx_formSearch .js-affix').removeClass("affix").addClass("affix-top");
                }
                $('#yxSjxx_formSearch .js-affix').css({
                    'top': '0px',
                    "z-index": 1,
                    "width": '100%'
                });
            }
        }

    })
    //初始化Table
    oTableInit.Init = function () {
        $('#yxSjxx_formSearch #sjxx_list').bootstrapTable({
            url: '/marketingInspection/marketingInspection/pageGetListMarketInspection',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#yxSjxx_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "jsrq desc,sfjs desc,bgrq desc,lrsj ",//排序字段
            sortOrder: "DESC",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber: 1,                       //初始化加载第一页，默认第一页
            pageSize: 50,                       //每页的记录行数（*）
            pageList: [15, 30, 50, 100],        //可供选择的每页的行数（*）
            paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "sjid",                     //每一行的唯一标识，一般为主键列
            showToggle: true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: fieldList,
            rowStyle: function (row, index) {
                if (row.fjid != null && row.fjid != '' && row.fjid != undefined) {
                    return {css: {"background-color": '#fff6a4'}};
                }
                return '';
            },
            onLoadSuccess: function (map) {
                $("#yxSjxx_formSearch #sfzje").text(map.sfzje)
                $("#yxSjxx_formSearch #tkzje").text(map.tkzje)
            },
            onLoadError: function () {

            },
            onDblClickRow: function (row, $element) {
                MarketInspectionDealById(row.sjid, row.fjid, row.xmglid,'view', $("#yxSjxx_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#yxSjxx_formSearch #sjxx_list").colResizable({
                liveDrag: true,
                gripInnerHtml: "<div class='grip'></div>",
                draggingClass: "dragging",
                resizeMode: 'fit',
                postbackSafe: true,
                partialRefresh: true,
                isFirst: firstFlg
            }
        );
    };
    // 得到查询的参数
    oTableInit.queryParams = function (params) {
        // 请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
        // 例如 toolbar 中的参数
        // 如果 queryParamsType = ‘limit’ ,返回参数必须包含
        // limit, offset, search, sort, order
        // 否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder.
        // 返回false将会终止请求
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token: $("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "sjid", // 防止同名排位用
            sortLastOrder: "asc", // 防止同名排位用
            limitColumns: $("#limitColumns").val(),
            sfzjezdqx: sfzjezdqx,
            tkzjezdqx: tkzjezdqx
        };
        return getSjxxSearchData(map);
    };
    return oTableInit;
}

//查询条件
function getSjxxSearchData(map) {
    var cxtj = $("#yxSjxx_formSearch #cxtj").val();
    var cxnr = $.trim(jQuery('#yxSjxx_formSearch #cxnr').val());
    if (cxtj == "0") {
        map["hzxm"] = cxnr;//患者姓名
    } else if (cxtj == "1") {
        map["db"] = cxnr;//合作伙伴
    } else if (cxtj == "2") {
        map["hospitalname"] = cxnr;//送检单位
    } else if (cxtj == "3") {
        map["cskz5"] = cxnr;//发票号码
    } else if (cxtj == "4") {
        map["ybbh"] = cxnr;//标本编号
    } else if (cxtj == "5") {
        map["nbbm"] = cxnr;//内部编码
    } else if (cxtj == "6") {
        map["sjys"] = cxnr;//送检医生
    } else if (cxtj == "7") {
        map["yytjmc"] = cxnr;//医院统计名称
    } else if (cxtj == "8") {
        map["dcbj"] = cxnr;//导出字段
    } else if (cxtj == "9") {
        map["qtks"] = cxnr;//导出字段
    }else if (cxtj == "10") {
        map["tjmc"] = cxnr;//导出字段
    }else if (cxtj == "11") {
         map["yhmc"] = cxnr;//导出字段
    }
    var cxtj1 = $("#yxSjxx_formSearch #cxtj1").val();
    var cxnr1 = $.trim(jQuery('#yxSjxx_formSearch #cxnr1').val());
    if (cxtj1 == "0") {
        map["hzxm"] = cxnr1;//患者姓名
    } else if (cxtj1 == "1") {
        map["db"] = cxnr1;//合作伙伴
    } else if (cxtj1 == "2") {
        map["hospitalname"] = cxnr1;//送检单位
    } else if (cxtj1 == "3") {
        map["cskz5"] = cxnr1;//发票号码
    } else if (cxtj1 == "4") {
        map["ybbh"] = cxnr1;//标本编号
    } else if (cxtj1 == "5") {
        map["nbbm"] = cxnr1;//内部编码
    } else if (cxtj1 == "6") {
        map["sjys"] = cxnr1;//送检医生
    } else if (cxtj1 == "7") {
        map["yytjmc"] = cxnr1;//医院统计名称
    } else if (cxtj1 == "8") {
        map["dcbj"] = cxnr1;//导出字段
    } else if (cxtj1 == "9") {
        map["qtks"] = cxnr1;//导出字段
    }else if (cxtj1 == "10"){
        map["tjmc"] = cxnr1;//导出字段
    }else if (cxtj1 == "11") {
        map["yhmc"] = cxnr1;//导出字段
    }
    var cxtj2 = $("#yxSjxx_formSearch #cxtj2").val();
    var cxnr2 = $.trim(jQuery('#yxSjxx_formSearch #cxnr2').val());
    if (cxtj2 == "0") {
        map["hzxm"] = cxnr2;//患者姓名
    } else if (cxtj2 == "1") {
        map["db"] = cxnr2;//合作伙伴
    } else if (cxtj2 == "2") {
        map["hospitalname"] = cxnr2;//送检单位
    } else if (cxtj2 == "3") {
        map["cskz5"] = cxnr2;//发票号码
    } else if (cxtj2 == "4") {
        map["ybbh"] = cxnr2;//标本编号
    } else if (cxtj2 == "5") {
        map["nbbm"] = cxnr2;//内部编码
    } else if (cxtj2 == "6") {
        map["sjys"] = cxnr2;//送检医生
    } else if (cxtj2 == "7") {
        map["yytjmc"] = cxnr2;//医院统计名称
    } else if (cxtj2 == "8") {
        map["dcbj"] = cxnr2;//导出字段
    } else if (cxtj2 == "9") {
        map["qtks"] = cxnr2;//导出字段
    }else if (cxtj2 == "10"){
         map["tjmc"] = cxnr2;//导出字段
    }else if (cxtj2 == "11") {
        map["yhmc"] = cxnr2;//导出字段
    }
    // 0.1、接收日期开始日期
    var jsrqstart = jQuery('#yxSjxx_formSearch #jsrqstart').val();
    map["jsrqstart"] = jsrqstart;
    // 0.2、接收日期结束日期
    var jsrqend = jQuery('#yxSjxx_formSearch #jsrqend').val();
    map["jsrqend"] = jsrqend;
    //1、送检科室
    var dwids = jQuery('#yxSjxx_formSearch #sjdw_id_tj').val();
    map["dwids"] = dwids;
    //2、标本类型
    var yblxs = jQuery('#yxSjxx_formSearch #yblx_id_tj').val();
    map["yblxs"] = yblxs;
    //3、项目性质
    var kylxs = $("#yxSjxx_formSearch #kylx_id_tj").val();
    map["kylxs"] = kylxs;
    //4、立项编号
    var kyxms = $("#yxSjxx_formSearch #kyxm_id_tj").val();
    map["kyxms"] = kyxms;
    //5、检测项目
    var jcxm = jQuery('#yxSjxx_formSearch #jcxm_id_tj').val()
    map["jcxms"] = jcxm;
    map["sj_jcxms"]= jcxm;
    //6、伙伴分类
    var sjhbfls = jQuery('#yxSjxx_formSearch #sjhbfl_id_tj').val();
    map["sjhbfls"] = sjhbfls;
    //6.1、伙伴子分类
    var sjhbzfls = jQuery('#yxSjxx_formSearch #sjhbzfl_id_tj').val();
    map["sjhbzfls"] = sjhbzfls;
    //7、报告结果
    var jyjgs = jQuery('#yxSjxx_formSearch #jyjg_id_tj').val()
    map["jyjgs"] = jyjgs;
    //8、是否付款
    var fkbj = jQuery('#yxSjxx_formSearch #fkbj_id_tj').val()
    map["fkbj"] = fkbj;
    //8、处理
    var clbj = jQuery('#yxSjxx_formSearch #clbj_id_tj').val()
    map["clbj"] = clbj;
    //8、处理
    var dcbjflg = jQuery('#yxSjxx_formSearch #dcbjflg_id_tj').val()
    map["dcbjflg"] = dcbjflg;
    //9、是否收费
    var sfsfs = jQuery('#yxSjxx_formSearch #sfsf_id_tj').val()
    map["sfsfs"] = sfsfs;
    //10、是否接收
    var sfjs = jQuery('#yxSjxx_formSearch #sfjs_id_tj').val()
    map["sfjs"] = sfjs;
    //11、是否开票
    var sjkzcs1 = jQuery('#yxSjxx_formSearch #cskz1_id_tj').val()
    map["sjkzcs1"] = sjkzcs1
    //12、是否上传
    var sfsc = jQuery('#yxSjxx_formSearch #sfsc_id_tj').val()
    map["sfsc"] = sfsc;
    //13、是否自免检测
    var sfzmjc = jQuery('#yxSjxx_formSearch #sfzmjc_id_tj').val()
    map["sfzmjc"] = sfzmjc;
    //14、是否到账
    var sjkzcs2 = jQuery('#yxSjxx_formSearch #cskz2_id_tj').val()
    map["sjkzcs2"] = sjkzcs2;
    //15、送检扩展四
    var sjkzcs4 = jQuery('#yxSjxx_formSearch #cskz4_id_tj').val()
    map["sjkzcs4"] = sjkzcs4
    //16、检测单位
    var jcdws = $("#yxSjxx_formSearch #jcdw_id_tj").val();
    map["jcdws"] = jcdws;
    //17、送检区分
    var sjqfs = $("#yxSjxx_formSearch #sjqf_id_tj").val();
    map["sjqfs"] = sjqfs;
    //18、重点等级
    var dwzddjs = $("#yxSjxx_formSearch #dwzddj_id_tj").val().replace("＋", "+");
    map["yyzddjs"] = dwzddjs;
    //19、重点等级
    var fjlxs = $("#yxSjxx_formSearch #fjlx_id_tj").val();
    map["fjlxs"] = fjlxs;
    //20.1、报告日期开始日期
    var bgrqstart = jQuery('#yxSjxx_formSearch #bgrqstart').val();
    map["bgrqstart"] = bgrqstart;
    //20.2、 报告日期结束日期
    var bgrqend = jQuery('#yxSjxx_formSearch #bgrqend').val();
    map["bgrqend"] = bgrqend;
    //21.1、付款日期开始时间
    var fkrqstart = jQuery('#yxSjxx_formSearch #fkrqstart').val();
    map["fkrqstart"] = fkrqstart;
    //21.2、付款日期结束时间
    var fkrqend = jQuery('#yxSjxx_formSearch #fkrqend').val();
    map["fkrqend"] = fkrqend;
    //22.1、实验日期开始日期
    var syrqstart = jQuery('#yxSjxx_formSearch #syrqstart').val();
    map["syrqstart"] = syrqstart;
    //22.2、实验日期结束日期
    var syrqend = jQuery('#yxSjxx_formSearch #syrqend').val();
    map["syrqend"] = syrqend;
    //平台归属
    var ptgss=$("#yxSjxx_formSearch #ptgs_id_tj").val().replace(/'/g, "");
    map["ptgss"]=ptgss;

    var xsbms=$("#yxSjxx_formSearch #xsbm_id_tj").val().replace(/'/g, "");
    map["xsbms"]=xsbms;
    //22.3、实验日期为空
    if (jQuery('#yxSjxx_formSearch #syrqflg').is(":checked")) {
        map["syrqflg"] = "1";
    }
    //是否对账
    var sfdz = jQuery('#yxSjxx_formSearch #sfdz_id_tj').val()
    map["sfdz"] = sfdz;
    return map;
}

//最终金额格式化
function zzjeformat(value, row, index) {
    if (row.sfje != null && row.sfje != '') {
        if (row.tkje != null && row.tkje != '') {
            return ((parseInt(row.sfje * 100) - parseInt(row.tkje * 100)) / 100).toFixed(2);
        } else {
            return row.sfje;
        }
    }
}

//性别格式化
function xbformat(value, row, index) {
    if (row.xb == '1') {
        return '男';
    } else if (row.xb == '2') {
        return '女';
    } else {
        return '未知';
    }
}
//是否对账格式化
function sfdzformat(value, row, index) {
    if (row.sfdz == '1') {
        return '是';
    } else {
        return '否';
    }
}
//RNA检测标记格式化
function jcbjformat(value, row, index) {
    if (row.jcxmkzcs == "D" || row.jcxmkzcs == "Z" || row.jcxmkzcs == "F") {
        var sfjc = "<span style='color:red;'>--</span>";
        return sfjc;
    } else {
        if (row.jcbj == '0' || row.jcbj == null) {
            var sfjc = "<span style='color:red;'>否</span>";
            return sfjc;
        } else if (row.jcbj == '1') {
            return '是';
        }
    }
}

//检验结果格式化
function jyjgformat(value, row, index) {
    if (row.jyjg == "1") {
        var jyjg = "<span style='color:red;font-weight:bold;'>阳性</span>";
        return jyjg;
    } else if (row.jyjg == "2") {
        var jyjg = "<span style='color:#0000cc;font-weight:bold;'>疑似</span>";
        return jyjg;
    } else if (row.jyjg == "0") {
        var jyjg = "<span style='font-weight:bold;'>阴性</span>";
        return jyjg;
    }
}

//DNA检测标记格式化
function djcbjformat(value, row, index) {
    if (row.jcxmkzcs == "R" || row.jcxmkzcs == "Z" || row.jcxmkzcs == "F") {
        var dsfjc = "<span style='color:red;'>--</span>";
        return dsfjc;
    } else {
        if (row.djcbj == '0' || row.djcbj == null) {
            var dsfjc = "<span style='color:red;'>否</span>";
            return dsfjc;
        } else if (row.djcbj == '1') {
            return '是';
        }
    }
}

//其他检测标记格式化
function qtjcbjformat(value, row, index) {
    if (row.jcxmkzcs != "Z" && row.jcxmkzcs != "F") {
        var qtsfjc = "<span style='color:red;'>--</span>";
        return qtsfjc;
    } else {
        if (row.qtjcbj == '0' || row.qtjcbj == null) {
            var qtsfjc = "<span style='color:red;'>否</span>";
            return qtsfjc;
        } else if (row.qtjcbj == '1') {
            return '是';
        }
    }
}

//是否自免检测标记格式化
function sfzmjcformat(value, row, index) {
    if (row.sfzmjc == "1") {
        var sfzmjc = "<span>是</span>"
        return sfzmjc;
    } else if (row.sfzmjc == '0' || row.sfzmjc == null) {
        var sfzmjc = "<span style='color:red;'>否</span>";
        return sfzmjc;
    }
}

//
function sfjsformat(value, row, index) {
    if (row.sfjs == '0') {
        var sfjs = "<span style='color:red;'>否</span>";
        return sfjs;
    } else if (row.sfjs == '1') {
        return '是';
    } else if (row.sfjs == '2') {
        return '其他(外送)';
    }
}

//是否vip标记格式化
function sfvipformat(value, row, index) {
    if (row.sfvip == '0') {
        var sfvip = "<span style='color:red;'>否</span>";
        return sfvip;
    } else if (row.sfvip == '1') {
        return '是';
    }
}

//是否统计标记格式化
function sftjformat(value, row, index) {
    if (row.sftj == '0') {
        var sftj = "<span style='color:red;'>否</span>";
        return sftj;
    } else if (row.sftj == '1') {
        return '是';
    }
}

//付款标记格式化
function fkbjformat(value, row, index) {
    if (row.fkbj == '1') {
        return '是';

    } else {
        return '否';
    }
}

//是否收費格式化
function sfsfformat(value, row, index) {
    if (row.sfsf == '1') {
        return '是';
    } else if (row.sfsf == '0') {
        var sfsf = "<span style='color:red;'>否</span>"
        return sfsf;
    } else if (row.sfsf == '2') {
        var sfsf = "<span style='color:#8E388E;font-weight:bold'>免            D</span>"
        return sfsf;
    } else if (row.sfsf == '3') {
        var sfsf = "<span style='color:#8470FF;font-weight:bold'>免         R</span>"
        return sfsf;
    }
}

//是否上传格式化
function wjformat(value, row, index) {
    if (row.sfsc == "1") {
        var sfsc = "<span>是</span>"
        return sfsc;
    } else {
        var sfsc = "<span style='color:red;'>否</span>"
        return sfsc;
    }
}

//反馈结果格式化
function lcfkformat(value, row, index) {
    if (row.sfzq == "1") {
        var sfzq = "<span>正确</span>"
        return sfzq;
    } else if (row.sfzq == "0") {
        var sfzq = "<span style='color:red;'>错误</span>"
        return sfzq;
    } else {
        var sfzq = "<span'>未反馈</span>"
        return sfzq;
    }
}

//处理标记
function clbjformat(value, row, index) {
    if (row.clbj == "待处理") {
        return "<span style='color: orange'>待处理</span>";
    } else if (row.clbj == "无需处理"){
        return "<span style='color: green'>无需处理</span>";
    }
}

//检测项目格式化
function jcxmformat(value, row, index) {
    /*if(row.jcxmkzcs=="D"){
        return null;
    }else{*/
    return row.jcxmmc;
    //}
}

//检测子项目格式化
function jczxmformat(value, row, index) {
    /*if(row.jcxmkzcs=="D"){
        return null;
    }else{*/
    return row.jczxmmc;
    //}
}

function ztqfformat(value, row, index) {
    if (row.ztqf) {
        if(row.ztqf=='0'){
            return "<span style='color:blue;'>存量</span>";
        }else if(row.ztqf=='1'){
            return "<span style='color:red;'>增量</span>";
        }else if(row.ztqf=='2'){
             return "<span style='color:black;'>其他</span>";
         }
    } else {
        return "";
    }
}

// 按钮动作函数
function MarketInspectionDealById(sjid, fjid, xmglid, action, tourl) {
    var url = tourl;
    if (!tourl) {
        return;
    }
    if (action == 'view') {
        var url = tourl + "?sjid=" + sjid + "&fjid=" + (fjid ? fjid : "");
        $.showDialog(url, '营销检测详细信息', viewSjxxConfig);
    } else if (action == 'mod') {
        var url = tourl + "?sjid=" + sjid + "&xg_flg=1" + "&fjid=" + (fjid ? fjid : "");
        $.showDialog(url, '修改送检信息', fjid ? modYxFjxxConfig : modYxSjxxConfig);
    } else if (action == 'payment') {
        var url = tourl + "?sjid=" + sjid;
        $.showDialog(url, '付款维护',  paymentConfig);
    } else if (action == 'advancedmod') {
        var url = tourl + "?sjid=" + sjid + "&fjid=" + (fjid ? fjid : "");
        $.showDialog(url, '高级修改营销检测信息', modYxSjxxConfig);
    } else if (action == 'extend') {
        var url = tourl + "?ids=" + sjid;
        $.showDialog(url, '修改送检信息', modkzcsConfig);
    } else if (action == "reportdownload") {
        var url = tourl + "?ids=" + sjid;
        $.showDialog(url, '报告下载', reportDownloadConfig);
    } else if (action == "pdfreportdownload") {
        var url = tourl + "?ids=" + sjid + "&flg=1";
        $.showDialog(url, 'PDF报告下载', reportDownloadConfig);
    } else if (action == "exportaccount") {
        var url = tourl ;
        $.showDialog(url, '导出对账单', exportAccountConfig);
    }else if(action =='add'){
        var url= tourl;
        $.showDialog(url,'新增送检信息',addYxSjxxConfig);
    }
}

var Inspection_ButtonInit = function () {
    var oInit = new Object();
    var postdata = {};
    oInit.Init = function () {
        //初始化页面上面的按钮事件
        var initTableField = $("#yxSjxx_formSearch #initTableField");//字段选择
        var btn_query = $("#yxSjxx_formSearch #btn_query");//模糊查询
        var btn_mod = $("#yxSjxx_formSearch #btn_mod");//修改
        var btn_payment = $("#yxSjxx_formSearch #btn_payment");//付款维护
        var btn_view = $("#yxSjxx_formSearch #btn_view");//查看详情
        var btn_selectexport = $("#yxSjxx_formSearch #btn_selectexport");//选中导出
        var btn_searchexport = $("#yxSjxx_formSearch #btn_searchexport");//搜索导出
        var btn_advancedmod = $("#yxSjxx_formSearch #btn_advancedmod");//高级修改
        var btn_extend = $("#yxSjxx_formSearch #btn_extend");//扩展修改
        var btn_amountselectexport = $("#yxSjxx_formSearch #btn_amountselectexport");//选中导出
        var btn_amountsearchexport = $("#yxSjxx_formSearch #btn_amountsearchexport");//搜索导出
        var btn_reportdownload = $("#yxSjxx_formSearch #btn_reportdownload");//报告下载
        var btn_pdfreportdownload = $("#yxSjxx_formSearch #btn_pdfreportdownload");//报告下载
        var btn_exportaccount = $("#yxSjxx_formSearch #btn_exportaccount");//导出对账单
	var btn_add = $("#yxSjxx_formSearch #btn_add");//新增

        //添加日期控件
        laydate.render({
            elem: '#yxSjxx_formSearch #jsrqstart'
            , theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#yxSjxx_formSearch #jsrqend'
            , theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#yxSjxx_formSearch #bgrqstart'
            , type: 'date'
        });
        //添加日期控件
        laydate.render({
            elem: '#yxSjxx_formSearch #bgrqend'
            , type: 'date'
        });
        //添加日期控件
        laydate.render({
            elem: '#yxSjxx_formSearch #qyrqstart'
            , type: 'datetime'
            , ready: function (date) {
                if (this.dateTime.hours == 0 && this.dateTime.minutes == 0 && this.dateTime.seconds == 0) {
                    var myDate = new Date(); //实例一个时间对象；
                    this.dateTime.hours = myDate.getHours();
                    this.dateTime.minutes = myDate.getMinutes();
                    this.dateTime.seconds = myDate.getSeconds();
                }
            }
        });
        //添加日期控件
        laydate.render({
            elem: '#yxSjxx_formSearch #qyrqend'
            , type: 'datetime'
            , ready: function (date) {
                if (this.dateTime.hours == 0 && this.dateTime.minutes == 0 && this.dateTime.seconds == 0) {
                    var myDate = new Date(); //实例一个时间对象；
                    this.dateTime.hours = myDate.getHours();
                    this.dateTime.minutes = myDate.getMinutes();
                    this.dateTime.seconds = myDate.getSeconds();
                }
            }
        });
        //添加日期控件
        laydate.render({
            elem: '#yxSjxx_formSearch #syrqstart'
        });
        //添加日期控件
        laydate.render({
            elem: '#yxSjxx_formSearch #syrqend'
        });
        //添加日期控件
        laydate.render({
            elem: '#yxSjxx_formSearch #fkrqstart'
        });
        //添加日期控件
        laydate.render({
            elem: '#yxSjxx_formSearch #fkrqend'
        });
        /*---------------------------穆湖查询-----------------------------------*/
        btn_query.unbind("click").click(function () {
            searchYxSjxxResult(true);
        });
        /*---------------------------查看详情---------------------------------*/
        btn_view.unbind("click").click(function () {
            var sel_row = $('#yxSjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
            if (sel_row.length == 1) {
                MarketInspectionDealById(sel_row[0].sjid, sel_row[0].fjid, sel_row[0].xmglid, "view", btn_view.attr("tourl"));
            } else {
                $.error("请选中一行");
            }
        });
        /*---------------------------修改-----------------------------------*/
        btn_mod.unbind("click").click(function () {
            var sel_row = $('#yxSjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
            if (sel_row.length == 1) {
                MarketInspectionDealById(sel_row[0].sjid, sel_row[0].fjid, sel_row[0].xmglid, "mod", btn_mod.attr("tourl"));
            } else {
                $.error("请选中一行");
            }
        });

        btn_payment.unbind("click").click(function () {
            var sel_row = $('#yxSjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
            if (sel_row.length == 1) {
                if (sel_row[0].fjid){
                    $.error("请选择正常送检数据！");
                    return;
                }
                if (sel_row[0].clbj == "无需处理"){
                    $.error("请选择待处理数据！");
                    return;
                }
                MarketInspectionDealById(sel_row[0].sjid, sel_row[0].fjid, sel_row[0].xmglid, "payment", btn_payment.attr("tourl"));
            } else {
                $.error("请选中一行");
            }
        });

        /* ------------------------------添加送检信息-----------------------------*/
        btn_add.unbind("click").click(function(){
            MarketInspectionDealById(null,null, null, "add",btn_add.attr("tourl"));
        });

        /*----------------------------高级修改--------------------------------*/
        btn_advancedmod.unbind("click").click(function () {
            var sel_row = $('#yxSjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
            if (sel_row.length == 1) {
                MarketInspectionDealById(sel_row[0].sjid, sel_row[0].fjid, sel_row[0].xmglid, "advancedmod", btn_advancedmod.attr("tourl"));
            } else {
                $.error("请选中一行");
            }
        });
        /*----------------------------列表字段显示-----------------------------*/
        initTableField.unbind("click").click(function () {
            $.showDialog(titleSelectUrl + "?ywid=MARKETINGINSPECT"
                , "列表字段设置", $.extend({}, setSjxxListFieldsConfig, {"width": "1020px"}));
        });
        /*----------------------------选中导出--------------------------------*/
        btn_selectexport.unbind("click").click(function () {
            var sel_row = $('#yxSjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
            if (sel_row.length >= 1) {
                var list = [];
                var ids = "";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    var map=new Map()
                    if (sel_row[i].fjid){
                        ids = ids + "," + sel_row[i].fjid;
                        map.set("id", sel_row[i].fjid);
                    }else {
                        ids = ids + "," + sel_row[i].sjid;
                        map.set("id", sel_row[i].sjid);
                    }
                    if (sel_row[i].xmglid){
                        map.set("xmglid", sel_row[i].xmglid);
                    }
                    list.push(Object.fromEntries(map));
                }
                if (ids.length > 0) {
                    ids = ids.substring(1);
                }
                $.showDialog(exportPrepareUrl + "?ywid=MARKETINGINSPECT_SELECT&expType=select&ids=" + ids +"&selectjson="+encodeURI(JSON.stringify(list)), btn_selectexport.attr("gnm"), $.extend({}, viewConfig, {"width": "1020px"}));
            } else {
                $.error("请选择数据");
            }
        });
        /*----------------------------搜索导出--------------------------------*/
        btn_searchexport.unbind("click").click(function () {
            $.showDialog(exportPrepareUrl + "?ywid=MARKETINGINSPECT_SEARCH&expType=search&callbackJs=inspectSearchData"
                , btn_searchexport.attr("gnm"), $.extend({}, viewConfig, {"width": "1020px"}));
        });
        /*----------------------------金额选中导出-----------------------------*/
        btn_amountselectexport.unbind("click").click(function () {
            var sel_row = $('#yxSjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
            if (sel_row.length >= 1) {
                var list = [];
                var ids = "";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    var map=new Map()
                    if (sel_row[i].fjid){
                        ids = ids + "," + sel_row[i].fjid;
                        map.set("id", sel_row[i].fjid);
                    }else {
                        ids = ids + "," + sel_row[i].sjid;
                        map.set("id", sel_row[i].sjid);
                    }
                    if (sel_row[i].xmglid){
                        map.set("xmglid", sel_row[i].xmglid);
                    }
                    list.push(Object.fromEntries(map));
                }
                if (ids.length > 0) {
                    ids = ids.substring(1);
                }
                $.showDialog(exportPrepareUrl + "?ywid=MARKETINGINSPECT_AMOUNTSELECT&expType=select&ids=" + ids +"&selectjson="+encodeURI(JSON.stringify(list))
                    , btn_amountselectexport.attr("gnm"), $.extend({}, viewConfig, {"width": "1020px"}));
            } else {
                $.error("请选择数据");
            }
        });
        /*----------------------------金额搜索导出-----------------------------*/
        btn_amountsearchexport.unbind("click").click(function () {
            $.showDialog(exportPrepareUrl + "?ywid=MARKETINGINSPECT_AMOUNTSEARCH&expType=search&callbackJs=inspectSearchData"
                , btn_amountsearchexport.attr("gnm"), $.extend({}, viewConfig, {"width": "1020px"}));
        });
        /*----------------------------修改扩展参数-----------------------------*/
        btn_extend.unbind("click").click(function () {
            var sel_row = $('#yxSjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
            if (sel_row.length == 0) {
                $.error("请至少选中一行");
                return;
            } else {
                var ids = "";
                for (var i = 0; i < sel_row.length; i++) {
                    if (sel_row[i].fjid){
                        $.error("请不要选择复检加测的数据进行扩展修改！");
                        return;
                    }
                    ids = ids + "," + sel_row[i].sjid;
                }
                ids = ids.substr(1);
                MarketInspectionDealById(ids, null, null, "extend", btn_extend.attr("tourl"));
            }
        })
        /*----------------------------报告下载--------------------------------*/
        btn_reportdownload.unbind("click").click(function () {
            //判断有选中的采用选中导出，没有采用选择导出
            var sel_row = $('#yxSjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
            var ids = "";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                if (sel_row[i].fjid){
                    $.error("请不要选择复检加测的数据进行报告下载！");
                    return;
                }
                ids = ids + "," + sel_row[i].sjid;
            }
            ids = ids.substr(1);
            MarketInspectionDealById(ids, null, null, "reportdownload", btn_reportdownload.attr("tourl"));
        });
        /*----------------------------报告下载--------------------------------*/
        btn_pdfreportdownload.unbind("click").click(function () {
            //判断有选中的采用选中导出，没有采用选择导出
            var sel_row = $('#yxSjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
            var ids = "";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                if (sel_row[i].fjid){
                    $.error("请不要选择复检加测的数据进行报告下载！");
                    return;
                }
                ids = ids + "," + sel_row[i].sjid;
            }
            ids = ids.substr(1);
            MarketInspectionDealById(ids, null, null, "pdfreportdownload", btn_pdfreportdownload.attr("tourl"));
        });
        /*---------------------------导出对账单---------------------------------*/
        btn_exportaccount.unbind("click").click(function () {
            MarketInspectionDealById(null, null, null, "exportaccount", btn_exportaccount.attr("tourl"));
        });
        /*高级筛选 显示隐藏*/
        $("#yxSjxx_formSearch #sl_searchMore").on("click", function (ev) {
            var ev = ev || event;
            if (Inspection_turnOff) {
                $("#yxSjxx_formSearch #searchMore").slideDown("low");
                Inspection_turnOff = false;
                this.innerHTML = "基本筛选";
            } else {
                $("#yxSjxx_formSearch #searchMore").slideUp("low");
                Inspection_turnOff = true;
                this.innerHTML = "高级筛选";
            }
            ev.cancelBubble = true;
        });
    };
    return oInit;
};

//查询
function searchYxSjxxResult(isTurnBack) {
    //关闭高级搜索条件
    $("#yxSjxx_formSearch #searchMore").slideUp("low");
    Inspection_turnOff = true;
    $("#yxSjxx_formSearch #sl_searchMore").html("高级筛选");
    if (isTurnBack) {
        $('#yxSjxx_formSearch #sjxx_list').bootstrapTable('refresh', {pageNumber: 1});
    } else {
        $('#yxSjxx_formSearch #sjxx_list').bootstrapTable('refresh');
    }
}

//报告下载 自动检查报告重新发送进度
var checkReportResendStatus = function (intervalTime, redisKey) {
    $.ajax({
        type: "POST",
        url: "/common/export/commCheckExport",
        data: {"wjid": redisKey + "", "access_token": $("#ac_tk").val()},
        dataType: "json",
        success: function (data) {
            if (data.cancel) {//取消导出则直接return
                return;
            }
            if (data.result == false) {
                if (data.msg && data.msg != "") {
                    $.error(data.msg);
                    if ($("#cardiv")) $("#cardiv").remove();
                } else {
                    if (intervalTime < 5000)
                        intervalTime = intervalTime + 1000;
                    if ($("#exportCount")) {
                        $("#exportCount").html(data.currentCount);
                    }
                    setTimeout("checkReportResendStatus(" + intervalTime + ",'" + redisKey + "')", intervalTime);
                }
            } else {
                if (data.msg && data.msg != "") {
                    $.error(data.msg);
                    if ($("#cardiv")) $("#cardiv").remove();
                } else {
                    $.success("发送成功！");
                    if ($("#cardiv")) $("#cardiv").remove();
                }
            }
        }
    });
}

//报告下载 自动检查报告压缩进度
var checkReportZipStatus = function (intervalTime, redisKey) {
    $.ajax({
        type: "POST",
        url: "/common/export/commCheckExport",
        data: {"wjid": redisKey + "", "access_token": $("#ac_tk").val()},
        dataType: "json",
        success: function (data) {
            if (data.cancel) {//取消导出则直接return
                return;
            }
            if (data.result == false) {
                if (data.msg && data.msg != "") {
                    $.error(data.msg);
                    if ($("#cardiv")) $("#cardiv").remove();
                } else {
                    if (intervalTime < 5000)
                        intervalTime = intervalTime + 1000;
                    if ($("#exportCount")) {
                        $("#exportCount").html(data.currentCount)
                        if (("/" + data.currentCount) == $("#totalCount").text()) {
                            $("#totalCount").html($("#totalCount").text() + " 压缩中....")
                        }
                    }
                    setTimeout("checkReportZipStatus(" + intervalTime + ",'" + redisKey + "')", intervalTime);
                }
            } else {
                if (data.msg && data.msg != "") {
                    $.error(data.msg);
                    if ($("#cardiv")) $("#cardiv").remove();
                } else {
                    if ($("#cardiv")) $("#cardiv").remove();
                    window.open("/common/export/commDownloadExport?wjid=" + redisKey + "&access_token=" + $("#ac_tk").val());
                }
            }
        }
    });
}

//高级筛选 合作伙伴子分类的取得
function getSjhbzfls() {
    // 合作伙伴分类
    var sjhbfl = jQuery('#yxSjxx_formSearch #sjhbfl_id_tj').val();
    if (!isEmpty(sjhbfl)) {
        sjhbfl = "'" + sjhbfl + "'";
        jQuery("#yxSjxx_formSearch #sjhbzfl_id").removeClass("hidden");
    } else {
        jQuery("#yxSjxx_formSearch #sjhbzfl_id").addClass("hidden");
    }
    // 项目类别不为空
    if (!isEmpty(sjhbfl)) {
        var url = "/systemmain/data/ansyGetJcsjListInStop";
        $.ajax({
            type: "POST",
            url: url,
            data: {"fcsids": sjhbfl, "access_token": $("#ac_tk").val()},
            dataType: "json",
            success: function (data) {
                if (data.length > 0) {
                    var html = "";
                    $.each(data, function (i) {
                        html += "<li>";
                        if (data[i].scbj == "2") {
                            html += "<a style='text-decoration:none;cursor:pointer;' onclick=\"addTj('sjhbzfl','" + data[i].csid + "','yxSjxx_formSearch');\" id=\"sjhbzfl_id_" + data[i].csid + "\"><span style='color:red'>" + data[i].csmc + "(停用)" + "</span></a>";
                        } else
                            html += "<a style='text-decoration:none;cursor:pointer;' onclick=\"addTj('sjhbzfl','" + data[i].csid + "','yxSjxx_formSearch');\" id=\"sjhbzfl_id_" + data[i].csid + "\"><span>" + data[i].csmc + "</span></a>";
                        html += "</li>";
                    });
                    jQuery("#yxSjxx_formSearch #ul_sjhbzfl").html(html);
                    jQuery("#yxSjxx_formSearch #ul_sjhbzfl").find("[name='more']").each(function () {
                        $(this).on("click", s_showMoreFn);
                    });
                } else {
                    jQuery("#yxSjxx_formSearch #ul_sjhbzfl").empty();

                }
                jQuery("#yxSjxx_formSearch [id^='sjhbzfl_li_']").remove();
                $("#yxSjxx_formSearch #sjhbzfl_id_tj").val("");
            }
        });
    } else {
        jQuery("#yxSjxx_formSearch #div_sjhbzfl").empty();
        jQuery("#yxSjxx_formSearch [id^='sjhbzfl_li_']").remove();
        $("#yxSjxx_formSearch #sjhbzfl_id_tj").val("");
    }
}

//提供给导出用的回调函数
function inspectSearchData() {
    var map = {};
    map["access_token"] = $("#ac_tk").val();
    map["sortLastName"] = "sj.sjid";
    map["sortLastOrder"] = "asc";
    map["sortName"] = "sj.lrsj";
    map["sortOrder"] = "asc";
    return getSjxxSearchData(map);
}

var exportAccountConfig = {
    width		: "1600px",
    modalName	: "exportAccountModal",
    formName	: "exportAccountForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        select : {
            label : "查 询",
            className : "btn-success",
            callback : function() {
                if (!$("#exportAccountForm #jsrq").val()) {
                    $.error("请选择接收日期！")
                    return false;
                }
                if (!$("#exportAccountForm #dzzq option:selected").val()) {
                    $.error("请选择对账周期！")
                    return false;
                }
                selectData();
                return false;
            }
        },
        export : {
            label : "导 出",
            className : "btn-primary",
            callback : function() {
                if (!$("#exportAccountForm #jsrq").val()) {
                    $.error("请选择接收日期！")
                    return false;
                }
                if (!$("#exportAccountForm #dzzq option:selected").val()) {
                    $.error("请选择对账周期！")
                    return false;
                }
                window.open("/marketingInspection/marketingInspection/pagedataZipDownload?jsrq="+$("#exportAccountForm #jsrq").val()+"&hbmc="+$("#exportAccountForm #hbmc").val()+"&khmc="+$("#exportAccountForm #khmc").val()
                    +"&dzzq="+$("#exportAccountForm #dzzq option:selected").val()
                    +"&dzzqdm="+$("#exportAccountForm #dzzq option:selected").attr("csdm")
                    +"&access_token="+$("#ac_tk").val()
                )
                return false;
            }
        },
        exportAndSave : {
            label : "导出并保存",
            className : "btn-danger",
            callback : function() {
                if (!$("#exportAccountForm #jsrq").val()) {
                    $.error("请选择接收日期！")
                    return false;
                }
                if (!$("#exportAccountForm #dzzq option:selected").val()) {
                    $.error("请选择对账周期！")
                    return false;
                }
                window.open("/marketingInspection/marketingInspection/pagedataZipDownload?jsrq="+$("#exportAccountForm #jsrq").val()+"&hbmc="+$("#exportAccountForm #hbmc").val()+"&khmc="+$("#exportAccountForm #khmc").val()
                    +"&dzzq="+$("#exportAccountForm #dzzq option:selected").val()
                    +"&dzzqdm="+$("#exportAccountForm #dzzq option:selected").attr("csdm")
                    +"&dcbj="+$("#exportAccountForm #dcbj").val()
                    +"&sfbc=1"
                    +"&access_token="+$("#ac_tk").val()
                )
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};


//查看送检信息模态框
var paymentConfig = {
    width: "1200px",
    modalName: "paymentModal",
    height: "500px",
    offAtOnce: true,  //当数据提交成功，立刻关闭窗口
    buttons: {
        success: {
            label: "确 定",
            className: "btn-primary",
            callback: function () {
                if(!$("#amountPayment_formSearch").valid()){
                    return false;
                }
                if(laterList != null && laterList.length > 0){
                    var json = [];
                    for(var i=0;i<laterList.length;i++){
                        var sz = {"xmglid":'',"sfje":'',"tkje":'',"yfje":'',"dzje":'',"fkrq":'',"tkrq":''};
                        sz.xmglid = laterList[i].xmglid;
                        sz.sfje = laterList[i].sfje;
                        sz.tkje = laterList[i].tkje;
                        sz.yfje = laterList[i].yfje;
                        sz.dzje = laterList[i].dzje;
                        sz.fkrq = laterList[i].fkrq;
                        if ( sz.fkrq && !/\d{4}(\-|\/|.)\d{1,2}\1\d{1,2}/.test( sz.fkrq)){
                            $("#amountPayment_formSearch #fkrq_"+i).val("");
                            $.error("请填写正确日期格式");
                            return false;
                        }
                        sz.tkrq = laterList[i].tkrq;
                        if ( sz.tkrq && !/\d{4}(\-|\/|.)\d{1,2}\1\d{1,2}/.test( sz.tkrq)){
                            $("#amountPayment_formSearch #tkrq_"+i).val("");
                            $.error("请填写正确日期格式");
                            return false;
                        }
                        json.push(sz)
                    }
                    $("#amountPayment_formSearch #json").val(JSON.stringify(json))
                }
                var $this = this;
                var opts = $this["options"] || {};
                $("#amountPayment_formSearch input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"amountPayment_formSearch",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchYxSjxxResult();
                        });
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {
                        });
                    }else{
                        preventResubmitForm(".modal-footer > button", false);
                        $.alert(responseText["message"],function() {
                        });
                    }
                },".modal-footer > button");
                return false;
            }
        },
        cancel: {
            label: "关 闭",
            className: "btn-default"
        }
    }
};
//添加送检信息模态框
var addYxSjxxConfig = {
    width		: "1000px",
    modalName	: "addYxSjxxModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                $("#ajaxForm #syfjInfoTab #ywlx").remove();
                //去除空格
                $("#ajaxForm #db").val($("#ajaxForm #db").val().replace(/\s+/g,""));
                $("#ajaxForm #sjys").val($("#ajaxForm #sjys").val().trim());

                var cskz2=$("#ajaxForm #kdlx").find("option:selected").attr("cskz2");

                if($("#ajaxForm #ybbh").val()==""||$("#ajaxForm #ybbh").val()==null){
                    $.alert("请输入标本编号！");
                    return false;
                }else if($("#ajaxForm #hzxm").val()==""||$("#ajaxForm #hzxm").val()==null){
                    $.alert("请输入患者姓名！");
                    return false;
                }else if($("#ajaxForm input:radio[name='xb']:checked").val()==null){
                    $.alert("请选择性别！");
                    return false;
                }else if($("#ajaxForm #nl").val()==""||$("#ajaxForm #nl").val()==null){
                    $.alert("请输入年龄！");
                    return false;
                }else if($("#ajaxForm #sjdw").val()==""||$("#ajaxForm #sjdw").val()==null){
                    $.alert("医院名称不正确！");
                    return false;
                }else if($("#ajaxForm #sjdwmc").val()==""||$("#ajaxForm #sjdwmc").val()==null){
                    $.alert("请输入报告显示单位！");
                    return false;
                }else if($("#ajaxForm #sjys").val()==""||$("#ajaxForm #sjys").val()==null){
                    $.alert("请输入主治医师！");
                    return false;
                }else if($("#ajaxForm #ks option:selected").text()=="--请选择--"){
                    $.alert("请选择科室！");
                    return false;
                }else if($("#ajaxForm #qqzd").val()==""||$("#ajaxForm #qqzd").val()==null){
                    $.alert("请输入前期诊断！");
                    return false;
                }else if($("#ajaxForm #db").val()==""||$("#ajaxForm #db").val()==null){
                    $.alert("请输入合作伙伴！");
                    return false;
                }else if($("#ajaxForm input:radio[name='jcxmids']:checked").val()==null){
                    $.alert("请选择检测项目！");
                    return false;
                }else if($("#ajaxForm input:radio[name='sjqf']:checked").val()==null){
                    $.alert("请选择送检区分！");
                    return false;
                }else if(!$("#ajaxForm #kyDIV").is(":hidden") && $("#ajaxForm #kyxm option:selected").text()=="--请选择--"){
                    $.alert("请选择立项编号！");
                    return false;
                }else if($("#ajaxForm #yblx option:selected").text()=="--请选择--"){
                    $.alert("请选择标本类型！");
                    return false;
                }else if($("#ajaxForm #ybtj").val()==""||$("#ajaxForm #ybtj").val()==null){
                    $.alert("请输入标本体积！");
                    return false;
                }else if($("#ajaxForm #cyrq").val()==""||$("#ajaxForm #cyrq").val()==null){
                    $.alert("请输入采样日期！");
                    return false;
                }else if($("#ajaxForm #kdlx").val()==""||$("#ajaxForm #kdlx").val()==null){
                    $.alert("请选择快递类型！");
                    return false;
                }else if($("#ajaxForm #lczz").val()==""||$("#ajaxForm #lczz").val()==null){
                    $.alert("请输入主诉！");
                    return false;
                }else if(!cskz2){
                    if($("#ajaxForm #kdh").val()==""){
                        $.alert("请填写快递单号！");
                        return false;
                    }
                }
                if(/[^\a-\z\A-\Z0-9 -]/g.test($("#ajaxForm #ybbh").val())){
                    $.error("样本编号只能包含英文、数字以及-");
                    return false;
                }
                var qf=$("#ajaxForm input:radio[name='sjqf']:checked").val();
                var divisionList=JSON.parse($("#ajaxForm #divisionListJson").val());
                var kycsid;
                for (let i = 0; i < divisionList.length; i++) {
                    if(divisionList[i].csmc=='科研'){
                        kycsid=divisionList[i].csid;
                    }
                }
                if(kycsid!=null||kycsid!=undefined){
                    if(kycsid!=qf){
                        $("#ajaxForm #kyxm").val("")
                    }
                }

                $("#ajaxForm #divisionListJson").val()
                var $this = this;
                var opts = $this["options"]||{};
                $("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            PCRAudit(responseText["bys"],responseText["jcdw"],responseText["sjid"],responseText["jcxmids"],"");
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
//查看送检信息模态框
var viewSjxxConfig = {
    width: "1000px",
    height: "500px",
    offAtOnce: true,  //当数据提交成功，立刻关闭窗口
    buttons: {
        cancel: {
            label: "关 闭",
            className: "btn-default"
        }
    }
};

//修改送检信息模态框
var modYxSjxxConfig = {
    width: "1000px",
    modalName: "modYxSjxxModal",
    offAtOnce: true,  //当数据提交成功，立刻关闭窗口
    buttons: {
        success: {
            label: "确 定",
            className: "btn-primary",
            callback: function () {
                $("#ajaxForm #db").val($("#ajaxForm #db").val().replace(/\s+/g, ""));
                var sfws =$("#ajaxForm #sfws").val();
                if ($("#ajaxForm #ybbh").val() == "" || $("#ajaxForm #ybbh").val() == null) {
                    $.alert("请输入标本编号！");
                    return false;
                } else if ($("#ajaxForm #hzxm").val() == "" || $("#ajaxForm #hzxm").val() == null) {
                    $.alert("请输入患者姓名！");
                    return false;
                } else if ($("#ajaxForm input:radio[name='xb']:checked").val() == null) {
                    $.alert("请选择性别！");
                    return false;
                } else if ($("#ajaxForm #nl").val() == "" || $("#ajaxForm #nl").val() == null) {
                    $.alert("请输入年龄！");
                    return false;
                } else if ($("#ajaxForm #db").val() == "" || $("#ajaxForm #db").val() == null) {
                    $.alert("请输入合作伙伴！");
                    return false;
                } else if($("#ajaxForm .jcxm").length >0){
                    var json = [];
                    for (let i = 0; i <= $("#ajaxForm .jcxm").length -1; i++) {
                        if ($("#ajaxForm #"+$("#ajaxForm .jcxm")[i].id).is(':checked')){
                            var sz = {"xmglid":'',"jcxmid": '', "jczxmid": '',"flag":'',"csmc":'',"zcsmc":'',"sfsf":'',"yfje":'',"sfje":'',"fkrq":''};
                            sz.jcxmid = $("#ajaxForm .jcxm")[i].id;
                            sz.jczxmid = null;
                            sz.csmc = $("#ajaxForm #" + $("#ajaxForm .jcxm")[i].id).attr("csmc");
                            sz.zcsmc = null;
                            sz.flag = $("#ajaxForm #" + $("#ajaxForm .jcxm")[i].id).attr("flag");
                            json.push(sz)
                        }
                    }
                    if (json.length == 0){
                        $.alert("请选择检测项目！");
                        return false;
                    }
                    if($("#ajaxForm .jczxm").length >0){
                        for (let i = 0; i <= $("#ajaxForm .jczxm").length -1; i++) {
                            if ($("#ajaxForm #" + $("#ajaxForm .jczxm")[i].id).is(':checked')) {
                                var jcxmid = $("#ajaxForm #" + $("#ajaxForm .jczxm")[i].id).attr("jcxmid")
                                if (json.length > 0) {
                                    for (let k = json.length - 1; k >= 0; k--) {
                                        if (json[k].jcxmid == jcxmid && json[k].jczxmid == null) {
                                            json.splice(k, 1);
                                        }
                                    }
                                    var sz = {"xmglid":'',"jcxmid": '', "jczxmid": '',"flag":'',"csmc":'',"zcsmc":'',"sfsf":'',"yfje":'',"sfje":'',"fkrq":''};
                                    sz.jcxmid = $("#ajaxForm #" + $("#ajaxForm .jczxm")[i].id).attr("jcxmid");
                                    sz.jczxmid = $("#ajaxForm .jczxm")[i].id;
                                    sz.csmc = $("#ajaxForm #" + sz.jcxmid).attr("csmc");
                                    sz.zcsmc = $("#ajaxForm #" + $("#ajaxForm .jczxm")[i].id).attr("csmc");
                                    sz.flag = $("#ajaxForm #" + sz.jcxmid).attr("flag");
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
                    var dtoList = JSON.parse($("#ajaxForm #dtoList").val());
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
                    if("1"!=$("#ajaxForm #xg_flg").val()){
                        for (let k = 0; k <json.length; k++) {
                            for (var i = 0; i < t_map.rows.length; i++) {
                                if (json[k].jcxmid == t_map.rows[i].jcxmid) {
                                    if(json[k].jczxmid){
                                        if (json[k].jczxmid == t_map.rows[i].jczxmid) {
                                            if(t_map.rows[i].sfsf){
                                                json[k].sfsf=t_map.rows[i].sfsf;
                                            }else{
                                                json[k].sfsf="1";
                                            }
                                            if(t_map.rows[i].yfje){
                                                json[k].yfje=t_map.rows[i].yfje;
                                            }else{
                                                json[k].yfje="0";
                                            }
                                            if(t_map.rows[i].sfsf&&t_map.rows[i].sfsf=='0'){
                                                json[k].yfje="0";
                                            }
                                            if(t_map.rows[i].sfje){
                                                json[k].sfje=t_map.rows[i].sfje;
                                            }else{
                                                json[k].sfje="0";
                                            }
                                            json[k].fkrq=t_map.rows[i].fkrq;
                                            break;
                                        }
                                    }else{
                                        if(t_map.rows[i].sfsf){
                                            json[k].sfsf=t_map.rows[i].sfsf;
                                        }else{
                                            json[k].sfsf="1";
                                        }
                                        if(t_map.rows[i].yfje){
                                            json[k].yfje=t_map.rows[i].yfje;
                                        }else{
                                            json[k].yfje="0";
                                        }
                                        if(t_map.rows[i].sfsf&&t_map.rows[i].sfsf=='0'){
                                            json[k].yfje="0";
                                        }
                                        if(t_map.rows[i].sfje){
                                            json[k].sfje=t_map.rows[i].sfje;
                                        }else{
                                            json[k].sfje="0";
                                        }
                                        json[k].fkrq=t_map.rows[i].fkrq;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    $("#ajaxForm #jcxmmc").val(jcxmmc.substring(1));
                    $("#ajaxForm #jcxm").val(JSON.stringify(json))
                } else if ($("#ajaxForm input:radio[name='sjqf']:checked").val() == null) {
                    $.alert("请选择送检区分！");
                    return false;
                } else if (!$("#ajaxForm #kyDIV").is(":hidden") && $("#ajaxForm #kyxm option:selected").text() == "--请选择--") {
                    $.alert("请选择立项编号！");
                    return false;
                } else if ($("#ajaxForm #yblx option:selected").text() == "--请选择--") {
                    $.alert("请选择标本类型！");
                    return false;
                }  else if ($("#ajaxForm #cyrq").val() == "" || $("#ajaxForm #cyrq").val() == null) {
                    $.alert("请输入采样日期！");
                    return false;
                }
                if (sfws==3){
                    if ($("#ajaxForm #hospitalname").val() == "" || $("#ajaxForm #hospitalname").val() == null) {
                        $.alert("请选择医院名称！");
                        return false;
                    } else if ($("#ajaxForm #sjdw").val() == "" || $("#ajaxForm #sjdw").val() == null) {
                        $.alert("医院名称不正确！");
                        return false;
                    } else if ($("#ajaxForm #sjdwmc").val() == "" || $("#ajaxForm #sjdwmc").val() == null) {
                        $.alert("请输入报告显示单位！");
                        return false;
                    } else if ($("#ajaxForm #sjys").val() == "" || $("#ajaxForm #sjys").val() == null) {
                        $.alert("请输入主治医师！");
                        return false;
                    } else if ($("#ajaxForm #ks option:selected").text() == "--请选择--") {
                        $.alert("请选择科室！");
                        return false;
                    } else if ($("#ajaxForm #ybtj").val() == "" || $("#ajaxForm #ybtj").val() == null) {
                        $.alert("请输入标本体积！");
                        return false;
                    } else if ($("#ajaxForm #kdlx").val() == "" || $("#ajaxForm #kdlx").val() == null) {
                        $.alert("请选择快递类型！");
                        return false;
                    } else if ($("#ajaxForm #lczz").val() == "" || $("#ajaxForm #lczz").val() == null) {
                        $.alert("请输入主诉！");
                        return false;
                    }else if ($("#ajaxForm #qqzd").val() == "" || $("#ajaxForm #qqzd").val() == null) {
                        $.alert("请输入前期诊断！");
                        return false;
                    }
                }

                //验证开票申请
                if ($("#ajaxForm #sfsf").val() && $("#ajaxForm #sfsf").val() != 0) {
                    var i = 0;
                    $("#ajaxForm .kpsq").each(function () {
                        if (this.checked == true) {
                            i++;
                        }
                    })
                    if (i == 0) {
                        $.alert("请填写开票申请！");
                        return false;
                    }
                }

                //验证快递单号
                var cskz2 = $("#ajaxForm #kdlx").find("option:selected").attr("cskz2");
                if (!cskz2) {
                    if ($("#ajaxForm #kdh").val() == "") {
                        $.alert("请填写快递单号！");
                        return false;
                    }
                }

                if ($("#ajaxForm #nbbm").val() != null) {
                    var nbbm = $("#ajaxForm #nbbm").val();
                    $("#ajaxForm #px").val(nbbm.substring(1, nbbm.length - 1));
                }
                var $this = this;
                var opts = $this["options"] || {};
                $("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
                if ($("#ajaxForm #sfsf").val() == "0" && $("#ajaxForm #sfje").val() > 0) {
                    $.confirm('该标本已付费：' + $("#ajaxForm #sfje").val() + '元！，是否继续操作？', function (result) {
                        if (result) {
                            submitForm(opts["formName"] || "ajaxForm", function (responseText, statusText) {
                                if (responseText["status"] == 'success') {
                                    $.success(responseText["message"], function () {
                                        if (opts.offAtOnce) {
                                            $.closeModal(opts.modalName);
                                        }
                                        PCRAudit(responseText["bys"], responseText["jcdw"], responseText["sjid"], responseText["jcxmids"], responseText["ygzbys"]);
                                    });
                                } else if (responseText["status"] == "fail") {
                                    preventResubmitForm(".modal-footer > button", false);
                                    $.error(responseText["message"], function () {
                                    });
                                } else {
                                    preventResubmitForm(".modal-footer > button", false);
                                    $.alert(responseText["message"], function () {
                                    });
                                }
                            }, ".modal-footer > button");
                        }
                    })
                } else {
                    submitForm(opts["formName"] || "ajaxForm", function (responseText, statusText) {
                        if (responseText["status"] == 'success') {
                            $.success(responseText["message"], function () {
                                if (opts.offAtOnce) {
                                    $.closeModal(opts.modalName);
                                }
                                PCRAudit(responseText["bys"], responseText["jcdw"], responseText["sjid"], responseText["jcxmids"], responseText["ygzbys"]);
                            });
                        } else if (responseText["status"] == "fail") {
                            preventResubmitForm(".modal-footer > button", false);
                            $.error(responseText["message"], function () {
                            });
                        } else {
                            preventResubmitForm(".modal-footer > button", false);
                            $.alert(responseText["message"], function () {
                            });
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
};

//修改送检信息模态框
var modYxFjxxConfig = {
    width: "1000px",
    modalName: "modYxFjxxModal",
    offAtOnce: true,  //当数据提交成功，立刻关闭窗口
    buttons: {
        success: {
            label: "确 定",
            className: "btn-primary",
            callback: function () {
                if (!$("#recheck_mod_Form").valid()) {
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"] || {};
                $("#recheck_mod_Form input[name='access_token']").val($("#ac_tk").val());

                if ($("#recheck_mod_Form .yy") != null && $("#recheck_mod_Form .yy").length > 0) {
                    var yys = "";
                    var yymcs = "";
                    for (var i = 0; i < $("#recheck_mod_Form .yy").length; i++) {
                        if ($("#recheck_mod_Form .yy")[i].checked) {
                            yys = yys + "," + $("#recheck_mod_Form .yy")[i].value;
                            yymcs = yymcs + ";" + $("#recheck_mod_Form #yy_" + $("#recheck_mod_Form .yy")[i].value + " span").text();
                        }
                    }
                    $("#recheck_mod_Form #yys").val(yys.substring(1));
                    $("#recheck_mod_Form #yy").val(yymcs.substring(1));
                }
                submitForm(opts["formName"] || "recheck_mod_Form", function (responseText, statusText) {
                    if (responseText["status"] == 'success') {
                        $.success(responseText["message"], function () {
                            if (opts.offAtOnce) {
                                $.closeModal(opts.modalName);
                            }
                            searchYxSjxxResult();
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

//扩展参数修改
var modkzcsConfig = {
    width: "800px",
    modalName: "modkzcsConfig",
    offAtOnce: true,  //当数据提交成功，立刻关闭窗口
    buttons: {
        success: {
            label: "确 定",
            className: "btn-primary",
            callback: function () {
                var $this = this;
                var opts = $this["options"] || {};
                $("#cskzFrom input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"] || "cskzFrom", function (responseText, statusText) {
                    if (responseText["status"] == 'success') {
                        $.success(responseText["message"], function () {
                            if (opts.offAtOnce) {
                                $.closeModal(opts.modalName);
                            }
                            searchYxSjxxResult();
                        });
                    } else if (responseText["status"] == "fail") {
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"], function () {
                        });
                    } else {
                        preventResubmitForm(".modal-footer > button", false);
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

//报告下载模态框
var reportDownloadConfig = {
    width: "800px",
    modalName: "reportDownloadConfig",
    formName: "recheck_Form",
    offAtOnce: true,  //当数据提交成功，立刻关闭窗口
    buttons: {
        success: {
            label: "确 定",
            className: "btn-primary",
            callback: function () {
                var $this = this;
                var opts = $this["options"] || {};
                var bglxbj = $("#ajaxForm input[type='radio']:checked").val();
                var map = {
                    access_token: $("#ac_tk").val(),
                    bglxbj: bglxbj,
                };
                //判断有选中的采用选中导出，没有采用选择导出
                var sel_row = $('#yxSjxx_formSearch #sjxx_list').bootstrapTable('getSelections');//获取选择行数据
                var ids = "";
                if (sel_row.length >= 1) {
                    for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                        if (sel_row[i].fjid){
                            $.error("请不要选择复检加测的数据进行扩展修改！");
                            return;
                        }
                        ids = ids + "," + sel_row[i].sjid;
                    }
                    ids = ids.substr(1);
                    map["ids"] = ids;
                } else {
                    getSjxxSearchData(map);
                }
                var url = $("#ajaxForm #action").val();
                $.post(url, map, function (data) {
                    if (data) {
                        if (data.status == 'success') {
                            if (opts.offAtOnce) {
                                $.closeModal(opts.modalName);
                            }
                            //创建objectNode
                            var cardiv = document.createElement("div");
                            cardiv.id = "cardiv";
                            var s_str = '<div class="modal-backdrop fade in" style="display: block; z-index: 9999;"><div align="center" style="top:45%"><img src="/js/plugins/backdrop/images/ico-loading.gif"><p><span id="exportCount" style="color:red;font-weight:600;">0</span><span id="totalCount" style="color:red;font-weight:600;">/' + data.count + '</span></p><p class="padding_t7"><button type="button" class="btn btn-primary" id="exportCancel">取消</button></p></div></div>';
                            cardiv.innerHTML = s_str;
                            //将上面创建的P元素加入到BODY的尾部
                            document.body.appendChild(cardiv);
                            setTimeout("checkReportZipStatus(2000,'" + data.redisKey + "')", 1000);
                            //绑定导出取消按钮事件
                            $("#exportCancel").click(function () {
                                //先移除导出提示，然后请求后台
                                if ($("#cardiv").length > 0) $("#cardiv").remove();
                                $.ajax({
                                    type: "POST",
                                    url: "/common/export/commCancelExport",
                                    data: {"wjid": data.redisKey + "", "access_token": $("#ac_tk").val()},
                                    dataType: "json",
                                    success: function (res) {
                                        if (res != null && res.result == false) {
                                            if (res.msg && res.msg != "")
                                                $.error(res.msg);
                                        }

                                    }
                                });
                            });
                        } else if (data.status == 'fail') {
                            $.error(data.error, function () {
                            });
                        } else {
                            preventResubmitForm(".modal-footer > button", false);
                            if (data.error) {
                                $.alert(data.error, function () {
                                });
                            }
                        }
                    }
                }, 'json');
                return false;
            }
        },
        cancel: {
            label: "关 闭",
            className: "btn-default"
        }
    }
}

//设置字段模态框
var setSjxxListFieldsConfig = {
    width: "1020px",
    modalName: "setYxSjxxListFieldsConfig",
    buttons: {
        reelect: {
            label: "恢复默认",
            className: "btn-info",
            callback: function () {
                $("#ajaxForm").attr("action", "/common/title/commTitleDefaultSave");
                $("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
                submitForm("ajaxForm", function (responseText, statusText) {
                    if (responseText["status"] == 'success') {
                        $.success(responseText["message"], function () {
                            //关闭Modal
                            $.closeModal("setYxSjxxListFieldsConfig");
                            $('#yxSjxx_formSearch #sjxx_list').bootstrapTable('destroy');
                            t_sjxxfieldList = getSjxxDataColumn(responseText.choseList, responseText.waitList);

                            var oTable = new Inspection_TableInit(t_sjxxfieldList, false);
                            oTable.Init();
                        });
                    } else {
                        $.error(responseText["message"],function() {
                                                                    });
                    }
                    preventResubmitForm(".modal-footer > button", false);
                }, ".modal-footer > button");
                return false;
            }
        },
        success: {
            label: "确 定",
            className: "btn-primary",
            callback: function () {
                var _choseListDiv = $("#ajaxForm #list2 div[id='choseListDiv']");
                if (_choseListDiv.length < 1) {
                    $.error("请选择显示字段。")
                    return false;
                }
                $("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
                submitForm("ajaxForm", function (responseText, statusText) {
                    if (responseText["status"] == 'success') {
                        $.success(responseText["message"], function () {
                            //关闭Modal
                            $.closeModal("setSjxxListFieldsConfig");
                            $('#yxSjxx_formSearch #sjxx_list').bootstrapTable('destroy');
                            t_sjxxfieldList = getSjxxDataColumn(responseText.choseList, responseText.waitList);

                            var oTable = new Inspection_TableInit(t_sjxxfieldList, false);
                            oTable.Init();
                        });
                    } else {
                        $.error(responseText["message"],function() {
                                                                    });
                    }
                    preventResubmitForm(".modal-footer > button", false);
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

//获取表格显示形式
function getSjxxDataColumn(zdList, waitList) {
    var flelds = [];
    var map = {};
    var item = null;
    map = {};
    map["checkbox"] = true;
    flelds.push(map);
    $.each(zdList, function (i) {
        item = zdList[i];
        map = {};
        map["title"] = item.xszdmc;
        map["titleTooltip"] = item.zdsm;
        map["field"] = item.xszd;
        if (item.pxzd && item.pxzd != "")
            map["sortable"] = true;
        map["sortName"] = item.pxzd;
        map["width"] = item.xskd + "%";
        if (item.xsgs) {
            map["formatter"] = eval(item.xsgs);
        }
        map["visible"] = true;
        flelds.push(map);
    });
    $.each(waitList, function (i) {
        item = waitList[i];
        map = {};
        map["title"] = item.xszdmc;
        map["titleTooltip"] = item.zdsm;
        map["field"] = item.xszd;
        if (item.pxzd && item.pxzd != "")
            map["sortable"] = true;
        map["sortName"] = item.pxzd;
        map["width"] = item.xskd + "%";
        if (item.xsgs) {
            map["formatter"] = eval(item.xsgs);
        }
        if (zdList.length == 0) {
            if (waitList[i].mrxs == '0') {
                map["visible"] = false;
            } else if (waitList[i].mrxs == '9') {
                map["visible"] = false;
            }
        } else
            map["visible"] = false;
        flelds.push(map);
    });
    return flelds;
}

//自动发起PCR验证
function PCRAudit(bys, jcdw, sjid, jcxmids, ygzbys) {
    if (bys != null && bys != '') {
        $.ajax({
            type: "post",
            url: "/inspection/auditProcess/pagedataPCRAudit",
            data: {
                "bys": bys.toString(),
                "jcdw": jcdw.toString(),
                "sjid": sjid.toString(),
                "jcxmids": jcxmids.toString(),
                "access_token": $("#ac_tk").val(),
                "ygzbys": ygzbys.toString()
            },
            dataType: "json",
            success: function (responseText) {
                if (responseText["status"] == 'success') {
                    preventResubmitForm(".modal-footer > button", false);
                } else if (responseText["status"] == "fail") {
                    preventResubmitForm(".modal-footer > button", false);
                } else {
                }
            },
            fail: function (result) {

            }
        })
    }
    searchYxSjxxResult();
}

//是否显示实付总金额
function sfxsSfzje() {
    var xszds = $("#yxSjxx_formSearch #xszds").val().split(",");
    var flag = false;
    for (var i = 0; i < xszds.length; i++) {
        if (xszds[i] == 'sfzje') {
            flag = true;
        }
    }
    if (flag) {
        $("#yxSjxx_formSearch #sfmoney").removeClass("hidden");
        sfzjezdqx = 1;
    } else {
        $("#yxSjxx_formSearch #sfmoney").addClass("hidden");
        sfzjezdqx = 0;
    }
}

//是否显示退款总金额
function sfxsTkzje() {
    var xszds = $("#yxSjxx_formSearch #xszds").val().split(",");
    var flag = false;
    for (var i = 0; i < xszds.length; i++) {
        if (xszds[i] == 'tkzje') {
            flag = true;
        }
    }
    if (flag) {
        $("#yxSjxx_formSearch #tkmoney").removeClass("hidden");
        tkzjezdqx = 1;
    } else {
        $("#yxSjxx_formSearch #tkmoney").addClass("hidden");
        tkzjezdqx = 0;
    }
}

//页面初始化
$(function () {
    changoption()
    //是否显示实付总金额
    sfxsSfzje();
    //是否显示退款总金额
    sfxsTkzje();
    //0.界面初始化
    // 1.初始化Table
    var oTable = new Inspection_TableInit(sjxxFieldList, true);
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new Inspection_ButtonInit();
    oButtonInit.Init();
    // 所有下拉框添加choose样式
    jQuery('#yxSjxx_formSearch .chosen-select').chosen({width: '100%'});
    // 初始绑定显示更多的事件
    $("#yxSjxx_formSearch [name='more']").each(function () {
        $(this).on("click", s_showMoreFn);
    });
});


function changoption(){
    var val1=$("#cxtj1").val()
    var val2=$("#cxtj2").val()
    var val=$("#cxtj").val()
    $("#cxtj option").each(function(){
        $(this).attr("disabled",false)
    })
    $("#cxtj1 option").each(function(){
        $(this).attr("disabled",false)
    })
    $("#cxtj2 option").each(function(){
        $(this).attr("disabled",false)
    })
    if(val){
        $("#yxsjxx_select1_"+val).attr("disabled",true)
        $("#yxsjxx_select2_"+val).attr("disabled",true)
    }
    if(val1){
        $("#yxsjxx_select_"+val1).attr("disabled",true)
        $("#yxsjxx_select2_"+val1).attr("disabled",true)
    }
    if(val2){
        $("#yxsjxx_select_"+val2).attr("disabled",true)
        $("#yxsjxx_select1_"+val2).attr("disabled",true)
    }
    $("#yxsjxx_select_"+val).attr("selected","selected");
    $("#yxsjxx_select1_"+val1).attr("selected","selected");
    $("#yxsjxx_select2_"+val2).attr("selected","selected");

    $("#yxsjxx_formSearch #cxtj").trigger("chosen:updated");
    $("#yxsjxx_formSearch #cxtj1").trigger("chosen:updated");
    $("#yxsjxx_formSearch #cxtj2").trigger("chosen:updated");
}