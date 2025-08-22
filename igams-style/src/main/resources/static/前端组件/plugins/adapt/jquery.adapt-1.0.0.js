/*
 * @discretion: jQuery Adapt plugin base on bootstrap and jQuery. adapt [a·dapt ||
 *              ə'dæpt] v. 使适应, 使适合; 改建, 改造; 改编, 改写; 适应
 * @author : hnxyhcwdl1003@163.com
 * @example : 1.引用jquery的库文件js/jquery.js 2.引用样式文件css/uibootui.adapt-1.0.0.css
 *          3.引用效果的具体js代码文件uibootui.adapt-1.0.0.js 4.<script
 *          language="javascript" type="text/javascript"> jQuery(function($) {
 * 
 * $("#scrollDiv").adapt({ afterRender : function(){
 * //这个方法是初始化后的回调函数，在需要做一些事情的时候重写即可
 *  } });
 * 
 * }); </script>
 */
;
(function($) {


	$.bootui = $.bootui || {};
	$.bootui.widget = $.bootui.widget || {};

	$.bootui.widget.Adapt = function(element, options) {
		options.beforeRender.call(this, element); // 渲染前的函数回调
		try {
			// 实例化直接调用原型的initialize方法
			this.initialize.call(this, element, options);
		} catch (e) {
			options.errorRender.call(this, e);
		}
		options.afterRender.call(this, element); /* 渲染后的函数回调 */
	};
	$.bootui.widget.Adapt.prototype = {
		constructor : $.bootui.widget.Adapt,
		/* 初始化组件参数 */
		initialize : function(element, options) {
			//循环按钮
			var buttons = options.buttons||[];
			if(buttons && buttons.length > 0 ){
				var heigth = $(element).innerHeight();
				var html = new Array();
				html.push('<div class="adapt-body">');
					html.push('<div class="btn-group ' +(options.group_class||"")+ '">');
					$.each(options.buttons||[],function(i,btn_opt){
						html.push('<button class="btn btn-default btn-sm btn-design '+  (btn_opt.btn_class||" width-100 ")+ ((heigth <= 35)?" btn-design-sm" : "") +' " type="button">' + (btn_opt.btn_text||"按钮"+i) + ' </button>');
					});
					html.push('</div>');
				html.push('</div>');

				$(element).after(html.join(""));
				$(element).after('<div class="adapt-backdrop"></div>');

				var adapt_body = $($(element).nextAll(".adapt-body")[0]);
				var adapt_backdrop = $($(element).nextAll(".adapt-backdrop")[0]);

				var padding_left = parseInt($(element).css("padding-left") || "0");
				var padding_right = parseInt($(element).css("padding-right") || "0");
				var width = $(element).outerWidth(true) - padding_left - padding_right;
				
				var z_index = 850;
				// 根据文本框调整相应提示信息的css
				var bootbox = $(element).closest(".bootbox");
				var top,left;
				// 兼容bootbox弹窗
				if (bootbox.size() > 0) {
					z_index = $(element).closest(".bootbox").css("z-index");
					top = $(element).position().top;
					left = $(element).position().left;
				}else{
					top = $(element).offset().top;
					left = $(element).offset().left;
				}
				
				$(adapt_backdrop).css( {
					"width" 		: width,
					"height" 		: heigth,
					"position" 		: "absolute",
					"top" 			: top,
					"left" 			: left + padding_left,
					"z-index" 		: z_index + 100
				}).hide();

				$(adapt_body).css( {
					"width" 		: width,
					"height" 		: heigth,
					"position" 		: "absolute",
					"top" 			: top,
					"left" 			: left + padding_left,
					"z-index" 		: z_index + 110
				}).hide();
				
				
				$.each(options.buttons||[],function(i,btn_opt){
					var button = $(adapt_body).find(".btn:eq("+i+")");
					
					/*按钮单击事件*/
					button.click(btn_opt.onBtnClick||$.noop)
					/*按钮双击事件*/
					.dblclick(btn_opt.onBtnDbClick||$.noop)
					/*去除点击时的虚线*/
					.each(function(){
						 this.onmousedown = function() {
						    this.blur(); // most browsers
						    this.hideFocus = true; // ie
						    this.style.outline = 'none'; // mozilla
						  }
						 this.onmouseout = this.onmouseup = function() {
						    this.blur(); // most browsers
						    this.hideFocus = false; // ie
						    this.style.outline = null; // mozilla
						 }
					});
					
					if (heigth > 35) {
						if (heigth > 50) {
							button.css({
								"margin-top"	: Math.round((heigth  - button.innerHeight())/2) - 20
							});
						}else{
							button.addClass("small");
						}
					}  
				});
				
				var adapts = $(".adapt-body,.adapt-backdrop");
				$(element).unbind().mouseenter(function(event) {
					$(adapts).hide();
					$.each( [ adapt_backdrop, adapt_body ], function() {
						$(this).show();
					});
					event.stopPropagation();
				});
				$(adapt_body).mouseleave(function(event) {
					$.each( [ adapt_backdrop, adapt_body ], function() {
						$(this).hide();
						$(adapts).hide();
					});
					event.stopPropagation();
				});
			}
			/* 添加需要暴露给开发者的函数 */
			$.extend(true, this, {
				destroy : function() {
					$(element).off('.adapt').removeData('bootui.adapt');
				}
			});
		},
		setDefaults : function(settings) {
			$.extend($.fn.adapt.defaults, settings);
		},
		getDefaults : function() {
			return $.fn.adapt.defaults;
		}
	};

	/* TREEVIEW PLUGIN DEFINITION */
	$.fn.adapt = function(option) {
		// 处理后的参数
		var args = $.grep(arguments || [], function(n, i) {
			return i >= 1;
		});
		return this.each(function() {
			var $this = $(this), data = $this.data("bootui.adapt");
			if (!data && option == 'destroy') {
				return;
			}
			if (!data) {
				var options = $.extend(true, {}, $.fn.adapt.defaults, $this .data(), ((typeof option == 'object' && option) ? option : {}));
				$this.data('bootui.adapt', (data = new $.bootui.widget.Adapt( this, options)));
			}
			if (typeof option == 'string') {
				// 调用函数
				data[option].apply(data, [].concat(args || []));
			}
		});
	};

	$.fn.adapt.defaults = {
		/* 版本号 */
		version : '1.0.0',
		/* 组件进行渲染前的回调函数：如重新加载远程数据并合并到本地数据 */
		beforeRender : $.noop,
		/* 组件渲染出错后的回调函数 */
		errorRender : $.noop,
		/* 组件渲染完成后的回调函数 */
		afterRender : $.noop,
		/*按钮*/
		buttons:[{
			/*按钮文本内容*/
			btn_text 	: "编辑",
			/*按钮单击事件*/
			onBtnClick	: $.noop,
			/*按钮双击事件*/
			onBtnDbClick: $.noop,
			/*按钮额外class*/
			btn_class	: "width-100"
		}],
		group_class	: "width-30"
	}

})(jQuery);