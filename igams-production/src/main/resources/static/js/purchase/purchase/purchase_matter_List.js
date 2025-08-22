var purchaseMatter_turnOff=true;

var PurchaseMatter_TableInit=function(){
	var oTableInit=new Object();
	oTableInit.Init=function(){
		$("#purchaseMatter_formSearch #purchaseMatter_list").bootstrapTable({
			url: $("#purchaseMatter_formSearch #urlPrefix").val()+'/purchase/purchase/pageGetListMatterPurchase',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#purchaseMatter_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "qggl.jlbh",				//排序字段
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
            uniqueId: "qgmxid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width: '1.5%'
            },{
            	title: '序',
				formatter: function (value, row, index) {
					return index+1;
				},
				titleTooltip:'序号',
				width: '2%',
                align: 'left',
                visible:true
            },{
                field: 'qgid',
                title: '请购ID',
                width: '8%',
                align: 'left',
                visible:false
            },{
                field: 'fzrmc',
                title: '采购',
                titleTooltip:'采购',
                width: '5%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'qgsqrmc',
                title: '申请人',
                titleTooltip:'申请人',
                width: '5%',
                align: 'left',
                sortable: true,
                visible:true
            },{
                field: 'jlbh',
                title: '记录编号',
                titleTooltip:'记录编号',
                width: '9%',
                align: 'left',
                formatter:purchaseMatter_djhformat,
                sortable: true,
                visible:true
            },{
                field: 'htnbbh',
                title: '合同内部编号',
                titleTooltip:'合同内部编号',
                width: '11%',
                align: 'left',
                formatter:purchaseMatter_htbhformat,
                sortable: true,
                visible: true
            },{
                field: 'gysmc',
                title: '供应商',
                titleTooltip:'供应商',
                width: '8%',
                align: 'left',
                sortable: true,
                formatter:gysformat,
                visible:true
            },{
                field: 'wlbm',
                title: '物料编码',
                titleTooltip:'物料编码',
                width: '8%',
                align: 'left',
                formatter:purchaseMatter_wlbmformat,
                sortable: true,
                visible: true
            },{
                field: 'cpzch',
                title: '产品注册号',
                titleTooltip:'产品注册号',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'wllbmc',
                title: '物料类别',
                titleTooltip:'物料类别',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'wlzlbmc',
                title: '物料子类别',
                titleTooltip:'物料子类别',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'zlbmtc',
                title: '子类别统称',
                titleTooltip:'子类别统称',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'wlmc',
                title: '购买物品/品牌',
                titleTooltip:'购买物品/品牌',
                width: '8%',
                align: 'left',
                formatter:purchaseMatter_wlmcformat,
                sortable: true,   
                visible: true
            },{
                field: 'gg',
                title: '规格',
                titleTooltip:'规格',
                width: '6%',
                align: 'left',
                formatter: ggformat,
                sortable: true,   
                visible: true
            },{
                field: 'ccdgmc',
                title: '初次订购',
                width: '4%',
                align: 'left',
                visible: true,
                sortable: true,
            },{
                field: 'wlflmc',
                title: '物料分类',
                width: '6%',
                align: 'left',
                visible: true,
                sortable: true,
            },{
                field: 'ychh',
                title: '货号',
                titleTooltip:'货号',
                width: '8%',
                align: 'left',
                sortable: true,   
                visible: true
            },{
                field: 'jldw',
                title: '单位',
                titleTooltip:'单位',
                width: '6%',
                align: 'left',
                formatter:jldwformat,
                sortable: true,   
                visible: true
            },{
                field: 'sl',
                title: '请购数量',
                titleTooltip:'请购数量',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'htsl',
                title: '合同数量',
                titleTooltip:'合同数量',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'hsdj',
                title: '含税单价',
                titleTooltip:'含税单价',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'hjje',
                title: '金额',
                titleTooltip:'金额',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'htfkfsmc',
                title: '合同付款方式',
                titleTooltip:'合同付款方式',
                width: '6%',
                align: 'left',
                formatter:htfkfsformat,
                sortable: true,
                visible: true
            }, {
                field: 'jhdhrq',
                title: '预计到货时间',
                width: '8%',
                align: 'left',
                visible: true,
                sortable: true,
            }, {
                field: 'dhrq',
                title: '到货时间',
                width: '7%',
                align: 'left',
                visible: true,
                sortable: true,
            },{
                field: 'dhsl',
                title: '到货数量',
                titleTooltip:'到货数量',
                formatter:purchaseMatter_dhslformat,
                width: '4%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'u8rkdh',
                title: 'U8入库单号',
                width: '12%',
                align: 'left',
                visible: true,
                formatter:u8rkdhformat,
                sortable: true,
            }, {
                field: 'rkzsl',
                title: '入库总数量',
                width: '5%',
                align: 'left',
                visible: false,
                sortable: true,
            }, {
                field: 'bhgsl',
                title: '不合格数量',
                width: '5%',
                align: 'left',
                visible: false,
                sortable: true,
            },{
                field: 'wlid',
                title: '物料ID',
                width: '8%',
                align: 'left',   
                visible: false
            }, {
                field: 'fph',
                title: '发票号',
                width: '10%',
                align: 'left',
                visible: true,
                formatter:fphformat,
                sortable: true,
            }, {
                field: 'fpzje',
                title: '发票总金额',
                width: '6%',
                align: 'left',
                visible: true,
                sortable: true,
            }, {
                field: 'fplrsj',
                title: '交给财务时间',
                width: '8%',
                align: 'left',
                visible: true,
                formatter:fplrsjformat,
                sortable: true,
            },{
                field: 'hwid',
                title: '货物id',
                width: '8%',
                formatter:purchaseMatter_hwidformat,
                align: 'left',
                visible: false
            },{
                field: 'fkrq',
                title: '付款日期',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'fkfsmc',
                title: '付款方式',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'fkje',
                title: '付款金额',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'wfkje',
                title: '未付款金额',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'bz',
                title: '备注',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'qglx',
                title: '请购类型',
                width: '4%',
                align: 'left',
                formatter:purchaseMatter_qglxformatter,
                visible: false
            },{
                field: 'qglbmc',
                title: '请购类别',
                width: '4%',
                align: 'left',
                visible: false
            },{
                field: 'qxqg',
                title: '请购',
                width: '4%',
                align: 'left',
                formatter:purchaseMatter_qxqgformatter,
                visible: true
            },{
                field: 'qgshzt',
                title: '审核状态',
                width: '6%',
                align: 'left',
                formatter:purchaseMatter_shztformatter,
                sortable: true,
                visible: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
            	alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            	PurchaseMatterDealById(row.qgmxid,row.htmxid,null,'view',$("#purchaseMatter_formSearch #btn_view").attr("tourl"));
             },
		});
		$("#purchaseMatter_formSearch #purchaseMatter_list").colResizable({
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
            sortLastName: "wlgl.wlbm", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
		return purchaseMatterSearchData(map);
	}
	return oTableInit
};
function htfkfsformat(value,row,index){
    var html ="";
    if (row.htfkfsmc){
        html = row.htfkfsmc;
        if (row.fkbz){
            html+=','+row.fkbz;
        }
    }
    return html;
}
/**
 * 是否初次订购
 * @returns
 */
function sfccdgformat(value,row,index) {
    var html="";
    if (row.ccdg == '1') {
        html="<span>"+"是"+"</span>";
    }else if (row.ccdg == '0'){
        html="<span>"+"否"+"</span>";
    }
    return html;
}
/**
 * U8入库单号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function u8rkdhformat(value,row,index){
    var html="";
    if(row.u8rkdh!=null&&row.u8rkdh!=''){
        var split = row.u8rkdh.split(',');
        if(split!=null&&split.length>0){
            for(var i=0;i<split.length;i++){
                html+="<span class='col-md-12 col-sm-12'>";
                var dh_lbcskz_csdm = split[i].split('!');
                var split1 = split[i].split("*");
                if(dh_lbcskz_csdm[1]=='1'||dh_lbcskz_csdm[1]=='2'||dh_lbcskz_csdm[1]=='3'||dh_lbcskz_csdm[1]=='4'||dh_lbcskz_csdm[1]=='5'||dh_lbcskz_csdm[2]=='cghz'){
                    if(split1[0]!=null&&split1[0]!=''){
                        html+="<a href='javascript:void(0);' style='width:70%;line-height:24px;' onclick=\"getInfoByDhdh('"+split1[2]+"')\">"+dh_lbcskz_csdm[0]+"</a>";
                    }
                }else{
                    if(split1[0]!=null&&split1[0]!=''){
                        html+="<a href='javascript:void(0);' style='width:70%;line-height:24px;' onclick=\"getInfoByRkid('"+split1[1]+"')\">"+dh_lbcskz_csdm[0]+"</a>";
                    }
                }
                html+="</span>";
            }
        }
    }
    return html;
}
/**
 * 交给财务事件格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function fplrsjformat(value,row,index){
    var html="";
    if(row.fplrsj!=null&&row.fplrsj!=''){
        var split1 = row.fplrsj.split(",");
        for(var i=0;i<split1.length;i++){
            html+="<span class='col-md-12 col-sm-12'>";
            html += "<span>"+split1[i]+"</span>";
            html+="</span>";
        }
    }
    return html;
}


function getInfoByDhdh(dhid){
    var url=$("#purchaseMatter_formSearch #urlPrefix").val()+"/storehouse/arrivalGoods/pagedataViewArrivalGoods?dhid="+dhid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'到货信息',viewDhConfig);
}
var viewDhConfig = {
    width		: "1600px",
    height		: "500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

function getInfoByRkid(rkid){
    var url=$("#purchaseMatter_formSearch #urlPrefix").val()+"/warehouse/putInStorage/pagedataViewPutInStorage?rkid="+rkid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'入库信息',viewHwConfig);
}
var viewHwConfig={
    width		: "1600px",
    modalName	:"viewHwModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

/**
 * 发票号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function fphformat(value,row,index){
    var html="";
    if(row.fph!=null&&row.fph!=''){
        var delReplyChar1 = delReplyChar(row.fph);
        for(var i=0;i<delReplyChar1.length;i++){
            var split = delReplyChar1[i].split("-");
            html+="<span class='col-md-12 col-sm-12'>";
            html += "<a href='javascript:void(0);' onclick=\"queryByFpid('"+split[1]+"')\">"+split[0]+"</a>";
            html+="</span>";
        }
    }
    return html;
}

function queryByFpid(fpid){
    var url=$("#purchaseMatter_formSearch #urlPrefix").val()+"/invoice/invoice/viewInvoice?fpid="+fpid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'发票详情',viewFpConfig);
}
var viewFpConfig = {
    width		: "1400px",
    modalName	:"viewInvoiceModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var delReplyChar = function (str) {
    var arr = str.split(',');
    var result = {};
    var resultArr = [];
    for (var i = 0; i < arr.length; i++) {
        if(!result[arr[i]]){
            result[arr[i]] = arr[i];
            resultArr.push(arr[i]);
        }
    }
    return resultArr;
};


//规格/货物标准格式化
function ggformat(value,row,index){
    var gg=row.gg;
    var title=row.gg;
    if(row.qglbdm!='MATERIAL' && row.qglbdm!=null && row.qglbdm!=''){
        gg=row.hwbz;
        title=row.hwbz;
    }
    var html="<span style='width:70%;line-height:24px;' title='"+title+"'>"+gg+"</span>";
    return html;
}

//单位格式化
function jldwformat(value,row,index){
    var jldw=row.jldw;
    var title=row.jldw;
    if(row.qglbdm!='MATERIAL' && row.qglbdm!=null && row.qglbdm!=''){
        jldw=row.hwjldw;
        title=row.hwjldw;
    }
    var html="<span style='width:70%;line-height:24px;' title='"+title+"'>"+jldw+"</span>";
    return html;
}
function purchaseMatter_wlmcformat(value,row,index){ 
	if(row.qglbdm!=null && row.qglbdm!='' && row.qglbdm != 'MATERIAL'){
		return row.hwmc;
	}else {
		return row.wlmc;
	}
}
/**
 * 物料编码格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function purchaseMatter_wlbmformat(value,row,index){
	var html = ""; 
	if(row.wlbm!=null && row.wlbm!=''){
		html += "<a href='javascript:void(0);' onclick=\"purchaseMatter_queryByWlbm('"+row.wlid+"')\">"+row.wlbm+"</a>";
	}else {
		html +="-";
	}
	return html;
}
function purchaseMatter_queryByWlbm(wlid){
	var url=$("#purchaseMatter_formSearch #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'物料信息',viewWlConfig);	
}
var viewWlConfig = {
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

function purchaseMatter_shztformatter(value,row,index) {
    if(row.qglx=='1'){
        if (row.qgshzt == '00') {
            return '未提交';
        }else if (row.qgshzt == '80') {
            return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONSTWO\",{prefix:\"" + $('#purchaseMatter_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
        }else if (row.qgshzt == '15') {
            return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONSTWO\",{prefix:\"" + $('#purchaseMatter_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
        }else{
            return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONSTWO\" ,{prefix:\"" + $('#purchaseMatter_formSearch #urlPrefix').val() + "\"})'>" + row.shxx_dqgwmc + "审核中</a>";
        }
    }else{
        if(row.qglbdm=='MATERIAL' ||  row.qglbdm==null || row.qglbdm==''){
            if (row.qgshzt == '00') {
                return '未提交';
            }else if (row.qgshzt == '80') {
                return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONS\",{prefix:\"" + $('#purchaseMatter_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
            }else if (row.qgshzt == '15') {
                return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONS\",{prefix:\"" + $('#purchaseMatter_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
            }else{
                return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONS\" ,{prefix:\"" + $('#purchaseMatter_formSearch #urlPrefix').val() + "\"})'>" + row.shxx_dqgwmc + "审核中</a>";
            }
        }else if(row.qglbdm=='ADMINISTRATION'){
            if (row.qgshzt == '00') {
                return '未提交';
            }else if (row.qgshzt == '80') {
                return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONS_ADMINISTRATION\",{prefix:\"" + $('#purchaseMatter_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
            }else if (row.qgshzt == '15') {
                return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONS_ADMINISTRATION\",{prefix:\"" + $('#purchaseMatter_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
            }else{
                return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONS_ADMINISTRATION\" ,{prefix:\"" + $('#purchaseMatter_formSearch #urlPrefix').val() + "\"})'>" + row.shxx_dqgwmc + "审核中</a>";
            }
        }else if(row.qglbdm=='SERVICE'){
            if (row.qgshzt == '00') {
                return '未提交';
            }else if (row.qgshzt == '80') {
                return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONS_SERVICE\",{prefix:\"" + $('#purchaseMatter_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
            }else if (row.qgshzt == '15') {
                return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONS_SERVICE\",{prefix:\"" + $('#purchaseMatter_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
            }else{
                return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONS_SERVICE\" ,{prefix:\"" + $('#purchaseMatter_formSearch #urlPrefix').val() + "\"})'>" + row.shxx_dqgwmc + "审核中</a>";
            }
        }else if(row.qglbdm=='DEVICE'){
            if (row.qgshzt == '00') {
                return '未提交';
            }else if (row.qgshzt == '80') {
                return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONS_DEVICE\",{prefix:\"" + $('#purchaseMatter_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
            }else if (row.qgshzt == '15') {
                return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONS_DEVICE\",{prefix:\"" + $('#purchaseMatter_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
            }else{
                return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONS_DEVICE\" ,{prefix:\"" + $('#purchaseMatter_formSearch #urlPrefix').val() + "\"})'>" + row.shxx_dqgwmc + "审核中</a>";
            }
        }else if(row.qglbdm=='OUTSOURCE'){
            if (row.qgshzt == '00') {
                return '未提交';
            }else if (row.qgshzt == '80') {
                return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONS\",{prefix:\"" + $('#purchaseMatter_formSearch #urlPrefix').val() + "\"})' >审核通过</a>";
            }else if (row.qgshzt == '15') {
                return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONS\",{prefix:\"" + $('#purchaseMatter_formSearch #urlPrefix').val() + "\"})' >审核未通过</a>";
            }else{
                return "<a href='javascript:void(0);' onclick='showAuditInfo(\"" + row.qgid + "\",event,\"AUDIT_REQUISITIONS\" ,{prefix:\"" + $('#purchaseMatter_formSearch #urlPrefix').val() + "\"})'>" + row.shxx_dqgwmc + "审核中</a>";
            }
        }
    }
}

/**
 * 单据号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function purchaseMatter_djhformat(value,row,index){
	var html = ""; 	
	if(row.jlbh!=null && row.jlbh!=''){
		html += "<a href='javascript:void(0);' onclick=\"purchaseMatter_queryByqgid('"+row.qgid+"')\">"+row.jlbh+"</a>";
	}else {
		html +="-";
	}
	return html;
}
function purchaseMatter_queryByqgid(qgid){
	var url=$("#purchaseMatter_formSearch #urlPrefix").val()+"/purchase/purchase/viewPurchase?qgid="+qgid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'请购信息',viewqgConfig);	
}
var viewqgConfig = {
		width		: "1500px",
		modalName	:"viewQgModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

// /**
//  * 合同日期格式化
//  * @param value
//  * @param row
//  * @param index
//  * @returns
//  */
// function purchaseMatter_htrqformat(value,row,index){
//     var html = "";
//     if(row.cjrq!=null){
//         var split = row.cjrq.split(',');
//         for(var i=0;i<split.length;i++){
//             html+="<span class='col-md-12 col-sm-12'>";
//             html+="<span href='javascript:void(0);' style='width:70%;line-height:24px;')\">"+split[i]+"</span>";
//             html+="</span>";
//         }
//     }else{
//         html +="-"
//     }
//     return html;
// }

/**
 * 合同数量格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function purchaseMatter_htslformat(value,row,index){
    var html = "";
    if(row.htsl!=null){
        var split = row.htsl.split(',');
        for(var i=0;i<split.length;i++){
            html+="<span class='col-md-12 col-sm-12'>";
            html+="<span href='javascript:void(0);' style='width:70%;line-height:24px;')\">"+split[i]+"</span>";
            html+="</span>";
        }
    }else{
        html +="-"
    }
    return html;
}

/**
 * 合同供应商格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function purchaseMatter_gysformat(value,row,index){
    var html = "";
    if(row.gysmc!=null){
        var split = row.gysmc.split(',');
        for(var i=0;i<split.length;i++){
            html+="<span class='col-md-12 col-sm-12'>";
            html+="<span href='javascript:void(0);' style='width:70%;line-height:24px;')\">"+split[i]+"</span>";
            html+="</span>";
        }
    }else{
        html +="-"
    }
    return html;
}

/**
 * 合同单价格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function purchaseMatter_htdjformat(value,row,index){
    var html = "";
    if(row.htdj!=null){
        var split = row.htdj.split(',');
        for(var i=0;i<split.length;i++){
            html+="<span class='col-md-12 col-sm-12'>";
            html+="<span href='javascript:void(0);' style='width:70%;line-height:24px;')\">"+split[i]+"</span>";
            html+="</span>";
        }
    }else{
        html +="-"
    }
    return html;
}

/**
 * 合同总金额格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function purchaseMatter_zjeformat(value,row,index){
    var html = "";
    if(row.hthjje!=null){
        var split = row.hthjje.split(',');
        for(var i=0;i<split.length;i++){
            html+="<span class='col-md-12 col-sm-12'>";
            html+="<span href='javascript:void(0);' style='width:70%;line-height:24px;')\">"+split[i]+"</span>";
            html+="</span>";
        }
    }else{
        html +="-"
    }
    return html;
}


/**
 * 计划到货日期格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function purchaseMatter_jhdhrqformat(value,row,index){
    var html = "";
    if(row.jhdhrq!=null){
        var split = row.jhdhrq.split(',');
        for(var i=0;i<split.length;i++){
            html+="<span class='col-md-12 col-sm-12'>";
            html+="<span href='javascript:void(0);' style='width:70%;line-height:24px;')\">"+split[i]+"</span>";
            html+="</span>";
        }
    }else{
        html +="-"
    }
    return html;
}

/**
 * 到货数量格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function purchaseMatter_dhslformat(value,row,index){
    var html = "";
    if(row.dhsl!=null){
        return row.dhsl;
    }else{
        return html;
    }
}

/**
 * 到货日期格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function purchaseMatter_dhrqformat(value,row,index){
    var html = "";
    if(row.dhrq!=null){
        var split = row.dhrq.split(',');
        for(var i=0;i<split.length;i++){
            html+="<span class='col-md-12 col-sm-12'>";
            html+="<span href='javascript:void(0);' style='width:70%;line-height:24px;')\">"+split[i]+"</span>";
            html+="</span>";
        }
    }else{
        html +="-"
    }
    return html;
}

/**
 * 供应商格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function gysformat(value,row,index){
    var html = "";
    if(row.gysmc!=null){
       return row.gysmc;
    }else{
        return html;
    }
}

/**
 * 合同ID格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function purchaseMatter_htidformat(value,row,index){
    var html = "";
    if(row.htid!=null){
        var split = row.htid.split(',');
        for(var i=0;i<split.length;i++){
            html+="<span class='col-md-12 col-sm-12'>";
            html+="<span href='javascript:void(0);' style='width:70%;line-height:24px;')\">"+split[i]+"</span>";
            html+="</span>";
        }
    }else{
        html +="-"
    }
    return html;
}

/**
 * 货物ID格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function purchaseMatter_hwidformat(value,row,index){
    var html = "";
    if(row.hwid!=null){
        var split = row.hwid.split(',');
        for(var i=0;i<split.length;i++){
            html+="<span class='col-md-12 col-sm-12'>";
            html+="<span href='javascript:void(0);' style='width:70%;line-height:24px;')\">"+split[i]+"</span>";
            html+="</span>";
        }
    }else{
        html +="-"
    }
    return html;
}

/**
 * 合同内部编号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function purchaseMatter_htbhformat(value,row,index){
    var html = "";
    if(row.htnbbh!=null && row.htnbbh!=''){
        html += "<a href='javascript:void(0);' onclick=\"purchaseMatter_queryByhtid('"+row.htid+"')\">"+row.htnbbh+"</a>";
    }else {
        html +="-";
    }
    return html;
}
function purchaseMatter_queryByhtid(htid){
	var url=$("#purchaseMatter_formSearch #urlPrefix").val()+"/contract/contract/pagedataViewContract?htid="+htid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'合同信息',viewhtConfig);	
}
var viewhtConfig = {
		width		: "1500px",
		modalName	:"viewHtModal",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			cancel : {
				label : "关 闭",
				className : "btn-default"
			}
		}
	};

/**
 * 是否请购格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function purchaseMatter_qxqgformatter(value,row,index){
	var html = "";
	if(row.qxqg=='1'){
		html="<span style='color:red;'>取消</span>";
	}else{
		html="<span style='color:green;'>正常</span>";
	}
	if(row.zt=='15'){
        html="<span style='color:red;'>拒绝</span>";
    }
	return html;
}
/**
 * 是否请购格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function purchaseMatter_qglxformatter(value,row,index){
	var html = "";
	if(row.qglx=='1'){
		html="<span>OA请购</span>";
	}else{
		html="<span>普通请购</span>";
	}
	return html;
}

function purchaseMatter_dhdhformat(value,row,index){
	var html = "";
	if(row.dhdh!=null){
        var split = row.dhdh.split(',');
        for(var i=0;i<split.length;i++){
            var split2 = split[i].split("+");
            html+="<span class='col-md-12 col-sm-12'>";
            html+="<a href='javascript:void(0);' style='width:70%;line-height:24px;' onclick=\"purchaseMatter_queryBydhdh('"+split2[1]+"')\">"+split2[0]+"</a>";
            html+="</span>";
        }
	}
	return html;
}
function purchaseMatter_queryBydhdh(dhid){
	var url=$("#purchaseMatter_formSearch #urlPrefix").val()+"/storehouse/arrivalGoods/pagedataViewArrivalGoods?dhid="+dhid+"&access_token=" + $("#ac_tk").val();
	$.showDialog(url,'到货信息',viewhwConfig);	
}

function purchaseMatterSearchData(map){
	var cxtj=$("#purchaseMatter_formSearch #cxtj").val();
	var cxnr=$.trim(jQuery('#purchaseMatter_formSearch #cxnr').val());
	if(cxtj=="0"){
		map["entire"]=cxnr;
	}else if(cxtj=="1"){
		map["djh"]=cxnr;
	}else if(cxtj=="2"){
		map["wlbm"]=cxnr;
	}else if(cxtj=="3"){
		map["wlmc"]=cxnr;
	}else if(cxtj=="4"){
		map["htnbbh"]=cxnr;
	}else if(cxtj=="5"){
		map["hwrkdh"]=cxnr;
	}
    // 初次订购
    var ccdg = jQuery('#purchaseMatter_formSearch #ccdg_id_tj').val();
    map["ccdg"] = ccdg;
    // 是否签订合同
    var sfqdht = jQuery('#purchaseMatter_formSearch #sfqdht_id_tj').val();
    map["sfqdht"] = sfqdht;
    // 是否及时到货
    var sfjsdh = jQuery('#purchaseMatter_formSearch #sfjsdh_id_tj').val();
    map["sfjsdh"] = sfjsdh;
    // 发票维护
    var fpwh = jQuery('#purchaseMatter_formSearch #fpwh_id_tj').val();
    map["fpwh"] = fpwh;
	// 合同标记
	var htbj = jQuery('#purchaseMatter_formSearch #htbj_id_tj').val();
	map["htbj"] = htbj;
	
	// 货物标记
	var hwbj = jQuery('#purchaseMatter_formSearch #hwbj_id_tj').val();
	map["hwbj"] = hwbj;
	
	// 取消标记
	var qxqg = jQuery('#purchaseMatter_formSearch #qxqg_id_tj').val();
	map["qxqg"] = qxqg;
	
	// 申请开始日期
	var sqsjstart = jQuery('#purchaseMatter_formSearch #sqsjstart').val();
	map["sqsjstart"] = sqsjstart;
	
	// 申请结束日期
	var sqsjend = jQuery('#purchaseMatter_formSearch #sqsjend').val();
	map["sqsjend"] = sqsjend;
	
	// 期望到货日期开始日期
	var qwdhsjstart = jQuery('#purchaseMatter_formSearch #qwdhsjstart').val();
	map["qwdhsjstart"] = qwdhsjstart;
	
	// 期望到货日期结束日期
	var qwdhsjend = jQuery('#purchaseMatter_formSearch #qwdhsjend').val();
	map["qwdhsjend"] = qwdhsjend;
	
	// 计划到货日期开始日期
	var jhdhsjstart = jQuery('#purchaseMatter_formSearch #jhdhsjstart').val();
	map["jhdhsjstart"] = jhdhsjstart;
	
	// 计划到货日期结束日期
	var jhdhsjend = jQuery('#purchaseMatter_formSearch #jhdhsjend').val();
	map["jhdhsjend"] = jhdhsjend;
	
	// 到货日期开始日期
	var dhsjstart = jQuery('#purchaseMatter_formSearch #dhsjstart').val();
	map["dhsjstart"] = dhsjstart;
	
	// 到货日期结束日期
	var dhsjend = jQuery('#purchaseMatter_formSearch #dhsjend').val();
	map["dhsjend"] = dhsjend;

    //请购审核通过时间开始时间
    var qgshtgsjstart = jQuery('#purchaseMatter_formSearch #qgshtgsjstart').val();
    map["qgshtgsjstart"] = qgshtgsjstart;

    //请购审核通过时间结束时间
    var qgshtgsjend = jQuery('#purchaseMatter_formSearch #qgshtgsjend').val();
    map["qgshtgsjend"] = qgshtgsjend;
    // 请购类型
    var qglx = jQuery('#purchaseMatter_formSearch #qglx_id_tj').val();
    map["qglx"] = qglx;

	// 审核状态
	var qgshzts = jQuery('#purchaseMatter_formSearch #qgshzt_id_tj').val();
	map["qgshzts"] = qgshzts;
    // 请购类别
    var qglbs = jQuery('#purchaseMatter_formSearch #qglb_id_tj').val();
    map["qglbs"] = qglbs;
    // 物料分类s
    var wlfls = jQuery('#purchaseMatter_formSearch #wlfl_id_tj').val();
    map["wlfls"] = wlfls;
	return map;
	

}


//提供给导出用的回调函数
function purchaseMatterDate(){
	var map ={};
	map["access_token"]=$("#ac_tk").val();
	map["sortLastName"]="wlgl.wlbm";
	map["sortLastOrder"]="desc";
	map["sortName"]="jlbh";
	map["sortOrder"]="desc";
	return purchaseMatterSearchData(map);
}

var PurchaseMatter_ButtonInit= function (){
	var oInit=new Object();
	var postdata = {};
	oInit.Init=function(){
		var btn_query=$("#purchaseMatter_formSearch #btn_query");
		var btn_view=$("#purchaseMatter_formSearch #btn_view");
		var btn_contractView=$("#purchaseMatter_formSearch #btn_contractView");
		var btn_searchexport = $("#purchaseMatter_formSearch #btn_searchexport");
    	var btn_selectexport = $("#purchaseMatter_formSearch #btn_selectexport");
    	var btn_maintenance = $("#purchaseMatter_formSearch #btn_maintenance");
    	var btn_contract = $("#purchaseMatter_formSearch #btn_contract");
        var btn_auditingsearchexport = $("#purchaseMatter_formSearch #btn_auditingsearchexport");
        var btn_auditingselectexport = $("#purchaseMatter_formSearch #btn_auditingselectexport");
    	
    	//添加日期控件
    	laydate.render({
    	   elem: '#sqsjstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#sqsjend'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#qwdhsjstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#qwdhsjend'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#jhdhsjstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#jhdhsjend'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#dhsjstart'
    	  ,theme: '#2381E9'
    	});
    	//添加日期控件
    	laydate.render({
    	   elem: '#dhsjend'
    	  ,theme: '#2381E9'
    	});
        //添加日期控件
        laydate.render({
            elem: '#purchaseMatter_formSearch #qgshtgsjstart'
            , theme: '#2381E9'
            , type: 'datetime'
            , format: 'yyyy-MM-dd HH:mm:ss'//保留时分
        });
        //添加日期控件
        laydate.render({
            elem: '#purchaseMatter_formSearch #qgshtgsjend'
            , theme: '#2381E9'
            , type: 'datetime'
            , format: 'yyyy-MM-dd HH:mm:ss'//保留时分
        });
        //添加日期控件
        laydate.render({
            elem: '#purchaseMatter_formSearch #lrsjstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#purchaseMatter_formSearch #lrsjend'
            ,theme: '#2381E9'
        });
		
    	 /* ------------------------------生成合同-----------------------------*/
    	btn_contract.unbind("click").click(function(){
    		var sel_row=$('#purchaseMatter_formSearch #purchaseMatter_list').bootstrapTable('getSelections');
    		var ids="";
    		if(sel_row.length>0){
    			for(var i = 0; i < sel_row.length; i++){
    				var sysl=sel_row[i].sysl;
    				var ydsl=sel_row[i].ydsl;
    				if(sysl==null || sysl==''){
    					sysl=0;
    				}
    				if(ydsl==null || ydsl==''){
    					ydsl=0;
    				}
    				if(parseInt(sysl*100)/100-parseInt(ydsl*100)/100<=0){
    					$.confirm(sel_row[i].wlmc+"已完成，不需要采购！");
    					return false;
    				}else{
    					ids= ids + ","+ sel_row[i].qgmxid;
    				}
    			}
    			ids=ids.substr(1);
    		}
    		PurchaseMatterDealById(ids,null,null,"contract",btn_contract.attr("tourl"));
    		
    	});
        // 	  ---------------------------审核导出-----------------------------------
        btn_auditingselectexport.unbind("click").click(function(){
            var sel_row = $('#purchaseMatter_formSearch #purchaseMatter_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length>=1){
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids = ids + ","+ sel_row[i].htmxid;
                }
                ids = ids.substr(1);
                $.showDialog($('#purchaseMatter_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=QGWL_AUDITINGSELECT&expType=select&ids="+ids
                    ,btn_auditingselectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
            }else{
                $.error("请选择数据");
            }
        });
        btn_auditingsearchexport.unbind("click").click(function(){
            $.showDialog($('#purchaseMatter_formSearch #urlPrefix').val()+exportPrepareUrl+"?ywid=QGWL_AUDITINGSEARCH&expType=search&callbackJs=purchaseMatterDate"
                ,btn_auditingsearchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
        });
    	 /* ------------------------------查看请购详细信息(高级查看)-----------------------------*/
    	btn_contractView.unbind("click").click(function(){
    		var sel_row=$('#purchaseMatter_formSearch #purchaseMatter_list').bootstrapTable('getSelections');
    		if(sel_row.length==1){
    			PurchaseMatterDealById(sel_row[0].qgmxid,sel_row[0].qgid,null,"contractView",btn_contractView.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
		/*--------------------------------模糊查询---------------------------*/
		if(btn_query!=null){
			btn_query.unbind("click").click(function(){
				searchPurchaseMatterResult(true); 
    		});
		}
	   	   

		 /* ------------------------------查看请购信息-----------------------------*/
    	btn_view.unbind("click").click(function(){
    		var sel_row=$('#purchaseMatter_formSearch #purchaseMatter_list').bootstrapTable('getSelections');
    		if(sel_row.length==1){
    			PurchaseMatterDealById(sel_row[0].qgmxid,sel_row[0].htmxid,null,"view",btn_view.attr("tourl"));
    		}else{
    			$.error("请选中一行");
    		}
    	});
    	
    	  /* ---------------------------过程维护-----------------------------------*/
    	btn_maintenance.unbind("click").click(function(){
    		var sel_row = $('#purchaseMatter_formSearch #purchaseMatter_list').bootstrapTable('getSelections');//获取选择行数据
    		var cwbj;
    		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
    			if(sel_row[i].htnbbh==""||sel_row[i].htnbbh==null){
    				cwbj = "1";
    			}
    		}
    		if(sel_row.length==0){
    			$.error("请至少选中一行");
    		}else if(cwbj=="1"){
    			$.error("合同为空的请购不容许维护。");
    			return false;
    		}else {
    			var ids="";
    			for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids= ids + ","+ sel_row[i].qgmxid+"."+sel_row[i].qgid;
        		}
    			ids=ids.substr(1);
    			PurchaseMatterDealById(ids,null,null,"maintenance",btn_maintenance.attr("tourl"));
    		}
    	});
    	
    	 // 	  ---------------------------导出-----------------------------------
    	btn_selectexport.unbind("click").click(function(){
    		var sel_row = $('#purchaseMatter_formSearch #purchaseMatter_list').bootstrapTable('getSelections');//获取选择行数据
    		if(sel_row.length>=1){
    			var ids="";
        		for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
        			ids = ids + ","+ sel_row[i].qgmxid+(sel_row[i].htmxid==null?'admin':sel_row[i].htmxid);
        		}
        		ids = ids.substr(1);
        		$.showDialog($("#purchaseMatter_formSearch #urlPrefix").val()+exportPrepareUrl+"?ywid=PURCHASEMATTER_SELECT&expType=select&ids="+ids
        				,btn_selectexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    		}else{
    			$.error("请选择数据");
    		}
    	});

    	btn_searchexport.unbind("click").click(function(){
    		$.showDialog($("#purchaseMatter_formSearch #urlPrefix").val()+exportPrepareUrl+"?ywid=PURCHASEMATTER_SEARCH&expType=search&callbackJs=purchaseMatterDate"
    				,btn_searchexport.attr("gnm"), $.extend({},viewConfig,{"width":"1020px"}));
    	});	
    	
       	/**显示隐藏**/      
    	$("#purchaseMatter_formSearch #sl_searchMore").on("click", function(ev){
    		var ev=ev||event; 
    		if(purchaseMatter_turnOff){
    			$("#purchaseMatter_formSearch #searchMore").slideDown("low");
    			purchaseMatter_turnOff=false;
    			this.innerHTML="基本筛选";
    		}else{
    			$("#purchaseMatter_formSearch #searchMore").slideUp("low");
    			purchaseMatter_turnOff=true;
    			this.innerHTML="高级筛选";
    		}
    		ev.cancelBubble=true;
    	});

	};
	return oInit;
};

function PurchaseMatterDealById(id,htmxid,qxbj,action,tourl){
	var url= tourl;
	if(!tourl){
		return;
	}
	tourl = $("#purchaseMatter_formSearch #urlPrefix").val()+tourl;
	if(action =='view'){
		var url= tourl+"?qgmxid="+id+"&htmxid="+htmxid;
		$.showDialog(url,'查看请购信息',viewPurchaseConfig);
	}else if(action =='contractView'){
		var url= tourl+"?htmxid="+id;
		$.showDialog(url,'查看请购信息',viewmorePurchaseConfig);
	}else if(action == 'maintenance'){
		var url= tourl + "?ids=" +id;
		$.showDialog(url,'过程维护',maintenanceMatterPurchaseConfig);
	}else if(action == 'contract'){
		var url= tourl + "?ids=" +id;
		$.showDialog(url,'生成合同',PurchaseMatteraddContractConfig);
	}
}

var PurchaseMatteraddContractConfig = {
		width		: "1400px",
		modalName	: "addContractModel",
		formName	: "editContractForm",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if(!$("#editContractForm").valid()){
						return false;
					}
					// 判断合同明细数量
					var isBeyond = "true";
					$("#editContractForm div[name='sldiv']").each(function(){
						if(this.getAttribute("isBeyond") == "false"){
							isBeyond = "false";
							return false;
						}
					});
					if(isBeyond == "false"){
						$.error("合同明细数量超出，请重新填写！");
						return false;
					}
					
					var $this = this;
					var opts = $this["options"]||{};
					if(t_map.rows != null && t_map.rows.length > 0){
						$("#editContractForm #htmxJson").val(JSON.stringify(t_map.rows));
					}
					$("#editContractForm input[name='access_token']").val($("#ac_tk").val());
					submitForm(opts["formName"]||"editContractForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
								}
							});
						}else if(responseText["status"] == "fail"){
							// 加载页面提示div
							var htmxList_wlgl = responseText["htmxList_wlgl"];
							var htmxList_qgmx = responseText["htmxList_qgmx"];
							if(htmxList_wlgl && htmxList_wlgl.length > 0){
								$("#checkwlgl").attr("style","display:block;");
								$("#checkwlgl").empty();
								var html = '<table border="1" cellspacing="0" cellpadding="0" class="table table-hover table-bordered table-hidden" style="text-align: center;">';
								html += '<tr style="background-color: #63a3ff;">';
								html += '<th style="width:100px;"><span />物料编码</th>';
								html += '<th style="width:100px;"><span />起订量</th>';
								html += '<th style="width:100px;"><span />当前数量</th>';
								html += '</tr>';
								for (var i = 0; i < htmxList_wlgl.length; i++) {
									html += '<tr>';
									html += '<td><span title="' + htmxList_wlgl[i].wlbm + '"/>' + htmxList_wlgl[i].wlbm + '</td>';
									html += '<td><span title="' + htmxList_wlgl[i].qdl + '"/>' + htmxList_wlgl[i].qdl + '</td>';
									html += '<td><span title="' + htmxList_wlgl[i].sl + '"/>' + htmxList_wlgl[i].sl + '</td>';
									html += '</tr>';
								}
								html += '</table>';
								$("#checkwlgl").append(html);
								preventResubmitForm(".modal-footer > button", false);
								$.confirm(responseText["message"] + ' 是否继续提交！',function(result){
					    			if(result){
					    				$("#checkqdl").val("0"); // 跳过起订量校验
					    				submitForm(opts["formName"]||"editContractForm",function(responseText,statusText){
					    					if(responseText["status"] == 'success'){
					    						$.success(responseText["message"],function() {
					    							if(opts.offAtOnce){
					    								$.closeModal(opts.modalName);
					    							}
					    						});
					    					}else if(responseText["status"] == "fail"){
					    						$.error(responseText["message"],function() {
				    								preventResubmitForm(".modal-footer > button", false);
				    							});
					    						// 加载页面提示div
					    						var htmxList_qgmx = responseText["htmxList_qgmx"];
					    						if(htmxList_qgmx && htmxList_qgmx.length > 0){
					    							$("#checkqgmx").attr("style","display:block;");
					    							$("#checkqgmx").empty();
					    							var html = '<table border="1" cellspacing="0" cellpadding="0" class="table table-hover table-bordered table-hidden" style="text-align: center;">';
					    							html += '<tr style="background-color: #63a3ff;">';
					    							html += '<th style="width:100px;"><span />请购单号</th>';
					    							html += '<th style="width:100px;"><span />物料编码</th>';
					    							html += '<th style="width:100px;"><span />起订量</th>';
					    							html += '<th style="width:100px;"><span />未完成总数</th>';
					    							html += '<th style="width:100px;"><span />采购中数</th>';
					    							html += '<th style="width:100px;"><span />当前数量</th>';
					    							html += '</tr>';
					    							for (var i = 0; i < htmxList_qgmx.length; i++) {
					    								html += '<tr>';
					    								html += '<td><span title="' + htmxList_qgmx[i].djh + '"/>' + htmxList_qgmx[i].djh + '</td>';
					    								html += '<td><span title="' + htmxList_qgmx[i].wlbm + '"/>' + htmxList_qgmx[i].wlbm + '</td>';
					    								html += '<td><span title="' + htmxList_qgmx[i].qdl + '"/>' + htmxList_qgmx[i].qdl + '</td>';
					    								html += '<td><span title="' + htmxList_qgmx[i].sysl + '"/>' + htmxList_qgmx[i].sysl + '</td>';
					    								html += '<td><span title="' + htmxList_qgmx[i].ydsl + '"/>' + htmxList_qgmx[i].ydsl + '</td>';
					    								html += '<td><span title="' + htmxList_qgmx[i].sl + '"/>' + htmxList_qgmx[i].sl + '</td>';
					    								html += '</tr>';
					    							}
					    							html += '</table>';
					    							$("#checkqgmx").append(html);
					    						}
					    					} else{
					    						$.alert(responseText["message"],function() {
					    						});
					    					}
					    				},".modal-footer > button");
					    				
					    			}
					    		});
					    	}else if(htmxList_qgmx && htmxList_qgmx.length > 0){
								$("#checkqgmx").attr("style","display:block;");
								$("#checkqgmx").empty();
								var html = '<table border="1" cellspacing="0" cellpadding="0" class="table table-hover table-bordered table-hidden" style="text-align: center;">';
								html += '<tr style="background-color: #63a3ff;">';
								html += '<th style="width:100px;"><span />请购单号</th>';
								html += '<th style="width:100px;"><span />物料编码</th>';
								html += '<th style="width:100px;"><span />起订量</th>';
								html += '<th style="width:100px;"><span />未完成总数</th>';
								html += '<th style="width:100px;"><span />采购中数</th>';
								html += '<th style="width:100px;"><span />当前数量</th>';
								html += '</tr>';
								for (var i = 0; i < htmxList_qgmx.length; i++) {
									html += '<tr>';
									html += '<td><span title="' + htmxList_qgmx[i].djh + '"/>' + htmxList_qgmx[i].djh + '</td>';
									html += '<td><span title="' + htmxList_qgmx[i].wlbm + '"/>' + htmxList_qgmx[i].wlbm + '</td>';
									html += '<td><span title="' + htmxList_qgmx[i].qdl + '"/>' + htmxList_qgmx[i].qdl + '</td>';
									html += '<td><span title="' + htmxList_qgmx[i].sysl + '"/>' + htmxList_qgmx[i].sysl + '</td>';
									html += '<td><span title="' + htmxList_qgmx[i].ydsl + '"/>' + htmxList_qgmx[i].ydsl + '</td>';
									html += '<td><span title="' + htmxList_qgmx[i].sl + '"/>' + htmxList_qgmx[i].sl + '</td>';
									html += '</tr>';
								}
								html += '</table>';
								$("#checkqgmx").append(html);
								$.error(responseText["message"],function() {
									preventResubmitForm(".modal-footer > button", false);
								});
							}else{
								$.error(responseText["message"],function() {
									preventResubmitForm(".modal-footer > button", false);
								});
							}
						} else{
							$.alert(responseText["message"],function() {
								preventResubmitForm(".modal-footer > button", false);
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
 * 过程维护
 */
var maintenanceMatterPurchaseConfig = {
		width		: "1600px",
		modalName	: "maintenanceModal",
		formName	: "puchasemaintenanceForm",
		offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
		buttons		: {
			success : {
				label : "确 定",
				className : "btn-primary",
				callback : function() {
					if($("#puchasemaintenanceForm input[name='checkQgmx']:checked").length == 0){
						$.alert("未选中请购明细信息！");
						return false;
					}
					if($("#puchasemaintenanceForm #kdlx").val()==null || $("#puchasemaintenanceForm #kdlx").val()==""){
						$.alert("请填写完整信息");
						return false;
					}
					if($("#puchasemaintenanceForm #kddh").val()==null || $("#puchasemaintenanceForm #kddh").val()==""){
						$.alert("请填写完整信息");
						return false;
					}
					if($("#puchasemaintenanceForm #yzbj").val()=="1"){
						if($("#puchasemaintenanceForm #kdlxmc").val()==null || $("#puchasemaintenanceForm #kdlxmc").val()==""){
							$.alert("请填写完整信息");
							return false;
						}				
					}
					var ids="";
					$("#puchasemaintenanceForm input[name='checkQgmx']").each(function(){
						if($(this).is(":checked")){
							var htmxid = this.dataset.htmxid
							ids= ids + ","+ htmxid;
						}
					})
					ids=ids.substr(1);
					$("#puchasemaintenanceForm #ids").val(ids);
					var $this = this;					
					var opts = $this["options"]||{};
					$("#puchasemaintenanceForm input[name='access_token']").val($("#ac_tk").val());
					
					submitForm(opts["formName"]||"puchasemaintenanceForm",function(responseText,statusText){
						if(responseText["status"] == 'success'){
							$.success(responseText["message"],function() {
								if(opts.offAtOnce){
									$.closeModal(opts.modalName);
									searchPurchaseMatterResult();
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
var viewPurchaseConfig={
	width		: "1600px",
	modalName	:"viewPurchaseModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
}

/**
 * 查看页面模态框(高级查看)
 */
var viewmorePurchaseConfig={
	width		: "1600px",
	modalName	:"viewmorePurchaseModal",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
}


/**
 * 列表刷新
 * @param isTurnBack
 * @returns
 */
function searchPurchaseMatterResult(isTurnBack){
	//关闭高级搜索条件
	$("#purchaseMatter_formSearch #searchMore").slideUp("low");
    purchaseMatter_turnOff=true;
	$("#purchaseMatter_formSearch #sl_searchMore").html("高级筛选");
	if(isTurnBack){
		$('#purchaseMatter_formSearch #purchaseMatter_list').bootstrapTable('refresh',{pageNumber:1});
	}else{
		$('#purchaseMatter_formSearch #purchaseMatter_list').bootstrapTable('refresh');
	}
}

var purchaseMatter_PageInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
    	var qgshzt = $("#purchaseMatter_formSearch a[id^='qgshzt_id_']");
    	var qglb=$("#purchaseMatter_formSearch a[id^='qglb_id_']")
    	$.each(qgshzt, function(i, n){
    		var id = $(this).attr("id");
    		var code = id.substring(id.lastIndexOf('_')+1,id.length);
    		if(code === '80'){
    			addTj('qgshzt',code,'purchaseMatter_formSearch');
    		}
    	});
        $.each(qglb, function(i, n){
            var id = $(this).attr("id");
            var code = id.substring(id.lastIndexOf('_')+1,id.length);
            var qglbdm=$("#purchaseMatter_formSearch #"+id).attr("csdm");
            if(qglbdm == 'DEVICE' || qglbdm == 'MATERIAL' || qglbdm == 'SERVICE'|| qglbdm == 'OUTSOURCE'){
                addTj('qglb',code,'purchaseMatter_formSearch');
            }
        });
    }
    return oInit;
}

$(function(){
	//0.界面初始化
    var oInit = new purchaseMatter_PageInit();
    oInit.Init();
	// 1.初始化Table
	var oTable = new PurchaseMatter_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new PurchaseMatter_ButtonInit();
    oButtonInit.Init();
    jQuery('#purchaseMatter_formSearch .chosen-select').chosen({width: '100%'});
    
    $("#purchaseMatter_formSearch [name='more']").each(function(){
		$(this).on("click", s_showMoreFn);
	});
})