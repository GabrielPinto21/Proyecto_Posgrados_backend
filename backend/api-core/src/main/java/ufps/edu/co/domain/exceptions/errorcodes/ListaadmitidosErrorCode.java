package ufps.edu.co.domain.exceptions.errorcodes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ufps.edu.co.domain.exceptions.BaseErrorCode;

@Getter
@AllArgsConstructor
public enum ListaadmitidosErrorCode implements BaseErrorCode {
    DIRECTOR_NO_VALIDO(
            "DIRECTOR_NO_VALIDO",
            "Director no encontrado o sin cargo asignado"),
    COHORTE_NO_PERTENECE_AL_DIRECTOR(
            "COHORTE_NO_PERTENECE_AL_DIRECTOR",
            "La cohorte no pertenece al programa del director de programa"),
    ESTADO_POR_LEGALIZAR_NOT_FOUND(
            "ESTADO_POR_LEGALIZAR_NOT_FOUND",
            "No se encontró el estado POR LEGALIZAR para aspirante en la tabla de estados"),
    NO_ADMITIDOS_EN_COHORTE(
            "NO_ADMITIDOS_EN_COHORTE",
            "No hay aspirantes con estado ADMITIDO en la cohorte especificada"),

    FECHA_INSCRIPCION_NO_FINALIZADA(
            "FECHA_INSCRIPCION_NO_FINALIZADA",
            "No es posible culminar el proceso debido a que aún no ha finalizado la fecha limite para inscripción"),

    FECHA_DOCUMENTACION_NO_FINALIZADA(
            "FECHA_DOCUMENTACION_NO_FINALIZADA",
            "No es posible culminar el proceso debido a que aún no ha finalizado la fecha limite para documentación"),

    FECHA_PAGO_NO_FINALIZADA(
            "FECHA_PAGO_NO_FINALIZADA",
            "No es posible culminar el proceso debido a que aún no ha finalizado la fecha limite para pago"),

    CUPOS_COHORTE_AGOTADOS(
            "CUPOS_COHORTE_AGOTADOS",
            "No se puede admitir al aspirante: la cohorte ya alcanzó el límite de cupos disponibles");

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