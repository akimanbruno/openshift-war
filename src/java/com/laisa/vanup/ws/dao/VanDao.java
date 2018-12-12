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

/**
 *
 * @author FLY
 */
public class VanDao {
    
    public static String createVan(String placa, String cor, String marca, String idproprietario){
        try {            
            //Criando conexao com bando de dados
             Class.forName( Consts.ClassForName );
            String context = Consts.DBContext;
            
            Connection connection = 
                DriverManager.getConnection(context,Consts.DBUser, Consts.DBPass);
            
            
            String sID = "";
            try {
                
                //Select para verificar se VAN ja existe
                String sqlVerify = " select id from tabvan ";
                sqlVerify += " where placa = '" + placa + "' ";                    
                sqlVerify += "  and id_proprietario = " + idproprietario;
                
                Statement stVerify = (Statement) connection.createStatement();
                ResultSet rsVerify = stVerify.executeQuery(sqlVerify);
                if (rsVerify.next()) { //Existe registro 
                    return Consts.FAIL + Consts.br + "Van (placa) já cadastrada para você!";
                }
                

                //Inserindo Van
                Statement st2 = connection.createStatement();
                String sql2 = "";
                sql2 += " insert into tabvan (placa, cor, marca, id_proprietario) ";
                sql2 += " values ( '" + placa + "' , '" + cor + "' , '" + marca +"' , " + idproprietario + " ) ";           
                st2.execute(sql2);


                //Select para pegar o ID regrado pelo registro
                String textosql = " select max(id) as id from tabvan ";
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
                   placa + Consts.col + 
                   cor + Consts.col +
                   marca + Consts.col +
                   Consts.row;
            
        } catch (Exception e) {
            return Consts.ERROR + Consts.br + e.toString();
        }
    }
    
    
    
    public static String listVan(String idproprietario){
        try {            
            //Criando conexao com bando de dados
             Class.forName( Consts.ClassForName );
            String context = Consts.DBContext;
            
            Connection connection = 
                DriverManager.getConnection(context,Consts.DBUser, Consts.DBPass);
            
            
            String sData = "";
            boolean hasExists = false;
            try {

                //Select para pegar o ID regrado pelo registro
                String textosql = " select id, placa, cor, marca from tabvan ";
                textosql += "  where id_proprietario = " + idproprietario; 
                textosql += " order by placa ";
                
                Statement statement = (Statement) connection.createStatement();
                ResultSet rs = statement.executeQuery(textosql);
                
                while (rs.next()) {
                    sData += String.valueOf(rs.getInt("id")) + Consts.col;
                    
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
    
    
    
}
