var Stock_turnOff=true;
var xmdllist = '';
var xmbmlist = '';
var stock_TableInit = function () { 
var oTableInit = new Object();	    
	// 初始化Table
	oTableInit.Init = function () {
		$('#stock_formSearch #stock_list').bootstrapTable({
			url: $("#stock_formSearch #urlPrefix").val()+'/storehouse/stock/pageGetListSecureStock',         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#stock_formSearch #toolbar', // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     // 是否启用排序
			sortName:"wlbm",					// 排序字段
			sortOrder: "desc",                   // 排序方式
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
			uniqueId: "wlid",                     // 每一行的唯一标识，一般为主键列
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
				field: 'wlid',
				title: '物料ID',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: false
			},{				
				field: 'wlbm',
				title: '物料编码',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'wlmc',
				title: '物料名称',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
				field: 'ckqxmc',
				title: '仓库分类',
				width: '6%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
				field: 'ychh',
				title: '货号',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			},{				
				field: 'scs',
				title: '生产商',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: true
			},{				
				field: 'gg',
				title: '规格',
				width: '4%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'jldw',
				title: '单位',
				width: '5%',
				align: 'left',
				sortable: true,
				visible: true
			}, {				
				field: 'kcl',
				title: '库存量',
				width: '5%',
				align: 'left',
				sortable: true,
				visible: true
			},{				
				field: 'yds',
				title: '预定数',
				width: '5%',
				align: 'left',
				sortable: true,
				visible: false
			},{				
				field: 'aqkc',
				title: '安全库存',
				width: '5%',
				align: 'left',
				sortable: true,
				visible: false
			},{				
				field: 'wllb',
				title: '物料类别',
				width: '5%',
				align: 'left',
				sortable: true,
				visible: false
			},{				
				field: 'wlzlb',
				title: '物料子类别',
				width: '5%',
				align: 'left',
				sortable: true,
				visible: false
			},{
				field: 'kczt',
				title: '库存',
				width: '5%',
				align: 'left',
				sortable: true,
				formatter:kcztFormat,
				visible: true
			},{
				field: 'jjts',
				title: '警戒提示',
				width: '5%',
				align: 'left',
				formatter:stock_jjtsFormat,
				visible: true
			}],
			onLoadSuccess: function () {
			},
			onLoadError: function () {
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
				stockById(row.wlid,'view',$("#stock_formSearch #btn_view").attr("tourl"));
			},
		});
		$("#stock_formSearch #stock_list").colResizable({
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
            sortLastName: "ckhwxx.wlid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		return stockSearchData(map);
	}
	return oTableInit
}

//警戒提示
function stock_jjtsFormat(value,row,index){
	if(row.aqkc == null||row.aqkc == ''){
		row.aqkc = 0;
	}
	if(row.kcl - row.aqkc>=0){
		var jjts = "1";
		var html="<span style='color:green;'>"+"库存充足"+"</span>";
	}else{
		var jjts = "0";
		var html="<span style='color:red;'>"+"库存不足"+"</span>";
	}
	
	return html;
}
function kcztFormat(value,row,index){
	if(row.kcl == null||row.kcl == ''){
		row.kcl = 0;
	}
	if(row.kcl>0){
		var html="<span style='color:green;'>"+"库存充足"+"</span>";
	}else{
		var html="<span style='color:red;'>"+"库存不足"+"</span>";
	}
	return html;
}

/**
 * 领料车操作格式化
 * @returns
 */
function stock_formSearch_llformat(value,row,index) {
	var idswl = $("#stock_formSearch #idswl").val().split(",");
	if($("#stock_formSearch #btn_pickingcar").length>0){
		for(var i=0;i<idswl.length;i++){
			if(row.ckhwid==idswl[i]){
				return "<div id='wl"+row.ckhwid+"'><span id='t"+row.ckhwid+"' class='btn btn-danger' title='移出领料车' onclick=\"stock_formSearch_delPickingCar('" + row.ckhwid + "',event)\" >取消</span></div>";
			}
		}
		return "<div id='wl"+row.ckhwid+"'><span id='t"+row.ckhwid+"' class='btn btn-info' title='加入领料车' onclick=\"addPickingCar('" + row.ckhwid + "',event)\" >领料</span></div>";
	}
	return "";
}

/**
 * 领料车操作格式化
 * @returns
 */
