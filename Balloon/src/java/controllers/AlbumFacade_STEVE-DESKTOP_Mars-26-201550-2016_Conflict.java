/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import entities.Album;
import entities.Image;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Order;

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
    
    public AlbumFacade() {
        super(Album.class);
        
        EntityManager em = entityManagerFactory.createEntityManager();
        Image image = em.find((Class<Album>) Album.class, 111);
        System.out.println("Customer details for order 111 : " + order.());
        em.close();
        entityManagerFactory.close();
    }
    
}
