/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import controllers.UserController;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.faces.context.FacesContext;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author stevevisinand
 */
@Entity
@Table(name = "album")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Album.findAll", query = "SELECT a FROM Album a"),
    @NamedQuery(name = "Album.findByIdAlbum", query = "SELECT a FROM Album a WHERE a.idAlbum = :idAlbum"),
    @NamedQuery(name = "Album.findByAName", query = "SELECT a FROM Album a WHERE a.aName = :aName"),
    @NamedQuery(name = "Album.findByApublicVisibility", query = "SELECT a FROM Album a WHERE a.apublicVisibility = :apublicVisibility"),
    @NamedQuery(name = "Album.findByAcreationDate", query = "SELECT a FROM Album a WHERE a.acreationDate = :acreationDate")})
public class Album implements Serializable {
    @JoinColumn(name = "fk_user", referencedColumnName = "id_user")
    @ManyToOne(optional = false)
    private User fkUser;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_album")
    private Integer idAlbum;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "a_name")
    private String aName;
    @Lob
    @Size(max = 65535)
    @Column(name = "a_description")
    private String aDescription;
    @Basic(optional = false)
    @NotNull
    @Column(name = "a_publicVisibility")
    private boolean apublicVisibility;
    @Column(name = "a_creationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date acreationDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkAlbum")
    private Collection<Image> imageCollection;

    public Album() {
        this.acreationDate = new Date();
    }

    public Album(Integer idAlbum) {
        this.idAlbum = idAlbum;
    }

    public Album(Integer idAlbum, String aName, boolean apublicVisibility) {
        this.idAlbum = idAlbum;
        this.aName = aName;
        this.apublicVisibility = apublicVisibility;
    }

    public boolean removeImageAlbum(Image image){
        return imageCollection.remove(image);
    }
    
    public Integer getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(Integer idAlbum) {
        this.idAlbum = idAlbum;
    }

    public String getAName() {
        return aName;
    }

    public void setAName(String aName) {
        this.aName = aName;
    }

    public String getADescription() {
        return aDescription;
    }

    public void setADescription(String aDescription) {
        this.aDescription = aDescription;
    }

    public boolean getApublicVisibility() {
        return apublicVisibility;
    }

    public void setApublicVisibility(boolean apublicVisibility) {
        this.apublicVisibility = apublicVisibility;
    }

    public Date getAcreationDate() {
        return acreationDate;
    }

    public void setAcreationDate(Date acreationDate) {
        this.acreationDate = acreationDate;
    }

    @XmlTransient
    public Collection<Image> getImageCollection() {
        return imageCollection;
    }

    public void setImageCollection(Collection<Image> imageCollection) {
        this.imageCollection = imageCollection;
    }
    
    public Image getRandomImage()
    {
        int max = this.imageCollection.size() - 1;
        if(max > 0)
        {
            Random r = new Random();
            int indice = r.nextInt(max + 1);
            return ((List<Image>)imageCollection).get(indice);
        }
        else
        {
            Image img = new Image();
            img.setIFilename("noPicture.jpg");
            return img;
        }
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAlbum != null ? idAlbum.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Album)) {
            return false;
        }
        Album other = (Album) object;
        if ((this.idAlbum == null && other.idAlbum != null) || (this.idAlbum != null && !this.idAlbum.equals(other.idAlbum))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Album[ idAlbum=" + idAlbum + " ]";
    }

    public User getFkUser() {
        return fkUser;
    }

    public void setFkUser(User fkUser) {
        this.fkUser = fkUser;
    }
    
}
