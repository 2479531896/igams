var xsbs = 10;
var bp = 452;
var xybp = 452;
var qtbp = 452;
var dly = '稀释液';
function btnBind() {
    var length=$("#ajaxForm #length").val();
    if(length!=null && length!=''){
        if(parseInt(length)>0){
            for(var i=parseInt(length)+1;i<=192;i++){
                var html="<tr class='wksjmc'>"+
                    "<td text='"+i+"'>"+i+"</td>"+
                    "<td id='ybbh'><input id='ybbh-"+i+"'  style='height:100%;border:0px;width:100%;' name='quantity' ></td>"+
                    "<td id='wkbm'><input id='wkbm-"+i+"'  style='height:100%;border:0px;width:100%;' name='quantity' xh='"+i+"'></td>"+
                    "<td><input id='jt1-"+i+"'  style='height:100%;border:0px;width:100%;' name='quantity' ></td>"+
                    "<td><input id='jt2-"+i+"'  style='height:100%;border:0px;width:100%;' name='quantity' ></td>"+
                    "<td><input id='spike-"+i+"'  style='height:100%;border:0px;width:100%;' name='quantity' ></td>"+
                    "<td><input id='dnand-"+i+"'  style='height:100%;border:0px;width:100%;' name='quantity' ></td>"+
                    "<td><input id='hsnd-"+i+"'  style='height:100%;border:0px;width:100%;' name='quantity' ></td>"+
                    "<td><input id='wknd-"+i+"'  style='height:100%;border:0px;width:100%;' name='quantity' ></td>"+
                    "<td id='dlnd'><input id='dlnd-"+i+"'  style='height:100%;border:0px;width:100%;' name='quantity' ></td>"+
                    "<td id='tj'><input id='tj-"+i+"'  style='height:100%;border:0px;width:100%;' name='quantity' ></td>"+
                    "<td id='hbnd'><input id='hbnd-"+i+"'  style='height:100%;border:0px;width:100%;' name='quantity' ></td>"+
                    "<td id='nddw'><input id='nddw-"+i+"'  style='height:100%;border:0px;width:100%;' name='quantity' ></td>"+
                    "<td><input id='yy-"+i+"'  style='height:100%;border:0px;width:100%;' name='quantity' ></td>"+
                    "<td><input id='yjxjsjl-"+i+"'  style='height:100%;border:0px;width:100%;' name='quantity' ></td>"+
                    "<td><input id='xsbs-"+i+"'  style='height:100%;border:0px;width:100%;' name='quantity' ></td>"+
                    "<td><input id='project_type-"+i+"'  style='height:100%;border:0px;width:100%;' name='quantity' ></td>"+
                    "<input id='xmdm-"+i+"'  style='height:100%;border:0px;width:100%;' name='quantity' type='hidden'>"+
                    "<input id='syglid-"+i+"'  style='height:100%;border:0px;width:100%;' name='quantity' type='hidden'>"+
                    "<input id='nbbm-"+i+"'  style='height:100%;border:0px;width:100%;' name='quantity' type='hidden'>"+
                    "</tr>";
                $("#ajaxForm #trmx").append(html);
            }
        }
    }
    var sel_xpxx = $("#ajaxForm #xpxx");
    //芯片下拉框事件
    sel_xpxx.unbind("change").change(function(){
        xpxxEvent();
    });
    //测序仪下拉框事件
    var sel_cxy = $("#ajaxForm #cxyid");
    sel_cxy.unbind("change").change(function(){
        cxyEvent();
    })
    /**
     * 监听用户粘贴事件
     * @param e
     * @returns
     */
    $("#ajaxForm input").on("paste",function(e){
        var qianzhuiList = e.currentTarget.id.split('-');
        // if (qianzhuiList[1] == "1"){
        e.preventDefault();
        var qianzhui = qianzhuiList[0];
        var quantitys=e.originalEvent.clipboardData.getData("text");
        var arr = quantitys.replaceAll(" ","").split('\r\n').map(function(item) {
            return item. split("\t");
        });
        var y = arr.length;
        var index=qianzhuiList[1];
        for (var i = 0; i < y; index++,i++) {
            if (index <193){
                if (i==y-1 && !(arr[i]!=null && arr[i]!='')){
                    continue;
                }
                $("#"+qianzhui+"-"+index).val(arr[i]);
                $("#"+qianzhui+"-"+index).attr("title",arr[i]);
                $("#"+qianzhui+"-"+index).blur();
                if(qianzhui == "dlnd"){
                    if(!$("#ajaxForm #wknd-"+index).val()){
                        var pddx = $("#ajaxForm #wkbm-"+index).val().indexOf("B-DNA")?xybp:qtbp;
                        if("稀释液" == dly){
                            //稀释液 文库浓度 = 定量浓度 * 文库定量标曲片段长度 / 片段大小 * 稀释倍数
                            $("#ajaxForm #wknd-"+index).val((arr[i] * bp * xsbs / pddx).toFixed(2))
                        }else if("原液"== dly){
                            //原液 文库浓度 = 定量浓度 * 文库定量标曲片段长度 / 片段大小
                            $("#ajaxForm #wknd-"+index).val((arr[i] * bp / pddx).toFixed(2))
                        }
                    }
                }
            }
        }
        // }

    })

    $("#ajaxForm #wkbm input").blur(function(){
        var xh=$(this).attr("xh");
        var wkbm=$(this).val();
        if(!wkbm){
            $("#ajaxForm #xmdm-"+xh).val('');
            $("#ajaxForm #syglid-"+xh).val('');
        }else{
            var split = wkbm.split("-");
            if(split.length>3){
                var thisnbbh="";
                var rqindex = 0;
                for(var i=split.length-1;i>=0;i--){
                    if(split[i].length == 8)
                    {
						rqindex = i-1;
						break;
					}
                }
                if(rqindex ==0)
                	rqindex = split.length-1;
                for(var i=1;i<rqindex;i++){
                    thisnbbh=thisnbbh+'-'+split[i];
                }
                thisnbbh=thisnbbh.substring(1);
                getDetectionData(thisnbbh,xh,wkbm);
                // 直接在后台处理，无需前台提交两次
                /*if(!$("#ajaxForm #syglid-"+xh).val()){
                    thisnbbh="";
                    for(var i=1;i<rqindex-1;i++){
                        thisnbbh=thisnbbh+'-'+split[i];
                    }
                    thisnbbh=thisnbbh.substring(1);
                    getDetectionData(thisnbbh,xh,wkbm);
                }*/
            }
        }
    });

    $("#ajaxForm #wkbm input").focus(function(){
        var xh=$(this).attr("xh");
        var wkbm=$(this).val();
        $("#ajaxForm #ywkbm-"+xh).val(wkbm);
    });


    $("#ajaxForm #dlnd input").blur(function(event) {
        var dlndid = event.target.id;
        var idIndex = dlndid.split("-")[1];
        if(!$("#ajaxForm #wknd-"+idIndex).val()){
            var pddx = $("#ajaxForm #wkbm-"+idIndex).val().indexOf("B-DNA")?xybp:qtbp;
            if("稀释液" == dly){
                //稀释液 文库浓度 = 定量浓度 * 文库定量标曲片段长度 / 片段大小 * 稀释倍数
                $("#ajaxForm #wknd-"+idIndex).val((event.target.value * bp * xsbs / pddx).toFixed(2))
            }else if("原液"== dly){
                //原液 文库浓度 = 定量浓度 * 文库定量标曲片段长度 / 片段大小
                $("#ajaxForm #wknd-"+idIndex).val((event.target.value * bp / pddx).toFixed(2))
            }
        }
    });

}

