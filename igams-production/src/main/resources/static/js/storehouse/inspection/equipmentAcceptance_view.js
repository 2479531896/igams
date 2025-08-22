var equipmentAcceptanceYjll_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#equipmentAcceptanceViewForm #yjtable_list").bootstrapTable({
            url: $("#equipmentAcceptanceViewForm #urlPrefix").val()+'/inspectionGoods/inspectionGoods/pagedataEquipmentAcceptance',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#equipmentAcceptanceViewForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "sqsj",				//排序字段
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
            uniqueId: "llid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                title: '序号',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '3%',
                align: 'left',
                visible:true
            },{
                field: 'lx',
                title: '类型',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sqsj',
                title: '申请时间',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'syrymc',
                title: '使用人员',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sydd',
                title: '使用地点',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'bmsbfzrmc',
                title: '部门负责人',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sqrmc',
                title: '申请人',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sqbmmc',
                title: '备注',
                width: '20%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: '',
                title: '调试记录',
                width: '8%',
                align: 'left',
                formatter:tsjlformatter,
                sortable: true,
                visible: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
        });
    }
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: 1,   // 页面大小
            pageNumber: 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "", // 防止同名排位用
            sortLastOrder: "asc", // 防止同名排位用
            flag: "sbyj", // 区分是编号还是移交
            sbysid: $("#equipmentAcceptanceViewForm #sbysid").val(), //
            // 搜索框使用
            // search:params.search
        };
        return map;
    }
    return oTableInit
}

function tsjlformatter(value,row,index) {
    return "<a href='javascript:void(0);' onclick=\"viewsbtsList('" + row.llid + "','/inspectionGoods/inspectionGoods/pagedataDeviceShakedownOA')\" >调试记录</a>";
}
function viewsbtsList(id,url){
    url=$("#equipmentAcceptanceViewForm #urlPrefix").val()+url+"?llid="+id;
    $.showDialog(url,'查看调试记录',viewsbtslistConfig);
}
var viewsbtslistConfig = {
    width        : "1300px",
    height        : "500px",
    offAtOnce    : true,  //当数据提交成功，立刻关闭窗口
    buttons        : {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var equipmentAcceptanceSbtsjl_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#equipmentAcceptanceViewForm #sbtsjltable_list").bootstrapTable({
            url: $("#equipmentAcceptanceViewForm #urlPrefix").val()+'/inspectionGoods/inspectionGoods/pagedataEquipmentAcceptance',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#equipmentAcceptanceViewForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "sbts.lrsj",				//排序字段
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
            uniqueId: "tsid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                title: '序',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序',
                width: '2%',
                align: 'left',
                visible:true
            },{
                field: 'sybmmc',
                title: '使用部门',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sydd',
                title: '使用地点',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'yjrmc',
                title: '移交人',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'yjsj',
                title: '移交日期',
                width: '13%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'azdw',
                title: '安装单位',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sydw',
                title: '使用单位',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'ysrmc',
                title: '验收人',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'ysrq',
                title: '验收日期',
                width: '13%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'yxsj',
                title: '运行时间',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'jsjdqbf',
                title: '机身及电器部分',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'czcdjg',
                title: '传动机构',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'fsxt',
                title: '附属系统',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'yspdyj',
                title: '验收评定意见',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
        });
    }
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: 1,   // 页面大小
            pageNumber: 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "", // 防止同名排位用
            sortLastOrder: "asc" ,// 防止同名排位用
            flag: "sbtsjl" ,// 区分是编号还是移交
            sbysid: $("#equipmentAcceptanceViewForm #sbysid").val(),
            // 搜索框使用
            // search:params.search
        };
        return map;
    }
    return oTableInit
}


var equipmentAcceptanceSbjl_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#equipmentAcceptanceViewForm #sbjltable_list").bootstrapTable({
            url: $("#equipmentAcceptanceViewForm #urlPrefix").val()+'/inspectionGoods/inspectionGoods/pagedataEquipmentAcceptance',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#equipmentAcceptanceViewForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "sbjl.lrsj",				//排序字段
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
            uniqueId: "sbjlid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                title: '序号',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '3%',
                align: 'left',
                visible:true
            },{
                field: 'sqrq',
                title: '申请日期',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sqrmc',
                title: '申请人',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sqbmmc',
                title: '申请部门',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'qwwcsj',
                title: '期望完成时间',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'jlrq',
                title: '计量日期',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'zt',
                title: '状态',
                width: '8%',
                align: 'left',
                formatter:ztformat,
                sortable: true,
                visible: true
            },{
                field: '',
                title: '关联文件',
                width: '8%',
                align: 'left',
                formatter:glwjformat,
                sortable: true,
                visible: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
        });
    }
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: 1,   // 页面大小
            pageNumber: 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "", // 防止同名排位用
            sortLastOrder: "asc" ,// 防止同名排位用
            flag: "sbjl" ,// 区分是编号还是移交
            sbysid: $("#equipmentAcceptanceViewForm #sbysid").val(),
            // 搜索框使用
            // search:params.search
        };
        return map;
    }
    return oTableInit
}

