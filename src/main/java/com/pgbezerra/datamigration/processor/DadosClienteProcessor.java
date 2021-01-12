package com.pgbezerra.datamigration.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.pgbezerra.datamigration.model.Cliente;
import com.pgbezerra.datamigration.model.Fatura;

@Component
public class DadosClienteProcessor implements ItemProcessor<Fatura, Fatura>{

	private final RestTemplate restTemplate = new RestTemplate();
	

	@Override
	public Fatura process(Fatura fatura) throws Exception {
		String url = String.format("http://my-json-server.typicode.com/giuliana-bezerra/demo/profile/%s", fatura.getCliente().getId());
		ResponseEntity<Cliente> response = restTemplate.getForEntity(url, Cliente.class);
		if(response.getStatusCode() == HttpStatus.OK) {
			fatura.setCliente(response.getBody());
			return fatura;
		}
		throw new ValidationException("Cliente nao encontrado");
	}

}
