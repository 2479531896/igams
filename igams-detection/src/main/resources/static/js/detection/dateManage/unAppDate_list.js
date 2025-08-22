/**
 * 高级筛选开关
 * @type {boolean}
 */
var UnAppDate_turnOff=true;

/**
 * 数据显示
 * @param FieldList
 * @returns {Object}
 * @constructor
 */
var UnAppDate_TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function (){
        $('#unAppDateFormSearch #unAppDate_list').bootstrapTable({
            url: '/detection/detection/pageGetListUnAppDate',   //请求后台的URL（*）
            method: 'get',                                      //请求方式（*）
            toolbar: '#unAppDateFormSearch #toolbar',           //工具按钮用哪个容器
            striped: true,                                      //是否显示行间隔色
            cache: false,                                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                                   //是否显示分页（*）
            paginationShowPageGo: true,                         //增加跳转页码的显示
            sortable: true,                                     //是否启用排序
            sortName: "bkyyrq",				                    //排序字段
            sortOrder: "DESC",                                  //排序方式
            queryParams: oTableInit.queryParams,                //传递参数（*）
            sidePagination: "server",                           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                                       //初始化加载第一页，默认第一页
            pageSize: 15,                                       //每页的记录行数（*）
            pageList: [15, 30, 50, 100],                        //可供选择的每页的行数（*）
            paginationPreText: '‹',				                //指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			                //指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                                  //是否显示所有的列
            showRefresh: true,                                  //是否显示刷新按钮
            minimumCountColumns: 2,                             //最少允许的列数
            clickToSelect: true,                                //是否启用点击选中行
            //height: 500,                                      //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "bkyyrqid",                                 //每一行的唯一标识，一般为主键列
            showToggle:true,                                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                                    //是否显示详细视图
            detailView: false,                                  //是否显示父子表
            columns:[{
                checkbox: true,
                width: '5%'
            },{
                field: 'bkyyrq',
                title: '不可预约日期',
                width: '10%',
                align: 'left',
                visible:true
            },{
                field: 'bkyysjd',
                title: '不可预约时间段',
                width: '40%',
                align: 'left',
                visible: true
            },{
                field: 'lrry',
                title: '操作人员',
                width: '10%',
                align: 'left',
                formatter:czryformat,
                visible:true,
            },{
                field: 'lrsj',
                title: '操作时间',
                width: '20%',
                align: 'left',
                formatter:czsjformat,
                visible:true,
            },{
                field: 'bz',
                title: '备注',
                width: '15%',
                align: 'left',
                visible:true,
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                unAppDateDealById(row.bkyyrqid,'view',$("#unAppDateFormSearch #btn_view").attr("tourl"));
            },
        });
        $("#unAppDateFormSearch #unAppDate_list").colResizable({
                liveDrag:true,
                gripInnerHtml:"<div class='grip'></div>",
                draggingClass:"dragging",
                resizeMode:'fit',
                postbackSafe:true,
                partialRefresh:true,
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
            sortLastName: "bkyyrqid", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getUnAppDateSearchData(map);
    };
    return oTableInit;
}

/**
 * 操作人员
 * @param value
 * @param row
 * @param index
 * @returns {*}
 */
function czryformat(value,row,index){
    var czry=row.lrry;
    if(row.xgry!='' && row.xgry!=null){
        czry=row.xgry;
    }
    return czry;
}

/**
 * 操作时间
 * @param value
 * @param row
 * @param index
 * @returns {*}
 */
function czsjformat(value,row,index){
    var czsj=row.lrsj;
    if(row.xgsj!=''&&row.xgsj!=null){
        czsj=row.xgsj;
    }
    return czsj;
}

/**
 * 查询条件
 * @param map
 * @returns {*}
 */
function getUnAppDateSearchData(map){
    var cxtj=$("#unAppDateFormSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#unAppDateFormSearch #cxnr').val());
    if(cxtj=="0"){
        map["entire"]=cxnr
    }
    // 不可预约日期日期
    var bkyyrqstart = jQuery('#unAppDateFormSearch #bkyyrqstart').val();
    map["bkyyrqstart"] = bkyyrqstart;
    var bkyyrqend = jQuery('#unAppDateFormSearch #bkyyrqend').val();
    map["bkyyrqend"] = bkyyrqend;
    return map;
}

/**
 * 按钮动作函数
 * @param id
 * @param action
 * @param tourl
 * @constructor
 */
function unAppDateDealById(id,action,tourl){
    if(!tourl){
        return;
    }
    if(action =='view'){
        var url= tourl + "?bkyyrqid=" +id;
        $.showDialog(url,'详情',viewUnAppDateConfig);
    }else if(action=='add'){
        var url=tourl;
        $.showDialog(url,'新增',editUnAppDateConfig);
    }else if(action=='mod'){
        var url=tourl + "?bkyyrqid=" +id;
        $.showDialog(url,'修改',editUnAppDateConfig);
    }/*else if(action =='del'){
        $.confirm('您确定要删除所选择的记录吗？',function(result){
            if(result){
                jQuery.ajaxSetup({async:false});
                var url=tourl;
                jQuery.post(url,{ids:id,"access_token":$("#ac_tk").val()},function(responseText){
                    setTimeout(function(){
                        if(responseText["status"] == 'success'){
                            $.success(responseText["message"],function() {
                                searchUnAppDateResult();
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
    }*/
}

/**
 * 初始化按钮
 * @returns {Object}
 * @constructor
 */
var UnAppDate_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};
    oInit.Init = function () {
        //初始化页面上面的按钮事件
        var btn_query =$("#unAppDateFormSearch #btn_query");//模糊查询
        var btn_view = $("#unAppDateFormSearch #btn_view");//详情
        var btn_add=$("#unAppDateFormSearch #btn_add");//新增
        var btn_mod=$("#unAppDateFormSearch #btn_mod");//修改
        var btn_del=$("#unAppDateFormSearch #btn_del");//删除

        //添加日期控件
        laydate.render({
            elem: '#bkyyrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#bkyyrqend'
            ,theme: '#2381E9'
        });
        /* ------------------------------查询-----------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchUnAppDateResult(true);
            });
        };
        /* ------------------------------查看-----------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#unAppDateFormSearch #unAppDate_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                unAppDateDealById(sel_row[0].bkyyrqid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /* ------------------------------新增-----------------------------*/
        btn_add.unbind("click").click(function(){
            unAppDateDealById(null,"add",btn_add.attr("tourl"));
        });
        /* ------------------------------删除-----------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#unAppDateFormSearch #unAppDate_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].bkyyrqid;
                }
                ids=ids.substr(1);
                $.confirm('您确定要删除所选择的信息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= btn_del.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchUnAppDateResult();
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
        /* ------------------------------修改-----------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row=$('#unAppDateFormSearch #unAppDate_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                unAppDateDealById(sel_row[0].bkyyrqid,"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
    };
    return oInit;
};

/**
 * 列表刷新
 * @param isTurnBack
 */
function searchUnAppDateResult(isTurnBack){
    if(isTurnBack){
        $('#unAppDateFormSearch #unAppDate_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#unAppDateFormSearch #unAppDate_list').bootstrapTable('refresh');
    }
}

/*查看详情信息模态框*/
var viewUnAppDateConfig = {
    width		: "700px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var editUnAppDateConfig = {
    width		: "700px",
    modalName	: "editUnAppDateModal",
    formName	: "editUnAppDateForm",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "保存",
            className : "btn-primary",
            callback : function() {
                if(!$("#editUnAppDateForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#editUnAppDateForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"editUnAppDateForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                                searchUnAppDateResult();
                            }
                        });
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {
                        });
                    } else{
                        $.alert(responseText["message"],function() {
                        });
                    }
                },".modal-footer > button");
                return false;
            }
        },
        cancel  : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

/**
 * 初始化
 */
$(function(){
    //初始化Table
    var oTable = new UnAppDate_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new UnAppDate_ButtonInit();
    oButtonInit.Init();

    jQuery('#unAppDateFormSearch .chosen-select').chosen({width: '100%'});
});
