package org.referenceCat.ui;

import org.referenceCat.utils.Utilities;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class ReportDialog {
    public JDialog dialog;
    public JPanel panel;
    public JButton applyButton;
    public JTextField dateInput1;
    public JTextField dateInput2;
    public JLabel dateInputLabel1;
    public JLabel dateInputLabel2;

    public ReportDialog(JFrame frame) {
        VerticalLayout layout = new VerticalLayout();
        panel = new JPanel(layout);
        dialog = new JDialog(frame);

        dateInput1 = new JTextField(40);
        dateInputLabel1 = new JLabel(" ");
        dateInputLabel1.setForeground(Color.RED);
        dateInputLabel1.setFont(new Font("Times New Roman", Font.ITALIC, 12));
        panel.add(new JLabel("От:"));
        panel.add(dateInput1);
        panel.add(dateInputLabel1);
        dateInput1.getDocument().addDocumentListener(new DocumentListener() {
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

        dateInput2 = new JTextField(40);
        dateInputLabel2 = new JLabel(" ");
        dateInputLabel2.setForeground(Color.RED);
        dateInputLabel2.setFont(new Font("Times New Roman", Font.ITALIC, 12));
        panel.add(new JLabel("До:"));
        panel.add(dateInput2);
        panel.add(dateInputLabel2);
        dateInput2.getDocument().addDocumentListener(new DocumentListener() {
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

        applyButton = new JButton("Apply");
        panel.add(applyButton);
        applyButton.setEnabled(false);
        dialog.add(panel);
        dialog.setSize(455, 210);
        onTextUpdate();
    }

    private void onTextUpdate() {
        boolean valid = true;
        Utilities.ValidationResponse validationResponse;

        validationResponse = Utilities.requiredFieldCheck(dateInput1.getText());
        dateInputLabel1.setText(validationResponse.message);
        valid &= validationResponse.isValid;
        if (validationResponse.isValid) {
            validationResponse = Utilities.dateValidation(dateInput1.getText(), Utilities.DATE_FORMAT);
            valid &= validationResponse.isValid;
            dateInputLabel1.setText(validationResponse.message);
        }

        validationResponse = Utilities.requiredFieldCheck(dateInput2.getText());
        dateInputLabel2.setText(validationResponse.message);
        valid &= validationResponse.isValid;
        if (validationResponse.isValid) {
            validationResponse = Utilities.dateValidation(dateInput2.getText(), Utilities.DATE_FORMAT);
            valid &= validationResponse.isValid;
            dateInputLabel2.setText(validationResponse.message);
        }

        if (valid) {
            try {
                if (Utilities.parseDate(dateInput1.getText(), Utilities.DATE_FORMAT).after(Utilities.parseDate(dateInput2.getText(), Utilities.DATE_FORMAT))) {
                    valid = false;
                    dateInputLabel2.setText("Вторая дата должна быть после первой ");
                }
            } catch (Exception e) {
                valid = false;
            }

        }
        applyButton.setEnabled(valid);
    }

    public void show() {
        dialog.setVisible(true);
    }

}
