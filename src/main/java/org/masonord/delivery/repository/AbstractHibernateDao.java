package org.masonord.delivery.repository;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hibernate.query.SelectionQuery;
import org.postgresql.shaded.com.ongres.scram.common.util.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.Target;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractHibernateDao <T> {
    private Class<T> targetClass;

    @Autowired
    protected SessionFactory sessionFactory;

    public final void setClass(final Class<T> targetClassToSet) {
        targetClass = Preconditions.checkNotNull(targetClassToSet, "targetClassToSet");
    }

    protected T getById(final Long id) {
        String query = "from " + targetClass.getName() + " u where u.id = :id";
        return (T) sessionFactory.getCurrentSession().createQuery(query).setParameter("id", id).uniqueResult();
    }

    protected T getByUUID(final String id) {
        return (T) sessionFactory.getCurrentSession().get(targetClass, id);
    }

    protected T getByEmail(final String email) {
        String query = "from " + targetClass.getName() + " u where u.email = :email";
        return (T) sessionFactory.getCurrentSession().createQuery(query).setParameter("email", email).uniqueResult();
    }

    protected List<T> getAll() {
        return  sessionFactory.getCurrentSession().createQuery("from " + targetClass.getName(), targetClass).list();
    }


    protected List<T> getAll(int offset, int limit) {
        Query<T> query = sessionFactory.getCurrentSession().createQuery("From " + targetClass.getName(), targetClass);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.list();
    }


    protected T create(final T entity) {
        Preconditions.checkNotNull(entity, "entity");
        sessionFactory.getCurrentSession().persist(entity);
        return entity;
    }

    protected T update(final T entity) {
        Preconditions.checkNotNull(entity, "entity");
        return (T) sessionFactory.getCurrentSession().merge(entity);
    }

    protected void delete(final T entity) {
        Preconditions.checkNotNull(entity, "entity");
        sessionFactory.getCurrentSession().remove(entity);
    }

    protected void deleteById(final Long id) {
        final T entity = getById(id);
        Preconditions.checkNotNull(entity, "entity");
        sessionFactory.getCurrentSession().detach(entity);
    }
}
