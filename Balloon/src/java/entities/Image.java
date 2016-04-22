/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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
@Table(name = "image")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Image.findAll", query = "SELECT i FROM Image i"),
    @NamedQuery(name = "Image.findByIdImage", query = "SELECT i FROM Image i WHERE i.idImage = :idImage"),
    @NamedQuery(name = "Image.findByIName", query = "SELECT i FROM Image i WHERE i.iName = :iName"),
    @NamedQuery(name = "Image.findByIFilename", query = "SELECT i FROM Image i WHERE i.iFilename = :iFilename"),
    @NamedQuery(name = "Image.findByIdateCapture", query = "SELECT i FROM Image i WHERE i.idateCapture = :idateCapture"),
    @NamedQuery(name = "Image.findByIdateUpload", query = "SELECT i FROM Image i WHERE i.idateUpload = :idateUpload"),
    @NamedQuery(name = "Image.findByILatititude", query = "SELECT i FROM Image i WHERE i.iLatititude = :iLatititude"),
    @NamedQuery(name = "Image.findByILongitude", query = "SELECT i FROM Image i WHERE i.iLongitude = :iLongitude"),
    @NamedQuery(name = "Image.findByICamera", query = "SELECT i FROM Image i WHERE i.iCamera = :iCamera")})
public class Image implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_image")
    private Integer idImage;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "i_name")
    private String iName;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "i_filename")
    private String iFilename;
    
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "i_description")
    private String iDescription;
    
    @Basic(optional = true)
    @Column(name = "i_dateCapture")
    @Temporal(TemporalType.TIMESTAMP)
    private Date idateCapture;
    
    @Basic(optional = true)
    @Column(name = "i_dateUpload")
    @Temporal(TemporalType.TIMESTAMP)
    private Date idateUpload;
    
    @Basic(optional = true)
    @Size(min = 0, max = 65535)
    @Column(name = "i_copyright")
    private String iCopyright;
    
    @Basic(optional = true)
    @Column(name = "i_latititude")
    private double iLatititude;
    
    @Basic(optional = true)
    @Column(name = "i_longitude")
    private double iLongitude;
    
    @Basic(optional = true)
    @Size(min = 1, max = 350)
    @Column(name = "i_camera")
    private String iCamera;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkImage")
    private Collection<Love> loveCollection;
    @JoinColumn(name = "fk_album", referencedColumnName = "id_album")
    @ManyToOne(optional = false)
    private Album fkAlbum;

    public Image() {
        this.idateUpload = new Date();
    }

    public Image(Integer idImage) {
        this.idImage = idImage;
    }

    public Image(Integer idImage, String iName, String iFilename, String iDescription, Date idateCapture, Date idateUpload, String iCopyright, double iLatititude, double iLongitude, String iCamera) {
        this.idImage = idImage;
        this.iName = iName;
        this.iFilename = iFilename;
        this.iDescription = iDescription;
        this.idateCapture = idateCapture;
        this.idateUpload = idateUpload;
        this.iCopyright = iCopyright;
        this.iLatititude = iLatititude;
        this.iLongitude = iLongitude;
        this.iCamera = iCamera;
    }

    public Integer getIdImage() {
        return idImage;
    }

    public void setIdImage(Integer idImage) {
        this.idImage = idImage;
    }

    public String getIName() {
        return iName;
    }

    public void setIName(String iName) {
        this.iName = iName;
    }

    public String getIFilename() {
        return iFilename;
    }

    public void setIFilename(String iFilename) {
        this.iFilename = iFilename;
    }

    public String getIDescription() {
        return iDescription;
    }

    public void setIDescription(String iDescription) {
        this.iDescription = iDescription;
    }

    public Date getIdateCapture() {
        return idateCapture;
    }

    public void setIdateCapture(Date idateCapture) {
        this.idateCapture = idateCapture;
    }

    public Date getIdateUpload() {
        return idateUpload;
    }

    public void setIdateUpload(Date idateUpload) {
        this.idateUpload = idateUpload;
    }

    public String getICopyright() {
        return iCopyright;
    }

    public void setICopyright(String iCopyright) {
        this.iCopyright = iCopyright;
    }

    public double getILatititude() {
        return iLatititude;
    }

    public void setILatititude(double iLatititude) {
        this.iLatititude = iLatititude;
    }

    public double getILongitude() {
        return iLongitude;
    }

    public void setILongitude(double iLongitude) {
        this.iLongitude = iLongitude;
    }

    public String getICamera() {
        return iCamera;
    }

    public void setICamera(String iCamera) {
        this.iCamera = iCamera;
    }

    @XmlTransient
    public Collection<Love> getLoveCollection() {
        return loveCollection;
    }

    public void setLoveCollection(Collection<Love> loveCollection) {
        this.loveCollection = loveCollection;
    }

    public Album getFkAlbum() {
        return fkAlbum;
    }

    public void setFkAlbum(Album fkAlbum) {
        this.fkAlbum = fkAlbum;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idImage != null ? idImage.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Image)) {
            return false;
        }
        Image other = (Image) object;
        if ((this.idImage == null && other.idImage != null) || (this.idImage != null && !this.idImage.equals(other.idImage))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Image[ idImage=" + idImage + " ]";
    }
    
}
