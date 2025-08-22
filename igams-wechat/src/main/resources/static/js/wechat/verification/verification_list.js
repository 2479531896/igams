var Verification_turnOff=true;

var Verification_TableInit=function(){
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#verification_formSearch #verification_list").bootstrapTable({
			url: '/verification/verification/pageGetListVerifi',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#verification_formSearch #toolbar',                //工具按钮用哪个容器
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
            /*rowStyle:rowStyle,                  //通过自定义函数设置行样式*/
            columns: [{
                checkbox: true,
				width: '3%'
            },{
            	title: '序',
				formatter: function (value, row, index) {
					return index+1;
				},
				titleTooltip:'序',
				width: '3%',
                align: 'left',
                visible:true
            },{
                field: 'ybbh',
                title: '标本编号',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'nbbm',
                title: '内部编码',
                width: '12%',
                align: 'left',
                sortable: true,   
                visible:true
            },{
                field: 'sjph',
                title: '试剂批号',
                width: '7%',
                align: 'left',
                sortable: true,   
                visible:true
            },{
                field: 'ywbh',
                title: '引物编号',
                width: '7%',
                align: 'left',
                sortable: true,   
                visible:true
            },{
                field: 'hzxm',
                title: '患者姓名',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'jcdwmc',
                title: '检测单位',
                width: '6%',
                align: 'left',
                sortable: true,   
                visible: true
            },{
                field: 'nl',
                title: '年龄',
                width: '6%',
                align: 'left',
                sortable: true,   
                visible: false
            },{
                field: 'yblxmc',
                title: '标本类型',
                width: '8%',
                align: 'left',
                visible: false
            },{
                field: 'db',
                title: '合作伙伴',
                width: '8%',
                align: 'left',
                visible: false
            },{
                field: 'yzdm',
                title: '验证代码',
                width: '6%',
                align: 'left',
                visible: true
            },{
                field: 'yzlbmc',
                title: '验证类别',
                width: '15%',
                align: 'left',
                sortable: true, 
                visible: true
            },{
                field: 'qfmc',
                title: '区分',
                width: '4%',
                align: 'left',
                sortable: true, 
                visible: true
            },{
                field: 'jcjg',
                title: '检测结果',
                width: '6%',
                align: 'left',
                // formatter:yzjgFormat,
                sortable: true, 
                visible: true
            },{
				field: 'ysjg',
				title: '疑似结果',
				width: '6%',
				align: 'left',
				// formatter:yzjgFormat,
				sortable: true,
				visible: true
			},{
				field: 'yzjg',
				title: '验证结果',
				width: '6%',
				align: 'left',
				// formatter:yzjgFormat,
				sortable: true,
				visible: true
			},{
                field: 'khtzmc',
                title: '客户通知',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'sfsc',
                title: '上传',
                width: '4%',
                align: 'left',
                formatter:sfscFormat,
                visible: true
            },{
                field: 'bz',
                title: '备注',
                width: '12%',
                align: 'left',
                sortable: true, 
                visible: true
            },{
                field: 'zt',
                title: '状态',
                width: '7%',
                formatter:ztFormat,
                align: 'left',
                visible: true
            },{
                field: 'lrsj_format',
                title: '录入时间',
                width: '10%',
                align: 'left',
                sortable: true, 
                visible: true
            },{
                field: 'lrrymc',
                title: '录入人员',
                width: '6%',
                align: 'left',
                visible: false
            },{
                field: 'cz',
                title: '操作',
                width: '6%',
                formatter:czFormat,
                align: 'center',
                visible: true
			},{
				field: 'sffh',
				title: '是否符合',
				width: '6%',
				formatter:sffhFormat,
				align: 'center',
				visible: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	VerificationDealById(row.yzid,'view',$("#verification_formSearch #btn_view").attr("tourl"));
             },
			rowStyle: function (row, index) {
				//这里有5个取值代表5中颜色['active', 'success', 'info', 'warning', 'danger'];
				if (row.qfdm.indexOf("LAB_")!=-1) {
					return { classes: 'info' }
				}else{
					return {}
				}
			},
		})
		$("#verification_formSearch #verification_list").colResizable({
			liveDrag:true, 
			gripInnerHtml:"<div class='grip'></div>", 
			draggingClass:"dragging", 
			resizeMode:'fit', 
			postbackSafe:true,
			partialRefresh:true
		})
	}
	oTableInit.queryParams=function(params){
		var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        	pageSize: params.limit,   // 页面大小
        	pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "sjyz.yzid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		return getVerificationSearchData(map);
	}
	return oTableInit
}
	
/**送检验证列表的状态格式化函数
 * @returns
 */
