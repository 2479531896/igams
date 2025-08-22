/**
 * 刷新需求单号
 * @returns
 */
function refreshZldh(){
	$.ajax({ 
	    type:'post',  
	    url:$('#progress_produce_ajaxForm #urlPrefix').val() + "/progress/progress/pagedataRefreshZlNumber",
	    cache: false,
	    data: {"access_token":$("#ac_tk").val()},
	    dataType:'json',
	    success:function(result){
	    	$("#progress_produce_ajaxForm #zldh").val(result.zldh);
	    }
	});
}



var jhcls = 0;
var t_map = [];
var t_index="";
// 显示字段
var columnsArray = [
	{
	// 	checkbox: true,
	// 	width: '2%'
	// },{
		title: '序号',
		formatter: function (value, row, index) {
			return index+1;
		},
		titleTooltip:'序号',
		width: '3%',
		align: 'left',
		visible:true
	}, {
		field: 'wlbm',
		title: '物料编码',
		width: '6%',
		align: 'left',
		visible: true
	},  {
		field: 'wlmc',
		title: '物料名称',
		width: '4%',
		align: 'left',
		visible: true
	},  {
		field: 'scs',
		title: '生产商',
		width: '4%',
		align: 'left',
		visible: true
	}, {
		field: 'gg',
		title: '规格',
		width: '4%',
		align: 'left',
		visible: true
	},{
		field: 'jldw',
		title: '单位',
		width: '4%',
		align: 'left',
		visible: true
	},{
		field: 'scsl',
		title: '生产数量',
		width: '4%',
		align: 'left',
		visible: true
	},{
		field: 'yjwcsj',
		title: '预计完成时间',
		width: '6%',
		align: 'left',
		formatter: yjwcsjformat,
		visible: true,
	},{
		field: 'bz',
		title: '需求备注',
		width: '8%',
		align: 'left',
		visible: true
	},{
		field: 'xqjhid',
		title: '需求计划id',
		width: '8%',
		align: 'left',
		visible: false
	},{
		field: 'xqjhmxid',
		title: '需求计划明细id',
		width: '8%',
		align: 'left',
		visible: false
	},{
		field: 'wlid',
		title: '物料id',
		width: '8%',
		align: 'left',
		visible: false
	},{
		field: 'xlh',
		title: '序列号',
		width: '6%',
		align: 'left',
		formatter: produceForm_xlhformat,
		visible: true,
	}];
