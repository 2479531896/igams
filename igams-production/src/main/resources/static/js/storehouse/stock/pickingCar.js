var qxll="取消领料";
var shzt="";//当前单据审核状态
var t_map=[];
// 请购车显示字段
var llcColumnsArray = [
	{
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
        visible: false
    },{
        field: 'wlbm',
        title: '物料编码',
        titleTooltip:'物料编码',
        width: '8%',
        align: 'left',
        formatter:wlbmformat,
        visible: true,
        sortable: true,
    }, {
        field: 'wlmc',
        title: '物料名称',
        titleTooltip:'物料名称',
        width: '8%',
        align: 'left',
        visible: true
    }, {
        field: 'jldw',
        title: '单位',
        width: '3%',
        align: 'left',
        visible: true
    }, {
        field: 'lbmc',
        title: '类别',
        titleTooltip:'类别',
        width: '10%',
        align: 'left',
        visible: false
    }, {
        field: 'scs',
        title: '生产商',
        titleTooltip:'生产商',
        width: '8%',
        align: 'left',
        visible: true
    }, {
        field: 'wllbmc',
        title: '物料类别',
        titleTooltip:'物料类别',
        width: '10%',
        align: 'left',
        visible: true
    }, {
        field: 'wlzlbmc',
        title: '物料子类别',
        titleTooltip:'物料子类别',
        width: '10%',
        align: 'left',
        visible: true
    }, {
        field: 'wlzlbtcmc',
        title: '子类别统称',
        titleTooltip:'子类别统称',
        width: '7%',
        align: 'left',
        visible: true
    }, {
        field: 'sl',
        title: '数量',
        titleTooltip:'数量',
        width: '7%',
        formatter:pickingCarForm_slformat,
        align: 'left',
        visible: true
    }, {
        field: 'ychh',
        title: '货号',
        titleTooltip:'货号',
        width: '20%',
        align: 'left',
        visible: false
    },{
        field: 'gg',
        title: '规格',
        titleTooltip:'规格',
        width: '5%',
        align: 'left',
        visible: true
    },{
        field: 'aqkc',
        title: '安全库存量',
        titleTooltip:'安全库存量',
        width: '10%',
        align: 'left',
        visible: false
    },{
        field: 'cz',
        title: '操作',
        width: '5%',
        align: 'left',
        formatter:czformat,
        visible: true
    }];
// 请购新增，提交，复制显示字段
var llmxColumnsArray = [
	{
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
        visible: false
    },{
        field: 'wlbm',
        title: '物料编码',
        titleTooltip:'物料编码',
        width: '8%',
        align: 'left',
        formatter:t_wlbmformat,
        visible: true,
        sortable: true,
    }, {
        field: 'wlmc',
        title: '物料名称',
        titleTooltip:'物料名称',
        width: '8%',
        align: 'left',
        visible: true
    }, {
        field: 'lbmc',
        title: '类别',
        titleTooltip:'类别',
        width: '10%',
        align: 'left',
        visible: false
    }, {
        field: 'scs',
        title: '生产商',
        titleTooltip:'生产商',
        width: '8%',
        align: 'left',
        visible: true
    }, {
        field: 'wllbmc',
        title: '物料类别',
        titleTooltip:'物料类别',
        width: '10%',
        align: 'left',
        visible: true
    }, {
        field: 'wlzlbmc',
        title: '物料子类别',
        titleTooltip:'物料子类别',
        width: '10%',
        align: 'left',
        visible: true
    }, {
        field: 'wlzlbtcmc',
        title: '子类别统称',
        titleTooltip:'子类别统称',
        width: '5%',
        align: 'left',
        visible: true
    }, {
        field: 'ychh',
        title: '货号',
        titleTooltip:'货号',
        width: '20%',
        align: 'left',
        visible: false
    },{
        field: 'gg',
        title: '规格',
        titleTooltip:'规格',
        width: '5%',
        align: 'left',
        visible: true
    },{
        field: 'jldw',
        title: '单位',
        titleTooltip:'单位',
        width: '4%',
        align: 'left',
        visible: true
    },{
        field: 'aqkc',
        title: '安全库存量',
        titleTooltip:'安全库存量',
        width: '10%',
        align: 'left',
        visible: false
    },{
        field: 'bz',
        title: '备注',
        width: '13%',
        align: 'left',
        formatter:bzformat,
        visible: true
    },{
        field: 'cz',
        title: '操作',
        width: '5%',
        align: 'left',
        formatter:czformat,
        visible: true
    }];
