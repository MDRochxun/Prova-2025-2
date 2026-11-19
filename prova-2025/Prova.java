import java.util.Scanner;

public class Prova {

    static final int TAM_CONTAS = 100;
    static final int TAM_OP = 1000;

    static class Conta {
        int id;
        String cliente;
        double saldo;
        double limite;
    }

    static class Operacao {
        int id;
        int idConta;
        char tipo;
        double valor;
    }

    public static int buscaId(Conta[] v, int tam, int id) {
        for (int i = 0; i < tam; i++) {
            if (v[i].id == id) {
                return i;
            }
        }
        return -1;
    }

    public static int cadastrarConta(Conta[] v, int tam, Scanner sc) {
        if (tam >= TAM_CONTAS) {
            System.out.println("Erro: Vetor de contas cheio!");
            return tam;
        }

        System.out.print("Digite o ID da conta: ");
        int id = sc.nextInt();
        sc.nextLine();

        if (buscaId(v, tam, id) != -1) {
            System.out.println("Erro: ID já cadastrado!");
            return tam;
        }

        System.out.print("Digite o nome do cliente: ");
        String cliente = sc.nextLine();

        System.out.print("Digite o saldo inicial: ");
        double saldo = sc.nextDouble();

        System.out.print("Digite o limite: ");
        double limite = sc.nextDouble();

        if (saldo < 0 || limite < 0) {
            System.out.println("Erro: Saldo ou limite negativos!");
            return tam;
        }

        v[tam] = new Conta();
        v[tam].id = id;
        v[tam].cliente = cliente;
        v[tam].saldo = saldo;
        v[tam].limite = limite;

        System.out.println("Conta cadastrada com sucesso!");
        return tam + 1;
    }

    public static int buscaBinariaCliente(Conta[] v, int tam, String x) {
        int inicio = 0;
        int fim = tam - 1;

        while (inicio <= fim) {
            int meio = (inicio + fim) / 2;
            int comparacao = v[meio].cliente.compareToIgnoreCase(x);

            if (comparacao == 0) {
                return meio;
            } else if (comparacao < 0) {
                inicio = meio + 1;
            } else {
                fim = meio - 1;
            }
        }
        return -1;
    }

    public static int insertionSortCliente(Conta[] v, int tam) {
        for (int i = 1; i < tam; i++) {
            Conta chave = v[i];
            int j = i - 1;

            while (j >= 0 && v[j].cliente.compareToIgnoreCase(chave.cliente) > 0) {
                v[j + 1] = v[j];
                j--;
            }
            v[j + 1] = chave;
        }
        return tam;
    }

    public static int filtraOperacoes(Operacao[] vOp, int tamOp, Operacao[] vOpFiltrado, int x) {
        int qtd = 0;
        for (int i = 0; i < tamOp; i++) {
            if (vOp[i].idConta == x) {
                vOpFiltrado[qtd] = vOp[i];
                qtd++;
            }
        }
        return qtd;
    }

    public static void extrato(Conta[] vContas, int tamConta, Operacao[] vOp, int tamOp, Scanner sc) {
        System.out.print("ID da conta: ");
        int idConta = sc.nextInt();

        int posConta = buscaId(vContas, tamConta, idConta);
        if (posConta == -1) {
            System.out.println("Erro: Conta não encontrada!");
            return;
        }

        Conta conta = vContas[posConta];
        Operacao[] vOpFiltrado = new Operacao[TAM_OP];
        int qtdOpConta = filtraOperacoes(vOp, tamOp, vOpFiltrado, idConta);

        double saldoCorrente = 0.0;
        double[] historicoSaldos = new double[qtdOpConta];

        for (int i = 0; i < qtdOpConta; i++) {
            Operacao op = vOpFiltrado[i];
            if (op.tipo == 'D' || (op.tipo == 'T' && op.valor >= 0)) {
                saldoCorrente += Math.abs(op.valor);
            } else {
                saldoCorrente -= Math.abs(op.valor);
            }
            historicoSaldos[i] = saldoCorrente;
        }

        int inicioExibicao = Math.max(0, qtdOpConta - 5);
        double saldoAnterior = (inicioExibicao == 0) ? 0.0 : historicoSaldos[inicioExibicao - 1];

        System.out.println("N." + conta.id + " - CLIENTE: " + conta.cliente);
        
        char tipoSaldoAnt = (saldoAnterior >= 0) ? 'C' : 'D';
        System.out.printf("Saldo Anterior: %.2f%c\n", Math.abs(saldoAnterior), tipoSaldoAnt);

        for (int i = inicioExibicao; i < qtdOpConta; i++) {
            Operacao op = vOpFiltrado[i];
            String descTipo = (op.tipo == 'D') ? "Depósito" : (op.tipo == 'S') ? "Saque" : "Transferência";
            char dC = (op.tipo == 'D' || (op.tipo == 'T' && op.valor >= 0)) ? 'C' : 'D';
            char sC = (historicoSaldos[i] >= 0) ? 'C' : 'D';

            System.out.printf("%05d - %s - %.2f%c - %.2f%c\n", 
                op.id, descTipo, Math.abs(op.valor), dC, Math.abs(historicoSaldos[i]), sC);
        }

        char tipoSaldoAtual = (saldoCorrente >= 0) ? 'C' : 'D';
        System.out.printf("Saldo Atual: %.2f%c\n", Math.abs(saldoCorrente), tipoSaldoAtual);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Conta[] vContas = new Conta[TAM_CONTAS];
        Operacao[] vOp = new Operacao[TAM_OP];
        int tamConta = 0;
        int tamOp = 0;

        int opcao;
        do {
            System.out.println("\n1 - Cadastrar Conta");
            System.out.println("2 - Ordenar Contas por Cliente");
            System.out.println("3 - Buscar Conta por Cliente");
            System.out.println("4 - Emitir Extrato");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");
            opcao = sc.nextInt();
            sc.nextLine();

            if (opcao == 1) {
                tamConta = cadastrarConta(vContas, tamConta, sc);
            } else if (opcao == 2) {
                insertionSortCliente(vContas, tamConta);
                System.out.println("Contas ordenadas com sucesso!");
            } else if (opcao == 3) {
                System.out.print("Nome do cliente: ");
                String nome = sc.nextLine();
                int pos = buscaBinariaCliente(vContas, tamConta, nome);
                if (pos != -1) {
                    System.out.println("Conta encontrada: ID " + vContas[pos].id + " - Saldo: " + vContas[pos].saldo);
                } else {
                    System.out.println("Cliente não encontrado!");
                }
            } else if (opcao == 4) {
                extrato(vContas, tamConta, vOp, tamOp, sc);
            }
        } while (opcao != 0);

        sc.close();
    }
}
