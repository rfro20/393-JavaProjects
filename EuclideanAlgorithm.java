import java.util.Scanner;

public class EuclideanAlgorithm {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.println("Enter two numbers: ");
		int num1 = input.nextInt();
		int num2 = input.nextInt();
		int gcd = euclidean(num1, num2);
		System.out.println("The greatest common factor is: " +gcd);
	}
	
	public static int euclidean(int a, int b) {
		if(a == 0) {
			return b;
		}
		return euclidean(b % a, a);
	}
}
