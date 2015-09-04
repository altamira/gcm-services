package br.com.altamira.message.gcm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
/*import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;*/

/*@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	//This reads, invoke this configuration only when
        //the pattern "/api/notify" is found.
        //When this configuration is invoked, restrict access,
        //disable CSRF, and if the path matches "/api/notify" permit all.
        http.antMatcher("/api")
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/api")
            .permitAll();

        //Your other configuration...
        http
            .authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers("/main").permitAll()
            .antMatchers("/lib").permitAll()
            .antMatchers("/images").permitAll()
            .anyRequest().authenticated();
        http
            .formLogin()
                .defaultSuccessUrl("/home")
            .failureUrl("/login?error")
            .loginPage("/login")
            .permitAll()
            .and()
            .logout()
            .permitAll();

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER");
    }
}*/