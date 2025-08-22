var Experiment_turnOff=true;
var Experiment_TableInit=function(experimentList,firstFlg){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#experiment_formSearch #experiment_list").bootstrapTable({
			url: '/experimentS/experimentS/pageGetListExperiment',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#experiment_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   // 是否显示分页（*）
            // paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "dsyrq desc, syrq desc, nbbm",				// 排序字段
            sortOrder: "asc",                   // 排序方式
            queryParams: oTableInit.queryParams,// 传递参数（*）
            sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       // 初始化加载第一页，默认第一页
			pageSize: 15,                       // 每页的记录行数（*）
			pageList: [15,30,50,100],        // 可供选择的每页的行数（*）
            paginationPreText: '‹',				// 指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			// 指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  // 是否显示所有的列
            showRefresh: true,                  // 是否显示刷新按钮
            minimumCountColumns: 2,             // 最少允许的列数
            clickToSelect: true,                // 是否启用点击选中行
            // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "sjid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            paginationDetailHAlign:' hiddenDetailInfo',
            columns: experimentList,
            onLoadSuccess:function(){

            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
				if ($("#experiment_formSearch #btn_viewmore").length>0){
					Experiment_DealById(row.sjid,'viewmore',$("#experiment_formSearch #btn_viewmore").attr("tourl"),row.qf,row.flg_qf);
				}else{
					Experiment_DealById(row.sjid,'view',$("#experiment_formSearch #btn_view").attr("tourl"),row.qf,row.flg_qf);
				}
             },
		});
        $("#experiment_formSearch #experiment_list").colResizable({
            liveDrag:true, 
            gripInnerHtml:"<div class='grip'></div>", 
            draggingClass:"dragging", 
            resizeMode:'fit', 
            postbackSafe:true,
			isFirst:firstFlg,
            partialRefresh:true}
        );
	};
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			// pageSize: params.limit,   // 页面大小
			// pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "dsyrq desc, syrq desc, nbbm", // 防止同名排位用
            sortLastOrder: "asc", // 防止同名排位用
            zt: $("#experiment_formSearch #zt").val(), // 防止同名排位用
            jcxmids: $("#experiment_formSearch #jcxmids").val(), // 防止同名排位用
			limitColumns: $("#experiment_formSearch #limitColumns").val() //筛选出这部分字段用于列表显示
            // 搜索框使用
            // search:params.search
        };
		return getExperimentSearchData(map);
	};
	return oTableInit;
}

