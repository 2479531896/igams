function refreshJydh(){
	var url = $("#inspectionModForm #urlPrefix").val()+"/inspectionGoods/pendingInspection/pagedataRefreshJydh"
	$.ajax({
		type : "get",
		url: url, 
		data : {"access_token":$("#ac_tk").val()},
        dataType : "json",
        success:function(data){
        	$("#inspectionModForm #jydh").val(data.jydh);
        	}
        });
}
/**
 * 选择负责人列表
 * @returns
 */
function selectjyfzr(){
	var url=$('#inspectionModForm #urlPrefix').val() + "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
	$.showDialog(url,'选择负责人',addFzrConfig);	
}
var addFzrConfig = {
	width		: "800px",
	modalName	:"addFzrModal",
	offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==1){
					$('#inspectionModForm #jyfzr').val(sel_row[0].yhid);
					$('#inspectionModForm #jyfzrname').val(sel_row[0].zsxm);
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
function displayUpInfo(fjid){
	if(!$("#inspectionModForm #fjids").val()){
		$("#inspectionModForm #fjids").val(fjid);
	}else{
		$("#inspectionModForm #fjids").val($("#inspectionModForm #fjids").val()+","+fjid);
	}
}
var dhjyhwList_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#inspectionModForm #dhjyhwList').bootstrapTable({
            url: $('#inspectionModForm #urlPrefix').val()+'/inspectionGoods/pendingInspection/pagedataGetGoodsFromDhjyid',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#inspectionModForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "wlbm",				//排序字段
            sortOrder: "desc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            isForceTable: true,
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: false,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "wlbm",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
            onLoadSuccess: function (map) {
            	
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	return;
            },
        });
    };
    // 得到查询的参数
	oTableInit.queryParams = function(params){
		var map = {
				access_token:$("#ac_tk").val(),
				dhjyid:$("#dhjyid").val()
		};
		return map;
	};
    return oTableInit;
};
var columnsArray = [
	{
    	title: '序号',
		formatter: function (value, row, index) {
			return index+1;
		},
		titleTooltip:'序号',
		width: '3%',
        align: 'left',
        visible:true
    }, {
    	field: 'htnbbh',
    	title: '合同编号',
    	titleTooltip:'合同编号',
    	width:'8%',
    	align:'left',
    	visible:true
    },{
    	field: 'wlbm',
    	title: '物料编码',
    	titleTooltip:'物料编码',
    	width:'8%',
    	align:'left',
    	formatter:wlbmformat,
    	visible:true
    },{
    	field: 'wlmc',
    	title: '物料名称',
    	titleTooltip:'物料名称',
    	width:'10%',
    	align:'left',
    	visible:true
    },{
    	field: 'gg',
    	title: '物料规格',
    	titleTooltip:'物料规格',
    	width:'7%',
    	align:'left',
    	visible:true
    },{
    	field: 'jldw',
    	title: '计量单位',
    	titleTooltip:'计量单位',
    	width:'7%',
    	align:'left',
    	visible:true
    },{
    	field: 'ychh',
    	title: '货号',
    	titleTooltip:'货号',
    	width:'7%',
    	align:'left',
    	visible:true
    },{
    	field: 'sl',
    	title: '数量',
    	titleTooltip:'数量',
    	width:'6%',
    	align:'left',
    	visible:true
    },{
    	field: 'bgdh',
    	title: '报告单号',
    	titleTooltip:'报告单号',
    	width:'7%',
    	align:'left',
    	visible:true,
    	formatter:bgdhformat
    },{
    	field: 'qyl',
    	title: '取样量',
    	titleTooltip:'取样量',
    	width:'7%',
    	align:'left',
    	visible:true,
    	formatter:qylformat
    },{
    	field: 'jysl',
    	title: '借用数量',
    	titleTooltip:'借用数量',
    	width:'7%',
    	align:'left',
    	visible:true
    },{
    	field: 'qyrq',
    	title: '取样日期',
    	titleTooltip:'取样日期',
    	width:'10%',
    	align:'left',
    	visible:true,
    	formatter:qyrqformat
    },{
    	field: 'zjthsl',
    	title: '不合格数量',
    	titleTooltip:'不合格数量',
    	width:'7%',
    	align:'left',
    	visible:true,
    	formatter:cythslformat
    },{
        field: 'lysl',
        title: '留样数量',
        titleTooltip:'留样数量',
        width:'7%',
        align:'left',
        visible:true,
        formatter:lyslformat
      },{
        field: 'fj',
        title: '附件',
        titleTooltip:'附件',
        width: '5%',
        align: 'left',
        formatter:fjformat,
        visible: true
    },{
    	field: 'hwid',
    	title: '货物id',
    	titleTooltip:'货物id',
    	width:'7%',
    	align:'left',
    	visible:false
    },{
    	field: 'wlid',
    	title: '物料id',
    	titleTooltip:'物料id',
    	width:'7%',
    	align:'left',
    	visible:false
    }
    
];
/**
 * 附件格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function fjformat(value,row,index){
	var html = "<div id='fj_"+index+"'>"; 
	html += "<input type='hidden' id='hwfj_"+index+"' name='hwfj_"+index+"'/>";
	if(row.fjbj == "0"){
		html += "<a href='javascript:void(0);' onclick='uploadShoppingFile(\"" + index + "\",\""+row.hwid+"\")' >是</a>";
	}else{
		html += "<a href='javascript:void(0);' onclick='uploadShoppingFile(\"" + index + "\",\""+row.hwid+"\")' >否</a>";
	}
	
	
	html += "</div>";
	return html;
}
/**
 * 货物质量检验上传附件
 * @param index
 * @returns
 */
