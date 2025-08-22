var Experiment_TableInit=function(experimentList,firstFlg){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#experiment_formSearchNew #experiment_newlist").bootstrapTable({
			url: '/experimentS/experimentS/pagedataListExperimentInfo'+($('#experiment_formSearchNew #single_flag').val()?('?single_flag='+$('#experiment_formSearchNew #single_flag').val()):''),         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#experiment_formSearchNew #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   // 是否显示分页（*）
            //paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "sj.sjid",				// 排序字段
            sortOrder: "desc",                   // 排序方式
            queryParams: oTableInit.queryParams,// 传递参数（*）
            sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       // 初始化加载第一页，默认第一页
			pageSize: 15,                       // 每页的记录行数（*）
			pageList: [15, 30, 50, 100],        // 可供选择的每页的行数（*）
            paginationPreText: '‹',				// 指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			// 指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  // 是否显示所有的列
            showRefresh: true,                  // 是否显示刷新按钮
            minimumCountColumns: 2,             // 最少允许的列数
            clickToSelect: true,                // 是否启用点击选中行
            // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "syglid",                     // 每一行的唯一标识，一般为主键列
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
				if ($("#experiment_formSearchNew #btn_viewmore").length>0){
					Experiment_DealByIdNew(row.sjid,'viewmore',$("#experiment_formSearchNew #btn_viewmore").attr("tourl"),row.qf,row.flg_qf);
				}else{
					Experiment_DealByIdNew(row.sjid,'view',$("#experiment_formSearchNew #btn_view").attr("tourl"),row.qf,row.flg_qf);
				}
             },
		});
        $("#experiment_formSearchNew #experiment_newlist").colResizable({
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
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "sj.nbbm", // 防止同名排位用
            sortLastOrder: "asc", // 防止同名排位用
            zt: $("#experiment_formSearchNew #zt").val(), // 防止同名排位用
			ybbhs: $("#experiment_formSearchNew #nbzbm").val(), // 防止同名排位用
            jcxmids: $("#experiment_formSearchNew #jcxmids").val(), // 防止同名排位用
			jcdwxz: $("#experiment_formSearchNew #jcdwxz").val(), // 防止同名排位用
			limitColumns: $("#experiment_formSearchNew #limitColumns").val() //筛选出这部分字段用于列表显示
            // 搜索框使用
            // search:params.search
        };
		return getExperimentSearchDataNew(map);
	};
	return oTableInit;
}

