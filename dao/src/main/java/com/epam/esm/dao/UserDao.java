package com.epam.esm.dao;

import com.epam.esm.entity.User;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * This interface contains logic for data access for users
 *
 * @author Stanislav Melnikov
 * @version 1.0
 */
public interface UserDao extends CrudRepository<User, Integer>, QuerydslPredicateExecutor<User> {
    Optional<User> findUserByLogin(String login);

    boolean existsByLogin(String login);
}
