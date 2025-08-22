var StockPending_turnOff=true;
var StockPending_TableInit=function(){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#stockPending_formSearch #stockPending_list").bootstrapTable({
			url: $("#stockPending_formSearch #urlPrefix").val()+'/warehouse/stockPending/pageGetListStockPending',
            method: 'get',                      // 请求方式（*）
            toolbar: '#stockPending_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "dhxx.dhdh",				// 排序字段
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
            uniqueId: "hwid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '4%'
            },{
            	title: '序',
        		formatter: function (value, row, index) {
        			return index+1;
        		},
        		titleTooltip:'序',
        		width: '3%',
                align: 'left',
                visible:true
            },{
                field: 'wlid',
                title: '物料id',
                width: '10%',
                align: 'left',
                visible:false
            },{
                field: 'wlbm',
                title: '物料编码',
                width: '8%',
                align: 'left',
                formatter:wlbmformat,
                visible:true
            },{
                field: 'wlmc',
                title: '物料名称',
                width: '10%',
                align: 'left',
                visible: true
            },{
				field: 'dhlxmc',
				title: '到货类型',
				width: '6%',
				align: 'left',
				visible: true
			},{
				field: 'rklbmc',
				title: '入库类别',
				width: '6%',
				align: 'left',
				visible: true
			},{
				field: 'zldh',
				title: '指令单号',
				width: '12%',
				align: 'left',
				visible: true
			},{
                field: 'gg',
                title: '规格',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'jldw',
                title: '单位',
                width: '5%',
                align: 'left',
                visible: true
            },{
                field: 'lbcsmc',
                title: '类别',
                width: '5%',
                align: 'left',
                visible: true
            },{
                field: 'zsh',
                title: '追溯号',
                width: '6%',
                align: 'left',
                visible: true
            },{
				field: 'scph',
				title: '生产批号',
				width: '6%',
				align: 'left',
				visible: true
			},{
				field: 'ckmc',
				title: '仓库名称',
				width: '6%',
				align: 'left',
				visible: true
			},{
				field: 'cskwmc',
				title: '库位',
				width: '6%',
				align: 'left',
				visible: true
			},{
                field: 'dhdh',
                title: '到货单号',
                width: '13%',
                align: 'left',
                formatter:dhdhformat,
                visible: true
            },{
                field: 'lbcskz1',
                title: '检验',
                width: '5%',
                align: 'left',
                formatter:jyformat,
                visible: true
            },{
                field: 'jydh',
                title: '检验单号',
                width: '10%',
                align: 'left',
                formatter:jydhformat,
                visible: true
            },{
                field: 'sl',
                title: '数量',
                width: '4%',
                align: 'left',
                visible: true
            },{
                field: 'qyl',
                title: '取样量',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'scrq',
                title: '生产日期',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'yxq',
                title: '失效日期',
                width: '8%',
                align: 'left',
                visible: true
            },{
				field: 'cz',
				title: '操作',
				titleTooltip:'操作',
				width: '5%',
				align: 'left',
				formatter:stockPending_czFormat,
				visible: true
			}],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	StockPending_DealById(row.hwid,'view',$("#stockPending_formSearch #btn_view").attr("tourl"));
             },
		});
        $("#stockPending_formSearch #stockPending_list").colResizable({
            liveDrag:true, 
            gripInnerHtml:"<div class='grip'></div>", 
            draggingClass:"dragging", 
            resizeMode:'fit', 
            postbackSafe:true,
            partialRefresh:true}        
        );
	};
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   // 页面大小
        	pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "hwxx.hwid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		return getStockPendingSearchData(map);
	};
	return oTableInit;
}

/**
 * 检验方式格式化
 * @returns
 */
function jyformat(value,row,index) {
	if(row.lbcskz1=="1"){
		var html="<span>"+"试剂"+"</span>";
	}else if(row.lbcskz1=="2"){
		var html="<span>"+"仪器"+"</span>";
	}else if(row.lbcskz1=="3"){
		var html="<span>"+"设备"+"</span>";
	}else{
		var html="<span>"+"未检验"+"</span>";
	}
	return html;
}

/**
 * 操作按钮格式化函数
 * @returns
 */
