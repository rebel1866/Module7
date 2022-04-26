package com.epam.esm.dao;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Map;

/**
 * This interface contains logic for data access for users
 *
 * @author Stanislav Melnikov
 * @version 1.0
 */
public interface UserDao extends CrudRepository<User, Integer> {


}
