/*
 * @discretion	: default messages for the jQuery Element Fixed plugin.
 * @author    	: wandalong 
 * @version		: v1.0.1
 * @email     	: hnxyhcwdl1003@163.com
 */
;(function($){
	
	/*
 		===data-api接口===：	
 	
 		data-placement		:	组件在容器中的位置：left,top,right,bottom
		data-width			:	组件宽度
		data-height			：	组件高度
		data-margin			：	组件浮动层距离容器边框距离；根据位置不同，分别表示左边距、右边距、上边距、下边距
		data-showPoint		：	组件显示位置值：横向时表示left，纵向时表示top
		data-z_index		:	组件显示层索引
		data-resizeDelay	:	尺寸变化回调函数执行间隔
		data-scrollDelay	:	容器滚动回调函数执行间隔
		data-animateDelay	:	组件显示隐藏特效执行过程时间
		data-position		:	组件位置属性：left,top,right,bottom,width,height 
	*/
	$.bootui = $.bootui || {};
	$.bootui.widget = $.bootui.widget || {};
	/*====================== Fixed CLASS DEFINITION ====================== */
	/*
	 * 构造器
	 */
	$.bootui.widget.Fixed = function(element,options){
		options.beforeRender.call(this,element);	//渲染前的函数回调
		//try {
			//实例化直接调用原型的initialize方法
			this.initialize.call(this, element, options);
		//} catch (e) {
			//options.errorRender.call(this,e);
		//}
		options.afterRender.call(this,element);	/*渲染后的函数回调*/
	};
	
	$.bootui.widget.Fixed.prototype = {
		constructor: $.bootui.widget.Fixed,
		/*初始化组件参数*/
		initialize : function(element, options) {
		
			var $this = this;
			this.$element  = $(element);
			this.options   = options;
			this.prefix	   = options.prefix;
			
			//判断滚动区域是否定义
		   	if ( options.container && $(options.container).size()==1) {
		   		
		   		var isWindow = $.isWindow(options.container);
		   		var placement 		= options.placement.toLowerCase()||"right";
		   		//给元素添加样式
		   		$(element).addClass("fixed-"+placement);
		   		
		   		//定位css
				var position = $.extend(true,{},{
					"position" 	: (isWindow ? "fixed" : "relative"),
					"z-index"	: parseInt(options.z_index || 1050 ) + 5
				});
				if($.trim(options.margin||"").length > 0){
					//css("right","10px !important")
					$(element).css("" + placement , parseInt($.trim(options.margin)) + "px");
		   		}
				
		   		//设置位置
				function setPosition(){
					// 新建一个deferred对象
					var dtd = $.Deferred(); 
					$(element).css({
						//先设置宽度，以便高度为auto时候好计算出真实高度
						"width"	: options.width||"40px",
						//先设置高度，以便宽度为auto时候好计算出真实宽度
						"height" : options.height||"40px"
					});
					//获得容器位置属性
					$this.containerPosi 	= $(options.container).getPosition();
					$this.containerRect 	= $(options.container).getBoundingRect();
					$this.elementPosi 		= $(element).getPosition();
					//判断位置
					switch (placement) {
						case "left":
						case "right":{
							//设置高度
							var height = options.height ? options.height : $this.containerPosi.height;
							$(element).css("height",options.position.height || height || $this.elementPosi.height);
							//自定义左右位置距离
							var _top =  parseInt(options.position.top||"-1"),_bottom = parseInt(options.position.bottom||"-1");
							var _top_last = 0;
							var _live = Math.abs($this.containerPosi.height - $this.elementPosi.height);
							if(Math.max(_top,_bottom) > 0){
								//居上
								if( _top >= _bottom){
									_top_last = Math.min(_live,_top);
								}else{
									//居下
									_top_last = Math.abs(_live) - _bottom;
								}
							}else{
								_top_last = Math.abs(_live)/2;
							}
							
							//扩展定位css
							$.extend(position,{
								"top"				: _top_last 
							});
						};break;
						case "top":
						case "bottom":{
							var left = $this.containerRect["padding-left"];
							var right = $this.containerRect["padding-right"];
							var width = 0;
							if(!options.width){
								if(isWindow){
									width = $(window).width();
								}else{
									width =  $this.containerPosi.width - left - right - $this.containerPosi.border;
								}
							}else{
								width = options.width;
							}
							//设置宽度
							$(element).css("width",options.position.width||width||$this.elementPosi.width);
							//自定义左右位置距离
							var _left =  parseInt(options.position.left||"-1"),_right = parseInt(options.position.right||"-1");
							var _left_last = 0;
							var _live = Math.abs($this.containerPosi.width - $this.elementPosi.width);
							if(Math.max(_left,_right) > 0){
								//居左
								if( _left >= _right){
									_left_last = Math.min(_live,_left);
								}else{
									//居右
									_left_last = Math.abs(_live) - _right;
								}
							}else{
								_left_last = Math.abs(_live)/2;
							}
							//扩展定位css
							$.extend(position,{
								"left"			: _left_last
							});
							
						};break;
					}  
					$(element).css(position||{});
					// 返回promise对象
					return dtd.promise();
				}
				
				//初始化位置
				setPosition();
				
				var marginTop 	=  $this.containerRect["margin-top"];
				var marginLeft 	=  $this.containerRect["margin-left"];
				
				var startX =  marginLeft + $(element).outerWidth(true) * 3/2 ;
				var startY =  marginTop + $(element).outerHeight() * 3/2 ;
				if(isWindow){
					startX	+=  Math.abs(Math.abs($this.elementPosi.left) -  Math.abs($this.containerPosi.left));
					startY	+=  Math.abs(Math.abs($this.elementPosi.top) -  Math.abs($this.containerPosi.top));
				}
				 
				//初始滚动量
				var scrollTop = $this.containerPosi.scrollTop;
				var scrollLeft = $this.containerPosi.scrollLeft;
				var isFirst = true;
				var animateDelay = options.animateDelay!=0?(options.animateDelay||200):0;
				//实时定位
				function fixed(){
					// 新建一个deferred对象
					var dtd = $.Deferred(); 
					//判断位置
					switch (placement) {
						case "left":
						case "right":{
							//container 不是在window对象，则需要动态改变位置
							if(!isWindow){
								$(element).css({
						    		"top"			:	(scrollTop - marginTop ) + "px"
								});
							}
							if(scrollTop >= options.showPoint){		
								if(isFirst){
									isFirst = false;
									$(element).width(0).show(0).animate({"width" : options.width||"40px"},animateDelay );
								}else{
									$(element).fadeIn(animateDelay);
								}
							}else{
								$(element).fadeOut(animateDelay);
							}
						};break;
						case "top":
						case "bottom":{
							//container 不是在window对象，则需要动态改变位置
							if(!isWindow){
								$(element).css({
						    		"left"			:	(scrollLeft - marginLeft ) + "px"
								});
							}
							if(scrollLeft >= options.showPoint){	
								if(isFirst){
									isFirst = false;
									$(element).height(0).show(0).animate({"height" : options.height||"40px"}, animateDelay );
								}else{
									$(element).fadeIn(animateDelay);
								}
							}else{
								$(element).fadeOut(animateDelay);
							}
						};break;
					}
					// 返回promise对象
					return dtd.promise();
				}
				
				if(scrollTop >= startY){
					fixed();
				}else if(scrollLeft >= startX){
					fixed();
				}
				//容器滚动事件
				var refixed = false;
				var scrollDelay = options.scrollDelay!=0? (options.scrollDelay||100):0;
				$this.scrollFunc = function(e){
					if(!refixed){
						$this.scrollEvent = e;
						window.setTimeout(function(){
							//实时获得滚动量
							scrollTop = $(this).scrollTop();
							scrollLeft = $(this).scrollLeft();
							refixed = true;
						}, Math.abs(scrollDelay - 10));
					}
				};
				//绑定scroll事件
				$(options.container).scroll($this.scrollFunc);
				$this.scrollTimer = window.setInterval(function(){
					if(refixed){
						$.when(fixed()).done(function(){ 
							refixed = false;
							//组件滚动回调
							(options.onScroll||$.noop).call(this,$this.scrollEvent);
						}).always(function(){
							
						});
					}
				}, scrollDelay);
				
				//尺寸变化事件
				var resized = false;
				var resizeDelay = options.resizeDelay!=0? (options.resizeDelay||200):0;
				$this.resizeFunc = function(e){
					if(!resized){
						window.setTimeout(function(){
							resized = true;
							$this.resizeEvent = e;
						},Math.abs(resizeDelay - 10) );
					}
				};
				//绑定resize事件
//				$(options.container).resize($this.resizeFunc);
				$this.resizeTimer = window.setInterval(function(){
					/*if(resized){
						//重置位置
						$.when(setPosition(),fixed()).done(function(){ 
							resized = false;
							//组件尺寸变化回调
							(options.onResize||$.noop).call(this,$this.resizeEvent);
						}).always(function(){
							
							
						});
					}*/
				}, resizeDelay);
		   	}else{
		   		throw new Error("Container is not a valid element!");
		   	}
		},
		destroy : function () {
			var container = this.options.container;
			var isWindow = $.isWindow(container);
			//卸载当前插件绑定的滚动事件
			$(container).unbind("scroll",this.scrollFunc);
			//清除滚动变化监听
			window.clearInterval(this.scrollTimer);
			//卸载当前插件绑定的resize事件
			$(container).unbind("resize",this.resizeFunc);
			//清除尺寸变化监听
			window.clearInterval(this.resizeTimer);
			//清除自身事件与绑定的data数据
			this.$element.off('.' + this.prefix).removeData(this.prefix+'.widget');
			//移除元素样式
			var placement 		=  this.options.placement.toLowerCase()||"right";
	   		$(this.$element).removeClass("fixed-"+placement);
		},
		setDefaults	: function(settings){
			$.extend($.fn.tooltips.defaults, settings );
		},
		getDefaults	: function(settings){
			 return $.fn.tooltips.defaults;
		}
	};
	
	/* Fixed PLUGIN DEFINITION  */
	
	/*
	 * jQuery原型上自定义的方法
	 */
	$.fn.fixed = function(option){
		//处理后的参数
		var args = $.grep( arguments || [], function(n,i){
			return i >= 1;
		});
		return this.each(function () {
			var $this = $(this);
			var data = $this.data('fixed.widget');
			if (!data && option == 'destroy') {return;}
			if (!data){
				var options = $.extend(true,{}, $.fn.fixed.defaults, $this.data(), ((typeof option == 'object' && option) ? option : {}));
				 $this.data('fixed.widget', (data = new $.bootui.widget.Fixed(this, options)));
			}
			if (typeof option == 'string'){
				//调用函数
				data[option].apply(data, [].concat(args || []) );
			}
		});
	};
	
	$.fn.fixed.defaults = {
		/*版本号*/
		version			: '1.0.0',
		/*组件命名前缀*/
		prefix			: "fixed",
		/*组件进行渲染前的回调函数：如重新加载远程数据并合并到本地数据*/
		beforeRender	: $.noop,
		/*组件渲染出错后的回调函数*/
		errorRender		: $.noop,
		/*组件渲染完成后的回调函数*/
		afterRender		: $.noop, 
		/*其他参数*/
		//在容器中的位置：left,top,right,bottom
		placement		: 'right',
		//容器
		container		: window,
		//宽度
		width			: "auto",
		//高度
		height			: "auto",
		//浮动层距离容器边框距离；根据位置不同，分别表示左边距、右边距、上边距、下边距
		margin			: "2",
		//显示位置值：横向时表示left，纵向时表示top
		showPoint		: 100,
		//层索引
		z_index			: "1058",
		//事件执行间隔
		resizeDelay		: 100,
		scrollDelay		: 200,
		animateDelay	: 200,
		//css
		position		: {},
		//组件滚动回调函数
		onScroll		: $.noop, 
		//组件尺寸变化回调函数
		onResize		: $.noop
	};

	$.fn.fixed.Constructor = $.bootui.widget.Fixed;

	/*============== Fixed DATA-API  ==============*/ 
	/*委托事件：实现自动绑定*/
	$(document).on('focus.data-api', '[data-bs-toggle*="fixed"]', function (event) {
		if(event.currentTarget = this){
			//弹出层
			if($(this).closest(".bootbox").size() > 0 ){
				var modalElement = $(this).closest(".bootbox");
				$(this).fixed({
		   			container		: modalElement,
		   			z_index			: modalElement.css("z-index")
				});
			}else{
				$(this).fixed({
					container : "#yhgnPage"
				});
			}
		}  
	});
	

}(jQuery));