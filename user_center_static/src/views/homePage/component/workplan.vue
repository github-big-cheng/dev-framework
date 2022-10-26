<template>
    <div class="work-plan">
        <div class="date-header">
            <p>
                <i class="el-icon-date"></i>您好，今天是：<br/>{{ currentYear }}年{{ currentMonth }}月{{ currentDay }}日 {{ dayWeekName }}
            </p>
            <em>{{ currentTime }}</em>
        </div>
        <div class="date-content">
            <div class="show-date">
                <div class="date"><span>{{ year }}年</span><span>{{ month }}月</span></div>
                <p>
                    <span class="next-month" @click="handleNext"><i class="el-icon-arrow-down"></i></span>
                    <span class="pre-month" @click="handlePrev"><i class="el-icon-arrow-up"></i></span>
                </p>
            </div>
            <div class="week-header">
                <div v-for="item in ['一', '二', '三', '四', '五', '六', '日']" :key="item">{{ item }}</div>
            </div>
            <div class="week-day">
                <div class="every-day" v-for="item in calenderDateList" :key="item.index">
                    <!-- 当月，上一个月，下一个月 -->
                    <div
                            v-if="item.currentMonth"
                            :class="
                            (item.haveWork ? 'haveWork ' : '') +
                                (selectedWorkDay == item.date ? 'active-day' : '') +
                                (item.weekday == 6 || item.weekday == 0 ? ' week-end' : '')
                        "
                    >
                        <a @click="setSelectedWorkDay(item)">{{ item.day }}</a>
                    </div>
                    <div class="other-day" v-else>
                        <a @click="setSelectedWorkDay(item)">{{ item.day }}</a>
                    </div>
                </div>
            </div>
        </div>
        <div v-if="workList.length" class="work-list">
            <ul>
                <li v-for="(item, index) in workList" :key="index">
                    <span class="date">{{ item.time }}</span>
                    <a class="title" href="javascript:void(0)" @click="workEdit(item)">{{ item.title }}</a>
                    <a class="close" @click="deleteWork(item.id)"><i class="el-icon-close"></i></a>
                </li>
            </ul>
        </div>
        <div class="plan-add">
            <a @click="addWork()"> <i class="el-icon-plus"></i>添加提醒</a>
        </div>
        <dialog-com
            title="备忘录"
            iconfont="el-icon-alinote-tit"
            :dialogVisible="workDialogVisible"
            @cancelClick="workDialogVisible = false"
            @trueClick="workHandleTrueClick"
            :btnLoading="workIsAddLoading"
            className="min-dialog-form"
        >
            <form-com ref="workForm" :config="workFormAdd" columnNum="row-col1" :formData="formData"></form-com>
        </dialog-com>
    </div>
</template>

