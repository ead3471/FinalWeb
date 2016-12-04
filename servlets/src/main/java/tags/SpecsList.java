package tags;

import model.Specialisation;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.*;

public class SpecsList extends TagSupport {


    public static String getUserSpecificationsTree(Map<Specialisation,Collection<Specialisation>> specsTree){
        StringBuilder outStringBuilder=new StringBuilder("<ul class=\"specs_list\">");
        for(Specialisation rootSpec:specsTree.keySet()){
            outStringBuilder.append("<li><b>" + rootSpec.getName() + "</b></li>\n");
            outStringBuilder.append("<ul class=\"sub_specs_list>\">\n");
            Collection<Specialisation> subSpecs=specsTree.get(rootSpec);
            for(Specialisation subSpec:subSpecs){
                outStringBuilder.append("<li>" + subSpec.getName() + "</li>\n");
            }
            outStringBuilder.append("</ul>\n");
        }
        outStringBuilder.append("</ul>\n");

        return outStringBuilder.toString();
    }


    public static String getAllSpecsListWithCheckedUserSpecs(List<Specialisation> userSpecialisations, List<Specialisation> allSpecialisations) {
        Map<Specialisation, List<Specialisation>> specialisationListMap = getSpecialisationMap(allSpecialisations);
        StringBuilder outBuilder = new StringBuilder();

        outBuilder.append("<ul class=\"specs_list\">");
        for (Specialisation spec : specialisationListMap.keySet()) {
            outBuilder.append("<li class=\"specs_list\">").append("<b>").append(spec.getName()).append("</b></li>");
            outBuilder.append("<ul class=\"sub_specs_list>\">");
            for (Specialisation subSpec : specialisationListMap.get(spec)) {
                outBuilder.append("<li>").append(subSpec.getName()).append("<input name=" + subSpec.getId() + " type=\"checkbox\" ");
                if (userSpecialisations.contains(subSpec)) {
                    outBuilder.append(" checked");
                }
                outBuilder.append(">");
            }

            outBuilder.append("</ul>");

        }
        outBuilder.append("</ul>");
        return outBuilder.toString();
    }

    private static Map<Specialisation, List<Specialisation>> getSpecialisationMap(List<Specialisation> specialisationsList) {
        Map<Specialisation, List<Specialisation>> resultMap = new HashMap<>();
        Map<Integer, List<Specialisation>> subSpecMap = new HashMap<>();
        for (Specialisation spec : specialisationsList) {
            if (spec.getRoot() == 0) {
                resultMap.put(spec, new ArrayList<>());
            } else {
                if (subSpecMap.containsKey(spec.getRoot())) {
                    subSpecMap.get(spec.getRoot()).add(spec);
                } else {
                    ArrayList<Specialisation> specsList = new ArrayList<>();
                    specsList.add(spec);
                    subSpecMap.put(spec.getRoot(), specsList);
                }
            }
        }
        for (Specialisation rootSpec : resultMap.keySet()) {
            resultMap.put(rootSpec, subSpecMap.get(rootSpec.getId()));
        }
        return resultMap;
    }

}
