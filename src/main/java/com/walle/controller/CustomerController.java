package com.walle.controller;

import com.walle.framework.annotation.Action;
import com.walle.framework.annotation.Inject;
import com.walle.framework.bean.Param;
import com.walle.framework.bean.View;
import com.walle.model.Customer;
import com.walle.service.CustomerService;

import java.util.List;

/**
 * @author haohong
 */
public class CustomerController {
    @Inject
    private CustomerService customerService;

    @Action("get:/customer")
    public View index(Param param) {
        List<Customer> customerList = customerService.getCustomerList();
        return new View("customer.jsp").addModel("customerList", customerList);
    }
}
