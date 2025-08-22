var t_map = [];
var htlx = $("#ajaxForm #htlx").val();
// 显示字段
var columnsArray = [
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
        field: 'htmxid',
        title: '合同明细ID',
        titleTooltip:'合同明细ID',
        width: '8%',
        align: 'left',
        visible: false
    }, {
        field: 'mxid',
        title: '明细ID',
        titleTooltip:'明细ID',
        width: '8%',
        align: 'left',
        visible: false
    }, {
        field: 'qgid',
        title: '请购ID',
        titleTooltip:'请购ID',
        width: '8%',
        align: 'left',
        visible: false
    }, {
        field: 'wlid',
        title: '物料ID',
        titleTooltip:'物料ID',
        width: '8%',
        align: 'left',
        visible: false
    }, {
        field: 'djh',
        title: '请购单号',
        titleTooltip:'请购单号',
        width: '8%',
        align: 'left',
        formatter:djhFormatter,
        visible: true
    }, {
        field: 'wlbm',
        title: '物料编码',
        titleTooltip:'物料编码',
        width: '5%',
        align: 'left',
        formatter:wlbmformat,
        visible: true
    }, {
        field: '',
        title: '物料名称',
        titleTooltip:'物料名称',
        formatter:wlmcformat,
        width: '8%',
        align: 'left',
        visible: true
    },{
        field: '',
        title: '规格/标准',
        titleTooltip:'规格/标准',
        formatter:ggformat,
        width: '4%',
        align: 'left',
        visible: true
    },{
        field: 'sfgdzc',
        title: '资产',
        titleTooltip:'资产',
        formatter:sfgdzcformat,
        width: '3%',
        align: 'left',
        visible: true
    }, {
        field: 'sl',
        title: '数量',
        titleTooltip:'数量',
        width: '6%',
        align: 'left',
        visible: true
    },{
        field: 'hsdj',
        title: '含税单价',
        titleTooltip:'含税单价',
        width: '8%',
        align: 'left',
        visible: true
    },{
        field: 'hjje',
        title: '合计金额',
        titleTooltip:'合计金额',
        width: '5%',
        align: 'left',
        visible: true
    },{
        field: 'jhdhrq',
        title: '计划到货日期',
        titleTooltip:'计划到货日期',
        width: '6%',
        align: 'left',
        visible: true
    },{
        field: 'bxsj',
        title: '保修时间',
        titleTooltip:'保修时间',
        width: '6%',
        align: 'left',
        visible: true
    },{
        field: 'ccdg',
        title: '初次订购',
        titleTooltip:'初次订购',
        formatter:sfccdgformat,
        width: '5%',
        align: 'left',
        visible: true
    },{
        field: 'wlflmc',
        title: '物料分类',
        titleTooltip:'物料分类',
        width: '5%',
        align: 'left',
        visible: true
    },{
        field: 'cpzch',
        title: '产品注册号',
        titleTooltip:'产品注册号',
        width: '8%',
        align: 'left',
        visible: true
    },{
        field: 'dkrymc',
        title: '行打开人员',
        titleTooltip:'备注',
        width: '6%',
        align: 'left',
        visible: true
    },{
        field: 'gbrymc',
        title: '行关闭人员',
        titleTooltip:'行关闭人员',
        width: '6%',
        align: 'left',
        visible: true
    },{
        field: 'hzt',
        title: '行状态',
        titleTooltip:'行状态',
        width: '4%',
        align: 'left',
        formatter:hztformatter,
        visible: true
    },{
        field: 'bz',
        title: '备注',
        titleTooltip:'备注',
        width: '8%',
        align: 'left',
        visible: true
    }];
var contractEdit_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#ajaxForm #tb_list').bootstrapTable({
            url: $("#ajaxForm #urlPrefix").val()+'/contract/contract/pagedataGetContractDetailList',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#ajaxForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "qgid",				//排序字段
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
            uniqueId: "htmxid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
            onLoadSuccess: function (map) {
            	t_map = map;
            	if(t_map.rows){
            		// 初始化htmx_json
            		var json = [];
            		for (var i = 0; i < t_map.rows.length; i++) {
            			var sz = {"qgid":'',"qgmxid":''};
						sz.qgid = t_map.rows[i].qgid;
						sz.qgmxid = t_map.rows[i].qgmxid;
						json.push(sz);
    				}
            		$("#ajaxForm #htmx_json").val(JSON.stringify(json));
            	}
            },
            onLoadError: function () {
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
            sortLastName: "qgmxid", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            htid: $("#ajaxForm #htid").val(),
            //搜索框使用
            //search:params.search
        };
    	return map;
    };
    return oTableInit;
};

