var specimenExperimental_TableInit=function(experimentList,firstFlg){
	var oTableInit = new Object();
	oTableInit.Init=function(){
		$("#specimenExperimentalForm #specimenExperimental_list").bootstrapTable({
			url: '/sampleExperiment/sampleExperiment/pageGetListSpecimenExperimental'+($('#specimenExperimentalForm #single_flag').val()?('?single_flag='+$('#specimenExperimentalForm #single_flag').val()):''),         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#specimenExperimentalForm #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "sj.jsrq",				// 排序字段
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
            uniqueId: "sjid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: experimentList,
            onLoadSuccess:function(){

            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
				if ($("#specimenExperimentalForm #btn_viewmore").length>0){
					specimenExperimental_DealById(row.sjid,'viewmore',$("#specimenExperimentalForm #btn_viewmore").attr("tourl"));
				}else{
					specimenExperimental_DealById(row.sjid,'view',$("#specimenExperimentalForm #btn_view").attr("tourl"));
				}
             },
		});
        $("#specimenExperimentalForm #specimenExperimental_list").colResizable({
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
			pageSize: params.limit,   // 页面大小
			pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "sj.nbbm", // 防止同名排位用
            sortLastOrder: "asc", // 防止同名排位用
			jcdwxz: $("#specimenExperimentalForm #jcdwxz").val(), // 防止同名排位用
			limitColumns: $("#specimenExperimentalForm #limitColumns").val() //筛选出这部分字段用于列表显示
        };
		return getSpecimenExperimentalSearchData(map);
	};
	return oTableInit;
}

function getSpecimenExperimentalSearchData(map){
	var specimenExperimental_select=$("#specimenExperimentalForm #specimenExperimental_select").val();
	var specimenExperimental_input=$.trim(jQuery('#specimenExperimentalForm #specimenExperimental_input').val());
	if(specimenExperimental_select=="0"){
		map["ybbh"]=specimenExperimental_input
	}else if(specimenExperimental_select=="1"){
		map["nbbm"]=specimenExperimental_input
	}else if(specimenExperimental_select=="2"){
		map["hzxm"]=specimenExperimental_input
	}

	var jcxm=jQuery('#specimenExperimentalForm #jcxm_id_tj').val()
	map["jcxms"]=jcxm
	//检测单位
	var jcdws=$("#specimenExperimentalForm #jcdw_id_tj").val();
	map["jcdws"]=jcdws

	var bbsdwdstart = jQuery('#specimenExperimentalForm #bbsdwdstart').val();
	map["bbsdwdstart"] = bbsdwdstart;

	var bbsdwdend = jQuery('#specimenExperimentalForm #bbsdwdend').val();
	map["bbsdwdend"] = bbsdwdend;
	// 报告日期开始日期
	var bgrqstart = jQuery('#specimenExperimentalForm #bgrqstart').val();
	map["bgrqstart"] = bgrqstart;
	// 报告日期结束日期
	var bgrqend = jQuery('#specimenExperimentalForm #bgrqend').val();
	map["bgrqend"] = bgrqend;
	// 采样日期开始时间
	var cyrqstart = jQuery('#specimenExperimentalForm #cyrqstart').val();
	map["cyrqstart"] = cyrqstart;
	// 采样日期结束时间
	var cyrqend = jQuery('#specimenExperimentalForm #cyrqend').val();
	map["cyrqend"] = cyrqend;
	// 接收日期开始时间
	var jsrqstart = jQuery('#specimenExperimentalForm #jsrqstart').val();
	map["jsrqstart"] = jsrqstart;
	// 接收日期结束时间
	var jsrqend = jQuery('#specimenExperimentalForm #jsrqend').val();
	map["jsrqend"] = jsrqend;
	// 实验日期开始日期
	var syrqstart = jQuery('#specimenExperimentalForm #syrqstart').val();
	map["syrqstart"] = syrqstart;
	// 实验日期结束日期
	var syrqend = jQuery('#specimenExperimentalForm #syrqend').val();
	map["syrqend"] = syrqend;
	return map;
}

function specimenExperimental_DealById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action =='view'){
		var url= tourl + "?sjid=" +id;
		$.showDialog(url,'送检详细信息',	viewSpecimenExperimentalConfig);
	}else if(action =='viewmore'){
		var url= tourl + "?sjid=" +id;
		$.showDialog(url,'送检详细信息',viewmoreSpecimenExperimentalConfig);
	}else if(action=="riskboard"){
		var url=tourl+"?ywid="+id;
		$.showDialog(url,'标本退回',riskBoardConfig);
	}else if(action =='adjust'){
		var url=tourl + "?sjid=" +id;
		$.showDialog(url,'调整送检信息',adjustSpecimenExperimentalConfig);
	}else if(action =='mod'){
		var url=tourl + "?sjid=" +id+"&xg_flg=1";
		$.showDialog(url,'修改送检信息',modSpecimenExperimentalConfig);
	}else if(action =='confirm'){
		if(id!=null){
			var url= tourl+"?sjid="+id;
		}else{
			var url= tourl;
		}
		$.showDialog(url,'送检收样确认',confirmSpecimenExperimentalConfig);
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
var confirmSpecimenExperimentalConfig = {
	width		: "1200px",
	modalName	: "confirmSpecimenExperimentalModal",
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
										print_nbbm(host,sz_flg);
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
										$("#ajaxForm #yblxdm").val("");
										$("#ajaxForm #jcdwmc").val("");
										$("#ajaxForm #yblx_flg").val("");
										$("#ajaxForm #state").val("");
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
										$("#ajaxForm #button").remove()
										searchSpecimenExperimentalResult();
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
								print_nbbm(host,sz_flg);
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
								$("#ajaxForm #yblxdm").val("");
								$("#ajaxForm #jcdwmc").val("");
								$("#ajaxForm #yblxmc").text("");
								$("#ajaxForm #yblx_flg").val("");
								$("#ajaxForm #state").val("");
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
								$("#ajaxForm #button").remove()
								searchSpecimenExperimentalResult();
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
			className : "btn-default",
			callback : function() {
				if (!$("#ajaxForm #key").val())
					return true;
				$.ajax({
					url: "/sseEmit/connect/pagedataCloseOA",
					data: {
						"access_token": $("#ac_tk").val(),
						"key": $("#ajaxForm #key").val(),
					}
				});
				return true;
			}
		}
	}
};

