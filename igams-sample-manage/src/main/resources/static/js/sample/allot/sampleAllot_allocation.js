var formAction = $("#sampleAllotForm #formAction").val();
var drhzInfoList = [];
$(document).ready(function(){
	jQuery('.chosen-select').chosen({width: '100%'});
	// 初始化日期控件
	initLaydate();
	// 初始化盒子信息
    var oTable = new hzInfo_TableInit();
    oTable.Init();
	if($("#sampleAllotForm #ybdbid").val()){
    	//修改页面
    	hzInfoList = JSON.parse($("#sampleAllotForm #hzlist").val())
	}
	//样本调拨特殊审核
	if (formAction == 'commonAudit'){
		//①.调拨日期，调拨人，调出储存单位，调出冰箱（格式：位置（设备号）），调出抽屉，调入储存单位，物流信息，调出盒子位置，调出盒子编号只读。
		$('#sampleAllotForm #dcccdw').attr("disabled", true);
		$('#sampleAllotForm #dcccdw').trigger("chosen:updated");
		$('#sampleAllotForm #dcbx').attr("disabled", true);
		$('#sampleAllotForm #dcbx').trigger("chosen:updated");
		$('#sampleAllotForm #dcct').attr("disabled", true);
		$('#sampleAllotForm #dcct').trigger("chosen:updated");
		$('#sampleAllotForm #drccdw').attr("disabled", true);
		$('#sampleAllotForm #drccdw').trigger("chosen:updated");
		$('#sampleAllotForm #wlxx').attr("readonly","readonly");
		//②.调入冰箱（格式：位置（设备号）），调入抽屉，调入盒子位置（格式：位置（设备号）），调入盒子编号必填，选择调入盒子位置，如果盒子的编号不为空，
		drhzInfoList = JSON.parse($("#sampleAllotForm #drhzList").val())
		//	渲染到盒子编号到调入盒子编号，只读。如果调入盒子编号为空，调入盒子编号文本框允许输入，必填。
		//③.新增按钮，调拨全部按钮，删除按钮不显示，查看样本按钮和普通审核页面点击显示页面一致。
	}
});
/*获取抽屉list*/
function getDrCtList_Allot() {
	var drbx = document.getElementById("drbx").value;
	if (drbx){
		$.ajax({
			type: "post",
			url: $("#urlPrefix").val()+"/sample/device/pagedataSbListByFsbid",
			data: {"fids":drbx,"access_token":$("#ac_tk").val()},
			dataType: "json",
			success: function (data) {
				if(data != null && data.length != 0){
					var zHtml = "";
					$.each(data,function(i){
						zHtml += "<option value='" + data[i].sbid + "' id='"+data[i].sbid+"'>" + data[i].wz + "</option>";
					});
					$("#sampleAllotForm #drct").empty();
					$("#sampleAllotForm #drct").append(zHtml);
					$("#sampleAllotForm #drct").trigger("chosen:updated");
					var drct = document.getElementById("drct").value;
					/*获取盒子list*/
					$.ajax({
						type: "post",
						url: $("#urlPrefix").val()+"/sample/device/pagedataSbListByFsbid",
						data: {"fids":drct,"access_token":$("#ac_tk").val()},
						dataType: "json",
						success: function (data) {
							if(data != null && data.length != 0){
								drhzInfoList = data;
							}else{
								drhzInfoList = [];
							}
							$('#sampleAllotForm #tb_hzInfo_list').bootstrapTable('refresh');
						}
					})
				}else{
					EmptySb('drct','该冰箱下没有抽屉')
					drhzInfoList = [];
					$('#sampleAllotForm #tb_hzInfo_list').bootstrapTable('refresh');
				}
			}
		})
	}else{
		EmptySb('drct','该冰箱下没有抽屉')
		drhzInfoList = [];
		$('#sampleAllotForm #tb_hzInfo_list').bootstrapTable('refresh');
	}
}
/*获取盒子list*/
function getDrHzList_Allot(){
	var drct = document.getElementById("drct").value;
	if (drct){
		/*获取盒子list*/
		$.ajax({
			type: "post",
			url: $("#urlPrefix").val()+"/sample/device/pagedataSbListByFsbid",
			data: {"fids":drct,"access_token":$("#ac_tk").val()},
			dataType: "json",
			success: function (data) {
				if(data != null && data.length != 0){
					drhzInfoList = data;
				}else{
					drhzInfoList = [];
				}
				$('#sampleAllotForm #tb_hzInfo_list').bootstrapTable('refresh');
			}
		})
	}else{
		drhzInfoList = [];
		$('#sampleAllotForm #tb_hzInfo_list').bootstrapTable('refresh');
	}
}
//空设备
function EmptySb(id,emptyData){
	var HzZHtml = "";
	HzZHtml += "<option value=''>--"+emptyData+"--</option>";
	$("#sampleAllotForm #"+id).empty();
	$("#sampleAllotForm #"+id).append(HzZHtml);
	$("#sampleAllotForm #"+id).trigger("chosen:updated");
}
var t_map=[];
//选择调拨人弹框初始化
var chooseDbrConfig = {
	width : "800px",
	height : "500px",
	modalName	: "chooseDBrModal",
	formName	: "sampleAllotForm",
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
					$("#sampleAllotForm #dbr").val(sel_row[0].yhid);
					$("#sampleAllotForm #dbrmc").val(sel_row[0].zsxm);
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
var hzInfo_TableInit = function(){
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#sampleAllotForm #tb_hzInfo_list").bootstrapTable({
			url: ($("#sampleAllotForm #urlPrefix").val()?$("#sampleAllotForm #urlPrefix").val():($("#urlPrefix").val()?$("#urlPrefix").val():"")) + '/sampleAllot/pagedataSampleAllotInfo?ybdbid='+$("#sampleAllotForm #ybdbid").val(),         //请求后台的URL（*）
			method: 'get',                      //请求方式（*）
			toolbar: "#sampleAllotForm #toolbar",                //工具按钮用哪个容器
			striped: false,                      //是否显示行间隔色
			cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   //是否显示分页（*）
			paginationShowPageGo: false,         //增加跳转页码的显示
			sortable: true,                     //是否启用排序
			sortName: "ybdbmx.xh",				//排序字段
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
			showColumns: false,                  //是否显示所有的列
			showRefresh: false,                  //是否显示刷新按钮
			minimumCountColumns: 2,             //最少允许的列数
			clickToSelect: false,                //是否启用点击选中行
			//height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId: "dbmxid",                     //每一行的唯一标识，一般为主键列
			showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
			cardView: false,                    //是否显示详细视图
			detailView: false,                   //是否显示父子表
			paginationDetailHAlign:' hidden',
			columns: [{
                'field': 'xh',
                'title': '序号',
                'align': 'center',
                'width': '10%',
				visible: true,
            },{
				field: 'dbmxid',
				title: '调拨明细id',
				width: '10%',
				align: 'left',
				visible:false
			},{
				field: 'dchz',
				title: '调出盒子',
				width: formAction == 'commonAudit'?'30%':'40%',
				align: 'left',
				visible: true,
				formatter:dchzInfoformat,
			},{
				field: 'drhz',
				title: '调入盒子',
				width: '30%',
				align: 'left',
				visible: formAction == 'commonAudit',
				formatter:drhzInfoformat,
			},{
				field: 'cz',
				title: '操作',
				width: formAction == 'commonAudit'?'20%':'40%',
				align: 'left',
				formatter:czformat,
				visible: true
			}],
			onLoadSuccess:function(map){
				t_map=map;
                setHzInfo_json();
			},
			onLoadError:function(){
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
			},
		});
		$("#sampleAllotForm #tb_hzInfo_list").colResizable({
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
			sortLastName: "ybdbmx.xh", // 防止同名排位用
			sortLastOrder: "asc" // 防止同名排位用
			// 搜索框使用
			// search:params.search
		};
		return map;
	};
	return oTableInit;
}
var hzInfoList = [];
//提交按钮点击事件
$("#sampleAllotForm").submit(function() {
//	$("#sampleAllotForm #cklb").attr("disabled",false);
//	$("#sampleAllotForm #rklb").attr("disabled",false);
	return false;
});

