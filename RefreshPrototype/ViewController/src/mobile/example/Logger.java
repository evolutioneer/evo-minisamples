package mobile.example;

import com.sun.util.logging.Formatter;

import org.apache.commons.lang.StringUtils;

public class Logger {
    public Logger() {
        super();
    }
    
    /**
     *
     * @param msg
     */
    public static void log(String msg) {
        System.out.println(getInvocationDetails() + msg);
    }
    
    /**
     *
     * @param msg
     * @param e
     */
    public static void log(String msg, Exception e) {
        System.out.println(getInvocationDetails() + msg);
        e.printStackTrace();
    }
    
    /**
     *
     * @return
     */
    public static String getInvocationDetails() {
        
        String output = "";
        
        try {
            throw new RuntimeException("");
        }
        
        catch(RuntimeException e) {
            StackTraceElement[] stackTraceElements = e.getStackTrace();
            int i = 1;
            while (Logger.class.getName().equals(stackTraceElements[i].getClassName())) { i++; }

            int lineNumber = stackTraceElements[i].getLineNumber();
            String fileName = stackTraceElements[i].getFileName();
            String methodName = stackTraceElements[i].getMethodName();
            
            output = "--> [" + fileName + "//" + methodName + ", line " + lineNumber + ": ";
        }
        
        finally {
            return output;
        }
    }
}
