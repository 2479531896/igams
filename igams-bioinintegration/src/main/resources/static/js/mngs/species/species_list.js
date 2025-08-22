
// 列表初始化
var species_TableInit=function(){
    var oTableInit=new Object();
    // 初始化table
    oTableInit.Init=function(){
        $("#species_formSearch #species_list").bootstrapTable({
            url: '/species/species/pageGetListSpecies',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#species_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "wzid",					//排序字段
            sortOrder: "asc",               	//排序方式
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
            //height: 500,                     	//行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "wzid",                	//每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                	//是否显示父子表
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
                field: 'wzid',
                title: '物种ID',
                titleTooltip:'物种ID',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'wzywm',
                title: '物种英文名',
                titleTooltip:'物种英文名',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'wzzwm',
                title: '物种中文名',
                titleTooltip:'物种中文名',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'wzfl',
                title: '物种分类',
                titleTooltip:'物种分类',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'wzlx',
                title: '物种类型',
                titleTooltip:'物种类型',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'fldj',
                title: '分类等级',
                titleTooltip:'分类等级',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'flmc',
                title: '分类',
                titleTooltip:'分类',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'xjlb',
                title: '细菌类别',
                titleTooltip:'细菌类别',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'bdlb',
                title: '病毒类别',
                titleTooltip:'病毒类别',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'wzdl',
                title: '物种大类',
                titleTooltip:'物种大类',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'zbx',
                title: '致病性',
                titleTooltip:'致病性',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'wzzs',
                title: '物种注释',
                titleTooltip:'物种注释',
                width: '30%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                // 双击事件
                speciesDealById(row.wzid,'view',$("#species_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#species_formSearch #species_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true
        })
    };
    // 得到查询的参数
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "wzywm", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return speciesSearchData(map);
    };
    return oTableInit
};
// 条件搜索
function speciesSearchData(map){
    var cxtj=$("#species_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#species_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["wzid"]=cxnr
    }else if(cxtj=="1"){
        map["wzywm"]=cxnr
    }else if(cxtj=="2"){
        map["wzzwm"]=cxnr
    }else if(cxtj=='3'){
        map["wzfl"]=cxnr
    }else if(cxtj=='4'){
        map["wzlx"]=cxnr
    }else if(cxtj=='5'){
        map["fldj"]=cxnr
    }else if(cxtj=='6'){
        map["xjlb"]=cxnr
    }else if(cxtj=='7'){
        map["bdlb"]=cxnr
    }else if(cxtj=='8'){
        map["wzdl"]=cxnr
    }else if(cxtj=='9'){
        map["zbx"]=cxnr
    }

    return map;
}

// 列表刷新
function searchSpeciesResult(isTurnBack){
    if(isTurnBack){
        $('#species_formSearch #species_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#species_formSearch #species_list').bootstrapTable('refresh');
    }
}

// 按钮动作函数
function speciesDealById(id,action,tourl){
    var url= tourl;
    if(!tourl){
        return;
    }
    if(action =='view'){
        var url= tourl + "?wzid="+id;
        $.showDialog(url,'详情',viewSpeciesConfig);
    }
}

// 按钮初始化
var species_ButtonInit= function (){
    var oInit=new Object();
    var postdata = {};
    oInit.Init=function(){
        //初始化页面上面的按钮事件
        // 查询
        var btn_query=$("#species_formSearch #btn_query");
        // 查看
        var btn_view=$("#species_formSearch #btn_view");
        /*--------------------模糊查询--------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchSpeciesResult(true);
            });
        }
        /*--------------------查　　看--------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row=$('#species_formSearch #species_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                speciesDealById(sel_row[0].wzid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
    }
    return oInit;
}

// 查看
var viewSpeciesConfig = {
    width : "800px",
    modalName : "viewSpeciesModal",
    offAtOnce : true,  //当数据提交成功，立刻关闭窗口
    buttons : {
        cancel : {
            label : "关　闭",
            className : "btn-default"
        }
    }
};


// 页面初始化
$(function(){
    var oTable = new species_TableInit();
    oTable.Init();
    // 2.初始化Button的点击事件
    var oButtonInit = new species_ButtonInit();
    oButtonInit.Init();
    jQuery('#species_formSearch .chosen-select').chosen({width: '100%'});
});