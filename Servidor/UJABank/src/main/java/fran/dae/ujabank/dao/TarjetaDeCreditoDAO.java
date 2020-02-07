/************************************************************
 ************************************************************
 *********** ESTE PROYECTO ESTA REALIZADO POR ***************
 ***********   FRANCISCO JESÚS NAJAS MARTOS   ***************
 ***********                                  ***************
 ***********  *USO LIBRERIAS DE CODIGO LIBRE* ***************
 ************************************************************
 ************************************************************/

package fran.dae.ujabank.dao;

import fran.dae.ujabank.entidades.TarjetaDeCredito;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author fran
 */

@Repository
public class TarjetaDeCreditoDAO {
    
    @PersistenceContext
    EntityManager em;

    public TarjetaDeCreditoDAO() {
    }
    
    @Transactional
    public void crear(TarjetaDeCredito tj) {
        try {
            em.persist(tj);
        } catch (Exception e) {
        }
    }
    
    @Transactional(propagation = Propagation.SUPPORTS)
    public TarjetaDeCredito buscar(String id) {
        return em.find(TarjetaDeCredito.class, id);
    }
}
