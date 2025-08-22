/*
 * @discretion	: default messages for the jQuery fixedtop plugin.
 * @author    	: wandalong 
 * @version		: v1.0.1
 * @email     	: hnxyhcwdl1003@163.com
 */
;(function($){
	
	/*
 		===data-api接口===：	
 	
		data-width			:	组件宽度
		data-z_index		:	组件显示层索引
		data-resizeDelay	:	尺寸变化回调函数执行间隔
		data-scrollDelay	:	容器滚动回调函数执行间隔
		data-animateDelay	:	组件显示隐藏特效执行过程时间
		...
		
	*/
	$.bootui = $.bootui || {};
	$.bootui.widget = $.bootui.widget || {};
	
	/*====================== FIXEDTOP CLASS DEFINITION ====================== */
	/*
	 * 构造器
	 */
	$.bootui.widget.FixedTop = function(element,options){
		options.beforeRender.call(this,element);	//渲染前的函数回调
		try {
			//实例化直接调用原型的initialize方法
			this.initialize.call(this, element, options);
		} catch (e) {
			options.errorRender.call(this,e);
		}
		options.afterRender.call(this,element);	/*渲染后的函数回调*/
	};
	
	$.bootui.widget.FixedTop.prototype = {
		constructor: $.bootui.widget.FixedTop,
		/*初始化组件参数*/
		initialize : function(element, options) {
			
			if(options.container && $(options.container).size() == 0){
				throw new Error("Container is not a valid element!");
			}
			
			var $this = this;
			$this.$element  = $(element);
			$this.options   = options;
		
			function getUID(prefix) {
				do {
				  	prefix += ~~(Math.random() * 1000000);
			  	}while (document.getElementById(prefix));
			  	return prefix;
			};
			var fixedtop_uid	 = getUID(options.prefix);
			$this.fixedtop_uid = fixedtop_uid;
			$this.prefix	   = options.prefix;
			
			//判断当前元素是否在bootbox弹窗中从而确定容器
			var bootbox = $(element).closest("div.bootbox");
			//判定容器
			$this.container =  options.container ? $(options.container) : ($(bootbox).size() > 0 ? $(bootbox).find("div.modal-body") : window); 
			var eContainer = $(bootbox).size() > 0 ? $(bootbox).find("div.modal-body") : document.body; 
			//层索引
			var z_index   = $(bootbox).size() > 0 ? ( parseInt(bootbox.css("z-index")||"1050") + 90) : 1010; 
			//判断参数
			var isWindow = $.isWindow($this.container);
			var isBody 	= $($this.container)[0].tagName?$($this.container)[0].tagName.toLowerCase() == 'body':false;
			//定位css
			var position = $.extend(true,{},{
				"position" 	: (isWindow ? "fixed" : "relative"),
				"z-index"	: parseInt(options.z_index || z_index ),
				"padding"	: (isWindow||isBody) ? "0px" : " 0px 8px"
			});
			var fixed_container = $(options.template);
			//设置ID;根据测试，this.id的速度比$(this).attr('id')快了20多倍。
			fixed_container[0].id = fixedtop_uid;
			 
			//设置位置
			function setPosition(){
				// 新建一个deferred对象
				var dtd = $.Deferred(); 
				//获得位置，尺寸信息
				$this.containerPosi 	= $($this.container).getPosition();
				$this.containerRect 	= $($this.container).getBoundingRect();
				$this.elementPosi 		= $(element).getPosition();
			 	var left = $this.containerRect["padding-left"];
				var right = $this.containerRect["padding-right"];
				//设置样式
				$(element).css($.extend(true,position,{
					"width"			:	isWindow ? $(window).width() : ($this.containerPosi.width - left - right - $this.containerPosi.border)
				}));
				// 返回promise对象
				return dtd.promise();
			}
			
			//初始化位置
			setPosition();
			
			var marginTop 	=  $this.containerRect["margin-top"];
			var startY =  marginTop + $(element).outerHeight() * 3/2 ;
			if(isWindow){
				startY	+=  Math.abs(Math.abs($this.elementPosi.top) -  Math.abs($this.containerPosi.top));
			}
			
			//初始滚动量
			var scrollTop = $this.containerPosi.scrollTop;
			var animateDelay = options.animateDelay!=0?(options.animateDelay||200):0;
			
			//实时顶部定位
			function fixedTop(){
				// 新建一个deferred对象
				var dtd = $.Deferred(); 
				//container 不是在window对象，则需要动态改变位置
				if(!isWindow){
					$(element).css({
			    		"top"			:	(scrollTop - marginTop ) + "px"
					});
				}
				if(scrollTop >= startY){
					//元素添加外层包装
					var fixedTarget = $("#"+fixed_uid);
					if(fixedTarget.size() == 0){
						$(element).wrap(fixed_container);
					}	
					//包装对象淡入
					$(fixed_container).fadeIn(animateDelay);
				}else{
					//包装对象淡出
					$(fixed_container).fadeOut(animateDelay);
					//元素去除外层包装
					$(element).unwrap();
				}
				// 返回promise对象
				return dtd.promise();
			}
			
			if(scrollTop >= startY){
				fixedTop();
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
			$($this.container).scroll($this.scrollFunc);
			$this.scrollTimer = window.setInterval(function(){
				if(refixed){
					$.when(fixedTop()).done(function(){ 
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
			$($this.container).resize($this.resizeFunc);
			$this.resizeTimer = window.setInterval(function(){
				if(resized){
					//重置位置
					$.when(setPosition(),fixedTop()).done(function(){ 
						resized = false;
						//组件尺寸变化回调
						(options.onResize||$.noop).call(this,$this.resizeEvent);
					}).always(function(){
						
					});
				}
			}, resizeDelay);
		},
		destroy : function () {
			var isWindow = $.isWindow(this.container);
			//卸载当前插件绑定的滚动事件
			$(this.container).unbind("scroll",this.scrollFunc);
			//清除滚动变化监听
			window.clearInterval(this.scrollTimer);
			//卸载当前插件绑定的resize事件
			$(this.container).unbind("resize",this.resizeFunc);
			//清除尺寸变化监听
			window.clearInterval(this.resizeTimer);
			//清除自身事件与绑定的data数据
			this.$element.off('.' + this.prefix).removeData('bootui.fixedtop');
			//根据测试，document.getElementById("foo")要比$("#foo")快10多倍。
			$(document.getElementById(this.fixed_uid)).remove();
		},
		setDefaults	: function(settings){
			$.extend($.fn.tooltips.defaults, settings );
		},
		getDefaults	: function(settings){
			 return $.fn.tooltips.defaults;
		}
	};
	
	/* FIXEDTOP PLUGIN DEFINITION  */
	$.fn.fixedtop = function(option){
		//处理后的参数
		var args = $.grep( arguments || [], function(n,i){
			return i >= 1;
		});
		return this.each(function () {
			var $this = $(this);
			var data = $.data($this,'bootui.fixedtop');
			if (!data && option == 'destroy') {return;}
			if (!data){
				var options = $.extend({}, $.fn.fixed.defaults, $this.data(),((typeof option == 'object' && option) ? option : {}));
				 $this.data('bootui.fixedtop', (data = new $.bootui.widget.FixedTop(this, options)));
			}
			if (typeof option == 'string'){
				//调用函数
				data[option].apply(data, [].concat(args || []) );
			}
		});
	};
	
	$.fn.fixedtop.defaults = {
		/*版本号*/
		version	: '1.0.0',
		prefix	: "fixedtop",
		/*组件进行渲染前的回调函数：如重新加载远程数据并合并到本地数据*/
		beforeRender: $.noop,
		/*组件渲染出错后的回调函数*/
		errorRender: $.noop,
		/*组件渲染完成后的回调函数*/
		afterRender: $.noop, 
		/*其他参数*/
		container		: null,
		width			: '500px',
		z_index			: null,
		//事件执行间隔
		resizeDelay		: 100,
		scrollDelay		: 200,
		animateDelay	: 200,
		//组件滚动回调函数
		onScroll		: $.noop, 
		//组件尺寸变化回调函数
		onResize		: $.noop,
		//模板
		template 		: "<div class='container sl_all_bg fixed-box'></div>"
	};

	$.fn.fixedtop.Constructor = $.bootui.widget.FixedTop;
		

	/*============== FixedTop DATA-API  ==============*/
	/*委托验证事件：实现自动绑定*/
	$(document).on('fixedtop.data-api', '[data-bs-toggle*="fixedtop"]', function (event) {
		if(event.currentTarget = this){
			//弹出层
			if($(this).closest(".bootbox").size() > 0 ){
				var modalElement = $(this).closest(".bootbox");
				$(this).fixed({
		   			scrollElement	: modalElement,
		   			container		: modalElement,
		   			width			: modalElement.find(".modal-dialog").width(),
		   			z_index			: modalElement.css("z-index") || 1058
				});
			}else{
				$(this).fixedtop({
					container : "#yhgnPage"
				});
			}
		}  
	});
	

}(jQuery));