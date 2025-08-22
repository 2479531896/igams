var qxqg="取消采购";
var shzt="";//当前单据审核状态
var t_map=[];
var zlyqlist=[];
// 请购车显示字段
var qgcColumnsArray = [
    {
        title: '序号',
        formatter: function (value, row, index) {
            return index+1;
        },
        titleTooltip:'序号',
        width: '3%',
        align: 'left',
        visible:true
    },{
        field: 'wlid',
        title: '物料ID',
        width: '8%',
        align: 'left',
        visible: false
    },{
        field: 'wlbm',
        title: '物料编码',
        titleTooltip:'物料编码',
        width: '15%',
        align: 'left',
        formatter:wlbmformat,
        visible: true,
        sortable: true
    }, {
        field: 'wlmc，gg',
        title: '生产商与规格',
        titleTooltip:'生产商与规格',
        width: '15%',
        align: 'left',
        formatter:scs_ggformat,
        visible: true
    }, {
        field: 'wlkcl',
        title: '库存量',
        titleTooltip:'库存量',
        width: '6%',
        align: 'left',
        formatter:wlkclformat,
        visible: true
    }, {
        field: 'sl,jldw',
        title: '数量',
        titleTooltip:'数量',
        width: '10%',
        align: 'left',
        formatter:slformat,
        visible: true
    }, {
        field: 'lbmc',
        title: '类别',
        titleTooltip:'类别',
        width: '10%',
        align: 'left',
        visible: false
    }, {
        field: 'wllbmc,wlzlbmc',
        title: '物料类别与子类别',
        titleTooltip:'物料类别与子类别',
        width: '20%',
        formatter:wllb_zlbformat,
        align: 'left',
        visible: true
    }, {
        field: 'wlzlbtcmc,ychh',
        title: '物料子类别统称与货号',
        titleTooltip:'物料子类别统称与货号',
        width: '15%',
        align: 'left',
        formatter:zlbtc_hhformat,
        visible: true
    },{
        field: 'bzqflg',
        title: '保质期标记',
        titleTooltip:'保质期标记',
        width: '6%',
        align: 'right',
        visible: false,
    },{
        field: 'bctj',
        title: '保存条件',
        titleTooltip:'保存条件',
        width: '10%',
        align: 'left',
        visible: false
    },{
        field: 'aqkc',
        title: '安全库存量',
        titleTooltip:'安全库存量',
        width: '10%',
        align: 'left',
        visible: false
    },{
        field: 'lrryxm',
        title: '申请人',
        width: '8%',
        align: 'left',
        visible: false
    },{
        field: 'jwlbm',
        title: '旧物料编码',
        width: '8%',
        align: 'left',
        formatter:wlbmformat,
        visible: false
    },{
        field: 'cpzch',
        title: '产品注册号',
        width: '8%',
        align: 'left',
        formatter:cpzchformat,
        visible: true
    },{
        field: 'qwrq',
        title: '物料使用日期',
        width: '10%',
        align: 'left',
        formatter:qwrqformat,
        visible: true
    },{
        field: 'bz',
        title: '备注',
        width: '15%',
        align: 'left',
        formatter:bzformat,
        visible: true
    },{
        field: 'fj',
        title: '附件',
        width: '3%',
        align: 'left',
        formatter:fjformat,
        visible: true
    },{
        field: 'cz',
        title: '操作',
        width: '5%',
        align: 'left',
        formatter:czformat,
        visible: true
    }];
// 请购新增，提交，复制显示字段
var qgmxColumnsArray = [
    {
        title: '序号',
        formatter: function (value, row, index) {
            return index+1;
        },
        titleTooltip:'序号',
        width: '3%',
        align: 'left',
        visible:true
    },{
        field: 'qgmxid',
        title: '请购明细ID',
        width: '2%',
        align: 'left',
        visible: false
    },{
        field: 'wlid',
        title: '物料ID',
        width: '8%',
        align: 'left',
        visible: false
    },{
        field: 'wlbm',
        title: '物料编码',
        titleTooltip:'物料编码',
        width: '15%',
        align: 'left',
        formatter:t_wlbmformat,
        visible: true,
        sortable: true,
    }, {
        field: 'scs，gg',
        title: '生产商与规格',
        titleTooltip:'生产商与规格',
        width: '15%',
        align: 'left',
        formatter:scs_ggformat,
        visible: true
    }, {
        field: 'wlkcl',
        title: '库存量',
        titleTooltip:'库存量',
        width: '6%',
        align: 'left',
        formatter:wlkclformat,
        visible: true
    }, {
        field: 'sl',
        title: '数量',
        titleTooltip:'数量',
        width: '8%',
        align: 'left',
        formatter:slformat,
        visible: true
    }, {
        field: 'lbmc',
        title: '类别',
        titleTooltip:'类别',
        width: '10%',
        align: 'left',
        visible: false
    }, {
        field: 'scs',
        title: '生产商',
        titleTooltip:'生产商',
        width: '8%',
        align: 'left',
        visible: true
    }, {
        field: 'wllbmc',
        title: '物料类别',
        titleTooltip:'物料类别',
        width: '10%',
        align: 'left',
        visible: true
    }, {
        field: 'wlzlbmc',
        title: '物料子类别',
        titleTooltip:'物料子类别',
        width: '10%',
        align: 'left',
        visible: true
    }, {
        field: 'wlzlbtcmc',
        title: '子类别统称',
        titleTooltip:'子类别统称',
        width: '5%',
        align: 'left',
        visible: true
    }, {
        field: 'ychh',
        title: '货号',
        titleTooltip:'货号',
        width: '20%',
        align: 'left',
        visible: false
    },{
        field: 'jldw',
        title: '单位',
        titleTooltip:'单位',
        width: '4%',
        align: 'left',
        visible: true
    },{
        field: 'bzqflg',
        title: '保质期标记',
        titleTooltip:'保质期标记',
        width: '6%',
        align: 'right',
        visible: false,
    },{
        field: 'bctj',
        title: '保存条件',
        titleTooltip:'保存条件',
        width: '10%',
        align: 'left',
        visible: false
    },{
        field: 'aqkc',
        title: '安全库存量',
        titleTooltip:'安全库存量',
        width: '10%',
        align: 'left',
        visible: false
    },{
        field: 'lrryxm',
        title: '申请人',
        width: '8%',
        align: 'left',
        visible: false
    },{
        field: 'jwlbm',
        title: '旧物料编码',
        width: '8%',
        align: 'left',
        visible: false
    },{
        field: 'cpzch',
        title: '产品注册号',
        width: '8%',
        align: 'left',
        formatter:cpzchformat,
        visible: true
    },{
        field: 'qwrq',
        title: '物料使用日期',
        width: '10%',
        align: 'left',
        formatter:qwrqformat,
        visible: true
    },{
        field: 'bz',
        title: '备注',
        width: '13%',
        align: 'left',
        formatter:bzformat,
        visible: true
    },{
        field: 'fj',
        title: '附件',
        width: '3%',
        align: 'left',
        formatter:fjformat,
        visible: fjvisible()
    },{
        field: 'qxqg',
        title: '审核',
        width: '3%',
        formatter:qxqgformat,
        align: 'left',
        visible: true
    },{
        field: 'zt',
        title: '状态',
        width: '5%',
        align: 'left',
        formatter:ztformat,
        visible: false
    },{
        field: 'cz',
        title: '操作',
        width: '5%',
        align: 'left',
        formatter:czformat,
        visible: true
    }];
// 请购采购部审核显示字段
var qxgjxgColumnsArray = [
    {
        title: '序号',
        formatter: function (value, row, index) {
            return index+1;
        },
        titleTooltip:'序号',
        width: '3%',
        align: 'left',
        visible:true
    },{
        field: 'wlbm',
        title: '物料编码',
        width: '15%',
        align: 'left',
        formatter:t_wlbmformat,
        sortable: true,
        visible: true
    }, {
        field: 'scs，gg',
        title: '生产商与规格',
        titleTooltip:'生产商与规格',
        width: '15%',
        align: 'left',
        formatter:scs_ggformat,
        visible: true
    },{
        field: 'wlzlbmc',
        title: '物料子类别',
        width: '8%',
        align: 'left',
        sortable: true,
        visible: true
    }, {
        field: 'wlzlbtcmc',
        title: '子类别统称',
        titleTooltip:'子类别统称',
        width: '6%',
        align: 'left',
        visible: true
    }, {
        field: 'wlkcl',
        title: '库存量',
        titleTooltip:'库存量',
        width: '6%',
        align: 'left',
        formatter:wlkclformat,
        visible: true
    }, {
        field: 'sl',
        title: '数量',
        width: '8%',
        align: 'left',
        formatter:slformat,
        sortable: true,
        visible: true
    },{
        field: 'jldw',
        title: '单位',
        width: '5%',
        align: 'left',
        sortable: true,
        visible: true
    },{
        field: 'jg',
        title: '价格',
        width: '5%',
        align: 'left',
        formatter:jgformat,
        visible: true
    },{
        field: 'cpzch',
        title: '产品注册号',
        width: '8%',
        align: 'left',
        formatter:cpzchformat,
        visible: true
    },{
        field: 'qwdhrq',
        title: '物料使用日期',
        width: '7%',
        align: 'left',
        formatter:qwrqformat,
        visible: true
    },{
        field: 'bz',
        title: '备注',
        width: '13%',
        align: 'left',
        formatter:bzformat,
        sortable: true,
        visible: true
    },{
        field: 'qxqg',
        title: '拒绝	',
        width: '3%',
        formatter:qxqgformat,
        align: 'left',
        visible: true
    },{
        field: 'fj',
        title: '附件',
        width: '3%',
        align: 'left',
        formatter:fjformat,
        visible: fjvisible()
    },{
        field: 'zt',
        title: '状态',
        width: '5%',
        align: 'left',
        formatter:ztformat,
        sortable: true,
        visible: false
    }]
// 请购高级修改显示字段
var gjxgColumnsArray = [
    {
        title: '序号',
        formatter: function (value, row, index) {
            return index+1;
        },
        titleTooltip:'序号',
        width: '3%',
        align: 'left',
        visible:true
    },{
        field: 'wlbm',
        title: '物料编码',
        width: '15%',
        align: 'left',
        formatter:t_wlbmformat,
        sortable: true,
        visible: true
    },{
        field: 'wllbmc',
        title: '物料类别',
        width: '8%',
        align: 'left',
        sortable: true,
        visible: true
    },{
        field: 'wlzlbmc',
        title: '物料子类别',
        width: '8%',
        align: 'left',
        sortable: true,
        visible: true
    }, {
        field: 'wlzlbtcmc',
        title: '子类别统称',
        titleTooltip:'子类别统称',
        width: '6%',
        align: 'left',
        visible: true
    }, {
        field: 'wlkcl',
        title: '库存量',
        titleTooltip:'库存量',
        width: '6%',
        align: 'left',
        formatter:wlkclformat,
        visible: true
    }, {
        field: 'sl',
        title: '数量',
        width: '8%',
        align: 'left',
        formatter:slformat,
        sortable: true,
        visible: true
    },{
        field: 'jldw',
        title: '单位',
        width: '5%',
        align: 'left',
        sortable: true,
        visible: true
    },{
        field: 'jg',
        title: '价格',
        width: '5%',
        align: 'left',
        formatter:jgformat,
        visible: true
    },{
        field: 'cpzch',
        title: '产品注册号',
        width: '8%',
        align: 'left',
        formatter:cpzchformat,
        visible: true
    },{
        field: 'qwdhrq',
        title: '物料使用日期',
        width: '7%',
        align: 'left',
        formatter:qwrqformat,
        visible: true
    },{
        field: 'bz',
        title: '备注',
        width: '13%',
        align: 'left',
        formatter:bzformat,
        sortable: true,
        visible: true
    },{
        field: 'qxqg',
        title: '拒绝审批',
        width: '5%',
        formatter:qxqgformat,
        align: 'left',
        visible: true
    },{
        field: 'fj',
        title: '附件',
        width: '3%',
        align: 'left',
        formatter:fjformat,
        visible: fjvisible()
    },{
        field: 'zt',
        title: '状态',
        width: '5%',
        align: 'left',
        formatter:ztformat,
        sortable: true,
        visible: false
    },{
        field: 'cz',
        title: '操作',
        width: '5%',
        align: 'left',
        formatter:czformat,
        visible: true
    }]

