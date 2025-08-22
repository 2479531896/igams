var hzxx_turnOff=true;
var hzxx_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
     console.log('标记22');
        $('#hzxxformSearch #hzxx_list').bootstrapTable({
            url: '/casereport/hzxx/listHzxx',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#hzxxformSearch #toolbar', //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName:"hz.rysj",					//排序字段
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
            uniqueId: "hzid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            }, {
                field: 'hzid',
                title: '患者ID',
                width: '1%',
                align: 'left',
                visible: false
            }, {
                field: 'hzxm',
                title: '患者姓名',
                width: '7%',
                align: 'left',
                visible: true
            }, {
                field: 'zyh',
                title: '住院号',
                width: '7%',
                align: 'left',
                visible: true
            }, {
                field: 'nryjbh',
                title: '纳入研究编号',
                align: 'left',
                 width: '7%',
                sortable: true,
                visible: true
            }, {
                field: 'nrsj',
                title: '纳入时间',
                align: 'center',
                 width: '5%',
                sortable: true,
                visible: true
            }, {
                field: 'rysj',
                title: '入院时间',
                width: '5%',
                align: 'center',
                sortable: true,
                visible: true
            }, {
				field: 'bgsj',
				title: '报告时间',
				width: '5%',
				align: 'center',
				sortable: true,
				visible: true
			},{
                field: 'yz',
                title: '亚组',
                width: '6%',
                align: 'left',
                visible: true,
                sortable: true
            }, {
                field: 'nl',
                title: '年龄',
                width: '4%',
                align: 'left',
                visible: true
            },{
                field: 'xb',
                title: '性别',
                 width: '4%',
                align: 'left',
                visible: true
            },
            {
                field: 'ssyy',
                title: '所属医院',
                width: '6%',
                align: 'left',
                visible: true,
                sortable: true
            },{
                field: 'jzks',
                title: '就诊科室',
                align: 'left',
                 width: '7%',
                sortable: true,
                visible: true
            },{
                field: 'brlb',
                title: '病人类别',
                width: '7%',
                align: 'left',
                visible: true,
                sortable: true
            },{
                field: 'kjywbls',
                title: '抗菌药物史',
                width: '7%',
                align: 'left',
                visible: true,
                sortable: true
            },{
                field: 'kjzlc',
                title: '抗菌药物天数',
                width: '5%',
                align: 'left',
                visible: true,
                sortable: true
            },{
                field: 'cysj',
                title: '出院时间',
                width: '9%',
                align: 'left',
                visible: true,
                sortable: true
            },{
                field: 'cyzt',
                title: '出院状态',
                width: '6%',
                align: 'left',
                visible: true,
                sortable: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	hzxxDealById(row.hzid, 'view',$("#hzxxformSearch #btn_view").attr("tourl"));
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
        	pageSize: params.limit,   //页面大小
        	pageNumber: (params.offset / params.limit) + 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名  
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "hz.hzid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };
        return getHzxxSearchData(map);
    };
    return oTableInit;
}

