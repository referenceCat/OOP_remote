package org.referenceCat.ui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class ViolationDialog {
    public JDialog dialog;
    public JPanel panel;
    public JButton applyButton;

    private final String[] options = {"Debt", "Jail (10 years)", "Warning", "Deprivation of license"};
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
        debtInput.getDocument().addDocumentListener(new DocumentListener() {public void changedUpdate(DocumentEvent e) {
            onTextUpdate();} public void removeUpdate(DocumentEvent e) {
            onTextUpdate();} public void insertUpdate(DocumentEvent e) {
            onTextUpdate();}});

        panel.add(new JLabel("Commentary:"));
        panel.add(commentaryInput);
        panel.add(commentaryInputLabel);
        commentaryInput.getDocument().addDocumentListener(new DocumentListener() {public void changedUpdate(DocumentEvent e) {
            onTextUpdate();} public void removeUpdate(DocumentEvent e) {
            onTextUpdate();} public void insertUpdate(DocumentEvent e) {
            onTextUpdate();}});


        panel.add(new JLabel("Date and time:"));
        panel.add(dateInput);
        panel.add(dateInputLabel);
        dateInput.getDocument().addDocumentListener(new DocumentListener() {public void changedUpdate(DocumentEvent e) {
            onTextUpdate();} public void removeUpdate(DocumentEvent e) {
            onTextUpdate();} public void insertUpdate(DocumentEvent e) {
            onTextUpdate();}});


        panel.add(new JLabel("Vehicle id: *"));
        panel.add(vehicleIdInput);
        panel.add(vehicleIdInputLabel);
        vehicleIdInput.getDocument().addDocumentListener(new DocumentListener() {public void changedUpdate(DocumentEvent e) {
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
        debtInput.setEnabled(penaltyInput.getSelectedIndex() == 0);
        boolean valid = true;

        System.out.println(penaltyInput.getSelectedIndex() == 0);
        System.out.println(debtInput.getText().isEmpty());
        if ((penaltyInput.getSelectedIndex() == 0) && debtInput.getText().isEmpty()) {
            valid = false;
            debtInputLabel.setText("Required field if penalty is debt");
        } else {
            debtInputLabel.setText(" ");
        }

        if (commentaryInput.getText().length() > 200) {
            valid = false;
            commentaryInputLabel.setText("Too big ");
        } else {
            commentaryInputLabel.setText(" ");
        }

        if (dateInputLabel.getText().isEmpty()) {
            valid = false;
            dateInputLabel.setText("Required field ");
        } else {
            dateInputLabel.setText(" ");
        }

        applyButton.setEnabled(valid);
        // todo other validations
    }

    public void show() {
        dialog.setVisible(true);
    }
}
