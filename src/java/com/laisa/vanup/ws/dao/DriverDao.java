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
public class DriverDao {
    
    public static String createDriver(String email, String senha, String nome, String cnh){
        try {            
            //Criando conexao com bando de dados
            Class.forName( Consts.ClassForName );
            String context = Consts.DBContext;
            
            Connection connection = 
                DriverManager.getConnection(context,Consts.DBUser, Consts.DBPass);
            
            String sID = "";
            try {
                
                //Select para verificar se Motorista ja existe
                String sqlVerify = " select id from tabmotorista ";
                sqlVerify += " where email = '" + email + "' ";                    
                Statement stVerify = (Statement) connection.createStatement();
                ResultSet rsVerify = stVerify.executeQuery(sqlVerify);
                if (rsVerify.next()) { //Existe registro 
                    return Consts.FAIL + Consts.br + "Motorista (e-mail) já cadastrado!";                         
                }
                

                //Inserindo motorista
                Statement st2 = connection.createStatement();
                String sql2 = "";
                sql2 += " insert into tabmotorista(nome, cnh, email, senha)   ";
                sql2 += " values ( '" + nome + "' , '" + cnh + "' , '" + email +"' , '" + senha + "' ) ";           
                st2.execute(sql2);


                //Select para pegar o ID regrado pelo registro
                String textosql = " select max(id) as id from tabmotorista ";
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
            /*Class.forName("org.postgresql.Driver");
            String context = "jdbc:postgresql://localhost:5433/vanupdb";
            
            Connection connection = 
                DriverManager.getConnection(context,"postgres", "123456");
            */
            Class.forName( Consts.ClassForName );
            String context = Consts.DBContext;
            
            Connection connection = 
                DriverManager.getConnection(context,Consts.DBUser, Consts.DBPass);
            
            
            
            try {
                
                //Select para verificar se Motorista ja existe
                String sqlVerify = " select * from tabmotorista ";
                sqlVerify += " where email = '" + email + "' ";                    
                sqlVerify += "   and senha = '" + pass + "' ";                    
                
                Statement stVerify = (Statement) connection.createStatement();
                ResultSet rsVerify = stVerify.executeQuery(sqlVerify);
                if (rsVerify.next()) { //Existe registro 
                    
                    String sId   = String.valueOf(rsVerify.getInt("id"));
                    String sNome = rsVerify.getString("nome");
                    String sEmail = rsVerify.getString("email");
                    String sCnh = rsVerify.getString("cnh");
                    
                    return Consts.OK + Consts.br   
                            //Dados do Motorista
                            + Consts.DriverType + Consts.col    // Tipo Usuário
                            + sId + Consts.col                  // ID 
                            + sNome + Consts.col                // Nome 
                            + sCnh + Consts.col                 // CNH
                            + sEmail + Consts.col               // E-mail
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
