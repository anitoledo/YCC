package yucatancountryclub.com.ycc.Model;

import java.util.ArrayList;

/**
 * Created by Zazu on 21/02/18.
 */

public class DocumentCategory {
    private String name;
    private String description;
    private ArrayList<Document> documents;

    public DocumentCategory(String name, String description, ArrayList<Document> documents){
        this.name = name;
        this.description = description;
        this.documents = documents;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(ArrayList<Document> documents) {
        this.documents = documents;
    }

}
