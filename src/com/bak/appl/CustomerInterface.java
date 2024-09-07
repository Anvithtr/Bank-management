package com.bak.appl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class CustomerInterface {
	private static ResultSet rs1;

	public static void showInterface() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Customer Interface");
		System.out.println("Welcome to SBI Bank");
		System.out.println("Enter the Acc No");
		int acc = sc.nextInt();
		System.out.println("Enter the PIN(If you are a new user press 0): ");
		int sPin = sc.nextInt();
		String pin = sPin != 0 ? String.valueOf(sPin) : "";
		if (pin.isEmpty()) {
			System.out.println("Do you like to generate PIN?");
			System.out.println("Press yes to generate pin");
			System.out.println("press any key to main");
			String ch = sc.next();
			if (ch.equalsIgnoreCase("yes")) {
				// generatePIN();
			} else {
				System.out.println("PIN Generation failed");
			}
		} else {
			Connection con = null;
			PreparedStatement ps1 = null;
			ResultSet rs1 = null;
			String cVal = "SELECT * from customert where acno=? and PIN=?";

			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankapp?user=root&password=tiger");
				ps1 = con.prepareStatement(cVal);
				ps1.setInt(1, acc);
				ps1.setInt(2, sPin);
				rs1 = ps1.executeQuery();
				if (rs1.next()) {
					String name = rs1.getString(2);
					System.out.println("Welcome back" + name + ",");
				} else {
					System.out.println("Invalid acnt no or PIN");
					return;
				}
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (rs1 != null) {
					try {
						rs1.close();

					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (ps1 != null) {
					try {
						ps1.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (con != null) {
					try {
						con.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}
		System.out.println("Select an option");
		System.out.println("1. Check Account Balance");
		System.out.println("2. Deposit the amount");
		System.out.println("3. Make a Transaction");
		System.out.println("4  Update PIN");
		System.out.println("5. Go Back to Main Method");

		int choice = sc.nextInt();
		switch (choice) {
		case 1:
			checkAccountBalance();
			break;
		case 2:
			deposit();
			break;
		case 3:
			transferAmount();
			break;
		case 4:
			generatePIN();
			break;
		case 5:
			System.out.println("Returning to main menu.");
			break;

		default:
			System.out.println("Invalid choice");
		}

	}

	private static void deposit() {
		Scanner sc = new Scanner(System.in);
		Connection con = null;
		PreparedStatement ps1 = null;
		String uQry = "UPDATE coustemert set balance=balance+? WHERE acno=? ";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankapp?user=root&password=tiger");
			System.out.println(uQry);
			System.out.println("Enter your account number: ");
			int acc1 = sc.nextInt();
			ps1.setInt(2, acc1);
			System.out.println("Enter the amount to be deposited: ");
			double amt = sc.nextDouble();
			ps1.setDouble(1, amt);
			ps1.executeUpdate();
			System.out.println("The amount of Rs" + amt + " added succesfully...!");

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void transferAmount() {
		Scanner sc = new Scanner(System.in);
		Connection con = null;
		PreparedStatement ps1 = null, ps2 = null, ps3 = null;
		String sQry = "SELECT coustmert WHERE acno=?";
		String uQry1 = "UPDATE coustmert SET balance=balance-? WHERE acno=?";
		String uQry2 = "UPDATE coustmert SET balance=balance-? WHERE acno=?";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankapp?user=root&password=tiger");
			ps1 = con.prepareStatement(sQry);
			System.out.println("Enter your account number: ");
			int acc1 = sc.nextInt();
			ps1.setInt(1, acc1);
			rs1 = ps1.executeQuery();
			if (rs1.next()) {
				String name = rs1.getString(2);
				double ball = rs1.getDouble(4);
				int pin = rs1.getInt(5);
				System.out.println("Dear " + name + ",");
				System.out.println("Do you like to transfer the amout to beneficiary account?");
				System.out.println("Press yes to continue and any key to main menu");
				String choice = sc.next();
				if (choice.equalsIgnoreCase("YES")) {
					System.out.println("Enter the beneficiary amount");
					int acc2 = sc.nextInt();
					if (acc2 != acc1) {
						System.out.println("Enter the amount to be transferred");
						double amt = sc.nextDouble();
						if (amt <= ball && amt > 0) {
							System.out.println("Enter the PIN");
							int pin2 = sc.nextInt();
							if (pin2 == pin) {
								ps2 = con.prepareStatement(uQry2);
								ps2.setDouble(1, amt);
								ps2.setInt(2, acc1);
								ps2.executeUpdate();
								ps3 = con.prepareStatement(uQry2);
								ps3.setDouble(1, amt);
								ps3.setInt(2, acc2);
								ps3.executeUpdate();
								double bal2 = ball - amt;
								System.out.println("Transaction succesful! your balance is Rs." + bal2);

							} else {
								System.out.println("Invalid PIN, transaction cancelled");
							}

						} else {
							System.out.println("Insufficient balance!");
						}
					} else {
						System.out.println("Invalid transaction, check the account number once again");
					}
				} else {
					System.out.println("Transaction cancelled");
				}
			}
			System.out.println("Incorrect account number");
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (ps3 != null) {
				try {
					ps3.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps2 != null) {
				try {
					ps2.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (rs1 != null) {
				try {
					rs1.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps1 != null) {
				try {
					ps1.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	// generatePIN
	private static void generatePIN() {
		Connection con = null;
		PreparedStatement ps = null;
		Scanner sc = new Scanner(System.in);
		String uQry = "UPDATE customert SET PIN=? WHERE acno=?";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankapp?user=root&password=tiger");
			ps = con.prepareStatement(uQry);
			System.out.println("Enter the account numer again: ");
			int acc = sc.nextInt();
			ps.setInt(2, acc);
			System.out.println("Set the PIN: ");
			int pin1 = sc.nextInt();
			if (pin1 >= 1000 && pin1 <= 9999) {
				System.out.println("Confirm the PIN: ");
				int pin2 = sc.nextInt();
				if (pin2 == pin1) {
					ps.setInt(1, pin2);
					ps.executeUpdate();
				} else {
					System.out.println("Pin mismatched. PIN generatio failed");
					return;
				}
			} else {
				System.out.println("Invalid PIN");
				return;
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	// checkAccountBalance
	private static boolean checkAccountBalance() {
		Scanner sc = new Scanner(System.in);
		String sQry = "SELECT * FROM customert WHERE acno=? AND PIN=?";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankapp?user=root&password=tiger");
			ps = con.prepareStatement(sQry);
			System.out.println("Enter the ACC no: ");
			int accno = sc.nextInt();
			ps.setInt(1, accno);
			int pin = sc.nextInt();
			ps.setInt(2, pin);
			rs = ps.executeQuery();
			if (rs.next()) {
				String name = rs.getString(2);
				double bal = rs.getDouble(4);
				System.out.println("Dear " + name + ",");
				System.out.println("your act balance is RS." + String.valueOf(bal));
			} else {
				System.out.println("Invalid PIN");
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return false;

	}

}
