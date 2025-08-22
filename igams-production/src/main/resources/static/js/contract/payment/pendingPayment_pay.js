//显示字段
var t_map = [];
var columnsArray = [
    {
        title: '序',
        formatter: function (value, row, index) {
            return index+1;
        },
        titleTooltip:'序',
        width: '2%',
        align: 'left',
        visible:true
    },{
        field: 'htnbbh',
        title: '合同编号',
        titleTooltip:'合同编号',
        width: '8%',
        align: 'left',
        formatter:pendingPayment_htbhFormat,
        visible: true
    },{
        field: 'zje',
        title: '合同金额',
        titleTooltip:'合同金额',
        width: '5%',
        align: 'left',
        formatter:pendingPayment_zjeFormat,
        visible: true
    },{
        field: 'htyfje',
        title: '已付金额',
        titleTooltip:'已付金额',
        width: '5%',
        align: 'left',
        visible: true
    },{
        field: 'wfkje',
        title: '未付金额',
        titleTooltip:'未付金额',
        width: '5%',
        align: 'left',
        visible: true
    },{
        field: 'fkzje',
        title: '付款中金额',
        titleTooltip:'付款中金额',
        width: '5%',
        align: 'left',
        visible: true
    },{
        field: 'yfje',
        title: '预付金额',
        titleTooltip:'预付金额',
        width: '5%',
        align: 'left',
        visible: false
    },{
        field: 'fkje',
        title: '*付款金额',
        titleTooltip:'付款金额',
        width: '5%',
        formatter:pendingPayment_fkjeFormat,
        align: 'left',
        visible: true
    },{
        field: 'yfbl',
        title: '预付比例',
        titleTooltip:'预付比例',
        width: '5%',
        align: 'left',
        formatter:pendingPayment_yfblFormat,
        visible: true
     }];

var pendingPaymentin_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#pendingPaymentForm #htfk_list').bootstrapTable({
            url: $('#pendingPaymentForm #urlPrefix').val()+'/contract/pendingPayment/pagedataGetNeedPayList?ids='+$("#pendingPaymentForm #fktxids").val()+'&htids='+$("#pendingPaymentForm #htids").val(),         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#pendingPaymentForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "htnbbh",				//排序字段
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
            isForceTable: true,
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: false,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "fktxid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
            onLoadSuccess: function (map) {
                t_map = map;

                var data=$('#pendingPaymentForm #htfk_list').bootstrapTable('getData');
                //刷新页面总金额
                var zje=0;
                for(var i=0;i<data.length;i++){
                    if(data[i].fkje!=null && data[i].fkje!=''){
                        zje=parseInt(zje)+parseInt(data[i].fkje*100000);
                    }
                }
                zje=(zje/100000).toFixed(2);
                $("#pendingPaymentForm #fkzje").val(zje);
                $("#pendingPaymentForm #ykbm").val(t_map.rows[0].ykbm);
                $("#pendingPaymentForm #ykbmmc").val(t_map.rows[0].ykbmmc);
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
            sortLastName: "qgmxid", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            qgmxid: $("#pendingPaymentForm #fktxid").val(),
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};

function pendingPayment_zjeFormat(value,row,index){
    var zje = row.zje;
    if(row.xzje!=null && row.xzje!=''){
        zje=row.xzje;
    }
    return zje;
}

function pendingPayment_yfblFormat(value,row,index){
        var html = '';
        var htzje = row.zje;
        if(row.xzje!=null && row.xzje!=''){
            htzje = row.xzje;
        }
        var fkje = row.fkje;
        fkjeNum = parseFloat(fkje);
        htzjeNum = parseFloat(htzje);
        result = parseFloat(Math.round(fkjeNum/htzjeNum*10000)/100);
        var bfb = result+'%';
        row.fkbfb=bfb;
        html = "<span id='yfbl_"+index+"'>"+bfb+"</span>";
        return html;
}

/**
 * 合同详情格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function pendingPayment_htbhFormat(value,row,index){
    var html = "";
    if(row.htnbbh!=null){
        html += "<a href='javascript:void(0);' onclick=\"queryByhtid('"+row.htid+"')\">"+row.htnbbh+"</a>";
    }else{
        html +="-"
    }
    return html;
}
function queryByhtid(htid){
    var url=$("#pendingPaymentForm #urlPrefix").val()+"/contract/contract/pagedataViewContract?htid="+htid+"&access_token=" + $("#ac_tk").val();
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
 * 付款金额格式化
 * @param value
 * @param row
 * @param index
 * @returns {string}
 */
