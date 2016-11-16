package tags;



import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

/**
 * Created by Freemind on 2016-11-15.
 */
public class JodaTimeFormatter extends TagSupport{
    private Temporal time;
    private String format ="yy/MM/dd HH:mm:ss";
    private ZoneId zone=ZoneId.of("UTC");

    public ZoneId getZone() {
        return zone;
    }

    public void setZone(ZoneId zone) {
        this.zone = zone;
    }

    public void setTime(Temporal time){
        this.time=time;
    }
    public Temporal getTime(){
        return time;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public int doStartTag() throws JspException {


        String formattedTime= DateTimeFormatter.ofPattern(format).withZone(zone).format(time);
        try {
            pageContext.getOut().print(formattedTime);
        } catch (IOException e) {
            throw new JspException(e);
        }
        return SKIP_BODY;
    }
}


/*
import lombok.Setter;
import lombok.SneakyThrows;
import model.Gun;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Collection;

public class Catalog extends TagSupport {

    @Setter
    private Collection<Gun> guns;

    @Override
    @SneakyThrows
    public int doStartTag() throws JspException {
        pageContext.getOut().print(getGunList(guns));

        return SKIP_BODY;
    }

    public static String getGunList(Collection<Gun> guns) throws IOException {
        StringBuffer out = new StringBuffer();
        for (Gun gun: guns)
            out.append("<tr><td><a href=\"/buy/?id=")
                    .append(gun.getId())
                    .append("\">")
                    .append(gun.getName())
                    .append("</a></td><td>")
                    .append(gun.getCaliber())
                    .append("</td></tr>");

        return out.toString();
    }
}

*/
