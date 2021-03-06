/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sysware.concesionario.servlets.polizas;

import com.sysware.concesionario.app.App;
import com.sysware.concesionario.entitie.Entitie;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author andre
 */
public class FormRecepcionPolizas extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try{
            if(request.getSession().getAttribute("session").equals("true")){
                String menu="";
                try{
                    menu= request.getParameter("menu");
                }catch(NullPointerException s){
                    s.printStackTrace();
                }
                if(menu.equals("polizas")){
                    String name;
                    ArrayList<Entitie> polizas = new ArrayList<>();
                    String canal="";
                    try{
                        canal= request.getParameter("canal");
                    }catch(NullPointerException s){
                        System.out.println("Error: "+s);
                    }
                    Entitie polizaAS = new Entitie(App.TABLE_ASIS_DENTAL); //POLIZAS DE ASISTENCIA DENTAL
                    name= "POLIZAS";
                    boolean nada = false;
                    ArrayList<String> param1=new ArrayList<>();
                    ArrayList<String> param2=new ArrayList<>();
                    ArrayList<String> operation=new ArrayList<>();

                    if(!canal.equals("")){
                        Entitie can = new Entitie(App.TABLE_CANALES);
                        can.getEntitieID(canal);
                        name ="LISTADO DE POLIZAS DEL CANAL </b>"+can.getDataOfLabel("NOMBRE")+"<b>";
                        param1.add("CANAL");
                        param2.add(canal);
                        operation.add("=");
                        nada=true;
                    }

                    if(param1.isEmpty() && param2.isEmpty() && operation.isEmpty() && nada==false){
                        String pago="PORPAGAR";
                        param1.add("ESTADOPAGO");
                        param2.add(pago);
                        operation.add("=");
                        param1.add("ESTADOPOL");
                        param2.add("VIGENTE");
                        operation.add("=");
                        polizas = polizaAS.getEntitieParams(param1, param2, operation);
                        name="LISTADO DE TODAS LAS POLIZAS";
                    }
                    else{
                        String pago="PORPAGAR";
                        param1.add("ESTADOPAGO");
                        param2.add(pago);
                        operation.add("=");
                        param1.add("ESTADOPOL");
                        param2.add("VIGENTE");
                        operation.add("=");
                        polizas = polizaAS.getEntitieParams(param1, param2, operation);
                    }
                    Entitie conce = new Entitie(App.TABLE_CANALES);
                    Entitie cliente = new Entitie(App.TABLE_CLIENTE);
                    try (PrintWriter out = response.getWriter()) {
                        out.println("{");
                        out.println("\"title\": \""+name+"\",");
                        out.println("\"info\": {");
                        out.println("   \"row\": [");
                        for(Entitie i: polizas){
                            out.println("       {");
                            out.println("       \"poliza\": \""+i.getDataOfLabel("POLIZA")+"\",");
                            out.println("       \"fecha\": \""+i.getDataOfLabel("FECHA")+"\",");
                            conce.getEntitieID(i.getDataOfLabel("CANAL"));
                            out.println("       \"canal\": \""+conce.getDataOfLabel("NOMBRE")+"\",");
                            cliente.getEntitieID(i.getDataOfLabel("CLIENTE"));
                            out.println("       \"cliente\": \""+cliente.getDataOfLabel("NOMBRE")+" "+cliente.getDataOfLabel("APELLIDO")+"\",");
                            int prima= Integer.parseInt(i.getDataOfLabel("VALORPRIMA"));
                            int pago= Integer.parseInt(i.getDataOfLabel("PAGOCANAL"));
                            out.println("       \"prima\": \""+prima+"\",");
                            out.println("       \"pago\": \""+pago+"\",");
                            out.println("       \"dias\": \""+0+"\"");
                            out.println("       }, ");
                        }
                            out.println("       {");
                            out.println("       \"poliza\": \"0\"");
                            out.println("       } ");
                        out.println("       ]");
                        out.println("   },");
                        out.println("\"andiazher\": \"andiazher.com\"");
                        out.println("}");
                        
                    }
                    //END
                }
            }
            else{
                response.sendRedirect("login.jsp?validate=Por+favor+ingresar+credenciales");
            }
        }
        catch(NullPointerException e){
            response.sendRedirect("login.jsp?validate=Por+favor+ingresar+credenciales");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(FormRecepcionPolizas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(FormRecepcionPolizas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
