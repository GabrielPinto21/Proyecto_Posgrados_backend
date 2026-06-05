package ufps.edu.co.records.input.entity;

import java.time.LocalDate;
import java.math.BigDecimal;
import ufps.edu.co.records.contracts.CreateType;
import ufps.edu.co.records.contracts.DeleteType;
import ufps.edu.co.records.contracts.FindType;
import ufps.edu.co.records.contracts.PatchType;
import ufps.edu.co.records.contracts.UpdateType;
import ufps.edu.co.records.input.entity.UbicacionInput.UBICACION_CREATE;

public enum PersonaInput {
        ;

        public record PERSONA_CREATE(
                        String nombres,
                        String apellidos,
                        String correo,
                        LocalDate fechanacimiento,
                        String celular,
                        String telefono,
                        Integer idUbicacionvivienda,
                        Integer idUbicacionnacimiento,
                        Integer idUbicaciontrabajo,
                        Integer idGenero,
                        Integer idEstadocivil,
                        Integer idGrupoetnico,
                        Integer idPoblacionindigena,
                        Integer idDiscapacidad,
                        Integer idCapacidadexepcional,
                        Integer idDocumentopersona,
                        BigDecimal promediopregrado,
                        String titulopregrado,
                        String titulosposgrados,
                        String empresa,
                        String experiencialaboral,
                        Boolean egresadoufps) implements CreateType {
        }

        public record PERSONA_UPDATE(
                        Integer id,
                        String nombres,
                        String apellidos,
                        String correo,
                        LocalDate fechanacimiento,
                        String celular,
                        String telefono,
                        Integer idUbicacionvivienda,
                        Integer idUbicacionnacimiento,
                        Integer idUbicaciontrabajo,
                        Integer idGenero,
                        Integer idEstadocivil,
                        Integer idGrupoetnico,
                        Integer idPoblacionindigena,
                        Integer idDiscapacidad,
                        Integer idCapacidadexepcional,
                        Integer idDocumentopersona,
                        BigDecimal promediopregrado,
                        String titulopregrado,
                        String titulosposgrados,
                        String empresa,
                        String experiencialaboral,
                        Boolean egresadoufps) implements UpdateType {
        }

        public record PERSONA_PATCH(
                        Integer id,
                        String nombres,
                        String apellidos,
                        String correo,
                        LocalDate fechanacimiento,
                        String celular,
                        String telefono,
                        Integer idUbicacionvivienda,
                        Integer idUbicacionnacimiento,
                        Integer idUbicaciontrabajo,
                        Integer idGenero,
                        Integer idEstadocivil,
                        Integer idGrupoetnico,
                        Integer idPoblacionindigena,
                        Integer idDiscapacidad,
                        Integer idCapacidadexepcional,
                        Integer idDocumentopersona,
                        BigDecimal promediopregrado,
                        String titulopregrado,
                        String titulosposgrados,
                        String empresa,
                        String experiencialaboral,
                        Boolean egresadoufps) implements PatchType {
        }

        public record PERSONA_FULL_CREATE(
                        String nombres,
                        String apellidos,
                        String correo,
                        LocalDate fechanacimiento,
                        String celular,
                        String telefono,
                        Integer idGenero,
                        Integer idEstadocivil,
                        Integer idGrupoetnico,
                        Integer idPoblacionindigena,
                        Integer idDiscapacidad,
                        Integer idCapacidadexepcional,
                        BigDecimal promediopregrado,
                        String titulopregrado,
                        String titulosposgrados,
                        String empresa,
                        String experiencialaboral,
                        Boolean egresadoufps,
                        UBICACION_CREATE ubicacionvivienda,
                        UBICACION_CREATE ubicacionnacimiento,
                        UBICACION_CREATE ubicaciontrabajo,
                        Integer numerodocumento,
                        Integer idTipodocumento,
                        UBICACION_CREATE lugarexpedicion) implements CreateType {
        }

        public record PERSONA_DELETE(
                        Integer id) implements DeleteType {
        }

        public record PERSONA_FIND(
                        Integer id) implements FindType {
        }
}
