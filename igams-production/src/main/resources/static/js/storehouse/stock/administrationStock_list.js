var Stock_turnOff=true;

var stock_TableInit = function () { 
	var oTableInit = new Object();
	// 初始化Table
	oTableInit.Init = function () {
		$('#administrationStock_formSearch #stock_list').bootstrapTable({
			url: $("#administrationStock_formSearch #urlPrefix").val()+'/storehouse/stock/pageGetListAdministrationStockt',         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#administrationStock_formSearch #toolbar', // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     // 是否启用排序
			sortName:"xzkcxx.hwmc",					// 排序字段
			sortOrder: "asc",                   // 排序方式
			queryParams: oTableInit.queryParams,// 传递参数（*）
			sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
			pageNumber:1,                       // 初始化加载第一页，默认第一页
			pageSize: 15,                       // 每页的记录行数（*）
			pageList: [15, 30, 50, 100],        // 可供选择的每页的行数（*）
			paginationPreText: '‹',				// 指定分页条中上一页按钮的图标或文字,这里是<
			paginationNextText: '›',			// 指定分页条中下一页按钮的图标或文字,这里是>
			search: false,                      // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
			strictSearch: true,
			showColumns: true,                  // 是否显示所有的列
			showRefresh: true,                  // 是否显示刷新按钮
			minimumCountColumns: 2,             // 最少允许的列数
			clickToSelect: true,                // 是否启用点击选中行
			// height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId: "xzkcid",                     // 每一行的唯一标识，一般为主键列
			showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮                 								
			cardView: false,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			columns: [{
				checkbox: true,
				width: '1%'
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
				field: 'hwmc',
				title: '货物名称',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'hwbz',
				title: '货物标准',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
				field: 'kcl',
				title: '库存量',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
				field: 'yds',
				title: '预定数',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'kw',
				title: '库位',
				width: '4%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'aqkc',
				title: '安全库存',
				width: '4%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'jjts',
				title: '警戒提示',
				width: '5%',
				align: 'left',
				formatter:administrationStock_formSearch_jjtsFormat,
				visible: true
			},{
				field: 'kcsfcz',
				title: '库存是否充足',
				width: '5%',
				align: 'left',
				formatter:administrationStock_formSearch_kcsfczFormat,
				visible: true
			},{
				field: 'll',
				title: '领料',
				width: '3%',
				align: 'left',
				formatter:administrationStock_formSearch_llformat,
				visible: true
			},{
				field: 'cz',
				title: '操作',
				width: '5%',
				align: 'left',
				formatter:administrationStock_formSearch_czformat,
				visible: true
			}],
			onLoadSuccess: function () {
			},
			onLoadError: function () {
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
				stockById(row.xzkcid,'view',$("#administrationStock_formSearch #btn_view").attr("tourl"));
			},
		});
		$("#administrationStock_formSearch #stock_list").colResizable({
			liveDrag:true, 
			gripInnerHtml:"<div class='grip'></div>", 
			draggingClass:"dragging", 
			resizeMode:'fit', 
			postbackSafe:true,
			partialRefresh:true
		})
	}
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   // 页面大小
        	pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "kcl", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		return stockSearchData(map);
	}
	return oTableInit
}

/**
 * 警戒提示
 */
//警戒提示
function administrationStock_formSearch_jjtsFormat(value,row,index){
	if(row.aqkc == null){
		row.aqkc = 0;
	}
	if(parseFloat(row.kcl) >=parseFloat(row.aqkc)){
		var jjts = "1";
		var html="<span style='color:green;'>"+"库存充足"+"</span>";
	}else{
		var jjts = "0";
		var html="<span style='color:red;'>"+"库存不足"+"</span>";
	}

	return html;
}
//库存是否充足
function administrationStock_formSearch_kcsfczFormat(value,row,index) {
	var html="";
	if (row.kcl>0){
		 html="<span style='color:green;'>"+"库存充足"+"</span>";
	}else if (row.kcl<=0){
		html="<span style='color:red;'>"+"库存不足"+"</span>";
	}
	return html;

}
/**
 * 领料车操作格式化
 * @returns
 */
