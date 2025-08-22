var rosterViewKsxx_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#viewRoster_Form #ksxx_list').bootstrapTable({
            url: $("#viewRoster_Form #urlPrefix").val()+'/roster/roster/pagedataKxxx',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#viewRoster_Form #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "grksgl.lrsj",				//排序字段
            sortOrder: "desc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 15,                       //每页的记录行数（*）
            pageList: [15, 30, 50, 100],        //可供选择的每页的行数（*）
            paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            isForceTable: true,
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: false,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [
                {
                    title: '序号',
                    formatter: function (value, row, index) {
                        return index+1;
                    },
                    width: '3%',
                    align: 'left',
                    visible:true
                }, {
                    field: 'grksid',
                    title: '个人考试id',
                    width: '8%',
                    align: 'left',
                    visible: false
                }, {
                    field: 'xm',
                    title: '姓名',
                    width: '8%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'jgmc',
                    title: '所属机构',
                    width: '8%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'pxbt',
                    title: '培训标题',
                    width: '8%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'zf',
                    title: '考试分数',
                    width: '8%',
                    align: 'left',
                    visible: true,
                }],
            onLoadSuccess: function (map) {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                rosterViewDealById(row.grksid,'view','/train/test/pagedataTest');
            }
        });
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
            pageSize: 1,   //页面大小
            pageNumber: 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            yhid:$("#viewRoster_Form #yhid").val(),
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};
function rosterViewDealById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $("#viewRoster_Form #urlPrefix").val()+tourl;
    if(action =='view'){
        var url= tourl + "?grksid=" +id;
        $.showDialog(url,'查看考试信息',viewksxxConfig);
    }
}
/**
 * 查看页面模态框
 */
var viewksxxConfig={
    width		: "1000px",
    modalName	:"viewksxxModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
}

var train_turnOff=true;

var rosterViewpxxx_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#viewRoster_Form #pxxx_list').bootstrapTable({
            url: $("#viewRoster_Form #urlPrefix").val()+'/train/test/pagedataPxxx',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#viewRoster_Form #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "gzgl.qwwcsj",				//排序字段
            sortOrder: "desc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 15,                       //每页的记录行数（*）
            pageList: [15, 30, 50, 100],        //可供选择的每页的行数（*）
            paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            isForceTable: true,
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: false,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [
                {
                    title: '序号',
                    formatter: function (value, row, index) {
                        return index+1;
                    },
                    width: '3%',
                    align: 'left',
                    visible:true
                }, {
                    field: 'qwwcsj',
                    title: '培训时间',
                    width: '10%',
                    align: 'left',
                    visible: true,
                    sortable: true
                }, {
                    field: 'jlbh',
                    title: '记录编号',
                    width: '12%',
                    align: 'left',
                    visible: true,
                    sortable: true
                }, {
                    field: 'ks',
                    title: '课时/小时',
                    width: '10%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'pxfsmc',
                    title: '培训方式',
                    width: '8%',
                    align: 'left',
                    visible: true,
                }, {
                    field: 'ywmc',
                    title: '培训内容',
                    width: '26%',
                    align: 'left',
                    formatter: ywmcformat,
                    visible: true,
                }, {
                    field: 'tgbj',
                    title: '考核结果',
                    width: '8%',
                    align: 'left',
                    formatter: function (value, row, index) {
                        if(row.tgbj&&row.tgbj=="1"){
                            return "<span style='color:green;'>合格</span>";
                        }else {
                            return "<span style='color:red;'>不合格</span>";
                        }
                    },
                    visible: true,
                }, {
                    field: 'ssgsmc',
                    title: '所属公司',
                    width: '16%',
                    align: 'left',
                    visible: true,
                    sortable: true
                }, {
                    field: 'bz',
                    title: '备注',
                    width: '16%',
                    align: 'left',
                    visible: true,
                    sortable: true
                }],
            onLoadSuccess: function (map) {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
        });
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
            pageSize: 1,   //页面大小
            pageNumber: 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            fzr:$("#viewRoster_Form #yhid").val(),
            //搜索框使用
            //search:params.search
        };
        var cxnr=$.trim(jQuery('#viewRoster_Form #cxnr').val());
        if(cxnr){
            map["ywmc"]=cxnr
        }
        var tgbj = jQuery('#viewRoster_Form #tgbj_id_tj').val();
        map["tgbj"] = tgbj;
        // 期望完成开始日期
        var qwwcsjstart = jQuery('#viewRoster_Form #qwwcsjstart').val();
        map["qwwcsjstart"] = qwwcsjstart;

        // 期望完成结束日期
        var qwwcsjend = jQuery('#viewRoster_Form #qwwcsjend').val();
        map["qwwcsjend"] = qwwcsjend;
        var pxlbs = jQuery('#viewRoster_Form #pxlb_id_tj').val();
        map["pxlbs"] = pxlbs.replace(/'/g, "");
        var ssgss = jQuery('#viewRoster_Form #ssgs_id_tj').val();
        map["ssgss"] = ssgss.replace(/'/g, "");
        var pxfss = jQuery('#viewRoster_Form #pxfs_id_tj').val();
        map["pxfss"] = pxfss.replace(/'/g, "");
        return map;
    };
    return oTableInit;
};

function ywmcformat(value,row,index){
    if(row.ywid){
        return "<a href='javascript:void(0);' onclick=\"queryByYwid('"+row.ywid+"')\">"+row.ywmc+"</a>";
    }else{
        return row.ywmc
    }
}

function queryByYwid(ywid){
    var urlPrefix = $("#viewRoster_Form #urlPrefix").val();
    var url=urlPrefix+"/train/test/viewTrain?pxid="+ywid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'培训信息',viewTrainConfig);
}
var viewTrainConfig = {
    width		: "1400px",
    modalName	:"viewTrainModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var htcolumnsArray = [
    {
        title: '序号',
        formatter: function (value, row, index) {
            return index+1;
        },
        titleTooltip:'序号',
        width: '3%',
        align: 'left',
        visible:true
    }, {
        field: 'htqsr',
        title: '合同起始日',
        titleTooltip:'合同起始日',
        width: '15%',
        align: 'left',
        visible: true
    }, {
        field: 'htdqr',
        title: '合同到期日',
        titleTooltip:'合同到期日',
        width: '15%',
        align: 'left',
        visible: true
    },{
        field: 'fjids',
        title: '附件',
        width: '3%',
        align: 'left',
        formatter:htfjformat,
    }];