function pendingPayment_fkjeFormat(value,row,index){
    //设置付款金额默认等于预付金额
    row.fkje=row.yfje;
    row.jg=row.yfje;
    if(row.fkje == null){
        row.fkje = "";
        row.jg="";
    }
    var html = "<input id='fkje_"+index+"' value='"+row.fkje+"' name='fkje_"+index+"' style='width:100%;border-top-left-radius:5px;border-bottom-left-radius:5px;border:1px solid #D5D5D5;'  onblur=\"checkPaymentSum('"+index+"',this,\'fkje\')\"></input>";
    return html;
}

function checkPaymentSum(index,data,zdmc){
    var data=$('#pendingPaymentForm #htfk_list').bootstrapTable('getData');
    data[index].fkje=$("#pendingPaymentForm #fkje_"+index).val();
    if(zdmc=='fkje'){
        //刷新页面总金额
        var zje=0;
        for(var i=0;i<data.length;i++){
            if(data[i].fkje!=null && data[i].fkje!=''){
                zje=parseInt(zje)+parseInt(data[i].fkje*100000);
            }
        }
        zje=(zje/100000).toFixed(2);
        $("#pendingPaymentForm #fkzje").val(zje);

        var fkje = $("#pendingPaymentForm #fkje_"+index).val();
        var htzje = data[index].zje;
        if(data[index].xzje!=null && data[index].xzje!=''){
            htzje = data[index].xzje;
        }
        fkjeNum = parseFloat(fkje);
        htzjeNum = parseFloat(htzje);
        result = parseFloat(Math.round(fkjeNum/htzjeNum*10000)/100);
        bfbbj = parseFloat("100");
        fbfbbj = parseFloat("-100");
        var bfb =result;
        document.getElementById("yfbl_"+index).innerText = bfb+'%';
        data[index].fkbfb=bfb;
    }
}

/**
 * 验证数量格式(五位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("fkjeNumber", function (value, element){
    if(value && !/^\d+(\.\d{1,2})?$/.test(value)){
        $.error("请填写正确价格格式，可保留两位小数!");
    }
    return this.optional(element) || /^\d+(\.\d{1,2})?$/.test(value);
},"请填写正确格式，可保留两位小数。");


/**
 * 选择负责人列表
 * @returns
 */
function chooseFzr(){
    var url=$('#pendingPaymentForm #urlPrefix').val() + "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
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
                    $('#pendingPaymentForm #sqr').val(sel_row[0].yhid);
                    $('#pendingPaymentForm #sqrmc').val(sel_row[0].zsxm);
                    //保存操作习惯
                    $.ajax({
                        type:'post',
                        url:$('#pendingPaymentForm #urlPrefix').val() + "/common/habit/commonModUserHabit",
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
 * 选择申请部门列表
 * @returns
 */
function chooseSqbm(){
    var url=$('#pendingPaymentForm #urlPrefix').val() + "/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
    $.showDialog(url,'选择申请部门',addSqbmConfig);
}
var addSqbmConfig = {
    width		: "800px",
    modalName	:"addSqbmModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var sel_row = $('#listDepartmentForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    $('#pendingPaymentForm #sqbm').val(sel_row[0].jgid);
                    $('#pendingPaymentForm #sqbmmc').val(sel_row[0].jgmc);
                    $('#pendingPaymentForm #jgdm').val(sel_row[0].jgdm);
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
 * 选择用款部门列表
 * @returns
 */
function chooseDfkYkbm(){
    var url=$('#pendingPaymentForm #urlPrefix').val() + "/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
    $.showDialog(url,'选择用款部门',addYkbmConfig);
}
var addYkbmConfig = {
    width		: "800px",
    modalName	:"addYkbmModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var sel_row = $('#listDepartmentForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    $('#pendingPaymentForm #ykbm').val(sel_row[0].jgid);
                    $('#pendingPaymentForm #ykbmmc').val(sel_row[0].jgmc);
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


$(document).ready(function(){
    // 1.初始化Table
    var oTable = new pendingPaymentin_TableInit();
    oTable.Init();

    //添加日期控件
    laydate.render({
        elem: '#pendingPaymentForm #zwzfrq'
        ,theme: '#2381E9'
    });
    //添加日期控件
    laydate.render({
        elem: '#pendingPaymentForm #fkrq'
        ,theme: '#2381E9'
        ,value:new Date()
    });
    //所有下拉框添加choose样式
    jQuery('#pendingPaymentForm .chosen-select').chosen({width: '100%'});
});