var contractPay_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#ajaxForm #fk_list').bootstrapTable({
            url: $("#ajaxForm #urlPrefix").val()+'/contract/payInfo/pagedataGetContractPayList',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#ajaxForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "htid",				//排序字段
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
            uniqueId: "htfkmxid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [
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
                    field: 'htfkmxid',
                    title: '合同付款明细ID',
                    titleTooltip:'合同付款明细ID',
                    width: '8%',
                    align: 'left',
                    visible: false
                }, {
                    field: 'htfkid',
                    title: '合同付款ID',
                    titleTooltip:'合同付款ID',
                    width: '8%',
                    align: 'left',
                    visible: false
                }, {
                    field: 'htnbbh',
                    title: '合同编号',
                    titleTooltip:'合同编号',
                    width: '8%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'zje',
                    title: '原总金额',
                    titleTooltip:'原总金额',
                    width: '8%',
                    align: 'left',
                    visible: true
                },{
                    field: 'xzje',
                    title: '现总金额',
                    titleTooltip:'现总金额',
                    width: '8%',
                    align: 'left',
                    visible: true
                },{
                    field: 'yfje',
                    title: '已付金额',
                    titleTooltip:'已付金额',
                    width: '8%',
                    align: 'left',
                    visible: true,
                }, {
                    field: 'wfje',
                    title: '未付金额',
                    titleTooltip:'未付金额',
                    width: '8%',
                    align: 'left',
                    visible: true
                },
//                {
//                    field: 'fkzje',
//                    title: '付款中金额',
//                    titleTooltip:'付款中金额',
//                    width: '8%',
//                    align: 'left',
//                    visible: true
//                },
                {
                    field: 'fkje',
                    title: '付款金额',
                    titleTooltip:'付款金额',
                    width: '4%',
                    align: 'left',
                    visible: true
                },{
                    field: 'lrsj',
                    title: '提交日期',
                    titleTooltip:'提交日期',
                    width: '6%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'fkbfb',
                    title: '付款百分比',
                    titleTooltip:'付款百分比',
                    formatter:fkbfbformat,
                    width: '6%',
                    align: 'left',
                    visible: true
                }],
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
            sortLastName: "htfkmxid", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            htid: $("#ajaxForm #htid").val(),
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};

var contractPayFp_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#ajaxForm #fp_list').bootstrapTable({
            url: $("#ajaxForm #urlPrefix").val()+'/contract/contract/pagedataViewFpOrRk',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#ajaxForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "fpmxid",				//排序字段
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
            uniqueId: "fpmxid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [
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
                    field: 'fpmxid',
                    title: '发票明细ID',
                    titleTooltip:'发票明细ID',
                    width: '8%',
                    align: 'left',
                    visible: false
                }, {
                    field: 'fph',
                    title: '发票号',
                    titleTooltip:'发票号',
                    width: '6%',
                    align: 'left',
                    formatter:fphformat,
                    visible: true
                }, {
                    field: 'fpzlmc',
                    title: '发票种类',
                    titleTooltip:'发票种类',
                    width: '4%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'kprq',
                    title: '开票日期',
                    titleTooltip:'开票日期',
                    width: '6%',
                    align: 'left',
                    visible: true,
                }, {
                    field: 'kpje',
                    title: '发票总金额',
                    titleTooltip:'发票总金额',
                    width: '4%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'wlbm',
                    title: '物料编码',
                    titleTooltip:'物料编码',
                    width: '4%',
                    align: 'left',
                    formatter:wlbmformat,
                    visible: true
                },{
                    field: 'wlmc',
                    title: '物料名称',
                    titleTooltip:'物料名称',
                    width: '10%',
                    align: 'left',
                    visible: true
                },{
                    field: 'gg',
                    title: '规格',
                    titleTooltip:'规格',
                    width: '10%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'sl',
                    title: '发票数量',
                    titleTooltip:'发票数量',
                    width: '3%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'hjje',
                    title: '发票物料金额',
                    titleTooltip:'发票物料金额',
                    width: '5%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'u8rkdh',
                    title: 'U8入库单号',
                    titleTooltip:'U8入库单号',
                    width: '8%',
                    align: 'left',
                    _formatter:u8rkdhformat,
                    visible: true
                }],
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
            sortLastName: "fpmxid", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            flag: "fp",
            htid: $("#htid").val()
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};


