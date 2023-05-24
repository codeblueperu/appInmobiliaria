package mac.inmobiliaria.repository;

import mac.inmobiliaria.model.Cliente;
import mac.inmobiliaria.model.Usuario;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    Page<Cliente> findByNombreContainingAndEstado(String t,int estado, Pageable p);
    
    Page<Cliente> findByNombreContainingAndUsuarioIdAndEstado(String t,int estado,int idusuario, Pageable p);

    Optional<Cliente> findById(Integer id);

    List<Cliente> findByNombre(String nombre);

    @Query("FROM Cliente c WHERE c.ubigeo.id=?1")
    List<Cliente> findByAllCliente(Integer id);

    Page<Cliente> findByTipoAndEstado(String t, int estado, Pageable p);
    
    Page<Cliente> findByTipoAndUsuarioIdAndEstado(String t, int estado,int idusuario, Pageable p);
    
    Page<Cliente> findByEstado(int estado, Pageable p);
    
    List<Cliente> findByUsuarioId(Integer id);
    
    List<Cliente> findByUsuarioAndEstado(Usuario usuarioOrigen, int estado);
    
    Page<Cliente> findByEstadoAndUsuarioId(int estado,int idusuario, Pageable p);

   @Query(value = "SELECT *FROM cliente LEFT JOIN asignacion ON cliente.cliente_id = asignacion.cliente_id WHERE asignacion.asignacion_id IS NULL",nativeQuery = true)
   List<Cliente> findByClienteAsignacionNoNull();

}
