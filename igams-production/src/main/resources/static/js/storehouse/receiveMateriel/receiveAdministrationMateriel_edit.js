var t_map = [];
var t_index="";
var loadFlag=$('#editXzPickingForm #loadFlag').val();
var tssh=$('#editXzPickingForm #tssh').val();
var lllx=$('#editXzPickingForm #lllx').val();
// //取样标记
// var qybj = $("#editXzPickingForm #qybj").val();
// 显示字段
var columnsArray = [
	{
    	title: '序',
		formatter: function (value, row, index) {
			return index+1;
		},
		titleTooltip:'序',
		width: '2%',
        align: 'left',
        visible:true
    }, {
        field: 'hwmc',
        title: '货物名称',
        titleTooltip:'物料名称',
        width: '8%',
        align: 'left',
        visible: true
	},{
        field: 'hwbz',
        title: '货物规格',
        titleTooltip:'货物规格',
        width: '8%',
        align: 'left',
        visible: true
    }, {
		field: 'klsl',
		title: '可领数量',
		titleTooltip:'可领数量',
		width: '6%',
		align: 'left',
		visible: true
	}, {
		field: 'qlsl',
		title:  $('#editXzPickingForm #formAction').val()!='/storehouse/receiveAdministrationMateriel/pagedataReleaseSaveReceiveAdminMateriel'?'领用数量':'出库数量',
		titleTooltip:$('#editXzPickingForm #formAction').val()!='/storehouse/receiveAdministrationMateriel/pagedataReleaseSaveReceiveAdminMateriel'?'领用数量':'出库数量',
		width: '6%',
		align: 'left',
		formatter: editXzPickingForm_qlsformat,
		visible: true
	},{
        field: 'cz',
        title: '操作',
        titleTooltip:'操作',
        width: '4%',
        align: 'left',
        formatter:editPickingCarForm_czformat,
        visible: true
    }];
var pickingEdit_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#editXzPickingForm #xzllmxtb_list').bootstrapTable({
            url: $('#editXzPickingForm #urlPrefix').val()+$('#editXzPickingForm #url').val(),         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#editXzPickingForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "wlmc",					//排序字段
            sortOrder: "asc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 15,                       //每页的记录行数（*）
            pageList: [15, 30, 50, 100],        //可供选择的每页的行数（*）
            paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            isForceTable: true,
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: false,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "xzllmxid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
            onLoadSuccess: function (map) {
            	t_map = map;
				// if(t_map.rows.length>0){
				// 	$("#editXzPickingForm #qgid").tagsinput({
				// 		itemValue: "qgid",
				// 		itemText: "djh",
				// 	})
				// 	for(var i = 0; i < t_map.rows.length; i++){
				// 		if (t_map.rows[i].qgid&&t_map.rows[i].djh){
				// 			var value =  t_map.rows[i].qgid;
				// 			var text =  t_map.rows[i].djh;
				// 			$("#editXzPickingForm #qgid").tagsinput('add', {"qgid":value,"djh":text});
				// 		}
				// 	}
				// }
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	return;
            },
        });
    };

    //得到查询的参数
    oTableInit.queryParams = function(params){
    	//请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
    	//例如 toolbar 中的参数 
    	//如果 queryParamsType = ‘limit’ ,返回参数必须包含 
    	//limit, offset, search, sort, order 
    	//否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder. 
    	//返回false将会终止请求
        var map = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: 1,   //页面大小
        	pageNumber: 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名  
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "wlid", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
			xzllid: $("#editXzPickingForm #xzllid").val(),
            //搜索框使用
            //search:params.search
        };
    	return map;
    };
    return oTableInit;
};


