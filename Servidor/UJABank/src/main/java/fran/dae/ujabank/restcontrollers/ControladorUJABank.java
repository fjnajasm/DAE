/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fran.dae.ujabank.restcontrollers;

import fran.dae.ujabank.beans.UJABank;
import fran.dae.ujabank.dto.CuentaBancariaDTO;
import fran.dae.ujabank.dto.MovimientoDTO;
import fran.dae.ujabank.dto.UsuarioDTO;
import fran.dae.ujabank.entidades.TarjetaDeCredito;
import fran.dae.ujabank.excepciones.CuentaBancariaInexistente;
import fran.dae.ujabank.excepciones.CuentasIguales;
import fran.dae.ujabank.excepciones.ImporteIncorrecto;
import fran.dae.ujabank.excepciones.Inaccesible;
import fran.dae.ujabank.excepciones.SaldoInsuficiente;
import fran.dae.ujabank.excepciones.TarjetaCreditoExistente;
import fran.dae.ujabank.excepciones.TarjetaCreditoInexistente;
import fran.dae.ujabank.excepciones.UsuarioNoRegistrado;
import fran.dae.ujabank.excepciones.UsuarioYaRegistrado;
import fran.dae.ujabank.excepciones.formato.DNIoNIFIncorrecto;
import fran.dae.ujabank.excepciones.formato.EmailIncorrecto;
import fran.dae.ujabank.excepciones.formato.FechaNacimientoIncorrecto;
import fran.dae.ujabank.excepciones.formato.TelefonoIncorrecto;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author fran
 */


@RestController
@RequestMapping("/ujabank")
public class ControladorUJABank {

    
    @Autowired
    UJABank sistema;

