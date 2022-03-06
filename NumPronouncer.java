import java.util.Scanner;

public class NumPronouncer {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		String decider;
		int n;
		do {
			System.out.print("What number would you like me to pronounce? ");
			
			while(!(input.hasNextInt())) {
				System.out.print("Please Enter an int.");
				input.next();
			}
			n = input.nextInt();
						
			if(n < 1000000) {
				System.out.println(numSay(n) + "\n");
			} else {
				System.out.println(bigNumSay(n) + "\n");
			}
			
			System.out.print("Would you like me to say another number? (y/n)");
			decider = input.next();
		} while(decider.equals("y"));
		input.close();
	}
	
	public static String numSay(int num) {
		String[] teens = {"Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
		String[] tens = {"Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};
		
		
		//Remove Leading Zeroes
		Integer n = num;
		String temp = n.toString();
		StringBuffer sb = new StringBuffer(temp);
		boolean isntZero;
		
		int i = 0; 
        while (i < temp.length() && temp.charAt(i) == '0') 
            i++;
        sb.replace(0, i, ""); 
        if(sb.length() == 0) {
        	return "Zero";
        }
        n = Integer.parseInt(sb.toString());
        
        
		//Build number recursively
		if(num > 0 && num < 10) {
			return singleSay(num);
		} else if(num >= 10 && num < 20) {
			return teens[(num % 10)];
		} else if(num >= 20 && num < 100) {
			return tens[(num / 10) - 2] + " " + numSay(num % 10);
		} else if(num >= 100 && num < 1000) {
			return singleSay(num / 100) + " Hundred " + numSay(num % 100);
		} else if(num >= 1000 && num < 10000) {
			return singleSay(num / 1000) + " Thousand " + numSay(num % 1000);
		} else if(num >= 10000 && num < 20000) {
			return teens[num % 10000 / 1000] + " Thousand " + numSay(num % 1000);
		} else if(num >= 20000 && num < 100000){
			return tens[(num / 10000) - 2] + " " + numSay(num % 10000);
		} else if(num >= 100000 && num < 1000000){
			// Implement
		} else {
			return "";
		}
	}
	
	public static String bigNumSay(int num) {
		//Work on this
		return "";
	}
	
	
	public static String singleSay(int num) {
		String[] nums = {"One", "Two", "Three", "Four", "Five", "Six", "Seven",
				"Eight", "Nine"};
		return nums[num - 1];
	}
}
