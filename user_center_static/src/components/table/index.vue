<template>
  <div
    :class="['table-list', tableClass, tableTitcolumn, !tableData.length ? 'table-nodata' : '']"
    v-loading="tbLoading"
    element-loading-text="加载中..."
    element-loading-spinner="el-icon-loading"
    element-loading-background="rgba(255, 255, 255, 1)"
  >
    <p class="table-unit" v-if="tableUnit && Object.keys(tableUnit).length">
      {{ tableUnit.text }}
    </p>
    <el-table
      :data="tableData"
      style="width: 100%"
      :stripe="stripe"
      :border="border"
      :max-height="maxHeight"
      :key="mainTableKey"
      ref="multipleTable"
      :row-class-name="tableRowClassName"
      :cell-class-name="columnClassName"
      :highlight-current-row="highlightCurrentRow"
      :cell-style="cellStyle"
      :row-key="rowKey"
      :default-expand-all="defaultExpandAll"
      :index="indexMethod"
      :show-summary="showSummary"
      :summary-method="showSummary ? getSummaries : null"
      :span-method="handleSpanMethod"
      :current-row-key="currentRowKey"
      @selection-change="handleSelectionChange"
      @row-click="handleRowClick"
      @current-change="handleCurrentChange"
      @row-dblclick="handleDbClick"
      @cell-mouse-enter="showTooltip"
      @cell-mouse-leave="hideTooltip"
    >
      <!-- 复选框 -->
      <el-table-column
        type="selection"
        align="center"
        width="50"
        :selectable="handelSelecTable"
        v-if="iSelection && tableTit.length"
        :resizable="false"
      >
      </el-table-column>

      <!-- 序号 -->
      <el-table-column
        type="index"
        label="序号"
        :width="numWidth"
        :isNums="isNums"
        :align="!iSelection ? 'center' : 'left'"
        v-if="isNums && tableTit.length"
        :resizable="false"
      >
        <template slot-scope="scope">
          {{ scope.row.mergeIndex || formatIndex(scope) }}
        </template>
      </el-table-column>

      <!-- 邮箱已读未读 -->
      <el-table-column
        v-if="isStatus"
        width="40"
        align="center"
        label=""
        :resizable="false"
      >
        <template slot-scope="scope">
          <slot :row="scope.row" name="emailStatus"></slot>
        </template>
      </el-table-column>

      <!-- 邮箱附件 -->
      <el-table-column
        v-if="isAnnex"
        width="40"
        align="center"
        label=""
        :resizable="false"
      >
        <template slot-scope="scope">
          <slot :row="scope.row" name="emailAnnex"></slot>
        </template>
      </el-table-column>

      <!-- 列表操作列 -->
      <el-table-column
        v-if="fixed != 'right' && isOperation && tableTit.length"
        :label="operationText"
        align="left"
        :width="operationWidth"
        :resizable="false"
      >
        <template slot-scope="scope">
          <slot :row="scope.row"></slot>
        </template>
      </el-table-column>

      <!-- 列表表头 S-->
      <template v-for="(item, index) of tableTit">
        <!-- 多级表头 -->
        <MyColumn
            v-if="item.type == 'multistep'"
            :key="index"
            :data="item"
        />
        <!-- <el-table-column
          v-if="item.type == 'multistep' && item.show"
          :align="item.alignType ? item.alignType : 'center'"
          :key="index"
          :label="item.label"
        >
          <el-table-column
            v-for="(child, i) in item.children"
            :key="i"
            :label="child.label"
            :prop="child.prop"
            :width="child.width"
            :min-width="child.minWidth"
          >
          </el-table-column>
        </el-table-column> -->

        <el-table-column
          v-if="item.type == 'click'"
          :prop="item.prop"
          :label="item.label"
          :width="item.width"
          :min-width="item.minWidth"
          :align="item.alignType ? item.alignType : 'left'"
          :key="item.prop"
          :show-overflow-tooltip="item.isTooltip !== undefined ? item.isTooltip : isTooltip"
          class-name="item-click"
          :label-class-name="item.orderBy ? 'is-sort' : ''"
          :resizable="false"
        >
          <template slot-scope="scope">
            <span
              :class="item.isOperation ? 'operat-link' : ''"
              @click="handleLookClick($event, scope.row)"
              v-if="item.isOperation"
              >{{ scope.row[item.prop] }}</span
            >
            <span v-else>{{ scope.row[item.prop] }}</span>
          </template>
          <!-- 列排序 -->
          <template v-slot:header="scope" v-if="item.orderBy">
            <div
              class="dv-sort"
              @click="handleSortClick(item.orderBy, item.asc, scope, $event)"
            >
              <span>{{ item.label }}</span>
              <em class="sort-icon" v-show="item.asc == ''"
                ><i class="arrow-top"></i><i class="arrow-bottom"></i
              ></em>
              <em class="sort-icon sort-up" v-show="item.asc == 'desc'"
                ><i class="arrow-top"></i><i class="arrow-bottom"></i
              ></em>
              <em class="sort-icon sort-bottom" v-show="item.asc == 'asc'"
                ><i class="arrow-top"></i><i class="arrow-bottom"></i
              ></em>
            </div>
          </template>
        </el-table-column>
        <!-- 列表type!=isSlot -->
        <el-table-column
          v-if="
            item.type != 'click' &&
            item.type != 'isSlot' &&
            item.type != 'multistep'
          "
          :prop="item.prop"
          :label="item.label"
          :width="item.width"
          :min-width="item.minWidth"
          :align="item.alignType"
          :key="item.prop"
          :show-overflow-tooltip="item.isTooltip !== undefined ? item.isTooltip : isTooltip"
          :label-class-name="item.orderBy ? 'is-sort' : ''"
          :resizable="false"
        >
          <template v-slot:header="scope" v-if="item.orderBy">
            <div
              class="dv-sort"
              @click="handleSortClick(item.orderBy, item.asc, scope, $event)"
            >
              <span>{{ item.label }}</span>
              <em class="sort-icon" v-show="item.asc == ''"
                ><i class="arrow-top"></i><i class="arrow-bottom"></i
              ></em>
              <em class="sort-icon sort-up" v-show="item.asc == 'desc'"
                ><i class="arrow-top"></i><i class="arrow-bottom"></i
              ></em>
              <em class="sort-icon sort-bottom" v-show="item.asc == 'asc'"
                ><i class="arrow-top"></i><i class="arrow-bottom"></i
              ></em>
            </div>
          </template>
        </el-table-column>

        <!-- 列表type=isSlot -->
        <el-table-column
          v-if="item.type == 'isSlot'"
          :prop="item.prop"
          :label="item.label"
          :width="item.width"
          :min-width="item.minWidth"
          :align="item.alignType"
          :key="item.prop"
          :resizable="false"
        >
          <template v-slot:header="scope" v-if="item.orderBy">
            <div
              class="dv-sort"
              @click="handleSortClick(item.orderBy, item.asc, scope, $event)"
            >
              <span>{{ item.label }}</span>
              <em class="sort-icon" v-show="item.asc == ''"
                ><i class="arrow-top"></i><i class="arrow-bottom"></i
              ></em>
              <em class="sort-icon sort-up" v-show="item.asc == 'desc'"
                ><i class="arrow-top"></i><i class="arrow-bottom"></i
              ></em>
              <em class="sort-icon sort-bottom" v-show="item.asc == 'asc'"
                ><i class="arrow-top"></i><i class="arrow-bottom"></i
              ></em>
            </div>
          </template>
          <template slot-scope="scope">
            <slot
              :name="slotName || item.slotName"
              :scope="scope"
            >
            </slot>
          </template>
        </el-table-column>
      </template>
      <!-- 列表表头 E-->

      <!-- 列表操作fixed=right -->
      <el-table-column
        v-if="fixed == 'right' && isOperation && tableTit.length"
        :label="operationText"
        align="left"
        :width="operationWidth"
        :resizable="false"
      >
        <template slot-scope="scope">
          <slot :row="scope.row"></slot>
        </template>
      </el-table-column>

      <!-- 列表配置 -->
      <el-table-column
        v-if="showSet && tableTit.length"
        label=""
        align="center"
        width="32"
        class-name="table-setcol-box"
        :resizable="false"
      >
        <template slot-scope="" slot="header">
          <div class="table-setcol" @click="handleSetClick">
            <i class="table-set el-icon-aliset" title="设置显示列"></i>
          </div>
        </template>
      </el-table-column>

      <!-- 列表无数据 S-->
      <template slot="empty">
        <EmptyComponent
          class="nodata-tips"
          :size="emptySize || '80px'"
          color="#ccc"
          v-if="!tableData || tableData.length"
        />
        <!-- <div class="nodata-tips" v-show="!tbLoading">
          <svg-icon iconClass="nodata"/>
          <p>暂无数据</p>
        </div> -->
      </template>
    </el-table>

    <!-- 重置表头弹窗 -->
    <component
      :is="changeTable"
      :showChangeTable="showChangeTable"
      :seTableList="seTableList"
      @closeDialog="showChangeTable = false"
      @trueClick="handleTrueClick"
      @defaultClick="handleDefaultClick"
    />
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import MyColumn from "./el-table-column";
import EmptyComponent from "@/components/empty";
import props from "./props";
import methods from "./methods";
import changeTable from "./changeTable";
export default {
  name: 'TableCom',
  components: { EmptyComponent, MyColumn },
  mixins: [changeTable],
  props,
  data() {
    return {
      highlightCurrentRow: true,
      baseUrl: window.location.origin,
      tableClass: `table-${parseInt(Math.random() * Math.pow(10, 16))}`,
      page: {
        No: this.pageNo,
        Size: this.pageSize,
      },
      mainTableKey: "",
      maxHeight: null,
      drag: {
        tableParent: null,
        root: null,
        target: null,
        column: null,
        enable: false,
        width: 0,
        offsetX: 0,
        index: 0,
        table: null,
        widthBox: {},
      },
      selectRow: [],
      multipleSelection: [],
    };
  },
  watch: {
    currentRowKey: {
      handler(val) {
        // console.log(val, '==+++++');
      },
      immediate: true
    },
    height: {
      handler(data) {
        this.maxHeight = data;
      },
      immediate: true
    },
    multipleSelection(data) {
      //存储选中的row
      this.selectRow = [];
      if (data?.length) {
        data.forEach((item, index) => {
          this.selectRow.push(item[this.currentRowKey]);
        });
      }
    },
    tableTit: {
      handler(val) {
        this.mainTableKey = Math.random();
      },
      deep: true,
    },
    tableData: {
      handler(val) {
        val.forEach((item) => {
          item.hasOwnProperty("startTime") &&
            (item.startTime = this.$clearSecond(item.startTime));
          item.hasOwnProperty("endTime") &&
            (item.endTime = this.$clearSecond(item.endTime));
          item.hasOwnProperty("durationMillisTime") &&
            (item.durationMillisTime = this.$transSecond(
              item.durationMillisTime
            ));
        });
        let pageNo = this.pageNo;
        let pageSize = this.pageSize;
        if (pageNo === 1) {
          // if (this.$parent.searchForm && this.$parent.searchForm.pageNo) {
          //   pageNo = this.$parent.searchForm.pageNo;
          //   pageSize = this.$parent.searchForm.pageSize;
          // }
        }
        this.page.No = pageNo;
        this.page.Size = pageSize;
        if (this.fixedHeight && !this.height) {
          this.gainHeight();
        }
      },
      deep: true,
      immediate: true,
    },
  },
  computed: {
    ...mapGetters(["sidebar"]),
    indexMethod(i) {
      const { pageNo, pageSize } = this
      return i + pageSize * (pageNo - 1)
    },
    numWidth(){
      const bodyW = document.body.clientWidth;
      if(bodyW > 1501 && bodyW <= 1920){
        return '60px'
      } else if(bodyW > 1920) {
        return '90px'
      } else {
        return '45px'
      }
    }
  },
  mounted() {
    this.$emit("mounted", true);

    setTimeout(() => {
      this.initDrag();
    }, 1000);
  },
  beforeDestroy() {
    this.release();
  },
  methods,
};
</script>

<style lang="scss" scoped>
@import "@/styles/table.scss";
</style>
