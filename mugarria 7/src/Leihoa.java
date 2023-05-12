import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

public class Leihoa extends JFrame {

    private JComboBox argazkilariak;
    private JList jList;
    private JPanel batbat, batbi, bibat, bibi,zerobat,zerobi;
    private Konexioa konexioa;
    private DefaultComboBoxModel model;
    private DefaultListModel modelList;
    private JLabel irudia;
    private JButton baward,bremove;

    Leihoa() {
        this.setLayout(new GridLayout(3, 2));
        Konexioa con=new Konexioa();

        //Award Botoia
        zerobat=new JPanel();
        baward=new JButton("Awarded");


        zerobat.add(baward);

        //Remove botoia
        zerobi=new JPanel();
        bremove=new JButton("remove");

        zerobi.add(bremove);

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
        this.add(zerobat);
        this.add(zerobi);
        this.add(batbat);
        this.add(batbi);
        this.add(bibat);
        this.add(bibi);

        //Konexioa klasean dagoen metodoa deitzen dut. Bertan HashMap-a sortzen da.
        con.createVisitsMap();

        //Combobox-aren actoin listenerra, honen bidez argazkien jlist-a beteko da.

        argazkilariak.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(argazkilariak.getSelectedItem().toString());
                DefaultListModel model1;
                Date date=datePicker.getDate();
                model1=(con.zerrendaBete(argazkilariak.getSelectedItem().toString(),date));
                jList.setModel(model1);
            }
        });

        //
        jList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String url;
                url="./img/"+jList.getSelectedValue().toString()+".jpg";

                if(e.getClickCount()==2){

                    //bisitak hasieran 0-n egongo dira, irudian sakatzen den bakoitzean datu basean visitak+=1 egingo da
                    con.bisitakEguneratu(jList.getSelectedValue().toString());

                    ImageIcon image = new ImageIcon(url);
                    Image image1= image.getImage();
                    image1.getScaledInstance(30,30,Image.SCALE_SMOOTH);
                    ImageIcon imageIcon=new ImageIcon(image1);

                    irudia.setIcon(imageIcon);

                };
            }
        });

        //Zenbat bisita izan beharko ditu artista batek saria lortzeko
        baward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int bisitaKopurua= Integer.parseInt(JOptionPane.showInputDialog(null,"Zenbat bisita?"));

                con.sarituaUpdate(bisitaKopurua);

            }
        });

        bremove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                con.ezabatu();
            }
        });



    }



    public static void main(String[] args) {
        Leihoa a = new Leihoa();
        a.setSize(800,500);
        a.setVisible(true);
    }
}