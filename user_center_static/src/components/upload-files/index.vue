<template>
    <div class="up-load-files">
        <el-upload
            v-if="!view && !noUpload"
            :class="{ 'upload-demo': true }"
            :action="url + upUrl"
            :data="sendData"
            :on-success="upSuccess"
            :on-error="onError"
            :on-progress="onProgress"
            :disabled="view"
        >
            <div class="flex-box">
                <el-button size="small" icon="el-icon-aliupload" class="upload-btn">
                    {{ title }}
                </el-button>
                <el-button
                    v-if="!view"
                    size="small"
                    icon="el-icon-aliback"
                    class="clear-btn"
                    :type="uploadFiles.length > 0 ? 'danger' : 'info'"
                    plain
                    @click.prevent.stop="handleClear"
                    :disabled="uploadFiles.length === 0"
                >
                    清除所有
                </el-button>
            </div>
        </el-upload>
        <draggable
            v-model="uploadFiles"
            :disabled="view || loadingFiles.length > 0"
            group="people"
            delay="100"
            @end="sortUpFiles"
            :class="{
                'upfile-list': true,
                readonly: view,
            }"
            ref="listRef"
        >
            <ul>
                <template v-for="(item, index) of uploadFiles">
                <li
                    v-if="item.fileName"
                    :key="item.filePath"
                    class="upfile-item"
                    :class="!upReadonly ? '' : 'file-curr'"
                    :index="index"
                >
                    <div class="upfile-item-left">
                        <span class="file-icon">
                            <span v-if="!view" class="drag-icon">
                                <i class="el-icon-alidrag" />
                            </span>
                            <i :class="formatFileIcon(item.fileType)" />
                        </span>
                        <span v-if="!item.editable" class="file-text readonly" :title="item.fileName">
                            <a
                                class="file-text-link"
                                :name="item.fileName"
                                :href="url + '/file' + item.filePath"
                                target="_blank"
                            >
                                {{ item.fileName }}
                            </a>
                        </span>
                        <el-input
                            v-else
                            v-focus="true"
                            ref="newFileName"
                            v-model="item.newFileName"
                            placeholder=""
                            :autofocus="true"
                            :readonly="false"
                            @input="formatFileName($event, item)"
                            class="file-text"
                        />
                        <span class="icon-modify-box" v-if="!view">
                            <i
                                v-if="!item.editable"
                                class="el-icon-alimodify"
                                @click.prevent.stop="handleEditable(item)"
                            />
                            <i
                                v-if="item.editable"
                                class="el-icon-alisubmit"
                                @click.prevent.stop="handleEditFile(true, item)"
                            />
                            <i
                                v-if="item.editable"
                                class="el-icon-close close _icon-close"
                                @click.prevent.stop="handleEditFile(false, item)"
                            />
                        </span>
                        <span v-if="item.fileSize && !item.editable && view" class="file-size">
                            ({{ item.fileSizeStr || $formatBytes(item.fileSize, 1) }})</span
                        >
                    </div>
                    <div class="upfile-item-right">
                        <span class="icon-links" v-if="!view && !item.editable">
                            <span
                                v-if="item.fileSize"
                                class="file-size"
                                :style="view ? { position: 'relative', left: '0rem' } : {}"
                                >({{ item.fileSizeStr || $formatBytes(item.fileSize, 1) }})</span
                            >
                            <a
                                :href="url + '/file' + item.filePath"
                                :name="item.fileName"
                                target="_blank"
                                :download="item.fileName"
                            >
                                <i class="el-icon-alifiledown"> </i>
                            </a>
                            <i class="el-icon-close close" @click.prevent.stop="handleRemoveFile(item)"> </i>
                        </span>
                        <span v-else-if="view" class="view-handle">
                            <a
                                class="file-download"
                                :name="item.fileName"
                                :href="url + '/file' + item.filePath"
                                target="_blank"
                            >
                                查看
                                <!-- @click.stop="ntkoOpenLocal(item)" -->
                            </a>
                            <a
                                class="file-download"
                                :href="url + '/file' + item.filePath"
                                target="_blank"
                                :download="item.fileName"
                            >
                                下载
                            </a>
                        </span>
                    </div>

                    <!-- <em>{{ item.fileName }}</em> -->
                </li>
                </template>
                <li
                    v-for="(item, index) of loadingFiles"
                    :key="index"
                    class="upfile-item"
                    :class="!upReadonly ? '' : 'file-curr'"
                >
                    <div class="upfile-item-left">
                        <div class="progress-box">
                            <div class="progress" :style="{ width: item.percent + '%' }" />
                        </div>
                        <span class="file-icon">
                            <span v-if="!view" class="drag-icon">
                                <i class="el-icon-alidrag" />
                            </span>
                            <i :class="['iconfont', formatFileIcon(item.fileType)]" />
                        </span>
                        <span class="file-text readonly" :title="item.fileName">
                            <span class="file-text-link" :name="item.fileName" target="_blank">
                                {{ item.fileName }}
                            </span>
                        </span>
                    </div>
                    <div class="upfile-item-right"></div>
                </li>
            </ul>
        </draggable>
        <div
            id="wps"
            style="
        width: 900px;
        height: 800px;
        left: 100px;
        margin-left: 20px;
        display: none;
      "
        ></div>
    </div>
