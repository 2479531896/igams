var Purchase_turnOff=true;

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
	var qglx=$("#chooseQgxxForm #qglx").val();
	var qglbdm = $("#chooseQgxxForm #qglbdm").val();
	var htaddflag = $("#chooseQgxxForm #htaddflag").val();
	var url='/purchase/purchase/pageGetListAllPurchase'+"?qglx="+qglx+"&qglbdm="+qglbdm+"&htaddflag="+htaddflag;
		oTableInit.Init=function(){
		$("#chooseQgxxForm #purchase_list").bootstrapTable({
			url: $("#chooseQgxxForm input[name='urlPrefix']").val() +url,         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#chooseQgxxForm #toolbar',                //工具按钮用哪个容器
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
            	if($("#chooseQgxxForm #cz_"+row.qgid).text()){
            		if($("#chooseQgxxForm #cz_"+row.qgid).text() != "-"){
            			$("#chooseQgxxForm #cz_"+row.qgid).removeAttr("class");
                		$("#chooseQgxxForm #cz_"+row.qgid).text("");
            		}
            	}else{
            		$("#chooseQgxxForm #cz_"+row.qgid).attr("class","btn btn-success");
                	$("#chooseQgxxForm #cz_"+row.qgid).text("调整明细");
                	// 判断是否更新请购信息
                	var qgmxJson = [];
                	var refresh = true;
    				if($("#chooseQgxxForm #qgmx_json").val()){
    					qgmxJson = JSON.parse($("#chooseQgxxForm #qgmx_json").val());
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
    					var htid = $("#editContractForm #htid").val();
						let lllb =$("#chooseQgxxForm #lllb").val();
    					// 查询请购明细信息
    					$.ajax({
    					    type:'post',  
    					    url: $("#chooseQgxxForm input[name='urlPrefix']").val() + "/purchase/purchase/pagedataGetPurchaseInfo",
    					    cache: false,
    					    data: {"qgid":row.qgid, "htid":htid,"lllb":lllb, "access_token":$("#ac_tk").val()},
    					    dataType:'json',
    					    success:function(result){
    					    	if(result.htmxDtos != null && result.htmxDtos.length > 0){
    					    		var isUpdate = false;
    					    		for (var i = 0; i < result.htmxDtos.length; i++) {
    					    			var sl = 0;
    					    			if(result.htmxDtos[i].sl){
    					    				sl = result.htmxDtos[i].sl;
    					    			}
    					    			var ydsl = 0;
    					    			if(result.htmxDtos[i].ydsl ){
    					    				ydsl = result.htmxDtos[i].ydsl;
    					    			}
    					    			if(parseFloat(result.htmxDtos[i].sysl)-parseFloat(ydsl) + parseFloat(sl) > 0 && lllb =="0"){
    					    				isUpdate = true;
    					    				var sz = {"qgid":'',"qgmxid":''};
        									sz.qgid = result.htmxDtos[i].qgid;
        									sz.qgmxid = result.htmxDtos[i].qgmxid;
        									qgmxJson.push(sz);
        					    		}
										if(lllb =="2"){
											isUpdate = true;
											var sz = {"qgid":'',"qgmxid":''};
											sz.qgid = result.htmxDtos[i].qgid;
											sz.qgmxid = result.htmxDtos[i].qgmxid;
											qgmxJson.push(sz);
										}
										if(parseFloat(result.htmxDtos[i].klsl) > 0 && lllb =="1"){
											isUpdate = true;
											var sz = {"qgid":'',"qgmxid":'',"ckqxlx":'',"wlid":''};
											sz.qgid = result.htmxDtos[i].qgid;
											sz.wlid = result.htmxDtos[i].wlid;
											sz.ckqxlx = result.htmxDtos[i].ckqxlx;
											sz.qgmxid = result.htmxDtos[i].qgmxid;
											qgmxJson.push(sz);
										}
									}
    					    		$("#chooseQgxxForm #qgmx_json").val(JSON.stringify(qgmxJson));
    					    		// 判断是否更新订单号
    					    		if(isUpdate){
    					    			$("#chooseQgxxForm  #yxdjh").tagsinput('add',{"value":row.qgid,"text":row.djh});
    					    		}else{
										$("#chooseQgxxForm #cz_"+row.qgid).removeAttr("class");
										$("#chooseQgxxForm #cz_"+row.qgid).text("-");
										if (lllb =="0"){
											$.alert("此订单明细都在采购中！");
										}else{
											$.alert("此订单可领数量为0！");
										}

									}
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
			lllb: $("#chooseQgxxForm #lllb").val(),
            // 搜索框使用
            // search:params.search
        };
		return purchaseSearchData(map);
	}
	return oTableInit
}
function purchaseSearchData(map){
	var cxtj=$("#chooseQgxxForm #cxtj").val();
	var cxnr=$.trim(jQuery('#chooseQgxxForm #cxnr').val());
	if(cxtj=="0"){
		map["djh"]=cxnr
	}else if(cxtj=="1"){
		map["sqrmc"]=cxnr
	}else if(cxtj=="2"){
		map["sqbmmc"]=cxnr
	}else if(cxtj=="3"){
		map["wlbm"]=cxnr
	}
	// 支付方式
	var zffss = jQuery('#chooseQgxxForm #zffs_id_tj').val();
	map["zffss"] = zffss;
	// 付款方
	var fkfs = jQuery('#chooseQgxxForm #fkf_id_tj').val();
	map["fkfs"] = fkfs;
	// 项目编码
	var xmbms = jQuery('#chooseQgxxForm #xmbm_id_tj').val();
	map["xmbms"] = xmbms;
	// 项目大类
	var xmdls = jQuery('#chooseQgxxForm #xmdl_id_tj').val();
	map["xmdls"] = xmdls;
	// 申请开始日期
	var sqrqstart = jQuery('#chooseQgxxForm #sqrqstart').val();
	map["sqrqstart"] = sqrqstart;
	// 申请结束日期
	var sqrqend = jQuery('#chooseQgxxForm #sqrqend').val();
	map["sqrqend"] = sqrqend;
	// 状态(默认80)
	map["zts"] = "80";
	// 完成标记为0
	let lllb =$("#chooseQgxxForm #lllb").val();
	map["wcbj"]= lllb;
	//请购类别
	var qglbs = jQuery('#chooseQgxxForm #qglb_id_tj').val();
	map["qglbs"] = qglbs;
	return map;
}
function searchPurchaseResult(isTurnBack){
	//关闭高级搜索条件
	$("#chooseQgxxForm #searchMore").slideUp("low");
	Purchase_turnOff=true;
	$("#chooseQgxxForm #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#chooseQgxxForm #purchase_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#chooseQgxxForm #purchase_list').bootstrapTable('refresh');
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
	let lllb =$("#chooseQgxxForm #lllb").val();
	var url = $("#chooseQgxxForm input[name='urlPrefix']").val() + "/purchase/purchase/pagedataChooseListPurchaseInfo?access_token=" + $("#ac_tk").val() + "&qgid=" + qgid+"&lllb="+lllb;
	$.showDialog(url, '选择请购明细', chooseQgmxConfig);
}
var chooseQgmxConfig = {
	width : "1000px",
	modalName	: "chooseQgmxModal",
	formName	: "chooseQgmxForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#chooseQgmxForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				// 获取选中明细并保存
				if($("#chooseQgmxForm input[name='checkQgmx']:checked").length == 0){
					$.alert("未选中请购明细信息！");
					return false;
				}
				var qgmxJson = [];
				if($("#chooseQgxxForm #qgmx_json").val()){
					qgmxJson = JSON.parse($("#chooseQgxxForm #qgmx_json").val());
				}
				var qgid = $("#chooseQgmxForm input[name='qgid']").val();
				if(qgmxJson.length > 0){
					for (var i = qgmxJson.length-1; i >= 0; i--) {
						if(qgmxJson[i].qgid == qgid){
							qgmxJson.splice(i,1);
						}
					}
				}
				$("#chooseQgmxForm input[name='checkQgmx']").each(function(){
					if($(this).is(":checked")){
						var sz={"qgid":'',"qgmxid":''};
						sz.qgid = qgid;
						sz.qgmxid = this.dataset.qgmxid;
						qgmxJson.push(sz);
					}
				})
				$("#chooseQgxxForm #qgmx_json").val(JSON.stringify(qgmxJson));
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
		//添加日期控件
    	laydate.render({
    	   elem: '#chooseQgxxForm #sqrqstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#chooseQgxxForm #sqrqend'
    	  ,theme: '#2381E9'
    	});
    	//初始化已选单据号
    	initTagsinput();
    	
		var btn_query=$("#chooseQgxxForm #btn_query");
		/*--------------------------------模糊查询---------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchPurchaseResult(true); 
    		});
		}
		/*-----------------------显示隐藏------------------------------------*/
    	$("#chooseQgxxForm #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(Purchase_turnOff){
    			$("#chooseQgxxForm #searchMore").slideDown("low");
    			Purchase_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#chooseQgxxForm #searchMore").slideUp("low");
    			Purchase_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    	});
	}
	return oInit;
}

/**
 * 初始化已选订单号
 * @returns
 */
function initTagsinput(){
	$("#chooseQgxxForm  #yxdjh").tagsinput({
		itemValue: "value",
		itemText: "text",
	})
	// 初始化已选明细json
	$("#chooseQgxxForm #djhs").val($("#editContractForm #htmx_json").val());
}

/**
 * 监听标签点击事件
 */
var tagClick = $("#chooseQgxxForm").on('click','.label-info',function(e){
	event.stopPropagation();
	let lllb =$("#chooseQgxxForm #lllb").val();
	var url = $("#chooseQgxxForm input[name='urlPrefix']").val() + "/purchase/purchase/pagedataChooseListPurchaseInfo?access_token=" + $("#ac_tk").val() + "&qgid=" + e.currentTarget.dataset.logo+"&lllb="+lllb;
	$.showDialog(url, '选择请购明细', chooseQgmxConfig);
});
/**
 * 监听标签移除事件
 */
var tagsRemoved = $('#chooseQgxxForm #yxdjh').on('beforeItemRemove', function(event) {
	var qgmxJson = [];
	if($("#chooseQgxxForm #qgmx_json").val()){
		qgmxJson = JSON.parse($("#chooseQgxxForm #qgmx_json").val());
	}
	var qgid = event.item.value;
	if(qgmxJson.length > 0){
		for (var i = qgmxJson.length-1; i >= 0; i--) {
			if(qgmxJson[i].qgid == qgid){
				qgmxJson.splice(i,1);
			}
		}
	}
	$("#chooseQgxxForm #qgmx_json").val(JSON.stringify(qgmxJson));
});

var choosePurchase_PageInit = function(){
	var oInit = new Object();
	var postdata = {};

	oInit.Init = function () {
		var qglb=$("#chooseQgxxForm a[id^='qglb_id_']")

		$.each(qglb, function(i, n){
			var id = $(this).attr("id");
			var code = id.substring(id.lastIndexOf('_')+1,id.length);
			var qglbdm=$("#chooseQgxxForm #"+id).attr("csdm");
			if(qglbdm == 'DEVICE' || qglbdm == 'MATERIAL' || qglbdm == 'SERVICE' || qglbdm == 'OUTSOURCE'){
				addTj('qglb',code,'chooseQgxxForm');
			}
		});
	}
	return oInit;
}

$(function(){
	var oInit = new choosePurchase_PageInit();
	oInit.Init();
	// 1.初始化Table
	var oTable = new ChoosePurchase_TableInit();
	oTable.Init();
	//2.初始化Button的点击事件
	var oButtonInit = new ChoosePurchase_ButtonInit();
	oButtonInit.Init();
	jQuery('#chooseQgxxForm .chosen-select').chosen({width: '100%'});
})