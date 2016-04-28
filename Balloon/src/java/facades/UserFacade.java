/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import controllers.util.JsfUtil;
import entities.User;
import entities.UserGroup;
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
    
    public void createLinkedUserGroup(User user)
    {
        UserGroup userGroup = new UserGroup();
        userGroup.setFkUsername(user.getUUsername());
        userGroup.setFkGroup("User");
        System.out.println("CREATE LINKED USER GROUP "+userGroup);
        getEntityManager().persist(userGroup);
    }
    
    public void removeLinkedUserGroup(User user)
    {
        List results = getEntityManager().createNamedQuery("UserGroup.findByFkUsername").setParameter("fkUsername", user.getUUsername()).getResultList();       
        
        results.stream().forEach((result) -> {
            getEntityManager().remove(result);
        });
    }
    
}
