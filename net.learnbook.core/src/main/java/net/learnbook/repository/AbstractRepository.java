package net.learnbook.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.springframework.transaction.annotation.Transactional;

@Transactional(noRollbackFor = Exception.class)
public class AbstractRepository<E> implements Repository<E> {

	final Class<E> typeParameterClass;

	// LearnBook Persistence Unit
	@PersistenceContext(unitName = "learnbookPU")
	protected EntityManager entityManager;

	public AbstractRepository(Class<E> typeParameterClass) {
		this.typeParameterClass = typeParameterClass;
	}


	@Override
	public boolean save(E entity) {
		try {
			this.entityManager.persist(entity);
		} catch (PersistenceException error) {
			error.printStackTrace();
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> list(String className) {
		try {
			return this.entityManager.createQuery("FROM " + className).getResultList();

		} catch (IllegalArgumentException error) {
			error.printStackTrace();

		} catch (PersistenceException error) {
			error.printStackTrace();
		}
		return null;
	}

	@Override
	public E findById(Integer id) {
		try {
			return this.entityManager.find(typeParameterClass, id);

		} catch (IllegalArgumentException error) {
			error.printStackTrace();
			return null;

		} catch (PersistenceException error) {
			error.printStackTrace();
			return null;
		}
	}


	@Override
	public boolean update(E entity) {
		try {
			this.entityManager.merge(entity);
			this.entityManager.flush();

		} catch (PersistenceException error) {
			error.printStackTrace();
			return false;
		}
		return true;
	}

}
