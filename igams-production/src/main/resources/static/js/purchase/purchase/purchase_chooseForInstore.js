

var columnsArray = [
	{
	    checkbox: true
	},{
		title: '序号',
		formatter: function (value, row, index) {
			return index+1;
		},
		titleTooltip:'序号',
		width: '10%',
	    align: 'left',
	    visible:true
	},{
	    field: 'qgid',
	    title: '请购ID',
	    width: '20%',
	    align: 'left',
	    sortable: true,   
	    visible: false
	},{
	    field: 'djh',
	    title: '请购单号',
	    width: '20%',
	    align: 'left',
	    sortable: true,   
	    visible: true
	},{
	    field: 'sqrmc',
	    title: '申请人',
	    width: '20%',
	    align: 'left',
	    visible:true
	},{
	    field: 'sqbmmc',
	    title: '申请部门',
	    width: '20%',
	    align: 'left',
	    sortable: true,   
	    visible: true
	},{
	    field: 'sqrq',
	    title: '申请时间',
	    width: '20%',
	    align: 'left',
	    sortable: true,   
	    visible: true
	},{
		field: 'cz',
		title: '操作',
		titleTooltip:'操作',
		width: '20%',
		align: 'left',
		formatter:czFormat,
		visible: true
	}]
var ChoosePurchase_TableInit=function(){
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#chooseQgForm #purchase_list").bootstrapTable({ 
			url: $("#chooseQgForm input[name='urlPrefix']").val() + '/purchase/purchase/pagedataAdminPurchase',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#chooseQgForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "qggl.lrsj",				//排序字段
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
            uniqueId: "qgid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onClickRow: function (row, $element) {
            	if($("#chooseQgForm #cz_"+row.qgid).text()){
            		if($("#chooseQgForm #cz_"+row.qgid).text() != "-"){
            			$("#chooseQgForm #cz_"+row.qgid).removeAttr("class");
                		$("#chooseQgForm #cz_"+row.qgid).text("");
            		}
            	}else{
            		$("#chooseQgForm #cz_"+row.qgid).attr("class","btn btn-success");
                	$("#chooseQgForm #cz_"+row.qgid).text("调整明细");
                	// 判断是否更新请购信息
                	var qgmxJson = [];
                	var refresh = true;
    				if($("#chooseQgForm #qgmx_json").val()){
    					qgmxJson = JSON.parse($("#chooseQgForm #qgmx_json").val());
    					if(qgmxJson.length > 0){
        					for (var i = qgmxJson.length-1; i >= 0; i--) {
        						if(qgmxJson[i].qgid == row.qgid){
        							refresh = false;
        							break;
        						}
        					}
        				}
    				}
    				if(refresh){
    					// 查询请购明细信息
    					$.ajax({
    					    type:'post',  
    					    url: $("#chooseQgForm input[name='urlPrefix']").val() + "/purchase/purchase/pagedataGetAdminPurchaseInfo",
    					    cache: false,
    					    data: {"qgid":row.qgid, "access_token":$("#ac_tk").val()},
    					    dataType:'json',
    					    success:function(result){
    					    	if(result.qgmxDtos != null && result.qgmxDtos.length > 0){
    					    		for (var i = 0; i < result.qgmxDtos.length; i++) {
										var sz = {"qgid":'',"qgmxid":''};
										sz.qgid = result.qgmxDtos[i].qgid;
										sz.qgmxid = result.qgmxDtos[i].qgmxid;
										qgmxJson.push(sz);
									}
    					    		$("#chooseQgForm #qgmx_json").val(JSON.stringify(qgmxJson));
									$("#chooseQgForm  #yxdjh").tagsinput('add',{"value":row.qgid,"text":row.djh});
    					    	}
    					    }
    					});
    				}
            	}
            },
            onDblClickRow: function (row, $element) {
            	return;
             },
		});
	}
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   // 页面大小
        	pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "qggl.qgid", // 防止同名排位用
            sortLastOrder: "asc", // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		return purchaseSearchData(map);
	}
	return oTableInit
}
function purchaseSearchData(map){
	var cxtj=$("#chooseQgForm #cxtj").val();
	var cxnr=$.trim(jQuery('#chooseQgForm #cxnr').val());
	if(cxtj=="0"){
		map["djh"]=cxnr
	}else if(cxtj=="1"){
		map["sqrmc"]=cxnr
	}else if(cxtj=="2"){
		map["sqbmmc"]=cxnr
	}
	return map;
}
function searchPurchaseResult(isTurnBack){
	if(isTurnBack){
		$('#chooseQgForm #purchase_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#chooseQgForm #purchase_list').bootstrapTable('refresh');
	}
}