function cxyEvent(){
    var cxyid=$("#ajaxForm #cxyid").val();
    //  根据测序仪名称获取index2标记
    $.ajax({
        type:'post',
        url:"/experiment/library/pagedataCxyxxByID",
        cache: false,
        data: {"cxyid":cxyid,"access_token":$("#ac_tk").val(),"wkid":$("#wkid").val()},
        dataType:'json',
        success:function(data){
            if(data.error){
                $.alert(data.error);
                return;
            }
            //返回值
            var cxyxxDto=data.cxyxxDto;
            if(cxyxxDto!=null){
                if(cxyxxDto.jt2bj=='1'){
                    for(var i=0;i<$("#ajaxForm .wksjmc").length;i++){
                        if($("#ajaxForm #wkbm-"+(i+1)).val()!=null && $("#ajaxForm #wkbm-"+(i+1)).val()!='' && $("#ajaxForm #jt2-"+(i+1)).val().substring($("#ajaxForm #jt2-"+(i+1)).val().length-1,$("#ajaxForm #jt2-"+(i+1)).val().length)!="R"){
                            $("#ajaxForm #jt2-"+(i+1)).attr("title",$("#ajaxForm #jt2-"+(i+1)).val()+"R");
                            $("#ajaxForm #jt2-"+(i+1)).val($("#ajaxForm #jt2-"+(i+1)).val()+"R");
                        }
                    }
                    //处理加R和去R单选样式
                    $("#ajaxForm #option-1").attr("checked",true);
                }else{
                    //处理加R和去R单选样式
                    $("#ajaxForm #option-2").attr("checked",true);
                }
                if(data.sfjyh!='0'){
                    var length=$("#ajaxForm #trmx tr").length;
                    for(var i=0;i<length;i++){
                        if($("#ajaxForm #wkbm-"+(i+1)).val().indexOf("MDL001")==-1 &&
                        $("#ajaxForm #wkbm-"+(i+1)).val().indexOf("MDX")==-1 &&
                        $("#ajaxForm #wkbm-"+(i+1)).val().indexOf("MDY")==-1 &&
                        $("#ajaxForm #wkbm-"+(i+1)).val()!='' && $("#ajaxForm #wkbm-"+(i+1)).val()!=null&&$("#ajaxForm #wkbm-"+(i+1)).val().indexOf("tNGS")!=-1){
                            $("#ajaxForm #wknd-"+(i+1)).val("-1");
                            $("#ajaxForm #dlnd-"+(i+1)).val("-1");
                        }
                        $("#ajaxForm #text-hbnd").text("合并浓度");
                        $("#ajaxForm #th-hbnd").attr("style","width:8%;");
                        $("#ajaxForm #hbnd").attr("style","width:8%;");
                        $("#ajaxForm #text-tj").text("体积");
                        $("#ajaxForm #th-tj").attr("style","width:8%;");
                        $("#ajaxForm #tj").attr("style","width:8%;");
                        $("#ajaxForm #text-nddw").text("浓度单位");
                        $("#ajaxForm #th-nddw").attr("style","width:8%;");
                        $("#ajaxForm #nddw").attr("style","width:8%;");
                        if($("#ajaxForm #wkbm-"+(i+1)).val()!='' && $("#ajaxForm #wkbm-"+(i+1)).val()!=null&&$("#ajaxForm #wkbm-"+(i+1)).val().indexOf("tNGS")!=-1){
                         $("#ajaxForm #nddw-"+(i+1)).val(data.nddw);
                        }
                    }
                    var wksjmxList=data.wksjmxList;
                    if(wksjmxList!=null&&wksjmxList.length>0){
                        for(var i=1;i<=wksjmxList.length;i++){
                            var wkbm=$("#wkbm-"+i).val()
                            if(wkbm!=""&&wkbm!=undefined&&wkbm!=null&&wkbm.indexOf("tNGS")!=-1){
                                var shadowRoot=document.querySelector("#tj-"+i).shadowRoot;
                                $("#tj-"+i).val(wksjmxList[i-1].tj)
                            }
                        }

                    }
                }else{
                    $("#ajaxForm #wknd input").val("");
                    $("#ajaxForm #dlnd input").val("");
                    $("#ajaxForm #hbnd input").val("");
                    $("#ajaxForm #tj input").val("");
                    $("#ajaxForm #text-tj").text("");
                    $("#ajaxForm #th-tj").attr("style","width:0%;");
                    $("#ajaxForm #tj").attr("style","width:0%;");
                    $("#ajaxForm #text-hbnd").text("");
                    $("#ajaxForm #th-hbnd").attr("style","width:0%;");
                    $("#ajaxForm #hbnd").attr("style","width:0%;");
                    $("#ajaxForm #nddw input").val("");
                    $("#ajaxForm #text-nddw").text("");
                    $("#ajaxForm #th-nddw").attr("style","width:0%;");
                    $("#ajaxForm #nddw").attr("style","width:0%;");

                }
            }
        }
    });
}

