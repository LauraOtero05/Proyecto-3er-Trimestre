package com.m74.proyecto_crm.util;

import java.util.Scanner;

public class InputHelper {

    private static final Scanner sc = new Scanner(System.in);

    public static String readString(String prompt) {

        String value;

        do {

            System.out.print(prompt);
            value = sc.nextLine().trim();

            if (value.isEmpty())
                System.out.println("El campo no puede estar vacío.");

        } while (value.isEmpty());

        return value;
    }

    public static int readInt(String prompt) {

        System.out.print(prompt);

        while (!sc.hasNextInt()) {

            sc.nextLine();
            System.out.print("Solo números. Intenta de nuevo: ");
        }

        int value = sc.nextInt();
        sc.nextLine();

        return value;
    }

    public static int readIntInRange(String prompt, int min, int max) {

        int value;

        do {

            value = readInt(prompt);

            if (value < min || value > max)
                System.out.println("Introduce un número entre " + min + " y " + max);

        } while (value < min || value > max);

        return value;
    }

    public static double readDouble(String prompt) {

        System.out.print(prompt);

        while (!sc.hasNextDouble()) {

            System.out.print("Solo números decimales. Intenta de nuevo: ");
            sc.nextLine();
        }

        double value = sc.nextDouble();
        sc.nextLine();

        return value;
    }
}
