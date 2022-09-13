<template>
    <div>
        <Dialog
            iconfont="el-icon-alipeople-tit"
            :dialogVisible="showComponent"
            :title="title"
            class="select-personnel"
            @cancelClick="handleCancelClick"
            @trueClick="handleTrueClick"
        >
            <el-tabs v-model="activeTab" class="tabs-box">
                <el-tab-pane
                    v-for="(item, key) of tabConfig"
                    :key="key"
                    :label="item.tabName"
                    :name="key+''"
                >
                    <single
                        :ref="key"
                        :isActived="activeTab == key"
                        :dialogVisible="showComponent"
                        :apiurl="item.apiUrl"
                        :params="params"
                        :select="{'id': defaultIds}"
                        :type="key+''"
                        :renderType="item.type"
                        :searchValue.sync="searchValue"
                        :idsScoped="idsScoped"
                        :firstIcon="
                            item.firstIcon && item.firstIcon.trim()
                                ? item.firstIcon
                                : 'tree-filebox'
                        "
                        :secondIcon="
                            item.secondIcon && item.secondIcon.trim()
                                ? item.secondIcon
                                : 'tree-file'
                        "
                        :thirdIcon="
                            item.thirdIcon && item.thirdIcon.trim()
                                ? item.thirdIcon
                                : 'el-icon-alihead'
                        "
                        @selected="handleGetSelected"
                        @dbSelected="handleDbSelected"
                    ></single>
                </el-tab-pane>
            </el-tabs>
        </Dialog>
    </div>
</template>

<script>
/**
 * 总控制表
 */
const CONTROL = new Map();

/**
 * 创建tab配置参数工厂函数
 * @param tabName tab名称
 * @param apiUrl 接口地址
 * @param params 类型参数
 * @default workingArea 为提交缓存区
 * @default treelist 列表数据
 */
const createConfig = (tabName, apiUrl, params) => {
    return {
        workingArea: new Map(),
        treelist: new Map(),
        tabName,
        apiUrl,
        params,
        isRenderTree: false,
        showSearchArea: false,
        hasSerach: false,
    };
};

/**
 * 总控制表映射关系
 */
// CONTROL.set(
//     "partyPerson",
//     createConfig("党支部人员", "getUcenterOrgTreePerson", { type: "12001-20" })
// );
CONTROL.set(
    "deptPerson",
    createConfig("部门人员", "getUcenterOrgTreePerson")
);
// CONTROL.set(
//     "party",
//     createConfig("党支部", "getListDeptTree", { type: "12001-20" })
// );
CONTROL.set(
    "dept",
    createConfig("部门", "getUcenterOrgTree")
);

CONTROL.set(
    "menu",
    createConfig("菜单管理", "getUcenterobjfunc")
);

CONTROL.set(
    "area",
    createConfig("地区", "getAreaTree")
);

CONTROL.set('party', createConfig('党组织', 'partyOrgTree'));
// CONTROL.set(
//     "docUnit",
//     createConfig("来文单位", "getListDeptTree", {
//         type: "12001-30",
//         subType: "12009-10",
//     })
// );
CONTROL.set(
    "insureUnit",
    createConfig("保险公司", "getUcenterOrgTree")
);
import Dialog from '@/components/dialog/index';
import single from './component/single/index';
export default {
    props: {
        showComponent: {
            type: Boolean,
            default: () => false
        },
        title: {
            type: String,
            default: () => '人员选择'
        },
        tabList: {
            type: Array,
            default: () => []
        },
        defaultIds: {
            type: String,
            default: ''
        },
        idsScoped: {
            type: Array,
            default: () => [],
        },
        params: {
            type: Object,
            default: () => {}
        }
    },
    components: {
        Dialog,
        single
    },
    data() {
        return {
            activeTab: '',
            selectedPerson: {},
            searchValue: '',
            tabConfig: [],
            CONTROL: CONTROL,
            noWait: false,
            diglogIconfont: "",
        };
    },
    created () {
        this.init()
        
    },
    methods: {
        init() {
            this.tabList.forEach((item) => {
                let config = this.CONTROL.get(item);
                if (!config) {
                    return;
                }
                config.type=item;
                this.tabConfig.push(config);
            });

            this.setDialogIcon()
        },
        setDialogIcon() {
            let config = this.tabList[0];
            this.diglogIconfont = config == 'partyPerson' || config == 'deptPerson' ? 
                'el-icon-aliuser' :
                'el-icon-alistructure';
        },
        handleDbSelected(data) {
            this.selectedPerson[data.key] = data.value;
            this.handleTrueClick();
        },
        handleGetSelected(data) {
            this.selectedPerson[data.key] = data.value;
        },
        handleCancelClick() {
            this.$emit('cancelClick');
        },
        handleTrueClick() {
            if (JSON.stringify(this.selectedPerson) == '{}') {
                this.$showWarning(`请选择${this.title}`);
                return;
            }
            let type = this.tabConfig[this.activeTab].type;
            let render = type == 'partyPerson' || type == 'deptPerson';
            if (render && (JSON.stringify(this.selectedPerson) == '{}' ||
                this.selectedPerson[this.activeTab].hasOwnProperty('children'))) {

                this.$showWarning(`请选择${this.title}`);
                return;
            } 

            this.$emit('trueClick', this.selectedPerson[this.activeTab]);
        }
    }
};
</script>

<style lang="scss" scoped>
 @import "@/styles/select-com.scss";
</style>
