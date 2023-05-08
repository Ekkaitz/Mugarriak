public class Photographer {
    private int id;
    private String name;
    private boolean awarded;

    Photographer(int id,String name, boolean awarded){
        this.id=id;
        this.name=name;
        this.awarded=awarded;
    }

    public boolean isAwarded() {
        return awarded;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}
