var SampleExperiment_turnOff=true;
var SampleExperiment_TableInit = function (fieldList,firstFlg) {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function (){
        $('#sjsygl_formSearch #sjsygl_list').bootstrapTable({
            url: '/sampleExperiment/sampleExperiment/pageGetListSampleExperiment'+($('#sjsygl_formSearch #single_flag').val()?('?single_flag='+$('#sjsygl_formSearch #single_flag').val()):''),         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#sjsygl_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "sjsygl.jsrq desc,sjsygl.lrsj ",//排序字段
            sortOrder:"DESC",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 50,                       //每页的记录行数（*）
            pageList: [15, 30, 50, 100],        //可供选择的每页的行数（*）
            paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "syglid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: fieldList,
            onLoadSuccess: function (map) {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
           onDblClickRow: function (row, $element) {
			   SampleExperimentDealById(row.sjid,null,'view',$("#sjsygl_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#sjsygl_formSearch #sjsygl_list").colResizable({
            liveDrag:true, 
            gripInnerHtml:"<div class='grip'></div>", 
            draggingClass:"dragging", 
            resizeMode:'fit', 
            postbackSafe:true,
            partialRefresh:true,
            isFirst:firstFlg}        
        );
    };
	// 得到查询的参数
	oTableInit.queryParams = function(params){
	// 请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
	// 例如 toolbar 中的参数
	// 如果 queryParamsType = ‘limit’ ,返回参数必须包含
	// limit, offset, search, sort, order
	// 否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder.
	// 返回false将会终止请求
    var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
    	pageSize: params.limit,   // 页面大小
    	pageNumber: (params.offset / params.limit) + 1,  // 页码
        access_token:$("#ac_tk").val(),
        sortName: params.sort,      // 排序列名
        sortOrder: params.order, // 排位命令（desc，asc）
        sortLastName: "sjsygl.syglid", // 防止同名排位用
        sortLastOrder: "asc", // 防止同名排位用
		limitColumns: $("#sjsygl_formSearch #limitColumns").val(),
    };
    return getSjsyglSearchData(map);
	};
    return oTableInit;
}

function getSjsyglSearchData(map){
	var cxtj=$("#sjsygl_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#sjsygl_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["hzxm"]=cxnr
	}else if(cxtj=="1"){
		map["db"]=cxnr
	}else if(cxtj=="2"){
		map["hospitalname"]=cxnr
	}else if(cxtj=="3"){
		map["cskz5"]=cxnr
	}else if(cxtj=="4"){
		map["zyh"]=cxnr
	}else if(cxtj=="5"){
		map["ybbh"]=cxnr
	}else if(cxtj=="6"){
		map["nbbm"]=cxnr
	}else if(cxtj=="7"){
		map["xsrymc"]=cxnr
	}else if(cxtj=="8"){
		map["lcfk"]=cxnr
	}else if(cxtj=="9"){
		map["kdh"]=cxnr
	}else if(cxtj=="10"){
		map["sjys"]=cxnr
	}else if(cxtj=="11"){
		map["kyxm"]=cxnr
	}else if(cxtj=="12"){
		map["jcjg"]=cxnr
	}else if(cxtj=="13"){
		map["wksxbm"]=cxnr
	}
		// 接收日期开始日期
	var jsrqstart = jQuery('#sjsygl_formSearch #jsrqstart').val();
		map["jsrqstart"] = jsrqstart;
	    // 接收日期结束日期
	var jsrqend = jQuery('#sjsygl_formSearch #jsrqend').val();
		map["jsrqend"] = jsrqend;
	    // 报告日期开始日期
	var bgrqstart = jQuery('#sjsygl_formSearch #bgrqstart').val();
		map["bgrqstart"] = bgrqstart;
	    // 报告日期结束日期
	var bgrqend = jQuery('#sjsygl_formSearch #bgrqend').val();
		map["bgrqend"] = bgrqend;
		// 起运日期开始时间
	var qyrqstart = jQuery('#sjsygl_formSearch #qyrqstart').val();
		map["qyrqstart"] = qyrqstart;
	    // 起运日期结束时间
	var qyrqend = jQuery('#sjsygl_formSearch #qyrqend').val();
		map["qyrqend"] = qyrqend;
		// 运达日期开始时间
	var ydrqstart = jQuery('#sjsygl_formSearch #ydrqstart').val();
		map["ydrqstart"] = ydrqstart;
	    // 运达日期结束时间
	var ydrqend = jQuery('#sjsygl_formSearch #ydrqend').val();
		map["ydrqend"] = ydrqend;
	// 付款日期开始时间
	var fkrqstart = jQuery('#sjsygl_formSearch #fkrqstart').val();
		map["fkrqstart"] = fkrqstart;
	// 付款日期结束时间
	var fkrqend = jQuery('#sjsygl_formSearch #fkrqend').val();
		map["fkrqend"] = fkrqend;
	//科室
	var dwids = jQuery('#sjsygl_formSearch #sjdw_id_tj').val();
		map["dwids"] = dwids;
	//合作伙伴分类
	var sjhbfls = jQuery('#sjsygl_formSearch #sjhbfl_id_tj').val();
		map["sjhbfls"] = sjhbfls;
	//合作伙伴子分类
	var sjhbzfls = jQuery('#sjsygl_formSearch #sjhbzfl_id_tj').val();
		map["sjhbzfls"] = sjhbzfls;
	//标本类型
	var yblxs = jQuery('#sjsygl_formSearch #yblx_id_tj').val();
		map["yblxs"] = yblxs;	
	//是否合作关系
	var dbm= jQuery('#sjsygl_formSearch #dbm_id_tj').val();
		map["dbm"] = dbm;
	//是否付款
	var fkbj=jQuery('#sjsygl_formSearch #fkbj_id_tj').val()
		map["fkbj"]=fkbj;
	//是否上传
	var sfsc=jQuery('#sjsygl_formSearch #sfsc_id_tj').val()
	    map["sfsc"]=sfsc;
	//是否检测
	var jcbj=jQuery('#sjsygl_formSearch #jcbj_id_tj').val()
	map["jcbj"]=jcbj;
	//是否自免检测
	var sfzmjc=jQuery('#sjsygl_formSearch #sfzmjc_id_tj').val()
	map["sfzmjc"]=sfzmjc;
	var sfjs=jQuery('#sjsygl_formSearch #sfjs_id_tj').val()
		map["sfjs"]=sfjs;
	var sftj=jQuery('#sjsygl_formSearch #sftj_id_tj').val()
		map["sftj"]=sftj;
	//扩展参数
	var sjkzcs1=jQuery('#sjsygl_formSearch #cskz1_id_tj').val()
		map["sjkzcs1"]=sjkzcs1
	var sjkzcs2=jQuery('#sjsygl_formSearch #cskz2_id_tj').val()
		map["sjkzcs2"]=sjkzcs2
	var sjkzcs3=jQuery('#sjsygl_formSearch #cskz3_id_tj').val()
		map["sjkzcs3"]=sjkzcs3
	var sjkzcs4=jQuery('#sjsygl_formSearch #cskz4_id_tj').val()
		map["sjkzcs4"]=sjkzcs4
	var jcxm=jQuery('#sjsygl_formSearch #jcxm_id_tj').val()
		map["jcxms"]=jcxm
	var jyjgs=jQuery('#sjsygl_formSearch #jyjg_id_tj').val()
		map["jyjgs"]=jyjgs
	var sfsfs=jQuery('#sjsygl_formSearch #sfsf_id_tj').val()
		map["sfsfs"]=sfsfs
	var sfzq=jQuery('#sjsygl_formSearch #sfzq_id_tj').val();
		map["sfzq"]=sfzq
		//快递类型
	var kdlxs=$("#sjsygl_formSearch #kdlx_id_tj").val();
		map["kdlxs"]=kdlxs
		//盖章类型
	var gzlxs=$("#sjsygl_formSearch #gzlx_id_tj").val();
		map["gzlxs"]=gzlxs
		//检测单位
	var jcdws=$("#sjsygl_formSearch #jcdw_id_tj").val();
		map["jcdws"]=jcdws
	//科研项目类型
	var kylxs=$("#sjsygl_formSearch #kylx_id_tj").val();
	map["kylxs"]=kylxs
	//科研项目
	var kyxms=$("#sjsygl_formSearch #kyxm_id_tj").val();
	map["kyxms"]=kyxms
	//送检区分
	var sjqfs=$("#sjsygl_formSearch #sjqf_id_tj").val();
	map["sjqfs"]=sjqfs
	//医院重点等级
	var dwzddjs=$("#sjsygl_formSearch #dwzddj_id_tj").val().replace("＋","+");
	map["yyzddjs"]=dwzddjs
		// 实验日期开始日期
	var syrqstart = jQuery('#sjsygl_formSearch #syrqstart').val();
		map["syrqstart"] = syrqstart;
	    // 实验日期结束日期
	var syrqend = jQuery('#sjsygl_formSearch #syrqend').val();
		map["syrqend"] = syrqend;
	map["jcxmids"] = $("#sjsygl_formSearch #jcxmids").val();
	 return map;
}
/**
 * 状态
 * @returns
 */
function ztformat(value,row,index) {
	if (row.zt == '00' || row.zt == '  ' || !row.zt) {
		return '未提交';
	}else if (row.zt == '80') {
		return "<span>审核通过</span>";
	}else if (row.zt == '15') {
		return "<span>审核未通过</span>";
	}else{
		return "<span>审核中</span>";
	}
}

/**
 * 类型
 * @returns
 */
function lxformat(value,row,index) {
	if (row.lx == 'DETECT_FJ' ) {
		return '复测加测';
	}else if (row.lx == 'DETECT_SJ') {
		return '正常送检';
	}
}
/*
 * 查看审核信息
 * @param xmid
 * @param event
 * @param shlb
 */
function showAuditInfo(ywid,event,shlb,params){
	if(shlb.split(",").length>1){
		$.showDialog(
			(params?(params.prefix?params.prefix:""):"") + auditViewUrl,
			'审核信息',
			$.extend({},viewConfig,{"width":"960px",data:{'ywid':ywid,'shlbs':shlb}})
		);
	}else{
		$.showDialog(
			(params?(params.prefix?params.prefix:""):"") + auditViewUrl,
			'审核信息',
			$.extend({},viewConfig,{"width":"960px",data:{'ywid':ywid,'shlb':shlb}})
		);
	}
	event.stopPropagation();
}
//最终金额格式化
function zzjeformat(value,row,index){
	if(row.sfje != null && row.sfje != '' ){
		if ( row.tkje != null && row.tkje != ''){
			return ((parseInt(row.sfje*100) - parseInt(row.tkje*100))/100).toFixed(2);
		}else {
			return row.sfje;
		}
	}
}
  //性别格式化
function xbformat(value,row,index){
	if(row.xb=='1'){
		return '男';
	}else if(row.xb=='2'){
		return '女';
	}else{
		return '未知';
	}
}
//RNA检测标记格式化
function jcbjformat(value,row,index){
	if(row.jcxmkzcs=="D" || row.jcxmkzcs=="Z" || row.jcxmkzcs=="F"){
		var sfjc="<span style='color:red;'>--</span>";
		return sfjc;
	}else {
		if(row.jcbj=='0'|| row.jcbj==null){
			var sfjc="<span style='color:red;'>否</span>";
			return sfjc;
		}else if(row.jcbj=='1'){
			return '是';
		}
	}
}
//检验结果格式化
function jyjgformat(value,row,index){
	if(row.jyjg=="1"){
		var jyjg="<span style='color:red;font-weight:bold;'>阳性</span>";
		return jyjg;
	}else if(row.jyjg=="2"){
		var jyjg="<span style='color:#0000cc;font-weight:bold;'>疑似</span>";
		return jyjg;
	}else if(row.jyjg=="0"){
		var jyjg="<span style='font-weight:bold;'>阴性</span>";
		return jyjg;
	}
}
//DNA检测标记格式化
function djcbjformat(value,row,index){
	if(row.jcxmkzcs=="R" || row.jcxmkzcs=="Z" || row.jcxmkzcs=="F"){
		var dsfjc="<span style='color:red;'>--</span>";
		return dsfjc;
	}else{
		if(row.djcbj=='0'|| row.djcbj==null){
			var dsfjc="<span style='color:red;'>否</span>";
			return dsfjc;
		}else if(row.djcbj=='1'){
			return '是';
		}
	}
}
//其他检测标记格式化
function qtjcbjformat(value,row,index){
	if(row.jcxmkzcs!="Z" && row.jcxmkzcs!="F"){
		var qtsfjc="<span style='color:red;'>--</span>";
		return qtsfjc;
	}else{
		if(row.qtjcbj=='0'|| row.qtjcbj==null){
			var qtsfjc="<span style='color:red;'>否</span>";
			return qtsfjc;
		}else if(row.qtjcbj=='1'){
			return '是';
		}
	}
}
//是否自免检测标记格式化
function sfzmjcformat(value,row,index){
	if(row.sfzmjc=="1"){
		var sfzmjc="<span>是</span>"
		return sfzmjc;
	}else if(row.sfzmjc=='0'|| row.sfzmjc==null){
		var sfzmjc="<span style='color:red;'>否</span>";
		return sfzmjc;
	}
}
function sfjsformat(value,row,index){
	if(row.sfjs=='0'){
		var sfjs="<span style='color:red;'>否</span>";
		return sfjs;
	}else if(row.sfjs=='1'){
		return '是';
	}else if(row.sfjs=='2'){
		return '其他';
	}
}

function sfvipformat(value,row,index){
	if(row.sfvip=='0'){
		var sfvip="<span style='color:red;'>否</span>";
		return sfvip;
	}else if(row.sfvip=='1'){
		return '是';
	}
}
function sftjformat(value,row,index){
	if(row.sftj=='0'){
		var sftj="<span style='color:red;'>否</span>";
		return sftj;
	}else if(row.sftj=='1'){
		return '是';
	}
}
//付款标记格式化
function fkbjformat(value,row,index){
	if(row.fkbj=='1'){
		return '是';
		
	}else{
		return '否';
	}
}
//是否收費格式化
function sfsfformat(value,row,index){
	if(row.sfsf=='1'){
		return '是';
	}else if(row.sfsf=='0'){
		var sfsf="<span style='color:red;'>否</span>"
		return sfsf;
	}else if(row.sfsf=='2'){
		var sfsf="<span style='color:#8E388E;font-weight:bold'>免            D</span>"
		return sfsf;
	}else if(row.sfsf=='3'){
		var sfsf="<span style='color:#8470FF;font-weight:bold'>免         R</span>"
		return sfsf;
	}
}
	//是否上传格式化
function wjformat(value,row,index){
	if(row.sfsc=="1"){
		var sfsc="<span>是</span>"
		return sfsc;
	}else{
		var sfsc="<span style='color:red;'>否</span>"
		return sfsc;
	}
}
//序号格式化
function xhformat(value, row, index) {
	//获取每页显示的数量
	var pageSize=$('#sjsygl_formSearch #sjsygl_list').bootstrapTable('getOptions').pageSize;
	//获取当前是第几页
	var pageNumber=$('#sjsygl_formSearch #sjsygl_list').bootstrapTable('getOptions').pageNumber;
	//返回序号，注意index是从0开始的，所以要加上1
	return pageSize * (pageNumber - 1) + index + 1;
}
/**
 * 反馈结果格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function lcfkformat(value,row,index){
	if(row.sfzq=="1"){
		var sfzq="<span>正确</span>"
			return sfzq;
	}else if(row.sfzq=="0"){
		var sfzq="<span style='color:red;'>错误</span>"
			return sfzq;
	}else{
		var sfzq="<span'>未反馈</span>"
			return sfzq;
	}
}

// 按钮动作函数
function SampleExperimentDealById(id,ks,action,tourl,lx,jcxmid,jczxmid){
	var url= tourl;
	if(!tourl){ return; }
	if(action =='view'){
		var url= tourl + "?sjid=" +id +"&ks=" +ks;
		$.showDialog(url,'送检详细信息',	viewSjxxConfig);
	}else if(action =='confirm'){
		if(id!=null){
			var url= tourl+"?sjid="+id;
		}else{
			var url= tourl;
		}
		$.showDialog(url,'送检收样确认',confirmInspectionConfigSy);
	}else if(action == 'adjust'){
		var url=tourl + "?ids=" +id;
		$.showDialog(url,'标本实验调整',adjustBbsyConfig);
	}else if(action =='mod'){
		var url=tourl + "?sjid=" +id+"&xg_flg=1";
		$.showDialog(url,'修改送检信息',modSjxxConfig);
	}else if(action =='add'){
		var url=tourl + "?sjid=" +id+"&lx="+lx+"&jcxmid="+jcxmid+"&jczxmid="+jczxmid;
		$.showDialog(url,'新增送检实验',addBbsyConfig);
	}
}


var addBbsyConfig = {
	width		: "1000px",
	modalName	:"addBbsyModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var sel_row = $('#addBbsyForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
				if(sel_row.length==0){
					$.error("请至少选择一行");
					return false;
				}else{
					var syList=JSON.parse($('#addBbsyForm #json').val());
					var json=[];
					for(var i=0;i<sel_row.length;i++){
						for(var j=0;j<syList.length;j++){
							if(sel_row[i].dydm==syList[j].nbzbm&&sel_row[i].kzcs1==syList[j].kzcs1&&sel_row[i].kzcs2==syList[j].kzcs2&&sel_row[i].kzcs3==syList[j].kzcs3
								&&sel_row[i].kzcs4==syList[j].kzcs4&&sel_row[i].kzcs5==syList[j].kzcs5){
								$.error("所选数据中存在与已有信息字段全部相同的情况");
								return false;
							}
						}
						var sz={"dyid":'',"sjid":'',"jcdw":'',"jcxmid":'',"jczxmid":'',"jclxid":'',"nbzbm":'',"lx":''};
						sz.dyid=sel_row[i].dyid;
						sz.sjid= $('#addBbsyForm #sjid').val();
						sz.jcdw=$('#addBbsyForm #jcdw').val();
						sz.lx=$('#addBbsyForm #lx').val();
						sz.jcxmid=$('#addBbsyForm #jcxmid').val();
						sz.jczxmid=$('#addBbsyForm #jczxmid').val();
						sz.jclxid=sel_row[i].dyxx;
						sz.nbzbm=sel_row[i].dydm;
						json.push(sz);
					}
					$("#addBbsyForm #bbsy_json").val(JSON.stringify(json));
				}

				var $this = this;
				var opts = $this["options"]||{};
				$("#addBbsyForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"addBbsyForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							searchSjsyglResult();
						});
					}else if(responseText["status"] == "fail"){
						$.error(responseText["message"],function() {
						});
					} else{
						$.alert(responseText["message"],function() {
						});
					}
				});
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
/*   修改送检信息模态框*/
var modSjxxConfig = {
	width		: "1000px",
	modalName	: "modSjxxModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				$("#ajaxForm #syfjInfoTab #ywlx").remove();
				$("#ajaxForm #db").val($("#ajaxForm #db").val().replace(/\s+/g,""));

				if($("#ajaxForm #ybbh").val()==""||$("#ajaxForm #ybbh").val()==null){
					$.alert("请输入标本编号！");
					return false;
				}else if($("#ajaxForm #hzxm").val()==""||$("#ajaxForm #hzxm").val()==null){
					$.alert("请输入患者姓名！");
					return false;
				}else if($("#ajaxForm input:radio[name='xb']:checked").val()==null){
					$.alert("请选择性别！");
					return false;
				}else if($("#ajaxForm #nl").val()==""||$("#ajaxForm #nl").val()==null){
					$.alert("请输入年龄！");
					return false;
				}else if($("#ajaxForm #hospitalname").val()==""||$("#ajaxForm #hospitalname").val()==null){
					$.alert("请选择医院名称！");
					return false;
				}else if($("#ajaxForm #sjdw").val()==""||$("#ajaxForm #sjdw").val()==null){
					$.alert("医院名称不正确！");
					return false;
				}else if($("#ajaxForm #sjdwmc").val()==""||$("#ajaxForm #sjdwmc").val()==null){
					$.alert("请输入报告显示单位！");
					return false;
				}else if($("#ajaxForm #sjys").val()==""||$("#ajaxForm #sjys").val()==null){
					$.alert("请输入主治医师！");
					return false;
				}else if($("#ajaxForm #ks option:selected").text()=="--请选择--"){
					$.alert("请选择科室！");
					return false;
				}else if($("#ajaxForm #qqzd").val()==""||$("#ajaxForm #qqzd").val()==null){
					$.alert("请输入前期诊断！");
					return false;
				}else if($("#ajaxForm #db").val()==""||$("#ajaxForm #db").val()==null){
					$.alert("请输入合作伙伴！");
					return false;
				}else if($("#ajaxForm .jcxm").length >0){
					var json = [];
					for (let i = 0; i <= $("#ajaxForm .jcxm").length -1; i++) {
						if ($("#ajaxForm #"+$("#ajaxForm .jcxm")[i].id).is(':checked')){
							var sz = {"xmglid":'',"jcxmid":'',"jczxmid":'',"flag":'',"csmc":'',"zcsmc":'',"sfsf":'',"yfje":'',"sfje":'',"fkrq":''};
							sz.jcxmid = $("#ajaxForm .jcxm")[i].id;
							sz.jczxmid = null;
							sz.csmc = $("#ajaxForm #" + $("#ajaxForm .jcxm")[i].id).attr("csmc");
							sz.zcsmc = null;
							sz.flag = $("#ajaxForm #" + $("#ajaxForm .jcxm")[i].id).attr("flag");
							json.push(sz)
						}
					}
					if (json.length == 0){
						$.alert("请选择检测项目！");
						return false;
					}
					if($("#ajaxForm .jczxm").length >0){
						for (let i = 0; i <= $("#ajaxForm .jczxm").length -1; i++) {
							if ($("#ajaxForm #" + $("#ajaxForm .jczxm")[i].id).is(':checked')) {
								var jcxmid = $("#ajaxForm #" + $("#ajaxForm .jczxm")[i].id).attr("jcxmid")
								if (json.length > 0) {
									for (let k = json.length - 1; k >= 0; k--) {
										if (json[k].jcxmid == jcxmid && json[k].jczxmid == null) {
											json.splice(k, 1);
										}
									}
									var sz = {"xmglid":'',"jcxmid": '', "jczxmid": '',"flag":'',"csmc":'',"zcsmc":'',"sfsf":'',"yfje":'',"sfje":'',"fkrq":''};
									sz.jcxmid = $("#ajaxForm #" + $("#ajaxForm .jczxm")[i].id).attr("jcxmid");
									sz.jczxmid = $("#ajaxForm .jczxm")[i].id;
									sz.csmc = $("#ajaxForm #" + sz.jcxmid).attr("csmc");
									sz.zcsmc = $("#ajaxForm #" + $("#ajaxForm .jczxm")[i].id).attr("csmc");
									sz.flag = $("#ajaxForm #" + sz.jcxmid).attr("flag");
									json.push(sz)
								}
							}
						}
						for (let k = json.length - 1; k >= 0; k--) {
							if (json[k].flag == "1"){
								if (json[k].jczxmid == null){
									$.alert("请选择检测子项目！");
									return false;
								}
							}
						}
					}
					var dtoList = JSON.parse($("#ajaxForm #dtoList").val());
					var jcxmmc = "";
					for (let i = 0; i < json.length; i++) {
						jcxmmc+=","+json[i].csmc;
						if (json[i].zcsmc)
							jcxmmc+="-"+json[i].zcsmc;
						for (let j = dtoList.length-1; j >= 0; j--) {
							if (json[i].jcxmid == dtoList[j].jcxmid && (!json[i].jczxmid || !dtoList[j].jczxmid || json[i].jczxmid == dtoList[j].jczxmid)) {
								json[i].xmglid = dtoList[j].xmglid;
								json[i].sfsf = dtoList[j].sfsf;
								json[i].yfje = dtoList[j].yfje;
								json[i].sfje = dtoList[j].sfje;
								json[i].fkrq = dtoList[j].fkrq;
							}
						}
					}
					if("1"!=$("#ajaxForm #xg_flg").val()){
						for (let k = 0; k <json.length; k++) {
							for (var i = 0; i < t_map.rows.length; i++) {
								if (json[k].jcxmid == t_map.rows[i].jcxmid) {
									if(json[k].jczxmid){
										if (json[k].jczxmid == t_map.rows[i].jczxmid) {
											json[k].sfsf=t_map.rows[i].sfsf;
											if(t_map.rows[i].yfje){
												json[k].yfje=t_map.rows[i].yfje;
											}else{
												json[k].yfje="0";
											}
											if(t_map.rows[i].sfsf&&t_map.rows[i].sfsf=='0'){
												json[k].yfje="0";
											}
											if(t_map.rows[i].sfje){
												json[k].sfje=t_map.rows[i].sfje;
											}else{
												json[k].sfje="0";
											}
											json[k].fkrq=t_map.rows[i].fkrq;
											break;
										}
									}else{
										json[k].sfsf=t_map.rows[i].sfsf;
										if(t_map.rows[i].yfje){
											json[k].yfje=t_map.rows[i].yfje;
										}else{
											json[k].yfje="0";
										}
										if(t_map.rows[i].sfsf&&t_map.rows[i].sfsf=='0'){
											json[k].yfje="0";
										}
										if(t_map.rows[i].sfje){
											json[k].sfje=t_map.rows[i].sfje;
										}else{
											json[k].sfje="0";
										}
										json[k].fkrq=t_map.rows[i].fkrq;
										break;
									}
								}
							}
						}
					}
					$("#ajaxForm #jcxmmc").val(jcxmmc.substring(1));
					$("#ajaxForm #jcxm").val(JSON.stringify(json))
				}else if($("#ajaxForm input:radio[name='sjqf']:checked").val()==null){
					$.alert("请选择送检区分！");
					return false;
				}else if(!$("#ajaxForm #kyDIV").is(":hidden") && $("#ajaxForm #kyxm option:selected").text()=="--请选择--"){
					$.alert("请选择立项编号！");
					return false;
				}else if($("#ajaxForm #yblx option:selected").text()=="--请选择--"){
					$.alert("请选择标本类型！");
					return false;
				}else if($("#ajaxForm #ybtj").val()==""||$("#ajaxForm #ybtj").val()==null){
					$.alert("请输入标本体积！");
					return false;
				}else if($("#ajaxForm #cyrq").val()==""||$("#ajaxForm #cyrq").val()==null){
					$.alert("请输入采样日期！");
					return false;
				}else if($("#ajaxForm #kdlx").val()==""||$("#ajaxForm #kdlx").val()==null){
					$.alert("请选择快递类型！");
					return false;
				}else if($("#ajaxForm #lczz").val()==""||$("#ajaxForm #lczz").val()==null){
					$.alert("请输入主诉！");
					return false;
				}

				if(/[^\a-\z\A-\Z0-9 -]/g.test($("#ajaxForm #ybbh").val())){
					$.error("请输入正确样本编号,禁用特殊字符！");
					return false;
				}

				//验证开票申请
				if($("#ajaxForm #sfsf").val()&&$("#ajaxForm #sfsf").val()!=0){
					var i=0;
					$("#ajaxForm .kpsq").each(function(){
						if(this.checked==true){
							i++;
						}
					})
					if(i==0){
						$.alert("请填写开票申请！");
						return false;
					}
				}

				//验证快递单号
				var cskz2=$("#ajaxForm #kdlx").find("option:selected").attr("cskz2");
				if(!cskz2){
					if($("#ajaxForm #kdh").val()==""){
						$.alert("请填写快递单号！");
						return false;
					}
				}

				if($("#ajaxForm #nbbm").val()!=null){
					var nbbm=$("#ajaxForm #nbbm").val();
					$("#ajaxForm #px").val(nbbm.substring(1,nbbm.length-1));
				}
				var $this = this;
				var opts = $this["options"]||{};
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
				if($("#ajaxForm #sfsf").val()=="0"&&$("#ajaxForm #sfje").val()>0){
					$.confirm('该标本已付费：'+$("#ajaxForm #sfje").val()+'元！，是否继续操作？',function(result){
						if(result){
							submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
								if(responseText["status"] == 'success'){
									$.success(responseText["message"],function() {
										if(opts.offAtOnce){
											$.closeModal(opts.modalName);
										}
										PCRAudit(responseText["bys"],responseText["jcdw"],responseText["sjid"],responseText["jcxmids"],responseText["ygzbys"]);
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
						}
					})
				}else{
					submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								PCRAudit(responseText["bys"],responseText["jcdw"],responseText["sjid"],responseText["jcxmids"],responseText["ygzbys"]);
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
function PCRAudit(bys,jcdw,sjid,jcxmids,ygzbys) {
	if (bys!=null && bys!= ''){
		$.ajax({
			type: "post",
			url: "/inspection/auditProcess/pagedataPCRAudit",
			data: {"bys" : bys.toString(),"jcdw" : jcdw.toString(),"sjid" : sjid.toString(),"jcxmids" : jcxmids.toString(),"access_token":$("#ac_tk").val(),"ygzbys":ygzbys.toString()},
			dataType: "json",
			success: function (responseText) {
				if(responseText["status"] == 'success'){
					preventResubmitForm(".modal-footer > button", false);

				}else if(responseText["status"] == "fail"){
					preventResubmitForm(".modal-footer > button", false);

				}
			},
			fail: function (result) {

			}
		})
	}
	searchSjsyglResult();
}
var confirmInspectionConfigSy = {
	width		: "1200px",
	modalName	: "confirmInspectionModalSy",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#ajaxForm").valid()){
					return false;
				}
				if($("#ajaxForm #sfjs1").is(":checked")){
					if($("#ajaxForm #nbbm").val()==""){
						$.alert("您已选择接收，请填写内部编码");
						return false;
					}
				}
				var json = '';
				$("#ajaxForm #jclx input[type='checkbox']").each(function(index){
					if (this.checked){
						json+=","+$("#ajaxForm #"+this.value).attr("nbzbm")
					}
				});
				if (json==''){
					$.alert("请选择检测类型！");
					return false;
				}
				$("#ajaxForm #jclxs").val(json.substring(1));
				var nbbm=$("#ajaxForm #nbbm").val();
				$("#ajaxForm #px").val(nbbm.substring(1,nbbm.length-1));
				var $this = this;
				var opts = $this["options"]||{};
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
				var szz=$('#ajaxForm input:radio[name="szz"]:checked').val();
				if(szz==$("#ajaxForm #ysszz").text()){
					$("#ajaxForm #grsz_flg").val(0);
				}else {
					$("#ajaxForm #grsz_flg").val(1);
				}
				var flag=0;
				$("#ajaxForm .ybztclass").each(function(){
					if(this.checked==true){
						flag++;
					}
				});
				if(flag>0){
					$.confirm("当前标本状态异常，是否继续确认？",function(result){
						if(result){
							submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
								if(responseText["status"] == 'success'){
									preventResubmitForm(".modal-footer > button", false);

									if(responseText["print"] && responseText["sz_flg"]){
										var host=responseText["print"];
										var sz_flg=responseText["sz_flg"];
										print_nbbm_sy(host,sz_flg);
									}

									$.success(responseText["message"],function() {
										$("#ajaxForm #jclxs").val("")
										$("#ajaxForm #ybbh").val("");
										$("#ajaxForm #nbbm").val("");
										$("#ajaxForm #hzxm").text("");
										$("#ajaxForm #fjids").val("");
										$("#ajaxForm #bs").val("1");
										$("#ajaxForm #redisFj").remove();
										$("#ajaxForm #nl").text("");
										$("#ajaxForm #xbmc").text("");
										$("#ajaxForm #sjdw").text("");
										$("#ajaxForm #sjys").text("");
										$("#ajaxForm #xgsj").val("");
										$("#ajaxForm #ks").text("");
										$("#ajaxForm #db").text("");
										$("#ajaxForm #ybtj").text("");
										$("#ajaxForm #yblxmc").text("");
										$("#ajaxForm #jcxm").text("");
										$("#ajaxForm #lczz").text("");
										$("#ajaxForm #qqzd").text("");
										$("#ajaxForm #gzbymc").text("");
										$("#ajaxForm #sjid").val("");
										$("#ajaxForm .ybztclass").prop("checked",false)
										$("#ajaxForm .sfjs").prop("checked",false)
										$("#ajaxForm #bz").val("");
										$("#ajaxForm #nbbmspan").show();
										$("#ajaxForm #jstj").val("");
										$("#ajaxForm #print_flg").val("");
										$("#ajaxForm #ybzt_flg").val("");
										$("#ajaxForm #ybztmc").val("");
										$("#ajaxForm #ysdh").val("");
										$("#ajaxForm .fileinput-remove-button").click();
										$("#ajaxForm label[name='jclxs']").remove()
										searchSjsyglResult();
									});
								}else if(responseText["status"] == "fail"){
									preSelectYbbh = null;
									$("#ajaxForm #ybbh").val("");
									preventResubmitForm(".modal-footer > button", false);
									$.error(responseText["message"],function() {
									});
								} else{
									preventResubmitForm(".modal-footer > button", false);
									$.alert("不好意思！出错了！");
								}
							},".modal-footer > button");
						}
					});
				}else{
					submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							preventResubmitForm(".modal-footer > button", false);

							if(responseText["print"] && responseText["sz_flg"]){
								var host=responseText["print"];
								var sz_flg=responseText["sz_flg"];
								print_nbbm_sy(host,sz_flg);
							}

							$.success(responseText["message"],function() {
								$("#ajaxForm #jclxs").val("");
								$("#ajaxForm #ybbh").val("");
								$("#ajaxForm #nbbm").val("");
								$("#ajaxForm #hzxm").text("");
								$("#ajaxForm #nl").text("");
								$("#ajaxForm #xbmc").text("");
								$("#ajaxForm #sjdw").text("");
								$("#ajaxForm #sjys").text("");
								$("#ajaxForm #xgsj").val("");
								$("#ajaxForm #fjids").val("");
								$("#ajaxForm #bs").val("1");
								$("#ajaxForm #redisFj").remove();
								$("#ajaxForm .fileinput-remove-button").click();
								$("#ajaxForm #ks").text("");
								$("#ajaxForm #db").text("");
								$("#ajaxForm #ybtj").text("");
								$("#ajaxForm #yblxmc").text("");
								$("#ajaxForm #jcxm").text("");
								$("#ajaxForm #lczz").text("");
								$("#ajaxForm #qqzd").text("");
								$("#ajaxForm #gzbymc").text("");
								$("#ajaxForm #sjid").val("");
								$("#ajaxForm .ybztclass").removeAttr("checked");
								$("#ajaxForm .sfjs").removeAttr("checked");
								$("#ajaxForm #bz").val("");
								$("#ajaxForm #nbbmspan").show();
								$("#ajaxForm #jstj").val("");
								$("#ajaxForm #print_flg").val("");
								$("#ajaxForm #ybzt_flg").val("");
								$("#ajaxForm #ybztmc").val("");
								$("#ajaxForm #ysdh").val("");
								$("#ajaxForm label[name='jclxs']").remove()
								searchSjsyglResult();
							});
						}else if(responseText["status"] == "fail"){
							preSelectYbbh = null;
							$("#ajaxForm #ybbh").val("");
							preventResubmitForm(".modal-footer > button", false);
							$.error(responseText["message"],function() {
							});
						} else{
							preventResubmitForm(".modal-footer > button", false);
							$.alert("不好意思！出错了！");
						}
					},".modal-footer > button");
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
/*   调整标本实验模态框*/
var adjustBbsyConfig = {
	width		: "1500px",
	modalName	: "adjustBbsyModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var $this = this;
				var opts = $this["options"]||{};
				if(t_map.rows != null && t_map.rows.length > 0){
					var json = [];
					for(var i=0;i<t_map.rows.length;i++){
						if (t_map.rows[i].jcdw == ''){
							$.alert("检测单位不能为空！");
							return false;
						}
						if (t_map.rows[i].sfjs == '1' && (t_map.rows[i].jsrq == ''||t_map.rows[i].jsrq == undefined||t_map.rows[i].jsrq == null)){
							$.alert("第"+(i+1)+"行未填写接收日期");
							return false;
						}
						if (t_map.rows[i].jcbj == '1' && (t_map.rows[i].syrq == ''||t_map.rows[i].syrq == undefined||t_map.rows[i].syrq == null)){
							$.alert("第"+(i+1)+"行未填写实验日期");
							return false;
						}
						if (t_map.rows[i].sfqy == '1' && (t_map.rows[i].qysj == ''||t_map.rows[i].qysj == undefined||t_map.rows[i].qysj == null)){
							$.alert("第"+(i+1)+"行未填写取样时间");
							return false;
						}
						if ( t_map.rows[i].jsrq!='' && t_map.rows[i].jsrq!=undefined &&t_map.rows[i].jsrq!=null && (t_map.rows[i].sfjs == ''||t_map.rows[i].sfjs == undefined||t_map.rows[i].sfjs == null||t_map.rows[i].sfjs =='0')){
							$.alert("第"+(i+1)+"行请勾选是否接收");
							return false;
						}
						if ( t_map.rows[i].syrq!='' && t_map.rows[i].syrq!=undefined &&t_map.rows[i].syrq!=null && (t_map.rows[i].jcbj == ''||t_map.rows[i].jcbj == undefined||t_map.rows[i].jcbj == null||t_map.rows[i].jcbj =='0')){
							$.alert("第"+(i+1)+"行请勾选是否接收");
							return false;
						}
						if ( t_map.rows[i].qysj!='' && t_map.rows[i].qysj!=undefined &&t_map.rows[i].qysj!=null && (t_map.rows[i].sfqy == ''||t_map.rows[i].sfqy == undefined||t_map.rows[i].sfqy == null||t_map.rows[i].sfqy =='0')){
							$.alert("第"+(i+1)+"行请勾选是否取样");
							return false;
						}
						var sz = {"xmsyglid":'',"sjid":'',"syglid":'',"jcdw":'',"lx":'',"jclxid":'',"sfjs":'',"jsrq":'',"jcbj":'',"syrq":'',"sfqy":'',"qysj":''};
						sz.syglid = t_map.rows[i].syglid;
						sz.xmsyglid = t_map.rows[i].xmsyglid;
						sz.lx = t_map.rows[i].lx;
						sz.jclxid = t_map.rows[i].jclxid;
						sz.sjid = t_map.rows[i].sjid;
						sz.jcdw = t_map.rows[i].jcdw;
						sz.sfjs = t_map.rows[i].sfjs;
						sz.jcbj = t_map.rows[i].jcbj;
						sz.jsrq = t_map.rows[i].jsrq;
						sz.syrq = t_map.rows[i].syrq;
						sz.sfqy = t_map.rows[i].sfqy;
						sz.qysj = t_map.rows[i].qysj;
						sz.sjid = t_map.rows[i].sjid;
						json.push(sz);
					}
					$("#ajaxFormBbsyAdjust #json").val(JSON.stringify(json));
					$("#ajaxFormBbsyAdjust input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"ajaxFormBbsyAdjust",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
									searchSjsyglResult();
								}
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

				}else{
					$.alert("未获取到数据！");
					return false;
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
function print_nbbm_sy(host,sz_flg){
	var print_url=null
	if(sz_flg=="0"){
		print_url="http://localhost"+host;
		var openWindow = window.open(print_url);
		setTimeout(function(){
			openWindow.close();
		}, 600);
	}else if(sz_flg=="1"){
		var glxx=$("#ajaxForm #glxx").val();
		print_url="http://"+glxx+host;
		openWindow = window.open(print_url);
		setTimeout(function(){
			openWindow.close();
		}, 600);
	}
}
/*查看送检信息模态框*/
var viewSjxxConfig = {
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

var SampleExperiment_ButtonInit = function(){
    	var oInit = new Object();
    	var postdata = {};
    	oInit.Init = function () {
    	var btn_query =$("#sjsygl_formSearch #btn_query");//模糊查询
		var initTableField = $("#sjsygl_formSearch #initTableField");//字段选择
		var btn_view = $("#sjsygl_formSearch #btn_view");//
		var btn_confirm = $("#sjsygl_formSearch #btn_confirm");//
		var btn_adjust = $("#sjsygl_formSearch #btn_adjust");//调整
		var btn_mod = $("#sjsygl_formSearch #btn_mod");
		var btn_add = $("#sjsygl_formSearch #btn_add");
    	//添加日期控件
    	laydate.render({
    	   elem: '#sjsygl_formSearch #jsrqstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#sjsygl_formSearch #jsrqend'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#sjsygl_formSearch #bgrqstart'
		   ,type: 'date'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#sjsygl_formSearch #bgrqend'
		   ,type: 'date'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#sjsygl_formSearch #qyrqstart'
    	   ,type: 'datetime'
		   ,ready: function(date){
				if(this.dateTime.hours==0&&this.dateTime.minutes==0&&this.dateTime.seconds==0){
					var myDate = new Date(); //实例一个时间对象；
					this.dateTime.hours=myDate.getHours();
					this.dateTime.minutes=myDate.getMinutes();
		        	this.dateTime.seconds=myDate.getSeconds();
				}
   	    	}
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#sjsygl_formSearch #qyrqend'
    	   ,type: 'datetime'
		   ,ready: function(date){
				if(this.dateTime.hours==0&&this.dateTime.minutes==0&&this.dateTime.seconds==0){
					var myDate = new Date(); //实例一个时间对象；
					this.dateTime.hours=myDate.getHours();
					this.dateTime.minutes=myDate.getMinutes();
		        	this.dateTime.seconds=myDate.getSeconds();
				}
		   }
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#sjsygl_formSearch #ydrqstart'
    	   ,type: 'datetime'
		   ,ready: function(date){
				if(this.dateTime.hours==0&&this.dateTime.minutes==0&&this.dateTime.seconds==0){
					var myDate = new Date(); //实例一个时间对象；
					this.dateTime.hours=myDate.getHours();
					this.dateTime.minutes=myDate.getMinutes();
		        	this.dateTime.seconds=myDate.getSeconds();
				}
		   }
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#sjsygl_formSearch #ydrqend'
    	   ,type: 'datetime'
		   ,ready: function(date){
				if(this.dateTime.hours==0&&this.dateTime.minutes==0&&this.dateTime.seconds==0){
					var myDate = new Date(); //实例一个时间对象；
					this.dateTime.hours=myDate.getHours();
					this.dateTime.minutes=myDate.getMinutes();
		        	this.dateTime.seconds=myDate.getSeconds();
				}
		    }
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#sjsygl_formSearch #syrqstart'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#sjsygl_formSearch #syrqend'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#sjsygl_formSearch #fkrqstart'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#sjsygl_formSearch #fkrqend'
    	});
		/*---------------------------新增送检信息-----------------------------------*/
		btn_add.unbind("click").click(function(){
			var sel_row = $('#sjsygl_formSearch #sjsygl_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				var jcxmid="";
				var jczxmid="";
				if(sel_row[0].jcxmid!=null&&sel_row[0].jcxmid!='null'&&sel_row[0].jcxmid!=undefined){
					jcxmid=sel_row[0].jcxmid
				}
				if(sel_row[0].jczxmid!=null&&sel_row[0].jczxmid!='null'&&sel_row[0].jczxmid!=undefined){
					jczxmid=sel_row[0].jczxmid
				}
				SampleExperimentDealById(sel_row[0].sjid,null,"add",btn_add.attr("tourl"),sel_row[0].lx,jcxmid,jczxmid);
			}else{
				$.error("请选中一行");
			}
		});
    	if(btn_query!=null){
    		btn_query.unbind("click").click(function(){
    			searchSjsyglResult(true); 
    		});
    	};
		if(initTableField!=null){
			initTableField.unbind("click").click(function(){
				$.showDialog(titleSelectUrl+"?ywid=SAMPLE_EXPERIMENT"
					,"列表字段设置", $.extend({},setSjsyglListFieldsConfig,{"width":"1020px"}));
			});
		}
		//送检收样确认
		btn_confirm.unbind("click").click(function(){
			var sel_row = $('#sjsygl_formSearch #sjsygl_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				SampleExperimentDealById(sel_row[0].sjid,null,"confirm",btn_confirm.attr("tourl"));
			}else{
				SampleExperimentDealById(null,null,"confirm",btn_confirm.attr("tourl"));
			}
		});
		/*---------------------------修改送检信息-----------------------------------*/
		btn_mod.unbind("click").click(function(){
			var sel_row = $('#sjsygl_formSearch #sjsygl_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				SampleExperimentDealById(sel_row[0].sjid,null,"mod",btn_mod.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
			/*---------------------------查看送检信息表-----------------------------------*/
		btn_view.unbind("click").click(function(){
			var sel_row = $('#sjsygl_formSearch #sjsygl_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				SampleExperimentDealById(sel_row[0].sjid,sel_row[0].ks,"view",btn_view.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		/*---------------------------调整送检信息表-----------------------------------*/
		btn_adjust.unbind("click").click(function(){
			var sel_row = $('#sjsygl_formSearch #sjsygl_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				SampleExperimentDealById(sel_row[0].xmsyglid,sel_row[0].ks,"adjust",btn_adjust.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
    	/**显示隐藏**/      
    	$("#sjsygl_formSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(SampleExperiment_turnOff){
    			$("#sjsygl_formSearch #searchMore").slideDown("low");
    			SampleExperiment_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#sjsygl_formSearch #searchMore").slideUp("low");
    			SampleExperiment_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    		//showMore();
    	});
    	
    };
    return oInit;
};
var setSjsyglListFieldsConfig = {
	width : "1020px",
	modalName : "setSjsyglListFieldsConfig",
	buttons : {
		reelect : {
			label : "恢复默认",
			className : "btn-info",
			callback : function() {
				$("#ajaxForm").attr("action","/common/title/commTitleDefaultSave");
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
				submitForm("ajaxForm", function(responseText,statusText) {
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							//关闭Modal
							$.closeModal("setSjsyglListFieldsConfig");
							$('#sjsygl_formSearch #sjsygl_list').bootstrapTable('destroy');
							t_sjsyglfieldList = getSjsyglDataColumn(responseText.choseList,responseText.waitList);

							var oTable = new SampleExperiment_TableInit(t_sjsyglfieldList,false);
							oTable.Init();
						});
					}else{
						$.error(responseText["message"],function() {
                                                                    });
					}
					preventResubmitForm(".modal-footer > button", false);
				}, ".modal-footer > button");
				return false;
			}
		},
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var _choseListDiv = $("#ajaxForm #list2 div[id='choseListDiv']");
				if(_choseListDiv.length < 1 ){
					$.error("请选择显示字段。")
					return false;
				}
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
				submitForm("ajaxForm", function(responseText,statusText) {
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							//关闭Modal
							$.closeModal("setSjsyglListFieldsConfig");
							$('#sjsygl_formSearch #sjsygl_list').bootstrapTable('destroy');
							t_sjsyglfieldList = getSjsyglDataColumn(responseText.choseList,responseText.waitList);

							var oTable = new SampleExperiment_TableInit(t_sjsyglfieldList,false);
							oTable.Init();
						});
					}else{
						$.error(responseText["message"],function() {
                                                                    });
					}
					preventResubmitForm(".modal-footer > button", false);
				}, ".modal-footer > button");
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
//获取表格显示形式
function getSjsyglDataColumn(zdList,waitList){
	var flelds = [];
	var map = {};
	var item = null;
	map = {};
	map["checkbox"] = true;
	flelds.push(map);
	$.each(zdList,function(i){
		item = zdList[i];
		map = {};
		map["title"] = item.xszdmc;
		map["titleTooltip"] = item.zdsm;
		map["field"] = item.xszd;
		if(item.pxzd&&item.pxzd!="")
			map["sortable"] = true;
		map["sortName"] = item.pxzd;
		map["width"] = item.xskd + "%";
		if(item.xsgs){
			map["formatter"] = eval(item.xsgs);
		}
		map["visible"]=true;
		flelds.push(map);
	});
	$.each(waitList,function(i){
		item = waitList[i];
		map = {};
		map["title"] = item.xszdmc;
		map["titleTooltip"] = item.zdsm;
		map["field"] = item.xszd;
		if(item.pxzd&&item.pxzd!="")
			map["sortable"] = true;
		map["sortName"] = item.pxzd;
		map["width"] = item.xskd + "%";
		if(item.xsgs){
			map["formatter"] = eval(item.xsgs);
		}
		if(zdList.length ==0){
			if(waitList[i].mrxs == '0'){
				map["visible"] = false;
			}else if(waitList[i].mrxs == '9'){
				map["visible"] = false;
			}
		}else
			map["visible"]=false;
		flelds.push(map);
	});
	return flelds;
}

function searchSjsyglResult(isTurnBack){
	dealSpaceQuery("#sjsygl_formSearch #cxnr");// 扫码模糊查询去除空格
	//关闭高级搜索条件
	$("#sjsygl_formSearch #searchMore").slideUp("low");
	SampleExperiment_turnOff=true;
	$("#sjsygl_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#sjsygl_formSearch #sjsygl_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#sjsygl_formSearch #sjsygl_list').bootstrapTable('refresh');
	}
}

document.onkeydown=function(e){
	if(e.keyCode==123)
		return false;
};

$("#sjsygl_formSearch #cxnr").on('keydown',function(e){
	if(e.keyCode==74 && $("#cxnr").val().length >=9){
		return false;
	}
});

$(function(){
	//0.界面初始化
    // 1.初始化Table
    var oTable = new SampleExperiment_TableInit(sjsyglFieldList,true);
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new SampleExperiment_ButtonInit();
    oButtonInit.Init();
	// 所有下拉框添加choose样式
    jQuery('#sjsygl_formSearch .chosen-select').chosen({width: '100%'});
	// 初始绑定显示更多的事件
	$("#sjsygl_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
});




