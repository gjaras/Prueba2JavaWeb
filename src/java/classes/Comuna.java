/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
 * @author gabriel.jara
 */
@Entity
@Table(name = "comuna")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Comuna.findAll", query = "SELECT c FROM Comuna c")
    , @NamedQuery(name = "Comuna.findByCodComuna", query = "SELECT c FROM Comuna c WHERE c.codComuna = :codComuna")
    , @NamedQuery(name = "Comuna.findByNombre", query = "SELECT c FROM Comuna c WHERE c.nombre = :nombre")})
public class Comuna implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_comuna")
    private Integer codComuna;
    @Size(max = 30)
    @Column(name = "nombre")
    private String nombre;
    @JoinColumn(name = "codregion", referencedColumnName = "cod_region")
    @ManyToOne
    private Region codregion;
    @OneToMany(mappedBy = "codcomuna")
    private Collection<Personal> personalCollection;

    public Comuna() {
    }

    public Comuna(Integer codComuna) {
        this.codComuna = codComuna;
    }

    public Integer getCodComuna() {
        return codComuna;
    }

    public void setCodComuna(Integer codComuna) {
        this.codComuna = codComuna;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Region getCodregion() {
        return codregion;
    }

    public void setCodregion(Region codregion) {
        this.codregion = codregion;
    }

    @XmlTransient
    public Collection<Personal> getPersonalCollection() {
        return personalCollection;
    }

    public void setPersonalCollection(Collection<Personal> personalCollection) {
        this.personalCollection = personalCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codComuna != null ? codComuna.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Comuna)) {
            return false;
        }
        Comuna other = (Comuna) object;
        if ((this.codComuna == null && other.codComuna != null) || (this.codComuna != null && !this.codComuna.equals(other.codComuna))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "classes.Comuna[ codComuna=" + codComuna + " ]";
    }
    
}
