package org.referenceCat; /**
 * Author: Mikhail Buyanov
 * Date: 26/09/2023 01:40
 */

import org.referenceCat.entities.Owner;
import org.referenceCat.entities.Vehicle;
import org.referenceCat.entities.Violation;
import org.referenceCat.exceptions.ValidationException;
import org.referenceCat.ui.GhostText;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class Application {
    private JFrame frame;
    private JButton addButton, deleteButton, editButton, searchButton, reloadButton, readXMLButton, writeXMLButton;
    private JTextField searchTextField;
    private JToolBar toolBar;
    private JTabbedPane tabs;
    private JScrollPane scrollVehicles, scrollOwners, scrollViolations;
    private JTable tableVehicles, tableOwners, tableViolations;
    private DefaultTableModel modelVehicles, modelOwners, modelViolations;

    public static void main(String[] args) {
        new Application().uiInit();
    }

    private static void commitTransaction(EntityManager em) {
        em.getTransaction().commit();
    }

    private static EntityManager beginTransaction() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence_unit");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        return em;
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

        readXMLButton = new JButton();
        readXMLButton.setToolTipText("Read XML");

        writeXMLButton = new JButton();
        writeXMLButton.setToolTipText("Write XML");

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

            Image image6 = ImageIO.read(new File("src/main/resources/ui/xml-file-format-symbol.png")).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            readXMLButton.setIcon(new ImageIcon(image6));

            Image image7 = ImageIO.read(new File("src/main/resources/ui/xml-file-format-symbol.png")).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            writeXMLButton.setIcon(new ImageIcon(image7));
        } catch (Exception ignored) {

        }

        searchTextField = new JTextField();
        searchTextField.setPreferredSize(new Dimension(400, 42));
        searchTextField.setMaximumSize(searchTextField.getPreferredSize());
        GhostText ghostText = new GhostText(searchTextField, "Search");

        toolBar = new JToolBar("Tool bar");
        toolBar.setPreferredSize(new Dimension(1000, 50));

        toolBar.add(addButton);
        toolBar.add(deleteButton);
        toolBar.add(editButton);
        toolBar.add(reloadButton);
        toolBar.add(readXMLButton);
        toolBar.add(writeXMLButton);
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
        deleteButton.addActionListener(event -> onDeleteButton());

        reloadButton.addActionListener(event -> updateTable());
        searchButton.addActionListener(event -> {
            try {
                search();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Error: ".concat(e.getMessage()));
                e.printStackTrace();
            }
        });

        readXMLButton.addActionListener(event -> {
            try {
                readXML();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Error: ".concat(e.getMessage()));
                e.printStackTrace();
            }
        });

        writeXMLButton.addActionListener(event -> {
            try {
                writeXML();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Error: ".concat(e.getMessage()));
                e.printStackTrace();
            }
        });

        writeXMLButton.setEnabled(false);
        tabs.addChangeListener(e -> {
            writeXMLButton.setEnabled(tabs.getSelectedIndex() == 2);
        });

        updateTable();
    }

    private void search() throws ValidationException {
        System.out.println(searchTextField.getText());
        if (searchTextField.getText().isEmpty() || searchTextField.getText().equals("Search"))
            throw new ValidationException("empty text field");
    }

    private void updateTable() {
        EntityManager em = beginTransaction();
        List<Vehicle> vehicles = em.createQuery("SELECT v FROM Vehicle v").getResultList();

        DefaultTableModel model = (DefaultTableModel) tableVehicles.getModel();
        model.setRowCount(0);
        for (Vehicle vehicle : vehicles) {
            Owner owner = vehicle.getOwner();
            model.addRow(new Object[]{vehicle.getId(), vehicle.getRegNumber(), vehicle.getModel(), vehicle.getColor(), dateToString(vehicle.getMaintenanceDate()), owner.getId(), owner.getSurname() + " " + owner.getName() + " " + owner.getPatronymic()});
        }

        List<Owner> owners = em.createQuery("SELECT v FROM Owner v").getResultList();

        model = (DefaultTableModel) tableOwners.getModel();
        model.setRowCount(0);
        for (Owner owner : owners) {
            model.addRow(new Object[]{owner.getId(), owner.getSurname(), owner.getName(), owner.getPatronymic(), dateToString(owner.getBirthDate()), owner.getPassportId(), owner.getLicenseId()});
        }

        List<Violation> violations = em.createQuery("SELECT v FROM Violation v").getResultList();

        model = (DefaultTableModel) tableViolations.getModel();
        model.setRowCount(0);
        for (Violation violation : violations) {
            Vehicle vehicle = violation.getVehicle();
            Owner owner = vehicle.getOwner();
            model.addRow(new Object[]{violation.getId(), violation.getPenalty(), violation.getDebt(), violation.getCommentary(), dateToString(violation.getDate()), vehicle.getId(), vehicle.getRegNumber(), owner.getId(), owner.getSurname() + " " + owner.getName() + " " + owner.getPatronymic()});
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

            EntityManager em = beginTransaction();
            Owner owner = em.getReference(Owner.class, Integer.parseInt(ownerField.getText()));
            vehicle.setOwner(owner);
            em.persist(vehicle);
            commitTransaction(em);

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
                parseDate(birthDateInput.getText());
            } catch (ParseException ignored) {
            }
            owner.setBirthDate(date);
            owner.setPassportId(passportInput.getText());
            owner.setLicenseId(licenseInput.getText());


            EntityManager em = beginTransaction();
            em.persist(owner);
            commitTransaction(em);

        } else {
            String[] options = {"Debt", "Jail (10 years)", "Warning", "Deprivation of license"};
            JComboBox penaltyInput = new JComboBox(options);
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

                    if (penaltyInput.getSelectedIndex() == 0 && !isInteger(debtInput.getText()))
                        debtInput.setBackground(Color.pink);
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
                date = parseDate(dateInput.getText());
            } catch (ParseException ignored) {
            }
            violation.setDate(date);

            EntityManager em = beginTransaction();
            vehicle = em.getReference(Vehicle.class, Integer.parseInt(vehicleIdInput.getText()));
            violation.setVehicle(vehicle);
            em.persist(violation);
            commitTransaction(em);
        }

        updateTable();
    }

    private void onDeleteButton() {
        int[] indexes;
        if (tabs.getSelectedIndex() == 0) {
            indexes = tableVehicles.getSelectedRows();
            for (int index : indexes) {
                deleteVehicle((Integer) tableVehicles.getValueAt(index, 0));
            }
        } else if (tabs.getSelectedIndex() == 1) {
            indexes = tableOwners.getSelectedRows();
            for (int index : indexes) {
                deleteOwner((Integer) tableOwners.getValueAt(index, 0));
            }
        } else {
            indexes = tableViolations.getSelectedRows();
            for (int index : indexes) {
                deleteViolation((Integer) tableViolations.getValueAt(index, 0));
            }
        }
        updateTable();
    }

    private void deleteVehicle(int id) {
        EntityManager em = beginTransaction();
        Vehicle vehicle = em.find(Vehicle.class, id);
        List<Violation> violations = em.createQuery("SELECT v from Violation v WHERE vehicle_id = " + vehicle.getId()).getResultList();
        for (Violation violation : violations) deleteViolation(violation.getId());
        em.remove(vehicle);
        commitTransaction(em);
    }

    private void deleteViolation(int id) {
        EntityManager em = beginTransaction();
        Violation violation = em.find(Violation.class, id);
        em.remove(violation);
        commitTransaction(em);
    }

    private void deleteOwner(int id) {
        EntityManager em = beginTransaction();
        Owner owner = em.find(Owner.class, id);
        List<Vehicle> vehicles = em.createQuery("SELECT v from Vehicle v WHERE owner_id = " + owner.getId()).getResultList();
        for (Vehicle vehicle : vehicles) deleteVehicle(vehicle.getId());
        em.remove(owner);
        commitTransaction(em);
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
        return !string.isEmpty();
        // TODO
    }

    private boolean isDate(String string) {
        try {
            parseDate(string);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private void readXML() throws ParserConfigurationException, IOException, SAXException, ParseException {
        DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = dBuilder.parse(new File("/home/referencecat/IdeaProjects/TrafficPoliceApplication/xml_io/input.xml"));
        doc.getDocumentElement().normalize();

        EntityManager em = beginTransaction();

        NodeList nlViolations = doc.getElementsByTagName("violation");
        for (int temp = 0; temp < nlViolations.getLength(); temp++) {
            Node elem = nlViolations.item(temp);
            NamedNodeMap attrs = elem.getAttributes();
            Violation violation = new Violation();
            violation.setPenalty(attrs.getNamedItem("penalty").getNodeValue());
            if (violation.getPenalty().equals("Debt"))
                violation.setDebt(Integer.parseInt(attrs.getNamedItem("debt").getNodeValue()));
            if (attrs.getNamedItem("commentary") != null)
                violation.setCommentary(attrs.getNamedItem("commentary").getNodeValue());
            violation.setDate(parseDate(attrs.getNamedItem("date").getNodeValue()));
            Vehicle vehicle = em.find(Vehicle.class, Integer.parseInt(attrs.getNamedItem("vehicle_id").getNodeValue()));
            violation.setVehicle(vehicle);
            em.persist(violation);
        }
        commitTransaction(em);
        updateTable();
    }

    private void writeXML() throws ParserConfigurationException, TransformerException, IOException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.newDocument();
        Node violations = doc.createElement("violations");
        doc.appendChild(violations);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence_unit");
        EntityManager em = emf.createEntityManager();
        int[] indexes;
        indexes = tableViolations.getSelectedRows();
        if (indexes.length == 0) {
            indexes = new int[tableViolations.getRowCount()];
            for (int i = 0; i < tableViolations.getRowCount(); i++) indexes[i] = i;
        }
        for (int index : indexes) {
            Violation violation = em.find(Violation.class, tableViolations.getValueAt(index, 0));
            Element item = doc.createElement("violation");
            violations.appendChild(item);
            item.setAttribute("id", Integer.toString(violation.getId()));
            item.setAttribute("penalty", violation.getPenalty());
            if (violation.getPenalty().equals("Debt")) item.setAttribute("debt", Integer.toString(violation.getDebt()));
            if (violation.getCommentary() != null) item.setAttribute("commentary", violation.getCommentary());
            item.setAttribute("vehicle_id", Integer.toString(violation.getVehicle().getId()));
            item.setAttribute("date", dateToString(violation.getDate()));
        }

        Transformer trans = TransformerFactory.newInstance().newTransformer();
        java.io.FileWriter fw = new FileWriter("/home/referencecat/IdeaProjects/TrafficPoliceApplication/xml_io/output.xml");
        trans.transform(new DOMSource(doc), new StreamResult(fw));
    }

    private Date parseDate(String s) throws ParseException {
        return new SimpleDateFormat("dd.MM.yyyy").parse(s);
    }

    private String dateToString(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat("dd.MM.yyyy").format(date);
    }
}

