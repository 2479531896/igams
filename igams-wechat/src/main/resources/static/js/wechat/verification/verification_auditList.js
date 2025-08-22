var Verification_turnOff=true;
var verifi_audit_TableInit=function(){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#verifi_audit_formSearch #verifi_audit_list").bootstrapTable({
			url: '/verification/verification/pageGetListVerifi_audit',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#verifi_audit_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "sjyz.lrsj",				//排序字段
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
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "yzid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            },{title: '序号',
				formatter: function (value, row, index) {
					return index+1;
				},
				width: '2%',
                align: 'left',
                titleTooltip:'序号',
                visible:true
        	},{
	            field: 'ybbh',
	            title: '标本编号',
	            width: '10%',
	            align: 'left',
	            visible: true
	        },{
	            field: 'nbbm',
	            title: '内部编码',
	            width: '10%',
	            align: 'left',
	            visible:true
	        },{
	            field: 'hzxm',
	            title: '患者姓名',
	            width: '6%',
	            align: 'left',
	            visible: true
	        },{
	            field: 'nl',
	            title: '年龄',
	            width: '4%',
	            align: 'left',
	            visible: true
	        },{
	            field: 'yblxmc',
	            title: '标本类型',
	            width: '8%',
	            align: 'left',
	            visible: true
	        },{
	            field: 'db',
	            title: '合作伙伴',
	            width: '8%',
	            align: 'left',
	            visible: true
	        },{
	            field: 'sjph',
	            title: '试剂批号',
	            width: '8%',
	            align: 'left',
	            visible: true
	        },{
	            field: 'ywbh',
	            title: '引物编号',
	            width: '8%',
	            align: 'left',
	            visible: true
	        },{
	            field: 'yzlbmc',
	            title: '验证类别',
	            width: '7%',
	            align: 'left',
	            visible: true
	        },{
	            field: 'qfmc',
	            title: '区分',
	            width: '4%',
	            align: 'left',
	            visible: true
	        },{
	            field: 'yzjgmc',
	            title: '验证结果',
	            width: '10%',
	            align: 'left',
	            visible: true
	        },{
				field: 'jcjg',
				title: '检测结果',
				width: '10%',
				align: 'left',
				visible: true
			},{
				field: 'ysjg',
				title: '疑似结果',
				width: '10%',
				align: 'left',
				visible: true
			},{
	            field: 'bz',
	            title: '备注',
	            width: '10%',
	            align: 'left',
	            visible: true
	        },{
	            field: 'shxx_sqsj',
	            title: '申请时间',
	            width: '14%',
	            align: 'left',
	            visible: true
            },{
				field: 'shxx_shyj',
				title: '审核意见',
				width: '6%',
				align: 'left',
				visible: true
			},{
                field: 'sfsc',
                title: '是否上传',
				titleTooltip:'是否上传',
                width: '5%',
                align: 'left',
                formatter:sfscFormat,
                visible: true
            },{

	            field: 'lrrymc',
	            title: '录入人员',
	            width: '10%',
	            align: 'left',
	            visible: false
	        },{
	            field: 'lrsj',
	            title: '录入时间',
	            width: '10%',
	            align: 'left',
	            visible: false,
	            sortable: true   
	        }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！"); 
            },
            onDblClickRow: function (row, $element) {
            	sjyz_audit_DealById(row.yzid,"view",$("#verifi_audit_formSearch #btn_view").attr("tourl"));
             },
		});
		$("#verifi_audit_formSearch #verifi_audit_list").colResizable({
            liveDrag:true, 
            gripInnerHtml:"<div class='grip'></div>", 
            draggingClass:"dragging", 
            resizeMode:'fit', 
            postbackSafe:true,
            partialRefresh:true        
	  });
	}
	
	//是否上传格式化
	function sfscFormat(value,row,index){
	    if(row.sfsc=="1"){
	        var sfsc="<span>是</span>"
	        return sfsc;
	    }else{
	        var sfsc="<span style='color:red;'>否</span>"
	        return sfsc;
	    }
	}


	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   // 页面大小
        	pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "sjyz.yzid", // 防止同名排位用
            sortLastOrder: "asc", // 防止同名排位用
            zt: "10"
            // 搜索框使用
            // search:params.search
        };
		var cxtj=$("#verifi_audit_formSearch #cxtj").val();
		var cxnr=$.trim(jQuery('#verifi_audit_formSearch #cxnr').val());
		if(cxtj=="0"){
			map["ybbh"]=cxnr
		}else if(cxtj=="1"){
			map["nbbm"]=cxnr
		}else if(cxtj=="2"){
			map["hzxm"]=cxnr
		}else if(cxtj=="3"){
			map["db"]=cxnr
		}else if(cxtj=="4"){
			map["bz"]=cxnr
		}else if(cxtj=="5"){
			map["all_param"]=cxnr
		}
		map["dqshzt"] = 'dsh';
		//验证类别
		var sfnbbms = jQuery('#verifi_audit_formSearch #sfnbbm_id_tj').val();
		map["sfnbbms"] = sfnbbms;
		var yzlbs = jQuery('#verifi_audit_formSearch #yzlb_id_tj').val();
		map["yzlbs"] = yzlbs;
		//验证结果
		var yzjgs = jQuery('#verifi_audit_formSearch #yzjg_id_tj').val();
		map["yzjgs"] = yzjgs;
		//客户通知
		var khtzs = jQuery('#verifi_audit_formSearch #khtz_id_tj').val();
		map["khtzs"] = khtzs;
		//区分
		var qfs = jQuery('#verifi_audit_formSearch #qf_id_tj').val();
		map["qfs"] = qfs;
		//标本类型
		var yblxs = jQuery('#verifi_audit_formSearch #yblx_id_tj').val();
		map["yblxs"] = yblxs;
		//检测项目
		var jcxms = jQuery('#verifi_audit_formSearch #jcxm_id_tj').val();
		map["jcxms"] = jcxms;
		var jcdws=jQuery('#verifi_audit_formSearch #jcdw_id_tj').val()
		map["jcdws"]=jcdws;
		var start=jQuery('#verifi_audit_formSearch #start').val()
		map["start"]=start;
		var end=jQuery('#verifi_audit_formSearch #end').val()
		map["end"]=end;

		return map;
	}
	return oTableInit;
}

