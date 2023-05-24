package mac.inmobiliaria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mac.inmobiliaria.model.Distrito;

public interface DistritoRepository extends JpaRepository<Distrito, Integer> {
	
	List<Distrito> findByProvinciaId(int idProvincia);

}
