package org.masonord.delivery.repository.dao;

import org.masonord.delivery.model.User;
import org.masonord.delivery.repository.AbstractHibernateDao;
import org.masonord.delivery.repository.interfaces.UserDaoInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class UserDao extends AbstractHibernateDao<User> implements UserDaoInterface {

    @Autowired
    public UserDao() {
        setClass(User.class);
    }

    @Override
    public User creatUser(User user) {
        return create(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return getByEmail(email);
    }
    @Override
    public User updateUserProfile(User user) {
        return update(user);
    }

    @Override
    public List<User> getAllUsers() {
        return getAll();
    }
}
