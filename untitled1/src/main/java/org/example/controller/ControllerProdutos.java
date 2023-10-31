package org.example.controller;

import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.swing.JOptionPane;

import org.example.models.Produtos;

public class ControllerProdutos {

    private static Scanner input = new Scanner(System.in);
    EntityManagerFactory gerenciador = Persistence.createEntityManagerFactory("exercícios-jpa");
    EntityManager gerenciador_mestre = gerenciador.createEntityManager();

    private String br = "\n";

    public ControllerProdutos() {
        System.out.printf(
                "1-/create:criar produto %s2-/delete:deletar produto  %s3-/alter:alterar produto %s4-/select:selecionar produto %s5-/select*:selecionar todos os elementos%s6-/filter:filtra os precos%s/7-filterAlpha:filtragem por inicial do nome%s8-/media:Media dos preços%s%s",
                br, br, br, br, br, br, br, br, br);

        System.out.println("Digite algum comando");
        String option = input.nextLine();

        switch (option) {
            case "/create":
                adicionarProduto();
                break;
            case "/delete":
                removerProduto();
                break;
            case "/alter":
                alterarproduto();
                break;
            case "/select":
                selecionarProduto();
                break;
            case "/select*":
                selecionarTodosProdutos();
                break;
            case "/filter":
                filter();
                break;
            case "/media":
                mediaPrecos();
                break;
            case "/filterAlpha":
                filterAlpha();
                break;

            default:
                System.out.println("nenhum comando foi reconhecido");
        }
    }

    private void adicionarProduto() {
        try {
            String nome = JOptionPane.showInputDialog("qual nome do produto a ser inserido");
            Double preco = Double.parseDouble(JOptionPane.showInputDialog("qual o preço do produto"));

            boolean valor_negativo = preco > 0;
            boolean valor_vazio = nome.isBlank();
            boolean valor_nulo = nome == null;
            if (valor_negativo) {
                if (!valor_vazio) {
                    if (!valor_nulo) {
                        Produtos p = new Produtos(nome, preco);

                        gerenciador_mestre.persist(p);

                        gerenciador_mestre.getTransaction().begin();
                        gerenciador_mestre.getTransaction().commit();
                        gerenciador.close();
                        gerenciador_mestre.close();
                        JOptionPane.showMessageDialog(null, "Produto inserido", "inserção", JOptionPane.PLAIN_MESSAGE);
                    } else {
                        System.out.println("Este produto não possui nome");
                    }
                } else {
                    System.out.println("o produto possui o nome vazio");
                }
            } else {
                System.out.println("valor do preço é menor que zero");
            }

        } catch (NullPointerException nuler) {
            JOptionPane.showMessageDialog(null, "saída incorreta da aplicação", br, JOptionPane.ERROR_MESSAGE);

        } catch (NumberFormatException numberFormat) {
            JOptionPane.showMessageDialog(null, "preco inserido incorretamente", br, JOptionPane.ERROR_MESSAGE);
        }

    }

