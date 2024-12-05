package software.ulpgc.moneycalculator.view;

import software.ulpgc.moneycalculator.model.Currency;

import java.util.List;

public interface CurrencyDialog {
    CurrencyDialog define(List<Currency> currencies);
    Currency get();
}