var contractRk_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#ajaxForm #rk_list').bootstrapTable({
            url: $("#ajaxForm #urlPrefix").val()+'/contract/contract/pagedataViewFpOrRk',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#ajaxForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "fkmxid",				//排序字段
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
            uniqueId: "fkmxid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [
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
                    field: 'rkid',
                    title: '入库ID',
                    titleTooltip:'入库ID',
                    width: '8%',
                    align: 'left',
                    visible: false
                }, {
                    field: 'wlbm',
                    title: '物料编码',
                    titleTooltip:'物料编码',
                    width: '6%',
                    align: 'left',
                    formatter:wlbmformat,
                    visible: true
                }, {
                    field: 'wlmc',
                    title: '物料名称',
                    titleTooltip:'物料名称',
                    width: '10%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'gg',
                    title: '规格',
                    titleTooltip:'规格',
                    width: '10%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'jldw',
                    title: '单位',
                    titleTooltip:'单位',
                    width: '3%',
                    align: 'left',
                    visible: true,
                }, {
                    field: 'scph',
                    title: '生产批号',
                    titleTooltip:'生产批号',
                    width: '6%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'scrq',
                    title: '生产日期',
                    titleTooltip:'生产日期',
                    width: '6%',
                    align: 'left',
                    visible: true
                },{
                    field: 'yxq',
                    title: '有效期',
                    titleTooltip:'有效期',
                    width: '6%',
                    align: 'left',
                    visible: true
                },{
                    field: 'sl',
                    title: '数量',
                    titleTooltip:'数量',
                    width: '3%',
                    align: 'left',
                    visible: true
                }, {
                    field: 'dhdh',
                    title: '到货单号',
                    titleTooltip:'到货单号',
                    width: '8%',
                    align: 'left',
                    formatter:dhdhformat,
                    visible: true
                }, {
                    field: 'rkdh',
                    title: '入库单号',
                    titleTooltip:'入库单号',
                    width: '8%',
                    align: 'left',
                    formatter:rkdhformat,
                    visible: true
                }, {
                    field: 'u8rkdh',
                    title: 'U8入库单号',
                    titleTooltip:'U8入库单号',
                    width: '8%',
                    align: 'left',
                    formatter:u8rkdhformat,
                    visible: true
                }],
            onLoadSuccess: function (map) {
            },
            onLoadError: function () {
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
            sortLastName: "rkid", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            htid: $("#htid").val(),
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};




/**
 * 是否固定资产格式化
 * @returns
 */
function sfgdzcformat(value,row,index) {
    var html="";
    if (row.sfgdzc == '0') {
        html="<span style='color:red;'>"+"否"+"</span>";
    }else if (row.sfgdzc == '1') {
        html="<span style='color:green;'>"+"是"+"</span>";
    }
    return html;
}
/**
 * 是否固定资产格式化
 * @returns
 */
function hztformatter(value,row,index) {
    var html="";
    if (row.hzt == '1'|| row.hzt ==null) {
        html="<span style='color:green;'>"+"打开"+"</span>";
    }else if (row.hzt == '0') {
        html="<span style='color:red;'>"+"关闭"+"</span>";
    }
    return html;
}

function fkbfbformat(value,row,index){
    return row.fkbfb+"%";
}

function wlmcformat(value,row,index){
	var html="";
	if(row.qglbdm!='MATERIAL' && row.qglbdm!=null && row.qglbdm!=''&&row.qglbdm!='DEVICE'&&row.qglbdm!='OUTSOURCE'){
		html="<span title='"+row.hwmc+"'>"+row.hwmc+"</span>";
	}else{
	    if(row.wlmc!=null&&row.wlmc!=''){
            html="<span title='"+row.wlmc+"'>"+row.wlmc+"</span>";
        }else{
            html="<span title='"+row.hwmc+"'>"+row.hwmc+"</span>";
        }
	}
	return html;
}

