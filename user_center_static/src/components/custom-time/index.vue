<!--
 * @Author: your name
 * @Date: 2021-06-15 14:27:13
 * @LastEditTime: 2021-08-16 16:30:35
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \gssp_fe\src\components\custom-time\index.vue
-->
<template>
    <div class="custom-time date-screen">
        <el-select v-if="!isSelect" v-model="definingTimeFmt" filterable clearable placeholder="" @change="timeTypeChange">
            <el-option :label="item.name" :value="item.value" v-for="(item, i) in options" :key="i"></el-option>
            
        </el-select>
        <span
            v-else
            v-for="(items, index) in options"
            :key="index"
            @click="timeTypeChange(items.value)"
            :class="{active: clickType == items.value}"
        >
            {{ items.name }}
        </span>
        <el-form-item label="时间段" v-if="definingTimeFmt=='other' || isSelect">
            <el-date-picker 
                @change="customTimeChange"
                v-model="customTimeFmt" 
                unlink-panels 
                value-format="yyyy-MM-dd" 
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期" 
                end-placeholder="结束日期"
                :picker-options="pickerOptions"
            ></el-date-picker>
        </el-form-item>
    </div>
</template>

<script>

export default {
    name: 'customTimeCom',
    props:{
        definingTime:{
            type: String,
            default: () => ""
        },
        customTime:{
            type: Array,
            default: () => []
        },
        isSelect: {
            type: Boolean,
            default: false
        },
        options: {
            type: Array,
            default: () => [
                {
                    name: '今天',
                    value: 'today'
                },
                {
                    name: '本周',
                    value: 'week'
                },
                {
                    name: '本月',
                    value: 'mounth'
                },
                {
                    name: '上个月',
                    value: 'prevMonth'
                },
                {
                    name: '本季',
                    value: 'season'
                },
                {
                    name: '本年',
                    value: 'year'
                },
                {
                    name: '上一年',
                    value: 'prevYear'
                },
                {
                    name: '自定义',
                    value: 'other'
                },
            ]
        }
    },
    data(){
        return{
            definingTimeFmt:this.definingTime,
            customTimeFmt:this.customTime,
            pickerOptions: this.$dateConfig(),
            startTime:"",
            endTime:"",
            clickType: ''
        }
    },
    watch: {
        customTimeFmt(val) {
            if (val && val.length > 0) {
                this.startTime = val[0];
                this.endTime = val[1];
                this.draftType = null;
                this.$emit('update:strat', this.startTime)
                this.$emit('update:end', this.endTime)
            }
        },
    },
    methods:{
        timeTypeChange(type) {
            this.clickType = type;
            this.customTimeFmt = [];
            if (type && type == "today") {
                this.startTime = this.$computedDate(type);
                this.endTime = this.$computedDate(type);
            } else if (type && type != "today" && type != 'other') {
                this.startTime = this.$computedDate(type).split("/")[0];
                this.endTime = this.$computedDate(type).split("/")[1];
            } else {
                this.startTime = "";
                this.endTime = "";
            }
           
            this.$emit('update:strat', this.startTime)
            this.$emit('update:end', this.endTime)
            this.$emit('customTimeChange', this.endTime, type)
        },
        customTimeChange(value) {
            this.customTimeFmt = value;
            if (value && value.length) {
                this.startTime = value[0];
                this.endTime = value[1];
                this.clickType = null
            } else {
                this.startTime = "";
                this.endTime = "";
                this.clickType = "";
            }
            this.$emit('update:strat', this.startTime)
            this.$emit('update:end', this.endTime)
        },
        handleDraftDate(type) {
                this.draftDate = [];
                this.draftType = type;
                if (type && type == "today") {
                    this.startTime = this.$computedDate(type);
                    this.endTime = this.$computedDate(type);
                } else if (type && type != "today") {
                    this.startTime = this.$computedDate(type).split("/")[0];
                    this.endTime = this.$computedDate(type).split("/")[1];
                } else {
                    this.startTime = "";
                    this.endTime = "";
                }
            },
        clear() {
            this.definingTimeFmt = "";
            this.customTimeFmt = ''
            this.startTime = "";
            this.endTime = "";
            this.clickType = "";
            this.$emit('update:strat', this.startTime)
            this.$emit('update:end', this.endTime)
        }
    }
}
</script>