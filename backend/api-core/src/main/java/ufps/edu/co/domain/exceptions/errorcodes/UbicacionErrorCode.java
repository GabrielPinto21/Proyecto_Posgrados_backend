package ufps.edu.co.domain.exceptions.errorcodes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ufps.edu.co.domain.exceptions.BaseErrorCode;

@Getter
@AllArgsConstructor
public enum UbicacionErrorCode implements BaseErrorCode {
    UBICACION_YA_EXISTE_CONFLICT(
        "UBICACION_YA_EXISTE_CONFLICT", 
        "La ubicación ya existe"),
        
    UBICACION_ES_NULA_O_VACIA(
        "UBICACION_ES_NULA_O_VACIA", 
        "La ubicación no puede ser nula o vacía");

    private final String code;
    private final String defaultMessage;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDefaultMessage() {
        return defaultMessage;
    }
}
