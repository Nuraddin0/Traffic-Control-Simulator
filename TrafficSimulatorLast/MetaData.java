

public class MetaData {
	private	double sceneWidth ; 
	private	double sceneHeight;
	private	int numberOfXCell;
	private	int numberOfYCell;
	private	int numberOfPaths;
	private	int winCondition ; 
	private	int allowedAccident;
	private double gridWidth;
	private double gridHeight;
	public MetaData(double sceneWidth, double sceneHeight, int numberOfXCell, int numberOfYCell, int numberOfPaths,
		int winCondition, int allowedAccident) 
	{
		setSceneWidth(sceneWidth);
		setSceneHeight(sceneHeight);
		setNumberOfXCell(numberOfXCell);
		setNumberOfYCell(numberOfYCell);
		setNumberOfPaths(numberOfPaths);
		setWinCondition(winCondition);
		setAllowedAccident(allowedAccident);
		this.gridWidth = sceneWidth / numberOfXCell;
		this.gridHeight = sceneHeight / numberOfYCell;
	}
	public double getGridWidth() {
		return gridWidth;
	}
	public void setGridWidth(double gridWidth) {
		this.gridWidth = gridWidth;
	}
	public double getGridHeight() {
		return gridHeight;
	}
	public void setGridHeight(double gridHeight) {
		this.gridHeight = gridHeight;
	}
	public double getSceneWidth() {
		return sceneWidth;
	}
	public void setSceneWidth(double sceneWidth) {
		this.sceneWidth = sceneWidth;
	}
	public double getSceneHeight() {
		return sceneHeight;
	}
	public void setSceneHeight(double sceneHeight) {
		this.sceneHeight = sceneHeight;
	}
	public int getNumberOfXCell() {
		return numberOfXCell;
	}
	public void setNumberOfXCell(int numberOfXCell) {
		this.numberOfXCell = numberOfXCell;
	}
	public int getNumberOfYCell() {
		return numberOfYCell;
	}
	public void setNumberOfYCell(int numberOfYCell) {
		this.numberOfYCell = numberOfYCell;
	}
	public int getNumberOfPaths() {
		return numberOfPaths;
	}
	public void setNumberOfPaths(int numberOfPaths) {
		this.numberOfPaths = numberOfPaths;
	}
	public int getWinCondition() {
		return winCondition;
	}
	public void setWinCondition(int winCondition) {
		this.winCondition = winCondition;
	}
	public int getAllowedAccident() {
		return allowedAccident;
	}
	public void setAllowedAccident(int allowedAccident) {
		this.allowedAccident = allowedAccident;
	}

	    
}


