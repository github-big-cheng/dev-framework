<!--
 * @Author: your name
 * @Date: 2021-05-21 14:41:17
 * @LastEditTime: 2021-06-15 14:22:45
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \fe_framework_template\src\views\systemManager\ucenterPerson\pageView.vue
-->
<template>

    <div class="main-content-wrap inner-maincon">
        <page-title title="基本信息" :isFirst="true" :isTitleBg="true"></page-title>
        <view-com columnNum="row-col2" :viewConfigs="viewConfigsFirst" :isImgShow="true" :imgPath="imgPath">
        </view-com>

        <page-title title="岗位信息" :isFirst="true" :isTitleBg="true"></page-title>
        <view-com :viewConfigs="viewConfigsSecond">
        </view-com>

        <page-title title="其他信息" :isFirst="true" :isTitleBg="true"></page-title>
        <view-com :columnNum="`row-col3`" :viewConfigs="viewConfigsThird">
        </view-com>
    </div>
</template>

<script>
    import pageTitle from '@/components/page-title'
    import viewCom from '@/components/view-com'

    export default ({
        name: "deptView",
        components: {
            pageTitle,
            viewCom,
        },
        data() {
            return {
                imgPath: "",
                viewCon: {},
                viewConfigsFirst: [],
                viewConfigsSecond: [],
                viewConfigsThird: [],
            }
        },
        created() {
            this.getData()
        },
        methods: {
            async getData() {
                let id = this.$route.params.id
                let res = await this.$http.getUcenterPersonView({id});
                const {code, data} = res;
                if (code == 0) {
                    this.viewCon = data;
                    this.imgPath = data?.personImg?.filePath;
                    this.initViewConfig();
                }
            },
            initViewConfig() {
                this.viewConfigsFirst = [
                    {
                        label: "姓名",
                        content: this.viewCon.name,
                    },
                    {
                        label: "账号",
                        content: this.viewCon.account,
                    },
                    {
                        label: "性别",
                        content: this.viewCon.sexName,
                    },
                    {
                        label: "工号",
                        content: this.viewCon.billNo,
                    },
                    {
                        label: "出生日期",
                        content: this.viewCon.birthday,
                    },
                    {
                        label: "婚姻状况",
                        content: this.viewCon.marriageName,
                    },
                    {
                        label: "民族",
                        content: this.viewCon.nationName,
                    },
                    {
                        label: "手机",
                        content: this.viewCon.mobile,
                    },
                    {
                        label: "电话",
                        content: this.viewCon.telephone,
                    },
                    {
                        label: "邮箱",
                        content: this.viewCon.email,
                    },
                    {
                        label: "职位",
                        content: this.viewCon.posName,
                    },
                    {
                        label: "职级",
                        content: this.viewCon.posLevelName,
                    },
                    {
                        label: "政治面貌",
                        content: this.viewCon.politicalName,
                    },
                    {
                        label: "备注",
                        content: this.viewCon.memo,
                        class: "single item-remark"
                    }
                ];
                this.viewConfigsSecond = [
                    {
                        label: "所属单位",
                        content: this.viewCon.companyName,
                        class: "single"
                    },
                ];
                this.viewConfigsThird = [
                    {
                        label: "毕业院校",
                        content: this.viewCon.graduateSchool,
                        class: "single"
                    },
                    {
                        label: "学历",
                        content: this.viewCon.educationName,
                    },
                    {
                        label: "学位",
                        content: this.viewCon.degreeName,
                    },
                    {
                        label: "毕业时间",
                        content: this.viewCon.graduateTime,
                    },
                    {
                        label: "专业",
                        content: this.viewCon.major,
                        class: 'single'
                    },
                    {
                        label: "入党日期",
                        content: this.viewCon.joinPartyDate,
                    },
                    {
                        label: "参加工作日期",
                        content: this.viewCon.beginWorkTime,
                    },
                    {
                        label: "入职日期",
                        content: this.viewCon.workTime,
                    },
                    {
                        label: "IMSI号码",
                        content: this.viewCon.imsiNo,
                    },
                    {
                        label: "IMEI号码",
                        content: this.viewCon.imeiNo,
                    },
                    {
                        label: "邮编",
                        content: this.viewCon.zipcode,
                    },
                    {
                        label: "户籍地址",
                        content: this.viewCon.homeProvinceName + this.viewCon.homeCityName + this.viewCon.homeCountyName,
                    },
                    {
                        label: "家庭住址",
                        content: this.viewCon.address,
                    },
                    {
                        label: "创建人",
                        content: this.viewCon.createByName,
                    },
                    {
                        label: "创建时间",
                        content: this.viewCon.createTime,
                    },
                    {
                        label: "修改人",
                        content: this.viewCon.updateByName,
                    },
                    {
                        label: "修改时间",
                        content: this.viewCon.updateTime,
                    }
                ];
            },
        }
    })
</script>

<style lang="scss" scoped>
    @import "@/styles/view.scss";
</style>
