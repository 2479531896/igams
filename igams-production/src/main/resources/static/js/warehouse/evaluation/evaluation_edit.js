var t_map=[];
var editEvaluation_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#evaluationEditForm #edit_list").bootstrapTable({
            url:   $('#evaluationEditForm #urlPrefix').val()+'/evaluation/evaluation/pagedataGetEvaluation',         //请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#evaluationEditForm #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   // 是否显示分页（*）
            paginationShowPageGo: false,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"yzmx.xh",					// 排序字段
            sortOrder: "asc",                   // 排序方式
            queryParams: oTableInit.queryParams,// 传递参数（*）
            sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       // 初始化加载第一页，默认第一页
            pageSize: 15,                       // 每页的记录行数（*）
            pageList: [15, 30, 50, 100],        // 可供选择的每页的行数（*）
            paginationPreText: '‹',				// 指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			// 指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: false,                  // 是否显示所有的列
            showRefresh: false,                  // 是否显示刷新按钮
            minimumCountColumns: 2,             // 最少允许的列数
            clickToSelect: true,                // 是否启用点击选中行
            // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "yzmxid",                     // 每一行的唯一标识，一般为主键列
            showToggle:false,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns:  [
                {
                    title: '序号',
                    formatter: function (value, row, index) {
                        return index+1;
                    },
                    titleTooltip:'序号',
                    width: '3%',
                    align: 'left',
                    visible:true
                },{
                    field: 'yzmxid',
                    title: '验证明细id',
                    width: '6%',
                    align: 'left',
                    visible: false
                 },{
                    field: 'wlbm',
                    title: '物料编码',
                    width: '6%',
                    align: 'left',
                    visible: true
                },{
                    field: 'wlmc',
                    title: '物料名称',
                    width: '15%',
                    align: 'left',
                    formatter:edit_wlmcformat,
                    visible: true
                },{
                    field: 'gg',
                    title: '规格',
                    width: '15%',
                    align: 'left',
                    visible: true
                },{
                    field: 'xmh',
                    title: '项目号',
                    width: '15%',
                    align: 'left',
                    formatter:edit_xmhformat,
                    visible: true
                },{
                    field: 'jszb',
                    title: '技术指标',
                    width: '25%',
                    align: 'left',
                    formatter:edit_jszbformat,
                    visible: true
                },{
                    field: 'zlyq',
                    title: '质量要求',
                    width: '25%',
                    align: 'left',
                    formatter:edit_zlyqformat,
                    visible: true
                },{
                    field: 'cz',
                    title: '操作',
                    width: '5%',
                    align: 'left',
                    formatter:edit_czformat,
                    visible: true
                }],
            onLoadSuccess:function(map){
                t_map=map;
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            },
        });
    };
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            access_token:$("#ac_tk").val(),
            gfyzid: $('#evaluationEditForm #gfyzid').val()
            // 搜索框使用
            // search:params.search
        };
        return map;
    };
    return oTableInit;
}


/**
 * 物料名称格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function edit_wlmcformat(value,row,index){
    var html="";
    if(!row.wlmc){
        if(row.fwmc == null){
            row.fwmc = "";
        }
        html = "<input id='fwmc_"+index+"' value='"+row.fwmc+"' text='"+row.fwmc+"' name='fwmc_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'fwmc\')\" required='required' autocomplete='off'></input>";
    }else{
        html=row.wlmc;
    }
    return html;
}

function edit_xmhformat(value,row,index){
    var html = "<textarea id='xmh_"+index+"'row='3' value='"+row.xmh+"' text='"+row.xmh+"' name='xmh_"+index+"'  style='height: auto;resize: vertical;' onblur=\"checkSum('"+index+"',this,\'xmh\')\" class='form-control'>"+row.xmh+"</textarea>";
    return html;
}

function edit_jszbformat(value,row,index){
    if(row.jszb == null){
        row.jszb = "";
    }
    var html = "<textarea id='jszb_"+index+"'row='3' value='"+row.jszb+"' text='"+row.jszb+"' name='jszb_"+index+"'  style='height: auto;resize: vertical;' onblur=\"checkSum('"+index+"',this,\'jszb\')\" class='form-control'>"+row.jszb+"</textarea>";
    return html;
}

function edit_zlyqformat(value,row,index){
    if(row.zlyq == null){
        row.zlyq = "";
    }
    var html = "<textarea id='zlyq_"+index+"'row='3' value='"+row.zlyq+"' text='"+row.zlyq+"' name='zlyq_"+index+"' style='height: auto;resize: vertical;' onblur=\"checkSum('"+index+"',this,\'zlyq\')\" class='form-control'>"+row.zlyq+"</textarea>";
    return html;
}


/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function edit_czformat(value,row,index){
    var html="";
    html = "<span style='margin-left:5px;' class='btn btn-default' title='删除' onclick=\"deleteDetail('" + index + "')\" >删除</span>";
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
    if (zdmc == 'fwmc') {
        t_map.rows[index].fwmc = e.value;
    }else if (zdmc == 'jszb') {
        t_map.rows[index].jszb = e.value;
    }else if (zdmc == 'zlyq') {
        t_map.rows[index].zlyq = e.value;
    }else if (zdmc == 'xmh') {
        t_map.rows[index].xmh = e.value;
    }
}

/**
 * 删除操作
 * @param index
 * @param event
 * @returns
 */
