<template>
  <!-- 职务管理 -->
  <table-group
    ref="listPage"
    :table-url="tableUrl"
    :search-list="searchList"
    :btn-configs="btnConfigs"
    :table-title="tableTitle"
    :table-title-code="tableTitleCode"
    :has-look="hasLook"
    :table-porps="tablePorps"
    @handlerType="operationHandler"
    @clickSelection="handleClickSelection"
    @lookClick="handleViewClickFunc"
    @dbTableClick="dbEditClick"
  >
  </table-group>
</template>

<script>
import tableGroup from "@/components/table-group";
import { searchList, btnConfigs, tableTitle } from "./config";

export default {
  name: "postList",
  components: {
    tableGroup,
  },
  data() {
    return {
      searchList: searchList({}),
      btnConfigs,
      tableTitle,
      tableUrl: "getPositionList",
      tableTitleCode: "ucenter_position_list",
      selectionList: [],
      tablePorps: {
        isTooltip: false,
      },
      hasLook: ["ucenter_position_edit", "ucenter_position_view"],
    };
  },
  created() {
    this.$route.meta.noLoading = true;
  },
  mounted() {
    this.getComboxList();
  },
  methods: {
    getComboxList() {
      this.$http.getUcenterCodeCombox({ type: "10001-70005" }).then((res) => {
        const { code, data } = res;
        if (code == 0) {
          this.searchList = searchList({ jobTypeChildren: data });
        }
      });
    },
    handleClickSelection(val) {
      this.selectionList = val;
    },
    //查看函数
    handleViewClickFunc({ id }) {
      if (!this.$filterBtnShow(["ucenter_position_view"])) {
        this.$showWarning("对不起，您没有查看的权限");
        return false;
      }
      this.$router.push({
        name: "postView",
        params: { noCache: true, id },
      });
      return true;
    },
    //编辑函数
    handleEditClickFunc({ id }) {
      if (!this.$filterBtnShow(["ucenter_position_edit"])) {
        this.$showWarning("对不起，您没有修改的权限");
        return false;
      }
      this.$router.push({
        name: "postEdit",
        params: { noCache: true, id },
      });
      return true;
    },
    //编辑
    handleEditClick() {
      if (this.selectionList.length == 0) {
        this.$showWarning("请选择一条数据");
      } else if (this.selectionList.length > 1) {
        this.$showWarning("只能选择一条数据");
      } else {
        this.handleEditClickFunc({ id: this.selectionList[0].id });
      }
    },
    //双击
    dbEditClick(item) {
      this.handleEditClickFunc(item) || this.handleViewClickFunc(item);
    },
    //操作按钮
    operationHandler(type) {
      this?.[type]?.();
    },
    handleAddClick() {
      this.$router.push({
        name: "postAdd",
        params: { noCache: true },
      });
    },
    handleDeleteClick() {
      if (this.selectionList.length == 0) {
        this.$showWarning("请选择要删除的数据");
        return;
      }
      this.$confirm("此操作会删除该职务, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          let ids,
            idsArr = [];
          this.selectionList.forEach((item) => {
            idsArr.push(item.id);
          });
          ids = idsArr.join(",");
          this.$http.getPositionDelete({ ids }).then((res) => {
            if (res.code == 0) {
              this.$showSuccess("删除成功！");
              this.$refs.listPage.reloadTableList();
            }
          });
        })
        .catch(() => {});
    },
  },
};
</script>
