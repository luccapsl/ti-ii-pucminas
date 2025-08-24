package ex01;
import java.util.*;

class SomaInteiros {
	public static Scanner sc = new Scanner(System.in);
	
	public static void main (String args[]) {
		int tam = 0, num = 0, soma = 0;
		System.out.print("Ola! Escreva numeros voce deseja somar: ");
		tam = sc.nextInt();
		
		for (int i = 1; i <= tam; i++) {
			System.out.print("\nDigite o " + i + " valor: ");
			num = sc.nextInt();
			soma += num;
		}
		
		System.out.println("\nA soma final foi: " + soma);
	}
}
