package application;


import java.util.Scanner;
import java.math.BigDecimal;
import java.math.BigInteger;



//DISCLAIMER: THIS PROGRAM IS NOT THE MOST OPTIMAL, NOR IS IT THE MOST EFFICIENT WAY OF CODING (YOU CAN PROBABLY MAKE FUNCTIONS AND DO BETTER CHECKING OF CONDITIONS). HOWEVER, IT WORKS.

public class Calculator{

	public static void main(String[] args) {
		
		//create scanner
		Scanner sc = new Scanner(System.in);
		
		//declare and/or initialize variables
		String number, integrand, mantissa;
		int decimalPointLocation, movementLocation = 0, wholeNumberLength, exponent = 0, biasedExponent;
		BigInteger whole;
		BigDecimal decimal;
		String iEEEWhole = "", iEEEDecimal = "", iEEEMantissa = "", iEEEExponent = "", iEEESign = "", iEEEFinal = "";
		boolean isFinished = false;
		
		//prompt
		System.out.print("Enter a floating point number (with a decimal point) for Step-by-Step IEEE 754 Storage: ");
		number = sc.nextLine();
		System.out.println();
		
		//breaks number into integrand and mantissa portions and saves them in two separate java objects BigInteger (for whole part) and BigDecimal (for decimal part)
		decimalPointLocation = number.indexOf('.');
		if(decimalPointLocation == 0) {
			integrand = "0";
		} else {
			integrand = number.substring(0, decimalPointLocation);
		}
		mantissa = number.substring(decimalPointLocation);
		whole = new BigInteger(integrand);
		decimal = new BigDecimal(mantissa);
		
		if(whole.compareTo(BigInteger.ZERO) == -1) {
			iEEESign = "1";
		} else {
			iEEESign = "0";
		}
		
		System.out.println("Sign Bit: " + iEEESign + "\n\n\n");
		
		//loops through dividing the integrand part by 2 to convert it to binary (shows each iterative step) and adds the 1's and 0's to a string
		
		System.out.println("Integrand Binary Conversion:\n");
		
		while(whole.compareTo(BigInteger.ZERO) != 0) {
			if(whole.remainder(new BigInteger("2")).compareTo(BigInteger.ZERO) == 0 && whole.compareTo(BigInteger.ONE) != 0) {
				System.out.format("%s / 2 --> 0\n", whole.toString());
				whole = whole.divide(new BigInteger("2"));
				iEEEWhole += "0";
			} else if(whole.remainder(new BigInteger("2")).compareTo(BigInteger.ZERO) != 0 && whole.compareTo(BigInteger.ONE) != 0) {
				System.out.format("%s / 2 --> 1\n", whole.toString());
				whole = whole.divide(new BigInteger("2"));
				iEEEWhole += "1";
			} else {
				whole = whole.divide(new BigInteger("2"));
				System.out.println("1 / 2 --> 1\n");
				iEEEWhole += "1";
			}
		}
		
		//reverses the string with the integrand part because it is backwards
		if(iEEEWhole != "") {
			StringBuilder reverse = new StringBuilder();
			reverse.append(iEEEWhole);
			reverse.reverse();
			iEEEWhole = reverse.toString();
			System.out.println(iEEEWhole + "\n");
			
			//determines where the first 1 is in the integrand so that the decimal point can be moved
			movementLocation = iEEEWhole.indexOf("1");
			iEEEMantissa = iEEEWhole.substring(movementLocation + 1);
			wholeNumberLength = iEEEWhole.length();
			movementLocation = wholeNumberLength - (movementLocation + 1);
			exponent = movementLocation;
		}

		//iEEEWhole is "" if the while loop was not entered, which means the integrand is 0
		//if the integrand is 0, we can't determine where the first 1 is yet - so we must keep iterating until we get a 1 in the mantissa portion
		if(iEEEWhole == "") {
			
			System.out.println("(No Integrand Part)\n\n\n");
			
			System.out.println("Keep Multiplying until we get a 1:\n");
			while(true) {
				decimal = decimal.multiply(new BigDecimal(2));
				
				if(decimal.compareTo(BigDecimal.ONE) == -1) {
					System.out.format("%s --> 0\n", decimal.toString());
					exponent--;
				} else if(decimal.compareTo(BigDecimal.ONE) == 1) {
					System.out.format("%s --> 1\n\n\n", decimal.toString());
					decimal = decimal.subtract(BigDecimal.ONE);
					exponent--;
					break;
				} else {
					System.out.println("Done --> 1\n(No Mantissa Part)\n\n\n");
					exponent--;
					isFinished = true;
					break;
				}
			}
		}
		
		//the remaining iterations are left here for the mantissa portion that can fit in IEEE standards
		
		if(!isFinished) {
			System.out.println("Mantissa Binary Conversion:\n");
			
			for(int i = 1; i <= 23-movementLocation; i++) {

				decimal = decimal.multiply(new BigDecimal(2));
				
				if(decimal.compareTo(BigDecimal.ONE) == -1) {
					System.out.format("%s --> 0\n", decimal.toString());
					iEEEDecimal += "0";
				} else if(decimal.compareTo(BigDecimal.ONE) == 1) {
					System.out.format("%s --> 1\n", decimal.toString());
					decimal = decimal.subtract(BigDecimal.ONE);
					iEEEDecimal += "1";
				} else {
					System.out.println("Done --> 1\n");
					iEEEDecimal += "1";
					isFinished = true;
					break;
				}
			}
		}
		
		decimal = decimal.multiply(new BigDecimal(2));
		System.out.format("Rounding Calculation: %s\n", decimal.toString());
		
		//checks rounding for accuracy - exactly the same as online converter
		if(decimal.compareTo(BigDecimal.ONE) == 1 && iEEEDecimal != "") {		
			iEEEDecimal = iEEEDecimal.substring(0, iEEEDecimal.length()-1);
			iEEEDecimal += "1";
		}
		
		//formatting to make the mantissa correct
		iEEEMantissa += iEEEDecimal;
		
		while(iEEEMantissa.length() < 23) {
			iEEEMantissa += "0";
		}
		
		//print mantissa
		System.out.println("Overall Mantissa: " + iEEEMantissa + "\n\n\n");
		
		System.out.println("Unbiased Exponent: " + exponent);
		//bias the exponent
		biasedExponent = exponent + 127;
		System.out.println("Biased Exponent: " + biasedExponent + "\n");
		
		System.out.println("Biased Exponent Binary Conversion:\n");
		//convert the exponent to binary
		while(biasedExponent != 0) {
			if(biasedExponent % 2 == 0 && biasedExponent != 1) {
				System.out.format("%d / 2 --> 0\n", biasedExponent);
				biasedExponent /= 2;
				iEEEExponent += "0";
			} else if(biasedExponent % 2 != 0 && biasedExponent != 1) {
				System.out.format("%d / 2 --> 1\n", biasedExponent);
				biasedExponent /= 2;
				iEEEExponent += "1";
			} else {
				biasedExponent /= 2;
				System.out.println("1 / 2 --> 1\n");
				iEEEExponent += "1";
			}
		}
		
		//reverses the string with the exponent because it is backwards
		while(iEEEExponent.length() < 8) {
			iEEEExponent += "0";
		}
		
		StringBuilder reverse2 = new StringBuilder();
		reverse2.append(iEEEExponent);
		reverse2.reverse();
		iEEEExponent = reverse2.toString();
		
		
		//print binary biased exponent
		System.out.println("Overall Exponent: " + iEEEExponent + "\n\n\n");
		
		
		//format final answer
		iEEEMantissa = iEEEMantissa.substring(0,5) + " " + iEEEMantissa.substring(5,10) + " " + iEEEMantissa.substring(10,15) + " " + iEEEMantissa.substring(15,20) + " " + iEEEMantissa.substring(20,23);
		
		iEEEFinal = iEEESign + "   " + iEEEExponent + "   " + iEEEMantissa;
		
		System.out.println("\n\n\n AND THE FINAL ANSWER IS:\n\n " + iEEEFinal + "\n\n\n\n\n");
			
	}	
	
}

