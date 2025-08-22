var t_map = [];
var ywid=$('#MoneyHosForm #ywid').val();
var ywlx = $('#MoneyHosForm #ywlx').val();
var flag = $('#MoneyHosForm #flag').val();
var fywid = $('#MoneyHosForm #fywid').val();
var dysyje = Number($('#MoneyHosForm #dysyje').val());
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
        field: 'dkjlid',
        title: '',
        titleTooltip:'',
        width: '10%',
        align: 'left',
        visible: false
    }, {
        field: 'dky',
        title: '到款月',
        titleTooltip:'到款月',
        width: '10%',
        align: 'left',
        visible: true
    }, {
        field: 'dkje',
        title: '到款金额',
        titleTooltip:'到款金额',
        width: '12%',
        align: 'left',
        visible: true
    }, {
        field: 'lb',
        title: '类别',
        titleTooltip:'类别',
        width: '5%',
        align: 'left',
        visible: true,
        formatter:lbformat,
    }, {
        field: 'djrq',
        title: '发货日',
        titleTooltip:'发货日',
        width: '10%',
        align: 'left',
        visible: ywlx!='0',
    }, {
        field: 'lrsj',
        title: '录入时间',
        titleTooltip:'录入时间',
        width: '10%',
        align: 'left',
        visible: true
    }, {
        field: '',
        title: '状态',
        titleTooltip:'状态',
        width: '5%',
        align: 'left',
        visible: true,
        formatter:ztformat,
    },{
        field: 'cz',
        title: '操作',
        titleTooltip:'操作',
        width: '5%',
        align: 'left',
        formatter:czformat,
        visible: flag=='dkwh'
    }];
var MoneyHosForm_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#MoneyHosForm #tb_list').bootstrapTable({
            url:$('#MoneyHosForm #urlPrefix').val()+'/storehouse/sale/pagedataGetMoneyHos',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#MoneyHosForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "lrsj",				        //排序字段
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
            uniqueId: "dkjlid",                     //每一行的唯一标识，一般为主键列
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
            sortLastName: "", //防止同名排位用
            sortLastOrder: "", //防止同名排位用
            ywid:ywid,
            fywid:fywid,
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};
function lbformat(value,row,index){
    if(row.lb=='0'){
        return '手动录入';
    }else if(row.lb=='1'){
        return '手动对应';
    }else if(row.lb=='2'){
        return '自动对应';
    }else{
        return '';
    }
}
function ztformat(value,row,index){
    if (row.ywlx=='0'){
        return "<span style='color:green;'>"+"正常"+"</span>";
    }
    if (row.lb=='1'||row.lb=='2'){
        return "<span style='color:green;'>"+"正常"+"</span>";
    }
    var hkzq = $('#MoneyHosForm #hkzq').val();
    if (hkzq){
        hkzq = Number(hkzq)
    }else{
        hkzq = 0;
    }
    var date1 = new Date(row.djrq);
    var date2 = new Date(row.dky);

    // 将日期转换为 UTC 时间，以避免时区偏移导致的计算错误
    var utc1 = Date.UTC(date1.getFullYear(), date1.getMonth(), date1.getDate());
    var utc2 = Date.UTC(date2.getFullYear(), date2.getMonth(), date2.getDate());

    // 计算天数差异
    var diffDays = Math.floor((utc2 - utc1) / (1000 * 60 * 60 * 24));

    // 将天数差异转换为中国时间
    var diffDaysInChina = diffDays + (date1.getTimezoneOffset() - date2.getTimezoneOffset()) / (60 * 60 * 1000);

    if (diffDaysInChina<=hkzq){
        return "<span style='color:green;'>"+"正常"+"</span>";
    }else if (hkzq<diffDaysInChina&&diffDaysInChina<=(hkzq+180)){
        return "<span style='color:yellow;'>"+"逾期"+"</span>";
    }else if (diffDaysInChina>(hkzq+180)&&diffDaysInChina<=(hkzq+360)){
        return "<span style='color:orange;'>"+"呆账"+"</span>";
    }else if (diffDaysInChina>(hkzq+360)){
        return "<span style='color:red;'>"+"坏账"+"</span>";
    }
}
/**
 * 验证格式(两位小数)
 * @param value
 * @param element
 * @returns
 */
function checknum(name) {
    var value = $("#" + name).val();
    if (value){
        var result = /^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
        if (!result.test(value)) {
            $.error("请填写正确格式，可保留两位小数!");
            $("#" + name).val("");
        }
    }
}
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
function delDetail(index,event){
    var row = t_map.rows[index]
    if (ywlx=='0'){
        if (Number(row.dkje)>dysyje){
            $.error("对应剩余金额不足，不允许删除！");
            return false;
        }
    }
    if (lb=='1'){
        dysyje = dysyje+Number(row.dkje);
    }
    t_map.rows.splice(index,1);
    $("#MoneyHosForm #tb_list").bootstrapTable('load',t_map);
}
function cofirm(){

    var dky = $('#MoneyHosForm #dky').val();
    if (dky==null||dky==''){
        $.error("请输入到款月");
        return false;
    }
    var dkje = $('#MoneyHosForm #dkje').val();
    if (dkje==null||dkje==''){
        $.error("请输入到款金额");
        return false;
    }
    var lb = $('#MoneyHosForm #lb').val()
    if (lb==null||lb==''){
        $.error("请选择类别");
        return false;
    }
    if (lb=='1'){
        if (Number(dkje)>dysyje){
            $.error("手动对应的到款金额不能超出对应剩余金额，请修改到款金额！");
            return false;
        }
        dysyje = dysyje-Number(dkje);
    }
    var lrsj=timestampToTime(Date.parse(new Date()));
    var sz = {"dkjlid":'',"dky":'',"dkje":'',"fywid":'',"ywid":'',"lb":'',"djrq":'',"ywlx":''};
    sz.dky = dky;
    sz.dkje = dkje;
    sz.ywid = ywid;
    sz.fywid = fywid;
    sz.lrsj = lrsj;
    sz.ywlx = ywlx;
    sz.lb =  lb
    sz.djrq =  $('#MoneyHosForm #djrq').val()
    t_map.rows.push(sz)
    $('#MoneyHosForm #dky').val('');
    $('#MoneyHosForm #dkje').val('');
    $("#MoneyHosForm #tb_list").bootstrapTable('load',t_map);
}
function timestampToTime(timestamp) {
    var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
    var Y = date.getFullYear() + '-';
    var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1):date.getMonth()+1) + '-';
    var D = (date.getDate()< 10 ? '0'+date.getDate():date.getDate())+ ' ';
    var h = (date.getHours() < 10 ? '0'+date.getHours():date.getHours())+ ':';
    var m = (date.getMinutes() < 10 ? '0'+date.getMinutes():date.getMinutes()) + ':';
    var s = date.getSeconds() < 10 ? '0'+date.getSeconds():date.getSeconds();
    return Y+M+D+h+m+s;
}
$(document).ready(function(){
    //初始化列表
    var oTable=new MoneyHosForm_TableInit();
    oTable.Init();
    //添加日期控件
    laydate.render({
        elem: '#MoneyHosForm #dky'
        ,theme: '#2381E9'
        , format: 'yyyy-MM-dd'
    });
    //如果为销售
    if (ywlx=='0'){
        $('#MoneyHosForm #lb').val('0')
        $('#MoneyHosForm #lb').attr("disabled", true);
        $('#MoneyHosForm #lb').trigger("chosen:updated");
    }
    //所有下拉框添加choose样式
    jQuery('#MoneyHosForm .chosen-select').chosen({width: '100%'});
});