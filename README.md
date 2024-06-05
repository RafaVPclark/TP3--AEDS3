# TP3--AEDS3
## Métodos Implementados/Alterados
### NA CLASSE PRINCIPAL
* Case
Foram adicionados dois cases ( Realizar Backup e Recuperar Backup )

* Método backup()
Esse método adiciona na pasta backup uma nova pasta com a data atual do backup e pega seu path, logo em seguida ele acessa o método copy.
* Método copy()
Esse método é responsável por receber o nome do arquivo que ele precisa criar e o caminho necessário para alcançar a pasta backup, ele adiciona ao novo arquivo que será criado o nome _backup. Acessa o arquivo que foi passado como parâmetro da pasta "dados", transforma ele em um array de bytes e codifica ele usando lzw. Logo em seguida ele escreve essa mensagem codificada no arquivo de backup.
* Método recuperarBackup()
Faz um for dentro do diretório de backup, fornecendo para o usuário escolher uma das opções disponíveis de backups de acordo com a data. Após o usuário escolher qual backup ele deseja realizar é passado por parâmetro uma string com o path do diretório acessado para o método copyBackupParaArquivo()
* Método copyBackupParaArquivo()
Recebe o diretório da data escolhida pelo usuário, e lê arquivo por arquivo .db presentes na pasta, passando para um outro método responsável por copiar os dados do backup e atualizá-los na pasta dados.
* Método copyRetorno()
O método copyRetorno recebe como parâmetro o path da pasta de backup para acessar os dados e o nome do arquivo que ele precisa escrever nos dados. Porém, antes de mais nada foi necessário formatar o nome do arquivo pois ele estava com um _backup que foi adicionado no método de copy(). Após as corretas formatações da pasta de destino e da de origem, os dados foram copiados do backup, descodificados e enfim reescritos na pasta dados. Vale ressaltar que houveram problemas com escrita dos dados, para resolver isso nós apagamos o conteúdo do arquivo e escrevemos ele novamente.

### Opinião do grupo

Todos os objetivos foram alcançados, o grupo sentiu dificuldade em passar os dados dos arquivos de backup de volta para a pasta dados, devido a problemas de indexOutOfBoundries dos bytes(Conseguimos resolver). Também sofremos com a falta de conhecimento em navegar entre pastas, porém após estudar sobre a classe FILE vimos que existem métodos prontos que facilitam transitar entre pastas, tão como mostrar todas as pastas dentro de backup para o usuário escolher a data de backup.

# CHECKLIST

* Há uma rotina de compactação usando o algoritmo LZW para fazer backup dos arquivos? SIM
* Há uma rotina de descompactação usando o algoritmo LZW para recuperação dos arquivos? SIM
* O usuário pode escolher a versão a recuperar? SIM
* Qual foi a taxa de compressão alcançada por esse backup? (Compare o tamanho dos arquivos compactados com os arquivos originais)
* O trabalho está funcionando corretamente? SIM
* O trabalho está completo? SIM 
* O trabalho é original e não a cópia de um trabalho de um colega? SIM
