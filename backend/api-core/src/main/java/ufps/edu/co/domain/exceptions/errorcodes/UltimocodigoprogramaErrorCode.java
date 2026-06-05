package ufps.edu.co.domain.exceptions.errorcodes;

import lombok.*;
import ufps.edu.co.domain.exceptions.*;

@Getter
@AllArgsConstructor
public enum UltimocodigoprogramaErrorCode implements BaseErrorCode {
    ULTIMOCODIGOPROGRAMA_NOT_FOUND(
            "ULTIMOCODIGOPROGRAMA_NOT_FOUND",
            "Registro de último código de programa no encontrado"),
    ULTIMOCODIGOPROGRAMA_ALREADY_EXISTS_CONFLICT(
            "ULTIMOCODIGOPROGRAMA_ALREADY_EXISTS_CONFLICT",
            "Ya existe un registro de código para este programa");

    private final String code;
    private final String defaultMessage;
}
