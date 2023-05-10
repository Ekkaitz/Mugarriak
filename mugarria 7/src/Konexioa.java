import javax.swing.*;
import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Konexioa {
    public Connection konexioa;
    public Statement sententzia;

    private String erabiltzailea="root";
    private String pasahitza="zubiri";
    private ListModel<String> modelList;
    private Map<Integer, Integer> hashMap;



    public Konexioa(){

        try {

            final String url="jdbc:mysql://localhost:3306/mugarria6";
            konexioa= DriverManager.getConnection(url,erabiltzailea,pasahitza);
            sententzia=konexioa.createStatement();
            System.out.println("Konektatuta");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public DefaultComboBoxModel argazkilariak(){
        DefaultComboBoxModel model=new DefaultComboBoxModel<>();
        String select="select * from photographers";
        PreparedStatement preparedStatement;

        try {
            preparedStatement=konexioa.prepareStatement(select);
            ResultSet resultSet=preparedStatement.executeQuery();

            while (resultSet.next()){
                int id=resultSet.getInt("photographer_id");
                String name=resultSet.getString("name");
                boolean award=resultSet.getBoolean("awarded");

                Photographer photographer=new Photographer(id,name,award);

                model.addElement(photographer.getName());
                //System.out.println(resultSet.getString("name"));
            }

            return model;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public DefaultListModel zerrendaBete(String artista, Date data){
        DefaultListModel modelList =new DefaultListModel<>();


        String selecta="select * from pictures where photographer_id= (select photographer_id from photographers where name= ?) and fecha < ?";

        PreparedStatement preparedStatement;

        try {
            preparedStatement=konexioa.prepareStatement(selecta);
            preparedStatement.setString(1,artista);
            preparedStatement.setDate(2, ( new java.sql.Date(data.getTime())));
            ResultSet resultSett=preparedStatement.executeQuery();

            while (resultSett.next()){
                int a=resultSett.getInt("picture_id");
                String b=resultSett.getString("title");
                Date c=resultSett.getDate("fecha");
                String d=resultSett.getString("file");
                int e=resultSett.getInt("visits");
                int f=resultSett.getInt("photographer_id");

                Picture picture=new Picture(a,b,c,d,e,f);
                modelList.addElement(picture);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return modelList;

    }

    public void bisitakEguneratu(String izena){
        String update="update pictures set visits= visits +1 where pictures.title= ? ";
        PreparedStatement preparedStatement;

        try {
            preparedStatement= konexioa.prepareStatement(update);
            preparedStatement.setString(1,izena);
            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public HashMap<Integer,Integer> createVisitsMap(){
        hashMap =new HashMap<>();

        String selecta="select photographer_id, sum(visits) as total_visits from pictures group by photographer_id";

        PreparedStatement preparedStatement;

        try {
            preparedStatement=konexioa.prepareStatement(selecta);
            ResultSet resultSet=preparedStatement.executeQuery();

            while (resultSet.next()){
                int idPhot=resultSet.getInt("photographer_id");
                int visits=resultSet.getInt("total_visits");

                hashMap.put(idPhot,visits);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return (HashMap<Integer, Integer>) hashMap;
    }

    public void sarituaUpdate(int bisitak){
        String update="update photographers set photographers.awarded= photographers.awarded+1 where photographer_id=?";
        PreparedStatement preparedStatement;

        try {
            preparedStatement= konexioa.prepareStatement(update);

            Iterator<Map.Entry<Integer,Integer>>entry= hashMap.entrySet().iterator();

            while (entry.hasNext()){
                Map.Entry<Integer,Integer> entry1=entry.next();

                if (entry1.getValue()>bisitak){
                    preparedStatement.setString(1, String.valueOf(entry1.getKey()));
                    preparedStatement.executeUpdate();
                }

            }

            JOptionPane.showMessageDialog(null, "Sari guztiak eman dira");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void ezabatu(){
        System.out.println(1);
        String borratu="select * from pictures  INNER JOIN photographers ON Pictures.photographer_id = photographers.photographer_id where pictures.visits=0 and photographers.awarded=0";
        PreparedStatement preparedStatement;

        try {
            preparedStatement=konexioa.prepareStatement(borratu);

            ResultSet resultSet= preparedStatement.executeQuery();
            System.out.println(2);

            if (resultSet.next()){

                System.out.println(3);
                while (resultSet.next()){
                    System.out.println(4);
                    String photographer=resultSet.getString("name");
                    String title=resultSet.getString("title");

                    int op=JOptionPane.showConfirmDialog(null,"Ezabatu nahi ahal duzu "+photographer+"ren "+title);

                    if (op == JOptionPane.YES_OPTION){

                        String del="delete from pictures where picture_id= ? ";
                        preparedStatement=konexioa.prepareStatement(del);
                        preparedStatement.setString(1,resultSet.getString("picture_id"));

                        preparedStatement.execute();

                    }

                }
            }else{
                JOptionPane.showMessageDialog(null,"ez daude kondizioak betetzen dituzten argazkilariak");
            }



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }










}