<script>
    import DialogCom from "@/components/dialog";
    import FormCom from "@/components/form-com";
    import {workFormAdd} from "../config";
    import moment from "moment";

    moment.locale("zh-cn");

    export default {
        name: "workplan",
        components: {
            DialogCom,
            FormCom,
        },
        data() {
            return {
                // 固定显示部分
                day: moment().format("D"),
                dayWeekName: moment().format("dddd"),
                // 动态选择部分
                currentYear: moment().format("YYYY"),
                currentMonth: moment().format("M"),
                currentDay:  moment().format("D"),
                year: moment().format("YYYY"),
                month: moment().format("M"),
                selectedWorkDay: moment().format("YYYY-MM-DD"),
                currentTime: moment().format("HH:mm:ss"),

                workDialogVisible: false,
                workIsAddLoading: false,
                rulesWork: {
                    title: [{required: true, message: "请输入标题", trigger: "blur"}],
                },
                pickerOptions: this.$dateConfig(),
                calenderDateList: [],
                workList: [],
                workFormAdd,
                formData: {},
            };
        },
        created() {
            this.timeClock(); // 时钟
            //初始化日历
            this.initMonthCalender();
            // 获取当天的工作计划
            this.getWorkList();
        },
        methods: {
            /**
             * 右上角时钟
             */
            timeClock() {
                setInterval(() => {
                    this.currentTime = moment().format("HH:mm:ss")
                }, 1000);
            },
            initMonthCalender() {
                //当前显示日历的年月日
                const calenderDateList = [];
                // 本月开始时间
                let m = moment(this.year + "-" + this.month + "-01", "YYYY-M-DD");
                let weekDays = m.date(1).weekday(); // 本月第一天是周几
                let monthDays = m.daysInMonth(); // 当月天数
                for (let i = 0; i < 42; i++) {
                    let date = moment(this.selectedWorkDay)
                        .date(1)
                        .weekday(i);
                    calenderDateList.push({
                        index: i,
                        date: date.format("YYYY-MM-DD"),
                        day: date.format("D"),
                        weekday: date.format("d"), // 一周的第几天
                        currentMonth: !(i < weekDays || i >= weekDays + monthDays),
                    });
                }
                // 获取当月的所有工作安排
                let monthWorkForm = {
                    startTimeQuery: calenderDateList[0].date + " 00:00",
                    endTimeQuery: calenderDateList[41].date + " 23:59",
                };
                // this.$http.getWorkPlanList(monthWorkForm).then((res) => {
                //     if (res.code == 0) {
                //         let {list} = res.data;
                //         calenderDateList.forEach((calender) => {
                //             let flag = list.some((item) =>
                //                 moment(calender.date).isBetween(item.startTime, item.endTime, "day", "[]")
                //             );
                //             calender.haveWork = flag;
                //         });
                //         this.calenderDateList = calenderDateList;
                //     }
                // });
            },
            handlePrev() {
                //上月
                if (this.month == 1) {
                    this.year = Number(this.year) - 1;
                    this.month = 12;
                } else {
                    this.month = Number(this.month) - 1;
                }
                this.defaultSelectDay();
                this.initMonthCalender();
            },
            handleNext() {
                //下月
                if (this.month == 12) {
                    this.year = Number(this.year) + 1;
                    this.month = 1;
                } else {
                    this.month = Number(this.month) + 1;
                }
                this.defaultSelectDay();
                this.initMonthCalender();
            },
            defaultSelectDay() {
                let year = moment().format("YYYY");
                let month = moment().format("M");
                if (year == this.year && month == this.month) {
                    this.setSelectedWorkDay({currentMonth: true, date: moment().format("YYYY-MM-DD")});
                    return;
                }

                this.setSelectedWorkDay({
                    currentMonth: true,
                    date: moment(this.year + "-" + this.month, "YYYY-M")
                        .date(1)
                        .format("YYYY-MM-DD"),
                });
            },
            setSelectedWorkDay(calendar) {
                this.selectedWorkDay = calendar.date;
                // 非当前月, 日历切换
                if (!calendar.currentMonth) {
                    let m = moment(this.selectedWorkDay);
                    this.year = m.format("YYYY");
                    this.month = m.format("M");
                    this.initMonthCalender();
                }
                this.getWorkList();
            },
            async getWorkList() {
                return;
                try {
                    const {code, data} = await this.$http.getWorkPlanList({currentTime: this.selectedWorkDay})
                    if (+code !== 0) return;
                    const {list} = data;
                    this.workList = list;
                    list.forEach((item) => {
                        item.time =
                            (item.startTime.substring(0, 10) == this.selectedWorkDay
                                ? item.startTime.substring(11, 16)
                                : "00:00") +
                            "-" +
                            (item.endTime.substring(0, 10) == this.selectedWorkDay
                                ? item.endTime.substring(11, 16)
                                : "23:59");
                    });

                } catch (e) {
                }
            },
            addWork() {
                this.workIsAddLoading = false;
                this.workDialogVisible = true;
                //添加工作计划安排
                this.formData = {id: null};
                this.$nextTick(() => {
                    const workFormRef = this.$refs.workForm;
                    workFormRef.clearFrom();
                    workFormRef.setFocus('title');
                })
                
            },
            async workHandleTrueClick() {
                try {
                    this.workIsAddLoading = true;
                    const {data, status} = await this.$refs.workForm.getFormAndValidate();
                    if (!status) {
                        this.workIsAddLoading = false;
                        return
                    };
                    delete data.workTime;
                    const {code, message} = await this.$http[data.id ? "getWorkPlanSave" : "getWorkPlanAdd"](data);
                    if (+code !== 0) return;
                    this.$showSuccess(message);
                    this.workDialogVisible = false;
                    this.initMonthCalender();
                    this.getWorkList();
                } catch (e) {
                    console.log('workHandleTrueClick:e', e)
                    this.workIsAddLoading = false;
                }
            },
            workEdit(row) {
                //编辑工作计划安排
                this.workIsAddLoading = false;
                this.workDialogVisible = true;
                row.workTime = [row.startTime, row.endTime];
                this.formData = row;
            },
            deleteWork(idQueryIn) {
                //删除工作计划安排
                this.$http.getWorkPlanDelete({idQueryIn}).then((res) => {
                    if (res.code == 0) {
                        this.$showSuccess("删除成功！");
                        this.initMonthCalender();
                        this.getWorkList();
                    }
                });
            },
        },
    };
</script>

<style lang="scss" scoped>
    @import "@/styles/work-plan.scss";
</style>
