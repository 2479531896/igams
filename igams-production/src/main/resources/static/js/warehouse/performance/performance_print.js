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

$(document).ready(function(){
	window.print();
});


window.onload = function() {
    gfjxmxJson = JSON.parse($("#gfjxmxJson").val());
    if(gfjxmxJson!=null && gfjxmxJson.length>0){
         for(var i=0;i<gfjxmxJson.length;i++){
            var bizmc = gfjxmxJson[i].bizmc;
            var newbizmc = bizmc.replaceAll("</br>","<br/>");
            var bizmcIn = document.getElementById(gfjxmxJson[i].jxmxid);
            bizmcIn.innerHTML = newbizmc;
         }
         var cell0 = document.getElementById('xm'+gfjxmxJson[0].jxmxid);
         cell0.rowSpan = 3;
         var cell3 = document.getElementById('xm'+gfjxmxJson[3].jxmxid);
         cell3.rowSpan = 1;
         var cell4 = document.getElementById('xm'+gfjxmxJson[4].jxmxid);
         cell4.rowSpan = 3;
         var cell5 = document.getElementById('xm'+gfjxmxJson[7].jxmxid);
         cell5.rowSpan = 2;
         var cell6 = document.getElementById('xm'+gfjxmxJson[9].jxmxid);
         cell6.rowSpan = 3;
         var tdElement = document.getElementById('xm'+gfjxmxJson[1].jxmxid);
         tdElement.parentNode.removeChild(tdElement);
         var tdElement1 = document.getElementById('xm'+gfjxmxJson[2].jxmxid);
         tdElement1.parentNode.removeChild(tdElement1);
         var tdElement2 = document.getElementById('xm'+gfjxmxJson[5].jxmxid);
         tdElement2.parentNode.removeChild(tdElement2);
         var tdElement3 = document.getElementById('xm'+gfjxmxJson[6].jxmxid);
         tdElement3.parentNode.removeChild(tdElement3);
         var tdElement4 = document.getElementById('xm'+gfjxmxJson[8].jxmxid);
         tdElement4.parentNode.removeChild(tdElement4);
         var tdElement5 = document.getElementById('xm'+gfjxmxJson[10].jxmxid);
         tdElement5.parentNode.removeChild(tdElement5);
         var tdElement6 = document.getElementById('xm'+gfjxmxJson[11].jxmxid);
         tdElement6.parentNode.removeChild(tdElement6);

    }
};