    @GetMapping(value = "")
    public ResponseEntity<String> login(@RequestParam String DNI, @RequestParam String password) {
        try {
            return new ResponseEntity(sistema.login(DNI, password), HttpStatus.OK);
        } catch (UsuarioNoRegistrado e) {
            return new ResponseEntity(false, HttpStatus.NOT_FOUND);
        } catch (Inaccesible i) {
            return new ResponseEntity(false, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(value = "/usuarios", consumes = "application/json")
    public ResponseEntity<Boolean> registrarUsuario(@RequestBody UsuarioDTO usuDTO) {
        try {
            
            sistema.registrarUsuario(usuDTO);

            return new ResponseEntity(true, HttpStatus.OK);
        } catch (DNIoNIFIncorrecto | EmailIncorrecto | FechaNacimientoIncorrecto | TelefonoIncorrecto e) {
            return new ResponseEntity(false, HttpStatus.CONFLICT);
        } catch (UsuarioYaRegistrado ee) {
            return new ResponseEntity(HttpStatus.IM_USED);
        }
    }

    @GetMapping(value = "/usuarios/{DNI}", produces = "application/json")
    public ResponseEntity<UsuarioDTO> buscarUsuario(@PathVariable String DNI) {
        try {
            return new ResponseEntity(sistema.buscarUsuario(DNI), HttpStatus.OK);
        } catch (UsuarioNoRegistrado e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    /**
     *
     * @param DNI
     * @return
     */
    @PostMapping(value = "/usuarios/{DNI}/cuentasbancarias")
    public ResponseEntity crearCuentaNueva(@PathVariable String DNI) {

        try {
            sistema.crearCuentaNueva(DNI);
            return new ResponseEntity(HttpStatus.OK);
        } catch (UsuarioNoRegistrado unr) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/usuarios/{DNI}/cuentasbancarias/{idCuenta}/ingreso", produces = "application/json")
    public ResponseEntity<MovimientoDTO> ingresoDinero(@PathVariable String DNI, @RequestParam Float importe,
            @PathVariable String idCuenta, @RequestParam String concepto) {

        try {

            return new ResponseEntity(sistema.ingresoDinero(DNI, importe, idCuenta, concepto), HttpStatus.OK);

        } catch (CuentaBancariaInexistente cbi) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } catch (ImporteIncorrecto ii) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } catch (Inaccesible i) {
            return new ResponseEntity(HttpStatus.LOCKED);
        }
    }

    @PostMapping(value = "/usuarios/{DNI}/tarjetas", produces = "application/json")
    public ResponseEntity<TarjetaDeCredito> anadirTarjeta(@PathVariable String DNI, @RequestParam String numeroTarjeta,
            @RequestParam String CVV, @RequestParam String banco,
            @RequestParam String fechaCaducidad, @RequestParam String nombreTitular) {

        try {
            Date fechaC = new Date(fechaCaducidad);
            return new ResponseEntity(sistema.crearTarjetaDeCredito(DNI, numeroTarjeta, CVV,
                    banco, fechaC, nombreTitular), HttpStatus.OK);

        } catch (TarjetaCreditoExistente tce) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        } catch (UsuarioNoRegistrado e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/usuarios/{DNI}/cuentasbancarias/{idCuenta}/retirada", produces = "application/json")
    public ResponseEntity<MovimientoDTO> retirarDinero(@PathVariable String DNI, @PathVariable String idCuenta,
            @RequestParam float importe, @RequestParam String tarjetaCredito,
            @RequestParam String concepto) {

        try {

            return new ResponseEntity(sistema.retirarUJACoin(DNI, idCuenta, importe, tarjetaCredito, concepto), HttpStatus.OK);

        } catch (CuentaBancariaInexistente | TarjetaCreditoInexistente cbi) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } catch (SaldoInsuficiente | ImporteIncorrecto si) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } catch (Inaccesible i) {
            return new ResponseEntity(HttpStatus.LOCKED);
        }
    }

    @PostMapping(value = "/usuarios/{DNI}/cuentasbancarias/{idCuentaE}/transferencia", produces = "application/json")
    public ResponseEntity<List<MovimientoDTO>> transferirUJACoin(@PathVariable String DNI, @PathVariable String idCuentaE,
            @RequestParam String idCuentaR, @RequestParam float importe, @RequestParam String concepto) {

        try {

            return new ResponseEntity(sistema.transferirUJACoin(DNI, idCuentaE, idCuentaR, importe, concepto), HttpStatus.OK);

        } catch (Inaccesible e) {
            return new ResponseEntity(HttpStatus.LOCKED);
        } catch (CuentasIguales | SaldoInsuficiente | ImporteIncorrecto c) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } catch (CuentaBancariaInexistente cbi) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/usuarios/{DNI}/cuentasbancarias", produces = "application/json")
    public ResponseEntity<List<String>> consultarCuentasBancarias(@PathVariable String DNI) {

        try {
            List<CuentaBancariaDTO> cuentas = sistema.consultarCuentasUsuario(DNI);
            List<String> numerosCuentas = new ArrayList();

            for (CuentaBancariaDTO c : cuentas) {
                numerosCuentas.add("ID Cuenta: " + c.getId() + " -------- Saldo: " + c.getSaldo() + " UJACoins");
            }
            

            return new ResponseEntity(numerosCuentas, HttpStatus.OK);
        } catch (UsuarioNoRegistrado e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/usuarios/{DNI}/cuentasbancarias/movimientos", produces = "application/json")
    public ResponseEntity<List<MovimientoDTO>> consultarMovimientosUsuario(@PathVariable String DNI) {

        try {
            return new ResponseEntity(sistema.consultarMovimientosUsuario(DNI), HttpStatus.OK);
        } catch (UsuarioNoRegistrado | CuentaBancariaInexistente e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/usuarios/{DNI}/cuentasbancarias/{cuentabancaria}/movimientos", produces = "application/json")
    public ResponseEntity<List<MovimientoDTO>> consultarMovimientosCuentaBancaria(@PathVariable String DNI, @PathVariable String cuentabancaria) {

        try {
            return new ResponseEntity(sistema.consultarMovimientosCuenta(DNI, cuentabancaria), HttpStatus.OK);
        } catch (CuentaBancariaInexistente i) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } catch (Inaccesible ii) {
            return new ResponseEntity(HttpStatus.LOCKED);
        }
    }

}
