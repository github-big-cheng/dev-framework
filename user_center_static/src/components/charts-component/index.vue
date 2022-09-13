<template>
    <div class="charts-component" :id="id"></div>
</template>

<script>
// import "echarts-liquidfill";
export default {
    name: "ChartsComponent",
    data() {
        return { geojsonState: false };
    },
    props: {
        id: {
            type: String,
            required: true,
        },
        liquidfill: {
            type: Boolean,
        },
        registerMap: {
            type: String,
            required: false,
        },
        options: {
            type: Object,
            required: true,
            deep: true,
        },
    },

    mounted() {
        import(/* webpackChunkName: 'echarts' */ "echarts").then((echarts) => {
            /* 水滴的应用 */
            if (this.liquidfill) {
                // import(/* webpackChunkName: 'echarts' */ "echarts-liquidfill");
            }
            // 基于准备好的dom，初始化echarts实例
            this.myChart = echarts.init(document.getElementById(this.id));
            this.renderEcharts(echarts);
        });
    },
    watch: {
        options(newData) {
            if (!this.geojsonState) {
                this?.myChart?.setOption?.(newData);
            }
        },
    },
    methods: {
        renderEcharts(echarts) {
            if (this.registerMap) {
                this.geojsonState = true;
                import("./geojson.json").then((geojson) => {
                    echarts.registerMap(this.registerMap, geojson);
                    this?.myChart?.setOption?.(this.options);
                    this.geojsonState = false;
                });
            } else {
                this?.myChart?.setOption?.(this.options);
            }
            this.myChart.on("click", (params) => this.$emit("click", params));
        },
        resize() {
            setTimeout(() => {
                this.myChart.resize();
            }, 300);
        },
        tooltipSlideshow() {
            var index = 0; //播放所在下标
            clearInterval(this.mTime);
            this.mTime = setInterval(() => {
                this.myChart.dispatchAction({
                    type: "showTip",
                    seriesIndex: 0,
                    dataIndex: index,
                });
                index++;
                if (index >= this.option?.series[0]?.data.length) {
                    index = 0;
                }
            }, 1000);
        },
    },
    destroyed() {
        clearInterval(this.mTime);
    },
};
</script>

<style lang="scss" scoped>
.charts-component {
    width: 100%;
    height: 100%;
}
</style>
