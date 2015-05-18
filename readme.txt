
Descrição dos diretórios do projeto:

/bin/: Executaveis gerados
/src/: Codigos fonte
/res/: Arquivos de entrada do programa
/out/: Arquivos de saida gerados


Parametros para execucao do sistema, devem ser inseridos nesta ordem e com separacao por espaco na linha de execucao:

1- Nome do arquivo do conjunto de dados de treino (String)
2- Nome do arquivo do conjunto de dados de validacao (String)
3- Nome do arquivo do conjunto de dados de teste (String)
4- Taxa de aprendizado inicial (float)
5- Numero de neuronios na camada escondida da MLP (int)
6- Numero de neuronios de saida da LVQ (int)
7- Inicializacao aleatoria de pesos e bias (true ou false) (boolean)
8- Quantidade de epocas totais (int)
9- A cada quantas epocas sera feita a validacao do erro (int)