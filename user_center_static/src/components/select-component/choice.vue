<!--
 * @Author: your name
 * @Date: 2021-03-31 15:49:36
 * @LastEditTime: 2021-08-26 14:43:41
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \zfzd_oa\src\components\selectionDialog\choice\index.vue
-->
<template>
  <div>
    <Dialog
      iconfont="el-icon-alipeople-tit"
      :dialogVisible="showComponent"
      :title="title"
      :trueText="DBtnText"
      @cancelClick="handleCancelClick"
      @trueClick="handleTrueClick"
      class="select-choice"
    >
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <el-tab-pane v-for="(item, key) in tabConfig" :key="key" :label="item.tabName" :name="key.toString()">
          <el-row :gutter="24" class="personal-choice">
            <el-col :span="11" class="col-zz" style="padding-left: 12px; padding-right: 12px">
              <choice
                :dialogVisible="showComponent"
                :ref="key"
                :controlData="controlData"
                :workingArea="item.workingArea"
                :apiurl="item.apiUrl"
                :params="params"
                :isRenderTree.sync="item.isRenderTree"
                :type="key"
                :searchArea="item.searchArea"
                :hasSerach.sync="item.hasSerach"
                :showSearchArea.sync="item.showSearchArea"
                :isActived="activeTab == key"
                :searchValue.sync="searchValue"
                :renderType="item.type"
                :idsScoped="idsScoped"
                @chekcIds="chekcIds"
                @setWorking="setWorking"
                @selected="handleGetSelected"
              >
              </choice>
            </el-col>
            <el-col :span="2" style="padding-left: 12px; padding-right: 12px">
              <div class="dv-btn">
                <span
                  class="s-right"
                  @click="selectedZzClick(item.workingArea, key)"
                  :style="{
                    background: item.workingArea.size || item.hasSerach ? '#118AF7' : '#ccc',
                  }"
                  ><i class="el-icon-aliarrright"></i
                ></span>
                <span
                  class="s-right s-left"
                  @click="removeZzSelected(key)"
                  :style="{
                    background: publicSelect.length ? '#118AF7' : '#ccc',
                  }"
                  ><i class="el-icon-aliarrleft"></i
                ></span>
                <span
                  class="s-right r-export"
                  @click="allZzSelected"
                  :style="{
                    background: item.showSearchArea ? '#ccc' : '#118AF7',
                  }"
                  ><i class="el-icon-aliexport"></i
                ></span>
                <span
                  class="s-right l-export"
                  @click="clearAllPublic"
                  :style="{
                    background:
                      (!item.showSearchArea && controlData.size > 0) || (!item.showSearchArea && controlData.size > 0)
                        ? '#118AF7'
                        : '#ccc',
                  }"
                  ><i class="el-icon-aliexport"></i
                ></span>
              </div>
            </el-col>
            <el-col :span="11" class="col-zz" style="padding-left: 12px; padding-right: 12px">
              <publicCom
                :ref="key + 'public'"
                :renderType="item.type"
                :controlData="controlData"
                @setPublicSelect="setPublicSelect"
                @setControlData="setControlData"
                @selected="handleGetSelected"
              ></publicCom>
            </el-col>
          </el-row>
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
CONTROL.set('deptPerson', createConfig('部门人员', 'getUcenterOrgTreePerson'));
CONTROL.set('dept', createConfig('部门', 'getUcenterOrgTree'));

import Dialog from '@/components/dialog/index';
import choice from './component/choice';
import publicCom from './component/choice/public';
import lodash from 'lodash';