/**
 * 请领数量格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function editXzPickingForm_qlsformat(value,row,index){
	var qlsl="";
	var html="";
	if(row.qlsl!=null){
		qlsl=row.qlsl;
	}
	if (loadFlag=="add"){
		qlsl="";
	}
	// html ="<div id='qlsl_"+index+"' name='qlsdiv' isBeyond='false' style='background:darkcyan;'>";
	// html += "<input id='qls_"+index+"' autocomplete='off' value='"+qls+"' name='qls_"+index+"' style='width:55%;border:1px solid #D5D5D5;' validate='{qlsNumber:true}' ";
	// html += "onblur=\"checkqlsSum('"+index+"',this,\'qls\')\" ";
	// html += "></input>";
	// html += "/<span id='kcl_"+index+"' style='font-size:13px;width:45%;margin-left:3px;'>"+kcl+"</span>";
	// html += "</div>";
	html += "<input id='qlsl_"+index+"' value='"+qlsl+"' name='qlsl_"+index+"' style='width:100%;border:1px solid #D5D5D5;' validate='{qlslNumber:true}' onblur=\"checkSum('"+index+"',this,\'qlsl\')\"></input>";

	return html;
}

/**
 * 验证数量格式(两位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("qlslNumber", function (value, element){
	if(!value){
		$.error("请填写数量!");
		return false;
	}else{
		if(!/^\d+(\.\d{1,2})?$/.test(value)){
			$.error("请填写正确数量格式，可保留两位小数!");
		}
	}
    return this.optional(element) || /^\d+(\.\d{1,2})?$/.test(value);
},"请填写正确格式，可保留两位小数。");


/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function editPickingCarForm_czformat(value,row,index){
	var html="";
	var val = $('#editXzPickingForm #formAction').val();
	if(val!='/storehouse/receiveAdministrationMateriel/pagedataReleaseSaveReceiveAdminMateriel'){
		html = html+"<span style='margin-left:5px;height:24px !important;' class='btn btn-danger' title='删除信息' onclick=\"delXzDetail('" + index + "','"+row.qgmxid+"',event)\" >删除</span>";
		if (tssh=='Y'&&lllx=='1'){
			html = html+"<span style='margin-left:5px;height:24px !important;' class='btn btn-success' title='设备维护' onclick=\"modSbys('" + row.xzllmxid + "',event)\" >设备维护</span>";
		}
	}
	return html;
}

function modSbys(xzllmxid){
	var url=$("#editXzPickingForm #urlPrefix").val()+"/device/device/pagedataModDeviceCheck?xzllmxid="+xzllmxid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'设备维护',pagedataDeviceCheckConfig);
}
var pagedataDeviceCheckConfig = {
	width		: "1600px",
	modalName	: "pagedataDeviceCheckModel",
	formName	: "editDeviceCheckForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if (!$("#editDeviceCheckForm").valid()) {
					$.alert("请填写完整信息");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};

				$("#editDeviceCheckForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"editDeviceCheckForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if (responseText["xzllmxid"]){
								for (var i = 0; i <t_map.rows.length ; i++) {
									if (t_map.rows[i].xzllmxid==responseText["xzllmxid"]){
										t_map.rows[i].sbysid=responseText["ywid"];
									}
								}
							}
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								$("#editXzPickingForm #xzllmxtb_list").bootstrapTable('load',t_map);
							}
						});
					}else if(responseText["status"] == "repetition"){
						preventResubmitForm(".modal-footer > button", false);
						$.error("固定资产编号或设备编号重复！",function() {
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
/**
 * 删除操作
 * @param index
 * @param event
 * @returns
 */
function delXzDetail(index,value,event){
	t_map.rows.splice(index,1);
	$("#editXzPickingForm #xzllmxtb_list").bootstrapTable('load',t_map);
}

/**
 * 失去焦点事件(修改列表参数)
 * @param id
 * @param e
 * @param zdmc
 * @returns
 */
function checkSum(index, e, zdmc) {
	if(t_map.rows.length > index){
		if (zdmc == 'qlsl') {
			var qlsl = e.value;
			t_map.rows[index].qlsl = qlsl;
		}
	}
}



/**
 * 初始化页面
 * @returns
 */
function init(){
  	//添加日期控件
	laydate.render({
	   elem: '#sqrq'
	  ,theme: '#2381E9'
	});
}

/**
 * 选择申请人
 * @returns
 */
