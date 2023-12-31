/**
 * Author: Mikhail Buyanov
 * Date: 26/09/2023 01:40
 */
package org.referenceCat;

import org.apache.fop.apps.*;
import org.apache.log4j.Logger;
import org.referenceCat.entities.Officer;
import org.referenceCat.entities.Owner;
import org.referenceCat.entities.Vehicle;
import org.referenceCat.entities.Violation;
import org.referenceCat.exceptions.PDApplicationCustomException;
import org.referenceCat.ui.*;
import org.referenceCat.utils.Utilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.swing.*;
import javax.swing.event.MouseInputListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.lang.Thread.sleep;


public class Application {
    final static Logger logger = Logger.getLogger(Application.class);
    private JFrame frame;
    private JButton addButton, deleteButton, editButton, searchButton, reloadButton, readXMLButton, writeXMLButton, pdfButton;
    private JTextField searchTextField;
    private JToolBar toolBar;
    private JTabbedPane tabs;
    private JTable tableVehicles, tableOwners, tableViolations;
    private DefaultTableModel modelOwners;
    private DefaultTableModel modelViolations;

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
        frame = new JFrame("База данных ГИБДД");
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
        addButton.setToolTipText("Добавить");

        deleteButton = new JButton();
        deleteButton.setToolTipText("Удалить");

        editButton = new JButton();
        editButton.setToolTipText("Редактировать");

        searchButton = new JButton();
        searchButton.setToolTipText("Поиск");

        reloadButton = new JButton();
        reloadButton.setToolTipText("Обновить таблицу");

        readXMLButton = new JButton();
        readXMLButton.setToolTipText("Загрузить нарушения из xml");

        writeXMLButton = new JButton();
        writeXMLButton.setToolTipText("Сохранить выделеные нарушения в xml");

        pdfButton = new JButton();
        pdfButton.setToolTipText("Отчет по нарушения за период");

        searchTextField = new JTextField();
        searchTextField.setPreferredSize(new Dimension(400, 42));
        searchTextField.setMaximumSize(searchTextField.getPreferredSize());
        GhostText ghostText = new GhostText(searchTextField, "Поиск");

        toolBar = new JToolBar("Tool bar");
        toolBar.setPreferredSize(new Dimension(1000, 50));

        toolBar.add(addButton);
        toolBar.add(deleteButton);
        toolBar.add(editButton);
        toolBar.add(reloadButton);
//        toolBar.add(readXMLButton);
//        toolBar.add(writeXMLButton);
        toolBar.add(pdfButton);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(searchTextField);
        toolBar.add(searchButton);
        initButtonIcons();
        frame.add(toolBar, BorderLayout.NORTH);

        deleteButton.setEnabled(false);
        editButton.setEnabled(false);

