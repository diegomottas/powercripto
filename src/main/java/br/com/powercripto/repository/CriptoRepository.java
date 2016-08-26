package br.com.powercripto.repository;

import br.com.powercripto.domain.Cripto;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Cripto entity.
 */
@SuppressWarnings("unused")
public interface CriptoRepository extends JpaRepository<Cripto,Long> {

}
