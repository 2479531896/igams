var t_map = [];
// 显示字段
var columnsArray = [
    {
        title: '序号',
        formatter: function (value, row, index) {
            return index+1;
        },
        titleTooltip:'序号',
        width: '3%',
        align: 'left',
        visible:true
    }, {
        field: 'fpmxid',
        title: '发票明细ID',
        titleTooltip:'发票明细ID',
        width: '8%',
        align: 'left',
        visible: false
    }, {
        field: 'wlid',
        title: '物料ID',
        titleTooltip:'物料ID',
        width: '8%',
        align: 'left',
        visible: false
    }, {
        field: 'u8rkdh',
        title: 'U8入库单号',
        titleTooltip:'U8入库单号',
        width: '8%',
        align: 'left',
        formatter:u8rkdhformat,
        visible: true
    },{
        field: 'htnbbh',
        title: '合同单号',
        titleTooltip:'合同单号',
        width: '10%',
        align: 'left',
        formatter:htdhformat,
        visible: true
    },{
        field: 'sbysdh',
        title: '设备验收单号',
        titleTooltip:'设备验收单号',
        width: '10%',
        align: 'left',
        formatter:sbysdhformat,
        visible: true
    },{
        field: 'qglbdm',
        title: '请购类别',
        titleTooltip:'请购类别',
        width: '5%',
        align: 'left',
        formatter:qglbformat,
        visible: true
    }, {
        field: 'wlbm',
        title: '物料编码',
        titleTooltip:'物料编码',
        width: '7%',
        formatter:MaterialCodeformat,
        align: 'left',
        visible: true
    }, {
        field: 'wlmc',
        title: '物料/服务/货物名称',
        titleTooltip:'物料/服务/货物名称',
        formatter:wlmcformat,
        width: '7%',
        align: 'left',
        visible: true
    },{
        field: 'gg',
        title: '规格/标准',
        titleTooltip:'规格/标准',
        width: '6%',
        formatter:ggformat,
        align: 'left',
        visible: true
    }, {
        field: 'sl',
        title: '数量',
        titleTooltip:'数量',
        width: '4%',
        align: 'left',
        formatter:slformat,
        visible: true
    }, {
        field: 'hsdj',
        title: '含税单价',
        titleTooltip:'含税单价',
        width: '5%',
        align: 'left',
        visible: true
    }, {
        field: 'wwhsl',
        title: '未维护数量',
        titleTooltip:'未维护数量',
        width: '5%',
        align: 'left',
        visible: true
    },{
        field: 'hjje',
        title: '总金额',
        titleTooltip:'总金额',
        width: '5%',
        align: 'left',
        formatter:zjeformat,
        visible: true
    },{
        field: 'cz',
        title: '操作',
        titleTooltip:'操作',
        width: '6%',
        align: 'left',
        formatter:czformat,
        visible: true
    }];
