package com.wisely.framework.helper.lock;

import com.wisely.framework.entity.Model;
import org.springframework.beans.factory.annotation.Autowired;

public class WiselyLockFactory {

    @Autowired(required = false)
    Model<String, WiselyLock> lockModel = Model.builder();


    public WiselyLock build(String mode, int expireTime) {
        WiselyLock lock = lockModel.get(mode);
        return lock == null ? new LocalLock(expireTime) : lock;
    }

}
