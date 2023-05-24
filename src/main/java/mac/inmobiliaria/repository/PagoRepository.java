package mac.inmobiliaria.repository;


import mac.inmobiliaria.model.Datos;
import mac.inmobiliaria.model.Pago;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {
    List<Pago> findByNombreBanco(String nomBanco);

    Page<Pago> findByDatosDni(String dni, Pageable p);
    
    Page<Pago> findByDatosDniAndIdUsuario(String dni, int idUsuario, Pageable p);
   
    @Query("FROM Pago c WHERE c.inmueble.id=?1")
    List<Pago> findByAllInmueble(Integer id);
    
    Page<Pago> findByIdUsuario(int idUsuario, Pageable p);

    
    List<Pago> findAllByDatosNombresAndIdUsuario(String listaDatos,int id);
}		