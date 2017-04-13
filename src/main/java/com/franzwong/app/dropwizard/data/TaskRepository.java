package com.franzwong.app.dropwizard.data;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class TaskRepository {
	
	@Inject
	Provider<EntityManager> em;
	
	public List<Task> findByUserName(String userName) {
		TypedQuery<Task> query = em.get().createQuery("SELECT t FROM Task t WHERE t.userName = :userName", Task.class);
		query.setParameter("userName", userName);
		return query.getResultList();
	}

        public List<Task> getAll() {
                TypedQuery<Task> query = em.get().createQuery("SELECT t FROM Task t", Task.class);
                return query.getResultList();
        }
	
	public Task addTask(Task task) {
		EntityManager entityManager = em.get();
		entityManager.persist(task);
		return task;
	}
}
