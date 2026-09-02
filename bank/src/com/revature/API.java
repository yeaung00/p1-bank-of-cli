package com.revature;

import java.util.*;

//TODO: maybe only run() should be public and everthing else private?
public class API {
    public Scanner s;

    public void launch() {
        System.out.println("Welcome to Bank of CLI!");
        while (true) {
            System.out.println("To login, click l. To register, click r");
            if (l) {
                // Login
            } else if (r) {
                // Register
            } else if (q) {
                break;
            } else {
                // Print nice error message and restart
            }
        }
    }

    public void register() {
    }

    //class is private because method should only be accessed within class (API) and not outside
    private void login(String AcountID, String PIN) {
        try {
            if(Business.verifyCredentials(AcountID, PIN)) {
                System.out.println("Login Successful!");
            }
            else {
                System.out.println("Username or password is incorrect, please try again");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public void homeAccountPage() {
    }

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

    public void run() {
        s = new Scanner(System.in);
        this.launch();
        s.close();
    }
}
