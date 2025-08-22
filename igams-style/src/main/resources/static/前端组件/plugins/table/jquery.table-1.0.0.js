/**
 * @discretion	: jQuery table插件，实现类似表格的累积增加和删除；效果见demo
 * @author    	: wandalong 
 * @version		: v1.0.1
 * @email     	: hnxyhcwdl1003@163.com
 * @example   	: 1.引用jquery的库文件js/jquery.js
  				  2.引用样式文件css/table-1.0.0.css
 				  3.引用效果的具体js代码文件 jquery.table-1.0.0.js
 				  4.<script language="javascript" type="text/javascript">
					jQuery(function($) {
					
						$("#scrollDiv").table({
							
						});
						
					});
					</script>
 */
;(function($){
	
	/*
		===data-api接口===：	
		data-bs-toggle	：	用于绑定组件的引用  data-bs-toggle="table"
		data-widget	:	组件元素上绑定的参数 {}
		
		data-url="data1.json" 
		data-cache="false" 
		data-height="299"
		data-sort-name=	"price" 
		data-sort-order="desc"
	*/

	$.bootui = $.bootui || {};
	$.bootui.widget = $.bootui.widget || {};
	
	/*====================== Table CLASS DEFINITION ====================== */
	
	$.bootui.widget.Table = function(element,options){
		options.beforeRender.call(this,element);	//渲染前的函数回调
		try {
			this.initialize.call(this, element, options);
		} catch (e) {
			options.errorRender.call(this,e);
		}
		options.afterRender.call(this,element);	/*渲染后的函数回调*/
	};
	
	$.bootui.widget.Table.prototype = {
		constructor: $.bootui.widget.Table,
		/*初始化组件参数*/
		initialize 	: function(element, options) {
			//定义变量
			var $this = this;
			
			function getUID(prefix) {
				do {
				  	prefix += ~~(Math.random() * 1000000);
			  	}while (document.getElementById(prefix));
			  	return prefix;
			};
			var table_uid		= getUID(options.prefix);
			var keyIndex	=	null;
        	$.each(options.colModel||[],function(i,model){
        		if(model.key==true){
        			keyIndex	= model.index || null;
        			return false;
        		}
        	});
			// the base DOM structure needed to create a table
			var templates = {
				container:function(){
					return '<div class="table-responsive"><table class="table table-bordered table-striped table-condensed table-hover tab-bor-col-1 '+options.classes+'" style="text-align:center"><thead></thead><tbody></tbody></table></div>';
				},
				thead:function(){
					var html = [];
					html.push('<tr>');
					//设置成false，就不显示行号；否则反之
					if(options.rownumbers==true){
						html.push('<td  width="'+(options.rownumWidth||'35px')+'" >&nbsp;</td>');
					}
					/*是否多选*/
					if(options.multiselect==true){
						html.push('<td  width="'+(options.multiselectWidth||'35px')+'" ><label><input type="checkbox" name="all_'+table_uid+'"/></label></td>');
					}else{
						html.push('<td  width="'+(options.multiselectWidth||'35px')+'" style="display: none;">&nbsp;</td>');
					}
					//key:true, sortable:false,hidden : true
					$.each(options.colModel||[],function(i,model){
						html.push('<td class="align-center bolder" '+ (model.width?'  width="'+model.width+'" ' :"")+ ((model.hidden==true)? ' style="display: none;" ':'') + ' >');
						html.push(model.label||"");
						html.push('</td>');
					});
					html.push('</tr>');
					return html.join("");
			    },
			    row:function(rowIndex,rowObject){
			    	var html = [];
					//key:true, hidden : true
			    	html.push('<tr index="'+rowObject[keyIndex]+'">');
			    	//设置成false，就不显示行号；否则反之
					if(options.rownumbers==true){
						html.push('<td class="align-center" data-mager="rownumber_'+rowObject[keyIndex]+'_1" width="'+(options.rownumWidth||'35px')+'" >'+(rowIndex+1)+'</td>');
					}
					/*是否多选*/
					if(options.multiselect==true){
						html.push('<td data-mager="input_'+rowObject[keyIndex]+'_2" ><label><input type="checkbox" name="'+table_uid+'" value="'+rowObject[keyIndex]+'"/></label></td>');
					}else{
						html.push('<td  data-mager="input_'+rowObject[keyIndex]+'_2"  style="display: none;"><label><input type="radio" name="'+table_uid+'"  value="'+rowObject[keyIndex]+'"/></label></td>');
					}
					$.each(options.colModel||[],function(colIndex,model){
						var mergerStr = model.mergeable == true ? ' data-mager="'+rowObject[keyIndex]+'_'+colIndex+'" ' : "";
						var alignClass	= 'class="align-'+(model.align||"center")+ '" ';
						var display		= (model.hidden==true ? ' style="display: none;" ' : '');
						var attr = $.isFunction(model.attr)? (model.attr(colIndex,rowObject[model.index],rowIndex,rowObject)||"") : (model.attr||"");
						html.push('<td '+ mergerStr + alignClass  + display + attr + '>');
						if($.isFunction(model.formatter)){
							html.push(model.formatter(colIndex,rowObject[model.index],rowIndex,rowObject)||"");
						}else{
							html.push(rowObject[model.index]);
						}
						html.push('</td>');
					});
					html.push('</tr>');
					return html.join("");
			    },
				empty:function(){
			    	var num = 0;
			    	if(options.rownumbers==true){
			    		num += 1;
					}
					/*是否多选*/
					if(options.multiselect==true){
						num += 1;
					}
			    	return '<tr><td class="align-center" colspan="'+(options.colModel.length + num)+'">'+options.emptyText+'</td></tr>';
			    }
			}
			
			var container = $(templates.container());
				$(element).empty().append(container);
				$(container).attr("id",table_uid);
				
            //======================添加数据元素============================================
			function magerCell(){
				//合并单元格
	            $(container).find(" tbody > tr ").each(function(i,tr){
					$(this).find("td").each(function(j,td){
						var key = $(this).data("mager");
						if($.trim(key).length>0){
							$(this).attr("rowspan",$("td[data-mager='"+key+"']").size());
							$("td[data-mager='"+key+"']:gt(0)").remove();
						}
					});
				});
	            //重新设置编号
	            if(options.rownumbers==true){
	            	var rowNum	=	1;
	            	$(container).find(" tbody > tr ").each(function(i,tr){
						var rownumberCell = $(this).find("td[data-mager='rownumber_"+$(tr).attr("index")+"_1']");
						if(rownumberCell.size()>0){
							$(rownumberCell).text(rowNum);
							rowNum	+=	1;
						}
					});
	            }
	            
			}
			function rander(){
				//thead
	            $(container).find("thead").empty().html(templates.thead());
	            //tbody
	            //程数据模式
	            if($.trim(options.href).length>0&&options.datatype=="json"){
	            	$(container).find("tbody").empty().html(options.loadingHtml);
					jQuery.ajaxSetup({async:false});
					//url, [data], [callback], [type]
					jQuery.get(options.href,options.postData||{} );
					jQuery.post(options.href,options.postData||{})

					//远程获取数据
					$.getJSON(options.href,options.postData||{}, function(data){
						options.data = data||[];
						options.loadComplete.call(container,data||[]);
						
						var rowHtmls	= 	[];
		                $.each(options.data||[],function(i,rowData){
		                 	rowHtmls.push(templates.row(i,rowData));
		                });
		                $(container).find("tbody").empty().html(rowHtmls.join(""));
		                magerCell();
		                options.tableComplete.call(container,options.data);
					});
	                jQuery.ajaxSetup({async:true});
	            }else{
	            	alert(options.data.length);
	                 if(options.data.length>0){
	                 	 var rowHtmls	= 	[];
	                      $.each(options.data||[],function(i,rowData){
	                      	  rowHtmls.push(templates.row(i,rowData));
	                      });
	                      $(container).find("tbody").empty().html(rowHtmls.join(""));
	                      magerCell();
	                 }else{
	                 	 $(container).find("tbody").empty().html(templates.empty());
	                 }
	                 options.tableComplete.call(container,options.data);
	            }
			}
            
            //=======================绑定事件===========================================
			
            function bindEvent(){
            	//全选的事件
                $("input[name='all_"+table_uid+"']").off(options.clickEventType).on(options.clickEventType, function (e) {
                	var status = $(this).prop("checked");
                	/*是否多选*/
                    if(options.multiselect==true){
                    	$("input[name='"+table_uid+"']").prop("checked",status);
                    }
                    var aRowids = [];
                    $(container).find("tbody > tr ").each(function(i,tr){
                    	aRowids.push($(tr).attr("index"));
                    });
                	options.onSelectAll.call(container,aRowids,status);
    			});
                $(container).find("tbody > tr ").each(function(i,tr){
                	$(tr).find("input[name='"+table_uid+"']").off(options.clickEventType).on(options.clickEventType, function (e) {
    					e.stopPropagation();
                        options.onSelectRow.call(container,$(this).val(),$(this).prop("checked"));
                        //$(tr).toggleClass("highlight");
    				});
                	
                	$(this).off(options.clickEventType).on(options.clickEventType, function (e) {
                		var input = $(this).find("input[name='"+table_uid+"']");
                		var status = status = true;
                		/*是否多选*/
                        if(options.multiselect==true){
                        	status = !$(input).prop("checked");
            			}
                        $(input).prop("checked",status);
                        options.onSelectRow.call(container,$(input).val(),status);
                        e.stopPropagation();
                		//$(this).toggleClass("highlight");
        			});
                });
            }
			
            /*添加需要暴露给开发者的函数*/
			$.extend(true,$this,{
				destroy : function () {
					$(element).off('.' + options.prefix).removeData('bootui.table');
					$("#"+table_uid).remove();
				},
				reload:function(paramMap){
					//删除原有参数
					$.each(paramMap||{}, function (k, v) { 
			            delete options.postData[k];
			        });
					$.extend(options.postData,paramMap||{});
					//渲染数据，绑定事件
					rander();
		            bindEvent();
				},
				resetSelection:function(){
					 $(container).find("tbody > tr ").each(function(i,tr){
						 $(this).find("input[name='"+table_uid+"']").prop("checked",false);
						 //$(this).removeClass("highlight");
					 });
				},
				setSelection:function(key,flag){
					var row_item = $(container).find("tr[index='"+key+"']");
					if(flag){
						$(row_item).trigger(options.clickEventType);
					}else{
						$(row_item).find("input[name='"+table_uid+"']").prop("checked",true);
						//$(row_item).toggleClass("highlight");
					}
				},
				getKeys:function(){
					var result = new Array();
					//获取选中项的input
					var checkeds = $("input[name='"+table_uid+"']").filter(":checked");
					$(checkeds).each(function(i,checked){
						result.push($(checked).val());
					});
					return result;
				},
				getRow:function(key){
					var rows = [];
					$.each(options.data||[],function(i,rowData){
	                   	 if(rowData[keyIndex]==key){
	                   		rows.push($.extend({},rowData));
	                   	 }
                    });
					return rows.length>1?rows:rows[0];
				},
				getRows:function(){
					return options.data||[];  
				},
				addRow:function(rowData){
					$(container).find("tbody").append(templates.row(options.data.length,rowData));
					options.data.push(rowData);
					//绑定事件
		            bindEvent();
				},
				setRow:function(key,rowData){
					$.each(options.data||[],function(i,item){
						if(item[keyIndex]==key){
							//[].splice(start, deletecount, items)
							options.data.splice(i, 1, rowData);
							$(container).find("tr[index='"+key+"']").replaceWith(templates.row(i,rowData));
							//绑定事件
				            bindEvent();
							return false;
                   	 	}
					});
				},
				deleteRow:function(key){
					$.each(options.data||[],function(i,rowData){
						if(rowData[keyIndex]==key){
							//[].splice(start, deletecount, items)
							options.data.splice(i, 1, rowData);
							$(container).find("tr[index='"+key+"']").remove();
							return false;
                   	 	}
					});
				}
			});
			
			//渲染数据，绑定事件
			rander();
            bindEvent();
		},
		setDefaults	: function(settings){
			$.extend($.fn.table.defaults, settings );
		},
		getDefaults	: function(settings){
			 return $.fn.table.defaults;
		}
	}
	
	/* Table PLUGIN DEFINITION  */
	
	$.fn.table = function(option){
		//处理后的参数
		var args = $.grep( arguments || [], function(n,i){
			return i >= 1;
		});
		return this.each(function () {
			var $this = $(this);
			var data = $this.data('bootui.table');
			if (!data && option == 'destroy') {return;}
			if (!data){
				var options = $.extend(true,{}, $.fn.table.defaults, $this.data(), ((typeof option == 'object' && option) ? option : {}));
				$this.data('bootui.table', (data = new $.bootui.widget.Table(this, options)));
			}
			if (typeof option == 'string'){
				//调用函数
				data[option].apply(data, [].concat(args || []) );
			}
		});
	};
	
	$.fn.table.defaults = {
		/*版本号*/
		version			:'1.0.0',
		prefix			: "table",
		/*组件进行渲染前的回调函数：如重新加载远程数据并合并到本地数据*/
		beforeRender	: $.noop,
		/*组件渲染出错后的回调函数*/
		errorRender		: $.noop,
		/*组件渲染完成后的回调函数*/
		afterRender		: $.noop, 
		/*其他参数*/
		rownumbers		: false,//设置成false，就不显示行号；否则反之
		rownumWidth		: 35,
		/*是否多选*/
		multiselect 	: true,  
		multiselectWidth: 20,
		/*是否条纹状表格:通过 .table-striped 类可以给 <tbody> 之内的每一行增加斑马条纹样式。不支持Internet Explorer 8 */
		striped 		: true,
		/*是否带边框的表格：添加 .table-bordered 类为表格和其中的每个单元格增加边框。*/
		bordered 		: true,
		/*是否紧缩表格：通过添加 .table-condensed 类可以让表格更加紧凑，单元格中的内补（padding）均会减半。*/
		condensed		: true,
		/*表格额外class*/
		classes			: "  tab-td-padding-10 ",
		/*数据类型，默认local。可选类型：local，json*/
		datatype 		: "local", 
		emptyText		: "无数据...",
		loadingHtml		: '<p class="text-center header smaller lighter loading"><i class="icon-spinner icon-spin orange  bigger-300"></i></br> 数据正在载入数据中....</p>',
		postData		: {},
		href			: '', //这是Action的请求地址  
		colModel		: [],
		/**colModel:[
			{label:'用户名',index: 'yhm',key:true,align:'center'},
			{label:'姓 名',index: 'xm',align:'center'},
			{label:'所属机构',index: 'jgmc',align:'center'},
			{label:'联系电话',index: 'sjhm',align:'center'},
			{label:'邮箱', index: 'dzyx',align:'center'},
			{label:'是否启用',index: 'sfqy',align:'center'},
			{label:'拥有角色数', index: 'jss',align:'center',formatter:function(rowIndex,cellvalue,rowObject){
				return "<a href='javascript:ckYh(\""+rowObject.yhm+"\")' >"+cellvalue+"</a>";
			}}
		],*/
		loadComplete	: function (table,data) {},
		tableComplete	: function (table) {},
		/*触发全选中事件*/
		onSelectAll		: function(aRowids,status){
			jQuery.each(aRowids,function(index,rowid){
				
			});
		},
		/*触发选中事件*/
		onSelectRow		: function(rowid,status){ 
	   	},
		data:[],
	 	//detect if you are on a touch device easily.
        clickEventType:((document.ontouchstart!==null)?'click':'touchstart')
	};

	$.fn.table.Constructor = $.bootui.widget.Table;
	

}(jQuery));

