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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
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

        frame.setVisible(true);

        addButton.addActionListener(event -> onAddButton());
        deleteButton.addActionListener(event -> onDeleteButton());
        editButton.addActionListener(event -> testDialog());

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

                EntityManager em = beginTransaction();
                Owner owner = em.find(Owner.class, Integer.parseInt(vehicleDialog.ownerIdInput.getText()));
                if (owner == null) throw new PersistenceException("Owner not found");
                vehicle.setOwner(owner);
                em.persist(vehicle);
                commitTransaction(em);
                vehicleDialog.dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getStackTrace(), "Error", JOptionPane.ERROR_MESSAGE);
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
                owner.setBirthDate(parseDate(ownerDialog.birthDateInput.getText()));
                owner.setPassportId(ownerDialog.patronymicInput.getText());
                owner.setLicenseId(ownerDialog.licenseInput.getText());

                EntityManager em = beginTransaction();
                em.persist(owner);
                commitTransaction(em);
                ownerDialog.dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getStackTrace(), "Error", JOptionPane.ERROR_MESSAGE);
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
                violation.setDate(parseDate(violationDialog.dateInput.getText()));

                EntityManager em = beginTransaction();
                Vehicle vehicle = em.find(Vehicle.class, Integer.parseInt(violationDialog.vehicleIdInput.getText()));
                if (vehicle == null) throw new PersistenceException("Owner not found");
                violation.setVehicle(vehicle);
                em.persist(violation);
                commitTransaction(em);
                violationDialog.dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getStackTrace(), "Error", JOptionPane.ERROR_MESSAGE);
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
        // todo change format
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

    private void testDialog() {
        ViolationDialog violationDialog = new ViolationDialog(frame);
        violationDialog.applyButton.addActionListener(e -> {
            System.out.println(violationDialog.penaltyInput.getSelectedIndex());
            violationDialog.dialog.dispose();
        });
        violationDialog.show();
    }

    public void convertToPDF() throws IOException, FOPException, TransformerException {

        // todo change xml format
        // the XSL FO file
        File xsltFile = new File("/home/referencecat/IdeaProjects/TrafficPoliceApplication/src/main/resources/template.xsl");
        // the XML file which provides the input
        StreamSource xmlSource = new StreamSource(new File("/home/referencecat/IdeaProjects/TrafficPoliceApplication/xml_io/output.xml"));
        // create an instance of fop factory
        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
        // a user agent is needed for transformation
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        // Setup output
        OutputStream out;
        out = new java.io.FileOutputStream("/home/referencecat/IdeaProjects/TrafficPoliceApplication/xml_io/output.pdf");

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