var invoiceEdit_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#editInvoiceForm #tb_list').bootstrapTable({
            url: $('#editInvoiceForm #urlPrefix').val()+'/invoice/invoice/pagedataGetInvoiceDetailList',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#editInvoiceForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "xh",				//排序字段
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
            uniqueId: "fpmxid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
            onLoadSuccess: function (map) {
                t_map = map;
                if(t_map.rows){
                    // 初始化fpmx_json
                    var json = [];
                    var str="";
                    var shuil="";
                    var hl="";
                    var slflag=true;
                    var hlflag=true;
                    $("#editInvoiceForm #htid").tagsinput({
                        itemValue: "htid",
                        itemText: "htnbbh",
                    });
                    if(t_map.rows.length>0){
                        if(t_map.rows[0].biz!=null&&t_map.rows[0].biz!=''){
                            var select = document.getElementById("biz");
                            for (var j = 0; j < select.options.length; j++){
                                if (select.options[j].id == t_map.rows[0].biz){
                                    $("#editInvoiceForm #biz").val(select.options[j].value);
                                    $("#editInvoiceForm #"+select.options[j].id).prop("selected","selected");
                                    $("#editInvoiceForm #"+select.options[j].id).trigger("chosen:updated");
                                    break;
                                }
                            }
                        }
                    }
                    var fpzje=0;
                    for (var i = 0; i < t_map.rows.length; i++) {
                        fpzje=fpzje+(t_map.rows[i].hjje*1);
                        t_map.rows[i].sl=(t_map.rows[i].sl*1).toFixed(5);
                        t_map.rows[i].hjje=(t_map.rows[i].hjje*1).toFixed(5);
                        if(str.indexOf(t_map.rows[i].htnbbh) == -1){
                            $("#editInvoiceForm #htid").tagsinput('add', {"htid":t_map.rows[i].htid,"htnbbh":t_map.rows[i].htnbbh});
                            str=str+t_map.rows[i].htnbbh+",";
                        }
                        if(i == 0){
                            shuil=shuil+t_map.rows[i].suil;
                            hl=hl+t_map.rows[i].hl;
                        }
                        if(shuil.indexOf(t_map.rows[i].suil)==-1){
                            slflag=false;
                        }
                        if(hl.indexOf(t_map.rows[i].hl)==-1){
                            hlflag=false;
                        }
                        var sz = {"htid":'',"htmxid":'',"sl":'',"hjje":'',"mxsl":'',"wwhsl":'',"qglbdm":'',"sbysid":'',"sbysdh":''};
                        sz.htid = t_map.rows[i].htid;
                        sz.htmxid = t_map.rows[i].htmxid;
                        sz.sl = t_map.rows[i].sl;
                        sz.mxsl = t_map.rows[i].mxsl;
                        sz.hjje = t_map.rows[i].hjje;
                        sz.wwhsl = t_map.rows[i].wwhsl;
                        sz.qglbdm = t_map.rows[i].qglbdm;
                        sz.sbysid = t_map.rows[i].sbysid;
                        sz.sbysdh = t_map.rows[i].sbysdh;
                        json.push(sz);
                    }
                    $("#editInvoiceForm #kpje").val(fpzje.toFixed(5));
                    if(!$("#editInvoiceForm #sl").val()){
                        var select = document.getElementById("fpzl");
                        if(slflag&&shuil!='null'){
                            $("#editInvoiceForm #sl").val(shuil);
                            for (var i = 0; i < select.options.length; i++){
                                if (select.options[i].id == (shuil*1).toString()){
                                    $("#editInvoiceForm #fpzl").val(select.options[i].value);
                                    $("#editInvoiceForm #"+select.options[i].id).prop("selected","selected");
                                    $("#editInvoiceForm #"+select.options[i].id).trigger("chosen:updated");
                                    break;
                                }
                            }
                        }else{
                            for (var i = 0; i < select.options.length; i++){
                                if (select.options[i].id == '0'){
                                    $("#editInvoiceForm #fpzl").val(select.options[i].value);
                                    $("#editInvoiceForm #"+select.options[i].id).prop("selected","selected");
                                    $("#editInvoiceForm #"+select.options[i].id).trigger("chosen:updated");
                                    break;
                                }
                            }
                        }
                    }
                    // if(!$("#editInvoiceForm #hl").val()){
                    //     if(hlflag&&hl!='null'){
                    //         $("#editInvoiceForm #hl").val(hl);
                    //     }
                    // }
                    $("#editInvoiceForm #fpmx_json").val(JSON.stringify(json));
                    $("#editInvoiceForm #tb_list").bootstrapTable('load',t_map);
                }
            },
            onLoadError: function () {
                alert("数据加载失败！");
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
            pageSize: 1,   //页面大小
            pageNumber: 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "fpmxid", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            ids: $("#editInvoiceForm #ids").val(),
            fpid: $("#editInvoiceForm #fpid").val(),
            sffpwh:"0",
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};



