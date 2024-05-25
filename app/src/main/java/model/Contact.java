package model;

public class Contact {
    private String id; // ID unique du contact
    private String fullName; // Nom complet du contact
    private String profession; // Profession du contact
    private String telephone; // Numéro de téléphone du contact

    // Constructeur par défaut requis pour Firebase Firestore
    public Contact() {
    }

    // Constructeur avec nom complet, profession et numéro de téléphone
    public Contact(String fullName, String profession, String telephone) {
        this.fullName = fullName;
        this.profession = profession;
        this.telephone = telephone;
    }

    // Getter et Setter pour l'ID
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getter et Setter pour le nom complet
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // Getter et Setter pour la profession
    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    // Getter et Setter pour le numéro de téléphone
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
