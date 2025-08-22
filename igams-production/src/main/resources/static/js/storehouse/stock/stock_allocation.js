var t_map = [];
var t_index="";
// 显示字段
var columnsArray = [
	{
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
		visible: false
	},{				
		field: 'wlbm',
		title: '物料编码',
		width: '8%',
		align: 'left',
		visible: true
	}, {				
		field: 'wlmc',
		title: '物料名称',
		width: '10%',
		align: 'left',
		visible: true
	},  {
		field: 'ckqxmc',
		title: '仓库分类',
		width: '10%',
		align: 'left',
		visible: true
	}, {
		field: 'ychh',
		title: '货号',
		width: '8%',
		align: 'left',
		visible: true
	},{				
		field: 'scs',
		title: '生产商',
		width: '8%',
		align: 'left',
		visible: true
	},{				
		field: 'gg',
		title: '规格',
		width: '4%',
		align: 'left',
		visible: true
	}, {				
		field: 'jldw',
		title: '单位',
		width: '5%',
		align: 'left',
		visible: true
	}, {				
		field: 'kcl',
		title: '库存量',
		width: '5%',
		align: 'left',
		visible: true
	},{				
		field: 'aqkc',
		title: '安全库存',
		width: '5%',
		align: 'left',
		visible: false
	},{
        field: 'cz',
        title: '操作',
        width: '5%',
        align: 'left',
        formatter:allocationForm_czformat,
        visible: true
    }];
var pickingEdit_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#allocationForm #tb_list').bootstrapTable({
            url: $('#allocationForm #urlPrefix').val()+$('#allocationForm #url').val(),         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#allocationForm #toolbar',                //工具按钮用哪个容器
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
        };
    	return map;
    };
    return oTableInit;
};


/**
 * 初始化页面
 * @returns
 */
function init(){
  	//添加日期控件
	laydate.render({
	   elem: '#dbrq'
	  ,theme: '#2381E9'
	});
}

$('#allocationForm').submit(function() {
	$('#allocationForm #cklb').attr("disabled",false);
	$('#allocationForm #rklb').attr("disabled",false);
	return false;
});
/**
 * 转出仓库改变事件
 * @returns
 */
function checkzcck(){
	let zrck = $('#allocationForm #zrck').val();
	if(zrck){
		let zcck = $('#allocationForm #zcck').val();
		if (zrck == zcck){
			$('#allocationForm #zrck option:first').prop("selected", 'selected');
			$("#allocationForm #zrck").trigger("chosen:updated");
			$.alert("转入仓库与转出仓库不能相同！");
			return
		}
	}
	for (let i = 0; i < t_map.rows.length; i++) {
		t_map.rows[i].dbmx_json = [];
	}
}
/**
 * 转入仓库改变事件
 * @returns
 */
function checkzrck(){
	let zcck = $('#allocationForm #zcck').val();
	if(zcck){
		let zrck = $('#allocationForm #zrck').val();
		if (zrck == zcck){
			$('#allocationForm #zrck option:first').prop("selected", 'selected');
			$("#allocationForm #zrck").trigger("chosen:updated");
			$.alert("转入仓库与转出仓库不能相同！");
			return
		}
	}
	for (let i = 0; i < t_map.rows.length; i++) {
		let dbmx_json = JSON.parse(t_map.rows[i].dbmx_json);
		for (let j = 0; j < dbmx_json.length; j++) {
			dbmx_json[j].drkw = "";
		}
		t_map.rows[i].dbmx_json = JSON.stringify(dbmx_json);
	}
}
/**
 * 删除操作
 * @param index
 * @param event
 * @returns
 */
function delWlInfo(index,event){
	t_map.rows.splice(index,1);
	$("#allocationForm #tb_list").bootstrapTable('load',t_map);
}



