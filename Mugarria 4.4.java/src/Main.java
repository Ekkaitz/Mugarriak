import javax.swing.*;
import java.awt.*;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main extends JFrame {

    private JPanel pCombobox,south,west,east;
    private JTextArea textArea;
    private JComboBox<String> comboBox;
    private JLabel labelImg;
    private JCheckBox checkBox;
    private Button bsave;

    Main(){
        this.setSize(500,500);
        this.setLayout(new BorderLayout());

        //ezkerreko panela
        west=new JPanel();
        west.setLayout(new BoxLayout(west,BoxLayout.Y_AXIS));

            //combobox
        //String [] a={"red.png","blue.png","green.png"};

        comboBox=new JComboBox<>();
        load_combo();
        pCombobox=new JPanel();
        pCombobox.add(comboBox);
        west.add(pCombobox);





            //checkbox
        checkBox=new JCheckBox("save your comment",true);
        west.add(checkBox);

        this.add(west,BorderLayout.WEST);

        //eskuineko zatia
        ImageIcon imageIcon=new ImageIcon("./img/red.png");
        Image image=imageIcon.getImage();
        Image image1=image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
        ImageIcon imageIcon1=new ImageIcon(image1);

        this.labelImg=new JLabel(imageIcon1);
        JPanel panelImg=new JPanel();


        east=new JPanel();
        east.setLayout(new BoxLayout(east,BoxLayout.Y_AXIS));
        panelImg.add(labelImg);
        east.add(panelImg);

        textArea=new JTextArea();
        textArea.setPreferredSize(new Dimension(200,50));
        east.add(textArea);
        this.add(east,BorderLayout.EAST);



        //beheko zatia
        bsave=new Button("Save");
        south=new JPanel();
        south.add(bsave);
        this.add(south,BorderLayout.SOUTH);

        //listeners

        comboBox.addActionListener(new Kargatu(comboBox,labelImg));

        bsave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String namee=(String) comboBox.getSelectedItem();
                int length=namee.length();
                int x=length-4;//.png kentzeko
                String name = namee;
                name=name.substring(0,x);//hemen bakarrik kolorearen izena daukagu .png gabe
                name=name+".txt";

                if (checkBox.isSelected()){
                    try {
                        FileOutputStream out=new FileOutputStream(name);
                        String textua=textArea.getText();

                        out.write(textua.getBytes());
                        out.write("\n".getBytes());

                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }



            }
        });

    }

    private void load_combo() {
        File carpeta = new File("./img" );
        File[] archivos = carpeta.listFiles();
        for (File archivo : archivos) {
            if (archivo.isFile()) {
                comboBox.addItem(archivo.getName());
            }
        }
    }







    public static void main(String[] args) {
        //String optionpane=JOptionPane.showInputDialog("Input password");

        String password = JOptionPane.showInputDialog(null, "Input password:", "Contraseña", JOptionPane.QUESTION_MESSAGE);

        if (password.equals("damocles")||password.equals("1")) {
            Main a=new Main();
            a.setVisible(true);
        } else {
           JOptionPane.showMessageDialog(null,"pasahitza gaizki sartu duzu");
        }

    }

}