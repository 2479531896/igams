
var columnsArray = [
	{
	//     checkbox: true
	// },{
		title: '序号',
		formatter: function (value, row, index) {
			return index+1;
		},
		titleTooltip:'序号',
		width: '10%',
	    align: 'left',
	    visible:true
	},{
	    field: 'xsid',
	    title: '主键',
	    width: '20%',
	    align: 'left',
	    sortable: true,   
	    visible: false
	},{
	    field: 'oaxsdh',
	    title: '订单号',
	    width: '20%',
	    align: 'left',
	    sortable: true,   
	    visible: true
	},{
	    field: 'xslxmc',
	    title: '销售类型',
	    width: '20%',
	    align: 'left',
	    visible:false
	},{
		field: 'ddrq',
		title: '订单日期',
		width: '20%',
		align: 'left',
		sortable: true,
		visible: false
	},{
	    field: 'khjcmc',
	    title: '客户名称',
	    width: '20%',
	    align: 'left',
	    sortable: true,   
	    visible: true
	},{
		field: 'xsbmmc',
		title: '销售部门',
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
var ChooseDdhPurchase_TableInit=function(){
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#chooseDdhForm #tb_list").bootstrapTable({ 
			url: $("#chooseDdhForm input[name='urlPrefix']").val() + '/ship/ship/pagedataListDdh',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#chooseDdhForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "oaxsdh",				//排序字段
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
            uniqueId: "xsid",                     //每一行的唯一标识，一般为主键列
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
            	if($("#chooseDdhForm #cz_"+row.xsid).text()){
            		if($("#chooseDdhForm #cz_"+row.xsid).text() != "-"){
            			$("#chooseDdhForm #cz_"+row.xsid).removeAttr("class");
                		$("#chooseDdhForm #cz_"+row.xsid).text("");
						if($("#chooseDdhForm #xsmx_json").val()){
							var xsmxJson = JSON.parse($("#chooseDdhForm #xsmx_json").val());
							if(xsmxJson.length > 0){
								for (var i = xsmxJson.length-1; i >= 0; i--) {
									$("#chooseDdhForm #ddhs").tagsinput('remove',{"value":row.xsid,"text":row.oaxsdh});
									if (xsmxJson[i].xsid == row.xsid ){
										xsmxJson.splice(i,1);
									}
								}
							}
							$("#chooseDdhForm #xsmx_json").val(JSON.stringify(xsmxJson));

						}
            		}
            	}else{
            		$("#chooseDdhForm #cz_"+row.xsid).attr("class","btn btn-success");
                	$("#chooseDdhForm #cz_"+row.xsid).text("调整明细");
                	// 判断是否更新请购信息
                	var xsmxJson = [];
                	var refresh = true;
					let khjcmc = row.khjcmc;
					let oaxsdh = row.oaxsdh;
					let xslx = row.xslx;
					let xslxmc = row.xslxmc;
					let xsbm = row.xsbm;
					let xsbmmc = row.xsbmmc;
					let shdz = row.shdz;
    				if($("#chooseDdhForm #xsmx_json").val()){
						xsmxJson = JSON.parse($("#chooseDdhForm #xsmx_json").val());
    					if(xsmxJson.length > 0){
        					// for (var i = xsmxJson.length-1; i >= 0; i--) {
        					// 	if(xsmxJson[i].xsid == row.xsid){
        					// 		refresh = false;
        					// 		break;
        					// 	}
							// 	if(xsmxJson[i].khjcmc != khjcmc){
							// 		refresh = false;
							// 		$.alert("客户简称不同！");
							// 		$("#chooseDdhForm #cz_"+row.xsid).removeAttr("class");
							// 		$("#chooseDdhForm #cz_"+row.xsid).text("");
							// 		return;
							// 	}
        					// }
							refresh = false;
							$.alert("只能选择一条数据！");
							$("#chooseDdhForm #cz_"+row.xsid).removeAttr("class");
							$("#chooseDdhForm #cz_"+row.xsid).text("");
							return;
        				}
    				}

    				if(refresh){
    					// 查询请购明细信息
    					$.ajax({
    					    type:'post',  
    					    url: $("#chooseDdhForm input[name='urlPrefix']").val() + "/ship/ship/pagedataGetXsmxInfo",
    					    cache: false,
    					    data: {"xsid":row.xsid, "access_token":$("#ac_tk").val()},
    					    dataType:'json',
    					    success:function(result){
    					    	if(result.xsmxDtos != null && result.xsmxDtos.length > 0){
    					    		var isUpdate = false;
    					    		for (var i = 0; i < result.xsmxDtos.length; i++) {
										isUpdate = true;
										var sz = {"xsid":'',"xsmxid":'',"khdm":'',"khjc":'',"khjcmc":'',"oaxsdh":'',"wlid":'',"wlbm":'',"sl":'',"suil":'',"xslx":'',"xslxmc":'',"xsbm":'',"xsbmmc":'',"shdz":''};
										sz.xsid = result.xsmxDtos[i].xsid;
										sz.xsmxid = result.xsmxDtos[i].xsmxid;
										sz.khdm = result.xsmxDtos[i].khdm;
										sz.khjc = result.xsmxDtos[i].khjc;
										sz.khjcmc = khjcmc;
										sz.oaxsdh = oaxsdh;
										sz.xslx = xslx;
										sz.xslxmc = xslxmc;
										sz.xsbm = xsbm;
										sz.xsbmmc = xsbmmc;
										sz.shdz = shdz;
										sz.wlid = result.xsmxDtos[i].wlid;
										sz.wlbm = result.xsmxDtos[i].wlbm;
										sz.sl = result.xsmxDtos[i].sl;
										sz.suil = result.xsmxDtos[i].suil;
										xsmxJson.push(sz);
									}
    					    		$("#chooseDdhForm #xsmx_json").val(JSON.stringify(xsmxJson));
    					    		// 判断是否更新订单号
    					    		if(isUpdate){
    					    			$("#chooseDdhForm  #ddhs").tagsinput('add',{"value":row.xsid,"text":row.oaxsdh});
    					    		}else{
										$("#chooseDdhForm #cz_"+row.xsid).removeAttr("class");
										$("#chooseDdhForm #cz_"+row.xsid).text("-");
										$.alert("此订单出现异常！");
									}
    					    	}else{
									$.alert("此订单没有可发货数量！");
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
            sortLastName: "xsid", // 防止同名排位用
            sortLastOrder: "asc", // 防止同名排位用
			zt: "80"
            // 搜索框使用
            // search:params.search
        };
		return purchaseDdhSearchData(map);
	}
	return oTableInit
}
function purchaseDdhSearchData(map){
	var cxtj=$("#chooseDdhForm #cxtj").val();
	var cxnr=$.trim(jQuery('#chooseDdhForm #cxnr').val());
	if(cxtj=="0"){
		map["oaxsdh"]=cxnr
	}else if(cxtj=="1"){
		map["xsbmmc"]=cxnr
	}else if(cxtj=="2"){
		map["khdm"]=cxnr
	}else if(cxtj=="3"){
		map["wlbm"]=cxnr
	}else if(cxtj=="4"){
		map["wlmc"]=cxnr
	}else if(cxtj=="5"){
		map["khjcmc"]=cxnr
	}else if(cxtj=="6"){
		map["khjc"]=cxnr
	}
	// 申请开始日期
	var ddrqstart = jQuery('#chooseDdhForm #ddrqstart').val();
	map["ddrqstart"] = ddrqstart;
	// 申请结束日期
	var ddrqend = jQuery('#chooseDdhForm #ddrqend').val();
	map["ddrqend"] = ddrqend;

	// 申请开始日期
	var yfhrqstart = jQuery('#chooseDdhForm #yfhrqstart').val();
	map["yfhrqstart"] = yfhrqstart;
	// 申请结束日期
	var yfhrqend = jQuery('#chooseDdhForm #yfhrqend').val();
	map["yfhrqend"] = yfhrqend;
	return map;
}
function searchPurchaseDdhResult(isTurnBack){
	if(isTurnBack){
		$('#chooseDdhForm #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#chooseDdhForm #tb_list').bootstrapTable('refresh');
	}
}

/**
 * 操作按钮格式化函数
 * @returns
 */
function czFormat(value,row,index) {
	return "<span id='cz_"+row.xsid+"' onclick=\"reviseXsDetail('" + row.xsid + "','" + row.khjcmc + "','" + row.khjc + "','" + row.khdm + "','" + row.xslx + "','" + row.xslxmc + "','" + row.xsbm + "','" + row.xsbmmc + "','" + row.shdz + "',event)\"/>";
}
/**
 * 调整明细点击事件
 * @param
 * @param event
 * @returns
 */
function reviseXsDetail(xsid,khjcmc,khzid,kh,xslxs,xslxmcs,xsbms,xsbmmcs,shdzmc,event){
	if($("#cz_"+xsid).text() == "-"){
		return false;
	}
	khjc = khjcmc;
	khdm = kh;
	khid = khzid;
	xslx = xslxs;
	xslxmc = xslxmcs;
	xsbm = xsbms;
	xsbmmc = xsbmmcs;
	shdz = shdzmc;
	event.stopPropagation();
	var url = $("#chooseDdhForm input[name='urlPrefix']").val() + "/ship/ship/pagedataChooseListXsmxInfo?access_token=" + $("#ac_tk").val() + "&xsid=" + xsid+ "&fhid=" + $("#chooseDdhForm #fhid").val();
	$.showDialog(url, '选择明细信息', chooseDdhConfig);
}

var khjc = "";
var khdm = "";
var khid = "";
var xslx = "";
var xslxmc = "";
var xsbm = "";
var xsbmmc = "";
var shdz = "";
/**
 * 监听标签点击事件
 */
var tagClick = $("#chooseDdhForm").on('click','.label-info',function(e){
	event.stopPropagation();
	let xsmxJson = JSON.parse($("#chooseDdhForm #xsmx_json").val());
	if(xsmxJson.length > 0){
		for (let i = xsmxJson.length-1; i >= 0; i--) {
			if (xsmxJson[i].xsid == e.currentTarget.dataset.logo){
				khjc = xsmxJson[i].khjcmc;
				khdm = xsmxJson[i].khdm;
				khid = xsmxJson[i].khjc;
				xslx =xsmxJson[i].xslx;
				xslxmc =xsmxJson[i].xslxmc;
				xsbm = xsmxJson[i].xsbm;
				xsbmmc = xsmxJson[i].xsbmmc;
				shdz = xsmxJson[i].shdz;
				break;
			}
		}
	}
	var url = $("#chooseDdhForm input[name='urlPrefix']").val() + "/ship/ship/pagedataChooseListXsmxInfo?access_token=" + $("#ac_tk").val() + "&xsid=" + e.currentTarget.dataset.logo+ "&fhid=" + $("#chooseDdhForm #fhid").val();
	$.showDialog(url, '选择请购明细', chooseDdhConfig);
});
var chooseDdhConfig = {
	width : "1000px",
	modalName	: "chooseXsmxModal",
	formName	: "chooseXsmxForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#chooseXsmxForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				// 获取选中明细并保存
				if($("#chooseXsmxForm input[name='checkXsmx']:checked").length == 0){
					$.alert("未选中明细信息！");
					return false;
				}
				var xsmxJson = [];
				if($("#chooseDdhForm #xsmx_json").val()){
					xsmxJson = JSON.parse($("#chooseDdhForm #xsmx_json").val());
				}
				var xsid = $("#chooseXsmxForm input[name='xsid']").val();
				if(xsmxJson.length > 0){
					for (var i = xsmxJson.length-1; i >= 0; i--) {
						if(xsmxJson[i].xsid == xsid){
							xsmxJson.splice(i,1);
						}
					}
				}
				$("#chooseXsmxForm input[name='checkXsmx']").each(function(){
					if($(this).is(":checked")){
						var sz={"xsid":'',"xsmxid":'',"khjcmc":'',"khdm":'','khjc':'',"xslx":'',"xslxmc":'',"xsbm":'',"xsbmmc":'',"shdz":''};
						sz.xsid = xsid;
						sz.xsmxid = this.dataset.xsmxid;
						sz.khjcmc = khjc;
						sz.khdm = khdm;
						sz.khjc = khid;
						sz.xslx = xslx;
						sz.xslxmc = xslxmc;
						sz.xsbm = xsbm;
						sz.xsbmmc = xsbmmc;
						sz.shdz = shdz;
						xsmxJson.push(sz);
					}
				})
				$("#chooseDdhForm #xsmx_json").val(JSON.stringify(xsmxJson));
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
var ChooseDdhPurchase_ButtonInit= function (){
	var oInit=new Object();
	var postdata = {};
	oInit.Init=function(){
		//添加日期控件
    	laydate.render({
    	   elem: '#chooseDdhForm #ddrqstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#chooseDdhForm #ddrqend'
    	  ,theme: '#2381E9'
    	});
		//添加日期控件
		laydate.render({
			elem: '#chooseDdhForm #yfhrqstart'
			,theme: '#2381E9'
		});
		//添加日期控件
		laydate.render({
			elem: '#chooseDdhForm #yfhrqend'
			,theme: '#2381E9'
		});
    	//初始化已选单据号
    	initTagsinput();
    	
		var btn_query=$("#chooseDdhForm #btn_query");
		/*--------------------------------模糊查询---------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchPurchaseDdhResult(true); 
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
	$("#chooseDdhForm  #ddhs").tagsinput({
		itemValue: "value",
		itemText: "text",
	})
	// 初始化已选明细json
	$("#chooseDdhForm #djhs").val($("#editContractForm #htmx_json").val());
}

/**
 * 监听标签移除事件
 */
var tagsRemoved = $('#chooseDdhForm #ddhs').on('beforeItemRemove', function(event) {
	var xsmxJson = [];
	if($("#chooseDdhForm #xsmx_json").val()){
		xsmxJson = JSON.parse($("#chooseDdhForm #xsmx_json").val());
	}
	var xsid = event.item.value;
	if(xsmxJson.length > 0){
		for (var i = xsmxJson.length-1; i >= 0; i--) {
			if(xsmxJson[i].xsid == xsid){
				xsmxJson.splice(i,1);
			}
		}
	}
	$("#chooseDdhForm #xsmx_json").val(JSON.stringify(xsmxJson));
});

$(function(){
	// 1.初始化Table
	var oTable = new ChooseDdhPurchase_TableInit();
	oTable.Init();
	//2.初始化Button的点击事件
	var oButtonInit = new ChooseDdhPurchase_ButtonInit();
	oButtonInit.Init();
	jQuery('#chooseDdhForm .chosen-select').chosen({width: '100%'});
})