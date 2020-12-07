import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
			
			
			
			//OPTION A
			if(line.equals("a")) {
				try {
					stmt = dbconn.createStatement();
				} catch (SQLException e) {
					System.err.println("Unable to create statement.");
					System.exit(-1);
				}
				System.out.print("Member, Employee, Product, or Supplier? ");
				line = input.nextLine().toLowerCase();
				
				//while it's not a proper response, 
				while(!line.equals("member") && !line.equals("employee") && 
					  !line.equals("product") && !line.equals("supplier") ){
					
					System.out.println("Invalid response, try again");
					System.out.print("Member, Employee, Product, or Supplier? ");
					line = input.nextLine().toLowerCase();	
					
				}
				
				
				
				//loop helps make input case insensitive but database tables names are slightly different
				//this corrects it
				if(line.equals("member")) { //MEMBER
					
					System.out.println("Add, update, or delete?");
					line = input.nextLine().toLowerCase();
					
					//proper response loop
					while(!line.equals("add") && !line.equals("update") && !line.equals("delete")) {
						System.out.println("Invalid response, try again");
						System.out.println("Add, update, or delete?");
						line = input.nextLine().toLowerCase();
					}
					
					
					if(line.equals("add")) { //ADD MEMBER
						String firstName;
						String lastName;
						String birthDate;
						String address;
						String phoneNum;
						int rewardPts;
						System.out.print("First Name: ");
						firstName = input.nextLine();
						System.out.print("Last Name: ");
						lastName = input.nextLine();
						System.out.print("Birth date: ");
						//add regex?
						birthDate = input.nextLine();
						System.out.print("Address: ");
						address = input.nextLine();
						System.out.print("Phone Number: ");
						//add regex?
						phoneNum = input.nextLine();
						System.out.print("Reward Points: ");
						//add check for int here.
						rewardPts = Integer.valueOf(input.nextLine());
						
						Member member = new Member(firstName, lastName, birthDate, address, phoneNum, rewardPts);
						member.addMember(stmt);
						
						
					} else if(line.equals("update")) { //UPDATE MEMBER
						
						Member member = new Member();
						Integer memID;
						System.out.print("Member ID: ");
						memID = Integer.valueOf(input.nextLine());
						member.getMemberByID(memID);
						//use setters here to change it and then call update function
						
						
					} else if(line.equals("delete")) { //DELETE MEMBER
						Integer memID;
						System.out.print("Member ID: ");
						memID = Integer.valueOf(input.nextLine());
						try {
							//scrub this maybe?
							stmt.executeQuery("delete from yungbluth.Member where memid = " + memID);
							//ADD REMOVAL OF FOREIGN KEYS
						} catch (SQLException e) {
							System.err.println("Could not delete Member");
							System.exit(-1);
						}
					}
					
				} else if(line.equals("employee")) { //EMPLOYEE
					
					System.out.println("Add, update, or delete?");
					line = input.nextLine().toLowerCase();
					
					//proper response loop
					while(!line.equals("add") && !line.equals("update") && !line.equals("delete")) {
						System.out.println("Invalid response, try again");
						System.out.println("Add, update, or delete?");
						line = input.nextLine().toLowerCase();
					}
					
					
					if(line.equals("add")) { //ADD EMPLOYEE
						
						String firstName;
						String lastName;
						String gender;
						String address;
						String phoneNum;
						String group;
						int salary;
						System.out.print("First Name: ");
						firstName = input.nextLine();
						System.out.print("Last Name: ");
						lastName = input.nextLine();
						System.out.print("Gender: ");
						gender = input.nextLine();
						System.out.print("Address: ");
						address = input.nextLine();
						System.out.print("Phone Number: ");
						//add regex?
						phoneNum = input.nextLine();
						System.out.print("Group: ");
						group = input.nextLine();
						System.out.print("Salary: ");
						//add check for int here.
						salary = Integer.valueOf(input.nextLine());
						
						Employee employee = new Employee(firstName, lastName, gender, address, phoneNum, group, salary);
						employee.addEmployee(stmt);
						
					} else if(line.equals("update")) { //UPDATE EMPLOYEE
						
					} else if(line.equals("delete")) { //DELETE EMPLOYEE
						
						Integer employeeID;
						System.out.print("Employee ID: ");
						employeeID = Integer.valueOf(input.nextLine());
						try {
							stmt.executeQuery("delete from yungbluth.Employee where empid = " + employeeID);
							//ADD REMOVAL OF FOREIGN KEYS
						} catch (SQLException e) {
							System.err.println("Could not delete Employee");
							System.exit(-1);
						}
						
					}
					
				} else if(line.equals("product")) { //PRODUCT
					
					System.out.println("Add, update, or delete?");
					line = input.nextLine().toLowerCase();
					
					//proper response loop
					while(!line.equals("add") && !line.equals("update") && !line.equals("delete")) {
						System.out.println("Invalid response, try again");
						System.out.println("Add, update, or delete?");
						line = input.nextLine().toLowerCase();
					}
					
					if(line.equals("add")) { // ADD PRODUCT
						
						String name;
						Integer retailPrice;
						String category;
						Integer memDiscount;
						Integer stockInfo;
						

						System.out.print("Name: ");
						name = input.nextLine();
						System.out.print("Retail Price: ");
						//add int checks
						retailPrice = Integer.valueOf(input.nextLine());
						System.out.print("Category: ");
						category = input.nextLine();
						System.out.print("Member discount: ");
						//add int checks
						memDiscount = Integer.valueOf(input.nextLine());
						System.out.print("Stock info: ");
						//add int checks
						stockInfo = Integer.valueOf(input.nextLine());
						
						Product product = new Product(name, retailPrice, category, memDiscount, stockInfo);
						product.addProduct(stmt);
						
					} else if(line.equals("update")) { //UPDATE PRODUCT
						
					} else if(line.equals("delete")) { //DELETE PRODUCT
						
						Integer productID;
						System.out.print("Product ID: ");
						productID = Integer.valueOf(input.nextLine());
						try {
							stmt.executeQuery("delete from yungbluth.Employee where productid = " + productID);
							//ADD REMOVAL OF FOREIGN KEYS
						} catch (SQLException e) {
							System.err.println("Could not delete Product");
							System.exit(-1);
						}
						
					}
					
				} else if(line.equals("supplier")) { //SUPPLIER
					
					System.out.println("Add, update, or delete?");
					line = input.nextLine().toLowerCase();
					
					//proper response loop
					while(!line.equals("add") && !line.equals("update") && !line.equals("delete")) {
						System.out.println("Invalid response, try again");
						System.out.println("Add, update, or delete?");
						line = input.nextLine().toLowerCase();
					}
					
					if(line.equals("add")) { //ADD SUPPLIER
						
						String name;
						String address;
						String contactPerson;

						System.out.print("Name: ");
						name = input.nextLine();
						System.out.print("Address: ");
						address = input.nextLine();
						System.out.print("Contact Person: ");
						contactPerson = input.nextLine();
						
						Supplier supplier = new Supplier(name, address, contactPerson);
						supplier.addSupplier(stmt);
						
						
					} else if(line.equals("update")) { //UPDATE SUPPLIER
						
					} else if(line.equals("delete")) { //DELETE SUPPLIER
						
						Integer productID;
						System.out.print("Supplier ID: ");
						productID = Integer.valueOf(input.nextLine());
						try {
							stmt.executeQuery("delete from yungbluth.Employee where productid = " + productID);
							//ADD THE REMOVAL OF ALL THINGS IN THE SALES/SUBRECORDS ASSOCIATED WITH SUPPLIER ID
						} catch (SQLException e) {
							System.err.println("Could not delete Product");
							System.exit(-1);
						}
					}

				}
				
				
				
				
			// OPTION B
			} else if(line.equals("b")) {
				
				try {
					stmt = dbconn.createStatement();
				} catch (SQLException e) {
					System.err.println("Unable to create statement.");
					System.exit(-1);
				}
				
				System.out.print("SalesRecord or SubRecord? ");
				line = input.nextLine().toLowerCase();
				
				//proper response loop
				while(!line.equals("salesrecord") && !line.equals("subrecord")){
					System.out.println("Invalid response, try again");
					System.out.print("SalesRecord or SubRecord? ");
					line = input.nextLine().toLowerCase();
				}
				
				
				if(line.equals("salesrecord")) { //SALESRECORD
					
					System.out.println("Add, update, or delete?");
					line = input.nextLine().toLowerCase();
					
					//proper response loop
					while(!line.equals("add") && !line.equals("update") && !line.equals("delete")) {
						System.out.println("Invalid response, try again");
						System.out.println("Add, update, or delete?");
						line = input.nextLine().toLowerCase();
					}
					
					if(line.equals("add")) { //ADD SALESRECORD
						
						String saleDate;
						String paymentMethod;
						Integer totalPrice;
						Integer memID;
						
						System.out.print("Sale Date: ");
						saleDate = input.nextLine();
						System.out.print("Payment Method: ");
						paymentMethod = input.nextLine();
						System.out.print("Total Price: ");
						//place Integer checks
						totalPrice = Integer.valueOf(input.nextLine());
						System.out.print("Member ID: ");
						//place Integer checks
						memID = Integer.valueOf(input.nextLine());
						
						SalesRecord salesRecord = new SalesRecord(saleDate, paymentMethod, totalPrice, memID);
						salesRecord.addSalesRecord(stmt);
						
						
						
					} else if(line.equals("update")) { //UPDATE SALESRECORD
						
					} else if(line.equals("delete")) { //DELETE SALESRECORD
						
					}
					
				} else if(line.equals("subrecord")) { //SUBRECORD
					
					System.out.println("Add, update, or delete?");
					line = input.nextLine().toLowerCase();
					
					//proper response loop
					while(!line.equals("add") && !line.equals("update") && !line.equals("delete")) {
						System.out.println("Invalid response, try again");
						System.out.println("Add, update, or delete?");
						line = input.nextLine().toLowerCase();
					}
					
					if(line.equals("add")) { //ADD SUBRECORD
						
					} else if(line.equals("update")) { //UPDATE SUBRECORD
						
					} else if(line.equals("delete")) { //DELETE SUBRECORD
						
					}

				}
				
				
				
			} else if(line.equals("c")) {
				String phoneNumber="";
				int memberID=-1;
				try {
					// find member
					System.out.println("Enter a for getting member by phone number or b for getting member by memID");
					String userInput = input.nextLine().toLowerCase();
					if(userInput.equals("a")) {
						System.out.println("Enter phone number");
						userInput = input.nextLine();
						phoneNumber = userInput;
					} else if(userInput.equals("b")) {
						System.out.println("Enter member ID");
						userInput = input.nextLine();
						try {
							Integer.parseInt(userInput);
							memberID = Integer.parseInt(userInput);
						} catch (NumberFormatException e) {
							System.out.println("Member ID must be numeric");
							continue;
						}
					}
					query = "SELECT firstName, lastName, birthDate, rewardPts FROM yungbluth.Member WHERE phoneNum = '" + phoneNumber +"' OR memID = " + String.valueOf(memberID);
					stmt = dbconn.createStatement();
				    answer = stmt.executeQuery(query);
				    if (answer != null) {
				    	
				        // Get the data about the query result to learn
				        // the attribute names and use them as column headers
				        ResultSetMetaData answermetadata = answer.getMetaData();
				        for (int i = 1; i <= answermetadata.getColumnCount(); i++) {
				            System.out.print(answermetadata.getColumnName(i) + "\t");
				        }
				        System.out.println();
				
				        // Use next() to advance cursor through the result
				        // tuples and print their attribute values
				        String format = "";
				        String lastName = "";
				        while (answer.next()) {
				        	// formatting the output neatly
				        	format = String.format("%-16s", answer.getString("firstName"));
				        	lastName = String.format("%-16s", answer.getString("lastName"));
				        	System.out.println(format + lastName + answer.getString("birthDate") + "\t" + answer.getInt("rewardPts"));	
				        }
				    }
				    System.out.println();
				} catch (SQLException e) {
					// catching the exception
			        System.err.println("*** SQLException:  "
			            + "Could not fetch query results.");
			        System.exit(-1);
				}
			} else if(line.equals("d")) {
				try {
					// finding most profitable product
					query = "SELECT * FROM(SELECT p.name, sur.amount*(p.retailPrice-COALESCE(p.memDiscount,1)-sur.purchasePrice) totalProfit FROM yungbluth.Product p JOIN yungbluth.SupplyRecord sur ON p.productID = sur.productID ORDER BY totalProfit DESC) WHERE ROWNUM <=1";
					stmt = dbconn.createStatement();
				    answer = stmt.executeQuery(query);
				    if (answer != null) {
				    	
				        // Get the data about the query result to learn
				        // the attribute names and use them as column headers
				        ResultSetMetaData answermetadata = answer.getMetaData();
				        for (int i = 1; i <= answermetadata.getColumnCount(); i++) {
				            System.out.print(answermetadata.getColumnName(i) + "\t\t");
				        }
				        System.out.println();
				
				        // Use next() to advance cursor through the result
				        // tuples and print their attribute values
				        String format = "";
				        while (answer.next()) {
				        	// formatting the output neatly
				        	format = String.format("%-16s", answer.getString("name"));
				        	System.out.println(format + answer.getDouble("totalProfit"));	
				        }
				    }
				    System.out.println();
				} catch (SQLException e) {
					// catching the exception
			        System.err.println("*** SQLException:  "
			            + "Could not fetch query results.");
			        System.exit(-1);
				}
			} else { //means line is e
				try {
					// find top 10 members with highest spending
					query = "SELECT * FROM (SELECT firstName, lastName, totalPrice FROM yungbluth.Member m JOIN yungbluth.SaleRecord sar ON m.memID = sar.memID ORDER BY totalPrice DESC) WHERE ROWNUM <= 10";
					stmt = dbconn.createStatement();
				    answer = stmt.executeQuery(query);
				    if (answer != null) {
				    	
				        // Get the data about the query result to learn
				        // the attribute names and use them as column headers
				        ResultSetMetaData answermetadata = answer.getMetaData();
				        for (int i = 1; i <= answermetadata.getColumnCount(); i++) {
				            System.out.print(answermetadata.getColumnName(i) + "\t");
				        }
				        System.out.println();
				
				        // Use next() to advance cursor through the result
				        // tuples and print their attribute values
				        String format = "";
				        String lastName = "";
				        while (answer.next()) {
				        	// formatting the output neatly
				        	format = String.format("%-16s", answer.getString("firstName"));
				        	lastName = String.format("%-16s", answer.getString("lastName"));
				        	System.out.println(format + lastName + answer.getDouble("totalPrice"));	
				        }
				    }
				    System.out.println();
				} catch (SQLException e) {
					// catching the exception
			        System.err.println("*** SQLException:  "
			            + "Could not fetch query results.");
			        System.exit(-1);
				}
			}
			
			System.out.print("Enter choice: ");
			line = input.nextLine();
			line = line.toLowerCase();
		}
		
		input.close();
		try {
			stmt.close();
		} catch (SQLException e) {
			
			System.err.println("Could not close statement");
			System.exit(-1);
		}
	}
	

}
	//classes together for now can separate into files later.
	//methods that take in the Statement object should use it to either receive from the DB
	//or send to the DB.
	//feel free to add methods and arguments to methods that may be needed 
	class Employee{
		
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
		
		public Employee(String firstName, String lastName, String gender, 
						String address, String phoneNum, String group, int salary) { // salary may become a decimal?

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
		
		public void addEmployee(Statement stmt) {
			try {
				System.out.println("insert into yungbluth.Employee values(yungbluth.auto_Employee.nextval" +
						", '" + firstName + "', '" + lastName + "', '" + gender +
						"', '" + address + "', '" + phoneNum + "', '" + group + "', " + salary + ")");
				stmt.executeQuery("insert into yungbluth.Employee values(yungbluth.auto_Employee.nextval" +
						", '" + firstName + "', '" + lastName + "', '" + gender +
						"', '" + address + "', '" + phoneNum + "', '" + group + "', " + salary + ")");
			} catch (SQLException e) {
				System.err.println("Could not create Employee");
				System.exit(-1);
			}
		}
	}
	class Member{
		
		String firstName;
		String lastName;
		String birthDate;
		String address;
		String phoneNum;
		int rewardPts;
		
		//this is for when the member already exists and we're trying to get them, otherwise use the other one
		public Member() {
			
		}
		
		public Member(String firstName, String lastName, String birthDate,
					  String address, String phoneNum, int rewardPts) {

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
		public void addMember(Statement stmt){
			try {
				stmt.executeQuery("insert into yungbluth.Member values(yungbluth.auto_Member.nextval" +
						", '" + firstName + "', '" + lastName + "', '" + birthDate +
						"', '" + address + "', '" + phoneNum + "', " + rewardPts + ")");
			} catch (SQLException e) {
				System.err.println("Could not create Member");
				System.exit(-1);
			}
		}
		
	}
	class Product{
		String name;
		int retailPrice;
		String category;
		int memDiscount;
		int stockInfo;
		
		public Product() {
			
		}
		
		public Product(String name, int retailPrice, String category, 
					   int memDiscount, int stockInfo) {

			this.name = name;
			this.retailPrice = retailPrice;
			this.category = category;
			this.memDiscount = memDiscount;
			this.stockInfo = stockInfo;
		}
		
		public void getProduct(Statement stmt, int productID) {
			
		}
		
		public void addProduct(Statement stmt) {
			try {
				stmt.executeQuery("insert into yungbluth.Product values(yungbluth.auto_Product.nextval" +
						", '" + name + "', " + retailPrice + ", '" + category +
						"', " + memDiscount + ", " + stockInfo + ")");
			} catch (SQLException e) {
				System.err.println("Could not create Product");
				System.exit(-1);
			}
		}
		
	}
	class SalesRecord{
		
		String saleDate;
		String paymentMethod;
		int totalPrice;
		int memID;
		
		public SalesRecord() {
			
		}
		
		public SalesRecord(String saleDate, String paymentMethod,
						   int totalPrice, int memID) {

			this.saleDate = saleDate;
			this.paymentMethod = paymentMethod;
			this.totalPrice = totalPrice;
			this.memID = memID;
			
		}
		
		public void getSalesRecord(Statement stmt, int saleID) {
			
		}
		
		public void addSalesRecord(Statement stmt) {
			try {
				stmt.executeQuery("insert into yungbluth.SalesRecord values(yungbluth.auto_SalesRecord.nextval" +
						", '" + saleDate + "', '" + paymentMethod + "', " + totalPrice +
						", " + memID + ")");
			} catch (SQLException e) {
				System.err.println("Could not create SalesRecord");
				System.exit(-1);
			}
			
		}
	}
	class SubRecord{
		
		int productID;
		int saleID;
		int price;
		int amount;
		
		
		public SubRecord() {
			
		}
		
		public SubRecord( int productID, int saleID,
						 int price, int amount) {
			this.productID = productID;
			this.saleID = saleID;
			this.price = price;
			this.amount = amount;
			
		}
		
		public void getSubRecord(Statement stmt, int subSaleID) {
			
		}
		
		public void addSubRecord(Statement stmt) {
			try {
				stmt.executeQuery("insert into yungbluth.SubRecord values(yungbluth.auto_SubRecord.nextval" +
						", " + productID + ", " + saleID + ", " + price +
						", " + amount + ")");
			} catch (SQLException e) {
				System.err.println("Could not create SubRecord");
				System.exit(-1);
			}
		}
	}
	class Supplier{
		
		String name;
		String address;
		String contactPerson;
		
		public Supplier() {
			
		}
		
		public Supplier(String name, String address, String contactPerson) {
			this.name = name;
			this.address = address;
			this.contactPerson = contactPerson;
			
		}
		
		public void getSupplier(Statement stmt, int supplierID) {
			
		}
		
		public void addSupplier(Statement stmt) {
			try {
				stmt.executeQuery("insert into yungbluth.Supplier values(yungbluth.auto_Supplier.nextval" +
						", '" + name + "', '" + address + "', '" + contactPerson + "')");
			} catch (SQLException e) {
				System.err.println("Could not create Supplier");
				System.exit(-1);
			}
		}
	}
	class SupplyRecord{
		
		int productID;
		String incomingDate;
		int purchasePrice;
		int amount;
		
		public SupplyRecord() {
			
		}
		
		public SupplyRecord(int productID, String incomingDate,
							int purchasePrice, int amount) {

			this.productID = productID;
			this.incomingDate = incomingDate;
			this.purchasePrice = purchasePrice;
			this.amount = amount;
			
		}
		public void getSupplyRecord(Statement stmt, int supplierID, int productID) {
			
		}
		
	}


