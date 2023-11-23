package org.referenceCat.ui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class OwnerDialog {
    public JDialog dialog;
    public JPanel panel;
    public JButton applyButton;

    JTextField surnameInput;
    JTextField nameInput;
    JTextField patronymicInput;
    JTextField birthDateInput;
    JTextField passportInput;
    JTextField licenseInput;
    JLabel surnameInputLabel;
    JLabel nameInputLabel;
    JLabel patronymicInputLabel;
    JLabel birthDateInputLabel;
    JLabel passportInputLabel;
    JLabel licenseInputLabel;


    public OwnerDialog(JFrame frame) {
        VerticalLayout layout = new VerticalLayout();
        panel = new JPanel(layout);
        dialog = new JDialog(frame);

        surnameInput = new JTextField(40);
        nameInput = new JTextField(40);
        patronymicInput= new JTextField(40);
        birthDateInput = new JTextField(40);
        passportInput = new JTextField(40);
        licenseInput = new JTextField(40);
        applyButton = new JButton("Apply");

        surnameInputLabel = new JLabel(" ");
        nameInputLabel = new JLabel(" ");
        patronymicInputLabel = new JLabel(" ");
        birthDateInputLabel = new JLabel(" ");
        passportInputLabel = new JLabel(" ");
        licenseInputLabel = new JLabel(" ");

        applyButton = new JButton("Apply");

        panel.add(new JLabel("Surname: *"));
        panel.add(surnameInput);
        panel.add(surnameInputLabel);
        surnameInput.getDocument().addDocumentListener(new DocumentListener() {public void changedUpdate(DocumentEvent e) {textUpdate();} public void removeUpdate(DocumentEvent e) {textUpdate();} public void insertUpdate(DocumentEvent e) {textUpdate();}});

        panel.add(new JLabel("Name: *"));
        panel.add(nameInput);
        panel.add(nameInputLabel);
        nameInput.getDocument().addDocumentListener(new DocumentListener() {public void changedUpdate(DocumentEvent e) {textUpdate();} public void removeUpdate(DocumentEvent e) {textUpdate();} public void insertUpdate(DocumentEvent e) {textUpdate();}});


        panel.add(new JLabel("Patronymic:"));
        panel.add(patronymicInput);
        panel.add(patronymicInputLabel);
        patronymicInput.getDocument().addDocumentListener(new DocumentListener() {public void changedUpdate(DocumentEvent e) {textUpdate();} public void removeUpdate(DocumentEvent e) {textUpdate();} public void insertUpdate(DocumentEvent e) {textUpdate();}});


        panel.add(new JLabel("Birth date: *"));
        panel.add(birthDateInput);
        panel.add(birthDateInputLabel);
        birthDateInput.getDocument().addDocumentListener(new DocumentListener() {public void changedUpdate(DocumentEvent e) {textUpdate();} public void removeUpdate(DocumentEvent e) {textUpdate();} public void insertUpdate(DocumentEvent e) {textUpdate();}});


        panel.add(new JLabel("Passport number: *"));
        panel.add(passportInput);
        panel.add(passportInputLabel);
        passportInput.getDocument().addDocumentListener(new DocumentListener() {public void changedUpdate(DocumentEvent e) {textUpdate();} public void removeUpdate(DocumentEvent e) {textUpdate();} public void insertUpdate(DocumentEvent e) {textUpdate();}});


        panel.add(new JLabel("License number: *"));
        panel.add(licenseInput);
        panel.add(licenseInputLabel);
        licenseInput.getDocument().addDocumentListener(new DocumentListener() {public void changedUpdate(DocumentEvent e) {textUpdate();} public void removeUpdate(DocumentEvent e) {textUpdate();} public void insertUpdate(DocumentEvent e) {textUpdate();}});


        panel.add(applyButton);
        applyButton.setEnabled(false);

        dialog.add(panel);
        dialog.setSize(455, 460);
    }

    private void textUpdate() {
        System.out.println("text");
    }

    public void show() {
        dialog.setVisible(true);
    }
}
