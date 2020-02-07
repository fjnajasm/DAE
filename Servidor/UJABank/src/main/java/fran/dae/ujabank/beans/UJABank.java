/** **********************************************************
 ************************************************************
 *********** ESTE PROYECTO ESTA REALIZADO POR ***************
 ***********   FRANCISCO JESÚS NAJAS MARTOS   ***************
 ***********                                  ***************
 ***********  *USO LIBRERIAS DE CODIGO LIBRE* ***************
 ************************************************************
 *********************************************************** */
package fran.dae.ujabank.beans;

import fran.dae.ujabank.dao.CuentaBancariaDAO;
import fran.dae.ujabank.dao.MovimientoDAO;
import fran.dae.ujabank.dao.TarjetaDeCreditoDAO;
import fran.dae.ujabank.dao.UsuarioDAO;
import fran.dae.ujabank.dto.CuentaBancariaDTO;
import fran.dae.ujabank.dto.MovimientoDTO;
import fran.dae.ujabank.dto.UsuarioDTO;
import fran.dae.ujabank.entidades.CuentaBancaria;
import fran.dae.ujabank.entidades.Movimiento;
import fran.dae.ujabank.entidades.TarjetaDeCredito;
import fran.dae.ujabank.entidades.Usuario;
import fran.dae.ujabank.excepciones.CuentaBancariaInexistente;
import fran.dae.ujabank.excepciones.CuentasIguales;
import fran.dae.ujabank.excepciones.ImporteIncorrecto;
import fran.dae.ujabank.excepciones.Inaccesible;
import fran.dae.ujabank.excepciones.SaldoInsuficiente;
import fran.dae.ujabank.excepciones.TarjetaCreditoExistente;
import fran.dae.ujabank.excepciones.TarjetaCreditoInexistente;
import fran.dae.ujabank.excepciones.UsuarioNoRegistrado;
import fran.dae.ujabank.excepciones.UsuarioYaRegistrado;
import fran.dae.ujabank.interfaces.UJABankInterface;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;

/**
 *
 * @author fran
 */
@Repository
public class UJABank implements UJABankInterface {

    @Autowired
    UsuarioDAO usuarioDAO;

    @Autowired
    CuentaBancariaDAO cuentaBancariaDAO;

    @Autowired
    MovimientoDAO movimientoDAO;

    @Autowired
    TarjetaDeCreditoDAO tarjetaDeCreditoDAO;

    BCryptPasswordEncoder b = new BCryptPasswordEncoder();

    /**
     * @brief Constructor por defecto
     */
    public UJABank() {
    }

    /**
     * @param DNI
     * @brief Devuelve el token de usuario
     * @param password Password del usuario
     * @return
     */
    @Override
    public Boolean login(String DNI, String password) {

        Usuario usu = usuarioDAO.buscarUsuarioPorDNI(DNI);
        if (usu == null) {
            throw new UsuarioNoRegistrado();
        }
        if (!b.matches(password, usu.getPassword())) {
            throw new Inaccesible();
        }
        return true;
    }

    /**
     * @brief Método para registrar usuarios
     * @param usuarioDTO DTO de usuario que se manda
     * @throws UsuarioYaRegistrado si el usuario ya está en el sistema
     */
    @Transactional
    @Override
    public void registrarUsuario(UsuarioDTO usuarioDTO) {

        if (usuarioDAO.buscarUsuarioPorDNI(usuarioDTO.getDNI()) != null) {
            throw new UsuarioYaRegistrado();
        }

        usuarioDTO.setPassword(b.encode(usuarioDTO.getPassword()));
        Usuario usuario = Usuario.fromDTO(usuarioDTO);

        System.out.println(usuario.getPassword());

        usuarioDAO.crear(usuario);
    }

    /**
     * @brief Método para buscar el usuario
     * @param DNI DNI que se le pasa
     * @return DTO del usuario
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public UsuarioDTO buscarUsuario(String DNI) {

        Usuario usu = usuarioDAO.buscarUsuarioPorDNI(DNI);
        if (usu == null) {
            throw new UsuarioNoRegistrado();
        }

        UsuarioDTO usuDTO = usu.toDTO();

        return usuDTO;
    }

    /**
     * @brief Método para crear una cuenta bancaria
     * @param DNI DNI del usuario al que se le crea la cuenta
     * @return ID de la cuenta creada
     * @throws UsuarioNoRegistrado si el usuario no está en el sistema
     */
    @Transactional
    @Override
    public CuentaBancariaDTO crearCuentaNueva(String DNI) {

        Usuario usu = usuarioDAO.buscarUsuarioPorDNI(DNI);
        if (usu == null) {
            throw new UsuarioNoRegistrado();
        }

        CuentaBancaria c = usu.crearCuentaBancaria();
        cuentaBancariaDAO.crear(c);

        usu.anadirCuentaBancaria(c);

        CuentaBancariaDTO cDTO = c.toDTO();

        return cDTO;
    }