function administrationStock_formSearch_llformat(value,row,index) {
	var ids = $("#administrationStock_formSearch #ids").val();
	if(ids!=null&&ids!=''){
		var split = ids.split(",");
		if($("#administrationStock_formSearch #btn_pickingcar").length>0){
			for(var i=0;i<split.length;i++){
				if(row.xzkcid==split[i]){
					return "<div id='wl"+row.xzkcid+"'><span id='t"+row.xzkcid+"' class='btn btn-danger' title='移出领料车' onclick=\"administrationStock_formSearch_delPickingCar('" + row.xzkcid + "',event)\" >取消</span></div>";
				}
			}
			return "<div id='wl"+row.xzkcid+"'><span id='t"+row.xzkcid+"' class='btn btn-info' title='加入领料车' onclick=\"administrationStock_formSearch_addPickingCar('" + row.xzkcid+ "','" + row.klsl + "',event)\" >领料</span></div>";
		}
	}else{
		return "<div id='wl"+row.xzkcid+"'><span id='t"+row.xzkcid+"' class='btn btn-info' title='加入领料车' onclick=\"administrationStock_formSearch_addPickingCar('" + row.xzkcid+ "','" + row.klsl + "',event)\" >领料</span></div>";
	}
}
function administrationStock_formSearch_czformat(value,row,index) {
	var idss = $("#administrationStock_formSearch #idss").val();
	if (idss!=null&&idss!='')
	{			var split = idss.split(",");
		if($("#administrationStock_formSearch #btn_allocationCar").length>0){
			for(var i=0;i<split.length;i++){
				if(row.xzkcid==split[i]){
					return "<div id='wll"+row.xzkcid+"'><span id='t1"+row.xzkcid+"' class='btn btn-info' title='加入调拨车' onclick=\"administrationStock_formSearch_allocation'" + row.xzkcid+ "','" + row.kcl+ "','" + row.klsl+ "','" + row.yds + "',event)\" >调拨</span> <span id='t2"+row.xzkcid+"' class='btn btn-danger' title='取消调拨' onclick=\"administrationStock_formSearch_delAllocationCar('" + row.xzkcid + "',event)\" >取消</span></div>";
				}
			}
			return "<div id='wll"+row.xzkcid+"'><span id='t1"+row.xzkcid+"' class='btn btn-info' title='加入调拨车' onclick=\"administrationStock_formSearch_allocation('" + row.xzkcid+ "','" + row.kcl+ "','" + row.klsl+ "','" + row.yds + "',event)\" >调拨</span> <span id='t2"+row.xzkcid+"' class='btn btn-info' title='更新调拨车' onclick=\"administrationStock_formSearch_allocationCar('" + row.xzkcid+ "','" + row.kcl+ "','" + row.klsl+ "','" + row.yds + "',event)\" >调拨车</span></div>";
		}
	}
	else {
		return "<div id='wll"+row.xzkcid+"'><span id='t1"+row.xzkcid+"' class='btn btn-info' title='加入调拨车' onclick=\"administrationStock_formSearch_allocation('" + row.xzkcid+ "','" + row.kcl+ "','" + row.klsl+ "','" + row.yds + "',event)\" )\" >调拨</span> <span id='t2"+row.xzkcid+"' class='btn btn-info' title='更新调拨车' onclick=\"administrationStock_formSearch_allocationCar('" + row.xzkcid+ "','" + row.kcl + "','" + row.klsl+ "','" + row.yds + "',event)\" >调拨车</span></div>";
	}

}
//行政库存调拨
function administrationStock_formSearch_allocation(xzkcid,kcl,klsl,yds,event) {
	var sfyhcgqx=$("#administrationStock_formSearch #btn_allocationCar");
	if(sfyhcgqx.length==0){
		$.confirm("很抱歉,您没有此权限!");
	}
	else if(kcl*1 <= 0){
		$.confirm("该货物库存不足！");
	}else if(klsl<=0) {
		$.confirm("该货物库存不足已有" + yds + "个库存被预定");
	}
	else
		$.showDialog($("#administrationStock_formSearch #urlPrefix").val()+"/storehouse/stock/pagedataAdministrationStockAllocation?xzkcid="+xzkcid+"&access_token="+$("#ac_tk").val(),'调拨信息',administrationConfig);

}
//加入调拨车
function administrationStock_formSearch_allocationCar(xzkcid,kcl,klsl,yds,event) {
	var sfyhcgqx=$("#administrationStock_formSearch #btn_allocationCar");
	if(sfyhcgqx.length==0){
		$.confirm("很抱歉,您没有此权限!");
	}else{
		//kcbj=1 有库存，kcbj=0 库存未0
		if(kcl*1 <= 0){
			$.confirm("该货物库存不足！");
		}else if(klsl<=0){
			$.confirm("该货物库存不足已有"+yds+"个库存被预定");
		}else{
			$.ajax({
				type:'post',
				url:$('#administrationStock_formSearch #urlPrefix').val()+"/storehouse/stock/pagedataAddToXzAllocationCar",
				cache: false,
				data: {"xzkcid":xzkcid,"access_token":$("#ac_tk").val()},
				dataType:'json',
				success:function(data){
					//返回值
					if(data.status=='success'){
						$("#idss").val(data.idss.substring(1));
						allocationCar_addOrDelNum("add");
						$("#administrationStock_formSearch #t2"+xzkcid).remove();
						var html="<span id='t2"+xzkcid+"' class='btn btn-danger' title='移出调拨车' onclick=\"administrationStock_formSearch_delAllocationCar('" + xzkcid + "',event)\" >取消</span>";
						$("#administrationStock_formSearch #wll"+xzkcid).append(html);
					}
				}
			});
		}
	}

}
//从调拨车中取消
function administrationStock_formSearch_delAllocationCar(xzkcid,event) {
	var sfyhcgqx=$("#administrationStock_formSearch #btn_allocationCar");
	if(sfyhcgqx.length==0){
		$.confirm("很抱歉,您没有此权限!");
	}else{
		$.ajax({
			type:'post',
			url:$('#administrationStock_formSearch #urlPrefix').val()+"/storehouse/stock/pagedataDelFromXzAllocationCar",
			cache: false,
			data: {"xzkcid":xzkcid,"access_token":$("#ac_tk").val()},
			dataType:'json',
			success:function(data){
				//返回值
				if(data.status=='success'){
					$("#idss").val(data.idss.substring(1));
					allocationCar_addOrDelNum("del");
					$("#administrationStock_formSearch #t2"+xzkcid).remove();
					var html="<span id='t2"+xzkcid+"' class='btn btn-info' title='加入调拨车' onclick=\"administrationStock_formSearch_allocationCar('" + xzkcid + "','" + data.kcl + "','" + data.klsl + "','" + data.yds + "',event)\" >调拨车</span>";
					$("#administrationStock_formSearch #wll"+xzkcid).append(html);
				}
			}
		});
	}


}
//行政库存调拨
var administrationConfig = {
	width		: "800px",
	modalName	: "administrationStockAllocationModal",
	formName	: "administrationStockAllocationForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#administrationStockAllocationForm").valid()){
					$.alert("请填写完整信息");
					return false;
				}


				var $this = this;
				var opts = $this["options"]||{};

				$("#administrationStockAllocationForm input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"]||"administrationStockAllocationForm",function(responseText,statusText){

					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								stockResult ();
							}
						});
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						$.error(responseText["message"],function() {
						});
					} else{
						preventResubmitForm(".modal-footer > button", false);
						$.alert(responseText["message"],function() {
						});
					}
				},".modal-footer > button");
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

