package com.wisely.framework.helper.sequence;

import com.wisely.framework.exception.SystemException;
import com.wisely.framework.helper.DateHelper;
import com.wisely.framework.helper.StringHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.time.format.DateTimeFormatter;

/**
 * com.dounion.core.sequence.AbstractSequence
 */
@Slf4j
public abstract class AbstractSequence {

    protected static final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern(DateHelper.PATTERN_TIMESTAMP_01);

    /**
     * 上次生产 ID 时间戳
     */
    protected long lastTimestamp = -1L;

    /**
     * 并发控制，序列号
     */
    protected long sequence = 0;

    public abstract long nextId();

    protected long timeGen() {
        return System.currentTimeMillis();
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected void throwClockException(long offset) {
        throw SystemException.builder(StringHelper.format("Clock moved backwards.  Refusing to generate id for {0} milliseconds", offset));
    }

    protected long calcWorkerId(long dataCenterId, long maxWorkerId) {

        StringBuilder mPid = new StringBuilder();
        mPid.append(dataCenterId);
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if (StringUtils.isNotEmpty(name)) {
            /*
             * GET jvmPid
             */
            mPid.append(name.split("@")[0]);
        }
        /*
         * MAC + PID 的 hashcode 获取16个低位
         */
        return (mPid.toString().hashCode() & 0xffff) % (maxWorkerId + 1);
    }

    /**
     * 计算数据中心ID
     *
     * @param dataCenterId 数据中心ID
     * @return 数据中心ID
     */
    protected long calcDataCenterId(long dataCenterId) {

        long id = 0L;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            if (network == null) {
                id = 1L;
            } else {
                byte[] mac = network.getHardwareAddress();
                if (null != mac) {
                    id = ((0x000000FF & (long) mac[mac.length - 1]) | (0x0000FF00 & (
                            ((long) mac[mac.length - 2]) << 8))) >> 6;
                    id = id % (dataCenterId + 1);
                }
            }
        } catch (Exception e) {
            log.warn(" getDataCenterId: " + e.getMessage());
        }
        return id;
    }

}