function deleteDetail(index){
    t_map.rows.splice(index,1);
    $("#evaluationEditForm #edit_list").bootstrapTable('load',t_map);
}

/**
 * 选择供应商列表
 * @returns
 */
function chooseGys(){
    var url=$('#evaluationEditForm #urlPrefix').val() + "/warehouse/supplier/pagedataSupplier?access_token=" + $("#ac_tk").val();
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
                    $("#evaluationEditForm #gfmc").val(sel_row[0].gysmc);
                    $("#evaluationEditForm #gysid").val(sel_row[0].gysid);
                    $("#evaluationEditForm #gfbh").val(sel_row[0].gfbh);
                    $("#evaluationEditForm #dh").val(sel_row[0].dh);
                    $("#evaluationEditForm #dz").val(sel_row[0].dq);
                    $("#evaluationEditForm #lxr").val(sel_row[0].lxr);
                    $("#evaluationEditForm #cz").val(sel_row[0].cz);
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
function chooseSqr(){
    var url=$('#evaluationEditForm #urlPrefix').val() + "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
    $.showDialog(url,'选择负责人',addSqrConfig);
}
var addSqrConfig = {
    width		: "800px",
    modalName	:"addSqrrModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    $('#evaluationEditForm #sqr').val(sel_row[0].yhid);
                    $('#evaluationEditForm #sqrmc').val(sel_row[0].zsxm);
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

function chooseShr(){
    var url=$('#evaluationEditForm #urlPrefix').val() + "/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
    $.showDialog(url,'选择审核人',addShrConfig);
}
var addShrConfig = {
    width		: "800px",
    modalName	:"addShrModal",
    offAtOnce	: false,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var xzrys = $('#taskListFzrForm #xzrys').val();
                if(xzrys!=null&&xzrys!=""){
                    var sel_row = JSON.parse(xzrys);
                    var ids="";
                    var mcs="";
                    for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                        ids = ids + ","+ sel_row[i].yhid;
                        mcs = mcs + ","+ sel_row[i].zsxm;
                    }
                    ids = ids.substr(1);
                    mcs = mcs.substr(1);
                    $('#evaluationEditForm #sybmshr').val(ids);
                    $('#evaluationEditForm #sybmshrmc').val(mcs);
                }else{
                    $.error("请至少选中一行");
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

function addWl() {
    var url = $("#evaluationEditForm #urlPrefix").val()+"/agreement/agreement/pagedataListMaterial?access_token=" + $("#ac_tk").val();
    $.showDialog(url, '选择物料', chooseWlConfig);
}
var chooseWlConfig = {
    width : "1200px",
    height : "500px",
    modalName	: "chooseWlModal",
    formName	: "chooseMaterialForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#chooseMaterialForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var sel = $("#chooseMaterialForm #yxwl").tagsinput('items');
                var ids="";
                for(var i = 0; i < sel.length; i++){
                    var value = sel[i].value;
                    ids+=","+value;
                }
                //如果有选中数据
                if(ids){
                    ids=ids.substring(1);
                    $.ajax({
                        async:false,
                        url: $('#evaluationEditForm #urlPrefix').val()+'/agreement/agreement/pagedataGetMaterialDetails',
                        type: 'post',
                        dataType: 'json',
                        data : {"ids":ids, "access_token":$("#ac_tk").val()},
                        success: function(data) {
                            if(data.wlglDtos.length>0){
                                for (var i = 0; i < data.wlglDtos.length; i++) {
                                    var isFind=false;
                                    for (var j = 0; j < t_map.rows.length; j++) {
                                        if(data.wlglDtos[i].wlid==t_map.rows[j].wlid){
                                            isFind=true;
                                            break;
                                        }
                                    }
                                    if(!isFind){
                                        var sz = {"wlid":'',"wlbm":'',"wlmc":'',"gg":'',"fwmc":'',"xmh":'',"jszb":'',"zlyq":''};
                                        sz.wlid = data.wlglDtos[i].wlid;
                                        sz.wlmc = data.wlglDtos[i].wlmc;
                                        sz.wlbm = data.wlglDtos[i].wlbm;
                                        sz.gg = data.wlglDtos[i].gg;
                                        sz.jszb = data.wlglDtos[i].jszb;
                                        sz.zlyq = data.wlglDtos[i].zlyq;
                                        sz.xmh = data.wlglDtos[i].sjxmhmc;
                                        t_map.rows.push(sz);
                                    }
                                }
                            }
                        }
                    });
                }
                $("#evaluationEditForm #edit_list").bootstrapTable('load',t_map);
                $.closeModal(opts.modalName);
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
 * 新增操作
 * @param index
 * @param event
 * @returns
 */
function addFw(){
    var sz = {"wlid":'',"wlbm":'',"wlmc":'',"gg":'',"fwmc":'',"xmh":'',"jszb":'',"zlyq":''};
    t_map.rows.push(sz);
    $("#evaluationEditForm #edit_list").bootstrapTable('load',t_map);
}


$(function(){
    //添加日期控件
    laydate.render({
        elem: '#evaluationEditForm #yzsqsj'
        ,theme: '#2381E9'
    });
    laydate.render({
            elem: '#evaluationEditForm #yzshsj'
            ,theme: '#2381E9'
        });
    var oTable_t=new editEvaluation_TableInit();
    oTable_t.Init();
})
