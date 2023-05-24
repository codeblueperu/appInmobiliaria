package mac.inmobiliaria.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import mac.inmobiliaria.model.Usuario;
import mac.inmobiliaria.repository.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    	
        Usuario usuario = usuarioRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("el usuario no existe: " + email));
        
        
        usuario.setSessionLogin(usuario.getSessionLogin() + 1);
        usuarioRepository.save(usuario);

        return new UserDetailsImpl(usuario);
    }
}