//行政请购类型显示字段
var xzqgColumnsArray = [
    {
        title: '序号',
        formatter: function (value, row, index) {
            return index+1;
        },
        titleTooltip:'序号',
        width: '1%',
        align: 'left',
        visible:true
    },{
        field: 'wlmc',
        title: '物料名称',
        width: '5%',
        align: 'left',
        formatter:wlmcformat,
        sortable: true,
        visible: true
    },{
        field: 'hwbz',
        title: '规格型号',
        width: '5%',
        align: 'left',
        formatter:hwbzformat,
        sortable: true,
        visible: true
    }, {
        field: 'sl',
        title: '数量',
        width: '3%',
        align: 'left',
        formatter:slformat,
        sortable: true,
        visible: true
    },{
        field: 'hwjldw',
        title: '单位',
        width: '3%',
        align: 'left',
        formatter:hwjldwformat,
        sortable: true,
        visible: true
    },{
        field: 'jg',
        title: '价格',
        width: '5%',
        align: 'left',
        formatter:jgformat,
        visible: true
    },{
        field: 'qwrq',
        title: '期望日期',
        width: '4%',
        align: 'left',
        formatter:qwrqformat,
        sortable: false,
        visible: true
    },{
        field: 'bz',
        title: '备注',
        width: '8%',
        align: 'left',
        formatter:bzformat,
        sortable: true,
        visible: true
    },{
        field: 'fj',
        title: '附件',
        width: '3%',
        align: 'left',
        formatter:fjformat,
        visible: fjvisible()
    },{
        field: 'zt',
        title: '状态',
        width: '3%',
        align: 'left',
        formatter:ztformat,
        sortable: true,
        visible: false
    },{
        field: 'cz',
        title: '操作',
        width: '5%',
        align: 'left',
        formatter:czformat,
        visible: true
    }]
//服务请购类型显示字段
var fwqgColumnsArray = [
    {
        title: '序号',
        formatter: function (value, row, index) {
            return index+1;
        },
        titleTooltip:'序号',
        width: '1%',
        align: 'left',
        visible:true
    },{
        field: 'wlmc',
        title: '服务名称',
        width: '5%',
        align: 'left',
        formatter:wlmcformat,
        visible: true
    },{
        field: 'hwbz',
        title: '服务期限',
        width: '10%',
        align: 'left',
        formatter:fwqxformat,
        visible: true
    }, {
        field: 'sl',
        title: '数量',
        titleTooltip:'数量',
        width: '3%',
        align: 'left',
        formatter:slformat,
        visible: true
    },{
        field: 'jg',
        title: '价格',
        width: '5%',
        align: 'left',
        formatter:jgformat,
        visible: jgvisible()
    }, {
        field: 'hwyt',
        title: '服务用途',
        width: '6%',
        align: 'left',
        formatter:hwytformat,
        visible: true
    },{
        field: 'gys',
        title: '生产厂家',
        width: '8%',
        align: 'left',
        formatter:fw_gysformat,
        visible: true
    },{
        field: 'yq',
        title: '服务要求',
        width: '6%',
        align: 'left',
        formatter:fw_yqformat,
        visible: true
    },{
        field: 'sjjl',
        title: '审计结论',
        width: '6%',
        align: 'left',
        formatter:fw_sjjlformat,
        visible: true
    },{
        field: 'bz',
        title: '备注',
        width: '8%',
        align: 'left',
        formatter:bzformat,
        visible: true
    },{
        field: 'fj',
        title: '附件',
        width: '3%',
        align: 'left',
        formatter:fjformat,
        visible: true
    },{
        field: 'zt',
        title: '状态',
        width: '3%',
        align: 'left',
        formatter:ztformat,
        visible: false
    },{
        field: 'cz',
        title: '操作',
        width: '5%',
        align: 'left',
        formatter:czformat,
        visible: true
    }]

//设备请购审核显示字段
var sbqgshColumnsArray = [
    {
        title: '序号',
        formatter: function (value, row, index) {
            return index+1;
        },
        titleTooltip:'序号',
        width: '3%',
        align: 'left',
        visible:true
    },{
        field: 'qgmxid',
        title: '请购明细ID',
        width: '2%',
        align: 'left',
        visible: false
    },{
        field: 'wlid',
        title: '物料ID',
        width: '8%',
        align: 'left',
        visible: false
    },{
        field: 'wlbm',
        title: '物料编码',
        titleTooltip:'物料编码',
        width: '15%',
        align: 'left',
        formatter:t_wlbmformat,
        visible: true,
        sortable: true,
    }, {
        field: 'wlkcl',
        title: '库存量',
        titleTooltip:'库存量',
        width: '6%',
        align: 'left',
        formatter:wlkclformat,
        visible: true
    }, {
        field: 'sl',
        title: '数量',
        titleTooltip:'数量',
        width: '8%',
        align: 'left',
        formatter:slformat,
        visible: true
    },{
        field: 'jg',
        title: '价格',
        width: '5%',
        align: 'left',
        formatter:jgformat,
        visible: true
    }, {
        field: 'scs，gg',
        title: '生产商与规格',
        titleTooltip:'生产商与规格',
        width: '15%',
        align: 'left',
        formatter:scs_ggformat,
        visible: true
    },{
        field: 'qwrq',
        title: '期望到货日期',
        width: '8%',
        align: 'left',
        formatter:qwrqformat,
        sortable: false,
        visible: true
    }, {
        field: 'ychh',
        title: '货号',
        titleTooltip:'货号',
        width: '20%',
        align: 'left',
        visible: false
    },{
        field: 'jldw',
        title: '单位',
        titleTooltip:'单位',
        width: '4%',
        align: 'left',
        visible: true
    },{
        field: 'bzqflg',
        title: '保质期标记',
        titleTooltip:'保质期标记',
        width: '6%',
        align: 'right',
        visible: false,
    },{
        field: 'bctj',
        title: '保存条件',
        titleTooltip:'保存条件',
        width: '10%',
        align: 'left',
        visible: false
    },{
        field: 'aqkc',
        title: '安全库存量',
        titleTooltip:'安全库存量',
        width: '10%',
        align: 'left',
        visible: false
    },{
        field: 'lrryxm',
        title: '申请人',
        width: '8%',
        align: 'left',
        visible: false
    },{
        field: 'jwlbm',
        title: '旧物料编码',
        width: '8%',
        align: 'left',
        visible: false
    },{
        field: 'yq',
        title: '质量要求',
        width: '8%',
        align: 'left',
        formatter:sb_yqformat,
        visible: true
    },{
        field: 'wbyq',
        title: '维保要求',
        width: '8%',
        align: 'left',
        formatter:sb_wbyqformat,
        visible: true
    },{
        field: 'pzyq',
        title: '配置要求',
        width: '8%',
        align: 'left',
        formatter:sb_pzyqformat,
        visible: true
    },{
        field: 'cpzch',
        title: '产品注册号',
        width: '8%',
        align: 'left',
        formatter:cpzchformat,
        visible: true
    },{
        field: 'bz',
        title: '备注',
        width: '13%',
        align: 'left',
        formatter:bzformat,
        visible: true
    },{
        field: 'qxqg',
        title: '拒绝	',
        width: '6%',
        formatter:qxqgformat,
        align: 'left',
        visible: true
    },{
        field: 'fj',
        title: '附件',
        width: '3%',
        align: 'left',
        formatter:fjformat,
        visible: true
    },{
        field: 'zt',
        title: '状态',
        width: '5%',
        align: 'left',
        formatter:ztformat,
        visible: false
    },{
        field: 'cz',
        title: '操作',
        width: '5%',
        align: 'left',
        formatter:czformat,
        visible: true
    }]

//设备请购类型显示字段
var sbqgColumnsArray = [
    {
        title: '序号',
        formatter: function (value, row, index) {
            return index+1;
        },
        titleTooltip:'序号',
        width: '3%',
        align: 'left',
        visible:true
    },{
        field: 'qgmxid',
        title: '请购明细ID',
        width: '2%',
        align: 'left',
        visible: false
    },{
        field: 'wlid',
        title: '物料ID',
        width: '8%',
        align: 'left',
        visible: false
    },{
        field: 'wlbm',
        title: '物料编码',
        titleTooltip:'物料编码',
        width: '15%',
        align: 'left',
        formatter:t_wlbmformat,
        visible: true,
        sortable: true,
    }, {
        field: 'scs，gg',
        title: '生产商与规格',
        titleTooltip:'生产商与规格',
        width: '15%',
        align: 'left',
        formatter:scs_ggformat,
        visible: true
    }, {
        field: 'wlkcl',
        title: '库存量',
        titleTooltip:'库存量',
        width: '6%',
        align: 'left',
        formatter:wlkclformat,
        visible: true
    }, {
        field: 'sl',
        title: '数量',
        titleTooltip:'数量',
        width: '8%',
        align: 'left',
        formatter:slformat,
        visible: true
    },{
        field: 'qwrq',
        title: '期望到货日期',
        width: '8%',
        align: 'left',
        formatter:qwrqformat,
        sortable: false,
        visible: true
    }, {
        field: 'ychh',
        title: '货号',
        titleTooltip:'货号',
        width: '20%',
        align: 'left',
        visible: false
    },{
        field: 'jldw',
        title: '单位',
        titleTooltip:'单位',
        width: '4%',
        align: 'left',
        visible: true
    },{
        field: 'bzqflg',
        title: '保质期标记',
        titleTooltip:'保质期标记',
        width: '6%',
        align: 'right',
        visible: false,
    },{
        field: 'bctj',
        title: '保存条件',
        titleTooltip:'保存条件',
        width: '10%',
        align: 'left',
        visible: false
    },{
        field: 'aqkc',
        title: '安全库存量',
        titleTooltip:'安全库存量',
        width: '10%',
        align: 'left',
        visible: false
    },{
        field: 'lrryxm',
        title: '申请人',
        width: '8%',
        align: 'left',
        visible: false
    },{
        field: 'jwlbm',
        title: '旧物料编码',
        width: '8%',
        align: 'left',
        visible: false
    },{
        field: 'yq',
        title: '质量要求',
        width: '8%',
        align: 'left',
        formatter:sb_yqformat,
        visible: true
    },{
        field: 'wbyq',
        title: '维保要求',
        width: '8%',
        align: 'left',
        formatter:sb_wbyqformat,
        visible: true
    },{
        field: 'pzyq',
        title: '配置要求',
        width: '8%',
        align: 'left',
        formatter:sb_pzyqformat,
        visible: true
    },{
        field: 'cpzch',
        title: '产品注册号',
        width: '8%',
        align: 'left',
        formatter:cpzchformat,
        visible: true
    },{
        field: 'bz',
        title: '备注',
        width: '13%',
        align: 'left',
        formatter:bzformat,
        visible: true
    },{
        field: 'fj',
        title: '附件',
        width: '3%',
        align: 'left',
        formatter:fjformat,
        visible: true
    },{
        field: 'zt',
        title: '状态',
        width: '5%',
        align: 'left',
        formatter:ztformat,
        visible: false
    },{
        field: 'cz',
        title: '操作',
        width: '5%',
        align: 'left',
        formatter:czformat,
        visible: true
    }]

