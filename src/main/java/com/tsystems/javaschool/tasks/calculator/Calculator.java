package com.tsystems.javaschool.tasks.calculator;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.POSITIVE_INFINITY;

public class Calculator {

    /**
     * Evaluate statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     * @return string value containing result of evaluation or null if statement is invalid
     */

    public String evaluate(String statement) {

        if (!isValid(statement)) return null;

        //на всякий случай удалим пробелы
        statement = statement.replaceAll(" ", "");

        //заменим "-" на "+-" для дальнейшего получения просто суммы положительных и отрицательных выражений
        statement = statement.replaceAll("-", "+-");

        //проверим на наличие скобок
        if (statement.contains("(")) {
            statement = evaluateParentheses (statement);
        }
        return evaluateSubstitutionMinuses(statement);
    }

    //вводим дополнительный метод, чтобы при вычислении выражения внутри скобок не менялись "-" на "+-" повторно
    public String evaluateSubstitutionMinuses (String str) {
        double result = 0.0;

        //разобьем строку по знаку "+" на массив выражений
        String[] expressions = str.split("\\+");

        //перебираем массив выражений, вычисляя их значения и плюсуя к результату
        for (String expression : expressions) {
            if (expression.contains("*")) {
                result += getMultiply(expression);
            } else if (expression.contains("/")) {
                result += getDivision(expression);
            } else result += Double.parseDouble(expression);
        }
        return getRoundAndIsDividedByZero(result);
    }

    //метод для действия деления
    public double getDivision (String expression){
        String [] byDelimiter = expression.split("/");
        double resultByDevision = Double.parseDouble(byDelimiter[0]);
        for (int i = 1; i < byDelimiter.length; i++){
            resultByDevision /= Double.parseDouble(byDelimiter[i]);
        }
        return resultByDevision;
    }

    //метод для действия умножения
    public double getMultiply (String expression){
        String [] byMultiply = expression.split("\\*");
        double resultByMultiply = 1.0;
        for (String element : byMultiply){

            //т.к. мы разбивали строку только по "+", в одно выражение могли попасть и "/", и "*", стоящие последовательно, поэтому делаем проверку на наличие "/" в подстроке
            if (element.contains("/")) {
                resultByMultiply *= getDivision(element);
            }
            else resultByMultiply *= Double.parseDouble(element);
        }
        return resultByMultiply;
    }

    //метод для округления и преобразования результата в строку, также проверяем деление на ноль (если в ответе бесконечность, значит в процессе вычислений было деление на ноль)
    public String getRoundAndIsDividedByZero (double value){
        if (value == POSITIVE_INFINITY || value == NEGATIVE_INFINITY) return null;

        Locale currentLocale = Locale.getDefault();
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(currentLocale);
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#.####", otherSymbols);
        decimalFormat.setRoundingMode(RoundingMode.CEILING);

        return decimalFormat.format(value);
    }

    //метод для вычисления, если в строке есть скобки
    public String evaluateParentheses(String str){
        int start = str.lastIndexOf("(");
        int finish = str.indexOf(")", start);

        //вычленяем из строки сегмент с внутренними скобками
        String segmentForReplacing = str.substring(start, finish+1);

        //удаляем скобки из сегмента для дальнейшего вычисления его значения
        String segmentForEvaluating = segmentForReplacing.substring(1, segmentForReplacing.length()-1);

        //вычисляем значение выражения внутри скобки
        String resultEvaluatingSegment = evaluateSubstitutionMinuses(segmentForEvaluating);

        //заменяем скобку значением, вычисленным в этой скобке
        str = str.replace(segmentForReplacing, resultEvaluatingSegment);
        if (str.contains("(")) str = evaluateParentheses(str);
        return str;
    }

    //проверяем строку на валидность
    public boolean isValid (String str){

        //на null и отсутствие символов
        if(str == null || str.length() == 0) return false;

        //на правильно расставленные скобки
        if(str.indexOf('(') > str.indexOf(')')) return false;

        //на задвоенные знаки действий
        if (str.contains("--") || str.contains("++") || str.contains("**") || str.contains("//") || str.contains("..")) return false;

        //на наличие постронних символов
        if (!str.matches("[0-9\\?\\*\\+\\-\\(\\)\\.\\/]+")) return false;

        //на наличие лишних скобок
        char [] array = str.toCharArray();
        int openPar = 0;
        int closePar = 0;
        for (char symbol : array){
            if (symbol == '(') openPar++;
            if (symbol == ')') closePar++;
        }
        if (openPar!=closePar) return false;
        else return true;
    }
}
