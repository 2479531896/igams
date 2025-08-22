var yhid = $("#calendar_attendanceForm #yhid").val();

	// 循环轮播到上一个项目（向下 0-11）
$(".prev-slide").click(function(){
	$(".carousel-item").each(function(){
		if($(this).hasClass("active")){
			if($(this).children("input").eq(0).val()==0){
				$("#calendar_attendanceForm #year_list").val(parseInt($("#calendar_attendanceForm #year_list").val())-1);
			}
		 }
	})
	$("#calendar_attendanceForm #myCarousel").carousel('prev');
});
// 循环轮播到下一个项目（向上 0-1）
$(".next-slide").click(function(){
	$(".carousel-item").each(function(){
		if($(this).hasClass("active")){
			if($(this).children("input").eq(0).val()==11){
				$("#calendar_attendanceForm #year_list").val(parseInt($("#calendar_attendanceForm #year_list").val())+1);
			}
		 }
		})
	$("#calendar_attendanceForm #myCarousel").carousel('next');
	
});
//轮播之后的回调
$('#calendar_attendanceForm #myCarousel').on('slid.bs.carousel', function () {
    // 执行一些动作...
	//判断当前是哪个月份被选中
	$(".carousel-item").each(function(){
	if($(this).hasClass("active")){
		//获取选中月份下标，（0-11）
		var mon=$(this).children("input").eq(0).val();
		//获取选中月份的英文名称
		var monmc=$(this).children("span").eq(0).text();
		//获取选中月份的中文名称
		var cn_mc=$(this).children("span").eq(1).text();
		//把选中的月份显示在页面上方
		$("#calendar_attendanceForm #calendar-title").children("span").eq(0).text(monmc);
		 var my_date = new Date();
		 var my_year
		 if($("#calendar_attendanceForm #year_list").val()!=null || $("#calendar_attendanceForm #year_list").val()!=""){
			 my_year=$("#calendar_attendanceForm #year_list").val();
		 }else{
			 my_year=my_date.getFullYear();//获取年份
		 }
		 var my_month = my_date.getMonth(); //获取月份，一月份的下标为0
		 var my_day = my_date.getDate();//获取当前日期
		 //给页面右上边加上年份
		 $("#calendar_attendanceForm #calendar-title").children("span").eq(1).text(my_year+"年");
		 //调用日历创建方法
		CalendarInit(my_year,mon,my_day);
	}
})
})

/**
 * 日历初始化
 * @param my_year
 * @param my_month
 * @param my_day
 * @returns
 */
