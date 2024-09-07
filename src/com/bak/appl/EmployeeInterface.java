package com.bak.appl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class EmployeeInterface {
	public static void EmloyeeLogin() {
		Scanner sc = new Scanner(System.in);
		boolean login = false;
		int attempts = 0;
		System.out.println("Employee Interface");
		System.out.println("Welcome to new Bank");
		while (!login && attempts < 3) {
			System.out.println("Enter emp ID");
			int id = sc.nextInt();
			System.out.println("Enter the pwd");
			String pwd = sc.next();

			if (loginValidation(id, pwd)) {
				System.out.println("Login successfull!");
				login = true;
			} else {
				attempts++;
				System.out.println("Login failed. Please try again");
			}
			if (attempts >= 3) {
				System.out.println("Max login attempts reached");
				return;
			}
		}
		System.out.println("Select an option");
		System.out.println("1. Add a customer data");
		System.out.println("2. Update a data");
		System.out.println("3. Delete a customer Data");
		System.out.println("4. View customer Data");
		System.out.println("5. Go Back to main");

		int choice = sc.nextInt();
		switch (choice) {
		case 1:
			addNewCustomer();
			break;
		case 2:
			updateCustomerData();
			break;
		case 3:
			deleteCustomerData();
			break;
		case 4:
			viewCustomerData();
			break;
		case 5:
			System.out.println("Returning to main menu");
			break;
		default:
			System.out.println("Invalid Choice");
		}
	}

	// insert the customer data
	private static void addNewCustomer() {
		Scanner sc = new Scanner(System.in);
		Connection con = null;
		PreparedStatement ps2 = null;
		String iQry = "insert into customert(cname,pno,email)values(?,?,?)";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankapp?user=root&password=tiger");
			ps2 = con.prepareStatement(iQry);
			System.out.println("Enter the customer name ");
			String name = sc.next();
			ps2.setString(1, name);
			System.out.println("Enter the pno");
			long pno = sc.nextLong();
			ps2.setLong(2, pno);
			System.out.println("Enter email");
			String email = sc.next();
			ps2.setString(3, email);
			System.out.println("data added");
			ps2.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				// if(rs2!=null)rs2.close();
				if (ps2 != null)
					ps2.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// update Customer Data
	private static void updateCustomerData() {
		Scanner sc = new Scanner(System.in);
		Connection con = null;
		PreparedStatement ps3 = null;
		String Uquery = "update customert set cname=?,pno=?,email=? where acno=?";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankapp?user=root&password=tiger");
			ps3 = con.prepareStatement(Uquery);
			System.out.println("Enter the name to be updated");
			String name = sc.next();
			ps3.setString(1, name);
			System.out.println("Enter the Customer phone no to be updated");
			long pno = sc.nextLong();
			ps3.setLong(2, pno);
			System.out.println("Enter the customer email to be Updated");
			String email = sc.next();
			ps3.setString(3, email);
			System.out.println("Enter the customer ac no");
			int acno = sc.nextInt();
			ps3.setInt(4, acno);
			int update = ps3.executeUpdate();
			if (update > 0) {
				System.out.println("Data updated succefully");
				System.out.println();
			} else {
				System.out.println("Failed to update");
				System.out.println();
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// delete customer data
	private static void deleteCustomerData() {
		Scanner sc = new Scanner(System.in);
		Connection con = null;
		PreparedStatement ps4 = null;
		ResultSet rs4 = null;
		String dquery = "delete from customert where acno=?";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankapp?user=root&password=tiger");
			ps4 = con.prepareStatement(dquery);
			System.out.println("Enter the acno");
			int accno = sc.nextInt();
			ps4.setInt(1, accno);

			System.out.println("do you wan to delete the data of " + accno + "?");
			System.out.println("print yes to delete");
			System.out.println("press any other key to main menu");
			String op = sc.next();
			if (op.equalsIgnoreCase("yes")) {
				ps4.executeUpdate();
				System.out.println("Data deleted successfully");
			} else {
				System.out.println("Returning to main menu");
				return;
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// viewCustomerData
	private static void viewCustomerData() {
		Scanner sc = new Scanner(System.in);
		Connection con = null;
		PreparedStatement ps5 = null;
		ResultSet rs5 = null;
		String viewData = "select * from customert where acno=?";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankapp?user=root&password=tiger");
			ps5 = con.prepareStatement(viewData);
			System.out.println("Enter the acno");
			int acno = sc.nextInt();
			ps5.setInt(1, acno);
			rs5 = ps5.executeQuery();
			if (rs5.next()) {
				String cname = rs5.getString(2);
				long pno = rs5.getLong(3);
				String email = rs5.getString(4);
				double balance = rs5.getDouble(5);
				System.out.println("Act holder name: " + cname);
				System.out.println("act holder pno: " + pno);
				System.out.println("act holder email: " + email);
				System.out.println("act holder balance: " + balance);
			} else {
				System.out.println("invalid act no");
				return;
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Employee login validation

	public static boolean loginValidation(int id, String pwd) {
		Scanner sc = new Scanner(System.in);
		Connection con = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		String eval = "Select * from emp where EID=? and pwd=?";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankapp?user=root&password=tiger");
			ps1 = con.prepareStatement(eval);
			ps1.setInt(1, id);
			ps1.setString(2, pwd);
			rs1 = ps1.executeQuery();
			if (rs1.next()) {
				String name = rs1.getString(3);
				System.out.println("welcome back " + name + ", ");
				return true;
			} else {
				System.out.println("Invalid Employee ID or password. Please try again");
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (rs1 != null)
					rs1.close();
				if (ps1 != null)
					ps1.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;

	}
}
