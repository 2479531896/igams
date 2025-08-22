var Jnsj_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#jnsj_formSearch #jnsj_list").bootstrapTable({
            url: '/crf/cdiff/listJnsj',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#jnsj_formSearch #toolbar',                // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         // 增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName: "bgjl.jnsjbgid",				// 排序字段
            sortOrder: "asc",                   // 排序方式
            queryParams: oTableInit.queryParams,// 传递参数（*）
            sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       // 初始化加载第一页，默认第一页
            pageSize: 15,                       // 每页的记录行数（*）
            pageList: [15, 30, 50, 100],        // 可供选择的每页的行数（*）
            paginationPreText: '‹',				// 指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			// 指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  // 是否显示所有的列
            showRefresh: true,                  // 是否显示刷新按钮
            minimumCountColumns: 2,             // 最少允许的列数
            clickToSelect: true,                // 是否启用点击选中行
            // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "jnsjbgid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '1%'
            },{
                field: 'xm',
                title: '患者姓名',
                titleTooltip:'患者姓名',
                sortable: true,
                width: '10%',
                align: 'left',
                visible:false
            },{
                field: 'xb',
                title: '性别',
                titleTooltip:'性别',
                sortable: true,
                width: '10%',
                align: 'left',
                visible:true
            },{
                field: 'nl',
                title: '年龄',
                titleTooltip:'年龄',
                sortable: true,
                width: '15%',
                align: 'left',
                visible:true
            },{
                field: 'rysj',
                title: '入院时间',
                titleTooltip:'入院时间',
                sortable: true,
                width: '13%',
                align: 'left',
                visible:true
            },{
                field: 'cysj',
                title: '出院时间',
                titleTooltip:'出院时间',
                sortable: true,
                width: '13%',
                align: 'left',
                visible:true
            },{
                field: 'cyzd',
                title: '出院诊断',
                titleTooltip:'出院诊断',
                width: '15%',
                align: 'left',
                visible:true
            },{
                field: 'blrzdwmc',
                title: '病例入组单位',
                titleTooltip:'病例入组单位',
                width: '5%',
                align: 'left',
                visible:true
            },{
                field: 'ksmc',
                title: '科室',
                titleTooltip:'科室',
                width: '5%',
                align: 'left',
                visible:true
            },{
                field: 'blrzbh',
                title: '病例入组编号',
                titleTooltip:'病例入组编号',
                width: '5%',
                align: 'left',
                visible:true
            },{
                field: 'idh',
                title: 'ID号',
                titleTooltip:'ID号',
                width: '5%',
                align: 'left',
                visible:true
            },{
                field: 'zyh',
                title: '住院号',
                titleTooltip:'住院号',
                width: '5%',
                align: 'left',
                visible:true
            },{
                field: 'tbrxm',
                title: '填表人姓名',
                titleTooltip:'填表人姓名',
                width: '5%',
                align: 'left',
                visible:true
            },{
                field: 'tbsj',
                title: '填表时间',
                titleTooltip:'填表时间',
                width: '5%',
                align: 'left',
                visible:true
            },{
                field: 'jzlx',
                title: '就诊类型',
                titleTooltip:'就诊类型',
                width: '5%',
                align: 'left',
                visible:true
            }],
            onLoadSuccess:function(){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                // Jnsj_DealById(row.jnsjbgid,'view',$("#jnsj_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#jnsj_formSearch #jnsj_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true}
        );
    };
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "bgjl.jnsjbgid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getJnsjSearchData(map);
    };
    return oTableInit;
}

function getJnsjSearchData(map){
    var jnsj_select=$("#jnsj_formSearch #jnsj_select").val();
    var jnsj_input=$.trim(jQuery('#jnsj_formSearch #jnsj_input').val());
    if(jnsj_select=="0"){
        map["kzbh"]=jnsj_input
    }else if(jnsj_select=="1"){
        map["jcdwmc"]=jnsj_input
    }else if(jnsj_select=="2"){
        map["kzsjph"]=jnsj_input
    }else if(jnsj_select=="3"){
        map["tqsjph"]=jnsj_input
    }
    return map;
}

function searchJnsjResult(isTurnBack){
    if(isTurnBack){
        $('#jnsj_formSearch #jnsj_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#jnsj_formSearch #jnsj_list').bootstrapTable('refresh');
    }
}

function Jnsj_DealById(id,action,tourl){
    if(!tourl){
        return;
    }
    if(action=="add"){
        var url=tourl;
        $.showDialog(url,'新增艰难梭菌报告信息',addJnsjConfig);
    }else if(action=="view"){
        var url=tourl+"?jnsjbgid="+id
        $.showDialog(url,'查看扩增板信息',viewKzbConfig);
    }else if(action=="mod"){
        var url=tourl+"?jnsjbgid="+id
        $.showDialog(url,'修改扩增板信息',modKzbConfig);
    }else if(action=="del"){
        var url=tourl+"?jnsjbgid="+id
        $.showDialog(url,'删除扩增板信息',delKzbConfig);
    }
}

