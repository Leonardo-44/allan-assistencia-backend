package com.loja.allanassistencia.venda.service;

import com.loja.allanassistencia.configuracao.entity.ConfiguracaoAssistencia;
import com.loja.allanassistencia.configuracao.service.ConfiguracaoAssistenciaService;
import com.loja.allanassistencia.exception.RecursoNaoEncontradoException;
import com.loja.allanassistencia.shared.util.LogoResourceService;
import com.loja.allanassistencia.shared.util.AssinaturaResourceService;
import com.loja.allanassistencia.venda.dto.ComprovanteVendaRequestDTO;
import com.loja.allanassistencia.venda.entity.Venda;
import com.loja.allanassistencia.venda.repository.VendaRepository;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class ComprovanteVendaPdfService {

    private final VendaRepository vendaRepository;
    private final ConfiguracaoAssistenciaService configuracaoService;
    private final TemplateEngine templateEngine;
    private final LogoResourceService logoResourceService;
    private final AssinaturaResourceService assinaturaResourceService;

    public ComprovanteVendaPdfService(
            VendaRepository vendaRepository,
            ConfiguracaoAssistenciaService configuracaoService,
            TemplateEngine templateEngine,
            LogoResourceService logoResourceService,
            AssinaturaResourceService assinaturaResourceService
    ) {
        this.vendaRepository = vendaRepository;
        this.configuracaoService = configuracaoService;
        this.templateEngine = templateEngine;
        this.logoResourceService = logoResourceService;
        this.assinaturaResourceService = assinaturaResourceService;
    }

    public byte[] gerarPdf(Long vendaId, ComprovanteVendaRequestDTO dto) {

        Venda venda = vendaRepository.findById(vendaId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Venda não encontrada."
                        )
                );

        ConfiguracaoAssistencia config = configuracaoService.buscar();

        String aparelho = valorOuPadrao(
                dto != null ? dto.aparelho() : null,
                venda.getAparelho()
        );

        String nomeCliente = venda.getCliente() != null
                ? venda.getCliente().getNome()
                : "Cliente não identificado";

        String imei = valorOuPadrao(
                dto != null ? dto.imei() : null,
                venda.getImei()
        );

        BigDecimal valor =
                dto != null && dto.valor() != null
                        ? dto.valor()
                        : venda.getValor();

        BigDecimal valorPago =
                dto != null && dto.valorPago() != null
                        ? dto.valorPago()
                        : venda.getValorPago();

        String formaPagamento = valorOuPadrao(
                dto != null ? dto.formaPagamento() : null,
                venda.getFormaPagamento()
        );

        Integer garantiaDias =
                dto != null && dto.garantiaDias() != null
                        ? dto.garantiaDias()
                        : null;

        BigDecimal valorRestante = valor.subtract(valorPago);
        if (valorRestante.compareTo(BigDecimal.ZERO) < 0) {
            valorRestante = BigDecimal.ZERO;
        }

        String statusPagamento;
        if (valorPago.compareTo(valor) >= 0) {
            statusPagamento = "PAGO";
        } else if (valorPago.compareTo(BigDecimal.ZERO) > 0) {
            statusPagamento = "PARCIAL";
        } else {
            statusPagamento = "PENDENTE";
        }

        LocalDate hoje = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Context context = new Context();

        // ==========================================
        // ASSISTÊNCIA
        // ==========================================

        context.setVariable("nomeFantasia", config.getNomeFantasia());
        context.setVariable("endereco", config.getEndereco());
        context.setVariable("telefone", config.getTelefone());
        context.setVariable("corPrimaria", corOuPadrao(config.getCorPrimaria()));

        String logo = logoResourceService.obterLogoBase64();
        context.setVariable("logoUrl", logo != null ? logo : config.getLogoUrl());

        String assinatura = assinaturaResourceService.obterAssinaturaBase64();
        context.setVariable("assinaturaUrl", assinatura);

        context.setVariable("rodapeTexto", config.getRodapeTexto());

        // ==========================================
        // VENDA
        // ==========================================

        context.setVariable("numeroVenda", venda.getId());
        context.setVariable("dataEmissao", hoje.format(formatter));

        LocalDate dataVendaLocal = venda.getDataVenda() != null
                ? venda.getDataVenda().toLocalDate()
                : hoje;

        context.setVariable("dataVenda",
                venda.getDataVenda() != null
                        ? venda.getDataVenda().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                        : hoje.format(formatter)
        );
        context.setVariable("aparelho", aparelho);
        context.setVariable("imei", imei);
        context.setVariable("nomeCliente", nomeCliente);
        context.setVariable("formaPagamento", formaPagamento);
        context.setVariable("valor", valor);
        context.setVariable("valorPago", valorPago);
        context.setVariable("valorRestante", valorRestante);
        context.setVariable("statusPagamento", statusPagamento);

        // ==========================================
        // GARANTIA
        // ==========================================

        if (garantiaDias != null && garantiaDias > 0) {
            context.setVariable("garantiaDias", garantiaDias);
            context.setVariable(
                    "dataLimiteGarantia",
                    dataVendaLocal.plusDays(garantiaDias).format(formatter)
            );
        } else {
            context.setVariable("garantiaDias", null);
            context.setVariable("dataLimiteGarantia", null);
        }

        // ==========================================
        // THYMELEAF
        // ==========================================

        String html = templateEngine.process("comprovante-venda", context);

        // ==========================================
        // PDF
        // ==========================================

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(os);
            builder.run();

            return os.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao gerar PDF do comprovante", e);
        }
    }

    private String valorOuPadrao(String valor, String padrao) {
        return valor != null && !valor.isBlank() ? valor : padrao;
    }

    private String corOuPadrao(String cor) {
        return cor != null && !cor.isBlank() ? cor : "#1e3a8a";
    }
}