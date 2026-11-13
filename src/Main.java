import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    // Map para guardar as matrizes na memoria, um array bidimensional para representar linhas e colunas
    private static final Map<String, double[][]> matrizes = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao = -1;

        lerArquivoMatrizes("matrizes.txt");

        while (opcao != 0) {
            exibirMenu();
            System.out.print("Escolha uma opção: ");

            // Tratamento básico para evitar que o programa quebre se digitar letra
            if (scanner.hasNextInt()) {
                opcao = scanner.nextInt();
                scanner.nextLine(); // Consome a quebra de linha após o número

                switch (opcao) {
                    case 1 : realizarSoma(scanner);
                        break;
                    case 2 : listarMatrizesCarregadas();
                        break;
                    case 3 : transporMatriz(scanner);
                        break;
                    case 4 : realizarMultiplicacao(scanner);
                        break;
                    case 0 : System.out.println("Encerrando o programa...");
                        break;
                    default : System.out.println("Opção inválida. Tente novamente.\n");
                }
            } else {
                System.out.println("Por favor, digite um número válido.\n");
                scanner.nextLine(); // Limpa o buffer de erro
            }
        }
    }

    private static void exibirMenu() {
        String menu = """
                ===================================
                |   CALCULADORA 2000 DE MATRIZES  |
                ===================================
                | 1. Somar matrizes               |
                | 2. Ver matrizes carregadas      |
                | 3. Transpor matriz              |
                | 4. Multiplicar matrizes         |
                | 0. Sair                         |
                ===================================
                """;
        System.out.println(menu);
    }

    private static void lerArquivoMatrizes(String filePath) {
        File file = new File(filePath);

        try (Scanner fileScanner = new Scanner(file)) {
            matrizes.clear(); // Limpa a memória caso leia mais de uma vez

            String actualName = null;
            // ArrayList ao invés de array, para poder armazenar as linhas dinamicamente e identificar o tamanho de cada matriz
            List<double[]> dynamicLines = new ArrayList<>();

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();

                if (line.length() == 0) continue; //Pula linhas em branco

                //Regex pra ver se a linha contém letras (identificar nome da matriz)
                if (line.matches(".*[a-zA-Z].*")) {
                    //
                    if (actualName != null && !(dynamicLines.size() == 0)) {
                        saveOnMap(actualName, dynamicLines);
                        dynamicLines.clear(); // Limpa a lista pra converter a próxima matriz do .txt
                    }
                    actualName = line;
                } else {
                    // é uma linha de numeros, com valores de verdade
                    String[] stringValues = line.split("\\s+");
                    double[] lineNumbers = new double[stringValues.length];

                    for (int i = 0; i < stringValues.length; i++) {
                        lineNumbers[i] = Double.parseDouble(stringValues[i]);
                    }

                    dynamicLines.add(lineNumbers);
                }
            }

            // Salva a ultima matriz que fica sobrando na memória quando o loop termina
            if (actualName != null && !(dynamicLines.size() == 0)) {
                saveOnMap(actualName, dynamicLines);
            }

            System.out.println("Matrizes encontradas: " + matrizes.size());
        } catch (FileNotFoundException e) {
            System.out.println("Erro: Arquivo '" + filePath + "' não encontrado.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao ler arquivo");
        }
    }

    // Mét0do auxiliar só pra converter o arraylist no double[][] final e colocar no Map
    private static void saveOnMap(String name, List<double[]> dynamicLines) {
        int linesQuantity = dynamicLines.size();
        int columnsQuantity = dynamicLines.get(0).length;

        double[][] finalMatriz = new double[linesQuantity][columnsQuantity];

        for (int i = 0; i < linesQuantity; i++) {
            finalMatriz[i] = dynamicLines.get(i);
        }

        matrizes.put(name, finalMatriz);
    }

    private static void listarMatrizesCarregadas() {
        if (matrizes.size() == 0) {
            System.out.println("Nenhum matriz encontrada.");
            return;
        }

        System.out.println("--- Matrizes ---");
        for (Map.Entry<String, double[][]> entry : matrizes.entrySet()) {
            System.out.println("Matriz " + entry.getKey() + ":");
            double[][] matriz = entry.getValue();

            for (double[] linha : matriz) {
                for (double valor : linha) {
                    System.out.printf("%.0f\t", valor);
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    public static void realizarSoma(Scanner scanner) {
        System.out.print("Digite o nome da primeira matriz: ");
        String nome1 = scanner.nextLine().trim();

        System.out.print("Digite o nome da segunda matriz: ");
        String nome2 = scanner.nextLine().trim();

        double[][] m1 = matrizes.get(nome1);
        double[][] m2 = matrizes.get(nome2);

        if (m1 == null || m2 == null) {
            System.out.println("Erro: Uma das matrizes não existe, digite novamente.\n");
            return;
        }

        try {
            double[][] resultado = Calculos.somar(m1, m2);

            System.out.println("Resultado da Soma:");
            imprimirResultado(resultado);
        } catch (RuntimeException e) {
            System.out.println("Erro: " + e.getMessage() + "\n");
        }
    }

    public static void transporMatriz(Scanner scanner) {
        System.out.print("Digite o nome da matriz: ");
        String nome1 = scanner.nextLine().trim();

        double[][] m = matrizes.get(nome1);

        if (m == null) {
            System.out.println("Erro: Essa matriz não existe, digite novamente.\n");
            return;
        }

        try {
            double[][] resultado = Calculos.transpor(m);

            System.out.printf("Resultado da Transposta de %s: %n", nome1);
            imprimirResultado(resultado);
        } catch (RuntimeException e) {
            System.out.println("Erro: " + e.getMessage() + "\n");
        }
    }

    public static void realizarMultiplicacao(Scanner scanner) {
        System.out.print("Digite o nome da primeira matriz: ");
        String nome1 = scanner.nextLine().trim();

        System.out.print("Digite o nome da segunda matriz: ");
        String nome2 = scanner.nextLine().trim();

        double[][] m1 = matrizes.get(nome1);
        double[][] m2 = matrizes.get(nome2);

        if (m1 == null || m2 == null) {
            System.out.println("Erro: Uma das matrizes não existe, digite novamente.\n");
            return;
        }

        try {
            double[][] resultado = Calculos.multiplicar(m1, m2);

            System.out.println("Resultado da Multiplicação:");
            imprimirResultado(resultado);
        } catch (RuntimeException e) {
            System.out.println("Erro: " + e.getMessage() + "\n");
        }
    }

    private static void imprimirResultado(double[][] matriz) {
        for (double[] linha : matriz) {
            for (double valor : linha) {
                System.out.printf("%.0f\t", valor);
            }
            System.out.println();
        }
        System.out.println();
    }
}