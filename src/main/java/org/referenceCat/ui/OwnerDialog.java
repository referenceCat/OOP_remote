package org.referenceCat.ui;

import org.referenceCat.utils.Utilities;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.Date;

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
        patronymicInput = new JTextField(40);
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
        surnameInput.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                onTextUpdate();
            }

            public void removeUpdate(DocumentEvent e) {
                onTextUpdate();
            }

            public void insertUpdate(DocumentEvent e) {
                onTextUpdate();
            }
        });

        panel.add(new JLabel("Name: *"));
        panel.add(nameInput);
        panel.add(nameInputLabel);
        nameInput.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                onTextUpdate();
            }

            public void removeUpdate(DocumentEvent e) {
                onTextUpdate();
            }

            public void insertUpdate(DocumentEvent e) {
                onTextUpdate();
            }
        });


        panel.add(new JLabel("Patronymic:"));
        panel.add(patronymicInput);
        panel.add(patronymicInputLabel);
        patronymicInput.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                onTextUpdate();
            }

            public void removeUpdate(DocumentEvent e) {
                onTextUpdate();
            }

            public void insertUpdate(DocumentEvent e) {
                onTextUpdate();
            }
        });


        panel.add(new JLabel("Birth date: *"));
        panel.add(birthDateInput);
        panel.add(birthDateInputLabel);
        birthDateInput.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                onTextUpdate();
            }

            public void removeUpdate(DocumentEvent e) {
                onTextUpdate();
            }

            public void insertUpdate(DocumentEvent e) {
                onTextUpdate();
            }
        });


        panel.add(new JLabel("Passport number: *"));
        panel.add(passportInput);
        panel.add(passportInputLabel);
        passportInput.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                onTextUpdate();
            }

            public void removeUpdate(DocumentEvent e) {
                onTextUpdate();
            }

            public void insertUpdate(DocumentEvent e) {
                onTextUpdate();
            }
        });


        panel.add(new JLabel("License number: *"));
        panel.add(licenseInput);
        panel.add(licenseInputLabel);
        licenseInput.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                onTextUpdate();
            }

            public void removeUpdate(DocumentEvent e) {
                onTextUpdate();
            }

            public void insertUpdate(DocumentEvent e) {
                onTextUpdate();
            }
        });


        panel.add(applyButton);
        applyButton.setEnabled(false);

        dialog.add(panel);
        dialog.setSize(455, 570);
        onTextUpdate();

    }

    private void onTextUpdate() {
        boolean valid = true;
        Utilities.ValidationResponse validationResponse;

        validationResponse = Utilities.requiredFieldCheck(surnameInput.getText());
        valid &= validationResponse.isValid;
        surnameInputLabel.setText(validationResponse.message);

        validationResponse = Utilities.requiredFieldCheck(nameInput.getText());
        valid &= validationResponse.isValid;
        nameInputLabel.setText(validationResponse.message);

        validationResponse = Utilities.requiredFieldCheck(passportInput.getText());
        passportInputLabel.setText(validationResponse.message);
        valid &= validationResponse.isValid;
        if (validationResponse.isValid) {
            validationResponse = Utilities.passportValidation(passportInput.getText());
            valid &= validationResponse.isValid;
            passportInputLabel.setText(validationResponse.message);
        }

        validationResponse = Utilities.requiredFieldCheck(licenseInput.getText());
        licenseInputLabel.setText(validationResponse.message);
        valid &= validationResponse.isValid;
        if (validationResponse.isValid) {
            validationResponse = Utilities.licenseValidation(licenseInput.getText());
            valid &= validationResponse.isValid;
            licenseInputLabel.setText(validationResponse.message);
        }

        validationResponse = Utilities.requiredFieldCheck(birthDateInput.getText());
        birthDateInputLabel.setText(validationResponse.message);
        valid &= validationResponse.isValid;
        if (validationResponse.isValid) {
            validationResponse = Utilities.dateValidation(birthDateInput.getText(), Utilities.DATE_FORMAT);
            valid &= validationResponse.isValid;
            birthDateInputLabel.setText(validationResponse.message);
        }

        applyButton.setEnabled(valid);
    }

    public void show() {
        dialog.setVisible(true);
    }
}
