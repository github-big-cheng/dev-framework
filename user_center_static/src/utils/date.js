import moment from 'moment'
export const dateHash = {
    /* 季节 */
    quarter: [
        moment().startOf("quarter").format("YYYY-MM-DD HH:mm:ss"), // 开始
        moment().endOf("quarter").format("YYYY-MM-DD HH:mm:ss"), // 结束
    ],
    /*本星期 */
    week: [
        moment().startOf("week").add(1, "day").format("YYYY-MM-DD HH:mm:ss"), // 开始
        moment().endOf("week").add(1, "day").format("YYYY-MM-DD HH:mm:ss"), // 结束
    ],
    /* 今天 */
    day: [
        moment().startOf("day").format("YYYY-MM-DD HH:mm:ss"),
        moment().endOf("day").format("YYYY-MM-DD HH:mm:ss"),
    ],
    /* 本月 */
    month: [
        moment()
            .month(moment().month())
            .startOf("month")
            .format("YYYY-MM-DD HH:mm:ss"),
        moment()
            .month(moment().month())
            .endOf("month")
            .format("YYYY-MM-DD HH:mm:ss"),
    ],
    /*本年 */
    year: [
        moment()
            .year(moment().year())
            .startOf("year")
            .format("YYYY-MM-DD HH:mm:ss"),
        moment().year(moment().year()).endOf("year").format("YYYY-MM-DD HH:mm:ss"),
    ],
};