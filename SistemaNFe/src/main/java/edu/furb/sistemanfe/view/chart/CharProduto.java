package edu.furb.sistemanfe.view.chart;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import edu.furb.sistemanfe.business.ProdutoBC;
import edu.furb.sistemanfe.pojo.ProdutoGraficoVendas;

@ManagedBean
public class CharProduto implements Serializable {

	private static final long serialVersionUID = 1837406881086556800L;

	private BarChartModel barModel;
	@Inject 
	private ProdutoBC produtoBC;

	@PostConstruct
	public void init() {
		createBarModels();
	}

	public BarChartModel getBarModel() {
		return barModel;
	}

	private BarChartModel initBarModel() {
		
		List<ProdutoGraficoVendas> teste = produtoBC.getteste();
		//System.out.println(teste.toString());
		
		BarChartModel model = new BarChartModel();

		ChartSeries boys = new ChartSeries();
		boys.setLabel("Boys");
		
		for (ProdutoGraficoVendas produtoGraficoVendas : teste) {
			boys.set(produtoGraficoVendas.getDsCodigo(), produtoGraficoVendas.getTotalItens());
		}
		
//		boys.set("2004", 120);
//		boys.set("2005", 100);
//		boys.set("2006", 44);
//		boys.set("2007", 150);
//		boys.set("2008", 25);

//		ChartSeries girls = new ChartSeries();
//		girls.setLabel("Girls");
//		girls.set("2004", 52);
//		girls.set("2005", 60);
//		girls.set("2006", 110);
//		girls.set("2007", 135);
//		girls.set("2008", 120);

		model.addSeries(boys);
		//model.addSeries(girls);

		return model;
	}

	private void createBarModels() {
		createBarModel();
	}

	private void createBarModel() {
		barModel = initBarModel();

		barModel.setTitle("Bar Chart");
		barModel.setLegendPosition("ne");

		Axis xAxis = barModel.getAxis(AxisType.X);
		xAxis.setLabel("Gender");

		Axis yAxis = barModel.getAxis(AxisType.Y);
		yAxis.setLabel("Births");
		yAxis.setMin(0);
		yAxis.setMax(10);
	}

}