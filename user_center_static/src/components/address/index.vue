<template>
    <div class="dv-address">
        <el-dropdown trigger="click" placement="bottom-start">
            <div class="dv-input">
                {{ addressText ? addressText : selectedAddress }}
                <i class="el-input__icon el-icon-arrow-up"></i>
            </div>
            <el-dropdown-menu
                    class="address-container"
                    slot="dropdown"
            >
                <div class="address-box">
                    <el-dropdown-item class="dv-clear"
                    ><span @click="handleClearClick">清空</span></el-dropdown-item
                    >
                    <el-tabs
                            v-model="activeName"
                            type="border-card"
                    >
                        <el-tab-pane label="省份" name="province">
                            <el-row :gutter="24">
                                <el-col
                                        :span="6"
                                        v-for="item of provinceList"
                                        :key="item.value"
                                        :class="provinceCodeFmt == item.value ? 'el-col-active' : ''"
                                >
                                    <div class="sheng" @click="provinceClick(item.value,item.name)">
                                        {{ item.name }}
                                    </div>
                                </el-col
                                >
                            </el-row>
                        </el-tab-pane>
                        <el-tab-pane label="城市" name="city">
                            <el-row :gutter="24">
                                <el-col
                                        :span="6"
                                        v-for="item of cityList[provinceCodeFmt]"
                                        :key="item.value"
                                        :class="cityCodeFmt == item.value ? 'el-col-active' : ''"
                                >
                                    <div class="sheng" @click="cityClick(item.value,item.name)">
                                        {{ item.name }}
                                    </div>
                                </el-col
                                >
                            </el-row>
                        </el-tab-pane>
                        <el-tab-pane label="区县" name="area">
                            <el-row :gutter="24">
                                <el-col
                                        :span="6"
                                        v-for="item of areaList[cityCodeFmt]"
                                        :class="areaCodeFmt == item.value ? 'el-col-active' : ''"
                                        :key="item.value"
                                >
                                    <div class="sheng" @click="areaClick(item.value,item.name)">
                                        <el-dropdown-item>{{ item.name }}</el-dropdown-item>
                                    </div>
                                </el-col
                                >
                            </el-row>
                        </el-tab-pane>
                    </el-tabs>
                </div>
            </el-dropdown-menu>
        </el-dropdown>
    </div>
</template>

<script>
    export default {
        name: 'addressCom',
        props: {
            provinceList: {
                type: Array,
                default: () => [],
            },
            cityList: {
                type: Object,
                default: () => {
                },
            },
            areaList: {
                type: Object,
                default: () => {
                },
            },
            selectedAddress: {
                type: String,
                default: () => ""
            },
            provinceCode: {
                type: String,
                default: () => ""
            },
            cityCode: {
                type: String,
                default: () => ""
            },
            areaCode: {
                type: String,
                default: () => ""
            }
        },
        data() {
            return {
                activeName: "province",
                provinceName: "",
                cityName: "",
                areaName: "",
                addressText: this.selectedAddress,
                provinceCodeFmt: this.provinceCode,
                cityCodeFmt: this.cityCode,
                areaCodeFmt: this.areaCode,
            };
        },
        watch: {
            provinceCode(val) {
                this.provinceCodeFmt = val;
            },
            cityCode(val) {
                this.cityCodeFmt = val;
            },
            areaCode(val) {
                this.areaCodeFmt = val;
            },
        },
        methods: {
            provinceClick(code, name) {
                this.provinceCodeFmt = code;
                this.provinceName = name;
                this.activeName = "city";
                this.addressText = name;
                this.$emit("selectProvince", code);
            },
            cityClick(code, name) {
                this.cityCodeFmt = code;
                this.cityName = name;
                this.activeName = "area";
                this.addressText = `${this.provinceName}/${this.cityName}`;
                this.$emit("selectCity", code);
            },
            areaClick(code, name) {
                this.areaCodeFmt = code;
                this.areaName = name;
                this.addressText = `${this.provinceName}/${this.cityName}/${this.areaName}`;
                this.$emit("selectArea", {code, name: `${this.provinceName}/${this.cityName}/${this.areaName}`});
            },
            handleClearClick() {
                this.provinceCodeFmt = "";
                this.cityCodeFmt = "";
                this.areaCodeFmt = "";
                this.activeName = "province";
                this.addressText = "";
                this.$emit("clearSelected");
            },
        }
    };
</script>

<style lang="scss" scoped>
    @import "@/styles/address.scss";
</style>
