<!--
 * @Author: your name
 * @Date: 2021-06-02 11:11:07
 * @LastEditTime: 2021-08-06 17:30:39
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \do_ucenter_fe\src\views\systemManager\menuManage\index.vue
-->
<template>
  <div class="main-wrapper menuManage">
    <div class="search-box">
      <div class="asearch-form input-w260">
        <el-form :inline="true" :model="searchForm" @submit.native.prevent>
          <el-form-item label="">
            <el-input
              v-model="searchForm.nameQueryLike"
              clearable
              :focus="true"
              class="input-search"
              placeholder="请输入菜单名称"
              @keyup.enter.native="reloadTableList"
            >
              <el-button
                slot="append"
                icon="el-icon-alisearch"
                @click="reloadTableList"
              ></el-button>
            </el-input>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <operation-com
      @handlerType="operationHandler"
      :btnConfigs="btnConfigs"
    ></operation-com>

    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane
        :label="item.name"
        :name="i + ''"
        v-for="(item, i) in allMenuList"
        :key="item.id"
      ></el-tab-pane>
    </el-tabs>

    <el-row
      :gutter="24"
      class="asideormain"
      :class="isAsideCollapse ? '' : 'aside-packup'"
    >
      <el-col
        :span="6"
        class="asidewrap"
        :width="isAsideCollapse ? '20px' : '290px'"
      >
        <div class="aside-con">
          <div class="hd" @click="isAsideCollapse = !isAsideCollapse">
            <h2 v-show="isAsideCollapse">菜单</h2>
            <span class="hd-right">
              <i class="el-icon-d-arrow-right"></i>
            </span>
          </div>
          <div class="bd">
            <fold-tree
              label="name"
              ref="zzTree"
              :treeLoading="treeLoading"
              :strictly="true"
              :treeList="treeListData"
              :highlight="true"
              :dfCheckedKeys="defaultIds"
              childrenName="subFunction"
              @clickNode="handleClickNode"
            ></fold-tree>
          </div>
        </div>
      </el-col>
      <el-col :span="18" class="mainwrap-has-aside">
        <table-com
          :tableTit="tableTit"
          :tableData="tableData"
          :tbLoading="tbLoading || treeLoading"
          :height="height"
          :pageNo="searchForm.pageNo"
          :pageSize="searchForm.pageSize"
          :showSet="true"
          :selectableFun="handelSelecTable"
          :addRowClassName="setRowClassName"
          @clickSelection="handleClickSelection"
          @dbClick="dbEditClick"
          @lookClick="handleViewClick"
          @setTableClick="showChangeTable = true"
          @sortClick="handleSetSort"
          :hasChangeTable="true"
          listCode="ucenter_function_list"
          @updateTable="getConfigList"
        ></table-com>
        <!-- @handleRowClicked="handleRowClick" -->

        <pagination
          :total="total"
          :defaultPage="searchForm.pageNo"
          @changePageSize="changePageSize"
          @changeCurrentPage="changeCurrentPage"
          v-show="tableData.length && !tbLoading"
        ></pagination>

        <!-- <changeTable
                        :showChangeTable="showChangeTable"
                        :seTableList="seTableList"
                        @closeDialog="showChangeTable = false"
                        @trueClick="handleTrueClick"
                        @defaultClick="handleDefaultClick"
                    ></changeTable> -->

        <!-- <el-drawer
                title="我是标题"
                :visible.sync="drawer"
                :direction="direction"
                :before-close="handleCloseDrawer">
                <span>我来啦!</span>
            </el-drawer> -->
      </el-col>
    </el-row>
  </div>
</template>

