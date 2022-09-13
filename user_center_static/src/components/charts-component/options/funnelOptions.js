import { w, chartsColor } from '@/utils/auth'
/* 漏斗 */
export default ({ data, legend, grid, series = {} }) => ({

    tooltip: {
        trigger: 'item',
        formatter: "{b}"
    },
    color: chartsColor,

    grid: {
        show: false,
        left: '0',
        right: '0',
        bottom: '0',
        top: 'center',
        containLabel: true,
        ...grid,
    },
    legend: {
        width: '30%',
        right: 0,
        itemHeight: w > 1400 ? 14 : 10,
        itemGap: w > 1400 ? 10 : 1,
        padding: 0,
        margin: 0,
        top: 0,
        textStyle: {
            fontSize: w > 1400 ? 13 : 10,
            lineHeight: 10
        },
        ...legend
    },
    series: [
        {
            type: 'funnel',
            sort: 'ascending',
            left: '0',
            right: '0',
            bottom: '0',
            top: '10px',
            width: '60%',
            min: 0,
            max: 100,
            minSize: '0%',
            maxSize: '100%',
            label: {
                show: false,
                fontSize: 0,
                color: '#fff',
            },
            labelLine: {
                show: false,
                length: 0,
                lineStyle: {
                    width: 1,
                    type: 'solid'
                }
            },
            itemStyle: {
                borderColor: '#fff',
                borderWidth: 1
            },
            //高亮的样式
            emphasis: {
                label: {
                    show: false,
                    fontSize: 12,
                    color: '#fff',
                }
            },
            data,
            ...series,
        }
    ]
})