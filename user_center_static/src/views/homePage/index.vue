<template>
    <div class="home-wrap">
        <el-row :gutter="24">
            <el-col :span="16">
                <el-row class="hm-row" :gutter="24">
                    <el-col :span="approvalAuth ? 12 : 24">
                        <div class="hm-mode">
                            <pageTitle title="通知中心" titleIcon="el-icon-alitidings"></pageTitle>
                            <el-tabs v-model="noticeActiveName" @tab-click="handleTabClick(noticeActiveName)">
                                <template v-for="n in noticesTabNames">
                                    <el-tab-pane :label="n.label" :name="n.name" :key="n.name">
                                        <span slot="label">
                                            <span class="label-span">
                                                <span>{{ n.label }}</span>
                                                <el-badge
                                                    :value="noticeNum(n.name)"
                                                    :max="99"
                                                    class="tip-badge"
                                                    :class="noticeNum(n.name) > 99 ? 'badge-big' : ''"
                                                    v-if="noticeNum(n.name)"
                                                />
                                            </span>
                                        </span>
                                        <ul
                                            class="con-list"
                                            element-loading-text="加载中..."
                                            element-loading-spinner="el-icon-loading"
                                            v-loading="noticeLoading"
                                        >
                                            <li
                                                :class="item.isRead === 0 ? 'item-unread' : ''"
                                                v-for="(item, index) in noticeList"
                                                :key="index"
                                                @click="handleLiClick(noticeActiveName, item)"
                                            >
                                                <a href="javascript:void(0)">{{ item.statusName }}{{ item.title }}</a>
                                                <span>{{ item.sendDate | date }}</span>
                                            </li>
                                        </ul>
                                        <el-link
                                            v-if="noticeTotal > 5"
                                            class="more"
                                            @click="handleMore(noticeActiveName)"
                                            >查看更多<i class="el-icon-d-arrow-right"></i
                                        ></el-link>
                                        <!-- 无数据 S-->
                                        <EmptyData v-if="!noticeTotal" />
                                    </el-tab-pane>
                                </template>
                            </el-tabs>
                        </div>
                    </el-col>
                    <el-col :span="12" v-if="approvalAuth">
                        <div class="hm-mode">
                            <pageTitle title="审批待办" titleIcon="el-icon-aliapprove"></pageTitle>
                            <el-tabs v-model="approvalActiveName" @tab-click="handleTabClick(approvalActiveName)">
                                <template v-for="n in approvalsTabNames">
                                    <el-tab-pane :label="n.label" :name="n.name" :key="n.name">
                                        <span slot="label">
                                            <span class="label-span">
                                                <span>{{ n.label }}</span>
                                                <el-badge
                                                    :value="approvalStayCount"
                                                    :max="99"
                                                    class="tip-badge"
                                                    :class="approvalStayCount > 99 ? 'badge-big' : ''"
                                                    v-if="n.name === 'approvalStay' && approvalStayCount"
                                                />
                                            </span>
                                        </span>
                                        <ul
                                            class="con-list"
                                            :class="approvalActiveName === 'approvalStay' ? 'unread-ul' : ''"
                                            element-loading-text="加载中..."
                                            element-loading-spinner="el-icon-loading"
                                            v-loading="approvalLoading"
                                        >
                                            <li
                                                v-for="(item, index) in approvalList"
                                                :key="index"
                                                @click="handleLiClick(approvalActiveName, item)"
                                            >
                                                <a href="javascript:void(0)">{{ item.title }}</a>
                                                <span>{{ item.createTime | date }}</span>
                                            </li>
                                        </ul>
                                        <el-link
                                            v-if="approvalTotal > 5"
                                            class="more"
                                            @click="handleMore(approvalActiveName)"
                                            >查看更多<i class="el-icon-d-arrow-right"></i
                                        ></el-link>
                                        <!-- 无数据 S-->
                                        <EmptyData v-if="!approvalTotal" />
                                    </el-tab-pane>
                                </template>
                            </el-tabs>
                        </div>
                    </el-col>
                </el-row>
                <el-row class="hm-row" :gutter="24" v-if="transmissionAuth || taskAuth">
                    <el-col :span="taskAuth ? 12 : 24" v-if="transmissionAuth">
                        <div class="hm-mode hm-cooperate">
                            <pageTitle
                                title="在线协作"
                                titleIcon="el-icon-aliscxd"
                                :countNum="cooperateCount"
                            ></pageTitle>
                            <ul
                                class="con-list"
                                element-loading-text="加载中..."
                                element-loading-spinner="el-icon-loading"
                                v-loading="cooperateLoading"
                            >
                                <li
                                    :class="item.confirmStatus === '27008-20' ? 'item-unread' : ''"
                                    v-for="(item, index) in cooperateList"
                                    :key="index"
                                    @click="handleLiClick('cooperate', item)"
                                >
                                    <a href="javascript:void(0)"
                                        ><span v-if="item.levelName"
                                            >【{{ item.bizTypeName }}】【{{ item.levelName }}】</span
                                        >{{ item.title }}</a
                                    >
                                    <span>{{ item.sendDate | date }}</span>
                                </li>
                            </ul>
                            <el-link v-if="cooperateTotal > 5" class="more" @click="handleMore('cooperate')"
                                >查看更多<i class="el-icon-d-arrow-right"></i
                            ></el-link>
                            <!-- 无数据 S-->
                            <EmptyData v-if="!cooperateTotal" />
                        </div>
                    </el-col>
                    <el-col :span="transmissionAuth ? 12 : 24" v-if="taskAuth">
                        <div class="hm-mode hm-task">
                            <pageTitle title="我的任务" titleIcon="el-icon-alilog" alignType="right">
                                <span slot="pageRight" class="title-right">共{{taskCount}}个任务</span>
                            </pageTitle>
                            <ChartsComponent
                                v-if="TaskOptions && Object.keys(TaskOptions).length"
                                :options="TaskOptions"
                                id="TaskChart"
                                ref="TaskChartBox"
                                class="Task-chart"
                            />
                            <!-- 无数据 S-->
                            <EmptyData v-if="TaskOptions && Object.keys(TaskOptions).length === 0" />
                        </div>
                    </el-col>
                </el-row>
                <el-row class="hm-row" :gutter="24">
                    <el-col :span="12" v-if="docAuth">
                        <div class="hm-mode">
                            <pageTitle title="我的公文" titleIcon="el-icon-alioffices"></pageTitle>
                            <el-tabs v-model="documentActiveName" @tab-click="handleTabClick(documentActiveName)">
                                <template v-for="n in documentsTabNames">
                                    <el-tab-pane :label="n.label" :name="n.name" :key="n.name">
                                        <span slot="label">
                                            <span class="label-span">
                                                <span>{{ n.label }}</span>
                                                <el-badge
                                                    :value="documentNum(n.name)"
                                                    :max="99"
                                                    class="tip-badge"
                                                    :class="documentNum(n.name) > 99 ? 'badge-big' : ''"
                                                    v-if="documentNum(n.name)"
                                                />
                                            </span>
                                        </span>
                                        <ul
                                            class="con-list"
                                            element-loading-text="加载中..."
                                            element-loading-spinner="el-icon-loading"
                                            v-loading="documentLoading"
                                        >
                                            <template>
                                                <li
                                                    :class="item.isRead == 0 ? 'item-unread' : ''"
                                                    :key="index"
                                                    @click="handleLiClick(documentActiveName, item)"
                                                    v-for="(item, index) in documentList"
                                                >
                                                    <template v-if="documentActiveName === 'sendRead'">
                                                        <a href="javascript:void(0)">{{ item.title }}</a>
                                                        <span>{{ item.sendDate | date }}</span>
                                                    </template>
                                                    <template v-else>
                                                        <a href="javascript:void(0)">{{ item.docName }}</a>
                                                        <span>{{ item.opDate | date }}</span>
                                                    </template>
                                                </li>
                                            </template>
                                        </ul>
                                        <el-link
                                            v-if="documentTotal > 5"
                                            class="more"
                                            @click="handleMore(documentActiveName)"
                                            >查看更多<i class="el-icon-d-arrow-right"></i
                                        ></el-link>
                                        <!-- 无数据 S-->
                                        <EmptyData v-if="!documentTotal" />
                                    </el-tab-pane>
                                </template>
                            </el-tabs>
                        </div>
                    </el-col>
                    <el-col :span="docAuth ? 12 : 24">
                        <div class="hm-mode hm-info">
                            <pageTitle title="信息公开" titleIcon="el-icon-aliinfo-open"></pageTitle>
                            <ul
                                class="con-list"
                                element-loading-text="加载中..."
                                element-loading-spinner="el-icon-loading"
                                v-loading="infoOpenLoading"
                            >
                                <li
                                    :class="item.isRead == 0 ? 'item-unread' : ''"
                                    v-for="(item, index) in infoOpenList"
                                    :key="index"
                                    @click="handleLiClick('infoOpen', item)"
                                >
                                    <a href="javascript:void(0)">{{ item.statusName }}{{ item.title }}</a>
                                    <span>{{ item.publishDate | date }}</span>
                                </li>
                            </ul>
                            <el-link v-if="infoOpenTotal > 5" class="more" @click="handleMore('infoOpen')"
                                >查看更多<i class="el-icon-d-arrow-right"></i
                            ></el-link>
                            <!-- 无数据 S-->
                            <EmptyData v-if="!infoOpenTotal" />
                        </div>
                    </el-col>
                </el-row>
            </el-col>
            <el-col :span="8" class="hm-sider">
                <el-row :gutter="24">
                    <div class="hm-mode hm-calendar">
                        <WorkPlan></WorkPlan>
                    </div>
                </el-row>
                <el-row :gutter="24">
                    <div class="hm-mode hm-dynamic-info">
                        <pageTitle title="动态信息" titleIcon="el-icon-chat-line-round"></pageTitle>
                        <div class="dynamic-info">
                            <el-timeline>
                                <el-timeline-item
                                    v-for="(item, index) in dynamicInfoList"
                                    :key="index"
                                    :timestamp="item.publishDate"
                                    placement="top"
                                >
                                    <a href="javascript:void(0)">
                                        <span @click.stop="handleLiClick('dynamicInfo', item)"
                                            >[{{ item.statusName }}]{{ item.title }}</span
                                        >
                                    </a>
                                </el-timeline-item>
                            </el-timeline>
                            <!-- 无数据 S-->
                            <EmptyData v-if="!dynamicInfoList" />
                        </div>
                    </div>
                </el-row>
            </el-col>
        </el-row>
    </div>
