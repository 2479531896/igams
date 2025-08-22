var structure_turnOff=true;
var structure_TableInit = function () {
    var oTableInit = new Object();

    // 初始化Table
    oTableInit.Init = function () {
        $('#structure_formSearch #structure_list').bootstrapTable({
            url: $("#structure_formSearch #urlPrefix").val()+'/storehouse/structure/pageGetListProduction',         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#structure_formSearch #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"cpjgid",					// 排序字段
            sortOrder: "desc",                   // 排序方式
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
            uniqueId: "cpjgid",                     // 每一行的唯一标识，一般为主键列
            showToggle:true,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                checkbox: true,
                width: '1%'
            },{
                title: '序号',
                formatter: function (value, row, index) {
                    return index+1;
                },
                titleTooltip:'序号',
                width: '2%',
                align: 'center',
                visible:true
            },{
                field: 'lbmc',
                title: 'Bom类别',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'wlbm',
                title: '母件物料编码',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'wlmc',
                title: '母件物料名称',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'mjshl',
                title: '损耗率',
                width: '5%',
                align: 'left',
                formatter: structure_formSearch_mjshlformat,
                sortable: true,
                visible: true
            },{
                field: 'bbdh',
                title: '版本代号',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }, {
                field: 'bbsm',
                title: '代码说明',
                width: '15%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'bbrq',
                title: '版本日期',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            }
            ],
            onLoadSuccess: function () {
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
                structureById(row.cpjgid,'view',$("#structure_formSearch #btn_view").attr("tourl"));
            },
        });
        $("#structure_formSearch #structure_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true
        })
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
            sortLastName: "cpjg.mjwlid", // 防止同名排位用
            sortLastOrder: "desc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return structureSearchData(map);
    };
    return oTableInit;
};

/**
 *母件损耗率
 * @param value
 * @param row
 * @param index
 * @returns
 */
function structure_formSearch_mjshlformat(value,row,index){
    let mjshl = 0;
    if (row.mjshl == null){
        mjshl = 0
    }else {
        mjshl = row.mjshl
    }
    var html="<span/>"+mjshl+"<span>%</span>";
    return html;
}


