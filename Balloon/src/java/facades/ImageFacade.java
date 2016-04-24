/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.Album;
import entities.Image;
import entities.Love;
import entities.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    
    public Image getImage(int imageID){
        EntityManager entitymanager = getEntityManager();
        return entitymanager.find(Image.class, imageID);
    }
    
    public Album getAlbum(int albumID){
        EntityManager entitymanager = getEntityManager();
        return entitymanager.find(Album.class, albumID);
    }	
    
    public void love(Love love, String username) {
        EntityManager entityManager = getEntityManager();
        love.setFkUser(getCurrentUser(username));
        entityManager.persist(love);
    }
    
    public void unlove(Image image, String username) {
        EntityManager entityManager = getEntityManager();
        User currentUser = getCurrentUser(username);
        Love love = (Love) entityManager.createNamedQuery("Love.findByUserAndImage")
            .setParameter("fk_user", currentUser)
            .setParameter("fk_image", image)
            .getSingleResult();
        entityManager.remove(love);
        
        image.removeLove(love);
    }
    
    public boolean canLove(String username, Image image) {
        EntityManager entityManager = getEntityManager();
        User currentUser = getCurrentUser(username);

        List results = entityManager.createNamedQuery("Love.findByUserAndImage")
            .setParameter("fk_user", currentUser)
            .setParameter("fk_image", image)
            .getResultList();

        return results.isEmpty();
    }
    
    public boolean isImageOwner(String username, Image image) {
        User currentUser = getCurrentUser(username);
        return currentUser.getIdUser() == image.getFkAlbum().getFkUser().getIdUser();
    }
}
