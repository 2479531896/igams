
function closeNav() {
	$("#purchaseAuditViewForm #t_div").animate({left:'-380px'},"slow");
	$("#purchaseAuditViewForm .tr").attr("style","background-color:white;");
}

function viewmore(qgmxid){
	$.ajax({ 
	    type:'post',  
	    url:$("#purchaseAuditViewForm #urlPrefix").val()+"/ws/production/viewMorePurchase", 
	    cache: false,
	    data: {"qgmxid":qgmxid,"qglbdm":$("#purchaseAuditViewForm #qglbdm").val(),"url":$("#purchaseAuditViewForm #url").val(),"urlPrefix":$("#purchaseAuditViewForm #urlPrefix").val()},
	    dataType:'json', 
	    success:function(data){
	    	var wlmc=data.QgmxDto.wlmc_t;
	    	var wllbmc=data.QgmxDto.wllbmc;
	    	var wlzlbmc=data.QgmxDto.wlzlbmc;
	    	var wlzlbtcmc=data.QgmxDto.wlzlbtcmc;
	    	var gg=data.QgmxDto.gg_t;
	    	var jldw=data.QgmxDto.jldw_t;
	    	var scs=data.QgmxDto.scs;
	    	if($("#purchaseAuditViewForm #qglbdm").val()!='DEVICE' && $("#purchaseAuditViewForm #qglbdm").val()!='MATERIAL'  && $("#purchaseAuditViewForm #qglbdm").val()!='OUTSOURCE' && $("#purchaseAuditViewForm #qglbdm").val()!=null && $("#purchaseAuditViewForm #qglbdm").val()!=''){
	    		scs=data.QgmxDto.gys
	    	}
	    	var wlbm=data.QgmxDto.wlbm;
	    	var wlid=data.QgmxDto.wlid;
	    	var jg=data.QgmxDto.jg;
	    	var sl=data.QgmxDto.sl;
	    	var fjcfbDtos=data.mx_fjcfbDtos;
	    	var fwqx=data.QgmxDto.hwbz;
	    	var hwyt=data.QgmxDto.hwyt;
	    	var fwyq=data.QgmxDto.yq;
	    	var zlyq=data.QgmxDto.zlyqmc;
	    	var wbyq=data.QgmxDto.wbyq;
	    	var pzyq=data.QgmxDto.pzyq;
	    	var wlbz=data.QgmxDto.wlbz;
	    	if(zlyq==null){
	    		zlyq="";
	    	}
	    	if(wbyq==null){
	    		wbyq="";
	    	}
	    	if(pzyq==null){
	    		pzyq="";
	    	}
	    	if(fwyq==null){
	    		fwyq="";
	    	}
	    	if(hwyt==null){
	    		hwyt="";
	    	}
	    	if(fwqx==null){
	    		fwqx="";
	    	}
	    	if(jg==null || jg==''){
	    		jg=0;
	    	}
	    	if(sl==null || sl==''){
	    		sl=0;
	    	}
	    	if(wlmc==null){
	    		wlmc="";
	    	}
	    	if(wlbm==null){
	    		wlbm=""
	    	}
	    	if(wllbmc==null){
	    		wllbmc="";
	    	}
	    	if(wlzlbmc==null){
	    		wlzlbmc="";
	    	}
	    	if(wlzlbtcmc==null){
	    		wlzlbtcmc="";
	    	}
	    	if(gg==null){
	    		gg="";
	    	}
	    	if(scs==null){
	    		scs="";
	    	}
	    	if(jldw==null){
	    		jldw="";
	    	}
	    	if(wlbz==null){
	    		wlbz="";
	    	}
	    	//返回值
	    	$("#purchaseAuditViewForm #zlyq").text(zlyq);
	    	$("#purchaseAuditViewForm #pzyq").text(pzyq);
	    	$("#purchaseAuditViewForm #wbyq").text(wbyq);	    	
	    	$("#purchaseAuditViewForm #wlmc").text(wlmc);
	    	$("#purchaseAuditViewForm #wlbm").text(wlbm);
	    	$("#purchaseAuditViewForm #wllbmc").text(wllbmc);
	    	$("#purchaseAuditViewForm #wlzlbmc").text(wlzlbmc);
	    	$("#purchaseAuditViewForm #wlzlbtcmc").text(wlzlbtcmc);
	    	$("#purchaseAuditViewForm #gg").text(gg);
	    	$("#purchaseAuditViewForm #jldw").text(jldw);
	    	$("#purchaseAuditViewForm #scs").text(scs);
	    	$("#purchaseAuditViewForm #sl").text(sl);
	    	$("#purchaseAuditViewForm #dj").text(jg);
	    	$("#purchaseAuditViewForm #zj").text(((parseInt(jg*100)*parseInt(sl*100))/10000).toFixed(2));
	    	$("#purchaseAuditViewForm #fwqx").text(fwqx);
	    	$("#purchaseAuditViewForm #fwyq").text(fwyq);
	    	$("#purchaseAuditViewForm #hwyt").text(hwyt);
	    	$("#purchaseAuditViewForm #wlbz").text(wlbz);
	    	$("#purchaseAuditViewForm #t_div").animate({left:'0px'},"slow");
	    	$("#purchaseAuditViewForm .tr").attr("style","background-color:white;");
	    	$("#purchaseAuditViewForm #"+qgmxid).attr("style","background-color:#B3F7F7;");
	    	//附件
	    	var html="";
	    	if(fjcfbDtos != null && fjcfbDtos.length>0){
	    		for(var i=0;i<fjcfbDtos.length;i++){
	    			html+="<div class='form-group'>";
	    			if(i==0){
	    				html+="<label for='' class='col-md-4 col-sm-4 col-xs-4 control-label' style='padding-left:0px;padding-right:0px;'>附件</label>";
	    			}else if(i>0){
	    				html+="<label for='' class='col-md-4 col-sm-4 col-xs-4 control-label' style='padding-left:0px;padding-right:0px;'>　　</label>";
	    			}
	    			html+="<div class='col-md-8 col-sm-8 padding_t7 h30'>";
	    			if(fjcfbDtos.length>1){
	    				html+="<label for='' text='"+(i+1)+"."+"'></label>";
	    			}
	    			html+="<span class='col-md-12 col-sm-12' style='overflow:hidden;text-overflow:ellipsis; white-space: nowrap;padding-left:0px;'>"+fjcfbDtos[i].wjm+"</span>";
	    			html+="</a>&nbsp;&nbsp; <a title='预览' onclick=\"yl('"+fjcfbDtos[i].fjid+"','"+fjcfbDtos[i].wjm+"')\"><span class='glyphicon glyphicon-eye-open'></span></a>";
	    			html+="</div>";
	    		}
	    	}
	    	$("#purchaseAuditViewForm #mx_fjcfbDtos").append(html);
			if ("SERVICE"!=$("#purchaseAuditViewForm #qglbdm").val()){
				loadWeekLeadStatis(wlid);
				$("#purchaseAuditViewForm #wlkclTable").empty();
				$("#purchaseAuditViewForm #lcslTable").empty();
				$("#purchaseAuditViewForm #ztslTable").empty();
				var kclhtml="";
				kclhtml+=' <div class="body-content tab-content" style="margin-left: -5px;padding-left: 0px;">\n' +
				'                <div class="panel-body" style="\n' +
				'    width: 376px;\n' +
				'    padding-left: 0px;\n">\n' +
				'                   <table class="tab" style="width:100%;">\n' +
				'                        <tr style="background-color: #63a3ff;">\n' +
				'                            <th style="width:5%">序</th>\n' +
				'                            <th style="width:20%">仓库</th>\n' +
				'                            <th style="width:25%">生产批号</th>\n' +
				'                            <th style="width:20%">生产日期</th>\n' +
				'                            <th style="width:20%">失效日期</th>\n' +
				'                            <th style="width:15%">库存量</th>\n' +
				'                        </tr>\n' +
				'                        <div>\n';

				for(var i=0;i<data.hwxxDtos.length;i++){
					kclhtml+=' <tr>\n' +
					'      <td>'+(i+1)+'</td><!-- 序号 -->\n' +
					'      <td>'+data.hwxxDtos[i].ckmc+'</td><!-- 仓库 -->\n' +
					'      <td>'+data.hwxxDtos[i].scph+'</td><!-- 生产批号 -->\n' +
					'      <td>'+data.hwxxDtos[i].scrq+'</td><!-- 生产日期 -->\n' +
					'      <td>'+data.hwxxDtos[i].yxq+'</td><!-- 失效日期 -->\n' +
					'      <td>'+data.hwxxDtos[i].kcl+'</td><!-- 库存量 -->\n' +
					'      </tr>';
				}
				kclhtml+=
				'                        </div>\n' +
				'                    </table>\n' +
				'                </div>\n' +
				'            </div>';
				$("#purchaseAuditViewForm #wlkclTable").append(kclhtml);
				if(data.llhwxxDtos != null && data.llhwxxDtos.length>0){
					var lcslhtml="";
					lcslhtml+=' <div class="body-content tab-content" style="margin-left: -5px;padding-left: 0px;">\n' +
					'                <div class="panel-body" style="\n' +
					'    width: 376px;\n' +
					'    padding-left: 0px;\n">\n' +
					'                   <table class="tab" style="width:100%;">\n' +
					'                        <tr style="background-color: #63a3ff;">\n' +
					'                            <th style="width:3%">序</th>\n' +
					'                            <th style="width:25%">月份</th>\n' +
					'                            <th style="width:25%">出库数量</th>\n' +
					'                        </tr>\n' +
					'                        <div>\n';

					for(var i=0;i<data.llhwxxDtos.length;i++){
						lcslhtml+=' <tr>\n' +
						'      <td>'+(i+1)+'</td><!-- 序号 -->\n' +
						'      <td>'+data.llhwxxDtos[i].yf+'</td><!-- 月份 -->\n' +
						'      <td>'+data.llhwxxDtos[i].lcsl+'</td><!-- 出库数量 -->\n' +
						'      </tr>';
					}
					lcslhtml+=
					'                        </div>\n' +
					'                    </table>\n' +
					'                </div>\n' +
					'            </div>';
					$("#purchaseAuditViewForm #lcslTable").append(lcslhtml);
				}
				var ztslhtml="";
				ztslhtml+=' <div class="body-content tab-content" style="margin-left: -5px;padding-left: 0px;">\n' +
				'                <div class="panel-body" style="\n' +
				'    width: 376px;\n' +
				'    padding-left: 0px;\n">\n' +
				'                   <table class="tab" style="width:100%;">\n' +
				'                        <tr style="background-color: #63a3ff;">\n' +
				'                            <th style="width:5%">序</th>\n' +
				'                            <th style="width:25%">合同单号</th>\n' +
				'                            <th style="width:15%">申请部门</th>\n' +
				'                            <th style="width:20%">在途数量</th>\n' +
				'                            <th style="width:20%">预计到货日期</th>\n' +
				'                        </tr>\n' +
				'                        <div>\n';
				if(data.ztslHwxxDtos != null && data.ztslHwxxDtos.length>0){
					for(var i=0;i<data.ztslHwxxDtos.length;i++){
						ztslhtml+=' <tr>\n' +
						'      <td>'+(i+1)+'</td><!-- 序号 -->\n' +
						'      <td>'+data.ztslHwxxDtos[i].htnbbh+'</td><!-- 合同单号 -->\n' +
						'      <td>'+data.ztslHwxxDtos[i].sqbmmc+'</td><!-- 申请部门 -->\n' +
						'      <td>'+data.ztslHwxxDtos[i].ztsl+'</td><!-- 在途数量 -->\n' +
						'      <td>'+data.ztslHwxxDtos[i].jhdhrq+'</td><!-- 预计到货日期 -->\n' +
						'      </tr>';
					}
					ztslhtml+=
					'                        </div>\n' +
					'                    </table>\n' +
					'                </div>\n' +
					'            </div>';
				}
				$("#purchaseAuditViewForm #ztslTable").append(ztslhtml);
			}
		}
	});
}

