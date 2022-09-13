package com.wisely.framework.helper.lock;

import com.wisely.framework.entity.Model;
import org.springframework.beans.factory.annotation.Autowired;

public class DoUnionLockFactory {

    @Autowired(required = false)
    Model<String, DoUnionLock> lockModel = Model.builder();


    public DoUnionLock build(String mode, int expireTime) {
        DoUnionLock lock = lockModel.get(mode);
        return lock == null ? new LocalLock(expireTime) : lock;
    }

}
