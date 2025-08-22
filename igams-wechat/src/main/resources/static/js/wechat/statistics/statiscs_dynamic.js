 
var tjzq=null;
var tjtys=null;
/**
  * 点击生成统计图
  * @returns
  */
function configuration(){
	 var tjzd = new Array();
	 var tjcol = new Array();
	 var mainselect = new Array();
	 var maingroup = new Array();
	 var selectzd = new Array();
	 var groupzd = new Array();
	 var titjStr = new Array(); 
	 var column=new Array(); 
	 
	 /**
		 * 获取选中的统计周期
		 * @returns
		 */
		$(".tjzq").each(function(){
			if(this.checked==true){
				tjzq=this.value;
			}
		})
		
	 /**
	  * 点击统计按钮时，先判断是否选中接收日期，如果选中接收日期，统计字段只能选一个
	  */
	 if(tjtys=="Bar"&&tjzq!="all"){
		 if($("#custom_jsrqstart").val()!="" || $("#custom_bgrqstart").val()!="" || $("#custom_jsrqend").val()!="" || $("#custom_bgrqend").val()!=""){
				if(this.value!="all"){
					 $("#tjzd_div").children().eq(1).children().children("option").eq(0).prop("selected", "selected");  
					 $("#tjzd_div").children().eq(1).children().trigger("chosen:updated");
				}
			}
	 }
	 
	/**
	 * 获取输入的宽度
	 */
	var width=$("#statisWidth").val()+"px";  
	
	/**
	 *获取输入的高度
	 */
	var height=$("#statisHeight").val()+"px";
	
	if(tjzq!="all"){
		if($("#custom_bgrqstart").val()!="" || $("#custom_bgrqend").val()!=""){
			tjzd.push("bgrq");
			tjcol.push("报告日期");
			if(tjzq=="year"){
				mainselect.push("to_char(sj.bgrq，'YYYY') bgrq");
				maingroup.push("to_char(sj.bgrq，'YYYY')");
				selectzd.push("to_char(sj.bgrq，'YYYY') bgrq");
				groupzd.push("to_char(sj.bgrq，'YYYY')");
			}else if(tjzq=="month"){
				mainselect.push("to_char(sj.bgrq，'YYYY-MM') bgrq");
				maingroup.push("to_char(sj.bgrq，'YYYY-MM')");
				selectzd.push("to_char(sj.bgrq，'YYYY-MM') bgrq");
				groupzd.push("to_char(sj.bgrq，'YYYY-MM')");
			}else if(tjzq=="day"){
				mainselect.push("to_char(sj.bgrq，'YYYY-MM-DD') bgrq");
				maingroup.push("to_char(sj.bgrq，'YYYY-MM-DD')");
				selectzd.push("to_char(sj.bgrq，'YYYY-MM-DD') bgrq");
				groupzd.push("to_char(sj.bgrq，'YYYY-MM-DD')");
			}
		}else if($("#custom_jsrqstart").val()!="" || $("#custom_jsrqend").val()!=""){
			tjzd.push("jsrq");
			tjcol.push("接收日期");
			if(tjzq=="year"){
				mainselect.push("to_char(sj.jsrq，'YYYY') jsrq");
				maingroup.push("to_char(sj.jsrq，'YYYY')");
				selectzd.push("to_char(sj.jsrq，'YYYY') jsrq");
				groupzd.push("to_char(sj.jsrq，'YYYY')");
			}else if(tjzq=="month"){
				mainselect.push("to_char(sj.jsrq，'YYYY-MM') jsrq");
				maingroup.push("to_char(sj.jsrq，'YYYY-MM')");
				selectzd.push("to_char(sj.jsrq，'YYYY-MM') jsrq");
				groupzd.push("to_char(sj.jsrq，'YYYY-MM')");
			}else if(tjzq=="day"){
				mainselect.push("to_char(sj.jsrq，'YYYY-MM-DD') jsrq");
				maingroup.push("to_char(sj.jsrq，'YYYY-MM-DD')");
				selectzd.push("to_char(sj.jsrq，'YYYY-MM-DD') jsrq");
				groupzd.push("to_char(sj.jsrq，'YYYY-MM-DD')");
			}
		}
	}
	
	/**
	 *  //获取统计字段
	 * @returns
	 */
	$(".tjzd").each(function(){
		if(this.selected==true){  
			if(this.value!="none"){
				if(tjzd.indexOf(this.value) == -1){
					tjzd.push(this.value);
					tjcol.push(this.text);
					mainselect.push($(this).attr("mainSelect"));
					maingroup.push($(this).attr("mainGroup"));
					selectzd.push($(this).attr("selzd"));
					groupzd.push($(this).attr("groupzd"));
				}
			}
		}
	})
	/**
	 * 把所有的参数封装为一个map
	 */
	var param={
		tjzq:tjzq,
		jsrqstart:$("#custom_jsrqstart").val(),
		jsrqend:$("#custom_jsrqend").val(),
		bgrqstart:$("#custom_bgrqstart").val(),
		bgrqend:$("#custom_bgrqend").val()
	}
	/**
	 * 获取统计条件以及参数
	 * @returns
	 */
	$(".tjtj").each(function(){   
		if(this.selected==true){  
			if(this.value!="none"){
				var value=$(this).parent().parent().parent().children().eq(2).children().val();
				if(value!=""){
					titjStr.push(this.value+":"+value)
				}
			}
		}
	})
	
	/**
	 * 组装两个字段数组
	 */
	function formatter(data,tem,j,Xdata,storage){
		var param0= storage[0].substring(1,storage[0].length-1);
		var param1= storage[1].substring(1,storage[1].length-1);
		var _data=new Array();
		if(tem[j]){
			for (var x = 0; x < Xdata.length; x++) {
				var flg=0;
				 $.each(data.rows,function(){
					if(eval("this."+param1)==tem[j] && eval("this."+param0)==Xdata[x]){
						_data.push(this.cn);
						flg++;
					}
				})
				if(flg==0){
					_data.push(0);
				}
			}
		}
		return _data;
	}
	if(tjtys=="Table"){
		if(tjzd.length>0){
				$("#tbale_list").css("width",$("#statisWidth").val());
				$("#tbale_list").css("height",$("#statisHeight").val());
				custom_TableInit(param,titjStr,tjzd,tjcol,mainselect,maingroup,selectzd,groupzd);
		}else {
			$.alert("请选择统计字段！");
		}
 	}else if(tjtys=="Bar"){
 		if(tjzd.length>0){
 			$("#echarts_custom_statis_sjxxStatis").css("width",parseInt($("#statisWidth").val())+parseInt(200));
			$("#echarts_custom_statis_sjxxStatis").css("height",parseInt($("#statisHeight").val())+parseInt(100));
			var myChart = echarts.init(document.getElementById('echarts_custom_statis_sjxxStatis'));
				myChart.resize();
 			loadBarStatis();
 			$.ajax({
 				url : "/ws/statistics/getGraphCustomStatis",
 				type : "post",
 				dataType : "json",
 				data:{"param":param,
 					"tjtj":JSON.stringify(titjStr),
 					"tjzds":JSON.stringify(tjzd),
 					"mainSelects":JSON.stringify(mainselect),
 					"mainGroups":JSON.stringify(maingroup),
 					"selectzds":JSON.stringify(selectzd),
 					"groupzds":JSON.stringify(groupzd)},
 				success : function(data) {
 					var Xdata=new Array();
 					var Ydata=new Array();
 					var tem=new Array();
 					if(data.rows!=null){
 	 					if(tjzd.length==2){
 	 						var storage=JSON.stringify(tjzd).replace("ks", "ksmc").replace("yblx", "yblxmc").replace("fl", "flmc").replace("zfl", "zflmc").replace("jcxmid", "jcxmmc").replace("[", "").replace("]", "").trim().split(",");
 	 						var zd1="data.rows[i]."+storage[0].substring(1,storage[0].length-1);
 	 						var zd2="data.rows[i]."+storage[1].substring(1,storage[1].length-1);
 	 						for (var i = 0; i < data.rows.length; i++) {
 	 							var zd_fir=eval(zd1);
 	 							if(zd_fir!=null && Xdata.indexOf(zd_fir) == -1){
 	 								Xdata.push(zd_fir);
 	 							}
 	 							var zd_sec=eval(zd2);
 	 							if(zd_sec!=null && tem.indexOf(zd_sec) == -1){
 	 								tem.push(zd_sec);
 	 							}
							}
 							for(var j = 0; j < tem.length; j++){
 								if(tem[j]){
 									Ydata.push({name:tem[j], type: 'bar',barGap:0,data:formatter(data,tem,j,Xdata,storage),label: {normal: { show: true,position: 'top'}
 	 						            },});
 								}
 							}
 	 						var myChart = echarts.init(document.getElementById('echarts_custom_statis_sjxxStatis'));
	 						 myChart.setOption({
	 							 grid: {
	 							    	width:width,
	 							    	height:height,
	 							    },
	 							 xAxis : [
	 							        {
	 							            data : Xdata,
	 							        }
	 							    ],
	 							   series : Ydata
	 						    });
 	 					}else if(tjzd.length==1){
 	 						var tmp=JSON.stringify(tjzd).replace("ks", "ksmc").replace("yblx", "yblxmc").replace("fl", "flmc").replace("zfl", "zflmc").replace("jcxmid", "jcxmmc").replace("[", "").replace("]", "").trim().split(",");
 	 						for (var i = 0; i < data.rows.length; i++) {
 	 							var zd="data.rows[i]."+tmp[0].substring(1,tmp[0].length-1);
 	 							    Xdata.push(eval(zd));
	 								Ydata.push(data.rows[i].cn);
 	 						}
 	 						var myChart = echarts.init(document.getElementById('echarts_custom_statis_sjxxStatis'));
 	 						 myChart.setOption({
 	 							 grid: {
 	 							    	width:width,
 	 							    	height:height,
 	 							    },
 	 							 xAxis : [
 	 							        {
 	 							            data : Xdata,
 	 							        }
 	 							    ],
 	 							    series : [
 	 							        {
 	 							        	name:'数量',
 	 							            type:'bar',
 	 							            data:Ydata,
	 	 							        label: {
	 	 								        normal: {
	 	 								            show: true,
	 	 								            position: 'top',
	 	 								            fontSize:'20',
	 	 								            color:'blue'
	 	 								        }
	 	 						            },
 	 							            itemStyle: {
 	 							                normal: {
 	 							                    color: new echarts.graphic.LinearGradient(
 	 							                        0, 0, 0, 1,
 	 							                        [
 	 							                            {offset: 0, color: '#B2DFEE'},                   //柱图渐变色
 	 							                            {offset: 0.2, color: '#C6E2FF'},                 //柱图渐变色
 	 							                            {offset: 0.4, color: '#BFEFFF'}, 
 	 							                            {offset: 0.6, color: '#B9D3EE'},
 	 							                            {offset: 0.8, color: '#B0C4DE'},
 	 							                            {offset: 1.0, color: '#AB82FF'},
 	 							                        ]
 	 							                    )
 	 							                },
 	 							                emphasis: {
 	 							                    color: new echarts.graphic.LinearGradient(
 	 							                        0, 0, 0, 1,
 	 							                        [
 	 							                            {offset: 0, color: '#71C8B1'},                  //柱图高亮渐变色
 	 							                            {offset: 0.7, color: '#44C0C1'},                //柱图高亮渐变色
 	 							                            {offset: 1, color: '#06B5D7'}                   //柱图高亮渐变色
 	 							                        ]
 	 							                    )
 	 							                }
 	 							            }
 	 							        }
 	 							    ]
 	 						    });
 	 					}
 					}
 				}
 			});
		}else {
			$.alert("请选择统计字段！");
		}
	}else if(tjtys=="Pie"){
		if(tjzd.length>0){
			$("#echarts_custom_statis_sjxxStatis").css("width",parseInt($("#statisWidth").val())+parseInt(100));
			$("#echarts_custom_statis_sjxxStatis").css("height",parseInt($("#statisHeight").val())+parseInt(100));
			var myChart = echarts.init(document.getElementById('echarts_custom_statis_sjxxStatis'));
				myChart.resize();
			loadPierStatis();
			delete param.tjzq;
			$.ajax({
				url : "/ws/statistics/getGraphCustomStatis",
				type : "post",
				dataType : "json",
				data:{"param":param,
					"tjtj":JSON.stringify(titjStr),
 					"tjzds":JSON.stringify(tjzd),
 					"mainSelects":JSON.stringify(mainselect),
 					"mainGroups":JSON.stringify(maingroup),
 					"selectzds":JSON.stringify(selectzd[0]),
 					"groupzds":JSON.stringify(groupzd)},
				success : function(data) {
					var legendData=new Array();
					var seriesData=new Array();
					if(data.rows!=null){
						var name=null;
						for (var i = 0; i < data.rows.length; i++) {
							if(data.rows[i].sjdw!=null){
								seriesData.push({value:data.rows[i].cn,name:data.rows[i].sjdw});
								legendData.push(data.rows[i].sjdw);
								name="医院名称"
							}else if(data.rows[i].ksmc!=null){
								seriesData.push({value:data.rows[i].cn,name:data.rows[i].ksmc});
								legendData.push(data.rows[i].ksmc);
								name="科室"
							}else if(data.rows[i].yblxmc!=null){
								seriesData.push({value:data.rows[i].cn,name:data.rows[i].yblxmc});
								legendData.push(data.rows[i].yblxmc);
								name="标本类型"
							}else if(data.rows[i].flmc!=null){
								seriesData.push({value:data.rows[i].cn,name:data.rows[i].flmc});
								legendData.push(data.rows[i].flmc);
								name="销售分类"
							}else if(data.rows[i].zflmc!=null){
								seriesData.push({value:data.rows[i].cn,name:data.rows[i].zflmc});
								legendData.push(data.rows[i].zflmc);
								name="销售子分类"
							}else if(data.rows[i].bgmb!=null){
								seriesData.push({value:data.rows[i].cn,name:data.rows[i].bgmb});
								legendData.push(data.rows[i].bgmb);
								name="报告模板"
							}else if(data.rows[i].db!=null){
								seriesData.push({value:data.rows[i].cn,name:data.rows[i].db});
								legendData.push(data.rows[i].db);
								name="合作伙伴"
							}else if(data.rows[i].jcxmmc!=null){
								seriesData.push({value:data.rows[i].cn,name:data.rows[i].jcxmmc});
								legendData.push(data.rows[i].jcxmmc);
								name="检测项目"
							}else if(data.rows[i].jsrq!=null){
								seriesData.push({value:data.rows[i].cn,name:data.rows[i].jsrq});
								legendData.push(data.rows[i].jsrq);
								name="接收日期"
							}else if(data.rows[i].bgrq!=null){
								seriesData.push({value:data.rows[i].cn,name:data.rows[i].bgrq});
								legendData.push(data.rows[i].bgrq);
								name="报告日期"
							}
						}
						var myChart = echarts.init(document.getElementById('echarts_custom_statis_sjxxStatis'));
						 myChart.setOption({
							 legend: {
							        data:legendData
							    	},
							series : [
							     {
							    	 	name: name,
							            radius : ['0','70%'],
							            data:seriesData
							        }
							   ]
						    });
					}
				}
			});
		}else {
			$.alert("请选择统计字段！");
		}
	}
}
 
