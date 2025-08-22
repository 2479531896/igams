var quality_TableInit = function () {
	//初始化Table
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#supplier_Form #zlxx_list").bootstrapTable({
			url: $("#supplier_Form #urlPrefix").val()+'/agreement/agreement/pagedataAgreement',         //请求后台的URL（*）
			method: 'get',                      //请求方式（*）
			toolbar: '#supplier_Form #toolbar',                //工具按钮用哪个容器
			striped: true,                      //是否显示行间隔色
			cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: false,                   //是否显示分页（*）
			paginationShowPageGo: false,         //增加跳转页码的显示
			sortable: true,                     //是否启用排序
			sortName: "zlxy.lrsj",				//排序字段
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
			uniqueId: "zlxyid",                     //每一行的唯一标识，一般为主键列
			showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
			cardView: false,                    //是否显示详细视图
			detailView: false,                   //是否显示父子表
			columns: [{
				title: '序',
				formatter: function (value, row, index) {
					return index+1;
				},
				titleTooltip:'序号',
				width: '2%',
				align: 'left',
				visible:true
			}, {
				field: 'zlxyid',
				title: '质量协议id',
				width: '8%',
				align: 'left',
				sortable: true,
				visible: false
			}, {
				field: 'zlxybh',
				title: '质量协议编号',
				width: '6%',
				align: 'left',
				sortable: true,
				visible: true,
				formatter:zlxybhformat
			},{
				field: 'cjsj',
				title: '创建时间',
				width: '4%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'kssj',
				title: '质量协议开始时间',
				width: '6%',
				align: 'left',
				sortable: true,
				visible: true
			}, {
				field: 'dqsj',
				title: '质量协议到期时间',
				width: '6%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: 'bz',
				title: '备注',
				width: '10%',
				align: 'left',
				sortable: true,
				visible: true
			},{
				field: '',
				title: '附件',
				titleTooltip:'附件',
				width: '8%',
				align: 'left',
				formatter:fjformat,
				visible: true
			},{
				field: '',
				title: '质量协议合同',
				titleTooltip:'质量协议合同',
				width: '6%',
				align: 'left',
				formatter:zlxyhtformat,
				visible: true
			}],
			onLoadSuccess:function(map){
			},
			onLoadError:function(){
				alert("数据加载失败！");
			},
		})
	};
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			pageSize: 1,   // 页面大小
			pageNumber: 1,  // 页码
			access_token:$("#ac_tk").val(),
			sortName: params.sort,      // 排序列名
			sortOrder: params.order, // 排位命令（desc，asc）
			sortLastName: "zlxy.xgsj", // 防止同名排位用
			sortLastOrder: "asc", // 防止同名排位用
			gysid: $("#supplier_Form #gysid").val() // 防止同名排位用
			// 搜索框使用
			// search:params.search
		};
		return map;
	};
	return oTableInit
}


//下载模板
function xz(fjid){
    jQuery('<form action="' + $("#supplier_Form #urlPrefix").val() + '/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
        '<input type="text" name="fjid" value="'+fjid+'"/>' +
        '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
        '</form>')
        .appendTo('body').submit().remove();
}
function fjformat(value,row,index) {
	return "<a href='javascript:void(0);' onclick=\"viewhtfj('"+row.zlxyid+"')\">"+"查看"+"</a>";
}
function zlxyhtformat(value,row,index) {
	return "<a href='javascript:void(0);' onclick=\"viewzlxyht('"+row.zlxyid+"')\">"+"查看"+"</a>";
}
function viewhtfj(ywid) {
	url="/agreement/agreement/pagedataViewFj?access_token=" + $("#ac_tk").val()+"&ywid="+ywid+"&ywlx="+"IMP_QUALITY_AGREEMENT_FILE";
	$.showDialog($("#supplier_Form #urlPrefix").val()+url, '查看附件', viewFileConfig);
}
function viewzlxyht(ywid) {
	url="/agreement/agreement/pagedataViewFj?access_token=" + $("#ac_tk").val()+"&ywid="+ywid+"&ywlx="+"IMP_UPQUALITYCONTRACT";
	$.showDialog($("#supplier_Form #urlPrefix").val()+url, '查看附件', viewFileConfig);
}

/**
 * 协议编号跳转
 */
function zlxybhformat(value,row,index){
	var html = "";
	if(row.zlxybh==null){
		html += "<span></span>"
	}else{
		html += "<a href='javascript:void(0);' onclick=\"queryByZlxybh('"+row.zlxyid+"')\">"+row.zlxybh+"</a>";
	}
	return html;
}
/**
 * 质量协议信息
 * @returns
 */
function queryByZlxybh(zlxyid){
	var url=$("#supplier_Form #urlPrefix").val()+"/agreement/agreement/viewAgreement?zlxyid="+zlxyid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'查看页面',viewZlxyConfig);
}
var viewZlxyConfig = {
	width		: "1200px",
	height		: "500px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var viewFileConfig = {
	width : "800px",
	height : "500px",
	modalName	: "uploadFileModal",
	formName	: "view_ajaxForm",
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

function yl(fjid,wjm){
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin,end);
	if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
		var url=$("#supplier_Form #urlPrefix").val()+"/ws/sjxxpripreview?fjid="+fjid
		$.showDialog(url,'图片预览',viewPreViewConfig);
	}else if(type.toLowerCase()==".pdf"){
		var url= $("#supplier_Form #urlPrefix").val()+"/common/file/pdfPreview?fjid=" + fjid;
        $.showDialog(url,'文件预览',viewPreViewConfig);
	}else {
		$.alert("暂不支持其他文件的预览，敬请期待！");
	}
}
var viewPreViewConfig = {
	width        : "900px",
	height        : "800px",
	offAtOnce    : true,  //当数据提交成功，立刻关闭窗口
	buttons        : {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
$(function(){
	//0.界初始化
	//1.初始化Table
	var oTable = new quality_TableInit();
	oTable.Init();
})