var t_map=[];
//培训题库选择
var columnsArray = [
    {
        title: '序号',
        formatter: function (value, row, index) {
            return index+1;
        },
        titleTooltip:'序号',
        width: '5%',
        align: 'left',
        visible:true
    },{
        field: 'tkmc',
        title: '题库',
        width: '33%',
        align: 'left',
        formatter:tkmcformat,
        visible: true
    },{
        field: 'tmlxmc',
        title: '题目类型',
        width: '15%',
        align: 'left',
        formatter:tmlxformat,
        visible: true
    }, {
        field: 'sl',
        title: '数量',
        width: '20%',
        align: 'left',
        formatter:slformat,
        visible: true
    },{
        field: 'fz',
        title: '每题分值',
        width: '15%',
        align: 'left',
        formatter:fzformat,
        visible: true
    },{
        field: 'cz',
        title: '操作',
        width: '10%',
        align: 'left',
        formatter:czformat,
        visible: true
    }]

var generateTestPapers_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#generatetestpapersForm #tb_list").bootstrapTable({
            url:   $('#generatetestpapersForm #urlPrefix').val()+'/train/question/pagedataListChoose',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#generatetestpapersForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            paginationShowPageGo: false,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: 'xh',				//排序字段
            sortOrder: "asc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: false,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: null,                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            paginationDetailHAlign:' hidden',
            columns: columnsArray,
            onLoadSuccess:function(map){
                t_map = map;
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
            pageSize: 1,   // 页面大小
            pageNumber:  1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "pxid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getQuestionSearchData(map);
    };
    return oTableInit;
}
/**
 * 组装列表查询条件(未使用)
 * @param map
 * @returns
 */
function getQuestionSearchData(map){
    return map;
}
function searchQuestionResult(isTurnBack){
    if(isTurnBack){
        $('#generatetestpapersForm #tb_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#generatetestpapersForm #tb_list').bootstrapTable('refresh');
    }
    //所有下拉框添加choose样式
    jQuery('#tb_list .chosen-select').chosen();
}
//新增明细
function addnew(){
    var json ={"index":t_map.rows.length,"tkmc":'',"tmlx":'',"sl":'',"zsl":'',"fz":''};
    t_map.rows.push(json);
    $("#generatetestpapersForm #tb_list").bootstrapTable('load',t_map);
    //所有下拉框添加choose样式
    jQuery('#tb_list .chosen-select').chosen();

}
/**
 * 题库名称格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function tkmcformat(value,row,index){
    var tklistJson = $("#generatetestpapersForm #tklist").val();
    var tklist = JSON.parse(tklistJson);
    var tkid=row.tkid;
    var html="";
    html += "<select id='tkmc_"+index+"' name='tkmc_"+index+"' class='form-control chosen-select'  onchange=\"checkSum('"+index+"',this,\'tkmc\')\">";
    html += "<option value=''>--请选择--</option>";
    for(var i = 0; i < tklist.length; i++) {
        html += "<option id='" + tklist[i].tkid + "' value='" + tklist[i].tkid + "'";
        if (tkid != null && tkid != "") {
            if (tkid == tklist[i].tkid) {
                html += "selected"
            }
        }
        html += ">" + tklist[i].tkmc + "</option>";
    }
    html +="</select>";
    return html;
}

/**
 * 题目类型格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function tmlxformat(value,row,index){
    var html="";
    html +=  "<select id='tmlx_"+index+"' name='tmlx_"+index+"' class='form-control chosen-select'  onchange=\"checkSum('"+index+"',this,\'tmlx\')\">";
    //父节点数据
    html += "<option value=''>--请选择--</option>";
    if(row.tmlx=='SELECT'){
        html += "<option value='SELECT' selected>单选题</option>";
        html += "<option value='MULTIPLE'>多选题</option>";
        html += "<option value='EXPLAIN'>简答题</option>";
        html += "<option value='GAP'>填空题</option>";
        html += "<option value='JUDGE'>判断题</option>";
    }else if(row.tmlx=='MULTIPLE'){
        html += "<option value='SELECT'>单选题</option>";
        html += "<option value='MULTIPLE' selected>多选题</option>";
        html += "<option value='EXPLAIN'>简答题</option>";
        html += "<option value='GAP'>填空题</option>";
        html += "<option value='JUDGE'>判断题</option>";
    }else if(row.tmlx=='EXPLAIN'){
        html += "<option value='SELECT'>单选题</option>";
        html += "<option value='MULTIPLE' >多选题</option>";
        html += "<option value='EXPLAIN' selected>简答题</option>";
        html += "<option value='GAP'>填空题</option>";
        html += "<option value='JUDGE'>判断题</option>";
    }else if(row.tmlx=='GAP'){
        html += "<option value='SELECT'>单选题</option>";
        html += "<option value='MULTIPLE' >多选题</option>";
        html += "<option value='EXPLAIN'>简答题</option>";
        html += "<option value='GAP'  selected>填空题</option>";
        html += "<option value='JUDGE'>判断题</option>";
    }else if(row.tmlx=='JUDGE'){
        html += "<option value='SELECT'>单选题</option>";
        html += "<option value='MULTIPLE' >多选题</option>";
        html += "<option value='EXPLAIN'>简答题</option>";
        html += "<option value='GAP'>填空题</option>";
        html += "<option value='JUDGE'  selected>判断题</option>";
    }else{
        html += "<option value='SELECT'>单选题</option>";
        html += "<option value='MULTIPLE'>多选题</option>";
        html += "<option value='EXPLAIN'>简答题</option>";
        html += "<option value='GAP'>填空题</option>";
        html += "<option value='JUDGE'>判断题</option>";
    }
    html += "</select>";
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
    var html = "<input id='sl_"+index+"' type='number'  value='"+row.sl+"' text='"+row.sl+"' name='sl_"+index+"' style='width:60%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'sl\')\" autocomplete='off'/>&nbsp;&nbsp;/&nbsp;&nbsp;<input id='zsl_"+index+"'text='"+row.zsl+"'  value='"+row.zsl+"'disabled style='color: red;width: 30%;border: 0px;'></input>";
    return html;
}

/**
 * 分值格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function fzformat(value,row,index){
    if(row.fz == null){
        row.fz = "";
    }
    var html = "<input id='fz_"+index+"' type='number' value='"+row.fz+"' text='"+row.fz+"' name='fz_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'fz\')\" autocomplete='off'></input>";
    return html;
}

/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function czformat(value,row,index){
    var html = "<span style='margin-left:5px;' class='btn btn-default' title='移出' onclick=\"delQuestionBank('" + index + "',event)\" >删除</span>";
    return html;
}
/**
 * 删除操作
 * @param index
 * @param event
 * @returns
 */