function stock_formSearch_jyformat(value,row,index) {
	var idsJywl = $("#stock_formSearch #idsJywl").val().split(",");
	if($("#stock_formSearch #btn_borrow").length>0){
		for(var i=0;i<idsJywl.length;i++){
			if(row.ckhwid==idsJywl[i]){
				return "<div id='jywl"+row.ckhwid+"'><span id='b"+row.ckhwid+"' class='btn btn-danger' title='移出借用车' onclick=\"stock_formSearch_delBorrowingCar('" + row.ckhwid + "',event)\" >取消</span></div>";
			}
		}
		return  "<div id='jywl"+row.ckhwid+"'><span id='b"+row.ckhwid+"' class='btn btn-info' title='加入借用车' onclick=\"addBorrowCar('" + row.ckhwid + "',event)\" >借用</span></div>";
	}
	return "";
}

//从领料车中删除
function stock_formSearch_delPickingCar(ckhwid,event){
	var sfyhcgqx=$("#stock_formSearch #btn_pickingcar");
	if(sfyhcgqx.length==0){
		$.confirm("很抱歉,您没有此权限!");
	}else{
		$.ajax({
		    type:'post',
		    url:$('#stock_formSearch #urlPrefix').val()+"/storehouse/stock/pagedataDelFromPickingCar",
		    cache: false,
		    data: {"ckhwid":ckhwid,"access_token":$("#ac_tk").val()},
		    dataType:'json',
		    success:function(data){
		    	//返回值
		    	if(data.status=='success'){
		    		$("#idswl").val(data.idswl);
		    		pickingCar_addOrDelNum("del");
		    		$("#stock_formSearch #t"+ckhwid).remove();
		    		var html="<span id='t"+ckhwid+"' class='btn btn-info' title='加入领料车' onclick=\"addPickingCar('" + ckhwid + "',event)\" >领料</span>";
		    		$("#stock_formSearch #wl"+ckhwid).append(html);
		    	}
		    }
		});
	}
}
//从借用车中删除
function stock_formSearch_delBorrowingCar(ckhwid,event){
	var sfyhcgqx=$("#stock_formSearch #btn_borrow");
	if(sfyhcgqx.length==0){
		$.confirm("很抱歉,您没有此权限!");
	}else{
		$.ajax({
			type:'post',
			url:$('#stock_formSearch #urlPrefix').val()+"/borrowing/borrowing/pagedataDelFromBorrowingCar",
			cache: false,
			data: {"ckhwid":ckhwid,"access_token":$("#ac_tk").val()},
			dataType:'json',
			success:function(data){
				//返回值
				if(data.status=='success'){
					$("#idsJywl").val(data.idsJywl);
					borrowCar_addOrDelNum("del");
					$("#stock_formSearch #b"+ckhwid).remove();
					var html="<span id='b"+ckhwid+"' class='btn btn-info' title='加入借用车' onclick=\"addBorrowCar('" + ckhwid + "',event)\" >借用</span>";
					$("#stock_formSearch #jywl"+ckhwid).append(html);
				}
			}
		});
	}
}
//添加至入库车
function addBorrowCar(ckhwid,event){
	var sfyhcgqx=$("#stock_formSearch #btn_borrow");
	if(sfyhcgqx.length==0){
		$.confirm("很抱歉,您没有此权限!");
	}else{
		$.ajax({
			async:false,
			type:'post',
			url:$('#stock_formSearch #urlPrefix').val()+"/storehouse/requisition/pagedataQueryWlxxByWlid",
			cache: false,
			data: {"ckhwid":ckhwid,"access_token":$("#ac_tk").val()},
			dataType:'json',
			success:function(data){
				//返回值
				if(data.ckhwxxDto!=null && data.ckhwxxDto!=''){
					//kcbj=1 有库存，kcbj=0 库存未0
					if(data.ckhwxxDto.kcbj!='1'){
						$.confirm("该物料库存不足！");
					}else{
						$.ajax({
							type:'post',
							url:$('#stock_formSearch #urlPrefix').val()+"/borrowing/borrowing/pagedataAddBorrowingCar",
							cache: false,
							data: {"ckhwid":ckhwid,"access_token":$("#ac_tk").val()},
							dataType:'json',
							success:function(data){
								//返回值
								if(data.status=='success'){
									$("#idsJywl").val(data.idsJywl);
									borrowCar_addOrDelNum("add");
									$("#stock_formSearch #b"+ckhwid).remove();
									var html="<span id='b"+ckhwid+"' class='btn btn-danger' title='移出入库车' onclick=\"stock_formSearch_delBorrowingCar('" + ckhwid + "',event)\" >取消</span>";
									$("#stock_formSearch #jywl"+ckhwid).append(html);
								}
							}
						});
					}
				}else{
					$.confirm("该物料不存在!");
				}
			}
		});
	}
}

