package edu.furb.sistemanfe.business;

import java.util.List;

import javax.inject.Inject;

import br.gov.frameworkdemoiselle.stereotype.BusinessController;
import br.gov.frameworkdemoiselle.template.DelegateCrud;
import edu.furb.sistemanfe.domain.Cliente;
import edu.furb.sistemanfe.domain.NotaFiscal;
import edu.furb.sistemanfe.persistence.ClienteDAO;
import edu.furb.sistemanfe.rest.ClienteDTO;
import edu.furb.sistemanfe.security.SistemaNFeCredentials;

@BusinessController
public class ClienteBC extends DelegateCrud<Cliente, Long, ClienteDAO> {
	private static final long serialVersionUID = 1L;

	@Inject
	private SistemaNFeCredentials credentials;

	/**
	 * Sobrescreve o metodo ALL para garantir que somente será carregado
	 * Clientes do Emitente atual.
	 */
	@Override
	public List<Cliente> findAll() {
		ClienteDTO dto = new ClienteDTO();
		dto.setEmitente(credentials.getUsuario().getEmitente());
		return getDelegate().pesquisar(dto);
		// return super.findAll();
	}

	/**
	 * Obtem um crliante com base em seu Documento
	 * 
	 * @param documento
	 * @return
	 */
	public Cliente buscaPorDocumento(String documento) {
		ClienteDTO dto = new ClienteDTO();
		dto.setEmitente(credentials.getUsuario().getEmitente());
		dto.setDocumento(documento);
		List<Cliente> lst = getDelegate().pesquisar(dto);
		if ((lst == null) || (lst.size() == 0)) {
			return null;
		}
		return lst.get(0);
	}

	/**
	 * Adualiza o cadastro do cliente com base na informação importada da Nota;
	 * 
	 * @param nfRet
	 */
	public void atualizaCadastro(NotaFiscal nfRet) {
		Cliente cliente = buscaPorDocumento(nfRet.getClienteNotaFiscal()
				.getDocumento());
		if ((cliente == null) || (cliente.getId() == 0)) {
			cliente = new Cliente();
			cliente.setDocumento(nfRet.getClienteNotaFiscal().getDocumento());
			cliente.setEmitente(nfRet.getEmitente());
		}
		cliente.setEndereco(nfRet.getEndereco());
		cliente.setNome(nfRet.getClienteNotaFiscal().getNome());
		cliente.setInscricaoEstadual(nfRet.getClienteNotaFiscal()
				.getInscricaoEstadual());
		cliente.setFone(nfRet.getClienteNotaFiscal().getFone());
		if ((cliente.getId() == null) || (cliente.getId() == 0)) {
			insert(cliente);
		} else {
			update(cliente);
		}
	}

}