// 请购采购部审核显示字段
var qxgjxgColumnsArray = [
	{
		title: '序号',
		formatter: function (value, row, index) {
			return index+1;
		},
		titleTooltip:'序号',
		width: '3%',
        align: 'left',
        visible:true
	},{
	    field: 'wlbm',
	    title: '物料编码',
	    width: '8%',
	    align: 'left',
	    formatter:t_wlbmformat,
	    sortable: true,
	    visible: true
	},{
	    field: 'wlmc',
	    title: '物料名称',
	    width: '8%',
	    align: 'left',
	    sortable: true,
	    visible: true
	},{
	    field: 'gg',
	    title: '规格',
	    width: '8%',
	    align: 'left',
	    sortable: true,
	    visible: true
	},{
	    field: 'wlzlbmc',
	    title: '物料子类别',
	    width: '8%',
	    align: 'left',
	    sortable: true,
	    visible: true
	}, {
        field: 'wlzlbtcmc',
        title: '子类别统称',
        titleTooltip:'子类别统称',
        width: '6%',
        align: 'left',
        visible: true
    },{
	    field: 'jldw',
	    title: '单位',
	    width: '5%',
	    align: 'left',
	    sortable: true,
	    visible: true
	},{
	    field: 'bz',
	    title: '备注',
	    width: '13%',
	    align: 'left',
	    formatter:bzformat,
	    sortable: true,
	    visible: true
	},{
	    field: 'zt',
	    title: '状态',
	    width: '5%',
	    align: 'left',
	    formatter:ztformat,
	    sortable: true,
	    visible: true
	}]

var PickingCar_TableInit=function(){
	var url = $("#pickingCarForm #url").val();
	if($("#pickingCarForm #fwbj").val() != null && $("#pickingCarForm #fwbj").val() != ""){
		url = $("#pickingCarForm #fwbj").val()+url;
	}
	url=$('#pickingCarForm #urlPrefix').val()+url;
	if($("#pickingCarForm #requestName").val()!=null && $("#pickingCarForm #requestName").val()!=""){
		//判断当前url中是否已经存在参数，
		if(url.indexOf("?")>0){
			url=url+"&requestName="+$("#pickingCarForm #requestName").val();
		}else{
			url=url+"?requestName="+$("#pickingCarForm #requestName").val();
			
		}
	}
	var flag=$("#pickingCarForm #flag").val();
	var sortName=null;
	var uniqueId=null;
	var sortLastName=null;
	var columnsArray=null;
		sortName = "llc.lrsj";
		uniqueId = "wlid";
		sortLastName="llc.wlid";
		columnsArray = llcColumnsArray;
	
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#pickingCarForm #tb_list").bootstrapTable({
			url: url,         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#pickingCarForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: false,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: sortName,				//排序字段
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
            uniqueId: uniqueId,                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            paginationDetailHAlign:' hidden',
            columns: columnsArray,
            onLoadSuccess:function(map){
            	t_map=map;
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
             },
            rowStyle: function (row, index) {
                 //这里有5个取值代表5中颜色['active', 'success', 'info', 'warning', 'danger'];
                 var strclass = "";
                 if (row.zt == "15") {
                     strclass = 'danger';//还有一个active
                 }
                 else if (row.zt == "30") {
                     strclass = 'warning';
                 }
                 else if (row.zt == "80") {
                     strclass = 'success';
                 }
                 else {
                     return {};
                 }
                 return { classes: strclass }
             },
		});
		$("#pickingCarForm #tb_list").colResizable({
			liveDrag:true, 
			gripInnerHtml:"<div class='grip'></div>", 
			draggingClass:"dragging", 
			resizeMode:'fit', 
			postbackSafe:true,
			partialRefresh:true
		})
	};
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   // 页面大小
        	pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: sortLastName, // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		return getPickingSearchData(map);
	};
	return oTableInit;
}
/**
 * 组装列表查询条件(未使用)
 * @param map
 * @returns
 */
