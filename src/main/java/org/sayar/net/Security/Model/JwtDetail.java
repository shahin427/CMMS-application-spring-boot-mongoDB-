package org.sayar.net.Security.Model;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

public class JwtDetail extends WebAuthenticationDetails {

    private long organId;


    public JwtDetail(HttpServletRequest request, long organId) {
        super(request);
        this.organId = organId;
    }

    public long getOrganId() {
        return organId;
    }

    public void setOrganId(long organId) {
        this.organId = organId;
    }
}
