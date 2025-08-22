var t_map = [];
var flag = $('#logisticsUpholdForm #flag').val();
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
        field: 'kdgs',
        title: '快递公司',
        titleTooltip:'快递公司',
        width: '14%',
        align: 'left',
        visible: true
    }, {
        field: 'wldh',
        title: '快递单号',
        titleTooltip:'快递单号',
        width: '14%',
        align: 'left',
        visible: true
    }, {
        field: 'fhrq',
        title: '发货日期',
        titleTooltip:'发货日期',
        width: '9%',
        align: 'left',
        formatter:fhrqformat,
        visible: true
    }, {
        field: 'yf',
        title: '运费(元)',
        titleTooltip:'运费(元)',
        width: '5%',
        align: 'left',
        formatter:yfformat,
        visible:  true
    }, {
        field: 'qsrq',
        title: '签收时间',
        titleTooltip:'签收时间',
        width: '12%',
        align: 'left',
        formatter:qsrqformat,
        visible: true
    }, {
        field: 'sfqr',
        title: '是否确认',
        titleTooltip:'是否确认',
        width: '6%',
        align: 'left',
        formatter:sfqrformat,
        visible: flag=='wlqs'?false:true
    },{
        field: 'fjids',
        title: '附件',
        width: '3%',
        align: 'left',
        formatter:fjformat,
    },{
        field: 'cz',
        title: '操作',
        titleTooltip:'操作',
        width: '5%',
        align: 'left',
        formatter:czformat,
        visible: flag=='wlwh'?true:false
    }];
var logisticsUpholdForm_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#logisticsUpholdForm #tb_list').bootstrapTable({
            url:$('#logisticsUpholdForm #urlPrefix').val()+'/storehouse/requisition/pagedataGetWlxxByYwid?ywid='+$('#logisticsUpholdForm #ywid').val(),         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#logisticsUpholdForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "wlxx.lrsj",				        //排序字段
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
            uniqueId: "wlxxid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
            onLoadSuccess: function (map) {
                t_map = map;
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
            sortLastName: "wldh", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};
/**
 * 运费格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function yfformat(value,row,index){
    if(row.yf == null){
        row.yf = "";
    }
    if (flag=="wlwh"){
        var html = "<input id='yf_"+index+"' value='"+row.yf+"' name='yf_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' validate='{yfNumber:true}' onblur=\"checkSum('"+index+"',this,\'yf\')\"/>";
        return html;
    }else {
        return  row.yf;
    }
}
/**
 * 验证运费格式(2位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("yfNumber", function (value, element){
    if(value && !/^\d+(\.\d{1,2})?$/.test(value)){
        $.error("请填写正确运费格式，可保留两位小数!");
    }
    return this.optional(element) || /^\d+(\.\d{1,2})?$/.test(value);
},"请填写正确格式，可保留两位小数。");
/**
 * 附件格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function fjformat(value,row,index){
    var html = "<div id='fj_"+index+"'>";
    html += "<input type='hidden' id='wlfj_"+index+"' name='wlfj_"+index+"'/>";
    if(row.fjbj == "0"){
        html += "<a href='javascript:void(0);' onclick='uploadLogisFile(\"" + index + "\",\""+row.wlxxid+"\")' >是</a>";
    }else{
        html += "<a href='javascript:void(0);' onclick='uploadLogisFile(\"" + index + "\",\""+row.wlxxid+"\")' >否</a>";
    }
    html += "</div>";
    return html;
}
/**
 * 货物质量检验上传附件
 * @param index
 * @returns
 */
