/*
 * Exercicio 3 - Trabalho Interdisciplinar Backend
 * Lucca de Paula Silva Lopes | 882446
 * 
 */
package Ex03;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import spark.Request;
import spark.Response;

//classe que gere o servico Aluno
public class AlunoService {
	private DAO alunoDAO = new DAO(); 
	private String form;
	private final int FORM_INSERT = 1;
	private final int FORM_DETAIL = 2;
	private final int FORM_UPDATE = 3;
	
	public AlunoService() {
		makeForm();
	}
	
	//construtor makeForm
	public void makeForm() {
        makeForm(FORM_INSERT, new Aluno("", "", 0, null));
    }

	public void makeForm(int tipo, Aluno aluno) {
	    String nome = (aluno != null && aluno.getNome() != null) ? aluno.getNome() : "";
	    String curso = (aluno != null && aluno.getCurso() != null) ? aluno.getCurso() : "";
	    String matricula = (aluno != null && aluno.getMatricula() != 0) ? String.valueOf(aluno.getMatricula()) : "";
	    String dataNascimento = (aluno != null && aluno.getDataNascimento() != null) ?
	            aluno.getDataNascimento().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "";

	    String action = "";
	    String labelButton = "";
	    boolean readonly = false; 

	    //faz o controle do CRUD
	    switch (tipo) {
	        case FORM_INSERT:
	            action = "/aluno/insert";
	            labelButton = "Inserir";
	            readonly = false;
	            break;
	        case FORM_UPDATE:
	            action = "/aluno/update/" + matricula;
	            labelButton = "Atualizar";
	            readonly = false;
	            break;
	        case FORM_DETAIL:
	            action = "";
	            labelButton = "Voltar";
	            readonly = true;
	            break;
	    }

	    form = "<!DOCTYPE html>"
	        + "<html lang=\"pt-BR\">"
	        + "<head>"
	        + "    <meta charset=\"UTF-8\">"
	        + "    <title>CRUD de Alunos</title>"
	        + "    <style>"
	        + "        body { font-family: Arial; margin: 30px; }"
	        + "        form { max-width: 400px; border: 1px solid #ccc; padding: 15px; border-radius: 8px; }"
	        + "        label { display: block; margin-top: 10px; }"
	        + "        input, button { width: 100%; padding: 6px; margin-top: 5px; }"
	        + "    </style>"
	        + "</head>"
	        + "<body>"
	        + "    <h1>CRUD de Alunos</h1>"
	        + "    <form action=\"" + action + "\" method=\"post\">"
	        + "        <input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">"

	        + "        <label>Matrícula:</label>"
	        + "        <input type=\"number\" name=\"matricula\" value=\"" + matricula + "\" "
	        + (tipo == FORM_INSERT ? "" : "readonly") + " required><br>"

	        + "        <label>Nome:</label>"
	        + "        <input type=\"text\" name=\"nome\" value=\"" + nome + "\" "
	        + (readonly ? "readonly" : "") + " required><br>"

	        + "        <label>Curso:</label>"
	        + "        <input type=\"text\" name=\"curso\" value=\"" + curso + "\" "
	        + (readonly ? "readonly" : "") + " required><br>"

	        + "        <label>Data de Nascimento:</label>"
	        + "        <input type=\"date\" name=\"dataNascimento\" value=\"" + dataNascimento + "\" "
	        + (readonly ? "readonly" : "") + " required><br>";

	    if (tipo == FORM_DETAIL) {
	        form += "        <a href=\"/aluno/list\"><button type=\"button\">" + labelButton + "</button></a>";
	    } else if (tipo == FORM_INSERT) {
	    	
	    	form += "        <button type=\"submit\">" + labelButton + "</button>"
		    	     + "        <br><br><a href='/'>Voltar para lista</a>";

	    }
	    else {
		    form += "        <button type=\"submit\">" + labelButton + "</button>"
		    	     + "        <br><br><a href='/'>Voltar para lista</a>";

	    }
	    


	    form += "    </form>"
	        + "</body>"
	        + "</html>";
	    
	    
	}


    public String getForm() {
        return form;
    }
	
    //metodo de insercao entre HTML e DAO
	public Object insert(Request request, Response response) {
		String nome = request.queryParams("nome");
		String curso = request.queryParams("curso");
		int matricula = Integer.parseInt(request.queryParams("matricula"));
		LocalDate dataNascimento = LocalDate.parse(request.queryParams("dataNascimento"));
		
		String resp = "";
		
		Aluno aluno = new Aluno(nome, curso, matricula, dataNascimento);
		
		alunoDAO.conectar();
		
		if(alunoDAO.insert(aluno) == true) {
            resp = "Aluno (" + matricula + ") inserido!";
            response.status(201); // 201 Created
		} else {
			resp = "Aluno (\" + matricula + \") nao pode ser inserido!";
			response.status(404); // 404 Not found
		}
			
		makeForm();
		alunoDAO.close();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}
	