function getExperimentSearchData(map){
	var experiment_select=$("#experiment_formSearch #experiment_select").val();
	var experiment_input=$.trim(jQuery('#experiment_formSearch #experiment_input').val());
	if(experiment_select=="0"){
		map["ybbh"]=experiment_input
	}else if(experiment_select=="1"){
		map["nbbm"]=experiment_input
	}else if(experiment_select=="2"){
		map["hzxm"]=experiment_input
	}
	// 接收日期开始日期
	var jsrqstart = jQuery('#experiment_formSearch #jsrqstart').val();
	map["jsrqstart"] = jsrqstart;
	// 接收日期结束日期
	var jsrqend = jQuery('#experiment_formSearch #jsrqend').val();
	map["jsrqend"] = jsrqend;
	// 报告日期开始日期
	var bgrqstart = jQuery('#experiment_formSearch #bgrqstart').val();
	map["bgrqstart"] = bgrqstart;
	// 报告日期结束日期
	var bgrqend = jQuery('#experiment_formSearch #bgrqend').val();
	map["bgrqend"] = bgrqend;
	// 起运日期开始时间
	var qyrqstart = jQuery('#experiment_formSearch #qyrqstart').val();
	map["qyrqstart"] = qyrqstart;
	// 起运日期结束时间
	var qyrqend = jQuery('#experiment_formSearch #qyrqend').val();
	map["qyrqend"] = qyrqend;
	// 运达日期开始时间
	var ydrqstart = jQuery('#experiment_formSearch #ydrqstart').val();
	map["ydrqstart"] = ydrqstart;
	// 运达日期结束时间
	var ydrqend = jQuery('#experiment_formSearch #ydrqend').val();
	map["ydrqend"] = ydrqend;
	// 付款日期开始时间
	var fkrqstart = jQuery('#experiment_formSearch #fkrqstart').val();
	map["fkrqstart"] = fkrqstart;
	// 付款日期结束时间
	var fkrqend = jQuery('#experiment_formSearch #fkrqend').val();
	map["fkrqend"] = fkrqend;
	//科室
	var dwids = jQuery('#experiment_formSearch #sjdw_id_tj').val();
	map["dwids"] = dwids;
	//合作伙伴分类
	var sjhbfls = jQuery('#experiment_formSearch #sjhbfl_id_tj').val();
	map["sjhbfls"] = sjhbfls;
	//合作伙伴子分类
	var sjhbzfls = jQuery('#experiment_formSearch #sjhbzfl_id_tj').val();
	map["sjhbzfls"] = sjhbzfls;
	//标本类型
	var yblxs = jQuery('#experiment_formSearch #yblx_id_tj').val();
	map["yblxs"] = yblxs;
	//是否合作关系
	var dbm= jQuery('#experiment_formSearch #dbm_id_tj').val();
	map["dbm"] = dbm;
	//是否付款
	var fkbj=jQuery('#experiment_formSearch #fkbj_id_tj').val()
	map["fkbj"]=fkbj;
	// //是否收费
	// var sfsf=jQuery('#experiment_formSearch #sfsf_id_tj').val()
	// 	map["sfsf"]=sfsf;
	//是否上传
	var sfsc=jQuery('#experiment_formSearch #sfsc_id_tj').val()
	map["sfsc"]=sfsc;
	//是否RNA检测
	var jcbj=jQuery('#experiment_formSearch #jcbj_id_tj').val()
	map["jcbj"]=jcbj;
	//是否DNA检测
	var djcbj=jQuery('#experiment_formSearch #djcbj_id_tj').val()
	map["djcbj"]=djcbj;
	//是否其他检测
	var qtjcbj=jQuery('#experiment_formSearch #qtjcbj_id_tj').val()
	map["qtjcbj"]=qtjcbj;
	//是否自免检测
	var sfzmjc=jQuery('#experiment_formSearch #sfzmjc_id_tj').val()
	map["sfzmjc"]=sfzmjc;
	var sfjs=jQuery('#experiment_formSearch #sfjs_id_tj').val()
	map["sfjs"]=sfjs;
	var sftj=jQuery('#experiment_formSearch #sftj_id_tj').val()
	map["sftj"]=sftj;
	//扩展参数
	var sjkzcs1=jQuery('#experiment_formSearch #cskz1_id_tj').val()
	map["sjkzcs1"]=sjkzcs1
	var sjkzcs2=jQuery('#experiment_formSearch #cskz2_id_tj').val()
	map["sjkzcs2"]=sjkzcs2
	var sjkzcs3=jQuery('#experiment_formSearch #cskz3_id_tj').val()
	map["sjkzcs3"]=sjkzcs3
	var sjkzcs4=jQuery('#experiment_formSearch #cskz4_id_tj').val()
	map["sjkzcs4"]=sjkzcs4
	var jcxm=jQuery('#experiment_formSearch #jcxm_id_tj').val()
	map["jcxms"]=jcxm
	var jyjgs=jQuery('#experiment_formSearch #jyjg_id_tj').val()
	map["jyjgs"]=jyjgs
	var sfsfs=jQuery('#experiment_formSearch #sfsf_id_tj').val()
	map["sfsfs"]=sfsfs
	var sfzq=jQuery('#experiment_formSearch #sfzq_id_tj').val();
	map["sfzq"]=sfzq
	//快递类型
	var kdlxs=$("#experiment_formSearch #kdlx_id_tj").val();
	map["kdlxs"]=kdlxs
	//盖章类型
	var gzlxs=$("#experiment_formSearch #gzlx_id_tj").val();
	map["gzlxs"]=gzlxs
	//检测单位
	var jcdws=$("#experiment_formSearch #jcdw_id_tj").val();
	map["jcdws"]=jcdws
	//科研项目类型
	var kylxs=$("#experiment_formSearch #kylx_id_tj").val();
	map["kylxs"]=kylxs
	//科研项目
	var kyxms=$("#experiment_formSearch #kyxm_id_tj").val();
	map["kyxms"]=kyxms
	//送检区分
	var sjqfs=$("#experiment_formSearch #sjqf_id_tj").val();
	map["sjqfs"]=sjqfs
	//医院重点等级
	var dwzddjs=$("#experiment_formSearch #dwzddj_id_tj").val().replace("＋","+");
	map["yyzddjs"]=dwzddjs
	// 实验日期开始日期
	var syrqstart = jQuery('#experiment_formSearch #syrqstart').val();
	map["syrqstart"] = syrqstart;
	// 实验日期结束日期
	var syrqend = jQuery('#experiment_formSearch #syrqend').val();
	map["syrqend"] = syrqend;
	if(jQuery('#experiment_formSearch #syrqflg').is(":checked")){
		map["syrqflg"] = "1";
	}
	//添加日期控件
	laydate.render({
		elem: '#experiment_formSearch #jsrqstart'
		,theme: '#2381E9'
	});
	//添加日期控件
	laydate.render({
		elem: '#experiment_formSearch #jsrqend'
		,theme: '#2381E9'
	});
	//添加日期控件
	laydate.render({
		elem: '#experiment_formSearch #bgrqstart'
		,type: 'date'
	});
	//添加日期控件
	laydate.render({
		elem: '#experiment_formSearch #bgrqend'
		,type: 'date'
	});
	//添加日期控件
	laydate.render({
		elem: '#experiment_formSearch #qyrqstart'
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
		elem: '#experiment_formSearch #qyrqend'
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
		elem: '#experiment_formSearch #ydrqstart'
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
		elem: '#experiment_formSearch #ydrqend'
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
		elem: '#experiment_formSearch #syrqstart'
	});
	//添加日期控件
	laydate.render({
		elem: '#experiment_formSearch #syrqend'
	});
	//添加日期控件
	laydate.render({
		elem: '#experiment_formSearch #fkrqstart'
	});
	//添加日期控件
	laydate.render({
		elem: '#experiment_formSearch #fkrqend'
	});

	return map;
}
//合作伙伴子分类的取得
function getSjhbzfls() {
	// 合作伙伴分类
	var sjhbfl = jQuery('#experiment_formSearch #sjhbfl_id_tj').val();
	if (!isEmpty(sjhbfl)) {
		sjhbfl = "'" + sjhbfl + "'";
		jQuery("#experiment_formSearch #sjhbzfl_id").removeClass("hidden");
	}else{
		jQuery("#experiment_formSearch #sjhbzfl_id").addClass("hidden");
	}
	// 项目类别不为空
	if (!isEmpty(sjhbfl)) {
		var url = "/systemmain/data/ansyGetJcsjListInStop";
		$.ajax({
			type : "POST",
			url : url,
			data : {"fcsids":sjhbfl,"access_token":$("#ac_tk").val()},
			dataType : "json",
			success : function(data){
				if(data.length > 0) {
					var html = "";
					$.each(data,function(i){
						html += "<li>";
						if(data[i].scbj=="2"){
							html += "<a style='text-decoration:none;cursor:pointer;' onclick=\"addTj('sjhbzfl','" + data[i].csid + "','experiment_formSearch');\" id=\"sjhbzfl_id_" + data[i].csid + "\"><span style='color:red'>" + data[i].csmc + "(停用)" +"</span></a>";
						}else
							html += "<a style='text-decoration:none;cursor:pointer;' onclick=\"addTj('sjhbzfl','" + data[i].csid + "','experiment_formSearch');\" id=\"sjhbzfl_id_" + data[i].csid + "\"><span>" + data[i].csmc + "</span></a>";
						html += "</li>";
					});
					jQuery("#experiment_formSearch #ul_sjhbzfl").html(html);
					jQuery("#experiment_formSearch #ul_sjhbzfl").find("[name='more']").each(function(){
						$(this).on("click", s_showMoreFn);
					});
				} else {
					jQuery("#experiment_formSearch #ul_sjhbzfl").empty();

				}
				jQuery("#experiment_formSearch [id^='sjhbzfl_li_']").remove();
				$("#experiment_formSearch #sjhbzfl_id_tj").val("");
			}
		});
	} else {
		jQuery("#experiment_formSearch #div_sjhbzfl").empty();
		jQuery("#experiment_formSearch [id^='sjhbzfl_li_']").remove();
		$("#experiment_formSearch #sjhbzfl_id_tj").val("");
	}
}

