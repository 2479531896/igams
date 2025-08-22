var Potency_turnOff=true;

var Potency_TableInit = function () {
	var oTableInit = new Object();
	// 初始化Table
	oTableInit.Init = function () {
		$('#potency_formSearch #tb_list').bootstrapTable({
			url: '/experiment/potency/pageGetListPotency',         // 请求后台的URL（*）
			method: 'get',                      // 请求方式（*）
			toolbar: '#potency_formSearch #toolbar', // 工具按钮用哪个容器
			striped: true,                      // 是否显示行间隔色
			cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination: true,                   // 是否显示分页（*）
			paginationShowPageGo: true,         //增加跳转页码的显示
			sortable: true,                     // 是否启用排序
			sortName:"lrsj",					// 排序字段
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
			uniqueId: "tqid",                     // 每一行的唯一标识，一般为主键列
			showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
			cardView: false,                    // 是否显示详细视图
			detailView: false,                   // 是否显示父子表
			columns: [{
				checkbox: true
			}, {
				'field': '',
			    'title': '序号',
			    'align': 'center',
			    'width': '20%',
			    'formatter': function (value, row, index) {
			    　　var options = $('#potency_formSearch #tb_list').bootstrapTable('getOptions'); 
			    　　return options.pageSize * (options.pageNumber - 1) + index + 1; 
			    }
			}, {
				field: 'mc',
				title: '名称',
				width: '40%',
				align: 'left',
				visible: true
			}, {
				field: 'sjph',
				title: '核酸提取试剂盒',
				width: '40%',
				align: 'left',
				visible: true
			}, {
				field: 'jcdwmc',
				title: '检测单位',
				width: '20%',
				align: 'left',
				visible: true
			},{
				field: 'lrsj',
				title: '创建日期',
				width: '30%',
				align: 'left',
				visible: true
			}, {
				field: 'lrrymc',
				title: '创建人员',
				width: '30%',
				align: 'left',
				visible: true
			}, {
				field: 'xgsj',
				title: '修改日期',
				width: '30%',
				align: 'left',
				visible: true
			}, {
				field: 'xgrymc',
				title: '修改人员',
				width: '30%',
				align: 'left',
				visible: true
			}, {
                field: 'czrmc',
                title: '操作人',
                width: '25%',
                align: 'left',
                visible: true
            }, {
                field: 'hdrmc',
                title: '核对人',
                width: '25%',
                align: 'left',
                visible: true
            }, {
				field: 'zt',
				title: '状态',
				width: '20%',
				align: 'left',
				formatter:ztformat,
				visible: true
			}, {
                field: 'tqsj',
                title: '提取试剂',
                width: '40%',
                align: 'left',
                visible: false
            }],
			onLoadSuccess: function () {
			},
			onLoadError: function () {
				alert("数据加载失败！");
			},
			onDblClickRow: function (row, $element) {
				PotencyById(row.tqid,'view',$("#potency_formSearch #btn_view").attr("tourl"));
			},
		});
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
        sortLastName: "lrsj", // 防止同名排位用
        sortLastOrder: "desc" // 防止同名排位用
        // 搜索框使用
        // search:params.search
    };
    return getPotencySearchData(map);
	};
	return oTableInit;
};

function getPotencySearchData(map){
	var cxtj=$("#potency_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#potency_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["mc"]=cxnr
	}else if(cxtj=="1"){
		map["nbbh"]=cxnr
	}else if(cxtj=="2"){
        map["czrmc"]=cxnr
    }else if(cxtj=="3"){
        map["hdrmc"]=cxnr
    }else if(cxtj=="4"){
        map["lrrymc"]=cxnr
    }else if(cxtj=="5"){
        map["xgrymc"]=cxnr
    }
	 return map;
}

//状态格式化
function ztformat(value,row,index){
	if(row.zt=="未通过"){
		var zt="<span style='color:red;'>"+row.zt+"</span>";
	}else{
		var zt=row.zt;
	}
	return zt;
}

function PotencyResult(isTurnBack){
	if(isTurnBack){
		$('#potency_formSearch #tb_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#potency_formSearch #tb_list').bootstrapTable('refresh');
	}
}

function PotencyById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action =='view'){
		var url= tourl + "?tqid=" +id;
		$.showDialog(url,'提取详细信息',viewPotencyConfig);
	}else if(action =='add'){
		var url= tourl;
		$.showDialog(url,'提取浓度信息',addPotencyConfig);
	}else if(action =='mod'){
		var url=tourl + "?tqid=" +id;
		$.showDialog(url,'编辑提取明细',modPotencyConfig);
	}else if(action=='examine'){
		$.showDialog(tourl,'检查编号',examinePotencyConfig);
	}else if(action=='print'){
		$.showDialog(tourl,'打印条码',printPotencyConfig);
	}
}


