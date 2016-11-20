package com.financeiro.controller.bean;

import java.util.List;

import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;

import com.financeiro.model.Despesa;
import com.financeiro.model.Lancamento;
import com.financeiro.model.TipoLancamento;
import com.financeiro.repository.Despesas;
import com.financeiro.repository.Lancamentos;
import com.financeiro.service.CadastroLancamentos;
import com.financeiro.service.NegocioException;

@Named
@javax.faces.view.ViewScoped
public class LancamentoBean extends BaseBean {

	private static final long serialVersionUID = 1L;

	@Inject
	private Lancamentos repository;
	@Inject
	private Despesas despesaRepository;
	@Inject
	private CadastroLancamentos service;

	private Lancamento lancamento;
	private List<Lancamento> lancamentos;

	public String novoLancamento() {
		return "/pages/interno/CadastroLancamento?faces-redirect=true";
	}

	public void prepararCadastro() {

		if (this.lancamento == null) {
			this.lancamento = new Lancamento();
		}

	}

	public void salvar() {
		try {
			this.lancamento = this.service.Salvar(lancamento);
			this.addInfoMessage("Lançamento salvo com sucesso!");
		} catch (NegocioException e) {
			this.addErrorMessage(e);
		} catch (Exception e) {
			this.addErrorMessage("Erro ao salvar registro.");
		}
	}

	public void salvarNovo() {
		try {
			this.service.Salvar(lancamento);
			this.lancamento = new Lancamento();
			this.addInfoMessage("Lançamento salvo com sucesso!");
		} catch (NegocioException ex) {
			this.addErrorMessage(ex);
		} catch (Exception e) {
			this.addErrorMessage("Erro ao salvar registro.");
		}
	}

	public void excluir() {
		try {
			this.service.excluir(this.lancamento);
			this.consultar();
		} catch (NegocioException e) {
			this.addErrorMessage(e);
		} catch (Exception e) {
			this.addErrorMessage("Erro ao excluir registro.");
		}
	}

	public void consultar() {
		try {
			this.lancamentos = this.repository.todos();
		} catch (Exception e) {
			this.addErrorMessage("Erro ao carregar registros.");
		}
	}

	public void pesquisar() {
		try {
			this.lancamentos = repository.filtrar(this.getFiltro());
		} catch (Exception e) {
			this.addErrorMessage("Erro ao pesquisar registros.");
		}
	}

	public List<Despesa> pesquisarDespesas(String nome) {
		try {
			return this.despesaRepository.porParteNome(nome);
		} catch (Exception e) {
			this.addErrorMessage("Erro ao carregar despesas.");
			return null;
		}
	}

	public void dataVencimentoAlterada(AjaxBehaviorEvent event) {
		if (this.lancamento.getDataPagamento() == null) {
			this.lancamento.setDataPagamento(this.lancamento.getDataVencimento());
		}
	}

	public List<Lancamento> getLancamentos() {
		return lancamentos;
	}

	public TipoLancamento[] getTiposLancamentos() {
		return TipoLancamento.values();
	}

	public Lancamento getLancamento() {
		return lancamento;
	}

	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}
}
