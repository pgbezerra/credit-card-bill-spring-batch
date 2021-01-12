package com.pgbezerra.datamigration.reader;

import java.util.Objects;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import com.pgbezerra.datamigration.model.Fatura;
import com.pgbezerra.datamigration.model.Transacao;

public class FaturaCartaoCreditoReader implements ItemStreamReader<Fatura>{
	
	private ItemStreamReader<Transacao> delegate;
	private Transacao transacaoAtual;
	
	
	public FaturaCartaoCreditoReader(ItemStreamReader<Transacao> delegate) {
		this.delegate = delegate;
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		delegate.open(executionContext);
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		delegate.update(executionContext);
	}

	@Override
	public void close() throws ItemStreamException {
		delegate.close();
	}

	@Override
	public Fatura read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		if(Objects.isNull(transacaoAtual))
			delegate.read();
		
		Fatura fatura = null;
		Transacao transacao = transacaoAtual;
		transacaoAtual = null;
		
		if(Objects.nonNull(transacao)) {
			fatura = new Fatura();
			fatura.setCartaoDeCredito(transacao.getCartaoDeCredito());
			fatura.setCliente(transacao.getCartaoDeCredito().getCliente());
			fatura.getTransacoes().add(transacao);
			while(isTransacaoRelacionada(transacao))
				fatura.getTransacoes().add(transacao);
		}
			
		return fatura;
	}

	private boolean isTransacaoRelacionada(Transacao transacao) throws UnexpectedInputException, ParseException, NonTransientResourceException, Exception {
		return Objects.nonNull(peek()) && transacao.getCartaoDeCredito().getNumero().equals(transacaoAtual.getCartaoDeCredito().getNumero());
	}

	private Transacao peek() throws UnexpectedInputException, ParseException, NonTransientResourceException, Exception {
		transacaoAtual = delegate.read();
		return transacaoAtual;
	}

}
