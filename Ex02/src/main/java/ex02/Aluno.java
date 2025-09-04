/*
 * Exercicio 2 - Trabalho Interdisciplinas Backend
 * Lucca de Paula Silva Lopes | 882446
 * 
 */

package ex02;

import java.time.LocalDate;

//classe principal do programa
public class Aluno {
	private String nome;
	private String curso;
	private int matricula;
	private LocalDate dataNascimento;
	
	public Aluno (String nome, String curso, int matricula, LocalDate dataNascimento) {	
		this.nome = nome;
		this.matricula = matricula;
		this.curso = curso;
		this.dataNascimento = dataNascimento;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCurso() {
		return curso;
	}
	public void setCurso(String curso) {
		this.curso = curso;
	}
	public int getMatricula() {
		return matricula;
	}
	public void setMatricula(int matricula) {
		this.matricula = matricula;
	}
	public LocalDate getDataNascimento() {
		return dataNascimento;
	}
	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	
	@Override
	public String toString() {
		return "Aluno [nome=" + this.nome + ", matricula=" + this.matricula + ", data de nascimento=" + this.dataNascimento + ", curso=" + this.curso + "]";
	}	
	
	
}