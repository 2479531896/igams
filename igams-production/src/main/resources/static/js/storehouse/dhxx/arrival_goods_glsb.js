var cskz2 = $('#dh_glsb_formSearch #cskz2').val();
var mater_GLSB_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#dh_glsb_formSearch #tb_list').bootstrapTable({
            url: $('#dh_glsb_formSearch #urlPrefix').val() + $('#dh_glsb_formSearch #url').val()+"?cskz2="+$('#dh_glsb_formSearch #cskz2').val(),         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#dh_glsb_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            //paginationShowPageGo: false,         //增加跳转页码的显示
//            paginationDetailHAlign: " hidden", //不显示底部文字
            sortable: true,                     //是否启用排序
            sortName: "sbys.lrsj",				//排序字段
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
            showColumns: true,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "sbysid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%'
            }, {
                field: 'sbysid',
                title: '设备验收ID',
                width: '8%',
                align: 'left',
                visible: false
            },
            {
                field: 'sbbh',
                title: '设备编号',
                titleTooltip:'设备编号',
                width: '10%',
                align: 'left',
                visible: true,
                sortable: true,
            },{
               field: 'sbmc',
               title: '设备名称',
               titleTooltip:'设备名称',
               width: '10%',
               align: 'left',
               visible: true
            },{
               field: 'sbccbh',
               title: '设备出厂编号',
               titleTooltip:'设备出厂编号',
               width: '10%',
               align: 'left',
               visible: true
            },{
                field: 'gdzcbh',
                title: '固定资产编号',
                titleTooltip:'固定资产编号',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'ggxh',
                title: '规格型号',
                titleTooltip:'规格型号',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'xlh',
                title: '序列号',
                titleTooltip:'序列号',
                width: '10%',
                align: 'left',
                visible: true
            }, {
                field: 'yzrq',
                title: '验证日期',
                titleTooltip:'验证日期',
                width: '10%',
                align: 'left',
                visible: cskz2=='1'?true:false
            }, {
                field: 'jlrq',
                title: '计量日期',
                titleTooltip:'计量日期',
                width: '10%',
                align: 'left',
                visible: cskz2=='0'?true:false
            }, {
                field: 'sqbmmc',
                title: '验证部门',
                titleTooltip:'验证部门',
                width: '10%',
                align: 'left',
                visible: cskz2=='1'?true:false
            }, {
                field: 'sqbmmc',
                title: '计量部门',
                titleTooltip:'计量部门',
                width: '10%',
                align: 'left',
                visible: cskz2=='0'?true:false
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            },
        });
		$("#dh_glsb_formSearch #tb_list").colResizable({
			liveDrag:true, 
			gripInnerHtml:"<div class='grip'></div>", 
			draggingClass:"dragging", 
			resizeMode:'fit', 
			postbackSafe:true,
			partialRefresh:true
		})
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
            sortLastName: "sbys.sbysid", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            //搜索框使用
            //search:params.search
        };

        return getMaterSearchData_GL(map);
    };
    return oTableInit;
};

function getMaterSearchData_GL(map){
	var cxtj = $("#dh_glsb_formSearch #cxtj").val();
	// 查询条件
	var cxnr = $.trim(jQuery('#dh_glsb_formSearch #cxnr').val());
	if (cxtj == "0") {
		map["entire"] = cxnr;
	}else if (cxtj == "1") {
		map["sbbh"] = cxnr;
	}else if (cxtj == "2") {
		map["sbmc"] = cxnr;
	}else if (cxtj == "3") {
		map["gdzcbh"] = cxnr;
	}else if (cxtj == "4") {
		map["sbccbh"] = cxnr;
	}else if (cxtj == "5") {
		map["xlh"] = cxnr;
	}
	return map;
}


var mater_GLSB_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};
    oInit.Init = function () {
        //初始化页面上面的按钮事件
    	var btn_query = $("#dh_glsb_formSearch #btn_query");

    	//绑定搜索发送功能
    	if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			searchGLSBResult(true);
    		});
    	}
    };

    return oInit;
};





function searchGLSBResult(isTurnBack){
	if(isTurnBack){
		$('#dh_glsb_formSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#dh_glsb_formSearch #tb_list').bootstrapTable('refresh');
	}
}


$(function(){
	//0.界面初始化
    //1.初始化Table
    var oTable = new mater_GLSB_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new mater_GLSB_ButtonInit();
    oButtonInit.Init();
    //所有下拉框添加choose样式
    jQuery('#dh_glsb_formSearch .chosen-select').chosen({width: '100%'});
});
