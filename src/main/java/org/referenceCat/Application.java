package org.referenceCat; /**
 * Author: Mikhail Buyanov
 * Date: 26/09/2023 01:40
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.awt.event.*;


public class Application {
    private JFrame frame;
    private JButton addButton, deleteButton, editButton, searchButton, reloadButton;
    private JTextField searchTextField;
    private JToolBar toolBar;
    private JTabbedPane tabs;
    private JScrollPane scrollVehicles, scrollOwners, scrollViolations;
    private JTable tableVehicles, tableOwners, tableViolations;
    private DefaultTableModel modelVehicles, modelOwners, modelViolations;

    public static void main(String[] args) {
        new Application().show();
    }

    public void show() {
        frame = new JFrame("Traffic Police database");
        frame.setSize(1500, 900);
        frame.setLocation(100, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addButton = new JButton();
        addButton.setToolTipText("Add");

        deleteButton = new JButton();
        deleteButton.setToolTipText("Delete");

        editButton = new JButton();
        editButton.setToolTipText("Edit");

        searchButton = new JButton();
        searchButton.setToolTipText("Search");

        reloadButton = new JButton();
        reloadButton.setToolTipText("Reload");

        try {
            Image image1 = ImageIO.read(new File("src/main/resources/ui/plus.png")).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            addButton.setIcon(new ImageIcon(image1));

            Image image2 = ImageIO.read(new File("src/main/resources/ui/minus.png")).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            deleteButton.setIcon(new ImageIcon(image2));

            Image image3 = ImageIO.read(new File("src/main/resources/ui/editing.png")).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            editButton.setIcon(new ImageIcon(image3));

            Image image4 = ImageIO.read(new File("src/main/resources/ui/search.png")).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            searchButton.setIcon(new ImageIcon(image4));

            Image image5 = ImageIO.read(new File("src/main/resources/ui/reload.png")).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            reloadButton.setIcon(new ImageIcon(image5));
        } catch (Exception e) {
        }

        searchTextField = new JTextField("search");
        searchTextField.setPreferredSize(new Dimension(400, 42));
        searchTextField.setMaximumSize(searchTextField.getPreferredSize());


        // Добавление кнопок на панель инструментов
        toolBar = new JToolBar("Tool bar");
        toolBar.setPreferredSize(new Dimension(1000, 50));

        toolBar.add(addButton);
        toolBar.add(deleteButton);
        toolBar.add(editButton);
        toolBar.add(reloadButton);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(searchTextField);
        toolBar.add(searchButton);
        frame.setLayout(new BorderLayout());
        frame.add(toolBar, BorderLayout.NORTH);

        scrollVehicles = new JScrollPane();
        scrollViolations = new JScrollPane();
        scrollOwners = new JScrollPane();

        String[] columnsVehicles = {"id", "reg number", "model", "color", "maintenance date", "oid", "owner", "passport", "license", "status"};
        modelVehicles = new DefaultTableModel(columnsVehicles, 100);
        tableVehicles = new JTable(modelVehicles);
        scrollVehicles = new JScrollPane(tableVehicles);

        tableVehicles.getColumnModel().getColumn(0).setMaxWidth(30);
        tableVehicles.getColumnModel().getColumn(0).setMinWidth(30);
        tableVehicles.getColumnModel().getColumn(1).setMaxWidth(100);
        tableVehicles.getColumnModel().getColumn(1).setMinWidth(100);
        tableVehicles.getColumnModel().getColumn(5).setMaxWidth(30);
        tableVehicles.getColumnModel().getColumn(5).setMinWidth(30);

        String[] columnsOwners = {"id", "name", "surname", "patronymic", "passport", "license"};
        modelOwners = new DefaultTableModel(columnsOwners, 10);
        tableOwners = new JTable(modelOwners);
        scrollOwners = new JScrollPane(tableOwners);

        tableOwners.getColumnModel().getColumn(0).setMaxWidth(30);
        tableOwners.getColumnModel().getColumn(0).setMinWidth(30);

        String[] columnsViolations = {"id", "penalty", "debt", "commentary", "cid", "reg number", "oid", "owner"};
        modelViolations = new DefaultTableModel(columnsViolations, 10);
        tableViolations = new JTable(modelViolations);
        scrollViolations = new JScrollPane(tableViolations);

        tableViolations.getColumnModel().getColumn(0).setMaxWidth(30);
        tableViolations.getColumnModel().getColumn(0).setMinWidth(30);
        tableViolations.getColumnModel().getColumn(4).setMaxWidth(30);
        tableViolations.getColumnModel().getColumn(4).setMinWidth(30);
        tableViolations.getColumnModel().getColumn(6).setMaxWidth(30);
        tableViolations.getColumnModel().getColumn(6).setMinWidth(30);

        tabs = new JTabbedPane();
        tabs.add("Vehicles", scrollVehicles);
        tabs.add("Owners", scrollOwners);
        tabs.add("Violations", scrollViolations);
        tabs.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(tabs);

        frame.setVisible(true);

        addButton.addActionListener (event -> JOptionPane.showMessageDialog (frame, "*Плейсхолдер*"));
    }
}

