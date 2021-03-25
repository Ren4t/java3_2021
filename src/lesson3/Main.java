package lesson3;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {

        try (InputStreamReader in = new InputStreamReader(new FileInputStream("demo.txt"),"UTF-8")) {
            int x;
            while ((x = in.read()) > -1) {
                System.out.print((char)x);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileInputStream in = new FileInputStream("demo.txt")) {

            System.out.println();
                System.out.print(in.read() + " ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

