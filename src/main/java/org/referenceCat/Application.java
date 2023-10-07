package org.referenceCat; /**
 * Author: Mikhail Buyanov
 * Date: 26/09/2023 01:40
 */

import javax.persistence.*;
import org.referenceCat.entities.Person;
import org.referenceCat.entities.CarOwner;
import org.referenceCat.entities.Officer;
import org.referenceCat.entities.Vehicle;
import org.referenceCat.entities.Violation;
import java.util.Date;

import java.awt.*;
import javax.swing.*;


public class Application {
    private JFrame frame;

    public void show() {
        frame = new JFrame("Traffic Police database");
        frame.setSize(960, 540);
        frame.setLocation(100, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        System.out.println("Hello world");
        new Application().show();
    }
}
