var columnsArray = [
	{
	    checkbox: true,
		width: '5%'
	},{
		title: '序',
		formatter: function (value, row, index) {
			return index+1;
		},
		titleTooltip:'序',
		width: '5%',
	    align: 'left',
	    visible:true
	},{
	    field: 'fhdh',
	    title: '发货单号',
	    width: '25%',
	    align: 'left',
	    sortable: true,   
	    visible: true
	},{
	    field: 'khmc',
	    title: '客户',
	    width: '25%',
	    align: 'left',
	    sortable: true,   
	    visible: true
	},{
	    field: 'xsbmmc',
	    title: '销售部门',
	    width: '10%',
	    align: 'left',
	    sortable: true,   
	    visible: true
	}, {				
		field: 'djrq',
		title: '单据日期',
		width: '15%',
		align: 'left',
		visible: true
	},{
		field: 'cz',
		title: '操作',
		titleTooltip:'操作',
		width: '15%',
		align: 'left',
		formatter:chooseFhglForm_czFormat,
		visible: true
	}]
var ChooseDeliver_TableInit=function(){
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#chooseFhglForm #deliver_list").bootstrapTable({
			url: $("#chooseFhglForm input[name='urlPrefix']").val() + '/storehouse/storehouse/pagedataGetPageListAlldeliver?zt='+$("#chooseFhglForm #zt").val(),         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#chooseFhglForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "fhgl.lrsj",				//排序字段
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
            uniqueId: "fhid",                     //每一行的唯一标识，一般为主键列
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
            	if($("#chooseFhglForm #cz_"+row.fhid).text()){
            		if($("#chooseFhglForm #cz_"+row.fhid).text() != "-"){
            			$("#chooseFhglForm #cz_"+row.fhid).removeAttr("class");
                		$("#chooseFhglForm #cz_"+row.fhid).text("");
						if($("#chooseFhglForm #fhmx_json").val()){
							var fhmx_json = JSON.parse($("#chooseFhglForm #fhmx_json").val());
							if(fhmx_json.length > 0){
								for (var i = fhmx_json.length-1; i >= 0; i--) {
									$("#chooseFhglForm #yxdjh").tagsinput('remove',{"value":row.fhid,"text":row.fhdh});
									if (fhmx_json[i].fhid == row.fhid ){
										fhmx_json.splice(i,1);
									}
								}
							}
							$("#chooseFhglForm #fhmx_json").val(JSON.stringify(fhmx_json));

						}
            		}
            	}else{
            		$("#chooseFhglForm #cz_"+row.fhid).attr("class","btn btn-success");
                	$("#chooseFhglForm #cz_"+row.fhid).text("调整明细");
                	// 判断是否更新明细信息
                	var fhmxJson = [];
                	var refresh = true;
    				if($("#chooseFhglForm #fhmx_json").val()){
    					fhmxJson = JSON.parse($("#chooseFhglForm #fhmx_json").val());
    					if(fhmxJson.length > 0){
        					// for (var i = fhmxJson.length-1; i >= 0; i--) {
        					// 	if(fhmxJson[i].fhid == row.fhid){
        					// 		refresh = false;
        					// 		break;
        					// 	}
        					// }
							refresh = false;
							$.alert("只能选择一条数据！");
							$("#chooseFhglForm #cz_"+row.fhid).removeAttr("class");
							$("#chooseFhglForm #cz_"+row.fhid).text("");
							return;
        				}
    				}
    				if(refresh){
    					// 查询明细信息
    					$.ajax({
    					    type:'post',  
    					    url: $("#chooseFhglForm input[name='urlPrefix']").val() + "/storehouse/storehouse/pagedataGetdeliverInfo", 
    					    cache: false,
    					    data: {"fhid":row.fhid,  "access_token":$("#ac_tk").val()},  
    					    dataType:'json',
    					    success:function(result){
    					    	if(result.fhmxList != null && result.fhmxList.length > 0){
    					    		for (var i = 0; i < result.fhmxList.length; i++) {
    					    			var sl = 0;
    					    			if(result.fhmxList[i].kthsl){
    					    				sl = result.fhmxList[i].kthsl;
    					    			}
					    				var sz = {"fhid":'',"fhmxid":''};
    									sz.fhid = result.fhmxList[i].fhid;
    									sz.fhmxid = result.fhmxList[i].fhmxid;
    									fhmxJson.push(sz);
									}
    					    		$("#chooseFhglForm #fhmx_json").val(JSON.stringify(fhmxJson));
    					    		$("#chooseFhglForm  #yxdjh").tagsinput('add',{"value":row.fhid,"text":row.fhdh});
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
            sortLastName: "fhgl.fhid", // 防止同名排位用
            sortLastOrder: "asc", // 防止同名排位用
        };
		return chooseFhSearchData(map);
	}
	return oTableInit
}
function chooseFhSearchData(map){
	var cxtj=$("#chooseFhglForm #cxtj").val();
	var cxnr=$.trim(jQuery('#chooseFhglForm #cxnr').val());
	if(cxtj=="0"){
		map["entire"]=cxnr;
	}else if(cxtj=="1"){
		map["fhdh"]=cxnr;
	}else if(cxtj=="2"){
		map["khmc"]=cxnr;
	}else if (cxtj == "3") {
    	map["xsbmmc"] = cxnr;
	}
	// 创建开始日期
	var shsjstart = jQuery('#chooseHtxxForm #shsjstart').val();
	map["shsjstart"] = shsjstart;
	
	// 创建结束日期
	var shsjend = jQuery('#chooseHtxxForm #shsjend').val();
	map["shsjend"] = shsjend;
	return map;
}
function searchChooseFhglResult(isTurnBack){
	if(isTurnBack){
		$('#chooseFhglForm #deliver_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#chooseFhglForm #deliver_list').bootstrapTable('refresh');
	}
}

/**
 * 操作按钮格式化函数
 * @returns
 */
function chooseFhglForm_czFormat(value,row,index) {
	return "<span id='cz_"+row.fhid+"' onclick=\"reviseDetailTh('" + row.fhid + "',event)\"/>";
}
/**
 * 调整明细点击事件
 * @param htid
 * @param event
 * @returns
 */
function reviseDetailTh(fhid, event){
	if($("#cz_"+fhid).text() == "-"){
		return false;
	}
	event.stopPropagation();
	var url = $("#chooseFhglForm input[name='urlPrefix']").val() + "/storehouse/storehouse/pagedataChooseListDeliverGoodsInfo?access_token=" + $("#ac_tk").val() + "&fhid=" + fhid;
	$.showDialog(url, '选择发货明细', choosefhmxConfig);
}
var choosefhmxConfig = {
	width : "1000px",
	modalName	: "chooseFhmxModal",
	formName	: "chooseFhmxForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#chooseFhmxForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				// 获取选中明细并保存
				if($("#chooseFhmxForm input[name='checkFhmx']:checked").length == 0){
					$.alert("未选中明细信息！");
					return false;
				}
				var fhmxJson = [];
				if($("#chooseFhglForm #fhmx_json").val()){
					fhmxJson = JSON.parse($("#chooseFhglForm #fhmx_json").val());
				}
				var fhid = $("#chooseFhmxForm input[name='fhid']").val();
				if(fhmxJson.length > 0){
					for (var i = fhmxJson.length-1; i >= 0; i--) {
						if(fhmxJson[i].fhid == fhid){
							fhmxJson.splice(i,1);
						}
					}
				}
				$("#chooseFhmxForm input[name='checkFhmx']").each(function(){
					if($(this).is(":checked")){
						var sz={"fhid":'',"fhmxid":''};
						sz.fhid = fhid;
						sz.fhmxid = this.dataset.fhmxid;
						fhmxJson.push(sz);
					}
				})
				$("#chooseFhglForm #fhmx_json").val(JSON.stringify(fhmxJson));
				preventResubmitForm(".modal-footer > button", true);
				$.closeModal(opts.modalName);
				preventResubmitForm(".modal-footer > button", false);
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
var ChooseDeliver_ButtonInit= function (){
	var oInit=new Object();
	var postdata = {};
	oInit.Init=function(){
		//添加日期控件
    	laydate.render({
    	   elem: '#chooseFhglForm #shsjstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#chooseFhglForm #shsjend'
    	  ,theme: '#2381E9'
    	});
    	//初始化已选单据号
    	initTagsinput();
    	
		var btn_query=$("#chooseFhglForm #btn_query");
		/*--------------------------------模糊查询---------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchChooseFhglResult(true); 
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
	$("#chooseFhglForm  #yxdjh").tagsinput({
		itemValue: "value",
		itemText: "text",
	})
	// 初始化已选明细json
	$("#chooseFhglForm #djhs").val($("#deliverGoodsForm #fhmx_json").val());
}

/**
 * 监听标签点击事件
 */
var tagClick = $("#chooseFhglForm").on('click','.label-info',function(e){
	event.stopPropagation();
	var url = $("#chooseFhglForm input[name='urlPrefix']").val() + "/storehouse/storehouse/pagedataChooseListDeliverGoodsInfo?access_token=" + $("#ac_tk").val() + "&fhid=" + e.currentTarget.dataset.logo;
	$.showDialog(url, '选择发货明细', choosefhmxConfig);
});
/**
 * 监听标签移除事件
 */
var tagsRemoved = $('#chooseFhglForm #yxdjh').on('beforeItemRemove', function(event) {
	var fhmxJson = [];
	if($("#chooseFhglForm #fhmx_json").val()){
		fhmxJson = JSON.parse($("#chooseFhglForm #fhmx_json").val());
	}
	var fhid = event.item.value;
	if(fhmxJson.length > 0){
		for (var i = fhmxJson.length-1; i >= 0; i--) {
			if(fhmxJson[i].fhid == fhid){
				fhmxJson.splice(i,1);
			}
		}
	}
	$("#chooseFhglForm #fhmx_json").val(JSON.stringify(fhmxJson));
});

$(function(){
	// 1.初始化Table
	var oTable = new ChooseDeliver_TableInit();
	oTable.Init();
	//2.初始化Button的点击事件
	var oButtonInit = new ChooseDeliver_ButtonInit();
	oButtonInit.Init();
	jQuery('#chooseFhglForm .chosen-select').chosen({width: '100%'});
})