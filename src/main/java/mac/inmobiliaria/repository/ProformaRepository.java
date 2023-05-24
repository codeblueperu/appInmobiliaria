package mac.inmobiliaria.repository;


import mac.inmobiliaria.model.Asignacion;
import mac.inmobiliaria.model.Proforma;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProformaRepository extends JpaRepository<Proforma, Integer> {

    @Query(value = "select prof.proforma_id,prof.fecha_Proforma,prof.entero_Proyecto,pag.pago_id,prof.usuario_id from proforma prof inner join usuario usu on prof.usuario_id = usu.usuario_id inner join pago pag on prof.pago_id=pag.pago_id inner join inmueble inm on pag.inmueble_id=inm.inmueble_id inner join datos dat on pag.datos_id = dat.datos_id where dat.dni LIKE %:dni%",
            countQuery = "select * from proforma",
            nativeQuery = true)
    Page<Proforma> findByPagoDatosDni(String dni, Pageable p);


    @Query("FROM Proforma a WHERE a.usuario.id=?1")
    Page<Proforma> findByAllProforma(Integer id,Pageable pageable);

    
    @Query("FROM Proforma a WHERE a.pago.id=?1")
    List<Proforma> findByAllProformas(Integer id);
    
    @Query("FROM Proforma a WHERE a.usuario.id=?1")
    List<Proforma> findByAllProformasUsuario(Integer id);
}
