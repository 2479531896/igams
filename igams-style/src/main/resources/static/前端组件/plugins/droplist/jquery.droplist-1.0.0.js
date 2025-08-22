/*
 * @discretion	: default messages for the jQuery droplist plugin.
 * @author    	: wandalong 
 * @version		: v1.0.2
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
	
	/*====================== DropList CLASS DEFINITION ====================== */
	
	$.bootui.widget.DropList = function(element,options){
		options.beforeRender.call(this,element);	//渲染前的函数回调
		try {
			this.initialize.call(this, element, options);
		} catch (e) {
			options.errorRender.call(this,e);
		}
		options.afterRender.call(this,element);	/*渲染后的函数回调*/
	};
	
	$.bootui.widget.DropList.prototype = {
		constructor: $.bootui.widget.DropList,
		/*初始化组件参数*/
		initialize 	: function(element, options) {
			//定义变量
			var $this = this;
			$this.loadAll = false;
			$this.prevLetter = '';
			$this.counts = {};
        	$this.allCount = 0;
			var isAll = true,firstClick = false;
			var container = $.founded(options.rangeSelector)?$(options.rangeSelector):element;
			if(options.mapper){
				options.mapper["pinyin"] = options.mapper["pinyin"]||"pinyin";
			}
			
			// Add the appropriate letter class to the current container
			function addLetterClass(firstChar, $el, isPrefix){
				if ( /\W/.test(firstChar) ) {
                    firstChar = '-'; // not A-Z, a-z or 0-9, so considered "other"
                }
                if ( !isNaN(firstChar) ) {
                    firstChar = '_'; // use '_' if the first char is a number
                }
                $el.addClass('ln-' + firstChar);
                if ( $this.counts[firstChar] === undefined ) {
                	$this.counts[firstChar] = 0;
                }
                $this.counts[firstChar]+=1;
                if (!isPrefix) {
                	$this.allCount+=1;
                }
			}
			
			function getLetterCount(el) {
                if ($(el).hasClass('all')) {
                    return $this.allCount;
                } else {
                    var count = $this.counts[$(el).attr('class').split(' ')[0]];
                    return (count !== undefined) ? count : 0; // some letters may not have a count in the hash
                }
            }
			
			// adds a class to each LI that has text content inside of it (ie, inside an <a>, a <div>, nested DOM nodes, etc)
            function changeClasses() {
            	$this.counts = {};
            	$this.allCount = 0;
                var str, spl, $this_item,
                    firstChar = '',
                    hasFilterSelector = $.founded(options.filterSelector);
                // Iterate over the list and set a class on each one and use that to filter by
                $this.$list.each(function (i,item) {
                	$this_item = $(item);
                    // I'm assuming you didn't choose a filterSelector, hopefully saving some cycles
                    if ( !hasFilterSelector ) {
                        //Grab the first text content of the LI, we'll use this to filter by
                        str = $.trim($this_item.text()).toLowerCase();
                    } else {
                        // You set a filterSelector so lets find it and use that to search by instead
                        str = $.trim($this_item.find(options.filterSelector).text()).toLowerCase();
                    }
                    
                    // This will run only if there is something to filter by, skipping over images and non-filterable content.
                    if (str !== '') {
                        // Find the first letter in the LI
                        firstChar = str.charAt(0);
                        // Doesn't send true to function, which will ++ the All count on prefixed items
                        addLetterClass.call($this,firstChar, $this_item);
                    }
                });
                $this.counts["matched"] = options.items.length;
                $.each(options.letters,function(i,letter){
                	var count = $this.counts[letter];
                	if ( !($.founded(count)) || ($.founded(count) && Number(count) == 0) ) {
                        $('.' + options.letters[i], $this.$letters).addClass('ln-disabled');
                    }else{
                    	 $('.' + options.letters[i], $this.$letters).removeClass('ln-disabled');
                    }
                });
            }
			
            function changeMatchedClasses() {
            	var count = options.items.length;
                $this.counts["matched"] = count;
            	if ( Number(count) == 0 ) {
            		$('li.matched', $this.$letters).addClass('ln-disabled');
                }else{
                	$('li.matched', $this.$letters).removeClass('ln-disabled');
                }
            }
            
			/*构建一个内置元素对象*/
			function buildItem(itemObj,mapper){
				if(!$.founded(itemObj[mapper.key])){
					return null;
				}
				var indexStr = (options.index||"")+"_"+itemObj[mapper.key];
				var pinyin = itemObj[mapper.pinyin];
				var key = itemObj[mapper.key];
				var text = itemObj[mapper.text];
				var checked = itemObj.checked || false;
				return $.extend({},itemObj,{"index":indexStr,"pinyin":pinyin,"checked":checked,"key":key,"text":text});
			}
			
			function bindItemEvent(li_item,rowObj,type){
				//绑定选择取消文字显示状态
				var isInputClick = false;
				$(li_item).find("label").off(options.clickEventType).on(options.clickEventType, function (e) {
					e.preventDefault();
				});
				
				function th_td_click(e){
					e.stopPropagation();
					var checkbox = $(this).find(":checkbox:eq(0)");
					if(!checkbox.prop("disabled")){
						var table = $(this).data("table");
						var list_item = $this.$list.filter("li[index="+rowObj.index+"]");
			 			if(checkbox.prop("checked")){
			 					
			 				//选中>>取消选中
			 				checkbox.prop("checked",false);
			 				checkbox.next("span").removeClass("highlight");
							$(li_item).removeClass("checked");
							$(list_item).find(":checkbox:gt(0)").prop("checked",false);
							$(list_item).find(".highlight").removeClass("highlight");
							
							rowObj[table] = [];
							//移除选中明细
							$this.removeCheckedItem(rowObj);
								
						}else{
							//未选中>>选中
							checkbox.prop("checked",true);
							checkbox.next("span").addClass("highlight");
							
							$(list_item).find(":checkbox:gt(0)").prop("checked",true);
							$(list_item).find(":checkbox:gt(0)").next("span").addClass("highlight");
							$(li_item).addClass("checked");
							
							rowObj[table] = [];
							$(list_item).find(":checkbox:gt(0)").each(function(){
								//获得值,text,索引
								var val = $(this).val(),txt = $(this).attr("text"); 
								rowObj[table].push({"key":val,"text":txt});
							});
							//添加选中明细
							$this.addCheckedItem(rowObj);
						}
						//调用样式和数量处理行数
		                changeClasses();
		                options.onSelectItem.call($(this),rowObj,checkbox.checked);
					}
				}
				
				//查找所有的checkbox
				$(li_item).find("th:eq(0)").off(options.clickEventType).on(options.clickEventType, function (e) {
					th_td_click.call(this, e);
				});
				
				if($(li_item).find("th").size() == 0 && $(li_item).find("td").size()==1){
					$(li_item).find("td:eq(0)").off(options.clickEventType).on(options.clickEventType, function (e) {
						th_td_click.call(this, e);
					});
				}

				//查找所有的checkbox
				$(li_item).find(":checkbox:eq(0)").off(options.clickEventType).on(options.clickEventType, function (e) {
					//不阻止默认行为，会自己选中，以下判断状态已是改变后的不能与li的点击事件相同
					e.stopPropagation();
					if(!$(this).prop("disabled")){
						var table = $(this).data("table");
						var checked = $(this).prop("checked");
						var list_item = $this.$list.filter("li[index="+rowObj.index+"]");
						//未选中>>选中
						if($(this).prop("checked")){
							$(this).next("span").addClass("highlight");
							$(list_item).find(":checkbox:gt(0)").prop("checked",true);
							$(list_item).find("label:gt(0)").each(function(){
								$(this).find("span").addClass("highlight");
							});
							$(li_item).addClass("checked");
							//添加选中明细
							rowObj[table] = [];
							$(list_item).find(":checkbox:gt(0)").each(function(){
								//获得值,text,索引
								var val = $(this).val(),txt = $(this).attr("text"); 
								rowObj[table].push({"key":val,"text":txt});
							});
							$this.addCheckedItem(rowObj);
						}else{
							//选中>>取消选中
							$(this).next("span").removeClass("highlight");
							$(li_item).removeClass("checked");
							$(list_item).find(":checkbox:gt(0)").prop("checked",false);
							$(list_item).find(".highlight").removeClass("highlight");
							
							rowObj[table] = [];
							//移除选中明细
							$this.removeCheckedItem(rowObj);
							
						}
						//调用样式和数量处理行数
		                changeClasses();
		                options.onSelectItem.call($(this),rowObj,checked);
					}
				});
				
				//有属性的情况下
					
				//查找所有的li_item
				$(li_item).find("label:gt(0)").off(options.clickEventType).on(options.clickEventType, function (e) {
					//不阻止默认行为，会自己选中，以下判断状态已是改变后的不能与li的点击事件相同
					e.stopPropagation();
					var checkbox = $(this).find(":checkbox:eq(0)");
					if(!$(checkbox).prop("disabled")){
						var table = $(checkbox).data("table");
						var checked = $(checkbox).prop("checked");
						//获得值,text,索引
						var val = $(checkbox).val(),txt = $(checkbox).attr("text"),index = $(this).parent().index(); 
						var list_item = $this.$list.filter("li[index="+rowObj.index+"]");
						//未选中>>选中
						if($(checkbox).prop("checked")){
							//选中年级的同时选中部门
							$(li_item).find(":checkbox:eq(0)").prop("checked",true);
							$(list_item).find(":checkbox:eq(0)").next("span").addClass("highlight");
							$(checkbox).next("span").addClass("highlight");
							
							if(!$.founded(rowObj[table])){
								rowObj[table] = [];
								rowObj[table][index] = {"key":val,"text":txt};
							}else{
								rowObj[table].splice(index,0,{"key":val,"text":txt});
							}
							
							if(!$(li_item).hasClass("checked")){
								$(li_item).addClass("checked");
								//添加选中明细
								$this.addCheckedItem(rowObj);
							}
						}else{
							//选中>>取消选中
							$(checkbox).next("span").removeClass("highlight");
							$.each(rowObj[table]||[],function(i,item){
								if($.founded(item) && val == item.key){
									rowObj[table].splice(i,1);
								}
							});
							
							/*if ($(li_item).find("input:checked:gt(0)").size() == 0 ){
								$(li_item).removeClass("checked");
								//移除选中明细
								$this.removeCheckedItem(rowObj);
								$(list_item).find(":checkbox:eq(0)").prop("checked",false);
								$(list_item).find(":checkbox:eq(0)").next("span").removeClass("highlight");
							}*/
						}
						//调用样式和数量处理行数
		                changeClasses();
		                options.onSelectItem.call($(this),rowObj,checked);
					}
				});
				
				//查找所有的checkbox
				$(li_item).find(":checkbox:gt(0)").off(options.clickEventType).on(options.clickEventType, function (e) {
					//不阻止默认行为，会自己选中，以下判断状态已是改变后的不能与li的点击事件相同
					e.stopPropagation();
					if(!$(this).prop("disabled")){
						var table = $(this).data("table");
						var checked = $(this).prop("checked");
						//获得值,text,索引
						var val = $(this).val(),txt = $(this).attr("text"),index = $(this).parent().index(); 
						var list_item = $this.$list.filter("li[index="+rowObj.index+"]");
						//未选中>>选中
						if($(this).prop("checked")){
							//选中年级的同时选中部门
							$(li_item).find(":checkbox:eq(0)").prop("checked",true);
							$(list_item).find(":checkbox:eq(0)").next("span").addClass("highlight");
							$(this).next("span").addClass("highlight");
							
							if(!$.founded(rowObj[table])){
								rowObj[table] = [];
								rowObj[table][index] = {"key":val,"text":txt};
							}else{
								rowObj[table].splice(index,0,{"key":val,"text":txt});
							}
							
							if(!$(li_item).hasClass("checked")){
								$(li_item).addClass("checked");
								//添加选中明细
								$this.addCheckedItem(rowObj);
							}
						}else{
							//选中>>取消选中
							$(this).next("span").removeClass("highlight");
							$.each(rowObj[table]||[],function(i,item){
								if($.founded(item) && val == item.key){
									rowObj[table].splice(i,1);
								}
							});
							
							/*if ($(li_item).find("input:checked:gt(0)").size() == 0 ){
								$(li_item).removeClass("checked");
								//移除选中明细
								$this.removeCheckedItem(rowObj);
								$(list_item).find(":checkbox:eq(0)").prop("checked",false);
								$(list_item).find(":checkbox:eq(0)").next("span").removeClass("highlight");
							}*/
						}
						//调用样式和数量处理行数
		                changeClasses();
		                options.onSelectItem.call($(this),rowObj,checked);
					}
				});
			}
			
			function buildItemHtml(i,itemObj){
				//如果formatter属性存在，且是函数，则获得此func,否则设置默认函数
				var formatter = ($.founded(options.formatter)&&$.isFunction(options.formatter))?options.formatter:function(index,rowObj,_options){
					var $that = this,mapper = _options.mapper;
					var pinyin = rowObj.pinyin,
						key = rowObj.key,
						text = rowObj.text,
						zddm = mapper.zddm,
						table = mapper.table;
					
					var html = [];
					//是否选中str
			    	var checkedStr = "";
			    	if(rowObj.checked==true||$this.hasCheckedItem(rowObj)){
			    		checkedStr = ' checked="checked" ';
			    	}
			    	html.push('<p class="selector-name">'+pinyin+'</p>');
					html.push('<table><tr>')
					html.push("<td valign='middle' "+($.founded(options.tdClass) ? " class='"+options.tdClass+"'" : "")+">");
					html.push("<label class='inline'><input data-table='"+ (table || "")+"'  name='"+mapper.key+"' value='"+key+"' text='"+text+"' type='checkbox' "+checkedStr+"/>&nbsp;");
					if(rowObj.checked==true||$this.hasCheckedItem(rowObj)){
						html.push('<span class="highlight">');
			    	}else{
			    		html.push("<span>");
			    	}
					html.push(text+"</span></label></td>");
					html.push('</tr></table>');
					return html.join("");
				};
				//组织html
				var item_html = [];
				var indexStr = ((itemObj.type == 1)? ' index="'+itemObj.index+'" ' :"");
				//生成当前元素标签
				var parentDataStr = [];
				$.each(itemObj.parents||[],function(i,parent_item){
					var parent_id_name = parent_item["key"];
					//判断当前父级ID是否在其属性中,且值是存在的
					if($.founded(itemObj.attrs[parent_id_name])){
						parentDataStr.push(' data-'+parent_id_name +'="'+itemObj.attrs[parent_id_name]+'" ');
					}
				});
				parentDataStr.push(' data-table="'+(options.mapper.table || "")+'" ');
				item_html.push('<li '+ parentDataStr.join(" ") + indexStr +($.founded(options.itemClass) ? (" class='" + options.itemClass + "'") : "" )+'>');
				item_html.push(formatter.call($this,i,itemObj,options));
				item_html.push("</li>");
				return item_html.join("");
			}
			
			function loadRemoteData(options){
				if($.founded(options.href)&&$.founded(options.mapper)&&options.beforeRequest.call($this,$this.letter)){
					$(container).empty().html(options.loadingHtml);
					$.ajaxSetup({async:false});
					//远程获取数据
					var paramMap = $.extend({},options.postData||{},{"letter_text":options.mapper.text});
					$.post(options.href,paramMap, function(data){
						$(container).empty();
						options.data = data;
						if($.founded(data)){
							//组织html
							var html = [];
							html.push('<ul class="item-droplist">');
							$.each(data,function(i,rowObj){
								var tmpObj = buildItem(rowObj,options.mapper);
								if($.founded(tmpObj)){
									tmpObj.type = 1;
									//组织每个元素html
									html.push(buildItemHtml.call($this,i,tmpObj));
								}
							});
							html.push("</ul>");
							html.push('<ul class="matched-droplist"></ul>');
							//将拼装后的html放置在数据范围区域显示
							$(container).append(html.join(""));
						}
						$(container).append('<div class="ln-no-match hide">' + options.noMatchText + '</div>');
						if($(container).find("ul.item-droplist li").size()==0){
							$(container).find(".ln-no-match").removeClass("hide").addClass("show");
						}
						//获得每个元素
                    	$this.$list = $(container).find("ul.item-droplist li");
                    	
						//绑定选择事件
						$(container).find("ul.item-droplist li").each(function(i,li_item){
							$(li_item).css(options.mapper.style||{});
							var tmpObj = buildItem(data[i],options.mapper);
							if($.founded(tmpObj)){
								bindItemEvent.call($this,li_item,tmpObj,1);
							}
             			});
						
        				$this.$list.addClass("hide").removeClass("show");
        				//重新调用样式和数量处理行数:默认全部隐藏
        				changeClasses();
        				options.afterRequest.call($this,data,$this.letter)
					}, "json");
                    $.ajaxSetup({async:true});
				}
			}

            /*添加需要暴露给开发者的函数*/
			$.extend(true,$this,{
				/*移除options.items中的元素*/
				removeCheckedItem:function(rowObj){
					$.each(options.items||[],function(i,itemObj){
						if(rowObj.index==itemObj.index){
							rowObj.checked = false;
							options.items.splice(i, 1);
							changeMatchedClasses();
							return false;
						}
					});
				},
				/*向对象options.items数组添加一个对象*/
				addCheckedItem:function(itemObj){
					//循环当前itemObject的items数组判断是否添加的元素已经存在
					if(!$this.hasCheckedItem(itemObj)){
						itemObj.checked = true;
						options.items.push(itemObj);
						changeMatchedClasses();
					}
				},
				  /*判断对象数组options.items中是否存在itemObj对象*/
				hasCheckedItem:function(itemObj){
					var ishave = false;
					$.each(options.items||[],function(i,item){
						if(item.index==itemObj.index){
							ishave = true;
							return false;
						}else{
							ishave = false;
						}
					});
					return ishave;
				},
				setOptions	: function(opts){
					$.each(opts||{},function(key,val){
						delete options[key];
					});
					$.extend(options,opts||{});
				},
				reload:function(paramMap,opts){
					$this.setOptions(opts||{});
					//扩展参数
					$this.reloadData = true;
					//删除原有参数
					$.each(paramMap||{}, function (k, v) { 
			            delete options.postData[k];
			        });
					$.extend(options.postData,paramMap||{});
					//触发当前字母的点击事件;进行重新加载数据
					$('.' + $this.letter , $this.$letters).trigger(options.clickEventType);
				},
				resetSelection:function(key){
					$.each(options.data,function(i,itemObj){
						var tmpObj = buildItem(itemObj,options.mapper);
						if($.founded(tmpObj)){
							if($.founded(key)&&tmpObj.key==key){
								$this.removeCheckedItem(tmpObj);
								var list_item = $this.$list.filter("li[index="+tmpObj.index+"]");
								$(list_item).find(":checkbox:eq(0)")[0].checked  = false;
								$(list_item).find("span").removeClass("highlight");
								return false;
							}else{
								$this.removeCheckedItem(tmpObj);
								var list_item = $this.$list.filter("li[index="+tmpObj.index+"]");
								$(list_item).find(":checkbox:eq(0)")[0].checked  = false;
								$(list_item).find("span").removeClass("highlight");
							}
						}
					});
				},
				setSelection:function(key){
					$.each(options.data,function(i,itemObj){
						var tmpObj = buildItem(itemObj,options.mapper);
						if($.founded(tmpObj)&&tmpObj.key==key){
							$this.addCheckedItem(tmpObj);
							var list_item = $this.$list.filter("li[index="+tmpObj.index+"]");
							$(list_item).find(":checkbox:eq(0)")[0].checked  = true;
							$(list_item).find("span").addClass("highlight");
							return false;
						}
					});
				},
				getChecked:function(){
					return options.items;
				}
			});
			
			if(!$(container).hasClass("droplist-content")){
				$(container).addClass("droplist-content");
			}
			//移除上次绑定的元素，并添加样式
			$(container).prev("div.droplist").remove();
			$(container).find(".ln-no-match").remove();
			//获取cookie中的数据
			if ( $.cookie && (options.cookieName !== null) ) {
                var cookieLetter = $.cookie(options.cookieName);
                if ( cookieLetter !== null ) {
                	options.initLetter = cookieLetter;
                }
            }
			
			//======================添加组件元素============================================
			
			$(container).before('<div class="droplist"/>');
			$(container).append('<div class="ln-no-match hide">' + options.noMatchText + '</div>');
			
			var $wrapper =  $(container).prev('.droplist');
            /*将生成的html添加到页面*/
			var context = new Array();
			context.push('<div class="ln-letters"><ul>');
            for (var i = 1; i < options.letters.length; i++) {
                if (i === 1) {
                	context.push('<li class="all" href="#">'+ options.allText + '</li><li class="_" href="#">0-9</li>');
                }
                var text = ((options.letters[i] === '-') ? options.otherText : ((options.letters[i] === 'matched') ? options.matchText : options.letters[i].toUpperCase()) );
                context.push('<li class="' + options.letters[i] + '" href="#">' + text + '</li>');
            }
            context.push('</ul></div>' + ((options.showCounts) ? '<div class="ln-letter-count hide">0</div>' : ''));
            // Remove inline styles, replace with css class
            // Element will be repositioned when made visible
			$wrapper.empty().append(context.join(""));
            //取得当前元素前面的a-z区域
			$this.$letters = $('.ln-letters', $wrapper).slice(0, 1);
			//取得当前元素显示总数的div
            if (options.showCounts ) {
            	$this.$letterCount = $('.ln-letter-count', $wrapper).slice(0, 1);
            }
           
            //====================处理组件元素===================================================
            // remove nav items we don't need
            if ( !options.includeAll ) {
                $('.all', $this.$letters).remove();
            }
            if ( !options.includeNums ) {
                $('._', $this.$letters).remove();
            }
            if ( !options.includeOther ) {
                $('.-', $this.$letters).remove();
            }
            if ( !options.incudeMatch ) {
                $('.matched', $this.$letters).remove();
            }
            $(':last', $this.$letters).addClass('ln-last');
            
            $(container).show();
            
            //======================添加数据元素============================================
            //程数据模式
            if($.founded(options.href)&&$.founded(options.mapper)){
				//初次加载，什么不做
            }else{
            	//本地数据模式
            	$this.$list = $(container).find("ul.item-droplist li");
                //调用样式和数量处理行数
                changeClasses();
            }
            
            //=======================绑定事件===========================================
            
            
            
            // click handler for letters: shows/hides relevant LI's
            $('li', $this.$letters).off(options.clickEventType).on(options.clickEventType, function (e) {
            	e.preventDefault(); 
            	
            	var $this_a = $(this),
                 	noMatches = $(container).find('.ln-no-match');
             		$this.letter = $this_a.attr('class').split(' ')[0];
             	//远程数据模式	
             	if($.founded(options.href)&&$.founded(options.mapper)){
    				//未加载过全部数据
             		//alert("options.ajaxAtOnce:" + options.ajaxAtOnce);
                	if(($this.loadAll == false && $this.letter != 'matched' && $this.letter != '-')	|| $this.reloadData == true){
                		//加载点击字符对应的数据；构建内容
                    	options.postData["letter"] = $this.letter;
                    	if(options.ajaxAtOnce == true){
                    		loadRemoteData.call($this,options);
                    		$this.reloadData = false;
                    		
                    		 if ( $this.letter === 'all' ) {
                             	//设置加载过全部数据标识
                             	$this.loadAll = true;
                    		 }
            			}else{
            				$this.$list = $(container).find("ul.item-droplist li");
            				//重新调用样式和数量处理行数:默认全部隐藏
            				changeClasses();
            			}
                	}
            	}
             	
             	//获得上次选中和本次选中的元素
             	var prevList = $this.$list.filter('.ln-' + $this.prevLetter);
             	var thisList = $this.$list.filter('.ln-' + $this.letter);
             	var droplistList = $(container).find("ul.item-droplist");
             	var matchedList	= $(container).find("ul.matched-droplist");
             	
             	function matchedClick(e){
                	//点击【已选】选项
                	$(droplistList).hide();
                	if(options.items.length==0){
    		    		noMatches.addClass("show").removeClass("hide");
    		    	}else{
    		    		noMatches.addClass("hide").removeClass("show");
    		    		$(matchedList).empty();
    		    		//循环已选元素对象
             			$.each(options.items||[],function(j,itemObj){
             				itemObj.checked = true;
             				itemObj.type = 2;
         					//组织选中却不存在于当前条件的元素html
         					var selectElement = buildItemHtml.call($this,j,itemObj);
         					$(matchedList).append(selectElement);
         					$(selectElement).addClass("show").removeClass("hide");
             			});
             			//绑定选择事件
             			$(container).find("ul.matched-droplist li").each(function(i,li_item){
             				$(li_item).css(options.mapper.style||{});
             				//bindItemEvent.call($this,li_item,options.items[i],2);
             			});
             			//显示已选元素
             			$(matchedList).show();
    		    	}
                }
             	
            	//=================相同处理逻辑区域=====================================
                //判断是否点击新的字符
                if ( $this.prevLetter !== $this.letter  ) {
                	// Only to run this once for each click, won't double up if they clicked the same $this.letter
                	// Won't hinder firstRun
                    $('li.ln-selected', $this.$letters).removeClass('ln-selected');
                    //隐藏所有元素
                    $this.$list.addClass("hide").removeClass("show");
                    //删除选中区域的元素
                    $(matchedList).hide();
                    $(matchedList).empty();
                    
                    //所有【全部】选项
                    if ( $this.letter === 'all' ) {
                    	//设置加载过全部数据标识
                    	$this.loadAll = true;
                    	$this.$list.addClass("show").removeClass("hide"); // Show ALL
                        noMatches.addClass("hide").removeClass("show"); // Hide the list item for no matches
                        isAll = true; // set this to quickly check later
                        $(droplistList).show();
                    } else if ( $this.letter === 'matched' ) {
                    	matchedClick(e);
                    }else {
                    	//隐藏上一次匹配的元素
                        if ( isAll ) {
                        	// 如果上次点击的是全部
                            // since you clicked ALL last time:
                        	$this.$list.addClass("hide").removeClass("show");
                            isAll = false;
                        } else if ($this.prevLetter !== '') {
                        	$this.$list.filter('.ln-' + $this.prevLetter).addClass("hide").removeClass("show");
                        }
                        //显示本次匹配的元素
                        $this.$list.filter('.ln-' + $this.letter).addClass("show").removeClass("hide");
                        var count = getLetterCount(this);
                        if (count > 0) {
                            noMatches.addClass("hide").removeClass("show"); // in case it's showing
                        } else {
                            noMatches.addClass("show").removeClass("hide");
                        }
                        $(droplistList).show();
                    }
                    //设置上次点击字母
                    $this.prevLetter = $this.letter;
                    if ($.cookie && (options.cookieName !== null)) {
                        $.cookie(options.cookieName, $this.letter, {
                            expires: 999
                        });
                    }
                    //添加当前字母选中效果
                    $this_a.addClass('ln-selected');
                    $this_a.blur();
                    // end if prevLetter !== $this.letter
                }else{
                	//所有【全部】选项
                    if ( $this.letter === 'all' ) {
                    	//设置加载过全部数据标识
                    	$this.loadAll = true;
                    	$this.$list.addClass("show").removeClass("hide"); // Show ALL
                        noMatches.addClass("hide").removeClass("show"); // Hide the list item for no matches
                        isAll = true; // set this to quickly check later
                    } else if ( $this.letter === 'matched' ) {
                    	matchedClick(e);
                    }else {
                    	//隐藏上一次匹配的元素
                        if ( isAll ) {
                        	// 如果上次点击的是全部
                            // since you clicked ALL last time:
                        	$this.$list.addClass("hide").removeClass("show");
                            isAll = false;
                        } else if ($this.prevLetter !== '') {
                        	$this.$list.filter('.ln-' + $this.prevLetter).addClass("hide").removeClass("show");
                        }
                        //显示本次匹配的元素
                        $this.$list.filter('.ln-' + $this.letter).addClass("show").removeClass("hide");
                        var count = getLetterCount(this);
                        if (count > 0) {
                            noMatches.addClass("hide").removeClass("show"); // in case it's showing
                        } else {
                            noMatches.addClass("show").removeClass("hide");
                        }
                    }
                } 
                
                if ($.founded(options.onLetterClick)) {
                	options.onLetterClick.call($this,$this.letter);
                }
                firstClick = false; //return false;
            }); // end click()
            
            if ( options.includeAll ) {
            	
            }
            
           
        	
            //=======================决定先显示的内容：触发事件===========================================
            // 如果有默认字母  initLetter ;先显示默认的字母
            if ( options.initLetter !== '' ) {
                firstClick = true;
                // click the initLetter if there was one
                $('.' + options.initLetter.toLowerCase(), $this.$letters).slice(0, 1).trigger(options.clickEventType);
            } else {
            	
            	
                // 如果有默认字母；但是设置了显示全部；则显示全部的元素
                if ( options.includeAll ) {
                    // 调用【全部】选项的click事件,实际上不真正的点击它
                    $('.all', $this.$letters).addClass('ln-selected');
                    //加载远程数据模式
                    if($.founded(options.href)&&$.founded(options.mapper)){
                    	//设置加载过全部数据标识
                    	$this.loadAll = true;
                     	//加载点击字符对应的数据；构建内容
                     	options.postData["letter"] = "";
         				loadRemoteData.call($this,options);
                    }
                } else {
                	//未设置显示全部，则显示第一个有对应内容的字母
                    for ( var i = ((options.includeNums) ? 0 : 1); i < options.letters.length; i+=1) {
                    	//远程数据模式	
                     	if($.founded(options.href)&&$.founded(options.mapper)){
                     		firstClick = true;
                            $('.' + options.letters[i], $this.$letters).slice(0, 1).trigger(options.clickEventType);
                            break;
                     	}else{
                     		 if ( $this.counts[options.letters[i]] > 0 ) {
                                 firstClick = true;
                                 $('.' + options.letters[i], $this.$letters).slice(0, 1).trigger(options.clickEventType);
                                 break;
                             }
                     	}
                    }
                }
            }
            
            //如果显示记录数
            if (options.showCounts) {
                // sets the top position of the count div in case something above it on the page has resized
            	$wrapper.mouseover(function () {
                	// we're going to need to subtract this from the top value of the wrapper to accomodate changes in font-size in CSS.
                    var letterCountHeight = $this.$letterCount.outerHeight();
                    $this.$letterCount.css({
                        top: $('li:first', $wrapper).slice(0, 1).position().top - letterCountHeight
                        // we're going to grab the first anchor in the list
                        // We can no longer guarantee that a specific letter will be present
                    });
                });
                
                //shows the count above the letter
                $('.ln-letters li', $wrapper).unbind("mouseover").mouseover(function (e) {
                	var $thisEl = $(this);
                    var left = $(this).position().left,
                        width = ($(this).outerWidth()) + 'px',
                        letter = $thisEl.attr('class').split(' ')[0];
                    var count = getLetterCount(this);
                	$this.$letterCount.css({
                        left: left,
                        width: width
                    }).text(count).addClass("show").removeClass("hide"); // set left position and width of letter count, set count text and show it
                }).unbind("mouseout").mouseout(function () { // mouseout for each letter: hide the count
                	$this.$letterCount.addClass("hide").removeClass("show");
                });
            }
            
		},
		setDefaults	: function(options){
			$.extend($.fn.droplist.defaults, options );
		}
	}
	
	/* DropList PLUGIN DEFINITION  */
	
	$.fn.droplist = function(option){
		var selector = $(this).selector;
		//处理后的参数
		var args = $.grep( arguments || [], function(n,i){
			return i >= 1;
		});
		return this.each(function () {
			var $this = $(this), data = $this.data('droplist');
			if (!data && option == 'destroy') {return;}
			if (!data){
				var options = $.extend(true , {}, $.fn.droplist.defaults, $this.data(), ((typeof option == 'object' && option) ? option : {}),{"selector":selector});
				$this.data('droplist', (data = new $.bootui.widget.DropList(this, options)));
			}
			if (typeof option == 'string'){
				//调用函数
				data[option].apply(data, [].concat(args || []) );
			}
		});
	};
	
	$.fn.droplist.defaults = {
		/*版本号*/
		version:'1.0.0',
		/*回调函数*/
		/*组件进行渲染前的回调函数：如重新加载远程数据并合并到本地数据*/
		beforeRender: $.noop,
		/*组件渲染出错后的回调函数*/
		errorRender: $.noop,
		/*组件渲染完成后的回调函数*/
		afterRender: $.noop, 
		/*加载远程数据前的回调函数：返回true则继续调用远程数据查询*/
		beforeRequest:function(letter){return true; },
		/*数据加载完后的回调函数*/
		afterRequest:function(data,letter){},
		/*字母被点击的回调函数*/
		onLetterClick: function(letter){},
		/*元素项被选择的回调函数*/
		onSelectItem: function(rowObj,status){},
		rangeSelector:null,
		/*数据请求参数*/
		href:"",
		postData:{},
		/*逻辑判断值*/
		switches:{
			
		},
		/*数据范围同步回调函数*/
		onDataRangeSync:function(){
			
		},
		/*JSON数据处理参数*/
		mapper:null,//{"pinyin":"pinyin","key":,"text":,}
		formatter:null,
		itemClass:"",
		/*组件参数*/
		loadingHtml:'<p class="text-center header smaller lighter loading"><i class="icon-spinner icon-spin orange  bigger-300"></i></br> 数据正在载入数据中....</p>',
		index:"droplist",
		/*是否立刻加载数据*/
		ajaxAtOnce: true,
		/*是否显示【全部】选项*/
        includeAll: true,
        allText:'全部',
        /*是否显示【0-9】选项*/
        includeNums: true,
        /*是否显示【其他】选项*/
        incudeOther: false,
        otherText:"其他",
        /*是否显示【已选】选项*/
        incudeMatch: true,
        matchText: "已选",
        /*默认的选项*/
        initLetter: 'all',
	    filterSelector: '.selector-name',//用于排序的引用
        noMatchText: '没有数据...',
        showCounts: true,
        cookieName: null,
        letters : ['_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '-','matched'],
	    firstClick : false,
	    items:[],
        //detect if you are on a touch device easily.
        clickEventType:((document.ontouchstart!==null)?'click':'touchstart')
	};

	$.fn.droplist.Constructor = $.bootui.widget.DropList;
	
	

}(jQuery));