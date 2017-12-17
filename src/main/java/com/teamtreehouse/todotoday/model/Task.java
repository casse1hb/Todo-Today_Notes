package com.teamtreehouse.todotoday.model;

import javax.persistence.*;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private boolean complete;


//associate each task in the database with a user.
// By using a user id column in the task table.
// map this to the user entity with a ManyToOne annotation, so
//I'm going to add that ManyToOne annotation here.
//And this is because many tasks can be associated to one user.
//And now I’d like to employ a simple approach to
//associating these two entities
//in the database and that is with a user id column
// in the task table. So let's use that join
// column annotation to configure that So
//right here all use the join column annotation and
//the name of that column is going to be user_id wonderful.
//And hey just so that JPA has access to both get and set the user when it needs
//to let's generate a getter and a setter for the user.

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Task() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
