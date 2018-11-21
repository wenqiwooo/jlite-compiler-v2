package wwu.compiler.util;

public class Util {

    public static String escapeStr(String s) {
        StringBuffer out = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\t') {
                out.append("\\t");
            } 
            else if (c == '\b') {
                out.append("\\b");
            } 
            else if (c == '\n') {
                out.append("\\n");
            } 
            else if (c == '\r') {
                out.append("\\r");
            } 
            else if (c == '\f') {
                out.append("\\f");
            } 
            else if (c == '\'') {
                out.append("\\\'");
            } 
            else if (c == '\"') {
                out.append("\\\"");
            } 
            else if (c == '\\') {
                out.append("\\\\");
            } 
            else {
                out.append(c);
            }
        }
        return out.toString();
    }
}