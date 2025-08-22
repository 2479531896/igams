var partner_turnOff=true;
var taskListPartner_TableInit = function () {
    var oTableInit = new Object();
    // 初始化Table
    oTableInit.Init = function () {
        $('#taskListPartnerForm #tb_list').bootstrapTable({
            url: '/wbaqyz/wbaqyz/pagedataPartner',         // 请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#taskListPartnerForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "hb.hbmc",				//排序字段
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
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "hbid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            },{
                field: 'hbid',
                title: '伙伴id',
                width: '10%',
                align: 'left',
                visible:false
            },{
                field: 'hbmc',
                title: '合作伙伴',
                width: '25%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'wxid',
                title: '微信id',
                width: '10%',
                align: 'left',
                visible:false
            },{
                field: 'wxm',
                title: '微信名称',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'hblxmc',
                title: '伙伴类型',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'province',
                title: '省份',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'city',
                title: '城市',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'bgfsmc',
                title: '报告方式',
                width: '8%',
                align: 'left',
                visible: false
            },{
                field: 'fsfs',
                title: '发送方式',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'yx',
                title: '邮箱',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'cskz2',
                title: '外部接口',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'xtyhm',
                title: '用户',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'flmc',
                title: '分类',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'zflmc',
                title: '子分类',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'bgmb',
                title: '报告模板',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'tjmc',
                title: '统计名称',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'swmc',
                title: '商务名称',
                width: '10%',
                align: 'left',
                visible: false
            },{
                field: 'gzlxmc',
                title: '盖章类型',
                width: '10%',
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'jcdwstr',
                title: '检测单位',
                width: '30%',
                align: 'left',
                visible: true
            },{
                field: 'scbj',
                title: '状态',
                width: '10%',
                formatter:ztformat,
                align: 'left',
                sortable: true,
                visible: false
            },{
                field: 'cskz3',
                title: 'onco权限',
                width: '10%',
                formatter:cskz3format,
                align: 'left',
                sortable: true,
                visible: false
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
                var sz={"hbmc":row.hbmc,"hbid":row.hbid};
                json_rys.push(sz);
                $("#taskListPartnerForm #xzrys").val(JSON.stringify(json_rys));
                $("#taskListPartnerForm  #t_xzrys").tagsinput({
                    itemValue: "hbid",
                    itemText: "hbmc",
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
                        if(row.hbid==json_rys[i].hbid){
                            delry.push(json_rys[i]);
                            json_rys.splice(i,1);
                        }
                    }
                }
                $("#taskListPartnerForm #xzrys").val(JSON.stringify(json_rys));
                $("#taskListPartnerForm  #t_xzrys").tagsinput({
                    itemValue: "hbid",
                    itemText: "hbmc",
                });
                json=JSON.stringify(delry);
                var jsonStr=eval('('+json+')');
                for (var i = 0; i < jsonStr.length; i++) {
                    $("#taskListPartnerForm  #t_xzrys").tagsinput('remove',jsonStr[i]);
                }
            }
        });
        $("#taskListPartnerForm #tb_list").colResizable({
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
            sortLastName: "hb.hbid", // 防止同名排位用
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
        map["hbmc"]=cxnr
    }else if(cxtj=="1"){
        map["sf"]=cxnr
    }else if(cxtj=="2"){
        map["cs"]=cxnr
    }else if(cxtj=="3"){
        map["xtyhm"]=cxnr
    }else if(cxtj=="4"){
        map["tjmc"]=cxnr
    }else if(cxtj=="5"){
        map["swmc"]=cxnr
    }
    // 删除标记
    var scbjs = jQuery('#taskListPartnerForm #scbj_id_tj').val();
    map["scbjs"] = scbjs;
    // 分类
    var fls = jQuery('#taskListPartnerForm #fl_id_tj').val();
    map["fls"] = fls;
    // 子分类
    var zfls = jQuery('#taskListPartnerForm #zfl_id_tj').val();
    map["zfls"] = zfls;
    // 报告模板
    var bgmbs = jQuery('#taskListPartnerForm #bgmb_id_tj').val();
    map["bgmbs"] = bgmbs;
    return map;
}
//提供给导出用的回调函数
function WbPartnerSearchData(){
    var map ={};
    map["access_token"]=$("#ac_tk").val();
    map["sortLastName"]="hb.hbid";
    map["sortLastOrder"]="asc";
    map["sortName"]="hb.hbmc";
    map["sortOrder"]="asc";
    return getWbPartnerSearchData(map);
}

//状态格式化
function ztformat(value,row,index){
    if(row.scbj=='1'){
        return '已删除';
    }else if(row.scbj=='0'){
        return '正常';
    }else if(row.scbj=='2'){
        return '停用';
    }
}
//状态格式化
function cskz3format(value,row,index){
    if(row.cskz3=='1'){
        return '是';
    }else {
        return '否';
    }
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

        var scbj = $("#taskListPartnerForm a[id^='scbj_id_']");
        var btn_query = $("#taskListPartnerForm #btn_query");

        $.each(scbj, function(i, n){
            var id = $(this).attr("id");
            var code = id.substring(id.lastIndexOf('_')+1,id.length);
            if(code == '0'){
                addTj('scbj',code,'taskListPartnerForm');
            }
        });
        //初始化页面上面的按钮事件
        
        /**显示隐藏**/
        $("#taskListPartnerForm #sl_searchMore").on("click", function(ev){
            var ev=ev||event;
            if(partner_turnOff){
                $("#taskListPartnerForm #searchMore").slideDown("low");
                partner_turnOff=false;
                this.innerHTML="基本筛选";
            }else{
                $("#taskListPartnerForm #searchMore").slideUp("low");
                partner_turnOff=true;
                this.innerHTML="高级筛选";
            }
            ev.cancelBubble=true;
        });
        //绑定搜索发送功能
        if(btn_query != null){
            btn_query.unbind("click").click(function(){
                searchtaskListPartnerResult();
            });
        }
    };

    return oInit;
};
/**
 * 监听标签点击事件
 */