    //metodo de GET entre HTML e DAO
	public Object get(Request request, Response response) {
		int matricula = Integer.parseInt(request.params(":matricula"));		
		alunoDAO.conectar();
		Aluno aluno = alunoDAO.get(matricula);
		
		if (aluno!= null) {
			response.status(200); // success
			makeForm(FORM_DETAIL, aluno);
        } else {
            response.status(404); // 404 Not found
            String resp = "Aluno " + matricula + " nao encontrado.";
    		makeForm();
    		form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }
		alunoDAO.close();
		return form;
	}
	
	//chama o formulario de Update
	public Object updateForm(Request request, Response response) {
	    int matricula = Integer.parseInt(request.params(":matricula"));
	    alunoDAO.conectar();
	    Aluno aluno = alunoDAO.get(matricula);
	    alunoDAO.close();

	    if (aluno != null) {
	        response.status(200);
	        makeForm(FORM_UPDATE, aluno);   
	        return form;
	    } else {
	        response.status(404);
	        return "Aluno não encontrado!";
	    }
	}

    //metodo de GET de todos os alunos entre HTML e DAO
	public Object listAll(Request request, Response response) {
	    alunoDAO.conectar(); // abre a conexão

	    Aluno[] alunos = alunoDAO.getAlunos();

	    String list = "<!DOCTYPE html>"
	        + "<html lang=\"pt-BR\">"
	        + "<head><meta charset=\"UTF-8\">"
	        + "<title>Lista de Alunos</title>"
	        + "<style>"
	        + "body { font-family: Arial; margin: 30px; }"
	        + "table { border-collapse: collapse; width: 100%; }"
	        + "th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }"
	        + "th { background: #f2f2f2; }"
	        + "a { margin-right: 10px; }"
	        + "</style></head>"
	        + "<body>"
	        + "<h1>Lista de Alunos</h1>"
	        + "<table>"
	        + "<tr><th>Matrícula</th><th>Nome</th><th>Curso</th><th>Data Nascimento</th><th>Ações</th></tr>";

	    if (alunos != null) {
	        for (Aluno a : alunos) {
	            list += "<tr>"
	                + "<td>" + a.getMatricula() + "</td>"
	                + "<td>" + a.getNome() + "</td>"
	                + "<td>" + a.getCurso() + "</td>"
	                + "<td>" + a.getDataNascimento() + "</td>"
	                + "<td>"
	                + "<a href='/aluno/updateForm/" + a.getMatricula() + "'>Editar</a> | "
	                + "<a href='/aluno/" + a.getMatricula() + "'>Detalhes</a> | "
	                + "<a href='/aluno/delete/" + a.getMatricula() + "'>Remover</a>"

	                + "</td>"
	                + "</tr>";
	        }
	    } else {
	        list += "<tr><td colspan='5'>Nenhum aluno encontrado.</td></tr>";
	    }

	    list += "</table>"
	        + "<br><a href='/aluno'>Inserir novo aluno</a>"
	        + "</body></html>";

	    alunoDAO.close(); // fecha a conexão
	    response.status(200);
	    return list;
	}
	
    //metodo de UPDATE entre HTML e DAO
	public Object update(Request request, Response response) {
		String nome = request.queryParams("nome");
		String curso = request.queryParams("curso");
		int matricula = Integer.parseInt(request.queryParams("matricula"));
		LocalDate dataNascimento = LocalDate.parse(request.queryParams("dataNascimento"));
		
		String resp = "";
		
		Aluno aluno = new Aluno(nome, curso, matricula, dataNascimento);
		
		alunoDAO.conectar();
		
		if(alunoDAO.update(aluno) == true) {
            resp = "Aluno (" + matricula + ") atualizado!";
            response.status(201); // 201 Created
		} else {
			resp = "Aluno (\" + matricula + \") nao pode ser atualizado!";
			response.status(404); // 404 Not found
		}
			
		makeForm();
		alunoDAO.close();
		String html = listAll(request, response).toString();
        return html.replaceFirst(
            "<h1>Lista de Alunos</h1>",
            "<h1>Lista de Alunos</h1><p style='color:blue'>" + resp + "</p>"
        );
	}


    //metodo de DELETE entre HTML e DAO
	public Object delete(Request request, Response response) {
		int matricula = Integer.parseInt(request.params(":matricula"));		
		alunoDAO.conectar();
		Aluno aluno = alunoDAO.get(matricula);
        String resp = "";       

        if (aluno != null) {
            alunoDAO.delete(matricula);
            response.status(200); // success
            resp = "Aluno(" + matricula + ") excluido!";
        } else {
            response.status(404); // 404 Not found
            resp = "Aluno (" + matricula + ") não encontrado!";
        }
        
        alunoDAO.close();
        
        String html = listAll(request, response).toString();
        return html.replaceFirst(
            "<h1>Lista de Alunos</h1>",
            "<h1>Lista de Alunos</h1><p style='color:blue'>" + resp + "</p>"
        );
	}

	
}