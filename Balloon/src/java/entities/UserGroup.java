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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Margaux
 */
@Entity
@Table(name = "user_group")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserGroup.findAll", query = "SELECT u FROM UserGroup u"),
    @NamedQuery(name = "UserGroup.findByIdUserGroup", query = "SELECT u FROM UserGroup u WHERE u.idUserGroup = :idUserGroup"),
    @NamedQuery(name = "UserGroup.findByFkGroup", query = "SELECT u FROM UserGroup u WHERE u.fkGroup = :fkGroup"),
    @NamedQuery(name = "UserGroup.findByFkUsername", query = "SELECT u FROM UserGroup u WHERE u.fkUsername = :fkUsername")})
public class UserGroup implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_user_group")
    private Integer idUserGroup;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "fk_group")
    private String fkGroup;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "fk_username")
    private String fkUsername;

    public UserGroup() {
    }

    public UserGroup(Integer idUserGroup) {
        this.idUserGroup = idUserGroup;
    }

    public UserGroup(Integer idUserGroup, String fkGroup, String fkUsername) {
        this.idUserGroup = idUserGroup;
        this.fkGroup = fkGroup;
        this.fkUsername = fkUsername;
    }

    public Integer getIdUserGroup() {
        return idUserGroup;
    }

    public void setIdUserGroup(Integer idUserGroup) {
        this.idUserGroup = idUserGroup;
    }

    public String getFkGroup() {
        return fkGroup;
    }

    public void setFkGroup(String fkGroup) {
        this.fkGroup = fkGroup;
    }

    public String getFkUsername() {
        return fkUsername;
    }

    public void setFkUsername(String fkUsername) {
        this.fkUsername = fkUsername;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUserGroup != null ? idUserGroup.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserGroup)) {
            return false;
        }
        UserGroup other = (UserGroup) object;
        if ((this.idUserGroup == null && other.idUserGroup != null) || (this.idUserGroup != null && !this.idUserGroup.equals(other.idUserGroup))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.UserGroup[ idUserGroup=" + idUserGroup + " fkUsername =" + fkUsername + " fkGroup=" + fkGroup + " ]";
    }
    
}