var ShoppingCar_TableInit=function(){
    var qglbdm=$("#shoppingCarForm #qglbdm").val();
    var url = $("#shoppingCarForm #url").val();
    if($("#shoppingCarForm #fwbj").val() != null && $("#shoppingCarForm #fwbj").val() != ""){
        url = $("#shoppingCarForm #fwbj").val()+url;
    }
    if(url.indexOf("?")>0){
        url=$('#shoppingCarForm #urlPrefix').val()+url+"&qglbdm="+qglbdm;
    }else{
        url=$('#shoppingCarForm #urlPrefix').val()+url+"?qglbdm="+qglbdm;
    }
    if($("#shoppingCarForm #requestName").val()!=null && $("#shoppingCarForm #requestName").val()!=""){
        //判断当前url中是否已经存在参数，
        if(url.indexOf("?")>0){
            url=url+"&requestName="+$("#shoppingCarForm #requestName").val();
        }else{
            url=url+"?requestName="+$("#shoppingCarForm #requestName").val();
        }
    }
    var flag=$("#shoppingCarForm #flag").val();
    var sortName=null;
    var uniqueId=null;
    var sortLastName=null;
    var columnsArray=null;
    if(qglbdm=="ADMINISTRATION"){
        sortName = "qgmx.xh";
        uniqueId = "hwmc";
        sortLastName="hwmc";
        columnsArray = xzqgColumnsArray;
    }else if(qglbdm=="SERVICE"){
        sortName = "qgmx.xh";
        uniqueId = "hwmc";
        sortLastName="hwmc";
        columnsArray = fwqgColumnsArray;
    }else if(qglbdm=="DEVICE"){
        if(flag=="qxgjxg"){
            sortName = "qgmx.xh";
            uniqueId = "qgmxid";
            sortLastName = "qgmx.qgmxid";
            columnsArray = sbqgshColumnsArray;
        }else{
            sortName = "qgmx.xh";
            uniqueId = "qgmxid";
            sortLastName = "qgmx.qgmxid";
            columnsArray = sbqgColumnsArray;
        }

    }else{
        if(flag=="qgc"){
            sortName = "qgc.lrsj";
            uniqueId = "wlid";
            sortLastName="qgc.wlid";
            columnsArray = qgcColumnsArray;
        }else if(flag=="qgmx"){
            sortName = "qgmx.xh";
            uniqueId = "qgmxid";
            sortLastName = "qgmx.qgmxid";
            columnsArray = qgmxColumnsArray;
        }else if(flag=="qxgjxg"){
            sortName = "qgmx.xh";
            uniqueId = "qgmxid";
            sortLastName = "qgmx.qgmxid";
            columnsArray = qxgjxgColumnsArray;
        }else if(flag=="gjxg"){
            sortName = "qgmx.xh";
            uniqueId = "qgmxid";
            sortLastName = "qgmx.qgmxid";
            columnsArray = gjxgColumnsArray;
        }
    }

    //获取质量类别和所属研发项目基础数据
    $.ajax({
        async: false,
        type:'post',
        url:$('#shoppingCarForm #urlPrefix').val()+"/purchase/purchase/pagedataGetMoreJcsj",
        cache: false,
        data: {"access_token":$("#ac_tk").val()},
        dataType:'json',
        success:function(data){
            //返回值
            zlyqlist=data.zlyqlist;
        }
    });
    var oTableInit=new Object();
    oTableInit.Init=function(){
        $("#shoppingCarForm #tb_list").bootstrapTable({
            url: url,         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#shoppingCarForm #toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            paginationShowPageGo: false,         //增加跳转页码的显示
            sortable: true,                     //是否启用排序
            sortName: sortName,				//排序字段
            sortOrder: "asc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 15,                       //每页的记录行数（*）
            pageList: [15, 30, 50, 100],        //可供选择的每页的行数（*）
            paginationPreText: '‹',				//指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',			//指定分页条中下一页按钮的图标或文字,这里是>
            search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: false,                  //是否显示所有的列
            showRefresh: false,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: false,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: uniqueId,                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            paginationDetailHAlign:' hidden',
            columns: columnsArray,
            onLoadSuccess:function(map){
                t_map=map;
                if(qglbdm!="DEVICE" && qglbdm!="MATERIAL" && qglbdm!="OUTSOURCE" && map.rows.length<=0){
                    var a=[];
                    t_map.rows.push(a);
                    $("#shoppingCarForm #tb_list").bootstrapTable('load',t_map);
                }
            },
            onLoadError:function(){
                alert("数据加载失败！");
            },
            onDblClickRow: function (row, $element) {
            },
            rowStyle: function (row, index) {
                //这里有5个取值代表5中颜色['active', 'success', 'info', 'warning', 'danger'];
                var strclass = "";
                if (row.zt == "15") {
                    strclass = 'danger';//还有一个active
                }
                else if (row.zt == "30") {
                    strclass = 'warning';
                }
                else if (row.zt == "80") {
                    strclass = 'success';
                }
                else {
                    return {};
                }
                return { classes: strclass }
            },
        });
        $("#shoppingCarForm #tb_list").colResizable({
            liveDrag:true,
            gripInnerHtml:"<div class='grip'></div>",
            draggingClass:"dragging",
            resizeMode:'fit',
            postbackSafe:true,
            partialRefresh:true
        })
    };
    oTableInit.queryParams=function(params){
        var map = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   // 页面大小
            pageNumber: (params.offset / params.limit) + 1,  // 页码
            access_token:$("#ac_tk").val(),
            sortName: params.sort,      // 排序列名
            sortOrder: params.order, // 排位命令（desc，asc）
            sortLastName: sortLastName, // 防止同名排位用
            sortLastOrder: "asc" // 防止同名排位用
            // 搜索框使用
            // search:params.search
        };
        return getShoppingSearchData(map);
    };
    return oTableInit;
}

//新增明细
function addnew(){
    var data=JSON.parse($("#shoppingCarForm #qgmx_json").val());
    var a={"index":data.length,"wlid":'',"cskz1":'',"cskz1":'',"sl":'',"qwrq":'',"bz":'',"wlbm":'',"qxqg":'','qgmxid':'','zt':'',"glid":'',"fjids":'',"sysl":'',"hwmc":'',"hwbz":'',"hwjldw":'',"sfrk":'1'};
    t_map.rows.push(a);
    data.push(a);
    $("#shoppingCarForm #qgmx_json").val(JSON.stringify(data));
    $("#shoppingCarForm #tb_list").bootstrapTable('load',t_map);
}

function cofirmaddwl(){
    var wlid=$('#shoppingCarForm #addwlid').val();
    //判断新增输入框是否有内容
    if(!$('#shoppingCarForm #addnr').val()){
        return false;
    }
    setTimeout(function(){ if(wlid != null && wlid != ''){
        $.ajax({
            type:'post',
            url:$('#shoppingCarForm #urlPrefix').val()+"/production/materiel/pagedataAddMaterOnShopping",
            cache: false,
            data: {"wlid":wlid,"access_token":$("#ac_tk").val()},
            dataType:'json',
            success:function(data){
                //返回值
                if(data.qgcglDto!=null && data.qgcglDto!=''){
                    if(data.qgcglDto.wlzt!='80'){
                        $.confirm("该物料未审核！");
                    }else{
                        var json=JSON.parse($("#shoppingCarForm #qgmx_json").val());
                        /*if(json!=null && json.length>0){
                            for(var i=0;i<json.length;i++){
                                if(json[i].wlid==data.qgcglDto.wlid){
                                    $.confirm("该物料已请购！");
                                    return false;
                                }
                            }
                        }*/
                        t_map.rows.push(data.qgcglDto);
                        var sz={"index":'',"wlid":'',"cskz1":'',"cskz2":'',"sl":'',"qwrq":'',"bz":'',"wlbm":'',"qxqg":'','qgmxid':'','zt':'',"glid":'',"fjids":''};
                        sz.index=json.length;
                        sz.wlid=data.qgcglDto.wlid;
                        sz.wlbm=data.qgcglDto.wlbm;
                        sz.cskz1=data.qgcglDto.cskz1;
                        sz.cskz2=data.qgcglDto.cskz2;
                        sz.zt='80';
                        json.push(sz);
                        $("#shoppingCarForm #qgmx_json").val(JSON.stringify(json));
                        $("#shoppingCarForm #tb_list").bootstrapTable('load',t_map);
                        $('#shoppingCarForm #addwlid').val("");
                        $('#shoppingCarForm #addnr').val("");
                    }
                }else{
                    $.confirm("该物料不存在!");
                }
            }
        });
    }else{
        var addnr = $('#shoppingCarForm #addnr').val();
        if(addnr != null && addnr != ''){
            $.confirm("请选择物料信息!");
        }
    }}, '200')
}
/**
 * 组装列表查询条件(未使用)
 * @param map
 * @returns
 */
function getShoppingSearchData(map){
    var cxtj=$("#shoppingCarForm #cxtj").val();
    var cxnr=$.trim(jQuery('#shoppingCarForm #cxnr').val());
    if(cxtj=="0"){
        map["wlbm"]=cxnr;
    }else if(cxtj=="1"){
        map["wlmc"]=cxnr;
    }else if(cxtj=="2"){
        map["scs"]=cxnr;
    }else if(cxtj=="3"){
        map["ychh"]=cxnr;
    }
    return map;
}
function searchShoppingResult(isTurnBack){
    if(isTurnBack){
        $('#shoppingCarForm #tb_list').bootstrapTable('refresh',{pageNumber:1});
    }else{
        $('#shoppingCarForm #tb_list').bootstrapTable('refresh');
    }
}

function jgvisible(){
    var flag=$("#shoppingCarForm #flag").val();
    if(flag=="qxgjxg"){
        return true;
    }
    return false;
}

/**
 * 物料名称格式化，请购类型为设备，服务，行政时自己输入
 * @param value
 * @param row
 * @param index
 * @returns
 */
function wlmcformat(value,row,index){
    if(row.hwmc == null){
        row.hwmc = "";
    }
    var html = "<input id='hwmc_"+index+"' value='"+row.hwmc+"' text='"+row.hwmc+"' name='hwmc_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'hwmc\')\" autocomplete='off'></input>";
    return html;
}

/**
 * 服务生产厂家格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function fw_gysformat(value,row,index){
    if(row.gys == null){
        row.gys = "";
    }
    var html = "<input id='gys_"+index+"' value='"+row.gys+"' text='"+row.gys+"' name='gys_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'gys\')\" autocomplete='off'></input>";
    return html;
}

/**
 * 设备质量要求
 * @param value
 * @param row
 * @param index
 * @returns
 */
function sb_yqformat(value,row,index){
    var html="";
    if(zlyqlist!=null && zlyqlist.length>0){
        html="<select name='yq' onchange=\"changezlyq('"+index+"')\" class='form-control chosen-select' id='yq_"+index+"' >";
        for(var i=0;i<zlyqlist.length;i++){
            if(row.yq){
                if(row.yq==zlyqlist[i].csid){
                    html+="<option selected='true' id='"+zlyqlist[i].csid+"' value='"+zlyqlist[i].csid+"' index='"+i+"'>"+zlyqlist[i].csmc+"</option>";
                }else{
                    html+="<option id='"+zlyqlist[i].csid+"' value='"+zlyqlist[i].csid+"' index='"+i+"'>"+zlyqlist[i].csmc+"</option>";
                }
            }else{
                html+="<option id='"+zlyqlist[i].csid+"' value='"+zlyqlist[i].csid+"' index='"+i+"'>"+zlyqlist[i].csmc+"</option>";
            }
        }
        html+="</select>";
    }
    return html;
}

