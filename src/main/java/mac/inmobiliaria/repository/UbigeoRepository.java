package mac.inmobiliaria.repository;

import java.util.List;

import mac.inmobiliaria.model.Ubigeo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface UbigeoRepository extends JpaRepository<Ubigeo, Integer> {
    Page<Ubigeo> findByDistritoContaining(String t, Pageable p);

    default boolean soloLetras(String cadena) {
    	cadena = cadena.replace(" ", "");
    	//System.err.println(cadena);
    	if(!cadena.matches("[a-záÁéÉìÌíÍóÓúÚñÑA-Z()¿?,.]*")) {
    		return false;
    	}
//        for (int i = 0; i < cadena.length(); i++)
//        {
//            char caracter = cadena.toUpperCase().charAt(i);
//            int valorASCII = (int)caracter;
//            if (valorASCII!=32 &&(valorASCII < 160 || valorASCII >165) &&  (valorASCII < 65 || valorASCII > 90))
//                return false; //Se ha encontrado un caracter que no es letra
//        }

        //Terminado el bucle sin que se haya retornado false, es que todos los caracteres son letras
        return true;
    }
    List<Ubigeo> findByDistrito(String c);
}