var addJnsjConfig = {
    width		: "1200px",
    modalName	:"addJnsjModal",
    offAtOnce	: false,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
//					$("#ajaxForm #txid").val($("#ajaxForm #txid").val().trim());
                //console.log($("#ajaxForm").html())
                // if(!$("#ajaxForm").valid()){// 表单验证
                //
                //     $.alert("请填写完整信息");
                //     return false;
                // }

                var $this = this;

                var sonData=this.setDataModel()
                sonData["access_token"]=$("#ac_tk").val()
                var opts = $this["options"]||{};
                // console.log(sonData)
                // console.log(obj)
                // console.log( $("#ajaxForm").html())
                //
                // alert(opts["ajaxForm"])
                // $("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
                $.ajax({
                    url: "/crf/cdiff/insertJnsj",
                    type: "post",
                    dataType: 'json',
                    data: {"formeData":JSON.stringify(sonData),"access_token":$("#ac_tk").val()},
                    success: function(data) {
                        if(data.status== 'success'){
                            $.closeModal(opts.modalName);
                            searchJnsjResult();
                        }else if(data.status == "fail"){
                            preventResubmitForm(".modal-footer > button", false);
                            $.error(data.message,function() {
                            });
                        } else{
                            preventResubmitForm(".modal-footer > button", false);
                            $.alert(data.message,function() {
                            });
                        }

                    },
                    error: function (data) {
                        console.log(data)
                        $.alert(data.message+'添加异常错误!');
                    }
                })
                // submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
                //     if(responseText["status"] == 'success'){
                //         $.success(responseText["message"],function() {
                //             if(opts.offAtOnce){
                //                 $.closeModal(opts.modalName);
                //             }
                //             searchJnsjResult();
                //         });
                //     }else if(responseText["status"] == "fail"){
                //         preventResubmitForm(".modal-footer > button", false);
                //         $.error(responseText["message"],function() {
                //         });
                //     } else{
                //         preventResubmitForm(".modal-footer > button", false);
                //         $.alert(responseText["message"],function() {
                //         });
                //     }
                // },".modal-footer > button");
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var modKzbConfig = {
    width		: "1200px",
    modalName	:"modJnsjModal",
    offAtOnce	: false,  // 当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
//					$("#ajaxForm #txid").val($("#ajaxForm #txid").val().trim());
                //console.log($("#ajaxForm").html())
                // if(!$("#ajaxForm").valid()){// 表单验证
                //
                //     $.alert("请填写完整信息");
                //     return false;
                // }

                var $this = this;

                var sonData=this.setDataModel()
                sonData["access_token"]=$("#ac_tk").val()
                var opts = $this["options"]||{};
                // console.log(sonData)
                // console.log(obj)
                // console.log( $("#ajaxForm").html())
                //
                // alert(opts["ajaxForm"])
                // $("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
                $.ajax({
                    url: "/crf/cdiff/insertJnsj",
                    type: "post",
                    dataType: 'json',
                    data: {"formeData":JSON.stringify(sonData),"access_token":$("#ac_tk").val()},
                    success: function(data) {
                        if(data.status== 'success'){
                            $.closeModal(opts.modalName);
                            searchJnsjResult();
                        }else if(data.status == "fail"){
                            preventResubmitForm(".modal-footer > button", false);
                            $.error(data.message,function() {
                            });
                        } else{
                            preventResubmitForm(".modal-footer > button", false);
                            $.alert(data.message,function() {
                            });
                        }

                    },
                    error: function (data) {
                        console.log(data)
                        $.alert(data.message+'添加异常错误!');
                    }
                })
                // submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
                //     if(responseText["status"] == 'success'){
                //         $.success(responseText["message"],function() {
                //             if(opts.offAtOnce){
                //                 $.closeModal(opts.modalName);
                //             }
                //             searchJnsjResult();
                //         });
                //     }else if(responseText["status"] == "fail"){
                //         preventResubmitForm(".modal-footer > button", false);
                //         $.error(responseText["message"],function() {
                //         });
                //     } else{
                //         preventResubmitForm(".modal-footer > button", false);
                //         $.alert(responseText["message"],function() {
                //         });
                //     }
                // },".modal-footer > button");
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

// var modKzbConfig= {
//     width		: "1400px",
//     modalName   :"modKzbModal",
//     offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
//     buttons		: {
//         success : {
//             label : "确认",
//             className : "btn-primary",
//             callback : function() {
//                 if(!$("#ajaxForm").valid()){// 表单验证
//                     $.alert("请填写完整信息");
//                     return false;
//                 }
//                 $("input[name='ybbh']").attr("style","border-color:#ccc;height:30px;");
//                 var ybbhs=[];
//                 var hls=[];
//                 var xhs=[];
//                 var ybbh;
//                 for(i=1;i<9;i++){
//                     for(j=1;j<13;j++){
//                         ybbh=$("#ybbh"+i+"-"+j).val();
//                         hl = i+"-"+j;
//                         if(ybbh!=null && ybbh!=''){
//                             ybbhs.push(ybbh);
//                             xhs.push($("#ybbh"+i+"-"+j).attr("xh"));
//                             hls.push(hl);
//                             var ybbhcxcs=0;//用于判断内部编号是否重复
//                             for(var l=0;l<ybbhs.length;l++){
//                                 if(ybbhs[l]==ybbh && ybbh!=""){
//                                     ybbhcxcs++;
//                                     if(ybbhcxcs>=2){
//                                         $.confirm("内部编号不允许重复!"+"(编号信息:"+ybbh+")");
//                                         return false;
//                                     }
//                                 }
//                             }
//                         }
//                     }
//                 }
//                 if(ybbhs==''){
//                     $.confirm("内容不能为空!");
//                     return false;
//                 }
//                 var $this = this;
//                 var opts = $this["options"]||{};
//
//                 $("#ajaxForm #ybbhs").val(ybbhs);
//                 $("#ajaxForm #xhs").val(xhs);
//                 $("#ajaxForm #hls").val(hls);
//
//                 $("#ajaxForm input[name='access_token']").val($("#ac_tk").val());
//                 submitForm(opts["formName"]||"ajaxForm",function(responseText,statusText){
//                     if(responseText["status"] == 'success'){
//                         $.success(responseText["message"],function() {
//                             if(opts.offAtOnce){
//                                 $.closeModal(opts.modalName);
//                             }
//                             searchJnsjResult();
//                         });
//                     }else if(responseText["status"] == "fail"){
//                         preventResubmitForm(".modal-footer > button", false);
//                         $.error(responseText["message"],function() {
//                         });
//                     } else{
//                         $.alert(responseText["message"],function() {
//                         });
//                     }
//                 },".modal-footer > button");
//                 return false;}
//         },
//         cancel : {
//             label : "关 闭",
//             className : "btn-default"
//         }
//
//     }
// };
// var viewKzbConfig = {
//     width		: "1400px",
//     height      : "500PX",
//     modalName	:"viewKzbModal",
//     offAtOnce	: true,  // 当数据提交成功，立刻关闭窗口
//     buttons		: {
//         cancel : {
//             label : "关 闭",
//             className : "btn-default"
//         }
//     }
// };

var Jnsj_oButton=function(){
    var oButtonInit = new Object();
    oButtonInit.Init=function(){
        var btn_query=$("#jnsj_formSearch #btn_query");
        var btn_view = $("#jnsj_formSearch #btn_view");//查看
        var btn_mod=$("#jnsj_formSearch #btn_mod");//修改
        var btn_add=$("#jnsj_formSearch #btn_add");//新增
        var btn_del=$("#jnsj_formSearch #btn_del");//删除
        /*--------------------------------删除---------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#jnsj_formSearch #jnsj_list').bootstrapTable('getSelections');// 获取选择行数据
            if (sel_row.length==0) {
                $.error("请至少选中一行");
                return;
            }else{
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {// 循环读取选中行数据
                    ids= ids + ","+ sel_row[i].jnsjbgid;
                }
                ids=ids.substr(1);
                $.confirm('您确定要删除所选择的信息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        // var url= $('#jnsj_formSearch #urlPrefix').val()+btn_del.attr("tourl");
                        jQuery.post(btn_del.attr("tourl"),{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        searchJnsjResult();
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
        /*--------------------------------新增---------------------------*/
        btn_add.unbind("click").click(function(){
            Jnsj_DealById(null,"add",btn_add.attr("tourl"));
        });
        /*--------------------------------修改---------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row=$('#jnsj_formSearch #jnsj_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                Jnsj_DealById(sel_row[0].jnsjbgid,"mod",btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*-----------------------模糊查询ok------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchJnsjResult(true);
            });
        }
        /*--------------------------------查看------------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#jnsj_formSearch #jnsj_list').bootstrapTable('getSelections');// 获取选择行数据
            if(sel_row.length==1){
                Jnsj_DealById(sel_row[0].jnsjbgid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*-------------------------------------------------------------*/
    }
    return oButtonInit;
}

$(function(){
    var oTable = new Jnsj_TableInit();
    oTable.Init();
    var oButton = new Jnsj_oButton();
    oButton.Init();
    jQuery('#jnsj_formSearch .chosen-select').chosen({width: '100%'});
})