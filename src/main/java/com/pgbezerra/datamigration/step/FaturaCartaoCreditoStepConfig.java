package com.pgbezerra.datamigration.step;


import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Configuration;

import com.pgbezerra.datamigration.model.Fatura;
import com.pgbezerra.datamigration.model.Transacao;
import com.pgbezerra.datamigration.reader.FaturaCartaoCreditoReader;

@Configuration
public class FaturaCartaoCreditoStepConfig {
	
	private StepBuilderFactory stepBuilderFactory;

	public FaturaCartaoCreditoStepConfig(StepBuilderFactory stepBuilderFactory) {
		this.stepBuilderFactory = stepBuilderFactory;
	}

	public Step faturaCartaoCreditoStep(ItemStreamReader<Transacao> faturaItemReader,
			ItemProcessor<Fatura, Fatura> faturaItemProcessor,
			ItemWriter<Fatura> faturaItemWriter) {
		return stepBuilderFactory
				.get("faturaCartaoCreditoStep")
				.<Fatura, Fatura>chunk(1)
				.reader(new FaturaCartaoCreditoReader(faturaItemReader))
				.processor(faturaItemProcessor)
				.writer(faturaItemWriter)
				.build();
	}
	

}
