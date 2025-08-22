
var before_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#amountPayment_formSearch #before_list").bootstrapTable({
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   // 是否显示分页（*）
            paginationShowPageGo: false,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            clickToSelect: true,                // 是否启用点击选中行
            showToggle: true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            strictSearch: true,
            showColumns: true,                  //是否显示所有的列
            // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "xmglid",                     // 每一行的唯一标识，一般为主键列
            columns: [{
                field: 'csmc',
                title: '项目',
                width: '16%',
                align: 'left',
                titleTooltip:'项目',
                visible: true
            },{
                field: 'zmc',
                title: '子项目',
                width: '16%',
                align: 'left',
                titleTooltip:'子项目',
                visible:true
            },{
                field: 'sfsf',
                title: '收费',
                width: '6%',
                align: 'left',
                titleTooltip:'收费',
                visible:false
            },{
                field: 'sfje',
                title: '实付金额',
                width: '8%',
                align: 'left',
                titleTooltip:'实付金额',
                visible:true
            },{
                field: 'fkrq',
                title: '付款日期',
                width: '10%',
                align: 'left',
                titleTooltip:'付款日期',
                visible:true
            },{
                field: 'tkje',
                title: '退款金额',
                width: '8%',
                align: 'left',
                titleTooltip:'退款金额',
                visible:true
            },{
                field: 'tkrq',
                title: '退款日期',
                width: '10%',
                align: 'left',
                titleTooltip:'退款日期',
                visible:true
            },{
                field: 'tkfs',
                title: '退款方式',
                width: '8%',
                align: 'left',
                titleTooltip:'退款方式',
                visible:false
            },{
                field: 'yfje',
                title: '应付金额',
                width: '8%',
                align: 'left',
                titleTooltip:'应付金额',
                visible:false
            },{
                field: 'sfdz',
                title: '对账',
                width: '6%',
                align: 'left',
                titleTooltip:'对账',
                visible:false
            },{
                field: 'dzje',
                title: '对账金额',
                width: '8%',
                align: 'left',
                titleTooltip:'对账金额',
                visible:false
            }],
            onLoadSuccess:function(map){
            },
            onLoadError:function(){
            }

        });
        $("#amountPayment_formSearch #before_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true}
        );
    };
    return oTableInit;
}
var beforeList = [];
var laterList = [];

$(function(){
    if ($("#amountPayment_formSearch #beforeList").val())
        beforeList = JSON.parse($("#amountPayment_formSearch #beforeList").val());
    if ($("#amountPayment_formSearch #laterList").val())
        laterList = JSON.parse($("#amountPayment_formSearch #laterList").val());
    var before_oTable = new before_TableInit();
    before_oTable.Init();
    var later_oTable = new later_TableInit();
    later_oTable.Init();
    $("#amountPayment_formSearch #later_list").bootstrapTable('load',laterList);
    $("#amountPayment_formSearch #before_list").bootstrapTable('load',beforeList);
    $("#amountPayment_formSearch   [name='toggle']").each(function(){
        $(this).click()
    });
    $("#amountPayment_formSearch [class='columns columns-right btn-group float-right']").each(function(){  $(this).remove()});
})



var later_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#amountPayment_formSearch #later_list").bootstrapTable({
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   // 是否显示分页（*）
            paginationShowPageGo: false,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            clickToSelect: true,                // 是否启用点击选中行
            showToggle: true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            strictSearch: true,
            showColumns: true,                  //是否显示所有的列
            // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "xmglid",                     // 每一行的唯一标识，一般为主键列
            search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            columns: [{
                field: 'csmc',
                title: '项目',
                width: '16%',
                align: 'left',
                titleTooltip:'项目',
                visible: true
            },{
                field: 'zmc',
                title: '子项目',
                width: '16%',
                align: 'left',
                titleTooltip:'子项目',
                visible:true
            },{
                field: 'sfsf',
                title: '收费',
                width: '6%',
                align: 'left',
                titleTooltip:'收费',
                visible:false
            },{
                field: 'sfje',
                title: '实付金额',
                width: '8%',
                align: 'left',
                titleTooltip:'实付金额',
                formatter:sfjeformat,
                visible:true
            },{
                field: 'fkrq',
                title: '付款日期',
                width: '10%',
                align: 'left',
                formatter:fkrqformat,
                titleTooltip:'付款日期',
                visible:true
            },{
                field: 'tkje',
                title: '退款金额',
                width: '8%',
                align: 'left',
                formatter:tkjeformat,
                titleTooltip:'退款金额',
                visible:true
            },{
                field: 'tkrq',
                title: '退款日期',
                width: '10%',
                align: 'left',
                formatter:tkrqformat,
                titleTooltip:'退款日期',
                visible:true
            },{
                field: 'tkfs',
                title: '退款方式',
                width: '8%',
                align: 'left',
                titleTooltip:'退款方式',
                visible:false

            },{
                field: 'yfje',
                title: '应付金额',
                width: '8%',
                align: 'left',
                formatter:yfjeformat,
                titleTooltip:'应付金额',
                visible:false
            },{
                field: 'sfdz',
                title: '对账',
                width: '6%',
                align: 'left',
                titleTooltip:'对账',
                visible:false
            },{
                field: 'dzje',
                title: '对账金额',
                width: '8%',
                formatter:dzjeformat,
                align: 'left',
                titleTooltip:'对账金额',
                visible:false
            }],
            onLoadSuccess:function(map){

            },
            onLoadError:function(){
                alert("数据加载失败！");
            }

        });
        $("#amountPayment_formSearch #later_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true}
        );
    }
    return oTableInit;
}