var Potency_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
		var btn_view = $("#potency_formSearch #btn_view");
		var btn_add = $("#potency_formSearch #btn_add");
		var btn_mod = $("#potency_formSearch #btn_mod");
		var btn_del = $("#potency_formSearch #btn_del");
		var btn_query = $("#potency_formSearch #btn_query");
		var btn_examine = $("#potency_formSearch #btn_examine");
		var btn_print = $("#potency_formSearch #btn_print");
		var btn_export = $("#potency_formSearch #btn_export");
		if(btn_query != null){
    		btn_query.unbind("click").click(function(){
    			PotencyResult(true);
    		});
    	}
        /*---------------------------查看文库列表-----------------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#potency_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			PotencyById(sel_row[0].tqid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/*---------------------------新增文库信息-----------------------------------*/
    	btn_add.unbind("click").click(function(){
    		PotencyById(null,"add",btn_add.attr("tourl"));
    	});
    	/*---------------------------编辑文库信息-----------------------------------*/
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#potency_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			PotencyById(sel_row[0].tqid,"mod",btn_mod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	/*---------------------------删除文库信息-----------------------------------*/
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#potency_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].tqid;
    		}
    		ids = ids.substr(1);
    		$.confirm('您确定要删除所选择的记录吗？',function(result){
    			if(result){
    				jQuery.ajaxSetup({async:false});
    				var url= btn_del.attr("tourl");
    				jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
    					setTimeout(function(){
    						if(responseText["status"] == 'success'){
    							$.success(responseText["message"],function() {
    								PotencyResult();
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
    	});
    	/*----------------------------------------检查编号---------------------------------------*/
    	btn_examine.unbind("click").click(function(){
    		PotencyById(null,"examine",btn_examine.attr("tourl"));
    	})
    	/*----------------------------------------打印条码---------------------------------------*/
    	btn_print.unbind("click").click(function(){
    		PotencyById(null,"print",btn_print.attr("tourl"));
    	})
		/* --------------------------- 导出 -----------------------------------*/
		btn_export.unbind("click").click(function(){
			var sel_row = $('#potency_formSearch #tb_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length ==0){
				$.error("请选中一行");
				return;
			}
			var url=btn_export.attr("tourl")+"?tqid="+sel_row[0].tqid+"&access_token="+$("#ac_tk").val();
			window.open(url);
		});
    	
    };
    return oInit;
};

copyContentH5: function copyText(content) {
	var copyDom = document.createElement('div');
	copyDom.innerText=content;
	copyDom.style.position='absolute';
	copyDom.style.top='0px';
	copyDom.style.right='-9999px';
	document.body.appendChild(copyDom);
	//创建选中范围
	var range = document.createRange();
	range.selectNode(copyDom);
	//移除剪切板中内容
	window.getSelection().removeAllRanges();
	//添加新的内容到剪切板
	window.getSelection().addRange(range);
	//复制
	var successful = document.execCommand('copy');
	copyDom.parentNode.removeChild(copyDom);
	try{
		var msg = successful ? "successful" : "failed";
		console.log('Copy command was : ' + msg);
	} catch(err){
		console.log('Oops , unable to copy!');
	}
	return successful;
}

var sfbc=0;//是否继续保存 

var viewPotencyConfig = {
		width		: "1500px",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};
var addPotencyConfig = {
		width		: "2000px",
		modalName	: "addLibraryModal",
		formName	: "ajaxForm",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success2 : {
				label : "复 制",
				className : "btn-danger",
				callback : function() {
					var str="";
					for(var i=1;i<97;i++){
						if($("#ajaxFormEdit #nbbh-"+i).val()){
							str=str+$("#ajaxFormEdit #nbbh-"+i).val()+",";
						}else{
							str=str+",";
						}
						if($("#ajaxFormEdit #hsnd-"+i).val()){
							str=str+$("#ajaxFormEdit #hsnd-"+i).val()+",";
						}else{
							str=str+",";
						}
						if($("#ajaxFormEdit #cdna-"+i).val()){
							str=str+$("#ajaxFormEdit #cdna-"+i).val()+",";
						}else{
							str=str+",";
						}
						if($("#ajaxFormEdit #nbzbm-"+i).val()){
							str=str+$("#ajaxFormEdit #nbzbm-"+i).val()+",";
						}else{
							str=str+",";
						}
						if($("#ajaxFormEdit #syglid-"+i).val()){
							str=str+$("#ajaxFormEdit #syglid-"+i).val()+",";
						}else{
							str=str+",";
						}
						if($("#ajaxFormEdit #tqykw-"+i).val()){
							str=str+$("#ajaxFormEdit #tqykw-"+i).val()+",";
						}else{
							str=str+",";
						}
						if($("#ajaxFormEdit #spike-"+i).val()){
							str=str+$("#ajaxFormEdit #spike-"+i).val()+";";
						}else{
							str=str+";";
						}
						if($("#ajaxFormEdit #xsbs-"+i).val()){
							str=str+$("#ajaxFormEdit #xsbs-"+i).val()+";";
						}else{
							str=str+";";
						}
					}
					copyText(str);
					$.confirm("复制成功!");
					return false;
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
                        var kwdata = $("#ajaxForm #kwdata").val().split(",");
                        var spikedata = $("#ajaxForm #spikedata").val().split(",");
                        var kw="";
                        var spike="";
                        for(var i=1;i<97;i++){
                            if($("#ajaxFormEdit #tqykw-"+i).val()){
                                var isFind=false;
                                for(var k=0;k<kwdata.length;k++){
                                    if($("#ajaxFormEdit #tqykw-"+i).val()==kwdata[k]){
                                        isFind=true;
                                    }
                                }
                                if(!isFind){
                                    kw+=","+$("#ajaxFormEdit #tqykw-"+i).val();
                                }
                            }
                            if($("#ajaxFormEdit #spike-"+i).val()){
                                var isFind=false;
                                for(var k=0;k<spikedata.length;k++){
                                    if($("#ajaxFormEdit #spike-"+i).val()==spikedata[k]){
                                        isFind=true;
                                    }
                                }
                                if(!isFind){
                                    spike+=","+$("#ajaxFormEdit #spike-"+i).val();
                                }
                            }
                            for(var k=1;k<97;k++){
                                if($("#ajaxFormEdit #nbbh-"+i).val() && i!=k && $("#ajaxFormEdit #nbbh-"+i).val() == $("#ajaxFormEdit #nbbh-"+k).val()){
                                    str += ","+$("#ajaxFormEdit #nbbh-"+i).val();
                                }
                            }
                        }

                        var tip="";
                        if (kw){
                            tip+="且孔位为 "+ kw.substring(1) + " 不符合规则";
                        }
                        if (spike){
                            tip+="且spike为 "+ spike.substring(1) + " 不符合规则";
                        }
                        if (str){
                            tip+="且存在提取相同的标本 "+ str.substring(1);
                        }
                        if (tip){
                            $.confirm(tip.substring(1) + " 是否继续保存",function(result) {
                                if(result){
                                    checkAndSubmit("btn_success",false,true,opts);
                                }
                            });
                            return false;
                        }
                        return checkAndSubmit("btn_success",false,true,opts);
                    }
                },
			successOne : {
				label : "保 存",
				className : "btn-success",
				callback : function() {
					var $this = this;
					var opts = $this["options"]||{};
					var str = "";
					var kwdata = $("#ajaxForm #kwdata").val().split(",");
					var spikedata = $("#ajaxForm #spikedata").val().split(",");
					var kw="";
					var spike="";
					for(var i=1;i<97;i++){
						if($("#ajaxFormEdit #tqykw-"+i).val()){
							var isFind=false;
							for(var k=0;k<kwdata.length;k++){
                                    if($("#ajaxFormEdit #tqykw-"+i).val()==kwdata[k]){
									isFind=true;
								}
							}
							if(!isFind){
								kw+=","+$("#ajaxFormEdit #tqykw-"+i).val();
							}
						}
						if($("#ajaxFormEdit #spike-"+i).val()){
							var isFind=false;
							for(var k=0;k<spikedata.length;k++){
								if($("#ajaxFormEdit #spike-"+i).val()==spikedata[k]){
									isFind=true;
								}
							}
							if(!isFind){
								spike+=","+$("#ajaxFormEdit #spike-"+i).val();
							}
						}
						for(var k=1;k<97;k++){
							if($("#ajaxFormEdit #nbbh-"+i).val() && i!=k && $("#ajaxFormEdit #nbbh-"+i).val() == $("#ajaxFormEdit #nbbh-"+k).val()){
								str += ","+$("#ajaxFormEdit #nbbh-"+i).val();
							}
						}
					}

					var tip="";
					if (kw){
						tip+="且孔位为 "+ kw.substring(1) + " 不符合规则";
					}
					if (spike){
						tip+="且spike为 "+ spike.substring(1) + " 不符合规则";
					}
					if (str){
						tip+="且存在提取相同的标本 "+ str.substring(1);
					}
					if (tip){
						$.confirm(tip.substring(1) + " 是否继续保存",function(result) {
							if(result){
								checkAndSubmit("btn_successOne",true,false,opts)
							}
						});
						return false;
					}
					return checkAndSubmit("btn_successOne",true,false,opts);
				}
			},
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

var modPotencyConfig = {
	width		: "2000px",
	modalName	: "modLibraryModal",
	formName	: "ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success : {
			label : "确 定",
			className : "btn-primary",
			callback : function() {
				var $this = this;
				var opts = $this["options"]||{};
				$("#ajaxForm #sfgx").val("1");
				var str = "";
				var kwdata = $("#ajaxForm #kwdata").val().split(",");
				var spikedata = $("#ajaxForm #spikedata").val().split(",");
				var kw="";
				var spike="";
				for(var i=1;i<97;i++){
					if($("#ajaxFormEdit #tqykw-"+i).val()){
						var isFind=false;
						for(var k=0;k<kwdata.length;k++){
							if($("#ajaxFormEdit #tqykw-"+i).val()==kwdata[k]){
								isFind=true;
							}
						}
						if(!isFind){
							kw+=","+$("#ajaxFormEdit #tqykw-"+i).val();
						}
					}
					if($("#ajaxFormEdit #spike-"+i).val()){
						var isFind=false;
						for(var k=0;k<spikedata.length;k++){
							if($("#ajaxFormEdit #spike-"+i).val()==spikedata[k]){
								isFind=true;
							}
						}
						if(!isFind){
							spike+=","+$("#ajaxFormEdit #spike-"+i).val();
						}
					}
					for(var k=1;k<97;k++){
						if($("#ajaxFormEdit #nbbh-"+i).val() && i!=k && $("#ajaxFormEdit #nbbh-"+i).val() == $("#ajaxFormEdit #nbbh-"+k).val()){
							str += ","+$("#ajaxFormEdit #nbbh-"+i).val();
						}
					}
				}

				var tip="";
				if (kw){
					tip+="且孔位为 "+ kw.substring(1) + " 不符合规则";
				}
				if (spike){
					tip+="且spike为 "+ spike.substring(1) + " 不符合规则";
				}
				if (str){
					tip+="且存在提取相同的标本 "+ str.substring(1);
				}
				if (tip){
					$.confirm(tip.substring(1) + " 是否继续保存",function(result) {
						if(result){
							checkAndSubmit("btn_success",false,true,opts);
						}
					});
					return false;
				}
				return checkAndSubmit("btn_success",false,true,opts);
			}
		},
		successtwo : {
			label : "保 存",
			className : "btn-success",
			callback : function() {
				var $this = this;
				var opts = $this["options"]||{};
				var str = "";
				var kwdata = $("#ajaxForm #kwdata").val().split(",");
				var spikedata = $("#ajaxForm #spikedata").val().split(",");
				var kw="";
				var spike="";
				for(var i=1;i<97;i++){
					if($("#ajaxFormEdit #tqykw-"+i).val()){
						var isFind=false;
						for(var k=0;k<kwdata.length;k++){
							if($("#ajaxFormEdit #tqykw-"+i).val()==kwdata[k]){
								isFind=true;
							}
						}
						if(!isFind){
							kw+=","+$("#ajaxFormEdit #tqykw-"+i).val();
						}
					}
					if($("#ajaxFormEdit #spike-"+i).val()){
						var isFind=false;
						for(var k=0;k<spikedata.length;k++){
							if($("#ajaxFormEdit #spike-"+i).val()==spikedata[k]){
								isFind=true;
							}
						}
						if(!isFind){
							spike+=","+$("#ajaxFormEdit #spike-"+i).val();
						}
					}
					for(var k=1;k<97;k++){
						if($("#ajaxFormEdit #nbbh-"+i).val() && i!=k && $("#ajaxFormEdit #nbbh-"+i).val() == $("#ajaxFormEdit #nbbh-"+k).val()){
							str += ","+$("#ajaxFormEdit #nbbh-"+i).val();
						}
					}
				}

				var tip="";
				if (kw){
					tip+="且孔位为 "+ kw.substring(1) + " 不符合规则";
				}
				if (spike){
					tip+="且spike为 "+ spike.substring(1) + " 不符合规则";
				}
				if (str){
					tip+="且存在提取相同的标本 "+ str.substring(1);
				}
				if (tip){
					$.confirm(tip.substring(1) + " 是否继续保存",function(result) {
						if(result){
							checkAndSubmit("btn_successtwo",true,false,opts)
						}
					});
					return false;
				}
				return checkAndSubmit("btn_successtwo",false,false,opts);
			}
		},
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};

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
	var syglids=[];
	var tqmxids=[];
	var tqykws=[];
	var tqybms=[];
	var spikes=[];
	var xsbss=[];
	var nbbh;
	var hsnd;
	var cdna;
	var nbzbm;
	var syglid;
	var tqmxid;
	var xh;
	var tqykw;
	var tqybm;
	var spike;
	var xsbs;
	for(var i=1;i<97;i++){
	    now = new Date();
		console.log(now + " " + i);
		nbbh=$("#ajaxFormEdit #nbbh-"+i).val();
		hsnd=$("#ajaxFormEdit #hsnd-"+i).val();
		cdna=$("#ajaxFormEdit #cdna-"+i).val();
		nbzbm=$("#ajaxFormEdit #nbzbm-"+i).val();
		nbzbm=$("#ajaxFormEdit #nbzbm-"+i).val();
		syglid=$("#ajaxFormEdit #syglid-"+i).val();
		tqmxid=$("#ajaxFormEdit #tqmxid-"+i).val();
		tqykw=$("#ajaxFormEdit #tqykw-"+i).val();
		spike=$("#ajaxFormEdit #spike-"+i).val();
		xsbs=$("#ajaxFormEdit #xsbs-"+i).val();
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
				tqybm=$("#ajaxFormEdit #tqybm-1").val();
				tqybms.push(tqybm);
			}else if(i>10&&i<=20){
				tqybm=$("#ajaxFormEdit #tqybm-2").val();
				tqybms.push(tqybm);
			}else if(i>20&&i<=30){
				tqybm=$("#ajaxFormEdit #tqybm-3").val();
				tqybms.push(tqybm);
			}else if(i>30&&i<=40){
				tqybm=$("#ajaxFormEdit #tqybm-4").val();
				tqybms.push(tqybm);
			}else if(i>40&&i<=50){
				tqybm=$("#ajaxFormEdit #tqybm-5").val();
				tqybms.push(tqybm);
			}else if(i>50&&i<=60){
				tqybm=$("#ajaxFormEdit #tqybm-6").val();
				tqybms.push(tqybm);
			}else if(i>60&&i<=70){
				tqybm=$("#ajaxFormEdit #tqybm-7").val();
				tqybms.push(tqybm);
			}else if(i>70&&i<=80){
				tqybm=$("#ajaxFormEdit #tqybm-8").val();
				tqybms.push(tqybm);
			}else if(i>80&&i<=90){
				tqybm=$("#ajaxFormEdit #tqybm-9").val();
				tqybms.push(tqybm);
			}else if(i>90&&i<=96){
				tqybm=$("#ajaxFormEdit #tqybm-10").val();
				tqybms.push(tqybm);
			}
			nbbhs.push(nbbh);
			hsnds.push(hsnd);
			cdnas.push(cdna);
			xsnds.push(xsnd);
			nbzbms.push(nbzbm);
			syglids.push(syglid);
			tqmxids.push(tqmxid);
			xhs.push(xh);
			tqykws.push(tqykw);
			spikes.push(spike);
			xsbss.push(xsbs);
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
			$("#ajaxFormEdit #nbbh-"+i).attr("style","border-color: #a94442");
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
	$("#ajaxForm #xsbss").val(xsbss);
	$("#ajaxForm #tqybms").val(tqybms);
	$("#ajaxForm #sfbc").val(sfbc);
	$("#ajaxForm #jkyls").val(jkyls);
	$("#ajaxForm #hcyyls").val(hcyyls);
	$("#ajaxForm #xsnds").val(xsnds);
	$("#ajaxForm #nbzbms").val(nbzbms);
	$("#ajaxForm #syglids").val(syglids);
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
				PotencyResult();
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
		    preventResubmitForm(".modal-footer > button", false);
			$.alert(responseText["message"],function() {
			});
		}
	},".modal-footer > button");
	return false;
}

var examinePotencyConfig = {
		width		: "800px",
		modalName	: "examineLibraryModal",
		formName	: "ajaxForm",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

var printPotencyConfig = {
		width		: "800px",
		modalName	: "printPotencyModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};
$(function(){
	//0.界面初始化
	// 1.初始化Table
	var oTable = new Potency_TableInit();
	oTable.Init();
	
    //2.初始化Button的点击事件
    var oButtonInit = new Potency_ButtonInit();
    oButtonInit.Init();
    
	// 所有下拉框添加choose样式
	jQuery('#potency_formSearch .chosen-select').chosen({width: '100%'});
	
});