package com.pgbezerra.datamigration.writer;

import java.io.IOException;
import java.io.Writer;
import java.text.NumberFormat;

import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.MultiResourceItemWriter;
import org.springframework.batch.item.file.ResourceSuffixCreator;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemWriterBuilder;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.pgbezerra.datamigration.model.Fatura;
import com.pgbezerra.datamigration.model.Transacao;

@Configuration
public class FaturaCartaoWriterConfig {

	@Bean
	public MultiResourceItemWriter<Fatura> arquivosFaturaCartaoCredito() {
		return new MultiResourceItemWriterBuilder<Fatura>().name("arquivosFaturaCartaoCredito")
				.resource(new FileSystemResource("files/fatura")).itemCountLimitPerResource(1)
				.resourceSuffixCreator(suffixCreator()).delegate(arquivoFaturaCartao())
				.build();
	}

	private FlatFileItemWriter<? super Fatura> arquivoFaturaCartao() {
		return new FlatFileItemWriterBuilder<Fatura>().name("arquivoFaturaCartao")
				.resource(new FileSystemResource("files/fatura.txt")).lineAggregator(lineAggregator())
				.headerCallback(headerCallback())
				.footerCallback(footerCallBack()).build();
	}

	@Bean
	public FlatFileFooterCallback footerCallBack() {
		return new TotalTransacoesCallBack();
	}

	private FlatFileHeaderCallback headerCallback() {
		return new FlatFileHeaderCallback() {
			
			@Override
			public void writeHeader(Writer writer) throws IOException {
				writer.append(String.format("%121s\n", "Cartao XPTO"));
				writer.append(String.format("%121s\n\n", "Rua vergueiro, 131"));
				
			}
		};
	}

	private LineAggregator<Fatura> lineAggregator() {
		return new LineAggregator<Fatura>() {

			@Override
			public String aggregate(Fatura fatura) {
				StringBuilder sb = new StringBuilder();
				sb.append(String.format("Nome: %s\n", fatura.getCliente().getNome()));
				sb.append(String.format("Endereco: %s\n\n\n", fatura.getCliente().getEndereco()));
				sb.append(String.format("Fatura completa do cartao %d\n", fatura.getCartaoDeCredito().getNumero()));
				sb.append(
						"---------------------------------------------------------------------------------------------------\n");
				sb.append("DATA DESCRICAO VALOR\n");
				sb.append(
						"---------------------------------------------------------------------------------------------------\n");
				for (Transacao transacao : fatura.getTransacoes()) 
					sb.append(String.format("\n[10%s] %-80s - %s", transacao.getData().toString(),
							transacao.getDescricao(), NumberFormat.getCurrencyInstance().format(transacao.getValor())));
				
				return sb.toString();
			}

		};
	}

	private ResourceSuffixCreator suffixCreator() {
		return new ResourceSuffixCreator() {

			@Override
			public String getSuffix(int index) {
				return String.format("%s.txt", index);
			}
		};
	}

}
