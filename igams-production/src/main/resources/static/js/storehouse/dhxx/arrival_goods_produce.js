var arrival_produce_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#arrival_produce_formSearch #arrival_produce_tb_list').bootstrapTable({
            url: $('#arrival_produce_formSearch #urlPrefix').val() + '/storehouse/produce/pagedataGetPagedDtoProduceList',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#arrival_produce_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
           paginationDetailHAlign: true, //不显示底部文字
            sortable: true,                     //是否启用排序
            sortName: "sczlgl.lrsj",				//排序字段
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
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "sczlgl.sczlid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
				width: '2%',
            }, {
                field: 'sczlid',
                title: '生产指令id',
                width: '8%',
                align: 'left',
                visible: false
            }, {
                field: 'zldh',
                title: '指令单号',
                titleTooltip:'指令单号',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },
            {
                field: 'zlrq',
                title: '指令日期',
                sortable: true,
                titleTooltip:'指令日期',
                width: '6%',
                align: 'left',
                visible: true,
            }, {
                field: 'cpbh',
                title: '产品编号',
                titleTooltip:'产品编号',
                sortable: true,
                width: '6%',
                align: 'left',
                visible: true

            }, {
				field: 'jhcl',
				title: '计划产量',
				titleTooltip:'计划产量',
				width: '6%',
				align: 'left',
				visible: true
            }, {
                field: 'kyysl',
                title: '可引用数量',
                titleTooltip:'可引用数量',
                width: '6%',
                align: 'left',
                visible: true
            }, {
                field: 'cz',
                title: '操作',
                titleTooltip:'操作',
                width: '6%',
                align: 'left',
                formatter:czFormat,
                visible: true
			}],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onClickRow: function (row, $element) {
                if($("#arrival_produce_formSearch #cz_"+row.sczlid).text()){
                    if($("#arrival_produce_formSearch #cz_"+row.sczlid).text() != "-"){
                        $("#arrival_produce_formSearch #cz_"+row.sczlid).removeAttr("class");
                        $("#arrival_produce_formSearch #cz_"+row.sczlid).text("");
                    }
                }else{

                    // 判断是否更新请购信息
                    var sczl_json = [];
                    var refresh = true;
                    if($("#arrival_produce_formSearch #sczl_json").val()){
                        sczl_json = JSON.parse($("#arrival_produce_formSearch #sczl_json").val());
                        if(sczl_json.length > 0){
                            for (var i = sczl_json.length-1; i >= 0; i--) {
                                if(sczl_json[i].sczlid == row.sczlid){
                                    refresh = false;
                                    break;
                                }else{
                                    $.alert("只能选择一条数据！");
                                    return;
                                }
                            }
                        }
                    }
                    $("#arrival_produce_formSearch #cz_"+row.sczlid).attr("class","btn btn-success");
                    $("#arrival_produce_formSearch #cz_"+row.sczlid).text("调整明细");
                    if(refresh){
                        $.ajax({
                            type:'post',
                            url: $("#arrival_produce_formSearch input[name='urlPrefix']").val() + "/storehouse/produce/pagedataGetProduceInfo",
                            cache: false,
                            data: {"sczlid":row.sczlid, "access_token":$("#ac_tk").val()},
                            dataType:'json',
                            success:function(result){
                                if(result.sczlglDtos != null && result.sczlglDtos.length > 0){
                                    for (var i = 0; i < result.sczlglDtos.length; i++) {
                                        var sz = {"sczlid":'',"sczlmxid":''};
                                        sz.sczlid = result.sczlglDtos[i].sczlid;
                                        sz.sczlmxid = result.sczlglDtos[i].sczlmxid;
                                        sczl_json.push(sz);
                                    }
                                    $("#arrival_produce_formSearch #sczl_json").val(JSON.stringify(sczl_json));
                                    $("#arrival_produce_formSearch #xzzldh").tagsinput('add',{"value":row.sczlid,"text":row.zldh});
                                }
                            }
                        });
                    }
                }
            },
        });
    };
    //得到查询的参数
    oTableInit.queryParams = function(params){
        var map = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   //页面大小
        	pageNumber: (params.offset / params.limit) + 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名  
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "sczlgl.sczlid", //防止同名排位用
            zt: "80",
            sortLastOrder: "asc" //防止同名排位用
        };

        return getProduceSearchData(map);
    };
    return oTableInit;
};

/**
 * 初始化已选订单号
 * @returns
 */
function initTagsinput(){
    $("#arrival_produce_formSearch  #xzzldh").tagsinput({
        itemValue: "value",
        itemText: "text",
    })

}

/**
 * 监听标签点击事件
 */
var tagClick = $("#arrival_produce_formSearch").on('click','.label-info',function(e){
    event.stopPropagation();
    var url = $("#arrival_produce_formSearch input[name='urlPrefix']").val() + "/storehouse/produce/pagedataChooseListProduceInfo?access_token=" + $("#ac_tk").val() + "&sczlid=" + e.currentTarget.dataset.logo;
    $.showDialog(url, '选择指令明细', chooseSczlConfig);

});
/**
 * 监听标签移除事件
 */
