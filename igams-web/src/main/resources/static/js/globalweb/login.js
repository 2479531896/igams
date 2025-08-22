$(document).ajaxStart(function () {
    //this 里面有很多信息 比如url 什么的 所有的请求都会出发它 d
    //do somesing
	//var $loading = $('<div id="loading">Loading....</div>').insertBefore("#dictionary");
    $(document).ajaxStart(function(){
        //$loading.show();
    }).ajaxStop(function(){
        //$loading.hide();
    });
})
