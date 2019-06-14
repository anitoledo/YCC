package yucatancountryclub.com.ycc.Model;

/**
 * Created by Zazu on 06/12/18.
 */

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Zazu on 06/12/18.
 */

public class Document implements Parcelable {
    // You can include parcel data types
    private String title;
    private String description;
    private String document;


    // This is where you write the values you want to save to the `Parcel`.
    // The `Parcel` class has methods defined to help you save all of your values.
    // Note that there are only methods defined for simple values, lists, and other Parcelable objects.
    // You may need to make several classes Parcelable to send the data you want.
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(title);
        out.writeString(description);
        out.writeString(document);
    }

    // Using the `in` variable, we can retrieve the values that
    // we originally wrote into the `Parcel`.  This constructor is usually
    // private so that only the `CREATOR` field can access.
    public Document(Parcel in) {
        title = in.readString();
        description = in.readString();
        document = in.readString();
    }

    // Constructor
    public Document(String title, String description, String document){
        this.title = title;
        this.description = description;
        this.document = document;
    }

    public String getTitle(){
        return this.title;
    }
    public String getDescription(){
        return this.description;
    }
    public String getDocument(){
        return this.document;
    }

    // In the vast majority of cases you can simply return 0 for this.
    // There are cases where you need to use the constant `CONTENTS_FILE_DESCRIPTOR`
    // But this is out of scope of this tutorial
    @Override
    public int describeContents() {
        return 0;
    }

    // After implementing the `Parcelable` interface, we need to create the
    // `Parcelable.Creator<MyParcelable> CREATOR` constant for our class;
    // Notice how it has our class specified as its type.
    public static final Parcelable.Creator<Document> CREATOR
            = new Parcelable.Creator<Document>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Document createFromParcel(Parcel in) {
            return new Document(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Document[] newArray(int size) {
            return new Document[size];
        }
    };
}

