package com.loja.allanassistencia.ordemservico.service;

import com.loja.allanassistencia.configuracao.entity.ConfiguracaoAssistencia;
import com.loja.allanassistencia.configuracao.service.ConfiguracaoAssistenciaService;
import com.loja.allanassistencia.exception.RecursoNaoEncontradoException;
import com.loja.allanassistencia.ordemservico.entity.OrdemServico;
import com.loja.allanassistencia.ordemservico.repository.OrdemServicoRepository;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
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

    public byte[] gerarPdf(Long ordemServicoId) {

        OrdemServico ordem = ordemServicoRepository.findById(ordemServicoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ordem de serviço não encontrada."));

        ConfiguracaoAssistencia config = configuracaoService.buscar();

        Context context = new Context();
        context.setVariable("nomeFantasia", config.getNomeFantasia());
        context.setVariable("endereco", config.getEndereco());
        context.setVariable("telefone", config.getTelefone());
        context.setVariable("corPrimaria", config.getCorPrimaria());
        context.setVariable("logoUrl", config.getLogoUrl());
        context.setVariable("rodapeTexto", config.getRodapeTexto());

        context.setVariable("nomeProduto", ordem.getAparelho());
        context.setVariable("nomeCliente", ordem.getCliente().getNome());
        context.setVariable("valor", ordem.getValor());
        context.setVariable("garantiaDias", ordem.getGarantiaDias());
        context.setVariable("garantiaInicio", formatar(ordem.getGarantiaInicio()));
        context.setVariable("garantiaFim", formatar(ordem.getGarantiaFim()));
        context.setVariable("servicoRealizado", ordem.getServicoRealizado());

        String html = templateEngine.process("comprovante", context);

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(os);
            builder.run();
            return os.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF do comprovante", e);
        }
    }

    private String formatar(LocalDate data) {
        return data != null ? data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "-";
    }
}