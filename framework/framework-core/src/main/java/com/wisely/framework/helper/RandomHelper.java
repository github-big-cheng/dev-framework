package com.wisely.framework.helper;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * RandomHelper
 * 随机数工具类
 */
public class RandomHelper {


    /**
     * 随机获取list中的一个元素
     *
     * @param contents
     * @return
     */
    public static <T> T get(List<T> contents) {
        return ValidHelper.isEmpty(contents) ? null : contents.get(getInt(contents.size()) - 1);
    }

    /**
     * 根据list里的内容随机获取 number个
     *
     * @param contents
     * @param returnSize
     * @return
     */
    public static <T> List<T> get(List<T> contents, int returnSize) {
        if (returnSize < 0 || returnSize >= contents.size()) {
            return contents;
        }

        List<T> newList = Lists.newArrayList();
        ThreadLocalRandom.current()
                .ints(0, returnSize - 1)
                .distinct()
                .limit(returnSize)
                .forEach(index -> newList.add(contents.get(index)));
        return newList;
    }


    /**
     * 32位UUID
     *
     * @return
     */
    public static String uuid(int length) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        String uuid = new UUID(random.nextLong(), random.nextLong()).toString()
                .replace("-", "").toUpperCase();
        if (length == 16) {
            return uuid.substring(8, 24);
        } else {
            return uuid.substring(0, length);
        }
    }

    public static String uuid() {
        return uuid(32);
    }


    public static Integer getInt(int value) {
        if (value == 0) return 1;
        return ThreadLocalRandom.current()
                .ints(1, value)
                .distinct()
                .limit(1)
                .findFirst()
                .getAsInt();
    }

    public static Long getLong(long value) {
        if (value == 0) return 1l;
        return ThreadLocalRandom.current()
                .longs(1, value)
                .distinct()
                .limit(1)
                .findFirst()
                .getAsLong();
    }
}
