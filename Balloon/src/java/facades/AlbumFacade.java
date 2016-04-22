/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.Album;
import entities.Image;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author stevevisinand
 */
@Stateless
public class AlbumFacade extends AbstractFacade<Album> {
    @PersistenceContext(unitName = "BalloonPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void removeImage(Image image, Album album)
    {
        System.out.print("adasdadsasd");
        
        EntityManager entitymanager = getEntityManager();
        entitymanager.getTransaction( ).begin( );

            
        album.removeImageAlbum(image);
        image.setFkAlbum(null);
      
        entitymanager.persist( album );
        entitymanager.persist( image );
        entitymanager.getTransaction( ).commit( );

        System.out.print("ASDASDASDAALDJKalkdjhaSDLKjahsdlkaJSDHlaksjdhlakSDJH");
        getEntityManager().remove(getEntityManager().merge(image));

    }
    
    
    public AlbumFacade() {
        super(Album.class);
    }
    
}
