var t_map = [];
var formAction = $('#editRedpacketsettingForm #formAction').val();
//显示字段
var columnsArray = [
    {
        title: '序',
        formatter: function (value, row, index) {
            return index+1;
        },
        titleTooltip:'序',
        width: '3%',
        align: 'left',
        visible:true
    }, {
        field: 'je',
        title: '金额',
        titleTooltip:'金额',
        width: '10%',
        align: 'left',
        formatter:jeformat,
        visible: true
    }, {
        field: 'sl',
        title: '数量',
        titleTooltip:'数量',
        width: '10%',
        align: 'left',
        formatter:slformat,
        visible: true
    }, {
        field: 'sysl',
        title: '剩余数量',
        titleTooltip:'剩余数量',
        width: '10%',
        align: 'left',
        visible: formAction=='addSaveRedpacketsetting'||formAction=='copySaveRedpacketsetting'?false:true,
    }, {
        field: 'zdf',
        title: '最低分',
        titleTooltip:'最低分',
        width: '8%',
        align: 'left',
        formatter:zdfformat,
        visible: true
    }, {
        field: 'zgf',
        title: '最高分',
        titleTooltip:'最高分',
        width: '8%',
        align: 'left',
        formatter:zgfformat,
        visible: true
    }, {
        field: 'sftz',
        title: '是否通知',
        titleTooltip:'是否通知',
        width: '10%',
        align: 'left',
        formatter:sftzformat,
        visible: true
    }, {
        field: 'tznr',
        title: '通知内容',
        titleTooltip:'通知内容',
        width: '30%',
        align: 'left',
        formatter:tznrformat,
        visible: true
    }, {
        field: 'bs',
        title: '倍数',
        titleTooltip:'倍数',
        width: '10%',
        align: 'left',
        formatter:bsformat,
        visible: true
    },{
        field: 'cz',
        title: '操作',
        titleTooltip:'操作',
        width: '10%',
        align: 'left',
        formatter:czformat,
        visible: true
    }];

var editRedpacketsetting_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#editRedpacketsettingForm #tb_list').bootstrapTable({
            url: $('#editRedpacketsettingForm #urlPrefix').val()+'/redpacket/redpacket/pagedataGetRedpacketsettingDetails?hbszid='+$('#editRedpacketsettingForm #hbszid').val(),         //请求后台的URL（*）
            method: 'get',                       //请求方式（*）
            toolbar: '#editRedpacketsettingForm #toolbar',                //工具按钮用哪个容器
            striped: true,                       //是否显示行间隔色
            cache: false,                        //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                      //是否启用排序
            sortName: "hbszmx.je",				     //排序字段
            sortOrder: "desc",                   //排序方式
            queryParams: oTableInit.queryParams, //传递参数（*）
            sidePagination: "server",            //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                        //初始化加载第一页，默认第一页
            pageSize: 15,                        //每页的记录行数（*）
            pageList: [15, 30, 50, 100],         //可供选择的每页的行数（*）
            paginationPreText: '‹',				 //指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			 //指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            isForceTable: true,
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,              //最少允许的列数
            clickToSelect: false,                //是否启用点击选中行
            //height: 500,                       //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "hbszmxid",                    //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                     //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
            onLoadSuccess: function (map) {
                t_map = map;
                if(map.rows.length>0){
                    // 初始化hbszmx_json
                    var json = [];
                    for (var i = 0; i < t_map.rows.length; i++) {
                        var sz = {"index":'',"hbszid":'',"hbmxid":'',"je":'',"sl":'',"sysl":'',"zdf":'',"zgf":'',"sftz":'',"tznr":'',"bs":''};
                        sz.index=t_map.rows.length;
                        sz.hbszid = t_map.rows[i].hbszid;
                        sz.hbmxid = t_map.rows[i].hbmxid;
                        sz.sl = t_map.rows[i].sl;
                        sz.sysl = t_map.rows[i].sysl;
                        sz.zdf = t_map.rows[i].zdf;
                        sz.zgf = t_map.rows[i].zgf;
                        sz.sftz = t_map.rows[i].sftz;
                        sz.tznr = t_map.rows[i].tznr;
                        sz.bs = t_map.rows[i].bs;
                        json.push(sz);
                    }
                    $("#editRedpacketsettingForm #hbszmx_json").val(JSON.stringify(json));
                    $("#editRedpacketsettingForm #xgqhbszmx_json").val(JSON.stringify(json));
                }else {
                    var hbszmx={"hbszid":'',"hbmxid":'',"je":'',"sl":'',"sysl":'',"zdf":'',"zgf":'',"sftz":'',"tznr":'',"bs":''};
                    var data=[];
                    data.push(hbszmx);
                    t_map.rows.push(data);
                    $("#editRedpacketsettingForm #hbszmx_json").val(JSON.stringify(data));
                    $("#editRedpacketsettingForm #tb_list").bootstrapTable('load',t_map);
                }
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                return;
            }
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
            sortLastName: "hbszmx.sl", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};