var equipmentAcceptanceSbyz_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#equipmentAcceptanceViewForm #sbyztable_list").bootstrapTable({
            url: $("#equipmentAcceptanceViewForm #urlPrefix").val()+'/inspectionGoods/inspectionGoods/pagedataEquipmentAcceptance',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#equipmentAcceptanceViewForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "sbyz.lrsj",				//排序字段
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
            uniqueId: "sbyzid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                title: '序号',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '3%',
                align: 'left',
                visible:true
            },{
                field: 'sqrq',
                title: '申请日期',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sqrmc',
                title: '申请人',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sqbmmc',
                title: '申请部门',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'qwwcsj',
                title: '期望完成时间',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'yzrq',
                title: '验证日期',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'zt',
                title: '状态',
                width: '8%',
                align: 'left',
                formatter:ztformat,
                sortable: true,
                visible: true
            },{
                field: '',
                title: '关联文件',
                width: '8%',
                align: 'left',
                sortable: true,
                formatter:glwjformat,
                visible: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
        });
    }
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: 1,   // 页面大小
            pageNumber: 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "", // 防止同名排位用
            sortLastOrder: "asc" ,// 防止同名排位用
            flag: "sbyz" ,// 区分是编号还是移交
            sbysid: $("#equipmentAcceptanceViewForm #sbysid").val(),
            // 搜索框使用
            // search:params.search
        };
        return map;
    }
    return oTableInit
}


var equipmentAcceptanceWxbyjl_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#equipmentAcceptanceViewForm #sbwxbytable_list").bootstrapTable({
            url: $("#equipmentAcceptanceViewForm #urlPrefix").val()+'/inspectionGoods/inspectionGoods/pagedataEquipmentAcceptance',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#equipmentAcceptanceViewForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "sbwx.lrsj",				//排序字段
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
            showColumns: true,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: false,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "sbwxid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                title: '序',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序',
                width: '2%',
                align: 'left',
                visible:true
            },{
                field: 'lb',
                title: '类别',
                width: '2%',
                align: 'left',
                formatter:lbformatter,
                sortable: true,
                visible: true
            },{
                field: 'sqrmc',
                title: '申请人',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sqbmmc',
                title: '申请部门',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sqrq',
                title: '申请日期',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'gzms',
                title: '故障描述',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sfcsfy',
                title: '是否产生费用',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true,
                formatter:sfcsfyformatter,
            },{
                field: 'fyje',
                title: '费用金额',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'wxqqwfsmc',
                title: '维修前去污方式',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'wxhqwfsmc',
                title: '维修后去污方式',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'wxdw',
                title: '维修单位',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'wbry',
                title: '维修人员',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'wxsj',
                title: '维修时间',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'jl',
                title: '结论',
                width: '15%',
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
                field: 'bfsjkdxyz',
                title: '备份数据可读性验证',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true,
                formatter:bfsjkdxyzformatter
            },{
                field: 'sfyz',
                title: '是否验证',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true,
                formatter:sfyzformatter
            },{
                field: 'yzrmc',
                title: '验证人',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'yzrq',
                title: '验证日期',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'hfsyrq',
                title: '恢复使用日期',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'yzfs',
                title: '验证方式',
                width: '15%',
                align: 'left',
                sortable: false,
                visible: true
            },{
                field: 'yzjl',
                title: '验证方式',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: false,
                formatter:ztformat
            },{
                field: 'zt',
                title: '状态',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true,
                formatter:ztformat
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
        });
    }
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: 1,   // 页面大小
            pageNumber: 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "", // 防止同名排位用
            sortLastOrder: "asc" ,// 防止同名排位用
            flag: "sbwx" ,// 区分是编号还是移交
            sbysid: $("#equipmentAcceptanceViewForm #sbysid").val(),
            // 搜索框使用
            // search:params.search
        };
        return map;
    }
    return oTableInit
}
function lbformatter(value,row,index){
    if(row.lb=='0'){
        return '维修';
    }else if(row.lb=='1'){
        return '保养';
    }
}
function sfcsfyformatter(value,row,index){
    if(row.sfcsfy=='1'){
        return '是';
    }else if(row.sfcsfy=='0'){
        return '否';
    }
}
function bfsjkdxyzformatter(value,row,index) {
    if(row.bfsjkdxyz=='1'){
        return '是';
    }else if(row.bfsjkdxyz=='0'){
        return '否';
    }
}

