package com.pgbezerra.datamigration.model;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transacao implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private CartaoDeCredito cartaoDeCredito;
	private String descricao;
	private Double valor;
	private LocalDate data;

}
