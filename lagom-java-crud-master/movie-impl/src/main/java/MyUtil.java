import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MyUtil {
    public static void sendInfo(String str){
        try {
            System.out.println("start");
            String[] args1=new String[]{"python","SendMail.py"};
            Process pr=Runtime.getRuntime().exec(str);

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    pr.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            pr.waitFor();
            System.out.println("end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
