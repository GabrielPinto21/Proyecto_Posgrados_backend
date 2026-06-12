package ufps.edu.co.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ufps.edu.co.DTO.ReciboInscripcionInputDTO;
import ufps.edu.co.processor.receipt.ReciboInscripcionBuildInput;
import ufps.edu.co.processor.receipt.ReciboInscripcionBuilderPort;
import ufps.edu.co.services.S3Service;

@Service
public class ReciboInscripcionBuilderAdapter implements ReciboInscripcionBuilderPort {

    private static final Logger log = LoggerFactory.getLogger(ReciboInscripcionBuilderAdapter.class);

    private static final Locale LOCALE_ES_CO = Locale.forLanguageTag("es-CO");
    private static final String VERSION = "V. 1.0";
    private static final String NIT = "Nit. 890500622-6";

    @Value("${BANK_META_INFO_BANCOLOMBIA}")
    private String convenioBancolombiaCode;

    @Value("${BANK_META_INFO_BBVA}")
    private String convenioBbvaCode;

    @Value("${BANK_META_INFO_GLOBALID_CODE}")
    private String bankMeta415;

    @Value("${BANK_META_INFO_REFERENCE_CODE}")
    private String bankMeta8020;

    @Value("${BANK_META_INFO_VALUE_CODE}")
    private String bankMeta3900;

    @Value("${BANK_META_INFO_EXPIREDATE_CODE}")
    private String bankMeta96;

    private final ReciboService reciboService;

    public ReciboInscripcionBuilderAdapter(ReciboService reciboService) {
        this.reciboService = reciboService;
    }

    @Override
    public String construirYSubirRecibo(ReciboInscripcionBuildInput input) {
        try {
            log.info("Construyendo recibo en memoria codigoRecibo={} referencia={} monto={} centavos={}",
                    input.codigoRecibo(), input.referenciaPago(), input.valor(), input.valorCentavos());
            ReciboInscripcionInputDTO payload = ReciboInscripcionInputDTO.builder()
                    .fechaGeneracion(formatearFechaLarga(input.fechaGeneracion()))
                    .codigoRecibo(input.codigoRecibo())
                    .periodo(valorConFallback(input.periodo(), String.valueOf(LocalDate.now().getYear())))
                    .version(VERSION)
                    .nit(NIT)
                    .programa(valorConFallback(input.programa(), "POSGRADOS"))
                    .nombre(valorConFallback(input.nombreCompleto(), "ASPIRANTE"))
                    .tipoPersona(valorConFallback(input.tipoPersona(), "Aspirante"))
                    .documento(valorConFallback(input.documento(), "N/A"))
                    .correo(valorConFallback(input.correo(), "N/A"))
                    .tipoPago("Inscripción a Posgrados")
                    .fechaLimite(formatearFechaLarga(input.fechaLimite()))
                    .concepto(valorConFallback(input.concepto(), "Derechos de inscripción posgrado"))
                    .valor(formatearMoneda(input.valor()))
                    .total(formatearMoneda(input.valor()))
                    .convenioBancolombia("Convenio " + convenioBancolombiaCode)
                    .convenioBbva("Convenio " + convenioBbvaCode)
                    .barcodeDato(construirBarcode(input))
                    .build();

            S3Service.UploadResult result = reciboService.construirYSubirRecibo(payload);
            log.info("Recibo construido y subido codigoRecibo={} key={} url={}", input.codigoRecibo(),
                    result != null ? result.keyfile() : null, result != null ? result.enlaceurl() : null);
            return result != null ? result.enlaceurl() : null;
        } catch (Exception e) {
            log.error("Error construyendo/subiendo recibo codigoRecibo={} referencia={}", input.codigoRecibo(),
                    input.referenciaPago(), e);
            throw new RuntimeException("No se pudo construir/subir el recibo de inscripción", e);
        }
    }

        private String construirBarcode(ReciboInscripcionBuildInput input) {
        // Use the amount in pesos (no centavos) for the barcode field, padded to 8 digits.
        BigDecimal valorPesosBD = input.valor() != null ? input.valor() : BigDecimal.ZERO;
        long pesos = valorPesosBD.setScale(0, RoundingMode.HALF_UP).longValue();
        String fecha = input.fechaLimite() != null
            ? input.fechaLimite().format(DateTimeFormatter.BASIC_ISO_DATE)
            : LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String referencia = valorConFallback(input.referenciaPago(), input.codigoRecibo());
        // Build GS1-like barcode using configured meta info parts
        // (415)<global payer id>(8020)<reference>(3900)<amount>(96)<date>
        String part415 = bankMeta415 != null ? bankMeta415 : "";
        String tag8020 = bankMeta8020 != null ? bankMeta8020 : "8020";
        String tag3900 = bankMeta3900 != null ? bankMeta3900 : "3900";
        String tag96 = bankMeta96 != null ? bankMeta96 : "96";
        // Pad amount in pesos to 8 digits with leading zeros for consistency (e.g. 00070000 for 70,000)
        String amountPadded = String.format("%08d", pesos);
        return "(415)" + part415 + "(" + tag8020 + ")" + referencia + "(" + tag3900 + ")" + amountPadded
            + "(" + tag96 + ")" + fecha;
        }

    private String formatearFechaLarga(LocalDateTime fecha) {
        if (fecha == null) {
            return formatearFechaLarga(LocalDate.now());
        }
        return formatearFechaLarga(fecha.toLocalDate());
    }

    private String formatearFechaLarga(LocalDate fecha) {
        LocalDate value = fecha != null ? fecha : LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy", LOCALE_ES_CO);
        return value.format(formatter);
    }

    private String formatearMoneda(BigDecimal valor) {
        NumberFormat currency = NumberFormat.getCurrencyInstance(LOCALE_ES_CO);
        BigDecimal value = valor != null ? valor : BigDecimal.ZERO;
        return currency.format(value);
    }

    private String valorConFallback(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}