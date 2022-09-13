export default {
    gainHeight() {
        this.$nextTick(() => {
            this.$formatTableHeight().then((data) => {
                this.maxHeight = data;
            });
        });
    },
    cellStyle(row, column, rowIndex, columnIndex) {
        if (
            row.row.statusName == "驳回发起人" ||
            (row.row.statusName == "驳回" && row.column.label == "处理结果")
        ) {
            return { color: "#ff0000" };
        }
        return this.addColumnStyle(row, column, rowIndex, columnIndex);
    },
    formatIndex(e) {
        return (this.page.No - 1) * this.page.Size + 1 + e.$index;
    },
    release() {
        try {
            const table = this.drag.table;
            if (table) {
                for (let j = 0; j < table.rows[0].cells.length; j++) {
                    table.rows[0].cells[j].onmousedown = null;
                    table.rows[0].cells[j].onmouseup = null;
                    table.rows[0].cells[j].onmousemove = null;
                }
                this.drag.tableParent.onmouseenter = null;
            }
        } catch (e) {
            console.log("release:e", e);
        }
    },
    initDrag() {
        const _this = this;
        let tTD;
        let tableClass;
        let table;
        const isLoad = document.getElementsByClassName(this.tableClass).length;
        if (!isLoad) {
            setTimeout(() => {
                this.initDrag();
            }, 100);
            return;
        }
        try {
            tableClass = `.${this.tableClass} .el-table__header`;
            table = document.querySelector(tableClass);
        } catch (e) {
            console.log('initDrag:e', e);
        }
        if (!table) {
            setTimeout(() => {
                this.initDrag();
            }, 100);
            return;
        }
        this.drag.table = table;
        this.drag.tableParent = document.querySelector(`.${this.tableClass}`);
        this.drag.tableParent.onmouseenter = (event) => {
            const t = document.querySelector(tableClass);
            if (t !== table) {
                this.release();
                this.initDrag();
            }
        };
        const max = table.rows[0].cells.length - 2;
        for (let j = 0; j < max; j++) {
            table.rows[0].cells[j].onmouseover = function (event) {
                let list = [this];
                let child = this.getElementsByClassName("is-sort")[0];
                if (child) {
                    list.push(child);
                }
                if (event.offsetX > this.offsetWidth - 20) {
                    for (const _el of list) {
                        _el.style.cursor = "col-resize";
                    }
                } else {
                    for (const _el of list) {
                        _el.style.cursor = "pointer";
                    }
                }
            };
            table.rows[0].cells[j].onmousedown = function (event) {
                tTD = this;
                if (event.offsetX > tTD.offsetWidth - 20) {
                    tTD.mouseDown = true;
                    tTD.oldX = event.x;
                    tTD.oldWidth = tTD.offsetWidth;
                    _this.drag.index = j;
                }
            };
            table.rows[0].cells[j].onmouseup = function (event) {
                if (tTD == undefined) {
                    tTD = this;
                }
                tTD.mouseDown = false;
                tTD.style.cursor = "pointer";
                const tableClass2 = `.${this.tableClass} .el-table__body`;
                const table2 = document.querySelector(tableClass);
            };
            table.rows[0].cells[j].onmousemove = function (event) {
                let index = _this.drag.index;
                let list = [this];
                let child = this.getElementsByClassName("is-sort")[0];
                if (child) {
                    list.push(child);
                }
                if (event.offsetX > this.offsetWidth - 20) {
                    for (const _el of list) {
                        _el.style.cursor = "col-resize";
                    }
                } else {
                    for (const _el of list) {
                        _el.style.cursor = "pointer";
                    }
                }
                if (tTD == undefined) {
                    tTD = this;
                    index = j;
                }

                if (tTD.mouseDown != null && tTD.mouseDown == true) {
                    tTD.style.cursor = "pointer";
                    if (tTD.oldWidth + (event.x - tTD.oldX) > 0) {
                        let width = tTD.oldWidth + (event.x - tTD.oldX);
                        width = width > 30 ? width : 30;
                        tTD.width = width;
                    }
                    tTD.style.cursor = "col-resize";
                    _this.$refs.multipleTable.columns[index].width = tTD.width * 1;
                    _this.$refs.multipleTable.doLayout();
                }
            };
        }
    },
    handleRowClick(row, column, event) {
        this.$refs.multipleTable.toggleRowSelection(row);
        let selections = this.$refs.multipleTable.selection;
        this.highlightCurrentRow = selections.some((item) => item[this.currentRowKey] == row[this.currentRowKey]);

        this.$emit("handleRowClicked", { row, column, event });
    },
    handleCurrentChange(val) {
        this.$emit("currentRowData", val);
    },
    handleDbClick(row) {
        const { hasLook } = this
        const state = Array.isArray(hasLook) ? hasLook.includes(true) : hasLook
        if (state) {
            this.$emit("dbClick", row);
        }
    },
    handleSelectionChange(val) {
        this.multipleSelection = val;
        this.$emit("clickSelection", val);
    },

    handleLookClick(e, row) {
        e.stopPropagation();
        this.$emit("lookClick", row);
    },
    handleSortClick(orderBy, asc) {
        if (asc == "") {
            asc = "asc";
        } else if (asc == "asc") {
            asc = "desc";
        } else if (asc == "desc") {
            asc = "";
        }
        let tableTit = JSON.parse(JSON.stringify(this.tableTit));
        tableTit.forEach((item) => {
            item.asc = "";
            if (orderBy == item.prop) {
                item.asc = asc;
            }
        });
        this.$emit("sortClick", { tableTit, orderBy: orderBy + asc });
    },
    tableRowClassName({ row, rowIndex }) {
        let addClass = this.addRowClassName(row, rowIndex);
        if (this.iSelection && this.selectRow.includes(row[this.currentRowKey])) {
            return "current-row " + (typeof addClass == "string" ? addClass : "");
        }

        return this.addRowClassName(row, rowIndex);
    },
    columnClassName(row, column, rowIndex, columnIndex) {
        return this.addColumnClassName(row, column, rowIndex, columnIndex);
    },
    handelSelecTable(row, rowIndex) {
        if (this.isCheckDisable) {
            this.selectableFun(row, rowIndex);
        } else {
            return true;
        }
    },
    //进入时显示提示框
    showTooltip(row, index) {
        //需要显示浮框的条件
        return this.addShowTooltip(row, index);
    },
    //退出时隐藏提示框
    hideTooltip(row, index) {
        return this.addHideTooltip(row, index);
    },
    getSummaries(param) {
        return this.tableSumFmt(param)
    },
    // 合并单元格
    handleSpanMethod({ row, column, rowIndex, columnIndex }) {
        return this.spanMethod({ row, column, rowIndex, columnIndex })
    }
}
