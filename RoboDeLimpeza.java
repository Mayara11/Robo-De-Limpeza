import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class RoboDeLimpeza {
	
    private static class Coord {
        int x;
        int y;

        Coord (int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static class Quarto {
        char[][] estQuarto;
        char[][] ambiente;
        Coord posicaoRobo;
        int bolsaDeSujeira;
        int energia = 10;
    

        Quarto(char[][] ambiente, char[] pontosSujos, int energia) {
            this.ambiente = ambiente;
            this.estQuarto = new char[ambiente.length][ambiente[0].length];
            inicializarEstQuarto(pontosSujos);
            this.posicaoRobo = new Coord (0, 0);
             this.bolsaDeSujeira = 0;
            this.energia = energia;
            
        }

        private void inicializarEstQuarto(char[] pontosSujos) {
            for (int i = 0; i < estQuarto.length; i++) {
                for (int j = 0; j < estQuarto[i].length; j++) {
                    if (temPontoSujos(pontosSujos, ambiente[i][j])) {
                        estQuarto[i][j] = 'S';
                    } else {
                        estQuarto[i][j] = 'L';
                    }
                }
            }
        }


  private boolean temSujeira() {
            int x = posicaoRobo.x;
            int y = posicaoRobo.y;
            return x >= 0 && x < estQuarto[0].length && y >= 0 && y < estQuarto.length && estQuarto[y][x] == 'S';
        }

        private void limparSujeira() {
            int x = posicaoRobo.x;
            int y = posicaoRobo.y;
            estQuarto[y][x] = 'L';
        }

        private boolean todasAsCelulasEstaoLimpa() {
            for (char[] linha : estQuarto) {
                for (char celula : linha) {
                    if (celula == 'S') {
                        return false;
                    }
                }
            }
            return true;
        }

        public void moverRobo (char direcao) {
            int novaPosX = posicaoRobo.x;
            int novaPosY = posicaoRobo.y;

            switch (direcao) {
                case 'N':
                    if (novaPosY > 0) {
                        novaPosY--;
                    }
                    break;
                case 'S':
                    if (novaPosY < 3) {
                        novaPosY++;
                    }
                    break;
                case 'L':
                    if (novaPosX < 3) {
                        novaPosX++;
                    }
                    break;
                case 'O':
                    if (novaPosX > 0) {
                        novaPosX--;
                    }
                    break;
            }

            posicaoRobo = new Coord (novaPosX, novaPosY);
        }
    }

    public static void main(String[] args) {
        char[][] ambiente = new char[][]{
                {'A', 'B', 'C', 'D'},
                {'E', 'F', 'G', 'H'},
                {'I', 'J', 'K', 'L'},
                {'M', 'N', 'O', 'P'}
        };

        char[] pontosSujos = {'C', 'F', 'H', 'I', 'K', 'M', 'O'};

        Quarto quarto = new Quarto(ambiente, pontosSujos, 100);

        while (true) {
            char LocalizacaoAtual = quarto.ambiente[quarto.posicaoRobo.y][quarto.posicaoRobo.x];
            System.out.println("Posição do Robo: " + LocalizacaoAtual);

            if (quarto.temSujeira()) {
                System.out.println("Sujeira encontrada e limpeza concluida" + LocalizacaoAtual );
                quarto.limparSujeira();
                quarto.bolsaDeSujeira++;
                quarto.energia--;

                if (quarto.bolsaDeSujeira >= 10) {
                    System.out.println("Ei,a bolsa de sujeira está muito cheia. Estou Voltando para Casa (A) para esvaziar.");
                    quarto.energia -= calcularRotaParaCasa(quarto.posicaoRobo);
                    quarto.bolsaDeSujeira = 0;
                }
            }

            if (quarto.todasAsCelulasEstaoLimpa()) {
                System.out.println("O quarto está limpo");
                break;
            }

            char proximaAcao = determinarAcao(quarto);

            if (proximaAcao == 'M') {
                char direcao = determinarDirecao(quarto.posicaoRobo, quarto.estQuarto);
                quarto.moverRobo (direcao);
                quarto.energia--;
            } else if (proximaAcao == 'V') {
                String rotaParaCasa = identificarRotaDeVolta(quarto.posicaoRobo);
                System.out.println("Rota de volta para casa: " + rotaParaCasa);
                quarto.energia -= calcularRotaParaCasa(quarto.posicaoRobo);
                quarto.moverRobo ('N');
                quarto.energia--;
            }
        }
    }

    private static char determinarAcao(Quarto quarto) {
        if (quarto.temSujeira()) {
            if (quarto.energia > 0 && quarto.bolsaDeSujeira < 10) {
                return 'A';
            }else{
                return 'V';
            }
        }
        return 'M';

    }

    private static char determinarDirecao(Coord posicaoRobo, char[][] estQuarto) {
        Random random = new Random();
        char[] direcoesPossiveis = {'N', 'S', 'L', 'O'};
        List<Character> direcoesDisp = new ArrayList<>();

        for (char direcao : direcoesPossiveis) {
            if (direcaoLevaASujeira(direcao, posicaoRobo, estQuarto)) {
                direcoesDisp.add(direcao);
            }
        }

        if (direcoesDisp.isEmpty()) {
            return direcoesPossiveis[random.nextInt(4)];
        }

        return direcoesDisp.get(random.nextInt(direcoesDisp.size()));
    }

    private static boolean direcaoLevaASujeira(char direcao, Coord posicaoRobo, char[][] estQuarto) {
        int novaPosX = posicaoRobo.x;
        int novaPosY = posicaoRobo.y;

        switch (direcao) {
            case 'N':
                novaPosY--;
                break;
            case 'S':
                novaPosY++;
                break;
            case 'L':
                novaPosX++;
                break;
            case 'O':
                novaPosX--;
                break;
        }

        return novaPosX >= 0 && novaPosX < estQuarto[0].length && novaPosY >= 0 && novaPosY < estQuarto.length && estQuarto[novaPosY][novaPosX] == 'S';
    }

    private static String identificarRotaDeVolta(Coord posicaoRobo) {
        StringBuilder rota = new StringBuilder();
        while (posicaoRobo.x > 0) {
            rota.append('O');
            posicaoRobo.x--;
        }
        while (posicaoRobo.y > 0) {
            rota.append('N');
            posicaoRobo.y--;
        }
        return rota.toString();
    }

    private static int calcularRotaParaCasa(Coord posicaoRobo) {
        return posicaoRobo.x + posicaoRobo.y;
    }

    private static boolean temPontoSujos(char[] pontosSujos, char ponto) {
        for (char sujeira : pontosSujos) {
            if (sujeira == ponto) {
                return true;
            }
        }
        return false;
    }

}
