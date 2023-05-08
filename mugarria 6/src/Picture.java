import java.util.Date;

public class Picture {
    private int id_pic;
    private String title;
    private String data;
    private String file;
    private int visits;
    private int id_photographer;

    Picture(int id, String title, Date data, String file, int visits, int id_photographer){
        this.id_pic=id;
        this.title=title;
        this.file=file;
        this.visits=visits;
        this.id_photographer=id_photographer;
    }

    public int getId_photographer() {
        return id_photographer;
    }

    public int getVisits() {
        return visits;
    }

    public String getFile() {
        return file;
    }

    public String getData() {
        return data;
    }

    public String getTitle() {
        return title;
    }

    public int getId_pic() {
        return id_pic;
    }

    @Override
    public String toString() {
        return title;
    }
}
