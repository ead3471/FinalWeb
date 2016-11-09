import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Freemind on 2016-11-09.
 */
public class BashOrgParser {
    public static void main(String[] args) throws IOException {
        URL oracle = new URL("http://bash.im/forweb/?u");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(oracle.openStream()));

        StringBuilder builder=new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            builder.append(inputLine);
        in.close();


    }


}