function structureSearchData(map){
    var cxtj=$("#structure_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#structure_formSearch #cxnr').val());
    if(cxtj=="0"){
        map["entire"]=cxnr;
    }else if(cxtj=="1"){
        map["wlmc"]=cxnr;
    }else if(cxtj=="2"){
        map["wlbm"]=cxnr;
    }else if (cxtj == "3") {
        map["bbsm"] = cxnr;
    }else if (cxtj == "4") {
        map["bbdh"] = cxnr;
    }
    // 版本开始日期
    var bbrqstart = jQuery('#structure_formSearch #bbrqstart').val();
    map["bbrqstart"] = bbrqstart;

    // 版本结束日期
    var bbrqend = jQuery('#structure_formSearch #bbrqend').val();
    map["bbrqend"] = bbrqend;
    //bom类型
    var boms=jQuery('#structure_formSearch #bom_id_tj').val();
    map["boms"] = boms.replace(/'/g, "");
    return map;
}

function structureById(id,action,tourl){
    if(!tourl){
        return;
    }
    tourl = $("#structure_formSearch #urlPrefix").val()+tourl;
    if(action =='view'){
        var url= tourl + "?cpjgid=" +id;
        $.showDialog(url,'产品详细信息',viewstructureConfig);
    }else if(action =='add') {
        var url = tourl;
        $.showDialog(url, '新增产品结构', addStructureConfig);
    }else if(action =='mod') {
        var url = tourl+"?cpjgid="+id ;
        $.showDialog(url, '修改产品结构', modStructureConfig);
    }
}
var modStructureConfig = {
    width		: "1600px",
    modalName	:"modStructureModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#structureEditForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                if(!$("#structureEditForm #mjwlid").val()){
                    $.alert("请选择母件！");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var json = [];
                if(t_map.rows != null && t_map.rows.length > 0){
                    for(var i=0;i<t_map.rows.length;i++){
                        var sz = {"zjwlid":'',"zjhh":'',"gxhh":'',"jbyl":'',"jcsl":'',"zjshl":'',
                            "gdyl":'',"gylx":'',"sysl":'',"scrq":'',"sxrq":'',"ckid":'',
                            "ccp":'',"cclx":'',"cbxg":'',"bz":'',"wlbm":'',"gylxdm":'',"ckdm":'',"cclxdm":''};
                        sz.zjwlid = t_map.rows[i].zjwlid;
                        sz.wlbm = t_map.rows[i].wlbm;
                        sz.gylxdm = $("#structureEditForm #gylx_"+i).find("option:selected").attr("gylxdm");
                        sz.ckdm = $("#structureEditForm #ckid_"+i).find("option:selected").attr("ckdm");
                        sz.cclxdm = $("#structureEditForm #cclx_"+i).find("option:selected").attr("cclxdm");
                        sz.zjhh = t_map.rows[i].zjhh;
                        sz.gxhh = t_map.rows[i].gxhh;
                        sz.jbyl = t_map.rows[i].jbyl;
                        sz.jcsl = t_map.rows[i].jcsl;
                        sz.zjshl = t_map.rows[i].zjshl;
                        sz.gdyl = t_map.rows[i].gdyl;
                        sz.gylx = t_map.rows[i].gylx;
                        sz.sysl = t_map.rows[i].sysl;
                        sz.scrq = $("#structureEditForm #scrq_"+i).val();
                        sz.sxrq = $("#structureEditForm #sxrq_"+i).val();
                        sz.ckid = t_map.rows[i].ckid;
                        sz.ccp = t_map.rows[i].ccp;
                        sz.cclx = t_map.rows[i].cclx;
                        sz.cbxg = t_map.rows[i].cbxg;
                        sz.bz = t_map.rows[i].bz;
                        json.push(sz);
                    }
                    $("#structureEditForm #cpjgmx_json").val(JSON.stringify(json));
                }else{
                    $.alert("明细信息不能为空！");
                    return false;
                }
                $("#structureEditForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"structureEditForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            structureResult();
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
            className : "btn-default"
        }
    }
};

var addStructureConfig = {
    width		: "1600px",
    modalName	:"addStructureModal",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#structureEditForm").valid()){
                    $.alert("请填写完整信息");
                    return false;
                }
                if(!$("#structureEditForm #mjwlid").val()){
                    $.alert("请选择母件！");
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var json = [];
                if(t_map.rows != null && t_map.rows.length > 0){
                    for(var i=0;i<t_map.rows.length;i++){
                        var sz = {"zjwlid":'',"zjhh":'',"gxhh":'',"jbyl":'',"jcsl":'',"zjshl":'',
                            "gdyl":'',"gylx":'',"sysl":'',"scrq":'',"sxrq":'',"ckid":'',
                            "ccp":'',"cclx":'',"cbxg":'',"bz":'',"wlbm":'',"gylxdm":'',"ckdm":'',"cclxdm":''};
                        sz.zjwlid = t_map.rows[i].zjwlid;
                        sz.wlbm = t_map.rows[i].wlbm;
                        sz.gylxdm = $("#structureEditForm #gylx_"+i).find("option:selected").attr("gylxdm");
                        sz.ckdm = $("#structureEditForm #ckid_"+i).find("option:selected").attr("ckdm");
                        sz.cclxdm = $("#structureEditForm #cclx_"+i).find("option:selected").attr("cclxdm");
                        sz.zjhh = t_map.rows[i].zjhh;
                        sz.gxhh = t_map.rows[i].gxhh;
                        sz.jbyl = t_map.rows[i].jbyl;
                        sz.jcsl = t_map.rows[i].jcsl;
                        sz.zjshl = t_map.rows[i].zjshl;
                        sz.gdyl = t_map.rows[i].gdyl;
                        sz.gylx = t_map.rows[i].gylx;
                        sz.sysl = t_map.rows[i].sysl;
                        sz.scrq = $("#structureEditForm #scrq_"+i).val();
                        sz.sxrq = $("#structureEditForm #sxrq_"+i).val();
                        sz.ckid = t_map.rows[i].ckid;
                        sz.ccp = t_map.rows[i].ccp;
                        sz.cclx = t_map.rows[i].cclx;
                        sz.cbxg = t_map.rows[i].cbxg;
                        sz.bz = t_map.rows[i].bz;
                        json.push(sz);
                    }
                    $("#structureEditForm #cpjgmx_json").val(JSON.stringify(json));
                }else{
                    $.alert("明细信息不能为空！");
                    return false;
                }
                $("#structureEditForm input[name='access_token']").val($("#ac_tk").val());
                submitForm(opts["formName"]||"structureEditForm",function(responseText,statusText){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            if(opts.offAtOnce){
                                $.closeModal(opts.modalName);
                            }
                            structureResult();
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
            className : "btn-default"
        }
    }
};

var viewstructureConfig = {
    width		: "1600px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

var structure_ButtonInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        var btn_view = $("#structure_formSearch #btn_view");
        var btn_query = $("#structure_formSearch #btn_query");
        var btn_add = $("#structure_formSearch #btn_add");
        var btn_mod = $("#structure_formSearch #btn_mod");
        var btn_del = $("#structure_formSearch #btn_del");// 删除

        //添加日期控件
        laydate.render({
            elem: '#bbrqstart'
            ,theme: '#2381E9'
        });
        //添加日期控件
        laydate.render({
            elem: '#bbrqend'
            ,theme: '#2381E9'
        });

        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                structureResult(true);
            });
        }

        /* --------------------------- 新增 -----------------------------------*/
        btn_add.unbind("click").click(function(){
            structureById(null, "add", btn_add.attr("tourl"));
        });

        /* --------------------------- 修改 -----------------------------------*/
        btn_mod.unbind("click").click(function(){
            var sel_row = $('#structure_formSearch #structure_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length == 1){
                structureById(sel_row[0].cpjgid, "mod", btn_mod.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });

        /* ---------------------------查看列表-----------------------------------*/
        btn_view.unbind("click").click(function(){
            var sel_row = $('#structure_formSearch #structure_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==1){
                structureById(sel_row[0].cpjgid,"view",btn_view.attr("tourl"));
            }else{
                $.error("请选中一行");
            }
        });

        /* ------------------------------删除领料信息-----------------------------*/
        btn_del.unbind("click").click(function(){
            var sel_row = $('#structure_formSearch #structure_list').bootstrapTable('getSelections');//获取选择行数据
            if(sel_row.length==0){
                $.error("请至少选中一行");
                return;
            }else {
                var ids="";
                for (var i = 0; i < sel_row.length; i++) {//循环读取选中行数据
                    ids= ids + ","+ sel_row[i].cpjgid;
                }
                ids=ids.substr(1);
                $.confirm('您确定要删除所选择的信息吗？',function(result){
                    if(result){
                        jQuery.ajaxSetup({async:false});
                        var url= $("#structure_formSearch #urlPrefix").val()+btn_del.attr("tourl");
                        jQuery.post(url,{ids:ids,"access_token":$("#ac_tk").val()},function(responseText){
                            setTimeout(function(){
                                if(responseText["status"] == 'success'){
                                    $.success(responseText["message"],function() {
                                        structureResult();
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

        /**显示隐藏**/
        $("#structure_formSearch #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(structure_turnOff){
                $("#structure_formSearch #searchMore").slideDown("low");
                structure_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#structure_formSearch #searchMore").slideUp("low");
                structure_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });

    };
    return oInit;
};

var structure_PageInit = function(){
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        var scbj = $("#structure_formSearch a[id^='scbj_id_']");
        $.each(scbj, function(i, n){
            var id = $(this).attr("id");
            var code = id.substring(id.lastIndexOf('_')+1,id.length);
            if(code === '0'){
                addTj('scbj',code,'structure_formSearch');
            }
        });
    }
    return oInit;
}

function structureResult(isTurnBack){
    //关闭高级搜索条件
    $("#structure_formSearch #searchMore").slideUp("low");
    structure_turnOff=true;
    $("#structure_formSearch #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#structure_formSearch #structure_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#structure_formSearch #structure_list').bootstrapTable('refresh');
    }
}


$(function(){

    //0.界面初始化
    var oInit = new structure_PageInit();
    oInit.Init();

    // 1.初始化Table
    var oTable = new structure_TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new structure_ButtonInit();
    oButtonInit.Init();

    // 所有下拉框添加choose样式
    jQuery('#structure_formSearch .chosen-select').chosen({width: '100%'});

    $("#structure_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });

});