function yl(fjid,wjm,zhwjxx){
	var urlPrefix= $("#purchaseAuditViewForm #urlPrefix").val();
	var begin=wjm.lastIndexOf(".");
	var end=wjm.length;
	var type=wjm.substring(begin,end);
	if(zhwjxx!=null && zhwjxx!=''){
		var url= urlPrefix+"/common/view/displayView?view_url=/ws/file/pdfPreview?fjid=" + fjid+"&pdf_flg=1";
		window.location.href=url;
	}else{
		if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
			var url=urlPrefix+"/ws/sjxxpripreview?pageflg=1&fjid="+fjid
			window.location.href=url;
		}else if(type.toLowerCase()==".pdf" ||type.toLowerCase()==".doc"||type.toLowerCase()==".docx"){
			var url= urlPrefix+"/common/view/displayView?view_url=/ws/file/pdfPreview?fjid=" + fjid;
			window.location.href=url;
		}else {
			alert("暂不支持其他文件的预览，敬请期待！");
		}
	}
}
//加载统计数据
var loadWeekLeadStatis = function(wlid){
	var _eventTag = "weekLeadStatis";//事件标签
	var _loadEffect = "whirling";//加载效果spin,bar,ring,whirling,dynamicLine,bubble'
	var _isShowLoading = false; //是否显示加载动画
	var _idPrefix = "purchaseAudit_viewkcInfo_"; //id前缀
	var _statis_theme="macarons";//显示主题 macarons,infographic,shine,world
	var _renderArr = [
		{
			id:"hqxxType",
			chart: null,
			el: null,
			render:function(data,searchData){
				if(data&&this.chart){
					var dataxAxis= new Array();
					var dataseries=new Array();
					var maxY=0;
					var interval;
					for (var i = 0; i < data.length; i++) {
						dataxAxis.push(data[i].htnbbh);
						if (data[i].hq==""){
							data[i].hq='0';
						}
						dataseries.push(data[i].hq);
						if(parseInt(maxY) < parseInt(data[i].hq))
							maxY = data[i].hq
					}
					maxY= parseInt(maxY) + data.length - parseInt(maxY%(data.length))
					interval=maxY/data.length

					var pieoption = {
						tooltip: {
							trigger: 'axis',
							axisPointer: {
								type: 'cross',
								crossStyle: {
									color: '#999'
								}
							}
						},
						grid: {
							left:'20%',
							bottom:'35%'
						},
						xAxis : [
							{
								type : 'category',
								data:dataxAxis,
								axisTick: {
									alignWithLabel: true
								},
								axisLabel:{ //展示角度
									rotate:60
								},
								axisLine: {
									lineStyle: {
										color: '#d14a61'
									},
								},
							}
						],
						yAxis : [
							{
								type: 'value',
								name: '天数',
								min: 0,
								max: maxY,
								interval: interval,
								axisLabel: {
									formatter: '{value}'
								}
							}
						],
						series : [
							{
								name:'货期',
								type:'line',
								itemStyle: {
									normal: {
										label: {
											show: true, //开启显示
											position: 'top', //在上方显示
											textStyle: { //数值样式
												fontSize: 12
											}
										}
									}
								},
								data:dataseries
							}
						]
					};
					this.chart.setOption(pieoption);
					this.chart.hideLoading();
					//点击事件
					this.chart.on("click",function(param){
					});
				}
			}
		}
	];
	// 路径配置
	setTimeout(function(){
		for(var i=0; i<_renderArr.length; i++){
			var _render = _renderArr[i];
			var _el = _render.el = document.getElementById(_idPrefix+_render.id);
			if(_el){
				_render.chart = echarts.init(_el,_statis_theme);
				_render.eventTag = "."+ _eventTag+"-"+_render.id;
				if(_isShowLoading){
					_render.chart.showLoading({
						effect:_loadEffect
					});
				}
				$(window).off(_render.eventTag).on("resize"+_render.eventTag, (function(_cfg){return function(){// resize事件
					if(!_cfg.chart||$(document).find(_cfg.chart.dom).size()==0){//元素不存在，则不操作
						return;
					}
					_cfg.chart.resize();
				}})(_render));
			}
		}
		//加载数据
		var _url = $("#purchaseAuditViewForm #urlPrefix").val()+"/ws/production/pagedataGetHq";
		var param ={};
		param["wlid"]= wlid;
		$.ajax({
			type : "post",
			url : _url,
			data:param,
			dataType : "json",
			success : function(datas) {
				if(datas){
					var _searchData = datas['searchData'];
					for(var i=0; i<_renderArr.length; i++){
						var _render = _renderArr[i];
						var _data = datas[_render.id];
						_render.render(_data,_searchData);
						if(datas[_render.id]&&datas[_render.id]!="searchData"){
							if(datas[_render.id].length==0){
								$("#purchaseAudit_viewkcInfo_"+_render.id).parent().hide();
							}
						}else {
							$("#purchaseAudit_viewkcInfo_"+_render.id).parent().hide();
						}
					}
				}else{
					//throw "loadClientStatis数据获取异常";
				}
			}
		});
	});
}
$(function(){
})