package com.wisely.sso.controller;

import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.AssertHelper;
import com.wisely.framework.helper.JsonHelper;
import com.wisely.framework.helper.ValidHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.ReentrantLock;


@ServerEndpoint(value = "/websocket/qrCode")
@Component
@Slf4j
public class QrCodeWebSocket {


    private final static String MSG_TYPE_KEY = "msgType";
    private final static String QR_CODE_KEY = "qrCode";

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    public static CopyOnWriteArraySet<QrCodeWebSocket> WEB_SOCKET_SET = new CopyOnWriteArraySet<>();

    /**
     * 关闭
     */
    public static ConcurrentHashMap<String, QrCodeWebSocket> WEB_SOCKET_MAP = new ConcurrentHashMap<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    public Session getSession() {
        return this.session;
    }

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        if (WEB_SOCKET_SET.add(this)) {
            log.trace("有新连接加入！当前在线人数为{}", WEB_SOCKET_SET.size());
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (WEB_SOCKET_SET.remove(this)) {
            log.trace("有一连接关闭！当前在线人数为{}", WEB_SOCKET_SET.size());
        }

        WEB_SOCKET_MAP.forEach((key, value) -> {
            if (ValidHelper.isEquals(value, this)) {
                WEB_SOCKET_MAP.remove(key);
            }
        });
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {

        log.debug("来自客户端的消息:{}", message);

        Model model = JsonHelper.json2Obj(message, Model.class);
        String msgType = model.getString(MSG_TYPE_KEY, "");
        switch (msgType) {
            case "register": // 注册
                String qrCode = model.getString(QR_CODE_KEY);
                AssertHelper.EX_VALIDATION.isNotBlank(qrCode, "common.parameter_required.qrCode");
                WEB_SOCKET_MAP.put(qrCode, this);
                log.trace("{}", WEB_SOCKET_MAP);
                break;
            default:
                // done nothing
                break;
        }
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("onError error:{}", error);
    }


    private static ReentrantLock lock = new ReentrantLock(true);


    /**
     * 该方法是我们根据业务需要调用的.
     *
     * @param message
     */
    public void sendMessage(String message) {
        synchronized (this.session) {
            if (session.isOpen()) {
                this.session.getAsyncRemote().sendText(message);
            }
        }
    }


}
