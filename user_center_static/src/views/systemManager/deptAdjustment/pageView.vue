<!--
 * @Author: your name
 * @Date: 2021-05-21 14:41:17
 * @LastEditTime: 2021-07-29 17:12:50
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \fe_framework_template\src\views\systemManager\ucenterPerson\pageView.vue
-->
<template>
    <div class="main-content-wrap inner-maincon">
        <view-com :columnNum="`row-col2`" :viewConfigs="viewConfigs">
        </view-com>
    </div>
</template>

<script>
import viewCom from '@/components/view-com'

export default({
    name: "deptAdjustmentView",
    components: {
        viewCom,
    },
    data() {
        return {
            viewCon: {},
            viewConfigs: [],
        }
    },
    created() {
        
        this.getData()
    },
    methods: {
        async getData() {
            let id = this.$route.params.id;
            let res = await this.$http.getUcenterDeptpersonView('10027-10', {id});
            if (res.code == 0) {
                this.viewCon = res.data;
                this.initViewConfig();
            }
        },
        initViewConfig(){
            this.viewConfigs = [
                {
                    label:"名称",
                    content: this.viewCon.cname,
                    class:"single"
                },
                {
                    label:"简称",
                    content: this.viewCon.sname,
                },
                {
                    label:"代码",
                    content: this.viewCon.code,
                },
                {
                    label:"英文名称",
                    content: this.viewCon.ename,
                },
                {
                    label:"联系人",
                    content: this.viewCon.linker,
                },
                {
                    label:"电话",
                    content:this.viewCon.telephone,
                },
                {
                    label:"传真",
                    content:this.viewCon.fax,
                },
                {
                    label:"顺序号",
                    content:this.viewCon.orderNo,
                },
                {
                    label:"上级部门",
                    content:this.viewCon.parentValue,
                },
                {
                    label:"机关(单位)",
                    content:this.viewCon.orgName,
                },
                {
                    label:"备注",
                    content:this.viewCon.memo,
                    class:"single item-remark"
                },
                {
                    label:"创建人",
                    content:this.viewCon.createByName,
                },
                {
                    label:"创建时间",
                    content:this.viewCon.createTime,
                },
                {
                    label:"修改人",
                    content:this.viewCon.updateByName,
                },
                {
                    label:"修改时间",
                    content:this.viewCon.updateTime,
                }
            ];
        },
    }
})
</script>