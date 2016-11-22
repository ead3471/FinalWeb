package controllers;

import dao.SpecialisationDao;
import dao.UserDao;
import dao.exceptions.DaoException;
import model.Specialisation;
import model.User;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;


@WebServlet("/profileEdit/")
public class ProfileEdit extends HttpServlet {

    private final Logger logger= LogManager.getLogger(ProfileEdit.class);
    private UserDao userDao;
    private SpecialisationDao specialisationDao;

    private final static String USER_PROFILE_EDIT_PAGE="/userpages/workerpages/profileEdit.jsp";
    private final static String USER_PROFILE_URL="/profile/";
    private final static String ERROR_PAGE="/error.jsp";

    private final static String UPLOAD_DIRECTORY="/user_avatars/";

    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 1;  // 1MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 5; // 2MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 6; // 5MB

    private static String pathToFileStorage="";

    @Override
    public void init() throws ServletException {
        userDao=(UserDao) getServletContext().getAttribute("userDao");
        specialisationDao=(SpecialisationDao)getServletContext().getAttribute("specDao");
        pathToFileStorage=(String)(getServletContext().getAttribute("filesStorage"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       User sessionUser=(User)req.getSession().getAttribute("user");
        String newUserName=Optional.ofNullable(req.getParameter("name")).orElse(sessionUser.getFullName());
        String newPassword=Optional.ofNullable(req.getParameter("password1")).orElse(sessionUser.getPassword());


        DiskFileItemFactory factory = new DiskFileItemFactory();
        // sets memory threshold - beyond which files are stored in disk
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // sets temporary location to store files
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);

        upload.setFileSizeMax(MAX_FILE_SIZE);

        upload.setSizeMax(MAX_REQUEST_SIZE);

        String uploadPath = pathToFileStorage+ File.separator + UPLOAD_DIRECTORY;


        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        String fileName="";
        try {
            List<FileItem> formItems = upload.parseRequest(req);
            if (formItems != null && formItems.size() > 0) {
                for (FileItem item : formItems) {
                    if (!item.isFormField()) {
                        fileName = new File(item.getName()).getName();
                        String filePath = uploadPath + File.separator + fileName;
                        // File.createTempFile(String prefix, String suffix, File directory)
                       // File storeFile = new File(filePath);
                        File storeFile = File.createTempFile("img",".jpg",new File(uploadPath));
                        item.write(storeFile);

                        removeOldImageFile(sessionUser.getPhotoUrl());
                        sessionUser.setPhotoUrl("/files/"+UPLOAD_DIRECTORY+storeFile.getName());
                        req.setAttribute("message",
                                "Upload has been done successfully!");
                    }

                }
            }
        } catch (Exception ex) {
            req.setAttribute("message",
                    "There was an error: " + ex.getMessage());
        }


        try {
            userDao.updateUserLimited(sessionUser.getId(),newUserName,newPassword,"/files/user_avatars/"+fileName);
        } catch (DaoException e) {
            e.printStackTrace();
        }


        resp.sendRedirect(USER_PROFILE_URL);
    }

    private void removeOldImageFile(String fileName){
        if(!fileName.endsWith("default.png")){
            File fileForRemove=new File(pathToFileStorage+File.separator+fileName.replaceFirst("/files/","") );
            if(fileForRemove.exists()){
                fileForRemove.delete();
            }
        }
    }



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<User> sessionUser = Optional.ofNullable((User) req.getSession().getAttribute("user"));

        if(sessionUser.isPresent()){
            try{

                List<Specialisation> userSpecs = specialisationDao.getByUserId(sessionUser.get().getId());
                req.setAttribute("userSpecs", userSpecs);
                req.getRequestDispatcher(USER_PROFILE_EDIT_PAGE).forward(req, resp);

            }
            catch(DaoException ex){
                logger.warn("Error at load user info", ex);
                req.getRequestDispatcher(ERROR_PAGE).forward(req, resp);

            }
        }

        else{
            //TODO:??????? what can i do if user not present& its impossible by security, but...

        }
    }


}
