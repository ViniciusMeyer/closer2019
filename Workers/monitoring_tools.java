/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Workers;

/**
 *
 * @author vinicius meyer
 */
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.Enumeration;

public class monitoring_tools {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException, SocketException, IOException, Exception {
        int i = 0;

        while (true) {
			
		 try {
                insertSql a = new insertSql(args[0]);
                a.insert(getIp(args[1]), getCpu(), getTotalPhysicalMemorySize(), getFreePhysicalMemorySize());
            } catch (Exception e) {
                System.err.println("MySQL Error : " + e.getMessage());
            }
    
            Thread.sleep(1000);

            i++;
        }
    }

    public static void printUsage() {
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        for (Method method : operatingSystemMXBean.getClass().getDeclaredMethods()) {
            method.setAccessible(true);
            if (method.getName().startsWith("")
                    && Modifier.isPublic(method.getModifiers())) {
                Object value;
                try {
                    value = method.invoke(operatingSystemMXBean);
                } catch (Exception e) {
                    value = e;
                } // try
                System.out.println(method.getName() + " = " + value);
            } // if
        } // for
    }

    public static String getTotalPhysicalMemorySize() {
        OperatingSystemMXBean operatingSystemMXBean2 = ManagementFactory.getOperatingSystemMXBean();
        String str = "";

        for (Method method : operatingSystemMXBean2.getClass().getDeclaredMethods()) {
            method.setAccessible(true);
            if (method.getName().startsWith("getTotalPhysicalMemorySize")
                    && Modifier.isPublic(method.getModifiers())) {
                Object value;
                try {
                    value = method.invoke(operatingSystemMXBean2);
                } catch (Exception e) {
                    value = e;
                } // try
                //System.out.println(method.getName() + " = " + value);

                str = String.valueOf(value);

            } // if
        } // for

        return str;

    }

    public static String getFreePhysicalMemorySize() {
        OperatingSystemMXBean operatingSystemMXBean2 = ManagementFactory.getOperatingSystemMXBean();
        String str = "";

        for (Method method : operatingSystemMXBean2.getClass().getDeclaredMethods()) {
            method.setAccessible(true);
            if (method.getName().startsWith("getFreePhysicalMemorySize")
                    && Modifier.isPublic(method.getModifiers())) {
                Object value;
                try {
                    value = method.invoke(operatingSystemMXBean2);
                } catch (Exception e) {
                    value = e;
                } // try
                //System.out.println(method.getName() + " = " + value);

                str = String.valueOf(value);

            } // if
        } // for

        return str;

    }

    public static String getCpu() {
        OperatingSystemMXBean operatingSystemMXBean2 = ManagementFactory.getOperatingSystemMXBean();
        String str = "";

        for (Method method : operatingSystemMXBean2.getClass().getDeclaredMethods()) {
            method.setAccessible(true);
            if (method.getName().startsWith("getSystem")
                    && Modifier.isPublic(method.getModifiers())) {
                Object value;
                try {
                    value = method.invoke(operatingSystemMXBean2);
                } catch (Exception e) {
                    value = e;
                } // try
                //System.out.println(method.getName() + " = " + value);

                str = String.valueOf(value);

            } // if
        } // for

        double amount = Double.parseDouble(str);
        amount = amount * 100;
        DecimalFormat formatter = new DecimalFormat("00");

        return formatter.format(amount);

    }

    public static String getIp(String interfaceName) throws SocketException {

        try {
            NetworkInterface networkInterface = NetworkInterface.getByName(interfaceName);
            Enumeration<InetAddress> inetAddress = networkInterface.getInetAddresses();
            InetAddress currentAddress;
            currentAddress = inetAddress.nextElement();

            while (inetAddress.hasMoreElements()) {
                currentAddress = inetAddress.nextElement();
                if (currentAddress instanceof Inet4Address && !currentAddress.isLoopbackAddress()) {
                    return currentAddress.toString().substring(1);
                }
            }
        } catch (Exception e) {
            return e.getMessage();
        }

        return "interface error";

    }

}
