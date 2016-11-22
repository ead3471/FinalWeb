package tags;

import model.Specialisation;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpecsList extends TagSupport {
    private List<Specialisation> specsList;


    public List<Specialisation> getSpecsList() {
        return specsList;
    }

    public void setSpecsList(List<Specialisation> specsList) {
        this.specsList = specsList;
    }

    @Override
    public int doStartTag() throws JspException {
        JspWriter writer = pageContext.getOut();

        Map<Specialisation, List<Specialisation>> specsMap = getSpecialisationMap(specsList);


        try {
            writer.write("<ul class=\"specs_list\">");
            for (Specialisation spec : specsMap.keySet()) {

                writer.write("<li><b>" + spec.getName() + "</b></li>");

                writer.write("<ul class=\"sub_specs_list>\">");
                for (Specialisation subSpec : specsMap.get(spec)) {
                    writer.write("<li>" + subSpec.getName() + "</li>");
                }
                writer.write("</ul>");

            }

            writer.write("</ul>");

        } catch (IOException e) {
            throw new JspException(e);
        }

        return SKIP_BODY;
    }

    private Map<Specialisation, List<Specialisation>> getSpecialisationMap(List<Specialisation> specialisationsList) {
        Map<Specialisation, List<Specialisation>> resultMap = new HashMap<>();
        Map<Integer, List<Specialisation>> subSpecMap = new HashMap<>();
        for (Specialisation spec : specialisationsList) {
            if (spec.getRoot() == 0) {
                resultMap.put(spec, new ArrayList<>());
            } else {
                if (subSpecMap.containsKey(spec.getRoot())) {
                    subSpecMap.get(spec.getRoot()).add(spec);
                }
                else{
                    ArrayList<Specialisation> specsList=new ArrayList<>();
                    specsList.add(spec);
                    subSpecMap.put(spec.getRoot(),specsList);
                }
            }
        }
        for (Specialisation rootSpec : resultMap.keySet()) {
            resultMap.put(rootSpec, subSpecMap.get(rootSpec.getId()));
        }
        return resultMap;
    }

}
