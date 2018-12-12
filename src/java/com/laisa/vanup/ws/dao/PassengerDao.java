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
public class PassengerDao {
    
    public static String createPassenger(String email, String senha, String nome, String fone){
        try {            
            //Criando conexao com bando de dados
            Class.forName( Consts.ClassForName );
            String context = Consts.DBContext;
            
            Connection connection = 
                DriverManager.getConnection(context,Consts.DBUser, Consts.DBPass);
            
            
            String sID = "";
            try {
                
                //Select para verificar se Motorista ja existe
                String sqlVerify = " select id from tabpassageiro ";
                sqlVerify += " where email = '" + email + "' ";                    
                Statement stVerify = (Statement) connection.createStatement();
                ResultSet rsVerify = stVerify.executeQuery(sqlVerify);
                if (rsVerify.next()) { //Existe registro 
                    return Consts.FAIL + Consts.br + "Passageiro (e-mail) já cadastrado!";                         
                }
                

                //Inserindo motorista
                Statement st2 = connection.createStatement();
                String sql2 = "";
                sql2 += " insert into tabpassageiro (nome, telefone, email, senha)   ";
                sql2 += " values ( '" + nome + "' , '" + fone + "' , '" + email +"' , '" + senha + "' ) ";           
                st2.execute(sql2);


                //Select para pegar o ID regrado pelo registro
                String textosql = " select max(id) as id from tabpassageiro ";
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
                   nome + Consts.col + 
                   email + Consts.col +
                   Consts.row;
            
        } catch (Exception e) {
            return Consts.ERROR + Consts.br + e.toString();
        }
    }
    
    
    public static String Validate(String email, String pass){
        
        try {            
            //Criando conexao com bando de dados
             Class.forName( Consts.ClassForName );
            String context = Consts.DBContext;
            
            Connection connection = 
                DriverManager.getConnection(context,Consts.DBUser, Consts.DBPass);
            
            
            try {
                
                //Select para verificar se Motorista ja existe
                String sqlVerify = " select * from tabpassageiro ";
                sqlVerify += " where email = '" + email + "' ";                    
                sqlVerify += "   and senha = '" + pass + "' ";                    
                
                Statement stVerify = (Statement) connection.createStatement();
                ResultSet rsVerify = stVerify.executeQuery(sqlVerify);
                if (rsVerify.next()) { //Existe registro 
                    
                    String sId   = String.valueOf(rsVerify.getInt("id"));
                    String sNome = rsVerify.getString("nome");
                    String sEmail = rsVerify.getString("email");
                    String sFone = rsVerify.getString("telefone");
                    
                    return Consts.OK + Consts.br   
                            //Dados do Passageiro
                            + Consts.PassengerType + Consts.col    // Tipo Usuário
                            + sId + Consts.col                     // ID 
                            + sNome + Consts.col                   // Nome 
                            + sFone + Consts.col                   // Fone
                            + sEmail + Consts.col                  // E-mail
                            + Consts.row;                  
                    
                } else {
                    return Consts.FAIL + Consts.br + "E-mail e/ou senha incorreto(s)!";
                }
            } finally {
                connection.close();
            }
            
        } catch (Exception e) {
            return Consts.ERROR + Consts.br + e.toString();
        }
        
    }
    
}
