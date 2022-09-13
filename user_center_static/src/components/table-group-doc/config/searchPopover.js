/*
 * @Author: your name
 * @Date: 2021-08-16 10:27:33
 * @LastEditTime: 2021-08-25 18:17:53
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \nwxt_oa\src\components\popover-search-form\config\searchPopover.js
 */
export default{
    props: {
        searchList: {
            /**
             * 有高级搜索为Object 无高级搜索Array
             * @Object { list:[] 高级搜索list , easy:{ label value} }
             */
            type: Object | Array,
            deep: true,
        },
    },
    data () {
        return {
            formDataState: false,
            searchConfig: [],
            searchFormData: {},
        }
    },
    created () {
        this.searchMassge();
    },
    watch: {
        searchList() {
            this.searchMassge();
        },
    },
    methods: {
        popoverShowBtn(){
            this.searchFormData[this.searchList.easy.prop] = "";
        },
        searchMassge() {
            const data = this.searchList;
            let list = Array.isArray(data) ? data : data.list;
            if (!list) {
                return (this.searchConfig = []);
            }
            const newList = list;
            newList.push({
                type: "slot",
                label: "",
                slotName: "button",
            });
            this.searchConfig = newList;
        },
        popoverStatus() {
            const formData = this.$refs.searchForm.getForm();
            this.formDataState = Object.values(formData).some((i) => (Array.isArray(i) ? i.length : i));
        },
        onEmpty() {
            this.$refs.searchForm.clearFrom();
            this.searchFormData = {};
            this.reloadTableList();
        },

        onCearch() {
            const formData = this.$refs.searchForm.getForm();
            this.searchFormData = Object.assign(this.searchFormData, formData);
            this.reloadTableList();
        },
        closePopover() {
            this.$refs.searchPopover.visible = false;
        }
    }
}
