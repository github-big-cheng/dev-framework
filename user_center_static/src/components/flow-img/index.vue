<!--
 * @Author: your name
 * @Date: 2021-03-30 18:11:16
 * @LastEditTime: 2021-08-24 11:00:44
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \zfzd_oa\src\components\activiti\index.vue
-->
<template>
    <div>
        <pageTitle title="流转过程" class="htitle"></pageTitle>
        <!-- 流程图active S -->
        <div class="step-activiti">
            <div class="node-info">
                <ul>
                    <li v-for="(item, index) in nodeflow" :key="item.type">
                        <span class="info-status" :class="'info-status'+ index"
                        ></span>
                        <span class="info-text">{{ item.text }}</span>
                    </li>
                </ul>
            </div>
            <div class="flow-img" id="activiti" @click="viewImage"></div>
            <div v-html="svgHtml"></div>
            <div class="flow-info-box" @mouseenter="flowMouseenter" @mouseleave="flowMouseleave">
                <div class="flow-info">
                    <p class="info-header" v-if="currentNodeInfo.length">
                    {{ loadCurrentStep(currentNodeInfo[0].stepId, 'name') }}:
                        <span :style="{display: 'inline-block',' margin-left': '10px', color: checkCurrentStatus(currentNodeInfo[0].stepId)}"
                            >{{ loadCurrentStep(currentNodeInfo[0].stepId, 'statusName')}}</span
                        >
                    </p>
                    <div class="table-box">
                        <p v-if="!currentNodeInfo.length">执行状态：<span class="unenforced">未执行</span></p>
                        <div else>
                            <el-table
                                border
                                :show-header="false"
                                :data="defaultProps"
                                style="width: 100%"
                                v-for="(item, i) in currentNodeInfo"
                                :key="i"
                            >
                                <el-table-column width="100">
                                    <template slot-scope="scope">
                                        <span>{{ scope.row.title }}</span>
                                    </template>
                                </el-table-column>
                                <el-table-column>
                                    <template slot-scope="scope">
                                        <span>{{ item[scope.row.attr] }}</span>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </div>
                    </div>
                </div>
            </div>
            <el-button class="btn-flow-table" :icon="showTable ?'el-icon-arrow-down' : 'el-icon-arrow-up'" @click="showTable = !showTable"></el-button>
        </div>
        <!-- 流程图active E -->
        <table-com
            :tableTit="tableTit"
            :tableData="filterList(tableData)"
            :tbLoading="false"
            :showSet="false"
            :iSelection="false"
            v-show="showTable"
            height="auto"
        >
            <template slot-scope="{scope}" slot="file">
                <upload-file class="file-box" :upFiles="scope.row.approveAttachmentList" :view="true"/>
            </template>
        </table-com>
    </div>
</template>

