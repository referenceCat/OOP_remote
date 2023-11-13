package org.referenceCat; /**
 * Author: Mikhail Buyanov
 * Date: 26/09/2023 01:40
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.referenceCat.exceptions.ValidationException;
import org.referenceCat.ui.GhostText;
import java.awt.Color;


import javax.persistence.*;

import org.referenceCat.entities.Owner;
import org.referenceCat.entities.Vehicle;
import org.referenceCat.entities.Violation;
import java.util.Date;


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
        new Application().uiInit();
    }

    public void uiInit() {
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
        } catch (Exception ignored) {
        }

        searchTextField = new JTextField();
        searchTextField.setPreferredSize(new Dimension(400, 42));
        searchTextField.setMaximumSize(searchTextField.getPreferredSize());
        GhostText ghostText = new GhostText(searchTextField, "Search");


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

        String[] columnsVehicles = {"id", "reg number", "model", "color", "maintenance date", "oid", "owner"};
        modelVehicles = new DefaultTableModel(columnsVehicles, 100);
        tableVehicles = new JTable(modelVehicles);
        scrollVehicles = new JScrollPane(tableVehicles);

        tableVehicles.getColumnModel().getColumn(0).setMaxWidth(30);
        tableVehicles.getColumnModel().getColumn(0).setMinWidth(30);
        tableVehicles.getColumnModel().getColumn(1).setMaxWidth(100);
        tableVehicles.getColumnModel().getColumn(1).setMinWidth(100);
        tableVehicles.getColumnModel().getColumn(5).setMaxWidth(30);
        tableVehicles.getColumnModel().getColumn(5).setMinWidth(30);

        String[] columnsOwners = {"id", "surname", "name", "patronymic", "birth date", "passport", "license"};
        modelOwners = new DefaultTableModel(columnsOwners, 10);
        tableOwners = new JTable(modelOwners);
        scrollOwners = new JScrollPane(tableOwners);

        tableOwners.getColumnModel().getColumn(0).setMaxWidth(30);
        tableOwners.getColumnModel().getColumn(0).setMinWidth(30);

        String[] columnsViolations = {"id", "penalty", "debt", "commentary", "date", "cid", "reg number", "oid", "owner"};
        modelViolations = new DefaultTableModel(columnsViolations, 10);
        tableViolations = new JTable(modelViolations);
        scrollViolations = new JScrollPane(tableViolations);

        tableViolations.getColumnModel().getColumn(0).setMaxWidth(30);
        tableViolations.getColumnModel().getColumn(0).setMinWidth(30);
        tableViolations.getColumnModel().getColumn(5).setMaxWidth(30);
        tableViolations.getColumnModel().getColumn(5).setMinWidth(30);
        tableViolations.getColumnModel().getColumn(7).setMaxWidth(30);
        tableViolations.getColumnModel().getColumn(7).setMinWidth(30);

        tabs = new JTabbedPane();
        tabs.add("Vehicles", scrollVehicles);
        tabs.add("Owners", scrollOwners);
        tabs.add("Violations", scrollViolations);
        tabs.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(tabs);

        frame.setVisible(true);

        addButton.addActionListener(event -> onAddButton());
        deleteButton.addActionListener (event -> onDeleteButton());

        reloadButton.addActionListener(event -> updateTable());
        searchButton.addActionListener(event -> {
            try {
                search();
            } catch (Exception e) {
                JOptionPane.showMessageDialog (frame, "Error: ".concat(e.getMessage()));
            }
        });
        updateTable();
    }

    private void search() throws ValidationException {
        System.out.println(searchTextField.getText());
        if (searchTextField.getText().isEmpty() || searchTextField.getText().equals("Search")) throw new ValidationException("empty text field");
    }

    private void updateTable() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence_unit");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        List<Vehicle> vehicles =em.createQuery("SELECT v FROM Vehicle v").getResultList();

        DefaultTableModel model = (DefaultTableModel) tableVehicles.getModel();
        model.setRowCount(0);
        for (Vehicle vehicle: vehicles) {
            Owner owner = vehicle.getOwner();
            model.addRow(new Object[]{vehicle.getId(), vehicle.getRegNumber(), vehicle.getModel(), vehicle.getColor(), vehicle.getMaintenanceDate(), owner.getId(), owner.getSurname() + " " + owner.getName() + " " + owner.getPatronymic()});
        }

        List<Owner> owners = em.createQuery("SELECT v FROM Owner v").getResultList();

        model = (DefaultTableModel) tableOwners.getModel();
        model.setRowCount(0);
        for (Owner owner: owners) {
            model.addRow(new Object[]{owner.getId(), owner.getSurname(), owner.getName(), owner.getPatronymic(), owner.getBirthDate(), owner.getPassportId(), owner.getLicenseId()});
        }

        List<Violation> violations = em.createQuery("SELECT v FROM Violation v").getResultList();

        model = (DefaultTableModel) tableViolations.getModel();
        model.setRowCount(0);
        for (Violation violation: violations) {
            Vehicle vehicle = violation.getVehicle();
            Owner owner = vehicle.getOwner();
            model.addRow(new Object[]{violation.getId(), violation.getPenalty(), violation.getDebt(), violation.getCommentary(), violation.getDate(), vehicle.getId(), vehicle.getRegNumber(), owner.getId(), owner.getSurname() + " " + owner.getName() + " " + owner.getPatronymic()});
        }
    }

    private void onAddButton() {
        if (tabs.getSelectedIndex() == 0) {
            JTextField registrationField = new JTextField();
            JTextField modelField = new JTextField();
            JTextField colorField = new JTextField();
            JTextField ownerField = new JTextField();
            final JComponent[] inputs = new JComponent[]{
                    new JLabel("Registration number: *"),
                    registrationField,
                    new JLabel("Model:"),
                    modelField,
                    new JLabel("Color:"),
                    colorField,
                    new JLabel("Owner id: *"),
                    ownerField
            };
            while (true) {
                int result = JOptionPane.showConfirmDialog(null, inputs, "Enter vehicle data", JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    if (isRegNumberValid(registrationField.getText()) && isInteger(ownerField.getText())) break;
                    if (!isRegNumberValid(registrationField.getText())) {
                        registrationField.setBackground(Color.pink);
                    }

                    if (!isInteger(ownerField.getText())) {
                        ownerField.setBackground(Color.pink);
                    }
                } else return;
            }

            Vehicle vehicle = new Vehicle();
            vehicle.setRegNumber(registrationField.getText());
            if (!modelField.getText().isEmpty()) vehicle.setModel(modelField.getText());
            if (!colorField.getText().isEmpty()) vehicle.setColor(colorField.getText());

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence_unit");
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            Owner owner = em.getReference(Owner.class, Integer.parseInt(ownerField.getText()));
            vehicle.setOwner(owner);
            em.persist(vehicle);
            em.getTransaction().commit();

        } else if (tabs.getSelectedIndex() == 1) {
            JTextField surnameInput = new JTextField();
            JTextField nameInput = new JTextField();
            JTextField patronymicInput = new JTextField();
            JTextField birthDateInput = new JTextField();
            JTextField passportInput = new JTextField();
            JTextField licenseInput = new JTextField();
            final JComponent[] inputs = new JComponent[]{
                    new JLabel("Surname: *"),
                    surnameInput,
                    new JLabel("Name: *"),
                    nameInput,
                    new JLabel("Patronymic:"),
                    patronymicInput,
                    new JLabel("Birth date: *"),
                    birthDateInput,
                    new JLabel("Passport number: *"),
                    passportInput,
                    new JLabel("License number: *"),
                    licenseInput
            };
            while (true) {
                int result = JOptionPane.showConfirmDialog(null, inputs, "Enter person's data", JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    if (!surnameInput.getText().isEmpty()
                            && !nameInput.getText().isEmpty()
                            && isDate(birthDateInput.getText())
                            && !passportInput.getText().isEmpty()
                            && !licenseInput.getText().isEmpty()) break;

                    if (surnameInput.getText().isEmpty()) surnameInput.setBackground(Color.pink);
                    if (nameInput.getText().isEmpty()) nameInput.setBackground(Color.pink);
                    if (!isDate(birthDateInput.getText())) birthDateInput.setBackground(Color.pink);
                    if (passportInput.getText().isEmpty()) passportInput.setBackground(Color.pink);
                    if (licenseInput.getText().isEmpty()) licenseInput.setBackground(Color.pink);
                } else return;
            }

            Owner owner = new Owner();
            owner.setSurname(surnameInput.getText());
            owner.setName(nameInput.getText());
            if (!patronymicInput.getText().isEmpty()) owner.setPatronymic(patronymicInput.getText());

            Date date = new Date();
            try {
                 date = new SimpleDateFormat("dd.MM.yyyy").parse(birthDateInput.getText());
            } catch (ParseException ignored) {}
            owner.setBirthDate(date);
            owner.setPassportId(passportInput.getText());
            owner.setLicenseId(licenseInput.getText());


            EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence_unit");
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            em.persist(owner);
            em.getTransaction().commit();

        } else {
            String[] options = {"Debt", "Jail (10 years)", "Warning", "Deprivation of license"};
            JComboBox penaltyInput= new JComboBox(options);
            JTextField debtInput = new JTextField();
            JTextField commentaryInput = new JTextField();
            JTextField dateInput = new JTextField();
            JTextField vehicleIdInput = new JTextField();
            final JComponent[] inputs = new JComponent[]{
                    new JLabel("Penalty: *"),
                    penaltyInput,
                    new JLabel("Debt: "),
                    debtInput,
                    new JLabel("Commentary:"),
                    commentaryInput,
                    new JLabel("Date: *"),
                    dateInput,
                    new JLabel("Vehicle id: *"),
                    vehicleIdInput
            };
            while (true) {
                int result = JOptionPane.showConfirmDialog(null, inputs, "Enter violation data", JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    if (!(penaltyInput.getSelectedIndex() == 0 && !isInteger(debtInput.getText()))
                            && isDate(dateInput.getText())
                            && isInteger(vehicleIdInput.getText())) break;

                    if (penaltyInput.getSelectedIndex() == 0 && !isInteger(debtInput.getText())) debtInput.setBackground(Color.pink);
                    if (!isDate(dateInput.getText())) dateInput.setBackground(Color.pink);
                    if (!isInteger(vehicleIdInput.getText())) vehicleIdInput.setBackground(Color.pink);
                } else return;
            }

            Violation violation = new Violation();
            Vehicle vehicle;
            violation.setPenalty(options[penaltyInput.getSelectedIndex()]);
            if (penaltyInput.getSelectedIndex() == 0) violation.setDebt(Integer.parseInt(debtInput.getText()));
            if (!commentaryInput.getText().isEmpty()) violation.setCommentary(commentaryInput.getText());

            Date date = new Date();
            try {
                date = new SimpleDateFormat("dd.MM.yyyy").parse(dateInput.getText());
            } catch (ParseException ignored) {}
            violation.setDate(date);

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence_unit");
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            vehicle = em.getReference(Vehicle.class, Integer.parseInt(vehicleIdInput.getText()));
            violation.setVehicle(vehicle);
            em.persist(violation);
            em.getTransaction().commit();
        }

        updateTable();
    }

    private void onDeleteButton() {
        int[] indexes;
        if (tabs.getSelectedIndex() == 0) {
            indexes = tableVehicles.getSelectedRows();
            for (int index : indexes) {
                deleteVehilce((Integer) tableVehicles.getValueAt(index, 0));
            }
        } else if (tabs.getSelectedIndex() == 1) {
            indexes = tableOwners.getSelectedRows();
            for (int index : indexes) {
                deleteOwner((Integer) tableOwners.getValueAt(index, 0));
            }
        } else {
            indexes = tableOwners.getSelectedRows();
            for (int index : indexes) {
                deleteViolation((Integer) tableViolations.getValueAt(index, 0));
            }
        }
        updateTable();
    }

    private void deleteVehilce(int id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence_unit");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        Vehicle vehicle = em.find(Vehicle.class, id);
        List<Violation> violations = em.createQuery("SELECT v from Violation v WHERE vehicle_id = " + Integer.toString(vehicle.getId())).getResultList();
        for (Violation violation : violations) deleteViolation(violation.getId());
        em.remove(vehicle);
        em.getTransaction().commit();
    }

    private void deleteViolation(int id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence_unit");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        Violation violation = em.find(Violation.class, id);
        em.remove(violation);
        em.getTransaction().commit();
    }

    private void deleteOwner(int id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence_unit");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        Owner owner = em.find(Owner.class, id);
        List<Vehicle> vehicles = em.createQuery("SELECT v from Vehicle v WHERE owner_id = " + Integer.toString(owner.getId())).getResultList();
        for (Vehicle vehicle : vehicles) deleteVehilce(vehicle.getId());
        em.remove(owner);
        em.getTransaction().commit();
    }


    private boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isRegNumberValid(String string) {
        if (string.isEmpty()) return false;
        // TODO
        return true;
    }

    private boolean isDate(String string) {
        try {
            Date date = new SimpleDateFormat("dd.MM.yyyy").parse(string);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}

