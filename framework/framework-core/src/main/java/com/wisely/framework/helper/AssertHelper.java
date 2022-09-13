package com.wisely.framework.helper;


import com.wisely.framework.exception.*;
import com.wisely.framework.exception.asse.FrameworkAssert;

/**
 * 断言工具类
 */
public class AssertHelper {


    // private it
    private AssertHelper() {
    }


    /**
     * BaseException
     */
    public final static FrameworkAssert<BaseException> EX_BASE = BaseException::builder;
    /**
     * BusinessException
     */
    public final static FrameworkAssert<BusinessException> EX_BUSINESS = BusinessException::builder;
    /**
     * ThirdPartyException
     */
    public final static FrameworkAssert<ThirdPartyException> EX_THIRD_PARTY = ThirdPartyException::builder;
    /**
     * SystemException
     */
    public final static FrameworkAssert<SystemException> EX_SYSTEM = SystemException::builder;
    /**
     * ValidationException
     */
    public final static FrameworkAssert<ValidationException> EX_VALIDATION = ValidationException::builder;

}
