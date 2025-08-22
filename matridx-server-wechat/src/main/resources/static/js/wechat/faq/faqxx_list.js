var Faqxx_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#faqxx_formSearch #faqxx_list").bootstrapTable({
            url: '/faq/getFaqxxList',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#faqxx_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
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
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "faqid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            },
                {
                field: 'bt',
                title: '标题',
                width: '20%',
                align: 'left',
                visible:true
            },{
                field: 'nr',
                title: '内容',
                width: '10%',
                align: 'left',
                visible:true
            },{
                field: 'wz',
                title: '位置',
                width: '10%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                Faqxx_DealById(row.faqid,'view',$("#faqxx_formSearch #btn_view").attr("tourl"));
            },
        });
    };
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "faqid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getFaqxxSearchData(map);
    };
    return oTableInit;
}

function getFaqxxSearchData(map){
    var cxtj=$("#faqxx_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#faqxx_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["bt"]=cxnr
    }else if(cxtj=="1"){
        map["nr"]=cxnr

    }
    return map;
}
function Faqxx_DealById(id,action,tourl){
    if(!tourl){
        return;
    }
    if(action=="add"){
        var url=tourl;
        $.showDialog(url,'新增信息',addFaqxxConfig);
    }else if(action=="mod"){
        var url=tourl+"?faqid="+id
        $.showDialog(url,'修改信息',modFaqxxConfig);
    }else if(action=="view"){
        var url=tourl+"?faqid="+id
        $.showDialog(url,'查看信息',viewFaqxxConfig);
    }
}
var Faqxx_oButtton= function (){
    var oInit=new Object();
    var postdata = {};
    oInit.Init=function(){
        var btn_query=$("#faqxx_formSearch #btn_query");
        var btn_add = $("#faqxx_formSearch #btn_add");
        var btn_mod = $("#faqxx_formSearch #btn_mod");
        var btn_view = $("#faqxx_formSearch #btn_view");
        var btn_del = $("#faqxx_formSearch #btn_del");
        /*-----------------------模糊查询------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchFaqxxResult(true);
            });
        }
        /*-----------------------新增------------------------------------*/
        btn_add.unbind("click").click(function(){
            Faqxx_DealById(null,"add",btn_add.attr("tourl"));
        });
        /*-----------------------修改------------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#faqxx_formSearch #faqxx_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                Faqxx_DealById(sel_row[0].faqid,"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*-----------------------查看------------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#faqxx_formSearch #faqxx_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                Faqxx_DealById(sel_row[0].faqid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*-----------------------删除------------------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#faqxx_formSearch #faqxx_list').bootstrapTable('getSelections');//获取选择行数据
            if (sel_row.length==0) {
                $.error("请至少选中一行");
                return;
            }else{
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].faqid;
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
                                        searchFaqxxResult();
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
    }
    return oInit;
}

var addFaqxxConfig = {
    width		: "800px",
    modalName	:"addFaqxxModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {

                if(!$("#ajaxForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchFaqxxResult();
                        });
                    }else if(responseText["status"] == "fail"){
                        $.error(responseText["message"],function() {
                        });
                    } else{
                        $.alert(responseText["message"],function() {
                        });
                    }
                });
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var modFaqxxConfig = {
    width		: "800px",
    modalName	:"modFaqxxModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#ajaxForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                $("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            searchFaqxxResult();
                        });
                    }else if(responseText["status"] == "fail"){
                        $.error(responseText["message"],function() {
                        });
                    } else{
                        $.alert(responseText["message"],function() {
                        });
                    }
                });
                return false;
            }
        },

        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var viewFaqxxConfig = {
    width		: "800px",
    modalName	:"viewFaqxxModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
function searchFaqxxResult(isTurnBack){
    if(isTurnBack){
        $('#faqxx_formSearch #faqxx_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#faqxx_formSearch #faqxx_list').bootstrapTable('refresh');
    }
}

$(function(){
    var oTable=new Faqxx_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new Faqxx_oButtton();
    oButtonInit.Init();
    jQuery('#faqxx_formSearch .chosen-select').chosen({width: '100%'});
})