//从领料车中删除
function administrationStock_formSearch_delPickingCar(xzkcid,event){
	var sfyhcgqx=$("#administrationStock_formSearch #btn_pickingcar");
	if(sfyhcgqx.length==0){
		$.confirm("很抱歉,您没有此权限!");
	}else{
		$.ajax({
			type:'post',
			url:$('#administrationStock_formSearch #urlPrefix').val()+"/storehouse/stock/pagedataDelFromXzPickingCar",
			cache: false,
			data: {"xzkcid":xzkcid,"access_token":$("#ac_tk").val()},
			dataType:'json',
			success:function(data){
				//返回值
				if(data.status=='success'){
					$("#ids").val(data.ids);
					pickingCar_addOrDelNum("del");
					$("#administrationStock_formSearch #t"+xzkcid).remove();
					var html="<span id='t"+xzkcid+"' class='btn btn-info' title='加入领料车' onclick=\"administrationStock_formSearch_addPickingCar('" + xzkcid + "','" + data.klsl + "',event)\" >领料</span>";
					$("#administrationStock_formSearch #wl"+xzkcid).append(html);
				}
			}
		});
	}
}

//添加至领料车
function administrationStock_formSearch_addPickingCar(xzkcid,klsl,event){
	var sfyhcgqx=$("#administrationStock_formSearch #btn_pickingcar");
	if(sfyhcgqx.length==0){
		$.confirm("很抱歉,您没有此权限!");
	}else{
		//kcbj=1 有库存，kcbj=0 库存未0
		if(klsl*1 <= 0){
			$.confirm("该货物库存不足！");
		}else{
			$.ajax({
				type:'post',
				url:$('#administrationStock_formSearch #urlPrefix').val()+"/storehouse/stock/pagedataAddToXzPickingCar",
				cache: false,
				data: {"xzkcid":xzkcid,"access_token":$("#ac_tk").val()},
				dataType:'json',
				success:function(data){
					//返回值
					if(data.status=='success'){
						$("#ids").val(data.ids);
						pickingCar_addOrDelNum("add");
						$("#administrationStock_formSearch #t"+xzkcid).remove();
						var html="<span id='t"+xzkcid+"' class='btn btn-danger' title='移出领料车' onclick=\"administrationStock_formSearch_delPickingCar('" + xzkcid + "',event)\" >取消</span>";
						$("#administrationStock_formSearch #wl"+xzkcid).append(html);
					}
				}
			});
		}
	}
}