var tagClick = $("#taskListPartnerForm").on('click','.label-info',function(e){
    event.stopPropagation();

    var url = "/partner/partner/viewpartner?access_token=" + $("#ac_tk").val() + "&hbid=" + e.currentTarget.dataset.logo;
    $.showDialog(url, '伙伴详情', choosePartnerConfig);
});

var choosePartnerConfig = {
    width		: "800px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
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
                if(event.item.hbid == json_rys[i].hbid){
                    json_rys.splice(i,1);
                }
            }
        }
        $("#taskListPartnerForm #xzrys").val(JSON.stringify(json_rys));
    }
});

function searchtaskListPartnerResult(isTurnBack){
    //关闭高级搜索条件
    $("#taskListPartnerForm #searchMore").slideUp("low");
    partner_turnOff=true;
    $("#taskListPartnerForm #sl_searchMore").html("高级筛选");
    if(isTurnBack){
        $('#taskListPartnerForm #tb_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#taskListPartnerForm #tb_list').bootstrapTable('refresh');
    }
}

//子分类的取得
function getZfls() {
    // 分类
    var fl = jQuery('#taskListPartnerForm #fl_id_tj').val();
    if (!isEmpty(fl)) {
        fl = "'" + fl + "'";
        jQuery("#taskListPartnerForm #zfl_id").removeClass("hidden");
    }else{
        jQuery("#taskListPartnerForm #zfl_id").addClass("hidden");
    }
    // 项目类别不为空
    if (!isEmpty(fl)) {
        var url = "/systemmain/data/ansyGetJcsjListInStop";
        $.ajax({
            type : "POST",
            url : url,
            data : {"fcsids":fl,"access_token":$("#ac_tk").val()},
            dataType : "json",
            success : function(data){
                if(data.length > 0) {
                    var html = "";
                    $.each(data,function(i){
                        html += "<li>";
                        html += "<a style='text-decoration:none;cursor:pointer;' onclick=\"addTj('zfl','" + data[i].csid + "','taskListPartnerForm');\" id=\"zfl_id_" + data[i].csid + "\"><span>" + data[i].csmc + "</span></a>";
                        html += "</li>";
                    });
                    jQuery("#taskListPartnerForm #ul_zfl").html(html);
                    jQuery("#taskListPartnerForm #ul_zfl").find("[name='more']").each(function(){
                        $(this).on("click", s_showMoreFn);
                    });
                } else {
                    jQuery("#taskListPartnerForm #ul_zfl").empty();

                }
                jQuery("#taskListPartnerForm [id^='zfl_li_']").remove();
                $("#taskListPartnerForm #zfl_id_tj").val("");
            }
        });
    } else {
        jQuery("#taskListPartnerForm #div_zfl").empty();
        jQuery("#taskListPartnerForm [id^='zfl_li_']").remove();
        $("#taskListPartnerForm #zfl_id_tj").val("");
    }
}



$(function(){

    // 1.初始化Table
    var oTable = new taskListPartner_TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new taskListPartner_ButtonInit();
    oButtonInit.Init();
    var tempt =JSON.parse($("#wbaqyzlist").val());
    $("#taskListPartnerForm  #t_xzrys").tagsinput({
        itemValue: "hbid",
        itemText: "hbmc",
    })
    if(tempt.length>1){for (let i = 0; i < tempt.length; i++) {
        $("#taskListPartnerForm  #t_xzrys").tagsinput('add',{"hbid":tempt[i].hbid,"hbmc":tempt[i].hbmc});
    }}
    else if(tempt.length==1&&tempt[0].hbid!=""&&tempt[0].hbid!=undefined){for (let i = 0; i < tempt.length; i++) {
        $("#taskListPartnerForm  #t_xzrys").tagsinput('add',{"hbid":tempt[i].hbid,"hbmc":tempt[i].hbmc});
    }}



    // 所有下拉框添加choose样式
    jQuery('#taskListPartnerForm .chosen-select').chosen({width: '100%'});

    $("#taskListPartnerForm [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
    $("#mater_formSearch [name='more']").each(function(){
        $(this).on("click", s_showMoreFn);
    });
});