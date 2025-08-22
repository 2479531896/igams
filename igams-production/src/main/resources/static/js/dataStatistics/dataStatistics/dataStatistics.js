function refresh(){
    $.ajax({
        type: 'post',
        url: '/dataStatistics/dataStatistics/getDataStatistics',
        cache: false,
        data: {"access_token":$("#ac_tk").val()},
        dataType: 'json',
        success:function (data) {
            var contractMapByMons = [];
            var contractMapByMonDatas = [];
            for (var i = 0; i < data.contractMapByMon.length ; i++) {
                contractMapByMons.push(data.contractMapByMon[i].rq);
                contractMapByMonDatas.push(data.contractMapByMon[i].count);
            }
            var purchaseMaterialMapByMons = [];
            var purchaseMaterialMapByMonDatas = [];
            for (var i = 0; i < data.purchaseMaterialMapByMon.length ; i++) {
                purchaseMaterialMapByMons.push(data.purchaseMaterialMapByMon[i].rq);
                purchaseMaterialMapByMonDatas.push(data.purchaseMaterialMapByMon[i].count);
            }
            var requisitionMapByMons = [];
            var requisitionMapByMonDatas = [];
            for (var i = 0; i < data.requisitionMapByMon.length ; i++) {
                requisitionMapByMons.push(data.requisitionMapByMon[i].rq);
                requisitionMapByMonDatas.push(data.requisitionMapByMon[i].count);
            }
            var purchaseMaterialMapByWeeks = [];
            var purchaseMaterialMapByWeekDatas = [];
            for (var i = 0; i < data.purchaseMaterialMapByWeek.length ; i++) {
                purchaseMaterialMapByWeeks.push(data.purchaseMaterialMapByWeek[i].rq);
                purchaseMaterialMapByWeekDatas.push(data.purchaseMaterialMapByWeek[i].num);
            }
            var requisitionMapByWeeks = [];
            var requisitionMapByWeekDatas = [];
            for (var i = 0; i < data.requisitionMapByWeek.length ; i++) {
                requisitionMapByWeeks.push(data.requisitionMapByWeek[i].rq);
                requisitionMapByWeekDatas.push(data.requisitionMapByWeek[i].num);
            }

            $("#byqgdsl").text(requisitionMapByMonDatas[0])
            $("#bzqgdsl").text(requisitionMapByWeekDatas[0]);
            $("#byqgwlsl").text(purchaseMaterialMapByMonDatas[0]);
            $("#bzqgwlsl").text(purchaseMaterialMapByWeekDatas[0]);
            $("#byhtsl").text(contractMapByMonDatas[0]);
            // $("#bzhtsl").text(data.bzhtsl);
            console.log(data)
            var qgd_weekChart = echarts.init(document.getElementById('qgd_week'));
            var qgd_weekOption = {
                backgroundColor: '#1b1e25',
                title: {
                    text: ''
                },

                tooltip: {
                    enterable: true,
                    trigger: 'axis'

                },
                grid: {
                    left: '3%',
                    right: '3%',
                    top: '10%',
                    bottom: "2%",
                    containLabel: true
                },
                xAxis: [
                    {
                        axisLine: {
                            lineStyle: {
                                color: '#3e4148',
                                width: 1,//这里是为了突出显示加上的
                            }
                        },
                        type: 'category',
                        boundaryGap: false,
                        data: requisitionMapByWeeks
                    }
                ],
                yAxis: [
                    {
                        splitLine: {
                            lineStyle: {
                                color: '#21242b',
                            }
                        },
                        type: 'value',
                        axisLine: {
                            lineStyle: {
                                color: '#43484e',
                                width: 0,//这里是为了突出显示加上的
                            }
                        }
                    }
                ],
                series: [
                    {
                        // name: '新增单量',
                        type: 'line',
                        symbol: 'none',
                        data: requisitionMapByWeekDatas,
                        smooth: true,
                        itemStyle: {
                            normal: {
                                lineStyle: {
                                    color: '#533d86'
                                }
                            }
                        },
                        areaStyle: {normal: {color: ['rgba(255,255,255,0.1)']}},

                    }
                ]
            };
            var qgwl_weekChart = echarts.init(document.getElementById('qgwl_week'));
            var qgwl_weekOption = {
                backgroundColor: '#1b1e25',
                title: {
                    text: ''
                },

                tooltip: {
                    enterable: true,
                    trigger: 'axis'

                },
                grid: {
                    left: '3%',
                    right: '3%',
                    top: '10%',
                    bottom: "2%",
                    containLabel: true
                },
                xAxis: [
                    {
                        axisLine: {
                            lineStyle: {
                                color: '#3e4148',
                                width: 1,//这里是为了突出显示加上的
                            }
                        },
                        type: 'category',
                        boundaryGap: false,
                        data: purchaseMaterialMapByWeeks
                    }
                ],
                yAxis: [
                    {
                        splitLine: {
                            lineStyle: {
                                color: '#21242b',
                            }
                        },
                        type: 'value',
                        axisLine: {
                            lineStyle: {
                                color: '#43484e',
                                width: 0,//这里是为了突出显示加上的
                            }
                        }
                    }
                ],
                series: [
                    {
                        // name: '拣选单量',
                        type: 'line',
                        symbol: 'none',
                        data: purchaseMaterialMapByWeekDatas,
                        smooth: true,
                        itemStyle: {
                            normal: {
                                lineStyle: {
                                    color: '#60ba9e'
                                }
                            }
                        },
                        areaStyle: {normal: {color: ['rgba(255,255,255,0.1)']}},

                    }
                ]
            };
            var qgd_monthChart = echarts.init(document.getElementById('qgd_month'));
            var qgd_monthOption = {
                backgroundColor: '#1b1e25',
                title: {
                    text: ''
                },

                tooltip: {
                    enterable: true,
                    trigger: 'axis'

                },
                grid: {
                    left: '3%',
                    right: '3%',
                    top: '10%',
                    bottom: "2%",
                    containLabel: true
                },
                xAxis: [
                    {
                        axisLine: {
                            lineStyle: {
                                color: '#3e4148',
                                width: 1,//这里是为了突出显示加上的
                            }
                        },
                        type: 'category',
                        boundaryGap: false,
                        data: requisitionMapByMons
                    }
                ],
                yAxis: [
                    {
                        splitLine: {
                            lineStyle: {
                                color: '#21242b',
                            }
                        },
                        type: 'value',
                        axisLine: {
                            lineStyle: {
                                color: '#43484e',
                                width: 0,//这里是为了突出显示加上的
                            }
                        }
                    }
                ],
                series: [
                    {
                        // name: '质检单量',
                        type: 'line',
                        symbol: 'none',
                        data: requisitionMapByMonDatas,
                        smooth: true,
                        itemStyle: {
                            normal: {
                                lineStyle: {
                                    color: '#476d9e'
                                }
                            }
                        },
                        areaStyle: {normal: {color: ['rgba(255,255,255,0.1)']}},

                    }
                ]
            };
            var qgwl_monthChart = echarts.init(document.getElementById('qgwl_month'));
            var qgwl_monthOption = {
                backgroundColor: '#1b1e25',
                title: {
                    text: ''
                },

                tooltip: {
                    enterable: true,
                    trigger: 'axis'

                },
                grid: {
                    left: '3%',
                    right: '3%',
                    top: '10%',
                    bottom: "2%",
                    containLabel: true
                },
                xAxis: [
                    {
                        axisLine: {
                            lineStyle: {
                                color: '#3e4148',
                                width: 1,//这里是为了突出显示加上的
                            }
                        },
                        type: 'category',
                        boundaryGap: false,
                        data: purchaseMaterialMapByMons
                    }
                ],
                yAxis: [
                    {
                        splitLine: {
                            lineStyle: {
                                color: '#21242b',
                            }
                        },
                        type: 'value',
                        axisLine: {
                            lineStyle: {
                                color: '#43484e',
                                width: 0,//这里是为了突出显示加上的
                            }
                        }
                    }
                ],
                series: [
                    {
                        // name: '出库单量',
                        type: 'line',
                        symbol: 'none',
                        data: contractMapByMonDatas,
                        smooth: true,
                        itemStyle: {
                            normal: {
                                lineStyle: {
                                    color: '#b1526a'
                                }
                            }
                        },
                        areaStyle: {normal: {color: ['rgba(255,255,255,0.1)']}},
                    }
                ]
            };
            var setoption = function () {
                qgd_weekChart.setOption(qgd_weekOption);
                qgwl_weekChart.setOption(qgwl_weekOption);
                qgd_monthChart.setOption(qgd_monthOption);
                qgwl_monthChart.setOption(qgwl_monthOption);
            }
            setoption()
        }
    })
}

$(function () {
    refresh();
})