    /**
     * @param token
     * @brief Método para ingresar dinero en una cuenta
     * @param importe Importe a ingresar en la cuenta
     * @param idCuenta ID de la cuenta en la que ingresar el dinero
     * @param concepto Concepto que introduce el usuario al hacer el ingreso
     * @return DTO del movimiento creado
     * @throws CuentaBancariaInexistente si no existe la cuenta
     * @throws ImporteIncorrecto si el importe que se le pasa es menor que 0
     */
    @Transactional
    @Override
    public MovimientoDTO ingresoDinero(String token, Float importe, String idCuenta, String concepto) {

        CuentaBancaria cuentaBancaria = cuentaBancariaDAO.buscarCuentaBancaria(idCuenta);

        if (cuentaBancaria == null) {
            throw new CuentaBancariaInexistente();
        }

        if (!cuentaBancaria.getDNIUsuarioAsociado().equals(token)) {
            throw new Inaccesible();
        }

        if (importe <= 0) {
            throw new ImporteIncorrecto();
        }

        Movimiento m = cuentaBancaria.ingresarDinero(importe, concepto);

        movimientoDAO.crear(m);
        cuentaBancaria.anadirMovimiento(m);

        MovimientoDTO mDTO = m.toDTO();

        return mDTO;
    }

    /**
     * @param DNI
     * @brief Metodo para crear tarjetas de credito
     * @param numeroTarjeta cadena con el numero de la tarjeta
     * @param CVV cadena con el CVV
     * @param banco cadena con el nombre de la entidad bancaria
     * @param fechaCaducidad Fecha de caducidad de la tarjeta
     * @param nombreTitular Nombre del titular de la tarjeta
     * @return the TarjetaDeCredito
     */
    @Transactional
    @Override
    public TarjetaDeCredito crearTarjetaDeCredito(String DNI, String numeroTarjeta, String CVV,
            String banco, Date fechaCaducidad, String nombreTitular) {

        if (usuarioDAO.buscarUsuarioPorDNI(DNI) == null) {
            throw new UsuarioNoRegistrado();
        }

        TarjetaDeCredito tj = tarjetaDeCreditoDAO.buscar(numeroTarjeta);
        if (tj != null) {
            throw new TarjetaCreditoExistente();
        }

        tj = new TarjetaDeCredito(numeroTarjeta, fechaCaducidad, CVV, nombreTitular, banco);
        tarjetaDeCreditoDAO.crear(tj);

        return tj;
    }

    /**
     * @param dniUsuario
     * @brief Método para retirar UJACoins en una tarjeta de credito previamente
     * creada
     * @param idCuenta ID de la cuenta bancaria de la que se saca el dinero
     * @param importe Importe a retirar de la cuenta bancaria
     * @param tarjetaCredito Tarjeta de credito en la que se ingresa el dinero
     * @param concepto Concepto creado por el usuario al retirar el dinero
     * @throws CuentaBancariaInexistente si no existe la cuenta
     * @throws SaldoInsuficiente si al cuenta no tiene suficiente saldo
     * @throws ImporteIncorrecto si el importe que se le pasa es menor que 0
     * @return DTO de movimiento creado.
     */
    @Transactional
    @Override
    public MovimientoDTO retirarUJACoin(String dniUsuario, String idCuenta, float importe, String tarjetaCredito, String concepto) {

        CuentaBancaria cuentaBancaria = cuentaBancariaDAO.buscarCuentaBancaria(idCuenta);
        if (cuentaBancaria == null) {
            throw new CuentaBancariaInexistente();
        }

        TarjetaDeCredito tj = tarjetaDeCreditoDAO.buscar(tarjetaCredito);
        if (tj == null) {
            throw new TarjetaCreditoInexistente();
        }

        if (!cuentaBancaria.getDNIUsuarioAsociado().equals(dniUsuario)) {
            throw new Inaccesible();
        }

        if (cuentaBancaria.getSaldo() < importe) {
            throw new SaldoInsuficiente();
        }
        if (importe <= 0) {
            throw new ImporteIncorrecto();
        }

        Movimiento m = cuentaBancaria.retirarDinero(importe, concepto, tj.getNumeroTarjeta());

        movimientoDAO.crear(m);
        cuentaBancaria.anadirMovimiento(m);

        MovimientoDTO mDTO = m.toDTO();

        return mDTO;
    }

