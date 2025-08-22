//检测结果
var JnsjhzxxView_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function (){
        $('#jnsjhzxxView_formSearch #jcjg_list').bootstrapTable({
            url: '/crf/cdiff/getjcjgList?jnsjbgid='+$("#jnsjhzxxView_formSearch #jnsjbgid").val(),         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#jnsjhzxxView_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "jcjg.jnsjjcsj",				//排序字段
            sortOrder: "DESC",                   //排序方式
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
            uniqueId: "fzjcid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            pagination:false,
            columns:[{
                field: 'jnsjjcsj',
                title: '艰难梭菌检测时间',
                titleTooltip:'艰难梭菌检测时间',
                width: '10%',
                align: 'left',
                visible:true
            },{
                field: 'gdhjg',
                title: 'GDH结果',
                titleTooltip:'GDH结果',
                width: '10%',
                align: 'left',
                formatter:jcjgformat,
                visible:true
            },{
                field: 'toxinjg',
                title: 'TOXIN结果',
                titleTooltip:'TOXIN结果',
                width: '10%',
                align: 'left',
                formatter:jcjgformat,
                visible:true
            },{
                field: 'dbjnsjpyjg',
                title: '大便艰难梭菌培养结果',
                titleTooltip:'大便艰难梭菌培养结果',
                width: '10%',
                align: 'left',
                formatter:jcjgformat,
                visible:true
            },{
                field: 'jnsjmspyjg',
                title: '艰难梭菌毒素培养结果',
                titleTooltip:'艰难梭菌毒素培养结果',
                width: '10%',
                align: 'left',
                formatter:jcjgformat,
                visible:true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            // onDblClickRow: function (row, $element) {
            //     CovidDealById(row.fzjcid,'view',$("#jnsjhzxxView_formSearch #btn_view").attr("tourl"));
            // },
        });
        $("#jnsjhzxxView_formSearch #covid_list").colResizable({
                liveDrag:true,
                gripInnerHtml:"<div class='grip'></div>",
                draggingClass:"dragging",
                resizeMode:'fit',
                postbackSafe:true,
                partialRefresh:true
            }
        );
    };
    // 得到查询的参数
    oTableInit.queryParams = function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            access_token:$("#ac_tk").val(),
            ybbh: $("#ybbh").val()
        };
        return map;
    };
    return oTableInit;
}
//检测结果格式化
function jcjgformat(value,row,index){
    if(row.gdhjg=='0'){
        return '阴性';
    }else if(row.gdhjg=='1'){
        return '阳性';
    }else if(row.toxinjg=='0'){
        return '阴性';
    }else if(row.toxinjg=='1'){
        return '阳性';
    }else if(row.dbjnsjpyjg=='0'){
        return '阴性';
    }else if(row.dbjnsjpyjg=='1'){
        return '阳性';
    }else if(row.jnsjmspyjg=='0'){
        return '阴性';
    }else if(row.jnsjmspyjg=='1'){
        return '阳性';
    }else {
        return value;
    }
}

//CDI治疗
var Jnsjcdizl_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function (){
        $('#jnsjhzxxView_formSearch #cdizl_list').bootstrapTable({
            url: '/crf/cdiff/getjnsjCdizlList?jnsjbgid='+$("#jnsjhzxxView_formSearch #jnsjbgid").val(),         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#jnsjhzxxView_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "cdizl.ksrq",				//排序字段
            sortOrder: "DESC",                   //排序方式
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
            uniqueId: "fzjcid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            pagination:false,
            columns:[{
                field: 'zlyw',
                title: '治疗药物',
                titleTooltip:'治疗药物',
                width: '10%',
                align: 'left',
                visible:true
            },{
                field: 'dcjl',
                title: '单次剂量',
                titleTooltip:'单次剂量',
                width: '5%',
                align: 'left',
                visible:true
            },{
                field: 'syff',
                title: '使用方法',
                titleTooltip:'使用方法',
                width: '5%',
                align: 'left',
                visible:true
            },{
                field: 'ksrq',
                title: '开始时间',
                titleTooltip:'开始时间',
                width: '10%',
                align: 'left',
                visible:true
            },{
                field: 'jsrq',
                title: '结束时间',
                titleTooltip:'结束时间',
                width: '10%',
                align: 'left',
                visible:true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            // onDblClickRow: function (row, $element) {
            //     CovidDealById(row.fzjcid,'view',$("#jnsjhzxxView_formSearch #btn_view").attr("tourl"));
            // },
        });
        $("#jnsjhzxxView_formSearch #covid_list").colResizable({
                liveDrag:true,
                gripInnerHtml:"<div class='grip'></div>",
                draggingClass:"dragging",
                resizeMode:'fit',
                postbackSafe:true,
                partialRefresh:true
            }
        );
    };
    // 得到查询的参数
    oTableInit.queryParams = function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            access_token:$("#ac_tk").val(),
            ybbh: $("#ybbh").val()
        };
        return map;
    };
    return oTableInit;
}

