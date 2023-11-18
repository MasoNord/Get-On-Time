package org.masonord.delivery.repository.dao;

import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.model.Customer;
import org.masonord.delivery.repository.AbstractHibernateDao;
import org.masonord.delivery.repository.interfaces.CustomerDaoInterface;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
@Transactional
public class CustomerDao extends AbstractHibernateDao<Customer> implements CustomerDaoInterface {
    public CustomerDao() {setClass(Customer.class);}

    @Override
    public Customer createCustomer(Customer customer) {
        return create(customer);
    }
    @Override
    public Customer getCustomerById(Long id) {
        return getById(id);
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        return getByEmail(email);
    }

    @Override
    public List<Customer> getCustomers() {
        return getAll();
    }

    @Override
    public List<Customer> getCustomers(int offset, int limit) {
        return getAll(offset, limit);
    }

    @Override
    public void deleteCustomer(Long id) {
        deleteById(id);
    }

    @Override
    public Customer updateProfile(Customer customer) {
        return update(customer);
    }

}