function xpxxEvent(){

    var xpid=$("#ajaxForm #xpxx").val();
    var xpmc=$("#ajaxForm #"+xpid).attr("xpmc");
    var sjsj=$("#ajaxForm #"+xpid).attr("sjsj");
    var xjsj=$("#ajaxForm #"+xpid).attr("xjsj");
    var cxymc=$("#ajaxForm #"+xpid).attr("cxymc");
    var fwq=$("#ajaxForm #"+xpid).attr("fwq");
    var sssys=$("#ajaxForm #"+xpid).attr("sssys");
    var yjxjsj=$("#ajaxForm #"+xpid).attr("yjxjsj");

    $("#ajaxForm #xpmc").text(xpmc);
    $("#ajaxForm #sjsj").text(sjsj);
    $("#ajaxForm #xjsj").text(xjsj);
    $("#ajaxForm #cxym").text(cxymc);
    $("#ajaxForm #fwq").text(fwq);
    $("#ajaxForm #sssys").text(sssys);
    $("#ajaxForm #yjxjsj").text(yjxjsj);
    $("#ajaxForm #t_xpmc").val(xpmc);
    $("#ajaxForm #t_sjsj").val(sjsj);
    $("#ajaxForm #t_xjsj").val(xjsj);
    $("#ajaxForm #t_cxy").val(cxymc);
    $("#ajaxForm #t_fwq").val(fwq);
    $("#ajaxForm #t_sssys").val(sssys);
    $("#ajaxForm #t_yjxjsj").val(yjxjsj);
    $("#ajaxForm #xpid").val(xpid);
     //根据测序仪名称获取index2标记
    $.ajax({
        type:'post',
        url:"/experiment/library/pagedataCxyxxByID",
        cache: false,
        data: {"mc":cxymc,"access_token":$("#ac_tk").val(),"wkid":$("#wkid").val()},
        dataType:'json',
        success:function(data){
            if(data.error){
                $.alert(data.error);
                return;
            }
            //返回值
            var cxyxxDto=data.cxyxxDto;

            if(cxyxxDto!=null){
                if(cxyxxDto.jt2bj=='1'){
                    for(var i=0;i<$("#ajaxForm .wksjmc").length;i++){
                        if($("#ajaxForm #wkbm-"+(i+1)).val()!=null && $("#ajaxForm #wkbm-"+(i+1)).val()!='' && $("#ajaxForm #jt2-"+(i+1)).val().substring($("#ajaxForm #jt2-"+(i+1)).val().length-1,$("#ajaxForm #jt2-"+(i+1)).val().length)!="R"){
                            $("#ajaxForm #jt2-"+(i+1)).attr("title",$("#ajaxForm #jt2-"+(i+1)).val()+"R");
                            $("#ajaxForm #jt2-"+(i+1)).val($("#ajaxForm #jt2-"+(i+1)).val()+"R");
                        }
                    }
                    //处理加R和去R单选样式
                    $("#ajaxForm #option-1").attr("checked",true);
                }else{
                    //处理加R和去R单选样式
                    $("#ajaxForm #option-2").attr("checked",true);
                }
            }

        }
    });
}

