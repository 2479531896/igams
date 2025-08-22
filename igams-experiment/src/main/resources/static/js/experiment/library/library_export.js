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
    var items = JSON.parse($("#libraryExportForm #wkmxDtos").val());
    for(var i=0;i<items.length;i++){
        $("#libraryExportForm #nbbh-"+(items[i].xh)).text(items[i].nbbh);
        $("#libraryExportForm #jt-"+(items[i].xh)).text(items[i].jtxx);
    }
    window.print();
});