package com.pgbezerra.datamigration.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableBatchProcessing
public class FaturaCartaoJobConfig {
	
	private JobBuilderFactory jobBuildFactory;

	public FaturaCartaoJobConfig(JobBuilderFactory jobBuildFactory) {
		this.jobBuildFactory = jobBuildFactory;
	}
	
	@Bean
	public Job faturaCartaoCreditoJob(Step faturaCartaoCreditoStep) {
		return jobBuildFactory
				.get("faturaCartaoCreditoJob")
				.start(faturaCartaoCreditoStep)
				.build();
	}

}
