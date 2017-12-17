package com.teamtreehouse.todotoday.config;

import com.teamtreehouse.todotoday.service.UserService;
import com.teamtreehouse.todotoday.web.FlashMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.query.spi.EvaluationContextExtension;
import org.springframework.data.repository.query.spi.EvaluationContextExtensionSupport;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;
//sets password to password encoder bean.
// the encoded version will be checked against the database. need to be stored.
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }
//password: spring security. Order does not matter.BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

//overriding the configure method that accepts a web sec. parameter
//this overrides the configure method that accepts a web
//security parameter. We override that one. inside this method
//we will tell spring to bypass sec checks
//for our static resources. to do this well call that web
// security objects ignoring method. and that is what we want to ignore.
//we wanted to ignore any path that matches this pattern"/assets/anything"
//in doing this well have a spring ignore requests to anything in that assets
//directory. which is where all of our static ref are.

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/assets/**");
    }


//First is restricting access to all resources to only those
//users that are authorized to do so for us that means a user
// must have the user role. So we'll start with that
// http parameter here and I'm gonna format.
//All of this code nicely as you often see on tutorials
// about spring security online.
//So http.uthorizeRequests.
//This will ensure that all requests must be authorized
// at this point.
//So here I'll say any request must have the role USER.
//So again I start by calling the authorizedRequest method so
//that anything that follows is assumed to be handled by authorisation.
//And it's implied that authentication has already happened at
// this point.
//Also notice that I didn't use roll underscore user here.
//I instead use just User.
//And that's because this has a role method will prepared
//a role_ before checking the role.
//So at this point every request other than ones to our static assets is restricted.
//This will completely lock down our application because we have no way for
//navigate to the app route in a browser, you'd see a 403 response code.
//users to authenticate In fact, if you were to run this app, and
//Which means that access is forbidden.

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .anyRequest().hasRole("USER")
//configures log-in role.
                .and()
   //allows access to log in form
            .formLogin()
                .loginPage("/login")
                .permitAll()
            //executes whether a login works or fails.
                .successHandler(loginSuccessHandler())
                .failureHandler(loginFailureHandler())
                //config logout.
                // configuring the log out actions so that spring knows
                //which URI to capture, for destroying a user's authentication data, and
                //to do this we'll use the log out method in that filter chain.
                .and()
            .logout()
                .permitAll()
                .logoutSuccessUrl("/login")
                .and()
                //adds csrf protection.
            .csrf();
        //look for inspect > elements > <input type="hidden" name="_csrf"value"
    }
//uses lambda expression to return 3 parameters. sends
// to redirect content root.

//A method that accepts an http servlet req param. an
    // http servlet response and an auth object.
        //could use a success handler int instead of a lambda all together.
    public AuthenticationSuccessHandler loginSuccessHandler() {
        return (request, response, authentication) -> response.sendRedirect("/");
    }

    //returns multi line lambda.
    //if it fails then redirects user
    // back to login page titled "/login"
    //"flash" is from login controller.java.
    // Uses the http servlet request, get session data,
    //and looks for an attribute named "flash".
    //if not found an exception is thrown "ex"
    //fragment named flash in layouts.html
    //also flash is located in login.html line13
    public AuthenticationFailureHandler loginFailureHandler() {
        return (request, response, exception) -> {
            request.getSession().setAttribute("flash", new FlashMessage("Incorrect username and/or password. Please try again.", FlashMessage.Status.FAILURE));
            response.sendRedirect("/login");
        };
    }

    //here we'll create a spring bean for an evaluation context extension.
    //And this will return an anonymous inner class.
    //Let me show you how that looks.
    //I will do that at the bottom.
    //It'll be a spring bean.
    //And I will call it.
    //It will be an evaluation context extension right there.
    //I'll call it securityExtension.
    //And it will return an anonymous inner class.
    //New EvaluationContextExtension.
    //Support.
    //And notice and in doing so, you have to implement a method,
    //this is an anonymous inner class.
    //Evaluation context extension support is an abstract class.
    //So you're gonna have to implement the abstract methods in there.
    //The extension ID that we should return here is security.
    //And in order to expose the authentication object we need to override the get
    //RootObject method in this anonymous class.
    //So I will do that as well.
    //I will override the method, the get RootObject method,
    //cool Inside this method we'll grab the authentication data and
    //return a security expression root object exposing all the details for
    //spring security expression of valuation.
    //So here's how that looks.
    //Will create the authentication object.
    //I'll just call mine authentication.
    //And where we're gonna grab it from is the security context holder.
    //So SecurityContextHolder.
    //Right there.
    //Dot getContext.
    //Dot Get authentication.
    //Sweet and then all return a new security expression root.
    //Just like that, using the authentication object that we just grabbed
    //from the security context holder.
    //Now, this is an abstract class.
    //So include the curly braces for the sub class or the implementation here.
    //But there are no methods that are required for you to implement.
    //So you can leave these curly braces empty.
    //Okay, one more task before we fire up this thing.
    //In order to make sure our initial data lines up with this
    //schema that hbm to ddl generates.
    //We better include that user ID column when we insert tasks.


    @Bean
    public EvaluationContextExtension securityExtension() {
        return new EvaluationContextExtensionSupport() {
            @Override
            public String getExtensionId() {
                return "security";
            }

            @Override
            public Object getRootObject() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                return new SecurityExpressionRoot(authentication) {};
            }
        };
    }
}
//native Query:Spring Data JPA interface method that you could use
//inject user-specific data using the authentication object for INSERT statements
    //@Modifying
    //@Transactional
    //@Query(nativeQuery = true, value = "insert into task (user_id,description,complete) values (:#{principal.id},:#{#task.description},:#{#task.complete})")
    //void saveForCurrentUser(@Param("task") Task task);
