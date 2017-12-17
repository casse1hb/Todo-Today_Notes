package com.teamtreehouse.todotoday.web.controller;

import com.teamtreehouse.todotoday.model.Task;
import com.teamtreehouse.todotoday.model.User;
import com.teamtreehouse.todotoday.service.TaskService;
import com.teamtreehouse.todotoday.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class TaskController {
    @Autowired
    private TaskService taskService;

    //Ex 1: save method (autowired in)
    @Autowired
    private UserService userService;

//What i could do is include a security principal argument.
//And this principal object is one that would
//be injected as a parameter value and
//encapsulate all authentication data.
//As stored in the spring session data.
//Specifically, this would be a username
// password authentication token object.
//Check the teachers notes for more details on this class.
//What is important to note here, is that
// this object has a get name method.
//You can see that by using principal.getName.
//I'm not going to actually use that in here.
//But what this method would return is the user name of the currently
//authenticated user.
//We could leverage this in our timely
// template by adding the principal to
//the model map. Then using principal dot name in time leave
//to render the user name on the user interface.

    @RequestMapping({"/", "/todo"})
    public String taskList(Model model) {
        Iterable<Task> tasks = taskService.findAll();
        model.addAttribute("tasks",tasks);
        model.addAttribute("newTask", new Task());
        return "todo";
    }

    @RequestMapping(path = "/mark", method = RequestMethod.POST)
    public String toggleComplete(@RequestParam Long id) {
        Task task = taskService.findOne(id);
        taskService.toggleComplete(id);
        return "redirect:/";
    }

//The second way: casts to username.pass.auth.token
    @RequestMapping(path = "/tasks", method = RequestMethod.POST)
    public String addTask(@ModelAttribute Task task, Principal principal) {
        User user = (User)((UsernamePasswordAuthenticationToken)principal).getPrincipal();
        task.setUser(user);
        taskService.save(task);
        return "redirect:/";
    }
}

//The first way is by fetching the user name
// from the principal, then using the user
// service to grab the user entity by its user name:

//    @RequestMapping(path = "/tasks", method = RequestMethod.POST)
//    public String addTask(@ModelAttribute Task task, Principal principal) {
//        User user = userService.findByUsername(principal.getName());
//        task.setUser(user);
//        taskService.save(task);
//        return "redirect:/";
//    }

//declare a user variable that's one
//of ours not any number of these other ones.
//I'll call it user, and I will use the userService
//"FindByUsername" and then Principle.GetName.
//declare a user variable that's one
//of ours not any number of these other ones.
//I'll call it user, and I will use the userService
//"FindByUsername" and then Principle.GetName.
//the principal object will have a method named
// getName whose return value will be the username.
//Now I do see that userService isn't recognized
// that's because I have not auto-wired it into this class (top).