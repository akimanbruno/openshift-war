/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.laisa.vanup.ws;

import com.laisa.vanup.ws.consts.Consts;
import com.laisa.vanup.ws.dao.DriverDao;
import com.laisa.vanup.ws.dao.PassengerDao;
import com.laisa.vanup.ws.dao.ServiceDao;
import com.laisa.vanup.ws.dao.TripDao;
import com.laisa.vanup.ws.dao.VanDao;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author FLY
 */
@WebService(serviceName = "VUWS")
public class VUWS {
    

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "VanUpWeb: Hello " + txt + " !";
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "login")
    public String login(@WebParam(name = "tipo") String tipo, @WebParam(name = "usuario") String usuario, @WebParam(name = "senha") String senha) {
        //TODO write your implementation code here:
        try {
            if (tipo.equals(Consts.DriverType)) {
                // Motorista
                return DriverDao.Validate(usuario, senha);
            } else 
                if (tipo.equals(Consts.PassengerType)) {
                    // Passageiro
                    return PassengerDao.Validate(usuario, senha);
                } else {
                    return Consts.FAIL + Consts.br + "Falha ao autenticar, tipo incorreto." + tipo ;
                }
        } catch (Exception e)  {
            return Consts.ERROR + Consts.br + e.toString();
        }
        
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "createdriver")
    public String createdriver(@WebParam(name = "email") String email, @WebParam(name = "senha") String senha, @WebParam(name = "nome") String nome, @WebParam(name = "cnh") String cnh) {
        return DriverDao.createDriver(email, senha, nome, cnh);
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "createpassenger")
    public String createpassenger(@WebParam(name = "email") String email, @WebParam(name = "senha") String senha, @WebParam(name = "nome") String nome, @WebParam(name = "fone") String fone) {
        return PassengerDao.createPassenger(email, senha, nome, fone);
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "createvan")
    public String createvan(@WebParam(name = "placa") String placa, @WebParam(name = "cor") String cor, @WebParam(name = "marca") String marca, @WebParam(name = "idproprietario") String idproprietario) {
        return VanDao.createVan(placa.toUpperCase(), cor.toUpperCase(), marca.toUpperCase(), idproprietario);
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "listvan")
    public String listvan(@WebParam(name = "idproprietario") String idproprietario) {
        return VanDao.listVan(idproprietario);
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "createtrip")
    public String createtrip(@WebParam(name = "idvan") String idvan, @WebParam(name = "idmotorista") String idmotorista, @WebParam(name = "desricao") String desricao) {
        return TripDao.createTrip(idvan, idmotorista, desricao.toUpperCase());
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "listtrip")
    public String listtrip(@WebParam(name = "idmotorista") String idmotorista) {
        return TripDao.listTrip(idmotorista);
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "starttrip")
    public String starttrip(@WebParam(name = "idtrip") String idtrip) {
        return TripDao.startTrip(idtrip);
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "saveposition")
    public String saveposition(@WebParam(name = "idtrip") String idtrip, @WebParam(name = "lat") String lat, @WebParam(name = "lon") String lon, @WebParam(name = "vel") String vel, @WebParam(name = "dttime") String dttime) {
        return TripDao.savePosition(idtrip, lat, lon, vel, dttime);
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "tripCheck1")
    public String tripCheck1(@WebParam(name = "idtrip") String idtrip) {
        return TripDao.tripCheck1(idtrip);
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "tripreturn")
    public String tripreturn(@WebParam(name = "idtrip") String idtrip) {
        return TripDao.tripReturn(idtrip);
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "tripfinalize")
    public String tripfinalize(@WebParam(name = "idtrip") String idtrip) {
        return TripDao.tripFinalize(idtrip);
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "listtripactive")
    public String listtripactive() {
        return Consts.ERROR + Consts.br + "Função subtituida por listtripactive2 ";
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "listtripactive2")
    public String listtripactive2(@WebParam(name = "idpassageiro") String idpassageiro) {
        return TripDao.listTripActive(idpassageiro);
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "tripcheckin")
    public String tripcheckin(@WebParam(name = "idtrip") String idtrip, @WebParam(name = "idpassageiro") String idpassageiro) {
        return TripDao.tripCheckIn(idtrip, idpassageiro);
    }

    /**
     * Operação de Web service
     
    @WebMethod(operationName = "tripcheckuot")
    public String tripcheckuot(@WebParam(name = "idtrip") String idtrip, @WebParam(name = "idpassageiro") String idpassageiro) {
        return TripDao.tripPassengerChange(idtrip, idpassageiro, "-1");
    }
    */

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "tripcheckout")
    public String tripcheckout(@WebParam(name = "idtrip") String idtrip, @WebParam(name = "idpassageiro") String idpassageiro) {
        return TripDao.tripPassengerChange(idtrip, idpassageiro, "-1");
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "tripcheckreturn")
    public String tripcheckreturn(@WebParam(name = "idtrip") String idtrip, @WebParam(name = "idpassageiro") String idpassageiro) {
        return TripDao.tripPassengerChange(idtrip, idpassageiro, "1");
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "passengerlist")
    public String passengerlist(@WebParam(name = "idtrip") String idtrip, @WebParam(name = "idpassageiro") String idpassageiro) {
        return TripDao.passengerList(idtrip, idpassageiro);
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "locatetrip")
    public String locatetrip(@WebParam(name = "idtrip") String idtrip) {
        return TripDao.locateTrip(idtrip);
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "trippassengerupdate")
    public String trippassengerupdate(@WebParam(name = "idtrip") String idtrip, @WebParam(name = "idpassageiro") String idpassageiro) {
        return TripDao.tripPassengerUpdate(idtrip, idpassageiro);
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "tripdelete")
    public String tripdelete(@WebParam(name = "idtrip") String idtrip) {
        return TripDao.tripDelete(idtrip);
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "createservice")
    public String createservice(@WebParam(name = "emailaccount") String emailaccount, @WebParam(name = "caption") String caption, @WebParam(name = "description") String description, @WebParam(name = "contact") String contact, @WebParam(name = "lat") String lat, @WebParam(name = "lon") String lon) {
        return ServiceDao.createService(emailaccount, caption, description, contact, lat, lon);
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "listService")
    public String listService(@WebParam(name = "find") String find) {
        return ServiceDao.listService(find);
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "getservice")
    public String getservice(@WebParam(name = "id") String id) {
        return "";//ServiceDao.getService("", id);
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "getcallservice")
    public String getcallservice(@WebParam(name = "email") String email, @WebParam(name = "id") String id) {
       return ServiceDao.getService(email, id);
    }
}
