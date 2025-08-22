
$(function(){
    var oTable = new resend_TableInit();
    oTable.Init();
    var rows=JSON.parse($("#resend_formSearch #list").val().replace("",""));
    var list = {"rows":''}
    list.rows=rows;
    $("#resend_formSearch #resend_list").bootstrapTable('load',list);

})

function formatterView(value, row, index) {
    var html = "<a href='javascript:void(0);' onclick=\"showInfo('"+row.fjid+"','"+row.flg+"')\">"+row.nbbm+"</a>";
    return html;
}

function showInfo(id,flg) {
    var url="/experimentS/experimentS/pagedataViewmore?sjid="+id+"&flg_qf="+flg+"&jcxmdm="+$("#resend_formSearch #jcxmdm").val();
    $.showDialog(url,'查看详细信息',viewExperimentConfig);
}
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


var resend_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#resend_formSearch #resend_list").bootstrapTable({
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   // 是否显示分页（*）
            paginationShowPageGo: false,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            clickToSelect: true,                // 是否启用点击选中行
            uniqueId: "fjid",                     // 每一行的唯一标识，一般为主键列
            columns: [{
                checkbox: true,
                width: '5%',
            },{
                field: 'nbbm',
                title: '内部编号',
                width: '20%',
                align: 'left',
                formatter:formatterView,
                titleTooltip:'内部编号',
                visible: true
            },{
                field: 'lrsj',
                title: '录入时间',
                width: '20%',
                align: 'left',
                titleTooltip:'录入时间',
                visible:true
            },{
                field: 'rsyrq',
                title: '实验日期',
                width: '20%',
                align: 'left',
                titleTooltip:'实验日期',
                visible:true
            },{
                field: 'lxmc',
                title: '类型区分',
                width: '20%',
                align: 'left',
                titleTooltip:'类型区分',
                visible:true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            }

        });
        $("#resend_formSearch #resend_list").colResizable({
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