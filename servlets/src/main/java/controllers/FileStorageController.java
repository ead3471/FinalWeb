package controllers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;

@WebServlet("/files/*")
public class FileStorageController extends HttpServlet {

   private  String pathToFileStorage ="";


    @Override
    public void init() throws ServletException {
        pathToFileStorage = (String) getServletContext().getAttribute("filesStorage") ;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException
        {
            String filename = URLDecoder.decode(request.getPathInfo().substring(1), "UTF-8");
            File file = new File(pathToFileStorage, filename);
            if(file.exists()){
            response.setHeader("Content-Type", getServletContext().getMimeType(filename));
            response.setHeader("Content-Length", String.valueOf(file.length()));
            response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
            Files.copy(file.toPath(), response.getOutputStream());}
            else
            {
                response.sendError(404);
            }
        }

}
