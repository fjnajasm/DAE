/************************************************************
 ************************************************************
 *********** ESTE PROYECTO ESTA REALIZADO POR ***************
 ***********   FRANCISCO JESÚS NAJAS MARTOS   ***************
 ***********                                  ***************
 ***********  *USO LIBRERIAS DE CODIGO LIBRE* ***************
 ************************************************************
 ************************************************************/

package fran.dae.ujabank.excepciones;

/**
 *
 * @author fran
 */
public class UsuarioNoRegistrado extends RuntimeException {

    private String usuario;
    
    public UsuarioNoRegistrado() {
    }
    
    public UsuarioNoRegistrado(String _usuario) {
        usuario = _usuario;
    }
    
}
