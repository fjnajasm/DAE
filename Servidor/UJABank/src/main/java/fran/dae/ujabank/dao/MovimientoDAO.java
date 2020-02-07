/************************************************************
 ************************************************************
 *********** ESTE PROYECTO ESTA REALIZADO POR ***************
 ***********   FRANCISCO JESÚS NAJAS MARTOS   ***************
 ***********                                  ***************
 ***********  *USO LIBRERIAS DE CODIGO LIBRE* ***************
 ************************************************************
 ************************************************************/

package fran.dae.ujabank.dao;

import fran.dae.ujabank.entidades.Movimiento;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;

/**
 *
 * @author fran
 */

@Repository
public class MovimientoDAO {
    
    @PersistenceContext
    EntityManager em;

    public MovimientoDAO() {
    }
   
    @Transactional
    public void crear(Movimiento movimiento) {
        
        try {
            em.persist(movimiento);
        } catch (Exception e) {
        }
    }
    
}
