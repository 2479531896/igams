var Bcht_TableInit = function () {
    var oTableInit = new Object();
    // 初始化Table
    oTableInit.Init = function () {
        $('#remindPaymentForm #htxx_list').bootstrapTable({
            url: $("#remindPaymentForm #urlPrefix").val()+'/contract/contract/pagedataQueryBchtmx?htid='+$("#remindPaymentForm #htid").val(),         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#remindPaymentForm #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"xh",					// 排序字段
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
            isForceTable: true,
            showColumns: false,                  // 是否显示所有的列
            showRefresh: false,                  // 是否显示刷新按钮
            minimumCountColumns: 2,             // 最少允许的列数
            clickToSelect: true,                // 是否启用点击选中行
            // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "htmxid",                     // 每一行的唯一标识，一般为主键列
            showToggle:false,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                'field': '',
                'title': '序号',
                'align': 'center',
                'width': '3%',
                'formatter': function (value, row, index) {
                    var options = $('#remindPaymentForm #htxx_list').bootstrapTable('getOptions');
                    return options.pageSize * (options.pageNumber - 1) + index + 1;
                }
            }, {
                field: 'wlbm',
                title: '物料编码',
                width: '6%',
                align: 'left',
                visible: true
            }, {
                field: 'wlmc',
                title: '物料名称',
                width: '15%',
                align: 'left',
                visible: true
            }, {
                field: 'gg',
                title: '规格',
                width: '15%',
                align: 'left',
                visible: true
            },{
                field: 'jldw',
                title: '单位',
                width: '4%',
                align: 'left',
                visible: true
            },{
                field: 'sl',
                title: '数量',
                width: '25%',
                align: 'left',
                formatter:bcht_slformat,
                visible: true
            },{
                field: 'hsdj',
                title: '含税单价',
                width: '25%',
                align: 'left',
                formatter:bcht_hsdjformat,
                visible: true
            },{
                field: 'hjje',
                title: '合计金额',
                width: '25%',
                align: 'left',
                formatter:bcht_hjjeformat,
                visible: true
            }],
            onLoadSuccess: function (map) {

            },
//            rowStyle: function(row,index){
//                if(row.bchtsl!=null && row.bchtsl!=''){
//                    return{
//                        css:{"background-color":'rgb(59,232,102)'}
//                    }
//                }else{
//                    return "";
//                }
//            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
        });
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
            pageSize: 1,   // 页面大小
            pageNumber:  1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "fktxid", // 防止同名排位用
            sortLastOrder: "desc", // 防止同名排位用
            htid:$("#remindPaymentForm #fktxid").val()
            // 搜索框使用
            // search:params.search
        };
        return map;
    };
    return oTableInit;
};
function bcht_slformat(value,row,index){
    var sl = parseFloat(row.sl);
    var bcsl = row.bchtsl;
    var endsl = bcsl;
    var startsl = '';
    var title = row.sl;
    var html="<span style='color:orange;'>"+ row.sl +"</span>";
    if(bcsl!=null && bcsl!=''){
        if(bcsl.includes('→')){
            var index = bcsl.lastIndexOf('→');
            //截取最后一个->后的数据
            endsl = bcsl.substring(index + 1);
            //截取最后一个->前的数据
            startsl = bcsl.substring(0, index+1);
        }
        html = html + "<span>→"+ startsl +"</span>";
        title = title + "→" + startsl + endsl;
        //比较数量是增加了还是减少了
        var endslFloat = parseFloat(endsl);
        if(sl>endslFloat){
            html = html + "<span style='color:green;'>"+ endsl +"↓</span>";
            title = title + "↓";
        }else if(sl<endslFloat){
            html = html + "<span style='color:red;'>"+ endsl +"↑</span>";
            title = title + "↑";
        }else{
            html = html + "<span>"+ endsl +"</span>";
        }
        return "<span title='"+title+"'>" + html + "</span>";
    }else{
        return "<span title='"+row.sl+"'>"+ row.sl +"</span>";
    }
}

function bcht_hsdjformat(value,row,index){
    var hsdj = parseFloat(row.hsdj);
    var bchsdj = row.bchthsdj;
    var endhsdj = bchsdj;
    var starthsdj = '';
    var title = row.hsdj;
    var html="<span style='color:orange;'>"+ row.hsdj +"</span>";
    if(bchsdj!=null && bchsdj!=''){
        if(bchsdj.includes('→')){
            var index = bchsdj.lastIndexOf('→');
            //截取最后一个->后的数据
            endhsdj = bchsdj.substring(index + 1);
            //截取最后一个->前的数据
            starthsdj = bchsdj.substring(0, index+1);
        }
        html = html + "<span>→"+ starthsdj +"</span>";
        title = title + "→" +starthsdj + endhsdj;
        //比较数量是增加了还是减少了
        var endhsdjFloat = parseFloat(endhsdj);
        if(hsdj>endhsdjFloat){
            html = html + "<span style='color:green;'>"+ endhsdj +"↓</span>";
            title = title + "↓";
        }else if(hsdj<endhsdjFloat){
            html = html + "<span style='color:red;'>"+ endhsdj +"↑</span>";
            title = title + "↑";
        }else{
            html = html + "<span>"+ endhsdj +"</span>";
        }
        return "<span title='"+title+"'>" + html + "</span>";
    }else{
        return "<span title='"+row.hsdj+"'>"+ row.hsdj +"</span>";
    }
}

