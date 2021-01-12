package com.pgbezerra.datamigration.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fatura implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Cliente cliente;
	private CartaoDeCredito cartaoDeCredito;
	private final List<Transacao> transacoes = new ArrayList<>();
	
	public Double getTotal() {
		return transacoes.stream().mapToDouble(Transacao::getValor).reduce(0, Double::sum);
	}

}
