<template>
  <div class="system-parameters">
    <div class="asearch-form">
      <el-form @submit.native.prevent>
        <el-form-item label="">
          <el-input
            v-model="keies"
            clearable
            :focus="true"
            class="input-search"
            placeholder="请输入内容"
            @keyup.enter.native="onSearch"
          >
            <el-button
              slot="append"
              icon="el-icon-alisearch"
              @click="onSearch"
            ></el-button>
          </el-input>
        </el-form-item>
      </el-form>
    </div>
    <operation-com
      @handlerType="operationHandler"
      :btnConfigs="btnConfigs"
    ></operation-com>
    <el-table
      v-loading="loading"
      element-loading-spinner="el-icon-loading"
      element-loading-background="rgba(255, 255, 255, 1)"
      size="mini"
      border
      ref="multipleTable"
      :data="tableData"
    >
      <el-table-column
        v-for="item in tableColumn"
        :key="item.prop"
        :label="item.label"
        :prop="item.prop"
        :width="item.width"
      >
        <template slot-scope="scope">
          <el-input
            v-if="scope.row.editState"
            v-model="scope.row[item.prop]"
            :class="scope.row[item.prop] ? '' : 'input-empty'"
          />
          <div class="tabel-text" v-else @click="tableClick(scope.row)">
            {{ scope.row[item.prop] }}
          </div>
        </template>
      </el-table-column>

      <el-table-column label="操作" width="120">
        <template slot-scope="scope">
          <el-button
            @click.native.prevent="deleteRow(scope.$index, tableData)"
            type="text"
            size="small"
          >
            移除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <i-pagination
      v-if="pageInfo.total"
      :total="pageInfo.total"
      :pageSize="pageInfo.pageSize"
      @changePageSize="changePageSize"
      @changeCurrentPage="changeCurrentPage"
    />
  </div>
</template>
<script>
import requset from "@/api/api";
import Pagination from "@/components/pagination";
import lodash from "lodash";
import operationCom from "@/components/operation";

