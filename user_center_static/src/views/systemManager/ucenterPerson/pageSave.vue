<template>
    <div class="main-content-wrap inner-maincon">
        <el-tabs v-model="activeName" class="tabs-hastree">
            <el-tab-pane label="基本信息" name="first" class="first-tab">
                <form-com ref="ruleFormBasic" :config="formConfigBasic" :form-data="basicFormData"></form-com>
                <div class="upload-img">
                    <upload-image
                            ref="uploadImage"
                            v-model="imgPath"
                            :actionUrl="actionUrl"
                            ctionType=".jpg,.png,.JPG,.PNG"
                            type="10068-70"
                            @upLoadImgFullInfo="handleGetUploadImg"
                    ></upload-image>
                    <div class="tip-text">支持jpg、png（大小不超过5MB）</div>
                </div>
            </el-tab-pane>
            <el-tab-pane label="组织信息" name="second">
                <info-config
                        ref="ruleFormOrg"
                        :tableTit="tableTit1"
                        :baseFrom="baseFrom1"
                        :defaultFrom="defaultFrom"
                        url="getUcenterOrgTree"
                ></info-config>
            </el-tab-pane>
            <el-tab-pane label="角色信息" name="third">
                <info-config
                        ref="ruleFormRole"
                        :tableTit="tableTit2"
                        :baseFrom="baseFrom2"
                        :defaultOrgIds="defaultOrgIds"
                        url="getUcenterRoleCombox"
                ></info-config>
            </el-tab-pane>
            <el-tab-pane label="其他信息" name="fourth">
                <form-com ref="ruleFormOther" :config="formConfigOther" :form-data="otherFormData">
                    <template #registryAddress>
                        <address-com
                                :provinceList="provinceList"
                                :cityList="cityList"
                                :areaList="areaList"
                                :selectedAddress="selectedAddress"
                                :provinceCode="addressForm.homeProvince"
                                :cityCode="addressForm.homeCity"
                                :areaCode="addressForm.homeCounty"
                                @selectProvince="selectProvince"
                                @selectCity="selectCity"
                                @selectArea="selectArea"
                                @clearSelected="clearSelected"
                        ></address-com>
                    </template>
                </form-com>
            </el-tab-pane>
            <!-- <el-tab-pane label="监管范围" name="fifth">
                <div class="all-check-box">
                    <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange"
                        >全选
                    </el-checkbox>
                    <el-checkbox-group v-model="manageFunc" @change="handleCheckedChange">
                        <el-checkbox v-for="area in controlledArea" :label="area.code" :key="area.code">
                            {{ area.name }}
                        </el-checkbox>
                    </el-checkbox-group>
                </div>
            </el-tab-pane> -->
        </el-tabs>

        <div class="form-button">
            <el-button @click="goBack($route)">取消</el-button>
            <el-button type="primary" v-loading="btnLoading" @click="submitForm(0)">保存</el-button>
        </div>
    </div>
</template>