/**
 * 领料车数字加减
 * @param sfbj
 * @returns
 */
function pickingCar_addOrDelNum(sfbj){
	if(sfbj=='add'){
		ll_count=parseInt(ll_count)+1;
	}else{
		ll_count=parseInt(ll_count)-1;
	}
	if((ll_count==1 && sfbj=='add') || (ll_count==0 && sfbj=='del')){
		ll_btnOinit();
	}
	$("#ll_num").text(ll_count);
}

var ll_count=0;
function ll_btnOinit(){
	if(ll_count>0){
		var strnum=ll_count;
		if(ll_count>99){
			strnum='99+';
		}
		var html="";
		html+="<span id='ll_num' style='position:absolute;background-color:red;color:white;border-radius: 15px;border:0px;width:20px;height:20px;top:-10px;font-size:10px;line-height:150%;'>"+strnum+"</span>";
		$("#administrationStock_formSearch #btn_pickingcar").append(html);
	}else{
		$("#administrationStock_formSearch #ll_num").remove();
	}
}
/**
 * 调拨车数字加减
 * @param sfbj
 * @returns
 */
function allocationCar_addOrDelNum(sfbj){
	if(sfbj=='add'){
		db_count=parseInt(db_count)+1;
	}else{
		db_count=parseInt(db_count)-1;
	}
	if((db_count==1 && sfbj=='add') || (db_count==0 && sfbj=='del')){
		db_btnOinit();
	}
	$("#db_num").text(db_count);
}

var db_count=0;
function db_btnOinit(){
	if(db_count>0){
		var strnum=db_count;
		if(db_count>99){
			strnum='99+';
		}
		var html="";
		html+="<span id='db_num' style='position:absolute;background-color:red;color:white;border-radius: 15px;border:0px;width:20px;height:20px;top:-10px;font-size:10px;line-height:150%;'>"+strnum+"</span>";
		$("#administrationStock_formSearch #btn_allocationCar").append(html);
	}else{
		$("#administrationStock_formSearch #db_num").remove();
	}
}