function stockPending_czFormat(value,row,index) {
	var idshw = $("#stockPending_formSearch #idshw").val().split(",");
	if (row.rklb==null){
		row.rklb = "";
	}
	if($("#stockPending_formSearch #btn_warehousing").length>0){
		for(var i=0;i<idshw.length;i++){
			if(row.hwid==idshw[i]){
				return "<div id='hw"+row.hwid+"'><span id='t"+row.hwid+"' class='btn btn-danger' title='移出入库车' onclick=\"delWarehousing('" + row.hwid + "','" + row.rklb + "',event)\" >取消</span></div>";
			}
		}
		return "<div id='hw"+row.hwid+"'><span id='t"+row.hwid+"' class='btn btn-info' title='加入入库车' onclick=\"addWarehousing('" + row.hwid + "','" + row.rklb + "',event)\" >入库</span></div>";
	}
	return "";
}

/**
 * 物料编码格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function wlbmformat(value,row,index){
	var html = ""; 	
		html += "<a href='javascript:void(0);' onclick=\"queryByWlbm('"+row.wlid+"')\">"+row.wlbm+"</a>";
	return html;
}
function queryByWlbm(wlid){
	var url=$("#stockPending_formSearch #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
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

/**
 * 检验单号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function jydhformat(value,row,index){
	var html = ""; 	
	if(row.jydh!=null && row.jydh!=""){
		if(row.lbcskz1=='3'){
			html += "<a href='javascript:void(0);' onclick=\"queryBySbysid('"+row.hwid+"')\">"+row.jydh+"</a>";
		}else{
			html += "<a href='javascript:void(0);' onclick=\"queryByJydh('"+row.dhjyid+"')\">"+row.jydh+"</a>";
		}
	}		
	return html;
}
function queryByJydh(dhjyid){
	var url=$("#stockPending_formSearch #urlPrefix").val()+"/inspectionGoods/inspectionGoods/viewInspectionGoods?dhjyid="+dhjyid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'检验信息',viewJyConfig);	
}

var viewJyConfig = {
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

function queryBySbysid(hwid){
	var url=$("#stockPending_formSearch #urlPrefix").val()+"/device/device/viewDeviceCheck?hwid="+hwid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'设备验收',viewSbysConfig);
}

var viewSbysConfig = {
	width		: "1000px",
	modalName	:"viewDeviceCheckModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

/**
 * 到货单号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function dhdhformat(value,row,index){
	var html = ""; 	
		html += "<a href='javascript:void(0);' onclick=\"queryByDhdh('"+row.dhid+"')\">"+row.dhdh+"</a>";
	return html;
}
function queryByDhdh(dhid){
	var url=$("#stockPending_formSearch #urlPrefix").val()+"/storehouse/arrivalGoods/pagedataViewArrivalGoods?dhid="+dhid+"&access_token=" + $("#ac_tk").val();
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

//添加至入库车
function addWarehousing(hwid,rklb,event){
	var sfyhcgqx=$("#stockPending_formSearch #btn_warehousing");
	if(sfyhcgqx.length==0){
		$.confirm("很抱歉,您没有此权限!");
	}else{
		$.ajax({ 
		    type:'post',  
		    url:$('#stockPending_formSearch #urlPrefix').val()+"/warehouse/stockPending/pagedataSaveWarehousing", 
		    cache: false,
		    data: {"rklb":rklb,"hwid":hwid,"access_token":$("#ac_tk").val()},
		    dataType:'json', 
		    success:function(data){
		    	//返回值
		    	if(data.status=='success'){
		    		$("#idshw").val(data.idshw);
		    		Warehousing_addOrDelNum("add");
		    		$("#stockPending_formSearch #t"+hwid).remove();
		    		var html="<span id='t"+hwid+"' class='btn btn-danger' title='移出入库车' onclick=\"delWarehousing('" + hwid + "','" + rklb + "',event)\" >取消</span>";
		    		$("#stockPending_formSearch #hw"+hwid).append(html);
		    	}else{
					$.confirm(data.message);
				}
		    }
		});
	}
}


//从入库车中删除
function delWarehousing(hwid,rklb,event){
	var sfyhcgqx=$("#stockPending_formSearch #btn_warehousing");
	if(sfyhcgqx.length==0){
		$.confirm("很抱歉,您没有此权限!");
	}else{
		$.ajax({ 
		    type:'post',  
		    url:$('#stockPending_formSearch #urlPrefix').val()+"/warehouse/stockPending/pagedataDelWarehousing", 
		    cache: false,
		    data: {"hwid":hwid,"access_token":$("#ac_tk").val()},  
		    dataType:'json', 
		    success:function(data){
		    	//返回值
		    	if(data.status=='success'){
		    		$("#idshw").val(data.idshw);
		    		Warehousing_addOrDelNum("del");
		    		$("#stockPending_formSearch #t"+hwid).remove();
		    		var html="<span id='t"+hwid+"' class='btn btn-info' title='加入入库车' onclick=\"addWarehousing('" + hwid + "','" + rklb + "',event)\" >入库</span>";
		    		$("#stockPending_formSearch #hw"+hwid).append(html);
		    	}
		    }
		});
	}
}

/**
 * 入库车数字加减
 * @param sfbj
 * @returns
 */
