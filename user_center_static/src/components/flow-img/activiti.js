/**
 * @param draw 目标区域
 * @param bpmnModel 流程定义信息
 * @param bmpNodeInfos 节点信息
 * @param historyNodes 历史节点
 * @param approvalInfo 审批信息
 */
class ActivitiSVG {

    constructor(options) {
        options = options || {};
        this.draw = document.createElementNS("http://www.w3.org/2000/svg", "svg");
        this.nodes = options.nodes || [];
        this.flows = options.flows || [];
        this.backgroundClass = options.backgroundClass || {'default': 'step-gray' /* 默认*/};
    }


    generate() {
        this.createRects();
        this.createLines();
        return this.draw;
    }

    // some common functions...
    /**
     * 获取背景色
     * @param status
     */
    pickUpbackgroundClass(status) {
        status = status || 'default';
        return this.backgroundClass[status] || 'step-gray';
    }


    /**
     * 判断是否指定类型节点
     * @param status
     */
    checkNodeType(node, types) {
        if (!node || !node.type) {
            return false;
        }

        let boo = false;
        types.forEach(function (v, i) {
            if (node.type == v) {
                boo = true;
                return false;
            }
        });
        return boo;
    }

    // draw functions...

    /**
     * 创建节点
     * @param rects
     * @param list
     * @param hights
     * @param approvalInfo
     * @param draw
     */
    createRects() {
        for (let key in this.nodes) {

            let node = this.nodes[key];
            // 圆形 -- 开始/结束节点
            if (this.checkNodeType(node, ["StartEvent", "EndEvent"])) {
                this.draw.appendChild(this.createCircle(node));
                continue;
            }
            // 菱形 -- 网关
            if (this.checkNodeType(node, ["InclusiveGateway", "ExclusiveGateway", "EventGateway", "ParallelGateway"])) {
                // this.draw.appendChild(this.createDoublePolygon(node));
                this.draw.appendChild(this.createGateWayPolygon(node));
                continue;
            }
            // 默认方形 -- 任务
            this.draw.appendChild(this.createRect(node));
        }
    }


    /**
     * 网关 --　双菱形
     * @param x
     * @param y
     * @param width
     * @param height
     * @param nodeObj
     * @returns {SVGGElement}
     */
    createDoublePolygon(nodeObj) {
        let g = document.createElementNS("http://www.w3.org/2000/svg", "g");
        g.appendChild(this.createPolygon(nodeObj.x, nodeObj.y, nodeObj.width, nodeObj.height, nodeObj));
        g.appendChild(this.createPolygon(nodeObj.x + nodeObj.width / 4, nodeObj.y + nodeObj.height / 4, nodeObj.width / 2, nodeObj.height / 2, nodeObj));
        return g;
    }


     /**
     * 网关 --　带X的
     * @param x
     * @param y
     * @param width
     * @param height
     * @param nodeObj
     * @returns {SVGGElement}
     */
    createGateWayPolygon(nodeObj) {
        let g = document.createElementNS("http://www.w3.org/2000/svg", "g");
        g.appendChild(this.createPolygon(nodeObj.x, nodeObj.y, nodeObj.width, nodeObj.height, nodeObj));
        
        let x1 = nodeObj.x;
        let x2 = nodeObj.x + nodeObj.width;
        let x3 = nodeObj.x + nodeObj.width / 2;
        let y1 = nodeObj.y;
        let y2 = nodeObj.y + nodeObj.height;
        let y3 = nodeObj.y + nodeObj.height / 2;

        let pointX1 = (x2 + x3) / 2;
        let pointY1 = (y1 + y3) /2;
        let pointX2 = (x1 + x3) / 2;
        let pointY2 = (y2 + y3) / 2;

        let path1 = document.createElementNS("http://www.w3.org/2000/svg", "path");
        path1.setAttribute("fill", "none");
        path1.setAttribute("class", "step-line");
        path1.setAttribute("d", "M" + (pointX1-2) + "," + (pointY1+2) + " L" + (pointX2+2) +"," + (pointY2-2));
        g.appendChild(path1);

        let path2 = document.createElementNS("http://www.w3.org/2000/svg", "path");
        path2.setAttribute("fill", "none");
        path2.setAttribute("class", "step-line");
        path2.setAttribute("d", "M" + (pointX1-2) + "," + (pointY2-2) + " L" + (pointX2+2) + "," + (pointY1+2));
        g.appendChild(path2);

        return g;
    }


