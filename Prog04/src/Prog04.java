import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Prog04 {
	public static void main(String[] args) {
		final String oracleURL =   "jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle";
		String query = "";
		String username = null, password = null;
		
		if (args.length == 2) {    // get username/password from cmd line args
            username = args[0];
            password = args[1];
        } else {
            System.out.println("\nUsage:  java JDBC <username> <password>\n"
                             + "    where <username> is your Oracle DBMS"
                             + " username,\n    and <password> is your Oracle"
                             + " password (not your system password).\n");
            System.exit(-1);
        }
		// load the (Oracle) JDBC driver by initializing its base
        // class, 'oracle.jdbc.OracleDriver'.

		try {

			Class.forName("oracle.jdbc.OracleDriver");

		} catch (ClassNotFoundException e) {

            System.err.println("*** ClassNotFoundException:  "
                + "Error loading Oracle JDBC driver.  \n"
                + "\tPerhaps the driver is not on the Classpath?");
            System.exit(-1);

		}
	    // make and return a database connection to the user's
        // Oracle database

		Connection dbconn = null;

		try {
            dbconn = DriverManager.getConnection
                           (oracleURL,username,password);

		} catch (SQLException e) {

            System.err.println("*** SQLException:  "
                + "Could not open JDBC connection.");
            System.err.println("\tMessage:   " + e.getMessage());
            System.err.println("\tSQLState:  " + e.getSQLState());
            System.err.println("\tErrorCode: " + e.getErrorCode());
            System.exit(-1);

		}
		
		//Program "menu" with it's functionalities
		System.out.println("Welcome. Select A, B, C, D, or E. Anything else to exit.");
		System.out.println("A for Modify Member, Employee, Product, or Supplier.");
		System.out.println("B for Modify sales and sub-sale records.");
		System.out.println("C for Get Member.");
		System.out.println("D for Most profitable product.");
		System.out.println("E for Top 10 Members with the highest spending.");
		Scanner input = new Scanner(System.in); //takes in user input
		System.out.print("Enter choice: ");
		String line = input.nextLine();
		line = line.toLowerCase();
		
		
		
		Statement stmt = null;
		ResultSet answer = null;
		
		//while input is a, b, c, d, or e for the query
		while(line.equals("a") || line.equals("b") || line.equals("c") || line.equals("d") || line.equals("e")) {
			if(line.equals("a")) {
				
			} else if(line.equals("b")) {
				
			} else if(line.equals("c")) {
				
			} else if(line.equals("d")) {
				
			} else { //means line is e
				
			}
			
			System.out.print("Enter choice: ");
			line = input.nextLine();
			line = line.toLowerCase();
		}
		
		input.close();
	}
	
	//classes together for now can separate into files later.
	//methods that take in the Statement object should use it to either receive from the DB
	//or send to the DB.
	//feel free to add methods and arguments to methods that may be needed 
	class Employee{
		int empID;
		String firstName;
		String lastName;
		String gender;
		String address;
		String phoneNum;
		String group;
		int salary;
		
		//this is for when the employee already exists and we're trying to get them, otherwise use the other one
		public Employee() {
			
		}
		
		public Employee(int empID, String firstName, String lastName, String gender, 
						String address, String phoneNum, String group, int salary) { // salary may become a decimal?
			this.empID = empID;
			this.firstName = firstName;
			this.lastName = lastName;
			this.gender = gender;
			this.address = address;
			this.phoneNum = phoneNum;
			this.group = group;
			this.salary = salary;
			
		}
		
		//meant to grab an Employee record in the form of an object
		//maybe we write setters to modify and then use updateEmployee to push it back?
		public void getEmployee(Statement stmt, int empID) { 
			//use stmt to get the employee
			//assign by answer.getInt()/answer.getString()
			
		}
		
		//push current info of employee back to the table if called
		public void updateEmployee(Statement stmt) {
			
		}
	}
	class Member{
		
		int memID;
		String firstName;
		String lastName;
		String birthDate;
		String address;
		String phoneNum;
		int rewardPts;
		
		//this is for when the member already exists and we're trying to get them, otherwise use the other one
		public Member() {
			
		}
		
		public Member(int memID, String firstName, String lastName, String birthDate,
					  String address, String phoneNum, int rewardPts) {
			this.memID = memID;
			this.firstName = firstName;
			this.lastName = lastName;
			this.birthDate = birthDate;
			this.address = address;
			this.phoneNum = phoneNum;
			this.rewardPts = rewardPts;
			
		}
		
		public void getMemberByID(int id) {
			
		}
		public void getMemberByPhone(String phone) {
			
		}
		
	}
	class Product{
		int productID;
		String name;
		int retailPrice;
		String category;
		int memDiscount;
		int stockInfo;
		
		public Product() {
			
		}
		
		public Product(int productID, String name, int retailPrice, String category, 
					   int memDiscount, int stockInfo) {
			this.productID = productID;
			this.name = name;
			this.retailPrice = retailPrice;
			this.category = category;
			this.memDiscount = memDiscount;
			this.stockInfo = stockInfo;
		}
		
		public void getProduct(Statement stmt, int productID) {
			
		}
		
	}
	class SalesRecord{
		
		int saleID;
		String saleDate;
		String paymentMethod;
		int totalPrice;
		int memID;
		
		public SalesRecord() {
			
		}
		
		public SalesRecord(int saleID, String saleDate, String paymentMethod,
						   int totalPrice, int memID) {
			this.saleID = saleID;
			this.saleDate = saleDate;
			this.paymentMethod = paymentMethod;
			this.totalPrice = totalPrice;
			this.memID = memID;
			
		}
		
		public void getSalesRecord(Statement stmt, int saleID) {
			
		}
	}
	class SubRecord{
		
		int subSaleID;
		int productID;
		int saleID;
		int price;
		int amount;
		
		
		public SubRecord() {
			
		}
		
		public SubRecord(int subSaleID, int productID, int saleID,
						 int price, int amount) {
			this.subSaleID = subSaleID;
			this.productID = productID;
			this.saleID = saleID;
			this.price = price;
			this.amount = amount;
			
		}
		
		public void getSubRecord(Statement stmt, int subSaleID) {
			
		}
	}
	class Supplier{
		
		int supplierID;
		String name;
		String address;
		String contactPerson;
		
		public Supplier() {
			
		}
		
		public Supplier(int supplierID, String name, String address,
						String contactPerson) {
			this.supplierID = supplierID;
			this.name = name;
			this.address = address;
			this.contactPerson = contactPerson;
			
		}
		
		public void getSupplier(Statement stmt, int supplierID) {
			
		}
	}
	class SupplyRecord{
		
		int supplierID;
		int productID;
		String incomingDate;
		int purchasePrice;
		int amount;
		
		public SupplyRecord() {
			
		}
		
		public SupplyRecord(int supplierID, int productID, String incomingDate,
							int purchasePrice, int amount) {
			this.supplierID = supplierID;
			this.productID = productID;
			this.incomingDate = incomingDate;
			this.purchasePrice = purchasePrice;
			this.amount = amount;
			
		}
		public void getSupplyRecord(Statement stmt, int supplierID, int productID) {
			
		}
		
	}

}

