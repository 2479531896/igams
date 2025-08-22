var pcrinfo_turnOff=true;
var Pcrinfo_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#pcrinfo_formSearch #pcrinfo_list").bootstrapTable({
            url: $("#pcrinfo_formSearch #urlPrefix").val()+'/pcrinfo/pcrinfo/pageGetListPcrinfo',
            method: 'get',                      // 请求方式（*）
            toolbar: '#pcrinfo_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "pcrsygl.sysj",				// 排序字段
            sortOrder: "DESC",                   // 排序方式
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
            uniqueId: "pcrsyglid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%'
            },{
                field: 'pcrsyglid',
                title: 'pcr实验id',
                titleTooltip:'pcr实验id',
                width: '40%',
                align: 'left',
                visible: false
            },{
                field: 'sysj',
                title: '实验时间',
                titleTooltip:'实验时间',
                width: '40%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'jcdwmc',
                title: '检测单位',
                titleTooltip:'检测单位',
                width: '30%',
                align: 'left',
                sortable: true,
                visible:true,
            },{
                field: 'jcdw',
                title: '检测单位id',
                titleTooltip:'检测单位id',
                width: '30%',
                align: 'left',
                visible:false,
            },{
                field: 'isyz',
                title: '验证/测试',
                titleTooltip:'验证/测试',
                width: '30%',
                align: 'left',
                visible:true,
                formatter:pcrisyzFormat,

            //     field: 'samplename',
            //     title: '标本名称',
            //     titleTooltip:'标本名称',
            //     width: '10%',
            //     align: 'left',
            //     visible: true
            // },{
            //     field: 'sampletype',
            //     title: '标本类型',
            //     titleTooltip:'标本类型',
            //     width: '6%',
            //     align: 'left',
            //     visible:true,
            //     formatter:pcrbblxFormat,
            // },{
            //     field: 'dyename',
            //     title: '通道',
            //     titleTooltip:'通道',
            //     width: '6%',
            //     align: 'left',
            //     visible:false
            // },{
            //     field: 'cy5',
            //     title: 'cy5通道',
            //     titleTooltip:'cy5通道',
            //     width: '6%',
            //     align: 'left',
            //     visible:true
            // },{
            //     field: 'fam',
            //     title: 'fam通道',
            //     titleTooltip:'fam通道',
            //     width: '6%',
            //     align: 'left',
            //     visible:true
            // },{
            //     field: 'rox',
            //     title: 'rox通道',
            //     titleTooltip:'rox通道',
            //     width: '6%',
            //     align: 'left',
            //     visible:true
            // },{
            //     field: 'hex',
            //     title: 'hex通道',
            //     titleTooltip:'hex通道',
            //     width: '6%',
            //     align: 'left',
            //     visible:true
            // },{
            //     field: 'dlysymxid',
            //     title: '荧光定量仪结果id',
            //     titleTooltip:'荧光定量仪结果id',
            //     width: '6%',
            //     align: 'left',
            //     visible:false
            // },{
            //     field: 'ctvalue',
            //     title: 'ct值',
            //     titleTooltip:'ct值',
            //     width: '6%',
            //     align: 'left',
            //     visible: true
            // },{
            //     field: 'stdconcentration',
            //     title: '标准浓度',
            //     titleTooltip:'标准浓度',
            //     width: '6%',
            //     align: 'left',
            //     visible: true
            // },{
            //     field: 'concentration',
            //     title: '浓度',
            //     titleTooltip:'浓度',
            //     width: '6%',
            //     align: 'left',
            //     visible:true,
            // },{
            //     field: 'concentrationunit',
            //     title: '浓度单位',
            //     titleTooltip:'浓度单位',
            //     width: '6%',
            //     align: 'left',
            //     visible:true
            // },{
            //     field: 'referencedye',
            //     title: '荧光',
            //     titleTooltip:'荧光',
            //     width: '6%',
            //     align: 'left',
            //     visible:false,
            //
            // },{
            //     field: 'sampleuid',
            //     title: '唯一编号',
            //     titleTooltip:'唯一编号',
            //     width: '6%',
            //     align: 'left',
            //     visible:false
            // },{
            //     field: 'replicatedgroup',
            //     title: '重复组',
            //     titleTooltip:'重复组',
            //     width: '6%',
            //     align: 'left',
            //     visible: false
            // },{
            //     field: 'qcresult',
            //     title: '结果',
            //     titleTooltip:'结果',
            //     width: '6%',
            //     align: 'left',
            //     visible:false,
            //
            // },{
            //     field: 'wellhindex',
            //     title: '横坐标',
            //     titleTooltip:'横坐标',
            //     width: '6%',
            //     align: 'left',
            //     visible:false
            // },{
            //     field: 'wellvindex',
            //     title: '纵坐标',
            //     titleTooltip:'纵坐标',
            //     width: '6%',
            //     align: 'left',
            //     visible: false
            // },{
            //     field: 'well',
            //     title: '孔号',
            //     titleTooltip:'孔号',
            //     width: '6%',
            //     align: 'left',
            //     visible:true,
            // },{
            //     field: 'samplenumber',
            //     title: '标本编号',
            //     titleTooltip:'标本编号',
            //     width: '6%',
            //     align: 'left',
            //     visible:false
            // },{
            //     field: 'genename',
            //     title: '目标基因',
            //     titleTooltip:'目标基因',
            //     width: '6%',
            //     align: 'left',
            //     visible: true
            // },{
            //     field: 'jcdwmc',
            //     title: '检测单位',
            //     titleTooltip:'检测单位',
            //     width: '10%',
            //     align: 'left',
            //     visible:true,
            // },{
            //     field: 'isyz',
            //     title: '验证/测试',
            //     titleTooltip:'验证/测试',
            //     width: '6%',
            //     align: 'left',
            //     visible:true,
            //     formatter:pcrisyzFormat,
            // },{
            //     field: 'jssj',
            //     title: '实验时间',
            //     titleTooltip:'实验时间',
            //     width: '10%',
            //     align: 'left',
            //     visible:true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                Pcrinfo_DealById(row.pcrsyglid,'view',$("#pcrinfo_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#pcrinfo_formSearch #pcrinfo_list").colResizable({
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
            sortLastName: "pcrsygl.pcrsyglid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getPcrinfoSearchData(map);
    };
    return oTableInit;
}