</template>

<script>
import draggable from "vuedraggable";
import { getToken } from "@/utils/auth";
import { apiUrl, requestUrl } from "@/api/api";

export default {
    props: {
        upUrl: {
            type: String,
            default: () => "",
        },
        upFiles: {
            type: Array,
            default: () => [],
        },
        title: {
            type: String,
            default: () => "上传附件",
        },
        type: {
            type: String,
            default: () => "",
        },
        view: {
            type: Boolean,
            default: false,
        },
        upReadonly: {
            type: Boolean,
            default: false,
        },
        noUpload: {
            type: Boolean,
            default: false,
        },
        value: {
            type: Array,
            default: () => [],
        },
    },
    components: {
        draggable,
    },
    data() {
        return {
            url: "",
            objs: null,
            sendData: {
                _sgk: getToken(),
                groupName: this.type,
            },
            uploadFiles: [],
            map: {
                fileIcon: {
                    doc: "el-icon-aliword",
                    docx: "el-icon-aliword",
                    pdf: "el-icon-alipdf",
                    ppt: "el-icon-alippt",
                    pptx: "el-icon-alippt",
                    xls: "el-icon-aliexcel",
                    xlsx: "el-icon-aliexcel",
                    jpg: "el-icon-alipic",
                    png: "el-icon-alipic",
                },
            },
            loadingFiles: [
                // {
                //     fileName: '新建 Microsoft Word 文档.docx',
                //     fileType: 'docx',
                //     fileSize: 0,
                //     fileSizeStr: '0 Bytes',
                //     filePath:
                //         '/2021/202103/14001-10/F080598E581586D2699F2BCCB578C922.docx'
                // }
            ],
        };
    },
    watch: {
        //监听父组件传过来的type,清空sendData数据,公文流程审批根据节点动态传附件类型
        type: {
            handler(newVal, oldVal) {
                this.sendData = {
                    _sgk: getToken(),
                    groupName: newVal,
                };
            },
        },
        upUrl: {
            handler(val) {},
            immediate: true,
        },
        value: {
            handler(value) {
                this.initFiles();
            },
            deep: true,
        },
        upFiles: {
            handler(newVal, oldVal) {
                if (newVal?.length) {
                    const uploadFiles = [];
                    for (const v of newVal) {
                        const { filePath } = v;
                        const temp = this.uploadFiles.find((item) => item.filePath === filePath);
                        if (temp) {
                            uploadFiles.push(
                                Object.assign(
                                    {
                                        editable: false,
                                        newFileName: "",
                                    },
                                    temp,
                                    v
                                )
                            );
                        } else {
                            uploadFiles.push(
                                Object.assign(
                                    {
                                        editable: false,
                                        newFileName: "",
                                    },
                                    v
                                )
                            );
                        }
                    }
                    this.uploadFiles = uploadFiles;
                } else {
                    this.uploadFiles = [];
                }
            },
            deep: true,
            immediate: true,
        },
    },
    created() {
        this.url = requestUrl;
        this.initFiles();
        //this.init('wps');
    },
    directives: {
        focus: {
            inserted(el, { value }) {
                if (value) {
                    // 重点注意这里 当前元素是div  所以要查到子元素中的input
                    const dom = el.querySelector("input") || el.querySelector("textarea");
                    dom.focus();
                }
            },
        },
    },
    methods: {
        initFiles() {
            if (this.value && !this.view) {
                this.uploadFiles = this.value.map((i) => ({
                    editable: false,
                    newFileName: "",
                    ...i,
                }));
            }
        },

        async ntkoOpenLocal(item) {
            let readControl = await this.$http.uploadFile({ keies: "READ_CONTROL" }),
                filterFileType = new Set(["xls", "xlsx", "doc", "docx", "ppt", "pptx"]);

            if (
                readControl.data &&
                readControl.data.length &&
                readControl.data[0].value == 1 &&
                filterFileType.has(item.fileType)
            ) {
                const newView = this.$router.resolve({
                    path: "/viewFile",
                    query: { filePath: item.filePath },
                });
                window.open(newView.href, "_blank");
            } else {
                window.open(apiUrl + "/file" + item.filePath);
            }
        },
        onProgress(e, file, fileList) {
            const { percent } = e;
            const { uid, name: fileName } = file;
            let index = this.loadingFiles.findIndex((item) => item.uid === uid);
            if (index === -1) {
                const fileType = fileName.split(".").splice(-1);
                this.loadingFiles.push({
                    uid,
                    fileName,
                    fileType,
                    percent,
                });
                index = this.loadingFiles.findIndex((item) => item.uid === uid);
            }
            this.$set(this.loadingFiles[index], "percent", percent);
            if (percent === 100) {
                this.loadingFiles.splice(index, 1);
            }
        },
        onError() {
            this.$message.error("上传失败！");
        },
        formatFileIcon(fileType) {
            const fileIcon = this.map.fileIcon[fileType] || "el-icon-aliother";
            return fileIcon;
        },
        upSuccess(res, file) {
            let upDates = [];
            if (res.code == 0) {
                let { data } = res;
                data.forEach((item) => {
                    const filePath = item.filePath;
                    upDates.push({
                        fileName: item.fileName,
                        fileType: item.fileType,
                        fileSize: item.fileSize,
                        fileSizeStr: this.$formatBytes(item.fileSize, 1),
                        filePath,
                    });
                });
                this.$emit("upSuccess", upDates);
                this.$emit("input", [...this.value, ...upDates]);
            } else {
                this.$showError(res.message);
            }
        },
        async handleRemoveFile(item, force = false) {
            try {
                if (!force) {
                    await this.$confirm(`您确定移除 ${item.fileName}？`, "提示", { type: "warning" });
                }
                this.$emit("removeFile", item.filePath);

                if (force) {
                    this.$emit("input", []);
                } else {
                    this.$emit("input", [...this.uploadFiles.filter((i) => i.filePath !== item.filePath)]);
                }
            } catch (e) {
                console.error(e);
            }
        },
        handleEditable(item) {
            item.editable = true;
            let fileName = item.fileName.split(".");

            if (fileName.length > 1) {
                fileName = fileName.slice(0, -1).join(".");
            } else {
                fileName = item.fileName;
            }
            item.newFileName = fileName;
            // this.$forceUpdate();
        },
        handleEditFile(upReadonly, item) {
            if (upReadonly) {
                const newItem = this.uploadFiles.find((item2) => item2.filePath === item.filePath);
                const newFileName = item.newFileName;
                if (item.newFileName && item.newFileName.length > 40) {
                    this.$showWarning("文件名称应小于40个字");
                    return;
                }

                let ext;
                let fileName = item.fileName.split(".");
                if (fileName.length > 1) {
                    ext = fileName.splice(-1)[0];
                    item.fileName = newFileName + "." + ext;
                } else {
                    item.fileName = newFileName;
                }

                newItem.fileName = item.fileName;

                // this.upReadonly = true;
                this.$emit("editFile", { filePath: item.filePath, fileName: item.fileName });
            }
            item.editable = false;
        },
        async handleClear() {
            try {
                await this.$confirm(`确定清除所有文件吗`, "提示", { type: "warning" });
                for (const item of this.uploadFiles) {
                    this.handleRemoveFile(item, true);
                }
            } catch (e) {}
        },
        formatFileName(value, item) {
            var reg = /[\/\\"<>\?\*]/;
            item.newFileName = value.replace(reg, "");
        },
        sortUpFiles() {
            // const uploadFiles = [...this.uploadFiles];
            // this.upFiles.sort((a, b) => {
            //     const aIndex = uploadFiles.findIndex(
            //         (item) => item.filePath === a.filePath
            //     );
            //     const bIndex = uploadFiles.findIndex(
            //         (item) => item.filePath === b.filePath
            //     );
            //     return aIndex - bIndex;
            // });
            this.$emit("input", [...this.uploadFiles]);
            // this.noUpload && this.$emit("sortUpFiles", this.upFiles);
        },
    },
};
</script>

<style lang="scss" scoped>
@import "@/styles/upload-file.scss";
</style>
