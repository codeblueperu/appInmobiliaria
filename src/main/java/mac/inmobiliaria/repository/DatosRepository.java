package mac.inmobiliaria.repository;

import mac.inmobiliaria.model.Datos;
import mac.inmobiliaria.model.Usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface DatosRepository extends JpaRepository<Datos, Integer> {

    Page<Datos> findByNombresAndEstado(String t,int  estado, Pageable p);
    
    Page<Datos> findByNombresAndEstadoAndIdUsuario(String t,int  estado,int idUsuario, Pageable p);

    List<Datos> findByNombres(String nomApeCli);
    
    Page<Datos> findByEstado(int  estado, Pageable p);
    
    Page<Datos> findByEstadoAndIdUsuario(int  estado,int idUsuario, Pageable p);

    @Query(value = "select inmu.inmueble_id,inmu.area_Depa,inmu.departamento,inmu.deposito,inmu.estacionamiento,inmu.precio_Depa,inmu.precio_Estaciona,inmu.precio_Total,inmu.proyecto, usu.usuario_id from usuario usu inner join proforma prof on usu.usuario_id = prof.proforma_id inner join pago pag on prof.pago_id=pag.pago_id inner join inmueble inmu on pag.inmueble_id=inmu.inmueble_id where usu.usuario_id LIKE %:id%",
    		countQuery = "select * from datos",
    		nativeQuery = true)
 	Page<Datos> findByAllDatos(Integer id, Pageable pageable);
    
    
    boolean existsByTelefono(String t);


    List<Datos> findByIdUsuarioAndEstado(int idUsuario, int estado);
    
    
}
