<template>
    <vxe-grid
      class="editable-box"
      border
      stripe
      resizable
      showHeaderOverflow
      showOverflow
      keep-source
      ref="xGrid"
      highlight-hover-row
      highlight-current-row
      :height="height"
      :toolbarConfig="toolbarConfig"
      :columns="tableColumn"
      :data="tableData"
      :edit-rules="validRule"
      :edit-config="editConfig"
      :pagerConfig="pagerConfig"
      @page-change="handlePageChange"
      @toolbar-button-click="toolbarButtonClickEvent"
    >
      <!-- :pagerConfig="{perfect: true,pageSize: 15}" -->
    <!-- :loading="loading" -->
      <!-- @cell-click="cellClickEvent"
        @checkbox-change="checkboxChangeEvent"
      @checkbox-all="changeAllEvent"
       -->
      <template #deptTableAdd>
        <slot name="deptTableAdd"></slot>
      </template>
    </vxe-grid>
</template>
<script>
  export default {
    name: 'etableCom',
    props: {
      slotName:{
        type: String,
        default: () => "",
      },
      height: {
        type: [Number, String],
        default: () => null,
      },
      toolbarConfig: {
        type: Object,
        default: () => {
          return {
            buttons: [
              { code: 'addCell', name: '新增' },
              { code: 'editCell', name: '修改' },
              { code: 'deleteCell', name: '删除' },
            ]
          }
        }
      },
      tableColumn:{
        type: Array,
        default: () => []
      },
      tableData:{
        type: Array,
        default: () => []
      },
      validRule:{
        type: Object,
        default: () => {}
      },
      editConfig: {
        type: Object,
        default: () => {
          return {
            trigger: 'click', 
            mode: 'row', 
            showStatus: true,
          }
        }
      },
      pagerConfig: {
        type: Object,
        default: () => {
          return {
            total: 0,
            currentPage: 1,
            pageSize: 10,
            pageSizes: [10, 20, 50, 100, 200, 500]
          }
        }
      }
    },
    methods: {
      toolbarButtonClickEvent ({ code }) {
        const $grid = this.$refs.xGrid
        switch (code) {
          case 'addCell':
            $grid.insert({
              name: 'xxx'
            })
            break
          case 'editCell':
            setTimeout(() => {
              const { insertRecords, removeRecords, updateRecords } = $grid.getRecordset()
              this.$XModal.message({ content: `新增 ${insertRecords.length} 条，删除 ${removeRecords.length} 条，更新 ${updateRecords.length} 条`, status: 'success' })
              this.loadData()
            }, 100)
            break
          case 'deleteCell':
            $grid.removeCheckboxRow({
              name: 'xxx'
            })
            break
        }
      },
      handlePageChange ({ currentPage, pageSize }) {
        this.pagerConfig.currentPage = currentPage
        this.pagerConfig.pageSize = pageSize
        // this.findList()
      },
    }
  }
</script>

<style lang="scss" scoped>
  @import "@/styles/editable.scss";
</style>