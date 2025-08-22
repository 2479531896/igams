/*
 * @discretion	: jQuery Tree plugin base on bootstrap and jQuery.
 * @author    	: wandalong
 * @version		: v1.0.0
 * @email     	: hnxyhcwdl1003@163.com
 */
;(function($){
	
	/*
		===data-api接口===：	
		data-height			：	组件高度
		data-url			：	组件数据的请求地址
		data-mtype			:	ajax提交方式。POST或者GET，默认GET ； data-mtype="POST|GET"
		data-cache			:	是否缓存数据； data-cache="true|false"
		data-collapsable	:	是否可折叠展开 ； data-collapsable="true|false"
		data-collapseSpeed	:	折叠展开速度； data-collapseSpeed="字符串("slow","normal", or "fast")或表示动画时长的毫秒数值(如：1000)"
		data-expandText		:	树展开鼠标提示； data-expandText="展开节点"
		data-collapseText	:	树折叠鼠标提示； data-collapseText="折叠节点"
		data-selectable		:	是否节点可选择；  data-selectable="true|false"
		...
	*/
		
	$.bootui = $.bootui || {};
	$.bootui.widget = $.bootui.widget || {};
	
	/*====================== TREEVIEW CLASS DEFINITION ====================== */
	
	$.bootui.widget.BootstrapTree = function(element,options){
		options.beforeRender.call(this,element);	//渲染前的函数回调
		try {
			//实例化直接调用原型的initialize方法
			this.initialize.call(this, element, options);
		} catch (e) {
			options.errorRender.call(this,e);
		}
		options.afterRender.call(this,element);	/*渲染后的函数回调*/
	};
	
	
	$.bootui.widget.BootstrapTree.prototype = {
		constructor: $.bootui.widget.BootstrapTree,
		/*初始化组件参数*/
		initialize 	: function(element,options) {
			//需要使用JSON数据渲染界面html
			if($(element).children().size() == 0 ){
				//程数据模式
	            if($.trim(options.url).length>0){
					$.ajax({
					   type		: options.mtype||"POST",
					   url		: options.url,
					   data		: options.postData||{},
					   cache	: options.cache || false,
					   async	: false,
					   success	: function(data){
						   options.data = data||[];
					   }
					});
	            }
	            /**
	             * node节点数据结构如：
	             * {id:"", name:"", text:"", parent_id:"", iconClass:"", disabled:"false", checked:"false", attributes:{}, children:[ {},{},... ] }
	             */
				function buildNode(nodeName,nodeLevel,node){
					var html = [];
					var styleStr = parseInt(nodeLevel||'0') > parseInt(options.visibleLevel||'0') ? '  style="display: none;" ' : " ";
					
						html.push('<li id="'+(node["id"]||"")+'" data-parent="'+(node["parent_id"]||"")+'" '+ ((node["disabled"] == true)?' class="disabled" ' : "")  + styleStr);
						$.each(node.attributes||{},function(key,value){
							html.push(' ' + key + '="' + value + '" ');
						});
						html.push(' >');
							if(node["selectable"] == true){
								html.push('<input type="checkbox" name="' + (nodeName+'.' + (node["name"]||node["id"]||"node_id")) + '"  ' + ((node["checked"] == true)?' checked="checked" ' : "") + '>');
							}
							html.push("<span >");
							if(node["selectable"] != true && $.founded(node["iconClass"])){
								html.push('<i class="'+node["iconClass"]+'"></i>');
							}
							html.push(node["text"]);
							html.push("</span>");
							$.each(node.children||[],function(i2,node2){
								html.push(buildNode(nodeName +'.children['+i2+']',(nodeLevel+1),node2));
							});
						html.push("</li>");
					return html.join("");
				}
				
				var html = [];
					if(!$(element).is("div")){
						html.push('<div class="tree well">');
					}
					html.push("<ul>");
					$.each(options.data||[],function(i,node){
						html.push(buildNode('treeList['+i+']',0,i,node));
					});
					html.push("</ul>");
					if(!$(element).is("div")){
						html.push("</div>");
					}
					
					$(element).empty().html(html.join(""));
			}
			if(options.collapsable == true){
				//仅需要绑定伸缩事件
				$(element).find('.tree li:has(ul)').addClass('parent_li').find(' > span:eq(0)').attr('title', options.collapseText);
				$(element).find('.tree li.parent_li > span').on('click', function(e) {
					if($(this).index() == 0){
						var children = $(this).parent('li.parent_li').find(' > ul > li');
						if (children.is(":visible")) {
							children.hide(options.collapseSpeed || 'fast');
							$(this).attr('title', options.expandText ).find(' > i').addClass(options.expandIconClass).removeClass(options.collapseIconClass);
						} else {
							children.show(options.collapseSpeed || 'fast');
							$(this).attr('title', options.collapseText ).find(' > i').addClass(options.collapseIconClass).removeClass(options.expandIconClass);
						}
					}
			        e.stopPropagation();
			    });

			}
			/*添加需要暴露给开发者的函数*/
			$.extend(true,this,{
				destroy : function () {
					$(element).off('.bootstrapTree').removeData('bootui.bootstrapTree');
				}
			});
		},
		setDefaults	: function(settings){
			$.extend($.fn.bootstrapTree.defaults, settings );
		},
		getDefaults	: function(){
			 return $.fn.bootstrapTree.defaults;
		}
	};
	
	/* TREEVIEW PLUGIN DEFINITION  */
	$.fn.bootstrapTree = function(option){
		//处理后的参数
		var args = $.grep( arguments || [], function(n,i){
			return i >= 1;
		});
		return this.each(function () {
			var $this = $(this),data = $this.data("bootui.bootstrapTree");
			if (!data && option == 'destroy') {return;}
			if (!data){
				var options = $.extend(true, {} , $.fn.bootstrapTree.defaults, $this.data(), ((typeof option == 'object' && option) ? option : {}));
				$this.data('bootui.bootstrapTree', (data = new $.bootui.widget.BootstrapTree(this, options)));
			}
			if (typeof option == 'string'){
				//调用函数
				data[option].apply(data, [].concat(args || []) );
			}
		});
	};
	
	$.fn.bootstrapTree.defaults = {
		/*版本号*/
		version			:'1.0.0',
		/*组件进行渲染前的回调函数：如重新加载远程数据并合并到本地数据*/
		beforeRender	: $.noop,
		/*组件渲染出错后的回调函数*/
		errorRender		: $.noop,
		/*组件渲染完成后的回调函数*/
		afterRender		: $.noop, 
		/*组件高度*/
		height			: "300px",
		/*数据的请求地址 */
		url				: '', 
		/* 请求参数 ： 此数组内容直接赋值到url上，参数类型：{name1:value1…} */
		postData		: {},
		/* ajax提交方式。POST或者GET，默认GET */
		mtype 			: 'POST',
		/*是否缓存数据*/
		cache			: false,
		/*是否可折叠展开 */
		collapsable 	: true,
		/*折叠展开速度*/
		collapseSpeed	: "fast",
		/*树展开鼠标提示 */
		expandText 		: "展开节点",
		/*树展开图标样式 ：默认是子父节点图标，还可用：icon-folder-open-alt, icon-chevron-sign-down,icon-caret-down*/
		expandIconClass : "icon-expand-alt", 
		/*树折叠鼠标提示 */
		collapseText	: "折叠节点",
		/*树折叠图标样式：默认是子父节点图标，还可用：icon-folder-close-alt,icon-chevron-sign-up, icon-caret-up */
		collapseIconClass : "icon-collapse-alt",
		/*是否节点可选择*/
		selectable 		: false,
		/*最小可见树形结构层级，大于该层级的节点初始时将不可见。*/
		visibleLevel 	: 1,
		/*树初始数据*/
		data			: []
	}
	
	
	
})(jQuery);