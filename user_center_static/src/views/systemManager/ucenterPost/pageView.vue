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
    name: "postView",
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
            let res = await this.$http.getPositionView({id});
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
                    label:"代码",
                    content: this.viewCon.code,
                },
                {
                    label:"职务类型",
                    content:this.viewCon.typeName,
                },
                {
                    label:"机关(单位)",
                    content:this.viewCon.orgName,
                },
                {
                    label:"备注",
                    content:this.viewCon.memo,
                    class:"item-remark"
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
        }
    }
})
</script>