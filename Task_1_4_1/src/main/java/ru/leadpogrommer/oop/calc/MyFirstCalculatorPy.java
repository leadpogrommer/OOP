package ru.leadpogrommer.oop.calc;

import org.apache.commons.math3.complex.Complex;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;


record Operation(String regex, int arity, BiFunction<Complex[], String, Complex> evaluate) {
    Operation(String regex, int arity, Function<Complex[], Complex> f){
        this(regex, arity, (values, s) -> f.apply(values));
    }
}


record Token(Operation op, String text) {
}


public class MyFirstCalculatorPy {
    private final List<Operation> operations = new ArrayList<>();

    static class UnknownTokenException extends RuntimeException{
        public final String tokenText;
        UnknownTokenException(String message, String tokenText){
            super(message);
            this.tokenText = tokenText;
        }
    }

    static class InvalidArityException extends RuntimeException{}

    MyFirstCalculatorPy() {
        operations.add(new Operation("\\+", 2, (values) -> values[0].add(values[1])));
        operations.add(new Operation("-", 2, (values) -> values[0].subtract(values[1])));
        operations.add(new Operation("\\*", 2, (values) -> values[0].multiply(values[1])));
        operations.add(new Operation("/", 2, (values) -> values[0].divide(values[1])));
        operations.add(new Operation("cos", 1, (values) -> values[0].cos()));
        operations.add(new Operation("sin", 1, (values) -> values[0].sin()));
        operations.add(new Operation("log", 1, (values) -> values[0].log()));
        operations.add(new Operation("pow", 2, (values) -> values[0].pow(values[1])));
        operations.add(new Operation("sqrt", 1, (values) -> values[0].sqrt()));

        operations.add(new Operation("-?\\d+(\\.\\d+)?", 0, (values, s) -> new Complex(Double.parseDouble(s))));
        operations.add(new Operation("PI", 0, (values, s) -> new Complex(Math.PI)));
        operations.add(new Operation("e", 0, (values, s) -> new Complex(Math.E)));
    }

    public static void main(String[] args) {
        var calc = new MyFirstCalculatorPy();
        var sc = new Scanner(System.in);
        while (true) {
            System.out.print('>');
            var str = sc.nextLine();
            if (str.equals(".quit")) break;
            try {
                var res = calc.evaluateString(str);
                System.out.println(res.getReal());
            } catch (UnknownTokenException e) {
                System.out.println("ERROR: " + e.getMessage());
            }catch (InvalidArityException e){
                System.out.println("Invalid number of arguments");
            }
        }
    }

    public Complex evaluateString(String str) {
        Stack<Complex> stack = new Stack<>();
        var wtf = Arrays.stream(str.split("\\s+")).map((s) -> {
            for (var op : operations) {
                if (Pattern.matches("^" + op.regex() + "$", s)) {
                    return new Token(op, s);
                }
            }
            throw new UnknownTokenException("Unknown token: " + s, s);
        }).toArray();
        var tokens = Arrays.copyOf(wtf, wtf.length, Token[].class);


        for (var i = tokens.length - 1; i >= 0; i--) {
            var token = tokens[i];
            var op = token.op();
            if (stack.size() < op.arity()) throw new InvalidArityException();
            Complex[] args = new Complex[op.arity()];
            for (var j = 0; j < op.arity(); j++) {
                args[j] = stack.pop();
            }
            stack.push(op.evaluate().apply(args, token.text()));
        }

        if (stack.size() != 1) throw new InvalidArityException();
        return stack.pop();
    }

}
