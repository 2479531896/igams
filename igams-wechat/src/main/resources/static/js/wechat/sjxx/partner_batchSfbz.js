// ====================整改收费标准模块=====================
var t_map=[];
//初始化收费标准json字段
function setSfbz_json(){
    var json=[];
    var data=$('#ajaxForm_batchmodSfbz #tb_sfbz_list').bootstrapTable('getData');//获取选择行数据
    for(var i=0;i<data.length;i++){
        var sz={"index":data.length,"hbid":'',"xm":'',"zxm":'',"sfbz":'',"tqje":'',"ksrq":'',"jsrq":''};
        sz.hbid=data[i].hbid;
        sz.xm=data[i].xm;
        sz.zxm=data[i].zxm;
        sz.sfbz=data[i].sfbz;
        sz.tqje=data[i].tqje;
        sz.ksrq=data[i].ksrq;
        sz.jsrq=data[i].jsrq;
        json.push(sz);
    }
    $("#ajaxForm_batchmodSfbz #sfbz_json").val(JSON.stringify(json));
}

//新增明细
function addnewSfbz(){
    var data=JSON.parse($("#ajaxForm_batchmodSfbz #sfbz_json").val());
    var a={"index":data.length,"hbid":'',"xm":'',"zxm":'',"sfbz":'',"tqje":'',"ksrq":'',"jsrq":''};
    t_map.rows.push(a);
    data.push(a);
    $("#ajaxForm_batchmodSfbz #sfbz_json").val(JSON.stringify(data));
    $("#ajaxForm_batchmodSfbz #tb_sfbz_list").bootstrapTable('load',t_map);
}
var Sfbz_TableInit=function(){
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#ajaxForm_batchmodSfbz #tb_sfbz_list").bootstrapTable({
            url: '/partner/partner/pagedataGetSfzbs',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#ajaxForm_batchmodSfbz #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: false,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: "sfbz.hbid",				//排序字段
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
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "hbid",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            paginationDetailHAlign:' hidden',
            columns: [{
                field: 'hbid',
                title: '伙伴id',
                width: '10%',
                align: 'left',
                visible:false
            },{
                field: 'xm',
                title: '项目',
                width: '26%',
                align: 'left',
                visible: true,
                formatter:xmformat,
            },{
                field: 'zxm',
                title: '子项目',
                width: '18%',
                align: 'left',
                visible: true,
                formatter:zxmformat,
            },{
                field: 'sfbz',
                title: '收费标准',
                width: '10%',
                align: 'left',
                visible: true,
                formatter:sfbzformat,
            },{
                field: 'tqje',
                title: '提取金额',
                width: '10%',
                align: 'left',
                visible: true,
                formatter:tqjeformat,
            },{
                field: 'ksrq',
                title: '开始日期',
                width: '10%',
                align: 'left',
                visible: true,
                formatter:ksrqformat,
            },{
                field: 'jsrq',
                title: '结束日期',
                width: '10%',
                align: 'left',
                visible: true,
                formatter:jsrqformat,
            },{
                field: 'cz',
                title: '操作',
                width: '6%',
                align: 'left',
                formatter:czformat,
                visible: true
            }],
            onLoadSuccess:function(map){
                t_map=map;
                // if( map.rows == null || map.rows == undefined || map.rows.length <= 0 ){
                // 	addnewSfbz()
                // }
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            },
        });
        $("#ajaxForm_batchmodSfbz #tb_sfbz_list").colResizable({
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
            sortLastName: "sfbz.hbid", // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return map;
    };
    return oTableInit;
}
/**
 * 收费标准---项目格式化 (该方法是选择基础数据项目)
 * @param value
 * @param row
 * @param index
 * @returns
 */
