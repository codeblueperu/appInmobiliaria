package mac.inmobiliaria.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	 @Bean
	    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		 http
		         .csrf()
		         .disable()
		         .httpBasic()
		         .and()
		         .authorizeRequests()
		         .antMatchers("/assets/**").permitAll()
		         .antMatchers("/create/count/user").permitAll()
		         .antMatchers("/admin/**").hasRole("ADMIN")
		         .antMatchers("/perfil/**").hasRole("ADMIN")
		         .antMatchers("/perfil/**").hasRole("VENDEDOR")
		         .antMatchers("/listaAsignaciones/**").hasRole("VENDEDOR")
		         .antMatchers("/pagos/**").hasRole("VENDEDOR")
		         .antMatchers("/proforma/**").hasRole("VENDEDOR")
		         .antMatchers("/inmueble/**").hasRole("VENDEDOR")
		         .antMatchers("/datos/**").hasRole("VENDEDOR")
		         .antMatchers("/usuarios/**", "/ubigeos/**","/perfil/**", "/clientes/**", "/asignaciones/**","/administradores**")
		         .authenticated()
		         .and()
		         .formLogin()
		         .loginPage("/login")
		         .defaultSuccessUrl("/home", true)
		         .permitAll()
		         .failureUrl("/login?error=true")
		         .and() .rememberMe().key("rememberMeKey").tokenValiditySeconds(3600) // 1 hora
		         .userDetailsService(userDetailsService)
				 .and().logout(logout -> logout
		         .logoutRequestMatcher(new AntPathRequestMatcher("/logoutuser", HttpMethod.GET.name()))
		         .logoutSuccessUrl("/login"));
		        http.headers().frameOptions().disable();

	        return http.build();
	    }

}
