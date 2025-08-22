var t_map = [];
var dwList = null;
// 显示字段
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
    }, {
        field: 'syglid',
        title: '实验管理id',
        titleTooltip:'实验管理id',
        width: '4%',
        align: 'left',
        visible: false
    }, {
        field: 'hzxm',
        title: '患者',
        titleTooltip:'患者',
        width: '4%',
        align: 'left',
        visible: true
    }, {
        field: 'ybbh',
        title: '标本编号',
        titleTooltip:'标本编号',
        width: '7%',
        align: 'left',
        visible: true
    }, {
        field: 'yblxmc',
        title: '标本类型',
        titleTooltip:'标本类型',
        width: '6%',
        align: 'left',
        visible: true
    }, {
        field: 'nbbm',
        title: '内部编码',
        titleTooltip:'内部编码',
        width: '7%',
        align: 'left',
        visible: true
    }, {
        field: 'jcxmmc',
        title: '检测项目',
        titleTooltip:'检测项目',
        width: '12%',
        align: 'left',
        visible: true
    }, {
        field: 'jczxmmc',
        title: '检测子项目',
        titleTooltip:'检测子项目',
        width: '6%',
        align: 'left',
        visible: false
    }, {
        field: 'jclxmc',
        title: '检测类型',
        titleTooltip:'检测类型',
        width: '6%',
        align: 'left',
        visible: true
    }, {
        field: 'jcdwmc',
        title: '检测单位',
        titleTooltip:'检测单位',
        width: '8%',
        align: 'left',
        formatter:jcdwformat,
        visible: true
    }, {
        field: 'sfjs',
        title: '是否接收',
        titleTooltip:'是否接收',
        width: '6%',
        align: 'center',
        formatter:adjustSfjsformat,
        visible: true
    }, {
        field: 'jsrq',
        title: '接收日期',
        titleTooltip:'接收日期',
        width: '8%',
        align: 'left',
        formatter:jsrqformat,
        visible: true
    }, {
        field: 'sfqy',
        title: '是否取样',
        titleTooltip:'是否取样',
        width: '6%',
        align: 'center',
        formatter:sfqyformat,
        visible: true
    }, {
        field: 'qysj',
        title: '取样时间',
        titleTooltip:'取样时间',
        width: '8%',
        align: 'left',
        formatter:qysjformat,
        visible: true
    }, {
        field: 'jcbj',
        title: '是否实验',
        titleTooltip:'是否实验',
        width: '6%',
        align: 'left',
        formatter:sfsyformat,
        visible: true
    }, {
        field: 'syrq',
        title: '实验日期',
        titleTooltip:'实验日期',
        width: '8%',
        align: 'left',
        formatter:syrqformat,
        visible: true
    }];
var adjust_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#ajaxFormBbsyAdjust #sygl_list').bootstrapTable({
            url:    '/sampleExperiment/sampleExperiment/pagedataAdjustInfo?ids='+$('#ajaxFormBbsyAdjust #ids').val(),             //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#ajaxFormBbsyAdjust #toolbar',    //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "jcxmid",					//排序字段
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
            uniqueId: "syglid",                     //每一行的唯一标识，一般为主键列
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
            sortLastName: "lrsj", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
        };
        return map;
    };
    return oTableInit;
};

function checkJcdw(index){
    t_map.rows[index].jcdw = $("#ajaxFormBbsyAdjust #jc_" + index).find("option:selected").val();
}
/**
 * 检测单位格式化(该方法是选择伙伴限制的检测单位)
 * @param value
 * @param row
 * @param index
 * @returns
 */
// function jcdwformat(value,row,index){
//     var dwList = row.jcdwList;
//     var html="";
//     if (dwList && dwList.length >0) {
//         html +="<div id='jcdwdiv_"+index+"' name='jcdwdiv' style='width:100%;display:inline-block'>";
//         html +=  "<select id='jc_"+index+"' name='jc_"+index+"' class='form-control'  onchange=\"checkJcdw('"+index+"')\">";
//         html += "<option value=''>请选择--</option>";
//         for(var i=0;i<dwList.length;i++){
//             if(row.jcdw==dwList[i].jcdw){
//                 html += "<option value='"+dwList[i].jcdw+"' selected >"+dwList[i].jcdwmc+"</option>";
//             }else{
//                 html += "<option value='"+dwList[i].jcdw+"' >"+dwList[i].jcdwmc+"</option>";
//             }
//         }
//         html += "</select></div>";
//     }else{
//         html += "<span  >"+value+"</span>";
//     }
//     return html;
// }
/**
 * 检测单位格式化 (该方法是选择基础数据检测单位)
 * @param value
 * @param row
 * @param index
 * @returns
 */