    /**
     * 画一个棱形
     * @param x
     * @param y
     * @param width
     * @param height
     * @param nodeObj
     * @returns {SVGPolygonElement}
     */
    createPolygon(x, y, width, height, nodeObj) {

        let point = (x + width / 2) + "," + y;
        point = point + " " + (x + width) + "," + (y + height / 2);
        point = point + " " + (x + width / 2) + "," + (y + height);
        point = point + " " + x + "," + (y + height / 2);

        let node = document.createElementNS("http://www.w3.org/2000/svg", "polygon");
        node.setAttribute("points", point);
        node.setAttribute("stroke-width", "2");
        node.setAttribute("class", this.pickUpbackgroundClass(nodeObj.status));

        return node;
    }


    /**
     * 画一个圆
     * @param x
     * @param y
     * @param r
     * @param nodeObj
     * @returns {SVGGElement}
     */
    createCircle(nodeObj) {

        let x = nodeObj.x + nodeObj.width / 2;
        let y = nodeObj.y + nodeObj.width / 2;
        let r = nodeObj.width / 2;

        // 图形
        let node = document.createElementNS("http://www.w3.org/2000/svg", "circle");
        node.setAttribute("cx", x);
        node.setAttribute("cy", y);
        node.setAttribute("r", r);
        node.setAttribute("class", this.pickUpbackgroundClass(nodeObj.status));

        // 文本
        let text = document.createElementNS("http://www.w3.org/2000/svg", "text");
        text.setAttribute("text-anchor", "middle");
        text.textContent = nodeObj.name;
        text.setAttribute("startOffset", "1");
        text.setAttribute("x", x);
        if (nodeObj.type == "StartEvent") {
            text.setAttribute("y", y - r - 10);
            text.setAttribute("class", "step-start-text");
            node.setAttribute("class", "step-start");
        } else {
            text.setAttribute("y", y + r + 10);
            text.setAttribute("class", "step-end-text");
            node.setAttribute("class", "step-noend");
        }
        text.setAttribute("fill", "black");

        // g标签
        let g = document.createElementNS("http://www.w3.org/2000/svg", "g");
        g.appendChild(node);
        g.appendChild(text);
        return g;
    }


    /**
     * 画一个矩形
     * @param nodeObj
     * @returns {SVGGElement}
     */
    createRect(nodeObj) {

        // 图形
        let node = document.createElementNS("http://www.w3.org/2000/svg", "rect");
        node.setAttribute("x", nodeObj.x);//矩形的左侧位置
        node.setAttribute("y", nodeObj.y);//矩形的顶部位置
        node.setAttribute("stroke-width", 2);//矩形边框宽度
        node.setAttribute("rx", "5");//圆角
        node.setAttribute("ry", "5");//圆角
        node.setAttribute("height", nodeObj.height);//矩形的高度
        node.setAttribute("width", nodeObj.width);//矩形的宽度
        node.setAttribute("class", this.pickUpbackgroundClass(nodeObj.status));

        // 文本
        let text = document.createElementNS("http://www.w3.org/2000/svg", "text");
        text.setAttribute("text-anchor", "middle");
        text.setAttribute("height", nodeObj.height);//矩形的高度 
        text.setAttribute("width", nodeObj.width);//矩形的宽度
        text.setAttribute("class", "step-text-con");//矩形的宽度
        text.style.fontSize = "0.7em";
        /**
         * 文本长度>5 折行
         */
        let tspan1, tspan2;
        if (nodeObj.hasOwnProperty('name') && nodeObj.name && nodeObj.name.length > 5) {
            tspan1 = document.createElementNS("http://www.w3.org/2000/svg", "tspan");
            tspan2 = document.createElementNS("http://www.w3.org/2000/svg", "tspan");
            tspan1.textContent = nodeObj.name.substr(0,5);
            tspan1.style.fill="#333";
            tspan1.setAttribute("x", (nodeObj.x + nodeObj.width / 2) + 10);
            tspan1.setAttribute("y", nodeObj.y + nodeObj.height / 2 - 3);
            tspan2.textContent = nodeObj.name && nodeObj.name.length>10 ? nodeObj.name.substr(5,4) + '...' :  nodeObj.name.substr(5,5);
            tspan2.setAttribute("x", (nodeObj.x + nodeObj.width / 2) + 10);
            tspan2.setAttribute("y", nodeObj.y + nodeObj.height / 2 + 11);
            tspan2.style.fill="#333";
        }else {

        }
        nodeObj.hasOwnProperty('name') && nodeObj.name && nodeObj.name.length<=5 && (text.textContent = nodeObj.name);
        text.setAttribute("title", nodeObj.name);
        text.setAttribute("startOffset", "1");
        text.setAttribute("x", (nodeObj.x + nodeObj.width / 2) + 10);
        text.setAttribute("y", nodeObj.y + nodeObj.height / 2 + 4);
        text.style.fill="#333";

        //icon
        let image = document.createElementNS("http://www.w3.org/2000/svg", "image");

        // icon.setAttribute('xlink:href', './user.svg')
        image.href.baseVal = require('@/assets/images/user.svg')
        image.setAttribute("height", '25');//矩形的高度
        image.setAttribute("width", '25');//矩形的宽度
        image.setAttribute("x", (nodeObj.x + nodeObj.width / 2) - 42.5);
        image.setAttribute("y", nodeObj.y + nodeObj.height / 2 - 13.5);



        // 返回g标签
        let g = document.createElementNS("http://www.w3.org/2000/svg", "g");
        g.setAttribute("type", "userTask");
        g.appendChild(node);
        g.appendChild(text);
        g.appendChild(image);
        g.setAttribute("stepId", nodeObj.id);
        tspan1 && text.appendChild(tspan1) && text.appendChild(tspan2);
        return g;
    }


