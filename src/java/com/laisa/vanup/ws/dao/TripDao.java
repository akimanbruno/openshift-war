/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.laisa.vanup.ws.dao;

import com.laisa.vanup.ws.consts.Consts;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author FLY
 */
public class TripDao {
    
    
    public static String createTrip(String idvan, String idmotorista, String descricao){
        try {            
            //Criando conexao com bando de dados
             Class.forName( Consts.ClassForName );
            String context = Consts.DBContext;
            
            Connection connection = 
                DriverManager.getConnection(context,Consts.DBUser, Consts.DBPass);
            
            
            String sID = "";
            try {
                
                //Select para verificar se VAN ja existe
                String sqlVerify = " select * from tabviagem ";
                sqlVerify += " where id_van = '" + idvan + "' ";
                sqlVerify += "   and id_motorista = " + idmotorista;
                sqlVerify += "   and status > 0 and status <> 6 ";
                
                Statement stVerify = (Statement) connection.createStatement();
                ResultSet rsVerify = stVerify.executeQuery(sqlVerify);
                if (rsVerify.next()) { //Existe registro 
                    return Consts.FAIL + Consts.br + "Esta van já possui uma viagem ativa, verifique!";
                }
                

                //Inserindo Van
                Statement st2 = connection.createStatement();
                String sql2 = "";
                sql2 += " insert into tabviagem (id_van, id_motorista, status, descricao, datahora) ";
                sql2 += " values ( " + idvan + " , " + idmotorista + " , 1 , '" + descricao + "' , current_timestamp ) ";           
                st2.execute(sql2);


                //Select para pegar o ID regrado pelo registro
                String textosql = " select max(id) as id from tabviagem ";
                Statement statement = (Statement) connection.createStatement();
                ResultSet rs = statement.executeQuery(textosql);
                //String sID = "";
                while (rs.next()) {
                    sID = String.valueOf(rs.getInt("id"));
                }
            
            } finally {
                connection.close();
            }
            
            return Consts.OK + Consts.br + 
                   sID + Consts.col +
                   Consts.row;
            
        } catch (Exception e) {
            return Consts.ERROR + Consts.br + e.toString();
        }
    }
    
    
    private static String prepareSQLTripActive(String idpassageiro){
        return prepareSQLTripActive(idpassageiro, "");
    }    
    private static String prepareSQLTripActive(String idpassageiro, String idTrip){
        return prepareSQLTripActive(idpassageiro, idTrip, "-1");
    }    
    private static String prepareSQLTripActive(String idpassageiro, String idTrip, String statusExcluido){
        String textosql = "  select tabviagem.* ,   ";
        textosql += "       tabmotorista.nome, tabvan.placa, tabvan.cor, tabvan.marca  ";

        textosql += "       , COALESCE( ( select id from tabviagempassageiro ";
        textosql += "            where tabviagempassageiro.id_viagem = tabviagem.id ";
        textosql += "              and status > 0";
        textosql += "              and id_passageiro = " + idpassageiro + " limit 1 ), -1) as pasid ";
        
        textosql += "       , COALESCE( ( select status from tabviagempassageiro ";
        textosql += "            where tabviagempassageiro.id_viagem = tabviagem.id ";
        textosql += "              and status > 0";
        textosql += "              and id_passageiro = " + idpassageiro + " limit 1 ), -1) as passtatus ";

        textosql += " from tabviagem ";
        textosql += " left join tabmotorista on tabmotorista.id = tabviagem.id_motorista ";
        textosql += " left join tabvan on tabvan.id = tabviagem.id_van ";
        textosql += " where status > 1 and status <> " + statusExcluido;
        
        if (idTrip.length() > 0){
            textosql += " and tabviagem.id = " + idTrip;
        }
        
        textosql += " order by pasid desc, tabviagem.descricao, tabviagem.status, tabvan.placa ";
        
        return textosql;
    }
    public static String listTripActive(String idpassageiro){
        try {            
            //Criando conexao com bando de dados
             Class.forName( Consts.ClassForName );
            String context = Consts.DBContext;
            
            Connection connection = 
                DriverManager.getConnection(context,Consts.DBUser, Consts.DBPass);
            
            
            String sData = "";
            boolean hasExists = false;
            try {

                //Select da consulta
                String textosql = prepareSQLTripActive(idpassageiro, "", "6");
                
                Statement statement = (Statement) connection.createStatement();
                ResultSet rs = statement.executeQuery(textosql);
                
                while (rs.next()) {
                    sData += String.valueOf(rs.getInt("id")) + Consts.col;
                    
                    sData += String.valueOf(rs.getInt("id_van")) + Consts.col;
                    sData += String.valueOf(rs.getInt("id_motorista")) + Consts.col;
                    sData += String.valueOf(rs.getInt("status")) + Consts.col;                    
                    sData += rs.getString("descricao") + Consts.col;
                    
                    // data e hora capturar
                    DateFormat df = new SimpleDateFormat("dd.MM.yyyy hh:mm");
                    Timestamp dataHora = rs.getTimestamp("datahora");
                    sData += df.format(dataHora) + Consts.col;
                    
                    sData += rs.getString("nome") + Consts.col;
                    sData += rs.getString("placa") + Consts.col;
                    sData += rs.getString("cor") + Consts.col;
                    sData += rs.getString("marca") + Consts.col;                    
                    
                    sData += rs.getString("pasid") + Consts.col;   
                    sData += rs.getString("passtatus") + Consts.col;                       
                    
                    sData += Consts.row;
                    
                    hasExists = true;
                }
            
            } finally {
                connection.close();
            }
            
            if (!hasExists){
                return Consts.FAIL + Consts.br;
            }
            
            return Consts.OK + Consts.br + sData;
            
        } catch (Exception e) {
            return Consts.ERROR + Consts.br + e.toString();
        }        
    }
    
    
    public static String listTrip(String idmotorista){
        try {            
            //Criando conexao com bando de dados
             Class.forName( Consts.ClassForName );
            String context = Consts.DBContext;
            
            Connection connection = 
                DriverManager.getConnection(context,Consts.DBUser, Consts.DBPass);
            
            
            String sData = "";
            boolean hasExists = false;
            try {

                //Select da consulta
                String textosql = " select tabviagem.* ,  ";
                textosql += "       tabmotorista.nome, tabvan.placa, tabvan.cor, tabvan.marca ";
                textosql += " from tabviagem ";
                textosql += " left join tabmotorista on tabmotorista.id = tabviagem.id_motorista ";
                textosql += " left join tabvan on tabvan.id = tabviagem.id_van ";
                textosql += " where status > 0 and status <> 6 ";
                textosql += " and tabviagem.id_motorista = " + idmotorista;
                textosql += " order by tabviagem.datahora, tabvan.placa   ";
                
                Statement statement = (Statement) connection.createStatement();
                ResultSet rs = statement.executeQuery(textosql);
                
                while (rs.next()) {
                    sData += String.valueOf(rs.getInt("id")) + Consts.col;
                    
                    sData += String.valueOf(rs.getInt("id_van")) + Consts.col;
                    sData += String.valueOf(rs.getInt("id_motorista")) + Consts.col;
                    sData += String.valueOf(rs.getInt("status")) + Consts.col;                    
                    sData += rs.getString("descricao") + Consts.col;
                    
                    // data e hora capturar
                    DateFormat df = new SimpleDateFormat("dd.MM.yyyy hh:mm");
                    Timestamp dataHora = rs.getTimestamp("datahora");
                    sData += df.format(dataHora) + Consts.col;
                    
                    sData += rs.getString("nome") + Consts.col;
                    sData += rs.getString("placa") + Consts.col;
                    sData += rs.getString("cor") + Consts.col;
                    sData += rs.getString("marca") + Consts.col;                    
                    
                    
                    sData += Consts.row;
                    
                    hasExists = true;
                }
            
            } finally {
                connection.close();
            }
            
            if (!hasExists){
                return Consts.FAIL + Consts.br;
            }
            
            return Consts.OK + Consts.br + sData;
            
        } catch (Exception e) {
            return Consts.ERROR + Consts.br + e.toString();
        }
    }
    
