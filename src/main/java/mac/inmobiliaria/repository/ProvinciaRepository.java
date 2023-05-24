package mac.inmobiliaria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mac.inmobiliaria.model.Provincia;

public interface ProvinciaRepository extends JpaRepository<Provincia, Integer> {

	
	
	List<Provincia> findByDepartamentoId(int idDepartamento);
}
