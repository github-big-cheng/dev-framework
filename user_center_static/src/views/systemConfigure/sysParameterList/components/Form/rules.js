import { getLocalStorage } from '@/utils/auth';

let rules = (rules) => {

    return {
        //年龄
        age: (rule, value, callback) => {
            if (!value && rule.required) {
                rules[rule.field].warn = '年龄不能为空';
                return callback(new Error('年龄不能为空'));
            }

            if (!Number.isInteger(Number(value))) {
                rules[rule.field].warn = '年龄必须是数值类型';
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
                return callback(new Error('邮箱不能为空'));
            } else {
                const reg = /^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$/;
                const email = reg.test(value);
                if (!email && value) {
                    rules[rule.field].warn = '邮箱格式如:admin@163.com';
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
                return callback(new Error('手机号码不能为空'));
            } else {
                const reg = /^1(3[0-9]|4[5,7]|5[0,1,2,3,5,6,7,8,9]|6[2,5,6,7]|7[0,1,7,8]|8[0-9]|9[1,8,9])\d{8}$/;
                const phone = reg.test(value);
                if (!phone && value) {
                    rules[rule.field].warn = '手机号码格式错误';
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
                return callback(new Error('电话号码不能为空'));
            } else {
                const reg = /^0\d{2,3}-?\d{7,8}$/;
                const reg2 = /^1(3[0-9]|4[5,7]|5[0,1,2,3,5,6,7,8,9]|6[2,5,6,7]|7[0,1,7,8]|8[0-9]|9[1,8,9])\d{8}$/;
                const telephone = reg.test(value) || reg2.test(value);
                if (!telephone && value) {
                    rules[rule.field].warn = '电话号码格式错误';
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
                return callback(new Error('网址不能为空'));
            } else {
                let strRegex = '^((https|http|ftp|rtsp|mms)?://)'
                    + '?(([0-9a-z_!~*\'().&amp;=+$%-]+: )?[0-9a-z_!~*\'().&amp;=+$%-]+@)?' //ftp的user@
                    + '(([0-9]{1,3}.){3}[0-9]{1,3}' // IP形式的URL- 199.194.52.184
                    + '|' // 允许IP和DOMAIN（域名）
                    + '([0-9a-z_!~*\'()-]+.)*' // 域名- www.
                    + '([0-9a-z][0-9a-z-]{0,61})?[0-9a-z].' // 二级域名
                    + '[a-z]{2,6})' // first level domain- .com or .museum
                    + '(:[0-9]{1,4})?' // 端口- :80
                    + '((/?)|' // a slash isn't required if there is no file name
                    + '(/[0-9a-z_!~*\'().;?:@&amp;=+$,%#-]+)+/?)$';
                let reg = new RegExp(strRegex);
                const website = reg.test(value);
                if (!website && value) {
                    rules[rule.field].warn = '网址格式错误';
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
                return callback(new Error('传真不能为空'));
            } else {
                const reg = /^0\d{2,3}-?\d{7,8}$/;
                const fax = reg.test(value);
                if (!fax && value) {
                    rules[rule.field].warn = '传真格式错误';
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
                return callback(new Error('邮政编码不能为空'));
            } else {
                const reg = /^[0-9]{6}$/;
                const postcode = reg.test(value);
                if (!postcode && value) {
                    rules[rule.field].warn = '邮政编码格式错误';
                    callback(new Error('邮政编码格式错误'));
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
                return callback(new Error('身份证不能为空'));
            } else {
                const reg = /^[1-9][0-9]{5}([1][9][0-9]{2}|[2][0][0|1][0-9])([0][1-9]|[1][0|1|2])([0][1-9]|[1|2][0-9]|[3][0|1])[0-9]{3}([0-9]|[X])$/;
                const idCard = reg.test(value);
                if (!idCard && value) {
                    rules[rule.field].warn = '身份证格式错误';
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
                return callback(new Error('车牌号不能为空'));
            } else {
                const reg = /^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$/;
                const cPattern = reg.test(value);
                if (!cPattern && value) {
                    rules[rule.field].warn = '车牌号错误';
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
                return callback(new Error('发动机型号不能为空'));
            } else {
                const reg = /[0-9]+[a-zA-Z]+[0-9a-zA-Z]*|[a-zA-Z]+[0-9]+[0-9a-zA-Z]*/;
                const cEngine = reg.test(value);
                if (!cEngine && value) {
                    rules[rule.field].warn = '原密码输入错误';
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
                return callback(new Error('请输入原密码'));
            } else {
                const oldpassword = getLocalStorage('userInfo')?.firstLogin;
                if (value !== oldpassword) {
                    rules[rule.field].warn = '原密码输入错误';
                    callback(new Error('原密码输入错误'));
                } else {
                    rules[rule.field].warn = null;
                    callback();
                }
            }
        },

        //新密码
        newPwd: (rule, value, callback) => {
            if (!value && rule.required) {
                rules[rule.field].warn = '请输入新密码';
                return callback(new Error('请输入新密码'));
            } else {
                const reg = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z!@#$%^&*?]{8,16}$/;
                const newPwd = reg.test(value);
                const oldpassword = getLocalStorage('userInfo')?.firstLogin;
                const newpassword = getLocalStorage('newpassword');
                if (!newPwd && value) {
                    rules[rule.field].warn = '须包含数字、字母，且长度为8-16位';
                    callback(new Error('须包含数字、字母，且长度为8-16位'));
                } else if (oldpassword === newpassword) {
                    rules[rule.field].warn = '新密码不能与原密码相同';
                    callback(new Error('新密码不能与原密码相同'));
                } else {
                    rules[rule.field].warn = null;
                    callback();
                }
            }
        },
        //确认新密码
        newPwdConfirm: (rule, value, callback) => {
            const newpassword = getLocalStorage('newpassword');
            if (!value && rule.required) {
                rules[rule.field].warn = '请再次输入密码';
                return callback(new Error('请再次输入密码'));
            } else {
                const newpassword = getLocalStorage('newpassword');
                if (value !== newpassword) {
                    rules[rule.field].warn = '两次输入密码不一致';
                    callback(new Error('两次输入密码不一致'));
                } else {
                    rules[rule.field].warn = null;
                    callback();
                }
            }
        },

        other: (rule, value, callback) => {
            let flag = true;
            if (rule.type == 'Array') {
                flag = !value.length && rule.required;
            } else {
                flag = !value && rule.required;
            }
            if (flag) {
                rules[rule.field].warn = rules[rule.field].message;
                return callback(new Error(rules[rule.field].message));
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
