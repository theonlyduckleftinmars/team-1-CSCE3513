package model;

public class Player {
    private int id;
    private String codeName;

    public Player(int id, String codeName) {
        this.id = id;
        this.codeName = codeName;
    }

    public int getId() {
        return id;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }
}
