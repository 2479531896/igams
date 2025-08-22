var rece_progress_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#rece_progress #rece_progress_tb_list').bootstrapTable({
            url: $('#rece_progress #urlPrefix').val() + '/progress/progress/pageGetListWaitProgress',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#rece_progress #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
//            paginationDetailHAlign: " hidden", //不显示底部文字
            sortable: true,                     //是否启用排序
            sortName: "cpxqjh.lrsj",				//排序字段
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
            uniqueId: "cpxqid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
				width: '2%',
            }, {
                field: 'xqdh',
                title: '需求单号',
                titleTooltip:'需求单号',
                width: '10%',
                align: 'left',
				sortable: true,
                visible: true
            }, {
                field: 'sqrmc',
                title: '申请人',
                titleTooltip:'申请人',
                width: '6%',
                align: 'left',
				visible: true
            // }, {
            //     field: 'sqbmmc',
            //     title: '申请部门',
            //     titleTooltip:'申请部门',
            //     width: '6%',
            //     align: 'left',
            //     visible: true,
            }, {
                field: 'xqrq',
                title: '需求日期',
                titleTooltip:'需求日期',
                width: '8%',
                align: 'left',
				sortable: true,
                visible: true
            }, {
                field: 'yjyt',
                title: '预计用途',
                titleTooltip:'预计用途',
                width: '12%',
                align: 'left',
                visible: true
            }, {
                field: 'bz',
                title: '备注',
                titleTooltip:'备注',
                width: '12%',
                align: 'left',
                visible: true
            },{
                field: 'cz',
                title: '操作',
                titleTooltip:'操作',
                width: '10%',
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
                if($("#rece_progress #cz_"+row.cpxqid).text()){
                    if($("#rece_progress #cz_"+row.cpxqid).text() != "-"){
                        $("#rece_progress #cz_"+row.cpxqid).removeAttr("class");
                        $("#rece_progress #cz_"+row.cpxqid).text("");
                    }
                }else{
                    $("#rece_progress #cz_"+row.cpxqid).attr("class","btn btn-success");
                    $("#rece_progress #cz_"+row.cpxqid).text("调整明细");
                    // 判断是否更新请购信息
                    var xqmx_json = [];
                    var refresh = true;
                    if($("#rece_progress #xqmx_json").val()){
                        xqmx_json = JSON.parse($("#rece_progress #xqmx_json").val());
                        if(xqmx_json.length > 0){
                            for (var i = xqmx_json.length-1; i >= 0; i--) {
                                if(xqmx_json[i].cpxqid == row.cpxqid){
                                    refresh = false;
                                    break;
                                }
                            }
                        }
                    }
                    if(refresh){
                        $.ajax({
                            type:'post',
                            url: $("#rece_progress input[name='urlPrefix']").val() + "/storehouse/receiveMateriel/pagedataProgressListInfo",
                            cache: false,
                            data: {"xqjhid":row.cpxqid, "access_token":$("#ac_tk").val()},
                            dataType:'json',
                            success:function(result){
                                if(result.dtoList != null && result.dtoList.length > 0){
                                    for (var i = 0; i < result.dtoList.length; i++) {
                                        var sz = {"cpxqid":'',"xqjhmxid":''};
                                        sz.cpxqid = row.cpxqid;
                                        sz.xqjhmxid = result.dtoList[i].xqjhmxid;
                                        xqmx_json.push(sz);
                                    }
                                    $("#rece_progress #xqmx_json").val(JSON.stringify(xqmx_json));
                                    $("#rece_progress  #xqdh").tagsinput('add',{"value":row.cpxqid,"text":row.xqdh});
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
            sortLastName: "cpxqjh.cpxqid", //防止同名排位用
            zt: "80", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
        };

        return getReceProgressSearchData(map);
    };
    return oTableInit;
};
/**
 * 操作按钮格式化函数
 * @returns
 */
function czFormat(value,row,index) {
    return "<span id='cz_"+row.cpxqid+"' onclick=\"reviseXqDetail('" + row.cpxqid + "',event)\"/>";
}
function searchReceProgressResult(isTurnBack){
	if(isTurnBack){
		$('#rece_progress #rece_progress_tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#rece_progress #rece_progress_tb_list').bootstrapTable('refresh');
	}
}
/**
 * 调整明细点击事件
 * @param qgid
 * @param event
 * @returns
 */
function reviseXqDetail(cpxqid, event){
    if($("#cz_"+cpxqid).text() == "-"){
        return false;
    }
    event.stopPropagation();
    var url = $("#rece_progress input[name='urlPrefix']").val() + "/storehouse/receiveMateriel/pagedataChooseListProgressInfo?access_token=" + $("#ac_tk").val() + "&cpxqid=" + cpxqid;
    $.showDialog(url, '选择需求明细', chooseXqmxConfig);
}

function getReceProgressSearchData(map){
	var cxtj = $("#rece_progress #cxtj").val();
	// 查询条件
	var cxnr = $.trim(jQuery('#rece_progress #cxnr').val());
	// '0':'物料编号','1':'物料名称','2':'生产商','3':'货号'
	if (cxtj == "0") {
		map["entire"] = cxnr;
	}else if (cxtj == "1") {
		map["xqdh"] = cxnr;
	}else if (cxtj == "2") {
		map["sqbmmc"] = cxnr;
	}else if (cxtj == "3") {
		map["sqrmc"] = cxnr;
	}else if (cxtj == "4") {
		map["wlbm"] = cxnr;
    }else if (cxtj == "5") {
    	map["wlmc"] = cxnr;
	}
	// 需求开始日期
	var xqrqstart = jQuery('#rece_progress #xqrqstart').val();
	map["xqrqstart"] = xqrqstart;
	// 需求结束日期
	var xqrqend = jQuery('#rece_progress #xqrqend').val();
	map["xqrqend"] = xqrqend;

	return map;
}


var rece_progress_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
    	var btn_query = $("#rece_progress #btn_query");

    	//添加日期控件
    	laydate.render({
    	   elem: '#xqrqstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#xqrqend'
    	  ,theme: '#2381E9'
    	});
        initTagsinput();
    	//绑定搜索发送功能
    	if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			searchReceProgressResult(true);
    		});
    	}
    };

    return oInit;
};
/**
 * 监听标签移除事件
 */