function bcht_hjjeformat(value,row,index){
    var hjje = parseFloat(row.hjje);
    var bchjje = row.bchthjje;
    var endhjje = bchjje;
    var starthjje = '';
    var title = row.hjje;
    var html="<span style='color:orange;'>"+ row.hjje +"</span>";
    if(bchjje!=null && bchjje!=''){
        if(bchjje.includes('→')){
            var index = bchjje.lastIndexOf('→');
            //截取最后一个->后的数据
            endhjje = bchjje.substring(index + 1);
            //截取最后一个->前的数据
            starthjje = bchjje.substring(0, index +1);
        }
        html = html + "<span>→"+ starthjje +"</span>";
        title = title + "→" +starthjje + endhjje;
        //比较数量是增加了还是减少了
        var endhjjeFloat = parseFloat(endhjje);
        if(hjje>endhjjeFloat){
            html = html + "<span style='color:green;'>"+ endhjje +"↓</span>";
            title = title + "↓";
        }else if(hjje<endhjjeFloat){
            html = html + "<span style='color:red;'>"+ endhjje +"↑</span>";
            title = title + "↑";
        }else{
            html = html + "<span>"+ endhjje +"</span>";
        }
        return "<span title='"+title+"'>" + html + "</span>";
    }else{
        return "<span title='"+row.hjje+"'>"+ row.hjje +"</span>";
    }
}


var t_map=[];
var payRemind_TableInit = function () {
    var oTableInit = new Object();
    // 初始化Table
    oTableInit.Init = function () {
        $('#remindPaymentForm #txjd_list').bootstrapTable({
            url: $("#remindPaymentForm #urlPrefix").val()+'/contract/contract/pagedataListPaymentRemind?htid='+$("#remindPaymentForm #htid").val(),         // 请求后台的URL（*）
            method: 'get',                      // 请求方式（*）
            toolbar: '#remindPaymentForm #toolbar', // 工具按钮用哪个容器
            striped: true,                      // 是否显示行间隔色
            cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   // 是否显示分页（*）
            paginationShowPageGo: true,         //增加跳转页码的显示
            sortable: true,                     // 是否启用排序
            sortName:"xh",					// 排序字段
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
            isForceTable: true,
            showColumns: false,                  // 是否显示所有的列
            showRefresh: false,                  // 是否显示刷新按钮
            minimumCountColumns: 2,             // 最少允许的列数
            clickToSelect: true,                // 是否启用点击选中行
            // height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "fktxid",                     // 每一行的唯一标识，一般为主键列
            showToggle:false,                    // 是否显示详细视图和列表视图的切换按钮
            cardView: false,                    // 是否显示详细视图
            detailView: false,                   // 是否显示父子表
            columns: [{
                'field': '',
                'title': '序号',
                'align': 'center',
                'width': '3%',
                'formatter': function (value, row, index) {
                    var options = $('#remindPaymentForm #txjd_list').bootstrapTable('getOptions');
                    return options.pageSize * (options.pageNumber - 1) + index + 1;
                }
            }, {
                field: 'yfbl',
                title: '预付比例*',
                width: '8%',
                align: 'left',
                formatter:remind_xyfblformat,
                visible: true
            }, {
                field: 'yfje',
                title: '预付金额*',
                width: '8%',
                align: 'left',
                formatter:remind_yfjeformat,
                visible: true
            }, {
                field: 'yfrq',
                title: '预付日期*',
                width: '8%',
                align: 'left',
                formatter:remind_yfrqformat,
                visible: true
            },{
                field: 'bz',
                title: '备注',
                width: '8%',
                align: 'left',
                formatter:remind_bzformat,
                visible: true
            },{
                field: 'cz',
                title: '操作',
                titleTooltip:'操作',
                width: '6%',
                align: 'left',
                formatter:remind_czformat,
                visible: true
            }],
            onLoadSuccess: function (map) {
                t_map=map;
                var json = [];
                if(t_map.rows){
                    for (var i = 0; i < t_map.rows.length; i++) {
                        var sz = {'index':i,'xh':'',"htid":'',"fktxid":'',"yfbl":'',"yfje":'',"yfrq":'',"bz":''};
                        sz.fktxid = t_map.rows[i].fktxid;
                        sz.xh = t_map.rows[i].xh;
                        sz.htid = t_map.rows[i].htid;
                        sz.yfbl = t_map.rows[i].yfbl;
                        sz.yfje = t_map.rows[i].yfje;
                        sz.yfrq = t_map.rows[i].yfrq;
                        sz.bz = t_map.rows[i].bz;
                        json.push(sz);
                    }
                    $("#remindPaymentForm #fktxJson").val(JSON.stringify(json));
                }
            },
            onLoadError: function () {
                alert("数据加载失败！");
            },
        });
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
            pageSize: 1,   // 页面大小
            pageNumber:  1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: "fktxid", // 防止同名排位用
            sortLastOrder: "desc", // 防止同名排位用
            htid:$("#remindPaymentForm #fktxid").val()
            // 搜索框使用
            // search:params.search
        };
        return map;
    };
    return oTableInit;
};

