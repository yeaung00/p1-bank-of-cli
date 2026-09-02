package com.revature;

import java.util.*;

public class API {
    // Attributes
    public Scanner s;

    // Constructors

    //////////////
    // Methods ///
    //////////////
    
    // Connor
    public void launch() {
        System.out.println("Welcome to Bank of CLI!");
        while (true) {
            System.out.println("To login, click l. To register, click r");
            if (l) {
                // Login
            } else if (r) {
                // Register
            } else if (q) {
                // Quit loop
                break;
            } else {
                // Print nice error message and restart
            }
        }
    }

    // After you register, it should send you back to the launch to login
    // Ydur
    public void register() {

    }

    // Yousef
    public void login() {

    }

    // This will be the query loop where it will ask you what you want to do:
    // view balance, deposit, withdraw, transfer, or view activity
    // Damon
    public void homeAccountPage() {
        // Another query loop with those 5 tasks
    }


    // First come first serve for these 5
    public void viewBalance() {

    }

    public void deposit() {

    }

    public void withdraw() {

    }

    public void transfer() {

    }

    public void viewActivity() {

    }

    // Main
    public void run() {
        s = new Scanner(System.in);
        this.launch();
        s.close();
    }
}
