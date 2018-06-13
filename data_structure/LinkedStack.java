package data_structure;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

// Implement a stack using a linked list
public class LinkedStack<T> implements Iterable<T>{
    private LinkedList<T> storage = new LinkedList<T>();

    public void push(T value) {
        storage.addFirst(value);
    }

    public T pop() {
        return storage.removeFirst();
    }

    public T peek() {
        return storage.getFirst();
    }

    public boolean isEmpty() {
        return storage.isEmpty();
    }

    @Override
    public Iterator<T> iterator() {
        return storage.iterator();
    }

    @Override
    public String toString() {
        return storage.toString();
    }

    // Application 1: reverse a string or linked list
    // Time and space complexity both: O(n), n is the length of string
    // More efficient way: swapping
    private static String reverseString(String str) {
        StringBuilder sb = new StringBuilder();
        LinkedStack<Character> characterStack = new LinkedStack<Character>();
        for (int i = 0; i < str.length(); i++)
            characterStack.push(str.charAt(i));
        for (int i = 0; i < str.length(); i++)
            sb.append(characterStack.pop());
        return sb.toString();
    }

    // Application 2: Check balanced expression
    private static boolean checkBalancedParenthesis(String expr) {
        LinkedStack<Character> stack = new LinkedStack<Character>();
        for (int i = 0; i < expr.length(); i++) {
            Character character = expr.charAt(i);
            if (character == '(' || character == '[' || character == '{')
                stack.push(character);
            if (character == ')' || character == ']' || character == '}') {
                if (stack.isEmpty()) return false;
                if (parenthesisPair(stack.peek(), character)) stack.pop();
            }
        }
        return stack.isEmpty();
    }

    // TODO: check balanced expression using recursion

    // Application 3: Evaluate postfix, prefix expressions
    private static int evaluatePostfix(String expr) {
        LinkedStack<Integer> stack = new LinkedStack<Integer>();
        String[] strArray = expr.split(" "); // to simplify, suppose operators/operands are seperated by space
        for (String str : strArray) {
            if (isOperand(str)) {
                stack.push(Integer.parseInt(str));
            } else if (isOperator(str)) {
                int op2 = stack.pop();
                int op1 = stack.pop();
                int result = evaluate(str, op1, op2);
                stack.push(result);
            }
        }
        return stack.pop();
    }

    private static int evaluatePrefix(String expr) {
        LinkedStack<Integer> stack = new LinkedStack<Integer>();
        String[] strArray = expr.split(" ");
        for (int i = strArray.length - 1; i >= 0; i--) {
            String str = strArray[i];
            if (isOperand(str)) {
                stack.push(Integer.parseInt(str));
            } else if (isOperator(str)) {
                int op1 = stack.pop();
                int op2 = stack.pop();
                int result = evaluate(str, op1, op2);
                stack.push(result);
            }
        }
        return stack.pop();
    }


    private static int evaluate(String str, int op1, int op2) {
        if (str.equals("+")) return op1 + op2;
        if (str.equals("-")) return op1 - op2;
        if (str.equals("*")) return op1 * op2;
        if (str.equals("/")) return op1 / op2;
        else throw new RuntimeException("No such operator!");
    }

    private static boolean isOperator(String str) {
        return str.equals("+") || str.equals("*") || str.equals("-") || str.equals("/");
    }

    private static boolean isOperand(String str) {
        return str.matches("[0-9]+");
    }


    private static boolean parenthesisPair(Character c1, Character c2) {
        return (c1 == '(' && c2 == ')') || (c1 == '[' && c2 == ']') || (c1 == '{' && c2 == '}');
    }

    // Application 4: convert infix expression to postfix expression, with no parenthesis in the expression
    private static String infixToPostfix(String expr) {
        LinkedStack<String> s = new LinkedStack<String>();
        // suppose that operator/operands are seperated by space
        String[] strArrays = expr.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String str : strArrays) {
            if (isOperand(str)) sb.append(str).append(" ");
            if (isOperator(str)) {
                while (!s.isEmpty() && hasHigherOrEqualPrec(s.peek(), str)) sb.append(s.pop()).append(" ");
                s.push(str);
            }
        }
        while (!s.isEmpty()) sb.append(s.pop()).append(" ");
        return sb.toString();
    }

    // Convert infix expression to postfix expression, with parenthesis in the expression
    private static String infixToPostfixWithParenthesis(String expr) {
        LinkedStack<String> s = new LinkedStack<String>();
        // suppose that operator/operands are separated by space
        String[] strArrays = expr.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String str : strArrays) {
            if (isOperand(str)) sb.append(str).append(" ");
            if (isOperator(str)) {
                while (!s.isEmpty() && hasHigherOrEqualPrec(s.peek(), str) && !isOpeningParenthesis(s.peek()))
                    sb.append(s.pop()).append(" ");
                s.push(str);
            }
            if (isOpeningParenthesis(str)) {
                s.push(str);
            }
            if (isClosingParenthesis(str)) {
                while (!s.isEmpty() && !isOpeningParenthesis(s.peek())) {
                    sb.append(s.pop()).append(" ");
                }
                s.pop();
            }
        }
        while (!s.isEmpty()) sb.append(s.pop()).append(" ");
        return sb.toString();
    }

    private static boolean isOpeningParenthesis(String str) {
        return str.equals("(");
    }

    private static boolean isClosingParenthesis(String str) {
        return str.equals(")");
    }

    private static boolean hasHigherOrEqualPrec(String peek, String str) {
        List<String> operators = Arrays.asList("+", "-", "*", "/");
        return operators.indexOf(peek) > 1 || operators.indexOf(str) <= 1;
    }


    public static void main(String[] args) {
        System.out.println("reverse the string 'abcd': " + reverseString("abcd"));
        System.out.println("does '[(])' have balanced parenthesis: " + checkBalancedParenthesis("[(])"));
        System.out.println("does ')(' have balanced parenthesis: " + checkBalancedParenthesis(")("));
        System.out.println("does '{()[]()}' have balanced parenthesis: " + checkBalancedParenthesis("{()[]()}"));
        System.out.println("the postfix evaluation of '2 3 * 5 4 * + 9 -' is: " + evaluatePostfix("2 3 * 5 4 * + 9 -"));
        System.out.println("the prefix evaluation of '- + * 2 3 * 5 4 9' is: " + evaluatePrefix("- + * 2 3 * 5 4 9"));
        System.out.println("the postfix representation of '1 + 2 * 3 - 4 * 5' is: " + infixToPostfix("1 + 2 * 3 - 4 * 5"));
        System.out.println("the postfix representation of '( ( 1 + 2 ) * 3 - 4 ) * 5' is: " + infixToPostfixWithParenthesis("( ( 1 + 2 ) * 3 - 4 ) * 5"));
        System.out.println(infixToPostfixWithParenthesis("the postfix representation of '1 * ( 2 + 3 )' is: " + "1 * ( 2 + 3 )"));
    }

}