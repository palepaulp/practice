import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.*;

public class CurrencyCalculator {
    private static final String CONFIG_FILE_PATH = "config.txt";

    private static Map<String, Double> loadExchangeRatesFromConfig() {
        Map<String, Double> rates = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String currency = parts[0].trim();
                    Double rate = new Double(parts[1].trim());
                    rates.put(currency, rate);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rates;
    }

    public static String performDollarCalculation(String input) {
        Pattern pattern = Pattern.compile("(\\$\\d+(\\.\\d+)?)\\s*([+\\-*/])\\s*(\\$\\d+(\\.\\d+)?)");

        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            double num1 = Double.parseDouble(matcher.group(1).replaceFirst("\\$", ""));
            String operator = matcher.group(3);
            double num2 = Double.parseDouble(matcher.group(4).replaceFirst("\\$", ""));
            double result = 0;

            if (operator.equals("+")) {
                result = num1 + num2;
            } else if (operator.equals("-")) {
                result = num1 - num2;
            } else if (operator.equals("*")) {
                result = num1 * num2;
            } else if (operator.equals("/")) {
                result = num1 / num2;
            }

            input = input.replace(matcher.group(), String.format(Locale.US,"$%.2f", result));
        }

        return input;
    }

    public static String performRublesCalculation(String input) {
        Pattern pattern = Pattern.compile("(\\d+(\\.\\d+)?р)\\s*([+\\-*/])\\s*(\\d+(\\.\\d+)?р)");

        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            double num1 = Double.parseDouble(matcher.group(1).replaceFirst("р", ""));
            String operator = matcher.group(3);
            double num2 = Double.parseDouble(matcher.group(4).replaceFirst("р", ""));
            double result = 0;

            if (operator.equals("+")) {
                result = num1 + num2;
            } else if (operator.equals("-")) {
                result = num1 - num2;
            } else if (operator.equals("*")) {
                result = num1 * num2;
            } else if (operator.equals("/")) {
                result = num1 / num2;
            }
            input = input.replace(matcher.group(), String.format(Locale.US,"%.2fр", result));
        }

        return input;
    }

    public static String convertToDollars(String input) {
        String regex = "toDollars\\((\\d+(\\.\\d+)?)р\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        Map<String, Double> rates = new HashMap<>();
        rates = loadExchangeRatesFromConfig();

        StringBuffer output = new StringBuffer();
        while (matcher.find()) {
            double number = Double.parseDouble(matcher.group(1));
            double result = number * rates.get("ConvertCurrencyToRubles");
            matcher.appendReplacement(output, "\\$" + result);
        }
        matcher.appendTail(output);

        return output.toString();
    }

    public static String convertToRubles(String input) {
        String regex = "toRubles\\(\\$(\\d+(\\.\\d+)?)\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        Map<String, Double> rates = new HashMap<>();
        rates = loadExchangeRatesFromConfig();

        StringBuffer output = new StringBuffer();
        while (matcher.find()) {
            double number = Double.parseDouble(matcher.group(1));
            double result = number * rates.get("ConvertCurrencyToDollars");
            matcher.appendReplacement(output, Double.toString(result) + "р");
        }
        matcher.appendTail(output);

        return output.toString();
    }

    public static String replaceCommas(String input) {
        return input.replace(",", ".");
    }

    public static String findAndRoundDouble(String input) {
        String numberString = input.replaceAll("[^0-9.]+", "");
        double number = Double.parseDouble(numberString);

        double roundedNumber = Math.round(number * 100) / 100.0;

        String roundedNumberString = String.format(Locale.US,"%.2f", roundedNumber);

        String updatedString = input.replace(numberString, roundedNumberString);

        return updatedString;
    }

    public static int countOccurrences(String str, char... chars) {
        int count = 0;
        for (char c : chars) {
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) == c) {
                    count++;
                }
            }
        }
        return count;
    }
    
    public static boolean validateString(String input) {
        String pattern = "^[0-9.$р]+$";

        return input.matches(pattern);
    }
}