function getHzxxSearchData(map){
	var cxbt = $("#hzxxformSearch #cxbt").val();
	// 查询条件
	var cxnr = $.trim(jQuery('#hzxxformSearch #cxnr').val());
	// '0':'来源编号','1':'外部编号','2':'孕周','3':'体重','4':'备注'
	if (cxbt == "0") {
		map["hzxm"] = cxnr;
	}else if (cxbt == "1") {
		map["zyh"] = cxnr;
	}else if (cxbt == "2") {
		map["nryjbh"] = cxnr;
	}else if (cxbt == "3") {
		map["nl"] = cxnr;
	}else if (cxbt == "4") {
		map["kjzlc"] = cxnr;
	}else if (cxbt == "5") {
		map["yz"] = cxnr;
	}
	// 删除标记
	var rysjstart = jQuery('#hzxxformSearch #rysjstart').val();
	map["rysjstart"] = rysjstart;
	var rysjend = jQuery('#hzxxformSearch #rysjend').val();
	map["rysjend"] = rysjend;
	var kjywbls = jQuery('#hzxxformSearch #kjywbls_id_tj').val();
	map["kjywblss"] = kjywbls.replace(/'/g, "");
	var cyzt = jQuery('#hzxxformSearch #cyzt_id_tj').val();
	map["cyzts"] = cyzt.replace(/'/g, "");
	var brlb = jQuery('#hzxxformSearch #brlb_id_tj').val();
	map["brlbs"] = brlb.replace(/'/g, "");
	var xb = jQuery('#hzxxformSearch #xb_id_tj').val();
	map["xbs"] = xb.replace(/'/g, "");
	var jzks = jQuery('#hzxxformSearch #jzks_id_tj').val();
	map["jzkss"] = jzks.replace(/'/g, "");
	var cysjstart = jQuery('#hzxxformSearch #cysjstart').val();
	map["cysjstart"] = cysjstart;
	var cysjend = jQuery('#hzxxformSearch #cysjend').val();
	map["cysjend"] = cysjend;

	return map;
}
laydate.render({
	   elem: '#rysjstart'
	  ,theme: '#2381E9'
	});
	laydate.render({
	   elem: '#rysjend'
	  ,theme: '#2381E9'
	});
	laydate.render({
	   elem: '#cysjstart'
	  ,theme: '#2381E9'
	});
	laydate.render({
	   elem: '#cysjend'
	  ,theme: '#2381E9'
	});
//锁定
function sdFormatter(value, row, index) {
    if (row.sfsd == '1') {
        return '已锁定';
    }
    else{
    	return "正常";
    }
}
//签名
function signFormatter(value, row, index) {
    if (row.signflg == '1') {
        return '已上传';
    }
    else{
    	return "未上传";
    }
}
var addHzxxConfig = {
	width		: "1200px",
	modalName	: "addHzxxModal",
	formName	: "hzxx_ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		/*success1 : {
			label : "保存患者与转归信息",
			className : "btn-primary",
			callback : function() {
				if(!$("#hzxx_ajaxForm").valid()){
					$.alert("请填写完整信息！");
					return false;
				}
				
				var $this = this;
				var opts = $this["options"]||{};
				$("#djrbj").val("2");//第几日标记,
				$("#hzxx_ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchHzxxResult();
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
		success2 : {
			label : "患者,转归,第一日保存",
			className : "btn-primary",
			callback : function() {
				if(!$("#hzxx_ajaxForm").valid()){
					$.alert("请填写完整信息！");
					return false;
				}
				
				var $this = this;
				var opts = $this["options"]||{};
				$("#djrbj").val("1");//第几日标记,1
				$("#hzxx_ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchHzxxResult();
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
		success4 : {
			label : "患者,转归,第三日保存",
			className : "btn-primary",
			callback : function() {
				if(!$("#hzxx_ajaxForm").valid()){
					$.alert("请填写完整信息！");
					return false;
				}
				
				var $this = this;
				var opts = $this["options"]||{};
				$("#djrbj").val("3");//第几日标记,7
				$("#hzxx_ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchHzxxResult();
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
		success5 : {
			label : "患者,转归,第五日保存",
			className : "btn-primary",
			callback : function() {
				if(!$("#hzxx_ajaxForm").valid()){
					$.alert("请填写完整信息！");
					return false;
				}
				
				var $this = this;
				var opts = $this["options"]||{};
				$("#djrbj").val("5");//第几日标记,7
				$("#hzxx_ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchHzxxResult();
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
		success6 : {
			label : "患者,转归,第七日保存",
			className : "btn-primary",
			callback : function() {
				if(!$("#hzxx_ajaxForm").valid()){
					$.alert("请填写完整信息！");
					return false;
				}
				
				var $this = this;
				var opts = $this["options"]||{};
				$("#djrbj").val("7");//第几日标记,7
				$("#hzxx_ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchHzxxResult();
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
		},*/
		success7 : {
			label : "保存",
			className : "btn-primary",
			callback : function() {
				if(!$("#hzxx_ajaxForm").valid()){
					$.alert("请填写完整信息！");
					return false;
				}
				
				var $this = this;
				var opts = $this["options"]||{};
				$("#djrbj").val("0");//第几日标记,0全部
				$("#hzxx_ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchHzxxResult();
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


var addNvHzxxConfig = {
	width		: "1200px",
	modalName	: "addNvHzxxModal",
	formName	: "hzxxnv_ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success7 : {
			label : "保存",
			className : "btn-primary",
			callback : function() {
				if(!$("#hzxxnv_ajaxForm").valid()){
					$.alert("请填写完整信息！");
					return false;
				}
				
				var $this = this;
				var opts = $this["options"]||{};
				$("#djrbj").val("0");//第几日标记,0全部
				$("#hzxxnv_ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchHzxxResult();
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

var editHzxxConfig = {
	width		: "1200px",
	modalName	: "addHzxxModal",
	formName	: "hzxx_ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		/*success1 : {
			label : "保存患者与转归信息",
			className : "btn-primary",
			callback : function() {
				if(!$("#hzxx_ajaxForm").valid()){
					$.alert("请填写完整信息！");
					return false;
				}
				
				var $this = this;
				var opts = $this["options"]||{};
				$("#djrbj").val("2");//第几日标记,7
				$("#hzxx_ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchHzxxResult();
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
		success2 : {
			label : "保存第一日",
			className : "btn-primary",
			callback : function() {
				var $this = this;
				var opts = $this["options"]||{};
				$("#djrbj").val("1");//第几日标记,7
				$("#hzxx_ajaxForm input[name='access_token']").val($("#ac_tk").val());
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchHzxxResult();
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
		success4 : {
			label : "保存第三日",
			className : "btn-primary",
			callback : function() {
				
				var $this = this;
				var opts = $this["options"]||{};
				$("#djrbj").val("3");//第几日标记,7
				$("#hzxx_ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchHzxxResult();
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
		success5 : {
			label : "保存第五日",
			className : "btn-primary",
			callback : function() {
				
				var $this = this;
				var opts = $this["options"]||{};
				$("#djrbj").val("5");//第几日标记,7
				$("#hzxx_ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchHzxxResult();
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
		success6 : {
			label : "保存第七日",
			className : "btn-primary",
			callback : function() {
				var $this = this;
				var opts = $this["options"]||{};
				$("#djrbj").val("7");//第几日标记,7
				$("#hzxx_ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchHzxxResult();
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
		},*/
		success7 : {
			label : "保存",
			className : "btn-primary",
			callback : function() {
				if(!$("#hzxx_ajaxForm").valid()){
					$.alert("请填写完整信息！");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				$("#djrbj").val("0");//第几日标记,0全部
				$("#hzxx_ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchHzxxResult();
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
var viewHzxxIndexConfig = {
	width		: "1200px",
	modalName	: "viewHzxxModal",
	formName	: "viewHzxx_ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};
var reportHzxxConfig = {
	width		: "1200px",
	modalName	: "reportHzxxModal",
	formName	: "hzxx_ajaxForm",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		success7 : {
			label : "保存",
			className : "btn-primary",
			callback : function() {
				if(!$("#hzxx_ajaxForm").valid()){
					$.alert("请填写完整信息！");
					return false;
				}
				var $this = this;
				var opts = $this["options"]||{};
				$("#djrbj").val("0");//第几日标记,0全部
				$("#hzxx_ajaxForm input[name='access_token']").val($("#ac_tk").val());
				
				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchHzxxResult();
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
var generateHzxxConfig = {
		width		: "800px",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		} 
	};

//按钮动作函数
function hzxxDealById(id,action,tourl){
	if(!tourl){
		return;
	}
	if(action =='view'){
		var url= tourl + "?hzid=" +id;
		$.showDialog(url,'查看',viewHzxxIndexConfig);
	}else if(action =='add'){
		var url=tourl;
		$.showDialog(url,'新增',addHzxxConfig);
	}else if(action =='addnv'){
		var url=tourl;
		$.showDialog(url+"?isVal='1'",'简易新增',addNvHzxxConfig);
	}else if(action =='mod'){
		var url= tourl + "?hzid=" +id;
		$.showDialog(url,'编辑',editHzxxConfig);
	}else if(action =='report'){
		var url= tourl+"?hzid=" +id;
		$.showDialog(url,'编辑病例报告',reportHzxxConfig);
	}else if(action =='wordexport'){
		var url= tourl + "?hzid=" +id;
		$.showDialog(url,'病例报告导出',generateHzxxConfig);
	}else if(action =='upload'){
		var url=tourl+ "?hzid="+id;
		$.showDialog(url,'上传',uploadHzxxConfig);
	}
}


var hzxx_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};
    oInit.Init = function () {
     console.log('标记221');
        //初始化页面上面的按钮事件
    	var btn_add = $("#hzxxformSearch #btn_add");
    	var btn_mod = $("#hzxxformSearch #btn_mod");
    	var btn_del = $("#hzxxformSearch #btn_del");
    	var btn_view = $("#hzxxformSearch #btn_view");
    	var btn_initpass = $("#hzxxformSearch #btn_initpass");
    	var bth_relwechat=$("#hzxxformSearch #btn_relwechat");
    	var btn_initdd = $("#hzxxformSearch #btn_initdd");
    	var btn_checkinrecord = $("#hzxxformSearch #btn_checkinrecord");
    	var btn_report = $("#hzxxformSearch #btn_report")
    	var btn_hzxx_query = $("#btn_hzxx_query");
    	var btn_uploadsign=$("#hzxxformSearch #btn_uploadsign");
    	var btn_generateHzxx=$("#hzxxformSearch #btn_wordexport");
		var btn_selectexport = $("#hzxxformSearch #btn_selectexport");//选中导出
		var btn_searchexport = $("#hzxxformSearch #btn_searchexport");//搜索导出
		var btn_upload =$("#hzxxformSearch #btn_upload");//上传
		var btn_addnv =$("#hzxxformSearch #btn_addnv");//上传

    	//绑定搜索发送功能
    	if(btn_hzxx_query != null){
    		console.log('标记11');
    	
    		btn_hzxx_query.unbind("click").click(function(){
    			searchHzxxResult(true);
    		});
    	}
    	btn_add.unbind("click").click(function(){
    		hzxxDealById(null,"add",btn_add.attr("tourl"));
    	});
    	btn_addnv.unbind("click").click(function(){
    		hzxxDealById(null,"addnv",btn_addnv.attr("tourl"));
    	});
    	btn_checkinrecord.unbind("click").click(function(){
    		hzxxDealById(null,"checkinrecord",btn_checkinrecord.attr("tourl"));
        });
		//---------------------------------选中导出---------------------------------------
		btn_selectexport.unbind("click").click(function(){
			var sel_row = $('#hzxxformSearch #hzxx_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length>=1){
				var ids="";
				for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
					ids = ids + ","+ sel_row[i].hzid;
				}
				ids = ids.substr(1);
				$.showDialog(exportPrepareUrl+"?ywid=NDZ_SELECT&expType=select&ids="+ids
					,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
			}else{
				$.error("请选择数据");
			}
		});
		//搜索导出
		btn_searchexport.unbind("click").click(function(){
			$.showDialog(exportPrepareUrl+"?ywid=NDZ_SEARCH&expType=search&callbackJs=ndzSearchData"
				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
		});
		/* ------------------------------上传-----------------------------*/
		btn_upload.unbind("click").click(function(){
			var sel_row = $('#hzxxformSearch #hzxx_list').bootstrapTable('getSelections');//获取选择行数据
			if(sel_row.length==1){
				hzxxDealById(sel_row[0].hzid,"upload",btn_upload.attr("tourl"));
			}else{
				$.error("请选中一行");
			}
		});
    	btn_mod.unbind("click").click(function(){
    		var sel_row = $('#hzxxformSearch #hzxx_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			hzxxDealById(sel_row[0].hzid,"mod",btn_mod.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_report.unbind("click").click(function(){
    		var sel_row = $('#hzxxformSearch #hzxx_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			hzxxDealById(sel_row[0].hzid,"report",btn_report.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_generateHzxx.unbind().click(function(){
    		var sel_row = $('#hzxxformSearch #hzxx_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length == 1){
    			hzxxDealById(sel_row[0].hzid, "wordexport", btn_generateHzxx.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	btn_view.unbind("click").click(function(){
    		var sel_row = $('#hzxxformSearch #hzxx_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length==1){
    			hzxxDealById(sel_row[0].hzid,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});

    	/* ------------------------------关联微信用户列表-----------------------------*/
    	bth_relwechat.unbind("click").click(function(){
    		var sel_row = $('#hzxxformSearch #hzxx_list').bootstrapTable('getSelections');
    		if(sel_row.length==1){
    			hzxxDealById(sel_row[0].hzid,"relwechat",bth_relwechat.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	
        btn_initpass.unbind("click").click(function(){
            var sel_row = $('#hzxxformSearch #hzxx_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length ==0){
                $.error("请至少选中一行");
                return;
            }
            var ids="";
            for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                ids = ids + ","+ sel_row[i].hzid;
                ids = ids.substr(1);
                $.confirm('您确定要初始化所选用户的密码吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= btn_initpass.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchHzxxResult();
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
            
        btn_initdd.unbind("click").click(function(){
            $.confirm('您确定要更新钉钉ID信息吗？',function(result){
                if(result){
                    jQuery.ajaxSetup({async:false});
                    var url= btn_initdd.attr("tourl");
                    jQuery.post(url,{"access_token":$("#ac_tk").val()},function(responseText){
                        setTimeout(function(){
                            if(responseText["status"] == 'success'){
                                $.success(responseText["message"],function() {
                                    searchHzxxResult();
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
	
    	btn_del.unbind("click").click(function(){
    		var sel_row = $('#hzxxformSearch #hzxx_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length ==0){
    			$.error("请至少选中一行");
    			return;
    		}
    		var ids="";
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			ids = ids + ","+ sel_row[i].hzid;
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
    								searchHzxxResult();
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
    	/* ------------------------------上传签名-----------------------------*/
    	btn_uploadsign.unbind("click").click(function(){
    		hzxxDealById(null,"uploadsign",btn_uploadsign.attr("tourl"));
    	});
    	
    	/**显示隐藏**/      
    	$("#hzxxformSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(hzxx_turnOff){
    			$("#hzxxformSearch #searchMore").slideDown("low");
    			hzxx_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#hzxxformSearch #searchMore").slideUp("low");
    			hzxx_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    	});
    };

    return oInit;
};

var uploadHzxxConfig = {
	width		: "900px",
	modalName	: "uploadSignModal",
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
				var $this = this;
				var opts = $this["options"]||{};

				$("#ajaxForm input[name='access_token']").val($("#ac_tk").val());

				submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
					if(responseText["status"] == 'success'){
						$.success(responseText["message"],function() {
							if(opts.offAtOnce){
								$.closeModal(opts.modalName);
								searchHzxxResult();
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

function searchHzxxResult(isTurnBack){
console.log('标记');
	if(isTurnBack){
		$('#hzxxformSearch #hzxx_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#hzxxformSearch #hzxx_list').bootstrapTable('refresh');
		}
}

function ndzSearchData(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="ndz.hzid asc, ndz.jldjt";
	map["sortLastOrder"]="asc";
	map["sortName"]="ndz.rysj";
	map["sortOrder"]="asc";
	return getHzxxSearchData(map);
}
var hzxx_PageInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
	    var sfsd = $("#hzxxformSearch a[id^='sfsd_id_']");
		$.each(sfsd, function(i, n){
			var id = $(this).attr("id");
			var code = id.substring(id.lastIndexOf('_')+1,id.length);
			if(code == '0'){
				addTj('sfsd',code,'hzxxformSearch');
			}
		});
    }
    return oInit;
}

$(function(){
	//0.界面初始化
    var oInit = new hzxx_PageInit();
    oInit.Init();
    //1.初始化Table
    var oTable = new hzxx_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new hzxx_ButtonInit();
    oButtonInit.Init();
    
	//所有下拉框添加choose样式
	jQuery('#hzxxformSearch .chosen-select').chosen({width: '100%'});
});
