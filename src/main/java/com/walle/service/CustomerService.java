package com.walle.service;

import com.walle.framework.annotation.Service;
import com.walle.framework.annotation.Transaction;
import com.walle.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.walle.framework.helper.DatabaseHelper;

import java.util.List;
import java.util.Map;

@Service
public class CustomerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    public List<Customer> getCustomerList() {
        String sql = "SELECT * FROM walle_customer";

        return DatabaseHelper.queryEntityList(Customer.class, sql);
    }

    public Customer getCustomer(long id) {
        String sql = "SELECT * FROM walle_customer WHERE id = ? LIMIT 1";
        return DatabaseHelper.queryEntity(Customer.class, sql, id);
    }

    @Transaction
    public boolean createCustomer(Map<String, Object> fieldMap) {
        return DatabaseHelper.insertEntity(Customer.class, fieldMap);
    }

    @Transaction
    public boolean updateCustomer(long id, Map<String, Object> fieldMap) {
        return DatabaseHelper.updateEntity(Customer.class, id, fieldMap);
    }

    @Transaction
    public boolean deleteCustomer(long id) {
        return DatabaseHelper.deleteEntity(Customer.class, id);
    }
}