/**
 * 物料编码格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function MaterialCodeformat(value,row,index){
    var html = "";
    if(row.wlbm)
        html += "<a href='javascript:void(0);' onclick=\"queryByMaterialCode('"+row.wlid+"')\">"+row.wlbm+"</a>";
    return html;
}
function queryByMaterialCode(wlid){
    var url=$("#editInvoiceForm #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'物料信息',viewMaterialCodeConfig);
}
var viewMaterialCodeConfig = {
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


/**
 * 物料名称格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function wlmcformat(value,row,index){

    if(row.qglbdm == null){
        row.qglbdm = "";
    }
    var html ="";
    if(row.qglbdm=='MATERIAL'){
        html += "<span>"+row.wlmc+"</span>";
    }else  if(row.qglbdm=='ADMINISTRATION'){
        if(row.hwmc)
            html += "<a href='javascript:void(0);' onclick=\"queryByQgmxid('"+row.qgmxid+"')\">"+row.hwmc+"</a>";
    }else  if(row.qglbdm=='SERVICE'){
        if(row.hwmc)
            html += "<a href='javascript:void(0);' onclick=\"queryByQgmxid('"+row.qgmxid+"')\">"+row.hwmc+"</a>";
    }else  if(row.qglbdm=='DEVICE'){
    	if(row.wlmc==null){
    		html += "<span>"+row.hwmc+"</span>";
    	}else{
    		html += "<span>"+row.wlmc+"</span>";
    	}
    }

    return html;
}

function queryByQgmxid(qgmxid){
    var url=$("#editInvoiceForm #urlPrefix").val()+"/purchase/production/viewPurchaseDetail?qgmxid="+qgmxid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'请购明细',viewQgmxConfig);
}
var viewQgmxConfig = {
    width		: "1200px",
    height		: "200px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

/**
 * 规格格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function ggformat(value,row,index){
	 var html ="";
	if(row.gg == null){
		html += "<span>"+row.hwbz+"</span>";
	}else{
		html += "<span>"+row.gg+"</span>";
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
    if(row.fpmxDtos!=null&&row.fpmxDtos.length>0){
        for(var i=0;i<row.fpmxDtos.length;i++){
            html+="<span class='col-md-12 col-sm-12'>";
            if(row.fpmxDtos[i].lbcskz1!=null&&row.fpmxDtos[i].lbcskz1!=''){
                if(row.fpmxDtos[i].u8rkdh!=null&&row.fpmxDtos[i].u8rkdh!=''){
                    html+="<a href='javascript:void(0);' style='width:70%;line-height:24px;' onclick=\"getInfoByDhdh('"+row.fpmxDtos[i].dhid+"')\">"+row.fpmxDtos[i].u8rkdh+"</a>";
                }
            }else{
                if(row.fpmxDtos[i].u8rkdh!=null&&row.fpmxDtos[i].u8rkdh!=''){
                    html+="<a href='javascript:void(0);' style='width:70%;line-height:24px;' onclick=\"getInfoByRkid('"+row.fpmxDtos[i].rkid+"')\">"+row.fpmxDtos[i].u8rkdh+"</a>";
                }
            }
            html+="</span>";
        }
    }
    return html;
}
function sbysdhformat(value,row,index) {
    var html="";
    if(row.fpmxDtos!=null&&row.fpmxDtos.length>0){
        for(var i=0;i<row.fpmxDtos.length;i++){
            html+="<span class='col-md-12 col-sm-12'>";
                if(row.fpmxDtos[i].sbysdh!=null&&row.fpmxDtos[i].sbysdh!=''){
                    html+="<a href='javascript:void(0);' style='width:70%;line-height:24px;' onclick=\"queryBySbysdh('"+row.fpmxDtos[i].sbysid+"')\">"+row.fpmxDtos[i].sbysdh+"</a>";
                }
            html+="</span>";
        }
    }
    return html;

}function queryBySbysdh(sbysid){
    var url=$("#editInvoiceForm #urlPrefix").val()+"/inspectionGoods/inspectionGoods/pagedataEquipmentAcceptanceOA?sbysid="+sbysid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'设备信息',viewSbConfig);
}
var viewSbConfig = {
    width		: "1300px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
function getInfoByDhdh(dhid){
    var url=$("#editInvoiceForm #urlPrefix").val()+"/storehouse/arrivalGoods/pagedataViewArrivalGoods?dhid="+dhid+"&access_token=" + $("#ac_tk").val();
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
    var url=$("#editInvoiceForm #urlPrefix").val()+"/warehouse/putInStorage/pagedataViewPutInStorage?rkid="+rkid+"&access_token=" + $("#ac_tk").val();
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
 * 查看详情格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function htdhformat(value,row,index){
    var html="";
    if(row.htid!=null && row.htid!=''){
        html="<a href='javascript:void(0);' style='width:70%;line-height:24px;' onclick=\"queryByHtid('"+row.htid+"')\">"+row.htnbbh+"</a>";
    }
    return html;
}

function queryByHtid(htid){
    var url=$("#editInvoiceForm #urlPrefix").val()+"/contract/contract/pagedataViewContract?htid="+htid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'合同信息',viewHtConfig);
}

var viewHtConfig={
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
 * 数量格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function slformat(value,row,index){
	
    if(row.sl == null){
        row.sl = "";
    }
    if(row.mxsl == null){
        row.mxsl = "";
    }else{
        row.mxsl=parseFloat( row.mxsl).toFixed(5);
    }
    var html ="";
    html += "<input id='sl_"+index+"' value='"+row.sl+"' name='sl_"+index+"' style='width:100%;border:1px solid #D5D5D5;' validate='{dhslNumber:true}' onblur=\"checkSum('"+index+"',this,\'sl\')\"></input>";
    html += "<div style='background-color: green'><p  id='mxsl_"+index+"' style='font-size:14px;width:100%;margin-left:3px;'>/"+row.mxsl+"</p></div>";
    return html;
}

/**
 * 验证数量格式(两位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("dhslNumber", function (value, element){
    if(!value){
        $.error("请填写数量!");
        return false;
    }else{
        if(!/^\d+(\.\d{1,5})?$/.test(value)){
            $.error("请填写正确数量格式，可保留五位小数!");
        }
    }
    return this.optional(element) || /^\d+(\.\d{1,5})?$/.test(value);
},"请填写正确格式，可保留五位小数。");

/**
 * 总金额格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function zjeformat(value,row,index){
    if(row.hjje == null){
        row.hjje = "";
    }
    var html =""
    html += "<input id='hjje_"+index+"' value='"+row.hjje+"' name='hjje_"+index+"' style='width:100%;border:1px solid #D5D5D5;' validate='{zjeNumber:true}' onblur=\"checkSum('"+index+"',this,\'hjje\')\"></input>";
    return html;
}

/**
 * 请购类别格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function qglbformat(value,row,index){
    if(row.qglbdm == null){
        row.qglbdm = "";
    }
    var html ="";
    if(row.qglbdm=='MATERIAL'){
        html += "<span>物料</span>";
    }else  if(row.qglbdm=='ADMINISTRATION'){
        html += "<span>行政</span>";
    }else  if(row.qglbdm=='SERVICE'){
        html += "<span>服务</span>";
    }else  if(row.qglbdm=='DEVICE'){
        html += "<span>设备</span>";
    }

    return html;
}

/**
 * 验证总金额格式(五位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("zjeNumber", function (value, element){
    if(!value){
        $.error("请填写金额!");
        return false;
    }else{
        if(!/^\d+(\.\d{1,5})?$/.test(value)){
            $.error("请填写正确格式，可保留五位小数!");
        }
    }
    return this.optional(element) || /^\d+(\.\d{1,5})?$/.test(value);
},"请填写正确格式，可保留五位小数。");


/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function czformat(value,row,index){
    var html = "<span style='margin-left:5px;height:24px !important;' class='btn btn-default' title='删除明细' onclick=\"delInvoiceDetail('" + index + "',event)\" >删除</span>";
    return html;
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
        var preJson = JSON.parse($("#editInvoiceForm #fpmx_json").val());
        if (zdmc == 'sl') {
            var sl = e.value;
            t_map.rows[index].sl = sl;
            preJson[index].sl=sl;
            if(!sl){
                sl = 0;
            }
            var hsdj = t_map.rows[index].hsdj;
            if(hsdj){ // 根据含税单价计算其它金额
                var zje = calculateZje(hsdj,sl);
                t_map.rows[index].hjje = zje;
                preJson[index].hjje=zje;
                $("#editInvoiceForm #hjje_"+index).val(zje);
                var fpzje=0;
                for (var i = 0; i < t_map.rows.length; i++) {
                    fpzje=fpzje+(t_map.rows[i].hjje*1);
                }
                $("#editInvoiceForm #kpje").val(fpzje.toFixed(5));
            }
        }else if(zdmc == 'hjje'){
            var hjje = e.value;
            t_map.rows[index].hjje = hjje;
            preJson[index].hjje=hjje;
            if(!hjje){
                hjje = 0;
            }
            var hsdj = t_map.rows[index].hsdj;
            if(hsdj){ // 根据含税单价计算其它金额
                // var sl = calculateDhsl(hsdj,hjje);
                // t_map.rows[index].sl = sl;
                // preJson[index].sl=sl;
                // $("#editInvoiceForm #sl_"+index).val(sl);
                var fpzje=0;
                for (var i = 0; i < t_map.rows.length; i++) {
                    fpzje=fpzje+(t_map.rows[i].hjje*1);
                }
                $("#editInvoiceForm #kpje").val(fpzje.toFixed(5));
            }
        }
        $("#editInvoiceForm #fpmx_json").val(JSON.stringify(preJson));
    }
}


/**
 * 总金额 = 数量 * 含税单价
 * @param sl
 * @param hsdj
 * @returns
 */
