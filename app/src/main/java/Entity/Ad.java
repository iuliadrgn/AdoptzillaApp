package Entity;

public class Ad
{

    public Ad(int ad_id, String adresa, String description, String nr_Telefon, String rasa) {
        Ad_id = ad_id;
        Adresa = adresa;
        Description = description;
        Nr_Telefon = nr_Telefon;
        Rasa = rasa;
    }

    public int getAd_id() {
        return Ad_id;
    }

    public void setAd_id(int ad_id) {
        Ad_id = ad_id;
    }

    public String getAdresa() {
        return Adresa;
    }

    public void setAdresa(String adresa) {
        Adresa = adresa;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getNr_Telefon() {
        return Nr_Telefon;
    }

    public void setNr_Telefon(String nr_Telefon) {
        Nr_Telefon = nr_Telefon;
    }

    public String getRasa() {
        return Rasa;
    }

    public void setRasa(String rasa) {
        Rasa = rasa;
    }

    public int Ad_id;
    public String Adresa;
    public String Description;
    public String Nr_Telefon;
    public String Rasa;

}
