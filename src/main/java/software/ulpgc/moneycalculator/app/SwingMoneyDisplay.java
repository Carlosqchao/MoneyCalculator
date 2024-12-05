package software.ulpgc.moneycalculator.app;

import software.ulpgc.moneycalculator.model.Money;
import software.ulpgc.moneycalculator.view.MoneyDisplay;

import javax.swing.*;
import java.awt.*;

public class SwingMoneyDisplay extends JLabel implements MoneyDisplay {
    public SwingMoneyDisplay() {
        this.setForeground(Color.WHITE);
        this.setFont(new Font("",Font.PLAIN, 18));
    }
    @Override
    public void show(Money money, double percentage) {
        String formattedPercentage = String.format("%.2f", percentage);
        if (percentage > 100){
            this.setForeground(Color.GREEN);
        } else if (percentage < 100){
            this.setForeground(Color.RED);
        }
        this.setText(STR."\{money.toString()} \{formattedPercentage}%");
    }

}
