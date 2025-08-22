var xzbj = $("#editPickingForm #xzbj").val();
var t_map = [];
var t_index="";
//取样标记
var qybj = $("#editPickingForm #qybj").val();
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
        field: 'wlid',
        title: '货物id',
        titleTooltip:'货物id',
        width: '4%',
        align: 'left',
        visible: false
    }, {
        field: 'wlbm',
        title: '物料编码',
        titleTooltip:'物料编码',
        width: '4%',
        align: 'left',
        visible: true
    }, {
        field: 'wlmc',
        title: '物料名称',
        titleTooltip:'物料名称',
        width: '8%',
        align: 'left',
        visible: true
    }, {
		field: 'ckmc',
		title: '仓库',
		titleTooltip:'仓库',
		width: '4%',
		align: 'left',
		visible: true
	},{
        field: 'gg',
        title: '规格',
        titleTooltip:'规格',
        width: '8%',
        align: 'left',
        visible: true
    }, {
        field: 'jldw',
        title: '单位',
        titleTooltip:'单位',
        width: '3%',
        align: 'left',
        visible: true
    }, {
        field: 'kcl',
        title: '库存数',
        titleTooltip:'库存数',
        width: '5%',
        align: 'left',
        visible: true
    }, {
	// 	field: 'slzs',
	// 	title: '实领总数',
	// 	titleTooltip:'实领总数',
	// 	width: '5%',
	// 	align: 'left',
	// 	visible: true
	// }, {
	// 	field: 'qlzs',
	// 	title: '请领总数',
	// 	titleTooltip:'请领总数',
	// 	width: '5%',
	// 	align: 'left',
	// 	visible: true
	// }, {
        field: 'qlsl',
        title: '请领数量',
        titleTooltip:'请领数量',
        width: '6%',
        align: 'left',
        formatter:editPickingForm_qlslformat,
        visible: true
    }, {
        field: 'xmdl',
        title: '项目大类',
        titleTooltip:'项目大类',
        width: '8%',
        align: 'left',
        formatter:editPickingForm_xmdlformat,
        visible: true
    }, {
        field: 'xmbm',
        title: '项目编码',
        titleTooltip:'项目编码',
        width: '6%',
        align: 'left',
        formatter:editPickingForm_xmbmformat,
        visible: true
    }, {
		field: 'cplx',
		title: '产品类型',
		titleTooltip:'产品类型',
		width: '6%',
		align: 'left',
		formatter:cplxformat,
		visible: $("#editPickingForm #lllx").val()=='3'?true:false
	},
	{
		field: 'bz',
		title: '备注',
		titleTooltip:'备注',
		width: '6%',
		align: 'left',
		formatter:editPickingForm_bzformat,
		visible: true
	},
	{
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
        $('#editPickingForm #llmxtb_list').bootstrapTable({
            url: $('#editPickingForm #urlPrefix').val()+$('#editPickingForm #url').val()+"?copyflg="+$('#editPickingForm #copyflg').val(),         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#editPickingForm #toolbar',                //工具按钮用哪个容器
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
            uniqueId: "wlid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
            onLoadSuccess: function (map) {
            	t_map = map;
            	if(t_map.rows!=null){          	
            		// 初始化llmx_json
            		var json = [];
            		for (var i = 0; i < t_map.rows.length; i++) {
            			var sz = {"hwllid":'',"wlid":'',"qlsl":'',"xmdl":'',"xmbm":'',"kqls":'',"qlyds":'',"cplx":'',"lbcskz1":'',"hwllmx_json":''};
    					sz.hwllid = t_map.rows[i].hwllid;
    					sz.wlid = t_map.rows[i].wlid;
    					sz.qlsl = t_map.rows[i].qlsl;
    					sz.xmdl = t_map.rows[i].xmdl;
    					sz.xmbm = t_map.rows[i].xmbm;
    					sz.kqls = t_map.rows[i].kqls;
    					sz.qlyds = t_map.rows[i].qlyds;
    					sz.cplx = t_map.rows[i].cplx;
    					sz.lbcskz1 = t_map.rows[i].lbcskz1;
    					if(t_map.hwllmxDtos!=null){
    						if(t_map.hwllmxDtos.length>0){
        						var json_mx = [];
        						for (var j = 0; j < t_map.hwllmxDtos.length; j++) {
        							if(t_map.rows[i].hwllid == t_map.hwllmxDtos[j].hwllid){
        								var sz_mx = {"hwid":'',"wlid":'',"qlsl":'',"qqls":'',"llmxid":''};        						
                						sz_mx.hwid=t_map.hwllmxDtos[j].hwid;
                						sz_mx.wlid=t_map.hwllmxDtos[j].wlid;
                						sz_mx.qlsl=t_map.hwllmxDtos[j].qlsl;
                						sz_mx.qqls=t_map.hwllmxDtos[j].qlsl;
                						sz_mx.llmxid=t_map.hwllmxDtos[j].llmxid;
                						json_mx.push(sz_mx);
        							}
        						}
        						sz.hwllmx_json = JSON.stringify(json_mx);
        						t_map.rows[i].hwllmx_json = JSON.stringify(json_mx);
    						}
    					}
    					json.push(sz);
    				}
            		$("#editPickingForm #llmx_json").val(JSON.stringify(json));
            	}       		
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
            llid: $("#editPickingForm #llid").val(),
			sfzy: $("#editPickingForm #sfzy").val(),
            //搜索框使用
            //search:params.search
        };
    	return map;
    };
    return oTableInit;
};

/**
 * 项目大类格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function editPickingForm_xmdlformat(value,row,index){
	var xmdls = $("#editPickingForm #xmdllist").val();
	var xmdllist = JSON.parse(xmdls);
	var xmdl=row.xmdl;
	var html="";
	html +="<div id='xmdldiv_"+index+"' name='xmdldiv' style='width:85%;display:inline-block'>";
	html += "<select id='xmdl_"+index+"' name='xmdl_"+index+"' class='form-control chosen-select' validate='{required:true}' onchange=\"checkXmdlXmbm('"+index+"',this,\'xmdl\')\">";
	html += "<option value=''>--请选择--</option>";
	for(var i = 0; i < xmdllist.length; i++) {
		html += "<option id='"+xmdllist[i].csid+"' value='"+xmdllist[i].csid+"'";
		if(xmdl!=null && xmdl!=""){
			if(xmdl==xmdllist[i].csid){
				html += "selected"
			}
		}		
		html += ">"+xmdllist[i].csdm+"--"+xmdllist[i].csmc+"</option>";
	}
	html +="</select></div><div style='width:10%;float: right;margin-top: 3px;'><span class='glyphicon glyphicon-pencil' style='font-size:13px;color:#AAAAAA;cursor:pointer;' title='复制当前日期并替换所有'  onclick=\"copyXmdl('"+index+"')\" autocomplete='off'><span></div>";
	return html;
}
/**
 * 选择营销合同列表
 * @returns
 */
function chooseHtbh(){
	var url=$('#editPickingForm #urlPrefix').val() + "/marketingContract/marketingContract/pagedataChooseMarketingContract?access_token=" + $("#ac_tk").val();
	$.showDialog(url,'选择营销合同',addHtbhConfig);
}
var addHtbhConfig = {
	width		: "1000px",
	modalName	:"addHtbhModal",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row = $('#chooseMarketingContractForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){

					$('#editPickingForm #htid').val(sel_row[0].htid);
					$('#editPickingForm #htdh').val(sel_row[0].htbh);
					$('#editPickingForm #khjc').val(sel_row[0].khjc);
					$('#editPickingForm #khid').val(sel_row[0].khid);
					$('#editPickingForm #zd').val(sel_row[0].zd);
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
 * 复制当前项目大类并替换全部
 * @param wlid
 * @returns
 */
function copyXmdl(index){
	let xmdl=$("#editPickingForm #xmdl_"+index).val();
	if(xmdl==null || xmdl==""){
		$.confirm("请先选择项目大类！");
	}else{
		for(let i=0;i<t_map.rows.length;i++){
			$("#editPickingForm #xmdl_"+i).val(xmdl);
		}
	}
	var data=JSON.parse($("#editPickingForm #llmx_json").val());
	if(data!=null && data.length>0){
		for(let j=0;j<data.length;j++){
			data[j].xmdl=xmdl;
			t_map.rows[j].xmdl=xmdl;
		}
	}
	if(xmdl == null || xmdl==""){
		var zlbHtml = "";
		zlbHtml += "<option value=''>--请选择--</option>";
		for(let i=0;i<t_map.rows.length;i++){
			$("#editPickingForm #xmbm_"+i).empty();
			$("#editPickingForm #xmbm_"+i).append(zlbHtml);
			$("#editPickingForm #xmbm_"+i).trigger("chosen:updated");
		}
		return;
	}
	var url="/systemmain/data/ansyGetJcsjList";
	url = $('#editPickingForm #urlPrefix').val()+url;
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
					zlbHtml += "<option value='" + data[i].csid + "' id='"+data[i].csid+"' csdm='"+data[i].csdm+"' csmc='"+data[i].csmc+"'>" +data[i].csdm +"--"+ data[i].csmc + "</option>";
				});
				for(let i=0;i<t_map.rows.length;i++){
					$("#editPickingForm #xmbm_"+i).empty();
					$("#editPickingForm #xmbm_"+i).append(zlbHtml);
					$("#editPickingForm #xmbm_"+i).trigger("chosen:updated");
				}
			}else{
				var zlbHtml = "";
				zlbHtml += "<option value=''>--请选择--</option>";
				$("#editPickingForm #xmbm_"+index).empty();
				$("#editPickingForm #xmbm_"+index).append(zlbHtml);
				$("#editPickingForm #xmbm_"+index).trigger("chosen:updated");
			}
		}
	});
	$("#editPickingForm #llmx_json").val(JSON.stringify(data));
}