//添加至入库车
function addPickingCar(ckhwid,event){
	var sfyhcgqx=$("#stock_formSearch #btn_pickingcar");
	if(sfyhcgqx.length==0){
		$.confirm("很抱歉,您没有此权限!");
	}else{
		$.ajax({ 
			async:false,
		    type:'post',  
	    	url:$('#stock_formSearch #urlPrefix').val()+"/storehouse/requisition/pagedataQueryWlxxByWlid", 
		    cache: false,
		    data: {"ckhwid":ckhwid,"access_token":$("#ac_tk").val()},
		    dataType:'json', 
		    success:function(data){
		    	//返回值
		    	if(data.ckhwxxDto!=null && data.ckhwxxDto!=''){
		    		//kcbj=1 有库存，kcbj=0 库存未0
		    		if(data.ckhwxxDto.kcbj!='1'){
		    			$.confirm("该物料库存不足！");
		    		}else{
		    			$.ajax({ 
		    			    type:'post',  
		    			    url:$('#stock_formSearch #urlPrefix').val()+"/storehouse/stock/pagedataAddToPickingCar", 
		    			    cache: false,
		    			    data: {"ckhwid":ckhwid,"access_token":$("#ac_tk").val()},
		    			    dataType:'json', 
		    			    success:function(data){
		    			    	//返回值
		    			    	if(data.status=='success'){
		    			    		$("#idswl").val(data.idswl);
		    			    		pickingCar_addOrDelNum("add");
		    			    		$("#stock_formSearch #t"+ckhwid).remove();
		    			    		var html="<span id='t"+ckhwid+"' class='btn btn-danger' title='移出入库车' onclick=\"stock_formSearch_delPickingCar('" + ckhwid + "',event)\" >取消</span>";
		    			    		$("#stock_formSearch #wl"+ckhwid).append(html);
		    			    	}
		    			    }
		    			});
		    		}
		    	}else{
		    		$.confirm("该物料不存在!");
		    	}
		    }
		});
	}
}
/**
 * 借用车数字加减
 * @param sfbj
 * @returns
 */
function borrowCar_addOrDelNum(sfbj){
	if(sfbj=='add'){
		jy_count=parseInt(jy_count)+1;
	}else{
		jy_count=parseInt(jy_count)-1;
	}
	if((jy_count==1 && sfbj=='add') || (jy_count==0 && sfbj=='del')){
		jy_btnOinit();
	}
	$("#jy_num").text(jy_count);
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
// 根据查询条件查询
function stockSearchData(map){
	var cxtj=$("#stock_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#stock_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["entire"]=cxnr;
	}else if(cxtj=="1"){
		map["wlmc"]=cxnr;
	}else if(cxtj=="2"){
		map["wlbm"]=cxnr;
	}
	// 高级筛选
	// 分组
	var bms = jQuery('#stock_formSearch #bm_id_tj').val();
	map["bms"] = bms;// 分组
	var fzs = jQuery('#stock_formSearch #fz_id_tj').val();
	map["fzs"] = fzs;
	// 类别
	var lbs = jQuery('#stock_formSearch #lb_id_tj').val();
	map["lbs"] = lbs;
	// 警戒提示
	var jjtss = jQuery('#stock_formSearch #jjts_id_tj').val();
	map["jjtss"] = jjtss;
	//库存状态
	var kczt = jQuery('#stock_formSearch #kczt_id_tj').val();
	map["kczt"] = kczt;
	return map;
}


//提供给导出用的回调函数
function ArrGoodsSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="";
	map["sortLastOrder"]="";
	map["sortName"]="ckhwxx.wlid";
	map["sortOrder"]="desc";
	return stockSearchData(map);
}

