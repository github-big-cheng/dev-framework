<!--
 * @Author: your name
 * @Date: 2021-06-02 09:56:58
 * @LastEditTime: 2021-08-05 11:29:53
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \do_ucenter_fe\src\components\svg-icon\index.vue
-->
<template>
    <svg :class="svgClass" aria-hidden="true">
        <use :xlink:href="iconName"></use>
    </svg>
</template>

<script>
    export default {
        name: 'svgIconCom',
        props: {
            iconClass: {
                default: 'tree-filebox',
                required: true
            },
            className: {
                type: String,
                default: ''
            }
        },
        computed: {
            iconName() {
                return `#icon-${this.iconClass}`
            },
            svgClass() {
                if (this.className) {
                    return 'svg-icon ' + this.className
                } else {
                    return 'svg-icon'
                }
            }
        }
    }
</script>

<style lang="scss" scoped>
    .svg-icon {
        width: 1em;
        height: 1em;
        vertical-align: -0.15em;
        fill: currentColor;
        overflow: hidden;
    }
</style>
