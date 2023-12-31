package org.referenceCat.ui;

import org.referenceCat.utils.Utilities;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class VehicleDialog {
    public JDialog dialog;
    public JPanel panel;
    public JButton applyButton;

    public JTextField regNumberInput;
    public JTextField modelInput;
    public JTextField colorInput;
    public JTextField maintenanceDateInput;
    public JTextField ownerIdInput;
    public JLabel regNumberInputLabel;
    public JLabel modelInputLabel;
    public JLabel colorInputLabel;
    public JLabel maintenanceDateInputLabel;
    public JLabel ownerIdInputLabel;


    public VehicleDialog(JFrame frame) {
        VerticalLayout layout = new VerticalLayout();
        panel = new JPanel(layout);
        dialog = new JDialog(frame);

        regNumberInput = new JTextField(40);
        modelInput = new JTextField(40);
        colorInput = new JTextField(40);
        maintenanceDateInput = new JTextField(40);
        ownerIdInput = new JTextField(40);
        applyButton = new JButton("Apply");

        regNumberInputLabel = new JLabel(" ");
        modelInputLabel = new JLabel(" ");
        colorInputLabel = new JLabel(" ");
        maintenanceDateInputLabel = new JLabel(" ");
        ownerIdInputLabel = new JLabel(" ");

        regNumberInputLabel.setForeground(Color.RED);
        modelInputLabel.setForeground(Color.RED);
        colorInputLabel.setForeground(Color.RED);
        maintenanceDateInputLabel.setForeground(Color.RED);
        ownerIdInputLabel.setForeground(Color.RED);

        regNumberInputLabel.setFont(new Font("Times New Roman", Font.ITALIC, 12));
        modelInputLabel.setFont(new Font("Times New Roman", Font.ITALIC, 12));
        colorInputLabel.setFont(new Font("Times New Roman", Font.ITALIC, 12));
        maintenanceDateInputLabel.setFont(new Font("Times New Roman", Font.ITALIC, 12));
        ownerIdInputLabel.setFont(new Font("Times New Roman", Font.ITALIC, 12));

        applyButton = new JButton("Apply");

        panel.add(new JLabel("Регистрационный номер: *"));
        panel.add(regNumberInput);
        panel.add(regNumberInputLabel);
        regNumberInput.getDocument().addDocumentListener(new DocumentListener() {
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

        panel.add(new JLabel("Модель ТС:"));
        panel.add(modelInput);
        panel.add(modelInputLabel);
        modelInput.getDocument().addDocumentListener(new DocumentListener() {
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


        panel.add(new JLabel("Цвет:"));
        panel.add(colorInput);
        panel.add(colorInputLabel);
        colorInput.getDocument().addDocumentListener(new DocumentListener() {
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

        panel.add(new JLabel("Дата последнего ТО:"));
        panel.add(maintenanceDateInput);
        panel.add(maintenanceDateInputLabel);
        maintenanceDateInput.getDocument().addDocumentListener(new DocumentListener() {
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


        panel.add(new JLabel("id владельца: *"));
        panel.add(ownerIdInput);
        panel.add(ownerIdInputLabel);
        ownerIdInput.getDocument().addDocumentListener(new DocumentListener() {
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
        dialog.setSize(455, 395);
        onTextUpdate();
    }

    private void onTextUpdate() {
        boolean valid = true;
        Utilities.ValidationResponse validationResponse;

        validationResponse = Utilities.requiredFieldCheck(regNumberInput.getText());
        regNumberInputLabel.setText(validationResponse.message);
        valid &= validationResponse.isValid;
        if (validationResponse.isValid) {
            validationResponse = Utilities.regNumberValidation(regNumberInput.getText());
            valid &= validationResponse.isValid;
            regNumberInputLabel.setText(validationResponse.message);
        }

        if (!maintenanceDateInput.getText().isEmpty()) {
            validationResponse = Utilities.dateValidation(maintenanceDateInput.getText(), Utilities.DATE_FORMAT);
            valid &= validationResponse.isValid;
            maintenanceDateInputLabel.setText(validationResponse.message);
        }

        validationResponse = Utilities.requiredFieldCheck(ownerIdInput.getText());
        ownerIdInputLabel.setText(validationResponse.message);
        valid &= validationResponse.isValid;
        if (validationResponse.isValid && !Utilities.isInteger(ownerIdInput.getText())) {
            valid = false;
            ownerIdInputLabel.setText("Must be Integer");
        }

        applyButton.setEnabled(valid);
    }

    public void show() {
        dialog.setVisible(true);
    }
}