//新增明细
function addhbszmx(){
    var data=JSON.parse($("#editRedpacketsettingForm #hbszmx_json").val());
    var hbszmx={"hbszid":'',"hbmxid":'',"je":'',"sl":'',"zdf":'',"zgf":'',"sftz":'',"tznr":'',"bs":''};
    t_map.rows.push(hbszmx);
    data.push(hbszmx);
    $("#editRedpacketsettingForm #hbszmx_json").val(JSON.stringify(data));
    $("#editRedpacketsettingForm #tb_list").bootstrapTable('load',t_map);
}
/**
 * 金额格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function jeformat(value,row,index){
    if(row.je == null){
        row.je = "";
    }
    var html = "<input id='je_"+index+"' value='"+row.je+"' name='je_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' validate='{jeNumber:true}' onblur= \"checkSum('"+index+"',this,\'je\')\"/>";
    return html;
}
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
    var html = "<input id='sl_"+index+"' value='"+row.sl+"' name='sl_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onkeyup=\"if(!/^[0-9]+$/.test(value)) value=value.replace(/\\D/g,'');if(value>999999)value=999999;if(value<1)value=null\" validate='{required:true}' onblur= \"checkSum('"+index+"',this,\'sl\')\"/>";
    return html;
}
/**
 *  最低分格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function zdfformat(value,row,index){
    if(row.zdf == null){
        row.zdf = "";
    }
    var html = "<input id='zdf_"+index+"' value='"+row.zdf+"' name='zdf_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' validate='{fsNumber:true}' onblur= \"checkSum('"+index+"',this,\'zdf\')\"/>";
    return html;
}
/**
 *  最高分格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function zgfformat(value,row,index){
    if(row.zgf == null){
        row.zgf = "";
    }
    var html = "<input id='zgf_"+index+"' value='"+row.zgf+"' name='zgf_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' validate='{fsNumber:true}' onblur= \"checkSum('"+index+"',this,\'zgf\')\"/>";
    return html;
}
/**
 * 是否通知格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function sftzformat(value,row,index){
    var html="";
    html +=  "<select id='sftz_"+index+"' name='sftz_"+index+"'  class='form-control chosen-select'  onchange=\"checkSum('"+index+"',this,\'sftz\')\">";
    if(row.sftz=='1'){
        html += "<option value=''>请选择--</option>";
        html += "<option value='1' selected>是</option>";
        html += "<option value='0'>否</option>";
    }else if(row.sftz=='0'){
        html += "<option value=''>请选择--</option>";
        html += "<option value='1' >是</option>";
        html += "<option value='0' selected>否</option>";
    }else{
        html += "<option value=''>请选择--</option>";
        html += "<option value='1' >是</option>";
        html += "<option value='0' >否</option>";
    }
    html += "</select>";
    return html;
}
/**
 * 备注内容格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function bsformat(value,row,index){
    if(row.bs == null){
        row.bs = "" ;
    }
    var html = "<input id='bs_"+index+"' value='"+row.bs+"' name='bs_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;'  onkeyup=\"if(!/^[0-9]+$/.test(value)) value=value.replace(/\\D/g,'')\" onblur=\"checkSum('"+index+"',this,\'bs\')\">";
    return html;
}
/**
 * 通知内容格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function tznrformat(value,row,index){
    if(row.tznr == null){
        row.tznr = "" ;
    }
    var html="<input id='tznr_"+index+"' name='tznr_"+index+"' value='"+row.tznr+"' validate='{required:true,stringMaxLength:1024}' placeholder='替换规则 姓名：【name】 金额：【je】' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'tznr\')\"></input>";
    return html;
}
/**
 * 验证分数格式(两位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("fsNumber", function (value, element){
    if(!value){
        $("#editRedpacketsettingForm #"+element.id).val("")
        return false;
    }else{
        if(!/^\d+(\.\d{1,2})?$/.test(value)){
            $.error("请填写正确分数格式，可保留两位小数!");
            $("#editRedpacketsettingForm #"+element.id).val("")
        }
    }
     return this.optional(element) || /^\d+(\.\d{1,2})?$/.test(value);
},"请填写正确格式，可保留两位小数。");
/**
 * 验证金额格式(两位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("jeNumber", function (value, element){
    if(!value){
        $("#editRedpacketsettingForm #"+element.id).val("")
        return false;
    }else{
        if (value<0){
            $.error("金额不能小于0!");
            $("#editRedpacketsettingForm #"+element.id).val("")
        }
        if(!/^\d+(\.\d{1,2})?$/.test(value)){
            $.error("请填写正确金额格式，可保留两位小数!");
            $("#editRedpacketsettingForm #"+element.id).val("")
        }
    }
    return this.optional(element) || /^\d+(\.\d{1,2})?$/.test(value);
},"请填写正确格式，可保留两位小数。");
/**
 * 删除操作
 * @param index
 * @param event
 * @returns
 */