/**
 * 判断选中的是哪种统计图
 * @returns
 */
$(".tjtys").click(function(){
	 checktjtys();
})

/**
 * 加载柱状图
 */
var loadBarStatis=function(){
	 var myChart = echarts.init(document.getElementById('echarts_custom_statis_sjxxStatis'));
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
			     toolbox: {
			         feature: {
			             dataView: {show: true, readOnly: false},
			             magicType: {show: true, type: ['line', 'bar']},
			             restore: {show: true},
			             saveAsImage: {show: true}
			         }
			     },
			     grid: {
			        containLabel: true,
			    },
			    xAxis : [
			        {
			            type : 'category',
			            data : []
			        }
			    ],
			    yAxis : [
			        {
			            type : 'value'
			        }
			    ],
			    series : []
			}
	 	myChart.setOption(pieoption,true);
}

/**
 * 加载圆饼统计图
 */
var loadPierStatis=function(){
	 var myChart = echarts.init(document.getElementById('echarts_custom_statis_sjxxStatis'));
	 var pieoption = {
			 tooltip : {
			        trigger: 'item',
			        formatter: "{a} <br/>{b} : {c} ({d}%)"
			    },
			    legend: {
			        x : 'center',
			        y : 'top',
			        data:[]
			    },
			    toolbox: {
			        show : true,
			        feature : {
			            mark : {show: true},
			            dataView : {show: true, readOnly: false},
			            magicType : {
			                show: true,
			                type: ['pie', 'funnel']
			            },
			            restore : {show: true},
			            saveAsImage : {show: true}
			        }
			    },
			    calculable : true,
			    series : [
			    	 {	 
			    		 name: [],
			             type: 'pie',
			             radius : [],
			             center: ['50%', '60%'],
			             data:[],
			             itemStyle: {
			                 emphasis: {
			                     shadowBlur: 10,
			                     shadowOffsetX: 0,
			                     shadowColor: 'rgba(0, 0, 0, 0.5)'
			                 }
			             },
			             label: {
			                 formatter:'{b}：{c}({d}% )'
			             }
			         }
			    ]
			}
	 	myChart.setOption(pieoption,true);
}
function checktjtys(){
	$(".tjtys").each(function(){
		 if(this.checked==true){
			 if(this.value=="Table"){
				 $("#tjzd_div").children().eq(1).show(); 
				 $("#tjzd_div").children().eq(2).show();
				 $("#tjzd_div").children().eq(3).show();
				 $("#tjzd_div").children().eq(4).show();
				 $("#tjzd_div").children().eq(5).show();
				 $("#tjzq_div").show();
				 tjtys=this.value;
				 $("#tbale_list").show();
				 $("#echarts_custom_statis_sjxxStatis").hide();
			 }else if(this.value=="Bar"){
				 $("#tjzd_div").children().eq(1).show();
				 $("#tjzd_div").children().eq(2).hide();
				 $("#tjzd_div").children().eq(2).children().children("option").eq(0).prop("selected", "selected");  
				 $("#tjzd_div").children().eq(2).children().trigger("chosen:updated");
				 $("#tjzd_div").children().eq(3).hide();
				 $("#tjzd_div").children().eq(3).children().children("option").eq(0).prop("selected", "selected");  
				 $("#tjzd_div").children().eq(3).children().trigger("chosen:updated");
				 $("#tjzd_div").children().eq(4).hide();
				 $("#tjzd_div").children().eq(4).children().children("option").eq(0).prop("selected", "selected");  
				 $("#tjzd_div").children().eq(4).children().trigger("chosen:updated");
				 $("#tjzd_div").children().eq(5).hide();
				 $("#tjzd_div").children().eq(5).children().children("option").eq(0).prop("selected", "selected");  
				 $("#tjzd_div").children().eq(5).children().trigger("chosen:updated");
				 $("#tjzq_div").show();
				 tjtys=this.value;
				 $("#tbale_list").hide();
				 $("#echarts_custom_statis_sjxxStatis").show();
			 }else if(this.value=="Pie"){
				 $("#tjzd_div").children().eq(1).hide();
				 $("#tjzd_div").children().eq(1).children().children("option").eq(0).prop("selected", "selected");  
				 $("#tjzd_div").children().eq(1).children().trigger("chosen:updated");
				 $("#tjzd_div").children().eq(2).hide();
				 $("#tjzd_div").children().eq(2).children().children("option").eq(0).prop("selected", "selected");  
				 $("#tjzd_div").children().eq(2).children().trigger("chosen:updated");
			     $("#tjzd_div").children().eq(3).hide();
				 $("#tjzd_div").children().eq(3).children().children("option").eq(0).prop("selected", "selected");  
				 $("#tjzd_div").children().eq(3).children().trigger("chosen:updated");
				 $("#tjzd_div").children().eq(4).hide();
				 $("#tjzd_div").children().eq(4).children().children("option").eq(0).prop("selected", "selected");  
				 $("#tjzd_div").children().eq(4).children().trigger("chosen:updated");
				 $("#tjzd_div").children().eq(5).hide();
				 $("#tjzd_div").children().eq(5).children().children("option").eq(0).prop("selected", "selected");  
				 $("#tjzd_div").children().eq(5).children().trigger("chosen:updated");
				 $("#tjzq_div").hide();
				 tjtys=this.value;
				 $("#tbale_list").hide();
				 $("#echarts_custom_statis_sjxxStatis").show();
			 }
		 }
	 })
}