    private void removerProduto() {
        try {
            int SetKey = Integer.parseInt(JOptionPane.showInputDialog(null, "qual id será removido"));
            if (SetKey > 1) {
                Produtos p = new Produtos();
                p.setId(SetKey);

                gerenciador_mestre.getTransaction().begin();
                gerenciador_mestre.remove(gerenciador_mestre.merge(p));
                gerenciador_mestre.getTransaction().commit();

            } else {
                JOptionPane.showMessageDialog(null, "Esta chave não existe", "", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException numberFormatException) {
            JOptionPane.showMessageDialog(null, "Valor de id inserido incorretamente", "", JOptionPane.ERROR_MESSAGE);
        } catch (NullPointerException nuler) {
            JOptionPane.showMessageDialog(null, "saida incorreta da aplicação", "", JOptionPane.ERROR_MESSAGE);

        }

    }

    private void alterarproduto() {
        System.out.println("/name :alterar nome\n/preco :alterar preco");
        int id = Integer.parseInt(JOptionPane.showInputDialog(null, "digite o id do produto"));
        if(id > 0){
        
        System.out.println("qual sua operação");
        String operator = input.nextLine();

        Produtos produtos_R = gerenciador_mestre.find(Produtos.class, id);

        switch (operator) {
            case "/name : alterar nome":
                String nome = JOptionPane.showInputDialog(null, "qual o nome do produto ?");
                gerenciador_mestre.getTransaction().begin();
                produtos_R.setNome(nome);
                gerenciador_mestre.getTransaction().commit();
                break;
            case "/preco : alterar preco":
                double preco = Double.parseDouble(JOptionPane.showInputDialog(null, "qual o preco do produto ?"));
                gerenciador_mestre.getTransaction().begin();
                produtos_R.setPreco(preco);
                gerenciador_mestre.getTransaction().commit();
                break;
            default:
                System.out.println("comando não existe");
            }   
        }else{
            JOptionPane.showMessageDialog(null, "valor inserido não existe", br, id);
        }
    }

    private void selecionarProduto() {
        int id = Integer.parseInt(JOptionPane.showInputDialog(null, "digite o id do produto"));
        try {
            Produtos produtos_R = gerenciador_mestre.find(Produtos.class, id);
            JOptionPane.showMessageDialog(null, " id => " + produtos_R.getId() + "\n" + "Produto => "
                    + produtos_R.getNome() + "\n" + "preço =>" + produtos_R.getPreco() + "R$");
        } catch (NullPointerException nuller) {
            JOptionPane.showMessageDialog(null, "nome não encontrado", "error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void selecionarTodosProdutos() {
        List<Produtos> prodList = produtosList();

        System.out.println("-----------Produtos---------");
        prodList.forEach(p -> {
            System.out.println("id => " + p.getId() + "\n" + "Produto => " + p.getNome() + "\n" + "preço =>"
                    + p.getPreco() + "R$" + "\n");
        });
        System.out.println("------------------------------");
    }

    private void filter() {
        double preco = Integer.parseInt(JOptionPane.showInputDialog(null, "Qual o preco de parametro irá utilizar ?"));

        List<Produtos> prodList = produtosList();
        System.out.println("-------------Produtos filtrados------------");
        prodList.forEach(p -> {
            if (p.getPreco() > preco) {
                System.out.println("id => " + p.getId() + "\n" + "Produto => " + p.getNome() + "\n" + "preço =>"
                        + p.getPreco() + "R$" + "\n");
            }
        });
        System.out.println("-------------------------------------------");
    }

    private void filterAlpha() {
        String caracter = (JOptionPane.showInputDialog(null,
                "quais produtos você quer buscar com esta inicial ?"));

        List<Produtos> prodList = produtosList();
        if (caracter.length() == 0) {
            prodList.forEach((produto) -> {
                if (produto.getNome().toLowerCase().charAt(0) == caracter.toLowerCase().charAt(0)
                        && caracter.matches("[A-z]")) {
                    System.out.println("id => " + produto.getId() + "\n" + "Produto => " + produto.getNome() + "\n"
                            + "preço =>" + produto.getPreco() + "R$" + "\n");
                }
            });
        } else {
            JOptionPane.showMessageDialog(null, "Coloque um unico caracter", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mediaPrecos() {

        List<Produtos> produtos = produtosList();
        double somatorio = 0;
        for (int i = 0; i < produtos.size(); i++) {
            somatorio += produtos.get(i).getPreco();
        }
        System.out.printf("a media de precos é de %.2f \n", somatorio / produtos.size());
    }

    private List produtosList() {

        String Jpql = "select u from Produtos u";
        TypedQuery<Produtos> query = gerenciador_mestre.createQuery(Jpql, Produtos.class);
        java.util.List<Produtos> prodList = query.getResultList();

        return prodList;
    }
}
