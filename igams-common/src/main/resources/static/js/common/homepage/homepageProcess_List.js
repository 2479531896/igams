
var HomepageProcess_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function (){
        $('#homepageProcess_formSearch #homepageProcess_list').bootstrapTable({
            url: '/process/process/pageGetListHomepageProcess',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#homepageProcess_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "sylc.lrsj",				//排序字段
            sortOrder: "desc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 50,                       //每页的记录行数（*）
            pageList: [15, 30, 50, 100],        //可供选择的每页的行数（*）
            paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "lcid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns:[{
                checkbox: true,
                width: '4%'
            },{
                field: 'lcid',
                title: '流程ID',
                width: '10%',
                align: 'left',
                visible:false
            },{
                field: 'lcmc',
                title: '流程名称',
                width: '25%',
                align: 'left',
                visible:true
            },{
                field: 'xh',
                title: '序号',
                width: '25%',
                align: 'left',
                visible:true
            },{
                field: 'lxmc',
                title: '类型',
                width: '25%',
                align: 'left',
                visible:true
            },{
                field: 'lcbj',
                title: '流程标记',
                width: '25%',
                align: 'left',
                formatter:lcbjFormat,
                visible:true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                HomepageProcessDealById(row.lcid,'view',$("#homepageProcess_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#homepageProcess_formSearch #homepageProcess_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true
        }
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
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "lcid", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getHomepageProcessSearchData(map);
    };
    return oTableInit;
}

function getHomepageProcessSearchData(map){
    var cxtj=$("#homepageProcess_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#homepageProcess_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["lcmc"]=cxnr
    }
    return map;
}

// 按钮动作函数
function HomepageProcessDealById(id,action,tourl){
    if(!tourl){
        return;
    }
    if(action =='view'){
        var url= tourl + "?lcid="+id;
        $.showDialog(url,'查看详情',viewHomepageProcessConfig);
    }else if(action =='add'){
        var url= tourl;
        $.showDialog(url,'新增',editHomepageProcessConfig);
    }else if(action =='mod'){
        var url= tourl + "?lcid="+id;
        $.showDialog(url,'修改',editHomepageProcessConfig);
    }
}


function lcbjFormat(value,row,index){
    if(row.lcbj=='0'){
        return "<span style='color:red;'>不显示箭头</span>";
    }else if(row.lcbj=='1'){
        return "<span style='color:green;'>显示箭头</span>";
    }
}
var HomepageProcess_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};
    oInit.Init = function () {
        //初始化页面上面的按钮事件
        var btn_view = $("#homepageProcess_formSearch #btn_view");//详情
        var btn_query =$("#homepageProcess_formSearch #btn_query");//模糊查询
        var btn_add = $("#homepageProcess_formSearch #btn_add");//新增
        var btn_mod = $("#homepageProcess_formSearch #btn_mod");//修改
        var btn_del = $("#homepageProcess_formSearch #btn_del");//删除

        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchHomepageProcessResult(true);
            });
        };
        /*---------------------------查看详情信息表-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#homepageProcess_formSearch #homepageProcess_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                HomepageProcessDealById(sel_row[0].lcid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------新增-----------------------------------*/
        btn_add.unbind("click").click(function(){
            HomepageProcessDealById(null,"add",btn_add.attr("tourl"));
        });
        /*---------------------------修改-----------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#homepageProcess_formSearch #homepageProcess_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                HomepageProcessDealById(sel_row[0].lcid,"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* ------------------------------删除-----------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#homepageProcess_formSearch #homepageProcess_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].lcid;
                }
                ids=ids.substr(1);
                $.confirm('您确定要删除所选择的流程吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= btn_del.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchHomepageProcessResult(true);
                                    });
                                }else if(responseText["status"] == "fail"){
                                    $.error(responseText["message"],function() {
                                    });
                                } else{
                                    $.alert(responseText["message"],function() {
                                    });
                                }
                            },1);

                        },'json');
                        jQuery.ajaxSetup({async:true});
                    }
                });
            }
        });
    };
    return oInit;
};

/*查看详情信息模态框*/
var viewHomepageProcessConfig = {
    width		: "800px",
    height		: "1000px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};


var editHomepageProcessConfig = {
    width		: "1200px",
    modalName	: "editHomepageProcessModal",
    formName	: "editHomepageProcessForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#editHomepageProcessForm").valid()){
                    $.alert("请填写完整信息！");
                    return false;
                }
                if(t_map.rows != null && t_map.rows.length > 0){
                    var json = [];
                    for(var i=0;i<t_map.rows.length;i++){
                        if(!t_map.rows[i].xsmc){
                            $.error("请填写显示名称!");
                            return false;
                        }
                        if(!t_map.rows[i].mkid){
                            $.error("请选择模块!");
                            return false;
                        }
                        if(!t_map.rows[i].zyid){
                            $.error("请选择菜单!");
                            return false;
                        }
                        var sz = {"xh":i+1,"lcmxid":'',"xsmc":'',"mkid":'',"zyid":'',"tzcs":''};
                        sz.lcmxid = t_map.rows[i].lcmxid;
                        sz.xsmc = t_map.rows[i].xsmc;
                        sz.mkid = t_map.rows[i].mkid;
                        sz.zyid = t_map.rows[i].zyid;
                        sz.tzcs = t_map.rows[i].tzcs;
                        json.push(sz);
                    }
                    $("#editHomepageProcessForm #lcmx_json").val(JSON.stringify(json));
                }else{
                    $.error("明细不允许为空!");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};

                $("#editHomepageProcessForm input[name='access_token']").val($("#ac_tk").val());

                submitForm(opts["formName"]||"editHomepageProcessForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        preventResubmitForm(".modal-footer > button", false);
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchHomepageProcessResult();
                        });
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {
                        });
                    } else{
                        preventResubmitForm(".modal-footer > button", false);
                        $.alert(responseText["message"],function() {
                        });
                    }
                },".modal-footer > button");
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default",
        }
    }
};

function searchHomepageProcessResult(isTurnBack){
    if(isTurnBack){
        $('#homepageProcess_formSearch #homepageProcess_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#homepageProcess_formSearch #homepageProcess_list').bootstrapTable('refresh');
    }
}

$(function(){
    //0.界面初始化
    // 1.初始化Table
    var oTable = new HomepageProcess_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new HomepageProcess_ButtonInit();
    oButtonInit.Init();
    // 所有下拉框添加choose样式
    jQuery('#homepageProcess_formSearch .chosen-select').chosen({width: '100%'});
});
