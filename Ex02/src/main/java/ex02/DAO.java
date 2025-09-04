/*
 * Exercicio 2 - Trabalho Interdisciplinas Backend
 * Lucca de Paula Silva Lopes | 882446
 * 
 */

package ex02;

import java.sql.*;
import java.util.Properties; //biblioteca para uso da config.properties (seguranca das credenciais)
import java.io.InputStream;
import java.io.IOException;


public class DAO {
	private Connection conexao;
	
	public DAO() {
		conexao = null;
	}
	
	//funcao que realiza conexao com o db
	public boolean conectar() {
       
		//vars de conexao com o db
		String driverName = "";
		String serverName = "";
		String mydatabase = "";
		int porta = 5432;
		String url = "";
		String username = "";
		String password = "";
		boolean status = false;
        
		
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("Arquivo config.properties não encontrado!");
            }
            
	        Properties prop = new Properties();
	        prop.load(input);
	        
	        //preenchimento das variaveis com o config.properties
			driverName = "org.postgresql.Driver"; //prop.getProperty("db.drivername");              
			serverName = prop.getProperty("db.servername");
			mydatabase = prop.getProperty("db.hostname");
			url = "jdbc:postgresql://" + serverName + ":" + porta +"/" + mydatabase;
			username = prop.getProperty("db.user");
			password = prop.getProperty("db.password");
			
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar config.properties: " + e.getMessage());
        }
		
		try {
			Class.forName(driverName);
			conexao = DriverManager.getConnection(url, username, password);
			status = (conexao == null);
			//System.out.println("Conexão efetuada com o postgres!");
		} catch (ClassNotFoundException e) { 
			System.err.println("Conexão NÃO efetuada com o postgres -- Driver não encontrado -- " + e.getMessage());
		} catch (SQLException e) {
			System.err.println("Conexão NÃO efetuada com o postgres -- " + e.getMessage());
		}

		return status;
	}
	
	public boolean close() {
		boolean status = false;
		
		try {
			conexao.close();
			status = true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return status;
	}
	
	//metodo de listagem de alunos na tabela
	public Aluno[] getAlunos() {
		Aluno[] alunos = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery("SELECT * FROM aluno");		
	         if(rs.next()){
	             rs.last();
	             alunos = new Aluno[rs.getRow()];
	             rs.beforeFirst();

	             for(int i = 0; rs.next(); i++) {
	                alunos[i] = new Aluno(rs.getString("nome"), rs.getString("curso"), 
	                		                  rs.getInt("matricula"), rs.getDate("data_nascimento").toLocalDate());
	             }
	          }
	          st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return alunos;
	}
	
	//metodo de listagem de alunos na tabela ordenada por matricula
	public Aluno[] getAlunos(int matricula) {
		Aluno[] alunos = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery("SELECT * FROM aluno WHERE matricula = " + matricula);		
	         if(rs.next()){
	             rs.last();
	             alunos = new Aluno[rs.getRow()];
	             rs.beforeFirst();

	             for(int i = 0; rs.next(); i++) {
	                alunos[i] = new Aluno(rs.getString("nome"), rs.getString("curso"), 
	                		                  rs.getInt("matricula"), rs.getDate("data_nascimento").toLocalDate());
	             }
	          }
	          st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return alunos;
	}

	//metodo de insercao de alunos na tabela
	public boolean inserirAluno(Aluno aluno) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			st.executeUpdate("INSERT INTO aluno (nome, matricula, curso, data_nascimento) "
					       + "VALUES ('"+aluno.getNome()+ "', '" + aluno.getMatricula() + "', '"  
					       + aluno.getCurso() + "', '" + aluno.getDataNascimento() + "');");
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
	
	//metodo de atualizacao de alunos na tabela
	public boolean atualizarAluno(Aluno aluno) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			String sql = "UPDATE aluno SET nome = '" + aluno.getNome() + 
		             "', curso = '" + aluno.getCurso() + 
		             "', data_nascimento = DATE '" + aluno.getDataNascimento() + 
		             "' WHERE matricula = " + aluno.getMatricula() + ";";
			st.executeUpdate(sql);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
	
	//metodo de remocao de alunos na tabela
	public boolean excluirAluno(int matricula) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			st.executeUpdate("DELETE FROM aluno WHERE matricula = " + matricula);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
	
}

