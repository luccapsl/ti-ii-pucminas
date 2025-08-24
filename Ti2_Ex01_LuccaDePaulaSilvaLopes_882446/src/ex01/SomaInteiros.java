/*
24-08-2025
Lucca de Paula Silva Lopes
882446

Ex 01 - TI II - Puc Minas
*/
package ex01;
import java.util.*;

class SomaInteiros {
	public static Scanner sc = new Scanner(System.in);
	
	public static void main (String args[]) {
		//declaracao de variaveis
		int tam = 0, num = 0, soma = 0;
		
		//controle para permitir qualquer quantidade de numeros a soma
		System.out.print("Ola! Escreva numeros voce deseja somar: ");
		tam = sc.nextInt();
		
		//soma tam numeros inteiros
		for (int i = 1; i <= tam; i++) {
			System.out.print("\nDigite o " + i + " valor: ");
			num = sc.nextInt();
			soma += num;
		}
		
		//print da soma total
		System.out.println("\nA soma final foi: " + soma);
	}
}