//根据仓库货物ID查询仓库信息
function stockById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl = $("#stock_formSearch #urlPrefix").val()+tourl;
	if(action =='view'){
		var url= tourl + "?wlid=" +id;
		$.showDialog(url,'到货详细信息',viewstockConfig);
	}else if(action=='pickingcar'){
		if(ll_count=='0'){
			return;
		}
		var url=tourl;
		$.showDialog(url,'领料车',pickingConfig);
	}else if(action=='allocation'){
		var url=tourl;
		$.showDialog(url,'调拨',allocationConfig);
	}else if(action=='borrow'){
		var url=tourl;
		$.showDialog(url,'借用车',addBorrowConfig);
	}else if(action=="system"){
		if(ll_count=='0'){
			return;
		}
		var url=tourl+"?ckcsdm=OA";
		$.showDialog(url,'OA领料',pickingConfig);
	}
}
var addBorrowConfig = {
	width		: "1600px",
	modalName	:"addBorrowConfig",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#borrowForm").valid()){
					$.alert("请填写完整信息");
					return false;
				}
				let jgdm= $("#borrowForm #bmdm").val()
				if(!jgdm){
					$.alert("所选部门存在异常！");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				var json = [];
				if(t_map.rows != null && t_map.rows.length > 0){
					for(var i=0;i<t_map.rows.length;i++){
						if(t_map.rows[i].jcsl==null || t_map.rows[i].jcsl==''){
							$.alert("借用数量不能为0！");
							return false;
						}
						if(t_map.rows[i].jcsl==0){
							$.alert("借用数量不能为0！");
							return false;
						}
						var sz = {"ckhwid":'',"ckqxlx":'',"wlid":'',"jcsl":'',"bz":'',"yjghrq":''};
						sz.ckhwid = t_map.rows[i].ckhwid;
						sz.ckqxlx = t_map.rows[i].ckqxlx;
						sz.wlid = t_map.rows[i].wlid;
						sz.jcsl = t_map.rows[i].jcsl;
						sz.bz = t_map.rows[i].hwllxxbz;
						sz.yjghrq = t_map.rows[i].yjghrq;
						json.push(sz);
					}
					$("#borrowForm #jymx_json").val(JSON.stringify(json));
				}else{
					$.alert("借用信息不能为空！");
					return false;
				}
				$("#borrowForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"borrowForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						var auditType = $("#borrowForm #auditType").val();
						var receiveMateriel_params=[];
						receiveMateriel_params.prefix=$('#borrowForm #urlPrefix').val();
						if(opts.offAtOnce){
							$.closeModal(opts.modalName);
						}
						//提交审核
						showAuditFlowDialog(auditType, responseText["ywid"],function(){
							stockResult();
						},null,receiveMateriel_params);
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

var viewstockConfig = {
	width		: "1600px",
	modalName	: "viewstockModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	} 
};

var pickingConfig = {
		width		: "1500px",
		modalName	: "pickingModal",
		formName	: "editPickingForm",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "提 交",
				className : "btn-primary",
				callback : function() {
					if(!$("#editPickingForm").valid()){
						$.alert("请填写完整信息");
						return false;
					}
					let jgdm= $("#editPickingForm #jgdm").val()
					if(!jgdm){
						$.alert("所选部门存在异常！");
						return false;
					}

					var nowDate=new Date();//当前系统时间
					var $this = this;
					var opts = $this["options"]||{};
					var json = [];
					if(t_map.rows != null && t_map.rows.length > 0){
						let wlids = "";
						var queryWlids = "";
						for(var i=0;i<t_map.rows.length;i++){
							if(t_map.rows[i].qlsl==null || t_map.rows[i].qlsl==''){
								$.alert("领料数量不能为空！");
								return false;
							}
							if(parseFloat(t_map.rows[i].qlsl)>parseFloat(t_map.rows[i].kqls)){
								$.alert("请领数不能大于可请领数！");
								return false;
							}
							if(t_map.rows[i].xmdl==null || t_map.rows[i].xmdl==''){
								$.alert("项目大类不能为空！");
								return false;
							}
							if(t_map.rows[i].xmbm==null || t_map.rows[i].xmbm==''){
								$.alert("项目编码不能为空！");
								return false;
							}
							if(t_map.rows[i].qlsl==0){
								$.alert("请领数量不能为0！");
								return false;
							}
							var sz = {"hwllid":'',"ckhwid":'',"wlid":'',"qlsl":'',"bz":'',"xmdl":'',"xmbm":'',"qlyds":'',"hwllmx_json":''};
	    					sz.ckhwid = t_map.rows[i].ckhwid;
							sz.hwllid = t_map.rows[i].hwllid;
	    					sz.wlid = t_map.rows[i].wlid;
	    					sz.qlsl = t_map.rows[i].qlsl;
							sz.bz = t_map.rows[i].hwllxxbz;
	    					sz.xmdl = t_map.rows[i].xmdl;
	    					sz.xmbm = t_map.rows[i].xmbm;
	    					sz.qlyds = t_map.rows[i].qlyds;
	    					sz.hwllmx_json=t_map.rows[i].hwllmx_json;
	    					json.push(sz);
							wlids = wlids + "," + t_map.rows[i].wlid;
							queryWlids = queryWlids + "," + t_map.rows[i].wlid;
						}
						queryWlids = queryWlids.substr(1);
						wlids = wlids.substr(1);
						stockConfirmAuditType(wlids);
						$("#editPickingForm #llmx_json").val(JSON.stringify(json));
						$("#editPickingForm #queryWlids").val(queryWlids);
					}else{
						$.alert("领料信息不能为空！");
						return false;
					}
					xmdllist = $("#editPickingForm #xmdllist").val();
					xmbmlist = $("#editPickingForm #xmbmlist").val();
					$("#editPickingForm #xmdllist").val('')
					$("#editPickingForm #xmbmlist").val('')
					$("#editPickingForm input[name='access_token']").val($("#ac_tk").val());

					submitForm(opts["formName"]||"editPickingForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							var auditType = $("#editPickingForm #auditType").val();
							var picking_params=[];
							picking_params.prefix=$('#editPickingForm #urlPrefix').val();
							$.success(responseText["message"],function() {
								//提交审核
								showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){
									$("#stock_formSearch #idswl").val("1,2")
									$("#stock_formSearch #ll_num").remove();
									$("#ll_ids").val("");
									$.closeModal(opts.modalName);
									stockResult();
								},null,picking_params);
							});
						}else if(responseText["status"] == "fail"){
							$("#editPickingForm #xmdllist").val(xmdllist)
							$("#editPickingForm #xmbmlist").val(xmbmlist)
							preventResubmitForm(".modal-footer > button", false);
							$.error(responseText["message"],function() {
							});
						} else{
							$("#editPickingForm #xmdllist").val(xmdllist)
							$("#editPickingForm #xmbmlist").val(xmbmlist)
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

//列表刷新
function stockConfirmAuditType(wlids){
	$.ajax({
		type:'post',
		url: $('#stock_formSearch #urlPrefix').val()+'/storehouse/requisition/pagedataConfirmAuditType',
		cache: false,
		data: {"wlids":wlids,"access_token":$("#ac_tk").val()},
		dataType:'json',
		success:function(data){
			if (data.status == "success"){
				$("#editPickingForm #auditType").val(data.auditType)
			}
		}
	});
}
var allocationConfig = {
	width		: "1600px",
	modalName    : "allocationModal",
	formName    : "allocationForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		successtwo : {
			label : "保 存",
			className : "btn-success",
			callback : function() {
				if (!$("#allocationForm").valid()) {
					$.alert("请填写完整信息");
					return false;
				}
				var json = [];
				if(t_map.rows != null && t_map.rows.length > 0){
					for(var i=0;i<t_map.rows.length;i++){
    					var dbmx_json=JSON.parse(t_map.rows[i].dbmx_json);
    					if(dbmx_json!=null && dbmx_json!=""){
    						for(var j=0;j<dbmx_json.length;j++){
    							var sz = {"dchwid":'',"dchw":'',"drhw":'',"dbsl":''};
    	    					sz.dchwid = dbmx_json[j].hwid;
    	    					sz.dchw = dbmx_json[j].dckw;
    	    					sz.drhw = dbmx_json[j].drkw;
    	    					sz.dbsl = dbmx_json[j].dbsl;
								if (sz.dbsl != "0"){
									json.push(sz);
								}
        					}  	
    					}									
					}
					$("#allocationForm #dbmx_json").val(JSON.stringify(json));
				}else{
					$.alert("调拨明细不能为空！");
					return false;
				}
				
				var $this = this;
				var opts = $this["options"] || {};

				$("#allocationForm input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"] || "allocationForm", function (responseText, statusText) {
					if (responseText["status"] == 'success') {
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							stockResult();
						});
					} else if (responseText["status"] == "fail") {
						preventResubmitForm(".modal-footer > button", false);
						$.error(responseText["message"], function () {
						});
					} else {
						preventResubmitForm(".modal-footer > button", false);
						$.alert(responseText["message"], function () {
						});
					}
				}, ".modal-footer > button");

				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};



var stock_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
		var btn_view = $("#stock_formSearch #btn_view");
		var btn_query = $("#stock_formSearch #btn_query");
		var btn_searchexport = $("#stock_formSearch #btn_searchexport");
    	var btn_selectexport = $("#stock_formSearch #btn_selectexport");
    	var btn_pickingcar= $("#stock_formSearch #btn_pickingcar");
		var btn_borrow= $("#stock_formSearch #btn_borrow");
		var btn_allocation= $("#stock_formSearch #btn_allocation");
		var btn_system = $("#stock_formSearch #btn_system"); //OA领料
		
		/*--------------------------------模糊查询---------------------------*/    	
		if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			stockResult(true);
    		});
    	}
  
       /* ---------------------------查看仓库货物-----------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#stock_formSearch #stock_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			stockById(sel_row[0].wlid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	
    	//---------------------------导出--------------------------------------------------
    	//选择导出
    	btn_selectexport.unbind("click").click(function(){
    		var sel_row = $('#stock_formSearch #stock_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length>=1){
    			var ids="";
        		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids = ids + ","+ sel_row[i].wlid;
        		}
        		ids = ids.substr(1);
        		$.showDialog($('#stock_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=STOCK_SECURE_SELECT&expType=select&ids="+ids
        				,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    		}else{
    			$.error("请选择数据");
    		}
    	});
    	//搜索导出
    	btn_searchexport.unbind("click").click(function(){
    		$.showDialog($('#stock_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=STOCK_SECURE_SEARCH&expType=search&callbackJs=ArrGoodsSearchData"
    				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    	});	
 
    	/*-----------------------领料车--------------------------------------*/
    	btn_pickingcar.unbind("click").click(function(){
    		stockById(null,"pickingcar",btn_pickingcar.attr("tourl"));
    	});
    	/*-----------------------OA领料--------------------------------------*/
    	btn_system.unbind("click").click(function(){
    		stockById(null,"system",btn_system.attr("tourl"));
    	});
		/*-----------------------领料车--------------------------------------*/
		btn_borrow.unbind("click").click(function(){
			stockById(null,"borrow",btn_borrow.attr("tourl"));
		});
		/*-----------------------调拨--------------------------------------*/
		btn_allocation.unbind("click").click(function(){
			stockById(null,"allocation",btn_allocation.attr("tourl"));
		});
    	
    	
		/*-----------------------显示隐藏------------------------------------*/
    	$("#stock_formSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(Stock_turnOff){
    			$("#stock_formSearch #searchMore").slideDown("low");
    			Stock_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#stock_formSearch #searchMore").slideUp("low");
    			Stock_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    	});
    	
    };
    	return oInit;
};

    
function stockResult(isTurnBack){
	//关闭高级搜索条件
	$("#stock_formSearch #searchMore").slideUp("low");
	Stock_turnOff=true;
	$("#stock_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#stock_formSearch #stock_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#stock_formSearch #stock_list').bootstrapTable('refresh');
	}
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
		$("#stock_formSearch #btn_pickingcar").append(html);
	}else{
		$("#stock_formSearch #ll_num").remove();
	}
}

