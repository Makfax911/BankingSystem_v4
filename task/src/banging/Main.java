package banging;

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Scanner scanner=new Scanner(System.in);

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

                    String sql = "INSERT INTO banking_card (number_card,pin_card) Values (?,?)";

                    PreparedStatement prSt=Connection().prepareStatement(sql);
                    prSt.setLong(1,number_card);
                    prSt.setLong(2,pin_card);
                    prSt.executeUpdate();
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

                    String sql1="SELECT * FROM banking_card WHERE number_card = '"+input_number+"'AND pin_card ='"+input_pin+"';";

                    Statement statement = Connection().createStatement();
                    ResultSet res = statement.executeQuery(sql1);

                    long num=0;
                    int numc=0;
                    while (res.next()) {
                         num=res.getLong("number_card");
                        numc=res.getInt("pin_card");
                        }

                    System.out.println(numc);
                        if (input_number==num){
                            System.out.println("You have successfully logged in!");
                            logged();
                        } else {
                            System.out.println("Wrong card number or PIN!");
                    }

                    break;
                case "0":
                    System.out.println("Bye!");
                    menu=false;
                    break;
            }
        }
    }
    public static long generate_number() {

        int min = 1000000000;
        int max = 999999999*10;
        int diff = max - min;
        Random random = new Random();
        int i = random.nextInt(diff + 1);
        i += min;
        String number_str=String.valueOf("400000"+i);
        Long number_long=Long.valueOf(number_str);

        return number_long;
    }

    public static Integer generate_pin() {
        int pin = (int)Math.floor(Math.random()*(9999-1111+1)+1111);
        return pin;
    }


    public static void logged(){
        Scanner scanner= new Scanner(System.in);

        int balance = 0;
        boolean menu=true;

        while (menu){
            System.out.println("1. Balance\n" +
                    "2. Log out\n" +
                    "0. Exit\n");
            String action=scanner.next();
            switch (action){
                case "1":
                    System.out.println("Balance: "+balance);
                    break;
                case "2":
                    System.out.println("You have successfully logged out!");
                    menu=false;
                    break;
                case "0":
                    System.out.println("Bye!");
                    menu=false;
                    break;
            }
        }
    }

    public static Connection Connection() throws SQLException, ClassNotFoundException {
        Connection dbConnection;
        String url = "jdbc:mysql://localhost:3306/banking_schema";
        String username = "root";
        String password = "root1";

        Class.forName("com.mysql.cj.jdbc.Driver");

        dbConnection=DriverManager.getConnection(url,username,password);

        return dbConnection;
    }
}