export default {
  name: "SystemParameters",
  props: {
    orgId: String | Number,
  },

  watch: {
    orgId() {
      this.editId = [];
      this.allTableData = [];
      this.deleteValue = [];
      this.requsetList();
    },
  },

  components: {
    "i-pagination": Pagination,
    operationCom,
  },

  data() {
    return {
      keies: "",
      tableData: [],
      allTableData: [],
      deleteValue: [],
      maxHeight: "",
      editId: [],
      loading: false,
      maxActivePageNo: 1,
      btnConfigs: [
        {
          type: "add",
          text: "新增",
          icon: "el-icon-aliadd",
          // has: 'ucenter_person_add',
          handlerType: "addTable",
        },
        {
          type: "refresh",
          text: "更新缓存",
          icon: "el-icon-alirefresh",
          // has: 'ucenter_person_edit',
          handlerType: "updateTable",
        },
      ],
      pageShowMax: 1,
      pageInfo: {
        total: 1,
        pageNo: 1,
        pageSize: 10,
      },
      tableColumn: [
        {
          label: "描述",
          prop: "descript",
        },
        {
          label: "key",
          prop: "keies",
        },
        {
          label: "值",
          prop: "value",
        },
        {
          label: "类型",
          prop: "type",
          width: "200",
        },
        {
          label: "子类型",
          prop: "subType",
          width: "200",
        },
      ],
    };
  },

  mounted() {
    this.requsetList();
  },

  methods: {
    //操作按钮
    operationHandler(type) {
      this[type]();
    },
    /* 请求列表 */
    async requsetList() {
      if (!this.orgId) {
        return ;
      }

      try {
        this.loading = true;
        const { pageInfo, keies } = this;
        const { data } = await requset.sysParameterList({
          orgId: this.orgId,
          keies,
          ...pageInfo,
        });

        const { total, list } = data;
        const dataMap = list.map((item) => ({ editState: false, ...item }));
        this.pageInfo = {
          ...pageInfo,
          total,
        };
        if (pageInfo.pageNo === 1) {
          this.allTableData = dataMap;
        } else {
          this.allTableData = !this.allTableData.length
            ? dataMap
            : [...this.allTableData, ...dataMap];
        }
        this.paginationChange();
        // this.gainHeight();
      } catch (err) {
        console.error(err);
      }
      this.loading = false;
    },

    /* 触发分页 */
    paginationChange() {
      const {
        pageInfo: { pageSize = 1, pageNo },
        allTableData,
      } = this;
      let max = pageSize * pageNo;
      let min = max - pageSize;
      this.pageShowMax = max;
      this.tableData = allTableData.slice(min, max);
    },

    /* 搜索 */
    onSearch: lodash.debounce(function () {
      this.pageInfo = {
        total: 1,
        pageNo: 1,
        pageSize: 10,
      };
      this.requsetList();
    }, 500),

    /* 判断是否有未填写的 */
    judgeEmpty() {
      const tableDataFilter = this.tableData.filter((i) => i.editState)[0];
      if (tableDataFilter) {
        const dataEmpty = this.tableColumn.filter(
          (i) => !tableDataFilter[i.prop]
        );

        if (dataEmpty.length) {
          this.$message.error(
            dataEmpty.map((i) => i.label).join("和") + "未填写!"
          );
          return false;
        }
        tableDataFilter.editState = false;
      }
      return true;
    },

    tableClick(row) {
      if (this.judgeEmpty()) {
        this.editId.push(row.id);
        row.editState = true;
      }
    },
    /* 获取值 */
    getForm() {
      const editId = [...new Set(this.editId)];
      return this.allTableData.filter((i) => !i.id || editId.includes(i.id));
    },

    getDelete() {
      return this.deleteValue.join(",");
    },

    updateTable() {

      if (!this.orgId) {
        this.$message.error('请先选择机构！');
        return;
      }

      this.pageInfo = {
        total: 1,
        pageNo: 1,
        pageSize: 10,
      };
      this.keies = "";
      this.allTableData = [];
      this.$nextTick(() => this.requsetList());
    },

    addTable() {

      if (!this.orgId) {
        this.$message.error('请先选择机构！');
        return;
      }

      if (!this.judgeEmpty()) return;
      const initialValue = Object.fromEntries(
        this.tableColumn.map((i) => [i.prop, ""])
      );
      this.allTableData.splice(this.pageShowMax - 1, 0, {
        initialValue,
        editState: true,
      });
      this.pageInfo.total = this.allTableData.length;
      this.paginationChange();
    },

    deleteRow(index) {
      const {
        pageInfo: { pageSize, pageNo },
      } = this;
      const allListIndex = (pageNo - 1) * pageSize + index;
      const spliceData = this.allTableData.splice(allListIndex, 1);
      this.deleteValue.push(spliceData[0].id);
      this.paginationChange();
    },

    changePageSize({ pageSize }) {
      //配置表格
      this.pageInfo.pageSize = pageSize;
      this.pageInfo.pageNo = 1;
      this.requsetList();
    },

    changeCurrentPage({ currentPage }) {
      this.pageInfo.pageNo = currentPage;
      if (currentPage > this.maxActivePageNo) {
        this.maxActivePageNo = currentPage;
        this.$nextTick(() => {
          this.requsetList();
        });
      } else {
        this.paginationChange();
      }
    },
    /* 
    gainHeight() {
      this.$nextTick(() => {
        this.$formatTableHeight().then((data) => {
          console.log(data);
          this.maxHeight = data;
        });
      });
    }, */
  },
};
</script>

<style lang="scss" scoped>
.system-parameters {
  /deep/ .el-input__inner {
    font-size: 12px;
  }
  .system-parameters-search {
    width: 200px;
    margin-bottom: 10px;
  }

  .table-title {
    border: 1px solid #ebeef5;
    border-bottom: none;
    padding-left: 10px;
  }
  .tabel-text {
    min-height: 30px;
  }
  /deep/.el-table td,
  /deep/.el-table th {
    padding: 2px 0;
  }
  .input-empty {
    /deep/.el-input__inner {
      border-color: #f56c6c;
    }
  }
}
//临时添加
.operation-btn {
  margin: 0;
  padding-left: 0;
}
</style>