var sbyzAudited_TableInit=function(){
	var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
    	$("#verifi_audit_audited #tb_list").bootstrapTable({
			url: '/verification/verification/pageGetListVerifi_audit',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#verifi_audit_audited #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "shxx.shsj",				//排序字段
            sortOrder: "desc",                  //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 15,                       //每页的记录行数（*）
            pageList: [15, 30, 50, 100],        //可供选择的每页的行数（*）
            paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                      //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "shxx_shxxid",            //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                  //是否显示父子表
            isForceTable:true,
            columns: [{
                checkbox: true
            },{
	            field: 'ybbh',
	            title: '标本编号',
	            width: '10%',
	            align: 'left',
	            visible: true
	        },{
	            field: 'nbbm',
	            title: '内部编码',
	            width: '10%',
	            align: 'left',
	            visible:true
	        },{
	            field: 'hzxm',
	            title: '患者姓名',
	            width: '6%',
	            align: 'left',
	            visible: true
	        },{
	            field: 'nl',
	            title: '年龄',
	            width: '3%',
	            align: 'left',
	            visible: false
	        },{
	            field: 'yblxmc',
	            title: '标本类型',
	            width: '8%',
	            align: 'left',
	            visible: true
	        },{
	            field: 'db',
	            title: '合作伙伴',
	            width: '8%',
	            align: 'left',
	            visible: false
	        },{
	            field: 'sjph',
	            title: '试剂批号',
	            width: '8%',
	            align: 'left',
	            visible: true
	        },{
	            field: 'ywbh',
	            title: '引物编号',
	            width: '8%',
	            align: 'left',
	            visible: true
	        },{
	            field: 'yzlbmc',
	            title: '验证类别',
	            width: '7%',
	            align: 'left',
	            visible: true
	        },{
	            field: 'yzjgmc',
	            title: '验证结果',
	            width: '10%',
	            align: 'left',
	            visible: true
	        },{
                field: 'shxx_sqsj',
                title: '申请时间',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_lrryxm',
                title: '审核人',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'shxx_shsj',
                title: '审核时间',
                width: '11%',
                align: 'left',
                visible: true
            },{
				field: 'shxx_shyj',
				title: '审核意见',
				width: '6%',
				align: 'left',
				visible: true
			},{
                field: 'shxx_sftgmc',
                title: '是否通过',
                width: '5%',
                align: 'left',
                visible: true
            },{
                field: 'sfsc',
                title: '是否上传',
                width: '5%',
                align: 'left',
                formatter:ssfscFormat,
                visible: true
            },{
                field: 'lrsj',
                align: 'center',
                title: '录入时间',
                width: '6%',
                visible: false
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	sjyz_audit_DealById(row.yzid,"view",$("#verifi_audit_formSearch #btn_view").attr("tourl"));
            },
        });
    };
	//是否上传格式化
	function ssfscFormat(value,row,index){
	    if(row.sfsc=="1"){
	        var sfsc="<span>是</span>"
	        return sfsc;
	    }else{
	        var sfsc="<span style='color:red;'>否</span>"
	        return sfsc;
	    }
	}
    //得到查询的参数
    oTableInit.queryParams = function(params){
    	//请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
    	//例如 toolbar 中的参数 
    	//如果 queryParamsType = ‘limit’ ,返回参数必须包含 
    	//limit, offset, search, sort, order 
    	//否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder. 
    	//返回false将会终止请求
        var map = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   //页面大小
        	pageNumber: (params.offset / params.limit) + 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名  
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "sjyz.yzid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

        var cxtj=$("#verifi_audit_audited #cxtj").val();
		var cxnr=$.trim(jQuery('#verifi_audit_audited #cxnr').val());
		if(cxtj=="0"){
			map["ybbh"]=cxnr
		}else if(cxtj=="1"){
			map["nbbm"]=cxnr
		}else if(cxtj=="2"){
			map["hzxm"]=cxnr
		}else if(cxtj=="3"){
			map["db"]=cxnr
		}else if(cxtj=="4"){
			map["bz"]=cxnr
		}
    	map["dqshzt"] = 'ysh';
		//验证类别
		var sfnbbms = jQuery('#verifi_audit_audited #sfnbbm_id_tj').val();
		map["sfnbbms"] = sfnbbms;
		var sftg = jQuery('#verifi_audit_audited #sftg_id_tj').val();
		map["shxx_sftg"] = sftg;
		var yzlbs = jQuery('#verifi_audit_audited #yzlb_id_tj').val();
		map["yzlbs"] = yzlbs;
		//验证结果
		var yzjgs = jQuery('#verifi_audit_audited #yzjg_id_tj').val();
		map["yzjgs"] = yzjgs;
		//客户通知
		var khtzs = jQuery('#verifi_audit_audited #khtz_id_tj').val();
		map["khtzs"] = khtzs;
		//区分
		var qfs = jQuery('#verifi_audit_audited #qf_id_tj').val();
		map["qfs"] = qfs;
		//标本类型
		var yblxs = jQuery('#verifi_audit_audited #yblx_id_tj').val();
		map["yblxs"] = yblxs;
		//检测项目
		var jcxms = jQuery('#verifi_audit_audited #jcxm_id_tj').val();
		map["jcxms"] = jcxms;
		var jcdws=jQuery('#verifi_audit_audited #jcdw_id_tj').val()
		map["jcdws"]=jcdws;
		var start=jQuery('#verifi_audit_audited #start').val()
		map["start"]=start;
		var end=jQuery('#verifi_audit_audited #end').val()
		map["end"]=end;
    	return map;
    };
    return oTableInit;
}

