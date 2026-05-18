package cl.plataforma_gimnasio.ms_pago.service;

import cl.plataforma_gimnasio.ms_pago.dto.PagoRequestDTO;
import cl.plataforma_gimnasio.ms_pago.dto.PagoResponseDTO;
import cl.plataforma_gimnasio.ms_pago.exception.ResourceNotFoundException;
import cl.plataforma_gimnasio.ms_pago.model.Pago;
import cl.plataforma_gimnasio.ms_pago.repository.PagoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PagoService {

    private final PagoRepository pagoRepository;

    public List<PagoResponseDTO> obtenerTodos() {
        log.info("Iniciando obtencion de todos los pagos");
        return pagoRepository.findAll().stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public PagoResponseDTO obtenerPorId(Integer id) {
        log.info("Iniciando obtencion de pago con ID: {}", id);
        return pagoRepository.findById(id)
                .map(this::convertirAResponseDTO)
                .orElseThrow(() -> {
                    log.error("Error al obtener pago: El ID {} no existe.", id);
                    return new ResourceNotFoundException("El pago con ID " + id + " no existe.");
                });
    }

    public PagoResponseDTO guardar(PagoRequestDTO dto) {
        log.info("Iniciando registro de nuevo pago por un monto de ${} via {}", dto.getMontoPago(), dto.getMetodoPago());

        Pago pago = new Pago();
        pago.setMontoPago(dto.getMontoPago());
        pago.setMetodoPago(dto.getMetodoPago().toUpperCase());
        pago.setEstadoPago(dto.getEstadoPago().toUpperCase());
        pago.setFechaHoraPago(LocalDateTime.now());

        Pago pagoGuardado = pagoRepository.save(pago);
        log.info("Pago guardado con exito. Nuevo ID asignado: {}", pagoGuardado.getIdPago());

        return convertirAResponseDTO(pagoGuardado);
    }

    public PagoResponseDTO actualizar(Integer id, PagoRequestDTO dto) {
        log.info("Iniciando actualizacion de pago con ID: {}", id);

        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Error al actualizar: El ID {} no existe.", id);
                    return new ResourceNotFoundException("El pago con ID " + id + " no existe.");
                });

        pago.setMontoPago(dto.getMontoPago());
        pago.setMetodoPago(dto.getMetodoPago().toUpperCase());
        pago.setEstadoPago(dto.getEstadoPago().toUpperCase());

        Pago pagoActualizado = pagoRepository.save(pago);
        log.info("Pago con ID {} actualizado con exito.", id);

        return convertirAResponseDTO(pagoActualizado);
    }

    public void eliminar(Integer id) {
        log.warn("Iniciando eliminacion de pago con ID: {}", id);

        if (!pagoRepository.existsById(id)) {
            log.error("Error al eliminar: No existe el pago con ID: {}", id);
            throw new ResourceNotFoundException("El pago con ID " + id + " no existe.");
        }

        pagoRepository.deleteById(id);
        log.info("Pago con ID {} eliminado correctamente.", id);
    }

    private PagoResponseDTO convertirAResponseDTO(Pago pago) {
        PagoResponseDTO response = new PagoResponseDTO();
        response.setIdPago(pago.getIdPago());
        response.setMontoPago(pago.getMontoPago());
        response.setMetodoPago(pago.getMetodoPago());
        response.setEstadoPago(pago.getEstadoPago());
        response.setFechaHoraPago(pago.getFechaHoraPago());
        return response;
    }
}