package ru.leadpogrommer.oop.calc;

import org.apache.commons.math3.complex.Complex;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;


record Operation(String regex, int arity, Function<Complex[], Complex> evaluate) {
}

record ValueParser(String regex, Function<String, Complex> parse) {
}

record Token(Operation op, Complex val) {
    public boolean isOperation() {
        return op != null;
    }
}


public class MyFirstCalculatorPy {
    private final List<Operation> operations = new ArrayList<>();
    private final List<ValueParser> valueParsers = new ArrayList<>();

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

        valueParsers.add(new ValueParser("-?\\d+(\\.\\d+)?", (s) -> new Complex(Double.parseDouble(s))));
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
                System.out.println(res);
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    public Complex evaluateString(String str) {
        Stack<Complex> stack = new Stack<>();
        var wtf = Arrays.stream(str.split("\\s+")).map((s) -> {
            for (var op : operations) {
                if (Pattern.matches("^" + op.regex() + "$", s)) {
                    return new Token(op, null);
                }
            }
            for (var vp : valueParsers) {
                if (Pattern.matches("^" + vp.regex() + "$", s)) {
                    return new Token(null, vp.parse().apply(s));
                }
            }
            throw new IllegalStateException();
        }).toArray();
        var tokens = Arrays.copyOf(wtf, wtf.length, Token[].class);


        for (var i = tokens.length - 1; i >= 0; i--) {
            var token = tokens[i];
            if (token.isOperation()) {
                var op = token.op();
                if (stack.size() < op.arity()) throw new IllegalStateException();
                Complex[] args = new Complex[op.arity()];
                for (var j = 0; j < op.arity(); j++) {
                    args[j] = stack.pop();
                }
                stack.push(op.evaluate().apply(args));
            } else {
                stack.push(token.val());
            }
        }

        if (stack.size() != 1) throw new IllegalStateException();
        return stack.pop();
    }

}
