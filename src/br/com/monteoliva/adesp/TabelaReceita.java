package br.com.monteoliva.adesp;

public class TabelaReceita {
	  // seta as colunas
	  public static String[] colunas = new String[] {"_id", "id_categoria", "valor", "mes_ano", "descricao"};

	  // seta os parametros com os tipos de cada coluna
	  public long id;
	  public long id_categoria;
	  public float valor;
	  public long mes_ano;
	  public String categ_descricao;
	  public String descricao;

	  /**
	   * Cosntructor
	   */
	  public TabelaReceita() { }

	  public TabelaReceita(long id_categoria, float valor, long mes_ano, String descricao) {
	     super();
	     
	     // seta as propriedades (campos)
	     this.id_categoria = id_categoria;
	     this.valor        = valor;
	     this.mes_ano      = mes_ano;
	     this.descricao    = descricao;
	  }

	  public TabelaReceita(long id, long id_categoria, float valor, long mes_ano, String descricao) {
	     super();
	     
	     // seta as propriedades (campos)
	     this.id           = id;
	     this.id_categoria = id_categoria;
	     this.valor        = valor;
	     this.mes_ano      = mes_ano;
	     this.descricao    = descricao;
	  }
}