function wlxxformat(value,row,index){
	var html="";
    if(row.wlmc!=null&&row.wlmc!=''){
        html="<span title='"+row.wlmc+"'>"+row.wlmc+"</span>";
    }else if (row.hwmc!=null&&row.hwmc!=''){
        html="<span title='"+row.hwmc+"'>"+row.hwmc+"</span>";
    }
	return html;	
}
function ggscsformat(value,row,index){
    if (row.fwbj=='1'){
        return "";
    }
    var gg=row.gg;
    var title=row.gg;
    if(gg==null || gg ==''){
        gg=row.hwbz;
        title=row.hwbz;
    }
    var html="<span style='width:70%;line-height:24px;' title='"+title+"'>"+gg+"</span>";
    return html;
}
function ggformat(value,row,index){
	var html="";
    if(row.gg!=null&&row.gg!=''){
        html="<span title='"+row.gg+"'>"+row.gg+"</span>";
    }else if (row.hwbz!=null&&row.hwbz!=''){
        html="<span title='"+row.hwbz+"'>"+row.hwbz+"</span>";
    }
	return html;
}
/**
 * 是否初次订购
 * @returns
 */
function sfccdgformat(value,row,index) {
    var html="";
    if (row.ccdg == '1') {
        html="<span>"+"是"+"</span>";
    }else if (row.ccdg == '0'){
        html="<span>"+"否"+"</span>";
    }
    return html;
}
/**
 * 发票号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function fphformat(value,row,index){
    var html="";
    if(row.fph!=null&&row.fph!=''){
        html+="<span class='col-md-12 col-sm-12'>";
        html += "<a href='javascript:void(0);' onclick=\"queryByFpid('"+row.fpid+"')\">"+row.fph+"</a>";
        html+="</span>";
    }
    return html;
}
/**
 * 单据号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function djhFormatter(value,row,index){
	var html = "";
	if(row.djh==null){
		html = "";
	}else{
		html += "<a href='javascript:void(0);' onclick=\"queryByqgid('"+row.qgid+"')\">"+row.djh+"</a>";

	}
	return html;
}

function queryByqgid(qgid){
	var url=$("#ajaxForm #urlPrefix").val()+"/purchase/purchase/viewPurchase?qgid="+qgid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'请购信息',viewQgConfig);	
}

var viewQgConfig={
	width		: "1500px",
	modalName	:"viewQgModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
}
function wlbmformat(value,row,index){
    var html = "";
    if(row.wlbm==null){
        html += "<span></span>"
    }else{
        html += "<a href='javascript:void(0);' onclick=\"queryByWlbm('"+row.wlid+"')\">"+row.wlbm+"</a>";
    }
    return html;
}
function queryByWlbm(wlid){
    var url=$("#ajaxForm #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
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
function dhdhformat(value,row,index){
    var html = "";
    if(row.dhdh==null){
        html += "<span></span>"
    }else{
        html += "<a href='javascript:void(0);' onclick=\"queryByDhdh('"+row.dhid+"')\">"+row.dhdh+"</a>";
    }
    return html;
}
function queryByDhdh(dhid){
    var url=$("#ajaxForm #urlPrefix").val()+"/storehouse/arrivalGoods/pagedataViewArrivalGoods?dhid="+dhid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'到货信息',viewDhConfig);
}
var viewDhConfig = {
    width		: "1600px",
    height		: "500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
function rkdhformat(value,row,index){
    var html = "";
    if(row.rkdh==null){
        html += "<span></span>"
    }else{
        html += "<a href='javascript:void(0);' onclick=\"queryByRkid('"+row.rkid+"')\">"+row.rkdh+"</a>";
    }
    return html;
}
function queryByRkid(rkid){
    var url=$("#ajaxForm #urlPrefix").val()+"/warehouse/putInStorage/pagedataViewPutInStorage?rkid="+rkid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'入库信息',viewHwConfig);
}
var viewHwConfig={
    width		: "1600px",
    modalName	:"viewHwModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
/**
 * U8入库单号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function u8rkdhformat(value,row,index){
    var html="";
    if(row.u8rkdh==null){
        html += "<span></span>"
    }else{
        if (row.lbcskz1!=null&&row.lbcskz1!='')
        html += "<a href='javascript:void(0);' onclick=\"queryByDhdh('"+row.dhid+"')\">"+row.u8rkdh+"</a>";
        else {
            html += "<a href='javascript:void(0);' onclick=\"queryByRkid('"+row.rkid+"')\">"+row.u8rkdh+"</a>";
        }
    }
    return html;
}
function queryByFpid(fpid){
    var url=$("#ajaxForm #urlPrefix").val()+"/invoice/invoice/viewInvoice?fpid="+fpid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'发票详情',viewFpConfig);
}
var viewFpConfig = {
    width		: "1400px",
    modalName	:"viewInvoiceModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};


function yl(fjid,wjm){
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin,end);
	if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
		var url=$("#ajaxForm #urlPrefix").val()+"/ws/sjxxpripreview?fjid="+fjid
		$.showDialog(url,'图片预览',viewPreViewConfig);
	}else if(type.toLowerCase()==".pdf"){
		var url= $("#ajaxForm #urlPrefix").val()+"/common/file/pdfPreview?fjid=" + fjid;
        $.showDialog(url,'文件预览',viewPreViewConfig);
	}else {
		$.alert("暂不支持其他文件的预览，敬请期待！");
	}
}

var viewPreViewConfig = {
    width        : "900px",
    height        : "800px",
    offAtOnce    : true,  //当数据提交成功，立刻关闭窗口
    buttons        : {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

function xz(fjid){
    jQuery('<form action="'+$('#ajaxForm #urlPrefix').val() + '/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
            '<input type="text" name="fjid" value="'+fjid+'"/>' + 
            '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' + 
        '</form>')
    .appendTo('body').submit().remove();
}
/**
 * 初始化页面
 * @returns
 */