function delQuestionBank(index,event,id){
    var sl= parseInt(t_map.rows[index].sl);
    var fz= parseInt(t_map.rows[index].fz);
    var zf = sl*fz
    var cszf = Number($("#generatetestpapersForm #cszf").text())
    if(cszf && zf){
        cszf = cszf-zf;
        $("#generatetestpapersForm #cszf").text(cszf)
    }
    t_map.rows.splice(index,1);
    $("#generatetestpapersForm #tb_list").bootstrapTable('load',t_map);
    //所有下拉框添加choose样式
    jQuery('#tb_list .chosen-select').chosen();

}
/**
 * 失去焦点事件(修改列表参数)
 * @param id
 * @param e
 * @param zdmc
 * @param bj
 * @returns
 */
function checkSum(index,e,zdmc){
    var tkid = $("#generatetestpapersForm #tkmc_"+index).val();
    var tmlx = $("#generatetestpapersForm #tmlx_"+index).val();
    $.ajax({
        url :  $('#generatetestpapersForm #urlPrefix').val()+'/train/question/pagedataCountInfo',
        type : 'post',
        data : {
            "tkid" :tkid,
            "tmlx": tmlx,
            "access_token" : $("#ac_tk").val()
        },
        dataType : 'json',
        success : function(result) {
            $("#generatetestpapersForm #zsl_"+index).val(result.count);
            var s = result.count.toString();
            var zf=0;
            for(var i=0;i<t_map.rows.length;i++){
                if(t_map.rows[i].index == index){
                    if(zdmc=='tkmc'){
                        t_map.rows[i].tkid=e.value;
                        t_map.rows[i].zsl=s;
                    }else if(zdmc=='tmlx'){
                        t_map.rows[i].tmlx=e.value;
                        t_map.rows[i].zsl=s;
                    }else if(zdmc=='sl'){
                        t_map.rows[i].sl=e.value;
                    }else if(zdmc=='fz'){
                        t_map.rows[i].fz=e.value;
                    }
                }
                var sl= parseInt(t_map.rows[i].sl);
                var fz= parseInt(t_map.rows[i].fz);
                if (sl && fz){
                    zf=parseInt(zf)+(sl*fz);
                }
            }
            if(zf){
                $("#generatetestpapersForm #cszf").text(zf);
            }
        }
    });
}
$(document).ready(function(){
    var oTable=new generateTestPapers_TableInit();
    oTable.Init();
});