//加R单选框点击事情
$("#ajaxForm #option-1").click(function(){
    if($("#ajaxForm #xpxx").val()==null || $("#ajaxForm #xpxx").val()==''){
        $.confirm("请先选择芯片！");
        $("#ajaxForm #option-1").attr("checked",false);
    }else{
        for(var i=0;i<$("#ajaxForm .wksjmc").length;i++){
            var value=$("#ajaxForm #jt2-"+(i+1)).val();
            if(value[value.length-1]!="R"){//若接头最后一位不为R，则加R
                if($("#ajaxForm #wkbm-"+(i+1)).val()!=null && $("#ajaxForm #wkbm-"+(i+1)).val()!=''){
                    $("#ajaxForm #jt2-"+(i+1)).attr("title",$("#ajaxForm #jt2-"+(i+1)).val()+"R");
                    $("#ajaxForm #jt2-"+(i+1)).val($("#ajaxForm #jt2-"+(i+1)).val()+"R");
                }
            }
        }
    }
})

//去R单选框点击事件
$("#ajaxForm #option-2").click(function(){
    if($("#ajaxForm #xpxx").val()==null || $("#ajaxForm #xpxx").val()==''){
        $.confirm("请先选择芯片！");
        $("#ajaxForm #option-2").attr("checked",false);
    }else{
        for(var i=0;i<$("#ajaxForm .wksjmc").length;i++){
            var value=$("#ajaxForm #jt2-"+(i+1)).val();
            if(value[value.length-1]=="R"){//若接头最后一位为R，则去R
                if($("#ajaxForm #wkbm-"+(i+1)).val()!=null && $("#ajaxForm #wkbm-"+(i+1)).val()!=''){
                    var new_value=value.substring(0,value.length-1);
                    $("#ajaxForm #jt2-"+(i+1)).attr("title",new_value);
                    $("#ajaxForm #jt2-"+(i+1)).val(new_value);
                }
            }
        }
    }
})

