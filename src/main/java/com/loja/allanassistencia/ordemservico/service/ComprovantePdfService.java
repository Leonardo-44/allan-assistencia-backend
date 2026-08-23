package com.loja.allanassistencia.ordemservico.service;

import com.loja.allanassistencia.configuracao.entity.ConfiguracaoAssistencia;
import com.loja.allanassistencia.configuracao.service.ConfiguracaoAssistenciaService;
import com.loja.allanassistencia.exception.RecursoNaoEncontradoException;
import com.loja.allanassistencia.ordemservico.dto.ComprovanteRequestDTO;
import com.loja.allanassistencia.ordemservico.entity.OrdemServico;
import com.loja.allanassistencia.ordemservico.repository.OrdemServicoRepository;
import com.loja.allanassistencia.shared.util.LogoResourceService;
import com.loja.allanassistencia.shared.util.AssinaturaResourceService;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class ComprovantePdfService {

    private final OrdemServicoRepository ordemServicoRepository;
    private final ConfiguracaoAssistenciaService configuracaoService;
    private final TemplateEngine templateEngine;
    private final LogoResourceService logoResourceService;
    private final AssinaturaResourceService assinaturaResourceService;

    public ComprovantePdfService(
            OrdemServicoRepository ordemServicoRepository,
            ConfiguracaoAssistenciaService configuracaoService,
            TemplateEngine templateEngine,
            LogoResourceService logoResourceService,
            AssinaturaResourceService assinaturaResourceService
    ) {
        this.ordemServicoRepository = ordemServicoRepository;
        this.configuracaoService = configuracaoService;
        this.templateEngine = templateEngine;
        this.logoResourceService = logoResourceService;
        this.assinaturaResourceService = assinaturaResourceService;
    }

    public byte[] gerarPdf(Long ordemServicoId, ComprovanteRequestDTO dto) {

        OrdemServico ordem = ordemServicoRepository.findById(ordemServicoId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Ordem de serviço não encontrada."
                        )
                );

        ConfiguracaoAssistencia config = configuracaoService.buscar();

        String nomeProduto = valorOuPadrao(
                dto != null ? dto.nomeProduto() : null,
                ordem.getAparelho()
        );

        String nomeCliente = valorOuPadrao(
                dto != null ? dto.nomeCliente() : null,
                ordem.getCliente().getNome()
        );

        String servicoRealizado = valorOuPadrao(
                dto != null ? dto.servicoRealizado() : null,
                ordem.getServicoRealizado()
        );

        String imei = valorOuPadrao(
                dto != null ? dto.imei() : null,
                ordem.getImei()
        );

        BigDecimal valor =
                dto != null && dto.valor() != null
                        ? dto.valor()
                        : ordem.getValor();

        Integer garantiaDias =
                dto != null && dto.garantiaDias() != null
                        ? dto.garantiaDias()
                        : ordem.getGarantiaDias();

        LocalDate hoje = LocalDate.now();

        boolean temGarantia =
                garantiaDias != null && garantiaDias > 0;

        LocalDate garantiaFim =
                temGarantia
                        ? hoje.plusDays(garantiaDias)
                        : null;

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Context context = new Context();

        // ==========================================
        // ASSISTÊNCIA
        // ==========================================

        context.setVariable(
                "nomeFantasia",
                config.getNomeFantasia()
        );

        context.setVariable(
                "endereco",
                config.getEndereco()
        );

        context.setVariable(
                "telefone",
                config.getTelefone()
        );

        context.setVariable(
                "corPrimaria",
                corOuPadrao(config.getCorPrimaria())
        );

        String logo = logoResourceService.obterLogoBase64();
        context.setVariable(
                "logoUrl",
                logo != null ? logo : config.getLogoUrl()
        );

        String assinatura = assinaturaResourceService.obterAssinaturaBase64();
        context.setVariable(
                "assinaturaUrl",
                assinatura
        );

        context.setVariable(
                "rodapeTexto",
                config.getRodapeTexto()
        );

        // ==========================================
        // OS
        // ==========================================

        context.setVariable(
                "numeroOs",
                ordem.getId()
        );

        context.setVariable(
                "dataEmissao",
                hoje.format(formatter)
        );

        context.setVariable(
                "nomeProduto",
                nomeProduto
        );

        context.setVariable(
                "nomeCliente",
                nomeCliente
        );

        context.setVariable(
                "imei",
                imei
        );

        context.setVariable(
                "defeito",
                ordem.getDefeito()
        );

        context.setVariable(
                "servicoRealizado",
                servicoRealizado
        );

        context.setVariable(
                "peca",
                ordem.getPeca()
        );

        context.setVariable(
                "valor",
                valor
        );

        // ==========================================
        // GARANTIA
        // ==========================================

        context.setVariable(
                "temGarantia",
                temGarantia
        );

        context.setVariable(
                "garantiaDias",
                garantiaDias
        );

        context.setVariable(
                "garantiaInicio",
                hoje.format(formatter)
        );

        context.setVariable(
                "garantiaFim",
                garantiaFim != null
                        ? garantiaFim.format(formatter)
                        : null
        );

        // ==========================================
        // THYMELEAF
        // ==========================================

        String html =
                templateEngine.process(
                        "comprovante-os",
                        context
                );

        // ==========================================
        // PDF
        // ==========================================

        try (ByteArrayOutputStream os =
                     new ByteArrayOutputStream()) {

            PdfRendererBuilder builder =
                    new PdfRendererBuilder();

            builder.useFastMode();

            builder.withHtmlContent(
                    html,
                    null
            );

            builder.toStream(os);

            builder.run();

            return os.toByteArray();

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Erro ao gerar PDF do comprovante",
                    e
            );
        }
    }

    private String valorOuPadrao(
            String valor,
            String padrao
    ) {
        return valor != null && !valor.isBlank()
                ? valor
                : padrao;
    }

    private String corOuPadrao(String cor) {
        return cor != null && !cor.isBlank()
                ? cor
                : "#1e3a8a";
    }
}