function chooseSqr() {
	var url = $('#editXzPickingForm #urlPrefix').val()+"/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择申请人', chooseQrrConfig);
}
var chooseQrrConfig = { 
	width : "800px",
	height : "500px",
	modalName	: "chooseSqrModal",
	formName	: "editXzPickingForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#taskListFzrForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					$('#editXzPickingForm #llry').val(sel_row[0].yhid);
					$('#editXzPickingForm #llrymc').val(sel_row[0].zsxm);
					$.closeModal(opts.modalName);
	    		}else{
	    			$.error("请选中一行");
	    			return;
	    		}
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
 * 选择发料人
 * @returns
 */
function chooseFlry() {
	var url = $('#editXzPickingForm #urlPrefix').val()+"/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择申请人', chooseFlryConfig);
}
var chooseFlryConfig = {
	width : "800px",
	height : "500px",
	modalName	: "chooseFlryModal",
	formName	: "editXzPickingForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#taskListFzrForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					$('#editXzPickingForm #flry').val(sel_row[0].yhid);
					$('#editXzPickingForm #flrymc').val(sel_row[0].zsxm);
					$.closeModal(opts.modalName);
				}else{
					$.error("请选中一行");
					return;
				}
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
 * 选择申请部门列表
 * @returns
 */
function chooseSqbm(){
	var url=$('#editXzPickingForm #urlPrefix').val() + "/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
	$.showDialog(url,'选择申请部门',addSqbmConfig);	
}
var addSqbmConfig = {
	width		: "800px",
	modalName	:"addSqbmModal",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row = $('#listDepartmentForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					$('#editXzPickingForm #sqbm').val(sel_row[0].jgid);
					$('#editXzPickingForm #sqbmmc').val(sel_row[0].jgmc);
				}else{
					$.error("请选中一行");
					return false;
				}
			},
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

/**
 * 选择明细
 * @returns
 */