var jy_count=0;
function jy_btnOinit(){
	if(jy_count>0){
		var strnum=jy_count;
		if(jy_count>99){
			strnum='99+';
		}
		var html="";
		html+="<span id='jy_num' style='position:absolute;background-color:red;color:white;border-radius: 15px;border:0px;width:20px;height:20px;top:-10px;font-size:10px;line-height:150%;'>"+strnum+"</span>";
		$("#stock_formSearch #btn_borrow").append(html);
	}else{
		$("#stock_formSearch #jy_num").remove();
	}
}
var stock_PageInit = function(){
	var oInit = new Object();
	var postdata = {};
	oInit.Init = function () {
		var kczt = $("#stock_formSearch a[id^='kczt_id_']");
		$.each(kczt, function(i, n){
			var id = $(this).attr("id");
			var code = id.substring(id.lastIndexOf('_')+1,id.length);
			if(code === '1'){
				addTj('kczt',code,'stock_formSearch');
			}
		});
	}
	return oInit;
}
$(function(){
	if($("#llcsl").val()==null || $("#llcsl").val()==''){
		ll_count=0;
	}else{
		ll_count=$("#llcsl").val();
	}
	if($("#jccsl").val()==null || $("#jccsl").val()==''){
		jy_count=0;
	}else{
		jy_count=$("#jccsl").val();
	}
	var oInit = new stock_PageInit();
	oInit.Init();
	ll_btnOinit();
	jy_btnOinit();
	// 1.初始化Table
	var oTable = new stock_TableInit();
	oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new stock_ButtonInit();
    oButtonInit.Init();
    
	// 所有下拉框添加choose样式
	jQuery('#stock_formSearch .chosen-select').chosen({width: '100%'});
	
	$("#stock_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
	
});