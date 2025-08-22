var Yhkqxx_turnOff=true;
var yhkqxx_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#yhkqxxformSearch #kqxx_list').bootstrapTable({
            url: $("#yhkqxxformSearch #urlPrefix").val()+'/attendance/attendance/pageGetListAttendanceNews',
            method: 'get',                      //请求方式（*）
            toolbar: '#yhkqxxformSearch #toolbar', //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName:"kqxx.rq desc,kqxx.yhm",					//排序字段
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
            uniqueId: "kqid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width: '4%'
            }, {
                field: 'kqid',
                title: '用户考勤ID',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'yhid',
                title: '用户ID',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'yhmc',
                title: '用户名',
                width: '6%',
                align: 'left',
                visible: true
            }, {
                field: 'jgmc',
                title: '所属机构',
                width: '8%',
                align: 'left',
                visible:true
            }, {
                field: 'rq',
                title: '日期',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'cqsj',
                title: '出勤时间',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'tqsj',
                title: '退勤时间',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'gzsc',
                title: '工作时长(小时)',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'cqzt',
                title: '出勤状态',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'qjlxmc',
                title: '请假类型',
                width: '8%',
                align: 'left',
                visible: true
            }, {
                field: 'qjkssj',
                title: '请假开始时间',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'qjjssj',
                title: '请假结束时间',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'qjsc',
                title: '请假时长',
                width: '8%',
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
                field: 'jbkssj',
                title: '加班开始时间',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'jbjssj',
                title: '加班结束时间',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'sc',
                title: '加班时长(小时)',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'sffdjr',
                title: '是否法定假日',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'hsfs',
                title: '核算方式',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'bz',
                title: '备注',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'ycqts',
                title: '应出勤天数',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false,
                formatter:ycqformat
            }, {
                field: 'cqts',
                title: '实际出勤天数',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'xxts',
                title: '休息天数',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
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
                visible: false
            }, {
                field: 'gzrjb',
                title: '工作日加班',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'xxrjb',
                title: '休息日加班',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'jjrjb',
                title: '节假日加班',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'kqjg',
                title: '考勤结果',
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
                yhkqxxDealById(row.kqid, 'view',$("#yhkqxxformSearch #btn_view").attr("tourl"));
            },
        });
        $("#yhkqxxformSearch #kqxx_list").colResizable({
                liveDrag:true,
                gripInnerHtml:"<div class='grip'></div>",
                draggingClass:"dragging",
                resizeMode:'fit',
                postbackSafe:true,
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
            sortLastName: "kqxx.cqsj", //防止同名排位用
            sortLastOrder: "desc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };
        return getKqxxSearchData(map);
    };
    return oTableInit;
}
/**
 * 应出勤格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function ycqformat(value,row,index){
    var html = "";
    if($('#ac_tk').length > 0){
        html += "<a href='javascript:void(0);' onclick=\"showAttendanceInfo('"+row.yhid+"')\">"+row.ycqts+"</a>";
    }else{
        html += row.ycqts;
    }
    return html;
}
function showAttendanceInfo(yhid){
    var url=$("#yhkqxxformSearch #urlPrefix").val()+"/attendance/attendance/pagedataViewAttendanceInfo?yhid="+yhid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'个人考勤信息',viewAttendanceConfig);
}
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
function getKqxxSearchData(map){
    var cxbt = $("#yhkqxxformSearch #cxbt").val();
    // 查询条件
    var cxnr = $.trim(jQuery('#yhkqxxformSearch #cxnr').val());
    // '0':'来源编号','1':'外部编号','2':'孕周','3':'体重','4':'备注'
    if (cxbt == "0") {
        map["yhmc"] = cxnr;
    }else if(cxbt == "1"){
        map["jgmc"] = cxnr;
    }else if(cxbt == "2"){
        map["qjlx"] = cxnr;
    }

    var rqstart = jQuery('#yhkqxxformSearch #rqstart').val();
    map["rqstart"] = rqstart;
    var rqend = jQuery('#yhkqxxformSearch #rqend').val();
    map["rqend"] = rqend;
    var cqsjstart = jQuery('#yhkqxxformSearch #cqsjstart').val();
    map["cqsjstart"] = cqsjstart;
    var cqsjend = jQuery('#yhkqxxformSearch #cqsjend').val();
    map["cqsjend"] = cqsjend;
    var tqsjstart = jQuery('#yhkqxxformSearch #tqsjstart').val();
    map["tqsjstart"] = tqsjstart;
    var tqsjend = jQuery('#yhkqxxformSearch #tqsjend').val();
    map["tqsjend"] = tqsjend;

    return map;
}
//判断时间是否过期
function judgeTime(time){
    var strtime = time.replace("/-/g", "/");//时间转换
//时间
    var date1=new Date(strtime);
//现在时间
    var date2=new Date();
//判断时间是否过期
    if(date1>date2){
        return true;
    }
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
        html += "<a href='javascript:void(0);' onclick=\"queryByqjcs('"+row.yhid+ "','"+row.rq+"')\" >"+row.qjcs+"</a>";
    }
    return html;
}
function queryByqjcs(yhid,rq){
    var url=$("#yhkqxxformSearch #urlPrefix").val()+"/attendance/attendance/pagedataViewLeave?yhid="+yhid+"&rq="+rq+"&access_token=" + $("#ac_tk").val();
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
var addYhkqxxConfig = {
    width		: "800px",
    modalName	: "AttendanceModal",
    formName	: "Yhkdxx_ajaxForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#Yhkdxx_ajaxForm").valid()){
                    $.alert("请填写完整信息！");
                    return false;
                }
                var rq=$("#Yhkdxx_ajaxForm #dkrq").val();
                var rqsj=$("#Yhkdxx_ajaxForm #rqsj").text();
                var cqsj=$("#Yhkdxx_ajaxForm #cqsj").val().slice(0,10);
                var tqsj=$("#Yhkdxx_ajaxForm #tqsj").val().slice(0,10);
                var qjkssj=$("#Yhkdxx_ajaxForm #qjkssj").val().slice(0,10);
                if(rq!=null){
                    if(judgeTime(rq)){
                        $.error('日期不得大于当前日期');
                        return false;
                    }
                    if(cqsj!=null&&cqsj!=""){
                        if(cqsj!=rq){
                            $.error('出勤时间必须与日期为同一天');
                            return false;
                        }
                    }
                    if(tqsj!=null&&tqsj!=""){
                        if(tqsj!=rq){
                            $.error('退勤时间必须与日期为同一天');
                            return false;
                        }
                    }
                    if(qjkssj!=null&&qjkssj!=""){
                        if(qjkssj!=rq){
                            $.error('请假开始时间要与日期为同一天');
                            return false;
                        }
                    }
                }else if(rqsj!=null){
                    if(cqsj!=null&&cqsj!=""){
                        if(cqsj!=rqsj){
                            $.error('出勤时间必须与日期为同一天');
                            return false;
                        }
                    }
                    if(tqsj!=null&&tqsj!=""){
                        if(tqsj!=rqsj){
                            $.error('退勤时间必须与日期为同一天');
                            return false;
                        }
                    }
                    if(qjkssj!=null&&qjkssj!=""){
                        if(qjkssj!=rqsj){
                            $.error('请假开始时间要与日期为同一天');
                            return false;
                        }
                    }
                }

                var $this = this;
                var opts = $this["options"]||{};

                $("#Yhkdxx_ajaxForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchYhkqxxResult();
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


var viewYhkqxxConfig = {
    width		: "600px",
    modalName	: "viewAttendanceModal",
    formName	: "viewYhkqxx_Form",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
var updatePersonalCheckConfig = {
    width		: "600px",
    modalName	: "updateCheckModal",
    formName	: "updateCheckForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success7 : {
            label : "确定",
            className : "btn-primary",
            callback : function() {
                if(!$("#updateCheckForm").valid()){
                    return false;
                }
                var sDate=$("#updateCheckForm #startdate").val();
                var eDate=$("#updateCheckForm #enddate").val();
                var sArr = sDate.split("-");
                var eArr = eDate.split("-");
                var sRDate = new Date(sArr[0], sArr[1], sArr[2]);
                var eRDate = new Date(eArr[0], eArr[1], eArr[2]);
                var result = (eRDate-sRDate)/(24*60*60*1000);
                if(result>31){
                    $.error('起止时间之间跨度不得超过一个月');
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#updateCheckForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"updateCheckForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchYhkqxxResult();
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

//按钮动作函数
function yhkqxxDealById(id,action,tourl){
    if(!tourl){
        return;
    }
    if(action =='view'){
        var url= tourl + "?kqid=" +id;
        $.showDialog(url,'查看用户',viewYhkqxxConfig);
    }else if(action =='add'){
        var url=tourl;
        $.showDialog(url,'新增用户',addYhkqxxConfig);
    }else if(action =='mod'){
        var url= tourl + "?kqid=" +id;
        $.showDialog(url,'编辑用户',addYhkqxxConfig);
    }else if(action =='updatecheck'){
        var url=tourl+ "?kqid=" +id;
        $.showDialog(url,'更新考勤信息',updatePersonalCheckConfig);
    }
}


var yhkqxx_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
        var btn_add = $("#yhkqxxformSearch #btn_add");
        var btn_mod = $("#yhkqxxformSearch #btn_mod");
        var btn_del = $("#yhkqxxformSearch #btn_del");
        var btn_view = $("#yhkqxxformSearch #btn_view");
        var btn_user_query = $("#btn_user_query");
        var btn_selectexport = $("#yhkqxxformSearch #btn_selectexport");//选中导出
        var btn_searchexport = $("#yhkqxxformSearch #btn_searchexport");//搜索导出
        var btn_updatecheck=$("#yhkqxxformSearch #btn_updatecheck");


        //添加日期控件
        laydate.render({
            elem: '#rqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#rqend'
            ,theme: '#2381E9'
        });
        laydate.render({
            elem: '#cqsjstart'
            , theme: '#2381E9'
            , type: 'time'
            , format: 'HH:mm:ss'//保留时分
        });
        laydate.render({
            elem: '#cqsjend'
            , theme: '#2381E9'
            , type: 'time'
            , format: 'HH:mm:ss'//保留时分
        });
        laydate.render({
            elem: '#tqsjstart'
            , theme: '#2381E9'
            , type: 'time'
            , format: 'HH:mm:ss'//保留时分
        });
        laydate.render({
            elem: '#tqsjend'
            , theme: '#2381E9'
            , type: 'time'
            , format: 'HH:mm:ss'//保留时分
        });
        //绑定搜索发送功能
        if(btn_user_query != null){
            btn_user_query.unbind("click").click(function(){
                searchYhkqxxResult(true);
            });
        }
        btn_add.unbind("click").click(function(){
            yhkqxxDealById(null,"add",btn_add.attr("tourl"));
        });
        btn_updatecheck.unbind("click").click(function(){
            var sel_row = $('#yhkqxxformSearch #kqxx_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                yhkqxxDealById(sel_row[0].kqid,"updatecheck",btn_updatecheck.attr("tourl"));
            }else if(sel_row.length>1){
                $.error("请选中一行");
            }else{
                yhkqxxDealById(null,"updatecheck",btn_updatecheck.attr("tourl"));
            }
        });

        btn_mod.unbind("click").click(function(){
            var sel_row = $('#yhkqxxformSearch #kqxx_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                yhkqxxDealById(sel_row[0].kqid,"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        btn_view.unbind("click").click(function(){
            var sel_row = $('#yhkqxxformSearch #kqxx_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                yhkqxxDealById(sel_row[0].kqid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        //---------------------------------选中导出---------------------------------------
        btn_selectexport.unbind("click").click(function(){
            var sel_row = $('#yhkqxxformSearch #kqxx_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length>=1){
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].kqid;
                }
                ids = ids.substr(1);
                $.showDialog(exportPrepareUrl+"?ywid=ATTENDENCE_SELECT&expType=select&ids="+ids
                    ,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
            }else{
                $.error("请选择数据");
            }
        });
        //搜索导出
        btn_searchexport.unbind("click").click(function(){
            $.showDialog(exportPrepareUrl+"?ywid=ATTENDENCE_SEARCH&expType=search&callbackJs=yhkqxxSearchData"
                ,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
        });

        btn_del.unbind("click").click(function(){
            var sel_row = $('#yhkqxxformSearch #kqxx_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                ids = ids + ","+ sel_row[i].kqid;
            }
            ids = ids.substr(1);
            $.confirm('您确定要删除所选择的记录吗？',function(result){
                if(result){
                    jQuery.ajaxSetup({async:false});
                    var url= btn_del.attr("tourl");
                    jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                        setTimeout(function(){
                            if(responseText["status"] == 'success'){
                                $.success(responseText["message"],function() {
                                    searchYhkqxxResult();
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
        $("#yhkqxxformSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(Yhkqxx_turnOff){
                $("#yhkqxxformSearch #searchMore").slideDown("low");
                Yhkqxx_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#yhkqxxformSearch #searchMore").slideUp("low");
                Yhkqxx_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });
    };

    return oInit;
};

function yhkqxxSearchData(){
    var map ={};
    map["access_token"]=$("#ac_tk").val();
    map["sortLastName"]="kqxx.cqsj";
    map["sortLastOrder"]="desc";
    map["sortName"]="kqxx.rq";
    map["sortOrder"]="desc";
    return getKqxxSearchData(map);
}


function searchYhkqxxResult(isTurnBack){
    //关闭高级搜索条件
    $("#yhkqxxformSearch #searchMore").slideUp("low");
    Yhkqxx_turnOff=true;
    $("#yhkqxxformSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#yhkqxxformSearch #kqxx_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#yhkqxxformSearch #kqxx_list').bootstrapTable('refresh');
    }
}

$(function(){

    //1.初始化Table
    var oTable = new yhkqxx_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new yhkqxx_ButtonInit();
    oButtonInit.Init();

    //所有下拉框添加choose样式
    jQuery('#yhkqxxformSearch .chosen-select').chosen({width: '100%'});
});


