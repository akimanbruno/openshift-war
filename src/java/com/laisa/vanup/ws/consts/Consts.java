/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.laisa.vanup.ws.consts;

/**
 *
 * @author FLY
 */
public class Consts {
    
    /**
     * Codigos de retornos
     */
    public static final String br  = "<br>";
    public static final String row = "<row>";
    public static final String col = "<col>";


     /**
     * Separadores de conteúdo
     */
    public static final String ERROR = "-1";
    public static final String FAIL  = "0";
    public static final String OK    = "1";
    
    /**
     * Tipo de Usuário
     */
    public static final int DriverTypeId = 1;
    public static final String DriverType = "1";

    public static final int PassengerTypeId = 2;
    public static final String PassengerType = "2";    
    
    
    /**
     * Conexao com Banco de Dados
     */
    /*postgresql 
    public static final String ClassForName = "org.postgresql.Driver";
    public static final String DBContext = "jdbc:postgresql://localhost:5433/vanupdb";
    public static final String DBUser = "postgres";
    public static final String DBPass = "123456"; */
    //MySQL
    public static final String ClassForName = "com.mysql.jdbc.Driver";
    public static final String DBContext = "jdbc:mysql://den1.mysql3.gear.host:3306/mymobiarts3";
    public static final String DBUser = "mymobiarts3";
    public static final String DBPass = "Mn7tz_?AoUFA"; 
    
            
    
}
