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
import java.util.Objects;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Margaux
 */
@Stateless
public class AlbumFacade extends AbstractFacade<Album> {
    @PersistenceContext(unitName = "BalloonPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void removeImage(int imageID, Album album)
    {        
        EntityManager entitymanager = getEntityManager();

        Image image = entitymanager.find(Image.class, imageID);

        album.removeImageAlbum(image);
        entitymanager.persist(album);
        entitymanager.remove(image);
    }
    
    public Album refreshAlbum(int albumid)
    {
        EntityManager entitymanager = getEntityManager();

        Album freshAlbum = entitymanager.find(Album.class, albumid);

        return freshAlbum;
    }
    
    public AlbumFacade() {
        super(Album.class);
    }
    
    public Album getAlbum(int albumID){
        EntityManager entitymanager = getEntityManager();
        return entitymanager.find(Album.class, albumID);
    }
    
    public boolean isAlbumOwner(String username, Album album) {
        try {
        int currentUserId = getCurrentUser(username).getIdUser();
        return album.getFkUser().getIdUser() == currentUserId;
        }
        catch(Exception e)
        {
            return false;
        }
    }
}