function sfyzformatter(value,row,index) {
    if(row.sfyz=='1'){
        return '是';
    }else if(row.sfyz=='0'){
        return '否';
    }
}
/**
 * 点击文件下载
 * @param fjid
 * @returns
 */
function xz(fjid){
    jQuery('<form action="'+$('#equipmentAcceptanceViewForm #urlPrefix').val() + '/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
        '<input type="text" name="fjid" value="'+fjid+'"/>' +
        '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
        '</form>')
        .appendTo('body').submit().remove();
}
function ztformat(value,row,index) {
    if(row.zt=='00'){
        return '未提交';
    }else if(row.zt=='10'){
        return '审核中';
    }else if(row.zt=='15'){
        return '审核不通过';
    }else if(row.zt=='80'){
        return '审核通过';
    }
}
function glwjformat(value,row,index) {
    if (row.sbjlid){
        return "<a href='javascript:void(0);' onclick=\"viewGlwjList('" + row.sbjlid + "','" + "0" + "','/inspectionGoods/inspectionGoods/pagedataDeviceFilesOA')\" >关联文件</a>";
    }else if (row.sbyzid){
        return "<a href='javascript:void(0);' onclick=\"viewGlwjList('" + row.sbyzid + "','" + "1" + "','/inspectionGoods/inspectionGoods/pagedataDeviceFilesOA')\" >关联文件</a>";
    }
}
function viewGlwjList(id,lb,url){
    url=$("#equipmentAcceptanceViewForm #urlPrefix").val()+url+"?sbglid="+id+"&lb="+lb;
    $.showDialog(url,'查看文件',viewsbglwjConfig);
}
var viewsbglwjConfig = {
    width        : "1000px",
    height        : "500px",
    offAtOnce    : true,  //当数据提交成功，立刻关闭窗口
    buttons        : {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
/**
 * 点击图片预览
 * @param fjid
 * @param wjm
 * @returns
 */
function view(fjid,wjm){
    var begin=wjm.lastIndexOf(".");
    var end=wjm.length;
    var type=wjm.substring(begin,end);
    if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
        var url=$("#equipmentAcceptanceViewForm #urlPrefix").val()+"/ws/sjxxpripreview/?fjid="+fjid
        $.showDialog(url,'图片预览',JPGMaterConfig);
    }else if(type.toLowerCase()==".pdf"){
        var url= $("#equipmentAcceptanceViewForm #urlPrefix").val()+"/common/file/pdfPreview?fjid=" + fjid;
        $.showDialog(url,'文件预览',JPGMaterConfig);
    }else {
        $.alert("暂不支持其他文件的预览，敬请期待！");
    }
}
var JPGMaterConfig = {
    width		: "800px",
    offAtOnce	: true,
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
var viewpurchasexxConfig = {
    width        : "1600px",
    offAtOnce    : true,  //当数据提交成功，立刻关闭窗口
    buttons        : {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
function viewQgxx(qgid){
    var url=$("#equipmentAcceptanceViewForm #urlPrefix").val()+"/purchase/purchase/viewPurchase"+"?qgid="+qgid;
    $.showDialog(url,'请购查看',viewpurchasexxConfig);
}
$(function(){
    // 1.初始化Table
    var oTable = new equipmentAcceptanceYjll_TableInit();
    oTable.Init();
    var oTable1 = new equipmentAcceptanceSbtsjl_TableInit();
    oTable1.Init();
    var oTable2 = new equipmentAcceptanceSbjl_TableInit();
    oTable2.Init();
    var oTable3 = new equipmentAcceptanceSbyz_TableInit();
    oTable3.Init();
    var oTable4 = new equipmentAcceptanceWxbyjl_TableInit();
    oTable4.Init();
    jQuery('#equipmentAcceptance_formSearch .chosen-select').chosen({width: '100%'});
})