    public static String startTrip(String idtrip){
        return tripChange(idtrip, "2");
    }
    
    public static String tripCheck1(String idtrip){
        return tripChange(idtrip, "3");
    }
    
    public static String tripReturn(String idtrip){
        return tripChange(idtrip, "4");
    }
    
    public static String tripFinalize(String idtrip){
        return tripChange(idtrip, "5");
    }
    
    public static String tripDelete(String idtrip){
        return tripChange(idtrip, "6");
    }
    
    private static String tripChange(String idtrip, String statusID){
        try {            
            //Criando conexao com bando de dados
             Class.forName( Consts.ClassForName );
            String context = Consts.DBContext;
            
            Connection connection = 
                DriverManager.getConnection(context,Consts.DBUser, Consts.DBPass);
            
            
            String sData = "";
            boolean hasExists = false;
            try {
                
                //Inserindo Van
                Statement st2 = connection.createStatement();
                String sql2 = "";
                sql2 += " update tabviagem  ";
                sql2 += "  set status = " + statusID ;
                sql2 += " where id = " + idtrip; // + " and status = 1 ";
                st2.execute(sql2);
                
                if (statusID == "2") {
                    //atualizar lista de passageiros
                    Statement stUpdatePass = connection.createStatement();
                    String sqlUpdatePass = " update tabviagempassageiro ";
                    sqlUpdatePass += " set status = -1, datahoraatualizacao = current_timestamp ";
                    sqlUpdatePass += " where id_viagem = " + idtrip + " and status > 0 ";
                    stUpdatePass.execute(sqlUpdatePass);
                }
                
                if (statusID == "3") {
                    //atualizar lista de passageiros
                    Statement stUpdatePass = connection.createStatement();
                    String sqlUpdatePass = " update tabviagempassageiro ";
                    sqlUpdatePass += " set status = 2, datahoraatualizacao = current_timestamp ";
                    sqlUpdatePass += " where id_viagem = " + idtrip + " and status > 0 ";
                    stUpdatePass.execute(sqlUpdatePass);
                }

                //Select para pegar o registro
                String textosql = " select tabviagem.* ,  ";
                textosql += "       tabmotorista.nome, tabvan.placa, tabvan.cor, tabvan.marca ";
                textosql += " from tabviagem ";
                textosql += " left join tabmotorista on tabmotorista.id = tabviagem.id_motorista ";
                textosql += " left join tabvan on tabvan.id = tabviagem.id_van ";
                textosql += " where status > 0 ";
                textosql += " and tabviagem.id = " + idtrip;
                textosql += " order by tabviagem.datahora, tabvan.placa   ";
                
                Statement statement = (Statement) connection.createStatement();
                ResultSet rs = statement.executeQuery(textosql);
                
                while (rs.next()) {
                    sData += String.valueOf(rs.getInt("id")) + Consts.col;
                    
                    sData += String.valueOf(rs.getInt("id_van")) + Consts.col;
                    sData += String.valueOf(rs.getInt("id_motorista")) + Consts.col;
                    sData += String.valueOf(rs.getInt("status")) + Consts.col;                    
                    sData += rs.getString("descricao") + Consts.col;
                    
                    // data e hora capturar
                    DateFormat df = new SimpleDateFormat("dd.MM.yyyy hh:mm");
                    Timestamp dataHora = rs.getTimestamp("datahora");
                    sData += df.format(dataHora) + Consts.col;
                    
                    sData += rs.getString("nome") + Consts.col;
                    sData += rs.getString("placa") + Consts.col;
                    sData += rs.getString("cor") + Consts.col;
                    sData += rs.getString("marca") + Consts.col;                    
                    
                    sData += Consts.row;
                    
                    hasExists = true;
                }                
            
            } finally {
                connection.close();
            }
            
            if (!hasExists){
                return Consts.FAIL + Consts.br;
            }
            
            return Consts.OK + Consts.br + sData;
            
        } catch (Exception e) {
            return Consts.ERROR + Consts.br + e.toString();
        }
    }

    
    public static String savePosition(String idTrip, String lat, String lon, String vel, String dtTime){
        try {            
            //Criando conexao com bando de dados
             Class.forName( Consts.ClassForName );
            String context = Consts.DBContext;
            
            Connection connection = 
                DriverManager.getConnection(context,Consts.DBUser, Consts.DBPass);
            
            
            String sID = "";
            try {

                //Inserindo Van
                Statement st2 = connection.createStatement();
                String sql2 = "";
                sql2 += " insert into tabposicao (id_viagem, lat, lon, vel, datahora) ";
                sql2 += " values ( " + idTrip + " , '" + lat + "' , '" + lon + "' , '" + vel + "',  current_timestamp ) ";           
                st2.execute(sql2);


                //Select para pegar o ID regrado pelo registro
                String textosql = " select max(id) as id from tabposicao where id_viagem = " + idTrip ;
                Statement statement = (Statement) connection.createStatement();
                ResultSet rs = statement.executeQuery(textosql);
                //String sID = "";
                while (rs.next()) {
                    sID = String.valueOf(rs.getInt("id"));
                }
            
            } finally {
                connection.close();
            }
            
            return Consts.OK + Consts.br + 
                   sID + Consts.col +
                   Consts.row;
            
        } catch (Exception e) {
            return Consts.ERROR + Consts.br + e.toString();
        }
    }
    
    
    