/**
 * 退款金额
 * @param value
 * @param row
 * @param index
 * @returns
 */
function tkjeformat(value,row,index){
    var tkje="";
    if(row.tkje!=null){
        tkje=row.tkje;
    }
    var html = "<input id='tkje_"+index+"'  onchange=\"checkSumInfo('"+index+"',this,\'tkje\')\" autocomplete='off' value='"+tkje+"' name='tkje_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;'  validate='{jeReq:true}' />";
    return html;
}
/**
 * 实付金额
 * @param value
 * @param row
 * @param index
 * @returns
 */
function sfjeformat(value,row,index){
    var sfje="";
    if(row.sfje!=null){
        sfje=row.sfje;
    }
    var html = "<input id='sfje_"+index+"'  onchange=\"checkSumInfo('"+index+"',this,\'sfje\')\" autocomplete='off' value='"+sfje+"' name='sfje_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;'  validate='{jeReq:true}' />";
    return html;
}


/**
 * 应付金额
 * @param value
 * @param row
 * @param index
 * @returns
 */
function yfjeformat(value,row,index){
    var yfje="";
    if(row.yfje!=null){
        yfje=row.yfje;
    }
    var html = "<input id='yfje_"+index+"'  onchange=\"checkSumInfo('"+index+"',this,\'yfje\')\" autocomplete='off' value='"+yfje+"' name='yfje_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;'  validate='{jeReq:true}' />";
    return html;
}



/**
 * 对账金额
 * @param value
 * @param row
 * @param index
 * @returns
 */
function dzjeformat(value,row,index){
    var dzje="";
    if(row.dzje!=null){
        dzje=row.dzje;
    }
    var html = "<input id='dzje_"+index+"' onchange=\"checkSumInfo('"+index+"',this,\'dzje\')\"  autocomplete='off' value='"+dzje+"' name='dzje_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;'  validate='{jeReq:true}' />";
    return html;
}


/**
 * 付款日期
 * @param value
 * @param row
 * @param index
 * @returns
 */
function fkrqformat(value,row,index){
    if(row.fkrq == null){
        row.fkrq = "";
    }
    var html = "<input id='fkrq_"+index+"' onchange=\"checkSumInfo('"+index+"',this,\'fkrq\')\" autocomplete='off' value='"+row.fkrq+"' name='fkrq_"+index+"'  style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' validate='{rqReq:true}' />";
    setTimeout(function() {
        laydate.render({
            elem: '#amountPayment_formSearch #fkrq_'+index
            ,theme: '#2381E9'
            ,btns: ['clear', 'confirm']
            ,done: function(value, date, endDate){
                laterList[index].fkrq = value;
            }
        });
    }, 100);
    return html;
}

/**
 * 退款日期
 * @param value
 * @param row
 * @param index
 * @returns
 */
function tkrqformat(value,row,index){
    if(row.tkrq == null){
        row.tkrq = "";
    }
    var html = "<input id='tkrq_"+index+"' onchange=\"checkSumInfo('"+index+"',this,\'tkrq\')\" autocomplete='off' value='"+row.tkrq+"' name='tkrq_"+index+"'  style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' validate='{rqReq:true}' />";
    setTimeout(function() {
        laydate.render({
            elem: '#amountPayment_formSearch #tkrq_'+index
            ,theme: '#2381E9'
            ,btns: ['clear', 'confirm']
            ,done: function(value, date, endDate){
                laterList[index].tkrq = value;
            }
        });
    }, 100);
    return html;
}
/**
 * 验证生产日期
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("rqReq", function (value, element){
    return this.optional(element) || value;
},"请填写日期。");

/**
 * 验证数量格式(两位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("jeReq", function (value, element){
    if(value){
        if(!/^\d+(\.\d{1,2})?$/.test(value)){
            $.error("请填写正确数值格式，可保留两位小数!");
        }
    }
    return this.optional(element) || /^\d+(\.\d{1,2})?$/.test(value);
},"请填写正确数值格式，可保留两位小数。");

/**
 * 失去焦点事件(修改列表参数)
 * @param id
 * @param e
 * @param zdmc
 * @returns
 */
function checkSumInfo(index, e, zdmc) {
    if (zdmc == "sfje"){
        laterList[index].sfje = e.value;
    }else if(zdmc == "tkje"){
        laterList[index].tkje = e.value;
    }else if(zdmc == "yfje"){
        laterList[index].yfje = e.value;
    }else if(zdmc == "dzje"){
        laterList[index].dzje = e.value;
    }else if(zdmc == "fkrq"){
        if (!/\d{4}(\-|\/|.)\d{1,2}\1\d{1,2}/.test(e.value)){
            $("#amountPayment_formSearch #fkrq_"+index).val("");
            $.error("请填写正确数值格式");
            return;
        }

        laterList[index].fkrq = e.value;
    }else if(zdmc == "tkrq"){
        if (!/\d{4}(\-|\/|.)\d{1,2}\1\d{1,2}/.test(e.value)){
            $("#amountPayment_formSearch #tkrq_"+index).val("");
            $.error("请填写正确数值格式");
            return;
        }
        laterList[index].tkrq = e.value;
    }
}