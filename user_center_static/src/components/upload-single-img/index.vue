<template>
    <el-upload
            class="uploader-single-img"
            :action="url + actionUrl"
            :accept="actionType"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
            :data="sendData"
            v-loading="isLoading"
            element-loading-text="上传中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            :disabled="disable"
    >
        <img v-if="imagePath" :src="imagePath" class="avatar"/>
        <template v-else>
            <i class="el-icon-plus avatar-uploader-icon"></i>
            <div class="el-upload__text">{{ title }}</div>
        </template>
    </el-upload>
</template>

<script>
    import {getToken} from "@/utils/auth";
    import {requestUrl} from '@/api/api'
    import {staticFilePathFormat} from "../../filters";

    export default {
        name: 'uploadSingleImgCom',
        props: {
            actionUrl: {
                type: String,
                default: () => "",
            },
            actionType: {
                type: String,
                default: () => ".jpg,.jpeg,.png,.JPG,.JPEG,.PBG",
            },
            maxSize: {
                type: Number,
                default: () => 3,
            },
            type: {
                type: String,
                default: () => "",
            },
            imageUrl: {
                type: String,
                default: () => "",
            },
            disable: {
                type: Boolean,
                default: () => false,
            },
            title: {
                type: String,
            },
            value: {
                type: String,
            },
        },
        created() {
            // this.url = window.location.origin;
            this.url = requestUrl;
            this.imagePath = this.value ? staticFilePathFormat(this.value) : "";
        },
        data() {
            return {
                imagePath: "",
                isLoading: false,
                url: "",
                sendData: {
                    _sgk: getToken(),
                    groupName: this.type,
                },
            };
        },
        watch: {
            imageUrl(url) {
                this.imagePath = staticFilePathFormat(url);
            },
            value(url) {
                this.imagePath = staticFilePathFormat(url);
            },
        },
        methods: {
            handleAvatarSuccess(res, file) {
                if (res.code == 0) {
                    this.imagePath = staticFilePathFormat(res.data[0].filePath);
                    this.$emit("upLoadImg", res.data[0].filePath);
                    this.$emit("upLoadImgFullInfo", res.data[0]);
                    this.$emit("input", res.data[0].filePath);
                } else {
                    this.$showError(res.message);
                }
                this.isLoading = false;
            },
            beforeAvatarUpload(file) {
                const isLt = file.size / 1024 / 1024 < this.maxSize;
                if (!isLt) {
                    this.$message.error(`上传头像图片大小不能超过 ${this.maxSize}MB!`);
                    return false;
                }
                this.isLoading = true;
                return true;
            },
        },
    };
</script>

<style lang="scss" scoped>
    @import "@/styles/upload-singleimg.scss";
</style>