$(function(){
    btnBind();
    jQuery('#ajaxForm .chosen-select').chosen({width: '100%'});

    /*控制滚轮上下滚动时候，表格标题浮动固定在顶部*/
    var width=$("#sjbbt tr").width()
    $('.sjbody').eq(0).parent().on('scroll', function () {
        if (($("#sjdivhidden").offset().top - $("#sjdiv").outerHeight()) < 0) {
            $("#sjbbt").css("position","fixed");
            $("#sjbbt").css("width",width-14);
            $("#sjbbt").css("top",$(".modal-header").eq(0).outerHeight());

        }else{
            $("#sjbbt").attr("style","");
        }
    })

    var poolingjson = $("#ajaxForm #poolingjson").val();
    if(poolingjson){
        var poolingSetting = JSON.parse(poolingjson);
        if(poolingSetting.xsbs){
            xsbs = poolingSetting.xsbs
        }
        if(poolingSetting.bp){
            bp = poolingSetting.bp
        }
        if(poolingSetting.xybp){
            xybp = poolingSetting.xybp
        }
        if(poolingSetting.qtbp){
            qtbp = poolingSetting.qtbp
        }
        if(poolingSetting.dly){
            dly = poolingSetting.dly
        }
    }
    //合并文库显示初始化
    if($("#ajaxForm #sfqyjyh").val()!='0'){
        $("#ajaxForm #th-hbnd").attr("style","width:8%;");
        $("#ajaxForm #hbnd").attr("style","width:8%;");
        $("#ajaxForm #th-nddw").attr("style","width:8%;");
        $("#ajaxForm #nddw").attr("style","width:8%;");
        $("#ajaxForm #th-tj").attr("style","width:8%;");
        $("#ajaxForm #tj").attr("style","width:8%;");
    }else{
        $("#ajaxForm #hbnd input").val("");
        $("#ajaxForm #text-hbnd").text("");
        $("#ajaxForm #th-hbnd").attr("style","width:0%;");
        $("#ajaxForm #hbnd").attr("style","width:0%;");
        $("#ajaxForm #nddw input").val("");
        $("#ajaxForm #text-nddw").text("");
        $("#ajaxForm #th-nddw").attr("style","width:0%;");
        $("#ajaxForm #nddw").attr("style","width:0%;");
        $("#ajaxForm #tj input").val("");
        $("#ajaxForm #text-tj").text("");
        $("#ajaxForm #th-tj").attr("style","width:0%;");
        $("#ajaxForm #tj").attr("style","width:0%;");
    }
})
//复制全部
$(".CopyEntire").click(function(e){
    var size = $("#sjbbt tr th").length;
    var context = '';
    for (var i = 1; i < 65; i++) {
        for (var j = 1; j < size; j++) {
            var idname = $("#sjbbt tr th div button")[j].id.split('-')[0]
            if(j == size-1){
                context = context+$("#"+idname+"-"+i)[0].defaultValue +"\r\n";
            }else {
                context = context+$("#"+idname+"-"+i)[0].defaultValue +"|";
            }
        }
    }
    var flag = copyText(context); //传递文本
    $.confirm(flag ? "复制全部列表成功！" : "复制全部列表失败！");
})
//复制整列

$(".CopyEntireColumn").click(function(e){
    var idname = e.currentTarget.id.split('-')[0];
    var context = '';
    for (var i = 1; i < 193; i++) {
        // if($("#"+idname+"-"+i)[0].defaultValue != null && $("#"+idname+"-"+i)[0].defaultValue != ''){
        context = context+$("#"+idname+"-"+i)[0].value +"\r\n";
    }
    var flag = copyText(context); //传递文本
    $.confirm(flag ? "复制本列成功！" : "复制本列失败！");
})
copyContentH5: function copyText(content) {
    var copyDom = document.createElement('div');
    copyDom.innerText=content;
    copyDom.style.position='absolute';
    copyDom.style.top='0px';
    copyDom.style.color='black';
    copyDom.style.right='-9999px';
    document.body.appendChild(copyDom);
    //创建选中范围
    var range = document.createRange();
    range.selectNode(copyDom);
    //移除剪切板中内容
    window.getSelection().removeAllRanges();
    //添加新的内容到剪切板
    window.getSelection().addRange(range);
    //复制
    var successful = document.execCommand('copy');
    copyDom.parentNode.removeChild(copyDom);
    try{
        var msg = successful ? "successful" : "failed";
        console.log('Copy command was : ' + msg);
    } catch(err){
        console.log('Oops , unable to copy!');
    }
    return successful;
}

