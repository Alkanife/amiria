package fr.alkanife.amiria.characters;

public class Character {

    private String full_name;
    private String title;
    private String race;
    private String age;
    private String sex;
    private String floor;
    private String specializations;
    private String weapons;
    private String character_traits;
    private String aspect;
    private String own_power;
    private String state;
    private String original_owner;
    private String owner_inspirations;
    private String owner_notes;
    private boolean representation_image;

    public Character() {
    }

    public Character(String full_name, String title, String race, String age, String sex, String floor, String specializations, String weapons, String character_traits, String aspect, String own_power, String state, String original_owner, String owner_inspirations, String owner_notes, boolean representation_image) {
        this.full_name = full_name;
        this.title = title;
        this.race = race;
        this.age = age;
        this.sex = sex;
        this.floor = floor;
        this.specializations = specializations;
        this.weapons = weapons;
        this.character_traits = character_traits;
        this.aspect = aspect;
        this.own_power = own_power;
        this.state = state;
        this.original_owner = original_owner;
        this.owner_inspirations = owner_inspirations;
        this.owner_notes = owner_notes;
        this.representation_image = representation_image;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getSpecializations() {
        return specializations;
    }

    public void setSpecializations(String specializations) {
        this.specializations = specializations;
    }

    public String getWeapons() {
        return weapons;
    }

    public void setWeapons(String weapons) {
        this.weapons = weapons;
    }

    public String getCharacter_traits() {
        return character_traits;
    }

    public void setCharacter_traits(String character_traits) {
        this.character_traits = character_traits;
    }

    public String getAspect() {
        return aspect;
    }

    public void setAspect(String aspect) {
        this.aspect = aspect;
    }

    public String getOwn_power() {
        return own_power;
    }

    public void setOwn_power(String own_power) {
        this.own_power = own_power;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOriginal_owner() {
        return original_owner;
    }

    public void setOriginal_owner(String original_owner) {
        this.original_owner = original_owner;
    }

    public String getOwner_inspirations() {
        return owner_inspirations;
    }

    public void setOwner_inspirations(String owner_inspirations) {
        this.owner_inspirations = owner_inspirations;
    }

    public String getOwner_notes() {
        return owner_notes;
    }

    public void setOwner_notes(String owner_notes) {
        this.owner_notes = owner_notes;
    }

    public boolean isRepresentation_image() {
        return representation_image;
    }

    public void setRepresentation_image(boolean representation_image) {
        this.representation_image = representation_image;
    }
}
