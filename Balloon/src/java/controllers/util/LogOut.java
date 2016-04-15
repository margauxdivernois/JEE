/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.util;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author nabil.ouerhani
 */
@ManagedBean
@SessionScoped
public class LogOut {

    /**
     * Creates a new instance of LogOut
     */
    
    public  LogOut() {
          

    }
    public String killSession() {
       
    FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    
     return "login";

    }
    
}
