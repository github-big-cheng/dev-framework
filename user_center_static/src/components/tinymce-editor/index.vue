<template>
    <div class="tinymce-editor">
        <editor
                v-model="myValue"
                :init="init"
                :disabled="disabled"
                @onClick="onClick"
        >
        </editor>
    </div>
</template>
<script>
    import tinymce from 'tinymce/tinymce';
    import Editor from '@tinymce/tinymce-vue';
    import 'tinymce/themes/silver';
    // 编辑器插件plugins
    // 更多插件参考：https://www.tiny.cloud/docs/plugins/
    import 'tinymce/plugins/image'; // 插入上传图片插件
    import 'tinymce/plugins/media'; // 插入视频插件
    import 'tinymce/plugins/table'; // 插入表格插件
    import 'tinymce/plugins/lists'; // 列表插件
    import 'tinymce/plugins/wordcount'; // 字数统计插件
    import 'tinymce/plugins/fullscreen'; // 全屏展示插件
    import 'tinymce/icons/default';
    import {getToken} from '@/utils/auth';

    export default {
        name: 'tinymceEditorCom',
        components: {
            Editor
        },
        props: {
            value: {
                type: String,
                default: ''
            },
            // 基本路径，默认为空根目录，如果你的项目发布后的地址为目录形式，
            // 即abc.com/tinymce，baseUrl需要配置成tinymce，不然发布后资源会找不到
            baseUrl: {
                type: String,
                default: ''
            },
            disabled: {
                type: Boolean,
                default: false
            },
            plugins: {
                type: [String, Array],
                default: 'lists image media table wordcount fullscreen'
            },
            toolbar: {
                type: [String, Array],
                default:
                    'formatselect | fontsizeselect bold italic forecolor backcolor | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | lists image media table | removeformat fullscreen | undo redo'
            },
            height: {
                type: Number,
                default: 300
            },
            groupName: {
                type: String,
                default: '14001-70'
            }
        },
        watch: {
            value(newValue, oldValue) {
                let value = newValue;
                // value = this.removeFontFamily(value || '');
                // if (!oldValue) {
                //     value = this.removeFontSize(value || '');
                // }
                this.myValue = value;
            },
            myValue(newValue, oldValue) {
                let value = newValue;
                this.$emit('input', value);
            }
        },
        data() {
            return {
                init: {
                    language_url: `${this.baseUrl}/tinymce/langs/zh_CN.js`,
                    language: 'zh_CN',
                    skin_url: `${this.baseUrl}/tinymce/skins/ui/oxide`,
                    content_css: `${this.baseUrl}/tinymce/skins/content/default/content.css`,
                    content_style: ".mce-content-body {font-size:12px;font-family:'Microsoft YaHei', '微软雅黑', 'Arial', sans-serif;}",
                    // skin_url: `${this.baseUrl}/tinymce/skins/ui/oxide-dark`, // 暗色系
                    // content_css: `${this.baseUrl}/tinymce/skins/content/dark/content.css`, // 暗色系
                    height: this.height,
                    plugins: this.plugins,
                    toolbar: this.toolbar,
                    branding: false,
                    convert_urls: false,
                    menubar: false,
                    fontsize_formats: '12px 14px 18px 24px 36px',
                    // 此处为图片上传处理函数
                    images_upload_handler: (blobInfo, success, failure) => {
                        let formData = new FormData();
                        formData.append(
                            'file',
                            blobInfo.blob(),
                            blobInfo.filename()
                        );
                        formData.append('_sgk', getToken());
                        formData.append('groupName', this.groupName);
                        this.$http.uploadFile(formData).then((res) => {
                            if (res.code == 0) {
                                const {data} = res;
                                success(
                                    '/file' +
                                    data[0].filePath
                                );
                            }
                        });
                    }
                },
                myValue: this.value
            };
        },
        mounted() {
            tinymce.init({});
        },
        methods: {
            // 添加相关的事件，可用的事件参照文档=> https://github.com/tinymce/tinymce-vue => All available events
            // 需要什么事件可以自己增加
            onClick(e) {
                this.$emit('onClick', e, tinymce);
            },
            // 可以添加一些自己的自定义事件，如清空内容
            clear() {
                this.myValue = '';
            },
            unique(arr) {
                const result = [];
                const hash = {};
                let elem;
                for (let i = 0; (elem = arr[i]) != null; i++) {
                    if (!hash[elem]) {
                        result.push(elem);
                        hash[elem] = true;
                    }
                }
                return result;
            },
            removeFontFamily(contnet) {
                let arr = contnet.match(/(font-family:[^><;"]*(;)?)/gi);
                if (!arr) {
                    return contnet;
                }
                arr = this.unique(arr);
                for (let i = 0; i < arr.length; i++) {
                    const reg = new RegExp(arr[i], 'ig');
                    contnet = contnet.replace(reg, '');
                }
                return contnet;
            },
            removeFontSize(contnet) {
                let arr = contnet.match(/(font-size:[^><;"]*(;)?)/gi);
                if (!arr) {
                    return contnet;
                }
                arr = this.unique(arr);
                for (let i = 0; i < arr.length; i++) {
                    const reg = new RegExp(arr[i], 'ig');
                    contnet = contnet.replace(reg, '');
                }
                return contnet;
            }
        },

    };
</script>

<style lang="scss">
    .tinymce-editor {
        width: 900px;

        .tox {
            .tox-tbtn {
                transform: scale(0.8);
                width: 24px;
                height: 24px;

                &.tox-tbtn--select {
                    width: auto;
                    margin: 0px 0 3px 0;

                    .tox-tbtn__select-label {
                        font-size: 14px;
                        width: 3em;
                    }
                }
            }
        }
    }
</style>
