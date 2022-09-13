package com.wisely.framework.helper;

import com.wisely.framework.exception.SystemException;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


/**
 * 序列化工具类
 */
public class ProtoBufHelper {

    private ProtoBufHelper() {
    }


    /**
     * 序列化
     *
     * @param o
     * @param <T>
     * @return
     */
    public static <T> byte[] serializer(T o) {
        Schema schema = RuntimeSchema.getSchema(o.getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.MIN_BUFFER_SIZE);
        try {
            return ProtobufIOUtil.toByteArray(o, schema, buffer);
        } finally {
            buffer.clear();
        }
    }


    /**
     * 反序列化
     *
     * @param bytes
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T deserializer(byte[] bytes, Class<T> clazz) {
        try {
            Schema schema = RuntimeSchema.createFrom(clazz);
            T obj = (T) schema.newMessage();
            ProtobufIOUtil.mergeFrom(bytes, obj, schema);
            return obj;
        } catch (Exception e) {
            throw SystemException.builder(e, "ProtoBufHelper.deserializer_error");
        }
    }


    /**
     * 序列化对象列表
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> byte[] serializeList(List<T> list, Class<T> clazz) {

        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        LinkedBuffer buffer = LinkedBuffer.allocate(1024 * 1024);
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ProtobufIOUtil.writeListTo(bos, list, schema, buffer);
            return bos.toByteArray();
        } catch (Exception e) {
            throw SystemException.builder(e, "ProtoBufHelper.serializeList_error");
        } finally {
            buffer.clear();
        }
    }

    /**
     * 反序列化对象列表
     *
     * @param data
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> deserializeList(byte[] data, Class<T> clazz) {
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        try (ByteArrayInputStream in = new ByteArrayInputStream(data)) {
            return ProtobufIOUtil.parseListFrom(in, schema);
        } catch (IOException e) {
            throw SystemException.builder(e, "ProtoBufHelper.deserializeList_error");
        }
    }

}
