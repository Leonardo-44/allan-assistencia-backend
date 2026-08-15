package com.loja.allanassistencia.ordemservico.service;

import com.loja.allanassistencia.configuracao.entity.ConfiguracaoAssistencia;
import com.loja.allanassistencia.configuracao.service.ConfiguracaoAssistenciaService;
import com.loja.allanassistencia.exception.RecursoNaoEncontradoException;
import com.loja.allanassistencia.ordemservico.dto.ComprovanteRequestDTO;
import com.loja.allanassistencia.ordemservico.entity.OrdemServico;
import com.loja.allanassistencia.ordemservico.repository.OrdemServicoRepository;
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

    public ComprovantePdfService(
            OrdemServicoRepository ordemServicoRepository,
            ConfiguracaoAssistenciaService configuracaoService,
            TemplateEngine templateEngine
    ) {
        this.ordemServicoRepository = ordemServicoRepository;
        this.configuracaoService = configuracaoService;
        this.templateEngine = templateEngine;
    }

    public byte[] gerarPdf(Long ordemServicoId, ComprovanteRequestDTO dto) {

        // =====================================================
        // BUSCAR OS
        // =====================================================

        OrdemServico ordem = ordemServicoRepository.findById(ordemServicoId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Ordem de serviço não encontrada."
                        )
                );

        // =====================================================
        // BUSCAR CONFIGURAÇÕES DA ASSISTÊNCIA
        // =====================================================

        ConfiguracaoAssistencia config = configuracaoService.buscar();

        // =====================================================
        // DADOS DA OS
        // =====================================================

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

        BigDecimal valor = dto != null && dto.valor() != null
                ? dto.valor()
                : ordem.getValor();

        Integer garantiaDias = dto != null && dto.garantiaDias() != null
                ? dto.garantiaDias()
                : ordem.getGarantiaDias();

        // Evita valor null no PDF
        if (valor == null) {
            valor = BigDecimal.ZERO;
        }

        // Evita garantia null
        if (garantiaDias == null) {
            garantiaDias = 0;
        }

        // =====================================================
        // GARANTIA
        // =====================================================

        LocalDate hoje = LocalDate.now();

        boolean temGarantia = garantiaDias > 0;

        LocalDate garantiaFim = temGarantia
                ? hoje.plusDays(garantiaDias)
                : null;

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // =====================================================
        // THYMELEAF
        // =====================================================

        Context context = new Context();

        // -----------------------------------------------------
        // DADOS DA ASSISTÊNCIA
        // -----------------------------------------------------

        String nomeFantasia = valorOuPadrao(
                config.getNomeFantasia(),
                "Assistência Técnica"
        );

        context.setVariable(
                "nomeFantasia",
                nomeFantasia
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

        context.setVariable(
                "logoUrl",
                config.getLogoUrl()
        );

        context.setVariable(
                "rodapeTexto",
                config.getRodapeTexto()
        );

        // -----------------------------------------------------
        // DADOS DA OS
        // -----------------------------------------------------

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

        // IMPORTANTE
        context.setVariable(
                "valor",
                valor
        );

        // -----------------------------------------------------
        // GARANTIA
        // -----------------------------------------------------

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

        // =====================================================
        // GERAR HTML
        // =====================================================

        String html = templateEngine.process(
                "comprovante",
                context
        );

        // =====================================================
        // GERAR PDF
        // =====================================================

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

            throw new RuntimeException(
                    "Erro ao gerar PDF do comprovante",
                    e
            );
        }
    }

    // =========================================================
    // MÉTODOS AUXILIARES
    // =========================================================

    private String valorOuPadrao(
            String valor,
            String padrao
    ) {

        if (valor != null && !valor.isBlank()) {
            return valor;
        }

        if (padrao != null && !padrao.isBlank()) {
            return padrao;
        }

        return "";
    }

    private String corOuPadrao(String cor) {

        if (cor != null && !cor.isBlank()) {
            return cor;
        }

        return "#1e3a8a";
    }
}