/**
 * 列表
 */
function custom_TableInit(param,titjStr,tjzd,tjcol,mainselect,maingroup,selectzd,groupzd){
	$.ajax({
		url:"/ws/statistics/getTableCustomStatis",
		type:"post",
		dataType:"json",
		data:{"param":param,"tjtj":JSON.stringify(titjStr),"tjzd":JSON.stringify(tjzd),"mainselects":JSON.stringify(mainselect),"maingroups":JSON.stringify(maingroup),"selectzds":JSON.stringify(selectzd),"groupzds":JSON.stringify(groupzd)},
		success:function(data){
			if(data.rows!=null){
				var tmp=JSON.stringify(tjzd).replace("ks", "ksmc").replace("yblx", "yblxmc").replace("fl", "flmc").replace("zfl", "zflmc").replace("jcxmid", "jcxmmc").replace("[", "").replace("]", "").trim().split(",");
				var table="<table class='table table-bordered'>";
					table+="<thead><tr>";
					for (var i = 0; i < tjcol.length; i++) {
						table+="<th>"+tjcol[i]+"</th>";
					}
					table+="<th>个数</th>";
					table+="</thead></tr>";
					table+="<tbody>";
					for (var i = 0; i < data.rows.length; i++) {
						table+="<tr>";
						for (var j = 0; j < tmp.length; j++) {
							var zd = "data.rows[i]."+tmp[j].substring(1,tmp[j].length-1);
							 if(eval(zd)!="" && eval(zd)!=null){
								 table+="<td>"+eval(zd)+"</td>"; 
							 }else{
								 table+="<td>&nbsp;</td>";
							 }
						}
						table+="<td>"+data.rows[i].cn+"</td>";
						table+="</tr>";
					}
					table+="</tbody>";
					table+="<table>";
			}
			$("#statisDiv #tbale_list").html(table);
		}
		
	})
}

$(function(){
	checktjtys();
	laydate.render({
			  elem: '#custom_jsrqstart'
				  ,theme: '#5BC0DE'
		});
	laydate.render({
		      elem: '#custom_jsrqend'
		    	  ,theme: '#5BC0DE'
		});
	laydate.render({
			elem: '#custom_bgrqstart'
				,theme: '#5BC0DE'
		});
	laydate.render({
			elem: '#custom_bgrqend'
				,theme: '#5BC0DE'
		});
    jQuery('#customDiv .chosen-select').chosen({width: '100%'});
});
