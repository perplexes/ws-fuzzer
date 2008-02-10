/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author chang
 */
public class StringUtils {

    /**
     * prevent to instancing
     */
    private StringUtils() {

    }

    /**
     * escape all special characters
     * @param string
     * @return
     */
    public static String escapeForXML(String string) {
        string = substitute(string, "&", "&amp;");
        string = substitute(string, "\"", "&quot;");
        string = substitute(string, "<", "&lt;");
        string = substitute(string, ">", "&gt;");
        string = substitute(string, "\n", "&#10;");
        string = substitute(string, "\r", "&#13;"); // Carriage return
        return string;
    }
    
    /**
     * recover all special characters
     * @param string
     * @return
     */
    public static String unescapeForXML(String string) {
        string = substitute(string, "&amp;", "&");
        string = substitute(string, "&quot;", "\"");
        string = substitute(string, "&lt;", "<");
        string = substitute(string, "&gt;", ">");
        string = substitute(string, "&#10;", "\n");
        string = substitute(string, "&#13;", "\r");
        return string;
    }
    
    /**
     * Replace all occurrences of pattern with replacement in string
     * @param string
     * @param pattern
     * @param replacement
     * @return
     */
    public static String substitute(String string, String pattern,
            String replacement) {
        int start = string.indexOf(pattern);

        while (start != -1) {
            StringBuffer buffer = new StringBuffer(string);
            buffer.delete(start, start + pattern.length());
            buffer.insert(start, replacement);
            string = new String(buffer);
            start = string.indexOf(pattern, start + replacement.length());
        }

        return string;
    }
    
    
    
}