function chooseRequisition(){
	var url = $('#editXzPickingForm #urlPrefix').val() + "/storehouse/receiveAdministrationMateriel/pagedataListChoosePicking?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择请购单', chooseQgConfig);
}
var chooseQgConfig = {
	width : "1000px",
	modalName	: "chooseQgModal",
	formName	: "chooseRequisitionForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#chooseRequisitionForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				// 获取更改后的请购单和明细信息
				var qgmxJson = [];
				if($("#chooseRequisitionForm #qgmx_json").val()){
					qgmxJson = JSON.parse($("#chooseRequisitionForm #qgmx_json").val());
					// 判断新增的请购明细信息
					var ids="";
					for (var i = 0; i < qgmxJson.length; i++) {
						var isAdd = true;
						for (var j = 0; j < t_map.rows.length; j++) {
							if(t_map.rows[j].xzkcid == qgmxJson[i].xzkcid){
								isAdd = false;
								break;
							}
						}
						if(isAdd){
							ids = ids + ","+ qgmxJson[i].xzkcid;
						}
					}
					if(ids.length > 0){
						ids = ids.substr(1);
						// 查询信息并更新到页面
						$.post($('#editXzPickingForm #urlPrefix').val() + "/storehouse/receiveAdministrationMateriel/pagedataGetRequisitionList",{"kcids":ids,"access_token":$("#ac_tk").val()},function(data){
							var mxdtos = data.rows;
							if(mxdtos != null && mxdtos.length > 0){
								// 更新页面列表(新增)
								for (var i = 0; i < mxdtos.length; i++) {
									mxdtos[i].qlsl=mxdtos[i].klsl;
									if(i == 0){
										if(!t_map.rows){
											t_map.rows= [];
										}
									}
									t_map.rows.push(mxdtos[i]);
								}
							}
							$("#editXzPickingForm #xzllmxtb_list").bootstrapTable('load',t_map);
							// 请购单号
							$.closeModal(opts.modalName);
						},'json');
					}else{
						$.closeModal(opts.modalName);
					}
				}else{
					$.alert("未获取到选中的请购信息！");
				}
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
 * 监听单据号标签移除事件
 */
var tagsRemoved = $('#editXzPickingForm #qgid').on('beforeItemRemove', function(event) {
	$.confirm('您确定要删除请购单下的所有明细记录吗？',function(result){
		if(result){
			var qgid = event.item.qgid;
			for (var i = t_map.rows.length-1; i >= 0 ; i--) {
				if(t_map.rows[i].qgid == qgid){
					t_map.rows.splice(i,1);

				}
			}
			$("#editXzPickingForm #xzllmxtb_list").bootstrapTable('load',t_map);
		}
	})
});
/**
 * 监听单据号标签点击事件
 */
var tagClick = $("#editXzPickingForm").on('click','.label-info',function(e){
	event.stopPropagation();
	var url = $('#editXzPickingForm #urlPrefix').val() + "/storehouse/receiveAdministrationMateriel/pagedataChoosePickingListInfo?access_token=" + $("#ac_tk").val() + "&qgid=" + e.currentTarget.dataset.logo;
	$.showDialog(url, '选择明细', chooseEditMxConfig);
});
var chooseEditMxConfig = {
	width : "1000px",
	modalName	: "chooseEditMxModal",
	formName	: "chooseRequisitionInfoForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#chooseRequisitionInfoForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				if($("#chooseRequisitionInfoForm input[name='checkQgmx']:checked").length == 0){
					$.alert("未选中请购明细信息！");
					return false;
				}
				// 获取更改请购单和明细信息
				var qgmxJson = [];
				$("#chooseRequisitionInfoForm input[name='checkQgmx']").each(function(){
					if($(this).is(":checked")){
						var sz={"qgid":'',"qgmxid":'',"xzkcid":''};
						sz.qgid = qgid;
						sz.xzkcid = this.dataset.xzkcid;
						sz.qgmxid = this.dataset.qgmxid;
						qgmxJson.push(sz);
					}
				})

				// 获取新增请购明细
				var ids="";
				for (var i = 0; i < qgmxJson.length; i++) {
					var isAdd = true;
					for (var j = 0; j < t_map.rows.length; j++) {
						if(t_map.rows[j].xzkcid == qgmxJson[i].xzkcid){
							isAdd = false;
							break;
						}
					}
					if(isAdd){
						ids = ids + ","+ qgmxJson[i].xzkcid;
					}
				}
				// 获取删除请购明细
				var qgid = $("#chooseRequisitionInfoForm input[name='qgid']").val();
				for (var j = t_map.rows.length-1; j >=0; j--) {
					if(qgid == t_map.rows[j].qgid){
						var isDel = true;
						for (var i = 0; i < qgmxJson.length; i++) {
							if(t_map.rows[j].xzkcid == qgmxJson[i].xzkcid){
								isDel = false;
								break;
							}
						}
						if(isDel){
							t_map.rows.splice(j,1);
						}
					}
				}
				if(ids.length > 0){
					ids = ids.substr(1);
					// 查询信息并更新到页面
					$.post($('#editXzPickingForm #urlPrefix').val() + "/storehouse/receiveAdministrationMateriel/pagedataGetRequisitionList",{"kcids":ids,"access_token":$("#ac_tk").val()},function(data){
						var dtos = data.rows;
						if(dtos != null && dtos.length > 0){
							// 更新页面列表(新增)
							for (var i = 0; i < dtos.length; i++) {
								dtos[i].qlsl=dtos[i].klsl;
								if(t_map.rows){
									t_map.rows.push(dtos[i]);
								}else{
									t_map.rows= [];
									t_map.rows.push(dtos[i]);
								}
							}
							$("#editXzPickingForm #xzllmxtb_list").bootstrapTable('load',t_map);
							$.closeModal(opts.modalName);
						}
					},'json');
				}else{
					$("#editXzPickingForm #xzllmxtb_list").bootstrapTable('load',t_map);
					$.closeModal(opts.modalName);
				}
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};


$(document).ready(function(){
	//初始化列表
	var oTable=new pickingEdit_TableInit();
	oTable.Init();
	// 初始化页面
	init();
	jQuery('#editXzPickingForm .chosen-select').chosen({width: '100%'});
});