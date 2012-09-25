package br.com.monteoliva.adesp;

public class TabelaCategoria {
	  // seta as colunas
	  public static String[] colunas = new String[] {"_id", "descricao", "st_tipo"};

	  // seta os parametros com os tipos de cada coluna
	  public long id;
	  public String descricao;
	  public float total;
	  public char mostra;
	  public int st_tipo;

	  /**
	   * Constructor
	   */
	  public TabelaCategoria() { }

	  public TabelaCategoria(String descricao, float total, char mostra, int st_tipo) {
	     super();
	     
	     // seta as propriedades (campos)
	     this.descricao = descricao;
	     this.total     = total;
	     this.mostra    = mostra;
	     this.st_tipo   = st_tipo;
	  }

	  public TabelaCategoria(long id, String descricao, float total, char mostra, int st_tipo) {
	     super();
	     
	     // seta as propriedades (campos)
	     this.id        = id;
	     this.descricao = descricao;
	     this.total     = total;
	     this.mostra    = mostra;
	     this.st_tipo   = st_tipo;
	  }
}