//添加日期控件
laydate.render({
	elem: '#start'
	,theme: '#2381E9'
});
//添加日期控件
laydate.render({
	elem: '#end'
	,theme: '#2381E9'
});

function sjyz_audit_DealById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action=="view"){
		var url=tourl+"?yzid="+id;
		$.showDialog(url,'查看信息',viewVerification_audit_Config);
	}else if(action=="audit"){
		showAuditDialogWithLoading({
			id: id,
			type: 'AUDIT_VERIFICATION',
			url:$("#verifi_audit_formSearch #btn_audit").attr("tourl"),
			data:{'ywzd':'yzid'},
			title:"送检验证审核",
			preSubmitCheck:'preSubmitVerification',
			callback:function(){
				searchSjyz_audit_Result();//回调
			},
			dialogParam:{width:1000}
		});
	}else if(action =='confirm'){
		showAuditDialogWithLoading({
			id: id,
			type: 'AUDIT_VERIFICATION',
			url:$("#verifi_audit_formSearch #btn_confirm").attr("tourl"),
			data:{'ywzd':'yzid'},
			title:"送检验证审核",
			preSubmitCheck:'preConfirmVerification',
			callback:function(){
				searchSjyz_audit_Result();//回调
			},
			dialogParam:{width:1000}
		});
	}else if(action='pcrdockingfile'){//生成pcr对接文档
		var url=tourl+"?ids="+id;
		$.showDialog(url,'生成pcr对接文档',pcrfileVerificationConfig);
	}
}

