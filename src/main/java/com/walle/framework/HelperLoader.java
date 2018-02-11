package com.walle.framework;

import com.walle.framework.helper.*;
import com.walle.framework.util.ClassUtil;

/**
 * @author haohong
 */
public final class HelperLoader {
    public static void init() {
        Class<?>[] classList = {
                ClassHelper.class,
                BeanHepler.class,
                AopHelper.class,
                IocHelper.class,
                ControllerHelper.class,
        };

        for (Class<?> cls : classList) {
            ClassUtil.loadClass(cls.getName());
        }
    }
}
