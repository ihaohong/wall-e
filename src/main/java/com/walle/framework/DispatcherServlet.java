package com.walle.framework;

import com.walle.framework.bean.Data;
import com.walle.framework.bean.Handler;
import com.walle.framework.bean.Param;
import com.walle.framework.bean.View;
import com.walle.framework.helper.BeanHepler;
import com.walle.framework.helper.ConfigHelper;
import com.walle.framework.helper.ControllerHelper;
import com.walle.framework.helper.ServletHelper;
import com.walle.framework.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author haohong
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(DispatcherServlet.class);

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        HelperLoader.init();
        ServletContext servletContext = servletConfig.getServletContext();
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletHelper.init(request, response);

        try {
            String requestMethod = request.getMethod().toLowerCase();
            String requestPath = request.getPathInfo();

            Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);

            if (handler != null) {
                Class<?> controllerClass = handler.getControllerClass();
                Object controllerBean = BeanHepler.getBean(controllerClass);

                Map<String, Object> paramMap = new HashMap<String, Object>();
                Enumeration<String> paramNames = request.getParameterNames();

                while (paramNames.hasMoreElements()) {
                    String paramName = paramNames.nextElement();
                    String paramValue = request.getParameter(paramName);
                    paramMap.put(paramName, paramValue);
                }

                String body = CodeUtil.decodeURL(StreamUtil.getString(request.getInputStream()));

                if (StringUtil.isNotEmpty(body)) {
                    String[] params = StringUtil.splitString(body, "&");
                    if (ArrayUtil.isNotEmpty(params)) {
                        for (String param : params) {
                            String[] array = StringUtil.splitString(param, "=");

                            if (ArrayUtil.isNotEmpty(array) && array.length == 2) {
                                String paramName = array[0];
                                String paramValue = array[1];
                                paramMap.put(paramName, paramValue);
                            }
                        }
                    }
                }

                Param param = new Param(paramMap);

                Method actionMethod = handler.getActionMethod();

                Object result;
                if (param.isEmpty()) {
                    result = ReflectionUtil.invokeMethod(controllerBean, actionMethod);
                } else {
                    result = ReflectionUtil.invokeMethod(controllerBean, actionMethod, param);
                }

                if (result instanceof View) {
                    View view = (View) result;
                    String path = view.getPath();

                    if (StringUtil.isNotEmpty(path)) {
                        if (path.startsWith("/")) {
                            response.sendRedirect(request.getContextPath() + path);
                        } else {
                            Map<String, Object> model = view.getModel();
                            for (Map.Entry<String, Object> entry : model.entrySet()) {
                                request.setAttribute(entry.getKey(), entry.getValue());
                            }

                            request.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(request, response);
                        }
                    }
                } else if (result instanceof Data) {
                    Data data = (Data) result;
                    Object model = data.getModel();

                    if (model != null) {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        PrintWriter writer = response.getWriter();
                        String json = JsonUtil.toJson(model);

                        writer.write(json);
                        writer.flush();
                        writer.close();
                    }
                }
            }
        } finally {
            ServletHelper.destroy();
        }
    }
}
