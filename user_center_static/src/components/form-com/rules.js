/*
 * @Author: your name
 * @Date: 2021-05-17 10:33:38
 * @LastEditTime: 2021-07-06 14:24:59
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \fe_framework_template\src\components\form-com\rules.js
 */

import { create } from "sortablejs";
import { getLocalStorage } from '@/utils/auth';

/*
 * @Author: your name
 * @Date: 2021-05-17 09:18:50
 * @LastEditTime: 2021-05-17 09:44:21
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \fe_framework_template\src\components\form-com\rules.js
 */
// const regMap = new Map();
// regMap.set('age', (value) => {
//     return Number.isInteger(Number(value))
// })
// regMap.set('email', (value) => {
//     const reg = /^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$/;
//     return reg.test(value);
// })
// regMap.set('mobilePhone', (value) => {
//     const reg = /^1(3[0-9]|4[5,7]|5[0,1,2,3,5,6,7,8,9]|6[2,5,6,7]|7[0,1,7,8]|8[0-9]|9[1,8,9])\d{8}$/;
//     return reg.test(value);
// })
// regMap.set('telephone', (value) => {
//     const reg = /^1(3[0-9]|4[5,7]|5[0,1,2,3,5,6,7,8,9]|6[2,5,6,7]|7[0,1,7,8]|8[0-9]|9[1,8,9])\d{8}$/;
//     return reg.test(value);
// })
let rules = (rules) => {
    
    return {
        //年龄
        age: (rule, value, callback) => {
            if (!value && rule.required) {
                rules[rule.field].warn = '年龄不能为空';
                rules[rule.field].message = '年龄不能为空';
                return callback(new Error('年龄不能为空'));
            }
            
            if (!Number.isInteger(Number(value))) {
                rules[rule.field].warn = '年龄必须是数值类型';
                rules[rule.field].message = '年龄必须是数值类型';
                callback(new Error('年龄必须是数值类型'));
            } else {
                rules[rule.field].warn = null;
                callback();
            }
        },
        //邮箱
        email: (rule, value, callback) => {
            if (!value && rule.required) {
                rules[rule.field].warn = '邮箱不能为空';
                rules[rule.field].message = '邮箱不能为空';
                return callback(new Error('邮箱不能为空'));
            } else {
                // const reg = /^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$/;
                const reg = /\w+([-+.]\w+)*@\w+([-.]\w+)*\.[a-z]+([-.][a-z]+)*$/;
                const email = reg.test(value);
                if (!email && value) {
                    rules[rule.field].warn = '邮箱格式如:admin@163.com';
                    rules[rule.field].message = '邮箱格式如:admin@163.com';
                    callback(new Error('邮箱格式如:admin@163.com'));
                } else {
                    rules[rule.field].warn = null;
                    callback();
                }
            }
        },
        //手机号码
        mobilePhone: (rule, value, callback) => {
            if (!value && rule.required) {
                rules[rule.field].warn = '手机号码不能为空';
                rules[rule.field].message = '手机号码不能为空';
                return callback(new Error('手机号码不能为空'));
            } else {
                // const reg = /^1(3[0-9]|4[5,7]|5[0,1,2,3,5,6,7,8,9]|6[2,5,6,7]|7[0,1,7,8]|8[0-9]|9[1,8,9])\d{8}$/;
                const reg = /(^\d{3}-\d{8}$)|(^\d{4}-\d{7,8}$)|(^\d{11}$)/;
                const phone = reg.test(value);
                if (!phone && value) {
                    rules[rule.field].warn = '手机号码格式错误';
                    rules[rule.field].message = '手机号码格式错误';
                    callback(new Error('手机号码格式错误'));
                } else {
                    rules[rule.field].warn = null;
                    callback();
                }
            }
        },
        //座机号码
        telephone: (rule, value, callback) => {
            if (!value && rule.required) {
                rules[rule.field].warn = '电话号码不能为空';
                rules[rule.field].message = '电话号码不能为空'
                return callback(new Error('电话号码不能为空'));
            } else {
                // const reg = /^0\d{2,3}-?\d{7,8}$/;
                // const reg2 = /^1(3[0-9]|4[5,7]|5[0,1,2,3,5,6,7,8,9]|6[2,5,6,7]|7[0,1,7,8]|8[0-9]|9[1,8,9])\d{8}$/;
                const reg = /(^\d{3}-\d{8}$)|(^\d{4}-\d{7,8}$)|(^\d{11}$)/;
                const telephone = reg.test(value);
                if (!telephone && value) {
                    rules[rule.field].warn = '电话号码格式错误';
                    rules[rule.field].message = '电话号码格式错误'
                    callback(new Error('电话号码格式错误'));
                } else {
                    rules[rule.field].warn = null;
                    callback();
                }
            }
        },
        //网址
        website: (rule, value, callback) => {
            if (!value && rule.required) {
                rules[rule.field].warn = '网址不能为空';
                rules[rule.field].message = '网址不能为空';
                return callback(new Error('网址不能为空'));
            } else {
                // let strRegex = '^((https|http|ftp|rtsp|mms)?://)'
                //     + '?(([0-9a-z_!~*\'().&amp;=+$%-]+: )?[0-9a-z_!~*\'().&amp;=+$%-]+@)?' //ftp的user@
                //     + '(([0-9]{1,3}.){3}[0-9]{1,3}' // IP形式的URL- 199.194.52.184
                //     + '|' // 允许IP和DOMAIN（域名）
                //     + '([0-9a-z_!~*\'()-]+.)*' // 域名- www.
                //     + '([0-9a-z][0-9a-z-]{0,61})?[0-9a-z].' // 二级域名
                //     + '[a-z]{2,6})' // first level domain- .com or .museum
                //     + '(:[0-9]{1,4})?' // 端口- :80
                //     + '((/?)|' // a slash isn't required if there is no file name
                //     + '(/[0-9a-z_!~*\'().;?:@&amp;=+$,%#-]+)+/?)$';
                // let reg = new RegExp(strRegex);
                const reg = /^http:\/\/([\w-]+(\.[\w-\/]+)+)+$/
                const website = reg.test(value);
                if (!website && value) {
                    rules[rule.field].warn = '网址格式错误';
                    rules[rule.field].message = '网址格式错误';
                    callback(new Error('网址格式错误'));
                } else {
                    rules[rule.field].warn = null;
                    callback();
                }
            }
        },
        //传真
        fax: (rule, value, callback) => {
            if (!value && rule.required) {
                rules[rule.field].warn = '传真不能为空';
                rules[rule.field].message = '传真不能为空';
                return callback(new Error('传真不能为空'));
            } else {
                const reg = /(^\d{3}-\d{8}$)|(^\d{4}-\d{7,8}$)/;
                // const reg = /^0\d{2,3}-?\d{7,8}$/;
                const fax = reg.test(value);
                if (!fax && value) {
                    rules[rule.field].warn = '传真格式错误';
                    rules[rule.field].message = '传真格式错误';
                    callback(new Error('传真格式错误'));
                } else {
                    rules[rule.field].warn = null;
                    callback();
                }
            }
        },
        //邮政编码
        postcode: (rule, value, callback) => {
            if (!value && rule.required) {
                rules[rule.field].warn = '邮政编码不能为空';
                rules[rule.field].message = '邮政编码不能为空';
                return callback(new Error('邮政编码不能为空'));
            } else {
                // const reg = /^[0-9]{6}$/;
                const reg = /^\d{6}$/
                const postcode = reg.test(value);
                if (!postcode && value) {
                    rules[rule.field].warn = '邮政编码格式错误';
                    rules[rule.field].message = '邮政编码格式错误';
                    callback(new Error('邮政编码格式错误'));
                } else {
                    rules[rule.field].warn = null;
                    callback();
                }
            }
        },
        //企业代码
        enterprisecode: (rule, value, callback) => {
            if (!value && rule.required) {
                rules[rule.field].warn = ' 企业代码不能为空';
                rules[rule.field].message = ' 企业代码不能为空';
                return callback(new Error(' 企业代码不能为空'));
            } else {
                const reg = /^[A-Z0-9]{2}[0-9]{6}[A-Z0-9]{10}$/
                const enterprisecode = reg.test(value);
                if (!enterprisecode && value) {
                    rules[rule.field].warn = '请输入18位社会统一信用代码';
                    rules[rule.field].message = '请输入18位社会统一信用代码';
                    callback(new Error('请输入18位社会统一信用代码'));
                } else {
                    rules[rule.field].warn = null;
                    callback();
                }
            }
        },
        //行政区划代码
        // xzqhcode: (rule, value, callback) => {
        //     if (!value && rule.required) {
        //         rules[rule.field].warn = ' 行政区划代码不能为空';
        //         rules[rule.field].message = ' 行政区划代码不能为空';
        //         return callback(new Error(' 行政区划代码不能为空'));
        //     } else {
        //         const reg = /^[1-8][1-7]\d{4}$/
        //         const xzqhcode = reg.test(value);
        //         if (!xzqhcode && value) {
        //             rules[rule.field].warn = '行政区划代码格式错误';
        //             rules[rule.field].message = '行政区划代码格式错误';
        //             callback(new Error('行政区划代码格式错误'));
        //         } else {
        //             rules[rule.field].warn = null;
        //             callback();
        //         }
        //     }
        // },
        //经度
        longitude: (rule, value, callback) => {
            if (!value && rule.required) {
                rules[rule.field].warn = ' 经度不能为空';
                rules[rule.field].message = ' 经度不能为空';
                return callback(new Error(' 经度不能为空'));
            } else {
                const reg = /^([1][3-9]|[2-9]\d{1}|1[0-2][0-9]|13[0-5])(\.\d{1,6})?$/
                const longitude = reg.test(value);
                if (!longitude && value) {
                    rules[rule.field].warn = '请输入正确的经度值，中国经度在13至135之间';
                    rules[rule.field].message = '请输入正确的经度值，中国经度在13至135之间';
                    callback(new Error('请输入正确的经度值，中国经度在13至135之间'));
                } else {
                    rules[rule.field].warn = null;
                    callback();
                }
            }
        },
        //纬度
        latitude: (rule, value, callback) => {
            if (!value && rule.required) {
                rules[rule.field].warn = ' 纬度不能为空';
                rules[rule.field].message = ' 纬度不能为空';
                return callback(new Error(' 纬度不能为空'));
            } else {
                const reg = /^([3-9]|[1-4][0-9]|5[0-3])(\.\d{1,6})?$/
                const latitude = reg.test(value);
                if (!latitude && value) {
                    rules[rule.field].warn = '请输入正确的纬度值，中国纬度在3至53之间';
                    rules[rule.field].message = '请输入正确的纬度值，中国纬度在3至53之间';
                    callback(new Error('请输入正确的纬度值，中国纬度在3至53之间'));
                } else {
                    rules[rule.field].warn = null;
                    callback();
                }
            }
        },
        // 身份证号
        idCard: (rule, value, callback) => {
            if (!value && rule.required) {
                rules[rule.field].warn = '身份证不能为空';
                rules[rule.field].message = '身份证不能为空';
                return callback(new Error('身份证不能为空'));
            } else {
                // const reg = /^[1-9][0-9]{5}([1][9][0-9]{2}|[2][0][0|1][0-9])([0][1-9]|[1][0|1|2])([0][1-9]|[1|2][0-9]|[3][0|1])[0-9]{3}([0-9]|[X])$/;
                const reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
                const idCard = reg.test(value);
                if (!idCard && value) {
                    rules[rule.field].warn = '身份证格式错误';
                    rules[rule.field].message = '身份证格式错误';
                    callback(new Error('邮身份证格式错误'));
                } else {
                    rules[rule.field].warn = null;
                    callback();
                }
            }
        },
        // 车牌号
        cPattern: (rule, value, callback) => {
            if (!value && rule.required) {
                rules[rule.field].warn = '车牌号不能为空';
                rules[rule.field].message = '车牌号不能为空';
                return callback(new Error('车牌号不能为空'));
            } else {
                const reg = /^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$/;
                const cPattern = reg.test(value);
                if (!cPattern && value) {
                    rules[rule.field].warn = '车牌号错误';
                    rules[rule.field].message = '车牌号错误';
                    callback(new Error('车牌号格式错误'));
                } else {
                    rules[rule.field].warn = null;
                    callback();
                }
            }
        },
        //发动机型号
        cEngine: (rule, value, callback) => {
            if (!value && rule.required) {
                rules[rule.field].warn = '发动机型号不能为空';
                rules[rule.field].message = '发动机型号不能为空';
                return callback(new Error('发动机型号不能为空'));
            } else {
                const reg = /[0-9]+[a-zA-Z]+[0-9a-zA-Z]*|[a-zA-Z]+[0-9]+[0-9a-zA-Z]*/;
                const cEngine = reg.test(value);
                if (!cEngine && value) {
                    rules[rule.field].warn = '发动机型号错误';
                    rules[rule.field].message = '发动机型号错误';
                    callback(new Error('发动机型号格式错误'));
                } else {
                    rules[rule.field].warn = null;
                    callback();
                }
            }
        },

        //原密码
        oldPwd: (rule, value, callback) => {
            if (!value && rule.required) {
                rules[rule.field].warn = '请输入原密码';
                rules[rule.field].message = '请输入原密码';
                return callback(new Error('请输入原密码'));
            } else {
                rules[rule.field].warn = null;
                callback();
            }
        },

        //新密码
        newPwd: (rule, value, callback) => {
            if (!value && rule.required) {
                rules[rule.field].warn = '请输入新密码';
                rules[rule.field].message = '请输入新密码';
                return callback(new Error('请输入新密码'));
            } else {
                const reg = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z.!@#$%^&*?]{8,16}$/;
                const newPwd = reg.test(value);
                if (!newPwd && value) {
                    let message = '须包含数字、字母，且长度为8-16位';
                    rules[rule.field].warn = message;
                    rules[rule.field].message = message;
                    callback(new Error(message));
                } else {
                    rules[rule.field].warn = null;
                    callback();
                }
            }
        },
        //确认新密码
        newPwdConfirm: (rule, value, callback) => {
            if (!value && rule.required) {
                rules[rule.field].warn = '请再次输入密码';
                rules[rule.field].message = '请再次输入密码';
                return callback(new Error('请再次输入密码'));
            } else {
                rules[rule.field].warn = null;
                callback();
            }
        },

        other: (rule, value, callback) => {
            let flag = true;
            if (rule.type == 'Array') {
                flag = !value.length && rule.required;
            }else {
                flag = (value === '' || value == null) && rule.required;
            }
            if (flag) {
                rules[rule.field].warn =  rules[rule.field].message;
                callback(rules[rule.field].warn);
            } else {
                rules[rule.field].warn = null;
                callback();
            }
        },
    }
}




function rulesMap(rule2) {
    let rulesMap = new Map();
    Object.keys(rules(rule2)).forEach(item => {
    
        rulesMap.set(item, rules(rule2)[item])
    })

    return rulesMap;
}

export default rulesMap;
