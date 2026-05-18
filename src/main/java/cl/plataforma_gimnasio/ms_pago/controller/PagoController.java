package cl.plataforma_gimnasio.ms_pago.controller;

import cl.plataforma_gimnasio.ms_pago.dto.PagoRequestDTO;
import cl.plataforma_gimnasio.ms_pago.dto.PagoResponseDTO;
import cl.plataforma_gimnasio.ms_pago.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gimnasio/pagos")
@RequiredArgsConstructor
@Slf4j
public class PagoController {

    private final PagoService pagoService;

    @GetMapping
    public ResponseEntity<List<PagoResponseDTO>> obtenerTodos() {
        log.info("Solicitud recibida para obtener todos los pagos");
        List<PagoResponseDTO> pagos = pagoService.obtenerTodos();
        if (pagos.isEmpty()) {
            log.warn("No se obtuvo ningun pago, la lista esta vacia");
            return ResponseEntity.noContent().build();
        }
        log.info("Obtenidos {} pagos", pagos.size());
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoResponseDTO> obtenerPorId(@PathVariable Integer id) {
        log.info("Solicitud recibida para obtener pago con ID: {}", id);
        PagoResponseDTO pago = pagoService.obtenerPorId(id);
        log.info("Obtenido pago con ID: {}", id);
        return ResponseEntity.ok(pago);
    }

    @PostMapping
    public ResponseEntity<PagoResponseDTO> guardar(@Valid @RequestBody PagoRequestDTO dto) {
        log.info("Solicitud recibida para crear pago por monto: ${}", dto.getMontoPago());
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.guardar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagoResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody PagoRequestDTO dto) {
        log.info("Solicitud recibida para actualizar pago con ID: {}", id);
        PagoResponseDTO pagoActualizado = pagoService.actualizar(id, dto);
        log.info("Pago con ID {} modificado exitosamente.", id);
        return ResponseEntity.ok(pagoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.warn("Solicitud recibida para eliminar pago con ID: {}", id);
        pagoService.eliminar(id);
        log.info("Eliminado pago con ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}