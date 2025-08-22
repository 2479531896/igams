var kssjstart=null;
var kssjend=null;
var jssjstart=null;
var jssjend=null;
var jcdws="";
var value="";
var csnr="";
$("#wbybxx_formSearch #btn_query").click(function(){
    var cxnr=$("#wbybxx_formSearch #cxnr").val();
    var syzt=$("#wbybxx_formSearch #syzt").val();
    searchData(syzt,cxnr);
})

function changetab(value,str){
    var syzt=$("#wbybxx_formSearch #syzt").val();
    $("#wbybxx_formSearch #btn_syz").removeClass();
    $("#wbybxx_formSearch #btn_ywc").removeClass();
    $("#wbybxx_formSearch #btn_lsjl").removeClass();
    $("#wbybxx_formSearch #btn_syz").addClass("btn btn-default");
    $("#wbybxx_formSearch #btn_ywc").addClass("btn btn-default");
    $("#wbybxx_formSearch #btn_lsjl").addClass("btn btn-default");
    $("#wbybxx_formSearch #"+str).removeClass();
    $("#wbybxx_formSearch #"+str).addClass("btn btn-primary");
    if(syzt==value){
        return;
    }else{
        searchData(value,null);
    }
}

function searchData(value,cxnr){
    $.ajax({
        async:false,
        type:'post',
        url:"/ws/sample/samplelistdata",
        cache: false,
        data: {"syzt":value,"cxnr":cxnr,"kssjstart":kssjstart,"kssjend":kssjend,"jssjstart":jssjstart,"jssjend":jssjend,"jcdws":jcdws},
        dataType:'json',
        success:function(data){
            $("#wbybxx_formSearch #tb").empty();
            var t_html="";
            var html="<tbody>";
            if(data.list.length>0){
                if(value!='1'){
                    t_html="<thead>"+
                                "<tr>"+
                                  "<th>标本编码</th>"+
                                  "<th>开始时间</th>"+
                                  "<th>结束时间</th></tr>"+
                              "</thead>";
                    $("#wbybxx_formSearch #tb").append(t_html);
                }
                for(var i=0;i<data.list.length;i++){
                    html+="<tr>"+
                        "<td >"+data.list[i].bbbm+"</td>";
                        if(value!='1'){
                           html+= "<td style='color:green;'>"+(data.list[i].kssj!=null?data.list[i].kssj:'')+"</td>"+
                            "<td style='color:orange;'>"+(data.list[i].jssj!=null?data.list[i].jssj:'')+"</td>";
                        }else{
                           html+= "<td style='color:green;'>"+(data.list[i].lcmc!=null?data.list[i].lcmc:'')+(data.list[i].lcxh!=null?data.list[i].lcxh:'')+"</td>"+
                           "<td style='color:orange;'>"+(data.list[i].zlcmc!=null?data.list[i].zlcmc:'')+(data.list[i].zlcxh!=null?data.list[i].zlcxh:'')+"</td>";
                        }

                    html+="</tr>";
                }
                html+="</tbody>";
            }
            $("#wbybxx_formSearch #tb").append(html);
            $("#wbybxx_formSearch #syzt").val(value);
        }
    });
}

/**
 * 根据条件查询
 */
function searchReportResult(){

    // 模糊查询内容
    cxnr = $('#wbybxx_formSearch #cxnr').val();
    //开始日期
    kssj = $('#kssj').val();
    //接收日期
    jssj = $('#jssj').val();
    jcdws="";
    $("#wbybxx_formSearch input[name='jcdw']").each(function(){
        var flag=$(this).prop("checked");
        if(flag){
            jcdws += ","+$(this).val();
        }
    });
    if(jcdws){
        jcdws = jcdws.substring(1);
    }
    $("#wbybxx_formSearch #dqjcdw").val($("#jcdwFw input[checked]").attr("jcdwmc"));
    value= $('#wbybxx_formSearch #syzt').val();
    searchData(value,cxnr);
}

function changeKssjStart(){
    weui.datePicker({
            id: 'datePickerKssjStart',
            onChange: function(result){
            },
            onConfirm: function(result){
                kssjstart = result[0].value + '-' + ((result[1].value>9)?result[1].value:'0'+result[1].value) + '-' + ((result[2].value>9)?result[2].value:'0'+result[2].value)
                $("#kssjstart").val(kssjstart)
            }
        });
}
function changeKssjEnd(){
    weui.datePicker({
            id: 'datePickerKssjEnd',
            onChange: function(result){
            },
            onConfirm: function(result){
                kssjend = result[0].value + '-' + ((result[1].value>9)?result[1].value:'0'+result[1].value) + '-' + ((result[2].value>9)?result[2].value:'0'+result[2].value)
                $("#kssjend").val(kssjend)
            }
        });
}
function changeJssjStart(){
    weui.datePicker({
        id: 'datePickerJssjStart',
        onChange: function(result){
        },
        onConfirm: function(result){
            jssjstart = result[0].value + '-' + ((result[1].value>9)?result[1].value:'0'+result[1].value) + '-' + ((result[2].value>9)?result[2].value:'0'+result[2].value)
            $("#jssjstart").val(jssjstart)
        }
    });
}
function changeJssjEnd(){
    weui.datePicker({
        id: 'datePickerJssjEnd',
        onChange: function(result){
        },
        onConfirm: function(result){
            jssjend = result[0].value + '-' + ((result[1].value>9)?result[1].value:'0'+result[1].value) + '-' + ((result[2].value>9)?result[2].value:'0'+result[2].value)
            $("#jssjend").val(jssjend);
        }
    });
}
/**
 * 清空筛选条件
 */
function emptyFilter(){
    $("#jssjstart").val("");
    jssjstart="";
    $("#jssjend").val("");
    jssjend="";
    $("#kssjstart").val("");
    kssjstart="";
    $("#kssjend").val("");
    kssjend="";
}

function viewSample(ybxxid){
    $("#wbybxx_formSearch").load("/ws/sample/viewSampleFlowpath?ybxxid="+ybxxid);
}
$(function(){

});