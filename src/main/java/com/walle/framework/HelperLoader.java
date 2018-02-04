package com.walle.framework;

import com.walle.framework.helper.BeanHepler;
import com.walle.framework.helper.ClassHelper;
import com.walle.framework.helper.ControllerHelper;
import com.walle.framework.helper.IocHelper;
import com.walle.framework.util.ClassUtil;

/**
 * @author haohong
 */
public final class HelperLoader {
    public static void init() {
        Class<?>[] classList = {
                ClassHelper.class,
                BeanHepler.class,
                IocHelper.class,
                ControllerHelper.class,
        };

        for (Class<?> cls : classList) {
            ClassUtil.loadClass(cls.getName());
        }
    }
}