//初始化日期控件
function initLaydate(){
  	//添加日期控件
	laydate.render({
	   elem: '#dbrq'
	  ,theme: '#2381E9'
	});
}

//选择调拨人
function chooseDbr() {
	var url = ($("#sampleAllotForm #urlPrefix").val()?$("#sampleAllotForm #urlPrefix").val():($("#urlPrefix").val()?$("#urlPrefix").val():""))+"/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择调拨人', chooseDbrConfig);
}

//储存单位改变事件
function checkCcdw(id,otherid){
    var ccdw = $("#sampleAllotForm #" + id).val();
    var otherccdw = $("#sampleAllotForm #" + otherid).val();
    if (ccdw && ccdw == otherccdw) {
        $("#sampleAllotForm #" + id +" option:first").prop("selected", 'selected');
        $("#sampleAllotForm #" + id).trigger("chosen:updated");
        $.alert("调出储存单位和调入储存单位不能是同一个，如果要单位内部调拨请在样本库存列表使用盒子修改按钮！");
        return
    }
    if (id == 'dcccdw'){
        hzInfoList = [];
        getAllotConditionsByFather("dcccdw","dcbx","/sampleAllot/pagedataGetBxList")
    }
}

//冰箱改变事件
function checkBx(){
    hzInfoList = [];
    getAllotConditionsByFather("dcbx","dcct","/sampleAllot/pagedataGetSbList")
}
//抽屉改变事件
function checkCt(){
    hzInfoList = [];
    getAllotConditionsByFather("dcct","hz","/sampleAllot/pagedataGetSbList")
}