function init(){
	//添加日期控件
	laydate.render({
	   elem: '#ajaxForm #cjrq'
	  ,theme: '#2381E9'
	});
	// 初始化请购单号
	initDjh();
	//币种下拉框改变事件
	var sel_biz = $("#ajaxForm #biz");
	sel_biz.unbind("change").change(function(){
		var csid = $("#ajaxForm #biz").val();
		var cskz1 = $("#ajaxForm #"+csid).attr("cskz1");
		if(cskz1){
			$("#ajaxForm #sl").val(cskz1);
		}
	});
}
/**
 * 初始化请购单号
 * @returns
 */
function initDjh(){
	var qgids = $("#ajaxForm #qgids").val();
	if(!qgids || qgids.length < 0){
		return;
	}
	$.ajax({ 
	    type:'post',  
	    url:$("#ajaxForm #urlPrefix").val()+"/purchase/purchase/pagedataGetPurchaseList",
	    cache: false,
	    data: {"ids":qgids,"access_token":$("#ac_tk").val()},  
	    dataType:'json',
	    success:function(result){
	    	//返回值
	    	var qgglDtos = result.qgglDtos;
	    	if(qgglDtos){
	    		$("#ajaxForm #djhs").tagsinput({
	    			itemValue: "qgid",
	    			itemText: "djh",
	    		})
	    		for(var i = 0; i < qgglDtos.length; i++){
		    		$("#ajaxForm #djhs").tagsinput('add',{"qgid": qgglDtos[i].qgid, "djh": qgglDtos[i].djh});
		    	}
	    	}
	    }
	});
}
var frameworkContractColumnsArray = [
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
        field: 'htmxid',
        title: '合同明细ID',
        titleTooltip:'合同明细ID',
        width: '8%',
        align: 'left',
        visible: false
    }, {
        field: 'wlid',
        title: '物料ID',
        titleTooltip:'物料ID',
        width: '8%',
        align: 'left',
        visible: false
    }, {
        field: 'wlbm',
        title: '物料编码',
        titleTooltip:'物料编码',
        width: '6%',
        align: 'left',
        formatter:wlbmformat,
        visible: true
    }, {
        field: 'wlxx',
        title: '物料名称',
        titleTooltip:'物料名称',
        width: '6%',
        align: 'left',
        formatter:wlxxformat,
        visible: true
    },{
        field: '',
        title: '规格',
        titleTooltip:'规格',
        formatter:ggscsformat,
        width: '6%',
        align: 'left',
        visible: true
    },{
        field: 'jldw',
        title: '单位',
        titleTooltip:'单位',
        width: '5%',
        align: 'left',
        visible: true
    },{
        field: 'hsdj',
        title: '含税单价',
        titleTooltip:'含税单价',
        width: '8%',
        align: 'left',
        visible: true
    },{
        field: 'dhzq',
        title: '到货周期',
        titleTooltip:'到货周期',
        width: '4%',
        align: 'left',
        visible: true
    },{
        field: 'ccdg',
        title: '初次订购',
        titleTooltip:'初次订购',
        formatter:sfccdgformat,
        width: '4%',
        align: 'left',
        visible: true
    },{
        field: 'wlflmc',
        title: '物料分类',
        titleTooltip:'物料分类',
        width: '5%',
        align: 'left',
        visible: true
    },{
        field: 'cpzch',
        title: '产品注册号',
        titleTooltip:'产品注册号',
        width: '8%',
        align: 'left',
        visible: true
    },{
        field: 'bxsj',
        title: '保修时间',
        titleTooltip:'保修时间',
        width: '6%',
        align: 'left',
        visible: true
    },{
        field: 'sfgdzc',
        title: '资产',
        titleTooltip:'资产',
        formatter:sfgdzcformat,
        width: '3%',
        align: 'left',
        visible: true
    },{
        field: 'bz',
        title: '备注',
        titleTooltip:'备注',
        width: '15%',
        align: 'left',
        visible: true
    },{
        field: 'scbj',
        title: '状态',
        titleTooltip:'状态',
        width: '4%',
        align: 'left',
        formatter:scbjformatter,
        visible: true
    }];
