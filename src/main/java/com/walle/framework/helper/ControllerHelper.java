package com.walle.framework.helper;

import com.walle.framework.annotation.Action;
import com.walle.framework.bean.Handler;
import com.walle.framework.bean.Request;
import com.walle.framework.util.ArrayUtil;
import com.walle.framework.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author haohong
 */
public class ControllerHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerHelper.class);

    private static final Map<Request, Handler> ACTION_MAP = new HashMap<Request, Handler>();

    static {
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();

        if (CollectionUtil.isNotEmpty(controllerClassSet)) {
            for (Class<?> controllerClass : controllerClassSet) {
                Method[] methods = controllerClass.getDeclaredMethods();

                if (ArrayUtil.isNotEmpty(methods)) {
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(Action.class)) {
                            Action action = method.getAnnotation(Action.class);
                            String mapping = action.value();

                            if (mapping.matches("\\w+:/[\\w/-]*")) {
                                String[] array = mapping.split(":");
                                if (ArrayUtil.isNotEmpty(array) && array.length == 2) {
                                    String requestMethod = array[0];
                                    String requestPath = array[1];
                                    Request request = new Request(requestMethod, requestPath);

                                    Handler handler = new Handler(controllerClass, method);
                                    ACTION_MAP.put(request, handler);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static Handler getHandler(String requestMethod, String requestPath) {
        Request request = new Request(requestMethod, requestPath);
        LOGGER.debug(requestPath);
        LOGGER.debug(requestMethod);
        LOGGER.debug(request.toString());
        LOGGER.debug(ACTION_MAP.toString());
        return ACTION_MAP.get(request);
    }
}