//父参数改变事件
function getAllotConditionsByFather(fatherName,childName,url) {
    delAllHzInfo()
	// 获取选中的父参数
	var ids = jQuery("#sampleAllotForm #" + fatherName).val();
	if (!isEmpty(ids)) {
		ids = "'" + ids + "'";
	}
	var html = "<option value=''>--请选择--</option>";
	// 若选中的父参数不为空
	if (!isEmpty(ids)) {
		var url = ($("#sampleAllotForm #urlPrefix").val()?$("#sampleAllotForm #urlPrefix").val():($("#urlPrefix").val()?$("#urlPrefix").val():"")) + url;
		$.ajax({
			type : "POST",
			url : url,
			data : {"fcsids":ids,"access_token":$("#ac_tk").val(),"ybdbid":("hz" == childName?$("#sampleAllotForm #ybdbid").val()?$("#sampleAllotForm #ybdbid").val():"undefine":"")},
			dataType : "json",
			success : function(data){
				if(data != null && data.list.length > 0) {
				    if ("hz" == childName){
                        hzInfoList = data.list
				    }else{
                        $.each(data.list,function(i){
                            if(childName == 'dcct'){
                                html +=  "<option value='" + data.list[i].csid + "' id='"+data.list[i].csid+"' csmc='"+data.list[i].wz +"'>" + data.list[i].wz + "</option>";
                            }else{
                                html +=  "<option value='" + data.list[i].csid + "' id='"+data.list[i].csid+"' csmc='"+data.list[i].wz + (data.list[i].sbh?"(" + data.list[i].sbh + ")":"") +"'>" + data.list[i].wz + (data.list[i].sbh?"(" + data.list[i].sbh + ")":"") + "</option>";
                            }
                        });
				    }
				}
				if ("hz" != childName){
                    $("#sampleAllotForm #" + childName).empty();
                    $("#sampleAllotForm #" + childName).append(html);
                    $("#sampleAllotForm #" + childName).trigger("chosen:updated");
                    //若是调出储存单位改变，则也执行冰箱改变事件，即更新抽屉
                    if (fatherName == "dcccdw"){
                        checkBx()
                    }
                }
			}
		});
	}else{
		if ("hz" != childName){
            $("#sampleAllotForm #" + childName).empty();
            $("#sampleAllotForm #" + childName).append(html);
            $("#sampleAllotForm #" + childName).trigger("chosen:updated");
            //若是调出储存单位改变，则也执行冰箱改变事件，即更新抽屉
            if (fatherName == "dcccdw"){
                checkBx()
            }
        }
	}
}
//盒子信息初始化
function setHzInfo_json(){
	var json=[];
	var data=$("#sampleAllotForm #tb_hzInfo_list").bootstrapTable('getData');//获取选择行数据
	for(var i=0;i<data.length;i++){
		var sz={"index":i,"xh":i+1,"dbmxid":"","dchz":"","dchzmc":"","drhz":""};
		sz.xh=i+1;
		sz.dbmxid=data[i].dbmxid;
		sz.dchz=data[i].dchz;
		sz.dchzmc=data[i].dchzmc;
		sz.drhz=data[i].drhz;
		json.push(sz);
	}
	$("#sampleAllotForm #hzInfo_json").val(JSON.stringify(json));
}

