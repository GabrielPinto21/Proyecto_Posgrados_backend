package ufps.edu.co.maps.specific;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ufps.edu.co.maps.GlobalMapper;
import ufps.edu.co.records.input.entity.PagoInput.*;
import ufps.edu.co.records.output.entity.PagoOutput;
import ufps.edu.co.rest.dto.PagoDTO;

@Component
public class PagoMap extends
        GlobalMapper<PAGO_CREATE, PAGO_UPDATE, PAGO_DELETE, PAGO_PATCH, PAGO_FIND, PagoOutput, PagoDTO> {

    @Autowired
    private AspiranteMap aspiranteMap;

    @Autowired
    private EstadoMap estadoMap;

    @Autowired
    private PagoconceptoMap pagoconceptoMap;

    public PagoMap() {
        super(PAGO_CREATE.class, PAGO_UPDATE.class, PAGO_DELETE.class, PAGO_PATCH.class,
                PAGO_FIND.class);
    }

    @Override
    protected PagoDTO toDtoCreate(PAGO_CREATE input) {
        return PagoDTO.builder()
                .idAspirante(input.idAspirante())
                .idEstado(input.idEstado())
                .idPagoconcepto(input.idPagoconcepto())
                .build();
    }

    @Override
    protected PagoDTO toDtoUpdate(PAGO_UPDATE input) {
        return PagoDTO.builder()
                .id(input.id())
                .idAspirante(input.idAspirante())
                .idEstado(input.idEstado())
                .idPagoconcepto(input.idPagoconcepto())
                .build();
    }

    @Override
    protected PagoDTO toDtoDelete(PAGO_DELETE input) {
        return PagoDTO.builder()
                .id(input.id())
                .build();
    }

    @Override
    protected PagoDTO toDtoPatch(PAGO_PATCH input) {
        return PagoDTO.builder()
                .id(input.id())
                .idAspirante(input.idAspirante())
                .idEstado(input.idEstado())
                .idPagoconcepto(input.idPagoconcepto())
                .build();
    }

    @Override
    protected PagoDTO toDtoFind(PAGO_FIND input) {
        return PagoDTO.builder()
                .id(input.id())
                .build();
    }

    @Override
    public PagoOutput toOutput(PagoDTO dto) {
        if (dto == null) return null;

        return PagoOutput.builder()
                .id(dto.getId())
                .idAspirante(dto.getIdAspirante())
                .idEstado(dto.getIdEstado())
                .idPagoconcepto(dto.getIdPagoconcepto())
            .valorPagoPesos(null)
            // TODO [PAGO_SCHEMA_FUTURO]: descomentar cuando el DTO incluya estos campos.
            // .urlRecibo(dto.getUrlrecibo())
            // .urlFactura(dto.getUrlfactura())
            // .referenciaPago(dto.getReferenciapago())
            // .valorPago(dto.getValorpago())
                .aspirante(dto.getAspirante() != null ? aspiranteMap.toOutput(dto.getAspirante()) : null)
                .estado(dto.getEstado() != null ? estadoMap.toOutput(dto.getEstado()) : null)
                .pagoconcepto(dto.getPagoconcepto() != null ? pagoconceptoMap.toOutput(dto.getPagoconcepto()) : null)
                .build();
    }

    public List<PagoOutput> toOutputList(List<PagoDTO> dtoList) {
        return dtoList.stream().map(this::toOutput).toList();
    }
}