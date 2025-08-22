
var borrowing_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#chooseBorrow_formSearch #borrowing_list").bootstrapTable({
            url: $("#chooseBorrow_formSearch #urlPrefix").val()+'/borrowing/borrowing/pagedataListBorrowingDetails',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#chooseBorrow_formSearch #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "jcjymx.lrsj",				//排序字段
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
            uniqueId: "jymxid",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true,
                width: '4%'
            },{
                field: 'jymxid',
                title: '主键id',
                width: '8%',
                align: 'left',
                sortable: true,
                visible: false
            }, {
                field: 'khmc',
                title: '客户名称',
                width: '20%',
                align: 'left',
                sortable: true,
                visible: true,
                formatter:khmcformat,
            },{
                field: 'wlbm',
                title: '物料编码',
                width: '18%',
                align: 'left',
                sortable: true,
                formatter:wlbmformat,
                visible: true
            },{
                field: 'wlmc',
                title: '物料名称',
                width: '18%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'gg',
                title: '规格型号',
                width: '12%',
                align: 'left',
                sortable: true,
                visible: true
            },{
                field: 'scph',
                title: '生产批号',
                width: '20%',
                align: 'left',
                sortable: true,
                visible: true
            }
            ],
            onLoadSuccess:function(map){
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            },
        });
        $("#chooseBorrow_formSearch #borrowing_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true
        })

    };
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "jymxid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
        };
        return getBorrowingSearchData(map);
    }
    return oTableInit;
}


function getBorrowingSearchData(map){
    var cxtj=$("#chooseBorrow_formSearch #cxtj").val();
    var cxnr=$.trim(jQuery('#chooseBorrow_formSearch #cxnr').val());
    var cxtj1=$("#chooseBorrow_formSearch #cxtj1").val();
        var cxnr1=$.trim(jQuery('#chooseBorrow_formSearch #cxnr1').val());
        if(cxtj=="0"){
            map["wlmc"]=cxnr
        }else if(cxtj=="1"){
            map["wlbm"]=cxnr
        }else if(cxtj=="2"){
            map["khmc"]=cxnr
        }else if(cxtj=="3"){
            map["scph"]=cxnr
        }

        if(cxtj1=="0"){
            if(!map["wlmc"]){
                map["wlmc"]=cxnr1
            }
        }else if(cxtj1=="1"){
            if(!map["wlbm"]){
                map["wlbm"]=cxnr1
            }
        }else if(cxtj1=="2"){
            if(!map["khmc"]){
                map["khmc"]=cxnr1
            }
        }else if(cxtj1=="3"){
            if(!map["scph"]){
                map["scph"]=cxnr1
            }
        }
    return map;
}


var borrowing_oButtton=function(){
    var oInit=new Object();
    var postdata = {};
    oInit.Init=function(){
        var btn_query=$("#chooseBorrow_formSearch #btn_query");
        /*-----------------------模糊查询------------------------------------*/
        if(btn_query!=null){
            btn_query.unbind("click").click(function(){
                searchBorrowingResult();
            });
        }
    }
    return oInit;
}

/**
 * 物料编码格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function khmcformat(value,row,index){
    var html = "";
    if(row.khmc !=null&&row.khmc!=''){
        html += "<a href='javascript:void(0);' onclick=\"queryBykhid('"+row.khid+"')\">"+row.khmc+"</a>";
    }
    return html;
}
function queryBykhid(khid){
    var url=$("#chooseBorrow_formSearch #urlPrefix").val()+"/storehouse/khgl/viewKhgl?khid="+khid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'客户信息',viewKhConfig);
}
var viewKhConfig = {
    width		: "800px",
    height		: "500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
/**
 * 物料编码格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function wlbmformat(value,row,index){
    var html = "";
    html += "<a href='javascript:void(0);' onclick=\"queryByFirstWlbm('"+row.wlid+"')\">"+row.wlbm+"</a>";
    return html;
}
function queryByFirstWlbm(wlid){
    var url=$("#chooseBorrow_formSearch #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'物料信息',viewWlConfig);
}
var viewWlConfig = {
    width		: "800px",
    height		: "500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};




function searchBorrowingResult(isTurnBack){
    if(isTurnBack){
        $('#chooseBorrow_formSearch #borrowing_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#chooseBorrow_formSearch #borrowing_list').bootstrapTable('refresh');
    }
}
$(function(){
    var oTable= new borrowing_TableInit();
    oTable.Init();
    var oButtonInit = new borrowing_oButtton();
    oButtonInit.Init();
    jQuery('#chooseBorrow_formSearch .chosen-select').chosen({width: '100%'});
})