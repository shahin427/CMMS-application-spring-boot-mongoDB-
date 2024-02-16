package org.sayar.net.Security.Controller;

import org.sayar.net.Enumes.ResponseKeyWord;
import org.sayar.net.Model.ResponseModel.ResponseContent;
import org.sayar.net.Model.ResponseModel.ResponseError;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;

//@RestController
public class TokenErrorController implements ErrorController {

    private static final String PATH = "/ResponseError";

    @RequestMapping(method = RequestMethod.GET,value = PATH)
    public ResponseEntity<?> error(HttpServletResponse response) {


        ResponseError e = new ResponseError();
        e.setStatus(401);

        if(response.getStatus() == 401){
//            return  ResponseEntity.status(401).body(e);

            return new ResponseContent().sendErrorResponseEntity("UNAUTHORIZED(401)", ResponseKeyWord.SC_UNAUTHORIZED,401);
        }
        else if(response.getStatus()==403) {
            return new ResponseContent().sendErrorResponseEntity("ACESSDENIDE(403)", ResponseKeyWord.ACESSDENIDE,403);
        }else {
            return null;
        }

    }



    @Override
    public String getErrorPath() {
        return PATH;
    }




}