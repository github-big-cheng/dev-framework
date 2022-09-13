import { w } from '@/utils/auth'

/**
 * @name 水滴图
 * @param {
 *  data: array[0]
 * }
 */
export default (data = [0]) => ({
    backgroundColor: 'transparent',
    polar: {
        radius: ["45%", "41%"],
        center: ['50%', '50%']
    },
    angleAxis: {
        clockwise: false,
        axisLine: {
            show: false,
        },
        axisTick: {
            show: false,
        },
        axisLabel: {
            show: false,
        },
        splitLine: {
            show: false,
        },
    },
    radiusAxis: {
        type: 'category',
        show: false,
        axisLabel: {
            show: false,
        },
        axisLine: {
            show: false,
        },
        axisTick: {
            show: false,
        },
    },
    series: [
        {
            type: 'liquidFill',
            data,
            center: ['40%', '50%'],
            radius: '60%',
            itemStyle: {
                opacity: 0.95,
                color: {
                    type: 'linear',
                    x: 0,
                    y: 0,
                    x2: 0,
                    y2: 1,
                    colorStops: [
                        {
                            offset: 0,
                            color: '#43BAFE',
                        },
                        {
                            offset: 1,
                            color: '#00FDF1',
                        },
                    ],
                    globalCoord: false,
                }
            }
            ,
            backgroundStyle: {
                color: {
                    type: 'radial',
                    x: 0.5,
                    y: 0.5,
                    r: 0.5,
                    colorStops: [{
                        offset: 0, color: 'transparent' // 0% 处的颜色
                    }, {
                        offset: 1, color: 'rgba(51, 214, 255, 0.15)' // 100% 处的颜色
                    }],
                    global: false
                }// 缺省为 false/* '' */,
            },
            //高亮的样式
            emphasis: {
                itemStyle: {
                    opacity: 0.9
                }
            },
            label: {
                show: true,
                color: '#328DC8',
                insideColor: '#fff',
                fontSize: w > 1400 ? 20 : 16,
                align: 'center',
            },
            outline: {
                show: true,
                borderDistance: 0,
                itemStyle: {
                    borderColor: '#328DC8',
                    borderWidth: 2,
                }
            },
        },
    ],
});