//新增调拨盒子
function addHzInfo(){
    var ccdw = $("#sampleAllotForm #dcccdw").val();
    var bx = $("#sampleAllotForm #dcbx").val();
    var ct = $("#sampleAllotForm #dcct").val();
    if (ccdw == null || ccdw == "" || ccdw == undefined){
        $.alert("请先选择调出储存单位！");
        return
    }
    if (bx == null || bx == "" || bx == undefined){
        $.alert("请先选择调出冰箱！");
        return
    }
    if (ct == null || ct == "" || ct == undefined){
        $.alert("请先选择调出抽屉！");
        return
    }
    if (hzInfoList && hzInfoList.length >0) {
        var data = JSON.parse($("#sampleAllotForm #hzInfo_json").val());
            var sz={"index":data.length,"xh":data.length+1,"dbmxid":"","dchz":"","dchzmc":"","drhz":""};
            t_map.rows.push(sz);
            data.push(sz);
            $("#sampleAllotForm #hzInfo_json").val(JSON.stringify(data));
            $("#sampleAllotForm #tb_hzInfo_list").bootstrapTable('load',t_map);
    }else{
       $.error("当前抽屉无盒子可调拨!");
    }

}

//删除调拨盒子
function delHzInfo(index,event){
	var t_data = JSON.parse($("#sampleAllotForm #hzInfo_json").val());
	t_map.rows = []
	for(var i=0;i<t_data.length;i++){
		if(t_data[i].index == index){
			t_data.splice(i,1);
		}
	}
	var json=[];
	for(var i=0;i<t_data.length;i++){
		var sz={"index":i,"xh":i+1,"dbmxid":"","dchz":"","dchzmc":"","drhz":""};
		sz.xh=i+1;
        sz.dbmxid=t_data[i].dbmxid;
        sz.dchz=t_data[i].dchz;
        sz.dchzmc=t_data[i].dchzmc;
        sz.drhz=t_data[i].drhz;
		json.push(sz);
		t_map.rows.push(sz);
	}
	$("#sampleAllotForm #tb_hzInfo_list").bootstrapTable('load',t_map);
	$("#sampleAllotForm #hzInfo_json").val(JSON.stringify(json));
}
function viewHzInfo(index,hzid,event){
    tourl = ($("#sampleAllotForm #urlPrefix").val()?$("#sampleAllotForm #urlPrefix").val():($("#urlPrefix").val()?$("#urlPrefix").val():"")) + "/sampleAllot/pagedataSampleAllotListView";
    var url = tourl + "?dchz=" +hzid;
    $.showDialog(url,'详情查看',viewHzInfoConfig);
}

