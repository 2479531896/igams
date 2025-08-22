/*
 * @discretion	: default messages for the jQuery backdrop plugin.
 * @author    	: wandalong 
 * @version		: v1.0.1
 * @email     	: hnxyhcwdl1003@163.com
 */
;(function($){
	
	/*
 		===data-api接口===：	
		data-bs-toggle	：	用于绑定组件的引用
 		data-widget	:	组件元素上绑定的参数 {}
	*/
	$.bootui = $.bootui || {};
	$.bootui.widget = $.bootui.widget || {};
	
	/*====================== Backdrop CLASS DEFINITION ====================== */
	/*
	 * 构造器
	 */
	$.bootui.widget.Backdrop = function(element,options){
		options.beforeRender.call(this,element);	//渲染前的函数回调
		try {
			//实例化直接调用原型的initialize方法
			this.initialize.call(this, element, options);
		} catch (e) {
			options.errorRender.call(this,e);
		}
		options.afterRender.call(this,element);	/*渲染后的函数回调*/
	};
	
	$.bootui.widget.Backdrop.prototype = {
		constructor: $.bootui.widget.Backdrop,
		/*初始化组件参数*/
		initialize : function(element, options) {
			
			var $this = this; 
			this.$element  = $(element);
			this.options   = options;
		
			
			var bootbox = $(element).closest("div.bootbox");
			var modalBody = $(element).closest("div.modal-body");
			var isDialog = modalBody.size() > 0 ? true : false;
			var container = isDialog ? modalBody : $(element); 
			
			var backdropSize = $(document.body).find(".widget-backdrop").size();
			
			var loading = 	'<div class="widget-backdrop in" style="display: none;"></div>'+
							'<div class="widget-backdrop-loading" style="display: none;">'+
								'<div class="image header smaller lighter"><i class="icon-spinner icon-spin orange  bigger-160 "></i></div><div class="msg"></div>' + 
							'</div>';
						
			
			var backdrop,backdropLoading,image,message;
			
			function getBackdrop(){
				backdrop 		= $(document.body).find(".widget-backdrop");
				backdropLoading = $(document.body).find(".widget-backdrop-loading");
				image 	 		= $(backdropLoading).find("div.image");
				message 	 	= $(backdropLoading).find("div.msg");
			}
			
			function buildBackDrop(){
				
				getBackdrop();
				
				if(backdrop.size()==0){
					$(document.body).append(loading);
					getBackdrop();
				}
				
				//$(image).html(options.img);
				if(options.html){
					$(message).html(options.html);
				}else if(options.text){
					$(message).text(options.text);
				}else if(options.href){
					$(message).load(options.href,options.args||{});
				}else{
					$(backdropLoading).empty();
				}	

				$.each([backdrop,backdropLoading],function(i,element){
					$(element).bind("contextmenu",function(){
				        return false;   
				    });
				});
				
			};
			
			
			function initBackdrop(){
				
				var width = $(container).innerWidth();
				var height = $(container).innerHeight();
				var outerHeight = $(container).outerHeight();
				var offset = $(container).offset();
				var winHeight = $(window).height();
				var z_index =  options["z-index"]||( (bootbox.size() > 0 ) ? $(bootbox).css("z-index") : $(container).css("z-index")  ) ||"1040";
				var new_z_index = parseInt(z_index=="auto" ? "1040" : z_index ) + 5;
				
				$.each([backdrop,backdropLoading],function(i,element){
					$(element).css({
						"width"	:	parseInt(width)+"px",
						"height":	parseInt(height)+"px",
						"top"	: 	offset.top,
						"left"	:	offset.left
					});
				});
				$(backdrop).css({"z-index":	new_z_index	});
				$(backdropLoading).css({"z-index":	new_z_index + 1 });

				if(options.className&&$.trim(options.className).length>0){
					$(image).find("i").removeClass(options.className).addClass(options.className);
				}
				
				var halfHeight = (parseInt($(image).outerHeight()) + parseInt($(message).outerHeight()))/2
				//要遮罩的元素 距离顶部距离+自身的高度  > 窗口高度
				if((offset.top + outerHeight) > winHeight){
					$(image).css({"margin-top": (Math.round((winHeight - offset.top)/2)  - halfHeight)+"px"});
					var refixed = false;
					$(window).scroll(function(e) {
						if(!refixed){
							var scrollTop = Math.abs($(this).scrollTop());
							refixed = true;
							window.setTimeout(function(){
								refixed = false;
								//滚动量  <= 距离顶部距离
								if( scrollTop <= offset.top ){
									//(窗口高度 - (距离顶部距离 - 滚动量  ))/2
									$(image).css({"margin-top": Math.round(Math.min(outerHeight,(winHeight -  (offset.top - scrollTop)))/2 - halfHeight)+"px"});
								}else{
									$(image).css({"margin-top":(Math.round(Math.min(outerHeight,winHeight)/2) - halfHeight + (scrollTop - offset.top))  + "px"});
								}
							}, Math.abs(options.scrollDelay ||50));
						}
					});
				}else{
					$(image).css({"margin-top":(parseInt(height)/2 - halfHeight)+"px"});
				}
				
			};
			

			if($.fn.resize){
				$(container).resize(function(event){
					buildBackDrop();
	    			initBackdrop();
			    });
			}
			
			/*扩展this:组件函数*/
	    	$.extend($this,{
	    		setOptions	: function(opts){
					$.each(opts||{},function(key,val){
						delete options[key];
					});
					$.extend(options,opts||{});
				},
				show:function(opts){
					$this.setOptions(opts||{});
	    			buildBackDrop();
	    			initBackdrop();
					$(backdrop).fadeIn(400);
		    		$(backdropLoading).show();
				},
				hide: function(opts){
					$this.setOptions(opts||{});
					$(backdrop).fadeOut().remove();
					$(backdropLoading).fadeOut(400).remove();
				}
			});
	    	
	    	buildBackDrop();
		},
		destroy : function () {
			this.$element.off('.backdrop.widget').removeData("widget.backdrop");
		},
		setDefaults	: function(settings){
			$.extend($.fn.backdrop.defaults, settings );
		}
	};
	
	/* Backdrop PLUGIN DEFINITION  */
	$.fn.backdrop = function(option){
		//处理后的参数
		var args = $.grep( arguments || [], function(n,i){
			return i >= 1;
		});
		return this.each(function () {
			var $this = $(this),data = $this.data("widget.backdrop");
			if (!data && option == 'destroy') {return;}
			if (!data){
				var options = $.extend(true, {} , $.fn.backdrop.defaults, $this.data(), ((typeof option == 'object' && option) ? option : {}));
				$this.data('widget.backdrop', (data = new $.bootui.widget.Backdrop(this, options)));
			}
			if (typeof option == 'string'){
				//调用函数
				data[option].apply(data, [].concat(args || []) );
			}
		});
	};
	
	$.fn.backdrop.defaults = {
		/*版本号*/
		version			: '1.0.0',
		/*组件进行渲染前的回调函数：如重新加载远程数据并合并到本地数据*/
		beforeRender	: $.noop,
		/*组件渲染出错后的回调函数*/
		errorRender		: $.noop,
		/*组件渲染完成后的回调函数*/
		afterRender		: $.noop, 
		/*其他参数*/
		"z-index"		: null,
		//事件执行间隔
		"scrollDelay"	: 50,
		"className"		: "",
		"img"			: '<img src="images/ico-loading.gif" width="32" height="32" align="middle" />',
		"text"			: '加载中...',
		"html"			: null,
		"href"			: null,
		"args"			: {},
		"events"		: {}
	};

	$.fn.backdrop.Constructor = $.bootui.widget.Backdrop;
		

	/*============== Backdrop DATA-API  ==============*/
	/*委托验证事件：实现自动绑定*/
	$(document).on('click.widget.data-api', '[data-bs-toggle*="backdrop"]', function (event) {
		if(event.currentTarget = this){
			var $target = $(this).data("target");
			$($target).backdrop({
				
			});
			
			$($target).backdrop("show");
		}
	});
	

}(jQuery));
