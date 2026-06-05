package ufps.edu.co.rest.dto;

public interface CohorteCountsProjection {
    Integer getIdCohorte();
    Long getTotalInscritos();
    Long getTotalNoConfirmados();
    Long getTotalConfirmados();
    Long getTotalPazysalvo();
    Long getTotalValidados();
    Long getTotalCalificados();
    Long getTotalAdmitidos();
    Long getTotalLegalizados();
}
