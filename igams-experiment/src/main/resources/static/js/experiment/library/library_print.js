var beforePrint = function() {
    console.log('Functionality to run before printing.');
};

var afterPrint = function() {
    console.log('Functionality to run after printing');
};

if (window.matchMedia) {
    var mediaQueryList = window.matchMedia('print');
    mediaQueryList.addListener(function(mql) {
        if (mql.matches) {
            beforePrint();
        } else {
            afterPrint();
        }
    });
}

window.onbeforeprint = beforePrint;
window.onafterprint = afterPrint;

var flag = 0;


function view(wkid){
	var url= "/experiment/library/viewLibrary?wkid=" + wkid+"&flag=merge";
	$.showDialog(url,'文库信息',viewMergeLibraryConfig);
}

var viewMergeLibraryConfig = {
	width		: "1200px",
	offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
	buttons		: {
		cancel : {
			label : "关 闭",
			className : "btn-default"
		}
	}
};


$(function(){
	var wkid=$("#wkid").val();
	var requestUrl = window.location.href;
    var url = new URL(requestUrl);
    var searchParams = url.searchParams;
	$.ajax({
		type:'post',
		url:"/experiment/library/pagedataCkxx",
		cache: false,
		data: {"wkid":wkid,"access_token":searchParams.get("access_token")},
		dataType:'json',
		success:function(data){
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
					if(list[i].quantity!=null && list[i].quantity!=''){
						$("#liend"+lie+"-"+num).text(list[i].quantity);
						$("#liend"+lie+"-"+num).attr("title",list[i].quantity);
					}
				}else{
					lie=parseInt(list[i].xh/8)+1;
					num=list[i].xh%8;
					$("#lie"+lie+"-"+num).text(list[i].jtxx+"--"+list[i].nbbh);
					$("#lie"+lie+"-"+num).attr("title",list[i].jtxx+"--"+list[i].nbbh);
					if(list[i].quantity!=null && list[i].quantity!=''){
						$("#liend"+lie+"-"+num).text(list[i].quantity);
						$("#liend"+lie+"-"+num).attr("title",list[i].quantity);
					}
				}
			}
			window.print();
		}
	});
})
