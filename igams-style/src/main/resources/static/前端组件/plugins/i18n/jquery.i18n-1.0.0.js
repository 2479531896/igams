/**
 * 
1、加载当前功能国际化资源,并对包含属性data-localize的元素进行国际化处理
$.i18n.localize({ 
	//当前功能所属的系统模块，国际化代码会根据该参数选择不同模块的国际化配置信息
	i18nModule	: "jxjhgl",
	//是否一直读取最新的国际化信息,测试阶段可设置为true
    i18nLastest	: true,
	//国际化信息key
	i18nKey 	: []
});
2、国际化信息取值
$.i18n.prop("key",param1,param2,param2,...)
$.i18n.prop("key",[params])
3、grid国际化
	//是否进行grid国际化：需要 jquery.i18n插件的支持
	i18nable	: true,
	//是否一直读取最新的国际化信息，仅当i18nable为true时有效
	i18nLastest : false,
 * 
 */
;(function($) {
	
	$.i18n = $.i18n || {};
	
	$.i18n.language = "zh_CN";
	
	/** Map holding bundle keys (if mode: 'map') */
	$.i18n.map = $.i18n.map || {};
	
	$.i18n.setLanguage = function (language) {
		$.i18n.language = language;
		var date = new Date();
	    	date.setTime(date.getTime() + (30 * 24 * 60 * 60 * 1000));
		if(store){
			store.set("language",language);
		}else if($.cookie){
			$.cookie( "language", escape(language),{
				"expires"	: date ,
				"path"		: "/"
			});
		}else{
			document.cookie = "language=" + escape(language) + "; " + "expires=" + date.toUTCString() + "; path=/";
		}
	};
	
	$.i18n.getLanguage = function(){
		if(store){
    		store.get("language");
		}else if($.cookie){
			return $.cookie("language");
		}else{
			var lan = null;
			var arrStr = document.cookie.split("; ");
			for (var i = 0; i < arrStr.length; i++) {
			    var temp = arrStr[i].split("=");
			    if (temp[0] == 'language') {
			        lan = unescape(temp[1]);
			    }
			}
			return lan;
		}
	};
	
	$.i18n.browserLang = function(){
		return $.i18n.getLanguage() || navigator.language || navigator.userLanguage;
	};
	
	$.i18n.set = function(language,i18nMap){
		if(store){
    		store.set(language,i18nMap);
		}else{
			$.i18n.map[language] = i18nMap;
		}
	};
	
	$.i18n.get = function(language){
		var i18nMap = {};
		if(store){
			i18nMap = store.get(language);
		}else{
			i18nMap = $.i18n.map[language];
		}
		if(!i18nMap){
			return null;
		}
		return i18nMap;
	};
	
	$.i18n.remove = function(language,i18nKey){
		if(store){
			var i18nMap = store.get(language) || {};
			if(i18nKey){
				delete i18nMap[i18nKey];
				store.set(language,i18nMap);
			}else{
				store.remove(language);
			}
		}else{
			var i18nMap = $.i18n.map[language];
            if(i18nKey){
 				delete i18nMap[i18nKey];
 				$.i18n.map[language] = i18nMap;
 			}else{
 				delete $.i18n.map[language];
 			}                 
		}
	};
	
	$.i18n.clear = function(){
		if(store){
			store.clear();
		}else{
			$.i18n.map = {};
		}
	};
	
	/**
	 * 加载当前功能国际化资源,并对包含属性data-localize的元素进行国际化处理
	 * 如：<label for="" class="col-sm-5 control-label" data-localize="kkbm">开课部门</label>
	 */
	$.i18n.localize = function(settings){
		// set up settings
	    var defaults = {
	      languagePath 	: _path + "/xtgl/i18n_cxI18nLanguage.html",
	      i18nKey 		: [],
	      i18nPath		: _path + "/xtgl/i18n_cxI18nIndex.html",
	      //是否一直读取最新的国际化信息,测试阶段可设置为true
	      i18nLastest	: false,
	      //当前功能所属的系统模块，国际化代码会根据该参数选择不同模块的国际化配置信息
	      i18nModule	: "",
	      async			: false,
	      data			: {},
	      callback		: null
	    };
	    settings = $.extend(defaults, settings);
	    //
	    $.ajaxSetup({async:false});
		var pathArr = settings["languagePath"].split("?");
		if(pathArr.length > 1){
			pathArr.push("&");
		}else{
			pathArr.push("?");
		}
		pathArr.push("t=" + new Date().getTime() );
    	 //查询服务端当前会话的国际化语言
	    $.get(pathArr.join("") , function(language){
	    	//记录服务端当前会话的国际化语言
			$.i18n.setLanguage(language);
			//组织需要国际化的参数
		    var dataMap = {};
		    var i18nIndex = 0;
		    if(settings.i18nLastest == true){
		    	$.each(settings["i18nKey"], function(i, i18nKey){
					dataMap["list["+ i18nIndex + "].i18nKey"] = i18nKey;
					$.i18n.remove(language,i18nKey);
					i18nIndex ++ ;
				});
			}else{
				//当前语言对应的国际化缓存信息
				var i18nMap = $.i18n.get(language) || {};
				//过滤已经缓存过的参数
			    $.each(settings["i18nKey"], function(i, i18nKey){
			    	//仅对未缓存的进行国际化查询
			    	if(!i18nMap[i18nKey] && i18nKey !='rn'){
						dataMap["list["+ i18nIndex + "].i18nKey"] = i18nKey;
						i18nIndex ++ ;
					}
				});
			}
		    //存在需要国际化的信息
		    if(i18nIndex > 0){
		    	var pathArr = settings["i18nPath"].split("?");
	    		if(pathArr.length > 1){
	    			pathArr.push("&");
	    		}else{
	    			pathArr.push("?");
	    		}
	    		pathArr.push("t=" + new Date().getTime() );
		    	//Ajax获取国际化结果
				$.ajax({
					type	: "POST",
				    url		: pathArr.join(""),
				    async	: settings["async"],
				    cache	: false,
				    data	: $.extend( dataMap ,settings["data"] || {"module" : settings["i18nModule"] || ""}),
				    dataType: 'json',
				    success	: function (data, status) {
						if(data){
							//获取缓存的国际化信息
							var language = data["language"];
							var i18nArray = $.makeArray(data["i18n"]);
							//记录服务端当前会话的国际化语言
							$.i18n.setLanguage(language);
							//当前语言对应的国际化缓存信息
							var i18nMap = $.i18n.get(language) || {};
							//缓存国际化结果
							$.each(i18nArray || [], function(i, i18nModel){
								i18nMap[i18nModel["i18nKey"]] = i18nModel["i18nValue"];
							});
							//缓存国际化信息
							$.i18n.set(language,i18nMap);
							//处理默认国际化
							$("[data-localize]").each(function() {
						        var elem = $(this),localizedValue = i18nMap[elem.data("localize")];
						        if (elem.is("input[type=text]") || elem.is("input[type=password]") || elem.is("input[type=email]")) {
						        	elem.attr("placeholder", localizedValue);
						        } else if (elem.is("input[type=button]") || elem.is("input[type=submit]")) {
						        	elem.attr("value", localizedValue);
						        } else {
						        	elem.text(localizedValue);
						        }
					      	});
						}
						if($.isFunction(settings.callback)){
				      		settings.callback.call(this);
				      	}
				    },
				    error: function (jqXHR, textStatus, errorThrown) {
				        console.log('International failure .' );
				    }
				});
		    }else{
		    	if($.isFunction(settings.callback)){
		      		settings.callback.call(this);
		      	}
		    }
	    });
    	$.ajaxSetup({async:true});
	};
	
	// http://jqueryvalidation.org/jQuery.validator.format/
	$.i18n.format = function( source, params ) {
		if ( arguments.length === 1 ) {
			return function() {
				var args = $.makeArray(arguments);
				args.unshift(source);
				return $.i18n.format.apply( this, args );
			};
		}
		if ( arguments.length > 2 && params.constructor !== Array  ) {
			params = $.makeArray(arguments).slice(1);
		}
		if ( params.constructor !== Array ) {
			params = [ params ];
		}
		
		$.each(params, function( i, n ) {
			source = $.i18n.replacement(source).replace( new RegExp("\\{" + i + "\\}", "g"), function() {
				return n;
			});
		});
		return source;
	};
	
	/**
     * Tested with:
     *   test.t1=asdf ''{0}''
     *   test.t2=asdf '{0}' '{1}'{1}'zxcv
     *   test.t3=This is \"a quote" 'a''{0}''s'd{fgh{ij'
     *   test.t4="'''{'0}''" {0}{a}
     *   test.t5="'''{0}'''" {1}
     *   test.t6=a {1} b {0} c
     *   test.t7=a 'quoted \\ s\ttringy' \t\t x
     *
     * Produces:
     *   test.t1, p1 ==> asdf 'p1'
     *   test.t2, p1 ==> asdf {0} {1}{1}zxcv
     *   test.t3, p1 ==> This is "a quote" a'{0}'sd{fgh{ij
     *   test.t4, p1 ==> "'{0}'" p1{a}
     *   test.t5, p1 ==> "'{0}'" {1}
     *   test.t6, p1 ==> a {1} b p1 c
     *   test.t6, p1, p2 ==> a p2 b p1 c
     *   test.t6, p1, p2, p3 ==> a p2 b p1 c
     *   test.t7 ==> a quoted \ s	tringy 		 x
     */
	$.i18n.replacement = function(value){
		var i;
	    if (typeof(value) == 'string') {
	    	// Handle escape characters. Done separately from the tokenizing loop below because escape characters are
	    	// active in quoted strings.
	    	i = 0;
	    	while ((i = value.indexOf('\\', i)) != -1) {
		        if (value.charAt(i + 1) == 't'){
		          value = value.substring(0, i) + '\t' + value.substring((i++) + 2); // tab
		        }else if (value.charAt(i + 1) == 'r'){
		          value = value.substring(0, i) + '\r' + value.substring((i++) + 2); // return
		        }else if (value.charAt(i + 1) == 'n'){
		          value = value.substring(0, i) + '\n' + value.substring((i++) + 2); // line feed
		        }else if (value.charAt(i + 1) == 'f'){
		          value = value.substring(0, i) + '\f' + value.substring((i++) + 2); // form feed
		        }else if (value.charAt(i + 1) == '\\'){
		          value = value.substring(0, i) + '\\' + value.substring((i++) + 2); // \
		        }else{
		          value = value.substring(0, i) + value.substring(i + 1); // Quietly drop the character
		        }
	    	}
	    	// Lazily convert the string to a list of tokens.
	    	var arr = [], j, index;
	    	i = 0;
	    	while (i < value.length) {
	    		if (value.charAt(i) == '\'') {
	    			// Handle quotes
	    			if (i == value.length - 1){
	    				value = value.substring(0, i); // Silently drop the trailing quote
	    			}else if (value.charAt(i + 1) == '\''){
	    				value = value.substring(0, i) + value.substring(++i); // Escaped quote
	    			}else {
	    				// Quoted string
	    				j = i + 2;
	    				while ((j = value.indexOf('\'', j)) != -1) {
	    					if (j == value.length - 1 || value.charAt(j + 1) != '\'') {
	    						// Found start and end quotes. Remove them
	    						value = value.substring(0, i) + value.substring(i + 1, j) + value.substring(j + 1);
	    						i = j - 1;
	    						break;
	    					}
	    					else {
				                // Found a double quote, reduce to a single quote.
				                value = value.substring(0, j) + value.substring(++j);
	    					}
	    				}

	    				if (j == -1) {
	    					// There is no end quote. Drop the start quote
	    					value = value.substring(0, i) + value.substring(i + 1);
	    				}
	    			}
	    		} else{
	    			i++;
	    		}
	    	}
	    }
	    return value;
	}
	
	$.i18n.prop = function (i18nKey /* Add parameters as function arguments as necessary  */) {
		if (!i18nKey){
	    	return null;
	    }
		//当前语言对应的国际化缓存信息
		var i18nMap = $.i18n.get($.i18n.language);
		var i18nValue = i18nMap[i18nKey];
		if((i18nValue == undefined || i18nValue == null || typeof value == 'undefined' || i18nValue == 'undefined')){
			$.each(i18nMap || {}, function(key, value){
				if(i18nKey == key){
					i18nValue = value;
					return false;
				}
			});
		}
		if (!i18nValue){
	    	return null;
	    }
	    var params = [];
	    if (arguments.length == 2 && $.isArray(arguments[1])){
	    	// An array was passed as the only parameter, so assume it is the list of place holder values.
	    	params = arguments[1];
	    }else if (arguments.length >= 2 && !$.isArray(arguments[1])) {
 	        params = $.makeArray(arguments).slice(1);
 	    }
	    // Place holder replacement
	    i18nValue = $.i18n.replacement(i18nValue);
	    if (i18nValue.length == 0){
	    	return "";
	    }else{
	    	return $.i18n.format(i18nValue,params);
	    }
    };
	
	$.i18n.grid = function(options){
		if($.isPlainObject(options) && options["i18nable"] === true){
			var i18nKeyArr = ["needCondition","emptyRecord"];
			var colModel = options["colModel"];
			if(colModel){
				//{label:'',name:'kcdmbbb_id', index: 'kcdmbbb_id',hidden:true,key:true,align:'center'},
				$.each(colModel, function(i, item){
					if(!(item["hidden"] === true || item["key"] === true)){
						i18nKeyArr.push(item["index"] || item["name"]);
					}
				});
				//加载当前Grid列国际化资源
				$.i18n.localize({ 
			       i18nKey 		: i18nKeyArr,
			       i18nLastest	: !options.i18nLastest,
			       data			: {"i18nTarget": "JQGrid"},
			       callback 	: function(){
						//{label:'',name:'kcdmbbb_id', index: 'kcdmbbb_id',hidden:true,key:true,align:'center'},
						$.each(colModel, function(i, item){
							if(!(item["hidden"] === true || item["key"] === true)){
								var i18nKey		= item["index"] || item["name"];
								//对grid列的label值进行国际化
								 console.log(i18nKey + ' : ' +  $.i18n.prop(i18nKey) );
								item["label"] = $.i18n.prop(i18nKey);
							}
						});
				   }
				});
			}
		}
		return options;
	};
	
}(jQuery));