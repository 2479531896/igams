
$(function(){
    var ua = navigator.userAgent.toLowerCase();
    if (ua.indexOf("micromessenger") > 0||ua.indexOf('dingtalk') > 0) {
        console.log("微信")
        location.href=$("#phonewxurl").val()+"?ywid="+$("#phonefileDiv #ywid").val()+"&ywlx="+$("#phonefileDiv #ywlx").val()+"&ywmc="+$("#phonefileDiv #ywmc").val()
    }else{
        location.href=$("#phonewxurl").val()+"?ywid="+$("#phonefileDiv #ywid").val()+"&ywlx="+$("#phonefileDiv #ywlx").val()+"&ywmc="+$("#phonefileDiv #ywmc").val()
    }


})