var currentUploadIndex = "";
function uploadLogisFile(index,wlxxid) {
    if(index){
        var ywid = $("#logisticsUpholdForm #ywid").val();
        var fjids = $("#logisticsUpholdForm #fj_"+ index +" input").val();
        var flag=$("#logisticsUpholdForm #flag").val();
        var ckbj="";
        if (flag =="wlqs"||flag =="wlqsqr")
            ckbj = "1";
        url=$('#logisticsUpholdForm #urlPrefix').val()+"/inspectionGoods/pendingInspection/pagedataGetUploadFilePage?access_token=" + $("#ac_tk").val()+"&ywid="+ywid+"&zywid="+wlxxid+"&ckbj="+ckbj;
        if(fjids){
            url=url + "&fjids="+fjids;
        }
        currentUploadIndex = index;
    }
    $.showDialog(url, '修改附件', uploadFileConfig);
}
var uploadFileConfig = {
    width : "800px",
    height : "500px",
    modalName	: "uploadFileModal",
    formName	: "logisticsUpholdForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var $this = this;
                var opts = $this["options"]||{};
                if($("#ajaxForm #fjids").val()){
                    $("#logisticsUpholdForm #fj_"+currentUploadIndex+" input").val($("#ajaxForm #fjids").val());
                    $("#logisticsUpholdForm #fj_"+currentUploadIndex+" a").text("是");
                    $('#logisticsUpholdForm #tb_list').bootstrapTable('getData')[currentUploadIndex].fjbj="0";
                }else {
                    //判断正式文件是否存在
                    var dom = $("#ajaxForm div[name='formalFile']").html();
                    $("#logisticsUpholdForm #fj_"+currentUploadIndex+" input").val($("#ajaxForm #fjids").val());
                    if ((dom == null || dom == "" || dom == undefined)) {
                            $("#logisticsUpholdForm #fj_" + currentUploadIndex + " input").val("");
                            $("#logisticsUpholdForm #fj_" + currentUploadIndex + " a").text("否");
                            $('#logisticsUpholdForm #tb_list').bootstrapTable('getData')[currentUploadIndex].fjbj = "1";
                        }
                }
                $.closeModal(opts.modalName);
                return true;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function czformat(value,row,index){
    var html = "<span style='margin-left:5px;height:24px !important;' class='btn btn-danger' title='删除明细' onclick=\"delDetail('" + index + "',event)\" >删除</span>";
    return html;
}
/**
 * 签收时间格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function qsrqformat(value,row,index){
    if(row.qsrq == null){
        row.qsrq = "";
    }
    if (flag=='wlqs'){
        var html = "<input id='qsrq_"+index+"' value='"+row.qsrq+"' name='qsrq_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onchange=\"checkSum('"+index+"',this,\'qsrq\')\"/>";
        setTimeout(function() {
            laydate.render({
                elem: '#logisticsUpholdForm  #qsrq_'+index
                ,type: 'datetime'
                ,ready: function(date){
                    var myDate = new Date(); //实例一个时间对象；
                    this.dateTime.hours=myDate.getHours();
                    this.dateTime.minutes=myDate.getMinutes();
                    this.dateTime.seconds=myDate.getSeconds();
                }
                ,theme: '#2381E9'
                ,btns: ['clear', 'confirm']
                ,done: function(value, date, endDate){
                    t_map.rows[index].qsrq = value;
                }
            });
        }, 100);
        return html;
    }else {
        return row.qsrq;
    }
}
/**
 * 发货日期格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function fhrqformat(value,row,index){
    if(row.fhrq == null){
        row.fhrq = "";
    }
    if (flag=='wlwh'){
        var html = "<input id='fhrq_"+index+"' value='"+row.fhrq+"' name='fhrq_"+index+"' style='width:85%;border-radius:5px;border:1px solid #D5D5D5;' onchange=\"checkSum('"+index+"',this,\'fhrq\')\"/>";
        setTimeout(function() {
            laydate.render({
                elem: '#logisticsUpholdForm  #fhrq_'+index
                ,type: 'date'
                ,ready: function(date){
                }
                ,theme: '#2381E9'
                ,btns: ['clear', 'confirm']
                ,done: function(value, date, endDate){
                    t_map.rows[index].fhrq = value;
                }
            });
        }, 100);
        return html;
    }else {
        return row.fhrq;
    }
}
function checkSum(index, e, zdmc) {
    if(t_map.rows.length > index){
        if (zdmc == 'qsrq') {
            t_map.rows[index].qsrq = e.value;
        }else if (zdmc == 'sfqr'){
            t_map.rows[index].sfqr = e.value;
        }else if (zdmc == 'fhrq'){
            t_map.rows[index].fhrq = e.value;
        }else if (zdmc == 'yf'){
            t_map.rows[index].yf = e.value;
        }
    }
}
function delDetail(index,event){
    t_map.rows.splice(index,1);
    $("#logisticsUpholdForm #tb_list").bootstrapTable('load',t_map);
}
function sfqrformat(value,row,index) {
    if (flag=="wlqsqr"){
        var html="";
        html +=  "<select id='sfqr_"+index+"' name='sfqr_"+index+"' class='form-control chosen-select'  onchange=\"checkSum('"+index+"',this,\'sfqr\')\">";
        if(row.sfqr=='1'){
            html += "<option value='1' selected>是</option>";
            html += "<option value='0'>否</option>";
        }else if(row.sfqr=='0'){
            html += "<option value='1' >是</option>";
            html += "<option value='0' selected>否</option>";
        }
        html += "</select>";
        return html;
    }else {
        if (row.sfqr == '1') {
            return "是";
        }else if (row.sfqr == '0') {
            return "否";
        }else {
            return "";
        }
    }

}
function cofirm(){
    var kdgs = $('#logisticsUpholdForm #kdgs').val();
    if (kdgs==null||kdgs==''){
        if (kdgs.length>24){
            $.error("快递公司字符长度超出");
            return false;
        }
        $.error("请输入快递公司");
        return false;
    }
    var wldh = $('#logisticsUpholdForm #wldh').val();
    if (wldh==null||wldh==''){
        if (wldh.length>64){
            $.error("快递单号字符长度超出");
            return false;
        }
        $.error("请输入快递单号");
        return false;
    }
    var sz = {"kdgs":'',"wldh":''};
    sz.kdgs = kdgs;
    sz.wldh = wldh;
    t_map.rows.push(sz)
    $('#logisticsUpholdForm #kdgs').val('');
    $('#logisticsUpholdForm #wldh').val('');
    $("#logisticsUpholdForm #tb_list").bootstrapTable('load',t_map);
}
$(document).ready(function(){
    //初始化列表
    var oTable=new logisticsUpholdForm_TableInit();
    oTable.Init();
    //所有下拉框添加choose样式
    jQuery('#logisticsUpholdForm .chosen-select').chosen({width: '100%'});
});