function getPickingSearchData(map){
	var cxtj=$("#pickingCarForm #cxtj").val();
	var cxnr=$.trim(jQuery('#pickingCarForm #cxnr').val());
	if(cxtj=="0"){
		map["wlbm"]=cxnr;
	}else if(cxtj=="1"){
		map["wlmc"]=cxnr;
	}else if(cxtj=="2"){
		map["scs"]=cxnr;
	}else if(cxtj=="3"){
		map["ychh"]=cxnr;
	}
	return map;
}
function searchPickingResult(isTurnBack){
	if(isTurnBack){
		$('#pickingCarForm #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#pickingCarForm #tb_list').bootstrapTable('refresh');
	}
}

/**
 * 状态格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function ztformat(value,row,index){
	var html="";

	return html;
}

function pickingCarForm_slformat(value,row,index){
	var qlsl="";
	var kcl="";
	if(row.qlsl!=null){
		qlsl=row.qlsl;
	}
	if(row.kcl!=null){
		kcl=row.kcl;
	}
	var html="";
	html ="<div id='qlsldiv_"+index+"' name='qlsldiv' isBeyond='false' style='background:darkcyan;'>";
	html += "<input id='qlsl_"+index+"' value='"+qlsl+"' name='dhsl_"+index+"' style='width:55%;border:1px solid #D5D5D5;' validate='{qlslNumber:true}' onblur=\"checkSum('"+index+"',this,\'qlsl\')\" autocomplete='off'></input>";
	html += "<span id='kcl_"+index+"' style='font-size:13px;width:45%;margin-left:3px;'>/"+kcl+"</span>";
	html += "</div>";
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
 * 失去焦点事件(修改列表参数)
 * @param id
 * @param e
 * @param zdmc
 * @returns
 */
function checkSum(index, e, zdmc) {
	if(t_map.rows.length > index){
		if (zdmc == 'qlsl') {
			var qlsl = e.value
			t_map.rows[index].qlsl = qlsl;
			if(!qlsl){
				qlsl = 0;
			}
			// 判断背景颜色
			var kcl = $("#pickingCarForm #kcl_"+index).text();
			kcl = kcl.substring(1,kcl.length);
			if(parseFloat(qlsl).toFixed(2) - parseFloat(kcl).toFixed(2) > 0){
				$("#pickingCarForm #qlsldiv_"+index).attr("style","background:orange;");
				$("#pickingCarForm #qlsldiv_"+index).attr("isBeyond","false");
			}else{
				$("#pickingCarForm #qlsldiv_"+index).attr("style","background:darkcyan;");
				$("#pickingCarForm #qlsldiv_"+index).attr("isBeyond","true");
			}
		} 
	}
}

/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function czformat(value,row,index){
	var html = "<span style='margin-left:5px;' class='btn btn-default' title='移出领料车' onclick=\"delPickingCar('" + index + "',event)\" >删除</span>";
	return html;
}