<script>
import pageTitle from '@/components/page-title'
import TableCom from '@/components/table'
import UploadFile from '@/components/upload-files'
import { ActivitiSVGDrawer } from "./activiti.js"
import $ from "jquery";
export default {
    name: 'flowImgCom',
    components:{
        pageTitle,
        TableCom,
        UploadFile
    },
    props: {
        resf: {
            type: Object,
            default: () => {},
        },
        processInstanceId: {
            type: String,
            default: "",
        },
        tableData: {
            type: Array,
            default: () => [],
        },
        tableTit: {
            type: Array,
            default: () => [
                {prop: "stepName", label: "任务名称", width: null},
                {prop: "startTime", label: "开始时间", width: null},
                {prop: "endTime", label: "结束时间", width: null},
                {prop: "durationMillisTime", label: "处理时长", width: null},
                {prop: "assigneeName", label: "处理人", width: null},
                {prop: "statusName", label: "审批状态", width: null},
                {prop: "comment", label: "审批意见", width: null},
            ]
        },
    },
    data() {
        return {
            width:  850,
            height: 400,
            showTable:false,
            nodeflow: [
                {
                    type: "unenforced",
                    text: "未执行",
                },
                {
                    type: "commit",
                    text: "提交",
                },
                {
                    type: "currentNode",
                    text: "当前节点",
                },
                {
                    type: "agree",
                    text: "同意",
                },
                {
                    type: "reject",
                    text: "驳回",
                },
                {
                    type: "oppose",
                    text: "反对",
                },
                {
                    type: "cancel",
                    text: "撤销",
                },
                // {
                //     type: "through",
                //     text: "会签通过",
                // },
            ],
            defaultProps: [
                {
                    title: "执行人",
                    attr: "assigneeName",
                },
                {
                    title: "开始时间",
                    attr: "startTime",
                },
                {
                    title: "结束时间",
                    attr: "endTime",
                },
                {
                    title: "处理时长",
                    attr: "durationMillisTime",
                },
                {
                    title: "状态",
                    attr: "statusName",
                },
                {
                    title: "意见",
                    attr: "comment",
                },
            ],
            nodeInfo: new Map(),
            currentNodeInfo: [],
            mouseStatus: false,
            nodeStatusMap: null
        };
    },
    computed: {
        svgHtml() {
            return `<style>
                svg {
                    width: ${this.width}px;
                    height: ${this.height}px;
                }
            </style>`;
        },
    },
    watch: {
        processInstanceId(val) {
            if (!val) return;
            this.init();
        },
        tableData: {
            handler(val) {
                let map = new Map();
                val.forEach((item) => {
                    let items = map.get(item.stepId);
                    if (items) {
                        items.unshift(item);
                        map.set(item.stepId, items);
                    } else {
                        map.set(item.stepId, [item]);
                    }
                });
                this.nodeInfo = new Map(map);
            },
            deep: true,
        },
        mouseStatus(val) {
            // val ? $(".flow-info-box").slideDown(300) : $(".flow-info-box").hide();
        },
    },
    created() {
        if (this.processInstanceId) {
            this.init();
        }
    },
    methods: {
        loadCurrentStep(stepId, key) {
            let nodeInfo = this.nodeStatusMap.get(stepId);
            return nodeInfo ? nodeInfo[key] || '' : '';
        },
        checkCurrentStatus(stepId) {
            let status = this.loadCurrentStep(stepId, 'status');
            if (status == '21010-70' || status == '21010-40') {
                return '#ff8d31';
            }
            if (status == '21010-80') {
                return '#8b0802'
            }
            return '#118af7';
        },
        init() {
            const backgroundClass = {
                "21010-10": "step-purple", // 提交
                "21010-20": "step-orange", // 待处理
                "21010-30": "step-blue", // 同意 
                "21010-40": "step-red", // 驳回
                "21010-50": "step-blue", // 完成
                "21010-70": "step-red", // 驳回到发起人
                "21010-80": "step-redImportant", // 反对
                //撤销，边框#023a62，底色#f8fafb
            };
            this.$nextTick(() => {
                this.$http.flowImg({
                    processInstanceId: this.processInstanceId
                }).then((res) => {
                    let json = res.data,
                        self = this;

                    let {actImgInfo, nodes} = json;
                    if (actImgInfo) {
                        this.height = actImgInfo.height || this.height;
                        this.width = actImgInfo.width || this.width;
                    }
                    json.backgroundClass = backgroundClass;
                    let svgHtml = ActivitiSVGDrawer().draw(json);
                    document
                        .getElementById("activiti")
                        .appendChild(svgHtml);

                    // 处理节点状态
                    if (nodes && nodes.length) {
                        this.nodeStatusMap = new Map();
                        nodes.forEach(node => {
                            this.nodeStatusMap.set(node.id, {name: node.name, status:node.status, statusName: node.statusName});
                        });
                    }

                    let userTask = $("#activiti svg g").filter(function () {
                        return $(this).attr("type") == "userTask";
                    });

                    userTask
                        .off("mouseenter")
                        .on("mouseenter", function () {
                            self.currentNodeInfo = [];
                            self.mouseStatus = true;
                            let stepId = $(this).attr("stepId");
                            let currentNodeInfo = self.nodeInfo.get(stepId);
                            currentNodeInfo &&
                                (self.currentNodeInfo = currentNodeInfo);

                            let x = $(this).find("rect").attr("x"),
                                y = $(this).find("rect").attr("y");
                            $(".flow-info-box")
                                .slideDown(300)
                                .css({
                                    left: x + "px",
                                    top: Number(y) + 66 + "px",
                                });
                        });

                    userTask.on("mouseleave", function () {
                        if ($(this).attr("type") != "userTask") return;
                        self.mouseStatus = false;
                        setTimeout(() => {
                            !self.mouseStatus && $(".flow-info-box").hide();
                        });
                    });
                });
            });
        },
        /**
         * 计算当前节点据浏览器底部或顶部高度
         */
        // computerDom(dom) {
        //     let h = dom.height(); //元素高度
        //     dom.offset().top; //元素距离顶部高度
        //     let wh = $(window).height(); //浏览器窗口高度
        //     $(document).scrollTop(); //滚动条高度
        //     let xh = wh - (h + dom.offset().top - $(document).scrollTop()); //元素到浏览器底部的高度
        // },
        flowMouseenter() {
            this.mouseStatus = true;
        },
        flowMouseleave() {
            this.mouseStatus = false;
            setTimeout(() => {
                if (!this.mouseStatus) {
                    $(".flow-info-box").hide();
                }
            });
        },
        viewImage(e) {
            // console.log(e.target)
        },
        //table
        filterList(list) {
            if (!list) {
                return [];
            }
            return list.filter(item => item.stepType && item.stepType.search('Gateway') == -1);
        }
    },
};
</script>

<style lang="scss" scoped>
    @import "@/styles/flow-step.scss";
</style>
