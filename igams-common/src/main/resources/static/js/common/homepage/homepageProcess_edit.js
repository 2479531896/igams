var t_map = [];
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
        field: 'lcxmid',
        title: '流程明细ID',
        titleTooltip:'流程明细ID',
        width: '16%',
        align: 'left',
        visible: false
    }, {
        field: 'xsmc',
        title: '显示名称',
        titleTooltip:'显示名称',
        width: '16%',
        align: 'left',
        formatter:xsmcformat,
        visible: true
    }, {
        field: 'mkid',
        title: '模块',
        titleTooltip:'模块',
        width: '16%',
        align: 'left',
        formatter:mkidformat,
        visible: true
    },{
        field: 'zyid',
        title: '菜单',
        titleTooltip:'菜单',
        width: '16%',
        align: 'left',
        formatter:zyidformat,
        visible: true
    },{
        field: 'tzcs',
        title: '跳转参数',
        titleTooltip:'跳转参数',
        width: '16%',
        align: 'left',
        formatter:tzcsformat,
        visible: true
    },{
        field: 'cz',
        title: '操作',
        titleTooltip:'操作',
        width: '6%',
        align: 'left',
        formatter:czformat,
        visible: true
    }];
var editHomepageProcess_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#editHomepageProcessForm #tb_list').bootstrapTable({
            url: '/process/process/pagedataGetProcessDetailList',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#editHomepageProcessForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "xh",				//排序字段
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
            uniqueId: "lcxmid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
            onLoadSuccess: function (map) {
                t_map = map;
                if(t_map.rows == null || t_map.rows.length == 0){
                    var a={"lcmxid":'',"xsmc":'',"mkid":'',"zyid":'',"tzcs":''};
                    t_map.rows.push(a);
                    $("#editHomepageProcessForm #tb_list").bootstrapTable('load',t_map);
                }
                jQuery('#tb_list .chosen-select').chosen();
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
            sortLastName: "lcxmid", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            lcid: $("#editHomepageProcessForm #lcid").val()
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};

function xsmcformat(value,row,index){
    if(row.xsmc == null){
        row.xsmc = "" ;
    }
    var html="<input id='xsmc_"+index+"' name='xsmc_"+index+"' value='"+row.xsmc+"'  style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'xsmc\')\" autocomplete='off'></input>";
    return html;
}

/**
 * 模块格式化
 */
function mkidformat(value,row,index){
    var json = $("#editHomepageProcessForm #mklist").val();
    var mklist = JSON.parse(json);
    var mkid=row.mkid;
    var html="";
    html += "<select id='mkid_"+index+"' name='mkid_"+index+"' class='form-control chosen-select'  onchange=\"checkSum('"+index+"',this,\'mkid\')\">";
    html += "<option value=''>--请选择--</option>";
    for(var i = 0; i < mklist.length; i++) {
        html += "<option id='" + mklist[i].zyid + "' value='" + mklist[i].zyid + "'";
        if (mkid) {
            if (mkid == mklist[i].zyid) {
                html += "selected"
            }
        }
        html += ">" + mklist[i].zybt + "</option>";
    }
    html +="</select>";
    return html;
}
/**
 * 菜单格式化
 */
function zyidformat(value,row,index){
    var html="";
    html += "<select id='zyid_"+index+"' name='zyid_"+index+"' class='form-control chosen-select'  onchange=\"checkSum('"+index+"',this,\'zyid\')\">";
    html += "<option value=''>--请选择--</option>";
    if(row.mkid){
        var json = $("#editHomepageProcessForm #menulist").val();
        var menulist = JSON.parse(json);
        if(menulist){
            for(var i = 0; i < menulist.length; i++) {
                if(menulist[i].ffjd&&row.mkid==menulist[i].ffjd){
                    html += "<option id='" + menulist[i].zyid + "' value='" + menulist[i].zyid + "'";
                    if (row.zyid) {
                        if (row.zyid == menulist[i].zyid) {
                            html += "selected";
                        }
                    }
                    html += ">" + menulist[i].zybt + "</option>";
                }
            }
        }
    }
    html += "</select>";
    return html;
}

/**
 * 跳转参数格式化
 */
