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

    //Artisten zerrenda betetzeko, Jcombo modeo bat bueltatuko da.
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

                //Objetua sortu eta Jlist modelora pasa
                Photographer photographer=new Photographer(id,name,award);
                model.addElement(photographer.getName());

            }

            return model;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    //argazkien jlist modeloa bueltatzen duen metodoa, argazkilaria eta data erabiliko ditu kontsultak egiteko.
    public DefaultListModel zerrendaBete(String artista, Date data){
        DefaultListModel modelList =new DefaultListModel<>();

        //Bi select mota sortuko dira. Bat date aldagaia pasatzen bada eta bestea date hori pasatzen ez bada.
        String selectaWithoutDate = null, selectaWithDate=null;

        if (data==null){
            //Date gabeko kontsulta
            selectaWithoutDate="select * from pictures where photographer_id= (select photographer_id from photographers where name= ?)";
        }else {
            //Date erabilitako kontsulta
            selectaWithDate="select * from pictures where photographer_id= (select photographer_id from photographers where name= ?) and fecha < ?";
        }



        PreparedStatement preparedStatement;

        try {

            //Statement ezberdinak kontsultaren arabera
            if (data==null){
                preparedStatement=konexioa.prepareStatement(selectaWithoutDate);
            }else {
                preparedStatement=konexioa.prepareStatement(selectaWithDate);
                preparedStatement.setDate(2, ( new java.sql.Date(data.getTime())));
            }

            preparedStatement.setString(1,artista);
            ResultSet resultSett=preparedStatement.executeQuery();

            //Itzulitzako datuekin (Irudiak), objetu berriak sortzen
            while (resultSett.next()){
                int a=resultSett.getInt("picture_id");
                String b=resultSett.getString("title");
                Date c=resultSett.getDate("fecha");
                String d=resultSett.getString("file");
                int e=resultSett.getInt("visits");
                int f=resultSett.getInt("photographer_id");

                //Objetua sortu eta Jlist modelora pasa
                Picture picture=new Picture(a,b,c,d,e,f);
                modelList.addElement(picture);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //Artista eta data zehatz horren zerrendaren modeloa
        return modelList;

    }

    //Irudi bat pantailaratzen den bakoitzean, datu basean irudi horren bisitak 1 igoko dira
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

    //Bisitak artista bakoitzeko, HashMap honen Key-a artistaren IDa izango da eta balioa bisita kopurua
    public HashMap<Integer,Integer> createVisitsMap(){
        hashMap =new HashMap<>();

        //Artista bakoitzaren bisitak batuta
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

    //Bisita kopuru bat pasako zaio, artistaren bisita guztien batura kopuru hau baino handiagoa bada, sari bat emango zaio
    public void sarituaUpdate(int bisitak){
        String update="update photographers set photographers.awarded= photographers.awarded+1 where photographer_id=?";
        PreparedStatement preparedStatement;

        try {
            preparedStatement= konexioa.prepareStatement(update);

            Iterator<Map.Entry<Integer,Integer>>entry= hashMap.entrySet().iterator();

            //Map-a iteratuko da pasatutako kopurua eta artistak dituen bisita kopurua alderatuz
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

    //Metodo honen bidez, kondizio jakin batzuen mende, artistak ezabatzeko aukera emango zaio erabiltzaileari
    public void ezabatu(){

        // kondizio hori hau da Bisitak=0 eta Sariak=0
        String borratu="select * from pictures  INNER JOIN photographers ON Pictures.photographer_id = photographers.photographer_id where pictures.visits=0 and photographers.awarded=0";
        PreparedStatement preparedStatement;

        try {
            preparedStatement=konexioa.prepareStatement(borratu);

            ResultSet resultSet= preparedStatement.executeQuery();

            if (resultSet.next()){
                //Kontsultaren emaitza lerroz lerro iteratu
                while (resultSet.next()){
                    String photographer=resultSet.getString("name");
                    String title=resultSet.getString("title");

                    //Artista ezabatu nahi duen edo ez galdetuko du
                    int op=JOptionPane.showConfirmDialog(null,"Ezabatu nahi ahal duzu "+photographer+"ren "+title);

                    //Yes botoia sakatu badu ezabatu egingo da.
                    if (op == JOptionPane.YES_OPTION){

                        String del="delete from pictures where picture_id= ? ";
                        preparedStatement=konexioa.prepareStatement(del);
                        preparedStatement.setString(1,resultSet.getString("picture_id"));

                        preparedStatement.execute();

                    }

                }
            }else{
                //Kondizio betetzen duen inor ez balego, mezu hau pantailaratuko zen
                JOptionPane.showMessageDialog(null,"ez daude kondizioak betetzen dituzten argazkilariak");
            }



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }










}
