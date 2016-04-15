/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author stevevisinand
 */
@Entity
@Table(name = "user_album")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserAlbum.findAll", query = "SELECT u FROM UserAlbum u"),
    @NamedQuery(name = "UserAlbum.findByIdUserAlbum", query = "SELECT u FROM UserAlbum u WHERE u.idUserAlbum = :idUserAlbum"),
    @NamedQuery(name = "UserAlbum.findByFkPermission", query = "SELECT u FROM UserAlbum u WHERE u.fkPermission = :fkPermission")})
public class UserAlbum implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_user_album")
    private Integer idUserAlbum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fk_permission")
    private int fkPermission;
    @JoinColumn(name = "fk_album", referencedColumnName = "id_album")
    @ManyToOne(optional = false)
    private Album fkAlbum;
    @JoinColumn(name = "fk_user", referencedColumnName = "id_user")
    @ManyToOne(optional = false)
    private User fkUser;

    public UserAlbum() {
    }

    public UserAlbum(Integer idUserAlbum) {
        this.idUserAlbum = idUserAlbum;
    }

    public UserAlbum(Integer idUserAlbum, int fkPermission) {
        this.idUserAlbum = idUserAlbum;
        this.fkPermission = fkPermission;
    }

    public Integer getIdUserAlbum() {
        return idUserAlbum;
    }

    public void setIdUserAlbum(Integer idUserAlbum) {
        this.idUserAlbum = idUserAlbum;
    }

    public int getFkPermission() {
        return fkPermission;
    }

    public void setFkPermission(int fkPermission) {
        this.fkPermission = fkPermission;
    }

    public Album getFkAlbum() {
        return fkAlbum;
    }

    public void setFkAlbum(Album fkAlbum) {
        this.fkAlbum = fkAlbum;
    }

    public User getFkUser() {
        return fkUser;
    }

    public void setFkUser(User fkUser) {
        this.fkUser = fkUser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUserAlbum != null ? idUserAlbum.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserAlbum)) {
            return false;
        }
        UserAlbum other = (UserAlbum) object;
        if ((this.idUserAlbum == null && other.idUserAlbum != null) || (this.idUserAlbum != null && !this.idUserAlbum.equals(other.idUserAlbum))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.UserAlbum[ idUserAlbum=" + idUserAlbum + " ]";
    }
    
}
