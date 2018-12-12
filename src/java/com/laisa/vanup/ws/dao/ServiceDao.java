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
import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author FLY
 */
public class ServiceDao {
    
    
    public static String createService(String emailaccount, String caption,
           String description, String contact,  String lat, String lon) {
        
        try {            
            //Criando conexao com bando de dados
             Class.forName( Consts.ClassForName );
            String context = Consts.DBContext;
            
            Connection connection = 
                DriverManager.getConnection(context,Consts.DBUser, Consts.DBPass);
            
            
            String sID = "";
            try {
                
                /*
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
                emailaccount, String caption,
           String description, String contact,  String lat, String lon
                */

                //Inserindo Van
                Statement st2 = connection.createStatement();
                String sql2 = "";
                sql2 += "insert into tabservice ( emailaccount, caption, description, contact, lat, lon, datahora) " +
                " values ('"+ emailaccount +"','"+ caption+"','"+description+"','"+contact+"','"+lat+"','"+lon+"', current_timestamp)";
                st2.execute(sql2);


                //Select para pegar o ID regrado pelo registro
                String textosql = " select max(id) as id from tabservice ";
                Statement statement = (Statement) connection.createStatement();
                ResultSet rs = statement.executeQuery(textosql);
                //String sID = "";
                while (rs.next()) {
                    sID = String.valueOf(rs.getInt("id"));
                }
                
                 //Notificando por email
                 String newDesc = "Ola seja bem vindo ao Servico Simples,\n"+
                         "seu novo servico (" + caption +") foi cadastrado com sucesso!\n"
                         + "Para mais infomacoes entre em contato conosco.";
                 new ServiceDao().sendMail2(emailaccount, "Novo Servico: " + caption, newDesc );
                 //new ServiceDao().sendMail2("akiman.bruno@gmail.com", "(COPY)Novo Servico: " + caption, description );
            
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
    
    public static String getService(String email, String id){
       
        try {           
           
            
            //Criando conexao com bando de dados
             Class.forName( Consts.ClassForName );
            String context = Consts.DBContext;
            
            Connection connection = 
                DriverManager.getConnection(context,Consts.DBUser, Consts.DBPass);
            
            
            String sData = "";
            String caption = "";
            String emailaccount  ="";
            boolean hasExists = false;
            try {

                //Select da consulta
                String textosql = " select id , emailaccount , caption, description, contact, lat , lon, datahora from tabservice "
                                    + "where id = " + id; 
                
                Statement statement = (Statement) connection.createStatement();
                ResultSet rs = statement.executeQuery(textosql);
                
                while (rs.next()) {
                    sData += String.valueOf(rs.getInt("id")) + Consts.col;
                       
                    emailaccount = rs.getString("emailaccount") ;
                    caption = rs.getString("caption") ;
                    // data e hora capturar
                    DateFormat df = new SimpleDateFormat("dd.MM.yyyy hh:mm");
                    Timestamp dataHora = rs.getTimestamp("datahora");
                    sData += df.format(dataHora) + Consts.col;
                    
                    sData += rs.getString("lat") + Consts.col;
                    sData += rs.getString("lon") + Consts.col;
                    
                    sData += Consts.row;
                    
                    hasExists = true;
                }
                
                if (hasExists){
                    //NOTIFICAR 
                     
        //Notificando por email
                 String newDesc = "Uma nova solicitacao para seu servico( " + caption + " ),\n"+                         
                         "Foi gerada pelo usuario (" + email +") com sucesso!\n"
                         + "Para mais infomacoes entre em contato conosco.";
                 new ServiceDao().sendMail2(emailaccount, "Pedido Servico: " + caption, newDesc );
                 
                 newDesc = "Identificamos sua solicitacao para o servico( " + caption + " ),\n"+ 
                         "em breve o fornecedor ( " + emailaccount + " ) entrara em contato";
                new ServiceDao().sendMail2(email, "Pedido Servico: " + caption, newDesc );
            
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
    
    
    public static String listService(String find){
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
                String textosql = " select id , emailaccount , caption, description, contact, lat , lon, datahora from tabservice " +
"where caption like '%"+find+"%' " + 
                        " or description  like '%"+find+"%' "  +
                        " or contact  like '%"+find+"%' order by datahora desc ";
                
                Statement statement = (Statement) connection.createStatement();
                ResultSet rs = statement.executeQuery(textosql);
                
                while (rs.next()) {
                    sData += String.valueOf(rs.getInt("id")) + Consts.col;
                                     
                    sData += rs.getString("caption") + Consts.col;
                    sData += rs.getString("description") + Consts.col;                    
                    sData += rs.getString("contact") + Consts.col;
                    
                    // data e hora capturar
                    DateFormat df = new SimpleDateFormat("dd.MM.yyyy hh:mm");
                    Timestamp dataHora = rs.getTimestamp("datahora");
                    sData += df.format(dataHora) + Consts.col;
                    
                    sData += rs.getString("lat") + Consts.col;
                    sData += rs.getString("lon") + Consts.col;
                    
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
    
    
    
    public int sendMail2(String toEmail, String assunto, String conteudo){
        
        
        try
        {
            Properties props = System.getProperties();
              // -- Attaching to default Session, or we could start a new one --
              props.put("mail.transport.protocol", "smtp" );
              props.put("mail.smtp.starttls.enable","true" );
              props.put("mail.smtp.host", "smtp.live.com");
              props.put("mail.smtp.auth", "true" );
              Authenticator auth = new SMTPAuthenticator();
              Session session = Session.getInstance(props, auth);
              //Session session = Session.getInstance(props);
              // -- Create a new message --
              Message msg = new MimeMessage(session);
              // -- Set the FROM and TO fields --
              String from = "bruno_akiman@hotmail.com";
              String to = toEmail;//"akiman.bruno@gmail.com";
              String subject = assunto;//"my assunto 04";
              String message = conteudo;//"my msg 04";              
              
              msg.setFrom(new InternetAddress(from));
              msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
              msg.setSubject(subject);
              msg.setText(message);
              // -- Set some other header information --
              msg.setHeader("MyMail", "Mr. SS" );
              msg.setSentDate(new Date());
              // -- Send the message --
              Transport.send(msg);
              System.out.println("Message sent to"+to+" OK." );
              return 0;
        }
        catch (Exception ex)
        {
          ex.printStackTrace();
          System.out.println("Exception "+ex);
          return -1;
        }
  }
    
    
    private class SMTPAuthenticator extends javax.mail.Authenticator {
        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            String username =  "bruno_akiman@hotmail.com";           // specify your email id here (sender's email id)
            String password = "symakiman++9";                                      // specify your password here
            return new PasswordAuthentication(username, password);
        }
  }
}
