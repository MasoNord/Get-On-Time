package org.masonord.delivery.repository.dao;

import org.masonord.delivery.model.Customer;
import org.masonord.delivery.repository.AbstractHibernateDao;
import org.masonord.delivery.repository.interfaces.CustomerDaoInterface;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerDao extends AbstractHibernateDao<Customer> implements CustomerDaoInterface {

    @Override
    public Customer createCustomer(Customer customer) {
        return create(customer);
    }

    @Override
    public Customer getCustomer(String id) {
        return getById(id);
    }

    @Override
    public List<Customer> getCustomers() {
        return getAll();
    }

    @Override
    public void deleteCustomer(String id) {
        deleteById(id);
    }
}
