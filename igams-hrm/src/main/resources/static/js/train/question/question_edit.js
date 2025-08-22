
var t_map=[];
//行政请购类型显示字段
var columnsArray = [
    {
        title: '序号',
        formatter: function (value, row, index) {
            return index+1;
        },
        titleTooltip:'序号',
        width: '2%',
        align: 'left',
        visible:true
    },{
        field: 'ksid',
        title: '考试ID',
        width: '5%',
        align: 'left',
        sortable: false,
        visible: false
    },{
        field: 'tmnr',
        title: '题目内容',
        width: '20%',
        align: 'left',
        formatter:tmnrformat,
        sortable: false,
        visible: true
    },{
        field: 'tmlxmc',
        title: '类型',
        width: '5%',
        align: 'left',
        formatter:tmlxformat,
        sortable: false,
        visible: true
    },{
        field: 'da',
        title: '答案',
        width: '3%',
        align: 'left',
        formatter:daformat,
        sortable: false,
        visible: true
    },{
        field: 'dajx',
        title: '答案解析',
        width: '5%',
        align: 'left',
        formatter:dajxformat,
        sortable: false,
        visible: true
    },{
        field: 'xxA',
        title: '选项A',
        width: '5%',
        align: 'left',
        formatter:xxAformat,
        sortable: false,
        visible: true
    },{
        field: 'xxB',
        title: '选项B',
        width: '5%',
        align: 'left',
        formatter:xxBformat,
        sortable: false,
        visible: true
    },{
        field: 'xxC',
        title: '选项C',
        width: '5%',
        align: 'left',
        formatter:xxCformat,
        sortable: false,
        visible: true
    },{
        field: 'xxD',
        title: '选项D',
        width: '5%',
        align: 'left',
        formatter:xxDformat,
        sortable: false,
        visible: true
    },{
        field: 'xxE',
        title: '选项E',
        width: '5%',
        align: 'left',
        sortable: false,
        formatter:xxEformat,
        visible: true
    },{
        field: 'xxF',
        title: '选项F',
        width: '5%',
        align: 'left',
        sortable: false,
        formatter:xxFformat,
        visible: true
    },{
        field: 'xxG',
        title: '选项G',
        width: '5%',
        align: 'left',
        formatter:xxGformat,
        sortable: false,
        visible: true
    },{
        field: 'xxH',
        title: '选项H',
        width: '5%',
        align: 'left',
        formatter:xxHformat,
        sortable: false,
        visible: true
    },{
        field: 'cz',
        title: '操作',
        width: '3%',
        align: 'left',
        formatter:czformat,
        visible: true
    }]

var QuestionBank_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#editQuestionForm #tb_list").bootstrapTable({
            url:   $('#editQuestionForm #urlPrefix').val()+$('#editQuestionForm #url').val()+$("#ac_tk").val(),         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#editQuestionForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: false,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "ksgl.xh",				//排序字段
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
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: false,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "ksid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            paginationDetailHAlign:' hidden',
            columns: columnsArray,
            onLoadSuccess:function(map){
                t_map=map;
                // if(map.rows.length<=0){
                //     var a=[];
                //     t_map.rows.push(a);
                //     $("#editQuestionForm #tb_list").bootstrapTable('load',t_map);
                // }
                if(t_map.rows!=null) {
                    var json = [];
                    for (var i = 0; i < t_map.rows.length; i++) {
                        var sz = {"index": '', "ksid": '', "da": ''};
                        sz.index = t_map.rows.length;
                        sz.ksid = t_map.rows[i].ksid;
                        sz.da = t_map.rows[i].da;
                        json.push(sz);
                    }
                    $("#editQuestionForm #xgqtk").val(JSON.stringify(json));
                    var json_t=[];
                    for(var i=0;i<t_map.rows.length;i++){
                        var sz={"index":i,"ksid":'',"tmnr":'',"tmlx":'',"da":'',"xxA":'',"xxB":'',"xxC":'',"xxD":'','xxE':'','xxF':'',"xxG":'',"xxH":'','dajx':''};
                        sz.ksid=t_map.rows[i].ksid;
                        sz.tmnr=t_map.rows[i].tmnr;
                        sz.tmlx=t_map.rows[i].tmlx;
                        sz.da=t_map.rows[i].da;
                        sz.xxA=t_map.rows[i].xxA;
                        sz.xxB=t_map.rows[i].xxB;
                        sz.xxC=t_map.rows[i].xxC;
                        sz.xxD=t_map.rows[i].xxD;
                        sz.xxE=t_map.rows[i].xxE;
                        sz.xxF=t_map.rows[i].xxF;
                        sz.xxG=t_map.rows[i].xxG;
                        sz.xxH=t_map.rows[i].xxH;
                        sz.dajx=t_map.rows[i].dajx;
                        json_t.push(sz);
                    }
                    $("#editQuestionForm #tmmx_json").val(JSON.stringify(json_t));
                }
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            },
        });
        $("#editQuestionForm #tb_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true
        })
    };
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "ksgl.ksid", // 防止同名排位用
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
        $('#editQuestionForm #tb_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#editQuestionForm #tb_list').bootstrapTable('refresh');
    }
}
//新增明细
function addnew(){
    var data=JSON.parse($("#editQuestionForm #tmmx_json").val());
    var a={"index":data.length,"ksid":'',"tmnr":'',"tmlx":'',"fs":'',"da":'',"xxA":'',"xxB":'',"xxC":'',"xxD":'','xxE':'','xxF':'',"xxG":'',"xxH":''};
    t_map.rows.push(a);
    data.push(a);
    $("#editQuestionForm #tmmx_json").val(JSON.stringify(data));
    $("#editQuestionForm #tb_list").bootstrapTable('load',t_map);
}

