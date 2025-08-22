
set_width:false：设置你内容的宽度 值可以是像素或者百分比
set_height:false：设置你内容的高度 值可以是像素或者百分比
horizontalScroll:Boolean：是否创建一个水平滚动条 默认是垂直滚动条 值可为:true(创建水平滚动条) 或 false
scrollInertia:Integer：滚动的惯性值 在毫秒中 使用0可以无滚动惯性 (滚动惯性可以使区块滚动更加平滑)
scrollEasing:String：滚动动作类型 查看 jquery UI easing 可以看到所有的类型
mouseWheel:String/Boolean：鼠标滚动的支持 值为:true.false,像素 默认的情况下 鼠标滚动设置成像素值 填写false取消鼠标滚动功能
mouseWheelPixels:Integer：鼠标滚动中滚动的像素数目 值为以像素为单位的数值
autoDraggerLength:Boolean：根据内容区域自动调整滚动条拖块的长度 值:true,false
scrollButtons:{ enable:Boolean }：是否添加 滚动条两端按钮支持 值:true,false
scrollButtons:{ scrollType:String }：滚动按钮滚动类型 值:”continuous”(当你点击滚动控制按钮时断断续续滚动) “pixels”(根据每次点击的像素数来滚动) 点击这里可以看到形象的例子
scrollButtons:{ scrollSpeed:Integer }：设置点击滚动按钮时候的滚动速度(默认 20) 设置一个更高的数值可以更快的滚动
scrollButtons:{ scrollAmount:Integer }：设置点击滚动按钮时候每次滚动的数值 像素单位 默认 40像素
advanced:{ updateOnBrowserResize:Boolean }：根据百分比为自适应布局 调整浏览器上滚动条的大小 值:true,false 设置 false 如果你的内容块已经被固定大小
advanced:{ updateOnContentResize:Boolean }：自动根据动态变换的内容调整滚动条的大小 值:true,false 设置成 true 将会不断的检查内容的长度并且据此改变滚动条大小 建议除非必要不要设置成 true 如果页面中有很多滚动条的时候 它有可能会产生额外的移出 你可以使用 update 方法来替代这个功能
advanced:{ autoExpandHorizontalScroll:Boolean }：自动扩大水平滚动条的长度 值:true,false 设置 true 你可以根据内容的动态变化自动调整大小 可以看Demo
advanced:{ autoScrollOnFocus:Boolean }：是否自动滚动到聚焦中的对象 例如表单使用类似TAB键那样跳转焦点 值:true false
callbacks:{ onScrollStart:function(){} }：使用自定义的回调函数在滚动时间开始的时候执行 具体请看Demo
callbacks:{ onScroll:function(){} }：自定义回调函数在滚动中执行 Demo 同上
callbacks:{ onTotalScroll:function(){} }：当滚动到底部的时候调用这个自定义回调函数 Demo 同上
callbacks:{ onTotalScrollBack:function(){} }：当滚动到顶部的时候调用这个自定义回调函数 Demo 同上
callbacks:{ onTotalScrollOffset:Integer }：设置到达顶部或者底部的偏移量 像素单位
callbacks:{ whileScrolling:function(){} }：当用户正在滚动的时候执行这个自定义回调函数
callbacks:{ whileScrollingInterval:Integer }：设置调用 whileScrolling 回调函数的时间间隔 毫秒单位
