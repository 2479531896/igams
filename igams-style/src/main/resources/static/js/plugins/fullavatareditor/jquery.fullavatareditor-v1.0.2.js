/*
 * @discretion	: jQuery fullAvatarEditor 插件
 * @author    	: wandalong 
 * @version		: v1.0.0
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
	
	/*====================== FULLAVATAREDITOR CLASS DEFINITION ====================== */
	
	$.bootui.widget.FullAvatarEditor = function(element,options){
		options.beforeRender.call(this,element);	//渲染前的函数回调
		try {
			//实例化直接调用原型的initialize方法
			this.initialize.call(this, element, options);
		} catch (e) {
			options.errorRender.call(this,e);
		}
		options.afterRender.call(this,element);	/*渲染后的函数回调*/
	};
	
	$.bootui.widget.FullAvatarEditor.prototype = {
		constructor: $.bootui.widget.FullAvatarEditor,
		/*初始化组件参数*/
		initialize 	: function(element,options) {
			
			var rtMap = {};
			
			//延时判断swfobject加载状态
			var times_1 = 0;
			var interval_1 = window.setInterval(function(){
				if(times_1 >= 20){
					window.clearInterval(interval_1);
					throw new Error("swfobject not initialized");
				}
				if(swfobject){
					window.clearInterval(interval_1);
					//监听对象事件
					swfobject.addDomLoadEvent(function () {
						$.when($.Deferred(function(def){
							$(element).data("def",def);
							try {
								function getUID(prefix) {
									do {
									  	prefix += ~~(Math.random() * 1000000);
								  	}while (document.getElementById(prefix));
								  	return prefix;
								};
								
								var flashvars = options["flashvars"]||{};
								$.each(flashvars , function(name,val){
									//处理参数
									if(name == 'upload_url' || name == 'src_url') {
										flashvars[name] = encodeURIComponent(flashvars[name]);
									}
								})
								//参数处理
								flashvars["id"] = flashvars["id"] || getUID("fullavatareditor");
								flashvars["src_size_over_limit"] = flashvars["src_size_over_limit"].replace('{src_size}',flashvars["src_size"]);
								flashvars["browse_tip"] = flashvars["browse_tip"].replace('{src_size}',flashvars["src_size"]);
								//swf参数
								var params = $.extend({
									menu				: 'true',
									scale				: 'noScale',
									allowFullscreen		: 'true',
									allowScriptAccess	: 'always',
									wmode				: 'transparent'
								},options["params"]||{});
								
								var attributes = $.extend({
									id	: flashvars["id"],
									name: flashvars["id"]
								},options["attributes"]||{});
								
								swfobject.embedSWF(
									options["file"], 
									options["container"],
									options["width"], 
									options["height"],
									options["version"], 
									options["expressInstall"],
									flashvars, 
									params, 
									attributes, 
									function(e) {
										// 改变deferred对象的执行状态为：已完成
										def.resolve(); 
										var swf = e.ref;
										swf.eventHandler = function(data) {
											//处理回调函数
											options["onEvent"].call(swf, data);
										};
										window.swf = swf;
									}
								);
							} catch (e) {
								$(element).show();
								console.error(e);
								// 改变Deferred对象的执行状态为：已失败
								def.reject();
							}
						}).promise()).done(function(){
							$(element).show();
							$(element).removeData("def");
						});
					});
				}
				times_1 += 1;
			}, 500);
			 
			
			/*添加需要暴露给开发者的函数*/
			$.extend(true,this,{
				getSwfObject : function () {
					if($(element).data("def")){
						throw new Error("FullAvatarEditor not initialized");
					}
					return window.swf;
				},
				destroy : function () {
					$(element).off('.fullAvatarEditor').removeData('bootui.fullAvatarEditor');
				}
			});
		},
		setDefaults	: function(settings){
			$.extend($.fn.fullAvatarEditor.defaults, settings );
		},
		getDefaults	: function(){
			 return $.fn.fullAvatarEditor.defaults;
		}
	}
	
	/* FULLAVATAREDITOR PLUGIN DEFINITION  */
	
	$.fn.fullAvatarEditor = function(option){
		//处理后的参数
		var args = $.grep( arguments || [], function(n,i){
			return i >= 1;
		});
		var get = '', element = this.each(function () {
			var $this = $(this),data = $this.data("bootui.fullAvatarEditor");
			if (!data && option == 'destroy') {return;}
			if (!data){
				var options = $.extend(true, {} , $.fn.fullAvatarEditor.defaults, $this.data(), ((typeof option == 'object' && option) ? option : {}));
				$this.data('bootui.fullAvatarEditor', (data = new $.bootui.widget.FullAvatarEditor(this, options)));
			}
			if (typeof option == 'string'){
				//调用函数
				get = data[option].apply(data, [].concat(args || []) );
			}
		});
		if ( typeof get !== undefined) {
			return get;
		} else {
			return element;
		}
	};
	
	$.fn.fullAvatarEditor.defaults = {
		/*组件进行渲染前的回调函数：如重新加载远程数据并合并到本地数据*/
		beforeRender	: $.noop,
		/*组件渲染出错后的回调函数*/
		errorRender		: $.noop,
		/*组件渲染完成后的回调函数*/
		afterRender		: $.noop, 
		/*其他参数*/
		
		//接收消息的swf的ID，用以区分同一页面如果存在多个组件。
		container		: 'swf_container',
		//flash文件的宽度
		width			: 630,
		//flash文件的高度
		height			: 430,
		//播放该flash所需的最低版本
		version 		: "10.1.0",
		//插件主swf文件的路径，文件名必须是FullAvatarEditor.swf。
		file			: _path + '/js/plugins/fullavatareditor/fullAvatarEditor.swf', 
		//expressInstall.swf的路径
		expressInstall	: _path + '/js/plugins/fullavatareditor/expressInstall.swf',
		attributes		: {
			
		},
		params			: {
			menu				: 'true',
			scale				: 'noScale',
			allowFullscreen		: 'true',
			allowScriptAccess	: 'always',
			wmode				: 'transparent'
		},
		//flashvars 配置参数（object，必须）
		flashvars		: {
			//接收消息的swf的ID，用以区分同一页面如果存在多个组件。
		    id			: 'swf_container',

		    //------------上传路径参数---------------------
		  
		    //上传图片的接口。该接口需返回一个json字符串，且会原样输出到 callback 回调函数 的参数对象的属性content中
	        upload_url 			: '',
	        //上传提交的方式，值为 get 或 post，不分大小写。
	        method				: "post",
	        //生成的头像图片的质量，取值范围1-100，数值越大生成的图片越清晰，相对地文件也会越大。
	        quality				: 100,
	        
			//------------原图片参数---------------------
			
	        //默认加载的原图片的url
	        src_url 			: "",
	        //选择的本地图片文件所允许的最大值，必须带单位，如888Byte，88KB，8MB			
	        src_size			: "8MB",
	      	//是否上传原图片的选项：2-显示复选框由用户选择，0-不上传，1-上传,2 -显示复选框由用户选择	
	        src_upload 			: 0,	
			//当选择的原图片文件的大小超出指定最大值时的提示文本。可使用占位符{0}表示选择的原图片文件的大小。
	        src_size_over_limit : '选择的文件大小（{0}）超出限制（{src_size}）\n请重新选择',
	        //上传区域提示
	        browse_tip:'仅支持JPG、JPEG、GIF、PNG格式的图片文件\n文件不能大于{src_size}',
	        //------------选项卡参数---------------------
	        
	      	//不显示选项卡，外部自定义
	        tab_visible 		: false,

   			//------------图片选择框	参数---------------------
   			
   			//图片选择框的水平对齐方式。left：左对齐；center：居中对齐；right：右对齐；数值：相对于舞台的x坐标
	        browse_box_align 	: 'left',
	        //图片选择框的宽度。	
	        browse_box_width 	: 300,
	        //图片选择框的高度。
	        browse_box_height	: 300,
	        
	      	//------------按钮和复选框参数---------------------
	      	
	      	//不显示按钮，外部自定义
	        button_visible 		: false,			
	      	//不显示复选框，外部自定义
	        checkbox_visible 	: false,	
	        		
	      	//------------ 摄像头相关参数---------------------
	      
	      	//摄像头拍照框的水平对齐方式，如上。			
			webcam_box_align 	: 38,	
			//头像拍照框对齐方式      
	        webcam_box_align	: 'left', 

	        //------------处理后的头像参数---------------------
	        
			//定义头像尺寸：表示一组或多组头像的尺寸。其间用"|"号分隔。
			avatar_sizes 		: '150*200',			
			//头像尺寸的提示文本。多个用"|"号分隔，与上一项对应
			avatar_sizes_desc 	: '150*200像素',	
			//头像的表单域名称，多个用"|"号分隔，与 avatar_sizes 项对应。
			avatar_field_names	: 'image1',  
			//头像简介
			avatar_intro 		: '最终会生成以下尺寸的头像，请注意是否清晰',
			//是否显示头像颜色调整工具。
			avatar_tools_visible: true
			
		},
		/*触发选中事件*/
		onEvent		: function(data){ 
			/*
			 * 请访问官网：http://www.fullavatareditor.com
			 * data.code : 
			  data.type : */
			 
			
	   	}
	};

}(jQuery));

