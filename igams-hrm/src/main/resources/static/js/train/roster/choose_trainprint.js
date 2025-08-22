var trainPrint_turnOff=true;
var rosterPrintpxxx_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#chooseTrainPrint_Form #pxxx_list').bootstrapTable({
            url: $("#chooseTrainPrint_Form #urlPrefix").val()+'/train/test/pagedataPxxx',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#chooseTrainPrint_Form #toolbar',                //工具按钮用哪个容器
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
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [
                {
                    checkbox: true,
                    width: '2%',
                    align: 'center',

                }, {
                    title: '序号',
                    formatter: function (value, row, index) {
                        return index+1;
                    },
                    width: '3%',
                    align: 'left',
                    visible:true
                }, {
                    field: 'gzid',
                    title: '工作id',
                    width: '12%',
                    align: 'left',
                    visible: false,
                    sortable: false
                }, {
                    field: 'qwwcsj',
                    title: '培训时间',
                    width: '12%',
                    align: 'left',
                    visible: true,
                    sortable: true
                }, {
                    field: 'jlbh',
                    title: '记录编号',
                    width: '15%',
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
                    width: '10%',
                    align: 'left',
                    visible: true,
                }, {
                    field: 'ywmc',
                    title: '培训内容',
                    width: '26%',
                    align: 'left',
                    visible: true,
                }, {
                    field: 'sfnd',
                    title: '是否年度',
                    width: '8%',
                    align: 'left',
                    formatter: function (value, row, index) {
                        if(row.sfnd=="1"){
                            return "<span>是</span>";
                        }else {
                            return "<span>否</span>";
                        }
                    },
                    visible: true,
                }, {
                    field: 'tgbj',
                    title: '考核结果',
                    width: '10%',
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
            fzr:$("#chooseTrainPrint_Form #yhid").val(),
            //搜索框使用
            //search:params.search
        };
        var tgbj = jQuery('#chooseTrainPrint_Form #tgbj_id_tj').val();
        map["tgbj"] = tgbj;
        // 期望完成开始日期
        var qwwcsjstart = jQuery('#chooseTrainPrint_Form #qwwcsjstart').val();
        map["qwwcsjstart"] = qwwcsjstart;

        // 期望完成结束日期
        var qwwcsjend = jQuery('#chooseTrainPrint_Form #qwwcsjend').val();
        map["qwwcsjend"] = qwwcsjend;
        var pxlbs = jQuery('#chooseTrainPrint_Form #pxlb_id_tj').val();
        map["pxlbs"] = pxlbs.replace(/'/g, "");
        var ssgss = jQuery('#chooseTrainPrint_Form #ssgs_id_tj').val();
        map["ssgss"] = ssgss.replace(/'/g, "");
        var pxfss = jQuery('#chooseTrainPrint_Form #pxfs_id_tj').val();
        map["pxfss"] = pxfss.replace(/'/g, "");
        var sfnd = jQuery('#chooseTrainPrint_Form #sfnd_id_tj').val();
        map["sfnd"] = sfnd;
        return map;
    };
    return oTableInit;
};

$(document).ready(function(){
    // 初始化页面
    var oTableTrain =rosterPrintpxxx_TableInit();
    oTableTrain.Init();
    //添加日期控件
    laydate.render({
        elem: '#chooseTrainPrint_Form #qwwcsjstart'
        ,theme: '#2381E9'
    });
    //添加日期控件
    laydate.render({
        elem: '#chooseTrainPrint_Form #qwwcsjend'
        ,theme: '#2381E9'
    });
    $("#chooseTrainPrint_Form #sl_searchMore").on("click", function(ev){
        var ev=ev||event;
        if(trainPrint_turnOff){
            $("#chooseTrainPrint_Form #searchMore").slideDown("low");
            trainPrint_turnOff=false;
            this.innerHTML="基本筛选";
        }else{
            $("#chooseTrainPrint_Form #searchMore").slideUp("low");
            trainPrint_turnOff=true;
            this.innerHTML="高级筛选";
        }
        ev.cancelBubble=true;
    });
    $("#chooseTrainPrint_Form #btn_query").unbind("click").click(function(){
        $('#chooseTrainPrint_Form #pxxx_list').bootstrapTable('refresh');
    });
});