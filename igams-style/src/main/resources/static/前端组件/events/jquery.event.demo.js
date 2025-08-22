/*
 * 自定义事件脚本模板
 * Usage:
 * $(selector).bind("demo", fn);   // Bind the function fn to the demo event on each of the matched elements.
 * $(selector).demo(fn);           // Bind the function fn to the demo event on each of the matched elements.
 * $(selector).trigger("demo");    // Trigger the demo event on each of the matched elements.
 * $(selector).demo();             // Trigger the demo event on each of the matched elements.
 * $(selector).unbind("demo", fn); // Unbind the function fn from the demo event on each of the matched elements.
 * $(selector).unbind("demo");     // Unbind all demo events from each of the matched elements.
 * $(selector).on("demo", fn);
 * $(selector).on("demo", ".xxx", fn);
 * $(selector).off("demo", fn );
 * $(selector).off("demo", ".xxx", fn);
 */
(function ($, elements, EVENT_TYPE) {
	
    $.event.special[EVENT_TYPE] = {
 	    //noBubble —— 指定该事件类型不能进行冒泡。
 	    //deleType, bindType —— 指定该事件类型的低阶事件类型，deleType 表示委托， bindType 表示非委托。
        //若指定该方法，其在绑定事件处理程序(addEventListener)前执行，如果返回 false， 才绑定事件处理程序；
        setup: function () {
            var elem = this;
            $.data(elem, 'key', elem.value);
        },
        //handle(event) —— 特殊的事件处理程序，若指定该方法，会替代其原来的事件处理程序 handleObj.handler 而被调用。
        //preDispatch(elem, event) —— 若指定该方法，在执行 $.event.dispatch 核心逻辑前调用该方法，如果返回 false，跳出 $.event.dispatch 方法。
    	//postDispatch(elem, event) —— 若指定该方法，在执行 $.event.dispatch 核心逻辑后调用该函数，进行额外调度。
        //trigger() —— 若指定该方法，在执行 $.event.trigger 核心逻辑前调用该方法，如果返回 false，跳出 $.event.trigger 函数；
        //若指定该方法，其在移除事件处理程序(removeEventListener)前执行，如果返回 false，才移除事件处理程序；
        teardown: function () {
            var elem = this;
            if (xx) return false;
        }
    };
	
	$.fn[EVENT_TYPE] = function (fn) {
		return fn ? this.bind(EVENT_TYPE, fn) : this.trigger(EVENT_TYPE);
	};

})(jQuery, [], 'demo');