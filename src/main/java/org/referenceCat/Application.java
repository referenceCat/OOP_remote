package org.referenceCat; /**
 * Author: Mikhail Buyanov
 * Date: 26/09/2023 01:40
 */

import org.apache.fop.apps.*;
import org.referenceCat.entities.Owner;
import org.referenceCat.entities.Vehicle;
import org.referenceCat.entities.Violation;
import org.referenceCat.exceptions.ValidationException;
import org.referenceCat.ui.GhostText;
import org.referenceCat.ui.OwnerDialog;
import org.referenceCat.ui.VehicleDialog;
import org.referenceCat.ui.ViolationDialog;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import org.referenceCat.utils.Utilities;


public class Application {
    private JFrame frame;
    private JButton addButton, deleteButton, editButton, searchButton, reloadButton, readXMLButton, writeXMLButton;
    private JTextField searchTextField;
    private JToolBar toolBar;
    private JTabbedPane tabs;
    private JScrollPane scrollVehicles, scrollOwners, scrollViolations;
    private JTable tableVehicles, tableOwners, tableViolations;
    private DefaultTableModel modelVehicles, modelOwners, modelViolations;

    final static Logger logger = Logger.getLogger(Application.class);

    public static void main(String[] args) {
        new Application().initGUI();
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

    public void initGUI() {
        frame = new JFrame("Traffic Police database");
        frame.setSize(1500, 900);
        frame.setLocation(100, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        initToolBar();
        initTables();
        initListeners();
        updateTable();

        frame.setVisible(true);
        logger.info("Frame is open");
    }

    private void initToolBar() {
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
        initButtonIcons();
        frame.add(toolBar, BorderLayout.NORTH);
        logger.info("Tool bar initialized");
    }

    private void initTables() {
        scrollVehicles = new JScrollPane();
        scrollViolations = new JScrollPane();
        scrollOwners = new JScrollPane();

        String[] columnsVehicles = {"id", "reg number", "model", "color", "maintenance date", "oid", "owner"};
        modelVehicles = new DefaultTableModel(columnsVehicles, 100) {
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };
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
        tableOwners = new JTable(modelOwners) {
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };
        scrollOwners = new JScrollPane(tableOwners);

        tableOwners.getColumnModel().getColumn(0).setMaxWidth(30);
        tableOwners.getColumnModel().getColumn(0).setMinWidth(30);

        String[] columnsViolations = {"id", "penalty", "debt", "commentary", "date", "cid", "reg number", "oid", "owner"};
        modelViolations = new DefaultTableModel(columnsViolations, 10);
        tableViolations = new JTable(modelViolations) {
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };
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

        logger.info("Tables initialized");
    }

    private void initListeners() {
        addButton.addActionListener(event -> onAddButton());
        deleteButton.addActionListener(event -> onDeleteButton());
        editButton.addActionListener(event -> onEditButton());

        reloadButton.addActionListener(event -> updateTable());
        searchButton.addActionListener(event -> {
            try {
                search();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Something went wrong", "Error", JOptionPane.ERROR_MESSAGE);
                logger.error("Exception ", e);
            }
        });

        readXMLButton.addActionListener(event -> {
            try {
                readXML();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Something went wrong", "Error", JOptionPane.ERROR_MESSAGE);
                logger.error("Exception ", e);
            }
        });

        writeXMLButton.addActionListener(event -> {
            try {
                writeXML("/home/referencecat/IdeaProjects/TrafficPoliceApplication/xml_io/output.xml");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Something went wrong", "Error", JOptionPane.ERROR_MESSAGE);
                logger.error("Exception ", e);
            }
        });

        writeXMLButton.setEnabled(false);
        tabs.addChangeListener(e -> {
            writeXMLButton.setEnabled(tabs.getSelectedIndex() == 2);
        });

//        frame.addKeyListener(new KeyListener() {
//            @Override
//            public void keyTyped(KeyEvent e) {
//                if (e.getKeyCode() == KeyEvent.VK_ENTER && !searchTextField.getText().isEmpty()) {
//                    search();
//                }
//            }
//
//            @Override
//            public void keyPressed(KeyEvent e) {
//                if (e.getKeyCode() == KeyEvent.VK_ENTER && !searchTextField.getText().isEmpty()) {
//                    search();
//                }
//            }
//
//            @Override
//            public void keyReleased(KeyEvent e) {
//                if (e.getKeyCode() == KeyEvent.VK_ENTER && !searchTextField.getText().isEmpty()) {
//                    search();
//                }
//            }
//        });
        logger.info("Listeners initialized");
    }

    private void initButtonIcons() {
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

            logger.info("Button images initialized");
        } catch (Exception e) {
            logger.error("Exception ", e);
        }
    }

    private void search() {
        tableVehicles.setRowHeight(16);
        tableOwners.setRowHeight(16);
        tableViolations.setRowHeight(16);
        if (searchTextField.getText().isEmpty() || searchTextField.getText().equals("Search")) return;
        if (tabs.getSelectedIndex() == 0) {

            for (int i = 0; i < tableVehicles.getRowCount(); i++) {
                if (!Integer.toString((Integer) tableVehicles.getValueAt(i, 0)).contains(searchTextField.getText()) &&
                        !((String) tableVehicles.getValueAt(i, 1)).contains(searchTextField.getText()) &&
                        !((String) tableVehicles.getValueAt(i, 2)).contains(searchTextField.getText()) &&
                        !((String) tableVehicles.getValueAt(i, 3)).contains(searchTextField.getText()) &&
                        !((String) tableVehicles.getValueAt(i, 4)).contains(searchTextField.getText()) &&
                        !Integer.toString((Integer) tableVehicles.getValueAt(i, 5)).contains(searchTextField.getText()) &&
                        !((String) tableVehicles.getValueAt(i, 6)).contains(searchTextField.getText())) {
                    tableVehicles.setRowHeight(i, 1);
                }
            }
        } else if (tabs.getSelectedIndex() == 1) {
            for (int i = 0; i < tableOwners.getRowCount(); i++) {
                if (!Integer.toString((Integer) tableOwners.getValueAt(i, 0)).contains(searchTextField.getText()) &&
                        !((String) tableOwners.getValueAt(i, 1)).contains(searchTextField.getText()) &&
                        !((String) tableOwners.getValueAt(i, 2)).contains(searchTextField.getText()) &&
                        !((String) tableOwners.getValueAt(i, 4)).contains(searchTextField.getText()) &&
                        !((String) tableOwners.getValueAt(i, 5)).contains(searchTextField.getText()) &&
                        !((String) tableOwners.getValueAt(i, 6)).contains(searchTextField.getText()) &&
                        !((String) tableOwners.getValueAt(i, 6)).contains(searchTextField.getText())) {
                    tableOwners.setRowHeight(i, 1);
                }
            }
        } else {
            for (int i = 0; i < tableViolations.getRowCount(); i++) {
                if (!Integer.toString((Integer) tableViolations.getValueAt(i, 0)).contains(searchTextField.getText()) &&
                        !((String) tableViolations.getValueAt(i, 1)).contains(searchTextField.getText()) &&
                        !Integer.toString((Integer) tableViolations.getValueAt(i, 2)).contains(searchTextField.getText()) &&
                        !((String) tableViolations.getValueAt(i, 3)).contains(searchTextField.getText()) &&
                        !((String) tableViolations.getValueAt(i, 4)).contains(searchTextField.getText()) &&
                        !Integer.toString((Integer) tableViolations.getValueAt(i, 5)).contains(searchTextField.getText()) &&
                        !((String) tableViolations.getValueAt(i, 6)).contains(searchTextField.getText()) &&
                        !Integer.toString((Integer) tableViolations.getValueAt(i, 7)).contains(searchTextField.getText()) &&
                        !((String) tableViolations.getValueAt(i, 8)).contains(searchTextField.getText())) {
                    tableViolations.setRowHeight(i, 1);
                }
            }
        }
        searchTextField.setText("");
        logger.info("Search completed");
    }

    private void updateTable() {
        EntityManager em = beginTransaction();
        List<Vehicle> vehicles = em.createQuery("SELECT v FROM Vehicle v").getResultList();

        DefaultTableModel model = (DefaultTableModel) tableVehicles.getModel();
        model.setRowCount(0);
        for (Vehicle vehicle : vehicles) {
            Owner owner = vehicle.getOwner();
            model.addRow(new Object[]{vehicle.getId(), vehicle.getRegNumber(), vehicle.getModel(), vehicle.getColor(), Utilities.dateToString(vehicle.getMaintenanceDate()), owner.getId(), owner.getSurname() + " " + owner.getName() + " " + owner.getPatronymic()});
        }

        List<Owner> owners = em.createQuery("SELECT v FROM Owner v").getResultList();

        model = (DefaultTableModel) tableOwners.getModel();
        model.setRowCount(0);
        for (Owner owner : owners) {
            model.addRow(new Object[]{owner.getId(), owner.getSurname(), owner.getName(), owner.getPatronymic(), Utilities.dateToString(owner.getBirthDate()), owner.getPassportId(), owner.getLicenseId()});
        }

        List<Violation> violations = em.createQuery("SELECT v FROM Violation v").getResultList();

        model = (DefaultTableModel) tableViolations.getModel();
        model.setRowCount(0);
        for (Violation violation : violations) {
            Vehicle vehicle = violation.getVehicle();
            Owner owner = vehicle.getOwner();
            model.addRow(new Object[]{violation.getId(), violation.getPenalty(), violation.getDebt(), violation.getCommentary(), Utilities.dateToString(violation.getDate()), vehicle.getId(), vehicle.getRegNumber(), owner.getId(), owner.getSurname() + " " + owner.getName() + " " + owner.getPatronymic()});
        }

        tableVehicles.setRowHeight(16);
        tableOwners.setRowHeight(16);
        tableViolations.setRowHeight(16);
        logger.info("Table updated");
    }

    private void onAddButton() {
        if (tabs.getSelectedIndex() == 0) {
            vehicleAdditionDialog();
        } else if (tabs.getSelectedIndex() == 1) {
            ownerAdditionDialog();
        } else {
            violationAdditionDialog();
        }
    }

    private void vehicleAdditionDialog() {
        VehicleDialog vehicleDialog = new VehicleDialog(frame);
        vehicleDialog.applyButton.addActionListener(e -> {
            try {
                Vehicle vehicle = new Vehicle();
                vehicle.setRegNumber(vehicleDialog.regNumberInput.getText());
                if (!vehicleDialog.modelInput.getText().isEmpty()) vehicle.setModel(vehicleDialog.modelInput.getText());
                if (!vehicleDialog.colorInput.getText().isEmpty()) vehicle.setColor(vehicleDialog.colorInput.getText());

                vehicle.setMaintenanceDate(Utilities.parseDate(vehicleDialog.maintenanceDateInput.getText()));

                EntityManager em = beginTransaction();
                Owner owner = em.find(Owner.class, Integer.parseInt(vehicleDialog.ownerIdInput.getText()));
                if (owner == null) throw new PersistenceException("Owner not found");
                vehicle.setOwner(owner);
                em.persist(vehicle);
                commitTransaction(em);
                vehicleDialog.dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Something went wrong", "Error", JOptionPane.ERROR_MESSAGE);
                logger.error("Exception ", ex);
            } finally {
                updateTable();
            }
        });
        vehicleDialog.show();
    }

    private void ownerAdditionDialog() {
        OwnerDialog ownerDialog = new OwnerDialog(frame);
        ownerDialog.applyButton.addActionListener(e -> {
            try {
                Owner owner = new Owner();
                owner.setSurname(ownerDialog.surnameInput.getText());
                owner.setName(ownerDialog.nameInput.getText());
                if (!ownerDialog.patronymicInput.getText().isEmpty()) owner.setPatronymic(ownerDialog.patronymicInput.getText());
                owner.setBirthDate(Utilities.parseDate(ownerDialog.birthDateInput.getText()));
                owner.setPassportId(ownerDialog.passportInput.getText());
                owner.setLicenseId(ownerDialog.licenseInput.getText());

                EntityManager em = beginTransaction();
                em.persist(owner);
                commitTransaction(em);
                ownerDialog.dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Something went wrong", "Error", JOptionPane.ERROR_MESSAGE);
                logger.error("Exception ", ex);
            } finally {
                updateTable();
            }
        });
        ownerDialog.show();
    }

    private void violationAdditionDialog() {
        ViolationDialog violationDialog = new ViolationDialog(frame);
        violationDialog.applyButton.addActionListener(e -> {
            try {
                Violation violation = new Violation();
                violation.setPenalty((String) violationDialog.penaltyInput.getSelectedItem());
                if (violationDialog.penaltyInput.getSelectedIndex() == 0) violation.setDebt(Integer.parseInt(violationDialog.debtInput.getText()));
                if (!violationDialog.commentaryInput.getText().isEmpty()) violation.setCommentary(violationDialog.commentaryInput.getText());
                violation.setDate(Utilities.parseDate(violationDialog.dateInput.getText()));

                EntityManager em = beginTransaction();
                Vehicle vehicle = em.find(Vehicle.class, Integer.parseInt(violationDialog.vehicleIdInput.getText()));
                if (vehicle == null) throw new PersistenceException("Vehicle not found");
                violation.setVehicle(vehicle);
                em.persist(violation);
                commitTransaction(em);
                violationDialog.dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Something went wrong", "Error", JOptionPane.ERROR_MESSAGE);
                logger.error("Exception ", ex);
            } finally {
                updateTable();
            }
        });
        violationDialog.show();
    }

    private void onEditButton() {
        if (tabs.getSelectedIndex() == 0) {
            vehicleEditingDialog();
        } else if (tabs.getSelectedIndex() == 1) {
            ownerEditingDialog();
        } else {
            violationEditingDialog();
        }
    }

    private void vehicleEditingDialog() {

        int selectedRow = tableVehicles.getSelectedRow();
        if (selectedRow == -1) return;

        VehicleDialog vehicleDialog = new VehicleDialog(frame);
        vehicleDialog.regNumberInput.setText((String) tableVehicles.getValueAt(selectedRow, 1));
        vehicleDialog.modelInput.setText((String) tableVehicles.getValueAt(selectedRow, 2));
        vehicleDialog.colorInput.setText((String) tableVehicles.getValueAt(selectedRow, 3));
        vehicleDialog.maintenanceDateInput.setText((String) tableVehicles.getValueAt(selectedRow, 4));
        vehicleDialog.ownerIdInput.setText(((Integer) tableVehicles.getValueAt(selectedRow, 5)).toString());
        vehicleDialog.applyButton.addActionListener(e -> {
            try {
                EntityManager em = beginTransaction();
                Vehicle vehicle = em.find(Vehicle.class, (Integer) tableVehicles.getValueAt(selectedRow, 0));
                if (vehicle == null) throw new PersistenceException("Owner not found");
                vehicle.setRegNumber(vehicleDialog.regNumberInput.getText());
                if (!vehicleDialog.modelInput.getText().isEmpty()) vehicle.setModel(vehicleDialog.modelInput.getText());
                if (!vehicleDialog.colorInput.getText().isEmpty()) vehicle.setColor(vehicleDialog.colorInput.getText());
                vehicle.setMaintenanceDate(null);
                if (!vehicleDialog.maintenanceDateInput.getText().isEmpty()) vehicle.setMaintenanceDate(Utilities.parseDate(vehicleDialog.maintenanceDateInput.getText()));



                Owner owner = em.find(Owner.class, Integer.parseInt(vehicleDialog.ownerIdInput.getText()));
                if (owner == null) throw new PersistenceException("Owner not found");
                vehicle.setOwner(owner);
                em.merge(vehicle);
                commitTransaction(em);
                vehicleDialog.dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Something went wrong", "Error", JOptionPane.ERROR_MESSAGE);
                logger.error("Exception ", ex);
            } finally {
                updateTable();
            }
        });
        vehicleDialog.show();
    }

    private void ownerEditingDialog() {
        int selectedRow = tableOwners.getSelectedRow();
        if (selectedRow == -1) {
            logger.debug("Empty selection");
            return;
        }

        OwnerDialog ownerDialog = new OwnerDialog(frame);
        ownerDialog.surnameInput.setText((String) tableOwners.getValueAt(selectedRow, 1));
        ownerDialog.nameInput.setText((String) tableOwners.getValueAt(selectedRow, 2));
        ownerDialog.patronymicInput.setText((String) tableOwners.getValueAt(selectedRow, 3));
        ownerDialog.birthDateInput.setText((String) tableOwners.getValueAt(selectedRow, 4));
        ownerDialog.passportInput.setText((String) tableOwners.getValueAt(selectedRow, 5));
        ownerDialog.licenseInput.setText((String) tableOwners.getValueAt(selectedRow, 6));
        ownerDialog.applyButton.addActionListener(e -> {
            try {
                EntityManager em = beginTransaction();
                Owner owner = em.find(Owner.class, (Integer) tableOwners.getValueAt(selectedRow, 0));
                if (owner == null) throw new PersistenceException("Owner not found");
                owner.setSurname(ownerDialog.surnameInput.getText());
                owner.setName(ownerDialog.nameInput.getText());
                if (!ownerDialog.patronymicInput.getText().isEmpty()) owner.setPatronymic(ownerDialog.patronymicInput.getText());
                owner.setBirthDate(Utilities.parseDate(ownerDialog.birthDateInput.getText()));
                owner.setPassportId(ownerDialog.passportInput.getText());
                owner.setLicenseId(ownerDialog.licenseInput.getText());
                em.merge(owner);
                commitTransaction(em);
                ownerDialog.dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Something went wrong", "Error", JOptionPane.ERROR_MESSAGE);
                logger.error("Exception ", ex);
            } finally {
                updateTable();
            }
        });
        ownerDialog.show();
    }

    private void violationEditingDialog() {

        int selectedRow = tableViolations.getSelectedRow();
        if (selectedRow == -1) {
            logger.debug("Empty selection");
            return;
        }

        ViolationDialog violationDialog = new ViolationDialog(frame);

        violationDialog.penaltyInput.setSelectedItem((String) tableViolations.getValueAt(selectedRow, 1));
        violationDialog.debtInput.setText(((Integer) tableViolations.getValueAt(selectedRow, 2)).toString());
        violationDialog.commentaryInput.setText((String) tableViolations.getValueAt(selectedRow, 3));
        violationDialog.dateInput.setText((String) tableViolations.getValueAt(selectedRow, 4));
        violationDialog.vehicleIdInput.setText(((Integer) tableViolations.getValueAt(selectedRow, 5)).toString());
        violationDialog.applyButton.addActionListener(e -> {
            try {
                EntityManager em = beginTransaction();
                Violation violation = em.find(Violation.class, tableViolations.getValueAt(selectedRow, 0));
                if (violation == null) throw new PersistenceException("Owner not found");
                violation.setPenalty((String) violationDialog.penaltyInput.getSelectedItem());
                if (violationDialog.penaltyInput.getSelectedIndex() == 0) violation.setDebt(Integer.parseInt(violationDialog.debtInput.getText()));
                if (!violationDialog.commentaryInput.getText().isEmpty()) violation.setCommentary(violationDialog.commentaryInput.getText());
                violation.setDate(Utilities.parseDate(violationDialog.dateInput.getText()));

                Vehicle vehicle = em.find(Vehicle.class, Integer.parseInt(violationDialog.vehicleIdInput.getText()));
                if (vehicle == null) throw new PersistenceException("Owner not found");
                violation.setVehicle(vehicle);
                em.persist(violation);
                commitTransaction(em);
                violationDialog.dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Something went wrong", "Error", JOptionPane.ERROR_MESSAGE);
                logger.error("Exception ", ex);
            } finally {
                updateTable();
            }
        });
        violationDialog.show();
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
            violation.setDate(Utilities.parseDate(attrs.getNamedItem("date").getNodeValue()));
            Vehicle vehicle = em.find(Vehicle.class, Integer.parseInt(attrs.getNamedItem("vehicle_id").getNodeValue()));
            violation.setVehicle(vehicle);
            em.persist(violation);
        }
        commitTransaction(em);
        updateTable();
    }