function changezlyq(index){
    var zlyq=$("#yq_"+index+"  option:selected").val();
    var zlyqmc=$("#yq_"+index+"  option:selected").text();
    var data=JSON.parse($("#shoppingCarForm #qgmx_json").val());
    if(data!=null && data.length>0){
        for(var i=0;i<data.length;i++){
            if(data[i].index==index){
                data[i].yq=zlyq;
                data[i].zlyqmc=zlyqmc;
                t_map.rows[index].yq=zlyq;
                t_map.rows[index].zlyqmc=zlyqmc;
            }
        }
    }
    $("#shoppingCarForm #qgmx_json").val(JSON.stringify(data));
    $("#shoppingCarForm #tb_list").bootstrapTable('load',t_map);
}

/**
 * 维保要求
 * @param value
 * @param row
 * @param index
 * @returns
 */
function sb_wbyqformat(value,row,index){
    if(row.wbyq == null){
        row.wbyq = "";
    }
    var html = "<input id='wbyq_"+index+"' value='"+row.wbyq+"' text='"+row.wbyq+"' name='wbyq_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'wbyq\')\" autocomplete='off' readonly='readonly' onFocus=\"showDiv('"+index+"',this,\'wbyq\')\"></input>";
    return html;
}

/**
 * 配置要求
 * @param value
 * @param row
 * @param index
 * @returns
 */
function sb_pzyqformat(value,row,index){
    if(row.pzyq == null){
        row.pzyq = "";
    }
    var html = "<input id='pzyq_"+index+"' value='"+row.pzyq+"' text='"+row.pzyq+"' name='pzyq_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'pzyq\')\" autocomplete='off' readonly='readonly' onFocus=\"showDiv('"+index+"',this,\'pzyq\')\"></input>";
    return html;
}


/**
 * 服务要求格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function fw_yqformat(value,row,index){
    if(row.yq == null){
        row.yq = "";
    }
    var html = "<input id='yq_"+index+"' value='"+row.yq+"' text='"+row.yq+"' name='yq_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' readonly='readonly' onblur=\"checkSum('"+index+"',this,\'yq\')\" autocomplete='off' onFocus=\"showDiv('"+index+"',this,\'yq\')\"></input>";
    return html;
}

/**
 * 审计结论格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function fw_sjjlformat(value,row,index){
    if(row.sjjl == null){
        row.sjjl = "";
    }
    var html = "<input id='sjjl_"+index+"' value='"+row.sjjl+"' text='"+row.sjjl+"' name='sjjl_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' readonly='readonly' onblur=\"checkSum('"+index+"',this,\'sjjl\')\" autocomplete='off' onFocus=\"showDiv('"+index+"',this,\'sjjl\')\"></input>";
    return html;
}

/**
 * 服务用途格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function hwytformat(value,row,index){
    if(row.hwyt == null){
        row.hwyt = "";
    }
    var html = "<input id='hwyt_"+index+"' value='"+row.hwyt+"' text='"+row.hwyt+"' name='hwyt_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'hwyt\')\" autocomplete='off' readonly='readonly' onFocus=\"showDiv('"+index+"',this,\'hwyt\')\"></input>";
    return html;
}

/**
 * 规格型号格式化，请购类型为设备，服务，行政时自己输入
 * @param value
 * @param row
 * @param index
 * @returns
 */
function hwbzformat(value,row,index){
    if(row.hwbz == null){
        row.hwbz = "";
    }
    var html = "<input id='hwbz_"+index+"' value='"+row.hwbz+"' name='hwbz_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'hwbz\')\" autocomplete='off'></input>";
    return html;
}

/**
 * 规格型号格式化，请购类型为设备，服务，行政时自己输入
 * @param value
 * @param row
 * @param index
 * @returns
 */
function hwjldwformat(value,row,index){
    if(row.hwjldw == null){
        row.hwjldw = "";
    }
    var html = "<input id='hwjldw_"+index+"' value='"+row.hwjldw+"' name='hwjldw_"+index+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'hwjldw\')\" autocomplete='off'></input>";
    return html;
}

/**
 * 状态格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function ztformat(value,row,index){
    var html="";
    if(row.zt=='15'){
        html="<span id='mxzt_"+index+"' cskz='15' style='color:red;'>拒绝审批</span>";
    }else if(row.zt=='30'){
        html="<span id='mxzt_"+index+"' cskz='30' style='color:orange;'>取消采购</span>";
    }else if(row.zt=='80'){
        html="<span id='mxzt_"+index+"' cskz='80' style='color:green;'>正常采购</span>";
    }else{
        html="<span id='mxzt_"+index+"' cskz='80' style='color:green;'>正常采购</span>";
    }
    return html;
}

/**
 * 附件格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function fjformat(value,row,index){
    var html = "<div id='fj_"+index+"'>";
    if ($("#shoppingCarForm #qgmx_json").val()!=null && $("#shoppingCarForm #qgmx_json").val() != ''){
        var data=JSON.parse($("#shoppingCarForm #qgmx_json").val());
        if(data[index].fjids!=null && data[index].fjids!=''){
            html += "<input type='hidden' name='mxfj_"+index+"' value='"+data[index].fjids+"'/>";
        }else{
            html += "<input type='hidden' name='mxfj_"+index+"'/>";
        }
    }else {
        html += "<input type='hidden' name='mxfj_"+index+"'/>";
    }
    if(row.fjbj == "0"){
        html += "<a href='javascript:void(0);' onclick='uploadShoppingFile(\"" + index + "\")' >是</a>";
    }else{
        html += "<a href='javascript:void(0);' onclick='uploadShoppingFile(\"" + index + "\")' >否</a>";
    }
    html += "</div>";
    return html;
}
/**
 * 附件显示格式化
 * @returns
 */
function fjvisible(){
    if($("#shoppingCarForm #fwbj").val()!=null && $("#shoppingCarForm #fwbj").val()!=""){
        return false;
    }else{
        return true;
    }
}

/**
 * 操作格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function czformat(value,row,index){
    var html = "<span style='margin-left:5px;' class='btn btn-default' title='移出采购车' onclick=\"delShoppingCarMater('" + index + "',event)\" >删除</span>";
    return html;
}

/**
 * 数量格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function slformat(value,row,index){
    if(row.sl == null){
        row.sl = "";
    }
    if(row.jldw == null){
        row.jldw = "";
    }
    //如果为服务请购，数量默认为1
    if($("#shoppingCarForm #qglbdm").val()=="SERVICE"){
        if(row.sl==null || row.sl=='')
            row.sl='1';
    }
    var html = "<input id='sl_"+index+"' value='"+row.sl+"' name='sl_"+index+"' style='width:80%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'sl\')\" autocomplete='off'></input>"+row.jldw;
    return html;
}

/**
 * 价格格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function jgformat(value,row,index){
    if(row.jg==null){
        row.jg="";
    }
    var html="";
    html = "<input id='jg_"+index+"' value='"+row.jg+"' name='jg_"+index+"' style='width:65%;border-top-left-radius:5px;border-bottom-left-radius:5px;border:1px solid #D5D5D5;' validate='{djNumber:true}' autocomplete='off' onblur=\"checkSum('"+index+"',this,\'jg\')\"></input><button type='button' class='glyphicon glyphicon-chevron-up' style='width:35%;top:0;font-weight:100;float:right;height:24px;border-top-right-radius:5px;border-bottom-right-radius:5px;border:0.2px solid black;' onclick=\"showImg(\'echarts_qggl\','"+row.wlid+"',\'jg\')\"></button>";
    return html;
}

/**
 * 验证数量格式(五位小数)
 * @param value
 * @param element
 * @returns
 */
jQuery.validator.addMethod("djNumber", function (value, element){
    if(value && !/^\d+(\.\d{1,5})?$/.test(value)){
        $.error("请填写正确单价格式，可保留五位小数!");
    }
    return this.optional(element) || /^\d+(\.\d{1,5})?$/.test(value);
},"请填写正确格式，可保留五位小数。");
/**
 * 产品注册号格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function cpzchformat(value,row,index){
    if(row.cpzch == null){
        row.cpzch = "" ;
    }
    var html="<input id='cpzch_"+index+"' name='cpzch_"+index+"' value='"+row.cpzch+"' validate='{required:true}' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'cpzch\')\" autocomplete='off'></input>";
    return html;
}

/**
 * 备注格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function bzformat(value,row,index){
    if(row.bz == null){
        row.bz = "" ;
    }
    var html="<input id='bz_"+index+"' name='bz_"+index+"' value='"+row.bz+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'bz\')\" autocomplete='off'></input>";
    return html;
}

/**
 * 服务期限格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function fwqxformat(value,row,index){
    if(row.hwbz == null){
        row.hwbz = "" ;
    }
    var html="<input id='hwbz_"+index+"' name='hwbz_"+index+"' value='"+row.hwbz+"' style='width:100%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'hwbz\')\" autocomplete='off'></input>";
    return html;
}

/**
 * 物料使用日期格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function qwrqformat(value,row,index){
    if(row.qwrq==null){
        row.qwrq="";
    }
    var html="<input id='qwrq_"+index+"' qgmxid='"+row.qgmxid+"' name='qwrq_"+index+"' class='qwdhrq' validate='{required:true}'  value='"+row.qwrq+"'  style='width:85%;border-radius:5px;border:1px solid #D5D5D5;' onblur=\"checkSum('"+index+"',this,\'qwrq\')\"></input><span class='glyphicon glyphicon-pencil' style='font-size:13px;color:#AAAAAA;width:10%;cursor:pointer;margin-left:3px;' title='复制当前日期并替换所有'  onclick=\"copyTime('"+index+"')\" autocomplete='off'><span>";
    setTimeout(function() {
        laydate.render({
            elem: '#shoppingCarForm #qwrq_'+index
            ,theme: '#2381E9'
            ,min: 1
            ,btns: ['clear', 'confirm']
            ,done: function(value, date, endDate){
                t_map.rows[index].qwrq=value;
                var data=JSON.parse($("#shoppingCarForm #qgmx_json").val())
                var xzqgmx=$("#shoppingCarForm #xzqgmx").val();
                if(xzqgmx && xzqgmx!='undefined'){
                    for(var i=0;i<data.length;i++){
                        if(xzqgmx==data[i].qgmxid){
                            data[i].qwrq=value;
                        }
                    }
                }else{
                    data[index].qwrq=value;
                }
                $("#shoppingCarForm #qgmx_json").val(JSON.stringify(data));
            }
        });
    }, 100);
    return html;
}

/**
 * 生产商与规格二合一
 * @param value
 * @param row
 * @param index
 * @returns
 */
function scs_ggformat(value,row,index){
    if(row.gg == null){
        row.gg = "暂无";
    }
    if (row.scs == null) {
        row.scs = "暂无";
    }

    var html = row.scs+"<hr style='width: 80%;margin: 0 auto;border: 0;height: 0;border-top: 1px solid rgba(0, 0, 0, 0.1);border-bottom: 1px solid rgba(255, 255, 255, 0.3);'>"+row.gg;

    return html;
}

/**
 * 子类别统称与货号二合一
 * @param value
 * @param row
 * @param index
 * @returns
 */
function zlbtc_hhformat(value,row,index){
    if(row.wlzlbtcmc == null){
        row.wlzlbtcmc = "暂无";
    }
    if (row.hh == null) {
        row.hh = "暂无";
    }

    var html = row.wlzlbtcmc+"<hr style='width: 80%;margin: 0 auto;border: 0;height: 0;border-top: 1px solid rgba(0, 0, 0, 0.1);border-bottom: 1px solid rgba(255, 255, 255, 0.3);'>"+row.hh;

    return html;
}

/**
 * 物料类别与子类别二合一
 * @param value
 * @param row
 * @param index
 * @returns
 */