/**
 * 备注格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function bzformat(value,row,index){
	if(row.bz == null){
		row.bz = "" ;
	}
	var html="<input id='bz_"+index+"' name='bz_"+index+"' value='"+row.bz+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'bz\')\" autocomplete='off'></input>";
	return html;
}

/**
 * 物料编码格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function wlbmformat(value,row,index){
	var html="";
	if(row.wlbm!=null && row.wlbm!=''){
		html="<span>"+row.wlbm+"</span><button type='button' class='glyphicon glyphicon-chevron-up' style='font-weight:100;float:right;width:35px;height:30px;border-radius:5px;border:0.5px solid black;' onclick='showImg(\""+index+"\")'></button>";
	}
	return html;
}

/**
 * 物料编码格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function t_wlbmformat(value,row,index){
	var html = ""; 
	if($('#ac_tk').length > 0){
		html += "<a href='javascript:void(0);' onclick=\"queryByWlbm('"+row.wlid+"')\">"+row.wlbm+"</a>";
	}else{
		html += row.wlbm;
	}
	return html;
}
function queryByWlbm(wlid){
	var url=$("#pickingCarForm #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
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


function refuseAudit(qgmxid,index){
	var checkstatus=$("#pickingCarForm #sfjj"+qgmxid).prop("checked");
	var data=JSON.parse($("#pickingCarForm #llmx_json").val());
	for(var i=0;i<data.length;i++){
		if(data[i].qgmxid==qgmxid){
			if(checkstatus==true){
				data[i].zt='15';
			}else{
				data[i].zt='80';
			}
		}
	}
	$("#pickingCarForm #llmx_json").val(JSON.stringify(data));
}


/**
 * 删除操作(从领料车删除)
 * @param index
 * @param event
 * @returns
 */
function delPickingCar(index,event){
	var preJson = JSON.parse($("#pickingCarForm #llmx_json").val());
	preJson.splice(index,1);
	t_map.rows.splice(index,1);
	$("#pickingCarForm #tb_list").bootstrapTable('load',t_map);
	$("#pickingCarForm #llmx_json").val(JSON.stringify(preJson));
}

/**
 * 采购车数字加减
 * @param sfbj
 * @returns
 */
function addOrDelNum(sfbj){
	if(sfbj=='add'){
		count=count+1;
	}else{
		count=count-1;
	}
	$("#mater_formSearch #cg_num").text(count);
}

/**
 * 选择申请人
 * @returns
 */
