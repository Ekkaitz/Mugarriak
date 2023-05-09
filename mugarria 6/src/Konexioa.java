import javax.swing.*;
import java.sql.*;
import java.util.Date;

public class Konexioa {
    public Connection konexioa;
    public Statement sententzia;

    private String erabiltzailea="root";
    private String pasahitza="zubiri";
    private ListModel<String> modelList;



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
        DefaultListModel <Picture> modelList =new DefaultListModel<>();


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










}
