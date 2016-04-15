/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author stevevisinand
 */
@Entity
@Table(name = "user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findByIdUser", query = "SELECT u FROM User u WHERE u.idUser = :idUser"),
    @NamedQuery(name = "User.findByUUsername", query = "SELECT u FROM User u WHERE u.uUsername = :uUsername"),
    @NamedQuery(name = "User.findByUEmail", query = "SELECT u FROM User u WHERE u.uEmail = :uEmail"),
    @NamedQuery(name = "User.findByUPassword", query = "SELECT u FROM User u WHERE u.uPassword = :uPassword"),
    @NamedQuery(name = "User.findByUIcon", query = "SELECT u FROM User u WHERE u.uIcon = :uIcon")})
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_user")
    private Integer idUser;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "u_username")
    private String uUsername;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 254)
    @Column(name = "u_email")
    private String uEmail;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "u_password")
    private String uPassword;
    @Size(max = 250)
    @Column(name = "u_icon")
    private String uIcon;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkUser")
    private Collection<Love> loveCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkUser")
    private Collection<UserAlbum> userAlbumCollection;

    public User() {
    }

    public User(Integer idUser) {
        this.idUser = idUser;
    }

    public User(Integer idUser, String uUsername, String uEmail, String uPassword) {
        this.idUser = idUser;
        this.uUsername = uUsername;
        this.uEmail = uEmail;
        this.uPassword = uPassword;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getUUsername() {
        return uUsername;
    }

    public void setUUsername(String uUsername) {
        this.uUsername = uUsername;
    }

    public String getUEmail() {
        return uEmail;
    }

    public void setUEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getUPassword() {
        return uPassword;
    }

    public void setUPassword(String uPassword) {
        this.uPassword = uPassword;
    }

    public String getUIcon() {
        return uIcon;
    }

    public void setUIcon(String uIcon) {
        this.uIcon = uIcon;
    }

    @XmlTransient
    public Collection<Love> getLoveCollection() {
        return loveCollection;
    }

    public void setLoveCollection(Collection<Love> loveCollection) {
        this.loveCollection = loveCollection;
    }

    @XmlTransient
    public Collection<UserAlbum> getUserAlbumCollection() {
        return userAlbumCollection;
    }

    public void setUserAlbumCollection(Collection<UserAlbum> userAlbumCollection) {
        this.userAlbumCollection = userAlbumCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUser != null ? idUser.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.idUser == null && other.idUser != null) || (this.idUser != null && !this.idUser.equals(other.idUser))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.User[ idUser=" + idUser + " ]";
    }
    
}
