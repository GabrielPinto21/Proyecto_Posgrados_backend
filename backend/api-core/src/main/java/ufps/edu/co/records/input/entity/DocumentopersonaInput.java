package ufps.edu.co.records.input.entity;

import jakarta.validation.constraints.NotNull;
import ufps.edu.co.records.contracts.CreateType;
import ufps.edu.co.records.contracts.DeleteType;
import ufps.edu.co.records.contracts.FindType;
import ufps.edu.co.records.contracts.PatchType;
import ufps.edu.co.records.contracts.UpdateType;

public class DocumentopersonaInput {
    public record DOCUMENTOPERSONA_CREATE(
            @NotNull String numerodocumento,
            @NotNull Integer idTipodocumento,
            @NotNull Integer idLugarexpedicion) implements CreateType {
    }

    public record DOCUMENTOPERSONA_UPDATE(
            @NotNull Integer id,
            @NotNull String numerodocumento,
            @NotNull Integer idTipodocumento,
            @NotNull Integer idLugarexpedicion) implements UpdateType {
    }

    public record DOCUMENTOPERSONA_PATCH(
            @NotNull Integer id,
            Integer numerodocumento,
            Integer idTipodocumento,
            Integer idLugarexpedicion) implements PatchType {
    }

    public record DOCUMENTOPERSONA_DELETE(
            @NotNull Integer id) implements DeleteType {
    }

    public record DOCUMENTOPERSONA_FIND(
            @NotNull Integer id) implements FindType {
    }
}