function scbjformatter(value,row,index){
    var  kjhthtml = "";
    if("0"==row.scbj){
        kjhthtml += "<span style='margin-left:5px;' class='btn btn-danger' title='停用' onclick=\"stopUsing('" + row.htmxid + "')\" >停用</span>";
    }else if ("3"==row.scbj){
        kjhthtml += "<span style='margin-left:5px;' class='btn btn-success' title='启用' onclick=\"startUsing('" + row.htmxid + "')\" >启用</span>";
    }
    return kjhthtml;
}
/**
 * 停用操作
 * @index index
 * @event event
 * @returns
 */
function stopUsing(htmxid){
    $.ajax({
        async:false,
        url: $('#ajaxForm #urlPrefix').val()+'/contract/contract/pagedataUpdateScbj',
        type: 'post',
        dataType: 'json',
        data : {"htmxid":htmxid, "scbj":"3", "access_token":$("#ac_tk").val()},
        success: function(data) {
            if(data.status == 'fail'){
                $.error("异常出错！");
            }
            $('#ajaxForm #kjhtmx_list').bootstrapTable('refresh');
        }
    });
}
/**
 * 启用操作
 * @param index
 * @param event
 * @returns
 */
function startUsing(htmxid){
    $.ajax({
        async:false,
        url: $('#ajaxForm #urlPrefix').val()+'/contract/contract/pagedataUpdateScbj',
        type: 'post',
        dataType: 'json',
        data : {"htmxid":htmxid, "scbj":"0", "access_token":$("#ac_tk").val()},
        success: function(data) {
            if(data.status == 'fail'){
                $.error("异常出错！");
            }
            $('#ajaxForm #kjhtmx_list').bootstrapTable('refresh');
        }
    });
}
var viewFrameworkContract_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#ajaxForm #kjhtmx_list').bootstrapTable({
            url: $("#ajaxForm #urlPrefix").val()+'/contract/contract/pagedataFrameworkContractDetail',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#ajaxForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "htmx.xh",				//排序字段
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
            uniqueId: "htmxid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: frameworkContractColumnsArray,
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
            sortLastName: "htmxid", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            htid: $("#ajaxForm #htid").val(),
            htlx: $("#ajaxForm #htlx").val(),
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};

$(document).ready(function(){

	//初始化列表
    if (htlx!='3'){
        var oTable=new contractEdit_TableInit();
        oTable.Init();
        var oTablePay=new contractPay_TableInit();
        oTablePay.Init();
        var oTablePayFp=new contractPayFp_TableInit();
        oTablePayFp.Init();
        var oTablePayRk=new contractRk_TableInit();
        oTablePayRk.Init();
    }
    var viewFrameworkContract = viewFrameworkContract_TableInit();
    viewFrameworkContract.Init();

	// 初始化页面
	init();
	//所有下拉框添加choose样式
	jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
});