function calculateZje(hsdj, dhsl){
    var zje =((hsdj*1)*(dhsl*1)).toFixed(5);
    return zje.toString();
}

/**
 * 数量 = 总金额 / 含税单价
 * @param sl
 * @param hsdj
 * @returns
 */
function calculateDhsl(hsdj, zje){
    var dhsl =((zje*1)/(hsdj*1)).toFixed(5);
    return dhsl.toString();
}
/**
 * 删除操作(从合同明细删除)
 * @param index
 * @param event
 * @returns
 */
function delInvoiceDetail(index,event){
    var preJson = JSON.parse($("#editInvoiceForm #fpmx_json").val());
    preJson.splice(index,1);
    t_map.rows.splice(index,1);
    var fpzje=0;
    for (var i = 0; i < t_map.rows.length; i++) {
        fpzje=fpzje+(t_map.rows[i].hjje*1);
    }
    $("#editInvoiceForm #kpje").val(fpzje.toFixed(5));
    $("#editInvoiceForm #tb_list").bootstrapTable('load',t_map);
    $("#editInvoiceForm #fpmx_json").val(JSON.stringify(preJson));
}

/**
 * 初始化页面
 * @returns
 */
function init(){
    //添加日期控件
    laydate.render({
        elem: '#editInvoiceForm #kprq'
        ,theme: '#2381E9'
    });
}

