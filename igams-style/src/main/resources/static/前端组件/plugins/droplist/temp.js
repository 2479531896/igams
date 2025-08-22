
			function loadRemoteData(options){
				if($.founded(options.href)&&$.founded(options.mapper)&&options.beforeRequest.call($this,$this.letter)){
					$(container).empty().html(loadingHtml);
					jQuery.ajaxSetup({async:false});
					//远程获取数据
					var paramMap = $.extend({},options.postData||{},{"letter_text":options.mapper.text});
					$.getJSON(options.href,paramMap, function(data){
						$(container).empty();
						options.data = data;
						if($.founded(data)){
							//组织html
							var html = [];
							html.push('<ul class="droplist">');
							$.each(data,function(i,rowObj){
								var tmpObj = buildItem(rowObj,options.mapper);
								//组织每个元素html
								html.push(buildItemHtml.call(i,$this,tmpObj));
							});
							html.push("</ul>");
							html.push('<ul class="droplist-matched"></ul>');
							//将拼装后的html放置在数据范围区域显示
							$(container).append(html.join(""));
						}
						$(container).append('<div class="ln-no-match hide">' + options.noMatchText + '</div>');
						if($(container).find("ul.droplist li").size()==0){
							$(container).find(".ln-no-match").show();
						}
						//绑定选择事件
						$(container).find("ul.droplist li").each(function(i,li_item){
							$(li_item).css(options.mapper.style||{});
							var tmpObj = buildItem(data[i],options.mapper);
             				bindItemEvent.call($this,li_item,tmpObj,1);
             			});
						//获得每个元素
                    	$this.$list = $(container).find("ul.droplist li");
        				$this.$list.addClass("hide").removeClass("show");
        				//重新调用样式和数量处理行数:默认全部隐藏
        				changeClasses();
        				options.afterRequest.call($this,data,$this.letter)
					});
                    jQuery.ajaxSetup({async:true});
				}
			}
			
			
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
            	$this.$list = $(container).find("ul.droplist li");
                //调用样式和数量处理行数
                changeClasses();
            }
            
            //=======================绑定事件===========================================
            
            // click handler for letters: shows/hides relevant LI's
            $('li', $this.$letters).unbind(options.clickEventType).bind(options.clickEventType, function (e) {
            	e.preventDefault(); 
            	
            	var $this_a = $(this),
                 	noMatches = $(container).find('.ln-no-match');
             		$this.letter = $this_a.attr('class').split(' ')[0];
             	//远程数据模式	
             	if($.founded(options.href)&&$.founded(options.mapper)){
    				//未加载过全部数据
                	if(($this.loadAll == false && $this.letter != 'matched' && $this.letter != '-')	|| $this.reloadData == true){
                		
                		//加载点击字符对应的数据；构建内容
                    	options.postData["letter"] = $this.letter;
        				loadRemoteData.call($this,options);
        				
        				$this.reloadData = false;
                	}
            	}
             	
             	
                
             	//获得上次选中和本次选中的元素
             	var prevList = $this.$list.filter('.ln-' + $this.prevLetter);
             	var thisList = $this.$list.filter('.ln-' + $this.letter);
             	var droplistList = $(container).find("ul.droplist");
             	var matchedList	= $(container).find("ul.droplist-matched");
             	
             	
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
                    	//点击【已选】选项
                    	$(droplistList).hide();
                    	if(options.items.length==0){
        		    		noMatches.addClass("show").removeClass("hide");
        		    	}else{
        		    		noMatches.addClass("hide").removeClass("show");
        		    		//循环已选元素对象
                 			$.each(options.items||[],function(j,itemObj){
                 				itemObj.checked = true;
             					//组织选中却不存在于当前条件的元素html
             					var selectElement = buildItemHtml.call(j,$this,itemObj);
             					$(selectElement).addClass("show").removeClass("hide");
             					$(matchedList).append(selectElement);
                 			});
                 			//绑定选择事件
                 			$(container).find("ul.droplist-matched li").each(function(i,li_item){
                 				$(li_item).css(options.mapper.style||{});
                 				bindItemEvent.call($this,li_item,options.items[i],2);
                 			});
                 			//显示已选元素
                 			$(matchedList).show();
        		    	}
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
                    	win_alert(options.letters[i]);
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
            
            /*添加需要暴露给开发者的函数*/
			$.extend(true,$this,{
				reload:function(paramMap){
					//扩展参数
					$this.reloadData = true;
					//删除原有参数
					$.each(paramMap, function (k, v) { 
			            delete options.postData[k];
			        });
					$.extend(options.postData,paramMap||{});
					//触发当前字母的点击事件;进行重新加载数据
					$('.' + $this.letter , $this.$letters).trigger(options.clickEventType);
				},
				resetSelection:function(key){
					$.each(options.data,function(i,itemObj){
						var tmpObj = buildItem(itemObj,options.mapper);
						if($.founded(key)&&tmpObj.key==key){
							removeItem(tmpObj);
							var list_item = $this.$list.filter("li[index="+tmpObj.index+"]");
							$(list_item).find(":checkbox:eq(0)")[0].checked  = false;
							$(list_item).find("span").removeClass("highlight");
							return false;
						}else{
							removeItem(tmpObj);
							var list_item = $this.$list.filter("li[index="+tmpObj.index+"]");
							$(list_item).find(":checkbox:eq(0)")[0].checked  = false;
							$(list_item).find("span").removeClass("highlight");
						}
					});
				},
				setSelection:function(key){
					$.each(options.data,function(i,itemObj){
						var tmpObj = buildItem(itemObj,options.mapper);
						if(tmpObj.key==key){
							addItem(tmpObj);
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