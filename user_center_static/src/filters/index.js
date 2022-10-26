/**
 * 文本格式化
 *
 * @param val
 * @param character
 * @returns {*}
 */
import {getToken} from "../utils/auth";
import {requestUrl} from "../api/api";

export const formatText = (val, character = "-") => {
    return val ? val : character;
}


/**
 * 是/否 格式化
 * @param val
 * @param defaultVal
 */
export const yeaOrNoFormat = (val, defaultVal = "-") => {
    return !val && val != 0 ? defaultVal : (val == 1 ? '是' : '否');
}


/**
 * 毫秒格式时间格式化
 *
 * @param val
 * @param defaultVal
 * @returns {string}
 */
export const timeMillisFormat = (val, defaultVal = "-") => {

    if (!val) {
        return "";
    }

    let ms = parseInt(val);
    ms = ms < 0 ? 0 : ms;
    let days = ms / 1000 / 60 / 60 / 24;
    let daysRound = Math.floor(days);
    let hours = ms / 1000 / 60 / 60 - 24 * daysRound;
    let hoursRound = Math.floor(hours);
    let minutes = ms / 1000 / 60 - 24 * 60 * daysRound - 60 * hoursRound;
    let minutesRound = Math.floor(minutes);

    if (daysRound > 0) {
        return daysRound + "天" + hoursRound + "小时" + minutesRound + "分钟";
    }
    if (hoursRound > 0) {
        return hoursRound + "小时" + minutesRound + "分钟";
    }
    if (minutesRound > 0) {
        return minutesRound + "分钟";
    }

    return defaultVal;
}

/**
 * 手机号格式化
 *
 * @param mobile
 * @param defaultVal
 * @returns {string}
 */
export const mobileNoFormat = (mobile = "", defaultVal = "-") => {

    if (!mobile) {
        return defaultVal;
    }
    let val = mobile.replace(/[^\d]/g, "");

    let arr = val.split("");
    let str = "";
    arr.forEach(function (val, ind) {
        if (ind === 3 || ind === 7) {
            str += " ";
        }
        str += val;
    });
    return str;
};


/**
 * 静态文件路径格式化
 *
 * @param filePath
 * @param defaultVal
 */
export const staticFilePathFormat = (filePath, defaultVal = "") => {
    if (!filePath) {
        return defaultVal;
    }
    return requestUrl + "/sys/file/download?_sgk=" + getToken() + "&filePath=" + filePath;
}

/**
 * 金额千分位格式化格式化
 *
 * @param filePath
 * @param defaultVal
 */
export const thousandsFormat = (price, defaultVal = "") => {
    if (!price || isNaN(price)) {
        return "-";
    }

    return (price + '').replace(/(\d{1,3})(?=(\d{3})+(?:$|\.))/g, '$1,');

}

/**
 * 去除金额千分位格式
 */
export const delcommafyFormat = (num) => {
    if (num != undefined) {
        num = num.toString();
        num = num.replace(/[ ]/g, ""); //去除空格
        num = num.replace(/,/gi, '');
        return Number(num);
    }
}

/**
 * 同乘人格式化
 *
 * @param filePath
 * @param defaultVal
 */
export const objNamesFormat = (names, defaultVal = "") => {
    if (!names || names.length == 0) {
        return "-";
    }
    return names.split(',').join('，')
}


export const jsonHighlight = (json = "") => {
    if (typeof json != 'string') {
        json = JSON.stringify(json, undefined, 2);
    }
    json = json.replace(/&/g, '&').replace(/</g, '<').replace(/>/g, '>');
    return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
        var cls = 'number';
        if (/^"/.test(match)) {
            if (/:$/.test(match)) {
                cls = 'key';
            } else {
                cls = 'string';
            }
        } else if (/true|false/.test(match)) {
            cls = 'boolean';
        } else if (/null/.test(match)) {
            cls = 'null';
        }
        return '<span class="' + cls + '">' + match + '</span>';
    });
}