    public static String tripCheckIn(String idTrip, String idPassageiro){
        try {            
            //Criando conexao com bando de dados
             Class.forName( Consts.ClassForName );
            String context = Consts.DBContext;
            
            Connection connection = 
                DriverManager.getConnection(context,Consts.DBUser, Consts.DBPass);
            
            
            String sReturn = "";
            try {
                //Substituindo caso exista
                Statement stReplace = connection.createStatement();
                String sql2 = "";
                sql2 += " update tabviagempassageiro ";
                sql2 += "   set status = -1 , datahoraatualizacao = current_timestamp ";           
                sql2 += " where id_passageiro = " + idPassageiro;
                sql2 += "     and status > 0 ";
                stReplace.execute(sql2);

                
                //Inserindo CheckIn
                Statement st2 = connection.createStatement();
                sql2 = "";
                sql2 += " insert into tabviagempassageiro (id_viagem, id_passageiro, status, datahoraatualizacao) ";
                sql2 += " values (" + idTrip + ", " + idPassageiro + ", 1, current_timestamp) ";           
                st2.execute(sql2);


                //Select para pegar o ID regrado pelo registro
                String textosql = " select max(id) as id from tabviagempassageiro ";
                Statement statement = (Statement) connection.createStatement();
                ResultSet rs = statement.executeQuery(textosql);
                //String sID = "";
                String sID = "";
                while (rs.next()) {
                    sID = String.valueOf(rs.getInt("id"));
                }
                
                String sqlReturn = prepareSQLTripActive(idPassageiro, idTrip);
                Statement stReturn = (Statement) connection.createStatement();
                ResultSet rsReturn = stReturn.executeQuery(sqlReturn);
                
                while (rsReturn.next()) {
                    sReturn += String.valueOf(rsReturn.getInt("id")) + Consts.col;
                    
                    sReturn += String.valueOf(rsReturn.getInt("id_van")) + Consts.col;
                    sReturn += String.valueOf(rsReturn.getInt("id_motorista")) + Consts.col;
                    sReturn += String.valueOf(rsReturn.getInt("status")) + Consts.col;                    
                    sReturn += rsReturn.getString("descricao") + Consts.col;
                    
                    // data e hora capturar
                    DateFormat df = new SimpleDateFormat("dd.MM.yyyy hh:mm");
                    Timestamp dataHora = rsReturn.getTimestamp("datahora");
                    sReturn += df.format(dataHora) + Consts.col;
                    
                    sReturn += rsReturn.getString("nome") + Consts.col;
                    sReturn += rsReturn.getString("placa") + Consts.col;
                    sReturn += rsReturn.getString("cor") + Consts.col;
                    sReturn += rsReturn.getString("marca") + Consts.col;                    
                    
                    sReturn += rsReturn.getString("pasid") + Consts.col;                    
                    sReturn += rsReturn.getString("passtatus") + Consts.col;                    
                    
                    sReturn += Consts.row;                    
                }
                
            
            } finally {
                connection.close();
            }
            
            return Consts.OK + Consts.br + 
                   sReturn + 
                   Consts.row;
            
        } catch (Exception e) {
            return Consts.ERROR + Consts.br + e.toString();
        }
    }
    
    
     public static String tripPassengerChange(String idTrip, String idPassageiro, String status){
        try {            
            //Criando conexao com bando de dados
             Class.forName( Consts.ClassForName );
            String context = Consts.DBContext;
            
            Connection connection = 
                DriverManager.getConnection(context,Consts.DBUser, Consts.DBPass);
            
            
            String sReturn = "";
            try {
                //Substituindo caso exista
                Statement stReplace = connection.createStatement();
                String sql2 = "";
                sql2 += " update tabviagempassageiro ";
                sql2 += "   set status = " + status + " , datahoraatualizacao = current_timestamp ";           
                sql2 += " where id_passageiro = " + idPassageiro;
                sql2 += "     and id_viagem = " + idTrip;
                sql2 += "     and status > 0 ";
                stReplace.execute(sql2);

                String sqlReturn = prepareSQLTripActive(idPassageiro, idTrip);
                Statement stReturn = (Statement) connection.createStatement();
                ResultSet rsReturn = stReturn.executeQuery(sqlReturn);
                
                while (rsReturn.next()) {
                    sReturn += String.valueOf(rsReturn.getInt("id")) + Consts.col;
                    
                    sReturn += String.valueOf(rsReturn.getInt("id_van")) + Consts.col;
                    sReturn += String.valueOf(rsReturn.getInt("id_motorista")) + Consts.col;
                    sReturn += String.valueOf(rsReturn.getInt("status")) + Consts.col;                    
                    sReturn += rsReturn.getString("descricao") + Consts.col;
                    
                    // data e hora capturar
                    DateFormat df = new SimpleDateFormat("dd.MM.yyyy hh:mm");
                    Timestamp dataHora = rsReturn.getTimestamp("datahora");
                    sReturn += df.format(dataHora) + Consts.col;
                    
                    sReturn += rsReturn.getString("nome") + Consts.col;
                    sReturn += rsReturn.getString("placa") + Consts.col;
                    sReturn += rsReturn.getString("cor") + Consts.col;
                    sReturn += rsReturn.getString("marca") + Consts.col;                    
                    
                    sReturn += rsReturn.getString("pasid") + Consts.col;                    
                    sReturn += rsReturn.getString("passtatus") + Consts.col;                    
                    
                    sReturn += Consts.row;                    
                }
                
            
            } finally {
                connection.close();
            }
            
            return Consts.OK + Consts.br + 
                   sReturn + 
                   Consts.row;
            
        } catch (Exception e) {
            return Consts.ERROR + Consts.br + e.toString();
        }
    }
     
     
    public static String passengerList(String idTrip, String idPassageiro){
        try {            
            //Criando conexao com bando de dados
             Class.forName( Consts.ClassForName );
            String context = Consts.DBContext;
            
            Connection connection = 
                DriverManager.getConnection(context,Consts.DBUser, Consts.DBPass);
            
            
            boolean hasExists = false;
            String sReturn = "";
            try {

                String sqlReturn = ""; 
                sqlReturn += " select tabviagempassageiro.* , tabpassageiro.nome, tabpassageiro.email, tabpassageiro.telefone ";
                sqlReturn += "        , CASE tabviagempassageiro.id_passageiro WHEN "+ idPassageiro +" THEN '1' ELSE '0' END as isyou ";
                sqlReturn += "        , tabviagem.status as viagem_status, tabviagem.descricao as viagem_desc ";
                
                sqlReturn += " from tabviagempassageiro left join tabpassageiro  ";
                sqlReturn += "    on tabpassageiro.id = tabviagempassageiro.id_passageiro";
                sqlReturn += "        left join tabviagem ";
                sqlReturn += "    on tabviagem.id = tabviagempassageiro.id_viagem ";
                
                sqlReturn += " where tabviagempassageiro.status > 0 ";
                sqlReturn += "   and tabviagempassageiro.id_viagem = " + idTrip ;
                sqlReturn += " order by isyou desc   ";
                
                Statement stReturn = (Statement) connection.createStatement();
                ResultSet rsReturn = stReturn.executeQuery(sqlReturn);
                
                while (rsReturn.next()) {
                    sReturn += String.valueOf(rsReturn.getInt("id")) + Consts.col;
                    
                    sReturn += String.valueOf(rsReturn.getInt("id_viagem")) + Consts.col;
                    sReturn += String.valueOf(rsReturn.getInt("id_passageiro")) + Consts.col;
                    sReturn += String.valueOf(rsReturn.getInt("status")) + Consts.col;                    
                    
                    // data e hora capturar
                    DateFormat df = new SimpleDateFormat("dd.MM.yyyy hh:mm");
                    Timestamp dataHora = rsReturn.getTimestamp("datahoraatualizacao");
                    sReturn += df.format(dataHora) + Consts.col;
                    
                    if (idPassageiro.equals( rsReturn.getString("id_passageiro") )) {
                        sReturn += rsReturn.getString("nome") + " (EU)" + Consts.col;
                    } 
                    else 
                    {                           
                        sReturn += rsReturn.getString("nome") + Consts.col;    
                    }
                    
                    sReturn += rsReturn.getString("email") + Consts.col;
                    sReturn += rsReturn.getString("telefone") + Consts.col;
                    sReturn += rsReturn.getString("isyou") + Consts.col;                    
                    
                    sReturn += rsReturn.getString("viagem_status") + Consts.col;                    
                    sReturn += rsReturn.getString("viagem_desc") + Consts.col;                    
                    
                    sReturn += Consts.row;    
                    
                    hasExists = true;
                }
                
            
            } finally {
                connection.close();
            }
            
            if (!hasExists){
                return Consts.FAIL + Consts.br;
            }
            
            return Consts.OK + Consts.br + sReturn;// + Consts.row;
            
        } catch (Exception e) {
            return Consts.ERROR + Consts.br + e.toString();
        }
    }
    
    
    public static String locateTrip(String idTrip){
        try {            
            //Criando conexao com bando de dados
             Class.forName( Consts.ClassForName );
            String context = Consts.DBContext;
            
            Connection connection = 
                DriverManager.getConnection(context,Consts.DBUser, Consts.DBPass);
            
            
            boolean hasExists = false;
            String sReturn = "";
            try {

                String sqlReturn = ""; 
                sqlReturn += " select tabposicao.*, tabviagem.descricao, tabviagem.status ";
                sqlReturn += "   from tabposicao left join tabviagem  ";
                sqlReturn += "     on tabviagem.id = tabposicao.id_viagem ";
                sqlReturn += " where id_viagem = " + idTrip;
                sqlReturn += " order by datahora desc ";
                sqlReturn += " limit 1 ";
                
                Statement stReturn = (Statement) connection.createStatement();
                ResultSet rsReturn = stReturn.executeQuery(sqlReturn);
                
                while (rsReturn.next()) {
                    sReturn += String.valueOf(rsReturn.getInt("id")) + Consts.col;
                    
                    sReturn += String.valueOf(rsReturn.getInt("id_viagem")) + Consts.col;
                    sReturn += String.valueOf(rsReturn.getString("lat")) + Consts.col;
                    sReturn += String.valueOf(rsReturn.getString("lon")) + Consts.col; 
                    sReturn += String.valueOf(rsReturn.getString("vel")) + Consts.col;                    
                    
                    // data e hora capturar
                    DateFormat df = new SimpleDateFormat("dd.MM.yyyy hh:mm");
                    Timestamp dataHora = rsReturn.getTimestamp("datahora");
                    sReturn += df.format(dataHora) + Consts.col;
                    
                    sReturn += rsReturn.getString("descricao") + Consts.col;                    
                    sReturn += rsReturn.getString("status") + Consts.col;                    
                    
                    sReturn += Consts.row;    
                    
                    hasExists = true;
                }
                
            
            } finally {
                connection.close();
            }
            
            if (!hasExists){
                return Consts.FAIL + Consts.br;
            }
            
            return Consts.OK + Consts.br + sReturn;// + Consts.row;
            
        } catch (Exception e) {
            return Consts.ERROR + Consts.br + e.toString();
        }
    }
    
