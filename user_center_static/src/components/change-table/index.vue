<template>
  <div class="change-table-dialog">
    <el-dialog
      ref="_dialog"
      :visible.sync="showChangeTable"
      v-dialogDrag
      class="change-table-dia"
      :append-to-body="true"
      :before-close="beforeClose"
      style="display: none"
      @closed="closed"
    >
      <template slot="title">
        <div class="alertTitle">
          <i class="el-icon-alicolumn-tit"></i>列表设置<span>您可以对列名进行排序和显示设置</span>
        </div>
      </template>
      <el-table
        :data="tableList"
        :height="height"
        class="table-list"
        ref="seTable"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" align="center" width="55" @select="handleSelectionChange">
        </el-table-column>
        <el-table-column prop="colName" label="列名" width="120"></el-table-column>
        <el-table-column label="显示名" label-class-name="el-icon-alimodify">
          <template slot-scope="scope">
            <el-input placeholder="" v-model="scope.row.newName" :disabled="scope.row.isEdit"></el-input>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template slot-scope="scope">
            <el-button @click="handlePrevClick(scope.$index - 1)" type="text" size="small" :disabled="scope.$index == 0"
              >上移</el-button
            >
            <el-button
              type="text"
              size="small"
              :disabled="scope.$index == tableList.length - 1"
              @click="handleNextClick(scope.$index - 1)"
              >下移</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleDefaultClick">恢复默认值</el-button>
        <el-button type="primary" @click="handleTrueClick">确定</el-button>
        <el-button @click="closeDialog">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import lodash from 'lodash';
export default {
  name: 'changeTableCom',
  props: {
    showChangeTable: {
      type: Boolean,
      default: () => false,
    },
    seTableList: {
      type: Array,
      default: () => [],
    },
  },
  watch: {
    showChangeTable(type) {
      this.tableList = this.seTableList;
      setTimeout(() => {
        this.tableList.forEach((item) => {
          if (item.isSelected == 1) {
            this.$refs.seTable.toggleRowSelection(item);
          }
        });
      }, 0);
    },
  },
  mounted() {},
  data() {
    return {
      tableList: [],
      multipleSelection: [],
      height: (document.documentElement.clientWidth / 1366) * 250,
    };
  },
  methods: {
    closed() {
        this.$nextTick(() => {
            this.$refs._dialog.$el.firstElementChild.removeAttribute('style')
        })
    },
    closeDialog() {
      this.$emit('closeDialog');
    },
    handleTrueClick() {
      
      let list = lodash.cloneDeep(this.multipleSelection);
      
      list.map((item, index) => {
        item.orderNo = index + 1;
        item.colName = item.newName;
      });
      // list.sort((a, b) => a.orderNo - b.orderNo)
      console.log(list, 'list---');
      this.$emit('trueClick', list);
    },
    handleDefaultClick() {
      this.$confirm('确定要重置吗?', '提示',  {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          this.$emit('defaultClick');
        })
        .catch(() => {});
    },
    beforeClose() {
      this.$emit('closeDialog');
      return false;
    },
    handleSelectionChange(val) {
      let newList = [];
      this.tableList.forEach((item) => {
        val.map((i) => {
          
          if (i.colKey == item.colKey) {
            newList.push(i);
          }
        });
      });
      this.multipleSelection = newList;
    },
    handlePrevClick(idx) {
      this.tableList[idx] = this.tableList.splice(idx + 1, 1, this.tableList[idx])[0];
      this.computedTable();
    },
    handleNextClick(idx) {
      this.tableList[idx + 2] = this.tableList.splice(idx + 1, 1, this.tableList[idx + 2])[0];
      this.computedTable();
    },
    computedTable() {
      let newList = [];
      this.tableList.forEach((item) => {
        this.multipleSelection.map((i) => {
          
          if (i.colKey == item.colKey) {
            newList.push(i);
          }
        });
      });
      this.multipleSelection = newList;

      this.multipleSelection.map((v, i) => v.orderNo = i + 1)

      this.multipleSelection.sort((a, b) => a.orderNo - b.orderNo)
    },
  },
};
</script>

<style lang="scss" scoped>
  @import "@/styles/dialog.scss";
</style>
