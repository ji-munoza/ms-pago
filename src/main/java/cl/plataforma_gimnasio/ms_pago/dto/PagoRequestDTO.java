package cl.plataforma_gimnasio.ms_pago.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoRequestDTO {

    @NotNull(message = "El monto del pago es obligatorio.")
    @Min(value = 5000, message = "El monto minimo de pago debe ser de $5.000.")
    @Max(value = 2000000, message = "El monto maximo de pago no puede superar los $2.000.000.")
    private Integer montoPago;

    @NotBlank(message = "El método de pago es obligatorio.")
    @Size(min = 3, max = 50, message = "El metodo de pago debe tener entre 3 y 50 caracteres.")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El metodo de pago solo puede contener letras y espacios.")
    private String metodoPago;

    @NotBlank(message = "El estado del pago es obligatorio.")
    @Size(min = 3, max = 50, message = "El estado del pago debe tener entre 3 y 50 caracteres.")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El estado del pago solo puede contener letras y espacios.")
    private String estadoPago;
}