function getExperimentSearchDataNew(map){
	var experiment_select=$("#experiment_formSearchNew #experiment_select").val();
	var experiment_input=$.trim(jQuery('#experiment_formSearchNew #experiment_input').val());
	if(experiment_select=="0"){
		map["ybbh"]=experiment_input
	}else if(experiment_select=="1"){
		map["nbbm"]=experiment_input
	}else if(experiment_select=="2"){
		map["hzxm"]=experiment_input
	}

	var jcxm=jQuery('#experiment_formSearchNew #jcxm_id_tj').val()
	map["jcxms"]=jcxm
	//检测单位
	var jcdws=$("#experiment_formSearchNew #jcdw_id_tj").val();
	map["jcdws"]=jcdws
	return map;
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


function xhformat(value,row,index){
	return index+1;
}

function Experiment_DealByIdNew(id,action,tourl,qf,flg_qf){
	if(!tourl){
		return;
	}
	if(action=="view"){
		var url=tourl+"?sjid="+id+"&qf="+qf+"&flg_qf="+flg_qf+"&jcxmdm="+$("#jcxmdm").val();
		$.showDialog(url,'查看详细信息',viewExperimentConfig);
	}else if(action=="confirm"){
		$.showDialog(tourl,'确认',confirmExperimentConfig);
	}else if(action=="riskboard"){
		var url=tourl+"?syglid="+id;
		$.showDialog(url,'标本退回',riskBoardConfig);
	}else if(action=="upload"){
		var url=tourl+"?sjid="+id+"&qf="+qf+"&flg_qf="+flg_qf;
		$.showDialog(url,'上传文件',uploadDetectionConfig);
	}else if(action=="viewmore"){
		var url=tourl+"?sjid="+id+"&qf="+qf+"&flg_qf="+flg_qf+"&jcxmdm="+$("#jcxmdm").val();
		$.showDialog(url,'详细信息',viewExperimentConfig);
	}else if(action =='sampling'){
		var url= tourl +"?nbbm="+id;
		$.showDialog(url,'取样',samplingExperimentConfig);
	}else if(action =='print'){
		var url= tourl +"?sjid="+id;
		$.showDialog(url,'打印',printExperimentConfig);
	}else if(action == 'adjust'){
		var url=tourl + "?ids=" +id+"&jcxmids="+$("#jcxmids").val()+"&lx=";
		$.showDialog(url,'标本实验调整',adjustBbsyConfig);
	}else if(action == 'extract'){
		var url=tourl + "?ids=" +id;
		$.showDialog(url,'提取新增',extractBbsyConfig);
	}else if(action == 'library'){
		var url=tourl + "?ids=" +id;
		$.showDialog(url,'文库新增',libraryBbsyConfig);
	}
}

var libraryBbsyConfig = {
	width		: "2000px",
	modalName	: "libraryBbsyModal",
	formName	: "ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#ajaxForm").valid()){
					return false;
				}
				$("input[name='nbbh']").attr("style","border-color:#ccc;height:30px;padding:6px 3px;");
				var jtxxs=[];
				var nbbhs=[];
				var tqms=[];
				var xhs=[];
				var syglids=[];
				var jtxx;
				var nbbh;
				var tqm;
				var json=[];
				var syglid='';
				var tip='';
				for(var i=1;i<25;i++){
					for(var j=1;j<9;j++){
						jtxx=$("#lie"+i+"-"+j).val();
						nbbh=$("#nbbh"+i+"-"+j).val();
						tqm=$("#tqm"+i+"-"+j).val();
						if(!tqm){
							tqm='';
						}
						syglid=$("#id"+i+"-"+j).val();
						if(!syglid){
							syglid='';
						}
						if(jtxx!=null && jtxx!='' && nbbh!=null && nbbh!=''){
							if(syglid!=null && syglid!=''){
								var sz={"syglid":'',"jtxx":''};
								sz.syglid=syglid;
								sz.jtxx=jtxx;
								json.push(sz);
							}else{
								twoFind(nbbh,i,j);
								syglid=$("#id"+i+"-"+j).val();
								if (syglid){
									var sz={"syglid":'',"jtxx":''};
									sz.syglid=syglid;
									sz.jtxx=jtxx;
									json.push(sz);
								}else{
									tip=tip+','+nbbh;
								}
							}
							jtxxs.push(jtxx);
							nbbhs.push(nbbh);
							tqms.push(tqm);
							syglids.push(syglid);
							xhs.push($("#nbbh"+i+"-"+j).attr("xh"));
							var jtcxcs=0;//用于判断接头信息是否重复
							var nbbhcxcs=0;//用于判断内部编号是否重复
							for(var l=0;l<jtxxs.length;l++){
								if(jtxxs[l]==jtxx && jtxx!=""){
									jtcxcs++;
									if(jtcxcs>=2){
										$.confirm("接头信息不允许重复!"+"(接头信息:"+jtxx+")");
										return false;
									}
								}
							}
							for(var l=0;l<nbbhs.length;l++){
								if(nbbhs[l]==nbbh && nbbh!=""){
									nbbhcxcs++;
									if(nbbhcxcs>=2){
										$.confirm("内部编号存在重复!"+"(编号信息:"+nbbh+")");
									}
								}
							}
						}else if((jtxx==null || jtxx=='') && (nbbh!=null && nbbh!='')){
							$.confirm("对应接头信息不能为空!");
							return false;
						}else if((jtxx!=null && jtxx!='') && (nbbh==null || nbbh=='')){
							$.confirm("对应内部编码不能为空!");
							return false;
						}
					}
				}
				if(jtxxs == '' && nbbhs==''){
					$.confirm("内容不能为空!");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};

				$("#ajaxForm #jtxxs").val(jtxxs);
				$("#ajaxForm #nbbhs").val(nbbhs);
				$("#ajaxForm #xhs").val(xhs);
				$("#ajaxForm #tqms").val(tqms);
				$("#ajaxForm #syglids").val(syglids);
				$("#ajaxForm #zt_flag").val("1");
				$("#ajaxForm #sfbc").val(sfbc);
				$("#ajaxForm #sygl_json").val(JSON.stringify(json));
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());

				if(tip){
					tip=tip.substring(1);
					$.confirm('内部编码'+tip+'未匹配实验管理数据，可能导致上机无法正确生成文库编号，是否继续保存？',function(result){
						if(result){
							submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
								if(responseText["status"] == 'success'){
									sfbc=0;
									$.success(responseText["message"],function() {
										if(opts.offAtOnce){
											$.closeModal(opts.modalName);
										}
										searchExperimentResult();
									});
								}else if(responseText["status"] == "caution"){
									preventResubmitForm(".modal-footer > button", false);
									sfbc=0;
									$.confirm(responseText["message"],function(result) {
										if(result){
											sfbc=1;
											$("#btn_success").click();
										}
									});
									var notexitnbbh=responseText["notexitnbbhs"];
									var allnbbhlist=$("input[name='nbbh']");
									for(var i=0;i<notexitnbbh.length;i++){
										for(var j=0;j<allnbbhlist.length;j++){
											if(allnbbhlist[j].value==notexitnbbh[i]){
												$("#"+allnbbhlist[j].id).attr("style","border-color: #a94442;height:30px;");
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
						}
					});
				}else{
					submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							sfbc=0;
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								searchExperimentResult();
							});
						}else if(responseText["status"] == "caution"){
							preventResubmitForm(".modal-footer > button", false);
							sfbc=0;
							$.confirm(responseText["message"],function(result) {
								if(result){
									sfbc=1;
									$("#btn_success").click();
								}
							});
							var notexitnbbh=responseText["notexitnbbhs"];
							var allnbbhlist=$("input[name='nbbh']");
							for(var i=0;i<notexitnbbh.length;i++){
								for(var j=0;j<allnbbhlist.length;j++){
									if(allnbbhlist[j].value==notexitnbbh[i]){
										$("#"+allnbbhlist[j].id).attr("style","border-color: #a94442;height:30px;");
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
				}
				return false;
			}
		},
		successtwo : {
			label : "保 存",
			className : "btn-success",
			callback : function() {
				if(!$("#ajaxForm").valid()){
					return false;
				}
				$("input[name='nbbh']").attr("style","border-color:#ccc;height:30px;");
				var jtxxs=[];
				var nbbhs=[];
				var xhs=[];
				var syglids=[];
				var jtxx;
				var nbbh;
				var tqms=[];
				var tqm;
				var json=[];
				var syglid='';
				var tip='';
				for(var i=1;i<25;i++){
					for(var j=1;j<9;j++){
						jtxx=$("#lie"+i+"-"+j).val();
						nbbh=$("#nbbh"+i+"-"+j).val();
						tqm=$("#tqm"+i+"-"+j).val();
						if(!tqm){
							tqm='';
						}
						syglid=$("#id"+i+"-"+j).val();
						if(!syglid){
							syglid='';
						}
						if(nbbh!=null && nbbh!=''){
							if(syglid!=null && syglid!=''){
								var sz={"syglid":'',"jtxx":''};
								sz.syglid=syglid;
								sz.jtxx=jtxx;
								json.push(sz);
							}else{
								twoFind(nbbh,i,j);
								syglid=$("#id"+i+"-"+j).val();
								if (syglid){
									var sz={"syglid":'',"jtxx":''};
									sz.syglid=syglid;
									sz.jtxx=jtxx;
									json.push(sz);
								}else{
									tip=tip+','+nbbh;
								}
							}
							jtxxs.push(jtxx);
							nbbhs.push(nbbh);
							tqms.push(tqm);
							syglids.push(syglid);
							xhs.push($("#nbbh"+i+"-"+j).attr("xh"));
							var jtcxcs=0;//用于判断接头信息是否重复
							var nbbhcxcs=0;//用于判断内部编号是否重复
							for(var l=0;l<jtxxs.length;l++){
								if(jtxxs[l]==jtxx && jtxx!=""){
									jtcxcs++;
									if(jtcxcs>=2){
										$.confirm("接头信息不允许重复!"+"(接头信息:"+jtxx+")");
										return false;
									}
								}
							}
							for(var l=0;l<nbbhs.length;l++){
								if(nbbhs[l]==nbbh && nbbh!=""){
									nbbhcxcs++;
									if(nbbhcxcs>=2){
										$.confirm("内部编号存在重复!"+"(编号信息:"+nbbh+")");
									}
								}
							}
						}else if(nbbh!=null && nbbh!=''){
							jtxxs.push(jtxx);
							nbbhs.push(nbbh);
							tqms.push(tqm);
							syglids.push(syglid);
							if(!syglid){
								tip=tip+','+nbbh;
							}
							xhs.push($("#nbbh"+i+"-"+j).attr("xh"));
						}else if((jtxx!=null && jtxx!='') && (nbbh==null || nbbh=='')){
							$.confirm("对应内部编码不能为空!");
							return false;
						}
					}
				}
				if(jtxxs == '' && nbbhs==''){
					$.confirm("内容不能为空!");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};

				$("#ajaxForm #jtxxs").val(jtxxs);
				$("#ajaxForm #nbbhs").val(nbbhs);
				$("#ajaxForm #xhs").val(xhs);
				$("#ajaxForm #tqms").val(tqms);
				$("#ajaxForm #syglids").val(syglids);
				$("#ajaxForm #zt_flag").val("0");
				$("#ajaxForm #sfbc").val(sfbc);
				$("#ajaxForm #sygl_json").val(JSON.stringify(json));
				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());

				if(tip){
					tip=tip.substring(1);
					$.confirm('内部编码'+tip+'未匹配实验管理数据，可能导致上机无法正确生成文库编号，是否继续保存？',function(result){
						if(result){
							submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
								if(responseText["status"] == 'success'){
									sfbc=0;
									$.success(responseText["message"],function() {
										if(opts.offAtOnce){
											$.closeModal(opts.modalName);
										}
										searchExperimentResult();
									});
								}else if(responseText["status"] == "caution"){
									preventResubmitForm(".modal-footer > button", false);
									sfbc=0;
									$.confirm(responseText["message"],function(result) {
										if(result){
											sfbc=1;
											$("#btn_successtwo").click();
										}
									});
									var notexitnbbh=responseText["notexitnbbhs"];
									var allnbbhlist=$("input[name='nbbh']");
									for(var i=0;i<notexitnbbh.length;i++){
										for(var j=0;j<allnbbhlist.length;j++){
											if(allnbbhlist[j].value==notexitnbbh[i]){
												$("#"+allnbbhlist[j].id).attr("style","border-color: #a94442;height:30px;");
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
						}
					});
				}else{
					submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							sfbc=0;
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								searchExperimentResult();
							});
						}else if(responseText["status"] == "caution"){
							preventResubmitForm(".modal-footer > button", false);
							sfbc=0;
							$.confirm(responseText["message"],function(result) {
								if(result){
									sfbc=1;
									$("#btn_successtwo").click();
								}
							});
							var notexitnbbh=responseText["notexitnbbhs"];
							var allnbbhlist=$("input[name='nbbh']");
							for(var i=0;i<notexitnbbh.length;i++){
								for(var j=0;j<allnbbhlist.length;j++){
									if(allnbbhlist[j].value==notexitnbbh[i]){
										$("#"+allnbbhlist[j].id).attr("style","border-color: #a94442;height:30px;");
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

var extractBbsyConfig = {
	width		: "2000px",
	modalName	: "extractBbsyModal",
	formName	: "ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		successOne : {
			label : "保 存",
			className : "btn-success",
			callback : function() {
				var $this = this;
				var opts = $this["options"]||{};
				var str = "";
				for(var i=1;i<97;i++){
					for(var k=1;k<97;k++){
						if($("#ajaxForm #nbbh-"+i).val() && i!=k && $("#ajaxForm #nbbh-"+i).val() == $("#ajaxForm #nbbh-"+k).val()){
							str += ","+$("#ajaxForm #nbbh-"+i).val();
						}
					}
				}
				if ( str){
					$.confirm("存在提取相同的标本 "+ str.substring(1) + " 是否继续保存",function(result) {
						if(result){
							checkAndSubmit("btn_successOne",true,false,opts)
						}
					});
					return false;
				}
				return checkAndSubmit("btn_successOne",true,false,opts);
			}
		},
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var $this = this;
				var opts = $this["options"]||{};
				$("#ajaxForm #sfgx").val("1");
				var str = "";
				for(var i=1;i<97;i++){
					for(var k=1;k<97;k++){
						if($("#ajaxForm #nbbh-"+i).val() && i!=k && $("#ajaxForm #nbbh-"+i).val() == $("#ajaxForm #nbbh-"+k).val()){
							str += ","+$("#ajaxForm #nbbh-"+i).val();
						}
					}
				}
				if ( str){
					$.confirm("存在提取相同的标本 "+ str.substring(1) + " 是否继续保存",function(result) {
						if(result){
							checkAndSubmit("btn_success",true,false,opts)
						}
					});
					return false;
				}
				return checkAndSubmit("btn_success",true,false,opts);
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
var sfbc=0;//是否继续保存
function checkAndSubmit(buttonName,isCheckNbbh,isCheckHsnd,opts){
	if(!$("#ajaxForm").valid()){
		$.alert("输入格式有误!");
		return false;
	}
	var jcdw=$("#ajaxForm #jcdw").val();
	if(jcdw==null || jcdw==""){
		$.alert("请选择检测单位");
		return false;
	}
	var nbbhs=[];
	var hsnds=[];
	var cdnas=[];
	var xhs=[];
	var jkyls=[];
	var hcyyls=[];
	var xsnds=[];
	var nbzbms=[];
	var tqmxids=[];
	var tqykws=[];
	var tqybms=[];
	var spikes=[];
	var nbbh;
	var hsnd;
	var cdna;
	var nbzbm;
	var tqmxid;
	var xh;
	var tqykw;
	var tqybm;
	var spike;
	for(var i=1;i<97;i++){
		nbbh=$("#ajaxForm #nbbh-"+i).val();
		hsnd=$("#ajaxForm #hsnd-"+i).val();
		cdna=$("#ajaxForm #cdna-"+i).val();
		nbzbm=$("#ajaxForm #nbzbm-"+i).val();
		tqmxid=$("#ajaxForm #tqmxid-"+i).val();
		tqykw=$("#ajaxForm #tqykw-"+i).val();
		spike=$("#ajaxForm #spike-"+i).val();
		xh=i;
		var xsnd;//稀释浓度
		if(hsnd>100){
			xsnd=hsnd/10;
		}else{
			xsnd=hsnd;
		}
//		if(isCheckHsnd){
//			if(nbbh!=null && nbbh!=""){
//				if((hsnd==null || hsnd=="") && (cdna==null || cdna=="")){
//					$.alert("内部编号为"+nbbh+"中的核酸浓度或CDNA至少有一项不能为空!");
//					return false;
//				}
//			}
//		}
		if(nbbh!=null && nbbh!=''){
			if(i<=10){
				tqybm=$("#ajaxForm #tqybm-1").val();
				tqybms.push(tqybm);
			}else if(i>10&&i<=20){
				tqybm=$("#ajaxForm #tqybm-2").val();
				tqybms.push(tqybm);
			}else if(i>20&&i<=30){
				tqybm=$("#ajaxForm #tqybm-3").val();
				tqybms.push(tqybm);
			}else if(i>30&&i<=40){
				tqybm=$("#ajaxForm #tqybm-4").val();
				tqybms.push(tqybm);
			}else if(i>40&&i<=50){
				tqybm=$("#ajaxForm #tqybm-5").val();
				tqybms.push(tqybm);
			}else if(i>50&&i<=60){
				tqybm=$("#ajaxForm #tqybm-6").val();
				tqybms.push(tqybm);
			}else if(i>60&&i<=70){
				tqybm=$("#ajaxForm #tqybm-7").val();
				tqybms.push(tqybm);
			}else if(i>70&&i<=80){
				tqybm=$("#ajaxForm #tqybm-8").val();
				tqybms.push(tqybm);
			}else if(i>80&&i<=90){
				tqybm=$("#ajaxForm #tqybm-9").val();
				tqybms.push(tqybm);
			}else if(i>90&&i<=96){
				tqybm=$("#ajaxForm #tqybm-10").val();
				tqybms.push(tqybm);
			}
			nbbhs.push(nbbh);
			hsnds.push(hsnd);
			cdnas.push(cdna);
			xsnds.push(xsnd);
			nbzbms.push(nbzbm);
			tqmxids.push(tqmxid);
			xhs.push(xh);
			tqykws.push(tqykw);
			spikes.push(spike);
			//若cdna不为空，建库用量为100/cdna
			if(cdna!=null && cdna!=''){
				var jkyl=Math.round(100/cdna);
				//若得到的值大于等于35则取35，若小于取实际值
				if(jkyl>=35){
					jkyl=35;
					jkyls.push(jkyl);
				}else{
					jkyls.push(jkyl);
				}
				//缓冲液用量
				var hcyyl=35-jkyl;
				if(jkyl==35){
					hcyyl=0;
				}
				hcyyls.push(hcyyl);
			}
			//若cdna为空，建库用量为100/稀释浓度
			else{
				var jkyl;
				if(xsnd==null || xsnd ==0 || xsnd==''){
					jkyl=35;
				}else{
					jkyl=Math.round(100/xsnd);
				}
				//若得到的值大于等于35则取35，若小于取实际值
				if(jkyl>=35){
					jkyl=35;
					jkyls.push(jkyl);
				}else{
					jkyls.push(jkyl);
				}
				//缓冲液用量
				var hcyyl=35-jkyl;
				if(jkyl==35){
					hcyyl=0;
				}
				hcyyls.push(hcyyl);
			}
		}else if((nbbh==null || nbbh=='') && (hsnd!=null && hsnd!='') || (cdna!=null && cdna!='')){
			$.alert("对应内部编号不能为空");
			$("#ajaxForm #nbbh-"+i).attr("style","border-color: #a94442");
			return false;
		}
	}
	if(isCheckNbbh){
		if(nbbhs.length<=0){
			$.alert("内容不能为空");
			return false;
		}
	}
	$("#ajaxForm #nbbhs").val(nbbhs);
	$("#ajaxForm #hsnds").val(hsnds);
	$("#ajaxForm #cdnas").val(cdnas);
	$("#ajaxForm #xhs").val(xhs);
	$("#ajaxForm #tqykws").val(tqykws);
	$("#ajaxForm #spikes").val(spikes);
	$("#ajaxForm #tqybms").val(tqybms);
	$("#ajaxForm #sfbc").val(sfbc);
	$("#ajaxForm #jkyls").val(jkyls);
	$("#ajaxForm #hcyyls").val(hcyyls);
	$("#ajaxForm #xsnds").val(xsnds);
	$("#ajaxForm #nbzbms").val(nbzbms);
	$("#ajaxForm #tqmxids").val(tqmxids);
	$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
	$("#"+buttonName).attr("disabled", true);//按钮变灰不可用防止重复提交
	submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
		if(responseText["status"] == 'success'){
			sfbc=0;
			$.success(responseText["message"],function() {
				if(opts.offAtOnce){
					$.closeModal(opts.modalName);
				}
				searchExperimentResult();
			});
		}else if(responseText["status"] == "caution"){
			$("#"+buttonName).attr("disabled", false);
			sfbc=0;
			$.confirm(responseText["message"],function(result) {
				if(result){
					sfbc=1;
					$("#"+buttonName).click();
				}
			});
			var notexitnbbh=responseText["notexitnbbhs"];
			var allnbbhlist=$("input[name='nbbh']");
			for(var i=0;i<notexitnbbh.length;i++){
				for(var j=0;j<allnbbhlist.length;j++){
					if(allnbbhlist[j].value==notexitnbbh[i]){
						$("#"+allnbbhlist[j].id).attr("style","border-color: #a94442;height:30px;");
					}
				}
			}

		}else if(responseText["status"] == "fail"){
			$("#"+buttonName).attr("disabled", false);
			sfbc=0;
			$.error(responseText["message"],function() {
			});
		} else{
			$.alert(responseText["message"],function() {
			});
		}
	},".modal-footer > button");
	preventResubmitForm(".modal-footer > button", false);
	return false;
}

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
						var sz = {"syglid":'',"jcd":'',"sfjs":'',"jsrq":'',"jcbj":'',"syrq":'',"sfqy":'',"qysj":''};
						sz.syglid = t_map.rows[i].syglid;
						sz.jcdw = t_map.rows[i].jcdw;
						sz.sfjs = t_map.rows[i].sfjs;
						sz.jcbj = t_map.rows[i].jcbj;
						sz.jsrq = t_map.rows[i].jsrq;
						sz.syrq = t_map.rows[i].syrq;
						sz.sfqy = t_map.rows[i].sfqy;
						sz.qysj = t_map.rows[i].qysj;
						json.push(sz);
					}
					$("#ajaxFormBbsyAdjust #json").val(JSON.stringify(json));
					$("#ajaxFormBbsyAdjust input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"ajaxFormBbsyAdjust",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
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
var printExperimentConfig = {
	width		: "800px",
	modalName	: "printExperimentModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "打 印",
			className : "btn-primary",
			callback : function() {
				if(!$("#printExperimentForm").valid()){
					return false;
				}
				var suffixs = '';
				var ids = '';
				$("#printExperimentForm #jclx input[type='checkbox']").each(function(index){
					if (this.checked){
						ids+=","+this.value;
						suffixs+=","+$("#printExperimentForm #"+this.value).attr("nbzbm")
					}
				});
				if (suffixs==''){
					$.alert("请选择检测类型！");
					return false;
				}
				$("#printExperimentForm #suffixs").val(suffixs.substring(1));
				$("#printExperimentForm #ids").val(ids.substring(1));

				var $this = this;
				var opts = $this["options"]||{};
				$("#printExperimentForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"printExperimentForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						if(responseText["print"] && responseText["sz_flg"]){
							var list=responseText["print"];
							var sz_flg=responseText["sz_flg"];
							if (list && list.length>0){
								for (let i = 0; i < list.length; i++) {
									print_nbbm_qy(list[i],sz_flg);
								}
							}
						}
						if(opts.offAtOnce){
							$.closeModal(opts.modalName);
						}
						searchExperimentResult();
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
									print_nbbm_qy(list[i],sz_flg);
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
function print_nbbm_qy(host,sz_flg){
	var print_url=null
	if(sz_flg=="0"){
		print_url="http://localhost"+host;
        var openWindow = window.open(print_url);
        setTimeout(function(){
            openWindow.close();
        }, 1200);
	}else if(sz_flg=="1"){
		var glxx=$("#samplingExperimentForm #glxx").val();
		if(!glxx){
			glxx=$("#printExperimentForm #glxx").val();
		}
		print_url="http://"+glxx+host;
		var openWindow = window.open(print_url);
		setTimeout(function(){
			openWindow.close();
		}, 1200);
	}
}

var riskBoardConfig = {
	width		: "900px",
	modalName	: "riskBoardModal",
	formName	: "riskBoardAjaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#riskBoardAjaxForm").valid()){
					$.alert("请填写完整信息！");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				$("#riskBoardAjaxForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"riskBoardAjaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
							}
							//提交审核
							if(responseText["auditType"]!=null){
								showAuditFlowDialog(responseText["auditType"],responseText["ywid"],function(){});
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
				var timers=$("#riskBoardModal").attr("dsq")
                 if(timers){
                    var arr=timers.split("_")
                        for(var i=0;i<arr.length;i++){
                            if(arr[i]!=""){
                              window.clearTimeout(parseInt(arr[i]))
                             }

                        }
                }
				return false;
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default",
			callback : function() {
                         delphoneredis();
                         var timers=$("#riskBoardModal").attr("dsq")
                         if(timers){
                           var arr=timers.split("_")
                           for(var i=0;i<arr.length;i++){
                             if(arr[i]!=""){
                                window.clearTimeout(parseInt(arr[i]))
                             }

                           }
                        }
                      }
		}
	}
};
function delphoneredis(){
 jQuery.ajaxSetup({async:false});
                    var url= "/common/file/pagedataDelPhoneUpload";
                    jQuery.post(url,{"ywlx":$("#ywlx").val(),"ywid":$("#phoneywid").val(),"access_token":$("#ac_tk").val()},function(responseText){
                        setTimeout(function(){

                        },1);

                    },'json');
                    jQuery.ajaxSetup({async:true});
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
			className : "btn-default",
			callback : function() {
				clearInterval(UpList_timer);
				return true;
			}
		}
	}
};



var viewExperimentConfig = {
		width		: "1100px",
		modalName	:"viewExperimentModal",
		offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};
var Experiment_turnOff=true;

function searchExperimentResult(isTurnBack){
	dealSpaceQuery("#experiment_formSearchNew #experiment_input");// 扫码模糊查询去除空格
	$("#experiment_formSearchNew #searchMore").slideUp("low");
	Experiment_turnOff=true;
	$("#experiment_formSearchNew #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#experiment_formSearchNew #experiment_newlist').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#experiment_formSearchNew #experiment_newlist').bootstrapTable('refresh');
	}
}

var Experiment_oButtonNew=function(){
	var oButtonInit = new Object();
	var postdata = {};
	oButtonInit.Init=function(){
		var btn_query=$("#experiment_formSearchNew #btn_query");
		var btn_view = $("#experiment_formSearchNew #btn_view");
		var btn_viewmore = $("#experiment_formSearchNew #btn_viewmore");
		var btn_upload = $("#experiment_formSearchNew #btn_upload");
		var btn_riskboard = $("#experiment_formSearchNew #btn_riskboard"); //风险上机
		var btn_confirm = $("#experiment_formSearchNew #btn_confirm");
		var initTableField = $("#experiment_formSearchNew #initTableField");
		var btn_selectexport = $("#experiment_formSearchNew #btn_selectexport");//选中导出
		var btn_searchexport = $("#experiment_formSearchNew #btn_searchexport");//搜索导出
		var btn_sampling = $("#experiment_formSearchNew #btn_sampling");
		var btn_print = $("#experiment_formSearchNew #btn_print");
		var btn_adjust = $("#experiment_formSearchNew #btn_adjust");//调整
		var btn_extract = $("#experiment_formSearchNew #btn_extract");//提取
		var btn_library = $("#experiment_formSearchNew #btn_library");//文库

		/*---------------------------文库-----------------------------------*/
		btn_library.unbind("click").click(function(){
			var sel_row = $('#experiment_formSearchNew #experiment_newlist').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length>=1){
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids= ids + ","+ sel_row[i].syglid;
				}
				ids=ids.substr(1);
				Experiment_DealByIdNew(ids,"library",btn_library.attr("tourl"));
			}else{
				$.error("请至少选中一行");
			}
		});
		/*---------------------------提取-----------------------------------*/
		btn_extract.unbind("click").click(function(){
			var sel_row = $('#experiment_formSearchNew #experiment_newlist').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length>=1){
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids= ids + ","+ sel_row[i].syglid;
				}
				ids=ids.substr(1);
				Experiment_DealByIdNew(ids,"extract",btn_extract.attr("tourl"));
			}else{
				$.error("请至少选中一行");
			}
		});
		/*---------------------------调整送检信息表-----------------------------------*/
		btn_adjust.unbind("click").click(function(){
			var sel_row = $('#experiment_formSearchNew #experiment_newlist').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length>=1){
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids= ids + ","+ sel_row[i].sjid;
					if ( sel_row[i].flg_qf=='1'){//1为复测，0为送检
						$.error("请选择类型区分为正常送检的数据");
						return;
					}
				}
				ids=ids.substr(1);
				Experiment_DealByIdNew(ids,"adjust",btn_adjust.attr("tourl"));
			}else{
				$.error("请至少选中一行");
			}
		});

		btn_print.unbind("click").click(function(){
			var sel_row = $('#experiment_formSearchNew #experiment_newlist').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				Experiment_DealByIdNew(sel_row[0].sjid,"print",btn_print.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});

		btn_riskboard.unbind("click").click(function(){
			var sel_row = $('#experiment_formSearchNew #experiment_newlist').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				Experiment_DealByIdNew(sel_row[0].syglid,"riskboard",btn_riskboard.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});

		btn_sampling.unbind("click").click(function(){
			var sel_row = $('#experiment_formSearchNew #experiment_newlist').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				Experiment_DealByIdNew(sel_row[0].nbbm !== undefined ? sel_row[0].nbbm : '',"sampling",btn_sampling.attr("tourl"));
			}else{
				Experiment_DealByIdNew("","sampling",btn_sampling.attr("tourl"));
			}
		});
//-----------------------模糊查询ok------------------------------------
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchExperimentResult(true);
				getBgTotal()
    		});
		}

//		上传
		//---------------------------------检验结果上传----------------------------------
		btn_upload.unbind("click").click(function(){
			var sel_row = $('#experiment_formSearchNew #experiment_newlist').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				if(sel_row[0].jcxmkzcs == 'F'){
					Experiment_DealByIdNew(sel_row[0].sjid,"upload",btn_upload.attr("tourl"),sel_row[0].qf,sel_row[0].flg_qf);
				}else {
					$.error("请选择ResFirst项目的数据！");
				}
			}else{
				Experiment_DealByIdNew(null,"upload",btn_upload.attr("tourl"));

			}
		});
