/*
 * Exercicio 3 - Trabalho Interdisciplinar Backend
 * Lucca de Paula Silva Lopes | 882446
 * 
 */

package Ex03;

import static spark.Spark.*;

public class Aplicacao {
	
	//chamada do metodo de servico do Aluno
	private static AlunoService alunoService = new AlunoService();
	
	public static void main(String[] args) {
		//porta padrao - http://localhost:6789
		port(6789);
		staticFiles.location("/public");
		
		//chama listagem de alunos
		get("/", (request, response) -> alunoService.listAll(request, response));  
		
		//chama formulario de get
		get("/aluno", (request, response) -> alunoService.getForm());

		//chama listagem de alunos
		get("/aluno/list", (request, response) -> alunoService.listAll(request, response));
		
		//chama formulario de insercao
        post("/aluno/insert", (request, response) -> alunoService.insert(request, response));

      //chama list de aluno
        get("/aluno/:matricula", (request, response) -> alunoService.get(request, response));
        
        //chama formulario de update
        get("/aluno/updateForm/:matricula", (request, response) -> alunoService.updateForm(request, response));
                
        //faz a atualizacao
        post("/aluno/update/:matricula", (request, response) -> alunoService.update(request, response));
           
        //faz a delecao
        get("/aluno/delete/:matricula", (request, response) -> alunoService.delete(request, response));
		
	}
	
}