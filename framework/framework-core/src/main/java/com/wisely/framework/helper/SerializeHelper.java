package com.wisely.framework.helper;

import com.wisely.framework.exception.SystemException;
import com.google.common.collect.Lists;

import java.io.*;
import java.util.List;


/**
 * 序列化工具类
 */
public class SerializeHelper {

    private SerializeHelper() {
    }

    //序列化 对象
    public static byte[] serialize(Object obj){
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream( byteArrayOutputStream );
            objectOutputStream.writeObject( obj );
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw SystemException.builder(e, "SerializeHelper.serializer_obj_error");
        }
    }

    //反序列化 对象
    public static Object deserizlize(byte[] byt){
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream( byt );
            ObjectInputStream objectInputStream = new ObjectInputStream( byteArrayInputStream );
            return objectInputStream.readObject();
        } catch (Exception e) {
            throw SystemException.builder(e, "SerializeHelper.deserializer_obj_error");
        }
    }

    //反序列化 对象
    public static List<Object> deserizlizeList(List<byte[]> bytes){
        try {
            List<Object> list = Lists.newArrayList();
            bytes.forEach(byt-> list.add(deserizlize(byt)));
            return list;
        } catch (Exception e) {
            throw SystemException.builder(e, "SerializeHelper.deserializer_obj_error");
        }
    }
}
