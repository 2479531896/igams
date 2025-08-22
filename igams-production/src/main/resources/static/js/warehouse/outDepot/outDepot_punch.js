var t_map = [];
var t_index="";

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
        field: 'wlid',
        title: '物料id',
        titleTooltip:'物料id',
        width: '4%',
        align: 'left',
        visible: false
    }, {
        field: 'wlbm',
        title: '物料编码',
        titleTooltip:'物料编码',
        width: '4%',
        align: 'left',
        visible: true
    }, {
        field: 'wlmc',
        title: '物料名称',
        titleTooltip:'物料名称',
        width: '6%',
        align: 'left',
        visible: true
    }, {
        field: 'jldw',
        title: '单位',
        titleTooltip:'单位',
        width: '3%',
        align: 'left',
        visible: true
    },{
        field: 'scph',
        title: '生产批号',
        titleTooltip:'生产批号',
        width: '5%',
        align: 'left',
        formatter:editOutPunchForm_scphformat,
        visible: true
    }, {
        field: 'cksl',
        title: '数量',
        titleTooltip:'数量',
        width: '4%',
        align: 'left',
        formatter: editOutPunchForm_Kckslformat,
        visible: true
    }, {
        field: 'scrq',
        title: '生产日期',
        titleTooltip:'生产日期',
        width: '5%',
        formatter:editOutPunchForm_scrqformat,
        align: 'left',
        visible: true
    }, {
        field: 'yxq',
        title: '失效日期',
        titleTooltip:'失效日期',
        width: '5%',
        formatter:editOutPunchForm_yxqformat,
        align: 'left',
        visible: true
    }, {
        field: 'hw',
        title: '货位',
        titleTooltip:'货位',
        width: '4%',
        formatter:editOutPunchForm_hwformat,
        align: 'left',
        visible: true
    // }, {
    //     field: 'bz',
    //     title: '备注',
    //     titleTooltip:'备注',
    //     formatter: editOutPunchForm_bzformat,
    //     width: '6%',
    //     align: 'left',
    //     visible: true
    },
    {
        field: 'cz',
        title: '操作',
        titleTooltip:'操作',
        width: '4%',
        align: 'left',
        formatter:editOutPunchForm_czformat,
        visible: true
    }];
var pickingEdit_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#editOutPunchForm #punch_list').bootstrapTable({
            url: $('#editOutPunchForm #urlPrefix').val()+$('#editOutPunchForm #url').val(),         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#editOutPunchForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "wlmc",					//排序字段
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
            uniqueId: "wlid",                     //每一行的唯一标识，一般为主键列
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
            sortLastName: "wlid", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            ckid: $('#editOutPunchForm #ckid').val()
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};

/**
 * 初始化页面
 * @returns
 */
function init(){
    //添加日期控件
    laydate.render({
        elem: '#ckrq'
        ,theme: '#2381E9'
    });
}
function checkTableValue(index, e, zdmc){
    if(zdmc=="scph"){
        t_map.rows[index].scph = e.value;
    }
    if(zdmc=="cksl"){
        if (t_map.rows[index].kcksl*1 < e.value*1 && t_map.rows[index].ckmxid && !$("#editOutPunchForm #pdbj").val()){
            $.alert("该出库数量不能大于"+t_map.rows[index].kcksl)
            $("#cksl_"+index).val(0);
            return;
        }else if (t_map.rows[index].kcksl*1 < e.value*1 && t_map.rows[index].yymxckd && $("#editOutPunchForm #pdbj").val()){
            $.alert("该出库数量不能大于"+t_map.rows[index].kcksl)
            $("#cksl_"+index).val(0);
            return;
        }else{
            t_map.rows[index].cksl = e.value;
        }
    }
    if(zdmc=="bz"){
        t_map.rows[index].bz = e.value;
    }
    if(zdmc=="scrq"){
        t_map.rows[index].scrq = e.value;
    }
    if(zdmc=="yxq"){
        t_map.rows[index].yxq = e.value;
    }
    if(zdmc=="hw"){
        t_map.rows[index].kwbh = e.value;
    }

}
/**
 * 验证数量格式(两位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("ckslNumber", function (value, element){
    if(!value){
        $.error("请填写数量!");
        return false;
    }else{
        if(!/^\d+(\.\d{1,2})?$/.test(value)){
            $.error("请填写正确数量格式，可保留两位小数!");
        }
    }
    return this.optional(element) || /^\d+(\.\d{1,2})?$/.test(value);
},"请填写正确格式，可保留两位小数。");
/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function editOutPunchForm_czformat(value,row,index){
    var html = "<span style='margin-left:5px;height:24px !important;' class='btn btn-danger' title='删除物料信息' onclick=\"punchDelWlDetail('" + index + "','"+row.wlid+"',event)\" >删除</span>";
    return html;
}

/**
 * 删除操作
 * @param index
 * @param event
 * @returns
 */