/**
 * 复制当前项目编码并替换全部
 * @param wlid
 * @returns
 */
function copyXmbm(index){
	let value=$("#editPickingForm #xmdl_"+index).val();
	for(let i=0;i<t_map.rows.length;i++){
		let xmdl = $("#editPickingForm #xmdl_"+i).val();
		if (!xmdl){
			$.confirm("项目大类有为空的项！");
			return
		}
		if (xmdl != value){
			$.confirm("请先进行项目大类的复制！");
			return
		}
	}
	let xmbm=$("#editPickingForm #xmbm_"+index).val();
	if(xmbm==null || xmbm==""){
		$.confirm("请先选择项目编码！");
	}else{
		for(let i=0;i<t_map.rows.length;i++){
			$("#editPickingForm #xmbm_"+i).val(xmbm);
		}
	}
	var data=JSON.parse($("#editPickingForm #llmx_json").val());
	if(data!=null && data.length>0){
		for(let j=0;j<data.length;j++){
			data[j].xmbm=xmbm;
			t_map.rows[j].xmbm=xmbm;
		}
	}
	$("#editPickingForm #llmx_json").val(JSON.stringify(data));
}
/**
 * 项目编码格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function editPickingForm_xmbmformat(value,row,index){
	var xmbms = $("#editPickingForm #xmbmlist").val();
	var xmbmlist = JSON.parse(xmbms);
	var xmbm=row.xmbm;
	var html="";
	html +="<div id='xmbmdiv_"+index+"' name='xmbmdiv' style='width:85%;display:inline-block'>";
	html += "<select id='xmbm_"+index+"' name='xmbm_"+index+"' class='form-control chosen-select' validate='{required:true}' onchange=\"checkXmdlXmbm('"+index+"',this,\'xmbm\')\">";
	html += "<option value=''>--请选择--</option>";
	for(var i = 0; i < xmbmlist.length; i++) {
		html += "<option id='"+xmbmlist[i].csid+"' value='"+xmbmlist[i].csid+"'";
		if(xmbm!=null && xmbm!=""){
			if(xmbm==xmbmlist[i].csid){
				html += "selected"
			}
		}		
		html += ">"+xmbmlist[i].csdm+"--"+xmbmlist[i].csmc+"</option>";
	}
	html +="</select></div><div style='width:10%;float: right;margin-top: 3px;'><span class='glyphicon glyphicon-pencil' style='font-size:13px;color:#AAAAAA;cursor:pointer;' title='复制当前日期并替换所有'  onclick=\"copyXmbm('"+index+"')\" autocomplete='off'><span></div>";
	return html;
}
/**
 * 产品类型格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function cplxformat(value,row,index){
	var html="";
	if ($("#editPickingForm #lllx").val() == '3'){
		html +="<div id='cplxdiv_"+index+"' name='cplxdiv' style='width:85%;display:inline-block'>";
		html +=  "<select id='cplx_"+index+"' name='cplx_"+index+"' class='form-control'  onchange=\"checkCplx('"+index+"',this,\'cplx\')\">";
		html += "<option value=''>请选择--</option>";
		var val = JSON.parse($("#editPickingForm #cplxlist").val());
		if(val){
			for(var i=0;i<val.length;i++){
				if(row.cplx){
					if(row.cplx==val[i].csid){
						html += "<option value='"+val[i].csid+"' selected >"+val[i].csmc+"</option>";
					}else{
						html += "<option value='"+val[i].csid+"' >"+val[i].csmc+"</option>";
					}
				}else{
					html += "<option value='"+val[i].csid+"' >"+val[i].csmc+"</option>";
				}
			}
		}
		html += "</select></div><div style='width:10%;float: right;margin-top: 3px;'><span class='glyphicon glyphicon-pencil' style='font-size:13px;color:#AAAAAA;cursor:pointer;' title='复制当前产品类型并替换所有'  onclick=\"copyCplx('"+index+"')\" autocomplete='off'><span></div>";
	}else {
		html +="--";
	}
	return html;
}
/**
 * 复制当前日期并替换全部
 * @param wlid
 * @returns
 */
function copyCplx(index){
	var cplx=$("#editPickingForm #cplx_"+index).val();
	if(cplx==null || cplx==""){
		$.confirm("请先选择产品类型！");
	}else{
		for(var i=0;i<t_map.rows.length;i++){
			$("#editPickingForm #cplx_"+i).val(cplx);
		}
	}
	var data=JSON.parse($("#editPickingForm #llmx_json").val());
	if(data!=null && data.length>0){
		for(var j=0;j<data.length;j++){
			data[j].cplx=cplx;
			t_map.rows[j].cplx=cplx;
		}
	}
	$("#editPickingForm #llmx_json").val(JSON.stringify(data));
}

function checkXmdlXmbm(index, e, zdmc){
	if(zdmc=="xmdl"){
		t_map.rows[index].xmdl = e.value;
		var xmdl = $("#editPickingForm #xmdl_"+index).val();
		if(xmdl == null || xmdl==""){
			var zlbHtml = "";
			zlbHtml += "<option value=''>--请选择--</option>";
			$("#editPickingForm #xmbm_"+index).empty();
	    	$("#editPickingForm #xmbm_"+index).append(zlbHtml);
			$("#editPickingForm #xmbm_"+index).trigger("chosen:updated");
			return;
		}
		var url="/systemmain/data/ansyGetJcsjList";
		url = $('#editPickingForm #urlPrefix').val()+url;
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
	            		zlbHtml += "<option value='" + data[i].csid + "' id='"+data[i].csid+"' csdm='"+data[i].csdm+"' csmc='"+data[i].csmc+"'>" +data[i].csdm +"--"+ data[i].csmc + "</option>";
	            	});
	            	$("#editPickingForm #xmbm_"+index).empty();
	            	$("#editPickingForm #xmbm_"+index).append(zlbHtml);
	            	$("#editPickingForm #xmbm_"+index).trigger("chosen:updated");
		    	}else{
		    		var zlbHtml = "";
		    		zlbHtml += "<option value=''>--请选择--</option>";
		    		$("#editPickingForm #xmbm_"+index).empty();
	            	$("#editPickingForm #xmbm_"+index).append(zlbHtml);
	            	$("#editPickingForm #xmbm_"+index).trigger("chosen:updated");
		    	}
		    }
		});
	}
	if(zdmc=="xmbm"){
		t_map.rows[index].xmbm = e.value;
	}
	$("#editPickingForm #llmx_json").val(JSON.stringify(t_map.rows));
}