function preSubmitVerification(){
	if ($("#verificationForm  #yzjg").val()){
		let data = JSON.parse($("#verificationForm #yzjgjson").val());
		let yzjg = "";
		$.each(data,function(i){
			yzjg += $("#verificationForm  input[name='"+data[i].csid+"']:checked").val();
			if (i != data.length-1){
				yzjg +=",";
			}
		});
		$("#verificationForm  input[name='yzjg']").val(yzjg);
	}
	if ($("#verifyModAuditForm  #yzjg").val()){
		let data = JSON.parse($("#verifyModAuditForm  #yzjg").val());
		let yzjg = "";
		$.each(data,function(i){
			let jg = ''
			if (typeof($("#verifyModAuditForm input[name='"+data[i].csid+"']:checked").val()) != 'undefined'){
				jg = $("#verifyModAuditForm input[name='"+data[i].csid+"']:checked").val();
			}else{
				jg = $("#verifyModAuditForm input[name='"+data[i].csid+"']").val();
				let str = jg.split(":")
				jg = str[0]+":";
			}
			yzjg += jg;
			if (i != data.length-1){
				yzjg +=",";
			}
		});
		$("#verifyModAuditForm  input[name='yzjg']").val(yzjg);
	}
	if($("#fixtop #sftg").val()=="1"&&$("#verificationForm #lastStep_flg").val()=="true"){
		if($("#verificationForm #yzjg").val()==""||$("#verificationForm #yzjg").val()==null){
			$.error("请选择验证结果！");
			return false;
		}
	}
	return true;
}

function preConfirmVerification(){
	if ($("#ajaxConfirmForm  #yzjg").val()){
		let data = JSON.parse($("#ajaxConfirmForm  #yzjg").val());
		let yzjg = "";
		$.each(data,function(i){
			yzjg += $("#ajaxConfirmForm  input[name='"+data[i].csid+"']:checked").val();
			if (i != data.length-1){
				yzjg +=",";
			}
		});
		$("#ajaxConfirmForm  input[name='yzjg']").val(yzjg);
	}
	if ($("#verifyModAuditForm  #yzjg").val()){
		let data = JSON.parse($("#verifyModAuditForm  #yzjg").val());
		let yzjg = "";
		$.each(data,function(i){
			yzjg += $("#verifyModAuditForm  input[name='"+data[i].csid+"']:checked").val();
			if (i != data.length-1){
				yzjg +=",";
			}
		});
		$("#verifyModAuditForm  input[name='yzjg']").val(yzjg);
	}
	if($("#fixtop #sftg").val()=="1"&&$("#ajaxConfirmForm #lastStep_flg").val()=="true"){
		if($("#ajaxConfirmForm #yzjg").val()==""||$("#ajaxConfirmForm #yzjg").val()==null){
			$.error("请选择验证结果！");
			return false;
		}
	}
	return true;
}