/*   修改送检信息模态框*/
var modSpecimenExperimentalConfig = {
	width		: "1000px",
	modalName	: "modSpecimenExperimentalModal",
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
	searchSpecimenExperimentalResult();
}
/*   调整送检信息模态框*/
var adjustSpecimenExperimentalConfig = {
	width		: "1200px",
	modalName	: "adjustSpecimenExperimentalModal",
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
						var sz = {"syglid":'',"jcdw":''};
						sz.syglid = t_map.rows[i].syglid;
						sz.jcdw = t_map.rows[i].jcdw;
						json.push(sz);
					}
					$("#ajaxFormAdjust #json").val(JSON.stringify(json));
					$("#ajaxFormAdjust input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"ajaxFormAdjust",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
								searchSpecimenExperimentalResult();
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

/*查看异常清单模态框*/
var exceptionListConfig = {
	width		: "800px",
	modalName	:"addExceptionModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				if(!$("#editExceptionForm").valid()){
					$.alert("请填写完整信息");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				var tzrys=$("#tzrys").val();
				// if(tzrys.length<=0){
				// 	$.confirm("通知人员不能为空");
				// 	return false;
				// }
				$("#editExceptionForm input[name='access_token']").val($("#ac_tk").val());
				submitForm("editExceptionForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								if ($("#editExceptionForm #key").val()){
									$.ajax({
										url: $("#editExceptionForm #urlPrefix").val()+"/sseEmit/connect/pagedataCloseOA",
										data: {
											"access_token": $("#ac_tk").val(),
											"key": $("#editExceptionForm #key").val(),
										}
									});
								}
								$.closeModal(opts.modalName);
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

/*查看送检信息模态框*/
var viewSpecimenExperimentalConfig = {
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

/*查看送检信息模态框*/
var viewmoreSpecimenExperimentalConfig = {
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
var specimenExperimental_turnOff=true;

function searchSpecimenExperimentalResult(isTurnBack){
	dealSpaceQuery("#specimenExperimentalForm #specimenExperimental_input");// 扫码模糊查询去除空格
	$("#specimenExperimentalForm #searchMore").slideUp("low");
	specimenExperimental_turnOff=true;
	$("#specimenExperimentalForm #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#specimenExperimentalForm #specimenExperimental_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#specimenExperimentalForm #specimenExperimental_list').bootstrapTable('refresh');
	}
}

var specimenExperimental_oButton=function(){
	var oButtonInit = new Object();
	var postdata = {};
	oButtonInit.Init=function(){
		var btn_query=$("#specimenExperimentalForm #btn_query");
		var btn_view = $("#specimenExperimentalForm #btn_view");
		var btn_viewmore = $("#specimenExperimentalForm #btn_viewmore");
		var btn_confirm = $("#specimenExperimentalForm #btn_confirm");
		var btn_mod = $("#specimenExperimentalForm #btn_mod");
		var btn_adjust = $("#specimenExperimentalForm #btn_adjust");
		var initTableField = $("#specimenExperimentalForm #initTableField");//字段选择
		var btn_riskboard = $("#specimenExperimentalForm #btn_riskboard");
		var btn_selectexport = $("#specimenExperimentalForm #btn_selectexport");
		var btn_searchexport = $("#specimenExperimentalForm #btn_searchexport");

		//添加日期控件
		laydate.render({
			elem: '#specimenExperimentalForm #syrqstart'
		});
		//添加日期控件
		laydate.render({
			elem: '#specimenExperimentalForm #syrqend'
		});
		//添加日期控件
		laydate.render({
			elem: '#specimenExperimentalForm #cyrqstart'
		});
		//添加日期控件
		laydate.render({
			elem: '#specimenExperimentalForm #cyrqend'
		});
		//添加日期控件
		laydate.render({
			elem: '#specimenExperimentalForm #jsrqstart'
		});
		//添加日期控件
		laydate.render({
			elem: '#specimenExperimentalForm #jsrqend'
		});
		//添加日期控件
		laydate.render({
			elem: '#specimenExperimentalForm #bgrqstart'
		});
		//添加日期控件
		laydate.render({
			elem: '#specimenExperimentalForm #bgrqend'
		});

		// 	  ---------------------------导出-----------------------------------
		btn_selectexport.unbind("click").click(function(){
			var sel_row = $('#specimenExperimentalForm #specimenExperimental_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length>=1){
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids = ids + ","+ sel_row[i].sjid;
				}
				ids = ids.substr(1);
				$.showDialog(exportPrepareUrl+"?ywid=BBSY_SELECT&expType=select&ids="+ids
					,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
			}else{
				$.error("请选择数据");
			}
		});

		btn_searchexport.unbind("click").click(function(){
			$.showDialog(exportPrepareUrl+"?ywid=BBSY_SEARCH&expType=search&callbackJs=BbsySearchData"
				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
		});


		/*---------------------------调整送检信息-----------------------------------*/
		btn_adjust.unbind("click").click(function(){
			var sel_row = $('#specimenExperimentalForm #specimenExperimental_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				specimenExperimental_DealById(sel_row[0].sjid,"adjust",btn_adjust.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});

		/*---------------------------修改送检信息-----------------------------------*/
		btn_mod.unbind("click").click(function(){
			var sel_row = $('#specimenExperimentalForm #specimenExperimental_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				specimenExperimental_DealById(sel_row[0].sjid,"mod",btn_mod.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		/*---------------------------风险上机-----------------------------------*/
		btn_riskboard.unbind("click").click(function(){
			var sel_row = $('#specimenExperimentalForm #specimenExperimental_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				specimenExperimental_DealById(sel_row[0].sjid,"riskboard",btn_riskboard.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});

		//送检收样确认
		btn_confirm.unbind("click").click(function(){
			var sel_row = $('#specimenExperimentalForm #specimenExperimental_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				specimenExperimental_DealById(sel_row[0].sjid,"confirm",btn_confirm.attr("tourl"));
			}else{
				specimenExperimental_DealById(null,null,"confirm",btn_confirm.attr("tourl"));
			}
		});

		/*---------------------------查看送检详细信息表-----------------------------------*/
		btn_viewmore.unbind("click").click(function(){
			var sel_row = $('#specimenExperimentalForm #specimenExperimental_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				specimenExperimental_DealById(sel_row[0].sjid,"viewmore",btn_viewmore.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
		if(initTableField!=null){
			initTableField.unbind("click").click(function(){
				$.showDialog(titleSelectUrl+"?ywid="+$('#specimenExperimentalForm #lx').val()
					,"列表字段设置", $.extend({},setExperimentConfig,{"width":"1020px"}));
			});
		}
//-----------------------模糊查询ok------------------------------------
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchSpecimenExperimentalResult(true);
    		});
		}


//-----------------------查看------------------------------------------
		btn_view.unbind("click").click(function(){
				var sel_row = $('#specimenExperimentalForm #specimenExperimental_list').bootstrapTable('getSelections');// 获取选择行数据
				if(sel_row.length==1){
					specimenExperimental_DealById(sel_row[0].sjid,"view",btn_view.attr("tourl"));
				}else{
					$.error("请选中一行");
				}
			});

		/**显示隐藏**/
		$("#specimenExperimentalForm #sl_searchMore").on("click", function(ev){
			var ev=ev||event;
			if(specimenExperimental_turnOff){
				$("#specimenExperimentalForm #searchMore").slideDown("low");
				specimenExperimental_turnOff=false;
				this.innerHTML="基本筛选";
			}else{
				$("#specimenExperimentalForm #searchMore").slideUp("low");
				specimenExperimental_turnOff=true;
				this.innerHTML="高级筛选";
			}
			ev.cancelBubble=true;

		});
//-----------------------------------------------------------------------
	}
	return oButtonInit;
}

//提供给导出用的回调函数
function BbsySearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="sj.nbbm";
	map["sortLastOrder"]="asc";
	map["sortName"]="sj.sjid";
	map["sortOrder"]="desc";
	return getSpecimenExperimentalSearchData(map);
}

//获取表格显示形式
function getExperimentDataColumn(zdList,waitList){
	var flelds = [];
	var map = {};
	var item = null;
	map = {};
	map["checkbox"] = true;
	map["width"] = "2%";
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
							$('#specimenExperimentalForm #specimenExperimental_list').bootstrapTable('destroy');
							t_experimentList = getExperimentDataColumn(responseText.choseList,responseText.waitList);
							var oTable = new specimenExperimental_TableInit(t_experimentList,false);;
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
							$('#specimenExperimentalForm #specimenExperimental_list').bootstrapTable('destroy');
							t_experimentList = getExperimentDataColumn(responseText.choseList,responseText.waitList);

							var oTable = new specimenExperimental_TableInit(t_experimentList,false);
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
	var oTable = new specimenExperimental_TableInit(specimenExperimental,true);
	    oTable.Init();
	var oButton = new specimenExperimental_oButton();
	    oButton.Init();
	$("#specimenExperimentalForm [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
    jQuery('#specimenExperimentalForm .chosen-select').chosen({width: '100%'});
})