var tagsRemoved = $('#rece_progress #xqdh').on('beforeItemRemove', function(event) {
    var xqmx_json = [];
    if($("#rece_progress #xqmx_json").val()){
        xqmx_json = JSON.parse($("#rece_progress #xqmx_json").val());
    }
    var cpxqid = event.item.value;
    if(xqmx_json.length > 0){
        for (var i = xqmx_json.length-1; i >= 0; i--) {
            if(xqmx_json[i].cpxqid == cpxqid){
                xqmx_json.splice(i,1);
            }
        }
    }
    $("#rece_progress #xqmx_json").val(JSON.stringify(xqmx_json));
});

var chooseXqmxConfig = {
    width : "1000px",
    modalName	: "chooseXqmxModal",
    formName	: "chooseXqmxForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#chooseXqmxForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                // 获取选中明细并保存
                if($("#chooseXqmxForm input[name='checkXqmx']:checked").length == 0){
                    $.alert("未选中请购明细信息！");
                    return false;
                }
                var xqmx_json = [];
                if($("#rece_progress #xqmx_json").val()){
                    xqmx_json = JSON.parse($("#rece_progress #xqmx_json").val());
                }
                var cpxqid = $("#chooseXqmxForm input[name='cpxqid']").val();
                if(xqmx_json.length > 0){
                    for (var i = xqmx_json.length-1; i >= 0; i--) {
                        if(xqmx_json[i].cpxqid == cpxqid){
                            xqmx_json.splice(i,1);
                        }
                    }
                }
                $("#chooseXqmxForm input[name='checkXqmx']").each(function(){
                    if($(this).is(":checked")){
                        var sz={"cpxqid":'',"xqjhmxid":''};
                        sz.cpxqid = cpxqid;
                        sz.xqjhmxid = this.dataset.xqjhmxid;
                        xqmx_json.push(sz);
                    }
                })
                $("#rece_progress #xqmx_json").val(JSON.stringify(xqmx_json));
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

/**
 * 监听标签点击事件
 */
var tagClick = $("#rece_progress").on('click','.label-info',function(e){
    event.stopPropagation();
    var url = $("#rece_progress input[name='urlPrefix']").val() + "/storehouse/receiveMateriel/pagedataChooseListProgressInfo?access_token=" + $("#ac_tk").val() + "&cpxqid=" + e.currentTarget.dataset.logo;
    $.showDialog(url, '选择需求明细', chooseXqmxConfig);
});
/**
 * 初始化需求单号
 * @returns
 */
function initTagsinput(){
    $("#rece_progress  #xqdh").tagsinput({
        itemValue: "value",
        itemText: "text",
    })
}
$(function(){
    //1.初始化Table
    var oTable = new rece_progress_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new rece_progress_ButtonInit();
    oButtonInit.Init();
	//所有下拉框添加choose样式
	jQuery('#rece_progress .chosen-select').chosen({width: '100%'});
	
});
