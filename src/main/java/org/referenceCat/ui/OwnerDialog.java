package org.referenceCat.ui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class OwnerDialog {
    public JDialog dialog;
    public JPanel panel;
    public JButton applyButton;

    public JTextField surnameInput;
    public JTextField nameInput;
    public JTextField patronymicInput;
    public JTextField birthDateInput;
    public JTextField passportInput;
    public JTextField licenseInput;
    public JLabel surnameInputLabel;
    public JLabel nameInputLabel;
    public JLabel patronymicInputLabel;
    public JLabel birthDateInputLabel;
    public JLabel passportInputLabel;
    public JLabel licenseInputLabel;


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

        surnameInputLabel.setForeground(Color.RED);
        nameInputLabel.setForeground(Color.RED);
        patronymicInputLabel.setForeground(Color.RED);
        birthDateInputLabel.setForeground(Color.RED);
        passportInputLabel.setForeground(Color.RED);
        licenseInputLabel.setForeground(Color.RED);

        surnameInputLabel.setFont(new Font("Times New Roman", Font.ITALIC, 12));
        nameInputLabel.setFont(new Font("Times New Roman", Font.ITALIC, 12));
        patronymicInputLabel.setFont(new Font("Times New Roman", Font.ITALIC, 12));
        birthDateInputLabel.setFont(new Font("Times New Roman", Font.ITALIC, 12));
        passportInputLabel.setFont(new Font("Times New Roman", Font.ITALIC, 12));
        licenseInputLabel.setFont(new Font("Times New Roman", Font.ITALIC, 12));

        applyButton = new JButton("Apply");

        panel.add(new JLabel("Surname: *"));
        panel.add(surnameInput);
        panel.add(surnameInputLabel);
        surnameInput.getDocument().addDocumentListener(new DocumentListener() {public void changedUpdate(DocumentEvent e) {
            onTextUpdate();} public void removeUpdate(DocumentEvent e) {
            onTextUpdate();} public void insertUpdate(DocumentEvent e) {
            onTextUpdate();}});

        panel.add(new JLabel("Name: *"));
        panel.add(nameInput);
        panel.add(nameInputLabel);
        nameInput.getDocument().addDocumentListener(new DocumentListener() {public void changedUpdate(DocumentEvent e) {
            onTextUpdate();} public void removeUpdate(DocumentEvent e) {
            onTextUpdate();} public void insertUpdate(DocumentEvent e) {
            onTextUpdate();}});


        panel.add(new JLabel("Patronymic:"));
        panel.add(patronymicInput);
        panel.add(patronymicInputLabel);
        patronymicInput.getDocument().addDocumentListener(new DocumentListener() {public void changedUpdate(DocumentEvent e) {
            onTextUpdate();} public void removeUpdate(DocumentEvent e) {
            onTextUpdate();} public void insertUpdate(DocumentEvent e) {
            onTextUpdate();}});


        panel.add(new JLabel("Birth date: *"));
        panel.add(birthDateInput);
        panel.add(birthDateInputLabel);
        birthDateInput.getDocument().addDocumentListener(new DocumentListener() {public void changedUpdate(DocumentEvent e) {
            onTextUpdate();} public void removeUpdate(DocumentEvent e) {
            onTextUpdate();} public void insertUpdate(DocumentEvent e) {
            onTextUpdate();}});


        panel.add(new JLabel("Passport number: *"));
        panel.add(passportInput);
        panel.add(passportInputLabel);
        passportInput.getDocument().addDocumentListener(new DocumentListener() {public void changedUpdate(DocumentEvent e) {
            onTextUpdate();} public void removeUpdate(DocumentEvent e) {
            onTextUpdate();} public void insertUpdate(DocumentEvent e) {
            onTextUpdate();}});


        panel.add(new JLabel("License number: *"));
        panel.add(licenseInput);
        panel.add(licenseInputLabel);
        licenseInput.getDocument().addDocumentListener(new DocumentListener() {public void changedUpdate(DocumentEvent e) {
            onTextUpdate();} public void removeUpdate(DocumentEvent e) {
            onTextUpdate();} public void insertUpdate(DocumentEvent e) {
            onTextUpdate();}});


        panel.add(applyButton);
        applyButton.setEnabled(false);

        dialog.add(panel);
        dialog.setSize(455, 460);
        onTextUpdate();
    }

    private void onTextUpdate() {
        boolean valid = true;

        if (surnameInput.getText().isEmpty()) {
            valid = false;
            surnameInputLabel.setText("Required field ");
        } else if (surnameInputLabel.getText().length() > 200) {
            valid = false;
            surnameInputLabel.setText("Too big ");
        } else {
            surnameInputLabel.setText(" ");
        }

        if (nameInput.getText().isEmpty()) {
            valid = false;
            nameInputLabel.setText("Required field ");
        } else if (nameInput.getText().length() > 200) {
            valid = false;
            nameInputLabel.setText("Too big ");
        } else {
            nameInputLabel.setText(" ");
        }

        if (patronymicInput.getText().length() > 200) {
            valid = false;
            patronymicInputLabel.setText("Too big ");
        } else {
            patronymicInputLabel.setText(" ");
        }

        if (passportInput.getText().isEmpty()) {
            valid = false;
            passportInputLabel.setText("Required field ");
        } else {
            passportInputLabel.setText(" ");
        }

        applyButton.setEnabled(valid);
        // todo other validations
    }

    public void show() {
        dialog.setVisible(true);
    }
}
