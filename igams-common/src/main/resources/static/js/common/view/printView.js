var t_img; // 定时器
var isLoad = true; // 控制变量（判断图片是否 加载完成）

isImgLoad(function(){//判断全部打印图片加载完成
    window.print();
    // 加载完成
});
//判断图片加载的函数
function isImgLoad(callback){
    // 查找打印图
    if ($('.printImg').length==0){
        isLoad = false;
    }
    // 为true，没有发现为0的。加载完毕
    if(isLoad){
        clearTimeout(t_img); // 清除定时器
        // 回调函数
        callback();
        // 为false，因为找到了没有加载完成的图，将调用定时器递归
    }else{
        isLoad = true;
        t_img = setTimeout(function(){
            isImgLoad(callback); // 递归扫描
        },500); // 我这里设置的是500毫秒就扫描一次，可以自己调整
    }
}