/**
 * 警戒提示
 * @returns
 */
function stock_jjtsFormat(value,row,index){
	if(row.kcl == null){
		row.kcl = 0;
	}
	if(row.kcl > 0){
		var jjts = "1";
		var html="<span style='color:green;'>"+"库存充足"+"</span>";
	}else{
		var jjts = "0";
		var html="<span style='color:red;'>"+"库存不足"+"</span>";
	}
	
	return html;
}
/**
 * 审核列表的状态格式化函数
 * @returns
 */
function ztFormat(value,row,index) {
	if (row.zt == '00') {
		return '未提交';
	}else if (row.zt == '80') {
		return "<span style='color:green;'>审核通过</span>";
	}else if (row.zt == '15') {
		return "<span style='color:red;'>审核未通过</span>";
	}else if(row.zt == '10'){
		return "审核中";
	}
	else{
		return '未提交';
	}
}

// 根据查询条件查询
function stockSearchData(map){
	var cxtj=$("#administrationStock_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#administrationStock_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["entire"]=cxnr;
	}else if(cxtj=="1"){
		map["hwmc"]=cxnr;
	}else if(cxtj=="2"){
		map["hwbz"]=cxnr;
	}
	// 高级筛选
	// 库位
	var kws = jQuery('#administrationStock_formSearch #kw_id_tj').val();
	map["kws"] = kws.replace(/'/g, "");
	// 警戒提示
	var jjtss = jQuery('#administrationStock_formSearch #jjts_id_tj').val();
	map["jjtss"] = jjtss.replace(/'/g, "");
	// 库存是否充足
	var jjtss = jQuery('#administrationStock_formSearch #jjts_id_tj').val();
	map["jjtss"] = jjtss.replace(/'/g, "");
	var kcsfczs = jQuery('#administrationStock_formSearch #kcsfcz_id_tj').val();
	map["kcsfczs"] = kcsfczs.replace(/'/g, "");
	return map;
}

/**
 * 设置高级筛选默认选中
 * @returns {Object}
 */
// var stock_PageInit = function(){
// 	var oInit = new Object();
//
// 	oInit.Init = function () {
// 		var jjts = $("#administrationStock_formSearch a[id^='jjts_id_']");
// 		$.each(jjts, function(i, n){
// 			var id = $(this).attr("id");
// 			var code = id.substring(id.lastIndexOf('_')+1,id.length);
// 			if(code === '1'){
// 				addTj('jjts',code,'administrationStock_formSearch');
// 			}
// 		});
// 		var zt = $("#administrationStock_formSearch a[id^='zt_id_']");
// 		$.each(zt, function(i, n){
// 			var id = $(this).attr("id");
// 			var code = id.substring(id.lastIndexOf('_')+1,id.length);
// 			if(code === '80'){
// 				addTj('zt',code,'administrationStock_formSearch');
// 			}
// 		});
// 	}
// 	return oInit;
// }

var stock_ButtonInit = function(){
    var oInit = new Object();

    oInit.Init = function () {
		var btn_view = $("#administrationStock_formSearch #btn_view");//查看
		var btn_query = $("#administrationStock_formSearch #btn_query");//模糊查询
		var btn_mod = $("#administrationStock_formSearch #btn_mod");//修改
		var btn_del = $("#administrationStock_formSearch #btn_del");//删除
		var btn_pickingcar= $("#administrationStock_formSearch #btn_pickingcar");
		var btn_allocationCar=$("#administrationStock_formSearch #btn_allocationCar");//调拨车
		var btn_stockuphold=$("#administrationStock_formSearch #btn_stockuphold");//库存维护
		var btn_searchexport = $("#administrationStock_formSearch #btn_searchexport");
		var btn_selectexport = $("#administrationStock_formSearch #btn_selectexport");
		/*--------------------------------模糊查询---------------------------*/
		if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			stockResult(true);
    		});
    	}
  
       /* ---------------------------查看行政库存信息-----------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#administrationStock_formSearch #stock_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			stockById(sel_row[0].xzkcid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
		/*-----------------------领料车--------------------------------------*/
		btn_pickingcar.unbind("click").click(function(){
			stockById(null,"pickingcar",btn_pickingcar.attr("tourl"));
		});
		/*-----------------------调拨车--------------------------------------*/
		btn_allocationCar.unbind("click").click(function(){
			stockById(null,"allocationCar",btn_allocationCar.attr("tourl"));
		});
		btn_stockuphold.unbind("click").click(function(){
			var sel_row = $('#administrationStock_formSearch #stock_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length == 1){
				stockById(sel_row[0].xzkcid, "stockuphold", btn_stockuphold.attr("tourl"));
			}else{
				$.error("请选中一行");
			}		});
		/*--------------------------------修改行政库存信息---------------------------*/
		btn_mod.unbind("click").click(function(){
			var sel_row = $('#administrationStock_formSearch #stock_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length == 1){
				stockById(sel_row[0].xzrkmxid, "mod", btn_mod.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});

		// 	  ---------------------------导出-----------------------------------
		btn_selectexport.unbind("click").click(function(){
			var sel_row = $('#administrationStock_formSearch #stock_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length>=1){
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids = ids + ","+ sel_row[i].xzkcid;
				}
				ids = ids.substr(1);
				$.showDialog($('#administrationStock_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=XZKCGL_SELECT&expType=select&ids="+ids
					,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
			}else{
				$.error("请选择数据");
			}
		});

		btn_searchexport.unbind("click").click(function(){
			$.showDialog($('#administrationStock_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=XZKCGL_SEARCH&expType=search&callbackJs=administrationStockSearchData"
				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
		});
		/* ------------------------------删除版本信息-----------------------------*/
		btn_del.unbind("click").click(function(){
			var sel_row = $('#administrationStock_formSearch #stock_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==0){
				$.error("请至少选中一行");
				return;
			}else {
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids= ids + ","+ sel_row[i].xzrkmxid;
				}
				ids=ids.substr(1);
				$.confirm('您确定要删除所选择的信息吗？',function(result){
					if(result){
						jQuery.ajaxSetup({async:false});
						var url= $("#administrationStock_formSearch #urlPrefix").val()+btn_del.attr("tourl");
						jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
							setTimeout(function(){
								if(responseText["status"] == 'success'){
									$.success(responseText["message"],function() {
										stockResult(true);
									});
								}else if(responseText["status"] == "fail"){
									$.error(responseText["message"],function() {
									});
								} else{
									$.alert(responseText["message"],function() {
									});
								}
							},1);

						},'json');
						jQuery.ajaxSetup({async:true});
					}
				});
			}
		});
		/*-----------------------显示隐藏------------------------------------*/
    	$("#administrationStock_formSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(Stock_turnOff){
    			$("#administrationStock_formSearch #searchMore").slideDown("low");
    			Stock_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#administrationStock_formSearch #searchMore").slideUp("low");
    			Stock_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    	});
    };
    	return oInit;
};
//提供给导出用的回调函数
function administrationStockSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="xzkcxx.hwmc";
	map["sortLastOrder"]="desc";
	map["sortName"]="xzkcxx.hwbz";
	map["sortOrder"]="desc";
	return stockSearchData(map);
}

//根据仓库货物ID查询仓库信息
function stockById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl = $("#administrationStock_formSearch #urlPrefix").val()+tourl;
	if(action =='view'){
		var url= tourl + "?xzkcid=" +id;
		$.showDialog(url,'行政库存信息',viewStockConfig);
	}else if(action =='mod'){
		var url= tourl + "?xzrkmxid=" +id;
		$.showDialog(url,'修改',editStockConfig);
	}else if(action=='pickingcar') {
		if (ll_count == '0') {
			return;
		}
		var url = tourl;
		$.showDialog(url, '领料车', pickingConfig);
	}
	else if (action=='allocationCar'){
		if (db_count=='0'){
			return;
		}
		var url = tourl;
		$.showDialog(url, '调拨车', allocationCarConfig);
	}
	else if (action='stockuphold'){
		var url= tourl + "?xzkcid=" +id;
		$.showDialog(url,'行政库存信息',upholdStockConfig);
	}

}
/**
 * 提交页面模态框
 */
