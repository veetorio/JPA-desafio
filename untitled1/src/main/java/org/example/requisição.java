package org.example;

import org.example.controller.ControllerProdutos;
import javax.swing.JOptionPane;

public class requisição {
    public static void main(String[] args) {

        System.out.println("iniciar a aplicação");
        int init = JOptionPane.showConfirmDialog(null,"Deseja iniciar aplicação", null, 0, 0, null);
        while(init == 0){
            ControllerProdutos a = new ControllerProdutos();
            init = JOptionPane.showConfirmDialog(null,"Deseja continuar aplicação", null, 0, 0, null);
        }
        System.out.println("Aplicação encerrada");
                                         
        

  

    }
}