/**
 * 预付比例格式化
 * @param value
 * @param row
 * @param index
 * @returns {string}
 */
//function remind_yfblformat(value,row,index){
//       var fkje = row.yfje;
//        var htzje = row.zje;
//        if(htzje!=null && htzje!=''){
//            fkjeNum = parseFloat(fkje);
//            htzjeNum = parseFloat(htzje);
//            result = parseFloat(Math.round(fkjeNum/htzjeNum*10000)/100);
//            bfbbj = parseFloat("100");
//            fbfbbj = parseFloat("-100");
//            var bfb="";
//            if(result >bfbbj){
//                bfb="100%";
//            }else if(result < fbfbbj){
//                bfb="-100%";
//            }else{
//                bfb =result+'%';
//            }
//            var html = "<span id='yfbl_"+index+"'>"+bfb+"</span>";
//            return html;
//        }else{
//            return "<span id='yfbl_"+index+"'></span>";
//        }
//}

function remind_xyfblformat(value,row,index){
    //初始化付款百分比
    var fkje = row.yfje;
    var htzje = row.zje;
    if(row.xzje!=null && row.xzje!=''){
        htzje = row.xzje;
    }
    if(htzje!=null && htzje!=''){
        var fkjeNum = parseFloat(fkje);
        var htzjeNum = parseFloat(htzje);
        var result = parseFloat(Math.round(fkjeNum/htzjeNum*10000)/100);
        var bfb = result+'%';
        var html = "<span id='yfbl_"+index+"'>"+bfb+"</span>";
        return html;
    }else{
        return "<span id='yfbl_"+index+"'></span>";
    }
}

/**
 * 预付金额格式化
 * @param value
 * @param row
 * @param index
 * @returns {string}
 */
function remind_yfjeformat(value,row,index){
    if(row.yfje == null){
        row.yfje = "";
    }
    var html = "<input id='yfje_"+index+"' value='"+row.yfje+"' name='yfje_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;'  autocomplete='off' onblur=\"checkSum('"+index+"',this,\'yfje\')\"></input>";
    return html;
}

/**
 * 预付日期格式化
 * @param value
 * @param row
 * @param index
 * @returns {string}
 */
function remind_yfrqformat(value,row,index){
    if(row.yfrq==null){
        row.yfrq="";
    }
    var html="<input id='yfrq_"+index+"'  name='yfrq_"+index+"'  value='"+row.yfrq+"'  style='width:85%;border-radius:5px;border:1px solid #D5D5D5;'></input>";
    setTimeout(function() {
        laydate.render({
            elem: '#remindPaymentForm #yfrq_'+index
            ,theme: '#2381E9'
            ,min: 0
            ,btns: ['clear', 'confirm']
            ,done: function(value, date, endDate){
                var data=JSON.parse($("#remindPaymentForm #fktxJson").val());
                data[index].yfrq=value;
                t_map.rows[index].yfrq=value;
                $("#remindPaymentForm #fktxJson").val(JSON.stringify(data));
            }
        });
    }, 100);
    return html;
}

/**
 * 备注格式化
 * @param value
 * @param row
 * @param index
 * @returns {string}
 */
function remind_bzformat(value,row,index){
    if(row.bz == null){
        row.bz = "";
    }
    var html = "<input id='bz_"+index+"' value='"+row.bz+"' name='bz_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;'  autocomplete='off' onblur=\"checkSum('"+index+"',this,\'bz\')\"></input>";
    return html;
}

/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function remind_czformat(value,row,index){
    var html = "<span style='margin-left:5px;' class='btn btn-default' title='删除提醒' onclick=\"delRemind('" + index + "',event)\" >删除</span>";
    return html;
}

/**
 * 失去焦点事件(修改列表参数)
 * @param id
 * @param e
 * @param zdmc
 * @returns
 */
