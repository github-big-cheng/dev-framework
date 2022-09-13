<!--
 * @Author: your name
 * @Date: 2021-03-31 17:47:54
 * @LastEditTime: 2021-08-20 14:42:00
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \zfzd_oa\src\components\selectionDialog\choice\component\control.vue
-->
<template>
  <div>
    <el-input
      v-model.trim="selectedZzValue"
      clearable
      class="search-ipt selected-zz"
      ref="touchFocus"
      placeholder="请输入关键字搜索"
      @input="filterSelectedData"
    >
      <el-button slot="append" icon="el-icon-alisearch"></el-button>
    </el-input>
    <div class="selected-col">
      <div class="s-list" v-show="!showselectedZz">
        <draggable
          v-model="selectedOrganization"
          chosen-class="chosen"
          force-fallback="true"
          group="people"
          animation="300"
          delay="100"
          @start="onStart"
          @end="onEnd"
        >
          <transition-group>
            <div
              v-for="item in selectedOrganization"
              :key="item.id"
              :style="{
                height: renderPerson ? '48px' : '30px',
              }"
              :class="item.isZzSelected ? 's-item is-selected' : 's-item'"
              @click="handleSelectedZz(item.id)"
              @dblclick.stop="handleDbRemove(item.id)"
            >
              <div v-if="!renderPerson" class="noRenderP">
                <div class="item-user">
                  <i
                    :class="firstIcon"
                    :style="{
                      color: item.isDisabled ? '#ccc' : '#E5E5E5',
                    }"
                  ></i>
                </div>
                <div class="item-desc">
                  <div
                    class="i-name"
                    :style="{
                      color: item.isDisabled ? '#ccc' : '#333',
                    }"
                    v-html="item[keyName]"
                  ></div>
                </div>
              </div>
              <div v-else class="renderPerson">
                <div class="item-user">
                  <span
                    v-if="renderPerson && item.imgPath"
                    :style="{
                      color: item.isDisabled ? '#ccc' : '#E5E5E5',
                    }"
                    ><img class="tuser-avatar" :src="URL + '/file' + item.imgPath"
                  /></span>
                  <span
                    v-if="renderPerson && !item.imgPath"
                    class="el-icon-aliuser default-avatar"
                    :style="{
                      color: item.isDisabled ? '#ccc' : '#E5E5E5',
                    }"
                  ></span>
                </div>
                <div class="item-desc">
                  <div
                    class="i-name"
                    :style="{
                      color: item.isDisabled ? '#ccc' : '#333',
                    }"
                    v-html="item[keyName]"
                  ></div>
                  <div
                    class="i-dept"
                    :style="{
                      color: item.isDisabled ? '#ccc' : '#999',
                    }"
                    v-html="item[keyName2]"
                    v-if="item[keyName2]"
                  ></div>
                </div>
              </div>
              <i class="el-icon-alipitchon" v-show="item.isZzSelected"></i>
            </div>
          </transition-group>
        </draggable>
      </div>

      <div class="s-list" v-show="showselectedZz" style="z-index: 10">
        <draggable v-model="selectedSearchZz" group="people" delay="100" animation="0" @start="onStart" @end="onEnd">
          <transition-group>
            <div
              v-for="(item, index) of selectedSearchZz"
              :key="item.id"
              :style="{
                height: renderPerson ? '48px' : '30px',
              }"
              :class="item.isZzSelected ? 's-item is-selected ' : 's-item'"
              @click="handleSelectedSearchZz(item.id, index)"
              @dblclick="handleSearchDbRemove(item.id, index)"
            >
              <div v-if="!renderPerson" class="noRenderP">
                <div class="item-user">
                  <i
                    :class="firstIcon"
                    :style="{
                      color: item.isDisabled ? '#ccc' : '#E5E5E5',
                    }"
                  ></i>
                </div>
                <div class="item-desc">
                  <div
                    class="i-name"
                    :style="{
                      color: item.isDisabled ? '#ccc' : '#333',
                    }"
                    v-html="item.personName2"
                  ></div>
                </div>
              </div>
              <div v-else class="renderPerson">
                <div class="item-user">
                  <span
                    v-if="renderPerson && item.imgPath"
                    :style="{
                      color: item.isDisabled ? '#ccc' : '#E5E5E5',
                    }"
                    ><img class="tuser-avatar" :src="URL + '/file' + item.imgPath"
                  /></span>
                  <span
                    v-if="renderPerson && !item.imgPath"
                    class="el-icon-aliuser default-avatar"
                    :style="{
                      color: item.isDisabled ? '#ccc' : '#E5E5E5',
                    }"
                  ></span>
                </div>
                <div class="item-desc">
                  <div
                    class="i-name"
                    :style="{
                      color: item.isDisabled ? '#ccc' : '#333',
                    }"
                    v-html="item.personName2"
                  ></div>
                  <div
                    class="i-dept"
                    :style="{
                      color: item.isDisabled ? '#ccc' : '#999',
                    }"
                    v-html="item.defDeptName2"
                  ></div>
                </div>
              </div>
              <i class="el-icon-alipitchon" v-show="item.isZzSelected"></i>
            </div>
          </transition-group>
        </draggable>
      </div>
    </div>
  </div>
