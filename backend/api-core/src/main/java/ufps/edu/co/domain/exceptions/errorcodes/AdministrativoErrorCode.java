package ufps.edu.co.domain.exceptions.errorcodes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ufps.edu.co.domain.exceptions.BaseErrorCode;

@Getter
@AllArgsConstructor
public enum AdministrativoErrorCode implements BaseErrorCode {
    ADMINISTRATIVO_DERIVACION_FORBIDDEN(
            "ADMINISTRATIVO_DERIVACION_FORBIDDEN",
            "No se pudo derivar el administrativo desde el usuario autenticado"),
    ADMINISTRATIVO_SIN_PROGRAMA_FORBIDDEN(
            "ADMINISTRATIVO_SIN_PROGRAMA_FORBIDDEN",
            "El usuario autenticado no tiene un programa asignado");

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
