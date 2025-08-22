var beforePrint = function() {
    var groupStr = $("#groupList").val();
    if(groupStr){
        var groupList = JSON.parse(groupStr);
        var trHtml1 = "";
        var trHtml2 = "";
        var trHtml3 = "";
        var trHtml4 = "";
        var trHtml5 = "";
        var trHtml6 = "";
        var trHtml7 = "";
        var trHtml8 = "";
        var trHtml9 = "";
        var trHtml10 = "";
        var trHtml11 = "";
        var trHtml12 = "";
        var trHtml13 = "";
        var trHtml14 = "";
        var trHtml15 = "";
        var trHtml16 = "";
        var trHtml17 = "";
        var trHtml18 = "";
        var trHtml19 = "";
        var trHtml20 = "";
        for (var i = 0; i < groupList.length; i++) {
            for (var j = 0; j < groupList[i].length; j++) {
                var s = j;
                if(1 == i ){
                    s = j+5;
                }
                var spanElement = document.getElementById('tqybm_'+s);
                if(spanElement) {
                    spanElement.textContent = groupList[i][j][0].tqybm;
                }
                for (var k = 0; k < groupList[i][j].length; k++) {
                    if(0 == i ){
                        var html = "";
                        var nbbh = groupList[i][j][k].nbbh;
                        if(typeof nbbh === 'undefined'){
                            html = html + '<td style="text-align:center;font-family:宋体;border: 1px solid #000;"><span style="height:18px">&nbsp;</span></td>';
                        }else{
                            html = html + '<td style="text-align:center;font-family:宋体;border: 1px solid #000;"><span style="height:18px">'+ nbbh +'</span></td>';
                        }
                        var tqykw = groupList[i][j][k].tqykw;
                        if(typeof tqykw === 'undefined'){
                            html = html + '<td style="text-align:center;font-family:宋体;border: 1px solid #000;"><span style="height:18px">&nbsp;</span></td>';
                        }else{
                            html = html + '<td style="text-align:center;font-family:宋体;border: 1px solid #000;"><span style="height:18px">'+ tqykw +'</span></td>';
                        }
                        var hsnd = groupList[i][j][k].hsnd;
                        if(typeof hsnd === 'undefined'){
                            html = html + '<td style="text-align:center;font-family:宋体;border: 1px solid #000;"><span style="height:18px">&nbsp;</span></td>';
                        }else{
                            html = html + '<td style="text-align:center;font-family:宋体;border: 1px solid #000;"><span style="height:18px">'+ hsnd +'</span></td>';
                        }
                        var xsbs = groupList[i][j][k].xsbs;
                        if(typeof xsbs === 'undefined'){
                            html = html + '<td style="text-align:center;font-family:宋体;border: 1px solid #000;"><span style="height:18px">&nbsp;</span></td>';
                        }else{
                            html = html + '<td style="text-align:center;font-family:宋体;border: 1px solid #000;"><span style="height:18px">'+ xsbs +'</span></td>';
                        }
                        if(k==0){
                        	trHtml1 = trHtml1 + html;
                        }
                        if(k==1){
                        	trHtml2 = trHtml2 + html;
                        }
                        if(k==2){
                        	trHtml3 = trHtml3 + html;
                        }
                        if(k==3){
                        	trHtml4 = trHtml4 + html;
                        }
                        if(k==4){
                        	trHtml5 = trHtml5 + html;
                        }
                        if(k==5){
                        	trHtml6 = trHtml6 + html;
                        }
                        if(k==6){
                        	trHtml7 = trHtml7 + html;
                        }
                        if(k==7){
                        	trHtml8 = trHtml8 + html;
                        }
                        if(k==8){
                        	trHtml9 = trHtml9 + html;
                        }
                        if(k==9){
                        	trHtml10 = trHtml10 + html;
                        }
                    }else{
                        var html = "";
                        var nbbh = groupList[i][j][k].nbbh;
                        if(typeof nbbh === 'undefined'){
                            html = html + '<td style="text-align:center;font-family:宋体;border: 1px solid #000;"><span style="height:18px">&nbsp;</span></td>';
                        }else{
                            html = html + '<td style="text-align:center;font-family:宋体;border: 1px solid #000;"><span style="height:18px">'+ nbbh +'</span></td>';
                        }
                        var tqykw = groupList[i][j][k].tqykw;
                        if(typeof tqykw === 'undefined'){
                            html = html + '<td style="text-align:center;font-family:宋体;border: 1px solid #000;"><span style="height:18px">&nbsp;</span></td>';
                        }else{
                            html = html + '<td style="text-align:center;font-family:宋体;border: 1px solid #000;"><span style="height:18px">'+ tqykw +'</span></td>';
                        }
                        var hsnd = groupList[i][j][k].hsnd;
                        if(typeof hsnd === 'undefined'){
                            html = html + '<td style="text-align:center;font-family:宋体;border: 1px solid #000;"><span style="height:18px">&nbsp;</span></td>';
                        }else{
                            html = html + '<td style="text-align:center;font-family:宋体;border: 1px solid #000;"><span style="height:18px">'+ hsnd +'</span></td>';
                        }
                        var xsbs = groupList[i][j][k].xsbs;
                        if(typeof xsbs === 'undefined'){
                            html = html + '<td style="text-align:center;font-family:宋体;border: 1px solid #000;"><span style="height:18px">&nbsp;</span></td>';
                        }else{
                            html = html + '<td style="text-align:center;font-family:宋体;border: 1px solid #000;"><span style="height:18px">'+ xsbs +'</span></td>';
                        }
                        if(k==0){
                            trHtml11 = trHtml11 + html;
                        }
                        if(k==1){
                            trHtml12 = trHtml12 + html;
                        }
                        if(k==2){
                            trHtml13 = trHtml13 + html;
                        }
                        if(k==3){
                            trHtml14 = trHtml14 + html;
                        }
                        if(k==4){
                            trHtml15 = trHtml15 + html;
                        }
                        if(k==5){
                        	trHtml16 = trHtml16 + html;
                        }
                        if(k==6){
                        	trHtml17 = trHtml17 + html;
                        }
                        if(k==7){
                        	trHtml18 = trHtml18 + html;
                        }
                        if(k==8){
                        	trHtml19 = trHtml19 + html;
                        }
                        if(k==9){
                        	trHtml20 = trHtml20 + html;
                        }
                    }

                }
            }
        }
        var tr1 = document.getElementById('tq_0');
        tr1.style.height = "21px";
        tr1.innerHTML = trHtml1;
        var tr2 = document.getElementById('tq_1');
        tr2.style.height = "21px";
        tr2.innerHTML = trHtml2;
        var tr3 = document.getElementById('tq_2');
        tr3.style.height = "21px"
        tr3.innerHTML = trHtml3;
        var tr4 = document.getElementById('tq_3');
        tr4.style.height = "21px"
        tr4.innerHTML = trHtml4;
        var tr5 = document.getElementById('tq_4');
        tr5.style.height = "21px"
        tr5.innerHTML = trHtml5;
        var tr6 = document.getElementById('tq_5');
        tr6.style.height = "21px"
        tr6.innerHTML = trHtml6;
        var tr7 = document.getElementById('tq_6');
        tr7.style.height = "21px"
        tr7.innerHTML = trHtml7;
        var tr8 = document.getElementById('tq_7');
        tr8.style.height = "21px"
        tr8.innerHTML = trHtml8;
        var tr9 = document.getElementById('tq_8');
        tr9.style.height = "21px"
        tr9.innerHTML = trHtml9;
        var tr10 = document.getElementById('tq_9');
        tr10.style.height = "21px"
        tr10.innerHTML = trHtml10;

        var tr11 = document.getElementById('tq_10');
        tr11.innerHTML = trHtml11;
        var tr12 = document.getElementById('tq_11');
        tr12.innerHTML = trHtml12;
        var tr13 = document.getElementById('tq_12');
        tr13.innerHTML = trHtml13;
        var tr14 = document.getElementById('tq_13');
        tr14.innerHTML = trHtml14;
        var tr15 = document.getElementById('tq_14');
        tr15.innerHTML = trHtml15;
        var tr16 = document.getElementById('tq_15');
        tr16.innerHTML = trHtml16;
        var tr17 = document.getElementById('tq_16');
        tr17.innerHTML = trHtml17;
        var tr18 = document.getElementById('tq_17');
        tr18.innerHTML = trHtml18;
        var tr19 = document.getElementById('tq_18');
        tr19.innerHTML = trHtml19;
        var tr20 = document.getElementById('tq_19');
        tr20.innerHTML = trHtml20;
    }


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