var pickingConfig ={
	width		: "1600px",
	modalName	: "pickingConfigModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "提 交",
			className : "btn-primary",
			callback : function() {
				if(!$("#editXzPickingForm").valid()){
					$.alert("请填写完整信息");
					return false;
				}



				var $this = this;
				var opts = $this["options"]||{};
				var json = [];
				if(t_map.rows != null && t_map.rows.length > 0){
					for(var i=0;i<t_map.rows.length;i++){
						if(t_map.rows[i].qlsl==null || t_map.rows[i].qlsl==''){
							$.alert("领料数量不能为空！");
							return false;
						}
						if(parseFloat(t_map.rows[i].qlsl)>parseFloat(t_map.rows[i].klsl)){
							$.alert("领用数量不能大于可领数量！");
							return false;
						}
						if(t_map.rows[i].qlsl==0){
							$.alert("请领数量不能为0！");
							return false;
						}
						var sz = {"xzkcid":'',"qlsl":'',"xh":'',"yds":''};
						sz.xzkcid = t_map.rows[i].xzkcid;
						sz.qlsl = t_map.rows[i].qlsl;
						sz.yds = t_map.rows[i].yds;
						sz.xh = i+1;
						json.push(sz);
					}
					$("#editXzPickingForm #llmx_json").val(JSON.stringify(json));
				}else{
					$.alert("领料信息不能为空！");
					return false;
				}

				$("#editXzPickingForm input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"]||"editXzPickingForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						var auditType = $("#editXzPickingForm #auditType").val();
						var picking_params=[];
						picking_params.prefix=$('#editXzPickingForm #urlPrefix').val();
						//提交审核
						showAuditFlowDialog(auditType,responseText["ywid"],function(){
							$("#administrationStock_formSearch #ids").val("");
							$("#administrationStock_formSearch #ll_num").remove();
							ll_count=0;
							$.closeModal(opts.modalName);
							stockResult();
						},null,picking_params);

					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						$.error(responseText["message"],function() {
						});
					} else{
						preventResubmitForm(".modal-footer > button", false);
						$.alert(responseText["message"],function() {
						});
					}
				},".modal-footer > button");
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
}