    public static String tripPassengerUpdate(String idTrip, String idPassageiro){
        try {            
            //Criando conexao com bando de dados
             Class.forName( Consts.ClassForName );
            String context = Consts.DBContext;
            
            Connection connection = 
                DriverManager.getConnection(context,Consts.DBUser, Consts.DBPass);
            
            
            String sReturn = "";
            try {
                String sqlReturn = prepareSQLTripActive(idPassageiro, idTrip, "-1");
                Statement stReturn = (Statement) connection.createStatement();
                ResultSet rsReturn = stReturn.executeQuery(sqlReturn);
                
                while (rsReturn.next()) {
                    sReturn += String.valueOf(rsReturn.getInt("id")) + Consts.col;
                    
                    sReturn += String.valueOf(rsReturn.getInt("id_van")) + Consts.col;
                    sReturn += String.valueOf(rsReturn.getInt("id_motorista")) + Consts.col;
                    sReturn += String.valueOf(rsReturn.getInt("status")) + Consts.col;                    
                    sReturn += rsReturn.getString("descricao") + Consts.col;
                    
                    // data e hora capturar
                    DateFormat df = new SimpleDateFormat("dd.MM.yyyy hh:mm");
                    Timestamp dataHora = rsReturn.getTimestamp("datahora");
                    sReturn += df.format(dataHora) + Consts.col;
                    
                    sReturn += rsReturn.getString("nome") + Consts.col;
                    sReturn += rsReturn.getString("placa") + Consts.col;
                    sReturn += rsReturn.getString("cor") + Consts.col;
                    sReturn += rsReturn.getString("marca") + Consts.col;                    
                    
                    sReturn += rsReturn.getString("pasid") + Consts.col;                    
                    sReturn += rsReturn.getString("passtatus") + Consts.col;                    
                    
                    sReturn += Consts.row;                    
                }
                
            
            } finally {
                connection.close();
            }
            
            return Consts.OK + Consts.br + 
                   sReturn + 
                   Consts.row;
            
        } catch (Exception e) {
            return Consts.ERROR + Consts.br + e.toString();
        }
    }
    
    
}
