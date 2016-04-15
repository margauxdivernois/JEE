/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
 
import entities.Album;
import entities.Image;

/**
 *
 * @author visinand
 */
public class TestPersistanceManytoOne {
    
    public static void main(String[] args) {
 
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("BalloonPU");
		EntityManager em = emf.createEntityManager();
		EntityTransaction txn = em.getTransaction();
 
                Album album = new Album();
                album.setAName("MyAlbum");
                
                txn.begin();
		em.persist(album);
		txn.commit();
                
		Image image = new Image();
                image.setIDescription("Une super description");
                image.setAlbum(album);
                

		txn.begin();
		em.persist(image);
		txn.commit();
                
                System.out.println(image);
 
	}
}