<script>
import searchForm from '@/components/search-form'
import CustomTime from '@/components/custom-time'
import pageTitle from '@/components/page-title'
import foldTree from '@/components/fold-tree'
import TableCom from '@/components/table'
import Pagination from '@/components/pagination'
import changeTable from '@/components/change-table'
import operationCom from '@/components/operation'
import lodash from 'lodash'
import { getLocalStorage } from '@/utils/auth'
export default {
  name: 'menuManage',
  components: {
    searchForm,
    CustomTime,
    pageTitle,
    foldTree,
    TableCom,
    Pagination,
    changeTable,
    operationCom,
  },
  data() {
    return {
      isAsideCollapse: true,
      searchForm: {
        pageNo: 1,
        pageSize: 10,
      },
      opTimeType: '',
      opTime: [],
      height: null,
      isCollapse: true,
      isCleanSearch: false,
      isShowAscurrVal: false,
      selectionList: [],
      btnConfigs: [
        {
          type: 'add',
          text: '新增',
          icon: 'el-icon-aliadd',
          handlerType: 'handleAddClick',
          has: 'ucenter_function_add',
        },
        {
          type: 'modify',
          text: '修改',
          icon: 'el-icon-alimodify',
          handlerType: 'handleEditClick',
          has: 'ucenter_function_save',
        },
        {
          type: 'remove',
          text: '删除',
          icon: 'el-icon-aliback',
          code: 'ucenter_function_delete',
          handlerType: 'handleDeleteClick',
        },
        {
          type: 'refresh',
          text: '刷新',
          icon: 'el-icon-alirefresh',
          code: 'ucenter_function_view',
          handlerType: 'getTableList',
        },
      ],
      total: 0,
      tbLoading: false,
      titList: [
        {
          colKey: 'parent',
          prop: 'parentName',
          label: '上级菜单',
        },
        {
          colKey: 'name',
          prop: 'name',
          label: '名称',
        },
        {
          colKey: 'code',
          prop: 'code',
          label: '代码',
        },
        {
          colKey: 'action',
          prop: 'action',
          label: '地址',
        },
        {
          colKey: 'IMG_PATH',
          prop: 'IMG_PATH',
          label: '图标路径',
        },
        {
          colKey: 'orderNo',
          prop: 'orderNo',
          label: '排序号',
        },
      ],
      tableTit: [],
      tableData: [],
      showChangeTable: false,
      seTableList: [],
      activeName: 0,
      treeListData: [],
      defaultIds: [],
      selectNode: null,
      treeLoading: false,
    }
  },
  watch: {
    isCollapse() {
      setTimeout(() => {
        this.getTbHeight()
      }, 0)
    },
    async 'searchForm.nameQueryLike'(val) {
      if (val.trim() == '') {
        let res = await this.getMenuListChild(this.defaultIds[0])
        if (res.code == 0) {
          this.total = res.data.total
          this.searchForm.pageNo = res.data.pageNum
          this.searchForm.pageSize = res.data.pageSize
          res.data.list.forEach((item) => {
            item.parentName = this.selectNode.name
          })
          this.tableData = []
          this.tableData.push(...res.data.list)
        }
        this.tbLoading = false
      }
    },
  },
  computed: {
    allMenuList() {
      return this.$store.getters.allMenuList
    },
  },
  created() {
    this.getMenuList(true)
    this.getConfigList()
  },
  mounted() {
    this.getTbHeight()
  },
  methods: {
    handleClick() {
      this.getMenuList(true)
    },
    getConfigList() {
      this.$getPageList('ucenter_function_list', this.titList).then((data) => {
        this.tableTit = data
      })
    },
    async getMenuList(init = false) {
      this.treeLoading = true
      try {
        let res
        let menuList
        // res = await this.$http.accessManage();
        res = await this.$http.getMenuList({
          projectId: this.allMenuList[this.activeName].id,
        })
        menuList = this.$formatTree(
          res.data[0].subFunction,
          'subFunction',
          false,
          'tree-filebox',
          'tree-file',
          '',
          false,
          true,
          'subFunction'
        )
        this.treeListData = this.formatMenuList(menuList)
        if (init) {
          this.treeListData.length &&
            this.handleClickNode(this.treeListData[0], init)
          this.defaultIds = [this.treeListData[0].id]
        }
        this.treeLoading = false
      } catch (e) {
        this.treeLoading = false
      }
    },
    formatMenuList(menuList) {
      let isArr = typeof menuList.length !== 'undefined'
      menuList = isArr ? menuList : [menuList]
      for (let v of menuList) {
        let tag = true
        const menuBox = []
        if (v.subFunction && v.subFunction.length > 0) {
          for (let v2 of v.subFunction) {
            if (v2.subFunction && v2.subFunction.length > 0) {
              v2 = this.formatMenuList(v2)
            }
            if (v2.type != 4) {
              tag = false
              // break;
            } else {
              menuBox.push(v2)
            }
          }
        }
        v.expandTag = tag
        v.menuBox = menuBox
      }
      return isArr ? menuList : menuList[0]
    },
    getMenuListChild(id) {
      this.tbLoading = true
      return new Promise((resolve, reject) => {
        this.$http
          .getMenuListChild({
            ...this.searchForm,
            projectId: this.allMenuList[this.activeName].id,
            parentId: id,
          })
          .then((res) => resolve(res))
          .catch((err) => reject(err))
      })
    },
    operationHandler(type) {
      this[type]()
    },
    // foldtree
    async getDeptData() {
      let res = await this.$http.getListDeptTree()
      if (res.code == 0) {
        let organizationList = this.$formatTree(
          res.data,
          'listPerson',
          true,
          'tree-file',
          'el-icon-alihead',
          '',
          false,
          true
        )
        organizationList.forEach((item) => {
          item.icon = 'tree-filebox'
        })
        this.treeListData = organizationList
      }
    },
    async handleClickNode(node = this.selectNode, init) {
      let res = await this.getMenuListChild(node.id)
      this.selectNode = node
      this.tbLoading = false
      if (res.code == 0) {
        this.total = res.data.total
        this.searchForm.pageNo = res.data.pageNum
        this.searchForm.pageSize = res.data.pageSize

        res.data.list.forEach((item) => {
          item.parentName = node.name
        })
        this.tableData = []
        this.tableData.push(...res.data.list)
      }
      if (init) {
        this.$route.meta.noLoading = true
      }
    },
    //table
    getTableList() {
      this.tbLoading = true
      this.handleClickNode()
    },
    getTbHeight() {
      setTimeout(async () => {
        try {
          this.height = await this.$formatTableHeight()
        } catch (e) {}
      }, 0)
    },
    handelSelecTable(item) {},
    setRowClassName(row) {
      if (row.isReview == '0') {
        //未读
        return 'unread-row'
      }
      return ''
    },
    //点击行
    // handleRowClick({row}){
    //     this.drawer= true;
    //
    // },
    // handleCloseDrawer(done){
    //     done();
    // },
    //多选复选框
    handleClickSelection(item) {
      this.selectionList = item
    },
    //operation
    //添加
    handleAddClick() {
      this.$router.push({
        name: 'menuManageAdd',
        params: { noCache: true },
      })
    },
    //编辑
    handleEditClick() {
      if (this.selectionList.length == 0) {
        this.$showWarning('请选择一条数据')
      } else if (this.selectionList.length > 1) {
        this.$showWarning('只能选择一条数据')
      } else {
        this.$router.push({
          name: 'menuManageEdit',
          params: { noCache: true, id: this.selectionList[0].id },
        })
      }
    },
    //双击
    dbEditClick({ id }) {
      if (this.$filterBtnShow(['ucenter_dept_edit'])) {
        this.$router.push({
          name: 'deptEdit',
          params: { noCache: true, id },
        })
      } else {
        if (this.$filterBtnShow(['ucenter_dept_view'])) {
          this.$router.push({
            name: 'tableView',
            params: { id },
          })
        }
      }
    },
    //查看
    handleViewClick({ id }) {
      this.$router.push({
        name: 'deptView',
        params: { id },
      })
    },
    //删除
    handleDeleteClick() {
      if (this.selectionList.length == 0) {
        this.$showWarning('请选择要删除的数据')
        return
      }
      this.$confirm('此操作会删除该人员, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          let ids = '',
            idsArr = []
          this.selectionList.forEach((item) => {
            idsArr.push(item.id)
          })
          ids = idsArr.join(',')
          let params = {
            idQueryIn: ids,
          }

          this.$http.ucenterFunctionDel(params).then((res) => {
            if (res.code == 0) {
              this.$showSuccess('删除成功！')
              this.getTableList()
            }
          })
        })
        .catch(() => {})
    },
    //锁定
    handleLockClick() {
      if (this.selectionList.length == 0) {
        this.$showWarning('请选择数据')
        return
      }
      let accounts = '',
        accountsArr = []
      let isError = false
      this.selectionList.forEach((item) => {
        if (!item.account || item.account == '') {
          this.$showWarning('请选择已分配账号的数据')
          isError = true
          return
        }
        if (item.accountStatus == '2') {
          this.$showWarning('请选择未锁定账号的数据')
          isError = true
          return
        }
        if (item.accountStatus == '3') {
          this.$showWarning('请选择未注销账号的数据')
          isError = true
          return
        }
        accountsArr.push(item.account)
      })
      if (!isError) {
        accounts = accountsArr.join(',')
        let params = { accounts }
        this.$confirm('账号将要被锁定, 请确认是否继续？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(() => {
            Api.ucenterPersonFrozen(params).then((res) => {
              if (res.code == 0) {
                this.$showSuccess('锁定成功！')
                this.getTableList()
              }
            })
          })
          .catch(() => {})
      }
    },
    //解锁
    handleUnLockClick() {
      if (this.selectionList.length == 0) {
        this.$showWarning('请选择数据')
        return
      }
      let accounts = '',
        accountsArr = []
      let isError = false
      this.selectionList.forEach((item) => {
        if (!item.account || item.account == '') {
          this.$showWarning('请选择已分配账号的数据')
          isError = true
          return
        }
        if (item.accountStatus == '1') {
          this.$showWarning('请选择已锁定账号的数据')
          isError = true
          return
        }
        if (item.accountStatus == '3') {
          this.$showWarning('请选择未注销账号的数据')
          isError = true
          return
        }
        accountsArr.push(item.account)
      })
      if (!isError) {
        accounts = accountsArr.join(',')
        let params = { accounts }
        Api.ucenterPersonUnFrozen(params)
          .then((res) => {
            if (res.code == 0) {
              this.$showSuccess('解锁成功！')
              this.getTableList()
            }
          })
          .catch(() => {})
      }
    },
    //注销
    handleDestroyClick() {
      if (this.selectionList.length == 0) {
        this.$showWarning('请选择数据')
        return
      }
      let accounts = '',
        accountsArr = []
      let isError = false
      this.selectionList.forEach((item) => {
        if (!item.account) {
          this.$showWarning('请选择已分配账号的数据')
          isError = true
          return
        }
        if (item.accountStatus == '3') {
          this.$showWarning('请选择未注销账号的数据')
          isError = true
          return
        }
        accountsArr.push(item.account)
      })
      if (!isError) {
        accounts = accountsArr.join(',')
        let params = { accounts }
        this.$confirm('账号将要被注销, 请确认是否继续？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(() => {
            Api.ucenterPersonCancel(params).then((res) => {
              if (res.code == 0) {
                this.$showSuccess('注销成功！')
                this.getTableList()
              }
            })
          })
          .catch(() => {})
      }
    },
    //重置密码
    handleResetPwdClick() {
      if (this.selectionList.length == 0) {
        this.$showWarning('请选择一条数据')
        return
      }
      if (this.selectionList.length > 1) {
        this.$showWarning('只能选择一条数据')
        return
      }
      let { account, accountStatus } = this.selectionList[0]
      if (!account) {
        this.$showError('当前记录未分配账号，请刷新后重试')
        return
      }
      if (accountStatus != '1') {
        this.$showError('当前记录账号状态异常，请刷新后重试')
        return
      }
      this.$confirm(
        '账号' + account + '的密码将要被重置，请确认是否继续？',
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }
      ).then(() => {
        Api.ucenterPersonResetPassword({ account: account })
          .then((res) => {
            if (res.code == 0) {
              this.$showSuccess('密码重置成功！')
              this.getTableList()
            }
          })
          .catch(() => {})
      })
    },
    //分页操作
    changePageSize({ pageSize }) {
      this.searchForm.pageSize = pageSize
      this.getTableList()
    },
    changeCurrentPage({ currentPage }) {
      this.searchForm.pageNo = currentPage
      this.getTableList()
    },
    reloadTableList() {
      this.changeCurrentPage({currentPage: 1})
    },
    //排序
    handleSetSort({ tableTit, orderBy }) {
      this.tableTit = tableTit
      this.searchForm.orderBy = orderBy
    },
    //配置表格
    handleTrueClick(list) {
      this.$setTrueTable(list).then((res) => {
        this.updateTable(res)
      })
    },
    handleDefaultClick() {
      this.$setDefaultTable('ucenter_person_list').then((res) => {
        this.updateTable(res)
      })
    },
    updateTable(res) {
      this.$showSuccess(res.message)
      this.showChangeTable = false
    },
  },
}
</script>

<style lang="scss" scoped>
.menuManage {
  height: 100%;
  /deep/.el-tabs__header {
    margin: -10px 18px 0;
  }
  .asideormain {
    height: calc(100% - 80px);
    padding-top: 12px;
  }
  .aside-con .bd {
    height: calc(100% - 30px);
  }
}

.main {
  padding: 0 10px;
  height: calc(100% - 114px);
}
</style>