/**
 * 操作按钮格式化函数
 * @returns
 */
function czFormat(value,row,index) {
	return "<span id='cz_"+row.qgid+"' onclick=\"reviseDetail('" + row.qgid + "',event)\"/>";
}
/**
 * 调整明细点击事件
 * @param qgid
 * @param event
 * @returns
 */
function reviseDetail(qgid, event){
	if($("#cz_"+qgid).text() == "-"){
		return false;
	}
	event.stopPropagation();
	var url = $("#chooseQgForm input[name='urlPrefix']").val() + "/purchase/purchase/pagedataChooseListAdminPurchaseInfo?access_token=" + $("#ac_tk").val() + "&qgid=" + qgid
	$.showDialog(url, '选择请购明细', chooseQgmxConfig);
}
var chooseQgmxConfig = {
	width : "1000px",
	modalName	: "chooseQgmxModal",
	formName	: "chooseMxForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#chooseMxForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				// 获取选中明细并保存
				if($("#chooseMxForm input[name='checkQgmx']:checked").length == 0){
					$.alert("未选中请购明细信息！");
					return false;
				}
				var qgmxJson = [];
				if($("#chooseQgForm #qgmx_json").val()){
					qgmxJson = JSON.parse($("#chooseQgForm #qgmx_json").val());
				}
				var qgid = $("#chooseMxForm input[name='qgid']").val();
				if(qgmxJson.length > 0){
					for (var i = qgmxJson.length-1; i >= 0; i--) {
						if(qgmxJson[i].qgid == qgid){
							qgmxJson.splice(i,1);
						}
					}
				}
				$("#chooseMxForm input[name='checkQgmx']").each(function(){
					if($(this).is(":checked")){
						var sz={"qgid":'',"qgmxid":''};
						sz.qgid = qgid;
						sz.qgmxid = this.dataset.qgmxid;
						qgmxJson.push(sz);
					}
				})
				$("#chooseQgForm #qgmx_json").val(JSON.stringify(qgmxJson));
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
 * 初始化页面
 */
var ChoosePurchase_ButtonInit= function (){
	var oInit=new Object();
	var postdata = {};
	oInit.Init=function(){
    	//初始化已选单据号
    	initTagsinput();

		var btn_query=$("#chooseQgForm #btn_query");
		/*--------------------------------模糊查询---------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchPurchaseResult(true); 
    		});
		}
	}
	return oInit;
}

/**
 * 初始化已选订单号
 * @returns
 */
function initTagsinput(){
	$("#chooseQgForm  #yxdjh").tagsinput({
		itemValue: "value",
		itemText: "text",
	})
}

/**
 * 监听标签点击事件
 */
var tagClick = $("#chooseQgForm").on('click','.label-info',function(e){
	event.stopPropagation();
	var url = $("#chooseQgForm input[name='urlPrefix']").val() + "/purchase/purchase/pagedataChooseListAdminPurchaseInfo?access_token=" + $("#ac_tk").val() + "&qgid=" + e.currentTarget.dataset.logo;
	$.showDialog(url, '选择请购明细', chooseQgmxConfig);
});
/**
 * 监听标签移除事件
 */
var tagsRemoved = $('#chooseQgForm #yxdjh').on('beforeItemRemove', function(event) {
	var qgmxJson = [];
	if($("#chooseQgForm #qgmx_json").val()){
		qgmxJson = JSON.parse($("#chooseQgForm #qgmx_json").val());
	}
	var qgid = event.item.value;
	if(qgmxJson.length > 0){
		for (var i = qgmxJson.length-1; i >= 0; i--) {
			if(qgmxJson[i].qgid == qgid){
				qgmxJson.splice(i,1);
			}
		}
	}
	$("#chooseQgForm #qgmx_json").val(JSON.stringify(qgmxJson));
});


$(function(){
	// 1.初始化Table
	var oTable = new ChoosePurchase_TableInit();
	oTable.Init();
	//2.初始化Button的点击事件
	var oButtonInit = new ChoosePurchase_ButtonInit();
	oButtonInit.Init();
	jQuery('#chooseQgForm .chosen-select').chosen({width: '100%'});
})