function punchDelWlDetail(index,value,event){
    t_map.rows.splice(index,1);
    $("#editOutPunchForm #punch_list").bootstrapTable('load',t_map);
}
/**
 * 有效日期格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function editOutPunchForm_yxqformat(value,row,index){
    if(row.yxq == null){
        row.yxq = "";
    }
    var html = "<input id='yxq_"+index+"' autocomplete='off' value='"+row.yxq+"' name='yxq_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' validate='{yxqReq:true}' onchange=\"checkTableValue('"+index+"',this,\'yxq\')\"></input>";
    setTimeout(function() {
        laydate.render({
            elem: '#editOutPunchForm #yxq_'+index
            ,theme: '#2381E9'
            ,done: function(value, date, endDate){
                t_map.rows[index].yxq = value;
            }
        });
    }, 100);
    return html;
}

/**
 * 备注格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function editOutPunchForm_bzformat(value,row,index){
    if(row.bz == null){
        row.bz = "";
    }
    var html = "<input id='bz_"+index+"' autocomplete='off' value='"+row.bz+"' name='bz_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' validate='{stringMaxLength:1024}' onchange=\"checkTableValue('"+index+"',this,\'bz\')\"></input>";
    return html;
}
/**
 * 验证有效日期
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("yxqReq", function (value, element){
    if(!value){
        $.error("请填写有效期!");
        return false;
    }
    return this.optional(element) || value;
},"请填写y有效期。");
/**
 * 生产日期格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function editOutPunchForm_scrqformat(value,row,index){
    if(row.scrq == null){
        row.scrq = "";
    }
    var html = "<input id='scrq_"+index+"' autocomplete='off' value='"+row.scrq+"' name='scrq_"+index+"'  style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' validate='{scrqReq:true}' onchange=\"checkTableValue('"+index+"',this,\'scrq\')\"></input>";
    setTimeout(function() {
        laydate.render({
            elem: '#editOutPunchForm #scrq_'+index
            ,theme: '#2381E9'
            ,max: 0
            ,done: function(value){
                t_map.rows[index].scrq = value;
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
jQuery.validator.addMethod("scrqReq", function (value, element){
    if(!value){
        $.error("请填写生产日期!");
        return false;
    }
    return this.optional(element) || value;
},"请填写生产日期。");
/**
 *出库数量格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function editOutPunchForm_Kckslformat(value,row,index){
    var cksl="";
    if(row.cksl!=null){
        cksl=row.cksl;
    }
    var html="";
    html ="<div id='cksldiv_"+index+"' name='cksldiv' style='width: 54px'>";

    html += "<input id='cksl_"+index+"' autocomplete='off' value='"+cksl+"' validate = '{ckslNumber:true}' name='cksl_"+index+"' style='border: 1px solid #ccc;border-radius: 4px;width: 100px' ";

        html += "onblur=\"checkTableValue('"+index+"',this,\'cksl\')\" ";

    html += "></input>";
    html += "</div>";
    return html;
}

/**
 * 生产批号格式话
 * @param value
 * @param row
 * @param index
 * @returns
 */
function editOutPunchForm_scphformat(value,row,index){
    if(row.scph == null){
        row.scph = "";
    }
    var html = "<input id='scph_"+index+"' autocomplete='off' value='"+row.scph+"' name='scph_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' validate='{stringMaxLength:64}' onchange=\"checkTableValue('"+index+"',this,\'scph\')\"></input>";
    // var html="";
    // html +="<div id='scphdiv_"+index+"' name='scphdiv' style='width:100%;display:inline-block'>";
    // html += "<select id='scph_"+index+"' name='scph_"+index+"' class='form-control chosen-select' validate='{required:true}'  onchange=\"checkTableValue('"+index+"',this,\'scph\')\">";
    // html += "<option value=''>--请选择--</option>";
    // if (row.hwxxDtoList){
    //     for(var i = 0; i < row.hwxxDtoList.length; i++) {
    //         html += "<option id='"+row.hwxxDtoList[i].scph+"' value='"+row.hwxxDtoList[i].scph+"'";
    //         if(scph!=null && scph!=""){
    //             if(scph==row.hwxxDtoList[i].scph){
    //                 html += "selected"
    //             }
    //         }
    //         html += ">"+row.hwxxDtoList[i].scph+"</option>";
    //     }
    // }
    // html +="</select></div>";
    return html;
}

/**
 * 货位
 * @param value
 * @param row
 * @param index
 * @returns
 */
