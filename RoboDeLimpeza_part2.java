

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

          