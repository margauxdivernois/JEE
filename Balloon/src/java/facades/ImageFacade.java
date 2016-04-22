/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import controllers.util.JsfUtil;
import entities.Image;
import entities.Love;
import entities.User;
import java.util.Arrays;
import java.util.List;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import org.jboss.weld.context.http.HttpRequestContext;

/**
 *
 * @author stevevisinand
 */
@Stateless
public class ImageFacade extends AbstractFacade<Image> {
    @PersistenceContext(unitName = "BalloonPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ImageFacade() {
        super(Image.class);
    }
    
    public void love(Love love, String username)
    {
        EntityManager entityManager = getEntityManager();
        love.setFkUser(getCurrentUser(username));
        entityManager.persist(love);
    }
    
    public boolean canLove(String username, Image image)
    {
        EntityManager entityManager = getEntityManager();
        User currentUser = getCurrentUser(username);

        List results = entityManager.createNamedQuery("Love.findByUserAndImage")
            .setParameter("fk_user", currentUser)
            .setParameter("fk_image", image)
            .getResultList();

        System.out.println("RESULT ! "+results.isEmpty());
        return results.isEmpty();
    }
}
