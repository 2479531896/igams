
// 列表初始化
var drugResistanceAnalysis_TableInit=function(){
    var oTableInit=new Object();
    // 初始化table
    oTableInit.Init=function(){
        $("#drugResistanceAnalysis_formSearch #drugResistanceAnalysis_list").bootstrapTable({
            url: '/analysis/analysis/pageGetListDrugResistanceAnalysis',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#drugResistanceAnalysis_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "mc",					//排序字段
            sortOrder: "asc",               	//排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 15,                       //每页的记录行数（*）
            pageList: [15, 30, 50, 100],        //可供选择的每页的行数（*）
            paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                     	//行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "zsid",                	//每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                	//是否显示父子表
            columns: [{
                checkbox: true,
                width: '2%',
            },{
                title: '序号',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '3%',
                align: 'left',
                visible:true
            },{
                field: 'zsid',
                title: '注释ID',
                titleTooltip:'注释ID',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'mc',
                title: '耐药基因名',
                titleTooltip:'耐药基因名',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'zs',
                title: '注释',
                titleTooltip:'注释',
                width: '50%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'glsrst',
                title: '革兰氏染色体',
                titleTooltip:'革兰氏染色体',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'knwzly',
                title: '基因所属物种',
                titleTooltip:'基因所属物种',
                width: '35%',
                align: 'left',
                sortable: true,
                visible: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                // 双击事件
                drugResistanceAnalysisDealById(row.zsid,'view',$("#drugResistanceAnalysis_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#drugResistanceAnalysis_formSearch #drugResistanceAnalysis_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true
        })
    };
    // 得到查询的参数
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "zsid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return drugResistanceAnalysisSearchData(map);
    };
    return oTableInit
};
// 条件搜索
function drugResistanceAnalysisSearchData(map){
    var cxtj=$("#drugResistanceAnalysis_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#drugResistanceAnalysis_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["mc"]=cxnr
    }else if(cxtj=="1"){
        map["zs"]=cxnr
    }else if(cxtj=="2"){
        map["glsrst"]=cxnr
    }else if(cxtj=="3"){
        map["knwzly"]=cxnr
    }

    return map;
}

// 列表刷新
function searchDrugResistanceAnalysisResult(isTurnBack){
    if(isTurnBack){
        $('#drugResistanceAnalysis_formSearch #drugResistanceAnalysis_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#drugResistanceAnalysis_formSearch #drugResistanceAnalysis_list').bootstrapTable('refresh');
    }
}

// 按钮动作函数
function drugResistanceAnalysisDealById(id,action,tourl){
    var url= tourl;
    if(!tourl){
        return;
    }
    if(action =='view'){
        var url= tourl + "?zsid="+id;
        $.showDialog(url,'详情',viewDrugResistanceAnalysisConfig);
    }else if(action =='add'){
        $.showDialog(url,'新增',editDrugResistanceAnalysisConfig);
    }else if(action =='mod'){
        var url= tourl + "?zsid="+id;
        $.showDialog(url,'修改',editDrugResistanceAnalysisConfig);
    }
}

// 按钮初始化
var drugResistanceAnalysis_ButtonInit= function (){
    var oInit=new Object();
    var postdata = {};
    oInit.Init=function(){
        //初始化页面上面的按钮事件
        // 查询
        var btn_query=$("#drugResistanceAnalysis_formSearch #btn_query");
        // 查看
        var btn_view=$("#drugResistanceAnalysis_formSearch #btn_view");
        // 新增
        var btn_add=$("#drugResistanceAnalysis_formSearch #btn_add");
        // 修改
        var btn_mod = $("#drugResistanceAnalysis_formSearch #btn_mod");
        // 删除
        var btn_del = $("#drugResistanceAnalysis_formSearch #btn_del");
        /*--------------------模糊查询--------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchDrugResistanceAnalysisResult(true);
            });
        }
        /*--------------------查　　看--------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row=$('#drugResistanceAnalysis_formSearch #drugResistanceAnalysis_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                drugResistanceAnalysisDealById(sel_row[0].zsid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*--------------------新　　增--------------------*/
        btn_add.unbind("click").click(function(){
            drugResistanceAnalysisDealById(null,"add",btn_add.attr("tourl"));
        });
        /*--------------------修　　改--------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#drugResistanceAnalysis_formSearch #drugResistanceAnalysis_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length == 1){
                drugResistanceAnalysisDealById(sel_row[0].zsid, "mod", btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*--------------------删　　除--------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#drugResistanceAnalysis_formSearch #drugResistanceAnalysis_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].zsid;
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
                                        searchDrugResistanceAnalysisResult(true);
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

// 查看
var viewDrugResistanceAnalysisConfig = {
    width : "800px",
    modalName : "viewDrugResistanceAnalysisModal",
    offAtOnce : true,  //当数据提交成功，立刻关闭窗口
    buttons : {
        cancel : {
            label : "关　闭",
            className : "btn-default"
        }
    }
};

// 新增/修改
var editDrugResistanceAnalysisConfig = {
    width : "850px",
    modalName : "editDrugResistanceAnalysisModal",
    formName : "editDrugResistanceAnalysisForm",
    offAtOnce : true,  // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "保 存",
            className : "btn-primary",
            callback : function() {
                if(!$("#editDrugResistanceAnalysisForm").valid()){// 表单验证
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#editDrugResistanceAnalysisForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"editDrugResistanceAnalysisForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchDrugResistanceAnalysisResult();
                        });
                    }else if(responseText["status"] == "fail"){
                        preventResubmitForm(".modal-footer > button", false);
                        $.error(responseText["message"],function() {});
                    } else{
                        preventResubmitForm(".modal-footer > button", false);
                        $.alert(responseText["message"],function() {});
                    }
                },".modal-footer > button");
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};



// 页面初始化
$(function(){
    var oTable = new drugResistanceAnalysis_TableInit();
    oTable.Init();
    // 2.初始化Button的点击事件
    var oButtonInit = new drugResistanceAnalysis_ButtonInit();
    oButtonInit.Init();
    jQuery('#drugResistanceAnalysis_formSearch .chosen-select').chosen({width: '100%'});
});