/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function allocationForm_czformat(value,row,index){
	var html="";
	html = html+"<span style='margin-left:5px;height:24px !important;' class='btn btn-info' title='选择调拨明细' onclick=\"chooseDbmx('" + index + "',event)\" >详细</span>";
	html = html+"<span style='margin-left:5px;height:24px !important;' class='btn btn-danger' title='删除物料信息' onclick=\"delWlInfo('" + index + "',event)\" >删除</span>";
	return html;
}

/**
 * 选择调拨明细
 * @returns
 */
function chooseDbmx(index,event){
	t_index=index;
	let zcck = document.getElementById("zcck").value;
	let zrck = document.getElementById("zrck").value;
	var url=$('#allocationForm #urlPrefix').val() + "/storehouse/stock/pagedataChooseDbmx?access_token=" + $("#ac_tk").val()+"&wlid="+t_map.rows[index].wlid+"&ckid="+zcck+"&zrck="+zrck;
	$.showDialog(url,'选择调拨明细',chooseDbmxConfig);	
}
var chooseDbmxConfig = {
	width		: "1600px",
	modalName	:"chooseDbmxModal",
	formName	: "chooseDbmx_formSearch",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#chooseDbmx_formSearch").valid()){
					$.alert("请确认信息是否正确！");
					return false;
				}
				if(th_map.rows != null && th_map.rows.length > 0){
					var json = [];
					for(var i=0;i<th_map.rows.length;i++){
						var sz = {"hwid":'',"dbsl":'',"dckw":'',"drkw":''};
						sz.hwid=th_map.rows[i].hwid;
						sz.dbsl=th_map.rows[i].dbsl;
						sz.dckw=th_map.rows[i].dckw;
						sz.drkw=th_map.rows[i].drkw;
						json.push(sz);
	        		}
					t_map.rows[t_index].dbmx_json = JSON.stringify(json);
	    		}
			},
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

function pdzcck() {
	var obj = document.getElementById("zcck");
	var index = obj.selectedIndex;
	var id = obj.options[index].value;
	var objr = document.getElementById("zrck");
	var indexr = objr.selectedIndex;
	var idr = obj.options[indexr].value;
	if ( id == '' || idr ==''){
		$.confirm("请先选择转出·转入仓库!");
		return false;
	}
}


/**
 * 选择申请人
 * @returns
 */
