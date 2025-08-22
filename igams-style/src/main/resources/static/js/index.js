$(function () {

    //BEGIN CALENDAR
    $("#my-calendar").zabuto_calendar({
        language: "en"
    });
    //END CALENDAR

    //BEGIN TO-DO-LIST
    /*$('.todo-list').slimScroll({
        "width": '100%',
        "height": '250px',
        "wheelStep": 30
    });*/
    $( ".sortable" ).sortable({ axis: 'x' });
    $( ".sortable" ).disableSelection();
    //END TO-DO-LIST

    //BEGIN COUNTER FOR SUMMARY BOX
    counterNum($(".profit h4 span:first-child"), 189, 112, 1, 30);
    counterNum($(".income h4 span:first-child"), 636, 812, 1, 50);
    counterNum($(".task h4 span:first-child"), 103, 155 , 1, 100);
    counterNum($(".visit h4 span:first-child"), 310, 376, 1, 500);
    function counterNum(obj, start, end, step, duration) {
        $(obj).html(start);
        setInterval(function(){
            var val = Number($(obj).html());
            if (val < end) {
                $(obj).html(val+step);
            } else {
                clearInterval();
            }
        },duration);
    }
    //END COUNTER FOR SUMMARY BOX
    
    
//echarts
	
	// 路径配置
    require.config({
        paths: {
            echarts: 'http://10.71.33.161:8080/zfstyle_v5/js/echarts'
        }
    });
    
    // 使用
    require(
        [
            'echarts',
            'echarts/chart/bar', // 使用柱状图就加载bar模块，按需加载
            'echarts/chart/line'
        ],
        function (ec) {
            // 基于准备好的dom，初始化echarts图表
            var myChart = ec.init(document.getElementById('area-chart-spline')); 
            
            var option = {
            	    title : {
            	        text: '测试测试测试',
            	        subtext: '纯属虚构'
            	    },
            	    tooltip : {
            	        trigger: 'axis'
            	    },
            	    legend: {
            	        data:['first','second','third']
            	    },
            	    toolbox: {
            	        show : true,
            	        feature : {
            	            mark : {show: true},
            	            dataView : {show: true, readOnly: false},
            	            magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
            	            restore : {show: true},
            	            saveAsImage : {show: true}
            	        }
            	    },
            	    calculable : true,
            	    xAxis : [
            	        {
            	            type : 'category',
            	            boundaryGap : false,
            	            data : ['周一','周二','周三','周四','周五','周六','周日']
            	        }
            	    ],
            	    yAxis : [
            	        {
            	            type : 'value'
            	        }
            	    ],
            	    series : [
            	        {
            	            name:'first',
            	            type:'line',
            	            smooth:true,
            	            itemStyle: {normal: {areaStyle: {type: 'default'}}},
            	            data:[10, 12, 21, 54, 260, 830, 710]
            	        },
            	        {
            	            name:'second',
            	            type:'line',
            	            smooth:true,
            	            itemStyle: {normal: {areaStyle: {type: 'default'}}},
            	            data:[30, 182, 434, 791, 390, 30, 10]
            	        },
            	        {
            	            name:'third',
            	            type:'line',
            	            smooth:true,
            	            itemStyle: {normal: {areaStyle: {type: 'default'}}},
            	            data:[1320, 1132, 601, 234, 120, 90, 20]
            	        }
            	    ]
            	};
    
            // 为echarts对象加载数据 
            myChart.setOption(option); 
        }
    );
    
    // echarts结束    
    $(".glyphicon-zoom-in").click(function(){
		$('.tab-content').addClass('enlarge-iframe');
		if($('.tab-content').find('.glyphicon-zoom-out').length == 0){
			$('.tab-pane.active').prepend('<div style="width: 100px;margin: auto;"><span class="glyphicon glyphicon-zoom-out" style="font-size: 200%;cursor: pointer;" ></span></div>');							
		}
	});
	$(document).off('click','.glyphicon-zoom-out').on('click','.glyphicon-zoom-out',function(){
		$(this).remove();
		$('.tab-content').removeClass('enlarge-iframe');
	});
    
    if($('#resize').length==0){
    	var resizeIcon='<div id="resize" class="hidden-xs"><span class="glyphicon glyphicon-resize-full"></span></div>';
    	$('body').append(resizeIcon);
    }
    
    $(document).off('click','#resize').on('click','#resize',function(){
    	if($(this).find('.glyphicon-resize-full').length>0){
    		$('.tab-pane').addClass('full-page');
	    	$(this).find('.glyphicon-resize-full').addClass('glyphicon-resize-small').removeClass('glyphicon-resize-full');
	    	$('body').addClass('page-full-resize');
    	}else{
    		$('.tab-pane').removeClass('full-page');
	    	$(this).find('.glyphicon-resize-small').addClass('glyphicon-resize-full').removeClass('glyphicon-resize-small');
	    	$('body').removeClass('page-full-resize');
    	}
    }).off('hover','.mega-menu-dropdown').on('hover','.mega-menu-dropdown',function(){
    	if($(this).hasClass('open') && $('body').find('.all-shade').length==0){
    		$('body').append('<div class="all-shade"></div>');
    	}
    }).off('click','.all-shade').on('click','.all-shade',function(){
    	$('body').find('.open').removeClass('open');
    }).off('mousemove','.all-shade').on('mousemove','.all-shade',function(){
    	if($('body').find('.open').length==0){
    		$('.all-shade').remove();
    	}
    }).off('mouseenter','#resize').on('mouseenter','#resize',function(){
    	if($('body').find('.all-shade2').length==0){
    		$('body').append('<div class="all-shade2"></div>');
    	}
    	$('#resize').draggable();
    	
    }).off('mouseout','#resize').on('mouseout','#resize',function(){
    	$('.all-shade2').remove();
    }).off('mouseleave','.mega-menu-dropdown .dropdown-menu,.mega-menu-dropdown').on('mouseleave','.mega-menu-dropdown .dropdown-menu,.mega-menu-dropdown',function(){
    	//上导航折叠面板
    	$(this).closest('.mega-menu-dropdown').removeClass('open');
    });
    
//    function resizeSize(){
//    	var width=$(window).width();
//    	 //分辨率小于1200的电脑端时导航启用小导航
//    	if(width<1200  && width>767){
//    		$('body').addClass('menu-collapse');
//    	}
//        //仅在非手机端使用tab
//    	if(width>767){
//    		$('#tabs').tabs({
//        		sortable : true,
//        		monitor	 : '#sidebar,.horizontal-menu',
//        	});
//    	}
//    	if(width<768){
//    		$('body').removeClass('menu-collapse');
//    	}
//    }
//    resizeSize();
//    $(window).resize(function(){
//    	resizeSize();
//    });

    
    //设置iframe的高度
    var iHeight=$(window).height-100;
    $('.embed-responsive.embed-responsive-16by9').height(iHeight);
    
});

