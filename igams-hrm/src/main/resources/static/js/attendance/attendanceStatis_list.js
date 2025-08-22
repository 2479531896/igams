var AttendanceStatis_turnOff=true;
var attendanceStatis_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#attendanceStatisForm #attendanceStatis_list').bootstrapTable({
            url: $("#attendanceStatisForm #urlPrefix").val()+'/attendance/attendance/pageGetListAttendanceStatis',
            method: 'get',                      //请求方式（*）
            toolbar: '#attendanceStatisForm #toolbar', //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName:"yhkqxx.yhm",					//排序字段
            sortOrder: "asc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 15,                       //每页的记录行数（*）
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
            uniqueId: "yhid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width: '4%'
            }, {
                field: 'yhid',
                title: '用户ID',
                width: '8',
                align: 'left',
                visible: false
            }, {
                field: 'yhm-zsxm',
                title: '用户名',
                width: '15%',
                align: 'left',
                visible: true,
                formatter:yhmzsxmformat,
            }, {
                field: 'jgmc',
                title: '部门',
                width: '15%',
                align: 'left',
                visible: true
            }, {
                field: 'rzrq',
                title: '入职日期',
                width: '13%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'zc',
                title: '职称',
                width: '13%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'ycqts',
                title: '应出勤天数',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true,
                formatter:ycqformat
            }, {
                field: 'cqts',
                title: '实际出勤天数',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'xxts',
                title: '休息天数',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'cdcs',
                title: '迟到次数',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'cdsc',
                title: '迟到时长(分钟)',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'ztcs',
                title: '早退次数',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'ztsc',
                title: '早退时长',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'sbqkcs',
                title: '上班缺卡次数',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'xbqkcs',
                title: '下班缺卡次数',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'kgts',
                title: '旷工天数',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'ccsc',
                title: '出差时长',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'wcsc',
                title: '外出时长',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'jbzsc',
                title: '加班总时长',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'gzrjb',
                title: '工作日加班',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'xxrjb',
                title: '休息日加班',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'jjrjb',
                title: '节假日加班',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'sjxs',
                title: '事假(小时)',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'bjxs',
                title: '病假(小时)',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'njxs',
                title: '年假(小时)',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'txxs',
                title: '调休(小时)',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'hjts',
                title: '婚假(天)',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'cjts',
                title: '产假(天)',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'hljts',
                title: '护理假(天)',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'cjjts',
                title: '产检假(天)',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'sjts',
                title: '丧假(天)',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'yejts',
                title: '育儿假(天)',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'dsznphfmjts',
                title: '独生子女陪护父母假(天)',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'qjcs',
                title: '请假次数',
                width: '8%',
                align: 'left',
                sortable: true,
                formatter:qjcsformat,
                visible: true
            }, {
                field: 'btje',
                title: '补贴金额',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'kqlx',
                title: '考勤类型',
                width: '8%',
                align: 'left',
                sortable: true,
                formatter:kqlxformat,
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                attendanceStatisDealById(row.yhid,jQuery('#attendanceStatisForm #rqstart').val(),jQuery('#attendanceStatisForm #rqend').val(), 'view',$("#attendanceStatisForm #btn_view").attr("tourl"));
            },
        });
        $("#attendanceStatisForm #attendanceStatis_list").colResizable({
                liveDrag:true,
                gripInnerHtml:"<div class='grip'></div>",
                draggingClass:"dragging",
                resizeMode:'fit',
                postbackSafe:false,
                partialRefresh:true
            }
        );
    };

    //得到查询的参数
    oTableInit.queryParams = function(params){
        //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
        //例如 toolbar 中的参数
        //如果 queryParamsType = ‘limit’ ,返回参数必须包含
        //limit, offset, search, sort, order
        //否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder.
        //返回false将会终止请求
        var map = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   //页面大小
            pageNumber: (params.offset / params.limit) + 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "yhkqxx.yhid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };
        return getAttendenceStatisSearchData(map);
    };
    return oTableInit;
}
/**
 * 用户名真实姓名
 * @param value
 * @param row
 * @param index
 * @returns
 */