        logger.info("Tool bar initialized");
    }

    private void initTables() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        JScrollPane scrollVehicles = new JScrollPane();
        JScrollPane scrollViolations = new JScrollPane();
        JScrollPane scrollOwners = new JScrollPane();

        String[] columnsVehicles = {"id", "Рег. номер ТС", "Модель", "Цвет", "Дата последнего ТО", "id в", "Владелец"};
        DefaultTableModel modelVehicles = new DefaultTableModel(columnsVehicles, 100) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        tableVehicles = new JTable(modelVehicles);
        scrollVehicles = new JScrollPane(tableVehicles);

        tableVehicles.getColumnModel().getColumn(0).setMaxWidth(50);
        tableVehicles.getColumnModel().getColumn(0).setMinWidth(50);
        tableVehicles.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tableVehicles.getColumnModel().getColumn(1).setMaxWidth(100);
        tableVehicles.getColumnModel().getColumn(1).setMinWidth(100);
        tableVehicles.getColumnModel().getColumn(5).setMaxWidth(50);
        tableVehicles.getColumnModel().getColumn(5).setMinWidth(50);
        tableVehicles.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);

        String[] columnsOwners = {"id", "Фамилия", "Имя", "Отчество", "Дата рождения", "Номер паспорта", "Номер удостоверения"};
        modelOwners = new DefaultTableModel(columnsOwners, 10);
        tableOwners = new JTable(modelOwners) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        scrollOwners = new JScrollPane(tableOwners);

        tableOwners.getColumnModel().getColumn(0).setMaxWidth(50);
        tableOwners.getColumnModel().getColumn(0).setMinWidth(50);
        tableOwners.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        String[] columnsViolations = {"id", "Тип", "Наказание", "Штраф", "Комментарий", "Дата", "id ТС", "Рег. номер ТС", "id н", "Нарушитель", "id с"};
        modelViolations = new DefaultTableModel(columnsViolations, 12);
        tableViolations = new JTable(modelViolations) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        scrollViolations = new JScrollPane(tableViolations);

        tableViolations.getColumnModel().getColumn(0).setMaxWidth(50);
        tableViolations.getColumnModel().getColumn(0).setMinWidth(50);
        tableViolations.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tableViolations.getColumnModel().getColumn(6).setMaxWidth(50);
        tableViolations.getColumnModel().getColumn(6).setMinWidth(50);
        tableViolations.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
        tableViolations.getColumnModel().getColumn(8).setMaxWidth(50);
        tableViolations.getColumnModel().getColumn(8).setMinWidth(50);
        tableViolations.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);
        tableViolations.getColumnModel().getColumn(10).setMaxWidth(50);
        tableViolations.getColumnModel().getColumn(10).setMinWidth(50);
        tableViolations.getColumnModel().getColumn(10).setCellRenderer(centerRenderer);

        tabs = new JTabbedPane();
        tabs.add("Транспортные средства", scrollVehicles);
        tabs.add("Автовладельцы", scrollOwners);
        tabs.add("Правонарушения", scrollViolations);
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
            } catch (Exception ex) {
                defaultExceptionCatch(ex);
            }
        });

        readXMLButton.addActionListener(event -> {
            try {
                readXML("/home/referencecat/IdeaProjects/TrafficPoliceApplication/xml_io/input.xml");
            } catch (Exception ex) {
                defaultExceptionCatch(ex);
            }
        });

        writeXMLButton.addActionListener(event -> {
            try {
                writeXML("/home/referencecat/IdeaProjects/TrafficPoliceApplication/xml_io/output.xml");
            } catch (Exception ex) {
                defaultExceptionCatch(ex);
            }
        });

        writeXMLButton.setEnabled(false);
        readXMLButton.setEnabled(false);
        tabs.addChangeListener(e -> {
            readXMLButton.setEnabled(tabs.getSelectedIndex() == 2);
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
//        })
        pdfButton.addActionListener(e -> {
            try {
                onReportButton();
            } catch (Exception ex) {
                defaultExceptionCatch(ex);
            }
        });

        tableViolations.addMouseListener(new MouseInputListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onTableSelection();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });

        tableOwners.addMouseListener(new MouseInputListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onTableSelection();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });

        tableVehicles.addMouseListener(new MouseInputListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onTableSelection();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });

        tabs.addChangeListener(e -> onTableSelection());


        logger.info("Listeners initialized");
    }

    private void onTableSelection() {
        logger.debug("onTableSelection");
        if (tabs.getSelectedIndex() == 0 && tableVehicles.getSelectedRow() == -1) {
            deleteButton.setEnabled(false);
            editButton.setEnabled(false);
        } else if (tabs.getSelectedIndex() == 1 && tableOwners.getSelectedRow() == -1) {
            deleteButton.setEnabled(false);
            editButton.setEnabled(false);
        } else if (tabs.getSelectedIndex() == 2 && tableViolations.getSelectedRow() == -1) {
            deleteButton.setEnabled(false);
            editButton.setEnabled(false);
        } else {
            deleteButton.setEnabled(true);
            editButton.setEnabled(true);
        }
    }

    private void makeReport() {
        try {
            writeXML("/home/referencecat/IdeaProjects/TrafficPoliceApplication/xml_io/report_buffer.xml");
            convertToPDF("/home/referencecat/IdeaProjects/TrafficPoliceApplication/xml_io/report_buffer.xml",
                    "/home/referencecat/IdeaProjects/TrafficPoliceApplication/xml_io/report.pdf");
        } catch (Exception ex) {
            defaultExceptionCatch(ex);
        }

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

            Image image6 = ImageIO.read(new File("src/main/resources/ui/load.png")).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            readXMLButton.setIcon(new ImageIcon(image6));

            Image image7 = ImageIO.read(new File("src/main/resources/ui/upload.png")).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            writeXMLButton.setIcon(new ImageIcon(image7));

            Image image8 = ImageIO.read(new File("src/main/resources/ui/pdf-file.png")).getScaledInstance(30, 30, Image.SCALE_DEFAULT);
            pdfButton.setIcon(new ImageIcon(image8));

            logger.info("Button images initialized");
        } catch (Exception e) {
            logger.error("Exception ", e);
        }
    }

    private void search() {
        tableVehicles.setRowHeight(16);
        tableOwners.setRowHeight(16);
        tableViolations.setRowHeight(16);
        if (searchTextField.getText().isEmpty() || searchTextField.getText().equals("Поиск")) return;
        if (tabs.getSelectedIndex() == 0) {

            for (int i = 0; i < tableVehicles.getRowCount(); i++) {
                if (    (tableVehicles.getValueAt(i, 0) == null || !Integer.toString((Integer) tableVehicles.getValueAt(i, 0)).contains(searchTextField.getText())) && (tableVehicles.getValueAt(i, 1) == null || !((String) tableVehicles.getValueAt(i, 1)).contains(searchTextField.getText())) &&
                        (tableVehicles.getValueAt(i, 2) == null || !((String) tableVehicles.getValueAt(i, 2)).contains(searchTextField.getText())) &&
                        (tableVehicles.getValueAt(i, 3) == null || !((String) tableVehicles.getValueAt(i, 3)).contains(searchTextField.getText())) &&
                        (tableVehicles.getValueAt(i, 4) == null || !((String) tableVehicles.getValueAt(i, 4)).contains(searchTextField.getText())) &&
                        (tableVehicles.getValueAt(i, 5) == null || !Integer.toString((Integer) tableVehicles.getValueAt(i, 5)).contains(searchTextField.getText())) &&
                        (tableVehicles.getValueAt(i, 6) == null || !((String) tableVehicles.getValueAt(i, 6)).contains(searchTextField.getText()))) {
                    tableVehicles.setRowHeight(i, 1);
                }
            }
        } else if (tabs.getSelectedIndex() == 1) {
            for (int i = 0; i < tableOwners.getRowCount(); i++) {
                if (    (tableOwners.getValueAt(i, 0) == null || !Integer.toString((Integer) tableOwners.getValueAt(i, 0)).contains(searchTextField.getText())) &&
                        (tableOwners.getValueAt(i, 1) == null || !((String) tableOwners.getValueAt(i, 1)).contains(searchTextField.getText())) &&
                        (tableOwners.getValueAt(i, 2) == null || !((String) tableOwners.getValueAt(i, 2)).contains(searchTextField.getText())) &&
                        (tableOwners.getValueAt(i, 3) == null || !((String) tableOwners.getValueAt(i, 3)).contains(searchTextField.getText())) &&
                        (tableOwners.getValueAt(i, 4) == null || !((String) tableOwners.getValueAt(i, 4)).contains(searchTextField.getText()) )&&
                        (tableOwners.getValueAt(i, 5) == null || !((String) tableOwners.getValueAt(i, 5)).contains(searchTextField.getText())) &&
                        (tableOwners.getValueAt(i, 6) == null || !((String) tableOwners.getValueAt(i, 6)).contains(searchTextField.getText()))) {
                    tableOwners.setRowHeight(i, 1);
                }
            }
        } else {
            for (int i = 0; i < tableViolations.getRowCount(); i++) { // todo (> _ <)
                if (    (tableViolations.getValueAt(i, 0) == null || !Integer.toString((Integer) tableViolations.getValueAt(i, 0)).contains(searchTextField.getText())) &&
                        (tableViolations.getValueAt(i, 1) == null || !((String) tableViolations.getValueAt(i, 1)).contains(searchTextField.getText())) &&
                        (tableViolations.getValueAt(i, 2) == null || !((String) tableViolations.getValueAt(i, 2)).contains(searchTextField.getText())) &&
                        (tableViolations.getValueAt(i, 3) == null || !Integer.toString((Integer) tableViolations.getValueAt(i, 3)).contains(searchTextField.getText())) &&
                        (tableViolations.getValueAt(i, 4) == null || !((String) tableViolations.getValueAt(i, 4)).contains(searchTextField.getText())) &&
                        (tableViolations.getValueAt(i, 5) == null || !((String) tableViolations.getValueAt(i, 5)).contains(searchTextField.getText())) &&
                        (tableViolations.getValueAt(i, 6) == null || !Integer.toString((Integer) tableViolations.getValueAt(i, 6)).contains(searchTextField.getText())) &&
                        (tableViolations.getValueAt(i, 7) == null || !((String) tableViolations.getValueAt(i, 7)).contains(searchTextField.getText())) &&
                        (tableViolations.getValueAt(i, 8) == null || !Integer.toString((Integer) tableViolations.getValueAt(i, 8)).contains(searchTextField.getText())) &&
                        (tableViolations.getValueAt(i, 9) == null || !((String) tableViolations.getValueAt(i, 9)).contains(searchTextField.getText())) &&
                        (tableViolations.getValueAt(i, 10) == null || !Integer.toString((Integer) tableViolations.getValueAt(i, 10)).contains(searchTextField.getText()))) {
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
            model.addRow(new Object[]{vehicle.getId(), vehicle.getRegNumber(), vehicle.getModel(), vehicle.getColor(), Utilities.dateToString(vehicle.getMaintenanceDate(), Utilities.DATE_FORMAT), owner.getId(), owner.getSurname() + " " + owner.getName() + " " + (owner.getPatronymic() != null ? owner.getPatronymic() : "")});
        }

        List<Owner> owners = em.createQuery("SELECT v FROM Owner v").getResultList();

        model = (DefaultTableModel) tableOwners.getModel();
        model.setRowCount(0);
        for (Owner owner : owners) {
            model.addRow(new Object[]{owner.getId(), owner.getSurname(), owner.getName(), owner.getPatronymic(), Utilities.dateToString(owner.getBirthDate(), Utilities.DATE_FORMAT), owner.getPassportId(), owner.getLicenseId()});
        }

        List<Violation> violations = em.createQuery("SELECT v FROM Violation v").getResultList();

        model = (DefaultTableModel) tableViolations.getModel();
        model.setRowCount(0);
        for (Violation violation : violations) {
            Vehicle vehicle = violation.getVehicle();
            Owner owner = violation.getOwner();
            Officer officer = violation.getOfficer();
            model.addRow(new Object[]{violation.getId(), violation.getType(), violation.getPenalty(), violation.getDebt(), violation.getCommentary(), Utilities.dateToString(violation.getDate(), Utilities.DATE_TIME_FORMAT), vehicle.getId(), vehicle.getRegNumber(), owner.getId(), owner.getSurname() + " " + owner.getName() + " " + (owner.getPatronymic() != null ? owner.getPatronymic() : ""), officer == null ? null : officer.getId()});
        }

        tableVehicles.setRowHeight(16);
        tableOwners.setRowHeight(16);
        tableViolations.setRowHeight(16);
        onTableSelection();
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
            EntityManager em = beginTransaction();
            try {
                Vehicle vehicle = new Vehicle();
                vehicle.setRegNumber(vehicleDialog.regNumberInput.getText());
                if (!vehicleDialog.modelInput.getText().isEmpty()) vehicle.setModel(vehicleDialog.modelInput.getText());
                if (!vehicleDialog.colorInput.getText().isEmpty()) vehicle.setColor(vehicleDialog.colorInput.getText());
                if (!vehicleDialog.maintenanceDateInput.getText().isEmpty()) vehicle.setMaintenanceDate(Utilities.parseDate(vehicleDialog.maintenanceDateInput.getText(), Utilities.DATE_FORMAT));

                Owner owner = em.find(Owner.class, Integer.parseInt(vehicleDialog.ownerIdInput.getText()));
                if (owner == null) throw new PersistenceException("Owner not found");
                vehicle.setOwner(owner);
                em.persist(vehicle);
                commitTransaction(em);
                vehicleDialog.dialog.dispose();
            } catch (Exception ex) {
                defaultExceptionCatch(ex);
            } finally {
                em.close();
                updateTable();
            }
        });
        vehicleDialog.show();
    }

    private void ownerAdditionDialog() {
        OwnerDialog ownerDialog = new OwnerDialog(frame);
        ownerDialog.applyButton.addActionListener(e -> {
            EntityManager em = beginTransaction();
            try {
                Owner owner = new Owner();
                owner.setSurname(ownerDialog.surnameInput.getText());
                owner.setName(ownerDialog.nameInput.getText());
                if (!ownerDialog.patronymicInput.getText().isEmpty())
                    owner.setPatronymic(ownerDialog.patronymicInput.getText());
                owner.setBirthDate(Utilities.parseDate(ownerDialog.birthDateInput.getText(), Utilities.DATE_FORMAT));
                owner.setPassportId(ownerDialog.passportInput.getText());
                owner.setLicenseId(ownerDialog.licenseInput.getText());

                em.persist(owner);
                commitTransaction(em);
                ownerDialog.dialog.dispose();
            } catch (Exception ex) {
                defaultExceptionCatch(ex);
            } finally {
                em.close();
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
                violation.setType((String) violationDialog.typeInput.getSelectedItem());
                violation.setPenalty((String) violationDialog.penaltyInput.getSelectedItem());
                if (violationDialog.penaltyInput.getSelectedIndex() == 0)
                    violation.setDebt(Integer.parseInt(violationDialog.debtInput.getText()));
                if (!violationDialog.commentaryInput.getText().isEmpty())
                    violation.setCommentary(violationDialog.commentaryInput.getText());
                violation.setDate(Utilities.parseDate(violationDialog.dateInput.getText(), Utilities.DATE_TIME_FORMAT));

                EntityManager em = beginTransaction();
                Vehicle vehicle = em.find(Vehicle.class, Integer.parseInt(violationDialog.vehicleIdInput.getText()));
                if (vehicle == null) throw new PersistenceException("Vehicle not found");
                violation.setVehicle(vehicle);

                Owner owner = em.find(Owner.class, Integer.parseInt(violationDialog.ownerIdInput.getText()));
                if (owner == null) throw new PersistenceException("Owner not found");
                violation.setOwner(owner);

                if (!violationDialog.officerIdInput.getText().isEmpty()) {
                    Officer officer = em.find(Officer.class, Integer.parseInt(violationDialog.officerIdInput.getText()));
                    if (officer == null) throw new PersistenceException("Officer not found");
                    violation.setOfficer(officer);
                } else violation.setOfficer(null);

                em.persist(violation);
                commitTransaction(em);
                violationDialog.dialog.dispose();
            } catch (Exception ex) {
                defaultExceptionCatch(ex);
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
                Vehicle vehicle = em.find(Vehicle.class, tableVehicles.getValueAt(selectedRow, 0));
                if (vehicle == null) throw new PersistenceException("Owner not found");
                vehicle.setRegNumber(vehicleDialog.regNumberInput.getText());
                if (!vehicleDialog.modelInput.getText().isEmpty()) vehicle.setModel(vehicleDialog.modelInput.getText());
                if (!vehicleDialog.colorInput.getText().isEmpty()) vehicle.setColor(vehicleDialog.colorInput.getText());
                vehicle.setMaintenanceDate(null);
                if (!vehicleDialog.maintenanceDateInput.getText().isEmpty())
                    vehicle.setMaintenanceDate(Utilities.parseDate(vehicleDialog.maintenanceDateInput.getText(), Utilities.DATE_FORMAT));


                Owner owner = em.find(Owner.class, Integer.parseInt(vehicleDialog.ownerIdInput.getText()));
                if (owner == null) throw new PersistenceException("Owner not found");
                vehicle.setOwner(owner);
                em.merge(vehicle);
                commitTransaction(em);
                vehicleDialog.dialog.dispose();
            } catch (Exception ex) {
                defaultExceptionCatch(ex);
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
                Owner owner = em.find(Owner.class, tableOwners.getValueAt(selectedRow, 0));
                if (owner == null) throw new PersistenceException("Owner not found");
                owner.setSurname(ownerDialog.surnameInput.getText());
                owner.setName(ownerDialog.nameInput.getText());
                if (!ownerDialog.patronymicInput.getText().isEmpty())
                    owner.setPatronymic(ownerDialog.patronymicInput.getText());
                owner.setBirthDate(Utilities.parseDate(ownerDialog.birthDateInput.getText(), Utilities.DATE_FORMAT));
                owner.setPassportId(ownerDialog.passportInput.getText());
                owner.setLicenseId(ownerDialog.licenseInput.getText());
                em.merge(owner);
                commitTransaction(em);
                ownerDialog.dialog.dispose();
            } catch (Exception ex) {
                defaultExceptionCatch(ex);
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

        violationDialog.typeInput.setSelectedItem((String) tableViolations.getValueAt(selectedRow, 1));
        violationDialog.penaltyInput.setSelectedItem(tableViolations.getValueAt(selectedRow, 2));
        violationDialog.debtInput.setText(((Integer) tableViolations.getValueAt(selectedRow, 3)).toString());
        violationDialog.commentaryInput.setText((String) tableViolations.getValueAt(selectedRow, 4));
        violationDialog.dateInput.setText((String) tableViolations.getValueAt(selectedRow, 5));
        violationDialog.vehicleIdInput.setText(((Integer) tableViolations.getValueAt(selectedRow, 6)).toString());
        if (tableViolations.getValueAt(selectedRow, 10) != null)
            violationDialog.officerIdInput.setText(((Integer) tableViolations.getValueAt(selectedRow, 10)).toString());
        violationDialog.ownerIdInput.setText(((Integer) tableViolations.getValueAt(selectedRow, 8)).toString());
        violationDialog.applyButton.addActionListener(e -> {
            try {
                EntityManager em = beginTransaction();
                Violation violation = em.find(Violation.class, tableViolations.getValueAt(selectedRow, 0));
                if (violation == null) throw new PersistenceException("Violation not found");
                violation.setType((String) violationDialog.typeInput.getSelectedItem());
                violation.setPenalty((String) violationDialog.penaltyInput.getSelectedItem());
                if (violationDialog.penaltyInput.getSelectedIndex() == 0)
                    violation.setDebt(Integer.parseInt(violationDialog.debtInput.getText()));
                if (!violationDialog.commentaryInput.getText().isEmpty())
                    violation.setCommentary(violationDialog.commentaryInput.getText());
                violation.setDate(Utilities.parseDate(violationDialog.dateInput.getText(), Utilities.DATE_FORMAT));

                Vehicle vehicle = em.find(Vehicle.class, Integer.parseInt(violationDialog.vehicleIdInput.getText()));
                if (vehicle == null) throw new PersistenceException("Vehicle not found");
                violation.setVehicle(vehicle);

                Owner owner = em.find(Owner.class, Integer.parseInt(violationDialog.ownerIdInput.getText()));
                if (owner == null) throw new PersistenceException("Owner not found");
                violation.setOwner(owner);

                if (!violationDialog.officerIdInput.getText().isEmpty()) {
                    Officer officer = em.find(Officer.class, Integer.parseInt(violationDialog.officerIdInput.getText()));
                    if (officer == null) throw new PersistenceException("Officer not found");
                    violation.setOfficer(officer);
                } else violation.setOfficer(null);
                em.persist(violation);
                commitTransaction(em);
                violationDialog.dialog.dispose();
            } catch (Exception ex) {
                defaultExceptionCatch(ex);
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
            if (JOptionPane.YES_NO_OPTION != JOptionPane.showConfirmDialog(frame, "Удалить выделенные записи? \n Соответсвующие нарушения также будут удалены!", "", JOptionPane.YES_NO_OPTION))
                return;

            for (int index : indexes) {

                deleteVehicle((Integer) tableVehicles.getValueAt(index, 0));
            }
        } else if (tabs.getSelectedIndex() == 1) {
            indexes = tableOwners.getSelectedRows();
            if (JOptionPane.YES_NO_OPTION != JOptionPane.showConfirmDialog(frame, "Удалить выделенные записи? \n Соответсвующие ТС и нарушения также будут удалены!", "", JOptionPane.YES_NO_OPTION))
                return;
            for (int index : indexes) {
                deleteOwner((Integer) tableOwners.getValueAt(index, 0));
            }
        } else {
            indexes = tableViolations.getSelectedRows();
            if (JOptionPane.YES_NO_OPTION != JOptionPane.showConfirmDialog(frame, "Удалить выделенные записи?", "", JOptionPane.YES_NO_OPTION))
                return;
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

    private Node findNode(NodeList nodelist, String s) {
        for (int i = 0; i < nodelist.getLength(); i++) {
            if (nodelist.item(i).getNodeName().equals(s)) return nodelist.item(i);
        }
        return null;
    }

    private void readXML(String path) throws PDApplicationCustomException {
        try {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = dBuilder.parse(new File(path));
            doc.getDocumentElement().normalize();

            EntityManager em = beginTransaction();

            NodeList nlViolations = doc.getElementsByTagName("violation");
            for (int i = 0; i < nlViolations.getLength(); i++) {
                Node elem = nlViolations.item(i);
                NodeList subnodes = elem.getChildNodes();
                Violation violation = new Violation();
                violation.setType(findNode(subnodes, "type").getTextContent());
                violation.setPenalty(findNode(subnodes, "penalty").getTextContent());
                if (violation.getPenalty().equals("Штраф"))
                    violation.setDebt(Integer.parseInt(findNode(subnodes, "debt").getTextContent()));
                if (findNode(subnodes, "commentary") != null)
                    violation.setCommentary(findNode(subnodes, "commentary").getTextContent());
                violation.setDate(Utilities.parseDate(findNode(subnodes, "date").getTextContent(), Utilities.DATE_FORMAT));
                Vehicle vehicle = em.find(Vehicle.class, Integer.parseInt(findNode(subnodes, "vehicle_id").getTextContent()));
                violation.setVehicle(vehicle);

                Owner owner = em.find(Owner.class, Integer.parseInt(findNode(subnodes, "owner_id").getTextContent()));
                violation.setOwner(owner);

                Node officerIdnode = findNode(subnodes, "officer_id");

                if (officerIdnode != null) {
                    Officer officer = em.find(Officer.class, Integer.parseInt(officerIdnode.getTextContent()));
                    if (officer != null) violation.setOfficer(officer);
                }

                em.persist(violation);
            }
            commitTransaction(em);
            updateTable();
        } catch (Exception ex) {
            throw new PDApplicationCustomException("readXML exception");
        }
    }

    private void writeXML(String pathToResult) throws PDApplicationCustomException {
        int[] ids;
        ids = tableViolations.getSelectedRows();
        if (ids.length == 0) {
            ids = new int[tableViolations.getRowCount()];
            for (int i = 0; i < tableViolations.getRowCount(); i++) ids[i] = (int) tableViolations.getValueAt(i, 0);;
        }
        for (int i = 0; i < ids.length; i++) {
            ids[i] = (int) tableViolations.getValueAt(ids[i], 0);
        }

        writeXMLbyId(pathToResult, ids, true);
    }

    private void writeXMLbyId(String pathToResult, int[] ids, boolean cyrAllowed) throws PDApplicationCustomException {
        writeXMLbyId(pathToResult, ids,  cyrAllowed, "", "");
    }

    private void writeXMLbyId(String pathToResult, int[] ids, boolean cyrAllowed, String from, String to) throws PDApplicationCustomException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.newDocument();

            Node violations = doc.createElement("violations");
            doc.appendChild(violations);

            Element fromN = doc.createElement("from");
            fromN.setTextContent(from);
            violations.appendChild(fromN);
            Element toN = doc.createElement("to");
            toN.setTextContent(to);
            violations.appendChild(toN);

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence_unit");
            EntityManager em = emf.createEntityManager();
            for (int id : ids) {
                Violation violation = em.find(Violation.class, id);
                Element violationItem = doc.createElement("violation");
                Element subitem;
                violations.appendChild(violationItem);

                subitem = doc.createElement("id");
                subitem.setTextContent(Integer.toString(violation.getId()));
                violationItem.appendChild(subitem);

                subitem = doc.createElement("type");
                subitem.setTextContent(cyrAllowed ? violation.getType() : Utilities.convertCyrilic(violation.getType()));
                violationItem.appendChild(subitem);

                subitem = doc.createElement("penalty");
                subitem.setTextContent(cyrAllowed ? violation.getPenalty() : Utilities.convertCyrilic(violation.getPenalty()));
                violationItem.appendChild(subitem);

                if (violation.getPenalty().equals("Штраф")) {
                    subitem = doc.createElement("debt");
                    subitem.setTextContent(Integer.toString(violation.getDebt()));
                    violationItem.appendChild(subitem);
                }

                if (violation.getCommentary() != null) {
                    // violationItem.setAttribute("commentary", violation.getCommentary());
                    subitem = doc.createElement("commentary");
                    subitem.setTextContent(cyrAllowed ? violation.getCommentary() : Utilities.convertCyrilic(violation.getCommentary()));
                    violationItem.appendChild(subitem);
                }

                // violationItem.setAttribute("vehicle_id", Integer.toString(violation.getVehicle().getId()));
                subitem = doc.createElement("vehicle_id");
                subitem.setTextContent(Integer.toString(violation.getVehicle().getId()));
                violationItem.appendChild(subitem);

                subitem = doc.createElement("owner_id");
                subitem.setTextContent(Integer.toString(violation.getOwner().getId()));
                violationItem.appendChild(subitem);

                // violationItem.setAttribute("date", Utilities.dateToString(violation.getDate()));
                subitem = doc.createElement("date");
                subitem.setTextContent(Utilities.dateToString(violation.getDate(), Utilities.DATE_FORMAT));
                violationItem.appendChild(subitem);

                if (violation.getOfficer() != null) {
                    subitem = doc.createElement("officer_id");
                    subitem.setTextContent(Integer.toString(violation.getOfficer().getId()));
                    violationItem.appendChild(subitem);
                }
            }

            Transformer trans = TransformerFactory.newInstance().newTransformer();
            FileWriter fw = new FileWriter(pathToResult);
            trans.transform(new DOMSource(doc), new StreamResult(fw));

        } catch (Exception ex) {
            logger.error(ex);
            throw new PDApplicationCustomException("writeXML exception");
        }
    }

    public void convertToPDF(String pathToSourceXML, String pathToResult) throws PDApplicationCustomException, IOException {
        OutputStream out = null;
        try {
            // the XSL FO file
            File xsltFile = new File("/home/referencecat/IdeaProjects/TrafficPoliceApplication/src/main/resources/template.xsl");
            // the XML file which provides the input
            StreamSource xmlSource = new StreamSource(new File(pathToSourceXML));
            // create an instance of fop factory
            FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
            // a user agent is needed for transformation
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            // Setup output



            out = new java.io.FileOutputStream(pathToResult);


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
        } catch (Exception ex) {
            logger.error(ex);
            throw  new PDApplicationCustomException("pdf exception");
        } finally {
            out.close();
        }
    }

    private void onReportButton() {
        ReportDialog reportDialog = new ReportDialog(frame);
        reportDialog.dateInput1.setText(Utilities.dateToString(Calendar.getInstance().getTime(), Utilities.DATE_FORMAT));
        reportDialog.dateInput2.setText(Utilities.dateToString(Calendar.getInstance().getTime(), Utilities.DATE_FORMAT));
        reportDialog.applyButton.addActionListener(e -> {
            try {
                Object mutex = new Object();
                Thread threadWriteToBuffer = new Thread(() -> {
                    synchronized (mutex) {
                        System.out.println("thread 1 starts");
                        logger.debug("thread 1 starts");
                        try {
                            sleep(100);
                            Date from = Utilities.parseDate(reportDialog.dateInput1.getText(), Utilities.DATE_FORMAT);
                            Date to = Utilities.parseDate(reportDialog.dateInput2.getText(), Utilities.DATE_FORMAT);
                            Date date;

                            ArrayList<Integer> ids = new ArrayList<>();
                            for (int i = 0; i < tableViolations.getRowCount(); i++) {
                                date = Utilities.parseDate((String) tableViolations.getValueAt(i, 5), Utilities.DATE_FORMAT);
                                if (!date.before(from) && !date.after(to))
                                    ids.add((Integer) tableViolations.getValueAt(i, 0));
                            }
                            int[] arr = ids.stream().mapToInt(i -> i).toArray();
                            writeXMLbyId("/home/referencecat/IdeaProjects/TrafficPoliceApplication/xml_io/report_buffer.xml", arr, false, reportDialog.dateInput1.getText(), reportDialog.dateInput2.getText());
                            mutex.notifyAll();
                        } catch (Exception ex) {
                            defaultExceptionCatch(ex);
                        }
                        System.out.println("thread 1 ends");
                        logger.debug("thread 1 ends");
                    }
                });

                Thread threadWriteToPdf = new Thread(() -> {
                    synchronized (mutex) {
                        try {
                            mutex.wait();
                        } catch (InterruptedException ignored) {
                        }
                        System.out.println("thread 2 starts");
                        logger.debug("thread 2 starts");
                        try {
                            sleep(1000);
                            convertToPDF("/home/referencecat/IdeaProjects/TrafficPoliceApplication/xml_io/report_buffer.xml",
                                    "/home/referencecat/IdeaProjects/TrafficPoliceApplication/xml_io/report.pdf");
                        } catch (Exception ex) {
                            defaultExceptionCatch(ex);
                        }
                        System.out.println("thread 2 ends");
                        logger.debug("thread 2 ends");
                    }

                });

                threadWriteToPdf.start();
                threadWriteToBuffer.start();

                reportDialog.dialog.dispose();
            } catch (Exception ex) {
                defaultExceptionCatch(ex);
            } finally {
                updateTable();
            }
        });
        reportDialog.show();
    }

    private String throwableDescription(Throwable ex) {
        if (ex.getClass() == PersistenceException.class) {
            if (ex.getMessage().contains("Violation not found")) return "Правонарушение не найдено";
            if (ex.getMessage().contains("Vehicle not found")) return "ТС не найдено";
            if (ex.getMessage().contains("Owner not found")) return "Автовладелец не найден";
            if (ex.getMessage().contains("Officer not found")) return "Сотрудник не найден";
            if (ex.getMessage().contains("could not execute statement")) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                ex.printStackTrace(pw);
                if (sw.toString().contains("for key 'persons.passport_id_UNIQUE'")) return "Номер паспорта не уникален";
                if (sw.toString().contains("for key 'persons.license_id_UNIQUE'")) return "Номер лицензии не уникален";
                if (sw.toString().contains("for key 'vehicles.reg_number_UNIQUE'")) return "Регистрационный номер ТС не уникален";
            }
        }
        if (ex.getClass() == ParseException.class) return "Неверный формат данных";
        if (ex.getClass() == PDApplicationCustomException.class) {
            if (ex.getMessage().contains("readXML")) return "Некоректные данные xml файла или он не найден";
            if (ex.getMessage().contains("writeXML")) return "Не возмжна запись xml файла или он не найден";
            if (ex.getMessage().contains("pdf")) return "Ошибка при составлении отчета";
            return "Ошибка валидации";
        }
        return ex.getMessage();
    }

    private void defaultExceptionCatch(Throwable ex) {
        String description = throwableDescription(ex);
        if (description == null || description.isEmpty())
            JOptionPane.showMessageDialog(frame, "Ошибка: " + ex.getClass().getName(), "Error", JOptionPane.ERROR_MESSAGE);
        JOptionPane.showMessageDialog(frame, "Ошибка: " + ex.getClass().getName() + "\n" + description, "Error", JOptionPane.ERROR_MESSAGE);
        logger.error("Exception ", ex);
    }
}