function xmformat(value,row,index){
    var xmList = JSON.parse($("#ajaxForm_batchmodSfbz #xmList").val());
    var html="";
    if (xmList && xmList.length >0) {
        html +="<div id='xmdiv_"+index+"' name='xmdiv' style='width:100%;display:inline-block'>";
        html +=  "<select id='xm_"+index+"' name='xm_"+index+"' class='form-control'  onchange=\"checkXm('"+index+"')\">";
        html += "<option value=''>请选择--</option>";
        for(var i=0;i<xmList.length;i++){
            if(row.xm==xmList[i].csid){
                html += "<option value='"+xmList[i].csid+"' selected >"+xmList[i].csmc+"</option>";
            }else{
                html += "<option value='"+xmList[i].csid+"' >"+xmList[i].csmc+"</option>";
            }
        }
        html += "</select></div>";
    }else{
        html += "<span  >"+value+"</span>";
    }
    return html;
}
function checkXm(index){
    //修改项目值
    t_map.rows[index].xm = $("#ajaxForm_batchmodSfbz #xm_" + index).find("option:selected").val();
    //子项目根据项目去限制选择范围
    var xmnew = $("#ajaxForm_batchmodSfbz #xm_" + index).find("option:selected").val();
    var zxmList = JSON.parse($("#ajaxForm_batchmodSfbz #zxmList").val());
    var html="";
    if (zxmList && zxmList.length >0) {
        html += "<option value=''>请选择--</option>";
        if( xmnew != null && xmnew != undefined && xmnew != ''){
            for(var i=0;i<zxmList.length;i++){
                if( xmnew == zxmList[i].fcsid ){
                    html += "<option value='"+zxmList[i].csid+"' >"+zxmList[i].csmc+"</option>";
                }
            }
            $("#ajaxForm_batchmodSfbz #zxm_"+index).empty();
            $("#ajaxForm_batchmodSfbz #zxm_"+index).append(html);
            $("#ajaxForm_batchmodSfbz #zxm_"+index).trigger("chosen:updated");
        }else{//项目id不存在，zxm无选择项
            $("#ajaxForm_batchmodSfbz #zxm_"+index).empty();
            $("#ajaxForm_batchmodSfbz #zxm_"+index).append(html);
            $("#ajaxForm_batchmodSfbz #zxm_"+index).trigger("chosen:updated");
        }
        html += "</select></div>";
    }
}


/**
 * 收费标准---子项目格式化 (该方法是选择基础数据项目)
 * @param value
 * @param row
 * @param index
 * @returns
 */
function zxmformat(value,row,index){
    var zxmList = JSON.parse($("#ajaxForm_batchmodSfbz #zxmList").val());
    var html="";
    if (zxmList && zxmList.length >0) {
        html +="<div id='zxmdiv_"+index+"' name='zxmdiv' style='width:100%;display:inline-block'>";
        html +=  "<select id='zxm_"+index+"' name='zxm_"+index+"' class='form-control'  onchange=\"checkZxm('"+index+"')\">";
        html += "<option value=''>请选择--</option>";
        if(row.xm != null && row.xm != undefined && row.xm != ''){
            for(var i=0;i<zxmList.length;i++){
                if(row.zxm==zxmList[i].csid){
                    html += "<option value='"+zxmList[i].csid+"' selected >"+zxmList[i].csmc+"</option>";
                }else if(row.xm==zxmList[i].fcsid){
                    html += "<option value='"+zxmList[i].csid+"' >"+zxmList[i].csmc+"</option>";
                }
            }
            $("#ajaxForm_batchmodSfbz #zxm_"+index).empty();
            $("#ajaxForm_batchmodSfbz #zxm_"+index).append(html);
        }else{//项目id不存在，zxm无选择项
            $("#ajaxForm_batchmodSfbz #zxm_"+index).empty();
            $("#ajaxForm_batchmodSfbz #zxm_"+index).append(html);
        }
        html += "</select></div>";
    }else{
        html += "<span  >"+value+"</span>";
    }
    $("#ajaxForm_batchmodSfbz #zxm_"+index).trigger("chosen:updated");
    return html;
}
function checkZxm(index){
    t_map.rows[index].zxm = $("#ajaxForm_batchmodSfbz #zxm_" + index).find("option:selected").val();
}
function sfbzformat(value,row,index){
    var html="";
    if(row.sfbz == null ||row.sfbz == undefined){
        row.sfbz="";
    }
    var html="<input id='sfbz_"+index+"' type='number' placeholder='填写数字' name='sfbz_"+index+"' class='sfbz' value='"+row.sfbz+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"tochangeZd('"+index+"',this,\'sfbz\')\" >";
    return html;
}
function tqjeformat(value,row,index){
    var html="";
    if(row.tqje == null ||row.tqje == undefined){
        row.tqje="";
    }
    var html="<input id='tqje_"+index+"' type='number' placeholder='填写数字' name='tqje_"+index+"' class='tqje' value='"+row.tqje+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"tochangeZd('"+index+"',this,\'tqje\')\" >";
    return html;
}
function tochangeZd(index,e,zdmc){
    if (zdmc=='sfbz'){
        t_map.rows[index].sfbz=e.value;
    }else if(zdmc=='tqje'){
        t_map.rows[index].tqje=e.value;
    }else if(zdmc=='ksrq'){
        t_map.rows[index].ksrq=e.value;
    }else if(zdmc=='jsrq'){
        t_map.rows[index].jsrq=e.value;
    }
}

