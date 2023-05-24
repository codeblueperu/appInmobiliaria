package mac.inmobiliaria.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import mac.inmobiliaria.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

	 @Query(value = "Select * from usuario where rol='VENDEDOR' and nom_completo LIKE %:t%",
	            nativeQuery = true)
	    Page<Usuario> findByNombreCompletoContaining(String t, Pageable p);

	    Optional<Usuario> findByEmail(String e);


	    List<Usuario> findByNombres(String c);
	    
	    Usuario findByCodeVerifyrecoverPassword(int code);

	    @Transactional
	    @Modifying
	    @Query("Update Usuario set sessionLogin=0 where sessionLogin > 0")
	    void actualizarSession(); 

	    boolean existsByEmail(String e);


	    boolean existsByTelefono(String t);


	    default boolean soloLetras(String cadena) {
	    	cadena = cadena.replace(" ", "");
	    	//System.err.println(cadena);
	    	if(!cadena.matches("[a-záÁéÉìÌíÍóÓúÚñÑA-Z()¿?,.]*")) {
	    		return false;
	    	}
//	        for (int i = 0; i < cadena.length(); i++)
//	        {
//	            char caracter = cadena.toUpperCase().charAt(i);
//	            int valorASCII = (int)caracter;
//	            if (valorASCII!=32 &&(valorASCII < 160 || valorASCII >165) &&  (valorASCII < 65 || valorASCII > 90))
//	                return false; //Se ha encontrado un caracter que no es letra
//	        }

	        //Terminado el bucle sin que se haya retornado false, es que todos los caracteres son letras
	        return true;
	    }//Acepte letras mayusculas y minisculas pero con tilde

	    @Query(value = "Select * from usuario where rol='VENDEDOR'",nativeQuery = true)
	    List<Usuario> findByNombresForTipo();

	    @Query("FROM Usuario u  WHERE u.rol='VENDEDOR' AND u.email = ?1")
	    List<Usuario> findNombreCompletoByRolAndEmail(String email);
	    
	    List<Usuario> findNombreCompletoByRol(Usuario.Rol rol);

	    Page<Usuario> findByRol(Usuario.Rol rol, Pageable pageable);

	    Page<Usuario> findByRolAndNombreCompletoContaining(Usuario.Rol rol, String nombreCompleto, Pageable pageable);

	    List<Usuario> findByRol(Usuario.Rol rol);
}