<script>
    import formCom from "@/components/form-com";
    import AddressCom from "@/components/address";
    import infoConfig from "@/components/info-config";
    import UploadImage from "@/components/upload-single-img";
    import {formConfigBasic, formConfigOther, tableTit1, tableTit2} from "./config/add";

    export default {
        components: {
            formCom,
            AddressCom,
            infoConfig,
            UploadImage,
        },
        data() {
            return {
                actionUrl: "/sys/file/upload",
                imgPath: null,
                imgObj: {},
                provinceList: [],
                cityList: {},
                areaList: {},
                selectedAddress: "",
                activeName: "first",
                btnLoading: false,
                formConfigBasic,
                formConfigOther,
                tableTit1,
                tableTit2,
                baseFrom1: {
                    deptId: null,
                    isMain: "0",
                    isMainPerson: false,
                },
                baseFrom2: {
                    deptId: null,
                },
                isFormBasicValid: false,
                isFormOtherValid: false,
                addressForm: {
                    homeProvince: '',
                    homeCity: '',
                    homeCounty: '',
                },
                basicFormData: {},
                otherFormData: {},
                defaultOrgIds: [],
                defaultFrom: [],

                // checkAll: false,
                // isIndeterminate: false,
                // manageFunc: [],
                // controlledArea: [],
            };
        },
        created() {
            this.getComboxList("10001-10004");
            this.getComboxList("10001-10033");
            this.getComboxList("10001-10034");
            this.getComboxList("10001-10035");
            const {id} = this.$route.params;
            if (id) {
                this.requestView(id);
            }
        },
        methods: {
            handleGetUploadImg(imgObj) {
                this.imgObj = imgObj;
            },
            setFormConfigBasic(index, data, attr = "children") {
                this.formConfigBasic[index][attr] = data;
            },
            // getPosList() {
            //     this.$http.getPositionCombox({pageSize: 99999}).then((res) => {
            //         this.setFormConfigBasic(10, res.data.list);
            //     });
            // },
            getComboxList(type) {
                this.$http.getUcenterCodeCombox({type}).then((res) => {
                    const {code, data} = res;
                    if (code == 0) {
                        switch (type) {
                            case "10001-10004": //性别
                                this.setFormConfigBasic(2, data);
                                break;
                            case "10001-10033":
                                this.provinceList = data;
                                break;
                            case "10001-10034":
                                this.formatCity(data || [], "city");
                                break;
                            case "10001-10035":
                                this.formatCity(data || [], "area");
                                break;

                            default:
                                return;
                        }
                    }
                });
            },

            //地址
            formatCity(list, type) {
                let cityMap = {};
                list.forEach((item) => {
                    if (cityMap[item.parent]) {
                        cityMap[item.parent].push(item);
                    } else {
                        cityMap[item.parent] = [item];
                    }
                });
                if (type == "city") {
                    this.cityList = cityMap;
                } else {
                    this.areaList = cityMap;
                }
            },
            selectProvince(val) {
                this.addressForm.homeProvince = val;
                this.selectCity("");
                this.addressForm.homeCounty = "";
            },
            selectCity(val) {
                this.addressForm.homeCity = val;
                this.addressForm.homeCounty = "";
            },
            selectArea({code, name}) {
                this.addressForm.homeCounty = code;
                this.selectedAddress = name;
            },
            clearSelected(val) {
                this.addressForm.homeProvince = "";
                this.addressForm.homeCity = "";
                this.addressForm.homeCounty = "";
                this.selectedAddress = "";
            },
            async submitForm(type) {
                try {
                    //基本信息
                    const basicForm = await this.$refs["ruleFormBasic"].getFormAndValidate();
                    if (!basicForm.status) {
                        this.activeName = "first";
                        return;
                    }
                    // 头像
                    let personImg = this.imgObj;

                    //校验组织信息
                    const {ucenterPersonOrgs} = this.$refs["ruleFormOrg"].getForm();
                    const {roleIds} = this.$refs["ruleFormRole"].getForm();
                    let ucenterPersonOrgsStatsu = false;
                    if (!ucenterPersonOrgs.length) {
                        this.activeName = "second";
                        this.$showWarning("请在选择组织信息");
                        return;
                    }
                    ucenterPersonOrgs.forEach((item) => {
                        item.isMain == "1" && (ucenterPersonOrgsStatsu = true);
                    });
                    if (!ucenterPersonOrgsStatsu) {
                        this.activeName = "second";
                        this.$showWarning("请在组织信息中选择是否主组织");
                        return;
                    }

                    //校验其他信息
                    const otherForm = await this.$refs["ruleFormOther"].getFormAndValidate();
                    if (!otherForm.status) {
                        this.activeName = "fourth";
                        return;
                    }
                    //合并数据
                    const info = {
                        ...otherForm.data,
                        ...this.addressForm,
                    };
                    const sendFormData = {
                        ...basicForm.data,
                        info : JSON.stringify(info),
                        personImg: personImg == null ? "" : JSON.stringify(personImg),
                        // manageFunc,
                        ucenterPersonOrgs: JSON.stringify(ucenterPersonOrgs),
                        roleIds,
                    };
                    this.btnLoading = true;
                    // 调接口
                    const {code, message} = await this.$http[
                        basicForm.data.id ? "getUcenterPersonEdit" : "getUcenterPersonAdd"
                        ](sendFormData);

                    if (+code !== 0) return;
                    this.$showSuccess(message);
                    this.goBack(this.$route, true);
                    this.btnLoading = false;
                } catch (err) {
                    this.btnLoading = false;
                    console.log('submitForm:e',err);
                }
            },

            //回显
            requestView(id) {
                this.$http
                    .getUcenterPersonView({id})
                    .then((res) => {
                        this.closeLoading(this.$route);
                        if (res.code == 0) {
                            let {data} = res;
                            try {
                                this.defaultFrom = data.ucenterPersonOrgs;
                            } catch (error) {
                                this.defaultFrom = [];
                            }
                            if (data.roleIds) {
                                this.defaultOrgIds = data.roleIds.split(",").map((item) => (item = Number(item)));
                            }
                            this.imgObj = data.personImg;
                            this.imgPath = data.personImg ? data.personImg.filePath : null;
                            this.basicFormData = data;
                            this.otherFormData = data.info;

                            let {homeProvince, homeProvinceName, homeCity, homeCityName, homeCounty, homeCountyName} = data.info;
                            this.addressForm = {
                                homeProvince,
                                homeCity,
                                homeCounty
                            };
                            this.selectedAddress = `${homeProvinceName||'请选择'} / ${homeCityName||'请选择'} / ${homeCountyName||'请选择'}`;
                        }
                    }).catch(() => this.closeLoading(this.$route));
            },
        },
    };
</script>
