var putIn_turnOff=true;
var PutInStorage_TableInit=function(){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#putInStorage_formSearch #putInStorage_list").bootstrapTable({
			url: $("#putInStorage_formSearch #urlPrefix").val()+'/warehouse/putInStorage/pageGetListPutInStorage',
            method: 'get',                      // 请求方式（*）
            toolbar: '#putInStorage_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "rkgl.rkrq",				// 排序字段
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
            uniqueId: "rkid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '4%'
            },{
                field: 'rkdh',
                title: '入库单号',
                width: '13%',
                align: 'left',
                visible: true
            },{
                field: 'rkrq',
                title: '入库日期',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'ckmc',
                title: '仓库名称',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'rklxmc',
                title: '入库类别',
                width: '8%',
                align: 'left',
                visible: true
            },{
                field: 'gysmc',
                title: '供应商',
                width: '20%',
                align: 'left',
                visible: true
            },{
				field: 'lrrymc',
				title: '录入人员',
				width: '8%',
				align: 'left',
				visible: true
			},{
                field: 'scbj',
                title: '状态',
                width: '4%',
                align: 'left',
                formatter:ztformat,
                visible: true
            },{
                field: 'bz',
                title: '备注',
                width: '20%',
                align: 'left',
                visible: true
            }, {
                field: 'zt',
                title: '审核状态',
                width: '10%',
                align: 'left',
                formatter:rkztformat,
                visible: true
            },{
				field: 'cz',
				title: '操作',
				titleTooltip:'操作',
				width: '5%',
				align: 'left',
				formatter:putInStorage_czFormat,
				visible: true
			}],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	PutInStorage_DealById(row.rkid,'view',$("#putInStorage_formSearch #btn_view").attr("tourl"),'');
             },
		});
        $("#putInStorage_formSearch #putInStorage_list").colResizable({
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
            sortLastName: "rkgl.rkdh", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		return getPutInStorageSearchData(map);
	};
	return oTableInit;
}

function ztformat(value,row,index){
	var html="";
	if(row.scbj=='0'){
		html="<span style='color:green;'>正常</span>";
	}else{
		html="<span style='color:red;'>废弃</span>";
	}
	return html;
}

/**
 * 操作按钮格式化函数
 * @returns
 */
function putInStorage_czFormat(value,row,index) {
	if (row.zt == '10' && row.scbj =='0' ) {
		return "<span class='btn btn-warning' onclick=\"recallPutInStorage('" + row.rkid + "',event)\" >撤回</span>";
	}else if(row.zt == '10' && row.scbj =='0' ){
		return "<span class='btn btn-warning' onclick=\"recallPutInStorage('" + row.rkid + "',event)\" >撤回</span>";
	}else{
		return "";
	}		
}

