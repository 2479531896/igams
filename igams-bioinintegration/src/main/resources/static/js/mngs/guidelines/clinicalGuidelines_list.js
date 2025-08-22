
// 列表初始化
var clinicalGuidelines_TableInit=function(){
    var oTableInit=new Object();
    // 初始化table
    oTableInit.Init=function(){
        $("#clinicalGuidelines_formSearch #clinicalGuidelines_list").bootstrapTable({
            url: '/guidelines/guidelines/pageGetListClinicalGuidelines',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#clinicalGuidelines_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "yygs",					//排序字段
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
            uniqueId: "znid",                	//每一行的唯一标识，一般为主键列
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
                field: 'znid',
                title: '指南ID',
                titleTooltip:'指南ID',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'yblxmc',
                title: '样本类型',
                titleTooltip:'样本类型',
                width: '6%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'nlzmc',
                title: '年龄',
                titleTooltip:'年龄',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'xbmc',
                title: '性别',
                titleTooltip:'性别',
                width: '4%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'yygs',
                title: '引用格式',
                titleTooltip:'引用格式',
                width: '30%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'mc',
                title: '名称',
                titleTooltip:'名称',
                width: '25%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'ly',
                title: '来源',
                titleTooltip:'来源',
                width: '18%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'fbsj',
                title: '发布日期',
                titleTooltip:'发布日期',
                width: '8%',
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
                clinicalGuidelinesDealById(row.znid,'view',$("#clinicalGuidelines_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#clinicalGuidelines_formSearch #clinicalGuidelines_list").colResizable({
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
            sortLastName: "znid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return clinicalGuidelinesSearchData(map);
    };
    return oTableInit
};
// 条件搜索
function clinicalGuidelinesSearchData(map){
    var cxtj=$("#clinicalGuidelines_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#clinicalGuidelines_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["yygs"]=cxnr
    }else if(cxtj=="1"){
        map["mc"]=cxnr
    }else if(cxtj=="2"){
        map["ly"]=cxnr
    }

    return map;
}

// 列表刷新
function searchClinicalGuidelinesResult(isTurnBack){
    if(isTurnBack){
        $('#clinicalGuidelines_formSearch #clinicalGuidelines_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#clinicalGuidelines_formSearch #clinicalGuidelines_list').bootstrapTable('refresh');
    }
}

// 按钮动作函数
function clinicalGuidelinesDealById(id,action,tourl){
    var url= tourl;
    if(!tourl){
        return;
    }
    if(action =='view'){
        var url= tourl + "?znid="+id;
        $.showDialog(url,'详情',viewClinicalGuidelinesConfig);
    }else if(action =='add'){
        $.showDialog(url,'新增',editClinicalGuidelinesConfig);
    }else if(action =='mod'){
        var url= tourl + "?znid="+id;
        $.showDialog(url,'修改',editClinicalGuidelinesConfig);
    }
}

// 按钮初始化
var clinicalGuidelines_ButtonInit= function (){
    var oInit=new Object();
    var postdata = {};
    oInit.Init=function(){
        //初始化页面上面的按钮事件
        // 查询
        var btn_query=$("#clinicalGuidelines_formSearch #btn_query");
        // 查看
        var btn_view=$("#clinicalGuidelines_formSearch #btn_view");
        // 新增
        var btn_add=$("#clinicalGuidelines_formSearch #btn_add");
        // 修改
        var btn_mod = $("#clinicalGuidelines_formSearch #btn_mod");
        // 删除
        var btn_del = $("#clinicalGuidelines_formSearch #btn_del");
        /*--------------------模糊查询--------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchClinicalGuidelinesResult(true);
            });
        }
        /*--------------------查　　看--------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row=$('#clinicalGuidelines_formSearch #clinicalGuidelines_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                clinicalGuidelinesDealById(sel_row[0].znid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*--------------------新　　增--------------------*/
        btn_add.unbind("click").click(function(){
            clinicalGuidelinesDealById(null,"add",btn_add.attr("tourl"));
        });
        /*--------------------修　　改--------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#clinicalGuidelines_formSearch #clinicalGuidelines_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length == 1){
                clinicalGuidelinesDealById(sel_row[0].znid, "mod", btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*--------------------删　　除--------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#clinicalGuidelines_formSearch #clinicalGuidelines_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].znid;
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
                                        searchClinicalGuidelinesResult(true);
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
var viewClinicalGuidelinesConfig = {
    width : "800px",
    modalName : "viewClinicalGuidelinesModal",
    offAtOnce : true,  //当数据提交成功，立刻关闭窗口
    buttons : {
        cancel : {
            label : "关　闭",
            className : "btn-default"
        }
    }
};

// 新增/修改
var editClinicalGuidelinesConfig = {
    width : "850px",
    modalName : "editClinicalGuidelinesModal",
    formName : "editClinicalGuidelinesForm",
    offAtOnce : true,  // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "保 存",
            className : "btn-primary",
            callback : function() {
                if(!$("#editClinicalGuidelinesForm").valid()){// 表单验证
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#editClinicalGuidelinesForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"editClinicalGuidelinesForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchClinicalGuidelinesResult();
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
    var oTable = new clinicalGuidelines_TableInit();
    oTable.Init();
    // 2.初始化Button的点击事件
    var oButtonInit = new clinicalGuidelines_ButtonInit();
    oButtonInit.Init();
    jQuery('#clinicalGuidelines_formSearch .chosen-select').chosen({width: '100%'});
});