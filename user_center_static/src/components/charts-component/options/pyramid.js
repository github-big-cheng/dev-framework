import { w, chartsColor } from '@/utils/auth'
export default ({ data = [] }) => (
    {
        color: chartsColor,
        backgroundColor: 'transparent',
        tooltip: {
            triggerOn: 'click'
        },
        radar: {
            name: {
                show: false
            },
            indicator: [{
                name: 'A',
                max: 100
            },
            {
                name: 'B',
                max: 100
            },
            {
                name: 'C',
                max: 100
            },
            {
                name: 'D',
                max: 100
            },
            {
                name: 'E',
                max: 100
            },
            {
                name: 'F',
                max: 100
            },
            {
                name: 'G',
                max: 100
            },
            {
                name: 'H',
                max: 100
            }
            ],
            center: ['50%', "5%"],
            radius: '100%',
            axisLine: {
                show: false
            },
            splitLine: {
                show: false
            },
            splitArea: {
                show: false
            }
        },
        series: [{
            type: 'radar',
            areaStyle: {
                opacity: 1,
                shadowBlur: 1,
                shadowColor: 'rgba(0,0,0,0)',
            },

            silent: true,

            data: data && data.map((item, i) => {
                let value = [0, 0, 0, 50, 60, 51];
                const position = 50 + (i * 30)
                if (i) {
                    value = [0, 0, 0, position, 55 + (i * 36), position + 1,]
                }
                return {
                    value,
                    symbol: 'circle',
                    symbolSize: 0,
                    emphasis: { //高亮的样式
                        label: {
                            show: true,
                            fontSize: w > 1400 ? 16 : 14,
                            color: '#fff',
                        }
                    },
                    label: {
                        show: true,
                        color: chartsColor[chartsColor.length - 1 - i],
                        formatter: function (point) {
                            if (point.value === position) {
                                return `{a|${item.name}}`
                            } else if (point.value === position + 1) {
                                return `{b|${item.value}}`
                            } else
                                return ''
                        },
                        rich: {
                            a: {
                                align: 'right',
                                fontSize: w > 1400 ? 14 : 12,
                                padding: [0, 30, 0, 0],
                            },
                            b: {
                                fontSize: w > 1400 ? 14 : 12,
                                padding: [0, 0, 1, 50],
                                align: 'left'
                            },
                        }
                    }
                }
            }).reverse()
        }],

    }
)