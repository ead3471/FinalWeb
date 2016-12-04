package controllers;

import dao.SpecialisationDao;
import dao.UserDao;
import dao.exceptions.DaoException;
import model.Specialisation;
import model.User;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@WebServlet("/profileEdit/")
public class ProfileEdit extends HttpServlet {

    private final Logger logger = LogManager.getLogger(ProfileEdit.class);
    private UserDao userDao;
    private SpecialisationDao specialisationDao;

    private final static String USER_PROFILE_EDIT_PAGE = "/userpages/workerpages/profileEdit.jsp";
    private final static String USER_PROFILE_URL = "/profile/";
    private final static String ERROR_PAGE = "/error.jsp";

    private final static String UPLOAD_DIRECTORY = "/files/user_avatars/";

    private static final int MEMORY_THRESHOLD = 1024 * 1024 * 1;  // 1MB
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 2; // 2MB
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 5; // 5MB

    private static String pathToFileStorage = "";

    @Override
    public void init() throws ServletException {
        userDao = (UserDao) getServletContext().getAttribute("userDao");
        specialisationDao = (SpecialisationDao) getServletContext().getAttribute("specDao");
        pathToFileStorage = (String) (getServletContext().getAttribute("filesStorage"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User sessionUser = (User) req.getSession().getAttribute("user");

        try {
            if (ServletFileUpload.isMultipartContent(req)) {
                processAvatarUploadRequest(req, sessionUser);
            } else {
                processEditProfileRequest(req, sessionUser);
            }
            resp.sendRedirect(USER_PROFILE_URL);
        } catch (Exception ex) {
            logger.warn("Error while process update user request ", ex);
            req.setAttribute("err_msg", "Error while process update user request " + ex);
            req.getRequestDispatcher(ERROR_PAGE).forward(req, resp);
        }
    }

    private void removeOldImageFile(String fileName) {
        if (!fileName.endsWith("default.png")) {
            File fileForRemove = new File(pathToFileStorage + File.separator + fileName);
            if (fileForRemove.exists()) {
                fileForRemove.delete();
            }
        }
    }

    private void processEditProfileRequest(HttpServletRequest req, User sessionUser) throws DaoException {
        String newUserName = Optional.ofNullable(req.getParameter("name")).orElse(sessionUser.getFullName());
        String newPassword = Optional.ofNullable(req.getParameter("password")).orElse(sessionUser.getPassword());
        userDao.updateUserLimited(sessionUser.getId(), newUserName, newPassword, sessionUser.getPhotoUrl());
        sessionUser.setFullName(newUserName);
        sessionUser.setPassword(newPassword);

        List<Specialisation> allSpecs=specialisationDao.getAll();
        List<Specialisation> userSpecs=specialisationDao.getByUserId(sessionUser.getId());

        Set<Specialisation> newUserSpecs=new HashSet<>();

        for(Specialisation spec:allSpecs){
            if(req.getParameter(spec.getId()+"")!=null){
                    newUserSpecs.add(spec);
            }

        }

    }

    private void processAvatarUploadRequest(HttpServletRequest request, User sessionUser) throws Exception {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(MAX_FILE_SIZE);
        upload.setSizeMax(MAX_REQUEST_SIZE);
        String uploadPath = pathToFileStorage + File.separator + UPLOAD_DIRECTORY;

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        String fileName = "";

        List<FileItem> formItems = upload.parseRequest(request);
        if (formItems != null && formItems.size() > 0) {
            for (FileItem item : formItems) {
                if (!item.isFormField() && item.getFieldName().equals("photo")) {
                    File storeFile = File.createTempFile("img", ".jpg", new File(uploadPath));
                    item.write(storeFile);
                    removeOldImageFile(sessionUser.getPhotoUrl());
                    request.setAttribute("msg", "Upload has been done successfully!");
                    String newUserPhotoUrl = UPLOAD_DIRECTORY + storeFile.getName();
                    userDao.updateUserLimited(sessionUser.getId(), sessionUser.getFullName(), sessionUser.getPassword(), newUserPhotoUrl);
                    sessionUser.setPhotoUrl(newUserPhotoUrl);
                }
            }
        }


    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<User> sessionUser = Optional.ofNullable((User) req.getSession().getAttribute("user"));

        if (sessionUser.isPresent()) {
            try {

                List<Specialisation> userSpecs = specialisationDao.getByUserId(sessionUser.get().getId());
                List<Specialisation> allSpecs=specialisationDao.getAll();
                req.setAttribute("userSpecs", userSpecs);
                req.setAttribute("allSpecs", allSpecs);
                req.getRequestDispatcher(USER_PROFILE_EDIT_PAGE).forward(req, resp);

            } catch (DaoException ex) {
                logger.warn("Error at load user info", ex);
                req.getRequestDispatcher(ERROR_PAGE).forward(req, resp);

            }
        }
    }


}