function chooseSqr() {
	var url = $('#pickingCarForm #urlPrefix').val()+"/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择申请人', chooseQrrConfig);
}
var chooseQrrConfig = { 
	width : "800px",
	height : "500px",
	modalName	: "chooseSqrModal",
	formName	: "pickingCarForm",
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
					$('#pickingCarForm #sqry').val(sel_row[0].yhid);
					$('#pickingCarForm #sqrmc').val(sel_row[0].zsxm);
					$.closeModal(opts.modalName);
					//保存操作习惯
					$.ajax({ 
					    type:'post',  
					    url:$('#pickingCarForm #urlPrefix').val()+"/common/habit/commonModUserHabit",
					    cache: false,
					    data: {"dxid":sel_row[0].yhid,"access_token":$("#ac_tk").val()},  
					    dataType:'json', 
					    success:function(data){}
					});
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
 * 选择申请部门
 * @returns
 */
function chooseBm() {
	var url = $('#pickingCarForm #urlPrefix').val()+"/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择部门', chooseBmConfig);
}
var chooseBmConfig = {
	width : "800px",
	height : "500px",
	modalName	: "chooseBmModal",
	formName	: "pickingCarForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#listDepartmentForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				var sel_row = $('#listDepartmentForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					$('#pickingCarForm #sqbm').val(sel_row[0].jgid);
					$('#pickingCarForm #sqbmmc').val(sel_row[0].jgmc);
					$('#pickingCarForm #sqbmdm').val(sel_row[0].jgdm);
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
 * 根据物料名称模糊查询库存
 */
$('#pickingCarForm #addnr').typeahead({
    source : function(query, process) {
        return $.ajax({
            url : $('#pickingCarForm #urlPrefix').val()+'/storehouse/requisition/pagedataQueryWlxx',
            type : 'post',
            data : {
                "entire" : query,
                "access_token" : $("#ac_tk").val()
            },
            dataType : 'json',
            success : function(result) {
                var resultList = result.ckhwxxDtos
                        .map(function(item) {
                            var aItem = {
                                id : item.wlid,
                                name : item.wlbm+"-"+item.wlmc+"-"+item.scs+"-"+item.gg
                            };
                            return JSON.stringify(aItem);
                        });
                return process(resultList);
            }
        });
    },
    matcher : function(obj) {
        var item = JSON.parse(obj);
        return ~item.name.toLowerCase().indexOf(
                this.query.toLowerCase())
    },
    sorter : function(items) {
        var beginswith = [], caseSensitive = [], caseInsensitive = [], item;
        while (aItem = items.shift()) {
            var item = JSON.parse(aItem);
            if (!item.name.toLowerCase().indexOf(
                    this.query.toLowerCase()))
                beginswith.push(JSON.stringify(item));
            else if (~item.name.indexOf(this.query))
                caseSensitive.push(JSON.stringify(item));
            else
                caseInsensitive.push(JSON.stringify(item));
        }
        return beginswith.concat(caseSensitive,
                caseInsensitive)
    },
    highlighter : function(obj) {
        var item = JSON.parse(obj);
        var query = this.query.replace(
                /[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&')
        return item.name.replace(new RegExp('(' + query
                + ')', 'ig'), function($1, match) {
            return '<strong>' + match + '</strong>'
        })
    },
    updater : function(obj) {
        var item = JSON.parse(obj);
        $('#pickingCarForm #addwlid').attr('value', item.id);
        return item.name;
    }
});

/**
 * 回车键添加物料至请购车
 * @param event
 * @returns
 */
var addkeydown = $("#addnr").bind("keydown",function(event){
	var wlid=$('#pickingCarForm #addwlid').val();
	if (event.keyCode == "13") {
		//判断新增输入框是否有内容
		if(!$('#pickingCarForm #addnr').val()){
			return false;
		}
		setTimeout(function(){ if(wlid != null && wlid != ''){
			$.ajax({ 
			    type:'post',  
		    		url:$('#pickingCarForm #urlPrefix').val()+"/storehouse/requisition/pagedataQueryWlxxByWlid", 
			    cache: false,
			    data: {"wlid":wlid,"access_token":$("#ac_tk").val()},  
			    dataType:'json', 
			    success:function(data){
			    	//返回值
			    	//返回值
			    	if(data.ckhwxxDto!=null && data.ckhwxxDto!=''){
			    		//kcbj=1 有库存，kcbj=0 库存未0
			    		if(data.ckhwxxDto.kcbj!='1'){
			    			$.confirm("该物料库存不足！");
			    		}else{
				    		var json=JSON.parse($("#pickingCarForm #llmx_json").val());
				    		if(t_map.rows==null || t_map.rows ==""){
				    			t_map.rows=[];
				    		}
				    		t_map.rows.push(data.ckhwxxDto);
				    		var sz={"index":'',"wlid":'',"qlsl":'',"xmdl":'',"xmbm":''};
				    		sz.index=json.length;
				    		sz.wlid=data.ckhwxxDto.wlid;
				    		sz.qlsl=data.ckhwxxDto.qlsl;
				    		sz.xmdl=data.ckhwxxDto.xmdl;
				    		sz.xmbm=data.ckhwxxDto.xmbm;
				    		json.push(sz);
				    		$("#pickingCarForm #llmx_json").val(JSON.stringify(json));
				    		$("#pickingCarForm #tb_list").bootstrapTable('load',t_map);
				    		$('#pickingCarForm #addwlid').val("");
				    		$('#pickingCarForm #addnr').val("");
			    		}
			    	}else{
			    		$.confirm("该物料不存在!");
			    	}
			    }
			});
		}else{
			var addnr = $('#pickingCarForm #addnr').val();
			if(addnr != null && addnr != ''){
				$.confirm("请选择物料信息!");
			}
		}}, '200')
	}
})
/**
 * 选择抄送人
 * @returns
 */
function chooseCsr(){
	var url = $('#pickingCarForm #urlPrefix').val()+"/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择申请人', chooseCsrConfig);
}
var chooseCsrConfig = {
	width : "800px",
	height : "500px",
	modalName	: "chooseCsrModal",
	formName	: "ajaxForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var $this = this;
				var opts = $this["options"]||{};
				var data = $('#taskListFzrForm #xzrys').val();//获取选择行数据
				if(data != null){
					json = data;
					var jsonStr = eval('('+json+')');
					for (var i = 0; i < jsonStr.length; i++) {
						$("#pickingCarForm  #csrs").tagsinput('add', jsonStr[i]);
					}
				}
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

function btnBind(){
	var sel_xmdl = $("#pickingCarForm #xmdl");
	var sel_xmbm =	$("#pickingCarForm #xmbm");
	//目大类下拉框事件
	sel_xmdl.unbind("change").change(function(){
		xmdlEvent();
	});
	//项目编码
	sel_xmbm.unbind("change").change(function(){
		xmbmEvent();
	});
	// 审核状态初始化
	shzt=$("#pickingCarForm #shzt").val();
	if(shzt=='10'){
		qxqg="拒绝审批";
	}
	// 默认人初始化
	getMrsqr();
	// 初始化明细json字段
	setTimeout(function() {
		setLlmx_json();
    	}, 1000);
}
/**
 * 项目编码下拉框事件
 * @returns
 */
function xmbmEvent(){
	var xmbm = $("#pickingCarForm #xmbm").val();
	if(xmbm == null || xmbm == "")
		return;
	var xmbmdm=$("#pickingCarForm #"+xmbm).attr("csdm");
	var xmmc=$("#pickingCarForm #"+xmbm).attr("csmc");
	$("#pickingCarForm #xmbmdm").val(xmbmdm);
	$("#pickingCarForm #xmmc").val(xmmc);
}
/**
 * 项目大类下拉框事件
 */
function xmdlEvent(){
	var xmdl = $("#pickingCarForm #xmdl").val();
	var xmdldm = $("#"+xmdl).attr("csdm");
	$("#pickingCarForm #xmdldm").val(xmdldm);
	if(xmdl == null || xmdl==""){
		var zlbHtml = "";
		zlbHtml += "<option value=''>--请选择--</option>";
		$("#pickingCarForm #xmbm").empty();
    	$("#pickingCarForm #xmbm").append(zlbHtml);
		$("#pickingCarForm #xmbm").trigger("chosen:updated");
		return;
	}
	var url="/systemmain/data/ansyGetJcsjList";
	if($("#pickingCarForm #fwbj").val()!=null && $("#pickingCarForm #fwbj").val()!=""){
		url="/ws/data/ansyGetJcsjList";
	}
	url = $('#pickingCarForm #urlPrefix').val()+url
	$.ajax({ 
	    type:'post',  
	    url:url, 
	    cache: false,
	    data: {"fcsid":xmdl,"access_token":$("#ac_tk").val()},  
	    dataType:'json', 
	    success:function(data){
	    	if(data != null && data.length != 0){
	    		var zlbHtml = "";
	    		zlbHtml += "<option value=''>--请选择--</option>";
            	$.each(data,function(i){   		
            		zlbHtml += "<option value='" + data[i].csid + "' id='"+data[i].csid+"' csdm='"+data[i].csdm+"' csmc='"+data[i].csmc+"'>" + data[i].csmc + "</option>";
            	});
            	$("#pickingCarForm #xmbm").empty();
            	$("#pickingCarForm #xmbm").append(zlbHtml);
            	$("#pickingCarForm #xmbm").trigger("chosen:updated");
	    	}else{
	    		var zlbHtml = "";
	    		zlbHtml += "<option value=''>--请选择--</option>";
	    		$("#pickingCarForm #xmbm").empty();
            	$("#pickingCarForm #xmbm").append(zlbHtml);
            	$("#pickingCarForm #xmbm").trigger("chosen:updated");
	    	}
	    }
	});
}
/**
 * 添加默认申请人信息
 * @returns
 */
function getMrsqr(){
	var mrsqr=$("#pickingCarForm #sqry").val();
	var mrsqrmc=$("#pickingCarForm #sqrmc").val();
	var mrsqbm=$("#pickingCarForm #mrsqbm").val();
	var mrsqbmmc=$("#pickingCarForm #mrsqbmmc").val();
	var mrsqbmdm=$("#pickingCarForm #mrsqbmdm").val();
	$("#pickingCarForm #sqry").val(mrsqr);
	$("#pickingCarForm #sqrmc").val(mrsqrmc);
	$("#pickingCarForm #sqbm").val(mrsqbm);
	$("#pickingCarForm #sqbmmc").val(mrsqbmmc);
	$("#pickingCarForm #sqbmdm").val(mrsqbmdm);
}
/**
 * 初始化明细json字段
 * @returns
 */
function setLlmx_json(){
	var json=[];
	var data=$('#pickingCarForm #tb_list').bootstrapTable('getData');//获取选择行数据
	for(var i=0;i<data.length;i++){
		var sz={"index":i,"wlid":'',};
		sz.wlid=data[i].wlid;
		sz.cskz1=data[i].cskz1;
		sz.sl=data[i].sl;
		sz.wlbm=data[i].wlbm;
		sz.qwrq=data[i].qwrq;
		sz.bz=data[i].bz;
		sz.qxqg=data[i].qxqg;
		sz.qgmxid=data[i].qgmxid;
		sz.zt=data[i].zt;
		sz.glid=data[i].glid;
		sz.jg=data[i].jg;
		sz.sysl=data[i].sl;
		sz.fjids=$("#pickingCarForm #fj"+data[i].wlid+" input").val();
		json.push(sz);
	}
	$("#pickingCarForm #llmx_json").val(JSON.stringify(json));
}

/**
 * 监听用户粘贴事件(新增物料)
 * @param e
 * @returns
 */
var addpaste = $("#pickingCarForm #addnr").bind("paste",function(e){
	var wlbms=e.originalEvent.clipboardData.getData("text");
	var arr_wlbms=wlbms.replaceAll(" ","").split(/[(\r\n)\r\n]+/);
	var length=arr_wlbms.length;
	$.ajax({ 
	    type:'post',  
	    url:$('#pickingCarForm #urlPrefix').val()+"/production/materiel/pagedataAddMatersOnShopping",
	    cache: false,
	    data: {"wlbms":arr_wlbms.toString(),"access_token":$("#ac_tk").val()},  
	    dataType:'json', 
	    success:function(data){
	    	//返回值
	    	var bczwl="";//不存在的物料
	    	if(data.qgcglDtos!=null && data.qgcglDtos.length>0){
	    		var wshwl="";//未审核物料
	    		var czwlbm=[];//查询到的物料编码
	    		for(var i=0;i<data.qgcglDtos.length;i++){
	    			czwlbm.push(data.qgcglDtos[i].wlbm);
	    			if(data.qgcglDtos[i].wlzt!='80'){
	    				wshwl=wshwl+","+data.qgcglDtos[i].wlbm;
		    		}else{
			    			t_map.rows.push(data.qgcglDtos[i]);
			    			var json=JSON.parse($("#pickingCarForm #llmx_json").val());
				    		var sz={"index":'',"wlid":'',"cskz1":'',"sl":'',"qwrq":'',"bz":'',"wlbm":'',"qxqg":'','qgmxid':'','zt':'',"glid":'',"fjids":''};
				    		sz.index=json.length;
				    		sz.wlid=data.qgcglDtos[i].wlid;
				    		sz.wlbm=data.qgcglDtos[i].wlbm;
				    		sz.cskz1=data.qgcglDtos[i].cskz1;
				    		sz.zt='80';
				    		json.push(sz);
				    		$("#pickingCarForm #llmx_json").val(JSON.stringify(json));
				    		$("#pickingCarForm #tb_list").bootstrapTable('load',t_map);
				    		$('#pickingCarForm #addwlid').val("");
				    		$('#pickingCarForm #addnr').val("");
		    		}
	    		}
	    		for(var i=0;i<arr_wlbms.length;i++){
	    			if($.inArray(arr_wlbms[i],czwlbm)=="-1"){
	    				bczwl=bczwl+","+arr_wlbms[i];
	    			}
	    		}
	    		wshwl=wshwl.substring(1);
	    		bczwl=bczwl.substring(1);
	    		var errorStr="";
	    		if(bczwl==""){
	    			if(wshwl!=""){
	    				errorStr="物料编码为"+wshwl+"的物料未审核!";
	    			}
	    		}else{
	    			errorStr="物料编码为"+bczwl+"的物料不存在!";
	    			if(wshwl!=""){
	    				errorStr="物料编码为"+wshwl+"的物料未审核!";
	    			}
	    		}
	    		if(errorStr!=""){
	    			$.confirm(errorStr);
	    			$('#pickingCarForm #addwlid').val("");
		    		$('#pickingCarForm #addnr').val("");
	    		}
	    	}
	    }
	});
})


/**
 * 统计查询
 * @returns
 */
function selectStatistic(domId, item, zdmc){
	if(zdmc == "jg"){
		$.ajax({
			type : 'post',
			url : $('#pickingCarForm #urlPrefix').val() + "/purchase/statistic/pagedataStatisticPrice",
			cache : false,
			data : {
				"wlid" : item,
				"access_token" : $("#ac_tk").val()
			},
			dataType : 'json',
			success : function(data) {
				buildStatistic(domId, data);
			}
		});
	}else if(zdmc == "wlbm"){
		$.ajax({
			type : 'post',
			url : $('#pickingCarForm #urlPrefix').val() + "/contract/statistic/pagedataStatisticCycle",
			cache : false,
			data : {
				"wlid" : item,
				"access_token" : $("#ac_tk").val()
			},
			dataType : 'json',
			success : function(data) {
				buildStatistic(domId, data);
			}
		});
	}
}

/**
 * 点击显示文件上传
 * @returns
 */
function editfile(){
	$("#fileDiv").show();
	$("#file_btn").hide();
}

/**
 * 点击隐藏文件上传
 * @returns
 */
function cancelfile(){
	$("#fileDiv").hide();
	$("#file_btn").show();
}

/**
 * 点击图片预览
 * @param fjid
 * @param wjm
 * @returns
 */
function view(fjid,wjm){
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin,end);
	if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
		var url=$('#pickingCarForm #urlPrefix').val()+"/ws/sjxxpripreview/?fjid="+fjid
		$.showDialog(url,'图片预览',JPGMaterConfig);
	}else if(type.toLowerCase()==".pdf"){
		var url= $('#pickingCarForm #urlPrefix').val()+"/common/file/pdfPreview?fjid=" + fjid;
        $.showDialog(url,'文件预览',JPGMaterConfig);
	}else {
		$.alert("暂不支持其他文件的预览，敬请期待！");
	}
}
var JPGMaterConfig = {
	width		: "800px",
	offAtOnce	: true,  
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
/**
 * 点击文件下载
 * @param fjid
 * @returns
 */
function xz(fjid){
    jQuery('<form action="'+$('#pickingCarForm #urlPrefix').val()+'/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
            '<input type="text" name="fjid" value="'+fjid+'"/>' + 
            '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' + 
        '</form>')
    .appendTo('body').submit().remove();
}
/**
 * 点击删除文件
 * @param fjid
 * @param wjlj
 * @returns
 */
function del(fjid,wjlj){
	$.confirm('您确定要删除所选择的记录吗？',function(result){
		if(result){
			jQuery.ajaxSetup({async:false});
			var url= $('#pickingCarForm #urlPrefix').val()+"/common/file/delFile";
			jQuery.post(url,{fjid:fjid,wjlj:wjlj,"access_token":$("#ac_tk").val()},function(responseText){
				setTimeout(function(){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							$("#"+fjid).remove();
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
/**
 * 附件上传回调
 * @param fjid
 * @returns
 */
function displayUpInfo(fjid){
	if(!$("#pickingCarForm #fjids").val()){
		$("#pickingCarForm #fjids").val(fjid);
	}else{
		$("#pickingCarForm #fjids").val($("#pickingCarForm #fjids").val()+","+fjid);
	}
}

$(function(){
	//0.初始化fileinput
	var oFileInput = new FileInput();
	var stock_params=[];
	stock_params.prefix=$('#pickingCarForm #urlPrefix').val();
	oFileInput.Init("pickingCarForm","displayUpInfo",2,1,"pro_file",null,stock_params);
	//初始化列表
	var oTable=new PickingCar_TableInit();
	oTable.Init();
	btnBind();
	jQuery('#pickingCarForm .chosen-select').chosen({width: '100%'});
})