/**
 * 状态
 * @returns
 */
function ztFormat(value,row,index) {
	if (row.jcxmkzcs == 'F'){
		if (row.zt == '00' || row.zt == '  ' || !row.zt) {
			return '未提交';
		}else if (row.zt == '80') {
			return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.sjid + "\",event,\"\")' >审核通过</a>";
		}else if (row.zt == '15') {
			return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.sjid + "\",event,\"\")' >审核未通过</a>";
		}else{
			return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.sjid + "\",event,\"\")' >" + row.shxx_dqgwmc + "审核中</a>";
		}
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
function xhformat(value,row,index){
	return index+1;
}

function Experiment_DealById(id,action,tourl,qf,flg_qf){
	if(!tourl){
		return;
	}
	if(action=="view"){
		var url=tourl+"?sjid="+id+"&qf="+qf+"&flg_qf="+flg_qf;
		$.showDialog(url,'查看详细信息',viewExperimentConfig);
	}else if(action=="confirm"){
		$.showDialog(tourl,'确认',confirmExperimentConfig);
	}else if(action=="detection"){
		var url=tourl+"?ids="+id+"&qf="+qf+"&flg_qf="+flg_qf;
		$.showDialog(url,'修改送检实验状态',modSJDetectionConfig);
	}else if(action=="detectionFJ"){
		var url=tourl+"?ids="+id+"&qf="+qf+"&flg_qf="+flg_qf;
		$.showDialog(url,'修改复检实验状态',modFJDetectionConfig);
	}else if(action=="upload"){
		var url=tourl+"?sjid="+id+"&qf="+qf+"&flg_qf="+flg_qf;
		$.showDialog(url,'上传文件',uploadDetectionConfig);
	}else if(action=="viewmore"){
		var url=tourl+"?sjid="+id+"&qf="+qf+"&flg_qf="+flg_qf;
		$.showDialog(url,'详细信息',viewExperimentConfig);
	}else if(action =='sampling'){
		var url= tourl;
		$.showDialog(url,'取样',samplingExperimentConfig);
	}
}
var confirmExperimentConfig = {
	width		: "800px",
	modalName	:"confirmExperimentModal",
	offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

var samplingExperimentConfig = {
	width		: "800px",
	modalName	: "samplingExperimentModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#samplingExperimentForm").valid()){
					return false;
				}
				var suffixs = '';
				var ids = '';
				$("#samplingExperimentForm #jclx input[type='checkbox']").each(function(index){
					if (this.checked){
						ids+=","+this.value;
						suffixs+=","+$("#samplingExperimentForm #"+this.value).attr("nbzbm")
					}
				});
				if (suffixs==''){
					$.alert("请选择检测类型！");
					return false;
				}
				$("#samplingExperimentForm #suffixs").val(suffixs.substring(1));
				$("#samplingExperimentForm #ids").val(ids.substring(1));

				var $this = this;
				var opts = $this["options"]||{};
				$("#samplingExperimentForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"samplingExperimentForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						preventResubmitForm(".modal-footer > button", false);

						if(responseText["print"] && responseText["sz_flg"]){
							var list=responseText["print"];
							var sz_flg=responseText["sz_flg"];
							if (list && list.length>0){
								for (let i = 0; i < list.length; i++) {
									print_nbbm(list[i],sz_flg);
								}
							}
						}
						$.success(responseText["message"],function() {
							searchExperimentResult();
							$("#samplingExperimentForm label[name='jclxs']").remove()
							$("#samplingExperimentForm tr[name='mxList']").remove()
							$("#samplingExperimentForm #nbbm").val("");
							$("#samplingExperimentForm #sjid").val("");
							$("#samplingExperimentForm #ids").val("");
							$("#samplingExperimentForm #ybbh").val("");
							$("#samplingExperimentForm #suffixs").val("");
						});
					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						$.error(responseText["message"],function() {
						});
					} else{
						preventResubmitForm(".modal-footer > button", false);
						$.alert("不好意思！出错了！");
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
 * ajax访问的时候，因为是跨域，正常访问之前会触发OPTION的事件，造成打印机会多打印一份
 * 所以改成window.open
 */
function print_nbbm(host,sz_flg){
	var print_url=null
	if(sz_flg=="0"){
		print_url="http://localhost"+host;
		var openWindow = window.open(print_url);
		setTimeout(function(){
			openWindow.close();
		}, 1200);
	}else if(sz_flg=="1"){
		var glxx=$("#samplingExperimentForm #glxx").val();
		print_url="http://"+glxx+host;
		openWindow = window.open(print_url);
		setTimeout(function(){
			openWindow.close();
		}, 1200);
	}
}

var uploadDetectionConfig = {
	width		: "900px",
	modalName	: "uploadDetectionModal",
	formName	: "uploadFileAjaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {

				if(!$("#uploadFileAjaxForm #fjids").val()){
					$.error("请上传文件！")
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};

				$("#uploadFileAjaxForm input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"]||"uploadFileAjaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.closeModal(opts.modalName);
						searchExperimentResult();
						$.showDialog("/experimentS/experimentS/pagedataUpView?fjid="+responseText["fjids"],'上传进度',viewUpViewConfig);
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
var viewUpViewConfig = {
	width		: "900px",
	modalName	:"viewUpViewModal",
	offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};


var modFJDetectionConfig = {
		width		: "800px",
		modalName	: "modFJDetectionConfig",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					var $this = this;
					var opts = $this["options"]||{};
					var sfsytz="0";
					if(($("#detectionFormExper #syrqFirst").val()==null || $("#detectionFormExper #syrqFirst").val()=='') && ($("#detectionFormExper #syrq").val()!=null && $("#detectionFormExper #syrq").val()!='')){
						$("#detectionFormExper #sfsytz").val("1");
					}
					if(($("#detectionFormExper #dsyrqFirst").val()==null || $("#detectionFormExper #dsyrqFirst").val()=='') && ($("#detectionFormExper #dsyrq").val()!=null && $("#detectionFormExper #dsyrq").val()!='')){
						$("#detectionFormExper #sfsytz").val("1");
					}
					if(($("#detectionFormExper #qtsyrqFirst").val()==null || $("#detectionFormExper #qtsyrqFirst").val()=='') && ($("#detectionFormExper #qtsyrq").val()!=null && $("#detectionFormExper #qtsyrq").val()!='')){
						$("#detectionFormExper #sfsytz").val("1");
					}
					$("#detectionFormExper input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"detectionFormExper",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								if(responseText["ywid"]!=null && responseText["ywid"]!=''){
									//提交审核
									var auditType = responseText["auditType"];
									showAuditFlowDialog(auditType,responseText["ywid"],function(){
										searchExperimentResult();
									});
								}else{
									searchExperimentResult();
								}
							});
						}else if(responseText["status"] == "fail"){
							preventResubmitForm(".modal-footer > button", false);
							$.error(responseText["message"],function() {
							});
						} else{
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


var modSJDetectionConfig = {
		width		: "800px",
		modalName	: "modSJDetectionConfig",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					var $this = this;
					var opts = $this["options"]||{};
					var sfsytz="0";
					if(($("#detectionForm #syrqfirst").val()==null || $("#detectionForm #syrqfirst").val()=='') && ($("#detectionForm #syrq").val()!=null && $("#detectionForm #syrq").val()!='')){
						$("#detectionForm #sfsytz").val("1");
					}
					if(($("#detectionForm #dsyrqfirst").val()==null || $("#detectionForm #dsyrqfirst").val()=='') && ($("#detectionForm #dsyrq").val()!=null && $("#detectionForm #dsyrq").val()!='')){
						$("#detectionForm #sfsytz").val("1");
					}
					if(($("#detectionForm #qtsyrqfirst").val()==null || $("#detectionForm #qtsyrqfirst").val()=='') && ($("#detectionForm #qtsyrq").val()!=null && $("#detectionForm #qtsyrq").val()!='')){
						$("#detectionForm #sfsytz").val("1");
					}
					$("#detectionForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"detectionForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								if(responseText["ywid"]!=null && responseText["ywid"]!=''){
									//提交审核
									var auditType = responseText["auditType"];
									showAuditFlowDialog(auditType,responseText["ywid"],function(){
										searchExperimentResult();
									});
								}else{
									searchExperimentResult();
								}
							});
						}else if(responseText["status"] == "fail"){
							preventResubmitForm(".modal-footer > button", false);
							$.error(responseText["message"],function() {
							});
						} else{
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

var viewExperimentConfig = {
		width		: "900px",
		modalName	:"viewExperimentModal",
		offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

//function searchExperimentResult(isTurnBack){
//	//关闭高级搜索条件
//	$("#experiment_formSearch #searchMore").slideUp("low");
//	Experiment_turnOff=true;
//	$("#experiment_formSearch #sl_searchMore").html("高级筛选");
//	if(isTurnBack){
//		$('#experiment_formSearch #experiment_list').bootstrapTable('refresh',{pageNumber:1});
//	}else{
//		$('#experiment_formSearch #experiment_list').bootstrapTable('refresh');
//	}
//}

function searchExperimentResult(isTurnBack){
	$("#experiment_formSearch #searchMore").slideUp("low");
	Experiment_turnOff=true;
	$("#experiment_formSearch #sl_searchMore").html("高级筛选");
if(isTurnBack){
	$('#experiment_formSearch #experiment_list').bootstrapTable('refresh',{pageNumber:1});
}else{
	$('#experiment_formSearch #experiment_list').bootstrapTable('refresh');
}
}

var Experiment_oButton=function(){
	var oButtonInit = new Object();
	var postdata = {};
	oButtonInit.Init=function(){
		var btn_query=$("#experiment_formSearch #btn_query");
		var btn_view = $("#experiment_formSearch #btn_view");
		var btn_viewmore = $("#experiment_formSearch #btn_viewmore");
		var btn_upload = $("#experiment_formSearch #btn_upload");
		var btn_confirm = $("#experiment_formSearch #btn_confirm");
		var initTableField = $("#experiment_formSearch #initTableField");
		var btn_detection=$("#experiment_formSearch #btn_detection");
		var btn_selectexport = $("#experiment_formSearch #btn_selectexport");//选中导出
		var btn_searchexport = $("#experiment_formSearch #btn_searchexport");//搜索导出
		var sjhbflBind = $("#experiment_formSearch #sjhbfl_id ul li a");
		var btn_sampling = $("#experiment_formSearch #btn_sampling");

		btn_sampling.unbind("click").click(function(){
			Experiment_DealById(null,"sampling",btn_sampling.attr("tourl"));
		});
//-----------------------模糊查询ok------------------------------------
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchExperimentResult(true); 
    		});
		}

//		上传
		//---------------------------------检验结果上传----------------------------------
		btn_upload.unbind("click").click(function(){
			var sel_row = $('#experiment_formSearch #experiment_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				if(sel_row[0].jcxmkzcs == 'F'){
					Experiment_DealById(sel_row[0].sjid,"upload",btn_upload.attr("tourl"),sel_row[0].qf,sel_row[0].flg_qf);
				}else {
					$.error("请选择ResFirst项目的数据！");
				}
			}else{
				Experiment_DealById(null,"upload",btn_upload.attr("tourl"));

			}
		});
//-----------------------查看------------------------------------------
   	btn_view.unbind("click").click(function(){
    		var sel_row = $('#experiment_formSearch #experiment_list').bootstrapTable('getSelections');// 获取选择行数据
    		if(sel_row.length==1){
    			Experiment_DealById(sel_row[0].sjid,"view",btn_view.attr("tourl"),sel_row[0].qf,sel_row[0].flg_qf);
    		}else{
    			$.error("请选中一行");
    		}
    	});
		//绑定合作伙伴分类的单击事件
		if(sjhbflBind!=null){
			sjhbflBind.on("click", function(){
				setTimeout(function(){
					getSjhbzfls();
				}, 10);
			});
		}
		//-----------------------确认------------------------------------------
		btn_confirm.unbind("click").click(function(){
			Experiment_DealById(null,"confirm",btn_confirm.attr("tourl"));
		});
		if(initTableField!=null){
			initTableField.unbind("click").click(function(){
				$.showDialog(titleSelectUrl+"?ywid="+$('#experiment_formSearch #lx').val()
					,"列表字段设置", $.extend({},setExperimentConfig,{"width":"1020px"}));
			});
		}
		//-----------------------详情查看------------------------------------------
		btn_viewmore.unbind("click").click(function(){
			var sel_row = $('#experiment_formSearch #experiment_list').bootstrapTable('getSelections');// 获取选择行数据
			if(sel_row.length==1){
				Experiment_DealById(sel_row[0].sjid,"viewmore",btn_viewmore.attr("tourl"),sel_row[0].qf,sel_row[0].flg_qf);
			}else{
				$.error("请选中一行");
			}
		});
		//绑定合作伙伴分类的单击事件
		if(sjhbflBind!=null){
			sjhbflBind.on("click", function(){
				setTimeout(function(){
					getSjhbzfls();
				}, 10);
			});
		}
//-----------------------实验(功能：修改DNA/RNA的实验状态)-------------------------------------------------
   	btn_detection.unbind("click").click(function(){
		    var sel_row = $('#experiment_formSearch #experiment_list').bootstrapTable('getSelections');
		    if(sel_row.length==0){
		    	$.error("请至少选择一行");
		    	return;
	        }else{       	
		    	var ids="";
	        	var flg_qfs="";
	        	var jcxmmcs="";
		    	for(var i=0; i<sel_row.length; i++){
		    		ids = ids+","+sel_row[i].sjid;
		    		flg_qfs = flg_qfs+","+sel_row[i].flg_qf;
		    		jcxmmcs = jcxmmcs+","+sel_row[i].jcxmmc;
		    	}
		    	ids=ids.substr(1);
		    	flg_qfs=flg_qfs.substr(1);
		    	jcxmmcs=jcxmmcs.substr(1);
		    	//判断是否是正常送检和复检都选择
		    	var idsStr = ids.split(",");
		    	var fqfStr = flg_qfs.split(",");
		    	var jcxmmcsStr = jcxmmcs.split(",");
		    	
		    	var jcxmmc = jcxmmcsStr[0];
		    	var flg_qf = fqfStr[0];
		    	var isSameOption = true;
		    	var isSameJcxm = true;
		    	for(var i=1; i<fqfStr.length; i++){
		    		if(fqfStr[i]!=flg_qf){
		    			isSameOption = false;
		    		    $.error("请全部选择正常送检或者全部选择复检")
		    			break;
		    		}		
		    	}
		    	for(var j=1; j<jcxmmcsStr.length; j++){
		    		if(jcxmmcsStr[j]!=jcxmmc){
		    			isSameJcxm = false;
		    		    $.error("检测项目必须相同")
		    			break;
		    		}		
		    	}
		    	var flag = isSameOption && isSameJcxm;
		    	if(flag && flg_qf=="0" ){
		    		//跳转至正常送检的实验修改
		    		var tourll = btn_detection.attr("tourl");
		    		Experiment_DealById(ids,"detection",btn_detection.attr("tourl"),sel_row[0].qf,sel_row[0].flg_qf);
		    	}
		    	else if(flag && flg_qf=="1" ){
		    		//跳转至复检的实验修改
		    		Experiment_DealById(ids,"detectionFJ","/experimentS/experimentS/pagedataDetectionFJ",sel_row[0].qf,sel_row[0].flg_qf);
		    	}
		    }
	});
		/*-----------------------导出------------------------------------*/
		btn_searchexport.unbind("click").click(function(){
			$.showDialog(exportPrepareUrl+"?ywid=EXPERIMENT_SEARCH&expType=search&callbackJs=experimentSearchData"
				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
		});

		btn_selectexport.unbind("click").click(function(){
			var sel_row = $('#experiment_formSearch #experiment_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length>=1){
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids = ids + ","+ sel_row[i].sjid;
				}
				ids = ids.substr(1);
				$.showDialog(exportPrepareUrl+"?ywid=EXPERIMENT_SELECT&expType=select&ids="+ids
					,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
			}else{
				$.error("请选择数据");
			}
		});

		/**显示隐藏**/
		$("#experiment_formSearch #sl_searchMore").on("click", function(ev){
			var ev=ev||event;
			if(Experiment_turnOff){
				$("#experiment_formSearch #searchMore").slideDown("low");
				Experiment_turnOff=false;
				this.innerHTML="基本筛选";
			}else{
				$("#experiment_formSearch #searchMore").slideUp("low");
				Experiment_turnOff=true;
				this.innerHTML="高级筛选";
			}
			ev.cancelBubble=true;
			//showMore();
		});

//-----------------------------------------------------------------------
	}
	return oButtonInit;
}
//提供给导出用的回调函数
function experimentSearchData(){
	var map ={};
	var jcxmids = jQuery('#experiment_formSearch #jcxmids').val();
	map["jcxmids"]=jcxmids;
	map["zt"]=$("#experiment_formSearch #zt").val();
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="sjid";
	map["sortLastOrder"]="asc";
	map["sortName"]="jcxmmc";
	map["sortOrder"]="asc";

	return getExperimentSearchData(map);
}
//获取表格显示形式
function getExperimentDataColumn(zdList,waitList){
	var flelds = [];
	var map = {};
	var item = null;
	map = {};
	map["checkbox"] = true;
	map["width"] = "1%";
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

var setExperimentConfig = {
	width : "1020px",
	modalName : "setExperimentConfig",
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
							$.closeModal("setExperimentConfig");
							$('#experiment_formSearch #experiment_list').bootstrapTable('destroy');
							t_experimentList = getExperimentDataColumn(responseText.choseList,responseText.waitList);
							var oTable = new Experiment_TableInit(t_experimentList,false);;
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
							$.closeModal("setExperimentConfig");
							$('#experiment_formSearch #experiment_list').bootstrapTable('destroy');
							t_experimentList = getExperimentDataColumn(responseText.choseList,responseText.waitList);

							var oTable = new Experiment_TableInit(t_experimentList,false);
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
$(function(){
	if (localStorage.getItem("timer")){
		var timer = localStorage.getItem("timer");
		clearInterval(timer);
		localStorage.removeItem("timer");
	}
	var oTable = new Experiment_TableInit(experimentList,true);
	    oTable.Init();
	runEvery1Min();
	var zt = $("#experiment_formSearch #zt").val()
	if ($("#experiment_formSearch .div1").length >0){
		if ($("#experiment_formSearch #RFSFlag").val() == '1'){
			$("#experiment_formSearch #all_div").attr("class","col-xs-12 col-sm-12 col-md-8 col-lg-8")
		}
		for(var i=0;i<$(".div1").length;i++){
			var id=$(".div1")[i].id;
			if ($("#experiment_formSearch #RFSFlag").val() == '1'){
				if (i != 5){
					$("#experiment_formSearch #div_"+id).attr("class","col-xs-6 col-sm-6 col-md-3 col-lg-3")
				}
			}
			if ( id == zt){
				$("#experiment_formSearch #"+id).attr("style","background: cornflowerblue")
			}else {
				if ($("#experiment_formSearch #RFSFlag").val() == '1'){
					if ( id >1 && id < 6){
						$("#experiment_formSearch #div_"+id).attr("style","display: none")
					}
				}else{
					if ( id > 6){
						$("#experiment_formSearch #div_"+id).attr("style","display: none")
					}
				}
			}

		}
	}
	$("#experiment_formSearch #btn_upload").attr("style","display: none;")
	var oButton = new Experiment_oButton();
	    oButton.Init();
	$("#experiment_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
    jQuery('#experiment_formSearch .chosen-select').chosen({width: '100%'});
})

function click_bq(bs){
	if ( bs == '4')
		return
	$("#experiment_formSearch #zt").val(bs) ;
	if ($("#experiment_formSearch .div1").length >0){
		for(var i=0;i<$(".div1").length;i++){
			var id=$(".div1")[i].id;
			if ( id == bs){
				$("#experiment_formSearch #"+id).attr("style","background: cornflowerblue")
			}else {
				$("#experiment_formSearch #"+id).removeAttr("style")
				if ($("#experiment_formSearch #RFSFlag").val() == '1'){
					if ( id >1 && id < 6){
						$("#experiment_formSearch #"+id).attr("style","display: none")
					}
				}else{
					if ( id > 6){
						$("#experiment_formSearch #"+id).attr("style","display: none")
					}
				}
			}
		}
		if (bs == '0'){
			$("#experiment_formSearch #btn_confirm").removeAttr("style")
		}else {
			$("#experiment_formSearch #btn_confirm").attr("style","display: none;")
		}

		if (bs*1 >= 6 && $("#experiment_formSearch #RFSFlag").val() == '1'){
			$("#experiment_formSearch #btn_upload").removeAttr("style")
		}else {
			$("#experiment_formSearch #btn_upload").attr("style","display: none;")
		}
		searchExperimentResult(true);
	}
}

function runEvery1Min() {
	getBgTotal();
	var timer = setInterval(function () {
		getBgTotal()
	},1000 * 60)
	localStorage.setItem("timer", timer);
}

function getBgTotal() {
	$.ajax({
		"dataType" : 'json',
		"type" : "POST",
		"url" : "/experimentS/experimentS/pagedataBgTotal",
		"data" : { "access_token" : $("#ac_tk").val(),"jcxmids":$("#experiment_formSearch #jcxmids").val()},
		"success" : function(data) {
			if (data!=null){
				$("#experiment_formSearch #yjs").text(data.yjs);
				$("#experiment_formSearch #yjswqy").text(data.yjswqy);
				$("#experiment_formSearch #yqy").text(data.yqy);
				$("#experiment_formSearch #yqywtq").text(data.yqywtq);
				$("#experiment_formSearch #ytq").text(data.ytq);
				$("#experiment_formSearch #ytqwjk").text(data.ytqwjk);
				$("#experiment_formSearch #yjk").text(data.yjk);
				$("#experiment_formSearch #yjkwp").text(data.yjkwp);
				$("#experiment_formSearch #yp").text(data.yp);
				$("#experiment_formSearch #ypwsj").text(data.ypwsj);
				$("#experiment_formSearch #ysj").text(data.ysj);
				$("#experiment_formSearch #ysjwbg").text(data.ysjwbg);
				$("#experiment_formSearch #ybg").text(data.ybg);
				$("#experiment_formSearch #ytqwjc").text(data.ytqwjc);
				$("#experiment_formSearch #yjc").text(data.yjc);
				$("#experiment_formSearch #yjcwbg").text(data.yjcwbg);
			}
		}
	});
}