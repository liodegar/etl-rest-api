package com.adverity.etl

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class WebSecurityConfiguration(private val env: Environment): WebSecurityConfigurerAdapter() {

    // Authentication : User --> Roles
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication().passwordEncoder(NoOpPasswordEncoder.getInstance())
                .withUser(env.getProperty("security.user.name"))
                .password(env.getProperty("security.user.password")).roles("USER")
    }

    // Authorization : Role -> Access
    override fun configure(http: HttpSecurity) {
        http.httpBasic().and().authorizeRequests().antMatchers("/v1/**")
                .hasRole("USER").and().csrf().disable().headers().frameOptions().disable()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder? {
        return BCryptPasswordEncoder()
    }
}