/**
 * 监听单据号标签移除事件
 */
var tagsRemoved = $('#editInvoiceForm #htid').on('beforeItemRemove', function(event) {
    $.confirm('您确定要删除到货单下的所有明细记录吗？',function(result){
        if(result){
            // 获取未更改请购单和明细信息
            var preJson = [];
            if($("#editInvoiceForm #fpmx_json").val()){
                preJson = JSON.parse($("#editInvoiceForm #fpmx_json").val());
                // 删除明细并刷新列表
                var htid = event.item.htid;
                for (var i = t_map.rows.length-1; i >= 0 ; i--) {
                    if(t_map.rows[i].htid == htid){
                        t_map.rows.splice(i,1);
                        preJson.splice(i,1);
                    }
                }
                if(t_map.rows.length==0){
                    $("#editInvoiceForm #gys").val(null);
                    $("#editInvoiceForm #gysmc").val(null);
                }
                var fpzje=0;
                for (var i = 0; i < t_map.rows.length; i++) {
                    fpzje=fpzje+(t_map.rows[i].hjje*1);
                }
                $("#editInvoiceForm #kpje").val(fpzje.toFixed(5));
                $("#editInvoiceForm #fpmx_json").val(JSON.stringify(preJson));
                $("#editInvoiceForm #tb_list").bootstrapTable('load',t_map);
            }
        }
    })
});
/**
 * 监听单据号标签点击事件
 */
