<!--
 * @Author: your name
 * @Date: 2021-03-05 17:54:59
 * @LastEditTime: 2022-03-08 16:10:39
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \zfzd_oa\src\components\Dialog\index.vue
-->
<template>
    <el-dialog
        ref="_dialog"
        class="dialog-box"
        :width="width"
        :class="className"
        :append-to-body="isAppendBody"
        :visible.sync="dialogVisible"
        :center="alignType"
        :before-close="beforeClose"
        :close-on-click-modal="closeOnClickModal"
        :destroy-on-close="isCloseDestroy"
        @closed="closed"
    >
        <div slot="title" class="dialog-title">
            <i :class="['iconfont', iconfont]"></i> <span>{{ title }}</span>
        </div>
        <slot></slot>
        <div v-if="isShowFooter" slot="footer" :class="['dialog-footer', dialogFooterLeft ? 'dialog-footer-flex' : '']">
            <slot class="dialog-footer-left" name="footerLeft" v-if="dialogFooterLeft"></slot>
            <div class="dialog-footer-right">
                <template v-if="btnConfig.length">
                    <el-button
                        v-for="(item, i) in btnConfig"
                        :key="i"
                        :type="item.type || 'primary'"
                        @click="handleClick(item.handlerType)"
                        :loading="item.btnLoading"
                    >
                        {{ item.text }}
                    </el-button>
                </template>
                <el-button v-if="showTrueBtn" type="primary" @click="handleTrueClick" :loading="btnLoading">
                    {{ trueText }}
                </el-button>
                <el-button v-if="showCancelBtn" @click="handleCancelClick">{{ cancelText }}</el-button>
            </div>
        </div>
    </el-dialog>
</template>

<script>
export default {
    name: 'dialogCom',
    props: {
        dialogVisible: {
            type: Boolean,
            default: () => false,
        },
        isCloseDestroy: {
            type: Boolean,
            default: () => false,
        },
        isAppendBody: {
            type: Boolean,
            default: () => true,
        },
        width: {
            type: String,
            default: "",
        },
        iconfont: {
            type: String,
            default: "",
        },
        title: {
            type: String,
            default: () => "",
        },
        alignType: {
            //
            type: Boolean,
            default: () => false,
        },
        btnLoading: {
            type: Boolean,
            default: () => false,
        },
        className: {
            type: String,
            default: () => "",
        },
        showTrueBtn: {
            type: Boolean,
            default: () => true,
        },
        showCancelBtn: {
            type: Boolean,
            default: () => true,
        },
        trueText: {
            type: String,
            default: () => "确 定",
        },
        cancelText: {
            type: String,
            default: () => "取 消",
        },
        btnConfig: {
            type: Array,
            default: () => [],
        },
        dialogFooterLeft: {
            type: Boolean,
            default: () => false,
        },
        closeOnClickModal: {
            default: true
        },
        //是否显示dialog按钮
        isShowFooter: {
            type: Boolean,
            default: () => true
        }
    },
    methods: {
        handleClick(type) {
            this.$emit("handlerType", type);
        },
        handleCancelClick() {
            this.$emit("cancelClick");
        },
        handleTrueClick() {
            this.$emit("trueClick");
        },
        beforeClose() {
            this.$emit("cancelClick");
            return false;
        },
        closed() {
           /*  this.$nextTick(() => {
                this.$refs._dialog.$el.firstElementChild.removeAttribute("style");
            }); */
        },
    },
};
</script>
<style lang="scss" scoped>
@import "@/styles/dialog.scss";
</style>
