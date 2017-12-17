package com.teamtreehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan(basePackages = "com.teamtreehouse.todotoday")
public class Application {
    public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}

// rely on a session attribute to save the flash message.
// Session attributes are server-stored values, and can
// even include rich objects. When session attributes are
// used, you'll notice that a session cookie
// (usually called JSESSIONID) is set in the browser and
// passed with every request, so that the server can pair
// the browsing session with the server-stored session data.


