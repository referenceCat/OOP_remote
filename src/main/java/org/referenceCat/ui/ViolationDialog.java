package org.referenceCat.ui;

import org.referenceCat.utils.Utilities;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class ViolationDialog {
    public JDialog dialog;
    public JPanel panel;
    public JButton applyButton;
    public JComboBox penaltyInput;
    public JTextField debtInput;
    public JTextField commentaryInput;
    public JTextField dateInput;
    public JTextField vehicleIdInput;
    public JLabel debtInputLabel;
    public JLabel commentaryInputLabel;
    public JLabel dateInputLabel;
    public JLabel vehicleIdInputLabel;


    public ViolationDialog(JFrame frame) {
        VerticalLayout layout = new VerticalLayout();
        panel = new JPanel(layout);
        dialog = new JDialog(frame);

        String[] options = {"Debt", "Jail (20 years)", "Warning", "Deprivation of license"};
        penaltyInput = new JComboBox(options);
        panel.add(new JLabel("Penalty: "));
        panel.add(penaltyInput);
        panel.add(new JLabel(" "));


        debtInput = new JTextField(40);
        commentaryInput = new JTextField(40);
        dateInput = new JTextField(40);
        vehicleIdInput = new JTextField(40);
        applyButton = new JButton("Apply");

        debtInputLabel = new JLabel(" ");
        commentaryInputLabel = new JLabel(" ");
        dateInputLabel = new JLabel(" ");
        vehicleIdInputLabel = new JLabel(" ");

        debtInputLabel.setForeground(Color.RED);
        commentaryInputLabel.setForeground(Color.RED);
        dateInputLabel.setForeground(Color.RED);
        vehicleIdInputLabel.setForeground(Color.RED);

        debtInputLabel.setFont(new Font("Times New Roman", Font.ITALIC, 12));
        commentaryInputLabel.setFont(new Font("Times New Roman", Font.ITALIC, 12));
        dateInputLabel.setFont(new Font("Times New Roman", Font.ITALIC, 12));
        vehicleIdInputLabel.setFont(new Font("Times New Roman", Font.ITALIC, 12));

        applyButton = new JButton("Apply");

        penaltyInput.addActionListener(e -> onTextUpdate());

        panel.add(new JLabel("Debt:"));
        panel.add(debtInput);
        panel.add(debtInputLabel);
        debtInput.getDocument().addDocumentListener(new DocumentListener() {
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

        panel.add(new JLabel("Commentary:"));
        panel.add(commentaryInput);
        panel.add(commentaryInputLabel);
        commentaryInput.getDocument().addDocumentListener(new DocumentListener() {
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


        panel.add(new JLabel("Date:"));
        panel.add(dateInput);
        panel.add(dateInputLabel);
        dateInput.getDocument().addDocumentListener(new DocumentListener() {
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


        panel.add(new JLabel("Vehicle id: *"));
        panel.add(vehicleIdInput);
        panel.add(vehicleIdInputLabel);
        vehicleIdInput.getDocument().addDocumentListener(new DocumentListener() {
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
        dialog.setSize(455, 410);
        onTextUpdate();
    }

    private void onTextUpdate() {
        debtInput.setEnabled(penaltyInput.getSelectedIndex() == 0);
        boolean valid = true;
        Utilities.ValidationResponse validationResponse;

        applyButton.setEnabled(valid);

        if ((penaltyInput.getSelectedIndex() == 0) && debtInput.getText().isEmpty()) {
            valid = false;
            debtInputLabel.setText("Required field if penalty is debt");
        } else if (!Utilities.isInteger(debtInput.getText())) {
            valid = false;
            debtInputLabel.setText("Must be integer");
        } else {
            debtInputLabel.setText(" ");
        }

        validationResponse = Utilities.requiredFieldCheck(dateInput.getText());
        dateInputLabel.setText(validationResponse.message);
        valid &= validationResponse.isValid;
        if (validationResponse.isValid) {
            validationResponse = Utilities.dateValidation(dateInput.getText());
            valid &= validationResponse.isValid;
            dateInputLabel.setText(validationResponse.message);
        }

        validationResponse = Utilities.requiredFieldCheck(vehicleIdInput.getText());
        vehicleIdInputLabel.setText(validationResponse.message);
        valid &= validationResponse.isValid;
        if (validationResponse.isValid && !Utilities.isInteger(vehicleIdInput.getText())) {
            valid = false;
            vehicleIdInputLabel.setText("Must be Integer");
        }


        applyButton.setEnabled(valid);
    }

    public void show() {
        dialog.setVisible(true);
    }
}