function yhmzsxmformat(value,row,index){
    if(row.yhm == null){
        row.yhm = "";
    }
    if (row.zsxm == null) {
        row.zsxm = "";
    }
    return row.yhm+"-"+row.zsxm;
}
/**
 * 请假次数格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function qjcsformat(value,row,index){
    var html = "";
    if(row.qjcs!=null){
        html += "<a href='javascript:void(0);' onclick=\"queryByqjcs('"+row.yhid+ "')\" >"+row.qjcs+"</a>";
    }
    return html;
}
/**
 * 考勤类型格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function kqlxformat(value,row,index){
    if(row.kqlx=="FIXED"){
       return "固定班制"
    }else if (row.kqlx=="TURN"){
        return "排班制";
    }else if (row.kqlx=="NONE"){
        return "自由工时";
    }else {
        return "";
    }
}
function queryByqjcs(yhid){
    var url=$("#attendanceStatisForm #urlPrefix").val()+"/attendance/attendance/pagedataViewLeave?yhid="+yhid+"&rqstart="+$("#attendanceStatisForm #rqstart").val()+"&rqend="+$("#attendanceStatisForm #rqend").val()+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'请假详情',viewLeaveConfig);
}
var viewLeaveConfig = {
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
 * 应出勤格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function ycqformat(value,row,index){
    var html = "";
    if(row.ycqts!=null){
        html = "<a href='javascript:void(0);' onclick=\"showAttendanceInfo('"+row.yhid+"')\">"+row.ycqts+"</a>";
    }
    return html;
}
//日历信息
function showAttendanceInfo(yhid){
    var url="/attendance/attendance/pagedataViewAttendanceInfo?yhid="+yhid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'个人考勤信息',viewAttendanceCalendarConfig);
}
var viewAttendanceCalendarConfig = {
    width		: "1600px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
var viewAttendanceConfig = {
    width		: "1600px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
function getAttendenceStatisSearchData(map){
    var cxbt = $("#attendanceStatisForm #cxbt").val();
    // 查询条件
    var cxnr = $.trim(jQuery('#attendanceStatisForm #cxnr').val());
    // 开始日期
    var rqstart = jQuery('#attendanceStatisForm #rqstart').val();
    map["rqstart"] = rqstart;
    // 结束日期
    var rqend = jQuery('#attendanceStatisForm #rqend').val();
    map["rqend"] = rqend;
    if (cxbt == "0") {
        map["entire"] = cxnr;
    }else  if (cxbt == "1") {
        map["yhm"] = cxnr;
    }else  if (cxbt == "2") {
        map["zsxm"] = cxnr;
    }else  if (cxbt == "3") {
        map["zc"] = cxnr;
    }else  if (cxbt == "4") {
        map["jgmc"] = cxnr;
    }
    //是否全勤
    var sfqq = jQuery('#attendanceStatisForm #sfqq_id_tj').val();
    map["sfqq"] = sfqq.replace(/'/g, "");;
    return map;
}

//按钮动作函数
function attendanceStatisDealById(id,rqstart,rqend,action,tourl){
    if(!tourl){
        return;
    }
    if(action =='view'){
        var url= tourl + "?yhid=" +id+"&rqstart="+rqstart+"&rqend="+rqend;
        $.showDialog(url,'查看用户考勤统计',viewAttendanceConfig);
    }
}
function SearchAttendenceData(){
    var map ={};
    map["access_token"]=$("#ac_tk").val();
    map["sortLastName"]="";
    map["sortLastOrder"]="desc";
    map["sortName"]="yhkqxx.kqlx";
    map["sortOrder"]="asc";
    return getAttendenceStatisSearchData(map);
}

var attendanceStatis_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //添加日期控件
        laydate.render({
            elem: '#attendanceStatisForm #rqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#attendanceStatisForm #rqend'
            ,theme: '#2381E9'
        });
        //初始化页面上面的按钮事件
        var btn_view = $("#attendanceStatisForm #btn_view");
        var btn_query = $("#attendanceStatisForm #btn_query");
        var btn_selectexport = $("#attendanceStatisForm #btn_selectexport");//选中导出
        var btn_searchexport = $("#attendanceStatisForm #btn_searchexport");//搜索导出
        var btn_detailedselectexport = $("#attendanceStatisForm #btn_detailedselectexport");//详细选中导出
        var btn_detailedsearchexport = $("#attendanceStatisForm #btn_detailedsearchexport");//详细搜索导出
        //绑定搜索发送功能
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                searchAttendanceStatisResult(true);
            });
        }
        btn_view.unbind("click").click(function(){
            var sel_row = $('#attendanceStatisForm #attendanceStatis_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                attendanceStatisDealById(sel_row[0].yhid,jQuery('#attendanceStatisForm #rqstart').val(),jQuery('#attendanceStatisForm #rqend').val(),"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        //---------------------------------选中导出---------------------------------------
        btn_selectexport.unbind("click").click(function(){
            var sel_row = $('#attendanceStatisForm #attendanceStatis_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length>=1){
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].yhid;
                }
                ids = ids.substr(1);
                $.showDialog($('#attendanceStatisForm #urlPrefix').val()+exportPrepareUrl+"?ywid=ATTENDANCESTATIS_SELECT&expType=select&ids="+ids+"&callbackJs=SearchAttendenceData"
                    ,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
            }else{
                $.error("请选择数据");
            }
        });
        //搜索导出
        btn_searchexport.unbind("click").click(function(){
            $.showDialog($('#attendanceStatisForm #urlPrefix').val()+exportPrepareUrl+"?ywid=ATTENDANCESTATIS_SEARCH&expType=search&callbackJs=SearchAttendenceData"
                ,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
        });
        //---------------------------------详细选中导出---------------------------------------
        btn_detailedselectexport.unbind("click").click(function(){
            var sel_row = $('#attendanceStatisForm #attendanceStatis_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length>=1){
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].yhid;
                }
                ids = ids.substr(1);
                $.showDialog($('#attendanceStatisForm #urlPrefix').val()+exportPrepareUrl+"?ywid=ATTENDANCESTATIS_DETAILS_SELECT&expType=select&ids="+ids+"&callbackJs=SearchAttendenceData"
                    ,btn_detailedselectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
            }else{
                $.error("请选择数据");
            }
        });
        //详细搜索导出
        btn_detailedsearchexport.unbind("click").click(function(){
            $.showDialog($('#attendanceStatisForm #urlPrefix').val()+exportPrepareUrl+"?ywid=ATTENDANCESTATIS_DETAILS_SEARCH&expType=search&callbackJs=SearchAttendenceData"
                ,btn_detailedsearchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
        });
        /*-----------------------显示隐藏------------------------------------*/
        $("#attendanceStatisForm #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(AttendanceStatis_turnOff){
                $("#attendanceStatisForm #searchMore").slideDown("low");
                AttendanceStatis_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#attendanceStatisForm #searchMore").slideUp("low");
                AttendanceStatis_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
            //showMore();
        });
    };

    return oInit;
};


function searchAttendanceStatisResult(isTurnBack){
    if(isTurnBack){
        $('#attendanceStatisForm #attendanceStatis_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#attendanceStatisForm #attendanceStatis_list').bootstrapTable('refresh');
    }
}

$(function(){

    //1.初始化Table
    var oTable = new attendanceStatis_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new attendanceStatis_ButtonInit();
    oButtonInit.Init();

    //所有下拉框添加choose样式
    jQuery('#attendanceStatisForm .chosen-select').chosen({width: '100%'});
});


