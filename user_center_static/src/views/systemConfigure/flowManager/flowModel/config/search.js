/*
 * @Author: your name
 * @Date: 2021-08-12 11:24:44
 * @LastEditTime: 2021-08-13 10:38:07
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \do_ucenter_fe\src\views\systemConfigure\flowManager\flowModel\config\search.js
 */
export default {
    data () {
        return {
            searchList: {
                easy: {
                    placeholder: '请输入模型名称',
                    prop: 'titleQueryLike'
                },
                list: [
                    {
                        type: 'select',
                        label: '流程分类',
                        prop: 'categoryQueryLike',
                        children: [
                            {
                                value: '常用流程',
                                name: '常用流程'
                            },
                            {
                                value: '办公流程',
                                name: '办公流程'
                            }
                        ]
                    },
                    {
                        type: 'input',
                        label: '关键字',
                        prop: 'keyQuery'
                    }
                ]
            }
        }
    }
}