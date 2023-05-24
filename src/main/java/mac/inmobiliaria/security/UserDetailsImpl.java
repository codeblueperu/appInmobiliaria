package mac.inmobiliaria.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import mac.inmobiliaria.model.Usuario;



public class UserDetailsImpl implements UserDetails{
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String nombre;
	    private final Usuario usuario;

	    public UserDetailsImpl(Usuario usuario) {

	        nombre = usuario.getNombres();
	        this.usuario = usuario;
	    }

	    @Override
	    public Collection<? extends GrantedAuthority> getAuthorities() {
	        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()));
	    }

	    @Override
	    public String getPassword() {
	        return usuario.getPassword();
	    }

	    @Override
	    public String getUsername() {
	        return usuario.getEmail();
	    }

	    @Override
	    public boolean isAccountNonExpired() {
	        return true;
	    }

	    @Override
	    public boolean isAccountNonLocked() {
	        return true;
	    }

	    @Override
	    public boolean isCredentialsNonExpired() {
	        return true;
	    }

	    @Override
	    public boolean isEnabled() {
	        return true;
	    }

	    public String getNombre() {
	        return nombre;
	    }


	    public Usuario getUsuario() {
	        return usuario;
	    }
}
