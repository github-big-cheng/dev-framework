package com.wisely.framework.entity;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 字节数组缓存对象
 */
public class ByteArrayCache implements Serializable {
    private static final long serialVersionUID = -1L;

    private byte[] array;
    private int hashCode;

    public ByteArrayCache() {}

    public ByteArrayCache(byte[] array) {
        this.array = array;
        this.hashCode = Arrays.hashCode(array);
    }

    public boolean equals(Object obj) {
        return obj instanceof ByteArrayCache && Arrays.equals( this.array, ( ( ByteArrayCache ) obj ).array );
    }

    public int hashCode() {
        return this.hashCode;
    }

    public byte[] getArray() {
        return this.array;
    }
}