/**
 * 请领数量格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function editPickingForm_qlslformat(value,row,index){
	var kqls="";
	var qlsl="";
	if(row.kqls!=null){
		kqls=row.kqls;	
	}
	if(row.klsl!=null){
		kqls=row.klsl;
	}
	if(qybj=="1"){
		//如果是取样申请的话，可领数量为请领数量
		kqls=row.qlsl;
	}
	if(row.qlsl!=null){
		qlsl=row.qlsl;
	}
	//获取当前领料状态
	var zt = $("#editPickingForm #zt").val();
	if(zt!=null){
		if(zt=="00" || zt=="15"){
			if(row.qlsl!=null){
				kqls = parseFloat(kqls) - parseFloat(qlsl);
			}
		}
	}
	
	
	var html="";
	html ="<div id='qlsldiv_"+index+"' name='qlsldiv' isBeyond='false' style='background:darkcyan;'>";
	if(qlsl!=null && qlsl!=""){
		html += "<input id='qlsl_"+index+"' autocomplete='off' value='"+qlsl+"' name='qlsl_"+index+"' style='width:55%;border:1px solid #D5D5D5;' validate='{qlslNumber:true}' ";
		if(qybj=="1"){
			html += "disabled='disabled'";
		}else{
			html += "onblur=\"checkqlslSum('"+index+"',this,\'qlsl\')\" ";
		}
		html += "></input>";
	}else{
		html += "<input id='qlsl_"+index+"' autocomplete='off' value='' name='qlsl_"+index+"' style='width:55%;border:1px solid #D5D5D5;' validate='{qlslNumber:true}' ";
		if(qybj=="1"){
			html += "disabled='disabled'";
		}else{
			html += "onblur=\"checkqlslSum('"+index+"',this,\'qlsl\')\" ";
		}
		html += "></input>";	
		// row.qlsl=kqls;
	}
	if(row.copyflg=='1'){
		var sl=parseFloat(row.kcl) - parseFloat(row.yds);
		html += "/<span id='kqls_"+index+"' style='font-size:13px;width:45%;margin-left:3px;'>"+sl+"</span>";
	}else{
		html += "/<span id='kqls_"+index+"' style='font-size:13px;width:45%;margin-left:3px;'>"+kqls+"</span>";
	}
	html += "</div>";
	return html;
}

/**
 * 备注格式化
 */
function editPickingForm_bzformat(value,row,index){
	var html="";
	if (!row.hwllxxbz){
		row.hwllxxbz="";
	}
	html ="<div id='bzdiv_"+index+"' name='bzdiv'>";
	html += "<input id='bz_"+index+"' value='"+row.hwllxxbz+"' name='bz_"+index+"' onblur=\"checkBz('"+index+"',this,\'bz\')\"  validate='{stringMaxLength:64}'></input>";
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
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function editPickingCarForm_czformat(value,row,index){
	var html="";
	let formAction = $('#editPickingForm #formAction').val()
	if("1"!=qybj){
		html = "<span style='margin-left:5px;height:24px !important;' class='btn btn-info' title='选取追溯号' onclick=\"chooseZsh('" + index + "',event)\" >详细</span>";
		if("/storehouse/requisition/saveAdvancedmodRequisition" != formAction ){
			html = html+"<span style='margin-left:5px;height:24px !important;' class='btn btn-danger' title='删除物料信息' onclick=\"delWlDetail('" + index + "','"+row.qgid+"','"+row.ckhwid+"',event)\" >删除</span>";
		}
	}
	return html;
}
/**
 * 监听单据号标签移除事件
 */
var tagsRemoved = $('#editPickingForm #zldh').on('beforeItemRemove', function(event) {
	$('#editPickingForm #sczlid').val('')
	for (var i = 0; i <t_map.rows.length; i++) {
		t_map.rows[i].hwllxxbz = '';
	}
	$("#editPickingForm #llmxtb_list").bootstrapTable('load',t_map);
});
/**
 * 监听单据号标签移除事件
 */
var tagsRemoved = $('#editPickingForm #djhs').on('beforeItemRemove', function(event) {
	// 获取未更改请购单和明细信息
	var preJson = [];
	if($("#editPickingForm #llmx_json").val()){
		preJson = JSON.parse($("#editPickingForm #llmx_json").val());
		// 删除明细并刷新列表
		var qgid = event.item.qgid;
		let ckhwid = "";
		let pops = 0;
		for (let i = 0; i < preJson.length; i++) {
			if(preJson[i].qgid == qgid){
				ckhwid = preJson[i].ckhwid;
				preJson.splice(i,1);
				i--;
				for (let j = 0; j < preJson.length; j++) {
					if(preJson[j].ckhwid == ckhwid){
						pops += 1;
					}
				}
				if (pops == 0){
					for (var j = t_map.rows.length-1; j >= 0 ; j--) {
						if(t_map.rows[j].ckhwid == ckhwid){
							t_map.rows.splice(j,1);
						}
					}
				}
				pops = 0;
			}
		}
		$("#editPickingForm #llmx_json").val(JSON.stringify(preJson));
		$("#editPickingForm #llmxtb_list").bootstrapTable('load',t_map);
	}
});

/**
 * 监听单据号标签移除事件
 */
var tagsRemoved = $('#editPickingForm #xqdhs').on('beforeItemRemove', function(event) {
	// 获取未更改请购单和明细信息
	var preJson = [];
	if($("#editPickingForm #llmx_json").val()){
		preJson = JSON.parse($("#editPickingForm #llmx_json").val());
		// 删除明细并刷新列表
		var xqjhid = event.item.xqjhid;
		let ckhwid = "";
		let pops = 0;
		for (let i = 0; i < preJson.length; i++) {
			if(preJson[i].xqjhid == xqjhid){
				ckhwid = preJson[i].ckhwid;
				preJson.splice(i,1);
				i--;
				for (let j = 0; j < preJson.length; j++) {
					if(preJson[j].wlid == ckhwid){
						pops += 1;
					}
				}
				if (pops == 0){
					for (var j = t_map.rows.length-1; j >= 0 ; j--) {
						if(t_map.rows[j].ckhwid == ckhwid){
							t_map.rows.splice(j,1);
						}
					}
				}
				pops = 0;
			}
		}
		$("#editPickingForm #llmx_json").val(JSON.stringify(preJson));
		$("#editPickingForm #llmxtb_list").bootstrapTable('load',t_map);
	}
});
/**
 * 监听标签点击事件
 */
var tagScClick = $("#editPickingForm #sc").on('click','.label-info',function(e){
	event.stopPropagation();
	var url = $("#editPickingForm input[name='urlPrefix']").val() +"/progress/produce/pagedataProduceForll?sczlid="+$("#editPickingForm #sczlid").val()+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url, '指令单信息', viewProduceConfig);
});

