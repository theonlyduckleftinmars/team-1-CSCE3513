package model;

public class Player {
    private int id;
    private int score = 0;
    private String codeName;
    private int baseHits = 0;
    private boolean hitBase = false;

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
    public int getScore()
    {
		return score;
	}
	public void setScore(int score)
	{
		this.score = score;
	}

    public void setId(int id) {
        this.id = id;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }
	
}