function tzcsformat(value,row,index){
    var html="";
    html += "<select id='tzcs_"+index+"' name='tzcs_"+index+"' class='form-control chosen-select'  onchange=\"checkSum('"+index+"',this,\'tzcs\')\">";
    html += "<option value=''>--请选择--</option>";
    if(row.zyid){
        var json = $("#editHomepageProcessForm #buttonlist").val();
        var buttonlist = JSON.parse(json);
        if(buttonlist){
            for(var i = 0; i < buttonlist.length; i++) {
                if(buttonlist[i].zyid&&row.zyid==buttonlist[i].zyid){
                    html += "<option id='" + buttonlist[i].czdm + "' value='" + buttonlist[i].czdm + "'";
                    if (row.tzcs) {
                        if (row.tzcs == buttonlist[i].czdm) {
                            html += "selected";
                        }
                    }
                    html += ">" + buttonlist[i].czmc + "</option>";
                }
            }
        }
    }
    html += "</select>";
    return html;
}

/**
 * 操作格式化
 */
function czformat(value,row,index){
    var html = "<span style='margin-left:5px;height:24px !important;' class='btn btn-danger' title='删除明细' onclick=\"delProcessDetail('" + index + "',event)\" >删除</span>";
    return html;
}

/**
 * 失去焦点事件(修改列表参数)
 */
function checkSum(index,e,zdmc){
    if(zdmc=='xsmc'){
        t_map.rows[index].xsmc=e.value;
    }else if(zdmc=='mkid'){
        t_map.rows[index].mkid=e.value;
        setSubMenu(index,e.value);
    }else if(zdmc=='zyid'){
        t_map.rows[index].zyid=e.value;
        setOperationButtons(index,e.value);
    }else if(zdmc=='tzcs'){
        t_map.rows[index].tzcs=e.value;
    }
}

function setSubMenu(index,zyid){
    if(zyid){
        var json = $("#editHomepageProcessForm #menulist").val();
        var menulist = JSON.parse(json);
        var html="";
        html += "<option value=''>--请选择--</option>";
        if(menulist){
            for(var i = 0; i < menulist.length; i++) {
                if(menulist[i].ffjd&&zyid==menulist[i].ffjd){
                    html += "<option id='" + menulist[i].zyid + "' value='" + menulist[i].zyid + "'";
                    html += ">" + menulist[i].zybt + "</option>";
                }
            }
        }
        $("#editHomepageProcessForm #zyid_"+index).empty();
        $("#editHomepageProcessForm #zyid_"+index).append(html);
        $("#editHomepageProcessForm #zyid_"+index).trigger("chosen:updated");
        var buttonHtml="";
        buttonHtml += "<option value=''>--请选择--</option>";
        $("#editHomepageProcessForm #tzcs_"+index).empty();
        $("#editHomepageProcessForm #tzcs_"+index).append(buttonHtml);
        $("#editHomepageProcessForm #tzcs_"+index).trigger("chosen:updated");
    }
}

function setOperationButtons(index,zyid){
    if(zyid){
        var json = $("#editHomepageProcessForm #buttonlist").val();
        var buttonlist = JSON.parse(json);
        var html="";
        html += "<option value=''>--请选择--</option>";
        if(buttonlist){
            for(var i = 0; i < buttonlist.length; i++) {
                if(buttonlist[i].zyid&&zyid==buttonlist[i].zyid){
                    html += "<option id='" + buttonlist[i].czdm + "' value='" + buttonlist[i].czdm + "'";
                    html += ">" + buttonlist[i].czmc + "</option>";
                }
            }
        }
        $("#editHomepageProcessForm #tzcs_"+index).empty();
        $("#editHomepageProcessForm #tzcs_"+index).append(html);
        $("#editHomepageProcessForm #tzcs_"+index).trigger("chosen:updated");
    }
}

/**
 * 删除操作
 */
function delProcessDetail(index,event){
    t_map.rows.splice(index,1);
    $("#editHomepageProcessForm #tb_list").bootstrapTable('load',t_map);
    jQuery('#tb_list .chosen-select').chosen();
}
function addnew(){
    var a={"lcmxid":'',"xsmc":'',"mkid":'',"zyid":'',"tzcs":''};
    t_map.rows.push(a);
    $("#editHomepageProcessForm #tb_list").bootstrapTable('load',t_map);
    jQuery('#tb_list .chosen-select').chosen();
}




$(document).ready(function(){
    //初始化列表
    var oTable=new editHomepageProcess_TableInit();
    oTable.Init();
    //所有下拉框添加choose样式
    jQuery('#editHomepageProcessForm .chosen-select').chosen({width: '100%'});
});