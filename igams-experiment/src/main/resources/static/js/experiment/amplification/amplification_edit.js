var libPreInput;
var libSecondPreInput;
$("#editAmplificationForm .kzmxDiv input").on('keydown',function(e){
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
        // $(this).val($(this).val().toUpperCase());
        if($(this).val().length >0){
            var nextxh=parseInt(thisxh)+1;
            if(nextxh==193){
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
        // $(this).val($(this).val().toUpperCase());
        var nextxh=parseInt(thisxh)+1;
        if(nextxh==193){
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

$("#editAmplificationForm .kzmxDiv input").blur(function(){
    var thisxh=$(this).attr("xh");
    var str = $("input[xh='"+thisxh+"']").val();
    findInfo(thisxh,str);
});

function findInfo(thisxh,str){
    //如果输入框为空，清空对应的提取码以及id
    if(!str){
        $("#tqm-"+thisxh).val('');
        $("#id-"+thisxh).val('');
        return;
    }
    var re = new RegExp(",","g");
    var rq = new RegExp("，","g");
    var Newstr = str.replace(re, "");
    Newstr = Newstr.replace(rq,"");
    $("input[xh='"+thisxh+"']").val(Newstr);
    var thisnbbh=$("input[xh='"+thisxh+"']").val();
    let mrhz = $("#editAmplificationForm #mrhz").val();
    //将input框里的值按照空格分割
    var strings = thisnbbh.split(" ");
    //分割出来的数组长度大于1，则说明是扫码录入
    if ( strings.length > 1){
        //将数组第二位按照 / 分割从而获取 kzcs2 和 kzcs3
        var strs=strings[1].split("/");
        $.ajax({
            type:'post',
            url:"/inspection/inspection/pagedataGetSjsyInfo",
            cache: false,
            async: false,
            data: {"nbbm":thisnbbh,"kzcs2":strs[0],"kzcs3":strs[1],"jcdw":$("#editAmplificationForm #jcdw").val(),"access_token":$("#ac_tk").val(),"xmlimit":"F"},
            dataType:'json',
            success:function(data){
                //返回值
                if(data. list!=null&&data.list.length>0){
                    $("#tqm-"+thisxh).val('');
                    $("#id-"+thisxh).val('');
                    //当前页面已经存在的syglid
                    var checkStr="";
                    for(var i=1;i<97;i++){
                        var id = $("#id-"+i).val();
                        if(id){
                            checkStr=checkStr+','+id;
                        }
                    };

                    if(mrhz){//如果默认后缀不为空
                        var isFind=false;
                        for(var i=0;i<data.list.length;i++){
                            if(checkStr.indexOf(data.list[i].syglid)==-1){//判断当前页面是否已经存在这一条数据
                                var dataresult=false
                                $.ajax({
                                    type:'post',
                                    async: false,
                                    url:"/amplification/amplification/pagedataVerifySame",
                                    cache: false,
                                    data: {"syglid":data.list[i].syglid,"nbbh":thisnbbh,"access_token":$("#ac_tk").val(),"kzid":$("#editAmplificationForm #kzid").val()},
                                    dataType:'json',
                                    success:function(newData){
                                        if('true'==newData.status){
                                            dataresult=true;
                                        }
                                    }
                                });
                                if(dataresult){
                                    //判断是否匹配成功
                                    if(mrhz==data.list[i].kzcs1){
                                        //说明默认后缀匹配成功，直接拼上后缀以及对tqm以及id赋值
                                        if(data.list[i].kzcs1){
                                            $("input[xh='"+thisxh+"']").val(strings[0]+'-'+data.list[i].kzcs1);
                                        }else{
                                            $("input[xh='"+thisxh+"']").val(strings[0]);
                                        }

                                        $("#tqm-"+thisxh).val(data.list[i].nbzbm);
                                        $("#id-"+thisxh).val(data.list[i].syglid);
                                        isFind=true;
                                        break;
                                    }
                                }
                            }
                        }
                        //说明默认后缀匹配失败
                        if(!isFind){
                            for(var i=0;i<data.list.length;i++){
                                if(checkStr.indexOf(data.list[i].syglid)==-1){//判断当前页面是否已经存在这一条数据
                                    var dataresult=false
                                    $.ajax({
                                        type:'post',
                                        async: false,
                                        url:"/amplification/amplification/pagedataVerifySame",
                                        cache: false,
                                        data: {"syglid":data.list[i].syglid,"nbbh":thisnbbh,"access_token":$("#ac_tk").val(),"kzid":$("#editAmplificationForm #kzid").val()},
                                        dataType:'json',
                                        success:function(newData){
                                            if('true'==newData.status){
                                                dataresult=true;
                                            }
                                        }
                                    });
                                    if(dataresult){
                                        if(data.list[i].kzcs1){
                                            $("input[xh='"+thisxh+"']").val(strings[0]+'-'+data.list[i].kzcs1);
                                        }else{
                                            $("input[xh='"+thisxh+"']").val(strings[0]);
                                        }
                                        $("#tqm-"+thisxh).val(data.list[i].nbzbm);
                                        $("#id-"+thisxh).val(data.list[i].syglid);
                                        break;
                                    }
                                }
                            }
                        }
                    }else{//说明默认后缀为空
                        for(var i=0;i<data.list.length;i++){
                            if(checkStr.indexOf(data.list[i].syglid)==-1){//判断当前页面是否已经存在这一条数据
                                var dataresult=false
                                $.ajax({
                                    type:'post',
                                    async: false,
                                    url:"/amplification/amplification/pagedataVerifySame",
                                    cache: false,
                                    data: {"syglid":data.list[i].syglid,"nbbh":thisnbbh,"access_token":$("#ac_tk").val(),"kzid":$("#editAmplificationForm #kzid").val()},
                                    dataType:'json',
                                    success:function(newData){
                                        if('true'==newData.status){
                                            dataresult=true;
                                        }
                                    }
                                });
                                if(dataresult){
                                    if(data.list[i].kzcs1){
                                        $("input[xh='"+thisxh+"']").val(strings[0]+'-'+data.list[i].kzcs1);
                                    }else{
                                        $("input[xh='"+thisxh+"']").val(strings[0]);
                                    }
                                    $("#tqm-"+thisxh).val(data.list[i].nbzbm);
                                    $("#id-"+thisxh).val(data.list[i].syglid);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        });
    }else{
        var id= $("#id-"+thisxh).val();
        if(!id){
            var nbbh="";
            var split = thisnbbh.split("-");
            var suffix="";
            if(split.length>=2){
                if("NC"==split[0]||"PC"==split[0]){
                    nbbh=split[0]+"-"+split[1];
                    for(var i=2;i<split.length;i++){
                        suffix=suffix+'-'+split[i];
                    }
                }else{
                    nbbh=split[0];
                    for(var i=1;i<split.length;i++){
                        suffix=suffix+'-'+split[i];
                    }
                }
                if(suffix){
                    suffix=suffix.substring(1);
                }
            }else{
                nbbh=split[0];
            }
            getDetectionData(nbbh,mrhz,suffix,thisxh);
        }else{
            var nbbh="";
            var split = thisnbbh.split("-");
            var suffix="";
            if(split.length>=2){
                if("NC"==split[0]||"PC"==split[0]){
                    nbbh=split[0]+"-"+split[1];
                    for(var i=2;i<split.length;i++){
                        suffix=suffix+'-'+split[i];
                    }
                }else{
                    nbbh=split[0];
                    for(var i=1;i<split.length;i++){
                        suffix=suffix+'-'+split[i];
                    }
                }
                if(suffix){
                    suffix=suffix.substring(1);
                }
            }else{
                nbbh=split[0];
            }
            checkDetectionData(nbbh,id,suffix,thisxh);
        }
    }

    var id=$("#id-"+thisxh).val();
    if(!id){
        $("input[xh='"+thisxh+"']").val('');
        $.error("此内部编码已无信息可关联！");
    }
}

function getDetectionData(nbbh,mrhz,suffix,thisxh) {
    $.ajax({
        type:'post',
        url:"/inspection/inspection/pagedataGetSjsyInfo",
        cache: false,
        async: false,
        data: {"nbbm":nbbh,"jcdw":$("#editAmplificationForm #jcdw").val(),"access_token":$("#ac_tk").val(),"xmlimit":"F"},
        dataType:'json',
        success:function(data){
            //返回值
            if(data. list!=null&&data.list.length>0){
                var checkStr="";
                for(var i=1;i<97;i++){
                    var id = $("#id-"+i).val();
                    if(id){
                        checkStr=checkStr+','+id;
                    }
                };

                if(mrhz){//如果默认后缀不为空
                    var isFind=false;
                    for(var i=0;i<data.list.length;i++){
                        if(checkStr.indexOf(data.list[i].syglid)==-1){//判断当前页面是否已经存在这一条数据
                            var dataresult=false
                            $.ajax({
                                type:'post',
                                async: false,
                                url:"/amplification/amplification/pagedataVerifySame",
                                cache: false,
                                data: {"syglid":data.list[i].syglid,"nbbh":nbbh,"access_token":$("#ac_tk").val(),"kzid":$("#editAmplificationForm #kzid").val()},
                                dataType:'json',
                                success:function(newData){
                                    if('true'==newData.status){
                                        dataresult=true;
                                    }
                                }
                            });
                            if(dataresult){
                                //判断是否匹配成功
                                if(mrhz==data.list[i].kzcs1){
                                    //判断当前输入框内是否有后缀，如果有则匹配相同后缀赋值，如果没有则取当前页面没有的后缀赋值
                                    if(suffix){
                                        if(suffix==data.list[i].kzcs1){
                                            //说明默认后缀匹配成功，直接拼上后缀以及对tqm以及id赋值
                                            if(data.list[i].kzcs1){
                                                $("input[xh='"+thisxh+"']").val(nbbh+'-'+data.list[i].kzcs1);
                                            }else{
                                                $("input[xh='"+thisxh+"']").val(nbbh);
                                            }

                                            $("#tqm-"+thisxh).val(data.list[i].nbzbm);
                                            $("#id-"+thisxh).val(data.list[i].syglid);
                                        }
                                    }else{
                                        //说明默认后缀匹配成功，直接拼上后缀以及对tqm以及id赋值
                                        if(data.list[i].kzcs1){
                                            $("input[xh='"+thisxh+"']").val(nbbh+'-'+data.list[i].kzcs1);
                                        }else{
                                            $("input[xh='"+thisxh+"']").val(nbbh);
                                        }

                                        $("#tqm-"+thisxh).val(data.list[i].nbzbm);
                                        $("#id-"+thisxh).val(data.list[i].syglid);
                                    }
                                    isFind=true;
                                }
                            }
                        }
                    }
                    //说明默认后缀匹配失败
                    if(!isFind){
                        for(var i=0;i<data.list.length;i++){
                            if(checkStr.indexOf(data.list[i].syglid)==-1){//判断当前页面是否已经存在这一条数据
                                var dataresult=false
                                $.ajax({
                                    type:'post',
                                    async: false,
                                    url:"/amplification/amplification/pagedataVerifySame",
                                    cache: false,
                                    data: {"syglid":data.list[i].syglid,"nbbh":nbbh,"access_token":$("#ac_tk").val(),"kzid":$("#editAmplificationForm #kzid").val()},
                                    dataType:'json',
                                    success:function(newData){
                                        if('true'==newData.status){
                                            dataresult=true;
                                        }
                                    }
                                });
                                if(dataresult){
                                    //判断当前输入框内是否有后缀，如果有则匹配相同后缀赋值，如果没有则取当前页面没有的后缀赋值
                                    if(suffix){
                                        if(suffix==data.list[i].kzcs1){
                                            if(data.list[i].kzcs1){
                                                $("input[xh='"+thisxh+"']").val(nbbh+'-'+data.list[i].kzcs1);
                                            }else{
                                                $("input[xh='"+thisxh+"']").val(nbbh);
                                            }

                                            $("#tqm-"+thisxh).val(data.list[i].nbzbm);
                                            $("#id-"+thisxh).val(data.list[i].syglid);
                                        }
                                    }else{
                                        if(data.list[i].kzcs1){
                                            $("input[xh='"+thisxh+"']").val(nbbh+'-'+data.list[i].kzcs1);
                                        }else{
                                            $("input[xh='"+thisxh+"']").val(nbbh);
                                        }

                                        $("#tqm-"+thisxh).val(data.list[i].nbzbm);
                                        $("#id-"+thisxh).val(data.list[i].syglid);
                                    }
                                }
                            }
                        }
                    }
                }else{//说明默认后缀为空
                    for(var i=0;i<data.list.length;i++){
                        if(checkStr.indexOf(data.list[i].syglid)==-1){//判断当前页面是否已经存在这一条数据
                            var dataresult=false
                            $.ajax({
                                type:'post',
                                async: false,
                                url:"/amplification/amplification/pagedataVerifySame",
                                cache: false,
                                data: {"syglid":data.list[i].syglid,"nbbh":nbbh,"access_token":$("#ac_tk").val(),"kzid":$("#editAmplificationForm #kzid").val()},
                                dataType:'json',
                                success:function(newData){
                                    if('true'==newData.status){
                                        dataresult=true;
                                    }
                                }
                            });
                            if(dataresult){
                                //判断当前输入框内是否有后缀，如果有则匹配相同后缀赋值，如果没有则取当前页面没有的后缀赋值
                                if(suffix){
                                    if(suffix==data.list[i].kzcs1){
                                        if(data.list[i].kzcs1){
                                            $("input[xh='"+thisxh+"']").val(nbbh+'-'+data.list[i].kzcs1);
                                        }else{
                                            $("input[xh='"+thisxh+"']").val(nbbh);
                                        }

                                        $("#tqm-"+thisxh).val(data.list[i].nbzbm);
                                        $("#id-"+thisxh).val(data.list[i].syglid);
                                    }
                                }else{
                                    if(data.list[i].kzcs1){
                                        $("input[xh='"+thisxh+"']").val(nbbh+'-'+data.list[i].kzcs1);
                                    }else{
                                        $("input[xh='"+thisxh+"']").val(nbbh);
                                    }

                                    $("#tqm-"+thisxh).val(data.list[i].nbzbm);
                                    $("#id-"+thisxh).val(data.list[i].syglid);
                                }
                            }
                        }
                    }
                }
            }
        }
    });
}

function checkDetectionData(nbbh,id,suffix,thisxh){
    $.ajax({
        type:'post',
        url:"/inspection/inspection/pagedataGetSjsyInfo",
        cache: false,
        async: false,
        data: {"nbbm":nbbh,"jcdw":$("#editAmplificationForm #jcdw").val(),"access_token":$("#ac_tk").val(),"xmlimit":"F"},
        dataType:'json',
        success:function(data){
            //返回值
            var checkflag=false;
            if(data.list!=null&&data.list.length>0){
                for(var i=0;i<data.list.length;i++){
                    if(id==data.list[i].syglid){
                        if(suffix&&data.list[i].kzcs1){
                            if(suffix!=data.list[i].kzcs1){
                                checkflag=true;
                            }
                        }
                        break;
                    }
                }
            }
            if(checkflag){
                $("#id-"+thisxh).val('');
                $("#tqm-"+thisxh).val('');
                $.confirm("当前"+ nbbh + "检测类型不存在,上机时无法关联标本编号,需重新确认!",function() {
                    setTimeout(function(){
                        $("#nbbh-"+thisxh).focus();
                    },200);
                });
                return;
            }
        }
    });
}

//复制粘贴
document.querySelectorAll("input[id^='nbbh-']").forEach(function(item) {
	item.addEventListener('paste', function(event) {
	    var inputIdNum = parseInt(event.target.id.split("-")[1]);
	    var inputhang = inputIdNum % 8;
	    var inputlie = Math.floor(inputIdNum / 8);
		var clipText = event.clipboardData.getData('Text');
		if (clipText) {
			//复制内容为Excel中内容的粘贴操作
			if (clipText.indexOf("\n") != -1) {
				var strings = clipText.split("\n");
				var num = inputIdNum;
				var row = inputlie;
				for (var i = 0; i < strings.length; i++) {
					if (strings[i]) {
						num++;
						row++;
					    findInfo(i + 1, strings[i].split("\t")[0].replace("\r",""));
					}
				}
				copyText("");
			}

		}
	});
})

document.addEventListener('paste', async e => {
    //获取剪切板数据
    var clipText = e.clipboardData.getData('text/plain');
    var id=e.target.id;
    if(clipText){
        if(id=="nbbh-1"){
            var strings = clipText.split(";");
            for(var i=0;i<strings.length;i++){
                var str = strings[i].split(",");
                for(var j=0;j<str.length;j++){
                    $("#editAmplificationForm #nbbh-"+(i+1)).val(str[0]);
                    $("#editAmplificationForm #tqm-"+(i+1)).val(str[1]);
                    $("#editAmplificationForm #id-"+(i+1)).val(str[2]);
                }
            }
        }else{
            $("#editAmplificationForm #"+id).val(clipText);
        }
        copyText("");//清空剪切板
    }
})

copyContentH5: function copyText(content) {
    var copyDom = document.createElement('div');
    copyDom.innerText=content;
    copyDom.style.position='absolute';
    copyDom.style.top='0px';
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
}



$(function(){
    if($("#format").val()=="addSaveAmplification"){
        laydate.render({
            elem: '#editAmplificationForm #kzsj'
            ,theme: '#2381E9'
            ,type: 'datetime'
            ,format: 'yyyy-MM-dd HH:mm:ss'//保留时分秒
            ,value: new Date()
        });
    }else{
        laydate.render({
            elem: '#editAmplificationForm #kzsj'
            ,theme: '#2381E9'
            ,type: 'datetime'
            ,format: 'yyyy-MM-dd HH:mm:ss'//保留时分秒
        });
    }
    jQuery('#editAmplificationForm .chosen-select').chosen({width: '100%'});
    if($("#format").val()=="modSaveAmplification"){
        var kzid=$("#editAmplificationForm #kzid").val();
        $.ajax({
            type:'post',
            url:"/amplification/amplification/pagedataGetInfo",
            cache: false,
            data: {"kzid":kzid,"access_token":$("#ac_tk").val()},
            dataType:'json',
            success:function(data){
                //返回值
                var list=data;
                for(var i=0;i<list.length;i++){
                    $("#nbbh-"+list[i].xh).val(list[i].nbbh);
                    $("#tqm-"+list[i].xh).val(list[i].tqm);
                    $("#id-"+list[i].xh).val(list[i].syglid);
                }
            }
        });
    }
    //打开新增页面光标显示在第一个输入框
    setTimeout(function(){
        $("#nbbh-1").focus();
    },200);
});