var currentUploadIndex = "";
function uploadShoppingFile(index,hwid) {
	if(index){
		var dhjyid = $("#inspectionModForm #dhjyid").val();
		var fjids = $("#inspectionModForm #fj_"+ index +" input").val();
		url=$('#inspectionModForm #urlPrefix').val()+"/inspectionGoods/pendingInspection/pagedataGetUploadFilePage?access_token=" + $("#ac_tk").val()+"&ywid="+dhjyid+"&zywid="+hwid;
		if(fjids){
			url=url + "&fjids="+fjids;
		}
		currentUploadIndex = index;
	}
	$.showDialog(url, '修改附件', uploadFileConfig);
}
var uploadFileConfig = { 
	width : "800px",
	height : "500px",
	modalName	: "uploadFileModal",
	formName	: "ajaxForm",
	offAtOnce : true, // 当数据提交成功，立刻关闭窗口
	buttons : {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var $this = this;
				var opts = $this["options"]||{};
				if($("#ajaxForm #fjids").val()){
					var zywid=$("#ajaxForm #zywid").val();
					if(!$("#inspectionModForm #fj_"+currentUploadIndex+" input").val()){
						$("#inspectionModForm #fj_"+currentUploadIndex+" input").val($("#ajaxForm #fjids").val());
					}else{
						$("#inspectionModForm #fj_"+currentUploadIndex+" input").val($("#inspectionModForm #fj_"+currentUploadIndex+" input").val() + "," + $("#ajaxForm #fjids").val());
					}
					$("#inspectionModForm #fj_"+currentUploadIndex+" a").text("是");
					$('#inspectionModForm #dhjyhwList').bootstrapTable('getData')[currentUploadIndex].fjbj="0";
				}else{
					//判断正式文件是否存在
					var dom = $("#ajaxForm div[name='formalFile']").html();
					if(dom == null || dom == "" || dom == undefined){
						$("#inspectionModForm #fj_"+currentUploadIndex+" input").val("");
						$("#inspectionModForm #fj_"+currentUploadIndex+" a").text("否");
						$('#inspectionModForm #dhjyhwList').bootstrapTable('getData')[currentUploadIndex].fjbj="1";
					}

				}
				$.closeModal(opts.modalName);
				return true;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
//报告单号格式化
function bgdhformat(value,row,index){
	var bgdh = "<input id='bgdh_"+index+"' autocomplete='off' name='bgdh_"+index+"' validate='{stringMaxLength:64}' style='width: 100%;' value='"+(row.bgdh==null?"":row.bgdh)+"'/>";
	bgdh=bgdh+ "<input type='hidden' id='rklbdm_"+index+"' autocomplete='off' name='rklbdm_"+index+"' validate='{stringMaxLength:64}' style='width: 100%;' value='"+(row.rklbdm==null?"":row.rklbdm)+"'/>";
	return bgdh;
}
//取样量格式化
function qylformat(value,row,index){
	var qyl = "<input id='qyl"+index+"' type='number' autocomplete='off' name='qyl"+index+"' min='0' max='"+row.sl+"' style='width: 100%;' value='"+(row.qyl==null?"0":row.qyl)+"'/>";
	return qyl;
}

function qyrqformat(value,row,index){
	if(row.qyrq==null){
		row.qyrq="";
	}
	var html="<input id='qyrq"+index+"' hwid='"+row.hwid+"' autocomplete='off' name='qyrq_"+index+"' class='qyrq'  value='"+row.qyrq+"' style='width:85%;border-radius:5px;border:1px solid #D5D5D5;' ></input><span class='glyphicon glyphicon-pencil' style='font-size:13px;color:#AAAAAA;width:10%;cursor:pointer;margin-left:3px;' title='复制当前日期并替换所有'  onclick=\"copyTime('"+index+"')\"><span>";
	setTimeout(function() {
		laydate.render({
			elem: '#inspectionModForm #qyrq'+index
		  	,theme: '#2381E9'
		  	,btns: ['clear', 'confirm']
	  		,done: function(value, date, endDate){
	  			$('#inspectionModForm #qyrq'+index).val(value);
	  		  }
		});
    }, 100);
	return html;
}
/**
 * 复制当前日期并替换全部
 * @param wlid
 * @returns
 */
function copyTime(index){
	var dqrq=$("#inspectionModForm #qyrq"+index).val();
	if(dqrq==null || dqrq==""){
		$.confirm("请先选择日期！");
	}else{
		var allTableData = $('#inspectionModForm #dhjyhwList').bootstrapTable('getData');
		for(var i=0;i<allTableData.length;i++){
			$("#inspectionModForm #qyrq"+i).val(dqrq);
		}
	}
}
//初验退回数量
function cythslformat(value,row,index){
	var zjthsl = "<input id='zjthsl"+index+"' autocomplete='off' name='zjthsl"+index+"' type='number' min='0' max='"+row.sl+"' style='width: 100%;' value='"+(row.zjthsl==null?"":row.zjthsl)+"'/>";
	return zjthsl;
}
function lyslformat(value,row,index){
	var lysl = "<input id='lysl"+index+"' type='number' autocomplete='off' name='lysl"+index+"' min='0' max='"+row.sl+"' style='width: 100%;' value='"+(row.lysl==null?"0":row.lysl)+"'/>";
	return lysl;
}

//操作，删除
function jycczFormat(value,row,index){
	return "<div ><span class='btn btn-danger' title='移除' onclick=\"removejychw('" + row.hwid + "',event)\" >移除</span></div>";
}
var delhwids = [];
function removejychw(hwid){
	$('#inspectionModForm #dhjyhwList').bootstrapTable('remove', {field: 'hwid', values: hwid});
	delhwids.push(hwid);
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
	var url=$("#inspectionModForm #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
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

$(function () {
	delhwids=[];
	//添加日期控件
	laydate.render({
	   elem: '#jyrq',
	   theme: '#2381E9',
	   value:$("#jyrq").val()==""?new Date():$("#jyrq").val()
	});
	if($("#jydh").val()==""){
		refreshJydh();
	}
	//当前用户
	if($("#jyfzr").val()==""){
		$("#jyfzr").val($("#currentUser").val());
		$("#jyfzrname").val($("#currentUserName").val());
	}
	var sflyck = $("#inspectionModForm #nsflyck").val();
	if (sflyck==null||sflyck==''){
		$('input:radio[name="sflyck"][value="0"]').prop('checked', true);
	}
	var sffqlld = $("#inspectionModForm #nsffqlld").val();
	if (sffqlld==null||sffqlld==''){
		$('#inspectionModForm #sffqlld').val("0")
	}
	//0.初始化fileinput
	/*var oFileInput = new FileInput();
	var sign_params=[];
	sign_params.prefix=$("#inspectionModForm #urlPrefix").val();
	oFileInput.Init("inspectionModForm","displayUpInfo",2,1,"inspection_file",null,sign_params);*/
	var tableInit = new dhjyhwList_TableInit();
	tableInit.Init();
	jQuery('#inspectionModForm .chosen-select').chosen({width: '100%'});
});