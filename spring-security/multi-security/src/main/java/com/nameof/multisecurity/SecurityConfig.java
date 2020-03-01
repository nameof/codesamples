package com.nameof.multisecurity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * #{@link ApiSecurityConfig} 处理/api/**，基于密钥认证,stateless
 * #{@link FormSecurityConfig} 处理除/api/**以外的，基于表单登录认证
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Configuration
    @Order(1)
    public static class ApiSecurityConfig extends WebSecurityConfigurerAdapter
    {
        @Override
        protected void configure(HttpSecurity http) throws Exception
        {
            http.csrf().disable()
                    .antMatcher("/api/**") //仅处理/api/**
                    .authorizeRequests().anyRequest()
                    .authenticated();

            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            http.addFilter(new KeyAuthorizationFilter(authenticationManager()));
        }
    }

    @Configuration
    @Order(2)
    public static class FormSecurityConfig extends WebSecurityConfigurerAdapter {
        @Autowired
        private UserDetailsService userDetailsService;

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers("/login", "/**/*.css", "/**/*.js", "/**/*.png");
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.cors().and().csrf().disable();
            http.authorizeRequests().anyRequest().authenticated();
            http.httpBasic();
            http.exceptionHandling().accessDeniedHandler((httpServletRequest, httpServletResponse, e) -> {
                httpServletResponse.setStatus(403);
            });
            http.exceptionHandling().authenticationEntryPoint((httpServletRequest, httpServletResponse, e) -> {
                if (isAjaxRequest(httpServletRequest))
                    httpServletResponse.setStatus(401);
                else
                    httpServletResponse.sendRedirect("/login");
            });

            //另一种方式是只配置这一个FormSecurityConfig，并在此处add KeyAuthorizationFilter，在KeyAuthorizationFilter内部判断当前请求是不是/api/**进行处理
            //配2个WebSecurityConfigurerAdapter：springsecurity内部判断调用哪一个，session、拦截、csrf等互不干扰，独立配置
            //配1个，自行使用Filter判断不同URL认证的不同处理，sessionCreationPolicy、拦截、csrf只能有一种
        }

        private boolean isAjaxRequest(HttpServletRequest httpServletRequest) {
            return false;
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
        }
    }

    @Bean
    public HttpSessionListener sessionListener() {
        return new HttpSessionListener() {
            @Override
            public void sessionCreated(HttpSessionEvent se) {
                logger.info("session created");
            }

            @Override
            public void sessionDestroyed(HttpSessionEvent se) {
                logger.info("session destroyed");
            }
        };
    }
}
