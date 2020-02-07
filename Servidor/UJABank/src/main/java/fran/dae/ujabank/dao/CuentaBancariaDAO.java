/** **********************************************************
 ************************************************************
 *********** ESTE PROYECTO ESTA REALIZADO POR ***************
 ***********   FRANCISCO JESÚS NAJAS MARTOS   ***************
 ***********                                  ***************
 ***********  *USO LIBRERIAS DE CODIGO LIBRE* ***************
 ************************************************************
 *********************************************************** */
package fran.dae.ujabank.dao;

import fran.dae.ujabank.entidades.CuentaBancaria;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;

/**
 *
 * @author fran
 */
@Repository
public class CuentaBancariaDAO {

    @PersistenceContext
    EntityManager em;

    public CuentaBancariaDAO() {
    }

    @Transactional
    public void crear(CuentaBancaria cb) {

        try {
            em.persist(cb);
        } catch (Exception e) {

        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public CuentaBancaria buscarCuentaBancaria(String id) {
        return em.find(CuentaBancaria.class, id);
    }

    @Transactional
    public void actualizar(CuentaBancaria cb) {
        try {
            em.merge(cb);
        } catch (Exception e) {

        }
    }

}
