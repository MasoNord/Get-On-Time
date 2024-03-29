package org.masonord.delivery.repository.hibernate;

import org.masonord.delivery.model.User;
import org.masonord.delivery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class HibernateUserRepository extends AbstractHibernateRep<User> implements UserRepository {
    @Autowired
    public HibernateUserRepository() {
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

    @Override
    public List<User> getAllUsers(int offset, int limit) {
        return getAll(offset, limit);
    }

}