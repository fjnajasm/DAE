/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fran.dae.ujabank.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.regex.Pattern;

/**
 *
 * @author fran
 */
public class Formato {
    
    private static final String DNI_REGEX = "[0-9]{7,8}[a-zA-Z]";
    private static final String TLF_REGEX = "[9|6|7][0-9]{8}";
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                      + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String NIF_REGEX = "[a-zA-Z][0-9]{8}";
    public static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    
    public static Pattern patronDNI = Pattern.compile(DNI_REGEX);
    public static Pattern patronNIF = Pattern.compile(NIF_REGEX);
    public static Pattern patronTLF = Pattern.compile(TLF_REGEX);
    public static Pattern patronEMAIL = Pattern.compile(EMAIL_REGEX);

    private static Date patronFECHA_1900;
    private static Date patronFECHA_MENOS18;
    
    public static boolean comprobarFechas(Date fechaDada) {
        try {
            patronFECHA_1900 = sdf.parse("1/1/1900");
            patronFECHA_MENOS18 = Date.from(Instant.now().minusSeconds(567648000));
            
            if(patronFECHA_1900.before(fechaDada))
                if(patronFECHA_MENOS18.after(fechaDada))
                    return true;
            
        } catch (ParseException e){
            
        }
        
        return false;
    }
            
            
}
