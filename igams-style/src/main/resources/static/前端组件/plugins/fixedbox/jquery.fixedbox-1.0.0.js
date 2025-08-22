/*
 * @discretion	: 区域固定插件
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
		data-innerTitle		：	组件是否有内部标题；默认false
		data-trigger		：	组件触发事件的类型hover,click,dbclick,mouse；默认click
		data-title			:	组件标题： 普通text文本、function
		data-disc			:	组件紧跟标题下的其他描述： 普通text文本、html文本、function ;格式：<span>已选<b>3</b></span>
		data-content		:	组件内容： 普通text文本、html文本、function
		data-href			:	组件远程地址
		data-data			:	组件远程参数
		...
		
	*/
	$.bootui = $.bootui || {};
	$.bootui.widget = $.bootui.widget || {};
	/*====================== FixedBox CLASS DEFINITION ====================== */
	/*
	 * 构造器
	 */
	$.bootui.widget.FixedBox = function(element,options){
		options.beforeRender.call(this,element);	//渲染前的函数回调
		//try {
			//实例化直接调用原型的initialize方法
			this.initialize.call(this, element, options);
		//} catch (e) {
			//options.errorRender.call(this,e);
		//}
		options.afterRender.call(this,element);	/*渲染后的函数回调*/
	};
	
	$.bootui.widget.FixedBox.prototype = {
		constructor: $.bootui.widget.FixedBox,
		/*初始化组件参数*/
		initialize : function(element, options) {
		
			//容器判断
			if(options.container && $(options.container).size() == 0){
				throw new Error("Container is not a valid element!");
			}
			
			var $this = this; 
			
			function getUID(prefix) {
				do {
				  	prefix += ~~(Math.random() * 1000000);
			  	}while (document.getElementById(prefix));
			  	return prefix;
			};
			
			this.fixedbox_uid = getUID(options.prefix);
			this.$element  	 = $(element);
			this.options   	 = options;
			
			//判断组件未初始化
			if($("#"+this.fixedbox_uid).size() == 0 ){
				
				//判断当前元素是否在bootbox弹窗中从而确定容器
				var bootbox = $(element).closest("div.bootbox");
				//判定容器
				var container 	=  options.container ? $(options.container) : ($(bootbox).size() > 0 ? $(bootbox).find("div.modal-body") : window); 
				var eContainer  = $(bootbox).size() > 0 ? $(bootbox).find("div.modal-body") : document.body; 
				//层索引
				var z_index   = $(bootbox).size() > 0 ? ( parseInt(bootbox.css("z-index")||"1050") + 90) : 1010; 
				//位置
				var placementVal 		= (options.placement||"right").toLowerCase();
				//初始化fixedbox 对象
				var fixedbox = $( options.template );
				//设置ID并给元素添加样式
				$(fixedbox).attr("id",this.fixedbox_uid).addClass("fixedbox-"+placementVal);
				//侧栏
				var outer = $(fixedbox).find(".fixedbox-outer");
				$(outer).addClass("fixedbox-outer-"+placementVal);
				//初始化箭头样式
				$(outer).find(".glyphicon").addClass("glyphicon-chevron-"+placementVal);
				
				//判断位置：决定添加dom的位置
				var content = '<div class="fixedbox-content"><div class="fixedbox-content-body"></div></div>';
				switch (placementVal) {
					case "left":
					case "top":{
						$(outer).before(content);
					};break;
					case "right":
					case "bottom":{
						$(outer).after(content);
					};break;
				}  
				//内容部分Element
				var fixedbox_content = $(fixedbox).find(".fixedbox-content");
				//添加样式
				$(fixedbox_content).addClass("fixedbox-content-"+placementVal)
				//小标题
				var fixedbox_outer_disc = $(outer).find(".fixedbox-outer-disc");
				$(fixedbox_outer_disc).append('<h5>' +  ($.isFunction(options.title) ? options.title.call(this) : options.title.toString()) + '</h5>');
				$(fixedbox_outer_disc).append( ($.isFunction(options.disc) ? options.disc.call(this) : options.disc.toString()));
				//内部大标题
				if(options.innerTitle){
					$(fixedbox_content).prepend('<h4 class="fixedbox-content-title">' +  ($.isFunction(options.title) ? options.title.call(this) : options.title.toString()) + '</h4>');
				}
				//初始化内容
				var content_body = $(fixedbox_content).find(".fixedbox-content-body");
				if($.trim(options.content).length > 0){
					$(content_body).append( ($.isFunction(options.content) ? options.content.call(this) : options.content.toString()));
				}else if($.trim(options.href).length > 0){
					$(content_body).load(options.href, $.extend(true,{},options.data||{}));
				}
				//判断位置：决定添加dom的位置
				switch (placementVal) {
					case "left":
					case "top":{
						//添加fixedbox到指定dom元素
						$(eContainer).prepend(fixedbox);
					};break;
					case "right":
					case "bottom":{
						//添加fixedbox到指定dom元素
						$(eContainer).append(fixedbox);
					};break;
				}  
				//是否左右
				var left_right = true;
				//判断位置：决定添加样式
				switch (placementVal) {
					case "left":{
						left_right = true;
						var content_width = parseInt(options.width||'100') - 60 ;
							content_width	= content_width< 0 ? 0 : content_width;
						$(fixedbox_content).css({width: content_width+"px"});
					}
					case "right":{
						left_right = true;
						var content_width = parseInt(options.width||'100') - 60 ;
							content_width	= content_width< 0 ? 0 : content_width;
						$(fixedbox_content).css({width: content_width+"px"});
						$(fixedbox).css('left','auto');
					};break;
					case "top":
					case "bottom":{
						left_right = false;
						var content_height = parseInt(options.height||'100') - 60 ;
							content_height	= content_height< 0 ? 0 : content_height;
						$(fixedbox_content).css({height: content_height+"px"});
					};break;
				}
				
				//绑定事件
				var triggerEvent = options.trigger||"click";
				var glyphicon = $(fixedbox).find(".fixedbox-outer .glyphicon");
				$(outer).off(triggerEvent).on((triggerEvent),function(e) {	
					if(left_right){
						$(glyphicon).removeClass("glyphicon-chevron-left glyphicon-chevron-right");
						if($(fixedbox).css("width") == "40px"){
							$(fixedbox).animate({width:options.width||'100px'},600);
							$(glyphicon).addClass("glyphicon-chevron-"+["left","right"]["left" == placementVal?1:0]);
						}else{
							$(fixedbox).animate({width:"40px"},600);
							$(glyphicon).addClass("glyphicon-chevron-"+["left","right"]["left" == placementVal?0:1]);
						}
					}else{
						$(glyphicon).removeClass("glyphicon-chevron-top glyphicon-chevron-bottom");
						if($(fixedbox).css("height") == "40px"){
							$(fixedbox).animate({height:options.height||'100px'},600);
							$(glyphicon).addClass("glyphicon-chevron-"+["top","bottom"]["top" == placementVal?1:0]);
						}else{
							$(fixedbox).animate({height:"40px"},600);
							$(glyphicon).addClass("glyphicon-chevron-"+["top","bottom"]["top" == placementVal?0:1]);
						}
					}
				});
				//点击页面其他地方右侧内容隐藏
				$(document).click(function(e){
					var drag = $(fixedbox), dragel = $(fixedbox)[0], target = e.target;
					if (dragel !== target && !$.contains(dragel, target)) {
						$(glyphicon).removeClass("glyphicon-chevron-"+placementVal);
						if(left_right){
							$(glyphicon).addClass("glyphicon-chevron-"+["left","right"]["left" == placementVal?1:0]);
							$(fixedbox).animate({width:"40px"},600);
						}else{
							$(glyphicon).addClass("glyphicon-chevron-"+["top","bottom"]["top" == placementVal?1:0]);
							$(fixedbox).animate({height:"40px"},600);
						}
					}
				});
				//如果框架映入 mCustomScrollbar 插件
				if($.fn.mCustomScrollbar){
					$(content_body).mCustomScrollbar({
						autoHideScrollbar:false,
						horizontalScroll:(left_right?false:true),
						advanced:{
						    updateOnBrowserResize: true,
						    updateOnContentResize: true,
						    autoExpandHorizontalScroll: true
						},
						theme:"dark-3"
					});
				}

				function resizeEvent(){
					if(left_right){
						$(content_body).css({"height":$(fixedbox).innerHeight() - $(fixedbox_content).find(".fixedbox-content-title").outerHeight() - 20});
					}else{
						$(content_body).css({"width":$(fixedbox).innerWidth() - $(fixedbox_content).find(".fixedbox-content-title").outerWidth() - 20});
					}
					//如果框架映入 mCustomScrollbar 插件
					if($.fn.mCustomScrollbar){
						//页面缩小和变化的时候滚动条参数修改
						$(content_body).mCustomScrollbar("update");
					}
				}
				//定位
				$(fixedbox).fixed({
					//位置
					"placement"		: placementVal,
					//容器
					"container"		: container,
					//宽度
					"width"			: left_right? "40px":"100%",
					//高度
					"height"		: left_right? "100%":"40px",
					//浮动层距离容器边框距离；根据位置不同，分别表示左边距、右边距、上边距、下边距
					margin			: "0",
					//显示位置值：横向时表示left，纵向时表示top
					showPoint		: "0",
					//层索引
					"z_index"		: z_index,
					//事件执行间隔
					resizeDelay		: 100,
					scrollDelay		: 200,
					animateDelay	: 200,
					//css
					position		: left_right? { "top":0,"bottom": 0 } :{"left": 0 ,"right": 0},
					//组件滚动回调函数
					onScroll		: function(e){
						
					},
					//组件尺寸变化回调函数
					onResize		: function(e){
						resizeEvent();
					} 
				});
				this.fixedbox = fixedbox;
			}
		},
		destroy : function () {
			this.$element.off('.fixedbox').removeData("bootui.fixedbox");
			if($("#"+this.fixedbox_uid).size() > 0 ){
				$(this.fixedbox).fixed("destroy");
				$("#"+this.fixedbox_uid).remove();
			}
		},
		setDefaults	: function(settings){
			$.extend($.fn.fixedbox.defaults, settings );
		}
	};
	
	/* FixedBox PLUGIN DEFINITION  */
	
	/*
	 * jQuery原型上自定义的方法
	 */
	$.fn.fixedbox = function(option){
		//处理后的参数
		var args = $.grep( arguments || [], function(n,i){
			return i >= 1;
		});
		return this.each(function () {
			var $this = $(this),data = $this.data("bootui.fixedbox");
			if (!data && option == 'destroy') {return;}
			if (!data){
				var options = $.extend({}, $.fn.fixedbox.defaults, $this.data(), ((typeof option == 'object' && option) ? option : {}));
				$this.data('bootui.fixedbox', (data = new $.bootui.widget.FixedBox(this, options)));
			}
			if (typeof option == 'string'){
				//调用函数
				data[option].apply(data, [].concat(args || []) );
			}
			
		});
	};
	
	$.fn.fixedbox.defaults = {
		/*版本号*/
		version:'1.0.0',
		/*组件命名前缀*/
		prefix			: 'fixedbox',
		/*组件进行渲染前的回调函数：如重新加载远程数据并合并到本地数据*/
		beforeRender	: $.noop,
		/*组件渲染出错后的回调函数*/
		errorRender		: $.noop,
		/*组件渲染完成后的回调函数*/
		afterRender		: $.noop, 
		//组件滚动回调函数
		onScroll		: $.noop, 
		//组件尺寸变化回调函数
		onResize		: $.noop, 
		/*其他参数*/
		//容器
		container		: null,
		//内部标题
		innerTitle		: false,
		//宽度
		width			: "100px",
		//高度
		height			: "100px",
		//触发事件的类型
		trigger			: 'click',
		//在容器中的位置：left,top,right,bottom
		placement		: 'right',
		//标题： 普通text文本、function
		title			: '',
		//紧跟标题下的其他描述： 普通text文本、html文本、function ;格式：<span>已选<b>3</b></span>
		disc			: '',
		//内容： 普通text文本、html文本、function
		content			: '',
		//远程地址
		href			: null,
		data			: {},
		//模板
		template		: '<div class="fixedbox"><div class="fixedbox-outer"><a href="javascript:void(0);" class="glyphicon"></a><a href="javascript:void(0);" class="fixedbox-outer-disc"></a></div></div>'
	};

	$.fn.fixedbox.Constructor = $.bootui.widget.FixedBox;

}(jQuery));