//CDI之前用药历史
var Jnsjcdizqyys_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function (){
        $('#jnsjhzxxView_formSearch #cdizqyyls_list').bootstrapTable({
            url: '/crf/cdiff/getjnsjCdiyylsList?jnsjbgid='+$("#jnsjhzxxView_formSearch #jnsjbgid").val(),         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#jnsjhzxxView_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "zqyys.ksrq",				//排序字段
            sortOrder: "DESC",                   //排序方式
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
            uniqueId: "fzjcid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            pagination:false,
            columns:[{
                field: 'ywlx',
                title: '药物类型',
                titleTooltip:'药物类型',
                width: '10%',
                align: 'left',
                formatter: ywlxformatter,
                visible:true
            },{
                field: 'ywzlx',
                title: '药物子类型',
                titleTooltip:'药物子类型',
                width: '10%',
                align: 'left',
                visible:true
            },{
                field: 'yw',
                title: '药物',
                titleTooltip:'药物',
                width: '10%',
                align: 'left',
                visible:true
            },{
                field: 'jl',
                title: '剂量',
                titleTooltip:'剂量',
                width: '10%',
                align: 'left',
                visible:true
            },{
                field: 'ksrq',
                title: '开始用药时间',
                titleTooltip:'开始用药时间',
                width: '15%',
                align: 'left',
                visible:true
            },{
                field: 'tyrq',
                title: '停药时间',
                titleTooltip:'停药时间',
                width: '15%',
                align: 'left',
                visible:true
            },{
                field: 'qtywmc',
                title: '其他药物名称',
                titleTooltip:'其他药物名称',
                width: '10%',
                align: 'left',
                visible:true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            // onDblClickRow: function (row, $element) {
            //     CovidDealById(row.fzjcid,'view',$("#jnsjhzxxView_formSearch #btn_view").attr("tourl"));
            // },
        });
        $("#jnsjhzxxView_formSearch #covid_list").colResizable({
                liveDrag:true,
                gripInnerHtml:"<div class='grip'></div>",
                draggingClass:"dragging",
                resizeMode:'fit',
                postbackSafe:true,
                partialRefresh:true
            }
        );
    };
    // 得到查询的参数
    oTableInit.queryParams = function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            access_token:$("#ac_tk").val(),
            ybbh: $("#ybbh").val()
        };
        return map;
    };
    return oTableInit;
}
//格式化药物类型
function ywlxformatter(value,row,index){
    if(row.ywlx=='1'){
        return 'β内酰胺类抗生素';
    }else if(row.ywlx=='2'){
        return '大环内酯类抗生素';
    }else if(row.ywlx=='3'){
        return '氨基甙类抗生素';
    }else if(row.ywlx=='4'){
        return '四环素类抗生素';
    }else if(row.ywlx=='5'){
        return '林可霉素类抗生素';
    }else if(row.ywlx=='6'){
        return '氯霉素类抗生素';
    }else if(row.ywlx=='7'){
        return '万古霉素类';
    }else if(row.ywlx=='8'){
        return '喹诺酮类药物';
    }else if(row.ywlx=='9'){
        return '质子泵抑制剂(抑酸类药物)';
    }else if(row.ywlx=='10'){
        return '细胞毒性类药物（抗肿瘤）生物碱类';
    }else if(row.ywlx=='11'){
        return '细胞毒性类药物（抗肿瘤）代谢类';
    }else if(row.ywlx=='12'){
        return '细胞毒性类药物（抗肿瘤）抗生素类';
    }else if(row.ywlx=='13'){
        return '细胞毒性类药物（抗肿瘤）烷化剂类';
    }else if(row.ywlx=='14'){
        return '细胞毒性类药物（抗肿瘤）铂剂类';
    }else {
        return value;
    }
}

$(function(){
    //0.界面初始化
    // 1.初始化Table
    var oTable = new JnsjhzxxView_TableInit();
    oTable.Init();
    var oTable = new Jnsjcdizl_TableInit();
    oTable.Init();
    var oTable = new Jnsjcdizqyys_TableInit();
    oTable.Init();
    $("#cdizqyyls_list").bootstrapTable('toggleView',true);
    // 所有下拉框添加choose样式
    jQuery('#jnsjhzxxView_formSearch .chosen-select').chosen({width: '100%'});
});