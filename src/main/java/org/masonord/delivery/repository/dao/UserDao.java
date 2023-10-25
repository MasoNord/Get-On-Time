package org.masonord.delivery.repository.dao;

import org.masonord.delivery.model.User;
import org.masonord.delivery.repository.AbstractHibernateDao;
import org.masonord.delivery.repository.interfaces.UserDaoInterface;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends AbstractHibernateDao<User> implements UserDaoInterface {
    public UserDao() {
        setClass(User.class);
    }

    @Override
    public User creatUser(User user) {
        return create(user);
    }

    @Override
    public User findUserById(String email) {
        return getByEmail(email);
    }
}