function checkSum(index, e, zdmc) {
    var data=JSON.parse($("#remindPaymentForm #fktxJson").val());
    for(var i=0;i<data.length;i++){
        if(data[i].index == index){
            if(zdmc=='yfje'){
                data[i].yfje=e.value;
                t_map.rows[i].yfje=e.value;
                yfjeblur(index,data,t_map);
            }else if(zdmc=='bz'){
                data[i].bz=e.value;
                t_map.rows[i].bz=e.value;
            }
        }
    }
    $("#remindPaymentForm #fktxJson").val(JSON.stringify(data));
}

/**
 * 删除提醒
 * @param index
 * @param event
 */
function delRemind(index,event){
    var data=JSON.parse($("#remindPaymentForm #fktxJson").val());
    t_map.rows.splice(index,1);
    data.splice(index,1);
    $("#remindPaymentForm #txjd_list").bootstrapTable("load",t_map);
    $("#remindPaymentForm #fktxJson").val(JSON.stringify(data));
}

//付款金额事件，计算付款百分比
function yfjeblur(index,data,t_map){

    var yfje = $("#remindPaymentForm #yfje_"+index).val();
    var htzje = $("#remindPaymentForm #htzje").val();
    if(yfje==""){
        $("#remindPaymentForm #yfbl_"+index).val("0");
        data[index].yfbl="0";
        t_map.rows[index].yfbl="0";
    }else if(htzje==""){
        $("#remindPaymentForm #yfbl_"+index).val("100");
        data[index].yfbl="100";
        t_map.rows[index].yfbl="100";
    }else if(htzje=="0"){
        $("#remindPaymentForm #yfbl_"+index).val("0");
        data[index].yfbl="0";
        t_map.rows[index].yfbl="0";
    }else{
        yfjeNum = parseFloat(yfje);
        htzjeNum = parseFloat(htzje);
        result = parseFloat(Math.round(yfjeNum/htzjeNum*10000)/100);
        var bfb =result;
        data[index].yfbl=bfb.toString();
        t_map.rows[index].yfbl=bfb.toString();
        var bfbString = bfb.toString();
        document.getElementById("yfbl_"+index).innerText = bfbString+"%";
    }
}

//添加阶段
function editdiv(){
    var data=JSON.parse($("#remindPaymentForm #fktxJson").val());
    var length=t_map.rows.length;
    var sz = {'index':'','xh':'',"htid":'',"fktxid":'',"yfbl":'',"yfje":'',"yfrq":'',"bz":''};
    sz.xh=length+1;
    sz.index=length;
    sz.htid=$("#remindPaymentForm #htid").val();
    data.push(sz);
    t_map.rows.push(sz);
    $("#remindPaymentForm #txjd_list").bootstrapTable("load",t_map);
    $("#remindPaymentForm #fktxJson").val(JSON.stringify(data));
}


$(function(){
    //0.界面初始化
    // 1.初始化Table
    var oTable = new payRemind_TableInit();
    oTable.Init();

    var oTableBc = new Bcht_TableInit();
    oTableBc.Init();

    var zje = $("#remindPaymentForm #zje").val();
    var htzje = $("#remindPaymentForm #bchtzje").val();
    var container = document.getElementById('container');
    var bchtnbbh = document.getElementById("bchtnbbh").textContent;
    if(bchtnbbh!=null && bchtnbbh!='' && htzje!=null && htzje!=''){
        var endzje = htzje;
        var startzje = '';
        if(htzje.includes('→')){
            var index = htzje.lastIndexOf('→');
            //截取最后一个->后的数据
            endzje = htzje.substring(index + 1);
            //截取最后一个->前的数据
            startzje = htzje.substring(0, index+1);
        }
        var endzjeFloat = parseFloat(endzje);
        var zjeFloat = parseFloat(zje);
        var span1 = document.createElement('span');
        span1.style.color = 'orange';
        span1.textContent = zje;
        container.appendChild(span1);
        var span2 = document.createElement('span');
        span2.textContent = "→"+startzje;
        container.appendChild(span2);
        if(zjeFloat>endzjeFloat){
            var span3 = document.createElement('span');
            span3.style.color = 'green';
            span3.textContent = endzje +'↓';
            container.appendChild(span3);
        }else if(zjeFloat<endzjeFloat){
            var span4 = document.createElement('span');
            span4.style.color = 'red';
            span4.textContent = endzje +'↑';
            container.appendChild(span4);
        }else{
            var span5 = document.createElement('span');
            span5.textContent = endzje;
            container.appendChild(span5);
        }
    }else{
        var span6 = document.createElement('span');
        span6.textContent = zje;
        container.appendChild(span6);
        endzje = zje;
    }
    $("#remindPaymentForm #htzje").val(endzje);
});

