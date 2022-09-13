<!--
 * @Author: your name
 * @Date: 2021-06-15 14:27:13
 * @LastEditTime: 2021-09-03 01:00:49
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \gssp_fe\src\components\view-com\index.vue
-->
<template>
    <div :class="['view-content', isImgShow ? 'view-has-img' : '']" ref="viewCom">
        <page-title v-if="headTitle" :title="headTitle" :isTitleBg="isTitleBg"></page-title>
        <ul class="view-list" :class="columnNum">
            <template v-for="(item, index) in viewConfigs">
                <li :class="item.class" :key="index" v-if="item.show !== false">
                    <span class="view-tit" :class="titClass" v-if="item.label">{{ item.label }}</span>
                    <div class="view-con">
                        <slot v-if="item.slotName" :name="item.slotName" :data="item"></slot>

                        <template v-else-if="item.type === 'uploadFile'">
                            <UpFile
                                    class="file-box"
                                    :upFiles="item.content"
                                    :view="true"
                                    v-if="item.content && item.content.length"
                            />
                            <span v-else>-</span>
                        </template>

                        <div v-else-if="item.type == 'renderHtml'" v-html="item.content || '-'"></div>

                        <span v-else>{{ item.content | formatText }} </span>
                    </div>
                </li>
            </template>
        </ul>

        <div class="user-img" v-if="isImgShow">
            <img :src="imgPath ? url + imgPath : ''" alt=""/>
        </div>

        <div class="form-button" v-if="showBack">
            <el-button type="primary" icon="el-icon-arrow-left" @click="goBack($route)">返回</el-button>
            <template v-if="isShowExamine">
                <template v-for="(item, index) in btnConfig">
                    <el-button
                        :key="index"
                        :type="item.type"
                        :class="item.class"
                        :icon="item.icon"
                        :loading="item.btnLoading"
                        @click="handlerClick(item)"
                        :disabled="item.disabled"
                        v-if="item.show === false ? item.show : true"
                    >{{ item.text }}</el-button>
                </template>
            </template>
        </div>
        <slot v-else name="workFlow"></slot>

        <div
                class="status-chapter"
                :class="statusChapter.color"
                v-if="statusChapter && Object.keys(statusChapter).length"
        >
            <span>{{ statusChapter.text }}</span>
        </div>
    </div>
</template>

<script>
    import pageTitle from "@/components/page-title";
    import UpFile from "@/components/upload-files";
    import {requestUrl} from "@/api/api";

    export default {
        components: {
            pageTitle,
            UpFile,
        },
        props: {
            headTitle: {
                type: String,
                default: "",
            },
            isTitleBg: {
                type: Boolean,
                default: false,
            },
            viewConfigs: {
                type: Array,
                default: () => [],
            },
            columnNum: {
                type: String,
                default: "",
            },
            titClass: {
                type: String,
                default: "",
            },
            isImgShow: {
                type: Boolean,
                default: false,
            },
            imgPath: {
                type: String,
                default: "",
            },
            isAnnex: {
                type: Boolean,
                default: false,
            },
            showBack: {
                type: Boolean,
                default: true,
            },
            isShowExamine: {
                type: Boolean,
                default: false,
            },
            btnDisabled: {
                type: Boolean,
                default: false,
            },
            btnConfig: {
                type: Array,
                default: () => [],
            },
            statusChapter: {
                type: Object,
                default: () => {
                },
            },
        },
        data() {
            return {
                url: "",
            };
        },
        mounted() {
            this.isImgShow ? (this.url = requestUrl + '/file') : '';
        },
        methods: {
            handlerClick(item) {
                this.$emit(
                    "submit",
                    {
                        handlerType: item.handlerType,
                        args: item.args,
                    },
                    item
                );
            },
        },
    };
</script>

<style lang="scss" scoped>
    @import "@/styles/view.scss";
</style>
