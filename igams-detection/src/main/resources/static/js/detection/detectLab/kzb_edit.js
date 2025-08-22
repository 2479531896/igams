function initPage(){
    $(".sortable").sortable({
        cursor: "move",
        opacity: 0.6,                       //拖动时，透明度为0.6
        revert: true,                       //释放时，增加动画
        update : function(event, ui){       //更新排序之后
            //每次拖拽都重排列input的id值
            var i=0;
            var xh=0;
            $(".dragsort").each(function (){
                i++;
                var x=0;
                $(this).find("input").each(function(){
                    x++;
                    xh++;
                    $(this).attr("id","ybbh"+x+"-"+i);
                    $(this).attr("xh",xh);
                })
            })
        }
    });

}

function showKzbmx(){
    var kzbid = $("#kzbid").val();
    $.ajax({
        type:'post',
        url:"/detectlab/kzbinfo/pagedataKzbmxList",
        cache: false,
        data: {"kzbid":kzbid,"access_token":$("#ac_tk").val()},
        dataType:'json',
        success:function(data){
            var array=data.kzbmxlist;
            if(array){
	            for (var i=0; i<array.length; i++){
	                var kzbmx = array[i];
	                var h = kzbmx.hs;
	                var l = kzbmx.ls;
	                $("#ybbh"+h+"-"+l).val(kzbmx.ybbh)
	            }
            }
        }
    });
}

function kzb_sjphlr1(){
    $("#ajaxForm #kzsjph").val($("#ajaxForm #sj1").val());
}
function kzb_sjphlr2(){
    $("#ajaxForm #tqsjph").val($("#ajaxForm #sj2").val());
}

var libPreInput;
var libSecondPreInput;
$("#ajaxForm input").on('keydown',function(e){
    if(libSecondPreInput==17 &&libPreInput==16 && e.keyCode==74){
        if($(this).val()=="")
            return false;
        var thisxh=$(this).attr("xh");
        var str = $("input[xh='"+thisxh+"']").val();
        re = new RegExp(",","g");
        rq = new RegExp("，","g");
        var Newstr = str.replace(re, "");
        Newstr = Newstr.replace(rq,"");
        $("input[xh='"+thisxh+"']").val(Newstr);
        if($(this).val().length >0){
            var nextxh=parseInt(thisxh)+1;
            if(nextxh==97){
                $("input[xh='1']").focus();
            }else{
                $("input[xh='"+nextxh+"']").focus();
            }
        }
    }else if (e.keyCode == 13) {
        if($(this).val()=="")
            return false;
        var thisxh=$(this).attr("xh");
        var str = $("input[xh='"+thisxh+"']").val();
        re = new RegExp(",","g");
        rq = new RegExp("，","g");
        var Newstr = str.replace(re, "");
        Newstr = Newstr.replace(rq,"");
        $("input[xh='"+thisxh+"']").val(Newstr);
        $(this).val($(this).val().toUpperCase());
        var nextxh=parseInt(thisxh)+1;
        if(nextxh==97){
            $("input[xh='1']").focus();
        }else{
            $("input[xh='"+nextxh+"']").focus();
        }
    }else if(e.keyCode != 13 && e.keyCode != 16&& e.keyCode != 17&& e.keyCode != 74&& e.keyCode != 37){
        return true;
    }
    libSecondPreInput = libPreInput
    libPreInput = e.keyCode
    return false;
});

$(document).ready(function(){
    showKzbmx();
    if($("#syrq").val()==""){
        laydate.render({
            elem: '#ajaxForm #syrq'
            ,theme: '#2381E9'
            ,value: new Date()
        });
    }else{
        laydate.render({
            elem: '#ajaxForm #syrq'
            ,theme: '#2381E9'
        });
    }
    //初始化页面数据
    initPage();
    jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});
    //打开新增页面光标显示在第一个输入框
    setTimeout(function(){
        $("#ybbh1-1").focus();
    },200);
});



