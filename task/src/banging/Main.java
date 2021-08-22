package banging;

import java.io.File;
import java.net.Socket;
import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Scanner scanner=new Scanner(System.in);
          createNewDatabase("card.s3db");
        boolean menu=true;
        while (menu){

            long number_card = generate_number();
            int pin_card = generate_pin();

            System.out.println("1. Create an account\n" +
                                "2. Log into account\n" +
                                "0. Exit\n");
            String action=scanner.next();


            switch (action){
                case "1":
                    String url = "jdbc:sqlite:card.s3db";

                    String sql0 =  "CREATE TABLE IF NOT EXISTS card(\n" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                            "number TEXT,\n" +
                            "pin TEXT,\n" +
                            "balance INTEGER \n" +
                            ");";

                    Connection conn=DriverManager.getConnection(url);
                    Statement stmt=conn.createStatement();
                    stmt.execute(sql0);
                    stmt.close();
                    Connection().close();

                            String sql ="INSERT INTO card (number,pin) VALUES (?,?)";

                    PreparedStatement prSt=Connection().prepareStatement(sql);
                    prSt.setLong(1,number_card);
                    prSt.setInt(2,pin_card);
                    prSt.executeUpdate();
                    prSt.close();
                    Connection().close();


                    System.out.println("Your card has been created\n" +
                                         "Your card number:");

                    System.out.println(number_card);
                    System.out.println("Your card PIN:");
                    System.out.println(pin_card+"\n");

                    break;
                case "2":
                    System.out.println("Enter your card number:");
                    long input_number=scanner.nextLong();
                    System.out.println("Enter your PIN:");
                    int input_pin=scanner.nextInt();

                    String sql1="SELECT * FROM card WHERE number = '"+input_number+"'AND pin ='"+input_pin+"'";

                    Statement statement = Connection().createStatement();
                    ResultSet res = statement.executeQuery(sql1);
                  //  statement.close();
                    Connection().close();

                    long num=0;
                    int numc=0;
                    while (res.next()) {
                         num=res.getLong("number");
                        numc=res.getInt("pin");
                        }

                        if (input_number==num||numc==input_pin){
                            System.out.println("You have successfully logged in!");
                            logged(input_number,menu);
                        } else {
                            System.out.println("Wrong card number or PIN!");
                    }

                    break;
                case "0":

                    File filedb= new File("D:/sqlite-tools-win32-x86-3360000/sqlite-tools-win32-x86-3360000/card");
                    filedb.delete();

                    System.out.println("Bye!");
                    menu=false;
                    break;
            }
        }
    }
    public static long generate_number() {
        long number=0;
        boolean temp=true;
        while (temp){
            int min = 1000000000;
            int max = 999999999 * 10;
            int diff = max - min;
            Random random = new Random();
            int t = random.nextInt(diff + 1);
            t += min;
            String number_str = String.valueOf("400000" + t);
            long number_long = Long.valueOf(number_str);


            double digit = 0;
            int sum = 0;
            int n = 1;
            int i = 0;

             number=number_long;

            while (number_long > 0) {
                digit = number_long % 10;
                number_long = number_long / 10;

                if (i % 2 != 0) {
                    digit *= 2;
                }

                if (digit > 9) {
                    digit = (digit % 10) + 1;
                } else
                    digit *= 1;

                sum += digit;
                n++;
                i++;
            }

            if (sum % 10 == 0) {
                temp = false;
            } else
                temp = true;
        }
        return number;
    }

    public static Integer generate_pin() {
        int pin = (int)Math.floor(Math.random()*(9999-1111+1)+1111);
        return pin;
    }

    public static void logged(long input_number,boolean menu ) throws SQLException, ClassNotFoundException {
        Scanner scanner= new Scanner(System.in);


      //  boolean menu=true;

        while (menu){
            System.out.println("1. Balance\n" +
                    "2. Add income\n" +
                    "3. Do transfer\n" +
                    "4. Close account\n" +
                    "5. Log out\n" +
                    "0. Exit\n");
            String action=scanner.next();
            switch (action){
                case "1":

                    String sql1="SELECT balance FROM card WHERE number = '"+input_number+"';";

                    Statement statement = Connection().createStatement();
                    ResultSet res = statement.executeQuery(sql1);
                    int balance= res.getInt("balance");
                    System.out.println("Balance: "+balance);
                    statement.close();
                    Connection().close();
                    break;
                case "2":
                    System.out.println("Enter income:");

                  int income=scanner.nextInt();

                    String sql_inc_add="SELECT balance FROM card WHERE number = '"+input_number+"';";

                    Statement statement_bal = Connection().createStatement();
                    ResultSet res_bal = statement_bal.executeQuery(sql_inc_add);
                    int balance_inc= res_bal.getInt("balance");

                    statement_bal.close();
                    Connection().close();
                    int sum_incom=balance_inc+income;

                    String add_income="UPDATE card SET balance= '"+sum_incom+"' WHERE number = '"+input_number+"';";

                    Statement statement1 = Connection().createStatement();
                    statement1.executeUpdate(add_income);
                    System.out.println("Income was added!");
                    statement1.close();
                    Connection().close();

                    break;
                case "3":
                    System.out.println("Transfer\n" +
                            "Enter card number:\n");
                    long transfer_card= scanner.nextLong();

                    long transfer_card1=transfer_card;

                    double digit = 0;
                    int sum = 0;
                    int n = 1;
                    int i = 0;

                        digit =transfer_card % 10;
                        transfer_card = transfer_card / 10;

                        if (i % 2 != 0) {
                            digit *= 2;
                        }

                        if (digit > 9) {
                            digit = (digit % 10) + 1;
                        } else
                            digit *= 1;

                        sum += digit;
                        n++;
                        i++;

                    if (sum % 10 != 0) {
                        System.out.println("Probably you made a mistake in the card number. Please try again! \n");
                    }
                        String sql_transfer="SELECT * FROM card WHERE number = '"+transfer_card1+"';";

                        Statement statement_tr = Connection().createStatement();
                        ResultSet res_tr = statement_tr.executeQuery(sql_transfer);
                      //  statement_tr.close();
                        Connection().close();
                        long num=0;

                        while (res_tr.next()) {
                            num=res_tr.getLong("number");
                        }

                        if (transfer_card1!=num){
                            System.out.println("Such a card does not exist.");
                        } else {
                            System.out.println("Enter how much money you want to transfer:");
                            int transfer_money=scanner.nextInt();

                            String sql_balance="SELECT balance FROM card WHERE number = '"+input_number+"';";

                            Statement statement_bl = Connection().createStatement();
                            ResultSet res_bl = statement_bl.executeQuery(sql_balance);

                            int balance1=res_bl.getInt("balance");
                            statement_bl.close();
                            Connection().close();

                            if(balance1<transfer_money){
                                System.out.println("Not enough money!");
                            }else {

                               int balance2=balance1-transfer_money;
                                System.out.println(balance2);

                                String update_bal_this_card="UPDATE card SET balance= '"+balance2+"' WHERE number = '"+input_number+"';";

                                Statement statement_up = Connection().createStatement();
                                statement_up.executeUpdate(update_bal_this_card);
                                statement_up.close();
                                Connection().close();

                                String update_bal_trans_card="UPDATE card SET balance= '"+transfer_money+"' WHERE number = '"+transfer_card1+"';";

                                Statement statement_upd = Connection().createStatement();
                                statement_upd.executeUpdate(update_bal_trans_card);
                                statement_upd.close();
                                Connection().close();
                                System.out.println("Success!");
                            }
                        }

                    break;
                case "4":
                    String sql_del="DELETE FROM card WHERE number = '"+input_number+"';";
                    Statement statement_del=Connection().createStatement();
                    statement_del.executeUpdate(sql_del);
                    statement_del.close();
                    System.out.println("The account has been closed!\n");
                    menu=false;
                    break;
                case "0":
                    System.out.println("Bye!");
                   System.exit(0);
                    break;
              }
            }
        }

    public static Connection Connection() throws SQLException, ClassNotFoundException {

//        //Connect to MySql
//        Connection dbConnection;
//        String url = "jdbc:mysql://localhost:3306/banking_schema";
//        String username = "root";
//        String password = "root1";
//
//        Class.forName("com.mysql.cj.jdbc.Driver");
//
//        dbConnection=DriverManager.getConnection(url,username,password);

//        return dbConnection;

//      Connect to sqlite
        Connection conn = null;
        String url = "jdbc:sqlite:card.s3db";

        conn = DriverManager.getConnection(url);

       return conn;
    }
    public static void createNewDatabase(String fileName) throws SQLException, ClassNotFoundException {

            if (Connection() != null) {
                DatabaseMetaData meta = Connection().getMetaData();
            }
    }
}
