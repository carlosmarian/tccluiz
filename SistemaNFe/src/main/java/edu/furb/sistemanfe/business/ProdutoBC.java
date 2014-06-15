package edu.furb.sistemanfe.business;

import java.util.List;

import javax.inject.Inject;

import br.gov.frameworkdemoiselle.stereotype.BusinessController;
import br.gov.frameworkdemoiselle.template.DelegateCrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import edu.furb.sistemanfe.domain.ItemNotaFiscal;
import edu.furb.sistemanfe.domain.NotaFiscal;
import edu.furb.sistemanfe.domain.Produto;
import edu.furb.sistemanfe.persistence.ProdutoDAO;
import edu.furb.sistemanfe.pojo.ProdutoGraficoVendas;
import edu.furb.sistemanfe.rest.ProdutoDTO;
import edu.furb.sistemanfe.security.SistemaNFeCredentials;

@BusinessController
public class ProdutoBC extends DelegateCrud<Produto, Long, ProdutoDAO> {
	private static final long serialVersionUID = 1L;

	@Inject
	private SistemaNFeCredentials credentials;

	/**
	 * Sobrescreve o metodo ALL para garantir que somente seram exibidos
	 * Produtos do Emitente atual;
	 */
	@Override
	public List<Produto> findAll() {
		ProdutoDTO dto = new ProdutoDTO();
		dto.setEmitente(credentials.getUsuario().getEmitente());
		return getDelegate().pesquisar(dto);
		// return super.findAll();
	}

	/**
	 * Carraga um produto com base em seu Código
	 * @param codigo
	 * @return
	 */
	public Produto buscaPorCodigo(String codigo) {
		ProdutoDTO dto = new ProdutoDTO();
		dto.setEmitente(credentials.getUsuario().getEmitente());
		dto.setCodigo(codigo);
		List<Produto> lst = getDelegate().pesquisar(dto);
		if ((lst == null) || (lst.size() == 0)) {
			return null;
		}
		return lst.get(0);
	}

	@Transactional
	/**
	 * Este metodo tem o objetivo de atualizar o cadastro de produtos com base nos dados de uma nota
	 * @param nf NotaFiscal com os itens
	 */
	public void atualizaCadastro(NotaFiscal nf) {
		for (ItemNotaFiscal item : nf.getItemNotaFiscal()) {
			Produto produto = buscaPorCodigo(item.getProduto().getCodigo());
			if (produto == null) {
				produto = new Produto();
				produto.setCodigo(item.getProduto().getCodigo());
				produto.setEmitente(nf.getEmitente());
			}

			produto.setNome(item.getProduto().getNome());
			if ((produto.getId() == null) || (produto.getId() == 0)) {
				insert(produto);
			} else {
				update(produto);
			}
		}

	}

	public List<ProdutoGraficoVendas> getteste() {
		// getDelegate().teste2(1L);
		// getDelegate().novoTeste2();
		return getDelegate().novoTeste3(credentials.getUsuario().getEmitente());
	}

}