</template>

<script>
import { mapGetters } from "vuex";
import pageTitle from "@/components/page-title";
import EmptyData from "@/components/empty";
import WorkPlan from "./component/workplan";
import ChartsComponent from "@/components/charts-component";
import { flowRouter } from "@/utils/flowRouter";
import { loopOptions, w } from "@/components/charts-component/options";
import { noticesTabNames, approvalsTabNames, documentsTabNames, openInfoTabNames } from "./config";
import { getLocalStorage } from "@/utils/auth";

export default {
    name: "home",
    components: {
        pageTitle,
        EmptyData,
        WorkPlan,
        ChartsComponent,
    },
    data() {
        return {
            //通知
            noticeActiveName: "notice",
            noticesTabNames,
            noticeLoading: "",
            noticeList: [],
            noticeTotal: 0,
            //审批
            approvalActiveName: "approvalStay",
            approvalsTabNames,
            approvalLoading: "",
            approvalList: [],
            approvalTotal: 0,
            approvalStayCount: 0,
            //公文
            documentActiveName: "receiveRead",
            documentsTabNames,
            documentLoading: "",
            documentList: [],
            sendList: [],
            documentTotal: 0,
            sendDocCount: 0,
            receiveDocCount: 0,
            //协作
            cooperateLoading: "",
            cooperateList: [],
            cooperateTotal: 0,
            cooperateCount: 0,
            //信息公开
            infoOpenLoading: "",
            openInfoTabNames,
            infoOpenList: [],
            infoOpenTotal: 0,
            //动态信息
            dynamicInfoLoading: "",
            dynamicInfoList: [],
            //我的任务
            taskCount:"",
            charts: [],
            TaskOptions: {},
            Task: {},
            params: {},
            standSize: document.body.clientWidth,
            selectClData: {
                未确认: "#7868F8",
                进行中: "#F6C02C",
                已完成: "#61DAAC",
                已延期: "#6596F8",
            },
            //权限
            userInfo: "",
            approvalAuth: false,
            transmissionAuth: false,
            docAuth: false,
            taskAuth: false,
        };
    },

    filters: {
        date(time) {
            let oldDate = new Date(time);
            //创建补0函数
            function p(s) {
                return s < 10 ? "0" + s : s;
            }

            let month = oldDate.getMonth() + 1;
            let day = oldDate.getDate();
            return p(month) + "-" + p(day);
        },
    },
    created() {
        this.userInfo = getLocalStorage("userInfo");
        //通知
        this.getTabList("getNewsList", this.noticesTabNames[0].params);
        //审批
        if (this.oaflowMap("oa_flow_list_approval")) {
            this.approvalAuth = true;
            this.getTabList("getFlowApprovallist", this.approvalsTabNames[0].params);
        }
        //协作办公
        if (this.oaflowMap("platform_transmission_module")) {
            this.transmissionAuth = true;
            this.getTabList("getTransMissionList", "", "my");
        }
        //我的公文
        if (this.oaflowMap("oa_doc_query")) {
            this.docAuth = true;
            this.documentsTabNames = this.documentsTabNames.map((item) => {
                item.show = this.userInfo && new Set(this.userInfo.authCodes).has(item.code);
                return item;
            });
            this.getTabList("docReceiveQueryList" || "docSendQueryList");
        }
        //我的任务
        if (this.oaflowMap("oa_supervision_task_detail_list")) {
            this.taskAuth = true;
            this.setTaskOptions();
            this.onResize();
        }
        //信息公开
        this.getTabList("getInfoOpenCombox", this.openInfoTabNames[0].params);
        //动态信息
        this.getTabList("partyIndexCombox");
    },
    computed: {
        ...mapGetters(["noticeCount", "meetingCount", "systemCount"]),
    },
    methods: {
        oaflowMap(code) {
            return this.userInfo && new Set(this.userInfo.authCodes).has(code);
        },
        noticeNum(attr) {
            let value;
            switch (attr) {
                case "notice":
                    value = this.noticeCount;
                    break;
                case "noticeMeet":
                    value = this.meetingCount;
                    break;
                case "noticeSys":
                    value = this.systemCount;
                    break;
            }
            return value;
        },
        documentNum(attr) {
            let value;
            switch (attr) {
                case "receiveRead":
                    value = this.receiveDocCount;
                    break;
                case "sendRead":
                    value = this.sendDocCount;
                    break;
            }
            return value;
        },
        //获取列表数据
        async getTabList(apiName, params, type, pageNo = 1, pageSize = 5) {
            try {
                const { code, data } = await this.$http[apiName]({ ...params, pageNo, pageSize }, type);
                if (code !== 0) return;
                //通知
                if (apiName == "getNewsList") {
                    this.noticeLoading = false;
                    const { list, total, unRead } = data;
                    this.noticeList = list;
                    this.noticeTotal = total;
                }
                //审批
                if (apiName == "getFlowApprovallist") {
                    this.approvalLoading = false;
                    const { list, total } = data;
                    this.approvalList = list;
                    this.approvalTotal = total;
                    const flag = list.some(item => item.status === "21002-30")
                    if(flag){
                        this.approvalStayCount = total;
                    }
                }
                //公文
                if (apiName == "docReceiveQueryList" || apiName == "docSendQueryList") {
                    this.documentLoading = false;
                    const { list, total, unRead } = data;
                    this.documentList = list;
                    this.documentTotal = total;
                    if (this.documentActiveName === "receiveRead") {
                        this.receiveDocCount = unRead;
                    } else {
                        this.sendDocCount = unRead;
                    }
                }
                //协作办公
                if (apiName == "getTransMissionList") {
                    this.cooperateLoading = false;
                    const { list, total, unRead } = data;
                    this.cooperateList = list;
                    this.cooperateTotal = total;
                    this.cooperateCount = unRead;
                }
                //信息公开
                if (apiName == "getInfoOpenCombox") {
                    this.infoOpenLoading = false;
                    const { list, total, unRead } = data;
                    this.infoOpenList = list;
                    this.infoOpenTotal = total;
                }
                //动态信息
                if (apiName == "partyIndexCombox") {
                    this.dynamicInfoLoading = false;
                    const { list } = data;
                    this.dynamicInfoList = list;
                }
            } catch (e) {
                console.log("getTabList:e", e);
            }
        },

        handleTabClick(activeName) {
            //通知
            if (activeName === "notice") {
                this.getTabList("getNewsList", this.noticesTabNames[0].params);
            } else if (activeName === "noticeMeet") {
                this.getTabList("getNewsList", this.noticesTabNames[1].params);
            } else if (activeName === "noticeSys") {
                this.getTabList("getNewsList", this.noticesTabNames[2].params);
            } else if (activeName === "approvalStay") {
                //审批
                this.getTabList("getFlowApprovallist", this.approvalsTabNames[0].params);
            } else if (activeName === "approvalAlready") {
                this.getTabList("getFlowApprovallist", this.approvalsTabNames[1].params);
            } else if (activeName === "approvalCompleted") {
                this.getTabList("getFlowApprovallist", this.approvalsTabNames[2].params);
            } else if (activeName === "receiveRead") {
                //公文
                this.getTabList("docReceiveQueryList");
            } else if (activeName === "sendRead") {
                this.getTabList("docSendQueryList");
            }
        },
        handleLiClick(name, row) {
            const { bizType, bizId, id, status } = row;
            if (name == "noticeSys") {
                this.$store.dispatch("GetNoticeSum");
                if (bizType === "10041-150") {
                    this.goView("noticeCar", "", "", bizId);
                } else if (bizType === "10041-160") {
                    this.goView("noticeContract", "all", "", bizId);
                } else {
                    this.goView("noticeSystem", "", id);
                }
            } else if (name == "notice") {
                this.$http.getNewsRead({idQueryIn: id, sendType: "10042-10"}).then((res) => {
                    if (res.code == 0) {
                        this.$store.dispatch("GetNoticeSum");
                    }
                });
                //通知
                this.goView("noticeDetail", "", "", bizId);
            } else if (name == "noticeMeet") {
                this.$http.getNewsRead({idQueryIn: id, sendType: "10042-10"}).then((res) => {
                    if (res.code == 0) {
                        this.$store.dispatch("GetNoticeSum");
                    }
                });
                //会议通知
                this.goView("noticeMeeting", "", "", bizId);
            } else if (name == "approvalStay" || name == "approvalAlready" || name == "approvalCompleted") {
                //待审核、已审核、已办结
                let authMap = flowRouter[bizType];
                if (authMap) {
                    this.goView(this.$filterPath(authMap.APPROVAL_VIEW), "approvalView", bizId);
                }
            } else if (name == "sendRead") {
                //发文库
                this.goView("sendPreview", "", id);
            } else if (name == "receiveRead") {
                //收文库
                this.goView("receivePreview", "", id);
            } else if (name == "cooperate") {
                //在线协作
                this.goView("myTransmitView", "", id, "", row.switchRecordObjId);
            } else if (name == "infoOpen") {
                //信息公开
                this.goView("indexInfoOpenDetail", "", id);
            } else if (name == "dynamicInfo") {
                //动态信息
                this.goView("dynamicInfoDetail", status, id);
            }
        },
        handleMore(type) {
            if (type == "notice") {
                //通知
                this.goView("noticeManager");
            } else if (type == "noticeMeet") {
                //会议通知
                this.goView("noticeManager");
            } else if (type == "noticeSys") {
                //系统消息
                this.goView("noticeManager");
            } else if (type == "approvalStay") {
                //待审批
                this.goView("myApprovalList");
            } else if (type == "approvalAlready") {
                //已审批
                this.goView("myApprovalList");
            } else if (type == "approvalCompleted") {
                //已办结
                this.goView("myApprovalList");
            } else if (type == "cooperate") {
                //在线协作
                this.goView("myTransmit");
            } else if (type == "receiveRead") {
                //收文阅读
                this.goView("queryList");
            } else if (type == "sendRead") {
                //发文阅读
                this.goView("queryList");
            } else if (type == "infoOpen") {
                //信息公开
                this.goView("infoOpenView");
            }
        },

        setTableRowClassName(row) {
            if (row.emailOpState === 0) {
                return "unread-row";
            }
            return "";
        },
        goView(name, type, id, bizId, switchRecordObjId) {
            this.$router.push({
                name: name,
                params: {
                    type,
                    id,
                    bizId,
                    switchRecordObjId,
                    noCache: true,
                },
            });
        },

        async setTaskOptions() {
            try {
                const _that = this;

                const { data } = await this.$http.getIndexTask(this.params);
                this.Task = data;
                const taskSizeArr = data.map(item => item.size);
                this.taskCount = taskSizeArr.reduce(function(prev, cur) {
                    return Number(prev) + Number(cur);
                }, 0)
                this.TaskOptions = loopOptions({
                    radius: w > 1400 ? "60%" : "60%", //调整圆大小
                    labelPadding: [0, 10],
                    minAngle: 10, // 设置每块扇形的最小占比
                    avoidLabelOverlap: false,
                    hoverAnimation: false, //是否开启 hover 在扇区上的放大动画效果
                    silent: true,
                    label: {
                        //饼图的指示折线
                        normal: {
                            show: true,
                            formatter: "{d}%",
                            textStyle: {
                                fontSize: this.standSize / 150,
                                color: "#fff",
                            },
                        },
                    },
                    labelLine: {
                        show: true,
                        //标签的视觉引导线样式
                        normal: {
                            smooth: 0.2,
                            length: this.standSize / 150,
                            length2: this.standSize / 150,
                        },
                    },
                    legend: {
                        show: true,
                        orient: "vertical",
                        icon: "circle",
                        itemWidth: 10,
                        itemHeight: 10,
                        top: 5,
                        left: 10,
                        formatter: "{name}",
                        textStyle: {
                            color: "#333",
                            fontWeight: 400,
                            fontSize: "0.075rem",
                        },
                    },
                    itemStyle: {
                        borderRadius: 0,
                        borderColor: "#fff",
                        borderWidth: 0,
                    },
                    //移上去放大
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 5,
                            shadowOffsetX: 0,
                            shadowColor: "rgba(0, 0, 0, 0.2)",
                        },
                    },
                    data: data.map((i) => {
                        return {
                            value: i.size,
                            name: i.name,
                            itemStyle: {
                                color: this.selectClData[i.name],
                            },
                        };
                    }),
                    seriesLabel: {
                        show:true, 
                        normal: {
                            show: true,
                            // position: "inside", //标签的位置
                            textStyle: {
                                fontWeight: 300,
                                fontSize: '0.075rem', //文字的字体大小
                                // color: "#fff",
                            },
                            // formatter: "{c}",
                        },
                    },
                    seriesCenter: ["60%", "40%"], //饼图的位置
                    tooltip: {
                        trigger: "item",
                        formatter: function(params) {
                            let color = _that.selectClData[params.name];
                            return `<strong style='display:block; margin-bottom:10px'>我的任务</strong><span style='color:${color};'>● ${params.name}</span>：${params.value}个 占比：${params.percent}%`;
                        },
                    },
                });
            } catch (e) {
                console.warn(e);
            }
        },

        onResize() {
            window.onresize = () => {
                this.$refs.TaskChartBox?.resize();
            };
        },
    },
};
</script>

<style lang="scss" scoped>
@import "@/styles/home.scss";
</style>