function pcrbblxFormat(value,row,index){
    if (row.sampletype == '1') {
        return '待测';
    }else if(row.sampletype == '2'){
        return '标准';
    }else if(row.sampletype == '3'){
        return '阳性对照';
    }else if(row.sampletype == '4'){
        return '阴性对照';
    }else {
        return '未知';
    }
}

//是否验证格式化
function pcrisyzFormat(value,row,index){
    if (row.isyz == '0') {
        return '验证';
    }else{
        return '测试';
    }
}

function getPcrinfoSearchData(map){
    var pcrinfo_select=$("#pcrinfo_formSearch #pcrinfo_select").val();
    var pcrinfo_input=$.trim(jQuery('#pcrinfo_formSearch #pcrinfo_input').val());
    if(pcrinfo_select=="0"){
        map["samplename"]=pcrinfo_input
    }
    // 通过开始日期
    var sysjstart = jQuery('#pcrinfo_formSearch #sysjstart').val();
    map["sysjstart"] = sysjstart;
    // 通过结束日期
    var sysjend = jQuery('#pcrinfo_formSearch #sysjend').val();
    map["sysjend"] = sysjend;
    //检测单位
    var jcdws= jQuery('#pcrinfo_formSearch #jcdw_id_tj').val();
    map["jcdws"] = jcdws.replace(/'/g, "");;
    //测试验证
    var isyzs= jQuery('#pcrinfo_formSearch #isyz_id_tj').val();
    map["isyzs"] = isyzs.replace(/'/g, "");;
    return map;
}

function searchPcrinfoResult(isTurnBack){
    if(isTurnBack){
        $('#pcrinfo_formSearch #pcrinfo_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#pcrinfo_formSearch #pcrinfo_list').bootstrapTable('refresh');
    }
}

function Pcrinfo_DealById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $("#pcrinfo_formSearch #urlPrefix").val() + tourl;
    if(action=="view"){
        var url=tourl+"?pcrsyglid="+id
        $.showDialog(url,'查看定量仪实验结果',viewPcrinfoConfig);
    }
}

var viewPcrinfoConfig = {
    width		: "1600px",
    modalName	:"viewPcrinfoModal",
    offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var Pcrinfo_oButton=function(){
    var oButtonInit = new Object();
    oButtonInit.Init=function(){
        var btn_query=$("#pcrinfo_formSearch #btn_query");
        var btn_view = $("#pcrinfo_formSearch #btn_view");
        //添加日期控件
        laydate.render({
            elem: '#pcrinfo_formSearch #sysjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#pcrinfo_formSearch #sysjend'
            ,theme: '#2381E9'
        });
        /*-----------------------高级筛选-------------------------------------*/
        $("#pcrinfo_formSearch #sl_searchMore").on("click",function(ev){
            var ev=ev||event;
            if(pcrinfo_turnOff){
                $("#pcrinfo_formSearch #searchMore").slideDown("low");
                pcrinfo_turnOff = false;
                this.innerHTML="基本筛选";
            }else{
                $("#pcrinfo_formSearch #searchMore").slideUp("low");
                pcrinfo_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });
        /*-----------------------模糊查询ok------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchPcrinfoResult(true);
            });
        }
        /*-----------------------查看------------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#pcrinfo_formSearch #pcrinfo_list').bootstrapTable('getSelections');// 获取选择行数据
            if(sel_row.length==1){
                Pcrinfo_DealById(sel_row[0].dlysymxid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*-------------------------------------------------------------*/
    }
    return oButtonInit;
}


$(function(){
    var oTable = new Pcrinfo_TableInit();
    oTable.Init();

    var oButton = new Pcrinfo_oButton();
    oButton.Init();

    jQuery('#pcrinfo_formSearch .chosen-select').chosen({width: '100%'});

    $("#pcrinfo_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
})