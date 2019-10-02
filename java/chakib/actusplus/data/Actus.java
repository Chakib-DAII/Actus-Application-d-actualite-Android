package chakib.actusplus.data;


import java.io.Serializable;

/**
 * Created by Chakib on 02/08/2017.
 */

public class Actus implements Serializable {
    private int id;
    private String titre, upperTitle, date, contenu, ressourceUrl, imageDrawable, imageBanner, imageHeader;

    //for main
    public Actus(int id, String titre, String upperTitle, String date, String imageDrawable) {
        this.id = id;
        this.titre = titre;
        this.upperTitle = upperTitle;
        this.date = date;
        this.imageDrawable = imageDrawable;
        this.contenu = null;
        this.ressourceUrl = null;
        this.imageBanner = null;
        this.imageHeader = null;
    }

    //for detail a modifier pour drawables
    public Actus(int id, String titre, String upperTitle, String date, String contenu, String ressourceUrl, String imageDrawable, String imageBanner, String imageHeader) {
        this.id = id;
        this.titre = titre;
        this.upperTitle = upperTitle;
        this.date = date;
        this.contenu = contenu;
        this.ressourceUrl = ressourceUrl;
        this.imageDrawable = imageDrawable;
        this.imageBanner = imageBanner;
        this.imageHeader = imageHeader;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getUpperTitle() {
        return upperTitle;
    }

    public void setUpperTitle(String upperTitle) {
        this.upperTitle = upperTitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getRessourceUrl() {
        return ressourceUrl;
    }

    public void setRessourceUrl(String ressourceUrl) {
        this.ressourceUrl = ressourceUrl;
    }

    public String getImageDrawable() {
        return imageDrawable;
    }

    public void setImageDrawable(String imageDrawable) {
        this.imageDrawable = imageDrawable;
    }

    public String getImageBanner() {
        return imageBanner;
    }

    public void setImageBanner(String imageBanner) {
        this.imageBanner = imageBanner;
    }

    public String getImageHeader() {
        return imageHeader;
    }

    public void setImageHeader(String imageHeader) {
        this.imageHeader = imageHeader;
    }

    @Override
    public String toString() {
        return "Actus{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", upperTitle='" + upperTitle + '\'' +
                ", date='" + date + '\'' +
                ", contenu='" + contenu + '\'' +
                ", ressourceUrl='" + ressourceUrl + '\'' +
                ", imageDrawable='" + imageDrawable + '\'' +
                ", imageBanner='" + imageBanner + '\'' +
                ", imageHeader='" + imageHeader + '\'' +
                '}';
    }
}
