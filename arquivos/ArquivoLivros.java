package arquivos;

import aeds3.Arquivo;
import aeds3.ArvoreBMais;
import aeds3.HashExtensivel;
import aeds3.ParIntInt;
import entidades.Livro;
// Imprtar a classe de lista invertida
import aeds3.ListaInvertida;

import java.io.File;
// Importei a classe normalizer para facilitar a formatação
import java.text.Normalizer;
import java.util.ArrayList;

public class ArquivoLivros extends Arquivo<Livro> {

  HashExtensivel<ParIsbnId> indiceIndiretoISBN;
  ArvoreBMais<ParIntInt> relLivrosDaCategoria;
  ListaInvertida lista;

  public ArquivoLivros() throws Exception {
    super("livros", Livro.class.getConstructor());
    indiceIndiretoISBN = new HashExtensivel<>(
        ParIsbnId.class.getConstructor(),
        4,
        "dados/livros_isbn.hash_d.db",
        "dados/livros_isbn.hash_c.db");
    relLivrosDaCategoria = new ArvoreBMais<>(ParIntInt.class.getConstructor(), 4, "dados/livros_categorias.btree.db");
    // Verificar se existe a tabela de registros da listaInvertida
    try {
      File d = new File("dados");
      if (!d.exists())
        d.mkdir();
      lista = new ListaInvertida(4, "dados/dicionario.listainv.db", "dados/blocos.listainv.db");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // Método que recebe o titulo do livro e retorna um vetor com as palavras
  // separadas e formatadas
  // com a remoção de stop words
  public String[] tratarTitulo(String titulo) {
    String[] array = titulo.split(" ");
    String[] stopWords = { "a", "acerca", "adeus", "agora", "ainda", "alem", "algmas", "algo", "algumas", "alguns",
        "ali", "além", "ambas", "ambos", "ano", "anos", "antes", "ao", "aonde", "aos", "apenas", "apoio", "apontar",
        "apos", "após", "aquela", "aquelas", "aquele", "aqueles", "aqui", "aquilo", "as", "assim", "através", "atrás",
        "até", "aí", "baixo", "bastante", "bem", "boa", "boas", "bom", "bons", "breve", "cada", "caminho", "catorze",
        "cedo", "cento", "certamente", "certeza", "cima", "cinco", "coisa", "com", "como", "comprido", "conhecido",
        "conselho", "contra", "contudo", "corrente", "cuja", "cujas", "cujo", "cujos", "custa", "cá", "da", "daquela",
        "daquelas", "daquele", "daqueles", "dar", "das", "de", "debaixo", "dela", "delas", "dele", "deles", "demais",
        "dentro", "depois", "desde", "desligado", "dessa", "dessas", "desse", "desses", "desta", "destas", "deste",
        "destes", "deve", "devem", "deverá", "dez", "dezanove", "dezasseis", "dezassete", "dezoito", "dia", "diante",
        "direita", "dispoe", "dispoem", "diversa", "diversas", "diversos", "diz", "dizem", "dizer", "do", "dois", "dos",
        "doze", "duas", "durante", "dá", "dão", "dúvida", "e", "ela", "elas", "ele", "eles", "em", "embora", "enquanto",
        "entao", "entre", "então", "era", "eram", "essa", "essas", "esse", "esses", "esta", "estado", "estamos",
        "estar", "estará", "estas", "estava", "estavam", "este", "esteja", "estejam", "estejamos", "estes", "esteve",
        "estive", "estivemos", "estiver", "estivera", "estiveram", "estiverem", "estivermos", "estivesse", "estivessem",
        "estiveste", "estivestes", "estivéramos", "estivéssemos", "estou", "está", "estás", "estávamos", "estão", "eu",
        "exemplo", "falta", "fará", "favor", "faz", "fazeis", "fazem", "fazemos", "fazer", "fazes", "fazia", "faço",
        "fez", "fim", "final", "foi", "fomos", "for", "fora", "foram", "forem", "forma", "formos", "fosse", "fossem",
        "foste", "fostes", "fui", "fôramos", "fôssemos", "geral", "grande", "grandes", "grupo", "ha", "haja", "hajam",
        "hajamos", "havemos", "havia", "hei", "hoje", "hora", "horas", "houve", "houvemos", "houver", "houvera",
        "houveram", "houverei", "houverem", "houveremos", "houveria", "houveriam", "houvermos", "houverá", "houverão",
        "houveríamos", "houvesse", "houvessem", "houvéramos", "houvéssemos", "há", "hão", "iniciar", "inicio", "ir",
        "irá", "isso", "ista", "iste", "isto", "já", "lado", "lhe", "lhes", "ligado", "local", "logo", "longe", "lugar",
        "lá", "maior", "maioria", "maiorias", "mais", "mal", "mas", "me", "mediante", "meio", "menor", "menos", "meses",
        "mesma", "mesmas", "mesmo", "mesmos", "meu", "meus", "mil", "minha", "minhas", "momento", "muito", "muitos",
        "máximo", "mês", "na", "nada", "nao", "naquela", "naquelas", "naquele", "naqueles", "nas", "nem", "nenhuma",
        "nessa", "nessas", "nesse", "nesses", "nesta", "nestas", "neste", "nestes", "no", "noite", "nome", "nos",
        "nossa", "nossas", "nosso", "nossos", "nova", "novas", "nove", "novo", "novos", "num", "numa", "numas", "nunca",
        "nuns", "não", "nível", "nós", "número", "o", "obra", "obrigada", "obrigado", "oitava", "oitavo", "oito",
        "onde", "ontem", "onze", "os", "ou", "outra", "outras", "outro", "outros", "para", "parece", "parte", "partir",
        "paucas", "pegar", "pela", "pelas", "pelo", "pelos", "perante", "perto", "pessoas", "pode", "podem", "poder",
        "poderá", "podia", "pois", "ponto", "pontos", "por", "porque", "porquê", "portanto", "posição", "possivelmente",
        "posso", "possível", "pouca", "pouco", "poucos", "povo", "primeira", "primeiras", "primeiro", "primeiros",
        "promeiro", "propios", "proprio", "própria", "próprias", "próprio", "próprios", "próxima", "próximas",
        "próximo", "próximos", "puderam", "pôde", "põe", "põem", "quais", "qual", "qualquer", "quando", "quanto",
        "quarta", "quarto", "quatro", "que", "quem", "quer", "quereis", "querem", "queremas", "queres", "quero",
        "questão", "quieto", "quinta", "quinto", "quinze", "quáis", "quê", "relação", "sabe", "sabem", "saber", "se",
        "segunda", "segundo", "sei", "seis", "seja", "sejam", "sejamos", "sem", "sempre", "sendo", "ser", "serei",
        "seremos", "seria", "seriam", "será", "serão", "seríamos", "sete", "seu", "seus", "sexta", "sexto", "sim",
        "sistema", "sob", "sobre", "sois", "somente", "somos", "sou", "sua", "suas", "são", "sétima", "sétimo", "só",
        "tal", "talvez", "tambem", "também", "tanta", "tantas", "tanto", "tarde", "te", "tem", "temos", "tempo",
        "tendes", "tenha", "tenham", "tenhamos", "tenho", "tens", "tentar", "tentaram", "tente", "tentei", "ter",
        "terceira", "terceiro", "terei", "teremos", "teria", "teriam", "terá", "terão", "teríamos", "teu", "teus",
        "teve", "tinha", "tinham", "tipo", "tive", "tivemos", "tiver", "tivera", "tiveram", "tiverem", "tivermos",
        "tivesse", "tivessem", "tiveste", "tivestes", "tivéramos", "tivéssemos", "toda", "todas", "todo", "todos",
        "trabalhar", "trabalho", "treze", "três", "tu", "tua", "tuas", "tudo", "tão", "tém", "têm", "tínhamos", "um",
        "uma", "umas", "uns", "usa", "usar", "vai", "vais", "valor", "veja", "vem", "vens", "ver", "verdade",
        "verdadeiro", "vez", "vezes", "viagem", "vindo", "vinte", "você", "vocês", "vos", "vossa", "vossas", "vosso",
        "vossos", "vários", "vão", "vêm", "vós", "zero", "à", "às", "área", "é", "éramos", "és", "último" };
    String[] arrayRetorno = new String[array.length];
    for (int i = 0; i < arrayRetorno.length; i++) {
      arrayRetorno[i] = "";
    }
    int contador = 0;
    // Tirar os acentos das stopWords
    for (int j = 0; j < stopWords.length; j++) {
      String stopWordSemAcento = stopWords[j];
      stopWords[j] = Normalizer.normalize(stopWordSemAcento, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }
    // For para remover as stop words e remover os acentos
    for (int i = 0; i < array.length; i++) {
      String palavraComAcento = array[i].toLowerCase();
      array[i] = Normalizer.normalize(palavraComAcento, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
      if (!(isStopWord(stopWords, array[i]))) {
        arrayRetorno[contador] = array[i];
        contador++;
      }
    }
    // Criando um array apenas com a quantidade certa de palavras existentes
    String[] arrayRetornoTamanhoCerto = new String[contador];
    for (int i = 0; i < contador; i++) {
      arrayRetornoTamanhoCerto[i] = arrayRetorno[i];
    }
    return arrayRetornoTamanhoCerto;
  }

  // Método para verificar se é uma stop word
  public boolean isStopWord(String[] stopWords, String palavra) {
    for (int i = 0; i < stopWords.length; i++) {
      if (palavra.equals(stopWords[i])) {
        return true;
      }
    }
    return false;
  }

  @Override
  public int create(Livro obj) throws Exception {
    System.out.println(obj.getTitulo());
    int id = super.create(obj);
    obj.setID(id);
    indiceIndiretoISBN.create(new ParIsbnId(obj.getIsbn(), obj.getID()));
    relLivrosDaCategoria.create(new ParIntInt(obj.getIdCategoria(), obj.getID()));
    String[] array = tratarTitulo(obj.getTitulo());
    // Inserir na lista invertida
    for (int i = 0; i < array.length; i++) {
      String chave = array[i];
      lista.create(chave, id);
    }
    lista.print();
    return id;
  }

  public Livro readISBN(String isbn) throws Exception {
    ParIsbnId pii = indiceIndiretoISBN.read(ParIsbnId.hashIsbn(isbn));
    if (pii == null)
      return null;
    int id = pii.getId();
    return super.read(id);
  }

  @Override
  public boolean delete(int id) throws Exception {
    Livro obj = super.read(id);
    if (obj != null)
      if (indiceIndiretoISBN.delete(ParIsbnId.hashIsbn(obj.getIsbn()))
          && relLivrosDaCategoria.delete(new ParIntInt(obj.getIdCategoria(), obj.getID()))) {
        String titulo = obj.getTitulo();
        String[] array = tratarTitulo(titulo);
        // Remover na lista invertida
        for (int i = 0; i < array.length; i++) {
          String chave = array[i];
          lista.delete(chave, id);
        }
        lista.print();
        return super.delete(id);
      }
    return false;
  }

  @Override
  public boolean update(Livro novoLivro) throws Exception {
    Livro livroAntigo = super.read(novoLivro.getID());
    if (livroAntigo != null) {
      // Armazena o título antigo separadamente
      String[] tituloAntigo = tratarTitulo(livroAntigo.getTitulo());
      int id = livroAntigo.getID();
      // Testa alteração do ISBN
      if (livroAntigo.getIsbn().compareTo(novoLivro.getIsbn()) != 0) {
        indiceIndiretoISBN.delete(ParIsbnId.hashIsbn(livroAntigo.getIsbn()));
        indiceIndiretoISBN.create(new ParIsbnId(novoLivro.getIsbn(), novoLivro.getID()));
      }

      // Testa alteração da categoria
      if (livroAntigo.getIdCategoria() != novoLivro.getIdCategoria()) {
        relLivrosDaCategoria.delete(new ParIntInt(livroAntigo.getIdCategoria(), livroAntigo.getID()));
        relLivrosDaCategoria.create(new ParIntInt(novoLivro.getIdCategoria(), novoLivro.getID()));
      }
      // Após tudo estar certo, apagar do dicionário as palavras antigas
      for (int i = 0; i < tituloAntigo.length; i++) {
        String chave = tituloAntigo[i];
        lista.delete(chave, id);
      }
      // Após apagar as palavras antigas do dicionario, inserir as novas
      String[] tituloNovo = tratarTitulo(novoLivro.getTitulo());
      int idNovo = novoLivro.getID();
      for (int i = 0; i < tituloNovo.length; i++) {
        String chave = tituloNovo[i];
        lista.create(chave, idNovo);
      }
      lista.print();
      // Atualiza o livro
      return super.update(novoLivro);
    }
    return false;
  }
  // Método que recebe dois arrays de inteiros e retorna um array com a interseção deles
  public static int[] vetorIntersecao(int[] vetor1, int[] vetor2) {
    ArrayList<Integer> vetorIntersecao = new ArrayList<>();
    for (int i = 0; i < vetor1.length; i++) {
        for (int j = 0; j < vetor2.length; j++) {
            if (vetor1[i] == vetor2[j]) {
                vetorIntersecao.add(vetor1[i]);
                break;
            }
        }
    }
    return vetorIntersecao.stream().mapToInt(Integer::intValue).toArray();
}
  // Método que retorna o id do livro
  public int[] buscarNomeLivro(String titulo){
    String[] tituloFiltrado = tratarTitulo(titulo);
    int[] dadosResultado = null;
    try {
      for(int i = 0; i < tituloFiltrado.length; i++){
        dadosResultado = lista.read(tituloFiltrado[0]);
        // System.out.println("Palavras do titulo: " + tituloFiltrado[i]);
        int[] dados = lista.read(tituloFiltrado[i]);
        dadosResultado = vetorIntersecao(dadosResultado, dados);
      }
    } catch (Exception e) {
      System.out.println(e);  
    }
    return dadosResultado;
  }
}
