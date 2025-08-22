var partner_turnOff=true;
var taskListPartner_TableInit = function () {
    var oTableInit = new Object();
    // 初始化Table
    oTableInit.Init = function () {
        $('#taskListPartnerForm #tb_list').bootstrapTable({
            url:'/systemmain/task/commonListUser',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#taskListPartnerForm #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"czsj",					// 排序字段
            sortOrder: "desc nulls last",                   // 排序方式
            queryParams: oTableInit.queryParams,// 传递参数（*）
            sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       // 初始化加载第一页，默认第一页
            pageSize: 10,                       // 每页的记录行数（*）
            pageList: [10, 15, 20],        // 可供选择的每页的行数（*）
            paginationPreText: '‹',				// 指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			// 指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  // 是否显示所有的列
            showRefresh: true,                  // 是否显示刷新按钮
            minimumCountColumns: 2,             // 最少允许的列数
            clickToSelect: true,                // 是否启用点击选中行
            // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "yhid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '5%'
            }, {
                field: 'no',
                title: '序号',
                titleTooltip:'序号',
                width: '10%',
                align: 'left',
                visible: true,
                formatter:xhFormat
            }, {
                field: 'yhid',
                title: '用户ID',
                width: '10%',
                align: 'left',
                visible: false
            }, {
                field: 'ddid',
                title: '钉钉ID',
                width: '10%',
                align: 'left',
                visible: false
            }, {
                field: 'zsxm',
                title: '真实姓名',
                titleTooltip:'真实姓名',
                width: '30%',
                align: 'left',
                visible: true
            }, {
                field: 'yhm',
                title: '用户名',
                titleTooltip:'用户名',
                width: '20%',
                align: 'left',
                visible: true
            }, {
                field: 'gwmc',
                title: '岗位名称',
                titleTooltip:'岗位名称',
                width: '20%',
                align: 'left',
                visible: true
            }, {
                field: 'jsmc',
                title: '角色',
                titleTooltip:'角色',
                width: '20%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                return;
            },
            //点击每一个单选框时触发的操作
            onCheck:function(row){
                var rys=$("#taskListPartnerForm #xzrys").val();
                var json_rys=[];

                if(rys!="" && rys!=null){
                    json_rys=JSON.parse(rys)
                }
                var sz={"zsxm":row.zsxm,"yhid":row.yhid};
                json_rys.push(sz);
                $("#taskListPartnerForm #xzrys").val(JSON.stringify(json_rys));
                $("#taskListPartnerForm  #t_xzrys").tagsinput({
                    itemValue: "yhid",
                    itemText: "zsxm",
                })
                let json=JSON.stringify(json_rys);
                var jsonStr=eval('('+json+')');
                for (var i = 0; i < jsonStr.length; i++) {
                    $("#taskListPartnerForm  #t_xzrys").tagsinput('add',jsonStr[i]);
                }
            },
            //取消每一个单选框时对应的操作；
            onUncheck:function(row){
                var rys=$("#taskListPartnerForm #xzrys").val();
                var json_rys=[];
                if(rys!="" && rys!=null){
                    json_rys=JSON.parse(rys)
                }
                var delry=[];
                if(json_rys.length>0){
                    for(var i=0;i<json_rys.length;i++){
                        if(row.yhid==json_rys[i].yhid){
                            delry.push(json_rys[i]);
                            json_rys.splice(i,1);
                        }
                    }
                }
                $("#taskListPartnerForm #xzrys").val(JSON.stringify(json_rys));
                $("#taskListPartnerForm  #t_xzrys").tagsinput({
                    itemValue: "yhid",
                    itemText: "zsxm",
                });
                json=JSON.stringify(delry);
                var jsonStr=eval('('+json+')');
                for (var i = 0; i < jsonStr.length; i++) {
                    $("#taskListPartnerForm  #t_xzrys").tagsinput('remove',jsonStr[i]);
                }
            }
        });
        $("#taskListPartnerForm #partner_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true}
        );

    };

    // 得到查询的参数
    oTableInit.queryParams = function(params){
        // 请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，
        // 例如 toolbar 中的参数
        // 如果 queryParamsType = ‘limit’ ,返回参数必须包含
        // limit, offset, search, sort, order
        // 否则, 需要包含: pageSize, pageNumber, searchText, sortName, sortOrder.
        // 返回false将会终止请求
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   // 页面大小
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "yh.yhid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };

        return getWbPartnerSearchData(map);

    };
    return oTableInit;
};

function getWbPartnerSearchData(map){
    var cxtj=$("#taskListPartnerForm #cxtj").val();
    var cxnr=$.trim(jQuery('#taskListPartnerForm #cxnr').val());
    if(cxtj=="0"){
        map["entire"]=cxnr
    }else if(cxtj=="1"){
        map["zsxm"]=cxnr
    }else if(cxtj=="2"){
        map["gwmc"]=cxnr
    }else if(cxtj=="3"){
        map["jsmc"]=cxnr
    }
    return map;
}

//序号格式化
function xhFormat(value, row, index) {
    //获取每页显示的数量
    var pageSize=$('#taskListPartnerForm #tb_list').bootstrapTable('getOptions').pageSize;
    //获取当前是第几页
    var pageNumber=$('#taskListPartnerForm #tb_list').bootstrapTable('getOptions').pageNumber;
    //返回序号，注意index是从0开始的，所以要加上1
    return pageSize * (pageNumber - 1) + index + 1;
}

var taskListPartner_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        var btn_query = $("#taskListPartnerForm #btn_query");
        //初始化页面上面的按钮事件
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                searchPartnerResult();
            });
        }
    };

    return oInit;
};


/**
 * 监听用户移除事件
 */
var tagsRemoved = $('#taskListPartnerForm #t_xzrys').on('itemRemoved', function(event) {
    var rys = $("#taskListPartnerForm #xzrys").val();
    var json_rys = [];
    if(rys!="" && rys!=null){
        json_rys=JSON.parse(rys);
        if(json_rys.length > 0){
            for(var i = 0; i < json_rys.length; i++){
                if(event.item.yhid == json_rys[i].yhid){
                    json_rys.splice(i,1);
                }
            }
        }
        $("#taskListPartnerForm #xzrys").val(JSON.stringify(json_rys));

    }
});

function searchPartnerResult(isTurnBack){
    if(isTurnBack){
        $('#taskListPartnerForm #tb_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#taskListPartnerForm #tb_list').bootstrapTable('refresh');
    }
}




$(function(){

    // 1.初始化Table
    var oTable = new taskListPartner_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new taskListPartner_ButtonInit();
    oButtonInit.Init();
    var tempt =($("#addSjpdForm  #tzry_str").val()).split(",");
    var hbmcs =($("#addSjpdForm  #hbmcs").val()).split(",");
    $("#taskListPartnerForm  #t_xzrys").tagsinput({
        itemValue: "yhid",
        itemText: "zsxm",
    });
    if(tempt.length>1){for (let i = 0; i < tempt.length; i++) {
        $("#taskListPartnerForm  #t_xzrys").tagsinput('add',{"yhid":tempt[i],"zsxm":hbmcs[i]});
    }}
    else if(tempt.length==1&&tempt[0]!=""&&tempt[0]!=undefined){
        $("#taskListPartnerForm  #t_xzrys").tagsinput('add',{"yhid":tempt[0],"zsxm":hbmcs[0]});
    }

    // 所有下拉框添加choose样式
    jQuery('#taskListPartnerForm .chosen-select').chosen({width: '100%'});
});