function CalendarInit(my_year,my_month,my_day){
	var variable=null;
	//日历初始化时，在h1里边显示对应的年和月
	$(".carousel-item").removeClass("active");
	$("#calendar_attendanceForm #monlist").children("div").eq(my_month).addClass("active");
	$("#calendar_attendanceForm #calendar-title").children("span").eq(0).text( $("#calendar_attendanceForm #monlist").children("div").eq(my_month).children("span").eq(0).text());
	$("#calendar_attendanceForm #calendar-title").children("span").eq(1).text(my_year+"年");
	$("#calendar_attendanceForm #year_list").val(my_year);
	//获取当月第一天，param : my_year,my_month, 1
	var now=new Date(my_year,my_month,1); 
	//当月第一天星期几[0,1,2,3,4,5,6 : SUN，MON，TUE，WED，THU，FRI，SAT]
    var firstday=now.getDay(); 
    //各月份的总天数,用于区分闰年和平年
    var m_days=new Array(31,28+is_leap(my_year),31,30,31,30,31,31,30,31,30,31); 
    //表格所需要行数
    var tr_str=Math.ceil((m_days[my_month] + firstday)/7); 
    //获取到当前的时间，用于判断日历中当前的日期改变背景颜色
    var To_date = new Date();
    var To_year = To_date.getFullYear();//获取年份
    var To_month = To_date.getMonth(); //获取月份，一月份的下标为0
    var To_day = To_date.getDate();//获取当前日期
    //日历加载之前先清空掉原有的日历数据
    $("#calendar_attendanceForm #tbody").html("");
    $.ajax({
  		 url:$('#calendar_attendanceForm #urlPrefix').val()+'/attendance/attendance/pagedataKqxxByYh',
  		 type:'post',
  		 dataType:'JSON',
  		 async : false,
  		 data:{"access_token":$("#ac_tk").val(),"rq":my_year+"-"+("0"+(parseInt(my_month)+1)).slice(-2),"yhid":yhid},
  		 success:function(data){
  			variable=data;
  		 }
	})
    //通过表格需要的行数，进行遍历，确定tr 的 个数
    for (var i = 0; i < tr_str; i++) {
		var  tr="<tr>";
	 		 tr+="</tr>";
	       $("#calendar_attendanceForm #tbody").append(tr);
	       //通过周进行循环，
	     //通过周进行循环，
			for (var k=0;k<7;k++) {
				 //单元格自然序列号
				  var idx=i*7+k;
				//计算日期
				  var date_str=idx-firstday+1;
				 //小于等于0的日期 和大于当前月份的的日期，进行为空显示（不行去掉，需要占位置）
				 if(date_str<=0 || date_str>m_days[my_month]){
					 var td="<td>";
					     td+="</td>";
				 }else{//正常日期进行td 排列
					 //通过当前的日期查询阴历，24节气，返回值为json格式。param://my_year,my_month,date_str(2019,9-24),月份为实际月份，并非下标月份，所以要加一
				   	 var lunar = calendar.solar2lunar(my_year,parseInt(my_month)+1,date_str);
				   	 //判断系统时间和传过来的参数的年月日都相等，确定今天的日期，背景颜色为#FFF6C2，并给予备注 ：今天
				   	 if(my_year==To_year && my_month==To_month && date_str==To_day){
				   		var td="<td id='td"+date_str+"' data-bs-toggle='tooltip' title='阴历"+lunar.IMonthCn+"，"+lunar.gzYear+"年，"+lunar.gzMonth+"月，"+lunar.gzDay+"日，"+lunar.astro+"' style='background-color:rgb(195,195,195)' mouseenter ='showMore(this)' mouseleave='hideMore(this)'>";
				   		td+="<div style='color:#F08080;margin-right:20px;float:right;'>今天</div>";
				   	 }else{
				   		var td="<td id='td"+date_str+"' data-bs-toggle='tooltip' title='阴历"+lunar.IMonthCn+"，"+lunar.gzYear+"年，"+lunar.gzMonth+"月，"+lunar.gzDay+"日，"+lunar.astro+"'>";
				   	 }
				   	 //显示公历时间
				   	 td+="<span style='display:inline-block;font-size:25px;padding-left:10px;'>"+date_str+"</span>";	
				   	 //如果节气（24节气），不为空的时候，优先显示节气，不显示阴历
				   	 if(lunar.Term!= null){
				   		td+="<span style='display:inline-block;font-size:14px;color:#1691FC;margin-left:10px;'>"+lunar.Term+"</span>";
				   	 }else{
				   		 //如果不是节气，显示阴历日期
				   		td+="<span style='display:inline-block;font-size:14px;color:#A8A8A8;margin-left:10px;'>"+lunar.IDayCn+"</span>";
				   	 }
				   	 //当前日期‘2019-9-1’ 改为‘2019-09-01’
				   	 var rq=my_year+"-"+("0"+(parseInt(my_month)+1)).slice(-2)+"-"+("0"+date_str).slice(-2);
				   	 if(variable.yhkqxxDtos.length>0){
				   		 $(variable.yhkqxxDtos).each(function(i){
							if (this.kqjg==null){
								this.kqjg = "";
							}
							 if(this.rq==rq){
								 td+="<div>";
								 td +="<span style='font-size: 16px;' id='span_"+(i+1)+"' value='"+this.kqjg+"' data-bs-toggle='tooltip' title='"+this.kqjg+"' >"+this.kqjg+"</span>";
								 td+="</div>";

							}
						})
				   	 }
				   	 td+="</td>";
			   	}
				$("#calendar_attendanceForm #tbody").children("tr").eq(i).append(td); //这里的索引从0开始，所以第三个用2
				setcolor();
			}
	}
	changeBackgroundColor()
    //加载bootstrap title；
    $(function () { $("[data-bs-toggle='tooltip']").tooltip(); });
}
function changeBackgroundColor() {
	for (var i = 1; i < 32; i++) {
		var tdDoc = $('#calendar_attendanceForm #td'+i)[0];
		var kqjg = $('#calendar_attendanceForm #span_'+i).text();
		if (tdDoc&&kqjg){
			if (kqjg.indexOf('早退')!=-1){
				tdDoc.setAttribute("style","background-color:rgb(255,255,204)");
			}else if ((kqjg.indexOf('加班')!=-1&&kqjg.indexOf('外勤')==-1)||kqjg.indexOf('假')!=-1){
				tdDoc.setAttribute("style","background-color:rgb(255,204,153)");
			}else if (kqjg.indexOf('迟到')!=-1){
				tdDoc.setAttribute("style","background-color:rgb(204,255,204)");
			}else if (kqjg.indexOf('缺卡')!=-1){
				tdDoc.setAttribute("style","background-color:rgb(255,128,128)");
			}else if (kqjg.indexOf('外勤')!=-1){
				tdDoc.setAttribute("style","background-color:rgb(204,255,255)");
			}else if (kqjg.indexOf('旷工')!=-1){
				tdDoc.setAttribute("style","background-color:rgb(255,153,204)");
			}else if (kqjg.indexOf('出差')!=-1){
				tdDoc.setAttribute("style","background-color:rgb(112,146,190)");
			}else if (kqjg.indexOf('调休')!=-1){
				tdDoc.setAttribute("style","background-color:rgb(255,0,255)");
			}
		}
	}
}
//判断是否是闰年
function is_leap(year) { 
	   return (year%100==0?res=(year%400==0?1:0):res=(year%4==0?1:0));
	} 
