package com.pgbezerra.datamigration.reader;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import com.pgbezerra.datamigration.model.CartaoDeCredito;
import com.pgbezerra.datamigration.model.Cliente;
import com.pgbezerra.datamigration.model.Transacao;

@Configuration
public class TransacaoReaderConfig {
	
	@Bean
	public JdbcCursorItemReader<Transacao> transacaoReader(@Qualifier("appDataSource") DataSource dataSource){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append("   * ");
		sql.append(" FROM ");
		sql.append("   transacao T ");
		sql.append(" JOIN ");
		sql.append("   carta_credito ");
		sql.append("   USING ");
		sql.append("     (numero_cartao_credito) ");
		sql.append(" ORDER BY ");
		sql.append("   numero_cartao_credito ");
		return new JdbcCursorItemReaderBuilder<Transacao>()
				.name("transacaoReader")
				.dataSource(dataSource)
				.sql(sql.toString())
				.rowMapper(transacaoRowMapper())
				.build();
	}

	private RowMapper<Transacao> transacaoRowMapper() {
		return new RowMapper<Transacao>() {

			@Override
			public Transacao mapRow(ResultSet rs, int rowNum) throws SQLException {
				CartaoDeCredito cartao = new CartaoDeCredito();
				cartao.setNumero(rs.getInt("numero_cartao_credito"));
				Cliente cliente = new Cliente();
				cliente.setId(rs.getInt("cliente"));
				cartao.setCliente(cliente);
				Transacao transacao = new Transacao();
				transacao.setId(rs.getInt("id"));
				transacao.setData(rs.getDate("data").toLocalDate());
				transacao.setValor(rs.getDouble("valor"));
				transacao.setDescricao(rs.getString("descricao"));
				transacao.setCartaoDeCredito(cartao);
				return transacao;
			}
		};
	}


}
