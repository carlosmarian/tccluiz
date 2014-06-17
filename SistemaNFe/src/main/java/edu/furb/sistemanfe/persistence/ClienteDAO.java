package edu.furb.sistemanfe.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.gov.frameworkdemoiselle.stereotype.PersistenceController;
import br.gov.frameworkdemoiselle.template.JPACrud;
import edu.furb.sistemanfe.domain.Cliente;
import edu.furb.sistemanfe.domain.Emitente;
import edu.furb.sistemanfe.pojo.ClienteCurvaABC;
import edu.furb.sistemanfe.pojo.ProdutoCurvaABC;
import edu.furb.sistemanfe.rest.ClienteDTO;

@PersistenceController
public class ClienteDAO extends JPACrud<Cliente, Long> {

	private static final long serialVersionUID = 1L;

	public List<Cliente> pesquisar(ClienteDTO dto) {

		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Cliente> query = builder.createQuery(Cliente.class);
		Root<Cliente> objeto = query.from(Cliente.class);
		query.select(objeto);

		List<Predicate> predicateList = new ArrayList<Predicate>();

		if (dto.getId() != null) {
			Predicate p = builder.equal(objeto.<Long> get("id"),
					dto.getId());
			predicateList.add(p);
		}
		if (dto.getDocumento() != null) {
			Predicate p = builder.equal(objeto.<String> get("documento"),
					dto.getDocumento());
			predicateList.add(p);
		}
		if (dto.getNome() != null) {
			Predicate p = builder.equal(objeto.<String> get("nome"),
					dto.getNome());
			predicateList.add(p);
		}

		//if (dto.getEmitente() != null) {
			Predicate p = builder.equal(objeto.<Emitente> get("emitente"),
					dto.getEmitente());
			predicateList.add(p);
		//}//TODO: Avaliar necessidade de criticar caso não tenha Emitente
		
		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);	
		return getEntityManager().createQuery(query).getResultList();

	}

	public List<ClienteCurvaABC> clientesABC(Emitente emitente) {
			String sqlQuery = "SELECT "
					+ " new edu.furb.sistemanfe.pojo.ClienteCurvaABC(c.documento, c.nome, sum(i.valorUnitario), "
					+ " sum(i.quantidade)) "
					+ "  from NotaFiscal as n, Cliente as c "
					+ "  join n.itemNotaFiscal as i "
					+ "  where c.documento = n.clienteNotaFiscal.documento and "
					+ " c.emitente = n.emitente and n.emitente = ?1 "
					+ "  group by c.documento, c.nome order by sum(i.valorUnitario) * sum(i.quantidade) desc ";
			javax.persistence.Query query3 = getEntityManager().createQuery(
					sqlQuery, ClienteCurvaABC.class);
			query3.setParameter(1, emitente);
			List<ClienteCurvaABC> clientes = (List<ClienteCurvaABC>) query3
					.getResultList();
			System.out.println(clientes.toString());
			return clientes;

		
	}
}