//撤回项目提交
function recallPutInStorage(rkid,event){
	var auditType = $("#putInStorage_formSearch #auditType").val();
	var msg = '您确定要撤回该合同吗？';
	var putInStorage_params = [];
	putInStorage_params.prefix = $("#putInStorage_formSearch #urlPrefix").val();
	$.confirm(msg,function(result){
		if(result){
			doAuditRecall(auditType,rkid,function(){
				searchPutInStorageResult();
			},putInStorage_params);
		}
	});
}
//状态格式化
function rkztformat(value,row,index){
	   if (row.zt == '00') {
	        return '未提交';
	    }else if (row.zt == '80'){
			return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.rkid + "\",event,\"AUDIT_GOODS_STORAGE\",{prefix:\"" + $('#putInStorage_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
	    }else if (row.zt == '15') {

			return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.rkid + "\",event,\"AUDIT_GOODS_STORAGE\",{prefix:\"" + $('#putInStorage_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
	    }else{

			return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.rkid + "\",event,\"AUDIT_GOODS_STORAGE\",{prefix:\"" + $('#putInStorage_formSearch #urlPrefix').val() + "\"})' >" + row.shxx_dqgwmc + "审核中</a>";
	    }
}


function getPutInStorageSearchData(map){
	var putInStorage_select=$("#putInStorage_formSearch #putInStorage_select").val();
	var putInStorage_input=$.trim(jQuery('#putInStorage_formSearch #putInStorage_input').val());
	if(putInStorage_select=="0"){
		map["rkdh"]=putInStorage_input
    }else if(putInStorage_select=="1"){
        map["wlmc"]=putInStorage_input
    }else if(putInStorage_select=="2"){
        map["wlbm"]=putInStorage_input
	}else if(putInStorage_select=="3"){
		map["u8rkdh"]=putInStorage_input
	}else if(putInStorage_select=="4"){
		map["lrrymc"]=putInStorage_input
	}else if(putInStorage_select=="5"){
		map["entire"]=putInStorage_input
	}
	
	// 入库开始日期
	var shsjstart = jQuery('#putInStorage_formSearch #shsjstart').val();
	map["shsjstart"] = shsjstart;
	
	// 入库结束日期
	var shsjend = jQuery('#putInStorage_formSearch #shsjend').val();
	map["shsjend"] = shsjend;
	
	// 删除标记
	var scbjs = jQuery('#putInStorage_formSearch #scbj_id_tj').val();
	map["scbjs"] = scbjs;
	
	return map;
}



function searchPutInStorageResult(isTurnBack){
	//关闭高级搜索条件
	$("#putInStorage_formSearch #searchMore").slideUp("low");
	putIn_turnOff=true;
	$("#putInStorage_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#putInStorage_formSearch #putInStorage_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#putInStorage_formSearch #putInStorage_list').bootstrapTable('refresh');
	}
}
function PutInStorage_DealById(id,action,tourl,rklxdm,ids){
	if(!tourl){
		return;
	}
	let bs = "";
	if (rklxdm == "bhg"){
		bs = "2"
	}
	tourl = $("#putInStorage_formSearch #urlPrefix").val() + tourl;
	if(action=="view"){
		var url=tourl+"?rkid="+id
		$.showDialog(url,'查看入库信息',viewPutInStorageConfig);
	}else if(action =='add'){
		var url=tourl;
		$.showDialog(url,'新增入库信息',addPutInStorageConfig);
	}else if(action =='mod'){
		var url=tourl + "?rkid=" +id+"&bs="+bs;
		$.showDialog(url,'修改入库信息',advancedPutInStorageConfig);
	}else if(action =='advancedmod'){
		var url=tourl + "?rkid=" +id+"&bs="+bs;
		$.showDialog(url,'修改入库信息',advancedPutInStorageConfig);
	}else if(action =='submit'){
		var url= tourl+"?rkid="+id+"&bs="+bs;
		$.showDialog(url,'提交入库信息',submitPutInStorageConfig);
	}else if(action=="viewCommon"){
		var url=tourl+"?rkid="+id;
		$.showDialog(url,'入库详细信息 共通',viewPutInStorageConfig);
	}else if (action=='hwprint'){
		var url=tourl+"?rkids="+ids;
		$.showDialog(url,'选择打印数据',printPutInStorageConfig);
	}
}

var printPutInStorageConfig = {
	width		: "1500px",
	modalName	: "printPutInStorageModal",
	formName	: "printPutInStorage_formSearch",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#printPutInStorage_formSearch").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
					var sel_row = $('#printPutInStorage_formSearch #printPutInStorage_list').bootstrapTable('getSelections');//获取选择行数据
					if(sel_row.length ==0){
						$.error("请至少选中一行");
						return;
					}
					var hwids="";
					for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
						hwids = hwids + ","+ sel_row[i].hwid;

					}
					hwids = hwids.substr(1);
					$('#printPutInStorage_formSearch #hwids').val(hwids);

				$("#printPutInStorage_formSearch input[name='access_token']").val($("#ac_tk").val());
				var url=$('#putInStorage_formSearch #urlPrefix').val()+"/warehouse/putInStorage/pagedataHwprintFreight"+"?hwids="+hwids+"&access_token="+$("#ac_tk").val();
				window.open(url);
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
var submitPutInStorageConfig = {
		width		: "1600px",
		modalName	: "submitPutInStorageModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "提交",
				className : "btn-primary",
				callback : function() {
					if(!$("#editPutInStorageForm").valid()){
						return false;
					}
					var ck="";
            		var lbbj="";
            		//0 类别标记相同，1:类别标记不同
            		var result_lb="0";
            		//0 仓库相同，1:仓库不同
            		var result_ck="0";
					if(t_map.rows != null && t_map.rows.length > 0){
						var hwJson = [];
						ck=t_map.rows[0].ckid;
	            		lbbj=t_map.rows[0].lbbj;
						var bs =  $('#editPutInStorageForm #bs').val();
						var csdm = $("#editPutInStorageForm #rklb").find("option:selected").attr("csdm");
						let rklb = $("#editPutInStorageForm #rklb").val();
						for (var i = 0; i < t_map.rows.length; i++) {
							if (rklb != t_map.rows[i].rklb && bs !="2" && csdm != "bhg"){
								$.alert("所选到货信息存在与入库类别不相符的数据！");
								return false;
							}
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
							if(opts.offAtOnce){
								//新增提交审
								var putInStorage_params=[];
								putInStorage_params.prefix=$('#editPutInStorageForm #urlPrefix').val();
								var auditType = $("#editPutInStorageForm #auditType").val();
								var ywid = $("#editPutInStorageForm #rkid").val();
								showAuditFlowDialog(auditType,ywid,function(){
									$.closeModal(opts.modalName);
									searchPutInStorageResult();
								},null,putInStorage_params);
								$.closeModal(opts.modalName);
							}
						}else if(responseText["status"] == "fail"){
							$.error(responseText["message"],function() {
								preventResubmitForm(".modal-footer > button", false);
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
}

var advancedPutInStorageConfig={
		width		: "1600px",
		modalName	: "advancedPutInStorageModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "保存",
				className : "btn-success",
				callback : function() {
					if(!$("#editPutInStorageForm").valid()){
						$.alert("请填写完整信息");
						return false;
					}
					if(!($("#editPutInStorageForm #hwxx_json").val().length>2)){
						$.alert("货物信息不能为空！");
						return false;
					}
					if(t_map.rows != null && t_map.rows.length > 0){
						var hwJson = [];
						let rklb = $("#editPutInStorageForm #rklb").val();
						var bs =  $('#editPutInStorageForm #bs').val();
						var csdm = $("#editPutInStorageForm #rklb").find("option:selected").attr("csdm");
						for (var i = 0; i < t_map.rows.length; i++) {
							if (rklb != t_map.rows[i].rklb && bs !="2" && csdm != "bhg"){
								$.alert("所选到货信息存在与入库类别不相符的数据！");
								return false;
							}
							var sz={"hwid":'',"rkbz":'',"kwbh":''};
							sz.hwid = t_map.rows[i].hwid;
							sz.rkbz = t_map.rows[i].rkbz;
							sz.kwbh = t_map.rows[i].kwbh;
							hwJson.push(sz);
						}
						$("#editPutInStorageForm #hwxx_json").val(JSON.stringify(hwJson));
					}
					var $this = this;
					var opts = $this["options"]||{};
					$("#editPutInStorageForm input[name='access_token']").val($("#ac_tk").val());

					submitForm(opts["formName"]||"editPutInStorageForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								searchPutInStorageResult();
							});
						}else if(responseText["status"] == "fail"){
							preventResubmitForm(".modal-footer > button", false);
							$.error(responseText["message"],function() {
							});
						}else{
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

var chancePuInTypeConfig={
		width		: "1600px",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		modalName	: "chancePurchaseTypeModal",
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		} 	
};

var addPutInStorageConfig = {
	width		: "1600px",
	modalName	: "editPutInStorageModal",
	formName	: "editPutInStorageForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#editPutInStorageForm").valid()){
					$.alert("请填写完整信息");
					return false;
				}
				if($("#editPutInStorageForm #gyscfbj").val()==1){
					$.alert("请选择供应商相同的货物！");
					return false;
				}
				if(t_map.rows != null && t_map.rows.length > 0){
					let rklb = $("#editPutInStorageForm #rklb").val();
					var hwJson = [];
					var ck="";
            		var lbbj="";
            		//0 类别标记相同，1:类别标记不同
            		var result_lb="0";
            		//0 仓库相同，1:仓库不同
            		var result_ck="0";
					let lb = "0";
					$.each(t_map.rows,function (i) {
						if ('1' == t_map.rows[i].lbbj){
							lb = "1";
						}
					})
					var ckid=$("#editPutInStorageForm #ckid").val();
					if (lb == "1"){
						$.each(t_map.rows,function (i) {
							if (ckid == t_map.rows[i].dhckid){
								$.confirm("不能与到货时的仓库相同！");
								return false;
							}
						})
					}
					for (var i = 0; i < t_map.rows.length; i++) {
						if (rklb != t_map.rows[i].rklb){
							$.alert("所选到货信息存在与入库类别不相符的数据！");
							return false;
						}
						ck=t_map.rows[0].ckid;
	            		lbbj=t_map.rows[0].lbbj;
	            		if(lbbj!=t_map.rows[i].lbbj){
							result_lb="1";
						}else{
							if(lbbj!="1" && ck!=t_map.rows[i].ckid){
								result_ck="1";
							}
						}	
						var sz={"hwid":'',"rkbz":'',"kwbh":'',"sl":"","wlid":"","zsh":"","scph":"","scrq":"","yxq":""};
						sz.hwid = t_map.rows[i].hwid;
						sz.rkbz = t_map.rows[i].rkbz;
						if(t_map.rows[i].sl!="" && t_map.rows[i].sl!=null){
							sz.sl = t_map.rows[i].sl;
						}else{
							$.alert("数量不能为空！");
							return false;
						}						
						sz.wlid = t_map.rows[i].wlid;
						if(t_map.rows[i].kwbh!="" && t_map.rows[i].kwbh!=null){
							sz.kwbh = t_map.rows[i].kwbh;
						}else{
							$.alert("库位信息不能为空！");
							return false;
						}			
						var rklbbj = $("#editPutInStorageForm #rklbbj").val();
						if(rklbbj=="1"){
							if(t_map.rows[i].zsh!="" && t_map.rows[i].zsh!=null){
								sz.zsh = t_map.rows[i].zsh;
							}else{
								$.alert("追溯号不能为空！");
								return false;
							}
							if(t_map.rows[i].scrq!="" && t_map.rows[i].scrq!=null){
								sz.scrq = t_map.rows[i].scrq;
							}else{
								$.alert("生产日期不能为空！");
								return false;
							}
							if(t_map.rows[i].yxq!="" && t_map.rows[i].yxq!=null){
								sz.yxq = t_map.rows[i].yxq;
							}else{
								$.alert("失效日期不能为空！");
								return false;
							}
							sz.scph = t_map.rows[i].scph;							
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
						var auditType = $("#editPutInStorageForm #auditType").val();
						var putInStorage_params=[];
						putInStorage_params.prefix=$('#editPutInStorageForm #urlPrefix').val();
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);								
							}
							//提交审核
							showAuditFlowDialog(auditType,responseText["ywid"],function(){
								searchPutInStorageResult();
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

var viewPutInStorageConfig = {
		width		: "1600px",
		modalName	:"viewPutInStorageModal",
		offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};


//提供给导出用的回调函数
function PutInSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="rkgl.rkrq";
	map["sortLastOrder"]="desc";
	map["sortName"]="rkgl.rkdh";
	map["sortOrder"]="desc";
	return getPutInStorageSearchData(map);
}

var PutInStorage_oButton=function(){
	var oButtonInit = new Object();
	var postdata = {};
	//添加日期控件
	laydate.render({
	   elem: '#shsjstart'
	  ,theme: '#2381E9'
	});
	//添加日期控件
	laydate.render({
	   elem: '#shsjend'
	  ,theme: '#2381E9'
	});
	oButtonInit.Init=function(){
		var btn_query=$("#putInStorage_formSearch #btn_query");
		var btn_view = $("#putInStorage_formSearch #btn_view");
		var btn_add = $("#putInStorage_formSearch #btn_add");
		var btn_mod = $("#putInStorage_formSearch #btn_mod");
		let btn_advancedmod=$("#putInStorage_formSearch #btn_advancedmod");
        var btn_hwprint = $("#putInStorage_formSearch #btn_hwprint");
		var btn_submit=$("#putInStorage_formSearch #btn_submit");
		var btn_del=$("#putInStorage_formSearch #btn_del");
		var btn_discard=$("#putInStorage_formSearch #btn_discard");
		var btn_searchexport = $("#putInStorage_formSearch #btn_searchexport");
		var btn_selectexport = $("#putInStorage_formSearch #btn_selectexport");
		var btn_viewCommon = $("#putInStorage_formSearch #btn_viewCommon");

/*-----------------------模糊查询ok------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchPutInStorageResult(true); 
    		});
		}
/*-----------------------查看------------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#putInStorage_formSearch #putInStorage_list').bootstrapTable('getSelections');// 获取选择行数据
    		if(sel_row.length==1){
    			PutInStorage_DealById(sel_row[0].rkid,"view",btn_view.attr("tourl"),'');
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	
    /* --------------------------- 新增 -----------------------------------*/
    	btn_add.unbind("click").click(function(){
    		PutInStorage_DealById(null, "add", btn_add.attr("tourl"),'');
    	});
    	
    	/* --------------------------- 修改 -----------------------------------*/
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#putInStorage_formSearch #putInStorage_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length == 1){
    			if(sel_row[0].zt=="00" || sel_row[0].zt=="15"){
    				PutInStorage_DealById(sel_row[0].rkid, "mod", btn_mod.attr("tourl"),sel_row[0].rklxdm,'');
    			}else{
    				$.alert("该记录在审核中或已审核，不允许修改!");
    			}   			   			
    		}else{
    			$.error("请选中一行");
    		}
    	});

		/* --------------------------- 高级修改 -----------------------------------*/
		btn_advancedmod.unbind("click").click(function(){
			var sel_row = $('#putInStorage_formSearch #putInStorage_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length == 1){
				PutInStorage_DealById(sel_row[0].rkid, "advancedmod", btn_advancedmod.attr("tourl"),sel_row[0].rklxdm,'');
			}else{
				$.error("请选中一行");
			}
		});

		/*--------------------------------提交---------------------------*/
		btn_submit.unbind("click").click(function(){
			var sel_row=$('#putInStorage_formSearch #putInStorage_list').bootstrapTable('getSelections');
    		if(sel_row.length==1){
    			if(sel_row[0].zt=="00" || sel_row[0].zt=="15"){
    				PutInStorage_DealById(sel_row[0].rkid,"submit",btn_submit.attr("tourl"),sel_row[0].rklxdm,'');
    			}else{
    				$.alert("该状态不允许提交!");
    			}
    		}else{
    			$.error("请选中一行");
    		}
    	});
		
		/* ------------------------------删除入库信息-----------------------------*/
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#putInStorage_formSearch #putInStorage_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==0){
    			$.error("请至少选中一行");
    			return;
    		}else {		
    			var ids="";
    			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids= ids + ","+ sel_row[i].rkid;
        		}
    			ids=ids.substr(1);
    			$.confirm('您确定要删除所选择的信息吗？',function(result){
        			if(result){
        				jQuery.ajaxSetup({async:false});
        				var url= $('#putInStorage_formSearch #urlPrefix').val()+btn_del.attr("tourl");
        				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
        					setTimeout(function(){
        						if(responseText["status"] == 'success'){
        							$.success(responseText["message"],function() {
        								searchPutInStorageResult();
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
        /* --------------------------- 打印货位卡 -----------------------------------*/
        btn_hwprint.unbind("click").click(function(){
            var sel_row = $('#putInStorage_formSearch #putInStorage_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                ids = ids + ","+ sel_row[i].rkid;
            }
			ids = ids.substr(1);
			PutInStorage_DealById('', "hwprint", btn_hwprint.attr("tourl"),sel_row[0].rklxdm,ids);
			// var url=$('#putInStorage_formSearch #urlPrefix').val()+btn_hwprint.attr("tourl")+"?ids="+ids.toString()+"&access_token="+$("#ac_tk").val();
            // window.open(url);
            // $.ajax({
            // 	type : 'get',
            // 	url : url,
            // 	success:function(map){
            // 		if ( map.hwlist.length>0 && map.hwlist!=null){
            // 			$.each(  map.hwlist,function (index,value) {
            // 				let url1 = $('#arrivalGoods_formSearch #urlPrefix').val()+"/storehouse/arrivalGoods/freightPrintOpen"+"?ysdh="+value.ysdh+
            // 					"&wlbm="+value.wlbm+"&wlmc="+value.wlmc+"&zsh="+value.zsh+"&jldw="+value.jldw+"&gg="+value.gg+"&yxq="+value.yxq+
            // 					"&gysmc="+value.gysmc+"&scph="+value.scph+"&dhrq="+value.dhrq+"&dhdh="+value.dhdh+"&lrry="+value.lrry+"&dhsl="+value.dhsl+
            // 					"&cskwmc="+value.cskwmc+"&access_token="+$("#ac_tk").val();
            // 				window.open(url1);
            // 			})
            // 		}
            // 	}
            // });
        });
    	/* ------------------------------废弃入库信息-----------------------------*/
    	btn_discard.unbind("click").click(function(){
    		var sel_row = $('#putInStorage_formSearch #putInStorage_list').bootstrapTable('getSelections');//获取选择行数据
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			if(sel_row[i].zt !='00' && sel_row[i].zt !='15'){
    				$.error("该记录在审核中或已审核，不允许废弃!");
        			return;
    			}
    		}
    		if(sel_row.length==0){
    			$.error("请至少选中一行");
    			return;
    		}else{
    			var ids="";
    			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids= ids + ","+ sel_row[i].rkid;
        		}
    			ids=ids.substr(1);
    			$.confirm('您确定要废弃所选择的信息吗？',function(result){
        			if(result){
        				jQuery.ajaxSetup({async:false});
        				var url=$('#putInStorage_formSearch #urlPrefix').val() + btn_discard.attr("tourl");
        				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
        					setTimeout(function(){
        						if(responseText["status"] == 'success'){
        							$.success(responseText["message"],function() {
        								searchPutInStorageResult();
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
    	
    	 // 	  ---------------------------导出-----------------------------------
    	btn_selectexport.unbind("click").click(function(){
    		var sel_row = $('#putInStorage_formSearch #putInStorage_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length>=1){
    			var ids="";
        		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids = ids + ","+ sel_row[i].rkid;
        		}
        		ids = ids.substr(1);
        		$.showDialog($('#putInStorage_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=GOODSRK_SELECT&expType=select&ids="+ids
        				,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    		}else{
    			$.error("请选择数据");
    		}
    	});

    	btn_searchexport.unbind("click").click(function(){
    		$.showDialog($('#putInStorage_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=GOODSRK_SEARCH&expType=search&callbackJs=PutInSearchData"
    				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    	});	
    	/* ---------------------------共通 查看列表-----------------------------------*/
    	btn_viewCommon.unbind("click").click(function(){
    		var sel_row = $('#putInStorage_formSearch #putInStorage_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			PutInStorage_DealById(sel_row[0].rkid,"viewCommon",btn_viewCommon.attr("tourl"),'');
    		}else{
    			$.error("请选中一行");
    		}
    	});
		/*-------------------------------------------------------------*/
    	
	}
	return oButtonInit;
}

var PutInStorage_PageInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
    	var scbj = $("#putInStorage_formSearch a[id^='scbj_id_']");
    	$.each(scbj, function(i, n){
    		var id = $(this).attr("id");
    		var code = id.substring(id.lastIndexOf('_')+1,id.length);
    		if(code === '0'){
    			addTj('scbj',code,'putInStorage_formSearch');
    		}
    	});
    }
    return oInit;
}
/**显示隐藏**/      
$("#putInStorage_formSearch #sl_searchMore").on("click", function(ev){
	var ev=ev||event; 
	if(putIn_turnOff){
		$("#putInStorage_formSearch #searchMore").slideDown("low");
		putIn_turnOff=false;
		this.innerHTML="基本筛选";
	}else{
		$("#putInStorage_formSearch #searchMore").slideUp("low");
		putIn_turnOff=true;
		this.innerHTML="高级筛选";
	}
	ev.cancelBubble=true;
});
$(function(){
	//0.界面初始化
     	var oInit = new PutInStorage_PageInit();
    	oInit.Init();
    
	var oTable = new PutInStorage_TableInit();
	    oTable.Init();
	
	var oButton = new PutInStorage_oButton();
	    oButton.Init();
	    
    jQuery('#putInStorage_formSearch .chosen-select').chosen({width: '100%'});
})