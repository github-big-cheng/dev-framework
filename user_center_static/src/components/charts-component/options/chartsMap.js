/**
 * 地图数据引入，现在是  开封 
 *  需要拉取 geojson
 * 获取其他地区地址 http://datav.aliyun.com/tools/atlas/index.html#&lat=33.68867890354664&lng=113.99138335572921&zoom=4.5
 */
import geojson from "@/components/charts-component/geojson.json";
import { w } from '@/utils/auth'

var convertData = () => geojson.features.map(({ properties: { name, centroid } }) => ({ name, value: centroid }))

export const chartsMap = ({ tooltip = {}, series = [], data, emphasis, silent, label, layoutSize, ...options }) => {

    return ({
        tooltip,
        geo: [{
            show: true,
            map: 'kaifeng',
            aspectScale: 0.9,
            roam: false, // 是否允许缩放
            zoom: .93, // 默认显示级别
            layoutSize: layoutSize || '100%',
            layoutCenter: ['53%', '50%'],
            label: {
                color: "#fff",
                show: false,
                textStyle: {
                    fontSize: w > 1400 ? 12 : 10,
                }
            },
            itemStyle: {
                borderColor: '#4B87DB',
                borderWidth: 2,
                areaColor: '#0057A8',
            },
            //高亮的样式
            emphasis: emphasis || {
                itemStyle: {
                    areaColor: '#118AF7',
                    borderColor: '#fff',
                    borderWidth: 1,
                }
            },

            select: {
                label: { show: false },
                itemStyle: false
            },
            zlevel: 3,
            silent,
        },
        {
            map: 'kaifeng',
            aspectScale: 0.9,
            roam: false, // 是否允许缩放
            zoom: .93, // 默认显示级别
            layoutSize: layoutSize || '100%',
            layoutCenter: ['53%', '50%'],
            itemStyle: {
                borderColor: '#fff',
                borderWidth: w > 1400 ? 5 : 3,
                shadowColor: '#fff',
                shadowOffsetY: 0,
                shadowBlur: 4,
                areaColor: 'rgba(29,85,139,.2)'
            },
            zlevel: 2,
            emphasis: emphasis || {
                itemStyle: {
                    areaColor: '#118AF7',
                    borderColor: '#fff',
                    borderWidth: 1,
                }
            },

            select: {
                label: { show: false },
                itemStyle: false
            },
            silent: true
        },
        {
            map: 'kaifeng',
            aspectScale: 0.9,
            roam: false, // 是否允许缩放
            zoom: .93, // 默认显示级别
            layoutSize: layoutSize || '100%',
            layoutCenter: ['53%', '52%'],
            itemStyle: {
                areaColor: '#2763bb',
                borderColor: '#649bea',
                borderWidth: 1
            },
            zlevel: 1,
            emphasis: emphasis || {
                itemStyle: {
                    areaColor: '#118AF7',
                    borderColor: '#fff',
                    borderWidth: 1,
                }
            },

            select: {
                label: { show: false },
                itemStyle: false
            },
            silent: true
        }],
        series: [
            {
                type: "map",
                roam: false,
                geoIndex: 0,
                data,
                map: 'kaifeng'
            },
            {
                type: 'scatter',
                coordinateSystem: 'geo',
                legendHoverLink: false,
                symbolSize: false,
                label: label || {
                    show: true,
                    textStyle: {
                        fontSize: w > 1400 ? 12 : 10,
                    },
                    formatter: ({ name }) => name,
                    color: '#fff',

                },
                tooltip: { show: false },
                itemStyle: {
                    show: false,
                },
                data: convertData(),
                /* 这个可以调试label的定位 */
                labelLayout(params) {
                    let x, y;
                    if (params.text.includes('龙亭区')) {
                        x = params.rect.x - 10 + (w > 1400 ? 0 : 10)
                        y = params.rect.y
                    } else if (params.text.includes('顺河回族区')) {
                        x = params.rect.x + (params.rect.width / 2)
                        y = (params.rect.y) + (w > 1400 ? 0 : 5)
                    } else if (params.text.includes('禹王台区')) {
                        x = params.rect.x
                        y = params.rect.y
                    } else if (params.text.includes('鼓楼区')) {
                        x = params.rect.x
                        y = params.rect.y + params.rect.height / 2 + 10
                    } else if (params.text.includes('祥符区')) {
                        x = params.rect.x + 20
                        y = params.rect.y + params.rect.height / 2 + 10
                    } else {
                        x = params.rect.x + (params.rect.width / 2)
                        y = params.rect.y
                    }
                    return {
                        x, y,
                        verticalAlign: 'middle',
                        align: 'center',
                    }
                },
                emphasis: {
                    show: false
                },

                select: {
                    label: { show: false },
                    itemStyle: false
                },
                zlevel: 9,
            },
            ...series
        ],
        ...options
    })
}
/* 进度条 */
export const tooltipBar = {
    trigger: "item",
    backgroundColor: 'rgba(50,50,50,0.7)',
    borderColor: '#FFCC00',
    textStyle: {
        fontSize: w > 1400 ? 12 : 9,
        color: '#fff',
    },
    formatter(params) {
        const { data, name } = params
        if (data) {
            const { value, name } = data
            const ratio = (type) => {
                const ratioValue = Math.ceil(value[2] / value[3] * 100)
                return (type === 'bar' ? (ratioValue <= 100 ? ratioValue : 100) : ratioValue) + '%'
            }
            return `
                <div class="echarts-tooltip-map">
                    <p>
                        <i>${name}</i>
                        <br/>
                        当前库存：${value[2] || 0}/吨
                        <br/>
                        计划库存规模：${value[3] || 0}/吨
                    </p>
                    <div> 
                        <div class="echarts-tooltip-bar">
                            <div style="width:${ratio('bar')}"></div> 
                        </div>
                        ${ratio()}
                    </div>
                 </div> 
                 `
        } else {
            return `${name}`
        }
    },
}