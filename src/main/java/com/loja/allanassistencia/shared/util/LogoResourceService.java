package com.loja.allanassistencia.shared.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Component
public class LogoResourceService {

    private static final String LOGO_PATH = "static/img/allanlogo.png";

    private String logoBase64Cache;

    public String obterLogoBase64() {
        if (logoBase64Cache == null) {
            logoBase64Cache = carregarLogoBase64();
        }
        return logoBase64Cache;
    }

    private String carregarLogoBase64() {
        try (InputStream is = new ClassPathResource(LOGO_PATH).getInputStream()) {
            byte[] bytes = is.readAllBytes();
            String base64 = Base64.getEncoder().encodeToString(bytes);
            return "data:image/png;base64," + base64;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}