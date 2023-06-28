public class Main {
    public static void main(String[] args) {

        String input = "toDollars(737р + toRubles($85,4))";
        input = CurrencyCalculator.replaceCommas(input);
        int count = CurrencyCalculator.countOccurrences(input,'р', '$');
        int notChangedCount = 0;

        while(true){
            input = CurrencyCalculator.performDollarCalculation(input);
            input = CurrencyCalculator.performRublesCalculation(input);
            input = CurrencyCalculator.convertToDollars(input);
            input = CurrencyCalculator.convertToRubles(input);
            int previousNumber = count;
            count = CurrencyCalculator.countOccurrences(input,'р', '$');
            if(previousNumber == count){
                notChangedCount++;
                if (notChangedCount == 3){
                    System.out.println("Invalid input");
                    return;
                }
            }else notChangedCount = 0;
            if (previousNumber == 1 && count == 1){
                if(CurrencyCalculator.validateString(input)){
                    break;
                }else{
                    System.out.println("Invalid input");
                    return;
                }
            }
        }
        input = CurrencyCalculator.findAndRoundDouble(input);
        System.out.println("Result: " + input);
    }
}
