var cs_map = [];

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
        field: 'wlbm',
        title: '物料编码',
        titleTooltip:'物料编码',
        width: '12%',
        align: 'left',
        visible: true
    },{
        field: 'wlmc',
        title: '物料名称',
        titleTooltip:'物料名称',
        width: '15%',
        align: 'left',
        visible: true
    }, {
        field: 'scph',
        title: '生产批号',
        titleTooltip:'生产批号',
        width: '12%',
        align: 'left',
        visible: true
    }, {
        field: 'ckmc',
        title: '仓库',
        titleTooltip:'仓库',
        width: '12%',
        align: 'left',
        visible: true
    }, {
        field: 'kwbhmc',
        title: '库位',
        titleTooltip:'库位',
        width: '12%',
        align: 'left',
        visible: true
    }, {
        field: 'kcsl',
        title: '库存量',
        titleTooltip:'库存量',
        width: '9%',
        align: 'left',
        visible: true
    },{
        field: 'sl',
        title: '数量',
        titleTooltip:'数量',
        width: '12%',
        align: 'left',
        formatter:slformat,
        visible: true
    }];
var chooseStock_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#chooseStockForm #tb_list').bootstrapTable({
            url: $('#chooseStockForm #urlPrefix').val()+'/warehouse/outDepot/pagedataGetPageChooseStock',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#chooseStockForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "wlbm",					//排序字段
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
            uniqueId: "hwid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: columnsArray,
            onLoadSuccess: function (map) {
                cs_map=map;
                var json = [];
                if(cs_map.rows){
                    for (var i = 0; i < cs_map.rows.length; i++) {
                        var sz = {"hwid":'',"wlid":'',"kcl":'',"sl":'',"wlmc":'',"wlbm":'',"scph":'',"ckmc":'',"kwbhmc":'',"ckkcl":'',"ckhwid":'',"kcsl":''};
                        sz.hwid = cs_map.rows[i].hwid;
                        sz.wlid = cs_map.rows[i].wlid;
                        sz.kcl = cs_map.rows[i].kcl;
                        sz.wlmc = cs_map.rows[i].wlmc;
                        sz.wlbm = cs_map.rows[i].wlbm;
                        sz.scph = cs_map.rows[i].scph;
                        sz.ckmc = cs_map.rows[i].ckmc;
                        sz.kwbhmc = cs_map.rows[i].kwbhmc;
                        sz.sl = cs_map.rows[i].sl;
                        sz.ckkcl = cs_map.rows[i].ckkcl;
                        sz.ckhwid = cs_map.rows[i].ckhwid;
                        sz.kcsl = cs_map.rows[i].kcsl;
                        json.push(sz);
                    }
                    $("#chooseStockForm #hwxx_json").val(JSON.stringify(json));
                }
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
            pageSize: 15,   //页面大小
            pageNumber: 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "hwid", //防止同名排位用
            sortLastOrder: "asc", //防止同名排位用
            wlid: $('#chooseStockForm #wlid').val(),
            ckid: $('#chooseStockForm #ckid').val(),
            //搜索框使用
            //search:params.search
        };
        return map;
    };
    return oTableInit;
};

/**
 * 数量格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function slformat(value,row,index){
    if(row.sl == null){
        row.sl = "" ;
    }
    var t_index=  $("#chooseStockForm #index").val();
    if(t_map.rows[t_index].ckJson.length>0){
        var parse = JSON.parse(t_map.rows[t_index].ckJson);
        for (var i=0; i < parse.length; i++){
            if ( parse[i].hwid == row.hwid){
                row.sl=parse[i].sl;
            }
        }
    }
    var html ="";
    html += "<input id='sl_"+index+"' value='"+row.sl+"' name='sl_"+index+"' style='width:100%;border:1px solid #D5D5D5;' oninput=\"value=value.replace(/[^\\d.]/g,'')\" onblur=\"checkSum('"+row.hwid+"',this,\'sl\')\"></input>";
    return html;
}


/**
 * 失去焦点事件(修改列表参数)
 * @param id
 * @param e
 * @param zdmc
 * @returns
 */
function checkSum(hwid, e, zdmc) {
    var data=JSON.parse($("#chooseStockForm #hwxx_json").val());
    for(var i=0;i<data.length;i++){
        if(data[i].hwid == hwid){
            if(zdmc=='sl'){
                data[i].sl=e.value;
            }
        }
    }
    $("#chooseStockForm #hwxx_json").val(JSON.stringify(data));
}


$(document).ready(function(){
    //初始化列表
    var oTable=new chooseStock_TableInit();
    oTable.Init();
    jQuery('#chooseStockForm .chosen-select').chosen({width: '100%'});
});