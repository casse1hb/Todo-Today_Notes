package com.teamtreehouse.todotoday.dao;

import com.teamtreehouse.todotoday.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

//@rep = spring picks it up as a DAO. when we boot the app the
// DAO will be implemented. it will see find by username and
// includes a string aprameter which will generate a method
//that will find a user in the database the uses whatever
// value is passed in "string _username_
@Repository
public interface UserDao extends CrudRepository<User,Long> {
    User findByUsername(String username);
}
