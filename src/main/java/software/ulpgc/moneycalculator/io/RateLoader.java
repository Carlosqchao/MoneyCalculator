package software.ulpgc.moneycalculator.io;

import software.ulpgc.moneycalculator.model.Currency;

import java.io.IOException;

public interface RateLoader {
    double load(Currency from, Currency to) throws IOException;
}