var yghtxxView_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#viewRoster_Form #httb_list').bootstrapTable({
            url:$('#viewRoster_Form #urlPrefix').val()+'/roster/roster/pagedataGetHtxxById?yghmcid='+$('#viewRoster_Form #yghmcid').val(),         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#viewRoster_Form #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "htqsr",				        //排序字段
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
            isForceTable: true,
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: false,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "yghtid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: htcolumnsArray,
            onLoadSuccess: function (map) {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                return;
            },
        });
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
            pageSize: 1,   //页面大小
            pageNumber: 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "htdqr", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};

var jqcolumnsArray = [
    {
        title: '序号',
        formatter: function (value, row, index) {
            return index+1;
        },
        titleTooltip:'序号',
        width: '2%',
        align: 'left',
        visible:true
    }, {
        field: 'jqlxmc',
        title: '假期类型',
        titleTooltip:'假期类型',
        width: '3%',
        align: 'left',
    },{
        field: 'dw',
        title: '单位',
        titleTooltip:'单位',
        width: '2%',
        align: 'left',
        visible: true,
        formatter:dwformat
    },{
        field: 'nd',
        title: '年度',
        titleTooltip:'年度',
        width: '2%',
        align: 'left',
        visible: true
    },{
        field: 'kssj',
        title: '开始时间',
        titleTooltip:'开始时间',
        width: '5%',
        align: 'left',
        visible: true
    },{
        field: 'yxq',
        title: '有效期',
        titleTooltip:'有效期',
        width: '5%',
        align: 'left',
        visible: true
    },{
        field: 'jqze',
        title: '假期总额',
        titleTooltip:'假期总额',
        width: '3%',
        align: 'left',
        visible: true
    }, {
        field: 'yyed',
        title: '已用额度',
        titleTooltip:'已用额度',
        width: '3%',
        align: 'left',
        visible: true
    },{
        field: 'syed',
        title: '剩余额度',
        titleTooltip:'剩余额度',
        width: '3%',
        align: 'left',
        visible: true
    },{
        field: 'ddze',
        title: '钉钉总额',
        titleTooltip:'钉钉总额',
        width: '3%',
        align: 'left',
        visible: true
    },{
        field: 'ddyyed',
        title: '钉钉已用额度',
        titleTooltip:'钉钉已用额度',
        width: '6%',
        align: 'left',
        visible: true
    },{
        field: 'ddsyed',
        title: '钉钉剩余额度',
        titleTooltip:'钉钉剩余额度',
        width: '6%',
        align: 'left',
        visible: true
    },{
        field: 'yeqksj',
        title: '余额清空时间',
        titleTooltip:'余额清空时间',
        width: '5%',
        align: 'left',
        visible: true
    }];
