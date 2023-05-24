package mac.inmobiliaria.repository;

import mac.inmobiliaria.model.Asignacion;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AsignacionRepository extends JpaRepository<Asignacion, Integer> {


	Page<Asignacion> findByUsuarioNombreCompletoStartingWithAndEstado(String nombreCompleto,String estado ,Pageable p);
	   
    Page<Asignacion> findByUsuarioNombreCompletoStartingWithAndEstadoAndUserLoginId(String nombreCompleto,String estado ,int idusuario, Pageable p);

    Page<Asignacion> findByEstado(String t, Pageable p);
    
    Page<Asignacion> findByEstadoAndUserLoginId(String t,int idusuario, Pageable p);

    @Query("FROM Asignacion a WHERE a.cliente.nombre=?1")
    Page<Asignacion> findByClienteNombre(String nombreCliente, Pageable p);
    
    Page<Asignacion> findByClienteNombreAndUserLoginId(String nombreCliente,int idusuario, Pageable p);

    Page<Asignacion> findByUsuarioNombreCompleto(String nombres,Pageable p);
    
    Page<Asignacion> findByUsuarioNombreCompletoAndUserLoginId(String nombres,int id,Pageable p);
    
    Page<Asignacion> findByUsuarioId(int idusuario,Pageable p);

    Page<Asignacion> findByUserLoginId(int id,Pageable p);
    
    Page<Asignacion> findByEstadoAndUsuarioId(String t,int id, Pageable p);
    
    Page<Asignacion> findByClienteDni(String nombres,Pageable p);
   
    
    

    List<Asignacion> findByEstadoNot(String estado);
    
    List<Asignacion> findByEstadoNotAndUsuarioId(String estado, int id);
    

    
    

    @Query("FROM Asignacion a WHERE a.usuario.id=?1")
    Page<Asignacion> findByAllAsignacion(Integer id,Pageable pageable);
    

    @Query("FROM Asignacion a WHERE a.usuario.id=?1")
    List<Asignacion> findByAllUsuario(Integer id);
    
    

    @Query("FROM Asignacion a WHERE a.cliente.id=?1")
    List<Asignacion> findByAllCliente(Integer id);


}