function chooseJsr() {
	var url = $('#allocationForm #urlPrefix').val()+"/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url, '选择申请人', chooseQrrConfig);
}
var chooseQrrConfig = { 
	width : "800px",
	height : "500px",
	modalName	: "chooseSqrModal",
	formName	: "allocationForm",
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
					$('#allocationForm #jsr').val(sel_row[0].yhid);
					$('#allocationForm #jsrmc').val(sel_row[0].zsxm);
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
 * 回车键添加物料至请购车
 * @param event
 * @returns
 */
var addkeydown = $("#addnr").bind("keydown",function(event){
	var wlid=$('#allocationForm #addwlid').val();
	if (event.keyCode == "13") {
		//判断新增输入框是否有内容
		if(!$('#allocationForm #addnr').val()){
			return false;
		}
		setTimeout(function(){ if(wlid != null && wlid != ''){
			$.ajax({ 
			    type:'post',  
		    		url:$('#allocationForm #urlPrefix').val()+"/storehouse/stock/pagedataQueryWlxxByWlid",
			    cache: false,
			    data: {"ckhwid":wlid,"access_token":$("#ac_tk").val()},
			    dataType:'json', 
			    success:function(data){
			    	//返回值
			    	if(data.ckhwxxDto!=null && data.ckhwxxDto!=''){
			    		//kcbj=1 有库存，kcbj=0 库存未0
			    		if(data.ckhwxxDto.kcbj!='1'){
			    			$.confirm("该物料库存不足！");
			    		}else{
				    		if(t_map.rows==null || t_map.rows ==""){
				    			t_map.rows=[];
				    		}
				    		t_map.rows.push(data.ckhwxxDto);
							let temp = [];
							let pos =0;
							do {
								temp[pos++] = t_map.rows[t_map.rows.length-1];
								t_map.rows = delTargetByCkhwid(t_map.rows,t_map.rows[t_map.rows.length-1])
							} while (t_map.rows.length != 0);
							t_map.rows = temp;
				    		$("#allocationForm #tb_list").bootstrapTable('load',t_map);
				    		$('#allocationForm #addwlid').val("");
				    		$('#allocationForm #addnr').val("");
			    		}
			    	}else{
			    		$.confirm("该物料不存在!");
			    	}
			    }
			});
		}else{
			var addnr = $('#allocationForm #addnr').val();
			if(addnr != null && addnr != ''){
				$.confirm("请选择物料信息!");
			}
		}}, '200')
	}
})

/**
 * 根据具体元素删除
 */
function delTargetByCkhwid(arr,target){
	let temp = [];
	let pos =0;
	for (let i = 0; i < arr.length; i++) {
		if (arr[i].ckhwid != target.ckhwid){
			temp[pos++] = arr[i]
		}
	}
	return temp;
}
/**
 * 根据物料名称模糊查询
 */
$('#allocationForm #addnr').typeahead({
	source : function(query, process) {
		let zcck = 	$('#allocationForm #zcck').val();
		return $.ajax({
			url : $('#allocationForm #urlPrefix').val()+'/storehouse/requisition/pagedataQueryWlxx',
			type : 'post',
			data : {
				"entire" : query,
				"zcck" : zcck,
				"access_token" : $("#ac_tk").val()
			},
			dataType : 'json',
			success : function(result) {
				var resultList = result.ckhwxxDtos
					.map(function(item) {
						var aItem = {
							id : item.ckhwid,
							name : item.wlbm+"-"+item.wlmc+"-"+item.ckqxmc+"-"+item.scs+"-"+item.gg,
							text:item.wlid
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
		$('#allocationForm #addwlid').attr('value', item.id);
		$('#allocationForm #wlid').attr('value', item.text);
		return item.name;
	}
});

/**
 * 点击确定添加物料
 * @param 
 * @param 
 * @returns
 */
function cofirmaddwl(){
	var wlid=$('#allocationForm #addwlid').val();
	var wlid_t=$('#allocationForm #wlid').val();
	if (t_map.rows.length>=1){
		for (let i = 0; i < t_map.rows.length; i++) {
			if (wlid_t==t_map.rows[i].wlid){
				$.confirm("该物料已存在!");
				return false;
			}
		}
	}
	//判断新增输入框是否有内容
	if(!$('#allocationForm #addnr').val()){
		return false;
	}
	setTimeout(function(){ if(wlid != null && wlid != ''){
		$.ajax({ 
		    type:'post',  
	    		url:$('#allocationForm #urlPrefix').val()+"/storehouse/stock/pagedataQueryWlxxByWlid",
		    cache: false,
		    data: {"ckhwid":wlid,"access_token":$("#ac_tk").val()},
		    dataType:'json', 
		    success:function(data){
		    	//返回值
		    	if(data.ckhwxxDto!=null && data.ckhwxxDto!=''){
		    		//kcbj=1 有库存，kcbj=0 库存未0
		    		if(data.ckhwxxDto.kcbj!='1'){
		    			$.confirm("该物料库存不足！");
		    		}else{
			    		t_map.rows.push(data.ckhwxxDto);
			    		$("#allocationForm #tb_list").bootstrapTable('load',t_map);
			    		$('#allocationForm #addwlid').val("");
			    		$('#allocationForm #addnr').val("");
		    		}
		    	}else{
		    		$.confirm("该物料不存在!");
		    	}
		    }
		});
	}else{
		var addnr = $('#allocationForm #addnr').val();
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

	jQuery('#allocationForm .chosen-select').chosen({width: '100%'});
});