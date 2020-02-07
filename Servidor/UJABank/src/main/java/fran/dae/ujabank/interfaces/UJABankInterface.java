/************************************************************
 ************************************************************
 *********** ESTE PROYECTO ESTA REALIZADO POR ***************
 ***********   FRANCISCO JESÚS NAJAS MARTOS   ***************
 ***********                                  ***************
 ***********  *USO LIBRERIAS DE CODIGO LIBRE* ***************
 ************************************************************
 ************************************************************/

package fran.dae.ujabank.interfaces;

import fran.dae.ujabank.dto.CuentaBancariaDTO;
import fran.dae.ujabank.dto.MovimientoDTO;
import fran.dae.ujabank.dto.UsuarioDTO;
import fran.dae.ujabank.entidades.TarjetaDeCredito;
import java.util.Date;
import java.util.List;

/**
 *
 * @author fran
 */
public interface UJABankInterface {
    public void registrarUsuario(UsuarioDTO usuarioDTO);
    public UsuarioDTO buscarUsuario(String DNI);
    //public boolean loginUsuario(String id, String pw);
    public CuentaBancariaDTO crearCuentaNueva(String DNI);
    public TarjetaDeCredito crearTarjetaDeCredito(String token, String numeroTarjeta, String CVV, String banco, Date fechaCaducidad, String nombreTitular);
    public MovimientoDTO ingresoDinero(String dniUsuario, Float importe, String idCuenta, String concepto);
    public List<MovimientoDTO> transferirUJACoin(String DNI, String idCuentaE, String idCuentaR, float importe, String concepto);
    public MovimientoDTO retirarUJACoin(String dniUsuario, String idCuenta, float importe, String tarjetaCredito, String concepto);
    public List<MovimientoDTO> consultarMovimientosUsuario(String DNIusuario);
    public List<MovimientoDTO> consultarMovimientosCuenta(String DNI, String idCuenta);
    public List<CuentaBancariaDTO> consultarCuentasUsuario(String DNIUsuario);
    public Boolean login(String usuario, String password);
    
}
