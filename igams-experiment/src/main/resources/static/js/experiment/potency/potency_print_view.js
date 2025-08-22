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
function init(){
            console.log(112)
            var nbbh;
	    	var hsnd;//核酸浓度
	    	var xsnd;//稀释浓度
	    	var cdna;//CDNA
			var tqybm;//提取仪编码
			var tqykw;//提取仪孔位
	    	var jkyl;//建库用量
	    	var hcyyl;//缓冲液用量
			var spike;//spike
			// var length=data.length;
			var lastxh = data[data.length-1].xh
			if(lastxh%10!=0){
				lastxh=parseInt(lastxh/10);
				lastxh=lastxh+1;
			}else{
				lastxh=parseInt(lastxh/10);
			}
			for(var i=1;i<=lastxh;i++){
				$("#ajaxForm #tb_"+i).css("display","block");
			}

	    	for(var i=0;i<data.length;i++){
	    		nbbh=data[i].nbbh;
	    		if(nbbh==null){
	    			nbbh="";
	    		}
	    		hsnd=data[i].hsnd;
	    		if(hsnd==null){
	    			hsnd="";
	    		}
	    		cdna=data[i].cdna;
	    		if(cdna==null){
	    			cdna="";
	    		}
	    		xsnd=data[i].xsnd;
	    		if(xsnd==null){
	    			xsnd="";
	    		}
				tqybm=data[i].tqybm;
				if(tqybm==null){
					tqybm="";
				}
				tqykw=data[i].tqykw;
				if(tqykw==null){
					tqykw="";
				}
				spike=data[i].spike;
				if(spike==null){
					spike="";
				}
	    		$("#nbbh-"+data[i].xh).val(nbbh);
	    		$("#nbbh-"+data[i].xh).text(nbbh);
	    		$("#nbbh-"+data[i].xh).attr("title",nbbh);
	    		$("#hsnd-"+data[i].xh).val(hsnd);
	    		$("#hsnd-"+data[i].xh).text(hsnd);
	    		$("#hsnd-"+data[i].xh).attr("title",hsnd);
	    		$("#xsnd-"+data[i].xh).val(xsnd);
	    		$("#xsnd-"+data[i].xh).text(xsnd);
	    		$("#xsnd-"+data[i].xh).attr("title",xsnd);
	    		$("#cdna-"+data[i].xh).val(cdna);
	    		$("#cdna-"+data[i].xh).text(cdna);
	    		$("#cdna-"+data[i].xh).attr("title",cdna);
				$("#tqybm-"+data[i].xh).val(tqybm);
				$("#tqybm-"+data[i].xh).text(tqybm);
				$("#tqybm-"+data[i].xh).attr("title",tqybm);
				$("#tqykw-"+data[i].xh).val(tqykw);
				$("#tqykw-"+data[i].xh).text(tqykw);
				$("#spike-"+data[i].xh).val(spike);
				$("#spike-"+data[i].xh).text(spike);
				$("#tqykw-"+data[i].xh).attr("title",tqykw);
	    		$("#jkyl-"+data[i].xh).val(Math.round(data[i].jkyl));
	    		$("#jkyl-"+data[i].xh).text(Math.round(data[i].jkyl));
	    		$("#jkyl-"+data[i].xh).attr("title",Math.round(data[i].jkyl));
	    		$("#hcyyl-"+data[i].xh).val(Math.round(data[i].hcyyl));
	    		$("#hcyyl-"+data[i].xh).text(Math.round(data[i].hcyyl));
	    		$("#hcyyl-"+data[i].xh).attr("title",Math.round(data[i].hcyyl));
	    		if(xsnd!=hsnd){//若稀释浓度和核酸浓度不同改变该行颜色
	    			$("#t-"+data[i].xh).attr("style","background-color:#f0ad4e");
	    		}
	    	}
}
$(document).ready(function(){
    init();
	window.print();
});