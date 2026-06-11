package ufps.edu.co.domain.exceptions.errorcodes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ufps.edu.co.domain.exceptions.BaseErrorCode;

@Getter
@AllArgsConstructor
public enum PersonaErrorCode implements BaseErrorCode {
    PERSONA_CREATION_ERROR(
        "PERSONA_CREATION_ERROR",
        "Error al crear la persona"),

    PERSONA_CORREO_DUPLICADO(
        "PERSONA_CORREO_DUPLICADO",
        "El correo ya existe"),

    PERSONA_MENOR_DE_EDAD(
        "PERSONA_MENOR_DE_EDAD",
        "El aspirante debe ser mayor de edad (18 años)");

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
