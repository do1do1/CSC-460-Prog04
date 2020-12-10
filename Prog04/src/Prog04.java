import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/*=============================================================================
|   Assignment:  Program #4:  Prog04.java
|       Author:  Hunter Brick, Justin Do, Sophia Wang, Matthew Yungbluth
|       Grader:  Unsure
|
|       Course:  CSC460
|   Instructor:  L. McCann
|     Due Date:  12/8/2020 12:30 PM
|
|  Description:  Program connects to a database and either add, updates, or deletes
				 a requested records off a requested table based off user response.
				 It also gives back the answer/records to "Get member (by Member ID or phone number)",
				 "Most profitable product", and "Top 10 members with highest spending".
|				 
|
|     Language:  Java 8
| Ex. Packages:  N/A
|                
| Deficiencies: N/A
*===========================================================================*/

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
		
		
		//prompts user for a choice for the above menu
		Scanner input = new Scanner(System.in); //takes in user input
		System.out.print("Enter choice: ");
		String line = input.nextLine();
		line = line.toLowerCase();
		
		
		
		Statement stmt = null;
		ResultSet answer = null;
		
		//while input is a, b, c, d, or e as the menu response
		while(line.equals("a") || line.equals("b") || line.equals("c") || line.equals("d") || line.equals("e")) {
			
			
			
			//OPTION A
			if(line.equals("a")) {
				try {
					stmt = dbconn.createStatement();
				} catch (SQLException e) {
					System.err.println("Unable to create statement.");
					System.exit(-1);
				}
				
				//prompt user to select which table they'll be modifying
				System.out.print("Member, Employee, Product, or Supplier? ");
				line = input.nextLine().toLowerCase();
				
				//while it's not a proper response 
				while(!line.equals("member") && !line.equals("employee") && 
					  !line.equals("product") && !line.equals("supplier") ){
					
					System.out.println("Invalid response, try again");
					System.out.print("Member, Employee, Product, or Supplier? ");
					line = input.nextLine().toLowerCase();	
					
				}
				
				
				
				//loop helps make input case insensitive but database tables names are slightly different
				//this corrects it
				if(line.equals("member")) { //MEMBER
					
					//prompts user for type of query to issue
					System.out.println("Add, update, or delete?");
					line = input.nextLine().toLowerCase();
					
					//proper response loop
					while(!line.equals("add") && !line.equals("update") && !line.equals("delete")) {
						System.out.println("Invalid response, try again");
						System.out.println("Add, update, or delete?");
						line = input.nextLine().toLowerCase();
					}
					
					
					if(line.equals("add")) { //ADD MEMBER
						
						//prompt for every attribute of member
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
						birthDate = input.nextLine();
						System.out.print("Address: ");
						address = input.nextLine();
						System.out.print("Phone Number: ");
						phoneNum = input.nextLine();
						System.out.print("Reward Points: ");
						rewardPts = Integer.valueOf(input.nextLine());
						
						//add member to table
						Member member = new Member(firstName, lastName, birthDate, address, phoneNum, rewardPts);
						member.addMember(stmt);
						
						
					} else if(line.equals("update")) { //UPDATE MEMBER
						
						//prompts a response for each attribute
						//if it's not to be changed, no response is given
						Member member = new Member();
						Integer memID;
						System.out.print("Member ID: ");
						memID = Integer.valueOf(input.nextLine());
						
						//get member by id
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
						
						//updates member
						member.updateMember(stmt,memID);
						System.out.println();
						
					} else if(line.equals("delete")) { //DELETE MEMBER
						
						//get member ID
						Integer memID;
						System.out.print("Member ID: ");
						memID = Integer.valueOf(input.nextLine());
						
						//delete children that are foreign keys to the given primary key of member
						//and record associated with the key
						try {
							stmt.executeQuery("delete from yungbluth.SubRecord sub WHERE sub.subSaleID = (SELECT sub.subSaleID FROM yungbluth.SubRecord sub JOIN yungbluth.SaleRecord sale on sub.saleID = sale.saleID where memid = " + memID.toString() + ")");
							stmt.executeQuery("delete from yungbluth.SaleRecord where memid = " + memID);
							stmt.executeQuery("delete from yungbluth.Member where memid = " + memID);
						} catch (SQLException e) {
							System.err.println("Could not delete Member");
							System.exit(-1);
						}
					}
					
				} else if(line.equals("employee")) { //EMPLOYEE
					
					//prompt query type for employee
					System.out.println("Add, update, or delete?");
					line = input.nextLine().toLowerCase();
					
					//proper response loop
					while(!line.equals("add") && !line.equals("update") && !line.equals("delete")) {
						System.out.println("Invalid response, try again");
						System.out.println("Add, update, or delete?");
						line = input.nextLine().toLowerCase();
					}
					
					
					if(line.equals("add")) { //ADD EMPLOYEE
						
						//prompt for every attribute of employee
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
						
						//add employee
						Employee employee = new Employee(firstName, lastName, gender, address, phoneNum, group, salary);
						employee.addEmployee(stmt);
						
					} else if(line.equals("update")) { //UPDATE EMPLOYEE
						
						//prompt for every attribute of employee, enter nothing to not change it
						Employee emp = new Employee();
						Integer empID;
						System.out.print("Employee ID: ");
						empID = Integer.valueOf(input.nextLine());
						
						//get employee by employee id
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
						
						//update employee
						emp.updateEmployee(stmt,empID);
						System.out.println();
						
					} else if(line.equals("delete")) { //DELETE EMPLOYEE
						
						//prompt for employee ID
						Integer employeeID;
						System.out.print("Employee ID: ");
						employeeID = Integer.valueOf(input.nextLine());
						
						//delete employee record with employee ID
						try {
							stmt.executeQuery("delete from yungbluth.Employee where empid = " + employeeID);
						} catch (SQLException e) {
							System.err.println("Could not delete Employee");
							System.exit(-1);
						}
						
					}
					
				} else if(line.equals("product")) { //PRODUCT
					
					//prompt for query type
					System.out.println("Add, update, or delete?");
					line = input.nextLine().toLowerCase();
					
					//proper response loop
					while(!line.equals("add") && !line.equals("update") && !line.equals("delete")) {
						System.out.println("Invalid response, try again");
						System.out.println("Add, update, or delete?");
						line = input.nextLine().toLowerCase();
					}
					
					
					if(line.equals("add")) { // ADD PRODUCT
						
						//prompt for product attributes
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
						
						//add product
						Product product = new Product(name, retailPrice, category, memDiscount, stockInfo);
						product.addProduct(stmt);
						
					} else if(line.equals("update")) { //UPDATE PRODUCT
						
						//prompt for product attribute, enter nothing to not change it
						Product prod = new Product();
						Integer prodID;
						System.out.print("Product ID: ");
						prodID = Integer.valueOf(input.nextLine());
						
						//get product by product id
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
						
						//update product
						prod.updateProduct(stmt,prodID);
						System.out.println();
						
					} else if(line.equals("delete")) { //DELETE PRODUCT
						
						//prompt for product ID
						Integer productID;
						System.out.print("Product ID: ");
						productID = Integer.valueOf(input.nextLine());
						
						//delete children that are foreign keys to the given primary key of product
						//and record associated with the key
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
					
					//prompt query type
					System.out.println("Add, update, or delete?");
					line = input.nextLine().toLowerCase();
					
					//proper response loop
					while(!line.equals("add") && !line.equals("update") && !line.equals("delete")) {
						System.out.println("Invalid response, try again");
						System.out.println("Add, update, or delete?");
						line = input.nextLine().toLowerCase();
					}
					
					if(line.equals("add")) { //ADD SUPPLIER
						
						//prompt for attributes of supplier
						String name;
						String address;
						String contactPerson;

						System.out.print("Name: ");
						name = input.nextLine();
						System.out.print("Address: ");
						address = input.nextLine();
						System.out.print("Contact Person: ");
						contactPerson = input.nextLine();
						
						//add supplier
						Supplier supplier = new Supplier(name, address, contactPerson);
						supplier.addSupplier(stmt);
						
						
					} else if(line.equals("update")) { //UPDATE SUPPLIER
						
						//prompt for attributes of supplier, enter nothing to not change
						Supplier supplier = new Supplier();
						Integer supplierID;
						System.out.print("Supplier ID: ");
						supplierID = Integer.valueOf(input.nextLine());
						
						//get supplier by supplier id
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
						
						//update supplier
						supplier.updateSupplier(stmt,supplierID);
						System.out.println();
						
					} else if(line.equals("delete")) { //DELETE SUPPLIER
						
						Integer supplierID;
						System.out.print("Supplier ID: ");
						supplierID = Integer.valueOf(input.nextLine());
						//delete children that are foreign keys to the given primary key of supply
						//and record associated with the key
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
				
				//prompt for which table
				System.out.print("SalesRecord or SubRecord? ");
				line = input.nextLine().toLowerCase();
				
				//proper response loop
				while(!line.equals("salesrecord") && !line.equals("subrecord")){
					System.out.println("Invalid response, try again");
					System.out.print("SalesRecord or SubRecord? ");
					line = input.nextLine().toLowerCase();
				}
				
				
				if(line.equals("salesrecord")) { //SALESRECORD
					
					//prompt for query type
					System.out.println("Add, update, or delete?");
					line = input.nextLine().toLowerCase();
					
					//proper response loop
					while(!line.equals("add") && !line.equals("update") && !line.equals("delete")) {
						System.out.println("Invalid response, try again");
						System.out.println("Add, update, or delete?");
						line = input.nextLine().toLowerCase();
					}
					
					if(line.equals("add")) { //ADD SALESRECORD
						
						//prompt for salesrecord attribute
						String saleDate;
						String paymentMethod;
						Integer totalPrice;
						Integer memID;
						
						System.out.print("Sale Date: ");
						saleDate = input.nextLine();
						System.out.print("Payment Method: ");
						paymentMethod = input.nextLine();
						System.out.print("Total Price: ");
						totalPrice = Integer.valueOf(input.nextLine());
						System.out.print("Member ID: ");
						memID = Integer.valueOf(input.nextLine());
						
						//add salesrecord
						SalesRecord salesRecord = new SalesRecord(saleDate, paymentMethod, totalPrice, memID);
						salesRecord.addSalesRecord(stmt);
						
						
						
					} else if(line.equals("update")) { //UPDATE SALESRECORD
						
						//prompt for attribute of salesrecord, enter nothing to not change it
						SalesRecord salesRecord = new SalesRecord();
						Integer salesID;
						System.out.print("Sale ID: ");
						salesID = Integer.valueOf(input.nextLine());
						
						//get salesrecord based off given input
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
						
						//update salesrecord
						salesRecord.updateSalesRecord(stmt, salesID);
						System.out.println();
					} 
					
				} else if(line.equals("subrecord")) { //SUBRECORD
					
					//prompt for query type
					System.out.println("Add, update, or delete?");
					line = input.nextLine().toLowerCase();
					
					//proper response loop
					while(!line.equals("add") && !line.equals("update") && !line.equals("delete")) {
						System.out.println("Invalid response, try again");
						System.out.println("Add, update, or delete?");
						line = input.nextLine().toLowerCase();
					}
					
					if(line.equals("add")) { //ADD SUBRECORD
						
						//prompt for subrecord attributes
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
						
						//add subrecord
						SubRecord subRec = new SubRecord(productID,saleID,price,amount);
						subRec.addSubRecord(stmt);
						
						SalesRecord saleRec = new SalesRecord();
						
						//change salesrecord total price with this addition
						saleRec.getSalesRecord(dbconn, subRec.saleID);
						Product prod = new Product();
						prod.getProduct(dbconn, productID);
						
						saleRec.totalPrice += (subRec.price - prod.memDiscount)*subRec.amount;
						saleRec.updateSalesRecord(stmt, subRec.saleID);
						
					} else if(line.equals("update")) { //UPDATE SUBRECORD
						
						//prompt for subrecord attribute, enter nothing to not change it
						SubRecord subRec = new SubRecord();
						Integer subID;
						System.out.print("Sale ID: ");
						subID = Integer.valueOf(input.nextLine());
						
						//get subrecord by subrecord id
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
						
						//update subrecord
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
					//get member by phone number
					if(userInput.equals("a")) {
						System.out.println("Enter phone number");
						userInput = input.nextLine();
						phoneNumber = userInput;
						
					//get member by member ID
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
	//classes are used to have an object form of a record.
	//all classes can add or update to the db with their assigned attributes
	//methods that take in the Statement object should use it to either receive from the DB
	//or send to the DB.
	
	class Employee{
		
		String firstName;
		String lastName;
		String gender;
		String address;
		String phoneNum;
		String group;
		int salary;
		
		//this is for when the employee already exists
		public Employee() {
			
		}
		
		public Employee(String firstName, String lastName, String gender, 
						String address, String phoneNum, String group, int salary) { 

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
		
		//push current info of object to the table if called
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
		
		//updates to table record with the given changes in attributes
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
		
		//push current info of object to the table if called	
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
		
		//updates to table record with the given changes in attributes
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
		
		//this is for when the product already exists
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
		
		//updates to table record with the given changes in attributes
		public void updateProduct(Statement stmt, int prodID){
			try {
				stmt.executeQuery("update yungbluth.Product SET name = '" + this.name + "', category= '" + this.category + "', retailPrice= " +
						this.retailPrice + ",memDiscount=" + this.memDiscount + ",stockinfo=" + this.stockInfo + " where productID = " + prodID);
			} catch (SQLException e) {
				System.err.println("Could not update supplier");
				System.exit(-1);
			}
		}
		
		//push current info of object to the table if called
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
		
		//this is for when the salesrecord already exists
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
		
		//updates to table record with the given changes in attributes
		public void updateSalesRecord(Statement stmt, int salesID){
			try {
				stmt.executeQuery("update yungbluth.salerecord SET saledate = '" + this.saleDate + "', paymentmethod= '" + this.paymentMethod + "', totalprice= " +
						this.totalPrice + ", memid = " + this.memID + " where saleID = " + salesID);
			} catch (SQLException e) {
				System.err.println("Could not update salerecord");
				System.exit(-1);
			}
		}
		
		//push current info of object to the table if called
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
		
		//this is for when the subrecord already exists
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
		
		//updates to table record with the given changes in attributes
		public void updateSubRecord(Statement stmt, int subSaleID){
			try {
				stmt.executeQuery("update yungbluth.subrecord SET price = " + this.price + ", amount= " + this.amount + " where subsaleID = " + subSaleID);
			} catch (SQLException e) {
				System.err.println("Could not update subrecord");
				System.exit(-1);
			}
		}
		
		//push current info of object to the table if called
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
		
		//this is for when the supplier already exists
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
		
		//updates to table record with the given changes in attributes
		public void updateSupplier(Statement stmt, int supplierID){
			try {
				stmt.executeQuery("update yungbluth.Supplier SET name = '" + this.name + "', address= '" + this.address + "', contactperson= '" +
						this.contactPerson + "' where supplierID = " + supplierID);
			} catch (SQLException e) {
				System.err.println("Could not update supplier");
				System.exit(-1);
			}
		}
		
		//push current info of object to the table if called
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
		
		//this is for when the supplyrecord already exists
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


