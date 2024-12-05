package software.ulpgc.moneycalculator.app;

import software.ulpgc.moneycalculator.control.Command;
import software.ulpgc.moneycalculator.control.ExchangeMoneyCommand;
import software.ulpgc.moneycalculator.fixerws.FixerCurrencyLoader;
import software.ulpgc.moneycalculator.model.Currency;
import software.ulpgc.moneycalculator.view.CurrencyDialog;
import software.ulpgc.moneycalculator.io.ExchangeRateLoader;
import software.ulpgc.moneycalculator.view.MoneyDialog;
import software.ulpgc.moneycalculator.view.MoneyDisplay;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static software.ulpgc.moneycalculator.io.ImageDownloader.downloadImage;


public class SwingMain extends JFrame {
    private final Map<String, Command> commands = new HashMap<>();
    private MoneyDisplay moneyDisplay;
    private MoneyDialog moneyDialog;
    private CurrencyDialog currencyDialog;

    public static void main(String[] args) {
        File path = new File("");

        String Path = path.getAbsolutePath();
        String urlInput = "https://cdn.icon-icons.com/icons2/606/PNG/512/coins-of-dollars-stack_icon-icons.com_56192.png";

        String outputPath = Path+"\\coins-of-dollars-stack_icon-icons.com_56192.png";

        downloadImage(urlInput, outputPath);
        SwingMain main = new SwingMain();
        List<Currency> currencies = new FixerCurrencyLoader().load();
        Command command = new ExchangeMoneyCommand(
                main.moneyDialog().define(currencies),
                main.currencyDialog().define(currencies),
                new ExchangeRateLoader(),
                main.moneyDisplay());
        main.add(command);
        main.setVisible(true);
    }

    public SwingMain() throws HeadlessException {
        this.setTitle("Money calculator");
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.setLayout(new FlowLayout());
        this.add(createMoneyDialog());
        this.add(createCurrencyDialog());
        this.add(Box.createHorizontalStrut(50));
        this.add(toolbar());
        this.add(Box.createVerticalStrut(20));
        this.add(Box.createHorizontalStrut(this.getWidth()));
        this.add(createMoneyDisplay());


        this.getContentPane().setBackground(Color.DARK_GRAY);
        this.setIconImage(new ImageIcon("coins-of-dollars-stack_icon-icons.com_56192.png").getImage());
    }

    private Component toolbar() {
        JButton button = new JButton("calculate");
        button.addActionListener(_ -> {
            try {
                commands.get("exchange money").execute();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        return button;
    }

    private Component createMoneyDisplay() {
        SwingMoneyDisplay display = new SwingMoneyDisplay();
        this.moneyDisplay = display;
        return display;
    }

    private Component createCurrencyDialog() {
        SwingCurrencyDialog dialog = new SwingCurrencyDialog();
        this.currencyDialog = dialog;
        return dialog;
    }

    private Component createMoneyDialog() {
        SwingMoneyDialog dialog = new SwingMoneyDialog();
        this.moneyDialog = dialog;
        return dialog;
    }

    private void add(Command command) {
        commands.put("exchange money", command);
    }

    private MoneyDisplay moneyDisplay() {
        return moneyDisplay;
    }

    private CurrencyDialog currencyDialog() {
        return currencyDialog;
    }

    private MoneyDialog moneyDialog() {
        return moneyDialog;
    }
}
