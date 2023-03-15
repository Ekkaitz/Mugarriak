import javax.swing.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Programa extends JFrame {

    private ActionListener itxi;
    private ActionListener ezabatu;
    private ActionEvent aukera;
    private String[] option ={"python.txt","java.txt","c+.txt"};
    private JPanel panelW,panelE;
    private JButton bClear,bClose;
    private JTextArea textArea;
    private JScrollPane jScrollPane;



    public Programa(){
        this.setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//hau betirako


        //aukeren panela
        panelW=new JPanel();

        JComboBox lenguailabox=new JComboBox<>(option);
        lenguailabox.actionPerformed(aukera);
        panelW.add(lenguailabox);

        bClear=new JButton("Clear");
        panelW.add(bClear);

        ActionListener aukera=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String a=(String) lenguailabox.getSelectedItem();
                File file=new File(a);

                try {
                    BufferedReader reader=new BufferedReader(new FileReader(file));
                    String line;

                    textArea.setText(" ");
                    while ((line=reader.readLine()) !=null){
                        textArea.append(line+"\n");
                    }
                    reader.close();

                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
        };

        ActionListener ezabatu=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText(" ");
            }
        };

        bClear.addActionListener(ezabatu);
        lenguailabox.addActionListener(aukera);




        panelW.setSize(450,0);
        this.add(panelW,BorderLayout.WEST);
        this.setSize(900,500);


        //text area panela
        panelE=new JPanel();
        JPanel panel=new JPanel();

        textArea=new JTextArea(30,50);
        jScrollPane= new JScrollPane(panel);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        bClose=new JButton("Close");



        ActionListener itxi =new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());
                frame.dispose();
            }
        };

        bClose.addActionListener(itxi);

        panelE.setLayout(new BoxLayout(panelE,BoxLayout.Y_AXIS));

        panel.add(textArea);
        panelE.add(jScrollPane);
        panelE.add(bClose);

        this.add(panelE,BorderLayout.EAST);

    }

}
