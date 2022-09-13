<template>
  <div class="main-content-wrap inner-maincon" ref="look_view">
    <div class="form-box">
      <form-com
        ref="ruleFormBox"
        :config="formConfigs"
        :isformBtn="true"
        :formBtn="formBtns"
        @submit="submit"
      >
      </form-com>
    </div>

    <pageTitle title="选择人员" class="htitle"></pageTitle>
    <div class="personnel-total">
      <el-button @click="selectPersonClick()">选择人员</el-button>
      <span
        >本{{ typeName }}总共{{ personSum }}人，其中原{{ typeName
        }}{{ personOldSum }}人，移除{{ personRemoveSum }}人，新增{{
          personAddSum
        }}人。</span
      >
      <span class="note"
        ><b class="n-add"><i></i>新增</b><b class="n-del"><i></i>移除</b></span
      >
    </div>
    <div>
      <el-table
        v-loading="personTableLoading"
        element-loading-text="加载中..."
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(255, 255, 255, 1)"
        id="personTable"
        :key="personTableRowKey"
        :data="personTableData"
        :stripe="stripe"
        border
        :max-height="height"
        ref="multipleTable"
        :row-class-name="tableRowClassName"
        :class="[
          'table-personnel',
          personTableData.length && 'table-personnel-has',
        ]"
      >
        <el-table-column label="人员" minWidth="30">
          <template slot-scope="scope">
            <span>{{ scope.row.personName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="顺序号" minWidth="30">
          <template slot-scope="scope">
            <span>{{ scope.row.orderNo }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" minWidth="30">
          <template slot-scope="scope">
            <el-button
              icon="iconfont"
              v-if="!scope.row.remove || scope.row.remove == null"
              @click.native="
                handlePersonOperate('remove', scope.$index, scope.row)
              "
              >移除
            </el-button>
            <el-button
              icon="iconfont"
              v-if="scope.row.remove"
              @click.native="
                handlePersonOperate('back', scope.$index, scope.row)
              "
              >还原
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!-- 人员多选 -->
    <personChoice
      title="选择人员"
      :tabList="['deptPerson']"
      :defaultIds="personIds"
      :showComponent="showPersonChoice"
      @cancelClick="showPersonChoice = false"
      @trueClick="handlePersonChoiceClick"
    >
    </personChoice>
  </div>
</template>

<script>
import pageTitle from '@/components/page-title'
import formCom from '@/components/form-com'
import personChoice from '@/components/select-component/choice'
import Sortable from 'sortablejs'

export default {
  name: 'deptAdjustmentAdd',
  props: {
    stripe: {
      type: Boolean,
      default: () => false,
    },
    isNumsShow: {
      type: Boolean,
      default: () => true,
    },
    isNums: {
      type: Boolean,
      default: () => true,
    },
  },
  components: {
    pageTitle,
    formCom,
    personChoice,
  },
  data() {
    return {
      isDisable: false,
      selectComIds: '',
      selectComNames: '',
      height: null,
      type: '',
      typeName: '部门',
      showOrgSingle: false,
      personTableRowKey: 0,
      personTableData: [],
      personTableLoading: false,
      showPersonChoice: false,
      personList: [],
      personIds: '',
      personSum: 0,
      personAddSum: 0,
      personRemoveSum: 0,
      personOldSum: 0,
      defaultIds: '',
      ruleForm: {
        deptId: '',
        personModel: {},
      },
      formConfigs: [
        {
          type: 'selectCom',
          label: '部门名称',
          prop: 'deptId',
          prop2: 'deptName',
          value: '',
          names: '',
          title: '部门名称',
          tabList: ['dept'],
          rules: {
            require: true,
          },
          class: 'single input-w360',
          changeCB: this.selectComUpdate,
        },
      ],
      formBtns: [
        {
          btnText: '取消',
          handlerType: 'cancelClick',
        },
        {
          type: 'primary',
          btnLoading: false,
          btnText: '保存',
          handlerType: 'submitForm',
        },
      ],
      seachDeptPerson: false,
    }
  },
  watch: {
    personTableData: {
      handler(val) {
        val.forEach((item, i) => {
          if (item.remove) {
            item.orderNo = ''
          } else {
            item.orderNo = (i + 1) * 10
          }
        })
      },
      deep: true,
    },
  },
  created() {
    let { params } = this.$route;
    this.ruleForm.deptId = params.deptId
    this.ruleForm.deptName = params.deptName
    this.$nextTick(() => {
      this.rowDrop('personTable')
    })
  },
  mounted(){
    if(this.ruleForm.deptId) {
      this.$refs.ruleFormBox.setFormValue('deptId',this.ruleForm.deptId);
      this.$refs.ruleFormBox.setFormValue('deptName',this.ruleForm.deptName);
    }
  },
  computed: {
    tabList() {
      return ['deptPerson']
    },
  },
  activated() {},
  methods: {
    selectComUpdate(data, value) {
      this.getTableList()
    },
    submit({ handlerType }) {
      this[handlerType]()
    },
    cancelClick() {
      this.goBack(this.$route, true)
    },
    //行拖拽
    rowDrop(attr) {
      const tbody = document.querySelector(
        `#${attr} .el-table__body-wrapper tbody`
      )
      const _this = this
      Sortable.create(tbody, {
        animation: 1000,
        filter: '.removeRowstyle', //指定不可拖动的类名（el-table中可通过row-class-name设置行的class）
        onEnd({ newIndex, oldIndex }) {
          attr == ++_this.personTableRowKey
          let scrollTop = _this.$refs.look_view.scrollTop
          const currRow = _this.personList.splice(oldIndex, 1)[0]
          _this.personList.splice(newIndex, 0, currRow)
          _this.sortData(attr)
          _this.$nextTick(() => {
            if (!_this.personTableData[newIndex].remove) {
              const curData = _this.personTableData.splice(oldIndex, 1)[0]
              _this.personTableData.splice(newIndex, 0, curData)
            }
            _this.$refs.look_view.scrollTop = scrollTop
            _this.rowDrop(attr)
          })
        },
      })
    },
    //重新排序
    sortData(attr) {
      let personIds = []
      this.personTableData.forEach((item, i) => {
        personIds.push(item.personId)
      })
      this.personIds = personIds.join()
    },
    async getTableList() {
      let validate = await this.$refs.ruleFormBox.getFormAndValidate()
      if (!validate.status) {
        this.personIds = ''
        this.personTableData = []
        this.personSum = 0
        this.personOldSum = 0
        this.personAddSum = 0
        this.personRemoveSum = 0
        return
      }
      this.personTableLoading = true
      this.$http
        .getDeptAdjustmentList({
          deptId: validate.data.deptId,
          pageNo: 1,
          pageSize: 100000,
        })
        .then((res) => {
          if (res.code == 0) {
            this.personIds = ''
            this.personList = []
            this.personTableData = []
            let { list } = res.data
            list.sort(function(a, b) {
              return a.orderNo - b.orderNo
            })
            res.data.list.forEach((item, i) => {
              item.remove = false
              this.personTableData.push(item)
              this.personList.push({
                personName: item.personName,
                id: item.personId,
              })
              this.personIds =
                this.personIds == ''
                  ? item.personId + ''
                  : this.personIds + ',' + item.personId
            })
            this.personSum = res.data.total
            this.personOldSum = res.data.total
            this.personAddSum = 0
            this.personRemoveSum = 0
          }
          this.personTableLoading = false
        })
        .catch((err) => {
          this.personTableLoading = false
        })
    },
    tableRowClassName(row) {
      if (row.row.remove == null || !row.row.remove) {
        if (row.row.id == null || row.row.id == '') {
          return 'addRowstyle'
        } else {
          return 'oldRowstyle'
        }
      } else {
        return 'removeRowstyle'
      }
    },
    async selectPersonClick() {
      let {
        data: { deptId },
      } = await this.$refs.ruleFormBox.getFormAndValidate()
      this.ruleForm.deptId = deptId
      if (deptId) {
        this.showPersonChoice = true
      } 
      // else {
      //   this.$showWarning('请选择' + this.typeName)
      // }
    },
    handlePersonChoiceClick(list) {
      //选择人员，按照选择人员顺序加载数据
      this.personIds = ''
      this.personList = list
      this.showPersonChoice = false
      if (list != null && list.length > 0) {
        this.personAddSum = 0
        this.personList.forEach((item) => {
          this.personIds =
            this.personIds == ''
              ? item.personId + ''
              : this.personIds + ',' + item.personId
          let isAdd = true
          this.personTableData.forEach((tableItem) => {
            if (item.id == tableItem.personId) {
              isAdd = false
              if (tableItem.remove) {
                //移除的被还原
                tableItem.remove = false
              }
            }
          })
          if (isAdd) {
            //新加人员
            let personData = {
              id: '',
              remove: false,
              personId: item.id,
              personName: item.name,
              orderNo: orderNo,
            }
            this.personTableData.push(personData)
            this.personAddSum = this.personAddSum + 1
          }
        })
        //原人员被移除（1、新增人员直接删除，2、原人员被移除）
        this.personRemoveSum = 0
        let newPersonTableData = []
        this.personTableData.forEach((tableItem) => {
          let isRemove = true
          this.personList.forEach((item) => {
            if (tableItem.personId == item.id) {
              isRemove = false
            }
          })
          if (isRemove) {
            //2、原人员被移除
            if (tableItem.id != null && tableItem.id != '') {
              tableItem.remove = true
              this.personRemoveSum = this.personRemoveSum + 1
              newPersonTableData.push(tableItem)
            } else {
              //1、新增人员直接删除
            }
          } else {
            //新增人员、原人员
            newPersonTableData.push(tableItem)
          }
        })
        this.personTableData = newPersonTableData
        this.getNewTebleList()
        //重新排顺序号
        let orderNo = 0
        this.personTableData.forEach((tableItem) => {
          orderNo = orderNo + 10
          tableItem.orderNo = orderNo
        })
        this.personSum =
          this.personOldSum + this.personAddSum - this.personRemoveSum
      }
    },
    //移除、还原
    handlePersonOperate(type, index, row) {
      //移除
      if (type == 'remove') {
        //原数据标记删除
        if (row.id != null && row.id != '') {
          row.remove = true
          this.personRemoveSum = this.personRemoveSum + 1
        } else {
          //新数据直接移除
          let newData = []
          this.personTableData.forEach((item) => {
            if (item.personId != row.personId) {
              newData.push(item)
            }
          })
          this.personTableData = newData
          this.personAddSum = this.personAddSum - 1
        }
      } else if (type == 'back') {
        //还原
        row.remove = false
        this.personRemoveSum = this.personRemoveSum - 1
      }
      this.personList = []
      this.personIds = ''
      this.personTableData.forEach((item) => {
        if (!item.remove) {
          this.personList.push({
            personName: item.personName,
            id: item.personId,
          })
          this.personIds =
            this.personIds == ''
              ? item.personId + ''
              : this.personIds + ',' + item.personId
        }
      })
      this.personSum =
        this.personOldSum - this.personRemoveSum + this.personAddSum
      this.getNewTebleList()
    },
    getNewTebleList() {
      //排序：原数据-->新数据-->被移除的数据
      let newPersonTableData = []
      let removePersonTableData = []
      this.personTableData.forEach((item) => {
        if (item.remove) {
          removePersonTableData.push(item)
        } else {
          newPersonTableData.push(item)
        }
      })
      if (newPersonTableData.length > 0) {
        //按照人员选择重新排序
        let newData = []
        this.personList.forEach((item) => {
          newPersonTableData.forEach((newItem) => {
            if (newItem.personId == item.id) {
              newData.push(newItem)
            }
          })
        })
        newPersonTableData = newData
      }
      this.personTableData = newPersonTableData.concat(removePersonTableData)
    },
    async submitForm() {
      let {
        data: { deptId },
      } = await this.$refs.ruleFormBox.getFormAndValidate()
      this.ruleForm.deptId = deptId
      if (!this.ruleForm.deptId) {
        this.$showWarning('请选择' + this.typeName + '名称！')
        return
      }
      let validate = await this.$refs.ruleFormBox.getFormAndValidate()

      if (!validate.status) {
        this.$showWarning('请选择' + this.typeName + '名称！')
        return
      }

      if (this.formBtns[1].btnLoading) return
      this.formBtns[1].btnLoading = true
      this.ruleForm.type = 0
      let personModel = {}
      this.personTableData.forEach((item) => {
        if (!item.remove) {
          personModel[item.personId] = item.orderNo + ''
        }
      })
      this.ruleForm.personModel = JSON.stringify(personModel)
      this.$http
        .getUcenterDeptpersonAdd(this.ruleForm)
        .then((res) => {
          if (res.code == 0) {
            this.$showSuccess(res.message)
            this.goBack(this.$route, true)
          }
          this.formBtns[1].btnLoading = false
        })
        .catch((err) => {
          this.formBtns[1].btnLoading = false
        })
    },
  },
}
</script>

<style lang="scss" scoped>
.form-box,.title-box{padding: 0 10px;}
/deep/.el-form-item__label {
  width: auto !important;
  min-width: auto !important;
}

/deep/ .el-table.table-personnel {
  width: 60%;
  margin-left: 10px;
  &::before {
    height: 0;
  }
  border-bottom: 1px solid #eee;
  &.table-personnel-has {
    border-bottom: 0 none;
  }
  th,
  td {
    padding: 0;
    height: 36px;
    line-height: 36px;
    text-align: center;
    // border-right: 1px solid #eee;
    // border-left: 1px solid #eee;
  }

  th {
    background-color: #f4f4f4;
    box-shadow: 0px 1px 0px 0px #d9e2eb;
  }

  td {
    .el-button {
      padding: 0;
      color: #118af7;
      border: 0 none;
      background: none;

      &:first-child {
        margin-left: 0;
      }

      &:hover {
        background: 0 none;
      }
    }
  }
}

.personnel-total {
  display: flex;
  align-items: center;
  padding-left: 10px;
  padding-bottom: 10px;
  .el-button {
    margin-right: 10px;
    padding: 0 15px;
  }

  .note {
    b {
      margin-left: 20px;
      color: #999;
      font-weight: normal;

      i {
        display: inline-block;
        width: 16px;
        height: 12px;
        margin-right: 5px;
        vertical-align: -2px;
        border: 1px solid #2cc43c;
        background: #eefaf0;
      }

      &.n-add {
        margin-left: 10px;
      }

      &.n-del i {
        background: #fff3f1;
        border-color: #ff6b49;
      }
    }
  }
}

/deep/ .el-table {
  .addRowstyle {
    background: rgba(44, 196, 60, 0.08);
  }
  .oldRowstyle {
    background: rgb(255, 255, 255);
  }
  .removeRowstyle {
    background: rgba(255, 107, 73, 0.08);
  }
}

/deep/ .el-table__row {
  cursor: move;
}

@media screen and (min-width: 1501px) {
  /deep/ .el-table.table-personnel {
    th,
    td {
      height: 36px;
      line-height: 36px;
    }
  }
  .personnel-total .note b i {
    width: 16px;
    height: 12px;
  }
}
</style>
