/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import controllers.util.JsfUtil;
import entities.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Margaux
 */
@Stateless
public class UserFacade extends AbstractFacade<User> {
    @PersistenceContext(unitName = "BalloonPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(User.class);
    }
    
    public User getUserFromUsername(String username)
    {
        try {
           List results = em.createNamedQuery("User.findByUUsername").setParameter("uUsername", username).getResultList();
           return (User) results.get(0);
        }
        catch(Exception e)
        {
            System.out.println("GET USER FROM USERNAME ERROR !");
            JsfUtil.addErrorMessage(e,"KO");
        }
        return null;
    }
    
}
