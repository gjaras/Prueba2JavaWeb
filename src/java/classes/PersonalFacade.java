/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author gabriel.jara
 */
@Stateless
public class PersonalFacade extends AbstractFacade<Personal> {

    @PersistenceContext(unitName = "Prueba2JavaWebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PersonalFacade() {
        super(Personal.class);
    }
    
}
