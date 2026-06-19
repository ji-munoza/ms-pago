package cl.plataforma_gimnasio.ms_pago.service;

import cl.plataforma_gimnasio.ms_pago.dto.PagoRequestDTO;
import cl.plataforma_gimnasio.ms_pago.dto.PagoResponseDTO;
import cl.plataforma_gimnasio.ms_pago.exception.ResourceNotFoundException;
import cl.plataforma_gimnasio.ms_pago.model.Pago;
import cl.plataforma_gimnasio.ms_pago.repository.PagoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias para PagoService")
class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @InjectMocks
    private PagoService pagoService;

    private Pago pagoMock;
    private PagoRequestDTO pagoRequestDTO;

    @BeforeEach
    void setUp() {
        pagoMock = new Pago();
        pagoMock.setIdPago(1);
        pagoMock.setMontoPago(25000);
        pagoMock.setMetodoPago("EFECTIVO");
        pagoMock.setEstadoPago("APROBADO");
        pagoMock.setFechaHoraPago(LocalDateTime.now());

        pagoRequestDTO = new PagoRequestDTO(25000, "efectivo", "aprobado");
    }

    @Test
    @DisplayName("Debe retornar todos los pagos registrados")
    void obtenerTodos_DebeRetornarListaDePagos() {
        when(pagoRepository.findAll()).thenReturn(List.of(pagoMock));

        List<PagoResponseDTO> resultado = pagoService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(25000, resultado.get(0).getMontoPago());
        assertEquals("EFECTIVO", resultado.get(0).getMetodoPago());
        verify(pagoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe retornar una lista vacía cuando no existen pagos")
    void obtenerTodos_DebeRetornarListaVacia_CuandoNoHayRegistros() {
        when(pagoRepository.findAll()).thenReturn(Collections.emptyList());

        List<PagoResponseDTO> resultado = pagoService.obtenerTodos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(pagoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe retornar el pago correspondiente cuando el ID existe")
    void obtenerPorId_DebeRetornarPago_CuandoIdExiste() {
        when(pagoRepository.findById(1)).thenReturn(Optional.of(pagoMock));

        PagoResponseDTO resultado = pagoService.obtenerPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdPago());
        assertEquals("APROBADO", resultado.getEstadoPago());
        verify(pagoRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException cuando el ID no existe al buscar")
    void obtenerPorId_DebeLanzarExcepcion_CuandoIdNoExiste() {
        when(pagoRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> pagoService.obtenerPorId(99));
        verify(pagoRepository, times(1)).findById(99);
    }

    @Test
    @DisplayName("Debe guardar un pago de forma exitosa y transformar textos a mayúsculas")
    void guardar_DebeRegistrarPagoExitosamente() {
        when(pagoRepository.save(any(Pago.class))).thenReturn(pagoMock);

        PagoResponseDTO resultado = pagoService.guardar(pagoRequestDTO);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdPago());
        assertEquals("EFECTIVO", resultado.getMetodoPago());
        assertEquals("APROBADO", resultado.getEstadoPago());
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    @DisplayName("Debe actualizar un pago existente de forma exitosa")
    void actualizar_DebeModificarPago_CuandoIdExiste() {
        when(pagoRepository.findById(1)).thenReturn(Optional.of(pagoMock));
        when(pagoRepository.save(any(Pago.class))).thenReturn(pagoMock);

        PagoResponseDTO resultado = pagoService.actualizar(1, pagoRequestDTO);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdPago());
        verify(pagoRepository, times(1)).findById(1);
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException cuando el ID no existe al actualizar")
    void actualizar_DebeLanzarExcepcion_CuandoIdNoExiste() {
        when(pagoRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> pagoService.actualizar(99, pagoRequestDTO));
        verify(pagoRepository, times(1)).findById(99);
        verify(pagoRepository, never()).save(any(Pago.class));
    }

    @Test
    @DisplayName("Debe eliminar un pago cuando el ID existe")
    void eliminar_DebeBorrarPago_CuandoIdExiste() {
        when(pagoRepository.existsById(1)).thenReturn(true);
        doNothing().when(pagoRepository).deleteById(1);

        assertDoesNotThrow(() -> pagoService.eliminar(1));
        verify(pagoRepository, times(1)).existsById(1);
        verify(pagoRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException cuando el ID no existe al eliminar")
    void eliminar_DebeLanzarExcepcion_CuandoIdNoExiste() {
        when(pagoRepository.existsById(99)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> pagoService.eliminar(99));
        verify(pagoRepository, times(1)).existsById(99);
        verify(pagoRepository, never()).deleteById(anyInt());
    }
}