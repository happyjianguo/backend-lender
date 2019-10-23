package com.yqg.common.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

/**
 * 自定义Bean工厂
 */
public class CustomRepositoryFactoryBean<T extends JpaRepository<S, ID>, S, ID extends Serializable>
		extends JpaRepositoryFactoryBean<T, S, ID> {
	public CustomRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
		super(repositoryInterface);
	}// 1

	@Override
	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {// 2
		return new CustomRepositoryFactory(entityManager);
	}

	private static class CustomRepositoryFactory extends JpaRepositoryFactory {// 3

		public CustomRepositoryFactory(EntityManager entityManager) {
			super(entityManager);
		}

		@Override
		protected <T , ID extends Serializable> SimpleJpaRepository<?, ?> getTargetRepository(
				RepositoryInformation information, EntityManager entityManager) {// 4
			return new BaseDaoImpl( information.getDomainType(), entityManager);

		}

		@Override
		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {// 5
			return BaseDaoImpl.class;
		}
	}
}