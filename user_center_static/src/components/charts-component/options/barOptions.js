import { w } from '@/utils/auth'
const barSeries = ({ data, barWidth, colors = [], name, barBorderRadius, ...seriesOptions }) => {
    return ({
        data,
        type: 'bar',
        name,
        barWidth, //柱宽
        barMaxWidth: 40,
        label: {
            barBorderRadius: barBorderRadius || [20, 20, 0, 0],
        },
        itemStyle: {
            color: {
                type: 'linear',
                x2: 0,
                y2: 1,
                colorStops: [{
                    offset: 0,
                    color: colors[0] || '#FF9A22'
                }, {
                    offset: 1,
                    color: colors[1] || '#FFD56E'
                }]
            },
        },
        ...seriesOptions
    })
}

export default (options) => {
    const { data, barWidth, axisData, axisPointer, legend, colors = [], tooltip, xAxis, yAxis, seriesOptions } = options;
    let series = []
    /* 处理数据，将多柱状图数据拆开显示 */
    if (Array.isArray(data[0]) || Array.isArray(data[0]['value'])) {
        series = data.map((item, i) => barSeries({ data: item.value || item, name: item.name, barWidth, colors: colors[i], seriesOptions }))
    } else {
        series = barSeries({ data, seriesOptions })
    }
    return {
        xAxis: {
            data: axisData,
            type: 'category',
            ...xAxis
        },
        legend,
        yAxis: yAxis || {
            type: 'value',
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: axisPointer || {            // 坐标轴指示器，坐标轴触发有效
                type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            },
            textStyle: {
                fontSize: w > 1400 ? 12 : 9,
                // color: '#fff',
            },
            ...tooltip
        },

        grid: {
            left: '0',
            right: '0',
            bottom: '0',
            top: '10px',
            containLabel: true
        },
        series
    }
}

