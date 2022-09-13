import { w, chartsColor } from "@/utils/auth";
/**
 *
 * @param {data:array,title: string}
 * @returns option
 */

export default ({
    data,
    title,
    itemStyle,
    position,
    legend,
    tooltip,
    emphasis,
    textColor,
    labelLine,
    labelPadding,
    radius,
    series,
    seriesLabel,
    seriesCenter,
    fontSize = w > 1400 ? 13 : 10,
}) => {
    const total = data
        .map((i) => i.value)
        .reduce((a, b) => {
            return +a + +b;
        }, 0);
    return {
        tooltip: {
            trigger: "item",
            position:
                position &&
                function(pos, params, dom, rect, size) {
                    var obj = { top: "center" };
                    obj[["left", "right"][+(pos[0] < size.viewSize[0] / 2)]] = 5;
                    return obj;
                },
            textStyle: {
                fontSize: w > 1400 ? 12 : 9,
            },
            ...tooltip,
        },
        legend: {
            show: false,
            ...legend,
        },
        color: chartsColor,
        title: {
            text: title,
            left: "center",
            color: "#999999",
            top: "center",
            textStyle: {
                fontSize,
            },
        },
        series: series || [
            {
                data,
                type: "pie",
                radius: radius || ["50", "100"],//内外圆的大小
                center: seriesCenter, //调整图像的位置，距离左右，上下距离的百分比 [x, y]
                itemStyle: itemStyle || {
                    borderRadius: 3,
                    borderColor: "#fff",
                    borderWidth: 8,
                },
                labelLine: {
                    show: false,
                    length: -10,
                    ...labelLine,
                },
                label: {
                    formatter: (params) => {
                        return `{icon|●}{name|${params.name}}\n{value|${params.value}}\n{total|${(
                            (+params.value / +total) *
                            100
                        ).toFixed(2) || 0}%}`;
                    },
                    padding: labelPadding,
                    rich: {
                        icon: {
                            fontSize: w > 1400 ? 16 : 14,
                            align: "left",
                        },
                        name: {
                            padding: [0, 0, 0, 3],
                            fontSize,
                            align: "left",
                            color: textColor || "#333",
                        },
                        value: {
                            fontSize,
                            fontWeight: "bold",
                            padding: [2, 0, 2, 13],
                            color: textColor || "#333",
                            align: "left",
                        },
                        total: {
                            fontSize,
                            padding: [0, 0, 0, 13],
                            fontWeight: "bold",
                            color: textColor || "#333",
                            align: "left",
                        },
                    },
                    ...seriesLabel,
                },
                emphasis: emphasis || {},
            },
        ],
    };
};
