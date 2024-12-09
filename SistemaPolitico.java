import javax.swing.*;
import java.sql.*;
import java.sql.Date;

public class SistemaPolitico {
    private static Connection connection;
    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sistemapolitico2", "root", "1234");

            while (true) {
                String menu = """
                        Escolha a tabela para gerenciar:
                        1. Eleição
                        2. Campanha
                        3. Candidato
                        4. Cargo Interno
                        5. Filiado
                        6. Evento
                        7. Doação
                        8. Sair
                        """;

                String opcaoStr = JOptionPane.showInputDialog(menu);
                int opcao = Integer.parseInt(opcaoStr);

                
                    switch (opcao) {
                        case 1: EleicaoCRUD();break;
                        case 2: CampanhaCRUD();break;
                        case 3: CandidatoCRUD();break;
                        case 4: CargoInternoCRUD();break;
                        case 5: FiliadoCRUD();break;
                        case 6: EventoCRUD();break;
                        case 7: DoacaoCRUD();break;
                        case 8: {
                            JOptionPane.showMessageDialog(null, "Encerrando o programa.");
                            System.exit(0);
                        }
                        default: JOptionPane.showMessageDialog(null, "Opção inválida!");
                    }
                }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
        }
     
    }

    private static void EleicaoCRUD() {
        String menuEleicao = "Escolha a operação para Eleições:\n" +
                "1. Criar Eleição\n" +
                "2. Ler Eleição\n" +
                "3. Atualizar Eleição\n" +
                "4. Deletar Eleição\n" +
                "5. Voltar";

        String opcaoStr = JOptionPane.showInputDialog(null, menuEleicao);
        if (opcaoStr == null)
            return;
        int opcao = Integer.parseInt(opcaoStr);

        switch (opcao) {
            case 1 -> criarEleicao();
            case 2 -> lerEleicao();
            case 3 -> atualizarEleicao();
            case 4 -> deletarEleicao();
            case 5 -> {
            }
            default -> JOptionPane.showMessageDialog(null, "Opção inválida!");
        }
    }

    private static void criarEleicao() {
        try {
            String anoStr = JOptionPane.showInputDialog("Ano da eleição:");
            int ano = Integer.parseInt(anoStr);
            String resultado = JOptionPane.showInputDialog("Resultado:");
            String descricao = JOptionPane.showInputDialog("Descrição:");

            String query = "INSERT INTO eleicao (ano, resultado, descricao) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, ano);
                stmt.setString(2, resultado);
                stmt.setString(3, descricao);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Eleição criada com sucesso!");
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Erro ao criar eleição: " + e.getMessage());
        }
    }

    private static void lerEleicao() {
        try {
            String idStr = JOptionPane.showInputDialog("Digite o ID da eleição:");
            int id = Integer.parseInt(idStr);

            String query = "SELECT * FROM eleicao WHERE id_eleicao = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String info = "ID: " + rs.getInt("id_eleicao") + "\n" +
                            "Ano: " + rs.getInt("ano") + "\n" +
                            "Resultado: " + rs.getString("resultado") + "\n" +
                            "Descrição: " + rs.getString("descricao");
                    JOptionPane.showMessageDialog(null, info);
                } else {
                    JOptionPane.showMessageDialog(null, "Eleição não encontrada.");
                }
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Erro ao ler eleição: " + e.getMessage());
        }
    }

    private static void atualizarEleicao() {
        try {
            String idStr = JOptionPane.showInputDialog("Digite o ID da eleição para atualizar:");
            int id = Integer.parseInt(idStr);

            String anoStr = JOptionPane.showInputDialog("Novo ano:");
            int ano = Integer.parseInt(anoStr);
            String resultado = JOptionPane.showInputDialog("Novo resultado:");
            String descricao = JOptionPane.showInputDialog("Nova descrição:");

            String query = "UPDATE eleicao SET ano = ?, resultado = ?, descricao = ? WHERE id_eleicao = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, ano);
                stmt.setString(2, resultado);
                stmt.setString(3, descricao);
                stmt.setInt(4, id);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Eleição atualizada com sucesso!");
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar eleição: " + e.getMessage());
        }
    }

    private static void deletarEleicao() {
        try {
            String idStr = JOptionPane.showInputDialog("Digite o ID da eleição para deletar:");
            int id = Integer.parseInt(idStr);

            String query = "DELETE FROM eleicao WHERE id_eleicao = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Eleição deletada com sucesso!");
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar eleição: " + e.getMessage());
        }
    }

    public static void CampanhaCRUD() {

        while (true) {
            String menu = """
                    Escolha uma operação para Campanha:
                    1. Criar Campanha
                    2. Consultar Campanhas
                    3. Atualizar Campanha
                    4. Deletar Campanha
                    5. Sair
                    """;

            String opcaoStr = JOptionPane.showInputDialog(menu);
            int opcao = Integer.parseInt(opcaoStr);

            switch (opcao) {
                case 1 -> criarCampanha();
                case 2 -> consultarCampanhas();
                case 3 -> atualizarCampanha();
                case 4 -> deletarCampanha();
                case 5 -> System.exit(0);
                default -> JOptionPane.showMessageDialog(null, "Opção inválida!");
            }
        }
    }

    private static void criarCampanha() {
        String nome = JOptionPane.showInputDialog("Nome da Campanha:");
        String orcamentoEstimadoStr = JOptionPane.showInputDialog("Orçamento Estimado:");
        double orcamentoEstimado = Double.parseDouble(orcamentoEstimadoStr);
        String valorGastoStr = JOptionPane.showInputDialog("Valor Gasto:");
        double valorGasto = Double.parseDouble(valorGastoStr);
        String dataInicio = JOptionPane.showInputDialog("Data de Início (YYYY-MM-DD):");
        String dataTermino = JOptionPane.showInputDialog("Data de Término (YYYY-MM-DD):");
        String eleicaoIdStr = JOptionPane.showInputDialog("ID da Eleição associada:");
        int eleicaoId = Integer.parseInt(eleicaoIdStr);

        String query = """
                INSERT INTO campanha (nome, orcamento_estimado, valor_gasto, data_inicio, data_termino, eleicao_id_eleicao)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nome);
            stmt.setDouble(2, orcamentoEstimado);
            stmt.setDouble(3, valorGasto);
            stmt.setString(4, dataInicio);
            stmt.setString(5, dataTermino);
            stmt.setInt(6, eleicaoId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Campanha criada com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao criar campanha: " + e.getMessage());
        }
    }

    private static void consultarCampanhas() {
        String query = "SELECT * FROM campanha";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            StringBuilder resultado = new StringBuilder("Campanhas:\n");

            while (rs.next()) {
                resultado.append("ID: ").append(rs.getInt("id_campanha"))
                        .append(", Nome: ").append(rs.getString("nome"))
                        .append(", Orçamento Estimado: ").append(rs.getDouble("orcamento_estimado"))
                        .append(", Valor Gasto: ").append(rs.getDouble("valor_gasto"))
                        .append(", Data Início: ").append(rs.getDate("data_inicio"))
                        .append(", Data Término: ").append(rs.getDate("data_termino"))
                        .append(", ID Eleição: ").append(rs.getInt("eleicao_id_eleicao"))
                        .append("\n");
            }

            JOptionPane.showMessageDialog(null, resultado.toString());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar campanhas: " + e.getMessage());
        }
    }

    private static void atualizarCampanha() {
        String idStr = JOptionPane.showInputDialog("ID da Campanha para atualizar:");
        int id = Integer.parseInt(idStr);

        String novoNome = JOptionPane.showInputDialog("Novo Nome da Campanha:");
        String novoOrcamentoEstimadoStr = JOptionPane.showInputDialog("Novo Orçamento Estimado:");
        double novoOrcamentoEstimado = Double.parseDouble(novoOrcamentoEstimadoStr);
        String novoValorGastoStr = JOptionPane.showInputDialog("Novo Valor Gasto:");
        double novoValorGasto = Double.parseDouble(novoValorGastoStr);
        String novaDataInicio = JOptionPane.showInputDialog("Nova Data de Início (YYYY-MM-DD):");
        String novaDataTermino = JOptionPane.showInputDialog("Nova Data de Término (YYYY-MM-DD):");

        String query = """
                UPDATE campanha
                SET nome = ?, orcamento_estimado = ?, valor_gasto = ?, data_inicio = ?, data_termino = ?
                WHERE id_campanha = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, novoNome);
            stmt.setDouble(2, novoOrcamentoEstimado);
            stmt.setDouble(3, novoValorGasto);
            stmt.setString(4, novaDataInicio);
            stmt.setString(5, novaDataTermino);
            stmt.setInt(6, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Campanha atualizada com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar campanha: " + e.getMessage());
        }
    }

    private static void deletarCampanha() {
        String idStr = JOptionPane.showInputDialog("ID da Campanha para deletar:");
        int id = Integer.parseInt(idStr);

        String query = "DELETE FROM campanha WHERE id_campanha = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Campanha deletada com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar campanha: " + e.getMessage());
        }
    }

    public static void CandidatoCRUD() {
        while (true) {
            String menu = """
                    Escolha uma operação para Candidato:
                    1. Criar Candidato
                    2. Consultar Candidatos
                    3. Atualizar Candidato
                    4. Deletar Candidato
                    5. Sair
                    """;

            String opcaoStr = JOptionPane.showInputDialog(menu);
            int opcao = Integer.parseInt(opcaoStr);

            switch (opcao) {
                case 1 -> criarCandidato();
                case 2 -> consultarCandidatos();
                case 3 -> atualizarCandidato();
                case 4 -> deletarCandidato();
                case 5 -> System.exit(0);
                default -> JOptionPane.showMessageDialog(null, "Opção inválida!");
            }
        }
    }

    private static void criarCandidato() {
        String nome = JOptionPane.showInputDialog("Nome do Candidato:");
        String cargoPublico = JOptionPane.showInputDialog("Cargo Público (se houver):");
        String estado = JOptionPane.showInputDialog("Estado:");
        String cidade = JOptionPane.showInputDialog("Cidade:");
        String campanhaIdStr = JOptionPane.showInputDialog("ID da Campanha associada:");
        int campanhaId = Integer.parseInt(campanhaIdStr);

        String query = """
                INSERT INTO candidato (nome, cargo_publico, estado, cidade, campanha_id_campanha)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nome);
            stmt.setString(2, cargoPublico);
            stmt.setString(3, estado);
            stmt.setString(4, cidade);
            stmt.setInt(5, campanhaId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Candidato criado com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao criar candidato: " + e.getMessage());
        }
    }

    private static void consultarCandidatos() {
        String query = "SELECT * FROM candidato";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            StringBuilder resultado = new StringBuilder("Candidatos:\n");

            while (rs.next()) {
                resultado.append("ID: ").append(rs.getInt("id_candidato"))
                        .append(", Nome: ").append(rs.getString("nome"))
                        .append(", Cargo Público: ").append(rs.getString("cargo_publico"))
                        .append(", Estado: ").append(rs.getString("estado"))
                        .append(", Cidade: ").append(rs.getString("cidade"))
                        .append(", ID Campanha: ").append(rs.getInt("campanha_id_campanha"))
                        .append("\n");
            }

            JOptionPane.showMessageDialog(null, resultado.toString());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar candidatos: " + e.getMessage());
        }
    }

    private static void atualizarCandidato() {
        String idStr = JOptionPane.showInputDialog("ID do Candidato para atualizar:");
        int id = Integer.parseInt(idStr);

        String novoNome = JOptionPane.showInputDialog("Novo Nome do Candidato:");
        String novoCargoPublico = JOptionPane.showInputDialog("Novo Cargo Público (se houver):");
        String novoEstado = JOptionPane.showInputDialog("Novo Estado:");
        String novaCidade = JOptionPane.showInputDialog("Nova Cidade:");

        String query = """
                UPDATE candidato
                SET nome = ?, cargo_publico = ?, estado = ?, cidade = ?
                WHERE id_candidato = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, novoNome);
            stmt.setString(2, novoCargoPublico);
            stmt.setString(3, novoEstado);
            stmt.setString(4, novaCidade);
            stmt.setInt(5, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Candidato atualizado com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar candidato: " + e.getMessage());
        }
    }

    private static void deletarCandidato() {
        String idStr = JOptionPane.showInputDialog("ID do Candidato para deletar:");
        int id = Integer.parseInt(idStr);

        String query = "DELETE FROM candidato WHERE id_candidato = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Candidato deletado com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar candidato: " + e.getMessage());
        }
    }

    public static void CargoInternoCRUD() {
        while (true) {
            String menu = """
                    Escolha uma operação para Cargo Interno:
                    1. Criar Cargo Interno
                    2. Consultar Cargos Internos
                    3. Atualizar Cargo Interno
                    4. Deletar Cargo Interno
                    5. Sair
                    """;

            String opcaoStr = JOptionPane.showInputDialog(menu);
            int opcao = Integer.parseInt(opcaoStr);

            switch (opcao) {
                case 1 -> criarCargoInterno();
                case 2 -> consultarCargosInternos();
                case 3 -> atualizarCargoInterno();
                case 4 -> deletarCargoInterno();
                case 5 -> System.exit(0);
                default -> JOptionPane.showMessageDialog(null, "Opção inválida!");
            }
        }
    }

    private static void criarCargoInterno() {
        String tipoCargo = JOptionPane.showInputDialog("Tipo do Cargo:");
        String dataInicio = JOptionPane.showInputDialog("Data de Início (YYYY-MM-DD):");

        String query = """
                INSERT INTO cargo_interno (tipo_cargo, data_inicio)
                VALUES (?, ?)
                """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, tipoCargo);
            stmt.setDate(2, Date.valueOf(dataInicio));
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Cargo Interno criado com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao criar Cargo Interno: " + e.getMessage());
        }
    }

    private static void consultarCargosInternos() {
        String query = "SELECT * FROM cargo_interno";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            StringBuilder resultado = new StringBuilder("Cargos Internos:\n");

            while (rs.next()) {
                resultado.append("ID: ").append(rs.getInt("id_cargo_interno"))
                        .append(", Tipo do Cargo: ").append(rs.getString("tipo_cargo"))
                        .append(", Data de Início: ").append(rs.getDate("data_inicio"))
                        .append("\n");
            }

            JOptionPane.showMessageDialog(null, resultado.toString());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar Cargos Internos: " + e.getMessage());
        }
    }

    private static void atualizarCargoInterno() {
        String idStr = JOptionPane.showInputDialog("ID do Cargo Interno para atualizar:");
        int id = Integer.parseInt(idStr);

        String novoTipoCargo = JOptionPane.showInputDialog("Novo Tipo do Cargo:");
        String novaDataInicio = JOptionPane.showInputDialog("Nova Data de Início (YYYY-MM-DD):");

        String query = """
                UPDATE cargo_interno
                SET tipo_cargo = ?, data_inicio = ?
                WHERE id_cargo_interno = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, novoTipoCargo);
            stmt.setDate(2, Date.valueOf(novaDataInicio));
            stmt.setInt(3, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Cargo Interno atualizado com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar Cargo Interno: " + e.getMessage());
        }
    }

    private static void deletarCargoInterno() {
        String idStr = JOptionPane.showInputDialog("ID do Cargo Interno para deletar:");
        int id = Integer.parseInt(idStr);

        String query = "DELETE FROM cargo_interno WHERE id_cargo_interno = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Cargo Interno deletado com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar Cargo Interno: " + e.getMessage());
        }
    }

    public static void FiliadoCRUD() {
        while (true) {
            String menu = """
                    Escolha uma operação para Filiado:
                    1. Criar Filiado
                    2. Consultar Filiados
                    3. Atualizar Filiado
                    4. Deletar Filiado
                    5. Sair
                    """;

            String opcaoStr = JOptionPane.showInputDialog(menu);
            int opcao = Integer.parseInt(opcaoStr);

            switch (opcao) {
                case 1 -> criarFiliado();
                case 2 -> consultarFiliados();
                case 3 -> atualizarFiliado();
                case 4 -> deletarFiliado();
                case 5 -> System.exit(0);
                default -> JOptionPane.showMessageDialog(null, "Opção inválida!");
            }
        }
    }

    private static void criarFiliado() {
        String cpf = JOptionPane.showInputDialog("CPF (11 dígitos):");
        String nome = JOptionPane.showInputDialog("Nome:");
        String fone = JOptionPane.showInputDialog("Telefone:");
        String dataFiliacao = JOptionPane.showInputDialog("Data de Filiação (YYYY-MM-DD):");
        String logradouro = JOptionPane.showInputDialog("Logradouro:");
        String numero = JOptionPane.showInputDialog("Número:");
        String cidade = JOptionPane.showInputDialog("Cidade:");
        String estado = JOptionPane.showInputDialog("Estado:");
        String cargoInternoId = JOptionPane.showInputDialog("ID do Cargo Interno:");

        String query = """
                INSERT INTO filiado (cpf, nome, fone, data_filiacao, logradouro, numero, cidade, estado, cargo_interno_id_cargo_interno)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, cpf);
            stmt.setString(2, nome);
            stmt.setString(3, fone);
            stmt.setDate(4, Date.valueOf(dataFiliacao));
            stmt.setString(5, logradouro);
            stmt.setString(6, numero);
            stmt.setString(7, cidade);
            stmt.setString(8, estado);
            stmt.setInt(9, Integer.parseInt(cargoInternoId));
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Filiado criado com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao criar Filiado: " + e.getMessage());
        }
    }

    private static void consultarFiliados() {
        String query = "SELECT * FROM filiado";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            StringBuilder resultado = new StringBuilder("Filiados:\n");

            while (rs.next()) {
                resultado.append("ID: ").append(rs.getInt("id_filiado"))
                        .append(", CPF: ").append(rs.getString("cpf"))
                        .append(", Nome: ").append(rs.getString("nome"))
                        .append(", Telefone: ").append(rs.getString("fone"))
                        .append(", Data Filiação: ").append(rs.getDate("data_filiacao"))
                        .append(", Endereço: ").append(rs.getString("logradouro")).append(", ")
                        .append(rs.getString("numero"))
                        .append(", Cidade: ").append(rs.getString("cidade"))
                        .append(", Estado: ").append(rs.getString("estado"))
                        .append(", Cargo Interno ID: ").append(rs.getInt("cargo_interno_id_cargo_interno"))
                        .append("\n");
            }
            JOptionPane.showMessageDialog(null, resultado.toString());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar Filiados: " + e.getMessage());
        }
    }

    private static void atualizarFiliado() {
        String idStr = JOptionPane.showInputDialog("ID do Filiado para atualizar:");
        int id = Integer.parseInt(idStr);

        String novoNome = JOptionPane.showInputDialog("Novo Nome:");
        String novoFone = JOptionPane.showInputDialog("Novo Telefone:");
        String novaDataFiliacao = JOptionPane.showInputDialog("Nova Data de Filiação (YYYY-MM-DD):");
        String novoLogradouro = JOptionPane.showInputDialog("Novo Logradouro:");
        String novoNumero = JOptionPane.showInputDialog("Novo Número:");
        String novaCidade = JOptionPane.showInputDialog("Nova Cidade:");
        String novoEstado = JOptionPane.showInputDialog("Novo Estado:");
        String novoCargoInternoId = JOptionPane.showInputDialog("Novo ID do Cargo Interno:");

        String query = """
                UPDATE filiado
                SET nome = ?, fone = ?, data_filiacao = ?, logradouro = ?, numero = ?, cidade = ?, estado = ?, cargo_interno_id_cargo_interno = ?
                WHERE id_filiado = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, novoNome);
            stmt.setString(2, novoFone);
            stmt.setDate(3, Date.valueOf(novaDataFiliacao));
            stmt.setString(4, novoLogradouro);
            stmt.setString(5, novoNumero);
            stmt.setString(6, novaCidade);
            stmt.setString(7, novoEstado);
            stmt.setInt(8, Integer.parseInt(novoCargoInternoId));
            stmt.setInt(9, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Filiado atualizado com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar Filiado: " + e.getMessage());
        }
    }

    private static void deletarFiliado() {
        String idStr = JOptionPane.showInputDialog("ID do Filiado para deletar:");
        int id = Integer.parseInt(idStr);

        String query = "DELETE FROM filiado WHERE id_filiado = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Filiado deletado com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar Filiado: " + e.getMessage());
        }
    }

    public static void EventoCRUD() {
        while (true) {
            String menu = """
                    Escolha uma operação para Evento:
                    1. Criar Evento
                    2. Consultar Eventos
                    3. Atualizar Evento
                    4. Deletar Evento
                    5. Sair
                    """;

            String opcaoStr = JOptionPane.showInputDialog(menu);
            int opcao = Integer.parseInt(opcaoStr);

            switch (opcao) {
                case 1 -> criarEvento();
                case 2 -> consultarEventos();
                case 3 -> atualizarEvento();
                case 4 -> deletarEvento();
                case 5 -> System.exit(0);
                default -> JOptionPane.showMessageDialog(null, "Opção inválida!");
            }
        }
    }

    private static void criarEvento() {
        String nome = JOptionPane.showInputDialog("Nome do Evento:");
        String data = JOptionPane.showInputDialog("Data do Evento (YYYY-MM-DD):");
        String local = JOptionPane.showInputDialog("Local do Evento:");
        String filiadosParticipantes = JOptionPane.showInputDialog("Filiados Participantes:");
        String custoEstimado = JOptionPane.showInputDialog("Custo Estimado:");

        String query = """
                INSERT INTO evento (nome, data, local, filiados_participantes, custo_estimado)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nome);
            stmt.setDate(2, Date.valueOf(data));
            stmt.setString(3, local);
            stmt.setInt(4, Integer.parseInt(filiadosParticipantes));
            stmt.setBigDecimal(5, new java.math.BigDecimal(custoEstimado));
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Evento criado com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao criar Evento: " + e.getMessage());
        }
    }

    private static void consultarEventos() {
        String query = "SELECT * FROM evento";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            StringBuilder resultado = new StringBuilder("Eventos:\n");

            while (rs.next()) {
                resultado.append("ID: ").append(rs.getInt("id_evento"))
                        .append(", Nome: ").append(rs.getString("nome"))
                        .append(", Data: ").append(rs.getDate("data"))
                        .append(", Local: ").append(rs.getString("local"))
                        .append(", Filiados Participantes: ").append(rs.getInt("filiados_participantes"))
                        .append(", Custo Estimado: ").append(rs.getBigDecimal("custo_estimado"))
                        .append("\n");
            }

            JOptionPane.showMessageDialog(null, resultado.toString());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar Eventos: " + e.getMessage());
        }
    }

    private static void atualizarEvento() {
        String idStr = JOptionPane.showInputDialog("ID do Evento para atualizar:");
        int id = Integer.parseInt(idStr);

        String novoNome = JOptionPane.showInputDialog("Novo Nome do Evento:");
        String novaData = JOptionPane.showInputDialog("Nova Data do Evento (YYYY-MM-DD):");
        String novoLocal = JOptionPane.showInputDialog("Novo Local do Evento:");
        String novosFiliadosParticipantes = JOptionPane.showInputDialog("Novo número de Filiados Participantes:");
        String novoCustoEstimado = JOptionPane.showInputDialog("Novo Custo Estimado:");

        String query = """
                UPDATE evento
                SET nome = ?, data = ?, local = ?, filiados_participantes = ?, custo_estimado = ?
                WHERE id_evento = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, novoNome);
            stmt.setDate(2, Date.valueOf(novaData));
            stmt.setString(3, novoLocal);
            stmt.setInt(4, Integer.parseInt(novosFiliadosParticipantes));
            stmt.setBigDecimal(5, new java.math.BigDecimal(novoCustoEstimado));
            stmt.setInt(6, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Evento atualizado com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar Evento: " + e.getMessage());
        }
    }

    private static void deletarEvento() {
        String idStr = JOptionPane.showInputDialog("ID do Evento para deletar:");
        int id = Integer.parseInt(idStr);

        String query = "DELETE FROM evento WHERE id_evento = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Evento deletado com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar Evento: " + e.getMessage());
        }
    }

    public static void DoacaoCRUD() {
        while (true) {
            String menu = """
                    Escolha uma operação para Doação:
                    1. Criar Doação
                    2. Consultar Doações
                    3. Atualizar Doação
                    4. Deletar Doação
                    5. Sair
                    """;

            String opcaoStr = JOptionPane.showInputDialog(menu);
            int opcao = Integer.parseInt(opcaoStr);

            switch (opcao) {
                case 1 -> criarDoacao();
                case 2 -> consultarDoacoes();
                case 3 -> atualizarDoacao();
                case 4 -> deletarDoacao();
                case 5 -> System.exit(0);
                default -> JOptionPane.showMessageDialog(null, "Opção inválida!");
            }
        }
    }

    private static void criarDoacao() {
        String origemDoacao = JOptionPane.showInputDialog("Origem da Doação:");
        String nomeDoador = JOptionPane.showInputDialog("Nome do Doador:");
        String valorDoacao = JOptionPane.showInputDialog("Valor da Doação:");
        String data = JOptionPane.showInputDialog("Data da Doação (YYYY-MM-DD):");
        String formaPagamento = JOptionPane.showInputDialog("Forma de Pagamento:");

        String idFiliado = JOptionPane.showInputDialog("ID do Filiado:");
        String idCampanha = JOptionPane.showInputDialog("ID da Campanha:");

        String query = """
                INSERT INTO doacao (origem_doacao, nome_doador, valor_doacao, data, forma_pagamento, filiado_id_filiado, campanha_id_campanha)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, origemDoacao);
            stmt.setString(2, nomeDoador);
            stmt.setBigDecimal(3, new java.math.BigDecimal(valorDoacao));
            stmt.setDate(4, Date.valueOf(data));
            stmt.setString(5, formaPagamento);
            stmt.setInt(6, Integer.parseInt(idFiliado));
            stmt.setInt(7, Integer.parseInt(idCampanha));
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Doação criada com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao criar Doação: " + e.getMessage());
        }
    }

    private static void consultarDoacoes() {
        String query = "SELECT * FROM doacao";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            StringBuilder resultado = new StringBuilder("Doações:\n");

            while (rs.next()) {
                resultado.append("ID: ").append(rs.getInt("id_doacao"))
                        .append(", Origem: ").append(rs.getString("origem_doacao"))
                        .append(", Doador: ").append(rs.getString("nome_doador"))
                        .append(", Valor: ").append(rs.getBigDecimal("valor_doacao"))
                        .append(", Data: ").append(rs.getDate("data"))
                        .append(", Forma de Pagamento: ").append(rs.getString("forma_pagamento"))
                        .append(", ID Filiado: ").append(rs.getInt("filiado_id_filiado"))
                        .append(", ID Campanha: ").append(rs.getInt("campanha_id_campanha"))
                        .append("\n");
            }
            JOptionPane.showMessageDialog(null, resultado.toString());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar Doações: " + e.getMessage());
        }
    }

    private static void atualizarDoacao() {
        String idStr = JOptionPane.showInputDialog("ID da Doação para atualizar:");
        int id = Integer.parseInt(idStr);

        String novaOrigemDoacao = JOptionPane.showInputDialog("Nova Origem da Doação:");
        String novoNomeDoador = JOptionPane.showInputDialog("Novo Nome do Doador:");
        String novoValorDoacao = JOptionPane.showInputDialog("Novo Valor da Doação:");
        String novaData = JOptionPane.showInputDialog("Nova Data da Doação (YYYY-MM-DD):");
        String novaFormaPagamento = JOptionPane.showInputDialog("Nova Forma de Pagamento:");
        String novoIdFiliado = JOptionPane.showInputDialog("Novo ID do Filiado:");
        String novoIdCampanha = JOptionPane.showInputDialog("Novo ID da Campanha:");

        String query = """
                UPDATE doacao
                SET origem_doacao = ?, nome_doador = ?, valor_doacao = ?, data = ?, forma_pagamento = ?, filiado_id_filiado = ?, campanha_id_campanha = ?
                WHERE id_doacao = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, novaOrigemDoacao);
            stmt.setString(2, novoNomeDoador);
            stmt.setBigDecimal(3, new java.math.BigDecimal(novoValorDoacao));
            stmt.setDate(4, Date.valueOf(novaData));
            stmt.setString(5, novaFormaPagamento);
            stmt.setInt(6, Integer.parseInt(novoIdFiliado));
            stmt.setInt(7, Integer.parseInt(novoIdCampanha));
            stmt.setInt(8, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Doação atualizada com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar Doação: " + e.getMessage());
        }
    }

    private static void deletarDoacao() {
        String idStr = JOptionPane.showInputDialog("ID da Doação para deletar:");
        int id = Integer.parseInt(idStr);

        String query = "DELETE FROM doacao WHERE id_doacao = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Doação deletada com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar Doação: " + e.getMessage());
        }
    }
}