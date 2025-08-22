//扇形统计图
var dom = document.getElementById("container");
var myChart = echarts.init(dom);
var app = {};
var option;
option = {
    tooltip: {
        trigger: 'item'
    },
    series: [
        {
            name: '访问来源',
            type: 'pie',
            radius: '60px',
            data: [
                {value: 1048, name: 'Spike-in'},
                {value: 735, name: 'Homo'},
                {value: 580, name: 'Bacteria'},
                {value: 484, name: 'Fungi'},
                {value: 300, name: 'viruses'},
                {value: 300, name: 'Parasite'},
                {value: 300, name: 'Others'},
                {value: 300, name: 'Unmapped'}
            ],
            emphasis: {
                itemStyle: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            }
        }
    ]
};

if (option && typeof option === 'object') {
    myChart.setOption(option);
}


//跳转详情页
function getDetailPage(jcdw) {
    $("#statistics_laboratory").load("/wechat/statistics/pageStatisticsLaboratoryStatisticsDetail?jcdw="+jcdw);

}
function getBackStatisticsLaboratory() {
    $("#statistics_laboratory_detial").load("/wechat/statistics/pageStatisticsLaboratoryStatistics");

}

~(function ($, window, document, undefined) {
    class statistics_laboratory_scroll {
        constructor(eles, opts) {
            this.$eles = eles;
            opts = opts || {};
            this.defaults = {
                mode: 'CSS',
                cssSpeed: 100,
                jsSpeed: 'slow',
            };
            this.options = $.extend(true, {}, this.defaults, opts);
            this.options.jsSpeed = this.handleJsSpeed(this.options.jsSpeed);
            this.init();
        }
        init() {
            this.handleEve();
        }
        handleJsSpeed(sp) {
            switch (sp) {
                case 'slow':
                    return 50;
                case 'normal':
                    return 30;
                case 'fast':
                    return 15;
            }
        }
        handleEve() {
            const _this = this;
            this.$eles.each(function (i, domEle) {
                _this.cloneNode(domEle);
                _this.initValue(domEle);
                _this.wrapDiv(domEle);
                _this.createKeyframes();
                if (_this.options.mode === 'CSS') {
                    _this.moveByCss(domEle);
                    _this.handleHoverByCss(domEle);
                } else {
                    _this.moveByJs(domEle);
                    _this.handleHoverByJs(domEle);
                }
            });
        }
        cloneNode(ele) {
            $(ele).children().clone().appendTo($(ele));
        }
        initValue(ele) {
            $(ele).css({
                margin: 0,
                padding: 0
            });
            ele.num = 0;
            const o = $(ele).parents(":hidden").eq($(ele).parents(":hidden").length - 1);
            o.css({
                display: 'block'
            });
            // ele.h = parseInt($(ele).outerHeight(true) / 2);
            // To prevent the father setting display: flex; from affecting the height of the child element
            let sum = 0;
            $(ele).children().each(function (i, item) {
                sum += $(item).outerHeight(true);
            });
            ele.h = parseInt(sum / 2);
            o.css({
                display: 'none'
            });
        }
        wrapDiv(ele) {
            $(ele).wrap($(`<div style="height: ${ele.h}px; overflow: hidden; padding: 0">`));
        }
        createKeyframes() {
            const runkeyframes = `@keyframes IFER_MOVE {
                100%{
                    transform: translateY(-50%);
                }
            }`;
            const style = document.createElement('style');
            style.type = 'text/css';
            style.innerHTML = runkeyframes;
            document.querySelector('head').appendChild(style);
        }
        moveByCss(ele) {
            $(ele).css({
                animation: `IFER_MOVE ${this.options.cssSpeed}s linear infinite`
            });
        }
        handleHoverByCss(ele) {
            $(ele).hover(function () {
                $(this).css('animation-play-state', 'paused');
            }, function () {
                $(this).css('animation-play-state', 'running');
            });
        }
        moveByJs(ele) {
            clearInterval(ele.timer);
            ele.timer = setInterval(() => {
                if (Math.abs(ele.num) === ele.h) {
                    ele.num = 0;
                } else {
                    $(ele).css('transform', 'translateY(' + ele.num + 'px)');
                }
                ele.num--;
            }, this.options.jsSpeed);
        }
        handleHoverByJs(ele) {
            const _this = this;
            $(ele).hover(function () {
                clearInterval(ele.timer);
            }, function () {
                _this.moveByJs(ele);
            });
        }
    }
    $.fn.statistics_laboratory_scroll = function (options) {
        new statistics_laboratory_scroll(this, options);
    };
})(jQuery, window, document);

$(".statistics_laboratory_scroll").statistics_laboratory_scroll();

/*=======================
调用方法：
传递参数方法如下：
对象1：banner最大容器====================必填
对象2：banner======>按钮父容器============必填
对象5：banner滚动时间==================>可选项=======>默认为2000
对象6：是否需要自动轮播需要==========true============不需要false:必填
=============================*/
function bannerListFn(a,b,e,f){
    var $bannerMaxWapDom=a;
    var windowWidth=$(window).width()-100;
    var timeShow=0;
    var array=0;
    var timeOff=0;

    var imgPos=$bannerMaxWapDom.find("ul").find("li");

    var cloneOne=imgPos.first().clone();
    $bannerMaxWapDom.find("ul").append(cloneOne);
    $bannerMaxWapDom.find("li").width(windowWidth);
    var liWidth=imgPos.width();
    var liLength=$bannerMaxWapDom.find("li").length;
    $bannerMaxWapDom.find("ul").width(liWidth*(liLength+1));

    var $imgBtnList=b;

    setTimeout(function(i){
        timeShow++;
        (timeShow == 1) ? $bannerMaxWapDom.find("ul").fadeIn() : $bannerMaxWapDom.find("ul").hide();
    },20);

    (e === undefined) ? e=2000 : e=e;

    function imgListBtn (){

        for (var i=0; i < liLength-1; i++ ){
            $imgBtnList.append("<span></span>");
        }

        $imgBtnList.children().eq(0).addClass("current");

        $imgBtnList.children().click(function(){
            var index=$(this).index();
            array=index;
            bannerImgList(index);
            $imgBtnList.children().eq(array).addClass("current").siblings().removeClass("current");

        });

    }

    imgListBtn();

    function bannerImgList(a){
        $bannerMaxWapDom.find("ul").animate({left: -a*windowWidth},400)
    }

    function setTime(){
        timeOff=setInterval(function(){
            array++;
            move();
        },e)
    }

    (f) ? setTime() : setTime;


    function move(){
        if (array == liLength){
            $bannerMaxWapDom.find("ul").css({left:0});
            array=1;
        }

        if (array == -1){
            $bannerMaxWapDom.find("ul").css({left:-liWidth*(liLength-1)});
            array=liLength-2
        }

        $bannerMaxWapDom.find("ul").stop(true).animate({
            left:-array*liWidth
        });

        (array == liLength-1) ? $imgBtnList.children().eq(0).addClass("current").siblings().removeClass("current") : $imgBtnList.children().eq(array).addClass("current").siblings().removeClass("current");
    }
    $bannerMaxWapDom.hover(function(){
        clearInterval(timeOff);
    },function(){(f) ? setTime() : setTime;});
}

bannerListFn(
    $(".banner"),
    $(".img-btn-list"),
    8000,
    true
);