package view;

import controller.BankController;
import model.Language;

import java.util.Scanner;

public class Cli {
    String a;
    String b;
    private Language lang;

    private final Scanner scanner;
    BankController my;

    public Cli(Language lang) {
        scanner = new Scanner(System.in);
        menue();
        eingabe();
    }

    public void menue() {
        System.out.println(lang.trans("welcomever"));
        System.out.println("=================================================");
//        System.out.println("Status: " + lang.connectedStr());
        System.out.println("menu - blendet dieses Menü ein");
        System.out.println("on   - Anmelden");
        System.out.println("off  - Abmelden");

        if (my.isLoggedIn()) {
            System.out.println("1    - Kontowahl");
            System.out.println("2    - Konto anlegen");
            System.out.println("3    - Konto auflösen");
            System.out.println("4    - Einzahlen");
            System.out.println("5    - Auszahlen");
            System.out.println("6    - Überweisung");
            System.out.println("7    - Umsätze");
            System.out.println("8    - Kontoinfo");
            System.out.println("9    - Festgeldkonto");
            System.out.println("10   - Sparbuch");
            System.out.println("11   - Arbeitsdatum");
        }
        System.out.println("x    - Beenden");
    }

    private void eingabe() {
        String a;
        do {
            System.out.print("\nMyBank> ");
            a = scanner.nextLine();
            exek(a);
        }
        while (!a.equals("x"));
    }

    private void exek(String ein) {
        if (ein.equals("")) eingabe();
        if (ein.equals("menu")) menue();
        if (ein.equals("x")) {
            System.out.println("Bye");
            my.quit();
        }
        if (ein.equals("conntest")) {
            System.out.println("\n" + lang.trans("connmess") + "\n" + lang.trans("connmess2") + "\n");
            System.out.println(my.connTest());
        }
        if (ein.equals("on")) login();
        if (ein.equals("off")) logout();
        if (ein.equals("saldo")) System.out.println(my.k_gesamtSaldo() + " Euro");
    }

    private void logout() {
        System.out.println(lang.trans("user") + " " + my.k_getFullname() + " " + lang.trans("logofft") + "\n" + lang.trans("welcomever"));
        my.dbLogoff();
    }

    private void login() {
        System.out.print(lang.trans("user") + ": ");
        a = scanner.next();
        System.out.print(lang.trans("passwort") + ": ");
        b = scanner.next();
        int lev = my.dbLogin(a, b);
        if (lev == 0) {
            System.out.print(lang.trans("user") + " " + my.k_getFullname() + " " + lang.trans("userconn"));
        }
    }
}