function wllb_zlbformat(value,row,index){
    if(row.wllbmc == null){
        row.wllbmc = "暂无";
    }
    if (row.wlzlbmc == null) {
        row.wlzlbmc = "暂无";
    }

    var html = row.wllbmc+"<hr style='width: 80%;margin: 0 auto;border: 0;height: 0;border-top: 1px solid rgba(0, 0, 0, 0.1);border-bottom: 1px solid rgba(255, 255, 255, 0.3);'>"+row.wlzlbmc;

    return html;
}

/**
 * 复制当前日期并替换全部
 * @param wlid
 * @returns
 */
function copyTime(index){
    var dqrq=$("#shoppingCarForm #qwrq_"+index).val();
    if(dqrq==null || dqrq==""){
        $.confirm("请先选择日期！");
    }else{
        for(var i=0;i<t_map.rows.length;i++){
            $("#shoppingCarForm #qwrq_"+i).val(dqrq);
        }
    }
    var data=JSON.parse($("#shoppingCarForm #qgmx_json").val());
    if(data!=null && data.length>0){
        for(var j=0;j<data.length;j++){
            data[j].qwrq=dqrq;
            t_map.rows[j].qwrq=dqrq;
        }
    }
    $("#shoppingCarForm #qgmx_json").val(JSON.stringify(data));
}

/**
 * 物料编码格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function wlbmformat(value,row,index){
    var html="";
    if(row.wlbm!=null && row.wlbm!=''){
        html="<span>"+row.wlbm+"</span><button type='button' class='glyphicon glyphicon-chevron-up' style='font-weight:100;float:right;width:35px;height:30px;border-radius:5px;border:0.5px solid black;' onclick=\"showImg(\'echarts_qggl\','"+row.wlid+"',\'wlbm\')\"></button>"
            +"<hr style='width: 80%;margin: 0 auto;border: 0;height: 0;border-top: 1px solid rgba(0, 0, 0, 0.1);border-bottom: 1px solid rgba(255, 255, 255, 0.3);'>"+'<span title='+row.wlmc+'>'+row.wlmc+'</span>';
    }
    return html;
}
/**
 * 物料库存格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function wlkclformat(value,row,index){
    var html = "";
    if($('#ac_tk').length > 0){
        html += "<a href='javascript:void(0);' onclick=\"showWlkcInfo('"+row.wlid+"')\">"+row.wlkcl+"</a>";
    }else{
        html += row.wlbm;
    }
    return html;
}
function showWlkcInfo(wlid){
    var sign="";
    if ($("#auditAjaxForm #business_url").val()==''||$("#auditAjaxForm #business_url").val()==null){
        sign ="0";
    }else {
        sign = "1";
    }
    var url=$("#shoppingCarForm #urlPrefix").val()+"/production/materiel/pagedataViewMaterKcInfo?wlid="+wlid+"&sign="+sign+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'物料库存信息',viewWlkcConfig);
}
var viewWlkcConfig = {
    width		: "1000px",
    height		: "500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
/**
 * 物料编码格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function t_wlbmformat(value,row,index){
    var html = "";
    if($('#ac_tk').length > 0){
        html += "<a href='javascript:void(0);' onclick=\"queryByWlbm('"+row.wlid+"')\">"+row.wlbm+"</a>"
            +"<hr style='width: 80%;margin: 0 auto;border: 0;height: 0;border-top: 1px solid rgba(0, 0, 0, 0.1);border-bottom: 1px solid rgba(255, 255, 255, 0.3);'>"+'<span title='+row.wlmc+'>'+row.wlmc+'</span>';
    }else{
        html += row.wlbm;
    }
    return html;
}
function queryByWlbm(wlid){
    var url=$("#shoppingCarForm #urlPrefix").val()+"/production/materiel/pagedataViewMater?wlid="+wlid+"&access_token=" + $("#ac_tk").val();
    $.showDialog(url,'物料信息',viewWlConfig);
}
var viewWlConfig = {
    width		: "800px",
    height		: "500px",
    offAtOnce	: true,  //当数据提交成功，立刻关闭窗口
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

/**
 * 取消请购格式化
 * @param value
 * @param row
 * @param index
 * @returns
 */
function qxqgformat(value,row,index){
    var html="";
    if(shzt=='10'){//无法触发点击事件
        if(row.qxqg!='1'){//若被取消请购了，则不需要进行审批操作
            if(row.zt=='15'){
                html="<label class='jj'><input name='sfjj' class='sfjj' id='sfjj"+row.qgmxid+"' type='checkbox' qgmxid='"+row.qgmxid+"' checked onclick=\"refuseAudit('"+row.qgmxid+"','"+index+"')\">　拒绝<label>";
            }else{
                html="<label class='jj'><input name='sfjj' class='sfjj' id='sfjj"+row.qgmxid+"' type='checkbox' qgmxid='"+row.qgmxid+"' onclick=\"refuseAudit('"+row.qgmxid+"','"+index+"')\">　拒绝<label>";
            }
        }
    }

    return html;

}

function refuseAudit(qgmxid,index){
    var checkstatus=$("#shoppingCarForm #sfjj"+qgmxid).prop("checked");
    var data=JSON.parse($("#shoppingCarForm #qgmx_json").val());
    for(var i=0;i<data.length;i++){
        if(data[i].qgmxid==qgmxid){
            if(checkstatus==true){
                data[i].zt='15';
                t_map.rows[index].zt='15';
            }else{
                data[i].zt='80';
                t_map.rows[index].zt='80';
            }
        }
    }

    $("#shoppingCarForm #qgmx_json").val(JSON.stringify(data));
    $("#shoppingCarForm #tb_list").bootstrapTable('load',t_map);
}

///**
// * 取消请购按钮点击事件
// * @param qgmxid
// * @param wlid
// * @returns
// */
//function cancelPurchase(qgmxid,index){
//	$("#shoppingCarForm  #qxqg_"+index).parents("td").html("<input type='hidden' id='qxqg_"+index+"' name='qxqg_"+index+"' value='1'></input><span class='btn btn-warning' title='采购' onclick=\"backPurchase('"+qgmxid+"','"+index+"')\">采购</span>");
//	$("#shoppingCarForm  #mxzt_"+index).parents("td").html("<span id='mxzt_"+index+"' cskz='30' style='color:orange;'>取消采购</span>");
//	var data=JSON.parse($("#shoppingCarForm #qgmx_json").val());
//    for(var i=0;i<data.length;i++){
//        if(data[i].qgmxid==qgmxid){
//            data[i].zt='30';
//            data[i].qxqg='1';
//        }
//    }
//    $("#shoppingCarForm #qgmx_json").val(JSON.stringify(data));
//}
///**
// * 采购按钮钮点击事件
// * @param qgmxid
// * @param wlid
// * @returns
// */
//function backPurchase(qgmxid,index){
//	$("#shoppingCarForm  #qxqg_"+index).parents("td").html("<input type='hidden' id='qxqg_"+index+"' name='qxqg_"+index+"' value='0'></input><span class='btn btn-danger' title='取消' onclick=\"cancelPurchase('"+qgmxid+"','"+index+"')\">取消</span>");
//	$("#shoppingCarForm  #mxzt_"+index).parents("td").html("<span id='mxzt_"+index+"' cskz='80' style='color:green;'>正常采购</span>");
//	var data=JSON.parse($("#shoppingCarForm #qgmx_json").val());
//    for(var i=0;i<data.length;i++){
//        if(data[i].qgmxid==qgmxid){
//            data[i].zt='80';
//            data[i].qxqg='0';
//        }
//    }
//    $("#shoppingCarForm #qgmx_json").val(JSON.stringify(data));
//}

/**
 * 失去焦点事件(修改列表参数)
 * @param id
 * @param e
 * @param zdmc
 * @param bj
 * @returns
 */
function checkSum(index,e,zdmc){
    var data=JSON.parse($("#shoppingCarForm #qgmx_json").val());
    for(var i=0;i<data.length;i++){
        if(data[i].index == index){
            if(zdmc=='sl'){
                if (e.value){
                    if(!/^\d+(\.\d{1,2})?$/.test(e.value)){
                        $("#shoppingCarForm  #sl_"+index).val("");
                        $.error("请填写正确数量格式，可保留两位小数!");
                    }
                }
                data[i].sl=e.value;
                t_map.rows[i].sl=e.value;
            }else if(zdmc=='jg'){
                data[i].jg=e.value;
                t_map.rows[i].jg=e.value;
            }else if(zdmc=='qwrq'){
                data[i].qwrq=e.value;
                t_map.rows[i].qwrq=e.value;
            }else if(zdmc=='bz'){
                data[i].bz=e.value;
                t_map.rows[i].bz=e.value;
            }else if(zdmc=='hwmc'){
                data[i].hwmc=e.value;
                t_map.rows[i].hwmc=e.value;
            }else if(zdmc=='hwbz'){
                data[i].hwbz=e.value;
                t_map.rows[i].hwbz=e.value;
            }else if(zdmc=='hwjldw'){
                data[i].hwjldw=e.value;
                t_map.rows[i].hwjldw=e.value;
            }else if(zdmc=='gys'){
                data[i].gys=e.value;
                t_map.rows[i].gys=e.value;
            }else if(zdmc=='cpzch'){
                data[i].cpzch=e.value;
                t_map.rows[i].cpzch=e.value;
            }
        }
    }
    $("#shoppingCarForm #qgmx_json").val(JSON.stringify(data));
}

/**
 * 删除操作(从采购车删除)
 * @param index
 * @param event
 * @returns
 */
function delShoppingCarMater(index,event){
    var t_data = JSON.parse($("#shoppingCarForm #qgmx_json").val());
    t_map.rows.splice(index,1);
    for(var i=0;i<t_data.length;i++){
        if(t_data[i].index == index){
            t_data.splice(i,1);
        }
    }
    $("#shoppingCarForm #tb_list").bootstrapTable('load',t_map);
    var json=[];
    for(var i=0;i<t_data.length;i++){
        var sz={"index":i,"wlid":'',"cskz1":'',"cskz2":'',"sl":'',"qwrq":'',"bz":'',"wlbm":'',"qxqg":'','qgmxid':'','zt':'',"glid":'',"fjids":''};
        sz.wlid=t_data[i].wlid;
        sz.cskz1=t_data[i].cskz1;
        sz.cskz2=t_data[i].cskz2;
        sz.sl=t_data[i].sl;
        sz.wlbm=t_data[i].wlbm;
        sz.qwrq=t_data[i].qwrq;
        sz.bz=t_data[i].bz;
        sz.qxqg=t_data[i].qxqg;
        sz.qgmxid=t_data[i].qgmxid;
        sz.zt=t_data[i].zt;
        sz.glid=t_data[i].glid;
        sz.sysl=t_data[i].sl;
        sz.fjids=t_data[i].fjids;
        json.push(sz);
    }
    $("#shoppingCarForm #qgmx_json").val(JSON.stringify(json));
}

/**
 * 采购车数字加减
 * @param sfbj
 * @returns
 */
function addOrDelNum(sfbj){
    if(sfbj=='add'){
        count=count+1;
    }else{
        count=count-1;
    }
    $("#mater_formSearch #cg_num").text(count);
}

/**
 * 选择申请人
 * @returns
 */
function chooseSqr() {
    var url = $('#shoppingCarForm #urlPrefix').val()+"/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
    $.showDialog(url, '选择申请人', chooseQrrConfig);
}
var chooseQrrConfig = {
    width : "800px",
    height : "500px",
    modalName	: "chooseSqrModal",
    formName	: "shoppingCarForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#taskListFzrForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var sel_row = $('#taskListFzrForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    $('#shoppingCarForm #sqr').val(sel_row[0].yhid);
                    $('#shoppingCarForm #sqrmc').val(sel_row[0].zsxm);
                    $.closeModal(opts.modalName);
                    //保存操作习惯
                    $.ajax({
                        type:'post',
                        url:$('#shoppingCarForm #urlPrefix').val()+"/common/habit/commonModUserHabit",
                        cache: false,
                        data: {"dxid":sel_row[0].yhid,"access_token":$("#ac_tk").val()},
                        dataType:'json',
                        success:function(data){}
                    });
                }else{
                    $.error("请选中一行");
                    return;
                }
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
/**
 * 选择申请部门
 * @returns
 */