function Warehousing_addOrDelNum(sfbj){
	if(sfbj=='add'){
		rk_count=parseInt(rk_count)+1;
	}else{
		rk_count=parseInt(rk_count)-1;
	}
	if((rk_count==1 && sfbj=='add') || (rk_count==0 && sfbj=='del')){
		btnOinit();
	}
	$("#rk_num").text(rk_count);
}

function getStockPendingSearchData(map){
	var stockPending_select=$("#stockPending_formSearch #stockPending_select").val();
	var stockPending_input=$.trim(jQuery('#stockPending_formSearch #stockPending_input').val());
	if(stockPending_select=="0"){//全部
		map["entire"]=stockPending_input
	}else if(stockPending_select=="1"){
		map["wlbm"]=stockPending_input
	}else if(stockPending_select=="2"){
		map["wlmc"]=stockPending_input
	}else if(stockPending_select=="3"){
		map["zsh"]=stockPending_input
	}else if(stockPending_select=="4"){
		map["bgdh"]=stockPending_input
	}else if(stockPending_select=="5"){
		map["dhdh"]=stockPending_input
	}else if(stockPending_select=="6"){
		map["jydh"]=stockPending_input
	}else if(stockPending_select=="7"){
		map["scph"]=stockPending_input
	}
	
	// 类别
	var lbs = jQuery('#stockPending_formSearch #lb_id_tj').val();
	map["lbs"] = lbs;
	
	return map;
}


function searchStockPendingResult(isTurnBack){
	//关闭高级搜索条件
	$("#stockPending_formSearch #searchMore").slideUp("low");
	StockPending_turnOff=true;
	$("#stockPending_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#stockPending_formSearch #stockPending_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#stockPending_formSearch #stockPending_list').bootstrapTable('refresh');
	}
}

function StockPending_DealById(id,action,tourl){
	if(!tourl){
		return;
	}
	tourl = $("#stockPending_formSearch #urlPrefix").val() + tourl;
	if(action=="view"){
		var url=tourl+"?hwid="+id
		$.showDialog(url,'查看待入库信息',viewStockPendingConfig);
	}else if(action=='warehousing'){
		if(rk_count=='0'){
			return;
		}
		var url=tourl;
		$.showDialog(url,'入库车',warehousingStockPendingConfig);
	}
}

