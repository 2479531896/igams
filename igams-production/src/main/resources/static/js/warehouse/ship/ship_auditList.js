var deviceCheck_audit_TableInit=function(){
    var oTableInit = new Object();
    oTableInit.Init=function(){
        $("#ship_audit_formSearch #shipCheck_audit_list").bootstrapTable({
            url: $("#ship_formAudit #urlPrefix").val()+'/ship/ship/pageGetListDeviceAudit',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#ship_audit_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "fhgl.fhdh",				//排序字段
            sortOrder: "desc",                   //排序方式
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
            uniqueId: "fhid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width: '3%'
            },{
                'field': '',
                'title': '序号',
                'align': 'center',
                'width': '4%',
                'formatter': function (value, row, index) {
                    var options = $('#ship_audit_formSearch #shipCheck_audit_list').bootstrapTable('getOptions');
                    return options.pageSize * (options.pageNumber - 1) + index + 1;
                }
            },{
                field: 'fhid',
                title: '发货ID',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'fhdh',
                title: '发货单号',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'xslxmc',
                title: '销售类型',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'jsrmc',
                title: '经手人',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'djrq',
                title: '单据日期',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'xsbmmc',
                title: '销售部门',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'xsdd',
                title: '订单号',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'khmc',
                title: '客户',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'shdz',
                title: '收货地址',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'shxx_shyj',
                title: '审核意见',
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
                ship_audit_DealById(row.fhid,"view",$("#ship_audit_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#ship_audit_formSearch #shipCheck_audit_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true
        });
    }
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "fhid", // 防止同名排位用
            sortLastOrder: "asc", // 防止同名排位用
            zt: "10"
            // 搜索框使用
            // search:params.search
        };
        var cxtj=$("#ship_audit_formSearch #cxtj").val();
        var cxnr=$.trim(jQuery('#ship_audit_formSearch #cxnr').val());
        if(cxtj=="0"){
            map["entire"]=cxnr;
        }else if(cxtj=="1"){
            map["fhdh"]=cxnr;
        }else if(cxtj=="2"){
            map["jsr"]=cxnr;
        }else if(cxtj=="3"){
            map["xsdd"]=cxnr;
        }else if(cxtj=="4"){
            map["kh"]=cxnr;
        }else if(cxtj=="5"){
            map["wlbm"]=cxnr;
        }else if(cxtj=="6"){
            map["wlmc"]=cxnr;
        }else if(cxtj=="7"){
            map["shdz"]=cxnr;
        }
        map["dqshzt"] = 'dsh';
        return map;
    };
    return oTableInit;
};