    /**
     * 创建线条-批量
     * @param draw
     * @param lines
     * @param hight
     */
    createLines() {
        for (let key in this.flows) {
            let flow = this.flows[key];
            if (flow.points && flow.points.length > 2) {
                let points = new Array();
                let j = 0;
                for (let i = 0; i < flow.points.length; i++) {
                    let pp = new Array();
                    pp[0] = flow.points[i].x;
                    pp[1] = flow.points[i].y;
                    points[j++] = pp;
                }
                this.draw.appendChild(this.createBrokenLine(points, flow.hl));
            } else {
                this.draw.appendChild(this.createLine(flow.points[0].x, flow.points[0].y, flow.points[1].x, flow.points[1].y, flow.hl));
            }
        }
    }


    /**
     * 创建线条
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param hl
     * @returns {SVGGElement}
     */
    createLine(x1, y1, x2, y2, hl) {

        let node = document.createElementNS("http://www.w3.org/2000/svg", "path");
        node.setAttribute("fill", "none");
        if (hl == true) {
            node.setAttribute("class", "step-hline");
        } else {
            node.setAttribute("class", "step-line");//线颜色
        }
        node.setAttribute("d", this.drawLineArrow(x1, y1, x2, y2));

        let g = document.createElementNS("http://www.w3.org/2000/svg", "g");
        g.setAttribute("type", "sequenceFLow");
        g.appendChild(node);
        return g;
    }


    createBrokenLine(points, hl) {
        let node = document.createElementNS("http://www.w3.org/2000/svg", "path");
        node.setAttribute("fill", "none");
        if (hl == true) {
            node.setAttribute("class", "step-hline");
        } else {
            node.setAttribute("class", "step-line");//线颜色
        }
        node.setAttribute("d", this.drawBrokenLineArrow(points));


        let g = document.createElementNS("http://www.w3.org/2000/svg", "g");
        g.setAttribute("type", "sequenceFLow");
        g.appendChild(node);
        return g;
    }


    /**
     * 画折线和箭头
     * @param points
     * @returns {string}
     */
    drawBrokenLineArrow(points) {
        let path = "";
        for (let i = 0; i <= points.length - 2; i++) {
            let p = points[i];
            let p2 = points[i + 1];
            if (i == points.length - 2) {
                path += this.drawLineArrow(p[0], p[1], p2[0], p2[1]);
            } else {
                path += "M" + p[0] + "," + p[1] + " L" + p2[0] + "," + p2[1];
            }
        }
        return path;
    }

    /**
     * 
     * @param {*} x1 
     * @param {*} y1 
     * @param {*} x2 
     * @param {*} y2 
     * @returns 
     */
    drawMarker() {
        var arrow = draw.marker(12, 12, function (add) {
            add.path('M2,2 L2,11 L10,6 L2,2');
            add.style({
                fill: 'green'
            });
        });
    }


    /**
     * 画直线和箭头
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @returns {string}
     */
    drawLineArrow(x1, y1, x2, y2) {
        let path;
        let slopy, cosy, siny;
        let Par = 10.0;
        let x3, y3;
        slopy = Math.atan2((y1 - y2), (x1 - x2));
        cosy = Math.cos(slopy);
        siny = Math.sin(slopy);

        path = "M" + x1 + "," + y1 + " L" + x2 + "," + y2;

        x3 = x2;
        y3 = y2;

        path += " M" + x3 + "," + y3;
        path += " L" + (Number(x3) + Number(Par * cosy - (Par / 2.0 * siny))) + "," + (Number(y3) + Number(Par * siny + (Par / 2.0 * cosy)));
        path += " M" + (Number(x3) + Number(Par * cosy + Par / 2.0 * siny) + "," + (Number(y3) - Number(Par / 2.0 * cosy - Par * siny)));
        path += " L" + x3 + "," + y3;
        return path;
    }
}

let ActivitiSVGDrawer = (options) => {
    return {
        draw: (options) =>{
            let drawer = new ActivitiSVG(options);
            return drawer.generate();
        }
    }
}

export {
    ActivitiSVGDrawer
}
