package com.teamtreehouse.todotoday.dao;

import com.teamtreehouse.todotoday.model.Task;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TaskDao extends CrudRepository<Task, Long> {
    @Query("select t from Task t where t.user.id=:#{principal.id}")
    List<Task> findAll();
}

//the task DAO is grabbing
//only the tasks associated with the currently
//authenticated use and
//not all tasks, so let's switch they're
// I'll open task DAO.
//Now I'd hate for all the spring data jpa
// implementation to go to waste so
//let's not do that.
//What we're going to do here is define our own
// query with an additional where clause
//to restrict the find all method to include only tasks for
//the currently authenticated user.
//We're going to leverage what is called JPQL which stands for
//Java Persistence Query Language.
//What this does is allow us to write queries
// in terms of RJPA
//entities instead of a Native SQL Query.
//So we'll do that with the query annotation.
//I'll use the query annotation o a method that I'm calling
//findAll it's going to return a list of task objects.
//findAll.
//Cool now as for the value element we use JPQL which looks a lot like SQL except
//that we can reference properties which will leverage our getters and setters.
//So here's how that looks select t from Task t,
//there we go we're naming the entity class right there task T,
//where T dot user dot ID equals and we'll put an expression there.
//Now here T is our task object user is the user property Property field,
//in our case, of the task object.
//And ID is another field of the user object, which, again,
//is part of the task object.
//So we're really using the
// object and property naming notation
//of JPA instead of the sequel
// convention using column names.
//Now here after the equals sign we need some
// way of grabbing the currently
//authenticated user's id.
//To do this, we'll use the spring
// expression language or Spell.
//We'll start with a colon and a hash sign,
// and enclose a value in curly braces.
//Now, in there, we can get access to the
// principal object using simply
//principal and then accessing its
// ID property like this.
//But in order for spring to be able to have
// access to this principal object,
//we need to configure what's called an evaluation context extension,
//to expose authentication data.
//And we'll do that in security config.


//To do that in this add task method we'll
//add a principal parameter whose
//value will be populated with the username
//password authentication token object.
//Which is implemented the principal interface.
//Save Methods:
// 1 = Go to TaskController (fetching username by principal interface)
    // grab user from task controller
// 2 =