var deviceCheckAudited_TableInit=function(){
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $("#ship_audit_audited #ship_tb_list").bootstrapTable({
            url:$("#ship_formAudit #urlPrefix").val()+'/ship/ship/pageGetListDeviceAudit',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#ship_audit_audited #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "fhgl.fhdh",				//排序字段
            sortOrder: "desc",                  //排序方式
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
            //height: 500,                      //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "fhid",            //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                  //是否显示父子表
            isForceTable:true,
            columns: [{
                checkbox: true,
                width: '3%'
            },{
                'field': '',
                'title': '序号',
                'align': 'center',
                'width': '4%',
                'formatter': function (value, row, index) {
                    var options = $('#ship_audit_audited #ship_tb_list').bootstrapTable('getOptions');
                    return options.pageSize * (options.pageNumber - 1) + index + 1;
                }
            },{
                field: 'fhid',
                title: '发货ID',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'fhdh',
                title: '发货单号',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'xslxmc',
                title: '销售类型',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'jsrmc',
                title: '经手人',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'djrq',
                title: '单据日期',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'xsbmmc',
                title: '销售部门',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'xsdd',
                title: '订单号',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'khmc',
                title: '客户',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'shdz',
                title: '收货地址',
                width: '10%',
                align: 'left',
                visible: true
            },{
                field: 'shxx_shyj',
                title: '审核意见',
                width: '10%',
                align: 'left',
                visible: true
            }],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                ship_audit_DealById(row.fhid,"view",$("#ship_audit_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#ship_audit_audited #ship_tb_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true
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
            pageSize: params.limit,   //页面大小
            pageNumber: (params.offset / params.limit) + 1,  //页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      //排序列名
            sortOrder: params.order, //排位命令（desc，asc）
            sortLastName: "fhid", //防止同名排位用
            sortLastOrder: "asc" //防止同名排位用
            //搜索框使用
            //search:params.search
        };

        var cxtj=$("#ship_audit_audited #cxtj").val();
        var cxnr=$.trim(jQuery('#ship_audit_audited #cxnr').val());
        if(cxtj=="0"){
            map["entire"]=cxnr;
        }else if(cxtj=="1"){
            map["fhdh"]=cxnr;
        }else if(cxtj=="2"){
            map["jsr"]=cxnr;
        }else if(cxtj=="3"){
            map["xsdd"]=cxnr;
        }else if(cxtj=="4"){
            map["kh"]=cxnr;
        }else if(cxtj=="5"){
            map["wlbm"]=cxnr;
        }else if(cxtj=="6"){
            map["wlmc"]=cxnr;
        }else if(cxtj=="7"){
            map["shdz"]=cxnr;
        }
        map["dqshzt"] = 'ysh';
        return map;
    };
    return oTableInit;
};

function ship_audit_DealById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $("#ship_formAudit #urlPrefix").val()+tourl;
    if(action=="view"){
        var url=tourl+"?fhid="+id;
        $.showDialog(url,'查看信息',viewConfig);
    }else if(action=="audit"){
        showAuditDialogWithLoading({
            id: id,
            type: 'AUDIT_SHIPPING',
            url:tourl,
            data:{'ywzd':'fhid'},
            title:"发货审核",
            preSubmitCheck:'preSubmitSHip',
            prefix:$('#ship_formAudit #urlPrefix').val(),
            callback:function(){
                searchShip_audit_Result(true);//回调
            },
            dialogParam:{width:1200}
        });
    }
}

function preSubmitSHip(){
    let json = [];
    if(t_map.rows != null && t_map.rows.length > 0){
        for(let i=0;i<t_map.rows.length;i++){
            if (t_map.rows[i].wlmx_json.length == "0"){
                $.alert("发货总数不能为0！");
                return false;
            }
            let wlmx_json=JSON.parse(t_map.rows[i].wlmx_json);
            if(wlmx_json!=null && wlmx_json!=""){
                for(let j=0;j<wlmx_json.length;j++){

                    let sz = {"xsid":'',"xsmxid":'',"wlid":'',"ck":'',"ckqxlx":'',"khdm":'',"ddmxid":'',"wlbm":'',"khmc":'',"xsdd":'',"kl":'',"kl2":'',"suil":'',"hsdj":'',"wsdj":'',"bj":'',"ddxh":'',"hwid":'',"fhsl":''};
                    sz.xsid = t_map.rows[i].xsid;
                    sz.xsdd = t_map.rows[i].oaxsdh;
                    sz.khdm = t_map.rows[i].khdm;
                    sz.xsmxid = t_map.rows[i].xsmxid;
                    sz.wlbm = t_map.rows[i].wlbm;
                    sz.khmc = t_map.rows[i].khjcmc;
                    sz.khid = t_map.rows[i].khjc;
                    sz.suil  = t_map.rows[i].suil;
                    sz.hsdj  = t_map.rows[i].hsdj;
                    sz.wsdj  = t_map.rows[i].wsdj;
                    sz.bj = t_map.rows[i].bj;
                    sz.yfhrq= t_map.rows[i].yfhrq;
                    sz.hwid = wlmx_json[j].hwid;
                    sz.fhsl = wlmx_json[j].cksl;
                    sz.wlid = wlmx_json[j].wlid;
                    sz.ckqxlx = wlmx_json[j].ckqxlx;
                    sz.ck = wlmx_json[j].ckid;
                    if (sz.fhsl > "0"){
                        json.push(sz);
                    }
                }
            }
        }
        $("#shipAjaxForm #xsmx_json").val(JSON.stringify(json));
    }else{
        $.alert("发货明细不能为空！");
        return false;
    }
    return true;
}

var deviceCheck_audit_oButtton=function(){
    var oInit=new Object();
    var postdata = {};
    oInit.Init=function(){
        var btn_query=$("#ship_audit_formSearch #btn_query");//模糊查询
        var btn_queryAudited = $("#ship_audit_audited #btn_query");//审核记录列表模糊查询
        var btn_cancelAudit = $("#ship_audit_audited #btn_cancelAudit");//取消审核
        var btn_view = $("#ship_audit_formSearch #btn_view");//查看页面
        var btn_audit = $("#ship_audit_formSearch #btn_audit");//审核

        /*-----------------------模糊查询(审核列表)------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchShip_audit_Result(true);
            });
        }

        /*-----------------------模糊查询(审核记录)------------------------------------*/
        if(btn_queryAudited!=null){
            btn_queryAudited.unbind("click").click(function(){
                searchShipAudited(true);
            });
        }

        /*---------------------------查看页面--------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row=$('#ship_audit_formSearch #shipCheck_audit_list').bootstrapTable('getSelections');
            if(sel_row.length==1){
                ship_audit_DealById(sel_row[0].fhid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------审核--------------------------------*/
        btn_audit.unbind("click").click(function(){
            var sel_row = $('#ship_audit_formSearch #shipCheck_audit_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                ship_audit_DealById(sel_row[0].fhid,"audit",btn_audit.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });
        /*---------------------------查看审核记录--------------------------------*/
        //选项卡切换事件回调
        $('#ship_formAudit #deviceCheck_audit_ul a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
            var _hash = e.target.hash.replace("#",'');
            if(!e.target.isLoaded){//只调用一次
                if(_hash=='ship_auditing'){
                    var oTable= new deviceCheck_audit_TableInit();
                    oTable.Init();
                }else{
                    var oTable= new deviceCheckAudited_TableInit();
                    oTable.Init();
                }
                e.target.isLoaded = true;
            }else{//重新加载
                //$(_gridId + ' #ship_tb_list').bootstrapTable('refresh');
            }
        }).first().trigger('shown.bs.tab');//触发第一个选中事件

        /*-------------------------------------取消审核-----------------------------------*/
        if(btn_cancelAudit!=null){
            btn_cancelAudit.unbind("click");
            btn_cancelAudit.click(function(){
                var putInStorage_params=[];
                putInStorage_params.prefix=$('#ship_formAudit #urlPrefix').val();
                cancelAudit($('#ship_audit_audited #ship_tb_list').bootstrapTable('getSelections'),function(){
                    searchShipAudited();
                },null,putInStorage_params);
            })
        }
    }
    return oInit;
}

var viewConfig = {
    width		: "1000px",
    modalName	:"viewConfig",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};


function searchShip_audit_Result(isTurnBack){
    if(isTurnBack){
        $('#ship_audit_formSearch #shipCheck_audit_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#ship_audit_formSearch #shipCheck_audit_list').bootstrapTable('refresh');
    }
}

function searchShipAudited(isTurnBack){
    if(isTurnBack){
        $('#ship_audit_audited #ship_tb_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#ship_audit_audited #ship_tb_list').bootstrapTable('refresh');
    }
}

$(function(){
    var oTable= new deviceCheck_audit_TableInit();
    oTable.Init();

    var oButtonInit = new deviceCheck_audit_oButtton();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#ship_audit_formSearch .chosen-select').chosen({width: '100%'});
    jQuery('#ship_audit_audited .chosen-select').chosen({width: '100%'});
});