import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Leihoa extends JFrame {

    private JComboBox argazkilariak;
    private JList jList;
    private JPanel batbat,batbi,bibat,bibi;
    private Konexioa konexioa;
    private DefaultComboBoxModel model;
    private DefaultListModel modelList;

    Leihoa(){
        this.setLayout(new GridLayout(2,2));
        this.konexioa=new Konexioa();
        this.konexioa.konexioEgin();

        //Jcombobox
        batbat=new JPanel();
        JLabel photographers=new JLabel("Photographer: ");
        model=new DefaultComboBoxModel<>();
        argazkilariak=new JComboBox<>();

        argazkilariak=argazkilariak();

        batbat.add(photographers);
        batbat.add(argazkilariak);

        //JXDatePicker
        batbi =new JPanel();
        JLabel data=new JLabel("Photos after: ");
        JXDatePicker datePicker=new JXDatePicker();


        batbi.add(datePicker);

        //argazkiak zerrenda
        bibat=new JPanel();
        modelList=new DefaultListModel<>();
        jList=new JList<>();


        bibat.add(jList);

        //argazkia
        bibi=new JPanel();

        //framera sartzen
        this.add(batbat);
        this.add(batbi);
        this.add(bibat);
        this.add(bibi);

        argazkilariak.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zerrendaBete();
            }
        });


    }

    public JComboBox argazkilariak(){

        String select="select * from photographers";
        PreparedStatement preparedStatement;

        try {
            preparedStatement=konexioa.konexioa.prepareStatement(select);
            ResultSet resultSet=preparedStatement.executeQuery();

            while (resultSet.next()){
                int id=resultSet.getInt("photographer_id");
                String name=resultSet.getString("name");
                boolean award=resultSet.getBoolean("awarded");

                Photographer photographer=new Photographer(id,name,award);

                model.addElement(photographer.getName());
                //System.out.println(resultSet.getString("name"));
            }

            JComboBox<String> a=new JComboBox<>();
            a.setModel(model);

            return a;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void zerrendaBete(){
        JList<String> zerrenda=new JList<>();
        String artista;
        artista=(String) this.argazkilariak.getSelectedItem();


        String selecta="select title from pictures where photographer_id=?";

        PreparedStatement preparedStatementt;

        try {
            preparedStatementt=konexioa.konexioa.prepareStatement(selecta);
            preparedStatementt.setString(1,artista);
            ResultSet resultSett=preparedStatementt.executeQuery();

            while (resultSett.next()){
                int a=resultSett.getInt("picture_id");
                String b=resultSett.getString("title");
                String c=resultSett.getString("fecha");
                String d=resultSett.getString("file");
                int e=resultSett.getInt("visit");
                int f=resultSett.getInt("photographer_id");

                Picture picture=new Picture(a,b,c,d,e,f);

            }

            zerrenda.setModel(this.modelList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        this.jList=zerrenda;
    }


}