function editOutPunchForm_hwformat(value,row,index){
    var kwbh=row.kwbh;
    let kwlist = JSON.parse($("#editOutPunchForm #kwlist").val());
    if (!kwlist){
        $.attr("未获取到库位信息");
        return;
    }
    var html="";
    html +="<div id='hwdiv_"+index+"' name='hwdiv' style='width:100%;display:inline-block'>";
    html += "<select id='hw_"+index+"' name='hw_"+index+"' class='form-control chosen-select' validate='{required:true}'  onchange=\"checkTableValue('"+index+"',this,\'hw\')\">";
    html += "<option value=''>--请选择--</option>";
    if (kwlist){
        for(var i = 0; i < kwlist.length; i++) {
            html += "<option id='"+kwlist[i].ckid+"' value='"+kwlist[i].ckid+"'";
            if(kwbh!=null && kwbh!=""){
                if(kwbh==kwlist[i].ckid){
                    html += "selected"
                }
            }
            html += ">"+kwlist[i].ckmc+"</option>";
        }
    }
    html +="</select></div>";
    return html;
}
/**
 * 根据物料名称模糊查询
 */
$('#editOutPunchForm #addnr').typeahead({
    source : function(query, process) {
        return $.ajax({
            url : $('#editOutPunchForm #urlPrefix').val()+'/storehouse/requisition/pagedataQueryWlxx',
            type : 'post',
            data : {
                "entire" : query,
                "access_token" : $("#ac_tk").val()
            },
            dataType : 'json',
            success : function(result) {
                var resultList = result.ckhwxxDtos
                    .map(function(item) {
                        var aItem = {
                            id : item.ckhwid,
                            name : item.wlbm+"-"+item.wlmc+"-"+item.scs+"-"+item.gg+"-"+item.ckqxmc
                        };
                        return JSON.stringify(aItem);
                    });
                return process(resultList);
            }
        });
    },
    matcher : function(obj) {
        var item = JSON.parse(obj);
        return ~item.name.toLowerCase().indexOf(
            this.query.toLowerCase())
    },
    sorter : function(items) {
        var beginswith = [], caseSensitive = [], caseInsensitive = [], item;
        while (aItem = items.shift()) {
            var item = JSON.parse(aItem);
            if (!item.name.toLowerCase().indexOf(
                this.query.toLowerCase()))
                beginswith.push(JSON.stringify(item));
            else if (~item.name.indexOf(this.query))
                caseSensitive.push(JSON.stringify(item));
            else
                caseInsensitive.push(JSON.stringify(item));
        }
        return beginswith.concat(caseSensitive,
            caseInsensitive)
    },
    highlighter : function(obj) {
        var item = JSON.parse(obj);
        var query = this.query.replace(
            /[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&')
        return item.name.replace(new RegExp('(' + query
            + ')', 'ig'), function($1, match) {
            return '<strong>' + match + '</strong>'
        })
    },
    updater : function(obj) {
        var item = JSON.parse(obj);
        $('#editOutPunchForm #addwlid').attr('value', item.id);
        return item.name;
    }
});

/**
 * 回车键添加物料至请购车
 * @param event
 * @returns
 */
var addkeydown = $("#addnr").bind("keydown",function(event){
    var ckhwid=$('#editOutPunchForm #addwlid').val();
    if (event.keyCode == "13") {
        //判断新增输入框是否有内容
        if(!$('#editOutPunchForm #addnr').val()){
            return false;
        }
        setTimeout(function(){ if(ckhwid != null && ckhwid != ''){
            $.ajax({
                type:'post',
                url:$('#editOutPunchForm #urlPrefix').val()+"/storehouse/requisition/pagedataQueryWlxxByWlid",
                cache: false,
                data: {"ckhwid":ckhwid,"access_token":$("#ac_tk").val()},
                dataType:'json',
                success:function(data){
                    //返回值
                    if(data.ckhwxxDto!=null && data.ckhwxxDto!=''){
                        //kcbj=1 有库存，kcbj=0 库存未0
                        // if(data.ckhwxxDto.kcbj!='1'){
                        //     $.confirm("该物料库存不足！");
                        // }else{
                            if(t_map.rows==null || t_map.rows ==""){
                                t_map.rows=[];
                            }
                            t_map.rows.push(data.ckhwxxDto);
                            $("#editOutPunchForm #punch_list").bootstrapTable('load',t_map);
                            $('#editOutPunchForm #addwlid').val("");
                            $('#editOutPunchForm #addnr').val("");
                        // }
                    }else{
                        $.confirm("该物料不存在!");
                    }
                }
            });
        }else{
            var addnr = $('#editOutPunchForm #addnr').val();
            if(addnr != null && addnr != ''){
                $.confirm("请选择物料信息!");
            }
        }}, '200')
    }
})
/**
 * 选择部门列表
 * @returns
 */
function chooseBm(){
    var url=$('#editOutPunchForm #urlPrefix').val() + "/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
    $.showDialog(url,'选择部门',addBmConfig);
}
var addBmConfig = {
    width		: "800px",
    modalName	:"addBmModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var sel_row = $('#listDepartmentForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    if (sel_row[0].jgdm){
                        $('#editOutPunchForm #jgdm').val(sel_row[0].jgdm);
                    }else{
                        $('#editOutPunchForm #jgdm').val("");
                        $.alert("U8不存在此部门！");
                        return false;
                    }
                    $('#editOutPunchForm #bm').val(sel_row[0].jgid);
                    $('#editOutPunchForm #jgmc').val(sel_row[0].jgmc);
                    if (sel_row[0].kzcs1){
                        let url= $('#editOutPunchForm #urlPrefix').val()+"/storehouse/requisition/pagedataGenerateLljlbh";
                        $.ajax({
                            type:'post',
                            url:url,
                            cache: false,
                            data: {"jgdh":sel_row[0].kzcs1,"access_token":$("#ac_tk").val()},
                            dataType:'json',
                            success:function(data){
                                if (data.jlbh){
                                    $('#editOutPunchForm #jlbh').val(data.jlbh);
                                }
                            }
                        });
                    }else{
                        $('#editOutPunchForm #bm').val("");
                        $('#editOutPunchForm #jgmc').val("");
                        $('#editOutPunchForm #bmdm').val("");
                        $('#editOutPunchForm #jlbh').val("");
                        $.alert("所选部门存在异常！");
                        return false;
                    }
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
 * 点击确定添加物料
 * @param
 * @param
 * @returns
 */
function punchCofirmaddwl(){
    var ckhwid=$('#editOutPunchForm #addwlid').val();
    //判断新增输入框是否有内容
    if(!$('#editOutPunchForm #addnr').val()){
        return false;
    }
    setTimeout(function(){ if(ckhwid != null && ckhwid != ''){
        $.ajax({
            type:'post',
            url:$('#editOutPunchForm #urlPrefix').val()+"/storehouse/requisition/pagedataQueryWlxxByWlid",
            cache: false,
            data: {"ckhwid":ckhwid,"access_token":$("#ac_tk").val()},
            dataType:'json',
            success:function(data){
                //返回值
                if(data.ckhwxxDto!=null && data.ckhwxxDto!=''){
                    //kcbj=1 有库存，kcbj=0 库存未0
                    if(data.ckhwxxDto.kcbj!='1'){
                        $.confirm("该物料库存不足！");
                    }else{
                        if(t_map.rows==null || t_map.rows ==""){
                            t_map.rows=[];
                        }
                        t_map.rows.push(data.ckhwxxDto);
                        $("#editOutPunchForm #punch_list").bootstrapTable('load',t_map);
                        $('#editOutPunchForm #addwlid').val("");
                        $('#editOutPunchForm #addnr').val("");
                    }
                }else{
                    $.confirm("该物料不存在!");
                }
            }
        });
    }else{
        var addnr = $('#editOutPunchForm #addnr').val();
        if(addnr != null && addnr != ''){
            $.confirm("请选择物料信息!");
        }
    }}, '200')
}

$(document).ready(function(){
    var sel_ck=$("#editOutPunchForm  #ck");
    //仓库下拉框事件
    sel_ck.unbind("change").change(function(){
        ckEventPunch();
    });
    //初始化列表
    var oTable=new pickingEdit_TableInit();
    oTable.Init();
    // 初始化页面
    init();
    //所有下拉框添加choose样式
    jQuery('#editOutPunchForm .chosen-select').chosen({width: '100%'});
});

//仓库下拉事件
function ckEventPunch(){
    var ckid=$("#editOutPunchForm #ck").val();
    $.ajax({
        type:'post',
        url:$('#editOutPunchForm #urlPrefix').val()+"/storehouse/arrivalGoods/pagedataGetkwlist",
        cache: false,
        data: {"ckid":ckid,"access_token":$("#ac_tk").val()},
        dataType:'json',
        success:function(data){
            if(data.kwlist!=null && data.kwlist.length>0){
                $("#editOutPunchForm #kwlist").val(JSON.stringify(data.kwlist));
                $("#editOutPunchForm #punch_list").bootstrapTable('load',t_map);
            }else{
                $.confirm("该仓库下库位信息为空，请先进行维护！");
            }
        }
    });
}