//-----------------------查看------------------------------------------
   	btn_view.unbind("click").click(function(){
    		var sel_row = $('#experiment_formSearchNew #experiment_newlist').bootstrapTable('getSelections');// 获取选择行数据
    		if(sel_row.length==1){
    			Experiment_DealByIdNew(sel_row[0].sjid,"view",btn_view.attr("tourl"),sel_row[0].qf,sel_row[0].flg_qf);
    		}else{
    			$.error("请选中一行");
    		}
    	});

//-----------------------确认------------------------------------------
		btn_confirm.unbind("click").click(function(){
			Experiment_DealByIdNew(null,"confirm",btn_confirm.attr("tourl"));
		});
		if(initTableField!=null){
			initTableField.unbind("click").click(function(){
				$.showDialog(titleSelectUrl+"?ywid="+$('#experiment_formSearchNew #lx').val()
					,"列表字段设置", $.extend({},setExperimentConfig,{"width":"1020px"}));
			});
		}
		//-----------------------详情查看------------------------------------------
		btn_viewmore.unbind("click").click(function(){
			var sel_row = $('#experiment_formSearchNew #experiment_newlist').bootstrapTable('getSelections');// 获取选择行数据
			if(sel_row.length==1){
				Experiment_DealByIdNew(sel_row[0].sjid,"viewmore",btn_viewmore.attr("tourl"),sel_row[0].qf,sel_row[0].flg_qf);
			}else{
				$.error("请选中一行");
			}
		});

		/*-----------------------导出------------------------------------*/
		btn_searchexport.unbind("click").click(function(){
			$.showDialog(exportPrepareUrl+"?ywid=EXPERIMENT_SEARCH&expType=search&callbackJs=experimentSearchData"
				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
		});

		btn_selectexport.unbind("click").click(function(){

			var sel_row = $('#experiment_formSearchNew #experiment_newlist').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length>=1){
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids = ids + ","+ sel_row[i].syglid;
				}
				ids = ids.substr(1);
				$.showDialog(exportPrepareUrl+"?ywid=EXPERIMENT_SELECT&expType=select&ids="+ids
					,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
			}else{
				$.error("请选择数据");
			}
		});
		/**显示隐藏**/
		$("#experiment_formSearchNew #sl_searchMore").on("click", function(ev){
			var ev=ev||event;
			if(Experiment_turnOff){
				$("#experiment_formSearchNew #searchMore").slideDown("low");
				Experiment_turnOff=false;
				this.innerHTML="基本筛选";
			}else{
				$("#experiment_formSearchNew #searchMore").slideUp("low");
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
	var jcxmids = jQuery('#experiment_formSearchNew #jcxmids').val();
	var jcdwxz = jQuery('#experiment_formSearchNew #jcdwxz').val();
	map["jcxmids"]=jcxmids;
	map["jcdwxz"]=jcdwxz;
	map["zt"]=$("#experiment_formSearchNew #zt").val();
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="sjid";
	map["sortLastOrder"]="asc";
	map["sortName"]="jcxmmc";
	map["sortOrder"]="asc";

	return getExperimentSearchDataNew(map);
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
							$('#experiment_formSearchNew #experiment_newlist').bootstrapTable('destroy');
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
							$('#experiment_formSearchNew #experiment_newlist').bootstrapTable('destroy');
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
	var zt = $("#experiment_formSearchNew #zt").val()
	if ($("#experiment_formSearchNew .div1").length >0){
		if ($("#experiment_formSearchNew #RFSFlag").val() == '1'){
			$("#experiment_formSearchNew #all_div").attr("class","col-xs-12 col-sm-12 col-md-10 col-lg-10")
		}
		for(var i=0;i<$(".div1").length;i++){
			var id=$(".div1")[i].id;
			if ($("#experiment_formSearchNew #RFSFlag").val() == '1'){
				if (i != 5){
					$("#experiment_formSearchNew #div_"+id).attr("class","col-xs-6 col-sm-6 col-md-2 col-lg-2")
				}
			}
			if ( id == zt){
				$("#experiment_formSearchNew #"+id).attr("style","background: cornflowerblue")
			}else {
				if ($("#experiment_formSearchNew #RFSFlag").val() == '1'){
					if ( id >1 && id < 6){
						$("#experiment_formSearchNew #div_"+id).attr("style","display: none")
					}
				}else{
					if ( id > 6){
						$("#experiment_formSearchNew #div_"+id).attr("style","display: none")
					}
				}
			}

		}
	}
	$("#experiment_formSearchNew #btn_upload").attr("style","display: none;")
	var oButton = new Experiment_oButtonNew();
	    oButton.Init();
	$("#experiment_formSearchNew [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
    jQuery('#experiment_formSearchNew .chosen-select').chosen({width: '100%'});
	$("#experiment_formSearchNew #btn_library").attr("style","display: none;")
	$("#experiment_formSearchNew #btn_extract").attr("style","display: none;")
})

function click_bq(bs){
	if (bs == '4')
		return
	$("#experiment_formSearchNew #zt").val(bs) ;
	if ($("#experiment_formSearchNew .div1").length >0){
		for(var i=0;i<$(".div1").length;i++){
			var id=$(".div1")[i].id;
			if ( id == bs){
				$("#experiment_formSearchNew #"+id).attr("style","background: cornflowerblue")
			}else {
				$("#experiment_formSearchNew #"+id).removeAttr("style")
				if ($("#experiment_formSearchNew #RFSFlag").val() == '1'){
					if ( id >1 && id < 6){
						$("#experiment_formSearchNew #"+id).attr("style","display: none")
					}
				}else{
					if ( id > 6){
						$("#experiment_formSearchNew #"+id).attr("style","display: none")
					}
				}
			}
		}
		if (bs == '0'){
			$("#experiment_formSearchNew #btn_confirm").removeAttr("style")
			$("#experiment_formSearchNew #btn_sampling").removeAttr("style")
		}else {
			$("#experiment_formSearchNew #btn_confirm").attr("style","display: none;")
			$("#experiment_formSearchNew #btn_sampling").attr("style","display: none;")
		}

		if (bs == '1'){
			$("#experiment_formSearchNew #btn_extract").removeAttr("style")
		}else {
			$("#experiment_formSearchNew #btn_extract").attr("style","display: none;")
		}

		if (bs == '7' || bs == '2'){
			$("#experiment_formSearchNew #btn_library").removeAttr("style")
		}else {
			$("#experiment_formSearchNew #btn_library").attr("style","display: none;")
		}

		if (bs*1 >= 8 && $("#experiment_formSearchNew #RFSFlag").val() == '1'){
			$("#experiment_formSearchNew #btn_upload").removeAttr("style")
		}else {
			$("#experiment_formSearchNew #btn_upload").attr("style","display: none;")
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
		"url" : "/experimentS/experimentS/pagedataExperimentTotal",
		"data" : { "access_token" : $("#ac_tk").val(),"single_flag" : $("#experiment_formSearchNew #single_flag").val(),"jcxmids":$("#experiment_formSearchNew #jcxmids").val(),"jcdws":$("#experiment_formSearchNew #jcdw_id_tj").val(),"ybbhs":$("#experiment_formSearchNew #nbzbm").val()},
		"success" : function(data) {
			if (data!=null){
				$("#experiment_formSearchNew #yjs").text(data.yjs);
				$("#experiment_formSearchNew #yjswqy").text(data.yjswqy);
				$("#experiment_formSearchNew #yqy").text(data.yqy);
				$("#experiment_formSearchNew #yqywtq").text(data.yqywtq);
				$("#experiment_formSearchNew #ytq").text(data.ytq);
				$("#experiment_formSearchNew #ytqwjk").text(data.ytqwjk);
				$("#experiment_formSearchNew #yjk").text(data.yjk);
				$("#experiment_formSearchNew #yjkwp").text(data.yjkwp);
				$("#experiment_formSearchNew #yp").text(data.yp);
				$("#experiment_formSearchNew #ypwsj").text(data.ypwsj);
				$("#experiment_formSearchNew #ysj").text(data.ysj);
				$("#experiment_formSearchNew #ysjwbg").text(data.ysjwbg);
				$("#experiment_formSearchNew #ybg").text(data.ybg);
				$("#experiment_formSearchNew #ytqwkz").text(data.ytqwkz);
				$("#experiment_formSearchNew #yjc").text(data.yjc);
				$("#experiment_formSearchNew #yjcwbg").text(data.yjcwbg);
				$("#experiment_formSearchNew #ykzwjc").text(data.ykzwjc);
				$("#experiment_formSearchNew #ykz").text(data.ykz);
			}
		}
	});
}