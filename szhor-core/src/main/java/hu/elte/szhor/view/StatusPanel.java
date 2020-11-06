package hu.elte.szhor.view;

import javax.swing.*;

public class StatusPanel extends JPanel {

    private final JLabel label;

    public StatusPanel() {
        this.label = new JLabel("Simulation started.");
        this.add(this.label);
    }

    public String getLabelText() {
        return this.label.getText();
    }

    public void setLabelText(final String labelText) {
        this.label.setText(labelText);
    }
}
