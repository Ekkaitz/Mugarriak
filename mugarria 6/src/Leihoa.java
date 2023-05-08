import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Leihoa extends JFrame {

    private JComboBox argazkilariak;
    private JList jList;
    private JPanel batbat, batbi, bibat, bibi;
    private Konexioa konexioa;
    private DefaultComboBoxModel model;
    private DefaultListModel modelList;
    private JLabel irudia;

    Leihoa() {
        this.setLayout(new GridLayout(2, 2));
        Konexioa con=new Konexioa();

        //Jcombobox
        batbat = new JPanel();
        JLabel photographers = new JLabel("Photographer: ");
        model = new DefaultComboBoxModel<>();
        argazkilariak = new JComboBox<>();

        model=con.argazkilariak();

        this.argazkilariak.setModel(model);

        batbat.add(photographers);
        batbat.add(argazkilariak);

        //JXDatePicker
        batbi = new JPanel();
        JLabel data = new JLabel("Photos after: ");
        JXDatePicker datePicker = new JXDatePicker();


        batbi.add(datePicker);

        //argazkiak zerrenda
        bibat = new JPanel();
        jList = new JList<Picture>();

        bibat.add(jList);

        //argazkia
        bibi = new JPanel();
        irudia = new JLabel();



        bibi.add(irudia);

        //framera sartzen
        this.add(batbat);
        this.add(batbi);
        this.add(bibat);
        this.add(bibi);

        argazkilariak.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(argazkilariak.getSelectedItem().toString());
                DefaultListModel model1= new DefaultListModel();
                Date date=datePicker.getDate();
                model1=(con.zerrendaBete(argazkilariak.getSelectedItem().toString(),date));
                jList.setModel(model1);
            }
        });

        jList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String url;
                url="./img/"+jList.getSelectedValue().toString()+".jpg";

                if(e.getClickCount()==2){
                    ImageIcon image = new ImageIcon(url);
                    Image image1= image.getImage();
                    image1.getScaledInstance(300,300,Image.SCALE_SMOOTH);
                    ImageIcon imageIcon=new ImageIcon(image1);

                    irudia.setIcon(imageIcon);

                };
            }
        });


    }

    public static void main(String[] args) {
        Leihoa a = new Leihoa();
        a.setSize(800,500);
        a.setVisible(true);
    }
}