    /**
     * @param DNI
     * @brief Método para transferir dinero de una cuenta origen a una cuenta
     * destino
     * @param idCuentaE ID de la cuenta que realiza la transferencia
     * @param idCuentaR ID de la cuenta que recibe la transferencia
     * @param importe Importe a transferir
     * @param concepto Concepto que crea el usuario que hace la transferencia
     * @return Lista de 2 DTO de movimiento
     * @throws CuentasIguales si ambas cuentas son la misma
     * @throws CuentaBancariaInexistente si no existe alguna de las dos cuentas
     * @throws SaldoInsuficiente si la cuenta emisora no dispone de ese saldo
     * @throws ImporteIncorrecto si el importe es menor que 0
     */
    @Transactional
    @Override
    public List<MovimientoDTO> transferirUJACoin(String DNI, String idCuentaE, String idCuentaR, float importe, String concepto) {

        if (idCuentaE.equals(idCuentaR)) {
            throw new CuentasIguales();
        }

        CuentaBancaria cbe = cuentaBancariaDAO.buscarCuentaBancaria(idCuentaE);
        CuentaBancaria cbr = cuentaBancariaDAO.buscarCuentaBancaria(idCuentaR);

        if (cbe == null || cbr == null) {
            throw new CuentaBancariaInexistente();
        }

        if (!cbe.getDNIUsuarioAsociado().equals(DNI)) {
            throw new Inaccesible();
        }
        if (cbe.getSaldo() < importe) {
            throw new SaldoInsuficiente();
        }

        if (importe <= 0) {
            throw new ImporteIncorrecto();
        }

        List<Movimiento> movimientos = cbe.transferenciaBancaria(importe, concepto, cbr);
        List<MovimientoDTO> movimientosDTO = new ArrayList<>();

        for (Movimiento m : movimientos) {
            movimientoDAO.crear(m);
            movimientosDTO.add(m.toDTO());
        }

        cbe.anadirMovimiento(movimientos.get(0));
        cbr.anadirMovimiento(movimientos.get(1));

        return movimientosDTO;
    }

    /**
     * @brief Metodo que devuelve los movimientos de un usuario
     * @param DNIusuario DNI del usuario
     * @return Lista de DTO de movimientos del usuario
     * @throws UsuarioNoRegistrado si el usuario no esta en la APP
     * @throws CuentaBancariaInexistente si no tiene cuentas bancarias
     */
    @Transactional
    @Override
    public List<MovimientoDTO> consultarMovimientosUsuario(String DNIusuario) {

        Usuario usu = usuarioDAO.buscarUsuarioPorDNI(DNIusuario);
        if (usu == null) {
            throw new UsuarioNoRegistrado();
        }

        if (usu.getCuentasBancarias().isEmpty()) {
            throw new CuentaBancariaInexistente();
        }

        List<MovimientoDTO> res = new ArrayList<>();

        for (CuentaBancaria c : usu.getCuentasBancarias().values()) {
            for (Movimiento m : c.getMovimientosCuenta().values()) {
                res.add(m.toDTO());
            }
        }

        return res;
    }

    /**
     * @param DNI
     * @brief Metodo que devuelve los movimientos de una cuenta
     * @param idCuenta ID de la cuenta bancaria
     * @return Lista de DTO de movimientos de la cuenta bancaria
     * @throws CuentaBancariaInexistente si no existe la cuenta
     */
    @Transactional
    @Override
    public List<MovimientoDTO> consultarMovimientosCuenta(String DNI, String idCuenta) {
        CuentaBancaria cb = cuentaBancariaDAO.buscarCuentaBancaria(idCuenta);
        if (cb == null) {
            throw new CuentaBancariaInexistente();
        }

        if (!cb.getDNIUsuarioAsociado().equals(DNI)) {
            throw new Inaccesible();
        }

        List<MovimientoDTO> res = new ArrayList<>();

        for (Movimiento m : cb.getMovimientosCuenta().values()) {
            res.add(m.toDTO());
        }

        return res;
    }

    /**
     * @brief Metodo que devuelve una lista con las cuentas bancarias de un
     * usuario
     * @param DNIUsuario DNI del usuario en cuestión
     * @return Lista de DTOs de cuentas bancarias
     * @throws UsuarioNoRegistrado si el usuario no está en la APP
     */
    @Transactional
    @Override
    public List<CuentaBancariaDTO> consultarCuentasUsuario(String DNIUsuario) {

        Usuario usuario = usuarioDAO.buscarUsuarioPorDNI(DNIUsuario);
        if (usuario == null) {
            throw new UsuarioNoRegistrado();
        }

        List<CuentaBancariaDTO> res = new ArrayList<>();

        for (CuentaBancaria c : usuario.getCuentasBancarias().values()) {
            res.add(c.toDTO());
        }

        return res;
    }
    
   
}
