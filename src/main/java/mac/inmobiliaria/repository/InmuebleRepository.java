package mac.inmobiliaria.repository;

import mac.inmobiliaria.model.Inmueble;
import mac.inmobiliaria.model.Pago;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InmuebleRepository extends JpaRepository<Inmueble, Integer> {

    List<Inmueble> findByDepartamento(String departamento);

    Page<Inmueble> findByDepartamentoAndEstado(String departamento, int estado, Pageable page);
    
    Page<Inmueble> findByEstado(int estado, Pageable page);
    
    
    List<Inmueble> findByEstado(int estado);

    //Page<Inmueble> findByDatosNombres(String departamento, Pageable page);
    
//    @Query(value = "select inmu.inmueble_id,inmu.area_Depa,inmu.departamento,inmu.deposito,inmu.estacionamiento,inmu.precio_Depa,inmu.precio_Estaciona,inmu.precio_Total,inmu.proyecto, usu.usuario_id from usuario usu inner join proforma prof on usu.usuario_id = prof.proforma_id inner join pago pag on prof.pago_id=pag.pago_id inner join inmueble inmu on pag.inmueble_id=inmu.inmueble_id where usu.usuario_id LIKE %:id%",
//            countQuery = "select * from inmueble",
//            nativeQuery = true)
//    Page<Inmueble> findByAllInmuebles(Integer id, Pageable pageable);
}