    private void writeXML(String pathToResult) throws ParserConfigurationException, TransformerException, IOException {
        // todo change format

        int[] ids;
        ids = tableViolations.getSelectedRows();
        if (ids.length == 0) {
            ids = new int[tableViolations.getRowCount()];
            for (int i = 0; i < tableViolations.getRowCount(); i++) ids[i] = i;
        }

        writeXMLbyId(pathToResult, ids);
    }

    private void writeXMLbyId(String pathToResult, int[] ids) throws ParserConfigurationException, IOException, TransformerException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.newDocument();
        Node violations = doc.createElement("violations");
        doc.appendChild(violations);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence_unit");
        EntityManager em = emf.createEntityManager();
        for (int id : ids) {
            Violation violation = em.find(Violation.class, tableViolations.getValueAt(id, 0));
            Element violationItem = doc.createElement("violation");
            Element subitem;
            violations.appendChild(violationItem);

            subitem = doc.createElement("id");
            subitem.setTextContent(Integer.toString(violation.getId()));
            violationItem.appendChild(subitem);

            subitem = doc.createElement("penalty");
            subitem.setTextContent(violation.getPenalty());
            violationItem.appendChild(subitem);

            if (violation.getPenalty().equals("Debt")) {
                subitem = doc.createElement("debt");
                subitem.setTextContent(Integer.toString(violation.getDebt()));
                violationItem.appendChild(subitem);
            }

            if (violation.getCommentary() != null) {
                // violationItem.setAttribute("commentary", violation.getCommentary());
                subitem = doc.createElement("commentary");
                subitem.setTextContent(violation.getCommentary());
                violationItem.appendChild(subitem);
            }

            // violationItem.setAttribute("vehicle_id", Integer.toString(violation.getVehicle().getId()));
            subitem = doc.createElement("vehicle_id");
            subitem.setTextContent(Integer.toString(violation.getVehicle().getId()));
            violationItem.appendChild(subitem);

            // violationItem.setAttribute("date", Utilities.dateToString(violation.getDate()));
            subitem = doc.createElement("date");
            subitem.setTextContent(Utilities.dateToString(violation.getDate()));
            violationItem.appendChild(subitem);
        }

        Transformer trans = TransformerFactory.newInstance().newTransformer();
        FileWriter fw = new FileWriter(pathToResult);
        trans.transform(new DOMSource(doc), new StreamResult(fw));
    }

    public void convertToPDF(String pathToSourceXML, String pathToResult) throws IOException, FOPException, TransformerException {

        // todo change xml format
        // the XSL FO file
        File xsltFile = new File("/home/referencecat/IdeaProjects/TrafficPoliceApplication/src/main/resources/template.xsl");
        // the XML file which provides the input
        StreamSource xmlSource = new StreamSource(new File(pathToSourceXML));
        // create an instance of fop factory
        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
        // a user agent is needed for transformation
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        // Setup output
        OutputStream out;
        out = new java.io.FileOutputStream(pathToResult);

        try {
            // Construct fop with desired output format
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

            // Setup XSLT
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xsltFile));

            // Resulting SAX events (the generated FO) must be piped through to
            // FOP
            Result res = new SAXResult(fop.getDefaultHandler());

            // Start XSLT transformation and FOP processing
            // That's where the XML is first transformed to XSL-FO and then
            // PDF is created
            transformer.transform(xmlSource, res);
        } finally {
            out.close();
        }

    }

}