var verifi_audit_oButtton=function(){
	var oInit=new Object();
	var postdata = {};
	oInit.Init=function(){
		var btn_query=$("#verifi_audit_formSearch #btn_query");//模糊查询
		var btn_queryAudited = $("#verifi_audit_audited #btn_query");//审核记录列表模糊查询
    	var btn_cancelAudit = $("#verifi_audit_audited #btn_cancelAudit");//取消审核
    	var btn_view = $("#verifi_audit_formSearch #btn_view");//查看页面
		var btn_confirm = $("#verifi_audit_formSearch #btn_confirm");//确认
    	var btn_audit = $("#verifi_audit_formSearch #btn_audit");//审核
		var btn_pcrdockingfile = $("#verifi_audit_formSearch #btn_pcrdockingfile");//生成PCR对接文档
    	/*-----------------------模糊查询(审核列表)------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchSjyz_audit_Result(true);
    		});
		}
		btn_confirm.unbind("click").click(function(){
			var sel_row = $('#verifi_audit_formSearch #verifi_audit_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				sjyz_audit_DealById(sel_row[0].yzid,"confirm",btn_confirm.attr("tourl"));
			}else{
				sjyz_audit_DealById(null,"confirm",btn_confirm.attr("tourl"));
			}
		});
		/*-----------------------模糊查询(审核记录)------------------------------------*/
		if(btn_queryAudited!=null){
			btn_queryAudited.unbind("click").click(function(){
				searchSjyzAudited(true);
    		});
		}
		/*---------------------------查看页面--------------------------------*/
		btn_view.unbind("click").click(function(){
			var sel_row=$('#verifi_audit_formSearch #verifi_audit_list').bootstrapTable('getSelections');
			if(sel_row.length==1){
				sjyz_audit_DealById(sel_row[0].yzid,"view",btn_view.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		/*---------------------------审核--------------------------------*/
    	btn_audit.unbind("click").click(function(){
    		var sel_row = $('#verifi_audit_formSearch #verifi_audit_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			sjyz_audit_DealById(sel_row[0].yzid,"audit",btn_audit.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/*---------------------------查看审核记录--------------------------------*/
    	//选项卡切换事件回调
    	$('#verifi_formAudit #verifi_audit_ul a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
    		var _hash = e.target.hash.replace("#",'');
    		if(!e.target.isLoaded){//只调用一次
    			if(_hash=='verifi_auditing'){
    				var oTable= new verifi_audit_TableInit();
    				oTable.Init();
    				
    			}else{
    				var oTable= new sbyzAudited_TableInit();
    				oTable.Init();
    			}
    			e.target.isLoaded = true;
    		}else{//重新加载
    			//$(_gridId + ' #tb_list').bootstrapTable('refresh');
    		}
    	}).first().trigger('shown.bs.tab');//触发第一个选中事件
    	/*-------------------------------------取消审核-----------------------------------*/
    	if(btn_cancelAudit!=null){
    		btn_cancelAudit.unbind("click");
    		btn_cancelAudit.click(function(){
    			cancelAudit($('#verifi_audit_audited #tb_list').bootstrapTable('getSelections'),function(){
    				searchSjyzAudited();
    			});
    		})
    	};
        //---------------------------------生成pcr对接文档---------------------------------------
		btn_pcrdockingfile.unbind("click").click(function(){
			var sel_row = $('#verifi_audit_formSearch #verifi_audit_list').bootstrapTable('getSelections');//获取选择行数据
			var ids="";
			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
				if(sel_row[i].nbbm == '' || sel_row[i].nbbm == undefined || sel_row[i].nbbm == null){
					$.error("存在内部编码为空的数据!");
					return;
				}
				ids= ids + ","+ sel_row[i].yzid;
			}
			ids=ids.substr(1);
			sjyz_audit_DealById(ids,"pcrdockingfile",btn_pcrdockingfile.attr("tourl"));
		});
		//------------------------------------------------------------------
		/**显示隐藏**/
		$("#verifi_auditing #sl_searchMore").on("click", function(ev){
			var ev=ev||event;
			if(Verification_turnOff){
				$("#verifi_auditing #searchMore").slideDown("low");
				Verification_turnOff=false;
				this.innerHTML="基本筛选";
			}else{
				$("#verifi_auditing #searchMore").slideUp("low");
				Verification_turnOff=true;
				this.innerHTML="高级筛选";
			}
			ev.cancelBubble=true;
			//showMore();
		});
		//------------------------------------------------------------------
		/**显示隐藏**/
		$("#verifi_audit_audited #sl_searchMore").on("click", function(ev){
			var ev=ev||event;
			if(Verification_turnOff){
				$("#verifi_audit_audited #searchMore").slideDown("low");
				Verification_turnOff=false;
				this.innerHTML="基本筛选";
			}else{
				$("#verifi_auditing #searchMore").slideUp("low");
				$("#verifi_audit_audited #searchMore").slideUp("low");
				Verification_turnOff=true;
				this.innerHTML="高级筛选";
			}
			ev.cancelBubble=true;
			//showMore();
		});

	}
	return oInit;
}


var viewVerification_audit_Config = {
		width		: "1000px",
		modalName	:"viewVerificationConfig",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};
/**
 * PCR模态框
 */
var pcrfileVerificationConfig = {
	width		: "1000px",
	modalName	: "dockingPCRModel",
	formName	: "dockingPCRYz_Form",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#dockingPCRYz_Form").valid()){
					return false;
				}
				$("input[name='nbbm']").attr("style","border-color:#ccc;height:30px;padding:6px 3px;");
				var nbbms=[];
				var xhs=[];
				var nbbm;
				for(i=1;i<13;i++){
					for(j=1;j<9;j++){
						nbbm=$("#nbbm"+i+"-"+j).val();
						if( nbbm!=null && nbbm!=''){
							nbbms.push(nbbm);
							xhs.push(i+12*(j-1));
							// var nbbmcxcs=0;//用于判断内部编号是否重复
							// for(var l=0;l<nbbms.length;l++){
							// 	if(nbbms[l]==nbbm && nbbm!=""){
							// 		nbbmcxcs++;
							// 		if(nbbmcxcs>=2&& i!=5 && i!=6 && i!=11 && i!=12 &&
							// 			nbbm!="NC-MTM" && nbbm!="PC-MTM" && nbbm!="NC-ACP" && nbbm!="PC-ACP"){
							// 			$.confirm("阴阳性对照以外的内部编号不允许重复!"+"(编号信息:"+nbbm+")");
							// 			return false;
							// 		}
							// 	}
							// }
						}else if(  nbbm==null || nbbm==''  ){
							continue;
							return false;
						}
					}
				}
				if(nbbms==''){
					$.confirm("内容不能为空!");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};

				$("#dockingPCRYz_Form #nbbms").val(nbbms);
				$("#dockingPCRYz_Form #xhs").val(xhs);
				$("#dockingPCRYz_Form input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"]||"dockingPCRYz_Form",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						jQuery('<form action="/verification/file/pagedataPredownPcrFile" method="POST">' +  // action请求路径及推送方法
							'<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
							'<input type="text" name="wjm" value="'+responseText["wjm"]+'"/>' +
							'<input type="text" name="wjlj" value="'+responseText["wjlj"]+'"/>' +
							'<input type="text" name="newWjlj" value="'+responseText["newWjlj"]+'"/>' +
							'</form>')
							.appendTo('body').submit().remove();

						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
						});
					}else if(responseText["status"] == "caution"){
						preventResubmitForm(".modal-footer > button", false);
//							sfbc=0;
						$.confirm(responseText["message"],function(result) {
							if(result){
								sfbc=1;
								$("#btn_success").click();
							}
						});
						var notexitnbbm=responseText["notexitnbbms"];
						var allnbbmlist=$("input[name='nbbm']");
						for(var i=0;i<notexitnbbm.length;i++){
							for(var j=0;j<allnbbmlist.length;j++){
								if(allnbbmlist[j].value==notexitnbbm[i]){
									$("#"+allnbbmlist[j].id).attr("style","border-color: #a94442;height:30px;");
								}
							}
						}

					}else if(responseText["status"] == "fail"){
						preventResubmitForm(".modal-footer > button", false);
						sfbc=0;
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


function searchSjyz_audit_Result(isTurnBack){
	//关闭高级搜索条件
	$("#verifi_audit_formSearch #searchMore").slideUp("low");
	Verification_turnOff=true;
	$("#verifi_audit_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#verifi_audit_formSearch #verifi_audit_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#verifi_audit_formSearch #verifi_audit_list').bootstrapTable('refresh');
	}
}

function searchSjyzAudited(isTurnBack){
	//关闭高级搜索条件
	$("#verifi_audit_audited #searchMore").slideUp("low");
	Verification_turnOff=true;
	$("#verifi_audit_audited #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#verifi_audit_audited #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#verifi_audit_audited #tb_list').bootstrapTable('refresh');
	}
}
runEvery10Sec_yz = function () {
	if($("#wwcbgNum").length > 0){
		$.ajax({
			"dataType" : 'json',
			"type" : "POST",
			"url" : "/verification/verification/pagedataUnFinishedReportCount",
			"data" : { "access_token" : $("#ac_tk").val()},
			"success" : function(data) {
				$("#wwcbgNum").text(data.wwcbgs);
				if(data.wwcbgs != 0){
					setTimeout( runEvery10Sec_yz, 1000 * 60 );
				}
			}
		});
	}
}

function lookMx(event){
	var titleHtml ="<tr><th><b>标本编码</b></th><th><b>内部编码</b></th><th><b>患者姓名</b></th><th><b>类型</b></th></tr>";
	$.ajax( {
		"dataType" : 'json',
		"type" : "POST",
		"url" : "/verification/verification/pagedataUnFinishedReportList",
		"data" : {
			"access_token" : $("#ac_tk").val()
		},
		"success" : function(map) {
			$("#wwcbgNum").text(map.wwcbgs);
			if(map.unFinishedReportList == null){
				return;
			}
			var ent=  map.unFinishedReportList;
			if(ent.length>0){
				for (var i=0;i<ent.length;i++){
					titleHtml = titleHtml +
					"<tr>" +
					"<td>" + ent[i].ybbh + "</td>" +
					"<td>" + ent[i].nbbm + "</td>" +
					"<td>" + ent[i].hzxm + "</td>" +
					"<td>" + ent[i].lxmc + "</td>" +
					"</tr>";
				}
				$("#tbody").html(titleHtml);
				$('#yzbgBodyHead').click(function(){
					if($("#showName").css("display")=="block"){
						$("#showName").hide();
						$("#tbody").html("");
						$('#yzbgBodyHead').unbind("click");
						return;
					}
				})

				$('#yzshBody').click(function(){
					if($("#showName").css("display")=="block"){
						$("#showName").hide();
						$("#tbody").html("");
						$('#yzshBody').unbind("click");
						return;
					}
				})
				var e_ = window.event || e; // 兼容IE，FF事件源
				var x = e_.clientX,y = e_.clientY; // 获取鼠标位置
				$("#showName").css({"left":x -$("#yzbgBodyHead").offset().left,"top":y , "display":"block"});
			}
		}
	});
}

function dowDiv(){
	$("#showName").hide();
}
$(function(){
	addTj('sfnbbm','00','verifi_audit_formSearch');
	addTj('sfnbbm','00','verifi_audit_audited');
	var oTable= new verifi_audit_TableInit();
	oTable.Init();
	var oButtonInit = new verifi_audit_oButtton();
	oButtonInit.Init();
	$("#verifi_auditing [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
	runEvery10Sec_yz();
	// 所有下拉框添加choose样式
	jQuery('#verifi_audit_formSearch .chosen-select').chosen({width: '100%'});
	jQuery('#verifi_audit_audited .chosen-select').chosen({width: '100%'});
})