function chooseBm() {
    var url = $('#shoppingCarForm #urlPrefix').val()+"/systemmain/department/pagedataDepartment?access_token=" + $("#ac_tk").val();
    $.showDialog(url, '选择部门', chooseBmConfig);
}
var chooseBmConfig = {
    width : "800px",
    height : "500px",
    modalName	: "chooseBmModal",
    formName	: "shoppingCarForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                if(!$("#listDepartmentForm").valid()){
                    return false;
                }
                var $this = this;
                var opts = $this["options"]||{};
                var sel_row = $('#listDepartmentForm #tb_list').bootstrapTable('getSelections');//获取选择行数据
                if(sel_row.length==1){
                    if($("#shoppingCarForm #qglbdm").val() == "MATERIAL" || $("#shoppingCarForm #qglbdm").val() == "OUTSOURCE"){
                        if (sel_row[0].jgdm){
                            $('#shoppingCarForm #sqbmdm').val(sel_row[0].jgdm);
                        }else{
                            $('#shoppingCarForm #sqbmdm').val("");
                            $.alert("U8不存在此部门！");
                            return false;
                        }
                    }
                    $('#shoppingCarForm #sqbm').val(sel_row[0].jgid);
                    $('#shoppingCarForm #sqbmmc').val(sel_row[0].jgmc);
                    if (!sel_row[0].kzcs1){
                        $('#shoppingCarForm #sqbm').val("");
                        $('#shoppingCarForm #sqbmmc').val("");
                        $('#shoppingCarForm #sqbmdm').val("");
                        $('#shoppingCarForm #jlbh').val("");
                        $.alert("所选部门存在异常！");
                        return false;
                    }
                    $.closeModal(opts.modalName);
                    //重新生成单据号
                    $.ajax({
                        type:'post',
                        url:$('#shoppingCarForm #urlPrefix').val()+"/purchase/purchase/pagedataGenerateDjh?cskz1="+$('#shoppingCarForm #cskz1').val(),
                        cache: false,
                        data: {"sqbm":sel_row[0].jgid,"access_token":$("#ac_tk").val()},
                        dataType:'json',
                        success:function(data){
                            //返回值
                            if(data.djh!=null && data.djh!=''){
                                $("#shoppingCarForm #djh").val(data.djh);
                            }
                            if(data.jlbh!=null && data.jlbh!=''){
                                $("#shoppingCarForm #jlbh").val(data.jlbh);
                            }
                        }
                    });
                }else{
                    $.error("请选中一行");
                    return;
                }
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

/**
 * 请购明细上传附件
 * @param index
 * @returns
 */
