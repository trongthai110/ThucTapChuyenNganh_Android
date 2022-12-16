package ndtt.myflix.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class Results(
    val backdrop_path: String,
    @PrimaryKey
    val id: Int,
    val overview: String,
    val poster_path: String,
    val release_date: String,
    val title: String,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(backdrop_path)
        parcel.writeInt(id)
        parcel.writeString(overview)
        parcel.writeString(poster_path)
        parcel.writeString(release_date)
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Results> {
        override fun createFromParcel(parcel: Parcel): Results {
            return Results(parcel)
        }

        override fun newArray(size: Int): Array<Results?> {
            return arrayOfNulls(size)
        }
    }
}