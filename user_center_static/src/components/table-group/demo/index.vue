<template>
  <table-group
    :table-url="tableUrl"
    :search-list="searchList"
    :btn-configs="btnConfigs"
    :table-title="tableTitle"
    :table-title-code="tableTitleCode"
    @handlerType="operationHandler"
  >
    <template slot-scope="{ scope }" slot="statusSlot">
      <el-tag :type="scopeRowStatusColor[scope.row.status]">{{
        scope.row.statusName
      }}</el-tag>
    </template>
    <template slot-scope="{ scope }" slot="dataSource">
      <span v-if="scope.row.dataSource == 1">企业在线填报</span>
      <span v-if="scope.row.dataSource == 2">区县局填报</span>
      <span v-if="scope.row.dataSource == 3">批量导入</span>
    </template>
    <template slot-scope="{ scope }" slot="address">
      <span>{{ scope.row.address }}</span>
    </template>
    <template slot-scope="{ scope }" slot="companyName">
      <span>{{ scope.row.companyName }}</span>
    </template>
  </table-group>
</template>

<script>
import TableGroup from "@/components/table-group/index.vue";
import { searchList, btnConfigs, tableTitle } from "./config";
export default {
  components: {
    TableGroup,
  },
  data() {
    return {
      searchList,
      btnConfigs,
      tableTitle,
      tableUrl: "getBizcompsurveyList",
      tableTitleCode: "party_report_list_my",
      scopeRowStatusColor: {
        "40001-SAVE": "cbrown",
        "40001-CHECK": "cgreen",
        "40001-REPORTED": "cgray",
      },
    };
  },
  created() {
    
  },
  methods: {
    operationHandler(type) {
      this[type]();
    },
    handleAddClick() {
      let params = {
        noCache: true,
        id: this.searchForm.deptIdQuery,
      };
      if (this.searchForm.hasOwnProperty("deptIdQuery")) {
        delete params.deptIdQuery;
      }
      this.$router.push({
        name: "grainInvestigationAdd",
        params: { noCache: true },
      });
    },
    handleEditClick() {
      if (this.selectionList.length == 0) {
        this.$showWarning("请选择一条数据");
      } else if (this.selectionList.length > 1) {
        this.$showWarning("只能选择一条数据");
      } else if (this.selectionList[0].status != "40001-SAVE") {
        this.$showError("只有已保存状态的数据才可以修改");
      } else {
        this.handleEditClickFunc({ id: this.selectionList[0].id });
      }
    },
    //删除
    handleDeleteClick() {
      if (this.selectionList.length == 0) {
        this.$showWarning("请选择要删除的数据");
        return;
      }
      let status = true,
        idQueryIn = "",
        idQueryInArr = [];
      this.selectionList.forEach((item) => {
        idQueryInArr.push(item.id);
        item.status != "40001-SAVE" && (status = false);
      });
      if (!status) {
        this.$showError("只有已保存状态的数据才可以删除");
        return;
      }
      this.$confirm("此操作会删除该数据, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          idQueryIn = idQueryInArr.join(",");
          this.$http.delDizcompsurvey({ idQueryIn }).then((res) => {
            if (res.code == 0) {
              this.$showSuccess("删除成功！");
              this.getTableList();
            }
          });
        })
        .catch(() => {});
    },
    //审核
    handleExamineClick() {
      if (this.selectionList.length == 0) {
        this.$showWarning("请选择需要审核的数据");
        return;
      } else if (this.selectionList.length > 1) {
        this.$showWarning("只能选择一条数据");
        return;
      } else if (this.selectionList[0].status != "40001-SAVE") {
        this.$showError("只有已保存状态的数据才可以审核");
        return;
      }
      this.handleViewClickFunc({ id: this.selectionList[0].id });
    },
    //取消审核
    handleCancelExamineClick() {
      if (this.selectionList.length == 0) {
        this.$showWarning("请选择需要取消审核的数据");
        return;
      }
      let idQueryIn = "",
        status = true,
        idQueryInArr = [];
      this.selectionList.forEach((item) => {
        idQueryInArr.push(item.id);
        item.status != "40001-CHECK" && (status = false);
      });
      if (!status) {
        this.$showError("只有已审核状态的数据才可以取消");
        return;
      }
      idQueryIn = idQueryInArr.join(",");
      this.$http.cancelCheckDizcompsurvey({ idQueryIn }).then((res) => {
        if (res.code == 0) {
          this.$showSuccess("取消审核成功！");
          this.getTableList();
        }
      });
    },
    //上报
    handleReportClick() {
      if (this.selectionList.length == 0) {
        this.$showWarning("请选择需要上报的数据");
        return;
      } else if (this.selectionList[0].status !== "40001-CHECK") {
        this.$showError("只有已审核状态的数据才可以上报");
      } else {
        let idQueryIn = "",
          idQueryInArr = [];
        let status = true;
        this.selectionList.forEach((item) => {
          item.status !== "40001-CHECK" && (status = false);
          idQueryInArr.push(item.id);
        });
        if (!status) {
          this.$showError("只有已审核状态的数据才可以上报");
          return;
        }
        idQueryIn = idQueryInArr.join(",");
        this.$http.upReportDizcompsurvey({ idQueryIn }).then((res) => {
          if (res.code == 0) {
            this.$showSuccess("上报成功！");
            this.getTableList();
          }
        });
      }
    },
  },
};
</script>

<style>
</style>