/**
* 题目内容格式化
* @param value
* @param row
* @param index
* @returns
*/
function tmnrformat(value,row,index){
    if(row.tmnr == null){
        row.tmnr = "";
    }
    var html = "<input id='tmnr_"+index+"'value='"+row.tmnr+"' text='"+row.tmnr+"' name='tmnr_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'tmnr\')\"  autocomplete='off'></input>";
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
    html +=  "<select id='tmlx_"+index+"'   name='tmlx_"+index+"' class='form-control chosen-select'  onchange=\"checkSum('"+index+"',this,\'tmlx\')\">";
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
 * 答案格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function daformat(value,row,index){
    if(row.da == null){
        row.da = "";
    }
    var html = "<input id='da_"+index+"'  value='"+row.da+"' text='"+row.da+"' name='da_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'da\')\"  autocomplete='off'></input>";
    return html;
}

/**
 * 答案解析格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function dajxformat(value,row,index){
    if(row.dajx == null){
        row.dajx = "";
    }
    var html = "<input id='dajx_"+index+"'  value='"+row.dajx+"' text='"+row.dajx+"' name='dajx_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'dajx\')\" autocomplete='off'></input>";
    return html;
}
/**
 * 选项A格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function xxAformat(value,row,index){
    if(row.xxA == null){
        row.xxA = "";
    }
    var html = "<input id='xxA_"+index+"' value='"+row.xxA+"' text='"+row.xxA+"' name='xxA_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'xxA\')\" autocomplete='off'></input>";
    return html;
}
/**
 * 选项B格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function xxBformat(value,row,index){
    if(row.xxB == null){
        row.xxB = "";
    }
    var html = "<input id='xxB_"+index+"' value='"+row.xxB+"' text='"+row.xxB+"' name='xxB_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'xxB\')\" autocomplete='off'></input>";
    return html;
}
/**
 * 选项C格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function xxCformat(value,row,index){
    if(row.xxC == null){
        row.xxC = "";
    }
    var html = "<input id='xxC_"+index+"' value='"+row.xxC+"' text='"+row.xxC+"' name='xxC_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'xxC\')\" autocomplete='off'></input>";
    return html;
}
/**
 * 选项D格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function xxDformat(value,row,index){
    if(row.xxD == null){
        row.xxD = "";
    }
    var html = "<input id='xxD_"+index+"' value='"+row.xxD+"' text='"+row.xxD+"' name='xxD_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'xxD\')\" autocomplete='off'></input>";
    return html;
}
/**
 * 选项E格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function xxEformat(value,row,index){
    if(row.xxE == null){
        row.xxE = "";
    }
    var html = "<input id='xxE_"+index+"' value='"+row.xxE+"' text='"+row.xxE+"' name='xxE_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'xxE\')\" autocomplete='off'></input>";
    return html;
}
/**
 * 选项F格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function xxFformat(value,row,index){
    if(row.xxF == null){
        row.xxF = "";
    }
    var html = "<input id='xxF_"+index+"' value='"+row.xxF+"' text='"+row.xxF+"' name='xxF_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'xxF\')\" autocomplete='off'></input>";
    return html;
}
/**
 * 选项G格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function xxGformat(value,row,index){
    if(row.xxG == null){
        row.xxG = "";
    }
    var html = "<input id='xxG_"+index+"' value='"+row.xxG+"' text='"+row.xxG+"' name='xxG_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'xxG\')\" autocomplete='off'></input>";
    return html;
}
/**
 * 选项H格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function xxHformat(value,row,index){
    if(row.xxH == null){
        row.xxH = "";
    }
    var html = "<input id='xxH_"+index+"' value='"+row.xxH+"' text='"+row.xxH+"' name='xxH_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'xxH\')\" autocomplete='off'></input>";
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
 * 删除操作(从采购车删除)
 * @param index
 * @param event
 * @returns
 */
function delQuestionBank(index,event,id){
    var t_data = JSON.parse($("#editQuestionForm #tmmx_json").val());
    t_map.rows.splice(index,1);
    for(var i=0;i<t_data.length;i++){
        if(t_data[i].index == index){
            t_data.splice(i,1);
        }
    }
    $("#editQuestionForm #tb_list").bootstrapTable('load',t_map);
    var json=[];
    for(var i=0;i<t_data.length;i++){
        var sz={"index":i,"ksid":'',"tmnr":'',"tmlx":'',"da":'',"xxA":'',"xxB":'',"xxC":'',"xxD":'','xxE':'','xxF':'',"xxG":'',"xxH":'','dajx':''};
        sz.ksid=t_data[i].ksid;
        sz.tmnr=t_data[i].tmnr;
        sz.tmlx=t_data[i].tmlx;
        sz.da=t_data[i].da;
        sz.xxA=t_data[i].xxA;
        sz.xxB=t_data[i].xxB;
        sz.xxC=t_data[i].xxC;
        sz.xxD=t_data[i].xxD;
        sz.xxE=t_data[i].xxE;
        sz.xxF=t_data[i].xxF;
        sz.xxG=t_data[i].xxG;
        sz.xxH=t_data[i].xxH;
        sz.dajx=t_data[i].dajx;
        json.push(sz);
    }
    $("#editQuestionForm #tmmx_json").val(JSON.stringify(json));
}
/**
 * 初始化明细json字段
 * @returns
 */
function settmmx_json(){
    var json=[];
    var data=$('#editQuestionForm #tb_list').bootstrapTable('getData');//获取选择行数据
    for(var i=0;i<data.length;i++){
        var sz={"index":i,"ksid":'',"tmnr":'',"tmlx":'',"da":'',"xxA":'',"xxB":'',"xxC":'',"xxD":'','xxE':'','xxF':'',"xxG":'',"xxH":'','dajx':''};
        sz.ksid=data[i].ksid;
        sz.tmnr=data[i].tmnr;
        sz.tmlx=data[i].tmlx;
        sz.da=data[i].da;
        sz.xxA=data[i].xxA;
        sz.xxB=data[i].xxB;
        sz.xxC=data[i].xxC;
        sz.xxD=data[i].xxD;
        sz.xxE=data[i].xxE;
        sz.xxF=data[i].xxF;
        sz.xxG=data[i].xxG;
        sz.xxH=data[i].xxH;
        sz.dajx=data[i].dajx;
        json.push(sz);
    }
    $("#editQuestionForm #tmmx_json").val(JSON.stringify(json));
}
function btnBind(){
    setTimeout(function() {
        settmmx_json();
    }, 1000);
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
    var data=JSON.parse($("#editQuestionForm #tmmx_json").val());
    for(var i=0;i<data.length;i++){
        if(data[i].index == index){
            if(zdmc=='tmnr'){
                data[i].tmnr=e.value;
                t_map.rows[i].tmnr=e.value;
            }else if(zdmc=='tmlx'){
                data[i].tmlx=e.value;
                t_map.rows[i].tmlx=e.value;
            }else if(zdmc=='da'){
                data[i].da=e.value;
                t_map.rows[i].da=e.value;
            }else if(zdmc=='dajx'){
                data[i].dajx=e.value;
                t_map.rows[i].dajx=e.value;
            }else if(zdmc=='xxA'){
                data[i].xxA=e.value;
                t_map.rows[i].xxA=e.value;
            }else if(zdmc=='xxB'){
                data[i].xxB=e.value;
                t_map.rows[i].xxB=e.value;
            }else if(zdmc=='xxD'){
                data[i].xxD=e.value;
                t_map.rows[i].xxD=e.value;
            }else if(zdmc=='xxC'){
                data[i].xxC=e.value;
                t_map.rows[i].xxC=e.value;
            }else if(zdmc=='xxE'){
                data[i].xxE=e.value;
                t_map.rows[i].xxE=e.value;
            }else if(zdmc=='xxF'){
                data[i].xxF=e.value;
                t_map.rows[i].xxF=e.value;
            }else if(zdmc=='xxG'){
                data[i].xxG=e.value;
                t_map.rows[i].xxG=e.value;
            }else if(zdmc=='xxH'){
                data[i].xxH=e.value;
                t_map.rows[i].xxH=e.value;
            }
        }
    }
    $("#editQuestionForm #tmmx_json").val(JSON.stringify(data));
}


$(function(){
    var oTable=new QuestionBank_TableInit();
    oTable.Init();
    btnBind();
    jQuery('#editQuestionForm .chosen-select').chosen({width: '100%'});
})