var tagClick = $("#editInvoiceForm").on('click','.label-info',function(e){
    event.stopPropagation();
    var url = $('#editInvoiceForm #urlPrefix').val() + "/contract/contract/pagedataChooseContractInfoList?access_token=" + $("#ac_tk").val() + "&htid=" + e.currentTarget.dataset.logo;
    $.showDialog(url, '选择明细', chooseEditHtmxConfig);
});
var chooseEditHtmxConfig = {
    width : "1000px",
    modalName	: "chooseHtmxModal",
    formName	: "chooseContractForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#chooseContractInfoForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                if($("#chooseContractInfoForm input[name='checkHtmx']:checked").length == 0){
                    $.alert("未选中明细信息！");
                    return false;
                }
                // 获取未更改明细信息
                var preJson = [];
                if($("#editInvoiceForm #fpmx_json").val()){
                    preJson = JSON.parse($("#editInvoiceForm #fpmx_json").val());
                }
                // 获取更改请购单和明细信息
                var Json = [];
                $("#chooseContractInfoForm input[name='checkHtmx']").each(function(){
                    if($(this).is(":checked")){
                        var sz={"htid":'',"htmxid":''};
                        sz.htid = htid;
                        sz.htmxid = this.dataset.htmxid;
                        Json.push(sz);
                    }
                })
                // 获取删除请购明细
                var htid = $("#chooseContractInfoForm input[name='htid']").val();
                for (var j = preJson.length-1; j >=0; j--) {
                    if(htid == preJson[j].htid){
                        var isDel = true;
                        for (var i = 0; i < Json.length; i++) {
                            if(preJson[j].htmxid == Json[i].htmxid){
                                isDel = false;
                                break;
                            }
                        }
                        if(isDel){
                            // 根据明细ID直接更新页面列表(删除)
                            for (var i = 0; i < t_map.rows.length; i++) {
                                if(t_map.rows[i].htmxid == preJson[j].htmxid){
                                    preJson.splice(j,1);
                                    t_map.rows.splice(i,1);
                                    break;
                                }
                            }
                        }
                    }
                }
                // 获取新增请购明细
                var ids="";
                for (var i = 0; i < Json.length; i++) {
                    var isAdd = true;
                    for (var j = 0; j < preJson.length; j++) {
                        if(preJson[j].htmxid == Json[i].htmxid){
                            isAdd = false;
                            break;
                        }
                    }
                    if(isAdd){
                        ids = ids + ","+ Json[i].htmxid;
                    }
                }
                if(ids.length > 0){
                    ids = ids.substr(1);
                    // 查询信息并更新到页面
                    $.post($('#editInvoiceForm #urlPrefix').val() + "/contract/contract/pagedataGetContractChooseInfo",{ids:ids,"access_token":$("#ac_tk").val()},function(data){
                        var htmxDtos = data.htmxDtos;
                        if(htmxDtos != null && htmxDtos.length > 0){
                            // 更新页面列表(新增)
                            for (var i = 0; i < htmxDtos.length; i++) {
                                if(t_map.rows){
                                    htmxDtos[i].sl=(htmxDtos[i].sl*1).toFixed(2);
                                    htmxDtos[i].hjje=(htmxDtos[i].hjje*1).toFixed(2);
                                    t_map.rows.push(htmxDtos[i]);
                                }else{
                                    t_map.rows= [];
                                    htmxDtos[i].sl=(htmxDtos[i].sl*1).toFixed(2);
                                    htmxDtos[i].hjje=(htmxDtos[i].hjje*1).toFixed(2);
                                    t_map.rows.push(htmxDtos[i]);
                                }
                                var sz = {"htid":'',"htmxid":'',"sl":'',"hjje":'',"mxsl":'',"wwhsl":'',"qglbdm":'',"sbysid":'',"sbysdh":''};
                                sz.htid = htmxDtos[i].htid;
                                sz.htmxid = htmxDtos[i].htmxid;
                                sz.sl = htmxDtos[i].sl;
                                sz.mxsl = htmxDtos[i].mxsl;
                                sz.hjje = htmxDtos[i].hjje;
                                sz.wwhsl = htmxDtos[i].wwhsl;
                                sz.qglbdm = htmxDtos[i].qglbdm;
                                sz.sbysid = htmxDtos[i].sbysid;
                                sz.sbysdh = htmxDtos[i].sbysdh;
                                preJson.push(sz);
                            }
                            var fpzje=0;
                            for (var i = 0; i < t_map.rows.length; i++) {
                                fpzje=fpzje+(t_map.rows[i].hjje*1);
                            }
                            $("#editInvoiceForm #kpje").val(fpzje.toFixed(5));
                            $("#editInvoiceForm #tb_list").bootstrapTable('load',t_map);
                            $("#editInvoiceForm #fpmx_json").val(JSON.stringify(preJson));
                            $.closeModal(opts.modalName);
                        }
                    },'json');
                }else{
                    var fpzje=0;
                    for (var i = 0; i < t_map.rows.length; i++) {
                        fpzje=fpzje+(t_map.rows[i].hjje*1);
                    }
                    $("#editInvoiceForm #kpje").val(fpzje.toFixed(5));
                    $("#editInvoiceForm #tb_list").bootstrapTable('load',t_map);
                    $("#editInvoiceForm #fpmx_json").val(JSON.stringify(preJson));
                    $.closeModal(opts.modalName);
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

/**
 * 选择供应商列表
 * @returns
 */
function chooseGys(){
    var url=$('#editInvoiceForm #urlPrefix').val() + "/warehouse/supplier/pagedataSupplier?access_token=" + $("#ac_tk").val();
    $.showDialog(url,'选择供应商',addGysConfig);
}
var addGysConfig = {
    width		: "1200px",
    modalName	:"addGysModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var sel_row=$('#choosesupplier_formSearch #tb_list').bootstrapTable('getSelections');
                if(sel_row.length == 1){
                    var gysid = sel_row[0].gysid;
                    var gysmc = sel_row[0].gysmc;
                    $("#gysmc").val(gysmc);
                    $("#gys").val(gysid);
                }else{
                    $.error("请选中一行");
                    return false;
                }
            },
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};



/**
 * 选择负责人列表
 * @returns
 */
function chooseFzr(){
    var url=$('#editInvoiceForm #urlPrefix').val() + "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
    $.showDialog(url,'选择负责人',addFzrConfig);
}
var addFzrConfig = {
    width		: "800px",
    modalName	:"addFzrModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    $('#editInvoiceForm #ywy').val(sel_row[0].yhid);
                    $('#editInvoiceForm #ywymc').val(sel_row[0].zsxm);
                    //保存操作习惯
                    $.ajax({
                        type:'post',
                        url:$('#editInvoiceForm #urlPrefix').val() + "/common/habit/commonModUserHabit",
                        cache: false,
                        data: {"dxid":sel_row[0].yhid,"access_token":$("#ac_tk").val()},
                        dataType:'json',
                        success:function(data){}
                    });
                }else{
                    $.error("请选中一行");
                    return false;
                }
            },
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

/**
 * 选择明细
 * @returns
 */
