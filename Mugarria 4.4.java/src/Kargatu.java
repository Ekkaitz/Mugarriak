import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Kargatu extends JFrame implements ActionListener {
    private JComboBox comboBox;
    private JLabel irudia;
    Kargatu(JComboBox a,JLabel b){
        this.comboBox=a;
        this.irudia=b;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String selekzioa=(String) comboBox.getSelectedItem();

        ImageIcon imageIcon=new ImageIcon("./img/"+selekzioa);
        Image image=imageIcon.getImage();
        Image image1=image.getScaledInstance(200,200,Image.SCALE_SMOOTH);
        ImageIcon imageIcon1=new ImageIcon(image1);

        irudia.setIcon(imageIcon1);

    }
}
