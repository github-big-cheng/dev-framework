<template>
  <div>
    <el-upload
      :action="url + actionUrl"
      :list-type="listType"
      :limit="limit"
      :on-preview="handlePictureCardPreview"
      :on-remove="handleRemove"
      :on-error="onError"
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
      <i class="el-icon-plus"></i>
    </el-upload>
    <el-dialog :visible.sync="dialogVisible">
      <img width="100%" :src="dialogImageUrl" alt="" />
    </el-dialog>
  </div>
</template>

<script>
import { getToken } from "@/utils/auth";
import { stringify } from "qs";

export default {
  data() {
    return {
      url: "",
      isLoading: false,
      imagePath: "",
      dialogImageUrl: "",
      dialogVisible: false,
      sendData: {
        _sgk: getToken(),
        groupName: this.type,
      },
    };
  },
  props: {
    listType: {
      type: String,
      default: () => "picture-card",
    },
    limit: {
      type: Number,
      default: () => 5,
    },
    uploadType: {
      type: String,
    },
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
    disable: {
      type: Boolean,
      default: () => false,
    },
    value: {
      type: String,
    },
    type: {
      type: String,
    },
  },
  watch: {
    /*  value(url) {
      this.imagePath = process.env.VUE_APP_BASE_API + "/file" + url;
    }, */
  },
  created() {
    this.url = process.env.VUE_APP_BASE_API;
  },
  methods: {
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    handleAvatarSuccess(res, file) {
      if (res.code == 0) {
        let filePath = this.url + "/file" + res.data[0].filePath;
        this.imagePath = filePath;
        this.$emit("upLoadImg", res.data[0].filePath);
        this.$emit("input", res.data[0].filePath);
      } else {
        this.$showError(res.message);
      }
      this.isLoading = false;
    },
    onError() {
      this.$message.error(`上传失败!`);
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