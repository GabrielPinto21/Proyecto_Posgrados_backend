package ufps.edu.co.Service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import ufps.edu.co.DTO.ReciboInscripcionInputDTO;
import ufps.edu.co.services.S3Service;

public class ReciboServiceTest {

    @Test
    public void generarRecibo_localFile() throws Exception {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false);

        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);

        // S3Service stub that writes the file to the test directory
        S3Service testS3 = new S3Service() {
            @Override
            public UploadResult uploadBytes(byte[] content, String contentType, String originalName) {
                try {
                    java.io.File out = new java.io.File("backend/check-builder/src/test/java/ufps/edu/co/Service/recibo_test_output.pdf");
                    if (out.getParentFile() != null && !out.getParentFile().exists()) {
                        out.getParentFile().mkdirs();
                    }
                    java.nio.file.Files.write(out.toPath(), content);
                    return new UploadResult(out.getName(), out.getAbsolutePath());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };

        ReciboService service = new ReciboService(engine, testS3);

        ReciboInscripcionInputDTO input = ReciboInscripcionInputDTO.builder()
                .fechaGeneracion("Sábado 12 de Junio de 2026")
                .codigoRecibo("TEST-12345")
                .periodo("20261")
                .version("V.1.0")
                .nit("Nit. 890500622-6")
                .programa("Ingeniería de Sistemas")
                .nombre("TEST NOMBRE")
                .tipoPersona("Estudiante")
                .documento("1152370")
                .correo("test@example.com")
                .tipoPago("Inscripción a Posgrados")
                .fechaLimite("31/05/2026")
                .concepto("Derechos de inscripción posgrado")
                .valor("$70,000")
                .total("$70,000")
                .convenioBancolombia("Convenio 61003")
                .convenioBbva("Convenio 4668")
                .barcodeDato("(415)7709998005938(8020)821111(3900)00070000(96)20260531")
                .build();

        S3Service.UploadResult result = service.construirYSubirRecibo(input);
        System.out.println("Recibo generado en: " + result.enlaceurl());
        assertTrue(new java.io.File(result.enlaceurl()).exists());
    }
}
