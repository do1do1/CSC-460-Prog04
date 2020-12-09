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
						member.getMemberByID(dbconn, memID);
						System.out.print("Update First Name to: (press enter to not) ");
						String userInput;
						userInput = input.nextLine();
						if(!userInput.isEmpty()) {
							member.firstName = userInput;
						}
						System.out.print("Update Last Name to: (press enter to not) ");
						userInput = input.nextLine();
						if(!userInput.isEmpty()) {
							member.lastName = userInput;
						}
						System.out.print("Update Birth Date to: (press enter to not) ");
						userInput = input.nextLine();
						if(!userInput.isEmpty()) {
							member.birthDate = userInput;
						}
						System.out.print("Update Address to: (press enter to not) ");
						userInput = input.nextLine();
						if(!userInput.isEmpty()) {
							member.address = userInput;
						}
						System.out.print("Update Phone Number to: (press enter to not) ");
						userInput = input.nextLine();
						if(!userInput.isEmpty()) {
							member.phoneNum = userInput;
						}
						System.out.print("Update Reward Points to: (press enter to not) ");
						userInput = input.nextLine();
						if(!userInput.isEmpty()) {
							member.rewardPts = Integer.parseInt(userInput);
						}
						//use setters here to change it and then call update function
						member.updateMember(stmt,memID);
						System.out.println();
						
					} else if(line.equals("delete")) { //DELETE MEMBER
						Integer memID;
						System.out.print("Member ID: ");
						memID = Integer.valueOf(input.nextLine());
						try {
							stmt.executeQuery("delete from yungbluth.SubRecord sub WHERE sub.subSaleID = (SELECT sub.subSaleID FROM yungbluth.SubRecord sub JOIN yungbluth.SaleRecord sale on sub.saleID = sale.saleID where memid = " + memID.toString() + ")");
							stmt.executeQuery("delete from yungbluth.SaleRecord where memid = " + memID);
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
						Employee emp = new Employee();
						Integer empID;
						System.out.print("Employee ID: ");
						empID = Integer.valueOf(input.nextLine());
						emp.getEmployeeByID(dbconn, empID);
						System.out.print("Update First Name to: (press enter to not) ");
						String userInput;
						userInput = input.nextLine();
						if(!userInput.isEmpty()) {
							emp.firstName = userInput;
						}
						System.out.print("Update Last Name to: (press enter to not) ");
						userInput = input.nextLine();
						if(!userInput.isEmpty()) {
							emp.lastName = userInput;
						}
						System.out.print("Update Gender to: (press enter to not) ");
						userInput = input.nextLine();
						if(!userInput.isEmpty()) {
							emp.gender = userInput;
						}
						System.out.print("Update Group to: (press enter to not) ");
						userInput = input.nextLine();
						if(!userInput.isEmpty()) {
							emp.group = userInput;
						}
						System.out.print("Update Salary to: (press enter to not) ");
						userInput = input.nextLine();
						if(!userInput.isEmpty()) {
							emp.salary = Integer.parseInt(userInput);
						}
						
						emp.updateEmployee(stmt,empID);
						System.out.println();
					} else if(line.equals("delete")) { //DELETE EMPLOYEE
						
						Integer employeeID;
						System.out.print("Employee ID: ");
						employeeID = Integer.valueOf(input.nextLine());
						try {
							stmt.executeQuery("delete from yungbluth.Employee where empid = " + employeeID);
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
						Product prod = new Product();
						Integer prodID;
						System.out.print("Product ID: ");
						prodID = Integer.valueOf(input.nextLine());
						prod.getProduct(dbconn, prodID);
						System.out.print("Update Name to: (press enter to not) ");
						String userInput;
						userInput = input.nextLine();
						if(!userInput.isEmpty()) {
							prod.name = userInput;
						}
						System.out.print("Update Retail Price to: (press enter to not) ");
						userInput = input.nextLine();
						if(!userInput.isEmpty()) {
							prod.retailPrice = Integer.parseInt(userInput);
						}
						System.out.print("Update Category to: (press enter to not) ");
						userInput = input.nextLine();
						if(!userInput.isEmpty()) {
							prod.category = userInput;
						}
						System.out.print("Update MemDiscount to: (press enter to not) ");
						userInput = input.nextLine();
						if(!userInput.isEmpty()) {
							prod.memDiscount = Integer.parseInt(userInput);
						}
						System.out.print("Update StockInfo to: (press enter to not) ");
						userInput = input.nextLine();
						if(!userInput.isEmpty()) {
							prod.stockInfo = Integer.parseInt(userInput);
						}
						
						prod.updateProduct(stmt,prodID);
						System.out.println();
					} else if(line.equals("delete")) { //DELETE PRODUCT
						
						Integer productID;
						System.out.print("Product ID: ");
						productID = Integer.valueOf(input.nextLine());
						try {
							stmt.executeQuery("delete from yungbluth.SupplyRecord WHERE productID = " + productID);
							stmt.executeQuery("delete from yungbluth.SubRecord WHERE productID = " + productID);
							stmt.executeQuery("delete from yungbluth.Product where productid = " + productID);
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
						Supplier supplier = new Supplier();
						Integer supplierID;
						System.out.print("Supplier ID: ");
						supplierID = Integer.valueOf(input.nextLine());
						supplier.getSupplierByID(dbconn, supplierID);
						System.out.print("Update Name to: (press enter to not) ");
						String userInput;
						userInput = input.nextLine();
						if(!userInput.isEmpty()) {
							supplier.name = userInput;
						}
						System.out.print("Update Address to: (press enter to not) ");
						userInput = input.nextLine();
						if(!userInput.isEmpty()) {
							supplier.address = userInput;
						}
						System.out.print("Update ContactPerson to: (press enter to not) ");
						userInput = input.nextLine();
						if(!userInput.isEmpty()) {
							supplier.contactPerson = userInput;
						}
						
						supplier.updateSupplier(stmt,supplierID);
						System.out.println();
					} else if(line.equals("delete")) { //DELETE SUPPLIER
						
						Integer supplierID;
						System.out.print("Supplier ID: ");
						supplierID = Integer.valueOf(input.nextLine());
						try {
							stmt.executeQuery("delete from yungbluth.SupplyRecord where supplierid = " + supplierID);
							stmt.executeQuery("delete from yungbluth.Supplier where supplierid = " + supplierID);
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
						SalesRecord salesRecord = new SalesRecord();
						Integer salesID;
						System.out.print("Sale ID: ");
						salesID = Integer.valueOf(input.nextLine());
						salesRecord.getSalesRecord(dbconn, salesID);
						System.out.print("Update SalesDate to: (press enter to not) ");
						String userInput;
						userInput = input.nextLine();
						if(!userInput.isEmpty()) {
							salesRecord.saleDate = userInput;
						}
						System.out.print("Update Payment Method to: (press enter to not) ");
						userInput = input.nextLine();
						if(!userInput.isEmpty()) {
							salesRecord.paymentMethod = userInput;
						}
						System.out.print("Update Total Price to: (press enter to not) ");
						userInput = input.nextLine();
						if(!userInput.isEmpty()) {
							salesRecord.totalPrice = Integer.parseInt(userInput);
						}
						System.out.print("Update Member ID to: (press enter to not) ");
						userInput = input.nextLine();
						if(!userInput.isEmpty()) {
							salesRecord.memID = Integer.parseInt(userInput);
						}
						salesRecord.updateSalesRecord(stmt, salesID);
						System.out.println();
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
						Integer productID;
						Integer saleID;
						Integer price;
						Integer amount;
						
						System.out.print("Product ID: ");
						productID = Integer.parseInt(input.nextLine());
						System.out.print("Sale ID: ");
						saleID = Integer.parseInt(input.nextLine());
						System.out.print("Price: ");
						price = Integer.valueOf(input.nextLine());
						System.out.print("Amount: ");
						amount = Integer.valueOf(input.nextLine());
						
						SubRecord subRec = new SubRecord(productID,saleID,price,amount);
						subRec.addSubRecord(stmt);
						
						SalesRecord saleRec = new SalesRecord();
						
						saleRec.getSalesRecord(dbconn, subRec.saleID);
						Product prod = new Product();
						prod.getProduct(dbconn, productID);
						
						saleRec.totalPrice += (subRec.price - prod.memDiscount)*subRec.amount;
						saleRec.updateSalesRecord(stmt, subRec.saleID);
						
					} else if(line.equals("update")) { //UPDATE SUBRECORD
						SubRecord subRec = new SubRecord();
						Integer subID;
						System.out.print("Sale ID: ");
						subID = Integer.valueOf(input.nextLine());
						subRec.getSubRecord(dbconn, subID);
						System.out.print("Update Price to: (press enter to not) ");
						String userInput;
						userInput = input.nextLine();
						if(!userInput.isEmpty()) {
							subRec.price = Integer.parseInt(userInput);
						}
						System.out.print("Update Amount to: (press enter to not) ");
						userInput = input.nextLine();
						if(!userInput.isEmpty()) {
							subRec.amount = Integer.parseInt(userInput);
						}
						
						subRec.updateSubRecord(stmt, subID);
						System.out.println();
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
		
		//The .get() methods on our classes all function the same way. We find the specific tuple 
		//in the database by searching for the primary key, and then we update a blank object (of that class)
		//by replacing its current attributes with the attributes from the tuple we searched for
		public void getEmployeeByID(Connection dbconn, int empID) { 
			try {
				String query="SELECT firstName,lastName,gender,address,phoneNum,empgroup,salary FROM yungbluth.Employee WHERE empID = "+empID;
				Statement stmt=dbconn.createStatement();
				ResultSet answer=stmt.executeQuery(query);
				if(answer!=null) {
					while(answer.next()) {
						this.firstName=answer.getString("firstName");
						this.lastName=answer.getString("lastName");
						this.gender=answer.getString("gender");
						this.address=answer.getString("address");
						this.phoneNum=answer.getString("phoneNum");
						this.group=answer.getString("empgroup");
						this.salary=answer.getInt("salary");
					}
				}
			} catch (SQLException e) {
				System.err.println("Could not get employee");
				System.exit(-1);
			}			
			
		}
		
		//push current info of employee back to the table if called
		public void addEmployee(Statement stmt) {
			try {
				stmt.executeQuery("insert into yungbluth.Employee values(yungbluth.auto_Employee.nextval" +
						", '" + firstName + "', '" + lastName + "', '" + gender +
						"', '" + address + "', '" + phoneNum + "', '" + group + "', " + salary + ")");
			} catch (SQLException e) {
				System.err.println("Could not create Employee");
				System.exit(-1);
			}
		}
		
		public void updateEmployee(Statement stmt, int empID){
			try {
				stmt.executeQuery("update yungbluth.Employee SET firstname = '" + this.firstName + "', lastname= '" + this.lastName + "', gender= '" +
						this.gender + "', address='" + this.address + "', phonenum='" + this.phoneNum + "', empgroup='" + this.group + "', salary=" + this.salary + " where empID = " + empID);
			} catch (SQLException e) {
				System.err.println("Could not update employee");
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
		
		//The .get() methods on our classes all function the same way. We find the specific tuple 
				//in the database by searching for the primary key, and then we update a blank object (of that class)
				//by replacing its current attributes with the attributes from the tuple we searched for
		public void getMemberByID(Connection dbconn, int id) {
			try {
				String query="SELECT firstName, lastName, birthDate, address, phoneNum, rewardpts FROM yungbluth.Member WHERE memID = "+String.valueOf(id);
				Statement stmt=dbconn.createStatement();
				ResultSet answer=stmt.executeQuery(query);
				if(answer!=null) {
					while(answer.next()) {
						this.firstName=answer.getString("firstName");
						this.lastName=answer.getString("lastName");
						this.birthDate=answer.getString("birthDate");
						this.address=answer.getString("address");
						this.phoneNum=answer.getString("phoneNum");
						this.rewardPts=answer.getInt("rewardpts");
					}
				}
			} catch (SQLException e) {
				System.err.println("Could not get member");
				System.exit(-1);
			}
			
			
		}
		/*
		//The .get() methods on our classes all function the same way. We find the specific tuple 
				//in the database by searching for the primary key, and then we update a blank object (of that class)
				//by replacing its current attributes with the attributes from the tuple we searched for
		public void getMemberByPhone(Connection dbconn, String phone) {
			String query="SELECT * FROM yungbluth.Member WHERE phoneNum = "+phone;
			Statement stmt=dbconn.createStatement();
			ResultSet answer=stmt.executeQuery(query);
			this.firstName=answer.getString("firstName");
			this.lastName=answer.getString("lastName");
			this.birthDate=answer.getString("birthDate");
			this.address=answer.getString("address");
			this.phoneNum=answer.getString("phoneNum");
			this.rewardPts=answer.getInt("rewardpts");
			
		}*/
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
		
		public void updateMember(Statement stmt, int memID){
			try {
				stmt.executeQuery("update yungbluth.Member SET firstname = '" + this.firstName + "', lastname= '" + this.lastName + "', birthdate= '" +
						this.birthDate + "', address='" + this.address + "', phonenum='" + this.phoneNum + "', rewardpts=" + this.rewardPts + " where memID = " + memID);
			} catch (SQLException e) {
				System.err.println("Could not update Member");
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
		//The .get() methods on our classes all function the same way. We find the specific tuple 
				//in the database by searching for the primary key, and then we update a blank object (of that class)
				//by replacing its current attributes with the attributes from the tuple we searched for
		public void getProduct(Connection dbconn, int productID) {
			try {
				String query="SELECT name,retailprice,category,memdiscount,stockinfo FROM yungbluth.Product WHERE productID = "+productID;
				Statement stmt=dbconn.createStatement();
				ResultSet answer=stmt.executeQuery(query);
				if(answer!=null) {
					while(answer.next()) {
						this.name=answer.getString("name");
						this.retailPrice=answer.getInt("retailPrice");
						this.category=answer.getString("category");
						this.memDiscount=answer.getInt("memDiscount");
						this.stockInfo=answer.getInt("stockInfo");
					}
				}
			} catch (SQLException e) {
				System.err.println("Could not get product");
				System.exit(-1);
			}
			
		}
		
		public void updateProduct(Statement stmt, int prodID){
			try {
				stmt.executeQuery("update yungbluth.Product SET name = '" + this.name + "', category= '" + this.category + "', retailPrice= " +
						this.retailPrice + ",memDiscount=" + this.memDiscount + ",stockinfo=" + this.stockInfo + " where productID = " + prodID);
			} catch (SQLException e) {
				System.err.println("Could not update supplier");
				System.exit(-1);
			}
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
		//The .get() methods on our classes all function the same way. We find the specific tuple 
				//in the database by searching for the primary key, and then we update a blank object (of that class)
				//by replacing its current attributes with the attributes from the tuple we searched for
		public void getSalesRecord(Connection dbconn, int saleID) {
			try {
				String query="SELECT saledate,paymentmethod,totalprice,memid FROM yungbluth.salerecord WHERE saleID = "+saleID;
				Statement stmt=dbconn.createStatement();
				ResultSet answer=stmt.executeQuery(query);
				if(answer!=null) {
					while(answer.next()) {
						this.saleDate=answer.getString("saleDate");
						this.paymentMethod=answer.getString("paymentMethod");
						this.totalPrice=answer.getInt("totalPrice");
						this.memID=answer.getInt("memID");
					}
				}
			} catch (SQLException e) {
				System.err.println("Could not get supplier");
				System.exit(-1);
			}
			
		}
		
		
		public void updateSalesRecord(Statement stmt, int salesID){
			try {
				stmt.executeQuery("update yungbluth.salerecord SET saledate = '" + this.saleDate + "', paymentmethod= '" + this.paymentMethod + "', totalprice= " +
						this.totalPrice + ", memid = " + this.memID + " where saleID = " + salesID);
			} catch (SQLException e) {
				System.err.println("Could not update salerecord");
				System.exit(-1);
			}
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
		//The .get() methods on our classes all function the same way. We find the specific tuple 
				//in the database by searching for the primary key, and then we update a blank object (of that class)
				//by replacing its current attributes with the attributes from the tuple we searched for
		public void getSubRecord(Connection dbconn, int subSaleID) {
			try {
				String query="SELECT productid,saleid,price,amount FROM yungbluth.subrecord WHERE subsaleID = "+subSaleID;
				Statement stmt=dbconn.createStatement();
				ResultSet answer=stmt.executeQuery(query);
				if(answer!=null) {
					while(answer.next()) {
						this.productID=answer.getInt("productID");
						this.saleID=answer.getInt("saleID");
						this.price=answer.getInt("price");
						this.amount=answer.getInt("amount");
					}
				}
			} catch (SQLException e) {
				System.err.println("Could not get supplier");
				System.exit(-1);
			}
			
		}
		
		public void updateSubRecord(Statement stmt, int subSaleID){
			try {
				stmt.executeQuery("update yungbluth.subrecord SET price = " + this.price + ", amount= " + this.amount + " where subsaleID = " + subSaleID);
			} catch (SQLException e) {
				System.err.println("Could not update subrecord");
				System.exit(-1);
			}
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
		//The .get() methods on our classes all function the same way. We find the specific tuple 
				//in the database by searching for the primary key, and then we update a blank object (of that class)
				//by replacing its current attributes with the attributes from the tuple we searched for
		public void getSupplierByID(Connection dbconn, int supplierID) {
			try {
				String query="SELECT name,address,contactperson FROM yungbluth.Supplier WHERE supplierID = "+supplierID;
				Statement stmt=dbconn.createStatement();
				ResultSet answer=stmt.executeQuery(query);
				if(answer!=null) {
					while(answer.next()) {
						this.name=answer.getString("name");
						this.address=answer.getString("address");
						this.contactPerson=answer.getString("contactPerson");
					}
				}
			} catch (SQLException e) {
				System.err.println("Could not get supplier");
				System.exit(-1);
			}
		}
		
		public void updateSupplier(Statement stmt, int supplierID){
			try {
				stmt.executeQuery("update yungbluth.Supplier SET name = '" + this.name + "', address= '" + this.address + "', contactperson= '" +
						this.contactPerson + "' where supplierID = " + supplierID);
			} catch (SQLException e) {
				System.err.println("Could not update supplier");
				System.exit(-1);
			}
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
		//The .get() methods on our classes all function the same way. We find the specific tuple 
				//in the database by searching for the primary key, and then we update a blank object (of that class)
				//by replacing its current attributes with the attributes from the tuple we searched for
		public void getSupplyRecord(Connection dbconn, int supplierID, int productID) {
			try {
				String query="SELECT * FROM yungbluth.SupplyRecord WHERE supplierID = "+supplierID+" AND productID = "+productID;
				Statement stmt=dbconn.createStatement();
				ResultSet answer=stmt.executeQuery(query);
				this.productID=answer.getInt("productID");
				this.incomingDate=answer.getString("incomingDate");
				this.purchasePrice=answer.getInt("purchasePrice");
				this.amount=answer.getInt("amount");
			} catch (SQLException e) {
				System.err.println("*** SQLException:  "
			            + "Could not fetch query results.");
			    System.exit(-1);
			}
			
		}
		
	}


