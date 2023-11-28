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
    public JTextField ownerIdInput;
    public JLabel regNumberInputLabel;
    public JLabel modelInputLabel;
    public JLabel colorInputLable;
    public JLabel ownerIdInputLabel;


    public VehicleDialog(JFrame frame) {
        VerticalLayout layout = new VerticalLayout();
        panel = new JPanel(layout);
        dialog = new JDialog(frame);

        regNumberInput = new JTextField(40);
        modelInput = new JTextField(40);
        colorInput = new JTextField(40);
        ownerIdInput = new JTextField(40);
        applyButton = new JButton("Apply");

        regNumberInputLabel = new JLabel(" ");
        modelInputLabel = new JLabel(" ");
        colorInputLable = new JLabel(" ");
        ownerIdInputLabel = new JLabel(" ");

        regNumberInputLabel.setForeground(Color.RED);
        modelInputLabel.setForeground(Color.RED);
        colorInputLable.setForeground(Color.RED);
        ownerIdInputLabel.setForeground(Color.RED);

        regNumberInputLabel.setFont(new Font("Times New Roman", Font.ITALIC, 12));
        modelInputLabel.setFont(new Font("Times New Roman", Font.ITALIC, 12));
        colorInputLable.setFont(new Font("Times New Roman", Font.ITALIC, 12));
        ownerIdInputLabel.setFont(new Font("Times New Roman", Font.ITALIC, 12));

        applyButton = new JButton("Apply");

        panel.add(new JLabel("Registration number: *"));
        panel.add(regNumberInput);
        panel.add(regNumberInputLabel);
        regNumberInput.getDocument().addDocumentListener(new DocumentListener() {public void changedUpdate(DocumentEvent e) {
            onTextUpdate();} public void removeUpdate(DocumentEvent e) {
            onTextUpdate();} public void insertUpdate(DocumentEvent e) {
            onTextUpdate();}});

        panel.add(new JLabel("Model:"));
        panel.add(modelInput);
        panel.add(modelInputLabel);
        modelInput.getDocument().addDocumentListener(new DocumentListener() {public void changedUpdate(DocumentEvent e) {
            onTextUpdate();} public void removeUpdate(DocumentEvent e) {
            onTextUpdate();} public void insertUpdate(DocumentEvent e) {
            onTextUpdate();}});


        panel.add(new JLabel("Color:"));
        panel.add(colorInput);
        panel.add(colorInputLable);
        colorInput.getDocument().addDocumentListener(new DocumentListener() {public void changedUpdate(DocumentEvent e) {
            onTextUpdate();} public void removeUpdate(DocumentEvent e) {
            onTextUpdate();} public void insertUpdate(DocumentEvent e) {
            onTextUpdate();}});


        panel.add(new JLabel("Owner's id: *"));
        panel.add(ownerIdInput);
        panel.add(ownerIdInputLabel);
        ownerIdInput.getDocument().addDocumentListener(new DocumentListener() {public void changedUpdate(DocumentEvent e) {
            onTextUpdate();} public void removeUpdate(DocumentEvent e) {
            onTextUpdate();} public void insertUpdate(DocumentEvent e) {
            onTextUpdate();}});


        panel.add(applyButton);
        applyButton.setEnabled(false);

        dialog.add(panel);
        dialog.setSize(455, 350);
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
