var t_map=[];
var PurchaseDownload_TableInit=function(){
	var qgid=$("#downloadpurchase_formSearch #qgid").val();
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#downloadpurchase_formSearch #tb_list").bootstrapTable({
			url: $("#downloadpurchase_formSearch #urlPrefix").val()+'/purchase/purchase/pagedataPurchaseInfo?qgid='+qgid+'&qxqg=0&zt=80',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#downloadpurchase_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "qgmx.xh",				//排序字段
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
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "qgmxid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
            	width: '3%',
                checkbox: true
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
                field: 'wlmc',
                title: '物料名称',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'wlbm',
                title: '物料编码',
                width: '8%',
                align: 'left',
                formatter:wlbmformat,
                sortable: true,
                visible: true
            },{
                field: 'wllbmc',
                title: '物料类别',
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
            },{
                field: 'wlzlbtcmc',
                title: '子类别统称',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'sl',
                title: '数量',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'jg',
                title: '价格',
                width: '5%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'qwdhrq',
                title: '物料使用日期',
                width: '7%',
                align: 'left',
                visible: true
            },{
                field: 'bz',
                title: '备注',
                width: '13%',
                align: 'left',
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
            },{
                field: 'fj',
                title: '附件',
                width: '13%',
                formatter:fjformat,
                align: 'left',
                visible: true
            }],
            onLoadSuccess:function(map){
            	t_map=map;
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
		})
	$("#downloadpurchase_formSearch #tb_list").colResizable({
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
           	sortLastName: "qgmx.qgmxid", // 防止同名排位用
           	 sortLastOrder: "asc" // 防止同名排位用
           	 // 搜索框使用
           	 // search:params.search
            };
	    return purchaseSearchData(map);
	};
	return oTableInit;
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
		html="<a href='javascript:void(0);' style='width:70%;line-height:24px;' onclick=\"queryByWlbm('"+row.wlid+"')\">"+row.wlbm+"</a><button type='button' class='glyphicon glyphicon-chevron-up' style='width:30%;font-weight:100;float:right;width:28px;height:24px;border-radius:5px;border:0.5px solid black;' onclick=\"showImg(\'echarts_qggl\','"+row.wlid+"',\'wlbm\')\"></button>";;
	}
	return html;
}

function queryByWlbm(wlid){
	var url=$("#downloadpurchase_formSearch #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
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


function fjformat(value,row,index){
	var html = "<div id='fj"+row.wlid+"'>"; 
	html += "<input type='hidden'>";
	if(row.fjbj == "0"){
		html += "<a href='javascript:void(0);' onclick='uploadShoppingFile(\"" + index + "\")' >是</a>";
	}else{
		html += "<a href='javascript:void(0);' onclick='uploadShoppingFile(\"" + index + "\")' >否</a>";
	}
	html += "</div>";
	return html;
}

//请购明细上传附件
function uploadShoppingFile(index) {
	var url = "/production/purchase/viewFilePage?access_token=" + $("#ac_tk").val();
	if(index){
		var qgid = $("#downloadpurchase_formSearch #qgid").val();
		var wlid = t_map.rows[index].wlid;
		var fjids = $("#downloadpurchase_formSearch #fj" + wlid+" input").val();
		var qgmxid = t_map.rows[index].qgmxid;
		if(!fjids){
			url="/production/purchase/pagedataGetUploadFile?access_token=" + $("#ac_tk").val()+"&qgmxid="+qgmxid+"&qgid="+qgid+"&wlid="+wlid+"&ckbj=1";
		}else{
			url="/production/purchase/pagedataGetUploadFile?access_token=" + $("#ac_tk").val()+"&qgmxid="+qgmxid+"&qgid="+qgid+"&wlid="+wlid+"&ckbj=1"+"&fjids="+fjids;
		}
	}
	$.showDialog($("#downloadpurchase_formSearch #urlPrefix").val()+url, '查看附件', viewFileConfig);
}

var viewFileConfig = { 
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

function view(fjid,wjm){
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin,end);
	if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
		var url="/ws/sjxxpripreview?fjid="+fjid
		$.showDialog($("#downloadpurchase_formSearch #urlPrefix").val()+url,'图片预览',JPGMaterConfig);
	}else if(type.toLowerCase()==".pdf"){
		var url= "/common/file/pdfPreview?fjid=" + fjid;
        $.showDialog($("#downloadpurchase_formSearch #urlPrefix").val()+url,'文件预览',JPGMaterConfig);
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
function xz(fjid){
    jQuery('<form action="'+$("#downloadpurchase_formSearch #urlPrefix").val()+'/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
            '<input type="text" name="fjid" value="'+fjid+'"/>' + 
            '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' + 
        '</form>')
    .appendTo('body').submit().remove();
}
function purchaseSearchData(map){
	var cxtj=$("#downloadpurchase_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#downloadpurchase_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["wlbm"]=cxnr
	}else if(cxtj=="1"){
		map["wlmc"]=cxnr
	}else if(cxtj=="2"){
		map["wllbmc"]=cxnr
	}else if(cxtj=="3"){
		map["wlzlbmc"]=cxnr
	}
	// 物料子类别统称
	var wlzlbtcs = jQuery('#downloadpurchase_formSearch #wlzlbtc_id_tj').val();
	map["wlzlbtcs"] = wlzlbtcs;
	return map;
}

function searchPurchaseResult(isTurnBack){
	if(isTurnBack){
		$('#downloadpurchase_formSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#downloadpurchase_formSearch #tb_list').bootstrapTable('refresh');
	}
}


function ztformat(value,row,index){
	var html="";
	if(row.zt=='15'){
		html="<span style='color:red;'>拒绝审批</span>";
	}else if(row.zt=='30'){
		html="<span style='color:orange;'>取消采购</span>";
	}else if(row.zt=='80'){
		html="<span style='color:green;'>正常采购</span>";
	}
	return html;
}

var Purchase_ButtonInit= function (){
	var oInit=new Object();
	var postdata = {};
	oInit.Init=function(){
		var btn_query=$("#downloadpurchase_formSearch #btn_query");
		/*--------------------------------模糊查询---------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchPurchaseResult(true); 
    		});
		}
		
	}
	return oInit;
}

$(function(){
	var oTable=new PurchaseDownload_TableInit();
	oTable.Init();
	
    var oButtonInit = new Purchase_ButtonInit();
    	oButtonInit.Init();
	jQuery('#downloadpurchase_formSearch .chosen-select').chosen({width: '100%'});
})