function chooseHtxx(){
    var url = $('#editInvoiceForm #urlPrefix').val() + "/contract/contract/pagedataChooseContractList?access_token=" + $("#ac_tk").val()+"&zt=80";
    $.showDialog(url, '选择合同单号', chooseHtxxConfig);
}
var chooseHtxxConfig = {
    width : "1000px",
    modalName	: "chooseHtxxModal",
    formName	: "chooseContractForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#chooseContractForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                // 获取未更改请购单和明细信息
                var preJson = [];
                if(!$("#editInvoiceForm #gys").val()){
                    if($("#chooseContractForm #htmx_json").val()) {
                        htmxJson = JSON.parse($("#chooseContractForm #htmx_json").val());
                        $("#editInvoiceForm #gys").val(htmxJson[0].gysid);
                        $("#editInvoiceForm #gysmc").val(htmxJson[0].gysmc);
                    }
                }
                if($("#editInvoiceForm #fpmx_json").val()){
                    preJson = JSON.parse($("#editInvoiceForm #fpmx_json").val());
                }
                // 获取更改后的请购单和明细信息
                var htmxJson = [];
                if($("#chooseContractForm #htmx_json").val()){
                    htmxJson = JSON.parse($("#chooseContractForm #htmx_json").val());
                    // 判断新增的请购明细信息
                    var ids="";
                    for (var i = 0; i < htmxJson.length; i++) {
                        var isAdd = true;
                        for (var j = 0; j < preJson.length; j++) {
                            if(preJson[j].htmxid == htmxJson[i].htmxid){
                                isAdd = false;
                                break;
                            }
                        }
                        if(isAdd){
                            ids = ids + ","+ htmxJson[i].htmxid;
                        }
                    }
                    if(ids.length > 0){
                        ids = ids.substr(1);
                        // 查询信息并更新到页面
                        $.post($('#editInvoiceForm #urlPrefix').val() + "/contract/contract/pagedataGetContractChooseInfo",{"ids":ids,"access_token":$("#ac_tk").val()},function(data){
                            var htmxDtos = data.htmxDtos;
                            if(htmxDtos != null && htmxDtos.length > 0){
                                if(htmxDtos[0].biz!=null&&htmxDtos[0].biz!=''){
                                    var select = document.getElementById("biz");
                                    for (var i = 0; i < select.options.length; i++){
                                        if (select.options[i].id == htmxDtos[0].biz){
                                            $("#editInvoiceForm #biz").val(select.options[i].value);
                                            $("#editInvoiceForm #"+select.options[i].id).prop("selected","selected");
                                            $("#editInvoiceForm #"+select.options[i].id).trigger("chosen:updated");
                                            break;
                                        }
                                    }
                                }
                                var shuil="";
                                var hl="";
                                var slflag=true;
                                var hlflag=true;
                                // 更新页面列表(新增)
                                var fpzje=0;
                                for (var i = 0; i < htmxDtos.length; i++) {
                                    fpzje=fpzje+(htmxDtos[i].hjje*1);
                                    if(i == 0){
                                        if(!t_map.rows){
                                            t_map.rows= [];
                                        }
                                        shuil=shuil+htmxDtos[i].suil;
                                        hl=hl+htmxDtos[i].hl;
                                    }
                                    if(shuil.indexOf(htmxDtos[i].suil)==-1){
                                        slflag=false;
                                    }
                                    if(hl.indexOf(htmxDtos[i].hl)==-1){
                                        hlflag=false;
                                    }
                                    htmxDtos[i].sl=(htmxDtos[i].sl*1).toFixed(2);
                                    htmxDtos[i].hjje=(htmxDtos[i].hjje*1).toFixed(2);
                                    t_map.rows.push(htmxDtos[i]);
                                    var sz = {"htid":'',"htmxid":'',"sl":'',"hjje":'',"mxsl":'',"wwhsl":'',"qglbdm":'',"sbysid":'',"sbysdh":''};
                                    sz.htid = htmxDtos[i].htid;
                                    sz.htmxid = htmxDtos[i].htmxid;
                                    sz.sl = htmxDtos[i].sl;
                                    sz.mxsl = htmxDtos[i].mxsl;
                                    sz.hjje = htmxDtos[i].hjje;
                                    sz.wwhsl = htmxDtos[i].wwhsl;
                                    sz.qglbdm = htmxDtos[i].qglbdm;
                                    sz.sbysid = htmxDtos[i].sbysid;
                                    sz.sbysdh = htmxDtos[i].sbysdh;
                                    preJson.push(sz);
                                }
                                $("#editInvoiceForm #kpje").val(fpzje.toFixed(5));
                            }
                            $("#editInvoiceForm #tb_list").bootstrapTable('load',t_map);
                            $("#editInvoiceForm #fpmx_json").val(JSON.stringify(preJson));
                            if(!$("#editInvoiceForm #sl").val()){
                                var select = document.getElementById("fpzl");
                                if(slflag&&shuil!='null'){
                                    $("#editInvoiceForm #sl").val(shuil);
                                        for (var i = 0; i < select.options.length; i++){
                                            if (select.options[i].id == (shuil*1).toString()){
                                                $("#editInvoiceForm #fpzl").val(select.options[i].value);
                                                $("#editInvoiceForm #"+select.options[i].id).prop("selected","selected");
                                                $("#editInvoiceForm #"+select.options[i].id).trigger("chosen:updated");
                                                break;
                                            }
                                        }
                                }else{
                                    for (var i = 0; i < select.options.length; i++){
                                        if (select.options[i].id == '0'){
                                            $("#editInvoiceForm #fpzl").val(select.options[i].value);
                                            $("#editInvoiceForm #"+select.options[i].id).prop("selected","selected");
                                            $("#editInvoiceForm #"+select.options[i].id).trigger("chosen:updated");
                                            break;
                                        }
                                    }
                                }
                            }
                            // if(!$("#editInvoiceForm #hl").val()){
                            //     if(hlflag&&hl!='null'){
                            //         $("#editInvoiceForm #hl").val(hl);
                            //     }
                            // }
                            var selDjh = $("#chooseContractForm #yxdjh").tagsinput('items');
                            $("#editInvoiceForm #htid").tagsinput({
                                itemValue: "htid",
                                itemText: "htnbbh",
                            });
                            for(var i = 0; i < selDjh.length; i++){
                                var value = selDjh[i].value;
                                var text = selDjh[i].text;
                                $("#editInvoiceForm #htid").tagsinput('add', {"htid":value,"htnbbh":text});
                            }
                            $.closeModal(opts.modalName);
                        },'json');
                    }else{
                        $("#editInvoiceForm #tb_list").bootstrapTable('load',t_map);
                        $("#editInvoiceForm #fpmx_json").val(JSON.stringify(preJson));
                        // 请购单号
                        var selDjh = $("#chooseContractForm #yxdjh").tagsinput('items');
                        $("#editInvoiceForm #htid").tagsinput({
                            itemValue: "htid",
                            itemText: "htnbbh",
                        })
                        for(var i = 0; i < selDjh.length; i++){
                            var value = selDjh[i].value;
                            var text = selDjh[i].text;
                            $("#editInvoiceForm #htid").tagsinput('add', {"htid":value,"htnbbh":text});
                        }
                        $.closeModal(opts.modalName);
                    }
                }else{
                    $.alert("未获取到选中的信息！");
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

$("#keydown").bind("keydown",function(e){
    var evt = window.event || e;
    if (evt.keyCode == 13) {
        smqsm()
    }
});

function smqsm() {
//回车执行查询
    var str =  document.getElementById("fph").value;
    var strList = str.split(",");
    if (strList.length > 5){
        document.getElementById("fph").value=strList[3];
        document.getElementById("kpje").value=strList[4];
        document.getElementById("kprq").value=strList[5];
        var select = document.getElementById("fpzl");
        if (strList[1] == "01"){
            for (var i = 0; i < select.options.length; i++){
                //专用发票
                if (select.options[i].id == "01"){
                    $("#editInvoiceForm #fpzl").val(select.options[i].value);
                    $("#editInvoiceForm #"+select.options[i].id).prop("selected","selected");
                    $("#editInvoiceForm #"+select.options[i].id).trigger("chosen:updated");
                    break;
                }
            }
        }
        if (strList[1] == "10") {
            for (var i = 0; i < select.options.length; i++) {
                //普通发票
                if (select.options[i].id == "10") {
                    $("#editInvoiceForm #fpzl").val(select.options[i].value);
                    $("#editInvoiceForm #" + select.options[i].id).prop("selected", "selected");
                    $("#editInvoiceForm #" + select.options[i].id).trigger("chosen:updated");
                    break;
                }
            }
        }
        if (strList[1] == "04"){
            for (var i = 0; i < select.options.length; i++){
                //专用发票
                if (select.options[i].id == "04"){
                    $("#editInvoiceForm #fpzl").val(select.options[i].value);
                    $("#editInvoiceForm #"+select.options[i].id).prop("selected","selected");
                    $("#editInvoiceForm #"+select.options[i].id).trigger("chosen:updated");
                    break;
                }
            }
        }
        var num =  document.getElementById("kprq").value;
        var reg = /(\d{4})/;
        var numlist = num.split(reg);
        if (numlist.length < 4){
            $.error("开票日期有误！");
            return false;
        }
        var year = numlist[1];
        var time = numlist[3];
        var reg2 =  /(\d{2})/;
        var timelist = time.split(reg2);
        if (timelist.length < 4){
            $.error("开票日期有误！");
            return false;
        }
        var month = timelist[1];
        var day = timelist[3];
        document.getElementById("kprq").value = year+"-"+month+"-"+day;
    }
}

$(document).ready(function(){
    //初始化列表
    var oTable=new invoiceEdit_TableInit();
    oTable.Init();
    init();
    //所有下拉框添加choose样式
    jQuery('#editInvoiceForm .chosen-select').chosen({width: '100%'});
});