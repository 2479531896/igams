/*
 * Require.js中使用jQuery UI组件
 * Require.js中使用jQuery UI组件，只要改造一下jQuery Widget Factory 代码就可以了，
 * 并且感觉jQuery UI的依赖关系加载就可以了
 * @param widgetFactory
 */
(function (widgetFactory) {

    if (typeof define === "function" && define.amd) {
        // AMD模式
        define("jquery.ui.widget", ["jquery"], function () {

            widgetFactory(window.jQuery);

        });
    } else {
        // 全局模式
        widgetFactory(window.jQuery);
    }
}(function ($, undefined) {

    // jQuery Widget Factory 代码
	
	

}));