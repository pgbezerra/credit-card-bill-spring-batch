package com.pgbezerra.datamigration.writer;

import java.io.IOException;
import java.io.Writer;
import java.text.NumberFormat;
import java.util.List;

import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeWrite;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.file.FlatFileFooterCallback;

import com.pgbezerra.datamigration.model.Fatura;

public class TotalTransacoesCallBack implements FlatFileFooterCallback {
	
	private Double total = 0d;

	@Override
	public void writeFooter(Writer writer) throws IOException {
		writer.append(String.format("\n%121s%s", "Total: ", NumberFormat.getCurrencyInstance().format(total)));
	}

	@BeforeWrite
	public void beforeWrite(List<Fatura> faturas) {
		for(Fatura fatura: faturas)
			total += fatura.getTotal();
	}
	
	@AfterChunk
	public void afterChunk(ChunkContext chunkContext) {
		total = 0d;
	}
	
}
