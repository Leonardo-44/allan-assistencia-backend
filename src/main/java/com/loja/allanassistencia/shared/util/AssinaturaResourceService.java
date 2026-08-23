package com.loja.allanassistencia.shared.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Component
public class AssinaturaResourceService {

    private static final String ASSINATURA_PATH = "static/img/alanassinatura.png";

    private String assinaturaBase64Cache;

    public String obterAssinaturaBase64() {
        if (assinaturaBase64Cache == null) {
            assinaturaBase64Cache = carregarAssinaturaBase64();
        }
        return assinaturaBase64Cache;
    }

    private String carregarAssinaturaBase64() {
        try (InputStream is = new ClassPathResource(ASSINATURA_PATH).getInputStream()) {
            byte[] bytes = is.readAllBytes();
            String base64 = Base64.getEncoder().encodeToString(bytes);
            return "data:image/jpeg;base64," + base64;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}