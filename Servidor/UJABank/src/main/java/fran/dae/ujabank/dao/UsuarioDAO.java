/** **********************************************************
 ************************************************************
 *********** ESTE PROYECTO ESTA REALIZADO POR ***************
 ***********   FRANCISCO JESÚS NAJAS MARTOS   ***************
 ***********                                  ***************
 ***********  *USO LIBRERIAS DE CODIGO LIBRE* ***************
 ************************************************************
 *********************************************************** */
package fran.dae.ujabank.dao;

import fran.dae.ujabank.entidades.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

/**
 *
 * @author fran
 */
@Repository
public class UsuarioDAO {

    @PersistenceContext
    EntityManager em;

    public UsuarioDAO() {
    }

    @Transactional
    public void crear(Usuario usuario) {
        try {
            em.persist(usuario);
        } catch (Exception e) {
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Usuario buscarUsuarioPorDNI(String dni) {
        return em.find(Usuario.class, dni);
    }

//    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
//    public Usuario buscarUsuarioPorToken(String token) {
//        try {
//            return (Usuario) em.createQuery("SELECT u FROM Usuario u where u.token = :token").
//                    setParameter("token", token).getSingleResult();
//        } catch (Exception e) {
//            throw new UsuarioNoRegistrado();
//        }
//    }

    @Transactional
    public void actualizar(Usuario usuario) {
        try {
            em.merge(usuario);
        } catch (Exception e) {
        }
    }

}
