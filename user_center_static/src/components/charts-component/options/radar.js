/**
 * @name 雷达图 
 * @param {
 *  object{
 *      position:[雷达图的位置]
 *      indicator:[{max,name}]
 *      value:[number]
 *  }
 * }
 */
export default ({ position, indicator = [], value = [], name = "" }) => {
    return ({

        backgroundColor: "transparent",
        tooltip: {
            show: true,
            borderWidth: 1,
            borderColor: '#00FDF1',
            appendToBody: true,
            backgroundColor: '#0a2349',
            textStyle: {
                lineHeight: 10,
                fontSize: 12,
                color: "#fff",
            },
            position: position && ((point) => [point[0] + 40, '-10%'])
        },

        radar: {
            radius: "60%", //大小
            axisNameGap: 1, // 图中工艺等字距离图的距离
            center: ["40%", "50%"], // 图的位置
            splitArea: {

                show: true,
                areaStyle: {
                    color: "rgba(255,0,0,0)" // 图表背景的颜色
                }
            },
            splitLine: {
                show: true,
                lineStyle: {
                    width: 1,
                    color: "rgba(153, 209, 246, 0.2)" // 设置网格的颜色
                }
            },
            axisName: {
                color: "rgba(101, 213, 255, 1)",
                fontSize: 12,
            },
            indicator,
            axisLine: {
                lineStyle: {
                    color: "rgba(153, 209, 246, 0.2)"
                }
            },
        },

        series: [
            {
                name,
                type: "radar",
                symbol: "angle",
                itemStyle: {
                    areaStyle: { type: "default" }
                },
                data: [
                    {
                        symbol: "circle",
                        symbolSize: 5,
                        value,
                        areaStyle: { color: "rgba(0, 253, 241, 0.2)" },
                        itemStyle: {
                            borderWidth: 1,
                            color: "rgba(0, 253, 241, 0.2)",
                            borderColor: "#00FDF1"
                        },
                        lineStyle: {
                            color: "#00FDF1",
                            width: 1
                        }
                    }
                ]
            }
        ]
    })
}