var warehousingStockPendingConfig = {
		width		: "1600px",
		modalName	: "addStockPendingModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "提交",
				className : "btn-primary",
				callback : function() {
					if(!$("#editPutInStorageForm").valid()){
						$.alert("请填写完整信息");
						return false;
					}
					var ck="";
            		var lbbj="";
            		//0 类别标记相同，1:类别标记不同
            		var result_lb="0";
            		//0 仓库相同，1:仓库不同
            		var result_ck="0";
					if(t_map.rows != null && t_map.rows.length > 0){
						//判断到货类型是否一致
	            		var dhlxdm = t_map.rows[0].dhlxdm;
						let lb = "0";
						var lx = true;
						$.each(t_map.rows,function (i) {
							if ('1' == t_map.rows[i].lbbj){
								lb = "1";
							}
							if(dhlxdm != t_map.rows[i].dhlxdm){
								lx=false;
							}
						})
						if(!lx){
							$.alert("不允许到货类型不一致的货物一起入库！");
							return false;
						}
						
						var ckid=$("#editPutInStorageForm #ckid").val();
						if (lb == "1"){
							$.each(t_map.rows,function (i) {
								if (ckid == t_map.rows[i].dhckid){
									$.confirm("不能与到货时的仓库相同！");
									return false;
								}
							})
						}
						var hwJson = [];
						ck=t_map.rows[0].ckid;
	            		lbbj=t_map.rows[0].lbbj;
						for (var i = 0; i < t_map.rows.length; i++) {
							if(lbbj!=t_map.rows[i].lbbj){
								result_lb="1";
							}else{
								if(lbbj!="1" && ck!=t_map.rows[i].ckid){
									result_ck="1";
								}
							}							
							var sz={"hwid":'',"rkbz":'',"kwbh":''};
							sz.hwid = t_map.rows[i].hwid;
							sz.rkbz = t_map.rows[i].rkbz;
							if(t_map.rows[i].kwbh!="" && t_map.rows[i].kwbh!=null){
								sz.kwbh = t_map.rows[i].kwbh;
							}else{
								$.alert("库位信息不能为空！");
								return false;
							}									
							hwJson.push(sz);
						}
						
						if(result_lb=="1"){
	            			$.alert("入库物料需要区分ABC,不允许ABC类物料和类别为无的物料一起入库！");
	            			return false;
	            		}
	            		if(result_ck=="1"){
	            			$.alert("入库物料的仓库需要一致！");
	            			return false;
	            		}
						$("#editPutInStorageForm #hwxx_json").val(JSON.stringify(hwJson));
					}
					var $this = this;
					var opts = $this["options"]||{};
					
					$("#editPutInStorageForm input[name='access_token']").val($("#ac_tk").val());
					
					submitForm(opts["formName"]||"editPutInStorageForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
				    		$("#stockPending_formSearch #rk_num").remove();
				    		var auditType = $("#editPutInStorageForm #auditType").val();
							var putInStorage_params=[];
							putInStorage_params.prefix=$('#editPutInStorageForm #urlPrefix').val();
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);								
								}
								//提交审核
								showAuditFlowDialog(auditType,responseText["ywid"],function(){
									searchStockPendingResult();
								},null,putInStorage_params);
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


var viewStockPendingConfig = {
		width		: "1200px",
		modalName	:"viewStockPendingModal",
		offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};


var StockPending_oButton=function(){
	var oButtonInit = new Object();
	var postdata = {};
	oButtonInit.Init=function(){
		var btn_query=$("#stockPending_formSearch #btn_query");
		var btn_view = $("#stockPending_formSearch #btn_view");
		var btn_warehousing = $("#stockPending_formSearch #btn_warehousing");
		/*-----------------------模糊查询ok------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchStockPendingResult(true); 
    		});
		}
		/*-----------------------查看------------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#stockPending_formSearch #stockPending_list').bootstrapTable('getSelections');// 获取选择行数据
    		if(sel_row.length==1){
    			StockPending_DealById(sel_row[0].hwid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/*-----------------------入库车--------------------------------------*/
    	btn_warehousing.unbind("click").click(function(){
    		StockPending_DealById(null,"warehousing",btn_warehousing.attr("tourl"));
    	});
    	
       	/**显示隐藏**/      
    	$("#stockPending_formSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(StockPending_turnOff){
    			$("#stockPending_formSearch #searchMore").slideDown("low");
    			StockPending_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#stockPending_formSearch #searchMore").slideUp("low");
    			StockPending_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    	});
	}
	return oButtonInit;
}
var rk_count=0;
function btnOinit(){
	if(rk_count>0){
		var strnum=rk_count;
		if(rk_count>99){
			strnum='99+';
		}
		var html="";
		html+="<span id='rk_num' style='position:absolute;background-color:red;color:white;border-radius: 15px;border:0px;width:20px;height:20px;top:-10px;font-size:10px;line-height:150%;'>"+strnum+"</span>";
		$("#stockPending_formSearch #btn_warehousing").append(html);
	}else{
		$("#stockPending_formSearch #rk_num").remove();
	}
}


$(function(){
	if($("#rkcsl").val()==null || $("#rkcsl").val()==''){
		rk_count=0;
	}else{
		rk_count=$("#rkcsl").val();
	}
	btnOinit();
	var oTable = new StockPending_TableInit();
	    oTable.Init();
	
	var oButton = new StockPending_oButton();
	    oButton.Init();
	    
    jQuery('#stockPending_formSearch .chosen-select').chosen({width: '100%'});
})