var month_name =["January","Febrary","March","April","May","June","July","Auguest","September","October","November","December"];//每个月的名称

//日历初始化
function load_init(){
	//页面加载时，默认获取当前的时间
    var my_date = new Date();
    var my_year = my_date.getFullYear();//获取年份
    var my_month = my_date.getMonth(); //获取月份，一月份的下标为0
    var my_day = my_date.getDate();//获取当前日期
	CalendarInit(my_year,my_month,my_day);//日历创建方法
}

//回到今天
$("#calendar_attendanceForm #to_today").click(function(){
	load_init();
})
//循环颜色
function setcolor(){
	let colorMap=["#99ffcc","#00ccff","#CAA2FB","#FFD700","#FF7F00","#FF00FF"];
		$(".spancolor").each(function(index){
			var id=this.id;
				let num=index%6;
				$(this).css("background",colorMap[num]);
		});
}
function colorRGBtoHex(color) {
    var rgb = color.split(',');
    var r = parseInt(rgb[0].split('(')[1]);
    var g = parseInt(rgb[1]);
    var b = parseInt(rgb[2].split(')')[0]);
    var hex = "#calendar_attendanceForm #" + ((1 << 24) + (r << 16) + (g << 8) + b).toString(16).slice(1);
    return hex;
 }
function getDate(){

    var date = new Date();
    var year = date.getFullYear();
    var month = date.getMonth()+1;    //js从0开始取 
    var date1 = date.getDate(); 
    var hour = date.getHours(); 
    var minutes = date.getMinutes(); 
    var second = date.getSeconds();
    return year+"-"+month+"-"+date1;
}
$(function(){
	laydate.render({
		   elem: '#calendar_attendanceForm #year_list'
		  ,type: 'year'
		  ,btns: ['confirm']
	      ,theme: 'grid'
	      ,done: function(value, date, endDate){
    		$(".carousel-item").each(function(){
    			if($(this).hasClass("active")){
    				//获取选中月份下标，（0-11）
    				var mon=$(this).children("input").eq(0).val();
    				//获取选中月份的英文名称
    				var monmc=$(this).children("span").eq(0).text();
    				//获取选中月份的中文名称
    				var cn_mc=$(this).children("span").eq(1).text()
    				//把选中的月份显示在页面上方
    				$("#calendar_attendanceForm #calendar-title").children("span").eq(0).text(monmc);
    				$("#calendar_attendanceForm #calendar-title").children("span").eq(1).text(date.year+"年");
    				 //调用日历创建方法
    				CalendarInit(date.year,mon,date.date);
    			}
    		})
	       }
		});
	load_init();
})