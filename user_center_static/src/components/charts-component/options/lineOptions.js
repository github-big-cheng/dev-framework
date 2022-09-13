/* 折线图 */

// import { w, chartsColor } from '@/utils/auth'

const chartsColor = ['#9E87FF', '#73DDFF', '#fe9a8b', '#F56948', '#9E87FF'];

export default ({ data = [], xData = [], legendData = [], title = "利润收支曲线" }) => ({
    backgroundColor: '#fff',
    title: {
        text: title,
        textStyle: {
            fontSize: 12,
            fontWeight: 400,
        },
        left: 'center',
        top: '5%',
        show: false,
    },
    legend: {
        x: 'right',
        y: 'top',
        show: true,
        top: '5%',
        right: '5%',
        itemWidth: 6,
        itemGap: 20,
        textStyle: {
            color: '#556677',
        },
        data: legendData,
    },
    dataZoom: [
        {
            type: 'inside',
            start: 0,
            end: 100,
        },
        {
            start: 0,
            end: 100,
        },
    ],
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            label: {
                show: true,
                backgroundColor: '#fff',
                color: '#556677',
                borderColor: 'rgba(0,0,0,0)',
                shadowColor: 'rgba(0,0,0,0)',
                shadowOffsetY: 0,
            },
            lineStyle: {
                width: 0,
            },
        },
        backgroundColor: '#fff',
        textStyle: {
            color: '#5c6c7c',
        },
        padding: [10, 10],
        extraCssText: 'box-shadow: 1px 0 2px 0 rgba(163,163,163,0.5)',
    },
    grid: {
        top: '15%',
        y2: 88,
    },
    xAxis: [
        {
            type: 'category',
            data: xData,
            axisLine: {
                show: true,
                lineStyle: {
                    color: '#DCE2E8',
                },
            },
            axisTick: {
                show: true,
            },
            axisLabel: {
                interval: 0,
                textStyle: {
                    color: '#556677',
                },
                // 默认x轴字体大小
                fontSize: 12,
                // margin:文字到x轴的距离
                margin: 15,
            },
            axisPointer: {
                label: {
                    padding: [0, 0, 10, 0],
                    margin: 15,
                    fontSize: 12,
                    backgroundColor: {
                        type: 'linear',
                        x: 0,
                        y: 0,
                        x2: 0,
                        y2: 1,
                        colorStops: [
                            {
                                offset: 0,
                                color: '#fff', // 0% 处的颜色
                            },
                            {
                                // offset: 0.9,
                                offset: 0.86,
                                color: '#fff', // 0% 处的颜色
                            },
                            {
                                offset: 0.86,
                                color: '#33c0cd', // 0% 处的颜色
                            },
                            {
                                offset: 1,
                                color: '#33c0cd', // 100% 处的颜色
                            },
                        ],
                        global: false, // 缺省为 false
                    },
                },
            },
            splitLine: {
                show: true,
                lineStyle: {
                    type: 'dashed',
                },
            },
            boundaryGap: false,
        },
    ],
    yAxis: [
        {
            type: 'value',
            name: '单位 ℃',
            nameTextStyle: {
                color: '#333',

            },
            axisTick: {
                show: false,
            },
            axisLine: {
                show: true,
                lineStyle: {
                    color: '#DCE2E8',
                },
            },
            axisLabel: {
                textStyle: {
                    color: '#556677',
                },
            },
            splitLine: {
                show: true,
                lineStyle: {
                    type: 'dashed',
                },
            },
        },
    ],
    series: data.map(({ value, name }, i) => ({
        name: name,
        type: 'line',
        data: value,
        symbolSize: 1,
        symbol: 'circle',
        smooth: true,
        yAxisIndex: 0,
        showSymbol: false,
        //高亮的样式
        emphasis: {
            focus: 'series',
        },
        lineStyle: {
            width: 2,
        },
        itemStyle: {
            normal: {
                color: chartsColor[i],
                borderColor: chartsColor[i],
            },
        },
        markPoint: {
            symbol: 'pin', //标记(气泡)的图形
            symbolSize: 50, //标记(气泡)的大小
            itemStyle: {
                // color: '#4587E7', //图形的颜色。
                borderColor: '#000', //图形的描边颜色。支持的颜色格式同 color，不支持回调函数。
                borderWidth: 0, //描边线宽。为 0 时无描边。
                borderType: 'solid', //柱条的描边类型，默认为实线，支持 ‘solid’, ‘dashed’, ‘dotted’。
            },
            data: [
                { type: 'max', name: '最大值' },
                { type: 'min', name: '最小值' },
            ],
        },
        /*    markLine: {
               data: [{ type: 'average', name: '平均值' }],
           }, */
    })),


});