</template>

<script>
const URL = window.location.origin;

import draggable from 'vuedraggable';
import lodash from 'lodash';
let TIMER = null;
export default {
  components: {
    draggable,
  },
  props: {
    controlData: {
      type: Map,
      default: () => new Map(),
    },
    renderType: {
      type: String,
      default: '',
    },
    firstIcon: {
      type: String,
      default: () => 'tree-org',
    },
  },
  data() {
    return {
      selectedZzValue: '',
      selectedOrganization: [],
      selectedSearchZz: [],
      showselectedZz: false,
      URL,
      renderPerson: this.renderType == 'partyPerson' || this.renderType == 'deptPerson',
    };
  },
  watch: {
    controlData: {
      handler(newData) {
        this.selectedOrganization = [];
        for (let value of newData.values()) {
          // let item = lodash.cloneDeep(value);
          !value.hasOwnProperty('isZzSelected') && (value.isZzSelected = false);

          this.selectedOrganization.push(value);
        }

        if (!this.showselectedZz) return;
        let data = this.selectedSearchZz.filter((item) => {
          return newData.get(item.id);
        });
        this.selectedSearchZz = data;
      },
      deep: true,
    },
    selectedZzValue(val) {
      if (!val) {
        this.showselectedZz = false;
        this.selectedSearchZz = [];
      }
    },
  },
  mounted() {
    this.$nextTick(() => {
      this.$refs['touchFocus'].focus();
    });
  },
  computed: {
    keyName() {
      return this.renderPerson ? 'name' : 'cname';
    },
    keyName2() {
      return this.renderPerson ? 'orgName' : 'cname';
    },
  },
  methods: {
    filterSelectedData() {
      this.throttle(this.filterSearch, 300)();
    },
    filterSearch() {
      let map = new Map();
      let query = this.selectedZzValue;
      for (let [key, value] of this.controlData) {
        if (value[this.keyName].indexOf(query) != -1 || value[this.keyName2].indexOf(query) != -1) {
          value.personName2 = this.transName(query, value[this.keyName]);
          value.defDeptName2 = this.transName(query, value[this.keyName2]);
          map.set(key, lodash.cloneDeep(value));
        }
      }
      this.selectedSearchZz = Array.from(map.values());
      this.showselectedZz = true;
      this.$emit('update:searchValue', this.selectedZzValue);
    },
    handleSearchDbRemove(id, index) {
      let del = this.controlData.get(id);
      this.selectedSearchZz.splice(index, 1);
      this.$emit('selected', del, 'del');
      this.$emit('setPublicSelect', del, false);
    },
    handleSelectedSearchZz(id, index) {
      let status = !this.selectedSearchZz[index].isZzSelected;
      this.selectedSearchZz[index].isZzSelected = status;
      this.$emit('setPublicSelect', this.controlData.get(id), status);
    },
    transName(query, name) {
      let reg = new RegExp(query, 'g');
      name = name.replace(query, `<label style="color: #fa8c16 !important;">${query}</label>`);
      return `<span>${name}<span>`;
    },
    /**
     * 防抖
     */
    throttle(fn, delay) {
      // last为上一次触发回调的时间, timer是定时器
      let last = 0,
        timer = null;
      // 将throttle处理结果当作函数返回

      return function() {
        // 保留调用时的this上下文
        let context = this;
        // 保留调用时传入的参数
        let args = arguments;
        // 记录本次触发回调的时间
        let now = +new Date();

        // 判断上次触发的时间和本次触发的时间差是否小于时间间隔的阈值
        if (now - last < delay) {
          // 如果时间间隔小于我们设定的时间间隔阈值，则为本次触发操作设立一个新的定时器
          clearTimeout(timer);
          timer = setTimeout(function() {
            last = now;
            fn.apply(context, args);
          }, delay);
        } else {
          // 如果时间间隔超出了我们设定的时间间隔阈值，那就不等了，无论如何要反馈给用户一次响应
          last = now;
          fn.apply(context, args);
        }
      };
    },
    handleSelectedZz(id) {
      let status = !this.controlData.get(id).isZzSelected;

      this.$emit('setPublicSelect', this.controlData.get(id), status);
    },
    handleDbRemove(id) {
      let del = this.controlData.get(id);
      this.$emit('selected', del, 'del');
      this.$emit('setPublicSelect', del, false);
    },
    onStart() {},
    onEnd() {
      let map = new Map();
      this.selectedOrganization.forEach((item) => {
        map.set(item.id, item);
      });
      this.$emit('setControlData', map);
    },
  },
};
</script>

<style lang="scss" scoped>
  @import "@/styles/select-com.scss";
</style>
