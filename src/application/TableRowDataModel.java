package application;

import javafx.beans.property.StringProperty;

public class TableRowDataModel {
	public StringProperty Title;
	
	public  TableRowDataModel(StringProperty Title) {
		this.Title=Title;
	}
	public StringProperty TitleProperty() {
		return Title;
	}
}

