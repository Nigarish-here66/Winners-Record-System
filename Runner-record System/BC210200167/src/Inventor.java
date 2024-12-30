public class Inventor {
    private String name;
    private int score;
    private int id;

    public Inventor(int id, String name, int score) {
        this.id = id;
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public int getId() {
        return id;
    }
}