function uploadShoppingFile(index) {
    var url = $('#shoppingCarForm #urlPrefix').val()+"/production/purchase/pagedataGetUploadFile?access_token=" + $("#ac_tk").val()+"&requestName="+$("#shoppingCarForm #requestName").val();
    if(index){
        var qgid = $("#shoppingCarForm #qgid").val();
        var wlid = t_map.rows[index].wlid;
        var fjids = $("#shoppingCarForm #fj_"+ index +" input").val();
        var qgmxid = t_map.rows[index].qgmxid;
        url=$('#shoppingCarForm #urlPrefix').val()+"/production/purchase/pagedataGetUploadFile?access_token=" + $("#ac_tk").val()+"&qgmxid="+qgmxid+"&qgid="+qgid+"&wlid="+wlid+"&index="+index+"&requestName="+$("#shoppingCarForm #requestName").val();
        if(fjids){
            url=url + "&fjids="+fjids;
        }
    }
    $.showDialog(url, '上传附件', uploadFileConfig);
}
var uploadFileConfig = {
    width : "800px",
    height : "500px",
    modalName	: "uploadFileModal",
    formName	: "ajaxForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var $this = this;
                var opts = $this["options"]||{};
                if($("#ajaxForm #fjids").val()){
                    var wlid=$("#ajaxForm #wlid").val();
                    var index=$("#ajaxForm #index").val();
                    if(!$("#shoppingCarForm #fj_"+index+" input").val()){
                        $("#shoppingCarForm #fj_"+index+" input").val($("#ajaxForm #fjids").val());
                    }else{
                        $("#shoppingCarForm #fj_"+index+" input").val($("#shoppingCarForm #fj_"+index+" input").val() + "," + $("#ajaxForm #fjids").val());
                    }
                    $("#shoppingCarForm #fj_"+index+" a").text("是");
                }
                // 更新临时复件信息到json
                var data=JSON.parse($("#shoppingCarForm #qgmx_json").val());
                for(var i=0;i<data.length;i++){
                    if(data[i].index == index){
                        data[i].fjids = $("#shoppingCarForm #fj_"+index+" input").val();
                        t_map.rows[index].fjbj = "0";
                    }
                }
                $("#shoppingCarForm #qgmx_json").val(JSON.stringify(data));
                $.closeModal(opts.modalName);
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

/**
 * 根据物料名称模糊查询
 */
$('#shoppingCarForm #addnr').typeahead({
    source : function(query, process) {
        return $.ajax({
            url : $('#shoppingCarForm #urlPrefix').val()+'/production/purchase/pagedataSelectWl',
            type : 'post',
            data : {
                "entire" : query,
                "access_token" : $("#ac_tk").val()
            },
            dataType : 'json',
            success : function(result) {
                var resultList = result.f_wlglDtos
                    .map(function(item) {
                        var aItem = {
                            id : item.wlid,
                            name : item.wlbm+"-"+item.wlmc+"-"+item.scs+"-"+item.gg
                        };
                        return JSON.stringify(aItem);
                    });
                return process(resultList);
            }
        });
    },
    matcher : function(obj) {
        var item = JSON.parse(obj);
        return ~item.name.toLowerCase().indexOf(
            this.query.toLowerCase())
    },
    sorter : function(items) {
        var beginswith = [], caseSensitive = [], caseInsensitive = [], item;
        while (aItem = items.shift()) {
            var item = JSON.parse(aItem);
            if (!item.name.toLowerCase().indexOf(
                this.query.toLowerCase()))
                beginswith.push(JSON.stringify(item));
            else if (~item.name.indexOf(this.query))
                caseSensitive.push(JSON.stringify(item));
            else
                caseInsensitive.push(JSON.stringify(item));
        }
        return beginswith.concat(caseSensitive,
            caseInsensitive)
    },
    highlighter : function(obj) {
        var item = JSON.parse(obj);
        var query = this.query.replace(
            /[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&')
        return item.name.replace(new RegExp('(' + query
            + ')', 'ig'), function($1, match) {
            return '<strong>' + match + '</strong>'
        })
    },
    updater : function(obj) {
        var item = JSON.parse(obj);
        $('#shoppingCarForm #addwlid').attr('value', item.id);
        return item.name;
    }
});


/**
 * 回车键添加物料至请购车
 * @param event
 * @returns
 */
var addkeydown = $("#addnr").bind("keydown",function(event){
    var wlid=$('#shoppingCarForm #addwlid').val();
    if (event.keyCode == "13") {
        //判断新增输入框是否有内容
        if(!$('#shoppingCarForm #addnr').val()){
            return false;
        }
        setTimeout(function(){ if(wlid != null && wlid != ''){
            $.ajax({
                type:'post',
                url:$('#shoppingCarForm #urlPrefix').val()+"/production/materiel/pagedataAddMaterOnShopping",
                cache: false,
                data: {"wlid":wlid,"access_token":$("#ac_tk").val()},
                dataType:'json',
                success:function(data){
                    //返回值
                    if(data.qgcglDto!=null && data.qgcglDto!=''){
                        if(data.qgcglDto.wlzt!='80'){
                            $.confirm("该物料未审核！");
                        }else{
                            var json=JSON.parse($("#shoppingCarForm #qgmx_json").val());
                            /*if(json!=null && json.length>0){
                                for(var i=0;i<json.length;i++){
                                    if(json[i].wlid==data.qgcglDto.wlid){
                                        $.confirm("该物料已请购！");
                                        return false;
                                    }
                                }
                            }*/
                            t_map.rows.push(data.qgcglDto);
                            var sz={"index":'',"wlid":'',"cskz1":'',"cskz2":'',"sl":'',"qwrq":'',"bz":'',"wlbm":'',"qxqg":'','qgmxid':'','zt':'',"glid":'',"fjids":''};
                            sz.index=json.length;
                            sz.wlid=data.qgcglDto.wlid;
                            sz.wlbm=data.qgcglDto.wlbm;
                            sz.cskz2=data.qgcglDto.cskz2;
                            sz.zt='80';
                            json.push(sz);
                            $("#shoppingCarForm #qgmx_json").val(JSON.stringify(json));
                            $("#shoppingCarForm #tb_list").bootstrapTable('load',t_map);
                            $('#shoppingCarForm #addwlid').val("");
                            $('#shoppingCarForm #addnr').val("");
                        }
                    }else{
                        $.confirm("该物料不存在!");
                    }
                }
            });
        }else{
            var addnr = $('#shoppingCarForm #addnr').val();
            if(addnr != null && addnr != ''){
                $.confirm("请选择物料信息!");
            }
        }}, '200')
    }
})
/**
 * 选择抄送人
 * @returns
 */
function chooseCsr(){
    var url = $('#shoppingCarForm #urlPrefix').val()+"/systemmain/task/pagedataCommonListUserPage?access_token=" + $("#ac_tk").val();
    $.showDialog(url, '选择申请人', chooseCsrConfig);
}
var chooseCsrConfig = {
    width : "800px",
    height : "500px",
    modalName	: "chooseCsrModal",
    formName	: "ajaxForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var $this = this;
                var opts = $this["options"]||{};
                var data = $('#taskListFzrForm #xzrys').val();//获取选择行数据
                if(data != null){
                    json = data;
                    var jsonStr = eval('('+json+')');
                    for (var i = 0; i < jsonStr.length; i++) {
                        $("#shoppingCarForm  #csrs").tagsinput('add', jsonStr[i]);
                    }
                }
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

function ssyfxmEvent(){
    var ssyfxm = $("#shoppingCarForm #ssyfxm").val();
    if(ssyfxm == null || ssyfxm == "")
        return;
    $("#shoppingCarForm #ssyfxmmc").val(ssyfxm);
}

function btnBind(){
    var sel_xmdl = $("#shoppingCarForm #xmdl");
    var sel_xmbm =	$("#shoppingCarForm #xmbm");
    var sel_ssyfxm = $("#shoppingCarForm #ssyfxm");
    //目大类下拉框事件
    sel_xmdl.unbind("change").change(function(){
        xmdlEvent();
    });
    //项目编码
    sel_xmbm.unbind("change").change(function(){
        xmbmEvent();
    });
    //所属研发项目
    sel_ssyfxm.unbind("change").change(function(){
        ssyfxmEvent();
    });
    // 审核状态初始化
    shzt=$("#shoppingCarForm #shzt").val();
    if(shzt=='10'){
        qxqg="拒绝审批";
    }
    // 默认人初始化
    getMrsqr();
    //添加日期控件
    laydate.render({
        elem: '#shoppingCarForm #sqrq'
        ,theme: '#2381E9'
    });
    // 初始化明细json字段
    setTimeout(function() {
        setQgmx_json();
    }, 1000);
    //初始化抄送人
    initCC();
}
/**
 * 项目编码下拉框事件
 * @returns
 */
function xmbmEvent(){
    var xmbm = $("#shoppingCarForm #xmbm").val();
    if(xmbm == null || xmbm == "")
        return;
    var xmbmdm=$("#shoppingCarForm #"+xmbm).attr("csdm");
    var xmmc=$("#shoppingCarForm #"+xmbm).attr("csmc");
    $("#shoppingCarForm #xmbmdm").val(xmbmdm);
    $("#shoppingCarForm #xmmc").val(xmmc);
}
/**
 * 项目大类下拉框事件
 */
function xmdlEvent(){
    var xmdl = $("#shoppingCarForm #xmdl").val();
    var xmdldm = $("#"+xmdl).attr("csdm");
    $("#shoppingCarForm #xmdldm").val(xmdldm);
    if(xmdl == null || xmdl==""){
        var zlbHtml = "";
        zlbHtml += "<option value=''>--请选择--</option>";
        $("#shoppingCarForm #xmbm").empty();
        $("#shoppingCarForm #xmbm").append(zlbHtml);
        $("#shoppingCarForm #xmbm").trigger("chosen:updated");
        return;
    }
    var url="/systemmain/data/ansyGetJcsjList";
    if($("#shoppingCarForm #fwbj").val()!=null && $("#shoppingCarForm #fwbj").val()!=""){
        url="/ws/data/ansyGetJcsjList";
    }
    url = $('#shoppingCarForm #urlPrefix').val()+url
    $.ajax({
        type:'post',
        url:url,
        cache: false,
        data: {"fcsid":xmdl,"access_token":$("#ac_tk").val()},
        dataType:'json',
        success:function(data){
            if(data != null && data.length != 0){
                var zlbHtml = "";
                zlbHtml += "<option value=''>--请选择--</option>";
                $.each(data,function(i){
                    zlbHtml += "<option value='" + data[i].csid + "' id='"+data[i].csid+"' csdm='"+data[i].csdm+"' csmc='"+data[i].csmc+"'>" + data[i].csmc + "</option>";
                });
                $("#shoppingCarForm #xmbm").empty();
                $("#shoppingCarForm #xmbm").append(zlbHtml);
                $("#shoppingCarForm #xmbm").trigger("chosen:updated");
            }else{
                var zlbHtml = "";
                zlbHtml += "<option value=''>--请选择--</option>";
                $("#shoppingCarForm #xmbm").empty();
                $("#shoppingCarForm #xmbm").append(zlbHtml);
                $("#shoppingCarForm #xmbm").trigger("chosen:updated");
            }
        }
    });
}
/**
 * 添加默认申请人信息
 * @returns
 */
function getMrsqr(){
    var mrsqr=$("#shoppingCarForm #sqr").val();
    var mrsqrmc=$("#shoppingCarForm #sqrmc").val();
    var mrsqbm=$("#shoppingCarForm #mrsqbm").val();
    var mrsqbmmc=$("#shoppingCarForm #mrsqbmmc").val();
    var mrsqbmdm=$("#shoppingCarForm #mrsqbmdm").val();
    $("#shoppingCarForm #sqr").val(mrsqr);
    $("#shoppingCarForm #sqrmc").val(mrsqrmc);
    $("#shoppingCarForm #sqbm").val(mrsqbm);
    $("#shoppingCarForm #sqbmmc").val(mrsqbmmc);
    $("#shoppingCarForm #sqbmdm").val(mrsqbmdm);
}
/**
 * 初始化明细json字段
 * @returns
 */
function setQgmx_json(){
    var json=[];
    var data=$('#shoppingCarForm #tb_list').bootstrapTable('getData');//获取选择行数据
    for(var i=0;i<data.length;i++){
        var sz={"index":i,"wlid":'',"cskz1":'',"cskz2":'',"sl":'',"qwrq":'',"bz":'',"wlbm":'',"qxqg":'','qgmxid':'','zt':'',"glid":'',"fjids":'',"sysl":'',"hwmc":'',"hwbz":'',"hwjldw":'',"sfrk":''};
        sz.wlid=data[i].wlid;
        sz.cskz1=data[i].cskz1;
        sz.cskz2=data[i].cskz2;
        sz.sl=data[i].sl;
        sz.wlbm=data[i].wlbm;
        sz.qwrq=data[i].qwrq;
        sz.bz=data[i].bz;
        sz.qxqg=data[i].qxqg;
        sz.qgmxid=data[i].qgmxid;
        sz.zt=data[i].zt;
        sz.glid=data[i].glid;
        sz.jg=data[i].jg;
        sz.sysl=data[i].sl;
        sz.hwmc=data[i].hwmc;
        sz.hwbz=data[i].hwbz;
        sz.hwjldw=data[i].hwjldw;
        sz.fjids=$("#shoppingCarForm #fj"+data[i].wlid+" input").val();
        sz.sfrk=data[i].sfrk;
        json.push(sz);
    }
    $("#shoppingCarForm #qgmx_json").val(JSON.stringify(json));
}
/**
 * 初始化抄送人
 * @returns
 */
function initCC(){
    //初始化已选单据号
    $("#shoppingCarForm #csrs").tagsinput({
        itemValue: "yhid",
        itemText: "zsxm",
    })
    var fwbj = $("#shoppingCarForm #fwbj").val();
    var flag = $("#shoppingCarForm #flag").val();
    var CCflag = $("#shoppingCarForm #CCflag").val();
    if(!fwbj && flag == "qxgjxg" && CCflag != "0"){
        // 查询默认抄送人信息
        $.ajax({
            type:'post',
            url:$('#shoppingCarForm #urlPrefix').val()+"/purchase/purchase/pagedataSelectCsrs",
            cache: false,
            data: {"access_token":$("#ac_tk").val(),"qglbdm":$("#shoppingCarForm #qglbdm").val()},
            dataType:'json',
            success:function(data){
                var ddxxglDtos = data.ddxxglDtos;
                if(ddxxglDtos != null && ddxxglDtos.length > 0){
                    for (var i = 0; i < ddxxglDtos.length; i++) {
                        $("#shoppingCarForm  #csrs").tagsinput('add', {"yhid":ddxxglDtos[i].yhid, "zsxm":ddxxglDtos[i].zsxm});
                    }
                }
            }
        });
    }
}

/**
 * 点击显示文件上传
 * @returns
 */
function editfile(){
    $("#fileDiv").show();
    $("#file_btn").hide();
}
/**
 * 点击隐藏文件上传
 * @returns
 */
function cancelfile(){
    $("#fileDiv").hide();
    $("#file_btn").show();
}
/**
 * 点击图片预览
 * @param fjid
 * @param wjm
 * @returns
 */
function view(fjid,wjm){
    var begin=wjm.lastIndexOf(".");
    var end=wjm.length;
    var type=wjm.substring(begin,end);
    if(type.toLowerCase()==".jpg" || type.toLowerCase()==".jpeg" || type.toLowerCase()==".jfif"||type.toLowerCase()==".png"){
        var url=$('#shoppingCarForm #urlPrefix').val()+"/ws/sjxxpripreview?fjid="+fjid
        $.showDialog(url,'图片预览',JPGMaterConfig);
    }else if(type.toLowerCase()==".pdf"){
        var url= $('#shoppingCarForm #urlPrefix').val()+"/common/file/pdfPreview?fjid=" + fjid;
        $.showDialog(url,'文件预览',JPGMaterConfig);
    }else {
        $.alert("暂不支持其他文件的预览，敬请期待！");
    }
}
var JPGMaterConfig = {
    width		: "800px",
    offAtOnce	: true,
    buttons		: {
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};
/**
 * 点击文件下载
 * @param fjid
 * @returns
 */
function xz(fjid){
    jQuery('<form action="'+$('#shoppingCarForm #urlPrefix').val()+'/common/file/downloadFile" method="POST">' +  // action请求路径及推送方法
        '<input type="text" name="fjid" value="'+fjid+'"/>' +
        '<input type="text" name="access_token" value="'+$("#ac_tk").val()+'"/>' +
        '</form>')
        .appendTo('body').submit().remove();
}
/**
 * 点击删除文件
 * @param fjid
 * @param wjlj
 * @returns
 */
function del(fjid,wjlj){
    $.confirm('您确定要删除所选择的记录吗？',function(result){
        if(result){
            jQuery.ajaxSetup({async:false});
            var url= $('#shoppingCarForm #urlPrefix').val()+"/common/file/delFile";
            jQuery.post(url,{fjid:fjid,wjlj:wjlj,"access_token":$("#ac_tk").val()},function(responseText){
                setTimeout(function(){
                    if(responseText["status"] == 'success'){
                        $.success(responseText["message"],function() {
                            $("#"+fjid).remove();
                        });
                    }else if(responseText["status"] == "fail"){
                        $.error(responseText["message"],function() {
                        });
                    } else{
                        $.alert(responseText["message"],function() {
                        });
                    }
                },1);
            },'json');
            jQuery.ajaxSetup({async:true});
        }
    });
}
/**
 * 请购附件上传回调
 * @param fjid
 * @returns
 */
function displayUpInfo(fjid){
    if(!$("#shoppingCarForm #fjids").val()){
        $("#shoppingCarForm #fjids").val(fjid);
    }else{
        $("#shoppingCarForm #fjids").val($("#shoppingCarForm #fjids").val()+","+fjid);
    }
}

/**
 * 监听用户粘贴事件(新增物料)
 * @param e
 * @returns
 */
var addpaste = $("#shoppingCarForm #addnr").bind("paste",function(e){
    var wlbms=e.originalEvent.clipboardData.getData("text");
    var arr_wlbms=wlbms.replaceAll(" ","").split(/[(\r\n)\r\n]+/);
    var length=arr_wlbms.length;
//	if(length>1){
//		arr_wlbms=arr_wlbms.remove(length-1);
//	}
    $.ajax({
        type:'post',
        url:$('#shoppingCarForm #urlPrefix').val()+"/production/materiel/pagedataAddMatersOnShopping",
        cache: false,
        data: {"wlbms":arr_wlbms.toString(),"access_token":$("#ac_tk").val()},
        dataType:'json',
        success:function(data){
            //返回值
            var bczwl="";//不存在的物料
            if(data.qgcglDtos!=null && data.qgcglDtos.length>0){
                var wshwl="";//未审核物料
                var czwlbm=[];//查询到的物料编码
                for(var i=0;i<data.qgcglDtos.length;i++){
                    czwlbm.push(data.qgcglDtos[i].wlbm);
                    if(data.qgcglDtos[i].wlzt!='80'){
                        wshwl=wshwl+","+data.qgcglDtos[i].wlbm;
                    }else{
                        t_map.rows.push(data.qgcglDtos[i]);
                        var json=JSON.parse($("#shoppingCarForm #qgmx_json").val());
                        var sz={"index":'',"wlid":'',"cskz1":'',"cskz2":'',"sl":'',"qwrq":'',"bz":'',"wlbm":'',"qxqg":'','qgmxid':'','zt':'',"glid":'',"fjids":''};
                        sz.index=json.length;
                        sz.wlid=data.qgcglDtos[i].wlid;
                        sz.wlbm=data.qgcglDtos[i].wlbm;
                        sz.cskz1=data.qgcglDtos[i].cskz1;
                        sz.cskz2=data.qgcglDtos[i].cskz2;
                        sz.zt='80';
                        json.push(sz);
                        $("#shoppingCarForm #qgmx_json").val(JSON.stringify(json));
                        $("#shoppingCarForm #tb_list").bootstrapTable('load',t_map);
                        $('#shoppingCarForm #addwlid').val("");
                        $('#shoppingCarForm #addnr').val("");
                    }
                }
                for(var i=0;i<arr_wlbms.length;i++){
                    if($.inArray(arr_wlbms[i],czwlbm)=="-1"){
                        bczwl=bczwl+","+arr_wlbms[i];
                    }
                }
                wshwl=wshwl.substring(1);
                bczwl=bczwl.substring(1);
                var errorStr="";
                if(bczwl==""){
                    if(wshwl!=""){
                        errorStr="物料编码为"+wshwl+"的物料未审核!";
                    }
                }else{
                    errorStr="物料编码为"+bczwl+"的物料不存在!";
                    if(wshwl!=""){
                        errorStr="物料编码为"+wshwl+"的物料未审核!";
                    }
                }
                if(errorStr!=""){
                    $.confirm(errorStr);
                    $('#shoppingCarForm #addwlid').val("");
                    $('#shoppingCarForm #addnr').val("");
                }
            }
        }
    });
})

$('body').on('click' , '.qwdhrq' , function() {
    var qgmxid=$(this).attr("qgmxid");
    $("#shoppingCarForm #xzqgmx").val(qgmxid);
})

/**
 * 统计查询
 * @returns
 */
function selectStatistic(domId, item, zdmc){
    if(zdmc == "jg"){
        $.ajax({
            type : 'post',
            url : $('#shoppingCarForm #urlPrefix').val() + "/purchase/statistic/pagedataStatisticPrice",
            cache : false,
            data : {
                "wlid" : item,
                "access_token" : $("#ac_tk").val()
            },
            dataType : 'json',
            success : function(data) {
                buildStatistic(domId, data);
            }
        });
    }else if(zdmc == "wlbm"){
        $.ajax({
            type : 'post',
            url : $('#shoppingCarForm #urlPrefix').val() + "/contract/statistic/pagedataStatisticCycle",
            cache : false,
            data : {
                "wlid" : item,
                "access_token" : $("#ac_tk").val()
            },
            dataType : 'json',
            success : function(data) {
                buildStatistic(domId, data);
            }
        });
    }
}

//图纸批量上传
function drawingupload(){
    var qgid = $("#shoppingCarForm #qgid").val();
    var ysfjids=$("#shoppingCarForm #ysfjids").val();
    var url=$('#shoppingCarForm #urlPrefix').val() +"/production/purchase/pagedataGetBatchUploadFilePage?access_token=" + $("#ac_tk").val()+"&qgid="+qgid+"&ysfjids="+ysfjids;
    $.showDialog(url,'物料信息',batchUploadFileConfig);
}

var batchUploadFileConfig = {
    width : "800px",
    height : "500px",
    modalName	: "batchUploadFileModal",
    formName	: "batchUploadForm",
    offAtOnce : true, // 当数据提交成功，立刻关闭窗口
    buttons : {
        success : {
            label : "确 定",
            className : "btn-primary",
            callback : function() {
                var $this = this;
                var opts = $this["options"]||{};
                if($("#batchUploadForm #ysfjids").val()){
                    $("#shoppingCarForm #ysfjids").val($("#batchUploadForm #ysfjids").val());
                    var arr_ysfjids=$("#batchUploadForm #ysfjids").val().split(",");
                    $("#shoppingCarForm #drawinguploadtext").text("图纸上传("+arr_ysfjids.length+")");
                }
                $.ajax({
                    async: false,
                    type:'post',
                    url:$('#shoppingCarForm #urlPrefix').val()+"/purchase/purchase/pagedataDealQgmxBatchFile",
                    cache: false,
                    data: {"qgmx_json":$("#shoppingCarForm #qgmx_json").val(),"ysfjids":$("#shoppingCarForm #ysfjids").val(),"access_token":$("#ac_tk").val(),"qgid":$("#shoppingCarForm #qgid").val()},
                    dataType:'json',
                    success:function(data){
                        ////将解压匹配后的文件分配到请购明细中
                        if(data.status!="success"){
                            $.confirm(data.message);
                        }
                        if(!data || data.qgmxlist == null)
                            return false;

                        var qgmxlist=data.qgmxlist;
                        var jsondata=JSON.parse($("#shoppingCarForm #qgmx_json").val());
                        if(qgmxlist!=null && qgmxlist.length>0){
                            for(var i=0;i<qgmxlist.length;i++){
                                var tzlist=qgmxlist[i].tzlist;
                                if(tzlist!=null && tzlist.length>0){
                                    var tzfjids="";
                                    for(var k=0;k<tzlist.length;k++){
                                        tzfjids=tzfjids+","+tzlist[k].fjid;
                                    }
                                    tzfjids=tzfjids.substring(1);
                                    var wlid=qgmxlist[i].wlid;
                                    var index=qgmxlist[i].index;
                                    if(!$("#shoppingCarForm #fj_"+index+" input").val()){
                                        $("#shoppingCarForm #fj_"+index+" input").val(tzfjids);
                                    }else{
                                        $("#shoppingCarForm #fj_"+index+" input").val($("#shoppingCarForm #fj_"+index+" input").val() + "," + tzfjids);
                                    }
                                    $("#shoppingCarForm #fj_"+index+" a").text("是");
                                    // 更新临时复件信息到json
                                    for(var j=0;j<jsondata.length;j++){
                                        if(jsondata[j].index == index){
                                            jsondata[j].fjids = $("#shoppingCarForm #fj_"+index+" input").val();
                                            t_map.rows[index].fjbj = "0";
                                        }
                                    }
                                }
                            }
                            $("#shoppingCarForm #qgmx_json").val(JSON.stringify(jsondata));
                        }
                    }
                });

                $.closeModal(opts.modalName);
                return false;
            }
        },
        cancel : {
            label : "关 闭",
            className : "btn-default"
        }
    }
};

$(function () { $('.collapse').collapse('hide')});

function getFlow(sprs,index){
    var t_collapse=$("#t_collapse"+index).attr("class");
    if(t_collapse.indexOf("collapsed")!='-1' && t_collapse.indexOf("getflow")=='-1'){
        $.ajax({
            type:'post',
            url:$('#shoppingCarForm #urlPrefix').val() +"/purchase/purchase/pagedataGetDingtalkFlow",
            cache: false,
            data: {"ids":sprs,"access_token":$("#ac_tk").val()},
            dataType:'json',
            success:function(data){
                //返回值=
                if(data.yhlist!=null && data.yhlist.length>0){
                    var html="";
                    html+="<div class='col-sm-12 col-md-12'>"+
                        "<div class='col-sm-3 col-md-3' style='padding:0px;margin-top:5px;'>"+
                        "<div class='jdsty btn-info col-sm-9 col-md-9 jdlist' text='申请人' title='申请人'>申请人</div>"+
                        "<div class='col-sm-2 col-md-2 glyphicon glyphicon-arrow-right jt' style='transform: translateY(50%);'></div>"+
                        "</div>";
                    for(var i=0;i<data.yhlist.length;i++){
                        html+="<div class='col-sm-3 col-md-3' style='padding:0px;margin-top:5px;'>"+
                            "<div class='jdsty btn-info col-sm-9 col-md-9 jdlist' text='"+data.yhlist[i].zsxm+"' title='"+data.yhlist[i].zsxm+"'>"+data.yhlist[i].zsxm+"</div>";
                        if(i<data.yhlist.length-1){
                            html+="<div class='col-sm-2 col-md-2 glyphicon glyphicon-arrow-right jt' style='transform: translateY(50%);'></div>";
                        }
                        html+="</div>";
                    }
                    html+="</div>";
                    $("#collapsebody"+index).append(html);
                    $("#t_collapse"+index).addClass("getflow");
                }
            }
        });
    }
}

function changeFlow(spid,index){
    $(".logeok").attr("style","float:right;color:grey;cursor:pointer;");
    $("#ok"+index).attr("style","float:right;color:green;cursor:pointer;");
    $("#shoppingCarForm #ddspid").val(spid);
}

//弹出DIV
function showDiv(index,e,zdmc){
    var qglbdm=$("#shoppingCarForm #qglbdm").val();
    $("#otherdiv").attr("style","display:block;left:65%;top:40%;");
    $("#otherdiv").attr("index",index);
    $("#otherdiv").attr("zdmc",zdmc);
    var value=$("#shoppingCarForm #"+zdmc+"_"+index).val();
    $("#nrdiv").text(value);
    $("#nrdiv").val(value);
    if(zdmc=='wbyq' && qglbdm=="DEVICE"){
        $("#nrdiv").attr("placeholder","维保要求提示语：维保期一般为到货后一年；维保到期后是否有续保需求；举例：可填写 离心机保修到期后 无需另买维保；或离心机一年保修到期后 有续保的需求  ");
    }else if(zdmc=='pzyq' && qglbdm=="DEVICE"){
        $("#nrdiv").attr("placeholder","配置要求提示语：配置复杂的情况下请上传附件 ");
    }else if(zdmc=='yq' && qglbdm=="SERVICE"){
        $("#nrdiv").attr("placeholder","服务要求提示语：复杂的情况下请上传附件 ");
    }else if(zdmc=='hwyt' && qglbdm=="SERVICE"){
        $("#nrdiv").attr("placeholder","服务用途提示语：复杂的情况下请上传附件 ");
    }else if(zdmc=='hwyt' && qglbdm=="DEVICE"){
        $("#nrdiv").attr("placeholder","设备用途提示语：复杂的情况下请上传附件 ");
    }else if(zdmc=='sjjl' && qglbdm=="SERVICE"){
        $("#nrdiv").attr("placeholder","服务商审计结论提示语：复杂的情况下请上传附件 ");
    }
    $("#otherdiv #nrdiv").focus();
}

//弹出框点击确定
function confirmdiv(){
    var nr=$("#otherdiv textarea").val();
    var zdmc=$("#otherdiv").attr("zdmc");
    var index=$("#otherdiv").attr("index");
    $("#nrdiv").text("");
    $("#nrdiv").val("");
    $("#shoppingCarForm #"+zdmc+"_"+index).val(nr);
    checkDeviceSum(index,nr,zdmc);
    $("#otherdiv").attr("style","display:none;");
}

/**
 * 失去焦点事件(修改列表参数)
 * @param id
 * @param e
 * @param zdmc
 * @param bj
 * @returns
 */
function checkDeviceSum(index,e,zdmc){
    var data=JSON.parse($("#shoppingCarForm #qgmx_json").val());
    for(var i=0;i<data.length;i++){
        if(data[i].index == index){
            if(zdmc=='wbyq'){
                data[i].wbyq=e;
                t_map.rows[i].wbyq=e;
            }else if(zdmc=='pzyq'){
                data[i].pzyq=e;
                t_map.rows[i].pzyq=e;
            }else if(zdmc=='sjjl'){
                data[i].sjjl=e;
                t_map.rows[i].sjjl=e;
            }else if(zdmc=='hwyt'){
                data[i].hwyt=e;
                t_map.rows[i].hwyt=e;
            }else if(zdmc=='yq'){
                data[i].yq=e;
                t_map.rows[i].yq=e;
            }
        }
    }
    $("#shoppingCarForm #qgmx_json").val(JSON.stringify(data));
}


//关闭弹出框
function canceldiv(){
    $("#otherdiv").attr("style","display:none;");
}

$(function(){
    //0.初始化fileinput
    var index=$("#shoppingCarForm #mrxz").val();
    $("#ok"+index).attr("style","float:right;color:green;cursor:pointer;");
    var oFileInput = new FileInput();
    var sign_params=[];
    sign_params.prefix=$('#shoppingCarForm #urlPrefix').val();
    oFileInput.Init("shoppingCarForm","displayUpInfo",2,1,"pro_file",null,sign_params);
    //初始化列表
    var oTable=new ShoppingCar_TableInit();
    oTable.Init();
    btnBind();
    jQuery('#shoppingCarForm .chosen-select').chosen({width: '100%'});
})