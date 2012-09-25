/**
 * Classe para conter as informacoes da tabela Lancamento
 *
 * @author Claudio Monteoliva
 * @since 1.0.0 - 16/07/2010
 *
 */
package br.com.monteoliva.adesp;

public class TabelaLancamento {
  // seta as colunas
  public static String[] colunas = new String[] {"_id","id_categoria","descricao","valor","vencimento","pago"};

  // seta os parametros com os tipos de cada coluna
  public long id;
  public long id_categoria;
  public String descricao;
  public float valor;
  public String vencimento;
  public int pago;
  public String categ_descricao;

  /**
   * Cosntructor
   */
  public TabelaLancamento() { }

  public TabelaLancamento(long id_categoria, String descricao, float valor, String vencimento, int pago, String categ_descricao) {
     super();
     
     // seta as propriedades (campos)
     this.id_categoria    = id_categoria;
     this.descricao       = descricao;
     this.valor           = valor;
     this.vencimento      = vencimento;
     this.pago            = pago;
     this.categ_descricao = categ_descricao;
  }

  public TabelaLancamento(long id, long id_categoria, String descricao, float valor, String vencimento, int pago, String categ_descricao) {
     super();
     
     // seta as propriedades (campos)
     this.id              = id;
     this.id_categoria    = id_categoria;
     this.descricao       = descricao;
     this.valor           = valor;
     this.vencimento      = vencimento;
     this.pago            = pago;
     this.categ_descricao = categ_descricao;
  }
}