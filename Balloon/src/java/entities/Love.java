/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Margaux
 */
@Entity
@Table(name = "love")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Love.findAll", query = "SELECT l FROM Love l"),
    @NamedQuery(name = "Love.findByIdLove", query = "SELECT l FROM Love l WHERE l.idLove = :idLove"),
    @NamedQuery(name = "Love.findByFkImage", query = "SELECT l FROM Love l WHERE l.fkImage = :fkImage"),
    @NamedQuery(name = "Love.findByFkUser", query = "SELECT l FROM Love l WHERE l.fkUser = :fkUser"),
    @NamedQuery(name = "Love.findByLDate", query = "SELECT l FROM Love l WHERE l.lDate = :lDate")})
public class Love implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_love")
    private Integer idLove;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fk_image")
    private int fkImage;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fk_user")
    private int fkUser;
    @Basic(optional = false)
    @NotNull
    @Column(name = "l_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lDate;

    public Love() {
    }

    public Love(Integer idLove) {
        this.idLove = idLove;
    }

    public Love(Integer idLove, int fkImage, int fkUser, Date lDate) {
        this.idLove = idLove;
        this.fkImage = fkImage;
        this.fkUser = fkUser;
        this.lDate = lDate;
    }

    public Integer getIdLove() {
        return idLove;
    }

    public void setIdLove(Integer idLove) {
        this.idLove = idLove;
    }

    public int getFkImage() {
        return fkImage;
    }

    public void setFkImage(int fkImage) {
        this.fkImage = fkImage;
    }

    public int getFkUser() {
        return fkUser;
    }

    public void setFkUser(int fkUser) {
        this.fkUser = fkUser;
    }

    public Date getLDate() {
        return lDate;
    }

    public void setLDate(Date lDate) {
        this.lDate = lDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLove != null ? idLove.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Love)) {
            return false;
        }
        Love other = (Love) object;
        if ((this.idLove == null && other.idLove != null) || (this.idLove != null && !this.idLove.equals(other.idLove))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Love[ idLove=" + idLove + " ]";
    }
    
}