var tagsRemoved = $('#arrival_produce_formSearch #xzzldh').on('beforeItemRemove', function(event) {
    var sczl_json = [];
    if($("#arrival_produce_formSearch #sczl_json").val()){
        sczl_json = JSON.parse($("#arrival_produce_formSearch #sczl_json").val());
    }
    var sczlid = event.item.value;
    if(sczl_json.length > 0){
        for (var i = sczl_json.length-1; i >= 0; i--) {
            if(sczl_json[i].sczlid == sczlid){
                sczl_json.splice(i,1);
            }
        }
    }
    $("#arrival_produce_formSearch #sczl_json").val(JSON.stringify(sczl_json));
});


function searchArrivalProduceResult(isTurnBack){
    //关闭高级搜索条件
    $("#arrival_produce_formSearch #searchMore").slideUp("low");
    produce_turnOff=true;
    $("#arrival_produce_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#arrival_produce_formSearch #arrival_produce_tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#arrival_produce_formSearch #arrival_produce_tb_list').bootstrapTable('refresh');
	}
}


function getProduceSearchData(map){
	var cxtj = $("#arrival_produce_formSearch #cxtj").val();
	// 查询条件
	var cxnr = $.trim(jQuery('#arrival_produce_formSearch #cxnr').val());
	// '0':'物料编号','1':'物料名称','2':'生产商','3':'货号'
	if (cxtj == "0") {
		map["entire"] = cxnr;
	}else if (cxtj == "1") {
		map["zldh"] = cxnr;
	}else if (cxtj == "2") {
		map["cpbh"] = cxnr;
	}
	// 需求开始日期
	var zlrqstart = jQuery('#arrival_produce_formSearch #zlrqstart').val();
	map["zlrqstart"] = zlrqstart;
	// 需求结束日期
	var zlrqend = jQuery('#arrival_produce_formSearch #zlrqend').val();
	map["zlrqend"] = zlrqend;

    // 高级筛选
    // 分组
    var cplxs = jQuery('#arrival_produce_formSearch #cplx_id_tj').val();
    map["cplxs"] = cplxs;

	return map;
}

/**
 * 操作按钮格式化函数
 * @returns
 */
function czFormat(value,row,index) {
    return "<span id='cz_"+row.sczlid+"' onclick=\"reviseSczlDetail('" + row.sczlid + "',event)\"/>";
}
/**
 * 调整明细点击事件
 * @param qgid
 * @param event
 * @returns
 */
function reviseSczlDetail(sczlid, event){
    if($("#cz_"+sczlid).text() == "-"){
        return false;
    }
    event.stopPropagation();
    var url = $("#arrival_produce_formSearch input[name='urlPrefix']").val() + "/storehouse/produce/pagedataChooseListProduceInfo?access_token=" + $("#ac_tk").val() + "&sczlid=" + sczlid;
    $.showDialog(url, '选择指令明细', chooseSczlConfig);
}
var chooseSczlConfig = {
    width : "1000px",
    modalName	: "chooseSczlModal",
    formName	: "chooseSczlForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#chooseZlmxForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                // 获取选中明细并保存
                if($("#chooseZlmxForm input[name='checkZlmx']:checked").length == 0){
                    $.alert("未选中明细信息！");
                    return false;
                }
                var sczl_json = [];
                if($("#arrival_produce_formSearch #sczl_json").val()){
                    sczl_json = JSON.parse($("#arrival_produce_formSearch #sczl_json").val());
                }
                var sczlid = $("#chooseZlmxForm input[name='sczlid']").val();
                if(sczl_json.length > 0){
                    for (var i = sczl_json.length-1; i >= 0; i--) {
                        if(sczl_json[i].sczlid == sczlid){
                            sczl_json.splice(i,1);
                        }
                    }
                }
                $("#chooseZlmxForm input[name='checkZlmx']").each(function(){
                    if($(this).is(":checked")){
                        var sz={"sczlid":'',"sczlmxid":''};
                        sz.sczlid = sczlid;
                        sz.sczlmxid = this.dataset.sczlmxid;
                        sczl_json.push(sz);
                    }
                })
                $("#arrival_produce_formSearch #sczl_json").val(JSON.stringify(sczl_json));
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


var arrival_produce_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};
    //初始化已选单据号
    initTagsinput();
    oInit.Init = function () {
        //初始化页面上面的按钮事件
    	var btn_query = $("#arrival_produce_formSearch #btn_query");
    	//添加日期控件
    	laydate.render({
    	   elem: '#zlrqstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#zlrqend'
    	  ,theme: '#2381E9'
    	});

    	//绑定搜索发送功能
    	if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			searchArrivalProduceResult(true);
    		});
    	}
        /*-----------------------显示隐藏------------------------------------*/
        $("#arrival_produce_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(produce_turnOff){
                $("#arrival_produce_formSearch #searchMore").slideDown("low");
                produce_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#arrival_produce_formSearch #searchMore").slideUp("low");
                produce_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });
    };

    return oInit;
};
var produce_turnOff=true;

$(function(){
    //1.初始化Table
    var oTable = new arrival_produce_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new arrival_produce_ButtonInit();
    oButtonInit.Init();
	//所有下拉框添加choose样式
	jQuery('#arrival_produce_formSearch .chosen-select').chosen({width: '100%'});
	
});