var viewProduceConfig = {
	width		: "1600px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
/**
 * 监听标签点击事件
 */
var tagXqClick = $("#editPickingForm #xq").on('click','.label-info',function(e){
	event.stopPropagation();
	var url = $("#editPickingForm input[name='urlPrefix']").val() + "/storehouse/receiveMateriel/pagedataChooseListProgressInfo?access_token=" + $("#ac_tk").val() + "&cpxqid=" + e.currentTarget.dataset.logo;
	$.showDialog(url, '选择需求明细', chooseEditXqmxConfig);
});
var chooseEditXqmxConfig = {
	width : "1000px",
	modalName	: "chooseXqmxModal",
	formName	: "chooseXqmxForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#chooseXqmxForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				// 获取未更改请购单和明细信息
				var preJson = [];
				if($("#editPickingForm #llmx_json").val()){
					preJson = JSON.parse($("#editPickingForm #llmx_json").val());
				}
				// 获取选中明细并保存
				if($("#chooseXqmxForm input[name='checkXqmx']:checked").length == 0){
					$.alert("未选中请购明细信息！");
					return false;
				}
				var ids="";
				$("#chooseXqmxForm input[name='checkXqmx']").each(function() {
					var isAdd = true;
					for (var j = 0; j < preJson.length; j++) {
						if($(this).is(":checked")){
							if(preJson[j].xqjhmxid == this.dataset.xqjhmxid){
								isAdd = false;
								break;
							}
						}
					}
					if(isAdd){
						if($(this).is(":checked")){
							ids = ids + ","+ this.dataset.xqjhmxid;
						}
					}
				});
				if(ids.length > 0){
					ids = ids.substr(1);
					// 查询信息并更新到页面
					$.post($('#editPickingForm #urlPrefix').val() + "/storehouse/receiveMateriel/pagedataProgressListInfo",{"ids":ids,"xzbj":xzbj,"lllb":"1","access_token":$("#ac_tk").val()},function(data){
						var dtoList = data.dtoList;
						if(dtoList != null && dtoList.length > 0){
							// 更新页面列表(新增)
							for (var i = 0; i < dtoList.length; i++) {
								if(i == 0){
									if(!t_map.rows){
										t_map.rows= [];
									}
								}
								if(dtoList[i].klsl > 0 && dtoList[i].klsl != null && dtoList[i].klsl != ''){
									var isRepeat = false;
									for(var repeatIndex = 0; repeatIndex < t_map.rows.length; repeatIndex++) {
										if(t_map.rows[repeatIndex].ckhwid==dtoList[i].ckhwid){
											isRepeat = true;
										}
									}
									if(!isRepeat){
										t_map.rows.push(dtoList[i]);
										var sz = {"xqjhid":'',"xqjhmxid":'',"ckhwid":''};
										sz.xqjhid = dtoList[i].xqjhid;
										sz.xqjhmxid = dtoList[i].xqjhmxid;
										sz.ckhwid = dtoList[i].ckhwid;
										preJson.push(sz);
									}
								}else{
									$.alert("所选"+dtoList[i].wlmc+"可领数量小于0！");
									return false;
								}

							}
						}
						$("#editPickingForm #llmxtb_list").bootstrapTable('load',t_map);
						$("#editPickingForm #llmx_json").val(JSON.stringify(preJson));
						$.closeModal(opts.modalName);
					},'json');
				}else{
					$("#editPickingForm #llmxtb_list").bootstrapTable('load',t_map);
					$("#editPickingForm #llmx_json").val(JSON.stringify(preJson));
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
/**
 * 选择客户列表
 * @returns
 */
function chooseKh(){
	var url=$('#editPickingForm #urlPrefix').val() + "/systemmain/client/pagedataListClient?access_token=" + $("#ac_tk").val();
	$.showDialog(url,'选择客户',addKhConfig);
}
var addKhConfig = {
	width		: "800px",
	modalName	:"addKhModal",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row = $('#client_list_ajaxForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					$('#editPickingForm #khid').val(sel_row[0].khid);
					$('#editPickingForm #khjc').val(sel_row[0].khjc);
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
 * 监听单据号标签点击事件
 */
var tagQgClick = $("#editPickingForm #qg").on('click','.label-info',function(e){
	event.stopPropagation();
	var url = $('#editPickingForm #urlPrefix').val() + "/purchase/purchase/pagedataChooseListPurchaseInfo?access_token=" + $("#ac_tk").val() + "&qgid=" + e.currentTarget.dataset.logo+"&lllb=1";
	$.showDialog(url, '选择请购明细', chooseEditQgmxConfig);
});
var chooseEditQgmxConfig = {
	width : "1000px",
	modalName	: "chooseEditQgmxModal",
	formName	: "chooseEditQgmxForm",
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
				if($("#chooseQgmxForm input[name='checkQgmx']:checked").length == 0){
					$.alert("未选中请购明细信息！");
					return false;
				}
				// 获取未更改请购单和明细信息
				var preJson = [];
				if($("#editPickingForm #llmx_json").val()){
					preJson = JSON.parse($("#editPickingForm #llmx_json").val());
				}
				// 获取更改请购单和明细信息
				var qgmxJson = [];
				$("#chooseQgmxForm input[name='checkQgmx']").each(function(){
					if($(this).is(":checked")){
						var sz={"qgid":'',"qgmxid":''};
						sz.qgid = qgid;
						sz.qgmxid = this.dataset.qgmxid;
						qgmxJson.push(sz);
					}
				})
				// 获取删除请购明细
				var qgid = $("#chooseQgmxForm input[name='qgid']").val();
				for (var j = preJson.length-1; j >=0; j--) {
					if(qgid == preJson[j].qgid){
						var isDel = true;
						for (var i = 0; i < qgmxJson.length; i++) {
							if(preJson[j].qgmxid == qgmxJson[i].qgmxid){
								isDel = false;
								break;
							}
						}
						if(isDel){
							// 根据明细ID直接更新页面列表(删除)
							for (var i = 0; i < t_map.rows.length; i++) {
								if(t_map.rows[i].qgmxid == preJson[j].qgmxid){
									preJson.splice(j,1);
									t_map.rows.splice(i,1);
									break;
								}
							}
						}
					}
				}
				// 获取新增请购明细
				var ids="";
				for (var i = 0; i < qgmxJson.length; i++) {
					var isAdd = true;
					for (var j = 0; j < preJson.length; j++) {
						if(preJson[j].qgmxid == qgmxJson[i].qgmxid){
							isAdd = false;
							break;
						}
					}
					if(isAdd){
						ids = ids + ","+ qgmxJson[i].qgmxid;
					}
				}
				if(ids.length > 0){
					ids = ids.substr(1);
					// 查询信息并更新到页面
					$.post($('#editPickingForm #urlPrefix').val() + "/purchase/purchase/pagedataGetPurchaseInfo",{ids:ids,"access_token":$("#ac_tk").val()},function(data){
						var htmxDtos = data.htmxDtos;
						if(htmxDtos != null && htmxDtos.length > 0){
							// 更新页面列表(新增)
							for (var i = 0; i < htmxDtos.length; i++) {
								if(t_map.rows){
									t_map.rows.push(htmxDtos[i]);
								}else{
									t_map.rows= [];
									t_map.rows.push(htmxDtos[i]);
								}
								var sz = {"qgid":'',"qgmxid":''};
								sz.qgid = htmxDtos[i].qgid;
								sz.qgmxid = htmxDtos[i].qgmxid;
								preJson.push(sz);
							}
							$("#editPickingForm #llmxtb_list").bootstrapTable('load',t_map);
							$("#editPickingForm #llmx_json").val(JSON.stringify(preJson));
							$.closeModal(opts.modalName);
						}
					},'json');
				}else{
					$("#editPickingForm #llmxtb_list").bootstrapTable('load',t_map);
					$("#editPickingForm #llmx_json").val(JSON.stringify(preJson));
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
/**
 * 需求单(及明细)
 * @returns
 */
function llglchooseXqdh(){
	var url = $('#editPickingForm #urlPrefix').val() + "/storehouse/receiveMateriel/pagedataProgress?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择需求单', llglchooseXqxxConfig);
}

var llglchooseXqxxConfig = {
	width : "1000px",
	modalName	: "chooseXqxxModal",
	formName	: "chooseXqxxForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#rece_progress").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				// 获取未更改请购单和明细信息
				var preJson = [];
				if($("#editPickingForm #llmx_json").val()){
					preJson = JSON.parse($("#editPickingForm #llmx_json").val());
				}
				// 获取更改后的请购单和明细信息
				var xqmx_json = [];
				if($("#rece_progress #xqmx_json").val()){
					xqmx_json = JSON.parse($("#rece_progress #xqmx_json").val());
					// 判断新增的请购明细信息
					var ids="";
					for (var i = 0; i < xqmx_json.length; i++) {
						var isAdd = true;
						for (var j = 0; j < preJson.length; j++) {
							if(preJson[j].xqjhmxid == xqmx_json[i].xqjhmxid){
								isAdd = false;
								break;
							}
						}
						if(isAdd){
							ids = ids + ","+ xqmx_json[i].xqjhmxid;
						}
					}
					if(ids.length > 0){
						ids = ids.substr(1);
						// 查询信息并更新到页面
						$.post($('#editPickingForm #urlPrefix').val() + "/storehouse/receiveMateriel/pagedataProgressListInfo",{"ids":ids,"xzbj":xzbj,"lllb":"1","access_token":$("#ac_tk").val()},function(data){
							var dtoList = data.dtoList;
							if(dtoList != null && dtoList.length > 0){
								//已存在的需求单号
								var yczxqdh = []
								if(!t_map.rows){
									t_map.rows= [];
								}
								for (var i = 0; i < t_map.rows.length; i++) {
									if (t_map.rows[i].xqjhid){
										yczxqdh.push(t_map.rows[i].xqjhid);
									}
								}
								// 更新页面列表(新增)
								for (var i = 0; i < dtoList.length; i++) {
									if(dtoList[i].klsl > 0 && dtoList[i].klsl != null && dtoList[i].klsl != ''){
										var isRepeat = false;
										for(var repeatIndex = 0; repeatIndex < t_map.rows.length; repeatIndex++) {
											if(t_map.rows[repeatIndex].ckhwid==dtoList[i].ckhwid){
												isRepeat = true;
											}
										}
										if(!isRepeat){
											t_map.rows.push(dtoList[i]);
											var sz = {"xqjhid":'',"xqjhmxid":'',"ckhwid":''};
											sz.xqjhid = dtoList[i].xqjhid;
											sz.xqjhmxid = dtoList[i].xqjhmxid;
											sz.ckhwid = dtoList[i].ckhwid;
											preJson.push(sz);
										}
									}
								}

								$("#editPickingForm #llmxtb_list").bootstrapTable('load',t_map);
								$("#editPickingForm #llmx_json").val(JSON.stringify(preJson));

								var selDjh = $("#rece_progress #xqdh").tagsinput('items');
								for(let i = yczxqdh.length-1; i >=0 ; i--){
									for(let j = selDjh.length-1; j >=0 ; j--){
										if (yczxqdh[i] == selDjh[j].value){
											selDjh.splice(j,1);
											j--;
										}
									}
								}
								$("#editPickingForm #xqdhs").tagsinput({
									itemValue: "xqjhid",
									itemText: "xqdh",
								})
								for(var i = 0; i < selDjh.length; i++){
									var value = selDjh[i].value;
									var text = selDjh[i].text;
									$("#editPickingForm #xqdhs").tagsinput('add', {"xqjhid":value,"xqdh":text});
								}
							}else{
								$.alert("仓库货物信息数量不足！");
							}

							$.closeModal(opts.modalName);
						},'json');
					}else{
						$("#editPickingForm #llmxtb_list").bootstrapTable('load',t_map);
						$("#editPickingForm #llmx_json").val(JSON.stringify(preJson));
						// 请购单号
						var selDjh = $("#rece_progress #xqdh").tagsinput('items');
						$("#editPickingForm #xqdhs").tagsinput({
							itemValue: "xqjhid",
							itemText: "xqdh",
						})
						for(var i = 0; i < selDjh.length; i++){
							var value = selDjh[i].value;
							var text = selDjh[i].text;
							$("#editPickingForm #xqdhs").tagsinput('add', {"xqjhid":value,"xqdh":text});
						}
						$.closeModal(opts.modalName);
					}
				}else{
					$.alert("添加领料失败！");
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
 * 生产指令单
 * @returns
 */
function chooseZldh(){
	var url = $('#editPickingForm #urlPrefix').val() + "/storehouse/receiveMateriel/pagedataChooseProduce?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择生产单', chooseZldhConfig);
}

var chooseZldhConfig = {
	width : "1000px",
	modalName	: "rece_ProduceModal",
	formName	: "rece_ProduceForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#rece_Produce").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				var sel_row = $('#rece_Produce #rece_Produce_tb_list').bootstrapTable('getSelections');//获取选择行数据
				var sczlid = "";
				var wlid = "";
				var zldh = "";
				var xlhs = "";
				var scsl = 0;
				if(sel_row.length>0) {
					sczlid = sel_row[0].sczlid;
					wlid = sel_row[0].wlid;
					zldh = sel_row[0].zldh;
					for (var i = 0; i < sel_row.length; i++) {
						xlhs = xlhs +","+sel_row[i].xlh;
						scsl = scsl+Number(sel_row[i].scsl);
					}
				}
				$("#editPickingForm #zldh").tagsinput({
					itemValue: "sczlid",
					itemText: "zldh",
				})
				var ysczlid = $('#editPickingForm #sczlid').val()
				$("#editPickingForm #zldh").tagsinput('remove', {"sczlid":ysczlid,"zldh":""});
				if (zldh.length>0){
					$("#editPickingForm #zldh").tagsinput('add', {"sczlid":sczlid,"zldh":zldh});
				}
				$('#editPickingForm #sczlid').val(sczlid)
				if (xlhs.length>0){
					xlhs= xlhs.substring(1);
				}
				// 查询信息并更新到页面
				$.post($('#editPickingForm #urlPrefix').val() + "/progress/produce/pagedataGetProduceReceiveBom",{"mjwlid":wlid,"xzbj":xzbj,"scsl":scsl,"access_token":$("#ac_tk").val()},function(data){
					var bommxDtos = data.bommxDtos;
					if(bommxDtos != null && bommxDtos.length > 0){
						//去除上个指令单的
						t_map.rows= [];
						var preJson = [];
						// 更新页面列表(新增)
						for (var i = 0; i < bommxDtos.length; i++) {
							if (bommxDtos[i].klsl>0){
								bommxDtos[i].hwllxxbz = xlhs;
								bommxDtos[i].kqls = bommxDtos[i].klsl;
								t_map.rows.push(bommxDtos[i]);
								var sz = {"sczlid":'',"wlid":'',"ckhwid":''};
								sz.sczlid = sczlid;
								sz.wlid = bommxDtos[i].wlid;
								sz.ckhwid = bommxDtos[i].ckhwid;
								preJson.push(sz);
							}
						}
					}
					$("#editPickingForm #kcbz_list").empty();
					var kcbz_listHtml="";
					kcbz_listHtml+=' <div class="body-content tab-content" style="min-height:0px">\n' +
					'                <div class="panel-body" >\n' +
					'                   <table class="tab" style="width:100%;">\n' +
					'                        <tr style="background-color: #63a3ff;line-height: 35px;">\n' +
					'                            <th style="width:5%">序</th>\n' +
					'                            <th style="width:15%">物料编码</th>\n' +
					'                            <th style="width:35%">物料名称</th>\n' +
					'                            <th style="width:10%">母件生产数量</th>\n' +
					'                            <th style="width:10%">子件标准用量</th>\n' +
					'                            <th style="width:10%">需领数量</th>\n' +
					'                            <th style="width:10%">可领数量</th>\n' +
					'                            <th style="width:10%">缺少数量</th>\n' +
					'                        </tr>\n' +
					'                        <div>\n';

					for(var i=0;i<data.qsKcList.length;i++){
						kcbz_listHtml+=' <tr>\n' +
						'      <td>'+(i+1)+'</td><!-- 序号 -->\n' +
						'      <td>'+data.qsKcList[i].wlbm+'</td><!-- 物料编码 -->\n' +
						'      <td>'+data.qsKcList[i].wlmc+'</td><!-- 物料名称 -->\n' +
						'      <td>'+data.qsKcList[i].scsl+'</td><!-- 母件生产数量 -->\n' +
						'      <td>'+data.qsKcList[i].bzyl+'</td><!-- 子件标准用量 -->\n' +
						'      <td>'+data.qsKcList[i].xlsl+'</td><!-- 需领数量 -->\n' +
						'      <td>'+data.qsKcList[i].klsl+'</td><!-- 可领数量 -->\n' +
						'      <td>'+data.qsKcList[i].qssl+'</td><!-- 缺少数量 -->\n' +
						'      </tr>';
					}
					kcbz_listHtml+=
					'                        </div>\n' +
					'                    </table>\n' +
					'                </div>\n' +
					'            </div>';
					$("#editPickingForm #kcbz_list").append(kcbz_listHtml);
					$("#editPickingForm #llmxtb_list").bootstrapTable('load',t_map);
					$("#editPickingForm #llmx_json").val(JSON.stringify(preJson));
					$.closeModal(opts.modalName);
				},'json');
				$("#editPickingForm #llmxtb_list").bootstrapTable('load',t_map);
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
 * 选择请购单据号(及明细)
 * @returns
 */
function llglchooseQgxx(){
	var url = $('#editPickingForm #urlPrefix').val() + "/purchase/purchase/pagedataChooseListPurchase?access_token=" + $("#ac_tk").val()+"&lllb=1";
	$.showDialog(url, '选择请购单号', llglchooseQgxxConfig);
}
var llglchooseQgxxConfig = {
	width : "1000px",
	modalName	: "chooseQgxxModal",
	formName	: "chooseQgxxForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#chooseQgxxForm").valid()){
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				// 获取未更改请购单和明细信息
				var preJson = [];
				if($("#editPickingForm #llmx_json").val()){
					preJson = JSON.parse($("#editPickingForm #llmx_json").val());
				}
				// 获取更改后的请购单和明细信息
				var qgmxJson = [];
				if($("#chooseQgxxForm #qgmx_json").val()){
					qgmxJson = JSON.parse($("#chooseQgxxForm #qgmx_json").val());
					// 判断新增的请购明细信息
					var ids="";
					for (var i = 0; i < qgmxJson.length; i++) {
						var isAdd = true;
						for (var j = 0; j < preJson.length; j++) {
							if(preJson[j].qgmxid == qgmxJson[i].qgmxid){
								isAdd = false;
								break;
							}
						}
						if(isAdd){
							ids = ids + ","+ qgmxJson[i].qgmxid;
						}
					}
					if(ids.length > 0){
						ids = ids.substr(1);
						var llid = $("#editPickingForm #llid").val();
						// 查询信息并更新到页面
						$.post($('#editPickingForm #urlPrefix').val() + "/purchase/purchase/pagedataGetPurchaseInfo",{"ids":ids,"xzbj":xzbj,"llid":llid,"lllb":"1","access_token":$("#ac_tk").val()},function(data){
							var htmxDtos = data.htmxDtos;
							//已存在的请购单号
							var yczqgdh = []
							if(htmxDtos != null && htmxDtos.length > 0){

								if(!t_map.rows){
									t_map.rows= [];
								}
								for (var i = 0; i < t_map.rows.length; i++) {
									if (t_map.rows[i].qgid){
										yczqgdh.push(t_map.rows[i].qgid);
									}
								}
								// 更新页面列表(新增)
								for (var i = 0; i < htmxDtos.length; i++) {
									if(htmxDtos[i].klsl > 0 && htmxDtos[i].klsl != null && htmxDtos[i].klsl != ''){
										var isRepeat = false;
										for(var repeatIndex = 0; repeatIndex < t_map.rows.length; repeatIndex++) {
											if(t_map.rows[repeatIndex].ckhwid==htmxDtos[i].ckhwid){
												isRepeat = true;
											}
										}
										if(!isRepeat){
											t_map.rows.push(htmxDtos[i]);
											var sz = {"qgid":'',"qgmxid":'',"ckhwid":''};
											sz.qgid = htmxDtos[i].qgid;
											sz.qgmxid = htmxDtos[i].qgmxid;
											sz.ckhwid = htmxDtos[i].ckhwid;
											preJson.push(sz);
										}
									}
								}
							}
							$("#editPickingForm #llmxtb_list").bootstrapTable('load',t_map);
							$("#editPickingForm #llmx_json").val(JSON.stringify(preJson));
							// 请购单号
							var selDjh = $("#chooseQgxxForm #yxdjh").tagsinput('items');
							for(let i = yczqgdh.length-1; i >=0 ; i--){
								for(let j = selDjh.length-1; j >=0 ; j--){
									if (yczqgdh[i] == selDjh[j].value){
										selDjh.splice(j,1);
										j--;
									}
								}
							}
							$("#editPickingForm #djhs").tagsinput({
								itemValue: "qgid",
								itemText: "djh",
							})
							for(var i = 0; i < selDjh.length; i++){
								var value = selDjh[i].value;
								var text = selDjh[i].text;
								$("#editPickingForm #djhs").tagsinput('add', {"qgid":value,"djh":text});
							}
							$.closeModal(opts.modalName);
						},'json');
					}else{
						$("#editPickingForm #llmxtb_list").bootstrapTable('load',t_map);
						$("#editPickingForm #llmx_json").val(JSON.stringify(preJson));
						// 请购单号
						var selDjh = $("#chooseQgxxForm #yxdjh").tagsinput('items');
						$("#editPickingForm #djhs").tagsinput({
							itemValue: "qgid",
							itemText: "djh",
						})
						for(var i = 0; i < selDjh.length; i++){
							var value = selDjh[i].value;
							var text = selDjh[i].text;
							$("#editPickingForm #djhs").tagsinput('add', {"qgid":value,"djh":text});
						}
						$.closeModal(opts.modalName);
					}
				}else{
					$.alert("添加领料失败！");
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
 * 选择领料明细
 * @returns
 */
function chooseZsh(index,event){
	t_index=index;
	var url=$('#editPickingForm #urlPrefix').val() + "/storehouse/receiveMateriel/pagedataChooseZsh?access_token=" + $("#ac_tk").val()+"&wlid="+t_map.rows[index].wlid+"&ckid="+t_map.rows[index].ckid+"&llid="+$("#editPickingForm #llid").val()+"&zt="+$("#editPickingForm #zt").val();
	$.showDialog(url,'选择追溯号',chooseZshConfig);	
}
var chooseZshConfig = {
	width		: "1600px",
	modalName	:"chooseZshModal",
	formName	: "chooseZsh_formSearch",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#chooseZsh_formSearch").valid()){
					$.alert("请填写完整信息");
					return false;
				}
				if(th_map.rows != null && th_map.rows.length > 0){
					var json = [];
					for(var i=0;i<th_map.rows.length;i++){
						var sz = {"hwid":'',"wlid":'',"qlsl":'',"qqls":''};
						sz.hwid=th_map.rows[i].hwid;
						sz.wlid=th_map.rows[i].wlid;
						sz.qlsl=th_map.rows[i].qlsl;
						sz.qqls=th_map.rows[i].qqls;
						sz.llmxid=th_map.rows[i].llmxid;
						json.push(sz);
	        		}
					t_map.rows[t_index].hwllmx_json = JSON.stringify(json);
					let hwllmx_json = $("#editPickingForm #hwllmx_json").val();
					if (hwllmx_json && hwllmx_json!="") {
						let jsarr = JSON.parse(hwllmx_json);
						for (let i = 0; i < json.length; i++) {
							jsarr.push(json[i]);
						}
						let temp = [];
						let pos =0;
						do {
							temp[pos++] = jsarr[jsarr.length-1];
							jsarr = delByTarget(jsarr,jsarr[jsarr.length-1])
						} while (jsarr.length != 0);
						$("#editPickingForm #hwllmx_json").val( JSON.stringify(temp));
					}else{
						$("#editPickingForm #hwllmx_json").val(t_map.rows[t_index].hwllmx_json);
					}
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
 * 根据具体元素删除
 */
function delByTarget(arr,target){
	let temp = [];
	let pos =0;
	for (let i = 0; i < arr.length; i++) {
		if (arr[i].hwid != target.hwid){
			temp[pos++] = arr[i]
		}
	}
	return temp;
}
/**
 * 删除操作
 * @param index
 * @param event
 * @returns
 */
function delWlDetail(index,value,ckhwid,event){
	var preJson = JSON.parse($("#editPickingForm #llmx_json").val());
	let rows = t_map.rows.length;
	let pops = 0;
	let temps = JSON.stringify(preJson);
	var temp = JSON.parse(temps);
	for (let i = 0; i < preJson.length; i++) {
		if (preJson[i].ckhwid == ckhwid){
			let id = preJson[i].qgid;
			let xqjhid = preJson[i].xqjhid;
			preJson.splice(i,1);
			i--;
			for (let j = 0; j < preJson.length; j++) {
				if (preJson[j].qgid == id){
					pops += 1;
				}
				if (preJson[j].xqjhid == xqjhid){
					pops += 1;
				}
			}
			if (pops <= 0){
				for (let k = 0; k < temp.length; k++) {
					let bz = 0;
					if (temp[k].qgid == id){
						$("#editPickingForm #djhs").tagsinput('remove', temp[k]);
						bz += 1;
					}
					if (temp[k].xqjhid == xqjhid){
						$("#editPickingForm #xqdhs").tagsinput('remove', temp[k]);
						bz += 1;
					}
					if (bz != 0){
						break
					}
				}
			}
			pops = 0;
			temps = JSON.stringify(preJson);
			temp = JSON.parse(temps);
		}
	}
	if (rows == t_map.rows.length){
		t_map.rows.splice(index,1);
	}
	$("#editPickingForm #llmxtb_list").bootstrapTable('load',t_map);
	$("#editPickingForm #llmx_json").val(JSON.stringify(preJson));
}

/**
 * 失去焦点事件(修改列表参数)
 * @param id
 * @param e
 * @param zdmc
 * @returns
 */
function checkqlslSum(index, e, zdmc) {
	var kqls =  $("#editPickingForm #kqls_"+index).text();
	var qlsl = 0.00;
	if(e.value!=null && e.value!=""){
		qlsl = parseFloat(e.value);
	}
	var kqls_t = parseFloat(kqls);
	if(qlsl.toFixed(2)-kqls_t.toFixed(2)>0){
		$("#editPickingForm #message").val("请领数量不能大于可请领数！");
		$("#editPickingForm #qlsldiv_"+index).attr("style","background:orange;");
		$("#editPickingForm #qlsldiv_"+index).attr("isBeyond","false");
		t_map.rows[index].qlsl = 0;
	}else{
		$("#editPickingForm #message").val("");
		$("#editPickingForm #qlsldiv_"+index).attr("style","background:darkcyan;");
		$("#editPickingForm #qlsldiv_"+index).attr("isBeyond","true");
	}
	t_map.rows[index].qlsl = e.value;
	$("#editPickingForm #llmx_json").val(JSON.stringify(t_map.rows));
}
/**
 * 失去焦点事件(修改列表参数)
 * @param id
 * @param e
 * @param zdmc
 * @returns
 */
function checkBz(index, e, zdmc) {
	t_map.rows[index].hwllxxbz = e.value;
	$("#editPickingForm #llmx_json").val(JSON.stringify(t_map.rows));
}
/**
 * 失去焦点事件(修改列表参数)
 * @param id
 * @param e
 * @param zdmc
 * @returns
 */
function checkCplx(index, e, zdmc) {
	t_map.rows[index].cplx = e.value;
	$("#editPickingForm #llmx_json").val(JSON.stringify(t_map.rows));
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
	
	var json=[];
	var data=$('#editPickingForm #llmxtb_list').bootstrapTable('getData');//获取选择行数据
	for(var i=0;i<data.length;i++){
		var sz={"index":i,"wlid":'',"xmdl":'',"xmbm":'',"qlsl":'',"kqls":'',"qlyds":''};
		sz.wlid=data[i].wlid;
		sz.xmdl=data[i].xmdl;
		sz.xmbm=data[i].xmbm;
		sz.qlsl=data[i].qlsl;
		sz.kqls=data[i].kqls;
		sz.qlyds=data[i].qlyds;
		json.push(sz);
	}
	$("#editPickingForm #llmx_json").val(JSON.stringify(json));
}

/**
 * 选择申请人
 * @returns
 */
function chooseSqr() {
	var url = $('#editPickingForm #urlPrefix').val()+"/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择申请人', chooseQrrConfig);
}
var chooseQrrConfig = { 
	width : "800px",
	height : "500px",
	modalName	: "chooseSqrModal",
	formName	: "editPickingForm",
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
					$('#editPickingForm #sqry').val(sel_row[0].yhid);
					$('#editPickingForm #sqrmc').val(sel_row[0].zsxm);
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
 * 选择归属人
 * @returns
 */
function chooseGsr() {
	var url = $('#editPickingForm #urlPrefix').val()+"/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择归属人', chooseGsrConfig);
}
var chooseGsrConfig = {
	width : "800px",
	height : "500px",
	modalName	: "chooseSqrModal",
	formName	: "editPickingForm",
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
					$('#editPickingForm #gsr').val(sel_row[0].yhid);
					$('#editPickingForm #gsrmc').val(sel_row[0].zsxm);
					$('#editPickingForm #fzr').val(sel_row[0].yhid);
					$('#editPickingForm #fzrmc').val(sel_row[0].zsxm);
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
 * 选择归属人
 * @returns
 */
var lx="";
function chooseBmsbfzr(flag) {
	var url = $('#editPickingForm #urlPrefix').val()+"/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择部门设备负责人', chooseBmsbfzrConfig);
	lx=flag;
}
var chooseBmsbfzrConfig = {
	width : "800px",
	height : "500px",
	modalName	: "chooseBmsbfzrModal",
	formName	: "editPickingForm",
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
					if (lx=='bmfzr'){
						$('#editPickingForm #bmsbfzr').val(sel_row[0].yhid);
						$('#editPickingForm #bmsbfzrmc').val(sel_row[0].zsxm);
					}else if (lx=='fzr'){
						$('#editPickingForm #fzr').val(sel_row[0].yhid);
						$('#editPickingForm #fzrmc').val(sel_row[0].zsxm);
					}
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
	var url=$('#editPickingForm #urlPrefix').val() + "/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
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
					if (sel_row[0].jgdm){
						$('#editPickingForm #jgdm').val(sel_row[0].jgdm);
					}else{
						$('#editPickingForm #jgdm').val("");
						$.alert("U8不存在此部门！");
						return false;
					}
					$('#editPickingForm #sqbm').val(sel_row[0].jgid);
					$('#editPickingForm #sqbmmc').val(sel_row[0].jgmc);
					if (sel_row[0].kzcs1){
						let url= $('#editPickingForm #urlPrefix').val()+"/storehouse/requisition/pagedataGenerateLljlbh";
						$.ajax({
							type:'post',
							url:url,
							cache: false,
							data: {"jgdh":sel_row[0].kzcs1,"access_token":$("#ac_tk").val()},
							dataType:'json',
							success:function(data){
								if (data.jlbh){
									$('#editPickingForm #jlbh').val(data.jlbh);
								}
							}
						});
					}else{
						$('#editPickingForm #sqbm').val("");
						$('#editPickingForm #sqbmmc').val("");
						$('#editPickingForm #jgdm').val("");
						$('#editPickingForm #jlbh').val("");
						$.alert("所选部门存在异常！");
						return false;
					}
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
 * 选择归属人部门列表
 * @returns
 */
function chooseGsrbm(){
	var url=$('#editPickingForm #urlPrefix').val() + "/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
	$.showDialog(url,'选择归属人部门',addGsrbmConfig);
}
var addGsrbmConfig = {
	width		: "800px",
	modalName	:"addGsrbmModal",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row = $('#listDepartmentForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					if (sel_row[0].jgdm){
						$('#editPickingForm #gsrbmdm').val(sel_row[0].jgdm);
					}else{
						$('#editPickingForm #gsrbmdm').val("");
					}
					$('#editPickingForm #gsrbm').val(sel_row[0].jgid);
					$('#editPickingForm #gsrbmmc').val(sel_row[0].jgmc);
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
		var url=$("#editPickingForm #urlPrefix").val()+"/ws/sjxxpripreview/?fjid="+fjid
		$.showDialog(url,'图片预览',JPGMaterConfig);
	}else if(type.toLowerCase()==".pdf"){
		var url= $("#editPickingForm #urlPrefix").val()+"/common/file/pdfPreview?fjid=" + fjid;
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
    jQuery('<form action="'+$('#editPickingForm #urlPrefix').val() + '/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
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
			var url= $("#editPickingForm #urlPrefix").val()+"/common/file/delFile";
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
	if(!$("#editPickingForm #fjids").val()){
		$("#editPickingForm #fjids").val(fjid);
	}else{
		$("#editPickingForm #fjids").val($("#editPickingForm #fjids").val()+","+fjid);
	}
}


/**
 * 根据物料名称模糊查询
 */
$('#editPickingForm #addnr').typeahead({
    source : function(query, process) {
        return $.ajax({
            url : $('#editPickingForm #urlPrefix').val()+'/storehouse/requisition/pagedataQueryWlxx?xzbj='+xzbj,
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
                                id : item.ckhwid,
                                name : item.ckmc+"-"+item.wlbm+"-"+item.wlmc
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
        $('#editPickingForm #addwlid').attr('value', item.id);
        return item.name;
    }
});

/**
 * 回车键添加物料至请购车
 * @param event
 * @returns
 */
var addkeydown = $("#addnr").bind("keydown",function(event){
	var ckhwid=$('#editPickingForm #addwlid').val();
	if (event.keyCode == "13") {
		//判断新增输入框是否有内容
		if(!$('#editPickingForm #addnr').val()){
			return false;
		}
		setTimeout(function(){ if(ckhwid != null && ckhwid != ''){
			$.ajax({ 
			    type:'post',  
		    		url:$('#editPickingForm #urlPrefix').val()+"/storehouse/requisition/pagedataQueryWlxxByWlid", 
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
				    		var json=JSON.parse($("#editPickingForm #llmx_json").val());
				    		if(t_map.rows==null || t_map.rows ==""){
				    			t_map.rows=[];
				    		}else {
								//是否有设备
								var isHave = false;
								for(var i=0;i<t_map.rows.length;i++){
									if (t_map.rows[i].lbcskz1=="3"){
										isHave = true;
									}
								}
								if (isHave&&data.ckhwxxDto.lbcskz1!='3'){
									$.confirm("设备不允许和其他物料一起领料！");
									return;
								}
							}
							var isRepeat = false;
							for(var repeatIndex = 0; repeatIndex < t_map.rows.length; repeatIndex++) {
								if(t_map.rows[repeatIndex].ckhwid==data.ckhwxxDto.ckhwid){
									isRepeat = true;
								}
							}
							if(!isRepeat){
								t_map.rows.push(data.ckhwxxDto);
								var sz={"index":'',"ckhwid":'',"wlid":'',"qlsl":'',"xmdl":'',"xmbm":'',"lbcskz1":''};
								sz.index=json.length;
								sz.ckhwid=data.ckhwxxDto.ckhwid;
								sz.wlid=data.ckhwxxDto.wlid;
								sz.qlsl=data.ckhwxxDto.qlsl;
								sz.xmdl=data.ckhwxxDto.xmdl;
								sz.xmbm=data.ckhwxxDto.xmbm;
								sz.lbcskz1=data.ckhwxxDto.lbcskz1;
								json.push(sz);
								$("#editPickingForm #llmx_json").val(JSON.stringify(json));
								$("#editPickingForm #llmxtb_list").bootstrapTable('load',t_map);
								$('#editPickingForm #addwlid').val("");
								$('#editPickingForm #addnr').val("");
							}else{
								$.confirm($('#editPickingForm #addnr').val()+"已选择！");
							}
			    		}
			    	}else{
			    		$.confirm("该物料不存在!");
			    	}
			    }
			});
		}else{
			var addnr = $('#editPickingForm #addnr').val();
			if(addnr != null && addnr != ''){
				$.confirm("请选择物料信息!");
			}
		}}, '200')
	}
})

/**
 * 点击确定添加物料
 * @param 
 * @param 
 * @returns
 */
function cofirmaddwl(){
	var ckhwid=$('#editPickingForm #addwlid').val();
	//判断新增输入框是否有内容
	if(!$('#editPickingForm #addnr').val()){
		return false;
	}
	setTimeout(function(){ if(ckhwid != null && ckhwid != ''){
		$.ajax({ 
		    type:'post',  
	    		url:$('#editPickingForm #urlPrefix').val()+"/storehouse/requisition/pagedataQueryWlxxByWlid", 
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
			    		var json=JSON.parse($("#editPickingForm #llmx_json").val());
						if(t_map.rows==null || t_map.rows ==""){
							t_map.rows=[];
						}else {
							//是否有设备
							var isHave = false;
							for(var i=0;i<t_map.rows.length;i++){
								if (t_map.rows[i].lbcskz1=="3"){
									isHave = true;
								}
							}
							if (isHave&&data.ckhwxxDto.lbcskz1!='3'){
								$.confirm("设备不允许和其他物料一起领料！");
								return;
							}
						}
						var isRepeat = false;
						for(var repeatIndex = 0; repeatIndex < t_map.rows.length; repeatIndex++) {
							if(t_map.rows[repeatIndex].ckhwid==data.ckhwxxDto.ckhwid){
								isRepeat = true;
							}
						}
						if (!isRepeat){
							t_map.rows.push(data.ckhwxxDto);
							var sz={"index":'',"ckhwid":'',"wlid":'',"qlsl":'',"xmdl":'',"xmbm":'',"lbcskz1":''};
							sz.index=json.length;
							sz.ckhwid=data.ckhwxxDto.ckhwid;
							sz.wlid=data.ckhwxxDto.wlid;
							sz.qlsl=data.ckhwxxDto.qlsl;
							sz.xmdl=data.ckhwxxDto.xmdl;
							sz.xmbm=data.ckhwxxDto.xmbm;
							sz.lbcskz1=data.ckhwxxDto.lbcskz1;
							json.push(sz);
							$("#editPickingForm #llmx_json").val(JSON.stringify(json));
							$("#editPickingForm #llmxtb_list").bootstrapTable('load',t_map);
							$('#editPickingForm #addwlid').val("");
							$('#editPickingForm #addnr').val("");
						}else{
							$.confirm($('#editPickingForm #addnr').val()+"已选择！");
						}
		    		}
		    	}else{
		    		$.confirm("该物料不存在!");
		    	}
		    }
		});
	}else{
		var addnr = $('#editPickingForm #addnr').val();
		if(addnr != null && addnr != ''){
			$.confirm("请选择物料信息!");
		}
	}}, '200')
}

$(document).ready(function(){
	//初始化列表
	var oTable=new pickingEdit_TableInit();
	oTable.Init();
	// 初始化页面
	init();	
	//0.初始化fileinput
	var oFileInput = new FileInput();
	var picking_params=[];
	picking_params.prefix=$("#editPickingForm #urlPrefix").val();
	oFileInput.Init("editPickingForm","displayUpInfo",2,1,"pro_file",null,picking_params);
	//所有下拉框添加choose样式
	jQuery('#editPickingForm .chosen-select').chosen({width: '100%'});
	var sczlid = $("#editPickingForm #sczlid").val();
	var zldh = $("#editPickingForm #xzldh").val();
	if (sczlid!=null&&sczlid!=''&&zldh!=null&&zldh!=''){
		$("#editPickingForm #zldh").tagsinput({
			itemValue: "sczlid",
			itemText: "zldh",
		})
		$("#editPickingForm #zldh").tagsinput('add', {"sczlid":sczlid,"zldh":zldh});
	}
});