function delHbszmx(index,event){
    var preJson = JSON.parse($("#editRedpacketsettingForm #hbszmx_json").val());
    preJson.splice(index,1);
    t_map.rows.splice(index,1);
    $("#editRedpacketsettingForm #hbszmx_json").val(JSON.stringify(preJson));
    $("#editRedpacketsettingForm #tb_list").bootstrapTable('load',t_map);
}
/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function czformat(value,row,index){
    var html="";
    html += "<span style='margin-left:5px;height:24px !important;' class='btn btn-danger' title='删除明细' onclick=\"delHbszmx('" + index + "',event)\" >删除</span></div>";
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
        if (zdmc == 'je') {
            t_map.rows[index].je = e.value;
        } else if (zdmc == 'sl') {
            if (t_map.rows[index].sysl!=null&&t_map.rows[index].sl!=''&&formAction!='copySaveRedpacketsetting'){
                if (Number(e.value)<Number(t_map.rows[index].sysl)){
                    $.error("数量不能小于剩余数量！");
                    e.value = '';
                    return false;
                }
            }
            t_map.rows[index].sl = e.value;
        }else if (zdmc == 'zdf') {
            if (t_map.rows[index].zgf!=null&&t_map.rows[index].zgf!=''&&t_map.rows[index].zdf!=null&&t_map.rows[index].zdf!=''){
                if (Number(e.value)>Number(t_map.rows[index].zgf)){
                    $.error("最低分不能大于最高分！");
                    e.value = '';
                    return false;
                }
            }
            t_map.rows[index].zdf = e.value;
        }else if (zdmc == 'zgf') {
            if (t_map.rows[index].zdf!=null&&t_map.rows[index].zdf!=''&&t_map.rows[index].zgf!=null&&t_map.rows[index].zgf!='') {
                if (Number(e.value) < Number(t_map.rows[index].zdf)) {
                    $.error("最高分不能小于最低分！");
                    e.value = '';
                    return false;
                }
            }
            t_map.rows[index].zgf = e.value;
        }else if (zdmc == 'sftz') {
            t_map.rows[index].sftz = e.value;
        }else if (zdmc == 'tznr') {
            t_map.rows[index].tznr = e.value;
        }else if (zdmc == 'bs') {
            t_map.rows[index].bs = e.value;
        }
    }
    $("#editRedpacketsettingForm #hbszmx_json").val(JSON.stringify(t_map.rows));
}
laydate.render({
    elem: '#editRedpacketsettingForm  #fssj'
    ,type: 'datetime'
    ,ready: function(date){
        var myDate = new Date(); //实例一个时间对象；
        this.dateTime.hours=myDate.getHours();
    }
    ,theme: '#2381E9'
    ,btns: ['clear', 'confirm']
});
$(function(){
    // 所有下拉框添加choose样式
    jQuery('#editRedpacketsettingForm .chosen-select').chosen({width: '100%'});

    //初始化列表
    var oTable=new editRedpacketsetting_TableInit();
    oTable.Init();
});