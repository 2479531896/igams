$("#ajaxForm input").blur(function(){
	var thisxh=$(this).attr("xh");
	var str = $("input[xh='"+thisxh+"']").val();
	re = new RegExp(",","g"); 
	rq = new RegExp("，","g");
	var Newstr = str.replace(re, "");
	Newstr = Newstr.replace(rq,"");
	$("input[xh='"+thisxh+"']").val(Newstr);
})

$(function(){
	var wkid=$("#ajaxForm #wkid").val();
	$.ajax({ 
	    type:'post',  
	    url:"/experiment/library/pagedataCkxx",
	    cache: false,
	    data: {"wkid":wkid,"access_token":$("#ac_tk").val()},  
	    dataType:'json', 
	    success:function(data){
	    	//返回值
	    	//返回值
	    	var list=data;
	    	var lie;
	    	var num;
	    	for(var i=0;i<list.length;i++){
	    		if(list[i].xh%8==0){
	    			lie=parseInt(list[i].xh/8);
	    			num=8;
	    			$("#lie"+lie+"-"+num).text(list[i].jtxx+"--"+list[i].nbbh);
	    			$("#lie"+lie+"-"+num).attr("title",list[i].jtxx+"--"+list[i].nbbh);
	    			$("#quantity"+lie+"-"+num).val(list[i].quantity);
	    			$("#quantity"+lie+"-"+num).attr("title",list[i].quantity);
	    		}else{
	    			lie=parseInt(list[i].xh/8)+1;
	    			num=list[i].xh%8;
	    			$("#lie"+lie+"-"+num).text(list[i].jtxx+"--"+list[i].nbbh);
	    			$("#lie"+lie+"-"+num).attr("title",list[i].jtxx+"--"+list[i].nbbh);
	    			$("#quantity"+lie+"-"+num).val(list[i].quantity);
	    			$("#quantity"+lie+"-"+num).attr("title",list[i].quantity);
	    		}
	    	}
	    }
	});
})

/**
 * 监听用户粘贴事件(新增物料)
 * @param e
 * @returns
 */
var addpaste = $("#ajaxForm input").bind("paste",function(e){
	e.preventDefault();
	var quantitys=e.originalEvent.clipboardData.getData("text");
	var arr_quantity=quantitys.replaceAll(" ","").split(/[(\r\n)\r\n]+/);
	var arr = quantitys.split(/[(\r\n)\r\n]+/)
		.filter(function(item) {
			return (item !== "")
		}).map(function(item) {
			return item. split("\t");
		});
	var y = arr.length;
	var x = 0;
	for (var j = 0; j < y; j++) {
		if(arr[j].length>=x){
			x = arr[j].length
		}
	}
	var arrlist=[];
	for (var i = 0; i < x; i++) {
		for (var j = 0; j < y; j++) {
			if(arr[j][i]!=null && arr[j][i]!='' && arr[j][i]!=' '){
				arrlist.push(arr[j][i])
			}
		}
	}
	var index=0;
	//12行
	for (var b = 1; b < 25; b++) {
		//8列
		for (var a = 1; a < 9; a++) {
			if ($("#ajaxForm #lie"+b+"-"+a).text()!=null && $("#ajaxForm #lie"+b+"-"+a).text()!=''){
				$("#ajaxForm #quantity"+b+"-"+a).val(arrlist[index]);
				index++;
			}
		}
	}

})
