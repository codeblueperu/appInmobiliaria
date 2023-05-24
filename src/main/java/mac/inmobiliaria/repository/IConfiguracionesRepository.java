package mac.inmobiliaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mac.inmobiliaria.model.Configuraciones;

@Repository
public interface IConfiguracionesRepository extends JpaRepository<Configuraciones, Integer> {

}