function jcdwformat(value,row,index){
    // var dwList = row.jcdwList;
    var dwList = JSON.parse($('#ajaxFormBbsyAdjust #dwList').val());
    var html="";
    if (dwList && dwList.length >0) {
        html +="<div id='jcdwdiv_"+index+"' name='jcdwdiv' style='width:100%;display:inline-block'>";
        html +=  "<select id='jc_"+index+"' name='jc_"+index+"' class='form-control'  onchange=\"checkJcdw('"+index+"')\">";
        html += "<option value=''>请选择--</option>";
        for(var i=0;i<dwList.length;i++){
            if(row.jcdw==dwList[i].csid){
                html += "<option value='"+dwList[i].csid+"' selected >"+dwList[i].csmc+"</option>";
            }else{
                html += "<option value='"+dwList[i].csid+"' >"+dwList[i].csmc+"</option>";
            }
        }
        html += "</select></div>";
    }else{
        html += "<span  >"+value+"</span>";
    }
    return html;
}
/**
 * 是否接收格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function adjustSfjsformat(value,row,index){
    var html="";
    if (row.sfjs =='1'){
        html="<input type=\"checkbox\" id='sfjs_"+index+"' name='sfjs_"+index+"' value=\"1\" checked onchange=\"tochangeZd('"+index+"',this,\'sfjs\')\">";
    }else{
        html="<input type=\"checkbox\" id='sfjs_"+index+"' name='sfjs_"+index+"' value=\"0\" onchange=\"tochangeZd('"+index+"',this,\'sfjs\')\" >";
    }
    return html;
}
function tochangeZd(index,e,zdmc){
    if (zdmc=='sfjs'){
        if ($("#ajaxFormBbsyAdjust #sfjs_"+index+"").is(":checked")){
            t_map.rows[index].sfjs='1';
            $("#ajaxFormBbsyAdjust #jsrq_"+index+"").removeAttr("disabled","true");
        }else{
            t_map.rows[index].sfjs='0';
            //是否接收不勾选，则接收清空且禁用
            $("#ajaxFormBbsyAdjust #jsrq_"+index+"").val("");
            $("#ajaxFormBbsyAdjust #jsrq_"+index+"").attr("disabled", "disabled");
            t_map.rows[index].jsrq='';
        }
    }else if(zdmc=='jcbj'){
        if ($("#ajaxFormBbsyAdjust #jcbj_"+index+"").is(":checked")){
            t_map.rows[index].jcbj='1';
            $("#ajaxFormBbsyAdjust #syrq_"+index+"").removeAttr("disabled","true");
        }else{
            t_map.rows[index].jcbj='0';
            //是否实验不勾选，则实验清空且禁用
            $("#ajaxFormBbsyAdjust #syrq_"+index+"").val("");
            $("#ajaxFormBbsyAdjust #syrq_"+index+"").attr("disabled", "disabled");
            t_map.rows[index].syrq='';
        }
    }else if(zdmc=='sfqy'){
        if ($("#ajaxFormBbsyAdjust #sfqy_"+index+"").is(":checked")){
            t_map.rows[index].sfqy='1';
            $("#ajaxFormBbsyAdjust #qysj_"+index+"").removeAttr("disabled","true");
        }else{
            t_map.rows[index].sfqy='0';
            //是否实验不勾选，则实验清空且禁用
            $("#ajaxFormBbsyAdjust #qysj_"+index+"").val("");
            $("#ajaxFormBbsyAdjust #qysj_"+index+"").attr("disabled", "disabled");
            t_map.rows[index].qysj='';
        }
    }else if(zdmc=='jsrq'){
        t_map.rows[index].jsrq=e.value;
        if (e.value != undefined && e.value != '' && e.value != null){
            $("#ajaxFormBbsyAdjust #sfjs_"+index+"").attr("checked", "checked");
        }else{
            $("#ajaxFormBbsyAdjust #sfjs_"+index+"").removeAttr("checked","true");
        }
    }else if(zdmc=='syrq'){
        t_map.rows[index].syrq=e.value;
        if (e.value != undefined && e.value != '' && e.value != null){
            $("#ajaxFormBbsyAdjust #jcbj_"+index+"").attr("checked", "checked");
        }else{
            $("#ajaxFormBbsyAdjust #jcbj_"+index+"").removeAttr("checked","true");
        }
    }else if(zdmc=='qysj'){
        t_map.rows[index].qysj=e.value;
        if (e.value != undefined && e.value != '' && e.value != null){
            $("#ajaxFormBbsyAdjust #qysj_"+index+"").attr("checked", "checked");
        }else{
            $("#ajaxFormBbsyAdjust #qysj_"+index+"").removeAttr("checked","true");
        }
    }

}

/**
 * 是否实验格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function sfsyformat(value,row,index){
    var html="";
    if (row.jcbj =='1'){
        html="<input type=\"checkbox\" id='jcbj_"+index+"' name='jcbj_"+index+"' value=\"1\" checked onchange=\"tochangeZd('"+index+"',this,\'jcbj\')\" >";
    }else{
        html="<input type=\"checkbox\" id='jcbj_"+index+"' name='jcbj_"+index+"' value=\"0\" onchange=\"tochangeZd('"+index+"',this,\'jcbj\')\" >";
    }
    return html;
}

/**
 * 是否取样格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function sfqyformat(value,row,index){
    var html="";
    if (row.qysj){
        html="<input type=\"checkbox\" id='sfqy_"+index+"' name='sfqy_"+index+"' value=\"1\" checked onchange=\"tochangeZd('"+index+"',this,\'sfqy\')\" >";
    }else{
        html="<input type=\"checkbox\" id='sfqy_"+index+"' name='sfqy_"+index+"' value=\"0\" onchange=\"tochangeZd('"+index+"',this,\'sfqy\')\" >";
    }
    return html;
}
/**
 * 接收日期格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function jsrqformat(value,row,index){
    if(row.jsrq==null){
        row.jsrq="";
    }
    var html="<input id='jsrq_"+index+"' name='jsrq_"+index+"' class='jsrq' value='"+row.jsrq+"' "+(row.jsrq?"":"disabled")+" style='width:85%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"tochangeZd('"+index+"',this,\'jsrq\')\" >";
    setTimeout(function() {
        laydate.render({
            elem: '#ajaxFormBbsyAdjust #jsrq_'+index
            ,theme: '#2381E9'
            , type: 'datetime'
            , format: 'yyyy-MM-dd HH:mm:ss'//保留时分
            ,min: 0
            ,btns: ['clear', 'confirm']
            ,done: function(value, date, endDate){
                t_map.rows[index].jsrq=value;
                if (value != undefined && value != '' && value != null){
                    $("#ajaxFormBbsyAdjust #sfjs_"+index+"").attr("checked", "checked");
                }else{
                    $("#ajaxFormBbsyAdjust #sfjs_"+index+"").removeAttr("checked","true");
                }
            }
        });
    }, 100);
    return html;
}
/**
 * 接收日期格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function syrqformat(value,row,index){
    if(row.syrq==null){
        row.syrq="";
    }
    var html="<input id='syrq_"+index+"' syglid='"+row.syglid+"' name='syrq_"+index+"' class='syrq' value='"+row.syrq+"' "+(row.syrq?"":"disabled")+"  style='width:85%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"tochangeZd('"+index+"',this,\'syrq\')\" >";
    setTimeout(function() {
        laydate.render({
            elem: '#ajaxFormBbsyAdjust #syrq_'+index
            ,theme: '#2381E9'
            , type: 'datetime'
            , format: 'yyyy-MM-dd HH:mm:ss'//保留时分
            ,min: 0
            ,btns: ['clear', 'confirm']
            ,done: function(value, date, endDate){
                t_map.rows[index].syrq=value;
                if (value != undefined && value != '' && value != null){
                    $("#ajaxFormBbsyAdjust #jcbj_"+index+"").attr("checked", "checked");
                }else{
                    $("#ajaxFormBbsyAdjust #jcbj_"+index+"").removeAttr("checked","true");
                }
                // var data=JSON.parse($("#ajaxFormBbsyAdjust #sjsygl_json").val())
                // data[index].syrq=value;
                // $("#ajaxFormBbsyAdjust #sjsygl_json").val(JSON.stringify(data));
            }
        });
    }, 100);
    return html;
}

/**
 * 取样时间格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function qysjformat(value,row,index){
    if(row.qysj==null){
        row.qysj="";
    }
    var html="<input id='qysj_"+index+"' syglid='"+row.syglid+"' name='qysj_"+index+"' class='qysj' value='"+row.qysj+"' "+(row.qysj?"":"disabled")+"  style='width:85%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"tochangeZd('"+index+"',this,\'qysj\')\" >";
    setTimeout(function() {
        laydate.render({
            elem: '#ajaxFormBbsyAdjust #qysj_'+index
            ,theme: '#2381E9'
            , type: 'datetime'
            , format: 'yyyy-MM-dd HH:mm:ss'//保留时分
            ,min: 0
            ,btns: ['clear', 'confirm']
            ,done: function(value, date, endDate){
                t_map.rows[index].qysj=value;
                if (value != undefined && value != '' && value != null){
                    $("#ajaxFormBbsyAdjust #sfqy_"+index+"").attr("checked", "checked");
                }else{
                    $("#ajaxFormBbsyAdjust #sfqy_"+index+"").removeAttr("checked","true");
                }
                // var data=JSON.parse($("#ajaxFormBbsyAdjust #sjsygl_json").val())
                // data[index].syrq=value;
                // $("#ajaxFormBbsyAdjust #sjsygl_json").val(JSON.stringify(data));
            }
        });
    }, 100);
    return html;
}

$(document).ready(function(){
    //初始化列表
    var oTable=new adjust_TableInit();
    oTable.Init();
    // var list = $('#ajaxFormBbsyAdjust #dwList').val()
    // if (list){
    //     dwList = JSON.parse(list);
    // }
    //所有下拉框添加choose样式
    jQuery('#ajaxFormBbsyAdjust .chosen-select').chosen({width: '100%'});
});