function getDetectionData(nbbh,xh,wkbm) {
    $.ajax({
        type:'post',
        url:"/experiment/library/pagedataGetWkmx",
        cache: false,
        async: false,
        data: {"nbbh":nbbh,"wkid":$("#ajaxForm #wkid").val(),"wksxbm":wkbm,"cxyid":$("#ajaxForm #cxyid").val(),"access_token":$("#ac_tk").val()},
        dataType:'json',
        success:function(data){
            //返回值
            if(data.wkmxDto!=null){
                $("#ajaxForm #xmdm-"+xh).val(data.wkmxDto.kzcs7);
                $("#ajaxForm #syglid-"+xh).val(data.wkmxDto.syglid);
                $("#ajaxForm #project_type-"+xh).val(data.wkmxDto.project_type);
            }else{
                $("#ajaxForm #xmdm-"+xh).val("");
                $("#ajaxForm #syglid-"+xh).val("");
                $("#ajaxForm #project_type-"+xh).val("mNGS");
            }
            if(data.sfqyjyh!='0' && $("#ajaxForm #ywkbm-"+xh).val()!=wkbm){
                $("#ajaxForm #tj-"+xh).val(data.tj);
                $("#ajaxForm #nddw-"+xh).val(data.nddw);
                $("#ajaxForm #wknd-"+xh).val(data.wknd);
                $("#ajaxForm #dlnd-"+xh).val(data.dlnd);
            }
        }
    });
}

//
// function copyText(text) {
//     var textarea = document.createElement("textarea");//创建input对象
//     var currentFocus = document.activeElement;//当前获得焦点的元素
//     document.body.appendChild(textarea);//添加元素
//     textarea.value = text;
//     textarea.focus();
//     if (textarea.setSelectionRange)
//         textarea.setSelectionRange(0, textarea.value.length);//获取光标起始位置到结束位置
//     else
//         textarea.select();
//     try {
//         var flag = document.execCommand("copy");//执行复制
//     } catch (eo) {
//         var flag = false;
//     }
//     document.body.removeChild(textarea);//删除元素
//     currentFocus.focus();
//     return flag;
// }

