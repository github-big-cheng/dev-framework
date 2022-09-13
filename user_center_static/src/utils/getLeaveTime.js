import moment from 'moment'
import { Message } from "element-ui";
/**
 * 计算请假时长
 * @param {String} beginTime    开始时间，例如：2020-12-26 12:00
 * @param {String} endTime      结束时间,例如：2020-12-27 12:00
 * @param {Number} stWorkTime   上班时间,例如：早上9点，就传9
 * @param {Number} enWrokTime   下班时间,例如：下午6点，就传18
 * @param {Boolean} isFreeTime  是否要去除午休工作时长
 * @param {Number} freeTimeMon  午休开始时间，例如：中午12点，就传12
 * @param {Number} freeTimeAft  午休结束时间，例如：下午1点，就传13
 * @param { Array } fillterDatas 节假日日期，比如国庆，中秋，是个数组
 * @param { Number } dateType 请假类型 如果为产假陪产假，不排除节假日与周末
 * 
 */
export const getLeaveTime = (beginTime, endTime, stWorkTime, enWrokTime, isFreeTime, freeTimeMon, freeTimeAft, fillterDatas, dateType) => {
  var days;
  var hours;
  var date;
  var freeTime = freeTimeAft - freeTimeMon;
  var beginArr = beginTime.split(" ");
  var beginHours = parseInt(beginArr[1].split(":")[0]);
  var beginMin = parseInt(beginArr[1].split(":")[1]);
  var beginHoursMin = beginHours + beginMin / 60;
  var endArr = endTime.split(" ");
  var endHours = parseInt(endArr[1].split(":")[0]);
  var endMin = parseInt(endArr[1].split(":")[1]);
  var endHoursMin = endHours + endMin / 60;
  //如果beginHoursMin时间小于上班时间都算上班时间
  if (beginHoursMin <= stWorkTime) {
    beginHoursMin = stWorkTime;
  }
  //如果endHoursMin时间大于上班时间都算下班时间
  if (endHoursMin >= enWrokTime) {
    endHoursMin = enWrokTime;
  }
  //如果endHoursMin时间小于上班时间都算下班时间
  if (endHoursMin <= stWorkTime) {
    endHoursMin = stWorkTime;
  }
  //如果结束时间在freeTimeMon和freeTimeAft之间都算freeTimeMon
  if (isFreeTime) {
    if (endHoursMin >= freeTimeMon && endHoursMin <= freeTimeAft) {
      endHoursMin = freeTimeMon;
    }
  }
  //获取结束时间-开始时间的天数
  var daysBetweenlist = daysBetween(beginTime, endTime);
  let effectiveLeaveDate
  if (dateType == 5 || dateType == 7 || dateType == 8 || dateType == 9) {
    effectiveLeaveDate = daysBetweenlist;
  } else {
    effectiveLeaveDate = daysBetweenlist.filter(date => !fillterDatas.includes(date)).filter(date => (new Date(date).getDay() != 6 && new Date(date).getDay() != 0))
  }
  if (beginTime > endTime) {
    Message({
      message: '开始时间需小于结束时间',
      type: "error",
      duration: 2 * 1000,
    });
    return false;
  }
  if (!effectiveLeaveDate.includes(beginTime.split(' ')[0]) || !effectiveLeaveDate.includes(endTime.split(' ')[0])) {
    Message({
      message: '调休开始、结束时间需在工作日，请您重新选择',
      type: "error",
      duration: 2 * 1000,
    });
    return false;
  }
  if (effectiveLeaveDate.length > 0) {
    var daysBetweenLen = effectiveLeaveDate.length;
    //午休
    if (isFreeTime) {
      var hour = enWrokTime - stWorkTime - freeTime;
      if (daysBetweenLen == 1) {
        //同一天
        if (endHoursMin - freeTimeAft > 0 && beginHoursMin <= freeTimeMon) {
          hours = (endHoursMin) - (beginHoursMin) - freeTime;
        } else {
          hours = (endHoursMin) - (beginHoursMin)
        }
      } else if (daysBetweenLen == 2) {
        //跨一天   
        //第一天的时长
        hours = enWrokTime - beginHoursMin;
        //是否有午休         
        if (beginHoursMin <= freeTimeMon)
          hours = hours - freeTime;
        //第二天的时长    
        hours += endHoursMin - stWorkTime;
        //是否有午休 
        if (endHoursMin >= freeTimeAft)
          hours = hours - freeTime;
      } else {
        //跨两天以上 
        //第一天的时长
        hours = enWrokTime - beginHoursMin;
        //是否有午休             
        if (beginHoursMin <= freeTimeMon)
          hours = hours - freeTime;
        //中间时长
        hours += (daysBetweenLen - 2) * (hour);
        //最后一天时长
        hours += endHoursMin - stWorkTime;
        //是否有午休 
        if (endHoursMin >= freeTimeAft)
          hours = hours - freeTime;
      }
      days = Math.floor(hours / hour);
      hours = hours % hour;
      date = {
        "days": days,
        "hours": hours
      };

    } else {
      //非午休   
      var hour = enWrokTime - stWorkTime;
      if (daysBetweenLen == 1) {
        //同一天
        hours = (endHoursMin) - (beginHoursMin);
      } else if (daysBetweenLen == 2) {
        //跨一天   
        hours = enWrokTime - beginHoursMin;
        //第二天的时长
        hours += endHoursMin - stWorkTime;
      } else {
        //跨两天以上 
        //第一天的时长
        hours = enWrokTime - beginHoursMin;
        //中间时长
        hours += (daysBetweenLen - 2) * (hour);
        //最后一天时长
        hours += endHoursMin - stWorkTime;
      }
      days = Math.floor(hours / hour);
      hours = hours % hour;
      date = {
        "days": days,
        "hours": hours
      };
    }
  }
  //调整不足30分钟算半小时，超过30分钟算一小时
  const s = parseInt(date.hours)
  let w = date.hours - s
  if (w > 0 && w <= 0.5) w = 0.5
  else if (w > 0.5) w = 1
  date.hours = s + w
  return date;
}

/**
 * 根据两个日期，判断相差天数
 * @param bdate 开始日期 如：2016-11-01
 * @param edate 结束日期 如：2016-11-02
 * @returns {number} 返回相差天数
 */
export const daysBetween = (bdate, edate) => {
  let oneDayMillisecond = 86400000 //一天毫秒数
  let bdateMillisecond = new Date(bdate).getTime()
  let edateMillisecond = new Date(edate).getTime()
  let diffMillisecond = edateMillisecond - bdateMillisecond
  let diffDays = Math.floor(Math.abs(diffMillisecond / oneDayMillisecond))
  let date_arr = []
  for (let index = 0; index <= diffDays; index++) {
    let new_date = formatTimestamp(bdateMillisecond + oneDayMillisecond * index, "YYYY-MM-DD")
    date_arr.push(new_date)
  }
  return date_arr
}

/**
 * 格式化时间戳
 * @param {时间戳，毫秒} timestamp 
 * @param {格式} format 
 */
export const formatTimestamp = (timestamp, format = 'YYYY-MM-DD HH:mm') => {
  return moment(timestamp).format(format);
}