//查看弹框初始化
var viewHzInfoConfig = {
	width		: "1200px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
//删除所有盒子
function delAllHzInfo(){
    if(t_map.rows.length>0){
        t_map.rows = []
        var json=[]
        $("#sampleAllotForm #hzInfo_json").val(JSON.stringify(json));
        $("#sampleAllotForm #tb_hzInfo_list").bootstrapTable('load',t_map);
    }
}

//盒子下拉框格式化
function dchzInfoformat(value,row,index){
	var html="";
	if (hzInfoList && hzInfoList.length >0) {
		html +="<div id='hzInfodcdiv_"+index+"' name='hzInfodcdiv' style='width:100%;display:inline-block'>";
		html +=  "<select id='hzInfo_dc_"+index+"' name='hzInfo_dc_"+index+"' class='form-control chosen-select' onchange=\"checkdcHzInfo('"+index+"')\">";
		html += "<option value=''>请选择--</option>";
		for(var i=0;i<hzInfoList.length;i++){
			if(row.dchz==hzInfoList[i].csid){
				html += "<option value='"+hzInfoList[i].csid+"' selected >"+hzInfoList[i].wz + (hzInfoList[i].sbh?"("+hzInfoList[i].sbh+")":"") +"</option>";
				if (formAction == 'commonAudit'){
					return hzInfoList[i].wz + (hzInfoList[i].sbh?"("+hzInfoList[i].sbh+")":"");
				}
			}else{
				html += "<option value='"+hzInfoList[i].csid+"' >"+hzInfoList[i].wz + (hzInfoList[i].sbh?"("+hzInfoList[i].sbh+")":"")+"</option>";
			}
		}
		html += "</select></div>";
	}else{
		html += "<span  >"+value+"</span>";
	}
	return html;
}
//盒子下拉框格式化
function drhzInfoformat(value,row,index){

    var html="";
	html +="<span>"+ row.dchzmc +"("+row.dchzsbh+")"+"</span>";
	return html;
}

//操作格式化
function czformat(value,row,index){
	var html = "<span id='hzInfo_viewbtn' style='margin-left:5px;' class='btn btn-default' onclick=\"viewHzInfo('" + index + "','" + row.dchz + "',event)\" >查看样本</span>";
	if (formAction != 'commonAudit'){
		html += "<span id='hzInfo_delbtn' style='margin-left:5px;' class='btn btn-danger' onclick=\"delHzInfo('" + index + "',event)\" >删除</span>"
	}
	return html;
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
		if (zdmc == 'drhzbh') {
			t_map.rows[index].drhzbh = e.value;
		}
	}
}
//盒子改变事件
function checkdcHzInfo(index){

    t_map.rows[index].dchz = $("#sampleAllotForm #hzInfo_dc_" + index).find("option:selected").val();
    t_map.rows[index].dchzmc = $("#sampleAllotForm #hzInfo_dc_" + index).find("option:selected").text();
    $('#sampleAllotForm #tb_hzInfo_list').bootstrapTable('load',t_map);
}
//盒子改变事件
function checkdrHzInfo(index){
	var sbh = $("#sampleAllotForm #hzInfo_dr_" + index).find("option:selected").attr("sbh");
	sbh = sbh=='null'?'':sbh;
	t_map.rows[index].drhzbh = sbh;
    t_map.rows[index].drycfs = $("#sampleAllotForm #hzInfo_dr_" + index).find("option:selected").attr("ycfs");
    t_map.rows[index].drhz = $("#sampleAllotForm #hzInfo_dr_" + index).find("option:selected").val();
    t_map.rows[index].drhzmc = $("#sampleAllotForm #hzInfo_dr_" + index).find("option:selected").text();
    $('#sampleAllotForm #tb_hzInfo_list').bootstrapTable('load',t_map);
	if (Number(t_map.rows[index].drycfs)>0){
		$.alert("调入盒子存在样本，请切换调入盒子！");
	}
}
//调拨所有盒子
function allotAll(){
    var ccdw = $("#sampleAllotForm #dcccdw").val();
    var bx = $("#sampleAllotForm #dcbx").val();
    var ct = $("#sampleAllotForm #dcct").val();
    if (ccdw == null || ccdw == "" || ccdw == undefined){
        $.alert("请先选择调出储存单位！");
        return
    }
    if (bx == null || bx == "" || bx == undefined){
        $.alert("请先选择调出冰箱！");
        return
    }
    if (ct == null || ct == "" || ct == undefined){
        $.alert("请先选择调出抽屉！");
        return
    }
    if (hzInfoList != null && hzInfoList.length > 0){
        delAllHzInfo();
        var data = []
        t_map.rows = []
        for ( i = 0 ; i < hzInfoList.length ; i ++){
            var sz={"index":i,"xh":i+1,"dbmxid":"","dchz":hzInfoList[i].csid,"dchzmc":hzInfoList[i].wz+(hzInfoList[i].sbh?"("+hzInfoList[i].sbh+")":""),"drhz":""};
            t_map.rows.push(sz);
            data.push(sz);
        }
        $("#sampleAllotForm #hzInfo_json").val(JSON.stringify(data));
        $("#sampleAllotForm #tb_hzInfo_list").bootstrapTable('load',t_map);
    }else{
        $.error("当前抽屉无盒子可调拨!");
    }
}