function regenerationSjbxx(){
    $.confirm("确认要“删除并重新创建”吗?",function(result) {
        if (result){					//创建objectNode
            var cardiv =document.createElement("div");
            cardiv.id="cardiv";
            var s_str = '<div class="modal-backdrop fade in" style="display: block; z-index: 9999;"><div align="center" style="top:45%"><img src="/js/plugins/backdrop/images/ico-loading.gif"><p><span id="poolingMsg" style="color:red;font-weight:600;">处理中</span></p><p class="padding_t7"></p></div></div>';
            cardiv.innerHTML=s_str;
            //将上面创建的P元素加入到BODY的尾部
            document.body.appendChild(cardiv);
            let wkid = $("#ajaxForm #wkid").val()
            $.ajax({
                type:'post',
                url: '/experiment/library/pagedataRegenerationSjbxx',
                cache: false,
                data: {"wkid":wkid,"cxy":$("#ajaxForm #cxyid").val(),"access_token":$("#ac_tk").val()},
                dataType:'json',
                success: function (res) {
                    if(res.error){
                        $.alert(res.error);
                        return;
                    }
                    if (res != null && res.wksjmxList) {
                        $("#trmx").empty();
                        var html = '';
                        for (let i = 0; i < res.wksjmxList.length; i++) {
                            var wksjmx = res.wksjmxList[i]
                            html += '<tr class="wksjmc">';
                            html += '<td style="width:5%;">' + (i + 1) + '</td>';
                            html += '<td style="width:11%;" id="ybbh">			<input id="ybbh-' + (i + 1) + '" style="height:100%;border:0px;width:100%;" name="quantity" title="' + wksjmx.ybbh + '" value="' + (wksjmx.ybbh?wksjmx.ybbh:'') + '"></td>';
                            html += '<td style="width:25%;" id="wkbm">			<input id="wkbm-' + (i + 1) + '" style="height:100%;border:0px;width:100%;" name="quantity" title="' + wksjmx.wkbm + '" value="' + (wksjmx.wkbm?wksjmx.wkbm:'') + '" xh="' + (i + 1) + '"></td>';
                            html += '<td style="width:5%;" id="jt1">			<input id="jt1-' + (i + 1) + '" style="height:100%;border:0px;width:100%;" name="quantity" title="' + (wksjmx.jt1?wksjmx.jt1:'') + '" value="' + (wksjmx.jt1?wksjmx.jt1:'') + '"></td>';
                            html += '<td style="width:5%;" id="jt2">			<input id="jt2-' + (i + 1) + '" style="height:100%;border:0px;width:100%;" name="quantity" title="' + (wksjmx.jt2?wksjmx.jt2:'') + '" value="' + (wksjmx.jt2?wksjmx.jt2:'') + '"></td>';
                            html += '<td style="width:6%;" id="spike">			<input id="spike-' + (i + 1) + '" style="height:100%;border:0px;width:100%;" name="quantity" title="' + (wksjmx.spike?wksjmx.spike:'') + '" value="' + (wksjmx.spike?wksjmx.spike:'') + '"></td>';
                            html += '<td style="width:8%;" id="dnand">			<input id="dnand-' + (i + 1) + '" style="height:100%;border:0px;width:100%;" name="quantity" title="' + (wksjmx.dnand?wksjmx.dnand:'') + '" value="' + (wksjmx.dnand?wksjmx.dnand:'') + '"></td>';
                            html += '<td style="width:8%;" id="hsnd">			<input id="hsnd-' + (i + 1) + '" style="height:100%;border:0px;width:100%;" name="quantity" title="' + (wksjmx.hsnd?wksjmx.hsnd:'') + '" value="' + (wksjmx.hsnd?wksjmx.hsnd:'') + '"></td>';
                            html += '<td style="width:8%;" id="wknd">			<input id="wknd-' + (i + 1) + '" style="height:100%;border:0px;width:100%;" name="quantity" title="' + (wksjmx.wknd?wksjmx.wknd:'') + '" value="' + (wksjmx.wknd?wksjmx.wknd:'') + '"></td>';
                            html += '<td style="width:8%;" id="dlnd">			<input id="dlnd-' + (i + 1) + '" style="height:100%;border:0px;width:100%;" name="quantity" title="' + (wksjmx.dlnd?wksjmx.dlnd:'') + '" value="' + (wksjmx.dlnd?wksjmx.dlnd:'') + '"></td>';
                            html += '<td style="width:8%;" id="tj">			    <input id="tj-' + (i + 1) + '" style="height:100%;border:0px;width:100%;" name="quantity" title="' + (wksjmx.tj?wksjmx.tj:'') + '" value="' + (wksjmx.tj?wksjmx.tj:'') + '"></td>';
                            html += '<td style="width:8%;" id="hbnd">			<input id="hbnd-' + (i + 1) + '" style="height:100%;border:0px;width:100%;" name="quantity" title="' + (wksjmx.hbnd?wksjmx.hbnd:'') + '" value="' + (wksjmx.hbnd?wksjmx.hbnd:'') + '"></td>';
                            html += '<td style="width:8%;" id="nddw">			<input id="nddw-' + (i + 1) + '" style="height:100%;border:0px;width:100%;" name="quantity" title="' + (wksjmx.nddw?wksjmx.nddw:'') + '" value="' + (wksjmx.nddw?wksjmx.nddw:'') + '"></td>';
                            html += '<td style="width:8%;" id="yy">				<input id="yy-' + (i + 1) + '" style="height:100%;border:0px;width:100%;" name="quantity" title="' + (wksjmx.yy?wksjmx.yy:'') + '" value="' + (wksjmx.yy?wksjmx.yy:'') + '"></td>';
                            html += '<td style="width:9%;" id="yjxjsjl">		<input id="yjxjsjl-' + (i + 1) + '" style="height:100%;border:0px;width:100%;" name="quantity" title="' + (wksjmx.yjxjsjl?wksjmx.yjxjsjl:'') + '" value="' + (wksjmx.yjxjsjl?wksjmx.yjxjsjl:'') + '"></td>';
                            html += '<td style="width:9%;" id="xsbs">			<input id="xsbs-' + (i + 1) + '" style="height:100%;border:0px;width:100%;" name="quantity" title="' + (wksjmx.xsbs?wksjmx.xsbs:'') + '" value="' + (wksjmx.xsbs?wksjmx.xsbs:'') + '"></td>';
                            html += '<td style="width:6%;" id="project_type">	<input id="project_type-' + (i + 1) + '" style="height:100%;border:0px;width:100%;" name="quantity" title="' + (wksjmx.project_type?wksjmx.project_type:'') + '" value="' + (wksjmx.project_type?wksjmx.project_type:'') + '"></td>';
                            html += '<input type="hidden" id="xmdm-' + (i + 1) + '"  name="l_xmdm" value="' + (wksjmx.xmdm?wksjmx.xmdm:'') + '">';
                            html += '<input type="hidden" id="syglid-' + (i + 1) + '" name="l_xmdm" value="' + (wksjmx.syglid?wksjmx.syglid:'') + '">';
                            html += '<input type="hidden" id="nbbm-' + (i + 1) + '"  name="l_nbbm" value="' + (wksjmx.nbbm?wksjmx.nbbm:'') + '">';
                            html += '</tr>';
                        }
                        if (res.wksjmxList.length<=192){
                            for (let i = res.wksjmxList.length + 1; i <= 192; i++) {
                                html += '<tr class="wksjmc">';
                                html += '<td style="width:5%;">' + (i) + '</td>';
                                html += '<td style="width:11%;" id="ybbh">			<input id="ybbh-' + (i) + '" style="height:100%;border:0px;width:100%;" name="quantity"></td>';
                                html += '<td style="width:25%;" id="wkbm">			<input id="wkbm-' + (i) + '" style="height:100%;border:0px;width:100%;" name="quantity" xh="' + (i) + '"></td>';
                                html += '<td style="width:5%;" id="jt1">			<input id="jt1-' + (i) + '" style="height:100%;border:0px;width:100%;" name="quantity"></td>';
                                html += '<td style="width:5%;" id="jt2">			<input id="jt2-' + (i) + '" style="height:100%;border:0px;width:100%;" name="quantity"></td>';
                                html += '<td style="width:6%;" id="spike">			<input id="spike-' + (i) + '" style="height:100%;border:0px;width:100%;" name="quantity"></td>';
                                html += '<td style="width:8%;" id="dnand">			<input id="dnand-' + (i) + '" style="height:100%;border:0px;width:100%;" name="quantity"></td>';
                                html += '<td style="width:8%;" id="hsnd">			<input id="hsnd-' + (i) + '" style="height:100%;border:0px;width:100%;" name="quantity"></td>';
                                html += '<td style="width:8%;" id="wknd">			<input id="wknd-' + (i) + '" style="height:100%;border:0px;width:100%;" name="quantity"></td>';
                                html += '<td style="width:8%;" id="dlnd">			<input id="dlnd-' + (i) + '" style="height:100%;border:0px;width:100%;" name="quantity"></td>';
                                html += '<td style="width:8%;" id="tj">			    <input id="tj-' + (i) + '" style="height:100%;border:0px;width:100%;" name="quantity"></td>';
                                html += '<td style="width:8%;" id="hbnd">			<input id="hbnd-' + (i) + '" style="height:100%;border:0px;width:100%;" name="quantity"></td>';
                                html += '<td style="width:8%;" id="nddw">			<input id="nddw-' + (i) + '" style="height:100%;border:0px;width:100%;" name="quantity"></td>';
                                html += '<td style="width:8%;" id="yy">				<input id="yy-' + (i) + '" style="height:100%;border:0px;width:100%;" name="quantity"></td>';
                                html += '<td style="width:9%;" id="yjxjsjl">		<input id="yjxjsjl-' + (i) + '" style="height:100%;border:0px;width:100%;" name="quantity"></td>';
                                html += '<td style="width:9%;" id="xsbs">			<input id="xsbs-' + (i) + '" style="height:100%;border:0px;width:100%;" name="quantity"></td>';
                                html += '<td style="width:6%;" id="project_type">	<input id="project_type-' + (i) + '" style="height:100%;border:0px;width:100%;" name="quantity"></td>';
                                html += '<input type="hidden" id="xmdm-' + (i) + '" style="height:100%;border:0px;width:100%;" name="quantity">';
                                html += '<input type="hidden" id="syglid-' + (i) + '" style="height:100%;border:0px;width:100%;" name="quantity">';
                                html += '<input type="hidden" id="nbbm-' + (i) + '" style="height:100%;border:0px;width:100%;" name="quantity">';
                                html += '</tr>';
                            }
                        }
                        $("#trmx").html(html)
                        //先移除导出提示，然后请求后台
                        if($("#cardiv").length>0) $("#cardiv").remove();
                    }
                    cxyEvent()
                },
                error:function(res){
                    //先移除导出提示，然后请求后台
                    if($("#cardiv").length>0) $("#cardiv").remove();
                }
            });
        }
    });
}