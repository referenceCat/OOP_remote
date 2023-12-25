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
    public JComboBox typeInput;
    public JTextField debtInput;
    public JTextField commentaryInput;
    public JTextField dateInput;
    public JTextField vehicleIdInput;
    public JTextField ownerIdInput;
    public JTextField officerIdInput;
    public JLabel debtInputLabel;
    public JLabel commentaryInputLabel;
    public JLabel dateInputLabel;
    public JLabel vehicleIdInputLabel;
    public JLabel officerIdInputLabel;
    public JLabel ownerIdInputLabel;


    public ViolationDialog(JFrame frame) {
        VerticalLayout layout = new VerticalLayout();
        panel = new JPanel(layout);
        dialog = new JDialog(frame);

        String[] options_penalty = {"Штраф", "Лишение  прав", "Предупреждение", "Административный арест", "Конфискация орудия нарушения"};
        String[] options_violation = {"Превышение скорости",
                "Несоблюдение требований дорожных знаков / разметки",
                "Проезд на красный или пересечение стоп-линии",
                "Неправильное расположение машины на дороге",
                "Движение по полосе для автобусов",
                "Нарушение правил стоянки или остановки"};

        typeInput = new JComboBox(options_violation);
        panel.add(new JLabel("Тип правонарушения: "));
        panel.add(typeInput);
        panel.add(new JLabel(" "));

        penaltyInput = new JComboBox(options_penalty);
        panel.add(new JLabel("Наказание: "));
        panel.add(penaltyInput);
        panel.add(new JLabel(" "));


        debtInput = new JTextField(40);
        commentaryInput = new JTextField(40);
        dateInput = new JTextField(40);
        vehicleIdInput = new JTextField(40);
        ownerIdInput = new JTextField(40);
        officerIdInput = new JTextField(40);
        applyButton = new JButton("Apply");

        debtInputLabel = new JLabel(" ");
        commentaryInputLabel = new JLabel(" ");
        dateInputLabel = new JLabel(" ");
        vehicleIdInputLabel = new JLabel(" ");
        ownerIdInputLabel = new JLabel(" ");
        officerIdInputLabel = new JLabel(" ");

        debtInputLabel.setForeground(Color.RED);
        commentaryInputLabel.setForeground(Color.RED);
        dateInputLabel.setForeground(Color.RED);
        vehicleIdInputLabel.setForeground(Color.RED);
        ownerIdInputLabel.setForeground(Color.RED);
        officerIdInputLabel.setForeground(Color.RED);

        debtInputLabel.setFont(new Font("Times New Roman", Font.ITALIC, 12));
        commentaryInputLabel.setFont(new Font("Times New Roman", Font.ITALIC, 12));
        dateInputLabel.setFont(new Font("Times New Roman", Font.ITALIC, 12));
        vehicleIdInputLabel.setFont(new Font("Times New Roman", Font.ITALIC, 12));
        ownerIdInputLabel.setFont(new Font("Times New Roman", Font.ITALIC, 12));
        officerIdInputLabel.setFont(new Font("Times New Roman", Font.ITALIC, 12));

        applyButton = new JButton("Apply");

        penaltyInput.addActionListener(e -> onTextUpdate());

        typeInput.addActionListener(e -> onTextUpdate());

        panel.add(new JLabel("Штраф:"));
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

        panel.add(new JLabel("Комментраий:"));
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


        panel.add(new JLabel("Дата:"));
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


        panel.add(new JLabel("Id ТС: *"));
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

        panel.add(new JLabel("Id нарушителя:"));
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

        panel.add(new JLabel("Id сотрудника:"));
        panel.add(officerIdInput);
        panel.add(officerIdInputLabel);
        officerIdInput.getDocument().addDocumentListener(new DocumentListener() {
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
        dialog.setSize(455, 600);
        onTextUpdate();
    }

    private void onTextUpdate() {
        debtInput.setEnabled(penaltyInput.getSelectedIndex() == 0);
        boolean valid = true;
        Utilities.ValidationResponse validationResponse;

        applyButton.setEnabled(valid);

        if ((penaltyInput.getSelectedIndex() == 0) && debtInput.getText().isEmpty()) {
            valid = false;
            debtInputLabel.setText("Необходимое поле ");
        } else if (!Utilities.isInteger(debtInput.getText())) {
            valid = false;
            debtInputLabel.setText("Значение должно быть целым числом ");
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
            vehicleIdInputLabel.setText("Значение должно быть целым числом ");
        }

        validationResponse = Utilities.requiredFieldCheck(ownerIdInput.getText());
        ownerIdInputLabel.setText(validationResponse.message);
        valid &= validationResponse.isValid;
        if (validationResponse.isValid && !Utilities.isInteger(ownerIdInput.getText())) {
            valid = false;
            ownerIdInputLabel.setText("Значение должно быть целым числом ");
        }

        if (!officerIdInput.getText().isEmpty() && !Utilities.isInteger(officerIdInput.getText())) {
            valid = false;
            officerIdInputLabel.setText("Значение должно быть целым числом ");
        }

        applyButton.setEnabled(valid);
    }

    public void show() {
        dialog.setVisible(true);
    }
}