function ksrqformat(value,row,index){
    if(row.ksrq==null || row.ksrq == undefined){
        row.ksrq="";
    }
    var html="<input id='ksrq_"+index+"' name='ksrq_"+index+"' placeholder='yyyy-mm-dd' class='ksrq' value='"+row.ksrq+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"tochangeZd('"+index+"',this,\'ksrq\')\" >";
    setTimeout(function() {
        laydate.render({
            elem: '#ajaxForm_batchmodSfbz #ksrq_'+index
            ,theme: '#2381E9'
            // , type: 'datetime'
            // , format: 'yyyy-MM-dd HH:mm:ss'//保留时分
            ,min: 0
            ,btns: ['clear', 'confirm']
            ,done: function(value, date, endDate){
                t_map.rows[index].ksrq=value;
            }
        });
    }, 100);
    return html;
}
function jsrqformat(value,row,index){
    if(row.jsrq==null || row.jsrq == undefined){
        row.jsrq="";
    }
    var html="<input id='jsrq_"+index+"' name='jsrq_"+index+"' placeholder='yyyy-mm-dd' class='jsrq' value='"+row.jsrq+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"tochangeZd('"+index+"',this,\'jsrq\')\" >";
    setTimeout(function() {
        laydate.render({
            elem: '#ajaxForm_batchmodSfbz #jsrq_'+index
            ,theme: '#2381E9'
            ,min: 0
            ,btns: ['clear', 'confirm']
            ,done: function(value, date, endDate){
                t_map.rows[index].jsrq=value;
            }
        });
    }, 100);
    return html;
}

/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function czformat(value,row,index){
    var html = "<span id='sfbz_delbtn' style='margin-left:5px;height: auto !important;' class='btn btn-default btn-sm' onclick=\"delSfbz('" + index + "',event)\" >删除</span>";
    return html;
}
/**
 * 删除操作(删除收费标准)
 * @param index
 * @param event
 * @returns
 */
function delSfbz(index,event){
    var t_data = JSON.parse($("#ajaxForm_batchmodSfbz #sfbz_json").val());
    t_map.rows.splice(index,1);
    for(var i=0;i<t_data.length;i++){
        if(t_data[i].index == index){
            t_data.splice(i,1);
        }
    }
    $("#ajaxForm_batchmodSfbz #tb_sfbz_list").bootstrapTable('load',t_map);
    var json=[];
    for(var i=0;i<t_data.length;i++){
        var sz={"index":i,"hbid":'',"xm":'',"zxm":'',"sfbz":'',"tqje":'',"ksrq":'',"jsrq":''};
        sz.hbid=t_data[i].hbid;
        sz.xm=t_data[i].xm;
        sz.zxm=t_data[i].zxm;
        sz.sfbz=t_data[i].sfbz;
        sz.tqje=t_data[i].tqje;
        sz.ksrq=t_data[i].ksrq;
        sz.jsrq=t_data[i].jsrq;
        json.push(sz);
    }
    $("#ajaxForm_batchmodSfbz #sfbz_json").val(JSON.stringify(json));
}



$(function(){
    //初始化收费标准表
    var oTable=new Sfbz_TableInit();
    oTable.Init();
    setSfbz_json();
    jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
});