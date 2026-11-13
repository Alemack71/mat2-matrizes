public class Calculos {
    public static double[][] somar(double[][] matriz1, double[][] matriz2) {
        // Descobrindo dimensões das matrizes, pois para somar as duas precisam ter extamente as mesmas dimensões
        int linhas1 = matriz1.length;
        int colunas1 = matriz1[0].length;

        int linhas2 = matriz2.length;
        int colunas2 = matriz2[0].length;

        // Não têm a mesma dimensão então não pode prosseguir
        if (linhas1 != colunas1 || linhas2 != colunas2) {
            throw new RuntimeException("Erro: As matrizes têm dimensões diferentes");
        }

        double[][] resultado = new double[linhas1][colunas1];

        // Loop duplo para chegar nas posições correspondentes
        for (int i = 0; i < linhas1; i++) {
            for (int j = 0; j < colunas1; j++) {
                resultado[i][j] = matriz1[i][j] + matriz2[i][j];
            }
        }
        return resultado;
    }

    public static double[][] transpor(double[][] matriz) {
        int linhas = matriz.length;
        int colunas = matriz[0].length;

        // Cria matriz pra guardar o resultado com as dimensões invertidas
        double[][] resultado = new double[linhas][colunas];

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                // Linha vira coluna e coluna vira linha
                resultado[j][i] = matriz[i][j];
            }
        }

        return resultado;
    }

    public static double[][] multiplicar(double[][] matriz1, double[][] matriz2) {
        int linhas1 = matriz1.length;
        int colunas1 = matriz1[0].length;

        int linhas2 = matriz2.length;
        int colunas2 = matriz2[0].length;

        // Validação se a coluna da primeira tem a mesma quantidade de linhas da segunda
        if (colunas1 != linhas2) {
            throw new RuntimeException("Erro: O número de colunas da primeira matriz deve ser igual ao número de linhas da segunda");
        }

        // A matriz resultante tera a quantidade de linhas da primeira matriz e a quantidade de colunas da segunda
        double[][] resultado = new double[linhas1][colunas2];

        for (int i = 0; i < linhas1; i++) { // fixa a linha da primeira matriz
            for (int j = 0; j < colunas2; j++) { // fixa a coluna da segunda matriz
                for (int k = 0; k < colunas1; k++) { // o indice k percorre os elementos a serem multiplicados
                    resultado[i][j] += matriz1[i][k] * matriz2[k][j]; // percorrendo linha do 1° pela coluna do 2°
                }
            }
        }

        return resultado;
    }

    public static double determinante(double[][] matriz) {
        int linhas = matriz.length;
        int colunas = matriz[0].length;

        // Validação pois a matriz precisa ser quadrada antes de qualquer calculo
        if  (linhas != colunas) {
            throw new RuntimeException("Erro: O determinante só pode ser obtido em matrizes quadradas");
        }

        if (linhas == 2) {
            // Mini Sarrus
            return (matriz[0][0] * matriz[1][1] - matriz[0][1] * matriz[1][0]);
        }
        else if (linhas == 3) {
            // Sarrus
            double primeiro = (matriz[0][0] * matriz[1][1] * matriz[2][2]) +
                    (matriz[0][1] * matriz[1][2] * matriz[2][0]) +
                    (matriz[0][2] * matriz[1][0] * matriz[2][1]);

            double segundo = (matriz[0][2] * matriz[1][1] * matriz[2][0]) +
                    (matriz[0][0] * matriz[1][2] * matriz[2][1]) +
                    (matriz[0][1] * matriz[1][0] * matriz[2][2]);

            return primeiro - segundo;
        }
        else {
            // 4x4 por Laplace
            double det = 0.0;

            for (int j = 0; j < linhas; j++) {
                double[][] submatriz = gerarSubmatriz(matriz, 0, j);

                double sinal = Math.pow(-1, 0 + j);

                double cofator = matriz[0][j] * sinal * determinante(submatriz);

                det += cofator;
            }

            return det;
        }
    }

    private static double[][] gerarSubmatriz(double[][] original, int linhaExcluida, int colunaExcluida) {
        int linhas = original.length;
        int colunas = original[0].length;

        double[][] submatriz = new double[linhas - 1][colunas - 1];
        int linhaSub = 0;

        for (int i = 0; i < linhas; i++) {
            if (i == linhaExcluida) continue; // Pula a linha cortada

            int colunaSub = 0;
            for  (int j = 0; j < colunas; j++) {
                if (j == colunaExcluida) continue; // Pula a coluna cortada

                submatriz[linhaSub][colunaSub] = original[i][j];
                colunaSub++;
            }
            linhaSub++;
        }
        return  submatriz;
    }
}