var progress_TableInit = function () {
	var oTableInit = new Object();
	//初始化Table
	oTableInit.Init = function () {
		$('#progress_produce_ajaxForm #progress_produce_tb_list').bootstrapTable({
			url: $('#progress_produce_ajaxForm #urlPrefix').val()+$('#progress_produce_ajaxForm #url').val(),         //请求后台的URL（*）
			method: 'get',                      //请求方式（*）
			toolbar: '#progress_produce_ajaxForm #toolbar',                //工具按钮用哪个容器
			striped: true,                      //是否显示行间隔色
			cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: false,                   //是否显示分页（*）
			sortable: true,                     //是否启用排序
			sortName: "xqjhmx.lrsj",					//排序字段
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
			uniqueId: "xqjhmx.xqjhmxid",                     //每一行的唯一标识，一般为主键列
			showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
			cardView: false,                    //是否显示详细视图
			detailView: false,                   //是否显示父子表
			columns: columnsArray,
			onLoadSuccess: function (map) {
				jhcls = map.rows[0].scsl*1;
                map.rows[0].scsl = map.rows[0].kscsl;
				$('#progress_produce_ajaxForm #jhcl').val(map.rows[0].kscsl*1);
				let cplxmc = $('#progress_produce_ajaxForm #cplxmc').val();
				if (cplxmc == "YQ"){
					let size =  map.rows[0].scsl;
					map.rows[0].scsl= 1;
					for (let i = 0; i < size*1-1; i++) {
						var sz = {"xqjhmxid":'',"xqjhid":'',"xlh":'',"wlid":'',"scsl":'',"wlbm":'',"wlmc":'',"scs":'',"gg":'',"jldw":'',"bz":'',"yjwcsj":''};
						sz.xqjhmxid = map.rows[0].xqjhmxid;
						sz.xqjhid = map.rows[0].xqjhid;
						sz.xlh = map.rows[0].xlh;
						sz.wlid = map.rows[0].wlid;
						sz.scsl = 1;
						sz.wlbm = map.rows[0].wlbm;
						sz.wlmc = map.rows[0].wlmc;
						sz.scs = map.rows[0].scs;
						sz.gg = map.rows[0].gg;
						sz.jldw = map.rows[0].jldw;
						sz.bz = map.rows[0].bz;
						sz.yjwcsj = map.rows[0].yjwcsj;
						map.rows.push(sz)
					}
				}
				t_map = map;
				$("#progress_produce_ajaxForm #progress_produce_tb_list").bootstrapTable('load',t_map);
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
			pageSize: 15,   //页面大小
			pageNumber: 1,  //页码
			access_token:$("#ac_tk").val(),
			sortName: params.sort,      // 排序列名
			sortOrder: params.order, // 排位命令（desc，asc）
			sortLastName: "xqjhmx.xqjhmxid", // 防止同名排位用
			sortLastOrder: "asc", // 防止同名排位用
			xqjhmxid: $("#progress_produce_ajaxForm #xqjhmxid").val(), // 防止同名排位用
		};
		return map;
	};
	return oTableInit;
};

function checkProduceValue(index, e, zdmc){
	if(zdmc=="xlh"){
		t_map.rows[index].xlh = e.value;
	}
	$("#progress_produce_ajaxForm #progress_produce_tb_list").bootstrapTable('load',t_map);
}
/**
 * 要求格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function produceForm_xlhformat(value,row,index){
	if(row.xlh == null){
		row.xlh = "";
	}
	var html = "<input id='xlh_"+index+"' autocomplete='off' value='"+row.xlh+"' name='xlh_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' validate='{required:true,stringMaxLength:32}' onchange=\"checkProduceValue('"+index+"',this,\'xlh\')\"></input>";
	return html;
}
/**
 * 预计完成时间格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function yjwcsjformat(value,row,index){
	if(row.yjwcsj == null){
		row.yjwcsj = "";
	}
	var html = "<input id='yjwcsj_"+index+"' value='"+row.yjwcsj+"' name='yjwcsj_"+index+"' style='width:85%;border-radius:5px;border:1px solid #D5D5D5;' validate='{required:true}' onchange=\"checkSum('"+index+"',this,\'yjwcsj\')\"><span class='glyphicon glyphicon-pencil' style='font-size:13px;color:#AAAAAA;width:10%;cursor:pointer;margin-left:3px;' title='复制当前日期并替换所有'  onclick=\"copyTime('"+index+"',\'yjwcsj\')\"><span></input>";
	setTimeout(function() {
		laydate.render({
			elem: '#progress_produce_ajaxForm #yjwcsj_'+index
			,theme: '#2381E9'
			,min: 1
			,btns: ['clear', 'confirm']
			,done: function(value, date, endDate){
				t_map.rows[index].yjwcsj = value;
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
function copyTime(index,zdmc) {
	var yjwcsj = $("#progress_produce_ajaxForm #" + zdmc + "_" + index).val();
	if (yjwcsj == null || yjwcsj == "") {
		$.confirm("请先选择日期！");
	} else {
		for (var i = 0; i < t_map.rows.length; i++) {
			$("#progress_produce_ajaxForm #" + zdmc + "_" + i).val(yjwcsj);
			t_map.rows[i].yjwcsj = yjwcsj;
		}
	}
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
		if (zdmc == 'yjwcsj') {
			t_map.rows[index].yjwcsj = e.value;
		}
	}
}

$(document).ready(function(){
	//添加日期控件
	laydate.render({
		elem: '#progress_produce_ajaxForm #zlrq'
		,theme: '#2381E9'
	});
	//初始化列表
	var oTable=new progress_TableInit();
	oTable.Init();
	//0.初始化fileinput
	var oFileInput = new FileInput();
	var picking_params=[];
	picking_params.prefix=$("#progress_produce_ajaxForm #urlPrefix").val();
	oFileInput.Init("progress_produce_ajaxForm","produceUpInfo",2,1,"pro_file",null,picking_params);
});
/**
 * 计划产量改变
 * @returns
 */
function jhclChoose(){
	let jhcl= $('#progress_produce_ajaxForm #jhcl').val();
	if (jhcl*1 <=0){
		$('#progress_produce_ajaxForm #jhcl').val("")
		$.alert("计划产量不能小于1！")
		return false;
	}
	let cplxmc = $('#progress_produce_ajaxForm #cplxmc').val();
	if (cplxmc == "YQ"){
		if (t_map.rows.length< jhcl*1){
			for (let i = t_map.rows.length; i < jhcl*1; i++) {
				var sz = {"xqjhmxid":'',"xqjhid":'',"xlh":'',"wlid":'',"scsl":'',"wlbm":'',"wlmc":'',"scs":'',"gg":'',"jldw":'',"bz":'',"yjwcsj":''};
				sz.xqjhmxid = t_map.rows[0].xqjhmxid;
				sz.xqjhid = t_map.rows[0].xqjhid;
				sz.xlh = t_map.rows[0].xlh;
				sz.wlid = t_map.rows[0].wlid;
				sz.scsl = 1;
				sz.wlbm = t_map.rows[0].wlbm;
				sz.wlmc = t_map.rows[0].wlmc;
				sz.scs = t_map.rows[0].scs;
				sz.gg = t_map.rows[0].gg;
				sz.jldw = t_map.rows[0].jldw;
				sz.bz = t_map.rows[0].bz;
				sz.yjwcsj = t_map.rows[0].yjwcsj;
				t_map.rows.push(sz);
			}
		}else{
			t_map.rows.splice(jhcl*1,t_map.rows.length*1-jhcl*1);
		}
	}else{
		t_map.rows[0].scsl = jhcl;
	}
	$("#progress_produce_ajaxForm #progress_produce_tb_list").bootstrapTable('load',t_map);

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
		var url=$("#progress_produce_ajaxForm #urlPrefix").val()+"/ws/sjxxpripreview/?fjid="+fjid
		$.showDialog(url,'图片预览',JPGMaterConfig);
	}else if(type.toLowerCase()==".pdf"){
		var url= $("#progress_produce_ajaxForm #urlPrefix").val()+"/common/file/pdfPreview?fjid=" + fjid;
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
 * 附件上传回调
 * @param fjid
 * @returns
 */
function produceUpInfo(fjid){
	if(!$("#progress_produce_ajaxForm #fjids").val()){
		$("#progress_produce_ajaxForm #fjids").val(fjid);
	}else{
		$("#progress_produce_ajaxForm #fjids").val($("#progress_produce_ajaxForm #fjids").val()+","+fjid);
	}
}
$(function(){
	//所有下拉框添加choose样式
	jQuery('#progress_produce_ajaxForm .chosen-select').chosen({width: '100%'});

});
