<!--
 * @Author: your name
 * @Date: 2021-05-21 14:41:17
 * @LastEditTime: 2021-06-04 19:37:42
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \fe_framework_template\src\views\systemManager\ucenterPerson\pageView.vue
-->
<template>
    
    <div class="main-content-wrap inner-maincon">
        <view-com :viewConfigs="viewConfigs">
        </view-com>
    </div>
</template>

<script>
import viewCom from '@/components/view-com'

export default({
    name: "codeView",
    components: {
        viewCom
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
            let res = await this.$http.getUcenterCodeView({id});
            if (res.code == 0) {
                this.viewCon = res.data;
                this.initViewConfig();
            }
        },
        initViewConfig(){
            this.viewConfigs = [
                {
                    label:"名称",
                    content: this.viewCon.name,
                },
                {
                    label:"类别",
                    content:this.viewCon.typeValue,
                },
                {
                    label:"代码",
                    content: this.viewCon.code,
                },
                {
                    label:"代码值",
                    content:this.viewCon.value,
                },
                 {
                    label: "简称",
                    content: this.viewCon.sname,
                },
                {
                    label: "英文名称",
                    content: this.viewCon.ename,
                },
                {
                    label: "顺序号",
                    content: this.viewCon.orderNo,
                },
                {
                    label: "系统/用户",
                    content: this.viewCon.isSys == 1 ? '系统' : '用户',
                },
                {
                    label:"创建人",
                    content:this.viewCon.createByValue,
                },
                {
                    label:"创建时间",
                    content:this.viewCon.createTime,
                },
                {
                    label:"修改人",
                    content:this.viewCon.updateByValue,
                },
                {
                    label:"修改时间",
                    content:this.viewCon.updateTime,
                }
            ];
        }
    }
})
</script>