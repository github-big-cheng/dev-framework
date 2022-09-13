export const DoVideo = {
    wsClient: {

    },
    window: {
        isOpen: 0,
        id: ""
    },
    ptzControl: {
        control: "1",
        d: 0,
        stop: 0
    },
    selectVideo: {
        isCreate: "1",
        count: 0,
        rows: 0,
    },
    play: {
        play: "1",
        ip: "",
        port: "",
        userName: "",
        pwd: "",
        type: "",
        channelNo: ""
    },
    init: function (window, f) {
        try {
            DoVideo.wsClient = new WebSocket('ws://localhost:40001');
        } catch (e) {
            alert("视频服务未开启");
        }
        DoVideo.wsClient.onopen = function (e) {
            var arr = [];
            arr.push(window);
            var str = JSON.stringify(arr);
            DoVideo.wsClient.send(str);
            // setInterval(DoVideo.setWinLocation, 10);
            setTimeout(f, 500);
        }
        DoVideo.wsClient.onclose = function (e) {
        }
        DoVideo.wsClient.onmessage = function (e) {
        }
        DoVideo.wsClient.onerror = function (e) {
        };
    },
    setWinLocation: function () {
        if (!document[hiddenProperty]) {
            console.log(1)
            var fx = 0, fy = 0, mainFy = 0, fy1 = 0;
            /* try {
                fx = parseInt($(parent.document).contents().find("#videoSecurity_videoUI").offset().left.toFixed(0)),
                    fy = parseInt($(parent.document).contents().find("#videoSecurity_videoUI").offset().top.toFixed(0)),
                    mainFy = $(parent.document).contents().find("#videoSecurity_videoUI").innerHeight(),
                    fy1 = $(parent.document).contents().find("#videoSecurity_videoUI").closest('#main_tabs').find('.tabs-header').innerHeight();
            } catch (e) {
            }
            var numberWidth = $('.inwarehouse-wrapper').innerHeight();
            if (fx == fy && !fx && !fy && !numberWidth) {//判断
                return;
            } else {
                fx += - 15;
            }
            if (fy1) {
                var inHeight = parent.parent.window.innerHeight;
                if (inHeight - mainFy > fy) {
                    console.log(123);
                    fy += (parseInt(fy1)) + 8;
                }
            }
            if ($("#content").parent().find('h3')[0]) {
                fx += parseInt($("#content").parent().find('h3')[0].offsetLeft);
                fy += parseInt($("#content").parent().find('h3')[0].offsetHeight)
                    + parseInt($("#content").parent().find('h3')[0].offsetTop);
            } else {

                // var ls = window.localStorage;
                // var tabName = ls.getItem("tabNames");
                // if(tabName != "库区视频监控"){
                //     return;
                // }
            }
            if (numberWidth) {
                var fy1 = parseInt($(parent.document).contents().find(".navs-wrap")[0].offsetHeight.toFixed(0));
                var obj = $(".inwarehouse-wrapper"), w = obj[0].offsetWidth.toFixed(0), h = obj[0].offsetHeight.toFixed(0),
                    x = obj.offset().left.toFixed(0), y = obj.offset().top.toFixed(0);
                fy += fy1 - 4;
                fx -= 10;
                w = parseInt(w) + 20;
                console.log("fx    " + fx + "    fy   " + fy + "   width   " + w + "   height   " + h + "  x   " + x + "    y " + y);
            } else {
                var obj = $("#content"), w = obj.width().toFixed(0), h = obj.height().toFixed(0),
                    x = obj.offset().left.toFixed(0), y = obj.offset().top.toFixed(0);
            } */
            DoVideo.wsClient.send("[{'isLocation': '1', 'fx': '" + 10 + "', 'fy': '" + 10 + "', 'w': '" + 900 + "', 'h': '" + 600 + "', 'x': '" + 100 + "', 'y': '" + 100 + "'}]");
        }
    },
    ptControl: function (direction, startOrStop) {
        var p = DoVideo.ptzControl;
        p.d = direction;
        p.stop = startOrStop;
        var str = JSON.stringify(p);
        DoVideo.wsClient.send("[" + str + "]");
    },
    choseVideo: function (ip, port, userName, pwd, channel, type) {
        var p = DoVideo.play;
        p.ip = ip;
        p.port = port;
        p.userName = userName;
        p.pwd = pwd;
        p.channelNo = channel;
        p.type = type;
        var str = JSON.stringify(p);
        DoVideo.wsClient.send("[" + str + "]");
    },
    createVideo: function (count, rows) {
        var p = DoVideo.selectVideo;
        p.count = count;
        p.rows = rows;
        var str = JSON.stringify(p);
        DoVideo.wsClient.send("[" + str + "]");
    }
}

var hiddenProperty = 'hidden' in document ? 'hidden' :
    'webkitHidden' in document ? 'webkitHidden' :
        'mozHidden' in document ? 'mozHidden' :
            null;
document.addEventListener(visibilityChangeEvent, onVisibilityChange);
// 不同浏览器的事件名
var visibilityChangeEvent = hiddenProperty.replace(/hidden/i, 'visibilitychange');
var onVisibilityChange = function () {
    var window = DoVideo.window;
    if (!document[hiddenProperty]) {
        window.isOpen = 1;
    } else {
        window.isOpen = 0;
    }
    DoVideo.wsClient.send("[" + JSON.stringify(window) + "]");
}





