package cl.plataforma_gimnasio.ms_pago.repository;

import cl.plataforma_gimnasio.ms_pago.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {
}