var allocationCarConfig ={
	width		: "800px",
	modalName	: "allocationCarConfigModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "提 交",
			className : "btn-primary",
			callback : function() {
				if(!$("#allocationCar_formSearch").valid()){
					$.alert("请填写完整信息");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				var json = [];
				var xzkcids = []
				if(t_map.rows != null && t_map.rows.length > 0){
					for(var i=0;i<t_map.rows.length;i++){
						if(t_map.rows[i].kwid==t_map.rows[i].drkw){
							$.alert("调入库位与调出库位一致");
							return false;
						}
						if(parseFloat(t_map.rows[i].dbsl)>parseFloat(t_map.rows[i].klsl)){
							$.alert("调拨数量不允许大于库存量");
							return false;
						}
						if(t_map.rows[i].drkw==''||t_map.rows[i].drkw==null){
							$.alert("调入库位与调出库位一致");
							return false;
						}
						var sz = {"xzkcid":'',"hwmc":'',"hwbz":'',"dckw":'',"drkw":'',"dbsl":'',"kcl":'',"hwbz":''};
						sz.xzkcid = t_map.rows[i].xzkcid;
						sz.hwmc = t_map.rows[i].hwmc;
						sz.hwbz = t_map.rows[i].hwbz;
						sz.dckw = t_map.rows[i].kwid;
						sz.drkw = t_map.rows[i].drkw;
						sz.dbsl = t_map.rows[i].dbsl;
						sz.kcl = t_map.rows[i].kcl;
						sz.hwbz = t_map.rows[i].hwbz;
						xzkcids.push(t_map.rows[i].xzkcid);
						json.push(sz);
					}
					$("#allocationCar_formSearch #dbmx_json").val(JSON.stringify(json));
				}
				$("#allocationCar_formSearch input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"]||"allocationCar_formSearch",function(responseText,statusText){

					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								for (let i = 0; i < xzkcids.length; i++) {
									allocationCar_addOrDelNum("del");
									let idss = $("#administrationStock_formSearch #idss").val()
									let split = idss.split(",");
									let str ="";
									for(let j=0;j<split.length;j++){
										if (split[j] != xzkcids[i]){
											str += ","+split[j];
										}
									}
									$("#administrationStock_formSearch #idss").val(str.substring(1));
								}
								$.closeModal(opts.modalName);
								stockResult ();
							}
						});
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						$.error(responseText["message"],function() {
						});
					} else{
						preventResubmitForm(".modal-footer > button", false);
						$.alert(responseText["message"],function() {
						});
					}
				},".modal-footer > button");
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
}
/**
 * 查看行政库存信息
 */
