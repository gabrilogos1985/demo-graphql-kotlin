package mx.gps.demographql.config

import org.springframework.security.authorization.AuthorityReactiveAuthorizationManager.hasRole
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order


@Configuration
@Order(SecurityProperties.BASIC_AUTH_ORDER - 10)
class ApplicationConfigurerAdapter : WebSecurityConfigurerAdapter() {
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.antMatcher("/graphql/**").anonymous()
                .and().antMatcher("/").authorizeRequests()
                .antMatchers("/foo/bar").hasRole("BAR")
                .anyRequest().authenticated();
    }
}