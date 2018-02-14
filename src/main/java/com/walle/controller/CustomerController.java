package com.walle.controller;

import com.walle.framework.annotation.Action;
import com.walle.framework.annotation.Controller;
import com.walle.framework.annotation.Inject;
import com.walle.framework.bean.Data;
import com.walle.framework.bean.Param;
import com.walle.framework.bean.View;
import com.walle.framework.helper.DatabaseHelper;
import com.walle.model.Customer;
import com.walle.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author haohong
 */
@Controller
public class CustomerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    @Inject
    private CustomerService customerService;

    @Action("get:/customer")
    public View index(Param param) {
        List<Customer> customerList = customerService.getCustomerList();
        return new View("customer.jsp").addModel("customerList", customerList);
    }


    @Action("get:/customer-detail")
    public View detail(Param param) {
        LOGGER.debug(param.toString());

        return new View("customer.jsp");
//        return new Data(param);
    }

    @Action("get:/customer/detail")
    public Data detail2(Param param) {
        LOGGER.debug(param.toString());

//        return new View("customer.jsp");
        return new Data(param);
    }

    @Action("get:/customer2")
    public View insert(Param param) {
        Map<String, Object> fieldMap = new HashMap<String, Object>();
        fieldMap.put("name", "customer100");
        fieldMap.put("contact", "John");
        fieldMap.put("telephone", "135");

        boolean result = customerService.createCustomer(fieldMap);

        System.out.println("result " + result);

        return new View("customer.jsp");
    }
}
