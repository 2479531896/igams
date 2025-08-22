/*
 * @discretion	: default messages for the jQuery fixedpanel plugin.
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
		...
	*/
	
	$.bootui = $.bootui || {};
	$.bootui.widget = $.bootui.widget || {};
	/*====================== FIXEDPANEL CLASS DEFINITION ====================== */
	/*
	 * 构造器
	 */
	$.bootui.widget.FixedPanel = function(element,options){
		options.beforeRender.call(this,element);	//渲染前的函数回调
		try {
			//实例化直接调用原型的initialize方法
			this.initialize.call(this, element, options);
		} catch (e) {
			options.errorRender.call(this,e);
		}
		options.afterRender.call(this,element);	/*渲染后的函数回调*/
	};
	
	$.bootui.widget.FixedPanel.prototype = {
		constructor: $.bootui.widget.FixedPanel,
		/*初始化组件参数*/
		initialize : function(element, options) {
			
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
			
			this.fixedpanel_uid = getUID(options.prefix);
			this.$element  	 = $(element);
			this.options   	 = options;
			//判断当前元素是否在bootbox弹窗中从而确定容器
			var bootbox = $(element).closest("div.bootbox");
			//判定容器
			var container =  options.container ? $(options.container) : ($(bootbox).size() > 0 ? $(bootbox).find("div.modal-body") : window); 
			var eContainer = $(bootbox).size() > 0 ? $(bootbox).find("div.modal-body") : document.body; 
			//层索引
			var z_index   = $(bootbox).size() > 0 ? ( parseInt(bootbox.css("z-index")||"1050") + 90) : 1010; 
			//获得fixedpanel对象
			var fixedpanel = element;
			var placement 		= (options.placement||"right").toLocaleLowerCase();
			//自动构建内容
			if($.trim(options.content).length > 0){
				//初始化panel
				fixedpanel =  $( options.template );
				//设置ID
				$(fixedpanel).attr("id",this.fixedpanel_uid);
				//标题
				if($.trim(options.title).lenght > 0){
					$(fixedpanel).find(".panel-heading").prepend('<h4 class="fixedpanel-title">' +  ($.isFunction(options.title) ? options.title.call(this) : options.title.toString()) + '</h4>');
				}else{
					$(fixedpanel).find(".panel-heading").remove();
				}
				//初始化内容
				$(fixedpanel).append( ($.isFunction(options.content) ? options.content.call(this) : options.content.toString()));
				//判断位置：决定添加dom的位置
				switch (placement) {
					case "left":
					case "top":{
						//添加fixedpanel到指定dom元素
						$(eContainer).prepend(fixedpanel);
					};break;
					case "right":
					case "bottom":{
						//添加fixedpanel到指定dom元素
						$(eContainer).append(fixedpanel);
					};break;
				}  
			}else{
				$(fixedpanel).addClass("panel fixedpanel");
			}
			
			//判断是否需要回滚到指定位置
			var opts =  options.rollback||{};
			if(opts && opts.enable){
				var list_group = $(fixedpanel).find(".list-group");
				var table = $(fixedpanel).find(".table");
				if(list_group.size() > 0 ){
					$(list_group).append('<li class="list-group-item btn_rollback">'+opts.text+'</li>');
				}else if(table.size() > 0 ){
					$(table).find("tbody").append('<tr class="tr-item"><td class="tr-td-item btn_rollback">'+opts.text+'</td></tr>')
				}
				//判断位置
				switch (placement) {
					case "left":
					case "right":{
						$(fixedpanel).find(".btn_rollback").click(function(){
							$("html,body").animate({scrollTop: opts.point}, opts.speed,opts.callback||$.noop);	
						});
					};break;
					case "top":
					case "bottom":{
						$(fixedpanel).find(".btn_rollback").click(function(){
							$("html,body").animate({scrollLeft: opts.point}, opts.speed, opts.callback||$.noop);	
						});
					}
				};
			}
			//定位
			$(fixedpanel).fixed({
				//位置
				"placement"		: 	placement,
				//容器
				"container"		: 	container,
				//宽度
				"width"			: 	options.width||"",
				//高度
				"height"		: 	options.height||"",
				//浮动层距离容器边框距离；根据位置不同，分别表示左边距、右边距、上边距、下边距
				"margin"		:   options.margin||"",
				//显示位置值：横向时表示left，纵向时表示top
				"showPoint"		: 	options.showPoint!=0? (options.showPoint||100):0,
				//层索引
				"z_index"		:	z_index,
				//事件执行间隔
				"resizeDelay"	:  	options.resizeDelay,
				"scrollDelay"	: 	options.scrollDelay,
				"animateDelay"	: 	options.animateDelay,
				//css
				"position"		:	options.position || {},
				
				onScroll		:	options.onScroll||$.noop,
				onResize		:	options.onResize||$.noop
				
			});
			//判断位置
			switch (placement) {
				case "left":
				case "right":{
					//初始化宽度
					$(fixedpanel).find(".list-group,.table").width(options.width||"auto");
				};break;
				case "top":
				case "bottom":{
					//初始化高度
					$(fixedpanel).find(".list-group,.table").height(options.height||"auto");
				}
			};
			this.fixedpanel = fixedpanel;
		},
		destroy : function () {
			this.$element.off('.fixedpanel.widget.data-api').removeData("fixedpanel");
			$(this.fixedpanel).fixed("destroy");
			//在自动生成的情况下才会有效的删除
			$("#"+this.fixedpanel_uid).remove();
		},
		setDefaults	: function(settings){
			$.extend($.fn.fixedpanel.defaults, settings );
		}
	};
	
	/* FIXEDPANEL PLUGIN DEFINITION  */
	$.fn.fixedpanel = function(option){
		//处理后的参数
		var args = $.grep( arguments || [], function(n,i){
			return i >= 1;
		});
		return this.each(function () {
			var $this = $(this),data = $this.data("fixedpanel");
			if (!data && option == 'destroy') {return;}
			if (!data){
				var options = $.extend(true,{}, $.fn.fixedpanel.defaults, $this.data(), ((typeof option == 'object' && option) ? option : {}));
				$this.data('fixedpanel', (data = new $.bootui.widget.FixedPanel(this, options)));
			}
			if (typeof option == 'string'){
				//调用函数
				data[option].apply(data, [].concat(args || []) );
			}
		});
	};
	
	$.fn.fixedpanel.defaults = {
		/*版本号*/
		version			: '1.0.0',
		/*组件命名前缀*/
		prefix			: 'fixedpanel',
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
		//标题： 普通text文本、function
		title			: '',
		//在容器中的位置：left,top,right,bottom
		placement		: 'right',
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
		//回滚参数
		rollback		: {
			//是否启用回滚
			enable		: false,
			//文字内容
			text		: "回到顶部",
			//返回点
			point		: 0,
			//返回记录点的速度以毫秒为单位
			speed		: 300,
			//滚动结束的回调函数
			callback	: $.noop
		},
		//css
		position		: {},
		//内容： 普通text文本、html文本、function
		content			: '', 
		//模板
		template		: '<div class="panel panel-default fixedpanel"><div class="panel-heading"></div></div>'
	};
	

	$.fn.fixedpanel.Constructor = $.bootui.widget.FixedPanel;

}(jQuery));
