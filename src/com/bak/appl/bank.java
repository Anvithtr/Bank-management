package com.bak.appl;

import java.util.Scanner;

public class bank {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int option = 0;
		Scanner sc = new Scanner(System.in);
		do {
			System.out.println("Select an option");
			System.out.println("1. Employee Interface");
			System.out.println("2. Customer Interface");
			System.out.println("3. Exit");
			option = sc.nextInt();

			switch (option) {
			case 1:
				EmployeeInterface.EmloyeeLogin();
				break;
			case 2:
				CustomerInterface.showInterface();
				break;
			case 3:
				System.out.println("Exiting the bank ");
				break;
			default:
				System.out.println("Invalid choice please select again ");
			}
		} while (option != 3);
		sc.close();
	}

}