var vacationView_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#viewRoster_Form #jqtb_list').bootstrapTable({
            url:$('#viewRoster_Form #urlPrefix').val()+'/vacation/vacation/pagedataGetVacationGrant',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#viewRoster_Form #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "yhjq.nd",				        //排序字段
            sortOrder: "desc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 15,                       //每页的记录行数（*）
            pageList: [15, 30, 50, 100],        //可供选择的每页的行数（*）
            paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            isForceTable: true,
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: false,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "yhjqid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: jqcolumnsArray,
            onLoadSuccess: function (map) {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                return;
            },
        });
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
            pageSize: 1,   //页面大小
            pageNumber: 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "yhjq.nd", //防止同名排位用
            sortLastOrder: "desc", //防止同名排位用
            yhid: $("#viewRoster_Form #yhid").val()
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};

function dwformat(value,row,index){
    var html = "";
    if(row.dw == "0"){
        html = "小时";
    }
    if(row.dw == "1"){
        html = "天";
    }
    return html;
}

/**
 * 附件格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function htfjformat(value,row,index){
    var html = "<div id='fj_"+index+"'>";
    html += "<input type='hidden' id='htfj_"+index+"' name='htfj_"+index+"'/>";
    if(row.fjbj == "1"){
        html += "<a href='javascript:void(0);' onclick='viewfj(\"" + index + "\",\""+row.yghtid+"\")' >是</a>";
    }else{
        html += "<a href='javascript:void(0);' onclick='viewfj(\"" + index + "\",\""+row.yghtid+"\")' >否</a>";
    }
    html += "</div>";
    return html;
}
var lzcolumnsArray = [
    {
        title: '序号',
        formatter: function (value, row, index) {
            return index+1;
        },
        titleTooltip:'序号',
        width: '3%',
        align: 'left',
        visible:true
    }, {
        field: 'rzrq',
        title: '入职日期',
        titleTooltip:'入职日期',
        width: '15%',
        align: 'left',
        visible: true
    }, {
        field: 'lzrq',
        title: '离职日期',
        titleTooltip:'离职日期',
        width: '15%',
        align: 'left',
        visible: true
    },{
        field: 'fjids',
        title: '附件',
        width: '3%',
        align: 'left',
        formatter:lzfjformat,
    }];
var yglzxxView_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#viewRoster_Form #lztb_list').bootstrapTable({
            url:$('#viewRoster_Form #urlPrefix').val()+'/roster/roster/pagedataGetLzxxById?yghmcid='+$('#viewRoster_Form #yghmcid').val(),         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#viewRoster_Form #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "rzrq",				        //排序字段
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
            isForceTable: true,
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: false,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "yglzid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: lzcolumnsArray,
            onLoadSuccess: function (map) {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                return;
            },
        });
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
            pageSize: 1,   //页面大小
            pageNumber: 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "lzrq", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};

/**
 * 附件格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function lzfjformat(value,row,index){
    var html = "<div id='fj_"+index+"'>";
    html += "<input type='hidden' id='lzfj_"+index+"' name='lzfj_"+index+"'/>";
    if(row.fjbj == "1"){
        html += "<a href='javascript:void(0);' onclick='viewfj(\"" + index + "\",\""+row.yglzid+"\")' >是</a>";
    }else{
        html += "<a href='javascript:void(0);' onclick='viewfj(\"" + index + "\",\""+row.yglzid+"\")' >否</a>";
    }
    html += "</div>";
    return html;
}
function viewfj(index,zywid) {
    var url="/storehouse/requisition/pagedataViewFj?access_token=" + $("#ac_tk").val()+"&zywid="+zywid+"&ywid="+$("#viewRoster_Form #yghmcid").val();
    $.showDialog($("#viewRoster_Form #urlPrefix").val()+url, '查看附件', viewFileConfig);
}

var viewFileConfig = {
    width : "800px",
    height : "500px",
    modalName	: "uploadFileModal",
    formName	: "viewRoster_Form",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var $this = this;
                var opts = $this["options"]||{};
                $.closeModal(opts.modalName);
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
var jkda_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#viewRoster_Form #jkda_list").bootstrapTable({
            url:   $('#viewRoster_Form #urlPrefix').val()+'/roster/roster/pagedataGetHealth',         //请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#viewRoster_Form #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   // 是否显示分页（*）
            paginationShowPageGo: false,         //增加跳转页码的显示
            sortable: false,                     // 是否启用排序
            sortName:"jkda.jcsj",					// 排序字段
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
            showColumns: false,                  // 是否显示所有的列
            showRefresh: false,                  // 是否显示刷新按钮
            minimumCountColumns: 2,             // 最少允许的列数
            clickToSelect: true,                // 是否启用点击选中行
            // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "jkdaid",                     // 每一行的唯一标识，一般为主键列
            showToggle:false,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns:  [
                {
                    checkbox: true,
                    width: '3%'
                }
                ,{
                    title: '序号',
                    formatter: function (value, row, index) {
                        return index+1;
                    },
                    width: '5%',
                    align: 'left',
                    visible:true
                }, {
                    field: 'jkdaid',
                    title: '健康答案id',
                    width: '6%',
                    align: 'left',
                    visible: false
                 },{
                    field: 'jcxm',
                    title: '检查项目',
                    width: '15%',
                    align: 'left',
                    visible: true
                },{
                    field: 'bm',
                    title: '部门',
                    width: '10%',
                    align: 'left',
                    visible: true
                },{
                    field: 'gwmc',
                    title: '岗位名称',
                    width: '15%',
                    align: 'left',
                    visible: true
                },{
                    field: 'jcsj',
                    title: '检查时间',
                    width: '10%',
                    align: 'left',
                    visible: true
                },{
                    field: 'jcjg',
                    title: '检查结果',
                    width: '15%',
                    align: 'left',
                    visible: true
                },{
                    field: 'clyj',
                    title: '处理意见',
                    width: '35%',
                    align: 'left',
                    visible: true
                }],
            onLoadSuccess:function(map){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            },
            onPostBody:function(){
            },
        });
    };
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            access_token:$("#ac_tk").val(),
            yghmcid: $('#viewRoster_Form #yghmcid').val()
            // 搜索框使用
            // search:params.search
        };
        return map;
    };
    return oTableInit;
}



$(document).ready(function(){
    var oTableTest=new rosterViewKsxx_TableInit();
    oTableTest.Init();
    // 初始化页面
    var oTableTrain =rosterViewpxxx_TableInit();
    oTableTrain.Init();
    var yghtxxViewTableInit=new yghtxxView_TableInit();
    yghtxxViewTableInit.Init();
    var yglzxxViewTableInit=new yglzxxView_TableInit();
    yglzxxViewTableInit.Init();
    var vacationViewTableInit=new vacationView_TableInit();
    vacationViewTableInit.Init();
    var jkdaTableInit=new jkda_TableInit();
    jkdaTableInit.Init();
    //添加日期控件
    laydate.render({
        elem: '#viewRoster_Form #qwwcsjstart'
        ,theme: '#2381E9'
    });
    //添加日期控件
    laydate.render({
        elem: '#viewRoster_Form #qwwcsjend'
        ,theme: '#2381E9'
    });
    $("#viewRoster_Form #sl_searchMore").on("click", function(ev){
        var ev=ev||event;
        if(train_turnOff){
            $("#viewRoster_Form #searchMore").slideDown("low");
            train_turnOff=false;
            this.innerHTML="基本筛选";
        }else{
            $("#viewRoster_Form #searchMore").slideUp("low");
            train_turnOff=true;
            this.innerHTML="高级筛选";
        }
        ev.cancelBubble=true;
    });
    $("#viewRoster_Form #btn_query").unbind("click").click(function(){
        $('#viewRoster_Form #pxxx_list').bootstrapTable('refresh');
    });
});


