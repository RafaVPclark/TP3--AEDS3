
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import aeds3.LZW;
import arquivos.ArquivoAutores;
import arquivos.ArquivoCategorias;
import arquivos.ArquivoLivros;
import entidades.Autor;
import entidades.Categoria;
import entidades.Livro;

public class Principal {

  private static Scanner console = new Scanner(System.in);

  public static void main(String[] args) {

    try {

      int opcao;
      do {
        System.out.println("\n\n\nBOOKAEDS 1.0");
        System.out.println("------------");
        System.out.println("\n> Início");
        System.out.println("\n1) Categorias");
        System.out.println("2) Autores");
        System.out.println("3) Livros");
        System.out.println("4) Realizar Backup");
        System.out.println("5) Recuperar backup");
        System.out.println("\n9) Reiniciar BD");
        System.out.println("\n0) Sair");

        System.out.print("\nOpção: ");
        try {
          opcao = Integer.valueOf(console.nextLine());
        } catch (NumberFormatException e) {
          opcao = -1;
        }

        switch (opcao) {
          case 1:
            (new MenuCategorias()).menu();
            break;
          case 2:
            (new MenuAutores()).menu();
            break;
          case 3:
            (new MenuLivros()).menu();
            break;
          case 4:
            backup();
            break;
          case 5:
            recuperarBackup();
            break;
          case 9:
            preencherDados();
            break;
          case 0:
            break;
          default:
            System.out.println("Opção inválida");
        }
      } while (opcao != 0);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void preencherDados() {
    try {
      new File("dados/categorias.db").delete();
      new File("dados/categorias.hash_d.db").delete();
      new File("dados/categorias.hash_c.db").delete();
      new File("dados/autores.db").delete();
      new File("dados/autores.hash_d.db").delete();
      new File("dados/autores.hash_c.db").delete();
      new File("dados/livros.db").delete();
      new File("dados/livros.hash_d.db").delete();
      new File("dados/livros.hash_c.db").delete();
      new File("dados/livros_isbn.hash_d.db").delete();
      new File("dados/livros_isbn.hash_c.db").delete();
      new File("dados/livros_categorias.btree.db").delete();
      // Excluir também o dicionário das palavras
      new File("dados/dicionario.listainv.db").delete();

      ArquivoLivros arqLivros = new ArquivoLivros();
      ArquivoCategorias arqCategorias = new ArquivoCategorias();
      ArquivoAutores arqAutores = new ArquivoAutores();

      arqCategorias.create(new Categoria("Romance"));
      arqCategorias.create(new Categoria("Educação"));
      arqCategorias.create(new Categoria("Sociologia"));
      arqCategorias.create(new Categoria("Policial"));
      arqCategorias.create(new Categoria("Aventura"));
      arqCategorias.create(new Categoria("Suspense"));

      arqAutores.create(new Autor("Homero"));
      arqAutores.create(new Autor("Lilian Bacich"));
      arqAutores.create(new Autor("Adolfo Tanzi Neto"));
      arqAutores.create(new Autor("Zygmunt Bauman"));
      arqAutores.create(new Autor("Plínio Dentzien"));
      arqAutores.create(new Autor("Ivan Izquierdo"));
      arqAutores.create(new Autor("Mariana Zapata"));

      arqLivros.close();
      arqCategorias.close();
      arqAutores.close();

      System.out.println("Banco de dados reinicializado com dados de exemplo.");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // ---------------------
  // Backups
  // ---------------------

  public static void copy(String nomeEntidade, Path backupDir) {
    String originalFilePath = "dados/" + nomeEntidade + ".db";
    String destinoFilePath = backupDir.toString() + "/" + nomeEntidade + "_backup.db";
    List<Byte> arquivoInteiro = new ArrayList<>();
    try (FileReader reader = new FileReader(originalFilePath);
        RandomAccessFile backupFile = new RandomAccessFile(destinoFilePath, "rw")) {
      int caractere;
      while ((caractere = reader.read()) != -1) {
        arquivoInteiro.add((byte) caractere);
      }
      System.out.println("Leitura do arquivo " + nomeEntidade + " realizada com sucesso.");
      // Convert List<Byte> to byte[]
      byte[] byteArray = new byte[arquivoInteiro.size()];
      for (int i = 0; i < arquivoInteiro.size(); i++) {
        byteArray[i] = arquivoInteiro.get(i);
      }
      // Codifica o conteúdo lido
      byte[] msgCodificada = LZW.codifica(byteArray);
      // Escreve o conteúdo codificado no arquivo de backup
      backupFile.write(msgCodificada);
      System.out.println("Backup do arquivo " + nomeEntidade + " realizado com sucesso.");
    } catch (IOException e) {
      System.out.println("Erro ao ler o arquivo " + nomeEntidade + ".");
      e.printStackTrace();
    }
  }

  public static void backup() {
    LocalDate hoje = LocalDate.now();
    String pastaBackup = "backup/" + hoje.toString();
    Path backupDir = Paths.get(pastaBackup);
    try {
      Files.createDirectories(backupDir);
      copy("livros", backupDir);
      copy("autores", backupDir);
      copy("autores.hash_c", backupDir);
      copy("autores.hash_d", backupDir);
      copy("blocos.listainv", backupDir);
      copy("categorias", backupDir);
      copy("categorias.hash_c", backupDir);
      copy("categorias.hash_d", backupDir);
      copy("dicionario.listainv", backupDir);
      copy("livros_isbn.hash_c", backupDir);
      copy("livros_isbn.hash_d", backupDir);
      copy("livros.hash_c", backupDir);
      copy("livros.hash_d", backupDir);
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  // Método que realiza a cópia de um arquivo para outro
  public static void copyRetorno(String nomeEntidade, String pathBackup) {
    String originalFilePath = pathBackup;
    // System.out.println(originalFilePath);
    // Tratamento para acessar a pasta dados
    nomeEntidade = nomeEntidade.replaceAll("_backup.db", "");
    String destinoFilePath = "dados/" + nomeEntidade + ".db";
    try {
      // Pega todos os bytes do arquivo específicado no arquivo .db de backup
      byte[] encodedBytes = Files.readAllBytes(Paths.get(originalFilePath));
      System.out.println("Leitura do arquivo " + nomeEntidade + " realizada com sucesso.");
      System.out.println("Tamanho do arquivo lido: " + encodedBytes.length);
      // Decofificação da mensagem
      byte[] msgDecodificada = LZW.decodifica(encodedBytes);

      // Limpar o conteúdo do arquivo de destino antes de escrever pra evitar qualquer erro
      File destinoFile = new File(destinoFilePath);
      if (destinoFile.exists()) {
        destinoFile.delete();
        destinoFile.createNewFile();
      }
      // Escrita no arquivo da pasta dados com as mensagens decodificadas
      try (RandomAccessFile dadosFile = new RandomAccessFile(destinoFile, "rw")) {
        dadosFile.write(msgDecodificada);
        System.out.println("Dados do arquivo " + nomeEntidade + " restaurado com sucesso.");
      }
    } catch (IOException e) {
      System.out.println("Erro ao ler o arquivo " + nomeEntidade + ".");
      e.printStackTrace();
    } catch (Exception e) {
      System.out.println("Erro ao decodificar o arquivo " + nomeEntidade + ".");
      e.printStackTrace();
    }
  }

  // Método de cópia dos arquivos de backup para os arquivos comuns
  public static void copyBackupParaArquivos(String nomeEntidade) throws Exception {
    // Percorre todos os arquivos dentro da pasta selecionada pelo usuário de backup
    File pasta = new File(nomeEntidade);
    File[] lista = pasta.listFiles();
    try {
      // Envia todos os arquivos _backup.db com seus respectivos paths para o método que descompacta os dados e os reescreve
      for (int i = 0; i < lista.length; i++) {
        copyRetorno(lista[i].getName(), lista[i].getPath());
      }
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  // Método para recuperar os backups e atualizar os bancos de dados

  public static void recuperarBackup() {
    File pasta = new File("backup/");
    File[] lista = pasta.listFiles();
    int opcao;
    System.out.println("Escolha uma opção de backup: ");
    for (int i = 0; i < lista.length; i++) {
      System.out.println("Opção " + i + ": " + lista[i]);
    }
    try {
      opcao = Integer.valueOf(console.nextLine());
    } catch (NumberFormatException e) {
      opcao = -1;
    }
    File backupEscolhido = lista[opcao];
    String path = backupEscolhido.getPath();
    try {
      copyBackupParaArquivos(path);
    } catch (Exception e) {
      System.out.println(e);
    }
  }

}
