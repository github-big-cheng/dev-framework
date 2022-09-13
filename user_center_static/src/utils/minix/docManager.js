/*
 * @Author: your name
 * @Date: 2021-03-15 11:22:35
 * @LastEditTime: 2021-08-19 16:25:48
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \zfzd_oa\src\utils\minix\docManager.js
 * 此文件为党务、行政收发文的催发事件提供
 */
import Api from "@/api/api";
import lodash from "lodash";
export default {
  data () {
    return {
      bizId: null,
      urgingVisible: false,
      urgingContent: [],
      msgTypeList: [],
    }
  },
  mounted () {
    this._getMsgTypeList();
  },
  // 为催办提供
  methods: {
    _checkStatus(status) {
      let checkObj = ['21004-20', '21004-30', '21004-40', '21004-50', '21004-60', '21004-110',
      '21003-20', '21003-30', '21003-40', '21003-50', '21003-60', '21003-110'];
      let hasStatus = checkObj.includes(status);
      return hasStatus;
    },
    _handleShowUrging() {
      if (this.selectionList.length == 0) {
        this.$showWarning("请选择一条数据！");
      } else if (this.selectionList.length > 1) {
        this.$showWarning("只能选择一条数据！");
      } else if (!this._checkStatus(this.selectionList[0].status)) {
        this.$showWarning("当前数据不可催办！");
      } else {
        
        this.bizId = this.selectionList[0].id;
        this.$nextTick(() => {
          this.urgingVisible = true;
          this.urgingContent = this.selectionList[0].urgingContent;
        })
      }
    },
    _getMsgTypeList() {
      Api.getUcenterCodeCombox({type: "10001-10042"}).then((res) => {
        if (res.code == 0) {
          this.msgTypeList = res.data.list;
        }
      });
    },
    _handleUrgingTrueClick(val) {
      //催办
      let urgingForm = lodash.cloneDeep(val);
      let params = {
        id: this.selectionList[0].id,
        personIds: urgingForm.personIds,
        content: urgingForm.content,
        remindType: urgingForm.msgTypes
      };
      Api[
        this.isReceive ? 'oadocoareceiveUrging' : 'oadocoasendUrging'
      ](params, this.isDw ? 20 : 10).then(
        (res) => {
          if (res.code == 0) {
            this.$showSuccess(res.message);
            this.urgingVisible = false;
          }
        }
      );
    },
    _handleUrgingCancelClick() {
      this.urgingVisible = false;
    },
  }
}