export default {
  props: {
    tabList: {
      type: Array,
      default: () => [],
    },
    showComponent: {
      type: Boolean,
      default: false,
    },
    title: {
      type: String,
      default: '',
    },
    defaultIds: {
      type: String,
      default: '',
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
    choice,
    publicCom,
  },
  data() {
    return {
      controlData: new Map(),
      DBtnText: '确 定',
      activeTab: '',
      allData: new Map(),
      commonIds: new Set(),
      publicSelect: [],
      tabConfig: [],
      CONTROL: CONTROL,
      searchValue: '',
      renderPerson: false,
    };
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      this.tabList.forEach((item) => {
        let config = this.CONTROL.get(item);
        if (!config) {
          return;
        }
        config.type = item;
        this.tabConfig.push(lodash.cloneDeep(config));
      });
      /**
       * 判断是否选人控件
       */
      this.renderPerson = this.tabConfig[0].type == 'partyPerson' || this.tabConfig[0].type == 'deptPerson';

      this.setDialogIcon();
    },
    setDialogIcon() {
      let config = this.tabList[0];
      this.diglogIconfont = config == 'partyPerson' || config == 'deptPerson' ? 'el-icon-aliuser' : 'tree-org';
    },
    /**
     * 弹窗事件 确定-通知父组件
     */
    handleTrueClick() {
      if (!this.controlData.size) {
        this.$showWarning(`请选择${this.title}`);
        return;
      }

      let data = [];
      for (let value of this.controlData.values()) {
        data.push(value);
      }

      this.$emit('trueClick', data);
    },

    /**
     * 弹窗事件 取消
     */
    handleCancelClick() {
      this.$emit('cancelClick');
    },

    /**
     * @params data 勾选的对象
     * @params status 执行状态
     * 勾选回调
     */
    handleGetSelected(map, status) {
      let untility = new Map();

      untility.set('add', 'addControlData');
      untility.set('del', 'delControlData');

      untility.set('clear', 'clearControlData');

      let value = untility.get(status);
      this[value](map);
    },

    setWorking(map, allMap) {
      this.tabConfig[this.activeTab].workingArea = new Map(map);
      if (!this.commonIds.size) return;

      let addMap = [],
        delIds = [];
      for (let key of this.commonIds.keys()) {
        if (!allMap.has(key)) continue;

        map.has(key) && addMap.push(map.get(key));
        !map.has(key) && delIds.push(key);
      }
      this.setPublicIds(addMap, true);
      this.setPublicIds(delIds, false);
    },
    /**
     * 收集所有tab页的id
     */
    chekcIds(data) {
      let commonIds = new Set();
      /**
       * @param defaultIds 渲染默认值
       * @param controlData 渲染默认值
       */
      let defaultIds = this.defaultIds.split(',');

      let controlData = lodash.cloneDeep(this.controlData);
      data.obj.forEach((item) => {
        this.allData.has(item.id) ? commonIds.add(item.id) : this.allData.set(item.id, item);
        this.tabConfig[data.type].treelist.set(item.id, item);
      });
      defaultIds.forEach((item) => {
        let value = this.allData.get(Number(item));
        value && controlData.set(value.id, lodash.cloneDeep(value)) && this.tabConfig[data.type].workingArea.set(value.id, value);
      });

      commonIds.size && (this.commonIds = new Set(commonIds));
      controlData.size && (this.controlData = new Map(controlData));

      this.tabConfig[this.activeTab].isRenderTree = true;
    },

    handleTabClick() {
      this.tabConfig[this.activeTab].isRenderTree = true;
    },

    setControlData(map) {
      this.controlData = new Map(map);
    },
    setPublicSelect(data, status) {
      if (status) {
        this.publicSelect.push(data);
      } else {
        this.publicSelect.forEach((item, i) => {
          data.id == item.id && this.publicSelect.splice(i, 1);
        });
      }
      let controlData = this.controlData;
      controlData.get(data.id).isZzSelected = status;
      this.controlData = new Map(controlData);
    },

    allZzSelected() {
      let allData = lodash.cloneDeep(this.allData);
      for (let value of allData.values()) {
        value.isZzSelected = false;
      }
      this.controlData = new Map(allData);

      this.tabConfig.forEach((item, i) => {
        let treelist = lodash.cloneDeep(item.treelist);
        this.tabConfig[i].workingArea = new Map(treelist);
      });
      this.tabConfig[this.activeTab].isRenderTree = true;
    },
    clearAllPublic() {
      this.controlData = new Map();
      this.tabConfig.forEach((item, i) => {
        this.tabConfig[i].workingArea = new Map();
      });
      this.tabConfig[this.activeTab].isRenderTree = true;
    },
    /**
     * 添加
     */
    selectedZzClick(workingArea, key) {
      this.$refs[key+'public'][0].showselectedZz = false;
      this.$refs[key+'public'][0].selectedZzValue = '';
      if (this.$refs[key][0].showSearchZz) {
        let map = new Map();
        this.$refs[key][0].searchOrganizationList.forEach((item) => {
          item.isZzSelected && map.set(item.id, lodash.cloneDeep(item));
        });

        this.handleGetSelected(map, 'add');
        return;
      }
      if (!workingArea.size) return;

      this.handleGetSelected(workingArea, 'add');
    },
    /**
     * 删除
     */
    removeZzSelected(key) {
      this.$refs[key][0].showSearchZz = false;
      this.$refs[key][0].organizationValue = '';
      let selected = [];
      for (let value of this.controlData.values()) {
        value.isZzSelected && selected.push(value);
      }
      this.handleGetSelected(selected, 'del');
    },

    /**
     * @param map 操作的map对象
     * 工具函数
     */
    addControlData(map) {
      let controlData = lodash.cloneDeep(this.controlData),
        item = this.tabConfig[this.activeTab],
        treeList = this.tabConfig[this.activeTab].treelist;

      for (let key of treeList.keys()) {
        controlData.get(key) && controlData.delete(key);
      }
      /**
       * 搜索弹窗优先
       */

      if (item.hasSerach) {
        let workingArea = new Map(this.tabConfig[this.activeTab].workingArea);
        this.$refs[this.activeTab][0].searchOrganizationList.forEach((item) => {
          if (item.isZzSelected) {
            let data = lodash.cloneDeep(item);
            data.isZzSelected = false;
            workingArea.set(item.id, data);
          }
        });
        for (let [key, value] of lodash.cloneDeep(workingArea)) {
          value.isZzSelected = false;
          controlData.set(key, value);
        }
        this.tabConfig[this.activeTab].workingArea = new Map(workingArea);
      } else {
        if (!item.workingArea.size) return;

        for (let [key, value] of lodash.cloneDeep(item.workingArea)) {
          value.isZzSelected = false;
          controlData.set(key, value);
        }
      }
      this.controlData = new Map(controlData);
      this.tabConfig[this.activeTab].isRenderTree = true;
    },

    delControlData(data) {
      let controlData = lodash.cloneDeep(this.controlData);
      if (Array.isArray(data)) {
        let ids = [];
        data.forEach((item) => {
          controlData.delete(item.id);
          ids.push(item.id);
        });

        this.delWorkingArea(ids);
      } else {
        controlData.delete(data.id);
        this.delWorkingArea(data.id);
      }
      this.controlData = new Map(controlData);
      this.tabConfig[this.activeTab].isRenderTree = true;
    },

    clearControlData(map) {
      this.controlData.clear();
    },
    delWorkingArea(ids) {
      this.tabConfig.forEach((item, i) => {
        let workingArea = lodash.cloneDeep(item.workingArea);
        if (Array.isArray(ids)) {
          ids.forEach((items) => {
            if (!item.workingArea.has(items)) return;
            workingArea.delete(items);
          });
        } else {
          if (!item.workingArea.has(ids)) return;
          workingArea.delete(ids);
        }

        this.tabConfig[i].workingArea = new Map(workingArea);
      });
    },

    /**
     * @param status true 给拥有此id的workingArea添加 反之
     */
    setPublicIds(data, status) {
      this.tabConfig.forEach((item, i) => {
        let workingArea = lodash.cloneDeep(item.workingArea);
        data.forEach((item) => {
          status ? workingArea.set(item.id, item) : workingArea.delete(item);
        });

        this.tabConfig[i].workingArea = new Map(workingArea);
      });
    },

    /**
     *
     */
  },
  watch: {
    controlData: {
      handler(val) {
        let num = val.size;

        num ? this.$set(this, 'trueText', `确 定(${num})`) : this.$set(this, 'trueText', `确 定`);
      },
    },
  },
};
</script>
<style lang="scss" scoped>
  @import "@/styles/select-com.scss";
</style>
