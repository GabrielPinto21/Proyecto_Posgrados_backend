package ufps.edu.co.domain.exceptions.errorcodes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ufps.edu.co.domain.exceptions.BaseErrorCode;

@Getter
@AllArgsConstructor
public enum CohorteErrorCode implements BaseErrorCode {
    COHORTE_NOT_FOUND(
            "COHORTE_NOT_FOUND",
            "Cohorte no encontrada"),
    COHORTE_DUPLICADO(
            "COHORTE_DUPLICADO",
            "Cohorte duplicada"),
    COHORTE_SEMESTRE_NO_VALIDO_CONFLICT(
            "COHORTE_SEMESTRE_NO_VALIDO_CONFLICT",
            "La cohorte solo puede crearse en un semestre EN CURSO o PROGRAMADO"),
    COHORTE_CON_ASIGNACIONES_BLOQUEADAS(
            "COHORTE_CON_ASIGNACIONES_BLOQUEADAS",
            "No se pueden eliminar asignaciones porque existen documentos cargados que dependen de ellas"),
    COHORTE_ID_REQUERIDO(
            "COHORTE_ID_REQUERIDO",
            "Se debe enviar idCohorte en el formulario de inscripción"),
    COHORTE_NO_ABIERTA_FORBIDDEN(
            "COHORTE_NO_ABIERTA_FORBIDDEN",
            "La cohorte indicada no está abierta"),
    COHORTE_SIN_PROGRAMA_CONFLICT(
            "COHORTE_SIN_PROGRAMA_CONFLICT",
            "La cohorte indicada no tiene programa asociado"),
    COHORTE_CON_DEPENDENCIAS_CONFLICT(
            "COHORTE_CON_DEPENDENCIAS_CONFLICT",
            "No se puede eliminar la cohorte porque tiene registros dependientes asociados (criterios de cohorte u otros)");

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