function ztFormat(value,row,index) {
    if (row.zt == '00') {
        return '未提交';
    }else if (row.zt == '80') {
    	return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.yzid + "\",event,\"" + row.qfcskz2 + "\")' >审核通过</a>";
    }else if (row.zt == '15') {
    	return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.yzid + "\",event,\"" + row.qfcskz2 + "\")' >审核未通过</a>";
    }else if (row.zt == '20') {
    	return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.yzid + "\",event,\"" + row.qfcskz2 + "\")' >" + row.shxx_dqgwmc + "审核中</a>";
    }else{
    	return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.yzid + "\",event,\"" + row.qfcskz2 + "\")' >" + row.shxx_dqgwmc + "审核中</a>";
    }
}

/**
 * 验证结果设置颜色
 * @param value
 * @param row
 * @param index
 * @returns
 */
function yzjgFormat(value,row,index){
	if(row.yzjgmc=="阴性"){
		return "<span style='color:#00cc33;'>"+row.yzjgmc+"</span>";
	}else if(row.yzjgmc=="阳性"){
		return "<span style='color:#cc0000;'>"+row.yzjgmc+"</span>";
	}else{
		return row.yzjgmc;
	}
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

/**
 * 操作按钮格式化函数
 * @returns
 */
function czFormat(value,row,index) {
	if (row.zt == '10') {
		return "<span class='btn btn-warning' onclick=\"recallMeasurement('" + row.yzid + "',event,'" + row.qfcskz2 + "')\" >撤回</span>";
	}else{
		return "";
	}
}

function sffhFormat(value,row,index) {
	if (row.sffh == '1') {
		return "<span style='color: green'>符合</span>";
	}else if (row.sffh == '0') {
		return "<span style='color: red'>不符合</span>";
	}else{
		return "-";
	}
}

//撤回提交
function recallMeasurement(yzid,event,auditType){
	var msg = '您确定要撤回送检验证吗？';
	$.confirm(msg,function(result){
		if(result){
			doAuditRecall(auditType,yzid,function(){
				searchVerificationResult();
			});
		}
	});
}

function getVerificationSearchData(map){
	var cxtj=$("#verification_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#verification_formSearch #cxnr').val());
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
	//审核流程序号
	var xlcxhs = jQuery('#verification_formSearch #lcxh_id_tj').val();
	map["xlcxhs"] = xlcxhs;
	//有无内部编码
	var sfnbbms = jQuery('#verification_formSearch #sfnbbm_id_tj').val();
	map["sfnbbm"] = sfnbbms;
	//验证类别
	var yzlbs = jQuery('#verification_formSearch #yzlb_id_tj').val();
		map["yzlbs"] = yzlbs;
	//验证结果
	var yzjgs = jQuery('#verification_formSearch #yzjg_id_tj').val();
		map["yzjgs"] = yzjgs;
	//客户通知
	var khtzs = jQuery('#verification_formSearch #khtz_id_tj').val();
		map["khtzs"] = khtzs;
	//区分
	var qfs = jQuery('#verification_formSearch #qf_id_tj').val();
		map["qfs"] = qfs;
	//标本类型
	var yblxs = jQuery('#verification_formSearch #yblx_id_tj').val();
		map["yblxs"] = yblxs;
	//检测项目
	var jcxms = jQuery('#verification_formSearch #jcxm_id_tj').val();
		map["jcxms"] = jcxms;
	var jcdws=jQuery('#verification_formSearch #jcdw_id_tj').val()
	map["jcdws"]=jcdws;
    var start=jQuery('#verification_formSearch #start').val()
    map["start"]=start;
    var end=jQuery('#verification_formSearch #end').val()
    map["end"]=end;
	var load_flag=jQuery('#verification_formSearch #load_flag').val()
	map["load_flag"]=load_flag;
	//审核状态
	var yzzts=jQuery('#verification_formSearch #shzt_id_tj').val()
	map["yzzts"]=yzzts
	return map;
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
/**
 * 按钮初始化
 */
var Verification_ButtonInit= function (){
	var oInit=new Object();
	var postdata = {};
	oInit.Init=function(){
		var btn_query=$("#verification_formSearch #btn_query");
		var btn_mod=$("#verification_formSearch #btn_mod");
		var btn_view=$("#verification_formSearch #btn_view");
		var btn_del = $("#verification_formSearch #btn_del");
		var btn_resubmit= $("#verification_formSearch #btn_resubmit");
		var btn_selectexport = $("#verification_formSearch #btn_selectexport");//选中导出
    	var btn_searchexport = $("#verification_formSearch #btn_searchexport");//搜索导出
    	var btn_pcrdockingfile = $("#verification_formSearch #btn_pcrdockingfile");//生成PCR对接文档
    	var btn_reread = $("#verification_formSearch #btn_reread");//重新读取PCR验证文件
		/*-----------------------模糊查询------------------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchVerificationResult(true); 
    		});
		}
		/*---------------------------修改---------------------------------*/
		btn_mod.unbind("click").click(function(){
			var sel_row=$('#verification_formSearch #verification_list').bootstrapTable('getSelections');
			if(sel_row.length==1){
				if(sel_row[0].zt!="10"){
					VerificationDealById(sel_row[0].yzid,"mod",btn_mod.attr("tourl"));
				}else{
					$.error("审核中，不允许修改!");
				}
			}else{
				$.error("请选中一行");
			}
    	});
		/*---------------------------重新读取---------------------------------*/
		btn_reread.unbind("click").click(function(){
			var sel_row = $('#verification_formSearch #verification_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length!=1){
				$.error("请选中一行");
				return;
			}else {
				$.confirm('您确定要重新读取PCR文件信息吗？',function(result){
					if(result){
						jQuery.ajaxSetup({async:false});
						var url= btn_reread.attr("tourl");
						jQuery.post(url,{"yzid":sel_row[0].yzid,"access_token":$("#ac_tk").val()},function(responseText){
							setTimeout(function(){
								if(responseText["status"] == 'success'){
									$.success(responseText["message"],function() {
										searchVerificationResult();
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
		});
		 /* ------------------------------查看送检验证信息-----------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row=$('#verification_formSearch #verification_list').bootstrapTable('getSelections');
    		if(sel_row.length==1){
    			VerificationDealById(sel_row[0].yzid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	 /* ------------------------------删除送检验证信息-----------------------------*/
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#verification_formSearch #verification_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==0){
    			$.error("请至少选中一行");
    			return;
    		}else {
    			var ids="";
    			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids= ids + ","+ sel_row[i].yzid;
        		}
    			ids=ids.substr(1);
    			$.confirm('您确定要删除所选择的信息吗？',function(result){
        			if(result){
        				jQuery.ajaxSetup({async:false});
        				var url= btn_del.attr("tourl");
        				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
        					setTimeout(function(){
        						if(responseText["status"] == 'success'){
        							$.success(responseText["message"],function() {
        								searchVerificationResult();
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
    	});
    	/*--------------------------------------重新提交-----------------------------------------*/  
    	btn_resubmit.unbind("click").click(function(){
			var sel_row=$('#verification_formSearch #verification_list').bootstrapTable('getSelections');
			if(sel_row.length==1){
				if(sel_row[0].zt =="00" ||sel_row[0].zt =="15"){
					var nbbm = sel_row[0].nbbm;
					if(nbbm!=null&&nbbm!=''){
						var s = nbbm.substr(nbbm.length-1);
						if(s=='B'){
							$.error("内部编码的最后一位为B，即血液，无法提交！");
						}else{
							VerificationDealById(sel_row[0].yzid,"resubmit",btn_resubmit.attr("tourl"));
						}
					}else{
						VerificationDealById(sel_row[0].yzid,"resubmit",btn_resubmit.attr("tourl"));
					}
				}else{
					$.error("当前状态不能提交！");
				}
			}else{
				$.error("请选中一行");
			}
		});
    	//---------------------------------选中导出---------------------------------------
    	btn_selectexport.unbind("click").click(function(){
    		var sel_row = $('#verification_formSearch #verification_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length>=1){
    			var ids="";
        		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids = ids + ","+ sel_row[i].yzid;
        		}
        		ids = ids.substr(1);
        		$.showDialog(exportPrepareUrl+"?ywid=VERIFICATION_SELECT&expType=select&ids="+ids
        				,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    		}else{
    			$.error("请选择数据");
    		}
    	});
    	//搜索导出
    	btn_searchexport.unbind("click").click(function(){
    		$.showDialog(exportPrepareUrl+"?ywid=VERIFICATION_SEARCH&expType=search&callbackJs=VerificationSearchData"
    				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    	});
    	//---------------------------------生成pcr对接文档---------------------------------------
    	btn_pcrdockingfile.unbind("click").click(function(){
    		var sel_row = $('#verification_formSearch #verification_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length == 1){
    			VerificationDealById(sel_row[0].yzid,"pcrdockingfile",btn_pcrdockingfile.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	//------------------------------------------------------------------
    	/**显示隐藏**/   
    	$("#verification_formSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(Verification_turnOff){
    			$("#verification_formSearch #searchMore").slideDown("low");
    			Verification_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#verification_formSearch #searchMore").slideUp("low");
    			Verification_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    		//showMore();
    	});
	}
	return oInit;
}
/**
 * 按钮点击操作
 * @param id
 * @param action
 * @param tourl
 * @returns
 */
function VerificationDealById(id,action,tourl){
	var url= tourl;
	if(!tourl){
		return;
	}
	if(action =='mod'){
		var url= tourl+"?yzid="+id+"&btnFlg=default";
		$.showDialog(url,'修改送检验证',modVerificationConfig);
	}else if(action =='view'){
		var url= tourl+"?yzid="+id;
		$.showDialog(url,'查看送检验证',viewVerificationConfig);
	}else if(action=="resubmit"){
		var url=tourl+"?yzid="+id;
		$.showDialog(url,'重新提交',resubmitVerificationConfig);
	}else if(action=="pcrdockingfile"){//生成pcr对接文档
		var url=tourl+"?yzid="+id;
		$.showDialog(url,'生成pcr对接文档',pcrfileVerificationConfig);
	}
}

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
								xhs.push($("#nbbm"+i+"-"+j).attr("xh"));
								var nbbmcxcs=0;//用于判断内部编号是否重复
								for(var l=0;l<nbbms.length;l++){
									if(nbbms[l]==nbbm && nbbm!=""){
										nbbmcxcs++;
										if(nbbmcxcs>=2){
											$.confirm("内部编号存在重复!"+"(编号信息:"+nbbm+")");
										}
									}
								}
							}else if(  nbbm==null || nbbm==''  ){
								continue;
//								$.confirm("对应内部编码不能为空!");
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
//					$("#ajaxForm #sfbc").val(sfbc);
					
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
							
//							sfbc=0;
//							$.success(responseText["message"],function() {
//								if(opts.offAtOnce){
//									$.closeModal(opts.modalName);
//								}
//								searchVerificationResult();
//							});
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

/*   修改送检信息模态框*/
var modVerificationConfig = {
	width		: "1000px",
	modalName	: "modVerificationModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#verifyModAuditForm").valid()){
					$.alert("请填写完整信息!");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				let data = JSON.parse($("#verifyModAuditForm #yzjg").val());
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
				$("#verifyModAuditForm input[name='yzjg']").val(yzjg);
				$("#verifyModAuditForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"verifyModAuditForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							searchVerificationResult();
						});
					}else if(responseText["status"] == "fail"){
						// preventResubmitForm(".modal-footer > button", false);
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

/**
 * 查看页面模态框
 */
var viewVerificationConfig={
	width		: "1000px",
	modalName	:"viewVerificationModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
}

/**
 * 重新提交
 */
var resubmitVerificationConfig={
		width		: "1000px",
		modalName	:"resubmitVerificationConfig",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "提交",
				className : "btn-primary",
				callback : function() {
					if(!$("#verificationForm").valid()){
						$.alert("请填写完整信息!");
						return false;
					}
					var $this = this;
					var opts = $this["options"]||{};
					let data = JSON.parse($("#verificationForm #yzjgjson").val());
					let yzjg = "";
					$.each(data,function(i){
						let jg = ''
						if (typeof($("#verificationForm input[name='"+data[i].csid+"']:checked").val()) != 'undefined'){
							jg = $("#verificationForm input[name='"+data[i].csid+"']:checked").val();
						}else{
							jg = $("#verificationForm input[name='"+data[i].csid+"']").val();
							let str = jg.split(":")
							jg = str[0]+":";
						}
						yzjg += jg;
						if (i != data.length-1){
							yzjg +=",";
						}
					});
					$("#verificationForm input[name='yzjg']").val(yzjg);
					$("#verificationForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"verificationForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								searchVerificationResult();
							});
						/*	$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								searchVerificationResult();
							});*/
						}else if(responseText["status"] == "fail"){
							// preventResubmitForm(".modal-footer > button", false);
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
}

//提供给导出用的回调函数
function VerificationSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="sjyz.yzid";
	map["sortLastOrder"]="asc";
	map["sortName"]="sjyz.lrsj";
	map["sortOrder"]="asc";
	return getVerificationSearchData(map);
}
/**
 * 列表刷新
 * @param isTurnBack
 * @returns
 */
function searchVerificationResult(isTurnBack){
	//关闭高级搜索条件
	$("#verification_formSearch #searchMore").slideUp("low");
	Verification_turnOff=true;
	$("#verification_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#verification_formSearch #verification_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#verification_formSearch #verification_list').bootstrapTable('refresh');
	}
}

$(function(){
	addTj('sfnbbm','1','verification_formSearch');
    // 1.初始化Table
	var oTable = new Verification_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new Verification_ButtonInit();
    oButtonInit.Init();
    jQuery('#verification_formSearch .chosen-select').chosen({width: '100%'});
    $("#verification_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
})