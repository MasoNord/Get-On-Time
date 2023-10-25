package org.masonord.delivery.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.SessionFactory;
import org.postgresql.shaded.com.ongres.scram.common.util.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractHibernateDao <T> {
    private Class<T> targetClass;

    @Autowired
    protected SessionFactory session;

    public final void setClass(final Class<T> targetClassToSet) {
        targetClass = Preconditions.checkNotNull(targetClassToSet, "targetClassToSet");
    }

    public T getById(final String id) {
        return (T) session.getCurrentSession().get(targetClass, id);
    }

    public T getByEmail(final String email) {
        return (T) session.getCurrentSession().get(targetClass, email);
    }

    public List<T> getAll() {
        return  session.getCurrentSession().createNativeQuery("from " + targetClass.getName()).list();
    }

    public T create(final T entity) {
        Preconditions.checkNotNull(entity, "entity");
        session.getCurrentSession().persist(entity);
        return entity;
    }

    public T update(final T entity) {
        Preconditions.checkNotNull(entity, "entity");
        return (T) session.getCurrentSession().merge(entity);
    }

    public void delete(final T entity) {
        Preconditions.checkNotNull(entity, "entity");
        session.getCurrentSession().detach(entity);
    }

    public void deleteById(final String id) {
        final T entity = getById(id);
        Preconditions.checkNotNull(entity, "entity");
        session.getCurrentSession().detach(entity);
    }
}
