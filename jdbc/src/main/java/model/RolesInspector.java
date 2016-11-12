package model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RolesInspector {
    private final HashMap<String,List<String>> usersRoles=new HashMap<>();

    public RolesInspector(Map <String,List<String>> usersRoles){
        this.usersRoles.putAll(usersRoles);
    }

    public boolean isUserRequestValid(User user, String requestString){
        List<String> userAccessedPaths=usersRoles.get(user.getRole());
        if(userAccessedPaths!=null)
        {
            for(String path:userAccessedPaths){
                if(requestString.matches(path)) return true;
            }
        }
        return false;
    }


    public static RolesInspector getMockRolesInspector(){

        Map<String,List<String>> rules=new HashMap<>();

        rules.put("admin", Arrays.asList(".*"));
        rules.put("worker",Arrays.asList("\\/userpages\\/worker.jsp","","\\/","\\/userpages\\/workerpages\\/.*"));
        rules.put("client",Arrays.asList("\\/userpages\\/client.jsp","","\\/"));
        rules.put("guest",Arrays.asList("\\/userpages\\/guestpage.jsp","","\\/"));
        return new RolesInspector(rules);




    }


}