var viewStockConfig = {
	width		: "1000px",
	modalName	: "viewstockModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
/**
 * 修改行政库存信息
 */
var editStockConfig = {
	width		: "800px",
	modalName	: "editStockModal",
	formName	: "modAdministrationStockForm",
	offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
//					$("#addVersionInfoForm #bbid").val($("#addVersionInfoForm #bbid").val());
				if(!$("#modAdministrationStockForm").valid()){
					$.alert("请填写完整信息");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};

				$("#modAdministrationStockForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"modAdministrationStockForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							stockResult(true);
						});
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						$.error(responseText["message"],function() {
						});
					} else{
						$.alert(responseText["message"],function() {
						});
					}
				},".modal-footer > button");
				return false;
			}
		},

		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var upholdStockConfig = {
	width		: "800px",
	modalName	: "upholdStockModal",
	formName	: "stockUpholdForm",
	offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#stockUpholdForm").valid()){
					$.alert("请填写完整信息");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};

				$("#stockUpholdForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"stockUpholdForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							stockResult(true);
						});
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						$.error(responseText["message"],function() {
						});
					} else{
						$.alert(responseText["message"],function() {
						});
					}
				},".modal-footer > button");
				return false;
			}
		},

		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
function stockResult(isTurnBack){
	//关闭高级搜索条件
	$("#administrationStock_formSearch #searchMore").slideUp("low");
	Stock_turnOff=true;
	$("#administrationStock_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#administrationStock_formSearch #stock_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#administrationStock_formSearch #stock_list').bootstrapTable('refresh');
	}
}

var administrationStock_PageInit = function(){
	var oInit = new Object();
	var postdata = {};
	oInit.Init = function () {
		var kcsfcz = $("#administrationStock_formSearch a[id^='kcsfcz_id_']");
		$.each(kcsfcz, function(i, n){
			var id = $(this).attr("id");
			var code = id.substring(id.lastIndexOf('_')+1,id.length);
			if(code === '1'){
				addTj('kcsfcz',code,'administrationStock_formSearch');
			}
		});
	}
	return oInit;
}
$(function(){

	//1.初始化Page
	// var oInit = new stock_PageInit();
	// oInit.Init();
	var oInit = new administrationStock_PageInit();
	oInit.Init();
	// 2.初始化Table
	var oTable = new stock_TableInit();
	oTable.Init();
    //3.初始化Button的点击事件
    var oButtonInit = new stock_ButtonInit();
    oButtonInit.Init();

	if($("#llcsl").val()==null || $("#llcsl").val()==''){
		ll_count=0;
	}else{
		ll_count=$("#llcsl").val();
	}
	ll_btnOinit();
	//调拨车初始数量
	if($("#dbcsl").val()==null || $("#dbcsl").val()==''){
		db_count=0;
	}else{
		db_count=$("#dbcsl").val();
	}
	db_btnOinit();
    
	// 所有下拉框添加choose样式
	jQuery('#administrationStock_formSearch .chosen-select').chosen({width: '100%'});

	$("#administrationStock_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
	
});