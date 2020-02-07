/************************************************************
 ************************************************************
 *********** ESTE PROYECTO ESTA REALIZADO POR ***************
 ***********   FRANCISCO JESÚS NAJAS MARTOS   ***************
 ***********                                  ***************
 ***********  *USO LIBRERIAS DE CODIGO LIBRE* ***************
 ************************************************************
 ************************************************************/

package fran.dae.ujabank.dto;

/**
 *
 * @author fran
 */
public class CuentaBancariaDTO {
    private String id;
    private float saldo;
    private String idUsuario;

    public CuentaBancariaDTO() {
    }

    public CuentaBancariaDTO(String id, float saldo, String idUsuario) {
        this.id = id;
        this.saldo = saldo;
        this.idUsuario = idUsuario;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the saldo
     */
    public float getSaldo() {
        return saldo;
    }

    /**
     * @param saldo the saldo to set
     */
    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }
    
    /**
     * @return the idUsuario 
     */
    public String getIdUsuario() {
        return idUsuario;
    }
    
    /**
     * @param idUsuario 
     */
    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    
    
}
