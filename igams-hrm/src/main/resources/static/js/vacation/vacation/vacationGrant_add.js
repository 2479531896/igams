
var t_map=[];
//行政请购类型显示字段
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
        field: 'jqlx',
        title: '假期类型',
        width: '55%',
        align: 'left',
        formatter:jqlxformat,
        visible: true
    },{
        field: 'sc',
        title: '时长',
        width: '30%',
        align: 'left',
        formatter:scformat,
        visible: true
    },{
        field: 'cz',
        title: '操作',
        width: '5%',
        align: 'left',
        formatter:czformat,
        visible: true
    }]

var GrantXZ_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#vacationGrant_Form #tb_list").bootstrapTable({
            url:  $("#vacationGrant_Form #urlPrefix").val()+ '/vacation/vacation/pagedataOnloadMx?access_token='+$("#ac_tk").val(),
            method: 'get',                      //请求方式（*）
            toolbar: '#vacationGrant_Form #toolbar',                //工具按钮用哪个容器
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
                t_map=map;
                var a={"jqlx":'',"sc":''};
                t_map.rows.push(a);
                $("#vacationGrant_Form #tb_list").bootstrapTable('load',t_map);
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            },
        });
    };
    return oTableInit;
}
/**
 * 题库名称格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function jqlxformat(value,row,index){
    var jqlxlist = JSON.parse($("#vacationGrant_Form #jqlxlist_t").val());
    var html="";
    var jqlx=row.jqlx;
    html += "<select id='jqlx_"+index+"' name='jqlx_"+index+"' class='form-control chosen-select'  onchange=\"checkSum('"+index+"',this,\'jqlx\')\">";
    html += "<option value=''>--请选择--</option>";
    for(var i = 0; i < jqlxlist.length; i++) {
                html += "<option id='" + jqlxlist[i].csid + "' value='" + jqlxlist[i].csid + "'";
                if (jqlx != null && jqlx != "") {
                    if (jqlx == jqlxlist[i].csid) {
                        html += "selected"
                    }
                }
                html += ">" + jqlxlist[i].csmc + "</option>";
    }
    html +="</select>";
    return html;
}










function changedw() {
    var cskz1=$("#vacationGrant_Form #jqlx option:selected").attr("cskz1");
    $("#vacationGrant_Form #dw").val(cskz1)
}
function chooseXtyh() {
    var url = $("#vacationGrant_Form #urlPrefix").val()+"/train/user/pagedataListUserForChoose?access_token=" + $("#ac_tk").val();
    $.showDialog(url, '选择用户 ', chooseYhConfig);
}
var chooseYhConfig = {
    width : "800px",
    height : "500px",
    modalName	: "chooseYhModal",
    formName	: "ajaxForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#userListForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var sel = $("#userListForm #yxyh").tagsinput('items');
                $("#vacationGrant_Form  #yhids").tagsinput({
                    itemValue: "value",
                    itemText: "text",
                });
                for(var i = 0; i < sel.length; i++){
                    var value = sel[i].value;
                    var text = sel[i].text;
                    $("#vacationGrant_Form  #yhids").tagsinput('add',{"value":value,"text":text});
                }
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

var sum=1;
//新增明细
function addFfxz(){
    var data=JSON.parse($("#vacationGrant_Form #jqxz_json").val());
    var a={"index":data.length,"jqlx":'',"sc":''};
    t_map.rows.push(a);
    data.push(a);
    $("#vacationGrant_Form #jqxz_json").val(JSON.stringify(data));
    $("#vacationGrant_Form #tb_list").bootstrapTable('load',t_map);
    sum ++;
}
/**
 * 时长格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function scformat(value,row,index){
    if(row.sc == null){
        row.sc = "";
    }
    var html = "<input id='sc_"+index+"'  value='"+row.sc+"' text='"+row.sc+"' name='sc_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onkeyup=\"this.value= this.value.match(/\\d+(\\.\\d{0,2})?/) ? this.value.match(/\\d+(\\.\\d{0,2})?/)[0] : ''\" onblur=\"checkSum('"+index+"',this,\'sc\')\" autocomplete='off'></input>";
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
    var html = "<span style='margin-left:5px;' class='btn btn-default' title='移出' onclick=\"delGrantBank('" + index + "',event)\" >删除</span>";
    return html;
}
/**
 * 删除操作
 * @param index
 * @param event
 * @returns
 */
function delGrantBank(index,event,id){
    var t_data = JSON.parse($("#vacationGrant_Form #jqxz_json").val());
    t_map.rows.splice(index,1);
    for(var i=0;i<t_data.length;i++){
        if(t_data[i].index == index){
            t_data.splice(i,1);
        }
    }
    $("#vacationGrant_Form #tb_list").bootstrapTable('load',t_map);
    var json=[];
    for(var i=0;i<t_data.length;i++){
        var sz={"index":i,"jqlx":'',"sc":''};
        sz.jqlx=t_data[i].jqlx;
        sz.sc=t_data[i].sc;
        json.push(sz);
    }
    sum=sum-1;
    $("#vacationGrant_Form #jqxz_json").val(JSON.stringify(json));
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
    var data=JSON.parse($("#vacationGrant_Form #jqxz_json").val());
    for(var i=0;i<data.length;i++){
        if(data[i].index == index){
            if(zdmc=='jqlx'){
                data[i].jqlx=e.value;
                t_map.rows[i].jqlx=e.value;
            }else if(zdmc=='sc'){
                data[i].sc=e.value;
                t_map.rows[i].sc=e.value;
            }
        }
    }
    $("#vacationGrant_Form #jqxz_json").val(JSON.stringify(data));
}
/**
 * 初始化明细json字段
 * @returns
 */
function settmmx_json(){
    var json=[];
    var data=$('#vacationGrant_Form #tb_list').bootstrapTable('getData');//获取选择行数据
    for(var i=0;i<data.length;i++){
        var sz={"index":i,"jqlx":'',"sc":''};
        sz.jqlx=data[i].jqlx;
        sz.sc=data[i].sc;
        json.push(sz);
    }
    $("#vacationGrant_Form #jqxz_json").val(JSON.stringify(json));
}
function btnBind(){
    setTimeout(function() {
        settmmx_json();
    }, 1000);
}
$(function(){
    var oTable=new GrantXZ_TableInit();
    oTable.Init();
    btnBind();
    //添加日期控件
    laydate.render({
        elem: '#vacationGrant_Form #kssj'
        ,theme: '#2381E9'
    });
    //添加日期控件
    laydate.render({
        elem: '#vacationGrant_Form #jssj'
        ,theme: '#2381E9'
    });
    laydate.render({
        elem: '#vacationGrant_Form #jzrq'
        ,theme: '#2381E9',
        format:'MM-dd'
    });
    // 所有下拉框添加choose样式
    jQuery('#vacationGrant_Form .chosen-select').chosen({width: '100%'});
})